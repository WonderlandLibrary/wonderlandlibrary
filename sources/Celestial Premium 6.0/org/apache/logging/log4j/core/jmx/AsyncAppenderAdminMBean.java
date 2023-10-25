/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.jmx;

public interface AsyncAppenderAdminMBean {
    public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s";

    public String getName();

    public String getLayout();

    public boolean isIgnoreExceptions();

    public String getErrorHandler();

    public String getFilter();

    public String[] getAppenderRefs();

    public boolean isIncludeLocation();

    public boolean isBlocking();

    public String getErrorRef();

    public int getQueueCapacity();

    public int getQueueRemainingCapacity();
}

