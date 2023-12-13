/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.combat;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.player.RotationUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ModuleInfo(name = "FightBot", description = "Fights for you", category = Category.COMBAT)
public final class FightBot extends Module {

    private final NoteSetting modeSettings = new NoteSetting("Mode Settings", this);
    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Hypixel", "Minemenclub");
    private final ModeSetting minemenclubMode = new ModeSetting("Minemenclub Mode", this, "Sumo", "Sumo");
    private final ModeSetting ranking = new ModeSetting("Ranking", this, "Unranked", "Unranked", "Ranked");
    private final ModeSetting hypixelMode = new ModeSetting("Hypixel Mode", this, "Sumo", "Sumo", "Classic", "Pit");
    private final NoteSetting generalSettings = new NoteSetting("General Settings", this);
    private final NumberSetting swingRange = new NumberSetting("Swing Range", this, 6, 0, 12, 0.1);
    private final NumberSetting range = new NumberSetting("Range", this, 3, 0, 6, 0.1);
    private final NumberSetting minCps = new NumberSetting("Min CPS", this, 8.0, 1, 20.0, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 9.0, 1, 20.0, 1);
    private final NoteSetting sumoSettings = new NoteSetting("Sumo Settings", this);
    private final ModeSetting antiFallMode = new ModeSetting("Anti Fall Mode", this, "Lagback", "Lagback", "Reverse");
    private final BooleanSetting antiFallNotifications = new BooleanSetting("Anti Fall Notifications", this, true);
    private final BooleanSetting antiFall = new BooleanSetting("Anti Fall", this, true);
    private final NoteSetting otherSettings = new NoteSetting("Other Settings", this);
    private final BooleanSetting targetStrafe = new BooleanSetting("Target Strafe", this, false);
    private final BooleanSetting autoBlock = new BooleanSetting("Auto Block", this, false);
    private final BooleanSetting autoGG = new BooleanSetting("Auto GG", this, true);
    private boolean hasGameStarted, saved, direction, inLimbo, isTargetStrafing, blocking, canClick, canClick2;
    private EntityLivingBase target, walkTarget, swingTarget;
    public static float yaw, pitch, lastYaw, lastPitch;
    private int wins, losses, cps, hitTicks, runTicks;
    private final TimeUtil windowTimer2 = new TimeUtil();
    private final TimeUtil windowTimer = new TimeUtil();
    private final TimeUtil limboTimer = new TimeUtil();
    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onUpdateAlwaysInGui() {
        if (maxCps.getValue() < minCps.getValue())
            maxCps.setValue(minCps.getValue());

        if (swingRange.getValue() < range.getValue())
            swingRange.setValue(range.getValue());

        switch (mode.getMode()) {
            case "Normal":
                hypixelMode.hidden = true;
                sumoSettings.hidden = true;
                antiFall.hidden = true;
                antiFallNotifications.hidden = true;
                antiFallMode.hidden = true;
                autoGG.hidden = true;
                autoBlock.hidden = false;
                minemenclubMode.hidden = true;
                ranking.hidden = true;
                break;

            case "Minemenclub":
                hypixelMode.hidden = true;
                sumoSettings.hidden = true;
                antiFall.hidden = true;
                antiFallNotifications.hidden = true;
                antiFallMode.hidden = true;
                autoGG.hidden = true;
                autoBlock.hidden = true;
                minemenclubMode.hidden = false;
                ranking.hidden = false;
                break;

            case "Hypixel":
                switch (hypixelMode.getMode()) {
                    case "Sumo":
                        sumoSettings.hidden = false;
                        antiFall.hidden = false;
                        autoBlock.hidden = true;
                        antiFallNotifications.hidden = !antiFall.isEnabled();
                        antiFallMode.hidden = !antiFall.isEnabled();
                        break;

                    case "Pit":
                    case "Classic":
                        sumoSettings.hidden = true;
                        antiFall.hidden = true;
                        autoBlock.hidden = false;
                        antiFallNotifications.hidden = true;
                        antiFallMode.hidden = true;
                        break;
                }
                minemenclubMode.hidden = true;
                hypixelMode.hidden = false;
                autoGG.hidden = false;
                ranking.hidden = true;
                break;
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        ++this.hitTicks;

        if (hasGameStarted || mode.is("Normal")) {
            /*
             * If we do not have a target that means we cannot run the aura so we
             * can just break our label here as there is no point in going further.
             */

            if (walkTarget != null) {
                /*
                 * Whilst we have silent rotations enabled we only only want the rotations to be seen server sided.
                 * And whilst we have non silent rotations we can just update our rotations manually.
                 */
                mc.thePlayer.rotationYaw = yaw;
                mc.thePlayer.rotationPitch = pitch;

                final double direction = Math.toRadians(walkTarget.motionX / walkTarget.motionZ);
                final double moveDirection = MoveUtil.getDirection();
                final double posX = -Math.sin(moveDirection) * MoveUtil.getSpeed() * 12;
                final double posZ = Math.cos(moveDirection) * MoveUtil.getSpeed() * 12;

                if ((Math.abs(direction - Math.toRadians(walkTarget.rotationYaw + 45)) < 0.6 || Math.abs(direction - Math.toRadians(walkTarget.rotationYaw - 45)) < 0.6) && (!(PlayerUtil.getBlockRelativeToPlayer(posX, -1, posZ) instanceof BlockAir) && mc.thePlayer.onGround || !hypixelMode.is("Sumo") || !mode.is("Hypixel"))) {
                    if ((mode.is("Hypixel") && hypixelMode.is("Sumo") || mode.is("Minemenclub") && minemenclubMode.is("Sumo")) && PlayerUtil.getBlockRelativeToPlayer(posX, -1, posZ) instanceof BlockAir && mc.thePlayer.onGround) {
                        this.direction = !this.direction;
                        return;
                    }

                    if (Math.abs(direction - Math.toRadians(walkTarget.rotationYaw + 45)) < 0.6)
                        this.direction = false;

                    if (Math.abs(direction - Math.toRadians(walkTarget.rotationYaw - 45)) < 0.6)
                        this.direction = true;

                    isTargetStrafing = true;
                } else {
                    isTargetStrafing = false;
                }

                if (mc.thePlayer.getDistance(walkTarget.posX, walkTarget.posY, walkTarget.posZ) > 2) {
                    mc.gameSettings.keyBindForward.setKeyPressed(true);
                    mc.gameSettings.keyBindBack.setKeyPressed(false);
                } else {
                    mc.gameSettings.keyBindBack.setKeyPressed(true);
                    mc.gameSettings.keyBindForward.setKeyPressed(false);
                }
            } else {
                mc.gameSettings.keyBindForward.setKeyPressed(mode.is("Hypixel") && hypixelMode.is("Classic"));
                mc.gameSettings.keyBindRight.setKeyPressed(false);
                mc.gameSettings.keyBindLeft.setKeyPressed(false);
                mc.gameSettings.keyBindBack.setKeyPressed(false);
            }

            /*
            Whilst attacking the target, this will cause the FightBot to strafe
            */
            if (targetStrafe.isEnabled() && swingTarget != null) {
                mc.gameSettings.keyBindRight.setKeyPressed(direction);
                mc.gameSettings.keyBindLeft.setKeyPressed(!direction);
            } else {
                mc.gameSettings.keyBindRight.setKeyPressed(false);
                mc.gameSettings.keyBindLeft.setKeyPressed(false);
            }

            if (mode.is("Hypixel") && hypixelMode.is("Sumo")) {
                if (antiFall.isEnabled() && mc.thePlayer.hurtTime > 0 && PlayerUtil.getBlockRelativeToPlayer(mc.thePlayer.motionX * 5, -3, mc.thePlayer.motionZ * 5) instanceof BlockAir && !saved) {
                    switch (antiFallMode.getMode()) {
                        case "Lagback":
                            event.setX(-1);
                            event.setZ(-1);
                            mc.thePlayer.motionX = -mc.thePlayer.motionX;
                            mc.thePlayer.motionZ = -mc.thePlayer.motionZ;
                            if (antiFallNotifications.isEnabled())
                                this.registerNotification("Lagged you back to prevent you from getting knocked off.");
                            break;

                        case "Reverse":
                            mc.thePlayer.motionX = -mc.thePlayer.motionX;
                            mc.thePlayer.motionZ = -mc.thePlayer.motionZ;
                            if (antiFallNotifications.isEnabled())
                                this.registerNotification("Reversed your motion to prevent you from getting knocked off.");
                            break;
                    }
                    saved = true;
                } else
                    saved = false;
            }
        } else {
            mc.gameSettings.keyBindForward.setKeyPressed(false);
            mc.gameSettings.keyBindRight.setKeyPressed(false);
            mc.gameSettings.keyBindLeft.setKeyPressed(false);
            mc.gameSettings.keyBindBack.setKeyPressed(false);
        }
    }

    @Override
    public void onUpdate(final UpdateEvent event) {
        switch (mode.getMode()) {
            case "Hypixel": {
                switch (hypixelMode.getMode()) {
                    case "Sumo":
                        mc.gameSettings.keyBindJump.setKeyPressed(mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround && MoveUtil.isMoving());
                        mc.gameSettings.keyBindSneak.setKeyPressed(mc.thePlayer.onGround && PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir);
                        break;

                    case "Classic":
                        if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                            if (walkTarget != null) {
                                if ((mc.thePlayer.getDistance(walkTarget.posX, walkTarget.posY, walkTarget.posZ) > (swingRange.getValue() + 3) || targetRunningAway() || walkTarget.isEating() || mc.thePlayer.isCollidedHorizontally)) {
                                    mc.gameSettings.keyBindJump.setKeyPressed(true);
                                }
                            } else if (mc.thePlayer.isCollidedHorizontally) {
                                mc.gameSettings.keyBindJump.setKeyPressed(true);
                            }
                        }

                        mc.thePlayer.inventory.currentItem = PlayerUtil.findSword() == -1 ? 0 : PlayerUtil.findSword();
                        break;

                    case "Pit":
                        if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                            if (walkTarget != null) {
                                if ((mc.thePlayer.getDistance(walkTarget.posX, walkTarget.posY, walkTarget.posZ) > (swingRange.getValue() + 3) || targetRunningAway() || walkTarget.isEating() || mc.thePlayer.isCollidedHorizontally)) {
                                    mc.gameSettings.keyBindJump.setKeyPressed(true);
                                }
                            } else if (mc.thePlayer.isCollidedHorizontally) {
                                mc.gameSettings.keyBindJump.setKeyPressed(true);
                            }
                        }

                        if (!hasGameStarted) {
                            mc.gameSettings.keyBindForward.setKeyPressed(true);
                        }

                        if (mc.thePlayer.fallDistance > 25 || mc.thePlayer.isRiding()) {
                            hasGameStarted = true;
                        }

                        mc.thePlayer.inventory.currentItem = PlayerUtil.findSword() == -1 ? 0 : PlayerUtil.findSword();
                        break;
                }

                if (inLimbo && limboTimer.hasReached(3100L)) {
                    mc.thePlayer.sendChatMessage(getJoinCommand());
                    limboTimer.reset();
                }
            }
            break;

            case "Minemenclub": {
                switch (minemenclubMode.getMode()) {
                    case "Sumo": {
                        if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround && MoveUtil.isMoving()) {
                            mc.gameSettings.keyBindJump.setKeyPressed(true);
                        }

                        mc.gameSettings.keyBindSneak.setKeyPressed(mc.thePlayer.onGround && PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir);

                        if (!hasGameStarted && mc.thePlayer.posX == -116.5 && mc.thePlayer.posZ == 149.5 && (wins > 0 || losses > 0)) {
                            onEnable();
                            mc.gameSettings.keyBindForward.setKeyPressed(true);
                        }

                        if (canClick && windowTimer.hasReached(RandomUtils.nextLong(700, 1200)) && ranking.is("Unranked") && mc.thePlayer.openContainer instanceof ContainerChest) {
                            final ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

                            for (int i = 0; i < chest.inventorySlots.size(); i++) {
                                final ItemStack stackInSlot = chest.getLowerChestInventory().getStackInSlot(i);
                                if (stackInSlot != null && stackInSlot.getDisplayName().equals("§e§lSolos")) {
                                    mc.playerController.windowClick(chest.windowId, i, 0, 0, mc.thePlayer);
                                    windowTimer2.reset();
                                    canClick2 = true;
                                }
                            }
                            canClick = false;
                        }

                        if ((canClick2 && windowTimer2.hasReached(RandomUtils.nextLong(700, 1200)) || ranking.is("Ranked") && canClick && windowTimer.hasReached(RandomUtils.nextLong(700, 1200))) && mc.thePlayer.openContainer instanceof ContainerChest) {
                            final ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

                            for (int i = 0; i < chest.inventorySlots.size(); i++) {
                                final ItemStack stackInSlot = chest.getLowerChestInventory().getStackInSlot(i);
                                if (stackInSlot != null && stackInSlot.getDisplayName().equals("§e§lSumo")) {
                                    mc.playerController.windowClick(chest.windowId, i, 0, 0, mc.thePlayer);
                                }
                            }
                            canClick2 = false;
                        }
                    }
                    break;
                }
            }
            break;

