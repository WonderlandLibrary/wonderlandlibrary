// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.altmanager;

import java.util.Iterator;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.module.impl.render.NotificationsMod;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.ColorUtil;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.ui.Screen;
import dev.tenacity.Tenacity;
import dev.tenacity.ui.mainmenu.CustomMainMenu;
import dev.tenacity.ui.altmanager.panels.MicrosoftInfoPanel;
import dev.tenacity.ui.altmanager.panels.LoginPanel;
import dev.tenacity.ui.altmanager.panels.InfoPanel;
import java.util.ArrayList;
import dev.tenacity.ui.altmanager.panels.AltPanel;
import dev.tenacity.ui.sidegui.utils.ToggleButton;
import dev.tenacity.utils.objects.TextField;
import java.util.List;
import dev.tenacity.ui.altmanager.helpers.Alt;
import dev.tenacity.ui.altmanager.helpers.AltManagerUtils;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen
{
    private final AltManagerUtils utils;
    public Alt currentSessionAlt;
    private List<Panel> panels;
    public final TextField searchField;
    public final ToggleButton filterBanned;
    private final AltPanel.AltRect altRect;
    
    public GuiAltManager() {
        this.utils = new AltManagerUtils();
        this.searchField = new TextField(GuiAltManager.tenacityFont20);
        this.filterBanned = new ToggleButton("Filter banned accounts");
        this.altRect = new AltPanel.AltRect(null);
        if (this.panels == null) {
            (this.panels = new ArrayList<Panel>()).add(new InfoPanel());
            this.panels.add(new LoginPanel());
            this.panels.add(new MicrosoftInfoPanel());
            this.panels.add(new AltPanel());
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        if (keyCode == 1) {
            GuiAltManager.mc.displayGuiScreen(new CustomMainMenu());
            this.searchField.setFocused(false);
        }
        this.searchField.keyTyped(typedChar, keyCode);
        this.panels.forEach(panel -> panel.keyTyped(typedChar, keyCode));
    }
    
    @Override
    public void initGui() {
        if (GuiAltManager.mc.gameSettings.guiScale != 2) {
            Tenacity.prevGuiScale = GuiAltManager.mc.gameSettings.guiScale;
            Tenacity.updateGuiScale = true;
            GuiAltManager.mc.gameSettings.guiScale = 2;
            GuiAltManager.mc.resize(GuiAltManager.mc.displayWidth - 1, GuiAltManager.mc.displayHeight);
            GuiAltManager.mc.resize(GuiAltManager.mc.displayWidth + 1, GuiAltManager.mc.displayHeight);
        }
        this.panels.forEach(Screen::initGui);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.utils.writeAltsToFile();
        GLUtil.startBlend();
        final ScaledResolution sr = new ScaledResolution(GuiAltManager.mc);
        Gui.drawRect2(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtil.tripleColor(35).getRGB());
        final AltPanel altPanel = this.panels.get(3);
        int count = 0;
        int seperation = 0;
        for (final Panel panel : this.panels) {
            final boolean notAltPanel = !(panel instanceof AltPanel);
            if (notAltPanel) {
                panel.setX(16.0f);
                panel.setY((float)(20 + seperation));
                panel.setWidth(325.0f);
            }
            else {
                panel.setX(361.0f);
            }
            panel.drawScreen(mouseX, mouseY);
            if (notAltPanel) {
                seperation += (int)(panel.getHeight() + ((count >= 1) ? 10 : 25));
            }
            ++count;
        }
        if (this.currentSessionAlt != null) {
            this.altRect.setAlt(this.currentSessionAlt);
            this.altRect.setHeight(40.0f);
            this.altRect.setX(altPanel.getX() + 10.0f);
            this.altRect.setY(altPanel.getY() - (this.altRect.getHeight() + 10.0f));
            this.altRect.setWidth(Math.min(160.0f, this.searchField.getXPosition() - 10.0f - altPanel.getX()));
            this.altRect.setClickable(true);
            this.altRect.setSelected(false);
            this.altRect.setBackgroundColor(ColorUtil.tripleColor(27));
            this.altRect.setRemoveShit(true);
            this.altRect.drawScreen(mouseX, mouseY);
            GuiAltManager.tenacityBoldFont22.drawCenteredString("Current Account", this.altRect.getX() + this.altRect.getWidth() / 2.0f, this.altRect.getY() - (GuiAltManager.tenacityBoldFont22.getHeight() + 5), -1);
        }
        this.searchField.setRadius(5.0f);
        this.searchField.setFill(ColorUtil.tripleColor(17, 1.0f));
        this.searchField.setOutline(ColorUtil.applyOpacity(Color.WHITE, 0.0f));
        this.searchField.setHeight(20.5f);
        this.searchField.setWidth(145.5f);
        this.searchField.setXPosition(this.width - this.searchField.getRealWidth() - 20.0f);
        this.searchField.setYPosition(45.0f);
        this.searchField.setBackgroundText("Search");
        this.searchField.drawTextBox();
        this.filterBanned.setX(this.searchField.getXPosition() + 85.0f);
        this.filterBanned.setBypass(true);
        this.filterBanned.setAlpha(1.0f);
        this.filterBanned.setY(this.searchField.getYPosition() - (this.filterBanned.getWH() + 10.0f));
        this.filterBanned.drawScreen(mouseX, mouseY);
        Tenacity.INSTANCE.getModuleCollection().getModule(NotificationsMod.class).render();
        if (Alt.stage != 0) {
            AltPanel.loadingAltRect = null;
        }
        switch (Alt.stage) {
            case 1: {
                NotificationManager.post(NotificationType.INFO, "Alt Manager", "Invalid credentials!", 3.0f);
                Alt.stage = 0;
                break;
            }
            case 2: {
                NotificationManager.post(NotificationType.SUCCESS, "Alt Manager", "Logged in successfully!", 3.0f);
                Alt.stage = 0;
                break;
            }
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        this.filterBanned.mouseClicked(mouseX, mouseY, mouseButton);
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton));
        if (this.currentSessionAlt != null) {
            this.altRect.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, state));
    }
    
    @Override
    public void onGuiClosed() {
        if (Tenacity.updateGuiScale) {
            GuiAltManager.mc.gameSettings.guiScale = Tenacity.prevGuiScale;
            Tenacity.updateGuiScale = false;
        }
    }
    
    public AltManagerUtils getUtils() {
        return this.utils;
    }
    
    public AltPanel getAltPanel() {
        return this.panels.get(3);
    }
    
    public boolean isTyping() {
        return this.searchField.isFocused() || this.panels.get(1).textFields.stream().anyMatch(TextField::isFocused);
    }
}
