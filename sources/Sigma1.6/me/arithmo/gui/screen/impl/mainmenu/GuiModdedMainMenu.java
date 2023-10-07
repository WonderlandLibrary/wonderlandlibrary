/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.lwjgl.opengl.Display
 */
package me.arithmo.gui.screen.impl.mainmenu;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.ssl.internal.ssl.Provider;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import me.arithmo.gui.altmanager.GuiAltManager;
import me.arithmo.gui.screen.component.GuiMenuButton;
import me.arithmo.gui.screen.component.particles.ParticleManager;
import me.arithmo.gui.screen.impl.mainmenu.ClientMainMenu;
import me.arithmo.management.Authentication;
import me.arithmo.util.security.SSLUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

public class GuiModdedMainMenu
extends GuiScreen {
    private ParticleManager particles = new ParticleManager();
    private ResourceLocation text = new ResourceLocation("textures/big.png");
    private ResourceLocation background = new ResourceLocation("textures/mainmenubackground.png");

    @Override
    public void initGui() {
        super.initGui();
        String strSSP = I18n.format("Singleplayer", new Object[0]);
        String strSMP = I18n.format("Multiplayer", new Object[0]);
        String strOptions = I18n.format("Options", new Object[0]);
        String strQuit = I18n.format("Exit", new Object[0]);
        String strLang = I18n.format("Language", new Object[0]);
        String strAccounts = "Accounts";
        int initHeight = this.height / 4 + 48;
        int objHeight = 17;
        int objWidth = 63;
        int xMid = this.width / 2 - 75;
        this.buttonList.add(new GuiMenuButton(0, xMid - 150, initHeight, objWidth, objHeight, strSSP));
        this.buttonList.add(new GuiMenuButton(1, xMid - 50, initHeight, objWidth, objHeight, strSMP));
        this.buttonList.add(new GuiMenuButton(2, xMid + 50, initHeight, objWidth, objHeight, strOptions));
        this.buttonList.add(new GuiMenuButton(3, xMid + 150, initHeight, objWidth, objHeight, strLang));
        this.buttonList.add(new GuiMenuButton(4, xMid - 100, initHeight + 100, objWidth, objHeight, strAccounts));
        this.buttonList.add(new GuiMenuButton(5, xMid + 100, initHeight + 100, objWidth, objHeight, strQuit));
//        try {
//            String inputLine;
//            SSLUtilities.trustAllHostnames();
//            SSLUtilities.trustAllHttpsCertificates();
//            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
//            Security.addProvider(new Provider());
//            HostnameVerifier allHostsValid = (hostname, session) -> true;
//            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//            HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
//            File file = new File(new File("Sigma"), "public.key");
//            URL url = new URL("https://www.sigmaclient.info/verify/" + Authentication.convertToHex(file) + "|" + Minecraft.getHwid());
//            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
//            connection.setReadTimeout(60000);
//            connection.setConnectTimeout(60000);
//            connection.setRequestProperty("User-Agent", "ArthimoWareTM-Agent");
//            connection.setUseCaches(false);
//            connection.connect();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder str = new StringBuilder();
//            while ((inputLine = reader.readLine()) != null) {
//                str.append(inputLine);
//            }
//            Gson gson = new Gson();
//            JsonObject array = (JsonObject)gson.fromJson(str.toString(), JsonObject.class);
//            if (!array.get("HWID").toString().equalsIgnoreCase("\"" + Minecraft.getHwid() + "\"")) {
//                // empty if block
//            }
//            reader.close();
//        }
//        catch (Exception e) {
//        //    Display.destroy();
//        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        } else if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        } else if (button.id == 4) {
            this.mc.displayGuiScreen(new GuiAltManager());
        } else if (button.id == 5) {
            this.mc.shutdown();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int w = sr.getScaledWidth();
        int h = sr.getScaledHeight();
        this.mc.getTextureManager().bindTexture(this.background);
        GuiModdedMainMenu.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, w + 2, h, w + 2, h, w + 2, h);
        GlStateManager.bindTexture(0);
        GlStateManager.enableBlend();
        this.mc.getTextureManager().bindTexture(this.text);
        GuiModdedMainMenu.drawModalRectWithCustomSizedTexture(w / 2 - 75, this.height / 5 - 20, 0.0f, 0.0f, 150, 48, 150.0f, 48.0f);
        GlStateManager.disableBlend();
        this.particles.render(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

