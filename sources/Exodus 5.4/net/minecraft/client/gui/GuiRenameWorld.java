/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.input.Keyboard;

public class GuiRenameWorld
extends GuiScreen {
    private GuiTextField field_146583_f;
    private final String saveName;
    private GuiScreen parentScreen;

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("selectWorld.renameButton", new Object[0])));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
        ISaveFormat iSaveFormat = this.mc.getSaveLoader();
        WorldInfo worldInfo = iSaveFormat.getWorldInfo(this.saveName);
        String string = worldInfo.getWorldName();
        this.field_146583_f = new GuiTextField(2, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.field_146583_f.setFocused(true);
        this.field_146583_f.setText(string);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.renameTitle", new Object[0]), width / 2, 20, 0xFFFFFF);
        this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), width / 2 - 100, 47, 0xA0A0A0);
        this.field_146583_f.drawTextBox();
        super.drawScreen(n, n2, f);
    }

    @Override
    public void updateScreen() {
        this.field_146583_f.updateCursorCounter();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 1) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (guiButton.id == 0) {
                ISaveFormat iSaveFormat = this.mc.getSaveLoader();
                iSaveFormat.renameWorld(this.saveName, this.field_146583_f.getText().trim());
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        this.field_146583_f.mouseClicked(n, n2, n3);
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        this.field_146583_f.textboxKeyTyped(c, n);
        boolean bl = ((GuiButton)this.buttonList.get((int)0)).enabled = this.field_146583_f.getText().trim().length() > 0;
        if (n == 28 || n == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    public GuiRenameWorld(GuiScreen guiScreen, String string) {
        this.parentScreen = guiScreen;
        this.saveName = string;
    }
}

