package de.violence.gui.mains;

import de.violence.font.FontManager;
import de.violence.gui.VSetting;
import de.violence.gui.mains.Button;
import de.violence.mcgui.utils.GuiMains;
import de.violence.save.manager.FileManager;
import de.violence.ui.Colours;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Component {
   private Button button;
   private VSetting vSetting;
   public int x;
   public int y;
   public boolean hovered;
   public int width;
   public int heigth;
   private Minecraft mc = Minecraft.getMinecraft();

   public Component(Button button, VSetting vSetting, int width) {
      this.button = button;
      this.vSetting = vSetting;
      this.width = width;
      this.heigth = 11;
   }

   public void onRender(int mouseX, int mouseY) {
      GuiScreen var10000;
      int y;
      if(this.vSetting.getSettingType() == VSetting.SettingType.BUTTON) {
         this.hovered = GuiMains.isHovered(mouseX, mouseY, this.x, this.y, this.width + 10, this.heigth - 6);
         GL11.glPushMatrix();
         GlStateManager.disableTexture2D();
         var10000 = this.mc.currentScreen;
         GuiScreen.drawRect(this.x, this.y, this.x + this.width + 10, this.y + (this.heigth - 3), Colours.getColor(0, 0, 0, 150));
         var10000 = this.mc.currentScreen;
         GuiScreen.drawRect(this.x, this.y, this.x + this.width + 10, this.y + (this.heigth - 3), Integer.MIN_VALUE);
         GlStateManager.enableTexture2D();
         GL11.glPopMatrix();
         y = -1;
         if(this.hovered) {
            y = Colours.getColor(128, 128, 128, 255);
         }

         if(this.vSetting.isToggled()) {
            y = Colours.getColor(50, 100, 255, 255);
         }

         FontManager.clickGUI.drawCenteredString(this.vSetting.getName(), this.x + this.width / 2 + 5, this.y - 2, y);
      } else if(this.vSetting.getSettingType() == VSetting.SettingType.SLIDER) {
         this.hovered = GuiMains.isHovered(mouseX, mouseY, this.x, this.y, this.width + 10, this.heigth - 4);
         if(this.hovered && Mouse.isButtonDown(0)) {
            this.sliderUpdate((double)mouseX);
         }

         GL11.glPushMatrix();
         GlStateManager.disableTexture2D();
         var10000 = this.mc.currentScreen;
         GuiScreen.drawRect(this.x, this.y, this.x + this.width + 10, this.y + this.heigth, Colours.getColor(0, 0, 0, 150));
         var10000 = this.mc.currentScreen;
         GuiScreen.drawRect(this.x, this.y, this.x + this.width + 10, this.y + this.heigth, Integer.MIN_VALUE);
         var10000 = this.mc.currentScreen;
         GuiScreen.drawRect(this.x + 3, this.y, (int)((double)this.x + this.vSetting.getCurrent() / this.vSetting.getMax() * (double)this.width + 9.0D), this.y + this.heigth - 2, Colours.getColor(255, 170, 0, 150));
         GlStateManager.enableTexture2D();
         GL11.glPopMatrix();
         FontManager.clickGUI.drawCenteredString(this.vSetting.getName() + " " + this.vSetting.getCurrent(), this.x + this.width / 2 + 5, this.y - 1, this.hovered?Colours.getColor(200, 200, 200, 255):-1);
         GlStateManager.disableTexture2D();
      } else if(this.vSetting.getSettingType() == VSetting.SettingType.MODE) {
         this.hovered = GuiMains.isHovered(mouseX, mouseY, this.x, this.y, this.width + 10, this.heigth - 6);
         if(this.vSetting.extend) {
            y = this.y + 9;

            for(Iterator var5 = this.vSetting.getModeList().iterator(); var5.hasNext(); y += 9) {
               String modes = (String)var5.next();
               boolean hovered = GuiMains.isHovered(mouseX, mouseY, this.x, y, this.width + 10, 5);
               GlStateManager.disableTexture2D();
               var10000 = this.mc.currentScreen;
               GuiScreen.drawRect(this.x, y, this.x + this.width + 10, y + 9, Colours.getColor(0, 0, 0, 255));
               var10000 = this.mc.currentScreen;
               GuiScreen.drawRect(this.x, y, this.x + this.width + 10, y + 9, Colours.getColor(64, 64, 64, 50));
               GlStateManager.enableTexture2D();
               GlStateManager.pushMatrix();
               if(this.vSetting.getActiveMode().equalsIgnoreCase(modes)) {
                  FontManager.clickGUI.drawCenteredString(modes, this.x + this.width / 2 + 6, y - 1, Colours.getMain(255));
               } else {
                  FontManager.clickGUI.drawCenteredString(modes, this.x + this.width / 2 + 6, y - 1, hovered?Colours.getColor(200, 200, 200, 255):-1);
               }

               GlStateManager.popMatrix();
            }

            var10000 = this.mc.currentScreen;
            GuiScreen.drawRect(this.x, y, this.x + this.width + 10, y + 1, Colours.getColor(0, 0, 0, 255));
         }

         GL11.glPushMatrix();
         GlStateManager.disableTexture2D();
         var10000 = this.mc.currentScreen;
         GuiScreen.drawRect(this.x, this.y, this.x + this.width + 10, this.y + this.heigth - 2, Colours.getColor(0, 0, 0, 255));
         var10000 = this.mc.currentScreen;
         GuiScreen.drawRect(this.x, this.y, this.x + this.width + 10, this.y + this.heigth - 2, Integer.MIN_VALUE);
         GlStateManager.enableTexture2D();
         GL11.glPopMatrix();
         FontManager.clickGUI.drawCenteredString(this.vSetting.getName(), this.x + (this.width + 10) / 2, this.y - 1, this.hovered?Colours.getColor(200, 200, 200, 255):-1);
      }

   }

   private void sliderUpdate(double mouseX) {
      double startX = (double)this.x;
      double endX = (double)(this.x + this.width + 11);
      double betweens = mouseX - startX;
      if(betweens >= 0.0D) {
         betweens /= (double)(this.width + 11) / this.vSetting.getMax();
         if(betweens > this.vSetting.getMax()) {
            betweens = this.vSetting.getMax();
         }

         if(this.vSetting.getMin() > betweens) {
            betweens = this.vSetting.getMin();
         }

         betweens = (double)Math.round(betweens * 10.0D) / 10.0D;
         if(this.vSetting.round) {
            betweens = (double)Math.round(betweens);
         }

         FileManager.gui.setDouble("Slider" + this.vSetting.getName() + ":" + this.vSetting.getModule().getName(), Double.valueOf(betweens));
         FileManager.gui.save();
         this.vSetting.current = betweens;
      }
   }

   public void onClick(int mouseX, int mouseY, int mouseButton) {
      if(this.hovered) {
         if(this.vSetting.getSettingType() == VSetting.SettingType.BUTTON) {
            this.vSetting.toggled = !this.vSetting.toggled;
            FileManager.gui.setBoolean("Button" + this.vSetting.getName() + ":" + this.vSetting.getModule().getName(), Boolean.valueOf(this.vSetting.toggled));
            FileManager.gui.save();
         }

         if(this.vSetting.getSettingType() == VSetting.SettingType.MODE) {
            this.vSetting.extend = !this.vSetting.extend;
         }
      }

      if(this.vSetting.extend) {
         int y = this.y + 9;

         for(Iterator var6 = this.vSetting.getModeList().iterator(); var6.hasNext(); y += 9) {
            String modes = (String)var6.next();
            boolean hovered = GuiMains.isHovered(mouseX, mouseY, this.x, y, this.width + 10, 5);
            if(hovered) {
               this.vSetting.activeMode = modes;
               FileManager.gui.setString("Mode" + this.vSetting.getName() + ":" + this.vSetting.getModule().getName(), modes);
               FileManager.gui.save();
            }
         }
      }

   }

   public Button getButton() {
      return this.button;
   }

   public VSetting getVSetting() {
      return this.vSetting;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeigth() {
      return this.heigth;
   }
}
