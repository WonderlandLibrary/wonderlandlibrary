/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class ChunkCompileTaskGenerator {
    private final ReentrantLock lock = new ReentrantLock();
    private RegionRenderCacheBuilder regionRenderCacheBuilder;
    private final RenderChunk renderChunk;
    private CompiledChunk compiledChunk;
    private final List<Runnable> listFinishRunnables = Lists.newArrayList();
    private final Type type;
    private boolean finished;
    private Status status = Status.PENDING;

    public boolean isFinished() {
        return this.finished;
    }

    public void setCompiledChunk(CompiledChunk compiledChunk) {
        this.compiledChunk = compiledChunk;
    }

    public Type getType() {
        return this.type;
    }

    public Status getStatus() {
        return this.status;
    }

    public CompiledChunk getCompiledChunk() {
        return this.compiledChunk;
    }

    public void setStatus(Status status) {
        this.lock.lock();
        this.status = status;
        this.lock.unlock();
    }

    public RegionRenderCacheBuilder getRegionRenderCacheBuilder() {
        return this.regionRenderCacheBuilder;
    }

    public ReentrantLock getLock() {
        return this.lock;
    }

    public ChunkCompileTaskGenerator(RenderChunk renderChunk, Type type) {
        this.renderChunk = renderChunk;
        this.type = type;
    }

    public void finish() {
        this.lock.lock();
        if (this.type == Type.REBUILD_CHUNK && this.status != Status.DONE) {
            this.renderChunk.setNeedsUpdate(true);
        }
        this.finished = true;
        this.status = Status.DONE;
        for (Runnable runnable : this.listFinishRunnables) {
            runnable.run();
        }
        this.lock.unlock();
    }

    public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder regionRenderCacheBuilder) {
        this.regionRenderCacheBuilder = regionRenderCacheBuilder;
    }

    public RenderChunk getRenderChunk() {
        return this.renderChunk;
    }

    public void addFinishRunnable(Runnable runnable) {
        this.lock.lock();
        this.listFinishRunnables.add(runnable);
        if (this.finished) {
            runnable.run();
        }
        this.lock.unlock();
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

