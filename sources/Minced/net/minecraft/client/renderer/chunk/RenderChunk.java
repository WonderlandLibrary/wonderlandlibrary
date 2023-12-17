// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.world.ChunkCache;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.optifine.CustomBlockLayers;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.EntityPlayerSP;
import javax.annotation.Nullable;
import net.optifine.render.RenderEnv;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.optifine.override.ChunkCacheOF;
import java.util.HashSet;
import java.util.Collection;
import java.util.BitSet;
import net.optifine.shaders.SVertexBuilder;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.optifine.reflect.ReflectorForge;
import net.optifine.BlockPosM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3i;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumFacing;
import net.optifine.reflect.Reflector;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.GLAllocation;
import com.google.common.collect.Sets;
import net.optifine.render.AabbFrame;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import java.nio.FloatBuffer;
import net.minecraft.tileentity.TileEntity;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class RenderChunk
{
    private final World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    public CompiledChunk compiledChunk;
    private final ReentrantLock lockCompileTask;
    private final ReentrantLock lockCompiledChunk;
    private ChunkCompileTaskGenerator compileTask;
    private final Set<TileEntity> setTileEntities;
    private final int index;
    private final FloatBuffer modelviewMatrix;
    private final VertexBuffer[] vertexBuffers;
    public AxisAlignedBB boundingBox;
    private int frameIndex;
    private boolean needsUpdate;
    private final BlockPos.MutableBlockPos position;
    private final BlockPos.MutableBlockPos[] mapEnumFacing;
    private boolean needsImmediateUpdate;
    public static final BlockRenderLayer[] ENUM_WORLD_BLOCK_LAYERS;
    private final BlockRenderLayer[] blockLayersSingle;
    private final boolean isMipmaps;
    private final boolean fixBlockLayer;
    private boolean playerUpdate;
    public int regionX;
    public int regionZ;
    private final RenderChunk[] renderChunksOfset16;
    private boolean renderChunksOffset16Updated;
    private Chunk chunk;
    private RenderChunk[] renderChunkNeighbours;
    private RenderChunk[] renderChunkNeighboursValid;
    private boolean renderChunkNeighboursUpated;
    private RenderGlobal.ContainerLocalRenderInformation renderInfo;
    public AabbFrame boundingBoxParent;
    
    public RenderChunk(final World worldIn, final RenderGlobal renderGlobalIn, final int indexIn) {
        this.compiledChunk = CompiledChunk.DUMMY;
        this.lockCompileTask = new ReentrantLock();
        this.lockCompiledChunk = new ReentrantLock();
        this.setTileEntities = (Set<TileEntity>)Sets.newHashSet();
        this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
        this.frameIndex = -1;
        this.needsUpdate = true;
        this.position = new BlockPos.MutableBlockPos(-1, -1, -1);
        this.mapEnumFacing = new BlockPos.MutableBlockPos[6];
        this.blockLayersSingle = new BlockRenderLayer[1];
        this.isMipmaps = Config.isMipmaps();
        this.fixBlockLayer = !Reflector.BetterFoliageClient.exists();
        this.playerUpdate = false;
        this.renderChunksOfset16 = new RenderChunk[6];
        this.renderChunksOffset16Updated = false;
        this.renderChunkNeighbours = new RenderChunk[EnumFacing.VALUES.length];
        this.renderChunkNeighboursValid = new RenderChunk[EnumFacing.VALUES.length];
        this.renderChunkNeighboursUpated = false;
        this.renderInfo = new RenderGlobal.ContainerLocalRenderInformation(this, null, 0);
        for (int i = 0; i < this.mapEnumFacing.length; ++i) {
            this.mapEnumFacing[i] = new BlockPos.MutableBlockPos();
        }
        this.world = worldIn;
        this.renderGlobal = renderGlobalIn;
        this.index = indexIn;
        if (OpenGlHelper.useVbo()) {
            for (int j = 0; j < BlockRenderLayer.values().length; ++j) {
                this.vertexBuffers[j] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }
    
    public boolean setFrameIndex(final int frameIndexIn) {
        if (this.frameIndex == frameIndexIn) {
            return false;
        }
        this.frameIndex = frameIndexIn;
        return true;
    }
    
    public VertexBuffer getVertexBufferByLayer(final int layer) {
        return this.vertexBuffers[layer];
    }
    
    public void setPosition(final int x, final int y, final int z) {
        if (x != this.position.getX() || y != this.position.getY() || z != this.position.getZ()) {
            this.stopCompileTask();
            this.position.setPos(x, y, z);
            final int i = 8;
            this.regionX = x >> i << i;
            this.regionZ = z >> i << i;
            this.boundingBox = new AxisAlignedBB(x, y, z, x + 16, y + 16, z + 16);
            for (final EnumFacing enumfacing : EnumFacing.VALUES) {
                this.mapEnumFacing[enumfacing.ordinal()].setPos(this.position).move(enumfacing, 16);
            }
            this.renderChunksOffset16Updated = false;
            this.renderChunkNeighboursUpated = false;
            for (int j = 0; j < this.renderChunkNeighbours.length; ++j) {
                final RenderChunk renderchunk = this.renderChunkNeighbours[j];
                if (renderchunk != null) {
                    renderchunk.renderChunkNeighboursUpated = false;
                }
            }
            this.chunk = null;
            this.boundingBoxParent = null;
            this.initModelviewMatrix();
        }
    }
    
    public void resortTransparency(final float x, final float y, final float z, final ChunkCompileTaskGenerator generator) {
        final CompiledChunk compiledchunk = generator.getCompiledChunk();
        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(BlockRenderLayer.TRANSLUCENT)) {
            final BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT);
            this.preRenderBlocks(bufferbuilder, this.position);
            bufferbuilder.setVertexState(compiledchunk.getState());
            this.postRenderBlocks(BlockRenderLayer.TRANSLUCENT, x, y, z, bufferbuilder, compiledchunk);
        }
    }
    
    public void rebuildChunk(final float x, final float y, final float z, final ChunkCompileTaskGenerator generator) {
        final CompiledChunk compiledchunk = new CompiledChunk();
        final int i = 1;
        final BlockPos blockpos = new BlockPos(this.position);
        final BlockPos blockpos2 = blockpos.add(15, 15, 15);
        generator.getLock().lock();
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }
            generator.setCompiledChunk(compiledchunk);
        }
        finally {
            generator.getLock().unlock();
        }
        final VisGraph lvt_9_1_ = new VisGraph();
        final HashSet lvt_10_1_ = Sets.newHashSet();
        if (!this.isChunkRegionEmpty(blockpos)) {
            ++RenderChunk.renderChunksUpdated;
            final ChunkCacheOF chunkcacheof = this.makeChunkCacheOF(blockpos);
            chunkcacheof.renderStart();
            final boolean[] aboolean = new boolean[RenderChunk.ENUM_WORLD_BLOCK_LAYERS.length];
            final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final boolean flag = Reflector.ForgeBlock_canRenderInLayer.exists();
            final boolean flag2 = Reflector.ForgeHooksClient_setRenderLayer.exists();
            for (final Object blockposm0 : BlockPosM.getAllInBoxMutable(blockpos, blockpos2)) {
                final BlockPosM blockposm2 = (BlockPosM)blockposm0;
                final IBlockState iblockstate = chunkcacheof.getBlockState(blockposm2);
                final Block block = iblockstate.getBlock();
                if (iblockstate.isOpaqueCube()) {
                    lvt_9_1_.setOpaqueCube(blockposm2);
                }
                if (ReflectorForge.blockHasTileEntity(iblockstate)) {
                    final TileEntity tileentity = chunkcacheof.getTileEntity(blockposm2, Chunk.EnumCreateEntityType.CHECK);
                    if (tileentity != null) {
                        final TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getRenderer(tileentity);
                        if (tileentityspecialrenderer != null) {
                            if (tileentityspecialrenderer.isGlobalRenderer(tileentity)) {
                                lvt_10_1_.add(tileentity);
                            }
                            else {
                                compiledchunk.addTileEntity(tileentity);
                            }
                        }
                    }
                }
                BlockRenderLayer[] ablockrenderlayer;
                if (flag) {
                    ablockrenderlayer = RenderChunk.ENUM_WORLD_BLOCK_LAYERS;
                }
                else {
                    ablockrenderlayer = this.blockLayersSingle;
                    ablockrenderlayer[0] = block.getRenderLayer();
                }
                for (int j = 0; j < ablockrenderlayer.length; ++j) {
                    BlockRenderLayer blockrenderlayer = ablockrenderlayer[j];
                    if (flag) {
                        final boolean flag3 = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, iblockstate, blockrenderlayer);
                        if (!flag3) {
                            continue;
                        }
                    }
                    if (flag2) {
                        Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, blockrenderlayer);
                    }
                    blockrenderlayer = this.fixBlockLayer(iblockstate, blockrenderlayer);
                    final int k = blockrenderlayer.ordinal();
                    if (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE) {
                        final BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(k);
                        bufferbuilder.setBlockLayer(blockrenderlayer);
                        final RenderEnv renderenv = bufferbuilder.getRenderEnv(iblockstate, blockposm2);
                        renderenv.setRegionRenderCacheBuilder(generator.getRegionRenderCacheBuilder());
                        if (!compiledchunk.isLayerStarted(blockrenderlayer)) {
                            compiledchunk.setLayerStarted(blockrenderlayer);
                            this.preRenderBlocks(bufferbuilder, blockpos);
                        }
                        final boolean[] array = aboolean;
                        final int n = k;
                        array[n] |= blockrendererdispatcher.renderBlock(iblockstate, blockposm2, chunkcacheof, bufferbuilder);
                        if (renderenv.isOverlaysRendered()) {
                            this.postRenderOverlays(generator.getRegionRenderCacheBuilder(), compiledchunk, aboolean);
                            renderenv.setOverlaysRendered(false);
                        }
                    }
                }
                if (flag2) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, (Object[])null);
                }
            }
            for (final BlockRenderLayer blockrenderlayer2 : RenderChunk.ENUM_WORLD_BLOCK_LAYERS) {
                if (aboolean[blockrenderlayer2.ordinal()]) {
                    compiledchunk.setLayerUsed(blockrenderlayer2);
                }
                if (compiledchunk.isLayerStarted(blockrenderlayer2)) {
                    if (Config.isShaders()) {
                        SVertexBuilder.calcNormalChunkLayer(generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer2));
                    }
                    final BufferBuilder bufferbuilder2 = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer2);
                    this.postRenderBlocks(blockrenderlayer2, x, y, z, bufferbuilder2, compiledchunk);
                    if (bufferbuilder2.animatedSprites != null) {
                        compiledchunk.setAnimatedSprites(blockrenderlayer2, (BitSet)bufferbuilder2.animatedSprites.clone());
                    }
                }
                else {
                    compiledchunk.setAnimatedSprites(blockrenderlayer2, null);
                }
            }
            chunkcacheof.renderFinish();
        }
        compiledchunk.setVisibility(lvt_9_1_.computeVisibility());
        this.lockCompileTask.lock();
        try {
            final Set<TileEntity> set = (Set<TileEntity>)Sets.newHashSet((Iterable)lvt_10_1_);
            final Set<TileEntity> set2 = (Set<TileEntity>)Sets.newHashSet((Iterable)this.setTileEntities);
            set.removeAll(this.setTileEntities);
            set2.removeAll(lvt_10_1_);
            this.setTileEntities.clear();
            this.setTileEntities.addAll(lvt_10_1_);
            this.renderGlobal.updateTileEntities(set2, set);
        }
        finally {
            this.lockCompileTask.unlock();
        }
    }
    
    protected void finishCompileTask() {
        this.lockCompileTask.lock();
        try {
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
        }
        finally {
            this.lockCompileTask.unlock();
        }
    }
    
    public ReentrantLock getLockCompileTask() {
        return this.lockCompileTask;
    }
    
    public ChunkCompileTaskGenerator makeCompileTaskChunk() {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;
        try {
            this.finishCompileTask();
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK, this.getDistanceSq());
            this.rebuildWorldView();
            chunkcompiletaskgenerator = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        return chunkcompiletaskgenerator;
    }
    
    private void rebuildWorldView() {
        final int i = 1;
    }
    
    @Nullable
    public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator4;
        try {
            if (this.compileTask != null && this.compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING) {
                final ChunkCompileTaskGenerator chunkcompiletaskgenerator2 = null;
                return chunkcompiletaskgenerator2;
            }
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
            (this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY, this.getDistanceSq())).setCompiledChunk(this.compiledChunk);
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator3 = chunkcompiletaskgenerator4 = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        return chunkcompiletaskgenerator4;
    }
    
    protected double getDistanceSq() {
        Minecraft.getMinecraft();
        final EntityPlayerSP entityplayersp = Minecraft.player;
        final double d0 = this.boundingBox.minX + 8.0 - entityplayersp.posX;
        final double d2 = this.boundingBox.minY + 8.0 - entityplayersp.posY;
        final double d3 = this.boundingBox.minZ + 8.0 - entityplayersp.posZ;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    private void preRenderBlocks(final BufferBuilder bufferBuilderIn, final BlockPos pos) {
        bufferBuilderIn.begin(7, DefaultVertexFormats.BLOCK);
        if (Config.isRenderRegions()) {
            final int i = 8;
            int j = pos.getX() >> i << i;
            final int k = pos.getY() >> i << i;
            int l = pos.getZ() >> i << i;
            j = this.regionX;
            l = this.regionZ;
            bufferBuilderIn.setTranslation(-j, -k, -l);
        }
        else {
            bufferBuilderIn.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
        }
    }
    
    private void postRenderBlocks(final BlockRenderLayer layer, final float x, final float y, final float z, final BufferBuilder bufferBuilderIn, final CompiledChunk compiledChunkIn) {
        if (layer == BlockRenderLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer)) {
            bufferBuilderIn.sortVertexData(x, y, z);
            compiledChunkIn.setState(bufferBuilderIn.getVertexState());
        }
        bufferBuilderIn.finishDrawing();
    }
    
    private void initModelviewMatrix() {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        final float f = 1.000001f;
        GlStateManager.translate(-8.0f, -8.0f, -8.0f);
        GlStateManager.scale(1.000001f, 1.000001f, 1.000001f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.getFloat(2982, this.modelviewMatrix);
        GlStateManager.popMatrix();
    }
    
    public void multModelviewMatrix() {
        GlStateManager.multMatrix(this.modelviewMatrix);
    }
    
    public CompiledChunk getCompiledChunk() {
        return this.compiledChunk;
    }
    
    public void setCompiledChunk(final CompiledChunk compiledChunkIn) {
        this.lockCompiledChunk.lock();
        try {
            this.compiledChunk = compiledChunkIn;
        }
        finally {
            this.lockCompiledChunk.unlock();
        }
    }
    
    public void stopCompileTask() {
        this.finishCompileTask();
        this.compiledChunk = CompiledChunk.DUMMY;
    }
    
    public void deleteGlResources() {
        this.stopCompileTask();
        for (int i = 0; i < BlockRenderLayer.values().length; ++i) {
            if (this.vertexBuffers[i] != null) {
                this.vertexBuffers[i].deleteGlBuffers();
            }
        }
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public void setNeedsUpdate(boolean immediate) {
        if (this.needsUpdate) {
            immediate |= this.needsImmediateUpdate;
        }
        this.needsUpdate = true;
        this.needsImmediateUpdate = immediate;
        if (this.isWorldPlayerUpdate()) {
            this.playerUpdate = true;
        }
    }
    
    public void clearNeedsUpdate() {
        this.needsUpdate = false;
        this.needsImmediateUpdate = false;
        this.playerUpdate = false;
    }
    
    public boolean needsUpdate() {
        return this.needsUpdate;
    }
    
    public boolean needsImmediateUpdate() {
        return this.needsUpdate && this.needsImmediateUpdate;
    }
    
    public BlockPos getBlockPosOffset16(final EnumFacing facing) {
        return this.mapEnumFacing[facing.ordinal()];
    }
    
    public World getWorld() {
        return this.world;
    }
    
    private boolean isWorldPlayerUpdate() {
        if (this.world instanceof WorldClient) {
            final WorldClient worldclient = (WorldClient)this.world;
            return worldclient.isPlayerUpdate();
        }
        return false;
    }
    
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }
    
    private BlockRenderLayer fixBlockLayer(final IBlockState p_fixBlockLayer_1_, final BlockRenderLayer p_fixBlockLayer_2_) {
        if (CustomBlockLayers.isActive()) {
            final BlockRenderLayer blockrenderlayer = CustomBlockLayers.getRenderLayer(p_fixBlockLayer_1_);
            if (blockrenderlayer != null) {
                return blockrenderlayer;
            }
        }
        if (!this.fixBlockLayer) {
            return p_fixBlockLayer_2_;
        }
        if (this.isMipmaps) {
            if (p_fixBlockLayer_2_ == BlockRenderLayer.CUTOUT) {
                final Block block = p_fixBlockLayer_1_.getBlock();
                if (block instanceof BlockRedstoneWire) {
                    return p_fixBlockLayer_2_;
                }
                if (block instanceof BlockCactus) {
                    return p_fixBlockLayer_2_;
                }
                return BlockRenderLayer.CUTOUT_MIPPED;
            }
        }
        else if (p_fixBlockLayer_2_ == BlockRenderLayer.CUTOUT_MIPPED) {
            return BlockRenderLayer.CUTOUT;
        }
        return p_fixBlockLayer_2_;
    }
    
    private void postRenderOverlays(final RegionRenderCacheBuilder p_postRenderOverlays_1_, final CompiledChunk p_postRenderOverlays_2_, final boolean[] p_postRenderOverlays_3_) {
        this.postRenderOverlay(BlockRenderLayer.CUTOUT, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
        this.postRenderOverlay(BlockRenderLayer.CUTOUT_MIPPED, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
        this.postRenderOverlay(BlockRenderLayer.TRANSLUCENT, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
    }
    
    private void postRenderOverlay(final BlockRenderLayer p_postRenderOverlay_1_, final RegionRenderCacheBuilder p_postRenderOverlay_2_, final CompiledChunk p_postRenderOverlay_3_, final boolean[] p_postRenderOverlay_4_) {
        final BufferBuilder bufferbuilder = p_postRenderOverlay_2_.getWorldRendererByLayer(p_postRenderOverlay_1_);
        if (bufferbuilder.isDrawing()) {
            p_postRenderOverlay_3_.setLayerStarted(p_postRenderOverlay_1_);
            p_postRenderOverlay_4_[p_postRenderOverlay_1_.ordinal()] = true;
        }
    }
    
    private ChunkCacheOF makeChunkCacheOF(final BlockPos p_makeChunkCacheOF_1_) {
        final BlockPos blockpos = p_makeChunkCacheOF_1_.add(-1, -1, -1);
        final BlockPos blockpos2 = p_makeChunkCacheOF_1_.add(16, 16, 16);
        final ChunkCache chunkcache = this.createRegionRenderCache(this.world, blockpos, blockpos2, 1);
        if (Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
            Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, this.world, p_makeChunkCacheOF_1_, chunkcache);
        }
        final ChunkCacheOF chunkcacheof = new ChunkCacheOF(chunkcache, blockpos, blockpos2, 1);
        return chunkcacheof;
    }
    
    public RenderChunk getRenderChunkOffset16(final ViewFrustum p_getRenderChunkOffset16_1_, final EnumFacing p_getRenderChunkOffset16_2_) {
        if (!this.renderChunksOffset16Updated) {
            for (int i = 0; i < EnumFacing.VALUES.length; ++i) {
                final EnumFacing enumfacing = EnumFacing.VALUES[i];
                final BlockPos blockpos = this.getBlockPosOffset16(enumfacing);
                this.renderChunksOfset16[i] = p_getRenderChunkOffset16_1_.getRenderChunk(blockpos);
            }
            this.renderChunksOffset16Updated = true;
        }
        return this.renderChunksOfset16[p_getRenderChunkOffset16_2_.ordinal()];
    }
    
    public Chunk getChunk() {
        return this.getChunk(this.position);
    }
    
    private Chunk getChunk(final BlockPos p_getChunk_1_) {
        Chunk chunk = this.chunk;
        if (chunk != null && chunk.isLoaded()) {
            return chunk;
        }
        chunk = this.world.getChunk(p_getChunk_1_);
        return this.chunk = chunk;
    }
    
    public boolean isChunkRegionEmpty() {
        return this.isChunkRegionEmpty(this.position);
    }
    
    private boolean isChunkRegionEmpty(final BlockPos p_isChunkRegionEmpty_1_) {
        final int i = p_isChunkRegionEmpty_1_.getY();
        final int j = i + 15;
        return this.getChunk(p_isChunkRegionEmpty_1_).isEmptyBetween(i, j);
    }
    
    public void setRenderChunkNeighbour(final EnumFacing p_setRenderChunkNeighbour_1_, final RenderChunk p_setRenderChunkNeighbour_2_) {
        this.renderChunkNeighbours[p_setRenderChunkNeighbour_1_.ordinal()] = p_setRenderChunkNeighbour_2_;
        this.renderChunkNeighboursValid[p_setRenderChunkNeighbour_1_.ordinal()] = p_setRenderChunkNeighbour_2_;
    }
    
    public RenderChunk getRenderChunkNeighbour(final EnumFacing p_getRenderChunkNeighbour_1_) {
        if (!this.renderChunkNeighboursUpated) {
            this.updateRenderChunkNeighboursValid();
        }
        return this.renderChunkNeighboursValid[p_getRenderChunkNeighbour_1_.ordinal()];
    }
    
    public RenderGlobal.ContainerLocalRenderInformation getRenderInfo() {
        return this.renderInfo;
    }
    
    private void updateRenderChunkNeighboursValid() {
        final int i = this.getPosition().getX();
        final int j = this.getPosition().getZ();
        final int k = EnumFacing.NORTH.ordinal();
        final int l = EnumFacing.SOUTH.ordinal();
        final int i2 = EnumFacing.WEST.ordinal();
        final int j2 = EnumFacing.EAST.ordinal();
        this.renderChunkNeighboursValid[k] = ((this.renderChunkNeighbours[k].getPosition().getZ() == j - 16) ? this.renderChunkNeighbours[k] : null);
        this.renderChunkNeighboursValid[l] = ((this.renderChunkNeighbours[l].getPosition().getZ() == j + 16) ? this.renderChunkNeighbours[l] : null);
        this.renderChunkNeighboursValid[i2] = ((this.renderChunkNeighbours[i2].getPosition().getX() == i - 16) ? this.renderChunkNeighbours[i2] : null);
        this.renderChunkNeighboursValid[j2] = ((this.renderChunkNeighbours[j2].getPosition().getX() == i + 16) ? this.renderChunkNeighbours[j2] : null);
        this.renderChunkNeighboursUpated = true;
    }
    
    public boolean isBoundingBoxInFrustum(final ICamera p_isBoundingBoxInFrustum_1_, final int p_isBoundingBoxInFrustum_2_) {
        return this.getBoundingBoxParent().isBoundingBoxInFrustumFully(p_isBoundingBoxInFrustum_1_, p_isBoundingBoxInFrustum_2_) || p_isBoundingBoxInFrustum_1_.isBoundingBoxInFrustum(this.boundingBox);
    }
    
    public AabbFrame getBoundingBoxParent() {
        if (this.boundingBoxParent == null) {
            final BlockPos blockpos = this.getPosition();
            final int i = blockpos.getX();
            final int j = blockpos.getY();
            final int k = blockpos.getZ();
            final int l = 5;
            final int i2 = i >> l << l;
            final int j2 = j >> l << l;
            final int k2 = k >> l << l;
            if (i2 != i || j2 != j || k2 != k) {
                final AabbFrame aabbframe = this.renderGlobal.getRenderChunk(new BlockPos(i2, j2, k2)).getBoundingBoxParent();
                if (aabbframe != null && aabbframe.minX == i2 && aabbframe.minY == j2 && aabbframe.minZ == k2) {
                    this.boundingBoxParent = aabbframe;
                }
            }
            if (this.boundingBoxParent == null) {
                final int l2 = 1 << l;
                this.boundingBoxParent = new AabbFrame(i2, j2, k2, i2 + l2, j2 + l2, k2 + l2);
            }
        }
        return this.boundingBoxParent;
    }
    
    @Override
    public String toString() {
        return "pos: " + this.getPosition() + ", frameIndex: " + this.frameIndex;
    }
    
    protected ChunkCache createRegionRenderCache(final World p_createRegionRenderCache_1_, final BlockPos p_createRegionRenderCache_2_, final BlockPos p_createRegionRenderCache_3_, final int p_createRegionRenderCache_4_) {
        return new ChunkCache(p_createRegionRenderCache_1_, p_createRegionRenderCache_2_, p_createRegionRenderCache_3_, p_createRegionRenderCache_4_);
    }
    
    static {
        ENUM_WORLD_BLOCK_LAYERS = BlockRenderLayer.values();
    }
}
