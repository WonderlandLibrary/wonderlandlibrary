/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.slf4j.IMarkerFactory
 *  org.slf4j.Marker
 */
package org.apache.logging.slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import org.apache.logging.log4j.MarkerManager;
import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;

class Log4jMarker
implements Marker {
    public static final long serialVersionUID = 1590472L;
    private final IMarkerFactory factory;
    private final org.apache.logging.log4j.Marker marker;

    public Log4jMarker(IMarkerFactory markerFactory, org.apache.logging.log4j.Marker marker) {
        this.factory = markerFactory;
        this.marker = marker;
    }

    public void add(Marker marker) {
        if (marker == null) {
            throw new IllegalArgumentException();
        }
        Marker m = this.factory.getMarker(marker.getName());
        this.marker.addParents(new org.apache.logging.log4j.Marker[]{((Log4jMarker)m).getLog4jMarker()});
    }

    public boolean contains(Marker marker) {
        if (marker == null) {
            throw new IllegalArgumentException();
        }
        return this.marker.isInstanceOf(marker.getName());
    }

    public boolean contains(String s) {
        return s != null ? this.marker.isInstanceOf(s) : false;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Log4jMarker)) {
            return false;
        }
        Log4jMarker other = (Log4jMarker)obj;
        return Objects.equals(this.marker, other.marker);
    }

    public org.apache.logging.log4j.Marker getLog4jMarker() {
        return this.marker;
    }

    public String getName() {
        return this.marker.getName();
    }

    public boolean hasChildren() {
        return this.marker.hasParents();
    }

    public int hashCode() {
        return 31 + Objects.hashCode(this.marker);
    }

    public boolean hasReferences() {
        return this.marker.hasParents();
    }

    public Iterator<Marker> iterator() {
        org.apache.logging.log4j.Marker[] log4jParents = this.marker.getParents();
        ArrayList<Marker> parents = new ArrayList<Marker>(log4jParents.length);
        for (org.apache.logging.log4j.Marker m : log4jParents) {
            parents.add(this.factory.getMarker(m.getName()));
        }
        return parents.iterator();
    }

    public boolean remove(Marker marker) {
        return marker != null ? this.marker.remove(MarkerManager.getMarker(marker.getName())) : false;
    }
}

