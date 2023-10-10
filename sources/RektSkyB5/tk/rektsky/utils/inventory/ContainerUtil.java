/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.inventory;

import java.util.ArrayList;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerUtil {
    public static ArrayList<Slot> getAllNonNullItems(Container c2) {
        ArrayList<Slot> output = new ArrayList<Slot>();
        for (int i2 = 0; i2 != c2.getInventory().size(); ++i2) {
            if (c2.getInventory().get(i2) == null) continue;
            output.add(c2.getSlot(i2));
        }
        return output;
    }
}

