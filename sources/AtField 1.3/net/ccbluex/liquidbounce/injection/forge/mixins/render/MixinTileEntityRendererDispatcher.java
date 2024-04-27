/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.tileentity.TileEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.injection.backend.BlockImplKt;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TileEntityRendererDispatcher.class})
public class MixinTileEntityRendererDispatcher {
    @Inject(method={"render(Lnet/minecraft/tileentity/TileEntity;FI)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderTileEntity(TileEntity tileEntity, float f, int n, CallbackInfo callbackInfo) {
        XRay xRay = (XRay)LiquidBounce.moduleManager.getModule(XRay.class);
        if (xRay.getState() && !xRay.getXrayBlocks().contains(BlockImplKt.wrap(tileEntity.func_145838_q()))) {
            callbackInfo.cancel();
        }
    }
}

