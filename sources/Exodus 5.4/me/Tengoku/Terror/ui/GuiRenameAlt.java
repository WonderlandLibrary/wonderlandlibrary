/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.ui;

import java.io.IOException;
import me.Tengoku.Terror.ui.GuiAltManager;
import me.Tengoku.Terror.ui.PasswordField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public class GuiRenameAlt
extends GuiScreen {
    private String status = (Object)((Object)EnumChatFormatting.GRAY) + "Waiting...";
    private GuiTextField nameField;
    private final GuiAltManager manager;
    private PasswordField pwField;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Edit Alt", width / 2, 10, -1);
        this.drawCenteredString(this.fontRendererObj, this.status, width / 2, 20, -1);
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        if (this.nameField.getText().isEmpty()) {
            this.drawString(Minecraft.fontRendererObj, "New name", width / 2 - 96, 66, -7829368);
        }
        if (this.pwField.getText().isEmpty()) {
            this.drawString(Minecraft.fontRendererObj, "New password", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(n, n2, f);
    }

    public GuiRenameAlt(GuiAltManager guiAltManager) {
        this.manager = guiAltManager;
    }

    @Override
    public void actionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 0: {
                this.manager.selectedAlt.setMask(this.nameField.getText());
                this.manager.selectedAlt.setPassword(this.pwField.getText());
                this.status = "Edited!";
            }
        }
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) {
        try {
            super.mouseClicked(n, n2, n3);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.nameField.mouseClicked(n, n2, n3);
        this.pwField.mouseClicked(n, n2, n3);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Edit"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
        this.nameField = new GuiTextField(this.eventButton, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.pwField = new PasswordField(Minecraft.fontRendererObj, width / 2 - 100, 100, 200, 20);
    }

    @Override
    protected void keyTyped(char c, int n) {
        this.nameField.textboxKeyTyped(c, n);
        this.pwField.textboxKeyTyped(c, n);
        if (c == '\t' && (this.nameField.isFocused() || this.pwField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
            this.pwField.setFocused(!this.pwField.isFocused());
        }
        if (c == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }
}

