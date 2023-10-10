package org.alphacentauri.gui.screens;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.alphacentauri.AC;
import org.alphacentauri.gui.screens.GuiAltManager;
import org.alphacentauri.management.data.Rect;
import org.alphacentauri.management.sexyness.background.BackgroundRenderer;
import org.alphacentauri.management.util.StringUtils;

public class AlphaCentauriMenu extends GuiScreen {
   private GuiScreen parent;
   private String[][] buttons = new String[2][];

   public AlphaCentauriMenu(GuiScreen parent) {
      this.parent = parent;
   }

   protected void actionPerformed(GuiButton button) {
      System.out.println(button.id);
      if(button.id == 4) {
         this.mc.displayGuiScreen(this.parent);
      } else if(button.id == 3) {
         AC.getConfig().setLSD(!AC.getConfig().isLSD());
         this.buildButtons();
      } else if(button.id == 5) {
         AC.getConfig().setToggleMessageVisible(!AC.getConfig().showToggleMsg());
         this.buildButtons();
      } else if(button.id == 0) {
         this.mc.displayGuiScreen(new GuiAltManager(this));
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void updateScreen() {
   }

   public void initGui() {
      this.buildButtons();
      this.setBgRenderer(new BackgroundRenderer(this, new Rect(0, 0, this.width, this.height), 100, -16728065, -16744792, 255, 255, 255));
   }

   private void buildButtons() {
      this.buttons[0] = new String[]{"Alts", "XRay Config", "Config Editor"};
      this.buttons[1] = new String[]{StringUtils.boolToColor(AC.getConfig().isLSD()) + "LSD", "Done", StringUtils.boolToColor(AC.getConfig().showToggleMsg()) + "Module Toggle Msg"};
      int id = 0;
      int maxWidth = 0;

      for(String[] button : this.buttons) {
         int l = button.length;
         if(maxWidth < l) {
            maxWidth = l;
         }
      }

      this.buttonList.clear();
      int baseX = (int)((float)this.width * 0.15F);
      int baseY = (int)((float)this.height * 0.15F);
      int widthPer = (int)((float)this.width * 0.7F) / maxWidth;
      int heightPer = (int)((float)this.height * 0.7F) / this.buttons.length;

      for(int y = 0; y < this.buttons.length; ++y) {
         for(int x = 0; x < maxWidth; ++x) {
            if(x < this.buttons[y].length) {
               this.buttonList.add(new GuiButton(id++, baseX + widthPer * x, baseY + heightPer * y, widthPer, heightPer, this.buttons[y][x]));
            }
         }
      }

   }
}
