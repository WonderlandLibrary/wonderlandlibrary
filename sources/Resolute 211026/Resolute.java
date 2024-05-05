// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute;

import java.util.concurrent.Executors;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Session;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import vip.Resolute.events.impl.EventChat;
import java.util.Iterator;
import vip.Resolute.events.Event;
import vip.Resolute.events.impl.EventKey;
import vip.Resolute.modules.impl.exploit.LunarSpoofer;
import vip.Resolute.modules.impl.player.TargetSpammer;
import vip.Resolute.modules.impl.player.AntiLixo;
import vip.Resolute.modules.impl.movement.TimerSlowdown;
import vip.Resolute.modules.impl.player.KillSults;
import vip.Resolute.modules.impl.exploit.PluginViewer;
import vip.Resolute.modules.impl.exploit.HackerDetector;
import vip.Resolute.modules.impl.exploit.MineplexDisabler;
import vip.Resolute.modules.impl.exploit.StaffDetector;
import vip.Resolute.modules.impl.exploit.LightningDetector;
import vip.Resolute.modules.impl.exploit.AntiTabComplete;
import vip.Resolute.modules.impl.exploit.Crasher;
import vip.Resolute.modules.impl.exploit.AntiVoid;
import vip.Resolute.modules.impl.exploit.Disabler;
import vip.Resolute.modules.impl.exploit.Freecam;
import vip.Resolute.modules.impl.render.OffscreenESP;
import vip.Resolute.modules.impl.render.HitMarkers;
import vip.Resolute.modules.impl.render.MotionPredict;
import vip.Resolute.modules.impl.render.FullBright;
import vip.Resolute.modules.impl.render.Crosshair;
import vip.Resolute.modules.impl.render.ESP;
import vip.Resolute.modules.impl.render.ChinaHat;
import vip.Resolute.modules.impl.render.Glint;
import vip.Resolute.modules.impl.render.BlockOverlay;
import vip.Resolute.modules.impl.render.ChestESP;
import vip.Resolute.modules.impl.render.Ambience;
import vip.Resolute.modules.impl.render.Trajectories;
import vip.Resolute.modules.impl.render.Animation;
import vip.Resolute.modules.impl.render.Camera;
import vip.Resolute.modules.impl.render.Chams;
import vip.Resolute.modules.impl.render.Overlay;
import vip.Resolute.modules.impl.render.ClickGUI;
import vip.Resolute.modules.impl.player.AutoSoup;
import vip.Resolute.modules.impl.player.StreamerMode;
import vip.Resolute.modules.impl.player.AutoGapple;
import vip.Resolute.modules.impl.player.Eagle;
import vip.Resolute.modules.impl.player.Safewalk;
import vip.Resolute.modules.impl.player.Regen;
import vip.Resolute.modules.impl.player.FastEat;
import vip.Resolute.modules.impl.player.Blink;
import vip.Resolute.modules.impl.player.InventoryManager;
import vip.Resolute.modules.impl.player.ChestStealer;
import vip.Resolute.modules.impl.player.AutoTool;
import vip.Resolute.modules.impl.player.AutoServer;
import vip.Resolute.modules.impl.player.FastBreak;
import vip.Resolute.modules.impl.player.Timer;
import vip.Resolute.modules.impl.player.FastPlace;
import vip.Resolute.modules.impl.player.Breaker;
import vip.Resolute.modules.impl.player.NoRotate;
import vip.Resolute.modules.impl.player.NoFall;
import vip.Resolute.modules.impl.movement.Jesus;
import vip.Resolute.modules.impl.combat.TargetStrafe;
import vip.Resolute.modules.impl.movement.Scaffold;
import vip.Resolute.modules.impl.movement.HighJump;
import vip.Resolute.modules.impl.movement.LongJump;
import vip.Resolute.modules.impl.movement.ScaffoldOld;
import vip.Resolute.modules.impl.movement.InventoryMove;
import vip.Resolute.modules.impl.movement.Phase;
import vip.Resolute.modules.impl.movement.Strafe;
import vip.Resolute.modules.impl.movement.NoSlowdown;
import vip.Resolute.modules.impl.movement.Step;
import vip.Resolute.modules.impl.movement.Speed;
import vip.Resolute.modules.impl.movement.Sprint;
import vip.Resolute.modules.impl.movement.Fly;
import vip.Resolute.modules.impl.combat.AimBot;
import vip.Resolute.modules.impl.combat.AntiAim;
import vip.Resolute.modules.impl.combat.Hitboxes;
import vip.Resolute.modules.impl.combat.DelayRemover;
import vip.Resolute.modules.impl.combat.Reach;
import vip.Resolute.modules.impl.combat.TPAura;
import vip.Resolute.modules.impl.combat.AutoClicker;
import vip.Resolute.modules.impl.combat.AimAssist;
import vip.Resolute.modules.impl.combat.Criticals;
import vip.Resolute.modules.impl.combat.KillAura;
import vip.Resolute.modules.impl.combat.AutoPot;
import vip.Resolute.modules.impl.combat.Velocity;
import vip.Resolute.modules.impl.combat.AntiBot;
import vip.Resolute.util.font.FontUtil;
import org.lwjgl.opengl.Display;
import java.net.Proxy;
import vip.Resolute.util.misc.TimerUtil;
import java.util.concurrent.ExecutorService;
import vip.Resolute.config.ConfigManager;
import vip.Resolute.util.rpc.DiscordRP;
import vip.Resolute.ui.notification.NotificationManager;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.ui.login.system.AccountManager;
import vip.Resolute.config.SaveLoad;
import vip.Resolute.command.CommandManager;
import vip.Resolute.util.misc.TimerUtils;
import vip.Resolute.modules.Module;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.File;
import net.minecraft.client.Minecraft;

