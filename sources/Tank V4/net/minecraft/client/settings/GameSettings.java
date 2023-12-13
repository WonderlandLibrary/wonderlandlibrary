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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.entity.player.EntityPlayer;
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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import shadersmod.client.Shaders;

public class GameSettings {
   public boolean field_181151_V;
   public boolean ofDrippingWaterLava;
   public boolean pauseOnLostFocus;
   public static final int FAST = 1;
   public int mipmapLevels;
   public static final int SMART = 4;
   public KeyBinding keyBindUseItem;
   public boolean ofFastMath;
   public boolean fboEnable = true;
   public boolean ofSmoothBiomes;
   public float ofCloudsHeight;
   public static final int ANIM_OFF = 2;
   public boolean invertMouse;
   private static final String[] KEYS_DYNAMIC_LIGHTS = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
   public KeyBinding keyBindTogglePerspective;
   public static final String DEFAULT_STR = "Default";
   public KeyBinding keyBindPlayerList;
   public KeyBinding[] keyBindsHotbar;
   public boolean advancedItemTooltips;
   public boolean hideServerAddress;
   public boolean heldItemTooltips;
   public boolean ofSky;
   private static final Gson gson = new Gson();
   public KeyBinding keyBindCommand;
   public int renderDistanceChunks = -1;
   public int thirdPersonView;
   public boolean smoothCamera;
   public static final int ANIM_GENERATED = 1;
   public int ofAnimatedLava;
   public int guiScale;
   public int ofMipmapType;
   private static final String[] AMBIENT_OCCLUSIONS = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
   public boolean chatLinksPrompt;
   public boolean ofChunkUpdatesDynamic;
   public boolean fullScreen;
   public boolean ofProfiler;
   public KeyBinding keyBindAttack;
   public boolean ofPortalParticles;
   private static final Logger logger = LogManager.getLogger();
   public KeyBinding keyBindSprint;
   public int clouds = 2;
   public float ofFogStart;
   private final Set setModelParts;
   public int ofAaLevel;
   public static final int DEFAULT = 0;
   public KeyBinding keyBindRight;
   private static final int[] OF_DYNAMIC_LIGHTS = new int[]{3, 1, 2};
   public KeyBinding keyBindDrop;
   private static final String[] STREAM_CHAT_MODES = new String[]{"options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never"};
   public EntityPlayer.EnumChatVisibility chatVisibility;
   public boolean ofAnimatedRedstone;
   private static final ParameterizedType typeListString = new ParameterizedType() {
      private static final String __OBFID = "CL_00000651";

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
   public boolean ofAnimatedPortal;
   public static final int ANIM_ON = 0;
   public boolean ofDynamicFov;
   public KeyBinding keyBindJump;
   public boolean hideGUI;
   public KeyBinding keyBindStreamToggleMic;
   public boolean ofFastRender;
   public float ofAoLevel;
   public int streamMicToggleBehavior;
   public boolean ofCustomColors;
   public String streamPreferredServer;
   public int streamChatUserFilter;
   public boolean ofShowCapes;
   public KeyBinding keyBindChat;
   public int overrideWidth;
   public KeyBinding keyBindStreamStartStop;
   public boolean ofVoidParticles;
   public int ofAutoSaveTicks;
   public boolean touchscreen;
   public boolean ofAnimatedSmoke;
   private File optionsFileOF;
   public String ofFullscreenMode;
   public boolean forceUnicodeFont;
   public float chatOpacity;
   public boolean ofLagometer;
   public boolean ofAnimatedTextures;
   private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
   public boolean ofAnimatedFire;
   public boolean ofNaturalTextures;
   public KeyBinding keyBindFullscreen;
   public boolean ofClearWater;
   public int limitFramerate = 120;
   public int ofConnectedTextures;
   public int streamChatEnabled;
   public float fovSetting;
   public boolean ofWaterParticles;
   public int ofDroppedItems;
   public boolean useVbo;
   public float chatHeightUnfocused;
   public KeyBinding keyBindBack;
   public int ofChunkUpdates;
   public float chatScale;
   public boolean ofAnimatedFlame;
   private static final String[] STREAM_MIC_MODES = new String[]{"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk"};
   public int ofVignette;
   private static final String[] STREAM_COMPRESSIONS = new String[]{"options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high"};
   public int ofFogType;
   private static final String[] field_181149_aW = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
   public boolean ofPotionParticles;
   public float streamBytesPerPixel;
   public boolean anaglyph;
   public boolean chatColours;
   public float gammaSetting;
   public int ofAnimatedWater;
   public KeyBinding keyBindScreenshot;
   private static final String[] STREAM_CHAT_FILTER_MODES = new String[]{"options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods"};
   private Map mapSoundLevels;
   public KeyBinding keyBindLeft;
   public boolean showDebugInfo;
   public boolean ofCustomItems;
   private static final int[] OF_TREES_VALUES = new int[]{0, 1, 4, 2};
   public List field_183018_l = Lists.newArrayList();
   public KeyBinding keyBindSpectatorOutlines;
   public KeyBinding keyBindSmoothCamera;
   public boolean debugCamEnable;
   private File optionsFile;
   public boolean ofOcclusionFancy;
   public int ofTime;
   public boolean fancyGraphics = true;
   public int overrideHeight;
   public int ofAfLevel;
   public int particleSetting;
   public KeyBinding keyBindStreamCommercials;
   private static final String[] PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
   public float chatWidth;
   public boolean ofStars;
   public boolean ofAnimatedExplosion;
   public float streamFps;
   public int ofBetterGrass;
   public boolean viewBobbing = true;
   public boolean ofRandomMobs;
   public boolean ofSmoothWorld;
   public boolean ofBetterSnow;
   public static final int OFF = 3;
   public boolean ofSmoothFps;
   public float streamMicVolume;
   public boolean ofRainSplash;
   public boolean ofSunMoon;
   public List resourcePacks = Lists.newArrayList();
   public KeyBinding togglePerspective;
   public KeyBinding keyBindForward;
   public boolean field_181657_aC;
   public boolean ofShowFps;
   public float saturation;
   public boolean enableVsync;
   public boolean reducedDebugInfo;
   public boolean ofFireworkParticles;
   public int ofClouds;
   public KeyBinding keyBindPickBlock;
   public static final int FANCY = 2;
   public EnumDifficulty difficulty;
   public boolean allowBlockAlternatives;
   public boolean ofCustomFonts;
   public boolean ofSwampColors;
   public float streamKbps;
   protected Minecraft mc;
   public int ambientOcclusion = 2;
   public boolean streamSendMetadata;
   public boolean ofWeather;
   public boolean showInventoryAchievementHint;
   public float streamGameVolume;
   public boolean ofLazyChunkLoading;
   public KeyBinding keyBindSneak;
   public int ofDynamicLights;
   public String language;
   public boolean field_181150_U;
   public String lastServer;
   public int ofTrees;
   public KeyBinding ofKeyBindZoom;
   public boolean snooperEnabled;
   public float chatHeightFocused;
   public int streamCompression;
   public boolean ofAnimatedTerrain;
   public float mouseSensitivity = 0.5F;
   public int ofTranslucentBlocks;
   public KeyBinding[] keyBindings;
   private static final String __OBFID = "CL_00000650";
   public KeyBinding keyBindStreamPauseUnpause;
   public KeyBinding keyBindInventory;
   public int ofRain;
   public boolean ofCustomSky;
   public boolean chatLinks;
   public boolean showDebugProfilerChart;

   private float parseFloat(String var1) {
      return var1.equals("true") ? 1.0F : (var1.equals("false") ? 0.0F : Float.parseFloat(var1));
   }

   private static int indexOf(int var0, int[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] == var0) {
            return var2;
         }
      }

