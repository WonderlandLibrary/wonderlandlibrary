// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.modern;

import dev.tenacity.module.impl.render.ClickGUIMod;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import java.awt.Color;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.misc.HoveringUtil;
import java.util.Iterator;
import dev.tenacity.Tenacity;
import dev.tenacity.module.ModuleCollection;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import java.util.HashMap;
import dev.tenacity.utils.objects.Scroll;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.clickguis.modern.components.ModuleRect;
import java.util.List;

public class ModulesPanel extends Panel
{
    public List<ModuleRect> modules;
    public Animation expandAnim;
    public Animation expandAnim2;
    Scroll settingScroll;
    private HashMap<Module, SettingsPanel> settingsPanelHashMap;
    private HashMap<Category, Scroll> scrollHashMap;
    private boolean rightClicked;
    public Category currentCategory;
    private boolean typing;
    private ModuleRect currentlySelected;
    
    public ModulesPanel() {
        this.settingScroll = new Scroll();
        this.rightClicked = false;
        this.typing = false;
    }
    
    @Override
    public void initGui() {
        (this.expandAnim2 = new DecelerateAnimation(300, 1.0, Direction.FORWARDS)).setDirection(Direction.BACKWARDS);
        if (this.scrollHashMap == null) {
            this.scrollHashMap = new HashMap<Category, Scroll>();
            for (final Category category : Category.values()) {
                this.scrollHashMap.put(category, new Scroll());
            }
            this.scrollHashMap.put(null, new Scroll());
        }
        this.refreshSettingMap();
    }
    
    public void refreshSettingMap() {
        if (this.settingsPanelHashMap == null || ModuleCollection.reloadModules) {
            this.settingsPanelHashMap = new HashMap<Module, SettingsPanel>();
            for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModules()) {
                final SettingsPanel settingsPanel = new SettingsPanel(module);
                settingsPanel.initGui();
                this.settingsPanelHashMap.put(module, settingsPanel);
            }
        }
        else {
            this.settingsPanelHashMap.forEach((m, p) -> p.initGui());
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.modules.forEach(moduleRect -> moduleRect.keyTyped(typedChar, keyCode));
        if (this.currentlySelected != null) {
            this.settingsPanelHashMap.get(this.currentlySelected.module).keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.typing = false;
        if (ModuleCollection.reloadModules) {
            this.initGui();
            ModuleCollection.reloadModules = false;
            return;
        }
        this.expandAnim2.setDirection(this.rightClicked ? Direction.FORWARDS : Direction.BACKWARDS);
        int spacing = 0;
        final Scroll scroll = this.scrollHashMap.get(this.currentCategory);
        if ((!HoveringUtil.isHovering(this.x + (250.0f + 55.0f * this.expandAnim.getOutput().floatValue()), this.y, 135.0f, 250.0f, mouseX, mouseY) || this.currentlySelected == null) && !Tenacity.INSTANCE.getSideGui().isFocused()) {
            scroll.onScroll(25);
        }
        final double scrollVal = MathUtils.roundToHalf(scroll.getScroll());
        ModulesPanel.tenacityFont18.drawCenteredString("Click your scroll wheel while hovering a module to change a keybind", this.x + 152.5f, (float)(this.y - 15.0f + scrollVal), new Color(128, 134, 141, 150));
        scroll.setMaxScroll((float)Math.max(0, (this.modules.size() - 4) * 50));
        for (final ModuleRect module : this.modules) {
            module.rectWidth = 305.0f;
            module.x = this.x;
            module.y = (float)(this.y + spacing + scrollVal);
            module.bigRecty = this.bigRecty;
            if (!this.typing) {
                this.typing = (module.binding != null);
            }
            module.drawSettingThing = (this.currentlySelected == module);
            module.drawScreen(mouseX, mouseY);
            spacing += 50;
            if (module.rightClicked) {
                if (this.currentlySelected == module) {
                    this.rightClicked = false;
                    this.currentlySelected = null;
                    module.rightClicked = false;
                }
                else {
                    this.rightClicked = true;
                    this.settingScroll = new Scroll();
                    this.currentlySelected = module;
                    module.rightClicked = false;
                }
            }
        }
        if (this.currentlySelected != null) {
            if (HoveringUtil.isHovering(this.x + 305.0f, this.y, 135.0f, 250.0f, mouseX, mouseY)) {
                this.settingScroll.onScroll(25);
            }
            final float newX = this.x + 5.0f + 305.0f;
            RoundedUtil.drawRound(newX, this.y - 20.0f, 130.0f, 255.0f, 8.0f, new Color(47, 49, 54));
            RenderUtil.setAlphaLimit(0.0f);
            Gui.drawGradientRect2(newX - 0.5f, this.y, 130.5, 8.0, new Color(0, 0, 0, 70).getRGB(), new Color(0, 0, 0, 0).getRGB());
            ModulesPanel.tenacityBoldFont22.drawCenteredString(this.currentlySelected.module.getName(), newX + 62.5f, this.y - 15.0f, -1);
            GL11.glEnable(3089);
            RenderUtil.scissor(newX, this.y + 0.5f, 135.0, 255.0);
            final SettingsPanel settingsPanel = this.settingsPanelHashMap.get(this.currentlySelected.module);
            this.settingScroll.setMaxScroll(Math.max(0.0f, settingsPanel.maxScroll - 100.0f));
            settingsPanel.x = this.x + 305.0f;
            settingsPanel.y = (float)(this.y - 20.0f + MathUtils.roundToHalf(this.settingScroll.getScroll()));
            settingsPanel.drawScreen(mouseX, mouseY);
            if (!this.typing) {
                this.typing = settingsPanel.typing;
            }
            GL11.glDisable(3089);
        }
        ClickGUIMod.modernClickGUI.adjustWidth(125.0f * this.expandAnim2.getOutput().floatValue());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.modules.forEach(moduleRect -> moduleRect.mouseClicked(mouseX, mouseY, button));
        if (this.currentlySelected != null) {
            this.settingsPanelHashMap.get(this.currentlySelected.module).mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.modules.forEach(moduleRect -> moduleRect.mouseReleased(mouseX, mouseY, state));
        if (this.currentlySelected != null) {
            this.settingsPanelHashMap.get(this.currentlySelected.module).mouseReleased(mouseX, mouseY, state);
        }
    }
    
    public boolean isTyping() {
        return this.typing;
    }
}
