package Method.Client.utils.SeedViewer;

import Method.Client.utils.system.Wrapper;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;

public class AwesomeWorld extends World {

    public ChunkProviderClient getChunkProvider()
    {
        return (ChunkProviderClient)super.getChunkProvider();
    }

    private ChunkProviderClient clientChunkProvider;
    public final Long2ObjectMap<Chunk> loadedChunks = new Long2ObjectOpenHashMap<>(8192);

    protected AwesomeWorld(WorldInfo worldInfo) {
        super(new SaveHandlerMP(), worldInfo, net.minecraftforge.common.DimensionManager.createProviderFor(0), Wrapper.mc.profiler, true);

        this.getWorldInfo().setDifficulty(EnumDifficulty.PEACEFUL);
        this.provider.setWorld(this);
        this.setSpawnPoint(new BlockPos(8, 64, 8));
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        this.initCapabilities();
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        this.clientChunkProvider = new ChunkProviderClient(this);
        return this.clientChunkProvider;
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty)
    {
        return allowEmpty || !this.getChunkProvider().provideChunk(x, z).isEmpty();
    }

}
