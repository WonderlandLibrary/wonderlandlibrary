package de.violence.tabgui.componenents;

import de.violence.font.FontManager;
import de.violence.module.Module;
import de.violence.ui.Colours;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.lwjgl.input.Keyboard;

public class ModuleTab {
   private Module module;
   private int x;
   private int y;
   private int width;
   private int heigth;
   private int listId;
   private Minecraft mc = Minecraft.getMinecraft();
   private int fadeX;
   private boolean finished;
   private long lastToggle;

   public ModuleTab(Module module, int x, int y, int width) {
      this.module = module;
      this.x = x;
      this.y = y;
      this.width = width;
      this.heigth = 11;
   }

   public void draw(boolean selected) {
      if(selected && Keyboard.isKeyDown(28) && System.currentTimeMillis() - this.lastToggle > 300L) {
         this.module.setToggled(!this.module.isToggled());
         this.lastToggle = System.currentTimeMillis();
      }

      int color = Integer.MIN_VALUE;
      if(selected) {
         color = Colours.getMain(255);
      }

      if(selected && !this.finished) {
         this.fadeX = 0;
      }

      if(this.fadeX < this.width) {
         ++this.fadeX;
         this.finished = true;
      }

      if(!selected && this.finished) {
         this.finished = false;
      }

      GuiIngame var10000 = this.mc.ingameGUI;
      GuiIngame.drawRect(this.x, this.y, this.x + this.width + 1, this.y + this.heigth, Colours.getColor(0, 0, 0, 150));
      var10000 = this.mc.ingameGUI;
      GuiIngame.drawRect(this.x, this.y, this.x + this.fadeX, this.y + this.heigth, Colours.getColor(0, 0, 0, 150));
      var10000 = this.mc.ingameGUI;
      GuiIngame.drawRect(this.x, this.y, this.x + this.fadeX, this.y + this.heigth, color);
      FontManager.arrayList_Bignoodletitling.drawString(this.module.getName(), this.x, this.y, -1);
   }

   public int getWidth() {
      return this.width;
   }

   public Module getModule() {
      return this.module;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }
}
