/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.hud.components;

import com.wallhacks.losebypass.systems.hud.HudComponent;

@HudComponent.Registration(name="PingCount", description="Shows you your ping")
public class PingCount
extends HudComponent {
    @Override
    public void drawComponent(boolean editor, float delta) {
        String ping = "Ping " + (mc.getCurrentServerData() != null ? PingCount.mc.getCurrentServerData().pingToServer : -1L) + "ms";
        this.width = 6 * this.fontRenderer().getStringWidth("W");
        this.height = this.fontRenderer().getStringHeight() * 2;
        if (!editor) {
            this.drawBackground();
        }
        this.drawStringCentered(ping);
    }
}

