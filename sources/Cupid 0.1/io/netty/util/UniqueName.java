package io.netty.util;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class UniqueName implements Comparable<UniqueName> {
  private static final AtomicInteger nextId = new AtomicInteger();
  
  private final int id;
  
  private final String name;
  
  public UniqueName(ConcurrentMap<String, Boolean> map, String name, Object... args) {
    if (map == null)
      throw new NullPointerException("map"); 
    if (name == null)
      throw new NullPointerException("name"); 
    if (args != null && args.length > 0)
      validateArgs(args); 
    if (map.putIfAbsent(name, Boolean.TRUE) != null)
      throw new IllegalArgumentException(String.format("'%s' is already in use", new Object[] { name })); 
    this.id = nextId.incrementAndGet();
    this.name = name;
  }
  
  protected void validateArgs(Object... args) {}
  
  public final String name() {
    return this.name;
  }
  
  public final int id() {
    return this.id;
  }
  
  public final int hashCode() {
    return super.hashCode();
  }
  
  public final boolean equals(Object o) {
    return super.equals(o);
  }
  
  public int compareTo(UniqueName other) {
    if (this == other)
      return 0; 
    int returnCode = this.name.compareTo(other.name);
    if (returnCode != 0)
      return returnCode; 
    return Integer.valueOf(this.id).compareTo(Integer.valueOf(other.id));
  }
  
  public String toString() {
    return name();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\UniqueName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */