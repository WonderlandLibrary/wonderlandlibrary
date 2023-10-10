package org.apache.logging.log4j.core.async;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.selector.ContextSelector;

public class AsyncLoggerContextSelector implements ContextSelector {
   private static final AsyncLoggerContext CONTEXT = new AsyncLoggerContext("AsyncLoggerContext");

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3) {
      return CONTEXT;
   }

   public List getLoggerContexts() {
      ArrayList var1 = new ArrayList();
      var1.add(CONTEXT);
      return Collections.unmodifiableList(var1);
   }

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4) {
      return CONTEXT;
   }

   public void removeContext(LoggerContext var1) {
   }
}
