/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.altmanager.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.ui.components.altmanager.GuiAltManager;
import org.celestial.client.ui.components.altmanager.alt.Alt;
import org.celestial.client.ui.components.altmanager.alt.AltManager;
import org.celestial.client.ui.components.altmanager.althening.api.AltService;

public final class AltLoginThread
extends Thread {
    private final Alt alt;
    private final Minecraft mc = Minecraft.getMinecraft();
    private String status;

    public AltLoginThread(Alt alt) {
        super("Alt Login Thread");
        this.alt = alt;
        this.status = "\u00a77Waiting...";
    }

    private Session createSession(String username, String password) {
        try {
            GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
            }
            catch (AuthenticationException e) {
                return null;
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void run() {
        if (this.alt.getPassword().equals("")) {
            this.mc.session = new Session(this.alt.getUsername(), "", "", "mojang");
            this.status = "\u00a7aLogged in - " + (Object)((Object)ChatFormatting.RED) + (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() ? "Protected" : this.alt.getUsername()) + (Object)((Object)ChatFormatting.GREEN) + " \u00a7c" + (Object)((Object)ChatFormatting.BOLD) + "(non license)";
        } else {
            this.status = "\u00a7bLogging in...";
            Session auth = this.createSession(this.alt.getUsername(), this.alt.getPassword());
            if (auth == null) {
                this.status = "\u00a7cConnect failed!";
                if (this.alt.getStatus().equals((Object)Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.NotWorking);
                }
            } else {
                AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword());
                this.status = "\u00a7aLogged in - " + (Object)((Object)ChatFormatting.RED) + (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() ? "Protected" : auth.getUsername()) + "\u00a7a" + (Object)((Object)ChatFormatting.BOLD) + " (license)";
                this.alt.setMask(auth.getUsername());
                this.mc.session = auth;
                if (this.alt.getStatus().equals((Object)Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.Working);
                }
            }
        }
    }
}

