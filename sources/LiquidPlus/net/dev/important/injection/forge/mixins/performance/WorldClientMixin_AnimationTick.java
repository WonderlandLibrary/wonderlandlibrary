/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.WorldClient
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.modules.module.modules.misc.Patcher;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value={WorldClient.class})
public class WorldClientMixin_AnimationTick {
    @ModifyConstant(method={"doVoidFogParticles"}, constant={@Constant(intValue=1000)})
    private int patcher$lowerTickCount(int original) {
        return (Boolean)Patcher.lowAnimationTick.get() != false ? 100 : original;
    }
}

