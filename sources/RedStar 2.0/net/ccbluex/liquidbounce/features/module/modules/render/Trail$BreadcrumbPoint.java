package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\u0000\n\u0000\n\n\b\n\t\n\u0000\n\b\n\b\n\u000020B-0000\b0\t¢\nR\b0\t¢\b\n\u0000\b\fR0¢\b\n\u0000\b\rR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Trail$BreadcrumbPoint;", "", "x", "", "y", "z", "time", "", "color", "", "(DDDJI)V", "getColor", "()I", "getTime", "()J", "getX", "()D", "getY", "getZ", "Pride"})
public static final class Trail$BreadcrumbPoint {
    private final double x;
    private final double y;
    private final double z;
    private final long time;
    private final int color;

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getZ() {
        return this.z;
    }

    public final long getTime() {
        return this.time;
    }

    public final int getColor() {
        return this.color;
    }

    public Trail$BreadcrumbPoint(double x, double y, double z, long time, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
        this.color = color;
    }
}
