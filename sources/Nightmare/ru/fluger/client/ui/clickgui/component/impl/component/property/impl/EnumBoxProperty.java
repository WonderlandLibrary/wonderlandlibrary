// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl.component.property.impl;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;

public class EnumBoxProperty extends ExpandableComponent implements PropertyComponent
{
    private final ListSetting property;
    
    public EnumBoxProperty(final Component parent, final ListSetting property, final float x, final float y, final float width, final float height) {
        super(parent, property.getName(), x, y, width, height);
        this.property = property;
    }
    
    @Override
    public Setting getProperty() {
        return this.property;
    }
    
    @Override
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final float x = (float)this.getX();
        final float y = (float)this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final String selectedText = this.property.getCurrentMode();
        final Color onecolor = new Color(ClickGui.color.getColor());
        final float dropDownBoxY = y + 10.0f;
        final boolean needScissor = EnumBoxProperty.mc.fontRenderer.getStringWidth(selectedText) > width - 4.0f;
        final int textColor = 16777215;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + height, RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker().darker().darker().darker(), 120).getRGB());
        EnumBoxProperty.mc.smallfontRenderer.drawStringWithShadow(this.getName(), x + 2.0f, y + 3.0f, textColor);
        RectHelper.drawRect(x + 2.5, dropDownBoxY + 0.5, x + this.getWidth() - 2.5, dropDownBoxY + 10.0f, new Color(0, 0, 0, 0).getRGB());
        RectHelper.drawGradientRect(x + 2.5, dropDownBoxY + 0.5, x + this.getWidth() - 2.5, dropDownBoxY + 10.0f, RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker().darker().darker().darker().darker().darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker(), 120).getRGB());
        if (needScissor) {
            RenderHelper.scissorRect(x + 2.0f, dropDownBoxY + 2.0f, width - 5.0f, 10.0);
        }
        if (ClickGui.glow.getCurrentValue()) {
            EnumBoxProperty.mc.fontRenderer.drawBlurredString(selectedText, x + 3.5f, dropDownBoxY + 2.0f, 15, RenderHelper.injectAlpha(new Color(onecolor.getRGB()), 200), new Color(onecolor.brighter().getRGB()).getRGB());
        }
        else {
            EnumBoxProperty.mc.fontRenderer.drawStringWithShadow(selectedText, x + 3.5f, dropDownBoxY + 2.0f, new Color(onecolor.brighter().getRGB()).getRGB());
        }
        RenderHelper.drawArrow(x + 103.0f, dropDownBoxY + 2.0f, 1.3f, 1.5f, this.isExpanded(), new Color(229, 229, 223, 255).getRGB());
        if (needScissor) {
            GL11.glDisable(2929);
        }
        if (this.isExpanded()) {
            RectHelper.drawRect(x + 1.0f, y + height, x + width - 1.0f, y + this.getHeightWithExpand(), new Color(25, 25, 25).getRGB());
            RectHelper.drawGradientRect(x + 1.0f, y + height, x + width - 1.0f, y + this.getHeightWithExpand(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker().darker().darker().darker().darker().darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker(), 120).getRGB());
            this.handleRender(x, y + this.getHeight() + 2.0f, width, (float)textColor);
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded()) {
            this.handleClick(mouseX, mouseY, (int)this.getX(), (int)this.getY() + (int)this.getHeight() + 2, (int)this.getWidth());
        }
    }
    
    private void handleRender(final float x, float y, final float width, final float textColor) {
        final ListSetting setting = this.property;
        final Color onecolor = new Color(ClickGui.color.getColor());
        final Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        final int color = c.getRGB();
        for (final String e : setting.getModes()) {
            if (setting.currentMode.equals(e)) {
                RectHelper.drawColorRect(x + 1.0f, y - 2.0f, x + width - 1.0f, y + 15.0f - 5.0f, new Color(color), new Color(color).darker(), new Color(color).darker().darker(), new Color(color).darker().darker().darker());
            }
            EnumBoxProperty.mc.fontRenderer.drawStringWithShadow(e, x + 1.0f + 2.0f, y + 1.0f, (int)textColor);
            y += 12.0f;
        }
    }
    
    private void handleClick(final int mouseX, final int mouseY, final int x, int y, final int width) {
        for (final String e : this.property.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + 15.0f - 3.0f) {
                this.property.setCurrentMode(e);
            }
            y += (int)12.0f;
        }
    }
    
    @Override
    public int getHeightWithExpand() {
        return (int)(this.getHeight() + this.property.getModes().toArray().length * 12.0f);
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public boolean canExpand() {
        return this.property.getModes().toArray().length > 1;
    }
}
