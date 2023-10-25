/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.altmanager.GuiAltManager;
import org.celestial.client.ui.components.altmanager.PasswordField;
import org.celestial.client.ui.components.altmanager.alt.Alt;
import org.celestial.client.ui.components.altmanager.alt.AltManager;
import org.lwjgl.input.Keyboard;

public class GuiAddAlt
extends GuiScreen {
    private final GuiAltManager manager;
    private PasswordField password;
    private String status = (Object)((Object)TextFormatting.GRAY) + "Idle...";
    private GuiTextField username;

    GuiAddAlt(GuiAltManager manager) {
        this.manager = manager;
    }

    private static void setStatus(GuiAddAlt guiAddAlt, String status) {
        guiAddAlt.status = status;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
                login.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 2: {
                String data;
                try {
                    data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                }
                catch (Exception var4) {
                    return;
                }
                if (!data.contains(":")) break;
                String[] credentials = data.split(":");
                this.username.setText(credentials[0]);
                this.password.setText(credentials[1]);
            }
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RectHelper.drawRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(17, 17, 17, 255).getRGB());
        RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), 0.0f, 0.0f, sr.getScaledWidth(), 2.0f, ClientHelper.getClientColor());
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.mc.fontRendererObj.drawCenteredString("Add Account", (float)this.width / 2.0f, 15.0f, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            this.drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            this.drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
        }
        this.mc.fontRendererObj.drawCenteredString(this.status, (float)this.width / 2.0f, 30.0f, -1);
        super.drawScreen(i, j, f);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 92 - 12, "Import User:Pass"));
        this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException var5) {
            var5.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }

    private class AddAltThread
    extends Thread {
        private final String password;
        private final String username;

        AddAltThread(String username, String password) {
            this.username = username;
            this.password = password;
            GuiAddAlt.setStatus(GuiAddAlt.this, (Object)((Object)TextFormatting.GRAY) + "Idle...");
        }

        private void checkAndAddAlt(String username, String password) {
            try {
                YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
                auth.setUsername(username);
                auth.setPassword(password);
                try {
                    auth.logIn();
                    AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working));
                    GuiAddAlt.setStatus(GuiAddAlt.this, "\u00a7AAdded alt - " + (Object)((Object)ChatFormatting.RED) + (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() ? "Protected" : this.username) + (Object)((Object)ChatFormatting.GREEN) + " \u00a7a" + (Object)((Object)ChatFormatting.BOLD) + "(license)");
                }
                catch (AuthenticationException var7) {
                    GuiAddAlt.setStatus(GuiAddAlt.this, (Object)((Object)TextFormatting.RED) + "Connect failed!");
                    var7.printStackTrace();
                }
            }
            catch (Throwable e) {
                GuiAddAlt.setStatus(GuiAddAlt.this, (Object)((Object)TextFormatting.RED) + "Error");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            if (this.password.equals("")) {
                AltManager.registry.add(new Alt(this.username, ""));
                GuiAddAlt.setStatus(GuiAddAlt.this, "\u00a7AAdded alt - " + (Object)((Object)ChatFormatting.RED) + (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() ? "Protected" : this.username) + (Object)((Object)ChatFormatting.GREEN) + " \u00a7c" + (Object)((Object)ChatFormatting.BOLD) + "(non license)");
            } else {
                GuiAddAlt.setStatus(GuiAddAlt.this, (Object)((Object)TextFormatting.AQUA) + "Trying connect...");
                this.checkAndAddAlt(this.username, this.password);
            }
        }
    }
}

