/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;

public interface RouteInfo {
    public HttpHost getTargetHost();

    public InetAddress getLocalAddress();

    public int getHopCount();

    public HttpHost getHopTarget(int var1);

    public HttpHost getProxyHost();

    public TunnelType getTunnelType();

    public boolean isTunnelled();

    public LayerType getLayerType();

    public boolean isLayered();

    public boolean isSecure();

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum LayerType {
        PLAIN,
        LAYERED;

    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum TunnelType {
        PLAIN,
        TUNNELLED;

    }
}

