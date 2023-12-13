package today.flux.module.implement.Movement.fly;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.util.MathHelper;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;

/**
 * Created by John on 2017/01/12.
 */
public class OldNCP  extends SubModule {
    private boolean flynig;
    private boolean second;

    double startY;

    public OldNCP() {
        super("OldNCP", "Fly");
    }

    @EventTarget
    public void onUpdatePost(PostUpdateEvent event) {
        if (this.mc.thePlayer.onGround) {
            flynig = false;
            second = false;
            startY = this.mc.thePlayer.posY;
            return;
        }

        if (mc.thePlayer.fallDistance > 3.0F && !flynig) {
            flynig = true;
        }

        if (this.mc.thePlayer.movementInput.jump && flynig && mc.thePlayer.fallDistance > 1.12F && this.mc.thePlayer.posY < startY - 1.0D) {
            if (!second) {
                second = true;
                this.mc.thePlayer.motionY = 0;
                this.mc.thePlayer.motionX = 0;
                this.mc.thePlayer.motionZ = 0;
            } else {

                this.mc.thePlayer.motionY = 0.42F;
                float var1 = this.mc.thePlayer.rotationYaw * 0.017453292F;
                this.mc.thePlayer.motionX -= (double) (MathHelper.sin(var1) * 0.07F);
                this.mc.thePlayer.motionZ += (double) (MathHelper.cos(var1) * 0.07F);
                this.mc.thePlayer.fallDistance = 0.0f;
            }
        }
    }
}