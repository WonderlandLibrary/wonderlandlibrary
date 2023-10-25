/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;

public interface LoggerContext {
    public Object getExternalContext();

    public Logger getLogger(String var1);

    public Logger getLogger(String var1, MessageFactory var2);

    public boolean hasLogger(String var1);
}

