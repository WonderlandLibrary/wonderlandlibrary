/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.ClickBlockEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoTool", description="Automatically selects the best tool in your inventory to mine a block.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AutoTool;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onClick", "", "event", "Lnet/ccbluex/liquidbounce/event/ClickBlockEvent;", "switchSlot", "blockPos", "Lnet/minecraft/util/BlockPos;", "KyinoClient"})
public final class AutoTool
extends Module {
    @EventTarget
    public final void onClick(@NotNull ClickBlockEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        BlockPos blockPos = event.getClickedBlock();
        if (blockPos == null) {
            return;
        }
        this.switchSlot(blockPos);
    }

    /*
     * WARNING - void declaration
     */
    public final void switchSlot(@NotNull BlockPos blockPos) {
        Intrinsics.checkParameterIsNotNull(blockPos, "blockPos");
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        IBlockState iBlockState = AutoTool.access$getMc$p$s1046033730().field_71441_e.func_180495_p(blockPos);
        Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState(blockPos)");
        Block block = iBlockState.func_177230_c();
        int n = 0;
        int n2 = 8;
        while (n <= n2) {
            void i;
            if (AutoTool.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70301_a((int)i) == null) {
            } else {
                ItemStack item;
                float speed = item.func_150997_a(block);
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
            ++i;
        }
        if (bestSlot != -1) {
            AutoTool.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c = bestSlot;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

