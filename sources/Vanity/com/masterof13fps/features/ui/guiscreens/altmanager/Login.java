package com.masterof13fps.features.ui.guiscreens.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import java.net.Proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Login {
    public static void login(String username, String password) {
        YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) authenticationService.createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(username);
        authentication.setPassword(password);

        Thread loginThread = new Thread(() -> {
            try {
                authentication.logIn();
                Minecraft.mc().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");
            } catch (Exception var5) {
                Minecraft.mc().session = new Session(username, "", "", "mojang");
            }
        });
        loginThread.start();

    }
}
