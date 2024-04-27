/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.Unit
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.render;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.world.IChunk;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.MiniMapRegister;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002\u001a\u001bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011J\u0006\u0010\u0013\u001a\u00020\u0011J\u0006\u0010\u0014\u001a\u00020\u0015J\u0016\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011J\u000e\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u000eJ\u0006\u0010\u0019\u001a\u00020\u0015R*\u0010\u0003\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006`\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u000bj\b\u0012\u0004\u0012\u00020\u0005`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u000e0\u000bj\b\u0012\u0004\u0012\u00020\u000e`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/MiniMapRegister;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "chunkTextureMap", "Ljava/util/HashMap;", "Lnet/ccbluex/liquidbounce/utils/render/MiniMapRegister$ChunkLocation;", "Lnet/ccbluex/liquidbounce/utils/render/MiniMapRegister$MiniMapTexture;", "Lkotlin/collections/HashMap;", "deleteAllChunks", "Ljava/util/concurrent/atomic/AtomicBoolean;", "queuedChunkDeletions", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "queuedChunkUpdates", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IChunk;", "getChunkTextureAt", "x", "", "z", "getLoadedChunkCount", "unloadAllChunks", "", "unloadChunk", "updateChunk", "chunk", "updateChunks", "ChunkLocation", "MiniMapTexture", "LiKingSense"})
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
    public final void updateChunk(@NotNull IChunk chunk) {
        Intrinsics.checkParameterIsNotNull((Object)chunk, (String)"chunk");
        HashSet<IChunk> hashSet = queuedChunkUpdates;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (hashSet) {
            boolean bl3 = false;
            bl2 = queuedChunkUpdates.add(chunk);
        }
    }

    @Nullable
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
                    ((MiniMapTexture)it.getValue()).delete$LiKingSense();
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
                        miniMapTexture.delete$LiKingSense();
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\r\u0010\r\u001a\u00020\u000eH\u0000\u00a2\u0006\u0002\b\u000fJ\b\u0010\u0010\u001a\u00020\u000eH\u0004J\u000e\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u0013R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/MiniMapRegister$MiniMapTexture;", "", "()V", "deleted", "", "getDeleted", "()Z", "setDeleted", "(Z)V", "texture", "Lnet/minecraft/client/renderer/texture/DynamicTexture;", "getTexture", "()Lnet/minecraft/client/renderer/texture/DynamicTexture;", "delete", "", "delete$LiKingSense", "finalize", "updateChunkData", "chunk", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IChunk;", "LiKingSense"})
    public static final class MiniMapTexture {
        @NotNull
        private final DynamicTexture texture;
        private boolean deleted;

        @NotNull
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
         * Exception decompiling
         */
        public final void updateChunkData(@NotNull IChunk chunk) {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }

        public final void delete$LiKingSense() {
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

        public MiniMapTexture() {
            DynamicTexture dynamicTexture;
            MiniMapTexture miniMapTexture = this;
            DynamicTexture dynamicTexture2 = dynamicTexture;
            dynamicTexture.texture = (DynamicTexture)0;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/MiniMapRegister$ChunkLocation;", "", "x", "", "z", "(II)V", "getX", "()I", "getZ", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "LiKingSense"})
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

        @NotNull
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

        @NotNull
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

