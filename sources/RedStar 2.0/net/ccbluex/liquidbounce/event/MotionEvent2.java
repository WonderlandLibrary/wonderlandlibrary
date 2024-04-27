package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.EventState;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\u000020B50000\b0\t0\n¢J$0\nR\f0\rX¢\n\u0000\b\"\bR\t0\nX¢\n\u0000\b\"\bR\b0X¢\n\u0000\b\"\bR0X¢\n\u0000\b\"\bR0X¢\n\u0000\b\"\bR0X¢\n\u0000\b \"\b!R0X¢\n\u0000\b\"\"\b#¨%"}, d2={"Lnet/ccbluex/liquidbounce/event/MotionEvent2;", "Lnet/ccbluex/liquidbounce/event/Event;", "x", "", "y", "z", "yaw", "", "pitch", "onGround", "", "(DDDFFZ)V", "eventState", "Lnet/ccbluex/liquidbounce/event/EventState;", "getEventState", "()Lnet/ccbluex/liquidbounce/event/EventState;", "setEventState", "(Lnet/ccbluex/liquidbounce/event/EventState;)V", "getOnGround", "()Z", "setOnGround", "(Z)V", "getPitch", "()F", "setPitch", "(F)V", "getX", "()D", "setX", "(D)V", "getY", "setY", "getYaw", "setYaw", "getZ", "setZ", "isPre", "Pride"})
public final class MotionEvent2
extends Event {
    @NotNull
    private EventState eventState;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;

    @NotNull
    public final EventState getEventState() {
        return this.eventState;
    }

    public final void setEventState(@NotNull EventState eventState) {
        Intrinsics.checkParameterIsNotNull((Object)eventState, "<set-?>");
        this.eventState = eventState;
    }

    public final boolean isPre() {
        return this.eventState == EventState.PRE;
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

    public final double getZ() {
        return this.z;
    }

    public final void setZ(double d) {
        this.z = d;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float f) {
        this.yaw = f;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public final void setPitch(float f) {
        this.pitch = f;
    }

    public final boolean getOnGround() {
        return this.onGround;
    }

    public final void setOnGround(boolean bl) {
        this.onGround = bl;
    }

    public MotionEvent2(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.eventState = EventState.PRE;
    }
}
