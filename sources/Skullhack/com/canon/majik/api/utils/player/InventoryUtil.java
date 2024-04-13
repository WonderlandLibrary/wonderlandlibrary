package com.canon.majik.api.utils.player;

import com.canon.majik.api.utils.Globals;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;

public class InventoryUtil implements Globals {

    public static boolean isHeld(EnumHand enumHand, Item item){
        return mc.player.getHeldItem(enumHand).getItem().equals(item);
    }

    public static void switchToSlot(int slot) {
        if (slot == -1) {
            return;
        }
        mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public static void switchBack(int slot) {
        if (slot == -1) {
            return;
        }
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public static int getItemSlot(Item item, boolean hotbar) {
        int itemSlot = -1;
        for (int i = 1; i <= (hotbar ? 45 : 36); ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem().equals(item)) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }

    public static int getItemSlot(Item item) {
        int itemSlot = -1;
        for (int i = 1; i <= 45; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem().equals(item)) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }

    public static int getBlockSlot(Block block) {
        int itemSlot = -1;
        for (int i = 1; i <= 45; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem().equals(Item.getItemFromBlock(block))) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }

    public static int getBlockSlotByName(String name) {
        int itemSlot = -1;
        for (int i = 1; i <= 45; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getDisplayName().toLowerCase().contains(name)) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }


    public static int getBlockFromHotbar(Block block) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getDisplayName().toLowerCase().contains("anchor")){
                continue;
            }
            if (stack.getItem().equals(Item.getItemFromBlock(block)))
                slot = i;
        }
        return slot;
    }

    public static int getItemFromHotbar(Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem().equals(item))
                slot = i;
        }
        return slot;
    }

    public static void clickSlot(final int id) {
        if (id != -1) {
            try {
                mc.playerController.windowClick(mc.player.openContainer.windowId, getClickSlot(id), 0, ClickType.PICKUP, (EntityPlayer)mc.player);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getClickSlot(int id) {
        if (id == -1) {
            return id;
        }
        if (id < 9) {
            id += 36;
            return id;
        }
        if (id == 39) {
            id = 5;
        }
        else if (id == 38) {
            id = 6;
        }
        else if (id == 37) {
            id = 7;
        }
        else if (id == 36) {
            id = 8;
        }
        else if (id == 40) {
            id = 45;
        }
        return id;
    }
}
