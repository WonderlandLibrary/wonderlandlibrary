package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;

public class BasicContextSelector implements ContextSelector {
   private static final LoggerContext CONTEXT = new LoggerContext("Default");

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3) {
      LoggerContext var4 = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
      return var4 != null ? var4 : CONTEXT;
   }

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4) {
      LoggerContext var5 = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
      return var5 != null ? var5 : CONTEXT;
   }

   public LoggerContext locateContext(String var1, String var2) {
      return CONTEXT;
   }

   public void removeContext(LoggerContext var1) {
   }

   public List getLoggerContexts() {
      ArrayList var1 = new ArrayList();
      var1.add(CONTEXT);
      return Collections.unmodifiableList(var1);
   }
}
