/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldProvider
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={World.class})
public class WorldMixin_SkyHeight {
    @Redirect(method={"getHorizon"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/WorldProvider;getHorizon()D", remap=false))
    private double patcher$alwaysZero(WorldProvider worldProvider) {
        return 0.0;
    }
}

