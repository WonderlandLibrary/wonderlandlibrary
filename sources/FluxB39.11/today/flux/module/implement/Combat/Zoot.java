package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import today.flux.event.PostUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;

public class Zoot extends Module {
    public Zoot() {
        super("Zoot", Category.Combat, false);
    }


    @EventTarget
    public void onUpdate(PostUpdateEvent event) {
        if (this.mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
            this.mc.thePlayer.removePotionEffect(Potion.blindness.getId());
        }
        if (this.mc.thePlayer.isPotionActive(Potion.confusion.getId())) {
            this.mc.thePlayer.removePotionEffect(Potion.confusion.getId());
        }
        if (this.mc.thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
            this.mc.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
        }
    }
}
