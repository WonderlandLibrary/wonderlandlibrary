package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.alphacentauri.AC;

public class GuiButtonLanguage extends GuiButton {
   public GuiButtonLanguage(int buttonID, int xPos, int yPos) {
      super(buttonID, xPos, yPos, 20, 20, "");
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if(this.visible) {
         mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         int i = 106;
         if(flag) {
            i += this.height;
         }

         if(AC.isGhost()) {
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width, this.height);
         } else {
            this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -1207959552, -1207959552);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int j = 14737632;
            if(!this.enabled) {
               j = 10526880;
            } else if(this.hovered) {
               j = 16777120;
            }

            int var10003 = this.xPosition + this.width / 2;
            this.drawCenteredString(AC.getMC().fontRendererObj, "L", var10003, this.yPosition + this.height / 2 - 4, j);
         }
      }

   }
}
