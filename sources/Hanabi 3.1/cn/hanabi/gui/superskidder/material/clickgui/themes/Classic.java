package cn.hanabi.gui.superskidder.material.clickgui.themes;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.superskidder.material.clickgui.ColorUtils;
import cn.hanabi.gui.superskidder.material.clickgui.Main;
import cn.hanabi.gui.superskidder.material.clickgui.Tab;
import cn.hanabi.gui.superskidder.material.clickgui.Tabs.SettingsTab;
import cn.hanabi.gui.superskidder.material.clickgui.button.CButton;
import cn.hanabi.gui.superskidder.material.clickgui.renderManager.Rect;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import java.util.Iterator;
import org.lwjgl.input.Mouse;

public class Classic extends Main {
   public void initGui() {
      super.initGui();
      this.Blist = new CButton("Modules", "Client/clickgui/modules.png", 2.0F, 4.0F, 12.0F, 8.0F);
      this.Btheme = new CButton("Modules", "Client/clickgui/theme.png", 3.0F, 3.0F, 11.0F, 11.0F);
      this.Bsettings = new CButton("Modules", "Client/clickgui/settings.png", 3.0F, 3.0F, 10.5F, 10.5F);
      this.categories.clear();
      Category[] var1 = Category.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Category mt = var1[var3];
         this.categories.add(new cn.hanabi.gui.superskidder.material.clickgui.Category(mt, 0, false));
      }

   }

   public void updateScreen() {
      super.updateScreen();
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
   }

   public void drawTasksBar() {
      super.drawTasksBar();
      RenderUtil.drawRect(windowX + animListX, windowY + 30.0F, windowX + windowWidth, windowY + 50.0F, ColorUtils.reAlpha(clientColor.getRGB(), 0.6F));
      float x = 4.0F;

      Iterator var2;
      final Tab t;
      float swidth;
      for(var2 = tabs.iterator(); var2.hasNext(); x += swidth) {
         t = (Tab)var2.next();
         if (currentTab == t) {
            t.render(this.mouseX, this.mouseY);
         }

         swidth = (float)(Hanabi.INSTANCE.fontManager.wqy16.getStringWidth(t.name) + 14);
         t.x = t.animationUtils.animate(windowX + x + animListX, t.x, this.drag ? 2.0F : 0.1F);
         (new Rect(t.x, windowY + 30.0F, swidth, 20.0F, new Color(0, 0, 0, 0), new Runnable() {
            public void run() {
               if (Mouse.isButtonDown(0)) {
                  Main.currentTab = t;
               }

            }
         })).render(this.mouseX, this.mouseY);
         if (isHovered(t.x + swidth - 4.0F, windowY + 30.0F, t.x + swidth + 4.0F, windowY + 50.0F, this.mouseX, this.mouseY)) {
            Hanabi.INSTANCE.fontManager.wqy18.drawString("-", t.x + swidth - 6.0F, windowY + 40.0F, (new Color(255, 0, 0)).getRGB());
         } else {
            Hanabi.INSTANCE.fontManager.wqy18.drawString("-", t.x + swidth - 6.0F, windowY + 40.0F, (new Color(255, 255, 255)).getRGB());
         }

         if (t == currentTab) {
            Hanabi.INSTANCE.fontManager.wqy16.drawString(t.name, t.x + 2.0F, windowY + 38.0F, -1);
            RenderUtil.drawRect(t.x, windowY + 48.0F, t.x + swidth, windowY + 50.0F, clientColor.getRGB());
         } else {
            Hanabi.INSTANCE.fontManager.wqy16.drawString(t.name, t.x + 2.0F, windowY + 38.0F, (new Color(255, 255, 255, 150)).getRGB());
         }
      }

      if (this.Bsettings.realized) {
         this.Bsettings.realized = false;
         var2 = tabs.iterator();

         while(var2.hasNext()) {
            t = (Tab)var2.next();
            if (t.name.equals("Settings")) {
               currentTab = t;
               return;
            }
         }

         tabs.add(new SettingsTab());
      }

   }

   public void drawList(float mouseX, float mouseY) {
      super.drawList(mouseX, mouseY);
      if (this.Blist.realized) {
         animListX = listAnim.animate(140.0F, animListX, 0.2F);
      } else {
         animListX = listAnim.animate(0.0F, animListX, 0.2F);
      }

      if (animListX != 0.0F) {
         RenderUtil.drawGradientSideways((double)(windowX + animListX), (double)(windowY + 30.0F), (double)(windowX + animListX + 3.0F), (double)(windowY + windowHeight), (new Color(50, 50, 50, 100)).getRGB(), (new Color(255, 255, 255, 0)).getRGB());
         float dWheel = (float)Mouse.getDWheel();
         if (dWheel > 0.0F && this.listRoll2 < 0.0F) {
            this.listRoll2 += 32.0F;
         } else if (dWheel < 0.0F) {
            this.listRoll2 -= 32.0F;
         }

         this.listRoll = rollAnim.animate(this.listRoll2, this.listRoll, 0.05F);
         float modsY = windowY + 35.0F + this.listRoll;

         for(Iterator var5 = this.categories.iterator(); var5.hasNext(); modsY += 25.0F) {
            cn.hanabi.gui.superskidder.material.clickgui.Category mt = (cn.hanabi.gui.superskidder.material.clickgui.Category)var5.next();
            if (!mt.show && !mt.needRemove) {
               (new Rect(windowX, modsY, animListX, 20.0F, new Color(255, 255, 255), new Runnable() {
                  public void run() {
                  }
               })).render(mouseX, mouseY);
               Hanabi.INSTANCE.fontManager.wqy18.drawString(mt.moduleType.name(), windowX + animListX - 130.0F, modsY + 5.0F, (new Color(0, 0, 0)).getRGB());
            } else {
               Hanabi.INSTANCE.fontManager.wqy18.drawString(mt.moduleType.name(), windowX + animListX - 130.0F, modsY + 5.0F, (new Color(0, 0, 0)).getRGB());
               modsY += 25.0F;
               mt.modsY2 = 0.0F;

               for(Iterator var7 = ModManager.getModules(mt.moduleType).iterator(); var7.hasNext(); mt.modsY2 += 20.0F) {
                  Mod m = (Mod)var7.next();
                  (new Rect(windowX, modsY + mt.modsY2, animListX, 15.0F, new Color(255, 255, 255), new Runnable() {
                     public void run() {
                     }
                  })).render(mouseX, mouseY);
                  if (modsY + 5.0F + mt.modsY2 < modsY + mt.modsY3 + 25.0F) {
                     Hanabi.INSTANCE.fontManager.wqy18.drawString(m.getName(), windowX + animListX - 120.0F, modsY + 5.0F + mt.modsY2, m.isEnabled() ? HUD.design.getColor() : (new Color(50, 50, 50)).getRGB());
                  }
               }

               if (mt.needRemove) {
                  mt.modsY3 = mt.rollAnim2.animate(-25.0F, mt.modsY3, 0.1F);
                  if (mt.modsY3 == -25.0F) {
                     mt.needRemove = false;
                     mt.show = false;
                  }
               } else {
                  mt.modsY3 = mt.rollAnim2.animate(mt.modsY2, mt.modsY3, 0.1F);
               }

               modsY += mt.modsY3;
            }
         }
      }

   }

   public void drawWindow(final float mouseX, final float mouseY) {
      RenderUtil.drawRoundedRect(windowX, windowY, windowX + windowWidth, windowY + windowHeight, 2.0F, (new Color(255, 255, 255)).getRGB());
      (new Rect(windowX, windowY, windowWidth, 30.0F, clientColor, new Runnable() {
         public void run() {
            if (Mouse.isButtonDown(0)) {
               Classic.this.drag = true;
               if (Classic.this.mouseDX == 0.0F) {
                  Classic.this.mouseDX = mouseX - Main.windowX;
               }

               if (Classic.this.mouseDY == 0.0F) {
                  Classic.this.mouseDY = mouseY - Main.windowY;
               }

            }
         }
      })).render(mouseX, mouseY);
   }
}
