package best.azura.client.impl.ui.config;

import best.azura.client.api.ui.buttons.Button;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.config.CloudConfig;
import best.azura.client.impl.config.CloudConfigButton;
import best.azura.client.impl.config.Config;
import best.azura.client.impl.config.ConfigButton;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.impl.ui.gui.ScrollRegion;
import best.azura.client.impl.ui.gui.impl.buttons.RatioButtonImpl;
import best.azura.client.impl.ui.gui.impl.buttons.SliderButtonImpl;
import best.azura.client.impl.ui.gui.impl.Window;
import best.azura.client.impl.ui.gui.impl.buttons.TextButton;
import best.azura.client.util.crypt.AESUtil;
import best.azura.client.util.crypt.Crypter;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.other.FileUtil;
import best.azura.client.util.render.RenderUtil;
import com.google.gson.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConfigGUI extends GuiScreen {

    private final ArrayList<ButtonImpl> buttons = new ArrayList<>();
    public double animation = 0;
    public long start = 0;
    private Window currentWindow;
    private Window configSettingWindow;
    private ScrollRegion scrollRegion;
    private Button selectedButton;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    @Override
    public void initGui() {
        start = System.currentTimeMillis();
        buttons.clear();
        buttons.add(new ButtonImpl("Local", 3, 3, 80, 40));
        buttons.add(new ButtonImpl("Online", 3, 46, 80, 40));
        buttons.add(new ButtonImpl("Reload", 3, 89, 80, 40));
        currentWindow = new Window("Local", 640, 600);
        configSettingWindow = new Window("Config Info", currentWindow.x + currentWindow.width + 10, currentWindow.y, (currentWindow.width / 2), 500);
        int offset = 40, offsetX = 0;
        for (Config config : Client.INSTANCE.getConfigManager().getConfigs()) {
            final ConfigButton button = new ConfigButton(config.getName(), currentWindow.x + offsetX + 2, currentWindow.y + offset, currentWindow.width - 2, 40);
            button.description = "Made by " + Client.INSTANCE.getIrcConnector().username + " on " + config.getCreationDate();
            button.config = config;
            currentWindow.buttons.add(button);
            if (button.selected) Client.INSTANCE.getConfigManager().getConfig(button.config.getName()).load();
            offset += 45;
        }
        scrollRegion = new ScrollRegion(currentWindow.x, currentWindow.y, currentWindow.width, currentWindow.height);
    }

    @Override
    public void onTick() {
        scrollRegion.onTick();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
        animation = -1 * Math.pow(anim - 1, 6) + 1;
        GlStateManager.pushMatrix();
        RenderUtil.INSTANCE.scaleFix(1.0);
        RenderUtil.INSTANCE.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(0, 0, 0, 160));

        if (selectedButton instanceof CloudConfigButton) {
            if (configSettingWindow != null) {
                for (Button button : configSettingWindow.buttons) {
                    button.draw(mouseX, mouseY);
                }
                configSettingWindow.draw(mouseX, mouseY);
                double y = configSettingWindow.y + Fonts.INSTANCE.arial15.FONT_HEIGHT + Fonts.INSTANCE.arial20.FONT_HEIGHT;

                CloudConfigButton cloudConfigButton = ((CloudConfigButton) selectedButton);

                Fonts.INSTANCE.arial15.drawString("Name: " + cloudConfigButton.config.getName(), configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Creator: " + cloudConfigButton.config.getCreatorName(), configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Updated: " + simpleDateFormat.format(new Date(Long.parseLong(cloudConfigButton.config.getLastUpdate()))), configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Compatible: ", configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                if (!cloudConfigButton.config.getVersion().equals(Client.RELEASE + "#" + Client.releaseBuild)) {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Compatible: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Compatible: ")) + 16, y + 18);
                } else {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Compatible: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Compatible: ")) + 16, y + 18);
                }

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 10;

                Fonts.INSTANCE.arial15.drawString("Killaura: ", configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                if (!cloudConfigButton.config.isKillaura()) {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Killaura: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Killaura: ")) + 16, y + 18);
                } else {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Killaura: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Killaura: ")) + 16, y + 18);
                }

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Velocity: ", configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                if (!cloudConfigButton.config.isVelocity()) {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Velocity: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Velocity: ")) + 16, y + 18);
                } else {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Velocity: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Velocity: ")) + 16, y + 18);
                }

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Speed: ", configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                if (!cloudConfigButton.config.isSpeed()) {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Speed: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Speed: ")) + 16, y + 18);
                } else {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Speed: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Speed: ")) + 16, y + 18);
                }

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Fly: ", configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                if (!cloudConfigButton.config.isFly()) {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Fly: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Fly: ")) + 16, y + 18);
                } else {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Fly: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Fly: ")) + 16, y + 18);
                }

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Scaffold: ", configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                if (!cloudConfigButton.config.isScaffold()) {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Scaffold: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Scaffold: ")) + 16, y + 18);
                } else {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Scaffold: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Scaffold: ")) + 16, y + 18);
                }

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Noslow: ", configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                if (!cloudConfigButton.config.isNoslow()) {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Noslow: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Noslow: ")) + 16, y + 18);
                } else {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Noslow: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Noslow: ")) + 16, y + 18);
                }

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Nofall: ", configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                if (!cloudConfigButton.config.isNofall()) {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/warning.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Nofall: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Nofall: ")) + 16, y + 18);
                } else {
                    RenderUtil.INSTANCE.drawTexture("custom/notif/okay.png", (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Nofall: ")), y + 2,
                            (configSettingWindow.x + 20 + Fonts.INSTANCE.arial15.getStringWidth("Nofall: ")) + 16, y + 18);
                }

                if (!cloudConfigButton.config.getNote().isEmpty()) {
                    y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 10;

                    Fonts.INSTANCE.arial15.drawString("Note: " + cloudConfigButton.config.getNote(), configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
                }
            }
        } else if (selectedButton instanceof ConfigButton) {
            if (configSettingWindow != null) {
                for (Button button : configSettingWindow.buttons) {
                    button.draw(mouseX, mouseY);
                }

                configSettingWindow.draw(mouseX, mouseY);
                double y = configSettingWindow.y + Fonts.INSTANCE.arial15.FONT_HEIGHT + Fonts.INSTANCE.arial20.FONT_HEIGHT;

                ConfigButton configButton = ((ConfigButton) selectedButton);

                Fonts.INSTANCE.arial15.drawString("Name: " + configButton.config.getName(), configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Creator: " + (configButton.config.getCreator() == null ? Client.INSTANCE.clientUserName : configButton.config.getCreator()), configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;

                Fonts.INSTANCE.arial15.drawString("Updated: " + configButton.config.getCreationDate(), configSettingWindow.x + 20, y, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;
            }
        }

        for (final ButtonImpl button : buttons) {
            button.animation = animation;
            button.roundness = 5;
            button.normalColor = new Color(3, 3, 3, (int) (button.animation * 150));
            button.hoverColor = new Color(33, 33, 33, (int) (button.animation * 150));
            button.draw(mouseX, mouseY);
        }

        scrollRegion.prepare();
        scrollRegion.render(mouseX, mouseY);
        if (currentWindow != null) {
            int calcHeight = 40, count = 0;
            for (Button button : currentWindow.buttons) {
                if (button instanceof ButtonImpl) {
                    ButtonImpl buttonImpl = (ButtonImpl) button;
                    buttonImpl.y = currentWindow.y + 40 + count * 45 + scrollRegion.mouse;
                    calcHeight += 45;
                    count++;
                }
            }
            currentWindow.draw(mouseX, mouseY);
            if (calcHeight > scrollRegion.height) {
                scrollRegion.offset = scrollRegion.height - calcHeight;
            }
        }
        scrollRegion.end();
        RenderUtil.INSTANCE.invertScaleFix(1.0);
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
    }


    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        scrollRegion.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        String clicked = "Empty";
        for (ButtonImpl button : buttons) {
            if (button.hovered) {
                selectedButton = button;
                if ((scrollRegion.isHovered() || !(button instanceof ConfigButton || button instanceof CloudConfigButton)))
                    clicked = button.text;
            }
            button.mouseClicked();
        }

        if (currentWindow != null) {
            for (Button button : currentWindow.buttons) {
                if (button instanceof ButtonImpl) {
                    ButtonImpl buttonImpl = ((ButtonImpl) button);
                    if (buttonImpl.hovered) {
                        selectedButton = button;
                        if (button instanceof ConfigButton) {
                            if (configSettingWindow != null) {
                                configSettingWindow.buttons.clear();
                                ConfigButton configButton = (ConfigButton) button;
                                int y = configSettingWindow.y + 140;
                                configSettingWindow.buttons.add(new RatioButtonImpl("Killaura", configSettingWindow.x + 20, y, 5.0, Fonts.INSTANCE.arial15));
                                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;
                                configSettingWindow.buttons.add(new RatioButtonImpl("Velocity", configSettingWindow.x + 20, y, 5.0, Fonts.INSTANCE.arial15));
                                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;
                                configSettingWindow.buttons.add(new RatioButtonImpl("Speed", configSettingWindow.x + 20, y, 5.0, Fonts.INSTANCE.arial15));
                                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;
                                configSettingWindow.buttons.add(new RatioButtonImpl("Fly ", configSettingWindow.x + 20, y, 5.0, Fonts.INSTANCE.arial15));
                                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;
                                configSettingWindow.buttons.add(new RatioButtonImpl("Scaffold", configSettingWindow.x + 20, y, 5.0, Fonts.INSTANCE.arial15));
                                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;
                                configSettingWindow.buttons.add(new RatioButtonImpl("Noslow", configSettingWindow.x + 20, y, 5.0, Fonts.INSTANCE.arial15));
                                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;
                                configSettingWindow.buttons.add(new RatioButtonImpl("Nofall", configSettingWindow.x + 20, y, 5.0, Fonts.INSTANCE.arial15));
                                y += Fonts.INSTANCE.arial15.FONT_HEIGHT;
                                configSettingWindow.buttons.add(new TextButton("Note", configSettingWindow.x + 20, y, configSettingWindow.width - 40, 30, false));
                                y += Fonts.INSTANCE.arial15.FONT_HEIGHT + 5;
                                configSettingWindow.buttons.add(new ButtonImpl("Upload Config", configSettingWindow.x + 85, configSettingWindow.y + 360, 150, 50, 1.0));
                                configSettingWindow.buttons.add(new ButtonImpl("Load Config", configSettingWindow.x + 85, configSettingWindow.y + 430, 150, 50, 1.0));
                            }
                        } else if (button instanceof CloudConfigButton) {
                            if (configSettingWindow != null) {
                                configSettingWindow.buttons.clear();
                                CloudConfigButton cloudConfigButton = (CloudConfigButton) button;

                                if (cloudConfigButton.config.getCreatorName().equals(Client.INSTANCE.clientUserName)) {
                                    configSettingWindow.buttons.add(new ButtonImpl("Load Config", configSettingWindow.x + 85, configSettingWindow.y + 410, 150, 35, 1.0));
                                    configSettingWindow.buttons.add(new ButtonImpl("Delete Config", configSettingWindow.x + 85, configSettingWindow.y + 450, 150, 35, 1.0));
                                } else {
                                    configSettingWindow.buttons.add(new ButtonImpl("Load Config", configSettingWindow.x + 85, configSettingWindow.y + 440, 150, 50, 1.0));
                                }
                            }
                        }
                    }
                    button.mouseClicked();
                }
            }
        }

        if (configSettingWindow != null) {
            for (Button button : configSettingWindow.buttons) {
                button.mouseClicked();
                if (button instanceof ButtonImpl) {
                    ButtonImpl button1 = (ButtonImpl) button;

                    if (button1.hovered) {
                        clicked = button1.text;
                    }
                }
            }
        }
        switch (clicked) {
            case "Upload Config":
                if (selectedButton instanceof ConfigButton) {
                    uploadConfig(((ConfigButton) selectedButton).config);
                }else {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Please select a Valid Config.", 3000, Type.ERROR));
                }
                break;
            case "Load Config":
                if (selectedButton instanceof ConfigButton) {
                    Config config = ((ConfigButton) selectedButton).config;
                    if (config.load()) {
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Loaded config " + config.getName() + ".", 3000, Type.INFO));
                    } else {
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't load config " + config.getName() + ".", 3000, Type.ERROR));
                    }
                } else if (selectedButton instanceof CloudConfigButton) {
                    ((CloudConfigButton) selectedButton).load();
                } else {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Please select a Valid Config.", 3000, Type.ERROR));
                }
                break;
            case "Delete Config":
                if (selectedButton instanceof CloudConfigButton) {
                    deleteConfig(((CloudConfigButton) selectedButton).config.getId());
                } else {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Please select a Valid Config.", 3000, Type.ERROR));
                }
                break;
            case "Online":
                this.initGui();
                if (currentWindow == null || !currentWindow.text.startsWith("Online")) {
                    if (currentWindow == null) initGui();
                    currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Online", currentWindow.width, currentWindow.height);
                }

                ArrayList<CloudConfig> configs = getOnlineConfigs();

                int offsetOnline = 40, offsetXOnline = 0;

                for (CloudConfig cloudConfig : configs) {
                    final CloudConfigButton button = new CloudConfigButton(cloudConfig.getName(), currentWindow.x + offsetXOnline, currentWindow.y + offsetOnline, currentWindow.width - 2, 40);

                    button.description = "Made by " + cloudConfig.getCreatorName() + " on " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(cloudConfig.getLastUpdate()))) + " using " + Client.NAME + " " + Client.VERSION + " " + cloudConfig.getVersion().split("#")[0] + " (" + cloudConfig.getVersion().split("#")[1] + ")";
                    button.config = cloudConfig;
                    currentWindow.buttons.add(button);
                    offsetOnline += 45;
                }

                scrollRegion.width = currentWindow.width;
                scrollRegion.height = currentWindow.height;
                break;
            case "Local":
                this.initGui();
                if (currentWindow == null || !currentWindow.text.startsWith("Local")) {
                    assert currentWindow != null;
                    currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Local", currentWindow.width, currentWindow.height);

                    int offset = 0, offsetX = 0;
                    for (Config config : Client.INSTANCE.getConfigManager().getConfigs()) {
                        final ConfigButton button = new ConfigButton(config.getName(), currentWindow.x + offsetX, currentWindow.y + offset, 300, 40);
                        button.description = "Made by " + Client.INSTANCE.getIrcConnector().username + " on " + config.getCreationDate();
                        button.config = config;
                        currentWindow.buttons.add(button);
                        offset += 45;
                    }
                }
                scrollRegion.width = currentWindow.width;
                scrollRegion.height = currentWindow.height;
                break;
            case "Reload":
                if (currentWindow == null) break;
                switch (currentWindow.text) {
                    case "Online":
                        Client.INSTANCE.getConfigManager().initConfigs();
                        this.initGui();
                        if (currentWindow == null || !currentWindow.text.startsWith("Online")) {
                            if (currentWindow == null) initGui();
                            currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Online", currentWindow.width, currentWindow.height);
                        }

                        ArrayList<CloudConfig> configsReload = getOnlineConfigs();

                        int offsetReload = 40, offsetXReload = 0;

                        for (CloudConfig cloudConfig : configsReload) {
                            final CloudConfigButton button = new CloudConfigButton(cloudConfig.getName(), currentWindow.x + offsetXReload, currentWindow.y + offsetReload, currentWindow.width - 2, 40);

                            button.description = "Made by " + cloudConfig.getCreatorName() + " on " + simpleDateFormat.format(new Date(Long.parseLong(cloudConfig.getLastUpdate()))) + " using " + Client.NAME + " " + Client.VERSION + " " + cloudConfig.getVersion().split("#")[0] + " (" + cloudConfig.getVersion().split("#")[1] + ")";
                            button.config = cloudConfig;
                            currentWindow.buttons.add(button);
                            offsetReload += 45;
                        }

                        scrollRegion.width = currentWindow.width;
                        scrollRegion.height = currentWindow.height;
                        break;
                    case "Local":
                        Client.INSTANCE.getConfigManager().initConfigs();
                        this.initGui();
                        if (currentWindow == null || !currentWindow.text.startsWith("Local")) {
                            assert currentWindow != null;
                            currentWindow = new Window("Local", currentWindow.width, currentWindow.height);
                            int offset = 0, offsetX = 0;
                            for (Config config : Client.INSTANCE.getConfigManager().getConfigs()) {
                                final ConfigButton button = new ConfigButton(config.getName(), currentWindow.x + offsetX, currentWindow.y + offset, 300, 40);
                                button.description = "Made by " + Client.INSTANCE.getIrcConnector().username + " on " + config.getCreationDate();
                                button.config = config;
                                currentWindow.buttons.add(button);
                                offset += 45;
                            }
                        }
                        scrollRegion.width = currentWindow.width;
                        scrollRegion.height = currentWindow.height;
                        break;
                }
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (Button button : buttons) {
            button.keyTyped(typedChar, keyCode);
        }

        if (currentWindow != null) {
            currentWindow.keyTyped(typedChar, keyCode);
        }

        if (configSettingWindow != null) {
            configSettingWindow.keyTyped(typedChar, keyCode);
        }
    }

    public void deleteConfig(String Id) {
        if (Id.equals("0")) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't deleted Config! Reason: Invalid ID.", 3000, Type.ERROR));
            return;
        }

        try {
            URL uploadUrl = new URL("https://api.azura.best/config/delete");

            URLConnection urlConnection = uploadUrl.openConnection();

            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Session-Token", Crypter.encode(Client.INSTANCE.sessionToken));
            urlConnection.addRequestProperty("Config", Id);
            // Set its primary task to output.
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            JsonStreamParser jsonStreamParser = new JsonStreamParser(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            if (jsonStreamParser.hasNext()) {
                JsonObject jsonObject = jsonStreamParser.next().getAsJsonObject();
                if (jsonObject.has("success") && jsonObject.get("success").getAsBoolean()) {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Config deleted!", 3000, Type.INFO));
                    buttons.clear();
                    selectedButton = null;
                    Client.INSTANCE.getConfigManager().initConfigs();
                    this.initGui();
                    if (currentWindow == null || !currentWindow.text.startsWith("Online")) {
                        if (currentWindow == null) initGui();
                        currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Online", currentWindow.width, currentWindow.height);
                    }

                    ArrayList<CloudConfig> configsReload = getOnlineConfigs();

                    int offsetReload = 40, offsetXReload = 0;

                    for (CloudConfig cloudConfig : configsReload) {
                        final CloudConfigButton button = new CloudConfigButton(cloudConfig.getName(), currentWindow.x + offsetXReload, currentWindow.y + offsetReload, currentWindow.width - 2, 40);

                        button.description = "Made by " + cloudConfig.getCreatorName() + " on " + simpleDateFormat.format(new Date(Long.parseLong(cloudConfig.getLastUpdate()))) + " using " + Client.NAME + " " + Client.VERSION + " " + cloudConfig.getVersion().split("#")[0] + " (" + cloudConfig.getVersion().split("#")[1] + ")";
                        button.config = cloudConfig;
                        currentWindow.buttons.add(button);
                        offsetReload += 45;
                    }

                    scrollRegion.width = currentWindow.width;
                    scrollRegion.height = currentWindow.height;
                } else {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't deleted Config! Reason: " + (jsonObject.has("reason") ? jsonObject.get("reason").getAsString() : "Unknown."), 3000, Type.ERROR));
                    if (jsonObject.has("reason"))
                        if (jsonObject.get("reason").getAsString().equalsIgnoreCase("Invalid Session Token."))
                            System.exit(-1);
                }
            }
        } catch (Exception exception) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't delete Config! Reason: " + exception.getMessage(), 3000, Type.ERROR));
        }
    }

    public ArrayList<CloudConfig> getOnlineConfigs() {
        try {
            URL getConfigUrl = new URL("https://api.azura.best/config/all");

            URLConnection urlConnection = getConfigUrl.openConnection();

            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Session-Token", Crypter.encode(Client.INSTANCE.sessionToken));
            urlConnection.addRequestProperty("Product", "1");

            // Set its primary task to output.
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            JsonStreamParser jsonStreamParser = new JsonStreamParser(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            if (jsonStreamParser.hasNext()) {
                JsonObject jsonObject = jsonStreamParser.next().getAsJsonObject();

                if (jsonObject.has("success") && jsonObject.get("success").getAsBoolean() && jsonObject.has("configs")) {
                    String encrypted = jsonObject.get("configs").getAsString();
                    String decrypted = AESUtil.decrypt(encrypted, Client.INSTANCE.aesKey);

                    this.initGui();
                    if (currentWindow == null || !currentWindow.text.startsWith("Online")) {
                        assert currentWindow != null;
                        currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Online", currentWindow.width, currentWindow.height);
                    }

                    if (decrypted != null && decrypted.length() > 2) {
                        JsonArray configArrayObject = new JsonParser().parse(decrypted).getAsJsonArray();

                        ArrayList<Integer> favouriteConfigsIds = new ArrayList<>();

                        File settingsFile = new File(Client.INSTANCE.getConfigManager().getClientDirectory() + "/data", "settings.json");

                        if (settingsFile.exists()) {

                            JsonObject settingsObject = new JsonParser().parse(FileUtil.getContentFromFileAsString(settingsFile)).getAsJsonObject();

                            if (settingsObject.has("favouriteConfigs")) {
                                if (settingsObject.get("favouriteConfigs").isJsonArray()) {
                                    JsonArray jsonArray = settingsObject.getAsJsonArray("favouriteConfigs");

                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        if (!favouriteConfigsIds.contains(jsonArray.get(i).getAsJsonObject().get("id").getAsInt()))
                                            favouriteConfigsIds.add(jsonArray.get(i).getAsJsonObject().get("id").getAsInt());
                                    }
                                }
                            } else {
                                settingsObject.add("favouriteConfigs", new JsonArray());
                            }

                            FileUtil.writeContentToFile(settingsFile, new GsonBuilder().setPrettyPrinting().create().toJson(settingsObject), true);
                        } else {
                            JsonObject settingsObject = new JsonObject();

                            settingsObject.add("favouriteConfigs", new JsonArray());

                            FileUtil.writeContentToFile(settingsFile, new GsonBuilder().setPrettyPrinting().create().toJson(settingsObject), true);
                        }

                        ArrayList<CloudConfig> allConfigs = new ArrayList<>();
                        ArrayList<CloudConfig> favouriteConfigs = new ArrayList<>();
                        ArrayList<CloudConfig> verifiedConfigs = new ArrayList<>();
                        ArrayList<CloudConfig> defaultConfigs = new ArrayList<>();

                        for (int i = 0; i < configArrayObject.size(); i++) {
                            JsonObject configOnline = configArrayObject.get(i).getAsJsonObject();

                            JsonObject configOnlineUser = configOnline.getAsJsonObject("user");
                            JsonObject configOnlineProperties = configOnline.getAsJsonObject("properties");
                            JsonObject configOnlineInfo = configOnline.getAsJsonObject("info");

                            CloudConfig cloudConfig = new CloudConfig(configOnlineProperties.get("name").getAsString(), configOnlineProperties.get("id").getAsString(), configOnlineProperties.get("version").getAsString(), configOnlineProperties.get("lastUpdate").getAsString(), configOnlineUser.get("id").getAsString(), configOnlineUser.get("username").getAsString(), configOnlineUser.get("rank").getAsString(), configOnlineInfo,null);

                            if (favouriteConfigsIds.contains(Integer.valueOf(cloudConfig.getId()))) {
                                cloudConfig.setFavourite(true, false);
                                favouriteConfigs.add(cloudConfig);
                            } else if (cloudConfig.getCreatorRank().equals("99") || cloudConfig.getCreatorRank().equals("100")) {
                                verifiedConfigs.add(cloudConfig);
                            } else {
                                defaultConfigs.add(cloudConfig);
                            }
                        }

                        allConfigs.addAll(favouriteConfigs);
                        allConfigs.addAll(verifiedConfigs);
                        allConfigs.addAll(defaultConfigs);

                        return allConfigs;
                    }
                } else {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't get Configs from the API! Reason: " + (jsonObject.has("reason") ? jsonObject.get("reason").getAsString() : "Unknown."), 3000, Type.ERROR));
                }
            }
        } catch (Exception exception) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't get Configs from the API! Reason: " + exception.getMessage(), 3000, Type.ERROR));
        }

        return new ArrayList<>();
    }

    public void uploadConfig(Config config) {
        if (config == null) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Please select a Valid Config.", 3000, Type.ERROR));
            return;
        }

        try {
            URL uploadUrl = new URL("https://api.azura.best/config/upload");

            URLConnection urlConnection = uploadUrl.openConnection();

            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Session-Token", Crypter.encode(Client.INSTANCE.sessionToken));

            // Set its primary task to output.
            urlConnection.setDoOutput(true);

            JsonObject configUploadObject = new JsonObject();

            JsonObject propertiesObject = new JsonObject();

            JsonObject configInfoObject = new JsonObject();

            configSettingWindow.buttons.stream().filter(button -> button instanceof RatioButtonImpl).forEach(button -> {
                RatioButtonImpl ratioButton = (RatioButtonImpl)button;
                switch (ratioButton.text) {
                    case "Killaura":
                        configInfoObject.addProperty("killaura", ratioButton.value);
                        break;
                    case "Velocity":
                        configInfoObject.addProperty("velocity", ratioButton.value);
                        break;
                    case "Speed":
                        configInfoObject.addProperty("speed", ratioButton.value);
                        break;
                    case "Fly":
                        configInfoObject.addProperty("fly", ratioButton.value);
                        break;
                    case "Scaffold":
                        configInfoObject.addProperty("scaffold", ratioButton.value);
                        break;
                    case "Noslow":
                        configInfoObject.addProperty("noslow", ratioButton.value);
                        break;
                    case "Nofall":
                        configInfoObject.addProperty("nofall", ratioButton.value);
                        break;
                }
            });

            configInfoObject.addProperty("note", "");

            propertiesObject.add("info", configInfoObject);
            propertiesObject.addProperty("product", 1);
            propertiesObject.addProperty("version", Client.RELEASE + "#" + Client.releaseBuild);
            propertiesObject.addProperty("name", config.getName());
            propertiesObject.addProperty("privacy", 0);

            configUploadObject.add("properties", propertiesObject);
            configUploadObject.add("config", config.buildJson());

            byte[] out = new GsonBuilder().create().toJson(configUploadObject).getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Set its primary task to output.
            urlConnection.setDoOutput(true);

            urlConnection.addRequestProperty("Content-Length", "" + length);
            urlConnection.connect();
            try (OutputStream os = urlConnection.getOutputStream()) {
                os.write(out);
            }

            JsonStreamParser jsonStreamParser = new JsonStreamParser(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            if (jsonStreamParser.hasNext()) {
                JsonObject jsonObject = jsonStreamParser.next().getAsJsonObject();
                if (jsonObject.has("success") && jsonObject.get("success").getAsBoolean()) {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Config uploaded!", 3000, Type.INFO));
                } else {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't upload! Reason: " + (jsonObject.has("reason") ? jsonObject.get("reason").getAsString() : "Unknown."), 3000, Type.ERROR));
                    if (jsonObject.has("reason"))
                        if (jsonObject.get("reason").getAsString().equalsIgnoreCase("Invalid Session Token."))
                            System.exit(-1);
                }
            }
        } catch (Exception exception) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't upload! Reason: " + exception.getMessage(), 3000, Type.ERROR));
        }
    }
}