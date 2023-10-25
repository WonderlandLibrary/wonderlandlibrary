/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import java.util.List;
import net.minecraft.item.ItemStack;

public interface IRememberedInventory {
    public List<ItemStack> getContents();

    public int getSize();
}

