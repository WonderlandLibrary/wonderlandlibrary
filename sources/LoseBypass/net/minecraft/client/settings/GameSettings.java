/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
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
import java.util.Iterator;
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
    public List<String> resourcePacks = Lists.newArrayList();
    public List<String> field_183018_l = Lists.newArrayList();
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
    private final Set<EnumPlayerModelParts> setModelParts = Sets.newHashSet(EnumPlayerModelParts.values());
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
    public boolean field_181150_U = true;
    public boolean field_181151_V = true;
    public boolean field_183509_X = true;
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
    public KeyBinding[] keyBindings = ArrayUtils.addAll(new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, this.keyBindsHotbar);
    protected Minecraft mc;
    private File optionsFile;
    public EnumDifficulty difficulty = EnumDifficulty.NORMAL;
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

    public GameSettings(Minecraft mcIn, File p_i46326_2_) {
        this.mc = mcIn;
        this.optionsFile = new File(p_i46326_2_, "options.txt");
        if (mcIn.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
            Options.RENDER_DISTANCE.setValueMax(32.0f);
        } else {
            Options.RENDER_DISTANCE.setValueMax(16.0f);
        }
        this.renderDistanceChunks = mcIn.isJava64bit() ? 12 : 8;
        this.loadOptions();
    }

    public GameSettings() {
    }

    public static String getKeyDisplayString(int p_74298_0_) {
        String string;
        if (p_74298_0_ < 0) {
            string = I18n.format("key.mouseButton", p_74298_0_ + 101);
            return string;
        }
        if (p_74298_0_ < 256) {
            string = Keyboard.getKeyName((int)p_74298_0_);
            return string;
        }
        string = String.format("%c", Character.valueOf((char)(p_74298_0_ - 256))).toUpperCase();
        return string;
    }

    public static boolean isKeyDown(KeyBinding p_100015_0_) {
        boolean bl;
        if (p_100015_0_.getKeyCode() == 0) {
            return false;
        }
        if (p_100015_0_.getKeyCode() < 0) {
            bl = Mouse.isButtonDown((int)(p_100015_0_.getKeyCode() + 100));
            return bl;
        }
        bl = Keyboard.isKeyDown((int)p_100015_0_.getKeyCode());
        return bl;
    }

    public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_) {
        p_151440_1_.setKeyCode(p_151440_2_);
        this.saveOptions();
    }

    public void setOptionFloatValue(Options p_74304_1_, float p_74304_2_) {
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
            int i = this.mipmapLevels;
            this.mipmapLevels = (int)p_74304_2_;
            if ((float)i != p_74304_2_) {
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
        if (p_74304_1_ != Options.STREAM_FPS) return;
        this.streamFps = p_74304_2_;
    }

    public void setOptionValue(Options p_74306_1_, int p_74306_2_) {
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
            Display.setVSyncEnabled((boolean)this.enableVsync);
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
            boolean bl = this.field_181151_V = !this.field_181151_V;
        }
        if (p_74306_1_ == Options.REALMS_NOTIFICATIONS) {
            this.field_183509_X = !this.field_183509_X;
        }
        this.saveOptions();
    }

    public float getOptionFloatValue(Options p_74296_1_) {
        float f;
        if (p_74296_1_ == Options.FOV) {
            f = this.fovSetting;
            return f;
        }
        if (p_74296_1_ == Options.GAMMA) {
            f = this.gammaSetting;
            return f;
        }
        if (p_74296_1_ == Options.SATURATION) {
            f = this.saturation;
            return f;
        }
        if (p_74296_1_ == Options.SENSITIVITY) {
            f = this.mouseSensitivity;
            return f;
        }
        if (p_74296_1_ == Options.CHAT_OPACITY) {
            f = this.chatOpacity;
            return f;
        }
        if (p_74296_1_ == Options.CHAT_HEIGHT_FOCUSED) {
            f = this.chatHeightFocused;
            return f;
        }
        if (p_74296_1_ == Options.CHAT_HEIGHT_UNFOCUSED) {
            f = this.chatHeightUnfocused;
            return f;
        }
        if (p_74296_1_ == Options.CHAT_SCALE) {
            f = this.chatScale;
            return f;
        }
        if (p_74296_1_ == Options.CHAT_WIDTH) {
            f = this.chatWidth;
            return f;
        }
        if (p_74296_1_ == Options.FRAMERATE_LIMIT) {
            f = this.limitFramerate;
            return f;
        }
        if (p_74296_1_ == Options.MIPMAP_LEVELS) {
            f = this.mipmapLevels;
            return f;
        }
        if (p_74296_1_ == Options.RENDER_DISTANCE) {
            f = this.renderDistanceChunks;
            return f;
        }
        if (p_74296_1_ == Options.STREAM_BYTES_PER_PIXEL) {
            f = this.streamBytesPerPixel;
            return f;
        }
        if (p_74296_1_ == Options.STREAM_VOLUME_MIC) {
            f = this.streamMicVolume;
            return f;
        }
        if (p_74296_1_ == Options.STREAM_VOLUME_SYSTEM) {
            f = this.streamGameVolume;
            return f;
        }
        if (p_74296_1_ == Options.STREAM_KBPS) {
            f = this.streamKbps;
            return f;
        }
        if (p_74296_1_ != Options.STREAM_FPS) return 0.0f;
        f = this.streamFps;
        return f;
    }

    public boolean getOptionOrdinalValue(Options p_74308_1_) {
        switch (2.$SwitchMap$net$minecraft$client$settings$GameSettings$Options[p_74308_1_.ordinal()]) {
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
                return this.field_181151_V;
            }
            case 18: {
                return this.field_183509_X;
            }
        }
        return false;
    }

    private static String getTranslation(String[] p_74299_0_, int p_74299_1_) {
        if (p_74299_1_ >= 0) {
            if (p_74299_1_ < p_74299_0_.length) return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
        }
        p_74299_1_ = 0;
        return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
    }

    public String getKeyBinding(Options p_74297_1_) {
        String s = I18n.format(p_74297_1_.getEnumString(), new Object[0]) + ": ";
        if (p_74297_1_.getEnumFloat()) {
            String string;
            float f1 = this.getOptionFloatValue(p_74297_1_);
            float f = p_74297_1_.normalizeValue(f1);
            if (p_74297_1_ == Options.SENSITIVITY) {
                if (f == 0.0f) {
                    string = s + I18n.format("options.sensitivity.min", new Object[0]);
                    return string;
                }
                if (f == 1.0f) {
                    string = s + I18n.format("options.sensitivity.max", new Object[0]);
                    return string;
                }
                string = s + (int)(f * 200.0f) + "%";
                return string;
            }
            if (p_74297_1_ == Options.FOV) {
                if (f1 == 70.0f) {
                    string = s + I18n.format("options.fov.min", new Object[0]);
                    return string;
                }
                if (f1 == 110.0f) {
                    string = s + I18n.format("options.fov.max", new Object[0]);
                    return string;
                }
                string = s + (int)f1;
                return string;
            }
            if (p_74297_1_ == Options.FRAMERATE_LIMIT) {
                if (f1 == p_74297_1_.valueMax) {
                    string = s + I18n.format("options.framerateLimit.max", new Object[0]);
                    return string;
                }
                string = s + (int)f1 + " fps";
                return string;
            }
            if (p_74297_1_ == Options.RENDER_CLOUDS) {
                if (f1 == p_74297_1_.valueMin) {
                    string = s + I18n.format("options.cloudHeight.min", new Object[0]);
                    return string;
                }
                string = s + ((int)f1 + 128);
                return string;
            }
            if (p_74297_1_ == Options.GAMMA) {
                if (f == 0.0f) {
                    string = s + I18n.format("options.gamma.min", new Object[0]);
                    return string;
                }
                if (f == 1.0f) {
                    string = s + I18n.format("options.gamma.max", new Object[0]);
                    return string;
                }
                string = s + "+" + (int)(f * 100.0f) + "%";
                return string;
            }
            if (p_74297_1_ == Options.SATURATION) {
                string = s + (int)(f * 400.0f) + "%";
                return string;
            }
            if (p_74297_1_ == Options.CHAT_OPACITY) {
                string = s + (int)(f * 90.0f + 10.0f) + "%";
                return string;
            }
            if (p_74297_1_ == Options.CHAT_HEIGHT_UNFOCUSED) {
                string = s + GuiNewChat.calculateChatboxHeight(f) + "px";
                return string;
            }
            if (p_74297_1_ == Options.CHAT_HEIGHT_FOCUSED) {
                string = s + GuiNewChat.calculateChatboxHeight(f) + "px";
                return string;
            }
            if (p_74297_1_ == Options.CHAT_WIDTH) {
                string = s + GuiNewChat.calculateChatboxWidth(f) + "px";
                return string;
            }
            if (p_74297_1_ == Options.RENDER_DISTANCE) {
                string = s + (int)f1 + " chunks";
                return string;
            }
            if (p_74297_1_ == Options.MIPMAP_LEVELS) {
                if (f1 == 0.0f) {
                    string = s + I18n.format("options.off", new Object[0]);
                    return string;
                }
                string = s + (int)f1;
                return string;
            }
            if (p_74297_1_ == Options.STREAM_FPS) {
                string = s + TwitchStream.formatStreamFps(f) + " fps";
                return string;
            }
            if (p_74297_1_ == Options.STREAM_KBPS) {
                string = s + TwitchStream.formatStreamKbps(f) + " Kbps";
                return string;
            }
            if (p_74297_1_ == Options.STREAM_BYTES_PER_PIXEL) {
                string = s + String.format("%.3f bpp", Float.valueOf(TwitchStream.formatStreamBps(f)));
                return string;
            }
            if (f == 0.0f) {
                string = s + I18n.format("options.off", new Object[0]);
                return string;
            }
            string = s + (int)(f * 100.0f) + "%";
            return string;
        }
        if (p_74297_1_.getEnumBoolean()) {
            String string;
            boolean flag = this.getOptionOrdinalValue(p_74297_1_);
            if (flag) {
                string = s + I18n.format("options.on", new Object[0]);
                return string;
            }
            string = s + I18n.format("options.off", new Object[0]);
            return string;
        }
        if (p_74297_1_ == Options.GUI_SCALE) {
            return s + GameSettings.getTranslation(GUISCALES, this.guiScale);
        }
        if (p_74297_1_ == Options.CHAT_VISIBILITY) {
            return s + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
        }
        if (p_74297_1_ == Options.PARTICLES) {
            return s + GameSettings.getTranslation(PARTICLES, this.particleSetting);
        }
        if (p_74297_1_ == Options.AMBIENT_OCCLUSION) {
            return s + GameSettings.getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        if (p_74297_1_ == Options.STREAM_COMPRESSION) {
            return s + GameSettings.getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
        }
        if (p_74297_1_ == Options.STREAM_CHAT_ENABLED) {
            return s + GameSettings.getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
        }
        if (p_74297_1_ == Options.STREAM_CHAT_USER_FILTER) {
            return s + GameSettings.getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
        }
        if (p_74297_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return s + GameSettings.getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
        }
        if (p_74297_1_ == Options.RENDER_CLOUDS) {
            return s + GameSettings.getTranslation(field_181149_aW, this.clouds);
        }
        if (p_74297_1_ != Options.GRAPHICS) return s;
        if (!this.fancyGraphics) return s + I18n.format("options.graphics.fast", new Object[0]);
        return s + I18n.format("options.graphics.fancy", new Object[0]);
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.optionsFile));
            String s = "";
            this.mapSoundLevels.clear();
            block4: while (true) {
                if ((s = bufferedreader.readLine()) == null) {
                    KeyBinding.resetKeyBindingArrayAndHash();
                    bufferedreader.close();
                    return;
                }
                try {
                    String[] astring = s.split(":");
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
                    }
                    if (astring[0].equals("fboEnable")) {
                        this.fboEnable = astring[1].equals("true");
                    }
                    if (astring[0].equals("difficulty")) {
                        this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(astring[1]));
                    }
                    if (astring[0].equals("fancyGraphics")) {
                        this.fancyGraphics = astring[1].equals("true");
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
                        this.resourcePacks = (List)gson.fromJson(s.substring(s.indexOf(58) + 1), (Type)typeListString);
                        if (this.resourcePacks == null) {
                            this.resourcePacks = Lists.newArrayList();
                        }
                    }
                    if (astring[0].equals("incompatibleResourcePacks")) {
                        this.field_183018_l = (List)gson.fromJson(s.substring(s.indexOf(58) + 1), (Type)typeListString);
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
                    if (astring[0].equals("realmsNotifications")) {
                        this.field_183509_X = astring[1].equals("true");
                    }
                    for (KeyBinding keyBinding : this.keyBindings) {
                        if (!astring[0].equals("key_" + keyBinding.getKeyDescription())) continue;
                        keyBinding.setKeyCode(Integer.parseInt(astring[1]));
                    }
                    for (SoundCategory soundCategory : SoundCategory.values()) {
                        if (!astring[0].equals("soundCategory_" + soundCategory.getCategoryName())) continue;
                        this.mapSoundLevels.put(soundCategory, Float.valueOf(this.parseFloat(astring[1])));
                    }
                    EnumPlayerModelParts[] objectArray = EnumPlayerModelParts.values();
                    int n = objectArray.length;
                    int n2 = 0;
                    while (true) {
                        if (n2 >= n) continue block4;
                        EnumPlayerModelParts enumPlayerModelParts = objectArray[n2];
                        if (astring[0].equals("modelPart_" + enumPlayerModelParts.getPartName())) {
                            this.setModelPartEnabled(enumPlayerModelParts, astring[1].equals("true"));
                        }
                        ++n2;
                    }
                }
                catch (Exception var8) {
                    logger.warn("Skipping bad option: " + s);
                    continue;
                }
                break;
            }
        }
        catch (Exception exception) {
            logger.error("Failed to load options", (Throwable)exception);
        }
    }

    private float parseFloat(String p_74305_1_) {
        if (p_74305_1_.equals("true")) {
            return 1.0f;
        }
        if (p_74305_1_.equals("false")) {
            return 0.0f;
        }
        float f = Float.parseFloat(p_74305_1_);
        return f;
    }

    public void saveOptions() {
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
                    break;
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
            printwriter.println("realmsNotifications:" + this.field_183509_X);
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
        this.sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory p_151438_1_) {
        if (!this.mapSoundLevels.containsKey((Object)p_151438_1_)) return 1.0f;
        float f = this.mapSoundLevels.get((Object)p_151438_1_).floatValue();
        return f;
    }

    public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_) {
        this.mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
        this.mapSoundLevels.put(p_151439_1_, Float.valueOf(p_151439_2_));
    }

    public void sendSettingsToServer() {
        if (this.mc.thePlayer == null) return;
        int i = 0;
        Iterator<EnumPlayerModelParts> iterator = this.setModelParts.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i));
                return;
            }
            EnumPlayerModelParts enumplayermodelparts = iterator.next();
            i |= enumplayermodelparts.getPartMask();
        }
    }

    public Set<EnumPlayerModelParts> getModelParts() {
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
        if (this.renderDistanceChunks < 4) return 0;
        int n = this.clouds;
        return n;
    }

    public boolean func_181148_f() {
        return this.field_181150_U;
    }

    public static enum Options {
        INVERT_MOUSE("options.invertMouse", false, true),
        SENSITIVITY("options.sensitivity", true, false),
        FOV("options.fov", true, false, 30.0f, 110.0f, 1.0f),
        GAMMA("options.gamma", true, false),
        SATURATION("options.saturation", true, false),
        RENDER_DISTANCE("options.renderDistance", true, false, 2.0f, 16.0f, 1.0f),
        VIEW_BOBBING("options.viewBobbing", false, true),
        ANAGLYPH("options.anaglyph", false, true),
        FRAMERATE_LIMIT("options.framerateLimit", true, false, 10.0f, 260.0f, 10.0f),
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
        STREAM_BYTES_PER_PIXEL("options.stream.bytesPerPixel", true, false),
        STREAM_VOLUME_MIC("options.stream.micVolumne", true, false),
        STREAM_VOLUME_SYSTEM("options.stream.systemVolume", true, false),
        STREAM_KBPS("options.stream.kbps", true, false),
        STREAM_FPS("options.stream.fps", true, false),
        STREAM_COMPRESSION("options.stream.compression", false, false),
        STREAM_SEND_METADATA("options.stream.sendMetadata", false, true),
        STREAM_CHAT_ENABLED("options.stream.chat.enabled", false, false),
        STREAM_CHAT_USER_FILTER("options.stream.chat.userFilter", false, false),
        STREAM_MIC_TOGGLE_BEHAVIOR("options.stream.micToggleBehavior", false, false),
        BLOCK_ALTERNATIVES("options.blockAlternatives", false, true),
        REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true),
        ENTITY_SHADOWS("options.entityShadows", false, true),
        REALMS_NOTIFICATIONS("options.realmsNotifications", false, true);

        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private float valueMin;
        private float valueMax;

        public static Options getEnumOptions(int p_74379_0_) {
            Options[] optionsArray = Options.values();
            int n = optionsArray.length;
            int n2 = 0;
            while (n2 < n) {
                Options gamesettings$options = optionsArray[n2];
                if (gamesettings$options.returnEnumOrdinal() == p_74379_0_) {
                    return gamesettings$options;
                }
                ++n2;
            }
            return null;
        }

        private Options(String p_i1015_3_, boolean p_i1015_4_, boolean p_i1015_5_) {
            this(p_i1015_3_, p_i1015_4_, p_i1015_5_, 0.0f, 1.0f, 0.0f);
        }

        private Options(String p_i45004_3_, boolean p_i45004_4_, boolean p_i45004_5_, float p_i45004_6_, float p_i45004_7_, float p_i45004_8_) {
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
            if (!(this.valueStep > 0.0f)) return p_148264_1_;
            return this.valueStep * (float)Math.round(p_148264_1_ / this.valueStep);
        }
    }
}

