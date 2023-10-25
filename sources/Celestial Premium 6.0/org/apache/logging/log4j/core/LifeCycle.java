/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

public interface LifeCycle {
    public void start();

    public void stop();

    public boolean isStarted();
}

