/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.IChatComponent;

public class DisconnectedRealmsScreen
extends RealmsScreen {
    private List<String> lines;
    private IChatComponent reason;
    private int textHeight;
    private final RealmsScreen parent;
    private String title;

    @Override
    public void init() {
        Realms.setConnectedToRealms(false);
        this.buttonsClear();
        this.lines = this.fontSplit(this.reason.getFormattedText(), this.width() - 50);
        this.textHeight = this.lines.size() * this.fontLineHeight();
        this.buttonsAdd(DisconnectedRealmsScreen.newButton(0, this.width() / 2 - 100, this.height() / 2 + this.textHeight / 2 + this.fontLineHeight(), DisconnectedRealmsScreen.getLocalizedString("gui.back")));
    }

    public DisconnectedRealmsScreen(RealmsScreen realmsScreen, String string, IChatComponent iChatComponent) {
        this.parent = realmsScreen;
        this.title = DisconnectedRealmsScreen.getLocalizedString(string);
        this.reason = iChatComponent;
    }

    @Override
    public void keyPressed(char c, int n) {
        if (n == 1) {
            Realms.setScreen(this.parent);
        }
    }

    @Override
    public void buttonClicked(RealmsButton realmsButton) {
        if (realmsButton.id() == 0) {
            Realms.setScreen(this.parent);
        }
    }

    @Override
    public void render(int n, int n2, float f) {
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, this.height() / 2 - this.textHeight / 2 - this.fontLineHeight() * 2, 0xAAAAAA);
        int n3 = this.height() / 2 - this.textHeight / 2;
        if (this.lines != null) {
            for (String string : this.lines) {
                this.drawCenteredString(string, this.width() / 2, n3, 0xFFFFFF);
                n3 += this.fontLineHeight();
            }
        }
        super.render(n, n2, f);
    }
}

