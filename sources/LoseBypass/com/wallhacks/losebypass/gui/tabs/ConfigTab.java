/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.tabs;

import com.wallhacks.losebypass.gui.tabs.ClickGuiTab;
import net.minecraft.util.ResourceLocation;

public class ConfigTab
extends ClickGuiTab {
    public ConfigTab() {
        super("Configs", new ResourceLocation("textures/icons/config.png"));
    }

    @Override
    public void drawTab(int mouseX, int mouseY, int click, int posX, int posY, double deltaTime) {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }
}

