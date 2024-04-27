package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\u0000\n\n\b\u000020B\b0\b0¢R0¢\b\n\u0000\b\bR0¢\b\n\u0000\b\t\n¨"}, d2={"Lnet/ccbluex/liquidbounce/event/ClickBlockEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "clickedBlock", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "WEnumFacing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;)V", "getWEnumFacing", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getClickedBlock", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "Pride"})
public final class ClickBlockEvent
extends Event {
    @Nullable
    private final WBlockPos clickedBlock;
    @Nullable
    private final IEnumFacing WEnumFacing;

    @Nullable
    public final WBlockPos getClickedBlock() {
        return this.clickedBlock;
    }

    @Nullable
    public final IEnumFacing getWEnumFacing() {
        return this.WEnumFacing;
    }

    public ClickBlockEvent(@Nullable WBlockPos clickedBlock, @Nullable IEnumFacing WEnumFacing) {
        this.clickedBlock = clickedBlock;
        this.WEnumFacing = WEnumFacing;
    }
}
