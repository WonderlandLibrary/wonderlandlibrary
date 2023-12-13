package org.alphacentauri.gui.screens;

import java.awt.Rectangle;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;

public class GuiClick extends GuiScreen {
   private final GuiManager guiManager;

   public GuiClick(GuiManager guiManager) {
      this.guiManager = guiManager;
   }

   protected void mouseClicked(int x, int y, int button) throws IOException {
      super.mouseClicked(x, y, button);

      for(Frame frame : this.guiManager.getFrames()) {
         if(frame.isVisible() && !frame.isMinimized() && !frame.getArea().contains(x, y)) {
            for(Component component : frame.getChildren()) {
               for(Rectangle area : component.getTheme().getUIForComponent(component).getInteractableRegions(component)) {
                  if(area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
                     frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
                     this.guiManager.bringForward(frame);
                     return;
                  }
               }
            }
         }
      }

      for(Frame frame : this.guiManager.getFrames()) {
         if(frame.isVisible()) {
            if(!frame.isMinimized() && frame.getArea().contains(x, y)) {
               frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
               this.guiManager.bringForward(frame);
               break;
            }

            if(frame.isMinimized()) {
               for(Rectangle area : frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame)) {
                  if(area.contains(x - frame.getX(), y - frame.getY())) {
                     frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
                     this.guiManager.bringForward(frame);
                     return;
                  }
               }
            }
         }
      }

   }

   public void mouseReleased(int x, int y, int button) {
      super.mouseReleased(x, y, button);

      for(Frame frame : this.guiManager.getFrames()) {
         if(frame.isVisible() && !frame.isMinimized() && !frame.getArea().contains(x, y)) {
            for(Component component : frame.getChildren()) {
               for(Rectangle area : component.getTheme().getUIForComponent(component).getInteractableRegions(component)) {
                  if(area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
                     frame.setDragging(false);
                     return;
                  }
               }
            }
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.guiManager.render();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void onGuiClosed() {
      if(this.mc.entityRenderer.theShaderGroup != null) {
         this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
         this.mc.entityRenderer.theShaderGroup = null;
      }

   }

   public void initGui() {
      if(OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         if(this.mc.entityRenderer.theShaderGroup != null) {
            this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
         }

         this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
      }

   }

   public void mouseClickMove(int x, int y, int button, long timeSinceLastClick) {
      super.mouseClickMove(x, y, button, timeSinceLastClick);

      for(Frame frame : this.guiManager.getFrames()) {
         if(frame.isVisible() && !frame.isMinimized() && !frame.getArea().contains(x, y)) {
            for(Component component : frame.getChildren()) {
               for(Rectangle area : component.getTheme().getUIForComponent(component).getInteractableRegions(component)) {
                  if(area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
                     frame.setDragging(true);
                     this.guiManager.bringForward(frame);
                     return;
                  }
               }
            }
         }
      }

   }
}
