// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonDownloadShaders extends GuiButton
{
    public GuiButtonDownloadShaders(final int buttonID, final int xPos, final int yPos) {
        super(buttonID, xPos, yPos, 22, 20, "");
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.visible) {
            super.drawButton(mc, mouseX, mouseY, partialTicks);
            final ResourceLocation resourcelocation = new ResourceLocation("optifine/textures/icons.png");
            mc.getTextureManager().bindTexture(resourcelocation);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.x + 3, this.y + 2, 0, 0, 16, 16);
        }
    }
}
