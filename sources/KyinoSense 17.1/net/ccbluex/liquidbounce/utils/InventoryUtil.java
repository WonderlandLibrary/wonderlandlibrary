/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C0EPacketClickWindow
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C16PacketClientStatus$EnumState
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0006J\u0006\u0010\u0013\u001a\u00020\u0014J\u0006\u0010\u0015\u001a\u00020\u0016J\u001e\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fJ\b\u0010 \u001a\u00020\u0011H\u0016J\u0006\u0010!\u001a\u00020\u0011J\u000e\u0010\"\u001a\u00020\u00112\u0006\u0010#\u001a\u00020$J\u0016\u0010%\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020&2\u0006\u0010\u001e\u001a\u00020\u001fJ\u000e\u0010'\u001a\u00020\u00112\u0006\u0010(\u001a\u00020\u0016J\u0010\u0010)\u001a\u00020\u00142\u0006\u0010*\u001a\u00020+H\u0007J\u0010\u0010,\u001a\u00020\u00142\u0006\u0010*\u001a\u00020-H\u0007J\u0006\u0010.\u001a\u00020\u0014R\u001f\u0010\u0004\u001a\u0010\u0012\f\u0012\n \u0007*\u0004\u0018\u00010\u00060\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\r\u00a8\u0006/"}, d2={"Lnet/ccbluex/liquidbounce/utils/InventoryUtil;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "BLOCK_BLACKLIST", "", "Lnet/minecraft/block/Block;", "kotlin.jvm.PlatformType", "getBLOCK_BLACKLIST", "()Ljava/util/List;", "CLICK_TIMER", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "getCLICK_TIMER", "()Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "INV_TIMER", "getINV_TIMER", "canPlaceBlock", "", "block", "closePacket", "", "findAutoBlockBlock", "", "findItem", "startSlot", "endSlot", "item", "Lnet/minecraft/item/Item;", "getItemDurability", "", "stack", "Lnet/minecraft/item/ItemStack;", "handleEvents", "hasSpaceHotbar", "isBlockListBlock", "itemBlock", "Lnet/minecraft/item/ItemBlock;", "isPositivePotion", "Lnet/minecraft/item/ItemPotion;", "isPositivePotionEffect", "id", "onClick", "event", "Lnet/ccbluex/liquidbounce/event/ClickWindowEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "openPacket", "KyinoClient"})
public final class InventoryUtil
extends MinecraftInstance
implements Listenable {
    @NotNull
    private static final MSTimer CLICK_TIMER;
    @NotNull
    private static final MSTimer INV_TIMER;
    @NotNull
    private static final List<Block> BLOCK_BLACKLIST;
    public static final InventoryUtil INSTANCE;

    @NotNull
    public final MSTimer getCLICK_TIMER() {
        return CLICK_TIMER;
    }

    @NotNull
    public final MSTimer getINV_TIMER() {
        return INV_TIMER;
    }

    @NotNull
    public final List<Block> getBLOCK_BLACKLIST() {
        return BLOCK_BLACKLIST;
    }

    /*
     * WARNING - void declaration
     */
    public final int findItem(int startSlot, int endSlot, @NotNull Item item) {
        Intrinsics.checkParameterIsNotNull(item, "item");
        int n = startSlot;
        int n2 = endSlot;
        while (n < n2) {
            void i;
            Slot slot = MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a((int)i);
            Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(i)");
            ItemStack stack = slot.func_75211_c();
            if (stack != null && stack.func_77973_b() == item) {
                return (int)i;
            }
            ++i;
        }
        return -1;
    }

    /*
     * WARNING - void declaration
     */
    public final boolean hasSpaceHotbar() {
        int n = 36;
        int n2 = 44;
        while (n <= n2) {
            void i;
            Slot slot = MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a((int)i);
            Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(i)");
            if (slot.func_75211_c() == null) {
                return true;
            }
            ++i;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public final int findAutoBlockBlock() {
        int n = 36;
        int n2 = 44;
        while (n <= n2) {
            void i;
            Slot slot = MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a((int)i);
            Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(i)");
            ItemStack itemStack = slot.func_75211_c();
            if (itemStack != null && itemStack.func_77973_b() instanceof ItemBlock) {
                Block block;
                Item item = itemStack.func_77973_b();
                if (item == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
                }
                ItemBlock itemBlock = (ItemBlock)item;
                Block block2 = block = itemBlock.func_179223_d();
                Intrinsics.checkExpressionValueIsNotNull(block2, "block");
                if (this.canPlaceBlock(block2) && itemStack.field_77994_a > 0) {
                    return (int)i;
                }
            }
            ++i;
        }
        return -1;
    }

    public final boolean canPlaceBlock(@NotNull Block block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        return block.func_149686_d() && !BLOCK_BLACKLIST.contains(block);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean isBlockListBlock(@NotNull ItemBlock itemBlock) {
        Intrinsics.checkParameterIsNotNull(itemBlock, "itemBlock");
        Block block = itemBlock.func_179223_d();
        if (BLOCK_BLACKLIST.contains(block)) return true;
        Block block2 = block;
        Intrinsics.checkExpressionValueIsNotNull(block2, "block");
        if (block2.func_149686_d()) return false;
        return true;
    }

    @EventTarget
    public final void onClick(@NotNull ClickWindowEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        CLICK_TIMER.reset();
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C0EPacketClickWindow || packet instanceof C08PacketPlayerBlockPlacement) {
            INV_TIMER.reset();
        }
        if (packet instanceof C08PacketPlayerBlockPlacement) {
            CLICK_TIMER.reset();
        }
    }

    public final void openPacket() {
        Minecraft minecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
    }

    public final void closePacket() {
        Minecraft minecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
    }

    public final boolean isPositivePotionEffect(int id) {
        return id == Potion.field_76428_l.field_76415_H || id == Potion.field_76424_c.field_76415_H || id == Potion.field_76432_h.field_76415_H || id == Potion.field_76439_r.field_76415_H || id == Potion.field_76430_j.field_76415_H || id == Potion.field_76441_p.field_76415_H || id == Potion.field_76429_m.field_76415_H || id == Potion.field_76427_o.field_76415_H || id == Potion.field_76444_x.field_76415_H || id == Potion.field_76422_e.field_76415_H || id == Potion.field_76420_g.field_76415_H || id == Potion.field_180152_w.field_76415_H || id == Potion.field_76426_n.field_76415_H;
    }

    public final boolean isPositivePotion(@NotNull ItemPotion item, @NotNull ItemStack stack) {
        Intrinsics.checkParameterIsNotNull(item, "item");
        Intrinsics.checkParameterIsNotNull(stack, "stack");
        List list = item.func_77832_l(stack);
        Intrinsics.checkExpressionValueIsNotNull(list, "item.getEffects(stack)");
        Iterable $this$forEach$iv = list;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            PotionEffect it = (PotionEffect)element$iv;
            boolean bl = false;
            PotionEffect potionEffect = it;
            Intrinsics.checkExpressionValueIsNotNull(potionEffect, "it");
            if (!INSTANCE.isPositivePotionEffect(potionEffect.func_76456_a())) continue;
            return true;
        }
        return false;
    }

    public final float getItemDurability(@NotNull ItemStack stack) {
        Intrinsics.checkParameterIsNotNull(stack, "stack");
        if (stack.func_77984_f() && stack.func_77958_k() > 0) {
            return (float)(stack.func_77958_k() - stack.func_77952_i()) / (float)stack.func_77958_k();
        }
        return 1.0f;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    private InventoryUtil() {
    }

    static {
        InventoryUtil inventoryUtil;
        INSTANCE = inventoryUtil = new InventoryUtil();
        CLICK_TIMER = new MSTimer();
        INV_TIMER = new MSTimer();
        BLOCK_BLACKLIST = CollectionsKt.listOf(Blocks.field_150381_bn, (Block)Blocks.field_150486_ae, Blocks.field_150477_bB, Blocks.field_150447_bR, Blocks.field_150467_bQ, (Block)Blocks.field_150354_m, Blocks.field_150321_G, Blocks.field_150478_aa, Blocks.field_150462_ai, Blocks.field_150460_al, Blocks.field_150392_bi, Blocks.field_150367_z, Blocks.field_150456_au, Blocks.field_150452_aw, (Block)Blocks.field_150328_O, Blocks.field_150457_bL, (Block)Blocks.field_150327_N, Blocks.field_150323_B, Blocks.field_150409_cd, Blocks.field_180393_cK, Blocks.field_180394_cL);
    }
}

