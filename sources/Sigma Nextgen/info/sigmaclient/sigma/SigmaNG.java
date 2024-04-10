package info.sigmaclient.sigma;

import info.sigmaclient.sigma.utils.anticrack.AntiAgent;
import info.sigmaclient.sigma.utils.anticrack.MiscAntiCrack;
import info.sigmaclient.sigma.utils.render.rendermanagers.TextureObf;
import info.sigmaclient.sigma.config.FriendsManager;
import info.sigmaclient.sigma.modules.ModuleManager;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.utils.UpdateLogs;
import info.sigmaclient.sigma.sigma5.jelloportal.vialoader.ViaLoader;
import info.sigmaclient.sigma.sigma5.utils.BlurUtils;
import info.sigmaclient.sigma.sigma5.utils.Sigma5BlurUtils;
import info.sigmaclient.sigma.config.ConfigManager;
import info.sigmaclient.sigma.event.EventManager;
import info.sigmaclient.sigma.premium.PremiumManager;
import info.sigmaclient.sigma.scripts.ScriptModuleManager;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.commands.CommandManager;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.minimap.XaeroMinimap;
import lombok.Getter;
import top.fl0wowp4rty.phantomshield.annotations.Native;
import top.fl0wowp4rty.phantomshield.annotations.Native;

import java.io.IOException;
public class SigmaNG {
    public AntiAgent antiAgent = new AntiAgent();
    public static String getClientName(){
        return "Sigma-nextgen";
    }
    public static String getClientVersion(){
        return "13.7";
    }
    public static String getClientAuthor(){
        return "IamFrozenMilk, UneCroissantine, IamFrozenNeko";
    }
    @Getter
    public static SigmaNG SigmaNG;
    @Getter
    public static Sigma SIGMA;
    public static float lineWidth = 1.0F;
    public static boolean betterResPack = true;
    public boolean guiBlur = false;
    public boolean inGameGuiBlur = false;
    public XaeroMinimap minimap = new XaeroMinimap();
    public MiscAntiCrack verify = new MiscAntiCrack();
    public ModuleManager moduleManager = new ModuleManager();
    public ConfigManager configManager = new ConfigManager();
    public ScriptModuleManager scriptModuleManager = new ScriptModuleManager();
    public CommandManager commandManager = new CommandManager();
    public FriendsManager friendsManager = new FriendsManager();
    public PremiumManager premiumManager = new PremiumManager();
    public NotificationManager notificationManager = new NotificationManager();
    public static GAME_MODE gameMode = GAME_MODE.SIGMA;
    public GAME_MODE gameMode2 = GAME_MODE.RELEASE;
    public enum GAME_MODE{
        SIGMA, dest, DEV, RELEASE, BETA, SAFEMODE
    }
    static public boolean init;
    public static void staticInitTitle() {
        SIGMA = new Sigma();
        SIGMA.initClient();
    }
    public static boolean initClient = false;
    public static void staticInit() {
        if(!initClient) {
            initClient = true;
            SigmaNG = new SigmaNG();
            SigmaNG.initClient();
        }
    }
    public void initClient(){
        premiumManager.init();
        try {
            minimap.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(!ConfigManager.normalDir.exists())
            ConfigManager.normalDir.mkdir();
        if(!ConfigManager.configDir.exists())
            ConfigManager.configDir.mkdir();
        if(!scriptModuleManager.scriptDir.exists())
            scriptModuleManager.scriptDir.mkdir();
        if(!ConfigManager.musicDir.exists())
            ConfigManager.musicDir.mkdir();
//        if(!ConfigManager.altDir.exists())
//            ConfigManager.altDir.mkdir();

        verify.verify();
        ViaLoader.load();
        FontUtil.setupFonts();
        FontUtil.load();
        JelloFontUtil.init();
        moduleManager.init();
        EventManager.init();
        configManager.loadDefaultConfig();
        commandManager.init();
        UpdateLogs.loadLogs();
        BlurUtils.reInit();
        Sigma5BlurUtils.initAll();
    }
    public static void initProxy(){
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyPort", "7880");
//
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "7880");
    }
}
