package cc.novoline;

import cc.novoline.events.EventManager;
import cc.novoline.gui.screen.alt.repository.AltRepositoryGUI;
import cc.novoline.gui.screen.click.DiscordGUI;
import cc.novoline.gui.screen.dropdown.DropdownGUI;
import cc.novoline.gui.screen.login.GuiLogin;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.ModuleManager.ModuleCreator;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.combat.*;
import cc.novoline.modules.configurations.ConfigManager;
import cc.novoline.modules.exceptions.OutdatedConfigException;
import cc.novoline.modules.exploits.*;
import cc.novoline.modules.misc.*;
import cc.novoline.modules.move.*;
import cc.novoline.modules.player.*;
import cc.novoline.modules.visual.*;
import cc.novoline.utils.notifications.NotificationManager;
import cc.novoline.utils.tasks.TaskManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.init.Blocks;
import net.optifine.RenderEnv;
//import net.skidunion.API;
//import net.skidunion.irc.IRCClientBuilder;
//import net.skidunion.security.Protection;
//import net.skidunion.security.annotations.Protect;
//import net.skidunion.security.crypto.AES;
//import net.skidunion.security.crypto.Blowfish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.lwjgl.opengl.Display.setTitle;

//@Protect.Virtualize
public class Initializer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static Initializer INSTANCE;
    private boolean isDev;

    public double secretShit = 0.01;
    public double flight50 = -50;
    public double flight90 = -12;
    public double flightAmp = -0.001;
    public double flight75 = -0.42;
    public double flightMagicCon1 = -1;
    public double flightMagicCon2 = 1;
    public double flightHypixelGroundCheck = 1;
    public double bunnyDivFriction = 50;
    public double threshold = 0.5;

    public double min17 = 0.00015, max17 = 0.000163166800276;
    public double min18 = 0.0099, max18 = 0.011921599284565;
    public double min19 = 0.014, max19 = 0.015919999545217;
    public double min20 = 0.0099, max20 = 0.011999999731779;

    private enum Singleton {
        INSTANCE;

        private final Initializer value;

        Singleton() {
            this.value = new Initializer();
        }
    }

    public static Initializer getInstance() {
        return Singleton.INSTANCE.value;
    }

    public String Xor(Object obj, String key) {
        StringBuilder sb = new StringBuilder();
        char[] keyChars = key.toCharArray();

        int i = 0;

        for (char c : obj.toString().toCharArray()) {
            sb.append((char) (c ^ keyChars[i % keyChars.length]));
            i++;
        }

        return sb.toString();
    }

    public void onProtection(String token) {
        try {

//            Blowfish blowfish = new Blowfish();
//            extra = blowfish.decryptString(extra);
//            long time = Long.parseLong(extra.split(":")[0]);
//
//            // Generic protection checks
//            if (Novoline.getInstance().getProtection() == null) {
//                throw new Throwable();
//            } else if (!Novoline.getInstance().getProtection().isPassed()) {
//                Novoline.getInstance().getProtection().novoDbgPrint("Protection is not passed");
//                throw new Throwable();
//            } else if (System.currentTimeMillis() - time > 8000) {
//                Novoline.getInstance().getProtection().novoDbgPrint("Failed time check");
//                throw new Throwable();
//            } else if (!Gui.protectionIsPassed) {
//                Novoline.getInstance().getProtection().novoDbgPrint("Failed GUIProtection check");
//                throw new Throwable();
//            } else if (API.getHWID().hashCode() == -1617199657) {
//                Novoline.getInstance().getProtection().novoDbgPrint("Failed hash check");
//                throw new Throwable();
//            }

            {
                secretShit = 0.0001000000001;
                flight50 = 5.0d;
                flight90 = 9.0d;
                flight75 = 0.75d;
                flightAmp = 0.13d;
                flightMagicCon1 = 2.1449999809265137;
                flightMagicCon2 = -0.1552320045166016;
                flightHypixelGroundCheck = 0.015625;
                bunnyDivFriction = 159.9;
                threshold = 0.66;
            }

            try {
                Novoline.getInstance().setModuleManager(new ModuleManager(Novoline.getInstance(), 3));
                isDev = true;

                register("sprint", Sprint::new);
                register("hud", HUD::new);
                register("click_gui", ClickGUI::new);
                register("anti_bot", AntiBot::new);
                //           register("no_rotate", NoRotate::new);
                register("speed_mine", SpeedMine::new);
                register("auto_pot", AutoPot::new);
                register("kill_aura", KillAura::new);
                register("criticals", Criticals::new);
                register("reach", Reach::new);
                register("step", Step::new);
                register("water-walk", WaterWalk::new);
                register("no_slow", NoSlow::new);
                register("speed", Speed::new);
                register("velocity", Velocity::new);
                register("glow", ESP::new);
                register("chams", Chams::new);
                register("chest_esp", ChestESP::new);
                register("nametags", Nametags::new);
                register("no_fall", NoFall::new);
                register("xray", XRay::new);
                register("chest_stealer", ChestStealer::new);
                register("scaffold", Scaffold::new);
                register("auto_armor", AutoArmor::new);
                register("inv_manager", InvManager::new);
                register("auto-hypixel", AutoHypixel::new);
                register("gui_move", GuiMove::new);
                register("phase", Phase::new);
                register("auto_tool", AutoTool::new);
                register("animations", Animations::new);
                //     register("long-jump", LongJump::new);
                register("teleport", Teleport::new);
                register("glint-colorize", GlintColorize::new);
                register("middle-click", MiddleClick::new);
                register("dmg-particles", DMGParticle::new);
                register("tracers", Tracers::new);
                register("respawn", Respawn::new);
                register("blink", Blink::new);
                register("anti_cactus", AntiCactus::new);
                register("no_effects", NoEffects::new);
                register("crosshair", Crosshair::new);
                register("brightness", Brightness::new);
                register("item_physic", ItemPhysic::new);
                register("item_esp", ItemESP::new);
                register("auto_clicker", AutoClicker::new);
                register("aim_assist", AimAssist::new);
                register("fast_place", FastPlace::new);
                register("radar", Radar::new);
                register("players_finder", Notifier::new);
                register("world_time", Atmosphere::new);
                register("killsults", Killsults::new);
                register("spammer", Spammer::new);
                register("player_esp", PlayerESP::new);
                register("hit_box", HitBox::new);
                register("chat_filter", ChatFilter::new);
                register("waypoints", Waypoints::new);
                register("streamer", Streamer::new);
                register("antiobbytrap", AntiObbyTrap::new);
                register("freecam", Freecam::new);
                register("target-strafe", TargetStrafe::new);
                register("tab-gui", TabGUI::new);
                register("lightning-trecker", LightningTracker::new);
                register("camera", Camera::new);
                register("fast-sneak", FastSneak::new);
                register("arrows", Arrows::new);
                register("bed-breaker", BedBreaker::new);
                register("auto-heal", AutoHead::new);
                //   register("click-teleport", ClickTeleport::new);
                register("flight", Flight::new);
                register("anti_void", AntiVoid::new);
                register("long-jump", LongJump::new);
                register("anti-alts", AntiAtlas::new);
                register("blocksoverlay",BlockOutline::new);

                if (isDev) {
                    register("game-speed", GameSpeed::new);
                    register("debug", Debug::new);
                    register("damage", Damage::new);
                }

                register("disabler", Disabler::new);

                Novoline.getInstance().playerManager = new PlayerManager(Novoline.getInstance(), "players.novo");
                Novoline.getInstance().taskManager = new TaskManager();
                Novoline.getInstance().notificationManager = new NotificationManager();
            } catch (Throwable t) {
                throw new RuntimeException("An error occurred while loading managers", t);
            }

            EventManager.register(Novoline.getInstance().getModuleManager().getModule(ClickGUI.class));
            Killsults killsults = Novoline.getInstance().getModuleManager().getModule(Killsults.class);

            if (Files.notExists(killsults.getPath())) {
                Files.createFile(killsults.getPath());
            }

            if (Novoline.getInstance().version.toCharArray()[0] == '@') {
                Novoline.getInstance().version = new SimpleDateFormat("MMddyy").format(new Date());
            }

            //region Loading configurations
            Novoline.getInstance().getModuleManager().getBindManager().load();

            ConfigManager configManager = Novoline.getInstance().getModuleManager().getConfigManager();
            Path path = configManager.getConfigPath("default");

            try {
                if (Files.exists(path)) {
                    configManager.load(path, false);
                }
            } catch (OutdatedConfigException oce) {
                LOGGER.error("Default config is outdated. Deleting it...");

                try {
                    Files.delete(path);
                } catch (IOException ioe) {
                    LOGGER.error("An I/O error occurred while deleting default config", ioe);
                }
            } catch (Throwable t) {
                LOGGER.error("An unexpected error occurred while loading default config", t);
            }

/*            try {
                new ViaFabric().onInitialize();
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            //endregion
            setTitle("Novoline " + Novoline.getInstance().getVersion());
            Novoline.getInstance().discordGUI = new DiscordGUI(Novoline.getInstance());
            Novoline.getInstance().dropDownGUI = new DropdownGUI();
            Novoline.getInstance().altRepositoryGUI = new AltRepositoryGUI(Novoline.getInstance());

//            Novoline.getInstance().irc = new IRCClientBuilder(URI.create("ws://ws.novoline.wtf:2095"))
//                    .setSecret("6zvaa5uqx8k4FMfT")
//                    .setHashFunction(new NativeCachedHashFunction())
//                    .setToken(AES.decrypt(System.getProperty("novo.delsyToken"), "WHTTB3KKadKgqFg5", "tJGZ2ZnM7bF6qDhs"))
//                    .build();

//            Novoline.getInstance().irc.getEventManager().register(SimpleEventListener.getInstance());
            ResourceIndex.wasInitialized = true;
            RenderEnv.wasInitialized = true;
            Blocks.fuckUp = 0x01c0c1;
            System.setProperty("java.net.preferIPv4Stack", "true");

        } catch (Throwable t) {
            t.printStackTrace();
            Minecraft.getInstance().shutdown();
            return;
        }

        Minecraft.getInstance().displayGuiScreen(new GuiMainMenu());
    }

    public static boolean guiLoginIsNotPassed(GuiScreen previousScreen, GuiScreen guiScreenIn) {
        return previousScreen instanceof GuiLogin && !(guiScreenIn instanceof GuiMainMenu);
    }

//    public static String grabToken() throws Exception {
//        return AES.decrypt(System.getProperty("novo.delsyToken"), "WHTTB3KKadKgqFg5", "tJGZ2ZnM7bF6qDhs");
//    }

    private <Module extends AbstractModule> void register(@NotNull String name, @NotNull ModuleCreator<Module> moduleCreator) {
        Novoline.getInstance().getModuleManager().register(name, moduleCreator);
    }
}
