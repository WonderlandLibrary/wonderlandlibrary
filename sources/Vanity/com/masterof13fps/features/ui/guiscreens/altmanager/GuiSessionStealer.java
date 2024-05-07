package com.masterof13fps.features.ui.guiscreens.altmanager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.utils.render.Colors;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;

public class GuiSessionStealer extends GuiScreen {
    public GuiScreen parent;

    public static ArrayList altList = new ArrayList();
    public static ArrayList guiSlotList = new ArrayList();
    public static File altFile;

    private GuiScreen parentScreen;
    private GuiTextField sessionID;

    public File getAltFile() {
        return altFile;
    }

    public GuiSessionStealer(GuiScreen parentScreen) {
        parent = parentScreen;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, width / 2 - 100, height - 185, 200, 20, "Session-Token verwenden"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 160, 200, 20, I18n.format("gui.cancel")));
        sessionID = new GuiTextField(0, fontRendererObj, width / 2 - 100, height - 250, 200, 20);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(parent);
        }
        // Use Session from ID
        if (button.id == 0) {
            if (!sessionID.getText().isEmpty()) {
                String input = sessionID.getText();
                if (input.length() != 65 || !input.startsWith(":", 32) || input.split(":").length != 2) {
                    return;
                }
                String uuid = input.split(":")[1];
                if (uuid.contains("-")) {
                    return;
                }

                JsonElement rawJson;
                try {
                    rawJson = jsonParser().parse(
                            new InputStreamReader(new URL("https://api.mojang.com/user/profiles/" + uuid + "/names")
                                    .openConnection().getInputStream()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (!rawJson.isJsonArray()) {
                    return;
                }

                JsonArray json = rawJson.getAsJsonArray();
                String name = json.get(json.size() - 1).getAsJsonObject().get("name").getAsString();

                try {
                    Proxy proxy = MinecraftServer.getServer() == null ? null
                            : MinecraftServer.getServer().getServerProxy();
                    if (proxy == null)
                        proxy = Proxy.NO_PROXY;

                    HttpURLConnection connection = (HttpURLConnection) new URL("https://authserver.mojang.com/validate")
                            .openConnection(proxy);

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    String content = "{\"accessToken\":\"" + input.split(":")[0] + "\"}";

                    connection.setRequestProperty("Content-Length", "" + content.getBytes().length);
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                    output.writeBytes(content);
                    output.flush();
                    output.close();

                    if (connection.getResponseCode() != 204)
                        throw new IOException();
                } catch (IOException ignored) {
                }
                mc.session = new Session(name, uuid, input.split(":")[0], "mojang");
                mc.displayGuiScreen(parent);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        sessionID.textboxKeyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        sessionID.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int posX, int posY, float f) {
        drawString(mc.fontRendererObj, "", width / 2 - 100, 79, 10526880);

        ScaledResolution sr = new ScaledResolution(Wrapper.mc);
        if (Keyboard.isKeyDown(1)) {
            mc.displayGuiScreen(parent);
        }

        mc.getTextureManager().bindTexture(new ResourceLocation(Client.main().getClientBackground()));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, sr.width(), sr.height(),
                width, height, sr.width(), sr.height());

        int x = width / 2 - 150;
        int darkGray = -15658735;
        int lightGray = -15066598;
        RenderUtils.drawBorderedRect(width / 2 - 150, height / 2 - 150, width / 2 + 150, height / 2 + 150, 1, darkGray, lightGray);
        UnicodeFontRenderer comfortaa20 = Client.main().fontMgr().font("Comfortaa", 20, Font.PLAIN);
        comfortaa20.drawString("REDEEM SESSION TOKEN", width / 2 - comfortaa20.getStringWidth("REDEEM SESSION TOKEN") / 2, height / 2 - 140, Colors.main().getGrey());
        comfortaa20.drawString("SESSION ID", width / 2 - comfortaa20.getStringWidth("SESSION ID") / 2, height - 265, -1);

        UnicodeFontRenderer font = Client.main().fontMgr().font("Comfortaa", 18, Font.PLAIN);
        String t1 = "Session IDs kannst du in Minecraft Crash Reports finden.";
        String t2 = "Suche im Internet nach \"Minecraft Session ID is token\"";
        String t3 = "Die dort enthaltene ID sieht so aus:";
        String t4 = "Session ID is token:d60534 ... XXX (65 Zeichen)";
        String t5 = "In neuen Launchern wird die Session ID im Log ausgeblendet!";
        String t6 = "Sie kann nur im Minecraft-Ordner unter";
        String t7 = "\"launcher-log.txt\" gefunden werden!";

        font.drawString(t1, width / 2 - font.getStringWidth(t1) / 2, height / 2 - 125, -1);
        font.drawString(t2, width / 2 - font.getStringWidth(t2) / 2, height / 2 - 115, -1);
        font.drawString(t3, width / 2 - font.getStringWidth(t3) / 2, height / 2 - 105, -1);
        font.drawString(t4, width / 2 - font.getStringWidth(t4) / 2, height / 2 - 95, -1);
        font.drawString(t5, width / 2 - font.getStringWidth(t5) / 2, height / 2 - 75, -1);
        font.drawString(t6, width / 2 - font.getStringWidth(t6) / 2, height / 2 - 65, -1);
        font.drawString(t7, width / 2 - font.getStringWidth(t7) / 2, height / 2 - 55, -1);
        sessionID.drawTextBox();
        super.drawScreen(posX, posY, f);
    }
}