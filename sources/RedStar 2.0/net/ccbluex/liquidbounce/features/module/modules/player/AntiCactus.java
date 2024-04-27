package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AntiCactus", description="Prevents cactuses from damaging you.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J020H¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AntiCactus;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "Pride"})
public final class AntiCactus
extends Module {
    @EventTarget
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (MinecraftInstance.classProvider.isBlockCactus(event.getBlock())) {
            event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(event.getX(), event.getY(), event.getZ(), (double)event.getX() + 1.0, (double)event.getY() + 1.0, (double)event.getZ() + 1.0));
        }
    }
}
