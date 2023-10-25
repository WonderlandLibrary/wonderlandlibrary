package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.status.StatusLogger;

public class JndiContextSelector implements NamedContextSelector {
  private static final LoggerContext CONTEXT = new LoggerContext("Default");
  
  private static final ConcurrentMap<String, LoggerContext> CONTEXT_MAP = new ConcurrentHashMap<>();
  
  private static final StatusLogger LOGGER = StatusLogger.getLogger();
  
  public JndiContextSelector() {
    if (!JndiManager.isJndiContextSelectorEnabled())
      throw new IllegalStateException("JNDI must be enabled by setting log4j2.enableJndiContextSelector=true"); 
  }
  
  public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
    LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
    if (ctx == null) {
      String loggingContextName = getContextName();
      if (loggingContextName != null)
        ctx = CONTEXT_MAP.get(loggingContextName); 
    } 
    if (ctx != null)
      ctx.stop(50L, TimeUnit.MILLISECONDS); 
  }
  
  public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
    LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
    if (ctx == null) {
      String loggingContextName = getContextName();
      if (loggingContextName == null)
        return false; 
      ctx = CONTEXT_MAP.get(loggingContextName);
    } 
    return (ctx != null && ctx.isStarted());
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
    return getContext(fqcn, loader, currentContext, null);
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
    LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
    if (lc != null)
      return lc; 
    String loggingContextName = null;
    try (JndiManager jndiManager = JndiManager.getDefaultManager()) {
      loggingContextName = (String)jndiManager.lookup("java:comp/env/log4j/context-name");
    } catch (NamingException ne) {
      LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
    } 
    return (loggingContextName == null) ? CONTEXT : locateContext(loggingContextName, null, configLocation);
  }
  
  private String getContextName() {
    String loggingContextName = null;
    try (JndiManager jndiManager = JndiManager.getDefaultManager()) {
      loggingContextName = (String)jndiManager.lookup("java:comp/env/log4j/context-name");
    } catch (NamingException ne) {
      LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
    } 
    return loggingContextName;
  }
  
  public LoggerContext locateContext(String name, Object externalContext, URI configLocation) {
    if (name == null) {
      LOGGER.error("A context name is required to locate a LoggerContext");
      return null;
    } 
    if (!CONTEXT_MAP.containsKey(name)) {
      LoggerContext ctx = new LoggerContext(name, externalContext, configLocation);
      CONTEXT_MAP.putIfAbsent(name, ctx);
    } 
    return CONTEXT_MAP.get(name);
  }
  
  public void removeContext(LoggerContext context) {
    for (Map.Entry<String, LoggerContext> entry : CONTEXT_MAP.entrySet()) {
      if (((LoggerContext)entry.getValue()).equals(context))
        CONTEXT_MAP.remove(entry.getKey()); 
    } 
  }
  
  public boolean isClassLoaderDependent() {
    return false;
  }
  
  public LoggerContext removeContext(String name) {
    return CONTEXT_MAP.remove(name);
  }
  
  public List<LoggerContext> getLoggerContexts() {
    return Collections.unmodifiableList(new ArrayList<>(CONTEXT_MAP.values()));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\selector\JndiContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */