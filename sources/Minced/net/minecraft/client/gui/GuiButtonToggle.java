// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonToggle extends GuiButton
{
    protected ResourceLocation resourceLocation;
    protected boolean stateTriggered;
    protected int xTexStart;
    protected int yTexStart;
    protected int xDiffTex;
    protected int yDiffTex;
    
    public GuiButtonToggle(final int buttonId, final int xIn, final int yIn, final int widthIn, final int heightIn, final boolean buttonText) {
        super(buttonId, xIn, yIn, widthIn, heightIn, "");
        this.stateTriggered = buttonText;
    }
    
    public void initTextureValues(final int xTexStartIn, final int yTexStartIn, final int xDiffTexIn, final int yDiffTexIn, final ResourceLocation resourceLocationIn) {
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.xDiffTex = xDiffTexIn;
        this.yDiffTex = yDiffTexIn;
        this.resourceLocation = resourceLocationIn;
    }
    
    public void setStateTriggered(final boolean p_191753_1_) {
        this.stateTriggered = p_191753_1_;
    }
    
    public boolean isStateTriggered() {
        return this.stateTriggered;
    }
    
    public void setPosition(final int p_191752_1_, final int p_191752_2_) {
        this.x = p_191752_1_;
        this.y = p_191752_2_;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.visible) {
            this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
            mc.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.disableDepth();
            int i = this.xTexStart;
            int j = this.yTexStart;
            if (this.stateTriggered) {
                i += this.xDiffTex;
            }
            if (this.hovered) {
                j += this.yDiffTex;
            }
            this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);
            GlStateManager.enableDepth();
        }
    }
}
