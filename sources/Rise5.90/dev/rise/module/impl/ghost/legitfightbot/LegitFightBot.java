/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.ghost.legitfightbot;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.combat.AntiBot;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.player.RotationUtil;
import dev.rise.util.world.BlockUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ModuleInfo(name = "LegitFightBot", description = "A more legit version of Fight Bot", category = Category.GHOST)
public final class LegitFightBot extends Module {

    private final NoteSetting targets = new NoteSetting("Targets", this);
    private final BooleanSetting players = new BooleanSetting("Players", this, true);
    private final BooleanSetting nonPlayers = new BooleanSetting("Non Players", this, true);
    private final BooleanSetting teams = new BooleanSetting("Ignore Teammates", this, true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", this, false);
    private final BooleanSetting dead = new BooleanSetting("Attack Dead", this, false);
    private final BooleanSetting ignoreNPCs = new BooleanSetting("Ignore NPCs", this, true);

    EntityLivingBase target;
    boolean forward, right, left, backward, attack, jump, sneak, build;
    int selectedSlot;
    boolean strafingRight;
    boolean targetRunningAway;
    int ticksRunningAway;
    BotState botState;
    double positionMovingTowardsX, positionMovingTowardsY, positionMovingTowardsZ;
    int ticksStandingOnEdge;
    int ticksPotting, ticksSincePotting;
    boolean potting;
    boolean rotatingForPots;
    int ticksHoldingPearl;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        /*
        resetting movement booleans, because we don't want previous inputs to carry over to this event.
         */
        forward = right = left = backward = attack = jump = sneak = build = false;

        target = getTarget();

        /*
        If target == null we return as there is no need to run any code
         */
        if (target == null) {
            setMovements();
            return;
        }

        /*
        Checks if the target is running away so we can make the bot smarter in some situations
         */
        final double lookX = target.posX - mc.thePlayer.posX;
        final double lookY = target.posZ - mc.thePlayer.posZ;

        float yaw = (float) (Math.atan2(lookY, lookX) * 180.0D / Math.PI) - 90.0F;
        yaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);

        if (Math.abs(yaw - MathHelper.wrapAngleTo180_float(target.rotationYaw)) < 90
                && target.getDistance(target.lastTickPosX, target.posY, target.lastTickPosZ) > 0.18) {
            ticksRunningAway++;
        } else {
            ticksRunningAway = 0;
        }

        targetRunningAway = ticksRunningAway > 4;

        if (botState == BotState.GENERAL) {
            onGeneralSate();
            avoidEdge();
        }

        if (botState == BotState.BRIDGE) {
            onBridgeState();
        }

        /*
        Makes the bot automatically pot if it has pots
         */
        final Integer potionSlot = getPotions();
        if (potionSlot != null) {

            final double health = mc.thePlayer.getHealth();

            if (health <= 16 && !distanceToTargetLessThan(7) && ticksPotting > 5) {
                potting = true;
                selectedSlot = potionSlot;
                rotatingForPots = true;
                if (ticksPotting > 11) ticksPotting = 0;
            }

            if (health <= 12 && !distanceToTargetLessThan(4) && targetRunningAway && ticksPotting > 5) {
                potting = true;
                selectedSlot = potionSlot;
                if (ticksPotting > 11) ticksPotting = 0;
            }

            if (health <= 7) {
                potting = true;
                selectedSlot = potionSlot;
                rotatingForPots = true;
                if (ticksPotting > 11) ticksPotting = 0;
            }

            if (ticksPotting == 10) {
                build = true;
            }
        }

        if (ticksPotting == 11) {
            potting = false;
            rotatingForPots = false;
        }

        if (!potting && ticksHoldingPearl == 0) {
            final Integer swordSlot = getSword();
            if (swordSlot != null) selectedSlot = swordSlot;
        }

        if (potting) {
            ticksPotting++;
            ticksSincePotting = 0;
        } else {
            ticksPotting = 0;
            ticksSincePotting++;
        }

        /*
        Makes you look at the target depending on the situation
         */
        final float[] rotations = getRotations();
        mc.thePlayer.rotationYaw = rotations[0];
        mc.thePlayer.rotationPitch = rotations[1];


