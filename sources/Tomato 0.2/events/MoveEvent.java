/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.Event;

public class MoveEvent
implements Event {
    public double x;
    public double y;
    public double z;

    public MoveEvent(double x2, double y2, double z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }
}

