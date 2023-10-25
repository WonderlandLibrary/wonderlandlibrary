package club.async.util;

import club.async.interfaces.MinecraftInterface;

import club.async.module.impl.combat.KillAura;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class AuraUtil implements MinecraftInterface {

    public int targetIndex;
    private final TimeUtil switchTimer = new TimeUtil();
    private final TimeUtil attackTimer = new TimeUtil();
    private final TimeUtil particleDelay = new TimeUtil();

    public void setTarget() {
        KillAura.getInstance().target = null;
        List<EntityLivingBase> livingEntities = getLivingEntities();
        if (KillAura.getInstance().priority.getCurrMode().equalsIgnoreCase("Health"))
            livingEntities.sort(new HealthSorter());
        else
            livingEntities.sort(new DistanceSorter());
        if (livingEntities.size() > 0) {
            if (KillAura.getInstance().mode.getCurrMode().equalsIgnoreCase("Switch")) {
                if (switchTimer.hasTimePassed(KillAura.getInstance().delay.getLong())) {
                    targetIndex++;
                    switchTimer.reset();
                }
                if (targetIndex >= livingEntities.size())
                    targetIndex = 0;
            } else {
                targetIndex = 0;
            }
            KillAura.getInstance().target = livingEntities.get(targetIndex);
        }
    }

    public void attack(EntityLivingBase entityLivingBase) {
        int cps = (int) Math.round(RandomUtil.getRandomNumber(KillAura.getInstance().minAPS.getInt(),KillAura.getInstance().maxAPS.getInt()));
        if (!attackTimer.hasTimePassed(1000 / cps))
            return;

        switch (KillAura.getInstance().attackMode.getCurrMode())
        {
            case "Normal":
                mc.thePlayer.swingItem();
                if (KillAura.getInstance().keepSprint.get())
                    mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entityLivingBase, C02PacketUseEntity.Action.ATTACK));
                else
                    mc.playerController.attackEntity(mc.thePlayer, entityLivingBase);
                break;
            case "Mouse Click":
                mc.clickMouse();
                if (KillAura.getInstance().keepSprint.get())
                    mc.thePlayer.setSprinting(true);
                break;
        }
        attackTimer.reset();
    }

    private boolean isValid(EntityLivingBase entityLivingBase) {
        return (mc.thePlayer.getDistanceToEntity(entityLivingBase) < KillAura.getInstance().range.getDouble()) &&
                ((!KillAura.getInstance().deathCheck.get() || entityLivingBase.isEntityAlive())) &&
                (entityLivingBase instanceof EntityPlayer || entityLivingBase instanceof EntityMob || entityLivingBase instanceof EntityAnimal) &&
                (!KillAura.getInstance().wallCheck.get() || mc.thePlayer.canEntityBeSeen(entityLivingBase)) &&
               // (Async.INSTANCE.getSettingsManager().getSettingByName("AuraScaffoldCheck").getValBoolean() && !Async.INSTANCE.getModuleManager().getModule("Scaffold").isToggled() || !Async.INSTANCE.getSettingsManager().getSettingByName("AuraScaffoldCheck").getValBoolean()) &&
                (!KillAura.getInstance().guiCheck.get() || mc.currentScreen == null) &&
                entityLivingBase != mc.thePlayer;
    }

    public void renderParticles() {
        if (!particleDelay.hasTimePassed(KillAura.getInstance().particleDelay.getLong()) || KillAura.getInstance().particleDelay.getLong() == 0)
            return;
        mc.effectRenderer.emitParticleAtEntity(KillAura.getInstance().target, EnumParticleTypes.CRIT);
        mc.effectRenderer.emitParticleAtEntity(KillAura.getInstance().target, EnumParticleTypes.CRIT_MAGIC);
        particleDelay.reset();

    }

    public void block() {
        mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 8100);
        mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
    }

    public void unblock() {
        mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    }

    private final class HealthSorter implements Comparator<EntityLivingBase> {
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            return Double.compare(PlayerUtil.getEffectiveHealth(o1), PlayerUtil.getEffectiveHealth(o2));
        }
    }

    private final class DistanceSorter implements Comparator<EntityLivingBase> {
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            return Double.compare(mc.thePlayer.getDistanceToEntity(o1), mc.thePlayer.getDistanceToEntity(o2));
        }
    }

    private List<EntityLivingBase> getLivingEntities() {
        List<EntityLivingBase> entities = new ArrayList<>();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase e = (EntityLivingBase) entity;
                if (isValid((EntityLivingBase) entity) && e != mc.thePlayer){
                    entities.add(e);
                }
            }
        }
        return entities;
    }
}
