package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;

public class AirJump extends Module{

    public AirJump() {
        super("AirJump", Category.Movement, false);
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent event) {
        this.mc.thePlayer.onGround = true;
        this.mc.thePlayer.isAirBorne = false;
        this.mc.thePlayer.fallDistance = 0;
    }
}
