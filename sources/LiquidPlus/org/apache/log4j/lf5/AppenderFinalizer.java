/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.lf5;

import org.apache.log4j.lf5.viewer.LogBrokerMonitor;

public class AppenderFinalizer {
    protected LogBrokerMonitor _defaultMonitor = null;

    public AppenderFinalizer(LogBrokerMonitor defaultMonitor) {
        this._defaultMonitor = defaultMonitor;
    }

    protected void finalize() throws Throwable {
        System.out.println("Disposing of the default LogBrokerMonitor instance");
        this._defaultMonitor.dispose();
    }
}

