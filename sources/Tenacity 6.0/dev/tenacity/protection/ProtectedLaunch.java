// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.protection;

import store.intent.intentguard.annotation.Bootstrap;
import dev.tenacity.viamcp.ViaMCP;
import dev.tenacity.ui.altmanager.helpers.KingGenApi;
import dev.tenacity.ui.altmanager.GuiAltManager;
import dev.tenacity.config.DragManager;
import java.io.File;
import net.minecraft.client.Minecraft;
import dev.tenacity.config.ConfigManager;
import dev.tenacity.module.BackgroundProcess;
import java.util.Collection;
import java.util.Arrays;
import dev.tenacity.commands.impl.ToggleCommand;
import dev.tenacity.commands.impl.LoadCommand;
import dev.tenacity.commands.impl.ClearConfigCommand;
import dev.tenacity.commands.impl.ClearBindsCommand;
import dev.tenacity.commands.impl.VClipCommand;
import dev.tenacity.commands.impl.HelpCommand;
import dev.tenacity.commands.impl.SettingCommand;
import dev.tenacity.commands.impl.ScriptCommand;
import dev.tenacity.commands.impl.UnbindCommand;
import dev.tenacity.commands.impl.BindCommand;
import dev.tenacity.commands.impl.CopyNameCommand;
import dev.tenacity.commands.impl.FriendCommand;
import dev.tenacity.commands.Command;
import dev.tenacity.commands.CommandHandler;
import dev.tenacity.scripting.api.ScriptManager;
import dev.tenacity.utils.server.PingerUtils;
import dev.tenacity.utils.render.Theme;
import dev.tenacity.module.impl.render.BrightPlayers;
import dev.tenacity.module.impl.render.Chams;
import dev.tenacity.module.impl.render.EntityEffects;
import dev.tenacity.module.impl.render.CustomModel;
import dev.tenacity.module.impl.render.JumpCircle;
import dev.tenacity.module.impl.render.PlayerList;
import dev.tenacity.module.impl.render.wings.DragonWings;
import dev.tenacity.utils.render.EntityCulling;
import dev.tenacity.module.impl.render.XRay;
import dev.tenacity.module.impl.render.ItemPhysics;
import dev.tenacity.module.impl.render.Keystrokes;
import dev.tenacity.module.impl.render.NoHurtCam;
import dev.tenacity.module.impl.render.Hitmarkers;
import dev.tenacity.module.impl.render.Streamer;
import dev.tenacity.module.impl.render.Breadcrumbs;
import dev.tenacity.module.impl.render.Glint;
import dev.tenacity.module.impl.render.TargetHUDMod;
import dev.tenacity.module.impl.render.Statistics;
import dev.tenacity.module.impl.render.PostProcessing;
import dev.tenacity.module.impl.render.ESP2D;
import dev.tenacity.module.impl.render.Brightness;
import dev.tenacity.module.impl.render.GlowESP;
import dev.tenacity.module.impl.render.ChinaHat;
import dev.tenacity.module.impl.render.Ambience;
import dev.tenacity.module.impl.render.SpotifyMod;
import dev.tenacity.module.impl.render.Animations;
import dev.tenacity.module.impl.render.Radar;
import dev.tenacity.module.impl.render.ClickGUIMod;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.module.impl.render.ScoreboardMod;
import dev.tenacity.module.impl.render.NotificationsMod;
import dev.tenacity.module.impl.render.ArrayListMod;
import dev.tenacity.module.impl.render.killeffects.KillEffects;
import dev.tenacity.module.impl.player.AntiVoid;
import dev.tenacity.module.impl.player.AutoTool;
import dev.tenacity.module.impl.player.Scaffold;
import dev.tenacity.module.impl.player.SafeWalk;
import dev.tenacity.module.impl.player.FastPlace;
import dev.tenacity.module.impl.player.Freecam;
import dev.tenacity.module.impl.player.Timer;
import dev.tenacity.module.impl.player.NoFall;
import dev.tenacity.module.impl.player.Blink;
import dev.tenacity.module.impl.player.SpeedMine;
import dev.tenacity.module.impl.player.AutoArmor;
import dev.tenacity.module.impl.player.InvManager;
import dev.tenacity.module.impl.player.ChestStealer;
import dev.tenacity.module.impl.movement.AutoHeadHitter;
import dev.tenacity.module.impl.movement.Spider;
import dev.tenacity.module.impl.movement.Jesus;
import dev.tenacity.module.impl.movement.InventoryMove;
import dev.tenacity.module.impl.movement.FastLadder;
import dev.tenacity.module.impl.combat.TargetStrafe;
import dev.tenacity.module.impl.movement.Step;
import dev.tenacity.module.impl.movement.Phase;
import dev.tenacity.module.impl.movement.LongJump;
import dev.tenacity.module.impl.movement.Flight;
import dev.tenacity.module.impl.movement.Speed;
import dev.tenacity.module.impl.movement.NoSlow;
import dev.tenacity.module.impl.movement.Sprint;
import dev.tenacity.module.impl.misc.Sniper;
import dev.tenacity.module.impl.misc.Killsults;
import dev.tenacity.module.impl.misc.AutoAuthenticate;
import dev.tenacity.module.impl.misc.MCF;
import dev.tenacity.module.impl.misc.AutoRespawn;
import dev.tenacity.module.impl.misc.NoRotate;
import dev.tenacity.module.impl.misc.AutoPlay;
import dev.tenacity.module.impl.misc.AutoHypixel;
import dev.tenacity.module.impl.misc.MurderDetector;
import dev.tenacity.module.impl.misc.LightningTracker;
import dev.tenacity.module.impl.misc.AntiFreeze;
import dev.tenacity.module.impl.misc.Spammer;
import dev.tenacity.module.impl.misc.AntiTabComplete;
import dev.tenacity.module.impl.misc.AntiDesync;
import dev.tenacity.module.impl.exploit.Crasher;
import dev.tenacity.module.impl.exploit.ResetVL;
import dev.tenacity.module.impl.exploit.AntiAim;
import dev.tenacity.module.impl.exploit.AntiAura;
import dev.tenacity.module.impl.exploit.TPAKiller;
import dev.tenacity.module.impl.exploit.Regen;
import dev.tenacity.module.impl.exploit.AntiInvis;
import dev.tenacity.module.impl.exploit.Disabler;
import dev.tenacity.module.impl.combat.AutoGapple;
import dev.tenacity.module.api.TargetManager;
import dev.tenacity.module.impl.combat.SuperKnockback;
import dev.tenacity.module.impl.combat.IdleFighter;
import dev.tenacity.module.impl.combat.KeepSprint;
import dev.tenacity.module.impl.combat.FastBow;
import dev.tenacity.module.impl.combat.AutoPot;
import dev.tenacity.module.impl.combat.AutoHead;
import dev.tenacity.module.impl.combat.Criticals;
import dev.tenacity.module.impl.combat.Velocity;
import dev.tenacity.module.impl.combat.KillAura;
import dev.tenacity.module.ModuleCollection;
import dev.tenacity.intent.api.account.IntentAccount;
import dev.tenacity.Tenacity;
import dev.tenacity.module.Module;
import java.util.HashMap;
import store.intent.intentguard.annotation.Native;

