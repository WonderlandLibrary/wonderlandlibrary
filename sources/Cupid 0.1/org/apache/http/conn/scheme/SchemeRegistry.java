package org.apache.http.conn.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@Deprecated
@ThreadSafe
public final class SchemeRegistry {
  private final ConcurrentHashMap<String, Scheme> registeredSchemes = new ConcurrentHashMap<String, Scheme>();
  
  public final Scheme getScheme(String name) {
    Scheme found = get(name);
    if (found == null)
      throw new IllegalStateException("Scheme '" + name + "' not registered."); 
    return found;
  }
  
  public final Scheme getScheme(HttpHost host) {
    Args.notNull(host, "Host");
    return getScheme(host.getSchemeName());
  }
  
  public final Scheme get(String name) {
    Args.notNull(name, "Scheme name");
    Scheme found = this.registeredSchemes.get(name);
    return found;
  }
  
  public final Scheme register(Scheme sch) {
    Args.notNull(sch, "Scheme");
    Scheme old = this.registeredSchemes.put(sch.getName(), sch);
    return old;
  }
  
  public final Scheme unregister(String name) {
    Args.notNull(name, "Scheme name");
    Scheme gone = this.registeredSchemes.remove(name);
    return gone;
  }
  
  public final List<String> getSchemeNames() {
    return new ArrayList<String>(this.registeredSchemes.keySet());
  }
  
  public void setItems(Map<String, Scheme> map) {
    if (map == null)
      return; 
    this.registeredSchemes.clear();
    this.registeredSchemes.putAll(map);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\scheme\SchemeRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */