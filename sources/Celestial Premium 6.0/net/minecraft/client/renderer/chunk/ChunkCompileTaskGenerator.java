/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class ChunkCompileTaskGenerator
implements Comparable<ChunkCompileTaskGenerator> {
    private final RenderChunk renderChunk;
    private final ReentrantLock lock = new ReentrantLock();
    private final List<Runnable> listFinishRunnables = Lists.newArrayList();
    private final Type type;
    private final double distanceSq;
    private RegionRenderCacheBuilder regionRenderCacheBuilder;
    private CompiledChunk compiledChunk;
    private Status status = Status.PENDING;
    private boolean finished;

    public ChunkCompileTaskGenerator(RenderChunk p_i46560_1_, Type p_i46560_2_, double p_i46560_3_) {
        this.renderChunk = p_i46560_1_;
        this.type = p_i46560_2_;
        this.distanceSq = p_i46560_3_;
    }

    public Status getStatus() {
        return this.status;
    }

    public RenderChunk getRenderChunk() {
        return this.renderChunk;
    }

    public CompiledChunk getCompiledChunk() {
        return this.compiledChunk;
    }

    public void setCompiledChunk(CompiledChunk compiledChunkIn) {
        this.compiledChunk = compiledChunkIn;
    }

    public RegionRenderCacheBuilder getRegionRenderCacheBuilder() {
        return this.regionRenderCacheBuilder;
    }

    public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
        this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
    }

    public void setStatus(Status statusIn) {
        this.lock.lock();
        try {
            this.status = statusIn;
        }
        finally {
            this.lock.unlock();
        }
    }

    public void finish() {
        this.lock.lock();
        try {
            if (this.type == Type.REBUILD_CHUNK && this.status != Status.DONE) {
                this.renderChunk.setNeedsUpdate(false);
            }
            this.finished = true;
            this.status = Status.DONE;
            for (Runnable runnable : this.listFinishRunnables) {
                runnable.run();
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    public void addFinishRunnable(Runnable runnable) {
        this.lock.lock();
        try {
            this.listFinishRunnables.add(runnable);
            if (this.finished) {
                runnable.run();
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    public ReentrantLock getLock() {
        return this.lock;
    }

    public Type getType() {
        return this.type;
    }

    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public int compareTo(ChunkCompileTaskGenerator p_compareTo_1_) {
        return Doubles.compare(this.distanceSq, p_compareTo_1_.distanceSq);
    }

    public double getDistanceSq() {
        return this.distanceSq;
    }

    public static enum Type {
        REBUILD_CHUNK,
        RESORT_TRANSPARENCY;

    }

    public static enum Status {
        PENDING,
        COMPILING,
        UPLOADING,
        DONE;

    }
}

