/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsScreen;
import org.lwjgl.input.Keyboard;

public class RealmsSettingsScreen
extends RealmsScreen {
    private final RealmsConfigureWorldScreen configureWorldScreen;
    private final RealmsServer serverData;
    private static final int BUTTON_CANCEL_ID = 0;
    private static final int BUTTON_DONE_ID = 1;
    private static final int NAME_EDIT_BOX = 2;
    private static final int DESC_EDIT_BOX = 3;
    private static final int BUTTON_OPEN_CLOSE_ID = 5;
    private final int COMPONENT_WIDTH = 212;
    private RealmsButton doneButton;
    private RealmsEditBox descEdit;
    private RealmsEditBox nameEdit;

    public RealmsSettingsScreen(RealmsConfigureWorldScreen configureWorldScreen, RealmsServer serverData) {
        this.configureWorldScreen = configureWorldScreen;
        this.serverData = serverData;
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
        this.descEdit.tick();
        this.doneButton.active(this.nameEdit.getValue() != null && !this.nameEdit.getValue().trim().isEmpty());
    }

    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        int center = this.width() / 2 - 106;
        this.doneButton = RealmsSettingsScreen.newButton(1, center - 2, RealmsConstants.row(12), 106, 20, RealmsSettingsScreen.getLocalizedString("mco.configure.world.buttons.done"));
        this.buttonsAdd(this.doneButton);
        this.buttonsAdd(RealmsSettingsScreen.newButton(0, this.width() / 2 + 2, RealmsConstants.row(12), 106, 20, RealmsSettingsScreen.getLocalizedString("gui.cancel")));
        this.buttonsAdd(RealmsSettingsScreen.newButton(5, this.width() / 2 - 53, RealmsConstants.row(0), 106, 20, RealmsSettingsScreen.getLocalizedString(this.serverData.state.equals((Object)RealmsServer.State.OPEN) ? "mco.configure.world.buttons.close" : "mco.configure.world.buttons.open")));
        this.nameEdit = this.newEditBox(2, center, RealmsConstants.row(4), 212, 20);
        this.nameEdit.setFocus(true);
        this.nameEdit.setMaxLength(32);
        if (this.serverData.getName() != null) {
            this.nameEdit.setValue(this.serverData.getName());
        }
        this.descEdit = this.newEditBox(3, center, RealmsConstants.row(8), 212, 20);
        this.descEdit.setMaxLength(32);
        if (this.serverData.getDescription() != null) {
            this.descEdit.setValue(this.serverData.getDescription());
        }
    }

    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 0: {
                Realms.setScreen(this.configureWorldScreen);
                break;
            }
            case 1: {
                this.save();
                break;
            }
            case 5: {
                if (this.serverData.state.equals((Object)RealmsServer.State.OPEN)) {
                    String line2 = RealmsSettingsScreen.getLocalizedString("mco.configure.world.close.question.line1");
                    String line3 = RealmsSettingsScreen.getLocalizedString("mco.configure.world.close.question.line2");
                    Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 5));
                    break;
                }
                this.configureWorldScreen.openTheWorld(false, this);
                break;
            }
            default: {
                return;
            }
        }
    }

    @Override
    public void confirmResult(boolean result, int id) {
        switch (id) {
            case 5: {
                if (result) {
                    this.configureWorldScreen.closeTheWorld(this);
                    break;
                }
                Realms.setScreen(this);
            }
        }
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        this.nameEdit.keyPressed(ch, eventKey);
        this.descEdit.keyPressed(ch, eventKey);
        switch (eventKey) {
            case 15: {
                this.nameEdit.setFocus(!this.nameEdit.isFocused());
                this.descEdit.setFocus(!this.descEdit.isFocused());
                break;
            }
            case 28: 
            case 156: {
                this.save();
                break;
            }
            case 1: {
                Realms.setScreen(this.configureWorldScreen);
            }
            default: {
                return;
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        this.descEdit.mouseClicked(x, y, buttonNum);
        this.nameEdit.mouseClicked(x, y, buttonNum);
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        this.drawCenteredString(RealmsSettingsScreen.getLocalizedString("mco.configure.world.settings.title"), this.width() / 2, 17, 0xFFFFFF);
        this.drawString(RealmsSettingsScreen.getLocalizedString("mco.configure.world.name"), this.width() / 2 - 106, RealmsConstants.row(3), 0xA0A0A0);
        this.drawString(RealmsSettingsScreen.getLocalizedString("mco.configure.world.description"), this.width() / 2 - 106, RealmsConstants.row(7), 0xA0A0A0);
        this.nameEdit.render();
        this.descEdit.render();
        super.render(xm, ym, a);
    }

    public void save() {
        this.configureWorldScreen.saveSettings(this.nameEdit.getValue(), this.descEdit.getValue());
    }
}

