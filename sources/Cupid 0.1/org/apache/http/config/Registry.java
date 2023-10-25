package org.apache.http.config;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class Registry<I> implements Lookup<I> {
  private final Map<String, I> map;
  
  Registry(Map<String, I> map) {
    this.map = new ConcurrentHashMap<String, I>(map);
  }
  
  public I lookup(String key) {
    if (key == null)
      return null; 
    return this.map.get(key.toLowerCase(Locale.US));
  }
  
  public String toString() {
    return this.map.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\config\Registry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */