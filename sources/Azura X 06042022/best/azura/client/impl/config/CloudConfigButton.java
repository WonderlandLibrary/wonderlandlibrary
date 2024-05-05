package best.azura.client.impl.config;

import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.util.crypt.AESUtil;
import best.azura.client.util.crypt.Crypter;
import best.azura.client.util.render.RenderUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;

import java.awt.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class CloudConfigButton extends ButtonImpl {

    public boolean lastClickedState, lastClickedState2;
    public CloudConfig config;

    public CloudConfigButton(String text, int x, int y, int width, int height) {
        super(text, x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        this.hovered = RenderUtil.INSTANCE.isHoveredScaled(x, y, width, height, mouseX, mouseY, 1.0);
        Color color = lastClickedState ? hovered ? hoverColor.brighter().brighter() : hoverColor.brighter() : hovered ? hoverColor : normalColor;
        RenderUtil.INSTANCE.drawRoundedRect(x, y, width * animation, height * animation, roundness * animation, selected ? RenderUtil.INSTANCE.modifiedAlpha(color, 100) : color);
        Fonts.INSTANCE.arial15bold.drawString(text, x, y + height / 2.0 - Fonts.INSTANCE.arial15bold.FONT_HEIGHT / 2.0 - Fonts.INSTANCE.arial12.FONT_HEIGHT / 3.0 - 2,
                new Color(255, 255, 255, (int) (255 * animation)).getRGB());
        Fonts.INSTANCE.arial12.drawString(description, x, y + height / 2.0 + Fonts.INSTANCE.arial12.FONT_HEIGHT / 3.0 - 2, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

        // TODO retarded way, but since error does not use any other datatype except Strings we gotta live with this.
        if (config.getCreatorRank().equals("100") || config.getCreatorRank().equals("99")) {
            RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (x + width * animation) - 32, (y + height * animation) - 32,
                    (x + width * animation) - 16, (y + height * animation) - 16);

            if (!config.getVersion().equals(Client.RELEASE + "#" + Client.releaseBuild)) {
                RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (x + width * animation) - 64, (y + height * animation) - 32,
                        (x + width * animation) - 48, (y + height * animation) - 16);

                if (config.isFavourite()) {
                    RenderUtil.INSTANCE.drawTexture("custom/icons/star.png", (x + width * animation) - 96, (y + height * animation) - 32,
                            (x + width * animation) - 80, (y + height * animation) - 16);
                }
            } else if (config.isFavourite()) {
                RenderUtil.INSTANCE.drawTexture("custom/icons/star.png", (x + width * animation) - 64, (y + height * animation) - 32,
                        (x + width * animation) - 48, (y + height * animation) - 16);
            }
        } else {
            if (!config.getVersion().equals(Client.RELEASE + "#" + Client.releaseBuild)) {
                RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (x + width * animation) - 64, (y + height * animation) - 32,
                        (x + width * animation) - 48, (y + height * animation) - 16);

                if (config.isFavourite()) {
                    RenderUtil.INSTANCE.drawTexture("custom/icons/star.png", (x + width * animation) - 64, (y + height * animation) - 32,
                            (x + width * animation) - 48, (y + height * animation) - 16);
                }
            } else if (config.isFavourite()) {
                RenderUtil.INSTANCE.drawTexture("custom/icons/star.png", (x + width * animation) - 32, (y + height * animation) - 32,
                        (x + width * animation) - 16, (y + height * animation) - 16);
            }
        }
    }

    public void load() {
        final Config tempConfig = new Config("", "", "", false, false, false, false);

        try {
            URL uploadUrl = new URL("https://api.azura.best/config/get");

            URLConnection urlConnection = uploadUrl.openConnection();

            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Session-Token", Crypter.encode(Client.INSTANCE.sessionToken));
            urlConnection.addRequestProperty("Config", config.getId());

            // Set its primary task to output.
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            JsonStreamParser jsonStreamParser = new JsonStreamParser(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            if (jsonStreamParser.hasNext()) {
                JsonElement jsonResponds = jsonStreamParser.next();

                if (jsonResponds.isJsonObject()) {
                    JsonObject respondsObject = jsonResponds.getAsJsonObject();

                    if (respondsObject.has("success") && respondsObject.get("success").getAsBoolean()) {
                        if (respondsObject.has("config")) {
                            String encrypted = respondsObject.get("config").getAsString();
                            String decrypted = AESUtil.decrypt(encrypted, Client.INSTANCE.aesKey);

                            if (decrypted == null) {
                                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't load config " + config.getName() + ".", 3000, Type.ERROR));
                                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Got the Error responds: Couldn't encrypt Responds!", 3000, Type.ERROR));

                                return;
                            }

                            JsonElement configJson = new JsonParser().parse(decrypted);

                            if (configJson.isJsonObject()) {
                                if (tempConfig.load(configJson.getAsJsonObject(), null)) {
                                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Loaded config " + config.getName() + ".", 3000, Type.INFO));
                                    if (!config.getVersion().equals(Client.RELEASE + "#" + Client.releaseBuild)) {
                                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "You loaded a Config made for another Version of Azura X!", 3000, Type.WARNING));
                                    }
                                } else {
                                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't load config " + config.getName() + ".", 3000, Type.ERROR));
                                }
                            } else {
                                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't load config " + config.getName() + ".", 3000, Type.ERROR));
                                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Got the Error responds: Config convert failed!", 3000, Type.ERROR));
                            }
                        } else {
                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't load config " + config.getName() + ".", 3000, Type.ERROR));
                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Got the Error responds: Invalid Config Content!", 3000, Type.ERROR));
                        }
                    } else {
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't load config " + config.getName() + ".", 3000, Type.ERROR));
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Got the Error responds: " + respondsObject.get("reason").getAsString(), 3000, Type.ERROR));
                    }
                }
            }
        } catch (Exception exception) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't load config " + config.getName() + ".", 3000, Type.ERROR));
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Got the Error responds: " + exception.getMessage(), 3000, Type.ERROR));
        }
    }

    @Override
    public void mouseClicked() {
        if (!hovered) {
            lastClickedState = false;
            lastClickedState2 = false;
            return;
        }
        if (lastClickedState) {
            if (config != null) {
                if (lastClickedState2) {

                    config.setFavourite(!config.isFavourite(), true);

                    if (config.isFavourite()) {
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Added config " + config.getName() + " to Favourites!", 3000, Type.INFO));
                    } else {
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Removed config " + config.getName() + " from Favourites!", 3000, Type.INFO));
                    }

                    lastClickedState2 = false;
                } else {
                    load();
                    lastClickedState2 = true;
                }
            }
        } else lastClickedState = true;
    }
}
