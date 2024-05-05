/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

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

@ModuleInfo(name="AutoTool", description="Automatically selects the best tool in your inventory to mine a block.", category=ModuleCategory.PLAYER)
public final class AutoTool
extends Module {
    @EventTarget
    public final void onClick(ClickBlockEvent event) {
        WBlockPos wBlockPos = event.getClickedBlock();
        if (wBlockPos == null) {
            return;
        }
        this.switchSlot(wBlockPos);
    }

    /*
     * WARNING - void declaration
     */
    public final void switchSlot(WBlockPos blockPos) {
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

