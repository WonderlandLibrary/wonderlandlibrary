package io.netty.util;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Recycler<T> {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(Recycler.class);
  
  private static final AtomicInteger ID_GENERATOR = new AtomicInteger(-2147483648);
  
  private static final int OWN_THREAD_ID = ID_GENERATOR.getAndIncrement();
  
  private static final int DEFAULT_MAX_CAPACITY;
  
  static {
    int maxCapacity = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity.default", 0);
    if (maxCapacity <= 0)
      maxCapacity = 262144; 
    DEFAULT_MAX_CAPACITY = maxCapacity;
    if (logger.isDebugEnabled())
      logger.debug("-Dio.netty.recycler.maxCapacity.default: {}", Integer.valueOf(DEFAULT_MAX_CAPACITY)); 
  }
  
  private static final int INITIAL_CAPACITY = Math.min(DEFAULT_MAX_CAPACITY, 256);
  
  private final int maxCapacity;
  
  private final FastThreadLocal<Stack<T>> threadLocal = new FastThreadLocal<Stack<T>>() {
      protected Recycler.Stack<T> initialValue() {
        return new Recycler.Stack<T>(Recycler.this, Thread.currentThread(), Recycler.this.maxCapacity);
      }
    };
  
  protected Recycler() {
    this(DEFAULT_MAX_CAPACITY);
  }
  
  protected Recycler(int maxCapacity) {
    this.maxCapacity = Math.max(0, maxCapacity);
  }
  
  public final T get() {
    Stack<T> stack = (Stack<T>)this.threadLocal.get();
    DefaultHandle handle = stack.pop();
    if (handle == null) {
      handle = stack.newHandle();
      handle.value = newObject(handle);
    } 
    return (T)handle.value;
  }
  
  public final boolean recycle(T o, Handle handle) {
    DefaultHandle h = (DefaultHandle)handle;
    if (h.stack.parent != this)
      return false; 
    if (o != h.value)
      throw new IllegalArgumentException("o does not belong to handle"); 
    h.recycle();
    return true;
  }
  
  static final class DefaultHandle implements Handle {
    private int lastRecycledId;
    
    private int recycleId;
    
    private Recycler.Stack<?> stack;
    
    private Object value;
    
    DefaultHandle(Recycler.Stack<?> stack) {
      this.stack = stack;
    }
    
    public void recycle() {
      Thread thread = Thread.currentThread();
      if (thread == this.stack.thread) {
        this.stack.push(this);
        return;
      } 
      Map<Recycler.Stack<?>, Recycler.WeakOrderQueue> delayedRecycled = (Map<Recycler.Stack<?>, Recycler.WeakOrderQueue>)Recycler.DELAYED_RECYCLED.get();
      Recycler.WeakOrderQueue queue = delayedRecycled.get(this.stack);
      if (queue == null)
        delayedRecycled.put(this.stack, queue = new Recycler.WeakOrderQueue(this.stack, thread)); 
      queue.add(this);
    }
  }
  
  private static final FastThreadLocal<Map<Stack<?>, WeakOrderQueue>> DELAYED_RECYCLED = new FastThreadLocal<Map<Stack<?>, WeakOrderQueue>>() {
      protected Map<Recycler.Stack<?>, Recycler.WeakOrderQueue> initialValue() {
        return new WeakHashMap<Recycler.Stack<?>, Recycler.WeakOrderQueue>();
      }
    };
  
  protected abstract T newObject(Handle paramHandle);
  
  private static final class WeakOrderQueue {
    private static final int LINK_CAPACITY = 16;
    
    private Link head;
    
    private Link tail;
    
    private WeakOrderQueue next;
    
    private final WeakReference<Thread> owner;
    
    private static final class Link extends AtomicInteger {
      private Link() {}
      
      private final Recycler.DefaultHandle[] elements = new Recycler.DefaultHandle[16];
      
      private int readIndex;
      
      private Link next;
    }
    
    private final int id = Recycler.ID_GENERATOR.getAndIncrement();
    
    WeakOrderQueue(Recycler.Stack<?> stack, Thread thread) {
      this.head = this.tail = new Link();
      this.owner = new WeakReference<Thread>(thread);
      synchronized (stack) {
        this.next = stack.head;
        stack.head = this;
      } 
    }
    
    void add(Recycler.DefaultHandle handle) {
      handle.lastRecycledId = this.id;
      Link tail = this.tail;
      int writeIndex;
      if ((writeIndex = tail.get()) == 16) {
        this.tail = tail = tail.next = new Link();
        writeIndex = tail.get();
      } 
      tail.elements[writeIndex] = handle;
      handle.stack = null;
      tail.lazySet(writeIndex + 1);
    }
    
    boolean hasFinalData() {
      return (this.tail.readIndex != this.tail.get());
    }
    
    boolean transfer(Recycler.Stack<?> to) {
      Link head = this.head;
      if (head == null)
        return false; 
      if (head.readIndex == 16) {
        if (head.next == null)
          return false; 
        this.head = head = head.next;
      } 
      int start = head.readIndex;
      int end = head.get();
      if (start == end)
        return false; 
      int count = end - start;
      if (to.size + count > to.elements.length)
        to.elements = Arrays.<Recycler.DefaultHandle>copyOf(to.elements, (to.size + count) * 2); 
      Recycler.DefaultHandle[] src = head.elements;
      Recycler.DefaultHandle[] trg = to.elements;
      int size = to.size;
      while (start < end) {
        Recycler.DefaultHandle element = src[start];
        if (element.recycleId == 0) {
          element.recycleId = element.lastRecycledId;
        } else if (element.recycleId != element.lastRecycledId) {
          throw new IllegalStateException("recycled already");
        } 
        element.stack = to;
        trg[size++] = element;
        src[start++] = null;
      } 
      to.size = size;
      if (end == 16 && head.next != null)
        this.head = head.next; 
      head.readIndex = end;
      return true;
    }
  }
  
  static final class Stack<T> {
    final Recycler<T> parent;
    
    final Thread thread;
    
    private Recycler.DefaultHandle[] elements;
    
    private final int maxCapacity;
    
    private int size;
    
    private volatile Recycler.WeakOrderQueue head;
    
    private Recycler.WeakOrderQueue cursor;
    
    private Recycler.WeakOrderQueue prev;
    
    Stack(Recycler<T> parent, Thread thread, int maxCapacity) {
      this.parent = parent;
      this.thread = thread;
      this.maxCapacity = maxCapacity;
      this.elements = new Recycler.DefaultHandle[Recycler.INITIAL_CAPACITY];
    }
    
    Recycler.DefaultHandle pop() {
      int size = this.size;
      if (size == 0) {
        if (!scavenge())
          return null; 
        size = this.size;
      } 
      size--;
      Recycler.DefaultHandle ret = this.elements[size];
      if (ret.lastRecycledId != ret.recycleId)
        throw new IllegalStateException("recycled multiple times"); 
      ret.recycleId = 0;
      ret.lastRecycledId = 0;
      this.size = size;
      return ret;
    }
    
    boolean scavenge() {
      if (scavengeSome())
        return true; 
      this.prev = null;
      this.cursor = this.head;
      return false;
    }
    
    boolean scavengeSome() {
      boolean success = false;
      Recycler.WeakOrderQueue cursor = this.cursor, prev = this.prev;
      while (cursor != null) {
        if (cursor.transfer(this)) {
          success = true;
          break;
        } 
        Recycler.WeakOrderQueue next = cursor.next;
        if (cursor.owner.get() == null) {
          if (cursor.hasFinalData())
            do {
            
            } while (cursor.transfer(this)); 
          if (prev != null)
            prev.next = next; 
        } else {
          prev = cursor;
        } 
        cursor = next;
      } 
      this.prev = prev;
      this.cursor = cursor;
      return success;
    }
    
    void push(Recycler.DefaultHandle item) {
      if ((item.recycleId | item.lastRecycledId) != 0)
        throw new IllegalStateException("recycled already"); 
      item.recycleId = item.lastRecycledId = Recycler.OWN_THREAD_ID;
      int size = this.size;
      if (size == this.elements.length) {
        if (size == this.maxCapacity)
          return; 
        this.elements = Arrays.<Recycler.DefaultHandle>copyOf(this.elements, size << 1);
      } 
      this.elements[size] = item;
      this.size = size + 1;
    }
    
    Recycler.DefaultHandle newHandle() {
      return new Recycler.DefaultHandle(this);
    }
  }
  
  public static interface Handle {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\Recycler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */