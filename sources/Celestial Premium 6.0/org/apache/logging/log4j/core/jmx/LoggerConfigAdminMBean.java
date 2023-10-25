/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.jmx;

public interface LoggerConfigAdminMBean {
    public static final String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s";

    public String getName();

    public String getLevel();

    public void setLevel(String var1);

    public boolean isAdditive();

    public void setAdditive(boolean var1);

    public boolean isIncludeLocation();

    public String getFilter();

    public String[] getAppenderRefs();
}

