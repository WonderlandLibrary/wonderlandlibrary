/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.util.RealmsUtil;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import org.lwjgl.opengl.GL11;

public class RealmsParentalConsentScreen
extends RealmsScreen {
    private final RealmsScreen nextScreen;
    private static final int BUTTON_BACK_ID = 0;
    private static final int BUTTON_OK_ID = 1;
    private boolean onLink;

    public RealmsParentalConsentScreen(RealmsScreen nextScreen) {
        this.nextScreen = nextScreen;
    }

    @Override
    public void init() {
        this.buttonsClear();
        String updateAccount = RealmsParentalConsentScreen.getLocalizedString("mco.account.update");
        String back = RealmsParentalConsentScreen.getLocalizedString("gui.back");
        int buttonWidth = Math.max(this.fontWidth(updateAccount), this.fontWidth(back)) + 30;
        this.buttonsAdd(new RealmsButton(0, this.width() / 2 - (buttonWidth + 5), RealmsConstants.row(13), buttonWidth, 20, back));
        this.buttonsAdd(new RealmsButton(1, this.width() / 2 + 5, RealmsConstants.row(13), buttonWidth, 20, updateAccount));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        switch (button.id()) {
            case 1: {
                RealmsUtil.browseTo("https://minecraft.net/update-account");
                break;
            }
            case 0: {
                Realms.setScreen(this.nextScreen);
                break;
            }
            default: {
                return;
            }
        }
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        String translatedText = RealmsParentalConsentScreen.getLocalizedString("mco.account.privacyinfo");
        int y = 15;
        for (String lineBrokenOnExplicits : translatedText.split("\\\\n")) {
            this.drawCenteredString(lineBrokenOnExplicits, this.width() / 2, y, 0xFFFFFF);
            y += 15;
        }
        this.renderLink(xm, ym, y);
        super.render(xm, ym, a);
    }

    @Override
    public void mouseClicked(int x, int y, int buttonNum) {
        if (this.onLink) {
            RealmsUtil.browseTo("https://minecraft.net/privacy/gdpr/");
        }
    }

    private void renderLink(int xm, int ym, int top) {
        int leftPadding;
        String text = RealmsParentalConsentScreen.getLocalizedString("mco.account.privacy.info");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        int textWidth = this.fontWidth(text);
        int x1 = leftPadding = this.width() / 2 - textWidth / 2;
        int x2 = x1 + textWidth + 1;
        int y1 = top;
        int y2 = y1 + this.fontLineHeight();
        GL11.glTranslatef(x1, y1, 0.0f);
        if (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2) {
            this.onLink = true;
            this.drawString(text, 0, 0, 7107012);
        } else {
            this.onLink = false;
            this.drawString(text, 0, 0, 0x3366BB);
        }
        GL11.glPopMatrix();
    }
}

