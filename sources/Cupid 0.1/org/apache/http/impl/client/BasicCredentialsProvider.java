package org.apache.http.impl.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

@ThreadSafe
public class BasicCredentialsProvider implements CredentialsProvider {
  private final ConcurrentHashMap<AuthScope, Credentials> credMap = new ConcurrentHashMap<AuthScope, Credentials>();
  
  public void setCredentials(AuthScope authscope, Credentials credentials) {
    Args.notNull(authscope, "Authentication scope");
    this.credMap.put(authscope, credentials);
  }
  
  private static Credentials matchCredentials(Map<AuthScope, Credentials> map, AuthScope authscope) {
    Credentials creds = map.get(authscope);
    if (creds == null) {
      int bestMatchFactor = -1;
      AuthScope bestMatch = null;
      for (AuthScope current : map.keySet()) {
        int factor = authscope.match(current);
        if (factor > bestMatchFactor) {
          bestMatchFactor = factor;
          bestMatch = current;
        } 
      } 
      if (bestMatch != null)
        creds = map.get(bestMatch); 
    } 
    return creds;
  }
  
  public Credentials getCredentials(AuthScope authscope) {
    Args.notNull(authscope, "Authentication scope");
    return matchCredentials(this.credMap, authscope);
  }
  
  public void clear() {
    this.credMap.clear();
  }
  
  public String toString() {
    return this.credMap.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\BasicCredentialsProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */