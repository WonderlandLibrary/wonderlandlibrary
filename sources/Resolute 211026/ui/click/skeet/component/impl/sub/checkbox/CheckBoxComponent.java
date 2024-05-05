// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.checkbox;

import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.framework.Component;
import vip.Resolute.ui.click.skeet.framework.PredicateComponent;
import vip.Resolute.ui.click.skeet.framework.ButtonComponent;

public abstract class CheckBoxComponent extends ButtonComponent implements PredicateComponent
{
    public CheckBoxComponent(final Component parent, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
    }
    
    @Override
    public void drawComponent(final ScaledResolution resolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(855309));
        final boolean checked = this.isChecked();
        final boolean hovered = this.isHovered(mouseX, mouseY);
        RenderUtils.drawGradientRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, false, checked ? SkeetUI.getColor() : SkeetUI.getColor(hovered ? RenderUtils.darker(4802889, 1.4f) : 4802889), checked ? RenderUtils.darker(SkeetUI.getColor(), 0.8f) : SkeetUI.getColor(hovered ? RenderUtils.darker(3158064, 1.4f) : 3158064));
    }
    
    @Override
    public void onPress(final int mouseButton) {
        if (mouseButton == 0) {
            this.setChecked(!this.isChecked());
        }
    }
    
    public abstract boolean isChecked();
    
    public abstract void setChecked(final boolean p0);
}
