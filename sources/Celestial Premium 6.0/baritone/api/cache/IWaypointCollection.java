/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import baritone.api.cache.IWaypoint;
import java.util.Set;

public interface IWaypointCollection {
    public void addWaypoint(IWaypoint var1);

    public void removeWaypoint(IWaypoint var1);

    public IWaypoint getMostRecentByTag(IWaypoint.Tag var1);

    public Set<IWaypoint> getByTag(IWaypoint.Tag var1);

    public Set<IWaypoint> getAllWaypoints();
}

