package me.yarukon.hud.window;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.utils.Colors;
import me.yarukon.BlurBuffer;
import me.yarukon.YRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.Display;

@ObfuscationClass
public class HudWindow {
   public float x;
   public float y;
   public Minecraft mc;
   public float x2;
   public float y2;
   public boolean drag;
   public float draggableHeight;
   public String windowID;
   public float width;
   public float height;
   public String title;
   public boolean resizeable;
   public float minWidth;
   public float minHeight;
   public boolean resizing;
   public String icon;
   public float iconOffX;
   public float iconOffY;
   public boolean hide;
   public long lastClickTime;
   public boolean focused;
   public int titleBGColor;
   public int frameBGColor;
   public int textColor;

   public HudWindow(String windowID, float x, float y, float width, float height, String title) {
      this(windowID, x, y, width, height, title, "", 15.0F, 0.0F, 0.0F, false, 0.0F, 0.0F);
   }

   public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon) {
      this(windowID, x, y, width, height, title, icon, 15.0F, 0.0F, 0.0F, false, 0.0F, 0.0F);
   }

   public HudWindow(String windowID, float x, float y, float width, float height, String title, boolean resizeable, float minWidth, float minHeight) {
      this(windowID, x, y, width, height, title, "", 15.0F, 0.0F, 0.0F, resizeable, minWidth, minHeight);
   }

   public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon, float draggableHeight, float iconOffX, float iconOffY) {
      this(windowID, x, y, width, height, title, icon, draggableHeight, iconOffX, iconOffY, false, 0.0F, 0.0F);
   }

   public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon, float draggableHeight, float iconOffX, float iconOffY, boolean resizeable, float minWidth, float minHeight) {
      this.mc = Minecraft.getMinecraft();
      this.drag = false;
      this.resizing = false;
      this.hide = false;
      this.lastClickTime = 0L;
      this.focused = false;
      this.windowID = windowID;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.title = title;
      this.icon = icon;
      this.draggableHeight = draggableHeight;
      this.iconOffX = iconOffX;
      this.iconOffY = iconOffY;
      this.resizeable = resizeable;
      this.minWidth = minWidth;
      this.minHeight = minHeight;
   }

   public void draw() {
      if (OpenGlHelper.shadersSupported && Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && !(Boolean)HudWindowManager.blur.getValueState()) {
         if (Hanabi.INSTANCE.hasOptifine) {
            if (Hanabi.INSTANCE.fastRenderDisabled(this.mc.gameSettings)) {
               BlurBuffer.blurArea((float)((int)this.x), (float)((int)this.y), (float)((int)this.width), (float)((int)this.height + (int)this.draggableHeight), true);
            }
         } else {
            BlurBuffer.blurArea((float)((int)this.x), (float)((int)this.y), (float)((int)this.width), (float)((int)this.height + (int)this.draggableHeight), true);
         }
      }

      boolean isClassic = HUD.hudMode.isCurrentMode("Classic");
      this.textColor = isClassic ? -1 : -12566464;
      this.titleBGColor = isClassic ? -869305089 : -1140850689;
      this.frameBGColor = isClassic ? -1442840576 : Colors.getColor(166, 173, 176, 175);
      YRenderUtil.drawRect(this.x, this.y, this.width, this.draggableHeight, this.titleBGColor);
      YRenderUtil.drawRect(this.x, this.y + this.draggableHeight, this.width, this.height, this.frameBGColor);
      boolean displayIcon = !this.icon.isEmpty();
      if (displayIcon) {
         Hanabi.INSTANCE.fontManager.sessionInfoIcon14.drawString(this.icon, this.x + this.iconOffX, this.y + this.iconOffY, this.textColor);
      }

      Hanabi.INSTANCE.fontManager.usans15.drawString(this.title, this.x + 3.0F + (float)(displayIcon ? 8 : 0), this.y + this.draggableHeight / 2.0F - (float)Hanabi.INSTANCE.fontManager.usans15.FONT_HEIGHT / 2.0F, this.textColor);
      if (this.mc.currentScreen instanceof GuiChat) {
         YRenderUtil.drawRect(this.x, this.y, this.width, this.draggableHeight, -16732281);
         Hanabi.INSTANCE.fontManager.sessionInfoIcon20.drawString("A", this.x + this.iconOffX, this.y + this.iconOffY + 2.0F, Colors.WHITE.c);
         Hanabi.INSTANCE.fontManager.usans15.drawString("Move", this.x + 12.0F, this.y + this.draggableHeight / 2.0F - (float)Hanabi.INSTANCE.fontManager.usans15.FONT_HEIGHT / 2.0F, -1);
      }

   }

   public void postDraw() {
      if (this.resizeable && this.mc.currentScreen instanceof GuiChat) {
         Hanabi.INSTANCE.fontManager.sessionInfoIcon14.drawString("N", this.x + this.width - 8.0F, this.y + this.draggableHeight + this.height - 8.0F, this.textColor);
         if (this.resizing) {
            String gay = (int)this.width + ", " + (int)this.height;
            YRenderUtil.drawRectNormal(this.x + this.width + 2.0F, this.y + this.draggableHeight + this.height - 12.0F, this.x + this.width + 2.0F + (float)Hanabi.INSTANCE.fontManager.usans16.getStringWidth(gay) + 4.0F, this.y + this.draggableHeight + this.height, this.frameBGColor);
            Hanabi.INSTANCE.fontManager.usans16.drawString(gay, this.x + this.width + 4.0F, this.y + this.draggableHeight + this.height - 10.0F, this.textColor);
         }
      }

   }

   public void updateScreen() {
   }

   public void mouseClick(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0) {
         if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.draggableHeight)) {
            this.drag = true;
            this.lastClickTime = System.currentTimeMillis();
            this.x2 = (float)mouseX - this.x;
            this.y2 = (float)mouseY - this.y;
         }

         if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x + this.width - 8.0F, this.y + this.draggableHeight + this.height - 8.0F, 8.0F, 8.0F) && this.resizeable) {
            this.resizing = true;
            this.lastClickTime = System.currentTimeMillis();
            this.x2 = (float)mouseX;
            this.y2 = (float)mouseY;
         }
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      if (this.drag) {
         this.drag = false;
         Hanabi.INSTANCE.fileManager.saveWindows();
      }

      if (this.resizing) {
         this.resizing = false;
         Hanabi.INSTANCE.fileManager.saveWindows();
      }

   }

   public void mouseCoordinateUpdate(int mouseX, int mouseY) {
      if (this.drag) {
         this.x = (float)mouseX - this.x2;
         this.y = (float)mouseY - this.y2;
      }

      if (this.resizing) {
         this.width += (float)mouseX - this.x2;
         if (this.width < this.minWidth) {
            this.width = this.minWidth;
         } else {
            this.x2 = (float)mouseX;
         }

         this.height += (float)mouseY - this.y2;
         if (this.height < this.minHeight) {
            this.height = this.minHeight;
         } else {
            this.y2 = (float)mouseY;
         }

         if (!Display.isActive()) {
            this.resizing = false;
         }
      }

   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
   }

   public void makeCenter(float scrW, float scrH) {
      this.x = scrW / 2.0F - this.width / 2.0F;
      this.y = scrH / 2.0F - this.height / 2.0F;
   }

   public void hide() {
      this.hide = true;
   }

   public void show() {
      this.hide = false;
   }

   public boolean isOnFrame(int mouseX, int mouseY) {
      if (this.resizing) {
         return YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.draggableHeight + this.height);
      } else {
         return this.resizeable ? YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.draggableHeight) || YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x + this.width - 8.0F, this.y + this.draggableHeight + this.height - 8.0F, 8.0F, 8.0F) : YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.draggableHeight);
      }
   }

   public void setFocused(boolean focused) {
      this.focused = focused;
   }

   public boolean isFocused() {
      return this.focused;
   }
}
