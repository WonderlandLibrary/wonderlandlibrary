/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.player;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import org.celestial.client.helpers.Helper;

public class InventoryHelper
implements Helper {
    public static int getInventoryItemSlot(Item item, boolean hotbar) {
        int i;
        int n = i = hotbar ? 0 : 9;
        while (i < 39) {
            ItemStack stack = InventoryHelper.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return i < 9 ? i + 36 : i;
            }
            ++i;
        }
        return -1;
    }

    public static int getCount(Item item, boolean offhand, boolean hotbar) {
        int i;
        int count = 0;
        if (offhand && InventoryHelper.mc.player.getHeldItemOffhand().getItem() == item) {
            ++count;
        }
        int n = i = hotbar ? 0 : 9;
        while (i < 39) {
            ItemStack stack = InventoryHelper.mc.player.inventory.getStackInSlot(i < 9 ? i + 36 : i);
            if (stack.getItem() == item) {
                ++count;
            }
            ++i;
        }
        return count;
    }

    public static void switchTo(int slot, boolean silent) {
        if (silent) {
            InventoryHelper.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        } else {
            InventoryHelper.mc.player.inventory.currentItem = slot;
        }
        InventoryHelper.mc.playerController.updateController();
    }

    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper.mc.player.inventory.getStackInSlot(i);
            if (!(InventoryHelper.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe)) continue;
            return true;
        }
        return false;
    }

    public static int getSlot(int slot) {
        if (Minecraft.getMinecraft().player.inventory.getStackInSlot(slot).getItem() == Item.getItemById(slot)) {
            return slot;
        }
        return 0;
    }

    public static ItemStack getSlotStack(int slot) {
        return InventoryHelper.mc.player.inventory.getStackInSlot(slot);
    }

    public static int getSlotWithDuels() {
        for (int i = 0; i < 45; ++i) {
            if (Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack().getItem() != Items.DIAMOND_SWORD) continue;
            return i;
        }
        return 0;
    }

    public static int getSlotBow() {
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack().getItem() != Items.BOW) continue;
            return i;
        }
        return 0;
    }

    public static int getSlotWithFood() {
        for (int i = 0; i < 45; ++i) {
            if (!(Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack().getItem() instanceof ItemFood)) continue;
            return i;
        }
        return 0;
    }

    public static int getBlocksAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.inventoryContainer.getSlot(i).getStack();
            if (!(itemStack.getItem() instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }

    public static int getShieldAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.inventoryContainer.getSlot(i).getStack();
            if (!(itemStack.getItem() instanceof ItemShield)) continue;
            return i;
        }
        return -1;
    }

    public static int getEnderPearlAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.ENDER_PEARL) continue;
            return i;
        }
        return -1;
    }

    public static int getSlotWithPot() {
        for (int i = 0; i < 9; ++i) {
            Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() != Items.SPLASH_POTION) continue;
            return i;
        }
        return 0;
    }

    public static int getCustomSlot(ItemStack stack) {
        for (int i = 0; i < 45; ++i) {
            Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (!Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getDisplayName().equalsIgnoreCase(stack.getDisplayName())) continue;
            return i;
        }
        return 0;
    }

    public static boolean hotbarHasAir(EntityPlayer entity) {
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack = entity.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null) continue;
            return true;
        }
        return false;
    }

    public static boolean inventoryHasPotion() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.SPLASH_POTION) continue;
            return true;
        }
        return false;
    }

    public static boolean inventoryHasAir() {
        for (int index = 0; index < InventoryHelper.mc.player.inventory.getSizeInventory() - 1; ++index) {
            if (InventoryHelper.mc.player.inventory.getStackInSlot(index).getItem() != Items.field_190931_a) continue;
            return true;
        }
        return false;
    }

    public static boolean hotbarHasAir() {
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null) continue;
            return true;
        }
        return false;
    }

    public static ItemStack getItemStackFromItem(Item item) {
        if (InventoryHelper.mc.player == null) {
            return null;
        }
        for (int slot = 0; slot <= 9; ++slot) {
            if (InventoryHelper.mc.player.inventory.getStackInSlot(slot).getItem() != item) continue;
            return InventoryHelper.mc.player.inventory.getStackInSlot(slot);
        }
        return null;
    }

    public static boolean doesHotbarHaveSword() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper.mc.player.inventory.getStackInSlot(i);
            if (!(InventoryHelper.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSword)) continue;
            return true;
        }
        return false;
    }

    public static boolean doesHotbarHavePot() {
        for (int i = 0; i < 9; ++i) {
            if (InventoryHelper.mc.player.inventory.getStackInSlot(i).getItem() != Items.SPLASH_POTION) continue;
            return true;
        }
        return false;
    }

    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (!(InventoryHelper.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)) continue;
            return true;
        }
        return false;
    }

    public static int findFood() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper.mc.player.inventory.getStackInSlot(i);
            if (!(InventoryHelper.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemFood)) continue;
            return i;
        }
        return -1;
    }

    public static int findWaterBucket() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper.mc.player.inventory.getStackInSlot(i);
            if (InventoryHelper.mc.player.inventory.getStackInSlot(i).getItem() != Items.WATER_BUCKET) continue;
            return i;
        }
        return -1;
    }

    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.inventory.getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemSword)) continue;
            return i;
        }
        return -1;
    }

    public static int getTotemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.TOTEM_OF_UNDYING) continue;
            return i;
        }
        return -1;
    }

    public static int getSlotWithAir() {
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.field_190931_a) continue;
            return i;
        }
        return -1;
    }

    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = InventoryHelper.getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            ItemStack is;
            if (!InventoryHelper.mc.player.inventoryContainer.getSlot(i).getHasStack() || !(InventoryHelper.getProtection(is = InventoryHelper.mc.player.inventoryContainer.getSlot(i).getStack()) > prot) || !is.getUnlocalizedName().contains(strType)) continue;
            return false;
        }
        return true;
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)((double)prot + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(0)), stack)) * 0.0075));
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(3)), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(1)), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(7)), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), stack) / 50.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(4)), stack) / 100.0);
        }
        return prot;
    }
}

