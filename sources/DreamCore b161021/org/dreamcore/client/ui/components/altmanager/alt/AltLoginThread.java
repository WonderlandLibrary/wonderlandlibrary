package org.dreamcore.client.ui.components.altmanager.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.impl.misc.StreamerMode;
import org.dreamcore.client.ui.components.altmanager.GuiAltManager;
import org.dreamcore.client.ui.components.altmanager.althening.api.AltService;

import java.net.Proxy;

public class AltLoginThread extends Thread {

    private final Alt alt;
    private final Minecraft mc = Minecraft.getInstance();
    private String status;

    public AltLoginThread(Alt alt) {
        this.alt = alt;
        this.status = "§7Waiting...";
    }

    private Session createSession(String username, String password) {
        try {
            GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);

            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
            } catch (AuthenticationException e) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void run() {
        if (this.alt.getPassword().equals("")) {
            this.mc.session = new Session(this.alt.getUsername(), "", "", "mojang");
            this.status = "§aLogged in - " + ChatFormatting.RED + (dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue() ? "Protected" : this.alt.getUsername()) + ChatFormatting.GREEN + " §c" + ChatFormatting.BOLD + "(non license)";
            //this.status = "§aLogged in - (" + ChatFormatting.RED + (this.alt.getUsername()) + ChatFormatting.GREEN + " - non license)";
        } else {
            this.status = "§bLogging in...";
            Session auth = this.createSession(this.alt.getUsername(), this.alt.getPassword());
            if (auth == null) {
                this.status = "§cConnect failed!";
                if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.NotWorking);
                }
            } else {
                AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword());
                this.status = "§aLogged in - " + ChatFormatting.RED + (dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue() ? "Protected" : auth.getUsername()) + "§a" + ChatFormatting.BOLD + " (license)";
                //this.status = "§aLogged in - (" + ChatFormatting.RED + (auth.getUsername()) + ChatFormatting.GREEN + ")";
                this.alt.setMask(auth.getUsername());
                this.mc.session = auth;
                if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.Working);
                }
            }
        }
    }
}