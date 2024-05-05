package net.minecraft.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import events.Event;
import events.EventType;
import events.listeners.EventClick;
import events.listeners.EventMouseOver;
import events.listeners.EventTick;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Delta;
import intent.AquaDev.aqua.modules.Module;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import windows.UIDGui;

public class Minecraft
implements IThreadListener,
IPlayerUsage {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
    public static final boolean isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;
    public static byte[] memoryReserve = new byte[0xA00000];
    private static final List<DisplayMode> macDisplayModes = Lists.newArrayList((Object[])new DisplayMode[]{new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)});
    private final File fileResourcepacks;
    private final PropertyMap twitchDetails;
    private final PropertyMap profileProperties;
    private ServerData currentServerData;
    private TextureManager renderEngine;
    private static Minecraft theMinecraft;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private final boolean enableGLErrorChecking = true;
    private boolean hasCrashed;
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;
    private boolean connectedToRealms = false;
    public final Timer timer = new Timer(20.0f);
    private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", (IPlayerUsage)this, MinecraftServer.getCurrentTimeMillis());
    public WorldClient theWorld;
    public RenderGlobal renderGlobal;
    private RenderManager renderManager;
    private RenderItem renderItem;
    private ItemRenderer itemRenderer;
    public EntityPlayerSP thePlayer;
    private Entity renderViewEntity;
    public Entity pointedEntity;
    public EffectRenderer effectRenderer;
    public Session session;
    private boolean isGamePaused;
    long lastFrame = Minecraft.getSystemTime();
    public FontRenderer fontRendererObj;
    public FontRenderer standardGalacticFontRenderer;
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;
    private int leftClickCounter;
    private final int tempDisplayWidth;
    private final int tempDisplayHeight;
    private IntegratedServer theIntegratedServer;
    public GuiAchievement guiAchievement;
    public GuiIngame ingameGUI;
    public boolean skipRenderWorld;
    public MovingObjectPosition objectMouseOver;
    public GameSettings gameSettings;
    public MouseHelper mouseHelper;
    public final File mcDataDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final Proxy proxy;
    private ISaveFormat saveLoader;
    private static int debugFPS;
    public int rightClickDelayTimer;
    private String serverName;
    private int serverPort;
    public boolean inGameHasFocus;
    long systemTime = Minecraft.getSystemTime();
    private int joinPlayerCounter;
    public final FrameTimer frameTimer = new FrameTimer();
    long startNanoTime = System.nanoTime();
    private final boolean jvm64bit;
    private final boolean isDemo;
    private NetworkManager myNetworkManager;
    private boolean integratedServerIsRunning;
    public final Profiler mcProfiler = new Profiler();
    private long debugCrashKeyPressTime = -1L;
    private IReloadableResourceManager mcResourceManager;
    private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
    private final List<IResourcePack> defaultResourcePacks = Lists.newArrayList();
    private final DefaultResourcePack mcDefaultResourcePack;
    private ResourcePackRepository mcResourcePackRepository;
    private LanguageManager mcLanguageManager;
    private Framebuffer framebufferMc;
    private TextureMap textureMapBlocks;
    private SoundHandler mcSoundHandler;
    private MusicTicker mcMusicTicker;
    private ResourceLocation mojangLogo;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
    private final long field_175615_aJ = 0L;
    private final Thread mcThread = Thread.currentThread();
    private ModelManager modelManager;
    private BlockRendererDispatcher blockRenderDispatcher;
    volatile boolean running = true;
    public String debug = "";
    public boolean field_175613_B = false;
    public boolean field_175614_C = false;
    public boolean field_175611_D = false;
    public boolean renderChunksMany = true;
    long debugUpdateTime = Minecraft.getSystemTime();
    int fpsCounter;
    long prevFrameTime = -1L;
    private String debugProfilerName = "root";

    public Minecraft(GameConfiguration gameConfig) {
        theMinecraft = this;
        this.mcDataDir = gameConfig.folderInfo.mcDataDir;
        this.fileAssets = gameConfig.folderInfo.assetsDir;
        this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
        this.launchedVersion = gameConfig.gameInfo.version;
        this.twitchDetails = gameConfig.userInfo.userProperties;
        this.profileProperties = gameConfig.userInfo.profileProperties;
        this.mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(gameConfig.folderInfo.assetsDir, gameConfig.folderInfo.assetIndex).getResourceMap());
        this.proxy = gameConfig.userInfo.proxy == null ? Proxy.NO_PROXY : gameConfig.userInfo.proxy;
        this.sessionService = new YggdrasilAuthenticationService(gameConfig.userInfo.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
        this.session = gameConfig.userInfo.session;
        logger.info("Setting user: " + this.session.getUsername());
        logger.info("(Session ID is " + this.session.getSessionID() + ")");
        this.isDemo = gameConfig.gameInfo.isDemo;
        this.displayWidth = gameConfig.displayInfo.width > 0 ? gameConfig.displayInfo.width : 1;
        this.displayHeight = gameConfig.displayInfo.height > 0 ? gameConfig.displayInfo.height : 1;
        this.tempDisplayWidth = gameConfig.displayInfo.width;
        this.tempDisplayHeight = gameConfig.displayInfo.height;
        this.fullscreen = gameConfig.displayInfo.fullscreen;
        this.jvm64bit = Minecraft.isJvm64bit();
        try {
            this.theIntegratedServer = new IntegratedServer(this);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (gameConfig.serverInfo.serverName != null) {
            this.serverName = gameConfig.serverInfo.serverName;
            this.serverPort = gameConfig.serverInfo.serverPort;
        }
        ImageIO.setUseCache((boolean)false);
        Bootstrap.register();
    }

    public void run() {
        block15: {
            this.running = true;
            try {
                this.startGame();
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Initializing game");
                crashreport.makeCategory("Initialization");
                this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(crashreport));
                return;
            }
            try {
                while (this.running) {
                    if (!this.hasCrashed || this.crashReporter == null) {
                        try {
                            this.runGameLoop();
                        }
                        catch (OutOfMemoryError var10) {
                            this.freeMemory();
                            this.displayGuiScreen((GuiScreen)new GuiMemoryErrorScreen());
                            System.gc();
                        }
                        continue;
                    }
                    this.displayCrashReport(this.crashReporter);
                }
            }
            catch (MinecraftError var12) {
                break block15;
            }
            catch (ReportedException reportedexception) {
                this.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
                this.freeMemory();
                logger.fatal("Reported exception thrown!", (Throwable)reportedexception);
                this.displayCrashReport(reportedexception.getCrashReport());
                break block15;
            }
            catch (Throwable throwable1) {
                CrashReport crashreport1 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
                this.freeMemory();
                logger.fatal("Unreported exception thrown!", throwable1);
                this.displayCrashReport(crashreport1);
                break block15;
            }
            finally {
                this.shutdownMinecraftApplet();
            }
            return;
        }
    }

    private void startGame() throws LWJGLException, IOException {
        this.gameSettings = new GameSettings(this, this.mcDataDir);
        this.defaultResourcePacks.add((Object)this.mcDefaultResourcePack);
        this.startTimerHackThread();
        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }
        logger.info("LWJGL Version: " + Sys.getVersion());
        this.setWindowIcon();
        this.setInitialDisplayMode();
        this.createDisplay();
        OpenGlHelper.initializeTextures();
        this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true);
        this.framebufferMc.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.registerMetadataSerializers();
        this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), (IResourcePack)this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings);
        this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.mcLanguageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager((IResourceManager)this.mcResourceManager);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.renderEngine);
        this.drawSplashScreen(this.renderEngine);
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
        this.mcSoundHandler = new SoundHandler((IResourceManager)this.mcResourceManager, this.gameSettings);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(this);
        this.fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
        if (this.gameSettings.language != null) {
            this.fontRendererObj.setUnicodeFlag(this.isUnicode());
            this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
        }
        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.fontRendererObj);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)new FoliageColorReloadListener());
        AchievementList.openInventory.setStatStringFormatter((IStatStringFormat)new /* Unavailable Anonymous Inner Class!! */);
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel((int)7425);
        GlStateManager.clearDepth((double)1.0);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc((int)515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        GlStateManager.cullFace((int)1029);
        GlStateManager.matrixMode((int)5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode((int)5888);
        this.checkGLError("Startup");
        this.textureMapBlocks = new TextureMap("textures");
        this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
        this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, (ITickableTextureObject)this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
        this.modelManager = new ModelManager(this.textureMapBlocks);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.modelManager);
        this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        this.itemRenderer = new ItemRenderer(this);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.renderItem);
        this.entityRenderer = new EntityRenderer(this, (IResourceManager)this.mcResourceManager);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.entityRenderer);
        this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.blockRenderDispatcher);
        this.renderGlobal = new RenderGlobal(this);
        this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.renderGlobal);
        this.guiAchievement = new GuiAchievement(this);
        GlStateManager.viewport((int)0, (int)0, (int)this.displayWidth, (int)this.displayHeight);
        this.effectRenderer = new EffectRenderer((World)this.theWorld, this.renderEngine);
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);
        this.displayGuiScreen((GuiScreen)new UIDGui());
        new Aqua();
        if (this.serverName != null) {
            this.renderEngine.deleteTexture(this.mojangLogo);
        }
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(this);
        if (this.gameSettings.fullScreen && !this.fullscreen) {
            this.toggleFullscreen();
        }
        try {
            Display.setVSyncEnabled((boolean)this.gameSettings.enableVsync);
        }
        catch (OpenGLException var2) {
            this.gameSettings.enableVsync = false;
            this.gameSettings.saveOptions();
        }
        this.renderGlobal.makeEntityOutlineShader();
    }

    private void registerMetadataSerializers() {
        this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }

    private void createDisplay() throws LWJGLException {
        Display.setResizable((boolean)true);
        Display.setTitle((String)("" + Aqua.name + " " + Aqua.build + " by " + Aqua.dev));
        try {
            Display.create((PixelFormat)new PixelFormat().withDepthBits(24));
        }
        catch (LWJGLException lwjglexception) {
            logger.error("Couldn't set pixel format", (Throwable)lwjglexception);
            try {
                Thread.sleep((long)1000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            if (this.fullscreen) {
                this.updateDisplayMode();
            }
            Display.create();
        }
    }

    private void setInitialDisplayMode() throws LWJGLException {
        if (this.fullscreen) {
            Display.setFullscreen((boolean)true);
            DisplayMode displaymode = Display.getDisplayMode();
            this.displayWidth = Math.max((int)1, (int)displaymode.getWidth());
            this.displayHeight = Math.max((int)1, (int)displaymode.getHeight());
        } else {
            Display.setDisplayMode((DisplayMode)new DisplayMode(this.displayWidth, this.displayHeight));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void setWindowIcon() {
        Util.EnumOS util$enumos = Util.getOSType();
        if (util$enumos != Util.EnumOS.OSX) {
            InputStream inputstream1;
            InputStream inputstream;
            block5: {
                inputstream = null;
                inputstream1 = null;
                try {
                    inputstream = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
                    inputstream1 = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
                    if (inputstream == null || inputstream1 == null) break block5;
                    Display.setIcon((ByteBuffer[])new ByteBuffer[]{this.readImageToBuffer(inputstream), this.readImageToBuffer(inputstream1)});
                }
                catch (IOException ioexception) {
                    try {
                        logger.error("Couldn't set icon", (Throwable)ioexception);
                    }
                    catch (Throwable throwable) {
                        IOUtils.closeQuietly(inputstream);
                        IOUtils.closeQuietly(inputstream1);
                        throw throwable;
                    }
                    IOUtils.closeQuietly((InputStream)inputstream);
                    IOUtils.closeQuietly(inputstream1);
                }
            }
            IOUtils.closeQuietly((InputStream)inputstream);
            IOUtils.closeQuietly((InputStream)inputstream1);
        }
    }

    private static boolean isJvm64bit() {
        String[] astring;
        for (String s : astring = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"}) {
            String s1 = System.getProperty((String)s);
            if (s1 == null || !s1.contains((CharSequence)"64")) continue;
            return true;
        }
        return false;
    }

    public Framebuffer getFramebuffer() {
        return this.framebufferMc;
    }

    public String getVersion() {
        return this.launchedVersion;
    }

    private void startTimerHackThread() {
        2 thread = new /* Unavailable Anonymous Inner Class!! */;
        thread.setDaemon(true);
        thread.start();
    }

    public void crashed(CrashReport crash) {
        this.hasCrashed = true;
        this.crashReporter = crash;
    }

    public void displayCrashReport(CrashReport crashReportIn) {
        File file1 = new File(Minecraft.getMinecraft().mcDataDir, "crash-reports");
        File file2 = new File(file1, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT((String)crashReportIn.getCompleteReport());
        if (crashReportIn.getFile() != null) {
            Bootstrap.printToSYSOUT((String)("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile()));
            System.exit((int)-1);
        } else if (crashReportIn.saveToFile(file2)) {
            Bootstrap.printToSYSOUT((String)("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath()));
            System.exit((int)-1);
        } else {
            Bootstrap.printToSYSOUT((String)"#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit((int)-2);
        }
    }

    public boolean isUnicode() {
        return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
    }

    public void refreshResources() {
        ArrayList list = Lists.newArrayList(this.defaultResourcePacks);
        for (ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries()) {
            list.add((Object)resourcepackrepository$entry.getResourcePack());
        }
        if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
            list.add((Object)this.mcResourcePackRepository.getResourcePackInstance());
        }
        try {
            this.mcResourceManager.reloadResources((List)list);
        }
        catch (RuntimeException runtimeexception) {
            logger.info("Caught error stitching, removing all assigned resourcepacks", (Throwable)runtimeexception);
            list.clear();
            list.addAll(this.defaultResourcePacks);
            this.mcResourcePackRepository.setRepositories(Collections.emptyList());
            this.mcResourceManager.reloadResources((List)list);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.incompatibleResourcePacks.clear();
            this.gameSettings.saveOptions();
        }
        this.mcLanguageManager.parseLanguageMetadata((List)list);
        if (this.renderGlobal != null) {
            this.renderGlobal.loadRenderers();
        }
    }

    private ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read((InputStream)imageStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[])null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate((int)(4 * aint.length));
        for (int i : aint) {
            bytebuffer.putInt(i << 8 | i >> 24 & 0xFF);
        }
        bytebuffer.flip();
        return bytebuffer;
    }

    private void updateDisplayMode() throws LWJGLException {
        HashSet set = Sets.newHashSet();
        Collections.addAll((Collection)set, (Object[])Display.getAvailableDisplayModes());
        DisplayMode displaymode = Display.getDesktopDisplayMode();
        if (!set.contains((Object)displaymode) && Util.getOSType() == Util.EnumOS.OSX) {
            block0: for (DisplayMode displaymode1 : macDisplayModes) {
                boolean flag = true;
                for (DisplayMode displaymode2 : set) {
                    if (displaymode2.getBitsPerPixel() != 32 || displaymode2.getWidth() != displaymode1.getWidth() || displaymode2.getHeight() != displaymode1.getHeight()) continue;
                    flag = false;
                    break;
                }
                if (flag) continue;
                for (DisplayMode displaymode3 : set) {
                    if (displaymode3.getBitsPerPixel() != 32 || displaymode3.getWidth() != displaymode1.getWidth() / 2 || displaymode3.getHeight() != displaymode1.getHeight() / 2) continue;
                    displaymode = displaymode3;
                    continue block0;
                }
            }
        }
        Display.setDisplayMode((DisplayMode)displaymode);
        this.displayWidth = displaymode.getWidth();
        this.displayHeight = displaymode.getHeight();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void drawSplashScreen(TextureManager textureManagerInstance) throws LWJGLException {
        ScaledResolution scaledresolution = new ScaledResolution(this);
        int i = scaledresolution.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode((int)5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho((double)0.0, (double)scaledresolution.getScaledWidth(), (double)scaledresolution.getScaledHeight(), (double)0.0, (double)1000.0, (double)3000.0);
        GlStateManager.matrixMode((int)5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)-2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        InputStream inputstream = null;
        try {
            inputstream = this.mcDefaultResourcePack.getInputStream(locationMojangPng);
            this.mojangLogo = textureManagerInstance.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read((InputStream)inputstream)));
            textureManagerInstance.bindTexture(this.mojangLogo);
        }
        catch (IOException ioexception) {
            try {
                logger.error("Unable to load logo: " + locationMojangPng, (Throwable)ioexception);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(inputstream);
                throw throwable;
            }
            IOUtils.closeQuietly((InputStream)inputstream);
        }
        IOUtils.closeQuietly((InputStream)inputstream);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, (double)this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)this.displayWidth, (double)this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)this.displayWidth, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int j = 256;
        int k = 256;
        this.draw((scaledresolution.getScaledWidth() - 256) / 2, (scaledresolution.getScaledHeight() - 256) / 2, 0, 0, 256, 256, 255, 255, 255, 255);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        this.updateDisplay();
    }

    public void draw(int posX, int posY, int texU, int texV, int width, int height, int red, int green, int blue, int alpha) {
        float f = 0.00390625f;
        float f1 = 0.00390625f;
        WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos((double)posX, (double)(posY + height), 0.0).tex((double)((float)texU * 0.00390625f), (double)((float)(texV + height) * 0.00390625f)).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos((double)(posX + width), (double)(posY + height), 0.0).tex((double)((float)(texU + width) * 0.00390625f), (double)((float)(texV + height) * 0.00390625f)).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos((double)(posX + width), (double)posY, 0.0).tex((double)((float)(texU + width) * 0.00390625f), (double)((float)texV * 0.00390625f)).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos((double)posX, (double)posY, 0.0).tex((double)((float)texU * 0.00390625f), (double)((float)texV * 0.00390625f)).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }

    public void displayGuiScreen(GuiScreen guiScreenIn) {
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }
        if (guiScreenIn == null && this.theWorld == null) {
            guiScreenIn = new GuiMainMenu();
        } else if (guiScreenIn == null && this.thePlayer.getHealth() <= 0.0f) {
            guiScreenIn = new GuiGameOver();
        }
        if (guiScreenIn instanceof GuiMainMenu) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages();
        }
        this.currentScreen = guiScreenIn;
        if (guiScreenIn != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(this);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(this, i, j);
            this.skipRenderWorld = false;
        } else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }

    private void checkGLError(String message) {
        this.getClass();
        int i = GL11.glGetError();
        if (i != 0) {
            String s = GLU.gluErrorString((int)i);
            logger.error("########## GL ERROR ##########");
            logger.error("@ " + message);
            logger.error(i + ": " + s);
        }
    }

    public void shutdownMinecraftApplet() {
        try {
            logger.info("Stopping!");
            try {
                this.loadWorld(null);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            this.mcSoundHandler.unloadSounds();
        }
        finally {
            Display.destroy();
            if (!this.hasCrashed) {
                System.exit((int)0);
            }
        }
        System.gc();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void runGameLoop() throws IOException {
        long currentTime = Minecraft.getSystemTime();
        int deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        Delta.DELTATIME = deltaTime;
        long i = System.nanoTime();
        this.mcProfiler.startSection("root");
        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }
        if (this.isGamePaused && this.theWorld != null) {
            float f = this.timer.renderPartialTicks;
            this.timer.updateTimer();
            this.timer.renderPartialTicks = f;
        } else {
            this.timer.updateTimer();
        }
        this.mcProfiler.startSection("scheduledExecutables");
        Queue<FutureTask<?>> f = this.scheduledTasks;
        synchronized (f) {
            while (!this.scheduledTasks.isEmpty()) {
                Util.runTask((FutureTask)((FutureTask)this.scheduledTasks.poll()), (Logger)logger);
            }
        }
        this.mcProfiler.endSection();
        long l = System.nanoTime();
        this.mcProfiler.startSection("tick");
        for (int j = 0; j < this.timer.elapsedTicks; ++j) {
            this.runTick();
        }
        this.mcProfiler.endStartSection("preRenderErrors");
        long i1 = System.nanoTime() - l;
        this.checkGLError("Pre render");
        this.mcProfiler.endStartSection("sound");
        this.mcSoundHandler.setListener((EntityPlayer)this.thePlayer, this.timer.renderPartialTicks);
        this.mcProfiler.endSection();
        this.mcProfiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear((int)16640);
        this.framebufferMc.bindFramebuffer(true);
        this.mcProfiler.startSection("display");
        GlStateManager.enableTexture2D();
        if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
            this.gameSettings.thirdPersonView = 0;
        }
        this.mcProfiler.endSection();
        if (!this.skipRenderWorld) {
            this.mcProfiler.endStartSection("gameRenderer");
            this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks, i);
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
            if (!this.mcProfiler.profilingEnabled) {
                this.mcProfiler.clearProfiling();
            }
            this.mcProfiler.profilingEnabled = true;
            this.displayDebugInfo(i1);
        } else {
            this.mcProfiler.profilingEnabled = false;
            this.prevFrameTime = System.nanoTime();
        }
        this.guiAchievement.updateAchievementWindow();
        this.framebufferMc.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.entityRenderer.renderStreamIndicator(this.timer.renderPartialTicks);
        GlStateManager.popMatrix();
        this.mcProfiler.startSection("root");
        this.updateDisplay();
        Thread.yield();
        this.mcProfiler.startSection("stream");
        this.mcProfiler.startSection("update");
        this.mcProfiler.endStartSection("submit");
        this.mcProfiler.endSection();
        this.mcProfiler.endSection();
        this.checkGLError("Post render");
        ++this.fpsCounter;
        this.isGamePaused = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic();
        long k = System.nanoTime();
        this.frameTimer.addFrame(k - this.startNanoTime);
        this.startNanoTime = k;
        while (Minecraft.getSystemTime() >= this.debugUpdateTime + 1000L) {
            debugFPS = this.fpsCounter * 3;
            Object[] objectArray = new Object[8];
            objectArray[0] = debugFPS;
            objectArray[1] = RenderChunk.renderChunksUpdated;
            objectArray[2] = RenderChunk.renderChunksUpdated != 1 ? "s" : "";
            objectArray[3] = (float)this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : Integer.valueOf((int)this.gameSettings.limitFramerate);
            objectArray[4] = this.gameSettings.enableVsync ? " vsync" : "";
            Object object = objectArray[5] = this.gameSettings.fancyGraphics ? "" : " fast";
            objectArray[6] = this.gameSettings.clouds == 0 ? "" : (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds");
            objectArray[7] = OpenGlHelper.useVbo() ? " vbo" : "";
            this.debug = String.format((String)"%d fps (%d chunk update%s) T: %s%s%s%s%s", (Object[])objectArray);
            RenderChunk.renderChunksUpdated = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.usageSnooper.addMemoryStatsToSnooper();
            if (this.usageSnooper.isSnooperRunning()) continue;
            this.usageSnooper.startSnooper();
        }
        if (this.isFramerateLimitBelowMax()) {
            this.mcProfiler.startSection("fpslimit_wait");
            Display.sync((int)this.getLimitFramerate());
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
    }

    public void updateDisplay() {
        this.mcProfiler.startSection("display_update");
        Display.update();
        this.mcProfiler.endSection();
        this.checkWindowResize();
    }

    protected void checkWindowResize() {
        if (!this.fullscreen && Display.wasResized()) {
            int i = this.displayWidth;
            int j = this.displayHeight;
            this.displayWidth = Display.getWidth();
            this.displayHeight = Display.getHeight();
            if (this.displayWidth != i || this.displayHeight != j) {
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
                this.resize(this.displayWidth, this.displayHeight);
            }
        }
    }

    public int getLimitFramerate() {
        return this.theWorld == null && this.currentScreen != null ? 30 : this.gameSettings.limitFramerate;
    }

    public boolean isFramerateLimitBelowMax() {
        return (float)this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }

    public void freeMemory() {
        try {
            memoryReserve = new byte[0];
            this.renderGlobal.deleteAllDisplayLists();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            System.gc();
            this.loadWorld(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        System.gc();
    }

    private void updateDebugProfilerName(int keyCount) {
        List list = this.mcProfiler.getProfilingData(this.debugProfilerName);
        if (list != null && !list.isEmpty()) {
            Profiler.Result profiler$result = (Profiler.Result)list.remove(0);
            if (keyCount == 0) {
                int i;
                if (profiler$result.field_76331_c.length() > 0 && (i = this.debugProfilerName.lastIndexOf(".")) >= 0) {
                    this.debugProfilerName = this.debugProfilerName.substring(0, i);
                }
            } else if (--keyCount < list.size() && !((Profiler.Result)list.get((int)keyCount)).field_76331_c.equals((Object)"unspecified")) {
                if (this.debugProfilerName.length() > 0) {
                    this.debugProfilerName = this.debugProfilerName + ".";
                }
                this.debugProfilerName = this.debugProfilerName + ((Profiler.Result)list.get((int)keyCount)).field_76331_c;
            }
        }
    }

    private void displayDebugInfo(long elapsedTicksTime) {
        if (this.mcProfiler.profilingEnabled) {
            List list = this.mcProfiler.getProfilingData(this.debugProfilerName);
            Profiler.Result profiler$result = (Profiler.Result)list.remove(0);
            GlStateManager.clear((int)256);
            GlStateManager.matrixMode((int)5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho((double)0.0, (double)this.displayWidth, (double)this.displayHeight, (double)0.0, (double)1000.0, (double)3000.0);
            GlStateManager.matrixMode((int)5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)-2000.0f);
            GL11.glLineWidth((float)1.0f);
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 160;
            int j = this.displayWidth - 160 - 10;
            int k = this.displayHeight - 320;
            GlStateManager.enableBlend();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double)((float)j - 176.0f), (double)((float)k - 96.0f - 16.0f), 0.0).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos((double)((float)j - 176.0f), (double)(k + 320), 0.0).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos((double)((float)j + 176.0f), (double)(k + 320), 0.0).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos((double)((float)j + 176.0f), (double)((float)k - 96.0f - 16.0f), 0.0).color(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            double d0 = 0.0;
            for (int l = 0; l < list.size(); ++l) {
                Profiler.Result profiler$result1 = (Profiler.Result)list.get(l);
                int i1 = MathHelper.floor_double((double)(profiler$result1.field_76332_a / 4.0)) + 1;
                worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                int j1 = profiler$result1.getColor();
                int k1 = j1 >> 16 & 0xFF;
                int l1 = j1 >> 8 & 0xFF;
                int i2 = j1 & 0xFF;
                worldrenderer.pos((double)j, (double)k, 0.0).color(k1, l1, i2, 255).endVertex();
                for (int j2 = i1; j2 >= 0; --j2) {
                    float f = (float)((d0 + profiler$result1.field_76332_a * (double)j2 / (double)i1) * Math.PI * 2.0 / 100.0);
                    float f1 = MathHelper.sin((float)f) * 160.0f;
                    float f2 = MathHelper.cos((float)f) * 160.0f * 0.5f;
                    worldrenderer.pos((double)((float)j + f1), (double)((float)k - f2), 0.0).color(k1, l1, i2, 255).endVertex();
                }
                tessellator.draw();
                worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                for (int i3 = i1; i3 >= 0; --i3) {
                    float f3 = (float)((d0 + profiler$result1.field_76332_a * (double)i3 / (double)i1) * Math.PI * 2.0 / 100.0);
                    float f4 = MathHelper.sin((float)f3) * 160.0f;
                    float f5 = MathHelper.cos((float)f3) * 160.0f * 0.5f;
                    worldrenderer.pos((double)((float)j + f4), (double)((float)k - f5), 0.0).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                    worldrenderer.pos((double)((float)j + f4), (double)((float)k - f5 + 10.0f), 0.0).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                }
                tessellator.draw();
                d0 += profiler$result1.field_76332_a;
            }
            DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            String s = "";
            if (!profiler$result.field_76331_c.equals((Object)"unspecified")) {
                s = s + "[0] ";
            }
            s = profiler$result.field_76331_c.length() == 0 ? s + "ROOT " : s + profiler$result.field_76331_c + " ";
            int l2 = 0xFFFFFF;
            this.fontRendererObj.drawStringWithShadow(s, (float)(j - 160), (float)(k - 80 - 16), 0xFFFFFF);
            s = decimalformat.format(profiler$result.field_76330_b) + "%";
            this.fontRendererObj.drawStringWithShadow(s, (float)(j + 160 - this.fontRendererObj.getStringWidth(s)), (float)(k - 80 - 16), 0xFFFFFF);
            for (int k2 = 0; k2 < list.size(); ++k2) {
                Profiler.Result profiler$result2 = (Profiler.Result)list.get(k2);
                String s1 = "";
                s1 = profiler$result2.field_76331_c.equals((Object)"unspecified") ? s1 + "[?] " : s1 + "[" + (k2 + 1) + "] ";
                s1 = s1 + profiler$result2.field_76331_c;
                this.fontRendererObj.drawStringWithShadow(s1, (float)(j - 160), (float)(k + 80 + k2 * 8 + 20), profiler$result2.getColor());
                s1 = decimalformat.format(profiler$result2.field_76332_a) + "%";
                this.fontRendererObj.drawStringWithShadow(s1, (float)(j + 160 - 50 - this.fontRendererObj.getStringWidth(s1)), (float)(k + 80 + k2 * 8 + 20), profiler$result2.getColor());
                s1 = decimalformat.format(profiler$result2.field_76330_b) + "%";
                this.fontRendererObj.drawStringWithShadow(s1, (float)(j + 160 - this.fontRendererObj.getStringWidth(s1)), (float)(k + 80 + k2 * 8 + 20), profiler$result2.getColor());
            }
        }
    }

    public void shutdown() {
        this.running = false;
    }

    public void setIngameFocus() {
        if (Display.isActive() && !this.inGameHasFocus) {
            this.inGameHasFocus = true;
            this.mouseHelper.grabMouseCursor();
            this.displayGuiScreen(null);
            this.leftClickCounter = 10000;
        }
    }

    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            KeyBinding.unPressAllKeys();
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }

    public void displayInGameMenu() {
        if (this.currentScreen == null) {
            this.displayGuiScreen((GuiScreen)new GuiIngameMenu());
            if (this.isSingleplayer() && !this.theIntegratedServer.getPublic()) {
                this.mcSoundHandler.pauseSounds();
            }
        }
    }

    private void sendClickBlockToController(boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }
        if (this.leftClickCounter <= 0 && !this.thePlayer.isUsingItem()) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockpos = this.objectMouseOver.getBlockPos();
                if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air && this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.sideHit)) {
                    this.effectRenderer.addBlockHitEffects(blockpos, this.objectMouseOver.sideHit);
                    this.thePlayer.swingItem();
                }
            } else {
                this.playerController.resetBlockRemoving();
            }
        }
    }

    public void clickMouse() {
        if (this.leftClickCounter <= 0) {
            this.thePlayer.swingItem();
            if (this.objectMouseOver == null) {
                logger.error("Null returned as 'hitResult', this shouldn't happen!");
                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            } else {
                switch (17.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[this.objectMouseOver.typeOfHit.ordinal()]) {
                    case 1: {
                        this.playerController.attackEntity((EntityPlayer)this.thePlayer, this.objectMouseOver.entityHit);
                        break;
                    }
                    case 2: {
                        BlockPos blockpos = this.objectMouseOver.getBlockPos();
                        if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
                            break;
                        }
                    }
                    default: {
                        if (!this.playerController.isNotCreative()) break;
                        this.leftClickCounter = 10;
                    }
                }
            }
        }
    }

    public void rightClickMouse() {
        if (!this.playerController.getIsHittingBlock()) {
            ItemStack itemstack1;
            this.rightClickDelayTimer = 4;
            boolean flag = true;
            ItemStack itemstack = this.thePlayer.inventory.getCurrentItem();
            if (this.objectMouseOver == null) {
                logger.warn("Null returned as 'hitResult', this shouldn't happen!");
            } else {
                switch (17.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[this.objectMouseOver.typeOfHit.ordinal()]) {
                    case 1: {
                        if (this.playerController.isPlayerRightClickingOnEntity((EntityPlayer)this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver)) {
                            flag = false;
                            break;
                        }
                        if (!this.playerController.interactWithEntitySendPacket((EntityPlayer)this.thePlayer, this.objectMouseOver.entityHit)) break;
                        flag = false;
                        break;
                    }
                    case 2: {
                        int i;
                        BlockPos blockpos = this.objectMouseOver.getBlockPos();
                        if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() == Material.air) break;
                        int n = i = itemstack != null ? itemstack.stackSize : 0;
                        if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemstack, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec)) {
                            flag = false;
                            this.thePlayer.swingItem();
                        }
                        if (itemstack == null) {
                            return;
                        }
                        if (itemstack.stackSize == 0) {
                            this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                            break;
                        }
                        if (itemstack.stackSize == i && !this.playerController.isInCreativeMode()) break;
                        this.entityRenderer.itemRenderer.resetEquippedProgress();
                    }
                }
            }
            if (flag && (itemstack1 = this.thePlayer.inventory.getCurrentItem()) != null && this.playerController.sendUseItem((EntityPlayer)this.thePlayer, (World)this.theWorld, itemstack1)) {
                this.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
        }
    }

    public void toggleFullscreen() {
        try {
            this.gameSettings.fullScreen = this.fullscreen = !this.fullscreen;
            if (this.fullscreen) {
                this.updateDisplayMode();
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            } else {
                Display.setDisplayMode((DisplayMode)new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
                this.displayWidth = this.tempDisplayWidth;
                this.displayHeight = this.tempDisplayHeight;
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            }
            if (this.currentScreen != null) {
                this.resize(this.displayWidth, this.displayHeight);
            } else {
                this.updateFramebufferSize();
            }
            Display.setFullscreen((boolean)this.fullscreen);
            Display.setVSyncEnabled((boolean)this.gameSettings.enableVsync);
            this.updateDisplay();
        }
        catch (Exception exception) {
            logger.error("Couldn't toggle fullscreen", (Throwable)exception);
        }
    }

    private void resize(int width, int height) {
        this.displayWidth = Math.max((int)1, (int)width);
        this.displayHeight = Math.max((int)1, (int)height);
        if (this.currentScreen != null) {
            ScaledResolution scaledresolution = new ScaledResolution(this);
            this.currentScreen.onResize(this, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
        }
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.updateFramebufferSize();
    }

    private void updateFramebufferSize() {
        this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);
        if (this.entityRenderer != null) {
            this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
        }
    }

    public MusicTicker getMusicTicker() {
        return this.mcMusicTicker;
    }

    public void runTick() throws IOException {
        if (this.thePlayer != null) {
            EventTick eventTick = new EventTick();
            eventTick.setType(EventType.PRE);
            Aqua.INSTANCE.onEvent((Event)eventTick);
        }
        Aqua.INSTANCE.onEvent((Event)new EventClick());
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }
        this.mcProfiler.startSection("gui");
        if (!this.isGamePaused) {
            this.ingameGUI.updateTick();
        }
        this.mcProfiler.endSection();
        this.entityRenderer.getMouseOver(1.0f);
        if (this.thePlayer != null && this.theWorld != null) {
            Aqua.INSTANCE.onEvent((Event)new EventMouseOver());
        }
        this.mcProfiler.startSection("gameMode");
        if (!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
        }
        this.mcProfiler.endStartSection("textures");
        if (!this.isGamePaused) {
            this.renderEngine.tick();
        }
        if (this.currentScreen == null && this.thePlayer != null) {
            if (this.thePlayer.getHealth() <= 0.0f) {
                this.displayGuiScreen(null);
            } else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null) {
                this.displayGuiScreen((GuiScreen)new GuiSleepMP());
            }
        } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
            this.displayGuiScreen(null);
        }
        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
        }
        if (this.currentScreen != null) {
            try {
                this.currentScreen.handleInput();
            }
            catch (Throwable throwable1) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable1, (String)"Updating screen events");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.addCrashSectionCallable("Screen name", (Callable)new /* Unavailable Anonymous Inner Class!! */);
                throw new ReportedException(crashreport);
            }
            if (this.currentScreen != null) {
                try {
                    this.currentScreen.updateScreen();
                }
                catch (Throwable throwable) {
                    CrashReport crashreport1 = CrashReport.makeCrashReport((Throwable)throwable, (String)"Ticking screen");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Affected screen");
                    crashreportcategory1.addCrashSectionCallable("Screen name", (Callable)new /* Unavailable Anonymous Inner Class!! */);
                    throw new ReportedException(crashreport1);
                }
            }
        }
        if (this.currentScreen == null || this.currentScreen.allowUserInput) {
            boolean flag;
            this.mcProfiler.endStartSection("mouse");
            while (Mouse.next()) {
                long i1;
                int i = Mouse.getEventButton();
                KeyBinding.setKeyBindState((int)(i - 100), (boolean)Mouse.getEventButtonState());
                if (Mouse.getEventButtonState()) {
                    if (this.thePlayer.isSpectator() && i == 2) {
                        this.ingameGUI.getSpectatorGui().func_175261_b();
                    } else {
                        KeyBinding.onTick((int)(i - 100));
                    }
                }
                if ((i1 = Minecraft.getSystemTime() - this.systemTime) > 200L) continue;
                int j = Mouse.getEventDWheel();
                if (j != 0) {
                    if (this.thePlayer.isSpectator()) {
                        int n = j = j < 0 ? -1 : 1;
                        if (this.ingameGUI.getSpectatorGui().func_175262_a()) {
                            this.ingameGUI.getSpectatorGui().func_175259_b(-j);
                        } else {
                            float f = MathHelper.clamp_float((float)(this.thePlayer.capabilities.getFlySpeed() + (float)j * 0.005f), (float)0.0f, (float)0.2f);
                            this.thePlayer.capabilities.setFlySpeed(f);
                        }
                    } else {
                        this.thePlayer.inventory.changeCurrentItem(j);
                    }
                }
                if (this.currentScreen == null) {
                    if (this.inGameHasFocus || !Mouse.getEventButtonState()) continue;
                    this.setIngameFocus();
                    continue;
                }
                if (this.currentScreen == null) continue;
                this.currentScreen.handleMouseInput();
            }
            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }
            this.mcProfiler.endStartSection("keyboard");
            while (Keyboard.next()) {
                int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
                KeyBinding.setKeyBindState((int)k, (boolean)Keyboard.getEventKeyState());
                if (Keyboard.getEventKeyState()) {
                    KeyBinding.onTick((int)k);
                }
                if (this.debugCrashKeyPressTime > 0L) {
                    if (Minecraft.getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
                        throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                    }
                    if (!Keyboard.isKeyDown((int)46) || !Keyboard.isKeyDown((int)61)) {
                        this.debugCrashKeyPressTime = -1L;
                    }
                } else if (Keyboard.isKeyDown((int)46) && Keyboard.isKeyDown((int)61)) {
                    this.debugCrashKeyPressTime = Minecraft.getSystemTime();
                }
                if (!Keyboard.getEventKeyState()) continue;
                if (k == 62 && this.entityRenderer != null) {
                    this.entityRenderer.switchUseShader();
                }
                if (this.currentScreen != null) {
                    this.currentScreen.handleKeyboardInput();
                } else {
                    for (Module m : Aqua.moduleManager.getModules()) {
                        if (k != m.getKeyBind()) continue;
                        m.toggle();
                    }
                    if (k == 1) {
                        this.displayInGameMenu();
                    }
                    if (k == 32 && Keyboard.isKeyDown((int)61) && this.ingameGUI != null) {
                        this.ingameGUI.getChatGUI().clearChatMessages();
                    }
                    if (k == 31 && Keyboard.isKeyDown((int)61)) {
                        this.refreshResources();
                    }
                    if (k != 17 || Keyboard.isKeyDown((int)61)) {
                        // empty if block
                    }
                    if (k != 18 || Keyboard.isKeyDown((int)61)) {
                        // empty if block
                    }
                    if (k != 47 || Keyboard.isKeyDown((int)61)) {
                        // empty if block
                    }
                    if (k != 38 || Keyboard.isKeyDown((int)61)) {
                        // empty if block
                    }
                    if (k != 22 || Keyboard.isKeyDown((int)61)) {
                        // empty if block
                    }
                    if (k == 20 && Keyboard.isKeyDown((int)61)) {
                        this.refreshResources();
                    }
                    if (k == 33 && Keyboard.isKeyDown((int)61)) {
                        this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
                    }
                    if (k == 30 && Keyboard.isKeyDown((int)61)) {
                        this.renderGlobal.loadRenderers();
                    }
                    if (k == 35 && Keyboard.isKeyDown((int)61)) {
                        this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                        this.gameSettings.saveOptions();
                    }
                    if (k == 48 && Keyboard.isKeyDown((int)61)) {
                        this.renderManager.setDebugBoundingBox(!this.renderManager.isDebugBoundingBox());
                    }
                    if (k == 25 && Keyboard.isKeyDown((int)61)) {
                        this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                        this.gameSettings.saveOptions();
                    }
                    if (k == 59) {
                        boolean bl = this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                    }
                    if (k == 61) {
                        this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                        this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                        this.gameSettings.showLagometer = GuiScreen.isAltKeyDown();
                    }
                    if (this.gameSettings.keyBindTogglePerspective.isPressed()) {
                        ++this.gameSettings.thirdPersonView;
                        if (this.gameSettings.thirdPersonView > 2) {
                            this.gameSettings.thirdPersonView = 0;
                        }
                        if (this.gameSettings.thirdPersonView == 0) {
                            this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
                        } else if (this.gameSettings.thirdPersonView == 1) {
                            this.entityRenderer.loadEntityShader((Entity)null);
                        }
                        this.renderGlobal.setDisplayListEntitiesDirty();
                    }
                    if (this.gameSettings.keyBindSmoothCamera.isPressed()) {
                        boolean bl = this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                    }
                }
                if (!this.gameSettings.showDebugInfo || !this.gameSettings.showDebugProfilerChart) continue;
                if (k == 11) {
                    this.updateDebugProfilerName(0);
                }
                for (int j1 = 0; j1 < 9; ++j1) {
                    if (k != 2 + j1) continue;
                    this.updateDebugProfilerName(j1 + 1);
                }
            }
            for (int l = 0; l < 9; ++l) {
                if (!this.gameSettings.keyBindsHotbar[l].isPressed()) continue;
                if (this.thePlayer.isSpectator()) {
                    this.ingameGUI.getSpectatorGui().func_175260_a(l);
                    continue;
                }
                this.thePlayer.inventory.currentItem = l;
            }
            boolean bl = flag = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;
            while (this.gameSettings.keyBindInventory.isPressed()) {
                if (this.playerController.isRidingHorse()) {
                    this.thePlayer.sendHorseInventory();
                    continue;
                }
                this.getNetHandler().addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                this.displayGuiScreen((GuiScreen)new GuiInventory((EntityPlayer)this.thePlayer));
            }
            while (this.gameSettings.keyBindDrop.isPressed()) {
                if (this.thePlayer.isSpectator()) continue;
                this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
            }
            while (this.gameSettings.keyBindChat.isPressed() && flag) {
                this.displayGuiScreen((GuiScreen)new GuiChat());
            }
            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && flag) {
                this.displayGuiScreen((GuiScreen)new GuiChat("/"));
            }
            if (this.thePlayer.isUsingItem()) {
                if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
                    this.playerController.onStoppedUsingItem((EntityPlayer)this.thePlayer);
                }
                while (this.gameSettings.keyBindAttack.isPressed()) {
                }
                while (this.gameSettings.keyBindUseItem.isPressed()) {
                }
                while (this.gameSettings.keyBindPickBlock.isPressed()) {
                }
            } else {
                while (this.gameSettings.keyBindAttack.isPressed()) {
                    this.clickMouse();
                }
                while (this.gameSettings.keyBindUseItem.isPressed()) {
                    this.rightClickMouse();
                }
                while (this.gameSettings.keyBindPickBlock.isPressed()) {
                    this.middleClickMouse();
                }
            }
            if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem()) {
                this.rightClickMouse();
            }
            this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
        }
        if (this.theWorld != null) {
            if (this.thePlayer != null) {
                ++this.joinPlayerCounter;
                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings((Entity)this.thePlayer);
                }
            }
            this.mcProfiler.endStartSection("gameRenderer");
            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }
            this.mcProfiler.endStartSection("levelRenderer");
            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }
            this.mcProfiler.endStartSection("level");
            if (!this.isGamePaused) {
                if (this.theWorld.getLastLightningBolt() > 0) {
                    this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
                }
                this.theWorld.updateEntities();
            }
        } else if (this.entityRenderer.isShaderActive()) {
            this.entityRenderer.stopUseShader();
        }
        if (!this.isGamePaused) {
            this.mcMusicTicker.update();
            this.mcSoundHandler.update();
        }
        if (this.theWorld != null) {
            if (!this.isGamePaused) {
                this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                try {
                    this.theWorld.tick();
                }
                catch (Throwable throwable2) {
                    CrashReport crashreport2 = CrashReport.makeCrashReport((Throwable)throwable2, (String)"Exception in world tick");
                    if (this.theWorld == null) {
                        CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected level");
                        crashreportcategory2.addCrashSection("Problem", (Object)"Level is null!");
                    } else {
                        this.theWorld.addWorldInfoToCrashReport(crashreport2);
                    }
                    throw new ReportedException(crashreport2);
                }
            }
            this.mcProfiler.endStartSection("animateTick");
            if (!this.isGamePaused && this.theWorld != null) {
                this.theWorld.doVoidFogParticles(MathHelper.floor_double((double)this.thePlayer.posX), MathHelper.floor_double((double)this.thePlayer.posY), MathHelper.floor_double((double)this.thePlayer.posZ));
            }
            this.mcProfiler.endStartSection("particles");
            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        } else if (this.myNetworkManager != null) {
            this.mcProfiler.endStartSection("pendingConnection");
            this.myNetworkManager.processReceivedPackets();
        }
        this.mcProfiler.endSection();
        this.systemTime = Minecraft.getSystemTime();
    }

    public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn) {
        this.loadWorld(null);
        System.gc();
        ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null && worldSettingsIn != null) {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }
        if (worldSettingsIn == null) {
            worldSettingsIn = new WorldSettings(worldinfo);
        }
        try {
            this.theIntegratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn);
            this.theIntegratedServer.startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", (Object)folderName);
            crashreportcategory.addCrashSection("Level Name", (Object)worldName);
            throw new ReportedException(crashreport);
        }
        this.loadingScreen.displaySavingString(I18n.format((String)"menu.loadingLevel", (Object[])new Object[0]));
        while (!this.theIntegratedServer.serverIsInRunLoop()) {
            String s = this.theIntegratedServer.getUserMessage();
            if (s != null) {
                this.loadingScreen.displayLoadingString(I18n.format((String)s, (Object[])new Object[0]));
            } else {
                this.loadingScreen.displayLoadingString("");
            }
            try {
                Thread.sleep((long)200L);
            }
            catch (InterruptedException crashreport) {}
        }
        this.displayGuiScreen(null);
        SocketAddress socketaddress = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.provideLocalClient((SocketAddress)socketaddress);
        networkmanager.setNetHandler((INetHandler)new NetHandlerLoginClient(networkmanager, this, (GuiScreen)null));
        networkmanager.sendPacket((Packet)new C00Handshake(47, socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.sendPacket((Packet)new C00PacketLoginStart(this.getSession().getProfile()));
        this.myNetworkManager = networkmanager;
    }

    public void loadWorld(WorldClient worldClientIn) {
        this.loadWorld(worldClientIn, "");
    }

    public void loadWorld(WorldClient worldClientIn, String loadingMessage) {
        if (worldClientIn == null) {
            NetHandlerPlayClient nethandlerplayclient = this.getNetHandler();
            if (nethandlerplayclient != null) {
                nethandlerplayclient.cleanup();
            }
            if (this.theIntegratedServer != null && this.theIntegratedServer.isAnvilFileSet()) {
                this.theIntegratedServer.initiateShutdown();
                this.theIntegratedServer.setStaticInstance();
            }
            this.theIntegratedServer = null;
            this.guiAchievement.clearAchievements();
            this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
        }
        this.renderViewEntity = null;
        this.myNetworkManager = null;
        if (this.loadingScreen != null) {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.displayLoadingString("");
        }
        if (worldClientIn == null && this.theWorld != null) {
            this.mcResourcePackRepository.clearResourcePack();
            this.ingameGUI.resetPlayersOverlayFooterHeader();
            this.setServerData(null);
            this.integratedServerIsRunning = false;
        }
        this.mcSoundHandler.stopSounds();
        this.theWorld = worldClientIn;
        if (worldClientIn != null) {
            if (this.renderGlobal != null) {
                this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
            }
            if (this.effectRenderer != null) {
                this.effectRenderer.clearEffects((World)worldClientIn);
            }
            if (this.thePlayer == null) {
                this.thePlayer = this.playerController.func_178892_a((World)worldClientIn, new StatFileWriter());
                this.playerController.flipPlayer((EntityPlayer)this.thePlayer);
            }
            this.thePlayer.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld((Entity)this.thePlayer);
            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities((EntityPlayer)this.thePlayer);
            this.renderViewEntity = this.thePlayer;
        } else {
            this.saveLoader.flushCache();
            this.thePlayer = null;
        }
        System.gc();
        this.systemTime = 0L;
    }

    public void setDimensionAndSpawnPlayer(int dimension) {
        this.theWorld.setInitialSpawnLocation();
        this.theWorld.removeAllEntities();
        int i = 0;
        String s = null;
        if (this.thePlayer != null) {
            i = this.thePlayer.getEntityId();
            this.theWorld.removeEntity((Entity)this.thePlayer);
            s = this.thePlayer.getClientBrand();
        }
        this.renderViewEntity = null;
        EntityPlayerSP entityplayersp = this.thePlayer;
        this.thePlayer = this.playerController.func_178892_a((World)this.theWorld, this.thePlayer == null ? new StatFileWriter() : this.thePlayer.getStatFileWriter());
        this.thePlayer.getDataWatcher().updateWatchedObjectsFromList(entityplayersp.getDataWatcher().getAllWatched());
        this.thePlayer.dimension = dimension;
        this.renderViewEntity = this.thePlayer;
        this.thePlayer.preparePlayerToSpawn();
        this.thePlayer.setClientBrand(s);
        this.theWorld.spawnEntityInWorld((Entity)this.thePlayer);
        this.playerController.flipPlayer((EntityPlayer)this.thePlayer);
        this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.thePlayer.setEntityId(i);
        this.playerController.setPlayerCapabilities((EntityPlayer)this.thePlayer);
        this.thePlayer.setReducedDebug(entityplayersp.hasReducedDebug());
        if (this.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }
    }

    public final boolean isDemo() {
        return this.isDemo;
    }

    public NetHandlerPlayClient getNetHandler() {
        return this.thePlayer != null ? this.thePlayer.sendQueue : null;
    }

    public static boolean isGuiEnabled() {
        return theMinecraft == null || !Minecraft.theMinecraft.gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled() {
        return theMinecraft != null && Minecraft.theMinecraft.gameSettings.fancyGraphics;
    }

    public static boolean isAmbientOcclusionEnabled() {
        return theMinecraft != null && Minecraft.theMinecraft.gameSettings.ambientOcclusion != 0;
    }

    private void middleClickMouse() {
        if (this.objectMouseOver != null) {
            Item item;
            boolean flag = this.thePlayer.capabilities.isCreativeMode;
            int i = 0;
            boolean flag1 = false;
            TileEntity tileentity = null;
            if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockpos = this.objectMouseOver.getBlockPos();
                Block block = this.theWorld.getBlockState(blockpos).getBlock();
                if (block.getMaterial() == Material.air) {
                    return;
                }
                item = block.getItem((World)this.theWorld, blockpos);
                if (item == null) {
                    return;
                }
                if (flag && GuiScreen.isCtrlKeyDown()) {
                    tileentity = this.theWorld.getTileEntity(blockpos);
                }
                Block block1 = item instanceof ItemBlock && !block.isFlowerPot() ? Block.getBlockFromItem((Item)item) : block;
                i = block1.getDamageValue((World)this.theWorld, blockpos);
                flag1 = item.getHasSubtypes();
            } else {
                if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !flag) {
                    return;
                }
                if (this.objectMouseOver.entityHit instanceof EntityPainting) {
                    item = Items.painting;
                } else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
                    item = Items.lead;
                } else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
                    EntityItemFrame entityitemframe = (EntityItemFrame)this.objectMouseOver.entityHit;
                    ItemStack itemstack = entityitemframe.getDisplayedItem();
                    if (itemstack == null) {
                        item = Items.item_frame;
                    } else {
                        item = itemstack.getItem();
                        i = itemstack.getMetadata();
                        flag1 = true;
                    }
                } else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
                    EntityMinecart entityminecart = (EntityMinecart)this.objectMouseOver.entityHit;
                    switch (17.$SwitchMap$net$minecraft$entity$item$EntityMinecart$EnumMinecartType[entityminecart.getMinecartType().ordinal()]) {
                        case 1: {
                            item = Items.furnace_minecart;
                            break;
                        }
                        case 2: {
                            item = Items.chest_minecart;
                            break;
                        }
                        case 3: {
                            item = Items.tnt_minecart;
                            break;
                        }
                        case 4: {
                            item = Items.hopper_minecart;
                            break;
                        }
                        case 5: {
                            item = Items.command_block_minecart;
                            break;
                        }
                        default: {
                            item = Items.minecart;
                            break;
                        }
                    }
                } else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
                    item = Items.boat;
                } else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
                    item = Items.armor_stand;
                } else {
                    item = Items.spawn_egg;
                    i = EntityList.getEntityID((Entity)this.objectMouseOver.entityHit);
                    flag1 = true;
                    if (!EntityList.entityEggs.containsKey((Object)i)) {
                        return;
                    }
                }
            }
            InventoryPlayer inventoryplayer = this.thePlayer.inventory;
            if (tileentity == null) {
                inventoryplayer.setCurrentItem(item, i, flag1, flag);
            } else {
                ItemStack itemstack1 = this.pickBlockWithNBT(item, i, tileentity);
                inventoryplayer.setInventorySlotContents(inventoryplayer.currentItem, itemstack1);
            }
            if (flag) {
                int j = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + inventoryplayer.currentItem;
                this.playerController.sendSlotPacket(inventoryplayer.getStackInSlot(inventoryplayer.currentItem), j);
            }
        }
    }

    private ItemStack pickBlockWithNBT(Item itemIn, int meta, TileEntity tileEntityIn) {
        ItemStack itemstack = new ItemStack(itemIn, 1, meta);
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        tileEntityIn.writeToNBT(nbttagcompound);
        if (itemIn == Items.skull && nbttagcompound.hasKey("Owner")) {
            NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
            NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            nbttagcompound3.setTag("SkullOwner", (NBTBase)nbttagcompound2);
            itemstack.setTagCompound(nbttagcompound3);
            return itemstack;
        }
        itemstack.setTagInfo("BlockEntityTag", (NBTBase)nbttagcompound);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag((NBTBase)new NBTTagString("(+NBT)"));
        nbttagcompound1.setTag("Lore", (NBTBase)nbttaglist);
        itemstack.setTagInfo("display", (NBTBase)nbttagcompound1);
        return itemstack;
    }

    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash) {
        theCrash.getCategory().addCrashSectionCallable("Launched Version", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("LWJGL", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("OpenGL", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("GL Caps", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("Using VBOs", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("Is Modded", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("Type", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("Resource Packs", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("Current Language", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("Profiler Position", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        theCrash.getCategory().addCrashSectionCallable("CPU", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        if (this.theWorld != null) {
            this.theWorld.addWorldInfoToCrashReport(theCrash);
        }
        return theCrash;
    }

    public static Minecraft getMinecraft() {
        return theMinecraft;
    }

    public ListenableFuture<Object> scheduleResourcesRefresh() {
        return this.addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
        playerSnooper.addClientStat("fps", (Object)debugFPS);
        playerSnooper.addClientStat("vsync_enabled", (Object)this.gameSettings.enableVsync);
        playerSnooper.addClientStat("display_frequency", (Object)Display.getDisplayMode().getFrequency());
        playerSnooper.addClientStat("display_type", (Object)(this.fullscreen ? "fullscreen" : "windowed"));
        playerSnooper.addClientStat("run_time", (Object)((MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
        playerSnooper.addClientStat("current_action", (Object)this.getCurrentAction());
        String s = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
        playerSnooper.addClientStat("endianness", (Object)s);
        playerSnooper.addClientStat("resource_packs", (Object)this.mcResourcePackRepository.getRepositoryEntries().size());
        int i = 0;
        for (ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries()) {
            playerSnooper.addClientStat("resource_pack[" + i++ + "]", (Object)resourcepackrepository$entry.getResourcePackName());
        }
        if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null) {
            playerSnooper.addClientStat("snooper_partner", (Object)this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }

    private String getCurrentAction() {
        return this.theIntegratedServer != null ? (this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer") : (this.currentServerData != null ? (this.currentServerData.isOnLAN() ? "playing_lan" : "multiplayer") : "out_of_game");
    }

    public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper) {
        playerSnooper.addStatToSnooper("opengl_version", (Object)GL11.glGetString((int)7938));
        playerSnooper.addStatToSnooper("opengl_vendor", (Object)GL11.glGetString((int)7936));
        playerSnooper.addStatToSnooper("client_brand", (Object)ClientBrandRetriever.getClientModName());
        playerSnooper.addStatToSnooper("launched_version", (Object)this.launchedVersion);
        ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", (Object)contextcapabilities.GL_ARB_arrays_of_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", (Object)contextcapabilities.GL_ARB_base_instance);
        playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", (Object)contextcapabilities.GL_ARB_blend_func_extended);
        playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", (Object)contextcapabilities.GL_ARB_clear_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", (Object)contextcapabilities.GL_ARB_color_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", (Object)contextcapabilities.GL_ARB_compatibility);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", (Object)contextcapabilities.GL_ARB_compressed_texture_pixel_storage);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", (Object)contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", (Object)contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", (Object)contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", (Object)contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", (Object)contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", (Object)contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", (Object)contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", (Object)contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", (Object)contextcapabilities.GL_ARB_depth_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", (Object)contextcapabilities.GL_ARB_depth_texture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", (Object)contextcapabilities.GL_ARB_draw_buffers);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", (Object)contextcapabilities.GL_ARB_draw_buffers_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", (Object)contextcapabilities.GL_ARB_draw_elements_base_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", (Object)contextcapabilities.GL_ARB_draw_indirect);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", (Object)contextcapabilities.GL_ARB_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", (Object)contextcapabilities.GL_ARB_explicit_attrib_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", (Object)contextcapabilities.GL_ARB_explicit_uniform_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", (Object)contextcapabilities.GL_ARB_fragment_layer_viewport);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", (Object)contextcapabilities.GL_ARB_fragment_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", (Object)contextcapabilities.GL_ARB_fragment_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", (Object)contextcapabilities.GL_ARB_fragment_program_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", (Object)contextcapabilities.GL_ARB_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", (Object)contextcapabilities.GL_ARB_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", (Object)contextcapabilities.GL_ARB_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", (Object)contextcapabilities.GL_ARB_gpu_shader5);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", (Object)contextcapabilities.GL_ARB_half_float_pixel);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", (Object)contextcapabilities.GL_ARB_half_float_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", (Object)contextcapabilities.GL_ARB_instanced_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", (Object)contextcapabilities.GL_ARB_map_buffer_alignment);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", (Object)contextcapabilities.GL_ARB_map_buffer_range);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", (Object)contextcapabilities.GL_ARB_multisample);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", (Object)contextcapabilities.GL_ARB_multitexture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", (Object)contextcapabilities.GL_ARB_occlusion_query2);
        playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", (Object)contextcapabilities.GL_ARB_pixel_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", (Object)contextcapabilities.GL_ARB_seamless_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", (Object)contextcapabilities.GL_ARB_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", (Object)contextcapabilities.GL_ARB_shader_stencil_export);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", (Object)contextcapabilities.GL_ARB_shader_texture_lod);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", (Object)contextcapabilities.GL_ARB_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", (Object)contextcapabilities.GL_ARB_shadow_ambient);
        playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", (Object)contextcapabilities.GL_ARB_stencil_texturing);
        playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", (Object)contextcapabilities.GL_ARB_sync);
        playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", (Object)contextcapabilities.GL_ARB_tessellation_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", (Object)contextcapabilities.GL_ARB_texture_border_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", (Object)contextcapabilities.GL_ARB_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", (Object)contextcapabilities.GL_ARB_texture_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", (Object)contextcapabilities.GL_ARB_texture_cube_map_array);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", (Object)contextcapabilities.GL_ARB_texture_non_power_of_two);
        playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", (Object)contextcapabilities.GL_ARB_uniform_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", (Object)contextcapabilities.GL_ARB_vertex_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", (Object)contextcapabilities.GL_ARB_vertex_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", (Object)contextcapabilities.GL_ARB_vertex_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", (Object)contextcapabilities.GL_ARB_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", (Object)contextcapabilities.GL_EXT_bindable_uniform);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", (Object)contextcapabilities.GL_EXT_blend_equation_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", (Object)contextcapabilities.GL_EXT_blend_func_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", (Object)contextcapabilities.GL_EXT_blend_minmax);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", (Object)contextcapabilities.GL_EXT_blend_subtract);
        playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", (Object)contextcapabilities.GL_EXT_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", (Object)contextcapabilities.GL_EXT_framebuffer_multisample);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", (Object)contextcapabilities.GL_EXT_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", (Object)contextcapabilities.GL_EXT_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", (Object)contextcapabilities.GL_EXT_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", (Object)contextcapabilities.GL_EXT_gpu_program_parameters);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", (Object)contextcapabilities.GL_EXT_gpu_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", (Object)contextcapabilities.GL_EXT_multi_draw_arrays);
        playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", (Object)contextcapabilities.GL_EXT_packed_depth_stencil);
        playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", (Object)contextcapabilities.GL_EXT_paletted_texture);
        playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", (Object)contextcapabilities.GL_EXT_rescale_normal);
        playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", (Object)contextcapabilities.GL_EXT_separate_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", (Object)contextcapabilities.GL_EXT_shader_image_load_store);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", (Object)contextcapabilities.GL_EXT_shadow_funcs);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", (Object)contextcapabilities.GL_EXT_shared_texture_palette);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", (Object)contextcapabilities.GL_EXT_stencil_clear_tag);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", (Object)contextcapabilities.GL_EXT_stencil_two_side);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", (Object)contextcapabilities.GL_EXT_stencil_wrap);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", (Object)contextcapabilities.GL_EXT_texture_3d);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", (Object)contextcapabilities.GL_EXT_texture_array);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", (Object)contextcapabilities.GL_EXT_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", (Object)contextcapabilities.GL_EXT_texture_integer);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", (Object)contextcapabilities.GL_EXT_texture_lod_bias);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", (Object)contextcapabilities.GL_EXT_texture_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", (Object)contextcapabilities.GL_EXT_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", (Object)contextcapabilities.GL_EXT_vertex_weighting);
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", (Object)GL11.glGetInteger((int)35658));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", (Object)GL11.glGetInteger((int)35657));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", (Object)GL11.glGetInteger((int)34921));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", (Object)GL11.glGetInteger((int)35660));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", (Object)GL11.glGetInteger((int)34930));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", (Object)GL11.glGetInteger((int)35071));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_max_texture_size", (Object)Minecraft.getGLMaximumTextureSize());
    }

    public static int getGLMaximumTextureSize() {
        for (int i = 16384; i > 0; i >>= 1) {
            GL11.glTexImage2D((int)32868, (int)0, (int)6408, (int)i, (int)i, (int)0, (int)6408, (int)5121, (ByteBuffer)null);
            int j = GL11.glGetTexLevelParameteri((int)32868, (int)0, (int)4096);
            if (j == 0) continue;
            return i;
        }
        return -1;
    }

    public boolean isSnooperEnabled() {
        return this.gameSettings.snooperEnabled;
    }

    public void setServerData(ServerData serverDataIn) {
        this.currentServerData = serverDataIn;
    }

    public ServerData getCurrentServerData() {
        return this.currentServerData;
    }

    public boolean isIntegratedServerRunning() {
        return this.integratedServerIsRunning;
    }

    public boolean isSingleplayer() {
        return this.integratedServerIsRunning && this.theIntegratedServer != null;
    }

    public IntegratedServer getIntegratedServer() {
        return this.theIntegratedServer;
    }

    public static void stopIntegratedServer() {
        IntegratedServer integratedserver;
        if (theMinecraft != null && (integratedserver = theMinecraft.getIntegratedServer()) != null) {
            integratedserver.stopServer();
        }
    }

    public PlayerUsageSnooper getPlayerUsageSnooper() {
        return this.usageSnooper;
    }

    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public boolean isFullScreen() {
        return this.fullscreen;
    }

    public Session getSession() {
        return this.session;
    }

    public PropertyMap getTwitchDetails() {
        return this.twitchDetails;
    }

    public PropertyMap getProfileProperties() {
        if (this.profileProperties.isEmpty()) {
            GameProfile gameprofile = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
            this.profileProperties.putAll((Multimap)gameprofile.getProperties());
        }
        return this.profileProperties;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public TextureManager getTextureManager() {
        return this.renderEngine;
    }

    public IResourceManager getResourceManager() {
        return this.mcResourceManager;
    }

    public ResourcePackRepository getResourcePackRepository() {
        return this.mcResourcePackRepository;
    }

    public LanguageManager getLanguageManager() {
        return this.mcLanguageManager;
    }

    public TextureMap getTextureMapBlocks() {
        return this.textureMapBlocks;
    }

    public boolean isJava64bit() {
        return this.jvm64bit;
    }

    public boolean isGamePaused() {
        return this.isGamePaused;
    }

    public SoundHandler getSoundHandler() {
        return this.mcSoundHandler;
    }

    public MusicTicker.MusicType getAmbientMusicType() {
        return this.thePlayer != null ? (this.thePlayer.worldObj.provider instanceof WorldProviderHell ? MusicTicker.MusicType.NETHER : (this.thePlayer.worldObj.provider instanceof WorldProviderEnd ? (BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END) : (this.thePlayer.capabilities.isCreativeMode && this.thePlayer.capabilities.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME))) : MusicTicker.MusicType.MENU;
    }

    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }

    public SkinManager getSkinManager() {
        return this.skinManager;
    }

    public Entity getRenderViewEntity() {
        return this.renderViewEntity;
    }

    public void setRenderViewEntity(Entity viewingEntity) {
        this.renderViewEntity = viewingEntity;
        this.entityRenderer.loadEntityShader(viewingEntity);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule) {
        Validate.notNull(callableToSchedule);
        if (!this.isCallingFromMinecraftThread()) {
            ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
            Queue<FutureTask<?>> queue = this.scheduledTasks;
            synchronized (queue) {
                this.scheduledTasks.add((Object)listenablefuturetask);
                return listenablefuturetask;
            }
        }
        try {
            return Futures.immediateFuture((Object)callableToSchedule.call());
        }
        catch (Exception exception) {
            return Futures.immediateFailedCheckedFuture((Exception)exception);
        }
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
        Validate.notNull((Object)runnableToSchedule);
        return this.addScheduledTask(Executors.callable((Runnable)runnableToSchedule));
    }

    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.mcThread;
    }

    public BlockRendererDispatcher getBlockRendererDispatcher() {
        return this.blockRenderDispatcher;
    }

    public RenderManager getRenderManager() {
        return this.renderManager;
    }

    public RenderItem getRenderItem() {
        return this.renderItem;
    }

    public ItemRenderer getItemRenderer() {
        return this.itemRenderer;
    }

    public static int getDebugFPS() {
        return debugFPS;
    }

    public FrameTimer getFrameTimer() {
        return this.frameTimer;
    }

    public static Map<String, String> getSessionInfo() {
        HashMap map = Maps.newHashMap();
        map.put((Object)"X-Minecraft-Username", (Object)Minecraft.getMinecraft().getSession().getUsername());
        map.put((Object)"X-Minecraft-UUID", (Object)Minecraft.getMinecraft().getSession().getPlayerID());
        map.put((Object)"X-Minecraft-Version", (Object)"1.8.9");
        return map;
    }

    public boolean isConnectedToRealms() {
        return this.connectedToRealms;
    }

    public void setConnectedToRealms(boolean isConnected) {
        this.connectedToRealms = isConnected;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    static /* synthetic */ String access$000(Minecraft x0) {
        return x0.launchedVersion;
    }

    static /* synthetic */ LanguageManager access$100(Minecraft x0) {
        return x0.mcLanguageManager;
    }
}
