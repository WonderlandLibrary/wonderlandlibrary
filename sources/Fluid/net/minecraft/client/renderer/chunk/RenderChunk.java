// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.world.ChunkCache;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.optifine.CustomBlockLayers;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
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
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.optifine.reflect.ReflectorForge;
import net.optifine.BlockPosM;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3i;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.OpenGlHelper;
import net.optifine.reflect.Reflector;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.GLAllocation;
import com.google.common.collect.Sets;
import net.optifine.render.AabbFrame;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.EnumFacing;
import java.util.EnumMap;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import java.nio.FloatBuffer;
import net.minecraft.tileentity.TileEntity;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class RenderChunk
{
    private final World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    private BlockPos position;
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
    private EnumMap<EnumFacing, BlockPos> mapEnumFacing;
    private BlockPos[] positionOffsets16;
    public static final EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS;
    private final EnumWorldBlockLayer[] blockLayersSingle;
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
    
    static {
        ENUM_WORLD_BLOCK_LAYERS = EnumWorldBlockLayer.values();
    }
    
    public RenderChunk(final World worldIn, final RenderGlobal renderGlobalIn, final BlockPos blockPosIn, final int indexIn) {
        this.compiledChunk = CompiledChunk.DUMMY;
        this.lockCompileTask = new ReentrantLock();
        this.lockCompiledChunk = new ReentrantLock();
        this.compileTask = null;
        this.setTileEntities = Sets.newHashSet();
        this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
        this.frameIndex = -1;
        this.needsUpdate = true;
        this.mapEnumFacing = null;
        this.positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
        this.blockLayersSingle = new EnumWorldBlockLayer[1];
        this.isMipmaps = Config.isMipmaps();
        this.fixBlockLayer = !Reflector.BetterFoliageClient.exists();
        this.playerUpdate = false;
        this.renderChunksOfset16 = new RenderChunk[6];
        this.renderChunksOffset16Updated = false;
        this.renderChunkNeighbours = new RenderChunk[EnumFacing.VALUES.length];
        this.renderChunkNeighboursValid = new RenderChunk[EnumFacing.VALUES.length];
        this.renderChunkNeighboursUpated = false;
        this.renderInfo = new RenderGlobal.ContainerLocalRenderInformation(this, null, 0);
        this.world = worldIn;
        this.renderGlobal = renderGlobalIn;
        this.index = indexIn;
        if (!blockPosIn.equals(this.getPosition())) {
            this.setPosition(blockPosIn);
        }
        if (OpenGlHelper.useVbo()) {
            for (int i = 0; i < EnumWorldBlockLayer.values().length; ++i) {
                this.vertexBuffers[i] = new VertexBuffer(DefaultVertexFormats.BLOCK);
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
    
    public void setPosition(final BlockPos pos) {
        this.stopCompileTask();
        this.position = pos;
        final int i = 8;
        this.regionX = pos.getX() >> i << i;
        this.regionZ = pos.getZ() >> i << i;
        this.boundingBox = new AxisAlignedBB(pos, pos.add(16, 16, 16));
        this.initModelviewMatrix();
        for (int j = 0; j < this.positionOffsets16.length; ++j) {
            this.positionOffsets16[j] = null;
        }
        this.renderChunksOffset16Updated = false;
        this.renderChunkNeighboursUpated = false;
        for (int k = 0; k < this.renderChunkNeighbours.length; ++k) {
            final RenderChunk renderchunk = this.renderChunkNeighbours[k];
            if (renderchunk != null) {
                renderchunk.renderChunkNeighboursUpated = false;
            }
        }
        this.chunk = null;
        this.boundingBoxParent = null;
    }
    
    public void resortTransparency(final float x, final float y, final float z, final ChunkCompileTaskGenerator generator) {
        final CompiledChunk compiledchunk = generator.getCompiledChunk();
        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)) {
            final WorldRenderer worldrenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT);
            this.preRenderBlocks(worldrenderer, this.position);
            worldrenderer.setVertexState(compiledchunk.getState());
            this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, x, y, z, worldrenderer, compiledchunk);
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
        generator.getLock().unlock();
        final VisGraph lvt_10_1_ = new VisGraph();
        final HashSet lvt_11_1_ = Sets.newHashSet();
        if (!this.isChunkRegionEmpty(blockpos)) {
            ++RenderChunk.renderChunksUpdated;
            final ChunkCacheOF chunkcacheof = this.makeChunkCacheOF(blockpos);
            chunkcacheof.renderStart();
            final boolean[] aboolean = new boolean[RenderChunk.ENUM_WORLD_BLOCK_LAYERS.length];
            final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final boolean flag = Reflector.ForgeBlock_canRenderInLayer.exists();
            final boolean flag2 = Reflector.ForgeHooksClient_setRenderLayer.exists();
            for (final Object o : BlockPosM.getAllInBoxMutable(blockpos, blockpos2)) {
                final BlockPosM blockposm = (BlockPosM)o;
                final IBlockState iblockstate = chunkcacheof.getBlockState(blockposm);
                final Block block = iblockstate.getBlock();
                if (block.isOpaqueCube()) {
                    lvt_10_1_.func_178606_a(blockposm);
                }
                if (ReflectorForge.blockHasTileEntity(iblockstate)) {
                    final TileEntity tileentity = chunkcacheof.getTileEntity(new BlockPos(blockposm));
                    final TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tileentity);
                    if (tileentity != null && tileentityspecialrenderer != null) {
                        compiledchunk.addTileEntity(tileentity);
                        if (tileentityspecialrenderer.forceTileEntityRender()) {
                            lvt_11_1_.add(tileentity);
                        }
                    }
                }
                EnumWorldBlockLayer[] aenumworldblocklayer;
                if (flag) {
                    aenumworldblocklayer = RenderChunk.ENUM_WORLD_BLOCK_LAYERS;
                }
                else {
                    aenumworldblocklayer = this.blockLayersSingle;
                    aenumworldblocklayer[0] = block.getBlockLayer();
                }
                for (int j = 0; j < aenumworldblocklayer.length; ++j) {
                    EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[j];
                    if (flag) {
                        final boolean flag3 = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, enumworldblocklayer);
                        if (!flag3) {
                            continue;
                        }
                    }
                    if (flag2) {
                        Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, enumworldblocklayer);
                    }
                    enumworldblocklayer = this.fixBlockLayer(iblockstate, enumworldblocklayer);
                    final int k = enumworldblocklayer.ordinal();
                    if (block.getRenderType() != -1) {
                        final WorldRenderer worldrenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(k);
                        worldrenderer.setBlockLayer(enumworldblocklayer);
                        final RenderEnv renderenv = worldrenderer.getRenderEnv(iblockstate, blockposm);
                        renderenv.setRegionRenderCacheBuilder(generator.getRegionRenderCacheBuilder());
                        if (!compiledchunk.isLayerStarted(enumworldblocklayer)) {
                            compiledchunk.setLayerStarted(enumworldblocklayer);
                            this.preRenderBlocks(worldrenderer, blockpos);
                        }
                        final boolean[] array = aboolean;
                        final int n = k;
                        array[n] |= blockrendererdispatcher.renderBlock(iblockstate, blockposm, chunkcacheof, worldrenderer);
                        if (renderenv.isOverlaysRendered()) {
                            this.postRenderOverlays(generator.getRegionRenderCacheBuilder(), compiledchunk, aboolean);
                            renderenv.setOverlaysRendered(false);
                        }
                    }
                }
                if (flag2) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, null);
                }
            }
            EnumWorldBlockLayer[] enum_WORLD_BLOCK_LAYERS;
            for (int length = (enum_WORLD_BLOCK_LAYERS = RenderChunk.ENUM_WORLD_BLOCK_LAYERS).length, l = 0; l < length; ++l) {
                final EnumWorldBlockLayer enumworldblocklayer2 = enum_WORLD_BLOCK_LAYERS[l];
                if (aboolean[enumworldblocklayer2.ordinal()]) {
                    compiledchunk.setLayerUsed(enumworldblocklayer2);
                }
                if (compiledchunk.isLayerStarted(enumworldblocklayer2)) {
                    if (Config.isShaders()) {
                        SVertexBuilder.calcNormalChunkLayer(generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer2));
                    }
                    final WorldRenderer worldrenderer2 = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer2);
                    this.postRenderBlocks(enumworldblocklayer2, x, y, z, worldrenderer2, compiledchunk);
                    if (worldrenderer2.animatedSprites != null) {
                        compiledchunk.setAnimatedSprites(enumworldblocklayer2, (BitSet)worldrenderer2.animatedSprites.clone());
                    }
                }
                else {
                    compiledchunk.setAnimatedSprites(enumworldblocklayer2, null);
                }
            }
            chunkcacheof.renderFinish();
        }
        compiledchunk.setVisibility(lvt_10_1_.computeVisibility());
        this.lockCompileTask.lock();
        try {
            final Set<TileEntity> set = Sets.newHashSet((Iterable)lvt_11_1_);
            final Set<TileEntity> set2 = Sets.newHashSet((Iterable)this.setTileEntities);
            set.removeAll(this.setTileEntities);
            set2.removeAll(lvt_11_1_);
            this.setTileEntities.clear();
            this.setTileEntities.addAll(lvt_11_1_);
            this.renderGlobal.updateTileEntities(set2, set);
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
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
        this.lockCompileTask.unlock();
    }
    
    public ReentrantLock getLockCompileTask() {
        return this.lockCompileTask;
    }
    
    public ChunkCompileTaskGenerator makeCompileTaskChunk() {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;
        try {
            this.finishCompileTask();
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
            chunkcompiletaskgenerator = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
        return chunkcompiletaskgenerator;
    }
    
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
            (this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)).setCompiledChunk(this.compiledChunk);
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator3 = chunkcompiletaskgenerator4 = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
        return chunkcompiletaskgenerator4;
    }
    
    private void preRenderBlocks(final WorldRenderer worldRendererIn, final BlockPos pos) {
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        if (Config.isRenderRegions()) {
            final int i = 8;
            int j = pos.getX() >> i << i;
            final int k = pos.getY() >> i << i;
            int l = pos.getZ() >> i << i;
            j = this.regionX;
            l = this.regionZ;
            worldRendererIn.setTranslation(-j, -k, -l);
        }
        else {
            worldRendererIn.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
        }
    }
    
    private void postRenderBlocks(final EnumWorldBlockLayer layer, final float x, final float y, final float z, final WorldRenderer worldRendererIn, final CompiledChunk compiledChunkIn) {
        if (layer == EnumWorldBlockLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer)) {
            worldRendererIn.sortVertexData(x, y, z);
            compiledChunkIn.setState(worldRendererIn.getVertexState());
        }
        worldRendererIn.finishDrawing();
    }
    
    private void initModelviewMatrix() {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        final float f = 1.000001f;
        GlStateManager.translate(-8.0f, -8.0f, -8.0f);
        GlStateManager.scale(f, f, f);
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
        this.lockCompiledChunk.unlock();
    }
    
    public void stopCompileTask() {
        this.finishCompileTask();
        this.compiledChunk = CompiledChunk.DUMMY;
    }
    
    public void deleteGlResources() {
        this.stopCompileTask();
        for (int i = 0; i < EnumWorldBlockLayer.values().length; ++i) {
            if (this.vertexBuffers[i] != null) {
                this.vertexBuffers[i].deleteGlBuffers();
            }
        }
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public void setNeedsUpdate(final boolean needsUpdateIn) {
        this.needsUpdate = needsUpdateIn;
        if (needsUpdateIn) {
            if (this.isWorldPlayerUpdate()) {
                this.playerUpdate = true;
            }
        }
        else {
            this.playerUpdate = false;
        }
    }
    
    public boolean isNeedsUpdate() {
        return this.needsUpdate;
    }
    
    public BlockPos getBlockPosOffset16(final EnumFacing p_181701_1_) {
        return this.getPositionOffset16(p_181701_1_);
    }
    
    public BlockPos getPositionOffset16(final EnumFacing p_getPositionOffset16_1_) {
        final int i = p_getPositionOffset16_1_.getIndex();
        BlockPos blockpos = this.positionOffsets16[i];
        if (blockpos == null) {
            blockpos = this.getPosition().offset(p_getPositionOffset16_1_, 16);
            this.positionOffsets16[i] = blockpos;
        }
        return blockpos;
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
    
    protected RegionRenderCache createRegionRenderCache(final World p_createRegionRenderCache_1_, final BlockPos p_createRegionRenderCache_2_, final BlockPos p_createRegionRenderCache_3_, final int p_createRegionRenderCache_4_) {
        return new RegionRenderCache(p_createRegionRenderCache_1_, p_createRegionRenderCache_2_, p_createRegionRenderCache_3_, p_createRegionRenderCache_4_);
    }
    
    private EnumWorldBlockLayer fixBlockLayer(final IBlockState p_fixBlockLayer_1_, final EnumWorldBlockLayer p_fixBlockLayer_2_) {
        if (CustomBlockLayers.isActive()) {
            final EnumWorldBlockLayer enumworldblocklayer = CustomBlockLayers.getRenderLayer(p_fixBlockLayer_1_);
            if (enumworldblocklayer != null) {
                return enumworldblocklayer;
            }
        }
        if (!this.fixBlockLayer) {
            return p_fixBlockLayer_2_;
        }
        if (this.isMipmaps) {
            if (p_fixBlockLayer_2_ == EnumWorldBlockLayer.CUTOUT) {
                final Block block = p_fixBlockLayer_1_.getBlock();
                if (block instanceof BlockRedstoneWire) {
                    return p_fixBlockLayer_2_;
                }
                if (block instanceof BlockCactus) {
                    return p_fixBlockLayer_2_;
                }
                return EnumWorldBlockLayer.CUTOUT_MIPPED;
            }
        }
        else if (p_fixBlockLayer_2_ == EnumWorldBlockLayer.CUTOUT_MIPPED) {
            return EnumWorldBlockLayer.CUTOUT;
        }
        return p_fixBlockLayer_2_;
    }
    
    private void postRenderOverlays(final RegionRenderCacheBuilder p_postRenderOverlays_1_, final CompiledChunk p_postRenderOverlays_2_, final boolean[] p_postRenderOverlays_3_) {
        this.postRenderOverlay(EnumWorldBlockLayer.CUTOUT, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
        this.postRenderOverlay(EnumWorldBlockLayer.CUTOUT_MIPPED, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
        this.postRenderOverlay(EnumWorldBlockLayer.TRANSLUCENT, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
    }
    
    private void postRenderOverlay(final EnumWorldBlockLayer p_postRenderOverlay_1_, final RegionRenderCacheBuilder p_postRenderOverlay_2_, final CompiledChunk p_postRenderOverlay_3_, final boolean[] p_postRenderOverlay_4_) {
        final WorldRenderer worldrenderer = p_postRenderOverlay_2_.getWorldRendererByLayer(p_postRenderOverlay_1_);
        if (worldrenderer.isDrawing()) {
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
        chunk = this.world.getChunkFromBlockCoords(p_getChunk_1_);
        return this.chunk = chunk;
    }
    
    public boolean isChunkRegionEmpty() {
        return this.isChunkRegionEmpty(this.position);
    }
    
    private boolean isChunkRegionEmpty(final BlockPos p_isChunkRegionEmpty_1_) {
        final int i = p_isChunkRegionEmpty_1_.getY();
        final int j = i + 15;
        return this.getChunk(p_isChunkRegionEmpty_1_).getAreLevelsEmpty(i, j);
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
}
