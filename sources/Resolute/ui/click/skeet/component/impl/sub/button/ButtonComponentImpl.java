// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.button;

import vip.Resolute.util.font.FontUtil;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.framework.Component;
import java.util.function.Consumer;
import vip.Resolute.ui.click.skeet.framework.ButtonComponent;

public final class ButtonComponentImpl extends ButtonComponent
{
    private final String text;
    private final Consumer<Integer> onPress;
    
    public ButtonComponentImpl(final Component parent, final String text, final Consumer<Integer> onPress, final float width, final float height) {
        super(parent, 0.0f, 0.0f, width, height);
        this.text = text;
        this.onPress = onPress;
    }
    
    @Override
    public void drawComponent(final ScaledResolution lockedResolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final boolean hovered = this.isHovered(mouseX, mouseY);
        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(1118481));
        Gui.drawRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, SkeetUI.getColor(2500134));
        RenderUtils.drawGradientRect(x + 1.0f, y + 1.0f, x + width - 1.0f, y + height - 1.0f, false, SkeetUI.getColor(hovered ? RenderUtils.darker(2236962, 1.2f) : 2236962), SkeetUI.getColor(hovered ? RenderUtils.darker(1973790, 1.2f) : 1973790));
        if (SkeetUI.shouldRenderText()) {
            FontUtil.tahomaSmall.drawStringWithShadow(this.text, x + width / 2.0f - FontUtil.tahomaSmall.getStringWidth(this.text) / 2.0, y + height / 2.0f - 1.0f, -1);
        }
    }
    
    @Override
    public void onPress(final int mouseButton) {
        this.onPress.accept(mouseButton);
    }
}
