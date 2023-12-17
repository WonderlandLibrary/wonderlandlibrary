// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager.alt;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;

public class AltLoginThread extends Thread
{
    private final Alt alt;
    private final Minecraft mc;
    private String status;
    
    public AltLoginThread(final Alt alt) {
        this.mc = Minecraft.getMinecraft();
        this.alt = alt;
        this.status = "§7Waiting...";
    }
    
    public Session login(final String name, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final UserAuthentication authentication = (UserAuthentication)new YggdrasilUserAuthentication(service, Agent.MINECRAFT);
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)authentication;
        auth.setUsername(name);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException var8) {
            Minecraft.LOGGER.error(var8.toString());
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
            this.mc.session = new Session(this.alt.getUsername(), "", "", "mojang");
            this.status = TextFormatting.GREEN + "Logged in - " + ChatFormatting.WHITE + this.alt.getUsername() + ChatFormatting.BOLD + ChatFormatting.RED + " (Not License)";
        }
        else {
            this.status = "Logging in...";
            final Session auth = this.login(this.alt.getUsername(), this.alt.getPassword());
            if (auth == null) {
                this.status = "Connect failed!";
                if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.NotWorking);
                }
            }
            else {
                AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword());
                this.status = TextFormatting.GREEN + "Logged in - " + ChatFormatting.WHITE + auth.getUsername() + ChatFormatting.BOLD + ChatFormatting.RED + " (License)";
                this.alt.setMask(auth.getUsername());
                this.mc.session = auth;
            }
        }
    }
}
