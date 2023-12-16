// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.config;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.JvmOverloads;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B+\b\u0007\u0012\u0006\u0010\u0003\u001a\u00028\u0000\u0012\u0006\u0010\u0004\u001a\u00028\u0000\u0012\b\b\u0002\u0010\u0005\u001a\u00028\u0000\u0012\b\b\u0002\u0010\u0006\u001a\u00028\u0000¢\u0006\u0002\u0010\u0007J\u000e\u0010\u0013\u001a\u00028\u0000H\u00c6\u0003¢\u0006\u0002\u0010\tJ\u000e\u0010\u0014\u001a\u00028\u0000H\u00c6\u0003¢\u0006\u0002\u0010\tJ\u000e\u0010\u0015\u001a\u00028\u0000H\u00c6\u0003¢\u0006\u0002\u0010\tJ\u000e\u0010\u0016\u001a\u00028\u0000H\u00c6\u0003¢\u0006\u0002\u0010\tJ<\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00028\u00002\b\b\u0002\u0010\u0004\u001a\u00028\u00002\b\b\u0002\u0010\u0005\u001a\u00028\u00002\b\b\u0002\u0010\u0006\u001a\u00028\u0000H\u00c6\u0001¢\u0006\u0002\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0002H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001R\u001c\u0010\u0004\u001a\u00028\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\u0006\u001a\u00028\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\f\u001a\u0004\b\r\u0010\t\"\u0004\b\u000e\u0010\u000bR\u001c\u0010\u0003\u001a\u00028\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\f\u001a\u0004\b\u000f\u0010\t\"\u0004\b\u0010\u0010\u000bR\u001c\u0010\u0005\u001a\u00028\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\f\u001a\u0004\b\u0011\u0010\t\"\u0004\b\u0012\u0010\u000b¨\u0006 " }, d2 = { "Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;", "ColorType", "", "normal", "hover", "selected", "mouseDown", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "getHover", "()Ljava/lang/Object;", "setHover", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "getMouseDown", "setMouseDown", "getNormal", "setNormal", "getSelected", "setSelected", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lio/github/nickacpt/behaviours/canvas/config/CanvasColorStyle;", "equals", "", "other", "hashCode", "", "toString", "", "canvas" })
public final class CanvasColorStyle<ColorType>
{
    private ColorType normal;
    private ColorType hover;
    private ColorType selected;
    private ColorType mouseDown;
    
    @JvmOverloads
    public CanvasColorStyle(final ColorType normal, final ColorType hover, final ColorType selected, final ColorType mouseDown) {
        this.normal = normal;
        this.hover = hover;
        this.selected = selected;
        this.mouseDown = mouseDown;
    }
    
    public final ColorType getNormal() {
        return this.normal;
    }
    
    public final void setNormal(final ColorType <set-?>) {
        this.normal = <set-?>;
    }
    
    public final ColorType getHover() {
        return this.hover;
    }
    
    public final void setHover(final ColorType <set-?>) {
        this.hover = <set-?>;
    }
    
    public final ColorType getSelected() {
        return this.selected;
    }
    
    public final void setSelected(final ColorType <set-?>) {
        this.selected = <set-?>;
    }
    
    public final ColorType getMouseDown() {
        return this.mouseDown;
    }
    
    public final void setMouseDown(final ColorType <set-?>) {
        this.mouseDown = <set-?>;
    }
    
    public final ColorType component1() {
        return this.normal;
    }
    
    public final ColorType component2() {
        return this.hover;
    }
    
    public final ColorType component3() {
        return this.selected;
    }
    
    public final ColorType component4() {
        return this.mouseDown;
    }
    
    @NotNull
    public final CanvasColorStyle<ColorType> copy(final ColorType normal, final ColorType hover, final ColorType selected, final ColorType mouseDown) {
        return new CanvasColorStyle<ColorType>(normal, hover, selected, mouseDown);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "CanvasColorStyle(normal=" + this.normal + ", hover=" + this.hover + ", selected=" + this.selected + ", mouseDown=" + this.mouseDown + ')';
    }
    
    @Override
    public int hashCode() {
        int result = (this.normal == null) ? 0 : this.normal.hashCode();
        result = result * 31 + ((this.hover == null) ? 0 : this.hover.hashCode());
        result = result * 31 + ((this.selected == null) ? 0 : this.selected.hashCode());
        result = result * 31 + ((this.mouseDown == null) ? 0 : this.mouseDown.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CanvasColorStyle)) {
            return false;
        }
        final CanvasColorStyle canvasColorStyle = (CanvasColorStyle)other;
        return Intrinsics.areEqual(this.normal, canvasColorStyle.normal) && Intrinsics.areEqual(this.hover, canvasColorStyle.hover) && Intrinsics.areEqual(this.selected, canvasColorStyle.selected) && Intrinsics.areEqual(this.mouseDown, canvasColorStyle.mouseDown);
    }
    
    @JvmOverloads
    public CanvasColorStyle(final ColorType normal, final ColorType hover, final ColorType selected) {
        this(normal, hover, selected, null, 8, null);
    }
    
    @JvmOverloads
    public CanvasColorStyle(final ColorType normal, final ColorType hover) {
        this(normal, hover, null, null, 12, null);
    }
}
