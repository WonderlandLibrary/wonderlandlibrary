package Method.Client.utils.SeedViewer;


import Method.Client.utils.system.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.WorldInfo;

import java.util.ArrayList;

public class WorldLoader {

    public static ArrayList<IChunkGenerator> ChunkGenerator = new ArrayList<>();
    public static long seed = 44776655;
    public static boolean GenerateStructures = true;
    public static ArrayList<AwesomeWorld> combinationWorld = new ArrayList<>();
    public static WorldInfo worldInfo;

    public static void setup() {
        WorldSettings worldSettings = new WorldSettings(seed, GameType.SURVIVAL, GenerateStructures, false, WorldType.DEFAULT);
        worldInfo = new WorldInfo(worldSettings, "awesomeWorld");
        worldInfo.setMapFeaturesEnabled(true);
    }

    public static void PopulateCombinations(int x, int z, AwesomeWorld combinationWorld, int i) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  // West, East, North, South

        // Iterate over 2^4 = 16 combinations
        for (int j = 0; j < 4; j++) {
            // Use bitwise shift and AND operations to determine if this square should be included
            if (((i >> j) & 1) == 1) {
                int newX = x + directions[j][0];
                int newZ = z + directions[j][1];
                ChunkGenerator.get(i).populate(newX, newZ);
            }
        }
    }


    public static Chunk CreateChunknoPop(int x, int z) {
        combinationWorld.clear();
        ChunkGenerator.clear();
        for (int i = 0; i < 16; i++) {
            combinationWorld.add(new AwesomeWorld(worldInfo));
            if (Wrapper.mc.player.dimension == -1)
                ChunkGenerator.add(new ChunkGeneratorHell(combinationWorld.get(i), combinationWorld.get(i).getWorldInfo().isMapFeaturesEnabled(), seed));
            else
                ChunkGenerator.add(new net.minecraft.world.gen.ChunkGeneratorOverworld(combinationWorld.get(i), combinationWorld.get(i).getSeed(),
                        combinationWorld.get(i).getWorldInfo().isMapFeaturesEnabled(), combinationWorld.get(i).getWorldInfo().getGeneratorOptions()));
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                int nx = x - 1 + (j % 3);
                int nz = z - 1 + (j / 3);
                Chunk testchunk = ChunkGenerator.get(i).generateChunk(nx, nz);
                combinationWorld.get(i).getChunkProvider().loadedChunks.put(ChunkPos.asLong(nx, nz), testchunk);
                testchunk.onLoad();
            }
        }

        for (int i = 0; i < 16; i++) {
            PopulateCombinations(x, z, combinationWorld.get(i), i);
        }

        int bestworld = 0;
        int bestblocks = 0;
        for (int i = 0; i < 16; i++) {
            Chunk testchunk = combinationWorld.get(i).getChunk(x, z);
            testchunk.populate(ChunkGenerator.get(i));
            int blocks = SearchChunk(testchunk);
            if (blocks > bestblocks) {
                bestblocks = blocks;
                bestworld = i;
            }
        }

        return combinationWorld.get(bestworld).getChunk(x, z);
    }

    private static int SearchChunk(Chunk chunk) {
        try {
            int localbest = 0;
            for (int x = chunk.getPos().getXStart(); x <= chunk.getPos().getXEnd(); x++) {
                for (int z = chunk.getPos().getZStart(); z <= chunk.getPos().getZEnd(); z++) {
                    for (int y = 0; y < 255; y++) {
                        // if chunk block is the same as the world block
                        if (chunk.getBlockState(new BlockPos(x, y, z)).getBlock() == Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock())
                            localbest++;

                    }
                }
            }
            return localbest;
        } catch (
                Exception ignored) {
        }
        return 0;
    }

}



