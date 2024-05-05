package rip.helium.gui.screen.account;

import com.thealtening.AltService;
import com.thealtening.api.TheAltening;
import com.thealtening.api.data.AccountData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.apache.commons.io.FileUtils;
import rip.helium.Helium;
import rip.helium.account.Account;
import rip.helium.gui.components.Dropbox;
import rip.helium.gui.components.FieldType;
import rip.helium.gui.components.TextButton;
import rip.helium.gui.components.TextField;
import rip.helium.utils.Draw;
import rip.helium.utils.font.Fonts;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;

public class DirectLoginGui extends GuiScreen {
    private String token;
    private String lastalt;
    private final GuiScreen parentScreen;
    private TextButton b1;
    //private TextButton generatebutton;
    //private TextButton altButton;
    //private TextButton setKeybutton;
    //private TextField apiKeyFeild;
    private TextField tokenField;
    private TextField usernameField;
    private TextField passwordField;
    private Dropbox serviceDropbox;

    public DirectLoginGui(final GuiScreen parentScreen) {
        this.token = null;
        this.parentScreen = parentScreen;
    }

    @Override
    public void updateScreen() {
        this.parentScreen.updateScreen();
        //Fonts.f16.drawCenteredString(Helium.instance.accountLoginService.getStatus(), (float)(this.width / 2), (float)(this.height - 12), -1);
        if (this.serviceDropbox.getSelected().equalsIgnoreCase("mojang")) {
            this.b1.enabled = !this.usernameField.getTypedContent().isEmpty();
            this.usernameField.updateTextField();
            this.passwordField.updateTextField();
            this.usernameField.setHidden(false);
            this.passwordField.setHidden(false);
            this.tokenField.setHidden(true);
            //this.apiKeyFeild.setHidden(true);
            //this.setKeybutton.visible = false;
        } else if (this.serviceDropbox.getSelected().equalsIgnoreCase("altening")) {
            this.b1.enabled = !this.tokenField.getTypedContent().isEmpty();
            //this.apiKeyFeild.updateTextField();
            this.tokenField.updateTextField();
            this.tokenField.setHidden(false);
            //this.apiKeyFeild.setHidden(this.token != null);
            this.usernameField.setHidden(true);
            this.passwordField.setHidden(true);
            //this.setKeybutton.enabled = (this.apiKeyFeild.isFocused() || this.token == null);
            //this.setKeybutton.visible = (this.token == null);
        }
        //this.generatebutton.visible = this.serviceDropbox.getSelected().equalsIgnoreCase("altening");
    }

