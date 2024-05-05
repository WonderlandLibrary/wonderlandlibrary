/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.altManager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import java.util.UUID;
import me.AveReborn.ui.altManager.GuiAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltLogin {
    public static void login(String username, String password) {
        if (password.contains("OfflineAccountName")) {
            Session sess = new Session(username, "", "", "mojang");
            try {
                Minecraft.getMinecraft().session = sess;
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            GuiAltManager.status = "Logged in (Cracked name)";
        } else {
            YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
            authentication.setUsername(username);
            authentication.setPassword(password);
            try {
                Session sess;
                authentication.logIn();
                Minecraft.getMinecraft().session = sess = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");
                GuiAltManager.status = "Logged in (Premium name)";
            }
            catch (Exception e3) {
                GuiAltManager.status = e3.getMessage();
            }
        }
    }
}

