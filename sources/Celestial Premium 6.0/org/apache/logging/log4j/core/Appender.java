/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import java.io.Serializable;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;

public interface Appender
extends LifeCycle {
    public void append(LogEvent var1);

    public String getName();

    public Layout<? extends Serializable> getLayout();

    public boolean ignoreExceptions();

    public ErrorHandler getHandler();

    public void setHandler(ErrorHandler var1);
}

