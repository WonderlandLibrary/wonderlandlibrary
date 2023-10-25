package org.apache.logging.log4j.core.net;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.OutputStreamManager;

public abstract class AbstractSocketManager extends OutputStreamManager {
  protected final InetAddress inetAddress;
  
  protected final String host;
  
  protected final int port;
  
  public AbstractSocketManager(String name, OutputStream os, InetAddress inetAddress, String host, int port, Layout<? extends Serializable> layout, boolean writeHeader, int bufferSize) {
    super(os, name, layout, writeHeader, bufferSize);
    this.inetAddress = inetAddress;
    this.host = host;
    this.port = port;
  }
  
  public Map<String, String> getContentFormat() {
    Map<String, String> result = new HashMap<>(super.getContentFormat());
    result.put("port", Integer.toString(this.port));
    result.put("address", this.inetAddress.getHostAddress());
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\AbstractSocketManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */