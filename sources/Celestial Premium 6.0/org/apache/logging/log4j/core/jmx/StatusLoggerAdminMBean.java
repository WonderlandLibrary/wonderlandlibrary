/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.jmx;

import java.util.List;
import org.apache.logging.log4j.status.StatusData;

public interface StatusLoggerAdminMBean {
    public static final String NAME = "org.apache.logging.log4j2:type=StatusLogger";
    public static final String NOTIF_TYPE_DATA = "com.apache.logging.log4j.core.jmx.statuslogger.data";
    public static final String NOTIF_TYPE_MESSAGE = "com.apache.logging.log4j.core.jmx.statuslogger.message";

    public List<StatusData> getStatusData();

    public String[] getStatusDataHistory();

    public String getLevel();

    public void setLevel(String var1);
}

