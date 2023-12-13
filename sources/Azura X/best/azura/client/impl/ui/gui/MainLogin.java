package best.azura.client.impl.ui.gui;

import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.impl.ui.gui.impl.buttons.TextButton;
import best.azura.irc.core.entities.*;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.crypt.Crypter;
import best.azura.client.util.crypt.HWIDUtil;
import best.azura.client.util.crypt.HashUtil;
import best.azura.client.util.crypt.RSAUtil;
import best.azura.client.util.other.FileUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class MainLogin extends GuiScreen {

    private final ArrayList<ButtonImpl> buttons = new ArrayList<>();
    private final GuiScreen parent;
    private double animation = 0;
    private long start = 0;
    private GuiScreen toShow;

    public MainLogin() {
        parent = null;
    }

    public MainLogin(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        start = System.currentTimeMillis();
        animation = 1;

        buttons.clear();
        Display.setTitle("Waiting for login...");
        String[] strings = new String[]{"Login", "Exit"};
        int calcWidth = 0;
        for (String s : strings) {
            int width = Fonts.INSTANCE.arial20.getStringWidth(s);
            buttons.add(new ButtonImpl(s, mc.displayWidth / 2 - 170 + calcWidth, mc.displayHeight / 2 + 150, width + 40, 40, 6));
            calcWidth += width + 40 + 5;
        }

        buttons.add(new TextButton("Name", mc.displayWidth / 2 - 170, mc.displayHeight / 2 - 50, 340, 40, false));
        buttons.add(new TextButton("Password", mc.displayWidth / 2 - 170, mc.displayHeight / 2, 340, 40, true));

        if (!Client.INSTANCE.getConfigManager().getDataDirectory().exists()) {
            //noinspection ResultOfMethodCallIgnored
            Client.INSTANCE.getConfigManager().getDataDirectory().mkdir();
        }

        File loginFile = new File(Client.INSTANCE.getConfigManager().getClientDirectory() + "/data", "login.json");

        if (loginFile.exists()) {
            JsonObject jsonObject;

            jsonObject = new JsonParser().parse(FileUtil.getContentFromFileAsString(loginFile)).getAsJsonObject();
            ((TextButton) buttons.stream().filter(button1 -> button1.text.equalsIgnoreCase("Name")).findAny().get()).fontText = jsonObject.has("username") ? jsonObject.get("username").getAsString() : "";
            ((TextButton) buttons.stream().filter(button1 -> button1.text.equalsIgnoreCase("Password")).findAny().get()).fontText = jsonObject.has("password") ? Crypter.decode(jsonObject.get("password").getAsString()) : "";
            Client.INSTANCE.clientUserName = ((TextButton) buttons.stream().filter(button1 -> button1.text.equalsIgnoreCase("Name")).findAny().get()).fontText = jsonObject.has("username") ? jsonObject.get("username").getAsString() : "";
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (toShow != null) {
            float anim = Math.min(1, (System.currentTimeMillis() - start) / 2500F);
            animation = -1 * Math.pow(anim - 1, 6) + 1;
            animation = 1 - animation;
        } else {
            float anim = Math.min(1, (System.currentTimeMillis() - start) / 250f);
            animation = -1 * Math.pow(anim - 1, 6) + 1;
        }

        if (toShow != null && animation <= 0.00001) {
            mc.displayGuiScreen(toShow);
            return;
        }

        GlStateManager.pushMatrix();
        glEnable(GL_BLEND);
        drawDefaultBackground();

        RenderUtil.INSTANCE.scaleFix(1.0);
        double scaleAnimation = Math.max(Math.min(animation * 10, 1), 0);
        double buttonAnimation = Math.max(Math.min((9 - animation * 10), 1), 0);
        double loadingAnimation = Math.max(Math.min((4 - animation * 5), 1), 0);
        double disappearAnimation = Math.max(Math.min((animation * 6), 1), 0);
        loadingAnimation = Math.min(disappearAnimation, loadingAnimation);
        scaleAnimation = (1 - scaleAnimation);
        buttonAnimation = (1 - buttonAnimation);
        final ScaledResolution sr = new ScaledResolution(mc);
        final boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurMenu.getObject();
        if (blur) {
            GL11.glPushMatrix();
            RenderUtil.INSTANCE.invertScaleFix(1.0);
            StencilUtil.initStencilToWrite();
            GlStateManager.scale(1.0 / sr.getScaleFactor(), 1.0 / sr.getScaleFactor(), 1);
        }
        RenderUtil.INSTANCE.drawRoundedRect(mc.displayWidth / 2.0 - 200 - 300 * scaleAnimation, mc.displayHeight / 2.0 - 200 - 200 * scaleAnimation, 400 + 600 * scaleAnimation, 400 + 400 * scaleAnimation, 10, new Color(0, 0, 0, 170));
        if (blur) {
            GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), 1);
            StencilUtil.readStencilBuffer(1);
            BlurModule.blurShader.blur();
            StencilUtil.uninitStencilBuffer();
            RenderUtil.INSTANCE.scaleFix(1.0);
            GL11.glPopMatrix();
        }
        for (ButtonImpl button : buttons) {
            button.animation = buttonAnimation;
            button.draw(mouseX, mouseY);
        }

        String text = "Log into Azura X";
        Fonts.INSTANCE.arial20.drawString(text, mc.displayWidth / 2.0 - Fonts.INSTANCE.arial20.getStringWidth(text) / 2F, mc.displayHeight / 2.0 - 190, new Color(255, 255, 255, (int) (255 * buttonAnimation)).getRGB());

        int lineColor = new Color(255, 255, 255, (int) (255 * loadingAnimation)).getRGB();
        if (loadingAnimation != 0) {
            RenderUtil.INSTANCE.drawLine(mc.displayWidth / 2.0 - 50 * loadingAnimation, mc.displayHeight / 2F - 50 * loadingAnimation, mc.displayWidth / 2. + 50 * loadingAnimation, mc.displayHeight / 2. + 50 * loadingAnimation, (float) (7.0f * loadingAnimation), lineColor);
            RenderUtil.INSTANCE.drawLine(mc.displayWidth / 2.0 + 50 * loadingAnimation, mc.displayHeight / 2F - 50 * loadingAnimation, mc.displayWidth / 2. - 50 * loadingAnimation, mc.displayHeight / 2. + 50 * loadingAnimation, (float) (7.0f * loadingAnimation), lineColor);
        }
        glDisable(GL_BLEND);
        GlStateManager.popMatrix();

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for (ButtonImpl button : buttons) {
            if (button.hovered) {
                switch (button.text) {
                    case "Exit":
                        Minecraft.getMinecraft().shutdown();
                        break;
                    case "Login":

                        // Für unsere lieben nicht Atembar. Nein du darfst diesen boolean nicht ändern. Frag nach falls du Account Issues hast. Affe.
                        boolean success = false;

                        String username, password;

                        username = ((TextButton) buttons.stream().filter(button1 -> button1.text.equalsIgnoreCase("Name")).findAny().get()).fontText;

                        password = ((TextButton) buttons.stream().filter(button1 -> button1.text.equalsIgnoreCase("Password")).findAny().get()).fontText;

                        // Client Session opening.
                        String currentToken = null, publicToken = null;

                        // Variable for the URL.
                        URL sessionURL = null;

                        try {
                            // Parse the String into a URL.
                            sessionURL = new URL("https://api.azura.best/auth/clientsession?username=" + URLEncoder.encode(username, StandardCharsets.UTF_8.name()));
                        } catch (Exception ignore) {
                        }

                        // Check if the URL isn't null.
                        if (sessionURL != null) {

                            // Variable for the actual connection.
                            HttpsURLConnection urlConnection = null;

                            try {
                                // Create a URL Connection with the URL.
                                urlConnection = (HttpsURLConnection) sessionURL.openConnection();

                                // Set RequestInfo.
                                urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                                urlConnection.addRequestProperty("Accept", "application/json");

                                if (!urlConnection.getDoOutput()) {
                                    // Set its primary task to output.
                                    urlConnection.setDoOutput(true);
                                }
                            } catch (Exception ignore) {
                            }

                            // Check if it's null.
                            if (urlConnection != null) {

                                if (urlConnection.getResponseCode() == 429) {
                                    try {
                                        // Parse the String into a URL.
                                        sessionURL = new URL("https://azura.best/api/auth/clientsession?username=" + URLEncoder.encode(username, StandardCharsets.UTF_8.name()));
                                    } catch (Exception ignore) {
                                    }


                                    // Create a URL Connection with the URL.
                                    urlConnection = (HttpsURLConnection) sessionURL.openConnection();

                                    // Set RequestInfo.
                                    urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                                    urlConnection.addRequestProperty("Accept", "application/json");

                                    if (!urlConnection.getDoOutput()) {
                                        // Set its primary task to output.
                                        urlConnection.setDoOutput(true);
                                    }
                                }

                                if (urlConnection.getResponseCode() == 403) {
                                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Cloudflare blocked the Request!", 5000, Type.ERROR));
                                    return;
                                }

                                // Variable to convert to JSON.
                                JsonStreamParser jsonStreamParser;

                                try {
                                    // Create the Parser.
                                    jsonStreamParser = new JsonStreamParser(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                                } catch (Exception ignore) {
                                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (JPx01)", 5000, Type.ERROR));
                                    return;
                                }

                                // Check if it's null.
                                if (jsonStreamParser.hasNext()) {

                                    JsonElement jsonElement = jsonStreamParser.next();

                                    if (!jsonElement.isJsonObject()) {
                                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (JPx03, most likely cloudflare Issue.)", 5000, Type.ERROR));
                                        return;
                                    }

                                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                                    // Get success.
                                    if (jsonObject.has("success") && jsonObject.get("success").getAsBoolean()) {
                                        Client.INSTANCE.clientUserName = username;
                                        if (jsonObject.has("public") && jsonObject.has("token")) {
                                            publicToken = jsonObject.get("public").getAsString();
                                            currentToken = jsonObject.get("token").getAsString();
                                            Client.INSTANCE.sessionToken = currentToken;
                                        }
                                    } else {
                                        if (jsonObject.has("reason")) {
                                            Client.INSTANCE.getLogger().info("DEV-NOTE-LRSP: " + jsonObject.get("reason").getAsString());
                                            Files.write(Paths.get("lel.txt"), jsonObject.get("reason").getAsString().getBytes(StandardCharsets.UTF_8));
                                        }
                                    }
                                }
                            } else {
                                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (ULCx01)", 5000, Type.ERROR));
                                return;
                            }
                        } else {
                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (ULx01)", 5000, Type.ERROR));
                            return;
                        }

                        // Check if Data is given.
                        if (publicToken == null || publicToken.isEmpty() || currentToken == null || currentToken.isEmpty()) {
                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (PDx01)", 5000, Type.ERROR));
                            return;
                        }

                        PublicKey publicKey;

                        try {
                            publicKey = RSAUtil.getPublicKey(publicToken);
                        } catch (Exception ignore) {
                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (PDx02)", 5000, Type.ERROR));
                            return;
                        }

                        if (publicKey == null) return;

                        // Actual auth

                        // Variable for the URL.
                        URL authURL = null;

                        try {
                            // Parse the String into a URL.
                            authURL = new URL("https://api.azura.best/auth/clientauth");
                        } catch (Exception ignore) {
                        }

                        // Check if the URL isn't null.
                        if (authURL != null) {

                            // Variable for the actual connection.
                            HttpsURLConnection urlConnection = null;

                            try {
                                // Create a URL Connection with the URL.
                                urlConnection = (HttpsURLConnection) authURL.openConnection();

                                // Set RequestInfo.
                                urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                                urlConnection.addRequestProperty("Accept", "application/json");
                                urlConnection.addRequestProperty("Session-Token", Crypter.encode(currentToken));
                                urlConnection.addRequestProperty("Client-Public", Crypter.encode(Base64.getEncoder().encodeToString(Client.INSTANCE.getKeyPair().getPublic().getEncoded())));
                                urlConnection.addRequestProperty("Client-Name", Crypter.encode(Objects.requireNonNull(RSAUtil.encrypt(URLEncoder.encode(username, StandardCharsets.UTF_8.name()), publicKey))));
                                urlConnection.addRequestProperty("Auth-Token",
                                        Crypter.encode(RSAUtil.encrypt(username, publicKey) + "0#0#0" + HashUtil.getPasswordHash(password) + "1#1#1" + RSAUtil.encrypt(Objects.requireNonNull(HWIDUtil.getHwid()), publicKey)));
                                if (!urlConnection.getDoOutput()) {
                                    // Set its primary task to output.
                                    urlConnection.setDoOutput(true);
                                }
                            } catch (Exception ignore) {
                            }

                            // Check if it's null.
                            if (urlConnection != null) {

                                if (urlConnection.getResponseCode() == 429) {
                                    try {
                                        // Parse the String into a URL.
                                        authURL = new URL("https://azura.best/api/auth/clientauth");
                                    } catch (Exception ignore) {
                                    }

                                    // Create a URL Connection with the URL.
                                    urlConnection = (HttpsURLConnection) authURL.openConnection();

                                    // Set RequestInfo.
                                    urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                                    urlConnection.addRequestProperty("Accept", "application/json");
                                    urlConnection.addRequestProperty("Session-Token", Crypter.encode(currentToken));
                                    urlConnection.addRequestProperty("Client-Public", Crypter.encode(Base64.getEncoder().encodeToString(Client.INSTANCE.getKeyPair().getPublic().getEncoded())));
                                    urlConnection.addRequestProperty("Client-Name", Crypter.encode(Objects.requireNonNull(RSAUtil.encrypt(URLEncoder.encode(username, StandardCharsets.UTF_8.name()), publicKey))));
                                    urlConnection.addRequestProperty("Auth-Token",
                                            Crypter.encode(RSAUtil.encrypt(username, publicKey) + "0#0#0" + HashUtil.getPasswordHash(password) + "1#1#1" + RSAUtil.encrypt(Objects.requireNonNull(HWIDUtil.getHwid()), publicKey)));
                                    if (!urlConnection.getDoOutput()) {
                                        // Set its primary task to output.
                                        urlConnection.setDoOutput(true);
                                    }
                                }

                                if (urlConnection.getResponseCode() == 403) {
                                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Cloudflare blocked the Request!", 5000, Type.ERROR));
                                    return;
                                }

                                // Variable to convert to JSON.
                                JsonStreamParser jsonStreamParser;

                                try {
                                    // Create the Parser.
                                    jsonStreamParser = new JsonStreamParser(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                                } catch (Exception ignore) {
                                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (JPx02)", 5000, Type.ERROR));
                                    return;
                                }

                                // Check if it's null.
                                if (jsonStreamParser.hasNext()) {

                                    JsonObject jsonObject = jsonStreamParser.next().getAsJsonObject();

                                    // Get success.
                                    if (jsonObject.has("success") && jsonObject.get("success").getAsBoolean()) {
                                        success = true;

                                        if (jsonObject.has("user")) {
                                            JsonObject userObject = jsonObject.getAsJsonObject("user");

                                            Client.INSTANCE.aesKey = new SecretKeySpec(Base64.getDecoder().decode(RSAUtil.decrypt(Crypter.decode(userObject.get("key").getAsString()), Client.INSTANCE.getKeyPair().getPrivate())), "AES");
                                        }

                                        if (jsonObject.has("ranks")) {

                                            for (JsonElement jsonElement : jsonObject.get("ranks").getAsJsonArray()) {
                                                if (jsonElement instanceof JsonObject) {
                                                    JsonObject rank = jsonElement.getAsJsonObject();

                                                    if (rank.has("rank_Id")
                                                            && rank.has("name")
                                                            && rank.has("prefix")
                                                            && rank.has("kurzel")) {

                                                        Rank ircRank = new Rank(rank.get("rank_Id").getAsInt(), rank.get("name").getAsString(),
                                                                rank.get("kurzel").getAsString(), rank.get("prefix").getAsString());

                                                        Client.INSTANCE.getIrcConnector().getIrcData().addIRCRank(rank.get("rank_Id").getAsInt(), ircRank);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (jsonObject.has("reason")) {
                                            Client.INSTANCE.getLogger().info("DEV-NOTE-LRSP: " + jsonObject.get("reason").getAsString() + " - " + HWIDUtil.getHwid());
                                            Files.write(Paths.get("lel.txt"), jsonObject.get("reason").getAsString().getBytes(StandardCharsets.UTF_8));
                                        }
                                    }
                                }
                            } else {
                                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (ULCx02)", 5000, Type.ERROR));
                                return;
                            }
                        } else {
                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Invalid Server Response! (ULx02)", 5000, Type.ERROR));
                            return;
                        }

                        if (success) {
                            Client.INSTANCE.getIrcConnector().username = username;

                            Client.INSTANCE.getIrcConnector().password = HashUtil.getPasswordHash(password);

                            Client.INSTANCE.getIrcConnector().startConnection();

                            JsonObject jsonObject = new JsonObject();

                            jsonObject.addProperty("username", username);
                            jsonObject.addProperty("password", Crypter.encode(password));

                            new File(String.valueOf(Client.INSTANCE.getConfigManager().getDataDirectory().mkdirs()));

                            FileUtil.writeContentToFile(new File(Client.INSTANCE.getConfigManager().getDataDirectory(), "login.json"), new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject), true);

                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Welcome!", "Welcome " + Client.INSTANCE.getUsername(), 5000, Type.SUCCESS));
                            toShow = parent;
                            start = System.currentTimeMillis();
                            mc.displayGuiScreen(new GuiMainMenu());
                            new Thread(() -> {
                                try {
                                    Thread.sleep(5 * 1000);
                                } catch (InterruptedException ignore) {}
                                while (true) {
                                    try {
                                        URL url = new URL("https://api.azura.best/session/keepalive");
                                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                                        connection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                                        connection.addRequestProperty("Accept", "application/json");
                                        connection.setRequestProperty("Session-Token", Base64.getEncoder().encodeToString(Client.INSTANCE.sessionToken.getBytes()));

                                        if (connection.getResponseCode() == 429) {
                                            try {
                                                // Parse the String into a URL.
                                                url = new URL("https://azura.best/api/session/keepalive");
                                            } catch (Exception ignore) {
                                            }

                                            // Create a URL Connection with the URL.
                                            connection = (HttpsURLConnection) url.openConnection();

                                            // Set RequestInfo.
                                            connection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                                            connection.addRequestProperty("Accept", "application/json");
                                            connection.setRequestProperty("Session-Token", Base64.getEncoder().encodeToString(Client.INSTANCE.sessionToken.getBytes()));
                                            if (!connection.getDoOutput()) {
                                                // Set its primary task to output.
                                                connection.setDoOutput(true);
                                            }
                                        }

                                        if (connection.getResponseCode() == 403) {
                                            Minecraft.getMinecraft().shutdown();
                                            return;
                                        }

                                        // Variable to convert to JSON.
                                        JsonStreamParser jsonStreamParser;

                                        try {
                                            // Create the Parser.
                                            jsonStreamParser = new JsonStreamParser(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                                        } catch (Exception exception) {
                                            Minecraft.getMinecraft().shutdown();
                                            return;
                                        }

                                        if (jsonStreamParser.hasNext()) {
                                            JsonElement jsonElement = jsonStreamParser.next();
                                            if (jsonElement.isJsonObject()) {
                                                JsonObject jsonObject1 = jsonElement.getAsJsonObject();
                                                if (!jsonObject1.has("success") || !jsonObject1.get("success").getAsBoolean()) Minecraft.getMinecraft().shutdown();
                                            } else {
                                                Minecraft.getMinecraft().shutdown();
                                            }
                                        } else {
                                            Minecraft.getMinecraft().shutdown();
                                        }

                                        connection.disconnect();
                                        Thread.sleep(60 * 1000);
                                    } catch (Exception ignore) {}
                                }
                            }).start();
                        } else {
                            // Variable for the URL.
                            URL sessionStopURL = null;
                            try {
                                // Parse the String into a URL.
                                sessionStopURL = new URL("https://api.azura.best/session/stop");
                            } catch (Exception ignore) {
                            }

                            // Variable for the actual connection.
                            URLConnection urlConnection;

                            try {
                                // Create a URL Connection with the URL.
                                assert sessionStopURL != null;
                                urlConnection = sessionStopURL.openConnection();

                                // Set RequestInfo.
                                urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                                urlConnection.addRequestProperty("Accept", "application/json");
                                urlConnection.addRequestProperty("Session-Token", Crypter.encode(currentToken));
                            } catch (Exception ignore) {
                            }

                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Login failed!", 5000, Type.ERROR));
                        }
                        break;
                }
            }
            button.mouseClicked();
        }

    }

    @Override
    public void confirmClicked(boolean result, int id) {
        initGui();
        mc.displayGuiScreen(this);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (ButtonImpl button : buttons) {
            if (button instanceof TextButton) button.keyTyped(typedChar, keyCode);
        }
    }
}
