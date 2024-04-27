/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  com.thealtening.AltService$EnumAltService
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.Proxy;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.TabUtils;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.input.Keyboard;

public class GuiAdd
extends WrappedGuiScreen {
    private IGuiButton clipboardButton;
    private IGuiTextField password;
    private final GuiAltManager prevGui;
    private String status = "\u00a77Idle...";
    private IGuiButton addButton;
    private IGuiTextField username;

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    public void actionPerformed(IGuiButton iGuiButton) throws IOException {
        if (!iGuiButton.getEnabled()) {
            return;
        }
        switch (iGuiButton.getId()) {
            case 0: {
                mc.displayGuiScreen(this.prevGui.representedScreen);
                break;
            }
            case 1: {
                if (LiquidBounce.fileManager.accountsConfig.accountExists(this.username.getText())) {
                    this.status = "\u00a7cThe account has already been added.";
                    break;
                }
                this.addAccount(this.username.getText(), this.password.getText());
                break;
            }
            case 2: {
                try {
                    String string = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    String[] stringArray = string.split(":", 2);
                    if (!string.contains(":") || stringArray.length != 2) {
                        this.status = "\u00a7cInvalid clipboard data. (Use: E-Mail:Password)";
                        return;
                    }
                    this.addAccount(stringArray[0], stringArray[1]);
                    break;
                }
                catch (UnsupportedFlavorException unsupportedFlavorException) {
                    this.status = "\u00a7cClipboard flavor unsupported!";
                    ClientUtils.getLogger().error("Failed to read data from clipboard.", (Throwable)unsupportedFlavorException);
                }
            }
        }
    }

    @Override
    public void keyTyped(char c, int n) throws IOException {
        switch (n) {
            case 1: {
                mc.displayGuiScreen(this.prevGui.representedScreen);
                return;
            }
            case 15: {
                TabUtils.tab(this.username, this.password);
                return;
            }
            case 28: {
                this.actionPerformed(this.addButton);
                return;
            }
        }
        if (this.username.isFocused()) {
            this.username.textboxKeyTyped(c, n);
        }
        if (this.password.isFocused()) {
            this.password.textboxKeyTyped(c, n);
        }
        super.keyTyped(c, n);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) throws IOException {
        this.username.mouseClicked(n, n2, n3);
        this.password.mouseClicked(n, n2, n3);
        super.mouseClicked(n, n2, n3);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.representedScreen.drawBackground(0);
        RenderUtils.drawRect(30, 30, this.representedScreen.getWidth() - 30, this.representedScreen.getHeight() - 30, Integer.MIN_VALUE);
        Fonts.roboto40.drawCenteredString("Add Account", (float)this.representedScreen.getWidth() / 2.0f, 34.0f, 0xFFFFFF);
        Fonts.roboto35.drawCenteredString(this.status == null ? "" : this.status, (float)this.representedScreen.getWidth() / 2.0f, (float)this.representedScreen.getHeight() / 4.0f + 60.0f, 0xFFFFFF);
        this.username.drawTextBox();
        this.password.drawTextBox();
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            Fonts.roboto40.drawCenteredString("\u00a77Username / E-Mail", this.representedScreen.getWidth() / 2 - 55, 66.0f, 0xFFFFFF);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            Fonts.roboto40.drawCenteredString("\u00a77Password", this.representedScreen.getWidth() / 2 - 74, 91.0f, 0xFFFFFF);
        }
        super.drawScreen(n, n2, f);
    }

    private void lambda$addAccount$0(MinecraftAccount minecraftAccount) {
        if (!minecraftAccount.isCracked()) {
            this.status = "\u00a7aChecking...";
            try {
                AltService.EnumAltService enumAltService = GuiAltManager.altService.getCurrentService();
                if (enumAltService != AltService.EnumAltService.MOJANG) {
                    GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                }
                YggdrasilUserAuthentication yggdrasilUserAuthentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                yggdrasilUserAuthentication.setUsername(minecraftAccount.getName());
                yggdrasilUserAuthentication.setPassword(minecraftAccount.getPassword());
                yggdrasilUserAuthentication.logIn();
                minecraftAccount.setAccountName(yggdrasilUserAuthentication.getSelectedProfile().getName());
                if (enumAltService == AltService.EnumAltService.THEALTENING) {
                    GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                }
            }
            catch (AuthenticationException | IllegalAccessException | NoSuchFieldException | NullPointerException throwable) {
                this.status = "\u00a7cThe account doesn't work.";
                this.addButton.setEnabled(true);
                this.clipboardButton.setEnabled(true);
                return;
            }
        }
        LiquidBounce.fileManager.accountsConfig.getAccounts().add(minecraftAccount);
        LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.accountsConfig);
        this.prevGui.status = this.status = "\u00a7aThe account has been added.";
        mc.displayGuiScreen(this.prevGui.representedScreen);
    }

    public GuiAdd(GuiAltManager guiAltManager) {
        this.prevGui = guiAltManager;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.addButton = classProvider.createGuiButton(1, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 72, "Add");
        this.representedScreen.getButtonList().add(this.addButton);
        this.clipboardButton = classProvider.createGuiButton(2, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 96, "Clipboard");
        this.representedScreen.getButtonList().add(this.clipboardButton);
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(0, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 120, "Back"));
        this.username = classProvider.createGuiTextField(2, Fonts.roboto40, this.representedScreen.getWidth() / 2 - 100, 60, 200, 20);
        this.username.setFocused(true);
        this.username.setMaxStringLength(Integer.MAX_VALUE);
        this.password = classProvider.createGuiPasswordField(3, Fonts.roboto40, this.representedScreen.getWidth() / 2 - 100, 85, 200, 20);
        this.password.setMaxStringLength(Integer.MAX_VALUE);
    }

    private void addAccount(String string, String string2) {
        if (LiquidBounce.fileManager.accountsConfig.accountExists(string)) {
            this.status = "\u00a7cThe account has already been added.";
            return;
        }
        this.addButton.setEnabled(false);
        this.clipboardButton.setEnabled(false);
        MinecraftAccount minecraftAccount = new MinecraftAccount(string, string2);
        new Thread(() -> this.lambda$addAccount$0(minecraftAccount)).start();
    }
}

