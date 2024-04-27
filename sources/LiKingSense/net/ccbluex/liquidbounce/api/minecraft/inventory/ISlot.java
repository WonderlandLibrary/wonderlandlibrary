/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.inventory;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u0004\u0018\u00010\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\r\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "", "hasStack", "", "getHasStack", "()Z", "slotNumber", "", "getSlotNumber", "()I", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getStack", "()Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "LiKingSense"})
public interface ISlot {
    public int getSlotNumber();

    @Nullable
    public IItemStack getStack();

    public boolean getHasStack();
}

