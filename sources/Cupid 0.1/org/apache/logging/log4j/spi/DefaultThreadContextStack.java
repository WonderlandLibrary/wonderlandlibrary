package org.apache.logging.log4j.spi;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.Strings;

public class DefaultThreadContextStack implements ThreadContextStack, StringBuilderFormattable {
  private static final long serialVersionUID = 5050501L;
  
  private static final ThreadLocal<MutableThreadContextStack> STACK = new ThreadLocal<>();
  
  private final boolean useStack;
  
  public DefaultThreadContextStack(boolean useStack) {
    this.useStack = useStack;
  }
  
  private MutableThreadContextStack getNonNullStackCopy() {
    MutableThreadContextStack values = STACK.get();
    return (values == null) ? new MutableThreadContextStack() : (MutableThreadContextStack)values.copy();
  }
  
  public boolean add(String s) {
    if (!this.useStack)
      return false; 
    MutableThreadContextStack copy = getNonNullStackCopy();
    copy.add(s);
    copy.freeze();
    STACK.set(copy);
    return true;
  }
  
  public boolean addAll(Collection<? extends String> strings) {
    if (!this.useStack || strings.isEmpty())
      return false; 
    MutableThreadContextStack copy = getNonNullStackCopy();
    copy.addAll(strings);
    copy.freeze();
    STACK.set(copy);
    return true;
  }
  
  public List<String> asList() {
    MutableThreadContextStack values = STACK.get();
    if (values == null)
      return Collections.emptyList(); 
    return values.asList();
  }
  
  public void clear() {
    STACK.remove();
  }
  
  public boolean contains(Object o) {
    MutableThreadContextStack values = STACK.get();
    return (values != null && values.contains(o));
  }
  
  public boolean containsAll(Collection<?> objects) {
    if (objects.isEmpty())
      return true; 
    MutableThreadContextStack values = STACK.get();
    return (values != null && values.containsAll(objects));
  }
  
  public ThreadContextStack copy() {
    MutableThreadContextStack values = null;
    if (!this.useStack || (values = STACK.get()) == null)
      return new MutableThreadContextStack(); 
    return values.copy();
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (obj instanceof DefaultThreadContextStack) {
      DefaultThreadContextStack defaultThreadContextStack = (DefaultThreadContextStack)obj;
      if (this.useStack != defaultThreadContextStack.useStack)
        return false; 
    } 
    if (!(obj instanceof ThreadContextStack))
      return false; 
    ThreadContextStack other = (ThreadContextStack)obj;
    MutableThreadContextStack values = STACK.get();
    if (values == null)
      return false; 
    return values.equals(other);
  }
  
  public int getDepth() {
    MutableThreadContextStack values = STACK.get();
    return (values == null) ? 0 : values.getDepth();
  }
  
  public int hashCode() {
    MutableThreadContextStack values = STACK.get();
    int prime = 31;
    int result = 1;
    result = 31 * result + ((values == null) ? 0 : values.hashCode());
    return result;
  }
  
  public boolean isEmpty() {
    MutableThreadContextStack values = STACK.get();
    return (values == null || values.isEmpty());
  }
  
  public Iterator<String> iterator() {
    MutableThreadContextStack values = STACK.get();
    if (values == null) {
      List<String> empty = Collections.emptyList();
      return empty.iterator();
    } 
    return values.iterator();
  }
  
  public String peek() {
    MutableThreadContextStack values = STACK.get();
    if (values == null || values.isEmpty())
      return ""; 
    return values.peek();
  }
  
  public String pop() {
    if (!this.useStack)
      return ""; 
    MutableThreadContextStack values = STACK.get();
    if (values == null || values.isEmpty())
      return ""; 
    MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
    String result = copy.pop();
    copy.freeze();
    STACK.set(copy);
    return result;
  }
  
  public void push(String message) {
    if (!this.useStack)
      return; 
    add(message);
  }
  
  public boolean remove(Object o) {
    if (!this.useStack)
      return false; 
    MutableThreadContextStack values = STACK.get();
    if (values == null || values.isEmpty())
      return false; 
    MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
    boolean result = copy.remove(o);
    copy.freeze();
    STACK.set(copy);
    return result;
  }
  
  public boolean removeAll(Collection<?> objects) {
    if (!this.useStack || objects.isEmpty())
      return false; 
    MutableThreadContextStack values = STACK.get();
    if (values == null || values.isEmpty())
      return false; 
    MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
    boolean result = copy.removeAll(objects);
    copy.freeze();
    STACK.set(copy);
    return result;
  }
  
  public boolean retainAll(Collection<?> objects) {
    if (!this.useStack || objects.isEmpty())
      return false; 
    MutableThreadContextStack values = STACK.get();
    if (values == null || values.isEmpty())
      return false; 
    MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
    boolean result = copy.retainAll(objects);
    copy.freeze();
    STACK.set(copy);
    return result;
  }
  
  public int size() {
    MutableThreadContextStack values = STACK.get();
    return (values == null) ? 0 : values.size();
  }
  
  public Object[] toArray() {
    MutableThreadContextStack result = STACK.get();
    if (result == null)
      return (Object[])Strings.EMPTY_ARRAY; 
    return result.toArray(new Object[result.size()]);
  }
  
  public <T> T[] toArray(T[] ts) {
    MutableThreadContextStack result = STACK.get();
    if (result == null) {
      if (ts.length > 0)
        ts[0] = null; 
      return ts;
    } 
    return result.toArray(ts);
  }
  
  public String toString() {
    MutableThreadContextStack values = STACK.get();
    return (values == null) ? "[]" : values.toString();
  }
  
  public void formatTo(StringBuilder buffer) {
    MutableThreadContextStack values = STACK.get();
    if (values == null) {
      buffer.append("[]");
    } else {
      StringBuilders.appendValue(buffer, values);
    } 
  }
  
  public void trim(int depth) {
    if (depth < 0)
      throw new IllegalArgumentException("Maximum stack depth cannot be negative"); 
    MutableThreadContextStack values = STACK.get();
    if (values == null)
      return; 
    MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
    copy.trim(depth);
    copy.freeze();
    STACK.set(copy);
  }
  
  public ThreadContext.ContextStack getImmutableStackOrNull() {
    return STACK.get();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\DefaultThreadContextStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */