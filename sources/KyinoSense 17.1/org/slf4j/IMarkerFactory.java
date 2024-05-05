/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j;

import org.slf4j.Marker;

public interface IMarkerFactory {
    public Marker getMarker(String var1);

    public boolean exists(String var1);

    public boolean detachMarker(String var1);

    public Marker getDetachedMarker(String var1);
}