public class Resolute
{
    public static Minecraft mc;
    public static String selectedCategory;
    public static String name;
    public static String build;
    public static String fullname;
    public static String loginName;
    public static int color;
    public static float posX;
    public static float posY;
    public static float boolTranslation;
    public boolean ebicauth;
    public static String APIKey;
    private static File directory;
    public static CopyOnWriteArrayList<Module> modules;
    private static final TimerUtils inventoryTimer;
    public static CommandManager commandManager;
    public static Resolute instance;
    public static SaveLoad saveLoad;
    public static AccountManager accountManager;
    public String username;
    public String uuid;
    public static boolean authorized;
    public static MinecraftFontRenderer fontRenderer;
    public static NotificationManager notificationManager;
    public static DiscordRP discordRP;
    public static ConfigManager configManager;
    public static ExecutorService executorService;
    public static TimerUtil sessionTime;
    private static String alt;
    private Proxy proxy;
    public String api;
    
    public Resolute() {
        this.proxy = Proxy.NO_PROXY;
        this.api = "";
    }
    
    public static void startup() {
        Display.setTitle(Resolute.fullname);
        Resolute.directory = new File(Minecraft.getMinecraft().mcDataDir, "Resolute");
        if (!Resolute.directory.exists()) {
            Resolute.directory.mkdir();
        }
        Resolute.discordRP.start();
        Resolute.sessionTime.reset();
        Resolute.notificationManager = new NotificationManager();
        Resolute.authorized = false;
        Resolute.accountManager = new AccountManager(Resolute.directory);
        Resolute.saveLoad = new SaveLoad("");
        FontUtil.bootstrap();
        Resolute.modules.add(new AntiBot());
        Resolute.modules.add(new Velocity());
        Resolute.modules.add(new AutoPot());
        Resolute.modules.add(new KillAura());
        Resolute.modules.add(new Criticals());
        Resolute.modules.add(new AimAssist());
        Resolute.modules.add(new AutoClicker());
        Resolute.modules.add(new TPAura());
        Resolute.modules.add(new Reach());
        Resolute.modules.add(new DelayRemover());
        Resolute.modules.add(new Hitboxes());
        Resolute.modules.add(new AntiAim());
        Resolute.modules.add(new AimBot());
        Resolute.modules.add(new Fly());
        Resolute.modules.add(new Sprint());
        Resolute.modules.add(new Speed());
        Resolute.modules.add(new Step());
        Resolute.modules.add(new NoSlowdown());
        Resolute.modules.add(new Strafe());
        Resolute.modules.add(new Phase());
        Resolute.modules.add(new InventoryMove());
        Resolute.modules.add(new ScaffoldOld());
        Resolute.modules.add(new LongJump());
        Resolute.modules.add(new HighJump());
        Resolute.modules.add(new Scaffold());
        Resolute.modules.add(new TargetStrafe());
        Resolute.modules.add(new Jesus());
        Resolute.modules.add(new NoFall());
        Resolute.modules.add(new NoRotate());
        Resolute.modules.add(new Breaker());
        Resolute.modules.add(new FastPlace());
        Resolute.modules.add(new Timer());
        Resolute.modules.add(new FastBreak());
        Resolute.modules.add(new AutoServer());
        Resolute.modules.add(new AutoTool());
        Resolute.modules.add(new ChestStealer());
        Resolute.modules.add(new InventoryManager());
        Resolute.modules.add(new Blink());
        Resolute.modules.add(new FastEat());
        Resolute.modules.add(new Regen());
        Resolute.modules.add(new Safewalk());
        Resolute.modules.add(new Eagle());
        Resolute.modules.add(new AutoGapple());
        Resolute.modules.add(new StreamerMode());
        Resolute.modules.add(new AutoSoup());
        Resolute.modules.add(new ClickGUI());
        Resolute.modules.add(new Overlay());
        Resolute.modules.add(new Chams());
        Resolute.modules.add(new Camera());
        Resolute.modules.add(new Animation());
        Resolute.modules.add(new Trajectories());
        Resolute.modules.add(new Ambience());
        Resolute.modules.add(new ChestESP());
        Resolute.modules.add(new BlockOverlay());
        Resolute.modules.add(new Glint());
        Resolute.modules.add(new ChinaHat());
        Resolute.modules.add(new ESP());
        Resolute.modules.add(new Crosshair());
        Resolute.modules.add(new FullBright());
        Resolute.modules.add(new MotionPredict());
        Resolute.modules.add(new HitMarkers());
        Resolute.modules.add(new OffscreenESP());
        Resolute.modules.add(new Freecam());
        Resolute.modules.add(new Disabler());
        Resolute.modules.add(new AntiVoid());
        Resolute.modules.add(new Crasher());
        Resolute.modules.add(new AntiTabComplete());
        Resolute.modules.add(new LightningDetector());
        Resolute.modules.add(new StaffDetector());
        Resolute.modules.add(new MineplexDisabler());
        Resolute.modules.add(new HackerDetector());
        Resolute.modules.add(new PluginViewer());
        Resolute.modules.add(new KillSults());
        Resolute.modules.add(new TimerSlowdown());
        Resolute.modules.add(new AntiLixo());
        Resolute.modules.add(new TargetSpammer());
        Resolute.modules.add(new LunarSpoofer());
    }
    
