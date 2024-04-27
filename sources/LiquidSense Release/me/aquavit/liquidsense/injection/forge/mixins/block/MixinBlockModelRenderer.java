package me.aquavit.liquidsense.injection.forge.mixins.block;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.render.XRay;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModelRenderer.class)
public class MixinBlockModelRenderer {

    @Inject(method = "renderModelAmbientOcclusion", at = @At("HEAD"), cancellable = true)
    private void renderModelAmbientOcclusion(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSide, final CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable) {
        final XRay xray = (XRay) LiquidSense.moduleManager.getModule(XRay.class);

        if (xray.getState() && !xray.xrayBlocks.contains(blockIn))
            booleanCallbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method = "renderModelStandard", at = @At("HEAD"), cancellable = true)
    private void renderModelStandard(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides, final CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable) {
        final XRay xray = (XRay) LiquidSense.moduleManager.getModule(XRay.class);

        if (xray.getState() && !xray.xrayBlocks.contains(blockIn))
            booleanCallbackInfoReturnable.setReturnValue(false);
    }
}
