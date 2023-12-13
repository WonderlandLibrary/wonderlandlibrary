package me.yarukon.palette;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.RenderUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import me.yarukon.YRenderUtil;
import me.yarukon.palette.impl.ColorPicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class PaletteGui extends GuiScreen {
   public float x;
   public float y;
   public Minecraft mc = Minecraft.getMinecraft();
   public float x2;
   public float y2;
   public boolean drag = false;
   public float draggableHeight = 15.0F;
   public ArrayList colorPickers = new ArrayList();
   public int mouseXX;
   public int mouseYY;
   public float scrollY = 0.0F;
   public float scrollAni = 0.0F;
   public float minY = -100.0F;

   public void initGui() {
      this.colorPickers.clear();
      Iterator var1 = ColorValue.colorValues.iterator();

      while(var1.hasNext()) {
         ColorValue v = (ColorValue)var1.next();
         this.colorPickers.add(new ColorPicker(this, v));
      }

      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.mouseXX = mouseX;
      this.mouseYY = mouseY;
      YRenderUtil.drawRect(this.x, this.y, 200.0F, this.draggableHeight, -16732281);
      YRenderUtil.drawRect(this.x, this.y + this.draggableHeight, 200.0F, 300.0F, -13223617);
      Hanabi.INSTANCE.fontManager.usans18.drawString("Palette", this.x + 5.0F, this.y + this.draggableHeight / 2.0F - (float)Hanabi.INSTANCE.fontManager.usans18.FONT_HEIGHT / 2.0F, -1);
      if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x, this.y + this.draggableHeight, 200.0F, 300.0F)) {
         this.minY = 301.0F;
      }

      this.scrollAni = RenderUtil.smoothAnimation(this.scrollAni, this.scrollY, 50.0F, 0.3F);
      float startY = this.y + this.draggableHeight + 2.0F + this.scrollAni;
      float totalY = 0.0F;
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)this.x, (int)(this.y + this.draggableHeight), 200, 300);

      for(Iterator var6 = this.colorPickers.iterator(); var6.hasNext(); totalY += 145.0F) {
         ColorPicker c = (ColorPicker)var6.next();
         c.render(this.x + 5.0F, startY, 188.0F, 140.0F, mouseX, mouseY);
         startY += 145.0F;
      }

      GL11.glDisable(3089);
      if (YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x, this.y + this.draggableHeight, 200.0F, 300.0F)) {
         this.minY -= totalY;
      }

      if (totalY > 300.0F) {
         float viewable = 296.0F;
         float progress = MathHelper.clamp_float(-this.scrollAni / -this.minY, 0.0F, 1.0F);
         float ratio = viewable / totalY * viewable;
         float barHeight = Math.max(ratio, 20.0F);
         float position = progress * (viewable - barHeight);
         YRenderUtil.drawRect(this.x + 196.5F, this.y + this.draggableHeight + 2.0F, 2.0F, 296.0F, -13749448);
         YRenderUtil.drawRect(this.x + 196.5F, this.y + this.draggableHeight + 2.0F + position, 2.0F, barHeight, -1);
      }

      this.coordinateUpdate(mouseX, mouseY);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void handleMouseInput() throws IOException {
      if (YRenderUtil.isHoveringBound((float)this.mouseXX, (float)this.mouseYY, this.x, this.y + this.draggableHeight, 200.0F, 300.0F)) {
         this.scrollY += (float)Mouse.getEventDWheel() / 5.0F;
         if (this.scrollY <= this.minY) {
            this.scrollY = this.minY;
         }

         if (this.scrollY >= 0.0F) {
            this.scrollY = 0.0F;
         }
      }

      super.handleMouseInput();
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (mouseButton == 0 && YRenderUtil.isHoveringBound((float)mouseX, (float)mouseY, this.x, this.y, 200.0F, this.draggableHeight)) {
         this.drag = true;
         this.x2 = (float)mouseX - this.x;
         this.y2 = (float)mouseY - this.y;
      }

      Iterator var4 = this.colorPickers.iterator();

      while(var4.hasNext()) {
         ColorPicker c = (ColorPicker)var4.next();
         c.mouseClicked(mouseX, mouseY, mouseButton);
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      if (this.drag) {
         this.drag = false;
      }

      Iterator var4 = this.colorPickers.iterator();

      while(var4.hasNext()) {
         ColorPicker c = (ColorPicker)var4.next();
         c.mouseReleased(mouseX, mouseY, state);
      }

      super.mouseReleased(mouseX, mouseY, state);
   }

   public void coordinateUpdate(int mouseX, int mouseY) {
      if (this.drag) {
         this.x = (float)mouseX - this.x2;
         this.y = (float)mouseY - this.y2;
      }

   }

   public void onGuiClosed() {
      Hanabi.INSTANCE.fileManager.saveColors();
      super.onGuiClosed();
   }

   public void updateScreen() {
      super.updateScreen();
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
