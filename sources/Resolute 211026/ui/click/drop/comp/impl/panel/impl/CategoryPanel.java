// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop.comp.impl.panel.impl;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import vip.Resolute.ui.click.drop.comp.impl.ExpandableComponent;
import net.minecraft.client.gui.Gui;
import vip.Resolute.modules.impl.render.ClickGUI;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Iterator;
import vip.Resolute.ui.click.drop.comp.impl.component.ModuleComponent;
import java.util.Collections;
import vip.Resolute.Resolute;
import vip.Resolute.ui.click.drop.comp.Component;
import vip.Resolute.util.misc.StringUtils;
import vip.Resolute.modules.Module;
import java.util.List;
import vip.Resolute.ui.click.drop.comp.impl.panel.DraggablePanel;

public final class CategoryPanel extends DraggablePanel
{
    public static final int HEADER_WIDTH = 120;
    public static final int X_ITEM_OFFSET = 1;
    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    private final List<Module> modules;
    
    public CategoryPanel(final Module.Category category, final int x, final int y) {
        super(null, StringUtils.upperSnakeCaseToPascal(category.name()), x, y, 120, 17);
        int moduleY = 17;
        this.modules = Collections.unmodifiableList((List<? extends Module>)Resolute.getModulesByCategory(category));
        for (final Module module : this.modules) {
            this.children.add(new ModuleComponent(this, module, 2, moduleY, 98, 15));
            moduleY += 15;
        }
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int heightWithExpand = this.getHeightWithExpand();
        final int headerHeight = this.isExpanded() ? heightWithExpand : height;
        Gui.drawRect((float)x, (float)y, (float)(x + width - 18), (float)(y + height), ClickGUI.color.getColor());
        Gui.drawRect((float)x, (float)y, (float)(x + 2), (float)(y + headerHeight), ClickGUI.color.getColor());
        Gui.drawRect((float)(x + width - 20), (float)y, (float)(x + width - 18), (float)(y + headerHeight), ClickGUI.color.getColor());
        Gui.drawRect((float)x, (float)(y + headerHeight), (float)(x + width - 18), (float)(y + headerHeight + 2), ClickGUI.color.getColor());
        if (this.isExpanded()) {
            int moduleY = height;
            for (final Component child : this.children) {
                child.setY(moduleY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = child.getHeight();
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)child;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                moduleY += cHeight;
            }
        }
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.getName(), (float)(x + 2), y + 8.5f - 4.0f, new Color(255, 255, 255).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.isExpanded() ? "<" : ">", (float)(x + width - 27), y + height / 2.0f - 4.0f, new Color(255, 255, 255).getRGB());
    }
    
    @Override
    public boolean canExpand() {
        return !this.modules.isEmpty();
    }
    
    @Override
    public int getHeightWithExpand() {
        int height = this.getHeight();
        if (this.isExpanded()) {
            for (final Component child : this.children) {
                int cHeight = child.getHeight();
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
