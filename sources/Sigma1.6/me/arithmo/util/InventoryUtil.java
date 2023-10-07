/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util;

import me.arithmo.util.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class InventoryUtil
implements MinecraftUtil {
    public static void clickSlot(int slot, int mouseButton, boolean shiftClick) {
        InventoryUtil.mc.playerController.windowClick(InventoryUtil.mc.currentScreen == null ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, InventoryUtil.mc.thePlayer);
    }

    public static void clickSlot(int slot, boolean shiftClick) {
        InventoryUtil.mc.playerController.windowClick(InventoryUtil.mc.currentScreen == null ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, 0, shiftClick ? 1 : 0, InventoryUtil.mc.thePlayer);
    }

    public static void doubleClickSlot(int slot) {
        InventoryUtil.mc.playerController.windowClick(InventoryUtil.mc.currentScreen == null ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, InventoryUtil.mc.thePlayer);
        InventoryUtil.mc.playerController.windowClick(InventoryUtil.mc.currentScreen == null ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, 0, 6, InventoryUtil.mc.thePlayer);
        InventoryUtil.mc.playerController.windowClick(InventoryUtil.mc.currentScreen == null ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, InventoryUtil.mc.thePlayer);
    }
}

