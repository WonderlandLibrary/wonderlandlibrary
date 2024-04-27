/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.ClickBlockEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoTool", spacedName="Auto Tool", description="Automatically selects the best tool in your inventory to mine a block.", category=Category.PLAYER, cnName="\u81ea\u52a8\u9009\u62e9\u5de5\u5177")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t\u00a8\u0006\n"}, d2={"Lnet/dev/important/modules/module/modules/player/AutoTool;", "Lnet/dev/important/modules/module/Module;", "()V", "onClick", "", "event", "Lnet/dev/important/event/ClickBlockEvent;", "switchSlot", "blockPos", "Lnet/minecraft/util/BlockPos;", "LiquidBounce"})
public final class AutoTool
extends Module {
    @EventTarget
    public final void onClick(@NotNull ClickBlockEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        BlockPos blockPos = event.getClickedBlock();
        if (blockPos == null) {
            return;
        }
        this.switchSlot(blockPos);
    }

    public final void switchSlot(@NotNull BlockPos blockPos) {
        Intrinsics.checkNotNullParameter(blockPos, "blockPos");
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        Block block = MinecraftInstance.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
        int n = 0;
        while (n < 9) {
            ItemStack item;
            float speed;
            int i;
            if (MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a(i = n++) == null || !((speed = item.func_150997_a(block)) > bestSpeed)) continue;
            bestSpeed = speed;
            bestSlot = i;
        }
        if (bestSlot != -1) {
            MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c = bestSlot;
        }
    }
}

