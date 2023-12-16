// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements;

import moonsense.features.ThemeSettings;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.Consumer;
import java.util.Map;

public class ElementReplay extends Element
{
    private int bgFade;
    private Map.Entry<String, String> entry;
    
    public ElementReplay(final int x, final int y, final int width, final int height, final Map.Entry<String, String> entry, final Consumer<Element> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, true, consumer, parent);
        this.entry = entry;
        this.bgFade = 50;
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 2.0f, MoonsenseClient.getMainColor(this.bgFade));
        GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 2.0f, 2.0f, MoonsenseClient.getMainColor(255));
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        super.renderElement(partialTicks);
        MoonsenseClient.textRenderer.drawString(this.entry.getKey(), this.getX() + 2, this.getY() + 2, ThemeSettings.INSTANCE.generalTextColor.getColor());
    }
    
    @Override
    public void update() {
        if (this.hovered && this.bgFade + 10 < 150) {
            this.bgFade += 10;
        }
        else if (!this.hovered && this.bgFade - 10 > 50) {
            this.bgFade -= 10;
        }
    }
}
