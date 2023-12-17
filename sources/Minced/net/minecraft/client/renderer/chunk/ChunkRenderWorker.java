// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import net.minecraft.world.World;
import java.util.concurrent.CancellationException;
import java.util.List;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import net.minecraft.util.BlockRenderLayer;
import com.google.common.collect.Lists;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker implements Runnable
{
    private static final Logger LOGGER;
    private final ChunkRenderDispatcher chunkRenderDispatcher;
    private final RegionRenderCacheBuilder regionRenderCacheBuilder;
    private boolean shouldRun;
    
    public ChunkRenderWorker(final ChunkRenderDispatcher chunkRenderDispatcherIn) {
        this(chunkRenderDispatcherIn, null);
    }
    
    public ChunkRenderWorker(final ChunkRenderDispatcher chunkRenderDispatcherIn, @Nullable final RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
        this.shouldRun = true;
        this.chunkRenderDispatcher = chunkRenderDispatcherIn;
        this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
    }
    
    @Override
    public void run() {
        while (this.shouldRun) {
            try {
                this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
                continue;
            }
            catch (InterruptedException var3) {
                ChunkRenderWorker.LOGGER.debug("Stopping chunk worker due to interrupt");
                return;
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Batching chunks");
                Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(crashreport));
                return;
            }
            break;
        }
    }
    
    protected void processTask(final ChunkCompileTaskGenerator generator) throws InterruptedException {
        generator.getLock().lock();
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
                if (!generator.isFinished()) {
                    ChunkRenderWorker.LOGGER.warn("Chunk render task was {} when I expected it to be pending; ignoring task", (Object)generator.getStatus());
                }
                return;
            }
            Minecraft.getMinecraft();
            final BlockPos blockpos = new BlockPos(Minecraft.player);
            final BlockPos blockpos2 = generator.getRenderChunk().getPosition();
            final int i = 16;
            final int j = 8;
            final int k = 24;
            if (blockpos2.add(8, 8, 8).distanceSq(blockpos) > 576.0) {
                final World world = generator.getRenderChunk().getWorld();
                final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(blockpos2);
                if (!this.isChunkExisting(blockpos$mutableblockpos.setPos(blockpos2).move(EnumFacing.WEST, 16), world) || !this.isChunkExisting(blockpos$mutableblockpos.setPos(blockpos2).move(EnumFacing.NORTH, 16), world) || !this.isChunkExisting(blockpos$mutableblockpos.setPos(blockpos2).move(EnumFacing.EAST, 16), world) || !this.isChunkExisting(blockpos$mutableblockpos.setPos(blockpos2).move(EnumFacing.SOUTH, 16), world)) {
                    return;
                }
            }
            generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
        }
        finally {
            generator.getLock().unlock();
        }
        final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (entity == null) {
            generator.finish();
        }
        else {
            generator.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
            final float f = (float)entity.posX;
            final float f2 = (float)entity.posY + entity.getEyeHeight();
            final float f3 = (float)entity.posZ;
            final ChunkCompileTaskGenerator.Type chunkcompiletaskgenerator$type = generator.getType();
            if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                generator.getRenderChunk().rebuildChunk(f, f2, f3, generator);
            }
            else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                generator.getRenderChunk().resortTransparency(f, f2, f3, generator);
            }
            generator.getLock().lock();
            try {
                if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                    if (!generator.isFinished()) {
                        ChunkRenderWorker.LOGGER.warn("Chunk render task was {} when I expected it to be compiling; aborting task", (Object)generator.getStatus());
                    }
                    this.freeRenderBuilder(generator);
                    return;
                }
                generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
            }
            finally {
                generator.getLock().unlock();
            }
            final CompiledChunk compiledchunk = generator.getCompiledChunk();
            final ArrayList arraylist = Lists.newArrayList();
            if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                for (final BlockRenderLayer blockrenderlayer : BlockRenderLayer.values()) {
                    if (compiledchunk.isLayerStarted(blockrenderlayer)) {
                        arraylist.add(this.chunkRenderDispatcher.uploadChunk(blockrenderlayer, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), generator.getRenderChunk(), compiledchunk, generator.getDistanceSq()));
                    }
                }
            }
            else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                arraylist.add(this.chunkRenderDispatcher.uploadChunk(BlockRenderLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT), generator.getRenderChunk(), compiledchunk, generator.getDistanceSq()));
            }
            final ListenableFuture<List<Object>> listenablefuture = (ListenableFuture<List<Object>>)Futures.allAsList((Iterable)arraylist);
            generator.addFinishRunnable(new Runnable() {
                @Override
                public void run() {
                    listenablefuture.cancel(false);
                }
            });
            Futures.addCallback((ListenableFuture)listenablefuture, (FutureCallback)new FutureCallback<List<Object>>() {
                public void onSuccess(@Nullable final List<Object> p_onSuccess_1_) {
                    ChunkRenderWorker.this.freeRenderBuilder(generator);
                    generator.getLock().lock();
                    Label_0111: {
                        try {
                            if (generator.getStatus() == ChunkCompileTaskGenerator.Status.UPLOADING) {
                                generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                                break Label_0111;
                            }
                            if (!generator.isFinished()) {
                                ChunkRenderWorker.LOGGER.warn("Chunk render task was {} when I expected it to be uploading; aborting task", (Object)generator.getStatus());
                            }
                        }
                        finally {
                            generator.getLock().unlock();
                        }
                        return;
                    }
                    generator.getRenderChunk().setCompiledChunk(compiledchunk);
                }
                
                public void onFailure(final Throwable p_onFailure_1_) {
                    ChunkRenderWorker.this.freeRenderBuilder(generator);
                    if (!(p_onFailure_1_ instanceof CancellationException) && !(p_onFailure_1_ instanceof InterruptedException)) {
                        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(p_onFailure_1_, "Rendering chunk"));
                    }
                }
            });
        }
    }
    
    private boolean isChunkExisting(final BlockPos pos, final World worldIn) {
        return !worldIn.getChunk(pos.getX() >> 4, pos.getZ() >> 4).isEmpty();
    }
    
    private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
        return (this.regionRenderCacheBuilder != null) ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
    }
    
    private void freeRenderBuilder(final ChunkCompileTaskGenerator taskGenerator) {
        if (this.regionRenderCacheBuilder == null) {
            this.chunkRenderDispatcher.freeRenderBuilder(taskGenerator.getRegionRenderCacheBuilder());
        }
    }
    
    public void notifyToStop() {
        this.shouldRun = false;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
