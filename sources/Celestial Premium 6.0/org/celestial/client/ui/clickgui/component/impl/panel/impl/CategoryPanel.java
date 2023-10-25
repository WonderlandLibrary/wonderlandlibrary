/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui.component.impl.panel.impl;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import org.celestial.client.Celestial;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.hud.ClickGui;
import org.celestial.client.helpers.Helper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.clickgui.ClickGuiScreen;
import org.celestial.client.ui.clickgui.Palette;
import org.celestial.client.ui.clickgui.SorterHelper;
import org.celestial.client.ui.clickgui.component.Component;
import org.celestial.client.ui.clickgui.component.impl.ExpandableComponent;
import org.celestial.client.ui.clickgui.component.impl.component.module.ModuleComponent;
import org.celestial.client.ui.clickgui.component.impl.panel.DraggablePanel;

public final class CategoryPanel
extends DraggablePanel
implements Helper {
    public static final float HEADER_WIDTH = 120.0f;
    public static final float X_ITEM_OFFSET = 1.0f;
    public static final float ITEM_HEIGHT = 15.0f;
    public static final float HEADER_HEIGHT = 17.0f;
    private final List<Feature> modules;
    public Type category;

    public CategoryPanel(Type category, float x, float y) {
        super(null, category.name(), x, y, 120.0f, 17.0f);
        float moduleY = 17.0f;
        this.modules = Collections.unmodifiableList(Celestial.instance.featureManager.getFeaturesForCategory(category));
        for (Feature module : this.modules) {
            this.components.add(new ModuleComponent((Component)this, module, 1.0f, moduleY, 118.0f, 15.0f));
            moduleY += 15.0f;
        }
        this.category = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float height;
        super.drawComponent(scaledResolution, mouseX, mouseY);
        this.components.sort(new SorterHelper());
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = this.getWidth();
        float headerHeight = height = this.getHeight();
        int heightWithExpand = this.getHeightWithExpand();
        if ((ClickGui.backGroundBlur.getCurrentValue() || ClickGui.panelMode.currentMode.equals("Blur")) && CategoryPanel.mc.gameSettings.ofFastRender) {
            CategoryPanel.mc.gameSettings.ofFastRender = false;
        }
        if (ClickGuiScreen.getInstance().getPalette() == Palette.DEFAULT) {
            headerHeight = this.isExpanded() ? (float)heightWithExpand : height;
        }
        float startAlpha = 1.0f;
        int size = 8;
        float left = x;
        float right = x + 120.0f;
        float bottom = y + 11.0f;
        float startAlpha1 = 0.14f;
        int size1 = 25;
        float left1 = x + 1.0f;
        float right1 = x + width;
        float bottom1 = y + headerHeight - 2.0f;
        float top1 = y + headerHeight - 2.0f;
        float top2 = y + 13.0f;
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        if (this.isExpanded()) {
            RectHelper.renderShadowVertical(Color.BLACK, 3.0f, startAlpha1, size1, right1 - 1.0f, top2, bottom1 + 3.0f, true, false);
            RectHelper.renderShadowVertical(Color.BLACK, 3.0f, startAlpha1, size1, (double)left1 - 0.3, top2, bottom1 + 3.0f, false, false);
            RectHelper.renderShadowHorizontal(Color.BLACK, 3.0f, (double)startAlpha - 0.15, size, bottom, left, right, false, false);
        }
        if (ClickGui.panelMode.currentMode.equals("Blur") && ClickGui.scale.getCurrentValue() == 1.0f) {
            RenderHelper.renderBlur((int)x, (int)y, (int)width, (int)headerHeight - 2, 15);
        } else if (ClickGui.panelMode.currentMode.equals("Rect")) {
            RectHelper.drawRect(x, y, x + width, y + headerHeight - 2.0f, new Color(40, 40, 40, 125).getRGB());
        }
        if (this.isExpanded()) {
            float moduleY = height;
            for (Component child : this.components) {
                ExpandableComponent expandableComponent;
                child.setY(moduleY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                float cHeight = child.getHeight();
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                moduleY += cHeight;
            }
        }
        RectHelper.drawColorRect(x - 3.0f, y, x + 123.0f, y + 15.0f, new Color(15, 15, 15), new Color(15, 15, 15).brighter(), new Color(15, 15, 15).brighter(), new Color(15, 15, 15).brighter().brighter());
        RectHelper.drawRectBetter(x + 1.0f, y - -3.0f, 15.0, 1.0, Color.WHITE.getRGB());
        RectHelper.drawRectBetter(x + 1.0f, y - -7.0f, 10.0, 1.0, Color.WHITE.getRGB());
        RectHelper.drawRectBetter(x + 1.0f, y - -11.0f, 15.0, 1.0, Color.WHITE.getRGB());
        if (this.isExpanded()) {
            RectHelper.drawColorRect((double)left1 - 1.5, top1, right1 + 1.0f, bottom1 + 3.0f, new Color(color).brighter(), new Color(color), new Color(color).darker(), new Color(color).darker().darker());
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(color), (double)((int)left1 - 2), (double)((int)bottom1), 125.0, 4.0, 15);
            }
        }
        CategoryPanel.mc.robotoRegularFontRender.drawStringWithShadow(this.getName(), x + 22.0f, y + 8.5f - 4.0f, -1);
    }

    @Override
    public boolean canExpand() {
        return !this.modules.isEmpty();
    }

    @Override
    public int getHeightWithExpand() {
        float height = this.getHeight();
        if (this.isExpanded()) {
            for (Component child : this.components) {
                ExpandableComponent expandableComponent;
                float cHeight = child.getHeight();
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                height += cHeight;
            }
        }
        return (int)height;
    }
}

