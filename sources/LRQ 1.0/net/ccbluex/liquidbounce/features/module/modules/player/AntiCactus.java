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
    public final void onBlockBB(BlockBBEvent event) {
        if (MinecraftInstance.classProvider.isBlockCactus(event.getBlock())) {
            event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(event.getX(), event.getY(), event.getZ(), (double)event.getX() + 1.0, (double)event.getY() + 1.0, (double)event.getZ() + 1.0));
        }
    }
}

