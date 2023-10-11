package io.github.raze.modules.collection.visual;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.util.ResourceLocation;

public class CustomCape extends AbstractModule {

    public CustomCape() {
        super("CustomCape", "Equip a custom cape.", ModuleCategory.VISUAL);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        mc.thePlayer.setLocationOfCape(new ResourceLocation("raze/capes/Classic.png"));
    }

    @Override
    public void onDisable() {
        if(mc.thePlayer == null) return;
        mc.thePlayer.setLocationOfCape(null);
    }

}
