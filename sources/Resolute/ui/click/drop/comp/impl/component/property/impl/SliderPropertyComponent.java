// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop.comp.impl.component.property.impl;

import vip.Resolute.settings.Setting;
import java.math.BigDecimal;
import vip.Resolute.util.render.RenderUtils;
import java.awt.Color;
import vip.Resolute.modules.impl.render.ClickGUI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.ui.click.drop.comp.impl.component.property.PropertyComponent;
import vip.Resolute.ui.click.drop.comp.Component;

public final class SliderPropertyComponent extends Component implements PropertyComponent
{
    private final NumberSetting doubleProperty;
    private boolean sliding;
    
    public SliderPropertyComponent(final Component parent, final NumberSetting property, final int x, final int y, final int width, final int height) {
        super(parent, property.name, x, y, width, height);
        this.doubleProperty = property;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final double min = this.doubleProperty.getMinimum();
        final double max = this.doubleProperty.getMaximum();
        final double value = this.doubleProperty.getValue();
        final double sliderPercentage = (value - min) / (this.doubleProperty.getMaximum() - min);
        final boolean hovered = this.isHovered(mouseX, mouseY);
        if (this.sliding) {
            if (hovered) {
                this.doubleProperty.setValue(MathHelper.clamp_double(this.roundToFirstDecimalPlace((mouseX - x) * (max - min) / width + min), min, max));
            }
            else {
                this.sliding = false;
            }
        }
        final String name = this.getName();
        final int middleHeight = this.getHeight() / 2;
        final String valueString = Double.toString(value);
        final float valueWidth = (float)(Minecraft.getMinecraft().fontRendererObj.getStringWidth(valueString) + 2);
        final float overflowWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) + 3 - (width - valueWidth);
        final boolean needOverflowBox = overflowWidth > 0.0f;
        final boolean showOverflowBox = hovered && needOverflowBox;
        final boolean needScissorBox = needOverflowBox && !hovered;
        Gui.drawRect(x - (showOverflowBox ? overflowWidth : 0.0f), (float)y, (float)(x + width + 1), (float)(y + height), this.getSecondaryBackgroundColor(hovered));
        Gui.drawRect((float)(x - 2), (float)y, (float)x, (float)(y + height), ClickGUI.color.getColor());
        Gui.drawRect(x, y + 13, x + width * sliderPercentage, y + height, new Color(255, 255, 255).getRGB());
        Gui.drawRect(x, y + 13, x + width * sliderPercentage - 2.0, y + height, ClickGUI.color.getColor());
        if (needScissorBox) {
            RenderUtils.startScissorBox(scaledResolution, x, y, (int)(width - valueWidth - 4.0f), height);
        }
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, x + 2 - (showOverflowBox ? overflowWidth : 0.0f), (float)(y + middleHeight - 3), new Color(255, 255, 255).getRGB());
        if (needScissorBox) {
            RenderUtils.endScissorBox();
        }
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(valueString, x + width - valueWidth, (float)(y + middleHeight - 3), new Color(255, 255, 255).getRGB());
    }
    
    private double roundToFirstDecimalPlace(final double value) {
        final double inc = this.doubleProperty.getIncrement();
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfOfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc).setScale(2, 4).doubleValue();
        }
        return new BigDecimal(floored).setScale(2, 4).doubleValue();
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (!this.sliding && button == 0 && this.isHovered(mouseX, mouseY)) {
            this.sliding = true;
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        this.sliding = false;
    }
    
    @Override
    public NumberSetting getProperty() {
        return this.doubleProperty;
    }
}
