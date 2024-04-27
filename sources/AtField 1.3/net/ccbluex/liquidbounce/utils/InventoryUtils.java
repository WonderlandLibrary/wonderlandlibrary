/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package net.ccbluex.liquidbounce.utils;

import java.util.Arrays;
import java.util.List;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.item.Item;

public final class InventoryUtils
extends MinecraftInstance
implements Listenable {
    public static final List BLOCK_BLACKLIST;
    public static final MSTimer CLICK_TIMER;

    public static int findItem(int n, int n2, IItem iItem) {
        for (int i = n; i < n2; ++i) {
            IItemStack iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (iItemStack == null || !iItemStack.getItem().equals(iItem)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    static {
        CLICK_TIMER = new MSTimer();
        BLOCK_BLACKLIST = Arrays.asList(classProvider.getBlockEnum(BlockType.CHEST), classProvider.getBlockEnum(BlockType.ENDER_CHEST), classProvider.getBlockEnum(BlockType.TRAPPED_CHEST), classProvider.getBlockEnum(BlockType.ANVIL), classProvider.getBlockEnum(BlockType.SAND), classProvider.getBlockEnum(BlockType.WEB), classProvider.getBlockEnum(BlockType.TORCH), classProvider.getBlockEnum(BlockType.CRAFTING_TABLE), classProvider.getBlockEnum(BlockType.FURNACE), classProvider.getBlockEnum(BlockType.WATERLILY), classProvider.getBlockEnum(BlockType.DISPENSER), classProvider.getBlockEnum(BlockType.STONE_PRESSURE_PLATE), classProvider.getBlockEnum(BlockType.WODDEN_PRESSURE_PLATE), classProvider.getBlockEnum(BlockType.NOTEBLOCK), classProvider.getBlockEnum(BlockType.DROPPER), classProvider.getBlockEnum(BlockType.TNT), classProvider.getBlockEnum(BlockType.STANDING_BANNER), classProvider.getBlockEnum(BlockType.WALL_BANNER), classProvider.getBlockEnum(BlockType.REDSTONE_TORCH));
    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (classProvider.isCPacketPlayerBlockPlacement(iPacket)) {
            CLICK_TIMER.reset();
        }
    }

    public static boolean hasSpaceHotbar() {
        for (int i = 36; i < 45; ++i) {
            IItemStack iItemStack = mc.getThePlayer().getInventory().getStackInSlot(i);
            if (iItemStack != null) continue;
            return true;
        }
        return false;
    }

    public static int findAutoBlockBlock() {
        IItemBlock iItemBlock;
        IBlock iBlock;
        IItemStack iItemStack;
        int n;
        for (n = 36; n < 45; ++n) {
            iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(n).getStack();
            if (iItemStack == null || !classProvider.isItemBlock(iItemStack.getItem()) || iItemStack.getStackSize() <= 0 || !(iBlock = (iItemBlock = iItemStack.getItem().asItemBlock()).getBlock()).isFullCube(iBlock.getDefaultState()) || BLOCK_BLACKLIST.contains(iBlock) || classProvider.isBlockBush(iBlock)) continue;
            return n;
        }
        for (n = 36; n < 45; ++n) {
            iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(n).getStack();
            if (iItemStack == null || !classProvider.isItemBlock(iItemStack.getItem()) || iItemStack.getStackSize() <= 0 || BLOCK_BLACKLIST.contains(iBlock = (iItemBlock = iItemStack.getItem().asItemBlock()).getBlock()) || classProvider.isBlockBush(iBlock)) continue;
            return n;
        }
        return -1;
    }

    public static boolean isBlockListBlock(IItemBlock iItemBlock) {
        IBlock iBlock = iItemBlock.getBlock();
        return BLOCK_BLACKLIST.contains(iBlock) || !iBlock.isFullCube(iBlock.getDefaultState());
    }

    @EventTarget
    public void onClick(ClickWindowEvent clickWindowEvent) {
        CLICK_TIMER.reset();
    }

    public static int findItem2(int n, int n2, Item item) {
        for (int i = n; i < n2; ++i) {
            IItemStack iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (iItemStack == null || iItemStack.getItem() != item) continue;
            return i;
        }
        return -1;
    }
}

