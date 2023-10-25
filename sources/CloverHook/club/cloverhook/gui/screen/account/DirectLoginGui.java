package club.cloverhook.gui.screen.account;

import club.cloverhook.gui.components.TextField;
import club.cloverhook.gui.screen.MainMenuGui;
import com.thealtening.AltService;

import club.cloverhook.Cloverhook;
import club.cloverhook.account.Account;
import club.cloverhook.gui.components.*;
import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class DirectLoginGui extends GuiScreen {
    private GuiScreen parentScreen;

    private GuiButton b1;

    private TextField tokenField;
    private TextField usernameField;
    private TextField passwordField;

    private Dropbox serviceDropbox;

    public DirectLoginGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    public void updateScreen() {
        parentScreen.updateScreen();

        if (serviceDropbox.getSelected().equalsIgnoreCase("mojang")) {
            b1.enabled = !usernameField.getTypedContent().isEmpty();
            usernameField.updateTextField();
            passwordField.updateTextField();
            usernameField.setHidden(false);
            passwordField.setHidden(false);
            tokenField.setHidden(true);
        } else if (serviceDropbox.getSelected().equalsIgnoreCase("altening")) {
            b1.enabled = !tokenField.getTypedContent().isEmpty();
            tokenField.updateTextField();
            tokenField.setHidden(false);
            usernameField.setHidden(true);
            passwordField.setHidden(true);
        }

    }

    public void initGui() {

        this.usernameField = new TextField(Fonts.f16,
                this.width / 2 - 75, this.height / 2 - 42,
                150, 20);

        this.passwordField = new TextField(Fonts.f16,
                this.width / 2 - 75, this.height / 2 - 20,
                150, 20);

        this.tokenField = new TextField(Fonts.f16,
                this.width / 2 - 75, this.height / 2 - 20,
                150, 20);

        this.usernameField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.usernameField.setDefaultContent("Username/Email");

        this.passwordField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.passwordField.setDefaultContent("Password");
        this.passwordField.setReplaceAll("-");

        this.tokenField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.tokenField.setDefaultContent("Token");

        this.buttonList.add(b1 = new GuiButton(1, this.width / 2 - 40, this.height / 2 + 7, 80, 20, "Login"));
        serviceDropbox =
                new Dropbox(this.width / 2 - 40, this.height / 2 + 29, 80, 15, Fonts.f16, 0, "Mojang", "Altening");

        this.buttonList.add(new GuiButton(6, this.width / 2 - 40, this.height - 24, 80, 20, "Back"));

        super.initGui();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                loginAccount();
                break;
            case 6:
                Minecraft.getMinecraft().displayGuiScreen(parentScreen);
                break;
        }
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        MainMenuGui.drawBackground();

        drawGradientRect(0, 0, width, height, -1, new Color(80, 80, 80, 120).getRGB());

        usernameField.drawTextField();
        passwordField.drawTextField();
        tokenField.drawTextField();

        serviceDropbox.drawDropbox(mouseX, mouseY);
        //Fonts.f16.drawCenteredString(Cloverhook.instance.accountLoginService.getStatus(), this.width / 2, this.height - 12, 0xffffffff);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_RETURN) {
            loginAccount();
            Minecraft.getMinecraft().displayGuiScreen(parentScreen);
        }

        if (serviceDropbox.getSelected().equalsIgnoreCase("mojang")) {
            if (keyCode == Keyboard.KEY_TAB) {
                if (usernameField.isFocused()) {
                    usernameField.setFocused(false);
                    passwordField.setFocused(true);
                } else if (passwordField.isFocused()) {
                    passwordField.setFocused(false);
                    usernameField.setFocused(true);
                }
            }

            usernameField.keyTyped(typedChar, keyCode);
            passwordField.keyTyped(typedChar, keyCode);
        } else if (serviceDropbox.getSelected().equalsIgnoreCase("altening")) {
            tokenField.keyTyped(typedChar, keyCode);
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (serviceDropbox.getSelected().equalsIgnoreCase("mojang")) {
            usernameField.mouseClicked(mouseX, mouseY, mouseButton);
            passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        } else if (serviceDropbox.getSelected().equalsIgnoreCase("altening")) {
            tokenField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        serviceDropbox.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void loginAccount() {
        AltService.EnumAltService service;

        if (serviceDropbox.getSelected().equals("Mojang")) {
            service = AltService.EnumAltService.MOJANG;

            Cloverhook.instance.accountLoginService.attemptLogin(
                    new Account(usernameField.getTypedContent(), passwordField.getTypedContent(), service));
        } else {
            service = AltService.EnumAltService.THEALTENING;

            Cloverhook.instance.accountLoginService.attemptLogin(
                    new Account(tokenField.getTypedContent(), "bruhl0l", service));
        }
    }
}
