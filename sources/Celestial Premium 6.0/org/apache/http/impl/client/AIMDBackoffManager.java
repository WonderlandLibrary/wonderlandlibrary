/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.client;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.BackoffManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.Clock;
import org.apache.http.impl.client.SystemClock;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.util.Args;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class AIMDBackoffManager
implements BackoffManager {
    private final ConnPoolControl<HttpRoute> connPerRoute;
    private final Clock clock;
    private final Map<HttpRoute, Long> lastRouteProbes;
    private final Map<HttpRoute, Long> lastRouteBackoffs;
    private long coolDown = 5000L;
    private double backoffFactor = 0.5;
    private int cap = 2;

    public AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute) {
        this(connPerRoute, new SystemClock());
    }

    AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute, Clock clock) {
        this.clock = clock;
        this.connPerRoute = connPerRoute;
        this.lastRouteProbes = new HashMap<HttpRoute, Long>();
        this.lastRouteBackoffs = new HashMap<HttpRoute, Long>();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void backOff(HttpRoute route) {
        ConnPoolControl<HttpRoute> connPoolControl = this.connPerRoute;
        synchronized (connPoolControl) {
            int curr = this.connPerRoute.getMaxPerRoute(route);
            Long lastUpdate = this.getLastUpdate(this.lastRouteBackoffs, route);
            long now = this.clock.getCurrentTime();
            if (now - lastUpdate < this.coolDown) {
                return;
            }
            this.connPerRoute.setMaxPerRoute(route, this.getBackedOffPoolSize(curr));
            this.lastRouteBackoffs.put(route, now);
        }
    }

    private int getBackedOffPoolSize(int curr) {
        if (curr <= 1) {
            return 1;
        }
        return (int)Math.floor(this.backoffFactor * (double)curr);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void probe(HttpRoute route) {
        ConnPoolControl<HttpRoute> connPoolControl = this.connPerRoute;
        synchronized (connPoolControl) {
            int curr = this.connPerRoute.getMaxPerRoute(route);
            int max = curr >= this.cap ? this.cap : curr + 1;
            Long lastProbe = this.getLastUpdate(this.lastRouteProbes, route);
            Long lastBackoff = this.getLastUpdate(this.lastRouteBackoffs, route);
            long now = this.clock.getCurrentTime();
            if (now - lastProbe < this.coolDown || now - lastBackoff < this.coolDown) {
                return;
            }
            this.connPerRoute.setMaxPerRoute(route, max);
            this.lastRouteProbes.put(route, now);
        }
    }

    private Long getLastUpdate(Map<HttpRoute, Long> updates, HttpRoute route) {
        Long lastUpdate = updates.get(route);
        if (lastUpdate == null) {
            lastUpdate = 0L;
        }
        return lastUpdate;
    }

    public void setBackoffFactor(double d) {
        Args.check(d > 0.0 && d < 1.0, "Backoff factor must be 0.0 < f < 1.0");
        this.backoffFactor = d;
    }

    public void setCooldownMillis(long l) {
        Args.positive(this.coolDown, "Cool down");
        this.coolDown = l;
    }

    public void setPerHostConnectionCap(int cap) {
        Args.positive(cap, "Per host connection cap");
        this.cap = cap;
    }
}

