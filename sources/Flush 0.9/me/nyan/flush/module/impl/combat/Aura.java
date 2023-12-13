package me.nyan.flush.module.impl.combat;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.ClickGui;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.*;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ColorSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.notifications.Notification;
import me.nyan.flush.utils.combat.CombatUtils;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
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
import net.minecraft.util.*;
import optifine.Config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public class Aura extends Module {
    public final ModeSetting sorting = new ModeSetting("Sorting", this, "Distance", "Single", "Switch", "Distance"),
            rotations = new ModeSetting("Rotations", this, "Normal", "None", "Normal", "Smooth"),
            autoblock = new ModeSetting("Block", this, "Verus", "None", "Hypixel", "Verus", "Fake");
    private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 12, 0, 20, 0.5),
            minCps = new NumberSetting("Min CPS", this, 9, 0, 20, 0.5),
            reach = new NumberSetting("Reach", this, 4.2, 3, 7, 0.05),
            blockReach = new NumberSetting("Block Reach", this, 4.2, 3, 7, 0.05,
                    () -> !autoblock.is("none")),
            switchDelay = new NumberSetting("Switch Delay", this, 100, 20, 600,
                    () -> sorting.is("switch")),
            smoothYawSpeed = new NumberSetting("Yaw Speed", this, 5, 1, 20,
                    () -> rotations.is("smooth")),
            smoothPitchSpeed = new NumberSetting("Pitch Speed", this, 5, 1, 20,
                    () -> rotations.is("smooth")),
            jitter = new NumberSetting("Jitter", this, 1, 0, 2, 0.1);
    private final BooleanSetting silent = new BooleanSetting("Silent Rotations", this, true,
                    () -> !rotations.is("none")),
            randomRots = new BooleanSetting("Random Rotations", this, false,
                    () -> !rotations.is("none")),
            invCheck = new BooleanSetting("Inventory Check", this, false),
            useBestSword = new BooleanSetting("Use Best Sword", this, false),
            rayCast = new BooleanSetting("Raycast", this, false),
            noSwing = new BooleanSetting("No Swing", this, false),
            targetEsp = new BooleanSetting("Target ESP", this, true),
            autoDisable = new BooleanSetting("Auto Disable", this, false),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            throughWalls = new BooleanSetting("Through Walls", this, true),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false);
    private final ColorSetting espColor = new ColorSetting("ESP Color", this, 0xFF00B8FF,
            targetEsp::getValue);

    public EntityLivingBase target;
    private final Timer timer = new Timer();
    private final Timer switchDelayTimer = new Timer();
    public boolean blocking;
    private final float[] smoothRots = new float[2];

    private float yaw, pitch;

    private float animationAngle;

    public Aura() {
        super("Aura", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        smoothRots[0] = mc.thePlayer.rotationYaw;
        smoothRots[1] = mc.thePlayer.rotationPitch;
        blocking = false;

        yaw = Integer.MIN_VALUE;
        pitch = Integer.MIN_VALUE;
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
    public void onUpdate(EventUpdate e) {
        /*
         if (!mc.isIntegratedServerRunning() && mc.getCurrentServerData().serverIP.toLowerCase().contains("cubecraft") &&
                 getModule(Scaffold.class).isEnabled())
             return;
        */

        if (mc.currentScreen != null && !(mc.currentScreen instanceof ClickGui) && invCheck.getValue()) {
            return;
        }

        if (!isValid(target)) {
            target = null;
            yaw = Integer.MIN_VALUE;
            pitch = Integer.MIN_VALUE;
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

        if (!rotations.is("none")) {
            if (silent.getValue()) {
                yaw = rots[0];
                pitch = rots[1];
            } else {
                mc.thePlayer.rotationYaw = rots[0];
                mc.thePlayer.rotationPitch = rots[1];
            }
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

                Entity entity = rayCast.getValue() ?
                        CombatUtils.rayCast(reach.getValueFloat(), rots[0], rots[1]) :
                        target;

                if (!noSwing.getValue()) {
                    mc.thePlayer.swingItem();
                }

                if (entity != null) {
                    mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                }
            }

            if (autoblock.is("hypixel") && isHoldingSword() && target.getDistanceToEntity(mc.thePlayer) <= blockReach.getValue()) {
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
        }

        if (autoblock.is("verus")) {
            if (!blocking && isHoldingSword() && target.getDistanceToEntity(mc.thePlayer) <= blockReach.getValue()) {
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                blocking = true;
            }
        } else {
            block();
        }
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (rotations.is("none")) {
            return;
        }
        if (silent.getValue()) {
            if (e.getState() == EventMotion.State.PRE) {
                if (yaw != Integer.MIN_VALUE) {
                    e.setYaw(yaw);
                }
                if (pitch != Integer.MIN_VALUE) {
                    e.setPitch(pitch);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        if (!targetEsp.getValue() || target == null) {
            return;
        }

        double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().renderPosX;
        double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().renderPosY;
        double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().renderPosZ;

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();

        drawFlushESP(target, x, y, z, ColorUtils.brightness(espColor.getRGB(),
                1 - (target.maxHurtTime > 0 ? (target.hurtTime / (float) target.maxHurtTime * 0.5F) : 0)));
        animationAngle += 1 / 10F * Flush.getFrameTime();

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        RenderUtils.glColor(-1);
    }

    public boolean shouldDrawESP(Entity entity) {
        return !targetEsp.getValue() || target != entity;
    }

    private void drawFlushESP(Entity entity, double x, double y, double z, int color) {
        y += 0.05;
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();

        float angle = animationAngle;

        float animation = (1 + MathHelper.cos(angle / 180F * MathHelper.PI)) * 0.5F;
        double size = Math.max(entityBoundingBox.maxX - entityBoundingBox.minX,
                entityBoundingBox.maxZ - entityBoundingBox.minZ);
        double radius = size * 0.85;
        double entityHeight = entity.height + 0.1;
        double v = entityHeight * animation;

        float direction = angle % 360F / 180F - 1;
        float f = direction < 0 ? animation : animation - 1;
        double height = (entityHeight / 2D) * f;
        if (v + height < 0) {
            height = 0 - v;
        }
        if (v + height > entityHeight) {
            height = entityHeight - v;
        }

        float dx = (float) (mc.thePlayer.posX - entity.posX);
        float dz = (float) (mc.thePlayer.posZ - entity.posZ);
        double distance = MathHelper.sqrt_float(dx * dx + dz * dz);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + v, z);

        glLineWidth(2);
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        glEnable(GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL_SMOOTH);

        glBegin(GL_TRIANGLE_STRIP);

        double a = 360 / Math.min((radius / distance * 500D * (Config.zoomMode ? 3 : 1)), 180);
        for (double i = -180; i <= 180; i += a) {
            double radians = Math.toRadians(i);
            RenderUtils.glColor(ColorUtils.alpha(color, 160));
            glVertex3d(Math.cos(radians) * radius, 0, Math.sin(radians) * radius);
            RenderUtils.glColor(ColorUtils.alpha(color, 0));
            glVertex3d(Math.cos(radians) * radius, height, Math.sin(radians) * radius);
        }

        RenderUtils.glColor(ColorUtils.alpha(color, 160));
        glVertex3d(Math.cos(Math.toRadians(180)) * radius, 0, Math.sin(Math.toRadians(180)) * radius);
        RenderUtils.glColor(ColorUtils.alpha(color, 0));
        glVertex3d(Math.cos(Math.toRadians(180)) * radius, height, Math.sin(Math.toRadians(180)) * radius);

        glEnd();

        RenderUtils.glColor(color);

        glBegin(GL_LINE_STRIP);
        for (double i = -180; i <= 180; i += a) {
            double radians = Math.toRadians(i);
            glVertex3d(Math.cos(radians) * radius, 0, Math.sin(radians) * radius);
        }
        glVertex3d(Math.cos(Math.toRadians(180)) * radius, 0, Math.sin(Math.toRadians(180)) * radius);
        glEnd();

        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        RenderUtils.glColor(-1);
        GlStateManager.popMatrix();
    }

    public float[] getRotations() {
        float[] rotations = CombatUtils.getRotations(target, randomRots.getValue());
        if (this.rotations.is("smooth")) {
            if (CombatUtils.rayCast(reach.getValueFloat(), smoothRots[0], smoothRots[1]) != target) {
                smoothRots[0] += CombatUtils.getFacingDifference(rotations[0], smoothRots[0]) * ((smoothYawSpeed.getValueFloat() / 100F) * 2);
                smoothRots[1] += CombatUtils.getFacingDifference(rotations[1], smoothRots[1]) * ((smoothPitchSpeed.getValueFloat() / 100F) * 2);
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

    @SubscribeEvent
    public void onWorldChange(EventWorldChange e) {
        if (autoDisable.getValue()) {
            disable();
            flush.getNotificationManager().show(
                    Notification.Type.INFO, name, name + " was " + EnumChatFormatting.RED + "disabled " +
                    EnumChatFormatting.RESET + "because you teleported.");
        }
    }

    @Override
    public String getSuffix() {
        return sorting.getValue();
    }

    public boolean isValid(EntityLivingBase entity) {
        if (entity instanceof AbstractClientPlayer && !mc.isIntegratedServerRunning() &&
                mc.getCurrentServerData().serverIP.toLowerCase().contains("funcraft.net") &&
                !entity.getDisplayName().getUnformattedText().contains("§")) {
            return false;
        }

        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(), villagers.getValue(), invisibles.getValue(),
                ignoreTeam.getValue()) && entity.getDistanceToEntity(mc.thePlayer) <= Math.max(reach.getValue(), blockReach.getValue()) &&
                (throughWalls.getValue() || mc.thePlayer.canEntityBeSeen(entity));
    }

    private void block() {
        if (autoblock.is("none") || !isHoldingSword() || target.getDistanceToEntity(mc.thePlayer) > blockReach.getValue()) {
            blocking = false;
            return;
        }

        if (autoblock.is("hypixel")) {
            mc.playerController.syncCurrentPlayItem();
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        }

        blocking = true;
    }

    private void unblock() {
        if (!autoblock.is("hypixel")) {
            blocking = false;
            return;
        }

        if (mc.thePlayer.isBlocking() || blocking) {
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
}