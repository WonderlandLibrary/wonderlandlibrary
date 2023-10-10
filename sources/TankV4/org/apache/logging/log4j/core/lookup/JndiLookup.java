package org.apache.logging.log4j.core.lookup;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "jndi",
   category = "Lookup"
)
public class JndiLookup implements StrLookup {
   static final String CONTAINER_JNDI_RESOURCE_PATH_PREFIX = "java:comp/env/";

   public String lookup(String var1) {
      return this.lookup((LogEvent)null, var1);
   }

   public String lookup(LogEvent var1, String var2) {
      if (var2 == null) {
         return null;
      } else {
         try {
            InitialContext var3 = new InitialContext();
            return (String)var3.lookup(this.convertJndiName(var2));
         } catch (NamingException var4) {
            return null;
         }
      }
   }

   private String convertJndiName(String var1) {
      if (!var1.startsWith("java:comp/env/") && var1.indexOf(58) == -1) {
         var1 = "java:comp/env/" + var1;
      }

      return var1;
   }
}
