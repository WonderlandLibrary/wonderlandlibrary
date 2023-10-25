package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;

@Deprecated
public interface HostNameResolver {
  InetAddress resolve(String paramString) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\scheme\HostNameResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */