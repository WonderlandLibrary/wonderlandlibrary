// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.config;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002BY\u0012\u0010\b\u0002\u0010\u0003\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0004\u0012\u0010\b\u0002\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0004\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00018\u0000\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00018\u0000\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00018\u0000\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010\nJ\u0011\u0010\u001c\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0004H\u00c6\u0003J\u0011\u0010\u001d\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0004H\u00c6\u0003J\u0010\u0010\u001e\u001a\u0004\u0018\u00018\u0000H\u00c6\u0003¢\u0006\u0002\u0010\u0012J\u0010\u0010\u001f\u001a\u0004\u0018\u00018\u0000H\u00c6\u0003¢\u0006\u0002\u0010\u0012J\u0010\u0010 \u001a\u0004\u0018\u00018\u0000H\u00c6\u0003¢\u0006\u0002\u0010\u0012J\u0010\u0010!\u001a\u0004\u0018\u00018\u0000H\u00c6\u0003¢\u0006\u0002\u0010\u0012Jh\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0010\b\u0002\u0010\u0003\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u00042\u0010\b\u0002\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u00042\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00018\u00002\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00018\u00002\n\b\u0002\u0010\b\u001a\u0004\u0018\u00018\u00002\n\b\u0002\u0010\t\u001a\u0004\u0018\u00018\u0000H\u00c6\u0001¢\u0006\u0002\u0010#J\u0013\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\u0002H\u00d6\u0003J\t\u0010'\u001a\u00020(H\u00d6\u0001J\t\u0010)\u001a\u00020*H\u00d6\u0001R\"\u0010\u0003\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\"\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u001e\u0010\b\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0015\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001e\u0010\t\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0015\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0014R\u001e\u0010\u0006\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0015\u001a\u0004\b\u0018\u0010\u0012\"\u0004\b\u0019\u0010\u0014R\u001e\u0010\u0007\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0015\u001a\u0004\b\u001a\u0010\u0012\"\u0004\b\u001b\u0010\u0014¨\u0006+" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/config/CanvasColorConfig;", "ColorType", "", "elementBackground", "Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;", "elementBorder", "selectionBackground", "selectionBorder", "resizeHandleBackground", "resizeHandleBorder", "(Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "getElementBackground", "()Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;", "setElementBackground", "(Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;)V", "getElementBorder", "setElementBorder", "getResizeHandleBackground", "()Ljava/lang/Object;", "setResizeHandleBackground", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "getResizeHandleBorder", "setResizeHandleBorder", "getSelectionBackground", "setSelectionBackground", "getSelectionBorder", "setSelectionBorder", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lio/github/nickacpt/behaviours/canvas/config/CanvasColorConfig;", "equals", "", "other", "hashCode", "", "toString", "", "canvas" })
public final class CanvasColorConfig<ColorType>
{
    @Nullable
    private CanvasColorStyle<ColorType> elementBackground;
    @Nullable
    private CanvasColorStyle<ColorType> elementBorder;
    @Nullable
    private ColorType selectionBackground;
    @Nullable
    private ColorType selectionBorder;
    @Nullable
    private ColorType resizeHandleBackground;
    @Nullable
    private ColorType resizeHandleBorder;
    
    public CanvasColorConfig(@Nullable final CanvasColorStyle<ColorType> elementBackground, @Nullable final CanvasColorStyle<ColorType> elementBorder, @Nullable final ColorType selectionBackground, @Nullable final ColorType selectionBorder, @Nullable final ColorType resizeHandleBackground, @Nullable final ColorType resizeHandleBorder) {
        this.elementBackground = elementBackground;
        this.elementBorder = elementBorder;
        this.selectionBackground = selectionBackground;
        this.selectionBorder = selectionBorder;
        this.resizeHandleBackground = resizeHandleBackground;
        this.resizeHandleBorder = resizeHandleBorder;
    }
    
    @Nullable
    public final CanvasColorStyle<ColorType> getElementBackground() {
        return this.elementBackground;
    }
    
    public final void setElementBackground(@Nullable final CanvasColorStyle<ColorType> <set-?>) {
        this.elementBackground = <set-?>;
    }
    
    @Nullable
    public final CanvasColorStyle<ColorType> getElementBorder() {
        return this.elementBorder;
    }
    
    public final void setElementBorder(@Nullable final CanvasColorStyle<ColorType> <set-?>) {
        this.elementBorder = <set-?>;
    }
    
    @Nullable
    public final ColorType getSelectionBackground() {
        return this.selectionBackground;
    }
    
    public final void setSelectionBackground(@Nullable final ColorType <set-?>) {
        this.selectionBackground = <set-?>;
    }
    
    @Nullable
    public final ColorType getSelectionBorder() {
        return this.selectionBorder;
    }
    
    public final void setSelectionBorder(@Nullable final ColorType <set-?>) {
        this.selectionBorder = <set-?>;
    }
    
    @Nullable
    public final ColorType getResizeHandleBackground() {
        return this.resizeHandleBackground;
    }
    
    public final void setResizeHandleBackground(@Nullable final ColorType <set-?>) {
        this.resizeHandleBackground = <set-?>;
    }
    
    @Nullable
    public final ColorType getResizeHandleBorder() {
        return this.resizeHandleBorder;
    }
    
    public final void setResizeHandleBorder(@Nullable final ColorType <set-?>) {
        this.resizeHandleBorder = <set-?>;
    }
    
    @Nullable
    public final CanvasColorStyle<ColorType> component1() {
        return this.elementBackground;
    }
    
    @Nullable
    public final CanvasColorStyle<ColorType> component2() {
        return this.elementBorder;
    }
    
    @Nullable
    public final ColorType component3() {
        return this.selectionBackground;
    }
    
    @Nullable
    public final ColorType component4() {
        return this.selectionBorder;
    }
    
    @Nullable
    public final ColorType component5() {
        return this.resizeHandleBackground;
    }
    
    @Nullable
    public final ColorType component6() {
        return this.resizeHandleBorder;
    }
    
    @NotNull
    public final CanvasColorConfig<ColorType> copy(@Nullable final CanvasColorStyle<ColorType> elementBackground, @Nullable final CanvasColorStyle<ColorType> elementBorder, @Nullable final ColorType selectionBackground, @Nullable final ColorType selectionBorder, @Nullable final ColorType resizeHandleBackground, @Nullable final ColorType resizeHandleBorder) {
        return new CanvasColorConfig<ColorType>(elementBackground, elementBorder, selectionBackground, selectionBorder, resizeHandleBackground, resizeHandleBorder);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "CanvasColorConfig(elementBackground=" + this.elementBackground + ", elementBorder=" + this.elementBorder + ", selectionBackground=" + this.selectionBackground + ", selectionBorder=" + this.selectionBorder + ", resizeHandleBackground=" + this.resizeHandleBackground + ", resizeHandleBorder=" + this.resizeHandleBorder + ')';
    }
    
    @Override
    public int hashCode() {
        int result = (this.elementBackground == null) ? 0 : this.elementBackground.hashCode();
        result = result * 31 + ((this.elementBorder == null) ? 0 : this.elementBorder.hashCode());
        result = result * 31 + ((this.selectionBackground == null) ? 0 : this.selectionBackground.hashCode());
        result = result * 31 + ((this.selectionBorder == null) ? 0 : this.selectionBorder.hashCode());
        result = result * 31 + ((this.resizeHandleBackground == null) ? 0 : this.resizeHandleBackground.hashCode());
        result = result * 31 + ((this.resizeHandleBorder == null) ? 0 : this.resizeHandleBorder.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CanvasColorConfig)) {
            return false;
        }
        final CanvasColorConfig canvasColorConfig = (CanvasColorConfig)other;
        return Intrinsics.areEqual(this.elementBackground, canvasColorConfig.elementBackground) && Intrinsics.areEqual(this.elementBorder, canvasColorConfig.elementBorder) && Intrinsics.areEqual(this.selectionBackground, canvasColorConfig.selectionBackground) && Intrinsics.areEqual(this.selectionBorder, canvasColorConfig.selectionBorder) && Intrinsics.areEqual(this.resizeHandleBackground, canvasColorConfig.resizeHandleBackground) && Intrinsics.areEqual(this.resizeHandleBorder, canvasColorConfig.resizeHandleBorder);
    }
    
    public CanvasColorConfig() {
        this(null, null, null, null, null, null, 63, null);
    }
}
