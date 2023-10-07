/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.helpers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarker;

public class BasicMarkerFactory
implements IMarkerFactory {
    private final ConcurrentMap<String, Marker> markerMap = new ConcurrentHashMap<String, Marker>();

    public Marker getMarker(String name) {
        Marker oldMarker;
        if (name == null) {
            throw new IllegalArgumentException("Marker name cannot be null");
        }
        Marker marker = (Marker)this.markerMap.get(name);
        if (marker == null && (oldMarker = this.markerMap.putIfAbsent(name, marker = new BasicMarker(name))) != null) {
            marker = oldMarker;
        }
        return marker;
    }

    public boolean exists(String name) {
        if (name == null) {
            return false;
        }
        return this.markerMap.containsKey(name);
    }

    public boolean detachMarker(String name) {
        if (name == null) {
            return false;
        }
        return this.markerMap.remove(name) != null;
    }

    public Marker getDetachedMarker(String name) {
        return new BasicMarker(name);
    }
}

