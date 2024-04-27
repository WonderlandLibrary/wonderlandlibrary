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
    private static final HashSet queuedChunkDeletions;
    private static final HashSet queuedChunkUpdates;
    private static final AtomicBoolean deleteAllChunks;
    public static final MiniMapRegister INSTANCE;
    private static final HashMap chunkTextureMap;

    public final void unloadChunk(int n, int n2) {
        HashSet hashSet = queuedChunkDeletions;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (hashSet) {
            boolean bl3 = false;
            bl2 = queuedChunkDeletions.add(new ChunkLocation(n, n2));
        }
    }

    public final MiniMapTexture getChunkTextureAt(int n, int n2) {
        return (MiniMapTexture)chunkTextureMap.get(new ChunkLocation(n, n2));
    }

    public final void unloadAllChunks() {
        deleteAllChunks.set(true);
    }

    public final void updateChunks() {
        HashSet hashSet = queuedChunkUpdates;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (hashSet) {
            Object object;
            Object object2;
            Object object3;
            boolean bl3;
            boolean bl4;
            Object object4;
            boolean bl5 = false;
            if (deleteAllChunks.get()) {
                object4 = queuedChunkDeletions;
                bl4 = false;
                boolean bl6 = false;
                synchronized (object4) {
                    bl3 = false;
                    queuedChunkDeletions.clear();
                    object3 = Unit.INSTANCE;
                }
                queuedChunkUpdates.clear();
                object4 = chunkTextureMap;
                bl4 = false;
                object3 = object4;
                bl3 = false;
                for (Map.Entry entry : object3.entrySet()) {
                    object2 = entry;
                    boolean bl7 = false;
                    ((MiniMapTexture)object2.getValue()).delete$AtField();
                }
                chunkTextureMap.clear();
                deleteAllChunks.set(false);
            } else {
                object4 = queuedChunkDeletions;
                bl4 = false;
                boolean bl8 = false;
                synchronized (object4) {
                    bl3 = false;
                    object = queuedChunkDeletions;
                    boolean bl9 = false;
                    object2 = object.iterator();
                    while (object2.hasNext()) {
                        Object t = object2.next();
                        ChunkLocation chunkLocation = (ChunkLocation)t;
                        boolean bl10 = false;
                        MiniMapTexture miniMapTexture = (MiniMapTexture)chunkTextureMap.remove(chunkLocation);
                        if (miniMapTexture == null) continue;
                        miniMapTexture.delete$AtField();
                    }
                    queuedChunkDeletions.clear();
                    object3 = Unit.INSTANCE;
                }
            }
            object4 = queuedChunkUpdates;
            bl4 = false;
            object3 = object4.iterator();
            while (object3.hasNext()) {
                Object e = object3.next();
                object = (IChunk)e;
                boolean bl11 = false;
                ((MiniMapTexture)chunkTextureMap.computeIfAbsent(new ChunkLocation(object.getX(), object.getZ()), updateChunks.1.4.1.INSTANCE)).updateChunkData((IChunk)object);
            }
            queuedChunkUpdates.clear();
            Unit unit = Unit.INSTANCE;
        }
    }

    private MiniMapRegister() {
    }

    public final void updateChunk(IChunk iChunk) {
        HashSet hashSet = queuedChunkUpdates;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (hashSet) {
            boolean bl3 = false;
            bl2 = queuedChunkUpdates.add(iChunk);
        }
    }

    public final int getLoadedChunkCount() {
        return chunkTextureMap.size();
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

        public final void setDeleted(boolean bl) {
            this.deleted = bl;
        }

        public final void delete$AtField() {
            if (!this.deleted) {
                this.texture.func_147631_c();
                this.deleted = true;
            }
        }

        public final boolean getDeleted() {
            return this.deleted;
        }

        public final void updateChunkData(IChunk iChunk) {
            int[] nArray = this.texture.func_110565_c();
            int n = 15;
            for (int i = 0; i <= n; ++i) {
                int n2 = 15;
                for (int j = 0; j <= n2; ++j) {
                    WBlockPos wBlockPos = new WBlockPos(i, iChunk.getHeightValue(i, j) - 1, j);
                    IIBlockState iIBlockState = iChunk.getBlockState(wBlockPos);
                    int n3 = nArray.length - (j * 16 + i + 1);
                    IBlock iBlock = iIBlockState.getBlock();
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    nArray[n3] = iBlock.getMapColor(iIBlockState, iWorldClient, wBlockPos) | 0xFF000000;
                }
            }
            this.texture.func_110564_a();
        }

        protected final void finalize() {
            if (!this.deleted) {
                this.texture.func_147631_c();
            }
        }

        public final DynamicTexture getTexture() {
            return this.texture;
        }
    }

    public static final class ChunkLocation {
        private final int x;
        private final int z;

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

        public final int component1() {
            return this.x;
        }

        public String toString() {
            return "ChunkLocation(x=" + this.x + ", z=" + this.z + ")";
        }

        public final int component2() {
            return this.z;
        }

        public int hashCode() {
            return Integer.hashCode(this.x) * 31 + Integer.hashCode(this.z);
        }

        public static ChunkLocation copy$default(ChunkLocation chunkLocation, int n, int n2, int n3, Object object) {
            if ((n3 & 1) != 0) {
                n = chunkLocation.x;
            }
            if ((n3 & 2) != 0) {
                n2 = chunkLocation.z;
            }
            return chunkLocation.copy(n, n2);
        }

        public final int getZ() {
            return this.z;
        }

        public final ChunkLocation copy(int n, int n2) {
            return new ChunkLocation(n, n2);
        }

        public ChunkLocation(int n, int n2) {
            this.x = n;
            this.z = n2;
        }

        public final int getX() {
            return this.x;
        }
    }
}

