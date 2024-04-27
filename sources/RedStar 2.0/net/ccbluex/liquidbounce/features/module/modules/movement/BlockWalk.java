package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="BlockWalk", description="Allows you to walk on non-fullblock blocks.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\n\b\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J02\b0\tHR0X¢\n\u0000R0X¢\n\u0000¨\n"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/BlockWalk;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "cobwebValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "snowValue", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "Pride"})
public final class BlockWalk
extends Module {
    private final BoolValue cobwebValue = new BoolValue("Cobweb", true);
    private final BoolValue snowValue = new BoolValue("Snow", true);

    @EventTarget
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.cobwebValue.get()).booleanValue() && Intrinsics.areEqual(event.getBlock(), MinecraftInstance.classProvider.getBlockEnum(BlockType.WEB)) || ((Boolean)this.snowValue.get()).booleanValue() && Intrinsics.areEqual(event.getBlock(), MinecraftInstance.classProvider.getBlockEnum(BlockType.SNOW_LAYER))) {
            event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(event.getX(), event.getY(), event.getZ(), (double)event.getX() + 1.0, (double)event.getY() + 1.0, (double)event.getZ() + 1.0));
        }
    }
}
