/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.pathfinder.NodeProcessor
 */
package net.dev.important.injection.forge.mixins.performance;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NodeProcessor.class})
public class NodeProcessorMixin_MemoryLeak {
    @Shadow
    protected IBlockAccess field_176169_a;

    @Inject(method={"postProcess"}, at={@At(value="HEAD")})
    private void patcher$cleanupBlockAccess(CallbackInfo ci) {
        this.field_176169_a = null;
    }
}

