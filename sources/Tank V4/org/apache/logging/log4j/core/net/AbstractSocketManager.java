package org.apache.logging.log4j.core.net;

import java.io.OutputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.OutputStreamManager;

public abstract class AbstractSocketManager extends OutputStreamManager {
   protected final InetAddress address;
   protected final String host;
   protected final int port;

   public AbstractSocketManager(String var1, OutputStream var2, InetAddress var3, String var4, int var5, Layout var6) {
      super(var2, var1, var6);
      this.address = var3;
      this.host = var4;
      this.port = var5;
   }

   public Map getContentFormat() {
      HashMap var1 = new HashMap(super.getContentFormat());
      var1.put("port", Integer.toString(this.port));
      var1.put("address", this.address.getHostAddress());
      return var1;
   }
}
