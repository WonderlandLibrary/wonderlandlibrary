/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.varia;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class DenyAllFilter
extends Filter {
    public String[] getOptionStrings() {
        return null;
    }

    public void setOption(String key, String value) {
    }

    public int decide(LoggingEvent event) {
        return -1;
    }
}

