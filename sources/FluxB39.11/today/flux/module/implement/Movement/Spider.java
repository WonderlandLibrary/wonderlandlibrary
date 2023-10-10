package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.event.PreUpdateEvent;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.ModeValue;

public class Spider extends Module{
    public static ModeValue mode = new ModeValue("Spider", "Mode", "Vanilla");

    private double jumpY;

    public Spider() {
        super("Spider", Category.Movement, mode);
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent event) {
        if (this.mc.thePlayer.isCollidedHorizontally) {
            if (mode.getValue().equals("Vanilla"))
                this.mc.thePlayer.motionY = 0.3f;
        }

        if(this.mc.thePlayer.onGround){
            this.jumpY = this.mc.thePlayer.posY;
        }
    }
}
