package club.async.account;

import club.async.Async;
import club.async.util.ChatFormatting;
import club.async.util.ColorUtil;
import club.async.util.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;

public class GuiAccountLogin extends GuiScreen {

    private GuiScreen parent;
    private GuiPasswordField passwordField;
    private GuiTextField usernameField;
    private AccountThread accountThread = null;

    private String type = "mojang";

    public GuiAccountLogin(GuiScreen parent) {
        this.parent = parent;
//        RenderUtil.initShader();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                accountThread = new AccountThread(new Account(usernameField.getText(), passwordField.getText()), type);
                accountThread.start();
                break;
            case 1:
                String clipboard = getClipboardString().trim();
                if(clipboard.contains("\n")) clipboard = clipboard.replaceAll("\n", "");

                if(clipboard.contains(":")) {
                    String[] split = clipboard.split(":");
                    usernameField.setText(split[0]);
                    passwordField.setText(split[1]);
                } else {
                    usernameField.setText(clipboard);
                }
                break;
            case 2:
                mc.displayGuiScreen(parent);
                break;
            case 3:
                if(type.equalsIgnoreCase("mojang")) type = "microsoft";
                else type = "mojang";

                buttonList.get(2).displayString = "Type: " + type;

                break;
        }

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
//        Keyboard.enableRepeatEvents(true);

        if (typedChar == '\t') {
            if (!usernameField.isFocused() && !passwordField.isFocused()) {
                usernameField.setFocused(true);
            } else {
                usernameField.setFocused(passwordField.isFocused());
                usernameField.setFocused(!usernameField.isFocused());
            }
        }

        if(keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
        }

        usernameField.textboxKeyTyped(typedChar, keyCode);
        passwordField.textboxKeyTyped(typedChar, keyCode);

//        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {

        int j = this.height / 4 + 48;

        usernameField = new GuiTextField(2, fontRendererObj, this.width / 2 - 100, j, 200, 20);

        usernameField.setFocused(true);

        passwordField = new GuiPasswordField(1, fontRendererObj, this.width / 2 - 100, j + 24, 200, 20);

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 50, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, j + 74, "Import email:pass"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, j + 98, "Type: " + type));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, j + 122, "Cancel"));

        Keyboard.enableRepeatEvents(true);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.renderShader(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);

        String text = "A" + ChatFormatting.GRAY + "ccount Login";
        Gui.drawRect(width / 2 - Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 - 3, height / 4 - 10,width / 2 + Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 + 3, height / 4 + 11, new Color(40,40,40,150));
        Gui.drawRect(width / 2 - Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 - 4, height / 4 - 11,width / 2 + Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 + 4, height / 4 - 10, ColorUtil.getMainColor());
        Gui.drawRect(width / 2 - Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 - 4, height / 4 + 11,width / 2 + Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 + 4, height / 4 + 12, ColorUtil.getMainColor());
        Gui.drawRect(width / 2 - Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 - 4, height / 4 - 11,width / 2 - Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 - 3, height / 4 + 12, ColorUtil.getMainColor());
        Gui.drawRect(width / 2 + Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 + 3, height / 4 - 11,width / 2 + Async.INSTANCE.getFontManager().getFont("Arial 40").getWidth(text) / 2 + 4, height / 4 + 12, ColorUtil.getMainColor());
        Async.INSTANCE.getFontManager().getFont("Arial 37").drawCenteredString(text, width / 2, height / 4 - 10, ColorUtil.getMainColor());

        usernameField.drawTextBox();
        passwordField.drawTextBox();

        int j = this.height / 4 + 48;

        if (this.usernameField.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Email", this.width / 2 - 95, j + 5, -7829368);
        }
        if (this.passwordField.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 95, j + 29, -7829368);
        }

        String status = accountThread == null ? "Idle" : accountThread.getStatus();

        this.drawString(this.mc.fontRendererObj, status, this.width / 2 - (mc.fontRendererObj.getStringWidth(status) / 2), j - 20, -7829368);

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        passwordField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);

        super.onGuiClosed();
    }
}
