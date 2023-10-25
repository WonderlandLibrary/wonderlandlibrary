/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.IChatComponent;

public class AnimalChest
extends InventoryBasic {
    public AnimalChest(IChatComponent iChatComponent, int n) {
        super(iChatComponent, n);
    }

    public AnimalChest(String string, int n) {
        super(string, false, n);
    }
}

