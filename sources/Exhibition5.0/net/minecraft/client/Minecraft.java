// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client;

import org.apache.logging.log4j.LogManager;
import java.util.HashMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.apache.commons.lang3.Validate;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.client.gui.GuiWinGame;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;
import java.nio.ByteOrder;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.init.Items;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.stats.StatFileWriter;
import java.net.SocketAddress;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldSettings;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import exhibition.management.keybinding.KeyHandler;
import exhibition.Client;
import exhibition.event.impl.EventKeyPress;
import org.lwjgl.input.Keyboard;
import exhibition.event.impl.EventMouse;
import org.lwjgl.input.Mouse;
import java.util.concurrent.Callable;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;
import java.text.DecimalFormat;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.player.EntityPlayer;
import java.util.concurrent.FutureTask;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiGameOver;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventScreenDisplay;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.ScaledResolution;
import java.util.HashSet;
import com.google.common.collect.Sets;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.util.Util;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.PixelFormat;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
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
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Display;
import exhibition.gui.screen.impl.mainmenu.ClientMainMenu;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.AchievementList;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.Sys;
import net.minecraft.util.ReportedException;
import net.minecraft.util.MinecraftError;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import exhibition.util.security.Crypto;
import javax.crypto.spec.SecretKeySpec;
import net.minecraft.init.Bootstrap;
import javax.imageio.ImageIO;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.util.UUID;
import net.minecraft.client.resources.ResourceIndex;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.resources.model.ModelManager;
import java.util.Queue;
import net.minecraft.client.resources.SkinManager;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.profiler.Profiler;
import net.minecraft.network.NetworkManager;
import net.minecraft.world.storage.ISaveFormat;
import java.net.Proxy;
import net.minecraft.util.MouseHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.Session;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.util.Timer;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.multiplayer.ServerData;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.util.IThreadListener;

public class Minecraft implements IThreadListener, IPlayerUsage
{
    private static final Logger logger;
    private static final ResourceLocation locationMojangPng;
    public static final boolean isRunningOnMac;
    public static byte[] memoryReserve;
    private static final List macDisplayModes;
    private final File fileResourcepacks;
    private final PropertyMap twitchDetails;
    private ServerData currentServerData;
    private TextureManager renderEngine;
    private static Minecraft theMinecraft;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private boolean field_175619_R;
    private boolean hasCrashed;
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;
    public Timer timer;
    private PlayerUsageSnooper usageSnooper;
    public WorldClient theWorld;
    public RenderGlobal renderGlobal;
    private RenderManager renderManager;
    private RenderItem renderItem;
    private ItemRenderer itemRenderer;
    public EntityPlayerSP thePlayer;
    private Entity field_175622_Z;
    public Entity pointedEntity;
    public EffectRenderer effectRenderer;
    public Session session;
    private boolean isGamePaused;
    public FontRenderer fontRendererObj;
    public FontRenderer standardGalacticFontRenderer;
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;
    private int leftClickCounter;
    private int tempDisplayWidth;
    private int tempDisplayHeight;
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
    public final Proxy proxy;
    private ISaveFormat saveLoader;
    public static int debugFPS;
    public int rightClickDelayTimer;
    private String serverName;
    private int serverPort;
    public boolean inGameHasFocus;
    long systemTime;
    private int joinPlayerCounter;
    private final boolean jvm64bit;
    private final boolean isDemo;
    private NetworkManager myNetworkManager;
    private boolean integratedServerIsRunning;
    public final Profiler mcProfiler;
    private long debugCrashKeyPressTime;
    private IReloadableResourceManager mcResourceManager;
    private final IMetadataSerializer metadataSerializer_;
    private final List defaultResourcePacks;
    private final DefaultResourcePack mcDefaultResourcePack;
    private ResourcePackRepository mcResourcePackRepository;
    private LanguageManager mcLanguageManager;
    private IStream stream;
    private Framebuffer framebufferMc;
    private TextureMap textureMapBlocks;
    private SoundHandler mcSoundHandler;
    private MusicTicker mcMusicTicker;
    private ResourceLocation mojangLogo;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue scheduledTasks;
    private long field_175615_aJ;
    private final Thread mcThread;
    private ModelManager modelManager;
    private BlockRendererDispatcher field_175618_aM;
    volatile boolean running;
    public String debug;
    public boolean field_175613_B;
    public boolean field_175614_C;
    public boolean field_175611_D;
    public boolean field_175612_E;
    long debugUpdateTime;
    int fpsCounter;
    long prevFrameTime;
    private String debugProfilerName;
    private static final String __OBFID = "CL_00000631";
    
