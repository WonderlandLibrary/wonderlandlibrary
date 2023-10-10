/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.hud.components;

import com.wallhacks.losebypass.systems.hud.HudComponent;
import net.minecraft.client.Minecraft;

@HudComponent.Registration(name="FPSComponent", description="Shows you your current fps")
public class FPSCounter
extends HudComponent {
    @Override
    public void drawComponent(boolean editor, float delta) {
        String fps = "FPS " + Minecraft.getDebugFPS();
        this.width = 6 * this.fontRenderer().getStringWidth("W");
        this.height = this.fontRenderer().getStringHeight() * 2;
        if (!editor) {
            this.drawBackground();
        }
        this.drawStringCentered(fps);
    }
}

