/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 */
package net.dev.important.utils;

import java.util.Arrays;
import java.util.List;
import net.dev.important.event.ClickWindowEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Listenable;
import net.dev.important.event.PacketEvent;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.minecraft.block.Block;
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
    public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(Blocks.field_150381_bn, Blocks.field_150486_ae, Blocks.field_150477_bB, Blocks.field_150447_bR, Blocks.field_150467_bQ, Blocks.field_150354_m, Blocks.field_150321_G, Blocks.field_150478_aa, Blocks.field_150462_ai, Blocks.field_150460_al, Blocks.field_150392_bi, Blocks.field_150367_z, Blocks.field_150456_au, Blocks.field_150452_aw, Blocks.field_150323_B, Blocks.field_150409_cd, Blocks.field_150335_W, Blocks.field_180393_cK, Blocks.field_180394_cL, Blocks.field_150429_aA, Blocks.field_150351_n, Blocks.field_150434_aF, Blocks.field_150324_C, Blocks.field_150442_at, Blocks.field_150472_an, Blocks.field_150444_as, Blocks.field_150421_aI, Blocks.field_180407_aO, Blocks.field_180408_aP, Blocks.field_180404_aQ, Blocks.field_180403_aR, Blocks.field_180406_aS, Blocks.field_180390_bo, Blocks.field_180391_bp, Blocks.field_180392_bq, Blocks.field_180386_br, Blocks.field_180385_bs, Blocks.field_150386_bk, Blocks.field_150415_aT, Blocks.field_150440_ba, Blocks.field_150382_bo, Blocks.field_150383_bp, Blocks.field_150465_bP, Blocks.field_150438_bZ, Blocks.field_150404_cg, Blocks.field_150488_af, Blocks.field_150445_bS, Blocks.field_150443_bT, Blocks.field_150453_bW);

    public static int findItem(int startSlot, int endSlot, Item item) {
        for (int i = startSlot; i < endSlot; ++i) {
            ItemStack stack = InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack == null || stack.func_77973_b() != item) continue;
            return i;
        }
        return -1;
    }

    public static boolean hasSpaceHotbar() {
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack != null) continue;
            return true;
        }
        return false;
    }

    public static int findAutoBlockBlock() {
        for (int i = 36; i < 45; ++i) {
            ItemBlock itemBlock;
            Block block;
            ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack == null || !(itemStack.func_77973_b() instanceof ItemBlock) || itemStack.field_77994_a <= 0 || !(block = (itemBlock = (ItemBlock)itemStack.func_77973_b()).func_179223_d()).func_149686_d() || BLOCK_BLACKLIST.contains(block)) continue;
            return i;
        }
        return -1;
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

    @Override
    public boolean handleEvents() {
        return true;
    }
}

