package Velo.api.Util.alt.gui.thread;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;

import Velo.api.Main.Main;
import Velo.api.Util.alt.gui.GuiAltManager;
import Velo.api.Util.alt.gui.impl.GuiAlteningLogin;
import Velo.api.Util.alt.system.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.util.Session;

import java.net.Proxy;
import java.util.UUID;

public class AccountLoginThread extends Thread {

    private String email, password;

    private String status = "Waiting for login...";

    public AccountLoginThread(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void run() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin || (Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected)) Main.INSTANCE.switchToTheAltening();
        else Main.INSTANCE.switchToMojang();
        status = "Logging in...";

        YggdrasilAuthenticationService yService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication userAuth = (YggdrasilUserAuthentication)yService.createUserAuthentication(Agent.MINECRAFT);

        if (userAuth == null) {
            status = ChatFormatting.RED + "Unknown error.";
            return;
        }

        userAuth.setUsername(email);
        userAuth.setPassword(password);
        try {
            userAuth.logIn();

            Session session = new Session(userAuth.getSelectedProfile().getName(), userAuth.getSelectedProfile().getId().toString(), userAuth.getAuthenticatedToken(), email.contains("@") ? "mojang" : "legacy");

            Minecraft.getMinecraft().setSession(session);

            Account account = new Account(email,password);
            account.setName(session.getUsername());
            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin || Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected))
            	Main.INSTANCE.getAccountManager().setLastAlt(account);
            Main.INSTANCE.getAccountManager().save();
            GuiAltManager.currentAccount = account;
            status = String.format(ChatFormatting.GREEN + "Logged in as %s.", account.getName());
        } catch (AuthenticationException exception) {
            status = ChatFormatting.RED + "Login failed.";
        } catch (NullPointerException exception) {
            status = ChatFormatting.RED + "Unknown error.";
        }
    }

    public String getStatus() {
        return status;
    }

}


