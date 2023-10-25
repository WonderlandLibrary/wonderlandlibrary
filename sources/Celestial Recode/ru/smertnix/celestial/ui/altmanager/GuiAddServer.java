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
import ru.smertnix.celestial.ui.altmanager.alt.Server;
import ru.smertnix.celestial.ui.altmanager.alt.ServerManager;
import ru.smertnix.celestial.ui.button.GuiMainMenuButton;
import ru.smertnix.celestial.utils.render.RenderUtils;

import org.lwjgl.input.Keyboard;


import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;

public class GuiAddServer extends GuiScreen {

    private final GuiAltManager manager;
    private NameField password;
    private String status;
    private NameField username;
    public String n;
    public boolean a;
    GuiAddServer(GuiAltManager manager) {
        this.status = TextFormatting.GRAY + "Idle...";
        this.manager = manager;
    }

    private static void setStatus(GuiAddServer guiAddAlt, String status) {
        guiAddAlt.status = status;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
            	if (!(username.getText().equalsIgnoreCase("") || password.getText().equalsIgnoreCase(""))) {
            	ServerManager.registry.add(new Server(username.getText(), password.getText()));
            	a = true;
            	n = username.getText();
            	}
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
        
        
            mc.mntsb_18.drawStringWithShadow("Server Name", width / 2 - 96 - 5, height / 2 - 70 - 10, -7829368);

            mc.mntsb_18.drawStringWithShadow("Server IP", width / 2 - 96 - 5, height / 2 - 35 - 10, -7829368);

            if (a)
        mc.mntsb_18.drawStringWithShadow("Server " + TextFormatting.GREEN + n + TextFormatting.RESET + " was added", 2, 2, -1);
      
        super.drawScreen(i, j, f);
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiMainMenuButton(0, width / 2 - 100, height / 4 + 92 + 12 + 20, 200, 10, "Add server"));
        this.buttonList.add(new GuiMainMenuButton(1, width / 2 - 100, height / 4 + 116 + 12 + 22, 200, 10, "Back"));
        this.username = new NameField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, height / 2 - 70, 200, 20);
        this.password = new NameField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, height / 2 - 35, 200, 20);
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
}
