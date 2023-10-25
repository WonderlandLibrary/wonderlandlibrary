/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.Tengoku.Terror.ui.GuiAddAlt$AddAltThread
 *  org.lwjgl.input.Keyboard
 */
package me.Tengoku.Terror.ui;

import java.io.IOException;
import me.Tengoku.Terror.ui.GuiAddAlt;
import me.Tengoku.Terror.ui.GuiAltManager;
import me.Tengoku.Terror.ui.PasswordField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class GuiAddAlt
extends GuiScreen {
    private GuiTextField username;
    private String status = (Object)((Object)EnumChatFormatting.GRAY) + "Idle...";
    private PasswordField password;
    private final GuiAltManager manager;

    static void access$0(GuiAddAlt guiAddAlt, String string) {
        guiAddAlt.status = string;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case 0: {
                AddAltThread addAltThread = new AddAltThread(this, this.username.getText(), this.password.getText());
                addAltThread.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.manager);
            }
        }
    }

    @Override
    protected void keyTyped(char c, int n) {
        this.username.textboxKeyTyped(c, n);
        this.password.textboxKeyTyped(c, n);
        if (c == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (c == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
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
        this.username.mouseClicked(n, n2, n3);
        this.password.mouseClicked(n, n2, n3);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Back"));
        this.username = new GuiTextField(this.eventButton, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(Minecraft.fontRendererObj, width / 2 - 100, 100, 200, 20);
    }

    public GuiAddAlt(GuiAltManager guiAltManager) {
        this.manager = guiAltManager;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(this.fontRendererObj, "Adding alts is disabled for now.", width / 2, 20, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(Minecraft.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(Minecraft.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
        }
        this.drawCenteredString(this.fontRendererObj, this.status, width / 2, 30, -1);
        super.drawScreen(n, n2, f);
    }
}