    @Override
    public void initGui() {
        this.usernameField = new TextField(Fonts.f16, width / 2 - 75, height / 2 - 42, 150, 20);
        this.passwordField = new TextField(Fonts.f16, width / 2 - 75, height / 2 - 20, 150, 20);
        this.tokenField = new TextField(Fonts.f16, width / 2 - 75, height / 2 - 20, 150, 20);
        //this.apiKeyFeild = new TextField(Fonts.f16, this.width / 2 - 81, this.height / 2 + 96, 150, 20);
        this.usernameField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.usernameField.setDefaultContent("Username/Email");
        this.passwordField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.passwordField.setDefaultContent("Password");
        this.passwordField.setReplaceAll("-");
        this.tokenField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.tokenField.setDefaultContent("Token");
        //this.apiKeyFeild.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        //this.apiKeyFeild.setDefaultContent("Put your altening API here!");
        this.serviceDropbox = new Dropbox(width / 2 - 40, height / 2 + 7, 80, 15, Fonts.f16, 0, "Mojang", "Altening");
        this.buttonList.add(this.b1 = new TextButton(1, width / 2 - 81, height / 2 + 40, 80, 20, "Login"));
        this.buttonList.add(new TextButton(2, width / 2 + 1, height / 2 + 40, 80, 20, "Clipboard"));
        //this.buttonList.add(this.generatebutton = new TextButton(3, this.width / 2 - 40, this.height / 2 + 46, 80, 20, "Generate"));
        //this.buttonList.add(this.altButton = new TextButton(4, this.width / 2 - 40, this.height / 2 + 46 + 22, 80, 20, "MCAlts.store"));
        //this.buttonList.add(this.setKeybutton = new TextButton(9, this.width / 2 - 81, this.height / 2 + 126, 80, 20, "Set API key"));
        super.initGui();
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                this.loginAccount();
                break;
            }
            case 2: {
                if (this.serviceDropbox.getSelected().equalsIgnoreCase("mojang")) {
                    String data;
                    try {
                        data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    } catch (Exception var6) {
                        return;
                    }
                    if (data.contains(":")) {
                        final String[] thepassword = data.split(":");
                        this.usernameField.setTypedContent(thepassword[0]);
                        this.passwordField.setTypedContent(thepassword[1]);
                        break;
                    }
                    break;
                }
                /*/else {
                    if (this.serviceDropbox.getSelected().equalsIgnoreCase("altening")) {
                        String data;
                        try {
                            data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                        }
                        catch (Exception var7) {
                            return;
                        }
                        this.tokenField.setTypedContent(data);
                        break;
                    }
                    break;
                }/*/

            }
            case 3: {
                try {
                    final File alteningFile = new File(Helium.clientDir + File.separator + "altening_token.txt");
                    if (alteningFile.exists()) {
                        final String token = FileUtils.readFileToString(alteningFile);
                        System.out.println(token);
                        if (!token.isEmpty()) {
                            this.token = token;
                            final AccountData accountData = new TheAltening(token).getAccountData();
                            if (accountData != null && !accountData.getToken().isEmpty()) {
                                this.tokenField.setTypedContent(this.lastalt = accountData.getToken());
                                System.out.println(accountData.getToken());
                            }
                        } else if (!token.isEmpty()) {
                            final AccountData accountData = new TheAltening(this.token).getAccountData();
                            if (accountData != null && !accountData.getToken().isEmpty()) {
                                this.tokenField.setTypedContent(this.lastalt = accountData.getToken());
                                System.out.println(accountData.getToken());
                            }
                        }
                    } else if (!this.token.isEmpty()) {
                        final AccountData accountData2 = new TheAltening(this.token).getAccountData();
                        if (accountData2 != null && !accountData2.getToken().isEmpty()) {
                            this.tokenField.setTypedContent(this.lastalt = accountData2.getToken());
                            System.out.println(accountData2.getToken());
                        }
                    }
                } catch (Exception e) {

                }
                break;
            }
            case 4: {
                try {
                    if (Util.getOSType() != Util.EnumOS.OSX && Util.getOSType() != Util.EnumOS.LINUX) {
                        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome https://mcalts.store/"});
                    } else {
                        Runtime.getRuntime().exec(new String[]{"chromium-browser", "https://mcalts.store/"});
                    }
                } catch (IOException ex) {
                }
            }
            case 9: {
                //this.token = this.apiKeyFeild.getTypedContent();
                break;
            }
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
      //  Gui.drawRect(0, 0, width, height, new Color(0, 0, 0).getRGB());
        drawGradientRect(0, 0, width, height, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0).getRGB());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Draw.drawImg(new ResourceLocation("client/Background.jpg"), 0.0, 0.0, width, height);
        this.usernameField.drawTextField();
        this.passwordField.drawTextField();
        this.tokenField.drawTextField();
        //this.apiKeyFeild.drawTextField();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.serviceDropbox.drawDropbox(mouseX, mouseY);
        Fonts.f16.drawCenteredString(Helium.instance.accountLoginService.getStatus(), (float) (width / 2), (float) (height - 12), -1);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 28) {
            this.loginAccount();
        }
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
        }
        if (this.serviceDropbox.getSelected().equalsIgnoreCase("mojang")) {
            if (keyCode == 15) {
                if (this.usernameField.isFocused()) {
                    this.usernameField.setFocused(false);
                    this.passwordField.setFocused(true);
                } else if (this.passwordField.isFocused()) {
                    this.passwordField.setFocused(false);
                    this.usernameField.setFocused(true);
                }
            }
            this.usernameField.keyTyped(typedChar, keyCode);
            this.passwordField.keyTyped(typedChar, keyCode);
        } else if (this.serviceDropbox.getSelected().equalsIgnoreCase("altening")) {
            this.tokenField.keyTyped(typedChar, keyCode);
            //this.apiKeyFeild.keyTyped(typedChar, keyCode);
            /*/if (this.apiKeyFeild.isFocused()) {
                this.apiKeyFeild.setFocused(true);
                this.passwordField.setFocused(false);
                this.usernameField.setFocused(false);
            }*/
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.serviceDropbox.getSelected().equalsIgnoreCase("mojang")) {
            this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
            this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        } else if (this.serviceDropbox.getSelected().equalsIgnoreCase("altening")) {
            this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
            //this.apiKeyFeild.mouseClicked(mouseX, mouseY, mouseButton);
        }
        final String value = this.serviceDropbox.getSelected();
        this.serviceDropbox.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.serviceDropbox.getSelected().equals(value)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void loginAccount() {
        if (this.serviceDropbox.getSelected().equals("Mojang")) {
            final AltService.EnumAltService service = AltService.EnumAltService.MOJANG;
            Helium.instance.accountLoginService.attemptLogin(new Account(this.usernameField.getTypedContent(), this.passwordField.getTypedContent(), service));
        } else {
            final AltService.EnumAltService service = AltService.EnumAltService.THEALTENING;
            Helium.instance.accountLoginService.attemptLogin(new Account(this.tokenField.getTypedContent(), "bruhLOOl", service));
        }
    }
}
