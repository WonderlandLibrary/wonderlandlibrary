// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.settings;

import net.minecraft.world.World;
import net.optifine.ClearWater;
import net.optifine.shaders.Shaders;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import net.optifine.CustomGuis;
import net.minecraft.client.renderer.OpenGlHelper;
import net.optifine.DynamicLights;
import net.minecraft.util.MathHelper;
import net.optifine.NaturalTextures;
import net.optifine.CustomSky;
import net.optifine.RandomEntities;
import net.optifine.CustomColors;
import java.util.Arrays;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C15PacketClientSettings;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import net.optifine.reflect.Reflector;
import org.apache.commons.io.IOUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.client.gui.GuiNewChat;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import net.optifine.Lang;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.resources.I18n;
import net.optifine.util.KeyUtils;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.src.Config;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import java.lang.reflect.Type;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.EnumDifficulty;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import java.util.Map;
import net.minecraft.entity.player.EnumPlayerModelParts;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import java.lang.reflect.ParameterizedType;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class GameSettings
{
    private static final Logger logger;
    private static final Gson gson;
    private static final ParameterizedType typeListString;
    private static final String[] GUISCALES;
    private static final String[] PARTICLES;
    private static final String[] AMBIENT_OCCLUSIONS;
    private static final String[] STREAM_COMPRESSIONS;
    private static final String[] STREAM_CHAT_MODES;
    private static final String[] STREAM_CHAT_FILTER_MODES;
    private static final String[] STREAM_MIC_MODES;
    private static final String[] CLOUDS_TYPES;
    public float mouseSensitivity;
    public boolean invertMouse;
    public int renderDistanceChunks;
    public boolean viewBobbing;
    public boolean anaglyph;
    public boolean fboEnable;
    public int limitFramerate;
    public int clouds;
    public boolean fancyGraphics;
    public int ambientOcclusion;
    public List<String> resourcePacks;
    public List<String> incompatibleResourcePacks;
    public EntityPlayer.EnumChatVisibility chatVisibility;
    public boolean chatColours;
    public boolean chatLinks;
    public boolean chatLinksPrompt;
    public float chatOpacity;
    public boolean snooperEnabled;
    public boolean fullScreen;
    public boolean enableVsync;
    public boolean useVbo;
    public boolean allowBlockAlternatives;
    public boolean reducedDebugInfo;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus;
    private final Set<EnumPlayerModelParts> setModelParts;
    public boolean touchscreen;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips;
    public float chatScale;
    public float chatWidth;
    public float chatHeightUnfocused;
    public float chatHeightFocused;
    public boolean showInventoryAchievementHint;
    public int mipmapLevels;
    private Map<SoundCategory, Float> mapSoundLevels;
    public float streamBytesPerPixel;
    public float streamMicVolume;
    public float streamGameVolume;
    public float streamKbps;
    public float streamFps;
    public int streamCompression;
    public boolean streamSendMetadata;
    public String streamPreferredServer;
    public int streamChatEnabled;
    public int streamChatUserFilter;
    public int streamMicToggleBehavior;
    public boolean useNativeTransport;
    public boolean entityShadows;
    public boolean realmsNotifications;
    public KeyBinding keyBindForward;
    public KeyBinding keyBindLeft;
    public KeyBinding keyBindBack;
    public KeyBinding keyBindRight;
    public KeyBinding keyBindJump;
    public KeyBinding keyBindSneak;
    public KeyBinding keyBindSprint;
    public KeyBinding keyBindInventory;
    public KeyBinding keyBindUseItem;
    public KeyBinding keyBindDrop;
    public KeyBinding keyBindAttack;
    public KeyBinding keyBindPickBlock;
    public KeyBinding keyBindChat;
    public KeyBinding keyBindPlayerList;
    public KeyBinding keyBindCommand;
    public KeyBinding keyBindScreenshot;
    public KeyBinding keyBindTogglePerspective;
    public KeyBinding keyBindSmoothCamera;
    public KeyBinding keyBindFullscreen;
    public KeyBinding keyBindSpectatorOutlines;
    public KeyBinding keyBindStreamStartStop;
    public KeyBinding keyBindStreamPauseUnpause;
    public KeyBinding keyBindStreamCommercials;
    public KeyBinding keyBindStreamToggleMic;
    public KeyBinding[] keyBindsHotbar;
    public KeyBinding[] keyBindings;
    protected Minecraft mc;
    private File optionsFile;
    public EnumDifficulty difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public boolean showLagometer;
    public String lastServer;
    public boolean smoothCamera;
    public boolean debugCamEnable;
    public float fovSetting;
    public float gammaSetting;
    public float saturation;
    public int guiScale;
    public int particleSetting;
    public String language;
    public boolean forceUnicodeFont;
    public int ofFogType;
    public float ofFogStart;
    public int ofMipmapType;
    public boolean ofOcclusionFancy;
    public boolean ofSmoothFps;
    public boolean ofSmoothWorld;
    public boolean ofLazyChunkLoading;
    public boolean ofRenderRegions;
    public boolean ofSmartAnimations;
    public float ofAoLevel;
    public int ofAaLevel;
    public int ofAfLevel;
    public int ofClouds;
    public float ofCloudsHeight;
    public int ofTrees;
    public int ofRain;
    public int ofDroppedItems;
    public int ofBetterGrass;
    public int ofAutoSaveTicks;
    public boolean ofLagometer;
    public boolean ofProfiler;
    public boolean ofShowFps;
    public boolean ofWeather;
    public boolean ofSky;
    public boolean ofStars;
    public boolean ofSunMoon;
    public int ofVignette;
    public int ofChunkUpdates;
    public boolean ofChunkUpdatesDynamic;
    public int ofTime;
    public boolean ofClearWater;
    public boolean ofBetterSnow;
    public String ofFullscreenMode;
    public boolean ofSwampColors;
    public boolean ofRandomEntities;
    public boolean ofSmoothBiomes;
    public boolean ofCustomFonts;
    public boolean ofCustomColors;
    public boolean ofCustomSky;
    public boolean ofShowCapes;
    public int ofConnectedTextures;
    public boolean ofCustomItems;
    public boolean ofNaturalTextures;
    public boolean ofEmissiveTextures;
    public boolean ofFastMath;
    public boolean ofFastRender;
    public int ofTranslucentBlocks;
    public boolean ofDynamicFov;
    public boolean ofAlternateBlocks;
    public int ofDynamicLights;
    public boolean ofCustomEntityModels;
    public boolean ofCustomGuis;
    public boolean ofShowGlErrors;
    public int ofScreenshotSize;
    public int ofAnimatedWater;
    public int ofAnimatedLava;
    public boolean ofAnimatedFire;
    public boolean ofAnimatedPortal;
    public boolean ofAnimatedRedstone;
    public boolean ofAnimatedExplosion;
    public boolean ofAnimatedFlame;
    public boolean ofAnimatedSmoke;
    public boolean ofVoidParticles;
    public boolean ofWaterParticles;
    public boolean ofRainSplash;
    public boolean ofPortalParticles;
    public boolean ofPotionParticles;
    public boolean ofFireworkParticles;
    public boolean ofDrippingWaterLava;
    public boolean ofAnimatedTerrain;
    public boolean ofAnimatedTextures;
    public static final int DEFAULT = 0;
    public static final int FAST = 1;
    public static final int FANCY = 2;
    public static final int OFF = 3;
    public static final int SMART = 4;
    public static final int ANIM_ON = 0;
    public static final int ANIM_GENERATED = 1;
    public static final int ANIM_OFF = 2;
    public static final String DEFAULT_STR = "Default";
    private static final int[] OF_TREES_VALUES;
    private static final int[] OF_DYNAMIC_LIGHTS;
    private static final String[] KEYS_DYNAMIC_LIGHTS;
    public KeyBinding ofKeyBindZoom;
    private File optionsFileOF;
    public KeyBinding config;
    
    static {
        logger = LogManager.getLogger();
        gson = new Gson();
        typeListString = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }
            
            @Override
            public Type getRawType() {
                return List.class;
            }
            
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        GUISCALES = new String[] { "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large" };
        PARTICLES = new String[] { "options.particles.all", "options.particles.decreased", "options.particles.minimal" };
        AMBIENT_OCCLUSIONS = new String[] { "options.ao.off", "options.ao.min", "options.ao.max" };
        STREAM_COMPRESSIONS = new String[] { "options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high" };
        STREAM_CHAT_MODES = new String[] { "options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never" };
        STREAM_CHAT_FILTER_MODES = new String[] { "options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods" };
        STREAM_MIC_MODES = new String[] { "options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk" };
        CLOUDS_TYPES = new String[] { "options.off", "options.graphics.fast", "options.graphics.fancy" };
        OF_TREES_VALUES = new int[] { 0, 1, 4, 2 };
        OF_DYNAMIC_LIGHTS = new int[] { 3, 1, 2 };
        KEYS_DYNAMIC_LIGHTS = new String[] { "options.off", "options.graphics.fast", "options.graphics.fancy" };
    }
    
    public GameSettings(final Minecraft mcIn, final File optionsFileIn) {
        this.mouseSensitivity = 0.5f;
        this.renderDistanceChunks = -1;
        this.viewBobbing = true;
        this.fboEnable = true;
        this.limitFramerate = 120;
        this.clouds = 2;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.resourcePacks = Lists.newArrayList();
        this.incompatibleResourcePacks = Lists.newArrayList();
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.chatColours = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.chatOpacity = 1.0f;
        this.snooperEnabled = true;
        this.enableVsync = true;
        this.useVbo = false;
        this.allowBlockAlternatives = true;
        this.reducedDebugInfo = false;
        this.pauseOnLostFocus = true;
        this.setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
        this.heldItemTooltips = true;
        this.chatScale = 1.0f;
        this.chatWidth = 1.0f;
        this.chatHeightUnfocused = 0.44366196f;
        this.chatHeightFocused = 1.0f;
        this.showInventoryAchievementHint = true;
        this.mipmapLevels = 4;
        this.mapSoundLevels = Maps.newEnumMap((Class)SoundCategory.class);
        this.streamBytesPerPixel = 0.5f;
        this.streamMicVolume = 1.0f;
        this.streamGameVolume = 1.0f;
        this.streamKbps = 0.5412844f;
        this.streamFps = 0.31690142f;
        this.streamCompression = 1;
        this.streamSendMetadata = true;
        this.streamPreferredServer = "";
        this.streamChatEnabled = 0;
        this.streamChatUserFilter = 0;
        this.streamMicToggleBehavior = 0;
        this.useNativeTransport = true;
        this.entityShadows = true;
        this.realmsNotifications = true;
        this.keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
        this.keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
        this.keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
        this.keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
        this.keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
        this.keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
        this.keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
        this.keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
        this.keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
        this.keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
        this.keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
        this.keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
        this.keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
        this.keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
        this.keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
        this.keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
        this.keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
        this.keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
        this.keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
        this.keyBindSpectatorOutlines = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
        this.keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
        this.keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
        this.keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
        this.keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
        this.keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
        this.ofMipmapType = 0;
        this.ofOcclusionFancy = false;
        this.ofSmoothFps = false;
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = Config.isSingleProcessor();
        this.ofRenderRegions = false;
        this.ofSmartAnimations = false;
        this.ofAoLevel = 1.0f;
        this.ofAaLevel = 0;
        this.ofAfLevel = 1;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0f;
        this.ofTrees = 0;
        this.ofRain = 0;
        this.ofDroppedItems = 0;
        this.ofBetterGrass = 3;
        this.ofAutoSaveTicks = 4000;
        this.ofLagometer = false;
        this.ofProfiler = false;
        this.ofShowFps = false;
        this.ofWeather = true;
        this.ofSky = true;
        this.ofStars = true;
        this.ofSunMoon = true;
        this.ofVignette = 0;
        this.ofChunkUpdates = 1;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = "Default";
        this.ofSwampColors = true;
        this.ofRandomEntities = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomSky = true;
        this.ofShowCapes = true;
        this.ofConnectedTextures = 2;
        this.ofCustomItems = true;
        this.ofNaturalTextures = false;
        this.ofEmissiveTextures = true;
        this.ofFastMath = false;
        this.ofFastRender = false;
        this.ofTranslucentBlocks = 0;
        this.ofDynamicFov = true;
        this.ofAlternateBlocks = true;
        this.ofDynamicLights = 3;
        this.ofCustomEntityModels = true;
        this.ofCustomGuis = true;
        this.ofShowGlErrors = true;
        this.ofScreenshotSize = 1;
        this.ofAnimatedWater = 0;
        this.ofAnimatedLava = 0;
        this.ofAnimatedFire = true;
        this.ofAnimatedPortal = true;
        this.ofAnimatedRedstone = true;
        this.ofAnimatedExplosion = true;
        this.ofAnimatedFlame = true;
        this.ofAnimatedSmoke = true;
        this.ofVoidParticles = true;
        this.ofWaterParticles = true;
        this.ofRainSplash = true;
        this.ofPortalParticles = true;
        this.ofPotionParticles = true;
        this.ofFireworkParticles = true;
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        this.config = new KeyBinding("Opens the config menu", 54, "Fluid Client");
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines }, (Object[])this.keyBindsHotbar);
        this.addClientKeybinds();
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_US";
        this.forceUnicodeFont = false;
        this.mc = mcIn;
        this.optionsFile = new File(optionsFileIn, "options.txt");
        if (mcIn.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
            Options.RENDER_DISTANCE.setValueMax(32.0f);
            final long i = 1000000L;
            if (Runtime.getRuntime().maxMemory() >= 1500L * i) {
                Options.RENDER_DISTANCE.setValueMax(48.0f);
            }
            if (Runtime.getRuntime().maxMemory() >= 2500L * i) {
                Options.RENDER_DISTANCE.setValueMax(64.0f);
            }
        }
        else {
            Options.RENDER_DISTANCE.setValueMax(16.0f);
        }
        this.renderDistanceChunks = (mcIn.isJava64bit() ? 12 : 8);
        this.optionsFileOF = new File(optionsFileIn, "optionsof.txt");
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
        KeyUtils.fixKeyConflicts(this.keyBindings = (KeyBinding[])ArrayUtils.add((Object[])this.keyBindings, (Object)this.ofKeyBindZoom), new KeyBinding[] { this.ofKeyBindZoom });
        this.renderDistanceChunks = 8;
        this.loadOptions();
        Config.initGameSettings(this);
    }
    
    public GameSettings() {
        this.mouseSensitivity = 0.5f;
        this.renderDistanceChunks = -1;
        this.viewBobbing = true;
        this.fboEnable = true;
        this.limitFramerate = 120;
        this.clouds = 2;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.resourcePacks = Lists.newArrayList();
        this.incompatibleResourcePacks = Lists.newArrayList();
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.chatColours = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.chatOpacity = 1.0f;
        this.snooperEnabled = true;
        this.enableVsync = true;
        this.useVbo = false;
        this.allowBlockAlternatives = true;
        this.reducedDebugInfo = false;
        this.pauseOnLostFocus = true;
        this.setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
        this.heldItemTooltips = true;
        this.chatScale = 1.0f;
        this.chatWidth = 1.0f;
        this.chatHeightUnfocused = 0.44366196f;
        this.chatHeightFocused = 1.0f;
        this.showInventoryAchievementHint = true;
        this.mipmapLevels = 4;
        this.mapSoundLevels = Maps.newEnumMap((Class)SoundCategory.class);
        this.streamBytesPerPixel = 0.5f;
        this.streamMicVolume = 1.0f;
        this.streamGameVolume = 1.0f;
        this.streamKbps = 0.5412844f;
        this.streamFps = 0.31690142f;
        this.streamCompression = 1;
        this.streamSendMetadata = true;
        this.streamPreferredServer = "";
        this.streamChatEnabled = 0;
        this.streamChatUserFilter = 0;
        this.streamMicToggleBehavior = 0;
        this.useNativeTransport = true;
        this.entityShadows = true;
        this.realmsNotifications = true;
        this.keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
        this.keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
        this.keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
        this.keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
        this.keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
        this.keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
        this.keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
        this.keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
        this.keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
        this.keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
        this.keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
        this.keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
        this.keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
        this.keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
        this.keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
        this.keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
        this.keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
        this.keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
        this.keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
        this.keyBindSpectatorOutlines = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
        this.keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
        this.keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
        this.keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
        this.keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
        this.keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
        this.ofMipmapType = 0;
        this.ofOcclusionFancy = false;
        this.ofSmoothFps = false;
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = Config.isSingleProcessor();
        this.ofRenderRegions = false;
        this.ofSmartAnimations = false;
        this.ofAoLevel = 1.0f;
        this.ofAaLevel = 0;
        this.ofAfLevel = 1;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0f;
        this.ofTrees = 0;
        this.ofRain = 0;
        this.ofDroppedItems = 0;
        this.ofBetterGrass = 3;
        this.ofAutoSaveTicks = 4000;
        this.ofLagometer = false;
        this.ofProfiler = false;
        this.ofShowFps = false;
        this.ofWeather = true;
        this.ofSky = true;
        this.ofStars = true;
        this.ofSunMoon = true;
        this.ofVignette = 0;
        this.ofChunkUpdates = 1;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = "Default";
        this.ofSwampColors = true;
        this.ofRandomEntities = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomSky = true;
        this.ofShowCapes = true;
        this.ofConnectedTextures = 2;
        this.ofCustomItems = true;
        this.ofNaturalTextures = false;
        this.ofEmissiveTextures = true;
        this.ofFastMath = false;
        this.ofFastRender = false;
        this.ofTranslucentBlocks = 0;
        this.ofDynamicFov = true;
        this.ofAlternateBlocks = true;
        this.ofDynamicLights = 3;
        this.ofCustomEntityModels = true;
        this.ofCustomGuis = true;
        this.ofShowGlErrors = true;
        this.ofScreenshotSize = 1;
        this.ofAnimatedWater = 0;
        this.ofAnimatedLava = 0;
        this.ofAnimatedFire = true;
        this.ofAnimatedPortal = true;
        this.ofAnimatedRedstone = true;
        this.ofAnimatedExplosion = true;
        this.ofAnimatedFlame = true;
        this.ofAnimatedSmoke = true;
        this.ofVoidParticles = true;
        this.ofWaterParticles = true;
        this.ofRainSplash = true;
        this.ofPortalParticles = true;
        this.ofPotionParticles = true;
        this.ofFireworkParticles = true;
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        this.config = new KeyBinding("Opens the config menu", 54, "Fluid Client");
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines }, (Object[])this.keyBindsHotbar);
        this.addClientKeybinds();
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_US";
        this.forceUnicodeFont = false;
    }
    
    public void addClientKeybinds() {
        this.keyBindings = (KeyBinding[])ArrayUtils.add((Object[])this.keyBindings, (Object)this.config);
    }
    
    public static String getKeyDisplayString(final int key) {
        return (key < 0) ? I18n.format("key.mouseButton", key + 101) : ((key < 256) ? Keyboard.getKeyName(key) : String.format("%c", (char)(key - 256)).toUpperCase());
    }
    
    public static boolean isKeyDown(final KeyBinding key) {
        return key.getKeyCode() != 0 && ((key.getKeyCode() < 0) ? Mouse.isButtonDown(key.getKeyCode() + 100) : Keyboard.isKeyDown(key.getKeyCode()));
    }
    
    public void setOptionKeyBinding(final KeyBinding key, final int keyCode) {
        key.setKeyCode(keyCode);
        this.saveOptions();
    }
    
    public void setOptionFloatValue(final Options settingsOption, final float value) {
        this.setOptionFloatValueOF(settingsOption, value);
        if (settingsOption == Options.SENSITIVITY) {
            this.mouseSensitivity = value;
        }
        if (settingsOption == Options.FOV) {
            this.fovSetting = value;
        }
        if (settingsOption == Options.GAMMA) {
            this.gammaSetting = value;
        }
        if (settingsOption == Options.FRAMERATE_LIMIT) {
            this.limitFramerate = (int)value;
            this.enableVsync = false;
            if (this.limitFramerate <= 0) {
                this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                this.enableVsync = true;
            }
            this.updateVSync();
        }
        if (settingsOption == Options.CHAT_OPACITY) {
            this.chatOpacity = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.CHAT_HEIGHT_FOCUSED) {
            this.chatHeightFocused = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.CHAT_HEIGHT_UNFOCUSED) {
            this.chatHeightUnfocused = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.CHAT_WIDTH) {
            this.chatWidth = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.CHAT_SCALE) {
            this.chatScale = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.MIPMAP_LEVELS) {
            final int i = this.mipmapLevels;
            this.mipmapLevels = (int)value;
            if (i != value) {
                this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
                this.mc.scheduleResourcesRefresh();
            }
        }
        if (settingsOption == Options.BLOCK_ALTERNATIVES) {
            this.allowBlockAlternatives = !this.allowBlockAlternatives;
            this.mc.renderGlobal.loadRenderers();
        }
        if (settingsOption == Options.RENDER_DISTANCE) {
            this.renderDistanceChunks = (int)value;
            this.mc.renderGlobal.setDisplayListEntitiesDirty();
        }
        if (settingsOption == Options.STREAM_BYTES_PER_PIXEL) {
            this.streamBytesPerPixel = value;
        }
        if (settingsOption == Options.STREAM_VOLUME_MIC) {
            this.streamMicVolume = value;
            this.mc.getTwitchStream().updateStreamVolume();
        }
        if (settingsOption == Options.STREAM_VOLUME_SYSTEM) {
            this.streamGameVolume = value;
            this.mc.getTwitchStream().updateStreamVolume();
        }
        if (settingsOption == Options.STREAM_KBPS) {
            this.streamKbps = value;
        }
        if (settingsOption == Options.STREAM_FPS) {
            this.streamFps = value;
        }
    }
    
    public void setOptionValue(final Options settingsOption, final int value) {
        this.setOptionValueOF(settingsOption, value);
        if (settingsOption == Options.INVERT_MOUSE) {
            this.invertMouse = !this.invertMouse;
        }
        if (settingsOption == Options.GUI_SCALE) {
            this.guiScale += value;
            if (GuiScreen.isShiftKeyDown()) {
                this.guiScale = 0;
            }
            final DisplayMode displaymode = Config.getLargestDisplayMode();
            final int i = displaymode.getWidth() / 320;
            final int j = displaymode.getHeight() / 240;
            final int k = Math.min(i, j);
            if (this.guiScale < 0) {
                this.guiScale = k - 1;
            }
            if (this.mc.isUnicode() && this.guiScale % 2 != 0) {
                this.guiScale += value;
            }
            if (this.guiScale < 0 || this.guiScale >= k) {
                this.guiScale = 0;
            }
        }
        if (settingsOption == Options.PARTICLES) {
            this.particleSetting = (this.particleSetting + value) % 3;
        }
        if (settingsOption == Options.VIEW_BOBBING) {
            this.viewBobbing = !this.viewBobbing;
        }
        if (settingsOption == Options.RENDER_CLOUDS) {
            this.clouds = (this.clouds + value) % 3;
        }
        if (settingsOption == Options.FORCE_UNICODE_FONT) {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            this.mc.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }
        if (settingsOption == Options.FBO_ENABLE) {
            this.fboEnable = !this.fboEnable;
        }
        if (settingsOption == Options.ANAGLYPH) {
            if (!this.anaglyph && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
                return;
            }
            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
        }
        if (settingsOption == Options.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.updateRenderClouds();
            this.mc.renderGlobal.loadRenderers();
        }
        if (settingsOption == Options.AMBIENT_OCCLUSION) {
            this.ambientOcclusion = (this.ambientOcclusion + value) % 3;
            this.mc.renderGlobal.loadRenderers();
        }
        if (settingsOption == Options.CHAT_VISIBILITY) {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + value) % 3);
        }
        if (settingsOption == Options.STREAM_COMPRESSION) {
            this.streamCompression = (this.streamCompression + value) % 3;
        }
        if (settingsOption == Options.STREAM_SEND_METADATA) {
            this.streamSendMetadata = !this.streamSendMetadata;
        }
        if (settingsOption == Options.STREAM_CHAT_ENABLED) {
            this.streamChatEnabled = (this.streamChatEnabled + value) % 3;
        }
        if (settingsOption == Options.STREAM_CHAT_USER_FILTER) {
            this.streamChatUserFilter = (this.streamChatUserFilter + value) % 3;
        }
        if (settingsOption == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            this.streamMicToggleBehavior = (this.streamMicToggleBehavior + value) % 2;
        }
        if (settingsOption == Options.CHAT_COLOR) {
            this.chatColours = !this.chatColours;
        }
        if (settingsOption == Options.CHAT_LINKS) {
            this.chatLinks = !this.chatLinks;
        }
        if (settingsOption == Options.CHAT_LINKS_PROMPT) {
            this.chatLinksPrompt = !this.chatLinksPrompt;
        }
        if (settingsOption == Options.SNOOPER_ENABLED) {
            this.snooperEnabled = !this.snooperEnabled;
        }
        if (settingsOption == Options.TOUCHSCREEN) {
            this.touchscreen = !this.touchscreen;
        }
        if (settingsOption == Options.USE_FULLSCREEN) {
            this.fullScreen = !this.fullScreen;
            if (this.mc.isFullScreen() != this.fullScreen) {
                this.mc.toggleFullscreen();
            }
        }
        if (settingsOption == Options.ENABLE_VSYNC) {
            Display.setVSyncEnabled(this.enableVsync = !this.enableVsync);
        }
        if (settingsOption == Options.USE_VBO) {
            this.useVbo = !this.useVbo;
            this.mc.renderGlobal.loadRenderers();
        }
        if (settingsOption == Options.BLOCK_ALTERNATIVES) {
            this.allowBlockAlternatives = !this.allowBlockAlternatives;
            this.mc.renderGlobal.loadRenderers();
        }
        if (settingsOption == Options.REDUCED_DEBUG_INFO) {
            this.reducedDebugInfo = !this.reducedDebugInfo;
        }
        if (settingsOption == Options.ENTITY_SHADOWS) {
            this.entityShadows = !this.entityShadows;
        }
        if (settingsOption == Options.REALMS_NOTIFICATIONS) {
            this.realmsNotifications = !this.realmsNotifications;
        }
        this.saveOptions();
    }
    
    public float getOptionFloatValue(final Options settingOption) {
        final float f = this.getOptionFloatValueOF(settingOption);
        return (f != Float.MAX_VALUE) ? f : ((settingOption == Options.FOV) ? this.fovSetting : ((settingOption == Options.GAMMA) ? this.gammaSetting : ((settingOption == Options.SATURATION) ? this.saturation : ((settingOption == Options.SENSITIVITY) ? this.mouseSensitivity : ((settingOption == Options.CHAT_OPACITY) ? this.chatOpacity : ((settingOption == Options.CHAT_HEIGHT_FOCUSED) ? this.chatHeightFocused : ((settingOption == Options.CHAT_HEIGHT_UNFOCUSED) ? this.chatHeightUnfocused : ((settingOption == Options.CHAT_SCALE) ? this.chatScale : ((settingOption == Options.CHAT_WIDTH) ? this.chatWidth : ((settingOption == Options.FRAMERATE_LIMIT) ? ((float)this.limitFramerate) : ((settingOption == Options.MIPMAP_LEVELS) ? ((float)this.mipmapLevels) : ((settingOption == Options.RENDER_DISTANCE) ? ((float)this.renderDistanceChunks) : ((settingOption == Options.STREAM_BYTES_PER_PIXEL) ? this.streamBytesPerPixel : ((settingOption == Options.STREAM_VOLUME_MIC) ? this.streamMicVolume : ((settingOption == Options.STREAM_VOLUME_SYSTEM) ? this.streamGameVolume : ((settingOption == Options.STREAM_KBPS) ? this.streamKbps : ((settingOption == Options.STREAM_FPS) ? this.streamFps : 0.0f)))))))))))))))));
    }
    
    public boolean getOptionOrdinalValue(final Options settingOption) {
        switch (settingOption) {
            case INVERT_MOUSE: {
                return this.invertMouse;
            }
            case VIEW_BOBBING: {
                return this.viewBobbing;
            }
            case ANAGLYPH: {
                return this.anaglyph;
            }
            case FBO_ENABLE: {
                return this.fboEnable;
            }
            case CHAT_COLOR: {
                return this.chatColours;
            }
            case CHAT_LINKS: {
                return this.chatLinks;
            }
            case CHAT_LINKS_PROMPT: {
                return this.chatLinksPrompt;
            }
            case SNOOPER_ENABLED: {
                return this.snooperEnabled;
            }
            case USE_FULLSCREEN: {
                return this.fullScreen;
            }
            case ENABLE_VSYNC: {
                return this.enableVsync;
            }
            case USE_VBO: {
                return this.useVbo;
            }
            case TOUCHSCREEN: {
                return this.touchscreen;
            }
            case STREAM_SEND_METADATA: {
                return this.streamSendMetadata;
            }
            case FORCE_UNICODE_FONT: {
                return this.forceUnicodeFont;
            }
            case BLOCK_ALTERNATIVES: {
                return this.allowBlockAlternatives;
            }
            case REDUCED_DEBUG_INFO: {
                return this.reducedDebugInfo;
            }
            case ENTITY_SHADOWS: {
                return this.entityShadows;
            }
            case REALMS_NOTIFICATIONS: {
                return this.realmsNotifications;
            }
            default: {
                return false;
            }
        }
    }
    
    private static String getTranslation(final String[] strArray, int index) {
        if (index < 0 || index >= strArray.length) {
            index = 0;
        }
        return I18n.format(strArray[index], new Object[0]);
    }
    
    public String getKeyBinding(final Options settingOption) {
        final String s = this.getKeyBindingOF(settingOption);
        if (s != null) {
            return s;
        }
        final String s2 = String.valueOf(I18n.format(settingOption.getEnumString(), new Object[0])) + ": ";
        if (settingOption.getEnumFloat()) {
            final float f1 = this.getOptionFloatValue(settingOption);
            final float f2 = settingOption.normalizeValue(f1);
            return (settingOption == Options.MIPMAP_LEVELS && f1 >= 4.0) ? (String.valueOf(s2) + Lang.get("of.general.max")) : ((settingOption == Options.SENSITIVITY) ? ((f2 == 0.0f) ? (String.valueOf(s2) + I18n.format("options.sensitivity.min", new Object[0])) : ((f2 == 1.0f) ? (String.valueOf(s2) + I18n.format("options.sensitivity.max", new Object[0])) : (String.valueOf(s2) + (int)(f2 * 200.0f) + "%"))) : ((settingOption == Options.FOV) ? ((f1 == 70.0f) ? (String.valueOf(s2) + I18n.format("options.fov.min", new Object[0])) : ((f1 == 110.0f) ? (String.valueOf(s2) + I18n.format("options.fov.max", new Object[0])) : (String.valueOf(s2) + (int)f1))) : ((settingOption == Options.FRAMERATE_LIMIT) ? ((f1 == settingOption.valueMax) ? (String.valueOf(s2) + I18n.format("options.framerateLimit.max", new Object[0])) : (String.valueOf(s2) + (int)f1 + " fps")) : ((settingOption == Options.RENDER_CLOUDS) ? ((f1 == settingOption.valueMin) ? (String.valueOf(s2) + I18n.format("options.cloudHeight.min", new Object[0])) : (String.valueOf(s2) + ((int)f1 + 128))) : ((settingOption == Options.GAMMA) ? ((f2 == 0.0f) ? (String.valueOf(s2) + I18n.format("options.gamma.min", new Object[0])) : ((f2 == 1.0f) ? (String.valueOf(s2) + I18n.format("options.gamma.max", new Object[0])) : (String.valueOf(s2) + "+" + (int)(f2 * 100.0f) + "%"))) : ((settingOption == Options.SATURATION) ? (String.valueOf(s2) + (int)(f2 * 400.0f) + "%") : ((settingOption == Options.CHAT_OPACITY) ? (String.valueOf(s2) + (int)(f2 * 90.0f + 10.0f) + "%") : ((settingOption == Options.CHAT_HEIGHT_UNFOCUSED) ? (String.valueOf(s2) + GuiNewChat.calculateChatboxHeight(f2) + "px") : ((settingOption == Options.CHAT_HEIGHT_FOCUSED) ? (String.valueOf(s2) + GuiNewChat.calculateChatboxHeight(f2) + "px") : ((settingOption == Options.CHAT_WIDTH) ? (String.valueOf(s2) + GuiNewChat.calculateChatboxWidth(f2) + "px") : ((settingOption == Options.RENDER_DISTANCE) ? (String.valueOf(s2) + (int)f1 + " chunks") : ((settingOption == Options.MIPMAP_LEVELS) ? ((f1 == 0.0f) ? (String.valueOf(s2) + I18n.format("options.off", new Object[0])) : (String.valueOf(s2) + (int)f1)) : ((settingOption == Options.STREAM_FPS) ? (String.valueOf(s2) + TwitchStream.formatStreamFps(f2) + " fps") : ((settingOption == Options.STREAM_KBPS) ? (String.valueOf(s2) + TwitchStream.formatStreamKbps(f2) + " Kbps") : ((settingOption == Options.STREAM_BYTES_PER_PIXEL) ? (String.valueOf(s2) + String.format("%.3f bpp", TwitchStream.formatStreamBps(f2))) : ((f2 == 0.0f) ? (String.valueOf(s2) + I18n.format("options.off", new Object[0])) : (String.valueOf(s2) + (int)(f2 * 100.0f) + "%")))))))))))))))));
        }
        if (settingOption.getEnumBoolean()) {
            final boolean flag = this.getOptionOrdinalValue(settingOption);
            return flag ? (String.valueOf(s2) + I18n.format("options.on", new Object[0])) : (String.valueOf(s2) + I18n.format("options.off", new Object[0]));
        }
        if (settingOption == Options.GUI_SCALE) {
            return (this.guiScale >= GameSettings.GUISCALES.length) ? (String.valueOf(s2) + this.guiScale + "x") : (String.valueOf(s2) + getTranslation(GameSettings.GUISCALES, this.guiScale));
        }
        if (settingOption == Options.CHAT_VISIBILITY) {
            return String.valueOf(s2) + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
        }
        if (settingOption == Options.PARTICLES) {
            return String.valueOf(s2) + getTranslation(GameSettings.PARTICLES, this.particleSetting);
        }
        if (settingOption == Options.AMBIENT_OCCLUSION) {
            return String.valueOf(s2) + getTranslation(GameSettings.AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        if (settingOption == Options.STREAM_COMPRESSION) {
            return String.valueOf(s2) + getTranslation(GameSettings.STREAM_COMPRESSIONS, this.streamCompression);
        }
        if (settingOption == Options.STREAM_CHAT_ENABLED) {
            return String.valueOf(s2) + getTranslation(GameSettings.STREAM_CHAT_MODES, this.streamChatEnabled);
        }
        if (settingOption == Options.STREAM_CHAT_USER_FILTER) {
            return String.valueOf(s2) + getTranslation(GameSettings.STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
        }
        if (settingOption == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return String.valueOf(s2) + getTranslation(GameSettings.STREAM_MIC_MODES, this.streamMicToggleBehavior);
        }
        if (settingOption == Options.RENDER_CLOUDS) {
            return String.valueOf(s2) + getTranslation(GameSettings.CLOUDS_TYPES, this.clouds);
        }
        if (settingOption != Options.GRAPHICS) {
            return s2;
        }
        if (this.fancyGraphics) {
            return String.valueOf(s2) + I18n.format("options.graphics.fancy", new Object[0]);
        }
        final String s3 = "options.graphics.fast";
        return String.valueOf(s2) + I18n.format("options.graphics.fast", new Object[0]);
    }
    
    public void loadOptions() {
        FileInputStream fileinputstream = null;
        Label_2160: {
            try {
                if (this.optionsFile.exists()) {
                    final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(fileinputstream = new FileInputStream(this.optionsFile)));
                    String s = "";
                    this.mapSoundLevels.clear();
                    while ((s = bufferedreader.readLine()) != null) {
                        try {
                            final String[] astring = s.split(":");
                            if (astring[0].equals("mouseSensitivity")) {
                                this.mouseSensitivity = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("fov")) {
                                this.fovSetting = this.parseFloat(astring[1]) * 40.0f + 70.0f;
                            }
                            if (astring[0].equals("gamma")) {
                                this.gammaSetting = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("saturation")) {
                                this.saturation = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("invertYMouse")) {
                                this.invertMouse = astring[1].equals("true");
                            }
                            if (astring[0].equals("renderDistance")) {
                                this.renderDistanceChunks = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("guiScale")) {
                                this.guiScale = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("particles")) {
                                this.particleSetting = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("bobView")) {
                                this.viewBobbing = astring[1].equals("true");
                            }
                            if (astring[0].equals("anaglyph3d")) {
                                this.anaglyph = astring[1].equals("true");
                            }
                            if (astring[0].equals("maxFps")) {
                                this.limitFramerate = Integer.parseInt(astring[1]);
                                if (this.enableVsync) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                                if (this.limitFramerate <= 0) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                            }
                            if (astring[0].equals("fboEnable")) {
                                this.fboEnable = astring[1].equals("true");
                            }
                            if (astring[0].equals("difficulty")) {
                                this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(astring[1]));
                            }
                            if (astring[0].equals("fancyGraphics")) {
                                this.fancyGraphics = astring[1].equals("true");
                                this.updateRenderClouds();
                            }
                            if (astring[0].equals("ao")) {
                                if (astring[1].equals("true")) {
                                    this.ambientOcclusion = 2;
                                }
                                else if (astring[1].equals("false")) {
                                    this.ambientOcclusion = 0;
                                }
                                else {
                                    this.ambientOcclusion = Integer.parseInt(astring[1]);
                                }
                            }
                            if (astring[0].equals("renderClouds")) {
                                if (astring[1].equals("true")) {
                                    this.clouds = 2;
                                }
                                else if (astring[1].equals("false")) {
                                    this.clouds = 0;
                                }
                                else if (astring[1].equals("fast")) {
                                    this.clouds = 1;
                                }
                            }
                            if (astring[0].equals("resourcePacks")) {
                                this.resourcePacks = (List)GameSettings.gson.fromJson(s.substring(s.indexOf(58) + 1), (Type)GameSettings.typeListString);
                                if (this.resourcePacks == null) {
                                    this.resourcePacks = Lists.newArrayList();
                                }
                            }
                            if (astring[0].equals("incompatibleResourcePacks")) {
                                this.incompatibleResourcePacks = (List)GameSettings.gson.fromJson(s.substring(s.indexOf(58) + 1), (Type)GameSettings.typeListString);
                                if (this.incompatibleResourcePacks == null) {
                                    this.incompatibleResourcePacks = Lists.newArrayList();
                                }
                            }
                            if (astring[0].equals("lastServer") && astring.length >= 2) {
                                this.lastServer = s.substring(s.indexOf(58) + 1);
                            }
                            if (astring[0].equals("lang") && astring.length >= 2) {
                                this.language = astring[1];
                            }
                            if (astring[0].equals("chatVisibility")) {
                                this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(astring[1]));
                            }
                            if (astring[0].equals("chatColors")) {
                                this.chatColours = astring[1].equals("true");
                            }
                            if (astring[0].equals("chatLinks")) {
                                this.chatLinks = astring[1].equals("true");
                            }
                            if (astring[0].equals("chatLinksPrompt")) {
                                this.chatLinksPrompt = astring[1].equals("true");
                            }
                            if (astring[0].equals("chatOpacity")) {
                                this.chatOpacity = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("snooperEnabled")) {
                                this.snooperEnabled = astring[1].equals("true");
                            }
                            if (astring[0].equals("fullscreen")) {
                                this.fullScreen = astring[1].equals("true");
                            }
                            if (astring[0].equals("enableVsync")) {
                                this.enableVsync = astring[1].equals("true");
                                if (this.enableVsync) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                                this.updateVSync();
                            }
                            if (astring[0].equals("useVbo")) {
                                this.useVbo = astring[1].equals("true");
                            }
                            if (astring[0].equals("hideServerAddress")) {
                                this.hideServerAddress = astring[1].equals("true");
                            }
                            if (astring[0].equals("advancedItemTooltips")) {
                                this.advancedItemTooltips = astring[1].equals("true");
                            }
                            if (astring[0].equals("pauseOnLostFocus")) {
                                this.pauseOnLostFocus = astring[1].equals("true");
                            }
                            if (astring[0].equals("touchscreen")) {
                                this.touchscreen = astring[1].equals("true");
                            }
                            if (astring[0].equals("overrideHeight")) {
                                this.overrideHeight = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("overrideWidth")) {
                                this.overrideWidth = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("heldItemTooltips")) {
                                this.heldItemTooltips = astring[1].equals("true");
                            }
                            if (astring[0].equals("chatHeightFocused")) {
                                this.chatHeightFocused = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("chatHeightUnfocused")) {
                                this.chatHeightUnfocused = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("chatScale")) {
                                this.chatScale = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("chatWidth")) {
                                this.chatWidth = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("showInventoryAchievementHint")) {
                                this.showInventoryAchievementHint = astring[1].equals("true");
                            }
                            if (astring[0].equals("mipmapLevels")) {
                                this.mipmapLevels = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("streamBytesPerPixel")) {
                                this.streamBytesPerPixel = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("streamMicVolume")) {
                                this.streamMicVolume = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("streamSystemVolume")) {
                                this.streamGameVolume = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("streamKbps")) {
                                this.streamKbps = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("streamFps")) {
                                this.streamFps = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals("streamCompression")) {
                                this.streamCompression = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("streamSendMetadata")) {
                                this.streamSendMetadata = astring[1].equals("true");
                            }
                            if (astring[0].equals("streamPreferredServer") && astring.length >= 2) {
                                this.streamPreferredServer = s.substring(s.indexOf(58) + 1);
                            }
                            if (astring[0].equals("streamChatEnabled")) {
                                this.streamChatEnabled = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("streamChatUserFilter")) {
                                this.streamChatUserFilter = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("streamMicToggleBehavior")) {
                                this.streamMicToggleBehavior = Integer.parseInt(astring[1]);
                            }
                            if (astring[0].equals("forceUnicodeFont")) {
                                this.forceUnicodeFont = astring[1].equals("true");
                            }
                            if (astring[0].equals("allowBlockAlternatives")) {
                                this.allowBlockAlternatives = astring[1].equals("true");
                            }
                            if (astring[0].equals("reducedDebugInfo")) {
                                this.reducedDebugInfo = astring[1].equals("true");
                            }
                            if (astring[0].equals("useNativeTransport")) {
                                this.useNativeTransport = astring[1].equals("true");
                            }
                            if (astring[0].equals("entityShadows")) {
                                this.entityShadows = astring[1].equals("true");
                            }
                            if (astring[0].equals("realmsNotifications")) {
                                this.realmsNotifications = astring[1].equals("true");
                            }
                            KeyBinding[] keyBindings;
                            for (int length = (keyBindings = this.keyBindings).length, i = 0; i < length; ++i) {
                                final KeyBinding keybinding = keyBindings[i];
                                if (astring[0].equals("key_" + keybinding.getKeyDescription())) {
                                    keybinding.setKeyCode(Integer.parseInt(astring[1]));
                                }
                            }
                            SoundCategory[] values;
                            for (int length2 = (values = SoundCategory.values()).length, j = 0; j < length2; ++j) {
                                final SoundCategory soundcategory = values[j];
                                if (astring[0].equals("soundCategory_" + soundcategory.getCategoryName())) {
                                    this.mapSoundLevels.put(soundcategory, this.parseFloat(astring[1]));
                                }
                            }
                            EnumPlayerModelParts[] values2;
                            for (int length3 = (values2 = EnumPlayerModelParts.values()).length, k = 0; k < length3; ++k) {
                                final EnumPlayerModelParts enumplayermodelparts = values2[k];
                                if (astring[0].equals("modelPart_" + enumplayermodelparts.getPartName())) {
                                    this.setModelPartEnabled(enumplayermodelparts, astring[1].equals("true"));
                                }
                            }
                        }
                        catch (final Exception exception) {
                            GameSettings.logger.warn("Skipping bad option: " + s);
                            exception.printStackTrace();
                        }
                    }
                    KeyBinding.resetKeyBindingArrayAndHash();
                    bufferedreader.close();
                    break Label_2160;
                }
            }
            catch (final Exception exception2) {
                GameSettings.logger.error("Failed to load options", (Throwable)exception2);
                break Label_2160;
            }
            finally {
                IOUtils.closeQuietly((InputStream)fileinputstream);
            }
            IOUtils.closeQuietly((InputStream)fileinputstream);
            return;
        }
        this.loadOfOptions();
    }
    
    private float parseFloat(final String str) {
        return str.equals("true") ? 1.0f : (str.equals("false") ? 0.0f : Float.parseFloat(str));
    }
    
    public void saveOptions() {
        if (Reflector.FMLClientHandler.exists()) {
            final Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            if (object != null && Reflector.callBoolean(object, Reflector.FMLClientHandler_isLoading, new Object[0])) {
                return;
            }
        }
        try {
            final PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
            printwriter.println("invertYMouse:" + this.invertMouse);
            printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
            printwriter.println("fov:" + (this.fovSetting - 70.0f) / 40.0f);
            printwriter.println("gamma:" + this.gammaSetting);
            printwriter.println("saturation:" + this.saturation);
            printwriter.println("renderDistance:" + this.renderDistanceChunks);
            printwriter.println("guiScale:" + this.guiScale);
            printwriter.println("particles:" + this.particleSetting);
            printwriter.println("bobView:" + this.viewBobbing);
            printwriter.println("anaglyph3d:" + this.anaglyph);
            printwriter.println("maxFps:" + this.limitFramerate);
            printwriter.println("fboEnable:" + this.fboEnable);
            printwriter.println("difficulty:" + this.difficulty.getDifficultyId());
            printwriter.println("fancyGraphics:" + this.fancyGraphics);
            printwriter.println("ao:" + this.ambientOcclusion);
            switch (this.clouds) {
                case 0: {
                    printwriter.println("renderClouds:false");
                    break;
                }
                case 1: {
                    printwriter.println("renderClouds:fast");
                    break;
                }
                case 2: {
                    printwriter.println("renderClouds:true");
                    break;
                }
            }
            printwriter.println("resourcePacks:" + GameSettings.gson.toJson((Object)this.resourcePacks));
            printwriter.println("incompatibleResourcePacks:" + GameSettings.gson.toJson((Object)this.incompatibleResourcePacks));
            printwriter.println("lastServer:" + this.lastServer);
            printwriter.println("lang:" + this.language);
            printwriter.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
            printwriter.println("chatColors:" + this.chatColours);
            printwriter.println("chatLinks:" + this.chatLinks);
            printwriter.println("chatLinksPrompt:" + this.chatLinksPrompt);
            printwriter.println("chatOpacity:" + this.chatOpacity);
            printwriter.println("snooperEnabled:" + this.snooperEnabled);
            printwriter.println("fullscreen:" + this.fullScreen);
            printwriter.println("enableVsync:" + this.enableVsync);
            printwriter.println("useVbo:" + this.useVbo);
            printwriter.println("hideServerAddress:" + this.hideServerAddress);
            printwriter.println("advancedItemTooltips:" + this.advancedItemTooltips);
            printwriter.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
            printwriter.println("touchscreen:" + this.touchscreen);
            printwriter.println("overrideWidth:" + this.overrideWidth);
            printwriter.println("overrideHeight:" + this.overrideHeight);
            printwriter.println("heldItemTooltips:" + this.heldItemTooltips);
            printwriter.println("chatHeightFocused:" + this.chatHeightFocused);
            printwriter.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
            printwriter.println("chatScale:" + this.chatScale);
            printwriter.println("chatWidth:" + this.chatWidth);
            printwriter.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
            printwriter.println("mipmapLevels:" + this.mipmapLevels);
            printwriter.println("streamBytesPerPixel:" + this.streamBytesPerPixel);
            printwriter.println("streamMicVolume:" + this.streamMicVolume);
            printwriter.println("streamSystemVolume:" + this.streamGameVolume);
            printwriter.println("streamKbps:" + this.streamKbps);
            printwriter.println("streamFps:" + this.streamFps);
            printwriter.println("streamCompression:" + this.streamCompression);
            printwriter.println("streamSendMetadata:" + this.streamSendMetadata);
            printwriter.println("streamPreferredServer:" + this.streamPreferredServer);
            printwriter.println("streamChatEnabled:" + this.streamChatEnabled);
            printwriter.println("streamChatUserFilter:" + this.streamChatUserFilter);
            printwriter.println("streamMicToggleBehavior:" + this.streamMicToggleBehavior);
            printwriter.println("forceUnicodeFont:" + this.forceUnicodeFont);
            printwriter.println("allowBlockAlternatives:" + this.allowBlockAlternatives);
            printwriter.println("reducedDebugInfo:" + this.reducedDebugInfo);
            printwriter.println("useNativeTransport:" + this.useNativeTransport);
            printwriter.println("entityShadows:" + this.entityShadows);
            printwriter.println("realmsNotifications:" + this.realmsNotifications);
            KeyBinding[] keyBindings;
            for (int length = (keyBindings = this.keyBindings).length, i = 0; i < length; ++i) {
                final KeyBinding keybinding = keyBindings[i];
                printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
            }
            SoundCategory[] values;
            for (int length2 = (values = SoundCategory.values()).length, j = 0; j < length2; ++j) {
                final SoundCategory soundcategory = values[j];
                printwriter.println("soundCategory_" + soundcategory.getCategoryName() + ":" + this.getSoundLevel(soundcategory));
            }
            EnumPlayerModelParts[] values2;
            for (int length3 = (values2 = EnumPlayerModelParts.values()).length, k = 0; k < length3; ++k) {
                final EnumPlayerModelParts enumplayermodelparts = values2[k];
                printwriter.println("modelPart_" + enumplayermodelparts.getPartName() + ":" + this.setModelParts.contains(enumplayermodelparts));
            }
            printwriter.close();
        }
        catch (final Exception exception) {
            GameSettings.logger.error("Failed to save options", (Throwable)exception);
        }
        this.saveOfOptions();
        this.sendSettingsToServer();
    }
    
    public float getSoundLevel(final SoundCategory sndCategory) {
        return this.mapSoundLevels.containsKey(sndCategory) ? this.mapSoundLevels.get(sndCategory) : 1.0f;
    }
    
    public void setSoundLevel(final SoundCategory sndCategory, final float soundLevel) {
        this.mc.getSoundHandler().setSoundLevel(sndCategory, soundLevel);
        this.mapSoundLevels.put(sndCategory, soundLevel);
    }
    
    public void sendSettingsToServer() {
        if (this.mc.thePlayer != null) {
            int i = 0;
            for (final EnumPlayerModelParts enumplayermodelparts : this.setModelParts) {
                i |= enumplayermodelparts.getPartMask();
            }
            this.mc.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i));
        }
    }
    
    public Set<EnumPlayerModelParts> getModelParts() {
        return (Set<EnumPlayerModelParts>)ImmutableSet.copyOf((Collection)this.setModelParts);
    }
    
    public void setModelPartEnabled(final EnumPlayerModelParts modelPart, final boolean enable) {
        if (enable) {
            this.setModelParts.add(modelPart);
        }
        else {
            this.setModelParts.remove(modelPart);
        }
        this.sendSettingsToServer();
    }
    
    public void switchModelPartEnabled(final EnumPlayerModelParts modelPart) {
        if (!this.getModelParts().contains(modelPart)) {
            this.setModelParts.add(modelPart);
        }
        else {
            this.setModelParts.remove(modelPart);
        }
        this.sendSettingsToServer();
    }
    
    public int shouldRenderClouds() {
        return (this.renderDistanceChunks >= 4) ? this.clouds : 0;
    }
    
    public boolean isUsingNativeTransport() {
        return this.useNativeTransport;
    }
    
    private void setOptionFloatValueOF(final Options p_setOptionFloatValueOF_1_, final float p_setOptionFloatValueOF_2_) {
        if (p_setOptionFloatValueOF_1_ == Options.CLOUD_HEIGHT) {
            this.ofCloudsHeight = p_setOptionFloatValueOF_2_;
            this.mc.renderGlobal.resetClouds();
        }
        if (p_setOptionFloatValueOF_1_ == Options.AO_LEVEL) {
            this.ofAoLevel = p_setOptionFloatValueOF_2_;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionFloatValueOF_1_ == Options.AA_LEVEL) {
            final int i = (int)p_setOptionFloatValueOF_2_;
            if (i > 0 && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
                return;
            }
            final int[] aint = { 0, 2, 4, 6, 8, 12, 16 };
            this.ofAaLevel = 0;
            for (int j = 0; j < aint.length; ++j) {
                if (i >= aint[j]) {
                    this.ofAaLevel = aint[j];
                }
            }
            this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
        }
        if (p_setOptionFloatValueOF_1_ == Options.AF_LEVEL) {
            final int k = (int)p_setOptionFloatValueOF_2_;
            if (k > 1 && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
                return;
            }
            this.ofAfLevel = 1;
            while (this.ofAfLevel * 2 <= k) {
                this.ofAfLevel *= 2;
            }
            this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
            this.mc.refreshResources();
        }
        if (p_setOptionFloatValueOF_1_ == Options.MIPMAP_TYPE) {
            final int l = (int)p_setOptionFloatValueOF_2_;
            this.ofMipmapType = Config.limit(l, 0, 3);
            this.mc.refreshResources();
        }
        if (p_setOptionFloatValueOF_1_ == Options.FULLSCREEN_MODE) {
            final int i2 = (int)p_setOptionFloatValueOF_2_ - 1;
            final String[] astring = Config.getDisplayModeNames();
            if (i2 < 0 || i2 >= astring.length) {
                this.ofFullscreenMode = "Default";
                return;
            }
            this.ofFullscreenMode = astring[i2];
        }
    }
    
    private float getOptionFloatValueOF(final Options p_getOptionFloatValueOF_1_) {
        if (p_getOptionFloatValueOF_1_ == Options.CLOUD_HEIGHT) {
            return this.ofCloudsHeight;
        }
        if (p_getOptionFloatValueOF_1_ == Options.AO_LEVEL) {
            return this.ofAoLevel;
        }
        if (p_getOptionFloatValueOF_1_ == Options.AA_LEVEL) {
            return (float)this.ofAaLevel;
        }
        if (p_getOptionFloatValueOF_1_ == Options.AF_LEVEL) {
            return (float)this.ofAfLevel;
        }
        if (p_getOptionFloatValueOF_1_ == Options.MIPMAP_TYPE) {
            return (float)this.ofMipmapType;
        }
        if (p_getOptionFloatValueOF_1_ == Options.FRAMERATE_LIMIT) {
            return (this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync) ? 0.0f : ((float)this.limitFramerate);
        }
        if (p_getOptionFloatValueOF_1_ != Options.FULLSCREEN_MODE) {
            return Float.MAX_VALUE;
        }
        if (this.ofFullscreenMode.equals("Default")) {
            return 0.0f;
        }
        final List list = Arrays.asList(Config.getDisplayModeNames());
        final int i = list.indexOf(this.ofFullscreenMode);
        return (i < 0) ? 0.0f : ((float)(i + 1));
    }
    
    private void setOptionValueOF(final Options p_setOptionValueOF_1_, final int p_setOptionValueOF_2_) {
        if (p_setOptionValueOF_1_ == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    this.ofFogType = 2;
                    if (!Config.isFancyFogAvailable()) {
                        this.ofFogType = 3;
                        break;
                    }
                    break;
                }
                case 2: {
                    this.ofFogType = 3;
                    break;
                }
                case 3: {
                    this.ofFogType = 1;
                    break;
                }
                default: {
                    this.ofFogType = 1;
                    break;
                }
            }
        }
        if (p_setOptionValueOF_1_ == Options.FOG_START) {
            this.ofFogStart += 0.2f;
            if (this.ofFogStart > 0.81f) {
                this.ofFogStart = 0.2f;
            }
        }
        if (p_setOptionValueOF_1_ == Options.SMOOTH_FPS) {
            this.ofSmoothFps = !this.ofSmoothFps;
        }
        if (p_setOptionValueOF_1_ == Options.SMOOTH_WORLD) {
            this.ofSmoothWorld = !this.ofSmoothWorld;
            Config.updateThreadPriorities();
        }
        if (p_setOptionValueOF_1_ == Options.CLOUDS) {
            ++this.ofClouds;
            if (this.ofClouds > 3) {
                this.ofClouds = 0;
            }
            this.updateRenderClouds();
            this.mc.renderGlobal.resetClouds();
        }
        if (p_setOptionValueOF_1_ == Options.TREES) {
            this.ofTrees = nextValue(this.ofTrees, GameSettings.OF_TREES_VALUES);
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.DROPPED_ITEMS) {
            ++this.ofDroppedItems;
            if (this.ofDroppedItems > 2) {
                this.ofDroppedItems = 0;
            }
        }
        if (p_setOptionValueOF_1_ == Options.RAIN) {
            ++this.ofRain;
            if (this.ofRain > 3) {
                this.ofRain = 0;
            }
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_WATER) {
            ++this.ofAnimatedWater;
            if (this.ofAnimatedWater == 1) {
                ++this.ofAnimatedWater;
            }
            if (this.ofAnimatedWater > 2) {
                this.ofAnimatedWater = 0;
            }
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_LAVA) {
            ++this.ofAnimatedLava;
            if (this.ofAnimatedLava == 1) {
                ++this.ofAnimatedLava;
            }
            if (this.ofAnimatedLava > 2) {
                this.ofAnimatedLava = 0;
            }
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_FIRE) {
            this.ofAnimatedFire = !this.ofAnimatedFire;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_PORTAL) {
            this.ofAnimatedPortal = !this.ofAnimatedPortal;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_REDSTONE) {
            this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_EXPLOSION) {
            this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_FLAME) {
            this.ofAnimatedFlame = !this.ofAnimatedFlame;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_SMOKE) {
            this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
        }
        if (p_setOptionValueOF_1_ == Options.VOID_PARTICLES) {
            this.ofVoidParticles = !this.ofVoidParticles;
        }
        if (p_setOptionValueOF_1_ == Options.WATER_PARTICLES) {
            this.ofWaterParticles = !this.ofWaterParticles;
        }
        if (p_setOptionValueOF_1_ == Options.PORTAL_PARTICLES) {
            this.ofPortalParticles = !this.ofPortalParticles;
        }
        if (p_setOptionValueOF_1_ == Options.POTION_PARTICLES) {
            this.ofPotionParticles = !this.ofPotionParticles;
        }
        if (p_setOptionValueOF_1_ == Options.FIREWORK_PARTICLES) {
            this.ofFireworkParticles = !this.ofFireworkParticles;
        }
        if (p_setOptionValueOF_1_ == Options.DRIPPING_WATER_LAVA) {
            this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_TERRAIN) {
            this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_TEXTURES) {
            this.ofAnimatedTextures = !this.ofAnimatedTextures;
        }
        if (p_setOptionValueOF_1_ == Options.RAIN_SPLASH) {
            this.ofRainSplash = !this.ofRainSplash;
        }
        if (p_setOptionValueOF_1_ == Options.LAGOMETER) {
            this.ofLagometer = !this.ofLagometer;
        }
        if (p_setOptionValueOF_1_ == Options.SHOW_FPS) {
            this.ofShowFps = !this.ofShowFps;
        }
        if (p_setOptionValueOF_1_ == Options.AUTOSAVE_TICKS) {
            final int i = 900;
            this.ofAutoSaveTicks = Math.max(this.ofAutoSaveTicks / i * i, i);
            this.ofAutoSaveTicks *= 2;
            if (this.ofAutoSaveTicks > 32 * i) {
                this.ofAutoSaveTicks = i;
            }
        }
        if (p_setOptionValueOF_1_ == Options.BETTER_GRASS) {
            ++this.ofBetterGrass;
            if (this.ofBetterGrass > 3) {
                this.ofBetterGrass = 1;
            }
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.CONNECTED_TEXTURES) {
            ++this.ofConnectedTextures;
            if (this.ofConnectedTextures > 3) {
                this.ofConnectedTextures = 1;
            }
            if (this.ofConnectedTextures == 2) {
                this.mc.renderGlobal.loadRenderers();
            }
            else {
                this.mc.refreshResources();
            }
        }
        if (p_setOptionValueOF_1_ == Options.WEATHER) {
            this.ofWeather = !this.ofWeather;
        }
        if (p_setOptionValueOF_1_ == Options.SKY) {
            this.ofSky = !this.ofSky;
        }
        if (p_setOptionValueOF_1_ == Options.STARS) {
            this.ofStars = !this.ofStars;
        }
        if (p_setOptionValueOF_1_ == Options.SUN_MOON) {
            this.ofSunMoon = !this.ofSunMoon;
        }
        if (p_setOptionValueOF_1_ == Options.VIGNETTE) {
            ++this.ofVignette;
            if (this.ofVignette > 2) {
                this.ofVignette = 0;
            }
        }
        if (p_setOptionValueOF_1_ == Options.CHUNK_UPDATES) {
            ++this.ofChunkUpdates;
            if (this.ofChunkUpdates > 5) {
                this.ofChunkUpdates = 1;
            }
        }
        if (p_setOptionValueOF_1_ == Options.CHUNK_UPDATES_DYNAMIC) {
            this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
        }
        if (p_setOptionValueOF_1_ == Options.TIME) {
            ++this.ofTime;
            if (this.ofTime > 2) {
                this.ofTime = 0;
            }
        }
        if (p_setOptionValueOF_1_ == Options.CLEAR_WATER) {
            this.ofClearWater = !this.ofClearWater;
            this.updateWaterOpacity();
        }
        if (p_setOptionValueOF_1_ == Options.PROFILER) {
            this.ofProfiler = !this.ofProfiler;
        }
        if (p_setOptionValueOF_1_ == Options.BETTER_SNOW) {
            this.ofBetterSnow = !this.ofBetterSnow;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.SWAMP_COLORS) {
            this.ofSwampColors = !this.ofSwampColors;
            CustomColors.updateUseDefaultGrassFoliageColors();
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.RANDOM_ENTITIES) {
            this.ofRandomEntities = !this.ofRandomEntities;
            RandomEntities.update();
        }
        if (p_setOptionValueOF_1_ == Options.SMOOTH_BIOMES) {
            this.ofSmoothBiomes = !this.ofSmoothBiomes;
            CustomColors.updateUseDefaultGrassFoliageColors();
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.CUSTOM_FONTS) {
            this.ofCustomFonts = !this.ofCustomFonts;
            this.mc.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
            this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
        }
        if (p_setOptionValueOF_1_ == Options.CUSTOM_COLORS) {
            this.ofCustomColors = !this.ofCustomColors;
            CustomColors.update();
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.CUSTOM_ITEMS) {
            this.ofCustomItems = !this.ofCustomItems;
            this.mc.refreshResources();
        }
        if (p_setOptionValueOF_1_ == Options.CUSTOM_SKY) {
            this.ofCustomSky = !this.ofCustomSky;
            CustomSky.update();
        }
        if (p_setOptionValueOF_1_ == Options.SHOW_CAPES) {
            this.ofShowCapes = !this.ofShowCapes;
        }
        if (p_setOptionValueOF_1_ == Options.NATURAL_TEXTURES) {
            this.ofNaturalTextures = !this.ofNaturalTextures;
            NaturalTextures.update();
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.EMISSIVE_TEXTURES) {
            this.ofEmissiveTextures = !this.ofEmissiveTextures;
            this.mc.refreshResources();
        }
        if (p_setOptionValueOF_1_ == Options.FAST_MATH) {
            this.ofFastMath = !this.ofFastMath;
            MathHelper.fastMath = this.ofFastMath;
        }
        if (p_setOptionValueOF_1_ == Options.FAST_RENDER) {
            if (!this.ofFastRender && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.fr.shaders1"), Lang.get("of.message.fr.shaders2"));
                return;
            }
            this.ofFastRender = !this.ofFastRender;
            if (this.ofFastRender) {
                this.mc.entityRenderer.stopUseShader();
            }
            Config.updateFramebufferSize();
        }
        if (p_setOptionValueOF_1_ == Options.TRANSLUCENT_BLOCKS) {
            if (this.ofTranslucentBlocks == 0) {
                this.ofTranslucentBlocks = 1;
            }
            else if (this.ofTranslucentBlocks == 1) {
                this.ofTranslucentBlocks = 2;
            }
            else if (this.ofTranslucentBlocks == 2) {
                this.ofTranslucentBlocks = 0;
            }
            else {
                this.ofTranslucentBlocks = 0;
            }
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.LAZY_CHUNK_LOADING) {
            this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
        }
        if (p_setOptionValueOF_1_ == Options.RENDER_REGIONS) {
            this.ofRenderRegions = !this.ofRenderRegions;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.SMART_ANIMATIONS) {
            this.ofSmartAnimations = !this.ofSmartAnimations;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.DYNAMIC_FOV) {
            this.ofDynamicFov = !this.ofDynamicFov;
        }
        if (p_setOptionValueOF_1_ == Options.ALTERNATE_BLOCKS) {
            this.ofAlternateBlocks = !this.ofAlternateBlocks;
            this.mc.refreshResources();
        }
        if (p_setOptionValueOF_1_ == Options.DYNAMIC_LIGHTS) {
            this.ofDynamicLights = nextValue(this.ofDynamicLights, GameSettings.OF_DYNAMIC_LIGHTS);
            DynamicLights.removeLights(this.mc.renderGlobal);
        }
        if (p_setOptionValueOF_1_ == Options.SCREENSHOT_SIZE) {
            ++this.ofScreenshotSize;
            if (this.ofScreenshotSize > 4) {
                this.ofScreenshotSize = 1;
            }
            if (!OpenGlHelper.isFramebufferEnabled()) {
                this.ofScreenshotSize = 1;
            }
        }
        if (p_setOptionValueOF_1_ == Options.CUSTOM_ENTITY_MODELS) {
            this.ofCustomEntityModels = !this.ofCustomEntityModels;
            this.mc.refreshResources();
        }
        if (p_setOptionValueOF_1_ == Options.CUSTOM_GUIS) {
            this.ofCustomGuis = !this.ofCustomGuis;
            CustomGuis.update();
        }
        if (p_setOptionValueOF_1_ == Options.SHOW_GL_ERRORS) {
            this.ofShowGlErrors = !this.ofShowGlErrors;
        }
        if (p_setOptionValueOF_1_ == Options.HELD_ITEM_TOOLTIPS) {
            this.heldItemTooltips = !this.heldItemTooltips;
        }
        if (p_setOptionValueOF_1_ == Options.ADVANCED_TOOLTIPS) {
            this.advancedItemTooltips = !this.advancedItemTooltips;
        }
    }
    
    private String getKeyBindingOF(final Options p_getKeyBindingOF_1_) {
        String s = String.valueOf(I18n.format(p_getKeyBindingOF_1_.getEnumString(), new Object[0])) + ": ";
        if (s == null) {
            s = p_getKeyBindingOF_1_.getEnumString();
        }
        if (p_getKeyBindingOF_1_ == Options.RENDER_DISTANCE) {
            final int i1 = (int)this.getOptionFloatValue(p_getKeyBindingOF_1_);
            String s2 = I18n.format("options.renderDistance.tiny", new Object[0]);
            int j = 2;
            if (i1 >= 4) {
                s2 = I18n.format("options.renderDistance.short", new Object[0]);
                j = 4;
            }
            if (i1 >= 8) {
                s2 = I18n.format("options.renderDistance.normal", new Object[0]);
                j = 8;
            }
            if (i1 >= 16) {
                s2 = I18n.format("options.renderDistance.far", new Object[0]);
                j = 16;
            }
            if (i1 >= 32) {
                s2 = Lang.get("of.options.renderDistance.extreme");
                j = 32;
            }
            if (i1 >= 48) {
                s2 = Lang.get("of.options.renderDistance.insane");
                j = 48;
            }
            if (i1 >= 64) {
                s2 = Lang.get("of.options.renderDistance.ludicrous");
                j = 64;
            }
            final int k = this.renderDistanceChunks - j;
            String s3 = s2;
            if (k > 0) {
                s3 = String.valueOf(s2) + "+";
            }
            return String.valueOf(s) + i1 + " " + s3;
        }
        if (p_getKeyBindingOF_1_ == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    return String.valueOf(s) + Lang.getFast();
                }
                case 2: {
                    return String.valueOf(s) + Lang.getFancy();
                }
                case 3: {
                    return String.valueOf(s) + Lang.getOff();
                }
                default: {
                    return String.valueOf(s) + Lang.getOff();
                }
            }
        }
        else {
            if (p_getKeyBindingOF_1_ == Options.FOG_START) {
                return String.valueOf(s) + this.ofFogStart;
            }
            if (p_getKeyBindingOF_1_ == Options.MIPMAP_TYPE) {
                switch (this.ofMipmapType) {
                    case 0: {
                        return String.valueOf(s) + Lang.get("of.options.mipmap.nearest");
                    }
                    case 1: {
                        return String.valueOf(s) + Lang.get("of.options.mipmap.linear");
                    }
                    case 2: {
                        return String.valueOf(s) + Lang.get("of.options.mipmap.bilinear");
                    }
                    case 3: {
                        return String.valueOf(s) + Lang.get("of.options.mipmap.trilinear");
                    }
                    default: {
                        return String.valueOf(s) + "of.options.mipmap.nearest";
                    }
                }
            }
            else {
                if (p_getKeyBindingOF_1_ == Options.SMOOTH_FPS) {
                    return this.ofSmoothFps ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                }
                if (p_getKeyBindingOF_1_ == Options.SMOOTH_WORLD) {
                    return this.ofSmoothWorld ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                }
                if (p_getKeyBindingOF_1_ == Options.CLOUDS) {
                    switch (this.ofClouds) {
                        case 1: {
                            return String.valueOf(s) + Lang.getFast();
                        }
                        case 2: {
                            return String.valueOf(s) + Lang.getFancy();
                        }
                        case 3: {
                            return String.valueOf(s) + Lang.getOff();
                        }
                        default: {
                            return String.valueOf(s) + Lang.getDefault();
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.TREES) {
                    switch (this.ofTrees) {
                        case 1: {
                            return String.valueOf(s) + Lang.getFast();
                        }
                        case 2: {
                            return String.valueOf(s) + Lang.getFancy();
                        }
                        default: {
                            return String.valueOf(s) + Lang.getDefault();
                        }
                        case 4: {
                            return String.valueOf(s) + Lang.get("of.general.smart");
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.DROPPED_ITEMS) {
                    switch (this.ofDroppedItems) {
                        case 1: {
                            return String.valueOf(s) + Lang.getFast();
                        }
                        case 2: {
                            return String.valueOf(s) + Lang.getFancy();
                        }
                        default: {
                            return String.valueOf(s) + Lang.getDefault();
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.RAIN) {
                    switch (this.ofRain) {
                        case 1: {
                            return String.valueOf(s) + Lang.getFast();
                        }
                        case 2: {
                            return String.valueOf(s) + Lang.getFancy();
                        }
                        case 3: {
                            return String.valueOf(s) + Lang.getOff();
                        }
                        default: {
                            return String.valueOf(s) + Lang.getDefault();
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.ANIMATED_WATER) {
                    switch (this.ofAnimatedWater) {
                        case 1: {
                            return String.valueOf(s) + Lang.get("of.options.animation.dynamic");
                        }
                        case 2: {
                            return String.valueOf(s) + Lang.getOff();
                        }
                        default: {
                            return String.valueOf(s) + Lang.getOn();
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.ANIMATED_LAVA) {
                    switch (this.ofAnimatedLava) {
                        case 1: {
                            return String.valueOf(s) + Lang.get("of.options.animation.dynamic");
                        }
                        case 2: {
                            return String.valueOf(s) + Lang.getOff();
                        }
                        default: {
                            return String.valueOf(s) + Lang.getOn();
                        }
                    }
                }
                else {
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_FIRE) {
                        return this.ofAnimatedFire ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_PORTAL) {
                        return this.ofAnimatedPortal ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_REDSTONE) {
                        return this.ofAnimatedRedstone ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_EXPLOSION) {
                        return this.ofAnimatedExplosion ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_FLAME) {
                        return this.ofAnimatedFlame ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_SMOKE) {
                        return this.ofAnimatedSmoke ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.VOID_PARTICLES) {
                        return this.ofVoidParticles ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.WATER_PARTICLES) {
                        return this.ofWaterParticles ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.PORTAL_PARTICLES) {
                        return this.ofPortalParticles ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.POTION_PARTICLES) {
                        return this.ofPotionParticles ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.FIREWORK_PARTICLES) {
                        return this.ofFireworkParticles ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.DRIPPING_WATER_LAVA) {
                        return this.ofDrippingWaterLava ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_TERRAIN) {
                        return this.ofAnimatedTerrain ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_TEXTURES) {
                        return this.ofAnimatedTextures ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.RAIN_SPLASH) {
                        return this.ofRainSplash ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.LAGOMETER) {
                        return this.ofLagometer ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.SHOW_FPS) {
                        return this.ofShowFps ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.AUTOSAVE_TICKS) {
                        final int l = 900;
                        return (this.ofAutoSaveTicks <= l) ? (String.valueOf(s) + Lang.get("of.options.save.45s")) : ((this.ofAutoSaveTicks <= 2 * l) ? (String.valueOf(s) + Lang.get("of.options.save.90s")) : ((this.ofAutoSaveTicks <= 4 * l) ? (String.valueOf(s) + Lang.get("of.options.save.3min")) : ((this.ofAutoSaveTicks <= 8 * l) ? (String.valueOf(s) + Lang.get("of.options.save.6min")) : ((this.ofAutoSaveTicks <= 16 * l) ? (String.valueOf(s) + Lang.get("of.options.save.12min")) : (String.valueOf(s) + Lang.get("of.options.save.24min"))))));
                    }
                    if (p_getKeyBindingOF_1_ == Options.BETTER_GRASS) {
                        switch (this.ofBetterGrass) {
                            case 1: {
                                return String.valueOf(s) + Lang.getFast();
                            }
                            case 2: {
                                return String.valueOf(s) + Lang.getFancy();
                            }
                            default: {
                                return String.valueOf(s) + Lang.getOff();
                            }
                        }
                    }
                    else if (p_getKeyBindingOF_1_ == Options.CONNECTED_TEXTURES) {
                        switch (this.ofConnectedTextures) {
                            case 1: {
                                return String.valueOf(s) + Lang.getFast();
                            }
                            case 2: {
                                return String.valueOf(s) + Lang.getFancy();
                            }
                            default: {
                                return String.valueOf(s) + Lang.getOff();
                            }
                        }
                    }
                    else {
                        if (p_getKeyBindingOF_1_ == Options.WEATHER) {
                            return this.ofWeather ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                        }
                        if (p_getKeyBindingOF_1_ == Options.SKY) {
                            return this.ofSky ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                        }
                        if (p_getKeyBindingOF_1_ == Options.STARS) {
                            return this.ofStars ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                        }
                        if (p_getKeyBindingOF_1_ == Options.SUN_MOON) {
                            return this.ofSunMoon ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                        }
                        if (p_getKeyBindingOF_1_ == Options.VIGNETTE) {
                            switch (this.ofVignette) {
                                case 1: {
                                    return String.valueOf(s) + Lang.getFast();
                                }
                                case 2: {
                                    return String.valueOf(s) + Lang.getFancy();
                                }
                                default: {
                                    return String.valueOf(s) + Lang.getDefault();
                                }
                            }
                        }
                        else {
                            if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES) {
                                return String.valueOf(s) + this.ofChunkUpdates;
                            }
                            if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES_DYNAMIC) {
                                return this.ofChunkUpdatesDynamic ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.TIME) {
                                return (this.ofTime == 1) ? (String.valueOf(s) + Lang.get("of.options.time.dayOnly")) : ((this.ofTime == 2) ? (String.valueOf(s) + Lang.get("of.options.time.nightOnly")) : (String.valueOf(s) + Lang.getDefault()));
                            }
                            if (p_getKeyBindingOF_1_ == Options.CLEAR_WATER) {
                                return this.ofClearWater ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.AA_LEVEL) {
                                String s4 = "";
                                if (this.ofAaLevel != Config.getAntialiasingLevel()) {
                                    s4 = " (" + Lang.get("of.general.restart") + ")";
                                }
                                return (this.ofAaLevel == 0) ? (String.valueOf(s) + Lang.getOff() + s4) : (String.valueOf(s) + this.ofAaLevel + s4);
                            }
                            if (p_getKeyBindingOF_1_ == Options.AF_LEVEL) {
                                return (this.ofAfLevel == 1) ? (String.valueOf(s) + Lang.getOff()) : (String.valueOf(s) + this.ofAfLevel);
                            }
                            if (p_getKeyBindingOF_1_ == Options.PROFILER) {
                                return this.ofProfiler ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.BETTER_SNOW) {
                                return this.ofBetterSnow ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.SWAMP_COLORS) {
                                return this.ofSwampColors ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.RANDOM_ENTITIES) {
                                return this.ofRandomEntities ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.SMOOTH_BIOMES) {
                                return this.ofSmoothBiomes ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.CUSTOM_FONTS) {
                                return this.ofCustomFonts ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.CUSTOM_COLORS) {
                                return this.ofCustomColors ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.CUSTOM_SKY) {
                                return this.ofCustomSky ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.SHOW_CAPES) {
                                return this.ofShowCapes ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.CUSTOM_ITEMS) {
                                return this.ofCustomItems ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.NATURAL_TEXTURES) {
                                return this.ofNaturalTextures ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.EMISSIVE_TEXTURES) {
                                return this.ofEmissiveTextures ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.FAST_MATH) {
                                return this.ofFastMath ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.FAST_RENDER) {
                                return this.ofFastRender ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.TRANSLUCENT_BLOCKS) {
                                return (this.ofTranslucentBlocks == 1) ? (String.valueOf(s) + Lang.getFast()) : ((this.ofTranslucentBlocks == 2) ? (String.valueOf(s) + Lang.getFancy()) : (String.valueOf(s) + Lang.getDefault()));
                            }
                            if (p_getKeyBindingOF_1_ == Options.LAZY_CHUNK_LOADING) {
                                return this.ofLazyChunkLoading ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.RENDER_REGIONS) {
                                return this.ofRenderRegions ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.SMART_ANIMATIONS) {
                                return this.ofSmartAnimations ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.DYNAMIC_FOV) {
                                return this.ofDynamicFov ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.ALTERNATE_BLOCKS) {
                                return this.ofAlternateBlocks ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.DYNAMIC_LIGHTS) {
                                final int m = indexOf(this.ofDynamicLights, GameSettings.OF_DYNAMIC_LIGHTS);
                                return String.valueOf(s) + getTranslation(GameSettings.KEYS_DYNAMIC_LIGHTS, m);
                            }
                            if (p_getKeyBindingOF_1_ == Options.SCREENSHOT_SIZE) {
                                return (this.ofScreenshotSize <= 1) ? (String.valueOf(s) + Lang.getDefault()) : (String.valueOf(s) + this.ofScreenshotSize + "x");
                            }
                            if (p_getKeyBindingOF_1_ == Options.CUSTOM_ENTITY_MODELS) {
                                return this.ofCustomEntityModels ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.CUSTOM_GUIS) {
                                return this.ofCustomGuis ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.SHOW_GL_ERRORS) {
                                return this.ofShowGlErrors ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.FULLSCREEN_MODE) {
                                return this.ofFullscreenMode.equals("Default") ? (String.valueOf(s) + Lang.getDefault()) : (String.valueOf(s) + this.ofFullscreenMode);
                            }
                            if (p_getKeyBindingOF_1_ == Options.HELD_ITEM_TOOLTIPS) {
                                return this.heldItemTooltips ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.ADVANCED_TOOLTIPS) {
                                return this.advancedItemTooltips ? (String.valueOf(s) + Lang.getOn()) : (String.valueOf(s) + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.FRAMERATE_LIMIT) {
                                final float f = this.getOptionFloatValue(p_getKeyBindingOF_1_);
                                return (f == 0.0f) ? (String.valueOf(s) + Lang.get("of.options.framerateLimit.vsync")) : ((f == p_getKeyBindingOF_1_.valueMax) ? (String.valueOf(s) + I18n.format("options.framerateLimit.max", new Object[0])) : (String.valueOf(s) + (int)f + " fps"));
                            }
                            return null;
                        }
                    }
                }
            }
        }
    }
    
    public void loadOfOptions() {
        try {
            File file1 = this.optionsFileOF;
            if (!file1.exists()) {
                file1 = this.optionsFile;
            }
            if (!file1.exists()) {
                return;
            }
            final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file1), "UTF-8"));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                try {
                    final String[] astring = s.split(":");
                    if (astring[0].equals("ofRenderDistanceChunks") && astring.length >= 2) {
                        this.renderDistanceChunks = Integer.valueOf(astring[1]);
                        this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 1024);
                    }
                    if (astring[0].equals("ofFogType") && astring.length >= 2) {
                        this.ofFogType = Integer.valueOf(astring[1]);
                        this.ofFogType = Config.limit(this.ofFogType, 1, 3);
                    }
                    if (astring[0].equals("ofFogStart") && astring.length >= 2) {
                        this.ofFogStart = Float.valueOf(astring[1]);
                        if (this.ofFogStart < 0.2f) {
                            this.ofFogStart = 0.2f;
                        }
                        if (this.ofFogStart > 0.81f) {
                            this.ofFogStart = 0.8f;
                        }
                    }
                    if (astring[0].equals("ofMipmapType") && astring.length >= 2) {
                        this.ofMipmapType = Integer.valueOf(astring[1]);
                        this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
                    }
                    if (astring[0].equals("ofOcclusionFancy") && astring.length >= 2) {
                        this.ofOcclusionFancy = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofSmoothFps") && astring.length >= 2) {
                        this.ofSmoothFps = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofSmoothWorld") && astring.length >= 2) {
                        this.ofSmoothWorld = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAoLevel") && astring.length >= 2) {
                        this.ofAoLevel = Float.valueOf(astring[1]);
                        this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0f, 1.0f);
                    }
                    if (astring[0].equals("ofClouds") && astring.length >= 2) {
                        this.ofClouds = Integer.valueOf(astring[1]);
                        this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                        this.updateRenderClouds();
                    }
                    if (astring[0].equals("ofCloudsHeight") && astring.length >= 2) {
                        this.ofCloudsHeight = Float.valueOf(astring[1]);
                        this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0f, 1.0f);
                    }
                    if (astring[0].equals("ofTrees") && astring.length >= 2) {
                        this.ofTrees = Integer.valueOf(astring[1]);
                        this.ofTrees = limit(this.ofTrees, GameSettings.OF_TREES_VALUES);
                    }
                    if (astring[0].equals("ofDroppedItems") && astring.length >= 2) {
                        this.ofDroppedItems = Integer.valueOf(astring[1]);
                        this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
                    }
                    if (astring[0].equals("ofRain") && astring.length >= 2) {
                        this.ofRain = Integer.valueOf(astring[1]);
                        this.ofRain = Config.limit(this.ofRain, 0, 3);
                    }
                    if (astring[0].equals("ofAnimatedWater") && astring.length >= 2) {
                        this.ofAnimatedWater = Integer.valueOf(astring[1]);
                        this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
                    }
                    if (astring[0].equals("ofAnimatedLava") && astring.length >= 2) {
                        this.ofAnimatedLava = Integer.valueOf(astring[1]);
                        this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
                    }
                    if (astring[0].equals("ofAnimatedFire") && astring.length >= 2) {
                        this.ofAnimatedFire = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAnimatedPortal") && astring.length >= 2) {
                        this.ofAnimatedPortal = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAnimatedRedstone") && astring.length >= 2) {
                        this.ofAnimatedRedstone = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAnimatedExplosion") && astring.length >= 2) {
                        this.ofAnimatedExplosion = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAnimatedFlame") && astring.length >= 2) {
                        this.ofAnimatedFlame = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAnimatedSmoke") && astring.length >= 2) {
                        this.ofAnimatedSmoke = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofVoidParticles") && astring.length >= 2) {
                        this.ofVoidParticles = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofWaterParticles") && astring.length >= 2) {
                        this.ofWaterParticles = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofPortalParticles") && astring.length >= 2) {
                        this.ofPortalParticles = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofPotionParticles") && astring.length >= 2) {
                        this.ofPotionParticles = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofFireworkParticles") && astring.length >= 2) {
                        this.ofFireworkParticles = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofDrippingWaterLava") && astring.length >= 2) {
                        this.ofDrippingWaterLava = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAnimatedTerrain") && astring.length >= 2) {
                        this.ofAnimatedTerrain = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAnimatedTextures") && astring.length >= 2) {
                        this.ofAnimatedTextures = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofRainSplash") && astring.length >= 2) {
                        this.ofRainSplash = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofLagometer") && astring.length >= 2) {
                        this.ofLagometer = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofShowFps") && astring.length >= 2) {
                        this.ofShowFps = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAutoSaveTicks") && astring.length >= 2) {
                        this.ofAutoSaveTicks = Integer.valueOf(astring[1]);
                        this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
                    }
                    if (astring[0].equals("ofBetterGrass") && astring.length >= 2) {
                        this.ofBetterGrass = Integer.valueOf(astring[1]);
                        this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
                    }
                    if (astring[0].equals("ofConnectedTextures") && astring.length >= 2) {
                        this.ofConnectedTextures = Integer.valueOf(astring[1]);
                        this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
                    }
                    if (astring[0].equals("ofWeather") && astring.length >= 2) {
                        this.ofWeather = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofSky") && astring.length >= 2) {
                        this.ofSky = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofStars") && astring.length >= 2) {
                        this.ofStars = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofSunMoon") && astring.length >= 2) {
                        this.ofSunMoon = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofVignette") && astring.length >= 2) {
                        this.ofVignette = Integer.valueOf(astring[1]);
                        this.ofVignette = Config.limit(this.ofVignette, 0, 2);
                    }
                    if (astring[0].equals("ofChunkUpdates") && astring.length >= 2) {
                        this.ofChunkUpdates = Integer.valueOf(astring[1]);
                        this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
                    }
                    if (astring[0].equals("ofChunkUpdatesDynamic") && astring.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofTime") && astring.length >= 2) {
                        this.ofTime = Integer.valueOf(astring[1]);
                        this.ofTime = Config.limit(this.ofTime, 0, 2);
                    }
                    if (astring[0].equals("ofClearWater") && astring.length >= 2) {
                        this.ofClearWater = Boolean.valueOf(astring[1]);
                        this.updateWaterOpacity();
                    }
                    if (astring[0].equals("ofAaLevel") && astring.length >= 2) {
                        this.ofAaLevel = Integer.valueOf(astring[1]);
                        this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
                    }
                    if (astring[0].equals("ofAfLevel") && astring.length >= 2) {
                        this.ofAfLevel = Integer.valueOf(astring[1]);
                        this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
                    }
                    if (astring[0].equals("ofProfiler") && astring.length >= 2) {
                        this.ofProfiler = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofBetterSnow") && astring.length >= 2) {
                        this.ofBetterSnow = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofSwampColors") && astring.length >= 2) {
                        this.ofSwampColors = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofRandomEntities") && astring.length >= 2) {
                        this.ofRandomEntities = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofSmoothBiomes") && astring.length >= 2) {
                        this.ofSmoothBiomes = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofCustomFonts") && astring.length >= 2) {
                        this.ofCustomFonts = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofCustomColors") && astring.length >= 2) {
                        this.ofCustomColors = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofCustomItems") && astring.length >= 2) {
                        this.ofCustomItems = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofCustomSky") && astring.length >= 2) {
                        this.ofCustomSky = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofShowCapes") && astring.length >= 2) {
                        this.ofShowCapes = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofNaturalTextures") && astring.length >= 2) {
                        this.ofNaturalTextures = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofEmissiveTextures") && astring.length >= 2) {
                        this.ofEmissiveTextures = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofLazyChunkLoading") && astring.length >= 2) {
                        this.ofLazyChunkLoading = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofRenderRegions") && astring.length >= 2) {
                        this.ofRenderRegions = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofSmartAnimations") && astring.length >= 2) {
                        this.ofSmartAnimations = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofDynamicFov") && astring.length >= 2) {
                        this.ofDynamicFov = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofAlternateBlocks") && astring.length >= 2) {
                        this.ofAlternateBlocks = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofDynamicLights") && astring.length >= 2) {
                        this.ofDynamicLights = Integer.valueOf(astring[1]);
                        this.ofDynamicLights = limit(this.ofDynamicLights, GameSettings.OF_DYNAMIC_LIGHTS);
                    }
                    if (astring[0].equals("ofScreenshotSize") && astring.length >= 2) {
                        this.ofScreenshotSize = Integer.valueOf(astring[1]);
                        this.ofScreenshotSize = Config.limit(this.ofScreenshotSize, 1, 4);
                    }
                    if (astring[0].equals("ofCustomEntityModels") && astring.length >= 2) {
                        this.ofCustomEntityModels = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofCustomGuis") && astring.length >= 2) {
                        this.ofCustomGuis = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofShowGlErrors") && astring.length >= 2) {
                        this.ofShowGlErrors = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofFullscreenMode") && astring.length >= 2) {
                        this.ofFullscreenMode = astring[1];
                    }
                    if (astring[0].equals("ofFastMath") && astring.length >= 2) {
                        this.ofFastMath = Boolean.valueOf(astring[1]);
                        MathHelper.fastMath = this.ofFastMath;
                    }
                    if (astring[0].equals("ofFastRender") && astring.length >= 2) {
                        this.ofFastRender = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofTranslucentBlocks") && astring.length >= 2) {
                        this.ofTranslucentBlocks = Integer.valueOf(astring[1]);
                        this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
                    }
                    if (!astring[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) {
                        continue;
                    }
                    this.ofKeyBindZoom.setKeyCode(Integer.parseInt(astring[1]));
                }
                catch (final Exception exception) {
                    Config.dbg("Skipping bad option: " + s);
                    exception.printStackTrace();
                }
            }
            KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[] { this.ofKeyBindZoom });
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (final Exception exception2) {
            Config.warn("Failed to load options");
            exception2.printStackTrace();
        }
    }
    
    public void saveOfOptions() {
        try {
            final PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFileOF), "UTF-8"));
            printwriter.println("ofFogType:" + this.ofFogType);
            printwriter.println("ofFogStart:" + this.ofFogStart);
            printwriter.println("ofMipmapType:" + this.ofMipmapType);
            printwriter.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
            printwriter.println("ofSmoothFps:" + this.ofSmoothFps);
            printwriter.println("ofSmoothWorld:" + this.ofSmoothWorld);
            printwriter.println("ofAoLevel:" + this.ofAoLevel);
            printwriter.println("ofClouds:" + this.ofClouds);
            printwriter.println("ofCloudsHeight:" + this.ofCloudsHeight);
            printwriter.println("ofTrees:" + this.ofTrees);
            printwriter.println("ofDroppedItems:" + this.ofDroppedItems);
            printwriter.println("ofRain:" + this.ofRain);
            printwriter.println("ofAnimatedWater:" + this.ofAnimatedWater);
            printwriter.println("ofAnimatedLava:" + this.ofAnimatedLava);
            printwriter.println("ofAnimatedFire:" + this.ofAnimatedFire);
            printwriter.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
            printwriter.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
            printwriter.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
            printwriter.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
            printwriter.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
            printwriter.println("ofVoidParticles:" + this.ofVoidParticles);
            printwriter.println("ofWaterParticles:" + this.ofWaterParticles);
            printwriter.println("ofPortalParticles:" + this.ofPortalParticles);
            printwriter.println("ofPotionParticles:" + this.ofPotionParticles);
            printwriter.println("ofFireworkParticles:" + this.ofFireworkParticles);
            printwriter.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
            printwriter.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
            printwriter.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
            printwriter.println("ofRainSplash:" + this.ofRainSplash);
            printwriter.println("ofLagometer:" + this.ofLagometer);
            printwriter.println("ofShowFps:" + this.ofShowFps);
            printwriter.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
            printwriter.println("ofBetterGrass:" + this.ofBetterGrass);
            printwriter.println("ofConnectedTextures:" + this.ofConnectedTextures);
            printwriter.println("ofWeather:" + this.ofWeather);
            printwriter.println("ofSky:" + this.ofSky);
            printwriter.println("ofStars:" + this.ofStars);
            printwriter.println("ofSunMoon:" + this.ofSunMoon);
            printwriter.println("ofVignette:" + this.ofVignette);
            printwriter.println("ofChunkUpdates:" + this.ofChunkUpdates);
            printwriter.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
            printwriter.println("ofTime:" + this.ofTime);
            printwriter.println("ofClearWater:" + this.ofClearWater);
            printwriter.println("ofAaLevel:" + this.ofAaLevel);
            printwriter.println("ofAfLevel:" + this.ofAfLevel);
            printwriter.println("ofProfiler:" + this.ofProfiler);
            printwriter.println("ofBetterSnow:" + this.ofBetterSnow);
            printwriter.println("ofSwampColors:" + this.ofSwampColors);
            printwriter.println("ofRandomEntities:" + this.ofRandomEntities);
            printwriter.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
            printwriter.println("ofCustomFonts:" + this.ofCustomFonts);
            printwriter.println("ofCustomColors:" + this.ofCustomColors);
            printwriter.println("ofCustomItems:" + this.ofCustomItems);
            printwriter.println("ofCustomSky:" + this.ofCustomSky);
            printwriter.println("ofShowCapes:" + this.ofShowCapes);
            printwriter.println("ofNaturalTextures:" + this.ofNaturalTextures);
            printwriter.println("ofEmissiveTextures:" + this.ofEmissiveTextures);
            printwriter.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
            printwriter.println("ofRenderRegions:" + this.ofRenderRegions);
            printwriter.println("ofSmartAnimations:" + this.ofSmartAnimations);
            printwriter.println("ofDynamicFov:" + this.ofDynamicFov);
            printwriter.println("ofAlternateBlocks:" + this.ofAlternateBlocks);
            printwriter.println("ofDynamicLights:" + this.ofDynamicLights);
            printwriter.println("ofScreenshotSize:" + this.ofScreenshotSize);
            printwriter.println("ofCustomEntityModels:" + this.ofCustomEntityModels);
            printwriter.println("ofCustomGuis:" + this.ofCustomGuis);
            printwriter.println("ofShowGlErrors:" + this.ofShowGlErrors);
            printwriter.println("ofFullscreenMode:" + this.ofFullscreenMode);
            printwriter.println("ofFastMath:" + this.ofFastMath);
            printwriter.println("ofFastRender:" + this.ofFastRender);
            printwriter.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
            printwriter.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
            printwriter.close();
        }
        catch (final Exception exception) {
            Config.warn("Failed to save options");
            exception.printStackTrace();
        }
    }
    
    private void updateRenderClouds() {
        switch (this.ofClouds) {
            case 1: {
                this.clouds = 1;
                break;
            }
            case 2: {
                this.clouds = 2;
                break;
            }
            case 3: {
                this.clouds = 0;
                break;
            }
            default: {
                if (this.fancyGraphics) {
                    this.clouds = 2;
                    break;
                }
                this.clouds = 1;
                break;
            }
        }
    }
    
    public void resetSettings() {
        this.renderDistanceChunks = 8;
        this.viewBobbing = true;
        this.anaglyph = false;
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.enableVsync = false;
        this.updateVSync();
        this.mipmapLevels = 4;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.clouds = 2;
        this.fovSetting = 70.0f;
        this.gammaSetting = 0.0f;
        this.guiScale = 0;
        this.particleSetting = 0;
        this.heldItemTooltips = true;
        this.useVbo = false;
        this.forceUnicodeFont = false;
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
        this.ofMipmapType = 0;
        this.ofOcclusionFancy = false;
        this.ofSmartAnimations = false;
        this.ofSmoothFps = false;
        Config.updateAvailableProcessors();
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = false;
        this.ofRenderRegions = false;
        this.ofFastMath = false;
        this.ofFastRender = false;
        this.ofTranslucentBlocks = 0;
        this.ofDynamicFov = true;
        this.ofAlternateBlocks = true;
        this.ofDynamicLights = 3;
        this.ofScreenshotSize = 1;
        this.ofCustomEntityModels = true;
        this.ofCustomGuis = true;
        this.ofShowGlErrors = true;
        this.ofAoLevel = 1.0f;
        this.ofAaLevel = 0;
        this.ofAfLevel = 1;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0f;
        this.ofTrees = 0;
        this.ofRain = 0;
        this.ofBetterGrass = 3;
        this.ofAutoSaveTicks = 4000;
        this.ofLagometer = false;
        this.ofShowFps = false;
        this.ofProfiler = false;
        this.ofWeather = true;
        this.ofSky = true;
        this.ofStars = true;
        this.ofSunMoon = true;
        this.ofVignette = 0;
        this.ofChunkUpdates = 1;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = "Default";
        this.ofSwampColors = true;
        this.ofRandomEntities = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomItems = true;
        this.ofCustomSky = true;
        this.ofShowCapes = true;
        this.ofConnectedTextures = 2;
        this.ofNaturalTextures = false;
        this.ofEmissiveTextures = true;
        this.ofAnimatedWater = 0;
        this.ofAnimatedLava = 0;
        this.ofAnimatedFire = true;
        this.ofAnimatedPortal = true;
        this.ofAnimatedRedstone = true;
        this.ofAnimatedExplosion = true;
        this.ofAnimatedFlame = true;
        this.ofAnimatedSmoke = true;
        this.ofVoidParticles = true;
        this.ofWaterParticles = true;
        this.ofRainSplash = true;
        this.ofPortalParticles = true;
        this.ofPotionParticles = true;
        this.ofFireworkParticles = true;
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        Shaders.setShaderPack("OFF");
        Shaders.configAntialiasingLevel = 0;
        Shaders.uninit();
        Shaders.storeConfig();
        this.updateWaterOpacity();
        this.mc.refreshResources();
        this.saveOptions();
    }
    
    public void updateVSync() {
        Display.setVSyncEnabled(this.enableVsync);
    }
    
    private void updateWaterOpacity() {
        if (Config.isIntegratedServerRunning()) {
            Config.waterOpacityChanged = true;
        }
        ClearWater.updateWaterOpacity(this, this.mc.theWorld);
    }
    
    public void setAllAnimations(final boolean p_setAllAnimations_1_) {
        final int i = p_setAllAnimations_1_ ? 0 : 2;
        this.ofAnimatedWater = i;
        this.ofAnimatedLava = i;
        this.ofAnimatedFire = p_setAllAnimations_1_;
        this.ofAnimatedPortal = p_setAllAnimations_1_;
        this.ofAnimatedRedstone = p_setAllAnimations_1_;
        this.ofAnimatedExplosion = p_setAllAnimations_1_;
        this.ofAnimatedFlame = p_setAllAnimations_1_;
        this.ofAnimatedSmoke = p_setAllAnimations_1_;
        this.ofVoidParticles = p_setAllAnimations_1_;
        this.ofWaterParticles = p_setAllAnimations_1_;
        this.ofRainSplash = p_setAllAnimations_1_;
        this.ofPortalParticles = p_setAllAnimations_1_;
        this.ofPotionParticles = p_setAllAnimations_1_;
        this.ofFireworkParticles = p_setAllAnimations_1_;
        this.particleSetting = (p_setAllAnimations_1_ ? 0 : 2);
        this.ofDrippingWaterLava = p_setAllAnimations_1_;
        this.ofAnimatedTerrain = p_setAllAnimations_1_;
        this.ofAnimatedTextures = p_setAllAnimations_1_;
    }
    
    private static int nextValue(final int p_nextValue_0_, final int[] p_nextValue_1_) {
        int i = indexOf(p_nextValue_0_, p_nextValue_1_);
        if (i < 0) {
            return p_nextValue_1_[0];
        }
        if (++i >= p_nextValue_1_.length) {
            i = 0;
        }
        return p_nextValue_1_[i];
    }
    
    private static int limit(final int p_limit_0_, final int[] p_limit_1_) {
        final int i = indexOf(p_limit_0_, p_limit_1_);
        return (i < 0) ? p_limit_1_[0] : p_limit_0_;
    }
    
    private static int indexOf(final int p_indexOf_0_, final int[] p_indexOf_1_) {
        for (int i = 0; i < p_indexOf_1_.length; ++i) {
            if (p_indexOf_1_[i] == p_indexOf_0_) {
                return i;
            }
        }
        return -1;
    }
    
    public enum Options
    {
        INVERT_MOUSE("INVERT_MOUSE", 0, "options.invertMouse", false, true), 
        SENSITIVITY("SENSITIVITY", 1, "options.sensitivity", true, false), 
        FOV("FOV", 2, "options.fov", true, false, 30.0f, 110.0f, 1.0f), 
        GAMMA("GAMMA", 3, "options.gamma", true, false), 
        SATURATION("SATURATION", 4, "options.saturation", true, false), 
        RENDER_DISTANCE("RENDER_DISTANCE", 5, "options.renderDistance", true, false, 2.0f, 16.0f, 1.0f), 
        VIEW_BOBBING("VIEW_BOBBING", 6, "options.viewBobbing", false, true), 
        ANAGLYPH("ANAGLYPH", 7, "options.anaglyph", false, true), 
        FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "options.framerateLimit", true, false, 0.0f, 260.0f, 5.0f), 
        FBO_ENABLE("FBO_ENABLE", 9, "options.fboEnable", false, true), 
        RENDER_CLOUDS("RENDER_CLOUDS", 10, "options.renderClouds", false, false), 
        GRAPHICS("GRAPHICS", 11, "options.graphics", false, false), 
        AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 12, "options.ao", false, false), 
        GUI_SCALE("GUI_SCALE", 13, "options.guiScale", false, false), 
        PARTICLES("PARTICLES", 14, "options.particles", false, false), 
        CHAT_VISIBILITY("CHAT_VISIBILITY", 15, "options.chat.visibility", false, false), 
        CHAT_COLOR("CHAT_COLOR", 16, "options.chat.color", false, true), 
        CHAT_LINKS("CHAT_LINKS", 17, "options.chat.links", false, true), 
        CHAT_OPACITY("CHAT_OPACITY", 18, "options.chat.opacity", true, false), 
        CHAT_LINKS_PROMPT("CHAT_LINKS_PROMPT", 19, "options.chat.links.prompt", false, true), 
        SNOOPER_ENABLED("SNOOPER_ENABLED", 20, "options.snooper", false, true), 
        USE_FULLSCREEN("USE_FULLSCREEN", 21, "options.fullscreen", false, true), 
        ENABLE_VSYNC("ENABLE_VSYNC", 22, "options.vsync", false, true), 
        USE_VBO("USE_VBO", 23, "options.vbo", false, true), 
        TOUCHSCREEN("TOUCHSCREEN", 24, "options.touchscreen", false, true), 
        CHAT_SCALE("CHAT_SCALE", 25, "options.chat.scale", true, false), 
        CHAT_WIDTH("CHAT_WIDTH", 26, "options.chat.width", true, false), 
        CHAT_HEIGHT_FOCUSED("CHAT_HEIGHT_FOCUSED", 27, "options.chat.height.focused", true, false), 
        CHAT_HEIGHT_UNFOCUSED("CHAT_HEIGHT_UNFOCUSED", 28, "options.chat.height.unfocused", true, false), 
        MIPMAP_LEVELS("MIPMAP_LEVELS", 29, "options.mipmapLevels", true, false, 0.0f, 4.0f, 1.0f), 
        FORCE_UNICODE_FONT("FORCE_UNICODE_FONT", 30, "options.forceUnicodeFont", false, true), 
        STREAM_BYTES_PER_PIXEL("STREAM_BYTES_PER_PIXEL", 31, "options.stream.bytesPerPixel", true, false), 
        STREAM_VOLUME_MIC("STREAM_VOLUME_MIC", 32, "options.stream.micVolumne", true, false), 
        STREAM_VOLUME_SYSTEM("STREAM_VOLUME_SYSTEM", 33, "options.stream.systemVolume", true, false), 
        STREAM_KBPS("STREAM_KBPS", 34, "options.stream.kbps", true, false), 
        STREAM_FPS("STREAM_FPS", 35, "options.stream.fps", true, false), 
        STREAM_COMPRESSION("STREAM_COMPRESSION", 36, "options.stream.compression", false, false), 
        STREAM_SEND_METADATA("STREAM_SEND_METADATA", 37, "options.stream.sendMetadata", false, true), 
        STREAM_CHAT_ENABLED("STREAM_CHAT_ENABLED", 38, "options.stream.chat.enabled", false, false), 
        STREAM_CHAT_USER_FILTER("STREAM_CHAT_USER_FILTER", 39, "options.stream.chat.userFilter", false, false), 
        STREAM_MIC_TOGGLE_BEHAVIOR("STREAM_MIC_TOGGLE_BEHAVIOR", 40, "options.stream.micToggleBehavior", false, false), 
        BLOCK_ALTERNATIVES("BLOCK_ALTERNATIVES", 41, "options.blockAlternatives", false, true), 
        REDUCED_DEBUG_INFO("REDUCED_DEBUG_INFO", 42, "options.reducedDebugInfo", false, true), 
        ENTITY_SHADOWS("ENTITY_SHADOWS", 43, "options.entityShadows", false, true), 
        REALMS_NOTIFICATIONS("REALMS_NOTIFICATIONS", 44, "options.realmsNotifications", false, true), 
        FOG_FANCY("FOG_FANCY", 45, "of.options.FOG_FANCY", false, false), 
        FOG_START("FOG_START", 46, "of.options.FOG_START", false, false), 
        MIPMAP_TYPE("MIPMAP_TYPE", 47, "of.options.MIPMAP_TYPE", true, false, 0.0f, 3.0f, 1.0f), 
        SMOOTH_FPS("SMOOTH_FPS", 48, "of.options.SMOOTH_FPS", false, false), 
        CLOUDS("CLOUDS", 49, "of.options.CLOUDS", false, false), 
        CLOUD_HEIGHT("CLOUD_HEIGHT", 50, "of.options.CLOUD_HEIGHT", true, false), 
        TREES("TREES", 51, "of.options.TREES", false, false), 
        RAIN("RAIN", 52, "of.options.RAIN", false, false), 
        ANIMATED_WATER("ANIMATED_WATER", 53, "of.options.ANIMATED_WATER", false, false), 
        ANIMATED_LAVA("ANIMATED_LAVA", 54, "of.options.ANIMATED_LAVA", false, false), 
        ANIMATED_FIRE("ANIMATED_FIRE", 55, "of.options.ANIMATED_FIRE", false, false), 
        ANIMATED_PORTAL("ANIMATED_PORTAL", 56, "of.options.ANIMATED_PORTAL", false, false), 
        AO_LEVEL("AO_LEVEL", 57, "of.options.AO_LEVEL", true, false), 
        LAGOMETER("LAGOMETER", 58, "of.options.LAGOMETER", false, false), 
        SHOW_FPS("SHOW_FPS", 59, "of.options.SHOW_FPS", false, false), 
        AUTOSAVE_TICKS("AUTOSAVE_TICKS", 60, "of.options.AUTOSAVE_TICKS", false, false), 
        BETTER_GRASS("BETTER_GRASS", 61, "of.options.BETTER_GRASS", false, false), 
        ANIMATED_REDSTONE("ANIMATED_REDSTONE", 62, "of.options.ANIMATED_REDSTONE", false, false), 
        ANIMATED_EXPLOSION("ANIMATED_EXPLOSION", 63, "of.options.ANIMATED_EXPLOSION", false, false), 
        ANIMATED_FLAME("ANIMATED_FLAME", 64, "of.options.ANIMATED_FLAME", false, false), 
        ANIMATED_SMOKE("ANIMATED_SMOKE", 65, "of.options.ANIMATED_SMOKE", false, false), 
        WEATHER("WEATHER", 66, "of.options.WEATHER", false, false), 
        SKY("SKY", 67, "of.options.SKY", false, false), 
        STARS("STARS", 68, "of.options.STARS", false, false), 
        SUN_MOON("SUN_MOON", 69, "of.options.SUN_MOON", false, false), 
        VIGNETTE("VIGNETTE", 70, "of.options.VIGNETTE", false, false), 
        CHUNK_UPDATES("CHUNK_UPDATES", 71, "of.options.CHUNK_UPDATES", false, false), 
        CHUNK_UPDATES_DYNAMIC("CHUNK_UPDATES_DYNAMIC", 72, "of.options.CHUNK_UPDATES_DYNAMIC", false, false), 
        TIME("TIME", 73, "of.options.TIME", false, false), 
        CLEAR_WATER("CLEAR_WATER", 74, "of.options.CLEAR_WATER", false, false), 
        SMOOTH_WORLD("SMOOTH_WORLD", 75, "of.options.SMOOTH_WORLD", false, false), 
        VOID_PARTICLES("VOID_PARTICLES", 76, "of.options.VOID_PARTICLES", false, false), 
        WATER_PARTICLES("WATER_PARTICLES", 77, "of.options.WATER_PARTICLES", false, false), 
        RAIN_SPLASH("RAIN_SPLASH", 78, "of.options.RAIN_SPLASH", false, false), 
        PORTAL_PARTICLES("PORTAL_PARTICLES", 79, "of.options.PORTAL_PARTICLES", false, false), 
        POTION_PARTICLES("POTION_PARTICLES", 80, "of.options.POTION_PARTICLES", false, false), 
        FIREWORK_PARTICLES("FIREWORK_PARTICLES", 81, "of.options.FIREWORK_PARTICLES", false, false), 
        PROFILER("PROFILER", 82, "of.options.PROFILER", false, false), 
        DRIPPING_WATER_LAVA("DRIPPING_WATER_LAVA", 83, "of.options.DRIPPING_WATER_LAVA", false, false), 
        BETTER_SNOW("BETTER_SNOW", 84, "of.options.BETTER_SNOW", false, false), 
        FULLSCREEN_MODE("FULLSCREEN_MODE", 85, "of.options.FULLSCREEN_MODE", true, false, 0.0f, (float)Config.getDisplayModes().length, 1.0f), 
        ANIMATED_TERRAIN("ANIMATED_TERRAIN", 86, "of.options.ANIMATED_TERRAIN", false, false), 
        SWAMP_COLORS("SWAMP_COLORS", 87, "of.options.SWAMP_COLORS", false, false), 
        RANDOM_ENTITIES("RANDOM_ENTITIES", 88, "of.options.RANDOM_ENTITIES", false, false), 
        SMOOTH_BIOMES("SMOOTH_BIOMES", 89, "of.options.SMOOTH_BIOMES", false, false), 
        CUSTOM_FONTS("CUSTOM_FONTS", 90, "of.options.CUSTOM_FONTS", false, false), 
        CUSTOM_COLORS("CUSTOM_COLORS", 91, "of.options.CUSTOM_COLORS", false, false), 
        SHOW_CAPES("SHOW_CAPES", 92, "of.options.SHOW_CAPES", false, false), 
        CONNECTED_TEXTURES("CONNECTED_TEXTURES", 93, "of.options.CONNECTED_TEXTURES", false, false), 
        CUSTOM_ITEMS("CUSTOM_ITEMS", 94, "of.options.CUSTOM_ITEMS", false, false), 
        AA_LEVEL("AA_LEVEL", 95, "of.options.AA_LEVEL", true, false, 0.0f, 16.0f, 1.0f), 
        AF_LEVEL("AF_LEVEL", 96, "of.options.AF_LEVEL", true, false, 1.0f, 16.0f, 1.0f), 
        ANIMATED_TEXTURES("ANIMATED_TEXTURES", 97, "of.options.ANIMATED_TEXTURES", false, false), 
        NATURAL_TEXTURES("NATURAL_TEXTURES", 98, "of.options.NATURAL_TEXTURES", false, false), 
        EMISSIVE_TEXTURES("EMISSIVE_TEXTURES", 99, "of.options.EMISSIVE_TEXTURES", false, false), 
        HELD_ITEM_TOOLTIPS("HELD_ITEM_TOOLTIPS", 100, "of.options.HELD_ITEM_TOOLTIPS", false, false), 
        DROPPED_ITEMS("DROPPED_ITEMS", 101, "of.options.DROPPED_ITEMS", false, false), 
        LAZY_CHUNK_LOADING("LAZY_CHUNK_LOADING", 102, "of.options.LAZY_CHUNK_LOADING", false, false), 
        CUSTOM_SKY("CUSTOM_SKY", 103, "of.options.CUSTOM_SKY", false, false), 
        FAST_MATH("FAST_MATH", 104, "of.options.FAST_MATH", false, false), 
        FAST_RENDER("FAST_RENDER", 105, "of.options.FAST_RENDER", false, false), 
        TRANSLUCENT_BLOCKS("TRANSLUCENT_BLOCKS", 106, "of.options.TRANSLUCENT_BLOCKS", false, false), 
        DYNAMIC_FOV("DYNAMIC_FOV", 107, "of.options.DYNAMIC_FOV", false, false), 
        DYNAMIC_LIGHTS("DYNAMIC_LIGHTS", 108, "of.options.DYNAMIC_LIGHTS", false, false), 
        ALTERNATE_BLOCKS("ALTERNATE_BLOCKS", 109, "of.options.ALTERNATE_BLOCKS", false, false), 
        CUSTOM_ENTITY_MODELS("CUSTOM_ENTITY_MODELS", 110, "of.options.CUSTOM_ENTITY_MODELS", false, false), 
        ADVANCED_TOOLTIPS("ADVANCED_TOOLTIPS", 111, "of.options.ADVANCED_TOOLTIPS", false, false), 
        SCREENSHOT_SIZE("SCREENSHOT_SIZE", 112, "of.options.SCREENSHOT_SIZE", false, false), 
        CUSTOM_GUIS("CUSTOM_GUIS", 113, "of.options.CUSTOM_GUIS", false, false), 
        RENDER_REGIONS("RENDER_REGIONS", 114, "of.options.RENDER_REGIONS", false, false), 
        SHOW_GL_ERRORS("SHOW_GL_ERRORS", 115, "of.options.SHOW_GL_ERRORS", false, false), 
        SMART_ANIMATIONS("SMART_ANIMATIONS", 116, "of.options.SMART_ANIMATIONS", false, false);
        
        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private float valueMin;
        private float valueMax;
        
        public static Options getEnumOptions(final int ordinal) {
            Options[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Options gamesettings$options = values[i];
                if (gamesettings$options.returnEnumOrdinal() == ordinal) {
                    return gamesettings$options;
                }
            }
            return null;
        }
        
        private Options(final String s, final int n, final String str, final boolean isFloat, final boolean isBoolean) {
            this(s, n, str, isFloat, isBoolean, 0.0f, 1.0f, 0.0f);
        }
        
        private Options(final String s, final int n, final String str, final boolean isFloat, final boolean isBoolean, final float valMin, final float valMax, final float valStep) {
            this.enumString = str;
            this.enumFloat = isFloat;
            this.enumBoolean = isBoolean;
            this.valueMin = valMin;
            this.valueMax = valMax;
            this.valueStep = valStep;
        }
        
        public boolean getEnumFloat() {
            return this.enumFloat;
        }
        
        public boolean getEnumBoolean() {
            return this.enumBoolean;
        }
        
        public int returnEnumOrdinal() {
            return this.ordinal();
        }
        
        public String getEnumString() {
            return this.enumString;
        }
        
        public float getValueMax() {
            return this.valueMax;
        }
        
        public void setValueMax(final float value) {
            this.valueMax = value;
        }
        
        public float normalizeValue(final float value) {
            return MathHelper.clamp_float((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0f, 1.0f);
        }
        
        public float denormalizeValue(final float value) {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(value, 0.0f, 1.0f));
        }
        
        public float snapToStepClamp(float value) {
            value = this.snapToStep(value);
            return MathHelper.clamp_float(value, this.valueMin, this.valueMax);
        }
        
        protected float snapToStep(float value) {
            if (this.valueStep > 0.0f) {
                value = this.valueStep * Math.round(value / this.valueStep);
            }
            return value;
        }
    }
}
