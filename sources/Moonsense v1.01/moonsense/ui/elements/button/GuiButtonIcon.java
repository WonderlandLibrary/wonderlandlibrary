// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.button;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.screen.settings.AbstractSettingsGui;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonIcon extends GuiButton
{
    private ResourceLocation ICON;
    private boolean dontUseClientColor;
    private String tooltip;
    private boolean hasTooltip;
    
    public GuiButtonIcon(final int buttonId, final int x, final int y, final int width, final int height, final String iconName, final String tooltip, final boolean dontUseClientColor) {
        super(buttonId, x, y, width, height, "");
        this.ICON = new ResourceLocation("streamlined/icons/" + iconName);
        this.dontUseClientColor = dontUseClientColor;
        this.tooltip = tooltip;
        this.hasTooltip = (this.tooltip != null && !this.tooltip.isEmpty());
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (this.dontUseClientColor) {
                GuiUtils.drawRoundedOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 3.0f, 1.5f, this.enabled ? new Color(255, 255, 255, 100).getRGB() : new Color(85, 96, 102, 100).getRGB());
                GuiUtils.drawRoundedRect((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height), 3.0f, this.enabled ? (this.hovered ? new Color(100, 111, 117, 100).getRGB() : new Color(42, 50, 55, 100).getRGB()) : new Color(70, 70, 70, 50).getRGB());
            }
            else {
                GuiUtils.drawRoundedOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 3.0f, 1.5f, this.enabled ? (this.hovered ? MoonsenseClient.getMainColor(255) : MoonsenseClient.getMainColor(150)) : MoonsenseClient.getMainColor(100));
                GuiUtils.drawRoundedRect((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height), 3.0f, this.enabled ? (this.hovered ? new Color(0, 0, 0, 100).getRGB() : new Color(30, 30, 30, 100).getRGB()) : new Color(70, 70, 70, 50).getRGB());
            }
            mc.getTextureManager().bindTexture(this.ICON);
            final int b = (Minecraft.getMinecraft().currentScreen instanceof AbstractSettingsGui) ? 12 : 10;
            GlStateManager.enableBlend();
            if (this.dontUseClientColor) {
                GuiUtils.setGlColor(this.enabled ? (this.hovered ? new Color(255, 255, 255, 255).getRGB() : Color.white.darker().getRGB()) : new Color(120, 120, 120, 255).getRGB());
            }
            else {
                GuiUtils.setGlColor(this.enabled ? (this.hovered ? new Color(255, 255, 255, 255).getRGB() : MoonsenseClient.getMainColor(255)) : new Color(120, 120, 120, 255).getRGB());
            }
            Gui.drawScaledCustomSizeModalRect(this.xPosition + (this.width - b) / 2, this.yPosition + (this.height - b) / 2, 0.0f, 0.0f, b, b, b, b, (float)b, (float)b);
        }
    }
    
    public void setImage(final String iconName) {
        this.ICON = new ResourceLocation("streamlined/icons/" + iconName);
    }
}
