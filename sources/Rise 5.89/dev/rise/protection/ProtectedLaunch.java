package dev.rise.protection;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.CommandManager;
import dev.rise.command.impl.PlayerHacks;
import dev.rise.command.impl.*;
import dev.rise.creative.RiseTab;
import dev.rise.module.Module;
import dev.rise.module.impl.combat.*;
import dev.rise.module.impl.ghost.*;
import dev.rise.module.impl.ghost.legitfightbot.LegitFightBot;
import dev.rise.module.impl.movement.*;
import dev.rise.module.impl.other.Sniper;
import dev.rise.module.impl.other.Spammer;
import dev.rise.module.impl.other.*;
import dev.rise.module.impl.player.*;
import dev.rise.module.impl.render.*;
import dev.rise.module.impl.render.particles.Particles;
import dev.rise.module.impl.render.targethud.TargetHud;
import dev.rise.module.manager.ModuleManager;
import dev.rise.notifications.NotificationManager;
import dev.rise.script.ScriptManager;
import dev.rise.ui.alt.AltGUI;
import dev.rise.ui.altmanager.gui.AltManagerGUI;
import dev.rise.ui.auth.AuthGUI;
import dev.rise.ui.clickgui.impl.ClickGUI;
import dev.rise.ui.clickgui.impl.strikeless.StrikeGUI;
import dev.rise.ui.guitheme.GuiTheme;
import dev.rise.ui.mainmenu.MainMenu;
import dev.rise.ui.version.VersionGui;
import dev.rise.util.misc.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.main.Main;
import store.intent.api.account.GetUserInfo;
import store.intent.api.account.IntentAccount;
import store.intent.intentguard.annotation.Bootstrap;
import store.intent.intentguard.annotation.Native;

import java.io.File;

@Native
public class ProtectedLaunch {

