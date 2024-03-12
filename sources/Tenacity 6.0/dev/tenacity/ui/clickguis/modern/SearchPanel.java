// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.modern;

import dev.tenacity.module.impl.render.ClickGUIMod;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.render.RenderUtil;
import java.awt.Color;
import dev.tenacity.utils.misc.HoveringUtil;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.StencilUtil;
import java.util.Iterator;
import dev.tenacity.Tenacity;
import java.util.ArrayList;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import java.util.HashMap;
import dev.tenacity.ui.clickguis.modern.components.ModuleRect;
import dev.tenacity.module.Module;
import java.util.List;
import dev.tenacity.utils.objects.Scroll;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.objects.PasswordField;

public class SearchPanel extends Panel
{
    private final PasswordField searchBar;
    private final boolean reinit = false;
    public Animation expandAnim2;
    Scroll settingScroll;
    private List<Module> modules;
    private List<ModuleRect> moduleRects;
    private String text;
    private Scroll scroll;
    private ModuleRect currentlySelected;
    private HashMap<Module, SettingsPanel> settingsPanelHashMap;
    private boolean rightClicked;
    
    public SearchPanel(final PasswordField passwordField) {
        this.settingScroll = new Scroll();
        this.text = " ";
        this.rightClicked = false;
        this.searchBar = passwordField;
    }
    
    @Override
    public void initGui() {
        (this.expandAnim2 = new DecelerateAnimation(300, 1.0, Direction.FORWARDS)).setDirection(Direction.BACKWARDS);
        this.moduleRects = new ArrayList<ModuleRect>();
        this.modules = Tenacity.INSTANCE.getModuleCollection().getModules();
        if (this.settingsPanelHashMap == null) {
            this.settingsPanelHashMap = new HashMap<Module, SettingsPanel>();
            for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModules()) {
                this.settingsPanelHashMap.put(module, new SettingsPanel(module));
            }
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.moduleRects.forEach(moduleRect -> moduleRect.keyTyped(typedChar, keyCode));
        if (this.currentlySelected != null) {
            this.settingsPanelHashMap.get(this.currentlySelected.module).keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        if (ModernClickGUI.searching) {
            this.expandAnim2.setDirection(this.rightClicked ? Direction.FORWARDS : Direction.BACKWARDS);
            final List<Module> possibleMods = Tenacity.INSTANCE.getModuleCollection().getModulesThatContainText(this.text);
            if (!this.text.equals(this.searchBar.getText())) {
                this.moduleRects.clear();
                this.scroll = new Scroll();
                possibleMods.forEach(module -> this.moduleRects.add(new ModuleRect(module)));
                this.moduleRects.forEach(ModuleRect::initGui);
                this.text = this.searchBar.getText();
                return;
            }
            StencilUtil.initStencilToWrite();
            Gui.drawRect2(this.x, this.y + 50.0f, 335.0, 190.0, -1);
            StencilUtil.readStencilBuffer(1);
            if (HoveringUtil.isHovering(this.x, this.bigRecty, 370.0f, 225.0f, mouseX, mouseY)) {
                this.scroll.onScroll(35);
            }
            this.scroll.setMaxScroll((float)Math.max(0, (this.moduleRects.size() - 2) * 50));
            int spacing = 0;
            for (final ModuleRect moduleRect : this.moduleRects) {
                moduleRect.rectWidth = 305.0f;
                moduleRect.x = this.x + 10.0f;
                moduleRect.y = this.y + 70.0f + spacing + this.scroll.getScroll();
                moduleRect.bigRecty = this.bigRecty;
                moduleRect.rectOffset = 70.0f;
                moduleRect.drawSettingThing = (this.currentlySelected == moduleRect);
                if (moduleRect.rightClicked) {
                    if (this.currentlySelected == moduleRect) {
                        this.rightClicked = false;
                        this.currentlySelected = null;
                        moduleRect.rightClicked = false;
                        continue;
                    }
                    this.rightClicked = true;
                    this.settingScroll = new Scroll();
                    this.currentlySelected = moduleRect;
                    moduleRect.rightClicked = false;
                }
                moduleRect.drawScreen(mouseX, mouseY);
                spacing += 50;
            }
            StencilUtil.uninitStencilBuffer();
            if (this.currentlySelected != null) {
                if (HoveringUtil.isHovering(this.x + 335.0f, this.y, 135.0f, 250.0f, mouseX, mouseY)) {
                    this.settingScroll.onScroll(35);
                }
                StencilUtil.initStencilToWrite();
                Gui.drawRect2(this.x, this.y - 15.0f, 370.0f + 125.0f * this.expandAnim2.getOutput().floatValue(), 255.0, -1);
                StencilUtil.readStencilBuffer(1);
                final float newX = this.x + 5.0f + 335.0f;
                RenderUtil.renderRoundedRect(newX, this.y - 15.0f, 130.0f, 255.0f, 10.0f, new Color(47, 49, 54).getRGB());
                Gui.drawGradientRect2(newX, this.y + 5.0f, 130.0, 8.0, new Color(0, 0, 0, 70).getRGB(), new Color(0, 0, 0, 1).getRGB());
                SearchPanel.tenacityFont24.drawCenteredString(this.currentlySelected.module.getName(), newX + 62.5f, this.y - 10.0f, -1);
                GL11.glEnable(3089);
                RenderUtil.scissor(newX, this.y + 5.5, 135.0f * this.expandAnim2.getOutput().floatValue(), 255.0);
                final SettingsPanel settingsPanel = this.settingsPanelHashMap.get(this.currentlySelected.module);
                this.settingScroll.setMaxScroll(Math.max(0.0f, settingsPanel.maxScroll - 100.0f));
                settingsPanel.x = this.x + 335.0f;
                settingsPanel.y = this.y - 15.0f + this.settingScroll.getScroll();
                settingsPanel.drawScreen(mouseX, mouseY);
                GL11.glDisable(3089);
                StencilUtil.uninitStencilBuffer();
            }
            ClickGUIMod.modernClickGUI.adjustWidth(125.0f * this.expandAnim2.getOutput().floatValue());
        }
        else {
            this.currentlySelected = null;
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.moduleRects.forEach(moduleRect -> moduleRect.mouseClicked(mouseX, mouseY, button));
        if (this.currentlySelected != null) {
            this.settingsPanelHashMap.get(this.currentlySelected.module).mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.moduleRects.forEach(moduleRect -> moduleRect.mouseReleased(mouseX, mouseY, state));
        if (this.currentlySelected != null) {
            this.settingsPanelHashMap.get(this.currentlySelected.module).mouseReleased(mouseX, mouseY, state);
        }
    }
}
