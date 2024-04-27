package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoFucker", description="CNM", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\n\n\b\n\n\u0000\n\n\u0000\n\b\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J0\f2\r0HR0X¢\n\u0000R0X¢\n\u0000R0\b¢\b\n\u0000\b\t\n¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/NoFucker;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "blockValue", "Lnet/ccbluex/liquidbounce/value/BlockValue;", "pos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "targetId", "", "getTargetId", "()I", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class NoFucker
extends Module {
    private final BlockValue blockValue = new BlockValue("Block", 26);
    private final int targetId = ((Number)this.blockValue.get()).intValue();
    private WBlockPos pos;

    public final int getTargetId() {
        return this.targetId;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block7: {
            block6: {
                void blockPos$iv;
                IBlock iBlock;
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (this.pos == null) break block6;
                WBlockPos wBlockPos = this.pos;
                if (wBlockPos == null) {
                    Intrinsics.throwNpe();
                }
                WBlockPos wBlockPos2 = wBlockPos;
                IExtractedFunctions iExtractedFunctions = MinecraftInstance.functions;
                boolean $i$f$getBlock = false;
                Object object = MinecraftInstance.mc.getTheWorld();
                IBlock iBlock2 = iBlock = object != null && (object = object.getBlockState((WBlockPos)blockPos$iv)) != null ? object.getBlock() : null;
                if (iBlock2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iExtractedFunctions.getIdFromBlock(iBlock2) != this.targetId) break block6;
                WBlockPos wBlockPos3 = Fucker.INSTANCE.getPos();
                if (wBlockPos3 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(BlockUtils.getCenterDistance(wBlockPos3) > (double)7)) break block7;
            }
            this.pos = Fucker.INSTANCE.find(this.targetId);
        }
    }
}
