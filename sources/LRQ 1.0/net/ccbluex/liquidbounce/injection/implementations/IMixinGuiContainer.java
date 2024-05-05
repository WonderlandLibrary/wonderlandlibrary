/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Slot
 */
package net.ccbluex.liquidbounce.injection.implementations;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;

public interface IMixinGuiContainer {
    public void publicHandleMouseClick(Slot var1, int var2, int var3, ClickType var4);
}

