package space.clowdy.modules.impl;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class AntiDebug extends Module {
     @SubscribeEvent
     public void ょ(RenderGameOverlayEvent renderGameOverlayEvent) {
          // $FF: Couldn't be decompiled
     }

     public AntiDebug() {
          super("Anti-Debug", "\u001d5 284=> DEBUG 8=D>@<0F88 ?@8 F3", 0, Category.DETECT);
     }
}
