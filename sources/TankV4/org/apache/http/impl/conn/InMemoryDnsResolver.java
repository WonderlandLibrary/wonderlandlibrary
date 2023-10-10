package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.DnsResolver;
import org.apache.http.util.Args;

public class InMemoryDnsResolver implements DnsResolver {
   private final Log log = LogFactory.getLog(InMemoryDnsResolver.class);
   private final Map dnsMap = new ConcurrentHashMap();

   public void add(String var1, InetAddress... var2) {
      Args.notNull(var1, "Host name");
      Args.notNull(var2, "Array of IP addresses");
      this.dnsMap.put(var1, var2);
   }

   public InetAddress[] resolve(String var1) throws UnknownHostException {
      InetAddress[] var2 = (InetAddress[])this.dnsMap.get(var1);
      if (this.log.isInfoEnabled()) {
         this.log.info("Resolving " + var1 + " to " + Arrays.deepToString(var2));
      }

      if (var2 == null) {
         throw new UnknownHostException(var1 + " cannot be resolved");
      } else {
         return var2;
      }
   }
}
