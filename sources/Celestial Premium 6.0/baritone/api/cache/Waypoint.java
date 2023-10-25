/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import baritone.api.cache.IWaypoint;
import baritone.api.utils.BetterBlockPos;
import java.util.Date;

public class Waypoint
implements IWaypoint {
    private final String name;
    private final IWaypoint.Tag tag;
    private final long creationTimestamp;
    private final BetterBlockPos location;

    public Waypoint(String name, IWaypoint.Tag tag, BetterBlockPos location) {
        this(name, tag, location, System.currentTimeMillis());
    }

    public Waypoint(String name, IWaypoint.Tag tag, BetterBlockPos location, long creationTimestamp) {
        this.name = name;
        this.tag = tag;
        this.location = location;
        this.creationTimestamp = creationTimestamp;
    }

    public int hashCode() {
        return this.name.hashCode() ^ this.tag.hashCode() ^ this.location.hashCode() ^ Long.hashCode(this.creationTimestamp);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IWaypoint.Tag getTag() {
        return this.tag;
    }

    @Override
    public long getCreationTimestamp() {
        return this.creationTimestamp;
    }

    @Override
    public BetterBlockPos getLocation() {
        return this.location;
    }

    public String toString() {
        return String.format("%s %s %s", this.name, BetterBlockPos.from(this.location).toString(), new Date(this.creationTimestamp).toString());
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof IWaypoint)) {
            return false;
        }
        IWaypoint w = (IWaypoint)o;
        return this.name.equals(w.getName()) && this.tag == w.getTag() && this.location.equals(w.getLocation());
    }
}

