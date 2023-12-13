/*
 * Decompiled with CFR 0.152.
 */
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
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
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
import optfine.BlockPosM;
import optfine.Reflector;

public class RenderChunk {
    private World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    private BlockPos position;
    public CompiledChunk compiledChunk = CompiledChunk.DUMMY;
    private final ReentrantLock lockCompileTask = new ReentrantLock();
    private final ReentrantLock lockCompiledChunk = new ReentrantLock();
    private ChunkCompileTaskGenerator compileTask = null;
    private final Set field_181056_j = Sets.newHashSet();
    private final int index;
    private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
    private final VertexBuffer[] vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
    public AxisAlignedBB boundingBox;
    private int frameIndex = -1;
    private boolean needsUpdate = true;
    private EnumMap field_181702_p;
    private static final String __OBFID = "CL_00002452";
    private BlockPos[] positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
    private static EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS;
    private EnumWorldBlockLayer[] blockLayersSingle = new EnumWorldBlockLayer[1];

    public RenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos blockPosIn, int indexIn) {
        this.world = worldIn;
        this.renderGlobal = renderGlobalIn;
        this.index = indexIn;
        if (!blockPosIn.equals(this.getPosition())) {
            this.setPosition(blockPosIn);
        }
        if (OpenGlHelper.useVbo()) {
            for (int i2 = 0; i2 < EnumWorldBlockLayer.values().length; ++i2) {
                this.vertexBuffers[i2] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }

    public boolean setFrameIndex(int frameIndexIn) {
        if (this.frameIndex == frameIndexIn) {
            return false;
        }
        this.frameIndex = frameIndexIn;
        return true;
    }

    public VertexBuffer getVertexBufferByLayer(int layer) {
        return this.vertexBuffers[layer];
    }

    public void setPosition(BlockPos pos) {
        this.stopCompileTask();
        this.position = pos;
        this.boundingBox = new AxisAlignedBB(pos, pos.add(16, 16, 16));
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i2 = aenumfacing.length;
        this.initModelviewMatrix();
        for (int j2 = 0; j2 < this.positionOffsets16.length; ++j2) {
            this.positionOffsets16[j2] = null;
        }
    }

    public void resortTransparency(float x2, float y2, float z2, ChunkCompileTaskGenerator generator) {
        CompiledChunk compiledchunk = generator.getCompiledChunk();
        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)) {
            WorldRenderer worldrenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT);
            this.preRenderBlocks(worldrenderer, this.position);
            worldrenderer.setVertexState(compiledchunk.getState());
            this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, x2, y2, z2, worldrenderer, compiledchunk);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void rebuildChunk(float x2, float y2, float z2, ChunkCompileTaskGenerator generator) {
        RegionRenderCache regionrendercache;
        CompiledChunk compiledchunk = new CompiledChunk();
        boolean flag = true;
        BlockPos blockpos = this.position;
        BlockPos blockpos1 = blockpos.add(15, 15, 15);
        generator.getLock().lock();
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }
            if (this.world == null) {
                return;
            }
            regionrendercache = new RegionRenderCache(this.world, blockpos.add(-1, -1, -1), blockpos1.add(1, 1, 1), 1);
            generator.setCompiledChunk(compiledchunk);
        }
        finally {
            generator.getLock().unlock();
        }
        VisGraph var10 = new VisGraph();
        HashSet<TileEntity> var11 = Sets.newHashSet();
        if (!regionrendercache.extendedLevelsInChunkCache()) {
            ++renderChunksUpdated;
            boolean[] aboolean = new boolean[EnumWorldBlockLayer.values().length];
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            Iterator iterator = BlockPosM.getAllInBoxMutable(blockpos, blockpos1).iterator();
            boolean flag1 = Reflector.ForgeBlock_hasTileEntity.exists();
            boolean flag2 = Reflector.ForgeBlock_canRenderInLayer.exists();
            boolean flag3 = Reflector.ForgeHooksClient_setRenderLayer.exists();
            while (iterator.hasNext()) {
                EnumWorldBlockLayer[] aenumworldblocklayer;
                boolean flag4;
                BlockPosM blockposm = (BlockPosM)iterator.next();
                IBlockState iblockstate = regionrendercache.getBlockState(blockposm);
                Block block = iblockstate.getBlock();
                if (block.isOpaqueCube()) {
                    var10.func_178606_a(blockposm);
                }
                if (flag4 = flag1 ? Reflector.callBoolean(iterator, Reflector.ForgeBlock_hasTileEntity, blockrendererdispatcher) : block.hasTileEntity()) {
                    TileEntity tileentity = regionrendercache.getTileEntity(new BlockPos(blockposm));
                    TileEntitySpecialRenderer tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tileentity);
                    if (tileentity != null && tileentityspecialrenderer != null) {
                        compiledchunk.addTileEntity(tileentity);
                        if (tileentityspecialrenderer.func_181055_a()) {
                            var11.add(tileentity);
                        }
                    }
                }
                if (flag2) {
                    aenumworldblocklayer = ENUM_WORLD_BLOCK_LAYERS;
                } else {
                    aenumworldblocklayer = this.blockLayersSingle;
                    aenumworldblocklayer[0] = block.getBlockLayer();
                }
                for (int i2 = 0; i2 < aenumworldblocklayer.length; ++i2) {
                    boolean flag5;
                    EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[i2];
                    if (flag2 && !(flag5 = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, new Object[]{enumworldblocklayer}))) continue;
                    enumworldblocklayer = this.fixBlockLayer(block, enumworldblocklayer);
                    if (flag3) {
                        Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[]{enumworldblocklayer});
                    }
                    int j2 = enumworldblocklayer.ordinal();
                    if (block.getRenderType() == -1) continue;
                    WorldRenderer worldrenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j2);
                    worldrenderer.setBlockLayer(enumworldblocklayer);
                    if (!compiledchunk.isLayerStarted(enumworldblocklayer)) {
                        compiledchunk.setLayerStarted(enumworldblocklayer);
                        this.preRenderBlocks(worldrenderer, blockpos);
                    }
                    int n2 = j2;
                    aboolean[n2] = aboolean[n2] | blockrendererdispatcher.renderBlock(iblockstate, blockposm, regionrendercache, worldrenderer);
                }
            }
            for (EnumWorldBlockLayer enumworldblocklayer1 : EnumWorldBlockLayer.values()) {
                if (aboolean[enumworldblocklayer1.ordinal()]) {
                    compiledchunk.setLayerUsed(enumworldblocklayer1);
                }
                if (!compiledchunk.isLayerStarted(enumworldblocklayer1)) continue;
                this.postRenderBlocks(enumworldblocklayer1, x2, y2, z2, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer1), compiledchunk);
            }
        }
        compiledchunk.setVisibility(var10.computeVisibility());
        this.lockCompileTask.lock();
        try {
            HashSet hashset1 = Sets.newHashSet(var11);
            HashSet hashset2 = Sets.newHashSet(this.field_181056_j);
            hashset1.removeAll(this.field_181056_j);
            hashset2.removeAll(var11);
            this.field_181056_j.clear();
            this.field_181056_j.addAll(var11);
            this.renderGlobal.func_181023_a(hashset2, hashset1);
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
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;
        this.lockCompileTask.lock();
        try {
            this.finishCompileTask();
            chunkcompiletaskgenerator = this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
        }
        finally {
            this.lockCompileTask.unlock();
        }
        return chunkcompiletaskgenerator;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
        ChunkCompileTaskGenerator chunkcompiletaskgenerator1;
        this.lockCompileTask.lock();
        try {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator;
            if (this.compileTask != null && this.compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING) {
                ChunkCompileTaskGenerator chunkcompiletaskgenerator2;
                ChunkCompileTaskGenerator chunkCompileTaskGenerator = chunkcompiletaskgenerator2 = null;
                return chunkCompileTaskGenerator;
            }
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
            this.compileTask.setCompiledChunk(this.compiledChunk);
            chunkcompiletaskgenerator1 = chunkcompiletaskgenerator = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        return chunkcompiletaskgenerator1;
    }

    private void preRenderBlocks(WorldRenderer worldRendererIn, BlockPos pos) {
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
    }

    private void postRenderBlocks(EnumWorldBlockLayer layer, float x2, float y2, float z2, WorldRenderer worldRendererIn, CompiledChunk compiledChunkIn) {
        if (layer == EnumWorldBlockLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer)) {
            worldRendererIn.func_181674_a(x2, y2, z2);
            compiledChunkIn.setState(worldRendererIn.func_181672_a());
        }
        worldRendererIn.finishDrawing();
    }

    private void initModelviewMatrix() {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        float f2 = 1.000001f;
        GlStateManager.translate(-8.0f, -8.0f, -8.0f);
        GlStateManager.scale(f2, f2, f2);
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

    public void setCompiledChunk(CompiledChunk compiledChunkIn) {
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
        this.world = null;
        for (int i2 = 0; i2 < EnumWorldBlockLayer.values().length; ++i2) {
            if (this.vertexBuffers[i2] == null) continue;
            this.vertexBuffers[i2].deleteGlBuffers();
        }
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public void setNeedsUpdate(boolean needsUpdateIn) {
        this.needsUpdate = needsUpdateIn;
    }

    public boolean isNeedsUpdate() {
        return this.needsUpdate;
    }

    public BlockPos func_181701_a(EnumFacing p_181701_1_) {
        return this.getPositionOffset16(p_181701_1_);
    }

    public BlockPos getPositionOffset16(EnumFacing p_getPositionOffset16_1_) {
        int i2 = p_getPositionOffset16_1_.getIndex();
        BlockPos blockpos = this.positionOffsets16[i2];
        if (blockpos == null) {
            this.positionOffsets16[i2] = blockpos = this.getPosition().offset(p_getPositionOffset16_1_, 16);
        }
        return blockpos;
    }

    private EnumWorldBlockLayer fixBlockLayer(Block p_fixBlockLayer_1_, EnumWorldBlockLayer p_fixBlockLayer_2_) {
        return p_fixBlockLayer_2_ == EnumWorldBlockLayer.CUTOUT ? (p_fixBlockLayer_1_ instanceof BlockRedstoneWire ? p_fixBlockLayer_2_ : (p_fixBlockLayer_1_ instanceof BlockCactus ? p_fixBlockLayer_2_ : EnumWorldBlockLayer.CUTOUT_MIPPED)) : p_fixBlockLayer_2_;
    }

    static {
        ENUM_WORLD_BLOCK_LAYERS = EnumWorldBlockLayer.values();
    }
}

