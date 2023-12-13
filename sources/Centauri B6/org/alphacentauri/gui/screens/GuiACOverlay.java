package org.alphacentauri.gui.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.alphacentauri.AC;
import org.alphacentauri.gui.components.TabButton;

public class GuiACOverlay extends GuiScreen {
   private final GuiScreen parent;
   private ArrayList tabs = new ArrayList();

   public GuiACOverlay(GuiScreen parent) {
      this.parent = parent;
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if(mouseButton == 0) {
         for(TabButton tab : this.tabs) {
            tab.onClick(mouseX, mouseY);
            if(tab.selected) {
               if(tab.text.equalsIgnoreCase("Close")) {
                  this.mc.displayGuiScreen(this.parent);
               } else if(tab.text.equalsIgnoreCase("Toggle Ghost Mode")) {
                  AC.setGhost(!AC.isGhost());
                  tab.selected = false;
               }
            }
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.drawGradientRect((int)((double)((float)this.width) * 0.1D), (int)((double)((float)this.height) * 0.1D), (int)((double)((float)this.width) * 0.9D), (int)((double)((float)this.height) * 0.9D), -1207433208, -1207433208);
      this.drawGradientRect((int)((double)((float)this.width) * 0.28D), (int)((double)((float)this.height) * 0.1D), (int)((double)((float)this.width) * 0.28D) + 1, (int)((double)((float)this.height) * 0.9D), -5723992, -5723992);
      this.drawBorder((int)((double)((float)this.width) * 0.1D), (int)((double)((float)this.height) * 0.1D), (int)((double)((float)this.width) * 0.9D), (int)((double)((float)this.height) * 0.9D), -5723992);
      this.tabs.forEach(TabButton::draw);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void onResize(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
      mcIn.displayGuiScreen(new GuiACOverlay(this.parent));
   }

   public void updateScreen() {
   }

   public void initGui() {
      this.tabs.add(new TabButton(this, (int)((double)((float)this.width) * 0.1D), (int)((double)((float)this.height) * 0.1D), (int)((double)((float)this.width) * 0.28D) + 1, (int)((double)((float)this.height) * 0.2D), "Chat"));
      this.tabs.add(new TabButton(this, (int)((double)((float)this.width) * 0.1D), (int)((double)((float)this.height) * 0.2D - 1.0D), (int)((double)((float)this.width) * 0.28D) + 1, (int)((double)((float)this.height) * 0.3D), "Toggle Ghost Mode"));
      this.tabs.add(new TabButton(this, (int)((double)((float)this.width) * 0.1D), (int)((double)((float)this.height) * 0.8D - 1.0D), (int)((double)((float)this.width) * 0.28D) + 1, (int)((double)((float)this.height) * 0.9D), "Close"));
   }

   private void drawBorder(int x1, int y1, int x2, int y2, int color) {
      this.drawGradientRect(x1, y1, x2, y1 + 1, color, color);
      this.drawGradientRect(x1, y2 - 1, x2, y2, color, color);
      this.drawGradientRect(x1, y1, x1 + 1, y2, color, color);
      this.drawGradientRect(x2 - 1, y1, x2, y2, color, color);
   }
}
