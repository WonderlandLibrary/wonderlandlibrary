/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.jmx;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public interface LoggerContextAdminMBean {
    public static final String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s";
    public static final String NOTIF_TYPE_RECONFIGURED = "com.apache.logging.log4j.core.jmx.config.reconfigured";

    public String getStatus();

    public String getName();

    public String getConfigLocationURI();

    public void setConfigLocationURI(String var1) throws URISyntaxException, IOException;

    public String getConfigText() throws IOException;

    public String getConfigText(String var1) throws IOException;

    public void setConfigText(String var1, String var2);

    public String getConfigName();

    public String getConfigClassName();

    public String getConfigFilter();

    public String getConfigMonitorClassName();

    public Map<String, String> getConfigProperties();
}