        setMovements();
    }

    public Integer getPotions() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && mc.currentScreen == null) {

                final Item item = itemStack.getItem();

                if (item instanceof ItemPotion) {

                    final ItemPotion p = (ItemPotion) item;
                    if (ItemPotion.isSplash(itemStack.getMetadata()) && p.getEffects(itemStack.getMetadata()) != null) {
                        final int potionID = p.getEffects(itemStack.getMetadata()).get(0).getPotionID();

                        /*Checks if potionID == 6 which is health potion ID*/
                        if (potionID == 6) {
                            return i - 36;
                        }
                    }
                }
            }
        }
        return null;
    }

    public Integer getSword() {

        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && mc.currentScreen == null) {
                if (itemStack.getItem() instanceof ItemSword || itemStack.getItem().equals(Items.stick)) {
                    return i - 36;
                }
            }
        }

        return null;
    }

    public Integer getPearl() {

        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && mc.currentScreen == null) {
                if (itemStack.getItem() instanceof ItemEnderPearl) {
                    return i - 36;
                }
            }
        }

        return null;
    }

    public void avoidEdge() {
        /*
        Makes it so the bot can jump up blocks or change direction depending on the situation
         */
        final double direction = MoveUtil.getDirection();

        final double x = -Math.sin(direction) * 0.9;
        final double z = Math.cos(direction) * 0.9;

        if (mc.thePlayer.isCollidedHorizontally
                && !(PlayerUtil.getBlockRelativeToPlayer(x, 0.5, z) instanceof BlockAir)
                && PlayerUtil.getBlockRelativeToPlayer(x, 1.1, z) instanceof BlockAir) {

            if (mc.thePlayer.onGround) {
                jump = true;
            }
        }

        /*
        Makes it so you can't walk off block/s
         */

        final double direction2 = MoveUtil.getDirection();
        final double posX = -Math.sin(direction2) * 4.9;
        final double posZ = Math.cos(direction2) * 4.9;
        final double posX2 = -Math.sin(direction2) * 2.1;
        final double posZ2 = Math.cos(direction2) * 2.1;
        final double posX3 = -Math.sin(direction2) * 1.1;
        final double posZ3 = Math.cos(direction2) * 1.1;
        if (PlayerUtil.getBlockRelativeToPlayer(posX, -3, posZ) instanceof BlockAir
                && (PlayerUtil.getBlockRelativeToPlayer(posX2, (mc.thePlayer.onGround) ? -1 : -1.3, posZ2) instanceof BlockAir
                || PlayerUtil.getBlockRelativeToPlayer(posX3, (mc.thePlayer.onGround) ? -1 : -1.3, posZ3) instanceof BlockAir)) {
            forward = false;
            right = false;
            left = false;
            jump = false;
            strafingRight = !strafingRight;

//            Rise.addChatMessage("Detected edge");

            final Integer pearlSlot = getPearl();
            if (pearlSlot != null) {
                selectedSlot = pearlSlot;
                ticksHoldingPearl++;
//                Rise.addChatMessage("Switched Slot");
            } else {
                ticksHoldingPearl = 0;
            }

            if (ticksHoldingPearl == 2) {
                build = true;
            }

            if (mc.thePlayer.onGround) {
                for (int i = 0; i <= 5; i++) {

                    final BlockPos pos1 = new BlockPos(mc.thePlayer.posX + 0.3 + posX3, mc.thePlayer.posY - i, mc.thePlayer.posZ + posZ3);
                    final BlockPos pos2 = new BlockPos(mc.thePlayer.posX - 0.3 + posX3, mc.thePlayer.posY - i, mc.thePlayer.posZ + posZ3);
                    final BlockPos pos3 = new BlockPos(mc.thePlayer.posX + posX3, mc.thePlayer.posY - i, mc.thePlayer.posZ + 0.3 + posZ3);
                    final BlockPos pos4 = new BlockPos(mc.thePlayer.posX + posX3, mc.thePlayer.posY - i, mc.thePlayer.posZ - 0.3 + posZ3);

                    if (!(mc.theWorld.getBlockState(pos1).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(pos2).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(pos3).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(pos4).getBlock() instanceof BlockAir)) {
                        forward = true;
                        right = false;
                        left = false;
                        Rise.addChatMessage("Detected landing spot, walking forward");
                    }

                }
            }

            if (forward) {
                ticksStandingOnEdge = 0;
            } else {
                ticksStandingOnEdge++;
            }

            if (ticksStandingOnEdge > 10 && !distanceToTargetLessThan(6) && !mc.thePlayer.isCollidedHorizontally) {

                if (ticksStandingOnEdge > 10) {
                    forward = true;
                }

                /*
                Sets the position the bot will build to
                 */
                final double dir = Math.toRadians(mc.thePlayer.rotationYaw);
                for (int i = 0; i < 30; i++) {


                    final double x2 = -Math.sin(dir) * 1 * i;
                    final double z2 = Math.cos(dir) * 1 * i;

                    if (i > 2) {
                        if (!(PlayerUtil.getBlockRelativeToPlayer(x2, -1, z2) instanceof BlockAir)) {
                            positionMovingTowardsX = mc.thePlayer.posX + x2;
                            positionMovingTowardsY = mc.thePlayer.posY;
                            positionMovingTowardsZ = mc.thePlayer.posZ + z2;
                        }
                    }
                }

            }
        } else {
            ticksStandingOnEdge = 0;
        }
    }

    public void onBridgeState() {

        if (mc.thePlayer.onGround && PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir) {
            sneak = true;
        }

        backward = true;
        build = true;

        /*
        Reverting back to default mode
         */
        if (mc.thePlayer.getDistance(positionMovingTowardsX, positionMovingTowardsY, positionMovingTowardsZ) < 2 || distanceToTargetLessThan(4) || (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getDistance(positionMovingTowardsX, positionMovingTowardsY, positionMovingTowardsZ) < 6)) {
            botState = BotState.GENERAL;
            backward = forward = false;
        }
    }

    public void onGeneralSate() {
        /*
        Swinging at the player if distance < 6
         */
        if (distanceToTargetLessThan(6)) {
            if (Math.random() > 0.4) {
                attack = true;
            }

            if (strafingRight) {
                right = true;
            } else {
                left = true;
            }

            if (Math.random() > 0.98) {
                strafingRight = !strafingRight;
            }
        }

        /*
        Makes it so the bot sprint jumps at the player when are away from him
         */
        if (!distanceToTargetLessThan(10) || targetRunningAway) {
            if (mc.thePlayer.onGround) {
                if (!(mc.thePlayer.isPotionActive(Potion.moveSpeed) && mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 >= 2)) {
                    jump = true;
                }
            }
        }

        /*
        Chooses random direction to strafe in when taking damage
         */
        if (mc.thePlayer.hurtTime == 9) {
            strafingRight = Math.random() > 0.5;
        }

        /*
        Makes it so the bot doesn't walk off edges whilst strafing
         */
        if (PlayerUtil.getBlockRelativeToPlayer(mc.thePlayer.motionX * 30, -3, mc.thePlayer.motionZ * 30) instanceof BlockAir
                && !(PlayerUtil.getBlockRelativeToPlayer(mc.thePlayer.motionX * 30, -3, mc.thePlayer.motionZ * 30) instanceof BlockAir)) {
            strafingRight = !strafingRight;
        }

        /*
        Makes the player move towards the target unless its to close to the target
         */
        if (!distanceToTargetLessThan(1.5)) {
            forward = true;
        } else {
            backward = true;
            forward = false;
        }
    }

    public float[] getRotations() {
        double predictValue = 0.2;
        float headSpeed = 3;

        if (target.getDistance(target.lastTickPosX, target.lastTickPosY, target.lastTickPosZ) > 3) {
            headSpeed = 0.3f;
        }

        /*
        Makes it so the bot predicts ahead of the player, if the player is far away or running away
         */
        float yaw = 0;
        float pitch = 0;

        if (botState == null) return new float[]{yaw, pitch};

        switch (botState) {

            case GENERAL: {

                if ((targetRunningAway && !distanceToTargetLessThan(7)) || !distanceToTargetLessThan(19)) {
                    predictValue = 15;
                    headSpeed = 1;
                }

                final double lookX = (target.posX - (target.lastTickPosX - target.posX) * predictValue) + 0.01 - mc.thePlayer.posX;
                final double lookZ = (target.posZ - (target.lastTickPosZ - target.posZ) * predictValue) + 0.01 - mc.thePlayer.posZ;

                double closestPointOnYAxis = (mc.thePlayer.posY - target.posY);
                if (closestPointOnYAxis < -1.4) closestPointOnYAxis = -1.4;
                if (closestPointOnYAxis > 0.1) closestPointOnYAxis = 0.1;

                final double lookY = (target.posY) + 0.4 + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + closestPointOnYAxis;

                final double var14 = MathHelper.sqrt_double(lookX * lookX + lookZ * lookZ);

                yaw = (float) (Math.atan2(lookZ, lookX) * 180.0D / Math.PI) - 90.0F;
                pitch = (float) -(Math.atan2(lookY, var14) * 180.0D / Math.PI);

                if (rotatingForPots) {
                    yaw -= 150;
                    pitch = (float) (70 + Math.random());
                } else if (potting) {
                    pitch = (float) (70 + Math.random());
                }

                break;
            }

            case BRIDGE: {

                final float[] rotations = BlockUtil.getDirectionToBlock(positionMovingTowardsX, positionMovingTowardsY, positionMovingTowardsZ, EnumFacing.UP);

                yaw = rotations[0] - 180;
                pitch = (float) (81 + (Math.random() - 0.5) * 35);

                break;
            }
        }

        /*
        This makes the rotations look more legit
         */
        if (mc.thePlayer.isSwingInProgress) {
            yaw += (Math.random() - 0.5) * 2;
            pitch += (Math.random() - 0.5) * 2;
        }

        yaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        pitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);

        /*
        Makes it so the bots headspeed is more precise in combat
         */
        if (mc.thePlayer.isSwingInProgress) {
            headSpeed = 6;
        }

        /*
        Makes rotations extra slow whilst potting
         */

        if (ticksSincePotting < 10) {
            yaw = (mc.thePlayer.prevRotationYaw * 3 + yaw) / 4;
            pitch = (mc.thePlayer.prevRotationPitch * 3 + pitch) / 4;
        } else {
            yaw = (mc.thePlayer.prevRotationYaw + yaw * headSpeed) / (headSpeed + 1);
            pitch = (mc.thePlayer.prevRotationPitch + pitch * headSpeed) / (headSpeed + 1);
        }

        /*
         * Gets the current and last rotations and smooths them for aura to be harder to flag.
         */
        final float[] rotations = new float[]{yaw, pitch};
        final float[] lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};

        final float[] fixedRotations = RotationUtil.getFixedRotation(rotations, lastRotations);

        yaw = fixedRotations[0];
        pitch = fixedRotations[1];

        // Clamps the pitch so that aura doesn't flag everything with an illegal rotation.
        pitch = MathHelper.clamp_float(pitch, -90.0F, 90.0F);

        return new float[]{yaw, pitch};
    }

    public boolean distanceToTargetLessThan(final double distance) {
        return mc.thePlayer.getDistanceToEntity(target) < distance;
    }

    /*
    This function is called on onPreMotion to set keyboard inputs to move depending on forward, right, left, backward, attack booleans
     */
    public void setMovements() {
        final GameSettings gameSettings = mc.gameSettings;

        gameSettings.keyBindForward.setKeyPressed(forward);
        gameSettings.keyBindRight.setKeyPressed(right);
        gameSettings.keyBindLeft.setKeyPressed(left);
        gameSettings.keyBindBack.setKeyPressed(backward);
        gameSettings.keyBindJump.setKeyPressed(jump);
        gameSettings.keyBindSneak.setKeyPressed(sneak);
        if (build) mc.rightClickMouse();
        if (attack) mc.clickMouse();
        mc.thePlayer.inventory.currentItem = selectedSlot;
    }

    public EntityLivingBase getTarget() {

        final List<EntityLivingBase> nigger = mc.theWorld.loadedEntityList
                // Stream our entity list.
                .stream()

                // Only get living entities so we don't have to check for items on ground etc.
                .filter(entity -> entity instanceof EntityLivingBase)

                // Map our entities to entity living base as we have filtered out none living entities.
                .map(entity -> ((EntityLivingBase) entity))

                // Only get the entities we can attack.
                .filter(this::canAttack)

                // Sort out potential targets with the algorithm provided as a setting.
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity)))

                .collect(Collectors.toList());

        if (!mc.theWorld.loadedEntityList.contains(target)) {
            target = null;
        }

        if (nigger.isEmpty()) return null;

        return nigger.get(0);
    }

    public boolean canAttack(final EntityLivingBase player) {
        if (player instanceof EntityPlayer && !players.isEnabled()) {
            return false;
        }

        if (player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (!nonPlayers.isEnabled())
                return false;
        }

        if (player.isInvisible() && !invisibles.isEnabled())
            return false;

        if (player.isDead && !dead.isEnabled())
            return false;

        if (AntiBot.bots.contains(player))
            return false;

        if (player.isOnSameTeam(mc.thePlayer) && teams.isEnabled())
            return false;

        if (player.ticksExisted < 2)
            return false;

        if (player.deathTime != 0)
            return false;

        if (player instanceof EntityPlayer) {
            for (final String name : Rise.INSTANCE.getFriends()) {
                if (name.equalsIgnoreCase(((EntityPlayer) player).getName())) {
                    return false;
                }
            }

            // DO NOT DO THIS CAUSES AURA DISABLERS... How the fuck does it cause it..... You can just make everyone's name contain [npc]
            if (((EntityPlayer) player).getName().toLowerCase().contains("[npc]") && ignoreNPCs.isEnabled())
                return false;
        }

        return mc.thePlayer != player;
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
    }

    @Override
    protected void onEnable() {
        target = null;
        botState = BotState.GENERAL;
        selectedSlot = 0;
    }

    @Override
    protected void onDisable() {
        final GameSettings gameSettings = mc.gameSettings;
        gameSettings.keyBindForward.setKeyPressed(false);
        gameSettings.keyBindRight.setKeyPressed(false);
        gameSettings.keyBindLeft.setKeyPressed(false);
        gameSettings.keyBindBack.setKeyPressed(false);
        gameSettings.keyBindJump.setKeyPressed(false);
        gameSettings.keyBindSneak.setKeyPressed(false);
    }
}
