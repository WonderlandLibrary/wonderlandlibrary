package de.violence.gui;

import de.violence.font.FontManager;
import de.violence.gui.mains.Frame;
import de.violence.mcgui.utils.GuiMains;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.save.manager.FileManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen {
   static List frameList = new ArrayList();
   static Frame dragging;

   public static List getFrameList() {
      return frameList;
   }

   public void initGui() {
      if(frameList.isEmpty()) {
         this.initFrames();
      }

      super.initGui();
   }

   public void onGuiClosed() {
      Frame frames;
      for(Iterator var2 = frameList.iterator(); var2.hasNext(); frames.dragging = false) {
         frames = (Frame)var2.next();
      }

      super.onGuiClosed();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      Iterator var5 = frameList.iterator();

      while(var5.hasNext()) {
         Frame frames = (Frame)var5.next();
         frames.onRender(mouseX, mouseY);
         if(dragging != null && dragging == frames && frames.dragging) {
            frames.x = mouseX - frames.dragX;
            frames.y = mouseY - frames.dragY;
         }
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      for(Iterator var5 = frameList.iterator(); var5.hasNext(); dragging = null) {
         Frame frames = (Frame)var5.next();
         FileManager.gui.setInteger("gui." + frames.getCategory().name() + ".x", Integer.valueOf(frames.x));
         FileManager.gui.setInteger("gui." + frames.getCategory().name() + ".y", Integer.valueOf(frames.y));
         FileManager.gui.save();
         frames.dragging = false;
      }

      super.mouseReleased(mouseX, mouseY, state);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      Iterator var5 = frameList.iterator();

      while(var5.hasNext()) {
         Frame frames = (Frame)var5.next();
         frames.onClick(mouseX, mouseY, mouseButton);
         if(GuiMains.isHovered(mouseX, mouseY, frames.x, frames.y, frames.getWidth(), frames.getHeigth()) && dragging == null) {
            frames.dragX = mouseX - frames.x;
            frames.dragY = mouseY - frames.y;
            frames.dragging = true;
            dragging = frames;
         }
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   private void initFrames() {
      int x = 20;
      byte y = 10;
      Category[] var6;
      int var5 = (var6 = Category.values()).length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Category category = var6[var4];
         if(FileManager.gui.getInteger("gui." + category.name() + ".x") != null) {
            int a = FileManager.gui.getInteger("gui." + category.name() + ".x").intValue();
            int b = FileManager.gui.getInteger("gui." + category.name() + ".y").intValue();
            frameList.add(new Frame(category, a, b, this.getWidth(category)));
         } else {
            frameList.add(new Frame(category, x, y, this.getWidth(category)));
         }

         x += this.getWidth(category) + 10;
      }

   }

   private int getWidth(Category category) {
      int w = 50;
      Iterator var4 = Module.getModuleList().iterator();

      while(var4.hasNext()) {
         Module modules = (Module)var4.next();
         if(modules.getCategory() == category) {
            w = Math.max(w, FontManager.clickGUI.getStringWidth(modules.getName()) + 5);
         }
      }

      w = Math.max(w, FontManager.clickGUI.getStringWidth(category.name())) + 3;
      return w;
   }
}
