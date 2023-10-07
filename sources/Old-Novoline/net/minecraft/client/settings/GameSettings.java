package net.minecraft.client.settings;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.world.EnumDifficulty;
import net.optifine.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import net.shadersmod.client.Shaders;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.minecraft.client.settings.GameSettings.Options.*;
import static net.minecraft.util.MathHelper.clamp_float;
import static net.minecraft.util.MathHelper.fastMath;

public class GameSettings {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final int DEFAULT = 0;
    public static final int FAST = 1;
    public static final int FANCY = 2;
    public static final int OFF = 3;
    public static final int SMART = 4;
    public static final int ANIM_ON = 0;
    public static final int ANIM_GENERATED = 1;
    public static final int ANIM_OFF = 2;
    public static final String DEFAULT_STR = "Default";
    private static final Gson gson = new Gson();
    private static final ParameterizedType typeListString = new ParameterizedType() {

        public Type[] getActualTypeArguments() {
            return new Type[]{String.class};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };
    /**
     * GUI scale values
     */
    private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
    private static final String[] AMBIENT_OCCLUSIONS = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
    private static final String[] STREAM_COMPRESSIONS = new String[]{"options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high"};
    private static final String[] STREAM_CHAT_MODES = new String[]{"options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never"};
    private static final String[] STREAM_CHAT_FILTER_MODES = new String[]{"options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods"};
    private static final String[] STREAM_MIC_MODES = new String[]{"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk"};
    private static final String[] field_181149_aW = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
    private static final String __OBFID = "CL_00000650";
    private static final int[] OF_TREES_VALUES = new int[]{0, 1, 4, 2};
    private static final int[] OF_DYNAMIC_LIGHTS = new int[]{3, 1, 2};
    private static final String[] KEYS_DYNAMIC_LIGHTS = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
    private final Set setModelParts = Sets.newHashSet(EnumPlayerModelParts.values());
    private final Map mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
    public float mouseSensitivity = 0.5F;
    public boolean invertMouse;
    public int renderDistanceChunks = -1;
    public boolean viewBobbing = true;
    public boolean anaglyph;
    public boolean fboEnable = true;
    public int limitFramerate = 120;
    /**
     * Clouds flag
     */
    public int clouds = 2;
    public boolean fancyGraphics = true;
    /**
     * Smooth Lighting
     */
    public int ambientOcclusion = 2;
    public List<String> resourcePacks = Lists.newArrayList();
    public List<String> field_183018_l = Lists.newArrayList();
    public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
    public boolean chatColours = true;
    public boolean chatLinks = true;
    public boolean chatLinksPrompt = true;
    public float chatOpacity = 1.0F;
    public boolean snooperEnabled = true;
    public boolean fullScreen;
    public boolean enableVsync = true;
    public boolean useVbo = false;
    public boolean allowBlockAlternatives = true;
    public boolean reducedDebugInfo = false;
    public boolean hideServerAddress;
    /**
     * Whether to show advanced information on item tooltips, toggled by F3+H
     */
    public boolean advancedItemTooltips;
    /**
     * Whether to pause when the game loses focus, toggled by F3+P
     */
    public boolean pauseOnLostFocus = true;
    public boolean touchscreen;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips = true;
    public float chatScale = 1.0F;
    public float chatWidth = 1.0F;
    public float chatHeightUnfocused = 0.44366196F;
    public float chatHeightFocused = 1.0F;
    public boolean showInventoryAchievementHint = true;
    public int mipmapLevels = 4;
    public float streamBytesPerPixel = 0.5F;
    public float streamMicVolume = 1.0F;
    public float streamGameVolume = 1.0F;
    public float streamKbps = 0.5412844F;
    public float streamFps = 0.31690142F;
    public int streamCompression = 1;
    public boolean streamSendMetadata = true;
    public String streamPreferredServer = "";
    public int streamChatEnabled = 0;
    public int streamChatUserFilter = 0;
    public int streamMicToggleBehavior = 0;
    public boolean field_181150_U = true;
    public boolean field_181151_V = true;
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
    public EnumDifficulty difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    /**
     * true if debug info should be displayed instead of version
     */
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public boolean field_181657_aC;
    /**
     * The lastServer string.
     */
    public String lastServer;
    /**
     * Smooth Camera Toggle
     */
    public boolean smoothCamera;
    public boolean debugCamEnable;
    public float fovSetting;
    public float gammaSetting;
    public float saturation;
    /**
     * GUI scale
     */
    public int guiScale;
    /**
     * Determines amount of particles. 0 = All, 1 = Decreased, 2 = Minimal
     */
    public int particleSetting;
    /**
     * Game settings language
     */
    public String language;
    public boolean forceUnicodeFont;
    public int ofFogType = 1;
    public float ofFogStart = 0.8F;
    public int ofMipmapType = 0;
    public boolean ofOcclusionFancy = false;
    public boolean ofSmoothFps = false;
    public boolean ofSmoothWorld = Config.isSingleProcessor();
    public boolean ofLazyChunkLoading = Config.isSingleProcessor();
    public float ofAoLevel = 1.0F;
    public int ofAaLevel = 0;
    public int ofAfLevel = 1;
    public int ofClouds = 0;
    public float ofCloudsHeight = 0.0F;
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
    public boolean ofRandomMobs = true;
    public boolean ofSmoothBiomes = true;
    public boolean ofCustomFonts = true;
    public boolean ofCustomColors = true;
    public boolean ofCustomSky = true;
    public boolean ofShowCapes = true;
    public int ofConnectedTextures = 2;
    public boolean ofCustomItems = true;
    public boolean ofNaturalTextures = false;
    public boolean ofFastMath = false;
    public boolean ofFastRender = false;
    public int ofTranslucentBlocks = 0;
    public boolean ofDynamicFov = true;
    public int ofDynamicLights = 3;
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
    public KeyBinding ofKeyBindZoom;
    protected Minecraft mc;
    private File optionsFile;
    private File optionsFileOF;

    public GameSettings(Minecraft mcIn, File p_i46326_2_) {
        this.keyBindings = ArrayUtils.addAll(new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0F;
        this.language = "en_US";
        this.forceUnicodeFont = false;
        this.mc = mcIn;
        this.optionsFile = new File(p_i46326_2_, "options.txt");
        this.optionsFileOF = new File(p_i46326_2_, "optionsof.txt");
        this.limitFramerate = (int) FRAMERATE_LIMIT.getValueMax();
        this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
        this.keyBindings = ArrayUtils.add(this.keyBindings, this.ofKeyBindZoom);
        RENDER_DISTANCE.setValueMax(32.0F);
        this.renderDistanceChunks = 8;
        this.loadOptions();
        Config.initGameSettings(this);
    }

    public GameSettings() {
        this.keyBindings = ArrayUtils.addAll(new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0F;
        this.language = "en_US";
        this.forceUnicodeFont = false;
    }

    /**
     * Represents a key or mouse button as a string. Args: key
     */
    public static String getKeyDisplayString(int p_74298_0_) {
        return p_74298_0_ < 0 ? I18n.format("key.mouseButton", p_74298_0_ + 101) : p_74298_0_ < 256 ? Keyboard.getKeyName(p_74298_0_) : String.format("%c", (char) (p_74298_0_ - 256)).toUpperCase();
    }

    /**
     * Returns whether the specified key binding is currently being pressed.
     */
    public static boolean isKeyDown(KeyBinding p_100015_0_) {
        final int i = p_100015_0_.getKeyCode();
        return i >= -100 && i <= 255 && p_100015_0_.getKeyCode() != 0 && (p_100015_0_.getKeyCode() < 0 ? Mouse.isButtonDown(p_100015_0_.getKeyCode() + 100) : Keyboard.isKeyDown(p_100015_0_.getKeyCode()));
    }

    /**
     * Returns the translation of the given index in the given String array. If the index is smaller than 0 or greater
     * than/equal to the length of the String array, it is changed to 0.
     */
    private static String getTranslation(String[] p_74299_0_, int p_74299_1_) {
        if (p_74299_1_ < 0 || p_74299_1_ >= p_74299_0_.length) {
            p_74299_1_ = 0;
        }

        return I18n.format(p_74299_0_[p_74299_1_]);
    }

    private static int nextValue(int p_nextValue_0_, int[] p_nextValue_1_) {
        int i = indexOf(p_nextValue_0_, p_nextValue_1_);

        if (i < 0) {
            return p_nextValue_1_[0];
        } else {
            ++i;

            if (i >= p_nextValue_1_.length) {
                i = 0;
            }

            return p_nextValue_1_[i];
        }
    }

    private static int limit(int p_limit_0_, int[] p_limit_1_) {
        final int i = indexOf(p_limit_0_, p_limit_1_);
        return i < 0 ? p_limit_1_[0] : p_limit_0_;
    }

    private static int indexOf(int p_indexOf_0_, int[] p_indexOf_1_) {
        for (int i = 0; i < p_indexOf_1_.length; ++i) {
            if (p_indexOf_1_[i] == p_indexOf_0_) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Sets a key binding and then saves all settings.
     */
    public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_) {
        p_151440_1_.setKeyCode(p_151440_2_);
        this.saveOptions();
    }

    /**
     * If the specified option is controlled by a slider (float value), this will set the float value.
     */
    public void setOptionFloatValue(Options p_74304_1_, float p_74304_2_) {
        this.setOptionFloatValueOF(p_74304_1_, p_74304_2_);

        if (p_74304_1_ == SENSITIVITY) {
            this.mouseSensitivity = p_74304_2_;
        }

        if (p_74304_1_ == FOV) {
            this.fovSetting = p_74304_2_;
        }

        if (p_74304_1_ == GAMMA) {
            this.gammaSetting = p_74304_2_;
        }

        if (p_74304_1_ == FRAMERATE_LIMIT) {
            this.limitFramerate = (int) p_74304_2_;
            this.enableVsync = false;

            if (this.limitFramerate <= 0) {
                this.limitFramerate = (int) FRAMERATE_LIMIT.getValueMax();
                this.enableVsync = true;
            }

            this.updateVSync();
        }

        if (p_74304_1_ == CHAT_OPACITY) {
            this.chatOpacity = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == CHAT_HEIGHT_FOCUSED) {
            this.chatHeightFocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == CHAT_HEIGHT_UNFOCUSED) {
            this.chatHeightUnfocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == CHAT_WIDTH) {
            this.chatWidth = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == CHAT_SCALE) {
            this.chatScale = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == MIPMAP_LEVELS) {
            final int i = this.mipmapLevels;
            this.mipmapLevels = (int) p_74304_2_;

            if ((float) i != p_74304_2_) {
                this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
                this.mc.scheduleResourcesRefresh();
            }
        }

        if (p_74304_1_ == BLOCK_ALTERNATIVES) {
            this.allowBlockAlternatives = !this.allowBlockAlternatives;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_74304_1_ == RENDER_DISTANCE) {
            this.renderDistanceChunks = (int) p_74304_2_;
            this.mc.renderGlobal.setDisplayListEntitiesDirty();
        }

        if (p_74304_1_ == STREAM_BYTES_PER_PIXEL) {
            this.streamBytesPerPixel = p_74304_2_;
        }

        if (p_74304_1_ == STREAM_VOLUME_MIC) {
            this.streamMicVolume = p_74304_2_;
            this.mc.getTwitchStream().updateStreamVolume();
        }

        if (p_74304_1_ == STREAM_VOLUME_SYSTEM) {
            this.streamGameVolume = p_74304_2_;
            this.mc.getTwitchStream().updateStreamVolume();
        }

        if (p_74304_1_ == STREAM_KBPS) {
            this.streamKbps = p_74304_2_;
        }

        if (p_74304_1_ == STREAM_FPS) {
            this.streamFps = p_74304_2_;
        }
    }

    /**
     * For non-float options. Toggles the option on/off, or cycles through the list i.e. render distances.
     */
    public void setOptionValue(Options p_74306_1_, int p_74306_2_) {
        this.setOptionValueOF(p_74306_1_, p_74306_2_);

        if (p_74306_1_ == INVERT_MOUSE) {
            this.invertMouse = !this.invertMouse;
        }

        if (p_74306_1_ == GUI_SCALE) {
            this.guiScale = this.guiScale + p_74306_2_ & 3;
        }

        if (p_74306_1_ == Options.PARTICLES) {
            this.particleSetting = (this.particleSetting + p_74306_2_) % 3;
        }

        if (p_74306_1_ == VIEW_BOBBING) {
            this.viewBobbing = !this.viewBobbing;
        }

        if (p_74306_1_ == RENDER_CLOUDS) {
            this.clouds = (this.clouds + p_74306_2_) % 3;
        }

        if (p_74306_1_ == FORCE_UNICODE_FONT) {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            this.mc.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }

        if (p_74306_1_ == FBO_ENABLE) {
            this.fboEnable = !this.fboEnable;
        }

        if (p_74306_1_ == ANAGLYPH) {
            if (!this.anaglyph && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
                return;
            }

            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
        }

        if (p_74306_1_ == GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.updateRenderClouds();
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_74306_1_ == AMBIENT_OCCLUSION) {
            this.ambientOcclusion = (this.ambientOcclusion + p_74306_2_) % 3;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_74306_1_ == CHAT_VISIBILITY) {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + p_74306_2_) % 3);
        }

        if (p_74306_1_ == STREAM_COMPRESSION) {
            this.streamCompression = (this.streamCompression + p_74306_2_) % 3;
        }

        if (p_74306_1_ == STREAM_SEND_METADATA) {
            this.streamSendMetadata = !this.streamSendMetadata;
        }

        if (p_74306_1_ == STREAM_CHAT_ENABLED) {
            this.streamChatEnabled = (this.streamChatEnabled + p_74306_2_) % 3;
        }

        if (p_74306_1_ == STREAM_CHAT_USER_FILTER) {
            this.streamChatUserFilter = (this.streamChatUserFilter + p_74306_2_) % 3;
        }

        if (p_74306_1_ == STREAM_MIC_TOGGLE_BEHAVIOR) {
            this.streamMicToggleBehavior = (this.streamMicToggleBehavior + p_74306_2_) % 2;
        }

        if (p_74306_1_ == CHAT_COLOR) {
            this.chatColours = !this.chatColours;
        }

        if (p_74306_1_ == CHAT_LINKS) {
            this.chatLinks = !this.chatLinks;
        }

        if (p_74306_1_ == CHAT_LINKS_PROMPT) {
            this.chatLinksPrompt = !this.chatLinksPrompt;
        }

        if (p_74306_1_ == SNOOPER_ENABLED) {
            this.snooperEnabled = !this.snooperEnabled;
        }

        if (p_74306_1_ == TOUCHSCREEN) {
            this.touchscreen = !this.touchscreen;
        }

        if (p_74306_1_ == USE_FULLSCREEN) {
            this.fullScreen = !this.fullScreen;

            if (this.mc.isFullScreen() != this.fullScreen) {
                this.mc.toggleFullscreen();
            }
        }

        if (p_74306_1_ == ENABLE_VSYNC) {
            this.enableVsync = !this.enableVsync;
            Display.setVSyncEnabled(this.enableVsync);
        }

        if (p_74306_1_ == USE_VBO) {
            this.useVbo = !this.useVbo;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_74306_1_ == BLOCK_ALTERNATIVES) {
            this.allowBlockAlternatives = !this.allowBlockAlternatives;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_74306_1_ == REDUCED_DEBUG_INFO) {
            this.reducedDebugInfo = !this.reducedDebugInfo;
        }

        if (p_74306_1_ == ENTITY_SHADOWS) {
            this.field_181151_V = !this.field_181151_V;
        }

        this.saveOptions();
    }

    public float getOptionFloatValue(Options p_74296_1_) {
        return p_74296_1_ == CLOUD_HEIGHT ? this.ofCloudsHeight : p_74296_1_ == AO_LEVEL ? this.ofAoLevel : p_74296_1_ == AA_LEVEL ? (float) this.ofAaLevel : p_74296_1_ == AF_LEVEL ? (float) this.ofAfLevel : p_74296_1_ == MIPMAP_TYPE ? (float) this.ofMipmapType : p_74296_1_ == FRAMERATE_LIMIT ? (float) this.limitFramerate == FRAMERATE_LIMIT.getValueMax() && this.enableVsync ? 0.0F : (float) this.limitFramerate : p_74296_1_ == FOV ? this.fovSetting : p_74296_1_ == GAMMA ? this.gammaSetting : p_74296_1_ == SATURATION ? this.saturation : p_74296_1_ == SENSITIVITY ? this.mouseSensitivity : p_74296_1_ == CHAT_OPACITY ? this.chatOpacity : p_74296_1_ == CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : p_74296_1_ == CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : p_74296_1_ == CHAT_SCALE ? this.chatScale : p_74296_1_ == CHAT_WIDTH ? this.chatWidth : p_74296_1_ == FRAMERATE_LIMIT ? (float) this.limitFramerate : p_74296_1_ == MIPMAP_LEVELS ? (float) this.mipmapLevels : p_74296_1_ == RENDER_DISTANCE ? (float) this.renderDistanceChunks : p_74296_1_ == STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : p_74296_1_ == STREAM_VOLUME_MIC ? this.streamMicVolume : p_74296_1_ == STREAM_VOLUME_SYSTEM ? this.streamGameVolume : p_74296_1_ == STREAM_KBPS ? this.streamKbps : p_74296_1_ == STREAM_FPS ? this.streamFps : 0.0F;
    }

    public boolean getOptionOrdinalValue(Options p_74308_1_) {
        switch (GameSettings$2.field_151477_a[p_74308_1_.ordinal()]) {
            case 1:
                return this.invertMouse;

            case 2:
                return this.viewBobbing;

            case 3:
                return this.anaglyph;

            case 4:
                return this.fboEnable;

            case 5:
                return this.chatColours;

            case 6:
                return this.chatLinks;

            case 7:
                return this.chatLinksPrompt;

            case 8:
                return this.snooperEnabled;

            case 9:
                return this.fullScreen;

            case 10:
                return this.enableVsync;

            case 11:
                return this.useVbo;

            case 12:
                return this.touchscreen;

            case 13:
                return this.streamSendMetadata;

            case 14:
                return this.forceUnicodeFont;

            case 15:
                return this.allowBlockAlternatives;

            case 16:
                return this.reducedDebugInfo;

            case 17:
                return this.field_181151_V;

            default:
                return false;
        }
    }

    /**
     * Gets a key binding.
     */
    public String getKeyBinding(Options p_74297_1_) {
        final String s = this.getKeyBindingOF(p_74297_1_);

        if (s != null) {
            return s;
        } else {
            final String s1 = I18n.format(p_74297_1_.getEnumString()) + ": ";

            if (p_74297_1_.getEnumFloat()) {
                final float f1 = this.getOptionFloatValue(p_74297_1_);
                final float f = p_74297_1_.normalizeValue(f1);
                return p_74297_1_ == SENSITIVITY ? f == 0.0F ? s1 + I18n.format("options.sensitivity.min") : f == 1.0F ? s1 + I18n.format("options.sensitivity.max") : s1 + (int) (f * 200.0F) + "%" : p_74297_1_ == FOV ? f1 == 70.0F ? s1 + I18n.format("options.fov.min") : f1 == 110.0F ? s1 + I18n.format("options.fov.max") : s1 + (int) f1 : p_74297_1_ == FRAMERATE_LIMIT ? f1 == p_74297_1_.valueMax ? s1 + I18n.format("options.framerateLimit.max") : s1 + (int) f1 + " fps" : p_74297_1_ == RENDER_CLOUDS ? f1 == p_74297_1_.valueMin ? s1 + I18n.format("options.cloudHeight.min") : s1 + ((int) f1 + 128) : p_74297_1_ == GAMMA ? f == 0.0F ? s1 + I18n.format("options.gamma.min") : f == 1.0F ? s1 + I18n.format("options.gamma.max") : s1 + "+" + (int) (f * 100.0F) + "%" : p_74297_1_ == SATURATION ? s1 + (int) (f * 400.0F) + "%" : p_74297_1_ == CHAT_OPACITY ? s1 + (int) (f * 90.0F + 10.0F) + "%" : p_74297_1_ == CHAT_HEIGHT_UNFOCUSED ? s1 + GuiNewChat.calculateChatBoxHeight(f) + "px" : p_74297_1_ == CHAT_HEIGHT_FOCUSED ? s1 + GuiNewChat.calculateChatBoxHeight(f) + "px" : p_74297_1_ == CHAT_WIDTH ? s1 + GuiNewChat.calculateChatBoxWidth(f) + "px" : p_74297_1_ == RENDER_DISTANCE ? s1 + (int) f1 + " chunks" : p_74297_1_ == MIPMAP_LEVELS ? f1 == 0.0F ? s1 + I18n.format("options.off") : s1 + (int) f1 : p_74297_1_ == STREAM_FPS ? s1 + TwitchStream.formatStreamFps(f) + " fps" : p_74297_1_ == STREAM_KBPS ? s1 + TwitchStream.formatStreamKbps(f) + " Kbps" : p_74297_1_ == STREAM_BYTES_PER_PIXEL ? s1 + String.format("%.3f bpp", TwitchStream.formatStreamBps(f)) : f == 0.0F ? s1 + I18n.format("options.off") : s1 + (int) (f * 100.0F) + "%";
            } else if (p_74297_1_.getEnumBoolean()) {
                final boolean flag = this.getOptionOrdinalValue(p_74297_1_);
                return flag ? s1 + I18n.format("options.on") : s1 + I18n.format("options.off");
            } else if (p_74297_1_ == GUI_SCALE) {
                return s1 + getTranslation(GUISCALES, this.guiScale);
            } else if (p_74297_1_ == CHAT_VISIBILITY) {
                return s1 + I18n.format(this.chatVisibility.getResourceKey());
            } else if (p_74297_1_ == Options.PARTICLES) {
                return s1 + getTranslation(PARTICLES, this.particleSetting);
            } else if (p_74297_1_ == AMBIENT_OCCLUSION) {
                return s1 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
            } else if (p_74297_1_ == STREAM_COMPRESSION) {
                return s1 + getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
            } else if (p_74297_1_ == STREAM_CHAT_ENABLED) {
                return s1 + getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
            } else if (p_74297_1_ == STREAM_CHAT_USER_FILTER) {
                return s1 + getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
            } else if (p_74297_1_ == STREAM_MIC_TOGGLE_BEHAVIOR) {
                return s1 + getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
            } else if (p_74297_1_ == RENDER_CLOUDS) {
                return s1 + getTranslation(field_181149_aW, this.clouds);
            } else if (p_74297_1_ == GRAPHICS) {
                if (this.fancyGraphics) {
                    return s1 + I18n.format("options.graphics.fancy");
                } else {
                    final String s2 = "options.graphics.fast";
                    return s1 + I18n.format("options.graphics.fast");
                }
            } else {
                return s1;
            }
        }
    }

    /**
     * Loads the options from the options file. It appears that this has replaced the previous 'loadOptions'
     */
    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }

            final BufferedReader bufferedreader = new BufferedReader(new FileReader(this.optionsFile));
            String s = "";
            this.mapSoundLevels.clear();

            while ((s = bufferedreader.readLine()) != null) {
                try {
                    final String[] astring = s.split(":");

                    if (astring[0].equals("mouseSensitivity")) {
                        this.mouseSensitivity = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("fov")) {
                        this.fovSetting = this.parseFloat(astring[1]) * 40.0F + 70.0F;
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
                        this.enableVsync = false;

                        if (this.limitFramerate <= 0) {
                            this.limitFramerate = (int) FRAMERATE_LIMIT.getValueMax();
                            this.enableVsync = true;
                        }

                        this.updateVSync();
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
                        } else if (astring[1].equals("false")) {
                            this.ambientOcclusion = 0;
                        } else {
                            this.ambientOcclusion = Integer.parseInt(astring[1]);
                        }
                    }

                    if (astring[0].equals("renderClouds")) {
                        switch (astring[1]) {
                            case "true":
                                this.clouds = 2;
                                break;
                            case "false":
                                this.clouds = 0;
                                break;
                            case "fast":
                                this.clouds = 1;
                                break;
                        }
                    }

                    if (astring[0].equals("resourcePacks")) {
                        this.resourcePacks = gson.fromJson(s.substring(s.indexOf(58) + 1), typeListString);

                        if (this.resourcePacks == null) {
                            this.resourcePacks = Lists.newArrayList();
                        }
                    }

                    if (astring[0].equals("incompatibleResourcePacks")) {
                        this.field_183018_l = gson.fromJson(s.substring(s.indexOf(58) + 1), typeListString);

                        if (this.field_183018_l == null) {
                            this.field_183018_l = Lists.newArrayList();
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
                        this.field_181150_U = astring[1].equals("true");
                    }

                    if (astring[0].equals("entityShadows")) {
                        this.field_181151_V = astring[1].equals("true");
                    }

                    for (KeyBinding keybinding : this.keyBindings) {
                        if (astring[0].equals("key_" + keybinding.getKeyDescription())) {
                            keybinding.setKeyCode(Integer.parseInt(astring[1]));
                        }
                    }

                    for (SoundCategory soundcategory : SoundCategory.values()) {
                        if (astring[0].equals("soundCategory_" + soundcategory.getCategoryName())) {
                            this.mapSoundLevels.put(soundcategory, this.parseFloat(astring[1]));
                        }
                    }

                    for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                        if (astring[0].equals("modelPart_" + enumplayermodelparts.getPartName())) {
                            this.setModelPartEnabled(enumplayermodelparts, astring[1].equals("true"));
                        }
                    }
                } catch (Exception exception) {
                    LOGGER.warn("Skipping bad option: " + s);
                    exception.printStackTrace();
                }
            }

            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        } catch (Exception exception1) {
            LOGGER.error("Failed to load options", exception1);
        }

        this.loadOfOptions();
    }

    /**
     * Parses a string into a float.
     */
    private float parseFloat(String p_74305_1_) {
        return p_74305_1_.equals("true") ? 1.0F : p_74305_1_.equals("false") ? 0.0F : Float.parseFloat(p_74305_1_);
    }

    /**
     * Saves the options to the options file.
     */
    public void saveOptions() {
        if (Reflector.FMLClientHandler.exists()) {
            final Object object = Reflector.call(Reflector.FMLClientHandler_instance);

            if (object != null && Reflector.callBoolean(object, Reflector.FMLClientHandler_isLoading)) {
                return;
            }
        }

        try {
            final PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
            printwriter.println("invertYMouse:" + this.invertMouse);
            printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
            printwriter.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
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
                case 0:
                    printwriter.println("renderClouds:false");
                    break;

                case 1:
                    printwriter.println("renderClouds:fast");
                    break;

                case 2:
                    printwriter.println("renderClouds:true");
            }

            printwriter.println("resourcePacks:" + gson.toJson(this.resourcePacks));
            printwriter.println("incompatibleResourcePacks:" + gson.toJson(this.field_183018_l));
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
            printwriter.println("useNativeTransport:" + this.field_181150_U);
            printwriter.println("entityShadows:" + this.field_181151_V);

            for (KeyBinding keybinding : this.keyBindings) {
                printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
            }

            for (SoundCategory soundcategory : SoundCategory.values()) {
                printwriter.println("soundCategory_" + soundcategory.getCategoryName() + ":" + this.getSoundLevel(soundcategory));
            }

            for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                printwriter.println("modelPart_" + enumplayermodelparts.getPartName() + ":" + this.setModelParts.contains(enumplayermodelparts));
            }

            printwriter.close();
        } catch (Exception exception) {
            LOGGER.error("Failed to save options", exception);
        }

        this.saveOfOptions();
        this.sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory p_151438_1_) {
        return this.mapSoundLevels.containsKey(p_151438_1_) ? (Float) this.mapSoundLevels.get(p_151438_1_) : 1.0F;
    }

    public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_) {
        this.mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
        this.mapSoundLevels.put(p_151439_1_, p_151439_2_);
    }

    /**
     * Send a client info packet with settings information to the server
     */
    public void sendSettingsToServer() {
        if (this.mc.player != null) {
            int i = 0;

            for (Object enumplayermodelparts : this.setModelParts) {
                i |= ((EnumPlayerModelParts) enumplayermodelparts).getPartMask();
            }

            this.mc.player.connection.sendPacket(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i));
        }
    }

    public Set getModelParts() {
        return ImmutableSet.copyOf(this.setModelParts);
    }

    public void setModelPartEnabled(EnumPlayerModelParts p_178878_1_, boolean p_178878_2_) {
        if (p_178878_2_) {
            this.setModelParts.add(p_178878_1_);
        } else {
            this.setModelParts.remove(p_178878_1_);
        }

        this.sendSettingsToServer();
    }

    public void switchModelPartEnabled(EnumPlayerModelParts p_178877_1_) {
        if (!this.getModelParts().contains(p_178877_1_)) {
            this.setModelParts.add(p_178877_1_);
        } else {
            this.setModelParts.remove(p_178877_1_);
        }

        this.sendSettingsToServer();
    }

    public int func_181147_e() {
        return this.renderDistanceChunks >= 4 ? this.clouds : 0;
    }

    public boolean func_181148_f() {
        return this.field_181150_U;
    }

    private void setOptionFloatValueOF(Options p_setOptionFloatValueOF_1_, float p_setOptionFloatValueOF_2_) {
        if (p_setOptionFloatValueOF_1_ == CLOUD_HEIGHT) {
            this.ofCloudsHeight = p_setOptionFloatValueOF_2_;
            this.mc.renderGlobal.resetClouds();
        }

        if (p_setOptionFloatValueOF_1_ == AO_LEVEL) {
            this.ofAoLevel = p_setOptionFloatValueOF_2_;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionFloatValueOF_1_ == AA_LEVEL) {
            final int i = (int) p_setOptionFloatValueOF_2_;

            if (i > 0 && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
                return;
            }

            final int[] aint = new int[]{0, 2, 4, 6, 8, 12, 16};
            this.ofAaLevel = 0;

            for (int value : aint) {
                if (i >= value) {
                    this.ofAaLevel = value;
                }
            }

            this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
        }

        if (p_setOptionFloatValueOF_1_ == AF_LEVEL) {
            final int k = (int) p_setOptionFloatValueOF_2_;

            if (k > 1 && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
                return;
            }

            for (this.ofAfLevel = 1; this.ofAfLevel * 2 <= k; this.ofAfLevel *= 2) {
            }

            this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
            this.mc.refreshResources();
        }

        if (p_setOptionFloatValueOF_1_ == MIPMAP_TYPE) {
            final int l = (int) p_setOptionFloatValueOF_2_;
            this.ofMipmapType = Config.limit(l, 0, 3);
            this.mc.refreshResources();
        }
    }

    private void setOptionValueOF(Options p_setOptionValueOF_1_, int p_setOptionValueOF_2_) {
        if (p_setOptionValueOF_1_ == FOG_FANCY) {
            switch (this.ofFogType) {
                case 1:
                    this.ofFogType = 2;

                    if (!Config.isFancyFogAvailable()) {
                        this.ofFogType = 3;
                    }

                    break;

                case 2:
                    this.ofFogType = 3;
                    break;

                default:
                    this.ofFogType = 1;
            }
        }

        if (p_setOptionValueOF_1_ == FOG_START) {
            this.ofFogStart += 0.2F;

            if (this.ofFogStart > 0.81F) {
                this.ofFogStart = 0.2F;
            }
        }

        if (p_setOptionValueOF_1_ == SMOOTH_FPS) {
            this.ofSmoothFps = !this.ofSmoothFps;
        }

        if (p_setOptionValueOF_1_ == SMOOTH_WORLD) {
            this.ofSmoothWorld = !this.ofSmoothWorld;
            Config.updateThreadPriorities();
        }

        if (p_setOptionValueOF_1_ == CLOUDS) {
            ++this.ofClouds;

            if (this.ofClouds > 3) {
                this.ofClouds = 0;
            }

            this.updateRenderClouds();
            this.mc.renderGlobal.resetClouds();
        }

        if (p_setOptionValueOF_1_ == TREES) {
            this.ofTrees = nextValue(this.ofTrees, OF_TREES_VALUES);
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == DROPPED_ITEMS) {
            ++this.ofDroppedItems;

            if (this.ofDroppedItems > 2) {
                this.ofDroppedItems = 0;
            }
        }

        if (p_setOptionValueOF_1_ == RAIN) {
            ++this.ofRain;

            if (this.ofRain > 3) {
                this.ofRain = 0;
            }
        }

        if (p_setOptionValueOF_1_ == ANIMATED_WATER) {
            ++this.ofAnimatedWater;

            if (this.ofAnimatedWater == 1) {
                ++this.ofAnimatedWater;
            }

            if (this.ofAnimatedWater > 2) {
                this.ofAnimatedWater = 0;
            }
        }

        if (p_setOptionValueOF_1_ == ANIMATED_LAVA) {
            ++this.ofAnimatedLava;

            if (this.ofAnimatedLava == 1) {
                ++this.ofAnimatedLava;
            }

            if (this.ofAnimatedLava > 2) {
                this.ofAnimatedLava = 0;
            }
        }

        if (p_setOptionValueOF_1_ == ANIMATED_FIRE) {
            this.ofAnimatedFire = !this.ofAnimatedFire;
        }

        if (p_setOptionValueOF_1_ == ANIMATED_PORTAL) {
            this.ofAnimatedPortal = !this.ofAnimatedPortal;
        }

        if (p_setOptionValueOF_1_ == ANIMATED_REDSTONE) {
            this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
        }

        if (p_setOptionValueOF_1_ == ANIMATED_EXPLOSION) {
            this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
        }

        if (p_setOptionValueOF_1_ == ANIMATED_FLAME) {
            this.ofAnimatedFlame = !this.ofAnimatedFlame;
        }

        if (p_setOptionValueOF_1_ == ANIMATED_SMOKE) {
            this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
        }

        if (p_setOptionValueOF_1_ == VOID_PARTICLES) {
            this.ofVoidParticles = !this.ofVoidParticles;
        }

        if (p_setOptionValueOF_1_ == WATER_PARTICLES) {
            this.ofWaterParticles = !this.ofWaterParticles;
        }

        if (p_setOptionValueOF_1_ == PORTAL_PARTICLES) {
            this.ofPortalParticles = !this.ofPortalParticles;
        }

        if (p_setOptionValueOF_1_ == POTION_PARTICLES) {
            this.ofPotionParticles = !this.ofPotionParticles;
        }

        if (p_setOptionValueOF_1_ == FIREWORK_PARTICLES) {
            this.ofFireworkParticles = !this.ofFireworkParticles;
        }

        if (p_setOptionValueOF_1_ == DRIPPING_WATER_LAVA) {
            this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
        }

        if (p_setOptionValueOF_1_ == ANIMATED_TERRAIN) {
            this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
        }

        if (p_setOptionValueOF_1_ == ANIMATED_TEXTURES) {
            this.ofAnimatedTextures = !this.ofAnimatedTextures;
        }

        if (p_setOptionValueOF_1_ == RAIN_SPLASH) {
            this.ofRainSplash = !this.ofRainSplash;
        }

        if (p_setOptionValueOF_1_ == LAGOMETER) {
            this.ofLagometer = !this.ofLagometer;
        }

        if (p_setOptionValueOF_1_ == SHOW_FPS) {
            this.ofShowFps = !this.ofShowFps;
        }

        if (p_setOptionValueOF_1_ == AUTOSAVE_TICKS) {
            this.ofAutoSaveTicks *= 10;

            if (this.ofAutoSaveTicks > 40000) {
                this.ofAutoSaveTicks = 40;
            }
        }

        if (p_setOptionValueOF_1_ == BETTER_GRASS) {
            ++this.ofBetterGrass;

            if (this.ofBetterGrass > 3) {
                this.ofBetterGrass = 1;
            }

            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == CONNECTED_TEXTURES) {
            ++this.ofConnectedTextures;

            if (this.ofConnectedTextures > 3) {
                this.ofConnectedTextures = 1;
            }

            if (this.ofConnectedTextures != 2) {
                this.mc.refreshResources();
            }
        }

        if (p_setOptionValueOF_1_ == WEATHER) {
            this.ofWeather = !this.ofWeather;
        }

        if (p_setOptionValueOF_1_ == SKY) {
            this.ofSky = !this.ofSky;
        }

        if (p_setOptionValueOF_1_ == STARS) {
            this.ofStars = !this.ofStars;
        }

        if (p_setOptionValueOF_1_ == SUN_MOON) {
            this.ofSunMoon = !this.ofSunMoon;
        }

        if (p_setOptionValueOF_1_ == VIGNETTE) {
            ++this.ofVignette;

            if (this.ofVignette > 2) {
                this.ofVignette = 0;
            }
        }

        if (p_setOptionValueOF_1_ == CHUNK_UPDATES) {
            ++this.ofChunkUpdates;

            if (this.ofChunkUpdates > 5) {
                this.ofChunkUpdates = 1;
            }
        }

        if (p_setOptionValueOF_1_ == CHUNK_UPDATES_DYNAMIC) {
            this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
        }

        if (p_setOptionValueOF_1_ == TIME) {
            ++this.ofTime;

            if (this.ofTime > 2) {
                this.ofTime = 0;
            }
        }

        if (p_setOptionValueOF_1_ == CLEAR_WATER) {
            this.ofClearWater = !this.ofClearWater;
            this.updateWaterOpacity();
        }

        if (p_setOptionValueOF_1_ == PROFILER) {
            this.ofProfiler = !this.ofProfiler;
        }

        if (p_setOptionValueOF_1_ == BETTER_SNOW) {
            this.ofBetterSnow = !this.ofBetterSnow;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == SWAMP_COLORS) {
            this.ofSwampColors = !this.ofSwampColors;
            CustomColors.updateUseDefaultGrassFoliageColors();
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == RANDOM_MOBS) {
            this.ofRandomMobs = !this.ofRandomMobs;
            RandomMobs.resetTextures();
        }

        if (p_setOptionValueOF_1_ == SMOOTH_BIOMES) {
            this.ofSmoothBiomes = !this.ofSmoothBiomes;
            CustomColors.updateUseDefaultGrassFoliageColors();
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == CUSTOM_FONTS) {
            this.ofCustomFonts = !this.ofCustomFonts;
            this.mc.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
            this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
        }

        if (p_setOptionValueOF_1_ == CUSTOM_COLORS) {
            this.ofCustomColors = !this.ofCustomColors;
            CustomColors.update();
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == CUSTOM_ITEMS) {
            this.ofCustomItems = !this.ofCustomItems;
            this.mc.refreshResources();
        }

        if (p_setOptionValueOF_1_ == CUSTOM_SKY) {
            this.ofCustomSky = !this.ofCustomSky;
            CustomSky.update();
        }

        if (p_setOptionValueOF_1_ == SHOW_CAPES) {
            this.ofShowCapes = !this.ofShowCapes;
        }

        if (p_setOptionValueOF_1_ == NATURAL_TEXTURES) {
            this.ofNaturalTextures = !this.ofNaturalTextures;
            NaturalTextures.update();
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == FAST_MATH) {
            this.ofFastMath = !this.ofFastMath;
            fastMath = this.ofFastMath;
        }

        if (p_setOptionValueOF_1_ == TRANSLUCENT_BLOCKS) {
            if (this.ofTranslucentBlocks == 0) {
                this.ofTranslucentBlocks = 1;
            } else if (this.ofTranslucentBlocks == 1) {
                this.ofTranslucentBlocks = 2;
            } else if (this.ofTranslucentBlocks == 2) {
                this.ofTranslucentBlocks = 0;
            } else {
                this.ofTranslucentBlocks = 0;
            }

            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == LAZY_CHUNK_LOADING) {
            this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
            Config.updateAvailableProcessors();

            if (!Config.isSingleProcessor()) {
                this.ofLazyChunkLoading = false;
            }

            this.mc.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == FULLSCREEN_MODE) {
            final List list = Arrays.asList(Config.getDisplayModeNames());

            if (this.ofFullscreenMode.equals("Default")) {
                this.ofFullscreenMode = (String) list.get(0);
            } else {
                int i = list.indexOf(this.ofFullscreenMode);

                if (i < 0) {
                    this.ofFullscreenMode = "Default";
                } else {
                    ++i;

                    if (i >= list.size()) {
                        this.ofFullscreenMode = "Default";
                    } else {
                        this.ofFullscreenMode = (String) list.get(i);
                    }
                }
            }
        }

        if (p_setOptionValueOF_1_ == DYNAMIC_FOV) {
            this.ofDynamicFov = !this.ofDynamicFov;
        }

        if (p_setOptionValueOF_1_ == DYNAMIC_LIGHTS) {
            this.ofDynamicLights = nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
            DynamicLights.removeLights(this.mc.renderGlobal);
        }

        if (p_setOptionValueOF_1_ == HELD_ITEM_TOOLTIPS) {
            this.heldItemTooltips = !this.heldItemTooltips;
        }
    }

    private String getKeyBindingOF(Options p_getKeyBindingOF_1_) {
        String s = I18n.format(p_getKeyBindingOF_1_.getEnumString()) + ": ";

        if (s == null) {
            s = p_getKeyBindingOF_1_.getEnumString();
        }

        if (p_getKeyBindingOF_1_ == RENDER_DISTANCE) {
            final int l = (int) this.getOptionFloatValue(p_getKeyBindingOF_1_);
            String s1 = I18n.format("options.renderDistance.tiny");
            int i = 2;

            if (l >= 4) {
                s1 = I18n.format("options.renderDistance.short");
                i = 4;
            }

            if (l >= 8) {
                s1 = I18n.format("options.renderDistance.normal");
                i = 8;
            }

            if (l >= 16) {
                s1 = I18n.format("options.renderDistance.far");
                i = 16;
            }

            if (l >= 32) {
                s1 = Lang.get("of.options.renderDistance.extreme");
                i = 32;
            }

            final int j = this.renderDistanceChunks - i;
            String s2 = s1;

            if (j > 0) {
                s2 = s1 + "+";
            }

            return s + l + " " + s2 + "";
        } else if (p_getKeyBindingOF_1_ == FOG_FANCY) {
            switch (this.ofFogType) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getOff();
            }
        } else if (p_getKeyBindingOF_1_ == FOG_START) {
            return s + this.ofFogStart;
        } else if (p_getKeyBindingOF_1_ == MIPMAP_TYPE) {
            switch (this.ofMipmapType) {
                case 0:
                    return s + Lang.get("of.options.mipmap.nearest");

                case 1:
                    return s + Lang.get("of.options.mipmap.linear");

                case 2:
                    return s + Lang.get("of.options.mipmap.bilinear");

                case 3:
                    return s + Lang.get("of.options.mipmap.trilinear");

                default:
                    return s + "of.options.mipmap.nearest";
            }
        } else if (p_getKeyBindingOF_1_ == SMOOTH_FPS) {
            return this.ofSmoothFps ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == SMOOTH_WORLD) {
            return this.ofSmoothWorld ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == CLOUDS) {
            switch (this.ofClouds) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                case 3:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == TREES) {
            switch (this.ofTrees) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                case 3:
                default:
                    return s + Lang.getDefault();

                case 4:
                    return s + Lang.get("of.general.smart");
            }
        } else if (p_getKeyBindingOF_1_ == DROPPED_ITEMS) {
            switch (this.ofDroppedItems) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == RAIN) {
            switch (this.ofRain) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                case 3:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == ANIMATED_WATER) {
            switch (this.ofAnimatedWater) {
                case 1:
                    return s + Lang.get("of.options.animation.dynamic");

                case 2:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getOn();
            }
        } else if (p_getKeyBindingOF_1_ == ANIMATED_LAVA) {
            switch (this.ofAnimatedLava) {
                case 1:
                    return s + Lang.get("of.options.animation.dynamic");

                case 2:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getOn();
            }
        } else if (p_getKeyBindingOF_1_ == ANIMATED_FIRE) {
            return this.ofAnimatedFire ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == ANIMATED_PORTAL) {
            return this.ofAnimatedPortal ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == ANIMATED_REDSTONE) {
            return this.ofAnimatedRedstone ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == ANIMATED_EXPLOSION) {
            return this.ofAnimatedExplosion ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == ANIMATED_FLAME) {
            return this.ofAnimatedFlame ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == ANIMATED_SMOKE) {
            return this.ofAnimatedSmoke ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == VOID_PARTICLES) {
            return this.ofVoidParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == WATER_PARTICLES) {
            return this.ofWaterParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == PORTAL_PARTICLES) {
            return this.ofPortalParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == POTION_PARTICLES) {
            return this.ofPotionParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == FIREWORK_PARTICLES) {
            return this.ofFireworkParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == DRIPPING_WATER_LAVA) {
            return this.ofDrippingWaterLava ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == ANIMATED_TERRAIN) {
            return this.ofAnimatedTerrain ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == ANIMATED_TEXTURES) {
            return this.ofAnimatedTextures ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == RAIN_SPLASH) {
            return this.ofRainSplash ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == LAGOMETER) {
            return this.ofLagometer ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == SHOW_FPS) {
            return this.ofShowFps ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == AUTOSAVE_TICKS) {
            return this.ofAutoSaveTicks <= 40 ? s + Lang.get("of.options.save.default") : this.ofAutoSaveTicks <= 400 ? s + Lang.get("of.options.save.20s") : this.ofAutoSaveTicks <= 4000 ? s + Lang.get("of.options.save.3min") : s + Lang.get("of.options.save.30min");
        } else if (p_getKeyBindingOF_1_ == BETTER_GRASS) {
            switch (this.ofBetterGrass) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getOff();
            }
        } else if (p_getKeyBindingOF_1_ == CONNECTED_TEXTURES) {
            switch (this.ofConnectedTextures) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getOff();
            }
        } else if (p_getKeyBindingOF_1_ == WEATHER) {
            return this.ofWeather ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == SKY) {
            return this.ofSky ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == STARS) {
            return this.ofStars ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == SUN_MOON) {
            return this.ofSunMoon ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == VIGNETTE) {
            switch (this.ofVignette) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == CHUNK_UPDATES) {
            return s + this.ofChunkUpdates;
        } else if (p_getKeyBindingOF_1_ == CHUNK_UPDATES_DYNAMIC) {
            return this.ofChunkUpdatesDynamic ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == TIME) {
            return this.ofTime == 1 ? s + Lang.get("of.options.time.dayOnly") : this.ofTime == 2 ? s + Lang.get("of.options.time.nightOnly") : s + Lang.getDefault();
        } else if (p_getKeyBindingOF_1_ == CLEAR_WATER) {
            return this.ofClearWater ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == AA_LEVEL) {
            String s3 = "";

            if (this.ofAaLevel != Config.getAntialiasingLevel()) {
                s3 = " (" + Lang.get("of.general.restart") + ")";
            }

            return this.ofAaLevel == 0 ? s + Lang.getOff() + s3 : s + this.ofAaLevel + s3;
        } else if (p_getKeyBindingOF_1_ == AF_LEVEL) {
            return this.ofAfLevel == 1 ? s + Lang.getOff() : s + this.ofAfLevel;
        } else if (p_getKeyBindingOF_1_ == PROFILER) {
            return this.ofProfiler ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == BETTER_SNOW) {
            return this.ofBetterSnow ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == SWAMP_COLORS) {
            return this.ofSwampColors ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == RANDOM_MOBS) {
            return this.ofRandomMobs ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == SMOOTH_BIOMES) {
            return this.ofSmoothBiomes ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == CUSTOM_FONTS) {
            return this.ofCustomFonts ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == CUSTOM_COLORS) {
            return this.ofCustomColors ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == CUSTOM_SKY) {
            return this.ofCustomSky ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == SHOW_CAPES) {
            return this.ofShowCapes ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == CUSTOM_ITEMS) {
            return this.ofCustomItems ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == NATURAL_TEXTURES) {
            return this.ofNaturalTextures ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == FAST_MATH) {
            return this.ofFastMath ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == TRANSLUCENT_BLOCKS) {
            return this.ofTranslucentBlocks == 1 ? s + Lang.getFast() : this.ofTranslucentBlocks == 2 ? s + Lang.getFancy() : s + Lang.getDefault();
        } else if (p_getKeyBindingOF_1_ == LAZY_CHUNK_LOADING) {
            return this.ofLazyChunkLoading ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == DYNAMIC_FOV) {
            return this.ofDynamicFov ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == DYNAMIC_LIGHTS) {
            final int k = indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
            return s + getTranslation(KEYS_DYNAMIC_LIGHTS, k);
        } else if (p_getKeyBindingOF_1_ == FULLSCREEN_MODE) {
            return this.ofFullscreenMode.equals("Default") ? s + Lang.getDefault() : s + this.ofFullscreenMode;
        } else if (p_getKeyBindingOF_1_ == HELD_ITEM_TOOLTIPS) {
            return this.heldItemTooltips ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == FRAMERATE_LIMIT) {
            final float f = this.getOptionFloatValue(p_getKeyBindingOF_1_);
            return f == 0.0F ? s + Lang.get("of.options.framerateLimit.vsync") : f == p_getKeyBindingOF_1_.valueMax ? s + I18n.format("options.framerateLimit.max") : s + (int) f + " fps";
        } else {
            return null;
        }
    }

    public void loadOfOptions() {
        try {
            File file1 = this.optionsFileOF;

            if (!file1.exists()) file1 = this.optionsFile;
            if (!file1.exists()) return;

            final BufferedReader bufferedreader = new BufferedReader(new FileReader(file1));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                try {
                    final String[] split = s.split(":");

                    if (split[0].equals("ofRenderDistanceChunks") && split.length >= 2) {
                        this.renderDistanceChunks = Integer.parseInt(split[1]);
                        this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 32);
                    }

                    if (split[0].equals("ofFogType") && split.length >= 2) {
                        this.ofFogType = Integer.parseInt(split[1]);
                        this.ofFogType = Config.limit(this.ofFogType, 1, 3);
                    }

                    if (split[0].equals("ofFogStart") && split.length >= 2) {
                        this.ofFogStart = Float.parseFloat(split[1]);

                        if (this.ofFogStart < 0.2F) {
                            this.ofFogStart = 0.2F;
                        }

                        if (this.ofFogStart > 0.81F) {
                            this.ofFogStart = 0.8F;
                        }
                    }

                    if (split[0].equals("ofMipmapType") && split.length >= 2) {
                        this.ofMipmapType = Integer.parseInt(split[1]);
                        this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
                    }

                    if (split[0].equals("ofOcclusionFancy") && split.length >= 2) {
                        this.ofOcclusionFancy = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofSmoothFps") && split.length >= 2) {
                        this.ofSmoothFps = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofSmoothWorld") && split.length >= 2) {
                        this.ofSmoothWorld = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAoLevel") && split.length >= 2) {
                        this.ofAoLevel = Float.parseFloat(split[1]);
                        this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
                    }

                    if (split[0].equals("ofClouds") && split.length >= 2) {
                        this.ofClouds = Integer.parseInt(split[1]);
                        this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                        this.updateRenderClouds();
                    }

                    if (split[0].equals("ofCloudsHeight") && split.length >= 2) {
                        this.ofCloudsHeight = Float.parseFloat(split[1]);
                        this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
                    }

                    if (split[0].equals("ofTrees") && split.length >= 2) {
                        this.ofTrees = Integer.parseInt(split[1]);
                        this.ofTrees = limit(this.ofTrees, OF_TREES_VALUES);
                    }

                    if (split[0].equals("ofDroppedItems") && split.length >= 2) {
                        this.ofDroppedItems = Integer.parseInt(split[1]);
                        this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
                    }

                    if (split[0].equals("ofRain") && split.length >= 2) {
                        this.ofRain = Integer.parseInt(split[1]);
                        this.ofRain = Config.limit(this.ofRain, 0, 3);
                    }

                    if (split[0].equals("ofAnimatedWater") && split.length >= 2) {
                        this.ofAnimatedWater = Integer.parseInt(split[1]);
                        this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
                    }

                    if (split[0].equals("ofAnimatedLava") && split.length >= 2) {
                        this.ofAnimatedLava = Integer.parseInt(split[1]);
                        this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
                    }

                    if (split[0].equals("ofAnimatedFire") && split.length >= 2) {
                        this.ofAnimatedFire = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAnimatedPortal") && split.length >= 2) {
                        this.ofAnimatedPortal = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAnimatedRedstone") && split.length >= 2) {
                        this.ofAnimatedRedstone = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAnimatedExplosion") && split.length >= 2) {
                        this.ofAnimatedExplosion = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAnimatedFlame") && split.length >= 2) {
                        this.ofAnimatedFlame = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAnimatedSmoke") && split.length >= 2) {
                        this.ofAnimatedSmoke = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofVoidParticles") && split.length >= 2) {
                        this.ofVoidParticles = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofWaterParticles") && split.length >= 2) {
                        this.ofWaterParticles = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofPortalParticles") && split.length >= 2) {
                        this.ofPortalParticles = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofPotionParticles") && split.length >= 2) {
                        this.ofPotionParticles = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofFireworkParticles") && split.length >= 2) {
                        this.ofFireworkParticles = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofDrippingWaterLava") && split.length >= 2) {
                        this.ofDrippingWaterLava = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAnimatedTerrain") && split.length >= 2) {
                        this.ofAnimatedTerrain = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAnimatedTextures") && split.length >= 2) {
                        this.ofAnimatedTextures = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofRainSplash") && split.length >= 2) {
                        this.ofRainSplash = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofLagometer") && split.length >= 2) {
                        this.ofLagometer = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofShowFps") && split.length >= 2) {
                        this.ofShowFps = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofAutoSaveTicks") && split.length >= 2) {
                        this.ofAutoSaveTicks = Integer.parseInt(split[1]);
                        this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
                    }

                    if (split[0].equals("ofBetterGrass") && split.length >= 2) {
                        this.ofBetterGrass = Integer.parseInt(split[1]);
                        this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
                    }

                    if (split[0].equals("ofConnectedTextures") && split.length >= 2) {
                        this.ofConnectedTextures = Integer.parseInt(split[1]);
                        this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
                    }

                    if (split[0].equals("ofWeather") && split.length >= 2) {
                        this.ofWeather = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofSky") && split.length >= 2) {
                        this.ofSky = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofStars") && split.length >= 2) {
                        this.ofStars = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofSunMoon") && split.length >= 2) {
                        this.ofSunMoon = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofVignette") && split.length >= 2) {
                        this.ofVignette = Integer.parseInt(split[1]);
                        this.ofVignette = Config.limit(this.ofVignette, 0, 2);
                    }

                    if (split[0].equals("ofChunkUpdates") && split.length >= 2) {
                        this.ofChunkUpdates = Integer.parseInt(split[1]);
                        this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
                    }

                    if (split[0].equals("ofChunkUpdatesDynamic") && split.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofTime") && split.length >= 2) {
                        this.ofTime = Integer.parseInt(split[1]);
                        this.ofTime = Config.limit(this.ofTime, 0, 2);
                    }

                    if (split[0].equals("ofClearWater") && split.length >= 2) {
                        this.ofClearWater = Boolean.parseBoolean(split[1]);
                        this.updateWaterOpacity();
                    }

                    if (split[0].equals("ofAaLevel") && split.length >= 2) {
                        this.ofAaLevel = Integer.parseInt(split[1]);
                        this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
                    }

                    if (split[0].equals("ofAfLevel") && split.length >= 2) {
                        this.ofAfLevel = Integer.parseInt(split[1]);
                        this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
                    }

                    if (split[0].equals("ofProfiler") && split.length >= 2) {
                        this.ofProfiler = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofBetterSnow") && split.length >= 2) {
                        this.ofBetterSnow = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofSwampColors") && split.length >= 2) {
                        this.ofSwampColors = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofRandomMobs") && split.length >= 2) {
                        this.ofRandomMobs = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofSmoothBiomes") && split.length >= 2) {
                        this.ofSmoothBiomes = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofCustomFonts") && split.length >= 2) {
                        this.ofCustomFonts = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofCustomColors") && split.length >= 2) {
                        this.ofCustomColors = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofCustomItems") && split.length >= 2) {
                        this.ofCustomItems = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofCustomSky") && split.length >= 2) {
                        this.ofCustomSky = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofShowCapes") && split.length >= 2) {
                        this.ofShowCapes = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofNaturalTextures") && split.length >= 2) {
                        this.ofNaturalTextures = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofLazyChunkLoading") && split.length >= 2) {
                        this.ofLazyChunkLoading = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofDynamicFov") && split.length >= 2) {
                        this.ofDynamicFov = Boolean.parseBoolean(split[1]);
                    }

                    if (split[0].equals("ofDynamicLights") && split.length >= 2) {
                        this.ofDynamicLights = Integer.parseInt(split[1]);
                        this.ofDynamicLights = limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
                    }

                    if (split[0].equals("ofFullscreenMode") && split.length >= 2) {
                        this.ofFullscreenMode = split[1];
                    }

                    if (split[0].equals("ofFastMath") && split.length >= 2) {
                        this.ofFastMath = Boolean.parseBoolean(split[1]);
                        fastMath = this.ofFastMath;
                    }

                    if (split[0].equals("ofTranslucentBlocks") && split.length >= 2) {
                        this.ofTranslucentBlocks = Integer.parseInt(split[1]);
                        this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
                    }

                    if (split[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) {
                        this.ofKeyBindZoom.setKeyCode(Integer.parseInt(split[1]));
                    }
                } catch (Exception e) {
                    Config.dbg("Skipping bad option: " + s);
                    mc.getLogger().error(e);
                }
            }

            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        } catch (Exception exception1) {
            Config.warn("Failed to load options");
            exception1.printStackTrace();
        }
    }

    public void saveOfOptions() {
        try {
            final PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFileOF));
            printwriter.println("ofRenderDistanceChunks:" + this.renderDistanceChunks);
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
            printwriter.println("ofRandomMobs:" + this.ofRandomMobs);
            printwriter.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
            printwriter.println("ofCustomFonts:" + this.ofCustomFonts);
            printwriter.println("ofCustomColors:" + this.ofCustomColors);
            printwriter.println("ofCustomItems:" + this.ofCustomItems);
            printwriter.println("ofCustomSky:" + this.ofCustomSky);
            printwriter.println("ofShowCapes:" + this.ofShowCapes);
            printwriter.println("ofNaturalTextures:" + this.ofNaturalTextures);
            printwriter.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
            printwriter.println("ofDynamicFov:" + this.ofDynamicFov);
            printwriter.println("ofDynamicLights:" + this.ofDynamicLights);
            printwriter.println("ofFullscreenMode:" + this.ofFullscreenMode);
            printwriter.println("ofFastMath:" + this.ofFastMath);
            printwriter.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
            printwriter.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
            printwriter.close();
        } catch (Throwable t) {
            Config.warn("Failed to save options");
            mc.getLogger().error(t);
        }
    }

    private void updateRenderClouds() {
        switch (this.ofClouds) {
            case 1:
                this.clouds = 1;
                break;

            case 2:
                this.clouds = 2;
                break;

            case 3:
                this.clouds = 0;
                break;

            default:
                if (this.fancyGraphics) {
                    this.clouds = 2;
                } else {
                    this.clouds = 1;
                }
        }
    }

    public void resetSettings() {
        this.renderDistanceChunks = 8;
        this.viewBobbing = true;
        this.anaglyph = false;
        this.limitFramerate = (int) FRAMERATE_LIMIT.getValueMax();
        this.enableVsync = false;
        this.updateVSync();
        this.mipmapLevels = 4;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.clouds = 2;
        this.fovSetting = 70.0F;
        this.gammaSetting = 0.0F;
        this.guiScale = 0;
        this.particleSetting = 0;
        this.heldItemTooltips = true;
        this.useVbo = false;
        this.allowBlockAlternatives = true;
        this.forceUnicodeFont = false;
        this.ofFogType = 1;
        this.ofFogStart = 0.8F;
        this.ofMipmapType = 0;
        this.ofOcclusionFancy = false;
        this.ofSmoothFps = false;
        Config.updateAvailableProcessors();
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = Config.isSingleProcessor();
        this.ofFastMath = false;
        this.ofTranslucentBlocks = 0;
        this.ofDynamicFov = true;
        this.ofDynamicLights = 3;
        this.ofAoLevel = 1.0F;
        this.ofAaLevel = 0;
        this.ofAfLevel = 1;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0F;
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
        this.ofRandomMobs = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomItems = true;
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
        this.ofFireworkParticles = true;
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        Shaders.setShaderPack(Shaders.packNameNone);
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
        if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
            Config.waterOpacityChanged = true;
        }

        ClearWater.updateWaterOpacity(this, this.mc.world);
    }

    public void setAllAnimations(boolean p_setAllAnimations_1_) {
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
        this.particleSetting = p_setAllAnimations_1_ ? 0 : 2;
        this.ofDrippingWaterLava = p_setAllAnimations_1_;
        this.ofAnimatedTerrain = p_setAllAnimations_1_;
        this.ofAnimatedTextures = p_setAllAnimations_1_;
    }

    public enum Options {
        INVERT_MOUSE("INVERT_MOUSE", 0, "options.invertMouse", false, true),
        SENSITIVITY("SENSITIVITY", 1, "options.sensitivity", true, false),
        FOV("FOV", 2, "options.fov", true, false, 30.0F, 110.0F, 1.0F),
        GAMMA("GAMMA", 3, "options.gamma", true, false),
        SATURATION("SATURATION", 4, "options.saturation", true, false),
        RENDER_DISTANCE("RENDER_DISTANCE", 5, "options.renderDistance", true, false, 2.0F, 16.0F, 1.0F),
        VIEW_BOBBING("VIEW_BOBBING", 6, "options.viewBobbing", false, true),
        ANAGLYPH("ANAGLYPH", 7, "options.anaglyph", false, true),
        FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F),
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
        MIPMAP_LEVELS("MIPMAP_LEVELS", 29, "options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
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
        FOG_FANCY("", 999, "of.options.FOG_FANCY", false, false),
        FOG_START("", 999, "of.options.FOG_START", false, false),
        MIPMAP_TYPE("", 999, "of.options.MIPMAP_TYPE", true, false, 0.0F, 3.0F, 1.0F),
        SMOOTH_FPS("", 999, "of.options.SMOOTH_FPS", false, false),
        CLOUDS("", 999, "of.options.CLOUDS", false, false),
        CLOUD_HEIGHT("", 999, "of.options.CLOUD_HEIGHT", true, false),
        TREES("", 999, "of.options.TREES", false, false),
        RAIN("", 999, "of.options.RAIN", false, false),
        ANIMATED_WATER("", 999, "of.options.ANIMATED_WATER", false, false),
        ANIMATED_LAVA("", 999, "of.options.ANIMATED_LAVA", false, false),
        ANIMATED_FIRE("", 999, "of.options.ANIMATED_FIRE", false, false),
        ANIMATED_PORTAL("", 999, "of.options.ANIMATED_PORTAL", false, false),
        AO_LEVEL("", 999, "of.options.AO_LEVEL", true, false),
        LAGOMETER("", 999, "of.options.LAGOMETER", false, false),
        SHOW_FPS("", 999, "of.options.SHOW_FPS", false, false),
        AUTOSAVE_TICKS("", 999, "of.options.AUTOSAVE_TICKS", false, false),
        BETTER_GRASS("", 999, "of.options.BETTER_GRASS", false, false),
        ANIMATED_REDSTONE("", 999, "of.options.ANIMATED_REDSTONE", false, false),
        ANIMATED_EXPLOSION("", 999, "of.options.ANIMATED_EXPLOSION", false, false),
        ANIMATED_FLAME("", 999, "of.options.ANIMATED_FLAME", false, false),
        ANIMATED_SMOKE("", 999, "of.options.ANIMATED_SMOKE", false, false),
        WEATHER("", 999, "of.options.WEATHER", false, false),
        SKY("", 999, "of.options.SKY", false, false),
        STARS("", 999, "of.options.STARS", false, false),
        SUN_MOON("", 999, "of.options.SUN_MOON", false, false),
        VIGNETTE("", 999, "of.options.VIGNETTE", false, false),
        CHUNK_UPDATES("", 999, "of.options.CHUNK_UPDATES", false, false),
        CHUNK_UPDATES_DYNAMIC("", 999, "of.options.CHUNK_UPDATES_DYNAMIC", false, false),
        TIME("", 999, "of.options.TIME", false, false),
        CLEAR_WATER("", 999, "of.options.CLEAR_WATER", false, false),
        SMOOTH_WORLD("", 999, "of.options.SMOOTH_WORLD", false, false),
        VOID_PARTICLES("", 999, "of.options.VOID_PARTICLES", false, false),
        WATER_PARTICLES("", 999, "of.options.WATER_PARTICLES", false, false),
        RAIN_SPLASH("", 999, "of.options.RAIN_SPLASH", false, false),
        PORTAL_PARTICLES("", 999, "of.options.PORTAL_PARTICLES", false, false),
        POTION_PARTICLES("", 999, "of.options.POTION_PARTICLES", false, false),
        FIREWORK_PARTICLES("", 999, "of.options.FIREWORK_PARTICLES", false, false),
        PROFILER("", 999, "of.options.PROFILER", false, false),
        DRIPPING_WATER_LAVA("", 999, "of.options.DRIPPING_WATER_LAVA", false, false),
        BETTER_SNOW("", 999, "of.options.BETTER_SNOW", false, false),
        FULLSCREEN_MODE("", 999, "of.options.FULLSCREEN_MODE", false, false),
        ANIMATED_TERRAIN("", 999, "of.options.ANIMATED_TERRAIN", false, false),
        SWAMP_COLORS("", 999, "of.options.SWAMP_COLORS", false, false),
        RANDOM_MOBS("", 999, "of.options.RANDOM_MOBS", false, false),
        SMOOTH_BIOMES("", 999, "of.options.SMOOTH_BIOMES", false, false),
        CUSTOM_FONTS("", 999, "of.options.CUSTOM_FONTS", false, false),
        CUSTOM_COLORS("", 999, "of.options.CUSTOM_COLORS", false, false),
        SHOW_CAPES("", 999, "of.options.SHOW_CAPES", false, false),
        CONNECTED_TEXTURES("", 999, "of.options.CONNECTED_TEXTURES", false, false),
        CUSTOM_ITEMS("", 999, "of.options.CUSTOM_ITEMS", false, false),
        AA_LEVEL("", 999, "of.options.AA_LEVEL", true, false, 0.0F, 16.0F, 1.0F),
        AF_LEVEL("", 999, "of.options.AF_LEVEL", true, false, 1.0F, 16.0F, 1.0F),
        ANIMATED_TEXTURES("", 999, "of.options.ANIMATED_TEXTURES", false, false),
        NATURAL_TEXTURES("", 999, "of.options.NATURAL_TEXTURES", false, false),
        HELD_ITEM_TOOLTIPS("", 999, "of.options.HELD_ITEM_TOOLTIPS", false, false),
        DROPPED_ITEMS("", 999, "of.options.DROPPED_ITEMS", false, false),
        LAZY_CHUNK_LOADING("", 999, "of.options.LAZY_CHUNK_LOADING", false, false),
        CUSTOM_SKY("", 999, "of.options.CUSTOM_SKY", false, false),
        FAST_MATH("", 999, "of.options.FAST_MATH", false, false),
        TRANSLUCENT_BLOCKS("", 999, "of.options.TRANSLUCENT_BLOCKS", false, false),
        DYNAMIC_FOV("", 999, "of.options.DYNAMIC_FOV", false, false),
        DYNAMIC_LIGHTS("", 999, "of.options.DYNAMIC_LIGHTS", false, false);

        private static final Options[] $VALUES = new Options[]{INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO, ENTITY_SHADOWS};
        private static final String __OBFID = "CL_00000653";
        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private final float valueMin;
        private float valueMax;

        Options(String p_i0_3_, int p_i0_4_, String p_i0_5_, boolean p_i0_6_, boolean p_i0_7_) {
            this(p_i0_3_, p_i0_4_, p_i0_5_, p_i0_6_, p_i0_7_, 0.0F, 1.0F, 0.0F);
        }

        Options(String p_i1_3_, int p_i1_4_, String p_i1_5_, boolean p_i1_6_, boolean p_i1_7_, float p_i1_8_, float p_i1_9_, float p_i1_10_) {
            this.enumString = p_i1_5_;
            this.enumFloat = p_i1_6_;
            this.enumBoolean = p_i1_7_;
            this.valueMin = p_i1_8_;
            this.valueMax = p_i1_9_;
            this.valueStep = p_i1_10_;
        }

        public static Options getEnumOptions(int p_74379_0_) {
            for (Options gamesettings$options : values()) {
                if (gamesettings$options.returnEnumOrdinal() == p_74379_0_) {
                    return gamesettings$options;
                }
            }

            return null;
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

        public void setValueMax(float p_148263_1_) {
            this.valueMax = p_148263_1_;
        }

        public float normalizeValue(float p_148266_1_) {
            return clamp_float((this.snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
        }

        public float denormalizeValue(float p_148262_1_) {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * clamp_float(p_148262_1_, 0.0F, 1.0F));
        }

        public float snapToStepClamp(float p_148268_1_) {
            p_148268_1_ = this.snapToStep(p_148268_1_);
            return clamp_float(p_148268_1_, this.valueMin, this.valueMax);
        }

        protected float snapToStep(float p_148264_1_) {
            if (this.valueStep > 0.0F) {
                p_148264_1_ = this.valueStep * (float) Math.round(p_148264_1_ / this.valueStep);
            }

            return p_148264_1_;
        }
    }

    static final class GameSettings$2 {

        static final int[] field_151477_a = new int[values().length];
        private static final String __OBFID = "CL_00000652";

        static {
            try {
                field_151477_a[INVERT_MOUSE.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[VIEW_BOBBING.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[ANAGLYPH.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[FBO_ENABLE.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[CHAT_COLOR.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[CHAT_LINKS.ordinal()] = 6;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[CHAT_LINKS_PROMPT.ordinal()] = 7;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[SNOOPER_ENABLED.ordinal()] = 8;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[USE_FULLSCREEN.ordinal()] = 9;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[ENABLE_VSYNC.ordinal()] = 10;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[USE_VBO.ordinal()] = 11;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[TOUCHSCREEN.ordinal()] = 12;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[STREAM_SEND_METADATA.ordinal()] = 13;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[FORCE_UNICODE_FONT.ordinal()] = 14;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[BLOCK_ALTERNATIVES.ordinal()] = 15;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[REDUCED_DEBUG_INFO.ordinal()] = 16;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                field_151477_a[ENTITY_SHADOWS.ordinal()] = 17;
            } catch (NoSuchFieldError ignored) {
            }
        }
    }

}
