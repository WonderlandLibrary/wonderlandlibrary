/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import java.io.Serializable;

public interface Message
extends Serializable {
    public String getFormattedMessage();

    public String getFormat();

    public Object[] getParameters();

    public Throwable getThrowable();
}

