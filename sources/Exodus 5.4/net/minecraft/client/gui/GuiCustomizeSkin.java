/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class GuiCustomizeSkin
extends GuiScreen {
    private String title;
    private final GuiScreen parentScreen;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, width / 2, 20, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    @Override
    public void initGui() {
        int n = 0;
        this.title = I18n.format("options.skinCustomisation.title", new Object[0]);
        EnumPlayerModelParts[] enumPlayerModelPartsArray = EnumPlayerModelParts.values();
        int n2 = enumPlayerModelPartsArray.length;
        int n3 = 0;
        while (n3 < n2) {
            EnumPlayerModelParts enumPlayerModelParts = enumPlayerModelPartsArray[n3];
            this.buttonList.add(new ButtonPart(enumPlayerModelParts.getPartId(), width / 2 - 155 + n % 2 * 160, height / 6 + 24 * (n >> 1), 150, 20, enumPlayerModelParts));
            ++n;
            ++n3;
        }
        if (n % 2 == 1) {
            ++n;
        }
        this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 24 * (n >> 1), I18n.format("gui.done", new Object[0])));
    }

    public GuiCustomizeSkin(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 200) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (guiButton instanceof ButtonPart) {
                EnumPlayerModelParts enumPlayerModelParts = ((ButtonPart)guiButton).playerModelParts;
                Minecraft.gameSettings.switchModelPartEnabled(enumPlayerModelParts);
                guiButton.displayString = this.func_175358_a(enumPlayerModelParts);
            }
        }
    }

    private String func_175358_a(EnumPlayerModelParts enumPlayerModelParts) {
        String string = Minecraft.gameSettings.getModelParts().contains((Object)enumPlayerModelParts) ? I18n.format("options.on", new Object[0]) : I18n.format("options.off", new Object[0]);
        return String.valueOf(enumPlayerModelParts.func_179326_d().getFormattedText()) + ": " + string;
    }

    class ButtonPart
    extends GuiButton {
        private final EnumPlayerModelParts playerModelParts;

        private ButtonPart(int n, int n2, int n3, int n4, int n5, EnumPlayerModelParts enumPlayerModelParts) {
            super(n, n2, n3, n4, n5, GuiCustomizeSkin.this.func_175358_a(enumPlayerModelParts));
            this.playerModelParts = enumPlayerModelParts;
        }
    }
}

