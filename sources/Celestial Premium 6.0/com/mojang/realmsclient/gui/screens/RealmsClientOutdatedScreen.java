/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsConstants;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class RealmsClientOutdatedScreen
extends RealmsScreen {
    private static final int BUTTON_BACK_ID = 0;
    private final RealmsScreen lastScreen;
    private final boolean outdated;

    public RealmsClientOutdatedScreen(RealmsScreen lastScreen, boolean outdated) {
        this.lastScreen = lastScreen;
        this.outdated = outdated;
    }

    @Override
    public void init() {
        this.buttonsClear();
        this.buttonsAdd(RealmsClientOutdatedScreen.newButton(0, this.width() / 2 - 100, RealmsConstants.row(12), "Back"));
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        String title = RealmsClientOutdatedScreen.getLocalizedString(this.outdated ? "mco.client.outdated.title" : "mco.client.incompatible.title");
        this.drawCenteredString(title, this.width() / 2, RealmsConstants.row(3), 0xFF0000);
        int lines = this.outdated ? 2 : 3;
        for (int i = 0; i < lines; ++i) {
            String message = RealmsClientOutdatedScreen.getLocalizedString((this.outdated ? "mco.client.outdated.msg.line" : "mco.client.incompatible.msg.line") + (i + 1));
            this.drawCenteredString(message, this.width() / 2, RealmsConstants.row(5) + i * 12, 0xFFFFFF);
        }
        super.render(xm, ym, a);
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (button.id() == 0) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey) {
        if (eventKey == 28 || eventKey == 156 || eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
}

