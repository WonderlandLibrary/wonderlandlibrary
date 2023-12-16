// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.config;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b2\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0085\u0001\b\u0007\u0012\u000e\b\u0002\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\b\b\u0002\u0010\f\u001a\u00020\u0006\u0012\b\b\u0002\u0010\r\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0006¢\u0006\u0002\u0010\u0013J\u000f\u00102\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u00c6\u0003J\t\u00103\u001a\u00020\u0006H\u00c6\u0003J\t\u00104\u001a\u00020\u0006H\u00c6\u0003J\t\u00105\u001a\u00020\u0006H\u00c6\u0003J\t\u00106\u001a\u00020\u0006H\u00c6\u0003J\t\u00107\u001a\u00020\u0006H\u00c6\u0003J\t\u00108\u001a\u00020\u0006H\u00c6\u0003J\t\u00109\u001a\u00020\nH\u00c6\u0003J\t\u0010:\u001a\u00020\nH\u00c6\u0003J\t\u0010;\u001a\u00020\u0006H\u00c6\u0003J\t\u0010<\u001a\u00020\u0006H\u00c6\u0003J\t\u0010=\u001a\u00020\u000fH\u00c6\u0003J\u008d\u0001\u0010>\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\b\u0002\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\u00062\b\b\u0002\u0010\r\u001a\u00020\u00062\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00062\b\b\u0002\u0010\u0011\u001a\u00020\u00062\b\b\u0002\u0010\u0012\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010?\u001a\u00020\n2\b\u0010@\u001a\u0004\u0018\u00010\u0002H\u00d6\u0003J\t\u0010A\u001a\u00020BH\u00d6\u0001J\t\u0010C\u001a\u00020DH\u00d6\u0001R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u001a\u0010\u000b\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001b\"\u0004\b\u001f\u0010\u001dR\u001a\u0010\r\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0015\"\u0004\b!\u0010\u0017R\u001a\u0010\u0012\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0015\"\u0004\b#\u0010\u0017R\u001a\u0010\u0011\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0015\"\u0004\b%\u0010\u0017R\u001a\u0010\u0010\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0015\"\u0004\b'\u0010\u0017R\u001a\u0010\u000e\u001a\u00020\u000fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u001a\u0010\u0007\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0015\"\u0004\b-\u0010\u0017R\u001a\u0010\f\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0015\"\u0004\b/\u0010\u0017R\u001a\u0010\b\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u0010\u0015\"\u0004\b1\u0010\u0017¨\u0006E" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/config/CanvasConfig;", "ColorType", "", "colors", "Lio/github/nickacpt/behaviours/canvas/config/CanvasColorConfig;", "borderWidth", "", "safeZoneSize", "snapLineWidth", "generateMiddleSnapLines", "", "elementsHaveMiddleSnapLines", "snapDistance", "mouseExitSnapDistance", "resizeHandleVisibility", "Lio/github/nickacpt/behaviours/canvas/config/CanvasResizeHandleVisibility;", "resizeHandleSize", "resizeHandleRenderSize", "resizeHandleBorderWidth", "(Lio/github/nickacpt/behaviours/canvas/config/CanvasColorConfig;FFFZZFFLio/github/nickacpt/behaviours/canvas/config/CanvasResizeHandleVisibility;FFF)V", "getBorderWidth", "()F", "setBorderWidth", "(F)V", "getColors", "()Lio/github/nickacpt/behaviours/canvas/config/CanvasColorConfig;", "getElementsHaveMiddleSnapLines", "()Z", "setElementsHaveMiddleSnapLines", "(Z)V", "getGenerateMiddleSnapLines", "setGenerateMiddleSnapLines", "getMouseExitSnapDistance", "setMouseExitSnapDistance", "getResizeHandleBorderWidth", "setResizeHandleBorderWidth", "getResizeHandleRenderSize", "setResizeHandleRenderSize", "getResizeHandleSize", "setResizeHandleSize", "getResizeHandleVisibility", "()Lio/github/nickacpt/behaviours/canvas/config/CanvasResizeHandleVisibility;", "setResizeHandleVisibility", "(Lio/github/nickacpt/behaviours/canvas/config/CanvasResizeHandleVisibility;)V", "getSafeZoneSize", "setSafeZoneSize", "getSnapDistance", "setSnapDistance", "getSnapLineWidth", "setSnapLineWidth", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "", "canvas" })
public final class CanvasConfig<ColorType>
{
    @NotNull
    private final CanvasColorConfig<ColorType> colors;
    private float borderWidth;
    private float safeZoneSize;
    private float snapLineWidth;
    private boolean generateMiddleSnapLines;
    private boolean elementsHaveMiddleSnapLines;
    private float snapDistance;
    private float mouseExitSnapDistance;
    @NotNull
    private CanvasResizeHandleVisibility resizeHandleVisibility;
    private float resizeHandleSize;
    private float resizeHandleRenderSize;
    private float resizeHandleBorderWidth;
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines, final boolean elementsHaveMiddleSnapLines, final float snapDistance, final float mouseExitSnapDistance, @NotNull final CanvasResizeHandleVisibility resizeHandleVisibility, final float resizeHandleSize, final float resizeHandleRenderSize, final float resizeHandleBorderWidth) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        Intrinsics.checkNotNullParameter(resizeHandleVisibility, "resizeHandleVisibility");
        this.colors = colors;
        this.borderWidth = borderWidth;
        this.safeZoneSize = safeZoneSize;
        this.snapLineWidth = snapLineWidth;
        this.generateMiddleSnapLines = generateMiddleSnapLines;
        this.elementsHaveMiddleSnapLines = elementsHaveMiddleSnapLines;
        this.snapDistance = snapDistance;
        this.mouseExitSnapDistance = mouseExitSnapDistance;
        this.resizeHandleVisibility = resizeHandleVisibility;
        this.resizeHandleSize = resizeHandleSize;
        this.resizeHandleRenderSize = resizeHandleRenderSize;
        this.resizeHandleBorderWidth = resizeHandleBorderWidth;
    }
    
    @NotNull
    public final CanvasColorConfig<ColorType> getColors() {
        return this.colors;
    }
    
    public final float getBorderWidth() {
        return this.borderWidth;
    }
    
    public final void setBorderWidth(final float <set-?>) {
        this.borderWidth = <set-?>;
    }
    
    public final float getSafeZoneSize() {
        return this.safeZoneSize;
    }
    
    public final void setSafeZoneSize(final float <set-?>) {
        this.safeZoneSize = <set-?>;
    }
    
    public final float getSnapLineWidth() {
        return this.snapLineWidth;
    }
    
    public final void setSnapLineWidth(final float <set-?>) {
        this.snapLineWidth = <set-?>;
    }
    
    public final boolean getGenerateMiddleSnapLines() {
        return this.generateMiddleSnapLines;
    }
    
    public final void setGenerateMiddleSnapLines(final boolean <set-?>) {
        this.generateMiddleSnapLines = <set-?>;
    }
    
    public final boolean getElementsHaveMiddleSnapLines() {
        return this.elementsHaveMiddleSnapLines;
    }
    
    public final void setElementsHaveMiddleSnapLines(final boolean <set-?>) {
        this.elementsHaveMiddleSnapLines = <set-?>;
    }
    
    public final float getSnapDistance() {
        return this.snapDistance;
    }
    
    public final void setSnapDistance(final float <set-?>) {
        this.snapDistance = <set-?>;
    }
    
    public final float getMouseExitSnapDistance() {
        return this.mouseExitSnapDistance;
    }
    
    public final void setMouseExitSnapDistance(final float <set-?>) {
        this.mouseExitSnapDistance = <set-?>;
    }
    
    @NotNull
    public final CanvasResizeHandleVisibility getResizeHandleVisibility() {
        return this.resizeHandleVisibility;
    }
    
    public final void setResizeHandleVisibility(@NotNull final CanvasResizeHandleVisibility <set-?>) {
        Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
        this.resizeHandleVisibility = <set-?>;
    }
    
    public final float getResizeHandleSize() {
        return this.resizeHandleSize;
    }
    
    public final void setResizeHandleSize(final float <set-?>) {
        this.resizeHandleSize = <set-?>;
    }
    
    public final float getResizeHandleRenderSize() {
        return this.resizeHandleRenderSize;
    }
    
    public final void setResizeHandleRenderSize(final float <set-?>) {
        this.resizeHandleRenderSize = <set-?>;
    }
    
    public final float getResizeHandleBorderWidth() {
        return this.resizeHandleBorderWidth;
    }
    
    public final void setResizeHandleBorderWidth(final float <set-?>) {
        this.resizeHandleBorderWidth = <set-?>;
    }
    
    @NotNull
    public final CanvasColorConfig<ColorType> component1() {
        return this.colors;
    }
    
    public final float component2() {
        return this.borderWidth;
    }
    
    public final float component3() {
        return this.safeZoneSize;
    }
    
    public final float component4() {
        return this.snapLineWidth;
    }
    
    public final boolean component5() {
        return this.generateMiddleSnapLines;
    }
    
    public final boolean component6() {
        return this.elementsHaveMiddleSnapLines;
    }
    
    public final float component7() {
        return this.snapDistance;
    }
    
    public final float component8() {
        return this.mouseExitSnapDistance;
    }
    
    @NotNull
    public final CanvasResizeHandleVisibility component9() {
        return this.resizeHandleVisibility;
    }
    
    public final float component10() {
        return this.resizeHandleSize;
    }
    
    public final float component11() {
        return this.resizeHandleRenderSize;
    }
    
    public final float component12() {
        return this.resizeHandleBorderWidth;
    }
    
    @NotNull
    public final CanvasConfig<ColorType> copy(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines, final boolean elementsHaveMiddleSnapLines, final float snapDistance, final float mouseExitSnapDistance, @NotNull final CanvasResizeHandleVisibility resizeHandleVisibility, final float resizeHandleSize, final float resizeHandleRenderSize, final float resizeHandleBorderWidth) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        Intrinsics.checkNotNullParameter(resizeHandleVisibility, "resizeHandleVisibility");
        return new CanvasConfig<ColorType>(colors, borderWidth, safeZoneSize, snapLineWidth, generateMiddleSnapLines, elementsHaveMiddleSnapLines, snapDistance, mouseExitSnapDistance, resizeHandleVisibility, resizeHandleSize, resizeHandleRenderSize, resizeHandleBorderWidth);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CanvasConfig(colors=").append(this.colors).append(", borderWidth=").append(this.borderWidth).append(", safeZoneSize=").append(this.safeZoneSize).append(", snapLineWidth=").append(this.snapLineWidth).append(", generateMiddleSnapLines=").append(this.generateMiddleSnapLines).append(", elementsHaveMiddleSnapLines=").append(this.elementsHaveMiddleSnapLines).append(", snapDistance=").append(this.snapDistance).append(", mouseExitSnapDistance=").append(this.mouseExitSnapDistance).append(", resizeHandleVisibility=").append(this.resizeHandleVisibility).append(", resizeHandleSize=").append(this.resizeHandleSize).append(", resizeHandleRenderSize=").append(this.resizeHandleRenderSize).append(", resizeHandleBorderWidth=");
        sb.append(this.resizeHandleBorderWidth).append(')');
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int result = this.colors.hashCode();
        result = result * 31 + Float.hashCode(this.borderWidth);
        result = result * 31 + Float.hashCode(this.safeZoneSize);
        result = result * 31 + Float.hashCode(this.snapLineWidth);
        final int n = result * 31;
        int generateMiddleSnapLines;
        if ((generateMiddleSnapLines = (this.generateMiddleSnapLines ? 1 : 0)) != 0) {
            generateMiddleSnapLines = 1;
        }
        result = n + generateMiddleSnapLines;
        final int n2 = result * 31;
        int elementsHaveMiddleSnapLines;
        if ((elementsHaveMiddleSnapLines = (this.elementsHaveMiddleSnapLines ? 1 : 0)) != 0) {
            elementsHaveMiddleSnapLines = 1;
        }
        result = n2 + elementsHaveMiddleSnapLines;
        result = result * 31 + Float.hashCode(this.snapDistance);
        result = result * 31 + Float.hashCode(this.mouseExitSnapDistance);
        result = result * 31 + this.resizeHandleVisibility.hashCode();
        result = result * 31 + Float.hashCode(this.resizeHandleSize);
        result = result * 31 + Float.hashCode(this.resizeHandleRenderSize);
        result = result * 31 + Float.hashCode(this.resizeHandleBorderWidth);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CanvasConfig)) {
            return false;
        }
        final CanvasConfig canvasConfig = (CanvasConfig)other;
        return Intrinsics.areEqual(this.colors, canvasConfig.colors) && Intrinsics.areEqual(this.borderWidth, (Object)canvasConfig.borderWidth) && Intrinsics.areEqual(this.safeZoneSize, (Object)canvasConfig.safeZoneSize) && Intrinsics.areEqual(this.snapLineWidth, (Object)canvasConfig.snapLineWidth) && this.generateMiddleSnapLines == canvasConfig.generateMiddleSnapLines && this.elementsHaveMiddleSnapLines == canvasConfig.elementsHaveMiddleSnapLines && Intrinsics.areEqual(this.snapDistance, (Object)canvasConfig.snapDistance) && Intrinsics.areEqual(this.mouseExitSnapDistance, (Object)canvasConfig.mouseExitSnapDistance) && this.resizeHandleVisibility == canvasConfig.resizeHandleVisibility && Intrinsics.areEqual(this.resizeHandleSize, (Object)canvasConfig.resizeHandleSize) && Intrinsics.areEqual(this.resizeHandleRenderSize, (Object)canvasConfig.resizeHandleRenderSize) && Intrinsics.areEqual(this.resizeHandleBorderWidth, (Object)canvasConfig.resizeHandleBorderWidth);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines, final boolean elementsHaveMiddleSnapLines, final float snapDistance, final float mouseExitSnapDistance, @NotNull final CanvasResizeHandleVisibility resizeHandleVisibility, final float resizeHandleSize, final float resizeHandleRenderSize) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        Intrinsics.checkNotNullParameter(resizeHandleVisibility, "resizeHandleVisibility");
        this(colors, borderWidth, safeZoneSize, snapLineWidth, generateMiddleSnapLines, elementsHaveMiddleSnapLines, snapDistance, mouseExitSnapDistance, resizeHandleVisibility, resizeHandleSize, resizeHandleRenderSize, 0.0f, 2048, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines, final boolean elementsHaveMiddleSnapLines, final float snapDistance, final float mouseExitSnapDistance, @NotNull final CanvasResizeHandleVisibility resizeHandleVisibility, final float resizeHandleSize) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        Intrinsics.checkNotNullParameter(resizeHandleVisibility, "resizeHandleVisibility");
        this(colors, borderWidth, safeZoneSize, snapLineWidth, generateMiddleSnapLines, elementsHaveMiddleSnapLines, snapDistance, mouseExitSnapDistance, resizeHandleVisibility, resizeHandleSize, 0.0f, 0.0f, 3072, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines, final boolean elementsHaveMiddleSnapLines, final float snapDistance, final float mouseExitSnapDistance, @NotNull final CanvasResizeHandleVisibility resizeHandleVisibility) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        Intrinsics.checkNotNullParameter(resizeHandleVisibility, "resizeHandleVisibility");
        this(colors, borderWidth, safeZoneSize, snapLineWidth, generateMiddleSnapLines, elementsHaveMiddleSnapLines, snapDistance, mouseExitSnapDistance, resizeHandleVisibility, 0.0f, 0.0f, 0.0f, 3584, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines, final boolean elementsHaveMiddleSnapLines, final float snapDistance, final float mouseExitSnapDistance) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        this(colors, borderWidth, safeZoneSize, snapLineWidth, generateMiddleSnapLines, elementsHaveMiddleSnapLines, snapDistance, mouseExitSnapDistance, null, 0.0f, 0.0f, 0.0f, 3840, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines, final boolean elementsHaveMiddleSnapLines, final float snapDistance) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        this(colors, borderWidth, safeZoneSize, snapLineWidth, generateMiddleSnapLines, elementsHaveMiddleSnapLines, snapDistance, 0.0f, null, 0.0f, 0.0f, 0.0f, 3968, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines, final boolean elementsHaveMiddleSnapLines) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        this(colors, borderWidth, safeZoneSize, snapLineWidth, generateMiddleSnapLines, elementsHaveMiddleSnapLines, 0.0f, 0.0f, null, 0.0f, 0.0f, 0.0f, 4032, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth, final boolean generateMiddleSnapLines) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        this(colors, borderWidth, safeZoneSize, snapLineWidth, generateMiddleSnapLines, false, 0.0f, 0.0f, null, 0.0f, 0.0f, 0.0f, 4064, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize, final float snapLineWidth) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        this(colors, borderWidth, safeZoneSize, snapLineWidth, false, false, 0.0f, 0.0f, null, 0.0f, 0.0f, 0.0f, 4080, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth, final float safeZoneSize) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        this(colors, borderWidth, safeZoneSize, 0.0f, false, false, 0.0f, 0.0f, null, 0.0f, 0.0f, 0.0f, 4088, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors, final float borderWidth) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        this(colors, borderWidth, 0.0f, 0.0f, false, false, 0.0f, 0.0f, null, 0.0f, 0.0f, 0.0f, 4092, null);
    }
    
    @JvmOverloads
    public CanvasConfig(@NotNull final CanvasColorConfig<ColorType> colors) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        this(colors, 0.0f, 0.0f, 0.0f, false, false, 0.0f, 0.0f, null, 0.0f, 0.0f, 0.0f, 4094, null);
    }
    
    @JvmOverloads
    public CanvasConfig() {
        this(null, 0.0f, 0.0f, 0.0f, false, false, 0.0f, 0.0f, null, 0.0f, 0.0f, 0.0f, 4095, null);
    }
}
