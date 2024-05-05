/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public class LockCode {
    public static final LockCode EMPTY_CODE = new LockCode("");
    private final String lock;

    public LockCode(String code) {
        this.lock = code;
    }

    public boolean isEmpty() {
        if (this.lock == null) return true;
        if (this.lock.isEmpty()) return true;
        return false;
    }

    public String getLock() {
        return this.lock;
    }

    public void toNBT(NBTTagCompound nbt) {
        nbt.setString("Lock", this.lock);
    }

    public static LockCode fromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("Lock", 8)) return EMPTY_CODE;
        String s = nbt.getString("Lock");
        return new LockCode(s);
    }
}

