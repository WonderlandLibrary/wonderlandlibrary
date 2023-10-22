package cn.hanabi.gui.superskidder.material.clickgui;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.superskidder.material.clickgui.Tabs.MainTab;
import cn.hanabi.gui.superskidder.material.clickgui.Tabs.ModuleTab;
import cn.hanabi.gui.superskidder.material.clickgui.button.CButton;
import cn.hanabi.gui.superskidder.material.clickgui.renderManager.Rect;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Main extends GuiScreen {
   public static float windowX;
   public static float windowY;
   public static float windowWidth;
   public static float windowHeight;
   public CButton Blist;
   public CButton Btheme;
   public CButton Bsettings;
   public float mouseX;
   public float mouseY;
   public ArrayList categories = new ArrayList();
   public static float animListX;
   public float listRoll2 = 0.0F;
   public float listRoll = 0.0F;
   public static AnimationUtils listAnim = new AnimationUtils();
   public static AnimationUtils rollAnim = new AnimationUtils();
   public float bg;
   public static AnimationUtils bgAnim = new AnimationUtils();
   public static ArrayList tabs = new ArrayList();
   public static Tab currentTab;
   public static Color clientColor;
   ScaledResolution sr;
   public static Object currentObj;
   public float mouseDX;
   public float mouseDY;
   public boolean drag;
   public float mouseDX2;
   public float mouseDY2;
   public boolean drag2;

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      Iterator var4 = tabs.iterator();

      while(var4.hasNext()) {
         Tab t = (Tab)var4.next();
         t.mouseClicked((float)mouseX, (float)mouseY);
      }

      if (!isHovered(windowX + windowWidth - 5.0F, windowY + windowHeight - 5.0F, windowX + windowWidth + 5.0F, windowY + windowHeight + 5.0F, (float)mouseX, (float)mouseY) && Mouse.isButtonDown(0)) {
         this.drag2 = false;
         this.mouseDX2 = 0.0F;
         this.mouseDY2 = 0.0F;
      }

      this.Blist.onMouseClicked((float)mouseX, (float)mouseY);
      this.Btheme.onMouseClicked((float)mouseX, (float)mouseY);
      this.Bsettings.onMouseClicked((float)mouseX, (float)mouseY);
      float modsY = windowY + 35.0F + this.listRoll;

      Iterator var7;
      for(Iterator var11 = this.categories.iterator(); var11.hasNext(); modsY += 25.0F) {
         final Category mt = (Category)var11.next();
         if (mt.show) {
            (new Rect(windowX, modsY, animListX, 20.0F, new Color(255, 255, 255), new Runnable() {
               public void run() {
                  if (Mouse.isButtonDown(0)) {
                     if (!mt.show) {
                        mt.show = true;
                     } else {
                        mt.needRemove = !mt.needRemove;
                     }
                  }

               }
            })).render((float)mouseX, (float)mouseY);
            modsY += 25.0F;

            for(var7 = ModManager.getModules(mt.moduleType).iterator(); var7.hasNext(); modsY += 20.0F) {
               final Mod m = (Mod)var7.next();
               if (modsY > windowY + 30.0F && modsY < windowY + windowHeight && !mt.needRemove) {
                  (new Rect(windowX, modsY, animListX, 15.0F, new Color(255, 255, 255), new Runnable() {
                     public void run() {
                        if (Mouse.isButtonDown(0)) {
                           m.set(!m.isEnabled());
                        } else if (Mouse.isButtonDown(1)) {
                           ModuleTab modT = new ModuleTab(m);
                           Iterator var2 = Main.tabs.iterator();

                           while(var2.hasNext()) {
                              Tab mx = (Tab)var2.next();
                              if (mx.name.equals(modT.name)) {
                                 Main.currentTab = mx;
                                 return;
                              }
                           }

                           Main.tabs.add(modT);
                           Main.currentTab = modT;
                        }

                     }
                  })).render((float)mouseX, (float)mouseY);
               }

               Hanabi.INSTANCE.fontManager.wqy18.drawString(m.getName(), windowX + animListX - 120.0F, modsY + 5.0F, (new Color(50, 50, 50)).getRGB());
            }
         } else {
            (new Rect(windowX, modsY, animListX, 20.0F, new Color(255, 255, 255), new Runnable() {
               public void run() {
                  if (Mouse.isButtonDown(0)) {
                     mt.show = !mt.show;
                  }

               }
            })).render((float)mouseX, (float)mouseY);
         }
      }

      ArrayList tabs2 = new ArrayList();
      float x = 4.0F;

      float swidth;
      final Tab tab;
      for(var7 = tabs.iterator(); var7.hasNext(); x += swidth) {
         tab = (Tab)var7.next();
         swidth = (float)(Hanabi.INSTANCE.fontManager.wqy16.getStringWidth(tab.name) + 14);
         tab.x = tab.animationUtils.animate(windowX + x + animListX, tab.x, this.drag ? 2.0F : 0.1F);
         (new Rect(tab.x, windowY + 30.0F, swidth, 20.0F, new Color(0, 0, 0, 0), new Runnable() {
            public void run() {
               if (Mouse.isButtonDown(0)) {
                  Main.currentTab = tab;
               }

            }
         })).render((float)mouseX, (float)mouseY);
         if (isHovered(tab.x + swidth - 4.0F, windowY + 30.0F, tab.x + swidth + 4.0F, windowY + 50.0F, (float)mouseX, (float)mouseY) && Mouse.isButtonDown(0)) {
            tabs2.add(tab);
         }
      }

      var7 = tabs2.iterator();

      while(var7.hasNext()) {
         tab = (Tab)var7.next();
         tabs.remove(tab);
      }

   }

   public void initGui() {
      this.sr = new ScaledResolution(this.mc);
      if (this.sr.getScaledWidth() < 550 && this.sr.getScaledHeight() < 300) {
         windowWidth = (float)(this.sr.getScaledWidth() - 10);
         windowHeight = (float)(this.sr.getScaledHeight() - 10);
         windowX = ((float)this.sr.getScaledWidth() - windowWidth) / 2.0F;
         windowY = ((float)this.sr.getScaledHeight() - windowHeight) / 2.0F;
      }

      if (windowWidth == 0.0F) {
         windowWidth = 550.0F;
      }

      if (windowHeight == 0.0F) {
         windowHeight = 300.0F;
      }

      if (windowX == 0.0F) {
         windowX = ((float)this.sr.getScaledWidth() - windowWidth) / 2.0F;
      }

      if (windowY == 0.0F) {
         windowY = ((float)this.sr.getScaledHeight() - windowHeight) / 2.0F;
      }

      if (tabs.size() == 0) {
         Tab tab = new MainTab();
         tabs.add(tab);
         currentTab = tab;
      }

      super.initGui();
   }

   public void drawWindow(float mouseX, float mouseY) {
   }

   public void drawMWindow(int mouseX, int mouseY) {
      if (currentObj == null) {
         this.mouseX = (float)mouseX;
         this.mouseY = (float)mouseY;
         if (this.mouseDX != 0.0F && this.drag && Mouse.isButtonDown(0)) {
            windowX = this.mouseX - this.mouseDX;
         } else {
            this.drag = false;
            this.mouseDX = 0.0F;
            this.mouseDY = 0.0F;
         }

         if (this.mouseDY != 0.0F && this.drag && Mouse.isButtonDown(0)) {
            windowY = this.mouseY - this.mouseDY;
         } else {
            this.drag = false;
            this.mouseDX = 0.0F;
            this.mouseDY = 0.0F;
         }
      }

      this.drawWindow(this.mouseX, this.mouseY);
   }

   public void drawTasksBar() {
   }

   public void drawBar(float mouseX, float mouseY) {
      this.Blist.onRender(windowX + 8.0F, windowY + 8.0F, mouseX, mouseY);
      this.Btheme.onRender(windowX + windowWidth - 20.0F, windowY + 8.0F, mouseX, mouseY);
      this.Bsettings.onRender(windowX + windowWidth - 40.0F, windowY + 8.0F, mouseX, mouseY);
   }

   public void drawBG() {
      this.bg = bgAnim.animate((float)this.sr.getScaledWidth(), this.bg, 0.01F);
      RenderUtil.circle((float)(this.sr.getScaledWidth() / 2), (float)(this.sr.getScaledHeight() / 2), this.bg, ColorUtils.reAlpha(clientColor.getRGB(), 0.1F));
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      clientColor = new Color(HUD.design.getColor());
      this.drawBG();
      this.drawMWindow(mouseX, mouseY);
      this.drawBar((float)mouseX, (float)mouseY);
      GL11.glEnable(3089);
      RenderUtil.doGlScissor(windowX, windowY + 30.0F, windowWidth, windowHeight - 30.0F);
      if (isHovered(windowX + windowWidth - 5.0F, windowY + windowHeight - 5.0F, windowX + windowWidth + 5.0F, windowY + windowHeight + 5.0F, (float)mouseX, (float)mouseY) && Mouse.isButtonDown(0) || this.drag2) {
         if (windowWidth > 560.0F) {
            windowWidth = (float)mouseX - windowX - this.mouseDX2;
         } else if ((float)mouseX - windowX > windowWidth) {
            windowWidth = (float)mouseX - windowX - this.mouseDX2;
         }

         if (windowHeight > 310.0F) {
            windowHeight = (float)mouseY - windowY - this.mouseDY2;
         } else if ((float)mouseY - windowY > windowHeight) {
            windowHeight = (float)mouseY - windowY - this.mouseDY2;
         }
      }

      if (isHovered(windowX + windowWidth - 5.0F, windowY + windowHeight - 5.0F, windowX + windowWidth + 5.0F, windowY + windowHeight + 5.0F, (float)mouseX, (float)mouseY) && Mouse.isButtonDown(0)) {
         this.drag2 = true;
         this.mouseDX2 = (float)mouseX - (windowX + windowWidth);
         this.mouseDY2 = (float)mouseY - (windowY + windowHeight);
      }

      if (!Mouse.isButtonDown(0)) {
         this.drag2 = false;
         this.mouseDX2 = 0.0F;
         this.mouseDY2 = 0.0F;
      }

      this.drawTasksBar();
      this.drawList((float)mouseX, (float)mouseY);
      GL11.glDisable(3089);
      if (!this.drag2 && !isHovered(windowX + windowWidth - 5.0F, windowY + windowHeight - 5.0F, windowX + windowWidth + 5.0F, windowY + windowHeight + 5.0F, (float)mouseX, (float)mouseY)) {
         if (Mouse.isGrabbed()) {
            Mouse.setCursorPosition(mouseX * 2, (this.sr.getScaledHeight() - mouseY) * 2);
            Mouse.setGrabbed(false);
         }
      } else {
         Mouse.setGrabbed(true);
         RenderUtil.drawImage(new ResourceLocation("client/clickgui/cur.png"), mouseX - 4, mouseY - 4, 16, 16);
      }

   }

   public void drawList(float mouseX, float mouseY) {
   }

   public void onGuiClosed() {
      super.onGuiClosed();
   }

   public static boolean isHovered(float x, float y, float x2, float y2, float mouseX, float mouseY) {
      return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
   }
}
