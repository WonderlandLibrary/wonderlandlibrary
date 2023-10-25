package net.minecraft.nbt;

import java.util.concurrent.Callable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/*
 * Exception performing whole class analysis ignored.
 */
class NBTTagCompound.1
implements Callable<String> {
    final /* synthetic */ String val$key;

    NBTTagCompound.1(String string) {
        this.val$key = string;
    }

    public String call() throws Exception {
        return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.access$000((NBTTagCompound)NBTTagCompound.this).get((Object)this.val$key)).getId()];
    }
}
