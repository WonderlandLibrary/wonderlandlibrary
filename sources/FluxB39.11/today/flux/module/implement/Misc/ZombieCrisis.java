package today.flux.module.implement.Misc;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.Vec3;
import today.flux.event.PreUpdateEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.RotationUtils;
import today.flux.utility.WorldRenderUtils;
import today.flux.utility.WorldUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ZombieCrisis extends Module {
    public static BooleanValue headshot = new BooleanValue("ZombieCrisis", "Headshot", false);
    public static BooleanValue gunattack = new BooleanValue("ZombieCrisis", "GunAttack", false);
    public static FloatValue range = new FloatValue("ZombieCrisis", "Range", 100.0f, 1.0f, 500.0f, 5.0f);
    public static FloatValue deviation = new FloatValue("ZombieCrisis", "Deviation", 1.5f, 0.0f, 10.0f, 0.1f);

    private Vec3 aimed;

    public ZombieCrisis() {
        super("ZombieCrisis", Category.Misc, false);
    }

    @EventTarget(Priority.LOW)
    private void onUpdatePre(PreUpdateEvent event) {
        if (event.isModified() || event.isCancelled() || !this.mc.gameSettings.keyBindUseItem.pressed && !gunattack.getValueState()) {
            this.aimed = null;
            return;
        }

        final List<EntityLivingBase> targets = WorldUtil.getLivingEntities().stream()
                .filter(this::isValid)
                .sorted(Comparator.comparing(e -> this.mc.thePlayer.getDistanceToEntity(e)))
                .collect(Collectors.toList());

        if (targets.size() <= 0)
            return;

        this.aimed = this.getFixedLocation(targets.get(0), deviation.getValue(), headshot.getValue());

        final float[] rotations = RotationUtils.getRotationToLocation(this.aimed);

        event.setYaw(rotations[0]);
        event.setPitch(rotations[1]);
        if(gunattack.getValueState()) {
        	mc.gameSettings.keyBindUseItem.pressed = true;
        	mc.gameSettings.keyBindUseItem.pressed = false;
        }
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

    private boolean isValid(final EntityLivingBase entity) {
    	if(!(entity instanceof EntityZombie))
    		return false;
    	
        if (entity.isDead || entity.getHealth() <= 0)
            return false;

        if (mc.thePlayer.getDistanceToEntity(entity) > range.getValue())
            return false;

        return mc.thePlayer.canEntityBeSeenFixed(entity);
    }
}
