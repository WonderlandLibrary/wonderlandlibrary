/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompressedStreamTools
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.world.chunk.storage.AnvilChunkLoader
 */
package net.dev.important.injection.forge.mixins.performance;

import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={AnvilChunkLoader.class})
public class AnvilChunkLoaderMixin_UnclosedStream {
    @Redirect(method={"loadChunk__Async"}, at=@At(value="INVOKE", target="Lnet/minecraft/nbt/CompressedStreamTools;read(Ljava/io/DataInputStream;)Lnet/minecraft/nbt/NBTTagCompound;"))
    private NBTTagCompound patcher$closeStream(DataInputStream stream) throws IOException {
        NBTTagCompound result = CompressedStreamTools.func_74794_a((DataInputStream)stream);
        stream.close();
        return result;
    }
}

