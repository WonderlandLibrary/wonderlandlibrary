/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.chunk.Chunk
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.world;

import java.util.Map;
import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ProphuntESP;
import net.ccbluex.liquidbounce.injection.backend.ChunkImplKt;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt;
import net.ccbluex.liquidbounce.utils.render.MiniMapRegister;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Chunk.class})
public class MixinChunk {
    @Shadow
    @Final
    public int field_76635_g;
    @Shadow
    @Final
    public int field_76647_h;

    @Inject(method={"onUnload"}, at={@At(value="HEAD")})
    private void injectFillChunk(CallbackInfo callbackInfo) {
        MiniMapRegister.INSTANCE.unloadChunk(this.field_76635_g, this.field_76647_h);
    }

    @Inject(method={"setBlockState"}, at={@At(value="HEAD")})
    private void setProphuntBlock(BlockPos blockPos, IBlockState iBlockState, CallbackInfoReturnable callbackInfoReturnable) {
        block1: {
            MiniMapRegister.INSTANCE.updateChunk(ChunkImplKt.wrap((Chunk)this));
            ProphuntESP prophuntESP = (ProphuntESP)LiquidBounce.moduleManager.getModule(ProphuntESP.class);
            if (!Objects.requireNonNull(prophuntESP).getState()) break block1;
            Map map = prophuntESP.getBlocks();
            synchronized (map) {
                prophuntESP.getBlocks().put(BackendExtentionsKt.wrap(blockPos), System.currentTimeMillis());
            }
        }
    }

    @Inject(method={"read"}, at={@At(value="RETURN")})
    private void injectFillChunk(PacketBuffer packetBuffer, int n, boolean bl, CallbackInfo callbackInfo) {
        MiniMapRegister.INSTANCE.updateChunk(ChunkImplKt.wrap((Chunk)this));
    }
}

