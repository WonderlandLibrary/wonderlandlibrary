package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.ClickBlockEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoTool", description="Automatically selects the best tool in your inventory to mine a block.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\b\u000020B¢J020HJ02\b0\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AutoTool;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onClick", "", "event", "Lnet/ccbluex/liquidbounce/event/ClickBlockEvent;", "switchSlot", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "Pride"})
public final class AutoTool
extends Module {
    @EventTarget
    public final void onClick(@NotNull ClickBlockEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        WBlockPos wBlockPos = event.getClickedBlock();
        if (wBlockPos == null) {
            return;
        }
        this.switchSlot(wBlockPos);
    }

    /*
     * WARNING - void declaration
     */
    public final void switchSlot(@NotNull WBlockPos blockPos) {
        Intrinsics.checkParameterIsNotNull(blockPos, "blockPos");
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IIBlockState blockState = iWorldClient.getBlockState(blockPos);
        int n = 0;
        int n2 = 8;
        while (n <= n2) {
            void i;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getInventory().getStackInSlot((int)i) == null) {
            } else {
                IItemStack item;
                float speed = item.getStrVsBlock(blockState);
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
            ++i;
        }
        if (bestSlot != -1) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.getInventory().setCurrentItem(bestSlot);
        }
    }
}
