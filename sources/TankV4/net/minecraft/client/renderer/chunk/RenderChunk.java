package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Sets;
import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import optifine.BlockPosM;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorForge;
import shadersmod.client.SVertexBuilder;

public class RenderChunk {
   public static int renderChunksUpdated;
   private ChunkCompileTaskGenerator compileTask;
   private EnumWorldBlockLayer[] blockLayersSingle;
   private World world;
   private boolean needsUpdate;
   private EnumMap field_181702_p;
   public AxisAlignedBB boundingBox;
   public CompiledChunk compiledChunk;
   private boolean isMipmaps;
   private BlockPos[] positionOffsets16;
   private static EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS = EnumWorldBlockLayer.values();
   private final RenderGlobal renderGlobal;
   private BlockPos position;
   private boolean playerUpdate;
   private final Set field_181056_j;
   private boolean fixBlockLayer;
   private final int index;
   private int frameIndex;
   private final ReentrantLock lockCompiledChunk;
   private final VertexBuffer[] vertexBuffers;
   private final ReentrantLock lockCompileTask;
   private final FloatBuffer modelviewMatrix;
   private static final String __OBFID = "CL_00002452";

   protected RegionRenderCache createRegionRenderCache(World var1, BlockPos var2, BlockPos var3, int var4) {
      return new RegionRenderCache(var1, var2, var3, var4);
   }

   public boolean isPlayerUpdate() {
      return this.playerUpdate;
   }

   public CompiledChunk getCompiledChunk() {
      return this.compiledChunk;
   }

