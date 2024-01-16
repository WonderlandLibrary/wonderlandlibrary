// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.altmanager.alt;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import ru.fluger.client.ui.components.altmanager.althening.api.AltService;
import ru.fluger.client.ui.components.altmanager.GuiAltManager;

public final class AltLoginThread extends Thread
{
    private final Alt alt;
    private final bib mc;
    private String status;
    
    public AltLoginThread(final Alt alt) {
        super("Alt Login Thread");
        this.mc = bib.z();
        this.alt = alt;
        this.status = "§7Waiting...";
    }
    
    private bii createSession(final String username, final String password) {
        try {
            GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
            final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                return new bii(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
            }
            catch (AuthenticationException e2) {
                return null;
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    @Override
    public void run() {
        if (this.alt.getPassword().equals("")) {
            this.mc.af = new bii(this.alt.getUsername(), "", "", "mojang");
            this.status = "§aLogged in - " + ChatFormatting.RED + this.alt.getUsername() + ChatFormatting.GREEN + " §c" + ChatFormatting.BOLD + "(non license)";
        }
        else {
            this.status = "§bLogging in...";
            final bii auth = this.createSession(this.alt.getUsername(), this.alt.getPassword());
            if (auth == null) {
                this.status = "§cConnect failed!";
                if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.NotWorking);
                }
            }
            else {
                AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword());
                this.status = "§aLogged in - " + ChatFormatting.RED + auth.c() + "§a" + ChatFormatting.BOLD + " (license)";
                this.alt.setMask(auth.c());
                this.mc.af = auth;
                if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.Working);
                }
            }
        }
    }
}
