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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TPAura extends Module {

    private EntityLivingBase currentTarget;
    private final List<EntityLivingBase> targetList = new ArrayList<>();

    private final TimerUtil timerUtil = new TimerUtil();

    private boolean blocking, wasBlocked;

    private final NumberSetting minimumCPS = new NumberSetting("Minimum CPS", 10, 1, 25, 0.1),
            maximumCps = new NumberSetting("Maximum CPS", 10, 1, 25, 0.1),
            reach = new NumberSetting("Range", 50, 8, 300, 1);

    private final BooleanSetting autoBlock = new BooleanSetting("Auto Block", false),
            rotate = new BooleanSetting("Rotate", true),
            enchantParticle = new BooleanSetting("Enchant Particle", false),
            criticalParticle = new BooleanSetting("Critical Particle", false);

    private final ModeSetting autoBlockMode = new ModeSetting("Auto Block Mode", "Vanilla", "Hurt Time", "Fake"),
            rotationMode = new ModeSetting("Rotation Mode", "Nope lol", "Not needed");

    public TPAura() {
        super("TPAura", "Don't HvH people with this :(", ModuleCategory.COMBAT);
        initializeSettings(minimumCPS, maximumCps, reach, autoBlock, autoBlockMode, rotate, rotationMode, enchantParticle, criticalParticle);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        sortAndAddTargets();

        currentTarget = !targetList.isEmpty() ? targetList.get(0) : null;
        blocking = autoBlock.isEnabled() && currentTarget != null && (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword);
        if(currentTarget == null) {
            if(event.isPre() && wasBlocked) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                wasBlocked = false;
            }
            return;
        }

        if(event.isPre()) {
            if(rotate.isEnabled()) {
                switch (rotationMode.getCurrentMode()) {
                    case "Nope lol":
                        break;
                }
                switch (rotationMode.getCurrentMode()) {
                    case "Not needed":
                        break;
                }

                final long cps = Math.round(MathUtil.getRandomInRange(minimumCPS.getCurrentValue(), maximumCps.getCurrentValue()));
                if(timerUtil.hasTimeElapsed(1000 / cps, true)) {

                    if(enchantParticle.isEnabled())
                        mc.thePlayer.onEnchantmentCritical(currentTarget);
                    if(criticalParticle.isEnabled())
                        mc.thePlayer.onCriticalHit(currentTarget);

                    mc.thePlayer.swingItem();
                    mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(currentTarget, C02PacketUseEntity.Action.ATTACK));
                }
            }

            if(blocking) {
                switch(autoBlockMode.getCurrentMode()) {
                    case "Hurt Time":
                        if(event.isPre() && mc.thePlayer.hurtTime == 0 && wasBlocked && !mc.thePlayer.isSwingInProgress) {
                            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                            wasBlocked = false;
                        }
                        break;
                }
            }
        }

        if(blocking) {
            switch(autoBlockMode.getCurrentMode()) {
                case "Vanilla":
                    if(!wasBlocked) {
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                        wasBlocked = true;
                    }
                    break;
                case "Hurt Time":
                    if(mc.thePlayer.hurtTime == 9 && !wasBlocked) {
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                        wasBlocked = true;
                    }
                    break;
            }
        }
    };

    @Override
    public void onDisable() {
        if(wasBlocked && !mc.thePlayer.isSwingInProgress)
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));

        wasBlocked = blocking = false;

        currentTarget = null;
        targetList.clear();

        super.onDisable();
    }

    private void sortAndAddTargets() {
        targetList.clear();
        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if(entity instanceof EntityPlayer && mc.thePlayer.getDistanceToEntity(entity) <= reach.getCurrentValue() && entity != mc.thePlayer && mc.thePlayer.canEntityBeSeen(entity) && !entity.isInvisible() && !FriendCommand.isFriend(((EntityPlayer) entity).getName())) {
                final EntityPlayer entityPlayer = (EntityPlayer) entity;
                targetList.add(entityPlayer);
            }
        });
        targetList.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
    }
    private final IEventListener<UpdateEvent> onUpdateEvent = event -> {
        setSuffix(String.valueOf(reach.getCurrentValue()));
    };
    public boolean isBlocking() {
        return blocking;
    }
}
