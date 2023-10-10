package today.flux.module.implement.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.util.MathHelper;
import today.flux.event.PreUpdateEvent;
import today.flux.module.SubModule;

public class AACYPort extends SubModule {
    public AACYPort() {
        super("AACYPort", "Speed");
    }

    @Override
    public void onDisable() {
        super.onDisable();

        this.mc.getTimer().timerSpeed = 1.0f;
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent e) {
        if (!mc.gameSettings.keyBindForward.pressed || this.mc.thePlayer.isInWater()) {
            this.mc.getTimer().timerSpeed = 1.0f;
            return;
        }

        if (this.mc.thePlayer.onGround) {
            if (!this.mc.thePlayer.isSneaking()) {
                this.mc.thePlayer.motionY = 0.388f;
                sprint(0.204F);
                mc.getTimer().timerSpeed = 1.35F;
            }
        } else {
            if(this.mc.thePlayer.motionY > 0) {
                mc.thePlayer.motionY = -0.2;
            }
            mc.getTimer().timerSpeed = 0.89F;
        }
    }

    private void sprint(float speed) {
        float f = this.mc.thePlayer.rotationYaw * 0.017453292F;
        this.mc.thePlayer.motionX -= (double) (MathHelper.sin(f) * speed);
        this.mc.thePlayer.motionZ += (double) (MathHelper.cos(f) * speed);
    }
}
