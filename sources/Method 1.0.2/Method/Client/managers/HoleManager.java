package Method.Client.managers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HoleManager {
    public final ArrayList<Hole> holes = new ArrayList<>();
    ArrayList<Hole> temp = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void detectHoles(int radius) {
        BlockPos playerPos = new BlockPos(mc.player.getPosition());
        temp.clear();
        // Clear the existing holes
        // Start the search in a new thread
        executor.submit(() -> {
            mc.world.getLoadedEntityList().stream()
                    .filter(entity -> entity instanceof EntityPlayer)
                    .map(entity -> (EntityPlayer) entity)
                    .filter(this::isInHole)
                    .filter(player -> !player.equals(mc.player))
                    .forEach(player -> {
                        BlockPos pos = new BlockPos(player.getPosition());
                        temp.add(new Hole(pos, Hole.HoleType.ENCASED, false));
                    });

            searchSurroundingBlocks(playerPos, radius);
        });
        holes.clear();
        holes.addAll(temp);
    }

    private void searchSurroundingBlocks(BlockPos center, int radius) {
        BlockPos.getAllInBox(center.add(-radius, -6, -radius), center.add(radius, 6, radius))
                .forEach(this::checkBlock);
    }

    private void checkBlock(BlockPos pos) {
        if (pos.getY() == 0 && mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            temp.add(new Hole(pos, Hole.HoleType.VOID, false));
        } else if (isValidHole(pos)) {
            addHoleIfValid(pos);
        }
    }

    private boolean isValidHole(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.up(2)).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.down()).getBlock() != Blocks.AIR;
    }

    private boolean isInHole(EntityPlayer player) {
        BlockPos pos = new BlockPos(player.getPosition());
        return isEncasedBlock(mc.world.getBlockState(pos).getBlock());
    }

    private void addHoleIfValid(BlockPos pos) {
        BlockPos[] sides = new BlockPos[]{pos.north(), pos.south(), pos.east(), pos.west()};
        boolean allBedrock = true;

        for (BlockPos side : sides) {
            if (!isEncasedBlock(mc.world.getBlockState(side).getBlock())) {
                return;
            }
            if (mc.world.getBlockState(side).getBlock() == Blocks.OBSIDIAN) {
                allBedrock = false;
            }
        }

        boolean isTall = mc.world.getBlockState(pos.down()).getBlock() == Blocks.AIR;
        if (allBedrock) {
            temp.add(new Hole(pos, Hole.HoleType.BEDROCK, isTall));
        } else {
            temp.add(new Hole(pos, Hole.HoleType.OBSIDIAN, isTall));
        }
    }

    private boolean isEncasedBlock(Block block) {
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL;
    }
}

