package net.minecraft.client.settings;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.optifine.ClearWater;
import net.optifine.CustomColors;
import net.optifine.CustomGuis;
import net.optifine.CustomSky;
import net.optifine.DynamicLights;
import net.optifine.Lang;
import net.optifine.NaturalTextures;
import net.optifine.RandomEntities;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.Shaders;
import net.optifine.util.KeyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/*
 * Exception performing whole class analysis ignored.
 */
public class GameSettings {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();
    private static final ParameterizedType typeListString = new /* Unavailable Anonymous Inner Class!! */;
    private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
    private static final String[] AMBIENT_OCCLUSIONS = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
    private static final String[] STREAM_COMPRESSIONS = new String[]{"options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high"};
    private static final String[] STREAM_CHAT_MODES = new String[]{"options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never"};
    private static final String[] STREAM_CHAT_FILTER_MODES = new String[]{"options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods"};
    private static final String[] STREAM_MIC_MODES = new String[]{"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk"};
    private static final String[] CLOUDS_TYPES = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
    public float mouseSensitivity = 0.5f;
    public boolean invertMouse;
    public int renderDistanceChunks = -1;
    public boolean viewBobbing = true;
    public boolean anaglyph;
    public boolean fboEnable = true;
    public int limitFramerate = 120;
    public int clouds = 2;
    public boolean fancyGraphics = true;
    public int ambientOcclusion = 2;
    public List<String> resourcePacks = Lists.newArrayList();
    public List<String> incompatibleResourcePacks = Lists.newArrayList();
    public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
    public boolean chatColours = true;
    public boolean chatLinks = true;
    public boolean chatLinksPrompt = true;
    public float chatOpacity = 1.0f;
    public boolean snooperEnabled = true;
    public boolean fullScreen;
    public boolean enableVsync = true;
    public boolean useVbo = false;
    public boolean allowBlockAlternatives = true;
    public boolean reducedDebugInfo = false;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus = true;
    private final Set<EnumPlayerModelParts> setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
    public boolean touchscreen;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips = true;
    public float chatScale = 1.0f;
    public float chatWidth = 1.0f;
    public float chatHeightUnfocused = 0.44366196f;
    public float chatHeightFocused = 1.0f;
    public boolean showInventoryAchievementHint = true;
    public int mipmapLevels = 4;
    private Map<SoundCategory, Float> mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
    public float streamBytesPerPixel = 0.5f;
    public float streamMicVolume = 1.0f;
    public float streamGameVolume = 1.0f;
    public float streamKbps = 0.5412844f;
    public float streamFps = 0.31690142f;
    public int streamCompression = 1;
    public boolean streamSendMetadata = true;
    public String streamPreferredServer = "";
    public int streamChatEnabled = 0;
    public int streamChatUserFilter = 0;
    public int streamMicToggleBehavior = 0;
    public boolean useNativeTransport = true;
    public boolean entityShadows = true;
    public boolean realmsNotifications = true;
    public KeyBinding keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
    public KeyBinding keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
    public KeyBinding keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
    public KeyBinding keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
    public KeyBinding keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
    public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
    public KeyBinding keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
    public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
    public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
    public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
    public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
    public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
    public KeyBinding keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
    public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
    public KeyBinding keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
    public KeyBinding keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
    public KeyBinding keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
    public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
    public KeyBinding keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
    public KeyBinding keyBindSpectatorOutlines = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
    public KeyBinding keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
    public KeyBinding keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
    public KeyBinding keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
    public KeyBinding keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
    public KeyBinding[] keyBindsHotbar = new KeyBinding[]{new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
    public KeyBinding[] keyBindings;
    protected Minecraft mc;
    private File optionsFile;
    public EnumDifficulty difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public boolean showLagometer;
    public String lastServer = "";
    public boolean smoothCamera;
    public boolean debugCamEnable;
    public float fovSetting = 70.0f;
    public float gammaSetting;
    public float saturation;
    public int guiScale;
    public int particleSetting;
    public String language = "en_US";
    public boolean forceUnicodeFont = false;
    public int ofFogType = 1;
    public float ofFogStart = 0.8f;
    public int ofMipmapType = 0;
    public boolean ofOcclusionFancy = false;
    public boolean ofSmoothFps = false;
    public boolean ofSmoothWorld = Config.isSingleProcessor();
    public boolean ofLazyChunkLoading = Config.isSingleProcessor();
    public boolean ofRenderRegions = false;
    public boolean ofSmartAnimations = false;
    public float ofAoLevel = 1.0f;
    public int ofAaLevel = 0;
    public int ofAfLevel = 1;
    public int ofClouds = 0;
    public float ofCloudsHeight = 0.0f;
    public int ofTrees = 0;
    public int ofRain = 0;
    public int ofDroppedItems = 0;
    public int ofBetterGrass = 3;
    public int ofAutoSaveTicks = 4000;
    public boolean ofLagometer = false;
    public boolean ofProfiler = false;
    public boolean ofShowFps = false;
    public boolean ofWeather = true;
    public boolean ofSky = true;
    public boolean ofStars = true;
    public boolean ofSunMoon = true;
    public int ofVignette = 0;
    public int ofChunkUpdates = 1;
    public boolean ofChunkUpdatesDynamic = false;
    public int ofTime = 0;
    public boolean ofClearWater = false;
    public boolean ofBetterSnow = false;
    public String ofFullscreenMode = "Default";
    public boolean ofSwampColors = true;
    public boolean ofRandomEntities = true;
    public boolean ofSmoothBiomes = true;
    public boolean ofCustomFonts = true;
    public boolean ofCustomColors = true;
    public boolean ofCustomSky = true;
    public boolean ofShowCapes = true;
    public int ofConnectedTextures = 2;
    public boolean ofCustomItems = true;
    public boolean ofNaturalTextures = false;
    public boolean ofEmissiveTextures = true;
    public boolean ofFastMath = false;
    public boolean ofFastRender = false;
    public int ofTranslucentBlocks = 0;
    public boolean ofDynamicFov = true;
    public boolean ofAlternateBlocks = true;
    public int ofDynamicLights = 3;
    public boolean ofCustomEntityModels = true;
    public boolean ofCustomGuis = true;
    public boolean ofShowGlErrors = true;
    public int ofScreenshotSize = 1;
    public int ofAnimatedWater = 0;
    public int ofAnimatedLava = 0;
    public boolean ofAnimatedFire = true;
    public boolean ofAnimatedPortal = true;
    public boolean ofAnimatedRedstone = true;
    public boolean ofAnimatedExplosion = true;
    public boolean ofAnimatedFlame = true;
    public boolean ofAnimatedSmoke = true;
    public boolean ofVoidParticles = true;
    public boolean ofWaterParticles = true;
    public boolean ofRainSplash = true;
    public boolean ofPortalParticles = true;
    public boolean ofPotionParticles = true;
    public boolean ofFireworkParticles = true;
    public boolean ofDrippingWaterLava = true;
    public boolean ofAnimatedTerrain = true;
    public boolean ofAnimatedTextures = true;
    public static final int DEFAULT = 0;
    public static final int FAST = 1;
    public static final int FANCY = 2;
    public static final int OFF = 3;
    public static final int SMART = 4;
    public static final int ANIM_ON = 0;
    public static final int ANIM_GENERATED = 1;
    public static final int ANIM_OFF = 2;
    public static final String DEFAULT_STR = "Default";
    private static final int[] OF_TREES_VALUES = new int[]{0, 1, 4, 2};
    private static final int[] OF_DYNAMIC_LIGHTS = new int[]{3, 1, 2};
    private static final String[] KEYS_DYNAMIC_LIGHTS = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
    public KeyBinding ofKeyBindZoom;
    private File optionsFileOF;

    public GameSettings(Minecraft mcIn, File optionsFileIn) {
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.mc = mcIn;
        this.optionsFile = new File(optionsFileIn, "options.txt");
        if (mcIn.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
            Options.RENDER_DISTANCE.setValueMax(32.0f);
            long i = 1000000L;
            if (Runtime.getRuntime().maxMemory() >= 1500L * i) {
                Options.RENDER_DISTANCE.setValueMax(48.0f);
            }
            if (Runtime.getRuntime().maxMemory() >= 2500L * i) {
                Options.RENDER_DISTANCE.setValueMax(64.0f);
            }
        } else {
            Options.RENDER_DISTANCE.setValueMax(16.0f);
        }
        this.renderDistanceChunks = mcIn.isJava64bit() ? 12 : 8;
        this.optionsFileOF = new File(optionsFileIn, "optionsof.txt");
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
        this.keyBindings = (KeyBinding[])ArrayUtils.add((Object[])this.keyBindings, (Object)this.ofKeyBindZoom);
        KeyUtils.fixKeyConflicts((KeyBinding[])this.keyBindings, (KeyBinding[])new KeyBinding[]{this.ofKeyBindZoom});
        this.renderDistanceChunks = 8;
        this.loadOptions();
        Config.initGameSettings((GameSettings)this);
    }

    public GameSettings() {
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
    }

    public static String getKeyDisplayString(int key) {
        return key < 0 ? I18n.format((String)"key.mouseButton", (Object[])new Object[]{key + 101}) : (key < 256 ? Keyboard.getKeyName((int)key) : String.format((String)"%c", (Object[])new Object[]{Character.valueOf((char)((char)(key - 256)))}).toUpperCase());
    }

    public static boolean isKeyDown(KeyBinding key) {
        return key.getKeyCode() == 0 ? false : (key.getKeyCode() < 0 ? Mouse.isButtonDown((int)(key.getKeyCode() + 100)) : Keyboard.isKeyDown((int)key.getKeyCode()));
    }

    public void setOptionKeyBinding(KeyBinding key, int keyCode) {
        key.setKeyCode(keyCode);
        this.saveOptions();
    }

    public void setOptionFloatValue(Options settingsOption, float value) {
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
            int i = this.mipmapLevels;
            this.mipmapLevels = (int)value;
            if ((float)i != value) {
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
        }
        if (settingsOption == Options.STREAM_VOLUME_SYSTEM) {
            this.streamGameVolume = value;
        }
        if (settingsOption == Options.STREAM_KBPS) {
            this.streamKbps = value;
        }
        if (settingsOption == Options.STREAM_FPS) {
            this.streamFps = value;
        }
    }

    public void setOptionValue(Options settingsOption, int value) {
        this.setOptionValueOF(settingsOption, value);
        if (settingsOption == Options.INVERT_MOUSE) {
            boolean bl = this.invertMouse = !this.invertMouse;
        }
        if (settingsOption == Options.GUI_SCALE) {
            this.guiScale += value;
            if (GuiScreen.isShiftKeyDown()) {
                this.guiScale = 0;
            }
            DisplayMode displaymode = Config.getLargestDisplayMode();
            int i = displaymode.getWidth() / 320;
            int j = displaymode.getHeight() / 240;
            int k = Math.min((int)i, (int)j);
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
            boolean bl = this.viewBobbing = !this.viewBobbing;
        }
        if (settingsOption == Options.RENDER_CLOUDS) {
            this.clouds = (this.clouds + value) % 3;
        }
        if (settingsOption == Options.FORCE_UNICODE_FONT) {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            this.mc.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }
        if (settingsOption == Options.FBO_ENABLE) {
            boolean bl = this.fboEnable = !this.fboEnable;
        }
        if (settingsOption == Options.ANAGLYPH) {
            if (!this.anaglyph && Config.isShaders()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.an.shaders1"), (String)Lang.get((String)"of.message.an.shaders2"));
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
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((int)((this.chatVisibility.getChatVisibility() + value) % 3));
        }
        if (settingsOption == Options.STREAM_COMPRESSION) {
            this.streamCompression = (this.streamCompression + value) % 3;
        }
        if (settingsOption == Options.STREAM_SEND_METADATA) {
            boolean bl = this.streamSendMetadata = !this.streamSendMetadata;
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
            boolean bl = this.chatColours = !this.chatColours;
        }
        if (settingsOption == Options.CHAT_LINKS) {
            boolean bl = this.chatLinks = !this.chatLinks;
        }
        if (settingsOption == Options.CHAT_LINKS_PROMPT) {
            boolean bl = this.chatLinksPrompt = !this.chatLinksPrompt;
        }
        if (settingsOption == Options.SNOOPER_ENABLED) {
            boolean bl = this.snooperEnabled = !this.snooperEnabled;
        }
        if (settingsOption == Options.TOUCHSCREEN) {
            boolean bl = this.touchscreen = !this.touchscreen;
        }
        if (settingsOption == Options.USE_FULLSCREEN) {
            boolean bl = this.fullScreen = !this.fullScreen;
            if (this.mc.isFullScreen() != this.fullScreen) {
                this.mc.toggleFullscreen();
            }
        }
        if (settingsOption == Options.ENABLE_VSYNC) {
            this.enableVsync = !this.enableVsync;
            Display.setVSyncEnabled((boolean)this.enableVsync);
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
            boolean bl = this.reducedDebugInfo = !this.reducedDebugInfo;
        }
        if (settingsOption == Options.ENTITY_SHADOWS) {
            boolean bl = this.entityShadows = !this.entityShadows;
        }
        if (settingsOption == Options.REALMS_NOTIFICATIONS) {
            this.realmsNotifications = !this.realmsNotifications;
        }
        this.saveOptions();
    }

    public float getOptionFloatValue(Options settingOption) {
        float f = this.getOptionFloatValueOF(settingOption);
        return f != Float.MAX_VALUE ? f : (settingOption == Options.FOV ? this.fovSetting : (settingOption == Options.GAMMA ? this.gammaSetting : (settingOption == Options.SATURATION ? this.saturation : (settingOption == Options.SENSITIVITY ? this.mouseSensitivity : (settingOption == Options.CHAT_OPACITY ? this.chatOpacity : (settingOption == Options.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : (settingOption == Options.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : (settingOption == Options.CHAT_SCALE ? this.chatScale : (settingOption == Options.CHAT_WIDTH ? this.chatWidth : (settingOption == Options.FRAMERATE_LIMIT ? (float)this.limitFramerate : (settingOption == Options.MIPMAP_LEVELS ? (float)this.mipmapLevels : (settingOption == Options.RENDER_DISTANCE ? (float)this.renderDistanceChunks : (settingOption == Options.STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : (settingOption == Options.STREAM_VOLUME_MIC ? this.streamMicVolume : (settingOption == Options.STREAM_VOLUME_SYSTEM ? this.streamGameVolume : (settingOption == Options.STREAM_KBPS ? this.streamKbps : (settingOption == Options.STREAM_FPS ? this.streamFps : 0.0f)))))))))))))))));
    }

    public boolean getOptionOrdinalValue(Options settingOption) {
        switch (2.$SwitchMap$net$minecraft$client$settings$GameSettings$Options[settingOption.ordinal()]) {
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
                return this.chatColours;
            }
            case 6: {
                return this.chatLinks;
            }
            case 7: {
                return this.chatLinksPrompt;
            }
            case 8: {
                return this.snooperEnabled;
            }
            case 9: {
                return this.fullScreen;
            }
            case 10: {
                return this.enableVsync;
            }
            case 11: {
                return this.useVbo;
            }
            case 12: {
                return this.touchscreen;
            }
            case 13: {
                return this.streamSendMetadata;
            }
            case 14: {
                return this.forceUnicodeFont;
            }
            case 15: {
                return this.allowBlockAlternatives;
            }
            case 16: {
                return this.reducedDebugInfo;
            }
            case 17: {
                return this.entityShadows;
            }
            case 18: {
                return this.realmsNotifications;
            }
        }
        return false;
    }

    private static String getTranslation(String[] strArray, int index) {
        if (index < 0 || index >= strArray.length) {
            index = 0;
        }
        return I18n.format((String)strArray[index], (Object[])new Object[0]);
    }

    public String getKeyBinding(Options settingOption) {
        String s = this.getKeyBindingOF(settingOption);
        if (s != null) {
            return s;
        }
        String s1 = I18n.format((String)settingOption.getEnumString(), (Object[])new Object[0]) + ": ";
        if (settingOption.getEnumFloat()) {
            float f1 = this.getOptionFloatValue(settingOption);
            float f = settingOption.normalizeValue(f1);
            return settingOption == Options.MIPMAP_LEVELS && (double)f1 >= 4.0 ? s1 + Lang.get((String)"of.general.max") : (settingOption == Options.SENSITIVITY ? (f == 0.0f ? s1 + I18n.format((String)"options.sensitivity.min", (Object[])new Object[0]) : (f == 1.0f ? s1 + I18n.format((String)"options.sensitivity.max", (Object[])new Object[0]) : s1 + (int)(f * 200.0f) + "%")) : (settingOption == Options.FOV ? (f1 == 70.0f ? s1 + I18n.format((String)"options.fov.min", (Object[])new Object[0]) : (f1 == 110.0f ? s1 + I18n.format((String)"options.fov.max", (Object[])new Object[0]) : s1 + (int)f1)) : (settingOption == Options.FRAMERATE_LIMIT ? (f1 == Options.access$000((Options)settingOption) ? s1 + I18n.format((String)"options.framerateLimit.max", (Object[])new Object[0]) : s1 + (int)f1 + " fps") : (settingOption == Options.RENDER_CLOUDS ? (f1 == Options.access$100((Options)settingOption) ? s1 + I18n.format((String)"options.cloudHeight.min", (Object[])new Object[0]) : s1 + ((int)f1 + 128)) : (settingOption == Options.GAMMA ? (f == 0.0f ? s1 + I18n.format((String)"options.gamma.min", (Object[])new Object[0]) : (f == 1.0f ? s1 + I18n.format((String)"options.gamma.max", (Object[])new Object[0]) : s1 + "+" + (int)(f * 100.0f) + "%")) : (settingOption == Options.SATURATION ? s1 + (int)(f * 400.0f) + "%" : (settingOption == Options.CHAT_OPACITY ? s1 + (int)(f * 90.0f + 10.0f) + "%" : (settingOption == Options.CHAT_HEIGHT_UNFOCUSED ? s1 + GuiNewChat.calculateChatboxHeight((float)f) + "px" : (settingOption == Options.CHAT_HEIGHT_FOCUSED ? s1 + GuiNewChat.calculateChatboxHeight((float)f) + "px" : (settingOption == Options.CHAT_WIDTH ? s1 + GuiNewChat.calculateChatboxWidth((float)f) + "px" : (settingOption == Options.RENDER_DISTANCE ? s1 + (int)f1 + " chunks" : (settingOption == Options.MIPMAP_LEVELS ? (f1 == 0.0f ? s1 + I18n.format((String)"options.off", (Object[])new Object[0]) : s1 + (int)f1) : (settingOption == Options.STREAM_FPS ? s1 : (settingOption == Options.STREAM_KBPS ? s1 + " Kbps" : (settingOption == Options.STREAM_BYTES_PER_PIXEL ? s1 + String.format((String)"%.3f bpp", (Object[])new Object[]{Float.valueOf((float)0.0f)}) : (f == 0.0f ? s1 + I18n.format((String)"options.off", (Object[])new Object[0]) : s1 + (int)(f * 100.0f) + "%"))))))))))))))));
        }
        if (settingOption.getEnumBoolean()) {
            boolean flag = this.getOptionOrdinalValue(settingOption);
            return flag ? s1 + I18n.format((String)"options.on", (Object[])new Object[0]) : s1 + I18n.format((String)"options.off", (Object[])new Object[0]);
        }
        if (settingOption == Options.GUI_SCALE) {
            return this.guiScale >= GUISCALES.length ? s1 + this.guiScale + "x" : s1 + GameSettings.getTranslation(GUISCALES, this.guiScale);
        }
        if (settingOption == Options.CHAT_VISIBILITY) {
            return s1 + I18n.format((String)this.chatVisibility.getResourceKey(), (Object[])new Object[0]);
        }
        if (settingOption == Options.PARTICLES) {
            return s1 + GameSettings.getTranslation(PARTICLES, this.particleSetting);
        }
        if (settingOption == Options.AMBIENT_OCCLUSION) {
            return s1 + GameSettings.getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        if (settingOption == Options.STREAM_COMPRESSION) {
            return s1 + GameSettings.getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
        }
        if (settingOption == Options.STREAM_CHAT_ENABLED) {
            return s1 + GameSettings.getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
        }
        if (settingOption == Options.STREAM_CHAT_USER_FILTER) {
            return s1 + GameSettings.getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
        }
        if (settingOption == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return s1 + GameSettings.getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
        }
        if (settingOption == Options.RENDER_CLOUDS) {
            return s1 + GameSettings.getTranslation(CLOUDS_TYPES, this.clouds);
        }
        if (settingOption == Options.GRAPHICS) {
            if (this.fancyGraphics) {
                return s1 + I18n.format((String)"options.graphics.fancy", (Object[])new Object[0]);
            }
            String s2 = "options.graphics.fast";
            return s1 + I18n.format((String)"options.graphics.fast", (Object[])new Object[0]);
        }
        return s1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadOptions() {
        block81: {
            FileInputStream fileinputstream = null;
            try {
                if (this.optionsFile.exists()) {
                    fileinputstream = new FileInputStream(this.optionsFile);
                    BufferedReader bufferedreader = new BufferedReader((Reader)new InputStreamReader((InputStream)fileinputstream));
                    String s = "";
                    this.mapSoundLevels.clear();
                    while ((s = bufferedreader.readLine()) != null) {
                        try {
                            String[] astring = s.split(":");
                            if (astring[0].equals((Object)"mouseSensitivity")) {
                                this.mouseSensitivity = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"fov")) {
                                this.fovSetting = this.parseFloat(astring[1]) * 40.0f + 70.0f;
                            }
                            if (astring[0].equals((Object)"gamma")) {
                                this.gammaSetting = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"saturation")) {
                                this.saturation = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"invertYMouse")) {
                                this.invertMouse = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"renderDistance")) {
                                this.renderDistanceChunks = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"guiScale")) {
                                this.guiScale = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"particles")) {
                                this.particleSetting = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"bobView")) {
                                this.viewBobbing = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"anaglyph3d")) {
                                this.anaglyph = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"maxFps")) {
                                this.limitFramerate = Integer.parseInt((String)astring[1]);
                                if (this.enableVsync) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                                if (this.limitFramerate <= 0) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                            }
                            if (astring[0].equals((Object)"fboEnable")) {
                                this.fboEnable = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"difficulty")) {
                                this.difficulty = EnumDifficulty.getDifficultyEnum((int)Integer.parseInt((String)astring[1]));
                            }
                            if (astring[0].equals((Object)"fancyGraphics")) {
                                this.fancyGraphics = astring[1].equals((Object)"true");
                                this.updateRenderClouds();
                            }
                            if (astring[0].equals((Object)"ao")) {
                                this.ambientOcclusion = astring[1].equals((Object)"true") ? 2 : (astring[1].equals((Object)"false") ? 0 : Integer.parseInt((String)astring[1]));
                            }
                            if (astring[0].equals((Object)"renderClouds")) {
                                if (astring[1].equals((Object)"true")) {
                                    this.clouds = 2;
                                } else if (astring[1].equals((Object)"false")) {
                                    this.clouds = 0;
                                } else if (astring[1].equals((Object)"fast")) {
                                    this.clouds = 1;
                                }
                            }
                            if (astring[0].equals((Object)"resourcePacks")) {
                                this.resourcePacks = (List)gson.fromJson(s.substring(s.indexOf(58) + 1), (Type)typeListString);
                                if (this.resourcePacks == null) {
                                    this.resourcePacks = Lists.newArrayList();
                                }
                            }
                            if (astring[0].equals((Object)"incompatibleResourcePacks")) {
                                this.incompatibleResourcePacks = (List)gson.fromJson(s.substring(s.indexOf(58) + 1), (Type)typeListString);
                                if (this.incompatibleResourcePacks == null) {
                                    this.incompatibleResourcePacks = Lists.newArrayList();
                                }
                            }
                            if (astring[0].equals((Object)"lastServer") && astring.length >= 2) {
                                this.lastServer = s.substring(s.indexOf(58) + 1);
                            }
                            if (astring[0].equals((Object)"lang") && astring.length >= 2) {
                                this.language = astring[1];
                            }
                            if (astring[0].equals((Object)"chatVisibility")) {
                                this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((int)Integer.parseInt((String)astring[1]));
                            }
                            if (astring[0].equals((Object)"chatColors")) {
                                this.chatColours = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"chatLinks")) {
                                this.chatLinks = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"chatLinksPrompt")) {
                                this.chatLinksPrompt = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"chatOpacity")) {
                                this.chatOpacity = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"snooperEnabled")) {
                                this.snooperEnabled = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"fullscreen")) {
                                this.fullScreen = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"enableVsync")) {
                                this.enableVsync = astring[1].equals((Object)"true");
                                if (this.enableVsync) {
                                    this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                                }
                                this.updateVSync();
                            }
                            if (astring[0].equals((Object)"useVbo")) {
                                this.useVbo = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"hideServerAddress")) {
                                this.hideServerAddress = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"advancedItemTooltips")) {
                                this.advancedItemTooltips = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"pauseOnLostFocus")) {
                                this.pauseOnLostFocus = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"touchscreen")) {
                                this.touchscreen = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"overrideHeight")) {
                                this.overrideHeight = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"overrideWidth")) {
                                this.overrideWidth = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"heldItemTooltips")) {
                                this.heldItemTooltips = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"chatHeightFocused")) {
                                this.chatHeightFocused = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"chatHeightUnfocused")) {
                                this.chatHeightUnfocused = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"chatScale")) {
                                this.chatScale = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"chatWidth")) {
                                this.chatWidth = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"showInventoryAchievementHint")) {
                                this.showInventoryAchievementHint = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"mipmapLevels")) {
                                this.mipmapLevels = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"streamBytesPerPixel")) {
                                this.streamBytesPerPixel = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"streamMicVolume")) {
                                this.streamMicVolume = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"streamSystemVolume")) {
                                this.streamGameVolume = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"streamKbps")) {
                                this.streamKbps = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"streamFps")) {
                                this.streamFps = this.parseFloat(astring[1]);
                            }
                            if (astring[0].equals((Object)"streamCompression")) {
                                this.streamCompression = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"streamSendMetadata")) {
                                this.streamSendMetadata = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"streamPreferredServer") && astring.length >= 2) {
                                this.streamPreferredServer = s.substring(s.indexOf(58) + 1);
                            }
                            if (astring[0].equals((Object)"streamChatEnabled")) {
                                this.streamChatEnabled = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"streamChatUserFilter")) {
                                this.streamChatUserFilter = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"streamMicToggleBehavior")) {
                                this.streamMicToggleBehavior = Integer.parseInt((String)astring[1]);
                            }
                            if (astring[0].equals((Object)"forceUnicodeFont")) {
                                this.forceUnicodeFont = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"allowBlockAlternatives")) {
                                this.allowBlockAlternatives = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"reducedDebugInfo")) {
                                this.reducedDebugInfo = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"useNativeTransport")) {
                                this.useNativeTransport = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"entityShadows")) {
                                this.entityShadows = astring[1].equals((Object)"true");
                            }
                            if (astring[0].equals((Object)"realmsNotifications")) {
                                this.realmsNotifications = astring[1].equals((Object)"true");
                            }
                            for (KeyBinding keyBinding : this.keyBindings) {
                                if (!astring[0].equals((Object)("key_" + keyBinding.getKeyDescription()))) continue;
                                keyBinding.setKeyCode(Integer.parseInt((String)astring[1]));
                            }
                            for (KeyBinding keyBinding : SoundCategory.values()) {
                                if (!astring[0].equals((Object)("soundCategory_" + keyBinding.getCategoryName()))) continue;
                                this.mapSoundLevels.put((Object)keyBinding, (Object)Float.valueOf((float)this.parseFloat(astring[1])));
                            }
                            for (KeyBinding keyBinding : EnumPlayerModelParts.values()) {
                                if (!astring[0].equals((Object)("modelPart_" + keyBinding.getPartName()))) continue;
                                this.setModelPartEnabled((EnumPlayerModelParts)keyBinding, astring[1].equals((Object)"true"));
                            }
                        }
                        catch (Exception exception) {
                            logger.warn("Skipping bad option: " + s);
                            exception.printStackTrace();
                        }
                    }
                    KeyBinding.resetKeyBindingArrayAndHash();
                    bufferedreader.close();
                    IOUtils.closeQuietly((InputStream)fileinputstream);
                    break block81;
                }
                IOUtils.closeQuietly(fileinputstream);
            }
            catch (Exception exception1) {
                logger.error("Failed to load options", (Throwable)exception1);
                break block81;
            }
            finally {
                IOUtils.closeQuietly(fileinputstream);
            }
            return;
        }
        this.loadOfOptions();
    }

    private float parseFloat(String str) {
        return str.equals((Object)"true") ? 1.0f : (str.equals((Object)"false") ? 0.0f : Float.parseFloat((String)str));
    }

    public void saveOptions() {
        Object object;
        if (Reflector.FMLClientHandler.exists() && (object = Reflector.call((ReflectorMethod)Reflector.FMLClientHandler_instance, (Object[])new Object[0])) != null && Reflector.callBoolean((Object)object, (ReflectorMethod)Reflector.FMLClientHandler_isLoading, (Object[])new Object[0])) {
            return;
        }
        try {
            PrintWriter printwriter = new PrintWriter((Writer)new FileWriter(this.optionsFile));
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
                }
            }
            printwriter.println("resourcePacks:" + gson.toJson(this.resourcePacks));
            printwriter.println("incompatibleResourcePacks:" + gson.toJson(this.incompatibleResourcePacks));
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
            for (KeyBinding keyBinding : this.keyBindings) {
                printwriter.println("key_" + keyBinding.getKeyDescription() + ":" + keyBinding.getKeyCode());
            }
            for (KeyBinding keyBinding : SoundCategory.values()) {
                printwriter.println("soundCategory_" + keyBinding.getCategoryName() + ":" + this.getSoundLevel((SoundCategory)keyBinding));
            }
            for (KeyBinding keyBinding : EnumPlayerModelParts.values()) {
                printwriter.println("modelPart_" + keyBinding.getPartName() + ":" + this.setModelParts.contains((Object)keyBinding));
            }
            printwriter.close();
        }
        catch (Exception exception) {
            logger.error("Failed to save options", (Throwable)exception);
        }
        this.saveOfOptions();
        this.sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory sndCategory) {
        return this.mapSoundLevels.containsKey((Object)sndCategory) ? ((Float)this.mapSoundLevels.get((Object)sndCategory)).floatValue() : 1.0f;
    }

    public void setSoundLevel(SoundCategory sndCategory, float soundLevel) {
        this.mc.getSoundHandler().setSoundLevel(sndCategory, soundLevel);
        this.mapSoundLevels.put((Object)sndCategory, (Object)Float.valueOf((float)soundLevel));
    }

    public void sendSettingsToServer() {
        if (this.mc.thePlayer != null) {
            int i = 0;
            for (EnumPlayerModelParts enumplayermodelparts : this.setModelParts) {
                i |= enumplayermodelparts.getPartMask();
            }
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i));
        }
    }

    public Set<EnumPlayerModelParts> getModelParts() {
        return ImmutableSet.copyOf(this.setModelParts);
    }

    public void setModelPartEnabled(EnumPlayerModelParts modelPart, boolean enable) {
        if (enable) {
            this.setModelParts.add((Object)modelPart);
        } else {
            this.setModelParts.remove((Object)modelPart);
        }
        this.sendSettingsToServer();
    }

    public void switchModelPartEnabled(EnumPlayerModelParts modelPart) {
        if (!this.getModelParts().contains((Object)modelPart)) {
            this.setModelParts.add((Object)modelPart);
        } else {
            this.setModelParts.remove((Object)modelPart);
        }
        this.sendSettingsToServer();
    }

    public int shouldRenderClouds() {
        return this.renderDistanceChunks >= 4 ? this.clouds : 0;
    }

    public boolean isUsingNativeTransport() {
        return this.useNativeTransport;
    }

    private void setOptionFloatValueOF(Options p_setOptionFloatValueOF_1_, float p_setOptionFloatValueOF_2_) {
        if (p_setOptionFloatValueOF_1_ == Options.CLOUD_HEIGHT) {
            this.ofCloudsHeight = p_setOptionFloatValueOF_2_;
            this.mc.renderGlobal.resetClouds();
        }
        if (p_setOptionFloatValueOF_1_ == Options.AO_LEVEL) {
            this.ofAoLevel = p_setOptionFloatValueOF_2_;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionFloatValueOF_1_ == Options.AA_LEVEL) {
            int i = (int)p_setOptionFloatValueOF_2_;
            if (i > 0 && Config.isShaders()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.aa.shaders1"), (String)Lang.get((String)"of.message.aa.shaders2"));
                return;
            }
            int[] aint = new int[]{0, 2, 4, 6, 8, 12, 16};
            this.ofAaLevel = 0;
            for (int j = 0; j < aint.length; ++j) {
                if (i < aint[j]) continue;
                this.ofAaLevel = aint[j];
            }
            this.ofAaLevel = Config.limit((int)this.ofAaLevel, (int)0, (int)16);
        }
        if (p_setOptionFloatValueOF_1_ == Options.AF_LEVEL) {
            int k = (int)p_setOptionFloatValueOF_2_;
            if (k > 1 && Config.isShaders()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.af.shaders1"), (String)Lang.get((String)"of.message.af.shaders2"));
                return;
            }
            this.ofAfLevel = 1;
            while (this.ofAfLevel * 2 <= k) {
                this.ofAfLevel *= 2;
            }
            this.ofAfLevel = Config.limit((int)this.ofAfLevel, (int)1, (int)16);
            this.mc.refreshResources();
        }
        if (p_setOptionFloatValueOF_1_ == Options.MIPMAP_TYPE) {
            int l = (int)p_setOptionFloatValueOF_2_;
            this.ofMipmapType = Config.limit((int)l, (int)0, (int)3);
            this.mc.refreshResources();
        }
        if (p_setOptionFloatValueOF_1_ == Options.FULLSCREEN_MODE) {
            int i1 = (int)p_setOptionFloatValueOF_2_ - 1;
            String[] astring = Config.getDisplayModeNames();
            if (i1 < 0 || i1 >= astring.length) {
                this.ofFullscreenMode = "Default";
                return;
            }
            this.ofFullscreenMode = astring[i1];
        }
    }

    private float getOptionFloatValueOF(Options p_getOptionFloatValueOF_1_) {
        if (p_getOptionFloatValueOF_1_ == Options.CLOUD_HEIGHT) {
            return this.ofCloudsHeight;
        }
        if (p_getOptionFloatValueOF_1_ == Options.AO_LEVEL) {
            return this.ofAoLevel;
        }
        if (p_getOptionFloatValueOF_1_ == Options.AA_LEVEL) {
            return this.ofAaLevel;
        }
        if (p_getOptionFloatValueOF_1_ == Options.AF_LEVEL) {
            return this.ofAfLevel;
        }
        if (p_getOptionFloatValueOF_1_ == Options.MIPMAP_TYPE) {
            return this.ofMipmapType;
        }
        if (p_getOptionFloatValueOF_1_ == Options.FRAMERATE_LIMIT) {
            return (float)this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync ? 0.0f : (float)this.limitFramerate;
        }
        if (p_getOptionFloatValueOF_1_ == Options.FULLSCREEN_MODE) {
            if (this.ofFullscreenMode.equals((Object)"Default")) {
                return 0.0f;
            }
            List list = Arrays.asList((Object[])Config.getDisplayModeNames());
            int i = list.indexOf((Object)this.ofFullscreenMode);
            return i < 0 ? 0.0f : (float)(i + 1);
        }
        return Float.MAX_VALUE;
    }

    private void setOptionValueOF(Options p_setOptionValueOF_1_, int p_setOptionValueOF_2_) {
        if (p_setOptionValueOF_1_ == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    this.ofFogType = 2;
                    if (Config.isFancyFogAvailable()) break;
                    this.ofFogType = 3;
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
            boolean bl = this.ofSmoothFps = !this.ofSmoothFps;
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
            this.ofTrees = GameSettings.nextValue(this.ofTrees, OF_TREES_VALUES);
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
            boolean bl = this.ofAnimatedFire = !this.ofAnimatedFire;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_PORTAL) {
            boolean bl = this.ofAnimatedPortal = !this.ofAnimatedPortal;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_REDSTONE) {
            boolean bl = this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_EXPLOSION) {
            boolean bl = this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_FLAME) {
            boolean bl = this.ofAnimatedFlame = !this.ofAnimatedFlame;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_SMOKE) {
            boolean bl = this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
        }
        if (p_setOptionValueOF_1_ == Options.VOID_PARTICLES) {
            boolean bl = this.ofVoidParticles = !this.ofVoidParticles;
        }
        if (p_setOptionValueOF_1_ == Options.WATER_PARTICLES) {
            boolean bl = this.ofWaterParticles = !this.ofWaterParticles;
        }
        if (p_setOptionValueOF_1_ == Options.PORTAL_PARTICLES) {
            boolean bl = this.ofPortalParticles = !this.ofPortalParticles;
        }
        if (p_setOptionValueOF_1_ == Options.POTION_PARTICLES) {
            boolean bl = this.ofPotionParticles = !this.ofPotionParticles;
        }
        if (p_setOptionValueOF_1_ == Options.FIREWORK_PARTICLES) {
            boolean bl = this.ofFireworkParticles = !this.ofFireworkParticles;
        }
        if (p_setOptionValueOF_1_ == Options.DRIPPING_WATER_LAVA) {
            boolean bl = this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_TERRAIN) {
            boolean bl = this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_TEXTURES) {
            boolean bl = this.ofAnimatedTextures = !this.ofAnimatedTextures;
        }
        if (p_setOptionValueOF_1_ == Options.RAIN_SPLASH) {
            boolean bl = this.ofRainSplash = !this.ofRainSplash;
        }
        if (p_setOptionValueOF_1_ == Options.LAGOMETER) {
            boolean bl = this.ofLagometer = !this.ofLagometer;
        }
        if (p_setOptionValueOF_1_ == Options.SHOW_FPS) {
            boolean bl = this.ofShowFps = !this.ofShowFps;
        }
        if (p_setOptionValueOF_1_ == Options.AUTOSAVE_TICKS) {
            int i = 900;
            this.ofAutoSaveTicks = Math.max((int)(this.ofAutoSaveTicks / i * i), (int)i);
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
            } else {
                this.mc.refreshResources();
            }
        }
        if (p_setOptionValueOF_1_ == Options.WEATHER) {
            boolean bl = this.ofWeather = !this.ofWeather;
        }
        if (p_setOptionValueOF_1_ == Options.SKY) {
            boolean bl = this.ofSky = !this.ofSky;
        }
        if (p_setOptionValueOF_1_ == Options.STARS) {
            boolean bl = this.ofStars = !this.ofStars;
        }
        if (p_setOptionValueOF_1_ == Options.SUN_MOON) {
            boolean bl = this.ofSunMoon = !this.ofSunMoon;
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
            boolean bl = this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
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
            boolean bl = this.ofProfiler = !this.ofProfiler;
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
            boolean bl = this.ofShowCapes = !this.ofShowCapes;
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
            MathHelper.fastMath = this.ofFastMath = !this.ofFastMath;
        }
        if (p_setOptionValueOF_1_ == Options.FAST_RENDER) {
            if (!this.ofFastRender && Config.isShaders()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.fr.shaders1"), (String)Lang.get((String)"of.message.fr.shaders2"));
                return;
            }
            boolean bl = this.ofFastRender = !this.ofFastRender;
            if (this.ofFastRender) {
                this.mc.entityRenderer.stopUseShader();
            }
            Config.updateFramebufferSize();
        }
        if (p_setOptionValueOF_1_ == Options.TRANSLUCENT_BLOCKS) {
            this.ofTranslucentBlocks = this.ofTranslucentBlocks == 0 ? 1 : (this.ofTranslucentBlocks == 1 ? 2 : (this.ofTranslucentBlocks == 2 ? 0 : 0));
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.LAZY_CHUNK_LOADING) {
            boolean bl = this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
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
            boolean bl = this.ofDynamicFov = !this.ofDynamicFov;
        }
        if (p_setOptionValueOF_1_ == Options.ALTERNATE_BLOCKS) {
            this.ofAlternateBlocks = !this.ofAlternateBlocks;
            this.mc.refreshResources();
        }
        if (p_setOptionValueOF_1_ == Options.DYNAMIC_LIGHTS) {
            this.ofDynamicLights = GameSettings.nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
            DynamicLights.removeLights((RenderGlobal)this.mc.renderGlobal);
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
            boolean bl = this.ofShowGlErrors = !this.ofShowGlErrors;
        }
        if (p_setOptionValueOF_1_ == Options.HELD_ITEM_TOOLTIPS) {
            boolean bl = this.heldItemTooltips = !this.heldItemTooltips;
        }
        if (p_setOptionValueOF_1_ == Options.ADVANCED_TOOLTIPS) {
            this.advancedItemTooltips = !this.advancedItemTooltips;
        }
    }

    private String getKeyBindingOF(Options p_getKeyBindingOF_1_) {
        String s = I18n.format((String)p_getKeyBindingOF_1_.getEnumString(), (Object[])new Object[0]) + ": ";
        if (s == null) {
            s = p_getKeyBindingOF_1_.getEnumString();
        }
        if (p_getKeyBindingOF_1_ == Options.RENDER_DISTANCE) {
            int i1 = (int)this.getOptionFloatValue(p_getKeyBindingOF_1_);
            String s1 = I18n.format((String)"options.renderDistance.tiny", (Object[])new Object[0]);
            int i = 2;
            if (i1 >= 4) {
                s1 = I18n.format((String)"options.renderDistance.short", (Object[])new Object[0]);
                i = 4;
            }
            if (i1 >= 8) {
                s1 = I18n.format((String)"options.renderDistance.normal", (Object[])new Object[0]);
                i = 8;
            }
            if (i1 >= 16) {
                s1 = I18n.format((String)"options.renderDistance.far", (Object[])new Object[0]);
                i = 16;
            }
            if (i1 >= 32) {
                s1 = Lang.get((String)"of.options.renderDistance.extreme");
                i = 32;
            }
            if (i1 >= 48) {
                s1 = Lang.get((String)"of.options.renderDistance.insane");
                i = 48;
            }
            if (i1 >= 64) {
                s1 = Lang.get((String)"of.options.renderDistance.ludicrous");
                i = 64;
            }
            int j = this.renderDistanceChunks - i;
            String s2 = s1;
            if (j > 0) {
                s2 = s1 + "+";
            }
            return s + i1 + " " + s2 + "";
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
            }
            return s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.FOG_START) {
            return s + this.ofFogStart;
        }
        if (p_getKeyBindingOF_1_ == Options.MIPMAP_TYPE) {
            switch (this.ofMipmapType) {
                case 0: {
                    return s + Lang.get((String)"of.options.mipmap.nearest");
                }
                case 1: {
                    return s + Lang.get((String)"of.options.mipmap.linear");
                }
                case 2: {
                    return s + Lang.get((String)"of.options.mipmap.bilinear");
                }
                case 3: {
                    return s + Lang.get((String)"of.options.mipmap.trilinear");
                }
            }
            return s + "of.options.mipmap.nearest";
        }
        if (p_getKeyBindingOF_1_ == Options.SMOOTH_FPS) {
            return this.ofSmoothFps ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SMOOTH_WORLD) {
            return this.ofSmoothWorld ? s + Lang.getOn() : s + Lang.getOff();
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
            }
            return s + Lang.getDefault();
        }
        if (p_getKeyBindingOF_1_ == Options.TREES) {
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
                case 4: 
            }
            return s + Lang.get((String)"of.general.smart");
        }
        if (p_getKeyBindingOF_1_ == Options.DROPPED_ITEMS) {
            switch (this.ofDroppedItems) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
            }
            return s + Lang.getDefault();
        }
        if (p_getKeyBindingOF_1_ == Options.RAIN) {
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
            }
            return s + Lang.getDefault();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_WATER) {
            switch (this.ofAnimatedWater) {
                case 1: {
                    return s + Lang.get((String)"of.options.animation.dynamic");
                }
                case 2: {
                    return s + Lang.getOff();
                }
            }
            return s + Lang.getOn();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_LAVA) {
            switch (this.ofAnimatedLava) {
                case 1: {
                    return s + Lang.get((String)"of.options.animation.dynamic");
                }
                case 2: {
                    return s + Lang.getOff();
                }
            }
            return s + Lang.getOn();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_FIRE) {
            return this.ofAnimatedFire ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_PORTAL) {
            return this.ofAnimatedPortal ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_REDSTONE) {
            return this.ofAnimatedRedstone ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_EXPLOSION) {
            return this.ofAnimatedExplosion ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_FLAME) {
            return this.ofAnimatedFlame ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_SMOKE) {
            return this.ofAnimatedSmoke ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.VOID_PARTICLES) {
            return this.ofVoidParticles ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.WATER_PARTICLES) {
            return this.ofWaterParticles ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.PORTAL_PARTICLES) {
            return this.ofPortalParticles ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.POTION_PARTICLES) {
            return this.ofPotionParticles ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.FIREWORK_PARTICLES) {
            return this.ofFireworkParticles ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.DRIPPING_WATER_LAVA) {
            return this.ofDrippingWaterLava ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_TERRAIN) {
            return this.ofAnimatedTerrain ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_TEXTURES) {
            return this.ofAnimatedTextures ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.RAIN_SPLASH) {
            return this.ofRainSplash ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.LAGOMETER) {
            return this.ofLagometer ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SHOW_FPS) {
            return this.ofShowFps ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.AUTOSAVE_TICKS) {
            int l = 900;
            return this.ofAutoSaveTicks <= l ? s + Lang.get((String)"of.options.save.45s") : (this.ofAutoSaveTicks <= 2 * l ? s + Lang.get((String)"of.options.save.90s") : (this.ofAutoSaveTicks <= 4 * l ? s + Lang.get((String)"of.options.save.3min") : (this.ofAutoSaveTicks <= 8 * l ? s + Lang.get((String)"of.options.save.6min") : (this.ofAutoSaveTicks <= 16 * l ? s + Lang.get((String)"of.options.save.12min") : s + Lang.get((String)"of.options.save.24min")))));
        }
        if (p_getKeyBindingOF_1_ == Options.BETTER_GRASS) {
            switch (this.ofBetterGrass) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
            }
            return s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.CONNECTED_TEXTURES) {
            switch (this.ofConnectedTextures) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
            }
            return s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.WEATHER) {
            return this.ofWeather ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SKY) {
            return this.ofSky ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.STARS) {
            return this.ofStars ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SUN_MOON) {
            return this.ofSunMoon ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.VIGNETTE) {
            switch (this.ofVignette) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
            }
            return s + Lang.getDefault();
        }
        if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES) {
            return s + this.ofChunkUpdates;
        }
        if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES_DYNAMIC) {
            return this.ofChunkUpdatesDynamic ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.TIME) {
            return this.ofTime == 1 ? s + Lang.get((String)"of.options.time.dayOnly") : (this.ofTime == 2 ? s + Lang.get((String)"of.options.time.nightOnly") : s + Lang.getDefault());
        }
        if (p_getKeyBindingOF_1_ == Options.CLEAR_WATER) {
            return this.ofClearWater ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.AA_LEVEL) {
            String s3 = "";
            if (this.ofAaLevel != Config.getAntialiasingLevel()) {
                s3 = " (" + Lang.get((String)"of.general.restart") + ")";
            }
            return this.ofAaLevel == 0 ? s + Lang.getOff() + s3 : s + this.ofAaLevel + s3;
        }
        if (p_getKeyBindingOF_1_ == Options.AF_LEVEL) {
            return this.ofAfLevel == 1 ? s + Lang.getOff() : s + this.ofAfLevel;
        }
        if (p_getKeyBindingOF_1_ == Options.PROFILER) {
            return this.ofProfiler ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.BETTER_SNOW) {
            return this.ofBetterSnow ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SWAMP_COLORS) {
            return this.ofSwampColors ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.RANDOM_ENTITIES) {
            return this.ofRandomEntities ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SMOOTH_BIOMES) {
            return this.ofSmoothBiomes ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_FONTS) {
            return this.ofCustomFonts ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_COLORS) {
            return this.ofCustomColors ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_SKY) {
            return this.ofCustomSky ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SHOW_CAPES) {
            return this.ofShowCapes ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_ITEMS) {
            return this.ofCustomItems ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.NATURAL_TEXTURES) {
            return this.ofNaturalTextures ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.EMISSIVE_TEXTURES) {
            return this.ofEmissiveTextures ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.FAST_MATH) {
            return this.ofFastMath ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.FAST_RENDER) {
            return this.ofFastRender ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.TRANSLUCENT_BLOCKS) {
            return this.ofTranslucentBlocks == 1 ? s + Lang.getFast() : (this.ofTranslucentBlocks == 2 ? s + Lang.getFancy() : s + Lang.getDefault());
        }
        if (p_getKeyBindingOF_1_ == Options.LAZY_CHUNK_LOADING) {
            return this.ofLazyChunkLoading ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.RENDER_REGIONS) {
            return this.ofRenderRegions ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SMART_ANIMATIONS) {
            return this.ofSmartAnimations ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.DYNAMIC_FOV) {
            return this.ofDynamicFov ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ALTERNATE_BLOCKS) {
            return this.ofAlternateBlocks ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.DYNAMIC_LIGHTS) {
            int k = GameSettings.indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
            return s + GameSettings.getTranslation(KEYS_DYNAMIC_LIGHTS, k);
        }
        if (p_getKeyBindingOF_1_ == Options.SCREENSHOT_SIZE) {
            return this.ofScreenshotSize <= 1 ? s + Lang.getDefault() : s + this.ofScreenshotSize + "x";
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_ENTITY_MODELS) {
            return this.ofCustomEntityModels ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_GUIS) {
            return this.ofCustomGuis ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.SHOW_GL_ERRORS) {
            return this.ofShowGlErrors ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.FULLSCREEN_MODE) {
            return this.ofFullscreenMode.equals((Object)"Default") ? s + Lang.getDefault() : s + this.ofFullscreenMode;
        }
        if (p_getKeyBindingOF_1_ == Options.HELD_ITEM_TOOLTIPS) {
            return this.heldItemTooltips ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.ADVANCED_TOOLTIPS) {
            return this.advancedItemTooltips ? s + Lang.getOn() : s + Lang.getOff();
        }
        if (p_getKeyBindingOF_1_ == Options.FRAMERATE_LIMIT) {
            float f = this.getOptionFloatValue(p_getKeyBindingOF_1_);
            return f == 0.0f ? s + Lang.get((String)"of.options.framerateLimit.vsync") : (f == Options.access$000((Options)p_getKeyBindingOF_1_) ? s + I18n.format((String)"options.framerateLimit.max", (Object[])new Object[0]) : s + (int)f + " fps");
        }
        return null;
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
            BufferedReader bufferedreader = new BufferedReader((Reader)new InputStreamReader((InputStream)new FileInputStream(file1), "UTF-8"));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                try {
                    String[] astring = s.split(":");
                    if (astring[0].equals((Object)"ofRenderDistanceChunks") && astring.length >= 2) {
                        this.renderDistanceChunks = Integer.valueOf((String)astring[1]);
                        this.renderDistanceChunks = Config.limit((int)this.renderDistanceChunks, (int)2, (int)1024);
                    }
                    if (astring[0].equals((Object)"ofFogType") && astring.length >= 2) {
                        this.ofFogType = Integer.valueOf((String)astring[1]);
                        this.ofFogType = Config.limit((int)this.ofFogType, (int)1, (int)3);
                    }
                    if (astring[0].equals((Object)"ofFogStart") && astring.length >= 2) {
                        this.ofFogStart = Float.valueOf((String)astring[1]).floatValue();
                        if (this.ofFogStart < 0.2f) {
                            this.ofFogStart = 0.2f;
                        }
                        if (this.ofFogStart > 0.81f) {
                            this.ofFogStart = 0.8f;
                        }
                    }
                    if (astring[0].equals((Object)"ofMipmapType") && astring.length >= 2) {
                        this.ofMipmapType = Integer.valueOf((String)astring[1]);
                        this.ofMipmapType = Config.limit((int)this.ofMipmapType, (int)0, (int)3);
                    }
                    if (astring[0].equals((Object)"ofOcclusionFancy") && astring.length >= 2) {
                        this.ofOcclusionFancy = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofSmoothFps") && astring.length >= 2) {
                        this.ofSmoothFps = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofSmoothWorld") && astring.length >= 2) {
                        this.ofSmoothWorld = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAoLevel") && astring.length >= 2) {
                        this.ofAoLevel = Float.valueOf((String)astring[1]).floatValue();
                        this.ofAoLevel = Config.limit((float)this.ofAoLevel, (float)0.0f, (float)1.0f);
                    }
                    if (astring[0].equals((Object)"ofClouds") && astring.length >= 2) {
                        this.ofClouds = Integer.valueOf((String)astring[1]);
                        this.ofClouds = Config.limit((int)this.ofClouds, (int)0, (int)3);
                        this.updateRenderClouds();
                    }
                    if (astring[0].equals((Object)"ofCloudsHeight") && astring.length >= 2) {
                        this.ofCloudsHeight = Float.valueOf((String)astring[1]).floatValue();
                        this.ofCloudsHeight = Config.limit((float)this.ofCloudsHeight, (float)0.0f, (float)1.0f);
                    }
                    if (astring[0].equals((Object)"ofTrees") && astring.length >= 2) {
                        this.ofTrees = Integer.valueOf((String)astring[1]);
                        this.ofTrees = GameSettings.limit(this.ofTrees, OF_TREES_VALUES);
                    }
                    if (astring[0].equals((Object)"ofDroppedItems") && astring.length >= 2) {
                        this.ofDroppedItems = Integer.valueOf((String)astring[1]);
                        this.ofDroppedItems = Config.limit((int)this.ofDroppedItems, (int)0, (int)2);
                    }
                    if (astring[0].equals((Object)"ofRain") && astring.length >= 2) {
                        this.ofRain = Integer.valueOf((String)astring[1]);
                        this.ofRain = Config.limit((int)this.ofRain, (int)0, (int)3);
                    }
                    if (astring[0].equals((Object)"ofAnimatedWater") && astring.length >= 2) {
                        this.ofAnimatedWater = Integer.valueOf((String)astring[1]);
                        this.ofAnimatedWater = Config.limit((int)this.ofAnimatedWater, (int)0, (int)2);
                    }
                    if (astring[0].equals((Object)"ofAnimatedLava") && astring.length >= 2) {
                        this.ofAnimatedLava = Integer.valueOf((String)astring[1]);
                        this.ofAnimatedLava = Config.limit((int)this.ofAnimatedLava, (int)0, (int)2);
                    }
                    if (astring[0].equals((Object)"ofAnimatedFire") && astring.length >= 2) {
                        this.ofAnimatedFire = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAnimatedPortal") && astring.length >= 2) {
                        this.ofAnimatedPortal = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAnimatedRedstone") && astring.length >= 2) {
                        this.ofAnimatedRedstone = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAnimatedExplosion") && astring.length >= 2) {
                        this.ofAnimatedExplosion = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAnimatedFlame") && astring.length >= 2) {
                        this.ofAnimatedFlame = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAnimatedSmoke") && astring.length >= 2) {
                        this.ofAnimatedSmoke = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofVoidParticles") && astring.length >= 2) {
                        this.ofVoidParticles = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofWaterParticles") && astring.length >= 2) {
                        this.ofWaterParticles = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofPortalParticles") && astring.length >= 2) {
                        this.ofPortalParticles = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofPotionParticles") && astring.length >= 2) {
                        this.ofPotionParticles = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofFireworkParticles") && astring.length >= 2) {
                        this.ofFireworkParticles = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofDrippingWaterLava") && astring.length >= 2) {
                        this.ofDrippingWaterLava = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAnimatedTerrain") && astring.length >= 2) {
                        this.ofAnimatedTerrain = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAnimatedTextures") && astring.length >= 2) {
                        this.ofAnimatedTextures = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofRainSplash") && astring.length >= 2) {
                        this.ofRainSplash = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofLagometer") && astring.length >= 2) {
                        this.ofLagometer = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofShowFps") && astring.length >= 2) {
                        this.ofShowFps = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAutoSaveTicks") && astring.length >= 2) {
                        this.ofAutoSaveTicks = Integer.valueOf((String)astring[1]);
                        this.ofAutoSaveTicks = Config.limit((int)this.ofAutoSaveTicks, (int)40, (int)40000);
                    }
                    if (astring[0].equals((Object)"ofBetterGrass") && astring.length >= 2) {
                        this.ofBetterGrass = Integer.valueOf((String)astring[1]);
                        this.ofBetterGrass = Config.limit((int)this.ofBetterGrass, (int)1, (int)3);
                    }
                    if (astring[0].equals((Object)"ofConnectedTextures") && astring.length >= 2) {
                        this.ofConnectedTextures = Integer.valueOf((String)astring[1]);
                        this.ofConnectedTextures = Config.limit((int)this.ofConnectedTextures, (int)1, (int)3);
                    }
                    if (astring[0].equals((Object)"ofWeather") && astring.length >= 2) {
                        this.ofWeather = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofSky") && astring.length >= 2) {
                        this.ofSky = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofStars") && astring.length >= 2) {
                        this.ofStars = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofSunMoon") && astring.length >= 2) {
                        this.ofSunMoon = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofVignette") && astring.length >= 2) {
                        this.ofVignette = Integer.valueOf((String)astring[1]);
                        this.ofVignette = Config.limit((int)this.ofVignette, (int)0, (int)2);
                    }
                    if (astring[0].equals((Object)"ofChunkUpdates") && astring.length >= 2) {
                        this.ofChunkUpdates = Integer.valueOf((String)astring[1]);
                        this.ofChunkUpdates = Config.limit((int)this.ofChunkUpdates, (int)1, (int)5);
                    }
                    if (astring[0].equals((Object)"ofChunkUpdatesDynamic") && astring.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofTime") && astring.length >= 2) {
                        this.ofTime = Integer.valueOf((String)astring[1]);
                        this.ofTime = Config.limit((int)this.ofTime, (int)0, (int)2);
                    }
                    if (astring[0].equals((Object)"ofClearWater") && astring.length >= 2) {
                        this.ofClearWater = Boolean.valueOf((String)astring[1]);
                        this.updateWaterOpacity();
                    }
                    if (astring[0].equals((Object)"ofAaLevel") && astring.length >= 2) {
                        this.ofAaLevel = Integer.valueOf((String)astring[1]);
                        this.ofAaLevel = Config.limit((int)this.ofAaLevel, (int)0, (int)16);
                    }
                    if (astring[0].equals((Object)"ofAfLevel") && astring.length >= 2) {
                        this.ofAfLevel = Integer.valueOf((String)astring[1]);
                        this.ofAfLevel = Config.limit((int)this.ofAfLevel, (int)1, (int)16);
                    }
                    if (astring[0].equals((Object)"ofProfiler") && astring.length >= 2) {
                        this.ofProfiler = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofBetterSnow") && astring.length >= 2) {
                        this.ofBetterSnow = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofSwampColors") && astring.length >= 2) {
                        this.ofSwampColors = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofRandomEntities") && astring.length >= 2) {
                        this.ofRandomEntities = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofSmoothBiomes") && astring.length >= 2) {
                        this.ofSmoothBiomes = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofCustomFonts") && astring.length >= 2) {
                        this.ofCustomFonts = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofCustomColors") && astring.length >= 2) {
                        this.ofCustomColors = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofCustomItems") && astring.length >= 2) {
                        this.ofCustomItems = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofCustomSky") && astring.length >= 2) {
                        this.ofCustomSky = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofShowCapes") && astring.length >= 2) {
                        this.ofShowCapes = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofNaturalTextures") && astring.length >= 2) {
                        this.ofNaturalTextures = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofEmissiveTextures") && astring.length >= 2) {
                        this.ofEmissiveTextures = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofLazyChunkLoading") && astring.length >= 2) {
                        this.ofLazyChunkLoading = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofRenderRegions") && astring.length >= 2) {
                        this.ofRenderRegions = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofSmartAnimations") && astring.length >= 2) {
                        this.ofSmartAnimations = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofDynamicFov") && astring.length >= 2) {
                        this.ofDynamicFov = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofAlternateBlocks") && astring.length >= 2) {
                        this.ofAlternateBlocks = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofDynamicLights") && astring.length >= 2) {
                        this.ofDynamicLights = Integer.valueOf((String)astring[1]);
                        this.ofDynamicLights = GameSettings.limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
                    }
                    if (astring[0].equals((Object)"ofScreenshotSize") && astring.length >= 2) {
                        this.ofScreenshotSize = Integer.valueOf((String)astring[1]);
                        this.ofScreenshotSize = Config.limit((int)this.ofScreenshotSize, (int)1, (int)4);
                    }
                    if (astring[0].equals((Object)"ofCustomEntityModels") && astring.length >= 2) {
                        this.ofCustomEntityModels = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofCustomGuis") && astring.length >= 2) {
                        this.ofCustomGuis = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofShowGlErrors") && astring.length >= 2) {
                        this.ofShowGlErrors = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofFullscreenMode") && astring.length >= 2) {
                        this.ofFullscreenMode = astring[1];
                    }
                    if (astring[0].equals((Object)"ofFastMath") && astring.length >= 2) {
                        MathHelper.fastMath = this.ofFastMath = Boolean.valueOf((String)astring[1]).booleanValue();
                    }
                    if (astring[0].equals((Object)"ofFastRender") && astring.length >= 2) {
                        this.ofFastRender = Boolean.valueOf((String)astring[1]);
                    }
                    if (astring[0].equals((Object)"ofTranslucentBlocks") && astring.length >= 2) {
                        this.ofTranslucentBlocks = Integer.valueOf((String)astring[1]);
                        this.ofTranslucentBlocks = Config.limit((int)this.ofTranslucentBlocks, (int)0, (int)2);
                    }
                    if (!astring[0].equals((Object)("key_" + this.ofKeyBindZoom.getKeyDescription()))) continue;
                    this.ofKeyBindZoom.setKeyCode(Integer.parseInt((String)astring[1]));
                }
                catch (Exception exception) {
                    Config.dbg((String)("Skipping bad option: " + s));
                    exception.printStackTrace();
                }
            }
            KeyUtils.fixKeyConflicts((KeyBinding[])this.keyBindings, (KeyBinding[])new KeyBinding[]{this.ofKeyBindZoom});
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception exception1) {
            Config.warn((String)"Failed to load options");
            exception1.printStackTrace();
        }
    }

    public void saveOfOptions() {
        try {
            PrintWriter printwriter = new PrintWriter((Writer)new OutputStreamWriter((OutputStream)new FileOutputStream(this.optionsFileOF), "UTF-8"));
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
        catch (Exception exception) {
            Config.warn((String)"Failed to save options");
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
                this.clouds = this.fancyGraphics ? 2 : 1;
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
        Shaders.setShaderPack((String)"OFF");
        Shaders.configAntialiasingLevel = 0;
        Shaders.uninit();
        Shaders.storeConfig();
        this.updateWaterOpacity();
        this.mc.refreshResources();
        this.saveOptions();
    }

    public void updateVSync() {
        Display.setVSyncEnabled((boolean)this.enableVsync);
    }

    private void updateWaterOpacity() {
        if (Config.isIntegratedServerRunning()) {
            Config.waterOpacityChanged = true;
        }
        ClearWater.updateWaterOpacity((GameSettings)this, (World)this.mc.theWorld);
    }

    public void setAllAnimations(boolean p_setAllAnimations_1_) {
        int i;
        this.ofAnimatedWater = i = p_setAllAnimations_1_ ? 0 : 2;
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
        this.particleSetting = p_setAllAnimations_1_ ? 0 : 2;
        this.ofDrippingWaterLava = p_setAllAnimations_1_;
        this.ofAnimatedTerrain = p_setAllAnimations_1_;
        this.ofAnimatedTextures = p_setAllAnimations_1_;
    }

    private static int nextValue(int p_nextValue_0_, int[] p_nextValue_1_) {
        int i = GameSettings.indexOf(p_nextValue_0_, p_nextValue_1_);
        if (i < 0) {
            return p_nextValue_1_[0];
        }
        if (++i >= p_nextValue_1_.length) {
            i = 0;
        }
        return p_nextValue_1_[i];
    }

    private static int limit(int p_limit_0_, int[] p_limit_1_) {
        int i = GameSettings.indexOf(p_limit_0_, p_limit_1_);
        return i < 0 ? p_limit_1_[0] : p_limit_0_;
    }

    private static int indexOf(int p_indexOf_0_, int[] p_indexOf_1_) {
        for (int i = 0; i < p_indexOf_1_.length; ++i) {
            if (p_indexOf_1_[i] != p_indexOf_0_) continue;
            return i;
        }
        return -1;
    }
}
