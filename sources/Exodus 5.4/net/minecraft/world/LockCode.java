/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public class LockCode {
    private final String lock;
    public static final LockCode EMPTY_CODE = new LockCode("");

    public String getLock() {
        return this.lock;
    }

    public boolean isEmpty() {
        return this.lock == null || this.lock.isEmpty();
    }

    public static LockCode fromNBT(NBTTagCompound nBTTagCompound) {
        if (nBTTagCompound.hasKey("Lock", 8)) {
            String string = nBTTagCompound.getString("Lock");
            return new LockCode(string);
        }
        return EMPTY_CODE;
    }

    public LockCode(String string) {
        this.lock = string;
    }

    public void toNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setString("Lock", this.lock);
    }
}

