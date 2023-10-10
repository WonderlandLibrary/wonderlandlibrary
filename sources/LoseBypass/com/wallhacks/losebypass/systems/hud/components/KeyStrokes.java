/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.hud.components;

import com.wallhacks.losebypass.systems.hud.HudComponent;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;

@HudComponent.Registration(name="KeyStrokes", description="Shows your wasd keys")
public class KeyStrokes
extends HudComponent {
    BooleanSetting space = this.booleanSetting("Space", true);

    @Override
    public void drawComponent(boolean editor, float delta) {
        this.width = 12 * this.fontRenderer().getStringWidth("W");
        this.height = this.fontRenderer().getStringHeight() * 2;
    }

    private class Component {
        String name;
        int keycode;

        public Component(String name, int keycode) {
            this.name = name;
            this.keycode = keycode;
        }
    }
}

