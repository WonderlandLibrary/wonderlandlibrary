package io.netty.util.internal;

import io.netty.util.Recycler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class RecyclableArrayList extends ArrayList<Object> {
  private static final long serialVersionUID = -8605125654176467947L;
  
  private static final int DEFAULT_INITIAL_CAPACITY = 8;
  
  private static final Recycler<RecyclableArrayList> RECYCLER = new Recycler<RecyclableArrayList>() {
      protected RecyclableArrayList newObject(Recycler.Handle handle) {
        return new RecyclableArrayList(handle);
      }
    };
  
  private final Recycler.Handle handle;
  
  public static RecyclableArrayList newInstance() {
    return newInstance(8);
  }
  
  public static RecyclableArrayList newInstance(int minCapacity) {
    RecyclableArrayList ret = (RecyclableArrayList)RECYCLER.get();
    ret.ensureCapacity(minCapacity);
    return ret;
  }
  
  private RecyclableArrayList(Recycler.Handle handle) {
    this(handle, 8);
  }
  
  private RecyclableArrayList(Recycler.Handle handle, int initialCapacity) {
    super(initialCapacity);
    this.handle = handle;
  }
  
  public boolean addAll(Collection<?> c) {
    checkNullElements(c);
    return super.addAll(c);
  }
  
  public boolean addAll(int index, Collection<?> c) {
    checkNullElements(c);
    return super.addAll(index, c);
  }
  
  private static void checkNullElements(Collection<?> c) {
    if (c instanceof java.util.RandomAccess && c instanceof List) {
      List<?> list = (List)c;
      int size = list.size();
      for (int i = 0; i < size; i++) {
        if (list.get(i) == null)
          throw new IllegalArgumentException("c contains null values"); 
      } 
    } else {
      for (Object element : c) {
        if (element == null)
          throw new IllegalArgumentException("c contains null values"); 
      } 
    } 
  }
  
  public boolean add(Object element) {
    if (element == null)
      throw new NullPointerException("element"); 
    return super.add(element);
  }
  
  public void add(int index, Object element) {
    if (element == null)
      throw new NullPointerException("element"); 
    super.add(index, element);
  }
  
  public Object set(int index, Object element) {
    if (element == null)
      throw new NullPointerException("element"); 
    return super.set(index, element);
  }
  
  public boolean recycle() {
    clear();
    return RECYCLER.recycle(this, this.handle);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\RecyclableArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */