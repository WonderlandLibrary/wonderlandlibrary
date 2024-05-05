package tech.atani.client.protection;

import cn.muyang.nativeobfuscator.Native;
import cn.muyang.nativeobfuscator.NotNative;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import de.florianmichael.rclasses.ArrayUtils;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Bootstrap;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.util.*;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.OpenGLException;
import tech.atani.client.feature.guis.screens.mainmenu.atani.AtaniMainMenu;
import tech.atani.client.feature.performance.memory.TextureFix;
import tech.atani.client.loader.Injector;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;
import java.util.UUID;

@Native
public class ProtectedLaunch {

    private static String[] args;

    @NotNative
    public void runMain(String[] args) {
        ProtectedLaunch.args = args;
        System.setProperty("java.net.preferIPv4Stack", "true");
        final OptionParser optionparser = new OptionParser();
        optionparser.allowsUnrecognizedOptions();
        optionparser.accepts("demo");
        optionparser.accepts("fullscreen");
        optionparser.accepts("checkGlErrors");
        final OptionSpec<String> optionspec = optionparser.accepts("server").withRequiredArg();
        final OptionSpec<Integer> optionspec2 = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);
        final OptionSpec<File> optionspec3 = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
        final OptionSpec<File> optionspec4 = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        final OptionSpec<File> optionspec5 = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        final OptionSpec<String> optionspec6 = optionparser.accepts("proxyHost").withRequiredArg();
        final OptionSpec<Integer> optionspec7 = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
        final OptionSpec<String> optionspec8 = optionparser.accepts("proxyUser").withRequiredArg();
        final OptionSpec<String> optionspec9 = optionparser.accepts("proxyPass").withRequiredArg();
        final OptionSpec<String> optionspec10 = optionparser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L);
        final OptionSpec<String> optionspec11 = optionparser.accepts("uuid").withRequiredArg();
        final OptionSpec<String> optionspec12 = optionparser.accepts("accessToken").withRequiredArg().required();
        final OptionSpec<String> optionspec13 = optionparser.accepts("version").withRequiredArg().required();
        final OptionSpec<Integer> optionspec14 = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        final OptionSpec<Integer> optionspec15 = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
        final OptionSpec<String> optionspec16 = optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}");
        final OptionSpec<String> optionspec17 = optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}");
        final OptionSpec<String> optionspec18 = optionparser.accepts("assetIndex").withRequiredArg();
        final OptionSpec<String> optionspec19 = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy");
        final OptionSpec<String> optionspec20 = optionparser.nonOptions();
        final OptionSet optionset = optionparser.parse(args);
        final List<String> list = optionset.valuesOf(optionspec20);

        if (!list.isEmpty()) {
            System.out.println("Completely ignored arguments: " + list);
        }
        final String s = optionset.valueOf(optionspec6);
        Proxy proxy = Proxy.NO_PROXY;
        if (s != null) {
            try {
                proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(s, optionset.valueOf(optionspec7)));
            }
            catch (Exception ex) {}
        }
        final String s2 = optionset.valueOf(optionspec8);
        final String s3 = optionset.valueOf(optionspec9);
        if (!proxy.equals(Proxy.NO_PROXY) && isNullOrEmpty(s2) && isNullOrEmpty(s3)) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(s2, s3.toCharArray());
                }
            });
        }
        final int i = optionset.valueOf(optionspec14);
        final int j = optionset.valueOf(optionspec15);
        final boolean flag = optionset.has("fullscreen");
        final boolean flag2 = optionset.has("checkGlErrors");
        final boolean flag3 = optionset.has("demo");
        final String s4 = optionset.valueOf(optionspec13);
        final Gson gson = new GsonBuilder().registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
        final PropertyMap propertymap = gson.fromJson(optionset.valueOf(optionspec16), PropertyMap.class);
        final PropertyMap propertymap2 = gson.fromJson(optionset.valueOf(optionspec17), PropertyMap.class);
        final File file1 = optionset.valueOf(optionspec3);
        final File file2 = optionset.has(optionspec4) ? optionset.valueOf(optionspec4) : new File(file1, "assets/");
        final File file3 = optionset.has(optionspec5) ? optionset.valueOf(optionspec5) : new File(file1, "resourcepacks/");
        final String s5 = optionset.has(optionspec11) ? optionspec11.value(optionset) : optionspec10.value(optionset);
        final String s6 = optionset.has(optionspec18) ? optionspec18.value(optionset) : null;
        final String s7 = optionset.valueOf(optionspec);
        final Integer integer = optionset.valueOf(optionspec2);
        final Session session = new Session(optionspec10.value(optionset), s5, optionspec12.value(optionset), optionspec19.value(optionset));
        final GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, propertymap, propertymap2, proxy), new GameConfiguration.DisplayInformation(i, j, flag, flag2), new GameConfiguration.FolderInformation(file1, file3, file2, s6), new GameConfiguration.GameInformation(flag3, s4), new GameConfiguration.ServerInformation(s7, integer));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
            @Override
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        createMinecraft(gameconfiguration);
        run();
    }

    private static boolean isNullOrEmpty(String str)
    {
        return str != null && !str.isEmpty();
    }

    private static Minecraft mc;

    @NotNative
    private void createMinecraft(GameConfiguration gameConfig) {
        mc = new Injector();
        Minecraft.theMinecraft = mc;
        GameConfiguration.FolderInformation folderInfo = gameConfig.folderInfo;
        GameConfiguration.GameInformation gameInfo = gameConfig.gameInfo;
        GameConfiguration.DisplayInformation displayInfo = gameConfig.displayInfo;
        GameConfiguration.UserInformation userInfo = gameConfig.userInfo;
        GameConfiguration.ServerInformation serverInfo = gameConfig.serverInfo;

        mc.mcDataDir = folderInfo.mcDataDir;
        mc.fileAssets = folderInfo.assetsDir;
        mc.fileResourcepacks = folderInfo.resourcePacksDir;
        mc.launchedVersion = gameInfo.version;
        mc.twitchDetails = userInfo.userProperties;
        mc.field_181038_N = userInfo.field_181172_c;
        mc.mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(folderInfo.assetsDir, folderInfo.assetIndex).getResourceMap());
        mc.proxy = userInfo.proxy == null ? Proxy.NO_PROXY : userInfo.proxy;
        if (userInfo.proxy != null) {
            mc.sessionService = new YggdrasilAuthenticationService(userInfo.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
        }
        mc.session = userInfo.session;
        Minecraft.logger.info("Setting user: " + mc.session.getUsername());
        Minecraft.logger.info("(Session ID is " + mc.session.getSessionID() + ")");
        mc.isDemo = gameInfo.isDemo;

        int width = gameConfig.displayInfo.width > 0 ? gameConfig.displayInfo.width : 1;
        int height = gameConfig.displayInfo.height > 0 ? gameConfig.displayInfo.height : 1;

        mc.displayWidth = width;
        mc.displayHeight = height;
        mc.tempDisplayWidth = width;
        mc.tempDisplayHeight = height;
        mc.fullscreen = displayInfo.fullscreen;
        mc.jvm64bit = Minecraft.isJvm64bit();
        mc.theIntegratedServer = new IntegratedServer(mc);

        if (serverInfo.serverName != null) {
            mc.serverName = serverInfo.serverName;
            mc.serverPort = serverInfo.serverPort;
        }

        ImageIO.setUseCache(false);
        Bootstrap.register();
    }

    @NotNative
    public void run()
    {
        mc.running = true;

        try
        {
            startGame();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
            crashreport.makeCategory("Initialization");
            mc.displayCrashReport(mc.addGraphicsAndWorldToCrashReport(crashreport));
            return;
        }

        while (true) {
            try
            {
                while (mc.running)
                {
                    if (!mc.hasCrashed || mc.crashReporter == null)
                    {
                        try
                        {
                            mc.runGameLoop();
                        }
                        catch (OutOfMemoryError var10)
                        {
                            mc.freeMemory();
                            mc.displayGuiScreen(new GuiMemoryErrorScreen());
                            System.gc();
                        }
                    }
                    else
                    {
                        mc.displayCrashReport(mc.crashReporter);
                    }
                }
            }
            catch (MinecraftError var12)
            {
                break;
            }
            catch (ReportedException reportedexception)
            {
                mc.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
                mc.freeMemory();
                Minecraft.logger.fatal("Reported exception thrown!", reportedexception);
                mc.displayCrashReport(reportedexception.getCrashReport());
                break;
            }
            catch (Throwable throwable1)
            {
                CrashReport crashreport1 = mc.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
                mc.freeMemory();
                Minecraft.logger.fatal("Unreported exception thrown!", throwable1);
                mc.displayCrashReport(crashreport1);
                break;
            }
            finally
            {
                mc.shutdownMinecraftApplet();
            }

            return;
        }
    }

    public void startGame() throws LWJGLException {
        mc.gameSettings = new GameSettings(mc, mc.mcDataDir);
        mc.defaultResourcePacks.add(mc.mcDefaultResourcePack);
        mc.startTimerHackThread();

        if (mc.gameSettings.overrideHeight > 0 && mc.gameSettings.overrideWidth > 0)
        {
            mc.displayWidth = mc.gameSettings.overrideWidth;
            mc.displayHeight = mc.gameSettings.overrideHeight;
        }
        Minecraft.logger.info("LWJGL Version: " + Sys.getVersion());
        mc.setWindowIcon();
        mc.setInitialDisplayMode();
        mc.createDisplay();
        OpenGlHelper.initializeTextures();
        mc.framebufferMc = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        mc.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        mc.registerMetadataSerializers();
        mc.mcResourcePackRepository = new ResourcePackRepository(mc.fileResourcepacks, new File(mc.mcDataDir, "server-resource-packs"), mc.mcDefaultResourcePack, mc.metadataSerializer_, mc.gameSettings);
        mc.mcResourceManager = new SimpleReloadableResourceManager(mc.metadataSerializer_);
        mc.mcLanguageManager = new LanguageManager(mc.metadataSerializer_, mc.gameSettings.language);
        mc.mcResourceManager.registerReloadListener(mc.mcLanguageManager);
        mc.refreshResources();
        mc.renderEngine = new TextureManager(mc.mcResourceManager);
        mc.mcResourceManager.registerReloadListener(mc.renderEngine);
        mc.drawSplashScreen(mc.renderEngine);
        mc.initStream();
        mc.skinManager = new SkinManager(mc.renderEngine, new File(mc.fileAssets, "skins"), mc.sessionService);
        mc.saveLoader = new AnvilSaveConverter(new File(mc.mcDataDir, "saves"));
        mc.mcSoundHandler = new SoundHandler(mc.mcResourceManager, mc.gameSettings);
        mc.mcResourceManager.registerReloadListener(mc.mcSoundHandler);
        mc.mcMusicTicker = new MusicTicker(mc);
        mc.fontRendererObj = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);

        if (mc.gameSettings.language != null)
        {
            mc.fontRendererObj.setUnicodeFlag(mc.isUnicode());
            mc.fontRendererObj.setBidiFlag(mc.mcLanguageManager.isCurrentLanguageBidirectional());
        }

        mc.standardGalacticFontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), mc.renderEngine, false);
        mc.mcResourceManager.registerReloadListener(mc.fontRendererObj);
        mc.mcResourceManager.registerReloadListener(mc.standardGalacticFontRenderer);
        mc.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        mc.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat()
        {
            public String formatString(String p_74535_1_)
            {
                try
                {
                    return String.format(p_74535_1_, new Object[] {GameSettings.getKeyDisplayString(mc.gameSettings.keyBindInventory.getKeyCode())});
                }
                catch (Exception exception)
                {
                    return "Error: " + exception.getLocalizedMessage();
                }
            }
        });
        mc.mouseHelper = new MouseHelper();
        mc.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0D);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.cullFace(1029);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        mc.checkGLError("Startup");
        mc.textureMapBlocks = new TextureMap("textures");
        mc.textureMapBlocks.setMipmapLevels(mc.gameSettings.mipmapLevels);
        mc.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, mc.textureMapBlocks);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        mc.textureMapBlocks.setBlurMipmapDirect(false, mc.gameSettings.mipmapLevels > 0);
        mc.modelManager = new ModelManager(mc.textureMapBlocks);
        mc.mcResourceManager.registerReloadListener(mc.modelManager);
        mc.renderItem = new RenderItem(mc.renderEngine, mc.modelManager);
        mc.renderManager = new RenderManager(mc.renderEngine, mc.renderItem);
        mc.itemRenderer = new ItemRenderer(mc);
        mc.mcResourceManager.registerReloadListener(mc.renderItem);
        mc.entityRenderer = new EntityRenderer(mc, mc.mcResourceManager);
        mc.mcResourceManager.registerReloadListener(mc.entityRenderer);
        mc.blockRenderDispatcher = new BlockRendererDispatcher(mc.modelManager.getBlockModelShapes(), mc.gameSettings);
        mc.mcResourceManager.registerReloadListener(mc.blockRenderDispatcher);
        mc.renderGlobal = new RenderGlobal(mc);
        mc.mcResourceManager.registerReloadListener(mc.renderGlobal);
        mc.guiAchievement = new GuiAchievement(mc);
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        mc.effectRenderer = new EffectRenderer(mc.theWorld, mc.renderEngine);
        mc.checkGLError("Post startup");
        mc.ingameGUI = new GuiIngame(mc);

        if(!ArrayUtils.contains(args, "stopTextureFix")) {
            TextureFix textureFix = new TextureFix();
            textureFix.runFix();
        }

        AtaniMainMenu guiScreen = new AtaniMainMenu();
        mc.displayGuiScreen(guiScreen);

        mc.renderEngine.deleteTexture(mc.mojangLogo);
        mc.mojangLogo = null;
        mc.loadingScreen = new LoadingScreenRenderer(mc);

        if (mc.gameSettings.fullScreen && !mc.fullscreen)
        {
            mc.toggleFullscreen();
        }

        try
        {
            Display.setVSyncEnabled(mc.gameSettings.enableVsync);
        }
        catch (OpenGLException var2)
        {
            mc.gameSettings.enableVsync = false;
            mc.gameSettings.saveOptions();
        }

        mc.renderGlobal.makeEntityOutlineShader();
    }

}
