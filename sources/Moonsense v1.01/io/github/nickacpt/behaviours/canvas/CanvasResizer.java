// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas;

import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction;
import java.util.List;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasCorner;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u00020\u0003B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005¢\u0006\u0002\u0010\u0006J\r\u0010\u0013\u001a\u00020\u0014H\u0000¢\u0006\u0002\b\u0015J\u001b\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0019¢\u0006\u0002\u0010\u001aJ\u0019\u0010\u001b\u001a\u00020\n*\u00028\u00002\u0006\u0010\u001c\u001a\u00020\fH\u0002¢\u0006\u0002\u0010\u001dR\u001d\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\rR\u0015\u0010\u000e\u001a\u00020\n*\u00028\u00008F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0015\u0010\u0011\u001a\u00020\n*\u00028\u00008F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010¨\u0006\u0004" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/CanvasResizer;", "ElementType", "ColorType", "", "canvas", "Lio/github/nickacpt/behaviours/canvas/Canvas;", "(Lio/github/nickacpt/behaviours/canvas/Canvas;)V", "getCanvas", "()Lio/github/nickacpt/behaviours/canvas/Canvas;", "elementRectangleCache", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "elementScaleCache", "", "Ljava/lang/Float;", "resizeHandleRectangle", "getResizeHandleRectangle", "(Ljava/lang/Object;)Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "resizeHandleRenderRectangle", "getResizeHandleRenderRectangle", "notifyStateChanged", "", "notifyStateChanged$canvas", "resize", "element", "mousePosition", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "(Ljava/lang/Object;Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;)V", "computeResizeHandleRect", "size", "(Ljava/lang/Object;F)Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;" })
public final class CanvasResizer<ElementType, ColorType>
{
    @NotNull
    private final Canvas<ElementType, ColorType> canvas;
    @Nullable
    private CanvasRectangle elementRectangleCache;
    @Nullable
    private Float elementScaleCache;
    
    public CanvasResizer(@NotNull final Canvas<ElementType, ColorType> canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        this.canvas = canvas;
    }
    
    @NotNull
    public final Canvas<ElementType, ColorType> getCanvas() {
        return this.canvas;
    }
    
    public final void notifyStateChanged$canvas() {
        this.elementRectangleCache = null;
        this.elementScaleCache = null;
    }
    
    public final void resize(final ElementType element, @NotNull final CanvasPoint mousePosition) {
        Intrinsics.checkNotNullParameter(mousePosition, "mousePosition");
        CanvasRectangle canvasRectangle;
        if ((canvasRectangle = this.elementRectangleCache) == null) {
            final CanvasAbstraction $this$resize_u24lambda_u2d0 = this.canvas.getAbstraction$canvas();
            final int n = 0;
            canvasRectangle = $this$resize_u24lambda_u2d0.getRectangle(element);
        }
        final CanvasRectangle elementRectangle = canvasRectangle;
        final Float elementScaleCache = this.elementScaleCache;
        float n2;
        if (elementScaleCache != null) {
            n2 = elementScaleCache;
        }
        else {
            final CanvasAbstraction $this$resize_u24lambda_u2d2 = this.canvas.getAbstraction$canvas();
            final int n3 = 0;
            n2 = $this$resize_u24lambda_u2d2.getElementScale(element);
        }
        final float elementScale = n2;
        if (this.elementRectangleCache == null) {
            this.elementRectangleCache = elementRectangle;
        }
        if (this.elementScaleCache == null) {
            this.elementScaleCache = elementScale;
        }
        final CanvasAbstraction $this$resize_u24lambda_u2d3 = this.canvas.getAbstraction$canvas();
        final int n4 = 0;
        final CanvasCorner oppositeHandleCorner = $this$resize_u24lambda_u2d3.getResizeHandleCorner(element).getInverted().invoke();
        final CanvasPoint oppositeHandleCornerPoint = elementRectangle.getCorner(oppositeHandleCorner);
        final List xPos = CollectionsKt__CollectionsKt.listOf(new Float[] { oppositeHandleCornerPoint.getX(), mousePosition.getX() });
        final List yPos = CollectionsKt__CollectionsKt.listOf(new Float[] { oppositeHandleCornerPoint.getY(), mousePosition.getY() });
        final CanvasPoint topLeft = new CanvasPoint(CollectionsKt___CollectionsKt.minOrThrow((Iterable<Float>)xPos), CollectionsKt___CollectionsKt.minOrThrow((Iterable<Float>)yPos));
        final CanvasPoint bottomRight = new CanvasPoint(CollectionsKt___CollectionsKt.maxOrThrow((Iterable<Float>)xPos), CollectionsKt___CollectionsKt.maxOrThrow((Iterable<Float>)yPos));
        final CanvasRectangle maxRectangle = new CanvasRectangle(topLeft, bottomRight);
        final float newScale = Math.min(maxRectangle.getHeight() / elementRectangle.getHeight(), maxRectangle.getWidth() / elementRectangle.getWidth());
        final float scale = elementScale * newScale;
        final CanvasAbstraction $this$resize_u24lambda_u2d4 = this.canvas.getAbstraction$canvas();
        final int n5 = 0;
        $this$resize_u24lambda_u2d4.setElementScale(element, scale);
    }
    
    @NotNull
    public final CanvasRectangle getResizeHandleRectangle(final ElementType $this$resizeHandleRectangle) {
        return this.computeResizeHandleRect($this$resizeHandleRectangle, this.canvas.getConfig$canvas().getResizeHandleSize());
    }
    
    @NotNull
    public final CanvasRectangle getResizeHandleRenderRectangle(final ElementType $this$resizeHandleRenderRectangle) {
        return this.computeResizeHandleRect($this$resizeHandleRenderRectangle, this.canvas.getConfig$canvas().getResizeHandleRenderSize());
    }
    
    private final CanvasRectangle computeResizeHandleRect(final ElementType $this$computeResizeHandleRect, final float size) {
        final CanvasAbstraction $this$computeResizeHandleRect_u24lambda_u2d4 = this.canvas.getAbstraction$canvas();
        final int n = 0;
        final CanvasPoint it = $this$computeResizeHandleRect_u24lambda_u2d4.getRectangle($this$computeResizeHandleRect).getCorner($this$computeResizeHandleRect_u24lambda_u2d4.getResizeHandleCorner($this$computeResizeHandleRect));
        final int n2 = 0;
        return new CanvasRectangle(it, it).expand(size / 2.0f);
    }
}
