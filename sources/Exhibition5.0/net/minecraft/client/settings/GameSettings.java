// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.settings;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.World;
import net.minecraft.optifine.ClearWater;
import java.util.Arrays;
import net.minecraft.util.MathHelper;
import net.minecraft.optifine.NaturalTextures;
import net.minecraft.optifine.CustomSky;
import net.minecraft.optifine.RandomMobs;
import net.minecraft.optifine.CustomColorizer;
import net.minecraft.optifine.TextureUtils;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C15PacketClientSettings;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import net.minecraft.optifine.Reflector;
import java.lang.reflect.Type;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.client.gui.GuiNewChat;
import org.lwjgl.opengl.Display;
import net.minecraft.client.renderer.texture.TextureMap;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.collect.Maps;
import net.minecraft.client.audio.SoundCategory;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EnumPlayerModelParts;
import com.google.common.collect.Lists;
import net.minecraft.optifine.Config;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.client.Minecraft;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import java.io.File;
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
    public float mouseSensitivity;
    public boolean invertMouse;
    public int renderDistanceChunks;
    public boolean viewBobbing;
    public boolean anaglyph;
    public boolean fboEnable;
    public int limitFramerate;
    public int ofFogType;
    public float ofFogStart;
    public int ofMipmapType;
    public boolean ofLoadFar;
    public int ofPreloadedChunks;
    public boolean ofOcclusionFancy;
    public boolean ofSmoothWorld;
    public boolean ofLazyChunkLoading;
    public float ofAoLevel;
    public int ofAaLevel;
    public int ofClouds;
    public float ofCloudsHeight;
    public int ofTrees;
    public int ofRain;
    public int ofDroppedItems;
    public int ofBetterGrass;
    public int ofAutoSaveTicks;
    public boolean ofLagometer;
    public boolean ofProfiler;
    public boolean ofWeather;
    public boolean ofSky;
    public boolean ofStars;
    public boolean ofSunMoon;
    public int ofChunkUpdates;
    public int ofChunkLoading;
    public boolean ofChunkUpdatesDynamic;
    public int ofTime;
    public boolean ofClearWater;
    public boolean ofBetterSnow;
    public String ofFullscreenMode;
    public boolean ofSwampColors;
    public boolean ofRandomMobs;
    public boolean ofSmoothBiomes;
    public boolean ofCustomFonts;
    public boolean ofCustomColors;
    public boolean ofCustomSky;
    public boolean ofShowCapes;
    public int ofConnectedTextures;
    public boolean ofNaturalTextures;
    public boolean ofFastMath;
    public boolean ofFastRender;
    public int ofTranslucentBlocks;
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
    public boolean ofDrippingWaterLava;
    public boolean ofAnimatedTerrain;
    public boolean ofAnimatedTextures;
    public static final int DEFAULT = 0;
    public static final int FAST = 1;
    public static final int FANCY = 2;
    public static final int OFF = 3;
    public static final int ANIM_ON = 0;
    public static final int ANIM_GENERATED = 1;
    public static final int ANIM_OFF = 2;
    public static final int CL_DEFAULT = 0;
    public static final int CL_SMOOTH = 1;
    public static final int CL_THREADED = 2;
    public static final String DEFAULT_STR = "Default";
    public KeyBinding ofKeyBindZoom;
    private File optionsFileOF;
    public boolean clouds;
    public boolean fancyGraphics;
    public int ambientOcclusion;
    public List resourcePacks;
    public EntityPlayer.EnumChatVisibility chatVisibility;
    public boolean chatColours;
    public boolean chatLinks;
    public boolean chatLinksPrompt;
    public float chatOpacity;
    public boolean snooperEnabled;
    public boolean fullScreen;
    public boolean enableVsync;
    public boolean field_178881_t;
    public boolean field_178880_u;
    public boolean field_178879_v;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus;
    private final Set field_178882_aU;
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
    private Map mapSoundLevels;
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
    public KeyBinding keyBindForward;
    public KeyBinding keyBindLeft;
    public KeyBinding keyBindBack;
    public KeyBinding keyBindRight;
    public KeyBinding keyBindJump;
    public KeyBinding keyBindSneak;
    public KeyBinding keyBindInventory;
    public KeyBinding keyBindUseItem;
    public KeyBinding keyBindDrop;
    public KeyBinding keyBindAttack;
    public KeyBinding keyBindPickBlock;
    public KeyBinding keyBindSprint;
    public KeyBinding keyBindChat;
    public KeyBinding keyBindPlayerList;
    public KeyBinding keyBindCommand;
    public KeyBinding keyBindScreenshot;
    public KeyBinding keyBindTogglePerspective;
    public KeyBinding keyBindSmoothCamera;
    public KeyBinding keyBindFullscreen;
    public KeyBinding keybindSpectateView;
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
    private static final String __OBFID = "CL_00000650";
    
    public GameSettings(final Minecraft mcIn, final File p_i46326_2_) {
        this.mouseSensitivity = 0.5f;
        this.renderDistanceChunks = -1;
        this.viewBobbing = true;
        this.fboEnable = true;
        this.limitFramerate = 120;
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
        this.ofMipmapType = 0;
        this.ofLoadFar = false;
        this.ofPreloadedChunks = 0;
        this.ofOcclusionFancy = false;
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = Config.isSingleProcessor();
        this.ofAoLevel = 1.0f;
        this.ofAaLevel = 0;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0f;
        this.ofTrees = 0;
        this.ofRain = 0;
        this.ofDroppedItems = 0;
        this.ofBetterGrass = 3;
        this.ofAutoSaveTicks = 4000;
        this.ofLagometer = false;
        this.ofProfiler = false;
        this.ofWeather = true;
        this.ofSky = true;
        this.ofStars = true;
        this.ofSunMoon = true;
        this.ofChunkUpdates = 1;
        this.ofChunkLoading = 0;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = "Default";
        this.ofSwampColors = true;
        this.ofRandomMobs = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomSky = true;
        this.ofShowCapes = true;
        this.ofConnectedTextures = 2;
        this.ofNaturalTextures = false;
        this.ofFastMath = false;
        this.ofFastRender = true;
        this.ofTranslucentBlocks = 0;
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
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        this.clouds = true;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.resourcePacks = Lists.newArrayList();
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.chatColours = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.chatOpacity = 1.0f;
        this.snooperEnabled = true;
        this.enableVsync = true;
        this.field_178881_t = false;
        this.field_178880_u = true;
        this.field_178879_v = false;
        this.pauseOnLostFocus = true;
        this.field_178882_aU = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
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
        this.keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
        this.keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
        this.keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
        this.keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
        this.keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
        this.keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
        this.keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
        this.keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
        this.keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
        this.keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
        this.keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
        this.keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.gameplay");
        this.keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
        this.keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
        this.keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
        this.keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
        this.keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
        this.keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
        this.keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
        this.keybindSpectateView = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
        this.keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
        this.keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
        this.keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
        this.keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
        this.keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindSprint, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keybindSpectateView }, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_US";
        this.forceUnicodeFont = false;
        this.mc = mcIn;
        this.optionsFile = new File(p_i46326_2_, "options.txt");
        this.optionsFileOF = new File(p_i46326_2_, "optionsof.txt");
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.ofKeyBindZoom = new KeyBinding("Zoom", 46, "key.categories.misc");
        this.keyBindings = (KeyBinding[])ArrayUtils.add((Object[])this.keyBindings, (Object)this.ofKeyBindZoom);
        Options.RENDER_DISTANCE.setValueMax(32.0f);
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
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
        this.ofMipmapType = 0;
        this.ofLoadFar = false;
        this.ofPreloadedChunks = 0;
        this.ofOcclusionFancy = false;
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = Config.isSingleProcessor();
        this.ofAoLevel = 1.0f;
        this.ofAaLevel = 0;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0f;
        this.ofTrees = 0;
        this.ofRain = 0;
        this.ofDroppedItems = 0;
        this.ofBetterGrass = 3;
        this.ofAutoSaveTicks = 4000;
        this.ofLagometer = false;
        this.ofProfiler = false;
        this.ofWeather = true;
        this.ofSky = true;
        this.ofStars = true;
        this.ofSunMoon = true;
        this.ofChunkUpdates = 1;
        this.ofChunkLoading = 0;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = "Default";
        this.ofSwampColors = true;
        this.ofRandomMobs = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomSky = true;
        this.ofShowCapes = true;
        this.ofConnectedTextures = 2;
        this.ofNaturalTextures = false;
        this.ofFastMath = false;
        this.ofFastRender = true;
        this.ofTranslucentBlocks = 0;
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
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        this.clouds = true;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.resourcePacks = Lists.newArrayList();
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.chatColours = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.chatOpacity = 1.0f;
        this.snooperEnabled = true;
        this.enableVsync = true;
        this.field_178881_t = false;
        this.field_178880_u = true;
        this.field_178879_v = false;
        this.pauseOnLostFocus = true;
        this.field_178882_aU = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
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
        this.keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
        this.keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
        this.keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
        this.keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
        this.keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
        this.keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
        this.keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
        this.keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
        this.keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
        this.keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
        this.keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
        this.keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.gameplay");
        this.keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
        this.keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
        this.keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
        this.keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
        this.keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
        this.keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
        this.keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
        this.keybindSpectateView = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
        this.keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
        this.keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
        this.keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
        this.keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
        this.keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindSprint, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keybindSpectateView }, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_US";
        this.forceUnicodeFont = false;
    }
    
    public static String getKeyDisplayString(final int p_74298_0_) {
        return (p_74298_0_ < 0) ? I18n.format("key.mouseButton", p_74298_0_ + 101) : ((p_74298_0_ < 256) ? Keyboard.getKeyName(p_74298_0_) : String.format("%c", (char)(p_74298_0_ - 256)).toUpperCase());
    }
    
    public static boolean isKeyDown(final KeyBinding p_100015_0_) {
        final int keyCode = p_100015_0_.getKeyCode();
        return keyCode >= -100 && keyCode <= 255 && p_100015_0_.getKeyCode() != 0 && ((p_100015_0_.getKeyCode() < 0) ? Mouse.isButtonDown(p_100015_0_.getKeyCode() + 100) : Keyboard.isKeyDown(p_100015_0_.getKeyCode()));
    }
    
    public void setOptionKeyBinding(final KeyBinding p_151440_1_, final int p_151440_2_) {
        p_151440_1_.setKeyCode(p_151440_2_);
        this.saveOptions();
    }
    
    public void setOptionFloatValue(final Options p_74304_1_, final float p_74304_2_) {
        this.setOptionFloatValueOF(p_74304_1_, p_74304_2_);
        if (p_74304_1_ == Options.SENSITIVITY) {
            this.mouseSensitivity = p_74304_2_;
        }
        if (p_74304_1_ == Options.FOV) {
            this.fovSetting = p_74304_2_;
        }
        if (p_74304_1_ == Options.GAMMA) {
            this.gammaSetting = p_74304_2_;
        }
        if (p_74304_1_ == Options.FRAMERATE_LIMIT) {
            this.limitFramerate = (int)p_74304_2_;
            this.enableVsync = false;
            if (this.limitFramerate <= 0) {
                this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                this.enableVsync = true;
            }
            this.updateVSync();
        }
        if (p_74304_1_ == Options.CHAT_OPACITY) {
            this.chatOpacity = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.CHAT_HEIGHT_FOCUSED) {
            this.chatHeightFocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.CHAT_HEIGHT_UNFOCUSED) {
            this.chatHeightUnfocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.CHAT_WIDTH) {
            this.chatWidth = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.CHAT_SCALE) {
            this.chatScale = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.MIPMAP_LEVELS) {
            final int var3 = this.mipmapLevels;
            this.mipmapLevels = (int)p_74304_2_;
            if (var3 != p_74304_2_) {
                this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.mc.getTextureMapBlocks().func_174937_a(false, this.mipmapLevels > 0);
                this.mc.func_175603_A();
            }
        }
        if (p_74304_1_ == Options.BLOCK_ALTERNATIVES) {
            this.field_178880_u = !this.field_178880_u;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_74304_1_ == Options.RENDER_DISTANCE) {
            this.renderDistanceChunks = (int)p_74304_2_;
            this.mc.renderGlobal.func_174979_m();
        }
        if (p_74304_1_ == Options.STREAM_BYTES_PER_PIXEL) {
            this.streamBytesPerPixel = p_74304_2_;
        }
        if (p_74304_1_ == Options.STREAM_VOLUME_MIC) {
            this.streamMicVolume = p_74304_2_;
            this.mc.getTwitchStream().func_152915_s();
        }
        if (p_74304_1_ == Options.STREAM_VOLUME_SYSTEM) {
            this.streamGameVolume = p_74304_2_;
            this.mc.getTwitchStream().func_152915_s();
        }
        if (p_74304_1_ == Options.STREAM_KBPS) {
            this.streamKbps = p_74304_2_;
        }
        if (p_74304_1_ == Options.STREAM_FPS) {
            this.streamFps = p_74304_2_;
        }
    }
    
    public void setOptionValue(final Options p_74306_1_, final int p_74306_2_) {
        this.setOptionValueOF(p_74306_1_, p_74306_2_);
        if (p_74306_1_ == Options.INVERT_MOUSE) {
            this.invertMouse = !this.invertMouse;
        }
        if (p_74306_1_ == Options.GUI_SCALE) {
            this.guiScale = (this.guiScale + p_74306_2_ & 0x3);
        }
        if (p_74306_1_ == Options.PARTICLES) {
            this.particleSetting = (this.particleSetting + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.VIEW_BOBBING) {
            this.viewBobbing = !this.viewBobbing;
        }
        if (p_74306_1_ == Options.RENDER_CLOUDS) {
            this.clouds = !this.clouds;
        }
        if (p_74306_1_ == Options.FORCE_UNICODE_FONT) {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            this.mc.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }
        if (p_74306_1_ == Options.FBO_ENABLE) {
            this.fboEnable = !this.fboEnable;
        }
        if (p_74306_1_ == Options.ANAGLYPH) {
            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
        }
        if (p_74306_1_ == Options.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.AMBIENT_OCCLUSION) {
            this.ambientOcclusion = (this.ambientOcclusion + p_74306_2_) % 3;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.CHAT_VISIBILITY) {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + p_74306_2_) % 3);
        }
        if (p_74306_1_ == Options.STREAM_COMPRESSION) {
            this.streamCompression = (this.streamCompression + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.STREAM_SEND_METADATA) {
            this.streamSendMetadata = !this.streamSendMetadata;
        }
        if (p_74306_1_ == Options.STREAM_CHAT_ENABLED) {
            this.streamChatEnabled = (this.streamChatEnabled + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.STREAM_CHAT_USER_FILTER) {
            this.streamChatUserFilter = (this.streamChatUserFilter + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            this.streamMicToggleBehavior = (this.streamMicToggleBehavior + p_74306_2_) % 2;
        }
        if (p_74306_1_ == Options.CHAT_COLOR) {
            this.chatColours = !this.chatColours;
        }
        if (p_74306_1_ == Options.CHAT_LINKS) {
            this.chatLinks = !this.chatLinks;
        }
        if (p_74306_1_ == Options.CHAT_LINKS_PROMPT) {
            this.chatLinksPrompt = !this.chatLinksPrompt;
        }
        if (p_74306_1_ == Options.SNOOPER_ENABLED) {
            this.snooperEnabled = !this.snooperEnabled;
        }
        if (p_74306_1_ == Options.TOUCHSCREEN) {
            this.touchscreen = !this.touchscreen;
        }
        if (p_74306_1_ == Options.USE_FULLSCREEN) {
            this.fullScreen = !this.fullScreen;
            if (this.mc.isFullScreen() != this.fullScreen) {
                this.mc.toggleFullscreen();
            }
        }
        if (p_74306_1_ == Options.ENABLE_VSYNC) {
            Display.setVSyncEnabled(this.enableVsync = !this.enableVsync);
        }
        if (p_74306_1_ == Options.USE_VBO) {
            this.field_178881_t = !this.field_178881_t;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.BLOCK_ALTERNATIVES) {
            this.field_178880_u = !this.field_178880_u;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.REDUCED_DEBUG_INFO) {
            this.field_178879_v = !this.field_178879_v;
        }
        this.saveOptions();
    }
    
    public float getOptionFloatValue(final Options p_74296_1_) {
        return (p_74296_1_ == Options.CLOUD_HEIGHT) ? this.ofCloudsHeight : ((p_74296_1_ == Options.AO_LEVEL) ? this.ofAoLevel : ((p_74296_1_ == Options.FRAMERATE_LIMIT) ? ((this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync) ? 0.0f : this.limitFramerate) : ((p_74296_1_ == Options.FOV) ? this.fovSetting : ((p_74296_1_ == Options.GAMMA) ? this.gammaSetting : ((p_74296_1_ == Options.SATURATION) ? this.saturation : ((p_74296_1_ == Options.SENSITIVITY) ? this.mouseSensitivity : ((p_74296_1_ == Options.CHAT_OPACITY) ? this.chatOpacity : ((p_74296_1_ == Options.CHAT_HEIGHT_FOCUSED) ? this.chatHeightFocused : ((p_74296_1_ == Options.CHAT_HEIGHT_UNFOCUSED) ? this.chatHeightUnfocused : ((p_74296_1_ == Options.CHAT_SCALE) ? this.chatScale : ((p_74296_1_ == Options.CHAT_WIDTH) ? this.chatWidth : ((p_74296_1_ == Options.FRAMERATE_LIMIT) ? this.limitFramerate : ((p_74296_1_ == Options.MIPMAP_LEVELS) ? this.mipmapLevels : ((p_74296_1_ == Options.RENDER_DISTANCE) ? this.renderDistanceChunks : ((p_74296_1_ == Options.STREAM_BYTES_PER_PIXEL) ? this.streamBytesPerPixel : ((p_74296_1_ == Options.STREAM_VOLUME_MIC) ? this.streamMicVolume : ((p_74296_1_ == Options.STREAM_VOLUME_SYSTEM) ? this.streamGameVolume : ((p_74296_1_ == Options.STREAM_KBPS) ? this.streamKbps : ((p_74296_1_ == Options.STREAM_FPS) ? this.streamFps : 0.0f)))))))))))))))))));
    }
    
    public boolean getOptionOrdinalValue(final Options p_74308_1_) {
        switch (SwitchOptions.optionIds[p_74308_1_.ordinal()]) {
            case 1: {
                return this.invertMouse;
            }
            case 2: {
                return this.viewBobbing;
            }
            case 3: {
                return this.anaglyph;
            }
            case 4: {
                return this.fboEnable;
            }
            case 5: {
                return this.clouds;
            }
            case 6: {
                return this.chatColours;
            }
            case 7: {
                return this.chatLinks;
            }
            case 8: {
                return this.chatLinksPrompt;
            }
            case 9: {
                return this.snooperEnabled;
            }
            case 10: {
                return this.fullScreen;
            }
            case 11: {
                return this.enableVsync;
            }
            case 12: {
                return this.field_178881_t;
            }
            case 13: {
                return this.touchscreen;
            }
            case 14: {
                return this.streamSendMetadata;
            }
            case 15: {
                return this.forceUnicodeFont;
            }
            case 16: {
                return this.field_178880_u;
            }
            case 17: {
                return this.field_178879_v;
            }
            default: {
                return false;
            }
        }
    }
    
    private static String getTranslation(final String[] p_74299_0_, int p_74299_1_) {
        if (p_74299_1_ < 0 || p_74299_1_ >= p_74299_0_.length) {
            p_74299_1_ = 0;
        }
        return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
    }
    
    public String getKeyBinding(final Options p_74297_1_) {
        final String strOF = this.getKeyBindingOF(p_74297_1_);
        if (strOF != null) {
            return strOF;
        }
        final String var2 = I18n.format(p_74297_1_.getEnumString(), new Object[0]) + ": ";
        if (p_74297_1_.getEnumFloat()) {
            final float var3 = this.getOptionFloatValue(p_74297_1_);
            final float var4 = p_74297_1_.normalizeValue(var3);
            return (p_74297_1_ == Options.SENSITIVITY) ? ((var4 == 0.0f) ? (var2 + I18n.format("options.sensitivity.min", new Object[0])) : ((var4 == 1.0f) ? (var2 + I18n.format("options.sensitivity.max", new Object[0])) : (var2 + (int)(var4 * 200.0f) + "%"))) : ((p_74297_1_ == Options.FOV) ? ((var3 == 70.0f) ? (var2 + I18n.format("options.fov.min", new Object[0])) : ((var3 == 110.0f) ? (var2 + I18n.format("options.fov.max", new Object[0])) : (var2 + (int)var3))) : ((p_74297_1_ == Options.FRAMERATE_LIMIT) ? ((var3 == p_74297_1_.valueMax) ? (var2 + I18n.format("options.framerateLimit.max", new Object[0])) : (var2 + (int)var3 + " fps")) : ((p_74297_1_ == Options.RENDER_CLOUDS) ? ((var3 == p_74297_1_.valueMin) ? (var2 + I18n.format("options.cloudHeight.min", new Object[0])) : (var2 + ((int)var3 + 128))) : ((p_74297_1_ == Options.GAMMA) ? ((var4 == 0.0f) ? (var2 + I18n.format("options.gamma.min", new Object[0])) : ((var4 == 1.0f) ? (var2 + I18n.format("options.gamma.max", new Object[0])) : (var2 + "+" + (int)(var4 * 100.0f) + "%"))) : ((p_74297_1_ == Options.SATURATION) ? (var2 + (int)(var4 * 400.0f) + "%") : ((p_74297_1_ == Options.CHAT_OPACITY) ? (var2 + (int)(var4 * 90.0f + 10.0f) + "%") : ((p_74297_1_ == Options.CHAT_HEIGHT_UNFOCUSED) ? (var2 + GuiNewChat.calculateChatboxHeight(var4) + "px") : ((p_74297_1_ == Options.CHAT_HEIGHT_FOCUSED) ? (var2 + GuiNewChat.calculateChatboxHeight(var4) + "px") : ((p_74297_1_ == Options.CHAT_WIDTH) ? (var2 + GuiNewChat.calculateChatboxWidth(var4) + "px") : ((p_74297_1_ == Options.RENDER_DISTANCE) ? (var2 + (int)var3 + " chunks") : ((p_74297_1_ == Options.MIPMAP_LEVELS) ? ((var3 == 0.0f) ? (var2 + I18n.format("options.off", new Object[0])) : (var2 + (int)var3)) : ((p_74297_1_ == Options.STREAM_FPS) ? (var2 + TwitchStream.func_152948_a(var4) + " fps") : ((p_74297_1_ == Options.STREAM_KBPS) ? (var2 + TwitchStream.func_152946_b(var4) + " Kbps") : ((p_74297_1_ == Options.STREAM_BYTES_PER_PIXEL) ? (var2 + String.format("%.3f bpp", TwitchStream.func_152947_c(var4))) : ((var4 == 0.0f) ? (var2 + I18n.format("options.off", new Object[0])) : (var2 + (int)(var4 * 100.0f) + "%"))))))))))))))));
        }
        if (p_74297_1_.getEnumBoolean()) {
            final boolean var5 = this.getOptionOrdinalValue(p_74297_1_);
            return var5 ? (var2 + I18n.format("options.on", new Object[0])) : (var2 + I18n.format("options.off", new Object[0]));
        }
        if (p_74297_1_ == Options.GUI_SCALE) {
            return var2 + getTranslation(GameSettings.GUISCALES, this.guiScale);
        }
        if (p_74297_1_ == Options.CHAT_VISIBILITY) {
            return var2 + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
        }
        if (p_74297_1_ == Options.PARTICLES) {
            return var2 + getTranslation(GameSettings.PARTICLES, this.particleSetting);
        }
        if (p_74297_1_ == Options.AMBIENT_OCCLUSION) {
            return var2 + getTranslation(GameSettings.AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        if (p_74297_1_ == Options.STREAM_COMPRESSION) {
            return var2 + getTranslation(GameSettings.STREAM_COMPRESSIONS, this.streamCompression);
        }
        if (p_74297_1_ == Options.STREAM_CHAT_ENABLED) {
            return var2 + getTranslation(GameSettings.STREAM_CHAT_MODES, this.streamChatEnabled);
        }
        if (p_74297_1_ == Options.STREAM_CHAT_USER_FILTER) {
            return var2 + getTranslation(GameSettings.STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
        }
        if (p_74297_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return var2 + getTranslation(GameSettings.STREAM_MIC_MODES, this.streamMicToggleBehavior);
        }
        if (p_74297_1_ != Options.GRAPHICS) {
            return var2;
        }
        if (this.fancyGraphics) {
            return var2 + I18n.format("options.graphics.fancy", new Object[0]);
        }
        final String var6 = "options.graphics.fast";
        return var2 + I18n.format("options.graphics.fast", new Object[0]);
    }
    
    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            final BufferedReader var9 = new BufferedReader(new FileReader(this.optionsFile));
            String var10 = "";
            this.mapSoundLevels.clear();
            while ((var10 = var9.readLine()) != null) {
                try {
                    final String[] var11 = var10.split(":");
                    if (var11[0].equals("mouseSensitivity")) {
                        this.mouseSensitivity = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("fov")) {
                        this.fovSetting = this.parseFloat(var11[1]) * 40.0f + 70.0f;
                    }
                    if (var11[0].equals("gamma")) {
                        this.gammaSetting = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("saturation")) {
                        this.saturation = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("invertYMouse")) {
                        this.invertMouse = var11[1].equals("true");
                    }
                    if (var11[0].equals("renderDistance")) {
                        this.renderDistanceChunks = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("guiScale")) {
                        this.guiScale = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("particles")) {
                        this.particleSetting = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("bobView")) {
                        this.viewBobbing = var11[1].equals("true");
                    }
                    if (var11[0].equals("anaglyph3d")) {
                        this.anaglyph = var11[1].equals("true");
                    }
                    if (var11[0].equals("maxFps")) {
                        this.limitFramerate = Integer.parseInt(var11[1]);
                        this.enableVsync = false;
                        if (this.limitFramerate <= 0) {
                            this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                            this.enableVsync = true;
                        }
                        this.updateVSync();
                    }
                    if (var11[0].equals("fboEnable")) {
                        this.fboEnable = var11[1].equals("true");
                    }
                    if (var11[0].equals("difficulty")) {
                        this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(var11[1]));
                    }
                    if (var11[0].equals("fancyGraphics")) {
                        this.fancyGraphics = var11[1].equals("true");
                    }
                    if (var11[0].equals("ao")) {
                        if (var11[1].equals("true")) {
                            this.ambientOcclusion = 2;
                        }
                        else if (var11[1].equals("false")) {
                            this.ambientOcclusion = 0;
                        }
                        else {
                            this.ambientOcclusion = Integer.parseInt(var11[1]);
                        }
                    }
                    if (var11[0].equals("renderClouds")) {
                        this.clouds = var11[1].equals("true");
                    }
                    if (var11[0].equals("resourcePacks")) {
                        this.resourcePacks = (List)GameSettings.gson.fromJson(var10.substring(var10.indexOf(58) + 1), (Type)GameSettings.typeListString);
                        if (this.resourcePacks == null) {
                            this.resourcePacks = Lists.newArrayList();
                        }
                    }
                    if (var11[0].equals("lastServer") && var11.length >= 2) {
                        this.lastServer = var10.substring(var10.indexOf(58) + 1);
                    }
                    if (var11[0].equals("lang") && var11.length >= 2) {
                        this.language = var11[1];
                    }
                    if (var11[0].equals("chatVisibility")) {
                        this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(var11[1]));
                    }
                    if (var11[0].equals("chatColors")) {
                        this.chatColours = var11[1].equals("true");
                    }
                    if (var11[0].equals("chatLinks")) {
                        this.chatLinks = var11[1].equals("true");
                    }
                    if (var11[0].equals("chatLinksPrompt")) {
                        this.chatLinksPrompt = var11[1].equals("true");
                    }
                    if (var11[0].equals("chatOpacity")) {
                        this.chatOpacity = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("snooperEnabled")) {
                        this.snooperEnabled = var11[1].equals("true");
                    }
                    if (var11[0].equals("fullscreen")) {
                        this.fullScreen = var11[1].equals("true");
                    }
                    if (var11[0].equals("enableVsync")) {
                        this.enableVsync = var11[1].equals("true");
                        this.updateVSync();
                    }
                    if (var11[0].equals("useVbo")) {
                        this.field_178881_t = var11[1].equals("true");
                    }
                    if (var11[0].equals("hideServerAddress")) {
                        this.hideServerAddress = var11[1].equals("true");
                    }
                    if (var11[0].equals("advancedItemTooltips")) {
                        this.advancedItemTooltips = var11[1].equals("true");
                    }
                    if (var11[0].equals("pauseOnLostFocus")) {
                        this.pauseOnLostFocus = var11[1].equals("true");
                    }
                    if (var11[0].equals("touchscreen")) {
                        this.touchscreen = var11[1].equals("true");
                    }
                    if (var11[0].equals("overrideHeight")) {
                        this.overrideHeight = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("overrideWidth")) {
                        this.overrideWidth = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("heldItemTooltips")) {
                        this.heldItemTooltips = var11[1].equals("true");
                    }
                    if (var11[0].equals("chatHeightFocused")) {
                        this.chatHeightFocused = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("chatHeightUnfocused")) {
                        this.chatHeightUnfocused = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("chatScale")) {
                        this.chatScale = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("chatWidth")) {
                        this.chatWidth = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("showInventoryAchievementHint")) {
                        this.showInventoryAchievementHint = var11[1].equals("true");
                    }
                    if (var11[0].equals("mipmapLevels")) {
                        this.mipmapLevels = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("streamBytesPerPixel")) {
                        this.streamBytesPerPixel = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("streamMicVolume")) {
                        this.streamMicVolume = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("streamSystemVolume")) {
                        this.streamGameVolume = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("streamKbps")) {
                        this.streamKbps = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("streamFps")) {
                        this.streamFps = this.parseFloat(var11[1]);
                    }
                    if (var11[0].equals("streamCompression")) {
                        this.streamCompression = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("streamSendMetadata")) {
                        this.streamSendMetadata = var11[1].equals("true");
                    }
                    if (var11[0].equals("streamPreferredServer") && var11.length >= 2) {
                        this.streamPreferredServer = var10.substring(var10.indexOf(58) + 1);
                    }
                    if (var11[0].equals("streamChatEnabled")) {
                        this.streamChatEnabled = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("streamChatUserFilter")) {
                        this.streamChatUserFilter = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("streamMicToggleBehavior")) {
                        this.streamMicToggleBehavior = Integer.parseInt(var11[1]);
                    }
                    if (var11[0].equals("forceUnicodeFont")) {
                        this.forceUnicodeFont = var11[1].equals("true");
                    }
                    if (var11[0].equals("allowBlockAlternatives")) {
                        this.field_178880_u = var11[1].equals("true");
                    }
                    if (var11[0].equals("reducedDebugInfo")) {
                        this.field_178879_v = var11[1].equals("true");
                    }
                    for (final KeyBinding var15 : this.keyBindings) {
                        if (var11[0].equals("key_" + var15.getKeyDescription())) {
                            var15.setKeyCode(Integer.parseInt(var11[1]));
                        }
                    }
                    for (final SoundCategory var17 : SoundCategory.values()) {
                        if (var11[0].equals("soundCategory_" + var17.getCategoryName())) {
                            this.mapSoundLevels.put(var17, this.parseFloat(var11[1]));
                        }
                    }
                    for (final EnumPlayerModelParts var19 : EnumPlayerModelParts.values()) {
                        if (var11[0].equals("modelPart_" + var19.func_179329_c())) {
                            this.func_178878_a(var19, var11[1].equals("true"));
                        }
                    }
                }
                catch (Exception var20) {
                    GameSettings.logger.warn("Skipping bad option: " + var10);
                    var20.printStackTrace();
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            var9.close();
        }
        catch (Exception var21) {
            GameSettings.logger.error("Failed to load options", (Throwable)var21);
        }
        this.loadOfOptions();
    }
    
    private float parseFloat(final String p_74305_1_) {
        return p_74305_1_.equals("true") ? 1.0f : (p_74305_1_.equals("false") ? 0.0f : Float.parseFloat(p_74305_1_));
    }
    
    public void saveOptions() {
        if (Reflector.FMLClientHandler.exists()) {
            final Object var6 = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            if (var6 != null && Reflector.callBoolean(var6, Reflector.FMLClientHandler_isLoading, new Object[0])) {
                return;
            }
        }
        try {
            final PrintWriter var7 = new PrintWriter(new FileWriter(this.optionsFile));
            var7.println("invertYMouse:" + this.invertMouse);
            var7.println("mouseSensitivity:" + this.mouseSensitivity);
            var7.println("fov:" + (this.fovSetting - 70.0f) / 40.0f);
            var7.println("gamma:" + this.gammaSetting);
            var7.println("saturation:" + this.saturation);
            var7.println("renderDistance:" + this.renderDistanceChunks);
            var7.println("guiScale:" + this.guiScale);
            var7.println("particles:" + this.particleSetting);
            var7.println("bobView:" + this.viewBobbing);
            var7.println("anaglyph3d:" + this.anaglyph);
            var7.println("maxFps:" + this.limitFramerate);
            var7.println("fboEnable:" + this.fboEnable);
            var7.println("difficulty:" + this.difficulty.getDifficultyId());
            var7.println("fancyGraphics:" + this.fancyGraphics);
            var7.println("ao:" + this.ambientOcclusion);
            var7.println("renderClouds:" + this.clouds);
            var7.println("resourcePacks:" + GameSettings.gson.toJson((Object)this.resourcePacks));
            var7.println("lastServer:" + this.lastServer);
            var7.println("lang:" + this.language);
            var7.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
            var7.println("chatColors:" + this.chatColours);
            var7.println("chatLinks:" + this.chatLinks);
            var7.println("chatLinksPrompt:" + this.chatLinksPrompt);
            var7.println("chatOpacity:" + this.chatOpacity);
            var7.println("snooperEnabled:" + this.snooperEnabled);
            var7.println("fullscreen:" + this.fullScreen);
            var7.println("enableVsync:" + this.enableVsync);
            var7.println("useVbo:" + this.field_178881_t);
            var7.println("hideServerAddress:" + this.hideServerAddress);
            var7.println("advancedItemTooltips:" + this.advancedItemTooltips);
            var7.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
            var7.println("touchscreen:" + this.touchscreen);
            var7.println("overrideWidth:" + this.overrideWidth);
            var7.println("overrideHeight:" + this.overrideHeight);
            var7.println("heldItemTooltips:" + this.heldItemTooltips);
            var7.println("chatHeightFocused:" + this.chatHeightFocused);
            var7.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
            var7.println("chatScale:" + this.chatScale);
            var7.println("chatWidth:" + this.chatWidth);
            var7.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
            var7.println("mipmapLevels:" + this.mipmapLevels);
            var7.println("streamBytesPerPixel:" + this.streamBytesPerPixel);
            var7.println("streamMicVolume:" + this.streamMicVolume);
            var7.println("streamSystemVolume:" + this.streamGameVolume);
            var7.println("streamKbps:" + this.streamKbps);
            var7.println("streamFps:" + this.streamFps);
            var7.println("streamCompression:" + this.streamCompression);
            var7.println("streamSendMetadata:" + this.streamSendMetadata);
            var7.println("streamPreferredServer:" + this.streamPreferredServer);
            var7.println("streamChatEnabled:" + this.streamChatEnabled);
            var7.println("streamChatUserFilter:" + this.streamChatUserFilter);
            var7.println("streamMicToggleBehavior:" + this.streamMicToggleBehavior);
            var7.println("forceUnicodeFont:" + this.forceUnicodeFont);
            var7.println("allowBlockAlternatives:" + this.field_178880_u);
            var7.println("reducedDebugInfo:" + this.field_178879_v);
            for (final KeyBinding var11 : this.keyBindings) {
                var7.println("key_" + var11.getKeyDescription() + ":" + var11.getKeyCode());
            }
            for (final SoundCategory var13 : SoundCategory.values()) {
                var7.println("soundCategory_" + var13.getCategoryName() + ":" + this.getSoundLevel(var13));
            }
            for (final EnumPlayerModelParts var15 : EnumPlayerModelParts.values()) {
                var7.println("modelPart_" + var15.func_179329_c() + ":" + this.field_178882_aU.contains(var15));
            }
            var7.close();
        }
        catch (Exception var16) {
            GameSettings.logger.error("Failed to save options", (Throwable)var16);
        }
        this.saveOfOptions();
        this.sendSettingsToServer();
    }
    
    public float getSoundLevel(final SoundCategory p_151438_1_) {
        return this.mapSoundLevels.containsKey(p_151438_1_) ? this.mapSoundLevels.get(p_151438_1_) : 1.0f;
    }
    
    public void setSoundLevel(final SoundCategory p_151439_1_, final float p_151439_2_) {
        this.mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
        this.mapSoundLevels.put(p_151439_1_, p_151439_2_);
    }
    
    public void sendSettingsToServer() {
        if (this.mc.thePlayer != null) {
            int var1 = 0;
            for (final EnumPlayerModelParts var3 : this.field_178882_aU) {
                var1 |= var3.func_179327_a();
            }
            this.mc.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, var1));
        }
    }
    
    public Set func_178876_d() {
        return (Set)ImmutableSet.copyOf((Collection)this.field_178882_aU);
    }
    
    public void func_178878_a(final EnumPlayerModelParts p_178878_1_, final boolean p_178878_2_) {
        if (p_178878_2_) {
            this.field_178882_aU.add(p_178878_1_);
        }
        else {
            this.field_178882_aU.remove(p_178878_1_);
        }
        this.sendSettingsToServer();
    }
    
    public void func_178877_a(final EnumPlayerModelParts p_178877_1_) {
        if (!this.func_178876_d().contains(p_178877_1_)) {
            this.field_178882_aU.add(p_178877_1_);
        }
        else {
            this.field_178882_aU.remove(p_178877_1_);
        }
        this.sendSettingsToServer();
    }
    
    public boolean shouldRenderClouds() {
        return this.renderDistanceChunks >= 4 && this.clouds;
    }
    
    private void setOptionFloatValueOF(final Options option, final float val) {
        if (option == Options.CLOUD_HEIGHT) {
            this.ofCloudsHeight = val;
            this.mc.renderGlobal.resetClouds();
        }
        if (option == Options.AO_LEVEL) {
            this.ofAoLevel = val;
            this.mc.renderGlobal.loadRenderers();
        }
    }
    
    private void setOptionValueOF(final Options par1EnumOptions, final int par2) {
        if (par1EnumOptions == Options.FOG_FANCY) {
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
        if (par1EnumOptions == Options.FOG_START) {
            this.ofFogStart += 0.2f;
            if (this.ofFogStart > 0.81f) {
                this.ofFogStart = 0.2f;
            }
        }
        if (par1EnumOptions == Options.MIPMAP_TYPE) {
            ++this.ofMipmapType;
            if (this.ofMipmapType > 3) {
                this.ofMipmapType = 0;
            }
            TextureUtils.refreshBlockTextures();
        }
        if (par1EnumOptions == Options.LOAD_FAR) {
            this.ofLoadFar = false;
        }
        if (par1EnumOptions == Options.PRELOADED_CHUNKS) {
            this.ofPreloadedChunks = 0;
        }
        if (par1EnumOptions == Options.SMOOTH_WORLD) {
            this.ofSmoothWorld = !this.ofSmoothWorld;
            Config.updateThreadPriorities();
        }
        if (par1EnumOptions == Options.CLOUDS) {
            ++this.ofClouds;
            if (this.ofClouds > 3) {
                this.ofClouds = 0;
            }
            this.mc.renderGlobal.resetClouds();
        }
        if (par1EnumOptions == Options.TREES) {
            ++this.ofTrees;
            if (this.ofTrees > 2) {
                this.ofTrees = 0;
            }
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.DROPPED_ITEMS) {
            ++this.ofDroppedItems;
            if (this.ofDroppedItems > 2) {
                this.ofDroppedItems = 0;
            }
        }
        if (par1EnumOptions == Options.RAIN) {
            ++this.ofRain;
            if (this.ofRain > 3) {
                this.ofRain = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_WATER) {
            ++this.ofAnimatedWater;
            if (this.ofAnimatedWater > 2) {
                this.ofAnimatedWater = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_LAVA) {
            ++this.ofAnimatedLava;
            if (this.ofAnimatedLava > 2) {
                this.ofAnimatedLava = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_FIRE) {
            this.ofAnimatedFire = !this.ofAnimatedFire;
        }
        if (par1EnumOptions == Options.ANIMATED_PORTAL) {
            this.ofAnimatedPortal = !this.ofAnimatedPortal;
        }
        if (par1EnumOptions == Options.ANIMATED_REDSTONE) {
            this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
        }
        if (par1EnumOptions == Options.ANIMATED_EXPLOSION) {
            this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
        }
        if (par1EnumOptions == Options.ANIMATED_FLAME) {
            this.ofAnimatedFlame = !this.ofAnimatedFlame;
        }
        if (par1EnumOptions == Options.ANIMATED_SMOKE) {
            this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
        }
        if (par1EnumOptions == Options.VOID_PARTICLES) {
            this.ofVoidParticles = !this.ofVoidParticles;
        }
        if (par1EnumOptions == Options.WATER_PARTICLES) {
            this.ofWaterParticles = !this.ofWaterParticles;
        }
        if (par1EnumOptions == Options.PORTAL_PARTICLES) {
            this.ofPortalParticles = !this.ofPortalParticles;
        }
        if (par1EnumOptions == Options.POTION_PARTICLES) {
            this.ofPotionParticles = !this.ofPotionParticles;
        }
        if (par1EnumOptions == Options.DRIPPING_WATER_LAVA) {
            this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
        }
        if (par1EnumOptions == Options.ANIMATED_TERRAIN) {
            this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
        }
        if (par1EnumOptions == Options.ANIMATED_TEXTURES) {
            this.ofAnimatedTextures = !this.ofAnimatedTextures;
        }
        if (par1EnumOptions == Options.RAIN_SPLASH) {
            this.ofRainSplash = !this.ofRainSplash;
        }
        if (par1EnumOptions == Options.LAGOMETER) {
            this.ofLagometer = !this.ofLagometer;
        }
        if (par1EnumOptions == Options.AUTOSAVE_TICKS) {
            this.ofAutoSaveTicks *= 10;
            if (this.ofAutoSaveTicks > 40000) {
                this.ofAutoSaveTicks = 40;
            }
        }
        if (par1EnumOptions == Options.BETTER_GRASS) {
            ++this.ofBetterGrass;
            if (this.ofBetterGrass > 3) {
                this.ofBetterGrass = 1;
            }
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CONNECTED_TEXTURES) {
            ++this.ofConnectedTextures;
            if (this.ofConnectedTextures > 3) {
                this.ofConnectedTextures = 1;
            }
            if (this.ofConnectedTextures != 2) {
                this.mc.func_175603_A();
            }
        }
        if (par1EnumOptions == Options.WEATHER) {
            this.ofWeather = !this.ofWeather;
        }
        if (par1EnumOptions == Options.SKY) {
            this.ofSky = !this.ofSky;
        }
        if (par1EnumOptions == Options.STARS) {
            this.ofStars = !this.ofStars;
        }
        if (par1EnumOptions == Options.SUN_MOON) {
            this.ofSunMoon = !this.ofSunMoon;
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES) {
            ++this.ofChunkUpdates;
            if (this.ofChunkUpdates > 5) {
                this.ofChunkUpdates = 1;
            }
        }
        if (par1EnumOptions == Options.CHUNK_LOADING) {
            ++this.ofChunkLoading;
            if (this.ofChunkLoading > 2) {
                this.ofChunkLoading = 0;
            }
            this.updateChunkLoading();
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES_DYNAMIC) {
            this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
        }
        if (par1EnumOptions == Options.TIME) {
            ++this.ofTime;
            if (this.ofTime > 3) {
                this.ofTime = 0;
            }
        }
        if (par1EnumOptions == Options.CLEAR_WATER) {
            this.ofClearWater = !this.ofClearWater;
            this.updateWaterOpacity();
        }
        if (par1EnumOptions == Options.AA_LEVEL) {
            this.ofAaLevel = 0;
        }
        if (par1EnumOptions == Options.PROFILER) {
            this.ofProfiler = !this.ofProfiler;
        }
        if (par1EnumOptions == Options.BETTER_SNOW) {
            this.ofBetterSnow = !this.ofBetterSnow;
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.SWAMP_COLORS) {
            this.ofSwampColors = !this.ofSwampColors;
            CustomColorizer.updateUseDefaultColorMultiplier();
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.RANDOM_MOBS) {
            this.ofRandomMobs = !this.ofRandomMobs;
            RandomMobs.resetTextures();
        }
        if (par1EnumOptions == Options.SMOOTH_BIOMES) {
            this.ofSmoothBiomes = !this.ofSmoothBiomes;
            CustomColorizer.updateUseDefaultColorMultiplier();
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CUSTOM_FONTS) {
            this.ofCustomFonts = !this.ofCustomFonts;
            this.mc.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
            this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
        }
        if (par1EnumOptions == Options.CUSTOM_COLORS) {
            this.ofCustomColors = !this.ofCustomColors;
            CustomColorizer.update();
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CUSTOM_SKY) {
            this.ofCustomSky = !this.ofCustomSky;
            CustomSky.update();
        }
        if (par1EnumOptions == Options.SHOW_CAPES) {
            this.ofShowCapes = !this.ofShowCapes;
        }
        if (par1EnumOptions == Options.NATURAL_TEXTURES) {
            this.ofNaturalTextures = !this.ofNaturalTextures;
            NaturalTextures.update();
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.FAST_MATH) {
            this.ofFastMath = !this.ofFastMath;
            MathHelper.fastMath = this.ofFastMath;
        }
        if (par1EnumOptions == Options.FAST_RENDER) {
            this.ofFastRender = !this.ofFastRender;
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.TRANSLUCENT_BLOCKS) {
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
        if (par1EnumOptions == Options.LAZY_CHUNK_LOADING) {
            this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
            Config.updateAvailableProcessors();
            if (!Config.isSingleProcessor()) {
                this.ofLazyChunkLoading = false;
            }
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.FULLSCREEN_MODE) {
            final List modeList = Arrays.asList(Config.getFullscreenModes());
            if (this.ofFullscreenMode.equals("Default")) {
                this.ofFullscreenMode = modeList.get(0);
            }
            else {
                int index = modeList.indexOf(this.ofFullscreenMode);
                if (index < 0) {
                    this.ofFullscreenMode = "Default";
                }
                else if (++index >= modeList.size()) {
                    this.ofFullscreenMode = "Default";
                }
                else {
                    this.ofFullscreenMode = modeList.get(index);
                }
            }
        }
        if (par1EnumOptions == Options.HELD_ITEM_TOOLTIPS) {
            this.heldItemTooltips = !this.heldItemTooltips;
        }
    }
    
    private String getKeyBindingOF(final Options par1EnumOptions) {
        final String var2 = I18n.format(par1EnumOptions.getEnumString(), new Object[0]) + ": ";
        if (par1EnumOptions == Options.RENDER_DISTANCE) {
            final int var3 = (int)this.getOptionFloatValue(par1EnumOptions);
            String str = "Tiny";
            byte baseDist = 2;
            if (var3 >= 4) {
                str = "Short";
                baseDist = 4;
            }
            if (var3 >= 8) {
                str = "Normal";
                baseDist = 8;
            }
            if (var3 >= 16) {
                str = "Far";
                baseDist = 16;
            }
            if (var3 >= 32) {
                str = "Extreme";
                baseDist = 32;
            }
            final int diff = this.renderDistanceChunks - baseDist;
            String descr = str;
            if (diff > 0) {
                descr = str + "+";
            }
            return var2 + var3 + " " + descr + "";
        }
        if (par1EnumOptions == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    return var2 + "Fast";
                }
                case 2: {
                    return var2 + "Fancy";
                }
                case 3: {
                    return var2 + "OFF";
                }
                default: {
                    return var2 + "OFF";
                }
            }
        }
        else {
            if (par1EnumOptions == Options.FOG_START) {
                return var2 + this.ofFogStart;
            }
            if (par1EnumOptions == Options.MIPMAP_TYPE) {
                switch (this.ofMipmapType) {
                    case 0: {
                        return var2 + "Nearest";
                    }
                    case 1: {
                        return var2 + "Linear";
                    }
                    case 2: {
                        return var2 + "Bilinear";
                    }
                    case 3: {
                        return var2 + "Trilinear";
                    }
                    default: {
                        return var2 + "Nearest";
                    }
                }
            }
            else {
                if (par1EnumOptions == Options.LOAD_FAR) {
                    return this.ofLoadFar ? (var2 + "ON") : (var2 + "OFF");
                }
                if (par1EnumOptions == Options.PRELOADED_CHUNKS) {
                    return (this.ofPreloadedChunks == 0) ? (var2 + "OFF") : (var2 + this.ofPreloadedChunks);
                }
                if (par1EnumOptions == Options.SMOOTH_WORLD) {
                    return this.ofSmoothWorld ? (var2 + "ON") : (var2 + "OFF");
                }
                if (par1EnumOptions == Options.CLOUDS) {
                    switch (this.ofClouds) {
                        case 1: {
                            return var2 + "Fast";
                        }
                        case 2: {
                            return var2 + "Fancy";
                        }
                        case 3: {
                            return var2 + "OFF";
                        }
                        default: {
                            return var2 + "Default";
                        }
                    }
                }
                else if (par1EnumOptions == Options.TREES) {
                    switch (this.ofTrees) {
                        case 1: {
                            return var2 + "Fast";
                        }
                        case 2: {
                            return var2 + "Fancy";
                        }
                        default: {
                            return var2 + "Default";
                        }
                    }
                }
                else if (par1EnumOptions == Options.DROPPED_ITEMS) {
                    switch (this.ofDroppedItems) {
                        case 1: {
                            return var2 + "Fast";
                        }
                        case 2: {
                            return var2 + "Fancy";
                        }
                        default: {
                            return var2 + "Default";
                        }
                    }
                }
                else if (par1EnumOptions == Options.RAIN) {
                    switch (this.ofRain) {
                        case 1: {
                            return var2 + "Fast";
                        }
                        case 2: {
                            return var2 + "Fancy";
                        }
                        case 3: {
                            return var2 + "OFF";
                        }
                        default: {
                            return var2 + "Default";
                        }
                    }
                }
                else if (par1EnumOptions == Options.ANIMATED_WATER) {
                    switch (this.ofAnimatedWater) {
                        case 1: {
                            return var2 + "Dynamic";
                        }
                        case 2: {
                            return var2 + "OFF";
                        }
                        default: {
                            return var2 + "ON";
                        }
                    }
                }
                else if (par1EnumOptions == Options.ANIMATED_LAVA) {
                    switch (this.ofAnimatedLava) {
                        case 1: {
                            return var2 + "Dynamic";
                        }
                        case 2: {
                            return var2 + "OFF";
                        }
                        default: {
                            return var2 + "ON";
                        }
                    }
                }
                else {
                    if (par1EnumOptions == Options.ANIMATED_FIRE) {
                        return this.ofAnimatedFire ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.ANIMATED_PORTAL) {
                        return this.ofAnimatedPortal ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.ANIMATED_REDSTONE) {
                        return this.ofAnimatedRedstone ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.ANIMATED_EXPLOSION) {
                        return this.ofAnimatedExplosion ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.ANIMATED_FLAME) {
                        return this.ofAnimatedFlame ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.ANIMATED_SMOKE) {
                        return this.ofAnimatedSmoke ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.VOID_PARTICLES) {
                        return this.ofVoidParticles ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.WATER_PARTICLES) {
                        return this.ofWaterParticles ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.PORTAL_PARTICLES) {
                        return this.ofPortalParticles ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.POTION_PARTICLES) {
                        return this.ofPotionParticles ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.DRIPPING_WATER_LAVA) {
                        return this.ofDrippingWaterLava ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.ANIMATED_TERRAIN) {
                        return this.ofAnimatedTerrain ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.ANIMATED_TEXTURES) {
                        return this.ofAnimatedTextures ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.RAIN_SPLASH) {
                        return this.ofRainSplash ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.LAGOMETER) {
                        return this.ofLagometer ? (var2 + "ON") : (var2 + "OFF");
                    }
                    if (par1EnumOptions == Options.AUTOSAVE_TICKS) {
                        return (this.ofAutoSaveTicks <= 40) ? (var2 + "Default (2s)") : ((this.ofAutoSaveTicks <= 400) ? (var2 + "20s") : ((this.ofAutoSaveTicks <= 4000) ? (var2 + "3min") : (var2 + "30min")));
                    }
                    if (par1EnumOptions == Options.BETTER_GRASS) {
                        switch (this.ofBetterGrass) {
                            case 1: {
                                return var2 + "Fast";
                            }
                            case 2: {
                                return var2 + "Fancy";
                            }
                            default: {
                                return var2 + "OFF";
                            }
                        }
                    }
                    else if (par1EnumOptions == Options.CONNECTED_TEXTURES) {
                        switch (this.ofConnectedTextures) {
                            case 1: {
                                return var2 + "Fast";
                            }
                            case 2: {
                                return var2 + "Fancy";
                            }
                            default: {
                                return var2 + "OFF";
                            }
                        }
                    }
                    else {
                        if (par1EnumOptions == Options.WEATHER) {
                            return this.ofWeather ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.SKY) {
                            return this.ofSky ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.STARS) {
                            return this.ofStars ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.SUN_MOON) {
                            return this.ofSunMoon ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.CHUNK_UPDATES) {
                            return var2 + this.ofChunkUpdates;
                        }
                        if (par1EnumOptions == Options.CHUNK_LOADING) {
                            return (this.ofChunkLoading == 1) ? (var2 + "Smooth") : ((this.ofChunkLoading == 2) ? (var2 + "Multi-Core") : (var2 + "Default"));
                        }
                        if (par1EnumOptions == Options.CHUNK_UPDATES_DYNAMIC) {
                            return this.ofChunkUpdatesDynamic ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.TIME) {
                            return (this.ofTime == 1) ? (var2 + "Day Only") : ((this.ofTime == 3) ? (var2 + "Night Only") : (var2 + "Default"));
                        }
                        if (par1EnumOptions == Options.CLEAR_WATER) {
                            return this.ofClearWater ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.AA_LEVEL) {
                            return (this.ofAaLevel == 0) ? (var2 + "OFF") : (var2 + this.ofAaLevel);
                        }
                        if (par1EnumOptions == Options.PROFILER) {
                            return this.ofProfiler ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.BETTER_SNOW) {
                            return this.ofBetterSnow ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.SWAMP_COLORS) {
                            return this.ofSwampColors ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.RANDOM_MOBS) {
                            return this.ofRandomMobs ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.SMOOTH_BIOMES) {
                            return this.ofSmoothBiomes ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.CUSTOM_FONTS) {
                            return this.ofCustomFonts ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.CUSTOM_COLORS) {
                            return this.ofCustomColors ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.CUSTOM_SKY) {
                            return this.ofCustomSky ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.SHOW_CAPES) {
                            return this.ofShowCapes ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.NATURAL_TEXTURES) {
                            return this.ofNaturalTextures ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.FAST_MATH) {
                            return this.ofFastMath ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.FAST_RENDER) {
                            return this.ofFastRender ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.TRANSLUCENT_BLOCKS) {
                            return (this.ofTranslucentBlocks == 1) ? (var2 + "Fast") : ((this.ofTranslucentBlocks == 2) ? (var2 + "Fancy") : (var2 + "Default"));
                        }
                        if (par1EnumOptions == Options.LAZY_CHUNK_LOADING) {
                            return this.ofLazyChunkLoading ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.FULLSCREEN_MODE) {
                            return var2 + this.ofFullscreenMode;
                        }
                        if (par1EnumOptions == Options.HELD_ITEM_TOOLTIPS) {
                            return this.heldItemTooltips ? (var2 + "ON") : (var2 + "OFF");
                        }
                        if (par1EnumOptions == Options.FRAMERATE_LIMIT) {
                            final float var4 = this.getOptionFloatValue(par1EnumOptions);
                            return (var4 == 0.0f) ? (var2 + "VSync") : ((var4 == par1EnumOptions.valueMax) ? (var2 + I18n.format("options.framerateLimit.max", new Object[0])) : (var2 + (int)var4 + " fps"));
                        }
                        return null;
                    }
                }
            }
        }
    }
    
    public void loadOfOptions() {
        try {
            File exception = this.optionsFileOF;
            if (!exception.exists()) {
                exception = this.optionsFile;
            }
            if (!exception.exists()) {
                return;
            }
            final BufferedReader bufferedreader = new BufferedReader(new FileReader(exception));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                try {
                    final String[] exception2 = s.split(":");
                    if (exception2[0].equals("ofRenderDistanceChunks") && exception2.length >= 2) {
                        this.renderDistanceChunks = Integer.valueOf(exception2[1]);
                        this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 32);
                    }
                    if (exception2[0].equals("ofFogType") && exception2.length >= 2) {
                        this.ofFogType = Integer.valueOf(exception2[1]);
                        this.ofFogType = Config.limit(this.ofFogType, 1, 3);
                    }
                    if (exception2[0].equals("ofFogStart") && exception2.length >= 2) {
                        this.ofFogStart = Float.valueOf(exception2[1]);
                        if (this.ofFogStart < 0.2f) {
                            this.ofFogStart = 0.2f;
                        }
                        if (this.ofFogStart > 0.81f) {
                            this.ofFogStart = 0.8f;
                        }
                    }
                    if (exception2[0].equals("ofMipmapType") && exception2.length >= 2) {
                        this.ofMipmapType = Integer.valueOf(exception2[1]);
                        this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
                    }
                    if (exception2[0].equals("ofLoadFar") && exception2.length >= 2) {
                        this.ofLoadFar = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofPreloadedChunks") && exception2.length >= 2) {
                        this.ofPreloadedChunks = Integer.valueOf(exception2[1]);
                        if (this.ofPreloadedChunks < 0) {
                            this.ofPreloadedChunks = 0;
                        }
                        if (this.ofPreloadedChunks > 8) {
                            this.ofPreloadedChunks = 8;
                        }
                    }
                    if (exception2[0].equals("ofOcclusionFancy") && exception2.length >= 2) {
                        this.ofOcclusionFancy = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofSmoothWorld") && exception2.length >= 2) {
                        this.ofSmoothWorld = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAoLevel") && exception2.length >= 2) {
                        this.ofAoLevel = Float.valueOf(exception2[1]);
                        this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0f, 1.0f);
                    }
                    if (exception2[0].equals("ofClouds") && exception2.length >= 2) {
                        this.ofClouds = Integer.valueOf(exception2[1]);
                        this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                    }
                    if (exception2[0].equals("ofCloudsHeight") && exception2.length >= 2) {
                        this.ofCloudsHeight = Float.valueOf(exception2[1]);
                        this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0f, 1.0f);
                    }
                    if (exception2[0].equals("ofTrees") && exception2.length >= 2) {
                        this.ofTrees = Integer.valueOf(exception2[1]);
                        this.ofTrees = Config.limit(this.ofTrees, 0, 2);
                    }
                    if (exception2[0].equals("ofDroppedItems") && exception2.length >= 2) {
                        this.ofDroppedItems = Integer.valueOf(exception2[1]);
                        this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
                    }
                    if (exception2[0].equals("ofRain") && exception2.length >= 2) {
                        this.ofRain = Integer.valueOf(exception2[1]);
                        this.ofRain = Config.limit(this.ofRain, 0, 3);
                    }
                    if (exception2[0].equals("ofAnimatedWater") && exception2.length >= 2) {
                        this.ofAnimatedWater = Integer.valueOf(exception2[1]);
                        this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
                    }
                    if (exception2[0].equals("ofAnimatedLava") && exception2.length >= 2) {
                        this.ofAnimatedLava = Integer.valueOf(exception2[1]);
                        this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
                    }
                    if (exception2[0].equals("ofAnimatedFire") && exception2.length >= 2) {
                        this.ofAnimatedFire = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAnimatedPortal") && exception2.length >= 2) {
                        this.ofAnimatedPortal = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAnimatedRedstone") && exception2.length >= 2) {
                        this.ofAnimatedRedstone = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAnimatedExplosion") && exception2.length >= 2) {
                        this.ofAnimatedExplosion = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAnimatedFlame") && exception2.length >= 2) {
                        this.ofAnimatedFlame = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAnimatedSmoke") && exception2.length >= 2) {
                        this.ofAnimatedSmoke = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofVoidParticles") && exception2.length >= 2) {
                        this.ofVoidParticles = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofWaterParticles") && exception2.length >= 2) {
                        this.ofWaterParticles = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofPortalParticles") && exception2.length >= 2) {
                        this.ofPortalParticles = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofPotionParticles") && exception2.length >= 2) {
                        this.ofPotionParticles = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofDrippingWaterLava") && exception2.length >= 2) {
                        this.ofDrippingWaterLava = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAnimatedTerrain") && exception2.length >= 2) {
                        this.ofAnimatedTerrain = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAnimatedTextures") && exception2.length >= 2) {
                        this.ofAnimatedTextures = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofRainSplash") && exception2.length >= 2) {
                        this.ofRainSplash = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofLagometer") && exception2.length >= 2) {
                        this.ofLagometer = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofAutoSaveTicks") && exception2.length >= 2) {
                        this.ofAutoSaveTicks = Integer.valueOf(exception2[1]);
                        this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
                    }
                    if (exception2[0].equals("ofBetterGrass") && exception2.length >= 2) {
                        this.ofBetterGrass = Integer.valueOf(exception2[1]);
                        this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
                    }
                    if (exception2[0].equals("ofConnectedTextures") && exception2.length >= 2) {
                        this.ofConnectedTextures = Integer.valueOf(exception2[1]);
                        this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
                    }
                    if (exception2[0].equals("ofWeather") && exception2.length >= 2) {
                        this.ofWeather = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofSky") && exception2.length >= 2) {
                        this.ofSky = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofStars") && exception2.length >= 2) {
                        this.ofStars = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofSunMoon") && exception2.length >= 2) {
                        this.ofSunMoon = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofChunkUpdates") && exception2.length >= 2) {
                        this.ofChunkUpdates = Integer.valueOf(exception2[1]);
                        this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
                    }
                    if (exception2[0].equals("ofChunkLoading") && exception2.length >= 2) {
                        this.ofChunkLoading = Integer.valueOf(exception2[1]);
                        this.ofChunkLoading = Config.limit(this.ofChunkLoading, 0, 2);
                        this.updateChunkLoading();
                    }
                    if (exception2[0].equals("ofChunkUpdatesDynamic") && exception2.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofTime") && exception2.length >= 2) {
                        this.ofTime = Integer.valueOf(exception2[1]);
                        this.ofTime = Config.limit(this.ofTime, 0, 3);
                    }
                    if (exception2[0].equals("ofClearWater") && exception2.length >= 2) {
                        this.ofClearWater = Boolean.valueOf(exception2[1]);
                        this.updateWaterOpacity();
                    }
                    if (exception2[0].equals("ofAaLevel") && exception2.length >= 2) {
                        this.ofAaLevel = Integer.valueOf(exception2[1]);
                        this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
                    }
                    if (exception2[0].equals("ofProfiler") && exception2.length >= 2) {
                        this.ofProfiler = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofBetterSnow") && exception2.length >= 2) {
                        this.ofBetterSnow = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofSwampColors") && exception2.length >= 2) {
                        this.ofSwampColors = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofRandomMobs") && exception2.length >= 2) {
                        this.ofRandomMobs = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofSmoothBiomes") && exception2.length >= 2) {
                        this.ofSmoothBiomes = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofCustomFonts") && exception2.length >= 2) {
                        this.ofCustomFonts = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofCustomColors") && exception2.length >= 2) {
                        this.ofCustomColors = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofCustomSky") && exception2.length >= 2) {
                        this.ofCustomSky = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofShowCapes") && exception2.length >= 2) {
                        this.ofShowCapes = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofNaturalTextures") && exception2.length >= 2) {
                        this.ofNaturalTextures = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofLazyChunkLoading") && exception2.length >= 2) {
                        this.ofLazyChunkLoading = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofFullscreenMode") && exception2.length >= 2) {
                        this.ofFullscreenMode = exception2[1];
                    }
                    if (exception2[0].equals("ofFastMath") && exception2.length >= 2) {
                        this.ofFastMath = Boolean.valueOf(exception2[1]);
                        MathHelper.fastMath = this.ofFastMath;
                    }
                    if (exception2[0].equals("ofFastRender") && exception2.length >= 2) {
                        this.ofFastRender = Boolean.valueOf(exception2[1]);
                    }
                    if (exception2[0].equals("ofTranslucentBlocks") && exception2.length >= 2) {
                        this.ofTranslucentBlocks = Integer.valueOf(exception2[1]);
                        this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
                    }
                    if (!exception2[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) {
                        continue;
                    }
                    this.ofKeyBindZoom.setKeyCode(Integer.parseInt(exception2[1]));
                }
                catch (Exception var5) {
                    Config.dbg("Skipping bad option: " + s);
                    var5.printStackTrace();
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception var6) {
            Config.warn("Failed to load options");
            var6.printStackTrace();
        }
    }
    
    public void saveOfOptions() {
        try {
            final PrintWriter exception = new PrintWriter(new FileWriter(this.optionsFileOF));
            exception.println("ofRenderDistanceChunks:" + this.renderDistanceChunks);
            exception.println("ofFogType:" + this.ofFogType);
            exception.println("ofFogStart:" + this.ofFogStart);
            exception.println("ofMipmapType:" + this.ofMipmapType);
            exception.println("ofLoadFar:" + this.ofLoadFar);
            exception.println("ofPreloadedChunks:" + this.ofPreloadedChunks);
            exception.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
            exception.println("ofSmoothWorld:" + this.ofSmoothWorld);
            exception.println("ofAoLevel:" + this.ofAoLevel);
            exception.println("ofClouds:" + this.ofClouds);
            exception.println("ofCloudsHeight:" + this.ofCloudsHeight);
            exception.println("ofTrees:" + this.ofTrees);
            exception.println("ofDroppedItems:" + this.ofDroppedItems);
            exception.println("ofRain:" + this.ofRain);
            exception.println("ofAnimatedWater:" + this.ofAnimatedWater);
            exception.println("ofAnimatedLava:" + this.ofAnimatedLava);
            exception.println("ofAnimatedFire:" + this.ofAnimatedFire);
            exception.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
            exception.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
            exception.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
            exception.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
            exception.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
            exception.println("ofVoidParticles:" + this.ofVoidParticles);
            exception.println("ofWaterParticles:" + this.ofWaterParticles);
            exception.println("ofPortalParticles:" + this.ofPortalParticles);
            exception.println("ofPotionParticles:" + this.ofPotionParticles);
            exception.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
            exception.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
            exception.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
            exception.println("ofRainSplash:" + this.ofRainSplash);
            exception.println("ofLagometer:" + this.ofLagometer);
            exception.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
            exception.println("ofBetterGrass:" + this.ofBetterGrass);
            exception.println("ofConnectedTextures:" + this.ofConnectedTextures);
            exception.println("ofWeather:" + this.ofWeather);
            exception.println("ofSky:" + this.ofSky);
            exception.println("ofStars:" + this.ofStars);
            exception.println("ofSunMoon:" + this.ofSunMoon);
            exception.println("ofChunkUpdates:" + this.ofChunkUpdates);
            exception.println("ofChunkLoading:" + this.ofChunkLoading);
            exception.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
            exception.println("ofTime:" + this.ofTime);
            exception.println("ofClearWater:" + this.ofClearWater);
            exception.println("ofAaLevel:" + this.ofAaLevel);
            exception.println("ofProfiler:" + this.ofProfiler);
            exception.println("ofBetterSnow:" + this.ofBetterSnow);
            exception.println("ofSwampColors:" + this.ofSwampColors);
            exception.println("ofRandomMobs:" + this.ofRandomMobs);
            exception.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
            exception.println("ofCustomFonts:" + this.ofCustomFonts);
            exception.println("ofCustomColors:" + this.ofCustomColors);
            exception.println("ofCustomSky:" + this.ofCustomSky);
            exception.println("ofShowCapes:" + this.ofShowCapes);
            exception.println("ofNaturalTextures:" + this.ofNaturalTextures);
            exception.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
            exception.println("ofFullscreenMode:" + this.ofFullscreenMode);
            exception.println("ofFastMath:" + this.ofFastMath);
            exception.println("ofFastRender:" + this.ofFastRender);
            exception.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
            exception.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
            exception.close();
        }
        catch (Exception var2) {
            Config.warn("Failed to save options");
            var2.printStackTrace();
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
        this.clouds = true;
        this.fovSetting = 70.0f;
        this.gammaSetting = 0.0f;
        this.guiScale = 0;
        this.particleSetting = 0;
        this.heldItemTooltips = true;
        this.field_178881_t = false;
        this.field_178880_u = true;
        this.forceUnicodeFont = false;
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
        this.ofMipmapType = 0;
        this.ofLoadFar = false;
        this.ofPreloadedChunks = 0;
        this.ofOcclusionFancy = false;
        Config.updateAvailableProcessors();
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = Config.isSingleProcessor();
        this.ofFastMath = false;
        this.ofFastRender = true;
        this.ofTranslucentBlocks = 0;
        this.ofAoLevel = 1.0f;
        this.ofAaLevel = 0;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0f;
        this.ofTrees = 0;
        this.ofRain = 0;
        this.ofBetterGrass = 3;
        this.ofAutoSaveTicks = 4000;
        this.ofLagometer = false;
        this.ofProfiler = false;
        this.ofWeather = true;
        this.ofSky = true;
        this.ofStars = true;
        this.ofSunMoon = true;
        this.ofChunkUpdates = 1;
        this.ofChunkLoading = 0;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = "Default";
        this.ofSwampColors = true;
        this.ofRandomMobs = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomSky = true;
        this.ofShowCapes = true;
        this.ofConnectedTextures = 2;
        this.ofNaturalTextures = false;
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
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        this.updateWaterOpacity();
        this.mc.refreshResources();
        this.saveOptions();
    }
    
    public void updateVSync() {
        Display.setVSyncEnabled(this.enableVsync);
    }
    
    private void updateWaterOpacity() {
        if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
            Config.waterOpacityChanged = true;
        }
        ClearWater.updateWaterOpacity(this, this.mc.theWorld);
    }
    
    public void updateChunkLoading() {
        if (this.mc.renderGlobal != null) {
            this.mc.renderGlobal.loadRenderers();
        }
    }
    
    public void setAllAnimations(final boolean flag) {
        final int animVal = flag ? 0 : 2;
        this.ofAnimatedWater = animVal;
        this.ofAnimatedLava = animVal;
        this.ofAnimatedFire = flag;
        this.ofAnimatedPortal = flag;
        this.ofAnimatedRedstone = flag;
        this.ofAnimatedExplosion = flag;
        this.ofAnimatedFlame = flag;
        this.ofAnimatedSmoke = flag;
        this.ofVoidParticles = flag;
        this.ofWaterParticles = flag;
        this.ofRainSplash = flag;
        this.ofPortalParticles = flag;
        this.ofPotionParticles = flag;
        this.particleSetting = (flag ? 0 : 2);
        this.ofDrippingWaterLava = flag;
        this.ofAnimatedTerrain = flag;
        this.ofAnimatedTextures = flag;
    }
    
    static {
        logger = LogManager.getLogger();
        gson = new Gson();
        typeListString = new ParameterizedType() {
            private static final String __OBFID = "CL_00000651";
            
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
    }
    
    public enum Options
    {
        INVERT_MOUSE("INVERT_MOUSE", 0, "INVERT_MOUSE", 0, "options.invertMouse", false, true), 
        SENSITIVITY("SENSITIVITY", 1, "SENSITIVITY", 1, "options.sensitivity", true, false), 
        FOV("FOV", 2, "FOV", 2, "options.fov", true, false, 30.0f, 110.0f, 1.0f), 
        GAMMA("GAMMA", 3, "GAMMA", 3, "options.gamma", true, false), 
        SATURATION("SATURATION", 4, "SATURATION", 4, "options.saturation", true, false), 
        RENDER_DISTANCE("RENDER_DISTANCE", 5, "RENDER_DISTANCE", 5, "options.renderDistance", true, false, 2.0f, 16.0f, 1.0f), 
        VIEW_BOBBING("VIEW_BOBBING", 6, "VIEW_BOBBING", 6, "options.viewBobbing", false, true), 
        ANAGLYPH("ANAGLYPH", 7, "ANAGLYPH", 7, "options.anaglyph", false, true), 
        FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "FRAMERATE_LIMIT", 8, "options.framerateLimit", true, false, 0.0f, 260.0f, 5.0f), 
        FBO_ENABLE("FBO_ENABLE", 9, "FBO_ENABLE", 9, "options.fboEnable", false, true), 
        RENDER_CLOUDS("RENDER_CLOUDS", 10, "RENDER_CLOUDS", 10, "options.renderClouds", false, true), 
        GRAPHICS("GRAPHICS", 11, "GRAPHICS", 11, "options.graphics", false, false), 
        AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 12, "AMBIENT_OCCLUSION", 12, "options.ao", false, false), 
        GUI_SCALE("GUI_SCALE", 13, "GUI_SCALE", 13, "options.guiScale", false, false), 
        PARTICLES("PARTICLES", 14, "PARTICLES", 14, "options.particles", false, false), 
        CHAT_VISIBILITY("CHAT_VISIBILITY", 15, "CHAT_VISIBILITY", 15, "options.chat.visibility", false, false), 
        CHAT_COLOR("CHAT_COLOR", 16, "CHAT_COLOR", 16, "options.chat.color", false, true), 
        CHAT_LINKS("CHAT_LINKS", 17, "CHAT_LINKS", 17, "options.chat.links", false, true), 
        CHAT_OPACITY("CHAT_OPACITY", 18, "CHAT_OPACITY", 18, "options.chat.opacity", true, false), 
        CHAT_LINKS_PROMPT("CHAT_LINKS_PROMPT", 19, "CHAT_LINKS_PROMPT", 19, "options.chat.links.prompt", false, true), 
        SNOOPER_ENABLED("SNOOPER_ENABLED", 20, "SNOOPER_ENABLED", 20, "options.snooper", false, true), 
        USE_FULLSCREEN("USE_FULLSCREEN", 21, "USE_FULLSCREEN", 21, "options.fullscreen", false, true), 
        ENABLE_VSYNC("ENABLE_VSYNC", 22, "ENABLE_VSYNC", 22, "options.vsync", false, true), 
        USE_VBO("USE_VBO", 23, "USE_VBO", 23, "options.vbo", false, true), 
        TOUCHSCREEN("TOUCHSCREEN", 24, "TOUCHSCREEN", 24, "options.touchscreen", false, true), 
        CHAT_SCALE("CHAT_SCALE", 25, "CHAT_SCALE", 25, "options.chat.scale", true, false), 
        CHAT_WIDTH("CHAT_WIDTH", 26, "CHAT_WIDTH", 26, "options.chat.width", true, false), 
        CHAT_HEIGHT_FOCUSED("CHAT_HEIGHT_FOCUSED", 27, "CHAT_HEIGHT_FOCUSED", 27, "options.chat.height.focused", true, false), 
        CHAT_HEIGHT_UNFOCUSED("CHAT_HEIGHT_UNFOCUSED", 28, "CHAT_HEIGHT_UNFOCUSED", 28, "options.chat.height.unfocused", true, false), 
        MIPMAP_LEVELS("MIPMAP_LEVELS", 29, "MIPMAP_LEVELS", 29, "options.mipmapLevels", true, false, 0.0f, 4.0f, 1.0f), 
        FORCE_UNICODE_FONT("FORCE_UNICODE_FONT", 30, "FORCE_UNICODE_FONT", 30, "options.forceUnicodeFont", false, true), 
        STREAM_BYTES_PER_PIXEL("STREAM_BYTES_PER_PIXEL", 31, "STREAM_BYTES_PER_PIXEL", 31, "options.stream.bytesPerPixel", true, false), 
        STREAM_VOLUME_MIC("STREAM_VOLUME_MIC", 32, "STREAM_VOLUME_MIC", 32, "options.stream.micVolumne", true, false), 
        STREAM_VOLUME_SYSTEM("STREAM_VOLUME_SYSTEM", 33, "STREAM_VOLUME_SYSTEM", 33, "options.stream.systemVolume", true, false), 
        STREAM_KBPS("STREAM_KBPS", 34, "STREAM_KBPS", 34, "options.stream.kbps", true, false), 
        STREAM_FPS("STREAM_FPS", 35, "STREAM_FPS", 35, "options.stream.fps", true, false), 
        STREAM_COMPRESSION("STREAM_COMPRESSION", 36, "STREAM_COMPRESSION", 36, "options.stream.compression", false, false), 
        STREAM_SEND_METADATA("STREAM_SEND_METADATA", 37, "STREAM_SEND_METADATA", 37, "options.stream.sendMetadata", false, true), 
        STREAM_CHAT_ENABLED("STREAM_CHAT_ENABLED", 38, "STREAM_CHAT_ENABLED", 38, "options.stream.chat.enabled", false, false), 
        STREAM_CHAT_USER_FILTER("STREAM_CHAT_USER_FILTER", 39, "STREAM_CHAT_USER_FILTER", 39, "options.stream.chat.userFilter", false, false), 
        STREAM_MIC_TOGGLE_BEHAVIOR("STREAM_MIC_TOGGLE_BEHAVIOR", 40, "STREAM_MIC_TOGGLE_BEHAVIOR", 40, "options.stream.micToggleBehavior", false, false), 
        BLOCK_ALTERNATIVES("BLOCK_ALTERNATIVES", 41, "BLOCK_ALTERNATIVES", 41, "options.blockAlternatives", false, true), 
        REDUCED_DEBUG_INFO("REDUCED_DEBUG_INFO", 42, "REDUCED_DEBUG_INFO", 42, "options.reducedDebugInfo", false, true), 
        FOG_FANCY("FOG_FANCY", 43, "FOG", 999, "Fog", false, false), 
        FOG_START("FOG_START", 44, "", 999, "Fog Start", false, false), 
        MIPMAP_TYPE("MIPMAP_TYPE", 45, "", 999, "Mipmap Type", false, false), 
        LOAD_FAR("LOAD_FAR", 46, "", 999, "Load Far", false, false), 
        PRELOADED_CHUNKS("PRELOADED_CHUNKS", 47, "", 999, "Preloaded Chunks", false, false), 
        CLOUDS("CLOUDS", 48, "", 999, "Clouds", false, false), 
        CLOUD_HEIGHT("CLOUD_HEIGHT", 49, "", 999, "Cloud Height", true, false), 
        TREES("TREES", 50, "", 999, "Trees", false, false), 
        RAIN("RAIN", 51, "", 999, "Rain & Snow", false, false), 
        ANIMATED_WATER("ANIMATED_WATER", 52, "", 999, "Water Animated", false, false), 
        ANIMATED_LAVA("ANIMATED_LAVA", 53, "", 999, "Lava Animated", false, false), 
        ANIMATED_FIRE("ANIMATED_FIRE", 54, "", 999, "Fire Animated", false, false), 
        ANIMATED_PORTAL("ANIMATED_PORTAL", 55, "", 999, "Portal Animated", false, false), 
        AO_LEVEL("AO_LEVEL", 56, "", 999, "Smooth Lighting Level", true, false), 
        LAGOMETER("LAGOMETER", 57, "", 999, "Lagometer", false, false), 
        AUTOSAVE_TICKS("AUTOSAVE_TICKS", 58, "", 999, "Autosave", false, false), 
        BETTER_GRASS("BETTER_GRASS", 59, "", 999, "Better Grass", false, false), 
        ANIMATED_REDSTONE("ANIMATED_REDSTONE", 60, "", 999, "Redstone Animated", false, false), 
        ANIMATED_EXPLOSION("ANIMATED_EXPLOSION", 61, "", 999, "Explosion Animated", false, false), 
        ANIMATED_FLAME("ANIMATED_FLAME", 62, "", 999, "Flame Animated", false, false), 
        ANIMATED_SMOKE("ANIMATED_SMOKE", 63, "", 999, "Smoke Animated", false, false), 
        WEATHER("WEATHER", 64, "", 999, "Weather", false, false), 
        SKY("SKY", 65, "", 999, "Sky", false, false), 
        STARS("STARS", 66, "", 999, "Stars", false, false), 
        SUN_MOON("SUN_MOON", 67, "", 999, "Sun & Moon", false, false), 
        CHUNK_UPDATES("CHUNK_UPDATES", 68, "", 999, "Chunk Updates", false, false), 
        CHUNK_UPDATES_DYNAMIC("CHUNK_UPDATES_DYNAMIC", 69, "", 999, "Dynamic Updates", false, false), 
        TIME("TIME", 70, "", 999, "Time", false, false), 
        CLEAR_WATER("CLEAR_WATER", 71, "", 999, "Clear Water", false, false), 
        SMOOTH_WORLD("SMOOTH_WORLD", 72, "", 999, "Smooth World", false, false), 
        VOID_PARTICLES("VOID_PARTICLES", 73, "", 999, "Void Particles", false, false), 
        WATER_PARTICLES("WATER_PARTICLES", 74, "", 999, "Water Particles", false, false), 
        RAIN_SPLASH("RAIN_SPLASH", 75, "", 999, "Rain Splash", false, false), 
        PORTAL_PARTICLES("PORTAL_PARTICLES", 76, "", 999, "Portal Particles", false, false), 
        POTION_PARTICLES("POTION_PARTICLES", 77, "", 999, "Potion Particles", false, false), 
        PROFILER("PROFILER", 78, "", 999, "Debug Profiler", false, false), 
        DRIPPING_WATER_LAVA("DRIPPING_WATER_LAVA", 79, "", 999, "Dripping Water/Lava", false, false), 
        BETTER_SNOW("BETTER_SNOW", 80, "", 999, "Better Snow", false, false), 
        FULLSCREEN_MODE("FULLSCREEN_MODE", 81, "", 999, "Fullscreen Mode", false, false), 
        ANIMATED_TERRAIN("ANIMATED_TERRAIN", 82, "", 999, "Terrain Animated", false, false), 
        SWAMP_COLORS("SWAMP_COLORS", 83, "", 999, "Swamp Colors", false, false), 
        RANDOM_MOBS("RANDOM_MOBS", 84, "", 999, "Random Mobs", false, false), 
        SMOOTH_BIOMES("SMOOTH_BIOMES", 85, "", 999, "Smooth Biomes", false, false), 
        CUSTOM_FONTS("CUSTOM_FONTS", 86, "", 999, "Custom Fonts", false, false), 
        CUSTOM_COLORS("CUSTOM_COLORS", 87, "", 999, "Custom Colors", false, false), 
        SHOW_CAPES("SHOW_CAPES", 88, "", 999, "Show Capes", false, false), 
        CONNECTED_TEXTURES("CONNECTED_TEXTURES", 89, "", 999, "Connected Textures", false, false), 
        AA_LEVEL("AA_LEVEL", 90, "", 999, "Antialiasing", false, false), 
        AF_LEVEL("AF_LEVEL", 91, "", 999, "Anisotropic Filtering", false, false), 
        ANIMATED_TEXTURES("ANIMATED_TEXTURES", 92, "", 999, "Textures Animated", false, false), 
        NATURAL_TEXTURES("NATURAL_TEXTURES", 93, "", 999, "Natural Textures", false, false), 
        CHUNK_LOADING("CHUNK_LOADING", 94, "", 999, "Chunk Loading", false, false), 
        HELD_ITEM_TOOLTIPS("HELD_ITEM_TOOLTIPS", 95, "", 999, "Held Item Tooltips", false, false), 
        DROPPED_ITEMS("DROPPED_ITEMS", 96, "", 999, "Dropped Items", false, false), 
        LAZY_CHUNK_LOADING("LAZY_CHUNK_LOADING", 97, "", 999, "Lazy Chunk Loading", false, false), 
        CUSTOM_SKY("CUSTOM_SKY", 98, "", 999, "Custom Sky", false, false), 
        FAST_MATH("FAST_MATH", 99, "", 999, "Fast Math", false, false), 
        FAST_RENDER("FAST_RENDER", 100, "", 999, "Fast Render", false, false), 
        TRANSLUCENT_BLOCKS("TRANSLUCENT_BLOCKS", 101, "", 999, "Translucent Blocks", false, false);
        
        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private float valueMin;
        private float valueMax;
        private static final Options[] $VALUES;
        private static final String __OBFID = "CL_00000653";
        private static final Options[] $VALUES$;
        
        public static Options getEnumOptions(final int p_74379_0_) {
            for (final Options var4 : values()) {
                if (var4.returnEnumOrdinal() == p_74379_0_) {
                    return var4;
                }
            }
            return null;
        }
        
        private Options(final String p_i46381_1_, final int p_i46381_2_, final String p_i1015_1_, final int p_i1015_2_, final String p_i1015_3_, final boolean p_i1015_4_, final boolean p_i1015_5_) {
            this(p_i46381_1_, p_i46381_2_, p_i1015_1_, p_i1015_2_, p_i1015_3_, p_i1015_4_, p_i1015_5_, 0.0f, 1.0f, 0.0f);
        }
        
        private Options(final String p_i46382_1_, final int p_i46382_2_, final String p_i45004_1_, final int p_i45004_2_, final String p_i45004_3_, final boolean p_i45004_4_, final boolean p_i45004_5_, final float p_i45004_6_, final float p_i45004_7_, final float p_i45004_8_) {
            this.enumString = p_i45004_3_;
            this.enumFloat = p_i45004_4_;
            this.enumBoolean = p_i45004_5_;
            this.valueMin = p_i45004_6_;
            this.valueMax = p_i45004_7_;
            this.valueStep = p_i45004_8_;
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
        
        public void setValueMax(final float p_148263_1_) {
            this.valueMax = p_148263_1_;
        }
        
        public float normalizeValue(final float p_148266_1_) {
            return MathHelper.clamp_float((this.snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0f, 1.0f);
        }
        
        public float denormalizeValue(final float p_148262_1_) {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0f, 1.0f));
        }
        
        public float snapToStepClamp(float p_148268_1_) {
            p_148268_1_ = this.snapToStep(p_148268_1_);
            return MathHelper.clamp_float(p_148268_1_, this.valueMin, this.valueMax);
        }
        
        protected float snapToStep(float p_148264_1_) {
            if (this.valueStep > 0.0f) {
                p_148264_1_ = this.valueStep * Math.round(p_148264_1_ / this.valueStep);
            }
            return p_148264_1_;
        }
        
        static {
            $VALUES = new Options[] { Options.INVERT_MOUSE, Options.SENSITIVITY, Options.FOV, Options.GAMMA, Options.SATURATION, Options.RENDER_DISTANCE, Options.VIEW_BOBBING, Options.ANAGLYPH, Options.FRAMERATE_LIMIT, Options.FBO_ENABLE, Options.RENDER_CLOUDS, Options.GRAPHICS, Options.AMBIENT_OCCLUSION, Options.GUI_SCALE, Options.PARTICLES, Options.CHAT_VISIBILITY, Options.CHAT_COLOR, Options.CHAT_LINKS, Options.CHAT_OPACITY, Options.CHAT_LINKS_PROMPT, Options.SNOOPER_ENABLED, Options.USE_FULLSCREEN, Options.ENABLE_VSYNC, Options.USE_VBO, Options.TOUCHSCREEN, Options.CHAT_SCALE, Options.CHAT_WIDTH, Options.CHAT_HEIGHT_FOCUSED, Options.CHAT_HEIGHT_UNFOCUSED, Options.MIPMAP_LEVELS, Options.FORCE_UNICODE_FONT, Options.STREAM_BYTES_PER_PIXEL, Options.STREAM_VOLUME_MIC, Options.STREAM_VOLUME_SYSTEM, Options.STREAM_KBPS, Options.STREAM_FPS, Options.STREAM_COMPRESSION, Options.STREAM_SEND_METADATA, Options.STREAM_CHAT_ENABLED, Options.STREAM_CHAT_USER_FILTER, Options.STREAM_MIC_TOGGLE_BEHAVIOR, Options.BLOCK_ALTERNATIVES, Options.REDUCED_DEBUG_INFO };
            $VALUES$ = new Options[] { Options.INVERT_MOUSE, Options.SENSITIVITY, Options.FOV, Options.GAMMA, Options.SATURATION, Options.RENDER_DISTANCE, Options.VIEW_BOBBING, Options.ANAGLYPH, Options.FRAMERATE_LIMIT, Options.FBO_ENABLE, Options.RENDER_CLOUDS, Options.GRAPHICS, Options.AMBIENT_OCCLUSION, Options.GUI_SCALE, Options.PARTICLES, Options.CHAT_VISIBILITY, Options.CHAT_COLOR, Options.CHAT_LINKS, Options.CHAT_OPACITY, Options.CHAT_LINKS_PROMPT, Options.SNOOPER_ENABLED, Options.USE_FULLSCREEN, Options.ENABLE_VSYNC, Options.USE_VBO, Options.TOUCHSCREEN, Options.CHAT_SCALE, Options.CHAT_WIDTH, Options.CHAT_HEIGHT_FOCUSED, Options.CHAT_HEIGHT_UNFOCUSED, Options.MIPMAP_LEVELS, Options.FORCE_UNICODE_FONT, Options.STREAM_BYTES_PER_PIXEL, Options.STREAM_VOLUME_MIC, Options.STREAM_VOLUME_SYSTEM, Options.STREAM_KBPS, Options.STREAM_FPS, Options.STREAM_COMPRESSION, Options.STREAM_SEND_METADATA, Options.STREAM_CHAT_ENABLED, Options.STREAM_CHAT_USER_FILTER, Options.STREAM_MIC_TOGGLE_BEHAVIOR, Options.BLOCK_ALTERNATIVES, Options.REDUCED_DEBUG_INFO, Options.FOG_FANCY, Options.FOG_START, Options.MIPMAP_TYPE, Options.LOAD_FAR, Options.PRELOADED_CHUNKS, Options.CLOUDS, Options.CLOUD_HEIGHT, Options.TREES, Options.RAIN, Options.ANIMATED_WATER, Options.ANIMATED_LAVA, Options.ANIMATED_FIRE, Options.ANIMATED_PORTAL, Options.AO_LEVEL, Options.LAGOMETER, Options.AUTOSAVE_TICKS, Options.BETTER_GRASS, Options.ANIMATED_REDSTONE, Options.ANIMATED_EXPLOSION, Options.ANIMATED_FLAME, Options.ANIMATED_SMOKE, Options.WEATHER, Options.SKY, Options.STARS, Options.SUN_MOON, Options.CHUNK_UPDATES, Options.CHUNK_UPDATES_DYNAMIC, Options.TIME, Options.CLEAR_WATER, Options.SMOOTH_WORLD, Options.VOID_PARTICLES, Options.WATER_PARTICLES, Options.RAIN_SPLASH, Options.PORTAL_PARTICLES, Options.POTION_PARTICLES, Options.PROFILER, Options.DRIPPING_WATER_LAVA, Options.BETTER_SNOW, Options.FULLSCREEN_MODE, Options.ANIMATED_TERRAIN, Options.SWAMP_COLORS, Options.RANDOM_MOBS, Options.SMOOTH_BIOMES, Options.CUSTOM_FONTS, Options.CUSTOM_COLORS, Options.SHOW_CAPES, Options.CONNECTED_TEXTURES, Options.AA_LEVEL, Options.AF_LEVEL, Options.ANIMATED_TEXTURES, Options.NATURAL_TEXTURES, Options.CHUNK_LOADING, Options.HELD_ITEM_TOOLTIPS, Options.DROPPED_ITEMS, Options.LAZY_CHUNK_LOADING, Options.CUSTOM_SKY, Options.FAST_MATH, Options.FAST_RENDER, Options.TRANSLUCENT_BLOCKS };
        }
    }
    
    static final class SwitchOptions
    {
        static final int[] optionIds;
        private static final String __OBFID = "CL_00000652";
        
        static {
            optionIds = new int[Options.values().length];
            try {
                SwitchOptions.optionIds[Options.INVERT_MOUSE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchOptions.optionIds[Options.VIEW_BOBBING.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchOptions.optionIds[Options.ANAGLYPH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchOptions.optionIds[Options.FBO_ENABLE.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchOptions.optionIds[Options.RENDER_CLOUDS.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchOptions.optionIds[Options.CHAT_COLOR.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchOptions.optionIds[Options.CHAT_LINKS.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchOptions.optionIds[Options.CHAT_LINKS_PROMPT.ordinal()] = 8;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                SwitchOptions.optionIds[Options.SNOOPER_ENABLED.ordinal()] = 9;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                SwitchOptions.optionIds[Options.USE_FULLSCREEN.ordinal()] = 10;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
            try {
                SwitchOptions.optionIds[Options.ENABLE_VSYNC.ordinal()] = 11;
            }
            catch (NoSuchFieldError noSuchFieldError11) {}
            try {
                SwitchOptions.optionIds[Options.USE_VBO.ordinal()] = 12;
            }
            catch (NoSuchFieldError noSuchFieldError12) {}
            try {
                SwitchOptions.optionIds[Options.TOUCHSCREEN.ordinal()] = 13;
            }
            catch (NoSuchFieldError noSuchFieldError13) {}
            try {
                SwitchOptions.optionIds[Options.STREAM_SEND_METADATA.ordinal()] = 14;
            }
            catch (NoSuchFieldError noSuchFieldError14) {}
            try {
                SwitchOptions.optionIds[Options.FORCE_UNICODE_FONT.ordinal()] = 15;
            }
            catch (NoSuchFieldError noSuchFieldError15) {}
            try {
                SwitchOptions.optionIds[Options.BLOCK_ALTERNATIVES.ordinal()] = 16;
            }
            catch (NoSuchFieldError noSuchFieldError16) {}
            try {
                SwitchOptions.optionIds[Options.REDUCED_DEBUG_INFO.ordinal()] = 17;
            }
            catch (NoSuchFieldError noSuchFieldError17) {}
        }
    }
}
