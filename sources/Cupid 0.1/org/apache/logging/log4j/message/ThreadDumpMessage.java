package org.apache.logging.log4j.message;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@AsynchronouslyFormattable
public class ThreadDumpMessage implements Message, StringBuilderFormattable {
  private static final long serialVersionUID = -1103400781608841088L;
  
  private static ThreadInfoFactory FACTORY;
  
  private volatile Map<ThreadInformation, StackTraceElement[]> threads;
  
  private final String title;
  
  private String formattedMessage;
  
  public ThreadDumpMessage(String title) {
    this.title = (title == null) ? "" : title;
    this.threads = getFactory().createThreadInfo();
  }
  
  private ThreadDumpMessage(String formattedMsg, String title) {
    this.formattedMessage = formattedMsg;
    this.title = (title == null) ? "" : title;
  }
  
  private static ThreadInfoFactory getFactory() {
    if (FACTORY == null)
      FACTORY = initFactory(ThreadDumpMessage.class.getClassLoader()); 
    return FACTORY;
  }
  
  private static ThreadInfoFactory initFactory(ClassLoader classLoader) {
    ServiceLoader<ThreadInfoFactory> serviceLoader = ServiceLoader.load(ThreadInfoFactory.class, classLoader);
    ThreadInfoFactory result = null;
    try {
      Iterator<ThreadInfoFactory> iterator = serviceLoader.iterator();
      while (result == null && iterator.hasNext())
        result = iterator.next(); 
    } catch (ServiceConfigurationError|LinkageError|Exception unavailable) {
      StatusLogger.getLogger().info("ThreadDumpMessage uses BasicThreadInfoFactory: could not load extended ThreadInfoFactory: {}", unavailable
          .toString());
      result = null;
    } 
    return (result == null) ? new BasicThreadInfoFactory() : result;
  }
  
  public String toString() {
    return getFormattedMessage();
  }
  
  public String getFormattedMessage() {
    if (this.formattedMessage != null)
      return this.formattedMessage; 
    StringBuilder sb = new StringBuilder(255);
    formatTo(sb);
    return sb.toString();
  }
  
  public void formatTo(StringBuilder sb) {
    sb.append(this.title);
    if (this.title.length() > 0)
      sb.append('\n'); 
    for (Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet()) {
      ThreadInformation info = entry.getKey();
      info.printThreadInfo(sb);
      info.printStack(sb, entry.getValue());
      sb.append('\n');
    } 
  }
  
  public String getFormat() {
    return (this.title == null) ? "" : this.title;
  }
  
  public Object[] getParameters() {
    return null;
  }
  
  protected Object writeReplace() {
    return new ThreadDumpMessageProxy(this);
  }
  
  private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    throw new InvalidObjectException("Proxy required");
  }
  
  private static class ThreadDumpMessageProxy implements Serializable {
    private static final long serialVersionUID = -3476620450287648269L;
    
    private final String formattedMsg;
    
    private final String title;
    
    ThreadDumpMessageProxy(ThreadDumpMessage msg) {
      this.formattedMsg = msg.getFormattedMessage();
      this.title = msg.title;
    }
    
    protected Object readResolve() {
      return new ThreadDumpMessage(this.formattedMsg, this.title);
    }
  }
  
  public static interface ThreadInfoFactory {
    Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
  }
  
  private static class BasicThreadInfoFactory implements ThreadInfoFactory {
    private BasicThreadInfoFactory() {}
    
    public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
      Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
      Map<ThreadInformation, StackTraceElement[]> threads = (Map)new HashMap<>(map.size());
      for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet())
        threads.put(new BasicThreadInformation(entry.getKey()), entry.getValue()); 
      return threads;
    }
  }
  
  public Throwable getThrowable() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ThreadDumpMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */