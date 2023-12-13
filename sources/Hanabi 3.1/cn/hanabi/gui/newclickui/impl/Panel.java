package cn.hanabi.gui.newclickui.impl;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.newclickui.ClickUI;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.TranslateUtil;
import cn.hanabi.utils.fontmanager.HanabiFonts;
import java.awt.Color;
import java.util.Iterator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Panel {
   private final Category category;
   private float desX;
   private float desY;
   private float x;
   private float y;
   private float dragX;
   private float dragY;
   private boolean needMove;
   private final long delay;
   private int wheel;
   private final String name;
   private final TranslateUtil anima = new TranslateUtil(1.0F, 0.0F);
   private final TranslateUtil translate = new TranslateUtil(0.0F, 0.0F);
   private final TimeHelper timer = new TimeHelper();

   public Panel(float x, float y, long delay, Category category) {
      this.desX = x;
      this.desY = y;
      this.delay = delay;
      this.category = category;
      this.name = category.toString();
      this.anima.setXY(1.0F, 0.0F);
      this.needMove = false;
      this.dragX = 0.0F;
      this.dragY = 0.0F;
   }

   public void draw(float mouseX, float mouseY) {
      if (this.needMove) {
         this.setXY(mouseX - this.dragX, mouseY - this.dragY);
      }

      if (!Mouse.isButtonDown(0) && this.needMove) {
         this.needMove = false;
      }

      float alpha = 0.01F;
      float yani = 0.0F;
      if (this.timer.isDelayComplete(this.delay)) {
         this.anima.interpolate(100.0F, 20.0F, 0.02F);
         alpha = this.anima.getX() / 100.0F;
         yani = this.anima.getY();
      }

      this.x = this.desX;
      this.y = this.desY + 20.0F - yani;
      HFontRenderer titlefont = Hanabi.INSTANCE.fontManager.usans25;
      HFontRenderer font = Hanabi.INSTANCE.fontManager.raleway20;
      HFontRenderer icon = Hanabi.INSTANCE.fontManager.icon30;
      float mstartY = this.y + 40.0F;
      float maddY = 22.0F;
      RenderUtil.drawRoundRect((double)this.x, (double)this.y, (double)(this.x + 140.0F), (double)(this.y + 260.0F), 4, (new Color(0, 0, 0, (int)(60.0F * alpha))).getRGB());
      titlefont.drawString(this.name, this.x + 15.0F, this.y + 12.0F, (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
      String iconstr = "";
      switch (this.category.toString()) {
         case "Combat":
            iconstr = HanabiFonts.ICON_CLICKGUI_COMBAT;
            break;
         case "Movement":
            iconstr = HanabiFonts.ICON_CLICKGUI_MOVEMENT;
            break;
         case "Player":
            iconstr = HanabiFonts.ICON_CLICKGUI_PLAYER;
            break;
         case "Render":
            iconstr = HanabiFonts.ICON_CLICKGUI_RENDER;
            break;
         case "World":
            iconstr = HanabiFonts.ICON_CLICKGUI_WORLD;
            break;
         case "Ghost":
            iconstr = HanabiFonts.ICON_CLICKGUI_GHOST;
      }

      icon.drawString(iconstr, this.x + 110.0F, this.y + 12.0F, (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)this.x, (int)mstartY - 4, 140, (int)(this.y + 260.0F - 5.0F - (mstartY - 4.0F)));
      float modY = this.translate.getX();
      Iterator var18 = ModManager.getModules(this.category).iterator();

      while(true) {
         Mod m;
         do {
            if (!var18.hasNext()) {
               GL11.glDisable(3089);
               GL11.glPopMatrix();
               float moduleHeight = modY - this.translate.getX() - 1.0F;
               if (Mouse.hasWheel() && ClickUI.isHover(mouseX, mouseY, this.x, mstartY - 4.0F, this.x + 140.0F, this.y + 260.0F - 5.0F) && ClickUI.currentMod == null) {
                  int i;
                  if (ClickUI.real > 0 && this.wheel < 0) {
                     for(i = 0; i < 5 && this.wheel < 0; ++i) {
                        this.wheel += 5;
                     }
                  } else {
                     for(i = 0; i < 5 && ClickUI.real < 0 && moduleHeight > this.y + 260.0F - 5.0F - (mstartY - 4.0F) && (float)Math.abs(this.wheel) < moduleHeight - (this.y + 260.0F - 5.0F - (mstartY - 4.0F)); ++i) {
                        this.wheel -= 5;
                     }
                  }
               }

               this.translate.interpolate((float)this.wheel, 0.0F, 0.2F);
               float sliderh = Math.min(this.y + 260.0F - 5.0F - (mstartY - 4.0F), (this.y + 260.0F - 5.0F - (mstartY - 4.0F)) * (this.y + 260.0F - 5.0F - (mstartY - 4.0F)) / moduleHeight);
               float slidert = -(this.y + 260.0F - 5.0F - (mstartY - 4.0F) - sliderh) * this.translate.getX() / (moduleHeight - (this.y + 260.0F - 5.0F - (mstartY - 4.0F)));
               if (sliderh < this.y + 260.0F - 5.0F - (mstartY - 4.0F)) {
                  GL11.glPushMatrix();
                  GL11.glEnable(3089);
                  RenderUtil.doGlScissor((int)this.x + 129, (int)mstartY - 4, 1, (int)(this.y + 260.0F - 5.0F - (mstartY - 4.0F)));
                  RenderUtil.drawRect(this.x + 129.0F, mstartY - 4.0F + slidert, this.x + 130.0F, mstartY - 4.0F + slidert + sliderh, (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
                  GL11.glDisable(3089);
                  GL11.glPopMatrix();
               }

               return;
            }

            m = (Mod)var18.next();
         } while(ClickUI.isSearching && !ClickUI.searchcontent.equalsIgnoreCase("") && ClickUI.searchcontent != null && !m.getName().toLowerCase().contains(ClickUI.searchcontent.toLowerCase()));

         boolean mhover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, this.x, mstartY + modY - 4.0F, this.x + 140.0F, mstartY + modY + 17.0F) && ClickUI.isHover(mouseX, mouseY, this.x, mstartY - 4.0F, this.x + 140.0F, this.y + 260.0F - 5.0F);
         font.drawString(m.getName(), this.x + 25.0F, mstartY + modY, mhover ? (new Color(180, 180, 180, (int)(255.0F * alpha))).getRGB() : (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
         if (m.isEnabled()) {
            Hanabi.INSTANCE.fontManager.micon15.drawString("B", this.x + 12.0F, mstartY + modY + 2.0F, mhover ? (new Color(180, 180, 180, (int)(255.0F * alpha))).getRGB() : (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
         }

         if (m.hasValues()) {
            font.drawString(">", this.x + 110.0F, mstartY + modY, mhover ? (new Color(180, 180, 180, (int)(255.0F * alpha))).getRGB() : (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
         }

         modY += maddY;
      }
   }

   public void drawShadow(float mouseX, float mouseY) {
      if (this.needMove) {
         this.setXY(mouseX - this.dragX, mouseY - this.dragY);
      }

      if (!Mouse.isButtonDown(0) && this.needMove) {
         this.needMove = false;
      }

      float alpha = 0.01F;
      float yani = 0.0F;
      if (this.timer.isDelayComplete(this.delay)) {
         this.anima.interpolate(100.0F, 20.0F, 0.02F);
         alpha = this.anima.getX() / 100.0F;
         yani = this.anima.getY();
      }

      this.x = this.desX;
      this.y = this.desY + 20.0F - yani;
      HFontRenderer titlefont = Hanabi.INSTANCE.fontManager.usans25;
      HFontRenderer font = Hanabi.INSTANCE.fontManager.raleway20;
      HFontRenderer icon = Hanabi.INSTANCE.fontManager.icon30;
      float mstartY = this.y + 40.0F;
      float maddY = 22.0F;
      int spread = 2;
      RenderUtil.drawRoundRect((double)(this.x - (float)spread), (double)(this.y - (float)spread), (double)(this.x + 140.0F + (float)spread), (double)(this.y + 260.0F + (float)spread), 4 + spread, (new Color(0, 0, 0, (int)(120.0F * alpha))).getRGB());
      titlefont.drawString(this.name, this.x + 15.0F, this.y + 12.0F, (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
      String iconstr = "";
      switch (this.category.toString()) {
         case "Combat":
            iconstr = HanabiFonts.ICON_CLICKGUI_COMBAT;
            break;
         case "Movement":
            iconstr = HanabiFonts.ICON_CLICKGUI_MOVEMENT;
            break;
         case "Player":
            iconstr = HanabiFonts.ICON_CLICKGUI_PLAYER;
            break;
         case "Render":
            iconstr = HanabiFonts.ICON_CLICKGUI_RENDER;
            break;
         case "World":
            iconstr = HanabiFonts.ICON_CLICKGUI_WORLD;
            break;
         case "Ghost":
            iconstr = HanabiFonts.ICON_CLICKGUI_GHOST;
      }

      icon.drawString(iconstr, this.x + 110.0F, this.y + 12.0F, (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)this.x, (int)mstartY - 4, 140, (int)(this.y + 260.0F - 5.0F - (mstartY - 4.0F)));
      float modY = this.translate.getX();
      Iterator var19 = ModManager.getModules(this.category).iterator();

      while(true) {
         Mod m;
         do {
            if (!var19.hasNext()) {
               GL11.glDisable(3089);
               GL11.glPopMatrix();
               float moduleHeight = modY - this.translate.getX() - 1.0F;
               if (Mouse.hasWheel() && ClickUI.isHover(mouseX, mouseY, this.x, mstartY - 4.0F, this.x + 140.0F, this.y + 260.0F - 5.0F) && ClickUI.currentMod == null) {
                  int i;
                  if (ClickUI.real > 0 && this.wheel < 0) {
                     for(i = 0; i < 5 && this.wheel < 0; ++i) {
                        this.wheel += 5;
                     }
                  } else {
                     for(i = 0; i < 5 && ClickUI.real < 0 && moduleHeight > this.y + 260.0F - 5.0F - (mstartY - 4.0F) && (float)Math.abs(this.wheel) < moduleHeight - (this.y + 260.0F - 5.0F - (mstartY - 4.0F)); ++i) {
                        this.wheel -= 5;
                     }
                  }
               }

               this.translate.interpolate((float)this.wheel, 0.0F, 0.2F);
               float sliderh = Math.min(this.y + 260.0F - 5.0F - (mstartY - 4.0F), (this.y + 260.0F - 5.0F - (mstartY - 4.0F)) * (this.y + 260.0F - 5.0F - (mstartY - 4.0F)) / moduleHeight);
               float slidert = -(this.y + 260.0F - 5.0F - (mstartY - 4.0F) - sliderh) * this.translate.getX() / (moduleHeight - (this.y + 260.0F - 5.0F - (mstartY - 4.0F)));
               if (sliderh < this.y + 260.0F - 5.0F - (mstartY - 4.0F)) {
                  GL11.glPushMatrix();
                  GL11.glEnable(3089);
                  RenderUtil.doGlScissor((int)this.x + 129, (int)mstartY - 4, 1, (int)(this.y + 260.0F - 5.0F - (mstartY - 4.0F)));
                  RenderUtil.drawRect(this.x + 129.0F, mstartY - 4.0F + slidert, this.x + 130.0F, mstartY - 4.0F + slidert + sliderh, (new Color(255, 255, 255, (int)(120.0F * alpha))).getRGB());
                  GL11.glDisable(3089);
                  GL11.glPopMatrix();
               }

               return;
            }

            m = (Mod)var19.next();
         } while(ClickUI.isSearching && !ClickUI.searchcontent.equalsIgnoreCase("") && ClickUI.searchcontent != null && !m.getName().toLowerCase().contains(ClickUI.searchcontent.toLowerCase()));

         boolean mhover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, this.x, mstartY + modY - 4.0F, this.x + 140.0F, mstartY + modY + 17.0F) && ClickUI.isHover(mouseX, mouseY, this.x, mstartY - 4.0F, this.x + 140.0F, this.y + 260.0F - 5.0F);
         font.drawString(m.getName(), this.x + 25.0F, mstartY + modY, mhover ? (new Color(70, 70, 70, (int)(255.0F * alpha))).getRGB() : (new Color(0, 0, 0, (int)(255.0F * alpha))).getRGB());
         if (m.isEnabled()) {
            Hanabi.INSTANCE.fontManager.micon15.drawString("B", this.x + 12.0F, mstartY + modY + 2.0F, mhover ? (new Color(180, 180, 180, (int)(255.0F * alpha))).getRGB() : (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
         }

         if (m.hasValues()) {
            font.drawString(">", this.x + 110.0F, mstartY + modY, mhover ? (new Color(180, 180, 180, (int)(255.0F * alpha))).getRGB() : (new Color(255, 255, 255, (int)(255.0F * alpha))).getRGB());
         }

         modY += maddY;
      }
   }

   public void handleMouseClicked(float mouseX, float mouseY, int key) {
      float mstartY = this.y + 40.0F;
      float maddY = 22.0F;
      boolean tophover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, this.x, this.y, this.x + 140.0F, mstartY);
      if (tophover && key == 0) {
         this.dragX = mouseX - this.desX;
         this.dragY = mouseY - this.desY;
         this.needMove = true;
      }

      float modY = mstartY + this.translate.getX();
      Iterator var8 = ModManager.getModules(this.category).iterator();

      while(true) {
         Mod m;
         do {
            if (!var8.hasNext()) {
               return;
            }

            m = (Mod)var8.next();
         } while(ClickUI.isSearching && !ClickUI.searchcontent.equalsIgnoreCase("") && ClickUI.searchcontent != null && !m.getName().toLowerCase().contains(ClickUI.searchcontent.toLowerCase()));

         boolean mhover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, this.x, modY - 4.0F, this.x + 140.0F, modY + 17.0F) && ClickUI.isHover(mouseX, mouseY, this.x, mstartY - 4.0F, this.x + 140.0F, this.y + 260.0F - 5.0F);
         if (mhover) {
            if (key == 0) {
               m.set(!m.isEnabled(), false);
            }

            if (key == 1 && m.hasValues()) {
               ClickUI.currentMod = m;
               ClickUI.settingwheel = 0;
               ClickUI.settingtranslate.setXY(0.0F, 0.0F);
               ClickUI.animatranslate.setXY(0.0F, 0.0F);
            }
         }

         modY += maddY;
      }
   }

   public void handleMouseReleased(float mouseX, float mouseY, int key) {
      float mstartY = this.y + 40.0F;
      boolean tophover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, this.x, this.y, this.x + 140.0F, mstartY);
      if (tophover && key == 0) {
         this.dragX = mouseX - this.desX;
         this.dragY = mouseY - this.desY;
         this.needMove = false;
      }

   }

   public void resetAnimation() {
      this.timer.reset();
      this.anima.setXY(1.0F, 0.0F);
      this.needMove = false;
      this.dragX = 0.0F;
      this.dragY = 0.0F;
   }

   public void resetTranslate() {
      this.translate.setXY(0.0F, 0.0F);
      this.wheel = 0;
   }

   public void setXY(float x, float y) {
      this.desX = x;
      this.desY = y;
   }
}
