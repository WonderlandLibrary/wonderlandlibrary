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
   private final Map attribHandlerMap = new HashMap(10);

   public void registerAttribHandler(String var1, CookieAttributeHandler var2) {
      Args.notNull(var1, "Attribute name");
      Args.notNull(var2, "Attribute handler");
      this.attribHandlerMap.put(var1, var2);
   }

   protected CookieAttributeHandler findAttribHandler(String var1) {
      return (CookieAttributeHandler)this.attribHandlerMap.get(var1);
   }

   protected CookieAttributeHandler getAttribHandler(String var1) {
      CookieAttributeHandler var2 = this.findAttribHandler(var1);
      if (var2 == null) {
         throw new IllegalStateException("Handler not registered for " + var1 + " attribute.");
      } else {
         return var2;
      }
   }

   protected Collection getAttribHandlers() {
      return this.attribHandlerMap.values();
   }
}
