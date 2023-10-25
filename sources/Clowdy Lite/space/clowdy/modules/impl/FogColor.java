package space.clowdy.modules.impl;

import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class FogColor extends Module {
     @SubscribeEvent
     public void onEntityViewRenderEventFOG(FogColors fogColors) {
          fogColors.setBlue(1.0F);
          fogColors.setGreen(0.0F);
          fogColors.setRed(0.0F);
     }

     public FogColor() {
          super("FogColor", "\u00187<5=O5B F25B BC<0=0", 0, Category.VISUAL);
     }
}
