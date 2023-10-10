// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import org.apache.logging.log4j.status.StatusData;
import java.util.List;
import javax.management.ObjectName;

public interface StatusLoggerAdminMBean
{
    public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=StatusLogger";
    public static final String NOTIF_TYPE_DATA = "com.apache.logging.log4j.core.jmx.statuslogger.data";
    public static final String NOTIF_TYPE_MESSAGE = "com.apache.logging.log4j.core.jmx.statuslogger.message";
    
    ObjectName getObjectName();
    
    List<StatusData> getStatusData();
    
    String[] getStatusDataHistory();
    
    String getLevel();
    
    void setLevel(final String level);
    
    String getContextName();
}
