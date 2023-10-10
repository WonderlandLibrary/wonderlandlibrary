package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.http.conn.DnsResolver;

public class SystemDefaultDnsResolver implements DnsResolver {
   public static final SystemDefaultDnsResolver INSTANCE = new SystemDefaultDnsResolver();

   public InetAddress[] resolve(String var1) throws UnknownHostException {
      return InetAddress.getAllByName(var1);
   }
}
