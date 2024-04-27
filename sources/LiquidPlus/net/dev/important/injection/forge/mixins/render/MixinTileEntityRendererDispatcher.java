/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.tileentity.TileEntity
 */
package net.dev.important.injection.forge.mixins.render;

import net.dev.important.Client;
import net.dev.important.modules.module.modules.render.XRay;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TileEntityRendererDispatcher.class})
public class MixinTileEntityRendererDispatcher {
    @Inject(method={"renderTileEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage, CallbackInfo callbackInfo) {
        XRay xray = (XRay)Client.moduleManager.getModule(XRay.class);
        if (xray.getState() && !xray.getXrayBlocks().contains(tileentityIn.func_145838_q())) {
            callbackInfo.cancel();
        }
    }
}

