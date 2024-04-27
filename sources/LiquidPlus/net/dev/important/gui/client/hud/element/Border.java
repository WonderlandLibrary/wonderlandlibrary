/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.gui.client.hud.element;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0006\u0010\u0012\u001a\u00020\u0013J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\u001b"}, d2={"Lnet/dev/important/gui/client/hud/element/Border;", "", "x", "", "y", "x2", "y2", "(FFFF)V", "getX", "()F", "getX2", "getY", "getY2", "component1", "component2", "component3", "component4", "copy", "draw", "", "equals", "", "other", "hashCode", "", "toString", "", "LiquidBounce"})
public final class Border {
    private final float x;
    private final float y;
    private final float x2;
    private final float y2;

    public Border(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final float getX2() {
        return this.x2;
    }

    public final float getY2() {
        return this.y2;
    }

    public final void draw() {
        RenderUtils.drawBorderedRect(this.x, this.y, this.x2, this.y2, 1.0f, Integer.MIN_VALUE, 0);
    }

    public final float component1() {
        return this.x;
    }

    public final float component2() {
        return this.y;
    }

    public final float component3() {
        return this.x2;
    }

    public final float component4() {
        return this.y2;
    }

    @NotNull
    public final Border copy(float x, float y, float x2, float y2) {
        return new Border(x, y, x2, y2);
    }

    public static /* synthetic */ Border copy$default(Border border, float f, float f2, float f3, float f4, int n, Object object) {
        if ((n & 1) != 0) {
            f = border.x;
        }
        if ((n & 2) != 0) {
            f2 = border.y;
        }
        if ((n & 4) != 0) {
            f3 = border.x2;
        }
        if ((n & 8) != 0) {
            f4 = border.y2;
        }
        return border.copy(f, f2, f3, f4);
    }

    @NotNull
    public String toString() {
        return "Border(x=" + this.x + ", y=" + this.y + ", x2=" + this.x2 + ", y2=" + this.y2 + ')';
    }

    public int hashCode() {
        int result = Float.hashCode(this.x);
        result = result * 31 + Float.hashCode(this.y);
        result = result * 31 + Float.hashCode(this.x2);
        result = result * 31 + Float.hashCode(this.y2);
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Border)) {
            return false;
        }
        Border border = (Border)other;
        if (!Intrinsics.areEqual((Object)Float.valueOf(this.x), (Object)Float.valueOf(border.x))) {
            return false;
        }
        if (!Intrinsics.areEqual((Object)Float.valueOf(this.y), (Object)Float.valueOf(border.y))) {
            return false;
        }
        if (!Intrinsics.areEqual((Object)Float.valueOf(this.x2), (Object)Float.valueOf(border.x2))) {
            return false;
        }
        return Intrinsics.areEqual((Object)Float.valueOf(this.y2), (Object)Float.valueOf(border.y2));
    }
}

