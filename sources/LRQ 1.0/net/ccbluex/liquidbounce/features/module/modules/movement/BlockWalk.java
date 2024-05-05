/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="BlockWalk", description="Allows you to walk on non-fullblock blocks.", category=ModuleCategory.MOVEMENT)
public final class BlockWalk
extends Module {
    private final BoolValue cobwebValue = new BoolValue("Cobweb", true);
    private final BoolValue snowValue = new BoolValue("Snow", true);

    @EventTarget
    public final void onBlockBB(BlockBBEvent event) {
        if (((Boolean)this.cobwebValue.get()).booleanValue() && event.getBlock().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.WEB)) || ((Boolean)this.snowValue.get()).booleanValue() && event.getBlock().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.SNOW_LAYER))) {
            event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(event.getX(), event.getY(), event.getZ(), (double)event.getX() + 1.0, (double)event.getY() + 1.0, (double)event.getZ() + 1.0));
        }
    }
}

