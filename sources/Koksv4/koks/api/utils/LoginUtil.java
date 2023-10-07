package koks.api.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import koks.Koks;
import lombok.extern.java.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class LoginUtil {

    private static LoginUtil loginUtil;

    public String status = "waiting...";
    public static String lastToken = null;

    public void login(String token) {
        if (token.contains("@alt")) {
            final Thread loginToken = new Thread("loginToken") {
                public void run() {
                    try {
                        status = "Logging in...";
                        final Session session = LoginHelper.INSTANCE.login(token, Koks.name);
                        status = "Logged into §e" + session.getUsername();
                        lastToken = session.getToken();
                        Minecraft.getMinecraft().session = session;
                        // Koks.getKoks().irc.setIngameName(session.getUsername());
                        this.stop();
                    } catch (Exception e) {
                        status = "§c§lError: §cAccount isn't working";
                    }
                }
            };
            loginToken.start();
        }
    }

    public void loginSession(String sessionId, String name) {
        final Thread loginSession = new Thread("loginSession") {
            public void run() {
                LoginHelper.INSTANCE.switchService(LoginHelper.Service.MOJANG);
                status = "Logged into §e" + name + " §7(§eSession§7)";

                String uuid = "";
                //TODO: With Mojang API get the UUID

                Minecraft.getMinecraft().session = new Session(name, uuid, sessionId, "LEGACY");
                //Koks.getKoks().irc.setIngameName(name);
                this.stop();
            }
        };
        loginSession.start();
    }

    public void loginCracked(String name) {
        final Thread loginCracked = new Thread("loginCracked") {
            public void run() {
                LoginHelper.INSTANCE.switchService(LoginHelper.Service.MOJANG);
                status = "Logged into §e" + name + " §7(§cCracked§7)";
                lastToken = null;
                Minecraft.getMinecraft().session = new Session(name, "", "", "LEGACY");
                //Koks.getKoks().irc.setIngameName(name);
                this.stop();
            }
        };
        loginCracked.start();
    }

    public void login(String email, String password) {
        final Thread login = new Thread("login") {
            public void run() {
                try {
                    status = "Logging in...";
                    final Session session = LoginHelper.INSTANCE.login(email, password);
                    status = "Logged into §e" + session.getUsername();
                    lastToken = session.getToken();

                    Minecraft.getMinecraft().session = session;
                    //Koks.getKoks().irc.setIngameName(session.getUsername());
                    this.stop();
                } catch (Exception e) {
                    status = "§c§lError: §cAccount isn't working";
                    this.stop();
                }
            }
        };
        login.start();
    }

    public void generate(String apiToken) {
        final Thread generateAlt = new Thread("generateAlt") {
            public void run() {
                try {
                    status = "Generate...";
                    final Session session = LoginHelper.INSTANCE.generateAlt(apiToken);
                    lastToken = session.getToken();
                    status = "Logged into §e" + session.getUsername();
                    Minecraft.getMinecraft().session = session;
                    Koks.getKoks().irc.setIngameName(session.getUsername());
                    this.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                    status = "§c§lError: §cAccount isn't working";
                    this.stop();
                }
            }
        };
        generateAlt.start();
    }

    public static LoginUtil getInstance() {
        if (loginUtil == null) {
            loginUtil = new LoginUtil();
        }
        return loginUtil;
    }
}
