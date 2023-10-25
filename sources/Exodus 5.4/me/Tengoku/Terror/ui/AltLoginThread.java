/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 */
package me.Tengoku.Terror.ui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.ui.Alt;
import me.Tengoku.Terror.ui.AltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class AltLoginThread
extends Thread {
    private final String password;
    private final String username;
    private Minecraft mc = Minecraft.getMinecraft();
    private String status;

    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Logged in. (" + this.username + " - offline name)";
            return;
        }
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Logging in...";
        Session session = this.createSession(this.username, this.password);
        if (session == null) {
            this.status = (Object)((Object)EnumChatFormatting.RED) + "Login failed!";
        } else {
            AltManager altManager = Exodus.INSTANCE.altManager;
            AltManager.lastAlt = new Alt(this.username, this.password);
            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Logged in. (" + session.getUsername() + ")";
            this.mc.session = session;
        }
    }

    public AltLoginThread(String string, String string2) {
        super("Alt Login Thread");
        this.username = string;
        this.password = string2;
        this.status = (Object)((Object)EnumChatFormatting.GRAY) + "Waiting...";
    }

    public void setStatus(String string) {
        this.status = string;
    }

    private Session createSession(String string, String string2) {
        YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication yggdrasilUserAuthentication = (YggdrasilUserAuthentication)yggdrasilAuthenticationService.createUserAuthentication(Agent.MINECRAFT);
        yggdrasilUserAuthentication.setUsername(string);
        yggdrasilUserAuthentication.setPassword(string2);
        yggdrasilUserAuthentication.logIn();
        try {
            return new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException authenticationException) {
            authenticationException.printStackTrace();
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }
}

