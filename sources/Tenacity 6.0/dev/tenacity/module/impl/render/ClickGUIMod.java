// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.client.gui.GuiScreen;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.utils.render.Theme;
import dev.tenacity.module.Category;
import dev.tenacity.ui.clickguis.compact.CompactClickGUI;
import dev.tenacity.ui.clickguis.modern.ModernClickGUI;
import dev.tenacity.ui.clickguis.dropdown.DropdownClickGUI;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class ClickGUIMod extends Module
{
    public static final ModeSetting clickguiMode;
    public static final ModeSetting scrollMode;
    public static final BooleanSetting gradient;
    public static final BooleanSetting outlineAccent;
    public static final BooleanSetting transparent;
    public static final BooleanSetting walk;
    public static final NumberSetting clickHeight;
    public static final BooleanSetting rescale;
    public static final DropdownClickGUI dropdownClickGUI;
    public static final ModernClickGUI modernClickGUI;
    public static final CompactClickGUI compactClickGUI;
    private int activeCategory;
    private Category activeCategory2;
    public static int prevGuiScale;
    
    public ClickGUIMod() {
        super("ClickGUI", "Click GUI", Category.RENDER, "Displays modules");
        this.activeCategory = 0;
        this.activeCategory2 = Category.COMBAT;
        ClickGUIMod.clickHeight.addParent(ClickGUIMod.scrollMode, selection -> selection.is("Value"));
        ClickGUIMod.gradient.addParent(ClickGUIMod.clickguiMode, selection -> selection.is("Dropdown") && !Theme.getCurrentTheme().isGradient());
        ClickGUIMod.transparent.addParent(ClickGUIMod.clickguiMode, selection -> selection.is("Dropdown"));
        ClickGUIMod.outlineAccent.addParent(ClickGUIMod.clickguiMode, selection -> selection.is("Dropdown"));
        ClickGUIMod.scrollMode.addParent(ClickGUIMod.clickguiMode, selection -> selection.is("Dropdown"));
        this.addSettings(ClickGUIMod.clickguiMode, ClickGUIMod.scrollMode, ClickGUIMod.outlineAccent, ClickGUIMod.gradient, ClickGUIMod.transparent, ClickGUIMod.walk, ClickGUIMod.clickHeight, ClickGUIMod.rescale);
        this.setKey(54);
    }
    
    @Override
    public void toggle() {
        this.onEnable();
    }
    
    @Override
    public void onEnable() {
        if (ClickGUIMod.rescale.isEnabled()) {
            ClickGUIMod.prevGuiScale = ClickGUIMod.mc.gameSettings.guiScale;
            ClickGUIMod.mc.gameSettings.guiScale = 2;
        }
        final String mode = ClickGUIMod.clickguiMode.getMode();
        switch (mode) {
            case "Dropdown": {
                ClickGUIMod.mc.displayGuiScreen(ClickGUIMod.dropdownClickGUI);
                break;
            }
            case "Modern": {
                ClickGUIMod.mc.displayGuiScreen(ClickGUIMod.modernClickGUI);
                break;
            }
            case "": {
                ClickGUIMod.mc.displayGuiScreen(ClickGUIMod.compactClickGUI);
                break;
            }
        }
    }
    
    public int getActiveCategoryy() {
        return this.activeCategory;
    }
    
    public Category getActiveCategory() {
        return this.activeCategory2;
    }
    
    public void setActiveCategory(final int activeCategory) {
        this.activeCategory = activeCategory;
    }
    
    public void setActiveCategory(final Category activeCategory) {
        this.activeCategory2 = activeCategory;
    }
    
    static {
        clickguiMode = new ModeSetting("ClickGui", "Dropdown", new String[] { "Dropdown", "Modern", "Compact" });
        scrollMode = new ModeSetting("Scroll Mode", "Screen Height", new String[] { "Screen Height", "Value" });
        gradient = new BooleanSetting("Gradient", false);
        outlineAccent = new BooleanSetting("Outline Accent", true);
        transparent = new BooleanSetting("Transparent", false);
        walk = new BooleanSetting("Allow Movement", true);
        clickHeight = new NumberSetting("Tab Height", 250.0, 500.0, 100.0, 1.0);
        rescale = new BooleanSetting("Rescale GUI", true);
        dropdownClickGUI = new DropdownClickGUI();
        modernClickGUI = new ModernClickGUI();
        compactClickGUI = new CompactClickGUI();
    }
}
