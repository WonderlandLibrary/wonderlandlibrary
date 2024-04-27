/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;

@ModuleInfo(name="AirLadder", description="Allows you to climb up ladders/vines without touching them.", category=ModuleCategory.MOVEMENT)
public final class AirLadder
extends Module {
    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (MinecraftInstance.classProvider.isBlockLadder(BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + 1.0, iEntityPlayerSP2.getPosZ()))) && iEntityPlayerSP2.isCollidedHorizontally() || MinecraftInstance.classProvider.isBlockVine(BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ()))) || MinecraftInstance.classProvider.isBlockVine(BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + 1.0, iEntityPlayerSP2.getPosZ())))) {
            iEntityPlayerSP2.setMotionY(0.15);
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
        }
    }
}

