package dev.rise.ui.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public final class AltLoginThread extends Thread {
    private final String password;
    private EnumAltStatus status = EnumAltStatus.NOT_LOGGED_IN;
    private String username;
    private final Minecraft mc = Minecraft.getMinecraft();

    public AltLoginThread(final String username, final String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.status = EnumAltStatus.NOT_LOGGED_IN;
    }

    private Session createSession(final String username, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(username);
        auth.setPassword(password);

        try {
            auth.logIn();

            this.username = auth.getSelectedProfile().getName();

            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (final AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }

    public EnumAltStatus getStatus() {
        return this.status;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = EnumAltStatus.CRACKED;

            return;
        }
        this.status = EnumAltStatus.NOT_LOGGED_IN;
        final Session auth = this.createSession(this.username, this.password);

        if (auth == null) {
            this.status = EnumAltStatus.FAILED;
        } else {
            this.status = EnumAltStatus.PREMIUM;
            this.mc.session = auth;
        }
    }
}