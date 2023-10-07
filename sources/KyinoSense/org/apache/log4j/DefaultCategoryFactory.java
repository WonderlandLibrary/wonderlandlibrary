/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

class DefaultCategoryFactory
implements LoggerFactory {
    DefaultCategoryFactory() {
    }

    public Logger makeNewLoggerInstance(String name) {
        return new Logger(name);
    }
}

