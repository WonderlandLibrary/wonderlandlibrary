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
    public final void onClick(ClickBlockEvent clickBlockEvent) {
        WBlockPos wBlockPos = clickBlockEvent.getClickedBlock();
        if (wBlockPos == null) {
            return;
        }
        this.switchSlot(wBlockPos);
    }

    public final void switchSlot(WBlockPos wBlockPos) {
        float f = 1.0f;
        int n = -1;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IIBlockState iIBlockState = iWorldClient.getBlockState(wBlockPos);
        int n2 = 8;
        for (int i = 0; i <= n2; ++i) {
            IItemStack iItemStack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getInventory().getStackInSlot(i) == null) {
                continue;
            }
            float f2 = iItemStack.getStrVsBlock(iIBlockState);
            if (!(f2 > f)) continue;
            f = f2;
            n = i;
        }
        if (n != -1) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.getInventory().setCurrentItem(n);
        }
    }
}

