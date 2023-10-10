package vestige.module.impl.visual;

import vestige.event.Listener;
import vestige.event.Priority;
import vestige.event.impl.MotionEvent;
import vestige.event.impl.RotationsRenderEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;

public class Rotations extends Module {

    private float yaw, pitch;
    private float lastYaw, lastPitch;

    private boolean customRender;

    private BooleanSetting smooth = new BooleanSetting("Smooth", true);

    public Rotations() {
        super("Rotations", Category.VISUAL);
        this.addSettings(smooth);
    }

    @Listener(Priority.LOWER)
    public void onMotion(MotionEvent event) {
        customRender = mc.thePlayer.rotationYaw != event.getYaw() || mc.thePlayer.rotationPitch != event.getPitch();

        this.lastYaw = yaw;
        this.lastPitch = pitch;

        yaw = event.getYaw();
        pitch = event.getPitch();
    }

    @Listener(Priority.LOWER)
    public void onRotsRender(RotationsRenderEvent event) {
        if(customRender) {
            float partialTicks = event.getPartialTicks();

            event.setYaw(smooth.isEnabled() ? interpolateRotation(lastYaw, yaw, partialTicks) : yaw);
            event.setBodyYaw(smooth.isEnabled() ? interpolateRotation(lastYaw, yaw, partialTicks) : yaw);
            event.setPitch(smooth.isEnabled() ? lastPitch + (pitch - lastPitch) * partialTicks : pitch);
        }
    }

    protected float interpolateRotation(float par1, float par2, float par3) {
        float f;

        for (f = par2 - par1; f < -180.0F; f += 360.0F) {
            ;
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return par1 + par3 * f;
    }

}
