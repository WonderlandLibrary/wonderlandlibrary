/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.chunk.Chunk
 */
package net.dev.important.injection.forge.mixins.performance;

import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={Chunk.class})
public class ChunkMixin_Optimization {
    @ModifyArg(method={"setBlockState"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/chunk/Chunk;relightBlock(III)V", ordinal=0), index=1)
    private int patcher$subtractOneFromY(int y) {
        return y - 1;
    }
}

