/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.spi.Provider;

public class Log4jProvider
extends Provider {
    public Log4jProvider() {
        super(Integer.valueOf(10), "2.6.0", Log4jContextFactory.class);
    }
}

