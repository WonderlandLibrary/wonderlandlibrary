package dev.tenacity.protection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.tenacity.Tenacity;
import dev.tenacity.commands.CommandHandler;
import dev.tenacity.commands.impl.*;
import dev.tenacity.config.ConfigManager;
import dev.tenacity.config.DragManager;
import dev.tenacity.intent.api.account.GetUserInfo;
import dev.tenacity.intent.api.account.IntentAccount;
import dev.tenacity.module.BackgroundProcess;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCollection;
import dev.tenacity.module.impl.combat.*;
import dev.tenacity.module.impl.exploit.*;
import dev.tenacity.module.impl.misc.*;
import dev.tenacity.module.impl.movement.*;
import dev.tenacity.module.impl.player.Timer;
import dev.tenacity.module.impl.player.*;
import dev.tenacity.module.impl.render.*;
import dev.tenacity.module.impl.render.killeffects.KillEffects;
import dev.tenacity.module.impl.render.wings.DragonWings;
import dev.tenacity.scripting.api.ScriptManager;
import dev.tenacity.ui.altmanager.GuiAltManager;
import dev.tenacity.ui.altmanager.helpers.KingGenApi;
import dev.tenacity.utils.misc.NetworkingUtils;
import dev.tenacity.utils.objects.DiscordAccount;
import dev.tenacity.utils.render.EntityCulling;
import dev.tenacity.utils.render.Theme;
import dev.tenacity.utils.server.PingerUtils;
import dev.tenacity.viamcp.ViaMCP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import store.intent.intentguard.annotation.Bootstrap;
import store.intent.intentguard.annotation.Native;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

@Native
public class ProtectedLaunch {

    private static final HashMap<Object, Module> modules = new HashMap<>();

