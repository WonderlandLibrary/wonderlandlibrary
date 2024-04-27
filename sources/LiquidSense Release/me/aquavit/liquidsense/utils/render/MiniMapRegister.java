package me.aquavit.liquidsense.utils.render;

import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MiniMapRegister extends MinecraftInstance {
    private static HashMap<ChunkLocation, MiniMapTexture> chunkTextureMap = new HashMap();
    private static final HashSet<Chunk> queuedChunkUpdates = new HashSet<>(256);
    private static final HashSet<ChunkLocation> queuedChunkDeletions = new HashSet<ChunkLocation>(256);
    private static final AtomicBoolean deleteAllChunks = new AtomicBoolean(false);
    public static final MiniMapRegister INSTANCE = new MiniMapRegister();

    public void updateChunk(Chunk chunk) {
        synchronized (queuedChunkUpdates) {
            queuedChunkUpdates.add(chunk);
        }
    }

    public static MiniMapTexture getChunkTextureAt(int x, int z) {
        return chunkTextureMap.get(new ChunkLocation(x, z));
    }

    public static void updateChunks() {
        synchronized (queuedChunkUpdates) {
            if (deleteAllChunks.get()) {
                synchronized (queuedChunkDeletions) {
                    queuedChunkDeletions.clear();
                }

                queuedChunkUpdates.clear();

                chunkTextureMap.forEach((key, value) -> value.delete());

                chunkTextureMap.clear();
                deleteAllChunks.set(false);
            } else {
                synchronized (queuedChunkDeletions) {

                    for (ChunkLocation it : queuedChunkDeletions) {
                        MiniMapTexture miniMapTexture = chunkTextureMap.remove(it);
                        if (miniMapTexture != null) {
                            miniMapTexture.delete();
                        }
                    }

                    queuedChunkDeletions.clear();
                }
            }

            for (Chunk it : queuedChunkUpdates) {
                chunkTextureMap.computeIfAbsent(new ChunkLocation(it.xPosition, it.zPosition), chunkLocation -> new MiniMapTexture()).updateChunkData(it);
            }

            queuedChunkUpdates.clear();
        }
    }

    public int getLoadedChunkCount() {
        return chunkTextureMap.size();
    }

    public void unloadChunk(int x, int z) {
        synchronized (queuedChunkDeletions) {
            queuedChunkDeletions.add(new MiniMapRegister.ChunkLocation(x, z));
        }
    }

    public void unloadAllChunks() {
        deleteAllChunks.set(true);
    }

    private MiniMapRegister() {
    }

    public static final class MiniMapTexture {
        private final DynamicTexture texture = new DynamicTexture(16, 16);
        private boolean deleted;

        public DynamicTexture getTexture() {
            return this.texture;
        }

        public boolean getDeleted() {
            return this.deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public void updateChunkData(Chunk chunk) {
            int[] rgbValues = this.texture.getTextureData();
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos bp = new BlockPos(x, chunk.getHeightValue(x, z) - 1, z);
                    IBlockState blockState = chunk.getBlockState(bp);
                    rgbValues[rgbValues.length - (z * 16 + x + 1)] = blockState.getBlock().getMapColor(blockState).colorValue | (0xFF << 24);
                }
            }
            this.texture.updateDynamicTexture();
        }

        public void delete() {
            if (!this.deleted) {
                this.texture.deleteGlTexture();
                this.deleted = true;
            }

        }

        protected void finalize() {
            if (!this.deleted) {
                this.texture.deleteGlTexture();
            }

        }
    }

    public static final class ChunkLocation {
        private final int x;
        private final int z;

        public int getX() {
            return this.x;
        }

        public int getZ() {
            return this.z;
        }

        public ChunkLocation(int x, int z) {
            this.x = x;
            this.z = z;
        }


        public int hashCode() {
            return Integer.hashCode(this.x) * 31 + Integer.hashCode(this.z);
        }

        public boolean equals(Object object) {
            if (this != object) {
                if (object instanceof ChunkLocation) {
                    ChunkLocation chunkLocation = (ChunkLocation) object;
                    return this.x == chunkLocation.x && this.z == chunkLocation.z;
                }

                return false;
            } else {
                return true;
            }
        }
    }
}
