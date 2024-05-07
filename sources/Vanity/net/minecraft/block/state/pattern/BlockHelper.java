package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.masterof13fps.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MathHelper;

public class BlockHelper implements Predicate<IBlockState> {
    private final Block block;

    private BlockHelper(Block blockType) {
        this.block = blockType;
    }

    public static BlockHelper forBlock(Block blockType) {
        return new BlockHelper(blockType);
    }

    public boolean apply(IBlockState p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.getBlock() == this.block;
    }

    public static float[] getBlockRotations(final double x, final double y, final double z) {
        final double var4 = x - Wrapper.mc.thePlayer.posX + 0.5;
        final double var5 = z - Wrapper.mc.thePlayer.posZ + 0.5;
        final double var6 = y - (Wrapper.mc.thePlayer.posY + Wrapper.mc.thePlayer.getEyeHeight() - 1.0);
        final double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var8 = (float) (Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        return new float[]{var8, (float) (-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793))};
    }
}
