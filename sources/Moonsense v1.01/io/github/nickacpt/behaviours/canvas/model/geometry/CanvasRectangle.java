// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.model.geometry;

import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLineSide;
import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLineDirection;
import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLine;
import java.util.List;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0003J\u001d\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00030\"H\u0000¢\u0006\u0002\b#J\u0013\u0010$\u001a\u00020\u001e2\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u000e\u0010&\u001a\u00020\u00002\u0006\u0010'\u001a\u00020\u0007J\u000e\u0010(\u001a\u00020\u00032\u0006\u0010)\u001a\u00020*J\u001d\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0000¢\u0006\u0002\b0J\t\u00101\u001a\u000202H\u00d6\u0001J\u001d\u00103\u001a\b\u0012\u0004\u0012\u0002040\"2\b\b\u0002\u00105\u001a\u00020\u001eH\u0000¢\u0006\u0002\b6J\t\u00107\u001a\u000208H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u000e\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\tR\u0011\u0010\u0010\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\tR\u0011\u0010\u0012\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\tR\u0011\u0010\u0014\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\fR\u0011\u0010\u0017\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\fR\u0011\u0010\u0019\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u001a\u0010\t¨\u00069" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "", "topLeft", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "bottomRight", "(Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;)V", "bottom", "", "getBottom", "()F", "bottomLeft", "getBottomLeft", "()Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "getBottomRight", "height", "getHeight", "left", "getLeft", "right", "getRight", "top", "getTop", "getTopLeft", "topRight", "getTopRight", "width", "getWidth", "component1", "component2", "contains", "", "point", "copy", "corners", "", "corners$canvas", "equals", "other", "expand", "amount", "getCorner", "corner", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasCorner;", "getSideValue", "direction", "Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineDirection;", "side", "Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineSide;", "getSideValue$canvas", "hashCode", "", "sides", "Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLine;", "withMiddle", "sides$canvas", "toString", "", "canvas" })
public final class CanvasRectangle
{
    @NotNull
    private final CanvasPoint topLeft;
    @NotNull
    private final CanvasPoint bottomRight;
    
    public CanvasRectangle(@NotNull final CanvasPoint topLeft, @NotNull final CanvasPoint bottomRight) {
        Intrinsics.checkNotNullParameter(topLeft, "topLeft");
        Intrinsics.checkNotNullParameter(bottomRight, "bottomRight");
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }
    
    @NotNull
    public final CanvasPoint getTopLeft() {
        return this.topLeft;
    }
    
    @NotNull
    public final CanvasPoint getBottomRight() {
        return this.bottomRight;
    }
    
    public final float getTop() {
        return this.topLeft.getY();
    }
    
    public final float getLeft() {
        return this.topLeft.getX();
    }
    
    public final float getRight() {
        return this.bottomRight.getX();
    }
    
    public final float getBottom() {
        return this.bottomRight.getY();
    }
    
    public final float getWidth() {
        return this.getRight() - this.getLeft();
    }
    
    public final float getHeight() {
        return this.getBottom() - this.getTop();
    }
    
    public final boolean contains(@NotNull final CanvasPoint point) {
        Intrinsics.checkNotNullParameter(point, "point");
        final float left = this.getLeft();
        final float right = this.getRight();
        final float x = point.getX();
        if (left <= x && x <= right) {
            final float top = this.getTop();
            final float bottom = this.getBottom();
            final float y = point.getY();
            if (top <= y && y <= bottom) {
                return true;
            }
        }
        return false;
    }
    
    @NotNull
    public final CanvasRectangle expand(final float amount) {
        return new CanvasRectangle(new CanvasPoint(this.getLeft() - amount, this.getTop() - amount), new CanvasPoint(this.getRight() + amount, this.getBottom() + amount));
    }
    
    @NotNull
    public final CanvasPoint getTopRight() {
        return new CanvasPoint(this.getRight(), this.getTop());
    }
    
    @NotNull
    public final CanvasPoint getBottomLeft() {
        return new CanvasPoint(this.getLeft(), this.getBottom());
    }
    
