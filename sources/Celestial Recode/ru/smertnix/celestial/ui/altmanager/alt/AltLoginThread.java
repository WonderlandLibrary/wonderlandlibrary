package ru.smertnix.celestial.ui.altmanager.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.ui.altmanager.GuiAltManager;
import ru.smertnix.celestial.ui.altmanager.api.AltService;

import java.net.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AltLoginThread extends Thread {

    private final Alt alt;
    private final Minecraft mc = Minecraft.getMinecraft();
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
            this.status = TextFormatting.GREEN + "Logged in - " + ChatFormatting.WHITE + alt.getUsername() + ChatFormatting.BOLD + ChatFormatting.RED + " (Not License)";
        } else {
            this.status = "Logging in...";
            Session auth = this.createSession(this.alt.getUsername(), this.alt.getPassword());
            if (auth == null) {
                this.status = "Connect failed!";
                if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.NotWorking);
                }
            } else {
            	 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                 Date date = new Date();
                AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword(), dateFormat.format(date));
                this.status = TextFormatting.GREEN + "Logged in - " + ChatFormatting.WHITE + auth.getUsername() + ChatFormatting.BOLD + ChatFormatting.RED + " (License)";
                this.alt.setMask(auth.getUsername());
                this.mc.session = auth;
            }
        }
    }
}