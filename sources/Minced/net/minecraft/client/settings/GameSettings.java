// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.settings;

import java.lang.reflect.ParameterizedType;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.World;
import net.optifine.ClearWater;
import net.optifine.shaders.Shaders;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.optifine.CustomGuis;
import net.minecraft.client.renderer.OpenGlHelper;
import net.optifine.DynamicLights;
import net.optifine.NaturalTextures;
import net.optifine.CustomSky;
import net.optifine.RandomEntities;
import net.optifine.CustomColors;
import java.util.Arrays;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClientSettings;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.FixTypes;
import java.util.Iterator;
import net.minecraft.util.JsonUtils;
import net.minecraft.nbt.NBTTagCompound;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import net.minecraft.client.gui.GuiNewChat;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import net.minecraftforge.client.resource.IResourceType;
import net.optifine.reflect.Reflector;
import net.optifine.Lang;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import org.lwjgl.input.Mouse;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import net.optifine.util.KeyUtils;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.src.Config;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.world.EnumDifficulty;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.util.SoundCategory;
import java.util.Map;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.player.EnumPlayerModelParts;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import com.google.common.base.Splitter;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class GameSettings
{
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final Type TYPE_LIST_STRING;
    public static final Splitter COLON_SPLITTER;
    private static final String[] GUISCALES;
    private static final String[] PARTICLES;
    private static final String[] AMBIENT_OCCLUSIONS;
    private static final String[] CLOUDS_TYPES;
    private static final String[] ATTACK_INDICATORS;
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
    public boolean reducedDebugInfo;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus;
    private final Set<EnumPlayerModelParts> setModelParts;
    public boolean touchscreen;
    public EnumHandSide mainHand;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips;
    public float chatScale;
    public float chatWidth;
    public float chatHeightUnfocused;
    public float chatHeightFocused;
    public int mipmapLevels;
    private final Map<SoundCategory, Float> soundLevels;
    public boolean useNativeTransport;
    public boolean entityShadows;
    public int attackIndicator;
    public boolean enableWeakAttacks;
    public boolean showSubtitles;
    public boolean realmsNotifications;
    public boolean autoJump;
    public TutorialSteps tutorialStep;
    public KeyBinding keyBindForward;
    public KeyBinding keyBindLeft;
    public KeyBinding keyBindBack;
    public KeyBinding keyBindRight;
    public KeyBinding keyBindJump;
    public KeyBinding keyBindSneak;
    public KeyBinding keyBindSprint;
    public KeyBinding keyBindInventory;
    public KeyBinding keyBindSwapHands;
    public KeyBinding keyBindDrop;
    public KeyBinding keyBindUseItem;
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
    public KeyBinding keyBindAdvancements;
    public KeyBinding[] keyBindsHotbar;
    public KeyBinding keyBindSaveToolbar;
    public KeyBinding keyBindLoadToolbar;
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
    private boolean needsResourceRefresh;
    
    public GameSettings(final Minecraft mcIn, final File mcDataDir) {
        this.mouseSensitivity = 0.5f;
        this.renderDistanceChunks = -1;
        this.viewBobbing = true;
        this.fboEnable = true;
        this.limitFramerate = 120;
        this.clouds = 2;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.resourcePacks = (List<String>)Lists.newArrayList();
        this.incompatibleResourcePacks = (List<String>)Lists.newArrayList();
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.chatColours = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.chatOpacity = 1.0f;
        this.snooperEnabled = true;
        this.enableVsync = true;
        this.useVbo = true;
        this.pauseOnLostFocus = true;
        this.setModelParts = (Set<EnumPlayerModelParts>)Sets.newHashSet((Object[])EnumPlayerModelParts.values());
        this.mainHand = EnumHandSide.RIGHT;
        this.heldItemTooltips = true;
        this.chatScale = 1.0f;
        this.chatWidth = 1.0f;
        this.chatHeightUnfocused = 0.44366196f;
        this.chatHeightFocused = 1.0f;
        this.mipmapLevels = 4;
        this.soundLevels = (Map<SoundCategory, Float>)Maps.newEnumMap((Class)SoundCategory.class);
        this.useNativeTransport = true;
        this.entityShadows = true;
        this.attackIndicator = 1;
        this.realmsNotifications = true;
        this.autoJump = true;
        this.tutorialStep = TutorialSteps.MOVEMENT;
        this.keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
        this.keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
        this.keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
        this.keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
        this.keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
        this.keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
        this.keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
        this.keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
        this.keyBindSwapHands = new KeyBinding("key.swapHands", 33, "key.categories.inventory");
        this.keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.inventory");
        this.keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
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
        this.keyBindAdvancements = new KeyBinding("key.advancements", 38, "key.categories.misc");
        this.keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
        this.keyBindSaveToolbar = new KeyBinding("key.saveToolbarActivator", 46, "key.categories.creative");
        this.keyBindLoadToolbar = new KeyBinding("key.loadToolbarActivator", 45, "key.categories.creative");
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
        this.ofDynamicFov = false;
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
        this.needsResourceRefresh = false;
        this.setForgeKeybindProperties();
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindFullscreen, this.keyBindSpectatorOutlines, this.keyBindSwapHands, this.keyBindSaveToolbar, this.keyBindLoadToolbar, this.keyBindAdvancements }, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_us";
        this.mc = mcIn;
        this.optionsFile = new File(mcDataDir, "options.txt");
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
        this.optionsFileOF = new File(mcDataDir, "optionsof.txt");
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
        this.resourcePacks = (List<String>)Lists.newArrayList();
        this.incompatibleResourcePacks = (List<String>)Lists.newArrayList();
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.chatColours = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.chatOpacity = 1.0f;
        this.snooperEnabled = true;
        this.enableVsync = true;
        this.useVbo = true;
        this.pauseOnLostFocus = true;
        this.setModelParts = (Set<EnumPlayerModelParts>)Sets.newHashSet((Object[])EnumPlayerModelParts.values());
        this.mainHand = EnumHandSide.RIGHT;
        this.heldItemTooltips = true;
        this.chatScale = 1.0f;
        this.chatWidth = 1.0f;
        this.chatHeightUnfocused = 0.44366196f;
        this.chatHeightFocused = 1.0f;
        this.mipmapLevels = 4;
        this.soundLevels = (Map<SoundCategory, Float>)Maps.newEnumMap((Class)SoundCategory.class);
        this.useNativeTransport = true;
        this.entityShadows = true;
        this.attackIndicator = 1;
        this.realmsNotifications = true;
        this.autoJump = true;
        this.tutorialStep = TutorialSteps.MOVEMENT;
        this.keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
        this.keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
        this.keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
        this.keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
        this.keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
        this.keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
        this.keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
        this.keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
        this.keyBindSwapHands = new KeyBinding("key.swapHands", 33, "key.categories.inventory");
        this.keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.inventory");
        this.keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
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
        this.keyBindAdvancements = new KeyBinding("key.advancements", 38, "key.categories.misc");
        this.keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
        this.keyBindSaveToolbar = new KeyBinding("key.saveToolbarActivator", 46, "key.categories.creative");
        this.keyBindLoadToolbar = new KeyBinding("key.loadToolbarActivator", 45, "key.categories.creative");
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
        this.ofDynamicFov = false;
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
        this.needsResourceRefresh = false;
        this.setForgeKeybindProperties();
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindFullscreen, this.keyBindSpectatorOutlines, this.keyBindSwapHands, this.keyBindSaveToolbar, this.keyBindLoadToolbar, this.keyBindAdvancements }, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_us";
    }
    
    public static String getKeyDisplayString(final int key) {
        if (key >= 0) {
            return (key < 256) ? Keyboard.getKeyName(key) : String.format("%c", (char)(key - 256)).toUpperCase();
        }
        switch (key) {
            case -100: {
                return I18n.format("key.mouse.left", new Object[0]);
            }
            case -99: {
                return I18n.format("key.mouse.right", new Object[0]);
            }
            case -98: {
                return I18n.format("key.mouse.middle", new Object[0]);
            }
            default: {
                return I18n.format("key.mouseButton", key + 101);
            }
        }
    }
    
    public static boolean isKeyDown(final KeyBinding key) {
        final int i = key.getKeyCode();
        return i != 0 && i < 256 && ((i < 0) ? Mouse.isButtonDown(i + 100) : Keyboard.isKeyDown(i));
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
                this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
                this.mc.scheduleResourcesRefresh();
            }
        }
        if (settingsOption == Options.RENDER_DISTANCE) {
            this.renderDistanceChunks = (int)value;
            this.mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }
    
    public void setOptionValue(final Options settingsOption, final int value) {
        this.setOptionValueOF(settingsOption, value);
        if (settingsOption == Options.RENDER_DISTANCE) {
            this.setOptionFloatValue(settingsOption, MathHelper.clamp((float)(this.renderDistanceChunks + value), settingsOption.getValueMin(), settingsOption.getValueMax()));
        }
        if (settingsOption == Options.MAIN_HAND) {
            this.mainHand = this.mainHand.opposite();
        }
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
            this.mc.fontRenderer.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
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
            if (Reflector.FMLClientHandler_refreshResources.exists()) {
                final Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
                final IResourceType iresourcetype = (IResourceType)Reflector.VanillaResourceType_TEXTURES.getValue();
                Reflector.call(object, Reflector.FMLClientHandler_refreshResources, iresourcetype);
            }
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
        if (settingsOption == Options.REDUCED_DEBUG_INFO) {
            this.reducedDebugInfo = !this.reducedDebugInfo;
        }
        if (settingsOption == Options.ENTITY_SHADOWS) {
            this.entityShadows = !this.entityShadows;
        }
        if (settingsOption == Options.ATTACK_INDICATOR) {
            this.attackIndicator = (this.attackIndicator + value) % 3;
        }
        if (settingsOption == Options.SHOW_SUBTITLES) {
            this.showSubtitles = !this.showSubtitles;
        }
        if (settingsOption == Options.REALMS_NOTIFICATIONS) {
            this.realmsNotifications = !this.realmsNotifications;
        }
        if (settingsOption == Options.AUTO_JUMP) {
            this.autoJump = !this.autoJump;
        }
        this.saveOptions();
    }
    
    public float getOptionFloatValue(final Options settingOption) {
        final float f = this.getOptionFloatValueOF(settingOption);
        if (f != Float.MAX_VALUE) {
            return f;
        }
        if (settingOption == Options.FOV) {
            return this.fovSetting;
        }
        if (settingOption == Options.GAMMA) {
            return this.gammaSetting;
        }
        if (settingOption == Options.SATURATION) {
            return this.saturation;
        }
        if (settingOption == Options.SENSITIVITY) {
            return this.mouseSensitivity;
        }
        if (settingOption == Options.CHAT_OPACITY) {
            return this.chatOpacity;
        }
        if (settingOption == Options.CHAT_HEIGHT_FOCUSED) {
            return this.chatHeightFocused;
        }
        if (settingOption == Options.CHAT_HEIGHT_UNFOCUSED) {
            return this.chatHeightUnfocused;
        }
        if (settingOption == Options.CHAT_SCALE) {
            return this.chatScale;
        }
        if (settingOption == Options.CHAT_WIDTH) {
            return this.chatWidth;
        }
        if (settingOption == Options.FRAMERATE_LIMIT) {
            return (float)this.limitFramerate;
        }
        if (settingOption == Options.MIPMAP_LEVELS) {
            return (float)this.mipmapLevels;
        }
        return (settingOption == Options.RENDER_DISTANCE) ? ((float)this.renderDistanceChunks) : 0.0f;
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
            case FORCE_UNICODE_FONT: {
                return this.forceUnicodeFont;
            }
            case REDUCED_DEBUG_INFO: {
                return this.reducedDebugInfo;
            }
            case ENTITY_SHADOWS: {
                return this.entityShadows;
            }
            case SHOW_SUBTITLES: {
                return this.showSubtitles;
            }
            case REALMS_NOTIFICATIONS: {
                return this.realmsNotifications;
            }
            case ENABLE_WEAK_ATTACKS: {
                return this.enableWeakAttacks;
            }
            case AUTO_JUMP: {
                return this.autoJump;
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
        final String s2 = I18n.format(settingOption.getTranslation(), new Object[0]) + ": ";
        if (settingOption.isFloat()) {
            final float f1 = this.getOptionFloatValue(settingOption);
            final float f2 = settingOption.normalizeValue(f1);
            if (settingOption == Options.SENSITIVITY) {
                if (f2 == 0.0f) {
                    return s2 + I18n.format("options.sensitivity.min", new Object[0]);
                }
                return (f2 == 1.0f) ? (s2 + I18n.format("options.sensitivity.max", new Object[0])) : (s2 + (int)(f2 * 200.0f) + "%");
            }
            else if (settingOption == Options.FOV) {
                if (f1 == 70.0f) {
                    return s2 + I18n.format("options.fov.min", new Object[0]);
                }
                return (f1 == 110.0f) ? (s2 + I18n.format("options.fov.max", new Object[0])) : (s2 + (int)f1);
            }
            else {
                if (settingOption == Options.FRAMERATE_LIMIT) {
                    return (f1 == settingOption.valueMax) ? (s2 + I18n.format("options.framerateLimit.max", new Object[0])) : (s2 + I18n.format("options.framerate", (int)f1));
                }
                if (settingOption == Options.RENDER_CLOUDS) {
                    return (f1 == settingOption.valueMin) ? (s2 + I18n.format("options.cloudHeight.min", new Object[0])) : (s2 + ((int)f1 + 128));
                }
                if (settingOption == Options.GAMMA) {
                    if (f2 == 0.0f) {
                        return s2 + I18n.format("options.gamma.min", new Object[0]);
                    }
                    return (f2 == 1.0f) ? (s2 + I18n.format("options.gamma.max", new Object[0])) : (s2 + "+" + (int)(f2 * 100.0f) + "%");
                }
                else {
                    if (settingOption == Options.SATURATION) {
                        return s2 + (int)(f2 * 400.0f) + "%";
                    }
                    if (settingOption == Options.CHAT_OPACITY) {
                        return s2 + (int)(f2 * 90.0f + 10.0f) + "%";
                    }
                    if (settingOption == Options.CHAT_HEIGHT_UNFOCUSED) {
                        return s2 + GuiNewChat.calculateChatboxHeight(f2) + "px";
                    }
                    if (settingOption == Options.CHAT_HEIGHT_FOCUSED) {
                        return s2 + GuiNewChat.calculateChatboxHeight(f2) + "px";
                    }
                    if (settingOption == Options.CHAT_WIDTH) {
                        return s2 + GuiNewChat.calculateChatboxWidth(f2) + "px";
                    }
                    if (settingOption == Options.RENDER_DISTANCE) {
                        return s2 + I18n.format("options.chunks", (int)f1);
                    }
                    if (settingOption != Options.MIPMAP_LEVELS) {
                        return (f2 == 0.0f) ? (s2 + I18n.format("options.off", new Object[0])) : (s2 + (int)(f2 * 100.0f) + "%");
                    }
                    if (f1 >= 4.0) {
                        return s2 + Lang.get("of.general.max");
                    }
                    return (f1 == 0.0f) ? (s2 + I18n.format("options.off", new Object[0])) : (s2 + (int)f1);
                }
            }
        }
        else {
            if (settingOption.isBoolean()) {
                final boolean flag = this.getOptionOrdinalValue(settingOption);
                return flag ? (s2 + I18n.format("options.on", new Object[0])) : (s2 + I18n.format("options.off", new Object[0]));
            }
            if (settingOption == Options.MAIN_HAND) {
                return s2 + this.mainHand;
            }
            if (settingOption == Options.GUI_SCALE) {
                return (this.guiScale >= GameSettings.GUISCALES.length) ? (s2 + this.guiScale + "x") : (s2 + getTranslation(GameSettings.GUISCALES, this.guiScale));
            }
            if (settingOption == Options.CHAT_VISIBILITY) {
                return s2 + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
            }
            if (settingOption == Options.PARTICLES) {
                return s2 + getTranslation(GameSettings.PARTICLES, this.particleSetting);
            }
            if (settingOption == Options.AMBIENT_OCCLUSION) {
                return s2 + getTranslation(GameSettings.AMBIENT_OCCLUSIONS, this.ambientOcclusion);
            }
            if (settingOption == Options.RENDER_CLOUDS) {
                return s2 + getTranslation(GameSettings.CLOUDS_TYPES, this.clouds);
            }
            if (settingOption == Options.GRAPHICS) {
                if (this.fancyGraphics) {
                    return s2 + I18n.format("options.graphics.fancy", new Object[0]);
                }
                final String s3 = "options.graphics.fast";
                return s2 + I18n.format("options.graphics.fast", new Object[0]);
            }
            else {
                if (settingOption == Options.ATTACK_INDICATOR) {
                    return s2 + getTranslation(GameSettings.ATTACK_INDICATORS, this.attackIndicator);
                }
                return s2;
            }
        }
    }
    
    public void loadOptions() {
        FileInputStream fileinputstream = null;
        Label_2014: {
            try {
                if (this.optionsFile.exists()) {
                    this.soundLevels.clear();
                    final List<String> list = (List<String>)IOUtils.readLines((InputStream)(fileinputstream = new FileInputStream(this.optionsFile)), StandardCharsets.UTF_8);
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    for (final String s : list) {
                        try {
                            final Iterator<String> iterator = GameSettings.COLON_SPLITTER.omitEmptyStrings().limit(2).split((CharSequence)s).iterator();
                            nbttagcompound.setString(iterator.next(), iterator.next());
                        }
                        catch (Exception var18) {
                            GameSettings.LOGGER.warn("Skipping bad option: {}", (Object)s);
                        }
                    }
                    nbttagcompound = this.dataFix(nbttagcompound);
                    for (final String s2 : nbttagcompound.getKeySet()) {
                        final String s3 = nbttagcompound.getString(s2);
                        try {
                            if ("mouseSensitivity".equals(s2)) {
                                this.mouseSensitivity = this.parseFloat(s3);
                            }
                            if ("fov".equals(s2)) {
                                this.fovSetting = this.parseFloat(s3) * 40.0f + 70.0f;
                            }
                            if ("gamma".equals(s2)) {
                                this.gammaSetting = this.parseFloat(s3);
                            }
                            if ("saturation".equals(s2)) {
                                this.saturation = this.parseFloat(s3);
                            }
                            if ("invertYMouse".equals(s2)) {
                                this.invertMouse = "true".equals(s3);
                            }
                            if ("renderDistance".equals(s2)) {
                                this.renderDistanceChunks = Integer.parseInt(s3);
                            }
                            if ("guiScale".equals(s2)) {
                                this.guiScale = Integer.parseInt(s3);
                            }
                            if ("particles".equals(s2)) {
                                this.particleSetting = Integer.parseInt(s3);
                            }
                            if ("bobView".equals(s2)) {
                                this.viewBobbing = "true".equals(s3);
                            }
                            if ("anaglyph3d".equals(s2)) {
                                this.anaglyph = "true".equals(s3);
                            }
                            if ("maxFps".equals(s2)) {
                                this.limitFramerate = Integer.parseInt(s3);
                                if (this.enableVsync) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                                if (this.limitFramerate <= 0) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                            }
                            if ("fboEnable".equals(s2)) {
                                this.fboEnable = "true".equals(s3);
                            }
                            if ("difficulty".equals(s2)) {
                                this.difficulty = EnumDifficulty.byId(Integer.parseInt(s3));
                            }
                            if ("fancyGraphics".equals(s2)) {
                                this.fancyGraphics = "true".equals(s3);
                                this.updateRenderClouds();
                            }
                            if ("tutorialStep".equals(s2)) {
                                this.tutorialStep = TutorialSteps.getTutorial(s3);
                            }
                            if ("ao".equals(s2)) {
                                if ("true".equals(s3)) {
                                    this.ambientOcclusion = 2;
                                }
                                else if ("false".equals(s3)) {
                                    this.ambientOcclusion = 0;
                                }
                                else {
                                    this.ambientOcclusion = Integer.parseInt(s3);
                                }
                            }
                            if ("renderClouds".equals(s2)) {
                                if ("true".equals(s3)) {
                                    this.clouds = 2;
                                }
                                else if ("false".equals(s3)) {
                                    this.clouds = 0;
                                }
                                else if ("fast".equals(s3)) {
                                    this.clouds = 1;
                                }
                            }
                            if ("attackIndicator".equals(s2)) {
                                if ("0".equals(s3)) {
                                    this.attackIndicator = 0;
                                }
                                else if ("1".equals(s3)) {
                                    this.attackIndicator = 1;
                                }
                                else if ("2".equals(s3)) {
                                    this.attackIndicator = 2;
                                }
                            }
                            if ("resourcePacks".equals(s2)) {
                                this.resourcePacks = JsonUtils.gsonDeserialize(GameSettings.GSON, s3, GameSettings.TYPE_LIST_STRING);
                                if (this.resourcePacks == null) {
                                    this.resourcePacks = (List<String>)Lists.newArrayList();
                                }
                            }
                            if ("incompatibleResourcePacks".equals(s2)) {
                                this.incompatibleResourcePacks = JsonUtils.gsonDeserialize(GameSettings.GSON, s3, GameSettings.TYPE_LIST_STRING);
                                if (this.incompatibleResourcePacks == null) {
                                    this.incompatibleResourcePacks = (List<String>)Lists.newArrayList();
                                }
                            }
                            if ("lastServer".equals(s2)) {
                                this.lastServer = s3;
                            }
                            if ("lang".equals(s2)) {
                                this.language = s3;
                            }
                            if ("chatVisibility".equals(s2)) {
                                this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(s3));
                            }
                            if ("chatColors".equals(s2)) {
                                this.chatColours = "true".equals(s3);
                            }
                            if ("chatLinks".equals(s2)) {
                                this.chatLinks = "true".equals(s3);
                            }
                            if ("chatLinksPrompt".equals(s2)) {
                                this.chatLinksPrompt = "true".equals(s3);
                            }
                            if ("chatOpacity".equals(s2)) {
                                this.chatOpacity = this.parseFloat(s3);
                            }
                            if ("snooperEnabled".equals(s2)) {
                                this.snooperEnabled = "true".equals(s3);
                            }
                            if ("fullscreen".equals(s2)) {
                                this.fullScreen = "true".equals(s3);
                            }
                            if ("enableVsync".equals(s2)) {
                                this.enableVsync = "true".equals(s3);
                                if (this.enableVsync) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                                this.updateVSync();
                            }
                            if ("useVbo".equals(s2)) {
                                this.useVbo = "true".equals(s3);
                            }
                            if ("hideServerAddress".equals(s2)) {
                                this.hideServerAddress = "true".equals(s3);
                            }
                            if ("advancedItemTooltips".equals(s2)) {
                                this.advancedItemTooltips = "true".equals(s3);
                            }
                            if ("pauseOnLostFocus".equals(s2)) {
                                this.pauseOnLostFocus = "true".equals(s3);
                            }
                            if ("touchscreen".equals(s2)) {
                                this.touchscreen = "true".equals(s3);
                            }
                            if ("overrideHeight".equals(s2)) {
                                this.overrideHeight = Integer.parseInt(s3);
                            }
                            if ("overrideWidth".equals(s2)) {
                                this.overrideWidth = Integer.parseInt(s3);
                            }
                            if ("heldItemTooltips".equals(s2)) {
                                this.heldItemTooltips = "true".equals(s3);
                            }
                            if ("chatHeightFocused".equals(s2)) {
                                this.chatHeightFocused = this.parseFloat(s3);
                            }
                            if ("chatHeightUnfocused".equals(s2)) {
                                this.chatHeightUnfocused = this.parseFloat(s3);
                            }
                            if ("chatScale".equals(s2)) {
                                this.chatScale = this.parseFloat(s3);
                            }
                            if ("chatWidth".equals(s2)) {
                                this.chatWidth = this.parseFloat(s3);
                            }
                            if ("mipmapLevels".equals(s2)) {
                                this.mipmapLevels = Integer.parseInt(s3);
                            }
                            if ("forceUnicodeFont".equals(s2)) {
                                this.forceUnicodeFont = "true".equals(s3);
                            }
                            if ("reducedDebugInfo".equals(s2)) {
                                this.reducedDebugInfo = "true".equals(s3);
                            }
                            if ("useNativeTransport".equals(s2)) {
                                this.useNativeTransport = "true".equals(s3);
                            }
                            if ("entityShadows".equals(s2)) {
                                this.entityShadows = "true".equals(s3);
                            }
                            if ("mainHand".equals(s2)) {
                                this.mainHand = ("left".equals(s3) ? EnumHandSide.LEFT : EnumHandSide.RIGHT);
                            }
                            if ("showSubtitles".equals(s2)) {
                                this.showSubtitles = "true".equals(s3);
                            }
                            if ("realmsNotifications".equals(s2)) {
                                this.realmsNotifications = "true".equals(s3);
                            }
                            if ("enableWeakAttacks".equals(s2)) {
                                this.enableWeakAttacks = "true".equals(s3);
                            }
                            if ("autoJump".equals(s2)) {
                                this.autoJump = "true".equals(s3);
                            }
                            for (final KeyBinding keybinding : this.keyBindings) {
                                if (s2.equals("key_" + keybinding.getKeyDescription())) {
                                    if (Reflector.KeyModifier_valueFromString.exists()) {
                                        if (s3.indexOf(58) != -1) {
                                            final String[] astring = s3.split(":");
                                            final Object object = Reflector.call(Reflector.KeyModifier_valueFromString, astring[1]);
                                            Reflector.call(keybinding, Reflector.ForgeKeyBinding_setKeyModifierAndCode, object, Integer.parseInt(astring[0]));
                                        }
                                        else {
                                            final Object object2 = Reflector.getFieldValue(Reflector.KeyModifier_NONE);
                                            Reflector.call(keybinding, Reflector.ForgeKeyBinding_setKeyModifierAndCode, object2, Integer.parseInt(s3));
                                        }
                                    }
                                    else {
                                        keybinding.setKeyCode(Integer.parseInt(s3));
                                    }
                                }
                            }
                            for (final SoundCategory soundcategory : SoundCategory.values()) {
                                if (s2.equals("soundCategory_" + soundcategory.getName())) {
                                    this.soundLevels.put(soundcategory, this.parseFloat(s3));
                                }
                            }
                            for (final EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                                if (s2.equals("modelPart_" + enumplayermodelparts.getPartName())) {
                                    this.setModelPartEnabled(enumplayermodelparts, "true".equals(s3));
                                }
                            }
                        }
                        catch (Exception exception1) {
                            GameSettings.LOGGER.warn("Skipping bad option: {}:{}", (Object)s2, (Object)s3);
                            exception1.printStackTrace();
                        }
                    }
                    KeyBinding.resetKeyBindingArrayAndHash();
                    break Label_2014;
                }
            }
            catch (Exception exception2) {
                GameSettings.LOGGER.error("Failed to load options", (Throwable)exception2);
                break Label_2014;
            }
            finally {
                IOUtils.closeQuietly((InputStream)fileinputstream);
            }
            return;
        }
        this.loadOfOptions();
    }
    
    private NBTTagCompound dataFix(final NBTTagCompound p_189988_1_) {
        int i = 0;
        try {
            i = Integer.parseInt(p_189988_1_.getString("version"));
        }
        catch (RuntimeException ex) {}
        return this.mc.getDataFixer().process(FixTypes.OPTIONS, p_189988_1_, i);
    }
    
    private float parseFloat(final String str) {
        if ("true".equals(str)) {
            return 1.0f;
        }
        return "false".equals(str) ? 0.0f : Float.parseFloat(str);
    }
    
    public void saveOptions() {
        if (Reflector.FMLClientHandler.exists()) {
            final Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            if (object != null && Reflector.callBoolean(object, Reflector.FMLClientHandler_isLoading, new Object[0])) {
                return;
            }
        }
        PrintWriter printwriter = null;
        try {
            printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
            printwriter.println("version:1343");
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
            printwriter.println("difficulty:" + this.difficulty.getId());
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
            printwriter.println("resourcePacks:" + GameSettings.GSON.toJson((Object)this.resourcePacks));
            printwriter.println("incompatibleResourcePacks:" + GameSettings.GSON.toJson((Object)this.incompatibleResourcePacks));
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
            printwriter.println("mipmapLevels:" + this.mipmapLevels);
            printwriter.println("forceUnicodeFont:" + this.forceUnicodeFont);
            printwriter.println("reducedDebugInfo:" + this.reducedDebugInfo);
            printwriter.println("useNativeTransport:" + this.useNativeTransport);
            printwriter.println("entityShadows:" + this.entityShadows);
            printwriter.println("mainHand:" + ((this.mainHand == EnumHandSide.LEFT) ? "left" : "right"));
            printwriter.println("attackIndicator:" + this.attackIndicator);
            printwriter.println("showSubtitles:" + this.showSubtitles);
            printwriter.println("realmsNotifications:" + this.realmsNotifications);
            printwriter.println("enableWeakAttacks:" + this.enableWeakAttacks);
            printwriter.println("autoJump:" + this.autoJump);
            printwriter.println("tutorialStep:" + this.tutorialStep.getName());
            for (final KeyBinding keybinding : this.keyBindings) {
                if (Reflector.ForgeKeyBinding_getKeyModifier.exists()) {
                    final String s = "key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode();
                    final Object object2 = Reflector.call(keybinding, Reflector.ForgeKeyBinding_getKeyModifier, new Object[0]);
                    final Object object3 = Reflector.getFieldValue(Reflector.KeyModifier_NONE);
                    printwriter.println((object2 != object3) ? (s + ":" + object2) : s);
                }
                else {
                    printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
                }
            }
            for (final SoundCategory soundcategory : SoundCategory.values()) {
                printwriter.println("soundCategory_" + soundcategory.getName() + ":" + this.getSoundLevel(soundcategory));
            }
            for (final EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                printwriter.println("modelPart_" + enumplayermodelparts.getPartName() + ":" + this.setModelParts.contains(enumplayermodelparts));
            }
        }
        catch (Exception exception) {
            GameSettings.LOGGER.error("Failed to save options", (Throwable)exception);
        }
        finally {
            IOUtils.closeQuietly((Writer)printwriter);
        }
        this.saveOfOptions();
        this.sendSettingsToServer();
    }
    
    public float getSoundLevel(final SoundCategory category) {
        return this.soundLevels.containsKey(category) ? this.soundLevels.get(category) : 1.0f;
    }
    
    public void setSoundLevel(final SoundCategory category, final float volume) {
        this.mc.getSoundHandler().setSoundLevel(category, volume);
        this.soundLevels.put(category, volume);
    }
    
    public void sendSettingsToServer() {
        final Minecraft mc = this.mc;
        if (Minecraft.player != null) {
            int i = 0;
            for (final EnumPlayerModelParts enumplayermodelparts : this.setModelParts) {
                i |= enumplayermodelparts.getPartMask();
            }
            final Minecraft mc2 = this.mc;
            Minecraft.player.connection.sendPacket(new CPacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i, this.mainHand));
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
        if (this.getModelParts().contains(modelPart)) {
            this.setModelParts.remove(modelPart);
        }
        else {
            this.setModelParts.add(modelPart);
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
            this.mc.fontRenderer.onResourceManagerReload(Config.getResourceManager());
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
        String s = I18n.format(p_getKeyBindingOF_1_.getTranslation(), new Object[0]) + ": ";
        if (s == null) {
            s = p_getKeyBindingOF_1_.getTranslation();
        }
        if (p_getKeyBindingOF_1_ == Options.RENDER_DISTANCE) {
            final int i1 = (int)this.getOptionFloatValue(p_getKeyBindingOF_1_);
            String s2 = I18n.format("of.options.renderDistance.tiny", new Object[0]);
            int j = 2;
            if (i1 >= 4) {
                s2 = I18n.format("of.options.renderDistance.short", new Object[0]);
                j = 4;
            }
            if (i1 >= 8) {
                s2 = I18n.format("of.options.renderDistance.normal", new Object[0]);
                j = 8;
            }
            if (i1 >= 16) {
                s2 = I18n.format("of.options.renderDistance.far", new Object[0]);
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
                s3 = s2 + "+";
            }
            return s + i1 + " " + s3 + "";
        }
        if (p_getKeyBindingOF_1_ == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
                case 3: {
                    return s + Lang.getOff();
                }
                default: {
                    return s + Lang.getOff();
                }
            }
        }
        else {
            if (p_getKeyBindingOF_1_ == Options.FOG_START) {
                return s + this.ofFogStart;
            }
            if (p_getKeyBindingOF_1_ == Options.MIPMAP_TYPE) {
                switch (this.ofMipmapType) {
                    case 0: {
                        return s + Lang.get("of.options.mipmap.nearest");
                    }
                    case 1: {
                        return s + Lang.get("of.options.mipmap.linear");
                    }
                    case 2: {
                        return s + Lang.get("of.options.mipmap.bilinear");
                    }
                    case 3: {
                        return s + Lang.get("of.options.mipmap.trilinear");
                    }
                    default: {
                        return s + "of.options.mipmap.nearest";
                    }
                }
            }
            else {
                if (p_getKeyBindingOF_1_ == Options.SMOOTH_FPS) {
                    return this.ofSmoothFps ? (s + Lang.getOn()) : (s + Lang.getOff());
                }
                if (p_getKeyBindingOF_1_ == Options.SMOOTH_WORLD) {
                    return this.ofSmoothWorld ? (s + Lang.getOn()) : (s + Lang.getOff());
                }
                if (p_getKeyBindingOF_1_ == Options.CLOUDS) {
                    switch (this.ofClouds) {
                        case 1: {
                            return s + Lang.getFast();
                        }
                        case 2: {
                            return s + Lang.getFancy();
                        }
                        case 3: {
                            return s + Lang.getOff();
                        }
                        default: {
                            return s + Lang.getDefault();
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.TREES) {
                    switch (this.ofTrees) {
                        case 1: {
                            return s + Lang.getFast();
                        }
                        case 2: {
                            return s + Lang.getFancy();
                        }
                        default: {
                            return s + Lang.getDefault();
                        }
                        case 4: {
                            return s + Lang.get("of.general.smart");
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.DROPPED_ITEMS) {
                    switch (this.ofDroppedItems) {
                        case 1: {
                            return s + Lang.getFast();
                        }
                        case 2: {
                            return s + Lang.getFancy();
                        }
                        default: {
                            return s + Lang.getDefault();
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.RAIN) {
                    switch (this.ofRain) {
                        case 1: {
                            return s + Lang.getFast();
                        }
                        case 2: {
                            return s + Lang.getFancy();
                        }
                        case 3: {
                            return s + Lang.getOff();
                        }
                        default: {
                            return s + Lang.getDefault();
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.ANIMATED_WATER) {
                    switch (this.ofAnimatedWater) {
                        case 1: {
                            return s + Lang.get("of.options.animation.dynamic");
                        }
                        case 2: {
                            return s + Lang.getOff();
                        }
                        default: {
                            return s + Lang.getOn();
                        }
                    }
                }
                else if (p_getKeyBindingOF_1_ == Options.ANIMATED_LAVA) {
                    switch (this.ofAnimatedLava) {
                        case 1: {
                            return s + Lang.get("of.options.animation.dynamic");
                        }
                        case 2: {
                            return s + Lang.getOff();
                        }
                        default: {
                            return s + Lang.getOn();
                        }
                    }
                }
                else {
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_FIRE) {
                        return this.ofAnimatedFire ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_PORTAL) {
                        return this.ofAnimatedPortal ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_REDSTONE) {
                        return this.ofAnimatedRedstone ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_EXPLOSION) {
                        return this.ofAnimatedExplosion ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_FLAME) {
                        return this.ofAnimatedFlame ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_SMOKE) {
                        return this.ofAnimatedSmoke ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.VOID_PARTICLES) {
                        return this.ofVoidParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.WATER_PARTICLES) {
                        return this.ofWaterParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.PORTAL_PARTICLES) {
                        return this.ofPortalParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.POTION_PARTICLES) {
                        return this.ofPotionParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.FIREWORK_PARTICLES) {
                        return this.ofFireworkParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.DRIPPING_WATER_LAVA) {
                        return this.ofDrippingWaterLava ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_TERRAIN) {
                        return this.ofAnimatedTerrain ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.ANIMATED_TEXTURES) {
                        return this.ofAnimatedTextures ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.RAIN_SPLASH) {
                        return this.ofRainSplash ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.LAGOMETER) {
                        return this.ofLagometer ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.SHOW_FPS) {
                        return this.ofShowFps ? (s + Lang.getOn()) : (s + Lang.getOff());
                    }
                    if (p_getKeyBindingOF_1_ == Options.AUTOSAVE_TICKS) {
                        final int l = 900;
                        if (this.ofAutoSaveTicks <= l) {
                            return s + Lang.get("of.options.save.45s");
                        }
                        if (this.ofAutoSaveTicks <= 2 * l) {
                            return s + Lang.get("of.options.save.90s");
                        }
                        if (this.ofAutoSaveTicks <= 4 * l) {
                            return s + Lang.get("of.options.save.3min");
                        }
                        if (this.ofAutoSaveTicks <= 8 * l) {
                            return s + Lang.get("of.options.save.6min");
                        }
                        return (this.ofAutoSaveTicks <= 16 * l) ? (s + Lang.get("of.options.save.12min")) : (s + Lang.get("of.options.save.24min"));
                    }
                    else if (p_getKeyBindingOF_1_ == Options.BETTER_GRASS) {
                        switch (this.ofBetterGrass) {
                            case 1: {
                                return s + Lang.getFast();
                            }
                            case 2: {
                                return s + Lang.getFancy();
                            }
                            default: {
                                return s + Lang.getOff();
                            }
                        }
                    }
                    else if (p_getKeyBindingOF_1_ == Options.CONNECTED_TEXTURES) {
                        switch (this.ofConnectedTextures) {
                            case 1: {
                                return s + Lang.getFast();
                            }
                            case 2: {
                                return s + Lang.getFancy();
                            }
                            default: {
                                return s + Lang.getOff();
                            }
                        }
                    }
                    else {
                        if (p_getKeyBindingOF_1_ == Options.WEATHER) {
                            return this.ofWeather ? (s + Lang.getOn()) : (s + Lang.getOff());
                        }
                        if (p_getKeyBindingOF_1_ == Options.SKY) {
                            return this.ofSky ? (s + Lang.getOn()) : (s + Lang.getOff());
                        }
                        if (p_getKeyBindingOF_1_ == Options.STARS) {
                            return this.ofStars ? (s + Lang.getOn()) : (s + Lang.getOff());
                        }
                        if (p_getKeyBindingOF_1_ == Options.SUN_MOON) {
                            return this.ofSunMoon ? (s + Lang.getOn()) : (s + Lang.getOff());
                        }
                        if (p_getKeyBindingOF_1_ == Options.VIGNETTE) {
                            switch (this.ofVignette) {
                                case 1: {
                                    return s + Lang.getFast();
                                }
                                case 2: {
                                    return s + Lang.getFancy();
                                }
                                default: {
                                    return s + Lang.getDefault();
                                }
                            }
                        }
                        else {
                            if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES) {
                                return s + this.ofChunkUpdates;
                            }
                            if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES_DYNAMIC) {
                                return this.ofChunkUpdatesDynamic ? (s + Lang.getOn()) : (s + Lang.getOff());
                            }
                            if (p_getKeyBindingOF_1_ == Options.TIME) {
                                if (this.ofTime == 1) {
                                    return s + Lang.get("of.options.time.dayOnly");
                                }
                                return (this.ofTime == 2) ? (s + Lang.get("of.options.time.nightOnly")) : (s + Lang.getDefault());
                            }
                            else {
                                if (p_getKeyBindingOF_1_ == Options.CLEAR_WATER) {
                                    return this.ofClearWater ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.AA_LEVEL) {
                                    String s4 = "";
                                    if (this.ofAaLevel != Config.getAntialiasingLevel()) {
                                        s4 = " (" + Lang.get("of.general.restart") + ")";
                                    }
                                    return (this.ofAaLevel == 0) ? (s + Lang.getOff() + s4) : (s + this.ofAaLevel + s4);
                                }
                                if (p_getKeyBindingOF_1_ == Options.AF_LEVEL) {
                                    return (this.ofAfLevel == 1) ? (s + Lang.getOff()) : (s + this.ofAfLevel);
                                }
                                if (p_getKeyBindingOF_1_ == Options.PROFILER) {
                                    return this.ofProfiler ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.BETTER_SNOW) {
                                    return this.ofBetterSnow ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.SWAMP_COLORS) {
                                    return this.ofSwampColors ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.RANDOM_ENTITIES) {
                                    return this.ofRandomEntities ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.SMOOTH_BIOMES) {
                                    return this.ofSmoothBiomes ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.CUSTOM_FONTS) {
                                    return this.ofCustomFonts ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.CUSTOM_COLORS) {
                                    return this.ofCustomColors ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.CUSTOM_SKY) {
                                    return this.ofCustomSky ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.SHOW_CAPES) {
                                    return this.ofShowCapes ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.CUSTOM_ITEMS) {
                                    return this.ofCustomItems ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.NATURAL_TEXTURES) {
                                    return this.ofNaturalTextures ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.EMISSIVE_TEXTURES) {
                                    return this.ofEmissiveTextures ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.FAST_MATH) {
                                    return this.ofFastMath ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.FAST_RENDER) {
                                    return this.ofFastRender ? (s + Lang.getOn()) : (s + Lang.getOff());
                                }
                                if (p_getKeyBindingOF_1_ == Options.TRANSLUCENT_BLOCKS) {
                                    if (this.ofTranslucentBlocks == 1) {
                                        return s + Lang.getFast();
                                    }
                                    return (this.ofTranslucentBlocks == 2) ? (s + Lang.getFancy()) : (s + Lang.getDefault());
                                }
                                else {
                                    if (p_getKeyBindingOF_1_ == Options.LAZY_CHUNK_LOADING) {
                                        return this.ofLazyChunkLoading ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.RENDER_REGIONS) {
                                        return this.ofRenderRegions ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.SMART_ANIMATIONS) {
                                        return this.ofSmartAnimations ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.DYNAMIC_FOV) {
                                        return s + Lang.getOff();
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.ALTERNATE_BLOCKS) {
                                        return this.ofAlternateBlocks ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.DYNAMIC_LIGHTS) {
                                        final int m = indexOf(this.ofDynamicLights, GameSettings.OF_DYNAMIC_LIGHTS);
                                        return s + getTranslation(GameSettings.KEYS_DYNAMIC_LIGHTS, m);
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.SCREENSHOT_SIZE) {
                                        return (this.ofScreenshotSize <= 1) ? (s + Lang.getDefault()) : (s + this.ofScreenshotSize + "x");
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.CUSTOM_ENTITY_MODELS) {
                                        return this.ofCustomEntityModels ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.CUSTOM_GUIS) {
                                        return this.ofCustomGuis ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.SHOW_GL_ERRORS) {
                                        return this.ofShowGlErrors ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.FULLSCREEN_MODE) {
                                        return this.ofFullscreenMode.equals("Default") ? (s + Lang.getDefault()) : (s + this.ofFullscreenMode);
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.HELD_ITEM_TOOLTIPS) {
                                        return this.heldItemTooltips ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ == Options.ADVANCED_TOOLTIPS) {
                                        return this.advancedItemTooltips ? (s + Lang.getOn()) : (s + Lang.getOff());
                                    }
                                    if (p_getKeyBindingOF_1_ != Options.FRAMERATE_LIMIT) {
                                        return null;
                                    }
                                    final float f = this.getOptionFloatValue(p_getKeyBindingOF_1_);
                                    if (f == 0.0f) {
                                        return s + Lang.get("of.options.framerateLimit.vsync");
                                    }
                                    return (f == p_getKeyBindingOF_1_.valueMax) ? (s + I18n.format("options.framerateLimit.max", new Object[0])) : (s + (int)f + " fps");
                                }
                            }
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
            final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8));
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
                catch (Exception exception1) {
                    Config.dbg("Skipping bad option: " + s);
                    exception1.printStackTrace();
                }
            }
            KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[] { this.ofKeyBindZoom });
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception exception2) {
            Config.warn("Failed to load options");
            exception2.printStackTrace();
        }
    }
    
    public void saveOfOptions() {
        try {
            final PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFileOF), StandardCharsets.UTF_8));
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
        catch (Exception exception1) {
            Config.warn("Failed to save options");
            exception1.printStackTrace();
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
        ClearWater.updateWaterOpacity(this, this.mc.world);
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
    
    private void setForgeKeybindProperties() {
        if (Reflector.KeyConflictContext_IN_GAME.exists() && Reflector.ForgeKeyBinding_setKeyConflictContext.exists()) {
            final Object object = Reflector.getFieldValue(Reflector.KeyConflictContext_IN_GAME);
            Reflector.call(this.keyBindForward, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindLeft, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindBack, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindRight, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindJump, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindSneak, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindSprint, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindAttack, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindChat, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindPlayerList, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindCommand, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindTogglePerspective, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindSmoothCamera, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
            Reflector.call(this.keyBindSwapHands, Reflector.ForgeKeyBinding_setKeyConflictContext, object);
        }
    }
    
    public void onGuiClosed() {
        if (this.needsResourceRefresh) {
            this.mc.scheduleResourcesRefresh();
            this.needsResourceRefresh = false;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new Gson();
        TYPE_LIST_STRING = new ParameterizedType() {
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
        COLON_SPLITTER = Splitter.on(':');
        GUISCALES = new String[] { "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large" };
        PARTICLES = new String[] { "options.particles.all", "options.particles.decreased", "options.particles.minimal" };
        AMBIENT_OCCLUSIONS = new String[] { "options.ao.off", "options.ao.min", "options.ao.max" };
        CLOUDS_TYPES = new String[] { "options.off", "options.clouds.fast", "options.clouds.fancy" };
        ATTACK_INDICATORS = new String[] { "options.off", "options.attack.crosshair", "options.attack.hotbar" };
        OF_TREES_VALUES = new int[] { 0, 1, 4, 2 };
        OF_DYNAMIC_LIGHTS = new int[] { 3, 1, 2 };
        KEYS_DYNAMIC_LIGHTS = new String[] { "options.off", "options.graphics.fast", "options.graphics.fancy" };
    }
    
    public enum Options
    {
        INVERT_MOUSE("options.invertMouse", false, true), 
        SENSITIVITY("options.sensitivity", true, false), 
        FOV("options.fov", true, false, 30.0f, 110.0f, 1.0f), 
        GAMMA("options.gamma", true, false), 
        SATURATION("options.saturation", true, false), 
        RENDER_DISTANCE("options.renderDistance", true, false, 2.0f, 16.0f, 1.0f), 
        VIEW_BOBBING("options.viewBobbing", false, true), 
        ANAGLYPH("options.anaglyph", false, true), 
        FRAMERATE_LIMIT("options.framerateLimit", true, false, 0.0f, 260.0f, 5.0f), 
        FBO_ENABLE("options.fboEnable", false, true), 
        RENDER_CLOUDS("options.renderClouds", false, false), 
        GRAPHICS("options.graphics", false, false), 
        AMBIENT_OCCLUSION("options.ao", false, false), 
        GUI_SCALE("options.guiScale", false, false), 
        PARTICLES("options.particles", false, false), 
        CHAT_VISIBILITY("options.chat.visibility", false, false), 
        CHAT_COLOR("options.chat.color", false, true), 
        CHAT_LINKS("options.chat.links", false, true), 
        CHAT_OPACITY("options.chat.opacity", true, false), 
        CHAT_LINKS_PROMPT("options.chat.links.prompt", false, true), 
        SNOOPER_ENABLED("options.snooper", false, true), 
        USE_FULLSCREEN("options.fullscreen", false, true), 
        ENABLE_VSYNC("options.vsync", false, true), 
        USE_VBO("options.vbo", false, true), 
        TOUCHSCREEN("options.touchscreen", false, true), 
        CHAT_SCALE("options.chat.scale", true, false), 
        CHAT_WIDTH("options.chat.width", true, false), 
        CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false), 
        CHAT_HEIGHT_UNFOCUSED("options.chat.height.unfocused", true, false), 
        MIPMAP_LEVELS("options.mipmapLevels", true, false, 0.0f, 4.0f, 1.0f), 
        FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true), 
        REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true), 
        ENTITY_SHADOWS("options.entityShadows", false, true), 
        MAIN_HAND("options.mainHand", false, false), 
        ATTACK_INDICATOR("options.attackIndicator", false, false), 
        ENABLE_WEAK_ATTACKS("options.enableWeakAttacks", false, true), 
        SHOW_SUBTITLES("options.showSubtitles", false, true), 
        REALMS_NOTIFICATIONS("options.realmsNotifications", false, true), 
        AUTO_JUMP("options.autoJump", false, true), 
        FOG_FANCY("of.options.FOG_FANCY", false, false), 
        FOG_START("of.options.FOG_START", false, false), 
        MIPMAP_TYPE("of.options.MIPMAP_TYPE", true, false, 0.0f, 3.0f, 1.0f), 
        SMOOTH_FPS("of.options.SMOOTH_FPS", false, false), 
        CLOUDS("of.options.CLOUDS", false, false), 
        CLOUD_HEIGHT("of.options.CLOUD_HEIGHT", true, false), 
        TREES("of.options.TREES", false, false), 
        RAIN("of.options.RAIN", false, false), 
        ANIMATED_WATER("of.options.ANIMATED_WATER", false, false), 
        ANIMATED_LAVA("of.options.ANIMATED_LAVA", false, false), 
        ANIMATED_FIRE("of.options.ANIMATED_FIRE", false, false), 
        ANIMATED_PORTAL("of.options.ANIMATED_PORTAL", false, false), 
        AO_LEVEL("of.options.AO_LEVEL", true, false), 
        LAGOMETER("of.options.LAGOMETER", false, false), 
        SHOW_FPS("of.options.SHOW_FPS", false, false), 
        AUTOSAVE_TICKS("of.options.AUTOSAVE_TICKS", false, false), 
        BETTER_GRASS("of.options.BETTER_GRASS", false, false), 
        ANIMATED_REDSTONE("of.options.ANIMATED_REDSTONE", false, false), 
        ANIMATED_EXPLOSION("of.options.ANIMATED_EXPLOSION", false, false), 
        ANIMATED_FLAME("of.options.ANIMATED_FLAME", false, false), 
        ANIMATED_SMOKE("of.options.ANIMATED_SMOKE", false, false), 
        WEATHER("of.options.WEATHER", false, false), 
        SKY("of.options.SKY", false, false), 
        STARS("of.options.STARS", false, false), 
        SUN_MOON("of.options.SUN_MOON", false, false), 
        VIGNETTE("of.options.VIGNETTE", false, false), 
        CHUNK_UPDATES("of.options.CHUNK_UPDATES", false, false), 
        CHUNK_UPDATES_DYNAMIC("of.options.CHUNK_UPDATES_DYNAMIC", false, false), 
        TIME("of.options.TIME", false, false), 
        CLEAR_WATER("of.options.CLEAR_WATER", false, false), 
        SMOOTH_WORLD("of.options.SMOOTH_WORLD", false, false), 
        VOID_PARTICLES("of.options.VOID_PARTICLES", false, false), 
        WATER_PARTICLES("of.options.WATER_PARTICLES", false, false), 
        RAIN_SPLASH("of.options.RAIN_SPLASH", false, false), 
        PORTAL_PARTICLES("of.options.PORTAL_PARTICLES", false, false), 
        POTION_PARTICLES("of.options.POTION_PARTICLES", false, false), 
        FIREWORK_PARTICLES("of.options.FIREWORK_PARTICLES", false, false), 
        PROFILER("of.options.PROFILER", false, false), 
        DRIPPING_WATER_LAVA("of.options.DRIPPING_WATER_LAVA", false, false), 
        BETTER_SNOW("of.options.BETTER_SNOW", false, false), 
        FULLSCREEN_MODE("of.options.FULLSCREEN_MODE", true, false, 0.0f, (float)Config.getDisplayModes().length, 1.0f), 
        ANIMATED_TERRAIN("of.options.ANIMATED_TERRAIN", false, false), 
        SWAMP_COLORS("of.options.SWAMP_COLORS", false, false), 
        RANDOM_ENTITIES("of.options.RANDOM_ENTITIES", false, false), 
        SMOOTH_BIOMES("of.options.SMOOTH_BIOMES", false, false), 
        CUSTOM_FONTS("of.options.CUSTOM_FONTS", false, false), 
        CUSTOM_COLORS("of.options.CUSTOM_COLORS", false, false), 
        SHOW_CAPES("of.options.SHOW_CAPES", false, false), 
        CONNECTED_TEXTURES("of.options.CONNECTED_TEXTURES", false, false), 
        CUSTOM_ITEMS("of.options.CUSTOM_ITEMS", false, false), 
        AA_LEVEL("of.options.AA_LEVEL", true, false, 0.0f, 16.0f, 1.0f), 
        AF_LEVEL("of.options.AF_LEVEL", true, false, 1.0f, 16.0f, 1.0f), 
        ANIMATED_TEXTURES("of.options.ANIMATED_TEXTURES", false, false), 
        NATURAL_TEXTURES("of.options.NATURAL_TEXTURES", false, false), 
        EMISSIVE_TEXTURES("of.options.EMISSIVE_TEXTURES", false, false), 
        HELD_ITEM_TOOLTIPS("of.options.HELD_ITEM_TOOLTIPS", false, false), 
        DROPPED_ITEMS("of.options.DROPPED_ITEMS", false, false), 
        LAZY_CHUNK_LOADING("of.options.LAZY_CHUNK_LOADING", false, false), 
        CUSTOM_SKY("of.options.CUSTOM_SKY", false, false), 
        FAST_MATH("of.options.FAST_MATH", false, false), 
        FAST_RENDER("of.options.FAST_RENDER", false, false), 
        TRANSLUCENT_BLOCKS("of.options.TRANSLUCENT_BLOCKS", false, false), 
        DYNAMIC_FOV("of.options.DYNAMIC_FOV", false, false), 
        DYNAMIC_LIGHTS("of.options.DYNAMIC_LIGHTS", false, false), 
        ALTERNATE_BLOCKS("of.options.ALTERNATE_BLOCKS", false, false), 
        CUSTOM_ENTITY_MODELS("of.options.CUSTOM_ENTITY_MODELS", false, false), 
        ADVANCED_TOOLTIPS("of.options.ADVANCED_TOOLTIPS", false, false), 
        SCREENSHOT_SIZE("of.options.SCREENSHOT_SIZE", false, false), 
        CUSTOM_GUIS("of.options.CUSTOM_GUIS", false, false), 
        RENDER_REGIONS("of.options.RENDER_REGIONS", false, false), 
        SHOW_GL_ERRORS("of.options.SHOW_GL_ERRORS", false, false), 
        SMART_ANIMATIONS("of.options.SMART_ANIMATIONS", false, false);
        
        private final boolean isFloat;
        private final boolean isBoolean;
        private final String translation;
        private final float valueStep;
        private float valueMin;
        private float valueMax;
        
        public static Options byOrdinal(final int ordinal) {
            for (final Options gamesettings$options : values()) {
                if (gamesettings$options.getOrdinal() == ordinal) {
                    return gamesettings$options;
                }
            }
            return null;
        }
        
        private Options(final String translation, final boolean isFloat, final boolean isBoolean) {
            this(translation, isFloat, isBoolean, 0.0f, 1.0f, 0.0f);
        }
        
        private Options(final String translation, final boolean isFloat, final boolean isBoolean, final float valMin, final float valMax, final float valStep) {
            this.translation = translation;
            this.isFloat = isFloat;
            this.isBoolean = isBoolean;
            this.valueMin = valMin;
            this.valueMax = valMax;
            this.valueStep = valStep;
        }
        
        public boolean isFloat() {
            return this.isFloat;
        }
        
        public boolean isBoolean() {
            return this.isBoolean;
        }
        
        public int getOrdinal() {
            return this.ordinal();
        }
        
        public String getTranslation() {
            return this.translation;
        }
        
        public float getValueMin() {
            return this.valueMin;
        }
        
        public float getValueMax() {
            return this.valueMax;
        }
        
        public void setValueMax(final float value) {
            this.valueMax = value;
        }
        
        public float normalizeValue(final float value) {
            return MathHelper.clamp((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0f, 1.0f);
        }
        
        public float denormalizeValue(final float value) {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp(value, 0.0f, 1.0f));
        }
        
        public float snapToStepClamp(float value) {
            value = this.snapToStep(value);
            return MathHelper.clamp(value, this.valueMin, this.valueMax);
        }
        
        private float snapToStep(float value) {
            if (this.valueStep > 0.0f) {
                value = this.valueStep * Math.round(value / this.valueStep);
            }
            return value;
        }
    }
}
