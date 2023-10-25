/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.gson.Gson
 *  org.apache.commons.lang3.ArrayUtils
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
    public int ambientOcclusion = 2;
    public KeyBinding keyBindSpectatorOutlines;
    public float mouseSensitivity = 0.5f;
    public float gammaSetting;
    public boolean anaglyph;
    public KeyBinding keyBindLeft;
    private static final String[] field_181149_aW;
    public boolean fancyGraphics = true;
    public KeyBinding keyBindStreamToggleMic;
    public KeyBinding keyBindSprint;
    public boolean invertMouse;
    public List<String> resourcePacks = Lists.newArrayList();
    public float chatScale = 1.0f;
    private static final String[] GUISCALES;
    public KeyBinding keyBindDrop;
    private static final ParameterizedType typeListString;
    public KeyBinding keyBindCommand;
    public KeyBinding keyBindSneak;
    protected Minecraft mc;
    private static final String[] STREAM_COMPRESSIONS;
    public float streamKbps = 0.5412844f;
    public int streamMicToggleBehavior = 0;
    public boolean field_181151_V = true;
    public KeyBinding keyBindBack;
    public boolean enableVsync = true;
    public float chatOpacity = 1.0f;
    public String language = "en_US";
    public int renderDistanceChunks = -1;
    public boolean field_181150_U = true;
    public boolean allowBlockAlternatives = true;
    public boolean viewBobbing = true;
    public boolean pauseOnLostFocus = true;
    private static final String[] PARTICLES;
    public boolean debugCamEnable;
    private static final Gson gson;
    public int guiScale;
    public int streamCompression = 1;
    public KeyBinding keyBindInventory;
    private File optionsFile;
    public KeyBinding keyBindRight;
    public int streamChatEnabled = 0;
    public boolean chatLinks = true;
    public boolean smoothCamera;
    public boolean hideGUI;
    public KeyBinding keyBindScreenshot;
    public int thirdPersonView;
    private static final String[] STREAM_MIC_MODES;
    public boolean touchscreen;
    public KeyBinding keyBindAttack;
    public boolean chatLinksPrompt = true;
    private static final Logger logger;
    public boolean heldItemTooltips = true;
    private static final String[] STREAM_CHAT_FILTER_MODES;
    public int streamChatUserFilter = 0;
    public int overrideHeight;
    public KeyBinding keyBindJump;
    public KeyBinding keyBindStreamPauseUnpause;
    public KeyBinding keyBindSmoothCamera;
    public float streamFps = 0.31690142f;
    public boolean fullScreen;
    public String lastServer = "";
    public float chatHeightUnfocused = 0.44366196f;
    public boolean snooperEnabled = true;
    public boolean streamSendMetadata = true;
    public KeyBinding[] keyBindsHotbar;
    public int particleSetting;
    public EntityPlayer.EnumChatVisibility chatVisibility;
    public float chatWidth = 1.0f;
    public float streamGameVolume = 1.0f;
    private static final String[] AMBIENT_OCCLUSIONS;
    public boolean showInventoryAchievementHint = true;
    public boolean field_181657_aC;
    public float streamMicVolume = 1.0f;
    public boolean showDebugInfo;
    public KeyBinding keyBindStreamCommercials;
    public KeyBinding keyBindFullscreen;
    public KeyBinding keyBindPlayerList;
    public KeyBinding[] keyBindings;
    public float fovSetting = 70.0f;
    public boolean reducedDebugInfo = false;
    public int mipmapLevels = 4;
    public float streamBytesPerPixel = 0.5f;
    public boolean hideServerAddress;
    public boolean forceUnicodeFont = false;
    public KeyBinding keyBindForward;
    public List<String> field_183018_l = Lists.newArrayList();
    public EnumDifficulty difficulty;
    public boolean advancedItemTooltips;
    public boolean useVbo = false;
    public KeyBinding keyBindUseItem;
    private final Set<EnumPlayerModelParts> setModelParts;
    public boolean showDebugProfilerChart;
    public KeyBinding keyBindStreamStartStop;
    private static final String[] STREAM_CHAT_MODES;
    public boolean fboEnable = true;
    public int limitFramerate = 120;
    public KeyBinding keyBindPickBlock;
    public int clouds = 2;
    public String streamPreferredServer = "";
    public float saturation;
    public int overrideWidth;
    public float chatHeightFocused = 1.0f;
    private Map<SoundCategory, Float> mapSoundLevels;
    public boolean chatColours = true;
    public KeyBinding keyBindChat;
    public KeyBinding keyBindTogglePerspective;

    public GameSettings(Minecraft minecraft, File file) {
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
        this.mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
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
        this.keyBindsHotbar = new KeyBinding[]{new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.mc = minecraft;
        this.optionsFile = new File(file, "options.txt");
        if (minecraft.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
            Options.RENDER_DISTANCE.setValueMax(32.0f);
        } else {
            Options.RENDER_DISTANCE.setValueMax(16.0f);
        }
        this.renderDistanceChunks = minecraft.isJava64bit() ? 12 : 8;
        this.loadOptions();
    }

    public boolean getOptionOrdinalValue(Options options) {
        switch (options) {
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

    public void setOptionValue(Options options, int n) {
        if (options == Options.INVERT_MOUSE) {
            boolean bl = this.invertMouse = !this.invertMouse;
        }
        if (options == Options.GUI_SCALE) {
            this.guiScale = this.guiScale + n & 3;
        }
        if (options == Options.PARTICLES) {
            this.particleSetting = (this.particleSetting + n) % 3;
        }
        if (options == Options.VIEW_BOBBING) {
            boolean bl = this.viewBobbing = !this.viewBobbing;
        }
        if (options == Options.RENDER_CLOUDS) {
            this.clouds = (this.clouds + n) % 3;
        }
        if (options == Options.FORCE_UNICODE_FONT) {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            Minecraft.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }
        if (options == Options.FBO_ENABLE) {
            boolean bl = this.fboEnable = !this.fboEnable;
        }
        if (options == Options.ANAGLYPH) {
            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
        }
        if (options == Options.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.renderGlobal.loadRenderers();
        }
        if (options == Options.AMBIENT_OCCLUSION) {
            this.ambientOcclusion = (this.ambientOcclusion + n) % 3;
            this.mc.renderGlobal.loadRenderers();
        }
        if (options == Options.CHAT_VISIBILITY) {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + n) % 3);
        }
        if (options == Options.STREAM_COMPRESSION) {
            this.streamCompression = (this.streamCompression + n) % 3;
        }
        if (options == Options.STREAM_SEND_METADATA) {
            boolean bl = this.streamSendMetadata = !this.streamSendMetadata;
        }
        if (options == Options.STREAM_CHAT_ENABLED) {
            this.streamChatEnabled = (this.streamChatEnabled + n) % 3;
        }
        if (options == Options.STREAM_CHAT_USER_FILTER) {
            this.streamChatUserFilter = (this.streamChatUserFilter + n) % 3;
        }
        if (options == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            this.streamMicToggleBehavior = (this.streamMicToggleBehavior + n) % 2;
        }
        if (options == Options.CHAT_COLOR) {
            boolean bl = this.chatColours = !this.chatColours;
        }
        if (options == Options.CHAT_LINKS) {
            boolean bl = this.chatLinks = !this.chatLinks;
        }
        if (options == Options.CHAT_LINKS_PROMPT) {
            boolean bl = this.chatLinksPrompt = !this.chatLinksPrompt;
        }
        if (options == Options.SNOOPER_ENABLED) {
            boolean bl = this.snooperEnabled = !this.snooperEnabled;
        }
        if (options == Options.TOUCHSCREEN) {
            boolean bl = this.touchscreen = !this.touchscreen;
        }
        if (options == Options.USE_FULLSCREEN) {
            boolean bl = this.fullScreen = !this.fullScreen;
            if (this.mc.isFullScreen() != this.fullScreen) {
                this.mc.toggleFullscreen();
            }
        }
        if (options == Options.ENABLE_VSYNC) {
            this.enableVsync = !this.enableVsync;
            Display.setVSyncEnabled((boolean)this.enableVsync);
        }
        if (options == Options.USE_VBO) {
            this.useVbo = !this.useVbo;
            this.mc.renderGlobal.loadRenderers();
        }
        if (options == Options.BLOCK_ALTERNATIVES) {
            this.allowBlockAlternatives = !this.allowBlockAlternatives;
            this.mc.renderGlobal.loadRenderers();
        }
        if (options == Options.REDUCED_DEBUG_INFO) {
            boolean bl = this.reducedDebugInfo = !this.reducedDebugInfo;
        }
        if (options == Options.ENTITY_SHADOWS) {
            this.field_181151_V = !this.field_181151_V;
        }
        this.saveOptions();
    }

    public String getKeyBinding(Options options) {
        String string = String.valueOf(I18n.format(options.getEnumString(), new Object[0])) + ": ";
        if (options.getEnumFloat()) {
            float f = this.getOptionFloatValue(options);
            float f2 = options.normalizeValue(f);
            return options == Options.SENSITIVITY ? (f2 == 0.0f ? String.valueOf(string) + I18n.format("options.sensitivity.min", new Object[0]) : (f2 == 1.0f ? String.valueOf(string) + I18n.format("options.sensitivity.max", new Object[0]) : String.valueOf(string) + (int)(f2 * 200.0f) + "%")) : (options == Options.FOV ? (f == 70.0f ? String.valueOf(string) + I18n.format("options.fov.min", new Object[0]) : (f == 110.0f ? String.valueOf(string) + I18n.format("options.fov.max", new Object[0]) : String.valueOf(string) + (int)f)) : (options == Options.FRAMERATE_LIMIT ? (f == options.valueMax ? String.valueOf(string) + I18n.format("options.framerateLimit.max", new Object[0]) : String.valueOf(string) + (int)f + " fps") : (options == Options.RENDER_CLOUDS ? (f == options.valueMin ? String.valueOf(string) + I18n.format("options.cloudHeight.min", new Object[0]) : String.valueOf(string) + ((int)f + 128)) : (options == Options.GAMMA ? (f2 == 0.0f ? String.valueOf(string) + I18n.format("options.gamma.min", new Object[0]) : (f2 == 1.0f ? String.valueOf(string) + I18n.format("options.gamma.max", new Object[0]) : String.valueOf(string) + "+" + (int)(f2 * 100.0f) + "%")) : (options == Options.SATURATION ? String.valueOf(string) + (int)(f2 * 400.0f) + "%" : (options == Options.CHAT_OPACITY ? String.valueOf(string) + (int)(f2 * 90.0f + 10.0f) + "%" : (options == Options.CHAT_HEIGHT_UNFOCUSED ? String.valueOf(string) + GuiNewChat.calculateChatboxHeight(f2) + "px" : (options == Options.CHAT_HEIGHT_FOCUSED ? String.valueOf(string) + GuiNewChat.calculateChatboxHeight(f2) + "px" : (options == Options.CHAT_WIDTH ? String.valueOf(string) + GuiNewChat.calculateChatboxWidth(f2) + "px" : (options == Options.RENDER_DISTANCE ? String.valueOf(string) + (int)f + " chunks" : (options == Options.MIPMAP_LEVELS ? (f == 0.0f ? String.valueOf(string) + I18n.format("options.off", new Object[0]) : String.valueOf(string) + (int)f) : (options == Options.STREAM_FPS ? String.valueOf(string) + TwitchStream.formatStreamFps(f2) + " fps" : (options == Options.STREAM_KBPS ? String.valueOf(string) + TwitchStream.formatStreamKbps(f2) + " Kbps" : (options == Options.STREAM_BYTES_PER_PIXEL ? String.valueOf(string) + String.format("%.3f bpp", Float.valueOf(TwitchStream.formatStreamBps(f2))) : (f2 == 0.0f ? String.valueOf(string) + I18n.format("options.off", new Object[0]) : String.valueOf(string) + (int)(f2 * 100.0f) + "%")))))))))))))));
        }
        if (options.getEnumBoolean()) {
            boolean bl = this.getOptionOrdinalValue(options);
            return bl ? String.valueOf(string) + I18n.format("options.on", new Object[0]) : String.valueOf(string) + I18n.format("options.off", new Object[0]);
        }
        if (options == Options.GUI_SCALE) {
            return String.valueOf(string) + GameSettings.getTranslation(GUISCALES, this.guiScale);
        }
        if (options == Options.CHAT_VISIBILITY) {
            return String.valueOf(string) + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
        }
        if (options == Options.PARTICLES) {
            return String.valueOf(string) + GameSettings.getTranslation(PARTICLES, this.particleSetting);
        }
        if (options == Options.AMBIENT_OCCLUSION) {
            return String.valueOf(string) + GameSettings.getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        if (options == Options.STREAM_COMPRESSION) {
            return String.valueOf(string) + GameSettings.getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
        }
        if (options == Options.STREAM_CHAT_ENABLED) {
            return String.valueOf(string) + GameSettings.getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
        }
        if (options == Options.STREAM_CHAT_USER_FILTER) {
            return String.valueOf(string) + GameSettings.getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
        }
        if (options == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return String.valueOf(string) + GameSettings.getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
        }
        if (options == Options.RENDER_CLOUDS) {
            return String.valueOf(string) + GameSettings.getTranslation(field_181149_aW, this.clouds);
        }
        if (options == Options.GRAPHICS) {
            if (this.fancyGraphics) {
                return String.valueOf(string) + I18n.format("options.graphics.fancy", new Object[0]);
            }
            String string2 = "options.graphics.fast";
            return String.valueOf(string) + I18n.format("options.graphics.fast", new Object[0]);
        }
        return string;
    }

    private float parseFloat(String string) {
        return string.equals("true") ? 1.0f : (string.equals("false") ? 0.0f : Float.parseFloat(string));
    }

    public void saveOptions() {
        try {
            KeyBinding keyBinding;
            PrintWriter printWriter = new PrintWriter(new FileWriter(this.optionsFile));
            printWriter.println("invertYMouse:" + this.invertMouse);
            printWriter.println("mouseSensitivity:" + this.mouseSensitivity);
            printWriter.println("fov:" + (this.fovSetting - 70.0f) / 40.0f);
            printWriter.println("gamma:" + this.gammaSetting);
            printWriter.println("saturation:" + this.saturation);
            printWriter.println("renderDistance:" + this.renderDistanceChunks);
            printWriter.println("guiScale:" + this.guiScale);
            printWriter.println("particles:" + this.particleSetting);
            printWriter.println("bobView:" + this.viewBobbing);
            printWriter.println("anaglyph3d:" + this.anaglyph);
            printWriter.println("maxFps:" + this.limitFramerate);
            printWriter.println("fboEnable:" + this.fboEnable);
            printWriter.println("difficulty:" + this.difficulty.getDifficultyId());
            printWriter.println("fancyGraphics:" + this.fancyGraphics);
            printWriter.println("ao:" + this.ambientOcclusion);
            switch (this.clouds) {
                case 0: {
                    printWriter.println("renderClouds:false");
                    break;
                }
                case 1: {
                    printWriter.println("renderClouds:fast");
                    break;
                }
                case 2: {
                    printWriter.println("renderClouds:true");
                }
            }
            printWriter.println("resourcePacks:" + gson.toJson(this.resourcePacks));
            printWriter.println("incompatibleResourcePacks:" + gson.toJson(this.field_183018_l));
            printWriter.println("lastServer:" + this.lastServer);
            printWriter.println("lang:" + this.language);
            printWriter.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
            printWriter.println("chatColors:" + this.chatColours);
            printWriter.println("chatLinks:" + this.chatLinks);
            printWriter.println("chatLinksPrompt:" + this.chatLinksPrompt);
            printWriter.println("chatOpacity:" + this.chatOpacity);
            printWriter.println("snooperEnabled:" + this.snooperEnabled);
            printWriter.println("fullscreen:" + this.fullScreen);
            printWriter.println("enableVsync:" + this.enableVsync);
            printWriter.println("useVbo:" + this.useVbo);
            printWriter.println("hideServerAddress:" + this.hideServerAddress);
            printWriter.println("advancedItemTooltips:" + this.advancedItemTooltips);
            printWriter.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
            printWriter.println("touchscreen:" + this.touchscreen);
            printWriter.println("overrideWidth:" + this.overrideWidth);
            printWriter.println("overrideHeight:" + this.overrideHeight);
            printWriter.println("heldItemTooltips:" + this.heldItemTooltips);
            printWriter.println("chatHeightFocused:" + this.chatHeightFocused);
            printWriter.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
            printWriter.println("chatScale:" + this.chatScale);
            printWriter.println("chatWidth:" + this.chatWidth);
            printWriter.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
            printWriter.println("mipmapLevels:" + this.mipmapLevels);
            printWriter.println("streamBytesPerPixel:" + this.streamBytesPerPixel);
            printWriter.println("streamMicVolume:" + this.streamMicVolume);
            printWriter.println("streamSystemVolume:" + this.streamGameVolume);
            printWriter.println("streamKbps:" + this.streamKbps);
            printWriter.println("streamFps:" + this.streamFps);
            printWriter.println("streamCompression:" + this.streamCompression);
            printWriter.println("streamSendMetadata:" + this.streamSendMetadata);
            printWriter.println("streamPreferredServer:" + this.streamPreferredServer);
            printWriter.println("streamChatEnabled:" + this.streamChatEnabled);
            printWriter.println("streamChatUserFilter:" + this.streamChatUserFilter);
            printWriter.println("streamMicToggleBehavior:" + this.streamMicToggleBehavior);
            printWriter.println("forceUnicodeFont:" + this.forceUnicodeFont);
            printWriter.println("allowBlockAlternatives:" + this.allowBlockAlternatives);
            printWriter.println("reducedDebugInfo:" + this.reducedDebugInfo);
            printWriter.println("useNativeTransport:" + this.field_181150_U);
            printWriter.println("entityShadows:" + this.field_181151_V);
            Object[] objectArray = this.keyBindings;
            int n = this.keyBindings.length;
            int n2 = 0;
            while (n2 < n) {
                keyBinding = objectArray[n2];
                printWriter.println("key_" + keyBinding.getKeyDescription() + ":" + keyBinding.getKeyCode());
                ++n2;
            }
            objectArray = SoundCategory.values();
            n = objectArray.length;
            n2 = 0;
            while (n2 < n) {
                keyBinding = objectArray[n2];
                printWriter.println("soundCategory_" + ((SoundCategory)((Object)keyBinding)).getCategoryName() + ":" + this.getSoundLevel((SoundCategory)((Object)keyBinding)));
                ++n2;
            }
            objectArray = EnumPlayerModelParts.values();
            n = objectArray.length;
            n2 = 0;
            while (n2 < n) {
                keyBinding = objectArray[n2];
                printWriter.println("modelPart_" + ((EnumPlayerModelParts)((Object)keyBinding)).getPartName() + ":" + this.setModelParts.contains(keyBinding));
                ++n2;
            }
            printWriter.close();
        }
        catch (Exception exception) {
            logger.error("Failed to save options", (Throwable)exception);
        }
        this.sendSettingsToServer();
    }

    public void setOptionFloatValue(Options options, float f) {
        if (options == Options.SENSITIVITY) {
            this.mouseSensitivity = f;
        }
        if (options == Options.FOV) {
            this.fovSetting = f;
        }
        if (options == Options.GAMMA) {
            this.gammaSetting = f;
        }
        if (options == Options.FRAMERATE_LIMIT) {
            this.limitFramerate = (int)f;
        }
        if (options == Options.CHAT_OPACITY) {
            this.chatOpacity = f;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (options == Options.CHAT_HEIGHT_FOCUSED) {
            this.chatHeightFocused = f;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (options == Options.CHAT_HEIGHT_UNFOCUSED) {
            this.chatHeightUnfocused = f;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (options == Options.CHAT_WIDTH) {
            this.chatWidth = f;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (options == Options.CHAT_SCALE) {
            this.chatScale = f;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (options == Options.MIPMAP_LEVELS) {
            int n = this.mipmapLevels;
            this.mipmapLevels = (int)f;
            if ((float)n != f) {
                this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
                this.mc.scheduleResourcesRefresh();
            }
        }
        if (options == Options.BLOCK_ALTERNATIVES) {
            this.allowBlockAlternatives = !this.allowBlockAlternatives;
            this.mc.renderGlobal.loadRenderers();
        }
        if (options == Options.RENDER_DISTANCE) {
            this.renderDistanceChunks = (int)f;
            this.mc.renderGlobal.setDisplayListEntitiesDirty();
        }
        if (options == Options.STREAM_BYTES_PER_PIXEL) {
            this.streamBytesPerPixel = f;
        }
        if (options == Options.STREAM_VOLUME_MIC) {
            this.streamMicVolume = f;
            this.mc.getTwitchStream().updateStreamVolume();
        }
        if (options == Options.STREAM_VOLUME_SYSTEM) {
            this.streamGameVolume = f;
            this.mc.getTwitchStream().updateStreamVolume();
        }
        if (options == Options.STREAM_KBPS) {
            this.streamKbps = f;
        }
        if (options == Options.STREAM_FPS) {
            this.streamFps = f;
        }
    }

    public boolean func_181148_f() {
        return this.field_181150_U;
    }

    public void setOptionKeyBinding(KeyBinding keyBinding, int n) {
        keyBinding.setKeyCode(n);
        this.saveOptions();
    }

    public float getSoundLevel(SoundCategory soundCategory) {
        return this.mapSoundLevels.containsKey((Object)soundCategory) ? this.mapSoundLevels.get((Object)soundCategory).floatValue() : 1.0f;
    }

    public static String getKeyDisplayString(int n) {
        return n < 0 ? I18n.format("key.mouseButton", n + 101) : (n < 256 ? Keyboard.getKeyName((int)n) : String.format("%c", Character.valueOf((char)(n - 256))).toUpperCase());
    }

    public float getOptionFloatValue(Options options) {
        return options == Options.FOV ? this.fovSetting : (options == Options.GAMMA ? this.gammaSetting : (options == Options.SATURATION ? this.saturation : (options == Options.SENSITIVITY ? this.mouseSensitivity : (options == Options.CHAT_OPACITY ? this.chatOpacity : (options == Options.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : (options == Options.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : (options == Options.CHAT_SCALE ? this.chatScale : (options == Options.CHAT_WIDTH ? this.chatWidth : (options == Options.FRAMERATE_LIMIT ? (float)this.limitFramerate : (options == Options.MIPMAP_LEVELS ? (float)this.mipmapLevels : (options == Options.RENDER_DISTANCE ? (float)this.renderDistanceChunks : (options == Options.STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : (options == Options.STREAM_VOLUME_MIC ? this.streamMicVolume : (options == Options.STREAM_VOLUME_SYSTEM ? this.streamGameVolume : (options == Options.STREAM_KBPS ? this.streamKbps : (options == Options.STREAM_FPS ? this.streamFps : 0.0f))))))))))))))));
    }

    public static boolean isKeyDown(KeyBinding keyBinding) {
        return keyBinding.getKeyCode() == 0 ? false : (keyBinding.getKeyCode() < 0 ? Mouse.isButtonDown((int)(keyBinding.getKeyCode() + 100)) : Keyboard.isKeyDown((int)keyBinding.getKeyCode()));
    }

    public Set<EnumPlayerModelParts> getModelParts() {
        return ImmutableSet.copyOf(this.setModelParts);
    }

    public void switchModelPartEnabled(EnumPlayerModelParts enumPlayerModelParts) {
        if (!this.getModelParts().contains((Object)enumPlayerModelParts)) {
            this.setModelParts.add(enumPlayerModelParts);
        } else {
            this.setModelParts.remove((Object)enumPlayerModelParts);
        }
        this.sendSettingsToServer();
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.optionsFile));
            String string = "";
            this.mapSoundLevels.clear();
            while ((string = bufferedReader.readLine()) != null) {
                try {
                    KeyBinding keyBinding;
                    String[] stringArray = string.split(":");
                    if (stringArray[0].equals("mouseSensitivity")) {
                        this.mouseSensitivity = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("fov")) {
                        this.fovSetting = this.parseFloat(stringArray[1]) * 40.0f + 70.0f;
                    }
                    if (stringArray[0].equals("gamma")) {
                        this.gammaSetting = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("saturation")) {
                        this.saturation = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("invertYMouse")) {
                        this.invertMouse = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("renderDistance")) {
                        this.renderDistanceChunks = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("guiScale")) {
                        this.guiScale = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("particles")) {
                        this.particleSetting = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("bobView")) {
                        this.viewBobbing = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("anaglyph3d")) {
                        this.anaglyph = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("maxFps")) {
                        this.limitFramerate = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("fboEnable")) {
                        this.fboEnable = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("difficulty")) {
                        this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(stringArray[1]));
                    }
                    if (stringArray[0].equals("fancyGraphics")) {
                        this.fancyGraphics = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("ao")) {
                        this.ambientOcclusion = stringArray[1].equals("true") ? 2 : (stringArray[1].equals("false") ? 0 : Integer.parseInt(stringArray[1]));
                    }
                    if (stringArray[0].equals("renderClouds")) {
                        if (stringArray[1].equals("true")) {
                            this.clouds = 2;
                        } else if (stringArray[1].equals("false")) {
                            this.clouds = 0;
                        } else if (stringArray[1].equals("fast")) {
                            this.clouds = 1;
                        }
                    }
                    if (stringArray[0].equals("resourcePacks")) {
                        this.resourcePacks = (List)gson.fromJson(string.substring(string.indexOf(58) + 1), (Type)typeListString);
                        if (this.resourcePacks == null) {
                            this.resourcePacks = Lists.newArrayList();
                        }
                    }
                    if (stringArray[0].equals("incompatibleResourcePacks")) {
                        this.field_183018_l = (List)gson.fromJson(string.substring(string.indexOf(58) + 1), (Type)typeListString);
                        if (this.field_183018_l == null) {
                            this.field_183018_l = Lists.newArrayList();
                        }
                    }
                    if (stringArray[0].equals("lastServer") && stringArray.length >= 2) {
                        this.lastServer = string.substring(string.indexOf(58) + 1);
                    }
                    if (stringArray[0].equals("lang") && stringArray.length >= 2) {
                        this.language = stringArray[1];
                    }
                    if (stringArray[0].equals("chatVisibility")) {
                        this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(stringArray[1]));
                    }
                    if (stringArray[0].equals("chatColors")) {
                        this.chatColours = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("chatLinks")) {
                        this.chatLinks = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("chatLinksPrompt")) {
                        this.chatLinksPrompt = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("chatOpacity")) {
                        this.chatOpacity = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("snooperEnabled")) {
                        this.snooperEnabled = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("fullscreen")) {
                        this.fullScreen = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("enableVsync")) {
                        this.enableVsync = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("useVbo")) {
                        this.useVbo = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("hideServerAddress")) {
                        this.hideServerAddress = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("advancedItemTooltips")) {
                        this.advancedItemTooltips = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("pauseOnLostFocus")) {
                        this.pauseOnLostFocus = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("touchscreen")) {
                        this.touchscreen = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("overrideHeight")) {
                        this.overrideHeight = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("overrideWidth")) {
                        this.overrideWidth = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("heldItemTooltips")) {
                        this.heldItemTooltips = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("chatHeightFocused")) {
                        this.chatHeightFocused = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("chatHeightUnfocused")) {
                        this.chatHeightUnfocused = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("chatScale")) {
                        this.chatScale = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("chatWidth")) {
                        this.chatWidth = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("showInventoryAchievementHint")) {
                        this.showInventoryAchievementHint = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("mipmapLevels")) {
                        this.mipmapLevels = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamBytesPerPixel")) {
                        this.streamBytesPerPixel = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamMicVolume")) {
                        this.streamMicVolume = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamSystemVolume")) {
                        this.streamGameVolume = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamKbps")) {
                        this.streamKbps = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamFps")) {
                        this.streamFps = this.parseFloat(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamCompression")) {
                        this.streamCompression = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamSendMetadata")) {
                        this.streamSendMetadata = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("streamPreferredServer") && stringArray.length >= 2) {
                        this.streamPreferredServer = string.substring(string.indexOf(58) + 1);
                    }
                    if (stringArray[0].equals("streamChatEnabled")) {
                        this.streamChatEnabled = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamChatUserFilter")) {
                        this.streamChatUserFilter = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("streamMicToggleBehavior")) {
                        this.streamMicToggleBehavior = Integer.parseInt(stringArray[1]);
                    }
                    if (stringArray[0].equals("forceUnicodeFont")) {
                        this.forceUnicodeFont = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("allowBlockAlternatives")) {
                        this.allowBlockAlternatives = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("reducedDebugInfo")) {
                        this.reducedDebugInfo = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("useNativeTransport")) {
                        this.field_181150_U = stringArray[1].equals("true");
                    }
                    if (stringArray[0].equals("entityShadows")) {
                        this.field_181151_V = stringArray[1].equals("true");
                    }
                    Object[] objectArray = this.keyBindings;
                    int n = this.keyBindings.length;
                    int n2 = 0;
                    while (n2 < n) {
                        keyBinding = objectArray[n2];
                        if (stringArray[0].equals("key_" + keyBinding.getKeyDescription())) {
                            keyBinding.setKeyCode(Integer.parseInt(stringArray[1]));
                        }
                        ++n2;
                    }
                    objectArray = SoundCategory.values();
                    n = objectArray.length;
                    n2 = 0;
                    while (n2 < n) {
                        keyBinding = objectArray[n2];
                        if (stringArray[0].equals("soundCategory_" + ((SoundCategory)((Object)keyBinding)).getCategoryName())) {
                            this.mapSoundLevels.put((SoundCategory)((Object)keyBinding), Float.valueOf(this.parseFloat(stringArray[1])));
                        }
                        ++n2;
                    }
                    objectArray = EnumPlayerModelParts.values();
                    n = objectArray.length;
                    n2 = 0;
                    while (n2 < n) {
                        keyBinding = objectArray[n2];
                        if (stringArray[0].equals("modelPart_" + ((EnumPlayerModelParts)((Object)keyBinding)).getPartName())) {
                            this.setModelPartEnabled((EnumPlayerModelParts)((Object)keyBinding), stringArray[1].equals("true"));
                        }
                        ++n2;
                    }
                }
                catch (Exception exception) {
                    logger.warn("Skipping bad option: " + string);
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedReader.close();
        }
        catch (Exception exception) {
            logger.error("Failed to load options", (Throwable)exception);
        }
    }

    public GameSettings() {
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
        this.mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
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
        this.keyBindsHotbar = new KeyBinding[]{new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
    }

    private static String getTranslation(String[] stringArray, int n) {
        if (n < 0 || n >= stringArray.length) {
            n = 0;
        }
        return I18n.format(stringArray[n], new Object[0]);
    }

    public void setSoundLevel(SoundCategory soundCategory, float f) {
        this.mc.getSoundHandler().setSoundLevel(soundCategory, f);
        this.mapSoundLevels.put(soundCategory, Float.valueOf(f));
    }

    static {
        logger = LogManager.getLogger();
        gson = new Gson();
        typeListString = new ParameterizedType(){

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }
        };
        GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
        PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
        AMBIENT_OCCLUSIONS = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
        STREAM_COMPRESSIONS = new String[]{"options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high"};
        STREAM_CHAT_MODES = new String[]{"options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never"};
        STREAM_CHAT_FILTER_MODES = new String[]{"options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods"};
        STREAM_MIC_MODES = new String[]{"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk"};
        field_181149_aW = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
    }

    public void setModelPartEnabled(EnumPlayerModelParts enumPlayerModelParts, boolean bl) {
        if (bl) {
            this.setModelParts.add(enumPlayerModelParts);
        } else {
            this.setModelParts.remove((Object)enumPlayerModelParts);
        }
        this.sendSettingsToServer();
    }

    public int func_181147_e() {
        return this.renderDistanceChunks >= 4 ? this.clouds : 0;
    }

    public void sendSettingsToServer() {
        if (Minecraft.thePlayer != null) {
            int n = 0;
            for (EnumPlayerModelParts enumPlayerModelParts : this.setModelParts) {
                n |= enumPlayerModelParts.getPartMask();
            }
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, n));
        }
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
        ENTITY_SHADOWS("options.entityShadows", false, true);

        private final float valueStep;
        private final boolean enumFloat;
        private final boolean enumBoolean;
        private float valueMin;
        private final String enumString;
        private float valueMax;

        public boolean getEnumFloat() {
            return this.enumFloat;
        }

        protected float snapToStep(float f) {
            if (this.valueStep > 0.0f) {
                f = this.valueStep * (float)Math.round(f / this.valueStep);
            }
            return f;
        }

        public boolean getEnumBoolean() {
            return this.enumBoolean;
        }

        public static Options getEnumOptions(int n) {
            Options[] optionsArray = Options.values();
            int n2 = optionsArray.length;
            int n3 = 0;
            while (n3 < n2) {
                Options options = optionsArray[n3];
                if (options.returnEnumOrdinal() == n) {
                    return options;
                }
                ++n3;
            }
            return null;
        }

        public float snapToStepClamp(float f) {
            f = this.snapToStep(f);
            return MathHelper.clamp_float(f, this.valueMin, this.valueMax);
        }

        private Options(String string2, boolean bl, boolean bl2, float f, float f2, float f3) {
            this.enumString = string2;
            this.enumFloat = bl;
            this.enumBoolean = bl2;
            this.valueMin = f;
            this.valueMax = f2;
            this.valueStep = f3;
        }

        public float getValueMax() {
            return this.valueMax;
        }

        public float denormalizeValue(float f) {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(f, 0.0f, 1.0f));
        }

        public float normalizeValue(float f) {
            return MathHelper.clamp_float((this.snapToStepClamp(f) - this.valueMin) / (this.valueMax - this.valueMin), 0.0f, 1.0f);
        }

        public void setValueMax(float f) {
            this.valueMax = f;
        }

        public int returnEnumOrdinal() {
            return this.ordinal();
        }

        private Options(String string2, boolean bl, boolean bl2) {
            this(string2, bl, bl2, 0.0f, 1.0f, 0.0f);
        }

        public String getEnumString() {
            return this.enumString;
        }
    }
}

