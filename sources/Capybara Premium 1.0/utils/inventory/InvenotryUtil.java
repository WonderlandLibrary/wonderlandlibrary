package fun.rich.client.utils.inventory;

import fun.rich.client.utils.Helper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;

public class InvenotryUtil implements Helper {
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }
        return false;
    }

    public static void disabler(int elytra) {
        mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, mc.player);
    }

    public static int getSlotWithElytra() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.ELYTRA) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int getSlowWithArmor() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.IRON_LEGGINGS) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static void swapElytraToChestplate() {
        for (ItemStack stack : mc.player.inventory.armorInventory) {
            if (stack.getItem() == Items.ELYTRA) {

                int slot = getSlowWithArmor() < 9 ? getSlowWithArmor() + 36 : getSlowWithArmor();
                if (getSlowWithArmor() != -1) {
                    mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(6, slot, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }

    public static boolean isActiveItemStackBlocking(EntityLivingBase base, int ticks) {
        if (base.isHandActive() && !base.getActiveItemStack().func_190926_b()) {
            Item item = base.getActiveItemStack().getItem();

            if (item.getItemUseAction(base.getActiveItemStack()) != EnumAction.BLOCK) {
                return false;
            } else {
                return item.getMaxItemUseDuration(base.getActiveItemStack()) - base.activeItemStackUseCount >= ticks;
            }
        } else {
            return false;
        }
    }

    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return 1;
    }

    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }

    public static int getTotemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.Totem) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemSword) {
                return i;
            }
        }
        return 1;
    }
}
