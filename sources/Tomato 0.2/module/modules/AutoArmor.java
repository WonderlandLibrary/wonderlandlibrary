/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.modules.AutoPot;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AutoArmor
extends Module {
    private final int[] boots = new int[]{313, 309, 317, 305, 301};
    private final int[] chestplate = new int[]{311, 307, 315, 303, 299};
    private final int[] helmet = new int[]{310, 306, 314, 302, 298};
    private final int[] leggings = new int[]{312, 308, 316, 304, 300};
    int delay = 0;

    public AutoArmor() {
        super("AutoArmor", 0, Category.COMBAT, 15120384, true, "Automatically equips the next available armor on the inventory.", new String[]{"aar", "aa", "autoa", "aarmor"});
    }

    private int getItem(int id2) {
        int index = 9;
        while (index < 45) {
            ItemStack item = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == id2) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    @EventTarget
    public void onPre(EventPreMotionUpdates e2) {
        int item = -1;
        ++this.delay;
        if (this.delay >= 10 && !AutoPot.CurrentlyPotting()) {
            int id2;
            if (Wrapper.mc.thePlayer.inventory.armorInventory[0] == null) {
                int[] boots = this.boots;
                int length = boots.length;
                int i2 = 0;
                while (i2 < length) {
                    id2 = boots[i2];
                    if (this.getItem(id2) != -1) {
                        item = this.getItem(id2);
                        break;
                    }
                    ++i2;
                }
            }
            if (Wrapper.mc.thePlayer.inventory.armorInventory[1] == null) {
                int[] leggings = this.leggings;
                int length2 = leggings.length;
                int j2 = 0;
                while (j2 < length2) {
                    id2 = leggings[j2];
                    if (this.getItem(id2) != -1) {
                        item = this.getItem(id2);
                        break;
                    }
                    ++j2;
                }
            }
            if (Wrapper.mc.thePlayer.inventory.armorInventory[2] == null) {
                int[] chestplate = this.chestplate;
                int length3 = chestplate.length;
                int k2 = 0;
                while (k2 < length3) {
                    id2 = chestplate[k2];
                    if (this.getItem(id2) != -1) {
                        item = this.getItem(id2);
                        break;
                    }
                    ++k2;
                }
            }
            if (Wrapper.mc.thePlayer.inventory.armorInventory[3] == null) {
                int[] helmet = this.helmet;
                int length4 = helmet.length;
                int l2 = 0;
                while (l2 < length4) {
                    id2 = helmet[l2];
                    if (this.getItem(id2) != -1) {
                        item = this.getItem(id2);
                        break;
                    }
                    ++l2;
                }
            }
            if (item != -1) {
                Wrapper.mc.playerController.windowClick(0, item, 0, 1, Wrapper.mc.thePlayer);
                this.delay = 0;
            }
        }
    }
}

