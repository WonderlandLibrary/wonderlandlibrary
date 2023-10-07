/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.chunk.Chunk
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.world;

import java.util.Map;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ProphuntESP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Chunk.class})
public class MixinChunk {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"setBlockState"}, at={@At(value="HEAD")})
    private void setProphuntBlock(BlockPos pos, IBlockState state, CallbackInfoReturnable callbackInfo) {
        ProphuntESP prophuntESP = (ProphuntESP)LiquidBounce.moduleManager.getModule(ProphuntESP.class);
        if (prophuntESP.getState()) {
            Map<BlockPos, Long> map = prophuntESP.blocks;
            synchronized (map) {
                prophuntESP.blocks.put(pos, System.currentTimeMillis());
            }
        }
    }
}

