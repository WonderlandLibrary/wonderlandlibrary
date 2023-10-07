/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.spi;

import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;

public class DefaultRepositorySelector
implements RepositorySelector {
    final LoggerRepository repository;

    public DefaultRepositorySelector(LoggerRepository repository) {
        this.repository = repository;
    }

    public LoggerRepository getLoggerRepository() {
        return this.repository;
    }
}

