/*
 * Decompiled with CFR 0.152.
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

public final class InventoryUtils
extends MinecraftInstance
implements Listenable {
    public static final MSTimer CLICK_TIMER = new MSTimer();
    public static final List<IBlock> BLOCK_BLACKLIST = Arrays.asList(classProvider.getBlockEnum(BlockType.CHEST), classProvider.getBlockEnum(BlockType.ENDER_CHEST), classProvider.getBlockEnum(BlockType.TRAPPED_CHEST), classProvider.getBlockEnum(BlockType.ANVIL), classProvider.getBlockEnum(BlockType.SAND), classProvider.getBlockEnum(BlockType.WEB), classProvider.getBlockEnum(BlockType.TORCH), classProvider.getBlockEnum(BlockType.CRAFTING_TABLE), classProvider.getBlockEnum(BlockType.FURNACE), classProvider.getBlockEnum(BlockType.WATERLILY), classProvider.getBlockEnum(BlockType.DISPENSER), classProvider.getBlockEnum(BlockType.STONE_PRESSURE_PLATE), classProvider.getBlockEnum(BlockType.WODDEN_PRESSURE_PLATE), classProvider.getBlockEnum(BlockType.NOTEBLOCK), classProvider.getBlockEnum(BlockType.DROPPER), classProvider.getBlockEnum(BlockType.TNT), classProvider.getBlockEnum(BlockType.STANDING_BANNER), classProvider.getBlockEnum(BlockType.WALL_BANNER), classProvider.getBlockEnum(BlockType.REDSTONE_TORCH));

    public static int findItem(int startSlot, int endSlot, IItem item) {
        for (int i = startSlot; i < endSlot; ++i) {
            IItemStack stack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (stack == null || !stack.getItem().equals(item)) continue;
            return i;
        }
        return -1;
    }

    /*
     * WARNING - void declaration
     */
    public static boolean hasSpaceHotbar() {
        void i;
        while (i < 45) {
            IItemStack stack = mc.getThePlayer().getInventory().getStackInSlot((int)i);
            if (stack == null) {
                return true;
            }
            ++i;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public static int findAutoBlockBlock() {
        IItemBlock itemBlock;
        IBlock block;
        IItemStack itemStack;
        void i;
        while (i < 45) {
            itemStack = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack();
            if (itemStack != null && classProvider.isItemBlock(itemStack.getItem()) && itemStack.getStackSize() > 0 && (block = (itemBlock = itemStack.getItem().asItemBlock()).getBlock()).isFullCube(block.getDefaultState()) && !BLOCK_BLACKLIST.contains(block) && !classProvider.isBlockBush(block)) {
                return (int)i;
            }
            ++i;
        }
        while (i < 45) {
            itemStack = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack();
            if (itemStack != null && classProvider.isItemBlock(itemStack.getItem()) && itemStack.getStackSize() > 0 && !BLOCK_BLACKLIST.contains(block = (itemBlock = itemStack.getItem().asItemBlock()).getBlock()) && !classProvider.isBlockBush(block)) {
                return (int)i;
            }
            ++i;
        }
        return -1;
    }

    @EventTarget
    public void onClick(ClickWindowEvent event) {
        CLICK_TIMER.reset();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        IPacket packet = event.getPacket();
        if (classProvider.isCPacketPlayerBlockPlacement(packet)) {
            CLICK_TIMER.reset();
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

