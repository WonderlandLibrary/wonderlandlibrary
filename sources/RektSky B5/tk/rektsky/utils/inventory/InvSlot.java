/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.inventory;

import net.minecraft.item.ItemStack;

public class InvSlot {
    private int slot;
    private ItemStack item;

    public InvSlot(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return this.slot;
    }

    public InvSlot setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public ItemStack getStack() {
        return this.item;
    }

    public InvSlot setItem(ItemStack item) {
        this.item = item;
        return this;
    }
}

