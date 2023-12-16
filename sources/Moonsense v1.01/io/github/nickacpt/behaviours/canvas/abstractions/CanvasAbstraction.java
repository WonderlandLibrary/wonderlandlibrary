// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.abstractions;

import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasCorner;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000<\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003J-\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00028\u00012\u0006\u0010\u0012\u001a\u00020\u0013H&¢\u0006\u0002\u0010\u0014J-\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00028\u00012\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0012\u001a\u00020\u0013H&¢\u0006\u0002\u0010\u0019R\u0012\u0010\u0004\u001a\u00020\u0005X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0018\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tX¦\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u001a" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction;", "ElementType", "ColorType", "Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasElementAbstraction;", "canvasRectangle", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "getCanvasRectangle", "()Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "elements", "", "getElements", "()Ljava/util/Collection;", "drawLine", "", "start", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "end", "color", "lineWidth", "", "(Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;Ljava/lang/Object;F)V", "drawRectangle", "rectangle", "hollow", "", "(Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;Ljava/lang/Object;ZF)V", "canvas" })
public interface CanvasAbstraction<ElementType, ColorType> extends CanvasElementAbstraction<ElementType>
{
    @NotNull
    CanvasRectangle getCanvasRectangle();
    
    @NotNull
    Collection<ElementType> getElements();
    
    void drawLine(@NotNull final CanvasPoint start, @NotNull final CanvasPoint end, final ColorType color, final float lineWidth);
    
    void drawRectangle(@NotNull final CanvasRectangle rectangle, final ColorType color, final boolean hollow, final float lineWidth);
    
    @Metadata(mv = { 1, 7, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
        @NotNull
        public static <ElementType, ColorType> CanvasCorner getResizeHandleCorner(@NotNull final CanvasAbstraction<ElementType, ColorType> $this, final ElementType $receiver) {
            return CanvasElementAbstraction.DefaultImpls.getResizeHandleCorner($this, $receiver);
        }
    }
}
