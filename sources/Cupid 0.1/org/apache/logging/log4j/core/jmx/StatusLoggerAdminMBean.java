package org.apache.logging.log4j.core.jmx;

import java.util.List;
import javax.management.ObjectName;
import org.apache.logging.log4j.status.StatusData;

public interface StatusLoggerAdminMBean {
  public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=StatusLogger";
  
  public static final String NOTIF_TYPE_DATA = "com.apache.logging.log4j.core.jmx.statuslogger.data";
  
  public static final String NOTIF_TYPE_MESSAGE = "com.apache.logging.log4j.core.jmx.statuslogger.message";
  
  ObjectName getObjectName();
  
  List<StatusData> getStatusData();
  
  String[] getStatusDataHistory();
  
  String getLevel();
  
  void setLevel(String paramString);
  
  String getContextName();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jmx\StatusLoggerAdminMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */