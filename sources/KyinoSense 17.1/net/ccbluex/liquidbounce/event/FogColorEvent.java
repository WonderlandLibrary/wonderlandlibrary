/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.JvmName;
import net.ccbluex.liquidbounce.event.Event;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0015\u0010\u000f\u001a\u00020\u00152\u0006\u0010\u0005\u001a\u00020\u0007H\u0007\u00a2\u0006\u0002\b\u0016J\u0015\u0010\u0012\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0007H\u0007\u00a2\u0006\u0002\b\u0017J\u0015\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u0007H\u0007\u00a2\u0006\u0002\b\u0018R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000e\"\u0004\b\u0012\u0010\u0010R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000e\"\u0004\b\u0014\u0010\u0010\u00a8\u0006\u0019"}, d2={"Lnet/ccbluex/liquidbounce/event/FogColorEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "red", "", "green", "blue", "alpha", "", "(FFFI)V", "getAlpha", "()I", "setAlpha", "(I)V", "getBlue", "()F", "setBlue", "(F)V", "getGreen", "setGreen", "getRed", "setRed", "", "setBlue1", "setGreen1", "setRed1", "KyinoClient"})
public final class FogColorEvent
extends Event {
    private float red;
    private float green;
    private float blue;
    private int alpha;

    @JvmName(name="setRed1")
    public final void setRed1(int red) {
        this.red = red;
    }

    @JvmName(name="setGreen1")
    public final void setGreen1(int green) {
        this.green = green;
    }

    @JvmName(name="setBlue1")
    public final void setBlue1(int blue) {
        this.blue = blue;
    }

    public final float getRed() {
        return this.red;
    }

    public final void setRed(float f) {
        this.red = f;
    }

    public final float getGreen() {
        return this.green;
    }

    public final void setGreen(float f) {
        this.green = f;
    }

    public final float getBlue() {
        return this.blue;
    }

    public final void setBlue(float f) {
        this.blue = f;
    }

    public final int getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(int n) {
        this.alpha = n;
    }

    public FogColorEvent(float red, float green, float blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
}

