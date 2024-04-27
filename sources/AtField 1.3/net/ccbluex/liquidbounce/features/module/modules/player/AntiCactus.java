/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="AntiCactus", description="Prevents cactuses from damaging you.", category=ModuleCategory.PLAYER)
public final class AntiCactus
extends Module {
    @EventTarget
    public final void onBlockBB(BlockBBEvent blockBBEvent) {
        if (MinecraftInstance.classProvider.isBlockCactus(blockBBEvent.getBlock())) {
            blockBBEvent.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(blockBBEvent.getX(), blockBBEvent.getY(), blockBBEvent.getZ(), (double)blockBBEvent.getX() + 1.0, (double)blockBBEvent.getY() + 1.0, (double)blockBBEvent.getZ() + 1.0));
        }
    }
}

