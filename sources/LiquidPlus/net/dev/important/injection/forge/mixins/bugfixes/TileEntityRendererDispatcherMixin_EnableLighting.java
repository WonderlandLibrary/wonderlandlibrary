/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TileEntityRendererDispatcher.class})
public class TileEntityRendererDispatcherMixin_EnableLighting {
    @Inject(method={"renderTileEntity"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/World;getCombinedLight(Lnet/minecraft/util/BlockPos;I)I")})
    private void patcher$enableLighting(CallbackInfo ci) {
        RenderHelper.func_74519_b();
    }
}

