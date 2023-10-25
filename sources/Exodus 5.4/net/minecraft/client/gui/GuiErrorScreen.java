/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiErrorScreen
extends GuiScreen {
    private String field_146312_f;
    private String field_146313_a;

    @Override
    public void drawScreen(int n, int n2, float f) {
        GuiErrorScreen.drawGradientRect(0.0, 0.0, width, height, -12574688, -11530224);
        this.drawCenteredString(this.fontRendererObj, this.field_146313_a, width / 2, 90, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, this.field_146312_f, width / 2, 110, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
    }

    public GuiErrorScreen(String string, String string2) {
        this.field_146313_a = string;
        this.field_146312_f = string2;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        this.mc.displayGuiScreen(null);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, width / 2 - 100, 140, I18n.format("gui.cancel", new Object[0])));
    }
}

