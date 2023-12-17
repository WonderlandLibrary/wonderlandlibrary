// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiButtonLanguage extends GuiButton
{
    public GuiButtonLanguage(final int buttonID, final int xPos, final int yPos) {
        super(buttonID, xPos, yPos, 20, 20, "");
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = 106;
            if (flag) {
                i += this.height;
            }
            this.drawTexturedModalRect(this.x, this.y, 0, i, this.width, this.height);
        }
    }
}