   public void resortTransparency(float var1, float var2, float var3, ChunkCompileTaskGenerator var4) {
      CompiledChunk var5 = var4.getCompiledChunk();
      if (var5.getState() != null && !var5.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)) {
         WorldRenderer var6 = var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT);
         this.preRenderBlocks(var6, this.position);
         var6.setVertexState(var5.getState());
         this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, var1, var2, var3, var6, var5);
      }

   }

   public boolean isNeedsUpdate() {
      return this.needsUpdate;
   }

   public void multModelviewMatrix() {
      GlStateManager.multMatrix(this.modelviewMatrix);
   }

   protected void finishCompileTask() {
      this.lockCompileTask.lock();
      if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
         this.compileTask.finish();
         this.compileTask = null;
      }

      this.lockCompileTask.unlock();
   }

   public BlockPos getPositionOffset16(EnumFacing var1) {
      int var2 = var1.getIndex();
      BlockPos var3 = this.positionOffsets16[var2];
      if (var3 == null) {
         var3 = this.getPosition().offset(var1, 16);
         this.positionOffsets16[var2] = var3;
      }

      return var3;
   }

   public ChunkCompileTaskGenerator makeCompileTaskChunk() {
      this.lockCompileTask.lock();
      this.finishCompileTask();
      this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
      ChunkCompileTaskGenerator var1 = this.compileTask;
      this.lockCompileTask.unlock();
      return var1;
   }

   public VertexBuffer getVertexBufferByLayer(int var1) {
      return this.vertexBuffers[var1];
   }

   public ReentrantLock getLockCompileTask() {
      return this.lockCompileTask;
   }

   public void stopCompileTask() {
      this.finishCompileTask();
      this.compiledChunk = CompiledChunk.DUMMY;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   private void initModelviewMatrix() {
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      float var1 = 1.000001F;
      GlStateManager.translate(-8.0F, -8.0F, -8.0F);
      GlStateManager.scale(var1, var1, var1);
      GlStateManager.translate(8.0F, 8.0F, 8.0F);
      GlStateManager.getFloat(2982, this.modelviewMatrix);
      GlStateManager.popMatrix();
   }

   public void deleteGlResources() {
      this.stopCompileTask();
      this.world = null;

      for(int var1 = 0; var1 < EnumWorldBlockLayer.values().length; ++var1) {
         if (this.vertexBuffers[var1] != null) {
            this.vertexBuffers[var1].deleteGlBuffers();
         }
      }

   }

   public RenderChunk(World var1, RenderGlobal var2, BlockPos var3, int var4) {
      this.compiledChunk = CompiledChunk.DUMMY;
      this.lockCompileTask = new ReentrantLock();
      this.lockCompiledChunk = new ReentrantLock();
      this.compileTask = null;
      this.field_181056_j = Sets.newHashSet();
      this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
      this.vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
      this.frameIndex = -1;
      this.needsUpdate = true;
      this.positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
      this.blockLayersSingle = new EnumWorldBlockLayer[1];
      this.isMipmaps = Config.isMipmaps();
      this.fixBlockLayer = !Reflector.BetterFoliageClient.exists();
      this.playerUpdate = false;
      this.world = var1;
      this.renderGlobal = var2;
      this.index = var4;
      if (!var3.equals(this.getPosition())) {
         this.setPosition(var3);
      }

      if (OpenGlHelper.useVbo()) {
         for(int var5 = 0; var5 < EnumWorldBlockLayer.values().length; ++var5) {
            this.vertexBuffers[var5] = new VertexBuffer(DefaultVertexFormats.BLOCK);
         }
      }

   }

   private void postRenderBlocks(EnumWorldBlockLayer var1, float var2, float var3, float var4, WorldRenderer var5, CompiledChunk var6) {
      if (var1 == EnumWorldBlockLayer.TRANSLUCENT && !var6.isLayerEmpty(var1)) {
         var5.func_181674_a(var2, var3, var4);
         var6.setState(var5.func_181672_a());
      }

      var5.finishDrawing();
   }

   public void setCompiledChunk(CompiledChunk var1) {
      this.lockCompiledChunk.lock();
      this.compiledChunk = var1;
      this.lockCompiledChunk.unlock();
   }

   public void setNeedsUpdate(boolean var1) {
      this.needsUpdate = var1;
      if (this.needsUpdate) {
         if (this != false) {
            this.playerUpdate = true;
         }
      } else {
         this.playerUpdate = false;
      }

   }

   public void rebuildChunk(float var1, float var2, float var3, ChunkCompileTaskGenerator var4) {
      CompiledChunk var5 = new CompiledChunk();
      boolean var6 = true;
      BlockPos var7 = this.position;
      BlockPos var8 = var7.add(15, 15, 15);
      var4.getLock().lock();
      if (var4.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
         var4.getLock().unlock();
      } else if (this.world == null) {
         var4.getLock().unlock();
      } else {
         RegionRenderCache var9 = this.createRegionRenderCache(this.world, var7.add(-1, -1, -1), var8.add(1, 1, 1), 1);
         if (Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
            Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, this.world, this.position, var9);
         }

         var4.setCompiledChunk(var5);
         var4.getLock().unlock();
         VisGraph var10 = new VisGraph();
         HashSet var11 = Sets.newHashSet();
         if (!var9.extendedLevelsInChunkCache()) {
            ++renderChunksUpdated;
            boolean[] var12 = new boolean[ENUM_WORLD_BLOCK_LAYERS.length];
            BlockRendererDispatcher var13 = Minecraft.getMinecraft().getBlockRendererDispatcher();
            Iterator var14 = BlockPosM.getAllInBoxMutable(var7, var8).iterator();
            boolean var15 = Reflector.ForgeBlock_hasTileEntity.exists();
            boolean var16 = Reflector.ForgeBlock_canRenderInLayer.exists();
            boolean var17 = Reflector.ForgeHooksClient_setRenderLayer.exists();

            EnumWorldBlockLayer[] var31;
            while(var14.hasNext()) {
               BlockPosM var18 = (BlockPosM)var14.next();
               IBlockState var19 = var9.getBlockState(var18);
               Block var20 = var19.getBlock();
               if (var20.isOpaqueCube()) {
                  var10.func_178606_a(var18);
               }

               if (ReflectorForge.blockHasTileEntity(var19)) {
                  TileEntity var21 = var9.getTileEntity(new BlockPos(var18));
                  TileEntitySpecialRenderer var22 = TileEntityRendererDispatcher.instance.getSpecialRenderer(var21);
                  if (var21 != null && var22 != null) {
                     var5.addTileEntity(var21);
                     if (var22.func_181055_a()) {
                        var11.add(var21);
                     }
                  }
               }

               if (var16) {
                  var31 = ENUM_WORLD_BLOCK_LAYERS;
               } else {
                  var31 = this.blockLayersSingle;
                  var31[0] = var20.getBlockLayer();
               }

               for(int var32 = 0; var32 < var31.length; ++var32) {
                  EnumWorldBlockLayer var23 = var31[var32];
                  if (var16) {
                     boolean var24 = Reflector.callBoolean(var20, Reflector.ForgeBlock_canRenderInLayer, var23);
                     if (!var24) {
                        continue;
                     }
                  }

                  if (var17) {
                     Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, var23);
                  }

                  if (this.fixBlockLayer) {
                     var23 = this.fixBlockLayer(var20, var23);
                  }

                  int var33 = var23.ordinal();
                  if (var20.getRenderType() != -1) {
                     WorldRenderer var25 = var4.getRegionRenderCacheBuilder().getWorldRendererByLayerId(var33);
                     var25.setBlockLayer(var23);
                     if (!var5.isLayerStarted(var23)) {
                        var5.setLayerStarted(var23);
                        this.preRenderBlocks(var25, var7);
                     }

                     var12[var33] |= var13.renderBlock(var19, var18, var9, var25);
                  }
               }
            }

            int var30 = (var31 = ENUM_WORLD_BLOCK_LAYERS).length;

            for(int var29 = 0; var29 < var30; ++var29) {
               EnumWorldBlockLayer var28 = var31[var29];
               if (var12[var28.ordinal()]) {
                  var5.setLayerUsed(var28);
               }

               if (var5.isLayerStarted(var28)) {
                  if (Config.isShaders()) {
                     SVertexBuilder.calcNormalChunkLayer(var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(var28));
                  }

                  this.postRenderBlocks(var28, var1, var2, var3, var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(var28), var5);
               }
            }
         }

         var5.setVisibility(var10.computeVisibility());
         this.lockCompileTask.lock();
         HashSet var26 = Sets.newHashSet((Iterable)var11);
         HashSet var27 = Sets.newHashSet((Iterable)this.field_181056_j);
         var26.removeAll(this.field_181056_j);
         var27.removeAll(var11);
         this.field_181056_j.clear();
         this.field_181056_j.addAll(var11);
         this.renderGlobal.func_181023_a(var27, var26);
         this.lockCompileTask.unlock();
      }
   }

   public BlockPos func_181701_a(EnumFacing var1) {
      return this.getPositionOffset16(var1);
   }

   private void preRenderBlocks(WorldRenderer var1, BlockPos var2) {
      var1.begin(7, DefaultVertexFormats.BLOCK);
      var1.setTranslation((double)(-var2.getX()), (double)(-var2.getY()), (double)(-var2.getZ()));
   }

   public boolean setFrameIndex(int var1) {
      if (this.frameIndex == var1) {
         return false;
      } else {
         this.frameIndex = var1;
         return true;
      }
   }

   public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
      this.lockCompileTask.lock();
      ChunkCompileTaskGenerator var2;
      if (this.compileTask != null && this.compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING) {
         var2 = null;
         this.lockCompileTask.unlock();
         return var2;
      } else {
         if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
            this.compileTask.finish();
            this.compileTask = null;
         }

         this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
         this.compileTask.setCompiledChunk(this.compiledChunk);
         var2 = this.compileTask;
         this.lockCompileTask.unlock();
         return var2;
      }
   }

   private EnumWorldBlockLayer fixBlockLayer(Block var1, EnumWorldBlockLayer var2) {
      if (this.isMipmaps) {
         if (var2 == EnumWorldBlockLayer.CUTOUT) {
            if (var1 instanceof BlockRedstoneWire) {
               return var2;
            }

            if (var1 instanceof BlockCactus) {
               return var2;
            }

            return EnumWorldBlockLayer.CUTOUT_MIPPED;
         }
      } else if (var2 == EnumWorldBlockLayer.CUTOUT_MIPPED) {
         return EnumWorldBlockLayer.CUTOUT;
      }

      return var2;
   }

   public void setPosition(BlockPos var1) {
      this.stopCompileTask();
      this.position = var1;
      this.boundingBox = new AxisAlignedBB(var1, var1.add(16, 16, 16));
      this.initModelviewMatrix();

      for(int var2 = 0; var2 < this.positionOffsets16.length; ++var2) {
         this.positionOffsets16[var2] = null;
      }

   }
}
