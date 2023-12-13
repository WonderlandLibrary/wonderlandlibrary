/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package me.wintware.client.ui.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import me.wintware.client.Main;
import me.wintware.client.ui.login.GLSLSandboxShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class LoginGui
extends GuiScreen {
    private final GLSLSandboxShader backgroundShader;
    private final long initTime = System.currentTimeMillis();
    public static GuiTextField hwid;
    String status = "";

    public LoginGui() {
        try {
            this.backgroundShader = new GLSLSandboxShader("/noise.fsh");
        }
        catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        hwid = new GuiTextField(123123, this.mc.fontRendererObj, width / 2 - 100, height / 2 - 60, 200, 20);
        hwid.setMaxStringLength(16);
        this.buttonList.add(new GuiButton(1001, width / 2 - 100, height / 2 + 10, "Authentication"));
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1001: {
                try {
                    String check = LoginGui.requestURLSRC("https://pastebin.com/raw/ES3BbTpr");
                    if (!check.equalsIgnoreCase("1337")) {
                        System.exit(-1);
                        break;
                    }
                    this.mc.displayGuiScreen(new GuiMainMenu());
                    break;
                }
                catch (Exception var7) {
                    this.mc.shutdownMinecraftApplet();
                }
            }
        }
    }

    public static String requestURLSRC(String BLviCHHy76v5Ch39PB3hpcX7W2qe45YaBPQyn285Dcg27) throws IOException {
        URL urlObject = new URL(BLviCHHy76v5Ch39PB3hpcX7W2qe45YaBPQyn285Dcg27);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        return LoginGui.AP2iKAwcS2gFL8cX8z944ZiJp2zS54T68Tp39nr2rJAwh(urlConnection.getInputStream());
    }

    private static String AP2iKAwcS2gFL8cX8z944ZiJp2zS54T68Tp39nr2rJAwh(InputStream L58C336iNBkwz86u4QV3HcDJ94i34gWv4gpzbqBC5ZCdG) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(L58C336iNBkwz86u4QV3HcDJ94i34gWv4gpzbqBC5ZCdG, StandardCharsets.UTF_8))){
            String var5;
            String string;
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            String string2 = string = (var5 = stringBuilder.toString());
            return string2;
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        GlStateManager.disableCull();
        this.backgroundShader.useShader(width, height, x2, y2, (float)(System.currentTimeMillis() - this.initTime) / 1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        new ScaledResolution(this.mc);
        hwid.drawTextBox();
        if (hwid.getText().isEmpty()) {
            float var10002 = width / 2 - 96;
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("UID", var10002, height / 2 - 54, -1);
        }
        Minecraft.getMinecraft().fontRenderer.drawCenteredString(Main.name + " Auth", width / 2, height / 2 - 90, -1);
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void mouseClicked(int x2, int y2, int z2) {
        try {
            super.mouseClicked(x2, y2, z2);
        }
        catch (IOException var5) {
            var5.printStackTrace();
        }
        hwid.mouseClicked(x2, y2, z2);
    }

    @Override
    protected void keyTyped(char character, int key) {
        hwid.textboxKeyTyped(character, key);
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        if (key == 1) {
            this.mc.displayGuiScreen(new LoginGui());
        }
    }
}

