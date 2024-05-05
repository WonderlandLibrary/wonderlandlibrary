// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.altmanager;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.ui.components.altmanager.alt.Alt;
import ru.fluger.client.ui.components.altmanager.alt.AltManager;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;

public class GuiAddAlt extends blk
{
    private final GuiAltManager manager;
    private PasswordField password;
    private String status;
    private bje username;
    
    GuiAddAlt(final GuiAltManager manager) {
        this.status = a.h + "Idle...";
        this.manager = manager;
    }
    
    private static void setStatus(final GuiAddAlt guiAddAlt, final String status) {
        guiAddAlt.status = status;
    }
    
    @Override
    protected void a(final bja button) {
        switch (button.k) {
            case 0: {
                final AddAltThread login = new AddAltThread(this.username.b(), this.password.getText());
                login.start();
                break;
            }
            case 1: {
                this.j.a(this.manager);
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
                if (!data.contains(":")) {
                    break;
                }
                final String[] credentials = data.split(":");
                this.username.a(credentials[0]);
                this.password.setText(credentials[1]);
                break;
            }
        }
    }
    
    @Override
    public void a(final int i, final int j, final float f) {
        final bit sr = new bit(this.j);
        RectHelper.drawRect(0.0, 0.0, sr.a(), sr.b(), new Color(17, 17, 17, 255).getRGB());
        RenderHelper.drawImage(new nf("celestial/skeet.png"), 0.0f, 0.0f, (float)sr.a(), 2.0f, ClientHelper.getClientColor());
        this.username.g();
        this.password.drawTextBox();
        this.j.k.drawCenteredString("Add Account", this.l / 2.0f, 15.0f, -1);
        if (this.username.b().isEmpty() && !this.username.m()) {
            this.c(this.j.k, "Username / E-Mail", this.l / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            this.c(this.j.k, "Password", this.l / 2 - 96, 106, -7829368);
        }
        this.j.k.drawCenteredString(this.status, this.l / 2.0f, 30.0f, -1);
        super.a(i, j, f);
    }
    
    @Override
    public void b() {
        Keyboard.enableRepeatEvents(true);
        this.n.clear();
        this.n.add(new bja(0, this.l / 2 - 100, this.m / 4 + 92 + 12, "Login"));
        this.n.add(new bja(1, this.l / 2 - 100, this.m / 4 + 116 + 12, "Back"));
        this.n.add(new bja(2, this.l / 2 - 100, this.m / 4 + 92 - 12, "Import User:Pass"));
        this.username = new bje(this.h, this.j.k, this.l / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.j.k, this.l / 2 - 100, 100, 200, 20);
    }
    
    @Override
    protected void a(final char par1, final int par2) {
        this.username.a(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.m() || this.password.isFocused())) {
            this.username.b(!this.username.m());
            this.password.setFocused(!this.password.isFocused());
        }
        if (par1 == '\r') {
            this.a(this.n.get(0));
        }
    }
    
    @Override
    protected void a(final int par1, final int par2, final int par3) {
        try {
            super.a(par1, par2, par3);
        }
        catch (IOException var5) {
            var5.printStackTrace();
        }
        this.username.a(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }
    
    private class AddAltThread extends Thread
    {
        private final String password;
        private final String username;
        
        AddAltThread(final String username, final String password) {
            this.username = username;
            this.password = password;
            setStatus(GuiAddAlt.this, a.h + "Idle...");
        }
        
        private void checkAndAddAlt(final String username, final String password) {
            try {
                final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
                auth.setUsername(username);
                auth.setPassword(password);
                try {
                    auth.logIn();
                    AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working));
                    setStatus(GuiAddAlt.this, "§AAdded alt - " + ChatFormatting.RED + username + ChatFormatting.GREEN + " §a" + ChatFormatting.BOLD + "(license)");
                }
                catch (AuthenticationException var7) {
                    setStatus(GuiAddAlt.this, a.m + "Connect failed!");
                    var7.printStackTrace();
                }
            }
            catch (Throwable e) {
                setStatus(GuiAddAlt.this, a.m + "Error");
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            if (this.password.equals("")) {
                AltManager.registry.add(new Alt(this.username, ""));
                setStatus(GuiAddAlt.this, "§AAdded alt - " + ChatFormatting.RED + this.username + ChatFormatting.GREEN + " §c" + ChatFormatting.BOLD + "(non license)");
            }
            else {
                setStatus(GuiAddAlt.this, a.l + "Trying connect...");
                this.checkAndAddAlt(this.username, this.password);
            }
        }
    }
}
