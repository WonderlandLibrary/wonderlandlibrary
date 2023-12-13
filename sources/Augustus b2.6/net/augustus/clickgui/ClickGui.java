// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.clickgui;

import java.util.List;
import net.augustus.ui.widgets.CustomButton;
import net.augustus.Augustus;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.interfaces.MC;
import java.util.Comparator;
import net.lenni0451.eventapi.events.IEvent;
import net.augustus.utils.EventHandler;
import net.augustus.events.EventClickGui;
import java.io.IOException;
import net.augustus.clickgui.screens.ConfigGui;
import net.minecraft.client.gui.GuiButton;
import java.util.Iterator;
import java.util.Collection;
import net.augustus.settings.Setting;
import net.augustus.modules.Module;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Categorys;
import net.augustus.clickgui.buttons.SettingsButton;
import net.augustus.clickgui.buttons.ModuleButton;
import net.augustus.clickgui.buttons.CategoryButton;
import java.util.ArrayList;
import net.augustus.utils.interfaces.SM;
import net.augustus.utils.interfaces.MM;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen implements MM, SM
{
    private final ArrayList<CategoryButton> categoryButtons;
    private final ArrayList<ModuleButton> moduleButtonsRandom;
    private final ArrayList<ModuleButton> moduleButtons;
    private final ArrayList<SettingsButton> settingsButtons;
    private Categorys draggedCategory;
    private boolean keyPress;
    private final TimeHelper timeHelper;
    private double sizeCounter;
    private String currentSorting;
    
    public ClickGui(final String title) {
        this.categoryButtons = new ArrayList<CategoryButton>();
        this.moduleButtonsRandom = new ArrayList<ModuleButton>();
        this.moduleButtons = new ArrayList<ModuleButton>();
        this.settingsButtons = new ArrayList<SettingsButton>();
        this.draggedCategory = null;
        this.timeHelper = new TimeHelper();
        this.currentSorting = null;
        int x = 10;
        final int y = 10;
        final int width = 100;
        final int height = 18;
        final int distancePanels = 20 + width;
        for (final Categorys category : Categorys.values()) {
            final String categoryName = Character.toUpperCase(category.name().toLowerCase().charAt(0)) + category.name().toLowerCase().substring(1);
            this.categoryButtons.add(new CategoryButton(1, x, y, width, height, categoryName, Color.white, category));
            int i = 1;
            final int moduleX = x + 3;
            final int moduleWidth = width - 6;
            final int moduleHeight = 16;
            for (final Module module : ClickGui.mm.getModules()) {
                if (module.getCategory() == category) {
                    this.moduleButtons.add(new ModuleButton(2, moduleX, y + i * moduleHeight, moduleWidth, moduleHeight, module.getName(), new Color(0, 0, 0, 190), category, module));
                    int j = 1;
                    final int settingHeight = 14;
                    for (final Setting setting : ClickGui.sm.getStgs()) {
                        if (setting.getParent() == module) {
                            this.settingsButtons.add(new SettingsButton(3, moduleX + moduleWidth + 1, y + i * moduleHeight + j * settingHeight, moduleWidth, settingHeight, setting.getName(), new Color(0, 0, 0, 190), module, setting));
                            ++j;
                        }
                    }
                    ++i;
                }
            }
            x += distancePanels;
        }
        this.moduleButtonsRandom.addAll(this.moduleButtons);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 4) {
            this.mc.displayGuiScreen(new ConfigGui(this));
        }
        super.actionPerformed(button);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final EventClickGui eventClickGui = new EventClickGui();
        EventHandler.call(eventClickGui);
        if (this.currentSorting == null) {
            final String selected = ClickGui.mm.clickGUI.sorting.getSelected();
            switch (selected) {
                case "Random": {
                    this.moduleButtons.clear();
                    this.moduleButtons.addAll(this.moduleButtonsRandom);
                    break;
                }
                case "Alphabet": {
                    this.moduleButtons.sort(Comparator.comparing(categoryButton -> categoryButton.displayString));
                    break;
                }
                case "Length": {
                    this.moduleButtons.sort(Comparator.comparingDouble(categoryButton -> 1000 - this.mc.fontRendererObj.getStringWidth(categoryButton.displayString)));
                    break;
                }
            }
        }
        else if (!this.currentSorting.equals(ClickGui.mm.clickGUI.sorting.getSelected())) {
            final String selected2 = ClickGui.mm.clickGUI.sorting.getSelected();
            switch (selected2) {
                case "Random": {
                    this.moduleButtons.clear();
                    this.moduleButtons.addAll(this.moduleButtonsRandom);
                    break;
                }
                case "Alphabet": {
                    this.moduleButtons.sort(Comparator.comparing(categoryButton -> categoryButton.displayString));
                    break;
                }
                case "Length": {
                    this.moduleButtons.sort(Comparator.comparingDouble(categoryButton -> 1000 - this.mc.fontRendererObj.getStringWidth(categoryButton.displayString)));
                    break;
                }
            }
        }
        this.currentSorting = ClickGui.mm.clickGUI.sorting.getSelected();
        for (final ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.setVisible(false);
        }
        for (final SettingsButton settingsButton : this.settingsButtons) {
            settingsButton.setVisible(false);
        }
        for (final CategoryButton categoryButton2 : this.categoryButtons) {
            if (categoryButton2.isUnfolded()) {
                for (final ModuleButton moduleButton2 : this.moduleButtons) {
                    if (moduleButton2.getParent() == categoryButton2.getCategory()) {
                        moduleButton2.setVisible(true);
                        for (final SettingsButton settingsButton2 : this.settingsButtons) {
                            if (settingsButton2.getModule() == moduleButton2.getModule() && moduleButton2.hasVisibleSetting() && settingsButton2.getSetting().isVisible()) {
                                settingsButton2.setVisible(true);
                            }
                        }
                    }
                }
            }
        }
        final int height = 18;
        for (final CategoryButton categoryButton3 : this.categoryButtons) {
            if (this.draggedCategory != null && this.draggedCategory == categoryButton3.getCategory()) {
                categoryButton3.xPosition = (int)(mouseX - (categoryButton3.getCm()[0] - categoryButton3.getCm()[2]));
                categoryButton3.yPosition = (int)(mouseY - (categoryButton3.getCm()[1] - categoryButton3.getCm()[3]));
            }
            categoryButton3.drawButton(MC.mc, mouseX, mouseY);
            if (categoryButton3.isUnfolded()) {
                int i = 0;
                final int moduleX = categoryButton3.xPosition + 3;
                final int moduleHeight = 16;
                for (final ModuleButton moduleButton3 : this.moduleButtons) {
                    if (moduleButton3.getParent() == categoryButton3.getCategory()) {
                        moduleButton3.xPosition = moduleX;
                        moduleButton3.yPosition = height + i * moduleHeight + categoryButton3.yPosition;
                        moduleButton3.drawButton(MC.mc, mouseX, mouseY);
                        int settingsPerModule = 0;
                        int underSettingsPerModule = 0;
                        for (final SettingsButton sb : this.settingsButtons) {
                            if (sb.getModule() == moduleButton3.getModule() && moduleButton3.hasVisibleSetting() && sb.getSetting().isVisible()) {
                                if (sb.isDropdownVisible()) {
                                    if (sb.getSetting() instanceof StringValue) {
                                        underSettingsPerModule += ((StringValue)sb.getSetting()).getStringList().length;
                                    }
                                    else if (sb.getSetting() instanceof BooleansSetting) {
                                        underSettingsPerModule += ((BooleansSetting)sb.getSetting()).getSettingList().length;
                                    }
                                }
                                ++settingsPerModule;
                            }
                        }
                        final int settingHeight = 14;
                        int j = 0;
                        int k = 0;
                        final ScaledResolution sr = new ScaledResolution(this.mc);
                        int buttonYPos = moduleButton3.yPosition;
                        final int maxYSize = settingsPerModule * settingHeight + underSettingsPerModule * settingHeight;
                        final int moduleButtonPosToBottomHeight = sr.getScaledHeight() - moduleButton3.yPosition;
                        if (maxYSize + Math.min(Math.max(buttonYPos - (maxYSize - sr.getScaledHeight() - moduleButton3.yPosition), 0) / 2, 22) > moduleButtonPosToBottomHeight) {
                            buttonYPos = Math.max(buttonYPos - (maxYSize - moduleButtonPosToBottomHeight), 0);
                            buttonYPos = Math.max(buttonYPos - Math.min(buttonYPos / 2, 22), 0);
                            if (maxYSize > sr.getScaledHeight()) {
                                final int mouseDelta = (int)(moduleButton3.getMouseWheelDelata() + Mouse.getDWheel() / 10.0f);
                                final int clampHeight = MathHelper.clamp_int(buttonYPos + mouseDelta, sr.getScaledHeight() - maxYSize, 0);
                                final int diff = buttonYPos + mouseDelta - clampHeight;
                                moduleButton3.setMouseWheelDelata(mouseDelta - diff);
                                buttonYPos += clampHeight;
                            }
                        }
                        for (final SettingsButton settingsButton3 : this.settingsButtons) {
                            if (settingsButton3.getModule() == moduleButton3.getModule() && moduleButton3.hasVisibleSetting() && settingsButton3.getSetting().isVisible()) {
                                settingsButton3.xPosition = moduleX + moduleButton3.getButtonWidth() + 1;
                                settingsButton3.yPosition = buttonYPos + j * settingHeight + k * settingHeight;
                                settingsButton3.drawButton(MC.mc, mouseX, mouseY);
                                if (settingsButton3.isDropdownVisible()) {
                                    if (settingsButton3.getSetting() instanceof StringValue) {
                                        k += ((StringValue)settingsButton3.getSetting()).getStringList().length;
                                    }
                                    else if (settingsButton3.getSetting() instanceof BooleansSetting) {
                                        k += ((BooleansSetting)settingsButton3.getSetting()).getSettingList().length;
                                    }
                                }
                                ++j;
                            }
                        }
                        ++i;
                    }
                }
            }
        }
        if (this.timeHelper.reached(70L)) {
            for (final SettingsButton settingsButton4 : this.settingsButtons) {
                settingsButton4.tick();
            }
            this.timeHelper.reset();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final CategoryButton categoryButton : this.categoryButtons) {
            categoryButton.click(mouseX, mouseY, mouseButton);
        }
        for (final ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.click1(mouseX, mouseY, mouseButton);
        }
        for (final SettingsButton settingsButton : this.settingsButtons) {
            settingsButton.click3(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        for (final CategoryButton categoryButton : this.categoryButtons) {
            if (this.mouseOver(mouseX, mouseY, categoryButton.xPosition, categoryButton.yPosition, categoryButton.getButtonWidth(), categoryButton.getHeight()) && clickedMouseButton == 0 && this.draggedCategory == null) {
                for (final Categorys categorys : Categorys.values()) {
                    if (categorys == categoryButton.getCategory()) {
                        this.draggedCategory = categorys;
                        break;
                    }
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.draggedCategory = null;
        for (final SettingsButton settingsButton : this.settingsButtons) {
            settingsButton.mouseReleased();
        }
    }
    
    @Override
    public void initGui() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.buttonList.add(new CustomButton(4, sr.getScaledWidth() - 70, sr.getScaledHeight() - 40, 50, 20, "Configs", Augustus.getInstance().getClientColor()));
        this.keyPress = false;
        this.setEventButton(-1);
        super.initGui();
    }
    
    @Override
    public void onGuiClosed() {
        Augustus.getInstance().getConverter().clickGuiSaver(this.categoryButtons);
        for (final ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.onClosed();
        }
        super.onGuiClosed();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        for (final SettingsButton settingsButton : this.settingsButtons) {
            settingsButton.onKey(keyCode);
        }
        for (final ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.onKey(keyCode);
        }
        if (keyCode == ClickGui.mm.clickGUI.getKey() && this.keyPress) {
            this.onGuiClosed();
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    public boolean mouseOver(final double mouseX, final double mouseY, final double posX, final double posY, final double width, final double height) {
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }
    
    public ArrayList<CategoryButton> getCategoryButtons() {
        return this.categoryButtons;
    }
}
