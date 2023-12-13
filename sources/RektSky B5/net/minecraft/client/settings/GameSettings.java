/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.settings;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import optfine.ClearWater;
import optfine.Config;
import optfine.CustomColorizer;
import optfine.CustomSky;
import optfine.NaturalTextures;
import optfine.RandomMobs;
import optfine.Reflector;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameSettings {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();
    private static final ParameterizedType typeListString = new ParameterizedType(){
        private static final String __OBFID = "CL_00000651";

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class};
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
    private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
    private static final String[] AMBIENT_OCCLUSIONS = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
    private static final String[] STREAM_COMPRESSIONS = new String[]{"options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high"};
    private static final String[] STREAM_CHAT_MODES = new String[]{"options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never"};
    private static final String[] STREAM_CHAT_FILTER_MODES = new String[]{"options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods"};
    private static final String[] STREAM_MIC_MODES = new String[]{"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk"};
    private static final String[] field_181149_aW = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
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
    public List resourcePacks = Lists.newArrayList();
    public List field_183018_l = Lists.newArrayList();
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
    private final Set setModelParts = Sets.newHashSet(EnumPlayerModelParts.values());
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
    private Map mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
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
    protected Minecraft mc;
    private File optionsFile;
    public EnumDifficulty difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public boolean field_181657_aC;
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
    private static final String __OBFID = "CL_00000650";
    public int ofFogType = 1;
    public float ofFogStart = 0.8f;
    public int ofMipmapType = 0;
    public boolean ofOcclusionFancy = false;
    public boolean ofSmoothFps = false;
    public boolean ofSmoothWorld = Config.isSingleProcessor();
    public boolean ofLazyChunkLoading = Config.isSingleProcessor();
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
    public int ofChunkLoading = 0;
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
    public boolean ofNaturalTextures = false;
    public boolean ofFastMath = false;
    public boolean ofFastRender = false;
    public int ofTranslucentBlocks = 0;
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
    public static final int ANIM_ON = 0;
    public static final int ANIM_GENERATED = 1;
    public static final int ANIM_OFF = 2;
    public static final int CL_DEFAULT = 0;
    public static final int CL_SMOOTH = 1;
    public static final int CL_THREADED = 2;
    public static final String DEFAULT_STR = "Default";
    public KeyBinding ofKeyBindZoom;
    private File optionsFileOF;

    public GameSettings(Minecraft mcIn, File p_i46326_2_) {
        this.keyBindings = ArrayUtils.addAll(new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.mc = mcIn;
        this.optionsFile = new File(p_i46326_2_, "options.txt");
        this.optionsFileOF = new File(p_i46326_2_, "optionsof.txt");
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.ofKeyBindZoom = new KeyBinding("Zoom", 46, "key.categories.misc");
        this.keyBindings = ArrayUtils.add(this.keyBindings, this.ofKeyBindZoom);
        Options.RENDER_DISTANCE.setValueMax(32.0f);
        this.renderDistanceChunks = 8;
        this.loadOptions();
        Config.initGameSettings(this);
    }

    public GameSettings() {
        this.keyBindings = ArrayUtils.addAll(new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
    }

    public static String getKeyDisplayString(int p_74298_0_) {
        return p_74298_0_ < 0 ? I18n.format("key.mouseButton", p_74298_0_ + 101) : (p_74298_0_ < 256 ? Keyboard.getKeyName(p_74298_0_) : String.format("%c", Character.valueOf((char)(p_74298_0_ - 256))).toUpperCase());
    }

    public static boolean isKeyDown(KeyBinding p_100015_0_) {
        int i2 = p_100015_0_.getKeyCode();
        return i2 >= -100 && i2 <= 255 ? (p_100015_0_.getKeyCode() == 0 ? false : (p_100015_0_.getKeyCode() < 0 ? Mouse.isButtonDown(p_100015_0_.getKeyCode() + 100) : Keyboard.isKeyDown(p_100015_0_.getKeyCode()))) : false;
    }

    public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_) {
        p_151440_1_.setKeyCode(p_151440_2_);
        this.saveOptions();
    }

    public void setOptionFloatValue(Options p_74304_1_, float p_74304_2_) {
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
            int i2 = this.mipmapLevels;
            this.mipmapLevels = (int)p_74304_2_;
            if ((float)i2 != p_74304_2_) {
                this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
                this.mc.scheduleResourcesRefresh();
            }
        }
        if (p_74304_1_ == Options.BLOCK_ALTERNATIVES) {
            this.allowBlockAlternatives = !this.allowBlockAlternatives;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_74304_1_ == Options.RENDER_DISTANCE) {
            this.renderDistanceChunks = (int)p_74304_2_;
            this.mc.renderGlobal.setDisplayListEntitiesDirty();
        }
        if (p_74304_1_ == Options.STREAM_BYTES_PER_PIXEL) {
            this.streamBytesPerPixel = p_74304_2_;
        }
        if (p_74304_1_ == Options.STREAM_VOLUME_MIC) {
            this.streamMicVolume = p_74304_2_;
            this.mc.getTwitchStream().updateStreamVolume();
        }
        if (p_74304_1_ == Options.STREAM_VOLUME_SYSTEM) {
            this.streamGameVolume = p_74304_2_;
            this.mc.getTwitchStream().updateStreamVolume();
        }
        if (p_74304_1_ == Options.STREAM_KBPS) {
            this.streamKbps = p_74304_2_;
        }
        if (p_74304_1_ == Options.STREAM_FPS) {
            this.streamFps = p_74304_2_;
        }
    }

    public void setOptionValue(Options p_74306_1_, int p_74306_2_) {
        this.setOptionValueOF(p_74306_1_, p_74306_2_);
        if (p_74306_1_ == Options.INVERT_MOUSE) {
            boolean bl = this.invertMouse = !this.invertMouse;
        }
        if (p_74306_1_ == Options.GUI_SCALE) {
            this.guiScale = this.guiScale + p_74306_2_ & 3;
        }
        if (p_74306_1_ == Options.PARTICLES) {
            this.particleSetting = (this.particleSetting + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.VIEW_BOBBING) {
            boolean bl = this.viewBobbing = !this.viewBobbing;
        }
        if (p_74306_1_ == Options.RENDER_CLOUDS) {
            this.clouds = (this.clouds + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.FORCE_UNICODE_FONT) {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            this.mc.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }
        if (p_74306_1_ == Options.FBO_ENABLE) {
            boolean bl = this.fboEnable = !this.fboEnable;
        }
        if (p_74306_1_ == Options.ANAGLYPH) {
            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
        }
        if (p_74306_1_ == Options.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.updateRenderClouds();
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
            boolean bl = this.streamSendMetadata = !this.streamSendMetadata;
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
            boolean bl = this.chatColours = !this.chatColours;
        }
        if (p_74306_1_ == Options.CHAT_LINKS) {
            boolean bl = this.chatLinks = !this.chatLinks;
        }
        if (p_74306_1_ == Options.CHAT_LINKS_PROMPT) {
            boolean bl = this.chatLinksPrompt = !this.chatLinksPrompt;
        }
        if (p_74306_1_ == Options.SNOOPER_ENABLED) {
            boolean bl = this.snooperEnabled = !this.snooperEnabled;
        }
        if (p_74306_1_ == Options.TOUCHSCREEN) {
            boolean bl = this.touchscreen = !this.touchscreen;
        }
        if (p_74306_1_ == Options.USE_FULLSCREEN) {
            boolean bl = this.fullScreen = !this.fullScreen;
            if (this.mc.isFullScreen() != this.fullScreen) {
                this.mc.toggleFullscreen();
            }
        }
        if (p_74306_1_ == Options.ENABLE_VSYNC) {
            this.enableVsync = !this.enableVsync;
            Display.setVSyncEnabled(this.enableVsync);
        }
        if (p_74306_1_ == Options.USE_VBO) {
            this.useVbo = !this.useVbo;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.BLOCK_ALTERNATIVES) {
            this.allowBlockAlternatives = !this.allowBlockAlternatives;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.REDUCED_DEBUG_INFO) {
            boolean bl = this.reducedDebugInfo = !this.reducedDebugInfo;
        }
        if (p_74306_1_ == Options.ENTITY_SHADOWS) {
            this.field_181151_V = !this.field_181151_V;
        }
        this.saveOptions();
    }

    public float getOptionFloatValue(Options p_74296_1_) {
        return p_74296_1_ == Options.CLOUD_HEIGHT ? this.ofCloudsHeight : (p_74296_1_ == Options.AO_LEVEL ? this.ofAoLevel : (p_74296_1_ == Options.AA_LEVEL ? (float)this.ofAaLevel : (p_74296_1_ == Options.AF_LEVEL ? (float)this.ofAfLevel : (p_74296_1_ == Options.MIPMAP_TYPE ? (float)this.ofMipmapType : (p_74296_1_ == Options.FRAMERATE_LIMIT ? ((float)this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync ? 0.0f : (float)this.limitFramerate) : (p_74296_1_ == Options.FOV ? this.fovSetting : (p_74296_1_ == Options.GAMMA ? this.gammaSetting : (p_74296_1_ == Options.SATURATION ? this.saturation : (p_74296_1_ == Options.SENSITIVITY ? this.mouseSensitivity : (p_74296_1_ == Options.CHAT_OPACITY ? this.chatOpacity : (p_74296_1_ == Options.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : (p_74296_1_ == Options.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : (p_74296_1_ == Options.CHAT_SCALE ? this.chatScale : (p_74296_1_ == Options.CHAT_WIDTH ? this.chatWidth : (p_74296_1_ == Options.FRAMERATE_LIMIT ? (float)this.limitFramerate : (p_74296_1_ == Options.MIPMAP_LEVELS ? (float)this.mipmapLevels : (p_74296_1_ == Options.RENDER_DISTANCE ? (float)this.renderDistanceChunks : (p_74296_1_ == Options.STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : (p_74296_1_ == Options.STREAM_VOLUME_MIC ? this.streamMicVolume : (p_74296_1_ == Options.STREAM_VOLUME_SYSTEM ? this.streamGameVolume : (p_74296_1_ == Options.STREAM_KBPS ? this.streamKbps : (p_74296_1_ == Options.STREAM_FPS ? this.streamFps : 0.0f))))))))))))))))))))));
    }

    public boolean getOptionOrdinalValue(Options p_74308_1_) {
        switch (p_74308_1_) {
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
                return this.field_181151_V;
            }
        }
        return false;
    }

    private static String getTranslation(String[] p_74299_0_, int p_74299_1_) {
        if (p_74299_1_ < 0 || p_74299_1_ >= p_74299_0_.length) {
            p_74299_1_ = 0;
        }
        return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
    }

    public String getKeyBinding(Options p_74297_1_) {
        String s2 = this.getKeyBindingOF(p_74297_1_);
        if (s2 != null) {
            return s2;
        }
        String s1 = I18n.format(p_74297_1_.getEnumString(), new Object[0]) + ": ";
        if (p_74297_1_.getEnumFloat()) {
            float f1 = this.getOptionFloatValue(p_74297_1_);
            float f2 = p_74297_1_.normalizeValue(f1);
            return p_74297_1_ == Options.SENSITIVITY ? (f2 == 0.0f ? s1 + I18n.format("options.sensitivity.min", new Object[0]) : (f2 == 1.0f ? s1 + I18n.format("options.sensitivity.max", new Object[0]) : s1 + (int)(f2 * 200.0f) + "%")) : (p_74297_1_ == Options.FOV ? (f1 == 70.0f ? s1 + I18n.format("options.fov.min", new Object[0]) : (f1 == 110.0f ? s1 + I18n.format("options.fov.max", new Object[0]) : s1 + (int)f1)) : (p_74297_1_ == Options.FRAMERATE_LIMIT ? (f1 == p_74297_1_.valueMax ? s1 + I18n.format("options.framerateLimit.max", new Object[0]) : s1 + (int)f1 + " fps") : (p_74297_1_ == Options.RENDER_CLOUDS ? (f1 == p_74297_1_.valueMin ? s1 + I18n.format("options.cloudHeight.min", new Object[0]) : s1 + ((int)f1 + 128)) : (p_74297_1_ == Options.GAMMA ? (f2 == 0.0f ? s1 + I18n.format("options.gamma.min", new Object[0]) : (f2 == 1.0f ? s1 + I18n.format("options.gamma.max", new Object[0]) : s1 + "+" + (int)(f2 * 100.0f) + "%")) : (p_74297_1_ == Options.SATURATION ? s1 + (int)(f2 * 400.0f) + "%" : (p_74297_1_ == Options.CHAT_OPACITY ? s1 + (int)(f2 * 90.0f + 10.0f) + "%" : (p_74297_1_ == Options.CHAT_HEIGHT_UNFOCUSED ? s1 + GuiNewChat.calculateChatboxHeight(f2) + "px" : (p_74297_1_ == Options.CHAT_HEIGHT_FOCUSED ? s1 + GuiNewChat.calculateChatboxHeight(f2) + "px" : (p_74297_1_ == Options.CHAT_WIDTH ? s1 + GuiNewChat.calculateChatboxWidth(f2) + "px" : (p_74297_1_ == Options.RENDER_DISTANCE ? s1 + (int)f1 + " chunks" : (p_74297_1_ == Options.MIPMAP_LEVELS ? (f1 == 0.0f ? s1 + I18n.format("options.off", new Object[0]) : s1 + (int)f1) : (p_74297_1_ == Options.STREAM_FPS ? s1 + TwitchStream.formatStreamFps(f2) + " fps" : (p_74297_1_ == Options.STREAM_KBPS ? s1 + TwitchStream.formatStreamKbps(f2) + " Kbps" : (p_74297_1_ == Options.STREAM_BYTES_PER_PIXEL ? s1 + String.format("%.3f bpp", Float.valueOf(TwitchStream.formatStreamBps(f2))) : (f2 == 0.0f ? s1 + I18n.format("options.off", new Object[0]) : s1 + (int)(f2 * 100.0f) + "%")))))))))))))));
        }
        if (p_74297_1_.getEnumBoolean()) {
            boolean flag = this.getOptionOrdinalValue(p_74297_1_);
            return flag ? s1 + I18n.format("options.on", new Object[0]) : s1 + I18n.format("options.off", new Object[0]);
        }
        if (p_74297_1_ == Options.GUI_SCALE) {
            return s1 + GameSettings.getTranslation(GUISCALES, this.guiScale);
        }
        if (p_74297_1_ == Options.CHAT_VISIBILITY) {
            return s1 + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
        }
        if (p_74297_1_ == Options.PARTICLES) {
            return s1 + GameSettings.getTranslation(PARTICLES, this.particleSetting);
        }
        if (p_74297_1_ == Options.AMBIENT_OCCLUSION) {
            return s1 + GameSettings.getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        if (p_74297_1_ == Options.STREAM_COMPRESSION) {
            return s1 + GameSettings.getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
        }
        if (p_74297_1_ == Options.STREAM_CHAT_ENABLED) {
            return s1 + GameSettings.getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
        }
        if (p_74297_1_ == Options.STREAM_CHAT_USER_FILTER) {
            return s1 + GameSettings.getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
        }
        if (p_74297_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return s1 + GameSettings.getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
        }
        if (p_74297_1_ == Options.RENDER_CLOUDS) {
            return s1 + GameSettings.getTranslation(field_181149_aW, this.clouds);
        }
        if (p_74297_1_ == Options.GRAPHICS) {
            if (this.fancyGraphics) {
                return s1 + I18n.format("options.graphics.fancy", new Object[0]);
            }
            String s22 = "options.graphics.fast";
            return s1 + I18n.format("options.graphics.fast", new Object[0]);
        }
        return s1;
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.optionsFile));
            String s2 = "";
            this.mapSoundLevels.clear();
            while ((s2 = bufferedreader.readLine()) != null) {
                try {
                    String[] astring = s2.split(":");
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
                        this.enableVsync = false;
                        if (this.limitFramerate <= 0) {
                            this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
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
                        this.ambientOcclusion = astring[1].equals("true") ? 2 : (astring[1].equals("false") ? 0 : Integer.parseInt(astring[1]));
                    }
                    if (astring[0].equals("renderClouds")) {
                        if (astring[1].equals("true")) {
                            this.clouds = 2;
                        } else if (astring[1].equals("false")) {
                            this.clouds = 0;
                        } else if (astring[1].equals("fast")) {
                            this.clouds = 1;
                        }
                    }
                    if (astring[0].equals("resourcePacks")) {
                        this.resourcePacks = (List)gson.fromJson(s2.substring(s2.indexOf(58) + 1), (Type)typeListString);
                        if (this.resourcePacks == null) {
                            this.resourcePacks = Lists.newArrayList();
                        }
                    }
                    if (astring[0].equals("incompatibleResourcePacks")) {
                        this.field_183018_l = (List)gson.fromJson(s2.substring(s2.indexOf(58) + 1), (Type)typeListString);
                        if (this.field_183018_l == null) {
                            this.field_183018_l = Lists.newArrayList();
                        }
                    }
                    if (astring[0].equals("lastServer") && astring.length >= 2) {
                        this.lastServer = s2.substring(s2.indexOf(58) + 1);
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
                        this.streamPreferredServer = s2.substring(s2.indexOf(58) + 1);
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
                    for (KeyBinding keyBinding : this.keyBindings) {
                        if (!astring[0].equals("key_" + keyBinding.getKeyDescription())) continue;
                        keyBinding.setKeyCode(Integer.parseInt(astring[1]));
                    }
                    for (SoundCategory soundCategory : SoundCategory.values()) {
                        if (!astring[0].equals("soundCategory_" + soundCategory.getCategoryName())) continue;
                        this.mapSoundLevels.put(soundCategory, Float.valueOf(this.parseFloat(astring[1])));
                    }
                    for (EnumPlayerModelParts enumPlayerModelParts : EnumPlayerModelParts.values()) {
                        if (!astring[0].equals("modelPart_" + enumPlayerModelParts.getPartName())) continue;
                        this.setModelPartEnabled(enumPlayerModelParts, astring[1].equals("true"));
                    }
                }
                catch (Exception exception) {
                    logger.warn("Skipping bad option: " + s2);
                    exception.printStackTrace();
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception exception1) {
            logger.error("Failed to load options", (Throwable)exception1);
        }
        this.loadOfOptions();
    }

    private float parseFloat(String p_74305_1_) {
        return p_74305_1_.equals("true") ? 1.0f : (p_74305_1_.equals("false") ? 0.0f : Float.parseFloat(p_74305_1_));
    }

    public void saveOptions() {
        Object object;
        if (Reflector.FMLClientHandler.exists() && (object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0])) != null && Reflector.callBoolean(object, Reflector.FMLClientHandler_isLoading, new Object[0])) {
            return;
        }
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
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
            for (KeyBinding keyBinding : this.keyBindings) {
                printwriter.println("key_" + keyBinding.getKeyDescription() + ":" + keyBinding.getKeyCode());
            }
            for (SoundCategory soundCategory : SoundCategory.values()) {
                printwriter.println("soundCategory_" + soundCategory.getCategoryName() + ":" + this.getSoundLevel(soundCategory));
            }
            for (EnumPlayerModelParts enumPlayerModelParts : EnumPlayerModelParts.values()) {
                printwriter.println("modelPart_" + enumPlayerModelParts.getPartName() + ":" + this.setModelParts.contains((Object)enumPlayerModelParts));
            }
            printwriter.close();
        }
        catch (Exception exception) {
            logger.error("Failed to save options", (Throwable)exception);
        }
        this.saveOfOptions();
        this.sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory p_151438_1_) {
        return this.mapSoundLevels.containsKey((Object)p_151438_1_) ? ((Float)this.mapSoundLevels.get((Object)p_151438_1_)).floatValue() : 1.0f;
    }

    public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_) {
        this.mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
        this.mapSoundLevels.put(p_151439_1_, Float.valueOf(p_151439_2_));
    }

    public void sendSettingsToServer() {
        if (this.mc.thePlayer != null) {
            int i2 = 0;
            for (Object enumplayermodelparts : this.setModelParts) {
                i2 |= ((EnumPlayerModelParts)((Object)enumplayermodelparts)).getPartMask();
            }
            this.mc.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i2));
        }
    }

    public Set getModelParts() {
        return ImmutableSet.copyOf(this.setModelParts);
    }

    public void setModelPartEnabled(EnumPlayerModelParts p_178878_1_, boolean p_178878_2_) {
        if (p_178878_2_) {
            this.setModelParts.add(p_178878_1_);
        } else {
            this.setModelParts.remove((Object)p_178878_1_);
        }
        this.sendSettingsToServer();
    }

    public void switchModelPartEnabled(EnumPlayerModelParts p_178877_1_) {
        if (!this.getModelParts().contains((Object)p_178877_1_)) {
            this.setModelParts.add(p_178877_1_);
        } else {
            this.setModelParts.remove((Object)p_178877_1_);
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
        if (p_setOptionFloatValueOF_1_ == Options.CLOUD_HEIGHT) {
            this.ofCloudsHeight = p_setOptionFloatValueOF_2_;
            this.mc.renderGlobal.resetClouds();
        }
        if (p_setOptionFloatValueOF_1_ == Options.AO_LEVEL) {
            this.ofAoLevel = p_setOptionFloatValueOF_2_;
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionFloatValueOF_1_ == Options.AA_LEVEL) {
            int[] aint = new int[]{0, 2, 4, 6, 8, 12, 16};
            this.ofAaLevel = 0;
            int i2 = (int)p_setOptionFloatValueOF_2_;
            for (int j2 = 0; j2 < aint.length; ++j2) {
                if (i2 < aint[j2]) continue;
                this.ofAaLevel = aint[j2];
            }
            this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
        }
        if (p_setOptionFloatValueOF_1_ == Options.AF_LEVEL) {
            int k2 = (int)p_setOptionFloatValueOF_2_;
            this.ofAfLevel = 1;
            while (this.ofAfLevel * 2 <= k2) {
                this.ofAfLevel *= 2;
            }
            this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
            this.mc.refreshResources();
        }
        if (p_setOptionFloatValueOF_1_ == Options.MIPMAP_TYPE) {
            int l2 = (int)p_setOptionFloatValueOF_2_;
            this.ofMipmapType = Config.limit(l2, 0, 3);
            this.mc.refreshResources();
        }
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
            ++this.ofTrees;
            if (this.ofTrees > 2) {
                this.ofTrees = 0;
            }
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
            if (this.ofAnimatedWater > 2) {
                this.ofAnimatedWater = 0;
            }
        }
        if (p_setOptionValueOF_1_ == Options.ANIMATED_LAVA) {
            ++this.ofAnimatedLava;
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
            this.ofAutoSaveTicks *= 10;
            if (this.ofAutoSaveTicks > 40000) {
                this.ofAutoSaveTicks = 40;
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
            if (this.ofConnectedTextures != 2) {
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
        if (p_setOptionValueOF_1_ == Options.CHUNK_LOADING) {
            ++this.ofChunkLoading;
            if (this.ofChunkLoading > 2) {
                this.ofChunkLoading = 0;
            }
            this.updateChunkLoading();
        }
        if (p_setOptionValueOF_1_ == Options.CHUNK_UPDATES_DYNAMIC) {
            boolean bl = this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
        }
        if (p_setOptionValueOF_1_ == Options.TIME) {
            ++this.ofTime;
            if (this.ofTime > 3) {
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
            CustomColorizer.updateUseDefaultColorMultiplier();
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.RANDOM_MOBS) {
            this.ofRandomMobs = !this.ofRandomMobs;
            RandomMobs.resetTextures();
        }
        if (p_setOptionValueOF_1_ == Options.SMOOTH_BIOMES) {
            this.ofSmoothBiomes = !this.ofSmoothBiomes;
            CustomColorizer.updateUseDefaultColorMultiplier();
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.CUSTOM_FONTS) {
            this.ofCustomFonts = !this.ofCustomFonts;
            this.mc.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
            this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
        }
        if (p_setOptionValueOF_1_ == Options.CUSTOM_COLORS) {
            this.ofCustomColors = !this.ofCustomColors;
            CustomColorizer.update();
            this.mc.renderGlobal.loadRenderers();
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
        if (p_setOptionValueOF_1_ == Options.FAST_MATH) {
            MathHelper.fastMath = this.ofFastMath = !this.ofFastMath;
        }
        if (p_setOptionValueOF_1_ == Options.FAST_RENDER) {
            this.ofFastRender = false;
            Config.updateFramebufferSize();
        }
        if (p_setOptionValueOF_1_ == Options.TRANSLUCENT_BLOCKS) {
            this.ofTranslucentBlocks = this.ofTranslucentBlocks == 0 ? 1 : (this.ofTranslucentBlocks == 1 ? 2 : (this.ofTranslucentBlocks == 2 ? 0 : 0));
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.LAZY_CHUNK_LOADING) {
            this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
            Config.updateAvailableProcessors();
            if (!Config.isSingleProcessor()) {
                this.ofLazyChunkLoading = false;
            }
            this.mc.renderGlobal.loadRenderers();
        }
        if (p_setOptionValueOF_1_ == Options.FULLSCREEN_MODE) {
            int i2;
            List<String> list = Arrays.asList(Config.getFullscreenModes());
            this.ofFullscreenMode = this.ofFullscreenMode.equals(DEFAULT_STR) ? list.get(0) : ((i2 = list.indexOf(this.ofFullscreenMode)) < 0 ? DEFAULT_STR : (++i2 >= list.size() ? DEFAULT_STR : list.get(i2)));
        }
        if (p_setOptionValueOF_1_ == Options.HELD_ITEM_TOOLTIPS) {
            this.heldItemTooltips = !this.heldItemTooltips;
        }
    }

    private String getKeyBindingOF(Options p_getKeyBindingOF_1_) {
        String s2 = I18n.format(p_getKeyBindingOF_1_.getEnumString(), new Object[0]) + ": ";
        if (s2 == null) {
            s2 = p_getKeyBindingOF_1_.getEnumString();
        }
        if (p_getKeyBindingOF_1_ == Options.RENDER_DISTANCE) {
            int k2 = (int)this.getOptionFloatValue(p_getKeyBindingOF_1_);
            String s1 = "Tiny";
            int i2 = 2;
            if (k2 >= 4) {
                s1 = "Short";
                i2 = 4;
            }
            if (k2 >= 8) {
                s1 = "Normal";
                i2 = 8;
            }
            if (k2 >= 16) {
                s1 = "Far";
                i2 = 16;
            }
            if (k2 >= 32) {
                s1 = "Extreme";
                i2 = 32;
            }
            int j2 = this.renderDistanceChunks - i2;
            String s22 = s1;
            if (j2 > 0) {
                s22 = s1 + "+";
            }
            return s2 + k2 + " " + s22 + "";
        }
        if (p_getKeyBindingOF_1_ == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    return s2 + "Fast";
                }
                case 2: {
                    return s2 + "Fancy";
                }
                case 3: {
                    return s2 + "OFF";
                }
            }
            return s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.FOG_START) {
            return s2 + this.ofFogStart;
        }
        if (p_getKeyBindingOF_1_ == Options.MIPMAP_TYPE) {
            switch (this.ofMipmapType) {
                case 0: {
                    return s2 + "Nearest";
                }
                case 1: {
                    return s2 + "Linear";
                }
                case 2: {
                    return s2 + "Bilinear";
                }
                case 3: {
                    return s2 + "Trilinear";
                }
            }
            return s2 + "Nearest";
        }
        if (p_getKeyBindingOF_1_ == Options.SMOOTH_FPS) {
            return this.ofSmoothFps ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.SMOOTH_WORLD) {
            return this.ofSmoothWorld ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.CLOUDS) {
            switch (this.ofClouds) {
                case 1: {
                    return s2 + "Fast";
                }
                case 2: {
                    return s2 + "Fancy";
                }
                case 3: {
                    return s2 + "OFF";
                }
            }
            return s2 + DEFAULT_STR;
        }
        if (p_getKeyBindingOF_1_ == Options.TREES) {
            switch (this.ofTrees) {
                case 1: {
                    return s2 + "Fast";
                }
                case 2: {
                    return s2 + "Fancy";
                }
            }
            return s2 + DEFAULT_STR;
        }
        if (p_getKeyBindingOF_1_ == Options.DROPPED_ITEMS) {
            switch (this.ofDroppedItems) {
                case 1: {
                    return s2 + "Fast";
                }
                case 2: {
                    return s2 + "Fancy";
                }
            }
            return s2 + DEFAULT_STR;
        }
        if (p_getKeyBindingOF_1_ == Options.RAIN) {
            switch (this.ofRain) {
                case 1: {
                    return s2 + "Fast";
                }
                case 2: {
                    return s2 + "Fancy";
                }
                case 3: {
                    return s2 + "OFF";
                }
            }
            return s2 + DEFAULT_STR;
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_WATER) {
            switch (this.ofAnimatedWater) {
                case 1: {
                    return s2 + "Dynamic";
                }
                case 2: {
                    return s2 + "OFF";
                }
            }
            return s2 + "ON";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_LAVA) {
            switch (this.ofAnimatedLava) {
                case 1: {
                    return s2 + "Dynamic";
                }
                case 2: {
                    return s2 + "OFF";
                }
            }
            return s2 + "ON";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_FIRE) {
            return this.ofAnimatedFire ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_PORTAL) {
            return this.ofAnimatedPortal ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_REDSTONE) {
            return this.ofAnimatedRedstone ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_EXPLOSION) {
            return this.ofAnimatedExplosion ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_FLAME) {
            return this.ofAnimatedFlame ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_SMOKE) {
            return this.ofAnimatedSmoke ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.VOID_PARTICLES) {
            return this.ofVoidParticles ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.WATER_PARTICLES) {
            return this.ofWaterParticles ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.PORTAL_PARTICLES) {
            return this.ofPortalParticles ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.POTION_PARTICLES) {
            return this.ofPotionParticles ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.FIREWORK_PARTICLES) {
            return this.ofFireworkParticles ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.DRIPPING_WATER_LAVA) {
            return this.ofDrippingWaterLava ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_TERRAIN) {
            return this.ofAnimatedTerrain ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.ANIMATED_TEXTURES) {
            return this.ofAnimatedTextures ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.RAIN_SPLASH) {
            return this.ofRainSplash ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.LAGOMETER) {
            return this.ofLagometer ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.SHOW_FPS) {
            return this.ofShowFps ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.AUTOSAVE_TICKS) {
            return this.ofAutoSaveTicks <= 40 ? s2 + "Default (2s)" : (this.ofAutoSaveTicks <= 400 ? s2 + "20s" : (this.ofAutoSaveTicks <= 4000 ? s2 + "3min" : s2 + "30min"));
        }
        if (p_getKeyBindingOF_1_ == Options.BETTER_GRASS) {
            switch (this.ofBetterGrass) {
                case 1: {
                    return s2 + "Fast";
                }
                case 2: {
                    return s2 + "Fancy";
                }
            }
            return s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.CONNECTED_TEXTURES) {
            switch (this.ofConnectedTextures) {
                case 1: {
                    return s2 + "Fast";
                }
                case 2: {
                    return s2 + "Fancy";
                }
            }
            return s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.WEATHER) {
            return this.ofWeather ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.SKY) {
            return this.ofSky ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.STARS) {
            return this.ofStars ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.SUN_MOON) {
            return this.ofSunMoon ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.VIGNETTE) {
            switch (this.ofVignette) {
                case 1: {
                    return s2 + "Fast";
                }
                case 2: {
                    return s2 + "Fancy";
                }
            }
            return s2 + DEFAULT_STR;
        }
        if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES) {
            return s2 + this.ofChunkUpdates;
        }
        if (p_getKeyBindingOF_1_ == Options.CHUNK_LOADING) {
            return this.ofChunkLoading == 1 ? s2 + "Smooth" : (this.ofChunkLoading == 2 ? s2 + "Multi-Core" : s2 + DEFAULT_STR);
        }
        if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES_DYNAMIC) {
            return this.ofChunkUpdatesDynamic ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.TIME) {
            return this.ofTime == 1 ? s2 + "Day Only" : (this.ofTime == 3 ? s2 + "Night Only" : s2 + DEFAULT_STR);
        }
        if (p_getKeyBindingOF_1_ == Options.CLEAR_WATER) {
            return this.ofClearWater ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.AA_LEVEL) {
            String s3 = "";
            if (this.ofAaLevel != Config.getAntialiasingLevel()) {
                s3 = " (restart)";
            }
            return this.ofAaLevel == 0 ? s2 + "OFF" + s3 : s2 + this.ofAaLevel + s3;
        }
        if (p_getKeyBindingOF_1_ == Options.AF_LEVEL) {
            return this.ofAfLevel == 1 ? s2 + "OFF" : s2 + this.ofAfLevel;
        }
        if (p_getKeyBindingOF_1_ == Options.PROFILER) {
            return this.ofProfiler ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.BETTER_SNOW) {
            return this.ofBetterSnow ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.SWAMP_COLORS) {
            return this.ofSwampColors ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.RANDOM_MOBS) {
            return this.ofRandomMobs ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.SMOOTH_BIOMES) {
            return this.ofSmoothBiomes ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_FONTS) {
            return this.ofCustomFonts ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_COLORS) {
            return this.ofCustomColors ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.CUSTOM_SKY) {
            return this.ofCustomSky ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.SHOW_CAPES) {
            return this.ofShowCapes ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.NATURAL_TEXTURES) {
            return this.ofNaturalTextures ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.FAST_MATH) {
            return this.ofFastMath ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.FAST_RENDER) {
            return "Fast Render: Not available";
        }
        if (p_getKeyBindingOF_1_ == Options.TRANSLUCENT_BLOCKS) {
            return this.ofTranslucentBlocks == 1 ? s2 + "Fast" : (this.ofTranslucentBlocks == 2 ? s2 + "Fancy" : s2 + DEFAULT_STR);
        }
        if (p_getKeyBindingOF_1_ == Options.LAZY_CHUNK_LOADING) {
            return this.ofLazyChunkLoading ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.FULLSCREEN_MODE) {
            return s2 + this.ofFullscreenMode;
        }
        if (p_getKeyBindingOF_1_ == Options.HELD_ITEM_TOOLTIPS) {
            return this.heldItemTooltips ? s2 + "ON" : s2 + "OFF";
        }
        if (p_getKeyBindingOF_1_ == Options.FRAMERATE_LIMIT) {
            float f2 = this.getOptionFloatValue(p_getKeyBindingOF_1_);
            return f2 == 0.0f ? s2 + "VSync" : (f2 == p_getKeyBindingOF_1_.valueMax ? s2 + I18n.format("options.framerateLimit.max", new Object[0]) : s2 + (int)f2 + " fps");
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
            BufferedReader bufferedreader = new BufferedReader(new FileReader(file1));
            String s2 = "";
            while ((s2 = bufferedreader.readLine()) != null) {
                try {
                    String[] astring = s2.split(":");
                    if (astring[0].equals("ofRenderDistanceChunks") && astring.length >= 2) {
                        this.renderDistanceChunks = Integer.valueOf(astring[1]);
                        this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 32);
                    }
                    if (astring[0].equals("ofFogType") && astring.length >= 2) {
                        this.ofFogType = Integer.valueOf(astring[1]);
                        this.ofFogType = Config.limit(this.ofFogType, 1, 3);
                    }
                    if (astring[0].equals("ofFogStart") && astring.length >= 2) {
                        this.ofFogStart = Float.valueOf(astring[1]).floatValue();
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
                        this.ofAoLevel = Float.valueOf(astring[1]).floatValue();
                        this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0f, 1.0f);
                    }
                    if (astring[0].equals("ofClouds") && astring.length >= 2) {
                        this.ofClouds = Integer.valueOf(astring[1]);
                        this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                        this.updateRenderClouds();
                    }
                    if (astring[0].equals("ofCloudsHeight") && astring.length >= 2) {
                        this.ofCloudsHeight = Float.valueOf(astring[1]).floatValue();
                        this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0f, 1.0f);
                    }
                    if (astring[0].equals("ofTrees") && astring.length >= 2) {
                        this.ofTrees = Integer.valueOf(astring[1]);
                        this.ofTrees = Config.limit(this.ofTrees, 0, 2);
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
                    if (astring[0].equals("ofChunkLoading") && astring.length >= 2) {
                        this.ofChunkLoading = Integer.valueOf(astring[1]);
                        this.ofChunkLoading = Config.limit(this.ofChunkLoading, 0, 2);
                        this.updateChunkLoading();
                    }
                    if (astring[0].equals("ofChunkUpdatesDynamic") && astring.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofTime") && astring.length >= 2) {
                        this.ofTime = Integer.valueOf(astring[1]);
                        this.ofTime = Config.limit(this.ofTime, 0, 3);
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
                    if (astring[0].equals("ofRandomMobs") && astring.length >= 2) {
                        this.ofRandomMobs = Boolean.valueOf(astring[1]);
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
                    if (astring[0].equals("ofCustomSky") && astring.length >= 2) {
                        this.ofCustomSky = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofShowCapes") && astring.length >= 2) {
                        this.ofShowCapes = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofNaturalTextures") && astring.length >= 2) {
                        this.ofNaturalTextures = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofLazyChunkLoading") && astring.length >= 2) {
                        this.ofLazyChunkLoading = Boolean.valueOf(astring[1]);
                    }
                    if (astring[0].equals("ofFullscreenMode") && astring.length >= 2) {
                        this.ofFullscreenMode = astring[1];
                    }
                    if (astring[0].equals("ofFastMath") && astring.length >= 2) {
                        MathHelper.fastMath = this.ofFastMath = Boolean.valueOf(astring[1]).booleanValue();
                    }
                    if (astring[0].equals("ofFastRender") && astring.length >= 2) {
                        this.ofFastRender = false;
                    }
                    if (astring[0].equals("ofTranslucentBlocks") && astring.length >= 2) {
                        this.ofTranslucentBlocks = Integer.valueOf(astring[1]);
                        this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
                    }
                    if (!astring[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) continue;
                    this.ofKeyBindZoom.setKeyCode(Integer.parseInt(astring[1]));
                }
                catch (Exception exception) {
                    Config.dbg("Skipping bad option: " + s2);
                    exception.printStackTrace();
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception exception1) {
            Config.warn("Failed to load options");
            exception1.printStackTrace();
        }
    }

    public void saveOfOptions() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFileOF));
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
            printwriter.println("ofChunkLoading:" + this.ofChunkLoading);
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
            printwriter.println("ofCustomSky:" + this.ofCustomSky);
            printwriter.println("ofShowCapes:" + this.ofShowCapes);
            printwriter.println("ofNaturalTextures:" + this.ofNaturalTextures);
            printwriter.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
            printwriter.println("ofFullscreenMode:" + this.ofFullscreenMode);
            printwriter.println("ofFastMath:" + this.ofFastMath);
            printwriter.println("ofFastRender:" + this.ofFastRender);
            printwriter.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
            printwriter.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
            printwriter.close();
        }
        catch (Exception exception) {
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
        this.allowBlockAlternatives = true;
        this.forceUnicodeFont = false;
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
        this.ofMipmapType = 0;
        this.ofOcclusionFancy = false;
        this.ofSmoothFps = false;
        Config.updateAvailableProcessors();
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = Config.isSingleProcessor();
        this.ofFastMath = false;
        this.ofFastRender = false;
        this.ofTranslucentBlocks = 0;
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
        this.ofChunkLoading = 0;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = DEFAULT_STR;
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
        this.ofFireworkParticles = true;
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

    public void setAllAnimations(boolean p_setAllAnimations_1_) {
        int i2;
        this.ofAnimatedWater = i2 = p_setAllAnimations_1_ ? 0 : 2;
        this.ofAnimatedLava = i2;
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

    public static enum Options {
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
        FOG_FANCY("FOG", 999, "Fog", false, false),
        FOG_START("", 999, "Fog Start", false, false),
        MIPMAP_TYPE("", 999, "Mipmap Type", true, false, 0.0f, 3.0f, 1.0f),
        SMOOTH_FPS("", 999, "Smooth FPS", false, false),
        CLOUDS("", 999, "Clouds", false, false),
        CLOUD_HEIGHT("", 999, "Cloud Height", true, false),
        TREES("", 999, "Trees", false, false),
        RAIN("", 999, "Rain & Snow", false, false),
        ANIMATED_WATER("", 999, "Water Animated", false, false),
        ANIMATED_LAVA("", 999, "Lava Animated", false, false),
        ANIMATED_FIRE("", 999, "Fire Animated", false, false),
        ANIMATED_PORTAL("", 999, "Portal Animated", false, false),
        AO_LEVEL("", 999, "Smooth Lighting Level", true, false),
        LAGOMETER("", 999, "Lagometer", false, false),
        SHOW_FPS("", 999, "Show FPS", false, false),
        AUTOSAVE_TICKS("", 999, "Autosave", false, false),
        BETTER_GRASS("", 999, "Better Grass", false, false),
        ANIMATED_REDSTONE("", 999, "Redstone Animated", false, false),
        ANIMATED_EXPLOSION("", 999, "Explosion Animated", false, false),
        ANIMATED_FLAME("", 999, "Flame Animated", false, false),
        ANIMATED_SMOKE("", 999, "Smoke Animated", false, false),
        WEATHER("", 999, "Weather", false, false),
        SKY("", 999, "Sky", false, false),
        STARS("", 999, "Stars", false, false),
        SUN_MOON("", 999, "Sun & Moon", false, false),
        VIGNETTE("", 999, "Vignette", false, false),
        CHUNK_UPDATES("", 999, "Chunk Updates", false, false),
        CHUNK_UPDATES_DYNAMIC("", 999, "Dynamic Updates", false, false),
        TIME("", 999, "Time", false, false),
        CLEAR_WATER("", 999, "Clear Water", false, false),
        SMOOTH_WORLD("", 999, "Smooth World", false, false),
        VOID_PARTICLES("", 999, "Void Particles", false, false),
        WATER_PARTICLES("", 999, "Water Particles", false, false),
        RAIN_SPLASH("", 999, "Rain Splash", false, false),
        PORTAL_PARTICLES("", 999, "Portal Particles", false, false),
        POTION_PARTICLES("", 999, "Potion Particles", false, false),
        FIREWORK_PARTICLES("", 999, "Firework Particles", false, false),
        PROFILER("", 999, "Debug Profiler", false, false),
        DRIPPING_WATER_LAVA("", 999, "Dripping Water/Lava", false, false),
        BETTER_SNOW("", 999, "Better Snow", false, false),
        FULLSCREEN_MODE("", 999, "Fullscreen Mode", false, false),
        ANIMATED_TERRAIN("", 999, "Terrain Animated", false, false),
        SWAMP_COLORS("", 999, "Swamp Colors", false, false),
        RANDOM_MOBS("", 999, "Random Mobs", false, false),
        SMOOTH_BIOMES("", 999, "Smooth Biomes", false, false),
        CUSTOM_FONTS("", 999, "Custom Fonts", false, false),
        CUSTOM_COLORS("", 999, "Custom Colors", false, false),
        SHOW_CAPES("", 999, "Show Capes", false, false),
        CONNECTED_TEXTURES("", 999, "Connected Textures", false, false),
        AA_LEVEL("", 999, "Antialiasing", true, false, 0.0f, 16.0f, 1.0f),
        AF_LEVEL("", 999, "Anisotropic Filtering", true, false, 1.0f, 16.0f, 1.0f),
        ANIMATED_TEXTURES("", 999, "Textures Animated", false, false),
        NATURAL_TEXTURES("", 999, "Natural Textures", false, false),
        CHUNK_LOADING("", 999, "Chunk Loading", false, false),
        HELD_ITEM_TOOLTIPS("", 999, "Held Item Tooltips", false, false),
        DROPPED_ITEMS("", 999, "Dropped Items", false, false),
        LAZY_CHUNK_LOADING("", 999, "Lazy Chunk Loading", false, false),
        CUSTOM_SKY("", 999, "Custom Sky", false, false),
        FAST_MATH("", 999, "Fast Math", false, false),
        FAST_RENDER("", 999, "Fast Render", false, false),
        TRANSLUCENT_BLOCKS("", 999, "Translucent Blocks", false, false);

        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private float valueMin;
        private float valueMax;
        private static final Options[] $VALUES;
        private static final String __OBFID = "CL_00000653";

        public static Options getEnumOptions(int p_74379_0_) {
            for (Options gamesettings$options : Options.values()) {
                if (gamesettings$options.returnEnumOrdinal() != p_74379_0_) continue;
                return gamesettings$options;
            }
            return null;
        }

        private Options(String p_i0_3_, int p_i0_4_, String p_i0_5_, boolean p_i0_6_, boolean p_i0_7_) {
            this(p_i0_3_, p_i0_4_, p_i0_5_, p_i0_6_, p_i0_7_, 0.0f, 1.0f, 0.0f);
        }

        private Options(String p_i1_3_, int p_i1_4_, String p_i1_5_, boolean p_i1_6_, boolean p_i1_7_, float p_i1_8_, float p_i1_9_, float p_i1_10_) {
            this.enumString = p_i1_5_;
            this.enumFloat = p_i1_6_;
            this.enumBoolean = p_i1_7_;
            this.valueMin = p_i1_8_;
            this.valueMax = p_i1_9_;
            this.valueStep = p_i1_10_;
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
            return MathHelper.clamp_float((this.snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0f, 1.0f);
        }

        public float denormalizeValue(float p_148262_1_) {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0f, 1.0f));
        }

        public float snapToStepClamp(float p_148268_1_) {
            p_148268_1_ = this.snapToStep(p_148268_1_);
            return MathHelper.clamp_float(p_148268_1_, this.valueMin, this.valueMax);
        }

        protected float snapToStep(float p_148264_1_) {
            if (this.valueStep > 0.0f) {
                p_148264_1_ = this.valueStep * (float)Math.round(p_148264_1_ / this.valueStep);
            }
            return p_148264_1_;
        }

        static {
            $VALUES = new Options[]{INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO, ENTITY_SHADOWS};
        }
    }
}

