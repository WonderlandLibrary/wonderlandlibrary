/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element$WhenMappings;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.Nullable;

public abstract class Element
extends MinecraftInstance {
    private final ElementInfo info;
    private float scale;
    private Border border;
    private boolean drag;
    private float prevMouseX;
    private float prevMouseY;
    private double x;
    private double y;
    private Side side;

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

    public final String getName() {
        return this.info.name();
    }

    public final double getRenderX() {
        double d;
        switch (Element$WhenMappings.$EnumSwitchMapping$0[this.side.getHorizontal().ordinal()]) {
            case 1: {
                d = this.x;
                break;
            }
            case 2: {
                d = (double)(MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledWidth() / 2) - this.x;
                break;
            }
            case 3: {
                d = (double)MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledWidth() - this.x;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return d;
    }

    public final void setRenderX(double value) {
        switch (Element$WhenMappings.$EnumSwitchMapping$1[this.side.getHorizontal().ordinal()]) {
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
        switch (Element$WhenMappings.$EnumSwitchMapping$2[this.side.getVertical().ordinal()]) {
            case 1: {
                d = this.y;
                break;
            }
            case 2: {
                d = (double)(MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledHeight() / 2) - this.y;
                break;
            }
            case 3: {
                d = (double)MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledHeight() - this.y;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return d;
    }

    public final void setRenderY(double value) {
        switch (Element$WhenMappings.$EnumSwitchMapping$3[this.side.getVertical().ordinal()]) {
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
    public List<Value<?>> getValues() {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$mapTo$iv$iv;
        Field[] $this$map$iv = this.getClass().getDeclaredFields();
        boolean $i$f$map = false;
        Field[] fieldArray = $this$map$iv;
        Collection destination$iv$iv = new ArrayList($this$map$iv.length);
        boolean $i$f$mapTo = false;
        Iterator iterator = $this$mapTo$iv$iv;
        int n = ((void)iterator).length;
        for (int i = 0; i < n; ++i) {
            void valueField;
            void item$iv$iv;
            void var10_11 = item$iv$iv = iterator[i];
            Collection collection = destination$iv$iv;
            boolean bl = false;
            valueField.setAccessible(true);
            Object object = valueField.get(this);
            collection.add(object);
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
        float f = border2.getX();
        float f2 = border2.getX2();
        boolean bl = false;
        float minX = Math.min(f, f2);
        f2 = border2.getY();
        float f3 = border2.getY2();
        boolean bl2 = false;
        float minY = Math.min(f2, f3);
        f3 = border2.getX();
        float f4 = border2.getX2();
        boolean bl3 = false;
        float maxX = Math.max(f3, f4);
        f4 = border2.getY();
        float f5 = border2.getY2();
        boolean bl4 = false;
        float maxY = Math.max(f4, f5);
        return (double)minX <= x && (double)minY <= y && (double)maxX >= x && (double)maxY >= y;
    }

    public void handleMouseClick(double x, double y, int mouseButton) {
    }

    public void handleKey(char c, int keyCode) {
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

    public final Side getSide() {
        return this.side;
    }

    public final void setSide(Side side) {
        this.side = side;
    }

    public Element(double x, double y, float scale, Side side) {
        this.x = x;
        this.y = y;
        this.side = side;
        ElementInfo elementInfo = this.getClass().getAnnotation(ElementInfo.class);
        if (elementInfo == null) {
            throw (Throwable)new IllegalArgumentException("Passed element with missing element info");
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

    public Element() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

