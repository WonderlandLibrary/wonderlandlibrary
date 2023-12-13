package today.flux.module.implement.Misc;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import today.flux.Flux;
import today.flux.event.PreUpdateEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.FriendManager;
import today.flux.utility.RotationUtils;
import today.flux.utility.WorldRenderUtils;
import today.flux.utility.WorldUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by John on 2017/06/20.
 */

public class Aimbot extends Module {
    public static BooleanValue headshot = new BooleanValue("Aimbot", "Headshot", false);
    public static FloatValue range = new FloatValue("Aimbot", "Range", 100.0f, 1.0f, 500.0f, 5.0f);
    public static FloatValue deviation = new FloatValue("Aimbot", "Deviation", 1.5f, 0.0f, 10.0f, 0.1f);

    private Vec3 aimed;

    public Aimbot() {
        super("Aimbot", Category.Misc, false);
    }

    @EventTarget(Priority.LOW)
    private void onUpdatePre(PreUpdateEvent event) {
        if (event.isModified() || event.isCancelled() || !this.mc.gameSettings.keyBindUseItem.pressed) {
            this.aimed = null;
            return;
        }

        final List<EntityPlayer> targets = WorldUtil.getLivingPlayers().stream()
                .filter(this::isValid)
                .sorted(Comparator.comparing(e -> this.mc.thePlayer.getDistanceToEntity(e)))
                .collect(Collectors.toList());

        if (targets.size() <= 0)
            return;

        this.aimed = this.getFixedLocation(targets.get(0), deviation.getValue(), headshot.getValue());

        final float[] rotations = RotationUtils.getRotationToLocation(this.aimed);

        event.setYaw(rotations[0]);
        event.setPitch(rotations[1]);
    }

    @EventTarget
    private void onRender3D(final WorldRenderEvent event) {
        if (this.aimed == null)
            return;

        double posX = this.aimed.xCoord - this.mc.getRenderManager().getRenderPosX();
        double posY = this.aimed.yCoord - this.mc.getRenderManager().getRenderPosY();
        double posZ = this.aimed.zCoord - this.mc.getRenderManager().getRenderPosZ();

        WorldRenderUtils.drawBlockESP(posX - 0.5, posY - 0.5, posZ - 0.5, new Color(255,0,0,100).getRGB(), new Color(0xFFE900).getRGB(), 1.0f);
    }

    private Vec3 getFixedLocation(final EntityLivingBase entity, final float velocity, final boolean head) {
        double x = entity.posX + ((entity.posX - entity.lastTickPosX) * velocity);
        double y = entity.posY + ((entity.posY - entity.lastTickPosY) * (velocity * 0.3)) + (head ? entity.getEyeHeight() : 1.0);
        double z = entity.posZ + ((entity.posZ - entity.lastTickPosZ) * velocity);

        return new Vec3(x, y, z);
    }

    private boolean isValid(final EntityPlayer entity) {
        if (entity.isDead || entity.getHealth() <= 0)
            return false;

        if (this.mc.thePlayer.getDistanceToEntity(entity) > range.getValue())
            return false;

        if (!this.mc.thePlayer.canEntityBeSeenFixed(entity))
            return false;

        if (FriendManager.isTeam(entity) && Flux.teams.getValue())
            return false;

        if (ModuleManager.antiBotsMod.isEnabled() && ModuleManager.antiBotsMod.isNPC(entity))
            return false;

        return true;
    }
}
