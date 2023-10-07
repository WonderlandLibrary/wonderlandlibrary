package sudo.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import sudo.module.ModuleManager;
import sudo.module.movement.NoSlow;
import sudo.module.render.xray;

@Mixin(Block.class)
public class BlockRender {
    
	@Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
		private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
		if (ModuleManager.INSTANCE.getModule(xray.class).isEnabled()) {
			cir.setReturnValue(xray.blocks.contains(state.getBlock()));
		}
	}
	
	@Inject(method = "isTranslucent", at = @At("HEAD"), cancellable = true)
		public void isTranslucent(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (ModuleManager.INSTANCE.getModule(xray.class).isEnabled()) {
			cir.setReturnValue(!xray.blocks.contains(state.getBlock()));
		}
	}
	
	@Inject(at = {@At("HEAD")}, method = {"getVelocityMultiplier()F"}, cancellable = true)
		private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
			if(!ModuleManager.INSTANCE.getModule(NoSlow.class).isEnabled()) return;
			if(cir.getReturnValueF() < 1) cir.setReturnValue(1F);
		}
}