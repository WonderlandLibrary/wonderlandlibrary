package fun.expensive.client.feature.impl.combat;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventAttackSilent;
import fun.rich.client.event.events.impl.packet.EventReceivePacket;
import fun.rich.client.event.events.impl.packet.EventSendPacket;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.MultipleBoolSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.inventory.InvenotryUtil;
import fun.rich.client.utils.math.GCDFix;
import fun.rich.client.utils.math.KillauraUtils;
import fun.rich.client.utils.math.RotationHelper;
import fun.rich.client.utils.math.TimerHelper;
import fun.rich.client.utils.movement.MovementUtils;
import fun.rich.client.utils.Helper;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

public class KillAura extends Feature {
    public static TimerHelper timerHelper = new TimerHelper();
    public static float yaw;
    public float pitch;
    public float pitch2 = 0;
    private int notiTicks;
    public static boolean isAttacking;
    TimerHelper shieldFixerTimer = new TimerHelper();
    public float yaw2 = 0;
    public static boolean isBreaked;
    public static EntityLivingBase target;
    public static ListSetting rotationMode = new ListSetting("Rotation Mode", "Matrix", () -> true, "Vanilla", "Matrix", "Sunrise", "Snap", "Custom");
    public static ListSetting typeMode = new ListSetting("Type Mode", "Single", () -> true, "Single", "Switch");
    public static ListSetting sortMode = new ListSetting("Priority Mode", "Distance", () -> typeMode.currentMode.equalsIgnoreCase("Switch"), "Distance", "Health", "Crosshair", "Higher Armor", "Lowest Armor");
    public static NumberSetting fov = new NumberSetting("FOV", "Позволяет редактировать радиус в котором вы можете ударить игрока", 180, 0, 180, 1, () -> true);
    public static NumberSetting attackCoolDown = new NumberSetting("Attack CoolDown", "Редактирует скорость удара", 0.85F, 0.1F, 1F, 0.01F, () -> !rotationMode.currentMode.equals("Snap"));
    public static NumberSetting range = new NumberSetting("AttackRange", "Дистанция в которой вы можете ударить игрока", 3.6F, 3, 6, 0.01f, () -> true);
    public static NumberSetting yawrandom = new NumberSetting("Yaw Random", 1.6f, 0.1f, 20, 0.01F, () -> rotationMode.currentMode.equals("Custom"));
    public static NumberSetting pitchRandom = new NumberSetting("Pitch Random", 1.6f, 0.1f, 20, 0.01F, () -> rotationMode.currentMode.equals("Custom"));
    public static BooleanSetting staticPitch = new BooleanSetting("Static Pitch", false, () -> rotationMode.currentMode.equals("Custom"));
    public static NumberSetting pitchHead = new NumberSetting("Pitch Head", 0.35f, 0.1f, 1.2f, 0.01F, () -> rotationMode.currentMode.equals("Custom"));
    public BooleanSetting rayCast = new BooleanSetting("RayCast", "Проверяет навелась ли ротация на хит-бокс энтити", false, () -> true);
    public static BooleanSetting walls = new BooleanSetting("Walls", "Позволяет бить сквозь стены", true, () -> true);
    public static BooleanSetting onlyCritical = new BooleanSetting("Only Critical", "Бьет в нужный момент для крита", false, () -> true);
    public BooleanSetting spaceOnly = new BooleanSetting("Space Only", "Only Crits будут работать если зажат пробел", false, () -> onlyCritical.getBoolValue());
    public NumberSetting criticalFallDistance = new NumberSetting("Critical Fall Distance", "Регулировка дистанции до земли для крита", 0.2F, 0.08F, 1F, 0.01f, () -> onlyCritical.getBoolValue());
    public BooleanSetting shieldFixer = new BooleanSetting("ShieldFixer", "Отжимает щит во время удара, помогает обойти Matrix", false, () -> true);
    public NumberSetting fixerDelay = new NumberSetting("Fixer Delay", "Регулировка как долго щит будет отжмиматься (чем больше, тем щит будет дольше отжиматься)", 150.0f, 0.0f, 600.0f, 10.0f, () -> shieldFixer.getBoolValue());
    public BooleanSetting shieldDesync = new BooleanSetting("Shield Desync", false, () -> true);
    public static BooleanSetting shieldBreaker = new BooleanSetting("ShieldBreaker", "Автоматически ломает щит противнику", false, () -> true);
    public static BooleanSetting breakNotifications = new BooleanSetting("Break Notifications", true, () -> shieldBreaker.getBoolValue());
    public static BooleanSetting silentMove = new BooleanSetting("SilentMove", false, () -> true);
    public static MultipleBoolSetting targetsSetting = new MultipleBoolSetting("Targets", new BooleanSetting("Players", true), new BooleanSetting("Mobs"), new BooleanSetting("Animals"), new BooleanSetting("Villagers"), new BooleanSetting("Invisibles", true));

