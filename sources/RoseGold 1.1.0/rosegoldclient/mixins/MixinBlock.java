package rosegoldclient.mixins;

import net.minecraft.block.*;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rosegoldclient.Main;
import rosegoldclient.features.Phase;

@Mixin(Block.class)
public abstract class MixinBlock {

    private final PropertyEnum<BlockSlab.EnumBlockHalf> HALF_SLAB = PropertyEnum.create("half", BlockSlab.EnumBlockHalf.class);

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    private void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if(Main.configFile.phase) {
            if(Phase.phaseable.contains(pos)) {
                if(shouldGB(blockState, worldIn, pos)) {
                    cir.setReturnValue(null);
                }
            }
        }
    }

    @Inject(method = "isFullCube", at = @At("HEAD"), cancellable = true)
    private void isFullCube(IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(Main.configFile.phase) {
            cir.setReturnValue(false);
        }
    }

    private boolean shouldGB(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        Block block = blockState.getBlock();
        if(block instanceof BlockSlab) {
            if(pos.getY() > Main.mc.player.getPosition().getY()) return true;
            return ((BlockSlab)blockState.getBlock()).isDouble() || blockState.getValue(HALF_SLAB) == BlockSlab.EnumBlockHalf.TOP;
        }
        return true;
    }
}
