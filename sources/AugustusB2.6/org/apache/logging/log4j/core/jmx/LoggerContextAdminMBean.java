// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.Map;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.management.ObjectName;

public interface LoggerContextAdminMBean
{
    public static final String PATTERN = "org.apache.logging.log4j2:type=%s";
    public static final String NOTIF_TYPE_RECONFIGURED = "com.apache.logging.log4j.core.jmx.config.reconfigured";
    
    ObjectName getObjectName();
    
    String getStatus();
    
    String getName();
    
    String getConfigLocationUri();
    
    void setConfigLocationUri(final String configLocation) throws URISyntaxException, IOException;
    
    String getConfigText() throws IOException;
    
    String getConfigText(final String charsetName) throws IOException;
    
    void setConfigText(final String configText, final String charsetName);
    
    String getConfigName();
    
    String getConfigClassName();
    
    String getConfigFilter();
    
    Map<String, String> getConfigProperties();
}
