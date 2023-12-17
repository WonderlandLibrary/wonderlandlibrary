// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client;

import org.apache.logging.log4j.LogManager;
import java.util.stream.Stream;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.client.util.ISearchTree;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.Futures;
import org.apache.commons.lang3.Validate;
import java.util.concurrent.Callable;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.client.gui.GuiWinGame;
import com.google.common.collect.Multimap;
import java.nio.IntBuffer;
import com.mojang.authlib.GameProfile;
import org.lwjgl.opengl.ContextCapabilities;
import org.apache.commons.io.Charsets;
import com.google.common.hash.Hashing;
import org.lwjgl.opengl.GLContext;
import java.nio.ByteOrder;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.init.Items;
import net.minecraft.entity.item.EntityPainting;
import com.mojang.authlib.AuthenticationService;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import java.net.SocketAddress;
import com.mojang.authlib.GameProfileRepository;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldSettings;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import ru.tuskevich.event.events.impl.MouseEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.world.EnumDifficulty;
import ru.tuskevich.event.events.impl.EventTick;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.client.gui.GuiSleepMP;
import ru.tuskevich.util.shader.BloomUtil;
import ru.tuskevich.util.shader.ShaderUtility;
import ru.tuskevich.util.render.BlurUtility;
import net.minecraft.util.EnumActionResult;
import ru.tuskevich.event.events.impl.EventInteract;
import net.minecraft.item.ItemAppleGold;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventRightClick;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumHand;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiIngameMenu;
import java.text.DecimalFormat;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Set;
import com.google.common.collect.Sets;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.util.Util;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.PixelFormat;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import java.util.Iterator;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.gui.recipebook.RecipeList;
import java.util.function.Consumer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.SearchTree;
import java.util.Collections;
import net.minecraft.item.Item;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Display;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.Sys;
import net.minecraft.util.ReportedException;
import net.minecraft.util.MinecraftError;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Bootstrap;
import java.util.Locale;
import javax.imageio.ImageIO;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.util.UUID;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.main.GameConfiguration;
import ru.tuskevich.Minced;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.ModelManager;
import java.util.concurrent.FutureTask;
import java.util.Queue;
import net.minecraft.client.resources.SkinManager;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.profiler.Profiler;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.FrameTimer;
import net.minecraft.world.storage.ISaveFormat;
import java.net.Proxy;
import net.minecraft.util.MouseHelper;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.Session;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.client.particle.ParticleManager;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.Snooper;
import net.minecraft.util.Timer;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.multiplayer.ServerData;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import org.lwjgl.opengl.DisplayMode;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.util.IThreadListener;

public class Minecraft implements IThreadListener, ISnooperInfo
{
    public static final Logger LOGGER;
    private static final ResourceLocation LOCATION_MOJANG_PNG;
    public static final boolean IS_RUNNING_ON_MAC;
    public static byte[] memoryReserve;
    private static final List<DisplayMode> MAC_DISPLAY_MODES;
    private final File fileResourcepacks;
    private final PropertyMap twitchDetails;
    private final PropertyMap profileProperties;
    private ServerData currentServerData;
    public TextureManager renderEngine;
    private static Minecraft instance;
    private final DataFixer dataFixer;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private final boolean enableGLErrorChecking = true;
    private boolean hasCrashed;
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;
    private boolean connectedToRealms;
    public final Timer timer;
    private final Snooper usageSnooper;
    public WorldClient world;
    public RenderGlobal renderGlobal;
    private RenderManager renderManager;
    private RenderItem renderItem;
    private ItemRenderer itemRenderer;
    public static EntityPlayerSP player;
    @Nullable
    private Entity renderViewEntity;
    public Entity pointedEntity;
    public ParticleManager effectRenderer;
    public static double frameTime;
    private SearchTreeManager searchTreeManager;
    public Session session;
    private boolean isGamePaused;
    private float renderPartialTicksPaused;
    public FontRenderer fontRenderer;
    public FontRenderer standardGalacticFontRenderer;
    @Nullable
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;
    public DebugRenderer debugRenderer;
    private int leftClickCounter;
    private final int tempDisplayWidth;
    private final int tempDisplayHeight;
    @Nullable
    private IntegratedServer integratedServer;
    public GuiIngame ingameGUI;
    public boolean skipRenderWorld;
    public RayTraceResult objectMouseOver;
    public GameSettings gameSettings;
    public CreativeSettings creativeSettings;
    public MouseHelper mouseHelper;
    public final File gameDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final String versionType;
    private final Proxy proxy;
    private ISaveFormat saveLoader;
    private static int debugFPS;
    public int rightClickDelayTimer;
    private String serverName;
    private int serverPort;
    public boolean inGameHasFocus;
    long systemTime;
    private int joinPlayerCounter;
    public final FrameTimer frameTimer;
    long startNanoTime;
    private final boolean jvm64bit;
    private final boolean isDemo;
    @Nullable
    private NetworkManager networkManager;
    private boolean integratedServerIsRunning;
    public final Profiler profiler;
    private long debugCrashKeyPressTime;
    private IReloadableResourceManager resourceManager;
    private final MetadataSerializer metadataSerializer;
    private final List<IResourcePack> defaultResourcePacks;
    private final DefaultResourcePack defaultResourcePack;
    private ResourcePackRepository resourcePackRepository;
    private LanguageManager languageManager;
    private BlockColors blockColors;
    private ItemColors itemColors;
    private Framebuffer framebuffer;
    private TextureMap textureMapBlocks;
    private SoundHandler soundHandler;
    private MusicTicker musicTicker;
    private ResourceLocation mojangLogo;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue<FutureTask<?>> scheduledTasks;
    private final Thread thread;
    private ModelManager modelManager;
    private BlockRendererDispatcher blockRenderDispatcher;
    private final GuiToast toastGui;
    volatile boolean running;
    public String debug;
    public boolean renderChunksMany;
    private long debugUpdateTime;
    private int fpsCounter;
    private boolean actionKeyF3;
    private final Tutorial tutorial;
    long prevFrameTime;
    private String debugProfilerName;
    Minced minced;
    public static boolean joinedFirst;
    
    public Minecraft(final GameConfiguration gameConfig) {
        this.timer = new Timer(20.0f);
        this.usageSnooper = new Snooper("client", this, MinecraftServer.getCurrentTimeMillis());
        this.searchTreeManager = new SearchTreeManager();
        this.systemTime = getSystemTime();
        this.frameTimer = new FrameTimer();
        this.startNanoTime = System.nanoTime();
        this.profiler = new Profiler();
        this.debugCrashKeyPressTime = -1L;
        this.metadataSerializer = new MetadataSerializer();
        this.defaultResourcePacks = (List<IResourcePack>)Lists.newArrayList();
        this.scheduledTasks = (Queue<FutureTask<?>>)Queues.newArrayDeque();
        this.thread = Thread.currentThread();
        this.running = true;
        this.debug = "";
        this.renderChunksMany = true;
        this.debugUpdateTime = getSystemTime();
        this.prevFrameTime = -1L;
        this.debugProfilerName = "root";
        Minecraft.instance = this;
        this.gameDir = gameConfig.folderInfo.gameDir;
        this.fileAssets = gameConfig.folderInfo.assetsDir;
        this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
        this.launchedVersion = gameConfig.gameInfo.version;
        this.versionType = gameConfig.gameInfo.versionType;
        this.twitchDetails = gameConfig.userInfo.userProperties;
        this.profileProperties = gameConfig.userInfo.profileProperties;
        this.defaultResourcePack = new DefaultResourcePack(gameConfig.folderInfo.getAssetsIndex());
        this.proxy = ((gameConfig.userInfo.proxy == null) ? Proxy.NO_PROXY : gameConfig.userInfo.proxy);
        this.sessionService = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
        this.session = gameConfig.userInfo.session;
        Minecraft.LOGGER.info("Setting user: {}", (Object)this.session.getUsername());
        Minecraft.LOGGER.debug("(Session ID is {})", (Object)this.session.getSessionID());
        this.isDemo = gameConfig.gameInfo.isDemo;
        this.displayWidth = ((gameConfig.displayInfo.width > 0) ? gameConfig.displayInfo.width : 1);
        this.displayHeight = ((gameConfig.displayInfo.height > 0) ? gameConfig.displayInfo.height : 1);
        this.tempDisplayWidth = gameConfig.displayInfo.width;
        this.tempDisplayHeight = gameConfig.displayInfo.height;
        this.fullscreen = gameConfig.displayInfo.fullscreen;
        this.jvm64bit = isJvm64bit();
        this.integratedServer = null;
        if (gameConfig.serverInfo.serverName != null) {
            this.serverName = gameConfig.serverInfo.serverName;
            this.serverPort = gameConfig.serverInfo.serverPort;
        }
        ImageIO.setUseCache(false);
        Locale.setDefault(Locale.ROOT);
        Bootstrap.register();
        TextComponentKeybind.displaySupplierFunction = KeyBinding::getDisplayString;
        this.dataFixer = DataFixesManager.createFixer();
        this.toastGui = new GuiToast(this);
        this.tutorial = new Tutorial(this);
    }
    
    public void run() {
        this.running = true;
        try {
            this.init();
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
            crashreport.makeCategory("Initialization");
            this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(crashreport));
            return;
        }
        try {
            while (this.running) {
                if (this.hasCrashed) {
                    if (this.crashReporter != null) {
                        this.displayCrashReport(this.crashReporter);
                        continue;
                    }
                }
                try {
                    this.runGameLoop();
                }
                catch (OutOfMemoryError var10) {
                    this.freeMemory();
                    this.displayGuiScreen(new GuiMemoryErrorScreen());
                    System.gc();
                }
            }
        }
        catch (MinecraftError var11) {}
        catch (ReportedException reportedexception) {
            this.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
            this.freeMemory();
            Minecraft.LOGGER.fatal("Reported exception thrown!", (Throwable)reportedexception);
            this.displayCrashReport(reportedexception.getCrashReport());
        }
        catch (Throwable throwable2) {
            final CrashReport crashreport2 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable2));
            this.freeMemory();
            Minecraft.LOGGER.fatal("Unreported exception thrown!", throwable2);
            this.displayCrashReport(crashreport2);
        }
        finally {
            this.shutdownMinecraftApplet();
        }
    }
    
    private void init() throws Exception {
        this.gameSettings = new GameSettings(this, this.gameDir);
        this.creativeSettings = new CreativeSettings(this, this.gameDir);
        this.defaultResourcePacks.add(this.defaultResourcePack);
        this.startTimerHackThread();
        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }
        Minecraft.LOGGER.info("LWJGL Version: {}", (Object)Sys.getVersion());
        this.setWindowIcon();
        this.setInitialDisplayMode();
        this.createDisplay();
        OpenGlHelper.initializeTextures();
        (this.framebuffer = new Framebuffer(this.displayWidth, this.displayHeight, true)).setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.registerMetadataSerializers();
        this.resourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.gameDir, "server-resource-packs"), this.defaultResourcePack, this.metadataSerializer, this.gameSettings);
        this.resourceManager = new SimpleReloadableResourceManager(this.metadataSerializer);
        this.languageManager = new LanguageManager(this.metadataSerializer, this.gameSettings.language);
        this.resourceManager.registerReloadListener(this.languageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(this.resourceManager);
        this.resourceManager.registerReloadListener(this.renderEngine);
        this.drawSplashScreen(this.renderEngine);
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.gameDir, "saves"), this.dataFixer);
        this.soundHandler = new SoundHandler(this.resourceManager, this.gameSettings);
        this.resourceManager.registerReloadListener(this.soundHandler);
        this.musicTicker = new MusicTicker(this);
        this.fontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
        if (this.gameSettings.language != null) {
            this.fontRenderer.setUnicodeFlag(this.isUnicode());
            this.fontRenderer.setBidiFlag(this.languageManager.isCurrentLanguageBidirectional());
        }
        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.resourceManager.registerReloadListener(this.fontRenderer);
        this.resourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.resourceManager.registerReloadListener(new GrassColorReloadListener());
        this.resourceManager.registerReloadListener(new FoliageColorReloadListener());
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        this.checkGLError("Startup");
        (this.textureMapBlocks = new TextureMap("textures")).setMipmapLevels(this.gameSettings.mipmapLevels);
        this.renderEngine.loadTickableTexture(TextureMap.LOCATION_BLOCKS_TEXTURE, this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
        this.modelManager = new ModelManager(this.textureMapBlocks);
        this.resourceManager.registerReloadListener(this.modelManager);
        this.blockColors = BlockColors.init();
        this.itemColors = ItemColors.init(this.blockColors);
        this.renderItem = new RenderItem(this.renderEngine, this.modelManager, this.itemColors);
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        this.itemRenderer = new ItemRenderer(this);
        this.resourceManager.registerReloadListener(this.renderItem);
        this.entityRenderer = new EntityRenderer(this, this.resourceManager);
        this.resourceManager.registerReloadListener(this.entityRenderer);
        this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.blockColors);
        this.resourceManager.registerReloadListener(this.blockRenderDispatcher);
        this.renderGlobal = new RenderGlobal(this);
        this.resourceManager.registerReloadListener(this.renderGlobal);
        this.populateSearchTreeManager();
        this.resourceManager.registerReloadListener(this.searchTreeManager);
        GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new ParticleManager(this.world, this.renderEngine);
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);
        (this.minced = new Minced()).init();
        try {
            Minced.getInstance().fileManager.init();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (this.serverName != null) {
            this.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort));
        }
        else {
            this.displayGuiScreen(new GuiMainMenu());
        }
        this.renderEngine.deleteTexture(this.mojangLogo);
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.debugRenderer = new DebugRenderer(this);
        if (this.gameSettings.fullScreen && !this.fullscreen) {
            this.toggleFullscreen();
        }
        try {
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
        }
        catch (OpenGLException var2) {
            this.gameSettings.enableVsync = false;
            this.gameSettings.saveOptions();
        }
        this.renderGlobal.makeEntityOutlineShader();
    }
    
    private void populateSearchTreeManager() {
        final SearchTree<ItemStack> searchtree = new SearchTree<ItemStack>(p_193988_0_ -> p_193988_0_.getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL).stream().map((Function<? super Object, ?>)TextFormatting::getTextWithoutFormattingCodes).map((Function<? super Object, ?>)String::trim).filter(p_193984_0_ -> !p_193984_0_.isEmpty()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), p_193985_0_ -> Collections.singleton(Item.REGISTRY.getNameForObject(p_193985_0_.getItem())));
        final NonNullList<ItemStack> nonnulllist = NonNullList.create();
        for (final Item item : Item.REGISTRY) {
            item.getSubItems(CreativeTabs.SEARCH, nonnulllist);
        }
        nonnulllist.forEach(searchtree::add);
        final SearchTree<RecipeList> searchtree2 = new SearchTree<RecipeList>(p_193990_0_ -> p_193990_0_.getRecipes().stream().flatMap(p_193993_0_ -> p_193993_0_.getRecipeOutput().getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL).stream()).map((Function<? super Object, ?>)TextFormatting::getTextWithoutFormattingCodes).map((Function<? super Object, ?>)String::trim).filter(p_193994_0_ -> !p_193994_0_.isEmpty()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), p_193991_0_ -> p_193991_0_.getRecipes().stream().map(p_193992_0_ -> Item.REGISTRY.getNameForObject(p_193992_0_.getRecipeOutput().getItem())).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        RecipeBookClient.ALL_RECIPES.forEach(searchtree2::add);
        this.searchTreeManager.register(SearchTreeManager.ITEMS, searchtree);
        this.searchTreeManager.register(SearchTreeManager.RECIPES, searchtree2);
    }
    
    private void registerMetadataSerializers() {
        this.metadataSerializer.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }
    
    private void createDisplay() throws LWJGLException {
        Display.setResizable(true);
        Display.setTitle("Minced Premium 0.3");
        try {
            Display.create(new PixelFormat().withDepthBits(24));
        }
        catch (LWJGLException lwjglexception) {
            Minecraft.LOGGER.error("Couldn't set pixel format", (Throwable)lwjglexception);
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException ex) {}
            if (this.fullscreen) {
                this.updateDisplayMode();
            }
            Display.create();
        }
    }
    
    private void setInitialDisplayMode() throws LWJGLException {
        if (this.fullscreen) {
            Display.setFullscreen(true);
            final DisplayMode displaymode = Display.getDisplayMode();
            this.displayWidth = Math.max(1, displaymode.getWidth());
            this.displayHeight = Math.max(1, displaymode.getHeight());
        }
        else {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }
    }
    
    private void setWindowIcon() {
        final Util.EnumOS util$enumos = Util.getOSType();
        if (util$enumos != Util.EnumOS.OSX) {
            InputStream inputstream = null;
            InputStream inputstream2 = null;
            try {
                inputstream = this.defaultResourcePack.getInputStream(new ResourceLocation("client/images/window/icon64.jpg"));
                inputstream2 = this.defaultResourcePack.getInputStream(new ResourceLocation("client/images/window/icon32.jpg"));
                if (inputstream != null && inputstream2 != null) {
                    Display.setIcon(new ByteBuffer[] { this.readImageToBuffer(inputstream), this.readImageToBuffer(inputstream2) });
                }
            }
            catch (IOException ioexception) {
                Minecraft.LOGGER.error("Couldn't set icon", (Throwable)ioexception);
            }
            finally {
                IOUtils.closeQuietly(inputstream);
                IOUtils.closeQuietly(inputstream2);
            }
        }
    }
    
    private static boolean isJvm64bit() {
        final String[] array;
        final String[] astring = array = new String[] { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        for (final String s : array) {
            final String s2 = System.getProperty(s);
            if (s2 != null && s2.contains("64")) {
                return true;
            }
        }
        return false;
    }
    
    public Framebuffer getFramebuffer() {
        return this.framebuffer;
    }
    
    public String getVersion() {
        return this.launchedVersion;
    }
    
    public String getVersionType() {
        return this.versionType;
    }
    
    private void startTimerHackThread() {
        final Thread thread = new Thread("Timer hack thread") {
            @Override
            public void run() {
                while (Minecraft.this.running) {
                    try {
                        Thread.sleep(2147483647L);
                    }
                    catch (InterruptedException ex) {}
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
    
    public void crashed(final CrashReport crash) {
        this.hasCrashed = true;
        this.crashReporter = crash;
    }
    
    public void displayCrashReport(final CrashReport crashReportIn) {
        final File file1 = new File(getMinecraft().gameDir, "crash-reports");
        final File file2 = new File(file1, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());
        if (crashReportIn.getFile() != null) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            System.exit(-1);
        }
        else if (crashReportIn.saveToFile(file2)) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            System.exit(-1);
        }
        else {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }
    
    public boolean isUnicode() {
        return this.languageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
    }
    
    public void refreshResources() {
        final List<IResourcePack> list = (List<IResourcePack>)Lists.newArrayList((Iterable)this.defaultResourcePacks);
        if (this.integratedServer != null) {
            this.integratedServer.reload();
        }
        for (final ResourcePackRepository.Entry resourcepackrepository$entry : this.resourcePackRepository.getRepositoryEntries()) {
            list.add(resourcepackrepository$entry.getResourcePack());
        }
        if (this.resourcePackRepository.getServerResourcePack() != null) {
            list.add(this.resourcePackRepository.getServerResourcePack());
        }
        try {
            this.resourceManager.reloadResources(list);
        }
        catch (RuntimeException runtimeexception) {
            Minecraft.LOGGER.info("Caught error stitching, removing all assigned resourcepacks", (Throwable)runtimeexception);
            list.clear();
            list.addAll(this.defaultResourcePacks);
            this.resourcePackRepository.setRepositories(Collections.emptyList());
            this.resourceManager.reloadResources(list);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.incompatibleResourcePacks.clear();
            this.gameSettings.saveOptions();
        }
        this.languageManager.parseLanguageMetadata(list);
        if (this.renderGlobal != null) {
            this.renderGlobal.loadRenderers();
        }
    }
    
    private ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        final BufferedImage bufferedimage = ImageIO.read(imageStream);
        final int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        final ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        for (final int i : aint) {
            bytebuffer.putInt(i << 8 | (i >> 24 & 0xFF));
        }
        bytebuffer.flip();
        return bytebuffer;
    }
    
    private void updateDisplayMode() throws LWJGLException {
        final Set<DisplayMode> set = (Set<DisplayMode>)Sets.newHashSet();
        Collections.addAll(set, Display.getAvailableDisplayModes());
        DisplayMode displaymode = Display.getDesktopDisplayMode();
        if (!set.contains(displaymode) && Util.getOSType() == Util.EnumOS.OSX) {
            for (final DisplayMode displaymode2 : Minecraft.MAC_DISPLAY_MODES) {
                boolean flag = true;
                for (final DisplayMode displaymode3 : set) {
                    if (displaymode3.getBitsPerPixel() == 32 && displaymode3.getWidth() == displaymode2.getWidth() && displaymode3.getHeight() == displaymode2.getHeight()) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    for (final DisplayMode displaymode4 : set) {
                        if (displaymode4.getBitsPerPixel() == 32 && displaymode4.getWidth() == displaymode2.getWidth() / 2 && displaymode4.getHeight() == displaymode2.getHeight() / 2) {
                            displaymode = displaymode4;
                            break;
                        }
                    }
                }
            }
        }
        Display.setDisplayMode(displaymode);
        this.displayWidth = displaymode.getWidth();
        this.displayHeight = displaymode.getHeight();
    }
    
    private void drawSplashScreen(final TextureManager textureManagerInstance) throws LWJGLException {
        final ScaledResolution scaledresolution = new ScaledResolution(this);
        final int i = ScaledResolution.getScaleFactor();
        final Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        InputStream inputstream = null;
        try {
            inputstream = this.defaultResourcePack.getInputStream(Minecraft.LOCATION_MOJANG_PNG);
            textureManagerInstance.bindTexture(this.mojangLogo = textureManagerInstance.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputstream))));
        }
        catch (IOException ioexception) {
            Minecraft.LOGGER.error("Unable to load logo: {}", (Object)Minecraft.LOCATION_MOJANG_PNG, (Object)ioexception);
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0, this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(this.displayWidth, this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(this.displayWidth, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int j = 256;
        final int k = 256;
        this.draw((scaledresolution.getScaledWidth() - 256) / 2, (scaledresolution.getScaledHeight() - 256) / 2, 0, 0, 256, 256, 255, 255, 255, 255);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        this.updateDisplay();
    }
    
    public void draw(final int posX, final int posY, final int texU, final int texV, final int width, final int height, final int red, final int green, final int blue, final int alpha) {
        final BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        bufferbuilder.pos(posX, posY + height, 0.0).tex(texU * 0.00390625f, (texV + height) * 0.00390625f).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX + width, posY + height, 0.0).tex((texU + width) * 0.00390625f, (texV + height) * 0.00390625f).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX + width, posY, 0.0).tex((texU + width) * 0.00390625f, texV * 0.00390625f).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX, posY, 0.0).tex(texU * 0.00390625f, texV * 0.00390625f).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }
    
    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }
    
    public void displayGuiScreen(@Nullable GuiScreen guiScreenIn) {
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }
        if (guiScreenIn == null && this.world == null) {
            guiScreenIn = new GuiMainMenu();
        }
        else if (guiScreenIn == null && Minecraft.player.getHealth() <= 0.0f) {
            guiScreenIn = new GuiGameOver(null);
        }
        if (guiScreenIn instanceof GuiMainMenu || guiScreenIn instanceof GuiMultiplayer) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages(true);
        }
        if ((this.currentScreen = guiScreenIn) != null) {
            this.setIngameNotInFocus();
            KeyBinding.unPressAllKeys();
            while (Mouse.next()) {}
            while (Keyboard.next()) {}
            final ScaledResolution scaledresolution = new ScaledResolution(this);
            final int i = scaledresolution.getScaledWidth();
            final int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(this, i, j);
            this.skipRenderWorld = false;
        }
        else {
            this.soundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }
    
    private void checkGLError(final String message) {
        final int i = GlStateManager.glGetError();
        if (i != 0) {
            final String s = GLU.gluErrorString(i);
            Minecraft.LOGGER.error("########## GL ERROR ##########");
            Minecraft.LOGGER.error("@ {}", (Object)message);
            Minecraft.LOGGER.error("{}: {}", (Object)i, (Object)s);
        }
    }
    
    public void shutdownMinecraftApplet() {
        try {
            Minecraft.LOGGER.info("Stopping!");
            try {
                this.loadWorld(null);
            }
            catch (Throwable t) {}
            this.soundHandler.unloadSounds();
        }
        finally {
            Display.destroy();
            if (!this.hasCrashed) {
                System.exit(0);
            }
        }
        System.gc();
    }
    
    private void runGameLoop() throws IOException {
        final long i = System.nanoTime();
        this.profiler.startSection("root");
        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }
        this.timer.updateTimer();
        this.profiler.startSection("scheduledExecutables");
        synchronized (this.scheduledTasks) {
            while (!this.scheduledTasks.isEmpty()) {
                Util.runTask(this.scheduledTasks.poll(), Minecraft.LOGGER);
            }
        }
        this.profiler.endSection();
        final long l = System.nanoTime();
        this.profiler.startSection("tick");
        for (int j = 0; j < Math.min(10, this.timer.elapsedTicks); ++j) {
            this.runTick();
        }
        this.profiler.endStartSection("preRenderErrors");
        final long i2 = System.nanoTime() - l;
        this.checkGLError("Pre render");
        this.profiler.endStartSection("sound");
        this.soundHandler.setListener(Minecraft.player, this.timer.renderPartialTicks);
        this.profiler.endSection();
        this.profiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        this.framebuffer.bindFramebuffer(true);
        this.profiler.startSection("display");
        GlStateManager.enableTexture2D();
        this.profiler.endSection();
        if (!this.skipRenderWorld) {
            this.profiler.endStartSection("gameRenderer");
            this.entityRenderer.updateCameraAndRender(this.isGamePaused ? this.renderPartialTicksPaused : this.timer.renderPartialTicks, i);
            this.profiler.endStartSection("toasts");
            this.toastGui.drawToast(new ScaledResolution(this));
            this.profiler.endSection();
        }
        this.profiler.endSection();
        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
            if (!this.profiler.profilingEnabled) {
                this.profiler.clearProfiling();
            }
            this.profiler.profilingEnabled = true;
            this.displayDebugInfo(i2);
        }
        else {
            this.profiler.profilingEnabled = false;
            this.prevFrameTime = System.nanoTime();
        }
        this.framebuffer.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (Display.isActive()) {
            this.framebuffer.framebufferRender(this.displayWidth, this.displayHeight);
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.entityRenderer.renderStreamIndicator(this.timer.renderPartialTicks);
        GlStateManager.popMatrix();
        this.profiler.startSection("root");
        this.updateDisplay();
        Thread.yield();
        this.checkGLError("Post render");
        ++this.fpsCounter;
        final boolean flag = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.integratedServer.getPublic();
        if (this.isGamePaused != flag) {
            if (this.isGamePaused) {
                this.renderPartialTicksPaused = this.timer.renderPartialTicks;
            }
            else {
                this.timer.renderPartialTicks = this.renderPartialTicksPaused;
            }
            this.isGamePaused = flag;
        }
        final long k = System.nanoTime();
        this.frameTimer.addFrame(k - this.startNanoTime);
        this.startNanoTime = k;
        while (getSystemTime() >= this.debugUpdateTime + 1000L) {
            Minecraft.debugFPS = this.fpsCounter;
            this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", Minecraft.debugFPS, RenderChunk.renderChunksUpdated, (RenderChunk.renderChunksUpdated == 1) ? "" : "s", (this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) ? "inf" : Integer.valueOf(this.gameSettings.limitFramerate), this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", (this.gameSettings.clouds == 0) ? "" : ((this.gameSettings.clouds == 1) ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.useVbo() ? " vbo" : "");
            RenderChunk.renderChunksUpdated = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.usageSnooper.addMemoryStatsToSnooper();
            if (!this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.startSnooper();
            }
        }
        if (this.isFramerateLimitBelowMax()) {
            this.profiler.startSection("fpslimit_wait");
            Display.sync(this.getLimitFramerate());
            this.profiler.endSection();
        }
        Minecraft.frameTime = (System.nanoTime() - i) / 1000000.0;
        this.profiler.endSection();
    }
    
    public void updateDisplay() {
        this.profiler.startSection("display_update");
        Display.update();
        this.profiler.endSection();
        this.checkWindowResize();
    }
    
    protected void checkWindowResize() {
        if (!this.fullscreen && Display.wasResized()) {
            final int i = this.displayWidth;
            final int j = this.displayHeight;
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
        return this.gameSettings.limitFramerate;
    }
    
    public boolean isFramerateLimitBelowMax() {
        return this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }
    
    public void freeMemory() {
        try {
            Minecraft.memoryReserve = new byte[0];
            this.renderGlobal.deleteAllDisplayLists();
        }
        catch (Throwable t) {}
        try {
            System.gc();
            this.loadWorld(null);
        }
        catch (Throwable t2) {}
        System.gc();
    }
    
    private void updateDebugProfilerName(int keyCount) {
        final List<Profiler.Result> list = this.profiler.getProfilingData(this.debugProfilerName);
        if (!list.isEmpty()) {
            final Profiler.Result profiler$result = list.remove(0);
            if (keyCount == 0) {
                if (!profiler$result.profilerName.isEmpty()) {
                    final int i = this.debugProfilerName.lastIndexOf(46);
                    if (i >= 0) {
                        this.debugProfilerName = this.debugProfilerName.substring(0, i);
                    }
                }
            }
            else if (--keyCount < list.size() && !"unspecified".equals(list.get(keyCount).profilerName)) {
                if (!this.debugProfilerName.isEmpty()) {
                    this.debugProfilerName += ".";
                }
                this.debugProfilerName += list.get(keyCount).profilerName;
            }
        }
    }
    
    private void displayDebugInfo(final long elapsedTicksTime) {
        if (this.profiler.profilingEnabled) {
            final List<Profiler.Result> list = this.profiler.getProfilingData(this.debugProfilerName);
            final Profiler.Result profiler$result = list.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, this.displayWidth, this.displayHeight, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GlStateManager.glLineWidth(1.0f);
            GlStateManager.disableTexture2D();
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final int i = 160;
            final int j = this.displayWidth - 160 - 10;
            final int k = this.displayHeight - 320;
            GlStateManager.enableBlend();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(j - 176.0f, k - 96.0f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos(j - 176.0f, k + 320, 0.0).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos(j + 176.0f, k + 320, 0.0).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos(j + 176.0f, k - 96.0f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            double d0 = 0.0;
            for (int l = 0; l < list.size(); ++l) {
                final Profiler.Result profiler$result2 = list.get(l);
                final int i2 = MathHelper.floor(profiler$result2.usePercentage / 4.0) + 1;
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                final int j2 = profiler$result2.getColor();
                final int k2 = j2 >> 16 & 0xFF;
                final int l2 = j2 >> 8 & 0xFF;
                final int i3 = j2 & 0xFF;
                bufferbuilder.pos(j, k, 0.0).color(k2, l2, i3, 255).endVertex();
                for (int j3 = i2; j3 >= 0; --j3) {
                    final float f = (float)((d0 + profiler$result2.usePercentage * j3 / i2) * 6.283185307179586 / 100.0);
                    final float f2 = MathHelper.sin(f) * 160.0f;
                    final float f3 = MathHelper.cos(f) * 160.0f * 0.5f;
                    bufferbuilder.pos(j + f2, k - f3, 0.0).color(k2, l2, i3, 255).endVertex();
                }
                tessellator.draw();
                bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
                for (int i4 = i2; i4 >= 0; --i4) {
                    final float f4 = (float)((d0 + profiler$result2.usePercentage * i4 / i2) * 6.283185307179586 / 100.0);
                    final float f5 = MathHelper.sin(f4) * 160.0f;
                    final float f6 = MathHelper.cos(f4) * 160.0f * 0.5f;
                    bufferbuilder.pos(j + f5, k - f6, 0.0).color(k2 >> 1, l2 >> 1, i3 >> 1, 255).endVertex();
                    bufferbuilder.pos(j + f5, k - f6 + 10.0f, 0.0).color(k2 >> 1, l2 >> 1, i3 >> 1, 255).endVertex();
                }
                tessellator.draw();
                d0 += profiler$result2.usePercentage;
            }
            final DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            String s = "";
            if (!"unspecified".equals(profiler$result.profilerName)) {
                s += "[0] ";
            }
            if (profiler$result.profilerName.isEmpty()) {
                s += "ROOT ";
            }
            else {
                s = s + profiler$result.profilerName + ' ';
            }
            final int l3 = 16777215;
            this.fontRenderer.drawStringWithShadow(s, (float)(j - 160), (float)(k - 80 - 16), 16777215);
            s = decimalformat.format(profiler$result.totalUsePercentage) + "%";
            this.fontRenderer.drawStringWithShadow(s, (float)(j + 160 - this.fontRenderer.getStringWidth(s)), (float)(k - 80 - 16), 16777215);
            for (int k3 = 0; k3 < list.size(); ++k3) {
                final Profiler.Result profiler$result3 = list.get(k3);
                final StringBuilder stringbuilder = new StringBuilder();
                if ("unspecified".equals(profiler$result3.profilerName)) {
                    stringbuilder.append("[?] ");
                }
                else {
                    stringbuilder.append("[").append(k3 + 1).append("] ");
                }
                String s2 = stringbuilder.append(profiler$result3.profilerName).toString();
                this.fontRenderer.drawStringWithShadow(s2, (float)(j - 160), (float)(k + 80 + k3 * 8 + 20), profiler$result3.getColor());
                s2 = decimalformat.format(profiler$result3.usePercentage) + "%";
                this.fontRenderer.drawStringWithShadow(s2, (float)(j + 160 - 50 - this.fontRenderer.getStringWidth(s2)), (float)(k + 80 + k3 * 8 + 20), profiler$result3.getColor());
                s2 = decimalformat.format(profiler$result3.totalUsePercentage) + "%";
                this.fontRenderer.drawStringWithShadow(s2, (float)(j + 160 - this.fontRenderer.getStringWidth(s2)), (float)(k + 80 + k3 * 8 + 20), profiler$result3.getColor());
            }
        }
    }
    
    public void shutdown() {
        Minced.getInstance().configManager.saveConfig("default");
        this.running = false;
    }
    
    public void setIngameFocus() {
        if (Display.isActive() && !this.inGameHasFocus) {
            if (!Minecraft.IS_RUNNING_ON_MAC) {
                KeyBinding.updateKeyBindState();
            }
            this.inGameHasFocus = true;
            this.mouseHelper.grabMouseCursor();
            this.displayGuiScreen(null);
            this.leftClickCounter = 10000;
        }
    }
    
    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }
    
    public void displayInGameMenu() {
        if (this.currentScreen == null) {
            this.displayGuiScreen(new GuiIngameMenu());
            if (this.isSingleplayer() && !this.integratedServer.getPublic()) {
                this.soundHandler.pauseSounds();
            }
        }
    }
    
    private void sendClickBlockToController(final boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }
        if (this.leftClickCounter <= 0 && !Minecraft.player.isHandActive()) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR && this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.sideHit)) {
                    this.effectRenderer.addBlockHitEffects(blockpos, this.objectMouseOver.sideHit);
                    Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            else {
                this.playerController.resetBlockRemoving();
            }
        }
    }
    
    public void clickMouse() {
        if (this.leftClickCounter <= 0) {
            if (this.objectMouseOver == null) {
                Minecraft.LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");
                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            }
            else if (!Minecraft.player.isRowingBoat()) {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY: {
                        this.playerController.attackEntity(Minecraft.player, this.objectMouseOver.entityHit);
                        break;
                    }
                    case BLOCK: {
                        final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                        if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR) {
                            this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
                            break;
                        }
                    }
                    case MISS: {
                        if (this.playerController.isNotCreative()) {
                            this.leftClickCounter = 10;
                        }
                        Minecraft.player.resetCooldown();
                        break;
                    }
                }
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
    
    public void rightClickMouse() {
        if (!this.playerController.getIsHittingBlock()) {
            this.rightClickDelayTimer = 4;
            if (!Minecraft.player.isRowingBoat()) {
                if (this.objectMouseOver == null) {
                    Minecraft.LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
                }
                for (final EnumHand enumhand : EnumHand.values()) {
                    final ItemStack itemstack = Minecraft.player.getHeldItem(enumhand);
                    final EventRightClick eventRightClick = new EventRightClick();
                    EventManager.call(eventRightClick);
                    if (itemstack.getItem() instanceof ItemAppleGold && eventRightClick.isCanceled()) {
                        break;
                    }
                    if (this.objectMouseOver != null) {
                        switch (this.objectMouseOver.typeOfHit) {
                            case ENTITY: {
                                final EventInteract interact = new EventInteract(this.objectMouseOver.entityHit);
                                EventManager.call(interact);
                                if (interact.isCanceled()) {
                                    break;
                                }
                                if (this.playerController.interactWithEntity(Minecraft.player, this.objectMouseOver.entityHit, this.objectMouseOver, enumhand) == EnumActionResult.SUCCESS) {
                                    return;
                                }
                                if (this.playerController.interactWithEntity(Minecraft.player, this.objectMouseOver.entityHit, enumhand) == EnumActionResult.SUCCESS) {
                                    return;
                                }
                                break;
                            }
                            case BLOCK: {
                                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                                if (this.world.getBlockState(blockpos).getMaterial() == Material.AIR) {
                                    break;
                                }
                                final int i = itemstack.getCount();
                                final EnumActionResult enumactionresult = this.playerController.processRightClickBlock(Minecraft.player, this.world, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec, enumhand);
                                if (enumactionresult == EnumActionResult.SUCCESS) {
                                    Minecraft.player.swingArm(enumhand);
                                    if (!itemstack.isEmpty() && (itemstack.getCount() != i || this.playerController.isInCreativeMode())) {
                                        this.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                    }
                                    return;
                                }
                                break;
                            }
                        }
                    }
                    if (!itemstack.isEmpty() && this.playerController.processRightClick(Minecraft.player, this.world, enumhand) == EnumActionResult.SUCCESS) {
                        this.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                        return;
                    }
                }
            }
        }
    }
    
    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            this.gameSettings.fullScreen = this.fullscreen;
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
            }
            else {
                Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
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
            }
            else {
                this.updateFramebufferSize();
            }
            Display.setFullscreen(this.fullscreen);
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
            this.updateDisplay();
        }
        catch (Exception exception) {
            Minecraft.LOGGER.error("Couldn't toggle fullscreen", (Throwable)exception);
        }
    }
    
    private void resize(final int width, final int height) {
        this.displayWidth = Math.max(1, width);
        this.displayHeight = Math.max(1, height);
        if (this.currentScreen != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(this);
            this.currentScreen.onResize(this, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
            BlurUtility.bloomFramebuffer = ShaderUtility.createFrameBuffer(BlurUtility.bloomFramebuffer);
            BloomUtil.framebuffer = ShaderUtility.createFrameBuffer(BloomUtil.framebuffer);
        }
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.updateFramebufferSize();
    }
    
    private void updateFramebufferSize() {
        this.framebuffer.createBindFramebuffer(this.displayWidth, this.displayHeight);
        if (this.entityRenderer != null) {
            this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
        }
    }
    
    public MusicTicker getMusicTicker() {
        return this.musicTicker;
    }
    
    public void runTick() throws IOException {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }
        this.profiler.startSection("gui");
        if (!this.isGamePaused) {
            this.ingameGUI.updateTick();
        }
        this.profiler.endSection();
        this.entityRenderer.getMouseOver(1.0f);
        this.tutorial.onMouseHover(this.world, this.objectMouseOver);
        this.profiler.startSection("gameMode");
        if (!this.isGamePaused && this.world != null) {
            this.playerController.updateController();
        }
        this.profiler.endStartSection("textures");
        if (this.world != null) {
            this.renderEngine.tick();
        }
        if (this.currentScreen == null && Minecraft.player != null) {
            if (Minecraft.player.getHealth() <= 0.0f && !(this.currentScreen instanceof GuiGameOver)) {
                this.displayGuiScreen(null);
            }
            else if (Minecraft.player.isPlayerSleeping() && this.world != null) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        }
        else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !Minecraft.player.isPlayerSleeping()) {
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
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.addDetail("Screen name", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return Minecraft.this.currentScreen.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }
            if (this.currentScreen != null) {
                try {
                    this.currentScreen.updateScreen();
                }
                catch (Throwable throwable2) {
                    final CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Ticking screen");
                    final CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected screen");
                    crashreportcategory2.addDetail("Screen name", new ICrashReportDetail<String>() {
                        @Override
                        public String call() throws Exception {
                            return Minecraft.this.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    throw new ReportedException(crashreport2);
                }
            }
        }
        if (this.currentScreen == null || this.currentScreen.allowUserInput) {
            this.profiler.endStartSection("mouse");
            this.runTickMouse();
            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }
            this.profiler.endStartSection("keyboard");
            this.runTickKeyboard();
        }
        if (this.world != null) {
            if (Minecraft.player != null) {
                ++this.joinPlayerCounter;
                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.world.joinEntityInSurroundings(Minecraft.player);
                }
            }
            this.profiler.endStartSection("gameRenderer");
            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }
            this.profiler.endStartSection("levelRenderer");
            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }
            this.profiler.endStartSection("level");
            if (!this.isGamePaused) {
                if (this.world.getLastLightningBolt() > 0) {
                    this.world.setLastLightningBolt(this.world.getLastLightningBolt() - 1);
                }
                EventManager.call(new EventTick());
                this.world.updateEntities();
            }
        }
        else if (this.entityRenderer.isShaderActive()) {
            this.entityRenderer.stopUseShader();
        }
        if (!this.isGamePaused) {
            this.musicTicker.update();
            this.soundHandler.update();
        }
        if (this.world != null) {
            if (!this.isGamePaused) {
                this.world.setAllowedSpawnTypes(this.world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                this.tutorial.update();
                try {
                    this.world.tick();
                }
                catch (Throwable throwable3) {
                    final CrashReport crashreport3 = CrashReport.makeCrashReport(throwable3, "Exception in world tick");
                    if (this.world == null) {
                        final CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Affected level");
                        crashreportcategory3.addCrashSection("Problem", "Level is null!");
                    }
                    else {
                        this.world.addWorldInfoToCrashReport(crashreport3);
                    }
                    throw new ReportedException(crashreport3);
                }
            }
            this.profiler.endStartSection("animateTick");
            if (!this.isGamePaused && this.world != null) {
                this.world.doVoidFogParticles(MathHelper.floor(Minecraft.player.posX), MathHelper.floor(Minecraft.player.posY), MathHelper.floor(Minecraft.player.posZ));
            }
            this.profiler.endStartSection("particles");
            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        }
        else if (this.networkManager != null) {
            this.profiler.endStartSection("pendingConnection");
            this.networkManager.processReceivedPackets();
        }
        this.profiler.endSection();
        this.systemTime = getSystemTime();
    }
    
    private void runTickKeyboard() throws IOException {
        while (Keyboard.next()) {
            final int i = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
            if (this.debugCrashKeyPressTime > 0L) {
                if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
                    throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                }
                if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                    this.debugCrashKeyPressTime = -1L;
                }
            }
            else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                this.actionKeyF3 = true;
                this.debugCrashKeyPressTime = getSystemTime();
            }
            this.dispatchKeypresses();
            if (this.currentScreen != null) {
                this.currentScreen.handleKeyboardInput();
            }
            final boolean flag = Keyboard.getEventKeyState();
            if (flag) {
                if (i == 62 && this.entityRenderer != null) {
                    this.entityRenderer.switchUseShader();
                }
                boolean flag2 = false;
                if (this.currentScreen == null) {
                    Minced.getInstance().keyTyped(i);
                    if (i == 1) {
                        this.displayInGameMenu();
                    }
                    flag2 = (Keyboard.isKeyDown(61) && this.processKeyF3(i));
                    this.actionKeyF3 |= flag2;
                    if (i == 59) {
                        this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                    }
                }
                if (flag2) {
                    KeyBinding.setKeyBindState(i, false);
                }
                else {
                    KeyBinding.setKeyBindState(i, true);
                    KeyBinding.onTick(i);
                }
                if (!this.gameSettings.showDebugProfilerChart) {
                    continue;
                }
                if (i == 11) {
                    this.updateDebugProfilerName(0);
                }
                for (int j = 0; j < 9; ++j) {
                    if (i == 2 + j) {
                        this.updateDebugProfilerName(j + 1);
                    }
                }
            }
            else {
                KeyBinding.setKeyBindState(i, false);
                if (i != 61) {
                    continue;
                }
                if (this.actionKeyF3) {
                    this.actionKeyF3 = false;
                }
                else {
                    this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                    this.gameSettings.showDebugProfilerChart = (this.gameSettings.showDebugInfo && GuiScreen.isShiftKeyDown());
                    this.gameSettings.showLagometer = (this.gameSettings.showDebugInfo && GuiScreen.isAltKeyDown());
                }
            }
        }
        this.processKeyBinds();
    }
    
    private boolean processKeyF3(final int auxKey) {
        if (auxKey == 30) {
            this.renderGlobal.loadRenderers();
            this.debugFeedbackTranslated("debug.reload_chunks.message", new Object[0]);
            return true;
        }
        if (auxKey == 48) {
            final boolean flag1 = !this.renderManager.isDebugBoundingBox();
            this.renderManager.setDebugBoundingBox(flag1);
            this.debugFeedbackTranslated(flag1 ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off", new Object[0]);
            return true;
        }
        if (auxKey == 32) {
            if (this.ingameGUI != null) {
                this.ingameGUI.getChatGUI().clearChatMessages(false);
            }
            return true;
        }
        if (auxKey == 33) {
            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
            this.debugFeedbackTranslated("debug.cycle_renderdistance.message", this.gameSettings.renderDistanceChunks);
            return true;
        }
        if (auxKey == 34) {
            final boolean flag2 = this.debugRenderer.toggleChunkBorders();
            this.debugFeedbackTranslated(flag2 ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off", new Object[0]);
            return true;
        }
        if (auxKey == 35) {
            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
            this.debugFeedbackTranslated(this.gameSettings.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off", new Object[0]);
            this.gameSettings.saveOptions();
            return true;
        }
        if (auxKey == 49) {
            if (!Minecraft.player.canUseCommand(2, "")) {
                this.debugFeedbackTranslated("debug.creative_spectator.error", new Object[0]);
            }
            else if (Minecraft.player.isCreative()) {
                Minecraft.player.sendChatMessage("/gamemode spectator");
            }
            else if (Minecraft.player.isSpectator()) {
                Minecraft.player.sendChatMessage("/gamemode creative");
            }
            return true;
        }
        if (auxKey == 25) {
            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
            this.gameSettings.saveOptions();
            this.debugFeedbackTranslated(this.gameSettings.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off", new Object[0]);
            return true;
        }
        if (auxKey == 16) {
            this.debugFeedbackTranslated("debug.help.message", new Object[0]);
            final GuiNewChat guinewchat = this.ingameGUI.getChatGUI();
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_chunks.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.show_hitboxes.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.clear_chat.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.cycle_renderdistance.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.chunk_boundaries.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.advanced_tooltips.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.creative_spectator.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.pause_focus.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.help.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_resourcepacks.help", new Object[0]));
            return true;
        }
        if (auxKey == 20) {
            this.debugFeedbackTranslated("debug.reload_resourcepacks.message", new Object[0]);
            this.refreshResources();
            return true;
        }
        return false;
    }
    
    private void processKeyBinds() {
        while (this.gameSettings.keyBindTogglePerspective.isPressed()) {
            final GameSettings gameSettings = this.gameSettings;
            ++gameSettings.thirdPersonView;
            if (this.gameSettings.thirdPersonView > 2) {
                this.gameSettings.thirdPersonView = 0;
            }
            if (this.gameSettings.thirdPersonView == 0) {
                this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
            }
            else if (this.gameSettings.thirdPersonView == 1) {
                this.entityRenderer.loadEntityShader(null);
            }
            this.renderGlobal.setDisplayListEntitiesDirty();
        }
        while (this.gameSettings.keyBindSmoothCamera.isPressed()) {
            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
        }
        for (int i = 0; i < 9; ++i) {
            final boolean flag = this.gameSettings.keyBindSaveToolbar.isKeyDown();
            final boolean flag2 = this.gameSettings.keyBindLoadToolbar.isKeyDown();
            if (this.gameSettings.keyBindsHotbar[i].isPressed()) {
                if (Minecraft.player.isSpectator()) {
                    this.ingameGUI.getSpectatorGui().onHotbarSelected(i);
                }
                else if (!Minecraft.player.isCreative() || this.currentScreen != null || (!flag2 && !flag)) {
                    Minecraft.player.inventory.currentItem = i;
                }
                else {
                    GuiContainerCreative.handleHotbarSnapshots(this, i, flag2, flag);
                }
            }
        }
        while (this.gameSettings.keyBindInventory.isPressed()) {
            if (this.playerController.isRidingHorse()) {
                Minecraft.player.sendHorseInventory();
            }
            else {
                this.tutorial.openInventory();
                this.displayGuiScreen(new GuiInventory(Minecraft.player));
            }
        }
        while (this.gameSettings.keyBindAdvancements.isPressed()) {
            this.displayGuiScreen(new GuiScreenAdvancements(Minecraft.player.connection.getAdvancementManager()));
        }
        while (this.gameSettings.keyBindSwapHands.isPressed()) {
            if (!Minecraft.player.isSpectator()) {
                this.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
        while (this.gameSettings.keyBindDrop.isPressed()) {
            if (!Minecraft.player.isSpectator()) {
                Minecraft.player.dropItem(GuiScreen.isCtrlKeyDown());
            }
        }
        final boolean flag3 = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;
        if (flag3) {
            while (this.gameSettings.keyBindChat.isPressed()) {
                this.displayGuiScreen(new GuiChat());
            }
            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed()) {
                this.displayGuiScreen(new GuiChat("/"));
            }
        }
        if (Minecraft.player.isHandActive()) {
            if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
                this.playerController.onStoppedUsingItem(Minecraft.player);
            }
            while (this.gameSettings.keyBindAttack.isPressed()) {}
            while (this.gameSettings.keyBindUseItem.isPressed()) {}
            while (this.gameSettings.keyBindPickBlock.isPressed()) {}
        }
        else {
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
        if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !Minecraft.player.isHandActive()) {
            this.rightClickMouse();
        }
        this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
    }
    
    private void runTickMouse() throws IOException {
        while (Mouse.next()) {
            final int i = Mouse.getEventButton();
            KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());
            if (Mouse.getEventButtonState()) {
                EventManager.call(new MouseEvent(i));
                if (Minecraft.player.isSpectator() && i == 2) {
                    this.ingameGUI.getSpectatorGui().onMiddleClick();
                }
                else {
                    KeyBinding.onTick(i - 100);
                }
            }
            final long j = getSystemTime() - this.systemTime;
            if (j <= 200L) {
                int k = Mouse.getEventDWheel();
                if (k != 0) {
                    if (Minecraft.player.isSpectator()) {
                        k = ((k < 0) ? -1 : 1);
                        if (this.ingameGUI.getSpectatorGui().isMenuActive()) {
                            this.ingameGUI.getSpectatorGui().onMouseScroll(-k);
                        }
                        else {
                            final float f = MathHelper.clamp(Minecraft.player.capabilities.getFlySpeed() + k * 0.005f, 0.0f, 0.2f);
                            Minecraft.player.capabilities.setFlySpeed(f);
                        }
                    }
                    else {
                        Minecraft.player.inventory.changeCurrentItem(k);
                    }
                }
                if (this.currentScreen == null) {
                    if (this.inGameHasFocus || !Mouse.getEventButtonState()) {
                        continue;
                    }
                    this.setIngameFocus();
                }
                else {
                    if (this.currentScreen == null) {
                        continue;
                    }
                    this.currentScreen.handleMouseInput();
                }
            }
        }
    }
    
    private void debugFeedbackTranslated(final String untranslatedTemplate, final Object... objs) {
        this.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("").appendSibling(new TextComponentTranslation("debug.prefix", new Object[0]).setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true))).appendText(" ").appendSibling(new TextComponentTranslation(untranslatedTemplate, objs)));
    }
    
    public void launchIntegratedServer(final String folderName, final String worldName, @Nullable WorldSettings worldSettingsIn) {
        this.loadWorld(null);
        System.gc();
        final ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null && worldSettingsIn != null) {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }
        if (worldSettingsIn == null) {
            worldSettingsIn = new WorldSettings(worldinfo);
        }
        try {
            final YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
            final MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            final GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            final PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(this.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
            TileEntitySkull.setProfileCache(playerprofilecache);
            TileEntitySkull.setSessionService(minecraftsessionservice);
            PlayerProfileCache.setOnlineMode(false);
            (this.integratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, playerprofilecache)).startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }
        this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));
        while (!this.integratedServer.serverIsInRunLoop()) {
            final String s = this.integratedServer.getUserMessage();
            if (s != null) {
                this.loadingScreen.displayLoadingString(I18n.format(s, new Object[0]));
            }
            else {
                this.loadingScreen.displayLoadingString("");
            }
            try {
                Thread.sleep(200L);
            }
            catch (InterruptedException ex) {}
        }
        this.displayGuiScreen(new GuiScreenWorking());
        final SocketAddress socketaddress = this.integratedServer.getNetworkSystem().addLocalEndpoint();
        final NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, null));
        networkmanager.sendPacket(new C00Handshake(socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.sendPacket(new CPacketLoginStart(this.getSession().getProfile()));
        this.networkManager = networkmanager;
    }
    
    public void loadWorld(@Nullable final WorldClient worldClientIn) {
        this.loadWorld(worldClientIn, "");
    }
    
    public void loadWorld(@Nullable final WorldClient worldClientIn, final String loadingMessage) {
        if (worldClientIn == null) {
            final NetHandlerPlayClient nethandlerplayclient = this.getConnection();
            if (nethandlerplayclient != null) {
                nethandlerplayclient.cleanup();
            }
            if (this.integratedServer != null && this.integratedServer.isAnvilFileSet()) {
                this.integratedServer.initiateShutdown();
            }
            this.integratedServer = null;
            this.entityRenderer.resetData();
            this.playerController = null;
        }
        this.renderViewEntity = null;
        this.networkManager = null;
        if (this.loadingScreen != null) {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.displayLoadingString("");
        }
        if (worldClientIn == null && this.world != null) {
            this.resourcePackRepository.clearResourcePack();
            this.ingameGUI.resetPlayersOverlayFooterHeader();
            this.setServerData(null);
            this.integratedServerIsRunning = false;
        }
        this.soundHandler.stopSounds();
        this.world = worldClientIn;
        if (this.renderGlobal != null) {
            this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
        }
        if (this.effectRenderer != null) {
            this.effectRenderer.clearEffects(worldClientIn);
        }
        TileEntityRendererDispatcher.instance.setWorld(worldClientIn);
        if (worldClientIn != null) {
            if (!this.integratedServerIsRunning) {
                final AuthenticationService authenticationservice = (AuthenticationService)new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
                final MinecraftSessionService minecraftsessionservice = authenticationservice.createMinecraftSessionService();
                final GameProfileRepository gameprofilerepository = authenticationservice.createProfileRepository();
                final PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(this.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
                TileEntitySkull.setProfileCache(playerprofilecache);
                TileEntitySkull.setSessionService(minecraftsessionservice);
                PlayerProfileCache.setOnlineMode(false);
            }
            if (Minecraft.player == null) {
                Minecraft.player = this.playerController.createPlayer(worldClientIn, new StatisticsManager(), new RecipeBookClient());
                this.playerController.flipPlayer(Minecraft.player);
            }
            Minecraft.player.preparePlayerToSpawn();
            worldClientIn.spawnEntity(Minecraft.player);
            Minecraft.player.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(Minecraft.player);
            this.renderViewEntity = Minecraft.player;
        }
        else {
            this.saveLoader.flushCache();
            Minecraft.player = null;
        }
        System.gc();
        this.systemTime = 0L;
    }
    
    public void setDimensionAndSpawnPlayer(final int dimension) {
        this.world.setInitialSpawnLocation();
        this.world.removeAllEntities();
        int i = 0;
        String s = null;
        if (Minecraft.player != null) {
            i = Minecraft.player.getEntityId();
            this.world.removeEntity(Minecraft.player);
            s = Minecraft.player.getServerBrand();
        }
        this.renderViewEntity = null;
        final EntityPlayerSP entityplayersp = Minecraft.player;
        Minecraft.player = this.playerController.createPlayer(this.world, (Minecraft.player == null) ? new StatisticsManager() : Minecraft.player.getStatFileWriter(), (Minecraft.player == null) ? new RecipeBook() : Minecraft.player.getRecipeBook());
        Minecraft.player.getDataManager().setEntryValues(entityplayersp.getDataManager().getAll());
        Minecraft.player.dimension = dimension;
        this.renderViewEntity = Minecraft.player;
        Minecraft.player.preparePlayerToSpawn();
        Minecraft.player.setServerBrand(s);
        this.world.spawnEntity(Minecraft.player);
        this.playerController.flipPlayer(Minecraft.player);
        Minecraft.player.movementInput = new MovementInputFromOptions(this.gameSettings);
        Minecraft.player.setEntityId(i);
        this.playerController.setPlayerCapabilities(Minecraft.player);
        Minecraft.player.setReducedDebug(entityplayersp.hasReducedDebug());
        if (this.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }
    }
    
    public final boolean isDemo() {
        return this.isDemo;
    }
    
    @Nullable
    public NetHandlerPlayClient getConnection() {
        return (Minecraft.player == null) ? null : Minecraft.player.connection;
    }
    
    public static boolean isGuiEnabled() {
        return Minecraft.instance == null || !Minecraft.instance.gameSettings.hideGUI;
    }
    
    public static boolean isFancyGraphicsEnabled() {
        return Minecraft.instance != null && Minecraft.instance.gameSettings.fancyGraphics;
    }
    
    public static boolean isAmbientOcclusionEnabled() {
        return Minecraft.instance != null && Minecraft.instance.gameSettings.ambientOcclusion != 0;
    }
    
    private void middleClickMouse() {
        if (this.objectMouseOver != null && this.objectMouseOver.typeOfHit != RayTraceResult.Type.MISS) {
            final boolean flag = Minecraft.player.capabilities.isCreativeMode;
            TileEntity tileentity = null;
            ItemStack itemstack;
            if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                final IBlockState iblockstate = this.world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (iblockstate.getMaterial() == Material.AIR) {
                    return;
                }
                itemstack = block.getItem(this.world, blockpos, iblockstate);
                if (itemstack.isEmpty()) {
                    return;
                }
                if (flag && GuiScreen.isCtrlKeyDown() && block.hasTileEntity()) {
                    tileentity = this.world.getTileEntity(blockpos);
                }
            }
            else {
                if (this.objectMouseOver.typeOfHit != RayTraceResult.Type.ENTITY || this.objectMouseOver.entityHit == null || !flag) {
                    return;
                }
                if (this.objectMouseOver.entityHit instanceof EntityPainting) {
                    itemstack = new ItemStack(Items.PAINTING);
                }
                else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
                    itemstack = new ItemStack(Items.LEAD);
                }
                else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
                    final EntityItemFrame entityitemframe = (EntityItemFrame)this.objectMouseOver.entityHit;
                    final ItemStack itemstack2 = entityitemframe.getDisplayedItem();
                    if (itemstack2.isEmpty()) {
                        itemstack = new ItemStack(Items.ITEM_FRAME);
                    }
                    else {
                        itemstack = itemstack2.copy();
                    }
                }
                else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
                    final EntityMinecart entityminecart = (EntityMinecart)this.objectMouseOver.entityHit;
                    Item item1 = null;
                    switch (entityminecart.getType()) {
                        case FURNACE: {
                            item1 = Items.FURNACE_MINECART;
                            break;
                        }
                        case CHEST: {
                            item1 = Items.CHEST_MINECART;
                            break;
                        }
                        case TNT: {
                            item1 = Items.TNT_MINECART;
                            break;
                        }
                        case HOPPER: {
                            item1 = Items.HOPPER_MINECART;
                            break;
                        }
                        case COMMAND_BLOCK: {
                            item1 = Items.COMMAND_BLOCK_MINECART;
                            break;
                        }
                        default: {
                            item1 = Items.MINECART;
                            break;
                        }
                    }
                    itemstack = new ItemStack(item1);
                }
                else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
                    itemstack = new ItemStack(((EntityBoat)this.objectMouseOver.entityHit).getItemBoat());
                }
                else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
                    itemstack = new ItemStack(Items.ARMOR_STAND);
                }
                else if (this.objectMouseOver.entityHit instanceof EntityEnderCrystal) {
                    itemstack = new ItemStack(Items.END_CRYSTAL);
                }
                else {
                    final ResourceLocation resourcelocation = EntityList.getKey(this.objectMouseOver.entityHit);
                    if (resourcelocation == null || !EntityList.ENTITY_EGGS.containsKey(resourcelocation)) {
                        return;
                    }
                    itemstack = new ItemStack(Items.SPAWN_EGG);
                    ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, resourcelocation);
                }
            }
            if (itemstack.isEmpty()) {
                String s = "";
                if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    s = Block.REGISTRY.getNameForObject(this.world.getBlockState(this.objectMouseOver.getBlockPos()).getBlock()).toString();
                }
                else if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                    s = EntityList.getKey(this.objectMouseOver.entityHit).toString();
                }
                Minecraft.LOGGER.warn("Picking on: [{}] {} gave null item", (Object)this.objectMouseOver.typeOfHit, (Object)s);
            }
            else {
                final InventoryPlayer inventoryplayer = Minecraft.player.inventory;
                if (tileentity != null) {
                    this.storeTEInStack(itemstack, tileentity);
                }
                final int i = inventoryplayer.getSlotFor(itemstack);
                if (flag) {
                    inventoryplayer.setPickedItemStack(itemstack);
                    this.playerController.sendSlotPacket(Minecraft.player.getHeldItem(EnumHand.MAIN_HAND), 36 + inventoryplayer.currentItem);
                }
                else if (i != -1) {
                    if (InventoryPlayer.isHotbar(i)) {
                        inventoryplayer.currentItem = i;
                    }
                    else {
                        this.playerController.pickItem(i);
                    }
                }
            }
        }
    }
    
    private ItemStack storeTEInStack(final ItemStack stack, final TileEntity te) {
        final NBTTagCompound nbttagcompound = te.writeToNBT(new NBTTagCompound());
        if (stack.getItem() == Items.SKULL && nbttagcompound.hasKey("Owner")) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            nbttagcompound3.setTag("SkullOwner", nbttagcompound2);
            stack.setTagCompound(nbttagcompound3);
            return stack;
        }
        stack.setTagInfo("BlockEntityTag", nbttagcompound);
        final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
        final NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag(new NBTTagString("(+NBT)"));
        nbttagcompound4.setTag("Lore", nbttaglist);
        stack.setTagInfo("display", nbttagcompound4);
        return stack;
    }
    
    public CrashReport addGraphicsAndWorldToCrashReport(final CrashReport theCrash) {
        theCrash.getCategory().addDetail("Launched Version", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.launchedVersion;
            }
        });
        theCrash.getCategory().addDetail("LWJGL", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Sys.getVersion();
            }
        });
        theCrash.getCategory().addDetail("OpenGL", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return GlStateManager.glGetString(7937) + " GL version " + GlStateManager.glGetString(7938) + ", " + GlStateManager.glGetString(7936);
            }
        });
        theCrash.getCategory().addDetail("GL Caps", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return OpenGlHelper.getLogText();
            }
        });
        theCrash.getCategory().addDetail("Using VBOs", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return Minecraft.this.gameSettings.useVbo ? "Yes" : "No";
            }
        });
        theCrash.getCategory().addDetail("Is Modded", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                final String s = ClientBrandRetriever.getClientModName();
                if (!"vanilla".equals(s)) {
                    return "Definitely; Client brand changed to '" + s + "'";
                }
                return (Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.";
            }
        });
        theCrash.getCategory().addDetail("Type", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return "Client (map_client.txt)";
            }
        });
        theCrash.getCategory().addDetail("Resource Packs", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                final StringBuilder stringbuilder = new StringBuilder();
                for (final String s : Minecraft.this.gameSettings.resourcePacks) {
                    if (stringbuilder.length() > 0) {
                        stringbuilder.append(", ");
                    }
                    stringbuilder.append(s);
                    if (Minecraft.this.gameSettings.incompatibleResourcePacks.contains(s)) {
                        stringbuilder.append(" (incompatible)");
                    }
                }
                return stringbuilder.toString();
            }
        });
        theCrash.getCategory().addDetail("Current Language", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.languageManager.getCurrentLanguage().toString();
            }
        });
        theCrash.getCategory().addDetail("Profiler Position", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.profiler.profilingEnabled ? Minecraft.this.profiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });
        theCrash.getCategory().addDetail("CPU", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return OpenGlHelper.getCpu();
            }
        });
        if (this.world != null) {
            this.world.addWorldInfoToCrashReport(theCrash);
        }
        return theCrash;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.instance;
    }
    
    public ListenableFuture<Object> scheduleResourcesRefresh() {
        return this.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Minecraft.this.refreshResources();
            }
        });
    }
    
    @Override
    public void addServerStatsToSnooper(final Snooper playerSnooper) {
        playerSnooper.addClientStat("fps", Minecraft.debugFPS);
        playerSnooper.addClientStat("vsync_enabled", this.gameSettings.enableVsync);
        playerSnooper.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
        playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
        playerSnooper.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerSnooper.addClientStat("current_action", this.getCurrentAction());
        playerSnooper.addClientStat("language", (this.gameSettings.language == null) ? "en_us" : this.gameSettings.language);
        final String s = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) ? "little" : "big";
        playerSnooper.addClientStat("endianness", s);
        playerSnooper.addClientStat("subtitles", this.gameSettings.showSubtitles);
        playerSnooper.addClientStat("touch", this.gameSettings.touchscreen ? "touch" : "mouse");
        playerSnooper.addClientStat("resource_packs", this.resourcePackRepository.getRepositoryEntries().size());
        int i = 0;
        for (final ResourcePackRepository.Entry resourcepackrepository$entry : this.resourcePackRepository.getRepositoryEntries()) {
            playerSnooper.addClientStat("resource_pack[" + i++ + "]", resourcepackrepository$entry.getResourcePackName());
        }
        if (this.integratedServer != null && this.integratedServer.getPlayerUsageSnooper() != null) {
            playerSnooper.addClientStat("snooper_partner", this.integratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }
    
    private String getCurrentAction() {
        if (this.integratedServer != null) {
            return this.integratedServer.getPublic() ? "hosting_lan" : "singleplayer";
        }
        if (this.currentServerData != null) {
            return this.currentServerData.isOnLAN() ? "playing_lan" : "multiplayer";
        }
        return "out_of_game";
    }
    
    @Override
    public void addServerTypeToSnooper(final Snooper playerSnooper) {
        playerSnooper.addStatToSnooper("opengl_version", GlStateManager.glGetString(7938));
        playerSnooper.addStatToSnooper("opengl_vendor", GlStateManager.glGetString(7936));
        playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
        final ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", contextcapabilities.GL_ARB_arrays_of_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", contextcapabilities.GL_ARB_base_instance);
        playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", contextcapabilities.GL_ARB_blend_func_extended);
        playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", contextcapabilities.GL_ARB_clear_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", contextcapabilities.GL_ARB_color_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", contextcapabilities.GL_ARB_compatibility);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", contextcapabilities.GL_ARB_compressed_texture_pixel_storage);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", contextcapabilities.GL_ARB_depth_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", contextcapabilities.GL_ARB_depth_texture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", contextcapabilities.GL_ARB_draw_buffers);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", contextcapabilities.GL_ARB_draw_buffers_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", contextcapabilities.GL_ARB_draw_elements_base_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", contextcapabilities.GL_ARB_draw_indirect);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", contextcapabilities.GL_ARB_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", contextcapabilities.GL_ARB_explicit_attrib_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", contextcapabilities.GL_ARB_explicit_uniform_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", contextcapabilities.GL_ARB_fragment_layer_viewport);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", contextcapabilities.GL_ARB_fragment_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", contextcapabilities.GL_ARB_fragment_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", contextcapabilities.GL_ARB_fragment_program_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", contextcapabilities.GL_ARB_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", contextcapabilities.GL_ARB_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", contextcapabilities.GL_ARB_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", contextcapabilities.GL_ARB_gpu_shader5);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", contextcapabilities.GL_ARB_half_float_pixel);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", contextcapabilities.GL_ARB_half_float_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", contextcapabilities.GL_ARB_instanced_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", contextcapabilities.GL_ARB_map_buffer_alignment);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", contextcapabilities.GL_ARB_map_buffer_range);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", contextcapabilities.GL_ARB_multisample);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", contextcapabilities.GL_ARB_multitexture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", contextcapabilities.GL_ARB_occlusion_query2);
        playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", contextcapabilities.GL_ARB_pixel_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", contextcapabilities.GL_ARB_seamless_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", contextcapabilities.GL_ARB_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", contextcapabilities.GL_ARB_shader_stencil_export);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", contextcapabilities.GL_ARB_shader_texture_lod);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", contextcapabilities.GL_ARB_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", contextcapabilities.GL_ARB_shadow_ambient);
        playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", contextcapabilities.GL_ARB_stencil_texturing);
        playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", contextcapabilities.GL_ARB_sync);
        playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", contextcapabilities.GL_ARB_tessellation_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", contextcapabilities.GL_ARB_texture_border_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", contextcapabilities.GL_ARB_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", contextcapabilities.GL_ARB_texture_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", contextcapabilities.GL_ARB_texture_cube_map_array);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", contextcapabilities.GL_ARB_texture_non_power_of_two);
        playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", contextcapabilities.GL_ARB_uniform_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", contextcapabilities.GL_ARB_vertex_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", contextcapabilities.GL_ARB_vertex_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", contextcapabilities.GL_ARB_vertex_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", contextcapabilities.GL_ARB_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", contextcapabilities.GL_EXT_bindable_uniform);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", contextcapabilities.GL_EXT_blend_equation_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", contextcapabilities.GL_EXT_blend_func_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", contextcapabilities.GL_EXT_blend_minmax);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", contextcapabilities.GL_EXT_blend_subtract);
        playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", contextcapabilities.GL_EXT_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", contextcapabilities.GL_EXT_framebuffer_multisample);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", contextcapabilities.GL_EXT_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", contextcapabilities.GL_EXT_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", contextcapabilities.GL_EXT_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", contextcapabilities.GL_EXT_gpu_program_parameters);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", contextcapabilities.GL_EXT_gpu_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", contextcapabilities.GL_EXT_multi_draw_arrays);
        playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", contextcapabilities.GL_EXT_packed_depth_stencil);
        playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", contextcapabilities.GL_EXT_paletted_texture);
        playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", contextcapabilities.GL_EXT_rescale_normal);
        playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", contextcapabilities.GL_EXT_separate_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", contextcapabilities.GL_EXT_shader_image_load_store);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", contextcapabilities.GL_EXT_shadow_funcs);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", contextcapabilities.GL_EXT_shared_texture_palette);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", contextcapabilities.GL_EXT_stencil_clear_tag);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", contextcapabilities.GL_EXT_stencil_two_side);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", contextcapabilities.GL_EXT_stencil_wrap);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", contextcapabilities.GL_EXT_texture_3d);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", contextcapabilities.GL_EXT_texture_array);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", contextcapabilities.GL_EXT_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", contextcapabilities.GL_EXT_texture_integer);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", contextcapabilities.GL_EXT_texture_lod_bias);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", contextcapabilities.GL_EXT_texture_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", contextcapabilities.GL_EXT_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", contextcapabilities.GL_EXT_vertex_weighting);
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GlStateManager.glGetInteger(35658));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GlStateManager.glGetInteger(35657));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GlStateManager.glGetInteger(34921));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GlStateManager.glGetInteger(35660));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GlStateManager.glGetInteger(34930));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_array_texture_layers]", GlStateManager.glGetInteger(35071));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_max_texture_size", getGLMaximumTextureSize());
        final GameProfile gameprofile = this.session.getProfile();
        if (gameprofile != null && gameprofile.getId() != null) {
            playerSnooper.addStatToSnooper("uuid", Hashing.sha1().hashBytes(gameprofile.getId().toString().getBytes(Charsets.ISO_8859_1)).toString());
        }
    }
    
    public static int getGLMaximumTextureSize() {
        for (int i = 16384; i > 0; i >>= 1) {
            GlStateManager.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, null);
            final int j = GlStateManager.glGetTexLevelParameteri(32868, 0, 4096);
            if (j != 0) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean isSnooperEnabled() {
        return this.gameSettings.snooperEnabled;
    }
    
    public void setServerData(final ServerData serverDataIn) {
        this.currentServerData = serverDataIn;
    }
    
    @Nullable
    public ServerData getCurrentServerData() {
        return this.currentServerData;
    }
    
    public boolean isIntegratedServerRunning() {
        return this.integratedServerIsRunning;
    }
    
    public boolean isSingleplayer() {
        return this.integratedServerIsRunning && this.integratedServer != null;
    }
    
    @Nullable
    public IntegratedServer getIntegratedServer() {
        return this.integratedServer;
    }
    
    public static void stopIntegratedServer() {
        if (Minecraft.instance != null) {
            final IntegratedServer integratedserver = Minecraft.instance.getIntegratedServer();
            if (integratedserver != null) {
                integratedserver.stopServer();
            }
        }
    }
    
    public Snooper getPlayerUsageSnooper() {
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
    
    public PropertyMap getProfileProperties() {
        if (this.profileProperties.isEmpty()) {
            final GameProfile gameprofile = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
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
        return this.resourceManager;
    }
    
    public ResourcePackRepository getResourcePackRepository() {
        return this.resourcePackRepository;
    }
    
    public LanguageManager getLanguageManager() {
        return this.languageManager;
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
        return this.soundHandler;
    }
    
    public MusicTicker.MusicType getAmbientMusicType() {
        if (this.currentScreen instanceof GuiWinGame) {
            return MusicTicker.MusicType.CREDITS;
        }
        if (Minecraft.player == null) {
            return MusicTicker.MusicType.MENU;
        }
        if (Minecraft.player.world.provider instanceof WorldProviderHell) {
            return MusicTicker.MusicType.NETHER;
        }
        if (Minecraft.player.world.provider instanceof WorldProviderEnd) {
            return this.ingameGUI.getBossOverlay().shouldPlayEndBossMusic() ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END;
        }
        return (Minecraft.player.capabilities.isCreativeMode && Minecraft.player.capabilities.allowFlying) ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME;
    }
    
    public void dispatchKeypresses() {
        final int i = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
        if (i != 0 && !Keyboard.isRepeatEvent() && (!(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= getSystemTime() - 20L) && Keyboard.getEventKeyState()) {
            if (i == this.gameSettings.keyBindFullscreen.getKeyCode()) {
                this.toggleFullscreen();
            }
            else if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
                this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.gameDir, this.displayWidth, this.displayHeight, this.framebuffer));
            }
        }
    }
    
    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }
    
    public SkinManager getSkinManager() {
        return this.skinManager;
    }
    
    @Nullable
    public Entity getRenderViewEntity() {
        return this.renderViewEntity;
    }
    
    public void setRenderViewEntity(final Entity viewingEntity) {
        this.renderViewEntity = viewingEntity;
        this.entityRenderer.loadEntityShader(viewingEntity);
    }
    
    public <V> ListenableFuture<V> addScheduledTask(final Callable<V> callableToSchedule) {
        Validate.notNull((Object)callableToSchedule);
        if (this.isCallingFromMinecraftThread()) {
            try {
                return (ListenableFuture<V>)Futures.immediateFuture((Object)callableToSchedule.call());
            }
            catch (Exception exception) {
                return (ListenableFuture<V>)Futures.immediateFailedCheckedFuture(exception);
            }
        }
        final ListenableFutureTask<V> listenablefuturetask = (ListenableFutureTask<V>)ListenableFutureTask.create((Callable)callableToSchedule);
        synchronized (this.scheduledTasks) {
            this.scheduledTasks.add((FutureTask<?>)listenablefuturetask);
            return (ListenableFuture<V>)listenablefuturetask;
        }
    }
    
    @Override
    public ListenableFuture<Object> addScheduledTask(final Runnable runnableToSchedule) {
        Validate.notNull((Object)runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }
    
    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.thread;
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
    
    public <T> ISearchTree<T> getSearchTree(final SearchTreeManager.Key<T> key) {
        return this.searchTreeManager.get(key);
    }
    
    public static int getDebugFPS() {
        return Minecraft.debugFPS;
    }
    
    public FrameTimer getFrameTimer() {
        return this.frameTimer;
    }
    
    public boolean isConnectedToRealms() {
        return this.connectedToRealms;
    }
    
    public void setConnectedToRealms(final boolean isConnected) {
        this.connectedToRealms = isConnected;
    }
    
    public DataFixer getDataFixer() {
        return this.dataFixer;
    }
    
    public float getRenderPartialTicks() {
        return this.timer.renderPartialTicks;
    }
    
    public float getTickLength() {
        return this.timer.elapsedPartialTicks;
    }
    
    public BlockColors getBlockColors() {
        return this.blockColors;
    }
    
    public boolean isReducedDebug() {
        return (Minecraft.player != null && Minecraft.player.hasReducedDebug()) || this.gameSettings.reducedDebugInfo;
    }
    
    public GuiToast getToastGui() {
        return this.toastGui;
    }
    
    public Tutorial getTutorial() {
        return this.tutorial;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        LOCATION_MOJANG_PNG = new ResourceLocation("textures/gui/title/mojang.png");
        IS_RUNNING_ON_MAC = (Util.getOSType() == Util.EnumOS.OSX);
        Minecraft.memoryReserve = new byte[10485760];
        MAC_DISPLAY_MODES = Lists.newArrayList((Object[])new DisplayMode[] { new DisplayMode(2560, 1600), new DisplayMode(2880, 1800) });
        Minecraft.joinedFirst = false;
    }
}
