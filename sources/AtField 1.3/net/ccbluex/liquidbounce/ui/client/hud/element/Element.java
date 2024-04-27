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
    private Border border;
    private Side side;
    private float prevMouseX;
    private boolean drag;
    private double y;
    private float prevMouseY;
    private float scale;
    private double x;
    private final ElementInfo info;

    public final void setDrag(boolean bl) {
        this.drag = bl;
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

    public final void setX(double d) {
        this.x = d;
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

    public final Side getSide() {
        return this.side;
    }

    public void handleKey(char c, int n) {
    }

    public final void setBorder(@Nullable Border border) {
        this.border = border;
    }

    public final float getPrevMouseX() {
        return this.prevMouseX;
    }

    public List getValues() {
        Object object = this.getClass().getDeclaredFields();
        boolean bl = false;
        Field[] fieldArray = object;
        Collection collection = new ArrayList(((Field[])object).length);
        boolean bl2 = false;
        Field[] fieldArray2 = fieldArray;
        int n = fieldArray2.length;
        for (int i = 0; i < n; ++i) {
            Field field;
            Field field2 = field = fieldArray2[i];
            Collection collection2 = collection;
            boolean bl3 = false;
            field2.setAccessible(true);
            Object object2 = field2.get(this);
            collection2.add(object2);
        }
        object = (List)collection;
        bl = false;
        fieldArray = object;
        collection = new ArrayList();
        bl2 = false;
        for (Object e : fieldArray) {
            if (!(e instanceof Value)) continue;
            collection.add(e);
        }
        return (List)collection;
    }

    public void updateElement() {
    }

    public final void setPrevMouseX(float f) {
        this.prevMouseX = f;
    }

    public final void setSide(Side side) {
        this.side = side;
    }

    public final void setY(double d) {
        this.y = d;
    }

    public final void setRenderX(double d) {
        switch (Element$WhenMappings.$EnumSwitchMapping$1[this.side.getHorizontal().ordinal()]) {
            case 1: {
                this.x += d;
                break;
            }
            case 2: 
            case 3: {
                this.x -= d;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
    }

    public Element() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public final void setScale(float f) {
        if (this.info.disableScale()) {
            return;
        }
        this.scale = f;
    }

    public final float getPrevMouseY() {
        return this.prevMouseY;
    }

    public boolean createElement() {
        return true;
    }

    public Element(double d, double d2, float f, Side side) {
        this.x = d;
        this.y = d2;
        this.side = side;
        ElementInfo elementInfo = this.getClass().getAnnotation(ElementInfo.class);
        if (elementInfo == null) {
            throw (Throwable)new IllegalArgumentException("Passed element with missing element info");
        }
        this.info = elementInfo;
        this.scale = 1.0f;
        this.setScale(f);
    }

    public final Border getBorder() {
        return this.border;
    }

    public void shadow() {
    }

    public final ElementInfo getInfo() {
        return this.info;
    }

    public final void setRenderY(double d) {
        switch (Element$WhenMappings.$EnumSwitchMapping$3[this.side.getVertical().ordinal()]) {
            case 1: {
                this.y += d;
                break;
            }
            case 2: 
            case 3: {
                this.y -= d;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final boolean getDrag() {
        return this.drag;
    }

    public Element(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    @Nullable
    public abstract Border drawElement();

    public final void setPrevMouseY(float f) {
        this.prevMouseY = f;
    }

    public boolean isInBorder(double d, double d2) {
        Border border = this.border;
        if (border == null) {
            return false;
        }
        Border border2 = border;
        float f = border2.getX();
        float f2 = border2.getX2();
        boolean bl = false;
        float f3 = Math.min(f, f2);
        f2 = border2.getY();
        float f4 = border2.getY2();
        boolean bl2 = false;
        f = Math.min(f2, f4);
        f4 = border2.getX();
        float f5 = border2.getX2();
        boolean bl3 = false;
        f2 = Math.max(f4, f5);
        f5 = border2.getY();
        float f6 = border2.getY2();
        boolean bl4 = false;
        f4 = Math.max(f5, f6);
        return (double)f3 <= d && (double)f <= d2 && (double)f2 >= d && (double)f4 >= d2;
    }

    public final float getScale() {
        if (this.info.disableScale()) {
            return 1.0f;
        }
        return this.scale;
    }

    public void destroyElement() {
    }

    public void handleMouseClick(double d, double d2, int n) {
    }

    public final String getName() {
        return this.info.name();
    }
}

