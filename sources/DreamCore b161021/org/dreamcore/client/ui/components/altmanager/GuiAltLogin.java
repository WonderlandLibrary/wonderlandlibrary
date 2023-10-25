package org.dreamcore.client.ui.components.altmanager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.ui.button.GuiAltButton;
import org.dreamcore.client.ui.components.altmanager.alt.Alt;
import org.dreamcore.client.ui.components.altmanager.alt.AltLoginThread;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

public final class GuiAltLogin extends GuiScreen {
    private final GuiScreen previousScreen;
    private PasswordField password;
    private AltLoginThread thread;
    private GuiTextField username;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    protected void actionPerformed(GuiButton button) {
        try {
            switch (button.id) {
                case 0:
                    (this.thread = new AltLoginThread(new Alt(this.username.getText(), this.password.getText()))).start();
                    break;
                case 1:
                    this.mc.displayGuiScreen(this.previousScreen);
                    break;
                case 2:
                    String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (data.contains(":")) {
                        String[] credentials = data.split(":");
                        this.username.setText(credentials[0]);
                        this.password.setText(credentials[1]);
                    }
            }

        } catch (Throwable e) {
            throw new RuntimeException();
        }
    }

    public void drawScreen(int x, int y, float z) {
        RectHelper.drawBorderedRect(0, 0, width, height, 0.5F, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
        RenderHelper.drawImage(new ResourceLocation("dreamcore/skeet.png"), 1, 1, 958, 1, Color.white);
        this.username.drawTextBox();
        this.password.drawTextBox();
        mc.circleregular.drawStringWithShadow("Alt Login", width / 2F, 20, -1);
        mc.circleregular.drawStringWithShadow(this.thread == null ? TextFormatting.GRAY + "Alts..." : this.thread.getStatus(), width / 2F, 29, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            mc.circleregular.drawStringWithShadow("Username / E-Mail", width / 2 - 96, 66, -7829368);
        }

        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            mc.circleregular.drawStringWithShadow("Password", width / 2 - 96, 106, -7829368);
        }

        super.drawScreen(x, y, z);
    }

    public void initGui() {
        int height1 = height / 4 + 24;
        this.buttonList.add(new GuiAltButton(0, width / 2 - 100, height1 + 72 + 12, "Login"));
        this.buttonList.add(new GuiAltButton(1, width / 2 - 100, height1 + 72 + 12 + 24, "Back"));
        this.buttonList.add(new GuiAltButton(2, width / 2 - 100, height1 + 72 + 12 - 24, "Import User:Pass"));
        this.username = new GuiTextField(height1, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }

        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }

        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }

}