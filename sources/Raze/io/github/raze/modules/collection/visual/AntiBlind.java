package io.github.raze.modules.collection.visual;

import io.github.raze.events.collection.game.EventUpdate;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.potion.Potion;

public class AntiBlind extends AbstractModule {

    public AntiBlind() {
        super("AntiBlind", "Removes bad visual effects.", ModuleCategory.VISUAL);
    }

    @Listen
    public void onUpdate(EventUpdate eventUpdate) {
        if (mc.thePlayer.getActivePotionEffect(Potion.blindness) != null) {
            mc.thePlayer.removePotionEffect(Potion.blindness.getId());
        }

        if (mc.thePlayer.getActivePotionEffect(Potion.confusion) != null) {
            mc.thePlayer.removePotionEffect(Potion.confusion.getId());
        }
    }
}
