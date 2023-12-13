// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.login.gui.impl;

import com.mojang.authlib.exceptions.AuthenticationException;
import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.ui.login.system.Account;
import vip.Resolute.Resolute;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.util.font.FontUtil;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiTextField;
import vip.Resolute.ui.login.gui.components.GuiPasswordField;
import vip.Resolute.ui.login.gui.GuiAltManager;
import net.minecraft.client.gui.GuiScreen;

public class GuiAddAlt extends GuiScreen
{
    private final GuiAltManager manager;
    private GuiPasswordField password;
    private String status;
    private GuiTextField username;
    
    public GuiAddAlt(final GuiAltManager manager) {
        this.status = EnumChatFormatting.GRAY + "Idle...";
        this.manager = manager;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                final AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
                login.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 2: {
                String data = null;
                try {
                    data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                }
                catch (Exception ignored) {
                    break;
                }
                if (data.contains(":")) {
                    final String[] credentials = data.split(":");
                    this.username.setText(credentials[0]);
                    this.password.setText(credentials[1]);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int i, final int j, final float f) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        final MinecraftFontRenderer fr = FontUtil.moon;
        fr.drawCenteredString("Add Alt", (float)(GuiAddAlt.width / 2), 20.0f, -1);
        if (this.username.getText().isEmpty()) {
            this.mc.fontRendererObj.drawString("Username / E-Mail", GuiAddAlt.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.mc.fontRendererObj.drawString("Password", GuiAddAlt.width / 2 - 96, 106, -7829368);
        }
        fr.drawCenteredString(this.status, (float)(GuiAddAlt.width / 2), 30.0f, -1);
        super.drawScreen(i, j, f);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiAddAlt.width / 2 - 100, GuiAddAlt.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, GuiAddAlt.width / 2 - 100, GuiAddAlt.height / 4 + 116 + 12, "Back"));
        this.buttonList.add(new GuiButton(2, GuiAddAlt.width / 2 - 100, GuiAddAlt.height / 4 + 116 + 36, "Import user:pass"));
        this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, GuiAddAlt.width / 2 - 100, 60, 200, 20);
        this.password = new GuiPasswordField(this.mc.fontRendererObj, GuiAddAlt.width / 2 - 100, 100, 200, 20);
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }
    
    static void access$0(final GuiAddAlt guiAddAlt, final String status) {
        guiAddAlt.status = status;
    }
    
    private class AddAltThread extends Thread
    {
        private final String password;
        private final String username;
        
        public AddAltThread(final String username, final String password) {
            this.username = username;
            this.password = password;
            GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.GRAY + "Idle...");
        }
        
        private final void checkAndAddAlt(final String username, final String password) {
            final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                Resolute.getAccountManager().getAccounts().add(new Account(username, password, auth.getSelectedProfile().getName()));
                try {
                    Resolute.getAccountManager().save();
                }
                catch (Exception ex) {}
                GuiAddAlt.access$0(GuiAddAlt.this, "Alt added. (" + username + ")");
                Resolute.getNotificationManager().add(new Notification("Success", "Alt added. (" + username + ")", 5000L, NotificationType.SUCCESS));
            }
            catch (AuthenticationException e) {
                GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.RED + "Alt failed!");
                Resolute.getNotificationManager().add(new Notification("Error", "Alt failed", 5000L, NotificationType.ERROR));
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            if (this.password.equals("")) {
                Resolute.getAccountManager().getAccounts().add(new Account(this.username, "", ""));
                GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.GREEN + "Alt added. (" + this.username + " - offline name)");
                Resolute.getNotificationManager().add(new Notification("Success", "Alt added. (" + this.username + " - offline name)", 5000L, NotificationType.SUCCESS));
                return;
            }
            GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.AQUA + "Trying alt...");
            this.checkAndAddAlt(this.username, this.password);
        }
    }
}
