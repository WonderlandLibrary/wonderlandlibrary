package me.offeex.bloomware.mixin.world;

import me.offeex.bloomware.client.module.visuals.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class MixinBiomeColors {
    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    private static void getWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        Environment module = Environment.INSTANCE;
        if (module.getEnabled() && Environment.INSTANCE.getWater().getToggled())
            cir.setReturnValue(Environment.INSTANCE.getWaterColor().getColor().getArgb());
    }

    @Inject(method = "getGrassColor", at = @At("HEAD"), cancellable = true)
    private static void getGrassBlock(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        Environment module = Environment.INSTANCE;
        if (module.getEnabled() && Environment.INSTANCE.getGrass().getToggled())
            cir.setReturnValue(Environment.INSTANCE.getGrassColor().getColor().getArgb());
    }

    @Inject(method = "getFoliageColor", at = @At("HEAD"), cancellable = true)
    private static void onGetFoliageColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        Environment module = Environment.INSTANCE;
        if (module.getEnabled() && Environment.INSTANCE.getLeaves().getToggled())
            cir.setReturnValue(Environment.INSTANCE.getLeavesColor().getColor().getArgb());
    }
}
