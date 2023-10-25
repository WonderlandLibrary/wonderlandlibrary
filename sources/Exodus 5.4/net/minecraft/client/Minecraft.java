/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Multimap
 *  com.google.common.collect.Queues
 *  com.google.common.collect.Sets
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.ListenableFutureTask
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftSessionService
 *  com.mojang.authlib.properties.Property
 *  com.mojang.authlib.properties.PropertyMap
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.Validate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.LWJGLException
 *  org.lwjgl.Sys
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.ContextCapabilities
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.DisplayMode
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.opengl.OpenGLException
 *  org.lwjgl.opengl.PixelFormat
 *  org.lwjgl.util.glu.GLU
 */
package net.minecraft.client;

import com.google.common.collect.Iterables;
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
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventType;
import me.Tengoku.Terror.event.events.EventKey;
import me.Tengoku.Terror.event.events.EventTick;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
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
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
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
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
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

public class Minecraft
implements IThreadListener,
IPlayerUsage {
    private final String launchedVersion;
    public boolean field_175614_C = false;
    public GuiAchievement guiAchievement;
    private static final List<DisplayMode> macDisplayModes;
    int fpsCounter;
    private int joinPlayerCounter;
    private MusicTicker mcMusicTicker;
    public static GameSettings gameSettings;
    public Timer timer = new Timer(20.0f);
    private RenderItem renderItem;
    public TextureManager renderEngine;
    private final MinecraftSessionService sessionService;
    private final boolean isDemo;
    private final File fileResourcepacks;
    private final Proxy proxy;
    long field_181543_z;
    public final FrameTimer field_181542_y;
    public GuiIngame ingameGUI;
    private static final ResourceLocation locationMojangPng;
    private ResourcePackRepository mcResourcePackRepository;
    private final PropertyMap field_181038_N;
    public static EntityPlayerSP thePlayer;
    private ResourceLocation mojangLogo;
    private LanguageManager mcLanguageManager;
    private TextureMap textureMapBlocks;
    private CrashReport crashReporter;
    private int leftClickCounter;
    private static Minecraft theMinecraft;
    public RenderManager renderManager;
    public int displayWidth;
    private final Queue<FutureTask<?>> scheduledTasks;
    public boolean field_175613_B = false;
    private IStream stream;
    private String debugProfilerName = "root";
    private SoundHandler mcSoundHandler;
    private final PropertyMap twitchDetails;
    private ModelManager modelManager;
    private static int debugFPS;
    public Entity pointedEntity;
    public static final boolean isRunningOnMac;
    private final IMetadataSerializer metadataSerializer_;
    public final File mcDataDir;
    public static byte[] memoryReserve;
    private NetworkManager myNetworkManager;
    public MouseHelper mouseHelper;
    public boolean renderChunksMany = true;
    private int tempDisplayWidth;
    private boolean field_181541_X = false;
    public static WorldClient theWorld;
    public int rightClickDelayTimer;
    private SkinManager skinManager;
    private boolean hasCrashed;
    private ItemRenderer itemRenderer;
    long debugUpdateTime;
    private boolean isGamePaused;
    public EntityRenderer entityRenderer;
    private final File fileAssets;
    public boolean field_175611_D = false;
    public Session session;
    public GuiScreen currentScreen;
    private long debugCrashKeyPressTime = -1L;
    public FontRenderer standardGalacticFontRenderer;
    long systemTime;
    private final boolean jvm64bit;
    private final DefaultResourcePack mcDefaultResourcePack;
    private final List<IResourcePack> defaultResourcePacks;
    private int serverPort;
    private IReloadableResourceManager mcResourceManager;
    private ISaveFormat saveLoader;
    private boolean enableGLErrorChecking = true;
    volatile boolean running = true;
    private long field_175615_aJ = 0L;
    private IntegratedServer theIntegratedServer;
    public static FontRenderer fontRendererObj;
    private boolean fullscreen;
    private int tempDisplayHeight;
    public boolean inGameHasFocus;
    public String debug = "";
    private String serverName;
    private ServerData currentServerData;
    long prevFrameTime = -1L;
    private static final Logger logger;
    private Entity renderViewEntity;
    public EffectRenderer effectRenderer;
    private final Thread mcThread;
    private BlockRendererDispatcher blockRenderDispatcher;
    private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
    public static PlayerControllerMP playerController;
    private Framebuffer framebufferMc;
    private boolean integratedServerIsRunning;
    public RenderGlobal renderGlobal;
    public static int displayHeight;
    public LoadingScreenRenderer loadingScreen;
    public MovingObjectPosition objectMouseOver;
    public boolean skipRenderWorld;
    public final Profiler mcProfiler;

    private void startTimerHackThread() {
        Thread thread = new Thread("Timer hack thread"){

            @Override
            public void run() {
                while (Minecraft.this.running) {
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }

    public Session getSession() {
        return this.session;
    }

    public boolean func_181540_al() {
        return this.field_181541_X;
    }

    public static int getGLMaximumTextureSize() {
        int n = 16384;
        while (n > 0) {
            GL11.glTexImage2D((int)32868, (int)0, (int)6408, (int)n, (int)n, (int)0, (int)6408, (int)5121, null);
            int n2 = GL11.glGetTexLevelParameteri((int)32868, (int)0, (int)4096);
            if (n2 != 0) {
                return n;
            }
            n >>= 1;
        }
        return -1;
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.mcThread;
    }

    public boolean isFullScreen() {
        return this.fullscreen;
    }

    public boolean isSingleplayer() {
        return this.integratedServerIsRunning && this.theIntegratedServer != null;
    }

    private void updateDisplayMode() throws LWJGLException {
        HashSet hashSet = Sets.newHashSet();
        Collections.addAll(hashSet, Display.getAvailableDisplayModes());
        DisplayMode displayMode = Display.getDesktopDisplayMode();
        if (!hashSet.contains(displayMode) && Util.getOSType() == Util.EnumOS.OSX) {
            block0: for (DisplayMode displayMode2 : macDisplayModes) {
                Object object2;
                boolean bl = true;
                for (Object object2 : hashSet) {
                    if (object2.getBitsPerPixel() != 32 || object2.getWidth() != displayMode2.getWidth() || object2.getHeight() != displayMode2.getHeight()) continue;
                    bl = false;
                    break;
                }
                if (bl) continue;
                object2 = hashSet.iterator();
                while (object2.hasNext()) {
                    DisplayMode displayMode3 = (DisplayMode)object2.next();
                    if (displayMode3.getBitsPerPixel() != 32 || displayMode3.getWidth() != displayMode2.getWidth() / 2 || displayMode3.getHeight() != displayMode2.getHeight() / 2) continue;
                    displayMode = displayMode3;
                    continue block0;
                }
            }
        }
        Display.setDisplayMode((DisplayMode)displayMode);
        this.displayWidth = displayMode.getWidth();
        displayHeight = displayMode.getHeight();
    }

    private void resize(int n, int n2) {
        this.displayWidth = Math.max(1, n);
        displayHeight = Math.max(1, n2);
        if (this.currentScreen != null) {
            ScaledResolution scaledResolution = new ScaledResolution(this);
            this.currentScreen.onResize(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        }
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.updateFramebufferSize();
    }

    public void dispatchKeypresses() {
        int n;
        int n2 = n = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
        if (!(n == 0 || Keyboard.isRepeatEvent() || this.currentScreen instanceof GuiControls && ((GuiControls)this.currentScreen).time > Minecraft.getSystemTime() - 20L)) {
            if (Keyboard.getEventKeyState()) {
                if (n == Minecraft.gameSettings.keyBindStreamStartStop.getKeyCode()) {
                    if (this.getTwitchStream().isBroadcasting()) {
                        this.getTwitchStream().stopBroadcasting();
                    } else if (this.getTwitchStream().isReadyToBroadcast()) {
                        this.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback(){

                            @Override
                            public void confirmClicked(boolean bl, int n) {
                                if (bl) {
                                    Minecraft.this.getTwitchStream().func_152930_t();
                                }
                                Minecraft.this.displayGuiScreen(null);
                            }
                        }, I18n.format("stream.confirm_start", new Object[0]), "", 0));
                    } else if (this.getTwitchStream().func_152928_D() && this.getTwitchStream().func_152936_l()) {
                        if (theWorld != null) {
                            this.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
                        }
                    } else {
                        GuiStreamUnavailable.func_152321_a(this.currentScreen);
                    }
                } else if (n == Minecraft.gameSettings.keyBindStreamPauseUnpause.getKeyCode()) {
                    if (this.getTwitchStream().isBroadcasting()) {
                        if (this.getTwitchStream().isPaused()) {
                            this.getTwitchStream().unpause();
                        } else {
                            this.getTwitchStream().pause();
                        }
                    }
                } else if (n == Minecraft.gameSettings.keyBindStreamCommercials.getKeyCode()) {
                    if (this.getTwitchStream().isBroadcasting()) {
                        this.getTwitchStream().requestCommercial();
                    }
                } else if (n == Minecraft.gameSettings.keyBindStreamToggleMic.getKeyCode()) {
                    this.stream.muteMicrophone(true);
                } else if (n == Minecraft.gameSettings.keyBindFullscreen.getKeyCode()) {
                    this.toggleFullscreen();
                } else if (n == Minecraft.gameSettings.keyBindScreenshot.getKeyCode()) {
                    this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, displayHeight, this.framebufferMc));
                }
            } else if (n == Minecraft.gameSettings.keyBindStreamToggleMic.getKeyCode()) {
                this.stream.muteMicrophone(false);
            }
        }
    }

    private ItemStack func_181036_a(Item item, int n, TileEntity tileEntity) {
        ItemStack itemStack = new ItemStack(item, 1, n);
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        tileEntity.writeToNBT(nBTTagCompound);
        if (item == Items.skull && nBTTagCompound.hasKey("Owner")) {
            NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Owner");
            NBTTagCompound nBTTagCompound3 = new NBTTagCompound();
            nBTTagCompound3.setTag("SkullOwner", nBTTagCompound2);
            itemStack.setTagCompound(nBTTagCompound3);
            return itemStack;
        }
        itemStack.setTagInfo("BlockEntityTag", nBTTagCompound);
        NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
        NBTTagList nBTTagList = new NBTTagList();
        nBTTagList.appendTag(new NBTTagString("(+NBT)"));
        nBTTagCompound4.setTag("Lore", nBTTagList);
        itemStack.setTagInfo("display", nBTTagCompound4);
        return itemStack;
    }

    public static boolean isGuiEnabled() {
        return theMinecraft == null || !Minecraft.gameSettings.hideGUI;
    }

    public int getLimitFramerate() {
        return theWorld == null && this.currentScreen != null ? 30 : Minecraft.gameSettings.limitFramerate;
    }

    private void updateFramebufferSize() {
        this.framebufferMc.createBindFramebuffer(this.displayWidth, displayHeight);
        if (this.entityRenderer != null) {
            this.entityRenderer.updateShaderGroupSize(this.displayWidth, displayHeight);
        }
    }

    public static boolean isFancyGraphicsEnabled() {
        return theMinecraft != null && Minecraft.gameSettings.fancyGraphics;
    }

    private void setInitialDisplayMode() throws LWJGLException {
        if (this.fullscreen) {
            Display.setFullscreen((boolean)true);
            DisplayMode displayMode = Display.getDisplayMode();
            this.displayWidth = Math.max(1, displayMode.getWidth());
            displayHeight = Math.max(1, displayMode.getHeight());
        } else {
            Display.setDisplayMode((DisplayMode)new DisplayMode(this.displayWidth, displayHeight));
        }
    }

    public TextureMap getTextureMapBlocks() {
        return this.textureMapBlocks;
    }

    private void startGame() throws IOException, LWJGLException {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> list = runtimeMXBean.getInputArguments();
        gameSettings = new GameSettings(this, this.mcDataDir);
        this.defaultResourcePacks.add(this.mcDefaultResourcePack);
        this.startTimerHackThread();
        if (Minecraft.gameSettings.overrideHeight > 0 && Minecraft.gameSettings.overrideWidth > 0) {
            this.displayWidth = Minecraft.gameSettings.overrideWidth;
            displayHeight = Minecraft.gameSettings.overrideHeight;
        }
        System.out.println(list);
        logger.info("LWJGL Version: " + Sys.getVersion());
        this.setWindowIcon();
        this.setInitialDisplayMode();
        this.createDisplay();
        OpenGlHelper.initializeTextures();
        this.framebufferMc = new Framebuffer(this.displayWidth, displayHeight, true);
        this.framebufferMc.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.registerMetadataSerializers();
        this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, gameSettings);
        if (list.contains("-XX:G1NewSizePercent=19") && list.contains("-XX:MaxGCPauseMillis=34")) {
            this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        }
        this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, Minecraft.gameSettings.language);
        this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.renderEngine);
        this.drawSplashScreen(this.renderEngine);
        this.initStream();
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
        this.mcSoundHandler = new SoundHandler(this.mcResourceManager, gameSettings);
        this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(this);
        fontRendererObj = new FontRenderer(gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
        if (Minecraft.gameSettings.language != null) {
            fontRendererObj.setUnicodeFlag(this.isUnicode());
            fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
        }
        this.standardGalacticFontRenderer = new FontRenderer(gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.mcResourceManager.registerReloadListener(fontRendererObj);
        this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat(){

            @Override
            public String formatString(String string) {
                try {
                    return String.format(string, GameSettings.getKeyDisplayString(Minecraft.gameSettings.keyBindInventory.getKeyCode()));
                }
                catch (Exception exception) {
                    return "Error: " + exception.getLocalizedMessage();
                }
            }
        });
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
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
        this.textureMapBlocks = new TextureMap("textures");
        this.textureMapBlocks.setMipmapLevels(Minecraft.gameSettings.mipmapLevels);
        this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        this.textureMapBlocks.setBlurMipmapDirect(false, Minecraft.gameSettings.mipmapLevels > 0);
        this.modelManager = new ModelManager(this.textureMapBlocks);
        this.mcResourceManager.registerReloadListener(this.modelManager);
        this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        this.itemRenderer = new ItemRenderer(this);
        this.mcResourceManager.registerReloadListener(this.renderItem);
        this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.entityRenderer);
        this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), gameSettings);
        this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
        this.renderGlobal = new RenderGlobal(this);
        this.mcResourceManager.registerReloadListener(this.renderGlobal);
        this.guiAchievement = new GuiAchievement(this);
        GlStateManager.viewport(0, 0, this.displayWidth, displayHeight);
        this.effectRenderer = new EffectRenderer(theWorld, this.renderEngine);
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);
        if (this.serverName != null) {
            this.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort));
        } else {
            this.displayGuiScreen(new GuiMainMenu());
        }
        this.renderEngine.deleteTexture(this.mojangLogo);
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(this);
        if (Minecraft.gameSettings.fullScreen && !this.fullscreen) {
            this.toggleFullscreen();
        }
        try {
            Display.setVSyncEnabled((boolean)Minecraft.gameSettings.enableVsync);
        }
        catch (OpenGLException openGLException) {
            Minecraft.gameSettings.enableVsync = false;
            gameSettings.saveOptions();
        }
        Exodus.INSTANCE.startClient();
        this.renderGlobal.makeEntityOutlineShader();
    }

    public LanguageManager getLanguageManager() {
        return this.mcLanguageManager;
    }

    public ItemRenderer getItemRenderer() {
        return this.itemRenderer;
    }

    public boolean isFramerateLimitBelowMax() {
        return (float)this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }

    public PropertyMap func_181037_M() {
        if (this.field_181038_N.isEmpty()) {
            GameProfile gameProfile = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
            this.field_181038_N.putAll((Multimap)gameProfile.getProperties());
        }
        return this.field_181038_N;
    }

    private void clickMouse() {
        if (this.leftClickCounter <= 0) {
            thePlayer.swingItem();
            if (this.objectMouseOver == null) {
                logger.error("Null returned as 'hitResult', this shouldn't happen!");
                if (playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            } else {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY: {
                        playerController.attackEntity(thePlayer, this.objectMouseOver.entityHit);
                        break;
                    }
                    case BLOCK: {
                        BlockPos blockPos = this.objectMouseOver.getBlockPos();
                        if (theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air) {
                            playerController.clickBlock(blockPos, this.objectMouseOver.sideHit);
                            break;
                        }
                    }
                    default: {
                        if (!playerController.isNotCreative()) break;
                        this.leftClickCounter = 10;
                    }
                }
            }
        }
    }

    private void displayDebugInfo(long l) {
        if (this.mcProfiler.profilingEnabled) {
            int n;
            int n2;
            Object object;
            List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);
            Profiler.Result result = list.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, this.displayWidth, displayHeight, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GL11.glLineWidth((float)1.0f);
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            int n3 = 160;
            int n4 = this.displayWidth - n3 - 10;
            int n5 = displayHeight - n3 * 2;
            GlStateManager.enableBlend();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldRenderer.pos((float)n4 - (float)n3 * 1.1f, (float)n5 - (float)n3 * 0.6f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
            worldRenderer.pos((float)n4 - (float)n3 * 1.1f, n5 + n3 * 2, 0.0).color(200, 0, 0, 0).endVertex();
            worldRenderer.pos((float)n4 + (float)n3 * 1.1f, n5 + n3 * 2, 0.0).color(200, 0, 0, 0).endVertex();
            worldRenderer.pos((float)n4 + (float)n3 * 1.1f, (float)n5 - (float)n3 * 0.6f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            double d = 0.0;
            int n6 = 0;
            while (n6 < list.size()) {
                float f;
                float f2;
                float f3;
                object = list.get(n6);
                n2 = MathHelper.floor_double(((Profiler.Result)object).field_76332_a / 4.0) + 1;
                worldRenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                n = ((Profiler.Result)object).func_76329_a();
                int n7 = n >> 16 & 0xFF;
                int n8 = n >> 8 & 0xFF;
                int n9 = n & 0xFF;
                worldRenderer.pos(n4, n5, 0.0).color(n7, n8, n9, 255).endVertex();
                int n10 = n2;
                while (n10 >= 0) {
                    f3 = (float)((d + ((Profiler.Result)object).field_76332_a * (double)n10 / (double)n2) * Math.PI * 2.0 / 100.0);
                    f2 = MathHelper.sin(f3) * (float)n3;
                    f = MathHelper.cos(f3) * (float)n3 * 0.5f;
                    worldRenderer.pos((float)n4 + f2, (float)n5 - f, 0.0).color(n7, n8, n9, 255).endVertex();
                    --n10;
                }
                tessellator.draw();
                worldRenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                n10 = n2;
                while (n10 >= 0) {
                    f3 = (float)((d + ((Profiler.Result)object).field_76332_a * (double)n10 / (double)n2) * Math.PI * 2.0 / 100.0);
                    f2 = MathHelper.sin(f3) * (float)n3;
                    f = MathHelper.cos(f3) * (float)n3 * 0.5f;
                    worldRenderer.pos((float)n4 + f2, (float)n5 - f, 0.0).color(n7 >> 1, n8 >> 1, n9 >> 1, 255).endVertex();
                    worldRenderer.pos((float)n4 + f2, (float)n5 - f + 10.0f, 0.0).color(n7 >> 1, n8 >> 1, n9 >> 1, 255).endVertex();
                    --n10;
                }
                tessellator.draw();
                d += ((Profiler.Result)object).field_76332_a;
                ++n6;
            }
            DecimalFormat decimalFormat = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            object = "";
            if (!result.field_76331_c.equals("unspecified")) {
                object = String.valueOf(object) + "[0] ";
            }
            object = result.field_76331_c.length() == 0 ? String.valueOf(object) + "ROOT " : String.valueOf(object) + result.field_76331_c + " ";
            n2 = 0xFFFFFF;
            fontRendererObj.drawStringWithShadow((String)object, n4 - n3, n5 - n3 / 2 - 16, n2);
            object = String.valueOf(decimalFormat.format(result.field_76330_b)) + "%";
            fontRendererObj.drawStringWithShadow((String)object, n4 + n3 - fontRendererObj.getStringWidth((String)object), n5 - n3 / 2 - 16, n2);
            n = 0;
            while (n < list.size()) {
                Profiler.Result result2 = list.get(n);
                String string = "";
                string = result2.field_76331_c.equals("unspecified") ? String.valueOf(string) + "[?] " : String.valueOf(string) + "[" + (n + 1) + "] ";
                string = String.valueOf(string) + result2.field_76331_c;
                fontRendererObj.drawStringWithShadow(string, n4 - n3, n5 + n3 / 2 + n * 8 + 20, result2.func_76329_a());
                string = String.valueOf(decimalFormat.format(result2.field_76332_a)) + "%";
                fontRendererObj.drawStringWithShadow(string, n4 + n3 - 50 - fontRendererObj.getStringWidth(string), n5 + n3 / 2 + n * 8 + 20, result2.func_76329_a());
                string = String.valueOf(decimalFormat.format(result2.field_76330_b)) + "%";
                fontRendererObj.drawStringWithShadow(string, n4 + n3 - fontRendererObj.getStringWidth(string), n5 + n3 / 2 + n * 8 + 20, result2.func_76329_a());
                ++n;
            }
        }
    }

    public static Map<String, String> getSessionInfo() {
        HashMap hashMap = Maps.newHashMap();
        hashMap.put("X-Minecraft-Username", Minecraft.getMinecraft().getSession().getUsername());
        hashMap.put("X-Minecraft-UUID", Minecraft.getMinecraft().getSession().getPlayerID());
        hashMap.put("X-Minecraft-Version", "1.8.8");
        return hashMap;
    }

    public boolean isUnicode() {
        return this.mcLanguageManager.isCurrentLocaleUnicode() || Minecraft.gameSettings.forceUnicodeFont;
    }

    public void displayInGameMenu() {
        if (this.currentScreen == null) {
            this.displayGuiScreen(new GuiIngameMenu());
            if (this.isSingleplayer() && !this.theIntegratedServer.getPublic()) {
                this.mcSoundHandler.pauseSounds();
            }
        }
    }

    public void crashed(CrashReport crashReport) {
        this.hasCrashed = true;
        this.crashReporter = crashReport;
    }

    public SoundHandler getSoundHandler() {
        return this.mcSoundHandler;
    }

    public String getVersion() {
        return this.launchedVersion;
    }

    public void setDimensionAndSpawnPlayer(int n) {
        theWorld.setInitialSpawnLocation();
        theWorld.removeAllEntities();
        int n2 = 0;
        String string = null;
        if (thePlayer != null) {
            n2 = thePlayer.getEntityId();
            theWorld.removeEntity(thePlayer);
            string = thePlayer.getClientBrand();
        }
        this.renderViewEntity = null;
        EntityPlayerSP entityPlayerSP = thePlayer;
        thePlayer = playerController.func_178892_a(theWorld, thePlayer == null ? new StatFileWriter() : thePlayer.getStatFileWriter());
        thePlayer.getDataWatcher().updateWatchedObjectsFromList(entityPlayerSP.getDataWatcher().getAllWatched());
        Minecraft.thePlayer.dimension = n;
        this.renderViewEntity = thePlayer;
        thePlayer.preparePlayerToSpawn();
        thePlayer.setClientBrand(string);
        theWorld.spawnEntityInWorld(thePlayer);
        playerController.flipPlayer(thePlayer);
        Minecraft.thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
        thePlayer.setEntityId(n2);
        playerController.setPlayerCapabilities(thePlayer);
        thePlayer.setReducedDebug(entityPlayerSP.hasReducedDebug());
        if (this.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }
    }

    public void setServerData(ServerData serverData) {
        this.currentServerData = serverData;
    }

    private void setWindowIcon() {
        block4: {
            Util.EnumOS enumOS = Util.getOSType();
            if (enumOS != Util.EnumOS.OSX) {
                InputStream inputStream;
                InputStream inputStream2;
                block3: {
                    inputStream2 = null;
                    inputStream = null;
                    try {
                        inputStream2 = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
                        inputStream = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
                        if (inputStream2 == null || inputStream == null) break block3;
                        Display.setIcon((ByteBuffer[])new ByteBuffer[]{this.readImageToBuffer(inputStream2), this.readImageToBuffer(inputStream)});
                    }
                    catch (IOException iOException) {
                        logger.error("Couldn't set icon", (Throwable)iOException);
                        IOUtils.closeQuietly((InputStream)inputStream2);
                        IOUtils.closeQuietly(inputStream);
                        break block4;
                    }
                }
                IOUtils.closeQuietly((InputStream)inputStream2);
                IOUtils.closeQuietly((InputStream)inputStream);
            }
        }
    }

    public void displayGuiScreen(GuiScreen guiScreen) {
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }
        if (guiScreen == null && theWorld == null) {
            guiScreen = new GuiMainMenu();
        } else if (guiScreen == null && thePlayer.getHealth() <= 0.0f) {
            guiScreen = new GuiGameOver();
        }
        if (guiScreen instanceof GuiMainMenu) {
            Minecraft.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages();
        }
        this.currentScreen = guiScreen;
        if (guiScreen != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledResolution = new ScaledResolution(this);
            int n = scaledResolution.getScaledWidth();
            int n2 = scaledResolution.getScaledHeight();
            guiScreen.setWorldAndResolution(this, n, n2);
            this.skipRenderWorld = false;
        } else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }

    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public Entity getRenderViewEntity() {
        return this.renderViewEntity;
    }

    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }

    public static int getDebugFPS() {
        return debugFPS;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public void toggleFullscreen() {
        try {
            Minecraft.gameSettings.fullScreen = this.fullscreen = !this.fullscreen;
            if (this.fullscreen) {
                this.updateDisplayMode();
                this.displayWidth = Display.getDisplayMode().getWidth();
                displayHeight = Display.getDisplayMode().getHeight();
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (displayHeight <= 0) {
                    displayHeight = 1;
                }
            } else {
                Display.setDisplayMode((DisplayMode)new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
                this.displayWidth = this.tempDisplayWidth;
                displayHeight = this.tempDisplayHeight;
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (displayHeight <= 0) {
                    displayHeight = 1;
                }
            }
            if (this.currentScreen != null) {
                this.resize(this.displayWidth, displayHeight);
            } else {
                this.updateFramebufferSize();
            }
            Display.setFullscreen((boolean)this.fullscreen);
            Display.setVSyncEnabled((boolean)Minecraft.gameSettings.enableVsync);
            this.updateDisplay();
        }
        catch (Exception exception) {
            logger.error("Couldn't toggle fullscreen", (Throwable)exception);
        }
    }

    public void loadWorld(WorldClient worldClient) {
        this.loadWorld(worldClient, "");
    }

    public PropertyMap getTwitchDetails() {
        return this.twitchDetails;
    }

    public void refreshResources() {
        ArrayList arrayList = Lists.newArrayList(this.defaultResourcePacks);
        for (ResourcePackRepository.Entry entry : this.mcResourcePackRepository.getRepositoryEntries()) {
            arrayList.add(entry.getResourcePack());
        }
        if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
            arrayList.add(this.mcResourcePackRepository.getResourcePackInstance());
        }
        try {
            this.mcResourceManager.reloadResources(arrayList);
        }
        catch (RuntimeException runtimeException) {
            logger.info("Caught error stitching, removing all assigned resourcepacks", (Throwable)runtimeException);
            arrayList.clear();
            arrayList.addAll(this.defaultResourcePacks);
            this.mcResourcePackRepository.setRepositories(Collections.emptyList());
            this.mcResourceManager.reloadResources(arrayList);
            Minecraft.gameSettings.resourcePacks.clear();
            Minecraft.gameSettings.field_183018_l.clear();
            gameSettings.saveOptions();
        }
        this.mcLanguageManager.parseLanguageMetadata(arrayList);
        if (this.renderGlobal != null) {
            this.renderGlobal.loadRenderers();
        }
    }

    public ServerData getCurrentServerData() {
        return this.currentServerData;
    }

    public <V> ListenableFuture<V> addScheduledTask(Callable<V> callable) {
        Validate.notNull(callable);
        if (!this.isCallingFromMinecraftThread()) {
            ListenableFutureTask listenableFutureTask = ListenableFutureTask.create(callable);
            Queue<FutureTask<?>> queue = this.scheduledTasks;
            synchronized (queue) {
                this.scheduledTasks.add((FutureTask<?>)listenableFutureTask);
                return listenableFutureTask;
            }
        }
        try {
            return Futures.immediateFuture(callable.call());
        }
        catch (Exception exception) {
            return Futures.immediateFailedCheckedFuture((Exception)exception);
        }
    }

    /*
     * Unable to fully structure code
     */
    public void runTick() throws IOException {
        block100: {
            block101: {
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
                if (!this.isGamePaused && Minecraft.theWorld != null) {
                    Minecraft.playerController.updateController();
                }
                this.mcProfiler.endStartSection("textures");
                if (!this.isGamePaused) {
                    this.renderEngine.tick();
                }
                if (this.currentScreen == null && Minecraft.thePlayer != null) {
                    if (Minecraft.thePlayer.getHealth() <= 0.0f) {
                        this.displayGuiScreen(null);
                    } else if (Minecraft.thePlayer.isPlayerSleeping() && Minecraft.theWorld != null) {
                        this.displayGuiScreen(new GuiSleepMP());
                    }
                } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !Minecraft.thePlayer.isPlayerSleeping()) {
                    this.displayGuiScreen(null);
                }
                if (this.currentScreen != null) {
                    this.leftClickCounter = 10000;
                }
                if (this.currentScreen != null) {
                    try {
                        this.currentScreen.handleInput();
                    }
                    catch (Throwable var1_1) {
                        var2_5 = CrashReport.makeCrashReport(var1_1, "Updating screen events");
                        var3_11 = var2_5.makeCategory("Affected screen");
                        var3_11.addCrashSectionCallable("Screen name", new Callable<String>(){

                            @Override
                            public String call() throws Exception {
                                return Minecraft.this.currentScreen.getClass().getCanonicalName();
                            }
                        });
                        throw new ReportedException(var2_5);
                    }
                    if (this.currentScreen != null) {
                        try {
                            this.currentScreen.updateScreen();
                        }
                        catch (Throwable var1_2) {
                            var2_6 = CrashReport.makeCrashReport(var1_2, "Ticking screen");
                            var3_12 = var2_6.makeCategory("Affected screen");
                            var3_12.addCrashSectionCallable("Screen name", new Callable<String>(){

                                @Override
                                public String call() throws Exception {
                                    return Minecraft.this.currentScreen.getClass().getCanonicalName();
                                }
                            });
                            throw new ReportedException(var2_6);
                        }
                    }
                }
                if (this.currentScreen != null && !this.currentScreen.allowUserInput) break block100;
                this.mcProfiler.endStartSection("mouse");
                while (Mouse.next()) {
                    var1_3 = Mouse.getEventButton();
                    KeyBinding.setKeyBindState(var1_3 - 100, Mouse.getEventButtonState());
                    if (Mouse.getEventButtonState()) {
                        if (Minecraft.thePlayer.isSpectator() && var1_3 == 2) {
                            this.ingameGUI.getSpectatorGui().func_175261_b();
                        } else {
                            KeyBinding.onTick(var1_3 - 100);
                        }
                    }
                    if ((var2_7 = Minecraft.getSystemTime() - this.systemTime) > 200L) continue;
                    var4_14 = Mouse.getEventDWheel();
                    if (var4_14 != 0) {
                        if (Minecraft.thePlayer.isSpectator()) {
                            v0 = var4_14 = var4_14 < 0 ? -1 : 1;
                            if (this.ingameGUI.getSpectatorGui().func_175262_a()) {
                                this.ingameGUI.getSpectatorGui().func_175259_b(-var4_14);
                            } else {
                                var5_15 = MathHelper.clamp_float(Minecraft.thePlayer.capabilities.getFlySpeed() + (float)var4_14 * 0.005f, 0.0f, 0.2f);
                                Minecraft.thePlayer.capabilities.setFlySpeed(var5_15);
                            }
                        } else {
                            Minecraft.thePlayer.inventory.changeCurrentItem(var4_14);
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
                    var1_3 = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
                    KeyBinding.setKeyBindState(var1_3, Keyboard.getEventKeyState());
                    if (Keyboard.getEventKeyState()) {
                        KeyBinding.onTick(var1_3);
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
                    this.dispatchKeypresses();
                    if (!Keyboard.getEventKeyState()) continue;
                    if (var1_3 == 62 && this.entityRenderer != null) {
                        this.entityRenderer.switchUseShader();
                    }
                    if (this.currentScreen != null) {
                        this.currentScreen.handleKeyboardInput();
                    } else {
                        var2_8 = new EventKey(var1_3);
                        var2_8.call();
                        Exodus.onEvent(var2_8);
                        if (var1_3 == 1) {
                            this.displayInGameMenu();
                        }
                        if (var1_3 == 32 && Keyboard.isKeyDown((int)61) && this.ingameGUI != null) {
                            this.ingameGUI.getChatGUI().clearChatMessages();
                        }
                        if (var1_3 == 31 && Keyboard.isKeyDown((int)61)) {
                            this.refreshResources();
                        }
                        if (var1_3 != 17 || Keyboard.isKeyDown((int)61)) {
                            // empty if block
                        }
                        if (var1_3 != 18 || Keyboard.isKeyDown((int)61)) {
                            // empty if block
                        }
                        if (var1_3 != 47 || Keyboard.isKeyDown((int)61)) {
                            // empty if block
                        }
                        if (var1_3 != 38 || Keyboard.isKeyDown((int)61)) {
                            // empty if block
                        }
                        if (var1_3 != 22 || Keyboard.isKeyDown((int)61)) {
                            // empty if block
                        }
                        if (var1_3 == 20 && Keyboard.isKeyDown((int)61)) {
                            this.refreshResources();
                        }
                        if (var1_3 == 33 && Keyboard.isKeyDown((int)61)) {
                            Minecraft.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() != false ? -1 : 1);
                        }
                        if (var1_3 == 30 && Keyboard.isKeyDown((int)61)) {
                            this.renderGlobal.loadRenderers();
                        }
                        if (var1_3 == 35 && Keyboard.isKeyDown((int)61)) {
                            Minecraft.gameSettings.advancedItemTooltips = Minecraft.gameSettings.advancedItemTooltips == false;
                            Minecraft.gameSettings.saveOptions();
                        }
                        if (var1_3 == 48 && Keyboard.isKeyDown((int)61)) {
                            this.renderManager.setDebugBoundingBox(this.renderManager.isDebugBoundingBox() == false);
                        }
                        if (var1_3 == 25 && Keyboard.isKeyDown((int)61)) {
                            Minecraft.gameSettings.pauseOnLostFocus = Minecraft.gameSettings.pauseOnLostFocus == false;
                            Minecraft.gameSettings.saveOptions();
                        }
                        if (var1_3 == 59) {
                            v1 = Minecraft.gameSettings.hideGUI = Minecraft.gameSettings.hideGUI == false;
                        }
                        if (var1_3 == 61) {
                            Minecraft.gameSettings.showDebugInfo = Minecraft.gameSettings.showDebugInfo == false;
                            Minecraft.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                            Minecraft.gameSettings.field_181657_aC = GuiScreen.isAltKeyDown();
                        }
                        if (Minecraft.gameSettings.keyBindTogglePerspective.isPressed()) {
                            ++Minecraft.gameSettings.thirdPersonView;
                            if (Minecraft.gameSettings.thirdPersonView > 2) {
                                Minecraft.gameSettings.thirdPersonView = 0;
                            }
                            if (Minecraft.gameSettings.thirdPersonView == 0) {
                                this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
                            } else if (Minecraft.gameSettings.thirdPersonView == 1) {
                                this.entityRenderer.loadEntityShader(null);
                            }
                            this.renderGlobal.setDisplayListEntitiesDirty();
                        }
                        if (Minecraft.gameSettings.keyBindSmoothCamera.isPressed()) {
                            v2 = Minecraft.gameSettings.smoothCamera = Minecraft.gameSettings.smoothCamera == false;
                        }
                    }
                    if (!Minecraft.gameSettings.showDebugInfo || !Minecraft.gameSettings.showDebugProfilerChart) continue;
                    if (var1_3 == 11) {
                        this.updateDebugProfilerName(0);
                    }
                    var2_9 = 0;
                    while (var2_9 < 9) {
                        if (var1_3 == 2 + var2_9) {
                            this.updateDebugProfilerName(var2_9 + 1);
                        }
                        ++var2_9;
                    }
                }
                var1_3 = 0;
                while (var1_3 < 9) {
                    if (Minecraft.gameSettings.keyBindsHotbar[var1_3].isPressed()) {
                        if (Minecraft.thePlayer.isSpectator()) {
                            this.ingameGUI.getSpectatorGui().func_175260_a(var1_3);
                        } else {
                            Minecraft.thePlayer.inventory.currentItem = var1_3;
                        }
                    }
                    ++var1_3;
                }
                var1_3 = Minecraft.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN ? 1 : 0;
                while (Minecraft.gameSettings.keyBindInventory.isPressed()) {
                    if (Minecraft.playerController.isRidingHorse()) {
                        Minecraft.thePlayer.sendHorseInventory();
                        continue;
                    }
                    this.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    this.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
                }
                while (Minecraft.gameSettings.keyBindDrop.isPressed()) {
                    if (Minecraft.thePlayer.isSpectator()) continue;
                    Minecraft.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                }
                while (Minecraft.gameSettings.keyBindChat.isPressed() && var1_3 != 0) {
                    this.displayGuiScreen(new GuiChat());
                }
                if (this.currentScreen == null && Minecraft.gameSettings.keyBindCommand.isPressed() && var1_3 != 0) {
                    this.displayGuiScreen(new GuiChat("/"));
                }
                if (!Minecraft.thePlayer.isUsingItem()) ** GOTO lbl196
                if (!Minecraft.gameSettings.keyBindUseItem.isKeyDown()) {
                    Minecraft.playerController.onStoppedUsingItem(Minecraft.thePlayer);
                }
                while (Minecraft.gameSettings.keyBindAttack.isPressed()) {
                }
                while (Minecraft.gameSettings.keyBindUseItem.isPressed()) {
                }
                while (Minecraft.gameSettings.keyBindPickBlock.isPressed()) {
                }
                break block101;
lbl-1000:
                // 1 sources

                {
                    this.clickMouse();
lbl196:
                    // 2 sources

                    ** while (Minecraft.gameSettings.keyBindAttack.isPressed())
                }
lbl197:
                // 2 sources

                while (Minecraft.gameSettings.keyBindUseItem.isPressed()) {
                    this.rightClickMouse();
                }
                while (Minecraft.gameSettings.keyBindPickBlock.isPressed()) {
                    this.middleClickMouse();
                }
            }
            if (Minecraft.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !Minecraft.thePlayer.isUsingItem()) {
                this.rightClickMouse();
            }
            this.sendClickBlockToController(this.currentScreen == null && Minecraft.gameSettings.keyBindAttack.isKeyDown() != false && this.inGameHasFocus != false);
        }
        if (Minecraft.theWorld != null) {
            if (Minecraft.thePlayer != null) {
                ++this.joinPlayerCounter;
                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    Minecraft.theWorld.joinEntityInSurroundings(Minecraft.thePlayer);
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
                if (Minecraft.theWorld.getLastLightningBolt() > 0) {
                    Minecraft.theWorld.setLastLightningBolt(Minecraft.theWorld.getLastLightningBolt() - 1);
                }
                Minecraft.theWorld.updateEntities();
            }
        } else if (this.entityRenderer.isShaderActive()) {
            this.entityRenderer.func_181022_b();
        }
        if (!this.isGamePaused) {
            this.mcMusicTicker.update();
            this.mcSoundHandler.update();
        }
        if (Minecraft.theWorld != null) {
            if (!this.isGamePaused) {
                Minecraft.theWorld.setAllowedSpawnTypes(Minecraft.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                try {
                    Minecraft.theWorld.tick();
                }
                catch (Throwable var1_4) {
                    var2_10 = CrashReport.makeCrashReport(var1_4, "Exception in world tick");
                    if (Minecraft.theWorld == null) {
                        var3_13 = var2_10.makeCategory("Affected level");
                        var3_13.addCrashSection("Problem", "Level is null!");
                    } else {
                        Minecraft.theWorld.addWorldInfoToCrashReport(var2_10);
                    }
                    throw new ReportedException(var2_10);
                }
            }
            this.mcProfiler.endStartSection("animateTick");
            if (!this.isGamePaused && Minecraft.theWorld != null) {
                Minecraft.theWorld.doVoidFogParticles(MathHelper.floor_double(Minecraft.thePlayer.posX), MathHelper.floor_double(Minecraft.thePlayer.posY), MathHelper.floor_double(Minecraft.thePlayer.posZ));
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

    public IntegratedServer getIntegratedServer() {
        return this.theIntegratedServer;
    }

    private void runGameLoop() throws IOException {
        long l = System.nanoTime();
        this.mcProfiler.startSection("root");
        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }
        if (this.isGamePaused && theWorld != null) {
            float f = this.timer.renderPartialTicks;
            this.timer.updateTimer();
            this.timer.renderPartialTicks = f;
        } else {
            this.timer.updateTimer();
        }
        this.mcProfiler.startSection("scheduledExecutables");
        Queue<FutureTask<?>> queue = this.scheduledTasks;
        synchronized (queue) {
            while (!this.scheduledTasks.isEmpty()) {
                Util.func_181617_a(this.scheduledTasks.poll(), logger);
            }
        }
        this.mcProfiler.endSection();
        long l2 = System.nanoTime();
        this.mcProfiler.startSection("tick");
        int n = 0;
        while (n < this.timer.elapsedTicks) {
            this.runTick();
            ++n;
        }
        this.mcProfiler.endStartSection("preRenderErrors");
        long l3 = System.nanoTime() - l2;
        this.checkGLError("Pre render");
        this.mcProfiler.endStartSection("sound");
        this.mcSoundHandler.setListener(thePlayer, this.timer.renderPartialTicks);
        this.mcProfiler.endSection();
        this.mcProfiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        this.framebufferMc.bindFramebuffer(true);
        this.mcProfiler.startSection("display");
        GlStateManager.enableTexture2D();
        EventTick eventTick = new EventTick();
        eventTick.call();
        Exodus.onEvent(eventTick);
        eventTick.setType(EventType.PRE);
        if (thePlayer != null && thePlayer.isEntityInsideOpaqueBlock()) {
            Minecraft.gameSettings.thirdPersonView = 0;
        }
        this.mcProfiler.endSection();
        if (!this.skipRenderWorld) {
            this.mcProfiler.endStartSection("gameRenderer");
            this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, l);
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
        if (Minecraft.gameSettings.showDebugInfo && Minecraft.gameSettings.showDebugProfilerChart && !Minecraft.gameSettings.hideGUI) {
            if (!this.mcProfiler.profilingEnabled) {
                this.mcProfiler.clearProfiling();
            }
            this.mcProfiler.profilingEnabled = true;
            this.displayDebugInfo(l3);
        } else {
            this.mcProfiler.profilingEnabled = false;
            this.prevFrameTime = System.nanoTime();
        }
        this.guiAchievement.updateAchievementWindow();
        this.framebufferMc.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.framebufferMc.framebufferRender(this.displayWidth, displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.entityRenderer.renderStreamIndicator(this.timer.renderPartialTicks);
        GlStateManager.popMatrix();
        this.mcProfiler.startSection("root");
        this.updateDisplay();
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
        this.isGamePaused = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic();
        long l4 = System.nanoTime();
        this.field_181542_y.func_181747_a(l4 - this.field_181543_z);
        this.field_181543_z = l4;
        while (Minecraft.getSystemTime() >= this.debugUpdateTime + 1000L) {
            debugFPS = this.fpsCounter;
            Object[] objectArray = new Object[8];
            objectArray[0] = debugFPS;
            objectArray[1] = RenderChunk.renderChunksUpdated;
            objectArray[2] = RenderChunk.renderChunksUpdated != 1 ? "s" : "";
            objectArray[3] = (float)Minecraft.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : Integer.valueOf(Minecraft.gameSettings.limitFramerate);
            objectArray[4] = Minecraft.gameSettings.enableVsync ? " vsync" : "";
            Object object = objectArray[5] = Minecraft.gameSettings.fancyGraphics ? "" : " fast";
            objectArray[6] = Minecraft.gameSettings.clouds == 0 ? "" : (Minecraft.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds");
            objectArray[7] = OpenGlHelper.useVbo() ? " vbo" : "";
            this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", objectArray);
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

    protected void checkWindowResize() {
        if (!this.fullscreen && Display.wasResized()) {
            int n = this.displayWidth;
            int n2 = displayHeight;
            this.displayWidth = Display.getWidth();
            displayHeight = Display.getHeight();
            if (this.displayWidth != n || displayHeight != n2) {
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (displayHeight <= 0) {
                    displayHeight = 1;
                }
                this.resize(this.displayWidth, displayHeight);
            }
        }
    }

    public TextureManager getTextureManager() {
        return this.renderEngine;
    }

    private static boolean isJvm64bit() {
        String[] stringArray;
        String[] stringArray2 = stringArray = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String string = stringArray2[n2];
            String string2 = System.getProperty(string);
            if (string2 != null && string2.contains("64")) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public void shutdownMinecraftApplet() {
        Exodus.INSTANCE.shutdown();
        this.stream.shutdownStream();
        logger.info("Stopping!");
        try {
            this.loadWorld(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        this.mcSoundHandler.unloadSounds();
        Display.destroy();
        if (!this.hasCrashed) {
            System.exit(0);
        }
        System.gc();
    }

    public final boolean isMoving() {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward == 0.0f) {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.moveStrafing == 0.0f) {
                return false;
            }
        }
        return true;
    }

    private void middleClickMouse() {
        if (this.objectMouseOver != null) {
            Item item;
            Object object;
            Object object2;
            boolean bl = Minecraft.thePlayer.capabilities.isCreativeMode;
            int n = 0;
            boolean bl2 = false;
            TileEntity tileEntity = null;
            if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                object2 = this.objectMouseOver.getBlockPos();
                object = theWorld.getBlockState((BlockPos)object2).getBlock();
                if (((Block)object).getMaterial() == Material.air) {
                    return;
                }
                item = ((Block)object).getItem(theWorld, (BlockPos)object2);
                if (item == null) {
                    return;
                }
                if (bl && GuiScreen.isCtrlKeyDown()) {
                    tileEntity = theWorld.getTileEntity((BlockPos)object2);
                }
                Block block = item instanceof ItemBlock && !((Block)object).isFlowerPot() ? Block.getBlockFromItem(item) : object;
                n = block.getDamageValue(theWorld, (BlockPos)object2);
                bl2 = item.getHasSubtypes();
            } else {
                if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !bl) {
                    return;
                }
                if (this.objectMouseOver.entityHit instanceof EntityPainting) {
                    item = Items.painting;
                } else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
                    item = Items.lead;
                } else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
                    object2 = (EntityItemFrame)this.objectMouseOver.entityHit;
                    object = ((EntityItemFrame)object2).getDisplayedItem();
                    if (object == null) {
                        item = Items.item_frame;
                    } else {
                        item = ((ItemStack)object).getItem();
                        n = ((ItemStack)object).getMetadata();
                        bl2 = true;
                    }
                } else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
                    object2 = (EntityMinecart)this.objectMouseOver.entityHit;
                    switch (((EntityMinecart)object2).getMinecartType()) {
                        case FURNACE: {
                            item = Items.furnace_minecart;
                            break;
                        }
                        case CHEST: {
                            item = Items.chest_minecart;
                            break;
                        }
                        case TNT: {
                            item = Items.tnt_minecart;
                            break;
                        }
                        case HOPPER: {
                            item = Items.hopper_minecart;
                            break;
                        }
                        case COMMAND_BLOCK: {
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
                    n = EntityList.getEntityID(this.objectMouseOver.entityHit);
                    bl2 = true;
                    if (!EntityList.entityEggs.containsKey(n)) {
                        return;
                    }
                }
            }
            object2 = Minecraft.thePlayer.inventory;
            if (tileEntity == null) {
                ((InventoryPlayer)object2).setCurrentItem(item, n, bl2, bl);
            } else {
                object = this.func_181036_a(item, n, tileEntity);
                ((InventoryPlayer)object2).setInventorySlotContents(((InventoryPlayer)object2).currentItem, (ItemStack)object);
            }
            if (bl) {
                int n2 = Minecraft.thePlayer.inventoryContainer.inventorySlots.size() - 9 + ((InventoryPlayer)object2).currentItem;
                playerController.sendSlotPacket(((InventoryPlayer)object2).getStackInSlot(((InventoryPlayer)object2).currentItem), n2);
            }
        }
    }

    @Override
    public boolean isSnooperEnabled() {
        return Minecraft.gameSettings.snooperEnabled;
    }

    public MusicTicker.MusicType getAmbientMusicType() {
        return thePlayer != null ? (Minecraft.thePlayer.worldObj.provider instanceof WorldProviderHell ? MusicTicker.MusicType.NETHER : (Minecraft.thePlayer.worldObj.provider instanceof WorldProviderEnd ? (BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END) : (Minecraft.thePlayer.capabilities.isCreativeMode && Minecraft.thePlayer.capabilities.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME))) : MusicTicker.MusicType.MENU;
    }

    public Minecraft(GameConfiguration gameConfiguration) {
        this.systemTime = Minecraft.getSystemTime();
        this.field_181542_y = new FrameTimer();
        this.field_181543_z = System.nanoTime();
        this.mcProfiler = new Profiler();
        this.metadataSerializer_ = new IMetadataSerializer();
        this.defaultResourcePacks = Lists.newArrayList();
        this.scheduledTasks = Queues.newArrayDeque();
        this.mcThread = Thread.currentThread();
        this.debugUpdateTime = Minecraft.getSystemTime();
        theMinecraft = this;
        this.mcDataDir = gameConfiguration.folderInfo.mcDataDir;
        this.fileAssets = gameConfiguration.folderInfo.assetsDir;
        this.fileResourcepacks = gameConfiguration.folderInfo.resourcePacksDir;
        this.launchedVersion = gameConfiguration.gameInfo.version;
        this.twitchDetails = gameConfiguration.userInfo.userProperties;
        this.field_181038_N = gameConfiguration.userInfo.field_181172_c;
        this.mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(gameConfiguration.folderInfo.assetsDir, gameConfiguration.folderInfo.assetIndex).getResourceMap());
        this.proxy = gameConfiguration.userInfo.proxy == null ? Proxy.NO_PROXY : gameConfiguration.userInfo.proxy;
        this.sessionService = new YggdrasilAuthenticationService(gameConfiguration.userInfo.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
        this.session = gameConfiguration.userInfo.session;
        logger.info("Setting user: " + this.session.getUsername());
        logger.info("(Session ID is " + this.session.getSessionID() + ")");
        this.isDemo = gameConfiguration.gameInfo.isDemo;
        this.displayWidth = gameConfiguration.displayInfo.width > 0 ? gameConfiguration.displayInfo.width : 1;
        displayHeight = gameConfiguration.displayInfo.height > 0 ? gameConfiguration.displayInfo.height : 1;
        this.tempDisplayWidth = gameConfiguration.displayInfo.width;
        this.tempDisplayHeight = gameConfiguration.displayInfo.height;
        this.fullscreen = gameConfiguration.displayInfo.fullscreen;
        this.jvm64bit = Minecraft.isJvm64bit();
        this.theIntegratedServer = new IntegratedServer(this);
        if (gameConfiguration.serverInfo.serverName != null) {
            this.serverName = gameConfiguration.serverInfo.serverName;
            this.serverPort = gameConfiguration.serverInfo.serverPort;
        }
        ImageIO.setUseCache(false);
        Bootstrap.register();
    }

    private void initStream() {
        try {
            this.stream = new TwitchStream(this, (Property)Iterables.getFirst((Iterable)this.twitchDetails.get((Object)"twitch_access_token"), null));
        }
        catch (Throwable throwable) {
            this.stream = new NullStream(throwable);
            logger.error("Couldn't initialize twitch stream");
        }
    }

    public boolean isJava64bit() {
        return this.jvm64bit;
    }

    public NetHandlerPlayClient getNetHandler() {
        return thePlayer != null ? Minecraft.thePlayer.sendQueue : null;
    }

    /*
     * Unable to fully structure code
     */
    public void run() {
        this.running = true;
        try {
            this.startGame();
            if (true) ** GOTO lbl22
        }
        catch (Throwable var1_1) {
            var2_3 = CrashReport.makeCrashReport(var1_1, "Initializing game");
            var2_3.makeCategory("Initialization");
            this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(var2_3));
            return;
        }
        {
            do {
                if (!this.hasCrashed || this.crashReporter == null) {
                    try {
                        this.runGameLoop();
                    }
                    catch (OutOfMemoryError var1_2) {
                        this.freeMemory();
                        this.displayGuiScreen(new GuiMemoryErrorScreen());
                        System.gc();
                    }
                    continue;
                }
                this.displayCrashReport(this.crashReporter);
lbl22:
                // 4 sources

            } while (this.running);
        }
        this.shutdownMinecraftApplet();
    }

    private ByteBuffer readImageToBuffer(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        int[] nArray = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 * nArray.length);
        int[] nArray2 = nArray;
        int n = nArray.length;
        int n2 = 0;
        while (n2 < n) {
            int n3 = nArray2[n2];
            byteBuffer.putInt(n3 << 8 | n3 >> 24 & 0xFF);
            ++n2;
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public void displayCrashReport(CrashReport crashReport) {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "crash-reports");
        File file2 = new File(file, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT(crashReport.getCompleteReport());
        if (crashReport.getFile() != null) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReport.getFile());
            System.exit(-1);
        } else if (crashReport.saveToFile(file2)) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            System.exit(-1);
        } else {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }

    public void setRenderViewEntity(Entity entity) {
        this.renderViewEntity = entity;
        this.entityRenderer.loadEntityShader(entity);
    }

    @Override
    public void addServerTypeToSnooper(PlayerUsageSnooper playerUsageSnooper) {
        playerUsageSnooper.addStatToSnooper("opengl_version", GL11.glGetString((int)7938));
        playerUsageSnooper.addStatToSnooper("opengl_vendor", GL11.glGetString((int)7936));
        playerUsageSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        playerUsageSnooper.addStatToSnooper("launched_version", this.launchedVersion);
        ContextCapabilities contextCapabilities = GLContext.getCapabilities();
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", contextCapabilities.GL_ARB_arrays_of_arrays);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", contextCapabilities.GL_ARB_base_instance);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", contextCapabilities.GL_ARB_blend_func_extended);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", contextCapabilities.GL_ARB_clear_buffer_object);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", contextCapabilities.GL_ARB_color_buffer_float);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", contextCapabilities.GL_ARB_compatibility);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", contextCapabilities.GL_ARB_compressed_texture_pixel_storage);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextCapabilities.GL_ARB_compute_shader);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextCapabilities.GL_ARB_copy_buffer);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextCapabilities.GL_ARB_copy_image);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextCapabilities.GL_ARB_depth_buffer_float);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextCapabilities.GL_ARB_compute_shader);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextCapabilities.GL_ARB_copy_buffer);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextCapabilities.GL_ARB_copy_image);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextCapabilities.GL_ARB_depth_buffer_float);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", contextCapabilities.GL_ARB_depth_clamp);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", contextCapabilities.GL_ARB_depth_texture);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", contextCapabilities.GL_ARB_draw_buffers);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", contextCapabilities.GL_ARB_draw_buffers_blend);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", contextCapabilities.GL_ARB_draw_elements_base_vertex);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", contextCapabilities.GL_ARB_draw_indirect);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", contextCapabilities.GL_ARB_draw_instanced);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", contextCapabilities.GL_ARB_explicit_attrib_location);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", contextCapabilities.GL_ARB_explicit_uniform_location);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", contextCapabilities.GL_ARB_fragment_layer_viewport);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", contextCapabilities.GL_ARB_fragment_program);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", contextCapabilities.GL_ARB_fragment_shader);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", contextCapabilities.GL_ARB_fragment_program_shadow);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", contextCapabilities.GL_ARB_framebuffer_object);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", contextCapabilities.GL_ARB_framebuffer_sRGB);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", contextCapabilities.GL_ARB_geometry_shader4);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", contextCapabilities.GL_ARB_gpu_shader5);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", contextCapabilities.GL_ARB_half_float_pixel);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", contextCapabilities.GL_ARB_half_float_vertex);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", contextCapabilities.GL_ARB_instanced_arrays);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", contextCapabilities.GL_ARB_map_buffer_alignment);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", contextCapabilities.GL_ARB_map_buffer_range);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_multisample]", contextCapabilities.GL_ARB_multisample);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", contextCapabilities.GL_ARB_multitexture);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", contextCapabilities.GL_ARB_occlusion_query2);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", contextCapabilities.GL_ARB_pixel_buffer_object);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", contextCapabilities.GL_ARB_seamless_cube_map);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", contextCapabilities.GL_ARB_shader_objects);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", contextCapabilities.GL_ARB_shader_stencil_export);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", contextCapabilities.GL_ARB_shader_texture_lod);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_shadow]", contextCapabilities.GL_ARB_shadow);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", contextCapabilities.GL_ARB_shadow_ambient);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", contextCapabilities.GL_ARB_stencil_texturing);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_sync]", contextCapabilities.GL_ARB_sync);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", contextCapabilities.GL_ARB_tessellation_shader);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", contextCapabilities.GL_ARB_texture_border_clamp);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", contextCapabilities.GL_ARB_texture_buffer_object);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", contextCapabilities.GL_ARB_texture_cube_map);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", contextCapabilities.GL_ARB_texture_cube_map_array);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", contextCapabilities.GL_ARB_texture_non_power_of_two);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", contextCapabilities.GL_ARB_uniform_buffer_object);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", contextCapabilities.GL_ARB_vertex_blend);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", contextCapabilities.GL_ARB_vertex_buffer_object);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", contextCapabilities.GL_ARB_vertex_program);
        playerUsageSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", contextCapabilities.GL_ARB_vertex_shader);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", contextCapabilities.GL_EXT_bindable_uniform);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", contextCapabilities.GL_EXT_blend_equation_separate);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", contextCapabilities.GL_EXT_blend_func_separate);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", contextCapabilities.GL_EXT_blend_minmax);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", contextCapabilities.GL_EXT_blend_subtract);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", contextCapabilities.GL_EXT_draw_instanced);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", contextCapabilities.GL_EXT_framebuffer_multisample);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", contextCapabilities.GL_EXT_framebuffer_object);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", contextCapabilities.GL_EXT_framebuffer_sRGB);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", contextCapabilities.GL_EXT_geometry_shader4);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", contextCapabilities.GL_EXT_gpu_program_parameters);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", contextCapabilities.GL_EXT_gpu_shader4);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", contextCapabilities.GL_EXT_multi_draw_arrays);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", contextCapabilities.GL_EXT_packed_depth_stencil);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", contextCapabilities.GL_EXT_paletted_texture);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", contextCapabilities.GL_EXT_rescale_normal);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", contextCapabilities.GL_EXT_separate_shader_objects);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", contextCapabilities.GL_EXT_shader_image_load_store);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", contextCapabilities.GL_EXT_shadow_funcs);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", contextCapabilities.GL_EXT_shared_texture_palette);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", contextCapabilities.GL_EXT_stencil_clear_tag);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", contextCapabilities.GL_EXT_stencil_two_side);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", contextCapabilities.GL_EXT_stencil_wrap);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", contextCapabilities.GL_EXT_texture_3d);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", contextCapabilities.GL_EXT_texture_array);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", contextCapabilities.GL_EXT_texture_buffer_object);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", contextCapabilities.GL_EXT_texture_integer);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", contextCapabilities.GL_EXT_texture_lod_bias);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", contextCapabilities.GL_EXT_texture_sRGB);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", contextCapabilities.GL_EXT_vertex_shader);
        playerUsageSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", contextCapabilities.GL_EXT_vertex_weighting);
        playerUsageSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GL11.glGetInteger((int)35658));
        GL11.glGetError();
        playerUsageSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GL11.glGetInteger((int)35657));
        GL11.glGetError();
        playerUsageSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GL11.glGetInteger((int)34921));
        GL11.glGetError();
        playerUsageSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GL11.glGetInteger((int)35660));
        GL11.glGetError();
        playerUsageSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger((int)34930));
        GL11.glGetError();
        playerUsageSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger((int)35071));
        GL11.glGetError();
        playerUsageSnooper.addStatToSnooper("gl_max_texture_size", Minecraft.getGLMaximumTextureSize());
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

    public IStream getTwitchStream() {
        return this.stream;
    }

    public static Minecraft getMinecraft() {
        return theMinecraft;
    }

    @Override
    public ListenableFuture<Object> addScheduledTask(Runnable runnable) {
        Validate.notNull((Object)runnable);
        return this.addScheduledTask(Executors.callable(runnable));
    }

    public IResourceManager getResourceManager() {
        return this.mcResourceManager;
    }

    private void rightClickMouse() {
        if (!playerController.func_181040_m()) {
            Object object;
            this.rightClickDelayTimer = 4;
            boolean bl = true;
            ItemStack itemStack = Minecraft.thePlayer.inventory.getCurrentItem();
            if (this.objectMouseOver == null) {
                logger.warn("Null returned as 'hitResult', this shouldn't happen!");
            } else {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY: {
                        if (playerController.func_178894_a(thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver)) {
                            bl = false;
                            break;
                        }
                        if (!playerController.interactWithEntitySendPacket(thePlayer, this.objectMouseOver.entityHit)) break;
                        bl = false;
                        break;
                    }
                    case BLOCK: {
                        int n;
                        object = this.objectMouseOver.getBlockPos();
                        if (theWorld.getBlockState((BlockPos)object).getBlock().getMaterial() == Material.air) break;
                        int n2 = n = itemStack != null ? itemStack.stackSize : 0;
                        if (playerController.onPlayerRightClick(thePlayer, theWorld, itemStack, (BlockPos)object, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec)) {
                            bl = false;
                            thePlayer.swingItem();
                        }
                        if (itemStack == null) {
                            return;
                        }
                        if (itemStack.stackSize == 0) {
                            Minecraft.thePlayer.inventory.mainInventory[Minecraft.thePlayer.inventory.currentItem] = null;
                            break;
                        }
                        if (itemStack.stackSize == n && !playerController.isInCreativeMode()) break;
                        this.entityRenderer.itemRenderer.resetEquippedProgress();
                    }
                }
            }
            if (bl && (object = Minecraft.thePlayer.inventory.getCurrentItem()) != null && playerController.sendUseItem(thePlayer, theWorld, (ItemStack)object)) {
                this.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
        }
    }

    @Override
    public void addServerStatsToSnooper(PlayerUsageSnooper playerUsageSnooper) {
        playerUsageSnooper.addClientStat("fps", debugFPS);
        playerUsageSnooper.addClientStat("vsync_enabled", Minecraft.gameSettings.enableVsync);
        playerUsageSnooper.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
        playerUsageSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
        playerUsageSnooper.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - playerUsageSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerUsageSnooper.addClientStat("current_action", this.func_181538_aA());
        String string = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
        playerUsageSnooper.addClientStat("endianness", string);
        playerUsageSnooper.addClientStat("resource_packs", this.mcResourcePackRepository.getRepositoryEntries().size());
        int n = 0;
        for (ResourcePackRepository.Entry entry : this.mcResourcePackRepository.getRepositoryEntries()) {
            playerUsageSnooper.addClientStat("resource_pack[" + n++ + "]", entry.getResourcePackName());
        }
        if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null) {
            playerUsageSnooper.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }

    public void setIngameFocus() {
        if (Display.isActive() && !this.inGameHasFocus) {
            this.inGameHasFocus = true;
            this.mouseHelper.grabMouseCursor();
            this.displayGuiScreen(null);
            this.leftClickCounter = 10000;
        }
    }

    public ListenableFuture<Object> scheduleResourcesRefresh() {
        return this.addScheduledTask(new Runnable(){

            @Override
            public void run() {
                Minecraft.this.refreshResources();
            }
        });
    }

    private void checkGLError(String string) {
        int n;
        if (this.enableGLErrorChecking && (n = GL11.glGetError()) != 0) {
            String string2 = GLU.gluErrorString((int)n);
            logger.error("########## GL ERROR ##########");
            logger.error("@ " + string);
            logger.error(String.valueOf(n) + ": " + string2);
        }
    }

    public Framebuffer getFramebuffer() {
        return this.framebufferMc;
    }

    private void drawSplashScreen(TextureManager textureManager) throws LWJGLException {
        Framebuffer framebuffer;
        int n;
        ScaledResolution scaledResolution;
        block2: {
            scaledResolution = new ScaledResolution(this);
            n = scaledResolution.getScaleFactor();
            framebuffer = new Framebuffer(scaledResolution.getScaledWidth() * n, scaledResolution.getScaledHeight() * n, true);
            framebuffer.bindFramebuffer(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();
            InputStream inputStream = null;
            try {
                inputStream = this.mcDefaultResourcePack.getInputStream(locationMojangPng);
                this.mojangLogo = textureManager.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputStream)));
                textureManager.bindTexture(this.mojangLogo);
            }
            catch (IOException iOException) {
                logger.error("Unable to load logo: " + locationMojangPng, (Throwable)iOException);
                IOUtils.closeQuietly((InputStream)inputStream);
                break block2;
            }
            IOUtils.closeQuietly((InputStream)inputStream);
        }
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRenderer.pos(0.0, displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldRenderer.pos(this.displayWidth, displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldRenderer.pos(this.displayWidth, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldRenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int n2 = 256;
        int n3 = 256;
        this.func_181536_a((scaledResolution.getScaledWidth() - n2) / 2, (scaledResolution.getScaledHeight() - n3) / 2, 0, 0, n2, n3, 255, 255, 255, 255);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledResolution.getScaledWidth() * n, scaledResolution.getScaledHeight() * n);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        this.updateDisplay();
    }

    public final boolean isDemo() {
        return this.isDemo;
    }

    public static void stopIntegratedServer() {
        IntegratedServer integratedServer;
        if (theMinecraft != null && (integratedServer = theMinecraft.getIntegratedServer()) != null) {
            integratedServer.stopServer();
        }
    }

    public SkinManager getSkinManager() {
        return this.skinManager;
    }

    public void func_181537_a(boolean bl) {
        this.field_181541_X = bl;
    }

    public MusicTicker func_181535_r() {
        return this.mcMusicTicker;
    }

    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport crashReport) {
        crashReport.getCategory().addCrashSectionCallable("Launched Version", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Minecraft.this.launchedVersion;
            }
        });
        crashReport.getCategory().addCrashSectionCallable("LWJGL", new Callable<String>(){

            @Override
            public String call() {
                return Sys.getVersion();
            }
        });
        crashReport.getCategory().addCrashSectionCallable("OpenGL", new Callable<String>(){

            @Override
            public String call() {
                return String.valueOf(GL11.glGetString((int)7937)) + " GL version " + GL11.glGetString((int)7938) + ", " + GL11.glGetString((int)7936);
            }
        });
        crashReport.getCategory().addCrashSectionCallable("GL Caps", new Callable<String>(){

            @Override
            public String call() {
                return OpenGlHelper.getLogText();
            }
        });
        crashReport.getCategory().addCrashSectionCallable("Using VBOs", new Callable<String>(){

            @Override
            public String call() {
                return Minecraft.gameSettings.useVbo ? "Yes" : "No";
            }
        });
        crashReport.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>(){

            @Override
            public String call() throws Exception {
                String string = ClientBrandRetriever.getClientModName();
                return !string.equals("vanilla") ? "Definitely; Client brand changed to '" + string + "'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.");
            }
        });
        crashReport.getCategory().addCrashSectionCallable("Type", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return "Client (map_client.txt)";
            }
        });
        crashReport.getCategory().addCrashSectionCallable("Resource Packs", new Callable<String>(){

            @Override
            public String call() throws Exception {
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : Minecraft.gameSettings.resourcePacks) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(string);
                    if (!Minecraft.gameSettings.field_183018_l.contains(string)) continue;
                    stringBuilder.append(" (incompatible)");
                }
                return stringBuilder.toString();
            }
        });
        crashReport.getCategory().addCrashSectionCallable("Current Language", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
            }
        });
        crashReport.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });
        crashReport.getCategory().addCrashSectionCallable("CPU", new Callable<String>(){

            @Override
            public String call() {
                return OpenGlHelper.func_183029_j();
            }
        });
        if (theWorld != null) {
            theWorld.addWorldInfoToCrashReport(crashReport);
        }
        return crashReport;
    }

    public void launchIntegratedServer(String string, String string2, WorldSettings worldSettings) {
        Object object;
        this.loadWorld(null);
        System.gc();
        ISaveHandler iSaveHandler = this.saveLoader.getSaveLoader(string, false);
        WorldInfo worldInfo = iSaveHandler.loadWorldInfo();
        if (worldInfo == null && worldSettings != null) {
            worldInfo = new WorldInfo(worldSettings, string);
            iSaveHandler.saveWorldInfo(worldInfo);
        }
        if (worldSettings == null) {
            worldSettings = new WorldSettings(worldInfo);
        }
        try {
            this.theIntegratedServer = new IntegratedServer(this, string, string2, worldSettings);
            this.theIntegratedServer.startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Starting integrated server");
            crashReportCategory.addCrashSection("Level ID", string);
            crashReportCategory.addCrashSection("Level Name", string2);
            throw new ReportedException(crashReport);
        }
        this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));
        while (!this.theIntegratedServer.serverIsInRunLoop()) {
            object = this.theIntegratedServer.getUserMessage();
            if (object != null) {
                this.loadingScreen.displayLoadingString(I18n.format((String)object, new Object[0]));
            } else {
                this.loadingScreen.displayLoadingString("");
            }
            try {
                Thread.sleep(200L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        this.displayGuiScreen(null);
        object = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager networkManager = NetworkManager.provideLocalClient((SocketAddress)object);
        networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, this, null));
        networkManager.sendPacket(new C00Handshake(47, object.toString(), 0, EnumConnectionState.LOGIN));
        networkManager.sendPacket(new C00PacketLoginStart(this.getSession().getProfile()));
        this.myNetworkManager = networkManager;
    }

    public BlockRendererDispatcher getBlockRendererDispatcher() {
        return this.blockRenderDispatcher;
    }

    public void updateDisplay() {
        this.mcProfiler.startSection("display_update");
        Display.update();
        this.mcProfiler.endSection();
        this.checkWindowResize();
    }

    private void updateDebugProfilerName(int n) {
        List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);
        if (list != null && !list.isEmpty()) {
            Profiler.Result result = list.remove(0);
            if (n == 0) {
                int n2;
                if (result.field_76331_c.length() > 0 && (n2 = this.debugProfilerName.lastIndexOf(".")) >= 0) {
                    this.debugProfilerName = this.debugProfilerName.substring(0, n2);
                }
            } else if (--n < list.size() && !list.get((int)n).field_76331_c.equals("unspecified")) {
                if (this.debugProfilerName.length() > 0) {
                    this.debugProfilerName = String.valueOf(this.debugProfilerName) + ".";
                }
                this.debugProfilerName = String.valueOf(this.debugProfilerName) + list.get((int)n).field_76331_c;
            }
        }
    }

    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            KeyBinding.unPressAllKeys();
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }

    public void func_181536_a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10) {
        float f = 0.00390625f;
        float f2 = 0.00390625f;
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRenderer.pos(n, n2 + n6, 0.0).tex((float)n3 * f, (float)(n4 + n6) * f2).color(n7, n8, n9, n10).endVertex();
        worldRenderer.pos(n + n5, n2 + n6, 0.0).tex((float)(n3 + n5) * f, (float)(n4 + n6) * f2).color(n7, n8, n9, n10).endVertex();
        worldRenderer.pos(n + n5, n2, 0.0).tex((float)(n3 + n5) * f, (float)n4 * f2).color(n7, n8, n9, n10).endVertex();
        worldRenderer.pos(n, n2, 0.0).tex((float)n3 * f, (float)n4 * f2).color(n7, n8, n9, n10).endVertex();
        Tessellator.getInstance().draw();
    }

    public RenderItem getRenderItem() {
        return this.renderItem;
    }

    public void loadWorld(WorldClient worldClient, String string) {
        if (worldClient == null) {
            NetHandlerPlayClient netHandlerPlayClient = this.getNetHandler();
            if (netHandlerPlayClient != null) {
                netHandlerPlayClient.cleanup();
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
            this.loadingScreen.resetProgressAndMessage(string);
            this.loadingScreen.displayLoadingString("");
        }
        if (worldClient == null && theWorld != null) {
            this.mcResourcePackRepository.func_148529_f();
            this.ingameGUI.func_181029_i();
            this.setServerData(null);
            this.integratedServerIsRunning = false;
        }
        this.mcSoundHandler.stopSounds();
        theWorld = worldClient;
        if (worldClient != null) {
            if (this.renderGlobal != null) {
                this.renderGlobal.setWorldAndLoadRenderers(worldClient);
            }
            if (this.effectRenderer != null) {
                this.effectRenderer.clearEffects(worldClient);
            }
            if (thePlayer == null) {
                thePlayer = playerController.func_178892_a(worldClient, new StatFileWriter());
                playerController.flipPlayer(thePlayer);
            }
            thePlayer.preparePlayerToSpawn();
            worldClient.spawnEntityInWorld(thePlayer);
            Minecraft.thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
            playerController.setPlayerCapabilities(thePlayer);
            this.renderViewEntity = thePlayer;
        } else {
            this.saveLoader.flushCache();
            thePlayer = null;
        }
        System.gc();
        this.systemTime = 0L;
    }

    public boolean isGamePaused() {
        return this.isGamePaused;
    }

    public PlayerUsageSnooper getPlayerUsageSnooper() {
        return this.usageSnooper;
    }

    private String func_181538_aA() {
        return this.theIntegratedServer != null ? (this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer") : (this.currentServerData != null ? (this.currentServerData.func_181041_d() ? "playing_lan" : "multiplayer") : "out_of_game");
    }

    private void createDisplay() throws LWJGLException {
        Display.setResizable((boolean)true);
        Display.setTitle((String)"Loading...");
        try {
            Display.create((PixelFormat)new PixelFormat().withDepthBits(24));
        }
        catch (LWJGLException lWJGLException) {
            logger.error("Couldn't set pixel format", (Throwable)lWJGLException);
            try {
                Thread.sleep(1000L);
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

    private void registerMetadataSerializers() {
        this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }

    public ResourcePackRepository getResourcePackRepository() {
        return this.mcResourcePackRepository;
    }

    private void sendClickBlockToController(boolean bl) {
        if (!bl) {
            this.leftClickCounter = 0;
        }
        if (this.leftClickCounter <= 0 && !thePlayer.isUsingItem()) {
            if (bl && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = this.objectMouseOver.getBlockPos();
                if (theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air && playerController.onPlayerDamageBlock(blockPos, this.objectMouseOver.sideHit)) {
                    this.effectRenderer.addBlockHitEffects(blockPos, this.objectMouseOver.sideHit);
                    thePlayer.swingItem();
                }
            } else {
                playerController.resetBlockRemoving();
            }
        }
    }

    static {
        logger = LogManager.getLogger();
        locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
        isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;
        memoryReserve = new byte[0xA00000];
        macDisplayModes = Lists.newArrayList((Object[])new DisplayMode[]{new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)});
    }

    public boolean isIntegratedServerRunning() {
        return this.integratedServerIsRunning;
    }

    public RenderManager getRenderManager() {
        return this.renderManager;
    }

    public FrameTimer func_181539_aj() {
        return this.field_181542_y;
    }

    public void shutdown() {
        this.running = false;
    }

    public static boolean isAmbientOcclusionEnabled() {
        return theMinecraft != null && Minecraft.gameSettings.ambientOcclusion != 0;
    }
}

