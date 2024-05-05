// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop.comp.impl.component;

import vip.Resolute.ui.click.drop.ClickGui;
import java.awt.Color;
import vip.Resolute.modules.impl.render.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import vip.Resolute.ui.click.drop.comp.impl.component.property.PropertyComponent;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Iterator;
import java.util.List;
import vip.Resolute.ui.click.drop.comp.impl.component.property.impl.EnumBoxProperty;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.ui.click.drop.comp.impl.component.property.impl.SliderPropertyComponent;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.ui.click.drop.comp.impl.component.property.impl.ColorPropertyComponent;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.ui.click.drop.comp.impl.component.property.impl.BooleanPropertyComponent;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.Setting;
import vip.Resolute.ui.click.drop.comp.Component;
import vip.Resolute.modules.Module;
import vip.Resolute.ui.click.drop.comp.impl.ExpandableComponent;

public final class ModuleComponent extends ExpandableComponent
{
    private final Module module;
    private boolean binding;
    
    public ModuleComponent(final Component parent, final Module module, final int x, final int y, final int width, final int height) {
        super(parent, module.getName(), x, y, width, height);
        this.module = module;
        final List<Setting> properties = module.getSettings();
        final int propertyX = 1;
        int propertyY = height;
        for (final Setting property : properties) {
            Component component = null;
            if (property instanceof BooleanSetting) {
                component = new BooleanPropertyComponent(this, (BooleanSetting)property, 1, propertyY, width - 2, 15);
            }
            else if (property instanceof ColorSetting) {
                component = new ColorPropertyComponent(this, (ColorSetting)property, 1, propertyY, width - 2, 15);
            }
            else if (property instanceof NumberSetting) {
                component = new SliderPropertyComponent(this, (NumberSetting)property, 1, propertyY, width - 2, 15);
            }
            else if (property instanceof ModeSetting) {
                component = new EnumBoxProperty(this, (ModeSetting)property, 1, propertyY, width - 2, 15);
            }
            if (component != null) {
                this.children.add(component);
                propertyY += component.getHeight();
            }
        }
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        if (this.isExpanded()) {
            int childY = 15;
            for (final Component child : this.children) {
                int cHeight = child.getHeight();
                if (child instanceof PropertyComponent) {
                    final PropertyComponent propertyComponent = (PropertyComponent)child;
                    if (!propertyComponent.getProperty().isAvailable()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)child;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                child.setY(childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }
        Gui.drawRect((float)x, (float)y, (float)(x + width), (float)(y + height), this.getBackgroundColor(this.isHovered(mouseX, mouseY)));
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.binding ? "Press A Key..." : this.getName(), (float)(x + 2), y + height / 2.0f - 4.0f, this.module.isEnabled() ? ClickGUI.color.getColor() : new Color(255, 255, 255).getRGB());
        if (this.canExpand()) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.isExpanded() ? "<" : ">", (float)(x + width - 7), y + height / 2.0f - 4.0f, new Color(255, 255, 255).getRGB());
        }
    }
    
    @Override
    public boolean canExpand() {
        return !this.children.isEmpty();
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
            ClickGui.escapeKeyInUse = true;
            this.module.setKey((keyCode == 1) ? 0 : keyCode);
            this.binding = false;
        }
    }
    
    @Override
    public int getHeightWithExpand() {
        int height = this.getHeight();
        if (this.isExpanded()) {
            for (final Component child : this.children) {
                int cHeight = child.getHeight();
                if (child instanceof PropertyComponent) {
                    final PropertyComponent propertyComponent = (PropertyComponent)child;
                    if (!propertyComponent.getProperty().isAvailable()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)child;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                height += cHeight;
            }
        }
        return height;
    }
}
