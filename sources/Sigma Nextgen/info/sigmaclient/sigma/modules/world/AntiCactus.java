package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.block.CactusBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AntiCactus extends Module {
    public AntiCactus() {
        super("AntiCactus", Category.World, "Prevent you from taking damage from cactus.");
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()){
            // SB
        }else{
            final AxisAlignedBB 䢿鷏낛缰䩜꿩 = mc.player.getBoundingBox();
            for (final Direction d : new Direction[] {
                    Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
            }) {
                if(mc.world.getBlockState(new BlockPos(event.x + d.getXOffset() * 0.1f, event.y, event.z + d.getZOffset() * 0.1f)).getBlock() instanceof CactusBlock){
                    event.x -= d.getXOffset() * 0.1f;
                    event.z -= d.getZOffset() * 0.1f;
                }
            }
        }
        super.onUpdateEvent(event);
    }
}
