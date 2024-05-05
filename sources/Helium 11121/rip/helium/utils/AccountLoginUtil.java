package rip.helium.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import java.net.Proxy;

public final class AccountLoginUtil
        extends Thread {
    private final String password;
    private String status;
    private final String username;
    private final Minecraft mc;

    public AccountLoginUtil(String username, String password) {
        super("Alt Login Thread");
        mc = Minecraft.getMinecraft();
        this.username = username;
        this.password = password;
        status = (EnumChatFormatting.GRAY + "Waiting...");
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void run() {
        if (password.equals("")) {
            mc.session = new Session(username, "", "", "mojang");
            status = (EnumChatFormatting.GREEN + "[CRACKED] " + username);
            return;
        }
        status = (EnumChatFormatting.YELLOW + "Logging in...");
        Session auth = createSession(username, password);
        if (auth == null) {
            status = (EnumChatFormatting.RED + "Login failed!");
        } else {
            status = (EnumChatFormatting.GREEN + auth.getUsername());
            mc.session = auth;
        }
    }
}
