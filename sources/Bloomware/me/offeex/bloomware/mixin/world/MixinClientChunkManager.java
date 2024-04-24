package me.offeex.bloomware.mixin.world;

import me.offeex.bloomware.event.events.EventChunk;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ClientChunkManager.class)
public class MixinClientChunkManager {
    @Inject(method = "loadChunkFromPacket", at = @At(value = "RETURN", ordinal = 1))
    void onLoadChunkFromPacket(int x, int z, PacketByteBuf buf, NbtCompound nbt, Consumer<ChunkData.BlockEntityVisitor> consumer, CallbackInfoReturnable<@Nullable WorldChunk> cir) {
        new EventChunk.Load(cir.getReturnValue()).post();
    }

    @ModifyVariable(method = "unload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientChunkManager;positionEquals(Lnet/minecraft/world/chunk/WorldChunk;II)Z"), index = 4)
    private WorldChunk unload(WorldChunk value) {
        if (value != null) new EventChunk.Unload(value).post();
        return value;
    }
}
