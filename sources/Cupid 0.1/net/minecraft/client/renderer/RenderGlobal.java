package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Matrix4f;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vector3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import optifine.ChunkUtils;
import optifine.CloudRenderer;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomSky;
import optifine.DynamicLights;
import optifine.Lagometer;
import optifine.RandomMobs;
import optifine.Reflector;
import optifine.RenderInfoLazy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;
import shadersmod.client.ShadowUtils;

public class RenderGlobal implements IWorldAccess, IResourceManagerReloadListener {
  private static final Logger logger = LogManager.getLogger();
  
  private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
  
  private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
  
  private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
  
  private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
  
  private static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
  
  public final Minecraft mc;
  
  private final TextureManager renderEngine;
  
  private final RenderManager renderManager;
  
  private WorldClient theWorld;
  
  private Set chunksToUpdate = Sets.newLinkedHashSet();
  
  private List renderInfos = Lists.newArrayListWithCapacity(69696);
  
  private final Set field_181024_n = Sets.newHashSet();
  
  private ViewFrustum viewFrustum;
  
  private int starGLCallList = -1;
  
  private int glSkyList = -1;
  
  private int glSkyList2 = -1;
  
  private VertexFormat vertexBufferFormat;
  
  private VertexBuffer starVBO;
  
  private VertexBuffer skyVBO;
  
  private VertexBuffer sky2VBO;
  
  private int cloudTickCounter;
  
  public final Map damagedBlocks = Maps.newHashMap();
  
  private final Map mapSoundPositions = Maps.newHashMap();
  
  private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
  
  private Framebuffer entityOutlineFramebuffer;
  
  private ShaderGroup entityOutlineShader;
  
  private double frustumUpdatePosX = Double.MIN_VALUE;
  
  private double frustumUpdatePosY = Double.MIN_VALUE;
  
  private double frustumUpdatePosZ = Double.MIN_VALUE;
  
  private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
  
  private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
  
  private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
  
  private double lastViewEntityX = Double.MIN_VALUE;
  
  private double lastViewEntityY = Double.MIN_VALUE;
  
  private double lastViewEntityZ = Double.MIN_VALUE;
  
  private double lastViewEntityPitch = Double.MIN_VALUE;
  
  private double lastViewEntityYaw = Double.MIN_VALUE;
  
  private final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
  
  private ChunkRenderContainer renderContainer;
  
  private int renderDistanceChunks = -1;
  
  private int renderEntitiesStartupCounter = 2;
  
  private int countEntitiesTotal;
  
  private int countEntitiesRendered;
  
  private int countEntitiesHidden;
  
  private boolean debugFixTerrainFrustum = false;
  
  private ClippingHelper debugFixedClippingHelper;
  
  private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
  
  private final Vector3d debugTerrainFrustumPosition = new Vector3d();
  
  private boolean vboEnabled = false;
  
  IRenderChunkFactory renderChunkFactory;
  
  private double prevRenderSortX;
  
  private double prevRenderSortY;
  
  private double prevRenderSortZ;
  
  public boolean displayListEntitiesDirty = true;
  
  private static final String __OBFID = "CL_00000954";
  
  private CloudRenderer cloudRenderer;
  
  public Entity renderedEntity;
  
  public Set chunksToResortTransparency = new LinkedHashSet();
  
  public Set chunksToUpdateForced = new LinkedHashSet();
  
  private Deque visibilityDeque = new ArrayDeque();
  
  private List renderInfosEntities = new ArrayList(1024);
  
  private List renderInfosTileEntities = new ArrayList(1024);
  
  private List renderInfosNormal = new ArrayList(1024);
  
  private List renderInfosEntitiesNormal = new ArrayList(1024);
  
  private List renderInfosTileEntitiesNormal = new ArrayList(1024);
  
  private List renderInfosShadow = new ArrayList(1024);
  
  private List renderInfosEntitiesShadow = new ArrayList(1024);
  
  private List renderInfosTileEntitiesShadow = new ArrayList(1024);
  
  private int renderDistance = 0;
  
  private int renderDistanceSq = 0;
  
  private static final Set SET_ALL_FACINGS = Collections.unmodifiableSet(new HashSet(Arrays.asList((Object[])EnumFacing.VALUES)));
  
  private int countTileEntitiesRendered;
  
  public RenderGlobal(Minecraft mcIn) {
    this.cloudRenderer = new CloudRenderer(mcIn);
    this.mc = mcIn;
    this.renderManager = mcIn.getRenderManager();
    this.renderEngine = mcIn.getTextureManager();
    this.renderEngine.bindTexture(locationForcefieldPng);
    GL11.glTexParameteri(3553, 10242, 10497);
    GL11.glTexParameteri(3553, 10243, 10497);
    GlStateManager.bindTexture(0);
    updateDestroyBlockIcons();
    this.vboEnabled = OpenGlHelper.useVbo();
    if (this.vboEnabled) {
      this.renderContainer = new VboRenderList();
      this.renderChunkFactory = (IRenderChunkFactory)new VboChunkFactory();
    } else {
      this.renderContainer = new RenderList();
      this.renderChunkFactory = (IRenderChunkFactory)new ListChunkFactory();
    } 
    this.vertexBufferFormat = new VertexFormat();
    this.vertexBufferFormat.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
    generateStars();
    generateSky();
    generateSky2();
  }
  
  public void onResourceManagerReload(IResourceManager resourceManager) {
    updateDestroyBlockIcons();
  }
  
