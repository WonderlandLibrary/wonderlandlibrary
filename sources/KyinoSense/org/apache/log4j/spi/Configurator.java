/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.spi;

import java.io.InputStream;
import java.net.URL;
import org.apache.log4j.spi.LoggerRepository;

public interface Configurator {
    public static final String INHERITED = "inherited";
    public static final String NULL = "null";

    public void doConfigure(InputStream var1, LoggerRepository var2);

    public void doConfigure(URL var1, LoggerRepository var2);
}

