package org.apache.logging.log4j;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.spi.CleanableThreadContextMap;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;
import org.apache.logging.log4j.spi.NoOpThreadContextMap;
import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap2;
import org.apache.logging.log4j.spi.ThreadContextMapFactory;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class ThreadContext {
  private static class EmptyThreadContextStack extends AbstractCollection<String> implements ThreadContextStack {
    private static final long serialVersionUID = 1L;
    
    private EmptyThreadContextStack() {}
    
    private static final Iterator<String> EMPTY_ITERATOR = new ThreadContext.EmptyIterator<>();
    
    public String pop() {
      return null;
    }
    
    public String peek() {
      return null;
    }
    
    public void push(String message) {
      throw new UnsupportedOperationException();
    }
    
    public int getDepth() {
      return 0;
    }
    
    public List<String> asList() {
      return Collections.emptyList();
    }
    
    public void trim(int depth) {}
    
    public boolean equals(Object o) {
      return (o instanceof Collection && ((Collection)o).isEmpty());
    }
    
    public int hashCode() {
      return 1;
    }
    
    public ThreadContext.ContextStack copy() {
      return (ThreadContext.ContextStack)this;
    }
    
    public <T> T[] toArray(T[] a) {
      throw new UnsupportedOperationException();
    }
    
    public boolean add(String e) {
      throw new UnsupportedOperationException();
    }
    
    public boolean containsAll(Collection<?> c) {
      return false;
    }
    
    public boolean addAll(Collection<? extends String> c) {
      throw new UnsupportedOperationException();
    }
    
    public boolean removeAll(Collection<?> c) {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> c) {
      throw new UnsupportedOperationException();
    }
    
    public Iterator<String> iterator() {
      return EMPTY_ITERATOR;
    }
    
    public int size() {
      return 0;
    }
    
    public ThreadContext.ContextStack getImmutableStackOrNull() {
      return (ThreadContext.ContextStack)this;
    }
  }
  
  private static class EmptyIterator<E> implements Iterator<E> {
    private EmptyIterator() {}
    
    public boolean hasNext() {
      return false;
    }
    
    public E next() {
      throw new NoSuchElementException("This is an empty iterator!");
    }
    
    public void remove() {}
  }
  
  public static final Map<String, String> EMPTY_MAP = Collections.emptyMap();
  
  public static final ThreadContextStack EMPTY_STACK = new EmptyThreadContextStack();
  
  private static final String DISABLE_MAP = "disableThreadContextMap";
  
  private static final String DISABLE_STACK = "disableThreadContextStack";
  
  private static final String DISABLE_ALL = "disableThreadContext";
  
  private static boolean useStack;
  
  private static ThreadContextMap contextMap;
  
  private static ThreadContextStack contextStack;
  
  private static ReadOnlyThreadContextMap readOnlyContextMap;
  
  static {
    init();
  }
  
  static void init() {
    ThreadContextMapFactory.init();
    contextMap = null;
    PropertiesUtil managerProps = PropertiesUtil.getProperties();
    boolean disableAll = managerProps.getBooleanProperty("disableThreadContext");
    useStack = (!managerProps.getBooleanProperty("disableThreadContextStack") && !disableAll);
    boolean useMap = (!managerProps.getBooleanProperty("disableThreadContextMap") && !disableAll);
    contextStack = (ThreadContextStack)new DefaultThreadContextStack(useStack);
    if (!useMap) {
      contextMap = (ThreadContextMap)new NoOpThreadContextMap();
    } else {
      contextMap = ThreadContextMapFactory.createThreadContextMap();
    } 
    if (contextMap instanceof ReadOnlyThreadContextMap) {
      readOnlyContextMap = (ReadOnlyThreadContextMap)contextMap;
    } else {
      readOnlyContextMap = null;
    } 
  }
  
  public static void put(String key, String value) {
    contextMap.put(key, value);
  }
  
  public static void putIfNull(String key, String value) {
    if (!contextMap.containsKey(key))
      contextMap.put(key, value); 
  }
  
  public static void putAll(Map<String, String> m) {
    if (contextMap instanceof ThreadContextMap2) {
      ((ThreadContextMap2)contextMap).putAll(m);
    } else if (contextMap instanceof DefaultThreadContextMap) {
      ((DefaultThreadContextMap)contextMap).putAll(m);
    } else {
      for (Map.Entry<String, String> entry : m.entrySet())
        contextMap.put(entry.getKey(), entry.getValue()); 
    } 
  }
  
  public static String get(String key) {
    return contextMap.get(key);
  }
  
  public static void remove(String key) {
    contextMap.remove(key);
  }
  
  public static void removeAll(Iterable<String> keys) {
    if (contextMap instanceof CleanableThreadContextMap) {
      ((CleanableThreadContextMap)contextMap).removeAll(keys);
    } else if (contextMap instanceof DefaultThreadContextMap) {
      ((DefaultThreadContextMap)contextMap).removeAll(keys);
    } else {
      for (String key : keys)
        contextMap.remove(key); 
    } 
  }
  
  public static void clearMap() {
    contextMap.clear();
  }
  
  public static void clearAll() {
    clearMap();
    clearStack();
  }
  
  public static boolean containsKey(String key) {
    return contextMap.containsKey(key);
  }
  
  public static Map<String, String> getContext() {
    return contextMap.getCopy();
  }
  
  public static Map<String, String> getImmutableContext() {
    Map<String, String> map = contextMap.getImmutableMapOrNull();
    return (map == null) ? EMPTY_MAP : map;
  }
  
  public static ReadOnlyThreadContextMap getThreadContextMap() {
    return readOnlyContextMap;
  }
  
  public static boolean isEmpty() {
    return contextMap.isEmpty();
  }
  
  public static void clearStack() {
    contextStack.clear();
  }
  
  public static ContextStack cloneStack() {
    return contextStack.copy();
  }
  
  public static ContextStack getImmutableStack() {
    ContextStack result = contextStack.getImmutableStackOrNull();
    return (result == null) ? (ContextStack)EMPTY_STACK : result;
  }
  
  public static void setStack(Collection<String> stack) {
    if (stack.isEmpty() || !useStack)
      return; 
    contextStack.clear();
    contextStack.addAll(stack);
  }
  
  public static int getDepth() {
    return contextStack.getDepth();
  }
  
  public static String pop() {
    return contextStack.pop();
  }
  
  public static String peek() {
    return contextStack.peek();
  }
  
  public static void push(String message) {
    contextStack.push(message);
  }
  
  public static void push(String message, Object... args) {
    contextStack.push(ParameterizedMessage.format(message, args));
  }
  
  public static void removeStack() {
    contextStack.clear();
  }
  
  public static void trim(int depth) {
    contextStack.trim(depth);
  }
  
  public static interface ContextStack extends Serializable, Collection<String> {
    String pop();
    
    String peek();
    
    void push(String param1String);
    
    int getDepth();
    
    List<String> asList();
    
    void trim(int param1Int);
    
    ContextStack copy();
    
    ContextStack getImmutableStackOrNull();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\ThreadContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */