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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.stream.IStream;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import optifine.ClearWater;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomSky;
import optifine.DynamicLights;
import optifine.Lang;
import optifine.NaturalTextures;
import optifine.RandomMobs;
import optifine.Reflector;
import optifine.ReflectorClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import shadersmod.client.Shaders;

public class GameSettings
{
  private static final Logger logger = ;
  private static final Gson gson = new Gson();
  private static final ParameterizedType typeListString = new ParameterizedType()
  {
    private static final String __OBFID = "CL_00000651";
    
    public Type[] getActualTypeArguments()
    {
      return new Type[] { String.class };
    }
    
    public Type getRawType()
    {
      return List.class;
    }
    
    public Type getOwnerType()
    {
      return null;
    }
  };
  private static final String[] GUISCALES = { "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large" };
  private static final String[] PARTICLES = { "options.particles.all", "options.particles.decreased", "options.particles.minimal" };
  private static final String[] AMBIENT_OCCLUSIONS = { "options.ao.off", "options.ao.min", "options.ao.max" };
  private static final String[] STREAM_COMPRESSIONS = { "options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high" };
  private static final String[] STREAM_CHAT_MODES = { "options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never" };
  private static final String[] STREAM_CHAT_FILTER_MODES = { "options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods" };
  private static final String[] STREAM_MIC_MODES = { "options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk" };
  public float mouseSensitivity = 0.5F;
  public boolean invertMouse;
  public int renderDistanceChunks = -1;
  public boolean viewBobbing = true;
  public boolean anaglyph;
  public boolean fboEnable = true;
  public int limitFramerate = 120;
  public boolean clouds = true;
  public boolean fancyGraphics = true;
  public int ambientOcclusion = 2;
  public List resourcePacks = Lists.newArrayList();
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
  public KeyBinding field_178883_an;
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
  public boolean ofFastRender = true;
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
  public static final int DEFAULT = 0;
  public static final int FAST = 1;
  public static final int FANCY = 2;
  public static final int OFF = 3;
  public static final int SMART = 4;
  public static final int ANIM_ON = 0;
  public static final int ANIM_GENERATED = 1;
  public static final int ANIM_OFF = 2;
  public static final String DEFAULT_STR = "Default";
  private static final int[] OF_TREES_VALUES = { 0, 1, 4, 2 };
  private static final int[] OF_DYNAMIC_LIGHTS = { 3, 1, 2 };
  private static final String[] KEYS_DYNAMIC_LIGHTS = { "options.off", "options.graphics.fast", "options.graphics.fancy" };
  public KeyBinding ofKeyBindZoom;
  private File optionsFileOF;
  
