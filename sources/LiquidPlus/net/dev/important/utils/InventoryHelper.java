/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C16PacketClientStatus$EnumState
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.ClickWindowEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Listenable;
import net.dev.important.event.PacketEvent;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u000e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u0016\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u001cJ\u0010\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007J\u0010\u0010 \u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020!H\u0007J\u0006\u0010\"\u001a\u00020\u000fR\u001f\u0010\u0004\u001a\u0010\u0012\f\u0012\n \u0007*\u0004\u0018\u00010\u00060\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006#"}, d2={"Lnet/dev/important/utils/InventoryHelper;", "Lnet/dev/important/utils/MinecraftInstance;", "Lnet/dev/important/event/Listenable;", "()V", "BLOCK_BLACKLIST", "", "Lnet/minecraft/block/Block;", "kotlin.jvm.PlatformType", "getBLOCK_BLACKLIST", "()Ljava/util/List;", "CLICK_TIMER", "Lnet/dev/important/utils/timer/MSTimer;", "getCLICK_TIMER", "()Lnet/dev/important/utils/timer/MSTimer;", "closePacket", "", "handleEvents", "", "isBlockListBlock", "itemBlock", "Lnet/minecraft/item/ItemBlock;", "isPositivePotion", "item", "Lnet/minecraft/item/ItemPotion;", "stack", "Lnet/minecraft/item/ItemStack;", "isPositivePotionEffect", "id", "", "onClickWindow", "event", "Lnet/dev/important/event/ClickWindowEvent;", "onPacket", "Lnet/dev/important/event/PacketEvent;", "openPacket", "LiquidBounce"})
public final class InventoryHelper
extends MinecraftInstance
implements Listenable {
    @NotNull
    public static final InventoryHelper INSTANCE = new InventoryHelper();
    @NotNull
    private static final MSTimer CLICK_TIMER = new MSTimer();
    @NotNull
    private static final List<Block> BLOCK_BLACKLIST;

    private InventoryHelper() {
    }

    @NotNull
    public final MSTimer getCLICK_TIMER() {
        return CLICK_TIMER;
    }

    @NotNull
    public final List<Block> getBLOCK_BLACKLIST() {
        return BLOCK_BLACKLIST;
    }

    public final boolean isBlockListBlock(@NotNull ItemBlock itemBlock) {
        Intrinsics.checkNotNullParameter(itemBlock, "itemBlock");
        Block block = itemBlock.func_179223_d();
        return BLOCK_BLACKLIST.contains(block) || !block.func_149686_d();
    }

    @EventTarget
    public final void onClickWindow(@NotNull ClickWindowEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        CLICK_TIMER.reset();
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C08PacketPlayerBlockPlacement) {
            CLICK_TIMER.reset();
        }
    }

    public final void openPacket() {
        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
    }

    public final void closePacket() {
        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
    }

    public final boolean isPositivePotionEffect(int id) {
        return id == Potion.field_76428_l.field_76415_H || id == Potion.field_76424_c.field_76415_H || id == Potion.field_76432_h.field_76415_H || id == Potion.field_76439_r.field_76415_H || id == Potion.field_76430_j.field_76415_H || id == Potion.field_76441_p.field_76415_H || id == Potion.field_76429_m.field_76415_H || id == Potion.field_76427_o.field_76415_H || id == Potion.field_76444_x.field_76415_H || id == Potion.field_76422_e.field_76415_H || id == Potion.field_76420_g.field_76415_H || id == Potion.field_180152_w.field_76415_H || id == Potion.field_76426_n.field_76415_H;
    }

    public final boolean isPositivePotion(@NotNull ItemPotion item, @NotNull ItemStack stack) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(stack, "stack");
        List list = item.func_77832_l(stack);
        Intrinsics.checkNotNullExpressionValue(list, "item.getEffects(stack)");
        Iterable $this$forEach$iv = list;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            PotionEffect it = (PotionEffect)element$iv;
            boolean bl = false;
            if (!INSTANCE.isPositivePotionEffect(it.func_76456_a())) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    static {
        Block[] blockArray = new Block[]{Blocks.field_150381_bn, (Block)Blocks.field_150486_ae, Blocks.field_150477_bB, Blocks.field_150447_bR, Blocks.field_150467_bQ, (Block)Blocks.field_150354_m, Blocks.field_150321_G, Blocks.field_150478_aa, Blocks.field_150462_ai, Blocks.field_150460_al, Blocks.field_150392_bi, Blocks.field_150367_z, Blocks.field_150456_au, Blocks.field_150452_aw, Blocks.field_150323_B, Blocks.field_150409_cd, Blocks.field_150335_W, Blocks.field_180393_cK, Blocks.field_180394_cL, Blocks.field_150429_aA, Blocks.field_150351_n, (Block)Blocks.field_150434_aF, Blocks.field_150324_C, Blocks.field_150442_at, Blocks.field_150472_an, Blocks.field_150444_as, Blocks.field_150421_aI, Blocks.field_180407_aO, Blocks.field_180408_aP, Blocks.field_180404_aQ, Blocks.field_180403_aR, Blocks.field_180406_aS, Blocks.field_180390_bo, Blocks.field_180391_bp, Blocks.field_180392_bq, Blocks.field_180386_br, Blocks.field_180385_bs, Blocks.field_150386_bk, Blocks.field_150415_aT, Blocks.field_150440_ba, Blocks.field_150382_bo, (Block)Blocks.field_150383_bp, (Block)Blocks.field_150465_bP, (Block)Blocks.field_150438_bZ, Blocks.field_150404_cg, (Block)Blocks.field_150488_af, Blocks.field_150445_bS, Blocks.field_150443_bT, (Block)Blocks.field_150453_bW};
        BLOCK_BLACKLIST = CollectionsKt.listOf(blockArray);
    }
}