    public KillAura() {
        super("KillAura", "Автоматически аттакует энтити", FeatureCategory.Combat);
        addSettings(rotationMode, typeMode, sortMode, targetsSetting, fov, attackCoolDown, range, rayCast, yawrandom, pitchRandom, pitchHead, staticPitch, walls, onlyCritical, spaceOnly, criticalFallDistance, shieldBreaker, breakNotifications, shieldFixer, fixerDelay, shieldDesync, silentMove);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        /* Interact Fix */
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.getPacket();

            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT) {
                event.setCancelled(true);
            }

            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPreAttack(EventPreMotion event) {
        String mode = rotationMode.getOptions();

        setSuffix("" + mode);
        /* Sorting */
        target = KillauraUtils.getSortEntities();

        /* хуйня ебаная */
        if (target == null) {
            return;
        }
        /* RayCast */

        if (!rotationMode.currentMode.equals("Snap") && !RotationHelper.isLookingAtEntity(false, yaw, pitch, 0.12f, 0.12f, 0.12f, target, (range.getNumberValue())) && rayCast.getBoolValue()) {
            return;
        }

        /* Only Critical */
        mc.player.jumpTicks = 0;
        BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ);
        Block block = mc.world.getBlockState(blockPos).getBlock();
        float f2 = mc.player.getCooledAttackStrength(0.5F);
        boolean flag = (f2 > 0.9F);
        if (!flag && onlyCritical.getBoolValue())
            return;
        if (!(!mc.gameSettings.keyBindJump.isKeyDown() && spaceOnly.getBoolValue())) {
            if (MovementUtils.airBlockAboveHead()) {
                if (!(mc.player.fallDistance >= criticalFallDistance.getNumberValue() || block instanceof BlockLiquid || !onlyCritical.getBoolValue() || mc.player.isRiding() || mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isInWeb)) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    return;
                }
            } else if (!(!(mc.player.fallDistance > 0.0f) || mc.player.onGround || !onlyCritical.getBoolValue() || mc.player.isRiding() || mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isInWeb)) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                return;
            }
        }
        if (rotationMode.currentMode.equals("Snap") && mc.player.getCooledAttackStrength(0.0f) >= attackCoolDown.getNumberValue()) {
            float[] rots1 = RotationHelper.getRotations(target);
            mc.player.rotationYaw = rots1[0];
            mc.player.rotationPitch = rots1[1];
        }
        KillauraUtils.attackEntity(target);
    }

    @EventTarget
    public void onRotations(EventPreMotion event) {

        String mode = rotationMode.getOptions();

        if (target == null) {
            return;
        }

        if (!target.isDead) {
            /* ROTATIONS */
            float[] matrix = RotationHelper.getRotations(target);
            float[] fake = RotationHelper.getFakeRotations(target);

            float[] custom = RotationHelper.getCustomRotations(target);

            if (mode.equalsIgnoreCase("Matrix")) {
                event.setYaw(matrix[0]);
                event.setPitch(matrix[1]);
                yaw = matrix[0];
                pitch = matrix[1];
                mc.player.renderYawOffset = matrix[0];
                mc.player.rotationYawHead = matrix[0];
                mc.player.rotationPitchHead = matrix[1];
            } else if (mode.equalsIgnoreCase("Sunrise")) {
                yaw2 = GCDFix.getFixedRotation(MathHelper.Rotate(yaw2, matrix[0], 40, 50));
                pitch2 = GCDFix.getFixedRotation(MathHelper.Rotate(pitch2, matrix[1], 0.35f, 2.1f));
                event.setYaw(yaw2);
                event.setPitch(pitch2);
                yaw = yaw2;
                pitch = pitch2;
                mc.player.renderYawOffset = fake[0];
                mc.player.rotationYawHead = fake[0];
                mc.player.rotationPitchHead = fake[1];
            } else if (mode.equalsIgnoreCase("Custom")) {
                event.setYaw(custom[0]);
                event.setPitch(custom[1]);
                yaw = custom[0];
                pitch = custom[1];
                mc.player.renderYawOffset = custom[0];
                mc.player.rotationYawHead = custom[0];
                mc.player.rotationPitchHead = custom[1];
            }

        }
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent eventAttackSilent) {
        /* SHIELD Fix */
        isAttacking = true;
        if (mc.player.isBlocking() && shieldFixerTimer.hasReached(fixerDelay.getNumberValue()) && mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemShield && shieldFixer.getBoolValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.UP));
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
            shieldFixerTimer.reset();
        }
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        /* SHIELD Desync */
        if (shieldDesync.getBoolValue() && mc.player.isBlocking() && target != null && mc.player.ticksExisted % 8 == 0) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.DOWN));
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
        }
        if (shieldFixer.getBoolValue()) {
            if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    mc.gameSettings.keyBindUseItem.pressed = false;
                }
            } else {
                mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        }
    }

    @EventTarget
    public void onSound(EventReceivePacket sound) {
        if (breakNotifications.getBoolValue()) {
            if (sound.getPacket() instanceof SPacketEntityStatus) {
                SPacketEntityStatus sPacketEntityStatus = (SPacketEntityStatus) sound.getPacket();
                if (sPacketEntityStatus.getOpCode() == 30) {
                    if (sPacketEntityStatus.getEntity(mc.world) == target) {
                        if (notiTicks < 2) {
                            NotificationRenderer.queue(TextFormatting.GREEN + "Shield-Breaker", "Successfully destroyed " + target.getName() + " shield", 2, NotificationMode.SUCCESS);
                        } else {
                            notiTicks = 0;
                        }
                    }
                }
            }
        }
    }

    public static void BreakShield(EntityLivingBase tg) {
        if (InvenotryUtil.doesHotbarHaveAxe() && shieldBreaker.getBoolValue()) {
            int item = InvenotryUtil.getAxe();
            if (InvenotryUtil.getAxe() >= 0 && tg instanceof EntityPlayer && tg.isHandActive() && tg.getActiveItemStack().getItem() instanceof ItemShield) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(item));
                mc.playerController.attackEntity(mc.player, target);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            }
        }
    }

    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
    }
}