            case "Normal": {
                if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                    if (walkTarget != null) {
                        if ((mc.thePlayer.getDistance(walkTarget.posX, walkTarget.posY, walkTarget.posZ) > (swingRange.getValue() + 3) || targetRunningAway() || walkTarget.isEating() || mc.thePlayer.isCollidedHorizontally)) {
                            mc.gameSettings.keyBindJump.setKeyPressed(true);
                        }
                    } else if (mc.thePlayer.isCollidedHorizontally) {
                        mc.gameSettings.keyBindJump.setKeyPressed(true);
                    }
                }

                mc.thePlayer.inventory.currentItem = PlayerUtil.findSword() == -1 ? 0 : PlayerUtil.findSword();
            }
            break;
        }

        if (target == null && blocking)
            unblock();

        if (swingTarget == null || (!hasGameStarted && !mode.is("Normal")) || (Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Scaffold")).isEnabled()))
            return;

        final int delayValue = -1;

        boolean attack = false;

        /*
         * This is the part we actually calculate the click delay we need in order land another hit.
         * The attack boolean will be true when the time required for another attack passes.
         */
        if (this.timer.hasReached(this.cps)) {
            final int maxValue = (int) ((this.minCps.getMaximum() - this.maxCps.getValue()) * 10);
            final int minValue = (int) ((this.minCps.getMaximum() - this.minCps.getValue()) * 10);

            this.cps = (int) randomBetween(minValue, maxValue);

            this.timer.reset();

            attack = true;
        }

        if (target != null && autoBlock.isEnabled() && !blocking && mc.currentScreen == null && PlayerUtil.isHoldingSword()) {
            block(target);
        }

        if (attack && this.hitTicks > delayValue) {
            if (!isTargetStrafing && !(swingTarget.getHeldItem() != null && (swingTarget.getHeldItem().getItem() instanceof ItemBow || swingTarget.getHeldItem().getItem() instanceof ItemFishingRod)))
                direction = !direction;

            mc.thePlayer.swingItem();

            /*
             * To keep everything vanilla about
             * movement we can use the games attack method to keep everything vanilla.
             */

            if (target != null && target != mc.thePlayer) {
                mc.playerController.attackEntity(mc.thePlayer, this.target);

                if (PlayerUtil.isHoldingSword()) {
                    if (autoBlock.isEnabled() && blocking)
                        unblock();
                } else
                    blocking = false;
            }
        }
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        swingTarget = null;
        walkTarget = null;
        target = null;
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        updateTarget();
        updateWalkTarget();
        updateSwingTarget();
        if (walkTarget != null)
            updateRotations();
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packetChat = (S02PacketChat) event.getPacket();
            final String message = packetChat.getChatComponent().getUnformattedText();

            switch (mode.getMode()) {
                case "Hypixel": {
                    if (message.startsWith("                     Eliminate your opponents!"))
                        hasGameStarted = true;

                    if (message.startsWith("You were spawned in Limbo.")) {
                        hasGameStarted = false;
                        inLimbo = true;
                    }

                    if (message.startsWith("Sending you to ") || message.startsWith("SERVER FOUND! Sending to "))
                        inLimbo = false;

                    if (hypixelMode.is("Pit") && (message.startsWith("§c§lDEATH!") || message.equals("DEATH!")))
                        hasGameStarted = false;

                    if (message.startsWith(mc.thePlayer.getName() + " was knocked out the arena by ")) {
                        if (autoGG.isEnabled())
                            mc.thePlayer.sendChatMessage("gg");
                        mc.thePlayer.sendChatMessage(getJoinCommand());
                        hasGameStarted = false;
                        losses++;

                        this.registerNotification("You have now lost " + losses + " " + (losses == 1 ? "time" : "times") + " so far.");
                    } else if (message.endsWith("was knocked out the arena by " + mc.thePlayer.getName() + ".")) {
                        if (autoGG.isEnabled())
                            mc.thePlayer.sendChatMessage("gg");
                        mc.thePlayer.sendChatMessage(getJoinCommand());
                        hasGameStarted = false;
                        wins++;

                        this.registerNotification("You have now won " + wins + " " + (wins == 1 ? "time" : "times") + " so far.");
                    }

                    switch (message) {
                        case "YOU WON! Want to play again? CLICK HERE! ":
                            if (autoGG.isEnabled())
                                mc.thePlayer.sendChatMessage("gg");
                            mc.thePlayer.sendChatMessage(getJoinCommand());
                            hasGameStarted = false;
                            wins++;

                            this.registerNotification("You have now won " + wins + " " + (wins == 1 ? "time" : "times") + " so far.");
                            break;

                        case "You are AFK. Move around to return from AFK.":
                            inLimbo = true;
                            break;
                    }
                }
                break;

                case "Minemenclub": {
                    if (message.startsWith(mc.thePlayer.getName() + " was killed")) {
                        hasGameStarted = false;
                        losses++;

                        this.registerNotification("You have now lost " + losses + " " + (losses == 1 ? "time" : "times") + " so far.");
                    } else if (message.startsWith("Winner: " + mc.thePlayer.getName())) {
                        hasGameStarted = false;
                        wins++;

                        this.registerNotification("You have now won " + wins + " " + (wins == 1 ? "time" : "times") + " so far.");
                    }

                    switch (message) {
                        case "The match has started, good luck!":
                            hasGameStarted = true;
                            break;
                    }
                }
                break;
            }
        }
    }

    private String getJoinCommand() {
        switch (hypixelMode.getMode()) {
            default:
            case "Sumo":
                return "/play duels_sumo_duel";

            case "Classic":
                return "/play duels_classic_duel";

            case "Pit":
                return "/play pit";
        }
    }

    private double randomBetween(final double min, final double max) {
        return ((Math.random() * ((RandomUtils.nextDouble(max, max + 1)) - RandomUtils.nextDouble(min, min + 1)) + 1) + min);
    }

    private void updateRotations() {
        /*
         * Update our last rotations as the current ones as we are updating
         * the current ones soon. We require the last rotations to smooth
         * out the current rotations properly based on the last rotations.
         */
        lastYaw = yaw;
        lastPitch = pitch;

        /*
         * Finally grab the required rotations to actually aim at the target.
         * We do not need to pass in any parameters as the method already grabs the settings for us.
         */
        final float[] rotations = this.getRotations();

        /*
         * We can now update the rotation fields for the aura so the client
         * can send the server the rotations we actually want to apply.
         */
        yaw = rotations[0];
        pitch = rotations[1];
    }

    private float[] getRotations() {
        double predictValue = 0.2;
        int headSpeed = 3;

        /*
        Makes it so the bot predicts ahead of the player, if the player is far away or running away
         */

        float yaw;
        float pitch;

        if ((targetRunningAway() && mc.thePlayer.getDistance(walkTarget.posX, walkTarget.posY, walkTarget.posZ) > 7) || mc.thePlayer.getDistance(walkTarget.posX, walkTarget.posY, walkTarget.posZ) > 19) {
            predictValue = 15;
            headSpeed = 1;
        }

        final double lookX = (walkTarget.posX - (walkTarget.lastTickPosX - walkTarget.posX) * predictValue) + 0.01 - mc.thePlayer.posX;
        final double lookZ = (walkTarget.posZ - (walkTarget.lastTickPosZ - walkTarget.posZ) * predictValue) + 0.01 - mc.thePlayer.posZ;

        double closestPointOnYAxis = (mc.thePlayer.posY - walkTarget.posY);
        if (closestPointOnYAxis < -1.4) closestPointOnYAxis = -1.4;
        if (closestPointOnYAxis > 0.1) closestPointOnYAxis = 0.1;

        final double lookY = (walkTarget.posY) + 0.4 + walkTarget.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + closestPointOnYAxis;

        final double var14 = MathHelper.sqrt_double(lookX * lookX + lookZ * lookZ);

        yaw = (float) (Math.atan2(lookZ, lookX) * 180.0D / Math.PI) - 90.0F;
        pitch = (float) -(Math.atan2(lookY, var14) * 180.0D / Math.PI);

        /*
        This makes the rotations look more legit
         */
        if (mc.thePlayer.isSwingInProgress) {
            yaw += (Math.random() - 0.5);
            pitch += (Math.random() - 0.5);
        }

        yaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        pitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);

        /*
        Makes it so the bots headspeed is more precise in combat
         */
        if (mc.thePlayer.isSwingInProgress)
            headSpeed = 6;

        yaw = (mc.thePlayer.prevRotationYaw + yaw * headSpeed) / (headSpeed + 1);
        pitch = (mc.thePlayer.prevRotationPitch + pitch * headSpeed) / (headSpeed + 1);

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

    private boolean targetRunningAway() {
        final double lookX = walkTarget.posX - mc.thePlayer.posX;
        final double lookY = walkTarget.posZ - mc.thePlayer.posZ;

        float yaw = (float) (Math.atan2(lookY, lookX) * 180.0D / Math.PI) - 90.0F;
        yaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);

        if (Math.abs(yaw - MathHelper.wrapAngleTo180_float(walkTarget.rotationYaw)) < 90 && walkTarget.getDistance(walkTarget.lastTickPosX, walkTarget.posY, walkTarget.lastTickPosZ) > 0.1)
            runTicks++;
        else
            runTicks = 0;

        return runTicks > 4;
    }

    private void updateTarget() {
        final List<EntityLivingBase> entities = mc.theWorld.loadedEntityList
                // Stream our entity list.
                .stream()

                // Only get living entities so we don't have to check for items on ground etc.
                .filter(entity -> entity instanceof EntityLivingBase)

                // Map our entities to entity living base as we have filtered out none living entities.
                .map(entity -> ((EntityLivingBase) entity))

                // Only get the entities we can attack.
                .filter(entity -> {
                    if (!(entity instanceof EntityPlayer)) return false;

                    if (entity.isInvisible()) return false;

                    if (((EntityPlayer) entity).getName().toLowerCase().contains("[npc]")) return false;

                    if (!mc.thePlayer.canEntityBeSeen(entity)) return false;

                    return mc.thePlayer != entity;
                })

                // Do a proper distance calculation to get entities we can reach.
                .filter(entity -> {
                    // DO NOT TOUCH THIS VALUE ITS CALCULATED WITH MATH
                    final double girth = 0.5657;

                    // See if the other entity is in our range.
                    return mc.thePlayer.getDistanceToEntity(entity) - girth < range.getValue();
                })

                // Sort out potential targets with the algorithm.
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))

                // Get the possible targets and put them in a list.
                .collect(Collectors.toList());

        // Grab our best option from the list.
        target = entities.size() > 0 ? entities.get(0) : null;
    }

    private void updateWalkTarget() {
        final List<EntityLivingBase> entities = mc.theWorld.loadedEntityList
                // Stream our entity list.
                .stream()

                // Only get living entities so we don't have to check for items on ground etc.
                .filter(entity -> entity instanceof EntityLivingBase)

                // Map our entities to entity living base as we have filtered out none living entities.
                .map(entity -> ((EntityLivingBase) entity))

                // Only get the entities we can attack.
                .filter(entity -> {
                    if (!(entity instanceof EntityPlayer)) return false;

                    if (entity.isInvisible()) return false;

                    if (((EntityPlayer) entity).getName().toLowerCase().contains("[npc]")) return false;

                    if (!mc.thePlayer.canEntityBeSeen(entity)) return false;

                    return mc.thePlayer != entity;
                })

                // Sort out potential targets with the algorithm.
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))

                // Get the possible targets and put them in a list.
                .collect(Collectors.toList());

        // Grab our best option from the list.
        walkTarget = entities.size() > 0 ? entities.get(0) : null;
    }

    private void updateSwingTarget() {
        final List<EntityLivingBase> entities = mc.theWorld.loadedEntityList
                // Stream our entity list.
                .stream()

                // Only get living entities so we don't have to check for items on ground etc.
                .filter(entity -> entity instanceof EntityLivingBase)

                // Map our entities to entity living base as we have filtered out none living entities.
                .map(entity -> ((EntityLivingBase) entity))

                // Only get the entities we can attack.
                .filter(entity -> {
                    if (!(entity instanceof EntityPlayer)) return false;

                    if (entity.isInvisible()) return false;

                    if (((EntityPlayer) entity).getName().toLowerCase().contains("[npc]")) return false;

                    if (!mc.thePlayer.canEntityBeSeen(entity)) return false;

                    return mc.thePlayer != entity;
                })

                // Do a proper distance calculation to get entities we can reach.
                .filter(entity -> {
                    // DO NOT TOUCH THIS VALUE ITS CALCULATED WITH MATH
                    final double girth = 0.5657;

                    // See if the other entity is in our range.
                    return mc.thePlayer.getDistanceToEntity(entity) - girth < swingRange.getValue();
                })

                // Sort out potential targets with the algorithm.
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))

                // Get the possible targets and put them in a list.
                .collect(Collectors.toList());

        // Grab our best option from the list.
        swingTarget = entities.size() > 0 ? entities.get(0) : null;
    }

    @Override
    protected void onEnable() {
        switch (mode.getMode()) {
            case "Hypixel": {
                mc.thePlayer.sendChatMessage(getJoinCommand());
            }
            break;

            case "Minemenclub": {
                for (int i = 0; i < 9; i++) {
                    final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                    if (itemStack == null || itemStack.getItem() == null) continue;

                    final String requiredItemName = ranking.is("Unranked") ? "§dUnranked Queue §7(Right Click)" : "§dRanked Queue §7(Right Click)";

                    if (itemStack.getItem() instanceof ItemSword && itemStack.getDisplayName().equals(requiredItemName)) {
                        mc.thePlayer.inventory.currentItem = i;
                        new Thread(() -> {
                            try {
                                Thread.sleep(RandomUtils.nextLong(100L, 200L));

                                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        canClick = true;
                        windowTimer.reset();
                    }
                }
            }
            break;
        }
        canClick2 = false;
        hasGameStarted = false;
        saved = false;
        inLimbo = false;
        runTicks = 0;
    }

    @Override
    protected void onDisable() {
        final String winsWordType = wins == 1 ? "time" : "times", lossesWordType = losses == 1 ? "time" : "times";
        switch (mode.getMode()) {
            case "Hypixel": {
                if (!hypixelMode.is("Pit"))
                    Rise.addChatMessage("You won " + wins + " " + winsWordType + " and lost " + losses + " " + lossesWordType + " before disabling the module.");
            }
            break;

            case "Minemenclub": {
                Rise.addChatMessage("You won " + wins + " " + winsWordType + " and lost " + losses + " " + lossesWordType + " before disabling the module.");
            }
            break;
        }
        limboTimer.reset();
        timer.reset();
        swingTarget = null;
        walkTarget = null;
        target = null;
        mc.gameSettings.keyBindForward.setKeyPressed(false);
        mc.gameSettings.keyBindRight.setKeyPressed(false);
        mc.gameSettings.keyBindLeft.setKeyPressed(false);
        mc.gameSettings.keyBindBack.setKeyPressed(false);
        mc.gameSettings.keyBindSneak.setKeyPressed(false);
        losses = 0;
        wins = 0;
        if (blocking)
            unblock();
    }

    private void block(final Entity target) {
        mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
        mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 5647);
        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
        blocking = true;
    }

    private void unblock() {
        PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        blocking = false;
    }
}