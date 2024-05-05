package client.module.impl.movement;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MoveEvent;
import client.event.impl.other.UpdateEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.ModuleManager;
import client.module.impl.combat.KillAura;
import client.module.impl.other.Scaffold;
import client.util.MoveUtil;
import client.util.RotationUtil;
import client.value.impl.BooleanValue;
import client.value.impl.NumberValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Target Strafe", description = "", category = Category.MOVEMENT)
public class TargetStrafe extends Module {

    private final BooleanValue speedRange = new BooleanValue("SpeedRange", this, false);
    private final NumberValue range = new NumberValue("Range", this, 1.0, 0.2, 4.0, 0.1, () -> !speedRange.getValue());

    private float yaw;
    private boolean left, colliding, active;

    @EventLink
    public final Listener<UpdateEvent> onUpdate = e -> {
        final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
        final EntityLivingBase target = moduleManager.get(KillAura.class).getTarget();
        active = moduleManager.get(KillAura.class).isEnabled() && target != null && !moduleManager.get(Scaffold.class).isEnabled() && (moduleManager.get(Flight.class).isEnabled() || moduleManager.get(Speed.class).isEnabled());
        if (active) {
            if (mc.thePlayer.isCollidedHorizontally && !colliding) left = !left;
            colliding = mc.thePlayer.isCollidedHorizontally;
            yaw = RotationUtil.get(target).y + 135 * (left ? -1 : 1);
            final double range = speedRange.getValue() ? MoveUtil.getSpeed() : this.range.getValue().doubleValue();
            yaw = RotationUtil.get(new Vec3(target.posX - MathHelper.sin(yaw * MathHelper.deg2Rad) * range, target.posY, target.posZ + MathHelper.cos(yaw * MathHelper.deg2Rad) * range)).y;
        }
    };

    @EventLink
    public final Listener<MoveEvent> onMove = e -> {
        if (active) {
            final double speed = MathHelper.sqrt_double(e.getX() * e.getX() + e.getZ() * e.getZ());
            final float yawRad = yaw * MathHelper.deg2Rad;
            e.setX(-MathHelper.sin(yawRad) * speed);
            e.setZ(MathHelper.cos(yawRad) * speed);
        }
    };
}