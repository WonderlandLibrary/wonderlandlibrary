/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

public interface ManagerFactory<M, T> {
    public M createManager(String var1, T var2);
}

