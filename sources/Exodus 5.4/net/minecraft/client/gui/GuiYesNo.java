/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;

public class GuiYesNo
extends GuiScreen {
    protected GuiYesNoCallback parentScreen;
    protected int parentButtonClickedId;
    private int ticksUntilEnable;
    protected String messageLine1;
    protected String cancelButtonText;
    private String messageLine2;
    private final List<String> field_175298_s = Lists.newArrayList();
    protected String confirmButtonText;

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        this.parentScreen.confirmClicked(guiButton.id == 0, this.parentButtonClickedId);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (--this.ticksUntilEnable == 0) {
            for (GuiButton guiButton : this.buttonList) {
                guiButton.enabled = true;
            }
        }
    }

    public GuiYesNo(GuiYesNoCallback guiYesNoCallback, String string, String string2, int n) {
        this.parentScreen = guiYesNoCallback;
        this.messageLine1 = string;
        this.messageLine2 = string2;
        this.parentButtonClickedId = n;
        this.confirmButtonText = I18n.format("gui.yes", new Object[0]);
        this.cancelButtonText = I18n.format("gui.no", new Object[0]);
    }

    public void setButtonDelay(int n) {
        this.ticksUntilEnable = n;
        for (GuiButton guiButton : this.buttonList) {
            guiButton.enabled = false;
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.messageLine1, width / 2, 70, 0xFFFFFF);
        int n3 = 90;
        for (String string : this.field_175298_s) {
            this.drawCenteredString(this.fontRendererObj, string, width / 2, n3, 0xFFFFFF);
            n3 += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(n, n2, f);
    }

    public GuiYesNo(GuiYesNoCallback guiYesNoCallback, String string, String string2, String string3, String string4, int n) {
        this.parentScreen = guiYesNoCallback;
        this.messageLine1 = string;
        this.messageLine2 = string2;
        this.confirmButtonText = string3;
        this.cancelButtonText = string4;
        this.parentButtonClickedId = n;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(0, width / 2 - 155, height / 6 + 96, this.confirmButtonText));
        this.buttonList.add(new GuiOptionButton(1, width / 2 - 155 + 160, height / 6 + 96, this.cancelButtonText));
        this.field_175298_s.clear();
        this.field_175298_s.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, width - 50));
    }
}

