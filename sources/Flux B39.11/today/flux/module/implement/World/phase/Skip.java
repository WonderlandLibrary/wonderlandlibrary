package today.flux.module.implement.World.phase;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.BBSetEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.SubModule;
import today.flux.utility.BlockUtils;

/**
 * Created by John on 2017/04/27.
 */
public class Skip extends SubModule {
    public Skip() {
        super("Skip", "Phase");
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent event) {
        if (BlockUtils.isInsideBlock() && this.mc.thePlayer.isSneaking()) {
            final float yaw = this.mc.thePlayer.rotationYaw;
            final float distance = 1.0f;
            this.mc.thePlayer.getEntityBoundingBox().offsetAndUpdate(distance * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, distance * Math.sin(Math.toRadians(yaw + 90.0f)));
        }

    }

    @EventTarget
    public void onSetBoundingbox(BBSetEvent event) {
        if (event.getBoundingBox() != null && event.getBoundingBox().maxY > this.mc.thePlayer.getEntityBoundingBox().minY && this.mc.thePlayer.isSneaking()) {
            event.setBoundingBox(null);
        }
    }
}