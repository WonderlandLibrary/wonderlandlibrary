// 
// Decompiled by Procyon v0.5.36
// 

package moonsense;

import java.util.regex.Pattern;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.ThemeSettings;
import net.minecraft.client.gui.GuiChat;
import moonsense.ui.screen.settings.AbstractSettingsGui;
import moonsense.utils.BoxUtils;
import moonsense.features.SCModule;
import moonsense.features.SCAbstractRenderModule;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.config.ModuleConfig;
import moonsense.ui.utils.blur.BlurShader;
import moonsense.features.modules.type.mechanic.SnaplookModule;
import moonsense.event.impl.SCRenderEvent;
import moonsense.event.impl.SCGuiScreenPostRenderEvent;
import moonsense.features.modules.type.mechanic.ItemPhysicsModule;
import moonsense.event.impl.SCUpdateEvent;
import moonsense.notifications.NotificationColor;
import moonsense.notifications.NotificationType;
import moonsense.ui.screen.settings.GuiHUDEditor;
import net.minecraft.client.gui.GuiScreen;
import moonsense.ui.screen.settings.GuiModules;
import moonsense.features.SettingsManager;
import moonsense.utils.KeyBinding;
import moonsense.event.impl.SCKeyEvent;
import moonsense.event.impl.SCWorldUnloadedEvent;
import moonsense.event.SubscribeEvent;
import moonsense.event.impl.SCWorldLoadedEvent;
import moonsense.cosmetics.CosmeticsManager;
import moonsense.cosmetics.impl.obj.OBJBandana;
import moonsense.cosmetics.obj.handler.OBJCosmeticHandler;
import moonsense.features.ModuleManager;
import moonsense.account.Account;
import java.io.IOException;
import moonsense.integrations.spotify.SpotifyManager;
import moonsense.config.ConfigManager;
import moonsense.config.utils.Config;
import moonsense.integrations.discord.DiscordIPC;
import moonsense.account.AccountLoginThread;
import java.net.UnknownHostException;
import java.net.SocketException;
import org.apache.logging.log4j.LogManager;
import moonsense.utils.ColorObject;
import moonsense.network.websocket.implementation.UDPSocketClient;
import moonsense.utils.SpotifyData;
import moonsense.featuredservers.FeaturedServerManager;
import moonsense.cosmetics.obj.OBJCosmeticLoader;
import moonsense.features.modules.type.server.hypixel.ReplayManager;
import moonsense.integrations.spotify.FavoriteSongManager;
import moonsense.features.modules.type.world.waypoints.WaypointManager;
import moonsense.notifications.NotificationManager;
import moonsense.event.EventBus;
import moonsense.account.AccountManager;
import java.util.Random;
import moonsense.features.modules.utils.CPSUtils;
import moonsense.utils.CustomFontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.io.File;
import moonsense.enums.DevelopmentStage;
import org.apache.logging.log4j.Logger;

public class MoonsenseClient
{
    public static final MoonsenseClient INSTANCE;
    public static final Logger LOGGER;
    public static final String minecraftVersion = "1.8";
    public static final String clientVersion = "1.0.1";
    public static final DevelopmentStage developmentStage;
    public static final File dir;
    private final Minecraft mc;
    public static final ResourceLocation CLIENT_LOGO;
    public static final ResourceLocation CLIENT_WATERMARK_LOGO;
    public static final ResourceLocation NAMETAG;
    public static final ResourceLocation BACKGROUND;
    public static final ResourceLocation TRANSPARENT;
    public static CustomFontRenderer titleRenderer;
    public static CustomFontRenderer titleRenderer2;
    public static CustomFontRenderer textRenderer;
    public static CustomFontRenderer titleRendererLarge;
    public static CustomFontRenderer titleRenderer2Large;
    public static CustomFontRenderer watermarkRenderer1;
    public static CustomFontRenderer watermarkRenderer2;
    public static CustomFontRenderer tinyTextRenderer;
    public static final CPSUtils left;
    public static final CPSUtils right;
    public final Random RANDOM;
    private AccountManager accountManager;
    private EventBus eventManager;
    private NotificationManager notificationManager;
    private WaypointManager waypointManager;
    private FavoriteSongManager songManager;
    private ReplayManager replayManager;
    private OBJCosmeticLoader objCosmeticLoader;
    private FeaturedServerManager featuredServerManager;
    private final SpotifyData spotifyData;
    private UDPSocketClient socketClient;
    private boolean spotifyLoggedIn;
    public boolean hasSentClientStart;
    public String prevUsernameToUpdate;
    public ColorObject clipboardColor;
    
