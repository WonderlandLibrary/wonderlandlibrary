package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\u0000\n\b\n\b\n\n\b\n\n\b\bf\u000020J\b\r0H&J\b0H&J\b0H&J\b0H&J\b0H&R0X¦¢\bR0X¦¢\b\b\tR\n0\u0000X¦¢\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "", "axisOrdinal", "", "getAxisOrdinal", "()I", "directionVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "getDirectionVec", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "opposite", "getOpposite", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "isEast", "", "isNorth", "isSouth", "isUp", "isWest", "Pride"})
public interface IEnumFacing {
    public boolean isNorth();

    public boolean isSouth();

    public boolean isEast();

    public boolean isWest();

    public boolean isUp();

    @NotNull
    public IEnumFacing getOpposite();

    @NotNull
    public WVec3i getDirectionVec();

    public int getAxisOrdinal();
}
