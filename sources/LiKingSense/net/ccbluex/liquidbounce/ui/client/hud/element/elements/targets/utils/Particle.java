/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.utils;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.utils.ShapeType;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u0006\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ8\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\u00052\u0006\u0010*\u001a\u00020\u00052\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\u00052\b\b\u0002\u0010/\u001a\u00020,R\u001a\u0010\u000b\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\r\"\u0004\b\u0015\u0010\u000fR\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\r\"\u0004\b\u0017\u0010\u000fR\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u00020\u001dX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001a\u0010\u0007\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\r\"\u0004\b#\u0010\u000fR\u001a\u0010$\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\r\"\u0004\b&\u0010\u000f\u00a8\u00060"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/Particle;", "", "color", "Ljava/awt/Color;", "distX", "", "distY", "radius", "drawType", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "(Ljava/awt/Color;FFFLnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;)V", "alpha", "getAlpha", "()F", "setAlpha", "(F)V", "getColor", "()Ljava/awt/Color;", "setColor", "(Ljava/awt/Color;)V", "getDistX", "setDistX", "getDistY", "setDistY", "getDrawType", "()Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "setDrawType", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;)V", "progress", "", "getProgress", "()D", "setProgress", "(D)V", "getRadius", "setRadius", "rotate", "getRotate", "setRotate", "render", "", "x", "y", "fade", "", "speed", "fadeSpeed", "canRotate", "LiKingSense"})
public final class Particle {
    private float alpha;
    private double progress;
    private float rotate;
    @NotNull
    private Color color;
    private float distX;
    private float distY;
    private float radius;
    @NotNull
    private ShapeType drawType;

    public final float getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(float f) {
        this.alpha = f;
    }

    public final double getProgress() {
        return this.progress;
    }

    public final void setProgress(double d) {
        this.progress = d;
    }

    public final float getRotate() {
        return this.rotate;
    }

    public final void setRotate(float f) {
        this.rotate = f;
    }

    public final void render(float x, float y, boolean fade, float speed, float fadeSpeed, boolean canRotate) {
        if (this.progress >= 1.0) {
            this.progress = 1.0;
            if (fade) {
                this.alpha -= fadeSpeed * 0.02f * (float)RenderUtils.deltaTime;
            }
            if (this.alpha < 0.0f) {
                this.alpha = 0.0f;
            }
        } else {
            this.progress += (double)(speed * 0.025f * (float)RenderUtils.deltaTime);
        }
        if (this.alpha <= 0.0f) {
            return;
        }
        Color reColored = new Color((float)this.color.getRed() / 255.0f, (float)this.color.getGreen() / 255.0f, (float)this.color.getBlue() / 255.0f, this.alpha);
        float easeOut = (float)EaseUtils.easeOutQuart(this.progress);
        if (canRotate && this.drawType != ShapeType.SOLID_CIRCLE && this.drawType != ShapeType.CIRCLE) {
            this.rotate += 10.0f * (1.0f - easeOut);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(x + this.distX * easeOut), (float)(y + this.distY * easeOut), (float)0.0f);
            GL11.glPushMatrix();
            GL11.glRotatef((float)this.rotate, (float)0.0f, (float)0.0f, (float)1.0f);
            this.drawType.performRendering(0.0f, 0.0f, this.radius, reColored);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        } else {
            this.drawType.performRendering(x + this.distX * easeOut, y + this.distY * easeOut, this.radius, reColored);
        }
    }

    public static /* synthetic */ void render$default(Particle particle, float f, float f2, boolean bl, float f3, float f4, boolean bl2, int n, Object object) {
        if ((n & 0x20) != 0) {
            bl2 = false;
        }
        particle.render(f, f2, bl, f3, f4, bl2);
    }

    @NotNull
    public final Color getColor() {
        return this.color;
    }

    public final void setColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull((Object)color, (String)"<set-?>");
        this.color = color;
    }

    public final float getDistX() {
        return this.distX;
    }

    public final void setDistX(float f) {
        this.distX = f;
    }

    public final float getDistY() {
        return this.distY;
    }

    public final void setDistY(float f) {
        this.distY = f;
    }

    public final float getRadius() {
        return this.radius;
    }

    public final void setRadius(float f) {
        this.radius = f;
    }

    @NotNull
    public final ShapeType getDrawType() {
        return this.drawType;
    }

    public final void setDrawType(@NotNull ShapeType shapeType) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)shapeType), (String)"<set-?>");
        this.drawType = shapeType;
    }

    public Particle(@NotNull Color color, float distX, float distY, float radius, @NotNull ShapeType drawType) {
        Intrinsics.checkParameterIsNotNull((Object)color, (String)"color");
        Intrinsics.checkParameterIsNotNull((Object)((Object)drawType), (String)"drawType");
        this.color = color;
        this.distX = distX;
        this.distY = distY;
        this.radius = radius;
        this.drawType = drawType;
        this.alpha = 1.0f;
    }

    public /* synthetic */ Particle(Color color, float f, float f2, float f3, ShapeType shapeType, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 0x10) != 0) {
            shapeType = ShapeType.SOLID_CIRCLE;
        }
        this(color, f, f2, f3, shapeType);
    }
}

