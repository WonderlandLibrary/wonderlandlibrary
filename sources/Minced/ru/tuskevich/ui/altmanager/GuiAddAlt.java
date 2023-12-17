// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.ui.altmanager.alt.AltManager;
import ru.tuskevich.ui.altmanager.alt.Alt;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.TextFormatting;
import java.awt.Color;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAddAlt extends GuiScreen
{
    private final GuiAltManager manager;
    private PasswordField password;
    private String status;
    private GuiTextField username;
    private Color gradientColor1;
    private Color gradientColor2;
    private Color gradientColor3;
    private Color gradientColor4;
    
    GuiAddAlt(final GuiAltManager manager) {
        this.gradientColor1 = Color.WHITE;
        this.gradientColor2 = Color.WHITE;
        this.gradientColor3 = Color.WHITE;
        this.gradientColor4 = Color.WHITE;
        this.status = TextFormatting.GRAY + "Idle...";
        this.manager = manager;
    }
    
    private static void setStatus(final GuiAddAlt guiAddAlt, final String status) {
        guiAddAlt.status = status;
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
                GuiAddAlt.mc.displayGuiScreen(this.manager);
                break;
            }
            case 2: {
                String data;
                try {
                    data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                }
                catch (Exception var4) {
                    return;
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
        RenderUtility.drawRect(0.0, 0.0, GuiAddAlt.mc.displayWidth, GuiAddAlt.mc.displayHeight, new Color(17, 17, 17).getRGB());
        this.username.drawTextBox();
        this.password.drawTextBox();
        Fonts.MONTSERRAT16.drawCenteredString("Add Account", this.width / 2.0f, 15.0f, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            Fonts.MONTSERRAT16.drawStringWithShadow("Username / E-Mail", (float)(this.width / 2 - 96), 66.0f, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            Fonts.MONTSERRAT16.drawStringWithShadow("Password", (float)(this.width / 2 - 96), 106.0f, -7829368);
        }
        Fonts.MONTSERRAT16.drawCenteredString(this.status, this.width / 2.0f, 30.0f, -1);
        super.drawScreen(i, j, f);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
        this.username = new GuiTextField(this.eventButton, GuiAddAlt.mc.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(GuiAddAlt.mc.fontRenderer, this.width / 2 - 100, 100, 200, 20);
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
        catch (IOException var5) {
            var5.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }
    
    private class AddAltThread extends Thread
    {
        private final String password;
        private final String username;
        
        AddAltThread(final String username, final String password) {
            this.username = username;
            this.password = password;
            setStatus(GuiAddAlt.this, TextFormatting.GRAY + "Idle...");
        }
        
        private void checkAndAddAlt(final String username, final String password) {
            try {
                final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
                auth.setUsername(username);
                auth.setPassword(password);
                try {
                    auth.logIn();
                    AltManager.addAccount(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working));
                    setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Added alt - " + ChatFormatting.RED + this.username + ChatFormatting.BOLD + "(license)");
                }
                catch (AuthenticationException var7) {
                    setStatus(GuiAddAlt.this, TextFormatting.RED + "Connect failed!");
                    var7.printStackTrace();
                }
            }
            catch (Throwable e) {
                setStatus(GuiAddAlt.this, TextFormatting.RED + "Error");
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            if (this.password.equals("")) {
                AltManager.addAccount(new Alt(this.username, ""));
                setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Added alt - " + ChatFormatting.RED + this.username + ChatFormatting.BOLD + "(non license)");
            }
            else {
                setStatus(GuiAddAlt.this, TextFormatting.AQUA + "Trying connect...");
                this.checkAndAddAlt(this.username, this.password);
            }
        }
    }
}
