// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.model.geometry;

import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLineSide;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function0;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u001d\b\u0002\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00000\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00000\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000e¨\u0006\u000f" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasCorner;", "", "inverted", "Lkotlin/Function0;", "side", "Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineSide;", "(Ljava/lang/String;ILkotlin/jvm/functions/Function0;Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineSide;)V", "getInverted", "()Lkotlin/jvm/functions/Function0;", "getSide$canvas", "()Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineSide;", "TOP_LEFT", "TOP_RIGHT", "BOTTOM_LEFT", "BOTTOM_RIGHT", "canvas" })
public enum CanvasCorner
{
    @NotNull
    private final Function0<CanvasCorner> inverted;
    @NotNull
    private final CanvasLineSide side;
    
    TOP_LEFT((Function0<? extends CanvasCorner>)CanvasCorner$1.INSTANCE, CanvasLineSide.FIRST), 
    TOP_RIGHT((Function0<? extends CanvasCorner>)CanvasCorner$2.INSTANCE, CanvasLineSide.SECOND), 
    BOTTOM_LEFT((Function0<? extends CanvasCorner>)CanvasCorner$3.INSTANCE, CanvasLineSide.FIRST), 
    BOTTOM_RIGHT((Function0<? extends CanvasCorner>)CanvasCorner$4.INSTANCE, CanvasLineSide.SECOND);
    
    private CanvasCorner(final Function0<? extends CanvasCorner> inverted, final CanvasLineSide side) {
        this.inverted = (Function0<CanvasCorner>)inverted;
        this.side = side;
    }
    
    @NotNull
    public final Function0<CanvasCorner> getInverted() {
        return this.inverted;
    }
    
    @NotNull
    public final CanvasLineSide getSide$canvas() {
        return this.side;
    }
    
    private static final /* synthetic */ CanvasCorner[] $values() {
        return new CanvasCorner[] { CanvasCorner.TOP_LEFT, CanvasCorner.TOP_RIGHT, CanvasCorner.BOTTOM_LEFT, CanvasCorner.BOTTOM_RIGHT };
    }
    
    static {
        $VALUES = $values();
    }
}
