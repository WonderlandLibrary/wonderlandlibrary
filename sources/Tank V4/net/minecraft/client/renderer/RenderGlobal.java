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
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
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
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
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
import net.minecraft.world.IWorldAccess;
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
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;
import shadersmod.client.ShadowUtils;

public class RenderGlobal implements IWorldAccess, IResourceManagerReloadListener {
   private int starGLCallList = -1;
   private final Set field_181024_n = Sets.newHashSet();
   private List renderInfosTileEntitiesNormal = new ArrayList(1024);
   private int glSkyList = -1;
   private List renderInfosTileEntitiesShadow = new ArrayList(1024);
   private Set chunksToUpdate = Sets.newLinkedHashSet();
   private int glSkyList2 = -1;
   private List renderInfosTileEntities = new ArrayList(1024);
   private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
   private boolean vboEnabled = false;
   private int renderEntitiesStartupCounter = 2;
   private final RenderManager renderManager;
   private final Vector3d debugTerrainFrustumPosition = new Vector3d();
   private int countEntitiesTotal;
   private int countTileEntitiesRendered;
   private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
   private static final String __OBFID = "CL_00000954";
   private static final Set SET_ALL_FACINGS;
   private int countEntitiesRendered;
   private double frustumUpdatePosY = Double.MIN_VALUE;
   private WorldClient theWorld;
   private int renderDistanceSq = 0;
   private ShaderGroup entityOutlineShader;
   public Entity renderedEntity;
   private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
   private int renderDistanceChunks = -1;
   public Set chunksToResortTransparency = new LinkedHashSet();
   private double prevRenderSortX;
   private double lastViewEntityZ = Double.MIN_VALUE;
   private ClippingHelper debugFixedClippingHelper;
   private VertexBuffer starVBO;
   private double frustumUpdatePosX = Double.MIN_VALUE;
   private ChunkRenderContainer renderContainer;
   private VertexBuffer sky2VBO;
   private CloudRenderer cloudRenderer;
   public final Minecraft mc;
   private double lastViewEntityY = Double.MIN_VALUE;
   private int cloudTickCounter;
   private VertexBuffer skyVBO;
   private boolean debugFixTerrainFrustum = false;
   private double lastViewEntityX = Double.MIN_VALUE;
   public final Map damagedBlocks = Maps.newHashMap();
   private final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
   private List renderInfosEntities = new ArrayList(1024);
   private List renderInfos = Lists.newArrayListWithCapacity(69696);
   private List renderInfosEntitiesShadow = new ArrayList(1024);
   private static final Logger logger = LogManager.getLogger();
   private ViewFrustum viewFrustum;
   public static TextureManager renderEngine;
   private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
   private int renderDistance = 0;
   private double prevRenderSortZ;
   private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
   private double lastViewEntityPitch = Double.MIN_VALUE;
   private Framebuffer entityOutlineFramebuffer;
   IRenderChunkFactory renderChunkFactory;
   private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
   private List renderInfosEntitiesNormal = new ArrayList(1024);
   private double frustumUpdatePosZ = Double.MIN_VALUE;
   private int countEntitiesHidden;
   private static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
   private double prevRenderSortY;
   private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
   private Deque visibilityDeque = new ArrayDeque();
   private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
   private List renderInfosShadow = new ArrayList(1024);
   public Set chunksToUpdateForced = new LinkedHashSet();
   public boolean displayListEntitiesDirty = true;
   private VertexFormat vertexBufferFormat;
   private final Map mapSoundPositions = Maps.newHashMap();
   private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
   private List renderInfosNormal = new ArrayList(1024);
   private double lastViewEntityYaw = Double.MIN_VALUE;

   public void sendBlockBreakProgress(int var1, BlockPos var2, int var3) {
      if (var3 >= 0 && var3 < 10) {
         DestroyBlockProgress var4 = (DestroyBlockProgress)this.damagedBlocks.get(var1);
         if (var4 == null || var4.getPosition().getX() != var2.getX() || var4.getPosition().getY() != var2.getY() || var4.getPosition().getZ() != var2.getZ()) {
            var4 = new DestroyBlockProgress(var1, var2);
            this.damagedBlocks.put(var1, var4);
         }

         var4.setPartialBlockDamage(var3);
         var4.setCloudUpdateTick(this.cloudTickCounter);
      } else {
         this.damagedBlocks.remove(var1);
      }

   }

   public void func_181023_a(Collection var1, Collection var2) {
      Set var3 = this.field_181024_n;
      Set var4;
      synchronized(var4 = this.field_181024_n){}
      this.field_181024_n.removeAll(var1);
      this.field_181024_n.addAll(var2);
   }

