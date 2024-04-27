/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.gui.client.hud.element;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.Value;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u001a\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010>\u001a\u00020\u0011H\u0016J\b\u0010?\u001a\u00020@H\u0016J\n\u0010A\u001a\u0004\u0018\u00010\u000bH&J\u0010\u0010B\u001a\u00020@2\u0006\u0010C\u001a\u00020DH\u0016J\u0018\u0010E\u001a\u00020@2\u0006\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020IH\u0016J \u0010J\u001a\u00020@2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010K\u001a\u00020IH\u0016J\u0018\u0010L\u001a\u00020\u00112\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010M\u001a\u00020@H\u0016R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u001a\u0010\u001e\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010 \"\u0004\b%\u0010\"R$\u0010'\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u00038F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R$\u0010,\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u00038F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b-\u0010)\"\u0004\b.\u0010+R&\u0010\u0005\u001a\u00020\u00062\u0006\u0010&\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010 \"\u0004\b0\u0010\"R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u001e\u00105\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u000307068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b8\u00109R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b:\u0010)\"\u0004\b;\u0010+R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b<\u0010)\"\u0004\b=\u0010+\u00a8\u0006N"}, d2={"Lnet/dev/important/gui/client/hud/element/Element;", "Lnet/dev/important/utils/MinecraftInstance;", "x", "", "y", "scale", "", "side", "Lnet/dev/important/gui/client/hud/element/Side;", "(DDFLnet/dev/important/gui/client/hud/element/Side;)V", "border", "Lnet/dev/important/gui/client/hud/element/Border;", "getBorder", "()Lnet/dev/important/gui/client/hud/element/Border;", "setBorder", "(Lnet/dev/important/gui/client/hud/element/Border;)V", "drag", "", "getDrag", "()Z", "setDrag", "(Z)V", "info", "Lnet/dev/important/gui/client/hud/element/ElementInfo;", "getInfo", "()Lnet/dev/important/gui/client/hud/element/ElementInfo;", "name", "", "getName", "()Ljava/lang/String;", "prevMouseX", "getPrevMouseX", "()F", "setPrevMouseX", "(F)V", "prevMouseY", "getPrevMouseY", "setPrevMouseY", "value", "renderX", "getRenderX", "()D", "setRenderX", "(D)V", "renderY", "getRenderY", "setRenderY", "getScale", "setScale", "getSide", "()Lnet/dev/important/gui/client/hud/element/Side;", "setSide", "(Lnet/dev/important/gui/client/hud/element/Side;)V", "values", "", "Lnet/dev/important/value/Value;", "getValues", "()Ljava/util/List;", "getX", "setX", "getY", "setY", "createElement", "destroyElement", "", "drawElement", "handleDamage", "ent", "Lnet/minecraft/entity/player/EntityPlayer;", "handleKey", "c", "", "keyCode", "", "handleMouseClick", "mouseButton", "isInBorder", "updateElement", "LiquidBounce"})
public abstract class Element
extends MinecraftInstance {
    private double x;
    private double y;
    @NotNull
    private Side side;
    @NotNull
    private final ElementInfo info;
    private float scale;
    @Nullable
    private Border border;
    private boolean drag;
    private float prevMouseX;
    private float prevMouseY;

    public Element(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkNotNullParameter(side, "side");
        this.x = x;
        this.y = y;
        this.side = side;
        ElementInfo elementInfo = this.getClass().getAnnotation(ElementInfo.class);
        if (elementInfo == null) {
            throw new IllegalArgumentException("Passed element with missing element info");
        }
        this.info = elementInfo;
        this.scale = 1.0f;
        this.setScale(scale);
    }

    public /* synthetic */ Element(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 2.0;
        }
        if ((n & 2) != 0) {
            d2 = 2.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = Side.Companion.default();
        }
        this(d, d2, f, side);
    }

    public final double getX() {
        return this.x;
    }

    public final void setX(double d) {
        this.x = d;
    }

    public final double getY() {
        return this.y;
    }

    public final void setY(double d) {
        this.y = d;
    }

    @NotNull
    public final Side getSide() {
        return this.side;
    }

    public final void setSide(@NotNull Side side) {
        Intrinsics.checkNotNullParameter(side, "<set-?>");
        this.side = side;
    }

    @NotNull
    public final ElementInfo getInfo() {
        return this.info;
    }

    public final float getScale() {
        if (this.info.disableScale()) {
            return 1.0f;
        }
        return this.scale;
    }

    public final void setScale(float value) {
        if (this.info.disableScale()) {
            return;
        }
        this.scale = value;
    }

    @NotNull
    public final String getName() {
        return this.info.name();
    }

    public final double getRenderX() {
        double d;
        switch (WhenMappings.$EnumSwitchMapping$0[this.side.getHorizontal().ordinal()]) {
            case 1: {
                d = this.x;
                break;
            }
            case 2: {
                d = (double)(new ScaledResolution(MinecraftInstance.mc).func_78326_a() / 2) - this.x;
                break;
            }
            case 3: {
                d = (double)new ScaledResolution(MinecraftInstance.mc).func_78326_a() - this.x;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return d;
    }

    public final void setRenderX(double value) {
        switch (WhenMappings.$EnumSwitchMapping$0[this.side.getHorizontal().ordinal()]) {
            case 1: {
                this.x += value;
                break;
            }
            case 2: 
            case 3: {
                this.x -= value;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
    }

    public final double getRenderY() {
        double d;
        switch (WhenMappings.$EnumSwitchMapping$1[this.side.getVertical().ordinal()]) {
            case 1: {
                d = this.y;
                break;
            }
            case 2: {
                d = (double)(new ScaledResolution(MinecraftInstance.mc).func_78328_b() / 2) - this.y;
                break;
            }
            case 3: {
                d = (double)new ScaledResolution(MinecraftInstance.mc).func_78328_b() - this.y;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return d;
    }

    public final void setRenderY(double value) {
        switch (WhenMappings.$EnumSwitchMapping$1[this.side.getVertical().ordinal()]) {
            case 1: {
                this.y += value;
                break;
            }
            case 2: 
            case 3: {
                this.y -= value;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
    }

    @Nullable
    public final Border getBorder() {
        return this.border;
    }

    public final void setBorder(@Nullable Border border) {
        this.border = border;
    }

    public final boolean getDrag() {
        return this.drag;
    }

    public final void setDrag(boolean bl) {
        this.drag = bl;
    }

    public final float getPrevMouseX() {
        return this.prevMouseX;
    }

    public final void setPrevMouseX(float f) {
        this.prevMouseX = f;
    }

    public final float getPrevMouseY() {
        return this.prevMouseY;
    }

    public final void setPrevMouseY(float f) {
        this.prevMouseY = f;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public List<Value<?>> getValues() {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$mapTo$iv$iv;
        Field[] fieldArray = this.getClass().getDeclaredFields();
        Intrinsics.checkNotNullExpressionValue(fieldArray, "javaClass.declaredFields");
        Object[] $this$map$iv = fieldArray;
        boolean $i$f$map = false;
        Object[] objectArray = $this$map$iv;
        Collection destination$iv$iv = new ArrayList($this$map$iv.length);
        boolean $i$f$mapTo = false;
        for (void item$iv$iv : $this$mapTo$iv$iv) {
            void valueField;
            Field field = (Field)item$iv$iv;
            Collection collection = destination$iv$iv;
            boolean bl = false;
            valueField.setAccessible(true);
            collection.add(valueField.get(this));
        }
        Iterable $this$filterIsInstance$iv = (List)destination$iv$iv;
        boolean $i$f$filterIsInstance = false;
        $this$mapTo$iv$iv = $this$filterIsInstance$iv;
        destination$iv$iv = new ArrayList();
        boolean $i$f$filterIsInstanceTo = false;
        for (Object element$iv$iv : $this$filterIsInstanceTo$iv$iv) {
            if (!(element$iv$iv instanceof Value)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    public boolean createElement() {
        return true;
    }

    public void destroyElement() {
    }

    @Nullable
    public abstract Border drawElement();

    public void updateElement() {
    }

    public boolean isInBorder(double x, double y) {
        Border border = this.border;
        if (border == null) {
            return false;
        }
        Border border2 = border;
        float minX = Math.min(border2.getX(), border2.getX2());
        float minY = Math.min(border2.getY(), border2.getY2());
        float maxX = Math.max(border2.getX(), border2.getX2());
        float maxY = Math.max(border2.getY(), border2.getY2());
        return (double)minX <= x && (double)minY <= y && (double)maxX >= x && (double)maxY >= y;
    }

    public void handleMouseClick(double x, double y, int mouseButton) {
    }

    public void handleKey(char c, int keyCode) {
    }

    public void handleDamage(@NotNull EntityPlayer ent) {
        Intrinsics.checkNotNullParameter(ent, "ent");
    }

    public Element() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] nArray = new int[Side.Horizontal.values().length];
            nArray[Side.Horizontal.LEFT.ordinal()] = 1;
            nArray[Side.Horizontal.MIDDLE.ordinal()] = 2;
            nArray[Side.Horizontal.RIGHT.ordinal()] = 3;
            $EnumSwitchMapping$0 = nArray;
            nArray = new int[Side.Vertical.values().length];
            nArray[Side.Vertical.UP.ordinal()] = 1;
            nArray[Side.Vertical.MIDDLE.ordinal()] = 2;
            nArray[Side.Vertical.DOWN.ordinal()] = 3;
            $EnumSwitchMapping$1 = nArray;
        }
    }
}

