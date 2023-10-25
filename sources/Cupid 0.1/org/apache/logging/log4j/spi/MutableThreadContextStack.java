package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class MutableThreadContextStack implements ThreadContextStack, StringBuilderFormattable {
  private static final long serialVersionUID = 50505011L;
  
  private final List<String> list;
  
  private boolean frozen;
  
  public MutableThreadContextStack() {
    this(new ArrayList<>());
  }
  
  public MutableThreadContextStack(List<String> list) {
    this.list = new ArrayList<>(list);
  }
  
  private MutableThreadContextStack(MutableThreadContextStack stack) {
    this.list = new ArrayList<>(stack.list);
  }
  
  private void checkInvariants() {
    if (this.frozen)
      throw new UnsupportedOperationException("context stack has been frozen"); 
  }
  
  public String pop() {
    checkInvariants();
    if (this.list.isEmpty())
      return null; 
    int last = this.list.size() - 1;
    String result = this.list.remove(last);
    return result;
  }
  
  public String peek() {
    if (this.list.isEmpty())
      return null; 
    int last = this.list.size() - 1;
    return this.list.get(last);
  }
  
  public void push(String message) {
    checkInvariants();
    this.list.add(message);
  }
  
  public int getDepth() {
    return this.list.size();
  }
  
  public List<String> asList() {
    return this.list;
  }
  
  public void trim(int depth) {
    checkInvariants();
    if (depth < 0)
      throw new IllegalArgumentException("Maximum stack depth cannot be negative"); 
    if (this.list == null)
      return; 
    List<String> copy = new ArrayList<>(this.list.size());
    int count = Math.min(depth, this.list.size());
    for (int i = 0; i < count; i++)
      copy.add(this.list.get(i)); 
    this.list.clear();
    this.list.addAll(copy);
  }
  
  public ThreadContextStack copy() {
    return new MutableThreadContextStack(this);
  }
  
  public void clear() {
    checkInvariants();
    this.list.clear();
  }
  
  public int size() {
    return this.list.size();
  }
  
  public boolean isEmpty() {
    return this.list.isEmpty();
  }
  
  public boolean contains(Object o) {
    return this.list.contains(o);
  }
  
  public Iterator<String> iterator() {
    return this.list.iterator();
  }
  
  public Object[] toArray() {
    return this.list.toArray();
  }
  
  public <T> T[] toArray(T[] ts) {
    return this.list.toArray(ts);
  }
  
  public boolean add(String s) {
    checkInvariants();
    return this.list.add(s);
  }
  
  public boolean remove(Object o) {
    checkInvariants();
    return this.list.remove(o);
  }
  
  public boolean containsAll(Collection<?> objects) {
    return this.list.containsAll(objects);
  }
  
  public boolean addAll(Collection<? extends String> strings) {
    checkInvariants();
    return this.list.addAll(strings);
  }
  
  public boolean removeAll(Collection<?> objects) {
    checkInvariants();
    return this.list.removeAll(objects);
  }
  
  public boolean retainAll(Collection<?> objects) {
    checkInvariants();
    return this.list.retainAll(objects);
  }
  
  public String toString() {
    return String.valueOf(this.list);
  }
  
  public void formatTo(StringBuilder buffer) {
    buffer.append('[');
    for (int i = 0; i < this.list.size(); i++) {
      if (i > 0)
        buffer.append(',').append(' '); 
      buffer.append(this.list.get(i));
    } 
    buffer.append(']');
  }
  
  public int hashCode() {
    return 31 + Objects.hashCode(this.list);
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (!(obj instanceof ThreadContextStack))
      return false; 
    ThreadContextStack other = (ThreadContextStack)obj;
    List<String> otherAsList = other.asList();
    return Objects.equals(this.list, otherAsList);
  }
  
  public ThreadContext.ContextStack getImmutableStackOrNull() {
    return copy();
  }
  
  public void freeze() {
    this.frozen = true;
  }
  
  public boolean isFrozen() {
    return this.frozen;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\MutableThreadContextStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */