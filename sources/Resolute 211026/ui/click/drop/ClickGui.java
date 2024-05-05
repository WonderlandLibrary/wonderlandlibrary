// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop;

import vip.Resolute.ui.click.drop.comp.impl.ExpandableComponent;
import java.io.IOException;
import java.util.Iterator;
import vip.Resolute.util.render.RenderUtils;
import vip.Resolute.ui.click.drop.comp.impl.panel.impl.CategoryPanel;
import vip.Resolute.modules.Module;
import java.util.ArrayList;
import vip.Resolute.ui.click.drop.comp.Component;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public final class ClickGui extends GuiScreen
{
    public static boolean escapeKeyInUse;
    private static ClickGui instance;
    private final List<Component> components;
    private final Palette palette;
    private Component selectedPanel;
    
    public ClickGui() {
        this.components = new ArrayList<Component>();
        ClickGui.instance = this;
        this.palette = Palette.PINK;
        int panelX = 2;
        for (final Module.Category category : Module.Category.values()) {
            final CategoryPanel panel = new CategoryPanel(category, panelX, 2);
            this.components.add(panel);
            panelX += panel.getWidth() - 10;
            this.selectedPanel = panel;
        }
    }
    
    public static ClickGui getInstance() {
        return ClickGui.instance;
    }
    
    public Palette getPalette() {
        return this.palette;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        for (final Component component : this.components) {
            component.drawComponent(RenderUtils.getScaledResolution(), mouseX, mouseY);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!ClickGui.escapeKeyInUse) {
            super.keyTyped(typedChar, keyCode);
        }
        ClickGui.escapeKeyInUse = false;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        for (int i = this.components.size() - 1; i >= 0; --i) {
            final Component component = this.components.get(i);
            final int x = component.getX();
            final int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                final ExpandableComponent expandableComponent = (ExpandableComponent)component;
                if (expandableComponent.isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand();
                }
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                (this.selectedPanel = component).onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.selectedPanel.onMouseRelease(state);
    }
}
