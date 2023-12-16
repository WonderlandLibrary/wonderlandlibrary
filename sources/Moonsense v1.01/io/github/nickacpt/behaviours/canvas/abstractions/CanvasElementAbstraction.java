// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.abstractions;

import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasCorner;
import org.jetbrains.annotations.NotNull;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J\u0019\u0010\u0011\u001a\u00020\u0012*\u00028\u00002\u0006\u0010\u0013\u001a\u00020\u0014H&¢\u0006\u0002\u0010\u0015R\u001c\u0010\u0003\u001a\u00020\u0004*\u00028\u0000X¦\u000e¢\u0006\f\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0016\u0010\t\u001a\u00020\n*\u00028\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0018\u0010\r\u001a\u00020\u000e*\u00028\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0016" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasElementAbstraction;", "ElementType", "", "elementScale", "", "getElementScale", "(Ljava/lang/Object;)F", "setElementScale", "(Ljava/lang/Object;F)V", "rectangle", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "getRectangle", "(Ljava/lang/Object;)Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "resizeHandleCorner", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasCorner;", "getResizeHandleCorner", "(Ljava/lang/Object;)Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasCorner;", "moveTo", "", "point", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "(Ljava/lang/Object;Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;)V", "canvas" })
public interface CanvasElementAbstraction<ElementType>
{
    @NotNull
    CanvasRectangle getRectangle(final ElementType $this$rectangle);
    
    float getElementScale(final ElementType $this$elementScale);
    
    void setElementScale(final ElementType $this$elementScale, final float <set-?>);
    
    @NotNull
    CanvasCorner getResizeHandleCorner(final ElementType $this$resizeHandleCorner);
    
    void moveTo(final ElementType $this$moveTo, @NotNull final CanvasPoint point);
    
    @Metadata(mv = { 1, 7, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
        @NotNull
        public static <ElementType> CanvasCorner getResizeHandleCorner(@NotNull final CanvasElementAbstraction<ElementType> $this, final ElementType $receiver) {
            return CanvasCorner.BOTTOM_RIGHT;
        }
    }
}
