package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.status.StatusLogger;

public class JNDIContextSelector implements NamedContextSelector {
   private static final LoggerContext CONTEXT = new LoggerContext("Default");
   private static final ConcurrentMap CONTEXT_MAP = new ConcurrentHashMap();
   private static final StatusLogger LOGGER = StatusLogger.getLogger();

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3) {
      return this.getContext(var1, var2, var3, (URI)null);
   }

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4) {
      LoggerContext var5 = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
      if (var5 != null) {
         return var5;
      } else {
         String var6 = null;

         try {
            InitialContext var7 = new InitialContext();
            var6 = (String)lookup(var7, "java:comp/env/log4j/context-name");
         } catch (NamingException var8) {
            LOGGER.error("Unable to lookup java:comp/env/log4j/context-name", var8);
         }

         return var6 == null ? CONTEXT : this.locateContext(var6, (Object)null, var4);
      }
   }

   public LoggerContext locateContext(String var1, Object var2, URI var3) {
      if (var1 == null) {
         LOGGER.error("A context name is required to locate a LoggerContext");
         return null;
      } else {
         if (!CONTEXT_MAP.containsKey(var1)) {
            LoggerContext var4 = new LoggerContext(var1, var2, var3);
            CONTEXT_MAP.putIfAbsent(var1, var4);
         }

         return (LoggerContext)CONTEXT_MAP.get(var1);
      }
   }

   public void removeContext(LoggerContext var1) {
      Iterator var2 = CONTEXT_MAP.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (((LoggerContext)var3.getValue()).equals(var1)) {
            CONTEXT_MAP.remove(var3.getKey());
         }
      }

   }

   public LoggerContext removeContext(String var1) {
      return (LoggerContext)CONTEXT_MAP.remove(var1);
   }

   public List getLoggerContexts() {
      ArrayList var1 = new ArrayList(CONTEXT_MAP.values());
      return Collections.unmodifiableList(var1);
   }

   protected static Object lookup(Context var0, String var1) throws NamingException {
      if (var0 == null) {
         return null;
      } else {
         try {
            return var0.lookup(var1);
         } catch (NameNotFoundException var3) {
            LOGGER.error("Could not find name [" + var1 + "].");
            throw var3;
         }
      }
   }
}
