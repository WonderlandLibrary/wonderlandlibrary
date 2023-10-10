/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.components.settings;

import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.utils.Timer;

public class SettingComponent {
    Setting<?> setting;
    Timer timer = new Timer();

    public SettingComponent(Setting<?> s) {
        this.setting = s;
    }

    public int drawComponent(int posX, int posY, double deltaTime, int click, int mouseX, int mouseY, boolean animating) {
        if (!animating && this.setting != null && this.setting.getDescription() != null && !this.setting.getDescription().equals("") && mouseX > posX && mouseX < posX + 200 && mouseY > posY && mouseY < posY + this.getHeight()) {
            if (!this.timer.passedMs(700L)) return this.drawComponent(posX, posY, deltaTime, click, mouseX, mouseY);
            ClickGui.info.setInfo(this.setting.getDescription(), this.setting.getName());
            return this.drawComponent(posX, posY, deltaTime, click, mouseX, mouseY);
        }
        this.timer.reset();
        return this.drawComponent(posX, posY, deltaTime, click, mouseX, mouseY);
    }

    public int drawComponent(int posX, int posY, double deltaTime, int click, int mouseX, int mouseY) {
        return 30;
    }

    public int getHeight() {
        return 30;
    }

    public boolean visible() {
        return true;
    }

    public void keyTyped(char charTyped, int keyCode) {
    }

    public Setting<?> getSetting() {
        return this.setting;
    }
}

