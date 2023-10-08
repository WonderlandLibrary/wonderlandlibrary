/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise;

import dev.rise.anticheat.AntiCheat;
import dev.rise.command.CommandManager;
import dev.rise.creative.RiseTab;
import dev.rise.module.Module;
import dev.rise.module.impl.other.AutoAuthme;
import dev.rise.module.manager.ModuleManager;
import dev.rise.notifications.NotificationManager;
import dev.rise.notifications.NotificationType;
import dev.rise.script.ScriptManager;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.ui.alt.AltGUI;
import dev.rise.ui.altmanager.AltAccount;
import dev.rise.ui.altmanager.gui.AltManagerGUI;
import dev.rise.ui.clickgui.impl.ClickGUI;
import dev.rise.ui.clickgui.impl.strikeless.StrikeGUI;
import dev.rise.ui.guitheme.GuiTheme;
import dev.rise.ui.guitheme.Theme;
import dev.rise.ui.version.VersionGui;
import dev.rise.util.misc.FileUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.render.BlurUtil;
import dev.rise.util.render.theme.ThemeUtil;
import dev.rise.viamcp.ViaMCP;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import store.intent.api.account.IntentAccount;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class for our client, this is where we store all of our managers and client information.
 * When the client starts or stops this class will handle the operations needed.
 *
 * @author Tecnio
 * @since 02/11/2021
 */
@Getter
public enum Rise {

    // Our enum instance for our class
    INSTANCE;

    // The users Intent account
    public static IntentAccount intentAccount;

    // BlurUtil
    public static final BlurUtil BLUR_UTIL = new BlurUtil();

    // Client information
    public static String CLIENT_NAME = "Rise", CLIENT_VERSION = "Lucas x Almir Edition";
    public static int CLIENT_THEME_COLOR_DEFAULT = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR_BRIGHT = new Color(185, 69, 255).hashCode();
    public static Color CLIENT_THEME_COLOR_BRIGHT_COLOR = new Color(185, 69, 255);

    // yes static die
    public static String ip;
    public static int port;

    // Vars
    public static boolean lol = false;

    //Statistics
    public static long timeJoinedServer;
    public static int totalKills;
    public static int totalDeaths;
    public static float distanceRan;
    public static float distanceFlew;
    public static float distanceJumped;
    public static int amountOfModulesOn;
    public static int amountOfVoidSaves;
    public static int amountOfConfigsLoaded;
    public static String lastLoggedAccount;

    // Friends
    private final List<String> friends = new ArrayList<>();

    // Targets
    private final List<String> targets = new ArrayList<>();

    // Threading
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ExecutorService onlineConfigExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService onlineScriptExecutor = Executors.newSingleThreadExecutor();

    //guis
    public VersionGui versionSwitcher = null;
    public String kingGenApi = "";

    // General stuff
    public ModuleManager moduleManager;
    public NotificationManager notificationManager;
    public CommandManager cmdManager;
    public ScriptManager scriptManager;

    // Misc
    private DynamicTexture viewportTexture;
    private ResourceLocation backgroundTexture;

    // ClickGUIs
    public ClickGUI clickGUI;
    public StrikeGUI strikeGUI;
    public AltGUI altGUI;
    public AltManagerGUI altManagerGUI;
    public boolean firstBoot;
    public GuiMultiplayer guiMultiplayer;
    public GuiScreen guiMainMenu;

    // Misc
    public boolean viaHasFailed;
    public AntiCheat antiCheat;
    public GuiTheme guiTheme;
    public RiseTab creativeTab;
    public boolean destructed;

