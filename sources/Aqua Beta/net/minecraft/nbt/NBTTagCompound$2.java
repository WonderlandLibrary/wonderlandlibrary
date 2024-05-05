package net.minecraft.nbt;

import java.util.concurrent.Callable;
import net.minecraft.nbt.NBTBase;

class NBTTagCompound.2
implements Callable<String> {
    final /* synthetic */ int val$expectedType;

    NBTTagCompound.2(int n) {
        this.val$expectedType = n;
    }

    public String call() throws Exception {
        return NBTBase.NBT_TYPES[this.val$expectedType];
    }
}
