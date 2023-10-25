package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HttpContext;

@Immutable
public class NoConnectionReuseStrategy implements ConnectionReuseStrategy {
  public static final NoConnectionReuseStrategy INSTANCE = new NoConnectionReuseStrategy();
  
  public boolean keepAlive(HttpResponse response, HttpContext context) {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\NoConnectionReuseStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */