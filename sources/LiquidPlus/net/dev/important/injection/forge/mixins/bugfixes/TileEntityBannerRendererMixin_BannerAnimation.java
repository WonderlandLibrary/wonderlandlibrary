/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer
 *  net.minecraft.world.World
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={TileEntityBannerRenderer.class})
public class TileEntityBannerRendererMixin_BannerAnimation {
    @Redirect(method={"renderTileEntityAt(Lnet/minecraft/tileentity/TileEntityBanner;DDDFI)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/World;getTotalWorldTime()J"))
    private long patcher$resolveOverflow(World world) {
        return world.func_82737_E() % 100L;
    }
}

