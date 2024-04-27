/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.render;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.IChunk;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.MiniMapRegister;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.jetbrains.annotations.Nullable;

public final class MiniMapRegister
extends MinecraftInstance {
    private static final HashMap<ChunkLocation, MiniMapTexture> chunkTextureMap;
    private static final HashSet<IChunk> queuedChunkUpdates;
    private static final HashSet<ChunkLocation> queuedChunkDeletions;
    private static final AtomicBoolean deleteAllChunks;
    public static final MiniMapRegister INSTANCE;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void updateChunk(IChunk chunk) {
        HashSet<IChunk> hashSet = queuedChunkUpdates;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (hashSet) {
            boolean bl3 = false;
            bl2 = queuedChunkUpdates.add(chunk);
        }
    }

    public final MiniMapTexture getChunkTextureAt(int x, int z) {
        return chunkTextureMap.get(new ChunkLocation(x, z));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void updateChunks() {
        HashSet<IChunk> hashSet = queuedChunkUpdates;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (hashSet) {
            boolean $i$f$forEach;
            Object $this$forEach$iv;
            Object object;
            boolean bl3 = false;
            if (deleteAllChunks.get()) {
                boolean bl4;
                HashSet<ChunkLocation> hashSet2 = queuedChunkDeletions;
                boolean bl5 = false;
                boolean bl6 = false;
                synchronized (hashSet2) {
                    bl4 = false;
                    queuedChunkDeletions.clear();
                    object = Unit.INSTANCE;
                }
                queuedChunkUpdates.clear();
                $this$forEach$iv = chunkTextureMap;
                $i$f$forEach = false;
                object = $this$forEach$iv;
                bl4 = false;
                Iterator iterator = object.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry element$iv;
                    Map.Entry it = element$iv = iterator.next();
                    boolean bl7 = false;
                    ((MiniMapTexture)it.getValue()).delete$LiquidSense();
                }
                chunkTextureMap.clear();
                deleteAllChunks.set(false);
            } else {
                $this$forEach$iv = queuedChunkDeletions;
                $i$f$forEach = false;
                boolean bl8 = false;
                synchronized ($this$forEach$iv) {
                    boolean bl9 = false;
                    Iterable $this$forEach$iv2 = queuedChunkDeletions;
                    boolean $i$f$forEach2 = false;
                    for (Object element$iv : $this$forEach$iv2) {
                        ChunkLocation it = (ChunkLocation)element$iv;
                        boolean bl10 = false;
                        MiniMapTexture miniMapTexture = chunkTextureMap.remove(it);
                        if (miniMapTexture == null) continue;
                        miniMapTexture.delete$LiquidSense();
                    }
                    queuedChunkDeletions.clear();
                    object = Unit.INSTANCE;
                }
            }
            $this$forEach$iv = queuedChunkUpdates;
            $i$f$forEach = false;
            object = $this$forEach$iv.iterator();
            while (object.hasNext()) {
                Object element$iv = object.next();
                IChunk it = (IChunk)element$iv;
                boolean bl11 = false;
                chunkTextureMap.computeIfAbsent(new ChunkLocation(it.getX(), it.getZ()), updateChunks.1.4.1.INSTANCE).updateChunkData(it);
            }
            queuedChunkUpdates.clear();
            Unit unit = Unit.INSTANCE;
        }
    }

    public final int getLoadedChunkCount() {
        return chunkTextureMap.size();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void unloadChunk(int x, int z) {
        HashSet<ChunkLocation> hashSet = queuedChunkDeletions;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (hashSet) {
            boolean bl3 = false;
            bl2 = queuedChunkDeletions.add(new ChunkLocation(x, z));
        }
    }

    public final void unloadAllChunks() {
        deleteAllChunks.set(true);
    }

    private MiniMapRegister() {
    }

    static {
        MiniMapRegister miniMapRegister;
        INSTANCE = miniMapRegister = new MiniMapRegister();
        chunkTextureMap = new HashMap();
        queuedChunkUpdates = new HashSet(256);
        queuedChunkDeletions = new HashSet(256);
        deleteAllChunks = new AtomicBoolean(false);
    }

    public static final class MiniMapTexture {
        private final DynamicTexture texture = new DynamicTexture(16, 16);
        private boolean deleted;

        public final DynamicTexture getTexture() {
            return this.texture;
        }

        public final boolean getDeleted() {
            return this.deleted;
        }

        public final void setDeleted(boolean bl) {
            this.deleted = bl;
        }

        /*
         * WARNING - void declaration
         */
        public final void updateChunkData(IChunk chunk) {
            int[] rgbValues = this.texture.func_110565_c();
            int n = 0;
            int n2 = 15;
            while (n <= n2) {
                void x;
                int n3 = 0;
                int n4 = 15;
                while (n3 <= n4) {
                    void z;
                    WBlockPos bp = new WBlockPos((int)x, chunk.getHeightValue((int)x, (int)z) - 1, (int)z);
                    IIBlockState blockState = chunk.getBlockState(bp);
                    int n5 = rgbValues.length - (z * 16 + x + true);
                    IBlock iBlock = blockState.getBlock();
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    rgbValues[n5] = iBlock.getMapColor(blockState, iWorldClient, bp) | 0xFF000000;
                    ++z;
                }
                ++x;
            }
            this.texture.func_110564_a();
        }

        public final void delete$LiquidSense() {
            if (!this.deleted) {
                this.texture.func_147631_c();
                this.deleted = true;
            }
        }

        protected final void finalize() {
            if (!this.deleted) {
                this.texture.func_147631_c();
            }
        }
    }

    public static final class ChunkLocation {
        private final int x;
        private final int z;

        public final int getX() {
            return this.x;
        }

        public final int getZ() {
            return this.z;
        }

        public ChunkLocation(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public final int component1() {
            return this.x;
        }

        public final int component2() {
            return this.z;
        }

        public final ChunkLocation copy(int x, int z) {
            return new ChunkLocation(x, z);
        }

        public static /* synthetic */ ChunkLocation copy$default(ChunkLocation chunkLocation, int n, int n2, int n3, Object object) {
            if ((n3 & 1) != 0) {
                n = chunkLocation.x;
            }
            if ((n3 & 2) != 0) {
                n2 = chunkLocation.z;
            }
            return chunkLocation.copy(n, n2);
        }

        public String toString() {
            return "ChunkLocation(x=" + this.x + ", z=" + this.z + ")";
        }

        public int hashCode() {
            return Integer.hashCode(this.x) * 31 + Integer.hashCode(this.z);
        }

        public boolean equals(@Nullable Object object) {
            block3: {
                block2: {
                    if (this == object) break block2;
                    if (!(object instanceof ChunkLocation)) break block3;
                    ChunkLocation chunkLocation = (ChunkLocation)object;
                    if (this.x != chunkLocation.x || this.z != chunkLocation.z) break block3;
                }
                return true;
            }
            return false;
        }
    }
}

