// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl.component.module;

import ru.fluger.client.ui.clickgui.ClickGuiScreen;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.helpers.render.AnimationHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import java.awt.Color;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import java.util.Iterator;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.VisibleComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.EnumBoxProperty;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.SliderPropertyComponent;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.ColorPropertyComponent;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.BooleanPropertyComponent;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.helpers.Helper;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;

public final class ModuleComponent extends ExpandableComponent implements Helper
{
    private final Feature module;
    private boolean binding;
    private final TimerHelper descTimer;
    private float moduleHeightAnim;
    private boolean canRenderArrow;
    int alpha;
    
    public ModuleComponent(final Component parent, final Feature module, final float x, final float y, final float width, final float height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.descTimer = new TimerHelper();
        this.alpha = 0;
        this.module = module;
        final float propertyX = 1.0f;
        for (final Setting setting : module.getOptions()) {
            if (setting instanceof BooleanSetting) {
                this.components.add(new BooleanPropertyComponent(this, (BooleanSetting)setting, propertyX, height, width - 2.0f, 17.0f));
            }
            else if (setting instanceof ColorSetting) {
                this.components.add(new ColorPropertyComponent(this, (ColorSetting)setting, propertyX, height, width - 2.0f, 15.0f));
            }
            else if (setting instanceof NumberSetting) {
                this.components.add(new SliderPropertyComponent(this, (NumberSetting)setting, propertyX, height, width - 2.0f, 15.0f));
            }
            else {
                if (!(setting instanceof ListSetting)) {
                    continue;
                }
                this.components.add(new EnumBoxProperty(this, (ListSetting)setting, propertyX, height, width - 2.0f, 22.0f));
            }
        }
        this.components.add(new VisibleComponent(module, this, propertyX, height, width - 2.0f, 15.0f));
    }
    
    @Override
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        if (this.moduleHeightAnim < 4.0f) {
            this.moduleHeightAnim = 4.0f;
        }
        final double x = this.getX();
        final double y = this.getY() - 1.0;
        final float width = this.getWidth();
        final float height = this.getHeight();
        float childY = 15.0f;
        for (final Component child : this.components) {
            if (child == null) {
                continue;
            }
            float cHeight = child.getHeight();
            if (!(child instanceof VisibleComponent)) {
                this.canRenderArrow = true;
                final PropertyComponent propertyComponent;
                if (child instanceof PropertyComponent && !(propertyComponent = (PropertyComponent)child).getProperty().isVisible()) {
                    continue;
                }
            }
            final ExpandableComponent expandableComponent;
            if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                cHeight = (float)expandableComponent.getHeightWithExpand();
            }
            if (!this.isExpanded()) {
                continue;
            }
            child.setY(childY);
            child.drawComponent(scaledResolution, mouseX, mouseY);
            childY += cHeight;
        }
        final Color onecolor = new Color(ClickGui.color.getColor());
        final Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        final int color = c.getRGB();
        ModuleComponent.mc.robotoRegularFontRender.drawString(this.isExpanded() ? "" : "...", (float)x + width - 10.0f, (float)y + height / 2.0f - 1.0f - this.moduleHeightAnim, -1);
        this.moduleHeightAnim = (this.isHovered(mouseX, mouseY) ? ((float)(this.moduleHeightAnim + 0.20000000298023224 * bib.frameTime * 0.10000000149011612)) : ((float)(this.moduleHeightAnim - 0.20000000298023224 * bib.frameTime * 0.10000000149011612)));
        this.moduleHeightAnim = rk.a(this.moduleHeightAnim, 4.0f, 6.0f);
        final String name = this.binding ? "Press a key..." : this.getName();
        if (ClickGui.glow.getCurrentValue() && this.module.getState()) {
            ModuleComponent.mc.rubik_18.drawCenteredBlurredString(name, (float)x + width - 60.0f, (float)y + height / 2.0f - this.moduleHeightAnim, ClickGui.glowRadius.getCurrentValueInt(), RenderHelper.injectAlpha(new Color(color).brighter(), 255), new Color(color).brighter().getRGB());
        }
        else {
            ModuleComponent.mc.rubik_18.drawCenteredString(name, (float)x + width - 60.0f, (float)y + height / 2.0f - this.moduleHeightAnim, this.module.getState() ? new Color(color).brighter().getRGB() : Color.GRAY.brighter().getRGB());
        }
        if (this.isHovered(mouseX, mouseY)) {
            this.alpha = (int)AnimationHelper.animation((float)this.alpha, 255.0f, 3.0f);
            this.alpha = rk.a(this.alpha, 0, 255);
            if (this.descTimer.hasReached(150.0)) {
                RenderHelper.drawBlurredShadow((float)(mouseX + 14), (float)(mouseY - 2), (float)(ModuleComponent.mc.fontRenderer.getStringWidth(this.module.getDesc()) + 2), 8.0f, 18, new Color(0, 0, 0, this.alpha));
                RectHelper.drawGradientRect(mouseX + 17, mouseY - 2, mouseX + 18 + ModuleComponent.mc.fontRenderer.getStringWidth(this.module.getDesc()) + 2, mouseY + 8, RenderHelper.injectAlpha(new Color(color).darker(), this.alpha).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker(), this.alpha).getRGB());
                ModuleComponent.mc.fontRenderer.drawStringWithShadow(this.module.getDesc(), mouseX + 18, mouseY, new Color(255, 255, 255, this.alpha).getRGB());
            }
        }
        else {
            this.alpha = 0;
            this.descTimer.reset();
        }
    }
    
    @Override
    public boolean canExpand() {
        return !this.components.isEmpty();
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
        switch (button) {
            case 0: {
                this.module.toggle();
                break;
            }
            case 2: {
                this.binding = !this.binding;
                break;
            }
        }
    }
    
    @Override
    public void onKeyPress(final int keyCode) {
        if (this.binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            this.module.setBind((keyCode == 1) ? 0 : keyCode);
            this.binding = false;
        }
    }
    
    @Override
    public int getHeightWithExpand() {
        float height = this.getHeight();
        if (this.isExpanded()) {
            for (final Component child : this.components) {
                float cHeight = child.getHeight();
                final PropertyComponent propertyComponent;
                if (!(child instanceof VisibleComponent) && child instanceof PropertyComponent && !(propertyComponent = (PropertyComponent)child).getProperty().isVisible()) {
                    continue;
                }
                final ExpandableComponent expandableComponent;
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = (float)expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return (int)height;
    }
}
