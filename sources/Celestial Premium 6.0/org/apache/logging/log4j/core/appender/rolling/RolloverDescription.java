/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public interface RolloverDescription {
    public String getActiveFileName();

    public boolean getAppend();

    public Action getSynchronous();

    public Action getAsynchronous();
}