    public static void shutdown() {
        if (Resolute.saveLoad != null) {
            Resolute.saveLoad.save();
        }
        if (getConfigManager().saveConfig("shutdownConfig")) {
            System.out.println("Saved Config");
        }
        Resolute.discordRP.shutdown();
        Resolute.accountManager.save();
    }
    
    public static void keyPress(final int key) {
        onEvent(new EventKey(key));
        for (final Module m : Resolute.modules) {
            if (m.getKey() == key) {
                m.toggle();
            }
        }
    }
    
    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }
    
    public static MinecraftFontRenderer getFontRenderer() {
        if (Overlay.cfont.is("Client")) {
            Resolute.fontRenderer = FontUtil.clientfont;
        }
        if (Overlay.cfont.is("Moon")) {
            Resolute.fontRenderer = FontUtil.moon;
        }
        if (Overlay.cfont.is("SF Large")) {
            Resolute.fontRenderer = FontUtil.summer;
        }
        if (Overlay.cfont.is("SF")) {
            Resolute.fontRenderer = FontUtil.sf;
        }
        if (Overlay.cfont.is("Tahoma")) {
            Resolute.fontRenderer = FontUtil.tahoma;
        }
        if (Overlay.cfont.is("Roboto")) {
            Resolute.fontRenderer = FontUtil.robo;
        }
        return Resolute.fontRenderer;
    }
    
    public DiscordRP getDiscordRP() {
        return Resolute.discordRP;
    }
    
    public void setAuthorized(final boolean authorized) {
        Resolute.authorized = authorized;
    }
    
    public void setResoluteName(final String newname) {
        this.username = newname;
    }
    
    public static ExecutorService executorService() {
        return Resolute.executorService;
    }
    
    public void setUUID(final String uuid) {
        this.uuid = uuid;
    }
    
    public static AccountManager getAccountManager() {
        return Resolute.accountManager;
    }
    
    public static Resolute getInstance() {
        return Resolute.instance;
    }
    
    public static NotificationManager getNotificationManager() {
        return Resolute.notificationManager;
    }
    
    public void setAPI(final String api) {
        this.api = api;
    }
    
    public String[] getSpectatorAlt() {
        return (String[])((Resolute.alt == null) ? null : Resolute.alt.split(":"));
    }
    
    public void setAlt(final String alt) {
        Resolute.alt = alt;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
    
    public static void onEvent(final Event e) {
        try {
            if (e instanceof EventChat) {
                Resolute.commandManager.handleChat((EventChat)e);
            }
            for (final Module m : Resolute.modules) {
                if (!m.isToggled()) {
                    continue;
                }
                try {
                    m.onEvent(e);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        catch (NullPointerException e2) {
            e2.printStackTrace();
        }
    }
    
    public static void addChatMessage(String message) {
        message = "§4[§cR§4]§8 " + message;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
    
    public static void printChat(String text) {
        text = "§4[§cR§4]§8 " + text;
        Resolute.mc.thePlayer.addChatComponentMessage(new ChatComponentText(text));
    }
    
    public static ConfigManager getConfigManager() {
        return Resolute.configManager;
    }
    
    public static List<Module> getModulesByCategory(final Module.Category c) {
        final List<Module> modules = new ArrayList<Module>();
        for (final Module m : Resolute.modules) {
            if (m.category == c) {
                modules.add(m);
            }
        }
        return modules;
    }
    
    public static void setPlayerName(final String string) {
        Minecraft.getMinecraft().session = new Session(string, "", "0", "");
    }
    
    public static String getVersion() {
        return Resolute.build;
    }
    
    public File getDirectory() {
        return Resolute.directory;
    }
    
    public static List<EntityLivingBase> getLivingEntities() {
        return Resolute.mc.theWorld.getLoadedEntityList().stream().filter(e -> e instanceof EntityLivingBase).map(e -> e).collect((Collector<? super Object, ?, List<EntityLivingBase>>)Collectors.toList());
    }
    
    public static List<EntityLivingBase> getLivingEntities(final Predicate<EntityLivingBase> validator) {
        final List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
        for (final Entity entity : Resolute.mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase e = (EntityLivingBase)entity;
                if (!validator.test(e)) {
                    continue;
                }
                entities.add(e);
            }
        }
        return entities;
    }
    
    public static ItemStack getStackInSlot(final int index) {
        return Resolute.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
    }
    
    static {
        Resolute.mc = Minecraft.getMinecraft();
        Resolute.selectedCategory = String.valueOf(Module.Category.COMBAT);
        Resolute.name = "Resolute";
        Resolute.build = "211026";
        Resolute.fullname = Resolute.name + " " + Resolute.build;
        Resolute.color = 251719423;
        Resolute.APIKey = "";
        Resolute.modules = new CopyOnWriteArrayList<Module>();
        inventoryTimer = new TimerUtils();
        Resolute.commandManager = new CommandManager();
        Resolute.instance = new Resolute();
        Resolute.fontRenderer = null;
        Resolute.discordRP = new DiscordRP();
        Resolute.configManager = new ConfigManager();
        Resolute.executorService = Executors.newSingleThreadExecutor();
        Resolute.sessionTime = new TimerUtil();
    }
}
