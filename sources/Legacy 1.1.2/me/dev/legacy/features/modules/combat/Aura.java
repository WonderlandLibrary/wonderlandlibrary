package me.dev.legacy.features.modules.combat;

import me.dev.legacy.Legacy;
import me.dev.legacy.event.events.UpdateWalkingPlayerEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.DamageUtil;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.MathUtil;
import me.dev.legacy.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Aura extends Module {
    public static Entity target;
    private final Timer timer = new Timer();
    public Setting<Float> range = register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
    public Setting<Boolean> delay = register(new Setting("HitDelay", Boolean.valueOf(true)));
    public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(false)));
    public Setting<Boolean> onlySharp = register(new Setting("SwordOnly", Boolean.valueOf(true)));
    public Setting<Float> raytrace = register(new Setting("Raytrace", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F), "Wall Range."));
    public Setting<Boolean> players = register(new Setting("Players", Boolean.valueOf(true)));
    public Setting<Boolean> mobs = register(new Setting("Mobs", Boolean.valueOf(false)));
    public Setting<Boolean> animals = register(new Setting("Animals", Boolean.valueOf(false)));
    public Setting<Boolean> vehicles = register(new Setting("Entities", Boolean.valueOf(false)));
    public Setting<Boolean> projectiles = register(new Setting("Projectiles", Boolean.valueOf(false)));
    public Setting<Boolean> tps = register(new Setting("TpsSync", Boolean.valueOf(true)));
    public Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));

    public Aura() {
        super("Aura", "Kills aura.", Module.Category.COMBAT, true, false, false);
    }

    public void onTick() {
        if (!rotate.getValue().booleanValue())
            doKillaura();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && rotate.getValue().booleanValue())
            doKillaura();
    }

    private void doKillaura() {
        if (onlySharp.getValue().booleanValue() && !EntityUtil.holdingWeapon(mc.player)) {
            target = null;
            return;
        }
        int wait = !delay.getValue().booleanValue() ? 0 : (int) (DamageUtil.getCooldownByWeapon(mc.player) * (tps.getValue().booleanValue() ? Legacy.serverManager.getTpsFactor() : 1.0F));
        if (!timer.passedMs(wait))
            return;
        target = getTarget();
        if (target == null)
            return;
        if (rotate.getValue().booleanValue())
            Legacy.rotationManager.lookAtEntity(target);
        EntityUtil.attackEntity(target, packet.getValue().booleanValue(), true);
        timer.reset();
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = range.getValue().floatValue();
        double maxHealth = 36.0D;
        for (Entity entity : mc.world.playerEntities) {
            if (((!players.getValue().booleanValue() || !(entity instanceof EntityPlayer)) && (!animals.getValue().booleanValue() || !EntityUtil.isPassive(entity)) && (!mobs.getValue().booleanValue() || !EntityUtil.isMobAggressive(entity)) && (!vehicles.getValue().booleanValue() || !EntityUtil.isVehicle(entity)) && (!projectiles.getValue().booleanValue() || !EntityUtil.isProjectile(entity))) || (entity instanceof net.minecraft.entity.EntityLivingBase &&
                    EntityUtil.isntValid(entity, distance)))
                continue;
            if (!mc.player.canEntityBeSeen(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && mc.player.getDistanceSq(entity) > MathUtil.square(raytrace.getValue().floatValue()))
                continue;
            if (target == null) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer) entity, 18)) {
                target = entity;
                break;
            }
            if (mc.player.getDistanceSq(entity) < distance) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
            if (EntityUtil.getHealth(entity) < maxHealth) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
        }
        return target;
    }

    public String getDisplayInfo() {
        if (target instanceof EntityPlayer)
            return target.getName();
        return null;
    }
}
