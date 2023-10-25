/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Loggers {
    private final ConcurrentMap<String, LoggerConfig> map;
    private final LoggerConfig root;

    public Loggers(ConcurrentMap<String, LoggerConfig> map, LoggerConfig root) {
        this.map = map;
        this.root = root;
    }

    public ConcurrentMap<String, LoggerConfig> getMap() {
        return this.map;
    }

    public LoggerConfig getRoot() {
        return this.root;
    }
}

