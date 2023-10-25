/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui.component.impl.component.property.impl;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import org.celestial.client.feature.impl.hud.ClickGui;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.Setting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.ui.clickgui.component.Component;
import org.celestial.client.ui.clickgui.component.impl.ExpandableComponent;
import org.celestial.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import org.lwjgl.opengl.GL11;

public class EnumBoxProperty
extends ExpandableComponent
implements PropertyComponent {
    private final ListSetting property;

    public EnumBoxProperty(Component parent, ListSetting property, float x, float y, float width, float height) {
        super(parent, property.getName(), x, y, width, height);
        this.property = property;
    }

    @Override
    public Setting getProperty() {
        return this.property;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        String selectedText = this.property.getCurrentMode();
        float dropDownBoxY = y + 10.0f;
        boolean needScissor = (float)EnumBoxProperty.mc.fontRenderer.getStringWidth(selectedText) > width - 4.0f;
        int textColor = 0xFFFFFF;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(15, 15, 15).getRGB());
        EnumBoxProperty.mc.smallfontRenderer.drawStringWithShadow(this.getName(), x + 2.0f, y + 3.0f, textColor);
        RectHelper.drawRect(x + 2.0f, dropDownBoxY, x + this.getWidth() - 2.0f, dropDownBoxY + 10.0f, new Color(30, 30, 30).getRGB());
        RectHelper.drawRect((double)x + 2.5, (double)dropDownBoxY + 0.5, (double)(x + this.getWidth()) - 2.5, dropDownBoxY + 10.0f, -12828863);
        if (needScissor) {
            RenderHelper.scissorRect(x + 2.0f, dropDownBoxY + 2.0f, width - 5.0f, 10.0);
        }
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(new Color(229, 229, 223, 255).brighter(), (double)(x + 3.5f), (double)(dropDownBoxY + 2.0f), (double)(EnumBoxProperty.mc.fontRenderer.getStringWidth(selectedText) + 2), 5.0, 25);
        }
        EnumBoxProperty.mc.fontRenderer.drawStringWithShadow(selectedText, x + 3.5f, dropDownBoxY + 2.0f, -1);
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(new Color(229, 229, 223, 255).brighter(), (double)(x + 105.0f), (double)(dropDownBoxY + 2.0f), 5.0, 5.0, 10);
        }
        RenderHelper.drawArrow(x + 103.0f, dropDownBoxY + 2.0f, 1.3f, 1.5f, this.isExpanded(), new Color(229, 229, 223, 255).getRGB());
        if (needScissor) {
            GL11.glDisable(2929);
        }
        if (this.isExpanded()) {
            RectHelper.drawRect(x + 1.0f, y + height, x + width - 1.0f, y + (float)this.getHeightWithExpand(), new Color(30, 30, 30).getRGB());
            this.handleRender(x, y + this.getHeight() + 2.0f, width, textColor);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded()) {
            this.handleClick(mouseX, mouseY, (int)this.getX(), (int)this.getY() + (int)this.getHeight() + 2, (int)this.getWidth());
        }
    }

    private void handleRender(float x, float y, float width, float textColor) {
        ListSetting setting = this.property;
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        for (String e : setting.getModes()) {
            if (setting.currentMode.equals(e)) {
                if (ClickGui.glow.getCurrentValue()) {
                    RenderHelper.renderBlurredShadow(Color.WHITE, (double)((int)x + 1), (double)((int)y + 2), 110.0, 7.0, 25);
                }
                RectHelper.drawColorRect(x + 1.0f, y - 2.0f, x + width - 1.0f, y + 15.0f - 5.0f, new Color(color), new Color(color).brighter(), new Color(color).brighter().brighter(), new Color(color).brighter().brighter().brighter());
            }
            EnumBoxProperty.mc.fontRenderer.drawStringWithShadow(e, x + 1.0f + 2.0f, y + 1.0f, (int)textColor);
            y += 12.0f;
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : this.property.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && (float)mouseY <= (float)y + 15.0f - 3.0f) {
                this.property.setCurrentMode(e);
            }
            y = (int)((float)y + 12.0f);
        }
    }

    @Override
    public int getHeightWithExpand() {
        return (int)(this.getHeight() + (float)this.property.getModes().toArray().length * 12.0f);
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    }

    @Override
    public boolean canExpand() {
        return this.property.getModes().toArray().length > 1;
    }
}