@Native
public class ProtectedLaunch
{
    private static final HashMap<Object, Module> modules;
    
    @Native
    @Bootstrap
    public static void start() {
        Tenacity.INSTANCE.setIntentAccount(new IntentAccount());
        Tenacity.INSTANCE.setModuleCollection(new ModuleCollection());
        ProtectedLaunch.modules.put(KillAura.class, new KillAura());
        ProtectedLaunch.modules.put(Velocity.class, new Velocity());
        ProtectedLaunch.modules.put(Criticals.class, new Criticals());
        ProtectedLaunch.modules.put(AutoHead.class, new AutoHead());
        ProtectedLaunch.modules.put(AutoPot.class, new AutoPot());
        ProtectedLaunch.modules.put(FastBow.class, new FastBow());
        ProtectedLaunch.modules.put(KeepSprint.class, new KeepSprint());
        ProtectedLaunch.modules.put(IdleFighter.class, new IdleFighter());
        ProtectedLaunch.modules.put(SuperKnockback.class, new SuperKnockback());
        ProtectedLaunch.modules.put(TargetManager.class, new TargetManager());
        ProtectedLaunch.modules.put(AutoGapple.class, new AutoGapple());
        ProtectedLaunch.modules.put(Disabler.class, new Disabler());
        ProtectedLaunch.modules.put(AntiInvis.class, new AntiInvis());
        ProtectedLaunch.modules.put(Regen.class, new Regen());
        ProtectedLaunch.modules.put(TPAKiller.class, new TPAKiller());
        ProtectedLaunch.modules.put(AntiAura.class, new AntiAura());
        ProtectedLaunch.modules.put(AntiAim.class, new AntiAim());
        ProtectedLaunch.modules.put(ResetVL.class, new ResetVL());
        ProtectedLaunch.modules.put(Crasher.class, new Crasher());
        ProtectedLaunch.modules.put(AntiDesync.class, new AntiDesync());
        ProtectedLaunch.modules.put(AntiTabComplete.class, new AntiTabComplete());
        ProtectedLaunch.modules.put(Spammer.class, new Spammer());
        ProtectedLaunch.modules.put(AntiFreeze.class, new AntiFreeze());
        ProtectedLaunch.modules.put(LightningTracker.class, new LightningTracker());
        ProtectedLaunch.modules.put(MurderDetector.class, new MurderDetector());
        ProtectedLaunch.modules.put(AutoHypixel.class, new AutoHypixel());
        ProtectedLaunch.modules.put(AutoPlay.class, new AutoPlay());
        ProtectedLaunch.modules.put(NoRotate.class, new NoRotate());
        ProtectedLaunch.modules.put(AutoRespawn.class, new AutoRespawn());
        ProtectedLaunch.modules.put(MCF.class, new MCF());
        ProtectedLaunch.modules.put(AutoAuthenticate.class, new AutoAuthenticate());
        ProtectedLaunch.modules.put(Killsults.class, new Killsults());
        ProtectedLaunch.modules.put(Sniper.class, new Sniper());
        ProtectedLaunch.modules.put(Sprint.class, new Sprint());
        ProtectedLaunch.modules.put(NoSlow.class, new NoSlow());
        ProtectedLaunch.modules.put(Speed.class, new Speed());
        ProtectedLaunch.modules.put(Flight.class, new Flight());
        ProtectedLaunch.modules.put(LongJump.class, new LongJump());
        ProtectedLaunch.modules.put(Phase.class, new Phase());
        ProtectedLaunch.modules.put(Step.class, new Step());
        ProtectedLaunch.modules.put(TargetStrafe.class, new TargetStrafe());
        ProtectedLaunch.modules.put(FastLadder.class, new FastLadder());
        ProtectedLaunch.modules.put(InventoryMove.class, new InventoryMove());
        ProtectedLaunch.modules.put(Jesus.class, new Jesus());
        ProtectedLaunch.modules.put(Spider.class, new Spider());
        ProtectedLaunch.modules.put(AutoHeadHitter.class, new AutoHeadHitter());
        ProtectedLaunch.modules.put(ChestStealer.class, new ChestStealer());
        ProtectedLaunch.modules.put(InvManager.class, new InvManager());
        ProtectedLaunch.modules.put(AutoArmor.class, new AutoArmor());
        ProtectedLaunch.modules.put(SpeedMine.class, new SpeedMine());
        ProtectedLaunch.modules.put(Blink.class, new Blink());
        ProtectedLaunch.modules.put(NoFall.class, new NoFall());
        ProtectedLaunch.modules.put(Timer.class, new Timer());
        ProtectedLaunch.modules.put(Freecam.class, new Freecam());
        ProtectedLaunch.modules.put(FastPlace.class, new FastPlace());
        ProtectedLaunch.modules.put(SafeWalk.class, new SafeWalk());
        ProtectedLaunch.modules.put(Scaffold.class, new Scaffold());
        ProtectedLaunch.modules.put(AutoTool.class, new AutoTool());
        ProtectedLaunch.modules.put(AntiVoid.class, new AntiVoid());
        ProtectedLaunch.modules.put(KillEffects.class, new KillEffects());
        ProtectedLaunch.modules.put(ArrayListMod.class, new ArrayListMod());
        ProtectedLaunch.modules.put(NotificationsMod.class, new NotificationsMod());
        ProtectedLaunch.modules.put(ScoreboardMod.class, new ScoreboardMod());
        ProtectedLaunch.modules.put(HUDMod.class, new HUDMod());
        ProtectedLaunch.modules.put(ClickGUIMod.class, new ClickGUIMod());
        ProtectedLaunch.modules.put(Radar.class, new Radar());
        ProtectedLaunch.modules.put(Animations.class, new Animations());
        ProtectedLaunch.modules.put(SpotifyMod.class, new SpotifyMod());
        ProtectedLaunch.modules.put(Ambience.class, new Ambience());
        ProtectedLaunch.modules.put(ChinaHat.class, new ChinaHat());
        ProtectedLaunch.modules.put(GlowESP.class, new GlowESP());
        ProtectedLaunch.modules.put(Brightness.class, new Brightness());
        ProtectedLaunch.modules.put(ESP2D.class, new ESP2D());
        ProtectedLaunch.modules.put(PostProcessing.class, new PostProcessing());
        ProtectedLaunch.modules.put(Statistics.class, new Statistics());
        ProtectedLaunch.modules.put(TargetHUDMod.class, new TargetHUDMod());
        ProtectedLaunch.modules.put(Glint.class, new Glint());
        ProtectedLaunch.modules.put(Breadcrumbs.class, new Breadcrumbs());
        ProtectedLaunch.modules.put(Streamer.class, new Streamer());
        ProtectedLaunch.modules.put(Hitmarkers.class, new Hitmarkers());
        ProtectedLaunch.modules.put(NoHurtCam.class, new NoHurtCam());
        ProtectedLaunch.modules.put(Keystrokes.class, new Keystrokes());
        ProtectedLaunch.modules.put(ItemPhysics.class, new ItemPhysics());
        ProtectedLaunch.modules.put(XRay.class, new XRay());
        ProtectedLaunch.modules.put(EntityCulling.class, new EntityCulling());
        ProtectedLaunch.modules.put(DragonWings.class, new DragonWings());
        ProtectedLaunch.modules.put(PlayerList.class, new PlayerList());
        ProtectedLaunch.modules.put(JumpCircle.class, new JumpCircle());
        ProtectedLaunch.modules.put(CustomModel.class, new CustomModel());
        ProtectedLaunch.modules.put(EntityEffects.class, new EntityEffects());
        ProtectedLaunch.modules.put(Chams.class, new Chams());
        ProtectedLaunch.modules.put(BrightPlayers.class, new BrightPlayers());
        Tenacity.INSTANCE.getModuleCollection().setModules(ProtectedLaunch.modules);
        Theme.init();
        Tenacity.INSTANCE.setPingerUtils(new PingerUtils());
        Tenacity.INSTANCE.setScriptManager(new ScriptManager());
        final CommandHandler commandHandler = new CommandHandler();
        commandHandler.commands.addAll(Arrays.asList(new FriendCommand(), new CopyNameCommand(), new BindCommand(), new UnbindCommand(), new ScriptCommand(), new SettingCommand(), new HelpCommand(), new VClipCommand(), new ClearBindsCommand(), new ClearConfigCommand(), new LoadCommand(), new ToggleCommand()));
        Tenacity.INSTANCE.setCommandHandler(commandHandler);
        Tenacity.INSTANCE.getEventProtocol().register(new BackgroundProcess());
        Tenacity.INSTANCE.setConfigManager(new ConfigManager());
        ConfigManager.defaultConfig = new File(Minecraft.getMinecraft().mcDataDir + "/Tenacity/Config.json");
        Tenacity.INSTANCE.getConfigManager().collectConfigs();
        if (ConfigManager.defaultConfig.exists()) {
            Tenacity.INSTANCE.getConfigManager().loadConfig(Tenacity.INSTANCE.getConfigManager().readConfigData(ConfigManager.defaultConfig.toPath()), true);
        }
        DragManager.loadDragData();
        Tenacity.INSTANCE.setAltManager(new GuiAltManager());
        Tenacity.INSTANCE.setKingGenApi(new KingGenApi());
        try {
            Tenacity.LOGGER.info("Starting ViaMCP...");
            final ViaMCP viaMCP = ViaMCP.getInstance();
            viaMCP.start();
            viaMCP.initAsyncSlider(100, 100, 110, 20);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SafeVarargs
    private static void addModules(final Class<? extends Module>... classes) {
        for (final Class<? extends Module> moduleClass : classes) {
            try {
                ProtectedLaunch.modules.put(moduleClass, (Module)moduleClass.newInstance());
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    static {
        modules = new HashMap<Object, Module>();
    }
}
