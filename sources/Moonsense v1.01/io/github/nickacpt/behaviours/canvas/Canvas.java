// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas;

import io.github.nickacpt.behaviours.canvas.config.CanvasResizeHandleVisibility;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle;
import kotlin.Unit;
import io.github.nickacpt.behaviours.canvas.model.CanvasAction;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import io.github.nickacpt.behaviours.canvas.model.CanvasState;
import io.github.nickacpt.behaviours.canvas.config.CanvasConfig;
import org.jetbrains.annotations.NotNull;
import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000N\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u00020\u0003B'\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010!\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010\"\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u000e\u0010#\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010$\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010%\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u0010\u0010&\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010'\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002R \u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001a\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0007X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0010X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u00148@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u001aX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c¨\u0006(" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/Canvas;", "ElementType", "ColorType", "", "abstraction", "Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction;", "config", "Lio/github/nickacpt/behaviours/canvas/config/CanvasConfig;", "(Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction;Lio/github/nickacpt/behaviours/canvas/config/CanvasConfig;)V", "getAbstraction$canvas", "()Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction;", "getConfig$canvas", "()Lio/github/nickacpt/behaviours/canvas/config/CanvasConfig;", "renderer", "Lio/github/nickacpt/behaviours/canvas/CanvasRenderer;", "resizer", "Lio/github/nickacpt/behaviours/canvas/CanvasResizer;", "getResizer$canvas", "()Lio/github/nickacpt/behaviours/canvas/CanvasResizer;", "safeZoneRectangle", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "getSafeZoneRectangle$canvas", "()Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "snapper", "Lio/github/nickacpt/behaviours/canvas/CanvasSnapper;", "state", "Lio/github/nickacpt/behaviours/canvas/model/CanvasState;", "getState$canvas", "()Lio/github/nickacpt/behaviours/canvas/model/CanvasState;", "computeCurrentAction", "", "mousePosition", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "handleAction", "moveElements", "onMouseDown", "onMouseUp", "onRender", "resizeElements", "selectElements", "canvas" })
public final class Canvas<ElementType, ColorType>
{
    @NotNull
    private final CanvasAbstraction<ElementType, ColorType> abstraction;
    @NotNull
    private final CanvasConfig<ColorType> config;
    @NotNull
    private final CanvasState<ElementType> state;
    @NotNull
    private final CanvasResizer<ElementType, ColorType> resizer;
    @NotNull
    private final CanvasRenderer<ElementType, ColorType> renderer;
    @NotNull
    private final CanvasSnapper<ElementType, ColorType> snapper;
    
    public Canvas(@NotNull final CanvasAbstraction<ElementType, ColorType> abstraction, @NotNull final CanvasConfig<ColorType> config) {
        Intrinsics.checkNotNullParameter(abstraction, "abstraction");
        Intrinsics.checkNotNullParameter(config, "config");
        this.abstraction = abstraction;
        this.config = config;
        this.state = new CanvasState<ElementType>((Function1<? super CanvasAction, Unit>)new Canvas$state.Canvas$state$1(this));
        this.resizer = new CanvasResizer<ElementType, ColorType>(this);
        this.renderer = new CanvasRenderer<ElementType, ColorType>(this);
        this.snapper = new CanvasSnapper<ElementType, ColorType>(this);
    }
    
    @NotNull
    public final CanvasAbstraction<ElementType, ColorType> getAbstraction$canvas() {
        return this.abstraction;
    }
    
    @NotNull
    public final CanvasConfig<ColorType> getConfig$canvas() {
        return this.config;
    }
    
    @NotNull
    public final CanvasState<ElementType> getState$canvas() {
        return this.state;
    }
    
    @NotNull
    public final CanvasResizer<ElementType, ColorType> getResizer$canvas() {
        return this.resizer;
    }
    
    @NotNull
    public final CanvasRectangle getSafeZoneRectangle$canvas() {
        return this.abstraction.getCanvasRectangle().expand(-this.config.getSafeZoneSize());
    }
    
    public final void onRender(@NotNull final CanvasPoint mousePosition) {
        Intrinsics.checkNotNullParameter(mousePosition, "mousePosition");
        this.renderer.renderBackground(mousePosition);
        this.handleAction(mousePosition);
        final CanvasPoint $this$onRender_u24lambda_u2d0 = this.state.getLastRenderMousePosition();
        final int n = 0;
        $this$onRender_u24lambda_u2d0.setX(mousePosition.getX());
        $this$onRender_u24lambda_u2d0.setY(mousePosition.getY());
    }
    
    private final void handleAction(final CanvasPoint mousePosition) {
        switch (WhenMappings.$EnumSwitchMapping$0[this.state.getCurrentAction().ordinal()]) {
            case 1: {
                this.moveElements(mousePosition);
                break;
            }
            case 2: {
                this.selectElements(mousePosition);
                break;
            }
            case 3: {
                this.resizeElements(mousePosition);
                break;
            }
            case 4: {
                break;
            }
            default: {
                throw new IllegalStateException("Unknown action: " + this.state.getCurrentAction());
            }
        }
    }
    
    private final void selectElements(final CanvasPoint mousePosition) {
        final CanvasPoint mouseDownPosition = this.state.getMouseDownPosition();
        if (mouseDownPosition == null) {
            return;
        }
        final CanvasPoint mouseDownPos = mouseDownPosition;
        final float topLeftX = Math.min(mouseDownPos.getX(), mousePosition.getX());
        final float topLeftY = Math.min(mouseDownPos.getY(), mousePosition.getY());
        final float bottomRightX = Math.max(mouseDownPos.getX(), mousePosition.getX());
        final float bottomRightY = Math.max(mouseDownPos.getY(), mousePosition.getY());
        final CanvasRectangle selectionRectangle = new CanvasRectangle(new CanvasPoint(topLeftX, topLeftY), new CanvasPoint(bottomRightX, bottomRightY));
        final ColorType selectionBackground = this.config.getColors().getSelectionBackground();
        if (selectionBackground != null) {
            final Object it = selectionBackground;
            final int n = 0;
            this.abstraction.drawRectangle(selectionRectangle, (ColorType)it, false, 0.0f);
        }
        final ColorType selectionBorder = this.config.getColors().getSelectionBorder();
        if (selectionBorder != null) {
            final Object it = selectionBorder;
            final int n2 = 0;
            this.abstraction.drawRectangle(selectionRectangle, (ColorType)it, true, 1.0f);
        }
        for (final Object element : this.abstraction.getElements()) {
            final CanvasAbstraction $this$selectElements_u24lambda_u2d3 = this.abstraction;
            final int n3 = 0;
            final List corners = $this$selectElements_u24lambda_u2d3.getRectangle(element).corners$canvas();
            final Iterable $this$any$iv = corners;
            final int $i$f$any = 0;
            boolean b = false;
            Label_0320: {
                if ($this$any$iv instanceof Collection && ((Collection)$this$any$iv).isEmpty()) {
                    b = false;
                }
                else {
                    for (final Object element$iv : $this$any$iv) {
                        final CanvasPoint it2 = (CanvasPoint)element$iv;
                        final int n4 = 0;
                        if (selectionRectangle.contains(it2)) {
                            b = true;
                            break Label_0320;
                        }
                    }
                    b = false;
                }
            }
            if (b) {
                this.state.getSelectedElements().add((ElementType)element);
            }
            else {
                this.state.getSelectedElements().remove(element);
            }
        }
    }
    
    private final void moveElements(final CanvasPoint mousePosition) {
        if (this.state.getSelectedElements().isEmpty()) {
            return;
        }
        final CanvasPoint delta = mousePosition.minus(this.state.getLastRenderMousePosition());
        final Iterable $this$forEach$iv = this.state.getSelectedElements();
        final int $i$f$forEach = 0;
        for (final Object it : $this$forEach$iv) {
            final Object element$iv = it;
            final int n = 0;
            final CanvasAbstraction $this$moveElements_u24lambda_u2d7_u24lambda_u2d6 = this.abstraction;
            final int n2 = 0;
            final CanvasRectangle elementRect = $this$moveElements_u24lambda_u2d7_u24lambda_u2d6.getRectangle(it);
            final CanvasRectangle canvasRect = this.getSafeZoneRectangle$canvas();
            final CanvasRectangle newRectangle = elementRect.copy(elementRect.getTopLeft().plus(delta), elementRect.getBottomRight().plus(delta));
            final CanvasPoint point = newRectangle.getTopLeft();
            if (this.state.getSelectedElements().size() == 1) {
                this.snapper.snap(mousePosition, newRectangle, point);
            }
            final CanvasPoint $this$moveElements_u24lambda_u2d7_u24lambda_u2d6_u24lambda_u2d5 = point;
            final int n3 = 0;
            $this$moveElements_u24lambda_u2d7_u24lambda_u2d6_u24lambda_u2d5.setX(RangesKt___RangesKt.coerceIn($this$moveElements_u24lambda_u2d7_u24lambda_u2d6_u24lambda_u2d5.getX(), canvasRect.getLeft(), canvasRect.getRight() - elementRect.getWidth()));
            $this$moveElements_u24lambda_u2d7_u24lambda_u2d6_u24lambda_u2d5.setY(RangesKt___RangesKt.coerceIn($this$moveElements_u24lambda_u2d7_u24lambda_u2d6_u24lambda_u2d5.getY(), canvasRect.getTop(), canvasRect.getBottom() - elementRect.getHeight()));
            System.out.println((Object)("Moving element: " + point));
            $this$moveElements_u24lambda_u2d7_u24lambda_u2d6.moveTo(it, point);
        }
        this.snapper.notifyStateChange(CanvasAction.ELEMENT_MOVE);
    }
    
    private final void resizeElements(final CanvasPoint mousePosition) {
        if (this.state.getSelectedElements().size() != 1) {
            return;
        }
        final Object element = CollectionsKt___CollectionsKt.first((Iterable<?>)this.state.getSelectedElements());
        this.resizer.resize((ElementType)element, mousePosition);
    }
    
    public final void onMouseDown(@NotNull final CanvasPoint mousePosition) {
        Intrinsics.checkNotNullParameter(mousePosition, "mousePosition");
        this.state.setMouseDown(true);
        this.state.setMouseDownPosition(CanvasPoint.copy$default(mousePosition, 0.0f, 0.0f, 3, null));
        this.snapper.notifyStateChange(CanvasAction.NONE);
        this.computeCurrentAction(mousePosition);
        for (final Object element : this.abstraction.getElements()) {
            final CanvasAbstraction $this$onMouseDown_u24lambda_u2d8 = this.abstraction;
            final int n = 0;
            final CanvasRectangle rect = $this$onMouseDown_u24lambda_u2d8.getRectangle(element);
            CanvasResizer it;
            final CanvasResizer canvasResizer = it = this.resizer;
            final int n2 = 0;
            final CanvasResizer<ElementType, ColorType> canvasResizer2 = (CanvasResizer<ElementType, ColorType>)((this.config.getResizeHandleVisibility() != CanvasResizeHandleVisibility.NEVER) ? canvasResizer : null);
            CanvasRectangle resizeHandleRectangle;
            if (canvasResizer2 != null) {
                it = canvasResizer2;
                final int n3 = 0;
                final CanvasResizer $this$onMouseDown_u24lambda_u2d11_u24lambda_u2d10 = it;
                final int n4 = 0;
                resizeHandleRectangle = $this$onMouseDown_u24lambda_u2d11_u24lambda_u2d10.getResizeHandleRectangle(element);
            }
            else {
                resizeHandleRectangle = null;
            }
            final CanvasRectangle canvasRectangle;
            final CanvasRectangle resizeRect = canvasRectangle = resizeHandleRectangle;
            final boolean isResizing = canvasRectangle != null && canvasRectangle.contains(mousePosition);
            if (rect.contains(mousePosition) || isResizing) {
                final boolean holdingMultiSelectKey = false;
                if (isResizing || (this.state.getCurrentAction() == CanvasAction.ELEMENT_MOVE && !this.state.getSelectedElements().isEmpty() && !this.state.getSelectedElements().contains(element))) {
                    this.state.getSelectedElements().clear();
                }
                this.state.getSelectedElements().add((ElementType)element);
                return;
            }
        }
        this.state.getSelectedElements().clear();
    }
    
    public final void onMouseUp(@NotNull final CanvasPoint mousePosition) {
        Intrinsics.checkNotNullParameter(mousePosition, "mousePosition");
        this.state.setMouseDown(false);
        this.state.setMouseDownPosition(null);
        if (this.state.getCurrentAction() == CanvasAction.ELEMENT_MOVE && this.state.getSelectedElements().size() == 1) {
            this.state.getSelectedElements().clear();
        }
        this.computeCurrentAction(mousePosition);
    }
    
    private final void computeCurrentAction(final CanvasPoint mousePosition) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/github/nickacpt/behaviours/canvas/Canvas.state:Lio/github/nickacpt/behaviours/canvas/model/CanvasState;
        //     4: getstatic       io/github/nickacpt/behaviours/canvas/model/CanvasAction.NONE:Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;
        //     7: invokevirtual   io/github/nickacpt/behaviours/canvas/model/CanvasState.setCurrentAction:(Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;)V
        //    10: aload_0         /* this */
        //    11: getfield        io/github/nickacpt/behaviours/canvas/Canvas.abstraction:Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction;
        //    14: invokeinterface io/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction.getElements:()Ljava/util/Collection;
        //    19: checkcast       Ljava/lang/Iterable;
        //    22: astore_3        /* $this$firstOrNull$iv */
        //    23: iconst_0       
        //    24: istore          $i$f$firstOrNull
        //    26: aload_3         /* $this$firstOrNull$iv */
        //    27: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    32: astore          5
        //    34: aload           5
        //    36: invokeinterface java/util/Iterator.hasNext:()Z
        //    41: ifeq            91
        //    44: aload           5
        //    46: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    51: astore          element$iv
        //    53: aload           element$iv
        //    55: astore          it
        //    57: iconst_0       
        //    58: istore          $i$a$-firstOrNull-Canvas$computeCurrentAction$hoveredElement$1
        //    60: aload_0         /* this */
        //    61: getfield        io/github/nickacpt/behaviours/canvas/Canvas.abstraction:Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction;
        //    64: astore          $this$computeCurrentAction_u24lambda_u2d13_u24lambda_u2d12
        //    66: iconst_0       
        //    67: istore          $i$a$-with-Canvas$computeCurrentAction$hoveredElement$1$1
        //    69: aload           $this$computeCurrentAction_u24lambda_u2d13_u24lambda_u2d12
        //    71: aload           it
        //    73: invokeinterface io/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction.getRectangle:(Ljava/lang/Object;)Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;
        //    78: nop            
        //    79: aload_1         /* mousePosition */
        //    80: invokevirtual   io/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle.contains:(Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;)Z
        //    83: ifeq            34
        //    86: aload           element$iv
        //    88: goto            92
        //    91: aconst_null    
        //    92: astore_2        /* hoveredElement */
        //    93: aload_0         /* this */
        //    94: getfield        io/github/nickacpt/behaviours/canvas/Canvas.abstraction:Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction;
        //    97: invokeinterface io/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction.getElements:()Ljava/util/Collection;
        //   102: astore          5
        //   104: aload           5
        //   106: astore          it
        //   108: iconst_0       
        //   109: istore          $i$a$-takeIf-Canvas$computeCurrentAction$resizedElement$1
        //   111: aload_0         /* this */
        //   112: getfield        io/github/nickacpt/behaviours/canvas/Canvas.config:Lio/github/nickacpt/behaviours/canvas/config/CanvasConfig;
        //   115: invokevirtual   io/github/nickacpt/behaviours/canvas/config/CanvasConfig.getResizeHandleVisibility:()Lio/github/nickacpt/behaviours/canvas/config/CanvasResizeHandleVisibility;
        //   118: getstatic       io/github/nickacpt/behaviours/canvas/config/CanvasResizeHandleVisibility.NEVER:Lio/github/nickacpt/behaviours/canvas/config/CanvasResizeHandleVisibility;
        //   121: if_acmpeq       128
        //   124: iconst_1       
        //   125: goto            129
        //   128: iconst_0       
        //   129: ifeq            137
        //   132: aload           5
        //   134: goto            138
        //   137: aconst_null    
        //   138: astore          4
        //   140: aload           4
        //   142: ifnull          224
        //   145: aload           4
        //   147: checkcast       Ljava/lang/Iterable;
        //   150: astore          5
        //   152: nop            
        //   153: iconst_0       
        //   154: istore          $i$f$firstOrNull
        //   156: aload           $this$firstOrNull$iv
        //   158: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   163: astore          7
        //   165: aload           7
        //   167: invokeinterface java/util/Iterator.hasNext:()Z
        //   172: ifeq            220
        //   175: aload           7
        //   177: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   182: astore          element$iv
        //   184: aload           element$iv
        //   186: astore          it
        //   188: iconst_0       
        //   189: istore          $i$a$-firstOrNull-Canvas$computeCurrentAction$resizedElement$2
        //   191: aload_0         /* this */
        //   192: getfield        io/github/nickacpt/behaviours/canvas/Canvas.resizer:Lio/github/nickacpt/behaviours/canvas/CanvasResizer;
        //   195: astore          $this$computeCurrentAction_u24lambda_u2d16_u24lambda_u2d15
        //   197: iconst_0       
        //   198: istore          $i$a$-with-Canvas$computeCurrentAction$resizedElement$2$1
        //   200: aload           $this$computeCurrentAction_u24lambda_u2d16_u24lambda_u2d15
        //   202: aload           it
        //   204: invokevirtual   io/github/nickacpt/behaviours/canvas/CanvasResizer.getResizeHandleRectangle:(Ljava/lang/Object;)Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;
        //   207: nop            
        //   208: aload_1         /* mousePosition */
        //   209: invokevirtual   io/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle.contains:(Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;)Z
        //   212: ifeq            165
        //   215: aload           element$iv
        //   217: goto            225
        //   220: aconst_null    
        //   221: goto            225
        //   224: aconst_null    
        //   225: astore_3        /* resizedElement */
        //   226: aload_2         /* hoveredElement */
        //   227: ifnull          234
        //   230: iconst_1       
        //   231: goto            235
        //   234: iconst_0       
        //   235: istore          overAnyElement
        //   237: aload_0         /* this */
        //   238: getfield        io/github/nickacpt/behaviours/canvas/Canvas.state:Lio/github/nickacpt/behaviours/canvas/model/CanvasState;
        //   241: invokevirtual   io/github/nickacpt/behaviours/canvas/model/CanvasState.getMouseDown:()Z
        //   244: ifne            248
        //   247: return         
        //   248: aload_0         /* this */
        //   249: getfield        io/github/nickacpt/behaviours/canvas/Canvas.state:Lio/github/nickacpt/behaviours/canvas/model/CanvasState;
        //   252: aload_3         /* resizedElement */
        //   253: ifnull          278
        //   256: aload_0         /* this */
        //   257: getfield        io/github/nickacpt/behaviours/canvas/Canvas.state:Lio/github/nickacpt/behaviours/canvas/model/CanvasState;
        //   260: invokevirtual   io/github/nickacpt/behaviours/canvas/model/CanvasState.getSelectedElements:()Ljava/util/Set;
        //   263: invokeinterface java/util/Set.size:()I
        //   268: iconst_1       
        //   269: if_icmpgt       278
        //   272: getstatic       io/github/nickacpt/behaviours/canvas/model/CanvasAction.ELEMENT_RESIZE:Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;
        //   275: goto            292
        //   278: iload           overAnyElement
        //   280: ifeq            289
        //   283: getstatic       io/github/nickacpt/behaviours/canvas/model/CanvasAction.ELEMENT_MOVE:Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;
        //   286: goto            292
        //   289: getstatic       io/github/nickacpt/behaviours/canvas/model/CanvasAction.ELEMENT_SELECT:Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;
        //   292: invokevirtual   io/github/nickacpt/behaviours/canvas/model/CanvasState.setCurrentAction:(Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;)V
        //   295: return         
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  mousePosition  
        //    StackMapTable: 00 11 FF 00 22 00 06 07 00 02 07 00 72 00 07 00 F1 01 07 00 E0 00 00 38 40 07 00 05 FF 00 23 00 08 07 00 02 07 00 72 07 00 05 07 00 F1 01 07 00 DA 07 00 DA 01 00 00 40 01 07 40 07 00 DA FF 00 1A 00 08 07 00 02 07 00 72 07 00 05 07 00 F1 07 00 DA 07 00 F1 01 07 00 E0 00 00 36 FF 00 03 00 08 07 00 02 07 00 72 07 00 05 07 00 F1 07 00 DA 07 00 DA 07 00 DA 01 00 00 FF 00 00 00 06 07 00 02 07 00 72 07 00 05 07 00 F1 07 00 DA 07 00 05 00 01 07 00 05 FF 00 08 00 06 07 00 02 07 00 72 07 00 05 07 00 05 07 00 DA 07 00 05 00 00 40 01 FF 00 0C 00 06 07 00 02 07 00 72 07 00 05 07 00 05 01 07 00 05 00 00 5D 07 00 1E 4A 07 00 1E FF 00 02 00 06 07 00 02 07 00 72 07 00 05 07 00 05 01 07 00 05 00 02 07 00 1E 07 00 8F
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
