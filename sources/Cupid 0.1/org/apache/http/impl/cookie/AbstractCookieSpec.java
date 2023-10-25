package org.apache.http.impl.cookie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.util.Args;

@NotThreadSafe
public abstract class AbstractCookieSpec implements CookieSpec {
  private final Map<String, CookieAttributeHandler> attribHandlerMap = new HashMap<String, CookieAttributeHandler>(10);
  
  public void registerAttribHandler(String name, CookieAttributeHandler handler) {
    Args.notNull(name, "Attribute name");
    Args.notNull(handler, "Attribute handler");
    this.attribHandlerMap.put(name, handler);
  }
  
  protected CookieAttributeHandler findAttribHandler(String name) {
    return this.attribHandlerMap.get(name);
  }
  
  protected CookieAttributeHandler getAttribHandler(String name) {
    CookieAttributeHandler handler = findAttribHandler(name);
    if (handler == null)
      throw new IllegalStateException("Handler not registered for " + name + " attribute."); 
    return handler;
  }
  
  protected Collection<CookieAttributeHandler> getAttribHandlers() {
    return this.attribHandlerMap.values();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\AbstractCookieSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */