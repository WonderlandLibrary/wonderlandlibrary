// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.model;

import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.LinkedHashSet;
import kotlin.jvm.internal.Intrinsics;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import org.jetbrains.annotations.NotNull;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000@\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010#\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u001b\u0012\u0014\b\u0002\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004¢\u0006\u0002\u0010\u0007J\u0015\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004H\u00c6\u0003J%\u0010)\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0014\b\u0002\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004H\u00c6\u0001J\u0013\u0010*\u001a\u00020\u001b2\b\u0010+\u001a\u0004\u0018\u00010\u0002H\u00d6\u0003J\t\u0010,\u001a\u00020-H\u00d6\u0001J\t\u0010.\u001a\u00020/H\u00d6\u0001R\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR$\u0010\u000b\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0010\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0015\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0016\u001a\u00020\u0017¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u001bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001c\u0010 \u001a\u0004\u0018\u00010\u0017X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0019\"\u0004\b\"\u0010#R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00028\u00000%¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'¨\u00060" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/model/CanvasState;", "ElementType", "", "actionChangedHandler", "Lkotlin/Function1;", "Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;", "", "(Lkotlin/jvm/functions/Function1;)V", "getActionChangedHandler", "()Lkotlin/jvm/functions/Function1;", "value", "currentAction", "getCurrentAction", "()Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;", "setCurrentAction", "(Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;)V", "lastClickedElement", "getLastClickedElement", "()Ljava/lang/Object;", "setLastClickedElement", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "lastRenderMousePosition", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "getLastRenderMousePosition", "()Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "mouseDown", "", "getMouseDown", "()Z", "setMouseDown", "(Z)V", "mouseDownPosition", "getMouseDownPosition", "setMouseDownPosition", "(Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;)V", "selectedElements", "", "getSelectedElements", "()Ljava/util/Set;", "component1", "copy", "equals", "other", "hashCode", "", "toString", "", "canvas" })
public final class CanvasState<ElementType>
{
    @NotNull
    private final Function1<CanvasAction, Unit> actionChangedHandler;
    @NotNull
    private CanvasAction currentAction;
    private boolean mouseDown;
    @Nullable
    private CanvasPoint mouseDownPosition;
    @NotNull
    private final CanvasPoint lastRenderMousePosition;
    @NotNull
    private final Set<ElementType> selectedElements;
    @Nullable
    private ElementType lastClickedElement;
    
    public CanvasState(@NotNull final Function1<? super CanvasAction, Unit> actionChangedHandler) {
        Intrinsics.checkNotNullParameter(actionChangedHandler, "actionChangedHandler");
        this.actionChangedHandler = (Function1<CanvasAction, Unit>)actionChangedHandler;
        this.currentAction = CanvasAction.NONE;
        this.lastRenderMousePosition = new CanvasPoint(0.0f, 0.0f);
        this.selectedElements = new LinkedHashSet<ElementType>();
    }
    
    @NotNull
    public final Function1<CanvasAction, Unit> getActionChangedHandler() {
        return this.actionChangedHandler;
    }
    
    @NotNull
    public final CanvasAction getCurrentAction() {
        return this.currentAction;
    }
    
    public final void setCurrentAction(@NotNull final CanvasAction value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.currentAction = value;
        this.actionChangedHandler.invoke(value);
    }
    
    public final boolean getMouseDown() {
        return this.mouseDown;
    }
    
    public final void setMouseDown(final boolean <set-?>) {
        this.mouseDown = <set-?>;
    }
    
    @Nullable
    public final CanvasPoint getMouseDownPosition() {
        return this.mouseDownPosition;
    }
    
    public final void setMouseDownPosition(@Nullable final CanvasPoint <set-?>) {
        this.mouseDownPosition = <set-?>;
    }
    
    @NotNull
    public final CanvasPoint getLastRenderMousePosition() {
        return this.lastRenderMousePosition;
    }
    
    @NotNull
    public final Set<ElementType> getSelectedElements() {
        return this.selectedElements;
    }
    
    @Nullable
    public final ElementType getLastClickedElement() {
        return this.lastClickedElement;
    }
    
    public final void setLastClickedElement(@Nullable final ElementType <set-?>) {
        this.lastClickedElement = <set-?>;
    }
    
    @NotNull
    public final Function1<CanvasAction, Unit> component1() {
        return this.actionChangedHandler;
    }
    
    @NotNull
    public final CanvasState<ElementType> copy(@NotNull final Function1<? super CanvasAction, Unit> actionChangedHandler) {
        Intrinsics.checkNotNullParameter(actionChangedHandler, "actionChangedHandler");
        return new CanvasState<ElementType>(actionChangedHandler);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "CanvasState(actionChangedHandler=" + this.actionChangedHandler + ')';
    }
    
    @Override
    public int hashCode() {
        return this.actionChangedHandler.hashCode();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return this == other || (other instanceof CanvasState && Intrinsics.areEqual(this.actionChangedHandler, ((CanvasState)other).actionChangedHandler));
    }
    
    public CanvasState() {
        this(null, 1, null);
    }
}
