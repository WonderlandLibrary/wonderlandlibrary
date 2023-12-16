// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas;

import io.github.nickacpt.behaviours.canvas.config.CanvasResizeHandleVisibility;
import kotlin.NoWhenBranchMatchedException;
import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction;
import java.util.Iterator;
import java.util.Collection;
import io.github.nickacpt.behaviours.canvas.config.CanvasColorStyle;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000B\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u00020\u0003B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005¢\u0006\u0002\u0010\u0006J3\u0010\t\u001a\u00028\u00012\u0006\u0010\n\u001a\u00028\u00002\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00010\u0010H\u0002¢\u0006\u0002\u0010\u0011J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\r\u001a\u00020\u000eJ\u001e\u0010\u0014\u001a\u00020\u00132\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u00162\u0006\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u0017\u001a\u00020\u0013H\u0002J(\u0010\u0018\u001a\u00020\u00192\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\fH\u0002J \u0010\u001d\u001a\u00020\u0013*\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00052\u0006\u0010\u001b\u001a\u00020\fH\u0002R\u001d\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u0004" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/CanvasRenderer;", "ElementType", "ColorType", "", "canvas", "Lio/github/nickacpt/behaviours/canvas/Canvas;", "(Lio/github/nickacpt/behaviours/canvas/Canvas;)V", "getCanvas", "()Lio/github/nickacpt/behaviours/canvas/Canvas;", "getBackgroundColorToUse", "element", "rect", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "mousePosition", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "bgColor", "Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;", "(Ljava/lang/Object;Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;)Ljava/lang/Object;", "renderBackground", "", "renderElementDecorators", "canvasElements", "", "renderSafeZone", "shouldRenderResizeHandle", "", "elementRect", "resizeHandleRectangle", "resizeHandleRenderRectangle", "renderResizeHandleDecorator" })
public final class CanvasRenderer<ElementType, ColorType>
{
    @NotNull
    private final Canvas<ElementType, ColorType> canvas;
    
    public CanvasRenderer(@NotNull final Canvas<ElementType, ColorType> canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        this.canvas = canvas;
    }
    
    @NotNull
    public final Canvas<ElementType, ColorType> getCanvas() {
        return this.canvas;
    }
    
    private final ColorType getBackgroundColorToUse(final ElementType element, final CanvasRectangle rect, final CanvasPoint mousePosition, final CanvasColorStyle<ColorType> bgColor) {
        final boolean containsMouse = rect.contains(mousePosition);
        ColorType colorType;
        if (this.canvas.getState$canvas().getSelectedElements().contains(element)) {
            colorType = bgColor.getSelected();
        }
        else if (containsMouse && this.canvas.getState$canvas().getMouseDown()) {
            colorType = bgColor.getMouseDown();
        }
        else {
            colorType = (containsMouse ? bgColor.getHover() : bgColor.getNormal());
            if (colorType == null) {
                colorType = bgColor.getNormal();
            }
        }
        return colorType;
    }
    
    private final void renderSafeZone() {
        final Canvas $this$renderSafeZone_u24lambda_u2d0 = this.canvas;
        final int n = 0;
        final Object selectionBackground = $this$renderSafeZone_u24lambda_u2d0.getConfig$canvas().getColors().getSelectionBackground();
        if (selectionBackground == null) {
            return;
        }
        final Object color = selectionBackground;
        final CanvasRectangle safeZone = $this$renderSafeZone_u24lambda_u2d0.getAbstraction$canvas().getCanvasRectangle().expand(-$this$renderSafeZone_u24lambda_u2d0.getConfig$canvas().getSafeZoneSize());
        $this$renderSafeZone_u24lambda_u2d0.getAbstraction$canvas().drawRectangle(safeZone, color, true, $this$renderSafeZone_u24lambda_u2d0.getConfig$canvas().getSnapLineWidth());
    }
    
    private final void renderResizeHandleDecorator(final Canvas<ElementType, ColorType> $this$renderResizeHandleDecorator, final CanvasRectangle resizeHandleRectangle) {
        final ColorType resizeHandleBackground = $this$renderResizeHandleDecorator.getConfig$canvas().getColors().getResizeHandleBackground();
        if (resizeHandleBackground != null) {
            final Object handleBackground = resizeHandleBackground;
            final int n = 0;
            $this$renderResizeHandleDecorator.getAbstraction$canvas().drawRectangle(resizeHandleRectangle, (ColorType)handleBackground, false, 0.0f);
        }
        final ColorType resizeHandleBorder = $this$renderResizeHandleDecorator.getConfig$canvas().getColors().getResizeHandleBorder();
        if (resizeHandleBorder != null) {
            final Object handleBorder = resizeHandleBorder;
            final int n2 = 0;
            $this$renderResizeHandleDecorator.getAbstraction$canvas().drawRectangle(resizeHandleRectangle, (ColorType)handleBorder, true, $this$renderResizeHandleDecorator.getConfig$canvas().getResizeHandleBorderWidth());
        }
    }
    
