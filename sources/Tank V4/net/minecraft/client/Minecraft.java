package net.minecraft.client;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import my.NewSnake.Tank.Snake;
import my.NewSnake.Tank.Ui.font.GlyphPageFontRenderer;
import my.NewSnake.event.events.KeyPressEvent;
import my.NewSnake.event.events.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.dispenser.zaadfcx;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Minecraft implements IPlayerUsage, IThreadListener {
   private final PropertyMap field_181038_N;
   public TextureManager renderEngine;
   public static int debugFPS;
   private CrashReport crashReporter;
   private String serverName;
   public final Profiler mcProfiler = new Profiler();
   public int displayHeight;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType;
   private final MinecraftSessionService sessionService;
   private static final List macDisplayModes;
   public ModelManager modelManager;
   public LoadingScreenRenderer loadingScreen;
   long prevFrameTime = -1L;
   public Session session;
   volatile boolean running = true;
   private final File fileResourcepacks;
   long systemTime = getSystemTime();
   private final Queue scheduledTasks = Queues.newArrayDeque();
   private boolean integratedServerIsRunning;
   private boolean enableGLErrorChecking = true;
   private final PropertyMap twitchDetails;
   private int tempDisplayWidth;
   private NetworkManager myNetworkManager;
   private String debugProfilerName = "root";
   private boolean isGamePaused;
   public boolean renderChunksMany = true;
   public static WorldClient theWorld;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$MovingObjectPosition$MovingObjectType;
   public static byte[] memoryReserve;
   private static Minecraft theMinecraft;
   private BlockRendererDispatcher blockRenderDispatcher;
   private final Proxy proxy;
   private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
   public static PlayerControllerMP playerController;
   private ResourcePackRepository mcResourcePackRepository;
   int fpsCounter;
   private static final Logger logger = LogManager.getLogger();
   public IReloadableResourceManager mcResourceManager;
   public boolean field_175613_B = false;
   private Entity renderViewEntity;
   public boolean skipRenderWorld;
   private SoundHandler mcSoundHandler;
   private ResourceLocation mojangLogo;
   private final boolean isDemo;
   public GuiIngame ingameGUI;
   public final FrameTimer field_181542_y = new FrameTimer();
   private int serverPort;
   private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
   private boolean fullscreen;
   public EffectRenderer effectRenderer;
   private int tempDisplayHeight;
   long field_181543_z = System.nanoTime();
   public GlyphPageFontRenderer clientFont;
   private int leftClickCounter;
   public static FontRenderer fontRendererObj;
   private final List defaultResourcePacks = Lists.newArrayList();
   public static final boolean isRunningOnMac;
   public GuiAchievement guiAchievement;
   public GlyphPageFontRenderer watermarkFont;
   public Timer timer = new Timer(20.0F);
   private MusicTicker mcMusicTicker;
   public MovingObjectPosition objectMouseOver;
   public RenderManager renderManager;
   private final File fileAssets;
   public LanguageManager mcLanguageManager;
   public final File mcDataDir;
   private IStream stream;
   private boolean field_181541_X = false;
   public MouseHelper mouseHelper;
   private SkinManager skinManager;
   private int rightClickDelayTimer;
   private ItemRenderer itemRenderer;
   public Entity pointedEntity;
   private final DefaultResourcePack mcDefaultResourcePack;
   private int joinPlayerCounter;
   public String debug = "";
   public boolean field_175614_C = false;
   private ISaveFormat saveLoader;
   public EntityRenderer entityRenderer;
   private long field_175615_aJ = 0L;
   public FontRenderer standardGalacticFontRenderer;
   public static EntityPlayerSP thePlayer;
   public int displayWidth;
   private final String launchedVersion;
   private TextureMap textureMapBlocks;
   long debugUpdateTime = getSystemTime();
   private Framebuffer framebufferMc;
   public RenderItem renderItem;
   public GuiScreen currentScreen;
   public GameSettings gameSettings;
   private final Thread mcThread = Thread.currentThread();
   private ServerData currentServerData;
   private boolean hasCrashed;
   private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
   private long debugCrashKeyPressTime = -1L;
   public boolean field_175611_D = false;
   private IntegratedServer theIntegratedServer;
   public boolean inGameHasFocus;
   public RenderGlobal renderGlobal;
   private final boolean jvm64bit;

   public RenderItem getRenderItem() {
      return this.renderItem;
   }

   public void freeMemory() {
      try {
         memoryReserve = new byte[0];
         this.renderGlobal.deleteAllDisplayLists();
      } catch (Throwable var4) {
      }

      try {
         System.gc();
         this.loadWorld((WorldClient)null);
      } catch (Throwable var3) {
      }

      System.gc();
   }

   public final boolean isDemo() {
      return this.isDemo;
   }

   public TextureManager getTextureManager() {
      return this.renderEngine;
   }

   public void addServerStatsToSnooper(PlayerUsageSnooper var1) {
      var1.addClientStat("fps", debugFPS);
      var1.addClientStat("vsync_enabled", this.gameSettings.enableVsync);
      var1.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
      var1.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
      var1.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - var1.getMinecraftStartTimeMillis()) / 60L * 1000L);
      var1.addClientStat("current_action", this.func_181538_aA());
      String var2 = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
      var1.addClientStat("endianness", var2);
      var1.addClientStat("resource_packs", this.mcResourcePackRepository.getRepositoryEntries().size());
      int var3 = 0;
      Iterator var5 = this.mcResourcePackRepository.getRepositoryEntries().iterator();

      while(var5.hasNext()) {
         ResourcePackRepository.Entry var4 = (ResourcePackRepository.Entry)var5.next();
         var1.addClientStat("resource_pack[" + var3++ + "]", var4.getResourcePackName());
      }

      if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null) {
         var1.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
      }

   }

   public IntegratedServer getIntegratedServer() {
      return this.theIntegratedServer;
   }

   public boolean isIntegratedServerRunning() {
      return this.integratedServerIsRunning;
   }

   public int getLimitFramerate() {
      return theWorld == null && this.currentScreen != null ? 30 : this.gameSettings.limitFramerate;
   }

   public void addServerTypeToSnooper(PlayerUsageSnooper var1) {
      var1.addStatToSnooper("opengl_version", GL11.glGetString(7938));
      var1.addStatToSnooper("opengl_vendor", GL11.glGetString(7936));
      var1.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
      var1.addStatToSnooper("launched_version", this.launchedVersion);
      ContextCapabilities var2 = GLContext.getCapabilities();
      var1.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", var2.GL_ARB_arrays_of_arrays);
      var1.addStatToSnooper("gl_caps[ARB_base_instance]", var2.GL_ARB_base_instance);
      var1.addStatToSnooper("gl_caps[ARB_blend_func_extended]", var2.GL_ARB_blend_func_extended);
      var1.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", var2.GL_ARB_clear_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_color_buffer_float]", var2.GL_ARB_color_buffer_float);
      var1.addStatToSnooper("gl_caps[ARB_compatibility]", var2.GL_ARB_compatibility);
      var1.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", var2.GL_ARB_compressed_texture_pixel_storage);
      var1.addStatToSnooper("gl_caps[ARB_compute_shader]", var2.GL_ARB_compute_shader);
      var1.addStatToSnooper("gl_caps[ARB_copy_buffer]", var2.GL_ARB_copy_buffer);
      var1.addStatToSnooper("gl_caps[ARB_copy_image]", var2.GL_ARB_copy_image);
      var1.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", var2.GL_ARB_depth_buffer_float);
      var1.addStatToSnooper("gl_caps[ARB_compute_shader]", var2.GL_ARB_compute_shader);
      var1.addStatToSnooper("gl_caps[ARB_copy_buffer]", var2.GL_ARB_copy_buffer);
      var1.addStatToSnooper("gl_caps[ARB_copy_image]", var2.GL_ARB_copy_image);
      var1.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", var2.GL_ARB_depth_buffer_float);
      var1.addStatToSnooper("gl_caps[ARB_depth_clamp]", var2.GL_ARB_depth_clamp);
      var1.addStatToSnooper("gl_caps[ARB_depth_texture]", var2.GL_ARB_depth_texture);
      var1.addStatToSnooper("gl_caps[ARB_draw_buffers]", var2.GL_ARB_draw_buffers);
      var1.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", var2.GL_ARB_draw_buffers_blend);
      var1.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", var2.GL_ARB_draw_elements_base_vertex);
      var1.addStatToSnooper("gl_caps[ARB_draw_indirect]", var2.GL_ARB_draw_indirect);
      var1.addStatToSnooper("gl_caps[ARB_draw_instanced]", var2.GL_ARB_draw_instanced);
      var1.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", var2.GL_ARB_explicit_attrib_location);
      var1.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", var2.GL_ARB_explicit_uniform_location);
      var1.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", var2.GL_ARB_fragment_layer_viewport);
      var1.addStatToSnooper("gl_caps[ARB_fragment_program]", var2.GL_ARB_fragment_program);
      var1.addStatToSnooper("gl_caps[ARB_fragment_shader]", var2.GL_ARB_fragment_shader);
      var1.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", var2.GL_ARB_fragment_program_shadow);
      var1.addStatToSnooper("gl_caps[ARB_framebuffer_object]", var2.GL_ARB_framebuffer_object);
      var1.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", var2.GL_ARB_framebuffer_sRGB);
      var1.addStatToSnooper("gl_caps[ARB_geometry_shader4]", var2.GL_ARB_geometry_shader4);
      var1.addStatToSnooper("gl_caps[ARB_gpu_shader5]", var2.GL_ARB_gpu_shader5);
      var1.addStatToSnooper("gl_caps[ARB_half_float_pixel]", var2.GL_ARB_half_float_pixel);
      var1.addStatToSnooper("gl_caps[ARB_half_float_vertex]", var2.GL_ARB_half_float_vertex);
      var1.addStatToSnooper("gl_caps[ARB_instanced_arrays]", var2.GL_ARB_instanced_arrays);
      var1.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", var2.GL_ARB_map_buffer_alignment);
      var1.addStatToSnooper("gl_caps[ARB_map_buffer_range]", var2.GL_ARB_map_buffer_range);
      var1.addStatToSnooper("gl_caps[ARB_multisample]", var2.GL_ARB_multisample);
      var1.addStatToSnooper("gl_caps[ARB_multitexture]", var2.GL_ARB_multitexture);
      var1.addStatToSnooper("gl_caps[ARB_occlusion_query2]", var2.GL_ARB_occlusion_query2);
      var1.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", var2.GL_ARB_pixel_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", var2.GL_ARB_seamless_cube_map);
      var1.addStatToSnooper("gl_caps[ARB_shader_objects]", var2.GL_ARB_shader_objects);
      var1.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", var2.GL_ARB_shader_stencil_export);
      var1.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", var2.GL_ARB_shader_texture_lod);
      var1.addStatToSnooper("gl_caps[ARB_shadow]", var2.GL_ARB_shadow);
      var1.addStatToSnooper("gl_caps[ARB_shadow_ambient]", var2.GL_ARB_shadow_ambient);
      var1.addStatToSnooper("gl_caps[ARB_stencil_texturing]", var2.GL_ARB_stencil_texturing);
      var1.addStatToSnooper("gl_caps[ARB_sync]", var2.GL_ARB_sync);
      var1.addStatToSnooper("gl_caps[ARB_tessellation_shader]", var2.GL_ARB_tessellation_shader);
      var1.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", var2.GL_ARB_texture_border_clamp);
      var1.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", var2.GL_ARB_texture_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_texture_cube_map]", var2.GL_ARB_texture_cube_map);
      var1.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", var2.GL_ARB_texture_cube_map_array);
      var1.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", var2.GL_ARB_texture_non_power_of_two);
      var1.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", var2.GL_ARB_uniform_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_vertex_blend]", var2.GL_ARB_vertex_blend);
      var1.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", var2.GL_ARB_vertex_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_vertex_program]", var2.GL_ARB_vertex_program);
      var1.addStatToSnooper("gl_caps[ARB_vertex_shader]", var2.GL_ARB_vertex_shader);
      var1.addStatToSnooper("gl_caps[EXT_bindable_uniform]", var2.GL_EXT_bindable_uniform);
      var1.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", var2.GL_EXT_blend_equation_separate);
      var1.addStatToSnooper("gl_caps[EXT_blend_func_separate]", var2.GL_EXT_blend_func_separate);
      var1.addStatToSnooper("gl_caps[EXT_blend_minmax]", var2.GL_EXT_blend_minmax);
      var1.addStatToSnooper("gl_caps[EXT_blend_subtract]", var2.GL_EXT_blend_subtract);
      var1.addStatToSnooper("gl_caps[EXT_draw_instanced]", var2.GL_EXT_draw_instanced);
      var1.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", var2.GL_EXT_framebuffer_multisample);
      var1.addStatToSnooper("gl_caps[EXT_framebuffer_object]", var2.GL_EXT_framebuffer_object);
      var1.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", var2.GL_EXT_framebuffer_sRGB);
      var1.addStatToSnooper("gl_caps[EXT_geometry_shader4]", var2.GL_EXT_geometry_shader4);
      var1.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", var2.GL_EXT_gpu_program_parameters);
      var1.addStatToSnooper("gl_caps[EXT_gpu_shader4]", var2.GL_EXT_gpu_shader4);
      var1.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", var2.GL_EXT_multi_draw_arrays);
      var1.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", var2.GL_EXT_packed_depth_stencil);
      var1.addStatToSnooper("gl_caps[EXT_paletted_texture]", var2.GL_EXT_paletted_texture);
      var1.addStatToSnooper("gl_caps[EXT_rescale_normal]", var2.GL_EXT_rescale_normal);
      var1.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", var2.GL_EXT_separate_shader_objects);
      var1.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", var2.GL_EXT_shader_image_load_store);
      var1.addStatToSnooper("gl_caps[EXT_shadow_funcs]", var2.GL_EXT_shadow_funcs);
      var1.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", var2.GL_EXT_shared_texture_palette);
      var1.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", var2.GL_EXT_stencil_clear_tag);
      var1.addStatToSnooper("gl_caps[EXT_stencil_two_side]", var2.GL_EXT_stencil_two_side);
      var1.addStatToSnooper("gl_caps[EXT_stencil_wrap]", var2.GL_EXT_stencil_wrap);
      var1.addStatToSnooper("gl_caps[EXT_texture_3d]", var2.GL_EXT_texture_3d);
      var1.addStatToSnooper("gl_caps[EXT_texture_array]", var2.GL_EXT_texture_array);
      var1.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", var2.GL_EXT_texture_buffer_object);
      var1.addStatToSnooper("gl_caps[EXT_texture_integer]", var2.GL_EXT_texture_integer);
      var1.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", var2.GL_EXT_texture_lod_bias);
      var1.addStatToSnooper("gl_caps[EXT_texture_sRGB]", var2.GL_EXT_texture_sRGB);
      var1.addStatToSnooper("gl_caps[EXT_vertex_shader]", var2.GL_EXT_vertex_shader);
      var1.addStatToSnooper("gl_caps[EXT_vertex_weighting]", var2.GL_EXT_vertex_weighting);
      var1.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GL11.glGetInteger(35658));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GL11.glGetInteger(35657));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GL11.glGetInteger(34921));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GL11.glGetInteger(35660));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger(34930));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger(35071));
      GL11.glGetError();
      var1.addStatToSnooper("gl_max_texture_size", getGLMaximumTextureSize());
   }

   public ListenableFuture addScheduledTask(Runnable var1) {
      Validate.notNull(var1);
      return this.addScheduledTask(Executors.callable(var1));
   }

   public void setRenderViewEntity(Entity var1) {
      this.renderViewEntity = var1;
      this.entityRenderer.loadEntityShader(var1);
   }

   private void clickMouse() {
      if (this.leftClickCounter <= 0) {
         thePlayer.swingItem();
         if (this.objectMouseOver == null) {
            logger.error("Null returned as 'hitResult', this shouldn't happen!");
            if (playerController.isNotCreative()) {
               this.leftClickCounter = 10;
            }
         } else {
            switch($SWITCH_TABLE$net$minecraft$util$MovingObjectPosition$MovingObjectType()[this.objectMouseOver.typeOfHit.ordinal()]) {
            case 2:
               BlockPos var1 = this.objectMouseOver.getBlockPos();
               if (theWorld.getBlockState(var1).getBlock().getMaterial() != Material.air) {
                  playerController.clickBlock(var1, this.objectMouseOver.sideHit);
                  break;
               }
            case 1:
            default:
               if (playerController.isNotCreative()) {
                  this.leftClickCounter = 10;
               }
               break;
            case 3:
               playerController.attackEntity(thePlayer, this.objectMouseOver.entityHit);
            }
         }
      }

   }

   public RenderManager getRenderManager() {
      return this.renderManager;
   }

   public void displayGuiScreen(GuiScreen var1) {
      if (this.currentScreen != null) {
         this.currentScreen.onGuiClosed();
      }

      if (var1 == null && theWorld == null) {
         var1 = new GuiMainMenu();
      } else if (var1 == null && thePlayer.getHealth() <= 0.0F) {
         var1 = new GuiGameOver();
      }

      if (var1 instanceof GuiMainMenu) {
         this.gameSettings.showDebugInfo = false;
         this.ingameGUI.getChatGUI().clearChatMessages();
      }

      this.currentScreen = (GuiScreen)var1;
      if (var1 != null) {
         this.setIngameNotInFocus();
         ScaledResolution var2 = new ScaledResolution(this);
         int var3 = var2.getScaledWidth();
         int var4 = ScaledResolution.getScaledHeight();
         ((GuiScreen)var1).setWorldAndResolution(this, var3, var4);
         this.skipRenderWorld = false;
      } else {
         this.mcSoundHandler.resumeSounds();
         this.setIngameFocus();
      }

   }

   public void func_181536_a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      float var11 = 0.00390625F;
      float var12 = 0.00390625F;
      WorldRenderer var13 = Tessellator.getInstance().getWorldRenderer();
      var13.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      var13.pos((double)var1, (double)(var2 + var6), 0.0D).tex((double)((float)var3 * var11), (double)((float)(var4 + var6) * var12)).color(var7, var8, var9, var10).endVertex();
      var13.pos((double)(var1 + var5), (double)(var2 + var6), 0.0D).tex((double)((float)(var3 + var5) * var11), (double)((float)(var4 + var6) * var12)).color(var7, var8, var9, var10).endVertex();
      var13.pos((double)(var1 + var5), (double)var2, 0.0D).tex((double)((float)(var3 + var5) * var11), (double)((float)var4 * var12)).color(var7, var8, var9, var10).endVertex();
      var13.pos((double)var1, (double)var2, 0.0D).tex((double)((float)var3 * var11), (double)((float)var4 * var12)).color(var7, var8, var9, var10).endVertex();
      Tessellator.getInstance().draw();
   }

   public void func_181537_a(boolean var1) {
      this.field_181541_X = var1;
   }

   private void updateFramebufferSize() {
      this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);
      if (this.entityRenderer != null) {
         this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
      }

   }

   public PropertyMap getTwitchDetails() {
      return this.twitchDetails;
   }

   public NetHandlerPlayClient getNetHandler() {
      return thePlayer != null ? thePlayer.sendQueue : null;
   }

   static String access$2(Minecraft var0) {
      return var0.launchedVersion;
   }

   public ISaveFormat getSaveLoader() {
      return this.saveLoader;
   }

   public void shutdown() {
      this.running = false;
   }

   public Proxy getProxy() {
      return this.proxy;
   }

   public MusicTicker.MusicType getAmbientMusicType() {
      return thePlayer != null ? (thePlayer.worldObj.provider instanceof WorldProviderHell ? MusicTicker.MusicType.NETHER : (thePlayer.worldObj.provider instanceof WorldProviderEnd ? (BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END) : (thePlayer.capabilities.isCreativeMode && thePlayer.capabilities.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME))) : MusicTicker.MusicType.MENU;
   }

   static {
      isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;
      memoryReserve = new byte[10485760];
      macDisplayModes = Lists.newArrayList((Object[])(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)));
   }

   public boolean isFullScreen() {
      return this.fullscreen;
   }

   public SoundHandler getSoundHandler() {
      return this.mcSoundHandler;
   }

   public void loadWorld(WorldClient var1) {
      this.loadWorld(var1, "");
   }

   public PropertyMap func_181037_M() {
      if (this.field_181038_N.isEmpty()) {
         GameProfile var1 = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
         this.field_181038_N.putAll(var1.getProperties());
      }

      return this.field_181038_N;
   }

   public void setIngameFocus() {
      if (Display.isActive() && !this.inGameHasFocus) {
         this.inGameHasFocus = true;
         this.mouseHelper.grabMouseCursor();
         this.displayGuiScreen((GuiScreen)null);
         this.leftClickCounter = 10000;
      }

   }

   public CrashReport addGraphicsAndWorldToCrashReport(CrashReport var1) {
      var1.getCategory().addCrashSectionCallable("Launched Version", new Callable(this) {
         final Minecraft this$0;

         {
            this.this$0 = var1;
         }

         public String call() throws Exception {
            return Minecraft.access$2(this.this$0);
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.getCategory().addCrashSectionCallable("LWJGL", new Callable(this) {
         final Minecraft this$0;

         public Object call() throws Exception {
            return this.call();
         }

         public String call() {
            return Sys.getVersion();
         }

         {
            this.this$0 = var1;
         }
      });
      var1.getCategory().addCrashSectionCallable("OpenGL", new Callable(this) {
         final Minecraft this$0;

         public String call() {
            return GL11.glGetString(7937) + " GL version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936);
         }

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
         }
      });
      var1.getCategory().addCrashSectionCallable("GL Caps", new Callable(this) {
         final Minecraft this$0;

         {
            this.this$0 = var1;
         }

         public String call() {
            return OpenGlHelper.getLogText();
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.getCategory().addCrashSectionCallable("Using VBOs", new Callable(this) {
         final Minecraft this$0;

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
         }

         public String call() {
            return this.this$0.gameSettings.useVbo ? "Yes" : "No";
         }
      });
      var1.getCategory().addCrashSectionCallable("Is Modded", new Callable(this) {
         final Minecraft this$0;

         public String call() throws Exception {
            String var1 = ClientBrandRetriever.getClientModName();
            return !var1.equals("vanilla") ? "Definitely; Client brand changed to '" + var1 + "'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.");
         }

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.getCategory().addCrashSectionCallable("Type", new Callable(this) {
         final Minecraft this$0;

         public String call() throws Exception {
            return "Client (map_client.txt)";
         }

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
         }
      });
      var1.getCategory().addCrashSectionCallable("Resource Packs", new Callable(this) {
         final Minecraft this$0;

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }

         public String call() throws Exception {
            StringBuilder var1 = new StringBuilder();
            Iterator var3 = this.this$0.gameSettings.resourcePacks.iterator();

            while(var3.hasNext()) {
               Object var2 = var3.next();
               if (var1.length() > 0) {
                  var1.append(", ");
               }

               var1.append(var2);
               if (this.this$0.gameSettings.field_183018_l.contains(var2)) {
                  var1.append(" (incompatible)");
               }
            }

            return String.valueOf(var1);
         }
      });
      var1.getCategory().addCrashSectionCallable("Current Language", new Callable(this) {
         final Minecraft this$0;

         public String call() throws Exception {
            return this.this$0.mcLanguageManager.getCurrentLanguage().toString();
         }

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
         }
      });
      var1.getCategory().addCrashSectionCallable("Profiler Position", new Callable(this) {
         final Minecraft this$0;

         public String call() throws Exception {
            return this.this$0.mcProfiler.profilingEnabled ? this.this$0.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
         }

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.getCategory().addCrashSectionCallable("CPU", new Callable(this) {
         final Minecraft this$0;

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
         }

         public String call() {
            return OpenGlHelper.func_183029_j();
         }
      });
      if (theWorld != null) {
         theWorld.addWorldInfoToCrashReport(var1);
      }

      return var1;
   }

   public void shutdownMinecraftApplet() {
      this.stream.shutdownStream();
      logger.info("Stopping!");

      try {
         this.loadWorld((WorldClient)null);
      } catch (Throwable var3) {
      }

      this.mcSoundHandler.unloadSounds();
      Display.destroy();
      if (!this.hasCrashed) {
         System.exit(0);
      }

      System.gc();
   }

   private String func_181538_aA() {
      return this.theIntegratedServer != null ? (this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer") : (this.currentServerData != null ? (this.currentServerData.func_181041_d() ? "playing_lan" : "multiplayer") : "out_of_game");
   }

   private void runGameLoop() throws IOException {
      long var1 = System.nanoTime();
      this.mcProfiler.startSection("root");
      if (Display.isCreated() && Display.isCloseRequested()) {
         this.shutdown();
      }

      if (this.isGamePaused && theWorld != null) {
         float var3 = this.timer.renderPartialTicks;
         this.timer.updateTimer();
         this.timer.renderPartialTicks = var3;
      } else {
         this.timer.updateTimer();
      }

      this.mcProfiler.startSection("scheduledExecutables");
      synchronized(this.scheduledTasks){}

      while(!this.scheduledTasks.isEmpty()) {
         Util.func_181617_a((FutureTask)this.scheduledTasks.poll(), logger);
      }

      this.mcProfiler.endSection();
      long var9 = System.nanoTime();
      this.mcProfiler.startSection("tick");

      for(int var5 = 0; var5 < this.timer.elapsedTicks; ++var5) {
         this.runTick();
      }

      this.mcProfiler.endStartSection("preRenderErrors");
      long var10 = System.nanoTime() - var9;
      this.checkGLError("Pre render");
      this.mcProfiler.endStartSection("sound");
      this.mcSoundHandler.setListener(thePlayer, this.timer.renderPartialTicks);
      this.mcProfiler.endSection();
      this.mcProfiler.startSection("render");
      GlStateManager.pushMatrix();
      GlStateManager.clear(16640);
      this.framebufferMc.bindFramebuffer(true);
      this.mcProfiler.startSection("display");
      GlStateManager.enableTexture2D();
      if (thePlayer != null && thePlayer.isEntityInsideOpaqueBlock()) {
         this.gameSettings.thirdPersonView = 0;
      }

      this.mcProfiler.endSection();
      if (!this.skipRenderWorld) {
         this.mcProfiler.endStartSection("gameRenderer");
         this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, var1);
         this.mcProfiler.endSection();
      }

      this.mcProfiler.endSection();
      if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
         if (!this.mcProfiler.profilingEnabled) {
            this.mcProfiler.clearProfiling();
         }

         this.mcProfiler.profilingEnabled = true;
         this.displayDebugInfo(var10);
      } else {
         this.mcProfiler.profilingEnabled = false;
         this.prevFrameTime = System.nanoTime();
      }

      this.guiAchievement.updateAchievementWindow();
      this.framebufferMc.unbindFramebuffer();
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      this.entityRenderer.renderStreamIndicator(this.timer.renderPartialTicks);
      GlStateManager.popMatrix();
      this.mcProfiler.startSection("root");
      this.updateDisplay();
      Thread.yield();
      this.mcProfiler.startSection("stream");
      this.mcProfiler.startSection("update");
      this.stream.func_152935_j();
      this.mcProfiler.endStartSection("submit");
      this.stream.func_152922_k();
      this.mcProfiler.endSection();
      this.mcProfiler.endSection();
      this.checkGLError("Post render");
      ++this.fpsCounter;
      this.isGamePaused = this != false && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic();
      long var7 = System.nanoTime();
      this.field_181542_y.func_181747_a(var7 - this.field_181543_z);
      this.field_181543_z = var7;

      while(getSystemTime() >= this.debugUpdateTime + 1000L) {
         debugFPS = this.fpsCounter;
         this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", debugFPS, RenderChunk.renderChunksUpdated, RenderChunk.renderChunksUpdated != 1 ? "s" : "", (float)this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : this.gameSettings.limitFramerate, this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 0 ? "" : (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.useVbo() ? " vbo" : "");
         RenderChunk.renderChunksUpdated = 0;
         this.debugUpdateTime += 1000L;
         this.fpsCounter = 0;
         this.usageSnooper.addMemoryStatsToSnooper();
         if (!this.usageSnooper.isSnooperRunning()) {
            this.usageSnooper.startSnooper();
         }
      }

      if (this != false) {
         this.mcProfiler.startSection("fpslimit_wait");
         Display.sync(this.getLimitFramerate());
         this.mcProfiler.endSection();
      }

      this.mcProfiler.endSection();
   }

   public void launchIntegratedServer(String var1, String var2, WorldSettings var3) {
      this.loadWorld((WorldClient)null);
      System.gc();
      ISaveHandler var4 = this.saveLoader.getSaveLoader(var1, false);
      WorldInfo var5 = var4.loadWorldInfo();
      if (var5 == null && var3 != null) {
         var5 = new WorldInfo(var3, var1);
         var4.saveWorldInfo(var5);
      }

      if (var3 == null) {
         var3 = new WorldSettings(var5);
      }

      try {
         this.theIntegratedServer = new IntegratedServer(this, var1, var2, var3);
         this.theIntegratedServer.startServerThread();
         this.integratedServerIsRunning = true;
      } catch (Throwable var10) {
         CrashReport var7 = CrashReport.makeCrashReport(var10, "Starting integrated server");
         CrashReportCategory var8 = var7.makeCategory("Starting integrated server");
         var8.addCrashSection("Level ID", var1);
         var8.addCrashSection("Level Name", var2);
         throw new ReportedException(var7);
      }

      this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel"));

      while(!this.theIntegratedServer.serverIsInRunLoop()) {
         String var6 = this.theIntegratedServer.getUserMessage();
         if (var6 != null) {
            this.loadingScreen.displayLoadingString(I18n.format(var6));
         } else {
            this.loadingScreen.displayLoadingString("");
         }

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var9) {
         }
      }

      this.displayGuiScreen((GuiScreen)null);
      SocketAddress var11 = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
      NetworkManager var12 = NetworkManager.provideLocalClient(var11);
      var12.setNetHandler(new NetHandlerLoginClient(var12, this, (GuiScreen)null));
      var12.sendPacket(new C00Handshake(47, var11.toString(), 0, EnumConnectionState.LOGIN));
      var12.sendPacket(new C00PacketLoginStart(this.getSession().getProfile()));
      this.myNetworkManager = var12;
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$MovingObjectPosition$MovingObjectType() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$MovingObjectPosition$MovingObjectType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[MovingObjectPosition.MovingObjectType.values().length];

         try {
            var0[MovingObjectPosition.MovingObjectType.BLOCK.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[MovingObjectPosition.MovingObjectType.ENTITY.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[MovingObjectPosition.MovingObjectType.MISS.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$MovingObjectPosition$MovingObjectType = var0;
         return var0;
      }
   }

   public static void stopIntegratedServer() {
      if (theMinecraft != null) {
         IntegratedServer var0 = theMinecraft.getIntegratedServer();
         if (var0 != null) {
            var0.stopServer();
         }
      }

   }

   public void toggleFullscreen() {
      try {
         this.fullscreen = !this.fullscreen;
         this.gameSettings.fullScreen = this.fullscreen;
         if (this.fullscreen) {
            this.updateDisplayMode();
            this.displayWidth = Display.getDisplayMode().getWidth();
            this.displayHeight = Display.getDisplayMode().getHeight();
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }
         } else {
            Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
            this.displayWidth = this.tempDisplayWidth;
            this.displayHeight = this.tempDisplayHeight;
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }
         }

         if (this.currentScreen != null) {
            this.resize(this.displayWidth, this.displayHeight);
         } else {
            this.updateFramebufferSize();
         }

         Display.setFullscreen(this.fullscreen);
         Display.setVSyncEnabled(this.gameSettings.enableVsync);
         this.updateDisplay();
      } catch (Exception var2) {
         logger.error((String)"Couldn't toggle fullscreen", (Throwable)var2);
      }

   }

   public String getVersion() {
      return this.launchedVersion;
   }

   private void startTimerHackThread() {
      Thread var1 = new Thread(this, "Timer hack thread") {
         final Minecraft this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            while(this.this$0.running) {
               try {
                  Thread.sleep(2147483647L);
               } catch (InterruptedException var2) {
               }
            }

         }
      };
      var1.setDaemon(true);
      var1.start();
   }

   private void createDisplay() throws LWJGLException {
      Display.setResizable(true);
      Display.setTitle("TankV4");

      try {
         Display.create((new PixelFormat()).withDepthBits(24));
      } catch (LWJGLException var4) {
         logger.error((String)"Couldn't set pixel format", (Throwable)var4);

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var3) {
         }

         if (this.fullscreen) {
            this.updateDisplayMode();
         }

         Display.create();
      }

   }

   public IResourceManager getResourceManager() {
      return this.mcResourceManager;
   }

   private static boolean isJvm64bit() {
      String[] var0 = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
      String[] var4 = var0;
      int var3 = var0.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String var1 = var4[var2];
         String var5 = System.getProperty(var1);
         if (var5 != null && var5.contains("64")) {
            return true;
         }
      }

      return false;
   }

   public void run() {
      this.running = true;

      CrashReport var2;
      try {
         this.startGame();
         Snake.start();
      } catch (Throwable var5) {
         var2 = CrashReport.makeCrashReport(var5, "Initializing game");
         var2.makeCategory("Initialization");
         this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(var2));
         return;
      }

      label40: {
         try {
            while(true) {
               if (!this.running) {
                  break label40;
               }

               if (this.hasCrashed && this.crashReporter != null) {
                  this.displayCrashReport(this.crashReporter);
               } else {
                  try {
                     this.runGameLoop();
                  } catch (OutOfMemoryError var4) {
                     this.freeMemory();
                     this.displayGuiScreen(new GuiMemoryErrorScreen());
                     System.gc();
                  }
               }
            }
         } catch (MinecraftError var6) {
            this.shutdownMinecraftApplet();
         } catch (ReportedException var7) {
            this.addGraphicsAndWorldToCrashReport(var7.getCrashReport());
            this.freeMemory();
            logger.fatal((String)"Reported exception thrown!", (Throwable)var7);
            this.displayCrashReport(var7.getCrashReport());
            this.shutdownMinecraftApplet();
         } catch (Throwable var8) {
            var2 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", var8));
            this.freeMemory();
            logger.fatal("Unreported exception thrown!", var8);
            this.displayCrashReport(var2);
            this.shutdownMinecraftApplet();
         }

         return;
      }

      this.shutdownMinecraftApplet();
   }

   private void middleClickMouse() {
      if (this.objectMouseOver != null) {
         boolean var1 = thePlayer.capabilities.isCreativeMode;
         int var2 = 0;
         boolean var3 = false;
         TileEntity var4 = null;
         Object var5;
         ItemStack var12;
         if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos var6 = this.objectMouseOver.getBlockPos();
            Block var7 = theWorld.getBlockState(var6).getBlock();
            if (var7.getMaterial() == Material.air) {
               return;
            }

            var5 = var7.getItem(theWorld, var6);
            if (var5 == null) {
               return;
            }

            if (var1 && GuiScreen.isCtrlKeyDown()) {
               var4 = theWorld.getTileEntity(var6);
            }

            Block var8 = var5 instanceof ItemBlock && !var7.isFlowerPot() ? Block.getBlockFromItem((Item)var5) : var7;
            var2 = var8.getDamageValue(theWorld, var6);
            var3 = ((Item)var5).getHasSubtypes();
         } else {
            if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !var1) {
               return;
            }

            if (this.objectMouseOver.entityHit instanceof EntityPainting) {
               var5 = Items.painting;
            } else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
               var5 = Items.lead;
            } else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
               EntityItemFrame var9 = (EntityItemFrame)this.objectMouseOver.entityHit;
               var12 = var9.getDisplayedItem();
               if (var12 == null) {
                  var5 = Items.item_frame;
               } else {
                  var5 = var12.getItem();
                  var2 = var12.getMetadata();
                  var3 = true;
               }
            } else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
               EntityMinecart var10 = (EntityMinecart)this.objectMouseOver.entityHit;
               switch($SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType()[var10.getMinecartType().ordinal()]) {
               case 2:
                  var5 = Items.chest_minecart;
                  break;
               case 3:
                  var5 = Items.furnace_minecart;
                  break;
               case 4:
                  var5 = Items.tnt_minecart;
                  break;
               case 5:
               default:
                  var5 = Items.minecart;
                  break;
               case 6:
                  var5 = Items.hopper_minecart;
                  break;
               case 7:
                  var5 = Items.command_block_minecart;
               }
            } else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
               var5 = Items.boat;
            } else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
               var5 = Items.armor_stand;
            } else {
               var5 = Items.spawn_egg;
               var2 = EntityList.getEntityID(this.objectMouseOver.entityHit);
               var3 = true;
               if (!EntityList.entityEggs.containsKey(var2)) {
                  return;
               }
            }
         }

         InventoryPlayer var11 = thePlayer.inventory;
         if (var4 == null) {
            var11.setCurrentItem((Item)var5, var2, var3, var1);
         } else {
            var12 = this.func_181036_a((Item)var5, var2, var4);
            var11.setInventorySlotContents(var11.currentItem, var12);
         }

         if (var1) {
            int var13 = thePlayer.inventoryContainer.inventorySlots.size() - 9 + var11.currentItem;
            playerController.sendSlotPacket(var11.getStackInSlot(var11.currentItem), var13);
         }
      }

   }

   public ServerData getCurrentServerData() {
      return this.currentServerData;
   }

   public MinecraftSessionService getSessionService() {
      return this.sessionService;
   }

   public SkinManager getSkinManager() {
      return this.skinManager;
   }

   public void loadWorld(WorldClient var1, String var2) {
      if (var1 == null) {
         NetHandlerPlayClient var3 = this.getNetHandler();
         if (var3 != null) {
            var3.cleanup();
         }

         if (this.theIntegratedServer != null && this.theIntegratedServer.isAnvilFileSet()) {
            this.theIntegratedServer.initiateShutdown();
            this.theIntegratedServer.setStaticInstance();
         }

         this.theIntegratedServer = null;
         this.guiAchievement.clearAchievements();
         this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
      }

      this.renderViewEntity = null;
      this.myNetworkManager = null;
      if (this.loadingScreen != null) {
         this.loadingScreen.resetProgressAndMessage(var2);
         this.loadingScreen.displayLoadingString("");
      }

      if (var1 == null && theWorld != null) {
         this.mcResourcePackRepository.func_148529_f();
         this.ingameGUI.func_181029_i();
         this.setServerData((ServerData)null);
         this.integratedServerIsRunning = false;
      }

      this.mcSoundHandler.stopSounds();
      theWorld = var1;
      if (var1 != null) {
         if (this.renderGlobal != null) {
            this.renderGlobal.setWorldAndLoadRenderers(var1);
         }

         if (this.effectRenderer != null) {
            this.effectRenderer.clearEffects(var1);
         }

         if (thePlayer == null) {
            thePlayer = playerController.func_178892_a(var1, new StatFileWriter());
            playerController.flipPlayer(thePlayer);
         }

         thePlayer.preparePlayerToSpawn();
         var1.spawnEntityInWorld(thePlayer);
         thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
         playerController.setPlayerCapabilities(thePlayer);
         this.renderViewEntity = thePlayer;
      } else {
         this.saveLoader.flushCache();
         thePlayer = null;
      }

      System.gc();
      this.systemTime = 0L;
   }

   private void rightClickMouse() {
      if (!playerController.func_181040_m()) {
         this.rightClickDelayTimer = 4;
         boolean var1 = true;
         ItemStack var2 = thePlayer.inventory.getCurrentItem();
         if (this.objectMouseOver == null) {
            logger.warn("Null returned as 'hitResult', this shouldn't happen!");
         } else {
            switch($SWITCH_TABLE$net$minecraft$util$MovingObjectPosition$MovingObjectType()[this.objectMouseOver.typeOfHit.ordinal()]) {
            case 2:
               BlockPos var3 = this.objectMouseOver.getBlockPos();
               if (theWorld.getBlockState(var3).getBlock().getMaterial() == Material.air) {
                  break;
               }

               int var4 = var2 != null ? var2.stackSize : 0;
               if (playerController.onPlayerRightClick(thePlayer, theWorld, var2, var3, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec)) {
                  var1 = false;
                  thePlayer.swingItem();
               }

               if (var2 == null) {
                  return;
               }

               if (var2.stackSize == 0) {
                  thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
               } else if (var2.stackSize != var4 || playerController.isInCreativeMode()) {
                  this.entityRenderer.itemRenderer.resetEquippedProgress();
               }
               break;
            case 3:
               if (playerController.func_178894_a(thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver)) {
                  var1 = false;
               } else if (playerController.interactWithEntitySendPacket(thePlayer, this.objectMouseOver.entityHit)) {
                  var1 = false;
               }
            }
         }

         if (var1) {
            ItemStack var5 = thePlayer.inventory.getCurrentItem();
            if (var5 != null && playerController.sendUseItem(thePlayer, theWorld, var5)) {
               this.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
         }
      }

   }

   private void setWindowIcon() {
      Util.EnumOS var1 = Util.getOSType();
      if (var1 != Util.EnumOS.OSX) {
         InputStream var2 = null;
         InputStream var3 = null;

         try {
            var2 = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
            var3 = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
            if (var2 != null && var3 != null) {
               Display.setIcon(new ByteBuffer[]{this.readImageToBuffer(var2), this.readImageToBuffer(var3)});
            }
         } catch (IOException var6) {
            logger.error((String)"Couldn't set icon", (Throwable)var6);
            IOUtils.closeQuietly(var2);
            IOUtils.closeQuietly(var3);
            return;
         }

         IOUtils.closeQuietly(var2);
         IOUtils.closeQuietly(var3);
      }

   }

   public static boolean isFancyGraphicsEnabled() {
      return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
   }

   public void displayCrashReport(CrashReport var1) {
      File var2 = new File(getMinecraft().mcDataDir, "crash-reports");
      File var3 = new File(var2, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
      Bootstrap.printToSYSOUT(var1.getCompleteReport());
      if (var1.getFile() != null) {
         Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + var1.getFile());
         System.exit(-1);
      } else if (var1.saveToFile(var3)) {
         Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + var3.getAbsolutePath());
         System.exit(-1);
      } else {
         Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
         System.exit(-2);
      }

   }

   public LanguageManager getLanguageManager() {
      return this.mcLanguageManager;
   }

   private void displayDebugInfo(long var1) {
      if (this.mcProfiler.profilingEnabled) {
         List var3 = this.mcProfiler.getProfilingData(this.debugProfilerName);
         Profiler.Result var4 = (Profiler.Result)var3.remove(0);
         GlStateManager.clear(256);
         GlStateManager.matrixMode(5889);
         GlStateManager.enableColorMaterial();
         GlStateManager.loadIdentity();
         GlStateManager.ortho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -2000.0F);
         GL11.glLineWidth(1.0F);
         GlStateManager.disableTexture2D();
         Tessellator var5 = Tessellator.getInstance();
         WorldRenderer var6 = var5.getWorldRenderer();
         short var7 = 160;
         int var8 = this.displayWidth - var7 - 10;
         int var9 = this.displayHeight - var7 * 2;
         GlStateManager.enableBlend();
         var6.begin(7, DefaultVertexFormats.POSITION_COLOR);
         var6.pos((double)((float)var8 - (float)var7 * 1.1F), (double)((float)var9 - (float)var7 * 0.6F - 16.0F), 0.0D).color(200, 0, 0, 0).endVertex();
         var6.pos((double)((float)var8 - (float)var7 * 1.1F), (double)(var9 + var7 * 2), 0.0D).color(200, 0, 0, 0).endVertex();
         var6.pos((double)((float)var8 + (float)var7 * 1.1F), (double)(var9 + var7 * 2), 0.0D).color(200, 0, 0, 0).endVertex();
         var6.pos((double)((float)var8 + (float)var7 * 1.1F), (double)((float)var9 - (float)var7 * 0.6F - 16.0F), 0.0D).color(200, 0, 0, 0).endVertex();
         var5.draw();
         GlStateManager.disableBlend();
         double var10 = 0.0D;

         int var14;
         int var15;
         for(int var12 = 0; var12 < var3.size(); ++var12) {
            Profiler.Result var13 = (Profiler.Result)var3.get(var12);
            var14 = MathHelper.floor_double(var13.field_76332_a / 4.0D) + 1;
            var6.begin(6, DefaultVertexFormats.POSITION_COLOR);
            var15 = var13.func_76329_a();
            int var16 = var15 >> 16 & 255;
            int var17 = var15 >> 8 & 255;
            int var18 = var15 & 255;
            var6.pos((double)var8, (double)var9, 0.0D).color(var16, var17, var18, 255).endVertex();

            int var19;
            float var20;
            float var21;
            float var22;
            for(var19 = var14; var19 >= 0; --var19) {
               var20 = (float)((var10 + var13.field_76332_a * (double)var19 / (double)var14) * 3.141592653589793D * 2.0D / 100.0D);
               var21 = MathHelper.sin(var20) * (float)var7;
               var22 = MathHelper.cos(var20) * (float)var7 * 0.5F;
               var6.pos((double)((float)var8 + var21), (double)((float)var9 - var22), 0.0D).color(var16, var17, var18, 255).endVertex();
            }

            var5.draw();
            var6.begin(5, DefaultVertexFormats.POSITION_COLOR);

            for(var19 = var14; var19 >= 0; --var19) {
               var20 = (float)((var10 + var13.field_76332_a * (double)var19 / (double)var14) * 3.141592653589793D * 2.0D / 100.0D);
               var21 = MathHelper.sin(var20) * (float)var7;
               var22 = MathHelper.cos(var20) * (float)var7 * 0.5F;
               var6.pos((double)((float)var8 + var21), (double)((float)var9 - var22), 0.0D).color(var16 >> 1, var17 >> 1, var18 >> 1, 255).endVertex();
               var6.pos((double)((float)var8 + var21), (double)((float)var9 - var22 + 10.0F), 0.0D).color(var16 >> 1, var17 >> 1, var18 >> 1, 255).endVertex();
            }

            var5.draw();
            var10 += var13.field_76332_a;
         }

         DecimalFormat var24 = new DecimalFormat("##0.00");
         GlStateManager.enableTexture2D();
         String var25 = "";
         if (!var4.field_76331_c.equals("unspecified")) {
            var25 = var25 + "[0] ";
         }

         if (var4.field_76331_c.length() == 0) {
            var25 = var25 + "ROOT ";
         } else {
            var25 = var25 + var4.field_76331_c + " ";
         }

         var14 = 16777215;
         fontRendererObj.drawStringWithShadow(var25, (float)(var8 - var7), (float)(var9 - var7 / 2 - 16), var14);
         fontRendererObj.drawStringWithShadow(var25 = var24.format(var4.field_76330_b) + "%", (float)(var8 + var7 - fontRendererObj.getStringWidth(var25)), (float)(var9 - var7 / 2 - 16), var14);

         for(var15 = 0; var15 < var3.size(); ++var15) {
            Profiler.Result var26 = (Profiler.Result)var3.get(var15);
            String var27 = "";
            if (var26.field_76331_c.equals("unspecified")) {
               var27 = var27 + "[?] ";
            } else {
               var27 = var27 + "[" + (var15 + 1) + "] ";
            }

            var27 = var27 + var26.field_76331_c;
            fontRendererObj.drawStringWithShadow(var27, (float)(var8 - var7), (float)(var9 + var7 / 2 + var15 * 8 + 20), var26.func_76329_a());
            fontRendererObj.drawStringWithShadow(var27 = var24.format(var26.field_76332_a) + "%", (float)(var8 + var7 - 50 - fontRendererObj.getStringWidth(var27)), (float)(var9 + var7 / 2 + var15 * 8 + 20), var26.func_76329_a());
            fontRendererObj.drawStringWithShadow(var27 = var24.format(var26.field_76330_b) + "%", (float)(var8 + var7 - fontRendererObj.getStringWidth(var27)), (float)(var9 + var7 / 2 + var15 * 8 + 20), var26.func_76329_a());
         }
      }

   }

   public ResourcePackRepository getResourcePackRepository() {
      return this.mcResourcePackRepository;
   }

   private void drawSplashScreen(TextureManager var1) throws LWJGLException {
      ScaledResolution var2 = new ScaledResolution(this);
      int var3 = var2.getScaleFactor();
      Framebuffer var4 = new Framebuffer(var2.getScaledWidth() * var3, ScaledResolution.getScaledHeight() * var3, true);
      var4.bindFramebuffer(false);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0D, (double)var2.getScaledWidth(), (double)ScaledResolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      GlStateManager.disableDepth();
      GlStateManager.enableTexture2D();
      InputStream var5 = null;

      label15: {
         try {
            var5 = this.mcDefaultResourcePack.getInputStream(locationMojangPng);
            this.mojangLogo = var1.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(var5)));
            var1.bindTexture(this.mojangLogo);
         } catch (IOException var10) {
            logger.error((String)("Unable to load logo: " + locationMojangPng), (Throwable)var10);
            IOUtils.closeQuietly(var5);
            break label15;
         }

         IOUtils.closeQuietly(var5);
      }

      Tessellator var6 = Tessellator.getInstance();
      WorldRenderer var7 = var6.getWorldRenderer();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      var7.pos(0.0D, (double)this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
      var7.pos((double)this.displayWidth, (double)this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
      var7.pos((double)this.displayWidth, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
      var7.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
      var6.draw();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      short var8 = 256;
      short var9 = 256;
      this.func_181536_a((var2.getScaledWidth() - var8) / 2, (ScaledResolution.getScaledHeight() - var9) / 2, 0, 0, var8, var9, 255, 255, 255, 255);
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      var4.unbindFramebuffer();
      var4.framebufferRender(var2.getScaledWidth() * var3, ScaledResolution.getScaledHeight() * var3);
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      this.updateDisplay();
   }

   public void dispatchKeypresses() {
      int var1 = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
      if (var1 != 0 && !Keyboard.isRepeatEvent() && (!(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= getSystemTime() - 20L)) {
         if (Keyboard.getEventKeyState()) {
            if (var1 == this.gameSettings.keyBindStreamStartStop.getKeyCode()) {
               if (this.getTwitchStream().isBroadcasting()) {
                  this.getTwitchStream().stopBroadcasting();
               } else if (this.getTwitchStream().isReadyToBroadcast()) {
                  this.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback(this) {
                     final Minecraft this$0;

                     {
                        this.this$0 = var1;
                     }

                     public void confirmClicked(boolean var1, int var2) {
                        if (var1) {
                           this.this$0.getTwitchStream().func_152930_t();
                        }

                        this.this$0.displayGuiScreen((GuiScreen)null);
                     }
                  }, I18n.format("stream.confirm_start"), "", 0));
               } else if (this.getTwitchStream().func_152928_D() && this.getTwitchStream().func_152936_l()) {
                  if (theWorld != null) {
                     this.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
                  }
               } else {
                  GuiStreamUnavailable.func_152321_a(this.currentScreen);
               }
            } else if (var1 == this.gameSettings.keyBindStreamPauseUnpause.getKeyCode()) {
               if (this.getTwitchStream().isBroadcasting()) {
                  if (this.getTwitchStream().isPaused()) {
                     this.getTwitchStream().unpause();
                  } else {
                     this.getTwitchStream().pause();
                  }
               }
            } else if (var1 == this.gameSettings.keyBindStreamCommercials.getKeyCode()) {
               if (this.getTwitchStream().isBroadcasting()) {
                  this.getTwitchStream().requestCommercial();
               }
            } else if (var1 == this.gameSettings.keyBindStreamToggleMic.getKeyCode()) {
               this.stream.muteMicrophone(true);
            } else if (var1 == this.gameSettings.keyBindFullscreen.getKeyCode()) {
               this.toggleFullscreen();
            } else if (var1 == this.gameSettings.keyBindScreenshot.getKeyCode()) {
               this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc));
            }
         } else if (var1 == this.gameSettings.keyBindStreamToggleMic.getKeyCode()) {
            this.stream.muteMicrophone(false);
         }
      }

   }

   private void startGame() throws LWJGLException, IOException, NoSuchAlgorithmException {
      this.gameSettings = new GameSettings(this, this.mcDataDir);
      this.defaultResourcePacks.add(this.mcDefaultResourcePack);
      this.startTimerHackThread();
      if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
         this.displayWidth = this.gameSettings.overrideWidth;
         this.displayHeight = this.gameSettings.overrideHeight;
      }

      logger.info("LWJGL Version: " + Sys.getVersion());
      this.setWindowIcon();
      this.setInitialDisplayMode();
      this.createDisplay();
      OpenGlHelper.initializeTextures();
      this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true);
      this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
      this.registerMetadataSerializers();
      this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings);
      this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
      this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
      this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
      this.refreshResources();
      this.renderEngine = new TextureManager(this.mcResourceManager);
      this.mcResourceManager.registerReloadListener(this.renderEngine);
      this.drawSplashScreen(this.renderEngine);
      this.initStream();
      this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
      this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
      this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
      this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
      this.mcMusicTicker = new MusicTicker(this);
      fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
      this.clientFont = GlyphPageFontRenderer.create("0", 19, true, true, true);
      if (this.gameSettings.language != null) {
         fontRendererObj.setUnicodeFlag(this.isUnicode());
         fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
      }

      this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
      this.mcResourceManager.registerReloadListener(fontRendererObj);
      this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
      this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
      this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
      AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat(this) {
         final Minecraft this$0;

         {
            this.this$0 = var1;
         }

         public String formatString(String var1) {
            try {
               return String.format(var1, GameSettings.getKeyDisplayString(this.this$0.gameSettings.keyBindInventory.getKeyCode()));
            } catch (Exception var3) {
               return "Error: " + var3.getLocalizedMessage();
            }
         }
      });
      this.mouseHelper = new MouseHelper();
      this.checkGLError("Pre startup");
      GlStateManager.enableTexture2D();
      GlStateManager.shadeModel(7425);
      GlStateManager.clearDepth(1.0D);
      GlStateManager.enableDepth();
      GlStateManager.depthFunc(515);
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.cullFace(1029);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.matrixMode(5888);
      this.checkGLError("Startup");
      this.textureMapBlocks = new TextureMap("textures");
      this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
      this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
      this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
      this.modelManager = new ModelManager(this.textureMapBlocks);
      this.mcResourceManager.registerReloadListener(this.modelManager);
      this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
      this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
      this.itemRenderer = new ItemRenderer(this);
      this.mcResourceManager.registerReloadListener(this.renderItem);
      this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
      this.mcResourceManager.registerReloadListener(this.entityRenderer);
      this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
      this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
      this.renderGlobal = new RenderGlobal(this);
      this.mcResourceManager.registerReloadListener(this.renderGlobal);
      this.guiAchievement = new GuiAchievement(this);
      GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
      this.effectRenderer = new EffectRenderer(theWorld, this.renderEngine);
      this.checkGLError("Post startup");
      this.ingameGUI = new GuiIngame(this);
      if (this.serverName != null) {
         this.displayGuiScreen(new GuiConnecting(new zaadfcx(), this, this.serverName, this.serverPort));
      } else {
         if (zaadfcx.isAllowedToRun()) {
            getMinecraft().displayGuiScreen(new GuiMainMenu());
            return;
         }

         this.displayGuiScreen(new zaadfcx());
      }

      this.renderEngine.deleteTexture(this.mojangLogo);
      this.mojangLogo = null;
      this.loadingScreen = new LoadingScreenRenderer(this);
      if (this.gameSettings.fullScreen && !this.fullscreen) {
         this.toggleFullscreen();
      }

      try {
         Display.setVSyncEnabled(this.gameSettings.enableVsync);
      } catch (OpenGLException var2) {
         this.gameSettings.enableVsync = false;
         this.gameSettings.saveOptions();
      }

      this.renderGlobal.makeEntityOutlineShader();
   }

   public boolean isGamePaused() {
      return this.isGamePaused;
   }

   public static boolean isAmbientOcclusionEnabled() {
      return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion != 0;
   }

   public boolean func_181540_al() {
      return this.field_181541_X;
   }

   private void checkGLError(String var1) {
      if (this.enableGLErrorChecking) {
         int var2 = GL11.glGetError();
         if (var2 != 0) {
            String var3 = GLU.gluErrorString(var2);
            logger.error("########## GL ERROR ##########");
            logger.error("@ " + var1);
            logger.error(var2 + ": " + var3);
         }
      }

   }

   public MusicTicker func_181535_r() {
      return this.mcMusicTicker;
   }

   public void setIngameNotInFocus() {
      if (this.inGameHasFocus) {
         KeyBinding.unPressAllKeys();
         this.inGameHasFocus = false;
         this.mouseHelper.ungrabMouseCursor();
      }

   }

   public static int getGLMaximumTextureSize() {
      for(int var0 = 16384; var0 > 0; var0 >>= 1) {
         GL11.glTexImage2D(32868, 0, 6408, var0, var0, 0, 6408, 5121, (ByteBuffer)null);
         int var1 = GL11.glGetTexLevelParameteri(32868, 0, 4096);
         if (var1 != 0) {
            return var0;
         }
      }

      return -1;
   }

   private void registerMetadataSerializers() {
      this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
   }

   public PlayerUsageSnooper getPlayerUsageSnooper() {
      return this.usageSnooper;
   }

   public void runTick() throws IOException {
      TickEvent var1 = new TickEvent();
      var1.call();
      if (this.rightClickDelayTimer > 0) {
         --this.rightClickDelayTimer;
      }

      this.mcProfiler.startSection("gui");
      if (!this.isGamePaused) {
         this.ingameGUI.updateTick();
      }

      this.mcProfiler.endSection();
      this.entityRenderer.getMouseOver(1.0F);
      this.mcProfiler.startSection("gameMode");
      if (!this.isGamePaused && theWorld != null) {
         playerController.updateController();
      }

      this.mcProfiler.endStartSection("textures");
      if (!this.isGamePaused) {
         this.renderEngine.tick();
      }

      if (this.currentScreen == null && thePlayer != null) {
         if (thePlayer.getHealth() <= 0.0F) {
            this.displayGuiScreen((GuiScreen)null);
         } else if (thePlayer.isPlayerSleeping() && theWorld != null) {
            this.displayGuiScreen(new GuiSleepMP());
         }
      } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !thePlayer.isPlayerSleeping()) {
         this.displayGuiScreen((GuiScreen)null);
      }

      if (this.currentScreen != null) {
         this.leftClickCounter = 10000;
      }

      CrashReport var3;
      CrashReportCategory var4;
      if (this.currentScreen != null) {
         try {
            this.currentScreen.handleInput();
         } catch (Throwable var8) {
            var3 = CrashReport.makeCrashReport(var8, "Updating screen events");
            var4 = var3.makeCategory("Affected screen");
            var4.addCrashSectionCallable("Screen name", new Callable(this) {
               final Minecraft this$0;

               public Object call() throws Exception {
                  return this.call();
               }

               public String call() throws Exception {
                  return this.this$0.currentScreen.getClass().getCanonicalName();
               }

               {
                  this.this$0 = var1;
               }
            });
            throw new ReportedException(var3);
         }

         if (this.currentScreen != null) {
            try {
               this.currentScreen.updateScreen();
            } catch (Throwable var7) {
               var3 = CrashReport.makeCrashReport(var7, "Ticking screen");
               var4 = var3.makeCategory("Affected screen");
               var4.addCrashSectionCallable("Screen name", new Callable(this) {
                  final Minecraft this$0;

                  public String call() throws Exception {
                     return this.this$0.currentScreen.getClass().getCanonicalName();
                  }

                  public Object call() throws Exception {
                     return this.call();
                  }

                  {
                     this.this$0 = var1;
                  }
               });
               throw new ReportedException(var3);
            }
         }
      }

      if (this.currentScreen == null || this.currentScreen.allowUserInput) {
         this.mcProfiler.endStartSection("mouse");

         int var2;
         while(Mouse.next()) {
            var2 = Mouse.getEventButton();
            KeyBinding.setKeyBindState(var2 - 100, Mouse.getEventButtonState());
            if (Mouse.getEventButtonState()) {
               if (thePlayer.isSpectator() && var2 == 2) {
                  this.ingameGUI.getSpectatorGui().func_175261_b();
               } else {
                  KeyBinding.onTick(var2 - 100);
               }
            }

            long var11 = getSystemTime() - this.systemTime;
            if (var11 <= 200L) {
               int var5 = Mouse.getEventDWheel();
               if (var5 != 0) {
                  if (thePlayer.isSpectator()) {
                     var5 = var5 < 0 ? -1 : 1;
                     if (this.ingameGUI.getSpectatorGui().func_175262_a()) {
                        this.ingameGUI.getSpectatorGui().func_175259_b(-var5);
                     } else {
                        float var6 = MathHelper.clamp_float(thePlayer.capabilities.getFlySpeed() + (float)var5 * 0.005F, 0.0F, 0.2F);
                        thePlayer.capabilities.setFlySpeed(var6);
                     }
                  } else {
                     thePlayer.inventory.changeCurrentItem(var5);
                  }
               }

               if (this.currentScreen == null) {
                  if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                     this.setIngameFocus();
                  }
               } else if (this.currentScreen != null) {
                  this.currentScreen.handleMouseInput();
               }
            }
         }

         if (this.leftClickCounter > 0) {
            --this.leftClickCounter;
         }

         this.mcProfiler.endStartSection("keyboard");

         label503:
         while(true) {
            do {
               do {
                  do {
                     if (!Keyboard.next()) {
                        for(var2 = 0; var2 < 9; ++var2) {
                           if (this.gameSettings.keyBindsHotbar[var2].isPressed()) {
                              if (thePlayer.isSpectator()) {
                                 this.ingameGUI.getSpectatorGui().func_175260_a(var2);
                              } else {
                                 thePlayer.inventory.currentItem = var2;
                              }
                           }
                        }

                        boolean var10 = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

                        while(this.gameSettings.keyBindInventory.isPressed()) {
                           if (playerController.isRidingHorse()) {
                              thePlayer.sendHorseInventory();
                           } else {
                              this.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                              this.displayGuiScreen(new GuiInventory(thePlayer));
                           }
                        }

                        while(this.gameSettings.keyBindDrop.isPressed()) {
                           if (!thePlayer.isSpectator()) {
                              thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                           }
                        }

                        while(this.gameSettings.keyBindChat.isPressed() && var10) {
                           this.displayGuiScreen(new GuiChat());
                        }

                        if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && var10) {
                           this.displayGuiScreen(new GuiChat("/"));
                        }

                        if (thePlayer.isUsingItem()) {
                           if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
                              playerController.onStoppedUsingItem(thePlayer);
                           }

                           while(this.gameSettings.keyBindAttack.isPressed()) {
                           }

                           while(this.gameSettings.keyBindUseItem.isPressed()) {
                           }

                           while(this.gameSettings.keyBindPickBlock.isPressed()) {
                           }
                        } else {
                           while(this.gameSettings.keyBindAttack.isPressed()) {
                              this.clickMouse();
                           }

                           while(this.gameSettings.keyBindUseItem.isPressed()) {
                              this.rightClickMouse();
                           }

                           while(this.gameSettings.keyBindPickBlock.isPressed()) {
                              this.middleClickMouse();
                           }
                        }

                        if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !thePlayer.isUsingItem()) {
                           this.rightClickMouse();
                        }

                        this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
                        break label503;
                     }

                     var2 = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
                     KeyBinding.setKeyBindState(var2, Keyboard.getEventKeyState());
                     if (Keyboard.getEventKeyState()) {
                        KeyBinding.onTick(var2);
                     }

                     if (this.debugCrashKeyPressTime > 0L) {
                        if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
                           throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                        }

                        if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                           this.debugCrashKeyPressTime = -1L;
                        }
                     } else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                        this.debugCrashKeyPressTime = getSystemTime();
                     }

                     this.dispatchKeypresses();
                  } while(!Keyboard.getEventKeyState());

                  if (var2 == 62 && this.entityRenderer != null) {
                     this.entityRenderer.switchUseShader();
                  }

                  if (this.currentScreen != null) {
                     this.currentScreen.handleKeyboardInput();
                  } else {
                     KeyPressEvent var12 = new KeyPressEvent(var2);
                     var12.call();
                     if (var2 == 1) {
                        this.displayInGameMenu();
                     }

                     if (var2 == 32 && Keyboard.isKeyDown(61) && this.ingameGUI != null) {
                        this.ingameGUI.getChatGUI().clearChatMessages();
                     }

                     if (var2 == 31 && Keyboard.isKeyDown(61)) {
                        this.refreshResources();
                     }

                     if (var2 == 17 && Keyboard.isKeyDown(61)) {
                     }

                     if (var2 == 18 && Keyboard.isKeyDown(61)) {
                     }

                     if (var2 == 47 && Keyboard.isKeyDown(61)) {
                     }

                     if (var2 == 38 && Keyboard.isKeyDown(61)) {
                     }

                     if (var2 == 22 && Keyboard.isKeyDown(61)) {
                     }

                     if (var2 == 20 && Keyboard.isKeyDown(61)) {
                        this.refreshResources();
                     }

                     if (var2 == 33 && Keyboard.isKeyDown(61)) {
                        this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
                     }

                     if (var2 == 30 && Keyboard.isKeyDown(61)) {
                        this.renderGlobal.loadRenderers();
                     }

                     if (var2 == 35 && Keyboard.isKeyDown(61)) {
                        this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                        this.gameSettings.saveOptions();
                     }

                     if (var2 == 48 && Keyboard.isKeyDown(61)) {
                        this.renderManager.setDebugBoundingBox(!this.renderManager.isDebugBoundingBox());
                     }

                     if (var2 == 25 && Keyboard.isKeyDown(61)) {
                        this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                        this.gameSettings.saveOptions();
                     }

                     if (var2 == 59) {
                        this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                     }

                     if (var2 == 61) {
                        this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                        this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                        this.gameSettings.field_181657_aC = GuiScreen.isAltKeyDown();
                     }

                     if (this.gameSettings.keyBindTogglePerspective.isPressed()) {
                        ++this.gameSettings.thirdPersonView;
                        if (this.gameSettings.thirdPersonView > 2) {
                           this.gameSettings.thirdPersonView = 0;
                        }

                        if (this.gameSettings.thirdPersonView == 0) {
                           this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
                        } else if (this.gameSettings.thirdPersonView == 1) {
                           this.entityRenderer.loadEntityShader((Entity)null);
                        }

                        this.renderGlobal.setDisplayListEntitiesDirty();
                     }

                     if (this.gameSettings.keyBindSmoothCamera.isPressed()) {
                        this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                     }
                  }
               } while(!this.gameSettings.showDebugInfo);
            } while(!this.gameSettings.showDebugProfilerChart);

            if (var2 == 11) {
               this.updateDebugProfilerName(0);
            }

            for(int var13 = 0; var13 < 9; ++var13) {
               if (var2 == 2 + var13) {
                  this.updateDebugProfilerName(var13 + 1);
               }
            }
         }
      }

      if (theWorld != null) {
         if (thePlayer != null) {
            ++this.joinPlayerCounter;
            if (this.joinPlayerCounter == 30) {
               this.joinPlayerCounter = 0;
               theWorld.joinEntityInSurroundings(thePlayer);
            }
         }

         this.mcProfiler.endStartSection("gameRenderer");
         if (!this.isGamePaused) {
            this.entityRenderer.updateRenderer();
         }

         this.mcProfiler.endStartSection("levelRenderer");
         if (!this.isGamePaused) {
            this.renderGlobal.updateClouds();
         }

         this.mcProfiler.endStartSection("level");
         if (!this.isGamePaused) {
            if (theWorld.getLastLightningBolt() > 0) {
               theWorld.setLastLightningBolt(theWorld.getLastLightningBolt() - 1);
            }

            theWorld.updateEntities();
         }
      } else if (this.entityRenderer.isShaderActive()) {
         this.entityRenderer.func_181022_b();
      }

      if (!this.isGamePaused) {
         this.mcMusicTicker.update();
         this.mcSoundHandler.update();
      }

      if (theWorld != null) {
         if (!this.isGamePaused) {
            theWorld.setAllowedSpawnTypes(theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);

            try {
               theWorld.tick();
            } catch (Throwable var9) {
               var3 = CrashReport.makeCrashReport(var9, "Exception in world tick");
               if (theWorld == null) {
                  var4 = var3.makeCategory("Affected level");
                  var4.addCrashSection("Problem", "Level is null!");
               } else {
                  theWorld.addWorldInfoToCrashReport(var3);
               }

               throw new ReportedException(var3);
            }
         }

         this.mcProfiler.endStartSection("animateTick");
         if (!this.isGamePaused && theWorld != null) {
            theWorld.doVoidFogParticles(MathHelper.floor_double(thePlayer.posX), MathHelper.floor_double(thePlayer.posY), MathHelper.floor_double(thePlayer.posZ));
         }

         this.mcProfiler.endStartSection("particles");
         if (!this.isGamePaused) {
            this.effectRenderer.updateEffects();
         }
      } else if (this.myNetworkManager != null) {
         this.mcProfiler.endStartSection("pendingConnection");
         this.myNetworkManager.processReceivedPackets();
      }

      this.mcProfiler.endSection();
      this.systemTime = getSystemTime();
   }

   public FrameTimer func_181539_aj() {
      return this.field_181542_y;
   }

   public TextureMap getTextureMapBlocks() {
      return this.textureMapBlocks;
   }

   public boolean isJava64bit() {
      return this.jvm64bit;
   }

   public boolean isSnooperEnabled() {
      return this.gameSettings.snooperEnabled;
   }

   private void updateDisplayMode() throws LWJGLException {
      HashSet var1 = Sets.newHashSet();
      Collections.addAll(var1, Display.getAvailableDisplayModes());
      DisplayMode var2 = Display.getDesktopDisplayMode();
      if (!var1.contains(var2) && Util.getOSType() == Util.EnumOS.OSX) {
         Iterator var4 = macDisplayModes.iterator();

         label49:
         while(true) {
            while(true) {
               DisplayMode var3;
               boolean var5;
               do {
                  if (!var4.hasNext()) {
                     break label49;
                  }

                  var3 = (DisplayMode)var4.next();
                  var5 = true;
                  Iterator var7 = var1.iterator();

                  while(var7.hasNext()) {
                     DisplayMode var6 = (DisplayMode)var7.next();
                     if (var6.getBitsPerPixel() == 32 && var6.getWidth() == var3.getWidth() && var6.getHeight() == var3.getHeight()) {
                        var5 = false;
                        break;
                     }
                  }
               } while(var5);

               Iterator var8 = var1.iterator();

               while(var8.hasNext()) {
                  DisplayMode var9 = (DisplayMode)var8.next();
                  if (var9.getBitsPerPixel() == 32 && var9.getWidth() == var3.getWidth() / 2 && var9.getHeight() == var3.getHeight() / 2) {
                     var2 = var9;
                     break;
                  }
               }
            }
         }
      }

      Display.setDisplayMode(var2);
      this.displayWidth = var2.getWidth();
      this.displayHeight = var2.getHeight();
   }

   protected void checkWindowResize() {
      if (!this.fullscreen && Display.wasResized()) {
         int var1 = this.displayWidth;
         int var2 = this.displayHeight;
         this.displayWidth = Display.getWidth();
         this.displayHeight = Display.getHeight();
         if (this.displayWidth != var1 || this.displayHeight != var2) {
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }

            this.resize(this.displayWidth, this.displayHeight);
         }
      }

   }

   private void initStream() {
      try {
         this.stream = new TwitchStream(this, (Property)Iterables.getFirst(this.twitchDetails.get("twitch_access_token"), (Object)null));
      } catch (Throwable var3) {
         this.stream = new NullStream(var3);
         logger.error("Couldn't initialize twitch stream");
      }

   }

   public Session getSession() {
      return this.session;
   }

   public BlockRendererDispatcher getBlockRendererDispatcher() {
      return this.blockRenderDispatcher;
   }

   public void crashed(CrashReport var1) {
      this.hasCrashed = true;
      this.crashReporter = var1;
   }

   public static int getDebugFPS() {
      return debugFPS;
   }

   public Minecraft(GameConfiguration var1) {
      theMinecraft = this;
      this.mcDataDir = var1.folderInfo.mcDataDir;
      this.fileAssets = var1.folderInfo.assetsDir;
      this.fileResourcepacks = var1.folderInfo.resourcePacksDir;
      this.launchedVersion = var1.gameInfo.version;
      this.twitchDetails = var1.userInfo.userProperties;
      this.field_181038_N = var1.userInfo.field_181172_c;
      this.mcDefaultResourcePack = new DefaultResourcePack((new ResourceIndex(var1.folderInfo.assetsDir, var1.folderInfo.assetIndex)).getResourceMap());
      this.proxy = var1.userInfo.proxy == null ? Proxy.NO_PROXY : var1.userInfo.proxy;
      this.sessionService = (new YggdrasilAuthenticationService(var1.userInfo.proxy, UUID.randomUUID().toString())).createMinecraftSessionService();
      this.session = var1.userInfo.session;
      logger.info("Setting user: " + this.session.getUsername());
      logger.info("(Session ID is " + this.session.getSessionID() + ")");
      this.isDemo = var1.gameInfo.isDemo;
      this.displayWidth = var1.displayInfo.width > 0 ? var1.displayInfo.width : 1;
      this.displayHeight = var1.displayInfo.height > 0 ? var1.displayInfo.height : 1;
      this.tempDisplayWidth = var1.displayInfo.width;
      this.tempDisplayHeight = var1.displayInfo.height;
      this.fullscreen = var1.displayInfo.fullscreen;
      this.jvm64bit = isJvm64bit();
      this.theIntegratedServer = new IntegratedServer(this);
      if (var1.serverInfo.serverName != null) {
         this.serverName = var1.serverInfo.serverName;
         this.serverPort = var1.serverInfo.serverPort;
      }

      ImageIO.setUseCache(false);
      Bootstrap.register();
   }

   private ByteBuffer readImageToBuffer(InputStream var1) throws IOException {
      BufferedImage var2 = ImageIO.read(var1);
      int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), (int[])null, 0, var2.getWidth());
      ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);
      int[] var8 = var3;
      int var7 = var3.length;

      for(int var6 = 0; var6 < var7; ++var6) {
         int var5 = var8[var6];
         var4.putInt(var5 << 8 | var5 >> 24 & 255);
      }

      var4.flip();
      return var4;
   }

   public static boolean isGuiEnabled() {
      return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
   }

   public static Map getSessionInfo() {
      HashMap var0 = Maps.newHashMap();
      var0.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
      var0.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
      var0.put("X-Minecraft-Version", "1.8.8");
      return var0;
   }

   public static Minecraft getMinecraft() {
      return theMinecraft;
   }

   public ListenableFuture addScheduledTask(Callable param1) {
      // $FF: Couldn't be decompiled
   }

   public void displayInGameMenu() {
      if (this.currentScreen == null) {
         this.displayGuiScreen(new GuiIngameMenu());
         if (this != false && !this.theIntegratedServer.getPublic()) {
            this.mcSoundHandler.pauseSounds();
         }
      }

   }

   public static long getSystemTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public void refreshResources() {
      ArrayList var1 = Lists.newArrayList((Iterable)this.defaultResourcePacks);
      Iterator var3 = this.mcResourcePackRepository.getRepositoryEntries().iterator();

      while(var3.hasNext()) {
         ResourcePackRepository.Entry var2 = (ResourcePackRepository.Entry)var3.next();
         var1.add(var2.getResourcePack());
      }

      if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
         var1.add(this.mcResourcePackRepository.getResourcePackInstance());
      }

      try {
         this.mcResourceManager.reloadResources(var1);
      } catch (RuntimeException var4) {
         logger.info((String)"Caught error stitching, removing all assigned resourcepacks", (Throwable)var4);
         var1.clear();
         var1.addAll(this.defaultResourcePacks);
         this.mcResourcePackRepository.setRepositories(Collections.emptyList());
         this.mcResourceManager.reloadResources(var1);
         this.gameSettings.resourcePacks.clear();
         this.gameSettings.field_183018_l.clear();
         this.gameSettings.saveOptions();
      }

      this.mcLanguageManager.parseLanguageMetadata(var1);
      if (this.renderGlobal != null) {
         this.renderGlobal.loadRenderers();
      }

   }

   public void setDimensionAndSpawnPlayer(int var1) {
      theWorld.setInitialSpawnLocation();
      theWorld.removeAllEntities();
      int var2 = 0;
      String var3 = null;
      if (thePlayer != null) {
         var2 = thePlayer.getEntityId();
         theWorld.removeEntity(thePlayer);
         var3 = thePlayer.getClientBrand();
      }

      this.renderViewEntity = null;
      EntityPlayerSP var4 = thePlayer;
      thePlayer = playerController.func_178892_a(theWorld, thePlayer == null ? new StatFileWriter() : thePlayer.getStatFileWriter());
      thePlayer.getDataWatcher().updateWatchedObjectsFromList(var4.getDataWatcher().getAllWatched());
      thePlayer.dimension = var1;
      this.renderViewEntity = thePlayer;
      thePlayer.preparePlayerToSpawn();
      thePlayer.setClientBrand(var3);
      theWorld.spawnEntityInWorld(thePlayer);
      playerController.flipPlayer(thePlayer);
      thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
      thePlayer.setEntityId(var2);
      playerController.setPlayerCapabilities(thePlayer);
      thePlayer.setReducedDebug(var4.hasReducedDebug());
      if (this.currentScreen instanceof GuiGameOver) {
         this.displayGuiScreen((GuiScreen)null);
      }

   }

   public Entity getRenderViewEntity() {
      return this.renderViewEntity;
   }

   public void updateDisplay() {
      this.mcProfiler.startSection("display_update");
      Display.update();
      this.mcProfiler.endSection();
      this.checkWindowResize();
   }

   private void setInitialDisplayMode() throws LWJGLException {
      if (this.fullscreen) {
         Display.setFullscreen(true);
         DisplayMode var1 = Display.getDisplayMode();
         this.displayWidth = Math.max(1, var1.getWidth());
         this.displayHeight = Math.max(1, var1.getHeight());
      } else {
         Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
      }

   }

   public boolean isUnicode() {
      return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
   }

   private void updateDebugProfilerName(int var1) {
      List var2 = this.mcProfiler.getProfilingData(this.debugProfilerName);
      if (var2 != null && !var2.isEmpty()) {
         Profiler.Result var3 = (Profiler.Result)var2.remove(0);
         if (var1 == 0) {
            if (var3.field_76331_c.length() > 0) {
               int var4 = this.debugProfilerName.lastIndexOf(".");
               if (var4 >= 0) {
                  this.debugProfilerName = this.debugProfilerName.substring(0, var4);
               }
            }
         } else {
            --var1;
            if (var1 < var2.size() && !((Profiler.Result)var2.get(var1)).field_76331_c.equals("unspecified")) {
               if (this.debugProfilerName.length() > 0) {
                  this.debugProfilerName = this.debugProfilerName + ".";
               }

               this.debugProfilerName = this.debugProfilerName + ((Profiler.Result)var2.get(var1)).field_76331_c;
            }
         }
      }

   }

   private void sendClickBlockToController(boolean var1) {
      if (!var1) {
         this.leftClickCounter = 0;
      }

      if (this.leftClickCounter <= 0 && !thePlayer.isUsingItem()) {
         if (var1 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos var2 = this.objectMouseOver.getBlockPos();
            if (theWorld.getBlockState(var2).getBlock().getMaterial() != Material.air && playerController.onPlayerDamageBlock(var2, this.objectMouseOver.sideHit)) {
               this.effectRenderer.addBlockHitEffects(var2, this.objectMouseOver.sideHit);
               thePlayer.swingItem();
            }
         } else {
            playerController.resetBlockRemoving();
         }
      }

   }

   static int[] $SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EntityMinecart.EnumMinecartType.values().length];

         try {
            var0[EntityMinecart.EnumMinecartType.CHEST.ordinal()] = 2;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.COMMAND_BLOCK.ordinal()] = 7;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.FURNACE.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.HOPPER.ordinal()] = 6;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.RIDEABLE.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.SPAWNER.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.TNT.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType = var0;
         return var0;
      }
   }

   public ItemRenderer getItemRenderer() {
      return this.itemRenderer;
   }

   public ListenableFuture scheduleResourcesRefresh() {
      return this.addScheduledTask(new Runnable(this) {
         final Minecraft this$0;

         public void run() {
            this.this$0.refreshResources();
         }

         {
            this.this$0 = var1;
         }
      });
   }

   public Framebuffer getFramebuffer() {
      return this.framebufferMc;
   }

   private ItemStack func_181036_a(Item var1, int var2, TileEntity var3) {
      ItemStack var4 = new ItemStack(var1, 1, var2);
      NBTTagCompound var5 = new NBTTagCompound();
      var3.writeToNBT(var5);
      NBTTagCompound var6;
      if (var1 == Items.skull && var5.hasKey("Owner")) {
         var6 = var5.getCompoundTag("Owner");
         NBTTagCompound var8 = new NBTTagCompound();
         var8.setTag("SkullOwner", var6);
         var4.setTagCompound(var8);
         return var4;
      } else {
         var4.setTagInfo("BlockEntityTag", var5);
         var6 = new NBTTagCompound();
         NBTTagList var7 = new NBTTagList();
         var7.appendTag(new NBTTagString("(+NBT)"));
         var6.setTag("Lore", var7);
         var4.setTagInfo("display", var6);
         return var4;
      }
   }

   private void resize(int var1, int var2) {
      this.displayWidth = Math.max(1, var1);
      this.displayHeight = Math.max(1, var2);
      if (this.currentScreen != null) {
         ScaledResolution var3 = new ScaledResolution(this);
         this.currentScreen.onResize(this, var3.getScaledWidth(), ScaledResolution.getScaledHeight());
      }

      this.loadingScreen = new LoadingScreenRenderer(this);
      this.updateFramebufferSize();
   }

   public void setServerData(ServerData var1) {
      this.currentServerData = var1;
   }

   public IStream getTwitchStream() {
      return this.stream;
   }
}
