// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity;

import org.apache.logging.log4j.LogManager;
import dev.tenacity.config.DragManager;
import dev.tenacity.utils.objects.Dragging;
import dev.tenacity.module.Module;
import java.awt.Color;
import java.util.concurrent.Executors;
import dev.tenacity.utils.objects.DiscordAccount;
import dev.tenacity.ui.altmanager.helpers.KingGenApi;
import dev.tenacity.utils.misc.DiscordRPC;
import dev.tenacity.utils.server.PingerUtils;
import dev.tenacity.commands.CommandHandler;
import dev.tenacity.ui.altmanager.GuiAltManager;
import dev.tenacity.config.ConfigManager;
import dev.tenacity.intent.api.account.IntentAccount;
import dev.tenacity.scripting.api.ScriptManager;
import dev.tenacity.module.ModuleCollection;
import dev.tenacity.ui.searchbar.SearchBar;
import dev.tenacity.ui.sidegui.SideGUI;
import java.util.concurrent.ExecutorService;
import dev.tenacity.intent.cloud.CloudDataManager;
import dev.tenacity.event.EventProtocol;
import java.io.File;
import org.apache.logging.log4j.Logger;
import dev.tenacity.utils.client.ReleaseType;
import dev.tenacity.utils.Utils;

public class Tenacity implements Utils
{
    public static final Tenacity INSTANCE;
    public static final String NAME = "Tenacity";
    public static final String VERSION = "6.0";
    public static final ReleaseType RELEASE;
    public static final Logger LOGGER;
    public static final File DIRECTORY;
    private final EventProtocol eventProtocol;
    private final CloudDataManager cloudDataManager;
    private final ExecutorService executorService;
    private final SideGUI sideGui;
    private final SearchBar searchBar;
    private ModuleCollection moduleCollection;
    private ScriptManager scriptManager;
    private IntentAccount intentAccount;
    private ConfigManager configManager;
    private GuiAltManager altManager;
    private CommandHandler commandHandler;
    private PingerUtils pingerUtils;
    private DiscordRPC discordRPC;
    public KingGenApi kingGenApi;
    private DiscordAccount discordAccount;
    public static boolean updateGuiScale;
    public static int prevGuiScale;
    
    public Tenacity() {
        this.eventProtocol = new EventProtocol();
        this.cloudDataManager = new CloudDataManager();
        this.executorService = Executors.newSingleThreadExecutor();
        this.sideGui = new SideGUI();
        this.searchBar = new SearchBar();
    }
    
    public String getVersion() {
        return "6.0" + ((Tenacity.RELEASE != ReleaseType.PUBLIC) ? (" (" + Tenacity.RELEASE.getName() + ")") : "");
    }
    
    public final Color getClientColor() {
        return new Color(236, 133, 209);
    }
    
    public final Color getAlternateClientColor() {
        return new Color(28, 167, 222);
    }
    
    public boolean isEnabled(final Class<? extends Module> c) {
        final Module m = Tenacity.INSTANCE.moduleCollection.get(c);
        return m != null && m.isEnabled();
    }
    
    public Dragging createDrag(final Module module, final String name, final float x, final float y) {
        DragManager.draggables.put(name, new Dragging(module, name, x, y));
        return DragManager.draggables.get(name);
    }
    
    public EventProtocol getEventProtocol() {
        return this.eventProtocol;
    }
    
    public CloudDataManager getCloudDataManager() {
        return this.cloudDataManager;
    }
    
    public ExecutorService getExecutorService() {
        return this.executorService;
    }
    
    public SideGUI getSideGui() {
        return this.sideGui;
    }
    
    public SearchBar getSearchBar() {
        return this.searchBar;
    }
    
    public ModuleCollection getModuleCollection() {
        return this.moduleCollection;
    }
    
    public ScriptManager getScriptManager() {
        return this.scriptManager;
    }
    
    public IntentAccount getIntentAccount() {
        return this.intentAccount;
    }
    
    public ConfigManager getConfigManager() {
        return this.configManager;
    }
    
    public GuiAltManager getAltManager() {
        return this.altManager;
    }
    
    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }
    
    public PingerUtils getPingerUtils() {
        return this.pingerUtils;
    }
    
    public DiscordRPC getDiscordRPC() {
        return this.discordRPC;
    }
    
    public KingGenApi getKingGenApi() {
        return this.kingGenApi;
    }
    
    public DiscordAccount getDiscordAccount() {
        return this.discordAccount;
    }
    
    public void setModuleCollection(final ModuleCollection moduleCollection) {
        this.moduleCollection = moduleCollection;
    }
    
    public void setScriptManager(final ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }
    
    public void setIntentAccount(final IntentAccount intentAccount) {
        this.intentAccount = intentAccount;
    }
    
    public void setConfigManager(final ConfigManager configManager) {
        this.configManager = configManager;
    }
    
    public void setAltManager(final GuiAltManager altManager) {
        this.altManager = altManager;
    }
    
    public void setCommandHandler(final CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    
    public void setPingerUtils(final PingerUtils pingerUtils) {
        this.pingerUtils = pingerUtils;
    }
    
    public void setDiscordRPC(final DiscordRPC discordRPC) {
        this.discordRPC = discordRPC;
    }
    
    public void setKingGenApi(final KingGenApi kingGenApi) {
        this.kingGenApi = kingGenApi;
    }
    
    public void setDiscordAccount(final DiscordAccount discordAccount) {
        this.discordAccount = discordAccount;
    }
    
    static {
        INSTANCE = new Tenacity();
        RELEASE = ReleaseType.DEV;
        LOGGER = LogManager.getLogger("Tenacity");
        DIRECTORY = new File(Tenacity.mc.mcDataDir, "Tenacity");
    }
}