    private final void renderElementDecorators(final Collection<? extends ElementType> canvasElements, final CanvasPoint mousePosition) {
        final Canvas $this$renderElementDecorators_u24lambda_u2d8 = this.canvas;
        final int n = 0;
        for (final Object element : canvasElements) {
            final CanvasAbstraction $this$renderElementDecorators_u24lambda_u2d8_u24lambda_u2d3 = $this$renderElementDecorators_u24lambda_u2d8.getAbstraction$canvas();
            final int n2 = 0;
            final CanvasRectangle elementRectangle = $this$renderElementDecorators_u24lambda_u2d8_u24lambda_u2d3.getRectangle(element);
            final CanvasColorStyle elementBackground = $this$renderElementDecorators_u24lambda_u2d8.getConfig$canvas().getColors().getElementBackground();
            Object backgroundColorToUse;
            if (elementBackground != null) {
                final CanvasColorStyle it = elementBackground;
                final int n3 = 0;
                backgroundColorToUse = this.getBackgroundColorToUse(element, elementRectangle, mousePosition, it);
            }
            else {
                backgroundColorToUse = null;
            }
            final Object backgroundColor = backgroundColorToUse;
            if (backgroundColor != null) {
                $this$renderElementDecorators_u24lambda_u2d8.getAbstraction$canvas().drawRectangle(elementRectangle, backgroundColor, false, 0.0f);
            }
            final CanvasColorStyle elementBorder = $this$renderElementDecorators_u24lambda_u2d8.getConfig$canvas().getColors().getElementBorder();
            Object backgroundColorToUse2;
            if (elementBorder != null) {
                final CanvasColorStyle it2 = elementBorder;
                final int n4 = 0;
                backgroundColorToUse2 = this.getBackgroundColorToUse(element, elementRectangle, mousePosition, it2);
            }
            else {
                backgroundColorToUse2 = null;
            }
            final Object borderColor = backgroundColorToUse2;
            if (borderColor != null) {
                $this$renderElementDecorators_u24lambda_u2d8.getAbstraction$canvas().drawRectangle(elementRectangle, borderColor, true, $this$renderElementDecorators_u24lambda_u2d8.getConfig$canvas().getBorderWidth());
            }
            final CanvasResizer $this$renderElementDecorators_u24lambda_u2d8_u24lambda_u2d4 = $this$renderElementDecorators_u24lambda_u2d8.getResizer$canvas();
            final int n5 = 0;
            final CanvasRectangle elementResizeRenderRect = $this$renderElementDecorators_u24lambda_u2d8_u24lambda_u2d4.getResizeHandleRenderRectangle(element);
            final CanvasResizer $this$renderElementDecorators_u24lambda_u2d8_u24lambda_u2d5 = $this$renderElementDecorators_u24lambda_u2d8.getResizer$canvas();
            final int n6 = 0;
            final CanvasRectangle elementResizeRect = $this$renderElementDecorators_u24lambda_u2d8_u24lambda_u2d5.getResizeHandleRectangle(element);
            if (this.shouldRenderResizeHandle(mousePosition, elementRectangle, elementResizeRect, elementResizeRenderRect)) {
                this.renderResizeHandleDecorator($this$renderElementDecorators_u24lambda_u2d8, elementResizeRect);
            }
        }
    }
    
    private final boolean shouldRenderResizeHandle(final CanvasPoint mousePosition, final CanvasRectangle elementRect, final CanvasRectangle resizeHandleRectangle, final CanvasRectangle resizeHandleRenderRectangle) {
        if (this.canvas.getState$canvas().getSelectedElements().size() > 1) {
            return false;
        }
        final Canvas $this$shouldRenderResizeHandle_u24lambda_u2d9 = this.canvas;
        final int n = 0;
        boolean contains = false;
        switch (WhenMappings.$EnumSwitchMapping$0[$this$shouldRenderResizeHandle_u24lambda_u2d9.getConfig$canvas().getResizeHandleVisibility().ordinal()]) {
            case 1: {
                contains = false;
                break;
            }
            case 2: {
                contains = (elementRect.contains(mousePosition) || resizeHandleRectangle.contains(mousePosition));
                break;
            }
            case 3: {
                contains = resizeHandleRenderRectangle.contains(mousePosition);
                break;
            }
            case 4: {
                contains = true;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return contains;
    }
    
    public final void renderBackground(@NotNull final CanvasPoint mousePosition) {
        Intrinsics.checkNotNullParameter(mousePosition, "mousePosition");
        final Canvas $this$renderBackground_u24lambda_u2d10 = this.canvas;
        final int n = 0;
        this.renderSafeZone();
        this.renderElementDecorators($this$renderBackground_u24lambda_u2d10.getAbstraction$canvas().getElements(), mousePosition);
    }
}
