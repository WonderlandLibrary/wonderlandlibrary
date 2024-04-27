package net.ccbluex.liquidbounce.api.minecraft.world.border;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\u0000\n\n\u0000\bf\u000020J020H&¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/world/border/IWorldBorder;", "", "contains", "", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "Pride"})
public interface IWorldBorder {
    public boolean contains(@NotNull WBlockPos var1);
}
