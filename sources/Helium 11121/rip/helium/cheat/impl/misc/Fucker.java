package rip.helium.cheat.impl.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;

public class Fucker extends Cheat {

    public Fucker() {
        super("Fucker", "eat da cake", CheatCategory.MISC);
    }

    @Collect
    public void packetEvent(PlayerUpdateEvent e) {
        final boolean breaking = false;
        for (int radius = 7, x = -radius; x < radius; ++x) {
            for (int y = radius; y > -radius; --y) {
                for (int z = -radius; z < radius; ++z) {
                    final int xPos = (int) mc.thePlayer.posX + x;
                    final int yPos = (int) mc.thePlayer.posY + y;
                    final int zPos = (int) mc.thePlayer.posZ + z;
                    final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                    if ((block.getBlockState().getBlock() == Block.getBlockById(92) || block.getBlockState().getBlock() == Blocks.bed) && mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                    }
                }
            }
        }
    }
}