    public Minecraft(final GameConfiguration config) {
        this.field_175619_R = true;
        this.timer = new Timer(20.0f);
        this.usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
        this.systemTime = getSystemTime();
        this.mcProfiler = new Profiler();
        this.debugCrashKeyPressTime = -1L;
        this.metadataSerializer_ = new IMetadataSerializer();
        this.defaultResourcePacks = Lists.newArrayList();
        this.scheduledTasks = Queues.newArrayDeque();
        this.field_175615_aJ = 0L;
        this.mcThread = Thread.currentThread();
        this.running = true;
        this.debug = "";
        this.field_175613_B = false;
        this.field_175614_C = false;
        this.field_175611_D = false;
        this.field_175612_E = true;
        this.debugUpdateTime = getSystemTime();
        this.prevFrameTime = -1L;
        this.debugProfilerName = "root";
        Minecraft.theMinecraft = this;
        this.mcDataDir = config.field_178744_c.field_178760_a;
        this.fileAssets = config.field_178744_c.field_178759_c;
        this.fileResourcepacks = config.field_178744_c.field_178758_b;
        this.launchedVersion = config.field_178741_d.field_178755_b;
        this.twitchDetails = config.field_178745_a.field_178750_b;
        this.mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(config.field_178744_c.field_178759_c, config.field_178744_c.field_178757_d).func_152782_a());
        this.proxy = ((config.field_178745_a.field_178751_c == null) ? Proxy.NO_PROXY : config.field_178745_a.field_178751_c);
        this.sessionService = new YggdrasilAuthenticationService(config.field_178745_a.field_178751_c, UUID.randomUUID().toString()).createMinecraftSessionService();
        this.session = config.field_178745_a.field_178752_a;
        Minecraft.logger.info("Setting user: " + this.session.getUsername());
        Minecraft.logger.info("(Session ID is " + this.session.getSessionID() + ")");
        this.isDemo = config.field_178741_d.field_178756_a;
        this.displayWidth = ((config.field_178743_b.field_178764_a > 0) ? config.field_178743_b.field_178764_a : 1);
        this.displayHeight = ((config.field_178743_b.field_178762_b > 0) ? config.field_178743_b.field_178762_b : 1);
        this.tempDisplayWidth = config.field_178743_b.field_178764_a;
        this.tempDisplayHeight = config.field_178743_b.field_178762_b;
        this.fullscreen = config.field_178743_b.field_178763_c;
        this.jvm64bit = isJvm64bit();
        this.theIntegratedServer = new IntegratedServer(this);
        if (config.field_178742_e.field_178754_a != null) {
            this.serverName = config.field_178742_e.field_178754_a;
            this.serverPort = config.field_178742_e.field_178753_b;
        }
        ImageIO.setUseCache(false);
        Bootstrap.register();
    }
    
    private SecretKeySpec getSecret() {
        final byte[] secret = Crypto.getUserKey(16);
        return new SecretKeySpec(secret, 0, secret.length, "AES");
    }
    
    public static String getHwid() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s = "";
        final String main = (String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim();
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] md2 = md.digest(bytes);
        int i = 0;
        byte[] array;
        for (int length = (array = md2).length, j = 0; j < length; ++j) {
            final byte b = array[j];
            s = String.valueOf(s) + Integer.toHexString((b & 0xFF) | 0x100).substring(0, 3);
            if (i != md2.length - 1) {
                s = String.valueOf(s) + "-";
            }
            ++i;
        }
        return s;
    }
    
    public void run() {
        this.running = true;
        try {
            this.startGame();
        }
        catch (Throwable var16) {
            this.xDD();
        }
        try {
            while (this.running) {
                if (this.hasCrashed) {
                    if (this.crashReporter != null) {
                        this.displayCrashReport(this.crashReporter);
                        return;
                    }
                }
                try {
                    this.runGameLoop();
                }
                catch (OutOfMemoryError var17) {
                    this.freeMemory();
                    this.displayGuiScreen(new GuiMemoryErrorScreen());
                    System.gc();
                }
            }
        }
        catch (MinecraftError minecraftError) {}
        catch (ReportedException var13) {
            this.addGraphicsAndWorldToCrashReport(var13.getCrashReport());
            this.freeMemory();
            Minecraft.logger.fatal("Reported exception thrown!", (Throwable)var13);
            this.displayCrashReport(var13.getCrashReport());
        }
        catch (Throwable var15) {
            final CrashReport var14 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", var15));
            this.freeMemory();
            Minecraft.logger.fatal("Unreported exception thrown!", var15);
            this.displayCrashReport(var14);
        }
        finally {
            this.shutdownMinecraftApplet();
        }
    }
    
    protected void startGame() throws Exception {
        this.gameSettings = new GameSettings(this, this.mcDataDir);
        this.defaultResourcePacks.add(this.mcDefaultResourcePack);
        this.startTimerHackThread();
        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }
        Minecraft.logger.info("LWJGL Version: " + Sys.getVersion());
        this.func_175594_ao();
        this.func_175605_an();
        this.func_175609_am();
        OpenGlHelper.initializeTextures();
        (this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true)).setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.func_175608_ak();
        this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings);
        this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
        this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.renderEngine);
        this.func_180510_a(this.renderEngine);
        this.func_175595_al();
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
        this.resetSounds();
        this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(this);
        this.fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
        if (this.gameSettings.language != null) {
            this.fontRendererObj.setUnicodeFlag(this.isUnicode());
            this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
        }
        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.mcResourceManager.registerReloadListener(this.fontRendererObj);
        this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat() {
            private static final String __OBFID = "CL_00000632";
            
            @Override
            public String formatString(final String p_74535_1_) {
                try {
                    return String.format(p_74535_1_, GameSettings.getKeyDisplayString(Minecraft.this.gameSettings.keyBindInventory.getKeyCode()));
                }
                catch (Exception var3) {
                    return "Error: " + var3.getLocalizedMessage();
                }
            }
        });
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTextures();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.cullFace(1029);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        this.checkGLError("Startup");
        (this.textureMapBlocks = new TextureMap("textures")).setMipmapLevels(this.gameSettings.mipmapLevels);
        this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        this.textureMapBlocks.func_174937_a(false, this.gameSettings.mipmapLevels > 0);
        this.modelManager = new ModelManager(this.textureMapBlocks);
        this.mcResourceManager.registerReloadListener(this.modelManager);
        this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        this.itemRenderer = new ItemRenderer(this);
        this.mcResourceManager.registerReloadListener(this.renderItem);
        this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.entityRenderer);
        this.field_175618_aM = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.field_175618_aM);
        this.renderGlobal = new RenderGlobal(this);
        this.mcResourceManager.registerReloadListener(this.renderGlobal);
        this.guiAchievement = new GuiAchievement(this);
        GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);
        this.displayGuiScreen(new ClientMainMenu());
        this.renderEngine.deleteTexture(this.mojangLogo);
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(this);
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
        this.renderGlobal.func_174966_b();
    }
    
    public void resetSounds() {
        this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
    }
    
    private void func_175608_ak() {
        this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }
    
    private void func_175595_al() {
        try {
            this.stream = new TwitchStream(this, (Property)Iterables.getFirst((Iterable)this.twitchDetails.get((Object)"twitch_access_token"), (Object)null));
        }
        catch (Throwable var2) {
            this.stream = new NullStream(var2);
            Minecraft.logger.error("Couldn't initialize twitch stream");
        }
    }
    
    private void func_175609_am() throws LWJGLException {
        Display.setResizable(true);
        Display.setTitle("Minecraft 1.8");
        try {
            Display.create(new PixelFormat().withDepthBits(24));
        }
        catch (LWJGLException var4) {
            Minecraft.logger.error("Couldn't set pixel format", (Throwable)var4);
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
    
    private void func_175605_an() throws LWJGLException {
        if (this.fullscreen) {
            Display.setFullscreen(true);
            final DisplayMode var1 = Display.getDisplayMode();
            this.displayWidth = Math.max(1, var1.getWidth());
            this.displayHeight = Math.max(1, var1.getHeight());
        }
        else {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }
    }
    
    private void func_175594_ao() {
        final Util.EnumOS var1 = Util.getOSType();
        if (var1 != Util.EnumOS.OSX) {
            InputStream var2 = null;
            InputStream var3 = null;
            try {
                var2 = this.mcDefaultResourcePack.func_152780_c(new ResourceLocation("icons/icon_16x16.png"));
                var3 = this.mcDefaultResourcePack.func_152780_c(new ResourceLocation("icons/icon_32x32.png"));
                if (var2 != null && var3 != null) {
                    Display.setIcon(new ByteBuffer[] { this.readImageToBuffer(var2), this.readImageToBuffer(var3) });
                }
            }
            catch (IOException var4) {
                Minecraft.logger.error("Couldn't set icon", (Throwable)var4);
            }
            finally {
                IOUtils.closeQuietly(var2);
                IOUtils.closeQuietly(var3);
            }
        }
    }
    
    private static boolean isJvm64bit() {
        final String[] var2;
        final String[] var0 = var2 = new String[] { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        for (int var3 = var0.length, var4 = 0; var4 < var3; ++var4) {
            final String var5 = var2[var4];
            final String var6 = System.getProperty(var5);
            if (var6 != null && var6.contains("64")) {
                return true;
            }
        }
        return false;
    }
    
    public Framebuffer getFramebuffer() {
        return this.framebufferMc;
    }
    
    public String func_175600_c() {
        return this.launchedVersion;
    }
    
    private void startTimerHackThread() {
        final Thread var1 = new Thread("Timer hack thread") {
            private static final String __OBFID = "CL_00000639";
            
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
        var1.setDaemon(true);
        var1.start();
    }
    
    public void crashed(final CrashReport crash) {
        this.hasCrashed = true;
        this.crashReporter = crash;
    }
    
    public void displayCrashReport(final CrashReport crashReportIn) {
        final File var2 = new File(getMinecraft().mcDataDir, "crash-reports");
        final File var3 = new File(var2, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
        Bootstrap.func_179870_a(crashReportIn.getCompleteReport());
        if (crashReportIn.getFile() != null) {
            Bootstrap.func_179870_a("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            System.exit(-1);
        }
        else if (crashReportIn.saveToFile(var3)) {
            Bootstrap.func_179870_a("#@!@# Game crashed! Crash report saved to: #@!@# " + var3.getAbsolutePath());
            System.exit(-1);
        }
        else {
            Bootstrap.func_179870_a("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }
    
    public boolean isUnicode() {
        return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
    }
    
    public void refreshResources() {
        final ArrayList var1 = Lists.newArrayList((Iterable)this.defaultResourcePacks);
        for (final ResourcePackRepository.Entry var3 : this.mcResourcePackRepository.getRepositoryEntries()) {
            var1.add(var3.getResourcePack());
        }
        if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
            var1.add(this.mcResourcePackRepository.getResourcePackInstance());
        }
        try {
            this.mcResourceManager.reloadResources(var1);
        }
        catch (RuntimeException var4) {
            Minecraft.logger.info("Caught error stitching, removing all assigned resourcepacks", (Throwable)var4);
            var1.clear();
            var1.addAll(this.defaultResourcePacks);
            this.mcResourcePackRepository.func_148527_a(Collections.emptyList());
            this.mcResourceManager.reloadResources(var1);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.saveOptions();
        }
        this.mcLanguageManager.parseLanguageMetadata(var1);
        if (this.renderGlobal != null) {
            this.renderGlobal.loadRenderers();
        }
    }
    
    private ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        final BufferedImage var2 = ImageIO.read(imageStream);
        final int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), null, 0, var2.getWidth());
        final ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);
        final int[] var5 = var3;
        for (int var6 = var3.length, var7 = 0; var7 < var6; ++var7) {
            final int var8 = var5[var7];
            var4.putInt(var8 << 8 | (var8 >> 24 & 0xFF));
        }
        var4.flip();
        return var4;
    }
    
    private void updateDisplayMode() throws LWJGLException {
        final HashSet var1 = Sets.newHashSet();
        Collections.addAll(var1, Display.getAvailableDisplayModes());
        DisplayMode var2 = Display.getDesktopDisplayMode();
        if (!var1.contains(var2) && Util.getOSType() == Util.EnumOS.OSX) {
            for (final DisplayMode var4 : Minecraft.macDisplayModes) {
                boolean var5 = true;
                for (final DisplayMode var7 : var1) {
                    if (var7.getBitsPerPixel() == 32 && var7.getWidth() == var4.getWidth() && var7.getHeight() == var4.getHeight()) {
                        var5 = false;
                        break;
                    }
                }
                if (!var5) {
                    for (final DisplayMode var7 : var1) {
                        if (var7.getBitsPerPixel() == 32 && var7.getWidth() == var4.getWidth() / 2 && var7.getHeight() == var4.getHeight() / 2) {
                            var2 = var7;
                            break;
                        }
                    }
                }
            }
        }
        Display.setDisplayMode(var2);
        this.displayWidth = var2.getWidth();
        this.displayHeight = var2.getHeight();
    }
    
    protected void func_180510_a(final TextureManager p_180510_1_) {
        final ScaledResolution var2 = new ScaledResolution(this, this.displayWidth, this.displayHeight);
        final int var3 = var2.getScaleFactor();
        final Framebuffer var4 = new Framebuffer(var2.getScaledWidth() * var3, var2.getScaledHeight() * var3, true);
        var4.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, var2.getScaledWidth(), var2.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTextures();
        InputStream var5 = null;
        try {
            var5 = this.mcDefaultResourcePack.getInputStream(Minecraft.locationMojangPng);
            p_180510_1_.bindTexture(this.mojangLogo = p_180510_1_.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(var5))));
        }
        catch (IOException var6) {
            Minecraft.logger.error("Unable to load logo: " + Minecraft.locationMojangPng, (Throwable)var6);
        }
        finally {
            IOUtils.closeQuietly(var5);
        }
        final Tessellator var7 = Tessellator.getInstance();
        final WorldRenderer var8 = var7.getWorldRenderer();
        var8.startDrawingQuads();
        var8.func_178991_c(16777215);
        var8.addVertexWithUV(0.0, this.displayHeight, 0.0, 0.0, 0.0);
        var8.addVertexWithUV(this.displayWidth, this.displayHeight, 0.0, 0.0, 0.0);
        var8.addVertexWithUV(this.displayWidth, 0.0, 0.0, 0.0, 0.0);
        var8.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
        var7.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        var8.func_178991_c(16777215);
        final short var9 = 256;
        final short var10 = 256;
        this.scaledTessellator((var2.getScaledWidth() - var9) / 2, (var2.getScaledHeight() - var10) / 2, 0, 0, var9, var10);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        var4.unbindFramebuffer();
        var4.framebufferRender(var2.getScaledWidth() * var3, var2.getScaledHeight() * var3);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        this.func_175601_h();
    }
    
    public void scaledTessellator(final int width, final int height, final int width2, final int height2, final int stdTextureWidth, final int stdTextureHeight) {
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final WorldRenderer var9 = Tessellator.getInstance().getWorldRenderer();
        var9.startDrawingQuads();
        var9.addVertexWithUV(width + 0, height + stdTextureHeight, 0.0, (width2 + 0) * var7, (height2 + stdTextureHeight) * var8);
        var9.addVertexWithUV(width + stdTextureWidth, height + stdTextureHeight, 0.0, (width2 + stdTextureWidth) * var7, (height2 + stdTextureHeight) * var8);
        var9.addVertexWithUV(width + stdTextureWidth, height + 0, 0.0, (width2 + stdTextureWidth) * var7, (height2 + 0) * var8);
        var9.addVertexWithUV(width + 0, height + 0, 0.0, (width2 + 0) * var7, (height2 + 0) * var8);
        Tessellator.getInstance().draw();
    }
    
    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }
    
    public void displayGuiScreen(GuiScreen guiScreenIn) {
        final EventScreenDisplay event = (EventScreenDisplay)EventSystem.getInstance(EventScreenDisplay.class);
        event.fire(guiScreenIn);
        guiScreenIn = event.getGuiScreen();
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }
        if (guiScreenIn == null && this.theWorld == null) {
            guiScreenIn = new ClientMainMenu();
        }
        else if (guiScreenIn == null && this.thePlayer.getHealth() <= 0.0f) {
            guiScreenIn = new GuiGameOver();
        }
        final boolean mainMenu = guiScreenIn instanceof GuiMainMenu || guiScreenIn instanceof ClientMainMenu;
        if (mainMenu) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages();
        }
        if ((this.currentScreen = guiScreenIn) != null) {
            this.setIngameNotInFocus();
            final ScaledResolution var2 = new ScaledResolution(this, this.displayWidth, this.displayHeight);
            final int var3 = var2.getScaledWidth();
            final int var4 = var2.getScaledHeight();
            guiScreenIn.setWorldAndResolution(this, var3, var4);
            this.skipRenderWorld = false;
        }
        else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }
    
    private void checkGLError(final String message) {
        if (this.field_175619_R) {
            final int var2 = GL11.glGetError();
            if (var2 != 0) {
                final String var3 = GLU.gluErrorString(var2);
                Minecraft.logger.error("########## GL ERROR ##########");
                Minecraft.logger.error("@ " + message);
                Minecraft.logger.error(var2 + ": " + var3);
            }
        }
    }
    
    public void shutdownMinecraftApplet() {
        try {
            this.stream.shutdownStream();
            Minecraft.logger.info("Stopping!");
            try {
                this.loadWorld(null);
            }
            catch (Throwable t) {}
            this.mcSoundHandler.unloadSounds();
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
        this.mcProfiler.startSection("root");
        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }
        if (this.isGamePaused && this.theWorld != null) {
            final float var1 = this.timer.renderPartialTicks;
            this.timer.updateTimer();
            this.timer.renderPartialTicks = var1;
        }
        else {
            this.timer.updateTimer();
        }
        this.mcProfiler.startSection("scheduledExecutables");
        final Queue var2 = this.scheduledTasks;
        synchronized (this.scheduledTasks) {
            while (!this.scheduledTasks.isEmpty()) {
                this.scheduledTasks.poll().run();
            }
        }
        this.mcProfiler.endSection();
        final long var3 = System.nanoTime();
        this.mcProfiler.startSection("tick");
        for (int var4 = 0; var4 < this.timer.elapsedTicks; ++var4) {
            this.runTick();
        }
        this.mcProfiler.endStartSection("preRenderErrors");
        final long var5 = System.nanoTime() - var3;
        this.checkGLError("Pre render");
        this.mcProfiler.endStartSection("sound");
        this.mcSoundHandler.setListener(this.thePlayer, this.timer.renderPartialTicks);
        this.mcProfiler.endSection();
        this.mcProfiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        this.framebufferMc.bindFramebuffer(true);
        this.mcProfiler.startSection("display");
        GlStateManager.enableTextures();
        if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
            this.gameSettings.thirdPersonView = 0;
        }
        this.mcProfiler.endSection();
        if (!this.skipRenderWorld) {
            this.mcProfiler.endStartSection("gameRenderer");
            this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
            if (!this.mcProfiler.profilingEnabled) {
                this.mcProfiler.clearProfiling();
            }
            this.mcProfiler.profilingEnabled = true;
            this.displayDebugInfo(var5);
        }
        else {
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
        this.entityRenderer.func_152430_c(this.timer.renderPartialTicks);
        GlStateManager.popMatrix();
        this.mcProfiler.startSection("root");
        this.func_175601_h();
        Thread.yield();
        this.mcProfiler.startSection("stream");
        this.mcProfiler.startSection("update");
        this.stream.func_152935_j();
        this.mcProfiler.endStartSection("submit");
        this.stream.func_152922_k();
        this.mcProfiler.endSection();
        this.mcProfiler.endSection();
        this.checkGLError("Post render");
        ++this.fpsCounter;
        this.isGamePaused = (this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic());
        while (getSystemTime() >= this.debugUpdateTime + 1000L) {
            Minecraft.debugFPS = this.fpsCounter;
            this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", Minecraft.debugFPS, RenderChunk.field_178592_a, (RenderChunk.field_178592_a != 1) ? "s" : "", (this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) ? "inf" : this.gameSettings.limitFramerate, this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds ? " clouds" : "", OpenGlHelper.func_176075_f() ? " vbo" : "");
            RenderChunk.field_178592_a = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.usageSnooper.addMemoryStatsToSnooper();
            if (!this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.startSnooper();
            }
        }
        if (this.isFramerateLimitBelowMax()) {
            this.mcProfiler.startSection("fpslimit_wait");
            Display.sync(this.getLimitFramerate());
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
    }
    
    public void func_175601_h() {
        this.mcProfiler.startSection("display_update");
        Display.update();
        this.mcProfiler.endSection();
        this.func_175604_i();
    }
    
    protected void func_175604_i() {
        if (!this.fullscreen && Display.wasResized()) {
            final int var1 = this.displayWidth;
            final int var2 = this.displayHeight;
            this.displayWidth = Display.getWidth();
            this.displayHeight = Display.getHeight();
            if (this.displayWidth != var1 || this.displayHeight != var2) {
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
        return (this.theWorld == null && this.currentScreen != null) ? 30 : this.gameSettings.limitFramerate;
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
        final List var2 = this.mcProfiler.getProfilingData(this.debugProfilerName);
        if (var2 != null && !var2.isEmpty()) {
            final Profiler.Result var3 = var2.remove(0);
            if (keyCount == 0) {
                if (var3.field_76331_c.length() > 0) {
                    final int var4 = this.debugProfilerName.lastIndexOf(".");
                    if (var4 >= 0) {
                        this.debugProfilerName = this.debugProfilerName.substring(0, var4);
                    }
                }
            }
            else if (--keyCount < var2.size() && !var2.get(keyCount).field_76331_c.equals("unspecified")) {
                if (this.debugProfilerName.length() > 0) {
                    this.debugProfilerName += ".";
                }
                this.debugProfilerName += var2.get(keyCount).field_76331_c;
            }
        }
    }
    
    private void displayDebugInfo(final long elapsedTicksTime) {
        if (this.mcProfiler.profilingEnabled) {
            final List var3 = this.mcProfiler.getProfilingData(this.debugProfilerName);
            final Profiler.Result var4 = var3.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, this.displayWidth, this.displayHeight, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GL11.glLineWidth(1.0f);
            GlStateManager.disableTextures();
            final Tessellator var5 = Tessellator.getInstance();
            final WorldRenderer var6 = var5.getWorldRenderer();
            final short var7 = 160;
            final int var8 = this.displayWidth - var7 - 10;
            final int var9 = this.displayHeight - var7 * 2;
            GlStateManager.enableBlend();
            var6.startDrawingQuads();
            var6.drawColor(0, 200);
            var6.addVertex(var8 - var7 * 1.1f, var9 - var7 * 0.6f - 16.0f, 0.0);
            var6.addVertex(var8 - var7 * 1.1f, var9 + var7 * 2, 0.0);
            var6.addVertex(var8 + var7 * 1.1f, var9 + var7 * 2, 0.0);
            var6.addVertex(var8 + var7 * 1.1f, var9 - var7 * 0.6f - 16.0f, 0.0);
            var5.draw();
            GlStateManager.disableBlend();
            double var10 = 0.0;
            for (int var11 = 0; var11 < var3.size(); ++var11) {
                final Profiler.Result var12 = var3.get(var11);
                final int var13 = MathHelper.floor_double(var12.field_76332_a / 4.0) + 1;
                var6.startDrawing(6);
                var6.func_178991_c(var12.func_76329_a());
                var6.addVertex(var8, var9, 0.0);
                for (int var14 = var13; var14 >= 0; --var14) {
                    final float var15 = (float)((var10 + var12.field_76332_a * var14 / var13) * 3.141592653589793 * 2.0 / 100.0);
                    final float var16 = MathHelper.sin(var15) * var7;
                    final float var17 = MathHelper.cos(var15) * var7 * 0.5f;
                    var6.addVertex(var8 + var16, var9 - var17, 0.0);
                }
                var5.draw();
                var6.startDrawing(5);
                var6.func_178991_c((var12.func_76329_a() & 0xFEFEFE) >> 1);
                for (int var14 = var13; var14 >= 0; --var14) {
                    final float var15 = (float)((var10 + var12.field_76332_a * var14 / var13) * 3.141592653589793 * 2.0 / 100.0);
                    final float var16 = MathHelper.sin(var15) * var7;
                    final float var17 = MathHelper.cos(var15) * var7 * 0.5f;
                    var6.addVertex(var8 + var16, var9 - var17, 0.0);
                    var6.addVertex(var8 + var16, var9 - var17 + 10.0f, 0.0);
                }
                var5.draw();
                var10 += var12.field_76332_a;
            }
            final DecimalFormat var18 = new DecimalFormat("##0.00");
            GlStateManager.enableTextures();
            String var19 = "";
            if (!var4.field_76331_c.equals("unspecified")) {
                var19 += "[0] ";
            }
            if (var4.field_76331_c.length() == 0) {
                var19 += "ROOT ";
            }
            else {
                var19 = var19 + var4.field_76331_c + " ";
            }
            final int var13 = 16777215;
            this.fontRendererObj.drawStringWithShadow(var19, var8 - var7, var9 - var7 / 2 - 16, var13);
            this.fontRendererObj.drawStringWithShadow(var19 = var18.format(var4.field_76330_b) + "%", var8 + var7 - this.fontRendererObj.getStringWidth(var19), var9 - var7 / 2 - 16, var13);
            for (int var20 = 0; var20 < var3.size(); ++var20) {
                final Profiler.Result var21 = var3.get(var20);
                String var22 = "";
                if (var21.field_76331_c.equals("unspecified")) {
                    var22 += "[?] ";
                }
                else {
                    var22 = var22 + "[" + (var20 + 1) + "] ";
                }
                var22 += var21.field_76331_c;
                this.fontRendererObj.drawStringWithShadow(var22, var8 - var7, var9 + var7 / 2 + var20 * 8 + 20, var21.func_76329_a());
                this.fontRendererObj.drawStringWithShadow(var22 = var18.format(var21.field_76332_a) + "%", var8 + var7 - 50 - this.fontRendererObj.getStringWidth(var22), var9 + var7 / 2 + var20 * 8 + 20, var21.func_76329_a());
                this.fontRendererObj.drawStringWithShadow(var22 = var18.format(var21.field_76330_b) + "%", var8 + var7 - this.fontRendererObj.getStringWidth(var22), var9 + var7 / 2 + var20 * 8 + 20, var21.func_76329_a());
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
            this.displayGuiScreen(new GuiIngameMenu());
            if (this.isSingleplayer() && !this.theIntegratedServer.getPublic()) {
                this.mcSoundHandler.pauseSounds();
            }
        }
    }
    
    private void xDD() {
        this.running = false;
        this.fuckOff();
    }
    
    protected void fuckOff() {
        this.shutdownMinecraftApplet();
    }
    
    private void kys() {
        this.hasCrashed = false;
        this.xDD();
    }
    
    private void sendClickBlockToController(final boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }
        if (this.leftClickCounter <= 0) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos pos = this.objectMouseOver.getBlockPos();
                if (this.theWorld.getBlockState(pos).getBlock().getMaterial() != Material.air && this.playerController.breakBlock(pos, this.objectMouseOver.facing)) {
                    this.effectRenderer.func_180532_a(pos, this.objectMouseOver.facing);
                    this.thePlayer.swingItem();
                }
            }
            else {
                this.playerController.resetBlockRemoving();
            }
        }
    }
    
    public void clickMouse() {
        if (this.leftClickCounter <= 0) {
            this.thePlayer.swingItem();
            if (this.objectMouseOver == null) {
                Minecraft.logger.error("Null returned as 'hitResult', this shouldn't happen!");
                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            }
            else {
                switch (SwitchEnumMinecartType.field_152390_a[this.objectMouseOver.typeOfHit.ordinal()]) {
                    case 1: {
                        this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
                        return;
                    }
                    case 2: {
                        final BlockPos var1 = this.objectMouseOver.getBlockPos();
                        if (this.theWorld.getBlockState(var1).getBlock().getMaterial() != Material.air) {
                            this.playerController.func_180511_b(var1, this.objectMouseOver.facing);
                            return;
                        }
                        break;
                    }
                }
                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            }
        }
    }
    
    private void rightClickMouse() {
        this.rightClickDelayTimer = 4;
        boolean var1 = true;
        final ItemStack var2 = this.thePlayer.inventory.getCurrentItem();
        if (this.objectMouseOver == null) {
            Minecraft.logger.warn("Null returned as 'hitResult', this shouldn't happen!");
        }
        else {
            switch (SwitchEnumMinecartType.field_152390_a[this.objectMouseOver.typeOfHit.ordinal()]) {
                case 1: {
                    if (this.playerController.func_178894_a(this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver)) {
                        var1 = false;
                        break;
                    }
                    if (this.playerController.interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit)) {
                        var1 = false;
                        break;
                    }
                    break;
                }
                case 2: {
                    final BlockPos var3 = this.objectMouseOver.getBlockPos();
                    if (this.theWorld.getBlockState(var3).getBlock().getMaterial() == Material.air) {
                        break;
                    }
                    final int var4 = (var2 != null) ? var2.stackSize : 0;
                    if (this.playerController.func_178890_a(this.thePlayer, this.theWorld, var2, var3, this.objectMouseOver.facing, this.objectMouseOver.hitVec)) {
                        var1 = false;
                        this.thePlayer.swingItem();
                    }
                    if (var2 == null) {
                        return;
                    }
                    if (var2.stackSize == 0) {
                        this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                        break;
                    }
                    if (var2.stackSize != var4 || this.playerController.isInCreativeMode()) {
                        this.entityRenderer.itemRenderer.resetEquippedProgress();
                        break;
                    }
                    break;
                }
            }
        }
        if (var1) {
            final ItemStack var5 = this.thePlayer.inventory.getCurrentItem();
            if (var5 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, var5)) {
                this.entityRenderer.itemRenderer.resetEquippedProgress2();
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
            this.func_175601_h();
        }
        catch (Exception var2) {
            Minecraft.logger.error("Couldn't toggle fullscreen", (Throwable)var2);
        }
    }
    
    private void resize(final int width, final int height) {
        this.displayWidth = Math.max(1, width);
        this.displayHeight = Math.max(1, height);
        if (this.currentScreen != null) {
            final ScaledResolution var3 = new ScaledResolution(this, width, height);
            this.currentScreen.func_175273_b(this, var3.getScaledWidth(), var3.getScaledHeight());
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
    
    public void runTick() throws IOException {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }
        this.mcProfiler.startSection("gui");
        if (!this.isGamePaused) {
            this.ingameGUI.updateTick();
        }
        this.mcProfiler.endSection();
        this.entityRenderer.getMouseOver(1.0f);
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
            }
            else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        }
        else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
            this.displayGuiScreen(null);
        }
        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
        }
        if (this.currentScreen != null) {
            try {
                this.currentScreen.handleInput();
            }
            catch (Throwable var3) {
                final CrashReport var2 = CrashReport.makeCrashReport(var3, "Updating screen events");
                final CrashReportCategory var4 = var2.makeCategory("Affected screen");
                var4.addCrashSectionCallable("Screen name", new Callable() {
                    private static final String __OBFID = "CL_00000640";
                    
                    @Override
                    public String call() {
                        return Minecraft.this.currentScreen.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(var2);
            }
            if (this.currentScreen != null) {
                try {
                    this.currentScreen.updateScreen();
                }
                catch (Throwable var5) {
                    final CrashReport var2 = CrashReport.makeCrashReport(var5, "Ticking screen");
                    final CrashReportCategory var4 = var2.makeCategory("Affected screen");
                    var4.addCrashSectionCallable("Screen name", new Callable() {
                        private static final String __OBFID = "CL_00000642";
                        
                        @Override
                        public String call() {
                            return Minecraft.this.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    throw new ReportedException(var2);
                }
            }
        }
        if (this.currentScreen == null || this.currentScreen.allowUserInput) {
            this.mcProfiler.endStartSection("mouse");
            while (Mouse.next()) {
                int mouseID = Mouse.getEventButton();
                final EventMouse eventMouse = (EventMouse)EventSystem.getInstance(EventMouse.class);
                eventMouse.fire(mouseID, Mouse.getEventButtonState());
                mouseID = eventMouse.getButtonID();
                KeyBinding.setKeyBindState(mouseID - 100, Mouse.getEventButtonState());
                if (Mouse.getEventButtonState()) {
                    if (this.thePlayer.func_175149_v() && mouseID == 2) {
                        this.ingameGUI.func_175187_g().func_175261_b();
                    }
                    else {
                        KeyBinding.onTick(mouseID - 100);
                    }
                }
                final long var6 = getSystemTime() - this.systemTime;
                if (var6 <= 200L) {
                    int var7 = Mouse.getEventDWheel();
                    if (var7 != 0) {
                        if (this.thePlayer.func_175149_v()) {
                            var7 = ((var7 < 0) ? -1 : 1);
                            if (this.ingameGUI.func_175187_g().func_175262_a()) {
                                this.ingameGUI.func_175187_g().func_175259_b(-var7);
                            }
                            else {
                                final float var8 = MathHelper.clamp_float(this.thePlayer.capabilities.getFlySpeed() + var7 * 0.005f, 0.0f, 0.2f);
                                this.thePlayer.capabilities.setFlySpeed(var8);
                            }
                        }
                        else {
                            this.thePlayer.inventory.changeCurrentItem(var7);
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
            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }
            this.mcProfiler.endStartSection("keyboard");
            while (Keyboard.next()) {
                int mouseID = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
                KeyBinding.setKeyBindState(mouseID, Keyboard.getEventKeyState());
                if (Keyboard.getEventKeyState()) {
                    final EventKeyPress eventKey = (EventKeyPress)EventSystem.getInstance(EventKeyPress.class);
                    eventKey.fire(mouseID);
                    mouseID = eventKey.getKey();
                    if (mouseID == 54 && this.currentScreen == null) {
                        this.displayGuiScreen(Client.getClickGui());
                    }
                    KeyBinding.onTick(mouseID);
                }
                if (this.debugCrashKeyPressTime > 0L) {
                    if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
                        throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                    }
                    if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                        this.debugCrashKeyPressTime = -1L;
                    }
                }
                else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                    this.debugCrashKeyPressTime = getSystemTime();
                }
                this.dispatchKeypresses();
                final boolean isInGui = this.currentScreen != null;
                KeyHandler.update(isInGui);
                if (Keyboard.getEventKeyState()) {
                    if (mouseID == 62 && this.entityRenderer != null) {
                        this.entityRenderer.func_175071_c();
                    }
                    if (isInGui) {
                        this.currentScreen.handleKeyboardInput();
                    }
                    else {
                        if (mouseID == 1) {
                            this.displayInGameMenu();
                        }
                        if (mouseID == 32 && Keyboard.isKeyDown(61) && this.ingameGUI != null) {
                            this.ingameGUI.getChatGUI().clearChatMessages();
                        }
                        if (mouseID == 31 && Keyboard.isKeyDown(61)) {
                            this.refreshResources();
                        }
                        if (mouseID != 17 || Keyboard.isKeyDown(61)) {}
                        if (mouseID != 18 || Keyboard.isKeyDown(61)) {}
                        if (mouseID != 47 || Keyboard.isKeyDown(61)) {}
                        if (mouseID != 38 || Keyboard.isKeyDown(61)) {}
                        if (mouseID != 22 || Keyboard.isKeyDown(61)) {}
                        if (mouseID == 20 && Keyboard.isKeyDown(61)) {
                            this.refreshResources();
                        }
                        if (mouseID == 33 && Keyboard.isKeyDown(61)) {
                            final boolean var9 = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
                            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, var9 ? -1 : 1);
                        }
                        if (mouseID == 30 && Keyboard.isKeyDown(61)) {
                            this.renderGlobal.loadRenderers();
                        }
                        if (mouseID == 35 && Keyboard.isKeyDown(61)) {
                            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                            this.gameSettings.saveOptions();
                        }
                        if (mouseID == 48 && Keyboard.isKeyDown(61)) {
                            this.renderManager.func_178629_b(!this.renderManager.func_178634_b());
                        }
                        if (mouseID == 25 && Keyboard.isKeyDown(61)) {
                            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                            this.gameSettings.saveOptions();
                        }
                        if (mouseID == 59) {
                            this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                        }
                        if (mouseID == 61) {
                            this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                            this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                        }
                        if (this.gameSettings.keyBindTogglePerspective.isPressed()) {
                            final GameSettings gameSettings = this.gameSettings;
                            ++gameSettings.thirdPersonView;
                            if (this.gameSettings.thirdPersonView > 2) {
                                this.gameSettings.thirdPersonView = 0;
                            }
                            if (this.gameSettings.thirdPersonView == 0) {
                                this.entityRenderer.func_175066_a(this.func_175606_aa());
                            }
                            else if (this.gameSettings.thirdPersonView == 1) {
                                this.entityRenderer.func_175066_a(null);
                            }
                        }
                        if (this.gameSettings.keyBindSmoothCamera.isPressed()) {
                            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                        }
                    }
                    if (!this.gameSettings.showDebugInfo || !this.gameSettings.showDebugProfilerChart) {
                        continue;
                    }
                    if (mouseID == 11) {
                        this.updateDebugProfilerName(0);
                    }
                    for (int var10 = 0; var10 < 9; ++var10) {
                        if (mouseID == 2 + var10) {
                            this.updateDebugProfilerName(var10 + 1);
                        }
                    }
                }
            }
            for (int mouseID = 0; mouseID < 9; ++mouseID) {
                if (this.gameSettings.keyBindsHotbar[mouseID].isPressed()) {
                    if (this.thePlayer.func_175149_v()) {
                        this.ingameGUI.func_175187_g().func_175260_a(mouseID);
                    }
                    else {
                        this.thePlayer.inventory.currentItem = mouseID;
                    }
                }
            }
            final boolean var11 = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;
            while (this.gameSettings.keyBindInventory.isPressed()) {
                if (this.playerController.isRidingHorse()) {
                    this.thePlayer.func_175163_u();
                }
                else {
                    this.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    this.displayGuiScreen(new GuiInventory(this.thePlayer));
                }
            }
            while (this.gameSettings.keyBindDrop.isPressed()) {
                if (!this.thePlayer.func_175149_v()) {
                    this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                }
            }
            while (this.gameSettings.keyBindChat.isPressed() && var11) {
                this.displayGuiScreen(new GuiChat());
            }
            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && var11) {
                this.displayGuiScreen(new GuiChat("/"));
            }
            if (this.thePlayer.isUsingItem()) {
                if (!this.gameSettings.keyBindUseItem.getIsKeyPressed()) {
                    this.playerController.onStoppedUsingItem(this.thePlayer);
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
            if (this.gameSettings.keyBindUseItem.getIsKeyPressed() && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem()) {
                this.rightClickMouse();
            }
            this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.getIsKeyPressed() && this.inGameHasFocus);
        }
        if (this.theWorld != null) {
            if (this.thePlayer != null) {
                ++this.joinPlayerCounter;
                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(this.thePlayer);
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
                if (this.theWorld.func_175658_ac() > 0) {
                    this.theWorld.setLastLightningBolt(this.theWorld.func_175658_ac() - 1);
                }
                this.theWorld.updateEntities();
            }
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
                catch (Throwable var12) {
                    final CrashReport var2 = CrashReport.makeCrashReport(var12, "Exception in world tick");
                    if (this.theWorld == null) {
                        final CrashReportCategory var4 = var2.makeCategory("Affected level");
                        var4.addCrashSection("Problem", "Level is null!");
                    }
                    else {
                        this.theWorld.addWorldInfoToCrashReport(var2);
                    }
                    throw new ReportedException(var2);
                }
            }
            this.mcProfiler.endStartSection("animateTick");
            if (!this.isGamePaused && this.theWorld != null) {
                this.theWorld.doVoidFogParticles(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
            }
            this.mcProfiler.endStartSection("particles");
            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        }
        else if (this.myNetworkManager != null) {
            this.mcProfiler.endStartSection("pendingConnection");
            this.myNetworkManager.processReceivedPackets();
        }
        this.mcProfiler.endSection();
        this.systemTime = getSystemTime();
    }
    
    public void launchIntegratedServer(final String folderName, final String worldName, WorldSettings worldSettingsIn) {
        this.loadWorld(null);
        System.gc();
        final ISaveHandler var4 = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo var5 = var4.loadWorldInfo();
        if (var5 == null && worldSettingsIn != null) {
            var5 = new WorldInfo(worldSettingsIn, folderName);
            var4.saveWorldInfo(var5);
        }
        if (worldSettingsIn == null) {
            worldSettingsIn = new WorldSettings(var5);
        }
        try {
            (this.theIntegratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn)).startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable var7) {
            final CrashReport var6 = CrashReport.makeCrashReport(var7, "Starting integrated server");
            final CrashReportCategory var8 = var6.makeCategory("Starting integrated server");
            var8.addCrashSection("Level ID", folderName);
            var8.addCrashSection("Level Name", worldName);
            throw new ReportedException(var6);
        }
        this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));
        while (!this.theIntegratedServer.serverIsInRunLoop()) {
            final String var9 = this.theIntegratedServer.getUserMessage();
            if (var9 != null) {
                this.loadingScreen.displayLoadingString(I18n.format(var9, new Object[0]));
            }
            else {
                this.loadingScreen.displayLoadingString("");
            }
            try {
                Thread.sleep(200L);
            }
            catch (InterruptedException ex) {}
        }
        this.displayGuiScreen(null);
        final SocketAddress var10 = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        final NetworkManager var11 = NetworkManager.provideLocalClient(var10);
        var11.setNetHandler(new NetHandlerLoginClient(var11, this, null));
        var11.sendPacket(new C00Handshake(47, var10.toString(), 0, EnumConnectionState.LOGIN));
        var11.sendPacket(new C00PacketLoginStart(this.getSession().getProfile()));
        this.myNetworkManager = var11;
    }
    
    public void loadWorld(final WorldClient worldClientIn) {
        this.loadWorld(worldClientIn, "");
    }
    
    public void loadWorld(final WorldClient worldClientIn, final String loadingMessage) {
        if (worldClientIn == null) {
            final NetHandlerPlayClient var3 = this.getNetHandler();
            if (var3 != null) {
                var3.cleanup();
            }
            if (this.theIntegratedServer != null && this.theIntegratedServer.func_175578_N()) {
                this.theIntegratedServer.initiateShutdown();
                this.theIntegratedServer.func_175592_a();
            }
            this.theIntegratedServer = null;
            this.guiAchievement.clearAchievements();
            this.entityRenderer.getMapItemRenderer().func_148249_a();
        }
        this.field_175622_Z = null;
        this.myNetworkManager = null;
        if (this.loadingScreen != null) {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.displayLoadingString("");
        }
        if (worldClientIn == null && this.theWorld != null) {
            if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
                this.mcResourcePackRepository.func_148529_f();
                this.func_175603_A();
            }
            this.setServerData(null);
            this.integratedServerIsRunning = false;
        }
        this.mcSoundHandler.stopSounds();
        if ((this.theWorld = worldClientIn) != null) {
            if (this.renderGlobal != null) {
                this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
            }
            if (this.effectRenderer != null) {
                this.effectRenderer.clearEffects(worldClientIn);
            }
            if (this.thePlayer == null) {
                this.thePlayer = this.playerController.func_178892_a(worldClientIn, new StatFileWriter());
                this.playerController.flipPlayer(this.thePlayer);
            }
            this.thePlayer.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(this.thePlayer);
            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(this.thePlayer);
            this.field_175622_Z = this.thePlayer;
        }
        else {
            this.saveLoader.flushCache();
            this.thePlayer = null;
        }
        System.gc();
        this.systemTime = 0L;
    }
    
    public void setDimensionAndSpawnPlayer(final int dimension) {
        this.theWorld.setInitialSpawnLocation();
        this.theWorld.removeAllEntities();
        int var2 = 0;
        String var3 = null;
        if (this.thePlayer != null) {
            var2 = this.thePlayer.getEntityId();
            this.theWorld.removeEntity(this.thePlayer);
            var3 = this.thePlayer.getClientBrand();
        }
        this.field_175622_Z = null;
        final EntityPlayerSP var4 = this.thePlayer;
        this.thePlayer = this.playerController.func_178892_a(this.theWorld, (this.thePlayer == null) ? new StatFileWriter() : this.thePlayer.getStatFileWriter());
        this.thePlayer.getDataWatcher().updateWatchedObjectsFromList(var4.getDataWatcher().getAllWatched());
        this.thePlayer.dimension = dimension;
        this.field_175622_Z = this.thePlayer;
        this.thePlayer.preparePlayerToSpawn();
        this.thePlayer.func_175158_f(var3);
        this.theWorld.spawnEntityInWorld(this.thePlayer);
        this.playerController.flipPlayer(this.thePlayer);
        this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.thePlayer.setEntityId(var2);
        this.playerController.setPlayerCapabilities(this.thePlayer);
        this.thePlayer.func_175150_k(var4.func_175140_cp());
        if (this.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }
    }
    
    public final boolean isDemo() {
        return this.isDemo;
    }
    
    public NetHandlerPlayClient getNetHandler() {
        return (this.thePlayer != null) ? this.thePlayer.sendQueue : null;
    }
    
    public static boolean isGuiEnabled() {
        return Minecraft.theMinecraft == null || !Minecraft.theMinecraft.gameSettings.hideGUI;
    }
    
    public static boolean isFancyGraphicsEnabled() {
        return Minecraft.theMinecraft != null && Minecraft.theMinecraft.gameSettings.fancyGraphics;
    }
    
    public static boolean isAmbientOcclusionEnabled() {
        return Minecraft.theMinecraft != null && Minecraft.theMinecraft.gameSettings.ambientOcclusion != 0;
    }
    
    private void middleClickMouse() {
        if (this.objectMouseOver != null) {
            final boolean var1 = this.thePlayer.capabilities.isCreativeMode;
            int var2 = 0;
            boolean var3 = false;
            TileEntity var4 = null;
            Object var7;
            if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos var5 = this.objectMouseOver.getBlockPos();
                final Block var6 = this.theWorld.getBlockState(var5).getBlock();
                if (var6.getMaterial() == Material.air) {
                    return;
                }
                var7 = var6.getItem(this.theWorld, var5);
                if (var7 == null) {
                    return;
                }
                if (var1 && GuiScreen.isCtrlKeyDown()) {
                    var4 = this.theWorld.getTileEntity(var5);
                }
                final Block var8 = (var7 instanceof ItemBlock && !var6.isFlowerPot()) ? Block.getBlockFromItem((Item)var7) : var6;
                var2 = var8.getDamageValue(this.theWorld, var5);
                var3 = ((Item)var7).getHasSubtypes();
            }
            else {
                if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !var1) {
                    return;
                }
                if (this.objectMouseOver.entityHit instanceof EntityPainting) {
                    var7 = Items.painting;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
                    var7 = Items.lead;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
                    final EntityItemFrame var9 = (EntityItemFrame)this.objectMouseOver.entityHit;
                    final ItemStack var10 = var9.getDisplayedItem();
                    if (var10 == null) {
                        var7 = Items.item_frame;
                    }
                    else {
                        var7 = var10.getItem();
                        var2 = var10.getMetadata();
                        var3 = true;
                    }
                }
                else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
                    final EntityMinecart var11 = (EntityMinecart)this.objectMouseOver.entityHit;
                    switch (SwitchEnumMinecartType.field_178901_b[var11.func_180456_s().ordinal()]) {
                        case 1: {
                            var7 = Items.furnace_minecart;
                            break;
                        }
                        case 2: {
                            var7 = Items.chest_minecart;
                            break;
                        }
                        case 3: {
                            var7 = Items.tnt_minecart;
                            break;
                        }
                        case 4: {
                            var7 = Items.hopper_minecart;
                            break;
                        }
                        case 5: {
                            var7 = Items.command_block_minecart;
                            break;
                        }
                        default: {
                            var7 = Items.minecart;
                            break;
                        }
                    }
                }
                else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
                    var7 = Items.boat;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
                    var7 = Items.armor_stand;
                }
                else {
                    var7 = Items.spawn_egg;
                    var2 = EntityList.getEntityID(this.objectMouseOver.entityHit);
                    var3 = true;
                    if (!EntityList.entityEggs.containsKey(var2)) {
                        return;
                    }
                }
            }
            final InventoryPlayer var12 = this.thePlayer.inventory;
            if (var4 == null) {
                var12.setCurrentItem((Item)var7, var2, var3, var1);
            }
            else {
                final NBTTagCompound var13 = new NBTTagCompound();
                var4.writeToNBT(var13);
                final ItemStack var14 = new ItemStack((Item)var7, 1, var2);
                var14.setTagInfo("BlockEntityTag", var13);
                final NBTTagCompound var15 = new NBTTagCompound();
                final NBTTagList var16 = new NBTTagList();
                var16.appendTag(new NBTTagString("(+NBT)"));
                var15.setTag("Lore", var16);
                var14.setTagInfo("display", var15);
                var12.setInventorySlotContents(var12.currentItem, var14);
            }
            if (var1) {
                final int var17 = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + var12.currentItem;
                this.playerController.sendSlotPacket(var12.getStackInSlot(var12.currentItem), var17);
            }
        }
    }
    
    public CrashReport addGraphicsAndWorldToCrashReport(final CrashReport theCrash) {
        theCrash.getCategory().addCrashSectionCallable("Launched Version", new Callable() {
            private static final String __OBFID = "CL_00000643";
            
            @Override
            public String call() {
                return Minecraft.this.launchedVersion;
            }
        });
        theCrash.getCategory().addCrashSectionCallable("LWJGL", new Callable() {
            private static final String __OBFID = "CL_00000644";
            
            @Override
            public String call() {
                return Sys.getVersion();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("OpenGL", new Callable() {
            private static final String __OBFID = "CL_00000645";
            
            @Override
            public String call() {
                return GL11.glGetString(7937) + " GL version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936);
            }
        });
        theCrash.getCategory().addCrashSectionCallable("GL Caps", new Callable() {
            private static final String __OBFID = "CL_00000646";
            
            @Override
            public String call() {
                return OpenGlHelper.func_153172_c();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Using VBOs", new Callable() {
            private static final String __OBFID = "CL_00000647";
            
            @Override
            public String call() {
                return Minecraft.this.gameSettings.field_178881_t ? "Yes" : "No";
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Is Modded", new Callable() {
            private static final String __OBFID = "CL_00000633";
            
            @Override
            public String call() {
                final String var1 = ClientBrandRetriever.getClientModName();
                return var1.equals("vanilla") ? ((Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.") : ("Definitely; Client brand changed to '" + var1 + "'");
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Type", new Callable() {
            private static final String __OBFID = "CL_00000634";
            
            @Override
            public String call() {
                return "Client (map_client.txt)";
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Resource Packs", new Callable() {
            private static final String __OBFID = "CL_00000635";
            
            @Override
            public String call() {
                return Minecraft.this.gameSettings.resourcePacks.toString();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Current Language", new Callable() {
            private static final String __OBFID = "CL_00000636";
            
            @Override
            public String call() {
                return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Profiler Position", new Callable() {
            private static final String __OBFID = "CL_00000637";
            
            @Override
            public String call() {
                return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });
        if (this.theWorld != null) {
            this.theWorld.addWorldInfoToCrashReport(theCrash);
        }
        return theCrash;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.theMinecraft;
    }
    
    public ListenableFuture func_175603_A() {
        return this.addScheduledTask(new Runnable() {
            private static final String __OBFID = "CL_00001853";
            
            @Override
            public void run() {
                Minecraft.this.refreshResources();
            }
        });
    }
    
    @Override
    public void addServerStatsToSnooper(final PlayerUsageSnooper playerSnooper) {
        playerSnooper.addClientStat("fps", Minecraft.debugFPS);
        playerSnooper.addClientStat("vsync_enabled", this.gameSettings.enableVsync);
        playerSnooper.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
        playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
        playerSnooper.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        final String var2 = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) ? "little" : "big";
        playerSnooper.addClientStat("endianness", var2);
        playerSnooper.addClientStat("resource_packs", this.mcResourcePackRepository.getRepositoryEntries().size());
        int var3 = 0;
        for (final ResourcePackRepository.Entry var5 : this.mcResourcePackRepository.getRepositoryEntries()) {
            playerSnooper.addClientStat("resource_pack[" + var3++ + "]", var5.getResourcePackName());
        }
        if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null) {
            playerSnooper.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }
    
    @Override
    public void addServerTypeToSnooper(final PlayerUsageSnooper playerSnooper) {
        playerSnooper.addStatToSnooper("opengl_version", GL11.glGetString(7938));
        playerSnooper.addStatToSnooper("opengl_vendor", GL11.glGetString(7936));
        playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
        final ContextCapabilities var2 = GLContext.getCapabilities();
        playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", var2.GL_ARB_arrays_of_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", var2.GL_ARB_base_instance);
        playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", var2.GL_ARB_blend_func_extended);
        playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", var2.GL_ARB_clear_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", var2.GL_ARB_color_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", var2.GL_ARB_compatibility);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", var2.GL_ARB_compressed_texture_pixel_storage);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", var2.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", var2.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", var2.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", var2.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", var2.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", var2.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", var2.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", var2.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", var2.GL_ARB_depth_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", var2.GL_ARB_depth_texture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", var2.GL_ARB_draw_buffers);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", var2.GL_ARB_draw_buffers_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", var2.GL_ARB_draw_elements_base_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", var2.GL_ARB_draw_indirect);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", var2.GL_ARB_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", var2.GL_ARB_explicit_attrib_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", var2.GL_ARB_explicit_uniform_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", var2.GL_ARB_fragment_layer_viewport);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", var2.GL_ARB_fragment_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", var2.GL_ARB_fragment_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", var2.GL_ARB_fragment_program_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", var2.GL_ARB_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", var2.GL_ARB_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", var2.GL_ARB_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", var2.GL_ARB_gpu_shader5);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", var2.GL_ARB_half_float_pixel);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", var2.GL_ARB_half_float_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", var2.GL_ARB_instanced_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", var2.GL_ARB_map_buffer_alignment);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", var2.GL_ARB_map_buffer_range);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", var2.GL_ARB_multisample);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", var2.GL_ARB_multitexture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", var2.GL_ARB_occlusion_query2);
        playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", var2.GL_ARB_pixel_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", var2.GL_ARB_seamless_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", var2.GL_ARB_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", var2.GL_ARB_shader_stencil_export);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", var2.GL_ARB_shader_texture_lod);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", var2.GL_ARB_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", var2.GL_ARB_shadow_ambient);
        playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", var2.GL_ARB_stencil_texturing);
        playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", var2.GL_ARB_sync);
        playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", var2.GL_ARB_tessellation_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", var2.GL_ARB_texture_border_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", var2.GL_ARB_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", var2.GL_ARB_texture_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", var2.GL_ARB_texture_cube_map_array);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", var2.GL_ARB_texture_non_power_of_two);
        playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", var2.GL_ARB_uniform_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", var2.GL_ARB_vertex_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", var2.GL_ARB_vertex_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", var2.GL_ARB_vertex_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", var2.GL_ARB_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", var2.GL_EXT_bindable_uniform);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", var2.GL_EXT_blend_equation_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", var2.GL_EXT_blend_func_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", var2.GL_EXT_blend_minmax);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", var2.GL_EXT_blend_subtract);
        playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", var2.GL_EXT_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", var2.GL_EXT_framebuffer_multisample);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", var2.GL_EXT_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", var2.GL_EXT_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", var2.GL_EXT_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", var2.GL_EXT_gpu_program_parameters);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", var2.GL_EXT_gpu_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", var2.GL_EXT_multi_draw_arrays);
        playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", var2.GL_EXT_packed_depth_stencil);
        playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", var2.GL_EXT_paletted_texture);
        playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", var2.GL_EXT_rescale_normal);
        playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", var2.GL_EXT_separate_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", var2.GL_EXT_shader_image_load_store);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", var2.GL_EXT_shadow_funcs);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", var2.GL_EXT_shared_texture_palette);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", var2.GL_EXT_stencil_clear_tag);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", var2.GL_EXT_stencil_two_side);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", var2.GL_EXT_stencil_wrap);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", var2.GL_EXT_texture_3d);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", var2.GL_EXT_texture_array);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", var2.GL_EXT_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", var2.GL_EXT_texture_integer);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", var2.GL_EXT_texture_lod_bias);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", var2.GL_EXT_texture_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", var2.GL_EXT_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", var2.GL_EXT_vertex_weighting);
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GL11.glGetInteger(35658));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GL11.glGetInteger(35657));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GL11.glGetInteger(34921));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GL11.glGetInteger(35660));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger(34930));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger(35071));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_max_texture_size", getGLMaximumTextureSize());
    }
    
    public static int getGLMaximumTextureSize() {
        for (int var0 = 16384; var0 > 0; var0 >>= 1) {
            GL11.glTexImage2D(32868, 0, 6408, var0, var0, 0, 6408, 5121, (ByteBuffer)null);
            final int var2 = GL11.glGetTexLevelParameteri(32868, 0, 4096);
            if (var2 != 0) {
                return var0;
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
        if (Minecraft.theMinecraft != null) {
            final IntegratedServer var0 = Minecraft.theMinecraft.getIntegratedServer();
            if (var0 != null) {
                var0.stopServer();
            }
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
    
    public PropertyMap func_180509_L() {
        return this.twitchDetails;
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
        return (this.currentScreen instanceof GuiWinGame) ? MusicTicker.MusicType.CREDITS : ((this.thePlayer != null) ? ((this.thePlayer.worldObj.provider instanceof WorldProviderHell) ? MusicTicker.MusicType.NETHER : ((this.thePlayer.worldObj.provider instanceof WorldProviderEnd) ? ((BossStatus.bossName != null && BossStatus.statusBarTime > 0) ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END) : ((this.thePlayer.capabilities.isCreativeMode && this.thePlayer.capabilities.allowFlying) ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME))) : MusicTicker.MusicType.MENU);
    }
    
    public IStream getTwitchStream() {
        return this.stream;
    }
    
    public void dispatchKeypresses() {
        final int var1 = (Keyboard.getEventKey() == 0) ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
        if (var1 != 0 && !Keyboard.isRepeatEvent() && (!(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= getSystemTime() - 20L)) {
            if (Keyboard.getEventKeyState()) {
                if (var1 == this.gameSettings.keyBindStreamStartStop.getKeyCode()) {
                    if (this.getTwitchStream().func_152934_n()) {
                        this.getTwitchStream().func_152914_u();
                    }
                    else if (this.getTwitchStream().func_152924_m()) {
                        this.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                            private static final String __OBFID = "CL_00001852";
                            
                            @Override
                            public void confirmClicked(final boolean result, final int id) {
                                if (result) {
                                    Minecraft.this.getTwitchStream().func_152930_t();
                                }
                                Minecraft.this.displayGuiScreen(null);
                            }
                        }, I18n.format("stream.confirm_start", new Object[0]), "", 0));
                    }
                    else if (this.getTwitchStream().func_152928_D() && this.getTwitchStream().func_152936_l()) {
                        if (this.theWorld != null) {
                            this.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
                        }
                    }
                    else {
                        GuiStreamUnavailable.func_152321_a(this.currentScreen);
                    }
                }
                else if (var1 == this.gameSettings.keyBindStreamPauseUnpause.getKeyCode()) {
                    if (this.getTwitchStream().func_152934_n()) {
                        if (this.getTwitchStream().isPaused()) {
                            this.getTwitchStream().func_152933_r();
                        }
                        else {
                            this.getTwitchStream().func_152916_q();
                        }
                    }
                }
                else if (var1 == this.gameSettings.keyBindStreamCommercials.getKeyCode()) {
                    if (this.getTwitchStream().func_152934_n()) {
                        this.getTwitchStream().func_152931_p();
                    }
                }
                else if (var1 == this.gameSettings.keyBindStreamToggleMic.getKeyCode()) {
                    this.stream.func_152910_a(true);
                }
                else if (var1 == this.gameSettings.keyBindFullscreen.getKeyCode()) {
                    this.toggleFullscreen();
                }
                else if (var1 == this.gameSettings.keyBindScreenshot.getKeyCode()) {
                    this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc));
                }
            }
            else if (var1 == this.gameSettings.keyBindStreamToggleMic.getKeyCode()) {
                this.stream.func_152910_a(false);
            }
        }
    }
    
    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }
    
    public SkinManager getSkinManager() {
        return this.skinManager;
    }
    
    public Entity func_175606_aa() {
        return this.field_175622_Z;
    }
    
    public void func_175607_a(final Entity p_175607_1_) {
        this.field_175622_Z = p_175607_1_;
        this.entityRenderer.func_175066_a(p_175607_1_);
    }
    
    public ListenableFuture addScheduledTask(final Callable callableToSchedule) {
        Validate.notNull((Object)callableToSchedule);
        if (!this.isCallingFromMinecraftThread()) {
            final ListenableFutureTask var2 = ListenableFutureTask.create(callableToSchedule);
            final Queue var3 = this.scheduledTasks;
            synchronized (this.scheduledTasks) {
                this.scheduledTasks.add(var2);
                return (ListenableFuture)var2;
            }
        }
        try {
            return Futures.immediateFuture(callableToSchedule.call());
        }
        catch (Exception var4) {
            return (ListenableFuture)Futures.immediateFailedCheckedFuture(var4);
        }
    }
    
    @Override
    public ListenableFuture addScheduledTask(final Runnable runnableToSchedule) {
        Validate.notNull((Object)runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }
    
    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.mcThread;
    }
    
    public BlockRendererDispatcher getBlockRendererDispatcher() {
        return this.field_175618_aM;
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
    
    public static int func_175610_ah() {
        return Minecraft.debugFPS;
    }
    
    public static Map func_175596_ai() {
        final HashMap var0 = Maps.newHashMap();
        var0.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
        var0.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
        var0.put("X-Minecraft-Version", "1.8");
        return var0;
    }
    
    public void setSession(final Session session) {
        this.session = session;
    }
    
    public Entity getRenderViewEntity() {
        return this.field_175622_Z;
    }
    
    static {
        logger = LogManager.getLogger();
        locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
        isRunningOnMac = (Util.getOSType() == Util.EnumOS.OSX);
        Minecraft.memoryReserve = new byte[10485760];
        macDisplayModes = Lists.newArrayList((Object[])new DisplayMode[] { new DisplayMode(2560, 1600), new DisplayMode(2880, 1800) });
    }
    
    static final class SwitchEnumMinecartType
    {
        static final int[] field_152390_a;
        static final int[] field_178901_b;
        private static final String __OBFID = "CL_00001959";
        
        static {
            field_178901_b = new int[EntityMinecart.EnumMinecartType.values().length];
            try {
                SwitchEnumMinecartType.field_178901_b[EntityMinecart.EnumMinecartType.FURNACE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumMinecartType.field_178901_b[EntityMinecart.EnumMinecartType.CHEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumMinecartType.field_178901_b[EntityMinecart.EnumMinecartType.TNT.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumMinecartType.field_178901_b[EntityMinecart.EnumMinecartType.HOPPER.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumMinecartType.field_178901_b[EntityMinecart.EnumMinecartType.COMMAND_BLOCK.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            field_152390_a = new int[MovingObjectPosition.MovingObjectType.values().length];
            try {
                SwitchEnumMinecartType.field_152390_a[MovingObjectPosition.MovingObjectType.ENTITY.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchEnumMinecartType.field_152390_a[MovingObjectPosition.MovingObjectType.BLOCK.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchEnumMinecartType.field_152390_a[MovingObjectPosition.MovingObjectType.MISS.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
        }
    }
}
