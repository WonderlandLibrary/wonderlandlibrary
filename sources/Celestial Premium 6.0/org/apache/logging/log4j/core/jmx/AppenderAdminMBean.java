/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.jmx;

public interface AppenderAdminMBean {
    public static final String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s";

    public String getName();

    public String getLayout();

    public boolean isExceptionSuppressed();

    public String getErrorHandler();
}

