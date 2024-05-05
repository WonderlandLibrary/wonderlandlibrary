/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.Nullable;

public final class Border {
    private final float x;
    private final float y;
    private final float x2;
    private final float y2;

    public final void draw() {
        RenderUtils.drawBorderedRect(this.x, this.y, this.x2, this.y2, 3.0f, Integer.MIN_VALUE, 0);
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final float getX2() {
        return this.x2;
    }

    public final float getY2() {
        return this.y2;
    }

    public Border(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public final float component1() {
        return this.x;
    }

    public final float component2() {
        return this.y;
    }

    public final float component3() {
        return this.x2;
    }

    public final float component4() {
        return this.y2;
    }

    public final Border copy(float x, float y, float x2, float y2) {
        return new Border(x, y, x2, y2);
    }

    public static /* synthetic */ Border copy$default(Border border, float f, float f2, float f3, float f4, int n, Object object) {
        if ((n & 1) != 0) {
            f = border.x;
        }
        if ((n & 2) != 0) {
            f2 = border.y;
        }
        if ((n & 4) != 0) {
            f3 = border.x2;
        }
        if ((n & 8) != 0) {
            f4 = border.y2;
        }
        return border.copy(f, f2, f3, f4);
    }

    public String toString() {
        return "Border(x=" + this.x + ", y=" + this.y + ", x2=" + this.x2 + ", y2=" + this.y2 + ")";
    }

    public int hashCode() {
        return ((Float.hashCode(this.x) * 31 + Float.hashCode(this.y)) * 31 + Float.hashCode(this.x2)) * 31 + Float.hashCode(this.y2);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof Border)) break block3;
                Border border = (Border)object;
                if (Float.compare(this.x, border.x) != 0 || Float.compare(this.y, border.y) != 0 || Float.compare(this.x2, border.x2) != 0 || Float.compare(this.y2, border.y2) != 0) break block3;
            }
            return true;
        }
        return false;
    }
}

