package today.flux.module.implement.Combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import today.flux.Flux;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.ModeValue;
import today.flux.utility.FriendManager;
import today.flux.utility.RotationUtils;
import today.flux.utility.WorldUtil;
import today.flux.module.value.FloatValue;


public class BowAimbot extends Module {
    public static EntityLivingBase target;
    public static FloatValue fov = new FloatValue("BowAimbot", "FoV", 180f, 10f, 360f, 10f, "°");
    public static FloatValue range = new FloatValue("BowAimbot", "Range", 100.0f, 1.0f, 200.0f, 10.0f);

    public static ModeValue priority = new ModeValue("BowAimbot", "Priority", "Angle", "Angle", "Range");

    public BowAimbot() {
        super("BowAimbot", Category.Combat, priority);
    }

    @EventTarget
    public void onUpdatePre(PreUpdateEvent event) {
        if (event.isModified())
            return;

        // check bow || check is using bow
        if (mc.thePlayer.inventory.getCurrentItem().getItem() != Items.bow || !mc.thePlayer.isUsingItem()) {
            target = null;
            return;
        }

        target = this.getTarget();

        if (target == null) {
            return;
        }

        final float[] rotation = this.getPlayerRotations(target);

        event.setYaw(rotation[0]);
        event.setPitch(rotation[1]);
    }

    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
    }

    private float[] getPlayerRotations(final Entity entity) {
        double distanceToEnt = mc.thePlayer.getDistanceToEntity(entity);
        double predictX = entity.posX + (entity.posX - entity.lastTickPosX) * (distanceToEnt * 0.8);
        double predictZ = entity.posZ + (entity.posZ - entity.lastTickPosZ) * (distanceToEnt * 0.8);

        double x = predictX - mc.thePlayer.posX;
        double z = predictZ - mc.thePlayer.posZ;
        double h = entity.posY + 1.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());

        double h1 = Math.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;

        float pitch = -RotationUtils.getTrajAngleSolutionLow((float) h1, (float) h, 1);

        return new float[]{yaw, pitch};
    }

    private EntityLivingBase getTarget() {
        Stream<EntityPlayer> stream = WorldUtil.getLivingPlayers().stream()
                .filter(e -> !Flux.teams.getValue() || !FriendManager.isTeam(e))
                .filter(e -> !ModuleManager.antiBotsMod.isEnabled() || !ModuleManager.antiBotsMod.isNPC(e))
                .filter(mc.thePlayer::canEntityBeSeen)
                .filter(e -> RotationUtils.isVisibleFOV(e, fov.getValue()));

        if (BowAimbot.priority.isCurrentMode("Range")) {
            stream = stream.sorted(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)));
        } else if (BowAimbot.priority.isCurrentMode("Angle")) {
            stream = stream.sorted(Comparator.comparingDouble(RotationUtils::getYawToEntity));
        }
        List<EntityPlayer> list = stream.collect(Collectors.toList());
        if (list.size() <= 0)
            return null;

        return list.get(0);
    }

}
