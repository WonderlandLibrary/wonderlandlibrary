// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.button;

import moonsense.features.ThemeSettings;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import moonsense.MoonsenseClient;
import moonsense.utils.CustomFontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiCustomButton extends GuiButton
{
    private CustomFontRenderer fr;
    private boolean dontUseClientColor;
    public boolean focused;
    
    public GuiCustomButton(final int buttonId, final int x, final int y, final String buttonText, final boolean dontUseClientColor) {
        this(buttonId, x, y, 200, 20, buttonText, dontUseClientColor);
    }
    
    public GuiCustomButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText, final boolean dontUseClientColor) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.focused = false;
        this.fr = MoonsenseClient.textRenderer;
        this.dontUseClientColor = dontUseClientColor;
    }
    
    public void setFontRenderer(final CustomFontRenderer fr) {
        this.fr = fr;
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
            if (ThemeSettings.INSTANCE.buttonFont.getBoolean()) {
                if (this.fr == null) {
                    this.fr = MoonsenseClient.textRenderer;
                }
                if (this.dontUseClientColor) {
                    this.fr.drawCenteredString(this.displayString.toUpperCase(), (float)(this.xPosition + this.width / 2), (float)(this.yPosition + (this.height - 10) / 2), this.enabled ? 14737632 : 10526880);
                }
                else {
                    this.fr.drawCenteredString(this.displayString.toUpperCase(), (float)(this.xPosition + this.width / 2), (float)(this.yPosition + (this.height - 10) / 2), this.enabled ? (this.hovered ? 16777120 : 14737632) : 10526880);
                }
            }
            else if (this.dontUseClientColor) {
                GuiUtils.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.enabled ? 14737632 : 10526880);
            }
            else {
                GuiUtils.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.enabled ? (this.hovered ? 16777120 : 14737632) : 10526880);
            }
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
