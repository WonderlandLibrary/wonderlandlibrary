package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.Executer;
import com.google.common.eventbus.Subscribe;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.ChunkEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.RenderUtils.RenderBlock;
import static Method.Client.utils.visual.RenderUtils.Standardbb;

public class Search extends Module {

    Setting OverlayColor = setmgr.add(new Setting("OverlayColor", this, 0, 1, 1, .42));
    Setting Mode = setmgr.add(new Setting("block Mode", this, "Outline", BlockEspOptions()));
    Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1.8, 0, 3, false));
    Setting Gui = setmgr.add(new Setting("Gui", this, Main.Search));

    public Search() {
        super("Search", Keyboard.KEY_NONE, Category.RENDER, "Search");
    }

    @Override
    public void setup() {
        Executer.init();
    }

    private final Long2ObjectArrayMap<MyChunk> chunks = new Long2ObjectArrayMap<>();

    private final Pool<MyBlock> blockPool = new Pool<>(MyBlock::new);

    private final LongList toRemove = new LongArrayList();

    private final BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

    @Override
    public void onEnable() {
        Executer.init();
        searchViewDistance();
    }

    @Override
    public void onDisable() {
        for (MyChunk chunk : chunks.values()) chunk.dispose();
        chunks.clear();
    }

    private void searchViewDistance() {
        int viewDist = mc.gameSettings.renderDistanceChunks;
        for (int x = mc.player.chunkCoordX - viewDist; x <= mc.player.chunkCoordX + viewDist; x++) {
            for (int z = mc.player.chunkCoordZ - viewDist; z <= mc.player.chunkCoordZ + viewDist; z++) {
                if (mc.world.isChunkGeneratedAt(x, z))
                    searchChunk(mc.world.getChunk(x, z));
            }
        }
    }


    @Subscribe
    public void ChunkeventLOAD(ChunkEvent.Load event) {
        searchChunk(event.getChunk());
    }

    private void searchChunk(Chunk chunk) {
        Executer.execute(() -> {
            MyChunk myChunk = new MyChunk(chunk.getPos().x, chunk.getPos().z);
            for (int x = chunk.getPos().getXStart(); x <= chunk.getPos().getXEnd(); x++) {
                for (int z = chunk.getPos().getZStart(); z <= chunk.getPos().getZEnd(); z++) {
                    for (int y = 0; y < 256; y++) {
                        blockPos.setPos(x, y, z);
                        if (isVisible(chunk.getBlockState(blockPos).getBlock()))
                            myChunk.add(blockPos, false);
                    }
                }
            }

            synchronized (chunks) {
                if (myChunk.blocks.size() > 0)
                    chunks.put(ChunkPos.asLong(chunk.getPos().x, chunk.getPos().z), myChunk);
            }

        });

    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof SPacketBlockChange) {
            final SPacketBlockChange packet2 = (SPacketBlockChange) packet;
            onBlockUpdate(packet2.getBlockPosition(), packet2.blockState);
        }
        if (packet instanceof SPacketBlockAction) {
            final SPacketBlockAction packet2 = (SPacketBlockAction) packet;
            onBlockUpdate(packet2.getBlockPosition(), packet2.getBlockType().getDefaultState());
        }
        if (packet instanceof SPacketMultiBlockChange) {
            final SPacketMultiBlockChange packet2 = (SPacketMultiBlockChange) packet;
            for (SPacketMultiBlockChange.BlockUpdateData changedBlock : packet2.getChangedBlocks()) {
                onBlockUpdate(changedBlock.getPos(), changedBlock.getBlockState());
            }
        }
        return true;
    }

    public void onBlockUpdate(BlockPos blockPos, IBlockState blockState) {
        Executer.execute(() -> {
            int chunkX = blockPos.getX() >> 4;
            int chunkZ = blockPos.getZ() >> 4;
            long key = ChunkPos.asLong(chunkX, chunkZ);
            synchronized (chunks) {
                if (isVisible(blockState.getBlock())) {
                    chunks.computeIfAbsent(key, aLong -> new MyChunk(chunkX, chunkZ)).add(blockPos, true);
                } else {
                    MyChunk chunk = chunks.get(key);
                    if (chunk != null) chunk.remove(blockPos);
                }
            }
        });
    }


    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        synchronized (chunks) {
            toRemove.clear();

            for (long key : chunks.keySet()) {
                MyChunk chunk = chunks.get(key);
                if (chunk.shouldBeDeleted()) toRemove.add(key);
                else chunk.render();
            }

            for (long key : toRemove) {
                chunks.remove(key);
            }
        }
    }

    private boolean isVisible(Block block) {
        for (SubGui.SelectedThing selectedThing : Main.Search.listGui.list) {
            if (selectedThing.isSelected)
                if (selectedThing.name.equalsIgnoreCase(getName(block))) return true;
        }

        return false;
    }

    public static String getName(Block block) {
        return "" + Block.REGISTRY.getNameForObject(block);
    }


    private class MyChunk {
        private final int x, z;
        private final List<MyBlock> blocks = new ArrayList<>();

        public MyChunk(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public void add(BlockPos blockPos, boolean checkForDuplicates) {
            if (checkForDuplicates) {
                for (MyBlock block : blocks) {
                    if (block.equals(blockPos)) return;
                }
            }

            MyBlock block = blockPool.get();
            block.set(blockPos);
            blocks.add(block);
        }

        public void remove(BlockPos blockPos) {
            for (int i = 0; i < blocks.size(); i++) {
                MyBlock block = blocks.get(i);

                if (block.equals(blockPos)) {
                    blocks.remove(i);
                    return;
                }
            }

        }


        public boolean shouldBeDeleted() {
            int viewDist = mc.gameSettings.renderDistanceChunks + 1;
            return x > mc.player.chunkCoordX + viewDist || x < mc.player.chunkCoordX - viewDist || z > mc.player.chunkCoordZ + viewDist || z < mc.player.chunkCoordZ - viewDist;
        }


        public void render() {
            for (MyBlock block : blocks)
                block.render();
        }

        public void dispose() {
            for (MyBlock block : blocks) blockPool.free(block);
            blocks.clear();
        }
    }

    private class MyBlock {
        private int x, y, z;

        public void set(BlockPos blockPos) {
            x = blockPos.getX();
            y = blockPos.getY();
            z = blockPos.getZ();
        }

        public void render() {
            RenderBlock(Mode.getValString(), Standardbb(new BlockPos(x, y, z)), OverlayColor.getcolor(), LineWidth.getValDouble());
        }

        public boolean equals(BlockPos blockPos) {
            return x == blockPos.getX() && y == blockPos.getY() && z == blockPos.getZ();
        }
    }

    public static class Pool<T> {
        private final List<T> items = new ArrayList<>();
        private final Producer<T> producer;

        public Pool(Producer<T> producer) {
            this.producer = producer;
        }

        public T get() {
            if (items.size() > 0) return items.remove(items.size() - 1);
            return producer.create();
        }

        public void free(T obj) {
            items.add(obj);
        }
    }

    public interface Producer<T> {
        T create();
    }

}




