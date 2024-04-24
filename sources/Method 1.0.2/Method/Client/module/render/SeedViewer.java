package Method.Client.module.render;


import Method.Client.managers.CommandManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.SeedViewer.ChunkData;
import Method.Client.utils.SeedViewer.WorldLoader;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.visual.ChatUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.RenderUtils.RenderBlock;
import static Method.Client.utils.visual.RenderUtils.Standardbb;

public class SeedViewer extends Module {

    Setting OverlayColor = setmgr.add(new Setting("OverlayColor", this, 0, 1, 1, 1));
    Setting Mode = setmgr.add(new Setting("Hole Mode", this, "Outline", BlockEspOptions()));
    Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1, 0, 3, false));
    Setting BlockLimit = setmgr.add(new Setting("BlockLimit", this, 200, 0, 5000, false));
    Setting Distance = setmgr.add(new Setting("Distance", this, 6, 0, 15, true));
    Setting FalsePositive = setmgr.add(new Setting("FalsePositive", this, true));
    Setting Nochecks = setmgr.add(new Setting("Nochecks", this, false));

    @Override
    public void setup() {
        executor = Executors.newSingleThreadExecutor();
    }

    private static ExecutorService executor;
    public int currentdis = 0;

    public SeedViewer() {
        super("SeedViewer", Keyboard.KEY_NONE, Category.RENDER, "SeedViewer");
    }

    private ArrayList<ChunkData> chunks = new ArrayList<>();
    public int tobesearchx;
    public int tobesearchz;
    private final TimerUtils timer = new TimerUtils();

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (timer.isDelay(1200)) {
            if (mc.player.dimension != currentdis) {
                ChatUtils.warning("You must re-enable on dimension change!");
                this.toggle();
            }
            searchViewDistance();
            timer.setLastMS();
            executor.execute(() -> {
                        Chunk chunk = WorldLoader.CreateChunknoPop(tobesearchx, tobesearchz);
                        FindDifferences(chunk);
                    }
            );
        }
    }

    private void FindDifferences(Chunk chunk) {
        ChunkData chunkdata = new ChunkData(new ChunkPos(chunk.x, chunk.z), true);
        for (int x = chunk.getPos().getXStart(); x <= chunk.getPos().getXEnd(); x++) {
            for (int z = chunk.getPos().getZStart(); z <= chunk.getPos().getZEnd(); z++) {
                for (int y = 0; y < 255; y++) {
                    if (BlockFast(new BlockPos(x, y, z), chunk.getBlockState(new BlockPos(x, y, z)).getBlock(), Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock())) {
                        chunkdata.blocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        chunks.add(chunkdata);
    }

    @Override
    public void onEnable() {
        if (mc.isSingleplayer()) {
            ChatUtils.warning("Only for multiplayer");
            this.toggle();
            return;
        }
        if (WorldLoader.seed == 44776655) {
            ChatUtils.message("Set Seed using the" + TextFormatting.GOLD + CommandManager.cmdPrefix + "WorldSeed" + TextFormatting.RESET + " Command");
            this.toggle();
            return;
        }
        currentdis = mc.player.dimension;
        executor = Executors.newSingleThreadExecutor();
        WorldLoader.setup();
        ChatUtils.warning("Still Working on this.");
        chunks = new ArrayList<>();
        searchViewDistance();
    }

    private void searchViewDistance() {
        executor.execute(() -> {
            for (int x = mc.player.chunkCoordX - (int) Distance.getValDouble(); x <= mc.player.chunkCoordX + (int) Distance.getValDouble(); x++) {
                for (int z = mc.player.chunkCoordZ - (int) Distance.getValDouble(); z <= mc.player.chunkCoordZ + (int) Distance.getValDouble(); z++) {
                    if (!chunkSearched(x, z))
                        if (mc.world.isChunkGeneratedAt(x, z)) {
                            tobesearchx = x;
                            tobesearchz = z;
                            return;
                        }
                }
            }
        });
    }

    private boolean chunkSearched(int x, int z) {
        for (ChunkData chunk : chunks) {
            if (chunk.chunkPos.x == x && chunk.chunkPos.z == z)
                if (chunk.Searched) return true;
        }
        return false;
    }

    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        try {
            int blocklimit = 0;
            ArrayList<ChunkData> Remove = new ArrayList<>();
            for (ChunkData chunk : chunks) {
                if (chunk.Searched) {
                    if (mc.player.getDistance(chunk.chunkPos.getXEnd(), 100, chunk.chunkPos.getZEnd()) > 2000)
                        Remove.add(chunk);
                    for (BlockPos block : chunk.blocks) {
                        if (blocklimit > BlockLimit.getValDouble())
                            break;
                        RenderBlock(Mode.getValString(), Standardbb(new BlockPos(block.x, block.y, block.z)), OverlayColor.getcolor(), LineWidth.getValDouble());
                        blocklimit++;
                    }

                }
            }
            chunks.removeAll(Remove);
        } catch (Exception ignored) {
        }
    }


    public boolean BlockFast(BlockPos blockPos, Block FakeChunk, Block RealChunk) {
        if (!Nochecks.getValBoolean()) {
            if (RealChunk instanceof BlockSnow)
                return false;
            if (FakeChunk instanceof BlockSnow)
                return false;
            if (RealChunk instanceof BlockVine)
                return false;
            if (FakeChunk instanceof BlockVine)
                return false;

            if (RealChunk instanceof BlockFalling)
                return false;
            if (FakeChunk instanceof BlockFalling)
                return false;


            if (RealChunk instanceof BlockLiquid)
                return false;
            if (FakeChunk instanceof BlockLiquid)
                return false;
            if (mc.world.getBlockState(blockPos.down()).getBlock() instanceof BlockLiquid)
                return false;
            if (mc.world.getBlockState(blockPos.down(2)).getBlock() instanceof BlockLiquid)
                return false;


            if (FakeChunk instanceof BlockGrass)
                if (Treeroots(blockPos))
                    return false;
            if (RealChunk instanceof BlockLog || RealChunk instanceof BlockLeaves)
                return false;
            if (FakeChunk instanceof BlockLog || FakeChunk instanceof BlockLeaves)
                return false;


            if (RealChunk instanceof BlockGrass && FakeChunk instanceof BlockDirt)
                return false;
            if (RealChunk instanceof BlockDirt && FakeChunk instanceof BlockGrass)
                return false;


            if (RealChunk instanceof BlockBush)
                return false;
            if (RealChunk instanceof BlockReed)
                return false;
            if (FakeChunk instanceof BlockBush)
                return false;

            if (RealChunk instanceof BlockCactus || FakeChunk instanceof BlockCactus)
                return false;

            if (FakeChunk instanceof BlockClay || RealChunk instanceof BlockClay)
                return false;

            if (RealChunk instanceof BlockObsidian || RealChunk.equals(Blocks.COBBLESTONE))
                if (Lavamix(blockPos))
                    return false;

            if (!FalsePositive.getValBoolean()) {
                if (FakeChunk instanceof BlockOre && (RealChunk instanceof BlockStone || RealChunk instanceof BlockMagma || RealChunk instanceof BlockNetherrack || RealChunk instanceof BlockDirt))
                    return false;
                if (RealChunk instanceof BlockOre && (FakeChunk instanceof BlockStone || FakeChunk instanceof BlockMagma || FakeChunk instanceof BlockNetherrack || FakeChunk instanceof BlockDirt))
                    return false;

                // Redstone ore is not in ore list???????
                if (FakeChunk instanceof BlockRedstoneOre && (RealChunk instanceof BlockStone || RealChunk instanceof BlockDirt))
                    return false;
                if (RealChunk instanceof BlockRedstoneOre && (FakeChunk instanceof BlockStone || FakeChunk instanceof BlockDirt))
                    return false;

                if (FakeChunk instanceof BlockGlowstone && (RealChunk instanceof BlockAir))
                    return false;
                if (RealChunk instanceof BlockGlowstone && (FakeChunk instanceof BlockAir))
                    return false;


                if (FakeChunk instanceof BlockMagma && RealChunk instanceof BlockNetherrack)
                    return false;
                if (RealChunk instanceof BlockMagma && FakeChunk instanceof BlockNetherrack)
                    return false;
                if (RealChunk instanceof BlockFire || FakeChunk instanceof BlockFire)
                    return false;
                if (RealChunk instanceof BlockOre && FakeChunk instanceof BlockOre)
                    return false;
                if (RealChunk.getLocalizedName().equals(Blocks.MONSTER_EGG.getLocalizedName()) && FakeChunk instanceof BlockStone)
                    return false;
                if ((FakeChunk instanceof BlockStone && RealChunk instanceof BlockDirt) || (FakeChunk instanceof BlockDirt && RealChunk instanceof BlockStone))
                    return false;
                if (!(FakeChunk instanceof BlockAir) && RealChunk instanceof BlockAir)
                    if (!mc.world.getBlockState(blockPos).getBlock().getLocalizedName().equals(RealChunk.getLocalizedName()))
                        return false;
            }
        }
        if (!FakeChunk.getLocalizedName().equals(RealChunk.getLocalizedName())) {
            return true;
        }
        return false;
    }

    public boolean Treeroots(BlockPos b) {
        if (mc.world.getBlockState(b.up()).getBlock() instanceof BlockLog) {
            return true;
        }
        return false;
    }

    public boolean Lavamix(BlockPos b) {
        if (mc.world.getBlockState(b.up()).getBlock() instanceof BlockLiquid) {
            return true;
        }
        if (mc.world.getBlockState(b.down()).getBlock() instanceof BlockLiquid) {
            return true;
        }
        if (mc.world.getBlockState(b.add(1, 0, 0)).getBlock() instanceof BlockLiquid) {
            return true;
        }
        if (mc.world.getBlockState(b.add(0, 0, 1)).getBlock() instanceof BlockLiquid) {
            return true;
        }
        if (mc.world.getBlockState(b.add(-1, 0, 0)).getBlock() instanceof BlockLiquid) {
            return true;
        }
        if (mc.world.getBlockState(b.add(0, 0, -1)).getBlock() instanceof BlockLiquid) {
            return true;
        }
        return false;
    }
}
