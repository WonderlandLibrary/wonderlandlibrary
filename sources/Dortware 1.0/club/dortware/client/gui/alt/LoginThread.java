/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 */
package club.dortware.client.gui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import java.net.Proxy;

public final class LoginThread
        extends Thread {
    private final String password;
    private String status;
    private final String username;
    private final Minecraft mc = Minecraft.getMinecraft();
    public LoginThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        status = EnumChatFormatting.GRAY + "Waiting...";
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (Exception exception) {}
        return null;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void run() {
        if (password.equals("")) {
            mc.session = new Session(username.replace("&", "\u00a7"), "", "", "mojang");
            status = EnumChatFormatting.GREEN + "Logged in. (" + username + " - offline name)";
            return;
        }
        status = EnumChatFormatting.AQUA + "Logging in...";
        Session auth = createSession(username, password);
        if (auth == null) {
            status = EnumChatFormatting.RED + "Login failed!";
        } else {
            status = EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")";
            mc.session = auth;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

