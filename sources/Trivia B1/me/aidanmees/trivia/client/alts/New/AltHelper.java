package me.aidanmees.trivia.client.alts.New;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltHelper {
    public static String login(String email, String password) {
        if (email.equalsIgnoreCase(null)) {
            return "Please enter a valid email/username!";
        }
        if (password.equalsIgnoreCase(null)) {
            return "Please enter a password!";
        }
        if (email.equalsIgnoreCase(null) && password.equalsIgnoreCase(null)) {
            return "Please enter username/email + password!";
        }
        YggdrasilAuthenticationService ys = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication ya = new YggdrasilUserAuthentication(ys, Agent.MINECRAFT);
        ya.setUsername(email);
        ya.setPassword(password);
        try {
            ya.logIn();
            String playerName = ya.getSelectedProfile().getName();
            String playerID = ya.getSelectedProfile().getId().toString();
            String token = ya.getAuthenticatedToken();
            Minecraft.getMinecraft().session = new Session(playerName, playerID, token, "mojang");
            return "Logged in as: " + Minecraft.getMinecraft().session.getUsername();
        }
        catch (Exception e) {
            return "Failed to Login!";
        }
    }
}

