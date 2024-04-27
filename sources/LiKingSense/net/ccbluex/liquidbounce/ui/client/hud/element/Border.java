/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J;\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u0003H\u00c6\u0001J&\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0002\u001a\u00020\u00172\u0006\u0010\u0004\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u0019H\u00d6\u0001J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u00a8\u0006!"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "", "x", "", "y", "x2", "y2", "radius", "(FFFFF)V", "getRadius", "()F", "getX", "getX2", "getY", "getY2", "component1", "component2", "component3", "component4", "component5", "copy", "draw", "", "", "mouseX", "", "mouseY", "equals", "", "other", "hashCode", "toString", "", "LiKingSense"})
public final class Border {
    private final float x;
    private final float y;
    private final float x2;
    private final float y2;
    private final float radius;

    public final void draw(double x, double y, int mouseX, int mouseY) {
        GlStateManager.func_179117_G();
        if (RenderUtils.isHovered((float)x, (float)y, this.x2, this.y2, mouseX, mouseY)) {
            double d = this.x;
            double d2 = this.y;
            double d3 = this.x2;
            double d4 = this.y2;
            double d5 = this.radius;
            Color color = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
            RenderUtils.drawOutlinedRoundedRect(d, d2, d3, d4, d5, 0.5f, color.getRGB());
            GlStateManager.func_179117_G();
        }
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

    public final float getRadius() {
        return this.radius;
    }

    public Border(float x, float y, float x2, float y2, float radius) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.radius = radius;
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

    public final float component5() {
        return this.radius;
    }

    @NotNull
    public final Border copy(float x, float y, float x2, float y2, float radius) {
        return new Border(x, y, x2, y2, radius);
    }

    public static /* synthetic */ Border copy$default(Border border, float f, float f2, float f3, float f4, float f5, int n, Object object) {
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
        if ((n & 0x10) != 0) {
            f5 = border.radius;
        }
        return border.copy(f, f2, f3, f4, f5);
    }

    @NotNull
    public String toString() {
        return "Border(x=" + this.x + ", y=" + this.y + ", x2=" + this.x2 + ", y2=" + this.y2 + ", radius=" + this.radius + ")";
    }

    public int hashCode() {
        return (((Float.hashCode(this.x) * 31 + Float.hashCode(this.y)) * 31 + Float.hashCode(this.x2)) * 31 + Float.hashCode(this.y2)) * 31 + Float.hashCode(this.radius);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof Border)) break block3;
                Border border = (Border)object;
                if (Float.compare(this.x, border.x) != 0 || Float.compare(this.y, border.y) != 0 || Float.compare(this.x2, border.x2) != 0 || Float.compare(this.y2, border.y2) != 0 || Float.compare(this.radius, border.radius) != 0) break block3;
            }
            return true;
        }
        return false;
    }
}

