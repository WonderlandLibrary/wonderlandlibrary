/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.block.Block;
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

public class RenderChunk {
    private BlockPos position;
    private EnumMap<EnumFacing, BlockPos> field_181702_p;
    private final ReentrantLock lockCompileTask;
    private final Set<TileEntity> field_181056_j;
    private World world;
    private final VertexBuffer[] vertexBuffers;
    private ChunkCompileTaskGenerator compileTask = null;
    private boolean needsUpdate = true;
    public static int renderChunksUpdated;
    private final ReentrantLock lockCompiledChunk;
    private int frameIndex = -1;
    public CompiledChunk compiledChunk = CompiledChunk.DUMMY;
    public AxisAlignedBB boundingBox;
    private final int index;
    private final RenderGlobal renderGlobal;
    private final FloatBuffer modelviewMatrix;

    private void initModelviewMatrix() {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        float f = 1.000001f;
        GlStateManager.translate(-8.0f, -8.0f, -8.0f);
        GlStateManager.scale(f, f, f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.getFloat(2982, this.modelviewMatrix);
        GlStateManager.popMatrix();
    }

    public void deleteGlResources() {
        this.stopCompileTask();
        this.world = null;
        int n = 0;
        while (n < EnumWorldBlockLayer.values().length) {
            if (this.vertexBuffers[n] != null) {
                this.vertexBuffers[n].deleteGlBuffers();
            }
            ++n;
        }
    }

    public void multModelviewMatrix() {
        GlStateManager.multMatrix(this.modelviewMatrix);
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public CompiledChunk getCompiledChunk() {
        return this.compiledChunk;
    }

    public void setCompiledChunk(CompiledChunk compiledChunk) {
        this.lockCompiledChunk.lock();
        this.compiledChunk = compiledChunk;
        this.lockCompiledChunk.unlock();
    }

    private void preRenderBlocks(WorldRenderer worldRenderer, BlockPos blockPos) {
        worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
        worldRenderer.setTranslation(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
    }

    public void setNeedsUpdate(boolean bl) {
        this.needsUpdate = bl;
    }

    public BlockPos func_181701_a(EnumFacing enumFacing) {
        return this.field_181702_p.get(enumFacing);
    }

    private void postRenderBlocks(EnumWorldBlockLayer enumWorldBlockLayer, float f, float f2, float f3, WorldRenderer worldRenderer, CompiledChunk compiledChunk) {
        if (enumWorldBlockLayer == EnumWorldBlockLayer.TRANSLUCENT && !compiledChunk.isLayerEmpty(enumWorldBlockLayer)) {
            worldRenderer.func_181674_a(f, f2, f3);
            compiledChunk.setState(worldRenderer.func_181672_a());
        }
        worldRenderer.finishDrawing();
    }

    public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
        this.lockCompileTask.lock();
        if (this.compileTask == null || this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
            ChunkCompileTaskGenerator chunkCompileTaskGenerator;
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
            this.compileTask.setCompiledChunk(this.compiledChunk);
            ChunkCompileTaskGenerator chunkCompileTaskGenerator2 = chunkCompileTaskGenerator = this.compileTask;
            this.lockCompileTask.unlock();
            return chunkCompileTaskGenerator2;
        }
        ChunkCompileTaskGenerator chunkCompileTaskGenerator = null;
        this.lockCompileTask.unlock();
        return chunkCompileTaskGenerator;
    }

    public boolean setFrameIndex(int n) {
        if (this.frameIndex == n) {
            return false;
        }
        this.frameIndex = n;
        return true;
    }

    public void stopCompileTask() {
        this.finishCompileTask();
        this.compiledChunk = CompiledChunk.DUMMY;
    }

    public void rebuildChunk(float f, float f2, float f3, ChunkCompileTaskGenerator chunkCompileTaskGenerator) {
        Object object;
        Object object2;
        CompiledChunk compiledChunk = new CompiledChunk();
        boolean bl = true;
        BlockPos blockPos = this.position;
        BlockPos blockPos2 = blockPos.add(15, 15, 15);
        chunkCompileTaskGenerator.getLock().lock();
        if (chunkCompileTaskGenerator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
            chunkCompileTaskGenerator.getLock().unlock();
            return;
        }
        RegionRenderCache regionRenderCache = new RegionRenderCache(this.world, blockPos.add(-1, -1, -1), blockPos2.add(1, 1, 1), 1);
        chunkCompileTaskGenerator.setCompiledChunk(compiledChunk);
        chunkCompileTaskGenerator.getLock().unlock();
        VisGraph visGraph = new VisGraph();
        HashSet hashSet = Sets.newHashSet();
        if (!regionRenderCache.extendedLevelsInChunkCache()) {
            Object object3;
            ++renderChunksUpdated;
            object2 = new boolean[EnumWorldBlockLayer.values().length];
            object = Minecraft.getMinecraft().getBlockRendererDispatcher();
            for (BlockPos.MutableBlockPos object4 : BlockPos.getAllInBoxMutable(blockPos, blockPos2)) {
                Object object5;
                IBlockState iBlockState = regionRenderCache.getBlockState(object4);
                object3 = iBlockState.getBlock();
                if (((Block)object3).isOpaqueCube()) {
                    visGraph.func_178606_a(object4);
                }
                if (((Block)object3).hasTileEntity()) {
                    object5 = regionRenderCache.getTileEntity(new BlockPos(object4));
                    TileEntitySpecialRenderer tileEntitySpecialRenderer = TileEntityRendererDispatcher.instance.getSpecialRenderer((TileEntity)object5);
                    if (object5 != null && tileEntitySpecialRenderer != null) {
                        compiledChunk.addTileEntity((TileEntity)object5);
                        if (tileEntitySpecialRenderer.func_181055_a()) {
                            hashSet.add(object5);
                        }
                    }
                }
                object5 = ((Block)object3).getBlockLayer();
                int n = object5.ordinal();
                if (((Block)object3).getRenderType() == -1) continue;
                WorldRenderer worldRenderer = chunkCompileTaskGenerator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(n);
                if (!compiledChunk.isLayerStarted((EnumWorldBlockLayer)((Object)object5))) {
                    compiledChunk.setLayerStarted((EnumWorldBlockLayer)((Object)object5));
                    this.preRenderBlocks(worldRenderer, blockPos);
                }
                Object object6 = object2;
                int n2 = n;
                object6[n2] = object6[n2] | ((BlockRendererDispatcher)object).renderBlock(iBlockState, object4, regionRenderCache, worldRenderer);
            }
            object3 = EnumWorldBlockLayer.values();
            int n = ((EnumWorldBlockLayer[])object3).length;
            int n3 = 0;
            while (n3 < n) {
                Object object7 = object3[n3];
                if (object2[((Enum)object7).ordinal()] != false) {
                    compiledChunk.setLayerUsed((EnumWorldBlockLayer)((Object)object7));
                }
                if (compiledChunk.isLayerStarted((EnumWorldBlockLayer)((Object)object7))) {
                    this.postRenderBlocks((EnumWorldBlockLayer)((Object)object7), f, f2, f3, chunkCompileTaskGenerator.getRegionRenderCacheBuilder().getWorldRendererByLayer((EnumWorldBlockLayer)((Object)object7)), compiledChunk);
                }
                ++n3;
            }
        }
        compiledChunk.setVisibility(visGraph.computeVisibility());
        this.lockCompileTask.lock();
        object2 = Sets.newHashSet((Iterable)hashSet);
        object = Sets.newHashSet(this.field_181056_j);
        object2.removeAll(this.field_181056_j);
        object.removeAll(hashSet);
        this.field_181056_j.clear();
        this.field_181056_j.addAll(hashSet);
        this.renderGlobal.func_181023_a((Collection<TileEntity>)object, (Collection<TileEntity>)object2);
        this.lockCompileTask.unlock();
    }

    public ChunkCompileTaskGenerator makeCompileTaskChunk() {
        this.lockCompileTask.lock();
        this.finishCompileTask();
        ChunkCompileTaskGenerator chunkCompileTaskGenerator = this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
        this.lockCompileTask.unlock();
        return chunkCompileTaskGenerator;
    }

    public void resortTransparency(float f, float f2, float f3, ChunkCompileTaskGenerator chunkCompileTaskGenerator) {
        CompiledChunk compiledChunk = chunkCompileTaskGenerator.getCompiledChunk();
        if (compiledChunk.getState() != null && !compiledChunk.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)) {
            this.preRenderBlocks(chunkCompileTaskGenerator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), this.position);
            chunkCompileTaskGenerator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT).setVertexState(compiledChunk.getState());
            this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, f, f2, f3, chunkCompileTaskGenerator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), compiledChunk);
        }
    }

    protected void finishCompileTask() {
        this.lockCompileTask.lock();
        if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
            this.compileTask.finish();
            this.compileTask = null;
        }
        this.lockCompileTask.unlock();
    }

    public boolean isNeedsUpdate() {
        return this.needsUpdate;
    }

    public void setPosition(BlockPos blockPos) {
        this.stopCompileTask();
        this.position = blockPos;
        this.boundingBox = new AxisAlignedBB(blockPos, blockPos.add(16, 16, 16));
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing = enumFacingArray[n2];
            this.field_181702_p.put(enumFacing, blockPos.offset(enumFacing, 16));
            ++n2;
        }
        this.initModelviewMatrix();
    }

    public VertexBuffer getVertexBufferByLayer(int n) {
        return this.vertexBuffers[n];
    }

    public RenderChunk(World world, RenderGlobal renderGlobal, BlockPos blockPos, int n) {
        this.lockCompileTask = new ReentrantLock();
        this.lockCompiledChunk = new ReentrantLock();
        this.field_181056_j = Sets.newHashSet();
        this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
        this.field_181702_p = Maps.newEnumMap(EnumFacing.class);
        this.world = world;
        this.renderGlobal = renderGlobal;
        this.index = n;
        if (!blockPos.equals(this.getPosition())) {
            this.setPosition(blockPos);
        }
        if (OpenGlHelper.useVbo()) {
            int n2 = 0;
            while (n2 < EnumWorldBlockLayer.values().length) {
                this.vertexBuffers[n2] = new VertexBuffer(DefaultVertexFormats.BLOCK);
                ++n2;
            }
        }
    }

    public ReentrantLock getLockCompileTask() {
        return this.lockCompileTask;
    }
}

