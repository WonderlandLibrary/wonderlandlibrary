/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.WorldClient
 */
package net.dev.important.injection.forge.mixins.world;

import net.dev.important.Client;
import net.dev.important.modules.module.modules.render.TrueSight;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={WorldClient.class})
public class MixinWorldClient {
    @ModifyVariable(method={"doVoidFogParticles"}, at=@At(value="INVOKE", target="Lnet/minecraft/block/Block;randomDisplayTick(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V", shift=At.Shift.AFTER), ordinal=0)
    private boolean handleBarriers(boolean flag) {
        TrueSight trueSight = (TrueSight)Client.moduleManager.getModule(TrueSight.class);
        return flag || trueSight.getState() && (Boolean)trueSight.getBarriersValue().get() != false;
    }
}

