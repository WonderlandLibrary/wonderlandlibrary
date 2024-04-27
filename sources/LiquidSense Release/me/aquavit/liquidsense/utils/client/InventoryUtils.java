package me.aquavit.liquidsense.utils.client;

import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.event.events.ClickWindowEvent;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.Listenable;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

import java.util.Arrays;
import java.util.List;

public final class InventoryUtils extends MinecraftInstance implements Listenable {

    public static final MSTimer CLICK_TIMER = new MSTimer();
    public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest,
            Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch, Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser,
            Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.noteblock, Blocks.dropper);

    public static int findItem(final int startSlot, final int endSlot, final Item item) {
        for(int i = startSlot; i < endSlot; i++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if(stack != null && stack.getItem() == item)
                return i;
        }
        return -1;
    }

    public static boolean hasSpaceHotbar() {
        for(int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if(itemStack == null)
                return true;
        }
        return false;
    }

    public static boolean canPlaceBlock(Block block) {
        return block.isFullCube() && !BLOCK_BLACKLIST.contains(block);
    }

    public static int findAutoBlockBlock() {
        for(int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if(itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
                final Block block = itemBlock.getBlock();

                if (block.isFullCube() && !BLOCK_BLACKLIST.contains(block))
                    return i;
            }
        }

        for(int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if(itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
                final Block block = itemBlock.getBlock();

                if (!BLOCK_BLACKLIST.contains(block))
                    return i;
            }
        }

        return -1;
    }

    @EventTarget
    public void onClick(final ClickWindowEvent event) {
        CLICK_TIMER.reset();
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();

        if (packet instanceof C08PacketPlayerBlockPlacement)
            CLICK_TIMER.reset();
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}
