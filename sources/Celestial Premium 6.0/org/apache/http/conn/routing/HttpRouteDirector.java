/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn.routing;

import org.apache.http.conn.routing.RouteInfo;

public interface HttpRouteDirector {
    public static final int UNREACHABLE = -1;
    public static final int COMPLETE = 0;
    public static final int CONNECT_TARGET = 1;
    public static final int CONNECT_PROXY = 2;
    public static final int TUNNEL_TARGET = 3;
    public static final int TUNNEL_PROXY = 4;
    public static final int LAYER_PROTOCOL = 5;

    public int nextStep(RouteInfo var1, RouteInfo var2);
}

