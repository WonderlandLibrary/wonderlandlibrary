package today.flux.module.implement.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.MoveEvent;
import today.flux.module.SubModule;

public class Swift extends SubModule {
    private int motionDelay;
    public Swift() {
        super("Swift", "Speed");
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (this.mc.thePlayer.onGround) {
            ++this.motionDelay;
            this.motionDelay %= 2;
            if (this.motionDelay == 1) {
                this.mc.thePlayer.motionX *= 2.58;
                this.mc.thePlayer.motionZ *= 2.58;
            } else {
                this.mc.thePlayer.motionX /= 1.5;
                this.mc.thePlayer.motionZ /= 1.5;
            }
            this.mc.thePlayer.moveStrafing *= 0.0f;
            this.mc.thePlayer.motionY = 0.001;
        }
        this.mc.thePlayer.isAirBorne = false;
    }

}
