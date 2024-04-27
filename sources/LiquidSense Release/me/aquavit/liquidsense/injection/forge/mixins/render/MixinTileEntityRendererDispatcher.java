package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.render.XRay;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {

    @Inject(method = "renderTileEntity", at = @At("HEAD"), cancellable = true)
    private void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage, final CallbackInfo callbackInfo) {
        final XRay xray = (XRay) LiquidSense.moduleManager.getModule(XRay.class);

        if (xray.getState() && !xray.xrayBlocks.contains(tileentityIn.getBlockType()))
            callbackInfo.cancel();
    }
}
