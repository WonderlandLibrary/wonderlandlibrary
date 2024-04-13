package dev.tenacity.module.impl.combat;

import dev.tenacity.command.impl.FriendCommand;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.misc.MathUtil;
import dev.tenacity.util.misc.TimerUtil;
import dev.tenacity.util.player.RotationUtil;
import lombok.Getter;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class KillAuraModule extends Module {

    @Getter
    private static EntityLivingBase currentTarget;
    private final List<EntityLivingBase> targetList = new ArrayList<>();

    private final TimerUtil timerUtil = new TimerUtil();

    private boolean blocking, wasBlocked;

    private final NumberSetting minimumCPS = new NumberSetting("Minimum CPS", 10, 1, 100, 0.1),
            maximumCPS = new NumberSetting("Maximum CPS", 10, 1, 100, 0.1),
            reach = new NumberSetting("Reach", 4, 3, 8, 0.1);

    private final BooleanSetting autoBlock = new BooleanSetting("Auto Block", false),
            rotate = new BooleanSetting("Rotate", true),
            enchantParticle = new BooleanSetting("Enchant Particle", false),
            criticalParticle = new BooleanSetting("Critical Particle", false);
    private final BooleanSetting randomizecps = new BooleanSetting("Extra Randomization", false);

    private final ModeSetting autoBlockMode = new ModeSetting("Auto Block Mode", "Vanilla", "Hurt Time", "Fake"),
            rotationMode = new ModeSetting("Rotation Mode", "Vanilla", "HvH Mode");

    public KillAuraModule() {
        super("KillAura", "Automatically attacks people within a set range", ModuleCategory.COMBAT);
        initializeSettings(minimumCPS, maximumCPS, reach, autoBlock, autoBlockMode, rotate, rotationMode, enchantParticle, criticalParticle, randomizecps);
        if(minimumCPS.getCurrentValue() > maximumCPS.getCurrentValue()) { minimumCPS.setCurrentValue(maximumCPS.getCurrentValue());}
        if(maximumCPS.getCurrentValue() < minimumCPS.getCurrentValue()) {maximumCPS.setCurrentValue(minimumCPS.getCurrentValue());}
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        sortAndAddTargets();
        if(minimumCPS.getCurrentValue() > maximumCPS.getCurrentValue()) { minimumCPS.setCurrentValue(maximumCPS.getCurrentValue());}
        if(maximumCPS.getCurrentValue() < minimumCPS.getCurrentValue()) {maximumCPS.setCurrentValue(minimumCPS.getCurrentValue());}
        currentTarget = !targetList.isEmpty() ? targetList.get(0) : null;
        blocking = autoBlock.isEnabled() && currentTarget != null && (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword);
        if (currentTarget == null) {
            if (event.isPre() && wasBlocked) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                wasBlocked = false;
            }
            return;
        }

        if (event.isPre()) {
            if(minimumCPS.getCurrentValue() > maximumCPS.getCurrentValue()) { minimumCPS.setCurrentValue(maximumCPS.getCurrentValue());}
            if(maximumCPS.getCurrentValue() < minimumCPS.getCurrentValue()) {maximumCPS.setCurrentValue(minimumCPS.getCurrentValue());}

            if (rotate.isEnabled()) {
                switch (rotationMode.getCurrentMode()) {
                    case "Vanilla":
                        final float[] rotations = RotationUtil.getRotationFromTarget(currentTarget);

                        final float sensitivityMultiplier = RotationUtil.getSensitivityMultiplier();
                        final float fixedYaw = rotations[0] - rotations[1] % sensitivityMultiplier;

                        event.setYaw(rotations[0]);
                        break;
                }
            }
            if (event.isPre()) {
                if (rotate.isEnabled()) {
                    if(minimumCPS.getCurrentValue() > maximumCPS.getCurrentValue()) { minimumCPS.setCurrentValue(maximumCPS.getCurrentValue());}
                    if(maximumCPS.getCurrentValue() < minimumCPS.getCurrentValue()) {maximumCPS.setCurrentValue(minimumCPS.getCurrentValue());}
                    switch (rotationMode.getCurrentMode()) {
                        case "HvH Mode":
                            final float[] rotations = RotationUtil.getRotationFromTarget(currentTarget);

                            final float sensitivityMultiplier = RotationUtil.getSensitivityMultiplier();
                            final float fixedYaw = rotations[1] - rotations[0] % sensitivityMultiplier;

                            event.setYaw((float) MathUtil.clamp(fixedYaw, 0, 10));
                            break;
                    }
                }
                if (!randomizecps.isEnabled()) {
                    if(minimumCPS.getCurrentValue() > maximumCPS.getCurrentValue()) { minimumCPS.setCurrentValue(maximumCPS.getCurrentValue());}
                    if(maximumCPS.getCurrentValue() < minimumCPS.getCurrentValue()) {maximumCPS.setCurrentValue(minimumCPS.getCurrentValue());}
                    final long cps = Math.round(MathUtil.getRandomInRange(minimumCPS.getCurrentValue(), maximumCPS.getCurrentValue()));
                    if (timerUtil.hasTimeElapsed(1000 / cps, true)) {

                        if (enchantParticle.isEnabled())
                            mc.thePlayer.onEnchantmentCritical(currentTarget);
                        if (criticalParticle.isEnabled())
                            mc.thePlayer.onCriticalHit(currentTarget);

                        mc.thePlayer.swingItem();
                        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(currentTarget, C02PacketUseEntity.Action.ATTACK));
                    }
                } else {
                    if (randomizecps.isEnabled()) {
                        if(minimumCPS.getCurrentValue() > maximumCPS.getCurrentValue()) { minimumCPS.setCurrentValue(maximumCPS.getCurrentValue());}
                        if(maximumCPS.getCurrentValue() < minimumCPS.getCurrentValue()) {maximumCPS.setCurrentValue(minimumCPS.getCurrentValue());}
                        final long cps = Math.round(MathUtil.getRandomInRange(minimumCPS.getCurrentValue(), maximumCPS.getCurrentValue()));
                        final long randomcps = (long) MathUtil.getRandomInRange(1, 2);
                        if (timerUtil.hasTimeElapsed(1000 / cps * randomcps, true)) {

                            if (enchantParticle.isEnabled())
                                mc.thePlayer.onEnchantmentCritical(currentTarget);
                            if (criticalParticle.isEnabled())
                                mc.thePlayer.onCriticalHit(currentTarget);

                            mc.thePlayer.swingItem();
                            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(currentTarget, C02PacketUseEntity.Action.ATTACK));
                        }
                    }
                }
            }
        }


        if (blocking) {
            switch (autoBlockMode.getCurrentMode()) {
                case "Hurt Time":
                    if (event.isPre() && mc.thePlayer.hurtTime == 0 && wasBlocked && !mc.thePlayer.isSwingInProgress) {
                        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                        wasBlocked = false;
                    }
                    break;
            }
        }

        if (blocking) {
            switch (autoBlockMode.getCurrentMode()) {
                case "Vanilla":
                    if (!wasBlocked) {
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                        wasBlocked = true;
                    }
                    break;
                case "Hurt Time":
                    if (mc.thePlayer.hurtTime == 9 && !wasBlocked) {
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                        wasBlocked = true;
                    }
                    break;
            }
        }
    };

    @Override
    public void onDisable() {
        if (wasBlocked && !mc.thePlayer.isSwingInProgress)
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));

        wasBlocked = blocking = false;

        currentTarget = null;
        targetList.clear();
        Timer.timerSpeed = 1f;
        super.onDisable();
    }
    private void sortAndAddTargets() {
        targetList.clear();
        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(entity) <= reach.getCurrentValue() && entity != mc.thePlayer && mc.thePlayer.canEntityBeSeen(entity) && !entity.isInvisible() && !FriendCommand.isFriend((entity).getDisplayName().getUnformattedText())) {
                    targetList.add(entityLivingBase);
                }
            }
        });
        targetList.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
    }
    public boolean isBlocking() {
        return blocking;
    }

    private final IEventListener<UpdateEvent> onUpdateEvent = event -> setSuffix(String.valueOf(maximumCPS.getCurrentValue() + " CPS"));

}