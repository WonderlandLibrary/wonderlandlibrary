package Method.Client.module.player;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.BlockUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Wrapper;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.RenderUtils.RenderBlock;
import static Method.Client.utils.visual.RenderUtils.Standardbb;

public class Nuker extends Module {

    public Setting mode = setmgr.add(new Setting("Mode", this, "All", "ID", "All"));
    public Setting distance = setmgr.add(new Setting("Distance", this, 6, .1, 6, false));
    public Setting Drawbox = setmgr.add(new Setting("Draw box", this, true));
    public Setting StopOnKick = setmgr.add(new Setting("StopOnKick", this, true));
    public Setting allcolor = setmgr.add(new Setting("allcolor", this, 0, 1, 1, 1));
    public Setting idcolor = setmgr.add(new Setting("idcolor", this, .22, 1, 1, 1));
    Setting Drawmode = setmgr.add(new Setting("Hole Mode", this, "Outline", BlockEspOptions()));
    Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1, 0, 3, false));

    public final ArrayDeque<Set<BlockPos>> prevBlocks = new ArrayDeque<>();
    public BlockPos currentBlock;
    public int id ;


    public Nuker() {
        super("Nuker", Keyboard.KEY_NONE, Category.PLAYER, "Nuker");
    }

    @Override
    public void onDisable() {
        if (currentBlock != null) {
            mc.playerController.isHittingBlock = true;
            Wrapper.INSTANCE.controller().resetBlockRemoving();
            currentBlock = null;
        }
        prevBlocks.clear();

        super.onDisable();
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.world == null && StopOnKick.getValBoolean() && this.isToggled()) {
            this.toggle();
        }
        currentBlock = null;

        Vec3d eyesPos = Utils.getEyesPos().subtract(0.5, 0.5, 0.5);
        BlockPos eyesBlock = new BlockPos(Utils.getEyesPos());

        double rangeSq = Math.pow(distance.getValDouble(), 2);
        int blockRange = (int) Math.ceil(distance.getValDouble());

        Stream<BlockPos> stream = StreamSupport.stream(BlockPos.getAllInBox(
                eyesBlock.add(blockRange, blockRange, blockRange),
                eyesBlock.add(-blockRange, -blockRange, -blockRange)).spliterator(), true);

        stream = stream.filter(pos -> eyesPos.squareDistanceTo(new Vec3d(pos)) <= rangeSq)
                .filter(BlockUtils::canBeClicked)
                .sorted(Comparator.comparingDouble(pos -> eyesPos.squareDistanceTo(new Vec3d(pos))));

        if (mode.getValString().equalsIgnoreCase("id")) {
            stream = stream.filter(pos -> Block.getIdFromBlock(BlockUtils.getBlock(pos)) == id);
        }

        List<BlockPos> blocks = stream.collect(Collectors.toList());

        if (mc.player.capabilities.isCreativeMode) {
            Stream<BlockPos> stream2 = blocks.parallelStream();

            for (Set<BlockPos> set : prevBlocks) {
                stream2 = stream2.filter(pos -> !set.contains(pos));
            }

            List<BlockPos> blocks2 = stream2.collect(Collectors.toList());
            prevBlocks.addLast(new HashSet<>(blocks2));

            while (prevBlocks.size() > 5) {
                prevBlocks.removeFirst();
            }

            if (!blocks2.isEmpty()) {
                currentBlock = blocks2.get(0);
            }

            Wrapper.INSTANCE.controller().resetBlockRemoving();


            for (BlockPos blockPos : blocks2) {
                BlockUtils.breakBlockPacketSpam(blockPos);
            }
            return;
        }

        for (BlockPos pos : blocks)
            if (BlockUtils.breakBlockSimple(pos)) {
                currentBlock = pos;
                break;
            }

        if (currentBlock == null) {
            Wrapper.INSTANCE.controller().resetBlockRemoving();
        }
    }

    @Subscribe
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (mode.getValString().equalsIgnoreCase("id") && mc.world.isRemote) {
            IBlockState blockState = BlockUtils.getState(event.getPos());
            id = Block.getIdFromBlock(blockState.getBlock());
        }
    }

    @Subscribe
    public void onWorldUnload(WorldEvent.Unload event) {
        if (StopOnKick.getValBoolean())
            this.toggle();
    }


    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (currentBlock == null) {
            return;
        }
        if (Drawbox.getValBoolean()) {
            if (mode.getValString().equalsIgnoreCase("all")) {
                RenderBlock(Drawmode.getValString(), Standardbb(currentBlock), allcolor.getcolor(), LineWidth.getValDouble());
            } else if (mode.getValString().equalsIgnoreCase("id")) {
                RenderBlock(Drawmode.getValString(), Standardbb(currentBlock), idcolor.getcolor(), LineWidth.getValDouble());
            }
        }
    }

}
