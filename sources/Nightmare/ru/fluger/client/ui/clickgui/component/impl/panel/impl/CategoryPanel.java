// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl.panel.impl;

import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import ru.fluger.client.ui.clickgui.Palette;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;
import ru.fluger.client.feature.impl.hud.ClickGui;
import java.util.Iterator;
import ru.fluger.client.ui.clickgui.component.impl.component.module.ModuleComponent;
import java.util.Collections;
import ru.fluger.client.Fluger;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;
import java.util.List;
import ru.fluger.client.helpers.Helper;
import ru.fluger.client.ui.clickgui.component.impl.panel.DraggablePanel;

public final class CategoryPanel extends DraggablePanel implements Helper
{
    public static final float HEADER_WIDTH = 120.0f;
    public static final float X_ITEM_OFFSET = 1.0f;
    public static final float ITEM_HEIGHT = 15.0f;
    public static final float HEADER_HEIGHT = 17.0f;
    private final List<Feature> modules;
    public Type category;
    
    public CategoryPanel(final Type category, final float x, final float y) {
        super(null, category.name(), x, y, 120.0f, 17.0f);
        float moduleY = 17.0f;
        this.modules = Collections.unmodifiableList((List<? extends Feature>)Fluger.instance.featureManager.getFeaturesForCategory(category));
        for (final Feature module : this.modules) {
            this.components.add(new ModuleComponent(this, module, 1.0f, moduleY, 118.0f, 15.0f));
            moduleY += 15.0f;
        }
        this.category = category;
    }
    
    @Override
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final float x = (float)this.getX();
        final float y = (float)this.getY();
        final float width = this.getWidth();
        float headerHeight;
        final float height = headerHeight = this.getHeight();
        final int heightWithExpand = this.getHeightWithExpand();
        if ((ClickGui.backGroundBlur.getCurrentValue() || ClickGui.panelMode.currentMode.equals("Blur")) && CategoryPanel.mc.t.ofFastRender) {
            CategoryPanel.mc.t.ofFastRender = false;
        }
        if (ClickGuiScreen.getInstance().getPalette() == Palette.DEFAULT) {
            headerHeight = (this.isExpanded() ? ((float)heightWithExpand) : height);
        }
        final float startAlpha = 1.0f;
        final int size = 8;
        final float left = x;
        final float right = x + 120.0f;
        final float bottom = y + 11.0f;
        final float startAlpha2 = 0.14f;
        final int size2 = 25;
        final float left2 = x + 1.0f;
        final float right2 = x + width;
        final float bottom2 = y + headerHeight - 2.0f;
        final float top1 = y + headerHeight - 2.0f;
        final float top2 = y + 13.0f;
        final Color onecolor = new Color(ClickGui.color.getColor());
        final Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        final int color = c.getRGB();
        if (this.isExpanded()) {
            RectHelper.renderShadowVertical(new Color(8, 8, 8, 255), 3.0f, startAlpha2, size2, right2 - 1.0f, top2, bottom2 + 2.0f, true, false);
            RectHelper.renderShadowVertical(new Color(8, 8, 8, 255), 3.0f, startAlpha2, size2, left2 - 0.3, top2, bottom2 + 4.0f, false, false);
        }
        if (ClickGui.panelMode.currentMode.equals("Blur") && ClickGui.scale.getCurrentValue() == 1.0f) {
            RenderHelper.renderBlur((int)x, (int)y, (int)width, (int)headerHeight - 2, 15);
        }
        else if (ClickGui.panelMode.currentMode.equals("Rect")) {
            RectHelper.drawRect(x, y, x + width, y + headerHeight - 2.0f, RenderHelper.injectAlpha(new Color(color).darker(), 0).getRGB());
            RectHelper.drawGradientRect(x, y, x + width, y + headerHeight - 2.0f, RenderHelper.injectAlpha(new Color(color).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker(), 120).getRGB());
        }
        if (this.isExpanded()) {
            float moduleY = height;
            for (final Component child : this.components) {
                child.setY(moduleY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                float cHeight = child.getHeight();
                final ExpandableComponent expandableComponent;
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = (float)(expandableComponent.getHeightWithExpand() + 5);
                }
                moduleY += cHeight;
            }
        }
        RenderHelper.renderBlurredShadow(new Color(0, 0, 0, 150), x - 3.0f, y, 123.0, 15.0, 15);
        RectHelper.drawGradientRect(x - 3.0f, y, x + 122.0f, y + 15.0f, RenderHelper.injectAlpha(new Color(color).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker(), 120).getRGB());
        RenderHelper.drawImage(new nf("nightmare/clickgui/" + this.getName().toLowerCase() + ".png"), x + 1.0f, y + 1.0f, 15.0f, 13.0f, new Color(color));
        if (this.isExpanded()) {
            RectHelper.drawColorRect(left2 - 1.0, top1, right2, bottom2 + 2.0f, new Color(color).brighter(), new Color(color), new Color(color).darker(), new Color(color).darker().darker());
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(color), (int)left2 - 2, (int)bottom2, 125.0, 4.0, 15);
            }
        }
        CategoryPanel.mc.robotoRegularFontRender.drawStringWithOutline(this.getName(), x + 22.0f, y + 8.5f - 4.0f, -1);
    }
    
    @Override
    public boolean canExpand() {
        return !this.modules.isEmpty();
    }
    
    @Override
    public int getHeightWithExpand() {
        float height = this.getHeight();
        if (this.isExpanded()) {
            for (final Component child : this.components) {
                float cHeight = child.getHeight();
                final ExpandableComponent expandableComponent;
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = (float)(expandableComponent.getHeightWithExpand() + 5);
                }
                height += cHeight;
            }
        }
        return (int)height;
    }
}
