// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.model.geometry;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0011\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u0003H\u0086\u0002J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\u0011\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0000H\u0086\u0002J\u0011\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0000H\u0086\u0002J\u0011\u0010\u0018\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u0003H\u0086\u0002J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t¨\u0006\u001b" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "", "x", "", "y", "(FF)V", "getX", "()F", "setX", "(F)V", "getY", "setY", "component1", "component2", "copy", "div", "factor", "equals", "", "other", "hashCode", "", "minus", "plus", "times", "toString", "", "canvas" })
public final class CanvasPoint
{
    private float x;
    private float y;
    
    public CanvasPoint(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public final float getX() {
        return this.x;
    }
    
    public final void setX(final float <set-?>) {
        this.x = <set-?>;
    }
    
    public final float getY() {
        return this.y;
    }
    
    public final void setY(final float <set-?>) {
        this.y = <set-?>;
    }
    
    @NotNull
    public final CanvasPoint plus(@NotNull final CanvasPoint other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return new CanvasPoint(this.x + other.x, this.y + other.y);
    }
    
    @NotNull
    public final CanvasPoint minus(@NotNull final CanvasPoint other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return new CanvasPoint(this.x - other.x, this.y - other.y);
    }
    
    @NotNull
    public final CanvasPoint times(final float factor) {
        return new CanvasPoint(this.x * factor, this.y * factor);
    }
    
    @NotNull
    public final CanvasPoint div(final float factor) {
        return new CanvasPoint(this.x / factor, this.y / factor);
    }
    
    public final float component1() {
        return this.x;
    }
    
    public final float component2() {
        return this.y;
    }
    
    @NotNull
    public final CanvasPoint copy(final float x, final float y) {
        return new CanvasPoint(x, y);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "CanvasPoint(x=" + this.x + ", y=" + this.y + ')';
    }
    
    @Override
    public int hashCode() {
        int result = Float.hashCode(this.x);
        result = result * 31 + Float.hashCode(this.y);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CanvasPoint)) {
            return false;
        }
        final CanvasPoint canvasPoint = (CanvasPoint)other;
        return Intrinsics.areEqual(this.x, (Object)canvasPoint.x) && Intrinsics.areEqual(this.y, (Object)canvasPoint.y);
    }
}
