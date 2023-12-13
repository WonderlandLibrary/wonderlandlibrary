package org.apache.logging.log4j.core.net;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;

public class DatagramSocketManager extends AbstractSocketManager {
   private static final DatagramSocketManager.DatagramSocketManagerFactory FACTORY = new DatagramSocketManager.DatagramSocketManagerFactory();

   protected DatagramSocketManager(String var1, OutputStream var2, InetAddress var3, String var4, int var5, Layout var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public static DatagramSocketManager getSocketManager(String var0, int var1, Layout var2) {
      if (Strings.isEmpty(var0)) {
         throw new IllegalArgumentException("A host name is required");
      } else if (var1 <= 0) {
         throw new IllegalArgumentException("A port value is required");
      } else {
         return (DatagramSocketManager)getManager("UDP:" + var0 + ":" + var1, new DatagramSocketManager.FactoryData(var0, var1, var2), FACTORY);
      }
   }

   public Map getContentFormat() {
      HashMap var1 = new HashMap(super.getContentFormat());
      var1.put("protocol", "udp");
      var1.put("direction", "out");
      return var1;
   }

   static Logger access$400() {
      return LOGGER;
   }

   private static class DatagramSocketManagerFactory implements ManagerFactory {
      private DatagramSocketManagerFactory() {
      }

      public DatagramSocketManager createManager(String var1, DatagramSocketManager.FactoryData var2) {
         DatagramOutputStream var4 = new DatagramOutputStream(DatagramSocketManager.FactoryData.access$100(var2), DatagramSocketManager.FactoryData.access$200(var2), DatagramSocketManager.FactoryData.access$300(var2).getHeader(), DatagramSocketManager.FactoryData.access$300(var2).getFooter());

         InetAddress var3;
         try {
            var3 = InetAddress.getByName(DatagramSocketManager.FactoryData.access$100(var2));
         } catch (UnknownHostException var6) {
            DatagramSocketManager.access$400().error((String)("Could not find address of " + DatagramSocketManager.FactoryData.access$100(var2)), (Throwable)var6);
            return null;
         }

         return new DatagramSocketManager(var1, var4, var3, DatagramSocketManager.FactoryData.access$100(var2), DatagramSocketManager.FactoryData.access$200(var2), DatagramSocketManager.FactoryData.access$300(var2));
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (DatagramSocketManager.FactoryData)var2);
      }

      DatagramSocketManagerFactory(Object var1) {
         this();
      }
   }

   private static class FactoryData {
      private final String host;
      private final int port;
      private final Layout layout;

      public FactoryData(String var1, int var2, Layout var3) {
         this.host = var1;
         this.port = var2;
         this.layout = var3;
      }

      static String access$100(DatagramSocketManager.FactoryData var0) {
         return var0.host;
      }

      static int access$200(DatagramSocketManager.FactoryData var0) {
         return var0.port;
      }

      static Layout access$300(DatagramSocketManager.FactoryData var0) {
         return var0.layout;
      }
   }
}