  private void updateDestroyBlockIcons() {
    TextureMap texturemap = this.mc.getTextureMapBlocks();
    for (int i = 0; i < this.destroyBlockIcons.length; i++)
      this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i); 
  }
  
  public void makeEntityOutlineShader() {
    if (OpenGlHelper.shadersSupported) {
      if (ShaderLinkHelper.getStaticShaderLinkHelper() == null)
        ShaderLinkHelper.setNewStaticShaderLinkHelper(); 
      ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
      try {
        this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);
        this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
        this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
      } catch (IOException ioexception) {
        logger.warn("Failed to load shader: " + resourcelocation, ioexception);
        this.entityOutlineShader = null;
        this.entityOutlineFramebuffer = null;
      } catch (JsonSyntaxException jsonsyntaxexception) {
        logger.warn("Failed to load shader: " + resourcelocation, (Throwable)jsonsyntaxexception);
        this.entityOutlineShader = null;
        this.entityOutlineFramebuffer = null;
      } 
    } else {
      this.entityOutlineShader = null;
      this.entityOutlineFramebuffer = null;
    } 
  }
  
  public void renderEntityOutlineFramebuffer() {
    if (isRenderEntityOutlines()) {
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
      this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
      GlStateManager.disableBlend();
    } 
  }
  
  protected boolean isRenderEntityOutlines() {
    return (!Config.isFastRender() && !Config.isShaders() && !Config.isAntialiasing()) ? ((this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.thePlayer != null && this.mc.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown())) : false;
  }
  
  private void generateSky2() {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    if (this.sky2VBO != null)
      this.sky2VBO.deleteGlBuffers(); 
    if (this.glSkyList2 >= 0) {
      GLAllocation.deleteDisplayLists(this.glSkyList2);
      this.glSkyList2 = -1;
    } 
    if (this.vboEnabled) {
      this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
      renderSky(worldrenderer, -16.0F, true);
      worldrenderer.finishDrawing();
      worldrenderer.reset();
      this.sky2VBO.func_181722_a(worldrenderer.getByteBuffer());
    } else {
      this.glSkyList2 = GLAllocation.generateDisplayLists(1);
      GL11.glNewList(this.glSkyList2, 4864);
      renderSky(worldrenderer, -16.0F, true);
      tessellator.draw();
      GL11.glEndList();
    } 
  }
  
  private void generateSky() {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    if (this.skyVBO != null)
      this.skyVBO.deleteGlBuffers(); 
    if (this.glSkyList >= 0) {
      GLAllocation.deleteDisplayLists(this.glSkyList);
      this.glSkyList = -1;
    } 
    if (this.vboEnabled) {
      this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
      renderSky(worldrenderer, 16.0F, false);
      worldrenderer.finishDrawing();
      worldrenderer.reset();
      this.skyVBO.func_181722_a(worldrenderer.getByteBuffer());
    } else {
      this.glSkyList = GLAllocation.generateDisplayLists(1);
      GL11.glNewList(this.glSkyList, 4864);
      renderSky(worldrenderer, 16.0F, false);
      tessellator.draw();
      GL11.glEndList();
    } 
  }
  
  private void renderSky(WorldRenderer worldRendererIn, float p_174968_2_, boolean p_174968_3_) {
    boolean flag = true;
    boolean flag1 = true;
    worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
    for (int i = -384; i <= 384; i += 64) {
      for (int j = -384; j <= 384; j += 64) {
        float f = i;
        float f1 = (i + 64);
        if (p_174968_3_) {
          f1 = i;
          f = (i + 64);
        } 
        worldRendererIn.pos(f, p_174968_2_, j).endVertex();
        worldRendererIn.pos(f1, p_174968_2_, j).endVertex();
        worldRendererIn.pos(f1, p_174968_2_, (j + 64)).endVertex();
        worldRendererIn.pos(f, p_174968_2_, (j + 64)).endVertex();
      } 
    } 
  }
  
  private void generateStars() {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    if (this.starVBO != null)
      this.starVBO.deleteGlBuffers(); 
    if (this.starGLCallList >= 0) {
      GLAllocation.deleteDisplayLists(this.starGLCallList);
      this.starGLCallList = -1;
    } 
    if (this.vboEnabled) {
      this.starVBO = new VertexBuffer(this.vertexBufferFormat);
      renderStars(worldrenderer);
      worldrenderer.finishDrawing();
      worldrenderer.reset();
      this.starVBO.func_181722_a(worldrenderer.getByteBuffer());
    } else {
      this.starGLCallList = GLAllocation.generateDisplayLists(1);
      GlStateManager.pushMatrix();
      GL11.glNewList(this.starGLCallList, 4864);
      renderStars(worldrenderer);
      tessellator.draw();
      GL11.glEndList();
      GlStateManager.popMatrix();
    } 
  }
  
  private void renderStars(WorldRenderer worldRendererIn) {
    Random random = new Random(10842L);
    worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
    for (int i = 0; i < 1500; i++) {
      double d0 = (random.nextFloat() * 2.0F - 1.0F);
      double d1 = (random.nextFloat() * 2.0F - 1.0F);
      double d2 = (random.nextFloat() * 2.0F - 1.0F);
      double d3 = (0.15F + random.nextFloat() * 0.1F);
      double d4 = d0 * d0 + d1 * d1 + d2 * d2;
      if (d4 < 1.0D && d4 > 0.01D) {
        d4 = 1.0D / Math.sqrt(d4);
        d0 *= d4;
        d1 *= d4;
        d2 *= d4;
        double d5 = d0 * 100.0D;
        double d6 = d1 * 100.0D;
        double d7 = d2 * 100.0D;
        double d8 = Math.atan2(d0, d2);
        double d9 = Math.sin(d8);
        double d10 = Math.cos(d8);
        double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
        double d12 = Math.sin(d11);
        double d13 = Math.cos(d11);
        double d14 = random.nextDouble() * Math.PI * 2.0D;
        double d15 = Math.sin(d14);
        double d16 = Math.cos(d14);
        for (int j = 0; j < 4; j++) {
          double d17 = 0.0D;
          double d18 = ((j & 0x2) - 1) * d3;
          double d19 = ((j + 1 & 0x2) - 1) * d3;
          double d20 = 0.0D;
          double d21 = d18 * d16 - d19 * d15;
          double d22 = d19 * d16 + d18 * d15;
          double d23 = d21 * d12 + 0.0D * d13;
          double d24 = 0.0D * d12 - d21 * d13;
          double d25 = d24 * d9 - d22 * d10;
          double d26 = d22 * d9 + d24 * d10;
          worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
        } 
      } 
    } 
  }
  
  public void setWorldAndLoadRenderers(WorldClient worldClientIn) {
    if (this.theWorld != null)
      this.theWorld.removeWorldAccess(this); 
    this.frustumUpdatePosX = Double.MIN_VALUE;
    this.frustumUpdatePosY = Double.MIN_VALUE;
    this.frustumUpdatePosZ = Double.MIN_VALUE;
    this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
    this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
    this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
    this.renderManager.set((World)worldClientIn);
    this.theWorld = worldClientIn;
    if (Config.isDynamicLights())
      DynamicLights.clear(); 
    if (worldClientIn != null) {
      worldClientIn.addWorldAccess(this);
      loadRenderers();
    } 
  }
  
  public void loadRenderers() {
    if (this.theWorld != null) {
      this.displayListEntitiesDirty = true;
      Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
      Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
      BlockModelRenderer.updateAoLightValue();
      if (Config.isDynamicLights())
        DynamicLights.clear(); 
      this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
      this.renderDistance = this.renderDistanceChunks * 16;
      this.renderDistanceSq = this.renderDistance * this.renderDistance;
      boolean flag = this.vboEnabled;
      this.vboEnabled = OpenGlHelper.useVbo();
      if (flag && !this.vboEnabled) {
        this.renderContainer = new RenderList();
        this.renderChunkFactory = (IRenderChunkFactory)new ListChunkFactory();
      } else if (!flag && this.vboEnabled) {
        this.renderContainer = new VboRenderList();
        this.renderChunkFactory = (IRenderChunkFactory)new VboChunkFactory();
      } 
      if (flag != this.vboEnabled) {
        generateStars();
        generateSky();
        generateSky2();
      } 
      if (this.viewFrustum != null)
        this.viewFrustum.deleteGlResources(); 
      stopChunkUpdates();
      Set var5 = this.field_181024_n;
      synchronized (this.field_181024_n) {
        this.field_181024_n.clear();
      } 
      this.viewFrustum = new ViewFrustum((World)this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
      if (this.theWorld != null) {
        Entity entity = this.mc.getRenderViewEntity();
        if (entity != null)
          this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ); 
      } 
      this.renderEntitiesStartupCounter = 2;
    } 
  }
  
  protected void stopChunkUpdates() {
    this.chunksToUpdate.clear();
    this.renderDispatcher.stopChunkUpdates();
  }
  
  public void createBindEntityOutlineFbs(int p_72720_1_, int p_72720_2_) {
    if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null)
      this.entityOutlineShader.createBindFramebuffers(p_72720_1_, p_72720_2_); 
  }
  
  public void renderEntities(Entity renderViewEntity, ICamera camera, float partialTicks) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: getstatic optifine/Reflector.MinecraftForgeClient_getRenderPass : Loptifine/ReflectorMethod;
    //   6: invokevirtual exists : ()Z
    //   9: ifeq -> 24
    //   12: getstatic optifine/Reflector.MinecraftForgeClient_getRenderPass : Loptifine/ReflectorMethod;
    //   15: iconst_0
    //   16: anewarray java/lang/Object
    //   19: invokestatic callInt : (Loptifine/ReflectorMethod;[Ljava/lang/Object;)I
    //   22: istore #4
    //   24: aload_0
    //   25: getfield renderEntitiesStartupCounter : I
    //   28: ifle -> 50
    //   31: iload #4
    //   33: ifle -> 37
    //   36: return
    //   37: aload_0
    //   38: dup
    //   39: getfield renderEntitiesStartupCounter : I
    //   42: iconst_1
    //   43: isub
    //   44: putfield renderEntitiesStartupCounter : I
    //   47: goto -> 2237
    //   50: aload_1
    //   51: getfield prevPosX : D
    //   54: aload_1
    //   55: getfield posX : D
    //   58: aload_1
    //   59: getfield prevPosX : D
    //   62: dsub
    //   63: fload_3
    //   64: f2d
    //   65: dmul
    //   66: dadd
    //   67: dstore #5
    //   69: aload_1
    //   70: getfield prevPosY : D
    //   73: aload_1
    //   74: getfield posY : D
    //   77: aload_1
    //   78: getfield prevPosY : D
    //   81: dsub
    //   82: fload_3
    //   83: f2d
    //   84: dmul
    //   85: dadd
    //   86: dstore #7
    //   88: aload_1
    //   89: getfield prevPosZ : D
    //   92: aload_1
    //   93: getfield posZ : D
    //   96: aload_1
    //   97: getfield prevPosZ : D
    //   100: dsub
    //   101: fload_3
    //   102: f2d
    //   103: dmul
    //   104: dadd
    //   105: dstore #9
    //   107: aload_0
    //   108: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   111: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   114: ldc 'prepare'
    //   116: invokevirtual startSection : (Ljava/lang/String;)V
    //   119: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   122: aload_0
    //   123: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   126: aload_0
    //   127: getfield mc : Lnet/minecraft/client/Minecraft;
    //   130: invokevirtual getTextureManager : ()Lnet/minecraft/client/renderer/texture/TextureManager;
    //   133: aload_0
    //   134: getfield mc : Lnet/minecraft/client/Minecraft;
    //   137: getfield fontRendererObj : Lnet/minecraft/client/gui/FontRenderer;
    //   140: aload_0
    //   141: getfield mc : Lnet/minecraft/client/Minecraft;
    //   144: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   147: fload_3
    //   148: invokevirtual cacheActiveRenderInfo : (Lnet/minecraft/world/World;Lnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/entity/Entity;F)V
    //   151: aload_0
    //   152: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   155: aload_0
    //   156: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   159: aload_0
    //   160: getfield mc : Lnet/minecraft/client/Minecraft;
    //   163: getfield fontRendererObj : Lnet/minecraft/client/gui/FontRenderer;
    //   166: aload_0
    //   167: getfield mc : Lnet/minecraft/client/Minecraft;
    //   170: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   173: aload_0
    //   174: getfield mc : Lnet/minecraft/client/Minecraft;
    //   177: getfield pointedEntity : Lnet/minecraft/entity/Entity;
    //   180: aload_0
    //   181: getfield mc : Lnet/minecraft/client/Minecraft;
    //   184: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   187: fload_3
    //   188: invokevirtual cacheActiveRenderInfo : (Lnet/minecraft/world/World;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/client/settings/GameSettings;F)V
    //   191: iload #4
    //   193: ifne -> 216
    //   196: aload_0
    //   197: iconst_0
    //   198: putfield countEntitiesTotal : I
    //   201: aload_0
    //   202: iconst_0
    //   203: putfield countEntitiesRendered : I
    //   206: aload_0
    //   207: iconst_0
    //   208: putfield countEntitiesHidden : I
    //   211: aload_0
    //   212: iconst_0
    //   213: putfield countTileEntitiesRendered : I
    //   216: aload_0
    //   217: getfield mc : Lnet/minecraft/client/Minecraft;
    //   220: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   223: astore #11
    //   225: aload #11
    //   227: getfield lastTickPosX : D
    //   230: aload #11
    //   232: getfield posX : D
    //   235: aload #11
    //   237: getfield lastTickPosX : D
    //   240: dsub
    //   241: fload_3
    //   242: f2d
    //   243: dmul
    //   244: dadd
    //   245: dstore #12
    //   247: aload #11
    //   249: getfield lastTickPosY : D
    //   252: aload #11
    //   254: getfield posY : D
    //   257: aload #11
    //   259: getfield lastTickPosY : D
    //   262: dsub
    //   263: fload_3
    //   264: f2d
    //   265: dmul
    //   266: dadd
    //   267: dstore #14
    //   269: aload #11
    //   271: getfield lastTickPosZ : D
    //   274: aload #11
    //   276: getfield posZ : D
    //   279: aload #11
    //   281: getfield lastTickPosZ : D
    //   284: dsub
    //   285: fload_3
    //   286: f2d
    //   287: dmul
    //   288: dadd
    //   289: dstore #16
    //   291: dload #12
    //   293: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerX : D
    //   296: dload #14
    //   298: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerY : D
    //   301: dload #16
    //   303: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerZ : D
    //   306: aload_0
    //   307: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   310: dload #12
    //   312: dload #14
    //   314: dload #16
    //   316: invokevirtual setRenderPosition : (DDD)V
    //   319: aload_0
    //   320: getfield mc : Lnet/minecraft/client/Minecraft;
    //   323: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
    //   326: invokevirtual enableLightmap : ()V
    //   329: aload_0
    //   330: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   333: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   336: ldc 'global'
    //   338: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   341: aload_0
    //   342: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   345: invokevirtual getLoadedEntityList : ()Ljava/util/List;
    //   348: astore #18
    //   350: iload #4
    //   352: ifne -> 366
    //   355: aload_0
    //   356: aload #18
    //   358: invokeinterface size : ()I
    //   363: putfield countEntitiesTotal : I
    //   366: invokestatic isFogOff : ()Z
    //   369: ifeq -> 388
    //   372: aload_0
    //   373: getfield mc : Lnet/minecraft/client/Minecraft;
    //   376: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
    //   379: getfield fogStandard : Z
    //   382: ifeq -> 388
    //   385: invokestatic disableFog : ()V
    //   388: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   391: invokevirtual exists : ()Z
    //   394: istore #19
    //   396: getstatic optifine/Reflector.ForgeTileEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   399: invokevirtual exists : ()Z
    //   402: istore #20
    //   404: iconst_0
    //   405: istore #21
    //   407: iload #21
    //   409: aload_0
    //   410: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   413: getfield weatherEffects : Ljava/util/List;
    //   416: invokeinterface size : ()I
    //   421: if_icmpge -> 512
    //   424: aload_0
    //   425: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   428: getfield weatherEffects : Ljava/util/List;
    //   431: iload #21
    //   433: invokeinterface get : (I)Ljava/lang/Object;
    //   438: checkcast net/minecraft/entity/Entity
    //   441: astore #22
    //   443: iload #19
    //   445: ifeq -> 471
    //   448: aload #22
    //   450: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   453: iconst_1
    //   454: anewarray java/lang/Object
    //   457: dup
    //   458: iconst_0
    //   459: iload #4
    //   461: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   464: aastore
    //   465: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   468: ifeq -> 506
    //   471: aload_0
    //   472: dup
    //   473: getfield countEntitiesRendered : I
    //   476: iconst_1
    //   477: iadd
    //   478: putfield countEntitiesRendered : I
    //   481: aload #22
    //   483: dload #5
    //   485: dload #7
    //   487: dload #9
    //   489: invokevirtual isInRangeToRender3d : (DDD)Z
    //   492: ifeq -> 506
    //   495: aload_0
    //   496: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   499: aload #22
    //   501: fload_3
    //   502: invokevirtual renderEntitySimple : (Lnet/minecraft/entity/Entity;F)Z
    //   505: pop
    //   506: iinc #21, 1
    //   509: goto -> 407
    //   512: aload_0
    //   513: invokevirtual isRenderEntityOutlines : ()Z
    //   516: ifeq -> 840
    //   519: sipush #519
    //   522: invokestatic depthFunc : (I)V
    //   525: invokestatic disableFog : ()V
    //   528: aload_0
    //   529: getfield entityOutlineFramebuffer : Lnet/minecraft/client/shader/Framebuffer;
    //   532: invokevirtual framebufferClear : ()V
    //   535: aload_0
    //   536: getfield entityOutlineFramebuffer : Lnet/minecraft/client/shader/Framebuffer;
    //   539: iconst_0
    //   540: invokevirtual bindFramebuffer : (Z)V
    //   543: aload_0
    //   544: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   547: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   550: ldc_w 'entityOutlines'
    //   553: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   556: invokestatic disableStandardItemLighting : ()V
    //   559: aload_0
    //   560: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   563: iconst_1
    //   564: invokevirtual setRenderOutlines : (Z)V
    //   567: iconst_0
    //   568: istore #21
    //   570: iload #21
    //   572: aload #18
    //   574: invokeinterface size : ()I
    //   579: if_icmpge -> 778
    //   582: aload #18
    //   584: iload #21
    //   586: invokeinterface get : (I)Ljava/lang/Object;
    //   591: checkcast net/minecraft/entity/Entity
    //   594: astore #22
    //   596: iload #19
    //   598: ifeq -> 624
    //   601: aload #22
    //   603: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   606: iconst_1
    //   607: anewarray java/lang/Object
    //   610: dup
    //   611: iconst_0
    //   612: iload #4
    //   614: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   617: aastore
    //   618: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   621: ifeq -> 772
    //   624: aload_0
    //   625: getfield mc : Lnet/minecraft/client/Minecraft;
    //   628: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   631: instanceof net/minecraft/entity/EntityLivingBase
    //   634: ifeq -> 657
    //   637: aload_0
    //   638: getfield mc : Lnet/minecraft/client/Minecraft;
    //   641: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   644: checkcast net/minecraft/entity/EntityLivingBase
    //   647: invokevirtual isPlayerSleeping : ()Z
    //   650: ifeq -> 657
    //   653: iconst_1
    //   654: goto -> 658
    //   657: iconst_0
    //   658: istore #23
    //   660: aload #22
    //   662: dload #5
    //   664: dload #7
    //   666: dload #9
    //   668: invokevirtual isInRangeToRender3d : (DDD)Z
    //   671: ifeq -> 723
    //   674: aload #22
    //   676: getfield ignoreFrustumCheck : Z
    //   679: ifne -> 711
    //   682: aload_2
    //   683: aload #22
    //   685: invokevirtual getEntityBoundingBox : ()Lnet/minecraft/util/AxisAlignedBB;
    //   688: invokeinterface isBoundingBoxInFrustum : (Lnet/minecraft/util/AxisAlignedBB;)Z
    //   693: ifne -> 711
    //   696: aload #22
    //   698: getfield riddenByEntity : Lnet/minecraft/entity/Entity;
    //   701: aload_0
    //   702: getfield mc : Lnet/minecraft/client/Minecraft;
    //   705: getfield thePlayer : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   708: if_acmpne -> 723
    //   711: aload #22
    //   713: instanceof net/minecraft/entity/player/EntityPlayer
    //   716: ifeq -> 723
    //   719: iconst_1
    //   720: goto -> 724
    //   723: iconst_0
    //   724: istore #24
    //   726: aload #22
    //   728: aload_0
    //   729: getfield mc : Lnet/minecraft/client/Minecraft;
    //   732: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   735: if_acmpne -> 756
    //   738: aload_0
    //   739: getfield mc : Lnet/minecraft/client/Minecraft;
    //   742: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   745: getfield thirdPersonView : I
    //   748: ifne -> 756
    //   751: iload #23
    //   753: ifeq -> 772
    //   756: iload #24
    //   758: ifeq -> 772
    //   761: aload_0
    //   762: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   765: aload #22
    //   767: fload_3
    //   768: invokevirtual renderEntitySimple : (Lnet/minecraft/entity/Entity;F)Z
    //   771: pop
    //   772: iinc #21, 1
    //   775: goto -> 570
    //   778: aload_0
    //   779: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   782: iconst_0
    //   783: invokevirtual setRenderOutlines : (Z)V
    //   786: invokestatic enableStandardItemLighting : ()V
    //   789: iconst_0
    //   790: invokestatic depthMask : (Z)V
    //   793: aload_0
    //   794: getfield entityOutlineShader : Lnet/minecraft/client/shader/ShaderGroup;
    //   797: fload_3
    //   798: invokevirtual loadShaderGroup : (F)V
    //   801: invokestatic enableLighting : ()V
    //   804: iconst_1
    //   805: invokestatic depthMask : (Z)V
    //   808: aload_0
    //   809: getfield mc : Lnet/minecraft/client/Minecraft;
    //   812: invokevirtual getFramebuffer : ()Lnet/minecraft/client/shader/Framebuffer;
    //   815: iconst_0
    //   816: invokevirtual bindFramebuffer : (Z)V
    //   819: invokestatic enableFog : ()V
    //   822: invokestatic enableBlend : ()V
    //   825: invokestatic enableColorMaterial : ()V
    //   828: sipush #515
    //   831: invokestatic depthFunc : (I)V
    //   834: invokestatic enableDepth : ()V
    //   837: invokestatic enableAlpha : ()V
    //   840: aload_0
    //   841: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   844: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   847: ldc_w 'entities'
    //   850: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   853: invokestatic isShaders : ()Z
    //   856: istore #21
    //   858: iload #21
    //   860: ifeq -> 866
    //   863: invokestatic beginEntities : ()V
    //   866: aload_0
    //   867: getfield renderInfosEntities : Ljava/util/List;
    //   870: invokeinterface iterator : ()Ljava/util/Iterator;
    //   875: astore #22
    //   877: aload_0
    //   878: getfield mc : Lnet/minecraft/client/Minecraft;
    //   881: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   884: getfield fancyGraphics : Z
    //   887: istore #23
    //   889: aload_0
    //   890: getfield mc : Lnet/minecraft/client/Minecraft;
    //   893: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   896: invokestatic isDroppedItemsFancy : ()Z
    //   899: putfield fancyGraphics : Z
    //   902: aload #22
    //   904: invokeinterface hasNext : ()Z
    //   909: ifeq -> 1292
    //   912: aload #22
    //   914: invokeinterface next : ()Ljava/lang/Object;
    //   919: checkcast net/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation
    //   922: astore #24
    //   924: aload_0
    //   925: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   928: aload #24
    //   930: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
    //   933: invokevirtual getPosition : ()Lnet/minecraft/util/BlockPos;
    //   936: invokevirtual getChunkFromBlockCoords : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/world/chunk/Chunk;
    //   939: astore #25
    //   941: aload #25
    //   943: invokevirtual getEntityLists : ()[Lnet/minecraft/util/ClassInheritanceMultiMap;
    //   946: aload #24
    //   948: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
    //   951: invokevirtual getPosition : ()Lnet/minecraft/util/BlockPos;
    //   954: invokevirtual getY : ()I
    //   957: bipush #16
    //   959: idiv
    //   960: aaload
    //   961: astore #26
    //   963: aload #26
    //   965: invokevirtual isEmpty : ()Z
    //   968: ifne -> 1289
    //   971: aload #26
    //   973: invokevirtual iterator : ()Ljava/util/Iterator;
    //   976: astore #27
    //   978: aload #27
    //   980: invokeinterface hasNext : ()Z
    //   985: ifne -> 991
    //   988: goto -> 902
    //   991: aload #27
    //   993: invokeinterface next : ()Ljava/lang/Object;
    //   998: checkcast net/minecraft/entity/Entity
    //   1001: astore #28
    //   1003: iload #19
    //   1005: ifeq -> 1031
    //   1008: aload #28
    //   1010: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   1013: iconst_1
    //   1014: anewarray java/lang/Object
    //   1017: dup
    //   1018: iconst_0
    //   1019: iload #4
    //   1021: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1024: aastore
    //   1025: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   1028: ifeq -> 978
    //   1031: aload_0
    //   1032: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1035: aload #28
    //   1037: aload_2
    //   1038: dload #5
    //   1040: dload #7
    //   1042: dload #9
    //   1044: invokevirtual shouldRender : (Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z
    //   1047: ifne -> 1065
    //   1050: aload #28
    //   1052: getfield riddenByEntity : Lnet/minecraft/entity/Entity;
    //   1055: aload_0
    //   1056: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1059: getfield thePlayer : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   1062: if_acmpne -> 1069
    //   1065: iconst_1
    //   1066: goto -> 1070
    //   1069: iconst_0
    //   1070: istore #29
    //   1072: iload #29
    //   1074: ifne -> 1080
    //   1077: goto -> 1250
    //   1080: aload_0
    //   1081: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1084: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   1087: instanceof net/minecraft/entity/EntityLivingBase
    //   1090: ifeq -> 1109
    //   1093: aload_0
    //   1094: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1097: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   1100: checkcast net/minecraft/entity/EntityLivingBase
    //   1103: invokevirtual isPlayerSleeping : ()Z
    //   1106: goto -> 1110
    //   1109: iconst_0
    //   1110: istore #30
    //   1112: aload #28
    //   1114: aload_0
    //   1115: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1118: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   1121: if_acmpne -> 1142
    //   1124: aload_0
    //   1125: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1128: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   1131: getfield thirdPersonView : I
    //   1134: ifne -> 1142
    //   1137: iload #30
    //   1139: ifeq -> 1247
    //   1142: aload #28
    //   1144: getfield posY : D
    //   1147: dconst_0
    //   1148: dcmpg
    //   1149: iflt -> 1183
    //   1152: aload #28
    //   1154: getfield posY : D
    //   1157: ldc2_w 256.0
    //   1160: dcmpl
    //   1161: ifge -> 1183
    //   1164: aload_0
    //   1165: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   1168: new net/minecraft/util/BlockPos
    //   1171: dup
    //   1172: aload #28
    //   1174: invokespecial <init> : (Lnet/minecraft/entity/Entity;)V
    //   1177: invokevirtual isBlockLoaded : (Lnet/minecraft/util/BlockPos;)Z
    //   1180: ifeq -> 1247
    //   1183: aload_0
    //   1184: dup
    //   1185: getfield countEntitiesRendered : I
    //   1188: iconst_1
    //   1189: iadd
    //   1190: putfield countEntitiesRendered : I
    //   1193: aload #28
    //   1195: invokevirtual getClass : ()Ljava/lang/Class;
    //   1198: ldc_w net/minecraft/entity/item/EntityItemFrame
    //   1201: if_acmpne -> 1212
    //   1204: aload #28
    //   1206: ldc2_w 0.06
    //   1209: putfield renderDistanceWeight : D
    //   1212: aload_0
    //   1213: aload #28
    //   1215: putfield renderedEntity : Lnet/minecraft/entity/Entity;
    //   1218: iload #21
    //   1220: ifeq -> 1228
    //   1223: aload #28
    //   1225: invokestatic nextEntity : (Lnet/minecraft/entity/Entity;)V
    //   1228: aload_0
    //   1229: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1232: aload #28
    //   1234: fload_3
    //   1235: invokevirtual renderEntitySimple : (Lnet/minecraft/entity/Entity;F)Z
    //   1238: pop
    //   1239: aload_0
    //   1240: aconst_null
    //   1241: putfield renderedEntity : Lnet/minecraft/entity/Entity;
    //   1244: goto -> 1250
    //   1247: goto -> 978
    //   1250: iload #29
    //   1252: ifne -> 1286
    //   1255: aload #28
    //   1257: instanceof net/minecraft/entity/projectile/EntityWitherSkull
    //   1260: ifeq -> 1286
    //   1263: iload #21
    //   1265: ifeq -> 1273
    //   1268: aload #28
    //   1270: invokestatic nextEntity : (Lnet/minecraft/entity/Entity;)V
    //   1273: aload_0
    //   1274: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1277: invokevirtual getRenderManager : ()Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1280: aload #28
    //   1282: fload_3
    //   1283: invokevirtual renderWitherSkull : (Lnet/minecraft/entity/Entity;F)V
    //   1286: goto -> 978
    //   1289: goto -> 902
    //   1292: aload_0
    //   1293: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1296: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   1299: iload #23
    //   1301: putfield fancyGraphics : Z
    //   1304: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1307: invokevirtual getFontRenderer : ()Lnet/minecraft/client/gui/FontRenderer;
    //   1310: astore #24
    //   1312: iload #21
    //   1314: ifeq -> 1323
    //   1317: invokestatic endEntities : ()V
    //   1320: invokestatic beginBlockEntities : ()V
    //   1323: aload_0
    //   1324: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   1327: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   1330: ldc_w 'blockentities'
    //   1333: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   1336: invokestatic enableStandardItemLighting : ()V
    //   1339: getstatic optifine/Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch : Loptifine/ReflectorMethod;
    //   1342: invokevirtual exists : ()Z
    //   1345: ifeq -> 1362
    //   1348: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1351: getstatic optifine/Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch : Loptifine/ReflectorMethod;
    //   1354: iconst_0
    //   1355: anewarray java/lang/Object
    //   1358: invokestatic call : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Ljava/lang/Object;
    //   1361: pop
    //   1362: aload_0
    //   1363: getfield renderInfosTileEntities : Ljava/util/List;
    //   1366: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1371: astore #25
    //   1373: aload #25
    //   1375: invokeinterface hasNext : ()Z
    //   1380: ifeq -> 1635
    //   1383: aload #25
    //   1385: invokeinterface next : ()Ljava/lang/Object;
    //   1390: astore #26
    //   1392: aload #26
    //   1394: checkcast net/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation
    //   1397: astore #27
    //   1399: aload #27
    //   1401: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
    //   1404: invokevirtual getCompiledChunk : ()Lnet/minecraft/client/renderer/chunk/CompiledChunk;
    //   1407: invokevirtual getTileEntities : ()Ljava/util/List;
    //   1410: astore #28
    //   1412: aload #28
    //   1414: invokeinterface isEmpty : ()Z
    //   1419: ifne -> 1632
    //   1422: aload #28
    //   1424: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1429: astore #29
    //   1431: aload #29
    //   1433: invokeinterface hasNext : ()Z
    //   1438: ifne -> 1444
    //   1441: goto -> 1373
    //   1444: aload #29
    //   1446: invokeinterface next : ()Ljava/lang/Object;
    //   1451: checkcast net/minecraft/tileentity/TileEntity
    //   1454: astore #30
    //   1456: iload #20
    //   1458: ifne -> 1464
    //   1461: goto -> 1526
    //   1464: aload #30
    //   1466: getstatic optifine/Reflector.ForgeTileEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   1469: iconst_1
    //   1470: anewarray java/lang/Object
    //   1473: dup
    //   1474: iconst_0
    //   1475: iload #4
    //   1477: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1480: aastore
    //   1481: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   1484: ifeq -> 1431
    //   1487: aload #30
    //   1489: getstatic optifine/Reflector.ForgeTileEntity_getRenderBoundingBox : Loptifine/ReflectorMethod;
    //   1492: iconst_0
    //   1493: anewarray java/lang/Object
    //   1496: invokestatic call : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Ljava/lang/Object;
    //   1499: checkcast net/minecraft/util/AxisAlignedBB
    //   1502: astore #31
    //   1504: aload #31
    //   1506: ifnull -> 1526
    //   1509: aload_2
    //   1510: aload #31
    //   1512: invokeinterface isBoundingBoxInFrustum : (Lnet/minecraft/util/AxisAlignedBB;)Z
    //   1517: ifeq -> 1523
    //   1520: goto -> 1526
    //   1523: goto -> 1431
    //   1526: aload #30
    //   1528: invokevirtual getClass : ()Ljava/lang/Class;
    //   1531: astore #31
    //   1533: aload #31
    //   1535: ldc_w net/minecraft/tileentity/TileEntitySign
    //   1538: if_acmpne -> 1593
    //   1541: getstatic optifine/Config.zoomMode : Z
    //   1544: ifne -> 1593
    //   1547: aload_0
    //   1548: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1551: getfield thePlayer : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   1554: astore #32
    //   1556: aload #30
    //   1558: aload #32
    //   1560: getfield posX : D
    //   1563: aload #32
    //   1565: getfield posY : D
    //   1568: aload #32
    //   1570: getfield posZ : D
    //   1573: invokevirtual getDistanceSq : (DDD)D
    //   1576: dstore #33
    //   1578: dload #33
    //   1580: ldc2_w 256.0
    //   1583: dcmpl
    //   1584: ifle -> 1593
    //   1587: aload #24
    //   1589: iconst_0
    //   1590: putfield enabled : Z
    //   1593: iload #21
    //   1595: ifeq -> 1603
    //   1598: aload #30
    //   1600: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
    //   1603: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1606: aload #30
    //   1608: fload_3
    //   1609: iconst_m1
    //   1610: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
    //   1613: aload_0
    //   1614: dup
    //   1615: getfield countTileEntitiesRendered : I
    //   1618: iconst_1
    //   1619: iadd
    //   1620: putfield countTileEntitiesRendered : I
    //   1623: aload #24
    //   1625: iconst_1
    //   1626: putfield enabled : Z
    //   1629: goto -> 1431
    //   1632: goto -> 1373
    //   1635: aload_0
    //   1636: getfield field_181024_n : Ljava/util/Set;
    //   1639: astore #25
    //   1641: aload_0
    //   1642: getfield field_181024_n : Ljava/util/Set;
    //   1645: dup
    //   1646: astore #26
    //   1648: monitorenter
    //   1649: aload_0
    //   1650: getfield field_181024_n : Ljava/util/Set;
    //   1653: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1658: astore #27
    //   1660: aload #27
    //   1662: invokeinterface hasNext : ()Z
    //   1667: ifeq -> 1851
    //   1670: aload #27
    //   1672: invokeinterface next : ()Ljava/lang/Object;
    //   1677: astore #28
    //   1679: iload #20
    //   1681: ifeq -> 1746
    //   1684: aload #28
    //   1686: getstatic optifine/Reflector.ForgeTileEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   1689: iconst_1
    //   1690: anewarray java/lang/Object
    //   1693: dup
    //   1694: iconst_0
    //   1695: iload #4
    //   1697: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1700: aastore
    //   1701: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   1704: ifne -> 1710
    //   1707: goto -> 1660
    //   1710: aload #28
    //   1712: getstatic optifine/Reflector.ForgeTileEntity_getRenderBoundingBox : Loptifine/ReflectorMethod;
    //   1715: iconst_0
    //   1716: anewarray java/lang/Object
    //   1719: invokestatic call : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Ljava/lang/Object;
    //   1722: checkcast net/minecraft/util/AxisAlignedBB
    //   1725: astore #29
    //   1727: aload #29
    //   1729: ifnull -> 1746
    //   1732: aload_2
    //   1733: aload #29
    //   1735: invokeinterface isBoundingBoxInFrustum : (Lnet/minecraft/util/AxisAlignedBB;)Z
    //   1740: ifne -> 1746
    //   1743: goto -> 1660
    //   1746: aload #28
    //   1748: invokevirtual getClass : ()Ljava/lang/Class;
    //   1751: astore #29
    //   1753: aload #29
    //   1755: ldc_w net/minecraft/tileentity/TileEntitySign
    //   1758: if_acmpne -> 1816
    //   1761: getstatic optifine/Config.zoomMode : Z
    //   1764: ifne -> 1816
    //   1767: aload_0
    //   1768: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1771: getfield thePlayer : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   1774: astore #30
    //   1776: aload #28
    //   1778: checkcast net/minecraft/tileentity/TileEntity
    //   1781: aload #30
    //   1783: getfield posX : D
    //   1786: aload #30
    //   1788: getfield posY : D
    //   1791: aload #30
    //   1793: getfield posZ : D
    //   1796: invokevirtual getDistanceSq : (DDD)D
    //   1799: dstore #31
    //   1801: dload #31
    //   1803: ldc2_w 256.0
    //   1806: dcmpl
    //   1807: ifle -> 1816
    //   1810: aload #24
    //   1812: iconst_0
    //   1813: putfield enabled : Z
    //   1816: iload #21
    //   1818: ifeq -> 1829
    //   1821: aload #28
    //   1823: checkcast net/minecraft/tileentity/TileEntity
    //   1826: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
    //   1829: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1832: aload #28
    //   1834: checkcast net/minecraft/tileentity/TileEntity
    //   1837: fload_3
    //   1838: iconst_m1
    //   1839: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
    //   1842: aload #24
    //   1844: iconst_1
    //   1845: putfield enabled : Z
    //   1848: goto -> 1660
    //   1851: aload #26
    //   1853: monitorexit
    //   1854: goto -> 1865
    //   1857: astore #35
    //   1859: aload #26
    //   1861: monitorexit
    //   1862: aload #35
    //   1864: athrow
    //   1865: getstatic optifine/Reflector.ForgeTileEntityRendererDispatcher_drawBatch : Loptifine/ReflectorMethod;
    //   1868: invokevirtual exists : ()Z
    //   1871: ifeq -> 1896
    //   1874: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1877: getstatic optifine/Reflector.ForgeTileEntityRendererDispatcher_drawBatch : Loptifine/ReflectorMethod;
    //   1880: iconst_1
    //   1881: anewarray java/lang/Object
    //   1884: dup
    //   1885: iconst_0
    //   1886: iload #4
    //   1888: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1891: aastore
    //   1892: invokestatic call : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Ljava/lang/Object;
    //   1895: pop
    //   1896: aload_0
    //   1897: invokespecial preRenderDamagedBlocks : ()V
    //   1900: aload_0
    //   1901: getfield damagedBlocks : Ljava/util/Map;
    //   1904: invokeinterface values : ()Ljava/util/Collection;
    //   1909: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1914: astore #26
    //   1916: aload #26
    //   1918: invokeinterface hasNext : ()Z
    //   1923: ifeq -> 2213
    //   1926: aload #26
    //   1928: invokeinterface next : ()Ljava/lang/Object;
    //   1933: astore #27
    //   1935: aload #27
    //   1937: checkcast net/minecraft/client/renderer/DestroyBlockProgress
    //   1940: invokevirtual getPosition : ()Lnet/minecraft/util/BlockPos;
    //   1943: astore #28
    //   1945: aload_0
    //   1946: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   1949: aload #28
    //   1951: invokevirtual getTileEntity : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
    //   1954: astore #29
    //   1956: aload #29
    //   1958: instanceof net/minecraft/tileentity/TileEntityChest
    //   1961: ifeq -> 2032
    //   1964: aload #29
    //   1966: checkcast net/minecraft/tileentity/TileEntityChest
    //   1969: astore #30
    //   1971: aload #30
    //   1973: getfield adjacentChestXNeg : Lnet/minecraft/tileentity/TileEntityChest;
    //   1976: ifnull -> 2003
    //   1979: aload #28
    //   1981: getstatic net/minecraft/util/EnumFacing.WEST : Lnet/minecraft/util/EnumFacing;
    //   1984: invokevirtual offset : (Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/BlockPos;
    //   1987: astore #28
    //   1989: aload_0
    //   1990: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   1993: aload #28
    //   1995: invokevirtual getTileEntity : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
    //   1998: astore #29
    //   2000: goto -> 2032
    //   2003: aload #30
    //   2005: getfield adjacentChestZNeg : Lnet/minecraft/tileentity/TileEntityChest;
    //   2008: ifnull -> 2032
    //   2011: aload #28
    //   2013: getstatic net/minecraft/util/EnumFacing.NORTH : Lnet/minecraft/util/EnumFacing;
    //   2016: invokevirtual offset : (Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/BlockPos;
    //   2019: astore #28
    //   2021: aload_0
    //   2022: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   2025: aload #28
    //   2027: invokevirtual getTileEntity : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
    //   2030: astore #29
    //   2032: aload_0
    //   2033: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   2036: aload #28
    //   2038: invokevirtual getBlockState : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   2041: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   2046: astore #30
    //   2048: iload #20
    //   2050: ifeq -> 2134
    //   2053: iconst_0
    //   2054: istore #31
    //   2056: aload #29
    //   2058: ifnull -> 2178
    //   2061: aload #29
    //   2063: getstatic optifine/Reflector.ForgeTileEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   2066: iconst_1
    //   2067: anewarray java/lang/Object
    //   2070: dup
    //   2071: iconst_0
    //   2072: iload #4
    //   2074: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2077: aastore
    //   2078: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   2081: ifeq -> 2178
    //   2084: aload #29
    //   2086: getstatic optifine/Reflector.ForgeTileEntity_canRenderBreaking : Loptifine/ReflectorMethod;
    //   2089: iconst_0
    //   2090: anewarray java/lang/Object
    //   2093: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   2096: ifeq -> 2178
    //   2099: aload #29
    //   2101: getstatic optifine/Reflector.ForgeTileEntity_getRenderBoundingBox : Loptifine/ReflectorMethod;
    //   2104: iconst_0
    //   2105: anewarray java/lang/Object
    //   2108: invokestatic call : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Ljava/lang/Object;
    //   2111: checkcast net/minecraft/util/AxisAlignedBB
    //   2114: astore #32
    //   2116: aload #32
    //   2118: ifnull -> 2131
    //   2121: aload_2
    //   2122: aload #32
    //   2124: invokeinterface isBoundingBoxInFrustum : (Lnet/minecraft/util/AxisAlignedBB;)Z
    //   2129: istore #31
    //   2131: goto -> 2178
    //   2134: aload #29
    //   2136: ifnull -> 2175
    //   2139: aload #30
    //   2141: instanceof net/minecraft/block/BlockChest
    //   2144: ifne -> 2171
    //   2147: aload #30
    //   2149: instanceof net/minecraft/block/BlockEnderChest
    //   2152: ifne -> 2171
    //   2155: aload #30
    //   2157: instanceof net/minecraft/block/BlockSign
    //   2160: ifne -> 2171
    //   2163: aload #30
    //   2165: instanceof net/minecraft/block/BlockSkull
    //   2168: ifeq -> 2175
    //   2171: iconst_1
    //   2172: goto -> 2176
    //   2175: iconst_0
    //   2176: istore #31
    //   2178: iload #31
    //   2180: ifeq -> 2210
    //   2183: iload #21
    //   2185: ifeq -> 2193
    //   2188: aload #29
    //   2190: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
    //   2193: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   2196: aload #29
    //   2198: fload_3
    //   2199: aload #27
    //   2201: checkcast net/minecraft/client/renderer/DestroyBlockProgress
    //   2204: invokevirtual getPartialBlockDamage : ()I
    //   2207: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
    //   2210: goto -> 1916
    //   2213: aload_0
    //   2214: invokespecial postRenderDamagedBlocks : ()V
    //   2217: aload_0
    //   2218: getfield mc : Lnet/minecraft/client/Minecraft;
    //   2221: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
    //   2224: invokevirtual disableLightmap : ()V
    //   2227: aload_0
    //   2228: getfield mc : Lnet/minecraft/client/Minecraft;
    //   2231: getfield mcProfiler : Lnet/minecraft/profiler/Profiler;
    //   2234: invokevirtual endSection : ()V
    //   2237: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #623	-> 0
    //   #625	-> 3
    //   #627	-> 12
    //   #630	-> 24
    //   #632	-> 31
    //   #634	-> 36
    //   #637	-> 37
    //   #641	-> 50
    //   #642	-> 69
    //   #643	-> 88
    //   #644	-> 107
    //   #645	-> 119
    //   #646	-> 151
    //   #648	-> 191
    //   #650	-> 196
    //   #651	-> 201
    //   #652	-> 206
    //   #653	-> 211
    //   #656	-> 216
    //   #657	-> 225
    //   #658	-> 247
    //   #659	-> 269
    //   #660	-> 291
    //   #661	-> 296
    //   #662	-> 301
    //   #663	-> 306
    //   #664	-> 319
    //   #665	-> 329
    //   #666	-> 341
    //   #668	-> 350
    //   #670	-> 355
    //   #673	-> 366
    //   #675	-> 385
    //   #678	-> 388
    //   #679	-> 396
    //   #681	-> 404
    //   #683	-> 424
    //   #685	-> 443
    //   #687	-> 471
    //   #689	-> 481
    //   #691	-> 495
    //   #681	-> 506
    //   #696	-> 512
    //   #698	-> 519
    //   #699	-> 525
    //   #700	-> 528
    //   #701	-> 535
    //   #702	-> 543
    //   #703	-> 556
    //   #704	-> 559
    //   #706	-> 567
    //   #708	-> 582
    //   #710	-> 596
    //   #712	-> 624
    //   #713	-> 660
    //   #715	-> 726
    //   #717	-> 761
    //   #706	-> 772
    //   #722	-> 778
    //   #723	-> 786
    //   #724	-> 789
    //   #725	-> 793
    //   #726	-> 801
    //   #727	-> 804
    //   #728	-> 808
    //   #729	-> 819
    //   #730	-> 822
    //   #731	-> 825
    //   #732	-> 828
    //   #733	-> 834
    //   #734	-> 837
    //   #737	-> 840
    //   #738	-> 853
    //   #740	-> 858
    //   #742	-> 863
    //   #745	-> 866
    //   #746	-> 877
    //   #747	-> 889
    //   #750	-> 902
    //   #752	-> 912
    //   #753	-> 924
    //   #754	-> 941
    //   #756	-> 963
    //   #758	-> 971
    //   #767	-> 978
    //   #769	-> 988
    //   #772	-> 991
    //   #774	-> 1003
    //   #776	-> 1031
    //   #778	-> 1072
    //   #780	-> 1077
    //   #783	-> 1080
    //   #785	-> 1112
    //   #787	-> 1183
    //   #789	-> 1193
    //   #791	-> 1204
    //   #794	-> 1212
    //   #796	-> 1218
    //   #798	-> 1223
    //   #801	-> 1228
    //   #802	-> 1239
    //   #803	-> 1244
    //   #805	-> 1247
    //   #808	-> 1250
    //   #810	-> 1263
    //   #812	-> 1268
    //   #815	-> 1273
    //   #817	-> 1286
    //   #819	-> 1289
    //   #821	-> 1292
    //   #822	-> 1304
    //   #824	-> 1312
    //   #826	-> 1317
    //   #827	-> 1320
    //   #830	-> 1323
    //   #831	-> 1336
    //   #833	-> 1339
    //   #835	-> 1348
    //   #840	-> 1362
    //   #842	-> 1392
    //   #843	-> 1399
    //   #844	-> 1407
    //   #846	-> 1412
    //   #848	-> 1422
    //   #856	-> 1431
    //   #858	-> 1441
    //   #861	-> 1444
    //   #863	-> 1456
    //   #865	-> 1461
    //   #868	-> 1464
    //   #870	-> 1487
    //   #872	-> 1504
    //   #874	-> 1520
    //   #876	-> 1523
    //   #879	-> 1526
    //   #881	-> 1533
    //   #883	-> 1547
    //   #884	-> 1556
    //   #886	-> 1578
    //   #888	-> 1587
    //   #892	-> 1593
    //   #894	-> 1598
    //   #897	-> 1603
    //   #898	-> 1613
    //   #899	-> 1623
    //   #900	-> 1629
    //   #902	-> 1632
    //   #904	-> 1635
    //   #906	-> 1641
    //   #908	-> 1649
    //   #910	-> 1679
    //   #912	-> 1684
    //   #914	-> 1707
    //   #916	-> 1710
    //   #918	-> 1727
    //   #920	-> 1743
    //   #924	-> 1746
    //   #926	-> 1753
    //   #928	-> 1767
    //   #929	-> 1776
    //   #931	-> 1801
    //   #933	-> 1810
    //   #937	-> 1816
    //   #939	-> 1821
    //   #942	-> 1829
    //   #943	-> 1842
    //   #944	-> 1848
    //   #945	-> 1851
    //   #947	-> 1865
    //   #949	-> 1874
    //   #952	-> 1896
    //   #954	-> 1900
    //   #956	-> 1935
    //   #957	-> 1945
    //   #959	-> 1956
    //   #961	-> 1964
    //   #963	-> 1971
    //   #965	-> 1979
    //   #966	-> 1989
    //   #968	-> 2003
    //   #970	-> 2011
    //   #971	-> 2021
    //   #975	-> 2032
    //   #978	-> 2048
    //   #980	-> 2053
    //   #982	-> 2056
    //   #984	-> 2099
    //   #986	-> 2116
    //   #988	-> 2121
    //   #990	-> 2131
    //   #994	-> 2134
    //   #997	-> 2178
    //   #999	-> 2183
    //   #1001	-> 2188
    //   #1004	-> 2193
    //   #1006	-> 2210
    //   #1008	-> 2213
    //   #1009	-> 2217
    //   #1010	-> 2227
    //   #1012	-> 2237
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   443	63	22	entity1	Lnet/minecraft/entity/Entity;
    //   407	105	21	j	I
    //   660	112	23	flag2	Z
    //   726	46	24	flag3	Z
    //   596	176	22	entity3	Lnet/minecraft/entity/Entity;
    //   570	208	21	k	I
    //   1112	135	30	flag6	Z
    //   1003	283	28	entity2	Lnet/minecraft/entity/Entity;
    //   1072	214	29	flag5	Z
    //   978	311	27	iterator	Ljava/util/Iterator;
    //   924	365	24	renderglobal$containerlocalrenderinformation	Lnet/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation;
    //   941	348	25	chunk	Lnet/minecraft/world/chunk/Chunk;
    //   963	326	26	classinheritancemultimap	Lnet/minecraft/util/ClassInheritanceMultiMap;
    //   1504	19	31	axisalignedbb	Lnet/minecraft/util/AxisAlignedBB;
    //   1556	37	32	entityplayer	Lnet/minecraft/entity/player/EntityPlayer;
    //   1578	15	33	d6	D
    //   1456	173	30	tileentity	Lnet/minecraft/tileentity/TileEntity;
    //   1533	96	31	oclass	Ljava/lang/Class;
    //   1431	201	29	iterator2	Ljava/util/Iterator;
    //   1399	233	27	renderglobal$containerlocalrenderinformation1	Lnet/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation;
    //   1412	220	28	list1	Ljava/util/List;
    //   1392	240	26	renderglobal$containerlocalrenderinformation10	Ljava/lang/Object;
    //   1727	19	29	axisalignedbb1	Lnet/minecraft/util/AxisAlignedBB;
    //   1776	40	30	entityplayer1	Lnet/minecraft/entity/player/EntityPlayer;
    //   1801	15	31	d7	D
    //   1753	95	29	oclass1	Ljava/lang/Class;
    //   1679	169	28	tileentity1	Ljava/lang/Object;
    //   1971	61	30	tileentitychest	Lnet/minecraft/tileentity/TileEntityChest;
    //   2116	15	32	axisalignedbb2	Lnet/minecraft/util/AxisAlignedBB;
    //   2056	78	31	flag8	Z
    //   1945	265	28	blockpos	Lnet/minecraft/util/BlockPos;
    //   1956	254	29	tileentity2	Lnet/minecraft/tileentity/TileEntity;
    //   2048	162	30	block	Lnet/minecraft/block/Block;
    //   2178	32	31	flag8	Z
    //   1935	275	27	destroyblockprogress	Ljava/lang/Object;
    //   69	2168	5	d0	D
    //   88	2149	7	d1	D
    //   107	2130	9	d2	D
    //   225	2012	11	entity	Lnet/minecraft/entity/Entity;
    //   247	1990	12	d3	D
    //   269	1968	14	d4	D
    //   291	1946	16	d5	D
    //   350	1887	18	list	Ljava/util/List;
    //   396	1841	19	flag	Z
    //   404	1833	20	flag1	Z
    //   858	1379	21	flag7	Z
    //   877	1360	22	iterator1	Ljava/util/Iterator;
    //   889	1348	23	flag4	Z
    //   1312	925	24	fontrenderer	Lnet/minecraft/client/gui/FontRenderer;
    //   1641	596	25	var32	Ljava/util/Set;
    //   0	2238	0	this	Lnet/minecraft/client/renderer/RenderGlobal;
    //   0	2238	1	renderViewEntity	Lnet/minecraft/entity/Entity;
    //   0	2238	2	camera	Lnet/minecraft/client/renderer/culling/ICamera;
    //   0	2238	3	partialTicks	F
    //   3	2235	4	i	I
    // Exception table:
    //   from	to	target	type
    //   1649	1854	1857	finally
    //   1857	1862	1857	finally
  }
  
  public String getDebugInfoRenders() {
    int i = this.viewFrustum.renderChunks.length;
    int j = 0;
    for (Object renderglobal$containerlocalrenderinformation0 : this.renderInfos) {
      ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = (ContainerLocalRenderInformation)renderglobal$containerlocalrenderinformation0;
      CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;
      if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty())
        j++; 
    } 
    return String.format("C: %d/%d %sD: %d, %s", new Object[] { Integer.valueOf(j), Integer.valueOf(i), this.mc.renderChunksMany ? "(s) " : "", Integer.valueOf(this.renderDistanceChunks), this.renderDispatcher.getDebugInfo() });
  }
  
  public String getDebugInfoEntities() {
    return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersionDebug();
  }
  
  public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
    Frustum frustum;
    if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks)
      loadRenderers(); 
    this.theWorld.theProfiler.startSection("camera");
    double d0 = viewEntity.posX - this.frustumUpdatePosX;
    double d1 = viewEntity.posY - this.frustumUpdatePosY;
    double d2 = viewEntity.posZ - this.frustumUpdatePosZ;
    if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || d0 * d0 + d1 * d1 + d2 * d2 > 16.0D) {
      this.frustumUpdatePosX = viewEntity.posX;
      this.frustumUpdatePosY = viewEntity.posY;
      this.frustumUpdatePosZ = viewEntity.posZ;
      this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
      this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
      this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
      this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
    } 
    if (Config.isDynamicLights())
      DynamicLights.update(this); 
    this.theWorld.theProfiler.endStartSection("renderlistcamera");
    double d3 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
    double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
    double d5 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
    this.renderContainer.initialize(d3, d4, d5);
    this.theWorld.theProfiler.endStartSection("cull");
    if (this.debugFixedClippingHelper != null) {
      Frustum frustum1 = new Frustum(this.debugFixedClippingHelper);
      frustum1.setPosition(this.debugTerrainFrustumPosition.field_181059_a, this.debugTerrainFrustumPosition.field_181060_b, this.debugTerrainFrustumPosition.field_181061_c);
      frustum = frustum1;
    } 
    this.mc.mcProfiler.endStartSection("culling");
    BlockPos blockpos2 = new BlockPos(d3, d4 + viewEntity.getEyeHeight(), d5);
    RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos2);
    BlockPos blockpos = new BlockPos(MathHelper.floor_double(d3 / 16.0D) * 16, MathHelper.floor_double(d4 / 16.0D) * 16, MathHelper.floor_double(d5 / 16.0D) * 16);
    this.displayListEntitiesDirty = (this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || viewEntity.rotationPitch != this.lastViewEntityPitch || viewEntity.rotationYaw != this.lastViewEntityYaw);
    this.lastViewEntityX = viewEntity.posX;
    this.lastViewEntityY = viewEntity.posY;
    this.lastViewEntityZ = viewEntity.posZ;
    this.lastViewEntityPitch = viewEntity.rotationPitch;
    this.lastViewEntityYaw = viewEntity.rotationYaw;
    boolean flag = (this.debugFixedClippingHelper != null);
    Lagometer.timerVisibility.start();
    if (Shaders.isShadowPass) {
      this.renderInfos = this.renderInfosShadow;
      this.renderInfosEntities = this.renderInfosEntitiesShadow;
      this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;
      if (!flag && this.displayListEntitiesDirty) {
        this.renderInfos.clear();
        this.renderInfosEntities.clear();
        this.renderInfosTileEntities.clear();
        RenderInfoLazy renderinfolazy = new RenderInfoLazy();
        Iterator<RenderChunk> iterator = ShadowUtils.makeShadowChunkIterator(this.theWorld, partialTicks, viewEntity, this.renderDistanceChunks, this.viewFrustum);
        while (iterator.hasNext()) {
          RenderChunk renderchunk1 = iterator.next();
          if (renderchunk1 != null) {
            renderinfolazy.setRenderChunk(renderchunk1);
            if (!renderchunk1.compiledChunk.isEmpty() || renderchunk1.isNeedsUpdate())
              this.renderInfos.add(renderinfolazy.getRenderInfo()); 
            BlockPos blockpos1 = renderchunk1.getPosition();
            if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(blockpos1)))
              this.renderInfosEntities.add(renderinfolazy.getRenderInfo()); 
            if (renderchunk1.getCompiledChunk().getTileEntities().size() > 0)
              this.renderInfosTileEntities.add(renderinfolazy.getRenderInfo()); 
          } 
        } 
      } 
    } else {
      this.renderInfos = this.renderInfosNormal;
      this.renderInfosEntities = this.renderInfosEntitiesNormal;
      this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
    } 
    if (!flag && this.displayListEntitiesDirty && !Shaders.isShadowPass) {
      this.displayListEntitiesDirty = false;
      this.renderInfos.clear();
      this.renderInfosEntities.clear();
      this.renderInfosTileEntities.clear();
      this.visibilityDeque.clear();
      Deque<ContainerLocalRenderInformation> deque = this.visibilityDeque;
      boolean flag1 = this.mc.renderChunksMany;
      if (renderchunk != null) {
        boolean flag2 = false;
        ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = new ContainerLocalRenderInformation(renderchunk, (EnumFacing)null, 0, null);
        Set set1 = SET_ALL_FACINGS;
        if (set1.size() == 1) {
          Vector3f vector3f = getViewVector(viewEntity, partialTicks);
          EnumFacing enumfacing = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
          set1.remove(enumfacing);
        } 
        if (set1.isEmpty())
          flag2 = true; 
        if (flag2 && !playerSpectator) {
          this.renderInfos.add(renderglobal$containerlocalrenderinformation3);
        } else {
          if (playerSpectator && this.theWorld.getBlockState(blockpos2).getBlock().isOpaqueCube())
            flag1 = false; 
          renderchunk.setFrameIndex(frameCount);
          deque.add(renderglobal$containerlocalrenderinformation3);
        } 
      } else {
        int i = (blockpos2.getY() > 0) ? 248 : 8;
        for (int j = -this.renderDistanceChunks; j <= this.renderDistanceChunks; j++) {
          for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; k++) {
            RenderChunk renderchunk2 = this.viewFrustum.getRenderChunk(new BlockPos((j << 4) + 8, i, (k << 4) + 8));
            if (renderchunk2 != null && frustum.isBoundingBoxInFrustum(renderchunk2.boundingBox)) {
              renderchunk2.setFrameIndex(frameCount);
              deque.add(new ContainerLocalRenderInformation(renderchunk2, (EnumFacing)null, 0, null));
            } 
          } 
        } 
      } 
      EnumFacing[] aenumfacing = EnumFacing.VALUES;
      int l = aenumfacing.length;
      while (!deque.isEmpty()) {
        ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = deque.poll();
        RenderChunk renderchunk4 = renderglobal$containerlocalrenderinformation1.renderChunk;
        EnumFacing enumfacing2 = renderglobal$containerlocalrenderinformation1.facing;
        BlockPos blockpos3 = renderchunk4.getPosition();
        if (!renderchunk4.compiledChunk.isEmpty() || renderchunk4.isNeedsUpdate())
          this.renderInfos.add(renderglobal$containerlocalrenderinformation1); 
        if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(blockpos3)))
          this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation1); 
        if (renderchunk4.getCompiledChunk().getTileEntities().size() > 0)
          this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation1); 
        for (int i1 = 0; i1 < l; i1++) {
          EnumFacing enumfacing1 = aenumfacing[i1];
          if ((!flag1 || !renderglobal$containerlocalrenderinformation1.setFacing.contains(enumfacing1.getOpposite())) && (!flag1 || enumfacing2 == null || renderchunk4.getCompiledChunk().isVisible(enumfacing2.getOpposite(), enumfacing1))) {
            RenderChunk renderchunk3 = func_181562_a(blockpos2, renderchunk4, enumfacing1);
            if (renderchunk3 != null && renderchunk3.setFrameIndex(frameCount) && frustum.isBoundingBoxInFrustum(renderchunk3.boundingBox)) {
              ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = new ContainerLocalRenderInformation(renderchunk3, enumfacing1, renderglobal$containerlocalrenderinformation1.counter + 1, null);
              renderglobal$containerlocalrenderinformation.setFacing.addAll(renderglobal$containerlocalrenderinformation1.setFacing);
              renderglobal$containerlocalrenderinformation.setFacing.add(enumfacing1);
              deque.add(renderglobal$containerlocalrenderinformation);
            } 
          } 
        } 
      } 
    } 
    if (this.debugFixTerrainFrustum) {
      fixTerrainFrustum(d3, d4, d5);
      this.debugFixTerrainFrustum = false;
    } 
    Lagometer.timerVisibility.end();
    if (Shaders.isShadowPass) {
      Shaders.mcProfilerEndSection();
    } else {
      this.renderDispatcher.clearChunkUpdates();
      Set set = this.chunksToUpdate;
      this.chunksToUpdate = Sets.newLinkedHashSet();
      Iterator<ContainerLocalRenderInformation> iterator1 = this.renderInfos.iterator();
      Lagometer.timerChunkUpdate.start();
      while (iterator1.hasNext()) {
        ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 = iterator1.next();
        RenderChunk renderchunk5 = renderglobal$containerlocalrenderinformation2.renderChunk;
        if (renderchunk5.isNeedsUpdate() || set.contains(renderchunk5)) {
          this.displayListEntitiesDirty = true;
          if (isPositionInRenderChunk(blockpos, renderglobal$containerlocalrenderinformation2.renderChunk)) {
            if (!renderchunk5.isPlayerUpdate()) {
              this.chunksToUpdateForced.add(renderchunk5);
              continue;
            } 
            this.mc.mcProfiler.startSection("build near");
            this.renderDispatcher.updateChunkNow(renderchunk5);
            renderchunk5.setNeedsUpdate(false);
            this.mc.mcProfiler.endSection();
            continue;
          } 
          this.chunksToUpdate.add(renderchunk5);
        } 
      } 
      Lagometer.timerChunkUpdate.end();
      this.chunksToUpdate.addAll(set);
      this.mc.mcProfiler.endSection();
    } 
  }
  
  private boolean isPositionInRenderChunk(BlockPos pos, RenderChunk renderChunkIn) {
    BlockPos blockpos = renderChunkIn.getPosition();
    return (MathHelper.abs_int(pos.getX() - blockpos.getX()) > 16) ? false : ((MathHelper.abs_int(pos.getY() - blockpos.getY()) > 16) ? false : ((MathHelper.abs_int(pos.getZ() - blockpos.getZ()) <= 16)));
  }
  
  private Set getVisibleFacings(BlockPos pos) {
    VisGraph visgraph = new VisGraph();
    BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
    Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos);
    for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15))) {
      if (chunk.getBlock((BlockPos)blockpos$mutableblockpos).isOpaqueCube())
        visgraph.func_178606_a((BlockPos)blockpos$mutableblockpos); 
    } 
    return visgraph.func_178609_b(pos);
  }
  
  private RenderChunk func_181562_a(BlockPos p_181562_1_, RenderChunk p_181562_2_, EnumFacing p_181562_3_) {
    BlockPos blockpos = p_181562_2_.getPositionOffset16(p_181562_3_);
    if (blockpos.getY() >= 0 && blockpos.getY() < 256) {
      int i = MathHelper.abs_int(p_181562_1_.getX() - blockpos.getX());
      int j = MathHelper.abs_int(p_181562_1_.getZ() - blockpos.getZ());
      if (Config.isFogOff()) {
        if (i > this.renderDistance || j > this.renderDistance)
          return null; 
      } else {
        int k = i * i + j * j;
        if (k > this.renderDistanceSq)
          return null; 
      } 
      return this.viewFrustum.getRenderChunk(blockpos);
    } 
    return null;
  }
  
  private void fixTerrainFrustum(double x, double y, double z) {
    this.debugFixedClippingHelper = (ClippingHelper)new ClippingHelperImpl();
    ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
    Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
    matrix4f.transpose();
    Matrix4f matrix4f1 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
    matrix4f1.transpose();
    Matrix4f matrix4f2 = new Matrix4f();
    Matrix4f.mul((Matrix4f)matrix4f1, (Matrix4f)matrix4f, (Matrix4f)matrix4f2);
    matrix4f2.invert();
    this.debugTerrainFrustumPosition.field_181059_a = x;
    this.debugTerrainFrustumPosition.field_181060_b = y;
    this.debugTerrainFrustumPosition.field_181061_c = z;
    this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
    this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
    this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
    this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
    this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
    this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
    this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);
    for (int i = 0; i < 8; i++) {
      Matrix4f.transform((Matrix4f)matrix4f2, this.debugTerrainMatrix[i], this.debugTerrainMatrix[i]);
      (this.debugTerrainMatrix[i]).x /= (this.debugTerrainMatrix[i]).w;
      (this.debugTerrainMatrix[i]).y /= (this.debugTerrainMatrix[i]).w;
      (this.debugTerrainMatrix[i]).z /= (this.debugTerrainMatrix[i]).w;
      (this.debugTerrainMatrix[i]).w = 1.0F;
    } 
  }
  
  protected Vector3f getViewVector(Entity entityIn, double partialTicks) {
    float f = (float)(entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
    float f1 = (float)(entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
    if ((Minecraft.getMinecraft()).gameSettings.thirdPersonView == 2)
      f += 180.0F; 
    float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
    float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
    float f4 = -MathHelper.cos(-f * 0.017453292F);
    float f5 = MathHelper.sin(-f * 0.017453292F);
    return new Vector3f(f3 * f4, f5, f2 * f4);
  }
  
  public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn) {
    RenderHelper.disableStandardItemLighting();
    if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT) {
      this.mc.mcProfiler.startSection("translucent_sort");
      double d0 = entityIn.posX - this.prevRenderSortX;
      double d1 = entityIn.posY - this.prevRenderSortY;
      double d2 = entityIn.posZ - this.prevRenderSortZ;
      if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0D) {
        this.prevRenderSortX = entityIn.posX;
        this.prevRenderSortY = entityIn.posY;
        this.prevRenderSortZ = entityIn.posZ;
        int k = 0;
        Iterator<ContainerLocalRenderInformation> iterator = this.renderInfos.iterator();
        this.chunksToResortTransparency.clear();
        while (iterator.hasNext()) {
          ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = iterator.next();
          if (renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && k++ < 15)
            this.chunksToResortTransparency.add(renderglobal$containerlocalrenderinformation.renderChunk); 
        } 
      } 
      this.mc.mcProfiler.endSection();
    } 
    this.mc.mcProfiler.startSection("filterempty");
    int l = 0;
    boolean flag = (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT);
    int i1 = flag ? (this.renderInfos.size() - 1) : 0;
    int i = flag ? -1 : this.renderInfos.size();
    int j1 = flag ? -1 : 1;
    int j;
    for (j = i1; j != i; j += j1) {
      RenderChunk renderchunk = ((ContainerLocalRenderInformation)this.renderInfos.get(j)).renderChunk;
      if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn)) {
        l++;
        this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
      } 
    } 
    if (l == 0) {
      this.mc.mcProfiler.endSection();
      return l;
    } 
    if (Config.isFogOff() && this.mc.entityRenderer.fogStandard)
      GlStateManager.disableFog(); 
    this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
    renderBlockLayer(blockLayerIn);
    this.mc.mcProfiler.endSection();
    return l;
  }
  
  private void renderBlockLayer(EnumWorldBlockLayer blockLayerIn) {
    this.mc.entityRenderer.enableLightmap();
    if (OpenGlHelper.useVbo()) {
      GL11.glEnableClientState(32884);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
      GL11.glEnableClientState(32888);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glEnableClientState(32888);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
      GL11.glEnableClientState(32886);
    } 
    if (Config.isShaders())
      ShadersRender.preRenderChunkLayer(blockLayerIn); 
    this.renderContainer.renderChunkLayer(blockLayerIn);
    if (Config.isShaders())
      ShadersRender.postRenderChunkLayer(blockLayerIn); 
    if (OpenGlHelper.useVbo())
      for (VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements()) {
        VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
        int i = vertexformatelement.getIndex();
        switch (RenderGlobal$2.field_178037_a[vertexformatelement$enumusage.ordinal()]) {
          case 1:
            GL11.glDisableClientState(32884);
          case 2:
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i);
            GL11.glDisableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
          case 3:
            GL11.glDisableClientState(32886);
            GlStateManager.resetColor();
        } 
      }  
    this.mc.entityRenderer.disableLightmap();
  }
  
  private void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> iteratorIn) {
    while (iteratorIn.hasNext()) {
      DestroyBlockProgress destroyblockprogress = iteratorIn.next();
      int i = destroyblockprogress.getCreationCloudUpdateTick();
      if (this.cloudTickCounter - i > 400)
        iteratorIn.remove(); 
    } 
  }
  
  public void updateClouds() {
    if (Config.isShaders() && Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
      Shaders.uninit();
      Shaders.loadShaderPack();
    } 
    this.cloudTickCounter++;
    if (this.cloudTickCounter % 20 == 0)
      cleanupDamagedBlocks(this.damagedBlocks.values().iterator()); 
  }
  
  private void renderSkyEnd() {
    if (Config.isSkyEnabled()) {
      GlStateManager.disableFog();
      GlStateManager.disableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.depthMask(false);
      this.renderEngine.bindTexture(locationEndSkyPng);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      for (int i = 0; i < 6; i++) {
        GlStateManager.pushMatrix();
        if (i == 1)
          GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F); 
        if (i == 2)
          GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F); 
        if (i == 3)
          GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F); 
        if (i == 4)
          GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F); 
        if (i == 5)
          GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F); 
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(40, 40, 40, 255).endVertex();
        worldrenderer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(40, 40, 40, 255).endVertex();
        worldrenderer.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(40, 40, 40, 255).endVertex();
        worldrenderer.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(40, 40, 40, 255).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
      } 
      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.enableAlpha();
    } 
  }
  
  public void renderSky(float partialTicks, int pass) {
    if (Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
      WorldProvider worldprovider = this.mc.theWorld.provider;
      Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
      if (object != null) {
        Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
        return;
      } 
    } 
    if (this.mc.theWorld.provider.getDimensionId() == 1) {
      renderSkyEnd();
    } else if (this.mc.theWorld.provider.isSurfaceWorld()) {
      GlStateManager.disableTexture2D();
      boolean flag1 = Config.isShaders();
      if (flag1)
        Shaders.disableTexture2D(); 
      Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
      vec3 = CustomColors.getSkyColor(vec3, (IBlockAccess)this.mc.theWorld, (this.mc.getRenderViewEntity()).posX, (this.mc.getRenderViewEntity()).posY + 1.0D, (this.mc.getRenderViewEntity()).posZ);
      if (flag1)
        Shaders.setSkyColor(vec3); 
      float f = (float)vec3.xCoord;
      float f1 = (float)vec3.yCoord;
      float f2 = (float)vec3.zCoord;
      if (pass != 2) {
        float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
        float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
        float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
        f = f3;
        f1 = f4;
        f2 = f5;
      } 
      GlStateManager.color(f, f1, f2);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.depthMask(false);
      GlStateManager.enableFog();
      if (flag1)
        Shaders.enableFog(); 
      GlStateManager.color(f, f1, f2);
      if (flag1)
        Shaders.preSkyList(); 
      if (Config.isSkyEnabled())
        if (this.vboEnabled) {
          this.skyVBO.bindBuffer();
          GL11.glEnableClientState(32884);
          GL11.glVertexPointer(3, 5126, 12, 0L);
          this.skyVBO.drawArrays(7);
          this.skyVBO.unbindBuffer();
          GL11.glDisableClientState(32884);
        } else {
          GlStateManager.callList(this.glSkyList);
        }  
      GlStateManager.disableFog();
      if (flag1)
        Shaders.disableFog(); 
      GlStateManager.disableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      RenderHelper.disableStandardItemLighting();
      float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
      if (afloat != null && Config.isSunMoonEnabled()) {
        GlStateManager.disableTexture2D();
        if (flag1)
          Shaders.disableTexture2D(); 
        GlStateManager.shadeModel(7425);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F) ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        float f6 = afloat[0];
        float f7 = afloat[1];
        float f8 = afloat[2];
        if (pass != 2) {
          float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
          float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
          float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
          f6 = f9;
          f7 = f10;
          f8 = f11;
        } 
        worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();
        boolean flag = true;
        for (int i = 0; i <= 16; i++) {
          float f20 = i * 3.1415927F * 2.0F / 16.0F;
          float f12 = MathHelper.sin(f20);
          float f13 = MathHelper.cos(f20);
          worldrenderer.pos((f12 * 120.0F), (f13 * 120.0F), (-f13 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
        } 
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.shadeModel(7424);
      } 
      GlStateManager.enableTexture2D();
      if (flag1)
        Shaders.enableTexture2D(); 
      GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
      GlStateManager.pushMatrix();
      float f15 = 1.0F - this.theWorld.getRainStrength(partialTicks);
      GlStateManager.color(1.0F, 1.0F, 1.0F, f15);
      GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
      CustomSky.renderSky((World)this.theWorld, this.renderEngine, this.theWorld.getCelestialAngle(partialTicks), f15);
      if (flag1)
        Shaders.preCelestialRotate(); 
      GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
      if (flag1)
        Shaders.postCelestialRotate(); 
      float f16 = 30.0F;
      if (Config.isSunTexture()) {
        this.renderEngine.bindTexture(locationSunPng);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-f16, 100.0D, -f16).tex(0.0D, 0.0D).endVertex();
        worldrenderer.pos(f16, 100.0D, -f16).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(f16, 100.0D, f16).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos(-f16, 100.0D, f16).tex(0.0D, 1.0D).endVertex();
        tessellator.draw();
      } 
      f16 = 20.0F;
      if (Config.isMoonTexture()) {
        this.renderEngine.bindTexture(locationMoonPhasesPng);
        int l = this.theWorld.getMoonPhase();
        int j = l % 4;
        int k = l / 4 % 2;
        float f21 = (j + 0) / 4.0F;
        float f22 = (k + 0) / 2.0F;
        float f23 = (j + 1) / 4.0F;
        float f14 = (k + 1) / 2.0F;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-f16, -100.0D, f16).tex(f23, f14).endVertex();
        worldrenderer.pos(f16, -100.0D, f16).tex(f21, f14).endVertex();
        worldrenderer.pos(f16, -100.0D, -f16).tex(f21, f22).endVertex();
        worldrenderer.pos(-f16, -100.0D, -f16).tex(f23, f22).endVertex();
        tessellator.draw();
      } 
      GlStateManager.disableTexture2D();
      if (flag1)
        Shaders.disableTexture2D(); 
      float f24 = this.theWorld.getStarBrightness(partialTicks) * f15;
      if (f24 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers((World)this.theWorld)) {
        GlStateManager.color(f24, f24, f24, f24);
        if (this.vboEnabled) {
          this.starVBO.bindBuffer();
          GL11.glEnableClientState(32884);
          GL11.glVertexPointer(3, 5126, 12, 0L);
          this.starVBO.drawArrays(7);
          this.starVBO.unbindBuffer();
          GL11.glDisableClientState(32884);
        } else {
          GlStateManager.callList(this.starGLCallList);
        } 
      } 
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableFog();
      if (flag1)
        Shaders.enableFog(); 
      GlStateManager.popMatrix();
      GlStateManager.disableTexture2D();
      if (flag1)
        Shaders.disableTexture2D(); 
      GlStateManager.color(0.0F, 0.0F, 0.0F);
      double d0 = (this.mc.thePlayer.getPositionEyes(partialTicks)).yCoord - this.theWorld.getHorizon();
      if (d0 < 0.0D) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 12.0F, 0.0F);
        if (this.vboEnabled) {
          this.sky2VBO.bindBuffer();
          GL11.glEnableClientState(32884);
          GL11.glVertexPointer(3, 5126, 12, 0L);
          this.sky2VBO.drawArrays(7);
          this.sky2VBO.unbindBuffer();
          GL11.glDisableClientState(32884);
        } else {
          GlStateManager.callList(this.glSkyList2);
        } 
        GlStateManager.popMatrix();
        float f17 = 1.0F;
        float f18 = -((float)(d0 + 65.0D));
        float f19 = -1.0F;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(-1.0D, f18, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, f18, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, f18, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, f18, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, f18, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, f18, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, f18, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, f18, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
      } 
      if (this.theWorld.provider.isSkyColored()) {
        GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
      } else {
        GlStateManager.color(f, f1, f2);
      } 
      if (this.mc.gameSettings.renderDistanceChunks <= 4)
        GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue); 
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, -((float)(d0 - 16.0D)), 0.0F);
      if (Config.isSkyEnabled())
        GlStateManager.callList(this.glSkyList2); 
      GlStateManager.popMatrix();
      GlStateManager.enableTexture2D();
      if (flag1)
        Shaders.enableTexture2D(); 
      GlStateManager.depthMask(true);
    } 
  }
  
  public void renderClouds(float partialTicks, int pass) {
    if (!Config.isCloudsOff()) {
      if (Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
        WorldProvider worldprovider = this.mc.theWorld.provider;
        Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
        if (object != null) {
          Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
          return;
        } 
      } 
      if (this.mc.theWorld.provider.isSurfaceWorld()) {
        if (Config.isShaders())
          Shaders.beginClouds(); 
        if (Config.isCloudsFancy()) {
          renderCloudsFancy(partialTicks, pass);
        } else {
          this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, partialTicks);
          partialTicks = 0.0F;
          GlStateManager.disableCull();
          float f9 = (float)((this.mc.getRenderViewEntity()).lastTickPosY + ((this.mc.getRenderViewEntity()).posY - (this.mc.getRenderViewEntity()).lastTickPosY) * partialTicks);
          boolean flag = true;
          boolean flag1 = true;
          Tessellator tessellator = Tessellator.getInstance();
          WorldRenderer worldrenderer = tessellator.getWorldRenderer();
          this.renderEngine.bindTexture(locationCloudsPng);
          GlStateManager.enableBlend();
          GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
          if (this.cloudRenderer.shouldUpdateGlList()) {
            this.cloudRenderer.startUpdateGlList();
            Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
            float f = (float)vec3.xCoord;
            float f1 = (float)vec3.yCoord;
            float f2 = (float)vec3.zCoord;
            if (pass != 2) {
              float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
              float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
              float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
              f = f3;
              f1 = f4;
              f2 = f5;
            } 
            float f10 = 4.8828125E-4F;
            double d2 = (this.cloudTickCounter + partialTicks);
            double d0 = (this.mc.getRenderViewEntity()).prevPosX + ((this.mc.getRenderViewEntity()).posX - (this.mc.getRenderViewEntity()).prevPosX) * partialTicks + d2 * 0.029999999329447746D;
            double d1 = (this.mc.getRenderViewEntity()).prevPosZ + ((this.mc.getRenderViewEntity()).posZ - (this.mc.getRenderViewEntity()).prevPosZ) * partialTicks;
            int i = MathHelper.floor_double(d0 / 2048.0D);
            int j = MathHelper.floor_double(d1 / 2048.0D);
            d0 -= (i * 2048);
            d1 -= (j * 2048);
            float f6 = this.theWorld.provider.getCloudHeight() - f9 + 0.33F;
            f6 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
            float f7 = (float)(d0 * 4.8828125E-4D);
            float f8 = (float)(d1 * 4.8828125E-4D);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            for (int k = -256; k < 256; k += 32) {
              for (int l = -256; l < 256; l += 32) {
                worldrenderer.pos((k + 0), f6, (l + 32)).tex(((k + 0) * 4.8828125E-4F + f7), ((l + 32) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                worldrenderer.pos((k + 32), f6, (l + 32)).tex(((k + 32) * 4.8828125E-4F + f7), ((l + 32) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                worldrenderer.pos((k + 32), f6, (l + 0)).tex(((k + 32) * 4.8828125E-4F + f7), ((l + 0) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                worldrenderer.pos((k + 0), f6, (l + 0)).tex(((k + 0) * 4.8828125E-4F + f7), ((l + 0) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
              } 
            } 
            tessellator.draw();
            this.cloudRenderer.endUpdateGlList();
          } 
          this.cloudRenderer.renderGlList();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.disableBlend();
          GlStateManager.enableCull();
        } 
        if (Config.isShaders())
          Shaders.endClouds(); 
      } 
    } 
  }
  
  public boolean hasCloudFog(double x, double y, double z, float partialTicks) {
    return false;
  }
  
  private void renderCloudsFancy(float partialTicks, int pass) {
    this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks);
    partialTicks = 0.0F;
    GlStateManager.disableCull();
    float f = (float)((this.mc.getRenderViewEntity()).lastTickPosY + ((this.mc.getRenderViewEntity()).posY - (this.mc.getRenderViewEntity()).lastTickPosY) * partialTicks);
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    float f1 = 12.0F;
    float f2 = 4.0F;
    double d0 = (this.cloudTickCounter + partialTicks);
    double d1 = ((this.mc.getRenderViewEntity()).prevPosX + ((this.mc.getRenderViewEntity()).posX - (this.mc.getRenderViewEntity()).prevPosX) * partialTicks + d0 * 0.029999999329447746D) / 12.0D;
    double d2 = ((this.mc.getRenderViewEntity()).prevPosZ + ((this.mc.getRenderViewEntity()).posZ - (this.mc.getRenderViewEntity()).prevPosZ) * partialTicks) / 12.0D + 0.33000001311302185D;
    float f3 = this.theWorld.provider.getCloudHeight() - f + 0.33F;
    f3 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
    int i = MathHelper.floor_double(d1 / 2048.0D);
    int j = MathHelper.floor_double(d2 / 2048.0D);
    d1 -= (i * 2048);
    d2 -= (j * 2048);
    this.renderEngine.bindTexture(locationCloudsPng);
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
    float f4 = (float)vec3.xCoord;
    float f5 = (float)vec3.yCoord;
    float f6 = (float)vec3.zCoord;
    if (pass != 2) {
      float f7 = (f4 * 30.0F + f5 * 59.0F + f6 * 11.0F) / 100.0F;
      float f8 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
      float f9 = (f4 * 30.0F + f6 * 70.0F) / 100.0F;
      f4 = f7;
      f5 = f8;
      f6 = f9;
    } 
    float f26 = f4 * 0.9F;
    float f27 = f5 * 0.9F;
    float f28 = f6 * 0.9F;
    float f10 = f4 * 0.7F;
    float f11 = f5 * 0.7F;
    float f12 = f6 * 0.7F;
    float f13 = f4 * 0.8F;
    float f14 = f5 * 0.8F;
    float f15 = f6 * 0.8F;
    float f16 = 0.00390625F;
    float f17 = MathHelper.floor_double(d1) * 0.00390625F;
    float f18 = MathHelper.floor_double(d2) * 0.00390625F;
    float f19 = (float)(d1 - MathHelper.floor_double(d1));
    float f20 = (float)(d2 - MathHelper.floor_double(d2));
    boolean flag = true;
    boolean flag1 = true;
    float f21 = 9.765625E-4F;
    GlStateManager.scale(12.0F, 1.0F, 12.0F);
    for (int k = 0; k < 2; k++) {
      if (k == 0) {
        GlStateManager.colorMask(false, false, false, false);
      } else {
        switch (pass) {
          case 0:
            GlStateManager.colorMask(false, true, true, true);
            break;
          case 1:
            GlStateManager.colorMask(true, false, false, true);
            break;
          case 2:
            GlStateManager.colorMask(true, true, true, true);
            break;
        } 
      } 
      this.cloudRenderer.renderGlList();
    } 
    if (this.cloudRenderer.shouldUpdateGlList()) {
      this.cloudRenderer.startUpdateGlList();
      for (int j1 = -3; j1 <= 4; j1++) {
        for (int l = -3; l <= 4; l++) {
          worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
          float f22 = (j1 * 8);
          float f23 = (l * 8);
          float f24 = f22 - f19;
          float f25 = f23 - f20;
          if (f3 > -5.0F) {
            worldrenderer.pos((f24 + 0.0F), (f3 + 0.0F), (f25 + 8.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
            worldrenderer.pos((f24 + 8.0F), (f3 + 0.0F), (f25 + 8.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
            worldrenderer.pos((f24 + 8.0F), (f3 + 0.0F), (f25 + 0.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
            worldrenderer.pos((f24 + 0.0F), (f3 + 0.0F), (f25 + 0.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
          } 
          if (f3 <= 5.0F) {
            worldrenderer.pos((f24 + 0.0F), (f3 + 4.0F - 9.765625E-4F), (f25 + 8.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
            worldrenderer.pos((f24 + 8.0F), (f3 + 4.0F - 9.765625E-4F), (f25 + 8.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
            worldrenderer.pos((f24 + 8.0F), (f3 + 4.0F - 9.765625E-4F), (f25 + 0.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
            worldrenderer.pos((f24 + 0.0F), (f3 + 4.0F - 9.765625E-4F), (f25 + 0.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
          } 
          if (j1 > -1)
            for (int i1 = 0; i1 < 8; i1++) {
              worldrenderer.pos((f24 + i1 + 0.0F), (f3 + 0.0F), (f25 + 8.0F)).tex(((f22 + i1 + 0.5F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
              worldrenderer.pos((f24 + i1 + 0.0F), (f3 + 4.0F), (f25 + 8.0F)).tex(((f22 + i1 + 0.5F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
              worldrenderer.pos((f24 + i1 + 0.0F), (f3 + 4.0F), (f25 + 0.0F)).tex(((f22 + i1 + 0.5F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
              worldrenderer.pos((f24 + i1 + 0.0F), (f3 + 0.0F), (f25 + 0.0F)).tex(((f22 + i1 + 0.5F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
            }  
          if (j1 <= 1)
            for (int k1 = 0; k1 < 8; k1++) {
              worldrenderer.pos((f24 + k1 + 1.0F - 9.765625E-4F), (f3 + 0.0F), (f25 + 8.0F)).tex(((f22 + k1 + 0.5F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
              worldrenderer.pos((f24 + k1 + 1.0F - 9.765625E-4F), (f3 + 4.0F), (f25 + 8.0F)).tex(((f22 + k1 + 0.5F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
              worldrenderer.pos((f24 + k1 + 1.0F - 9.765625E-4F), (f3 + 4.0F), (f25 + 0.0F)).tex(((f22 + k1 + 0.5F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
              worldrenderer.pos((f24 + k1 + 1.0F - 9.765625E-4F), (f3 + 0.0F), (f25 + 0.0F)).tex(((f22 + k1 + 0.5F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
            }  
          if (l > -1)
            for (int l1 = 0; l1 < 8; l1++) {
              worldrenderer.pos((f24 + 0.0F), (f3 + 4.0F), (f25 + l1 + 0.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + l1 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
              worldrenderer.pos((f24 + 8.0F), (f3 + 4.0F), (f25 + l1 + 0.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + l1 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
              worldrenderer.pos((f24 + 8.0F), (f3 + 0.0F), (f25 + l1 + 0.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + l1 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
              worldrenderer.pos((f24 + 0.0F), (f3 + 0.0F), (f25 + l1 + 0.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + l1 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
            }  
          if (l <= 1)
            for (int i2 = 0; i2 < 8; i2++) {
              worldrenderer.pos((f24 + 0.0F), (f3 + 4.0F), (f25 + i2 + 1.0F - 9.765625E-4F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + i2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
              worldrenderer.pos((f24 + 8.0F), (f3 + 4.0F), (f25 + i2 + 1.0F - 9.765625E-4F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + i2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
              worldrenderer.pos((f24 + 8.0F), (f3 + 0.0F), (f25 + i2 + 1.0F - 9.765625E-4F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + i2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
              worldrenderer.pos((f24 + 0.0F), (f3 + 0.0F), (f25 + i2 + 1.0F - 9.765625E-4F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + i2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
            }  
          tessellator.draw();
        } 
      } 
      this.cloudRenderer.endUpdateGlList();
    } 
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.disableBlend();
    GlStateManager.enableCull();
  }
  
  public void updateChunks(long finishTimeNano) {
    finishTimeNano = (long)(finishTimeNano + 1.0E8D);
    this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
    if (this.chunksToUpdateForced.size() > 0) {
      Iterator<RenderChunk> iterator = this.chunksToUpdateForced.iterator();
      while (iterator.hasNext()) {
        RenderChunk renderchunk = iterator.next();
        if (!this.renderDispatcher.updateChunkLater(renderchunk))
          break; 
        renderchunk.setNeedsUpdate(false);
        iterator.remove();
        this.chunksToUpdate.remove(renderchunk);
        this.chunksToResortTransparency.remove(renderchunk);
      } 
    } 
    if (this.chunksToResortTransparency.size() > 0) {
      Iterator<RenderChunk> iterator2 = this.chunksToResortTransparency.iterator();
      if (iterator2.hasNext()) {
        RenderChunk renderchunk2 = iterator2.next();
        if (this.renderDispatcher.updateTransparencyLater(renderchunk2))
          iterator2.remove(); 
      } 
    } 
    int j = 0;
    int k = Config.getUpdatesPerFrame();
    int i = k * 2;
    Iterator<RenderChunk> iterator1 = this.chunksToUpdate.iterator();
    while (iterator1.hasNext()) {
      RenderChunk renderchunk1 = iterator1.next();
      if (!this.renderDispatcher.updateChunkLater(renderchunk1))
        break; 
      renderchunk1.setNeedsUpdate(false);
      iterator1.remove();
      if (renderchunk1.getCompiledChunk().isEmpty() && k < i)
        k++; 
      j++;
      if (j >= k)
        break; 
    } 
  }
  
  public void renderWorldBorder(Entity p_180449_1_, float partialTicks) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    WorldBorder worldborder = this.theWorld.getWorldBorder();
    double d0 = (this.mc.gameSettings.renderDistanceChunks * 16);
    if (p_180449_1_.posX >= worldborder.maxX() - d0 || p_180449_1_.posX <= worldborder.minX() + d0 || p_180449_1_.posZ >= worldborder.maxZ() - d0 || p_180449_1_.posZ <= worldborder.minZ() + d0) {
      double d1 = 1.0D - worldborder.getClosestDistance(p_180449_1_) / d0;
      d1 = Math.pow(d1, 4.0D);
      double d2 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * partialTicks;
      double d3 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * partialTicks;
      double d4 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * partialTicks;
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
      this.renderEngine.bindTexture(locationForcefieldPng);
      GlStateManager.depthMask(false);
      GlStateManager.pushMatrix();
      int i = worldborder.getStatus().getID();
      float f = (i >> 16 & 0xFF) / 255.0F;
      float f1 = (i >> 8 & 0xFF) / 255.0F;
      float f2 = (i & 0xFF) / 255.0F;
      GlStateManager.color(f, f1, f2, (float)d1);
      GlStateManager.doPolygonOffset(-3.0F, -3.0F);
      GlStateManager.enablePolygonOffset();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableAlpha();
      GlStateManager.disableCull();
      float f3 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
      float f4 = 0.0F;
      float f5 = 0.0F;
      float f6 = 128.0F;
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
      worldrenderer.setTranslation(-d2, -d3, -d4);
      double d5 = Math.max(MathHelper.floor_double(d4 - d0), worldborder.minZ());
      double d6 = Math.min(MathHelper.ceiling_double_int(d4 + d0), worldborder.maxZ());
      if (d2 > worldborder.maxX() - d0) {
        float f7 = 0.0F;
        for (double d7 = d5; d7 < d6; f7 += 0.5F) {
          double d8 = Math.min(1.0D, d6 - d7);
          float f8 = (float)d8 * 0.5F;
          worldrenderer.pos(worldborder.maxX(), 256.0D, d7).tex((f3 + f7), (f3 + 0.0F)).endVertex();
          worldrenderer.pos(worldborder.maxX(), 256.0D, d7 + d8).tex((f3 + f8 + f7), (f3 + 0.0F)).endVertex();
          worldrenderer.pos(worldborder.maxX(), 0.0D, d7 + d8).tex((f3 + f8 + f7), (f3 + 128.0F)).endVertex();
          worldrenderer.pos(worldborder.maxX(), 0.0D, d7).tex((f3 + f7), (f3 + 128.0F)).endVertex();
          d7++;
        } 
      } 
      if (d2 < worldborder.minX() + d0) {
        float f9 = 0.0F;
        for (double d9 = d5; d9 < d6; f9 += 0.5F) {
          double d12 = Math.min(1.0D, d6 - d9);
          float f12 = (float)d12 * 0.5F;
          worldrenderer.pos(worldborder.minX(), 256.0D, d9).tex((f3 + f9), (f3 + 0.0F)).endVertex();
          worldrenderer.pos(worldborder.minX(), 256.0D, d9 + d12).tex((f3 + f12 + f9), (f3 + 0.0F)).endVertex();
          worldrenderer.pos(worldborder.minX(), 0.0D, d9 + d12).tex((f3 + f12 + f9), (f3 + 128.0F)).endVertex();
          worldrenderer.pos(worldborder.minX(), 0.0D, d9).tex((f3 + f9), (f3 + 128.0F)).endVertex();
          d9++;
        } 
      } 
      d5 = Math.max(MathHelper.floor_double(d2 - d0), worldborder.minX());
      d6 = Math.min(MathHelper.ceiling_double_int(d2 + d0), worldborder.maxX());
      if (d4 > worldborder.maxZ() - d0) {
        float f10 = 0.0F;
        for (double d10 = d5; d10 < d6; f10 += 0.5F) {
          double d13 = Math.min(1.0D, d6 - d10);
          float f13 = (float)d13 * 0.5F;
          worldrenderer.pos(d10, 256.0D, worldborder.maxZ()).tex((f3 + f10), (f3 + 0.0F)).endVertex();
          worldrenderer.pos(d10 + d13, 256.0D, worldborder.maxZ()).tex((f3 + f13 + f10), (f3 + 0.0F)).endVertex();
          worldrenderer.pos(d10 + d13, 0.0D, worldborder.maxZ()).tex((f3 + f13 + f10), (f3 + 128.0F)).endVertex();
          worldrenderer.pos(d10, 0.0D, worldborder.maxZ()).tex((f3 + f10), (f3 + 128.0F)).endVertex();
          d10++;
        } 
      } 
      if (d4 < worldborder.minZ() + d0) {
        float f11 = 0.0F;
        for (double d11 = d5; d11 < d6; f11 += 0.5F) {
          double d14 = Math.min(1.0D, d6 - d11);
          float f14 = (float)d14 * 0.5F;
          worldrenderer.pos(d11, 256.0D, worldborder.minZ()).tex((f3 + f11), (f3 + 0.0F)).endVertex();
          worldrenderer.pos(d11 + d14, 256.0D, worldborder.minZ()).tex((f3 + f14 + f11), (f3 + 0.0F)).endVertex();
          worldrenderer.pos(d11 + d14, 0.0D, worldborder.minZ()).tex((f3 + f14 + f11), (f3 + 128.0F)).endVertex();
          worldrenderer.pos(d11, 0.0D, worldborder.minZ()).tex((f3 + f11), (f3 + 128.0F)).endVertex();
          d11++;
        } 
      } 
      tessellator.draw();
      worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
      GlStateManager.enableCull();
      GlStateManager.disableAlpha();
      GlStateManager.doPolygonOffset(0.0F, 0.0F);
      GlStateManager.disablePolygonOffset();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
      GlStateManager.depthMask(true);
    } 
  }
  
  private void preRenderDamagedBlocks() {
    GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
    GlStateManager.enableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
    GlStateManager.doPolygonOffset(-3.0F, -3.0F);
    GlStateManager.enablePolygonOffset();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableAlpha();
    GlStateManager.pushMatrix();
    if (Config.isShaders())
      ShadersRender.beginBlockDamage(); 
  }
  
  private void postRenderDamagedBlocks() {
    GlStateManager.disableAlpha();
    GlStateManager.doPolygonOffset(0.0F, 0.0F);
    GlStateManager.disablePolygonOffset();
    GlStateManager.enableAlpha();
    GlStateManager.depthMask(true);
    GlStateManager.popMatrix();
    if (Config.isShaders())
      ShadersRender.endBlockDamage(); 
  }
  
  public void drawBlockDamageTexture(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks) {
    double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
    double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
    double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
    if (!this.damagedBlocks.isEmpty()) {
      this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      preRenderDamagedBlocks();
      worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
      worldRendererIn.setTranslation(-d0, -d1, -d2);
      worldRendererIn.markDirty();
      Iterator<DestroyBlockProgress> iterator = this.damagedBlocks.values().iterator();
      while (iterator.hasNext()) {
        boolean flag;
        DestroyBlockProgress destroyblockprogress = iterator.next();
        BlockPos blockpos = destroyblockprogress.getPosition();
        double d3 = blockpos.getX() - d0;
        double d4 = blockpos.getY() - d1;
        double d5 = blockpos.getZ() - d2;
        Block block = this.theWorld.getBlockState(blockpos).getBlock();
        if (Reflector.ForgeTileEntity_canRenderBreaking.exists()) {
          boolean flag1 = (block instanceof net.minecraft.block.BlockChest || block instanceof net.minecraft.block.BlockEnderChest || block instanceof net.minecraft.block.BlockSign || block instanceof net.minecraft.block.BlockSkull);
          if (!flag1) {
            TileEntity tileentity = this.theWorld.getTileEntity(blockpos);
            if (tileentity != null)
              flag1 = Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]); 
          } 
          flag = !flag1;
        } else {
          flag = (!(block instanceof net.minecraft.block.BlockChest) && !(block instanceof net.minecraft.block.BlockEnderChest) && !(block instanceof net.minecraft.block.BlockSign) && !(block instanceof net.minecraft.block.BlockSkull));
        } 
        if (flag) {
          if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D) {
            iterator.remove();
            continue;
          } 
          IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
          if (iblockstate.getBlock().getMaterial() != Material.air) {
            int i = destroyblockprogress.getPartialBlockDamage();
            TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];
            BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
            blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, (IBlockAccess)this.theWorld);
          } 
        } 
      } 
      tessellatorIn.draw();
      worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
      postRenderDamagedBlocks();
    } 
  }
  
  public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int p_72731_3_, float partialTicks) {
    if (p_72731_3_ == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
      GL11.glLineWidth(2.0F);
      GlStateManager.disableTexture2D();
      if (Config.isShaders())
        Shaders.disableTexture2D(); 
      GlStateManager.depthMask(false);
      float f = 0.002F;
      BlockPos blockpos = movingObjectPositionIn.getBlockPos();
      Block block = this.theWorld.getBlockState(blockpos).getBlock();
      if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockpos)) {
        block.setBlockBoundsBasedOnState((IBlockAccess)this.theWorld, blockpos);
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        func_181561_a(block.getSelectedBoundingBox((World)this.theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
      } 
      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      if (Config.isShaders())
        Shaders.enableTexture2D(); 
      GlStateManager.disableBlend();
    } 
  }
  
  public static void func_181561_a(AxisAlignedBB p_181561_0_) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
    worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
    worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
    worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
    worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(1, DefaultVertexFormats.POSITION);
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
    worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
    worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
    worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
    worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
    worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
    tessellator.draw();
  }
  
  public static void func_181563_a(AxisAlignedBB p_181563_0_, int p_181563_1_, int p_181563_2_, int p_181563_3_, int p_181563_4_) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    tessellator.draw();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    tessellator.draw();
    worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
    tessellator.draw();
  }
  
  private void markBlocksForUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    this.viewFrustum.markBlocksForUpdate(x1, y1, z1, x2, y2, z2);
  }
  
  public void markBlockForUpdate(BlockPos pos) {
    int i = pos.getX();
    int j = pos.getY();
    int k = pos.getZ();
    markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
  }
  
  public void notifyLightSet(BlockPos pos) {
    int i = pos.getX();
    int j = pos.getY();
    int k = pos.getZ();
    markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
  }
  
  public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
  }
  
  public void playRecord(String recordName, BlockPos blockPosIn) {
    ISound isound = (ISound)this.mapSoundPositions.get(blockPosIn);
    if (isound != null) {
      this.mc.getSoundHandler().stopSound(isound);
      this.mapSoundPositions.remove(blockPosIn);
    } 
    if (recordName != null) {
      ItemRecord itemrecord = ItemRecord.getRecord(recordName);
      if (itemrecord != null)
        this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal()); 
      ResourceLocation resourcelocation = null;
      if (Reflector.ForgeItemRecord_getRecordResource.exists() && itemrecord != null)
        resourcelocation = (ResourceLocation)Reflector.call(itemrecord, Reflector.ForgeItemRecord_getRecordResource, new Object[] { recordName }); 
      if (resourcelocation == null)
        resourcelocation = new ResourceLocation(recordName); 
      PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.create(resourcelocation, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ());
      this.mapSoundPositions.put(blockPosIn, positionedsoundrecord);
      this.mc.getSoundHandler().playSound((ISound)positionedsoundrecord);
    } 
  }
  
  public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {}
  
  public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {}
  
  public void spawnParticle(int particleID, boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, double xOffset, double yOffset, double zOffset, int... p_180442_15_) {
    try {
      spawnEntityFX(particleID, ignoreRange, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_180442_15_);
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
      crashreportcategory.addCrashSection("ID", Integer.valueOf(particleID));
      if (p_180442_15_ != null)
        crashreportcategory.addCrashSection("Parameters", p_180442_15_); 
      crashreportcategory.addCrashSectionCallable("Position", new Callable() {
            private static final String __OBFID = "CL_00000955";
            
            public String call() throws Exception {
              return CrashReportCategory.getCoordinateInfo(xCoord, yCoord, zCoord);
            }
          });
      throw new ReportedException(crashreport);
    } 
  }
  
  private void spawnParticle(EnumParticleTypes particleIn, double p_174972_2_, double p_174972_4_, double p_174972_6_, double p_174972_8_, double p_174972_10_, double p_174972_12_, int... p_174972_14_) {
    spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
  }
  
  private EntityFX spawnEntityFX(int p_174974_1_, boolean ignoreRange, double p_174974_3_, double p_174974_5_, double p_174974_7_, double p_174974_9_, double p_174974_11_, double p_174974_13_, int... p_174974_15_) {
    if (this.mc != null && this.mc.getRenderViewEntity() != null && this.mc.effectRenderer != null) {
      int i = this.mc.gameSettings.particleSetting;
      if (i == 1 && this.theWorld.rand.nextInt(3) == 0)
        i = 2; 
      double d0 = (this.mc.getRenderViewEntity()).posX - p_174974_3_;
      double d1 = (this.mc.getRenderViewEntity()).posY - p_174974_5_;
      double d2 = (this.mc.getRenderViewEntity()).posZ - p_174974_7_;
      if (p_174974_1_ == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID() && !Config.isAnimatedPortal())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava())
        return null; 
      if (p_174974_1_ == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles())
        return null; 
      if (ignoreRange)
        return this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_); 
      double d3 = 16.0D;
      double d4 = 256.0D;
      if (p_174974_1_ == EnumParticleTypes.CRIT.getParticleID())
        d4 = 38416.0D; 
      if (d0 * d0 + d1 * d1 + d2 * d2 > d4)
        return null; 
      if (i > 1)
        return null; 
      EntityFX entityfx = this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
      if (p_174974_1_ == EnumParticleTypes.WATER_BUBBLE.getParticleID())
        CustomColors.updateWaterFX(entityfx, (IBlockAccess)this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_); 
      if (p_174974_1_ == EnumParticleTypes.WATER_SPLASH.getParticleID())
        CustomColors.updateWaterFX(entityfx, (IBlockAccess)this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_); 
      if (p_174974_1_ == EnumParticleTypes.WATER_DROP.getParticleID())
        CustomColors.updateWaterFX(entityfx, (IBlockAccess)this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_); 
      if (p_174974_1_ == EnumParticleTypes.TOWN_AURA.getParticleID())
        CustomColors.updateMyceliumFX(entityfx); 
      if (p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID())
        CustomColors.updatePortalFX(entityfx); 
      if (p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID())
        CustomColors.updateReddustFX(entityfx, (IBlockAccess)this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_); 
      return entityfx;
    } 
    return null;
  }
  
  public void onEntityAdded(Entity entityIn) {
    RandomMobs.entityLoaded(entityIn, (World)this.theWorld);
    if (Config.isDynamicLights())
      DynamicLights.entityAdded(entityIn, this); 
  }
  
  public void onEntityRemoved(Entity entityIn) {
    if (Config.isDynamicLights())
      DynamicLights.entityRemoved(entityIn, this); 
  }
  
  public void deleteAllDisplayLists() {}
  
  public void broadcastSound(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_) {
    switch (p_180440_1_) {
      case 1013:
      case 1018:
        if (this.mc.getRenderViewEntity() != null) {
          double d0 = p_180440_2_.getX() - (this.mc.getRenderViewEntity()).posX;
          double d1 = p_180440_2_.getY() - (this.mc.getRenderViewEntity()).posY;
          double d2 = p_180440_2_.getZ() - (this.mc.getRenderViewEntity()).posZ;
          double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
          double d4 = (this.mc.getRenderViewEntity()).posX;
          double d5 = (this.mc.getRenderViewEntity()).posY;
          double d6 = (this.mc.getRenderViewEntity()).posZ;
          if (d3 > 0.0D) {
            d4 += d0 / d3 * 2.0D;
            d5 += d1 / d3 * 2.0D;
            d6 += d2 / d3 * 2.0D;
          } 
          if (p_180440_1_ == 1013) {
            this.theWorld.playSound(d4, d5, d6, "mob.wither.spawn", 1.0F, 1.0F, false);
            break;
          } 
          this.theWorld.playSound(d4, d5, d6, "mob.enderdragon.end", 5.0F, 1.0F, false);
        } 
        break;
    } 
  }
  
  public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int p_180439_4_) {
    int k, l;
    double d13, d15, d19;
    int l1;
    Block block;
    double d11, d12, d14;
    int i1, j1;
    float f, f1, f2;
    EnumParticleTypes enumparticletypes;
    int k1;
    double var7, var9, var11;
    int var13;
    double var32;
    int var18;
    Random random = this.theWorld.rand;
    switch (sfxType) {
      case 1000:
        this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.0F, false);
        break;
      case 1001:
        this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.2F, false);
        break;
      case 1002:
        this.theWorld.playSoundAtPos(blockPosIn, "random.bow", 1.0F, 1.2F, false);
        break;
      case 1003:
        this.theWorld.playSoundAtPos(blockPosIn, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1004:
        this.theWorld.playSoundAtPos(blockPosIn, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
        break;
      case 1005:
        if (Item.getItemById(p_180439_4_) instanceof ItemRecord) {
          this.theWorld.playRecord(blockPosIn, "records." + ((ItemRecord)Item.getItemById(p_180439_4_)).recordName);
          break;
        } 
        this.theWorld.playRecord(blockPosIn, (String)null);
        break;
      case 1006:
        this.theWorld.playSoundAtPos(blockPosIn, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1007:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.charge", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1008:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1009:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1010:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.wood", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1011:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.metal", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1012:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.woodbreak", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1014:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.wither.shoot", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1015:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.bat.takeoff", 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1016:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.infect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1017:
        this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.unfect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1020:
        this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1021:
        this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1022:
        this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 2000:
        k = p_180439_4_ % 3 - 1;
        l = p_180439_4_ / 3 % 3 - 1;
        d13 = blockPosIn.getX() + k * 0.6D + 0.5D;
        d15 = blockPosIn.getY() + 0.5D;
        d19 = blockPosIn.getZ() + l * 0.6D + 0.5D;
        for (l1 = 0; l1 < 10; l1++) {
          double d20 = random.nextDouble() * 0.2D + 0.01D;
          double d21 = d13 + k * 0.01D + (random.nextDouble() - 0.5D) * l * 0.5D;
          double d22 = d15 + (random.nextDouble() - 0.5D) * 0.5D;
          double d23 = d19 + l * 0.01D + (random.nextDouble() - 0.5D) * k * 0.5D;
          double d24 = k * d20 + random.nextGaussian() * 0.01D;
          double d9 = -0.03D + random.nextGaussian() * 0.01D;
          double d10 = l * d20 + random.nextGaussian() * 0.01D;
          spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d21, d22, d23, d24, d9, d10, new int[0]);
        } 
        return;
      case 2001:
        block = Block.getBlockById(p_180439_4_ & 0xFFF);
        if (block.getMaterial() != Material.air)
          this.mc.getSoundHandler().playSound((ISound)new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getFrequency() * 0.8F, blockPosIn.getX() + 0.5F, blockPosIn.getY() + 0.5F, blockPosIn.getZ() + 0.5F)); 
        this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(p_180439_4_ >> 12 & 0xFF));
        break;
      case 2002:
        d11 = blockPosIn.getX();
        d12 = blockPosIn.getY();
        d14 = blockPosIn.getZ();
        for (i1 = 0; i1 < 8; i1++) {
          spawnParticle(EnumParticleTypes.ITEM_CRACK, d11, d12, d14, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem((Item)Items.potionitem), p_180439_4_ });
        } 
        j1 = Items.potionitem.getColorFromDamage(p_180439_4_);
        f = (j1 >> 16 & 0xFF) / 255.0F;
        f1 = (j1 >> 8 & 0xFF) / 255.0F;
        f2 = (j1 >> 0 & 0xFF) / 255.0F;
        enumparticletypes = EnumParticleTypes.SPELL;
        if (Items.potionitem.isEffectInstant(p_180439_4_))
          enumparticletypes = EnumParticleTypes.SPELL_INSTANT; 
        for (k1 = 0; k1 < 100; k1++) {
          double d16 = random.nextDouble() * 4.0D;
          double d17 = random.nextDouble() * Math.PI * 2.0D;
          double d18 = Math.cos(d17) * d16;
          double d7 = 0.01D + random.nextDouble() * 0.5D;
          double d8 = Math.sin(d17) * d16;
          EntityFX entityfx = spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d11 + d18 * 0.1D, d12 + 0.3D, d14 + d8 * 0.1D, d18, d7, d8, new int[0]);
          if (entityfx != null) {
            float f3 = 0.75F + random.nextFloat() * 0.25F;
            entityfx.setRBGColorF(f * f3, f1 * f3, f2 * f3);
            entityfx.multiplyVelocity((float)d16);
          } 
        } 
        this.theWorld.playSoundAtPos(blockPosIn, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 2003:
        var7 = blockPosIn.getX() + 0.5D;
        var9 = blockPosIn.getY();
        var11 = blockPosIn.getZ() + 0.5D;
        for (var13 = 0; var13 < 8; var13++) {
          spawnParticle(EnumParticleTypes.ITEM_CRACK, var7, var9, var11, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem(Items.ender_eye) });
        } 
        for (var32 = 0.0D; var32 < 6.283185307179586D; var32 += 0.15707963267948966D) {
          spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var32) * 5.0D, var9 - 0.4D, var11 + Math.sin(var32) * 5.0D, Math.cos(var32) * -5.0D, 0.0D, Math.sin(var32) * -5.0D, new int[0]);
          spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var32) * 5.0D, var9 - 0.4D, var11 + Math.sin(var32) * 5.0D, Math.cos(var32) * -7.0D, 0.0D, Math.sin(var32) * -7.0D, new int[0]);
        } 
        return;
      case 2004:
        for (var18 = 0; var18 < 20; var18++) {
          double d3 = blockPosIn.getX() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
          double d4 = blockPosIn.getY() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
          double d5 = blockPosIn.getZ() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
          this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
          this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
        } 
        return;
      case 2005:
        ItemDye.spawnBonemealParticles((World)this.theWorld, blockPosIn, p_180439_4_);
        break;
    } 
  }
  
  public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
    if (progress >= 0 && progress < 10) {
      DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)this.damagedBlocks.get(Integer.valueOf(breakerId));
      if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ()) {
        destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
        this.damagedBlocks.put(Integer.valueOf(breakerId), destroyblockprogress);
      } 
      destroyblockprogress.setPartialBlockDamage(progress);
      destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
    } else {
      this.damagedBlocks.remove(Integer.valueOf(breakerId));
    } 
  }
  
  public void setDisplayListEntitiesDirty() {
    this.displayListEntitiesDirty = true;
  }
  
  public void resetClouds() {
    this.cloudRenderer.reset();
  }
  
  public int getCountRenderers() {
    return this.viewFrustum.renderChunks.length;
  }
  
  public int getCountActiveRenderers() {
    return this.renderInfos.size();
  }
  
  public int getCountEntitiesRendered() {
    return this.countEntitiesRendered;
  }
  
  public int getCountTileEntitiesRendered() {
    return this.countTileEntitiesRendered;
  }
  
  public RenderChunk getRenderChunk(BlockPos p_getRenderChunk_1_) {
    return this.viewFrustum.getRenderChunk(p_getRenderChunk_1_);
  }
  
  public RenderChunk getRenderChunk(RenderChunk p_getRenderChunk_1_, EnumFacing p_getRenderChunk_2_) {
    if (p_getRenderChunk_1_ == null)
      return null; 
    BlockPos blockpos = p_getRenderChunk_1_.func_181701_a(p_getRenderChunk_2_);
    return this.viewFrustum.getRenderChunk(blockpos);
  }
  
  public WorldClient getWorld() {
    return this.theWorld;
  }
  
  public void func_181023_a(Collection<?> p_181023_1_, Collection p_181023_2_) {
    Set set = this.field_181024_n;
    synchronized (this.field_181024_n) {
      this.field_181024_n.removeAll(p_181023_1_);
      this.field_181024_n.addAll(p_181023_2_);
    } 
  }
  
  static final class RenderGlobal$2 {
    static final int[] field_178037_a = new int[(VertexFormatElement.EnumUsage.values()).length];
    
    private static final String __OBFID = "CL_00002535";
    
    static {
      try {
        field_178037_a[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_178037_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_178037_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {}
    }
  }
  
  public static class ContainerLocalRenderInformation {
    final RenderChunk renderChunk;
    
    final EnumFacing facing;
    
    final Set setFacing;
    
    final int counter;
    
    private static final String __OBFID = "CL_00002534";
    
    public ContainerLocalRenderInformation(RenderChunk p_i4_1_, EnumFacing p_i4_2_, int p_i4_3_) {
      this.setFacing = EnumSet.noneOf(EnumFacing.class);
      this.renderChunk = p_i4_1_;
      this.facing = p_i4_2_;
      this.counter = p_i4_3_;
    }
    
    ContainerLocalRenderInformation(RenderChunk p_i5_1_, EnumFacing p_i5_2_, int p_i5_3_, Object p_i5_4_) {
      this(p_i5_1_, p_i5_2_, p_i5_3_);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\RenderGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */