// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiButtonLanguage extends GuiButton
{
    private static final String __OBFID = "CL_00000672";
    
    public GuiButtonLanguage(final int p_i1041_1_, final int p_i1041_2_, final int p_i1041_3_) {
        super(p_i1041_1_, p_i1041_2_, p_i1041_3_, 20, 20, "");
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean var4 = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int var5 = 106;
            if (var4) {
                var5 += this.height;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, var5, this.width, this.height);
        }
    }
}
