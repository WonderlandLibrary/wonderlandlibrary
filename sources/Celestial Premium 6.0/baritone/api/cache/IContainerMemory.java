/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import baritone.api.cache.IRememberedInventory;
import java.util.Map;
import net.minecraft.util.math.BlockPos;

public interface IContainerMemory {
    public IRememberedInventory getInventoryByPos(BlockPos var1);

    public Map<BlockPos, IRememberedInventory> getRememberedInventories();
}

