package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import today.flux.event.PostUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.DelayTimer;

public class Nuker extends Module {
    public DelayTimer delay = new DelayTimer();

    public Nuker(){ super("Nuker", Category.World, false);}

    @EventTarget
    public void onTick(PostUpdateEvent event) {
        if (mc.theWorld == null) {
            return;
        }

        int radius;
        int y = radius = 6;
        while (y >= -radius) {
            int x = -radius;
            while (x <= radius) {
                int z = -radius;
                while (z <= radius) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX - 0.5 + (double) x, mc.thePlayer.posY - 0.5 + (double) y, mc.thePlayer.posZ - 0.5 + (double) z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();
                    if (this.getFacingDirection(pos) != null && !(block instanceof BlockAir)) {
                        eraseBlock(pos, this.getFacingDirection(pos));
                    }
                    ++z;
                }
                ++x;
            }
            --y;
        }
    }

    private void eraseBlock(BlockPos pos, EnumFacing facing) {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
    }

    private EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (! mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.UP;
        } else if (! mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.DOWN;
        } else if (! mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.EAST;
        } else if (! mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.WEST;
        } else if (! mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.SOUTH;
        } else if (! mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }
}
