package space.clowdy.modules.impl;

import java.awt.Color;
import net.minecraft.client.gui.IngameGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;
import space.clowdy.utils.ColorUtils;

public class Watermark extends Module {
     public static int x = 5;
     public static int y = 15;

     @SubscribeEvent
     public void onRenderGameOverlayEvent(RenderGameOverlayEvent renderGameOverlayEvent) {
          if (renderGameOverlayEvent.getType() == ElementType.TEXT && !SelfDestruct.hidden) {
               String string3 = "Clowdy Lite | User: " + this.mc.player.getName().getString();
               IngameGui.fill(renderGameOverlayEvent.getMatrixStack(), x + 1, y - 6, this.mc.fontRenderer.getStringWidth(string3) + x + 9, y + 12, (new Color(1325400064, true)).getRGB());
               IngameGui.fill(renderGameOverlayEvent.getMatrixStack(), x + 2, y - 5, x + 4, y + 11, ColorUtils.clientColor.getRGB());
               this.mc.fontRenderer.drawString(renderGameOverlayEvent.getMatrixStack(), string3, (float)(x + 7), (float)y, ColorUtils.clientColor.getRGB());
          }

     }

     public Watermark() {
          super("Watermark", "\u00120B5@<0@:0 G8B0", 0, Category.VISUAL);
     }
}