      return -1;
   }

   private void updateRenderClouds() {
      switch(this.ofClouds) {
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

   public boolean func_181148_f() {
      return this.field_181150_U;
   }

   public void setModelPartEnabled(EnumPlayerModelParts var1, boolean var2) {
      if (var2) {
         this.setModelParts.add(var1);
      } else {
         this.setModelParts.remove(var1);
      }

      this.sendSettingsToServer();
   }

   private void registerKeybinds() {
      this.keyBindings = (KeyBinding[])ArrayUtils.add(this.keyBindings, this.togglePerspective);
   }

   public float getOptionFloatValue(GameSettings.Options var1) {
      return var1 == GameSettings.Options.CLOUD_HEIGHT ? this.ofCloudsHeight : (var1 == GameSettings.Options.AO_LEVEL ? this.ofAoLevel : (var1 == GameSettings.Options.AA_LEVEL ? (float)this.ofAaLevel : (var1 == GameSettings.Options.AF_LEVEL ? (float)this.ofAfLevel : (var1 == GameSettings.Options.MIPMAP_TYPE ? (float)this.ofMipmapType : (var1 == GameSettings.Options.FRAMERATE_LIMIT ? ((float)this.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync ? 0.0F : (float)this.limitFramerate) : (var1 == GameSettings.Options.FOV ? this.fovSetting : (var1 == GameSettings.Options.GAMMA ? this.gammaSetting : (var1 == GameSettings.Options.SATURATION ? this.saturation : (var1 == GameSettings.Options.SENSITIVITY ? this.mouseSensitivity : (var1 == GameSettings.Options.CHAT_OPACITY ? this.chatOpacity : (var1 == GameSettings.Options.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : (var1 == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : (var1 == GameSettings.Options.CHAT_SCALE ? this.chatScale : (var1 == GameSettings.Options.CHAT_WIDTH ? this.chatWidth : (var1 == GameSettings.Options.FRAMERATE_LIMIT ? (float)this.limitFramerate : (var1 == GameSettings.Options.MIPMAP_LEVELS ? (float)this.mipmapLevels : (var1 == GameSettings.Options.RENDER_DISTANCE ? (float)this.renderDistanceChunks : (var1 == GameSettings.Options.STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : (var1 == GameSettings.Options.STREAM_VOLUME_MIC ? this.streamMicVolume : (var1 == GameSettings.Options.STREAM_VOLUME_SYSTEM ? this.streamGameVolume : (var1 == GameSettings.Options.STREAM_KBPS ? this.streamKbps : (var1 == GameSettings.Options.STREAM_FPS ? this.streamFps : 0.0F))))))))))))))))))))));
   }

   private static int nextValue(int var0, int[] var1) {
      int var2 = indexOf(var0, var1);
      if (var2 < 0) {
         return var1[0];
      } else {
         ++var2;
         if (var2 >= var1.length) {
            var2 = 0;
         }

         return var1[var2];
      }
   }

   public float getSoundLevel(SoundCategory var1) {
      return this.mapSoundLevels.containsKey(var1) ? (Float)this.mapSoundLevels.get(var1) : 1.0F;
   }

   public void loadOptions() {
      try {
         if (!this.optionsFile.exists()) {
            return;
         }

         BufferedReader var1 = new BufferedReader(new FileReader(this.optionsFile));
         String var2 = "";
         this.mapSoundLevels.clear();

         while((var2 = var1.readLine()) != null) {
            try {
               String[] var3 = var2.split(":");
               if (var3[0].equals("mouseSensitivity")) {
                  this.mouseSensitivity = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("fov")) {
                  this.fovSetting = this.parseFloat(var3[1]) * 40.0F + 70.0F;
               }

               if (var3[0].equals("gamma")) {
                  this.gammaSetting = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("saturation")) {
                  this.saturation = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("invertYMouse")) {
                  this.invertMouse = var3[1].equals("true");
               }

               if (var3[0].equals("renderDistance")) {
                  this.renderDistanceChunks = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("guiScale")) {
                  this.guiScale = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("particles")) {
                  this.particleSetting = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("bobView")) {
                  this.viewBobbing = var3[1].equals("true");
               }

               if (var3[0].equals("anaglyph3d")) {
                  this.anaglyph = var3[1].equals("true");
               }

               if (var3[0].equals("maxFps")) {
                  this.limitFramerate = Integer.parseInt(var3[1]);
                  this.enableVsync = false;
                  if (this.limitFramerate <= 0) {
                     this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
                     this.enableVsync = true;
                  }

                  this.updateVSync();
               }

               if (var3[0].equals("fboEnable")) {
                  this.fboEnable = var3[1].equals("true");
               }

               if (var3[0].equals("difficulty")) {
                  this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(var3[1]));
               }

               if (var3[0].equals("fancyGraphics")) {
                  this.fancyGraphics = var3[1].equals("true");
                  this.updateRenderClouds();
               }

               if (var3[0].equals("ao")) {
                  if (var3[1].equals("true")) {
                     this.ambientOcclusion = 2;
                  } else if (var3[1].equals("false")) {
                     this.ambientOcclusion = 0;
                  } else {
                     this.ambientOcclusion = Integer.parseInt(var3[1]);
                  }
               }

               if (var3[0].equals("renderClouds")) {
                  if (var3[1].equals("true")) {
                     this.clouds = 2;
                  } else if (var3[1].equals("false")) {
                     this.clouds = 0;
                  } else if (var3[1].equals("fast")) {
                     this.clouds = 1;
                  }
               }

               if (var3[0].equals("resourcePacks")) {
                  this.resourcePacks = (List)gson.fromJson((String)var2.substring(var2.indexOf(58) + 1), (Type)typeListString);
                  if (this.resourcePacks == null) {
                     this.resourcePacks = Lists.newArrayList();
                  }
               }

               if (var3[0].equals("incompatibleResourcePacks")) {
                  this.field_183018_l = (List)gson.fromJson((String)var2.substring(var2.indexOf(58) + 1), (Type)typeListString);
                  if (this.field_183018_l == null) {
                     this.field_183018_l = Lists.newArrayList();
                  }
               }

               if (var3[0].equals("lastServer") && var3.length >= 2) {
                  this.lastServer = var2.substring(var2.indexOf(58) + 1);
               }

               if (var3[0].equals("lang") && var3.length >= 2) {
                  this.language = var3[1];
               }

               if (var3[0].equals("chatVisibility")) {
                  this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(var3[1]));
               }

               if (var3[0].equals("chatColors")) {
                  this.chatColours = var3[1].equals("true");
               }

               if (var3[0].equals("chatLinks")) {
                  this.chatLinks = var3[1].equals("true");
               }

               if (var3[0].equals("chatLinksPrompt")) {
                  this.chatLinksPrompt = var3[1].equals("true");
               }

               if (var3[0].equals("chatOpacity")) {
                  this.chatOpacity = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("snooperEnabled")) {
                  this.snooperEnabled = var3[1].equals("true");
               }

               if (var3[0].equals("fullscreen")) {
                  this.fullScreen = var3[1].equals("true");
               }

               if (var3[0].equals("enableVsync")) {
                  this.enableVsync = var3[1].equals("true");
                  this.updateVSync();
               }

               if (var3[0].equals("useVbo")) {
                  this.useVbo = var3[1].equals("true");
               }

               if (var3[0].equals("hideServerAddress")) {
                  this.hideServerAddress = var3[1].equals("true");
               }

               if (var3[0].equals("advancedItemTooltips")) {
                  this.advancedItemTooltips = var3[1].equals("true");
               }

               if (var3[0].equals("pauseOnLostFocus")) {
                  this.pauseOnLostFocus = var3[1].equals("true");
               }

               if (var3[0].equals("touchscreen")) {
                  this.touchscreen = var3[1].equals("true");
               }

               if (var3[0].equals("overrideHeight")) {
                  this.overrideHeight = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("overrideWidth")) {
                  this.overrideWidth = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("heldItemTooltips")) {
                  this.heldItemTooltips = var3[1].equals("true");
               }

               if (var3[0].equals("chatHeightFocused")) {
                  this.chatHeightFocused = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("chatHeightUnfocused")) {
                  this.chatHeightUnfocused = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("chatScale")) {
                  this.chatScale = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("chatWidth")) {
                  this.chatWidth = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("showInventoryAchievementHint")) {
                  this.showInventoryAchievementHint = var3[1].equals("true");
               }

               if (var3[0].equals("mipmapLevels")) {
                  this.mipmapLevels = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("streamBytesPerPixel")) {
                  this.streamBytesPerPixel = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("streamMicVolume")) {
                  this.streamMicVolume = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("streamSystemVolume")) {
                  this.streamGameVolume = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("streamKbps")) {
                  this.streamKbps = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("streamFps")) {
                  this.streamFps = this.parseFloat(var3[1]);
               }

               if (var3[0].equals("streamCompression")) {
                  this.streamCompression = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("streamSendMetadata")) {
                  this.streamSendMetadata = var3[1].equals("true");
               }

               if (var3[0].equals("streamPreferredServer") && var3.length >= 2) {
                  this.streamPreferredServer = var2.substring(var2.indexOf(58) + 1);
               }

               if (var3[0].equals("streamChatEnabled")) {
                  this.streamChatEnabled = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("streamChatUserFilter")) {
                  this.streamChatUserFilter = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("streamMicToggleBehavior")) {
                  this.streamMicToggleBehavior = Integer.parseInt(var3[1]);
               }

               if (var3[0].equals("forceUnicodeFont")) {
                  this.forceUnicodeFont = var3[1].equals("true");
               }

               if (var3[0].equals("allowBlockAlternatives")) {
                  this.allowBlockAlternatives = var3[1].equals("true");
               }

               if (var3[0].equals("reducedDebugInfo")) {
                  this.reducedDebugInfo = var3[1].equals("true");
               }

               if (var3[0].equals("useNativeTransport")) {
                  this.field_181150_U = var3[1].equals("true");
               }

               if (var3[0].equals("entityShadows")) {
                  this.field_181151_V = var3[1].equals("true");
               }

               KeyBinding[] var7;
               int var6 = (var7 = this.keyBindings).length;

               int var5;
               for(var5 = 0; var5 < var6; ++var5) {
                  KeyBinding var4 = var7[var5];
                  if (var3[0].equals("key_" + var4.getKeyDescription())) {
                     var4.setKeyCode(Integer.parseInt(var3[1]));
                  }
               }

               SoundCategory[] var12;
               var6 = (var12 = SoundCategory.values()).length;

               for(var5 = 0; var5 < var6; ++var5) {
                  SoundCategory var10 = var12[var5];
                  if (var3[0].equals("soundCategory_" + var10.getCategoryName())) {
                     this.mapSoundLevels.put(var10, this.parseFloat(var3[1]));
                  }
               }

               EnumPlayerModelParts[] var13;
               var6 = (var13 = EnumPlayerModelParts.values()).length;

               for(var5 = 0; var5 < var6; ++var5) {
                  EnumPlayerModelParts var11 = var13[var5];
                  if (var3[0].equals("modelPart_" + var11.getPartName())) {
                     this.setModelPartEnabled(var11, var3[1].equals("true"));
                  }
               }
            } catch (Exception var8) {
               logger.warn("Skipping bad option: " + var2);
               var8.printStackTrace();
            }
         }

         KeyBinding.resetKeyBindingArrayAndHash();
         var1.close();
      } catch (Exception var9) {
         logger.error((String)"Failed to load options", (Throwable)var9);
      }

      this.loadOfOptions();
   }

   private void setOptionFloatValueOF(GameSettings.Options var1, float var2) {
      if (var1 == GameSettings.Options.CLOUD_HEIGHT) {
         this.ofCloudsHeight = var2;
         this.mc.renderGlobal.resetClouds();
      }

      if (var1 == GameSettings.Options.AO_LEVEL) {
         this.ofAoLevel = var2;
         this.mc.renderGlobal.loadRenderers();
      }

      int var3;
      if (var1 == GameSettings.Options.AA_LEVEL) {
         var3 = (int)var2;
         if (var3 > 0 && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
            return;
         }

         int[] var4 = new int[]{0, 2, 4, 6, 8, 12, 16};
         this.ofAaLevel = 0;

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var3 >= var4[var5]) {
               this.ofAaLevel = var4[var5];
            }
         }

         this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
      }

      if (var1 == GameSettings.Options.AF_LEVEL) {
         var3 = (int)var2;
         if (var3 > 1 && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
            return;
         }

         for(this.ofAfLevel = 1; this.ofAfLevel * 2 <= var3; this.ofAfLevel *= 2) {
         }

         this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
         this.mc.refreshResources();
      }

      if (var1 == GameSettings.Options.MIPMAP_TYPE) {
         var3 = (int)var2;
         this.ofMipmapType = Config.limit(var3, 0, 3);
         this.mc.refreshResources();
      }

   }

   public void setOptionKeyBinding(KeyBinding var1, int var2) {
      var1.setKeyCode(var2);
      this.saveOptions();
   }

   public void setOptionFloatValue(GameSettings.Options var1, float var2) {
      this.setOptionFloatValueOF(var1, var2);
      if (var1 == GameSettings.Options.SENSITIVITY) {
         this.mouseSensitivity = var2;
      }

      if (var1 == GameSettings.Options.FOV) {
         this.fovSetting = var2;
      }

      if (var1 == GameSettings.Options.GAMMA) {
         this.gammaSetting = var2;
      }

      if (var1 == GameSettings.Options.FRAMERATE_LIMIT) {
         this.limitFramerate = (int)var2;
         this.enableVsync = false;
         if (this.limitFramerate <= 0) {
            this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
            this.enableVsync = true;
         }

         this.updateVSync();
      }

      if (var1 == GameSettings.Options.CHAT_OPACITY) {
         this.chatOpacity = var2;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (var1 == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
         this.chatHeightFocused = var2;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (var1 == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
         this.chatHeightUnfocused = var2;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (var1 == GameSettings.Options.CHAT_WIDTH) {
         this.chatWidth = var2;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (var1 == GameSettings.Options.CHAT_SCALE) {
         this.chatScale = var2;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (var1 == GameSettings.Options.MIPMAP_LEVELS) {
         int var3 = this.mipmapLevels;
         this.mipmapLevels = (int)var2;
         if ((float)var3 != var2) {
            this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
            this.mc.scheduleResourcesRefresh();
         }
      }

      if (var1 == GameSettings.Options.BLOCK_ALTERNATIVES) {
         this.allowBlockAlternatives = !this.allowBlockAlternatives;
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.RENDER_DISTANCE) {
         this.renderDistanceChunks = (int)var2;
         this.mc.renderGlobal.setDisplayListEntitiesDirty();
      }

      if (var1 == GameSettings.Options.STREAM_BYTES_PER_PIXEL) {
         this.streamBytesPerPixel = var2;
      }

      if (var1 == GameSettings.Options.STREAM_VOLUME_MIC) {
         this.streamMicVolume = var2;
         this.mc.getTwitchStream().updateStreamVolume();
      }

      if (var1 == GameSettings.Options.STREAM_VOLUME_SYSTEM) {
         this.streamGameVolume = var2;
         this.mc.getTwitchStream().updateStreamVolume();
      }

      if (var1 == GameSettings.Options.STREAM_KBPS) {
         this.streamKbps = var2;
      }

      if (var1 == GameSettings.Options.STREAM_FPS) {
         this.streamFps = var2;
      }

   }

   public int func_181147_e() {
      return this.renderDistanceChunks >= 4 ? this.clouds : 0;
   }

   public void resetSettings() {
      this.renderDistanceChunks = 8;
      this.viewBobbing = true;
      this.anaglyph = false;
      this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
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
      this.updateWaterOpacity();
      this.mc.refreshResources();
      this.saveOptions();
   }

   public Set getModelParts() {
      return ImmutableSet.copyOf((Collection)this.setModelParts);
   }

   public boolean getOptionOrdinalValue(GameSettings.Options var1) {
      switch(var1) {
      case INVERT_MOUSE:
         return this.invertMouse;
      case VIEW_BOBBING:
         return this.viewBobbing;
      case ANAGLYPH:
         return this.anaglyph;
      case FBO_ENABLE:
         return this.fboEnable;
      case CHAT_COLOR:
         return this.chatColours;
      case CHAT_LINKS:
         return this.chatLinks;
      case CHAT_LINKS_PROMPT:
         return this.chatLinksPrompt;
      case SNOOPER_ENABLED:
         return this.snooperEnabled;
      case USE_FULLSCREEN:
         return this.fullScreen;
      case ENABLE_VSYNC:
         return this.enableVsync;
      case USE_VBO:
         return this.useVbo;
      case TOUCHSCREEN:
         return this.touchscreen;
      case STREAM_SEND_METADATA:
         return this.streamSendMetadata;
      case FORCE_UNICODE_FONT:
         return this.forceUnicodeFont;
      case BLOCK_ALTERNATIVES:
         return this.allowBlockAlternatives;
      case REDUCED_DEBUG_INFO:
         return this.reducedDebugInfo;
      case ENTITY_SHADOWS:
         return this.field_181151_V;
      default:
         return false;
      }
   }

   private void updateWaterOpacity() {
      if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
         Config.waterOpacityChanged = true;
      }

      ClearWater.updateWaterOpacity(this, Minecraft.theWorld);
   }

   public static boolean isKeyDown(KeyBinding var0) {
      int var1 = var0.getKeyCode();
      return var1 >= -100 && var1 <= 255 ? (var0.getKeyCode() == 0 ? false : (var0.getKeyCode() < 0 ? Mouse.isButtonDown(var0.getKeyCode() + 100) : Keyboard.isKeyDown(var0.getKeyCode()))) : false;
   }

   public void setAllAnimations(boolean var1) {
      int var2 = var1 ? 0 : 2;
      this.ofAnimatedWater = var2;
      this.ofAnimatedLava = var2;
      this.ofAnimatedFire = var1;
      this.ofAnimatedPortal = var1;
      this.ofAnimatedRedstone = var1;
      this.ofAnimatedExplosion = var1;
      this.ofAnimatedFlame = var1;
      this.ofAnimatedSmoke = var1;
      this.ofVoidParticles = var1;
      this.ofWaterParticles = var1;
      this.ofRainSplash = var1;
      this.ofPortalParticles = var1;
      this.ofPotionParticles = var1;
      this.ofFireworkParticles = var1;
      this.particleSetting = var1 ? 0 : 2;
      this.ofDrippingWaterLava = var1;
      this.ofAnimatedTerrain = var1;
      this.ofAnimatedTextures = var1;
   }

   public void sendSettingsToServer() {
      if (Minecraft.thePlayer != null) {
         int var1 = 0;

         Object var2;
         for(Iterator var3 = this.setModelParts.iterator(); var3.hasNext(); var1 |= ((EnumPlayerModelParts)var2).getPartMask()) {
            var2 = var3.next();
         }

         Minecraft.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, var1));
      }

   }

   public void saveOfOptions() {
      try {
         PrintWriter var1 = new PrintWriter(new FileWriter(this.optionsFileOF));
         var1.println("ofRenderDistanceChunks:" + this.renderDistanceChunks);
         var1.println("ofFogType:" + this.ofFogType);
         var1.println("ofFogStart:" + this.ofFogStart);
         var1.println("ofMipmapType:" + this.ofMipmapType);
         var1.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
         var1.println("ofSmoothFps:" + this.ofSmoothFps);
         var1.println("ofSmoothWorld:" + this.ofSmoothWorld);
         var1.println("ofAoLevel:" + this.ofAoLevel);
         var1.println("ofClouds:" + this.ofClouds);
         var1.println("ofCloudsHeight:" + this.ofCloudsHeight);
         var1.println("ofTrees:" + this.ofTrees);
         var1.println("ofDroppedItems:" + this.ofDroppedItems);
         var1.println("ofRain:" + this.ofRain);
         var1.println("ofAnimatedWater:" + this.ofAnimatedWater);
         var1.println("ofAnimatedLava:" + this.ofAnimatedLava);
         var1.println("ofAnimatedFire:" + this.ofAnimatedFire);
         var1.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
         var1.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
         var1.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
         var1.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
         var1.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
         var1.println("ofVoidParticles:" + this.ofVoidParticles);
         var1.println("ofWaterParticles:" + this.ofWaterParticles);
         var1.println("ofPortalParticles:" + this.ofPortalParticles);
         var1.println("ofPotionParticles:" + this.ofPotionParticles);
         var1.println("ofFireworkParticles:" + this.ofFireworkParticles);
         var1.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
         var1.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
         var1.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
         var1.println("ofRainSplash:" + this.ofRainSplash);
         var1.println("ofLagometer:" + this.ofLagometer);
         var1.println("ofShowFps:" + this.ofShowFps);
         var1.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
         var1.println("ofBetterGrass:" + this.ofBetterGrass);
         var1.println("ofConnectedTextures:" + this.ofConnectedTextures);
         var1.println("ofWeather:" + this.ofWeather);
         var1.println("ofSky:" + this.ofSky);
         var1.println("ofStars:" + this.ofStars);
         var1.println("ofSunMoon:" + this.ofSunMoon);
         var1.println("ofVignette:" + this.ofVignette);
         var1.println("ofChunkUpdates:" + this.ofChunkUpdates);
         var1.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
         var1.println("ofTime:" + this.ofTime);
         var1.println("ofClearWater:" + this.ofClearWater);
         var1.println("ofAaLevel:" + this.ofAaLevel);
         var1.println("ofAfLevel:" + this.ofAfLevel);
         var1.println("ofProfiler:" + this.ofProfiler);
         var1.println("ofBetterSnow:" + this.ofBetterSnow);
         var1.println("ofSwampColors:" + this.ofSwampColors);
         var1.println("ofRandomMobs:" + this.ofRandomMobs);
         var1.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
         var1.println("ofCustomFonts:" + this.ofCustomFonts);
         var1.println("ofCustomColors:" + this.ofCustomColors);
         var1.println("ofCustomItems:" + this.ofCustomItems);
         var1.println("ofCustomSky:" + this.ofCustomSky);
         var1.println("ofShowCapes:" + this.ofShowCapes);
         var1.println("ofNaturalTextures:" + this.ofNaturalTextures);
         var1.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
         var1.println("ofDynamicFov:" + this.ofDynamicFov);
         var1.println("ofDynamicLights:" + this.ofDynamicLights);
         var1.println("ofFullscreenMode:" + this.ofFullscreenMode);
         var1.println("ofFastMath:" + this.ofFastMath);
         var1.println("ofFastRender:" + this.ofFastRender);
         var1.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
         var1.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
         var1.close();
      } catch (Exception var2) {
         Config.warn("Failed to save options");
         var2.printStackTrace();
      }

   }

   private static String getTranslation(String[] var0, int var1) {
      if (var1 < 0 || var1 >= var0.length) {
         var1 = 0;
      }

      return I18n.format(var0[var1]);
   }

   public void loadOfOptions() {
      try {
         File var1 = this.optionsFileOF;
         if (!var1.exists()) {
            var1 = this.optionsFile;
         }

         if (!var1.exists()) {
            return;
         }

         BufferedReader var2 = new BufferedReader(new FileReader(var1));
         String var3 = "";

         while((var3 = var2.readLine()) != null) {
            try {
               String[] var4 = var3.split(":");
               if (var4[0].equals("ofRenderDistanceChunks") && var4.length >= 2) {
                  this.renderDistanceChunks = Integer.valueOf(var4[1]);
                  this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 32);
               }

               if (var4[0].equals("ofFogType") && var4.length >= 2) {
                  this.ofFogType = Integer.valueOf(var4[1]);
                  this.ofFogType = Config.limit(this.ofFogType, 1, 3);
               }

               if (var4[0].equals("ofFogStart") && var4.length >= 2) {
                  this.ofFogStart = Float.valueOf(var4[1]);
                  if (this.ofFogStart < 0.2F) {
                     this.ofFogStart = 0.2F;
                  }

                  if (this.ofFogStart > 0.81F) {
                     this.ofFogStart = 0.8F;
                  }
               }

               if (var4[0].equals("ofMipmapType") && var4.length >= 2) {
                  this.ofMipmapType = Integer.valueOf(var4[1]);
                  this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
               }

               if (var4[0].equals("ofOcclusionFancy") && var4.length >= 2) {
                  this.ofOcclusionFancy = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofSmoothFps") && var4.length >= 2) {
                  this.ofSmoothFps = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofSmoothWorld") && var4.length >= 2) {
                  this.ofSmoothWorld = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAoLevel") && var4.length >= 2) {
                  this.ofAoLevel = Float.valueOf(var4[1]);
                  this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
               }

               if (var4[0].equals("ofClouds") && var4.length >= 2) {
                  this.ofClouds = Integer.valueOf(var4[1]);
                  this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                  this.updateRenderClouds();
               }

               if (var4[0].equals("ofCloudsHeight") && var4.length >= 2) {
                  this.ofCloudsHeight = Float.valueOf(var4[1]);
                  this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
               }

               if (var4[0].equals("ofTrees") && var4.length >= 2) {
                  this.ofTrees = Integer.valueOf(var4[1]);
                  this.ofTrees = limit(this.ofTrees, OF_TREES_VALUES);
               }

               if (var4[0].equals("ofDroppedItems") && var4.length >= 2) {
                  this.ofDroppedItems = Integer.valueOf(var4[1]);
                  this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
               }

               if (var4[0].equals("ofRain") && var4.length >= 2) {
                  this.ofRain = Integer.valueOf(var4[1]);
                  this.ofRain = Config.limit(this.ofRain, 0, 3);
               }

               if (var4[0].equals("ofAnimatedWater") && var4.length >= 2) {
                  this.ofAnimatedWater = Integer.valueOf(var4[1]);
                  this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
               }

               if (var4[0].equals("ofAnimatedLava") && var4.length >= 2) {
                  this.ofAnimatedLava = Integer.valueOf(var4[1]);
                  this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
               }

               if (var4[0].equals("ofAnimatedFire") && var4.length >= 2) {
                  this.ofAnimatedFire = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAnimatedPortal") && var4.length >= 2) {
                  this.ofAnimatedPortal = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAnimatedRedstone") && var4.length >= 2) {
                  this.ofAnimatedRedstone = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAnimatedExplosion") && var4.length >= 2) {
                  this.ofAnimatedExplosion = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAnimatedFlame") && var4.length >= 2) {
                  this.ofAnimatedFlame = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAnimatedSmoke") && var4.length >= 2) {
                  this.ofAnimatedSmoke = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofVoidParticles") && var4.length >= 2) {
                  this.ofVoidParticles = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofWaterParticles") && var4.length >= 2) {
                  this.ofWaterParticles = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofPortalParticles") && var4.length >= 2) {
                  this.ofPortalParticles = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofPotionParticles") && var4.length >= 2) {
                  this.ofPotionParticles = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofFireworkParticles") && var4.length >= 2) {
                  this.ofFireworkParticles = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofDrippingWaterLava") && var4.length >= 2) {
                  this.ofDrippingWaterLava = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAnimatedTerrain") && var4.length >= 2) {
                  this.ofAnimatedTerrain = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAnimatedTextures") && var4.length >= 2) {
                  this.ofAnimatedTextures = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofRainSplash") && var4.length >= 2) {
                  this.ofRainSplash = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofLagometer") && var4.length >= 2) {
                  this.ofLagometer = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofShowFps") && var4.length >= 2) {
                  this.ofShowFps = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofAutoSaveTicks") && var4.length >= 2) {
                  this.ofAutoSaveTicks = Integer.valueOf(var4[1]);
                  this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
               }

               if (var4[0].equals("ofBetterGrass") && var4.length >= 2) {
                  this.ofBetterGrass = Integer.valueOf(var4[1]);
                  this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
               }

               if (var4[0].equals("ofConnectedTextures") && var4.length >= 2) {
                  this.ofConnectedTextures = Integer.valueOf(var4[1]);
                  this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
               }

               if (var4[0].equals("ofWeather") && var4.length >= 2) {
                  this.ofWeather = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofSky") && var4.length >= 2) {
                  this.ofSky = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofStars") && var4.length >= 2) {
                  this.ofStars = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofSunMoon") && var4.length >= 2) {
                  this.ofSunMoon = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofVignette") && var4.length >= 2) {
                  this.ofVignette = Integer.valueOf(var4[1]);
                  this.ofVignette = Config.limit(this.ofVignette, 0, 2);
               }

               if (var4[0].equals("ofChunkUpdates") && var4.length >= 2) {
                  this.ofChunkUpdates = Integer.valueOf(var4[1]);
                  this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
               }

               if (var4[0].equals("ofChunkUpdatesDynamic") && var4.length >= 2) {
                  this.ofChunkUpdatesDynamic = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofTime") && var4.length >= 2) {
                  this.ofTime = Integer.valueOf(var4[1]);
                  this.ofTime = Config.limit(this.ofTime, 0, 2);
               }

               if (var4[0].equals("ofClearWater") && var4.length >= 2) {
                  this.ofClearWater = Boolean.valueOf(var4[1]);
                  this.updateWaterOpacity();
               }

               if (var4[0].equals("ofAaLevel") && var4.length >= 2) {
                  this.ofAaLevel = Integer.valueOf(var4[1]);
                  this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
               }

               if (var4[0].equals("ofAfLevel") && var4.length >= 2) {
                  this.ofAfLevel = Integer.valueOf(var4[1]);
                  this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
               }

               if (var4[0].equals("ofProfiler") && var4.length >= 2) {
                  this.ofProfiler = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofBetterSnow") && var4.length >= 2) {
                  this.ofBetterSnow = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofSwampColors") && var4.length >= 2) {
                  this.ofSwampColors = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofRandomMobs") && var4.length >= 2) {
                  this.ofRandomMobs = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofSmoothBiomes") && var4.length >= 2) {
                  this.ofSmoothBiomes = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofCustomFonts") && var4.length >= 2) {
                  this.ofCustomFonts = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofCustomColors") && var4.length >= 2) {
                  this.ofCustomColors = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofCustomItems") && var4.length >= 2) {
                  this.ofCustomItems = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofCustomSky") && var4.length >= 2) {
                  this.ofCustomSky = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofShowCapes") && var4.length >= 2) {
                  this.ofShowCapes = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofNaturalTextures") && var4.length >= 2) {
                  this.ofNaturalTextures = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofLazyChunkLoading") && var4.length >= 2) {
                  this.ofLazyChunkLoading = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofDynamicFov") && var4.length >= 2) {
                  this.ofDynamicFov = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofDynamicLights") && var4.length >= 2) {
                  this.ofDynamicLights = Integer.valueOf(var4[1]);
                  this.ofDynamicLights = limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
               }

               if (var4[0].equals("ofFullscreenMode") && var4.length >= 2) {
                  this.ofFullscreenMode = var4[1];
               }

               if (var4[0].equals("ofFastMath") && var4.length >= 2) {
                  this.ofFastMath = Boolean.valueOf(var4[1]);
                  MathHelper.fastMath = this.ofFastMath;
               }

               if (var4[0].equals("ofFastRender") && var4.length >= 2) {
                  this.ofFastRender = Boolean.valueOf(var4[1]);
               }

               if (var4[0].equals("ofTranslucentBlocks") && var4.length >= 2) {
                  this.ofTranslucentBlocks = Integer.valueOf(var4[1]);
                  this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
               }

               if (var4[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) {
                  this.ofKeyBindZoom.setKeyCode(Integer.parseInt(var4[1]));
               }
            } catch (Exception var5) {
               Config.dbg("Skipping bad option: " + var3);
               var5.printStackTrace();
            }
         }

         KeyBinding.resetKeyBindingArrayAndHash();
         var2.close();
      } catch (Exception var6) {
         Config.warn("Failed to load options");
         var6.printStackTrace();
      }

   }

   public void saveOptions() {
      if (Reflector.FMLClientHandler.exists()) {
         Object var1 = Reflector.call(Reflector.FMLClientHandler_instance);
         if (var1 != null && Reflector.callBoolean(var1, Reflector.FMLClientHandler_isLoading)) {
            return;
         }
      }

      try {
         PrintWriter var7 = new PrintWriter(new FileWriter(this.optionsFile));
         var7.println("invertYMouse:" + this.invertMouse);
         var7.println("mouseSensitivity:" + this.mouseSensitivity);
         var7.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
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
         switch(this.clouds) {
         case 0:
            var7.println("renderClouds:false");
            break;
         case 1:
            var7.println("renderClouds:fast");
            break;
         case 2:
            var7.println("renderClouds:true");
         }

         var7.println("resourcePacks:" + gson.toJson((Object)this.resourcePacks));
         var7.println("incompatibleResourcePacks:" + gson.toJson((Object)this.field_183018_l));
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
         var7.println("useVbo:" + this.useVbo);
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
         var7.println("allowBlockAlternatives:" + this.allowBlockAlternatives);
         var7.println("reducedDebugInfo:" + this.reducedDebugInfo);
         var7.println("useNativeTransport:" + this.field_181150_U);
         var7.println("entityShadows:" + this.field_181151_V);
         KeyBinding[] var5;
         int var4 = (var5 = this.keyBindings).length;

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            KeyBinding var2 = var5[var3];
            var7.println("key_" + var2.getKeyDescription() + ":" + var2.getKeyCode());
         }

         SoundCategory[] var10;
         var4 = (var10 = SoundCategory.values()).length;

         for(var3 = 0; var3 < var4; ++var3) {
            SoundCategory var8 = var10[var3];
            var7.println("soundCategory_" + var8.getCategoryName() + ":" + this.getSoundLevel(var8));
         }

         EnumPlayerModelParts[] var11;
         var4 = (var11 = EnumPlayerModelParts.values()).length;

         for(var3 = 0; var3 < var4; ++var3) {
            EnumPlayerModelParts var9 = var11[var3];
            var7.println("modelPart_" + var9.getPartName() + ":" + this.setModelParts.contains(var9));
         }

         var7.close();
      } catch (Exception var6) {
         logger.error((String)"Failed to save options", (Throwable)var6);
      }

      this.saveOfOptions();
      this.sendSettingsToServer();
   }

   public void switchModelPartEnabled(EnumPlayerModelParts var1) {
      if (!this.getModelParts().contains(var1)) {
         this.setModelParts.add(var1);
      } else {
         this.setModelParts.remove(var1);
      }

      this.sendSettingsToServer();
   }

   public static String getKeyDisplayString(int var0) {
      return var0 < 0 ? I18n.format("key.mouseButton", var0 + 101) : (var0 < 256 ? Keyboard.getKeyName(var0) : String.format("%c", (char)(var0 - 256)).toUpperCase());
   }

   public void setSoundLevel(SoundCategory var1, float var2) {
      this.mc.getSoundHandler().setSoundLevel(var1, var2);
      this.mapSoundLevels.put(var1, var2);
   }

   private static int limit(int var0, int[] var1) {
      int var2 = indexOf(var0, var1);
      return var2 < 0 ? var1[0] : var0;
   }

   public void updateVSync() {
      Display.setVSyncEnabled(this.enableVsync);
   }

   public String getKeyBinding(GameSettings.Options var1) {
      String var2 = this.getKeyBindingOF(var1);
      if (var2 != null) {
         return var2;
      } else {
         String var3 = I18n.format(var1.getEnumString()) + ": ";
         if (var1.getEnumFloat()) {
            float var7 = this.getOptionFloatValue(var1);
            float var5 = var1.normalizeValue(var7);
            return var1 == GameSettings.Options.SENSITIVITY ? (var5 == 0.0F ? var3 + I18n.format("options.sensitivity.min") : (var5 == 1.0F ? var3 + I18n.format("options.sensitivity.max") : var3 + (int)(var5 * 200.0F) + "%")) : (var1 == GameSettings.Options.FOV ? (var7 == 70.0F ? var3 + I18n.format("options.fov.min") : (var7 == 110.0F ? var3 + I18n.format("options.fov.max") : var3 + (int)var7)) : (var1 == GameSettings.Options.FRAMERATE_LIMIT ? (var7 == GameSettings.Options.access$2(var1) ? var3 + I18n.format("options.framerateLimit.max") : var3 + (int)var7 + " fps") : (var1 == GameSettings.Options.RENDER_CLOUDS ? (var7 == GameSettings.Options.access$3(var1) ? var3 + I18n.format("options.cloudHeight.min") : var3 + ((int)var7 + 128)) : (var1 == GameSettings.Options.GAMMA ? (var5 == 0.0F ? var3 + I18n.format("options.gamma.min") : (var5 == 1.0F ? var3 + I18n.format("options.gamma.max") : var3 + "+" + (int)(var5 * 100.0F) + "%")) : (var1 == GameSettings.Options.SATURATION ? var3 + (int)(var5 * 400.0F) + "%" : (var1 == GameSettings.Options.CHAT_OPACITY ? var3 + (int)(var5 * 90.0F + 10.0F) + "%" : (var1 == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED ? var3 + GuiNewChat.calculateChatboxHeight(var5) + "px" : (var1 == GameSettings.Options.CHAT_HEIGHT_FOCUSED ? var3 + GuiNewChat.calculateChatboxHeight(var5) + "px" : (var1 == GameSettings.Options.CHAT_WIDTH ? var3 + GuiNewChat.calculateChatboxWidth(var5) + "px" : (var1 == GameSettings.Options.RENDER_DISTANCE ? var3 + (int)var7 + " chunks" : (var1 == GameSettings.Options.MIPMAP_LEVELS ? (var7 == 0.0F ? var3 + I18n.format("options.off") : var3 + (int)var7) : (var1 == GameSettings.Options.STREAM_FPS ? var3 + TwitchStream.formatStreamFps(var5) + " fps" : (var1 == GameSettings.Options.STREAM_KBPS ? var3 + TwitchStream.formatStreamKbps(var5) + " Kbps" : (var1 == GameSettings.Options.STREAM_BYTES_PER_PIXEL ? var3 + String.format("%.3f bpp", TwitchStream.formatStreamBps(var5)) : (var5 == 0.0F ? var3 + I18n.format("options.off") : var3 + (int)(var5 * 100.0F) + "%")))))))))))))));
         } else if (var1.getEnumBoolean()) {
            boolean var6 = this.getOptionOrdinalValue(var1);
            return var6 ? var3 + I18n.format("options.on") : var3 + I18n.format("options.off");
         } else if (var1 == GameSettings.Options.GUI_SCALE) {
            return var3 + getTranslation(GUISCALES, this.guiScale);
         } else if (var1 == GameSettings.Options.CHAT_VISIBILITY) {
            return var3 + I18n.format(this.chatVisibility.getResourceKey());
         } else if (var1 == GameSettings.Options.PARTICLES) {
            return var3 + getTranslation(PARTICLES, this.particleSetting);
         } else if (var1 == GameSettings.Options.AMBIENT_OCCLUSION) {
            return var3 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
         } else if (var1 == GameSettings.Options.STREAM_COMPRESSION) {
            return var3 + getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
         } else if (var1 == GameSettings.Options.STREAM_CHAT_ENABLED) {
            return var3 + getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
         } else if (var1 == GameSettings.Options.STREAM_CHAT_USER_FILTER) {
            return var3 + getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
         } else if (var1 == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return var3 + getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
         } else if (var1 == GameSettings.Options.RENDER_CLOUDS) {
            return var3 + getTranslation(field_181149_aW, this.clouds);
         } else if (var1 == GameSettings.Options.GRAPHICS) {
            if (this.fancyGraphics) {
               return var3 + I18n.format("options.graphics.fancy");
            } else {
               String var4 = "options.graphics.fast";
               return var3 + I18n.format("options.graphics.fast");
            }
         } else {
            return var3;
         }
      }
   }

   private String getKeyBindingOF(GameSettings.Options var1) {
      String var2 = I18n.format(var1.getEnumString()) + ": ";
      if (var2 == null) {
         var2 = var1.getEnumString();
      }

      int var8;
      if (var1 == GameSettings.Options.RENDER_DISTANCE) {
         var8 = (int)this.getOptionFloatValue(var1);
         String var4 = I18n.format("options.renderDistance.tiny");
         byte var5 = 2;
         if (var8 >= 4) {
            var4 = I18n.format("options.renderDistance.short");
            var5 = 4;
         }

         if (var8 >= 8) {
            var4 = I18n.format("options.renderDistance.normal");
            var5 = 8;
         }

         if (var8 >= 16) {
            var4 = I18n.format("options.renderDistance.far");
            var5 = 16;
         }

         if (var8 >= 32) {
            var4 = Lang.get("of.options.renderDistance.extreme");
            var5 = 32;
         }

         int var6 = this.renderDistanceChunks - var5;
         String var7 = var4;
         if (var6 > 0) {
            var7 = var4 + "+";
         }

         return var2 + var8 + " " + var7;
      } else if (var1 == GameSettings.Options.FOG_FANCY) {
         switch(this.ofFogType) {
         case 1:
            return var2 + Lang.getFast();
         case 2:
            return var2 + Lang.getFancy();
         case 3:
            return var2 + Lang.getOff();
         default:
            return var2 + Lang.getOff();
         }
      } else if (var1 == GameSettings.Options.FOG_START) {
         return var2 + this.ofFogStart;
      } else if (var1 == GameSettings.Options.MIPMAP_TYPE) {
         switch(this.ofMipmapType) {
         case 0:
            return var2 + Lang.get("of.options.mipmap.nearest");
         case 1:
            return var2 + Lang.get("of.options.mipmap.linear");
         case 2:
            return var2 + Lang.get("of.options.mipmap.bilinear");
         case 3:
            return var2 + Lang.get("of.options.mipmap.trilinear");
         default:
            return var2 + "of.options.mipmap.nearest";
         }
      } else if (var1 == GameSettings.Options.SMOOTH_FPS) {
         return this.ofSmoothFps ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.SMOOTH_WORLD) {
         return this.ofSmoothWorld ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.CLOUDS) {
         switch(this.ofClouds) {
         case 1:
            return var2 + Lang.getFast();
         case 2:
            return var2 + Lang.getFancy();
         case 3:
            return var2 + Lang.getOff();
         default:
            return var2 + Lang.getDefault();
         }
      } else if (var1 == GameSettings.Options.TREES) {
         switch(this.ofTrees) {
         case 1:
            return var2 + Lang.getFast();
         case 2:
            return var2 + Lang.getFancy();
         case 3:
         default:
            return var2 + Lang.getDefault();
         case 4:
            return var2 + Lang.get("of.general.smart");
         }
      } else if (var1 == GameSettings.Options.DROPPED_ITEMS) {
         switch(this.ofDroppedItems) {
         case 1:
            return var2 + Lang.getFast();
         case 2:
            return var2 + Lang.getFancy();
         default:
            return var2 + Lang.getDefault();
         }
      } else if (var1 == GameSettings.Options.RAIN) {
         switch(this.ofRain) {
         case 1:
            return var2 + Lang.getFast();
         case 2:
            return var2 + Lang.getFancy();
         case 3:
            return var2 + Lang.getOff();
         default:
            return var2 + Lang.getDefault();
         }
      } else if (var1 == GameSettings.Options.ANIMATED_WATER) {
         switch(this.ofAnimatedWater) {
         case 1:
            return var2 + Lang.get("of.options.animation.dynamic");
         case 2:
            return var2 + Lang.getOff();
         default:
            return var2 + Lang.getOn();
         }
      } else if (var1 == GameSettings.Options.ANIMATED_LAVA) {
         switch(this.ofAnimatedLava) {
         case 1:
            return var2 + Lang.get("of.options.animation.dynamic");
         case 2:
            return var2 + Lang.getOff();
         default:
            return var2 + Lang.getOn();
         }
      } else if (var1 == GameSettings.Options.ANIMATED_FIRE) {
         return this.ofAnimatedFire ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.ANIMATED_PORTAL) {
         return this.ofAnimatedPortal ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.ANIMATED_REDSTONE) {
         return this.ofAnimatedRedstone ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.ANIMATED_EXPLOSION) {
         return this.ofAnimatedExplosion ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.ANIMATED_FLAME) {
         return this.ofAnimatedFlame ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.ANIMATED_SMOKE) {
         return this.ofAnimatedSmoke ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.VOID_PARTICLES) {
         return this.ofVoidParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.WATER_PARTICLES) {
         return this.ofWaterParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.PORTAL_PARTICLES) {
         return this.ofPortalParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.POTION_PARTICLES) {
         return this.ofPotionParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.FIREWORK_PARTICLES) {
         return this.ofFireworkParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.DRIPPING_WATER_LAVA) {
         return this.ofDrippingWaterLava ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.ANIMATED_TERRAIN) {
         return this.ofAnimatedTerrain ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.ANIMATED_TEXTURES) {
         return this.ofAnimatedTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.RAIN_SPLASH) {
         return this.ofRainSplash ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.LAGOMETER) {
         return this.ofLagometer ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.SHOW_FPS) {
         return this.ofShowFps ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.AUTOSAVE_TICKS) {
         return this.ofAutoSaveTicks <= 40 ? var2 + Lang.get("of.options.save.default") : (this.ofAutoSaveTicks <= 400 ? var2 + Lang.get("of.options.save.20s") : (this.ofAutoSaveTicks <= 4000 ? var2 + Lang.get("of.options.save.3min") : var2 + Lang.get("of.options.save.30min")));
      } else if (var1 == GameSettings.Options.BETTER_GRASS) {
         switch(this.ofBetterGrass) {
         case 1:
            return var2 + Lang.getFast();
         case 2:
            return var2 + Lang.getFancy();
         default:
            return var2 + Lang.getOff();
         }
      } else if (var1 == GameSettings.Options.CONNECTED_TEXTURES) {
         switch(this.ofConnectedTextures) {
         case 1:
            return var2 + Lang.getFast();
         case 2:
            return var2 + Lang.getFancy();
         default:
            return var2 + Lang.getOff();
         }
      } else if (var1 == GameSettings.Options.WEATHER) {
         return this.ofWeather ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.SKY) {
         return this.ofSky ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.STARS) {
         return this.ofStars ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.SUN_MOON) {
         return this.ofSunMoon ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.VIGNETTE) {
         switch(this.ofVignette) {
         case 1:
            return var2 + Lang.getFast();
         case 2:
            return var2 + Lang.getFancy();
         default:
            return var2 + Lang.getDefault();
         }
      } else if (var1 == GameSettings.Options.CHUNK_UPDATES) {
         return var2 + this.ofChunkUpdates;
      } else if (var1 == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
         return this.ofChunkUpdatesDynamic ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.TIME) {
         return this.ofTime == 1 ? var2 + Lang.get("of.options.time.dayOnly") : (this.ofTime == 2 ? var2 + Lang.get("of.options.time.nightOnly") : var2 + Lang.getDefault());
      } else if (var1 == GameSettings.Options.CLEAR_WATER) {
         return this.ofClearWater ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.AA_LEVEL) {
         String var9 = "";
         if (this.ofAaLevel != Config.getAntialiasingLevel()) {
            var9 = " (" + Lang.get("of.general.restart") + ")";
         }

         return this.ofAaLevel == 0 ? var2 + Lang.getOff() + var9 : var2 + this.ofAaLevel + var9;
      } else if (var1 == GameSettings.Options.AF_LEVEL) {
         return this.ofAfLevel == 1 ? var2 + Lang.getOff() : var2 + this.ofAfLevel;
      } else if (var1 == GameSettings.Options.PROFILER) {
         return this.ofProfiler ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.BETTER_SNOW) {
         return this.ofBetterSnow ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.SWAMP_COLORS) {
         return this.ofSwampColors ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.RANDOM_MOBS) {
         return this.ofRandomMobs ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.SMOOTH_BIOMES) {
         return this.ofSmoothBiomes ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.CUSTOM_FONTS) {
         return this.ofCustomFonts ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.CUSTOM_COLORS) {
         return this.ofCustomColors ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.CUSTOM_SKY) {
         return this.ofCustomSky ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.SHOW_CAPES) {
         return this.ofShowCapes ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.CUSTOM_ITEMS) {
         return this.ofCustomItems ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.NATURAL_TEXTURES) {
         return this.ofNaturalTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.FAST_MATH) {
         return this.ofFastMath ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.FAST_RENDER) {
         return this.ofFastRender ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.TRANSLUCENT_BLOCKS) {
         return this.ofTranslucentBlocks == 1 ? var2 + Lang.getFast() : (this.ofTranslucentBlocks == 2 ? var2 + Lang.getFancy() : var2 + Lang.getDefault());
      } else if (var1 == GameSettings.Options.LAZY_CHUNK_LOADING) {
         return this.ofLazyChunkLoading ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.DYNAMIC_FOV) {
         return this.ofDynamicFov ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.DYNAMIC_LIGHTS) {
         var8 = indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
         return var2 + getTranslation(KEYS_DYNAMIC_LIGHTS, var8);
      } else if (var1 == GameSettings.Options.FULLSCREEN_MODE) {
         return this.ofFullscreenMode.equals("Default") ? var2 + Lang.getDefault() : var2 + this.ofFullscreenMode;
      } else if (var1 == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
         return this.heldItemTooltips ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (var1 == GameSettings.Options.FRAMERATE_LIMIT) {
         float var3 = this.getOptionFloatValue(var1);
         return var3 == 0.0F ? var2 + Lang.get("of.options.framerateLimit.vsync") : (var3 == GameSettings.Options.access$2(var1) ? var2 + I18n.format("options.framerateLimit.max") : var2 + (int)var3 + " fps");
      } else {
         return null;
      }
   }

   public void setOptionValue(GameSettings.Options var1, int var2) {
      this.setOptionValueOF(var1, var2);
      if (var1 == GameSettings.Options.INVERT_MOUSE) {
         this.invertMouse = !this.invertMouse;
      }

      if (var1 == GameSettings.Options.GUI_SCALE) {
         this.guiScale = this.guiScale + var2 & 3;
      }

      if (var1 == GameSettings.Options.PARTICLES) {
         this.particleSetting = (this.particleSetting + var2) % 3;
      }

      if (var1 == GameSettings.Options.VIEW_BOBBING) {
         this.viewBobbing = !this.viewBobbing;
      }

      if (var1 == GameSettings.Options.RENDER_CLOUDS) {
         this.clouds = (this.clouds + var2) % 3;
      }

      if (var1 == GameSettings.Options.FORCE_UNICODE_FONT) {
         this.forceUnicodeFont = !this.forceUnicodeFont;
         Minecraft.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
      }

      if (var1 == GameSettings.Options.FBO_ENABLE) {
         this.fboEnable = !this.fboEnable;
      }

      if (var1 == GameSettings.Options.ANAGLYPH) {
         if (!this.anaglyph && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
            return;
         }

         this.anaglyph = !this.anaglyph;
         this.mc.refreshResources();
      }

      if (var1 == GameSettings.Options.GRAPHICS) {
         this.fancyGraphics = !this.fancyGraphics;
         this.updateRenderClouds();
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.AMBIENT_OCCLUSION) {
         this.ambientOcclusion = (this.ambientOcclusion + var2) % 3;
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.CHAT_VISIBILITY) {
         this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + var2) % 3);
      }

      if (var1 == GameSettings.Options.STREAM_COMPRESSION) {
         this.streamCompression = (this.streamCompression + var2) % 3;
      }

      if (var1 == GameSettings.Options.STREAM_SEND_METADATA) {
         this.streamSendMetadata = !this.streamSendMetadata;
      }

      if (var1 == GameSettings.Options.STREAM_CHAT_ENABLED) {
         this.streamChatEnabled = (this.streamChatEnabled + var2) % 3;
      }

      if (var1 == GameSettings.Options.STREAM_CHAT_USER_FILTER) {
         this.streamChatUserFilter = (this.streamChatUserFilter + var2) % 3;
      }

      if (var1 == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
         this.streamMicToggleBehavior = (this.streamMicToggleBehavior + var2) % 2;
      }

      if (var1 == GameSettings.Options.CHAT_COLOR) {
         this.chatColours = !this.chatColours;
      }

      if (var1 == GameSettings.Options.CHAT_LINKS) {
         this.chatLinks = !this.chatLinks;
      }

      if (var1 == GameSettings.Options.CHAT_LINKS_PROMPT) {
         this.chatLinksPrompt = !this.chatLinksPrompt;
      }

      if (var1 == GameSettings.Options.SNOOPER_ENABLED) {
         this.snooperEnabled = !this.snooperEnabled;
      }

      if (var1 == GameSettings.Options.TOUCHSCREEN) {
         this.touchscreen = !this.touchscreen;
      }

      if (var1 == GameSettings.Options.USE_FULLSCREEN) {
         this.fullScreen = !this.fullScreen;
         if (this.mc.isFullScreen() != this.fullScreen) {
            this.mc.toggleFullscreen();
         }
      }

      if (var1 == GameSettings.Options.ENABLE_VSYNC) {
         this.enableVsync = !this.enableVsync;
         Display.setVSyncEnabled(this.enableVsync);
      }

      if (var1 == GameSettings.Options.USE_VBO) {
         this.useVbo = !this.useVbo;
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.BLOCK_ALTERNATIVES) {
         this.allowBlockAlternatives = !this.allowBlockAlternatives;
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.REDUCED_DEBUG_INFO) {
         this.reducedDebugInfo = !this.reducedDebugInfo;
      }

      if (var1 == GameSettings.Options.ENTITY_SHADOWS) {
         this.field_181151_V = !this.field_181151_V;
      }

      this.saveOptions();
   }

   public GameSettings(Minecraft var1, File var2) {
      this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
      this.chatColours = true;
      this.chatLinks = true;
      this.chatLinksPrompt = true;
      this.chatOpacity = 1.0F;
      this.snooperEnabled = true;
      this.enableVsync = true;
      this.useVbo = false;
      this.allowBlockAlternatives = true;
      this.reducedDebugInfo = false;
      this.pauseOnLostFocus = true;
      this.setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
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
      this.field_181150_U = true;
      this.field_181151_V = true;
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
      this.ofFogType = 1;
      this.ofFogStart = 0.8F;
      this.ofMipmapType = 0;
      this.ofOcclusionFancy = false;
      this.ofSmoothFps = false;
      this.ofSmoothWorld = Config.isSingleProcessor();
      this.ofLazyChunkLoading = Config.isSingleProcessor();
      this.ofAoLevel = 1.0F;
      this.ofAaLevel = 0;
      this.ofAfLevel = 1;
      this.ofClouds = 0;
      this.ofCloudsHeight = 0.0F;
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
      this.ofRandomMobs = true;
      this.ofSmoothBiomes = true;
      this.ofCustomFonts = true;
      this.ofCustomColors = true;
      this.ofCustomSky = true;
      this.ofShowCapes = true;
      this.ofConnectedTextures = 2;
      this.ofCustomItems = true;
      this.ofNaturalTextures = false;
      this.ofFastMath = false;
      this.ofFastRender = true;
      this.ofTranslucentBlocks = 0;
      this.ofDynamicFov = true;
      this.ofDynamicLights = 3;
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
      this.togglePerspective = new KeyBinding("Toggle Perspective", 33, "TANKV4");
      this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])(new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}), (Object[])this.keyBindsHotbar);
      this.difficulty = EnumDifficulty.NORMAL;
      this.lastServer = "";
      this.fovSetting = 70.0F;
      this.language = "en_US";
      this.forceUnicodeFont = false;
      this.mc = var1;
      this.optionsFile = new File(var2, "options.txt");
      this.optionsFileOF = new File(var2, "optionsof.txt");
      this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
      this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
      this.keyBindings = (KeyBinding[])ArrayUtils.add(this.keyBindings, this.ofKeyBindZoom);
      GameSettings.Options.RENDER_DISTANCE.setValueMax(32.0F);
      this.renderDistanceChunks = 8;
      this.loadOptions();
      Config.initGameSettings(this);
      this.registerKeybinds();
   }

   private void setOptionValueOF(GameSettings.Options var1, int var2) {
      if (var1 == GameSettings.Options.FOG_FANCY) {
         switch(this.ofFogType) {
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

      if (var1 == GameSettings.Options.FOG_START) {
         this.ofFogStart += 0.2F;
         if (this.ofFogStart > 0.81F) {
            this.ofFogStart = 0.2F;
         }
      }

      if (var1 == GameSettings.Options.SMOOTH_FPS) {
         this.ofSmoothFps = !this.ofSmoothFps;
      }

      if (var1 == GameSettings.Options.SMOOTH_WORLD) {
         this.ofSmoothWorld = !this.ofSmoothWorld;
         Config.updateThreadPriorities();
      }

      if (var1 == GameSettings.Options.CLOUDS) {
         ++this.ofClouds;
         if (this.ofClouds > 3) {
            this.ofClouds = 0;
         }

         this.updateRenderClouds();
         this.mc.renderGlobal.resetClouds();
      }

      if (var1 == GameSettings.Options.TREES) {
         this.ofTrees = nextValue(this.ofTrees, OF_TREES_VALUES);
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.DROPPED_ITEMS) {
         ++this.ofDroppedItems;
         if (this.ofDroppedItems > 2) {
            this.ofDroppedItems = 0;
         }
      }

      if (var1 == GameSettings.Options.RAIN) {
         ++this.ofRain;
         if (this.ofRain > 3) {
            this.ofRain = 0;
         }
      }

      if (var1 == GameSettings.Options.ANIMATED_WATER) {
         ++this.ofAnimatedWater;
         if (this.ofAnimatedWater == 1) {
            ++this.ofAnimatedWater;
         }

         if (this.ofAnimatedWater > 2) {
            this.ofAnimatedWater = 0;
         }
      }

      if (var1 == GameSettings.Options.ANIMATED_LAVA) {
         ++this.ofAnimatedLava;
         if (this.ofAnimatedLava == 1) {
            ++this.ofAnimatedLava;
         }

         if (this.ofAnimatedLava > 2) {
            this.ofAnimatedLava = 0;
         }
      }

      if (var1 == GameSettings.Options.ANIMATED_FIRE) {
         this.ofAnimatedFire = !this.ofAnimatedFire;
      }

      if (var1 == GameSettings.Options.ANIMATED_PORTAL) {
         this.ofAnimatedPortal = !this.ofAnimatedPortal;
      }

      if (var1 == GameSettings.Options.ANIMATED_REDSTONE) {
         this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
      }

      if (var1 == GameSettings.Options.ANIMATED_EXPLOSION) {
         this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
      }

      if (var1 == GameSettings.Options.ANIMATED_FLAME) {
         this.ofAnimatedFlame = !this.ofAnimatedFlame;
      }

      if (var1 == GameSettings.Options.ANIMATED_SMOKE) {
         this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
      }

      if (var1 == GameSettings.Options.VOID_PARTICLES) {
         this.ofVoidParticles = !this.ofVoidParticles;
      }

      if (var1 == GameSettings.Options.WATER_PARTICLES) {
         this.ofWaterParticles = !this.ofWaterParticles;
      }

      if (var1 == GameSettings.Options.PORTAL_PARTICLES) {
         this.ofPortalParticles = !this.ofPortalParticles;
      }

      if (var1 == GameSettings.Options.POTION_PARTICLES) {
         this.ofPotionParticles = !this.ofPotionParticles;
      }

      if (var1 == GameSettings.Options.FIREWORK_PARTICLES) {
         this.ofFireworkParticles = !this.ofFireworkParticles;
      }

      if (var1 == GameSettings.Options.DRIPPING_WATER_LAVA) {
         this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
      }

      if (var1 == GameSettings.Options.ANIMATED_TERRAIN) {
         this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
      }

      if (var1 == GameSettings.Options.ANIMATED_TEXTURES) {
         this.ofAnimatedTextures = !this.ofAnimatedTextures;
      }

      if (var1 == GameSettings.Options.RAIN_SPLASH) {
         this.ofRainSplash = !this.ofRainSplash;
      }

      if (var1 == GameSettings.Options.LAGOMETER) {
         this.ofLagometer = !this.ofLagometer;
      }

      if (var1 == GameSettings.Options.SHOW_FPS) {
         this.ofShowFps = !this.ofShowFps;
      }

      if (var1 == GameSettings.Options.AUTOSAVE_TICKS) {
         this.ofAutoSaveTicks *= 10;
         if (this.ofAutoSaveTicks > 40000) {
            this.ofAutoSaveTicks = 40;
         }
      }

      if (var1 == GameSettings.Options.BETTER_GRASS) {
         ++this.ofBetterGrass;
         if (this.ofBetterGrass > 3) {
            this.ofBetterGrass = 1;
         }

         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.CONNECTED_TEXTURES) {
         ++this.ofConnectedTextures;
         if (this.ofConnectedTextures > 3) {
            this.ofConnectedTextures = 1;
         }

         if (this.ofConnectedTextures != 2) {
            this.mc.refreshResources();
         }
      }

      if (var1 == GameSettings.Options.WEATHER) {
         this.ofWeather = !this.ofWeather;
      }

      if (var1 == GameSettings.Options.SKY) {
         this.ofSky = !this.ofSky;
      }

      if (var1 == GameSettings.Options.STARS) {
         this.ofStars = !this.ofStars;
      }

      if (var1 == GameSettings.Options.SUN_MOON) {
         this.ofSunMoon = !this.ofSunMoon;
      }

      if (var1 == GameSettings.Options.VIGNETTE) {
         ++this.ofVignette;
         if (this.ofVignette > 2) {
            this.ofVignette = 0;
         }
      }

      if (var1 == GameSettings.Options.CHUNK_UPDATES) {
         ++this.ofChunkUpdates;
         if (this.ofChunkUpdates > 5) {
            this.ofChunkUpdates = 1;
         }
      }

      if (var1 == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
         this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
      }

      if (var1 == GameSettings.Options.TIME) {
         ++this.ofTime;
         if (this.ofTime > 2) {
            this.ofTime = 0;
         }
      }

      if (var1 == GameSettings.Options.CLEAR_WATER) {
         this.ofClearWater = !this.ofClearWater;
         this.updateWaterOpacity();
      }

      if (var1 == GameSettings.Options.PROFILER) {
         this.ofProfiler = !this.ofProfiler;
      }

      if (var1 == GameSettings.Options.BETTER_SNOW) {
         this.ofBetterSnow = !this.ofBetterSnow;
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.SWAMP_COLORS) {
         this.ofSwampColors = !this.ofSwampColors;
         CustomColors.updateUseDefaultGrassFoliageColors();
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.RANDOM_MOBS) {
         this.ofRandomMobs = !this.ofRandomMobs;
         RandomMobs.resetTextures();
      }

      if (var1 == GameSettings.Options.SMOOTH_BIOMES) {
         this.ofSmoothBiomes = !this.ofSmoothBiomes;
         CustomColors.updateUseDefaultGrassFoliageColors();
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.CUSTOM_FONTS) {
         this.ofCustomFonts = !this.ofCustomFonts;
         Minecraft.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
         this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
      }

      if (var1 == GameSettings.Options.CUSTOM_COLORS) {
         this.ofCustomColors = !this.ofCustomColors;
         CustomColors.update();
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.CUSTOM_ITEMS) {
         this.ofCustomItems = !this.ofCustomItems;
         this.mc.refreshResources();
      }

      if (var1 == GameSettings.Options.CUSTOM_SKY) {
         this.ofCustomSky = !this.ofCustomSky;
         CustomSky.update();
      }

      if (var1 == GameSettings.Options.SHOW_CAPES) {
         this.ofShowCapes = !this.ofShowCapes;
      }

      if (var1 == GameSettings.Options.NATURAL_TEXTURES) {
         this.ofNaturalTextures = !this.ofNaturalTextures;
         NaturalTextures.update();
         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.FAST_MATH) {
         this.ofFastMath = !this.ofFastMath;
         MathHelper.fastMath = this.ofFastMath;
      }

      if (var1 == GameSettings.Options.FAST_RENDER) {
         if (!this.ofFastRender && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.fr.shaders1"), Lang.get("of.message.fr.shaders2"));
            return;
         }

         this.ofFastRender = !this.ofFastRender;
         if (this.ofFastRender) {
            this.mc.entityRenderer.func_181022_b();
         }

         Config.updateFramebufferSize();
      }

      if (var1 == GameSettings.Options.TRANSLUCENT_BLOCKS) {
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

      if (var1 == GameSettings.Options.LAZY_CHUNK_LOADING) {
         this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
         Config.updateAvailableProcessors();
         if (!Config.isSingleProcessor()) {
            this.ofLazyChunkLoading = false;
         }

         this.mc.renderGlobal.loadRenderers();
      }

      if (var1 == GameSettings.Options.FULLSCREEN_MODE) {
         List var3 = Arrays.asList(Config.getDisplayModeNames());
         if (this.ofFullscreenMode.equals("Default")) {
            this.ofFullscreenMode = (String)var3.get(0);
         } else {
            int var4 = var3.indexOf(this.ofFullscreenMode);
            if (var4 < 0) {
               this.ofFullscreenMode = "Default";
            } else {
               ++var4;
               if (var4 >= var3.size()) {
                  this.ofFullscreenMode = "Default";
               } else {
                  this.ofFullscreenMode = (String)var3.get(var4);
               }
            }
         }
      }

      if (var1 == GameSettings.Options.DYNAMIC_FOV) {
         this.ofDynamicFov = !this.ofDynamicFov;
      }

      if (var1 == GameSettings.Options.DYNAMIC_LIGHTS) {
         this.ofDynamicLights = nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
         DynamicLights.removeLights(this.mc.renderGlobal);
      }

      if (var1 == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
         this.heldItemTooltips = !this.heldItemTooltips;
      }

   }

   public GameSettings() {
      this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
      this.chatColours = true;
      this.chatLinks = true;
      this.chatLinksPrompt = true;
      this.chatOpacity = 1.0F;
      this.snooperEnabled = true;
      this.enableVsync = true;
      this.useVbo = false;
      this.allowBlockAlternatives = true;
      this.reducedDebugInfo = false;
      this.pauseOnLostFocus = true;
      this.setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
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
      this.field_181150_U = true;
      this.field_181151_V = true;
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
      this.ofFogType = 1;
      this.ofFogStart = 0.8F;
      this.ofMipmapType = 0;
      this.ofOcclusionFancy = false;
      this.ofSmoothFps = false;
      this.ofSmoothWorld = Config.isSingleProcessor();
      this.ofLazyChunkLoading = Config.isSingleProcessor();
      this.ofAoLevel = 1.0F;
      this.ofAaLevel = 0;
      this.ofAfLevel = 1;
      this.ofClouds = 0;
      this.ofCloudsHeight = 0.0F;
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
      this.ofRandomMobs = true;
      this.ofSmoothBiomes = true;
      this.ofCustomFonts = true;
      this.ofCustomColors = true;
      this.ofCustomSky = true;
      this.ofShowCapes = true;
      this.ofConnectedTextures = 2;
      this.ofCustomItems = true;
      this.ofNaturalTextures = false;
      this.ofFastMath = false;
      this.ofFastRender = true;
      this.ofTranslucentBlocks = 0;
      this.ofDynamicFov = true;
      this.ofDynamicLights = 3;
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
      this.togglePerspective = new KeyBinding("Toggle Perspective", 33, "TANKV4");
      this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])(new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines}), (Object[])this.keyBindsHotbar);
      this.difficulty = EnumDifficulty.NORMAL;
      this.lastServer = "";
      this.fovSetting = 70.0F;
      this.language = "en_US";
      this.forceUnicodeFont = false;
      this.registerKeybinds();
   }

   static final class GameSettings$2 {
      private static final String __OBFID = "CL_00000652";
      static final int[] field_151477_a = new int[GameSettings.Options.values().length];

      static {
         try {
            field_151477_a[GameSettings.Options.INVERT_MOUSE.ordinal()] = 1;
         } catch (NoSuchFieldError var17) {
         }

         try {
            field_151477_a[GameSettings.Options.VIEW_BOBBING.ordinal()] = 2;
         } catch (NoSuchFieldError var16) {
         }

         try {
            field_151477_a[GameSettings.Options.ANAGLYPH.ordinal()] = 3;
         } catch (NoSuchFieldError var15) {
         }

         try {
            field_151477_a[GameSettings.Options.FBO_ENABLE.ordinal()] = 4;
         } catch (NoSuchFieldError var14) {
         }

         try {
            field_151477_a[GameSettings.Options.CHAT_COLOR.ordinal()] = 5;
         } catch (NoSuchFieldError var13) {
         }

         try {
            field_151477_a[GameSettings.Options.CHAT_LINKS.ordinal()] = 6;
         } catch (NoSuchFieldError var12) {
         }

         try {
            field_151477_a[GameSettings.Options.CHAT_LINKS_PROMPT.ordinal()] = 7;
         } catch (NoSuchFieldError var11) {
         }

         try {
            field_151477_a[GameSettings.Options.SNOOPER_ENABLED.ordinal()] = 8;
         } catch (NoSuchFieldError var10) {
         }

         try {
            field_151477_a[GameSettings.Options.USE_FULLSCREEN.ordinal()] = 9;
         } catch (NoSuchFieldError var9) {
         }

         try {
            field_151477_a[GameSettings.Options.ENABLE_VSYNC.ordinal()] = 10;
         } catch (NoSuchFieldError var8) {
         }

         try {
            field_151477_a[GameSettings.Options.USE_VBO.ordinal()] = 11;
         } catch (NoSuchFieldError var7) {
         }

         try {
            field_151477_a[GameSettings.Options.TOUCHSCREEN.ordinal()] = 12;
         } catch (NoSuchFieldError var6) {
         }

         try {
            field_151477_a[GameSettings.Options.STREAM_SEND_METADATA.ordinal()] = 13;
         } catch (NoSuchFieldError var5) {
         }

         try {
            field_151477_a[GameSettings.Options.FORCE_UNICODE_FONT.ordinal()] = 14;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_151477_a[GameSettings.Options.BLOCK_ALTERNATIVES.ordinal()] = 15;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_151477_a[GameSettings.Options.REDUCED_DEBUG_INFO.ordinal()] = 16;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_151477_a[GameSettings.Options.ENTITY_SHADOWS.ordinal()] = 17;
         } catch (NoSuchFieldError var1) {
         }

      }
   }

   public static enum Options {
      FOV("FOV", 2, "options.fov", true, false, 30.0F, 110.0F, 1.0F),
      FOG_FANCY("", 999, "of.options.FOG_FANCY", false, false),
      SUN_MOON("", 999, "of.options.SUN_MOON", false, false),
      FORCE_UNICODE_FONT("FORCE_UNICODE_FONT", 30, "options.forceUnicodeFont", false, true),
      GUI_SCALE("GUI_SCALE", 13, "options.guiScale", false, false),
      MIPMAP_TYPE("", 999, "of.options.MIPMAP_TYPE", true, false, 0.0F, 3.0F, 1.0F),
      DROPPED_ITEMS("", 999, "of.options.DROPPED_ITEMS", false, false),
      CLOUDS("", 999, "of.options.CLOUDS", false, false),
      VIGNETTE("", 999, "of.options.VIGNETTE", false, false),
      REDUCED_DEBUG_INFO("REDUCED_DEBUG_INFO", 42, "options.reducedDebugInfo", false, true);

      private final boolean enumBoolean;
      NATURAL_TEXTURES("", 999, "of.options.NATURAL_TEXTURES", false, false),
      FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F),
      FBO_ENABLE("FBO_ENABLE", 9, "options.fboEnable", false, true),
      INVERT_MOUSE("INVERT_MOUSE", 0, "options.invertMouse", false, true),
      CUSTOM_FONTS("", 999, "of.options.CUSTOM_FONTS", false, false),
      ANIMATED_FLAME("", 999, "of.options.ANIMATED_FLAME", false, false),
      SHOW_CAPES("", 999, "of.options.SHOW_CAPES", false, false),
      FOG_START("", 999, "of.options.FOG_START", false, false);

      private final String enumString;
      SMOOTH_FPS("", 999, "of.options.SMOOTH_FPS", false, false),
      SMOOTH_BIOMES("", 999, "of.options.SMOOTH_BIOMES", false, false),
      SNOOPER_ENABLED("SNOOPER_ENABLED", 20, "options.snooper", false, true),
      ENTITY_SHADOWS("ENTITY_SHADOWS", 43, "options.entityShadows", false, true),
      USE_VBO("USE_VBO", 23, "options.vbo", false, true),
      ANIMATED_PORTAL("", 999, "of.options.ANIMATED_PORTAL", false, false),
      ANIMATED_WATER("", 999, "of.options.ANIMATED_WATER", false, false),
      BETTER_GRASS("", 999, "of.options.BETTER_GRASS", false, false),
      CHUNK_UPDATES("", 999, "of.options.CHUNK_UPDATES", false, false),
      FIREWORK_PARTICLES("", 999, "of.options.FIREWORK_PARTICLES", false, false),
      STREAM_SEND_METADATA("STREAM_SEND_METADATA", 37, "options.stream.sendMetadata", false, true),
      CHAT_SCALE("CHAT_SCALE", 25, "options.chat.scale", true, false),
      ANIMATED_FIRE("", 999, "of.options.ANIMATED_FIRE", false, false),
      STREAM_CHAT_ENABLED("STREAM_CHAT_ENABLED", 38, "options.stream.chat.enabled", false, false),
      SHOW_FPS("", 999, "of.options.SHOW_FPS", false, false),
      AUTOSAVE_TICKS("", 999, "of.options.AUTOSAVE_TICKS", false, false),
      STREAM_COMPRESSION("STREAM_COMPRESSION", 36, "options.stream.compression", false, false),
      CHAT_LINKS_PROMPT("CHAT_LINKS_PROMPT", 19, "options.chat.links.prompt", false, true),
      TIME("", 999, "of.options.TIME", false, false),
      BLOCK_ALTERNATIVES("BLOCK_ALTERNATIVES", 41, "options.blockAlternatives", false, true),
      CHUNK_UPDATES_DYNAMIC("", 999, "of.options.CHUNK_UPDATES_DYNAMIC", false, false);

      private final float valueStep;
      STREAM_KBPS("STREAM_KBPS", 34, "options.stream.kbps", true, false),
      CHAT_VISIBILITY("CHAT_VISIBILITY", 15, "options.chat.visibility", false, false),
      ANIMATED_TEXTURES("", 999, "of.options.ANIMATED_TEXTURES", false, false),
      CHAT_WIDTH("CHAT_WIDTH", 26, "options.chat.width", true, false),
      ANIMATED_SMOKE("", 999, "of.options.ANIMATED_SMOKE", false, false),
      SKY("", 999, "of.options.SKY", false, false),
      STREAM_MIC_TOGGLE_BEHAVIOR("STREAM_MIC_TOGGLE_BEHAVIOR", 40, "options.stream.micToggleBehavior", false, false),
      CLEAR_WATER("", 999, "of.options.CLEAR_WATER", false, false),
      MIPMAP_LEVELS("MIPMAP_LEVELS", 29, "options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
      SENSITIVITY("SENSITIVITY", 1, "options.sensitivity", true, false),
      LAZY_CHUNK_LOADING("", 999, "of.options.LAZY_CHUNK_LOADING", false, false),
      TOUCHSCREEN("TOUCHSCREEN", 24, "options.touchscreen", false, true),
      ANIMATED_REDSTONE("", 999, "of.options.ANIMATED_REDSTONE", false, false),
      STREAM_CHAT_USER_FILTER("STREAM_CHAT_USER_FILTER", 39, "options.stream.chat.userFilter", false, false),
      DRIPPING_WATER_LAVA("", 999, "of.options.DRIPPING_WATER_LAVA", false, false);

      private float valueMax;
      STREAM_FPS("STREAM_FPS", 35, "options.stream.fps", true, false),
      PROFILER("", 999, "of.options.PROFILER", false, false),
      ANIMATED_LAVA("", 999, "of.options.ANIMATED_LAVA", false, false),
      DYNAMIC_LIGHTS("", 999, "of.options.DYNAMIC_LIGHTS", false, false),
      SMOOTH_WORLD("", 999, "of.options.SMOOTH_WORLD", false, false),
      VIEW_BOBBING("VIEW_BOBBING", 6, "options.viewBobbing", false, true),
      CHAT_HEIGHT_FOCUSED("CHAT_HEIGHT_FOCUSED", 27, "options.chat.height.focused", true, false),
      CUSTOM_SKY("", 999, "of.options.CUSTOM_SKY", false, false),
      VOID_PARTICLES("", 999, "of.options.VOID_PARTICLES", false, false),
      AF_LEVEL("", 999, "of.options.AF_LEVEL", true, false, 1.0F, 16.0F, 1.0F),
      ANIMATED_EXPLOSION("", 999, "of.options.ANIMATED_EXPLOSION", false, false),
      CUSTOM_COLORS("", 999, "of.options.CUSTOM_COLORS", false, false),
      PARTICLES("PARTICLES", 14, "options.particles", false, false),
      STARS("", 999, "of.options.STARS", false, false),
      CHAT_HEIGHT_UNFOCUSED("CHAT_HEIGHT_UNFOCUSED", 28, "options.chat.height.unfocused", true, false),
      GRAPHICS("GRAPHICS", 11, "options.graphics", false, false),
      STREAM_BYTES_PER_PIXEL("STREAM_BYTES_PER_PIXEL", 31, "options.stream.bytesPerPixel", true, false),
      ENABLE_VSYNC("ENABLE_VSYNC", 22, "options.vsync", false, true),
      SWAMP_COLORS("", 999, "of.options.SWAMP_COLORS", false, false),
      TREES("", 999, "of.options.TREES", false, false),
      RANDOM_MOBS("", 999, "of.options.RANDOM_MOBS", false, false),
      CUSTOM_ITEMS("", 999, "of.options.CUSTOM_ITEMS", false, false),
      PORTAL_PARTICLES("", 999, "of.options.PORTAL_PARTICLES", false, false),
      HELD_ITEM_TOOLTIPS("", 999, "of.options.HELD_ITEM_TOOLTIPS", false, false);

      private static final GameSettings.Options[] ENUM$VALUES = new GameSettings.Options[]{INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO, ENTITY_SHADOWS, FOG_FANCY, FOG_START, MIPMAP_TYPE, SMOOTH_FPS, CLOUDS, CLOUD_HEIGHT, TREES, RAIN, ANIMATED_WATER, ANIMATED_LAVA, ANIMATED_FIRE, ANIMATED_PORTAL, AO_LEVEL, LAGOMETER, SHOW_FPS, AUTOSAVE_TICKS, BETTER_GRASS, ANIMATED_REDSTONE, ANIMATED_EXPLOSION, ANIMATED_FLAME, ANIMATED_SMOKE, WEATHER, SKY, STARS, SUN_MOON, VIGNETTE, CHUNK_UPDATES, CHUNK_UPDATES_DYNAMIC, TIME, CLEAR_WATER, SMOOTH_WORLD, VOID_PARTICLES, WATER_PARTICLES, RAIN_SPLASH, PORTAL_PARTICLES, POTION_PARTICLES, FIREWORK_PARTICLES, PROFILER, DRIPPING_WATER_LAVA, BETTER_SNOW, FULLSCREEN_MODE, ANIMATED_TERRAIN, SWAMP_COLORS, RANDOM_MOBS, SMOOTH_BIOMES, CUSTOM_FONTS, CUSTOM_COLORS, SHOW_CAPES, CONNECTED_TEXTURES, CUSTOM_ITEMS, AA_LEVEL, AF_LEVEL, ANIMATED_TEXTURES, NATURAL_TEXTURES, HELD_ITEM_TOOLTIPS, DROPPED_ITEMS, LAZY_CHUNK_LOADING, CUSTOM_SKY, FAST_MATH, FAST_RENDER, TRANSLUCENT_BLOCKS, DYNAMIC_FOV, DYNAMIC_LIGHTS};
      STREAM_VOLUME_MIC("STREAM_VOLUME_MIC", 32, "options.stream.micVolumne", true, false),
      TRANSLUCENT_BLOCKS("", 999, "of.options.TRANSLUCENT_BLOCKS", false, false),
      RAIN("", 999, "of.options.RAIN", false, false),
      AA_LEVEL("", 999, "of.options.AA_LEVEL", true, false, 0.0F, 16.0F, 1.0F),
      GAMMA("GAMMA", 3, "options.gamma", true, false);

      private float valueMin;
      CLOUD_HEIGHT("", 999, "of.options.CLOUD_HEIGHT", true, false);

      private final boolean enumFloat;
      BETTER_SNOW("", 999, "of.options.BETTER_SNOW", false, false),
      RENDER_DISTANCE("RENDER_DISTANCE", 5, "options.renderDistance", true, false, 2.0F, 16.0F, 1.0F),
      FULLSCREEN_MODE("", 999, "of.options.FULLSCREEN_MODE", false, false),
      DYNAMIC_FOV("", 999, "of.options.DYNAMIC_FOV", false, false),
      RAIN_SPLASH("", 999, "of.options.RAIN_SPLASH", false, false);

      private static final String __OBFID = "CL_00000653";
      AO_LEVEL("", 999, "of.options.AO_LEVEL", true, false),
      FAST_RENDER("", 999, "of.options.FAST_RENDER", false, false),
      CONNECTED_TEXTURES("", 999, "of.options.CONNECTED_TEXTURES", false, false),
      SATURATION("SATURATION", 4, "options.saturation", true, false),
      WATER_PARTICLES("", 999, "of.options.WATER_PARTICLES", false, false);

      private static final GameSettings.Options[] $VALUES = new GameSettings.Options[]{INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO, ENTITY_SHADOWS};
      CHAT_OPACITY("CHAT_OPACITY", 18, "options.chat.opacity", true, false),
      LAGOMETER("", 999, "of.options.LAGOMETER", false, false),
      RENDER_CLOUDS("RENDER_CLOUDS", 10, "options.renderClouds", false, false),
      CHAT_COLOR("CHAT_COLOR", 16, "options.chat.color", false, true),
      WEATHER("", 999, "of.options.WEATHER", false, false),
      CHAT_LINKS("CHAT_LINKS", 17, "options.chat.links", false, true),
      AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 12, "options.ao", false, false),
      POTION_PARTICLES("", 999, "of.options.POTION_PARTICLES", false, false),
      FAST_MATH("", 999, "of.options.FAST_MATH", false, false),
      ANIMATED_TERRAIN("", 999, "of.options.ANIMATED_TERRAIN", false, false),
      STREAM_VOLUME_SYSTEM("STREAM_VOLUME_SYSTEM", 33, "options.stream.systemVolume", true, false),
      ANAGLYPH("ANAGLYPH", 7, "options.anaglyph", false, true),
      USE_FULLSCREEN("USE_FULLSCREEN", 21, "options.fullscreen", false, true);

      public boolean getEnumBoolean() {
         return this.enumBoolean;
      }

      public void setValueMax(float var1) {
         this.valueMax = var1;
      }

      private Options(String var3, int var4, String var5, boolean var6, boolean var7, float var8, float var9, float var10) {
         this.enumString = var5;
         this.enumFloat = var6;
         this.enumBoolean = var7;
         this.valueMin = var8;
         this.valueMax = var9;
         this.valueStep = var10;
      }

      protected float snapToStep(float var1) {
         if (this.valueStep > 0.0F) {
            var1 = this.valueStep * (float)Math.round(var1 / this.valueStep);
         }

         return var1;
      }

      public static GameSettings.Options getEnumOptions(int var0) {
         GameSettings.Options[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            GameSettings.Options var1 = var4[var2];
            if (var1.returnEnumOrdinal() == var0) {
               return var1;
            }
         }

         return null;
      }

      static float access$3(GameSettings.Options var0) {
         return var0.valueMin;
      }

      public float normalizeValue(float var1) {
         return MathHelper.clamp_float((this.snapToStepClamp(var1) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
      }

      static float access$2(GameSettings.Options var0) {
         return var0.valueMax;
      }

      public String getEnumString() {
         return this.enumString;
      }

      public boolean getEnumFloat() {
         return this.enumFloat;
      }

      public float snapToStepClamp(float var1) {
         var1 = this.snapToStep(var1);
         return MathHelper.clamp_float(var1, this.valueMin, this.valueMax);
      }

      public int returnEnumOrdinal() {
         return this.ordinal();
      }

      public float denormalizeValue(float var1) {
         return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(var1, 0.0F, 1.0F));
      }

      private Options(String var3, int var4, String var5, boolean var6, boolean var7) {
         this(var3, var4, var5, var6, var7, 0.0F, 1.0F, 0.0F);
      }

      public float getValueMax() {
         return this.valueMax;
      }
   }
}
