// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.model.geometry.line;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0007H\u00c6\u0003J'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLine;", "", "value", "", "direction", "Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineDirection;", "side", "Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineSide;", "(FLio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineDirection;Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineSide;)V", "getDirection", "()Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineDirection;", "getSide", "()Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineSide;", "getValue", "()F", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "canvas" })
public final class CanvasLine
{
    private final float value;
    @NotNull
    private final CanvasLineDirection direction;
    @NotNull
    private final CanvasLineSide side;
    
    public CanvasLine(final float value, @NotNull final CanvasLineDirection direction, @NotNull final CanvasLineSide side) {
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(side, "side");
        this.value = value;
        this.direction = direction;
        this.side = side;
    }
    
    public final float getValue() {
        return this.value;
    }
    
    @NotNull
    public final CanvasLineDirection getDirection() {
        return this.direction;
    }
    
    @NotNull
    public final CanvasLineSide getSide() {
        return this.side;
    }
    
    public final float component1() {
        return this.value;
    }
    
    @NotNull
    public final CanvasLineDirection component2() {
        return this.direction;
    }
    
    @NotNull
    public final CanvasLineSide component3() {
        return this.side;
    }
    
    @NotNull
    public final CanvasLine copy(final float value, @NotNull final CanvasLineDirection direction, @NotNull final CanvasLineSide side) {
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(side, "side");
        return new CanvasLine(value, direction, side);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "CanvasLine(value=" + this.value + ", direction=" + this.direction + ", side=" + this.side + ')';
    }
    
    @Override
    public int hashCode() {
        int result = Float.hashCode(this.value);
        result = result * 31 + this.direction.hashCode();
        result = result * 31 + this.side.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CanvasLine)) {
            return false;
        }
        final CanvasLine canvasLine = (CanvasLine)other;
        return Intrinsics.areEqual(this.value, (Object)canvasLine.value) && this.direction == canvasLine.direction && this.side == canvasLine.side;
    }
}
