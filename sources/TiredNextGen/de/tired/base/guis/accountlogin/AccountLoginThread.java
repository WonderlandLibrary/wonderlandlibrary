package de.tired.base.guis.accountlogin;

import de.tired.base.interfaces.IHook;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import lombok.SneakyThrows;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import java.net.Proxy;

public class AccountLoginThread extends Thread implements IHook {

    private String pass, name;
    public String status;

    public AccountLoginThread(final String name, final String pass) {
        super("AccountLoginThread");
        this.name = name;
        this.pass = pass;
        this.status = "Waiting for login.";
    }

    @SneakyThrows
    private Session doSession(final String name, final String pass) {
        //todo add microsoft login.

        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(name);
        auth.setPassword(pass);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }

    }

    @Override
    public void run() {

        //not supporting cracked login because mojang´s agbs

        final Session auth = doSession(name, pass);

        if (this.pass.equals("")) {
            this.MC.session = new Session(this.name, "", "", "mojang");
            this.status = (Object)((Object) EnumChatFormatting.GREEN) + "Logged in. (" + this.name + " - offline name)";
            return;
        }


        if (auth == null) {
            this.status = "Login failed. Account not existing, or banned.";
        } else {
            this.status = "Logged sucessfully into account. " + auth.getUsername();
            MC.session = auth;
        }

    }

    public String getPass() {
        return pass;
    }

    public String getStatus() {
        return status;
    }
}