    /**
     * Adds a message to the players chat without sending it to the server.
     *
     * @param message The message that is gonna be added to chat.
     */
    public static void addChatMessage(final Object message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§d" + Rise.CLIENT_NAME + "§7 » " + message));
        }
    }

    /**
     * Adds a message with a hover message to the players chat without sending it to the server.
     *
     * @param message      The message that is gonna be added to chat.
     * @param hoverMessage The hover message that is gonna be added to the message.
     */
    public static void addChatMessage(final Object message, final Object hoverMessage) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            final ChatStyle hoverStyle = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
                    EnumChatFormatting.GRAY + hoverMessage.toString().replace("\n", "\n" + EnumChatFormatting.GRAY)
            )));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§d" + Rise.CLIENT_NAME + "§7 » " + message).setChatStyle(hoverStyle));
        }
    }

    /**
     * When the clients get started this method gets called.
     * We can handle the stuff that we want to handle on start here.
     */
    /*public void start() {
        final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        final List<String> arguments = runtimeMxBean.getInputArguments();

        if (lol) {
            //ircClient = new IRCClient();
            moduleManager = new ModuleManager();
            notificationManager = new NotificationManager();
            cmdManager = new CommandManager();
            clickGUI = new ClickGUI();
            dropdownGui = new DropdownGui();
            altGUI = new AltGUI();
            altManagerGUI = new AltManagerGUI();
            alansPathfinder = new AlansPathUtil();
            antiCheat = new AntiCheat();
            guiTheme = new GuiTheme();
            scriptManager = new ScriptManager();
        }

        final Minecraft mc = Minecraft.getMinecraft();

        //Compatibility
        mc.gameSettings.guiScale = 2;
        mc.gameSettings.ofFastRender = false;

        //Performance settings
        mc.gameSettings.ofSmartAnimations = true;
        mc.gameSettings.ofSmoothFps = false;
        mc.gameSettings.ofFastMath = false;

        if (!validateUser()) {
            validateUser();
        }

        // Creating Rise folder
        if (!FileUtil.riseDirectoryExists()) {
            firstBoot = true;
            FileUtil.createRiseDirectory();
        }

        if (!FileUtil.exists("Config" + File.separator)) {
            FileUtil.createDirectory("Config" + File.separator);
        }

        if (!FileUtil.exists("Script" + File.separator)) {
            FileUtil.createDirectory("Script" + File.separator);
        }


        final AtomicBoolean viaInit = new AtomicBoolean(false);
        final AtomicBoolean viaError = new AtomicBoolean(false);

        final Thread thread = new Thread(() -> {
            try {
                ViaMCP.getInstance().start();
            } catch (final Throwable t) {
                viaError.set(true);
                t.printStackTrace();
            }

            viaInit.set(true);
        });

        thread.setName("Version Switcher Initializer Thread");

        final long start = System.currentTimeMillis();
        thread.start();

        while (!viaInit.get()) {
            final long delta = Math.abs(System.currentTimeMillis() - start);

            if (delta > 20000) {
                viaError.set(true);
                break;
            }
        }

        if (viaError.get()) viaHasFailed = true;

        loadClient();
    }*/

    /**
     * When the client gets shut down this method gets called.
     * We can handle the stuff that we want to handle on stop here.
     */
    public void stop() {
        saveClient();
    }

    /**
     * Called when the client initializes the texture engine.
     */
    public void textureStart() {
        viewportTexture = new DynamicTexture(256, 256);
        backgroundTexture = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
    }

    /**
     * When a chat message is sent this method ges called and we can handle our stuff here.
     *
     * @param s The chat message that is sent by the player.
     */
    public boolean onSendChatMessage(final String s) {
        if (s.toLowerCase().contains("rise") && PlayerUtil.isOnServer("hypixel")) {
            Rise.addChatMessage("In order to prevent staff from working out which clients bypass," +
                    " Rise has hidden this message, this allows Rise to bypass for longer and better thank you for understanding.");
            return false;
        }

        if (s.startsWith(".") && !Rise.INSTANCE.destructed) {
            cmdManager.callCommand(s.substring(1));
            return false;
        }

        //ircClient.write(s.substring(1));
        return !s.startsWith("-");
    }

    /**
     * This method gets ran to ensure the player is not running the client without a license.
     * This is a basic but useful way of getting rid of basic jar sharing.
     *
     * @return Returns true if the player has been validated.
     */

    /**
     * Saves basic information that needs to be stored on a file.
     */
    public void saveClient() {
        saveConfig();
        saveStatistics();
        saveLogins();
        saveAlts();
    }

    /**
     * This method loads the settings we need from the last launch.
     * This allows us to keep settings etc.
     */
    public void loadClient() {
        loadConfig();
        loadStatistics();
        loadLogins();
        loadAlts();
        ViaMCP.getInstance().start();
    }

    private void loadConfig() {
        final String config = FileUtil.loadFile("settings.txt");
        if (config == null) return;
        final String[] configLines = config.split("\r\n");

        for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (m.isEnabled()) {
                m.toggleNoEvent();
            }
        }

        for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (m.isEnabled()) {
                m.toggleNoEvent();
            }
        }

        boolean gotConfigVersion = false;
        for (final String line : configLines) {
            if (line == null) return;

            final String[] split = line.split("_");
            if (split[0].contains("Rise")) {
                if (split[1].contains("Version")) {
                    gotConfigVersion = true;

                    final String clientVersion = CLIENT_VERSION;
                    final String configVersion = split[2];

                    if (!clientVersion.equalsIgnoreCase(configVersion)) {
                        addChatMessage("This config was made in a different version of Rise! Incompatibilities are expected.");
                        getNotificationManager().registerNotification(
                                "This config was made in a different version of Rise! Incompatibilities are expected.", NotificationType.WARNING
                        );
                    }
                }
            }

            if (split[0].contains("MainMenuTheme")) {
                getGuiTheme().setCurrentTheme(Theme.valueOf(split[1]));
                continue;
            }

            if (split[0].contains("ClientName")) {
                ThemeUtil.setCustomClientName(split.length > 1 ? split[1] : "");
                continue;
            }

            if (split[0].contains("PlayMusic")) {
                Minecraft.getMinecraft().riseMusicTicker.shouldKeepPlaying = Boolean.parseBoolean(split[1]);
                continue;
            }

            if (split[0].contains("Toggle")) {
                if (split[2].contains("true")) {
                    if (getModuleManager().getModule(split[1]) != null) {
                        final Module module = Objects.requireNonNull(getModuleManager().getModule(split[1]));

                        if (!module.isEnabled()) {
                            module.toggleNoEvent();
                        }
                    }
                }
            }

            final Setting setting = getModuleManager().getSetting(split[1], split[2]);

            if (split[0].contains("BooleanSetting") && setting instanceof BooleanSetting) {
                if (split[3].contains("true")) {
                    ((BooleanSetting) setting).enabled = true;
                }

                if (split[3].contains("false")) {
                    ((BooleanSetting) setting).enabled = false;
                }
            }

            if (split[0].contains("NumberSetting") && setting instanceof NumberSetting)
                ((NumberSetting) setting).setValue(Double.parseDouble(split[3]));

            if (split[0].contains("ModeSetting") && setting instanceof ModeSetting)
                ((ModeSetting) setting).set(split[3]);

            if (split[0].contains("Bind")) {
                final Module m = getModuleManager().getModule(split[1]);

                if (m != null)
                    Objects.requireNonNull(m).setKeyBind(Integer.parseInt(split[2]));
            }
        }
        if (!gotConfigVersion) {
            addChatMessage("This config was made in a different version of Rise! Incompatibilities are expected.");
            getNotificationManager().registerNotification(
                    "This config was made in a different version of Rise! Incompatibilities are expected.", NotificationType.WARNING
            );
        }
    }

    private void loadStatistics() {
        final String statistics = FileUtil.loadFile("statistics.txt");
        if (statistics == null) return;

        final String[] statisticsLines = statistics.split("\r\n");

        for (final String line : statisticsLines) {
            if (line == null) return;

            final String[] split = line.split("_");

            if (split[0].contains("Kills"))
                totalKills = Integer.parseInt(split[1]);

            if (split[0].contains("Deaths"))
                totalDeaths = Integer.parseInt(split[1]);

            if (split[0].contains("DistanceRan"))
                distanceRan = Float.parseFloat(split[1]);

            if (split[0].contains("DistanceFlew"))
                distanceFlew = Float.parseFloat(split[1]);

            if (split[0].contains("DistanceJumped"))
                distanceJumped = Float.parseFloat(split[1]);

            if (split[0].contains("VoidSaves"))
                amountOfVoidSaves = Integer.parseInt(split[1]);

            if (split[0].contains("ConfigsLoaded"))
                amountOfConfigsLoaded = Integer.parseInt(split[1]);
        }
    }

    private void loadLogins() {
        AutoAuthme.loadLogins();
        // Add normal logins here once an alt list is implemented.
    }

    private void loadAlts() {
        final String fileAlts = FileUtil.loadFile("alts.txt");

        if (fileAlts == null) return;

        final String[] alts = fileAlts.split("\r\n");

        altManagerGUI.altList.clear();
        altGUI.altList.clear();

        for (final String line : alts) {
            if (line == null) return;

            if (line.contains(":")) {
                final String[] alt = line.split(":");

                if (alt[1].equalsIgnoreCase(" ")) {
                    altManagerGUI.altList.add(new AltAccount(alt[0], ""));
                    altGUI.altList.add(new AltAccount(alt[0], ""));
                } else {
                    final AltAccount altAccount = new AltAccount(alt[0], alt[1]);

                    altAccount.setUsername(alt[2]);

                    altManagerGUI.altList.add(altAccount);
                    altGUI.altList.add(altAccount);
                }
            }
        }
    }

    public static void saveAlts() {
        final StringBuilder altsBuilder = new StringBuilder();

        for (final AltAccount alt : INSTANCE.getAltManagerGUI().altList) {
            if (alt.isCracked()) {
                altsBuilder.append(alt.getEmail()).append(":").append(" ").append("\r\n");
            } else {
                altsBuilder.append(alt.getEmail()).append(":").append(alt.getPassword()).append(":").append(alt.getUsername()).append("\r\n");
            }
        }

        FileUtil.saveFile("alts.txt", true, altsBuilder.toString());
    }

    private void saveConfig() {
        final StringBuilder configBuilder = new StringBuilder();
        configBuilder.append("Rise_Version_").append(CLIENT_VERSION).append("\r\n");
        configBuilder.append("PlayMusic_").append(Minecraft.getMinecraft().riseMusicTicker.shouldKeepPlaying).append("\r\n");
        configBuilder.append("MainMenuTheme_").append(getGuiTheme().getCurrentTheme()).append("\r\n");
        configBuilder.append("ClientName_").append(ThemeUtil.getCustomClientName()).append("\r\n");

        for (final Module m : getModuleManager().getModuleList()) {
            final String moduleName = m.getModuleInfo().name();
            configBuilder.append("Toggle_").append(moduleName).append("_").append(m.isEnabled()).append("\r\n");

            for (final Setting s : m.getSettings()) {
                if (s instanceof BooleanSetting) {
                    configBuilder.append("BooleanSetting_").append(moduleName).append("_").append(s.name).append("_").append(((BooleanSetting) s).enabled).append("\r\n");
                }
                if (s instanceof NumberSetting) {
                    configBuilder.append("NumberSetting_").append(moduleName).append("_").append(s.name).append("_").append(((NumberSetting) s).value).append("\r\n");
                }
                if (s instanceof ModeSetting) {
                    configBuilder.append("ModeSetting_").append(moduleName).append("_").append(s.name).append("_").append(((ModeSetting) s).getMode()).append("\r\n");
                }
            }
            configBuilder.append("Bind_").append(moduleName).append("_").append(m.getKeyBind()).append("\r\n");
        }

        FileUtil.saveFile("settings.txt", true, configBuilder.toString());
    }

    private void saveStatistics() {
        final String statisticsBuilder = "Kills_" + totalKills + "\r\n" +
                "Deaths_" + totalDeaths + "\r\n" +
                "DistanceRan_" + distanceRan + "\r\n" +
                "DistanceFlew_" + distanceFlew + "\r\n" +
                "DistanceJumped_" + distanceJumped + "\r\n" +
                "VoidSaves_" + amountOfVoidSaves + "\r\n" +
                "ConfigsLoaded_" + amountOfConfigsLoaded + "\r\n";
        FileUtil.saveFile("statistics.txt", true, statisticsBuilder);
    }

    private void saveLogins() {
        AutoAuthme.saveLogins();
        // Add normal logins here once an alt list is implemented.
    }
}
