// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop.comp.impl.component.property.impl;

import vip.Resolute.settings.Setting;
import java.util.Iterator;
import vip.Resolute.util.misc.StringUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import vip.Resolute.ui.click.drop.ClickGui;
import vip.Resolute.modules.impl.render.ClickGUI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.drop.comp.Component;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.ui.click.drop.comp.impl.component.property.PropertyComponent;
import vip.Resolute.ui.click.drop.comp.impl.ExpandableComponent;

public final class EnumBoxProperty extends ExpandableComponent implements PropertyComponent
{
    private final ModeSetting property;
    
    public EnumBoxProperty(final Component parent, final ModeSetting property, final int x, final int y, final int width, final int height) {
        super(parent, property.name, x, y, width, height);
        this.property = property;
    }
    
    @Override
    public ModeSetting getProperty() {
        return this.property;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int textColor = 16777215;
        final int bgColor = this.getSecondaryBackgroundColor(this.isHovered(mouseX, mouseY));
        Gui.drawRect((float)x, (float)y, (float)(x + width + 1), (float)(y + height), bgColor);
        Gui.drawRect((float)(x - 2), (float)y, (float)x, (float)(y + height), ClickGUI.color.getColor());
        if (this.isExpanded()) {
            Gui.drawRect((float)(x - 2), (float)(y + height), (float)(x + 1), (float)(y + this.getHeightWithExpand()), ClickGUI.color.getColor());
            Gui.drawRect((float)(x + 1), (float)(y + height), (float)(x + width - 1 + 2), (float)(y + this.getHeightWithExpand()), ClickGui.getInstance().getPalette().getSecondaryBackgroundColor().getRGB());
            this.handleRender(x, y + this.getHeight() + 2, width);
        }
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.getName(), (float)(x + 2), y + this.getHeight() / 2.0f - 3.0f, 16777215);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.isExpanded() ? "<" : ">", (float)(x + width - 6), y + height / 2.0f - 4.0f, new Color(255, 255, 255).getRGB());
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded()) {
            this.handleClick(mouseX, mouseY, this.getX(), this.getY() + this.getHeight() + 2, this.getWidth());
        }
    }
    
    private void handleRender(final int x, int y, final int width) {
        final ModeSetting property = this.property;
        for (final String e : property.getModes()) {
            if (property.is(e)) {
                Gui.drawRect((float)(x + 1), (float)(y - 2), (float)(x + width - 1 + 2), (float)(y + 15 - 3 - 2), new Color(0, 0, 0, 95).getRGB());
            }
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(StringUtils.upperSnakeCaseToPascal(e.toString()), (float)(x + 1 + 2), (float)y, property.is(e) ? ClickGUI.color.getColor() : new Color(255, 255, 255).getRGB());
            y += 12;
        }
    }
    
    private void handleClick(final int mouseX, final int mouseY, final int x, int y, final int width) {
        final ModeSetting property = this.property;
        for (final String e : property.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + 15 - 3) {
                property.setSelected(e);
            }
            y += 12;
        }
    }
    
    @Override
    public int getHeightWithExpand() {
        return this.getHeight() + this.property.getModes().size() * 12;
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public boolean canExpand() {
        return this.property.getModes().size() > 1;
    }
}
