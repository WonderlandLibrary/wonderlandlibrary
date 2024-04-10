
package info.sigmaclient.sigma.utils.nbs;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;

import static info.sigmaclient.sigma.modules.Module.mc;

public class ANoteBlock
{
    public BlockPos blockPos;
    public int yindiao;
    public int id;
    public float downBlockV;
    
    public ANoteBlock(final BlockPos maCT, final int luAg) {
        super();
        this.blockPos = maCT;
        final BlockState blockState = mc.world.getBlockState(this.blockPos);
        if(!(blockState.getBlock() instanceof NoteBlock)){
            yindiao = -1;
            id = -1;
            downBlockV = -1;
            return;
        }
        this.yindiao = blockState.get(NoteBlock.NOTE);
        this.id = luAg;
        this.downBlockV = (float)WRss(this.blockPos);
    }
    
    public static int WRss(final BlockPos blockPos) {
        final BlockState blockState = mc.world.getBlockState(blockPos.down());
        int n = 0;
        if (blockState.getBlock() == Blocks.COBBLESTONE) {
            n = 1;
        }
        if (blockState.getBlock() == Blocks.SAND) {
            n = 2;
        }
        if (blockState.getBlock() == Blocks.GLASS) {
            n = 3;
        }
        if (blockState.getBlock() instanceof RotatedPillarBlock) {
            n = 4;
        }
        final Block block = blockState.getBlock();
        if (blockState.getBlock() == Blocks.CLAY) {
            n = 5;
        }
        if (blockState.getBlock() == Blocks.GOLD_BLOCK) {
            n = 6;
        }
        if (blockState.getMaterial() == Material.WOOL) {
            n = 7;
        }
        if (block instanceof IceBlock) {
            n = 8;
        }
        if (block == Blocks.BONE_BLOCK) {
            n = 9;
        }
        return n;
    }
}
