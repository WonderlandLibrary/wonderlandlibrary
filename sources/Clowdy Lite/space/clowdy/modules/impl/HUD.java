package space.clowdy.modules.impl;

import java.util.Iterator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;
import space.clowdy.modules.ModuleManager;
import space.clowdy.utils.ColorUtils;

public class HUD extends Module {
     @SubscribeEvent
     public void negr(RenderGameOverlayEvent renderGameOverlayEvent) {
          if (renderGameOverlayEvent.getType() == ElementType.TEXT && !SelfDestruct.hidden) {
               int integer3 = 10;
               int integer4 = 41;
               Iterator var4 = ModuleManager.modules.iterator();

               while(var4.hasNext()) {
                    Module domingo6 = (Module)var4.next();
                    if (domingo6.isEnabled()) {
                         this.mc.fontRenderer.drawString(renderGameOverlayEvent.getMatrixStack(), domingo6.getName(), (float)integer3, (float)integer4, ColorUtils.clientColor.getRGB());
                         integer4 += 10;
                    }
               }
          }

     }

     public HUD() {
          super("HUD", "\u0012K2>4 2:;NG5==KE <>4C;59 =0 M:@0=", 0, Category.VISUAL);
     }
}
