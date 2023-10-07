/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 */
package net.ccbluex.liquidbounce.utils;

import java.util.Arrays;
import java.util.List;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public final class InventoryUtils
extends MinecraftInstance
implements Listenable {
    public static final MSTimer CLICK_TIMER = new MSTimer();
    public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(Blocks.field_150381_bn, Blocks.field_150486_ae, Blocks.field_150477_bB, Blocks.field_150447_bR, Blocks.field_150467_bQ, Blocks.field_150354_m, Blocks.field_150321_G, Blocks.field_150478_aa, Blocks.field_150462_ai, Blocks.field_150460_al, Blocks.field_150392_bi, Blocks.field_150367_z, Blocks.field_150456_au, Blocks.field_150452_aw, Blocks.field_150323_B, Blocks.field_150409_cd, Blocks.field_150335_W, Blocks.field_180393_cK, Blocks.field_180394_cL);

    public static int findItem(int startSlot, int endSlot, Item item) {
        for (int i = startSlot; i < endSlot; ++i) {
            ItemStack stack = InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack == null || stack.func_77973_b() != item) continue;
            return i;
        }
        return -1;
    }

    public static void click(int slot, int mouseButton, boolean shiftClick) {
        InventoryUtils.mc.field_71442_b.func_78753_a(InventoryUtils.mc.field_71439_g.field_71069_bz.field_75152_c, slot, mouseButton, shiftClick ? 1 : 0, (EntityPlayer)InventoryUtils.mc.field_71439_g);
    }

    public static void drop(int slot) {
        InventoryUtils.mc.field_71442_b.func_78753_a(0, slot, 1, 4, (EntityPlayer)InventoryUtils.mc.field_71439_g);
    }

    public static void swap(int slot, int hSlot) {
        InventoryUtils.mc.field_71442_b.func_78753_a(InventoryUtils.mc.field_71439_g.field_71069_bz.field_75152_c, slot, hSlot, 2, (EntityPlayer)InventoryUtils.mc.field_71439_g);
    }

    public static boolean hasSpaceHotbar() {
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack != null) continue;
            return true;
        }
        return false;
    }

    @EventTarget
    public void onClick(ClickWindowEvent event) {
        CLICK_TIMER.reset();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof C08PacketPlayerBlockPlacement) {
            CLICK_TIMER.reset();
        }
    }

    public static boolean isBlockListBlock(ItemBlock itemBlock) {
        Block block = itemBlock.func_179223_d();
        return BLOCK_BLACKLIST.contains(block) || !block.func_149686_d();
    }

    public static int findAutoBlockBlock() {
        for (int i = 36; i < 45; ++i) {
            ItemBlock itemBlock;
            Block block;
            ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack == null || !(itemStack.func_77973_b() instanceof ItemBlock) || !InventoryUtils.canPlaceBlock(block = (itemBlock = (ItemBlock)itemStack.func_77973_b()).func_179223_d())) continue;
            return i;
        }
        return -1;
    }

    public static int findEmptySlot() {
        for (int i = 0; i < 8; ++i) {
            if (InventoryUtils.mc.field_71439_g.field_71071_by.field_70462_a[i] != null) continue;
            return i;
        }
        return InventoryUtils.mc.field_71439_g.field_71071_by.field_70461_c + (InventoryUtils.mc.field_71439_g.field_71071_by.func_70448_g() == null ? 0 : (InventoryUtils.mc.field_71439_g.field_71071_by.field_70461_c < 8 ? 4 : -1));
    }

    public static int findEmptySlot(int priority) {
        if (InventoryUtils.mc.field_71439_g.field_71071_by.field_70462_a[priority] == null) {
            return priority;
        }
        return InventoryUtils.findEmptySlot();
    }

    public static boolean canPlaceBlock(Block block) {
        return block.func_149686_d() && !BLOCK_BLACKLIST.contains(block);
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

