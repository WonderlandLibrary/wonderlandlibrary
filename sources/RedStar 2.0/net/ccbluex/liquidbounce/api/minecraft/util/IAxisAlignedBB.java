package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\u0000\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\b\b\bf\u000020J 0\u0000202020H&J02020H&J 0\u0000202020H&J020\u0000H&J020H&J 0\u00002 02!02\"0H&R0X¦¢\bR0X¦¢\bR\b0X¦¢\b\tR\n0X¦¢\bR\f0X¦¢\b\rR0X¦¢\b¨#"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "", "maxX", "", "getMaxX", "()D", "maxY", "getMaxY", "maxZ", "getMaxZ", "minX", "getMinX", "minY", "getMinY", "minZ", "getMinZ", "addCoord", "x", "y", "z", "calculateIntercept", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "from", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "to", "expand", "intersectsWith", "", "boundingBox", "isVecInside", "vec", "offset", "sx", "sy", "sz", "Pride"})
public interface IAxisAlignedBB {
    @NotNull
    public IAxisAlignedBB addCoord(double var1, double var3, double var5);

    @NotNull
    public IAxisAlignedBB expand(double var1, double var3, double var5);

    @Nullable
    public IMovingObjectPosition calculateIntercept(@NotNull WVec3 var1, @NotNull WVec3 var2);

    public boolean isVecInside(@NotNull WVec3 var1);

    @NotNull
    public IAxisAlignedBB offset(double var1, double var3, double var5);

    public boolean intersectsWith(@NotNull IAxisAlignedBB var1);

    public double getMinX();

    public double getMinY();

    public double getMinZ();

    public double getMaxX();

    public double getMaxY();

    public double getMaxZ();
}