    static {
        developmentStage = DevelopmentStage.BETA;
        INSTANCE = new MoonsenseClient();
        LOGGER = LogManager.getLogger();
        dir = new File(Minecraft.getMinecraft().mcDataDir, "Moonsense Client");
        CLIENT_LOGO = new ResourceLocation("streamlined/moonsense.png");
        CLIENT_WATERMARK_LOGO = new ResourceLocation("streamlined/logo 1.png");
        NAMETAG = new ResourceLocation("streamlined/moonsense.png");
        BACKGROUND = new ResourceLocation("streamlined/bg.png");
        TRANSPARENT = new ResourceLocation("streamlined/transparent.png");
        left = new CPSUtils(CPSUtils.Type.LEFT);
        right = new CPSUtils(CPSUtils.Type.RIGHT);
    }
    
    public MoonsenseClient() {
        this.RANDOM = new Random();
        this.spotifyData = new SpotifyData();
        this.spotifyLoggedIn = false;
        this.hasSentClientStart = false;
        this.prevUsernameToUpdate = "";
        this.mc = Minecraft.getMinecraft();
    }
    
    public void onPreInit() {
        System.setProperty("log4j2.formatMsgNoLookups", "true");
        try {
            this.socketClient = new UDPSocketClient();
        }
        catch (SocketException | UnknownHostException ex2) {
            final IOException ex;
            final IOException e1 = ex;
            e1.printStackTrace();
        }
        new Thread("Webserver Checker") {
            @Override
            public void run() {
                while (true) {
                    MoonsenseClient.INSTANCE.getSocketClient().pingServer();
                    try {
                        Thread.sleep(60000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        this.featuredServerManager = new FeaturedServerManager().init();
        this.notificationManager = new NotificationManager();
        this.eventManager = new EventBus();
        EventBus.register(this);
        (this.accountManager = new AccountManager(new File("Moonsense Client"))).load();
        final Account acc = this.accountManager.getLastAlt();
        if (acc != null && acc != null) {
            try {
                final AccountLoginThread lt = new AccountLoginThread(acc.getEmail(), acc.getPassword());
                lt.start();
            }
            catch (Exception e3) {
                System.out.println("Failed to load last alt.");
            }
        }
        try {
            DiscordIPC.INSTANCE.init();
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        try {
            ConfigManager.INSTANCE.configs.get(1).loadConfig();
            ConfigManager.INSTANCE.configs.get(2).loadConfig();
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        try {
            (this.waypointManager = new WaypointManager()).load();
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        try {
            (this.songManager = new FavoriteSongManager()).load();
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        try {
            (this.replayManager = new ReplayManager()).load();
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        SpotifyManager.init();
    }
    
    public void onPostInit() {
        try {
            ModuleManager.INSTANCE.init();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            ConfigManager.INSTANCE.configs.get(0).loadConfig();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        this.objCosmeticLoader = new OBJCosmeticLoader();
        CosmeticsManager.registerCosmetics(new OBJBandana());
        MoonsenseClient.titleRenderer = new CustomFontRenderer("Raleway Bold", 16.0f);
        MoonsenseClient.titleRenderer2 = new CustomFontRenderer("Raleway Black", 16.0f);
        MoonsenseClient.titleRendererLarge = new CustomFontRenderer("Raleway Bold", 25.0f);
        MoonsenseClient.titleRenderer2Large = new CustomFontRenderer("Raleway Black", 25.0f);
        MoonsenseClient.textRenderer = new CustomFontRenderer("Raleway Light", 16.0f);
        MoonsenseClient.watermarkRenderer1 = new CustomFontRenderer("Raleway Bold", 38.0f);
        MoonsenseClient.watermarkRenderer2 = new CustomFontRenderer("Raleway Light", 38.0f);
        MoonsenseClient.tinyTextRenderer = new CustomFontRenderer("Raleway Light", 10.0f);
        if (this.mc.session.getToken() != null && !this.mc.session.getToken().equalsIgnoreCase("0") && this.socketClient.isRunning()) {
            this.socketClient.updateSelf(true);
            this.hasSentClientStart = true;
        }
    }
    
    @SubscribeEvent
    public void onWorldLoaded(final SCWorldLoadedEvent event) {
    }
    
    @SubscribeEvent
    public void onWorldUnloaded(final SCWorldUnloadedEvent event) {
    }
    
    @SubscribeEvent
    public void onKeyPress(final SCKeyEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && event.getKey() == SettingsManager.INSTANCE.hudEditorKeybind.getValue().get(0).getKeyCode()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiHUDEditor(new GuiModules(null)));
        }
        if (event.getKey() == 25) {
            this.notificationManager.createNotification("Test Notification", "This is a test notification!", true, 5000L, NotificationType.INFO, NotificationColor.GREEN);
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final SCUpdateEvent event) {
        if (this.mc.thePlayer != null && this.mc.theWorld != null && this.mc.session.getToken() != null && !this.mc.session.getToken().equalsIgnoreCase("0") && !this.hasSentClientStart && this.socketClient.isRunning()) {
            if (this.prevUsernameToUpdate.isEmpty()) {
                this.socketClient.updateSelf(true);
                this.hasSentClientStart = true;
            }
            else {
                this.socketClient.updateUserWithPacket(this.prevUsernameToUpdate, false);
                this.socketClient.updateSelf(true);
                this.hasSentClientStart = true;
                this.prevUsernameToUpdate = "";
            }
        }
        ItemPhysicsModule.INSTANCE.onUpdate();
    }
    
    @SubscribeEvent
    public void onPostGuiRender(final SCGuiScreenPostRenderEvent event) {
    }
    
    @SubscribeEvent
    public void onRenderOverlay(final SCRenderEvent event) {
        if (this.mc.gameSettings.showDebugInfo || this.mc.currentScreen instanceof GuiHUDEditor) {
            return;
        }
        SnaplookModule.INSTANCE.onTick();
        BlurShader.INSTANCE.onRenderTick();
        ModuleManager.INSTANCE.modules.stream().filter(module -> ModuleConfig.INSTANCE.isEnabled(module) && module.isRender()).forEach(module -> {
            GlStateManager.pushMatrix();
            GlStateManager.scale(module.getScale(), module.getScale(), 1.0f);
            GlStateManager.translate(BoxUtils.getBoxOffX(module, (int)ModuleConfig.INSTANCE.getActualX(module), module.getWidth()) / module.getScale(), BoxUtils.getBoxOffY(module, (int)ModuleConfig.INSTANCE.getActualY(module), module.getHeight()) / module.getScale(), 0.0f);
            if (this.mc.currentScreen instanceof AbstractSettingsGui) {
                module.renderDummy(0.0f, 0.0f);
                GlStateManager.popMatrix();
                return;
            }
            else {
                if (this.mc.currentScreen instanceof GuiChat) {
                    if (module.showWhileTyping.getBoolean()) {
                        module.render(0.0f, 0.0f);
                    }
                }
                else {
                    module.render(0.0f, 0.0f);
                }
                GlStateManager.popMatrix();
                return;
            }
        });
        MoonsenseClient.left.tick();
        MoonsenseClient.right.tick();
        this.notificationManager.onRender();
    }
    
    public void onShutdown() {
        DiscordIPC.INSTANCE.shutdown();
        if (this.mc.session.getToken() != null && !this.mc.session.getToken().equalsIgnoreCase("0") && this.socketClient.isRunning()) {
            this.socketClient.updateSelf(false);
        }
        ConfigManager.INSTANCE.saveAll();
        this.waypointManager.save();
        this.songManager.save();
    }
    
    public static int getMainColor(final int alpha) {
        return GuiUtils.getRGB(ThemeSettings.INSTANCE.mainColor.getColor(), alpha);
    }
    
    public static int getBrandingColor(final int alpha) {
        return GuiUtils.getRGB(ThemeSettings.INSTANCE.brandingColor.getColor(), alpha);
    }
    
    public static void info(final Object msg, final Object... objs) {
        MoonsenseClient.LOGGER.info("$[Moonsense Client] " + msg, objs);
    }
    
    public static void debug(final Object msg, final Object... objs) {
        MoonsenseClient.LOGGER.debug("$[Moonsense Client] " + msg, objs);
    }
    
    public static void warn(final Object msg, final Object... objs) {
        MoonsenseClient.LOGGER.warn("$[Moonsense Client] " + msg, objs);
    }
    
    public static void error(final Object msg, final Object... objs) {
        MoonsenseClient.LOGGER.error("$[Moonsense Client] " + msg, objs);
    }
    
    public static Pattern getSearchPattern(final String text) {
        return Pattern.compile("\\Q" + text.replace("*", "\\E.*\\Q") + "\\E", 2);
    }
    
    public static String getVersion() {
        String v = "1.0.0";
        if (MoonsenseClient.developmentStage != DevelopmentStage.PRODUCTION) {
            v = String.valueOf(v) + "-" + MoonsenseClient.developmentStage.stage;
        }
        return v;
    }
    
    public NotificationManager getNotificationManager() {
        return this.notificationManager;
    }
    
    public EventBus getEventManager() {
        return this.eventManager;
    }
    
    public AccountManager getAccountManager() {
        return this.accountManager;
    }
    
    public OBJCosmeticLoader getCosmeticLoader() {
        return this.objCosmeticLoader;
    }
    
    public SpotifyData getSpotifyData() {
        return this.spotifyData;
    }
    
    public boolean isSpotifyLoggedIn() {
        return this.spotifyLoggedIn;
    }
    
    public void setSpotifyLoggedIn(final boolean spotifyLoggedIn) {
        this.spotifyLoggedIn = spotifyLoggedIn;
    }
    
    public WaypointManager getWaypointManager() {
        return this.waypointManager;
    }
    
    public FavoriteSongManager getSongManager() {
        return this.songManager;
    }
    
    public FeaturedServerManager getFeaturedServerManager() {
        return this.featuredServerManager;
    }
    
    public UDPSocketClient getSocketClient() {
        return this.socketClient;
    }
    
    public ReplayManager getReplayManager() {
        return this.replayManager;
    }
}
