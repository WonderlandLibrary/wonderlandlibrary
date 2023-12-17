// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImage extends GuiButton
{
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffText;
    
    public GuiButtonImage(final int buttonId, final int xIn, final int yIn, final int widthIn, final int heightIn, final int textureOffestX, final int textureOffestY, final int p_i47392_8_, final ResourceLocation resource) {
        super(buttonId, xIn, yIn, widthIn, heightIn, "");
        this.xTexStart = textureOffestX;
        this.yTexStart = textureOffestY;
        this.yDiffText = p_i47392_8_;
        this.resourceLocation = resource;
    }
    
    public void setPosition(final int p_191746_1_, final int p_191746_2_) {
        this.x = p_191746_1_;
        this.y = p_191746_2_;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.visible) {
            this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
            mc.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.disableDepth();
            final int i = this.xTexStart;
            int j = this.yTexStart;
            if (this.hovered) {
                j += this.yDiffText;
            }
            this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);
            GlStateManager.enableDepth();
        }
    }
}
