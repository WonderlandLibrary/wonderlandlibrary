package ru.smertnix.celestial.ui.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.ui.altmanager.alt.Alt;
import ru.smertnix.celestial.ui.altmanager.alt.AltManager;
import ru.smertnix.celestial.ui.button.GuiMainMenuButton;
import ru.smertnix.celestial.utils.render.RenderUtils;

import org.lwjgl.input.Keyboard;


import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GuiAddAlt extends GuiScreen {

    private final GuiAltManager manager;
    private PasswordField password;
    private String status;
    private NameField username;

    GuiAddAlt(GuiAltManager manager) {
        this.status = TextFormatting.GRAY + "Idle...";
        this.manager = manager;
    }

    private static void setStatus(GuiAddAlt guiAddAlt, String status) {
        guiAddAlt.status = status;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
                login.start();
                break;
            case 1:
                this.mc.displayGuiScreen(this.manager);
                break;
            case 2:
                String data;
                try {
                    data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (Exception var4) {
                    return;
                }

                if (data.contains(":")) {
                    String[] credentials = data.split(":");
                    this.username.setText(credentials[0]);
                    this.password.setText(credentials[1]);
                }
        }

    }

    public void drawScreen(int i, int j, float f) {

        ScaledResolution sr = new ScaledResolution(mc);
        
    	RenderUtils.drawImage(new ResourceLocation("celestial/pesun3.jpg"), 0,0,sr.getScaledWidth() + 5, sr.getScaledHeight(), new Color(100, 100, 100));

        this.username.drawTextBox();
        this.password.drawTextBox();
        
        
            mc.mntsb_18.drawStringWithShadow("Name", width / 2 - 96 - 5, height / 2 - 70 - 10, -7829368);

            mc.mntsb_18.drawStringWithShadow("Password", width / 2 - 96 - 5, height / 2 - 35 - 10, -7829368);

        mc.neverlose500_18.drawCenteredString(this.status, width / 2F, 30, -1);
        super.drawScreen(i, j, f);
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiMainMenuButton(0, width / 2 - 100, height / 4 + 92 + 12 + 20, 200, 10, "Login alt"));
        this.buttonList.add(new GuiMainMenuButton(1, width / 2 - 100, height / 4 + 116 + 12 + 22, 200, 10, "Back"));
        this.username = new NameField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, height / 2 - 70, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, height / 2 - 35, 200, 20);
    }

    protected void keyTyped(char par1, int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }

        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }

    private class AddAltThread extends Thread {
        private final String password;
        private final String username;

        AddAltThread(String username, String password) {
            this.username = username;
            this.password = password;
            GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GRAY + "Idle...");
        }

        private void checkAndAddAlt(String username, String password) {
            try {
                YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
                auth.setUsername(username);
                auth.setPassword(password);

                try {
                    auth.logIn();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working, false, dateFormat.format(date)));

                   /* try {
                        Main.instance.fileManager.getFile(Alts.class).saveFile();
                    } catch (Exception var6) {
                    }*/

                    GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Added alt - " + ChatFormatting.RED + (this.username) + ChatFormatting.BOLD + "(license)");
                } catch (AuthenticationException var7) {
                    GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Connect failed!");
                    var7.printStackTrace();
                }
            } catch (Throwable e) {
                GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Error");
                e.printStackTrace();
            }
        }

        public void run() {
            if (this.password.equals("")) {
            	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                AltManager.registry.add(new Alt(this.username, "", dateFormat.format(date)));
                GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Added alt - " + ChatFormatting.RED + (this.username) + ChatFormatting.BOLD + "(non license)");
            } else {
                GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.AQUA + "Trying connect...");
                this.checkAndAddAlt(this.username, this.password);
            }
        }
    }
}