    @Native
    @Bootstrap
    public static void start() {
        // Setup Intent API access
        Tenacity.INSTANCE.setIntentAccount(GetUserInfo.loginFailure);
        Tenacity.INSTANCE.setModuleCollection(new ModuleCollection());

        // Combat
        modules.put(KillAura.class, new KillAura());
        modules.put(Velocity.class, new Velocity());
        modules.put(Criticals.class, new Criticals());
        modules.put(AutoHead.class, new AutoHead());
        modules.put(AutoPot.class, new AutoPot());
        modules.put(FastBow.class, new FastBow());
        modules.put(KeepSprint.class, new KeepSprint());
        modules.put(IdleFighter.class, new IdleFighter());
        modules.put(SuperKnockback.class, new SuperKnockback());

        // Exploit
        modules.put(Disabler.class, new Disabler());
        modules.put(AntiInvis.class, new AntiInvis());
        modules.put(Regen.class, new Regen());
        modules.put(TPAKiller.class, new TPAKiller());
        modules.put(AntiAura.class, new AntiAura());
        modules.put(ResetVL.class, new ResetVL());
        modules.put(Crasher.class, new Crasher());

        // Misc
        modules.put(AntiDesync.class, new AntiDesync());
        modules.put(AntiTabComplete.class, new AntiTabComplete());
        modules.put(Spammer.class, new Spammer());
        modules.put(AntiFreeze.class, new AntiFreeze());
        modules.put(LightningTracker.class, new LightningTracker());
        modules.put(HackerDetector.class, new HackerDetector());
        modules.put(AutoHypixel.class, new AutoHypixel());
        modules.put(NoRotate.class, new NoRotate());
        modules.put(AutoRespawn.class, new AutoRespawn());
        modules.put(MCF.class, new MCF());
        modules.put(AutoAuthenticate.class, new AutoAuthenticate());
        modules.put(Killsults.class, new Killsults());
        modules.put(Sniper.class, new Sniper());

        // Movement
        modules.put(Sprint.class, new Sprint());
        modules.put(Scaffold.class, new Scaffold());
        modules.put(Speed.class, new Speed());
        modules.put(Flight.class, new Flight());
        modules.put(LongJump.class, new LongJump());
        modules.put(Step.class, new Step());
        modules.put(TargetStrafe.class, new TargetStrafe());
        modules.put(FastLadder.class, new FastLadder());
        modules.put(InventoryMove.class, new InventoryMove());
        modules.put(Jesus.class, new Jesus());
        modules.put(Spider.class, new Spider());
        modules.put(AutoHeadHitter.class, new AutoHeadHitter());

        // Player
        modules.put(ChestStealer.class, new ChestStealer());
        modules.put(InvManager.class, new InvManager());
        modules.put(AutoArmor.class, new AutoArmor());
        modules.put(SpeedMine.class, new SpeedMine());
        modules.put(Blink.class, new Blink());
        modules.put(NoFall.class, new NoFall());
        modules.put(Timer.class, new Timer());
        modules.put(Freecam.class, new Freecam());
        modules.put(FastPlace.class, new FastPlace());
        modules.put(SafeWalk.class, new SafeWalk());
        modules.put(NoSlow.class, new NoSlow());
        modules.put(AutoTool.class, new AutoTool());
        modules.put(AntiVoid.class, new AntiVoid());
        modules.put(KillEffects.class, new KillEffects());

        // Render
        modules.put(ArrayListMod.class, new ArrayListMod());
        modules.put(NotificationsMod.class, new NotificationsMod());
        modules.put(ScoreboardMod.class, new ScoreboardMod());
        modules.put(HUDMod.class, new HUDMod());
        modules.put(ClickGUIMod.class, new ClickGUIMod());
        modules.put(Radar.class, new Radar());
        modules.put(Animations.class, new Animations());
        modules.put(SpotifyMod.class, new SpotifyMod());
        modules.put(Ambience.class, new Ambience());
        modules.put(ChinaHat.class, new ChinaHat());
        modules.put(GlowESP.class, new GlowESP());
        modules.put(Brightness.class, new Brightness());
        modules.put(ESP2D.class, new ESP2D());
        modules.put(PostProcessing.class, new PostProcessing());
        modules.put(Statistics.class, new Statistics());
        modules.put(TargetHUDMod.class, new TargetHUDMod());
        modules.put(Glint.class, new Glint());
        modules.put(Breadcrumbs.class, new Breadcrumbs());
        modules.put(Streamer.class, new Streamer());
        modules.put(Hitmarkers.class, new Hitmarkers());
        modules.put(NoHurtCam.class, new NoHurtCam());
        modules.put(Keystrokes.class, new Keystrokes());
        modules.put(ItemPhysics.class, new ItemPhysics());
        modules.put(XRay.class, new XRay());
        modules.put(EntityCulling.class, new EntityCulling());
        modules.put(DragonWings.class, new DragonWings());
        modules.put(PlayerList.class, new PlayerList());
        modules.put(JumpCircle.class, new JumpCircle());
        modules.put(CustomModel.class, new CustomModel());
        modules.put(EntityEffects.class, new EntityEffects());
        modules.put(Chams.class, new Chams());
        modules.put(BrightPlayers.class, new BrightPlayers());

        Tenacity.INSTANCE.getModuleCollection().setModules(modules);

        Theme.init();

        Tenacity.INSTANCE.setPingerUtils(new PingerUtils());

        Tenacity.INSTANCE.setScriptManager(new ScriptManager());

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.commands.addAll(Arrays.asList(
                new FriendCommand(), new CopyNameCommand(), new BindCommand(), new UnbindCommand(),
                new ScriptCommand(), new SettingCommand(), new HelpCommand(),
                new VClipCommand(), new ClearBindsCommand(), new ClearConfigCommand(),
                new LoadCommand(), new ToggleCommand()
        ));
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


        //String apiKey = Tenacity.INSTANCE.getIntentAccount().api_key;
        //Tenacity.INSTANCE.getIrcUtil().connect(apiKey);

        //Cloud.setApiKey(apiKey);
        //Tenacity.INSTANCE.getCloudDataManager().refreshData();


        Tenacity.INSTANCE.kingGenApi = new KingGenApi();

        try {
            Tenacity.LOGGER.info("Starting ViaMCP...");
            ViaMCP viaMCP = ViaMCP.getInstance();
            viaMCP.start();
            viaMCP.initAsyncSlider(100, 100, 110, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }

        downloadDiscordImages();
    }

    private static void downloadDiscordImages() {
        if (Tenacity.INSTANCE.getIntentAccount().discord_id != null && !Tenacity.INSTANCE.getIntentAccount().discord_id.isEmpty()) {
            IntentAccount intentAccount = Tenacity.INSTANCE.getIntentAccount();
            NetworkingUtils.HttpResponse response = NetworkingUtils.httpsConnection("https://api.senoe.win/discord/user/" + intentAccount.discord_id);
            if (response != null && response.getResponse() == 200) {
                DiscordAccount discordAccount = new DiscordAccount();
                JsonObject responseObject = JsonParser.parseString(response.getContent()).getAsJsonObject();

                if (responseObject.has("avatar")) {
                    String avatarIDActual = responseObject.get("avatar").isJsonNull() ? null : responseObject.get("avatar").getAsString();
                    if (avatarIDActual == null) return;
                    String url = "https://cdn.discordapp.com/avatars/" + intentAccount.discord_id + "/" + avatarIDActual + ".png?size=64";
                    discordAccount.setDiscordAvatar(NetworkingUtils.downloadImage(url));
                }

                if (responseObject.has("banner")) {
                    if (responseObject.get("banner").isJsonNull()) {
                        discordAccount.setBannerColor(responseObject.get("banner_color").isJsonNull() ? "000000" : responseObject.get("banner_color").getAsString().substring(1));
                    } else {
                        // Load the banner image
                        String bannerID = responseObject.get("banner").getAsString();
                        if (bannerID == null) return;
                        String finalURL = "https://cdn.discordapp.com/banners/" + intentAccount.discord_id + "/" + bannerID + ".png?size=256";
                        discordAccount.setDiscordBanner(NetworkingUtils.downloadImage(finalURL));
                    }
                }
                Tenacity.INSTANCE.setDiscordAccount(discordAccount);
            }
        }
    }

    @SafeVarargs
    private static void addModules(Class<? extends Module>... classes) {
        for (Class<? extends Module> moduleClass : classes) {
            try {
                modules.put(moduleClass, moduleClass.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
