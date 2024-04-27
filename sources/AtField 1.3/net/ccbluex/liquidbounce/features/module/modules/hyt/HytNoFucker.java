/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BlockValue;

@ModuleInfo(name="HytNoFucker", description="CNM", category=ModuleCategory.HYT)
public final class HytNoFucker
extends Module {
    private WBlockPos pos;
    private final int targetId;
    private final BlockValue blockValue = new BlockValue("Block", 26);

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public HytNoFucker() {
        this.targetId = ((Number)this.blockValue.get()).intValue();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        block7: {
            block6: {
                if (this.pos == null) break block6;
                IExtractedFunctions iExtractedFunctions = HytNoFucker.access$getFunctions$p$s1046033730();
                WBlockPos wBlockPos = this.pos;
                if (wBlockPos == null) {
                    Intrinsics.throwNpe();
                }
                IBlock iBlock = BlockUtils.getBlock(wBlockPos);
                if (iBlock == null) {
                    Intrinsics.throwNpe();
                }
                if (iExtractedFunctions.getIdFromBlock(iBlock) != this.targetId) break block6;
                WBlockPos wBlockPos2 = Fucker.INSTANCE.getPos();
                if (wBlockPos2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(BlockUtils.getCenterDistance(wBlockPos2) > (double)7)) break block7;
            }
            this.pos = Fucker.INSTANCE.find(this.targetId);
        }
    }

    public final int getTargetId() {
        return this.targetId;
    }
}