    @Native
    @Bootstrap
    public static void start() {
        if (Main.apiKey != null && !Main.apiKey.isEmpty())
            Rise.intentAccount = new GetUserInfo().getIntentAccount(Main.apiKey);

        if (Rise.intentAccount == null) Rise.intentAccount = new IntentAccount();

        final Rise instance = Rise.INSTANCE;
        //ircClient = new IRCClient();
        try {
            (instance.moduleManager = new ModuleManager()).moduleList = new Module[]{
                    // IMPORTANT RENDER
                    new Interface(),
                    new TabGui(),

                    // COMBAT
                    new AutoGap(),
                    new AutoHead(),
                    new Aura(),
                    new FastBow(),
                    new TPAura(),
                    new BackTrack(),
                    new Velocity(),
                    new TargetStrafe(),
                    new FastBow(),
                    new AutoSoup(),
                    new FightBot(),
                    new AntiBot(),
                    new WTap(),
                    new Criticals(),
                    new ComboOneHit(),
                    new Regen(),
                    new AttackCrash(),

                    // PLAYER
                    new Derp(),
                    new ChestAura(),
                    new Stealer(),
                    new AutoTool(),
                    new FastPlace(),
                    new FastEat(),
                    new NoVoid(),
                    new NoFall(),
                    new Scaffold(),
                    new FastBreak(),
                    new Manager(),
                    new Infinite(),

                    // MOVEMENT
                    new Sprint(),
                    new Sneak(),
                    new Fly(),
                    new Speed(),
                    new No003(),
                    new Strafe(),
                    new Jesus(),
                    new NoSlow(),
                    new HighJump(),
                    new LongJump(),
                    new InvMove(),
                    new Step(),
                    new Phase(),
                    new Blink(),
                    new Spider(),
                    new ClickTP(),
                    new BedWalker(),
                    new BowFly(),
                    new Clipper(),
                    new NoBob(),

                    // GHOST
                    new LegitFightBot(),
                    new AimAssist(),
                    new Reach(),
                    new AutoClicker(),
                    new Eagle(),
                    new KeepSprint(),

                    // OTHER
                    new AutoHypixel(),
                    new Disabler(),
                    new Plugins(),
                    new AutoAuthme(),
                    new NoRot(),
                    new Insults(),
                    new ChatBypass(),
                    new Breaker(),
                    new AutoGroomer(),
                    new Nuker(),
                    new Timer(),
                    new Spammer(),
                    new Crasher(),
                    new PingSpoof(),
                    new NoGuiClose(),
                    new AntiSuffocation(),
                    new dev.rise.module.impl.other.PlayerHacks(),
                    new AntiCheat(),
                    new StaffAlert(),
                    new Sniper(),
                    new ClientSpoofer(),
                    new AutoBuild(),
                    new Test(),

                    // RENDER
                    new ShaderESP(),
                    new PopOutAnimation(),
                    new ChinaHat(),
                    new Hitmarks(),
                    new SimsESP(),
                    new ClickGui(),
                    new Nametags(),
                    new Chams(),
                    new BrightPlayers(),
                    new ESP(),
                    new Animations(),
                    new Ambiance(),
                    new AttackEffects(),
                    new TargetHud(),
                    new DamageColor(),
                    new TwoDESP(),
                    new Enchant(),
                    new DeathEffects(),
                    new Fullbright(),
                    new NoWeather(),
                    new Giants(),
                    new Freecam(),
                    new Tracers(),
                    new NameProtect(),
                    new Streamer(),
                    new Scoreboard(),
                    new Breadcrumbs(),
                    new Radar(),
                    new Blur(),
                    new ChestESP(),
                    new NoHurtCam(),
                    new CameraClip(),
                    new ItemPhysics(),
                    new MotionGraph(),
                    new NoAchievements(),
                    new ImageESP(),
                    new Health(),
                    new XRay(),
                    new Particles(),
//                    new FaceCam(),
//                    new KillGlow(),

                    //SPECIAL ORDER (These modules events must be called in a specific order)
                    new AutoPot(),
                    new SSMode()
            };

            instance.notificationManager = new NotificationManager();

            instance.cmdManager = new CommandManager();

            CommandManager.COMMANDS = new Command[]{
                    new Bind(),
                    new Toggle(),
                    new Config(),
                    new Say(),
                    new Friend(),
                    new Crash(),
                    new Target(),
                    new ClientName(),
                    new VClip(),
                    new HClip(),
                    new Panic(),
                    new dev.rise.command.impl.Sniper(),
                    new dev.rise.command.impl.Spammer(),
                    new SetArea(),
                    new PlayerHacks(),
                    new Tp(),
                    new Name(),
                    new Help(),
            };
        } catch (final Exception e) {
            e.printStackTrace();
        }

        try {
            instance.clickGUI = new ClickGUI();
            instance.guiMainMenu = new MainMenu();
            instance.guiMultiplayer = new GuiMultiplayer(instance.guiMainMenu);
            instance.strikeGUI = new StrikeGUI();
            instance.altGUI = new AltGUI();
            instance.altManagerGUI = new AltManagerGUI();
            instance.guiTheme = new GuiTheme();
            instance.antiCheat = new dev.rise.anticheat.AntiCheat();
            instance.scriptManager = new ScriptManager();
            instance.creativeTab = new RiseTab();
            instance.versionSwitcher = new VersionGui();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        final Minecraft mc = Minecraft.getMinecraft();

        // Compatibility
        mc.gameSettings.guiScale = 2;
        mc.gameSettings.ofFastRender = false;

        // Performance settings
        mc.gameSettings.ofSmartAnimations = true;
        mc.gameSettings.ofSmoothFps = false;

        try {
            // Creating Rise folder
            if (!FileUtil.riseDirectoryExists()) {
                instance.firstBoot = true;
                FileUtil.createRiseDirectory();
            }

            if (!FileUtil.exists("Config" + File.separator)) {
                FileUtil.createDirectory("Config" + File.separator);
            }

            if (!FileUtil.exists("Script" + File.separator)) {
                FileUtil.createDirectory("Script" + File.separator);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        try {
            instance.loadClient();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        AuthGUI.authed = true;
        mc.displayGuiScreen(instance.getGuiMainMenu());
    }


}
