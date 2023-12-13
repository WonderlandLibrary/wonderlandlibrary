package me.nyan.flush.module.impl.combat;

import com.google.common.collect.Lists;
import me.nyan.flush.clickgui.ClickGui;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventReach;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.combat.CombatUtils;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class FightBot extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", this, "Lunar", "Lunar"),
            sorting = new ModeSetting("Sorting", this, "Distance", "Single", "Switch", "Distance"),
            rotations = new ModeSetting("Rotations", this, "Smooth", "Normal", "Smooth"),
            autoblock = new ModeSetting("Block", this, "None", "None", "Pre", "Post", "Verus", "Fake");
    private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 12, 0, 20, 0.5),
            minCps = new NumberSetting("Min CPS", this, 9, 0, 20, 0.5),
            reach = new NumberSetting("Reach", this, 3.4, 3, 6, 0.05),
            blockReach = new NumberSetting("Block Reach", this, 3.4, 3, 7, 0.05,
                    () -> !autoblock.is("none")),
            switchDelay = new NumberSetting("Switch Delay", this, 1000, 100, 2000,
                    () -> sorting.is("switch")),
            smoothYawSpeed = new NumberSetting("Yaw Speed", this, 5, 1, 20,
                    () -> rotations.is("smooth")),
            smoothPitchSpeed = new NumberSetting("Pitch Speed", this, 5, 1, 20,
                    () -> rotations.is("smooth")),
            jitter = new NumberSetting("Jitter", this, 1, 0, 2, 0.1);
    private final BooleanSetting invCheck = new BooleanSetting("Inventory Check", this, true),
            useBestSword = new BooleanSetting("Use Best Sword", this, false),
            targetEsp = new BooleanSetting("Target ESP", this, true),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false);

    public EntityLivingBase target;
    private final Timer timer = new Timer();
    private final Timer switchDelayTimer = new Timer();
    public boolean blocking;
    private final float[] smoothRots = new float[2];

    private int stage;

    public FightBot() {
        super("FightBot", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        smoothRots[0] = mc.thePlayer.rotationYaw;
        smoothRots[1] = mc.thePlayer.rotationPitch;
        blocking = false;
        stage = 0;
    }

    @Override
    public void onDisable() {
        target = null;
        if (autoblock.is("verus") && blocking) {
            mc.playerController.onStoppedUsingItem(mc.thePlayer);
            blocking = false;
        } else {
            unblock();
        }
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (mode.is("lunar") && shouldJoinGame()) {
            if (!(mc.currentScreen instanceof GuiChest)) {
                if (mc.playerController.currentPlayerItem == 0) {
                    ItemStack itemStack = mc.thePlayer.inventory.getCurrentItem();
                    if (stage == 0) {
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));
                    }

                    if (stage < 5) {
                        stage++;
                    } else {
                        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
                                C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        stage = 0;
                    }
                } else {
                    mc.thePlayer.inventory.currentItem = 0;
                }
            } else {
                GuiChest chest = (GuiChest) mc.currentScreen;
                String chestname = EnumChatFormatting.getTextWithoutFormattingCodes(chest.lowerChestInventory.getName()).toLowerCase();

                if (chestname.contains("join") && chestname.contains("queue")) {
                    for (Slot slot : chest.inventorySlots.inventorySlots) {
                        int slotNumber = slot.slotNumber;
                        ItemStack stack = chest.lowerChestInventory.getStackInSlot(slotNumber);
                        if (stack == null || !stack.hasDisplayName() ||
                                !EnumChatFormatting.getTextWithoutFormattingCodes(stack.getDisplayName())
                                                .equalsIgnoreCase("Boxing")) {
                            continue;
                        }

                        mc.playerController.windowClick(chest.inventorySlots.windowId, slotNumber, 0, 1, mc.thePlayer);
                        stage = 0;
                        break;
                    }
                }
            }
        }

        if (!inFight()) {
            return;
        }

        if (stage > -1) {
            mc.thePlayer.inventory.currentItem = 0;
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
            mc.playerController.onStoppedUsingItem(mc.thePlayer);
            stage = -1;
        }
        if (target == null || target.getDistanceToEntity(mc.thePlayer) > 3) {
            mc.thePlayer.moveForward = 1;
            MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() - (mc.thePlayer.moveStrafing != 0 ? 0.18 : 0));

            if (!mc.thePlayer.isPotionActive(Potion.moveSpeed) && mc.thePlayer.onGround && mc.thePlayer.jumpTicks == 0) {
                mc.thePlayer.jump();
                mc.thePlayer.jumpTicks = 10;
            }
        } else {
            MovementUtils.setSpeed(MovementUtils.getSpeed() / 10D);
        }
        if (mc.currentScreen != null && !(mc.currentScreen instanceof ClickGui) && invCheck.getValue()) {
            return;
        }

        if (e.getState() == EventMotion.State.PRE) {
            if (!isValid(target)) {
                target = null;
            }
            ArrayList<Entity> entities = mc.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase &&
                    isValid((EntityLivingBase) entity))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (sorting.is("distance")) {
                entities.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));
            }

            entities.sort(Comparator.comparing(entity -> !flush.getTargetManager().isTarget(entity)));
            
            if (!entities.isEmpty()) {
                if (sorting.is("switch")) {
                    if (switchDelayTimer.hasTimeElapsed(switchDelay.getValueInt(), true) || target == null) {
                        target = (EntityLivingBase) entities.get(MathUtils.getRandomNumber(0, entities.size()));
                        if (target == null) {
                            switchDelayTimer.reset();
                        }
                    }
                } else if (target == null || (entities.size() > 1 && target.getDistanceToEntity(mc.thePlayer) > (mc.thePlayer.canEntityBeSeen(target) ? reach.getValue() : 3))) {
                    target = (EntityLivingBase) entities.get(0);
                }
            }

            if (autoblock.is("verus")) {
                if (!isHoldingSword() && blocking) {
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    blocking = false;
                }
            }

            if (target == null) {
                if (autoblock.is("verus")) {
                    if (blocking) {
                        mc.playerController.onStoppedUsingItem(mc.thePlayer);
                        blocking = false;
                    }
                } else {
                    unblock();
                }
                smoothRots[0] = mc.thePlayer.rotationYaw;
                smoothRots[1] = mc.thePlayer.rotationPitch;
                return;
            }

            if (useBestSword.getValue() && (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))) {
                int bestSword = -1;
                for (int i = 36; i < 45; i++) {
                    Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                    ItemStack stack = slot.getStack();

                    if (stack != null) {
                        Item item = stack.getItem();
                        if (item instanceof ItemSword) {
                            if (bestSword == -1 || isBetterSword(stack, mc.thePlayer.inventoryContainer.getSlot(bestSword).getStack()))
                                bestSword = i;
                        }
                    }
                }

                if (bestSword != -1) {
                    mc.thePlayer.inventory.currentItem = bestSword - 36;
                }
            }

            float[] rots = getRotations();
            mc.thePlayer.rotationYaw = rots[0];
            if (target.getDistanceToEntity(mc.thePlayer) <= reach.getValue()) {
                mc.thePlayer.rotationPitch = rots[1];
            }

            if (mc.thePlayer.getDistanceToEntity(target) >= 2) {
                mc.thePlayer.moveForward = 1;
            }

            if (!shouldAttack(target)) {
                return;
            }

            if (!autoblock.is("verus")) {
                unblock();
            }

            double cps = MathUtils.getRandomNumber(maxCps.getValue(), minCps.getValue());
            if (timer.hasTimeElapsed((long) (1000 / cps), true)) {
                if (!autoblock.is("verus")) {
                    unblock();
                }

                if (target.getDistanceToEntity(mc.thePlayer) <= (mc.thePlayer.canEntityBeSeen(target) ? reach.getValue() : 3)) {
                    Entity entity = CombatUtils.rayCast(reach.getValueFloat(), rots[0], rots[1]);
                    mc.thePlayer.swingItem();

                    if (entity != null) {
                        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                    }
                    //mc.clickMouse();
                }

                if ((autoblock.is("pre") || autoblock.is("post")) && isHoldingSword() && target.getDistanceToEntity(mc.thePlayer) <= blockReach.getValue()) {
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                }
            }

            if (autoblock.is("verus")) {
                if (!blocking && isHoldingSword() && target.getDistanceToEntity(mc.thePlayer) <= blockReach.getValue()) {
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                    blocking = true;
                }
            } else if (!autoblock.is("post")) {
                block();
            }
        } else {
            if (autoblock.is("post") && target != null) {
                block();
            }
        }
    }

    @SubscribeEvent
    public void onReach(EventReach e) {
        e.setReach(reach.getValue());
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        if (!targetEsp.getValue()) {
            return;
        }

        double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().renderPosX;
        double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().renderPosY;
        double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().renderPosZ;

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();

        drawESPBox(target, x, y, z, target.hurtTime > 0 && target.hurtTime < 14 ? 0x5000FF00 : 0x50FF0000);
        drawESPBoxOutline(target, x, y, z, target.hurtTime > 0 && target.hurtTime < 14 ? 0xFF00FF00 : 0xFFFF0000);

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.color(1, 1, 1, 1);
    }

    private void drawESPBoxOutline(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        GL11.glLineWidth(1F);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderUtils.drawBoundingBoxOutline(axisAlignedBB, color);
    }

    private void drawESPBox(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        RenderUtils.fillBoundingBox(axisAlignedBB, color);
    }

    public float[] getRotations() {
        float[] rotations = CombatUtils.getRotations(target, true);
        if (this.rotations.is("smooth")) {
            if (CombatUtils.rayCast(reach.getValueFloat(), smoothRots[0], smoothRots[1]) != target) {
                smoothRots[0] += CombatUtils.getFacingDifference(rotations[0], smoothRots[0]) * ((smoothYawSpeed.getValueFloat() / 100F) * 4);
                smoothRots[1] += CombatUtils.getFacingDifference(rotations[1], smoothRots[1]) * ((smoothPitchSpeed.getValueFloat() / 100F) * 4);
            }
            rotations = smoothRots;
        }

        if (jitter.getValue() > 0) {
            rotations[0] += MathUtils.getRandomNumber(-jitter.getValue(), jitter.getValue());
            rotations[1] += MathUtils.getRandomNumber(-jitter.getValue(), jitter.getValue());
        }

        return rotations;
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {

    }

    @Override
    public String getSuffix() {
        return sorting.getValue();
    }

    public boolean isValid(EntityLivingBase entity) {
        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(), villagers.getValue(), invisibles.getValue(), ignoreTeam.getValue());
    }

    public boolean shouldAttack(EntityLivingBase entity) {
        return isValid(entity) &&
                entity.getDistanceToEntity(mc.thePlayer) <= Math.max(reach.getValue(), blockReach.getValue()) &&
                mc.thePlayer.canEntityBeSeen(entity);
    }

    private void block() {
        if (autoblock.is("none") || !isHoldingSword() || target.getDistanceToEntity(mc.thePlayer) > blockReach.getValue()) {
            blocking = false;
            return;
        }

        if (autoblock.is("pre") || autoblock.is("post")) {
            mc.playerController.syncCurrentPlayItem();
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        }

        blocking = true;
    }

    private void unblock() {
        if (!(autoblock.is("pre") || autoblock.is("post"))) {
            blocking = false;
            return;
        }

        if ((mc.thePlayer.isBlocking() || blocking)) {
            mc.playerController.syncCurrentPlayItem();

            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN, EnumFacing.DOWN));
            blocking = false;
        }
    }

    private boolean isHoldingSword() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    private boolean isBetterSword(ItemStack newStack, ItemStack oldStack) {
        return ((ItemSword) oldStack.getItem()).getDamageVsEntity() + getSwordStrength(oldStack) <=
                ((ItemSword) newStack.getItem()).getDamageVsEntity() + getSwordStrength(newStack);
    }

    private float getSwordStrength(ItemStack stack) {
        return (!(stack.getItem() instanceof ItemSword) ? 0.0F : (float)
                EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F) +
                (!(stack.getItem() instanceof ItemSword) ? 0.0F : (float)
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack));
    }

    private ArrayList<Score> getScores() {
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        return scoreboard.getSortedScores(scoreboard.getObjectiveInDisplaySlot(1))
                .stream()
                .filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#"))
                .collect(Collectors.toCollection(Lists::newArrayList));
    }
    private boolean scoreboardContains(String s) {
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        boolean found = false;
        for (Score score : getScores()) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String text = EnumChatFormatting.getTextWithoutFormattingCodes(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
            if (text.contains(s)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private boolean shouldJoinGame() {
        return scoreboardContains("Online:") && scoreboardContains("In Fights:") &&
                !scoreboardContains("Time:") && !inFight();
    }

    private boolean inFight() {
        return scoreboardContains("Fighting:");
    }
}