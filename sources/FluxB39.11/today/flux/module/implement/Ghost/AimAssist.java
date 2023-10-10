package today.flux.module.implement.Ghost;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import today.flux.Flux;
import today.flux.event.RespawnEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.*;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AimAssist extends Module {
    private EntityLivingBase target;
    private DelayTimer speedLimit = new DelayTimer();
    public static FloatValue Hspeed = new FloatValue("AimAssist", "Speed (H)", 0.4f, 0.1f, 3.0f, 0.1f);
    public static FloatValue Vspeed = new FloatValue("AimAssist", "Speed (V)", 0.4f, 0.1f, 3.0f, 0.1f);
    public static FloatValue range = new FloatValue("AimAssist", "Range", 4.2f, 1.0f, 6.0f, 0.1f);
    public static FloatValue fov = new FloatValue("AimAssist", "FoV", 60.0f, 1.0f, 360.0f, 10f);
    public static BooleanValue AttackInvisible = new BooleanValue("AimAssist", "Invisible", true);
    public static BooleanValue autodisable = new BooleanValue("AimAssist", "Auto Disable", true);
    public static BooleanValue checksword = new BooleanValue("AimAssist", "Check Weapon", true);

    public AimAssist() {
        super("AimAssist", Category.Ghost, false);
    }

    @EventTarget
    public void onUpdate(final WorldRenderEvent event) {
        if (checksword.getValue() && !InvUtils.hasWeapon())
            return;

        this.target = this.getTarget();

        if (this.target == null || !speedLimit.hasPassed(1) || this.target.getDistanceToEntity(this.mc.thePlayer) > 5.0)
            return;

        if (this.mc.objectMouseOver.entityHit != null)
            return;


        this.mc.thePlayer.rotationYaw = this.faceTarget(target, Hspeed.getValue(), Vspeed.getValue(), false)[0];
        speedLimit.reset();
    }
    @EventTarget
    public void onRespawn(RespawnEvent respawnEvent) {
        if (autodisable.getValue()) {
            this.setEnabled(false);
            NotificationManager.show("Module", this.getName() + " Disabled (Auto)", Notification.Type.INFO);
        }
    }

    private boolean isVaildToAttack(EntityPlayer entity) {
        if (entity == null || entity.isDead || entity.getHealth() <= 0)
            return false;

        float range = this.mc.thePlayer.canEntityBeSeenFixed(entity) ? KillAura.range.getValue() : 3.2f;

        //range
        if (!RotationUtils.isValidRangeForNCP(entity, range))
            return false;

        //modify
        if (entity.isInvisible() && !AttackInvisible.getValue())
            return false;

        //friend
        if (Flux.INSTANCE.getFriendManager().isFriend(entity.getName()))
            return false;

        //team
        if (Flux.teams.getValue() && FriendManager.isTeam((EntityPlayer) entity))
            return false;

        //fov
        if (fov.getValue() != 360f && !RotationUtils.isVisibleFOV(entity, fov.getValue()))
            return false;

        //bots (last)
        if (ModuleManager.antiBotsMod.isEnabled() && ModuleManager.antiBotsMod.isNPC(entity))
            return false;

        return true;
    }

    private EntityPlayer getTarget() {
        List<EntityPlayer> players = WorldUtil.getLivingPlayers().stream().filter(this::isVaildToAttack).sorted(Comparator.comparing(e -> e.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toList());

        if (players.size() <= 0) {
            return null;
        }

        return players.get(0);
    }

    private float[] faceTarget(final Entity target, final float p_70625_2_, final float p_70625_3_, final boolean miss) {
        final double var4 = target.posX - this.mc.thePlayer.posX;
        final double var5 = target.posZ - this.mc.thePlayer.posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase) target;
            var7 = var6.posY + var6.getEyeHeight() - (this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight());
        } else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight());
        }
        final Random rnd = new Random();
        final float offset = miss ? (rnd.nextInt(15) * 0.25f + 5.0f) : 0.0f;
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float) (Math.atan2(var5 + offset, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float) (-(Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.5f : 0.0f) + offset, var8) * 180.0 / 3.141592653589793));
        final float pitch = changeRotation(this.mc.thePlayer.rotationPitch, var10, p_70625_3_);
        final float yaw = changeRotation(this.mc.thePlayer.rotationYaw, var9, p_70625_2_);
        return new float[]{yaw, pitch};
    }

    private float changeRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }
}