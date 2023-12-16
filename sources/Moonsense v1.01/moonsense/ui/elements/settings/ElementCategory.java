// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings;

import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.ui.elements.Element;

public class ElementCategory extends Element
{
    private final String text;
    
    public ElementCategory(final int x, final int y, final int width, final int height, final String text, final boolean shouldScissor) {
        super(x, y, width, height, shouldScissor);
        this.text = text;
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)(this.getX() + 1), (float)(this.getY() + this.height / 2 - 1), (float)(this.getX() + 10), (float)(this.getY() + this.height / 2 + 1), 1.0f, new Color(150, 150, 150, 200).getRGB());
        GuiUtils.drawRoundedRect(this.getX() + 1 + MoonsenseClient.titleRenderer2.getWidth(this.text.toUpperCase()) + 13.0f, (float)(this.getY() + this.height / 2 - 1), this.getX() + 10 + MoonsenseClient.titleRenderer2.getWidth(this.text.toUpperCase()) + 13.0f, (float)(this.getY() + this.height / 2 + 1), 1.0f, new Color(150, 150, 150, 200).getRGB());
        MoonsenseClient.titleRenderer2.drawString(this.text.toUpperCase(), (float)(this.getX() + 1 + 12), this.getY() + (this.height - 10) / 2.0f, new Color(40, 40, 40, 200).getRGB());
        MoonsenseClient.titleRenderer2.drawString(this.text.toUpperCase(), (float)(this.getX() + 12), this.getY() + (this.height - 10) / 2.0f, new Color(150, 150, 150, 200).getRGB());
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
    }
}