  public GameSettings(Minecraft mcIn, File p_i46326_2_)
  {
    this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
    this.chatColours = true;
    this.chatLinks = true;
    this.chatLinksPrompt = true;
    this.chatOpacity = 1.0F;
    this.snooperEnabled = true;
    this.enableVsync = true;
    this.field_178881_t = false;
    this.field_178880_u = true;
    this.field_178879_v = false;
    this.pauseOnLostFocus = true;
    this.field_178882_aU = Sets.newHashSet(EnumPlayerModelParts.values());
    this.heldItemTooltips = true;
    this.chatScale = 1.0F;
    this.chatWidth = 1.0F;
    this.chatHeightUnfocused = 0.44366196F;
    this.chatHeightFocused = 1.0F;
    this.showInventoryAchievementHint = true;
    this.mipmapLevels = 4;
    this.mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
    this.streamBytesPerPixel = 0.5F;
    this.streamMicVolume = 1.0F;
    this.streamGameVolume = 1.0F;
    this.streamKbps = 0.5412844F;
    this.streamFps = 0.31690142F;
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
    this.field_178883_an = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
    this.keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
    this.keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
    this.keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
    this.keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
    this.keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
    this.keyBindings = ((KeyBinding[])ArrayUtils.addAll(new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindSprint, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.field_178883_an }, this.keyBindsHotbar));
    this.difficulty = EnumDifficulty.NORMAL;
    this.lastServer = "";
    this.fovSetting = 70.0F;
    this.language = "en_US";
    this.forceUnicodeFont = false;
    this.mc = mcIn;
    this.optionsFile = new File(p_i46326_2_, "options.txt");
    this.optionsFileOF = new File(p_i46326_2_, "optionsof.txt");
    this.limitFramerate = ((int)Options.FRAMERATE_LIMIT.getValueMax());
    this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
    this.keyBindings = ((KeyBinding[])ArrayUtils.add(this.keyBindings, this.ofKeyBindZoom));
    Options.RENDER_DISTANCE.setValueMax(32.0F);
    this.renderDistanceChunks = 8;
    loadOptions();
    Config.initGameSettings(this);
  }
  
  public GameSettings()
  {
    this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
    this.chatColours = true;
    this.chatLinks = true;
    this.chatLinksPrompt = true;
    this.chatOpacity = 1.0F;
    this.snooperEnabled = true;
    this.enableVsync = true;
    this.field_178881_t = false;
    this.field_178880_u = true;
    this.field_178879_v = false;
    this.pauseOnLostFocus = true;
    this.field_178882_aU = Sets.newHashSet(EnumPlayerModelParts.values());
    this.heldItemTooltips = true;
    this.chatScale = 1.0F;
    this.chatWidth = 1.0F;
    this.chatHeightUnfocused = 0.44366196F;
    this.chatHeightFocused = 1.0F;
    this.showInventoryAchievementHint = true;
    this.mipmapLevels = 4;
    this.mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
    this.streamBytesPerPixel = 0.5F;
    this.streamMicVolume = 1.0F;
    this.streamGameVolume = 1.0F;
    this.streamKbps = 0.5412844F;
    this.streamFps = 0.31690142F;
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
    this.field_178883_an = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
    this.keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
    this.keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
    this.keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
    this.keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
    this.keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
    this.keyBindings = ((KeyBinding[])ArrayUtils.addAll(new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindSprint, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.field_178883_an }, this.keyBindsHotbar));
    this.difficulty = EnumDifficulty.NORMAL;
    this.lastServer = "";
    this.fovSetting = 70.0F;
    this.language = "en_US";
    this.forceUnicodeFont = false;
  }
  
  public static String getKeyDisplayString(int p_74298_0_)
  {
    return p_74298_0_ < 256 ? Keyboard.getKeyName(p_74298_0_) : p_74298_0_ < 0 ? I18n.format("key.mouseButton", new Object[] { Integer.valueOf(p_74298_0_ + 101) }) : String.format("%c", new Object[] { Character.valueOf((char)(p_74298_0_ - 256)) }).toUpperCase();
  }
  
  public static boolean isKeyDown(KeyBinding p_100015_0_)
  {
    int keyCode = p_100015_0_.getKeyCode();
    return (keyCode >= -100) && (keyCode <= 255) && (p_100015_0_.getKeyCode() != 0) && (p_100015_0_.getKeyCode() < 0 ? Mouse.isButtonDown(p_100015_0_.getKeyCode() + 100) : Keyboard.isKeyDown(p_100015_0_.getKeyCode()));
  }
  
  public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_)
  {
    p_151440_1_.setKeyCode(p_151440_2_);
    saveOptions();
  }
  
  public void setOptionFloatValue(Options p_74304_1_, float p_74304_2_)
  {
    setOptionFloatValueOF(p_74304_1_, p_74304_2_);
    if (p_74304_1_ == Options.SENSITIVITY) {
      this.mouseSensitivity = p_74304_2_;
    }
    if (p_74304_1_ == Options.FOV) {
      this.fovSetting = p_74304_2_;
    }
    if (p_74304_1_ == Options.GAMMA) {
      this.gammaSetting = p_74304_2_;
    }
    if (p_74304_1_ == Options.FRAMERATE_LIMIT)
    {
      this.limitFramerate = ((int)p_74304_2_);
      this.enableVsync = false;
      if (this.limitFramerate <= 0)
      {
        this.limitFramerate = ((int)Options.FRAMERATE_LIMIT.getValueMax());
        this.enableVsync = true;
      }
      updateVSync();
    }
    if (p_74304_1_ == Options.CHAT_OPACITY)
    {
      this.chatOpacity = p_74304_2_;
      this.mc.ingameGUI.getChatGUI().refreshChat();
    }
    if (p_74304_1_ == Options.CHAT_HEIGHT_FOCUSED)
    {
      this.chatHeightFocused = p_74304_2_;
      this.mc.ingameGUI.getChatGUI().refreshChat();
    }
    if (p_74304_1_ == Options.CHAT_HEIGHT_UNFOCUSED)
    {
      this.chatHeightUnfocused = p_74304_2_;
      this.mc.ingameGUI.getChatGUI().refreshChat();
    }
    if (p_74304_1_ == Options.CHAT_WIDTH)
    {
      this.chatWidth = p_74304_2_;
      this.mc.ingameGUI.getChatGUI().refreshChat();
    }
    if (p_74304_1_ == Options.CHAT_SCALE)
    {
      this.chatScale = p_74304_2_;
      this.mc.ingameGUI.getChatGUI().refreshChat();
    }
    if (p_74304_1_ == Options.MIPMAP_LEVELS)
    {
      int var3 = this.mipmapLevels;
      this.mipmapLevels = ((int)p_74304_2_);
      if (var3 != p_74304_2_)
      {
        this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        this.mc.getTextureMapBlocks().func_174937_a(false, this.mipmapLevels > 0);
        this.mc.func_175603_A();
      }
    }
    if (p_74304_1_ == Options.BLOCK_ALTERNATIVES)
    {
      this.field_178880_u = (!this.field_178880_u);
      this.mc.renderGlobal.loadRenderers();
    }
    if (p_74304_1_ == Options.RENDER_DISTANCE)
    {
      this.renderDistanceChunks = ((int)p_74304_2_);
      this.mc.renderGlobal.func_174979_m();
    }
    if (p_74304_1_ == Options.STREAM_BYTES_PER_PIXEL) {
      this.streamBytesPerPixel = p_74304_2_;
    }
    if (p_74304_1_ == Options.STREAM_VOLUME_MIC)
    {
      this.streamMicVolume = p_74304_2_;
      this.mc.getTwitchStream().func_152915_s();
    }
    if (p_74304_1_ == Options.STREAM_VOLUME_SYSTEM)
    {
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
  
  public void setOptionValue(Options p_74306_1_, int p_74306_2_)
  {
    setOptionValueOF(p_74306_1_, p_74306_2_);
    if (p_74306_1_ == Options.INVERT_MOUSE) {
      this.invertMouse = (!this.invertMouse);
    }
    if (p_74306_1_ == Options.GUI_SCALE) {
      this.guiScale = (this.guiScale + p_74306_2_ & 0x3);
    }
    if (p_74306_1_ == Options.PARTICLES) {
      this.particleSetting = ((this.particleSetting + p_74306_2_) % 3);
    }
    if (p_74306_1_ == Options.VIEW_BOBBING) {
      this.viewBobbing = (!this.viewBobbing);
    }
    if (p_74306_1_ == Options.RENDER_CLOUDS) {
      this.clouds = (!this.clouds);
    }
    if (p_74306_1_ == Options.FORCE_UNICODE_FONT)
    {
      this.forceUnicodeFont = (!this.forceUnicodeFont);
      Minecraft.fontRendererObj.setUnicodeFlag((this.mc.getLanguageManager().isCurrentLocaleUnicode()) || (this.forceUnicodeFont));
    }
    if (p_74306_1_ == Options.FBO_ENABLE) {
      this.fboEnable = (!this.fboEnable);
    }
    if (p_74306_1_ == Options.ANAGLYPH)
    {
      this.anaglyph = (!this.anaglyph);
      this.mc.refreshResources();
    }
    if (p_74306_1_ == Options.GRAPHICS)
    {
      this.fancyGraphics = (!this.fancyGraphics);
      updateRenderClouds();
      this.mc.renderGlobal.loadRenderers();
    }
    if (p_74306_1_ == Options.AMBIENT_OCCLUSION)
    {
      this.ambientOcclusion = ((this.ambientOcclusion + p_74306_2_) % 3);
      this.mc.renderGlobal.loadRenderers();
    }
    if (p_74306_1_ == Options.CHAT_VISIBILITY) {
      this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + p_74306_2_) % 3);
    }
    if (p_74306_1_ == Options.STREAM_COMPRESSION) {
      this.streamCompression = ((this.streamCompression + p_74306_2_) % 3);
    }
    if (p_74306_1_ == Options.STREAM_SEND_METADATA) {
      this.streamSendMetadata = (!this.streamSendMetadata);
    }
    if (p_74306_1_ == Options.STREAM_CHAT_ENABLED) {
      this.streamChatEnabled = ((this.streamChatEnabled + p_74306_2_) % 3);
    }
    if (p_74306_1_ == Options.STREAM_CHAT_USER_FILTER) {
      this.streamChatUserFilter = ((this.streamChatUserFilter + p_74306_2_) % 3);
    }
    if (p_74306_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
      this.streamMicToggleBehavior = ((this.streamMicToggleBehavior + p_74306_2_) % 2);
    }
    if (p_74306_1_ == Options.CHAT_COLOR) {
      this.chatColours = (!this.chatColours);
    }
    if (p_74306_1_ == Options.CHAT_LINKS) {
      this.chatLinks = (!this.chatLinks);
    }
    if (p_74306_1_ == Options.CHAT_LINKS_PROMPT) {
      this.chatLinksPrompt = (!this.chatLinksPrompt);
    }
    if (p_74306_1_ == Options.SNOOPER_ENABLED) {
      this.snooperEnabled = (!this.snooperEnabled);
    }
    if (p_74306_1_ == Options.TOUCHSCREEN) {
      this.touchscreen = (!this.touchscreen);
    }
    if (p_74306_1_ == Options.USE_FULLSCREEN)
    {
      this.fullScreen = (!this.fullScreen);
      if (this.mc.isFullScreen() != this.fullScreen) {
        this.mc.toggleFullscreen();
      }
    }
    if (p_74306_1_ == Options.ENABLE_VSYNC)
    {
      this.enableVsync = (!this.enableVsync);
      Display.setVSyncEnabled(this.enableVsync);
    }
    if (p_74306_1_ == Options.USE_VBO)
    {
      this.field_178881_t = (!this.field_178881_t);
      this.mc.renderGlobal.loadRenderers();
    }
    if (p_74306_1_ == Options.BLOCK_ALTERNATIVES)
    {
      this.field_178880_u = (!this.field_178880_u);
      this.mc.renderGlobal.loadRenderers();
    }
    if (p_74306_1_ == Options.REDUCED_DEBUG_INFO) {
      this.field_178879_v = (!this.field_178879_v);
    }
    saveOptions();
  }
  
  public float getOptionFloatValue(Options p_74296_1_)
  {
    return p_74296_1_ == Options.STREAM_FPS ? this.streamFps : p_74296_1_ == Options.STREAM_KBPS ? this.streamKbps : p_74296_1_ == Options.STREAM_VOLUME_SYSTEM ? this.streamGameVolume : p_74296_1_ == Options.STREAM_VOLUME_MIC ? this.streamMicVolume : p_74296_1_ == Options.STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : p_74296_1_ == Options.RENDER_DISTANCE ? this.renderDistanceChunks : p_74296_1_ == Options.MIPMAP_LEVELS ? this.mipmapLevels : p_74296_1_ == Options.FRAMERATE_LIMIT ? this.limitFramerate : p_74296_1_ == Options.CHAT_WIDTH ? this.chatWidth : p_74296_1_ == Options.CHAT_SCALE ? this.chatScale : p_74296_1_ == Options.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : p_74296_1_ == Options.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : p_74296_1_ == Options.CHAT_OPACITY ? this.chatOpacity : p_74296_1_ == Options.SENSITIVITY ? this.mouseSensitivity : p_74296_1_ == Options.SATURATION ? this.saturation : p_74296_1_ == Options.GAMMA ? this.gammaSetting : p_74296_1_ == Options.FOV ? this.fovSetting : p_74296_1_ == Options.FRAMERATE_LIMIT ? this.limitFramerate : (this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax()) && (this.enableVsync) ? 0.0F : p_74296_1_ == Options.MIPMAP_TYPE ? this.ofMipmapType : p_74296_1_ == Options.AF_LEVEL ? this.ofAfLevel : p_74296_1_ == Options.AA_LEVEL ? this.ofAaLevel : p_74296_1_ == Options.AO_LEVEL ? this.ofAoLevel : p_74296_1_ == Options.CLOUD_HEIGHT ? this.ofCloudsHeight : 0.0F;
  }
  
  public boolean getOptionOrdinalValue(Options p_74308_1_)
  {
    switch (SwitchOptions.optionIds[p_74308_1_.ordinal()])
    {
    case 1: 
      return this.invertMouse;
    case 2: 
      return this.viewBobbing;
    case 3: 
      return this.anaglyph;
    case 4: 
      return this.fboEnable;
    case 5: 
      return this.clouds;
    case 6: 
      return this.chatColours;
    case 7: 
      return this.chatLinks;
    case 8: 
      return this.chatLinksPrompt;
    case 9: 
      return this.snooperEnabled;
    case 10: 
      return this.fullScreen;
    case 11: 
      return this.enableVsync;
    case 12: 
      return this.field_178881_t;
    case 13: 
      return this.touchscreen;
    case 14: 
      return this.streamSendMetadata;
    case 15: 
      return this.forceUnicodeFont;
    case 16: 
      return this.field_178880_u;
    case 17: 
      return this.field_178879_v;
    }
    return false;
  }
  
  private static String getTranslation(String[] p_74299_0_, int p_74299_1_)
  {
    if ((p_74299_1_ < 0) || (p_74299_1_ >= p_74299_0_.length)) {
      p_74299_1_ = 0;
    }
    return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
  }
  
  public String getKeyBinding(Options p_74297_1_)
  {
    String strOF = getKeyBindingOF(p_74297_1_);
    if (strOF != null) {
      return strOF;
    }
    String var2 = I18n.format(p_74297_1_.getEnumString(), new Object[0]) + ": ";
    if (p_74297_1_.getEnumFloat())
    {
      float var32 = getOptionFloatValue(p_74297_1_);
      float var4 = p_74297_1_.normalizeValue(var32);
      return var2 + (int)(var4 * 100.0F) + "%";
    }
    if (p_74297_1_.getEnumBoolean())
    {
      boolean var31 = getOptionOrdinalValue(p_74297_1_);
      return var2 + I18n.format("options.off", new Object[0]);
    }
    if (p_74297_1_ == Options.GUI_SCALE) {
      return var2 + getTranslation(GUISCALES, this.guiScale);
    }
    if (p_74297_1_ == Options.CHAT_VISIBILITY) {
      return var2 + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
    }
    if (p_74297_1_ == Options.PARTICLES) {
      return var2 + getTranslation(PARTICLES, this.particleSetting);
    }
    if (p_74297_1_ == Options.AMBIENT_OCCLUSION) {
      return var2 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
    }
    if (p_74297_1_ == Options.STREAM_COMPRESSION) {
      return var2 + getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
    }
    if (p_74297_1_ == Options.STREAM_CHAT_ENABLED) {
      return var2 + getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
    }
    if (p_74297_1_ == Options.STREAM_CHAT_USER_FILTER) {
      return var2 + getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
    }
    if (p_74297_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
      return var2 + getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
    }
    if (p_74297_1_ == Options.GRAPHICS)
    {
      if (this.fancyGraphics) {
        return var2 + I18n.format("options.graphics.fancy", new Object[0]);
      }
      String var3 = "options.graphics.fast";
      return var2 + I18n.format("options.graphics.fast", new Object[0]);
    }
    return var2;
  }
  
  public void loadOptions()
  {
    try
    {
      if (!this.optionsFile.exists()) {
        return;
      }
      BufferedReader var9 = new BufferedReader(new FileReader(this.optionsFile));
      String var2 = "";
      this.mapSoundLevels.clear();
      while ((var2 = var9.readLine()) != null) {
        try
        {
          String[] var8 = var2.split(":");
          if (var8[0].equals("mouseSensitivity")) {
            this.mouseSensitivity = parseFloat(var8[1]);
          }
          if (var8[0].equals("fov")) {
            this.fovSetting = (parseFloat(var8[1]) * 40.0F + 70.0F);
          }
          if (var8[0].equals("gamma")) {
            this.gammaSetting = parseFloat(var8[1]);
          }
          if (var8[0].equals("saturation")) {
            this.saturation = parseFloat(var8[1]);
          }
          if (var8[0].equals("invertYMouse")) {
            this.invertMouse = var8[1].equals("true");
          }
          if (var8[0].equals("renderDistance")) {
            this.renderDistanceChunks = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("guiScale")) {
            this.guiScale = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("particles")) {
            this.particleSetting = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("bobView")) {
            this.viewBobbing = var8[1].equals("true");
          }
          if (var8[0].equals("anaglyph3d")) {
            this.anaglyph = var8[1].equals("true");
          }
          if (var8[0].equals("maxFps"))
          {
            this.limitFramerate = Integer.parseInt(var8[1]);
            this.enableVsync = false;
            if (this.limitFramerate <= 0)
            {
              this.limitFramerate = ((int)Options.FRAMERATE_LIMIT.getValueMax());
              this.enableVsync = true;
            }
            updateVSync();
          }
          if (var8[0].equals("fboEnable")) {
            this.fboEnable = var8[1].equals("true");
          }
          if (var8[0].equals("difficulty")) {
            this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(var8[1]));
          }
          if (var8[0].equals("fancyGraphics"))
          {
            this.fancyGraphics = var8[1].equals("true");
            updateRenderClouds();
          }
          if (var8[0].equals("ao")) {
            if (var8[1].equals("true")) {
              this.ambientOcclusion = 2;
            } else if (var8[1].equals("false")) {
              this.ambientOcclusion = 0;
            } else {
              this.ambientOcclusion = Integer.parseInt(var8[1]);
            }
          }
          if (var8[0].equals("renderClouds")) {
            this.clouds = var8[1].equals("true");
          }
          if (var8[0].equals("resourcePacks"))
          {
            this.resourcePacks = ((List)gson.fromJson(var2.substring(var2.indexOf(':') + 1), typeListString));
            if (this.resourcePacks == null) {
              this.resourcePacks = Lists.newArrayList();
            }
          }
          if ((var8[0].equals("lastServer")) && (var8.length >= 2)) {
            this.lastServer = var2.substring(var2.indexOf(':') + 1);
          }
          if ((var8[0].equals("lang")) && (var8.length >= 2)) {
            this.language = var8[1];
          }
          if (var8[0].equals("chatVisibility")) {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(var8[1]));
          }
          if (var8[0].equals("chatColors")) {
            this.chatColours = var8[1].equals("true");
          }
          if (var8[0].equals("chatLinks")) {
            this.chatLinks = var8[1].equals("true");
          }
          if (var8[0].equals("chatLinksPrompt")) {
            this.chatLinksPrompt = var8[1].equals("true");
          }
          if (var8[0].equals("chatOpacity")) {
            this.chatOpacity = parseFloat(var8[1]);
          }
          if (var8[0].equals("snooperEnabled")) {
            this.snooperEnabled = var8[1].equals("true");
          }
          if (var8[0].equals("fullscreen")) {
            this.fullScreen = var8[1].equals("true");
          }
          if (var8[0].equals("enableVsync"))
          {
            this.enableVsync = var8[1].equals("true");
            updateVSync();
          }
          if (var8[0].equals("useVbo")) {
            this.field_178881_t = var8[1].equals("true");
          }
          if (var8[0].equals("hideServerAddress")) {
            this.hideServerAddress = var8[1].equals("true");
          }
          if (var8[0].equals("advancedItemTooltips")) {
            this.advancedItemTooltips = var8[1].equals("true");
          }
          if (var8[0].equals("pauseOnLostFocus")) {
            this.pauseOnLostFocus = var8[1].equals("true");
          }
          if (var8[0].equals("touchscreen")) {
            this.touchscreen = var8[1].equals("true");
          }
          if (var8[0].equals("overrideHeight")) {
            this.overrideHeight = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("overrideWidth")) {
            this.overrideWidth = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("heldItemTooltips")) {
            this.heldItemTooltips = var8[1].equals("true");
          }
          if (var8[0].equals("chatHeightFocused")) {
            this.chatHeightFocused = parseFloat(var8[1]);
          }
          if (var8[0].equals("chatHeightUnfocused")) {
            this.chatHeightUnfocused = parseFloat(var8[1]);
          }
          if (var8[0].equals("chatScale")) {
            this.chatScale = parseFloat(var8[1]);
          }
          if (var8[0].equals("chatWidth")) {
            this.chatWidth = parseFloat(var8[1]);
          }
          if (var8[0].equals("showInventoryAchievementHint")) {
            this.showInventoryAchievementHint = var8[1].equals("true");
          }
          if (var8[0].equals("mipmapLevels")) {
            this.mipmapLevels = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("streamBytesPerPixel")) {
            this.streamBytesPerPixel = parseFloat(var8[1]);
          }
          if (var8[0].equals("streamMicVolume")) {
            this.streamMicVolume = parseFloat(var8[1]);
          }
          if (var8[0].equals("streamSystemVolume")) {
            this.streamGameVolume = parseFloat(var8[1]);
          }
          if (var8[0].equals("streamKbps")) {
            this.streamKbps = parseFloat(var8[1]);
          }
          if (var8[0].equals("streamFps")) {
            this.streamFps = parseFloat(var8[1]);
          }
          if (var8[0].equals("streamCompression")) {
            this.streamCompression = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("streamSendMetadata")) {
            this.streamSendMetadata = var8[1].equals("true");
          }
          if ((var8[0].equals("streamPreferredServer")) && (var8.length >= 2)) {
            this.streamPreferredServer = var2.substring(var2.indexOf(':') + 1);
          }
          if (var8[0].equals("streamChatEnabled")) {
            this.streamChatEnabled = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("streamChatUserFilter")) {
            this.streamChatUserFilter = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("streamMicToggleBehavior")) {
            this.streamMicToggleBehavior = Integer.parseInt(var8[1]);
          }
          if (var8[0].equals("forceUnicodeFont")) {
            this.forceUnicodeFont = var8[1].equals("true");
          }
          if (var8[0].equals("allowBlockAlternatives")) {
            this.field_178880_u = var8[1].equals("true");
          }
          if (var8[0].equals("reducedDebugInfo")) {
            this.field_178879_v = var8[1].equals("true");
          }
          KeyBinding[] var4 = this.keyBindings;
          int var5 = var4.length;
          for (int var6 = 0; var6 < var5; var6++)
          {
            KeyBinding var10 = var4[var6];
            if (var8[0].equals("key_" + var10.getKeyDescription())) {
              var10.setKeyCode(Integer.parseInt(var8[1]));
            }
          }
          SoundCategory[] var12 = SoundCategory.values();
          var5 = var12.length;
          for (var6 = 0; var6 < var5; var6++)
          {
            SoundCategory var11 = var12[var6];
            if (var8[0].equals("soundCategory_" + var11.getCategoryName())) {
              this.mapSoundLevels.put(var11, Float.valueOf(parseFloat(var8[1])));
            }
          }
          EnumPlayerModelParts[] var131 = EnumPlayerModelParts.values();
          var5 = var131.length;
          for (var6 = 0; var6 < var5; var6++)
          {
            EnumPlayerModelParts var13 = var131[var6];
            if (var8[0].equals("modelPart_" + var13.func_179329_c())) {
              func_178878_a(var13, var8[1].equals("true"));
            }
          }
        }
        catch (Exception var101)
        {
          logger.warn("Skipping bad option: " + var2);
          var101.printStackTrace();
        }
      }
      KeyBinding.resetKeyBindingArrayAndHash();
      var9.close();
    }
    catch (Exception var111)
    {
      logger.error("Failed to load options", var111);
    }
    loadOfOptions();
  }
  
  private float parseFloat(String p_74305_1_)
  {
    return p_74305_1_.equals("false") ? 0.0F : p_74305_1_.equals("true") ? 1.0F : Float.parseFloat(p_74305_1_);
  }
  
  public void saveOptions()
  {
    if (Reflector.FMLClientHandler.exists())
    {
      Object var6 = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
      if ((var6 != null) && (Reflector.callBoolean(var6, Reflector.FMLClientHandler_isLoading, new Object[0]))) {
        return;
      }
    }
    try
    {
      PrintWriter var9 = new PrintWriter(new FileWriter(this.optionsFile));
      var9.println("invertYMouse:" + this.invertMouse);
      var9.println("mouseSensitivity:" + this.mouseSensitivity);
      var9.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
      var9.println("gamma:" + this.gammaSetting);
      var9.println("saturation:" + this.saturation);
      var9.println("renderDistance:" + this.renderDistanceChunks);
      var9.println("guiScale:" + this.guiScale);
      var9.println("particles:" + this.particleSetting);
      var9.println("bobView:" + this.viewBobbing);
      var9.println("anaglyph3d:" + this.anaglyph);
      var9.println("maxFps:" + this.limitFramerate);
      var9.println("fboEnable:" + this.fboEnable);
      var9.println("difficulty:" + this.difficulty.getDifficultyId());
      var9.println("fancyGraphics:" + this.fancyGraphics);
      var9.println("ao:" + this.ambientOcclusion);
      var9.println("renderClouds:" + this.clouds);
      var9.println("resourcePacks:" + gson.toJson(this.resourcePacks));
      var9.println("lastServer:" + this.lastServer);
      var9.println("lang:" + this.language);
      var9.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
      var9.println("chatColors:" + this.chatColours);
      var9.println("chatLinks:" + this.chatLinks);
      var9.println("chatLinksPrompt:" + this.chatLinksPrompt);
      var9.println("chatOpacity:" + this.chatOpacity);
      var9.println("snooperEnabled:" + this.snooperEnabled);
      var9.println("fullscreen:" + this.fullScreen);
      var9.println("enableVsync:" + this.enableVsync);
      var9.println("useVbo:" + this.field_178881_t);
      var9.println("hideServerAddress:" + this.hideServerAddress);
      var9.println("advancedItemTooltips:" + this.advancedItemTooltips);
      var9.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
      var9.println("touchscreen:" + this.touchscreen);
      var9.println("overrideWidth:" + this.overrideWidth);
      var9.println("overrideHeight:" + this.overrideHeight);
      var9.println("heldItemTooltips:" + this.heldItemTooltips);
      var9.println("chatHeightFocused:" + this.chatHeightFocused);
      var9.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
      var9.println("chatScale:" + this.chatScale);
      var9.println("chatWidth:" + this.chatWidth);
      var9.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
      var9.println("mipmapLevels:" + this.mipmapLevels);
      var9.println("streamBytesPerPixel:" + this.streamBytesPerPixel);
      var9.println("streamMicVolume:" + this.streamMicVolume);
      var9.println("streamSystemVolume:" + this.streamGameVolume);
      var9.println("streamKbps:" + this.streamKbps);
      var9.println("streamFps:" + this.streamFps);
      var9.println("streamCompression:" + this.streamCompression);
      var9.println("streamSendMetadata:" + this.streamSendMetadata);
      var9.println("streamPreferredServer:" + this.streamPreferredServer);
      var9.println("streamChatEnabled:" + this.streamChatEnabled);
      var9.println("streamChatUserFilter:" + this.streamChatUserFilter);
      var9.println("streamMicToggleBehavior:" + this.streamMicToggleBehavior);
      var9.println("forceUnicodeFont:" + this.forceUnicodeFont);
      var9.println("allowBlockAlternatives:" + this.field_178880_u);
      var9.println("reducedDebugInfo:" + this.field_178879_v);
      KeyBinding[] var2 = this.keyBindings;
      int var3 = var2.length;
      for (int var4 = 0; var4 < var3; var4++)
      {
        KeyBinding var7 = var2[var4];
        var9.println("key_" + var7.getKeyDescription() + ":" + var7.getKeyCode());
      }
      SoundCategory[] var101 = SoundCategory.values();
      var3 = var101.length;
      for (var4 = 0; var4 < var3; var4++)
      {
        SoundCategory var8 = var101[var4];
        var9.println("soundCategory_" + var8.getCategoryName() + ":" + getSoundLevel(var8));
      }
      EnumPlayerModelParts[] var11 = EnumPlayerModelParts.values();
      var3 = var11.length;
      for (var4 = 0; var4 < var3; var4++)
      {
        EnumPlayerModelParts var10 = var11[var4];
        var9.println("modelPart_" + var10.func_179329_c() + ":" + this.field_178882_aU.contains(var10));
      }
      var9.close();
    }
    catch (Exception var81)
    {
      logger.error("Failed to save options", var81);
    }
    saveOfOptions();
    sendSettingsToServer();
  }
  
  public float getSoundLevel(SoundCategory p_151438_1_)
  {
    return this.mapSoundLevels.containsKey(p_151438_1_) ? ((Float)this.mapSoundLevels.get(p_151438_1_)).floatValue() : 1.0F;
  }
  
  public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_)
  {
    this.mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
    this.mapSoundLevels.put(p_151439_1_, Float.valueOf(p_151439_2_));
  }
  
  public void sendSettingsToServer()
  {
    if (Minecraft.thePlayer != null)
    {
      int var1 = 0;
      EnumPlayerModelParts var3;
      for (Iterator var2 = this.field_178882_aU.iterator(); var2.hasNext(); var1 |= var3.func_179327_a()) {
        var3 = (EnumPlayerModelParts)var2.next();
      }
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, var1));
    }
  }
  
  public Set func_178876_d()
  {
    return ImmutableSet.copyOf(this.field_178882_aU);
  }
  
  public void func_178878_a(EnumPlayerModelParts p_178878_1_, boolean p_178878_2_)
  {
    if (p_178878_2_) {
      this.field_178882_aU.add(p_178878_1_);
    } else {
      this.field_178882_aU.remove(p_178878_1_);
    }
    sendSettingsToServer();
  }
  
  public void func_178877_a(EnumPlayerModelParts p_178877_1_)
  {
    if (!func_178876_d().contains(p_178877_1_)) {
      this.field_178882_aU.add(p_178877_1_);
    } else {
      this.field_178882_aU.remove(p_178877_1_);
    }
    sendSettingsToServer();
  }
  
  public boolean shouldRenderClouds()
  {
    return (this.renderDistanceChunks >= 4) && (this.clouds);
  }
  
  private void setOptionFloatValueOF(Options option, float val)
  {
    if (option == Options.CLOUD_HEIGHT)
    {
      this.ofCloudsHeight = val;
      this.mc.renderGlobal.resetClouds();
    }
    if (option == Options.AO_LEVEL)
    {
      this.ofAoLevel = val;
      this.mc.renderGlobal.loadRenderers();
    }
    if (option == Options.AA_LEVEL)
    {
      int valInt = (int)val;
      if ((valInt > 0) && (Config.isShaders()))
      {
        Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
        return;
      }
      int[] aaLevels = { 0, 2, 4, 6, 8, 12, 16 };
      this.ofAaLevel = 0;
      for (int l = 0; l < aaLevels.length; l++) {
        if (valInt >= aaLevels[l]) {
          this.ofAaLevel = aaLevels[l];
        }
      }
      this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
    }
    if (option == Options.AF_LEVEL)
    {
      int valInt = (int)val;
      if ((valInt > 1) && (Config.isShaders()))
      {
        Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
        return;
      }
      for (this.ofAfLevel = 1; this.ofAfLevel * 2 <= valInt; this.ofAfLevel *= 2) {}
      this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
      this.mc.refreshResources();
    }
    if (option == Options.MIPMAP_TYPE)
    {
      int valInt = (int)val;
      this.ofMipmapType = Config.limit(valInt, 0, 3);
      this.mc.refreshResources();
    }
  }
  
  private void setOptionValueOF(Options par1EnumOptions, int par2)
  {
    if (par1EnumOptions == Options.FOG_FANCY) {
      switch (this.ofFogType)
      {
      case 1: 
        this.ofFogType = 2;
        if (!Config.isFancyFogAvailable()) {
          this.ofFogType = 3;
        }
        break;
      case 2: 
        this.ofFogType = 3;
        break;
      case 3: 
        this.ofFogType = 1;
        break;
      default: 
        this.ofFogType = 1;
      }
    }
    if (par1EnumOptions == Options.FOG_START)
    {
      this.ofFogStart += 0.2F;
      if (this.ofFogStart > 0.81F) {
        this.ofFogStart = 0.2F;
      }
    }
    if (par1EnumOptions == Options.SMOOTH_FPS) {
      this.ofSmoothFps = (!this.ofSmoothFps);
    }
    if (par1EnumOptions == Options.SMOOTH_WORLD)
    {
      this.ofSmoothWorld = (!this.ofSmoothWorld);
      Config.updateThreadPriorities();
    }
    if (par1EnumOptions == Options.CLOUDS)
    {
      this.ofClouds += 1;
      if (this.ofClouds > 3) {
        this.ofClouds = 0;
      }
      updateRenderClouds();
      this.mc.renderGlobal.resetClouds();
    }
    if (par1EnumOptions == Options.TREES)
    {
      this.ofTrees = nextValue(this.ofTrees, OF_TREES_VALUES);
      this.mc.renderGlobal.loadRenderers();
    }
    if (par1EnumOptions == Options.DROPPED_ITEMS)
    {
      this.ofDroppedItems += 1;
      if (this.ofDroppedItems > 2) {
        this.ofDroppedItems = 0;
      }
    }
    if (par1EnumOptions == Options.RAIN)
    {
      this.ofRain += 1;
      if (this.ofRain > 3) {
        this.ofRain = 0;
      }
    }
    if (par1EnumOptions == Options.ANIMATED_WATER)
    {
      this.ofAnimatedWater += 1;
      if (this.ofAnimatedWater == 1) {
        this.ofAnimatedWater += 1;
      }
      if (this.ofAnimatedWater > 2) {
        this.ofAnimatedWater = 0;
      }
    }
    if (par1EnumOptions == Options.ANIMATED_LAVA)
    {
      this.ofAnimatedLava += 1;
      if (this.ofAnimatedLava == 1) {
        this.ofAnimatedLava += 1;
      }
      if (this.ofAnimatedLava > 2) {
        this.ofAnimatedLava = 0;
      }
    }
    if (par1EnumOptions == Options.ANIMATED_FIRE) {
      this.ofAnimatedFire = (!this.ofAnimatedFire);
    }
    if (par1EnumOptions == Options.ANIMATED_PORTAL) {
      this.ofAnimatedPortal = (!this.ofAnimatedPortal);
    }
    if (par1EnumOptions == Options.ANIMATED_REDSTONE) {
      this.ofAnimatedRedstone = (!this.ofAnimatedRedstone);
    }
    if (par1EnumOptions == Options.ANIMATED_EXPLOSION) {
      this.ofAnimatedExplosion = (!this.ofAnimatedExplosion);
    }
    if (par1EnumOptions == Options.ANIMATED_FLAME) {
      this.ofAnimatedFlame = (!this.ofAnimatedFlame);
    }
    if (par1EnumOptions == Options.ANIMATED_SMOKE) {
      this.ofAnimatedSmoke = (!this.ofAnimatedSmoke);
    }
    if (par1EnumOptions == Options.VOID_PARTICLES) {
      this.ofVoidParticles = (!this.ofVoidParticles);
    }
    if (par1EnumOptions == Options.WATER_PARTICLES) {
      this.ofWaterParticles = (!this.ofWaterParticles);
    }
    if (par1EnumOptions == Options.PORTAL_PARTICLES) {
      this.ofPortalParticles = (!this.ofPortalParticles);
    }
    if (par1EnumOptions == Options.POTION_PARTICLES) {
      this.ofPotionParticles = (!this.ofPotionParticles);
    }
    if (par1EnumOptions == Options.FIREWORK_PARTICLES) {
      this.ofFireworkParticles = (!this.ofFireworkParticles);
    }
    if (par1EnumOptions == Options.DRIPPING_WATER_LAVA) {
      this.ofDrippingWaterLava = (!this.ofDrippingWaterLava);
    }
    if (par1EnumOptions == Options.ANIMATED_TERRAIN) {
      this.ofAnimatedTerrain = (!this.ofAnimatedTerrain);
    }
    if (par1EnumOptions == Options.ANIMATED_TEXTURES) {
      this.ofAnimatedTextures = (!this.ofAnimatedTextures);
    }
    if (par1EnumOptions == Options.RAIN_SPLASH) {
      this.ofRainSplash = (!this.ofRainSplash);
    }
    if (par1EnumOptions == Options.LAGOMETER) {
      this.ofLagometer = (!this.ofLagometer);
    }
    if (par1EnumOptions == Options.SHOW_FPS) {
      this.ofShowFps = (!this.ofShowFps);
    }
    if (par1EnumOptions == Options.AUTOSAVE_TICKS)
    {
      this.ofAutoSaveTicks *= 10;
      if (this.ofAutoSaveTicks > 40000) {
        this.ofAutoSaveTicks = 40;
      }
    }
    if (par1EnumOptions == Options.BETTER_GRASS)
    {
      this.ofBetterGrass += 1;
      if (this.ofBetterGrass > 3) {
        this.ofBetterGrass = 1;
      }
      this.mc.renderGlobal.loadRenderers();
    }
    if (par1EnumOptions == Options.CONNECTED_TEXTURES)
    {
      this.ofConnectedTextures += 1;
      if (this.ofConnectedTextures > 3) {
        this.ofConnectedTextures = 1;
      }
      if (this.ofConnectedTextures != 2) {
        this.mc.refreshResources();
      }
    }
    if (par1EnumOptions == Options.WEATHER) {
      this.ofWeather = (!this.ofWeather);
    }
    if (par1EnumOptions == Options.SKY) {
      this.ofSky = (!this.ofSky);
    }
    if (par1EnumOptions == Options.STARS) {
      this.ofStars = (!this.ofStars);
    }
    if (par1EnumOptions == Options.SUN_MOON) {
      this.ofSunMoon = (!this.ofSunMoon);
    }
    if (par1EnumOptions == Options.VIGNETTE)
    {
      this.ofVignette += 1;
      if (this.ofVignette > 2) {
        this.ofVignette = 0;
      }
    }
    if (par1EnumOptions == Options.CHUNK_UPDATES)
    {
      this.ofChunkUpdates += 1;
      if (this.ofChunkUpdates > 5) {
        this.ofChunkUpdates = 1;
      }
    }
    if (par1EnumOptions == Options.CHUNK_UPDATES_DYNAMIC) {
      this.ofChunkUpdatesDynamic = (!this.ofChunkUpdatesDynamic);
    }
    if (par1EnumOptions == Options.TIME)
    {
      this.ofTime += 1;
      if (this.ofTime > 2) {
        this.ofTime = 0;
      }
    }
    if (par1EnumOptions == Options.CLEAR_WATER)
    {
      this.ofClearWater = (!this.ofClearWater);
      updateWaterOpacity();
    }
    if (par1EnumOptions == Options.PROFILER) {
      this.ofProfiler = (!this.ofProfiler);
    }
    if (par1EnumOptions == Options.BETTER_SNOW)
    {
      this.ofBetterSnow = (!this.ofBetterSnow);
      this.mc.renderGlobal.loadRenderers();
    }
    if (par1EnumOptions == Options.SWAMP_COLORS)
    {
      this.ofSwampColors = (!this.ofSwampColors);
      CustomColors.updateUseDefaultGrassFoliageColors();
      this.mc.renderGlobal.loadRenderers();
    }
    if (par1EnumOptions == Options.RANDOM_MOBS)
    {
      this.ofRandomMobs = (!this.ofRandomMobs);
      RandomMobs.resetTextures();
    }
    if (par1EnumOptions == Options.SMOOTH_BIOMES)
    {
      this.ofSmoothBiomes = (!this.ofSmoothBiomes);
      CustomColors.updateUseDefaultGrassFoliageColors();
      this.mc.renderGlobal.loadRenderers();
    }
    if (par1EnumOptions == Options.CUSTOM_FONTS)
    {
      this.ofCustomFonts = (!this.ofCustomFonts);
      Minecraft.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
      this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
    }
    if (par1EnumOptions == Options.CUSTOM_COLORS)
    {
      this.ofCustomColors = (!this.ofCustomColors);
      CustomColors.update();
      this.mc.renderGlobal.loadRenderers();
    }
    if (par1EnumOptions == Options.CUSTOM_ITEMS)
    {
      this.ofCustomItems = (!this.ofCustomItems);
      this.mc.refreshResources();
    }
    if (par1EnumOptions == Options.CUSTOM_SKY)
    {
      this.ofCustomSky = (!this.ofCustomSky);
      CustomSky.update();
    }
    if (par1EnumOptions == Options.SHOW_CAPES) {
      this.ofShowCapes = (!this.ofShowCapes);
    }
    if (par1EnumOptions == Options.NATURAL_TEXTURES)
    {
      this.ofNaturalTextures = (!this.ofNaturalTextures);
      NaturalTextures.update();
      this.mc.renderGlobal.loadRenderers();
    }
    if (par1EnumOptions == Options.FAST_MATH)
    {
      this.ofFastMath = (!this.ofFastMath);
      MathHelper.fastMath = this.ofFastMath;
    }
    if (par1EnumOptions == Options.FAST_RENDER)
    {
      if ((!this.ofFastRender) && (Config.isShaders()))
      {
        Config.showGuiMessage(Lang.get("of.message.fr.shaders1"), Lang.get("of.message.fr.shaders2"));
        return;
      }
      this.ofFastRender = (!this.ofFastRender);
      if (this.ofFastRender) {
        this.mc.entityRenderer.stopUseShader();
      }
      Config.updateFramebufferSize();
    }
    if (par1EnumOptions == Options.TRANSLUCENT_BLOCKS)
    {
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
    if (par1EnumOptions == Options.LAZY_CHUNK_LOADING)
    {
      this.ofLazyChunkLoading = (!this.ofLazyChunkLoading);
      Config.updateAvailableProcessors();
      if (!Config.isSingleProcessor()) {
        this.ofLazyChunkLoading = false;
      }
      this.mc.renderGlobal.loadRenderers();
    }
    if (par1EnumOptions == Options.FULLSCREEN_MODE)
    {
      List modeList = Arrays.asList(Config.getFullscreenModes());
      if (this.ofFullscreenMode.equals("Default"))
      {
        this.ofFullscreenMode = ((String)modeList.get(0));
      }
      else
      {
        int index = modeList.indexOf(this.ofFullscreenMode);
        if (index < 0)
        {
          this.ofFullscreenMode = "Default";
        }
        else
        {
          index++;
          if (index >= modeList.size()) {
            this.ofFullscreenMode = "Default";
          } else {
            this.ofFullscreenMode = ((String)modeList.get(index));
          }
        }
      }
    }
    if (par1EnumOptions == Options.DYNAMIC_FOV) {
      this.ofDynamicFov = (!this.ofDynamicFov);
    }
    if (par1EnumOptions == Options.DYNAMIC_LIGHTS)
    {
      this.ofDynamicLights = nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
      DynamicLights.removeLights(this.mc.renderGlobal);
    }
    if (par1EnumOptions == Options.HELD_ITEM_TOOLTIPS) {
      this.heldItemTooltips = (!this.heldItemTooltips);
    }
  }
  
  private String getKeyBindingOF(Options par1EnumOptions)
  {
    String var2 = I18n.format(par1EnumOptions.getEnumString(), new Object[0]) + ": ";
    if (var2 == null) {
      var2 = par1EnumOptions.getEnumString();
    }
    if (par1EnumOptions == Options.RENDER_DISTANCE)
    {
      int var61 = (int)getOptionFloatValue(par1EnumOptions);
      String str = I18n.format("options.renderDistance.tiny", new Object[0]);
      byte baseDist = 2;
      if (var61 >= 4)
      {
        str = I18n.format("options.renderDistance.short", new Object[0]);
        baseDist = 4;
      }
      if (var61 >= 8)
      {
        str = I18n.format("options.renderDistance.normal", new Object[0]);
        baseDist = 8;
      }
      if (var61 >= 16)
      {
        str = I18n.format("options.renderDistance.far", new Object[0]);
        baseDist = 16;
      }
      if (var61 >= 32)
      {
        str = Lang.get("of.options.renderDistance.extreme");
        baseDist = 32;
      }
      int diff = this.renderDistanceChunks - baseDist;
      String descr = str;
      if (diff > 0) {
        descr = str + "+";
      }
      return var2 + var61 + " " + descr + "";
    }
    if (par1EnumOptions == Options.FOG_FANCY)
    {
      switch (this.ofFogType)
      {
      case 1: 
        return var2 + Lang.getFast();
      case 2: 
        return var2 + Lang.getFancy();
      case 3: 
        return var2 + Lang.getOff();
      }
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.FOG_START) {
      return var2 + this.ofFogStart;
    }
    if (par1EnumOptions == Options.MIPMAP_TYPE)
    {
      switch (this.ofMipmapType)
      {
      case 0: 
        return var2 + Lang.get("of.options.mipmap.nearest");
      case 1: 
        return var2 + Lang.get("of.options.mipmap.linear");
      case 2: 
        return var2 + Lang.get("of.options.mipmap.bilinear");
      case 3: 
        return var2 + Lang.get("of.options.mipmap.trilinear");
      }
      return var2 + "of.options.mipmap.nearest";
    }
    if (par1EnumOptions == Options.SMOOTH_FPS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.SMOOTH_WORLD) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.CLOUDS)
    {
      switch (this.ofClouds)
      {
      case 1: 
        return var2 + Lang.getFast();
      case 2: 
        return var2 + Lang.getFancy();
      case 3: 
        return var2 + Lang.getOff();
      }
      return var2 + Lang.getDefault();
    }
    if (par1EnumOptions == Options.TREES)
    {
      switch (this.ofTrees)
      {
      case 1: 
        return var2 + Lang.getFast();
      case 2: 
        return var2 + Lang.getFancy();
      case 3: 
      default: 
        return var2 + Lang.getDefault();
      }
      return var2 + Lang.get("of.general.smart");
    }
    if (par1EnumOptions == Options.DROPPED_ITEMS)
    {
      switch (this.ofDroppedItems)
      {
      case 1: 
        return var2 + Lang.getFast();
      case 2: 
        return var2 + Lang.getFancy();
      }
      return var2 + Lang.getDefault();
    }
    if (par1EnumOptions == Options.RAIN)
    {
      switch (this.ofRain)
      {
      case 1: 
        return var2 + Lang.getFast();
      case 2: 
        return var2 + Lang.getFancy();
      case 3: 
        return var2 + Lang.getOff();
      }
      return var2 + Lang.getDefault();
    }
    if (par1EnumOptions == Options.ANIMATED_WATER)
    {
      switch (this.ofAnimatedWater)
      {
      case 1: 
        return var2 + Lang.get("of.options.animation.dynamic");
      case 2: 
        return var2 + Lang.getOff();
      }
      return var2 + Lang.getOn();
    }
    if (par1EnumOptions == Options.ANIMATED_LAVA)
    {
      switch (this.ofAnimatedLava)
      {
      case 1: 
        return var2 + Lang.get("of.options.animation.dynamic");
      case 2: 
        return var2 + Lang.getOff();
      }
      return var2 + Lang.getOn();
    }
    if (par1EnumOptions == Options.ANIMATED_FIRE) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.ANIMATED_PORTAL) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.ANIMATED_REDSTONE) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.ANIMATED_EXPLOSION) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.ANIMATED_FLAME) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.ANIMATED_SMOKE) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.VOID_PARTICLES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.WATER_PARTICLES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.PORTAL_PARTICLES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.POTION_PARTICLES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.FIREWORK_PARTICLES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.DRIPPING_WATER_LAVA) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.ANIMATED_TERRAIN) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.ANIMATED_TEXTURES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.RAIN_SPLASH) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.LAGOMETER) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.SHOW_FPS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.AUTOSAVE_TICKS) {
      return var2 + Lang.get("of.options.save.30min");
    }
    if (par1EnumOptions == Options.BETTER_GRASS)
    {
      switch (this.ofBetterGrass)
      {
      case 1: 
        return var2 + Lang.getFast();
      case 2: 
        return var2 + Lang.getFancy();
      }
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.CONNECTED_TEXTURES)
    {
      switch (this.ofConnectedTextures)
      {
      case 1: 
        return var2 + Lang.getFast();
      case 2: 
        return var2 + Lang.getFancy();
      }
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.WEATHER) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.SKY) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.STARS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.SUN_MOON) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.VIGNETTE)
    {
      switch (this.ofVignette)
      {
      case 1: 
        return var2 + Lang.getFast();
      case 2: 
        return var2 + Lang.getFancy();
      }
      return var2 + Lang.getDefault();
    }
    if (par1EnumOptions == Options.CHUNK_UPDATES) {
      return var2 + this.ofChunkUpdates;
    }
    if (par1EnumOptions == Options.CHUNK_UPDATES_DYNAMIC) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.TIME) {
      return var2 + Lang.getDefault();
    }
    if (par1EnumOptions == Options.CLEAR_WATER) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.AA_LEVEL)
    {
      String var62 = "";
      if (this.ofAaLevel != Config.getAntialiasingLevel()) {
        var62 = " (" + Lang.get("of.general.restart") + ")";
      }
      return var2 + this.ofAaLevel + var62;
    }
    if (par1EnumOptions == Options.AF_LEVEL) {
      return var2 + this.ofAfLevel;
    }
    if (par1EnumOptions == Options.PROFILER) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.BETTER_SNOW) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.SWAMP_COLORS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.RANDOM_MOBS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.SMOOTH_BIOMES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.CUSTOM_FONTS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.CUSTOM_COLORS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.CUSTOM_SKY) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.SHOW_CAPES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.CUSTOM_ITEMS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.NATURAL_TEXTURES) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.FAST_MATH) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.FAST_RENDER) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.TRANSLUCENT_BLOCKS) {
      return var2 + Lang.getDefault();
    }
    if (par1EnumOptions == Options.LAZY_CHUNK_LOADING) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.DYNAMIC_FOV) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.DYNAMIC_LIGHTS)
    {
      int var61 = indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
      return var2 + getTranslation(KEYS_DYNAMIC_LIGHTS, var61);
    }
    if (par1EnumOptions == Options.FULLSCREEN_MODE) {
      return var2 + this.ofFullscreenMode;
    }
    if (par1EnumOptions == Options.HELD_ITEM_TOOLTIPS) {
      return var2 + Lang.getOff();
    }
    if (par1EnumOptions == Options.FRAMERATE_LIMIT)
    {
      float var6 = getOptionFloatValue(par1EnumOptions);
      return var2 + (int)var6 + " fps";
    }
    return null;
  }
  
  public void loadOfOptions()
  {
    try
    {
      File exception = this.optionsFileOF;
      if (!exception.exists()) {
        exception = this.optionsFile;
      }
      if (!exception.exists()) {
        return;
      }
      BufferedReader bufferedreader = new BufferedReader(new FileReader(exception));
      String s = "";
      while ((s = bufferedreader.readLine()) != null) {
        try
        {
          String[] exception1 = s.split(":");
          if ((exception1[0].equals("ofRenderDistanceChunks")) && (exception1.length >= 2))
          {
            this.renderDistanceChunks = Integer.valueOf(exception1[1]).intValue();
            this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 32);
          }
          if ((exception1[0].equals("ofFogType")) && (exception1.length >= 2))
          {
            this.ofFogType = Integer.valueOf(exception1[1]).intValue();
            this.ofFogType = Config.limit(this.ofFogType, 1, 3);
          }
          if ((exception1[0].equals("ofFogStart")) && (exception1.length >= 2))
          {
            this.ofFogStart = Float.valueOf(exception1[1]).floatValue();
            if (this.ofFogStart < 0.2F) {
              this.ofFogStart = 0.2F;
            }
            if (this.ofFogStart > 0.81F) {
              this.ofFogStart = 0.8F;
            }
          }
          if ((exception1[0].equals("ofMipmapType")) && (exception1.length >= 2))
          {
            this.ofMipmapType = Integer.valueOf(exception1[1]).intValue();
            this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
          }
          if ((exception1[0].equals("ofOcclusionFancy")) && (exception1.length >= 2)) {
            this.ofOcclusionFancy = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofSmoothFps")) && (exception1.length >= 2)) {
            this.ofSmoothFps = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofSmoothWorld")) && (exception1.length >= 2)) {
            this.ofSmoothWorld = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAoLevel")) && (exception1.length >= 2))
          {
            this.ofAoLevel = Float.valueOf(exception1[1]).floatValue();
            this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
          }
          if ((exception1[0].equals("ofClouds")) && (exception1.length >= 2))
          {
            this.ofClouds = Integer.valueOf(exception1[1]).intValue();
            this.ofClouds = Config.limit(this.ofClouds, 0, 3);
            updateRenderClouds();
          }
          if ((exception1[0].equals("ofCloudsHeight")) && (exception1.length >= 2))
          {
            this.ofCloudsHeight = Float.valueOf(exception1[1]).floatValue();
            this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
          }
          if ((exception1[0].equals("ofTrees")) && (exception1.length >= 2))
          {
            this.ofTrees = Integer.valueOf(exception1[1]).intValue();
            this.ofTrees = limit(this.ofTrees, OF_TREES_VALUES);
          }
          if ((exception1[0].equals("ofDroppedItems")) && (exception1.length >= 2))
          {
            this.ofDroppedItems = Integer.valueOf(exception1[1]).intValue();
            this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
          }
          if ((exception1[0].equals("ofRain")) && (exception1.length >= 2))
          {
            this.ofRain = Integer.valueOf(exception1[1]).intValue();
            this.ofRain = Config.limit(this.ofRain, 0, 3);
          }
          if ((exception1[0].equals("ofAnimatedWater")) && (exception1.length >= 2))
          {
            this.ofAnimatedWater = Integer.valueOf(exception1[1]).intValue();
            this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
          }
          if ((exception1[0].equals("ofAnimatedLava")) && (exception1.length >= 2))
          {
            this.ofAnimatedLava = Integer.valueOf(exception1[1]).intValue();
            this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
          }
          if ((exception1[0].equals("ofAnimatedFire")) && (exception1.length >= 2)) {
            this.ofAnimatedFire = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAnimatedPortal")) && (exception1.length >= 2)) {
            this.ofAnimatedPortal = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAnimatedRedstone")) && (exception1.length >= 2)) {
            this.ofAnimatedRedstone = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAnimatedExplosion")) && (exception1.length >= 2)) {
            this.ofAnimatedExplosion = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAnimatedFlame")) && (exception1.length >= 2)) {
            this.ofAnimatedFlame = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAnimatedSmoke")) && (exception1.length >= 2)) {
            this.ofAnimatedSmoke = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofVoidParticles")) && (exception1.length >= 2)) {
            this.ofVoidParticles = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofWaterParticles")) && (exception1.length >= 2)) {
            this.ofWaterParticles = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofPortalParticles")) && (exception1.length >= 2)) {
            this.ofPortalParticles = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofPotionParticles")) && (exception1.length >= 2)) {
            this.ofPotionParticles = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofFireworkParticles")) && (exception1.length >= 2)) {
            this.ofFireworkParticles = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofDrippingWaterLava")) && (exception1.length >= 2)) {
            this.ofDrippingWaterLava = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAnimatedTerrain")) && (exception1.length >= 2)) {
            this.ofAnimatedTerrain = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAnimatedTextures")) && (exception1.length >= 2)) {
            this.ofAnimatedTextures = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofRainSplash")) && (exception1.length >= 2)) {
            this.ofRainSplash = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofLagometer")) && (exception1.length >= 2)) {
            this.ofLagometer = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofShowFps")) && (exception1.length >= 2)) {
            this.ofShowFps = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofAutoSaveTicks")) && (exception1.length >= 2))
          {
            this.ofAutoSaveTicks = Integer.valueOf(exception1[1]).intValue();
            this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
          }
          if ((exception1[0].equals("ofBetterGrass")) && (exception1.length >= 2))
          {
            this.ofBetterGrass = Integer.valueOf(exception1[1]).intValue();
            this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
          }
          if ((exception1[0].equals("ofConnectedTextures")) && (exception1.length >= 2))
          {
            this.ofConnectedTextures = Integer.valueOf(exception1[1]).intValue();
            this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
          }
          if ((exception1[0].equals("ofWeather")) && (exception1.length >= 2)) {
            this.ofWeather = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofSky")) && (exception1.length >= 2)) {
            this.ofSky = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofStars")) && (exception1.length >= 2)) {
            this.ofStars = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofSunMoon")) && (exception1.length >= 2)) {
            this.ofSunMoon = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofVignette")) && (exception1.length >= 2))
          {
            this.ofVignette = Integer.valueOf(exception1[1]).intValue();
            this.ofVignette = Config.limit(this.ofVignette, 0, 2);
          }
          if ((exception1[0].equals("ofChunkUpdates")) && (exception1.length >= 2))
          {
            this.ofChunkUpdates = Integer.valueOf(exception1[1]).intValue();
            this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
          }
          if ((exception1[0].equals("ofChunkUpdatesDynamic")) && (exception1.length >= 2)) {
            this.ofChunkUpdatesDynamic = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofTime")) && (exception1.length >= 2))
          {
            this.ofTime = Integer.valueOf(exception1[1]).intValue();
            this.ofTime = Config.limit(this.ofTime, 0, 2);
          }
          if ((exception1[0].equals("ofClearWater")) && (exception1.length >= 2))
          {
            this.ofClearWater = Boolean.valueOf(exception1[1]).booleanValue();
            updateWaterOpacity();
          }
          if ((exception1[0].equals("ofAaLevel")) && (exception1.length >= 2))
          {
            this.ofAaLevel = Integer.valueOf(exception1[1]).intValue();
            this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
          }
          if ((exception1[0].equals("ofAfLevel")) && (exception1.length >= 2))
          {
            this.ofAfLevel = Integer.valueOf(exception1[1]).intValue();
            this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
          }
          if ((exception1[0].equals("ofProfiler")) && (exception1.length >= 2)) {
            this.ofProfiler = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofBetterSnow")) && (exception1.length >= 2)) {
            this.ofBetterSnow = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofSwampColors")) && (exception1.length >= 2)) {
            this.ofSwampColors = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofRandomMobs")) && (exception1.length >= 2)) {
            this.ofRandomMobs = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofSmoothBiomes")) && (exception1.length >= 2)) {
            this.ofSmoothBiomes = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofCustomFonts")) && (exception1.length >= 2)) {
            this.ofCustomFonts = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofCustomColors")) && (exception1.length >= 2)) {
            this.ofCustomColors = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofCustomItems")) && (exception1.length >= 2)) {
            this.ofCustomItems = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofCustomSky")) && (exception1.length >= 2)) {
            this.ofCustomSky = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofShowCapes")) && (exception1.length >= 2)) {
            this.ofShowCapes = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofNaturalTextures")) && (exception1.length >= 2)) {
            this.ofNaturalTextures = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofLazyChunkLoading")) && (exception1.length >= 2)) {
            this.ofLazyChunkLoading = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofDynamicFov")) && (exception1.length >= 2)) {
            this.ofDynamicFov = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofDynamicLights")) && (exception1.length >= 2))
          {
            this.ofDynamicLights = Integer.valueOf(exception1[1]).intValue();
            this.ofDynamicLights = limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
          }
          if ((exception1[0].equals("ofFullscreenMode")) && (exception1.length >= 2)) {
            this.ofFullscreenMode = exception1[1];
          }
          if ((exception1[0].equals("ofFastMath")) && (exception1.length >= 2))
          {
            this.ofFastMath = Boolean.valueOf(exception1[1]).booleanValue();
            MathHelper.fastMath = this.ofFastMath;
          }
          if ((exception1[0].equals("ofFastRender")) && (exception1.length >= 2)) {
            this.ofFastRender = Boolean.valueOf(exception1[1]).booleanValue();
          }
          if ((exception1[0].equals("ofTranslucentBlocks")) && (exception1.length >= 2))
          {
            this.ofTranslucentBlocks = Integer.valueOf(exception1[1]).intValue();
            this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
          }
          if (exception1[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) {
            this.ofKeyBindZoom.setKeyCode(Integer.parseInt(exception1[1]));
          }
        }
        catch (Exception var5)
        {
          Config.dbg("Skipping bad option: " + s);
          var5.printStackTrace();
        }
      }
      KeyBinding.resetKeyBindingArrayAndHash();
      bufferedreader.close();
    }
    catch (Exception var6)
    {
      Config.warn("Failed to load options");
      var6.printStackTrace();
    }
  }
  
  public void saveOfOptions()
  {
    try
    {
      PrintWriter exception = new PrintWriter(new FileWriter(this.optionsFileOF));
      exception.println("ofRenderDistanceChunks:" + this.renderDistanceChunks);
      exception.println("ofFogType:" + this.ofFogType);
      exception.println("ofFogStart:" + this.ofFogStart);
      exception.println("ofMipmapType:" + this.ofMipmapType);
      exception.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
      exception.println("ofSmoothFps:" + this.ofSmoothFps);
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
      exception.println("ofFireworkParticles:" + this.ofFireworkParticles);
      exception.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
      exception.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
      exception.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
      exception.println("ofRainSplash:" + this.ofRainSplash);
      exception.println("ofLagometer:" + this.ofLagometer);
      exception.println("ofShowFps:" + this.ofShowFps);
      exception.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
      exception.println("ofBetterGrass:" + this.ofBetterGrass);
      exception.println("ofConnectedTextures:" + this.ofConnectedTextures);
      exception.println("ofWeather:" + this.ofWeather);
      exception.println("ofSky:" + this.ofSky);
      exception.println("ofStars:" + this.ofStars);
      exception.println("ofSunMoon:" + this.ofSunMoon);
      exception.println("ofVignette:" + this.ofVignette);
      exception.println("ofChunkUpdates:" + this.ofChunkUpdates);
      exception.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
      exception.println("ofTime:" + this.ofTime);
      exception.println("ofClearWater:" + this.ofClearWater);
      exception.println("ofAaLevel:" + this.ofAaLevel);
      exception.println("ofAfLevel:" + this.ofAfLevel);
      exception.println("ofProfiler:" + this.ofProfiler);
      exception.println("ofBetterSnow:" + this.ofBetterSnow);
      exception.println("ofSwampColors:" + this.ofSwampColors);
      exception.println("ofRandomMobs:" + this.ofRandomMobs);
      exception.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
      exception.println("ofCustomFonts:" + this.ofCustomFonts);
      exception.println("ofCustomColors:" + this.ofCustomColors);
      exception.println("ofCustomItems:" + this.ofCustomItems);
      exception.println("ofCustomSky:" + this.ofCustomSky);
      exception.println("ofShowCapes:" + this.ofShowCapes);
      exception.println("ofNaturalTextures:" + this.ofNaturalTextures);
      exception.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
      exception.println("ofDynamicFov:" + this.ofDynamicFov);
      exception.println("ofDynamicLights:" + this.ofDynamicLights);
      exception.println("ofFullscreenMode:" + this.ofFullscreenMode);
      exception.println("ofFastMath:" + this.ofFastMath);
      exception.println("ofFastRender:" + this.ofFastRender);
      exception.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
      exception.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
      exception.close();
    }
    catch (Exception var2)
    {
      Config.warn("Failed to save options");
      var2.printStackTrace();
    }
  }
  
  private void updateRenderClouds()
  {
    switch (this.ofClouds)
    {
    case 1: 
    case 2: 
    default: 
      this.clouds = true;
      break;
    case 3: 
      this.clouds = false;
    }
  }
  
  public void resetSettings()
  {
    this.renderDistanceChunks = 8;
    this.viewBobbing = true;
    this.anaglyph = false;
    this.limitFramerate = ((int)Options.FRAMERATE_LIMIT.getValueMax());
    this.enableVsync = false;
    updateVSync();
    this.mipmapLevels = 4;
    this.fancyGraphics = true;
    this.ambientOcclusion = 2;
    this.clouds = true;
    this.fovSetting = 70.0F;
    this.gammaSetting = 0.0F;
    this.guiScale = 0;
    this.particleSetting = 0;
    this.heldItemTooltips = true;
    this.field_178881_t = false;
    this.field_178880_u = true;
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
    this.ofFastRender = false;
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
    updateWaterOpacity();
    this.mc.refreshResources();
    saveOptions();
  }
  
  public void updateVSync()
  {
    Display.setVSyncEnabled(this.enableVsync);
  }
  
  private void updateWaterOpacity()
  {
    if ((this.mc.isIntegratedServerRunning()) && (this.mc.getIntegratedServer() != null)) {
      Config.waterOpacityChanged = true;
    }
    ClearWater.updateWaterOpacity(this, Minecraft.theWorld);
  }
  
  public void setAllAnimations(boolean flag)
  {
    int animVal = flag ? 0 : 2;
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
    this.ofFireworkParticles = flag;
    this.particleSetting = (flag ? 0 : 2);
    this.ofDrippingWaterLava = flag;
    this.ofAnimatedTerrain = flag;
    this.ofAnimatedTextures = flag;
  }
  
  private static int nextValue(int val, int[] vals)
  {
    int index = indexOf(val, vals);
    if (index < 0) {
      return vals[0];
    }
    index++;
    if (index >= vals.length) {
      index = 0;
    }
    return vals[index];
  }
  
  private static int limit(int val, int[] vals)
  {
    int index = indexOf(val, vals);
    return index < 0 ? vals[0] : val;
  }
  
  private static int indexOf(int val, int[] vals)
  {
    for (int i = 0; i < vals.length; i++) {
      if (vals[i] == val) {
        return i;
      }
    }
    return -1;
  }
  
  public static enum Options
  {
    private final boolean enumFloat;
    private final boolean enumBoolean;
    private final String enumString;
    private final float valueStep;
    private float valueMin;
    private float valueMax;
    private static final Options[] $VALUES = { INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO };
    private static final String __OBFID = "CL_00000653";
    private static final Options[] $VALUES$ = { INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO, FOG_FANCY, FOG_START, MIPMAP_TYPE, SMOOTH_FPS, CLOUDS, CLOUD_HEIGHT, TREES, RAIN, ANIMATED_WATER, ANIMATED_LAVA, ANIMATED_FIRE, ANIMATED_PORTAL, AO_LEVEL, LAGOMETER, SHOW_FPS, AUTOSAVE_TICKS, BETTER_GRASS, ANIMATED_REDSTONE, ANIMATED_EXPLOSION, ANIMATED_FLAME, ANIMATED_SMOKE, WEATHER, SKY, STARS, SUN_MOON, VIGNETTE, CHUNK_UPDATES, CHUNK_UPDATES_DYNAMIC, TIME, CLEAR_WATER, SMOOTH_WORLD, VOID_PARTICLES, WATER_PARTICLES, RAIN_SPLASH, PORTAL_PARTICLES, POTION_PARTICLES, FIREWORK_PARTICLES, PROFILER, DRIPPING_WATER_LAVA, BETTER_SNOW, FULLSCREEN_MODE, ANIMATED_TERRAIN, SWAMP_COLORS, RANDOM_MOBS, SMOOTH_BIOMES, CUSTOM_FONTS, CUSTOM_COLORS, SHOW_CAPES, CONNECTED_TEXTURES, CUSTOM_ITEMS, AA_LEVEL, AF_LEVEL, ANIMATED_TEXTURES, NATURAL_TEXTURES, HELD_ITEM_TOOLTIPS, DROPPED_ITEMS, LAZY_CHUNK_LOADING, CUSTOM_SKY, FAST_MATH, FAST_RENDER, TRANSLUCENT_BLOCKS, DYNAMIC_FOV, DYNAMIC_LIGHTS };
    
    public static Options getEnumOptions(int p_74379_0_)
    {
      Options[] var1 = values();
      int var2 = var1.length;
      for (int var3 = 0; var3 < var2; var3++)
      {
        Options var4 = var1[var3];
        if (var4.returnEnumOrdinal() == p_74379_0_) {
          return var4;
        }
      }
      return null;
    }
    
    private Options(String p_i46375_1_, int p_i46375_2_, String p_i1015_1_, int p_i1015_2_, String p_i1015_3_, boolean p_i1015_4_, boolean p_i1015_5_)
    {
      this(p_i46375_1_, p_i46375_2_, p_i1015_1_, p_i1015_2_, p_i1015_3_, p_i1015_4_, p_i1015_5_, 0.0F, 1.0F, 0.0F);
    }
    
    private Options(String p_i46376_1_, int p_i46376_2_, String p_i45004_1_, int p_i45004_2_, String p_i45004_3_, boolean p_i45004_4_, boolean p_i45004_5_, float p_i45004_6_, float p_i45004_7_, float p_i45004_8_)
    {
      this.enumString = p_i45004_3_;
      this.enumFloat = p_i45004_4_;
      this.enumBoolean = p_i45004_5_;
      this.valueMin = p_i45004_6_;
      this.valueMax = p_i45004_7_;
      this.valueStep = p_i45004_8_;
    }
    
    public boolean getEnumFloat()
    {
      return this.enumFloat;
    }
    
    public boolean getEnumBoolean()
    {
      return this.enumBoolean;
    }
    
    public int returnEnumOrdinal()
    {
      return ordinal();
    }
    
    public String getEnumString()
    {
      return this.enumString;
    }
    
    public float getValueMax()
    {
      return this.valueMax;
    }
    
    public void setValueMax(float p_148263_1_)
    {
      this.valueMax = p_148263_1_;
    }
    
    public float normalizeValue(float p_148266_1_)
    {
      return MathHelper.clamp_float((snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
    }
    
    public float denormalizeValue(float p_148262_1_)
    {
      return snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0F, 1.0F));
    }
    
    public float snapToStepClamp(float p_148268_1_)
    {
      p_148268_1_ = snapToStep(p_148268_1_);
      return MathHelper.clamp_float(p_148268_1_, this.valueMin, this.valueMax);
    }
    
    protected float snapToStep(float p_148264_1_)
    {
      if (this.valueStep > 0.0F) {
        p_148264_1_ = this.valueStep * Math.round(p_148264_1_ / this.valueStep);
      }
      return p_148264_1_;
    }
  }
  
  static final class SwitchOptions
  {
    static final int[] optionIds = new int[GameSettings.Options.values().length];
    private static final String __OBFID = "CL_00000652";
    
    SwitchOptions() {}
    
    static
    {
      try
      {
        optionIds[GameSettings.Options.INVERT_MOUSE.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        optionIds[GameSettings.Options.VIEW_BOBBING.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        optionIds[GameSettings.Options.ANAGLYPH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        optionIds[GameSettings.Options.FBO_ENABLE.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        optionIds[GameSettings.Options.RENDER_CLOUDS.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        optionIds[GameSettings.Options.CHAT_COLOR.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      try
      {
        optionIds[GameSettings.Options.CHAT_LINKS.ordinal()] = 7;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        optionIds[GameSettings.Options.CHAT_LINKS_PROMPT.ordinal()] = 8;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      try
      {
        optionIds[GameSettings.Options.SNOOPER_ENABLED.ordinal()] = 9;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      try
      {
        optionIds[GameSettings.Options.USE_FULLSCREEN.ordinal()] = 10;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
      try
      {
        optionIds[GameSettings.Options.ENABLE_VSYNC.ordinal()] = 11;
      }
      catch (NoSuchFieldError localNoSuchFieldError11) {}
      try
      {
        optionIds[GameSettings.Options.USE_VBO.ordinal()] = 12;
      }
      catch (NoSuchFieldError localNoSuchFieldError12) {}
      try
      {
        optionIds[GameSettings.Options.TOUCHSCREEN.ordinal()] = 13;
      }
      catch (NoSuchFieldError localNoSuchFieldError13) {}
      try
      {
        optionIds[GameSettings.Options.STREAM_SEND_METADATA.ordinal()] = 14;
      }
      catch (NoSuchFieldError localNoSuchFieldError14) {}
      try
      {
        optionIds[GameSettings.Options.FORCE_UNICODE_FONT.ordinal()] = 15;
      }
      catch (NoSuchFieldError localNoSuchFieldError15) {}
      try
      {
        optionIds[GameSettings.Options.BLOCK_ALTERNATIVES.ordinal()] = 16;
      }
      catch (NoSuchFieldError localNoSuchFieldError16) {}
      try
      {
        optionIds[GameSettings.Options.REDUCED_DEBUG_INFO.ordinal()] = 17;
      }
      catch (NoSuchFieldError localNoSuchFieldError17) {}
    }
  }
}