    @NotNull
    public final CanvasPoint getCorner(@NotNull final CanvasCorner corner) {
        Intrinsics.checkNotNullParameter(corner, "corner");
        CanvasPoint canvasPoint = null;
        switch (WhenMappings.$EnumSwitchMapping$0[corner.ordinal()]) {
            case 1: {
                canvasPoint = this.topLeft;
                break;
            }
            case 2: {
                canvasPoint = this.getTopRight();
                break;
            }
            case 3: {
                canvasPoint = this.getBottomLeft();
                break;
            }
            case 4: {
                canvasPoint = this.bottomRight;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return canvasPoint;
    }
    
    @NotNull
    public final List<CanvasPoint> corners$canvas() {
        return CollectionsKt__CollectionsKt.listOf(new CanvasPoint[] { this.topLeft, this.getTopRight(), this.getBottomLeft(), this.bottomRight });
    }
    
    @NotNull
    public final List<CanvasLine> sides$canvas(final boolean withMiddle) {
        return CollectionsKt___CollectionsKt.plus((Collection<? extends CanvasLine>)CollectionsKt__CollectionsKt.listOf(new CanvasLine[] { new CanvasLine(this.getTop(), CanvasLineDirection.HORIZONTAL, CanvasLineSide.FIRST), new CanvasLine(this.getBottom(), CanvasLineDirection.HORIZONTAL, CanvasLineSide.SECOND), new CanvasLine(this.getLeft(), CanvasLineDirection.VERTICAL, CanvasLineSide.FIRST), new CanvasLine(this.getRight(), CanvasLineDirection.VERTICAL, CanvasLineSide.SECOND) }), (Iterable<? extends CanvasLine>)(withMiddle ? CollectionsKt__CollectionsKt.listOf(new CanvasLine[] { new CanvasLine(this.getTop() + this.getHeight() / 2, CanvasLineDirection.HORIZONTAL, CanvasLineSide.MIDDLE), new CanvasLine(this.getLeft() + this.getWidth() / 2, CanvasLineDirection.VERTICAL, CanvasLineSide.MIDDLE) }) : CollectionsKt__CollectionsKt.emptyList()));
    }
    
    public final float getSideValue$canvas(@NotNull final CanvasLineDirection direction, @NotNull final CanvasLineSide side) {
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(side, "side");
        float n = 0.0f;
        Label_0197: {
            switch (WhenMappings.$EnumSwitchMapping$2[direction.ordinal()]) {
                case 1: {
                    switch (WhenMappings.$EnumSwitchMapping$1[side.ordinal()]) {
                        case 1: {
                            n = this.getTop();
                            break Label_0197;
                        }
                        case 2: {
                            n = this.getBottom();
                            break Label_0197;
                        }
                        case 3: {
                            n = this.getTop() + this.getHeight() / 2;
                            break Label_0197;
                        }
                        default: {
                            throw new NoWhenBranchMatchedException();
                        }
                    }
                    break;
                }
                case 2: {
                    switch (WhenMappings.$EnumSwitchMapping$1[side.ordinal()]) {
                        case 1: {
                            n = this.getLeft();
                            break Label_0197;
                        }
                        case 2: {
                            n = this.getRight();
                            break Label_0197;
                        }
                        case 3: {
                            n = this.getLeft() + this.getWidth() / 2;
                            break Label_0197;
                        }
                        default: {
                            throw new NoWhenBranchMatchedException();
                        }
                    }
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
        }
        return n;
    }
    
    @NotNull
    public final CanvasPoint component1() {
        return this.topLeft;
    }
    
    @NotNull
    public final CanvasPoint component2() {
        return this.bottomRight;
    }
    
    @NotNull
    public final CanvasRectangle copy(@NotNull final CanvasPoint topLeft, @NotNull final CanvasPoint bottomRight) {
        Intrinsics.checkNotNullParameter(topLeft, "topLeft");
        Intrinsics.checkNotNullParameter(bottomRight, "bottomRight");
        return new CanvasRectangle(topLeft, bottomRight);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "CanvasRectangle(topLeft=" + this.topLeft + ", bottomRight=" + this.bottomRight + ')';
    }
    
    @Override
    public int hashCode() {
        int result = this.topLeft.hashCode();
        result = result * 31 + this.bottomRight.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CanvasRectangle)) {
            return false;
        }
        final CanvasRectangle canvasRectangle = (CanvasRectangle)other;
        return Intrinsics.areEqual(this.topLeft, canvasRectangle.topLeft) && Intrinsics.areEqual(this.bottomRight, canvasRectangle.bottomRight);
    }
}
