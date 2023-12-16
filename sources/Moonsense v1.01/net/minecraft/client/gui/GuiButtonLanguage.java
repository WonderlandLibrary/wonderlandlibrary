// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonLanguage extends GuiButton
{
    private static final String __OBFID = "CL_00000672";
    private final ResourceLocation language;
    private boolean dontUseClientColor;
    
    public GuiButtonLanguage(final int p_i1041_1_, final int p_i1041_2_, final int p_i1041_3_, final boolean dontUseClientColor) {
        super(p_i1041_1_, p_i1041_2_, p_i1041_3_, 20, 20, "");
        this.language = new ResourceLocation("streamlined/icons/language.png");
        this.dontUseClientColor = dontUseClientColor;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (this.dontUseClientColor) {
                GuiUtils.drawRoundedOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 3.0f, 2.0f, this.enabled ? new Color(255, 255, 255, 100).getRGB() : new Color(85, 96, 102, 100).getRGB());
                GuiUtils.drawRoundedRect((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height), 3.0f, this.enabled ? (this.hovered ? new Color(100, 111, 117, 100).getRGB() : new Color(42, 50, 55, 100).getRGB()) : new Color(70, 70, 70, 50).getRGB());
            }
            else {
                GuiUtils.drawRoundedOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 3.0f, 2.0f, this.enabled ? (this.hovered ? MoonsenseClient.getMainColor(255) : MoonsenseClient.getMainColor(150)) : MoonsenseClient.getMainColor(100));
                GuiUtils.drawRoundedRect((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height), 3.0f, this.enabled ? (this.hovered ? new Color(0, 0, 0, 100).getRGB() : new Color(30, 30, 30, 100).getRGB()) : new Color(70, 70, 70, 50).getRGB());
            }
            mc.getTextureManager().bindTexture(this.language);
            if (this.dontUseClientColor) {
                GuiUtils.setGlColor(Color.white.darker().getRGB());
            }
            else {
                GuiUtils.setGlColor(MoonsenseClient.getMainColor(255));
            }
            GlStateManager.enableBlend();
            final int b = 12;
            GuiUtils.drawModalRectWithCustomSizedTexture(this.xPosition + b / 3.0f, this.yPosition + b / 3.0f - 0.5f, 0.0f, 0.0f, b, b, (float)b, (float)b);
        }
    }
}