   private void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.viewFrustum.markBlocksForUpdate(var1, var2, var3, var4, var5, var6);
   }

   private void generateStars() {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      if (this.starVBO != null) {
         this.starVBO.deleteGlBuffers();
      }

      if (this.starGLCallList >= 0) {
         GLAllocation.deleteDisplayLists(this.starGLCallList);
         this.starGLCallList = -1;
      }

      if (this.vboEnabled) {
         this.starVBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderStars(var2);
         var2.finishDrawing();
         var2.reset();
         this.starVBO.func_181722_a(var2.getByteBuffer());
      } else {
         this.starGLCallList = GLAllocation.generateDisplayLists(1);
         GlStateManager.pushMatrix();
         GL11.glNewList(this.starGLCallList, 4864);
         this.renderStars(var2);
         var1.draw();
         GL11.glEndList();
         GlStateManager.popMatrix();
      }

   }

   public void playSound(String var1, double var2, double var4, double var6, float var8, float var9) {
   }

   public void setupTerrain(Entity var1, double var2, ICamera var4, int var5, boolean var6) {
      if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
         this.loadRenderers();
      }

      this.theWorld.theProfiler.startSection("camera");
      double var7 = var1.posX - this.frustumUpdatePosX;
      double var9 = var1.posY - this.frustumUpdatePosY;
      double var11 = var1.posZ - this.frustumUpdatePosZ;
      if (this.frustumUpdatePosChunkX != var1.chunkCoordX || this.frustumUpdatePosChunkY != var1.chunkCoordY || this.frustumUpdatePosChunkZ != var1.chunkCoordZ || var7 * var7 + var9 * var9 + var11 * var11 > 16.0D) {
         this.frustumUpdatePosX = var1.posX;
         this.frustumUpdatePosY = var1.posY;
         this.frustumUpdatePosZ = var1.posZ;
         this.frustumUpdatePosChunkX = var1.chunkCoordX;
         this.frustumUpdatePosChunkY = var1.chunkCoordY;
         this.frustumUpdatePosChunkZ = var1.chunkCoordZ;
         this.viewFrustum.updateChunkPositions(var1.posX, var1.posZ);
      }

      if (Config.isDynamicLights()) {
         DynamicLights.update(this);
      }

      this.theWorld.theProfiler.endStartSection("renderlistcamera");
      double var13 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var2;
      double var15 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var2;
      double var17 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var2;
      this.renderContainer.initialize(var13, var15, var17);
      this.theWorld.theProfiler.endStartSection("cull");
      if (this.debugFixedClippingHelper != null) {
         Frustum var19 = new Frustum(this.debugFixedClippingHelper);
         var19.setPosition(this.debugTerrainFrustumPosition.field_181059_a, this.debugTerrainFrustumPosition.field_181060_b, this.debugTerrainFrustumPosition.field_181061_c);
         var4 = var19;
      }

      this.mc.mcProfiler.endStartSection("culling");
      BlockPos var36 = new BlockPos(var13, var15 + (double)var1.getEyeHeight(), var17);
      RenderChunk var20 = this.viewFrustum.getRenderChunk(var36);
      BlockPos var21 = new BlockPos(MathHelper.floor_double(var13 / 16.0D) * 16, MathHelper.floor_double(var15 / 16.0D) * 16, MathHelper.floor_double(var17 / 16.0D) * 16);
      this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || var1.posX != this.lastViewEntityX || var1.posY != this.lastViewEntityY || var1.posZ != this.lastViewEntityZ || (double)var1.rotationPitch != this.lastViewEntityPitch || (double)var1.rotationYaw != this.lastViewEntityYaw;
      this.lastViewEntityX = var1.posX;
      this.lastViewEntityY = var1.posY;
      this.lastViewEntityZ = var1.posZ;
      this.lastViewEntityPitch = (double)var1.rotationPitch;
      this.lastViewEntityYaw = (double)var1.rotationYaw;
      boolean var22 = this.debugFixedClippingHelper != null;
      Lagometer.timerVisibility.start();
      Iterator var24;
      if (Shaders.isShadowPass) {
         this.renderInfos = this.renderInfosShadow;
         this.renderInfosEntities = this.renderInfosEntitiesShadow;
         this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;
         if (!var22 && this.displayListEntitiesDirty) {
            this.renderInfos.clear();
            this.renderInfosEntities.clear();
            this.renderInfosTileEntities.clear();
            RenderInfoLazy var23 = new RenderInfoLazy();
            var24 = ShadowUtils.makeShadowChunkIterator(this.theWorld, var2, var1, this.renderDistanceChunks, this.viewFrustum);

            label205:
            while(true) {
               RenderChunk var25;
               do {
                  if (!var24.hasNext()) {
                     break label205;
                  }

                  var25 = (RenderChunk)var24.next();
               } while(var25 == null);

               var23.setRenderChunk(var25);
               if (!var25.compiledChunk.isEmpty() || var25.isNeedsUpdate()) {
                  this.renderInfos.add(var23.getRenderInfo());
               }

               BlockPos var26 = var25.getPosition();
               if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(var26))) {
                  this.renderInfosEntities.add(var23.getRenderInfo());
               }

               if (var25.getCompiledChunk().getTileEntities().size() > 0) {
                  this.renderInfosTileEntities.add(var23.getRenderInfo());
               }
            }
         }
      } else {
         this.renderInfos = this.renderInfosNormal;
         this.renderInfosEntities = this.renderInfosEntitiesNormal;
         this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
      }

      if (!var22 && this.displayListEntitiesDirty && !Shaders.isShadowPass) {
         this.displayListEntitiesDirty = false;
         this.renderInfos.clear();
         this.renderInfosEntities.clear();
         this.renderInfosTileEntities.clear();
         this.visibilityDeque.clear();
         Deque var37 = this.visibilityDeque;
         boolean var39 = this.mc.renderChunksMany;
         RenderChunk var28;
         EnumFacing var29;
         int var42;
         if (var20 != null) {
            boolean var41 = false;
            RenderGlobal.ContainerLocalRenderInformation var44 = new RenderGlobal.ContainerLocalRenderInformation(var20, (EnumFacing)null, 0, (Object)null);
            Set var46 = SET_ALL_FACINGS;
            if (var46.size() == 1) {
               Vector3f var49 = this.getViewVector(var1, var2);
               var29 = EnumFacing.getFacingFromVector(var49.x, var49.y, var49.z).getOpposite();
               var46.remove(var29);
            }

            if (var46.isEmpty()) {
               var41 = true;
            }

            if (var41 && !var6) {
               this.renderInfos.add(var44);
            } else {
               if (var6 && this.theWorld.getBlockState(var36).getBlock().isOpaqueCube()) {
                  var39 = false;
               }

               var20.setFrameIndex(var5);
               var37.add(var44);
            }
         } else {
            int var40 = var36.getY() > 0 ? 248 : 8;

            for(var42 = -this.renderDistanceChunks; var42 <= this.renderDistanceChunks; ++var42) {
               for(int var27 = -this.renderDistanceChunks; var27 <= this.renderDistanceChunks; ++var27) {
                  var28 = this.viewFrustum.getRenderChunk(new BlockPos((var42 << 4) + 8, var40, (var27 << 4) + 8));
                  if (var28 != null && ((ICamera)var4).isBoundingBoxInFrustum(var28.boundingBox)) {
                     var28.setFrameIndex(var5);
                     var37.add(new RenderGlobal.ContainerLocalRenderInformation(var28, (EnumFacing)null, 0, (Object)null));
                  }
               }
            }
         }

         EnumFacing[] var43 = EnumFacing.VALUES;
         var42 = var43.length;

         while(!var37.isEmpty()) {
            RenderGlobal.ContainerLocalRenderInformation var48 = (RenderGlobal.ContainerLocalRenderInformation)var37.poll();
            var28 = var48.renderChunk;
            var29 = var48.facing;
            BlockPos var30 = var28.getPosition();
            if (!var28.compiledChunk.isEmpty() || var28.isNeedsUpdate()) {
               this.renderInfos.add(var48);
            }

            if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(var30))) {
               this.renderInfosEntities.add(var48);
            }

            if (var28.getCompiledChunk().getTileEntities().size() > 0) {
               this.renderInfosTileEntities.add(var48);
            }

            for(int var31 = 0; var31 < var42; ++var31) {
               EnumFacing var32 = var43[var31];
               if ((!var39 || !var48.setFacing.contains(var32.getOpposite())) && (!var39 || var29 == null || var28.getCompiledChunk().isVisible(var29.getOpposite(), var32))) {
                  RenderChunk var33 = this.func_181562_a(var36, var28, var32);
                  if (var33 != null && var33.setFrameIndex(var5) && ((ICamera)var4).isBoundingBoxInFrustum(var33.boundingBox)) {
                     RenderGlobal.ContainerLocalRenderInformation var34 = new RenderGlobal.ContainerLocalRenderInformation(var33, var32, var48.counter + 1, (Object)null);
                     var34.setFacing.addAll(var48.setFacing);
                     var34.setFacing.add(var32);
                     var37.add(var34);
                  }
               }
            }
         }
      }

      if (this.debugFixTerrainFrustum) {
         this.fixTerrainFrustum(var13, var15, var17);
         this.debugFixTerrainFrustum = false;
      }

      Lagometer.timerVisibility.end();
      if (Shaders.isShadowPass) {
         Shaders.mcProfilerEndSection();
      } else {
         this.renderDispatcher.clearChunkUpdates();
         Set var38 = this.chunksToUpdate;
         this.chunksToUpdate = Sets.newLinkedHashSet();
         var24 = this.renderInfos.iterator();
         Lagometer.timerChunkUpdate.start();

         while(true) {
            RenderGlobal.ContainerLocalRenderInformation var45;
            RenderChunk var47;
            do {
               if (!var24.hasNext()) {
                  Lagometer.timerChunkUpdate.end();
                  this.chunksToUpdate.addAll(var38);
                  this.mc.mcProfiler.endSection();
                  return;
               }

               var45 = (RenderGlobal.ContainerLocalRenderInformation)var24.next();
               var47 = var45.renderChunk;
            } while(!var47.isNeedsUpdate() && !var38.contains(var47));

            this.displayListEntitiesDirty = true;
            if (var45.renderChunk != false) {
               if (!var47.isPlayerUpdate()) {
                  this.chunksToUpdateForced.add(var47);
               } else {
                  this.mc.mcProfiler.startSection("build near");
                  this.renderDispatcher.updateChunkNow(var47);
                  var47.setNeedsUpdate(false);
                  this.mc.mcProfiler.endSection();
               }
            } else {
               this.chunksToUpdate.add(var47);
            }
         }
      }
   }

   public String getDebugInfoEntities() {
      return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersionDebug();
   }

   private void renderStars(WorldRenderer var1) {
      Random var2 = new Random(10842L);
      var1.begin(7, DefaultVertexFormats.POSITION);

      for(int var3 = 0; var3 < 1500; ++var3) {
         double var4 = (double)(var2.nextFloat() * 2.0F - 1.0F);
         double var6 = (double)(var2.nextFloat() * 2.0F - 1.0F);
         double var8 = (double)(var2.nextFloat() * 2.0F - 1.0F);
         double var10 = (double)(0.15F + var2.nextFloat() * 0.1F);
         double var12 = var4 * var4 + var6 * var6 + var8 * var8;
         if (var12 < 1.0D && var12 > 0.01D) {
            var12 = 1.0D / Math.sqrt(var12);
            var4 *= var12;
            var6 *= var12;
            var8 *= var12;
            double var14 = var4 * 100.0D;
            double var16 = var6 * 100.0D;
            double var18 = var8 * 100.0D;
            double var20 = Math.atan2(var4, var8);
            double var22 = Math.sin(var20);
            double var24 = Math.cos(var20);
            double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
            double var28 = Math.sin(var26);
            double var30 = Math.cos(var26);
            double var32 = var2.nextDouble() * 3.141592653589793D * 2.0D;
            double var34 = Math.sin(var32);
            double var36 = Math.cos(var32);

            for(int var38 = 0; var38 < 4; ++var38) {
               double var39 = 0.0D;
               double var41 = (double)((var38 & 2) - 1) * var10;
               double var43 = (double)((var38 + 1 & 2) - 1) * var10;
               double var45 = 0.0D;
               double var47 = var41 * var36 - var43 * var34;
               double var49 = var43 * var36 + var41 * var34;
               double var51 = var47 * var28 + 0.0D * var30;
               double var53 = 0.0D * var28 - var47 * var30;
               double var55 = var53 * var22 - var49 * var24;
               double var57 = var49 * var22 + var53 * var24;
               var1.pos(var14 + var55, var16 + var51, var18 + var57).endVertex();
            }
         }
      }

   }

   static {
      SET_ALL_FACINGS = Collections.unmodifiableSet(new HashSet(Arrays.asList(EnumFacing.VALUES)));
   }

   public void onEntityRemoved(Entity var1) {
      if (Config.isDynamicLights()) {
         DynamicLights.entityRemoved(var1, this);
      }

   }

   private void renderCloudsFancy(float var1, int var2) {
      this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, var1);
      var1 = 0.0F;
      GlStateManager.disableCull();
      float var3 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)var1);
      Tessellator var4 = Tessellator.getInstance();
      WorldRenderer var5 = var4.getWorldRenderer();
      float var6 = 12.0F;
      float var7 = 4.0F;
      double var8 = (double)((float)this.cloudTickCounter + var1);
      double var10 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)var1 + var8 * 0.029999999329447746D) / 12.0D;
      double var12 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)var1) / 12.0D + 0.33000001311302185D;
      float var14 = this.theWorld.provider.getCloudHeight() - var3 + 0.33F;
      var14 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
      int var15 = MathHelper.floor_double(var10 / 2048.0D);
      int var16 = MathHelper.floor_double(var12 / 2048.0D);
      var10 -= (double)(var15 * 2048);
      var12 -= (double)(var16 * 2048);
      renderEngine.bindTexture(locationCloudsPng);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      Vec3 var17 = this.theWorld.getCloudColour(var1);
      float var18 = (float)var17.xCoord;
      float var19 = (float)var17.yCoord;
      float var20 = (float)var17.zCoord;
      float var21;
      float var22;
      float var23;
      if (var2 != 2) {
         var21 = (var18 * 30.0F + var19 * 59.0F + var20 * 11.0F) / 100.0F;
         var22 = (var18 * 30.0F + var19 * 70.0F) / 100.0F;
         var23 = (var18 * 30.0F + var20 * 70.0F) / 100.0F;
         var18 = var21;
         var19 = var22;
         var20 = var23;
      }

      var21 = var18 * 0.9F;
      var22 = var19 * 0.9F;
      var23 = var20 * 0.9F;
      float var24 = var18 * 0.7F;
      float var25 = var19 * 0.7F;
      float var26 = var20 * 0.7F;
      float var27 = var18 * 0.8F;
      float var28 = var19 * 0.8F;
      float var29 = var20 * 0.8F;
      float var30 = 0.00390625F;
      float var31 = (float)MathHelper.floor_double(var10) * 0.00390625F;
      float var32 = (float)MathHelper.floor_double(var12) * 0.00390625F;
      float var33 = (float)(var10 - (double)MathHelper.floor_double(var10));
      float var34 = (float)(var12 - (double)MathHelper.floor_double(var12));
      boolean var35 = true;
      boolean var36 = true;
      float var37 = 9.765625E-4F;
      GlStateManager.scale(12.0F, 1.0F, 12.0F);

      int var38;
      for(var38 = 0; var38 < 2; ++var38) {
         if (var38 == 0) {
            GlStateManager.colorMask(false, false, false, false);
         } else {
            switch(var2) {
            case 0:
               GlStateManager.colorMask(false, true, true, true);
               break;
            case 1:
               GlStateManager.colorMask(true, false, false, true);
               break;
            case 2:
               GlStateManager.colorMask(true, true, true, true);
            }
         }

         this.cloudRenderer.renderGlList();
      }

      if (this.cloudRenderer.shouldUpdateGlList()) {
         this.cloudRenderer.startUpdateGlList();

         for(var38 = -3; var38 <= 4; ++var38) {
            for(int var39 = -3; var39 <= 4; ++var39) {
               var5.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
               float var40 = (float)(var38 * 8);
               float var41 = (float)(var39 * 8);
               float var42 = var40 - var33;
               float var43 = var41 - var34;
               if (var14 > -5.0F) {
                  var5.pos((double)(var42 + 0.0F), (double)(var14 + 0.0F), (double)(var43 + 8.0F)).tex((double)((var40 + 0.0F) * 0.00390625F + var31), (double)((var41 + 8.0F) * 0.00390625F + var32)).color(var24, var25, var26, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                  var5.pos((double)(var42 + 8.0F), (double)(var14 + 0.0F), (double)(var43 + 8.0F)).tex((double)((var40 + 8.0F) * 0.00390625F + var31), (double)((var41 + 8.0F) * 0.00390625F + var32)).color(var24, var25, var26, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                  var5.pos((double)(var42 + 8.0F), (double)(var14 + 0.0F), (double)(var43 + 0.0F)).tex((double)((var40 + 8.0F) * 0.00390625F + var31), (double)((var41 + 0.0F) * 0.00390625F + var32)).color(var24, var25, var26, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                  var5.pos((double)(var42 + 0.0F), (double)(var14 + 0.0F), (double)(var43 + 0.0F)).tex((double)((var40 + 0.0F) * 0.00390625F + var31), (double)((var41 + 0.0F) * 0.00390625F + var32)).color(var24, var25, var26, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
               }

               if (var14 <= 5.0F) {
                  var5.pos((double)(var42 + 0.0F), (double)(var14 + 4.0F - 9.765625E-4F), (double)(var43 + 8.0F)).tex((double)((var40 + 0.0F) * 0.00390625F + var31), (double)((var41 + 8.0F) * 0.00390625F + var32)).color(var18, var19, var20, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                  var5.pos((double)(var42 + 8.0F), (double)(var14 + 4.0F - 9.765625E-4F), (double)(var43 + 8.0F)).tex((double)((var40 + 8.0F) * 0.00390625F + var31), (double)((var41 + 8.0F) * 0.00390625F + var32)).color(var18, var19, var20, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                  var5.pos((double)(var42 + 8.0F), (double)(var14 + 4.0F - 9.765625E-4F), (double)(var43 + 0.0F)).tex((double)((var40 + 8.0F) * 0.00390625F + var31), (double)((var41 + 0.0F) * 0.00390625F + var32)).color(var18, var19, var20, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                  var5.pos((double)(var42 + 0.0F), (double)(var14 + 4.0F - 9.765625E-4F), (double)(var43 + 0.0F)).tex((double)((var40 + 0.0F) * 0.00390625F + var31), (double)((var41 + 0.0F) * 0.00390625F + var32)).color(var18, var19, var20, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
               }

               int var44;
               if (var38 > -1) {
                  for(var44 = 0; var44 < 8; ++var44) {
                     var5.pos((double)(var42 + (float)var44 + 0.0F), (double)(var14 + 0.0F), (double)(var43 + 8.0F)).tex((double)((var40 + (float)var44 + 0.5F) * 0.00390625F + var31), (double)((var41 + 8.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var42 + (float)var44 + 0.0F), (double)(var14 + 4.0F), (double)(var43 + 8.0F)).tex((double)((var40 + (float)var44 + 0.5F) * 0.00390625F + var31), (double)((var41 + 8.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var42 + (float)var44 + 0.0F), (double)(var14 + 4.0F), (double)(var43 + 0.0F)).tex((double)((var40 + (float)var44 + 0.5F) * 0.00390625F + var31), (double)((var41 + 0.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var42 + (float)var44 + 0.0F), (double)(var14 + 0.0F), (double)(var43 + 0.0F)).tex((double)((var40 + (float)var44 + 0.5F) * 0.00390625F + var31), (double)((var41 + 0.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                  }
               }

               if (var38 <= 1) {
                  for(var44 = 0; var44 < 8; ++var44) {
                     var5.pos((double)(var42 + (float)var44 + 1.0F - 9.765625E-4F), (double)(var14 + 0.0F), (double)(var43 + 8.0F)).tex((double)((var40 + (float)var44 + 0.5F) * 0.00390625F + var31), (double)((var41 + 8.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var42 + (float)var44 + 1.0F - 9.765625E-4F), (double)(var14 + 4.0F), (double)(var43 + 8.0F)).tex((double)((var40 + (float)var44 + 0.5F) * 0.00390625F + var31), (double)((var41 + 8.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var42 + (float)var44 + 1.0F - 9.765625E-4F), (double)(var14 + 4.0F), (double)(var43 + 0.0F)).tex((double)((var40 + (float)var44 + 0.5F) * 0.00390625F + var31), (double)((var41 + 0.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var42 + (float)var44 + 1.0F - 9.765625E-4F), (double)(var14 + 0.0F), (double)(var43 + 0.0F)).tex((double)((var40 + (float)var44 + 0.5F) * 0.00390625F + var31), (double)((var41 + 0.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                  }
               }

               if (var39 > -1) {
                  for(var44 = 0; var44 < 8; ++var44) {
                     var5.pos((double)(var42 + 0.0F), (double)(var14 + 4.0F), (double)(var43 + (float)var44 + 0.0F)).tex((double)((var40 + 0.0F) * 0.00390625F + var31), (double)((var41 + (float)var44 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                     var5.pos((double)(var42 + 8.0F), (double)(var14 + 4.0F), (double)(var43 + (float)var44 + 0.0F)).tex((double)((var40 + 8.0F) * 0.00390625F + var31), (double)((var41 + (float)var44 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                     var5.pos((double)(var42 + 8.0F), (double)(var14 + 0.0F), (double)(var43 + (float)var44 + 0.0F)).tex((double)((var40 + 8.0F) * 0.00390625F + var31), (double)((var41 + (float)var44 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                     var5.pos((double)(var42 + 0.0F), (double)(var14 + 0.0F), (double)(var43 + (float)var44 + 0.0F)).tex((double)((var40 + 0.0F) * 0.00390625F + var31), (double)((var41 + (float)var44 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                  }
               }

               if (var39 <= 1) {
                  for(var44 = 0; var44 < 8; ++var44) {
                     var5.pos((double)(var42 + 0.0F), (double)(var14 + 4.0F), (double)(var43 + (float)var44 + 1.0F - 9.765625E-4F)).tex((double)((var40 + 0.0F) * 0.00390625F + var31), (double)((var41 + (float)var44 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                     var5.pos((double)(var42 + 8.0F), (double)(var14 + 4.0F), (double)(var43 + (float)var44 + 1.0F - 9.765625E-4F)).tex((double)((var40 + 8.0F) * 0.00390625F + var31), (double)((var41 + (float)var44 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                     var5.pos((double)(var42 + 8.0F), (double)(var14 + 0.0F), (double)(var43 + (float)var44 + 1.0F - 9.765625E-4F)).tex((double)((var40 + 8.0F) * 0.00390625F + var31), (double)((var41 + (float)var44 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                     var5.pos((double)(var42 + 0.0F), (double)(var14 + 0.0F), (double)(var43 + (float)var44 + 1.0F - 9.765625E-4F)).tex((double)((var40 + 0.0F) * 0.00390625F + var31), (double)((var41 + (float)var44 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                  }
               }

               var4.draw();
            }
         }

         this.cloudRenderer.endUpdateGlList();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.enableCull();
   }

   public static void func_181563_a(AxisAlignedBB var0, int var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.getInstance();
      WorldRenderer var6 = var5.getWorldRenderer();
      var6.begin(3, DefaultVertexFormats.POSITION_COLOR);
      var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.maxX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.maxX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.minX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var5.draw();
      var6.begin(3, DefaultVertexFormats.POSITION_COLOR);
      var6.pos(var0.minX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.maxX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.maxX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.minX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.minX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var5.draw();
      var6.begin(1, DefaultVertexFormats.POSITION_COLOR);
      var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.minX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.maxX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.maxX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.maxX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.maxX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.minX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
      var6.pos(var0.minX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
      var5.draw();
   }

   public static void renderSkyEnd() {
      if (Config.isSkyEnabled()) {
         GlStateManager.disableFog();
         GlStateManager.disableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         RenderHelper.disableStandardItemLighting();
         GlStateManager.depthMask(false);
         renderEngine.bindTexture(locationEndSkyPng);
         Tessellator var0 = Tessellator.getInstance();
         WorldRenderer var1 = var0.getWorldRenderer();

         for(int var2 = 0; var2 < 6; ++var2) {
            GlStateManager.pushMatrix();
            if (var2 == 1) {
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (var2 == 2) {
               GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (var2 == 3) {
               GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (var2 == 4) {
               GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if (var2 == 5) {
               GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            var1.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var1.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(40, 40, 40, 255).endVertex();
            var1.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(40, 40, 40, 255).endVertex();
            var1.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(40, 40, 40, 255).endVertex();
            var1.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(40, 40, 40, 255).endVertex();
            var0.draw();
            GlStateManager.popMatrix();
         }

         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         GlStateManager.enableAlpha();
      }

   }

   public static void func_181561_a(AxisAlignedBB var0) {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      var2.begin(3, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(3, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(1, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var1.draw();
   }

   private Set getVisibleFacings(BlockPos var1) {
      VisGraph var2 = new VisGraph();
      BlockPos var3 = new BlockPos(var1.getX() >> 4 << 4, var1.getY() >> 4 << 4, var1.getZ() >> 4 << 4);
      Chunk var4 = this.theWorld.getChunkFromBlockCoords(var3);
      Iterator var6 = BlockPos.getAllInBoxMutable(var3, var3.add(15, 15, 15)).iterator();

      while(var6.hasNext()) {
         BlockPos.MutableBlockPos var5 = (BlockPos.MutableBlockPos)var6.next();
         if (var4.getBlock(var5).isOpaqueCube()) {
            var2.func_178606_a(var5);
         }
      }

      return var2.func_178609_b(var1);
   }

   public void createBindEntityOutlineFbs(int var1, int var2) {
      if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null) {
         this.entityOutlineShader.createBindFramebuffers(var1, var2);
      }

   }

   public WorldClient getWorld() {
      return this.theWorld;
   }

   public int getCountRenderers() {
      return this.viewFrustum.renderChunks.length;
   }

   public int getCountActiveRenderers() {
      return this.renderInfos.size();
   }

   private void updateDestroyBlockIcons() {
      TextureMap var1 = this.mc.getTextureMapBlocks();

      for(int var2 = 0; var2 < this.destroyBlockIcons.length; ++var2) {
         this.destroyBlockIcons[var2] = var1.getAtlasSprite("minecraft:blocks/destroy_stage_" + var2);
      }

   }

   public int renderBlockLayer(EnumWorldBlockLayer var1, double var2, int var4, Entity var5) {
      RenderHelper.disableStandardItemLighting();
      if (var1 == EnumWorldBlockLayer.TRANSLUCENT) {
         this.mc.mcProfiler.startSection("translucent_sort");
         double var6 = var5.posX - this.prevRenderSortX;
         double var8 = var5.posY - this.prevRenderSortY;
         double var10 = var5.posZ - this.prevRenderSortZ;
         if (var6 * var6 + var8 * var8 + var10 * var10 > 1.0D) {
            this.prevRenderSortX = var5.posX;
            this.prevRenderSortY = var5.posY;
            this.prevRenderSortZ = var5.posZ;
            int var12 = 0;
            Iterator var13 = this.renderInfos.iterator();
            this.chunksToResortTransparency.clear();

            while(var13.hasNext()) {
               RenderGlobal.ContainerLocalRenderInformation var14 = (RenderGlobal.ContainerLocalRenderInformation)var13.next();
               if (var14.renderChunk.compiledChunk.isLayerStarted(var1) && var12++ < 15) {
                  this.chunksToResortTransparency.add(var14.renderChunk);
               }
            }
         }

         this.mc.mcProfiler.endSection();
      }

      this.mc.mcProfiler.startSection("filterempty");
      int var15 = 0;
      boolean var7 = var1 == EnumWorldBlockLayer.TRANSLUCENT;
      int var16 = var7 ? this.renderInfos.size() - 1 : 0;
      int var9 = var7 ? -1 : this.renderInfos.size();
      int var17 = var7 ? -1 : 1;

      for(int var11 = var16; var11 != var9; var11 += var17) {
         RenderChunk var18 = ((RenderGlobal.ContainerLocalRenderInformation)this.renderInfos.get(var11)).renderChunk;
         if (!var18.getCompiledChunk().isLayerEmpty(var1)) {
            ++var15;
            this.renderContainer.addRenderChunk(var18, var1);
         }
      }

      if (var15 == 0) {
         this.mc.mcProfiler.endSection();
         return var15;
      } else {
         if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
            GlStateManager.disableFog();
         }

         this.mc.mcProfiler.endStartSection("render_" + var1);
         this.renderBlockLayer(var1);
         this.mc.mcProfiler.endSection();
         return var15;
      }
   }

   public void setDisplayListEntitiesDirty() {
      this.displayListEntitiesDirty = true;
   }

   public boolean hasCloudFog(double var1, double var3, double var5, float var7) {
      return false;
   }

   public void renderEntities(Entity var1, ICamera var2, float var3) {
      int var4 = 0;
      if (Reflector.MinecraftForgeClient_getRenderPass.exists()) {
         var4 = Reflector.callInt(Reflector.MinecraftForgeClient_getRenderPass);
      }

      if (this.renderEntitiesStartupCounter > 0) {
         if (var4 <= 0) {
            --this.renderEntitiesStartupCounter;
         }
      } else {
         double var5 = var1.prevPosX + (var1.posX - var1.prevPosX) * (double)var3;
         double var7 = var1.prevPosY + (var1.posY - var1.prevPosY) * (double)var3;
         double var9 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)var3;
         this.theWorld.theProfiler.startSection("prepare");
         TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), Minecraft.fontRendererObj, this.mc.getRenderViewEntity(), var3);
         this.renderManager.cacheActiveRenderInfo(this.theWorld, Minecraft.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, var3);
         if (var4 == 0) {
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            this.countTileEntitiesRendered = 0;
         }

         Entity var11 = this.mc.getRenderViewEntity();
         double var12 = var11.lastTickPosX + (var11.posX - var11.lastTickPosX) * (double)var3;
         double var14 = var11.lastTickPosY + (var11.posY - var11.lastTickPosY) * (double)var3;
         double var16 = var11.lastTickPosZ + (var11.posZ - var11.lastTickPosZ) * (double)var3;
         TileEntityRendererDispatcher.staticPlayerX = var12;
         TileEntityRendererDispatcher.staticPlayerY = var14;
         TileEntityRendererDispatcher.staticPlayerZ = var16;
         this.renderManager.setRenderPosition(var12, var14, var16);
         this.mc.entityRenderer.enableLightmap();
         this.theWorld.theProfiler.endStartSection("global");
         List var18 = this.theWorld.getLoadedEntityList();
         if (var4 == 0) {
            this.countEntitiesTotal = var18.size();
         }

         if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
            GlStateManager.disableFog();
         }

         boolean var19 = Reflector.ForgeEntity_shouldRenderInPass.exists();
         boolean var20 = Reflector.ForgeTileEntity_shouldRenderInPass.exists();

         int var21;
         Entity var22;
         for(var21 = 0; var21 < this.theWorld.weatherEffects.size(); ++var21) {
            var22 = (Entity)this.theWorld.weatherEffects.get(var21);
            if (!var19 || Reflector.callBoolean(var22, Reflector.ForgeEntity_shouldRenderInPass, var4)) {
               ++this.countEntitiesRendered;
               if (var22.isInRangeToRender3d(var5, var7, var9)) {
                  this.renderManager.renderEntitySimple(var22, var3);
               }
            }
         }

         boolean var23;
         if (this != false) {
            GlStateManager.depthFunc(519);
            GlStateManager.disableFog();
            this.entityOutlineFramebuffer.framebufferClear();
            this.entityOutlineFramebuffer.bindFramebuffer(false);
            this.theWorld.theProfiler.endStartSection("entityOutlines");
            RenderHelper.disableStandardItemLighting();
            this.renderManager.setRenderOutlines(true);

            for(var21 = 0; var21 < var18.size(); ++var21) {
               var22 = (Entity)var18.get(var21);
               if (!var19 || Reflector.callBoolean(var22, Reflector.ForgeEntity_shouldRenderInPass, var4)) {
                  var23 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                  boolean var24 = var22.isInRangeToRender3d(var5, var7, var9) && (var22.ignoreFrustumCheck || var2.isBoundingBoxInFrustum(var22.getEntityBoundingBox()) || var22.riddenByEntity == Minecraft.thePlayer) && var22 instanceof EntityPlayer;
                  if ((var22 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || var23) && var24) {
                     this.renderManager.renderEntitySimple(var22, var3);
                  }
               }
            }

            this.renderManager.setRenderOutlines(false);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.depthMask(false);
            this.entityOutlineShader.loadShaderGroup(var3);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            this.mc.getFramebuffer().bindFramebuffer(false);
            GlStateManager.enableFog();
            GlStateManager.enableBlend();
            GlStateManager.enableColorMaterial();
            GlStateManager.depthFunc(515);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
         }

         this.theWorld.theProfiler.endStartSection("entities");
         boolean var35 = Config.isShaders();
         if (var35) {
            Shaders.beginEntities();
         }

         Iterator var36 = this.renderInfosEntities.iterator();
         var23 = this.mc.gameSettings.fancyGraphics;
         this.mc.gameSettings.fancyGraphics = Config.isDroppedItemsFancy();

         label301:
         while(true) {
            ClassInheritanceMultiMap var26;
            Iterator var27;
            do {
               if (!var36.hasNext()) {
                  this.mc.gameSettings.fancyGraphics = var23;
                  FontRenderer var38 = TileEntityRendererDispatcher.instance.getFontRenderer();
                  if (var35) {
                     Shaders.endEntities();
                     Shaders.beginBlockEntities();
                  }

                  this.theWorld.theProfiler.endStartSection("blockentities");
                  RenderHelper.enableStandardItemLighting();
                  if (Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch.exists()) {
                     Reflector.call(TileEntityRendererDispatcher.instance, Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch);
                  }

                  Iterator var41 = this.renderInfosTileEntities.iterator();

                  TileEntity var52;
                  label262:
                  while(true) {
                     List var46;
                     do {
                        if (!var41.hasNext()) {
                           Set var40 = this.field_181024_n;
                           Set var42;
                           synchronized(var42 = this.field_181024_n){}
                           Iterator var47 = this.field_181024_n.iterator();

                           while(true) {
                              Object var45;
                              while(true) {
                                 if (!var47.hasNext()) {
                                    if (Reflector.ForgeTileEntityRendererDispatcher_drawBatch.exists()) {
                                       Reflector.call(TileEntityRendererDispatcher.instance, Reflector.ForgeTileEntityRendererDispatcher_drawBatch, var4);
                                    }

                                    this.preRenderDamagedBlocks();
                                    var27 = this.damagedBlocks.values().iterator();

                                    while(var27.hasNext()) {
                                       Object var43 = var27.next();
                                       BlockPos var48 = ((DestroyBlockProgress)var43).getPosition();
                                       TileEntity var53 = this.theWorld.getTileEntity(var48);
                                       if (var53 instanceof TileEntityChest) {
                                          TileEntityChest var55 = (TileEntityChest)var53;
                                          if (var55.adjacentChestXNeg != null) {
                                             var48 = var48.offset(EnumFacing.WEST);
                                             var53 = this.theWorld.getTileEntity(var48);
                                          } else if (var55.adjacentChestZNeg != null) {
                                             var48 = var48.offset(EnumFacing.NORTH);
                                             var53 = this.theWorld.getTileEntity(var48);
                                          }
                                       }

                                       Block var57 = this.theWorld.getBlockState(var48).getBlock();
                                       boolean var59;
                                       if (var20) {
                                          var59 = false;
                                          if (var53 != null && Reflector.callBoolean(var53, Reflector.ForgeTileEntity_shouldRenderInPass, var4) && Reflector.callBoolean(var53, Reflector.ForgeTileEntity_canRenderBreaking)) {
                                             AxisAlignedBB var60 = (AxisAlignedBB)Reflector.call(var53, Reflector.ForgeTileEntity_getRenderBoundingBox);
                                             if (var60 != null) {
                                                var59 = var2.isBoundingBoxInFrustum(var60);
                                             }
                                          }
                                       } else {
                                          var59 = var53 != null && (var57 instanceof BlockChest || var57 instanceof BlockEnderChest || var57 instanceof BlockSign || var57 instanceof BlockSkull);
                                       }

                                       if (var59) {
                                          if (var35) {
                                             Shaders.nextBlockEntity(var53);
                                          }

                                          TileEntityRendererDispatcher.instance.renderTileEntity(var53, var3, ((DestroyBlockProgress)var43).getPartialBlockDamage());
                                       }
                                    }

                                    this.postRenderDamagedBlocks();
                                    this.mc.entityRenderer.disableLightmap();
                                    this.mc.mcProfiler.endSection();
                                    return;
                                 }

                                 var45 = var47.next();
                                 if (!var20) {
                                    break;
                                 }

                                 if (Reflector.callBoolean(var45, Reflector.ForgeTileEntity_shouldRenderInPass, var4)) {
                                    AxisAlignedBB var50 = (AxisAlignedBB)Reflector.call(var45, Reflector.ForgeTileEntity_getRenderBoundingBox);
                                    if (var50 == null || var2.isBoundingBoxInFrustum(var50)) {
                                       break;
                                    }
                                 }
                              }

                              Class var51 = var45.getClass();
                              if (var51 == TileEntitySign.class && !Config.zoomMode) {
                                 EntityPlayerSP var54 = Minecraft.thePlayer;
                                 double var58 = ((TileEntity)var45).getDistanceSq(var54.posX, var54.posY, var54.posZ);
                                 if (var58 > 256.0D) {
                                    var38.enabled = false;
                                 }
                              }

                              if (var35) {
                                 Shaders.nextBlockEntity((TileEntity)var45);
                              }

                              TileEntityRendererDispatcher.instance.renderTileEntity((TileEntity)var45, var3, -1);
                              var38.enabled = true;
                           }
                        }

                        Object var39 = var41.next();
                        RenderGlobal.ContainerLocalRenderInformation var44 = (RenderGlobal.ContainerLocalRenderInformation)var39;
                        var46 = var44.renderChunk.getCompiledChunk().getTileEntities();
                     } while(var46.isEmpty());

                     Iterator var49 = var46.iterator();

                     while(var49.hasNext()) {
                        var52 = (TileEntity)var49.next();
                        if (!var20) {
                           break label262;
                        }

                        if (Reflector.callBoolean(var52, Reflector.ForgeTileEntity_shouldRenderInPass, var4)) {
                           AxisAlignedBB var31 = (AxisAlignedBB)Reflector.call(var52, Reflector.ForgeTileEntity_getRenderBoundingBox);
                           if (var31 == null || var2.isBoundingBoxInFrustum(var31)) {
                              break label262;
                           }
                        }
                     }
                  }

                  Class var56 = var52.getClass();
                  if (var56 == TileEntitySign.class && !Config.zoomMode) {
                     EntityPlayerSP var32 = Minecraft.thePlayer;
                     double var33 = var52.getDistanceSq(var32.posX, var32.posY, var32.posZ);
                     if (var33 > 256.0D) {
                        var38.enabled = false;
                     }
                  }

                  if (var35) {
                     Shaders.nextBlockEntity(var52);
                  }

                  TileEntityRendererDispatcher.instance.renderTileEntity(var52, var3, -1);
                  ++this.countTileEntitiesRendered;
                  var38.enabled = true;
                  return;
               }

               RenderGlobal.ContainerLocalRenderInformation var37 = (RenderGlobal.ContainerLocalRenderInformation)var36.next();
               Chunk var25 = this.theWorld.getChunkFromBlockCoords(var37.renderChunk.getPosition());
               var26 = var25.getEntityLists()[var37.renderChunk.getPosition().getY() / 16];
            } while(var26.isEmpty());

            var27 = var26.iterator();

            Entity var28;
            boolean var29;
            label299:
            do {
               boolean var30;
               do {
                  do {
                     if (!var27.hasNext()) {
                        continue label301;
                     }

                     var28 = (Entity)var27.next();
                  } while(var19 && !Reflector.callBoolean(var28, Reflector.ForgeEntity_shouldRenderInPass, var4));

                  var29 = this.renderManager.shouldRender(var28, var2, var5, var7, var9) || var28.riddenByEntity == Minecraft.thePlayer;
                  if (!var29) {
                     continue label299;
                  }

                  var30 = this.mc.getRenderViewEntity() instanceof EntityLivingBase ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping() : false;
               } while(var28 == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0 && !var30 || !(var28.posY < 0.0D) && !(var28.posY >= 256.0D) && !this.theWorld.isBlockLoaded(new BlockPos(var28)));

               ++this.countEntitiesRendered;
               if (var28.getClass() == EntityItemFrame.class) {
                  var28.renderDistanceWeight = 0.06D;
               }

               this.renderedEntity = var28;
               if (var35) {
                  Shaders.nextEntity(var28);
               }

               this.renderManager.renderEntitySimple(var28, var3);
               this.renderedEntity = null;
            } while(var29 || !(var28 instanceof EntityWitherSkull));

            if (var35) {
               Shaders.nextEntity(var28);
            }

            this.mc.getRenderManager().renderWitherSkull(var28, var3);
            return;
         }
      }
   }

   public void playAuxSFX(EntityPlayer var1, int var2, BlockPos var3, int var4) {
      Random var5 = this.theWorld.rand;
      double var15;
      double var17;
      double var19;
      double var27;
      int var32;
      double var33;
      double var35;
      switch(var2) {
      case 1000:
         this.theWorld.playSoundAtPos(var3, "random.click", 1.0F, 1.0F, false);
         break;
      case 1001:
         this.theWorld.playSoundAtPos(var3, "random.click", 1.0F, 1.2F, false);
         break;
      case 1002:
         this.theWorld.playSoundAtPos(var3, "random.bow", 1.0F, 1.2F, false);
         break;
      case 1003:
         this.theWorld.playSoundAtPos(var3, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1004:
         this.theWorld.playSoundAtPos(var3, "random.fizz", 0.5F, 2.6F + (var5.nextFloat() - var5.nextFloat()) * 0.8F, false);
         break;
      case 1005:
         if (Item.getItemById(var4) instanceof ItemRecord) {
            this.theWorld.playRecord(var3, "records." + ((ItemRecord)Item.getItemById(var4)).recordName);
         } else {
            this.theWorld.playRecord(var3, (String)null);
         }
         break;
      case 1006:
         this.theWorld.playSoundAtPos(var3, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1007:
         this.theWorld.playSoundAtPos(var3, "mob.ghast.charge", 10.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1008:
         this.theWorld.playSoundAtPos(var3, "mob.ghast.fireball", 10.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1009:
         this.theWorld.playSoundAtPos(var3, "mob.ghast.fireball", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1010:
         this.theWorld.playSoundAtPos(var3, "mob.zombie.wood", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1011:
         this.theWorld.playSoundAtPos(var3, "mob.zombie.metal", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1012:
         this.theWorld.playSoundAtPos(var3, "mob.zombie.woodbreak", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1014:
         this.theWorld.playSoundAtPos(var3, "mob.wither.shoot", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1015:
         this.theWorld.playSoundAtPos(var3, "mob.bat.takeoff", 0.05F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1016:
         this.theWorld.playSoundAtPos(var3, "mob.zombie.infect", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1017:
         this.theWorld.playSoundAtPos(var3, "mob.zombie.unfect", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1020:
         this.theWorld.playSoundAtPos(var3, "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1021:
         this.theWorld.playSoundAtPos(var3, "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1022:
         this.theWorld.playSoundAtPos(var3, "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 2000:
         int var6 = var4 % 3 - 1;
         int var7 = var4 / 3 % 3 - 1;
         double var8 = (double)var3.getX() + (double)var6 * 0.6D + 0.5D;
         double var10 = (double)var3.getY() + 0.5D;
         double var12 = (double)var3.getZ() + (double)var7 * 0.6D + 0.5D;

         for(int var39 = 0; var39 < 10; ++var39) {
            var15 = var5.nextDouble() * 0.2D + 0.01D;
            var17 = var8 + (double)var6 * 0.01D + (var5.nextDouble() - 0.5D) * (double)var7 * 0.5D;
            var19 = var10 + (var5.nextDouble() - 0.5D) * 0.5D;
            double var40 = var12 + (double)var7 * 0.01D + (var5.nextDouble() - 0.5D) * (double)var6 * 0.5D;
            double var41 = (double)var6 * var15 + var5.nextGaussian() * 0.01D;
            double var42 = -0.03D + var5.nextGaussian() * 0.01D;
            var27 = (double)var7 * var15 + var5.nextGaussian() * 0.01D;
            this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var17, var19, var40, var41, var42, var27);
         }

         return;
      case 2001:
         Block var14 = Block.getBlockById(var4 & 4095);
         if (var14.getMaterial() != Material.air) {
            this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var14.stepSound.getBreakSound()), (var14.stepSound.getVolume() + 1.0F) / 2.0F, var14.stepSound.getFrequency() * 0.8F, (float)var3.getX() + 0.5F, (float)var3.getY() + 0.5F, (float)var3.getZ() + 0.5F));
         }

         this.mc.effectRenderer.addBlockDestroyEffects(var3, var14.getStateFromMeta(var4 >> 12 & 255));
         break;
      case 2002:
         var15 = (double)var3.getX();
         var17 = (double)var3.getY();
         var19 = (double)var3.getZ();

         int var21;
         for(var21 = 0; var21 < 8; ++var21) {
            this.spawnParticle(EnumParticleTypes.ITEM_CRACK, var15, var17, var19, var5.nextGaussian() * 0.15D, var5.nextDouble() * 0.2D, var5.nextGaussian() * 0.15D, Item.getIdFromItem(Items.potionitem), var4);
         }

         var21 = Items.potionitem.getColorFromDamage(var4);
         float var22 = (float)(var21 >> 16 & 255) / 255.0F;
         float var23 = (float)(var21 >> 8 & 255) / 255.0F;
         float var24 = (float)(var21 >> 0 & 255) / 255.0F;
         EnumParticleTypes var25 = EnumParticleTypes.SPELL;
         if (Items.potionitem.isEffectInstant(var4)) {
            var25 = EnumParticleTypes.SPELL_INSTANT;
         }

         for(int var43 = 0; var43 < 100; ++var43) {
            var27 = var5.nextDouble() * 4.0D;
            double var29 = var5.nextDouble() * 3.141592653589793D * 2.0D;
            double var31 = Math.cos(var29) * var27;
            var33 = 0.01D + var5.nextDouble() * 0.5D;
            var35 = Math.sin(var29) * var27;
            EntityFX var45 = this.spawnEntityFX(var25.getParticleID(), var25.getShouldIgnoreRange(), var15 + var31 * 0.1D, var17 + 0.3D, var19 + var35 * 0.1D, var31, var33, var35);
            if (var45 != null) {
               float var38 = 0.75F + var5.nextFloat() * 0.25F;
               var45.setRBGColorF(var22 * var38, var23 * var38, var24 * var38);
               var45.multiplyVelocity((float)var27);
            }
         }

         this.theWorld.playSoundAtPos(var3, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 2003:
         double var26 = (double)var3.getX() + 0.5D;
         double var28 = (double)var3.getY();
         double var30 = (double)var3.getZ() + 0.5D;

         for(var32 = 0; var32 < 8; ++var32) {
            this.spawnParticle(EnumParticleTypes.ITEM_CRACK, var26, var28, var30, var5.nextGaussian() * 0.15D, var5.nextDouble() * 0.2D, var5.nextGaussian() * 0.15D, Item.getIdFromItem(Items.ender_eye));
         }

         for(double var44 = 0.0D; var44 < 6.283185307179586D; var44 += 0.15707963267948966D) {
            this.spawnParticle(EnumParticleTypes.PORTAL, var26 + Math.cos(var44) * 5.0D, var28 - 0.4D, var30 + Math.sin(var44) * 5.0D, Math.cos(var44) * -5.0D, 0.0D, Math.sin(var44) * -5.0D);
            this.spawnParticle(EnumParticleTypes.PORTAL, var26 + Math.cos(var44) * 5.0D, var28 - 0.4D, var30 + Math.sin(var44) * 5.0D, Math.cos(var44) * -7.0D, 0.0D, Math.sin(var44) * -7.0D);
         }

         return;
      case 2004:
         for(var32 = 0; var32 < 20; ++var32) {
            var33 = (double)var3.getX() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            var35 = (double)var3.getY() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            double var37 = (double)var3.getZ() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var33, var35, var37, 0.0D, 0.0D, 0.0D, new int[0]);
            this.theWorld.spawnParticle(EnumParticleTypes.FLAME, var33, var35, var37, 0.0D, 0.0D, 0.0D, new int[0]);
         }

         return;
      case 2005:
         ItemDye.spawnBonemealParticles(this.theWorld, var3, var4);
      }

   }

   public RenderChunk getRenderChunk(BlockPos var1) {
      return this.viewFrustum.getRenderChunk(var1);
   }

   private void generateSky() {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      if (this.skyVBO != null) {
         this.skyVBO.deleteGlBuffers();
      }

      if (this.glSkyList >= 0) {
         GLAllocation.deleteDisplayLists(this.glSkyList);
         this.glSkyList = -1;
      }

      if (this.vboEnabled) {
         this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderSky(var2, 16.0F, false);
         var2.finishDrawing();
         var2.reset();
         this.skyVBO.func_181722_a(var2.getByteBuffer());
      } else {
         this.glSkyList = GLAllocation.generateDisplayLists(1);
         GL11.glNewList(this.glSkyList, 4864);
         this.renderSky(var2, 16.0F, false);
         var1.draw();
         GL11.glEndList();
      }

   }

   public void deleteAllDisplayLists() {
   }

   public void setWorldAndLoadRenderers(WorldClient var1) {
      if (this.theWorld != null) {
         this.theWorld.removeWorldAccess(this);
      }

      this.frustumUpdatePosX = Double.MIN_VALUE;
      this.frustumUpdatePosY = Double.MIN_VALUE;
      this.frustumUpdatePosZ = Double.MIN_VALUE;
      this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
      this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
      this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
      this.renderManager.set(var1);
      this.theWorld = var1;
      if (Config.isDynamicLights()) {
         DynamicLights.clear();
      }

      if (var1 != null) {
         var1.addWorldAccess(this);
         this.loadRenderers();
      }

   }

   public void drawBlockDamageTexture(Tessellator var1, WorldRenderer var2, Entity var3, float var4) {
      double var5 = var3.lastTickPosX + (var3.posX - var3.lastTickPosX) * (double)var4;
      double var7 = var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double)var4;
      double var9 = var3.lastTickPosZ + (var3.posZ - var3.lastTickPosZ) * (double)var4;
      if (!this.damagedBlocks.isEmpty()) {
         renderEngine.bindTexture(TextureMap.locationBlocksTexture);
         this.preRenderDamagedBlocks();
         var2.begin(7, DefaultVertexFormats.BLOCK);
         var2.setTranslation(-var5, -var7, -var9);
         var2.markDirty();
         Iterator var11 = this.damagedBlocks.values().iterator();

         while(var11.hasNext()) {
            DestroyBlockProgress var12 = (DestroyBlockProgress)var11.next();
            BlockPos var13 = var12.getPosition();
            double var14 = (double)var13.getX() - var5;
            double var16 = (double)var13.getY() - var7;
            double var18 = (double)var13.getZ() - var9;
            Block var20 = this.theWorld.getBlockState(var13).getBlock();
            boolean var21;
            if (Reflector.ForgeTileEntity_canRenderBreaking.exists()) {
               boolean var22 = var20 instanceof BlockChest || var20 instanceof BlockEnderChest || var20 instanceof BlockSign || var20 instanceof BlockSkull;
               if (!var22) {
                  TileEntity var23 = this.theWorld.getTileEntity(var13);
                  if (var23 != null) {
                     var22 = Reflector.callBoolean(var23, Reflector.ForgeTileEntity_canRenderBreaking);
                  }
               }

               var21 = !var22;
            } else {
               var21 = !(var20 instanceof BlockChest) && !(var20 instanceof BlockEnderChest) && !(var20 instanceof BlockSign) && !(var20 instanceof BlockSkull);
            }

            if (var21) {
               if (var14 * var14 + var16 * var16 + var18 * var18 > 1024.0D) {
                  var11.remove();
               } else {
                  IBlockState var26 = this.theWorld.getBlockState(var13);
                  if (var26.getBlock().getMaterial() != Material.air) {
                     int var27 = var12.getPartialBlockDamage();
                     TextureAtlasSprite var24 = this.destroyBlockIcons[var27];
                     BlockRendererDispatcher var25 = this.mc.getBlockRendererDispatcher();
                     var25.renderBlockDamage(var26, var13, var24, this.theWorld);
                  }
               }
            }
         }

         var1.draw();
         var2.setTranslation(0.0D, 0.0D, 0.0D);
         this.postRenderDamagedBlocks();
      }

   }

   protected void stopChunkUpdates() {
      this.chunksToUpdate.clear();
      this.renderDispatcher.stopChunkUpdates();
   }

   public void updateClouds() {
      if (Config.isShaders() && Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
         Shaders.uninit();
         Shaders.loadShaderPack();
      }

      ++this.cloudTickCounter;
      if (this.cloudTickCounter % 20 == 0) {
         this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
      }

   }

   public void renderSky(float var1, int var2) {
      if (Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
         WorldProvider var3 = Minecraft.theWorld.provider;
         Object var4 = Reflector.call(var3, Reflector.ForgeWorldProvider_getSkyRenderer);
         if (var4 != null) {
            Reflector.callVoid(var4, Reflector.IRenderHandler_render, var1, this.theWorld, this.mc);
            return;
         }
      }

      if (Minecraft.theWorld.provider.getDimensionId() == 1) {
         renderSkyEnd();
      } else if (Minecraft.theWorld.provider.isSurfaceWorld()) {
         GlStateManager.disableTexture2D();
         boolean var20 = Config.isShaders();
         if (var20) {
            Shaders.disableTexture2D();
         }

         Vec3 var21 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), var1);
         var21 = CustomColors.getSkyColor(var21, Minecraft.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);
         if (var20) {
            Shaders.setSkyColor(var21);
         }

         float var5 = (float)var21.xCoord;
         float var6 = (float)var21.yCoord;
         float var7 = (float)var21.zCoord;
         if (var2 != 2) {
            float var8 = (var5 * 30.0F + var6 * 59.0F + var7 * 11.0F) / 100.0F;
            float var9 = (var5 * 30.0F + var6 * 70.0F) / 100.0F;
            float var10 = (var5 * 30.0F + var7 * 70.0F) / 100.0F;
            var5 = var8;
            var6 = var9;
            var7 = var10;
         }

         GlStateManager.color(var5, var6, var7);
         Tessellator var22 = Tessellator.getInstance();
         WorldRenderer var23 = var22.getWorldRenderer();
         GlStateManager.depthMask(false);
         GlStateManager.enableFog();
         if (var20) {
            Shaders.enableFog();
         }

         GlStateManager.color(var5, var6, var7);
         if (var20) {
            Shaders.preSkyList();
         }

         if (Config.isSkyEnabled()) {
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
         }

         GlStateManager.disableFog();
         if (var20) {
            Shaders.disableFog();
         }

         GlStateManager.disableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         RenderHelper.disableStandardItemLighting();
         float[] var24 = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(var1), var1);
         float var11;
         float var12;
         float var13;
         float var16;
         float var17;
         float var18;
         int var29;
         if (var24 != null && Config.isSunMoonEnabled()) {
            GlStateManager.disableTexture2D();
            if (var20) {
               Shaders.disableTexture2D();
            }

            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(MathHelper.sin(this.theWorld.getCelestialAngleRadians(var1)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            var11 = var24[0];
            var12 = var24[1];
            var13 = var24[2];
            if (var2 != 2) {
               float var14 = (var11 * 30.0F + var12 * 59.0F + var13 * 11.0F) / 100.0F;
               float var15 = (var11 * 30.0F + var12 * 70.0F) / 100.0F;
               var16 = (var11 * 30.0F + var13 * 70.0F) / 100.0F;
               var11 = var14;
               var12 = var15;
               var13 = var16;
            }

            var23.begin(6, DefaultVertexFormats.POSITION_COLOR);
            var23.pos(0.0D, 100.0D, 0.0D).color(var11, var12, var13, var24[3]).endVertex();
            boolean var26 = true;

            for(var29 = 0; var29 <= 16; ++var29) {
               var16 = (float)var29 * 3.1415927F * 2.0F / 16.0F;
               var17 = MathHelper.sin(var16);
               var18 = MathHelper.cos(var16);
               var23.pos((double)(var17 * 120.0F), (double)(var18 * 120.0F), (double)(-var18 * 40.0F * var24[3])).color(var24[0], var24[1], var24[2], 0.0F).endVertex();
            }

            var22.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
         }

         GlStateManager.enableTexture2D();
         if (var20) {
            Shaders.enableTexture2D();
         }

         GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
         GlStateManager.pushMatrix();
         var11 = 1.0F - this.theWorld.getRainStrength(var1);
         GlStateManager.color(1.0F, 1.0F, 1.0F, var11);
         GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
         CustomSky.renderSky(this.theWorld, renderEngine, this.theWorld.getCelestialAngle(var1), var11);
         if (var20) {
            Shaders.preCelestialRotate();
         }

         GlStateManager.rotate(this.theWorld.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
         if (var20) {
            Shaders.postCelestialRotate();
         }

         var12 = 30.0F;
         if (Config.isSunTexture()) {
            renderEngine.bindTexture(locationSunPng);
            var23.begin(7, DefaultVertexFormats.POSITION_TEX);
            var23.pos((double)(-var12), 100.0D, (double)(-var12)).tex(0.0D, 0.0D).endVertex();
            var23.pos((double)var12, 100.0D, (double)(-var12)).tex(1.0D, 0.0D).endVertex();
            var23.pos((double)var12, 100.0D, (double)var12).tex(1.0D, 1.0D).endVertex();
            var23.pos((double)(-var12), 100.0D, (double)var12).tex(0.0D, 1.0D).endVertex();
            var22.draw();
         }

         var12 = 20.0F;
         if (Config.isMoonTexture()) {
            renderEngine.bindTexture(locationMoonPhasesPng);
            int var25 = this.theWorld.getMoonPhase();
            int var27 = var25 % 4;
            var29 = var25 / 4 % 2;
            var16 = (float)(var27 + 0) / 4.0F;
            var17 = (float)(var29 + 0) / 2.0F;
            var18 = (float)(var27 + 1) / 4.0F;
            float var19 = (float)(var29 + 1) / 2.0F;
            var23.begin(7, DefaultVertexFormats.POSITION_TEX);
            var23.pos((double)(-var12), -100.0D, (double)var12).tex((double)var18, (double)var19).endVertex();
            var23.pos((double)var12, -100.0D, (double)var12).tex((double)var16, (double)var19).endVertex();
            var23.pos((double)var12, -100.0D, (double)(-var12)).tex((double)var16, (double)var17).endVertex();
            var23.pos((double)(-var12), -100.0D, (double)(-var12)).tex((double)var18, (double)var17).endVertex();
            var22.draw();
         }

         GlStateManager.disableTexture2D();
         if (var20) {
            Shaders.disableTexture2D();
         }

         var13 = this.theWorld.getStarBrightness(var1) * var11;
         if (var13 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.theWorld)) {
            GlStateManager.color(var13, var13, var13, var13);
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
         if (var20) {
            Shaders.enableFog();
         }

         GlStateManager.popMatrix();
         GlStateManager.disableTexture2D();
         if (var20) {
            Shaders.disableTexture2D();
         }

         GlStateManager.color(0.0F, 0.0F, 0.0F);
         double var28 = Minecraft.thePlayer.getPositionEyes(var1).yCoord - this.theWorld.getHorizon();
         if (var28 < 0.0D) {
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
            var16 = 1.0F;
            var17 = -((float)(var28 + 65.0D));
            var18 = -1.0F;
            var23.begin(7, DefaultVertexFormats.POSITION_COLOR);
            var23.pos(-1.0D, (double)var17, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, (double)var17, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, (double)var17, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, (double)var17, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, (double)var17, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, (double)var17, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, (double)var17, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, (double)var17, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var23.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var22.draw();
         }

         if (this.theWorld.provider.isSkyColored()) {
            GlStateManager.color(var5 * 0.2F + 0.04F, var6 * 0.2F + 0.04F, var7 * 0.6F + 0.1F);
         } else {
            GlStateManager.color(var5, var6, var7);
         }

         if (this.mc.gameSettings.renderDistanceChunks <= 4) {
            GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
         }

         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, -((float)(var28 - 16.0D)), 0.0F);
         if (Config.isSkyEnabled()) {
            GlStateManager.callList(this.glSkyList2);
         }

         GlStateManager.popMatrix();
         GlStateManager.enableTexture2D();
         if (var20) {
            Shaders.enableTexture2D();
         }

         GlStateManager.depthMask(true);
      }

   }

   public int getCountTileEntitiesRendered() {
      return this.countTileEntitiesRendered;
   }

   public void resetClouds() {
      this.cloudRenderer.reset();
   }

   public void markBlockForUpdate(BlockPos var1) {
      int var2 = var1.getX();
      int var3 = var1.getY();
      int var4 = var1.getZ();
      this.markBlocksForUpdate(var2 - 1, var3 - 1, var4 - 1, var2 + 1, var3 + 1, var4 + 1);
   }

   private void renderBlockLayer(EnumWorldBlockLayer var1) {
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

      if (Config.isShaders()) {
         ShadersRender.preRenderChunkLayer(var1);
      }

      this.renderContainer.renderChunkLayer(var1);
      if (Config.isShaders()) {
         ShadersRender.postRenderChunkLayer(var1);
      }

      if (OpenGlHelper.useVbo()) {
         Iterator var3 = DefaultVertexFormats.BLOCK.getElements().iterator();

         while(var3.hasNext()) {
            VertexFormatElement var2 = (VertexFormatElement)var3.next();
            VertexFormatElement.EnumUsage var4 = var2.getUsage();
            int var5 = var2.getIndex();
            switch(var4) {
            case POSITION:
               GL11.glDisableClientState(32884);
               break;
            case UV:
               OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var5);
               GL11.glDisableClientState(32888);
               OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
               break;
            case COLOR:
               GL11.glDisableClientState(32886);
               GlStateManager.resetColor();
            }
         }
      }

      this.mc.entityRenderer.disableLightmap();
   }

   public void updateChunks(long var1) {
      var1 = (long)((double)var1 + 1.0E8D);
      this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(var1);
      Iterator var3;
      RenderChunk var4;
      if (this.chunksToUpdateForced.size() > 0) {
         var3 = this.chunksToUpdateForced.iterator();

         while(var3.hasNext()) {
            var4 = (RenderChunk)var3.next();
            if (!this.renderDispatcher.updateChunkLater(var4)) {
               break;
            }

            var4.setNeedsUpdate(false);
            var3.remove();
            this.chunksToUpdate.remove(var4);
            this.chunksToResortTransparency.remove(var4);
         }
      }

      if (this.chunksToResortTransparency.size() > 0) {
         var3 = this.chunksToResortTransparency.iterator();
         if (var3.hasNext()) {
            var4 = (RenderChunk)var3.next();
            if (this.renderDispatcher.updateTransparencyLater(var4)) {
               var3.remove();
            }
         }
      }

      int var9 = 0;
      int var10 = Config.getUpdatesPerFrame();
      int var5 = var10 * 2;
      Iterator var6 = this.chunksToUpdate.iterator();

      while(var6.hasNext()) {
         RenderChunk var7 = (RenderChunk)var6.next();
         if (!this.renderDispatcher.updateChunkLater(var7)) {
            break;
         }

         var7.setNeedsUpdate(false);
         var6.remove();
         if (var7.getCompiledChunk().isEmpty() && var10 < var5) {
            ++var10;
         }

         ++var9;
         if (var9 >= var10) {
            break;
         }
      }

   }

   public void onEntityAdded(Entity var1) {
      RandomMobs.entityLoaded(var1, this.theWorld);
      if (Config.isDynamicLights()) {
         DynamicLights.entityAdded(var1, this);
      }

   }

   private void renderSky(WorldRenderer var1, float var2, boolean var3) {
      boolean var4 = true;
      boolean var5 = true;
      var1.begin(7, DefaultVertexFormats.POSITION);

      for(int var6 = -384; var6 <= 384; var6 += 64) {
         for(int var7 = -384; var7 <= 384; var7 += 64) {
            float var8 = (float)var6;
            float var9 = (float)(var6 + 64);
            if (var3) {
               var9 = (float)var6;
               var8 = (float)(var6 + 64);
            }

            var1.pos((double)var8, (double)var2, (double)var7).endVertex();
            var1.pos((double)var9, (double)var2, (double)var7).endVertex();
            var1.pos((double)var9, (double)var2, (double)(var7 + 64)).endVertex();
            var1.pos((double)var8, (double)var2, (double)(var7 + 64)).endVertex();
         }
      }

   }

   public String getDebugInfoRenders() {
      int var1 = this.viewFrustum.renderChunks.length;
      int var2 = 0;
      Iterator var4 = this.renderInfos.iterator();

      while(var4.hasNext()) {
         Object var3 = var4.next();
         RenderGlobal.ContainerLocalRenderInformation var5 = (RenderGlobal.ContainerLocalRenderInformation)var3;
         CompiledChunk var6 = var5.renderChunk.compiledChunk;
         if (var6 != CompiledChunk.DUMMY && !var6.isEmpty()) {
            ++var2;
         }
      }

      return String.format("C: %d/%d %sD: %d, %s", var2, var1, this.mc.renderChunksMany ? "(s) " : "", this.renderDistanceChunks, this.renderDispatcher.getDebugInfo());
   }

   public void spawnParticle(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      try {
         this.spawnEntityFX(var1, var2, var3, var5, var7, var9, var11, var13, var15);
      } catch (Throwable var20) {
         CrashReport var17 = CrashReport.makeCrashReport(var20, "Exception while adding particle");
         CrashReportCategory var18 = var17.makeCategory("Particle being added");
         var18.addCrashSection("ID", var1);
         if (var15 != null) {
            var18.addCrashSection("Parameters", var15);
         }

         var18.addCrashSectionCallable("Position", new Callable(this, var3, var5, var7) {
            private final double val$xCoord;
            final RenderGlobal this$0;
            private final double val$yCoord;
            private final double val$zCoord;
            private static final String __OBFID = "CL_00000955";

            {
               this.this$0 = var1;
               this.val$xCoord = var2;
               this.val$yCoord = var4;
               this.val$zCoord = var6;
            }

            public Object call() throws Exception {
               return this.call();
            }

            public String call() throws Exception {
               return CrashReportCategory.getCoordinateInfo(this.val$xCoord, this.val$yCoord, this.val$zCoord);
            }
         });
         throw new ReportedException(var17);
      }
   }

   public void broadcastSound(int var1, BlockPos var2, int var3) {
      switch(var1) {
      case 1013:
      case 1018:
         if (this.mc.getRenderViewEntity() != null) {
            double var4 = (double)var2.getX() - this.mc.getRenderViewEntity().posX;
            double var6 = (double)var2.getY() - this.mc.getRenderViewEntity().posY;
            double var8 = (double)var2.getZ() - this.mc.getRenderViewEntity().posZ;
            double var10 = Math.sqrt(var4 * var4 + var6 * var6 + var8 * var8);
            double var12 = this.mc.getRenderViewEntity().posX;
            double var14 = this.mc.getRenderViewEntity().posY;
            double var16 = this.mc.getRenderViewEntity().posZ;
            if (var10 > 0.0D) {
               var12 += var4 / var10 * 2.0D;
               var14 += var6 / var10 * 2.0D;
               var16 += var8 / var10 * 2.0D;
            }

            if (var1 == 1013) {
               this.theWorld.playSound(var12, var14, var16, "mob.wither.spawn", 1.0F, 1.0F, false);
            } else {
               this.theWorld.playSound(var12, var14, var16, "mob.enderdragon.end", 5.0F, 1.0F, false);
            }
         }
      default:
      }
   }

   public void loadRenderers() {
      if (this.theWorld != null) {
         this.displayListEntitiesDirty = true;
         Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
         Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
         BlockModelRenderer.updateAoLightValue();
         if (Config.isDynamicLights()) {
            DynamicLights.clear();
         }

         this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
         this.renderDistance = this.renderDistanceChunks * 16;
         this.renderDistanceSq = this.renderDistance * this.renderDistance;
         boolean var1 = this.vboEnabled;
         this.vboEnabled = OpenGlHelper.useVbo();
         if (var1 && !this.vboEnabled) {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
         } else if (!var1 && this.vboEnabled) {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
         }

         if (var1 != this.vboEnabled) {
            this.generateStars();
            this.generateSky();
            this.generateSky2();
         }

         if (this.viewFrustum != null) {
            this.viewFrustum.deleteGlResources();
         }

         this.stopChunkUpdates();
         Set var2 = this.field_181024_n;
         Set var3;
         synchronized(var3 = this.field_181024_n){}
         this.field_181024_n.clear();
         this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
         if (this.theWorld != null) {
            Entity var4 = this.mc.getRenderViewEntity();
            if (var4 != null) {
               this.viewFrustum.updateChunkPositions(var4.posX, var4.posZ);
            }
         }

         this.renderEntitiesStartupCounter = 2;
      }

   }

   private void generateSky2() {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      if (this.sky2VBO != null) {
         this.sky2VBO.deleteGlBuffers();
      }

      if (this.glSkyList2 >= 0) {
         GLAllocation.deleteDisplayLists(this.glSkyList2);
         this.glSkyList2 = -1;
      }

      if (this.vboEnabled) {
         this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderSky(var2, -16.0F, true);
         var2.finishDrawing();
         var2.reset();
         this.sky2VBO.func_181722_a(var2.getByteBuffer());
      } else {
         this.glSkyList2 = GLAllocation.generateDisplayLists(1);
         GL11.glNewList(this.glSkyList2, 4864);
         this.renderSky(var2, -16.0F, true);
         var1.draw();
         GL11.glEndList();
      }

   }

   private void spawnParticle(EnumParticleTypes var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      this.spawnParticle(var1.getParticleID(), var1.getShouldIgnoreRange(), var2, var4, var6, var8, var10, var12, var14);
   }

   public void renderClouds(float var1, int var2) {
      if (!Config.isCloudsOff()) {
         if (Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
            WorldProvider var3 = Minecraft.theWorld.provider;
            Object var4 = Reflector.call(var3, Reflector.ForgeWorldProvider_getCloudRenderer);
            if (var4 != null) {
               Reflector.callVoid(var4, Reflector.IRenderHandler_render, var1, this.theWorld, this.mc);
               return;
            }
         }

         if (Minecraft.theWorld.provider.isSurfaceWorld()) {
            if (Config.isShaders()) {
               Shaders.beginClouds();
            }

            if (Config.isCloudsFancy()) {
               this.renderCloudsFancy(var1, var2);
            } else {
               this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, var1);
               var1 = 0.0F;
               GlStateManager.disableCull();
               float var26 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)var1);
               boolean var27 = true;
               boolean var5 = true;
               Tessellator var6 = Tessellator.getInstance();
               WorldRenderer var7 = var6.getWorldRenderer();
               renderEngine.bindTexture(locationCloudsPng);
               GlStateManager.enableBlend();
               GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
               if (this.cloudRenderer.shouldUpdateGlList()) {
                  this.cloudRenderer.startUpdateGlList();
                  Vec3 var8 = this.theWorld.getCloudColour(var1);
                  float var9 = (float)var8.xCoord;
                  float var10 = (float)var8.yCoord;
                  float var11 = (float)var8.zCoord;
                  float var12;
                  if (var2 != 2) {
                     var12 = (var9 * 30.0F + var10 * 59.0F + var11 * 11.0F) / 100.0F;
                     float var13 = (var9 * 30.0F + var10 * 70.0F) / 100.0F;
                     float var14 = (var9 * 30.0F + var11 * 70.0F) / 100.0F;
                     var9 = var12;
                     var10 = var13;
                     var11 = var14;
                  }

                  var12 = 4.8828125E-4F;
                  double var28 = (double)((float)this.cloudTickCounter + var1);
                  double var15 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)var1 + var28 * 0.029999999329447746D;
                  double var17 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)var1;
                  int var19 = MathHelper.floor_double(var15 / 2048.0D);
                  int var20 = MathHelper.floor_double(var17 / 2048.0D);
                  var15 -= (double)(var19 * 2048);
                  var17 -= (double)(var20 * 2048);
                  float var21 = this.theWorld.provider.getCloudHeight() - var26 + 0.33F;
                  var21 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
                  float var22 = (float)(var15 * 4.8828125E-4D);
                  float var23 = (float)(var17 * 4.8828125E-4D);
                  var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

                  for(int var24 = -256; var24 < 256; var24 += 32) {
                     for(int var25 = -256; var25 < 256; var25 += 32) {
                        var7.pos((double)(var24 + 0), (double)var21, (double)(var25 + 32)).tex((double)((float)(var24 + 0) * 4.8828125E-4F + var22), (double)((float)(var25 + 32) * 4.8828125E-4F + var23)).color(var9, var10, var11, 0.8F).endVertex();
                        var7.pos((double)(var24 + 32), (double)var21, (double)(var25 + 32)).tex((double)((float)(var24 + 32) * 4.8828125E-4F + var22), (double)((float)(var25 + 32) * 4.8828125E-4F + var23)).color(var9, var10, var11, 0.8F).endVertex();
                        var7.pos((double)(var24 + 32), (double)var21, (double)(var25 + 0)).tex((double)((float)(var24 + 32) * 4.8828125E-4F + var22), (double)((float)(var25 + 0) * 4.8828125E-4F + var23)).color(var9, var10, var11, 0.8F).endVertex();
                        var7.pos((double)(var24 + 0), (double)var21, (double)(var25 + 0)).tex((double)((float)(var24 + 0) * 4.8828125E-4F + var22), (double)((float)(var25 + 0) * 4.8828125E-4F + var23)).color(var9, var10, var11, 0.8F).endVertex();
                     }
                  }

                  var6.draw();
                  this.cloudRenderer.endUpdateGlList();
               }

               this.cloudRenderer.renderGlList();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.disableBlend();
               GlStateManager.enableCull();
            }

            if (Config.isShaders()) {
               Shaders.endClouds();
            }
         }
      }

   }

   private void postRenderDamagedBlocks() {
      GlStateManager.disableAlpha();
      GlStateManager.doPolygonOffset(0.0F, 0.0F);
      GlStateManager.disablePolygonOffset();
      GlStateManager.enableAlpha();
      GlStateManager.depthMask(true);
      GlStateManager.popMatrix();
      if (Config.isShaders()) {
         ShadersRender.endBlockDamage();
      }

   }

   public void onResourceManagerReload(IResourceManager var1) {
      this.updateDestroyBlockIcons();
   }

   public void playRecord(String var1, BlockPos var2) {
      ISound var3 = (ISound)this.mapSoundPositions.get(var2);
      if (var3 != null) {
         this.mc.getSoundHandler().stopSound(var3);
         this.mapSoundPositions.remove(var2);
      }

      if (var1 != null) {
         ItemRecord var4 = ItemRecord.getRecord(var1);
         if (var4 != null) {
            this.mc.ingameGUI.setRecordPlayingMessage(var4.getRecordNameLocal());
         }

         ResourceLocation var5 = null;
         if (Reflector.ForgeItemRecord_getRecordResource.exists() && var4 != null) {
            var5 = (ResourceLocation)Reflector.call(var4, Reflector.ForgeItemRecord_getRecordResource, var1);
         }

         if (var5 == null) {
            var5 = new ResourceLocation(var1);
         }

         PositionedSoundRecord var6 = PositionedSoundRecord.create(var5, (float)var2.getX(), (float)var2.getY(), (float)var2.getZ());
         this.mapSoundPositions.put(var2, var6);
         this.mc.getSoundHandler().playSound(var6);
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
      if (Config.isShaders()) {
         ShadersRender.beginBlockDamage();
      }

   }

   public void renderWorldBorder(Entity var1, float var2) {
      Tessellator var3 = Tessellator.getInstance();
      WorldRenderer var4 = var3.getWorldRenderer();
      WorldBorder var5 = this.theWorld.getWorldBorder();
      double var6 = (double)(this.mc.gameSettings.renderDistanceChunks * 16);
      if (var1.posX >= var5.maxX() - var6 || var1.posX <= var5.minX() + var6 || var1.posZ >= var5.maxZ() - var6 || var1.posZ <= var5.minZ() + var6) {
         double var8 = 1.0D - var5.getClosestDistance(var1) / var6;
         var8 = Math.pow(var8, 4.0D);
         double var10 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
         double var12 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
         double var14 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
         renderEngine.bindTexture(locationForcefieldPng);
         GlStateManager.depthMask(false);
         GlStateManager.pushMatrix();
         int var16 = var5.getStatus().getID();
         float var17 = (float)(var16 >> 16 & 255) / 255.0F;
         float var18 = (float)(var16 >> 8 & 255) / 255.0F;
         float var19 = (float)(var16 & 255) / 255.0F;
         GlStateManager.color(var17, var18, var19, (float)var8);
         GlStateManager.doPolygonOffset(-3.0F, -3.0F);
         GlStateManager.enablePolygonOffset();
         GlStateManager.alphaFunc(516, 0.1F);
         GlStateManager.enableAlpha();
         GlStateManager.disableCull();
         float var20 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
         float var21 = 0.0F;
         float var22 = 0.0F;
         float var23 = 128.0F;
         var4.begin(7, DefaultVertexFormats.POSITION_TEX);
         var4.setTranslation(-var10, -var12, -var14);
         double var24 = Math.max((double)MathHelper.floor_double(var14 - var6), var5.minZ());
         double var26 = Math.min((double)MathHelper.ceiling_double_int(var14 + var6), var5.maxZ());
         float var28;
         double var29;
         double var31;
         float var33;
         if (var10 > var5.maxX() - var6) {
            var28 = 0.0F;

            for(var29 = var24; var29 < var26; var28 += 0.5F) {
               var31 = Math.min(1.0D, var26 - var29);
               var33 = (float)var31 * 0.5F;
               var4.pos(var5.maxX(), 256.0D, var29).tex((double)(var20 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var5.maxX(), 256.0D, var29 + var31).tex((double)(var20 + var33 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var5.maxX(), 0.0D, var29 + var31).tex((double)(var20 + var33 + var28), (double)(var20 + 128.0F)).endVertex();
               var4.pos(var5.maxX(), 0.0D, var29).tex((double)(var20 + var28), (double)(var20 + 128.0F)).endVertex();
               ++var29;
            }
         }

         if (var10 < var5.minX() + var6) {
            var28 = 0.0F;

            for(var29 = var24; var29 < var26; var28 += 0.5F) {
               var31 = Math.min(1.0D, var26 - var29);
               var33 = (float)var31 * 0.5F;
               var4.pos(var5.minX(), 256.0D, var29).tex((double)(var20 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var5.minX(), 256.0D, var29 + var31).tex((double)(var20 + var33 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var5.minX(), 0.0D, var29 + var31).tex((double)(var20 + var33 + var28), (double)(var20 + 128.0F)).endVertex();
               var4.pos(var5.minX(), 0.0D, var29).tex((double)(var20 + var28), (double)(var20 + 128.0F)).endVertex();
               ++var29;
            }
         }

         var24 = Math.max((double)MathHelper.floor_double(var10 - var6), var5.minX());
         var26 = Math.min((double)MathHelper.ceiling_double_int(var10 + var6), var5.maxX());
         if (var14 > var5.maxZ() - var6) {
            var28 = 0.0F;

            for(var29 = var24; var29 < var26; var28 += 0.5F) {
               var31 = Math.min(1.0D, var26 - var29);
               var33 = (float)var31 * 0.5F;
               var4.pos(var29, 256.0D, var5.maxZ()).tex((double)(var20 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var29 + var31, 256.0D, var5.maxZ()).tex((double)(var20 + var33 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var29 + var31, 0.0D, var5.maxZ()).tex((double)(var20 + var33 + var28), (double)(var20 + 128.0F)).endVertex();
               var4.pos(var29, 0.0D, var5.maxZ()).tex((double)(var20 + var28), (double)(var20 + 128.0F)).endVertex();
               ++var29;
            }
         }

         if (var14 < var5.minZ() + var6) {
            var28 = 0.0F;

            for(var29 = var24; var29 < var26; var28 += 0.5F) {
               var31 = Math.min(1.0D, var26 - var29);
               var33 = (float)var31 * 0.5F;
               var4.pos(var29, 256.0D, var5.minZ()).tex((double)(var20 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var29 + var31, 256.0D, var5.minZ()).tex((double)(var20 + var33 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var29 + var31, 0.0D, var5.minZ()).tex((double)(var20 + var33 + var28), (double)(var20 + 128.0F)).endVertex();
               var4.pos(var29, 0.0D, var5.minZ()).tex((double)(var20 + var28), (double)(var20 + 128.0F)).endVertex();
               ++var29;
            }
         }

         var3.draw();
         var4.setTranslation(0.0D, 0.0D, 0.0D);
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

   private void fixTerrainFrustum(double var1, double var3, double var5) {
      this.debugFixedClippingHelper = new ClippingHelperImpl();
      ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
      Matrix4f var7 = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
      var7.transpose();
      Matrix4f var8 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
      var8.transpose();
      Matrix4f var9 = new Matrix4f();
      Matrix4f.mul(var8, var7, var9);
      var9.invert();
      this.debugTerrainFrustumPosition.field_181059_a = var1;
      this.debugTerrainFrustumPosition.field_181060_b = var3;
      this.debugTerrainFrustumPosition.field_181061_c = var5;
      this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);

      for(int var10 = 0; var10 < 8; ++var10) {
         Matrix4f.transform(var9, this.debugTerrainMatrix[var10], this.debugTerrainMatrix[var10]);
         Vector4f var10000 = this.debugTerrainMatrix[var10];
         var10000.x /= this.debugTerrainMatrix[var10].w;
         var10000 = this.debugTerrainMatrix[var10];
         var10000.y /= this.debugTerrainMatrix[var10].w;
         var10000 = this.debugTerrainMatrix[var10];
         var10000.z /= this.debugTerrainMatrix[var10].w;
         this.debugTerrainMatrix[var10].w = 1.0F;
      }

   }

   private RenderChunk func_181562_a(BlockPos var1, RenderChunk var2, EnumFacing var3) {
      BlockPos var4 = var2.getPositionOffset16(var3);
      if (var4.getY() >= 0 && var4.getY() < 256) {
         int var5 = MathHelper.abs_int(var1.getX() - var4.getX());
         int var6 = MathHelper.abs_int(var1.getZ() - var4.getZ());
         if (Config.isFogOff()) {
            if (var5 > this.renderDistance || var6 > this.renderDistance) {
               return null;
            }
         } else {
            int var7 = var5 * var5 + var6 * var6;
            if (var7 > this.renderDistanceSq) {
               return null;
            }
         }

         return this.viewFrustum.getRenderChunk(var4);
      } else {
         return null;
      }
   }

   public int getCountEntitiesRendered() {
      return this.countEntitiesRendered;
   }

   protected Vector3f getViewVector(Entity var1, double var2) {
      float var4 = (float)((double)var1.prevRotationPitch + (double)(var1.rotationPitch - var1.prevRotationPitch) * var2);
      float var5 = (float)((double)var1.prevRotationYaw + (double)(var1.rotationYaw - var1.prevRotationYaw) * var2);
      if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
         var4 += 180.0F;
      }

      float var6 = MathHelper.cos(-var5 * 0.017453292F - 3.1415927F);
      float var7 = MathHelper.sin(-var5 * 0.017453292F - 3.1415927F);
      float var8 = -MathHelper.cos(-var4 * 0.017453292F);
      float var9 = MathHelper.sin(-var4 * 0.017453292F);
      return new Vector3f(var7 * var8, var9, var6 * var8);
   }

   public void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var4 + 1, var5 + 1, var6 + 1);
   }

   public void drawSelectionBox(EntityPlayer var1, MovingObjectPosition var2, int var3, float var4) {
      if (var3 == 0 && var2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
         GL11.glLineWidth(2.0F);
         GlStateManager.disableTexture2D();
         if (Config.isShaders()) {
            Shaders.disableTexture2D();
         }

         GlStateManager.depthMask(false);
         float var5 = 0.002F;
         BlockPos var6 = var2.getBlockPos();
         Block var7 = this.theWorld.getBlockState(var6).getBlock();
         if (var7.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(var6)) {
            var7.setBlockBoundsBasedOnState(this.theWorld, var6);
            double var8 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var4;
            double var10 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var4;
            double var12 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var4;
            func_181561_a(var7.getSelectedBoundingBox(this.theWorld, var6).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-var8, -var10, -var12));
         }

         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         if (Config.isShaders()) {
            Shaders.enableTexture2D();
         }

         GlStateManager.disableBlend();
      }

   }

   private EntityFX spawnEntityFX(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      if (this.mc != null && this.mc.getRenderViewEntity() != null && this.mc.effectRenderer != null) {
         int var16 = this.mc.gameSettings.particleSetting;
         if (var16 == 1 && this.theWorld.rand.nextInt(3) == 0) {
            var16 = 2;
         }

         double var17 = this.mc.getRenderViewEntity().posX - var3;
         double var19 = this.mc.getRenderViewEntity().posY - var5;
         double var21 = this.mc.getRenderViewEntity().posZ - var7;
         if (var1 == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
         } else if (var1 == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
         } else if (var1 == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
         } else if (var1 == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles()) {
            return null;
         } else if (var1 == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles()) {
            return null;
         } else if (var1 == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke()) {
            return null;
         } else if (var1 == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke()) {
            return null;
         } else if (var1 == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if (var1 == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if (var1 == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if (var1 == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if (var1 == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if (var1 == EnumParticleTypes.PORTAL.getParticleID() && !Config.isAnimatedPortal()) {
            return null;
         } else if (var1 == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame()) {
            return null;
         } else if (var1 == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone()) {
            return null;
         } else if (var1 == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava()) {
            return null;
         } else if (var1 == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava()) {
            return null;
         } else if (var1 == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles()) {
            return null;
         } else if (var2) {
            return this.mc.effectRenderer.spawnEffectParticle(var1, var3, var5, var7, var9, var11, var13, var15);
         } else {
            double var23 = 16.0D;
            double var25 = 256.0D;
            if (var1 == EnumParticleTypes.CRIT.getParticleID()) {
               var25 = 38416.0D;
            }

            if (var17 * var17 + var19 * var19 + var21 * var21 > var25) {
               return null;
            } else if (var16 > 1) {
               return null;
            } else {
               EntityFX var27 = this.mc.effectRenderer.spawnEffectParticle(var1, var3, var5, var7, var9, var11, var13, var15);
               if (var1 == EnumParticleTypes.WATER_BUBBLE.getParticleID()) {
                  CustomColors.updateWaterFX(var27, this.theWorld, var3, var5, var7);
               }

               if (var1 == EnumParticleTypes.WATER_SPLASH.getParticleID()) {
                  CustomColors.updateWaterFX(var27, this.theWorld, var3, var5, var7);
               }

               if (var1 == EnumParticleTypes.WATER_DROP.getParticleID()) {
                  CustomColors.updateWaterFX(var27, this.theWorld, var3, var5, var7);
               }

               if (var1 == EnumParticleTypes.TOWN_AURA.getParticleID()) {
                  CustomColors.updateMyceliumFX(var27);
               }

               if (var1 == EnumParticleTypes.PORTAL.getParticleID()) {
                  CustomColors.updatePortalFX(var27);
               }

               if (var1 == EnumParticleTypes.REDSTONE.getParticleID()) {
                  CustomColors.updateReddustFX(var27, this.theWorld, var3, var5, var7);
               }

               return var27;
            }
         }
      } else {
         return null;
      }
   }

   public void renderEntityOutlineFramebuffer() {
      if (this != false) {
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
         this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
         GlStateManager.disableBlend();
      }

   }

   public RenderGlobal(Minecraft var1) {
      this.cloudRenderer = new CloudRenderer(var1);
      this.mc = var1;
      this.renderManager = var1.getRenderManager();
      renderEngine = var1.getTextureManager();
      renderEngine.bindTexture(locationForcefieldPng);
      GL11.glTexParameteri(3553, 10242, 10497);
      GL11.glTexParameteri(3553, 10243, 10497);
      GlStateManager.bindTexture(0);
      this.updateDestroyBlockIcons();
      this.vboEnabled = OpenGlHelper.useVbo();
      if (this.vboEnabled) {
         this.renderContainer = new VboRenderList();
         this.renderChunkFactory = new VboChunkFactory();
      } else {
         this.renderContainer = new RenderList();
         this.renderChunkFactory = new ListChunkFactory();
      }

      this.vertexBufferFormat = new VertexFormat();
      this.vertexBufferFormat.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
      this.generateStars();
      this.generateSky();
      this.generateSky2();
   }

   public void playSoundToNearExcept(EntityPlayer var1, String var2, double var3, double var5, double var7, float var9, float var10) {
   }

   private void cleanupDamagedBlocks(Iterator var1) {
      while(var1.hasNext()) {
         DestroyBlockProgress var2 = (DestroyBlockProgress)var1.next();
         int var3 = var2.getCreationCloudUpdateTick();
         if (this.cloudTickCounter - var3 > 400) {
            var1.remove();
         }
      }

   }

   public void notifyLightSet(BlockPos var1) {
      int var2 = var1.getX();
      int var3 = var1.getY();
      int var4 = var1.getZ();
      this.markBlocksForUpdate(var2 - 1, var3 - 1, var4 - 1, var2 + 1, var3 + 1, var4 + 1);
   }

   public void makeEntityOutlineShader() {
      if (OpenGlHelper.shadersSupported) {
         if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
         }

         ResourceLocation var1 = new ResourceLocation("shaders/post/entity_outline.json");

         try {
            this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), var1);
            this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
         } catch (IOException var3) {
            logger.warn((String)("Failed to load shader: " + var1), (Throwable)var3);
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
         } catch (JsonSyntaxException var4) {
            logger.warn((String)("Failed to load shader: " + var1), (Throwable)var4);
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
         }
      } else {
         this.entityOutlineShader = null;
         this.entityOutlineFramebuffer = null;
      }

   }

   public RenderChunk getRenderChunk(RenderChunk var1, EnumFacing var2) {
      if (var1 == null) {
         return null;
      } else {
         BlockPos var3 = var1.func_181701_a(var2);
         return this.viewFrustum.getRenderChunk(var3);
      }
   }

   public static class ContainerLocalRenderInformation {
      final Set setFacing;
      final RenderChunk renderChunk;
      private static final String __OBFID = "CL_00002534";
      final EnumFacing facing;
      final int counter;

      ContainerLocalRenderInformation(RenderChunk var1, EnumFacing var2, int var3, Object var4) {
         this(var1, var2, var3);
      }

      public ContainerLocalRenderInformation(RenderChunk var1, EnumFacing var2, int var3) {
         this.setFacing = EnumSet.noneOf(EnumFacing.class);
         this.renderChunk = var1;
         this.facing = var2;
         this.counter = var3;
      }
   }

   static final class RenderGlobal$2 {
      private static final String __OBFID = "CL_00002535";
      static final int[] field_178037_a = new int[VertexFormatElement.EnumUsage.values().length];

      static {
         try {
            field_178037_a[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_178037_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_178037_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
