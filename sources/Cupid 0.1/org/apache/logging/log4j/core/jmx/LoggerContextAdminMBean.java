package org.apache.logging.log4j.core.jmx;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import javax.management.ObjectName;

public interface LoggerContextAdminMBean {
  public static final String PATTERN = "org.apache.logging.log4j2:type=%s";
  
  public static final String NOTIF_TYPE_RECONFIGURED = "com.apache.logging.log4j.core.jmx.config.reconfigured";
  
  ObjectName getObjectName();
  
  String getStatus();
  
  String getName();
  
  String getConfigLocationUri();
  
  void setConfigLocationUri(String paramString) throws URISyntaxException, IOException;
  
  String getConfigText() throws IOException;
  
  String getConfigText(String paramString) throws IOException;
  
  void setConfigText(String paramString1, String paramString2);
  
  String getConfigName();
  
  String getConfigClassName();
  
  String getConfigFilter();
  
  Map<String, String> getConfigProperties();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jmx\LoggerContextAdminMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */