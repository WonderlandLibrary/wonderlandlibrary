package Method.Client.utils.SeedViewer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class ChunkData {
    public boolean Searched;

    public List<BlockPos> getBlocks() {
        return blocks;
    }

    public final List<BlockPos> blocks = new ArrayList<>();
    public ChunkPos chunkPos;

    public ChunkData(ChunkPos chunkPos, boolean Searched) {
        this.chunkPos = chunkPos;
        this.Searched = Searched;
    }
}
