package io.netty.util.internal.chmv8;

import io.netty.util.internal.ThreadLocalRandom;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import sun.misc.Unsafe;

public class ForkJoinPool extends AbstractExecutorService {
  private static void checkPermission() {
    SecurityManager security = System.getSecurityManager();
    if (security != null)
      security.checkPermission(modifyThreadPermission); 
  }
  
  static final class DefaultForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {
    public final ForkJoinWorkerThread newThread(ForkJoinPool pool) {
      return new ForkJoinWorkerThread(pool);
    }
  }
  
  static final class EmptyTask extends ForkJoinTask<Void> {
    private static final long serialVersionUID = -7721805057305804111L;
    
    public final Void getRawResult() {
      return null;
    }
    
    public final void setRawResult(Void x) {}
    
    public final boolean exec() {
      return true;
    }
  }
  
  static final class WorkQueue {
    static final int INITIAL_QUEUE_CAPACITY = 8192;
    
    static final int MAXIMUM_QUEUE_CAPACITY = 67108864;
    
    volatile long pad00;
    
    volatile long pad01;
    
    volatile long pad02;
    
    volatile long pad03;
    
    volatile long pad04;
    
    volatile long pad05;
    
    volatile long pad06;
    
    volatile int eventCount;
    
    int nextWait;
    
    int nsteals;
    
    int hint;
    
    short poolIndex;
    
    final short mode;
    
    volatile int qlock;
    
    volatile int base;
    
    int top;
    
    ForkJoinTask<?>[] array;
    
    final ForkJoinPool pool;
    
    final ForkJoinWorkerThread owner;
    
    volatile Thread parker;
    
    volatile ForkJoinTask<?> currentJoin;
    
    ForkJoinTask<?> currentSteal;
    
    volatile Object pad10;
    
    volatile Object pad11;
    
    volatile Object pad12;
    
    volatile Object pad13;
    
    volatile Object pad14;
    
    volatile Object pad15;
    
    volatile Object pad16;
    
    volatile Object pad17;
    
    volatile Object pad18;
    
    volatile Object pad19;
    
    volatile Object pad1a;
    
    volatile Object pad1b;
    
    volatile Object pad1c;
    
    volatile Object pad1d;
    
    private static final Unsafe U;
    
    private static final long QBASE;
    
    private static final long QLOCK;
    
    private static final int ABASE;
    
    private static final int ASHIFT;
    
    WorkQueue(ForkJoinPool pool, ForkJoinWorkerThread owner, int mode, int seed) {
      this.pool = pool;
      this.owner = owner;
      this.mode = (short)mode;
      this.hint = seed;
      this.base = this.top = 4096;
    }
    
    final int queueSize() {
      int n = this.base - this.top;
      return (n >= 0) ? 0 : -n;
    }
    
    final boolean isEmpty() {
      int s, n = this.base - (s = this.top);
      ForkJoinTask<?>[] a;
      int m;
      return (n >= 0 || (n == -1 && ((a = this.array) == null || (m = a.length - 1) < 0 || U.getObject(a, ((m & s - 1) << ASHIFT) + ABASE) == null)));
    }
    
    final void push(ForkJoinTask<?> task) {
      int s = this.top;
      ForkJoinTask<?>[] a;
      if ((a = this.array) != null) {
        int m = a.length - 1;
        U.putOrderedObject(a, (((m & s) << ASHIFT) + ABASE), task);
        int n;
        if ((n = (this.top = s + 1) - this.base) <= 2) {
          ForkJoinPool p;
          (p = this.pool).signalWork(p.workQueues, this);
        } else if (n >= m) {
          growArray();
        } 
      } 
    }
    
    final ForkJoinTask<?>[] growArray() {
      ForkJoinTask<?>[] oldA = this.array;
      int size = (oldA != null) ? (oldA.length << 1) : 8192;
      if (size > 67108864)
        throw new RejectedExecutionException("Queue capacity exceeded"); 
      ForkJoinTask<?>[] a = this.array = (ForkJoinTask<?>[])new ForkJoinTask[size];
      int oldMask, t, b;
      if (oldA != null && (oldMask = oldA.length - 1) >= 0 && (t = this.top) - (b = this.base) > 0) {
        int mask = size - 1;
        do {
          int oldj = ((b & oldMask) << ASHIFT) + ABASE;
          int j = ((b & mask) << ASHIFT) + ABASE;
          ForkJoinTask<?> x = (ForkJoinTask)U.getObjectVolatile(oldA, oldj);
          if (x == null || !U.compareAndSwapObject(oldA, oldj, x, null))
            continue; 
          U.putObjectVolatile(a, j, x);
        } while (++b != t);
      } 
      return a;
    }
    
    final ForkJoinTask<?> pop() {
      ForkJoinTask<?>[] a;
      int m;
      if ((a = this.array) != null && (m = a.length - 1) >= 0) {
        long j = (((m & s) << ASHIFT) + ABASE);
        ForkJoinTask<?> t;
        int s;
        while ((s = this.top - 1) - this.base >= 0 && (t = (ForkJoinTask)U.getObject(a, j)) != null) {
          if (U.compareAndSwapObject(a, j, t, null)) {
            this.top = s;
            return t;
          } 
        } 
      } 
      return null;
    }
    
    final ForkJoinTask<?> pollAt(int b) {
      int j = ((a.length - 1 & b) << ASHIFT) + ABASE;
      ForkJoinTask<?> t, a[];
      if ((a = this.array) != null && (t = (ForkJoinTask)U.getObjectVolatile(a, j)) != null && this.base == b && U.compareAndSwapObject(a, j, t, null)) {
        U.putOrderedInt(this, QBASE, b + 1);
        return t;
      } 
      return null;
    }
    
    final ForkJoinTask<?> poll() {
      ForkJoinTask<?>[] a;
      int b;
      while ((b = this.base) - this.top < 0 && (a = this.array) != null) {
        int j = ((a.length - 1 & b) << ASHIFT) + ABASE;
        ForkJoinTask<?> t = (ForkJoinTask)U.getObjectVolatile(a, j);
        if (t != null) {
          if (U.compareAndSwapObject(a, j, t, null)) {
            U.putOrderedInt(this, QBASE, b + 1);
            return t;
          } 
          continue;
        } 
        if (this.base == b) {
          if (b + 1 == this.top)
            break; 
          Thread.yield();
        } 
      } 
      return null;
    }
    
    final ForkJoinTask<?> nextLocalTask() {
      return (this.mode == 0) ? pop() : poll();
    }
    
    final ForkJoinTask<?> peek() {
      ForkJoinTask<?>[] a = this.array;
      int m;
      if (a == null || (m = a.length - 1) < 0)
        return null; 
      int i = (this.mode == 0) ? (this.top - 1) : this.base;
      int j = ((i & m) << ASHIFT) + ABASE;
      return (ForkJoinTask)U.getObjectVolatile(a, j);
    }
    
    final boolean tryUnpush(ForkJoinTask<?> t) {
      ForkJoinTask<?>[] a;
      int s;
      if ((a = this.array) != null && (s = this.top) != this.base && U.compareAndSwapObject(a, (((a.length - 1 & --s) << ASHIFT) + ABASE), t, null)) {
        this.top = s;
        return true;
      } 
      return false;
    }
    
    final void cancelAll() {
      ForkJoinTask.cancelIgnoringExceptions(this.currentJoin);
      ForkJoinTask.cancelIgnoringExceptions(this.currentSteal);
      ForkJoinTask<?> t;
      while ((t = poll()) != null)
        ForkJoinTask.cancelIgnoringExceptions(t); 
    }
    
    final void pollAndExecAll() {
      ForkJoinTask<?> t;
      while ((t = poll()) != null)
        t.doExec(); 
    }
    
    final void runTask(ForkJoinTask<?> task) {
      if ((this.currentSteal = task) != null) {
        task.doExec();
        ForkJoinTask<?>[] a = this.array;
        int md = this.mode;
        this.nsteals++;
        this.currentSteal = null;
        if (md != 0) {
          pollAndExecAll();
        } else if (a != null) {
          int m = a.length - 1;
          int s;
          while ((s = this.top - 1) - this.base >= 0) {
            long i = (((m & s) << ASHIFT) + ABASE);
            ForkJoinTask<?> t = (ForkJoinTask)U.getObject(a, i);
            if (t == null)
              break; 
            if (U.compareAndSwapObject(a, i, t, null)) {
              this.top = s;
              t.doExec();
            } 
          } 
        } 
      } 
    }
    
    final boolean tryRemoveAndExec(ForkJoinTask<?> task) {
      boolean stat;
      ForkJoinTask<?>[] a;
      int m;
      int s;
      int b;
      int n;
      if (task != null && (a = this.array) != null && (m = a.length - 1) >= 0 && (n = (s = this.top) - (b = this.base)) > 0) {
        boolean removed = false, empty = true;
        stat = true;
        while (true) {
          long j = (((--s & m) << ASHIFT) + ABASE);
          ForkJoinTask<?> t = (ForkJoinTask)U.getObject(a, j);
          if (t == null)
            break; 
          if (t == task) {
            if (s + 1 == this.top) {
              if (!U.compareAndSwapObject(a, j, task, null))
                break; 
              this.top = s;
              removed = true;
              break;
            } 
            if (this.base == b)
              removed = U.compareAndSwapObject(a, j, task, new ForkJoinPool.EmptyTask()); 
            break;
          } 
          if (t.status >= 0) {
            empty = false;
          } else if (s + 1 == this.top) {
            if (U.compareAndSwapObject(a, j, t, null))
              this.top = s; 
            break;
          } 
          if (--n == 0) {
            if (!empty && this.base == b)
              stat = false; 
            break;
          } 
        } 
        if (removed)
          task.doExec(); 
      } else {
        stat = false;
      } 
      return stat;
    }
    
    final boolean pollAndExecCC(CountedCompleter<?> root) {
      ForkJoinTask<?>[] a;
      int b;
      if ((b = this.base) - this.top < 0 && (a = this.array) != null) {
        long j = (((a.length - 1 & b) << ASHIFT) + ABASE);
        Object o;
        if ((o = U.getObjectVolatile(a, j)) == null)
          return true; 
        if (o instanceof CountedCompleter) {
          CountedCompleter<?> t = (CountedCompleter)o, r = t;
          do {
            if (r == root) {
              if (this.base == b && U.compareAndSwapObject(a, j, t, null)) {
                U.putOrderedInt(this, QBASE, b + 1);
                t.doExec();
              } 
              return true;
            } 
          } while ((r = r.completer) != null);
        } 
      } 
      return false;
    }
    
    final boolean externalPopAndExecCC(CountedCompleter<?> root) {
      long j = (((a.length - 1 & s - 1) << ASHIFT) + ABASE);
      ForkJoinTask<?>[] a;
      int s;
      Object o;
      if (this.base - (s = this.top) < 0 && (a = this.array) != null && o = U.getObject(a, j) instanceof CountedCompleter) {
        CountedCompleter<?> t = (CountedCompleter)o, r = t;
        do {
          if (r == root) {
            if (U.compareAndSwapInt(this, QLOCK, 0, 1))
              if (this.top == s && this.array == a && U.compareAndSwapObject(a, j, t, null)) {
                this.top = s - 1;
                this.qlock = 0;
                t.doExec();
              } else {
                this.qlock = 0;
              }  
            return true;
          } 
        } while ((r = r.completer) != null);
      } 
      return false;
    }
    
    final boolean internalPopAndExecCC(CountedCompleter<?> root) {
      long j = (((a.length - 1 & s - 1) << ASHIFT) + ABASE);
      ForkJoinTask<?>[] a;
      int s;
      Object o;
      if (this.base - (s = this.top) < 0 && (a = this.array) != null && o = U.getObject(a, j) instanceof CountedCompleter) {
        CountedCompleter<?> t = (CountedCompleter)o, r = t;
        do {
          if (r == root) {
            if (U.compareAndSwapObject(a, j, t, null)) {
              this.top = s - 1;
              t.doExec();
            } 
            return true;
          } 
        } while ((r = r.completer) != null);
      } 
      return false;
    }
    
    final boolean isApparentlyUnblocked() {
      Thread wt;
      Thread.State s;
      return (this.eventCount >= 0 && (wt = this.owner) != null && (s = wt.getState()) != Thread.State.BLOCKED && s != Thread.State.WAITING && s != Thread.State.TIMED_WAITING);
    }
    
    static {
      try {
        U = ForkJoinPool.getUnsafe();
        Class<?> k = WorkQueue.class;
        Class<?> ak = ForkJoinTask[].class;
        QBASE = U.objectFieldOffset(k.getDeclaredField("base"));
        QLOCK = U.objectFieldOffset(k.getDeclaredField("qlock"));
        ABASE = U.arrayBaseOffset(ak);
        int scale = U.arrayIndexScale(ak);
        if ((scale & scale - 1) != 0)
          throw new Error("data type scale not a power of two"); 
        ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
      } catch (Exception e) {
        throw new Error(e);
      } 
    }
  }
  
  private static final synchronized int nextPoolId() {
    return ++poolNumberSequence;
  }
  
  private int acquirePlock() {
    int spins = 256;
    while (true) {
      int ps;
      int nps;
      if (((ps = this.plock) & 0x2) == 0 && U.compareAndSwapInt(this, PLOCK, ps, nps = ps + 2))
        return nps; 
      if (spins >= 0) {
        if (ThreadLocalRandom.current().nextInt() >= 0)
          spins--; 
        continue;
      } 
      if (U.compareAndSwapInt(this, PLOCK, ps, ps | 0x1))
        synchronized (this) {
          if ((this.plock & 0x1) != 0) {
            try {
              wait();
            } catch (InterruptedException ie) {
              try {
                Thread.currentThread().interrupt();
              } catch (SecurityException ignore) {}
            } 
          } else {
            notifyAll();
          } 
        }  
    } 
  }
  
  private void releasePlock(int ps) {
    this.plock = ps;
    synchronized (this) {
      notifyAll();
    } 
  }
  
  private void tryAddWorker() {
    long c;
    int u;
    int e;
    while ((u = (int)((c = this.ctl) >>> 32L)) < 0 && (u & 0x8000) != 0 && (e = (int)c) >= 0) {
      long nc = (u + 1 & 0xFFFF | u + 65536 & 0xFFFF0000) << 32L | e;
      if (U.compareAndSwapLong(this, CTL, c, nc)) {
        Throwable ex = null;
        ForkJoinWorkerThread wt = null;
        try {
          ForkJoinWorkerThreadFactory fac;
          if ((fac = this.factory) != null && (wt = fac.newThread(this)) != null) {
            wt.start();
            break;
          } 
        } catch (Throwable rex) {
          ex = rex;
        } 
        deregisterWorker(wt, ex);
        break;
      } 
    } 
  }
  
  final WorkQueue registerWorker(ForkJoinWorkerThread wt) {
    // Byte code:
    //   0: aload_1
    //   1: iconst_1
    //   2: invokevirtual setDaemon : (Z)V
    //   5: aload_0
    //   6: getfield ueh : Ljava/lang/Thread$UncaughtExceptionHandler;
    //   9: dup
    //   10: astore_2
    //   11: ifnull -> 19
    //   14: aload_1
    //   15: aload_2
    //   16: invokevirtual setUncaughtExceptionHandler : (Ljava/lang/Thread$UncaughtExceptionHandler;)V
    //   19: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   22: aload_0
    //   23: getstatic io/netty/util/internal/chmv8/ForkJoinPool.INDEXSEED : J
    //   26: aload_0
    //   27: getfield indexSeed : I
    //   30: dup
    //   31: istore #4
    //   33: iload #4
    //   35: ldc 1640531527
    //   37: iadd
    //   38: istore #4
    //   40: iload #4
    //   42: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   45: ifeq -> 19
    //   48: iload #4
    //   50: ifeq -> 19
    //   53: new io/netty/util/internal/chmv8/ForkJoinPool$WorkQueue
    //   56: dup
    //   57: aload_0
    //   58: aload_1
    //   59: aload_0
    //   60: getfield mode : S
    //   63: iload #4
    //   65: invokespecial <init> : (Lio/netty/util/internal/chmv8/ForkJoinPool;Lio/netty/util/internal/chmv8/ForkJoinWorkerThread;II)V
    //   68: astore #6
    //   70: aload_0
    //   71: getfield plock : I
    //   74: dup
    //   75: istore #5
    //   77: iconst_2
    //   78: iand
    //   79: ifne -> 102
    //   82: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   85: aload_0
    //   86: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   89: iload #5
    //   91: iinc #5, 2
    //   94: iload #5
    //   96: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   99: ifne -> 108
    //   102: aload_0
    //   103: invokespecial acquirePlock : ()I
    //   106: istore #5
    //   108: iload #5
    //   110: ldc -2147483648
    //   112: iand
    //   113: iload #5
    //   115: iconst_2
    //   116: iadd
    //   117: ldc 2147483647
    //   119: iand
    //   120: ior
    //   121: istore #7
    //   123: aload_0
    //   124: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   127: dup
    //   128: astore_3
    //   129: ifnull -> 266
    //   132: aload_3
    //   133: arraylength
    //   134: istore #8
    //   136: iload #8
    //   138: iconst_1
    //   139: isub
    //   140: istore #9
    //   142: iload #4
    //   144: iconst_1
    //   145: ishl
    //   146: iconst_1
    //   147: ior
    //   148: istore #10
    //   150: aload_3
    //   151: iload #10
    //   153: iload #9
    //   155: iand
    //   156: dup
    //   157: istore #10
    //   159: aaload
    //   160: ifnull -> 245
    //   163: iconst_0
    //   164: istore #11
    //   166: iload #8
    //   168: iconst_4
    //   169: if_icmpgt -> 176
    //   172: iconst_2
    //   173: goto -> 185
    //   176: iload #8
    //   178: iconst_1
    //   179: iushr
    //   180: ldc 65534
    //   182: iand
    //   183: iconst_2
    //   184: iadd
    //   185: istore #12
    //   187: aload_3
    //   188: iload #10
    //   190: iload #12
    //   192: iadd
    //   193: iload #9
    //   195: iand
    //   196: dup
    //   197: istore #10
    //   199: aaload
    //   200: ifnull -> 245
    //   203: iinc #11, 1
    //   206: iload #11
    //   208: iload #8
    //   210: if_icmplt -> 187
    //   213: aload_0
    //   214: aload_3
    //   215: iload #8
    //   217: iconst_1
    //   218: ishl
    //   219: dup
    //   220: istore #8
    //   222: invokestatic copyOf : ([Ljava/lang/Object;I)[Ljava/lang/Object;
    //   225: checkcast [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   228: dup
    //   229: astore_3
    //   230: putfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   233: iload #8
    //   235: iconst_1
    //   236: isub
    //   237: istore #9
    //   239: iconst_0
    //   240: istore #11
    //   242: goto -> 187
    //   245: aload #6
    //   247: iload #10
    //   249: i2s
    //   250: putfield poolIndex : S
    //   253: aload #6
    //   255: iload #10
    //   257: putfield eventCount : I
    //   260: aload_3
    //   261: iload #10
    //   263: aload #6
    //   265: aastore
    //   266: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   269: aload_0
    //   270: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   273: iload #5
    //   275: iload #7
    //   277: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   280: ifne -> 320
    //   283: aload_0
    //   284: iload #7
    //   286: invokespecial releasePlock : (I)V
    //   289: goto -> 320
    //   292: astore #13
    //   294: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   297: aload_0
    //   298: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   301: iload #5
    //   303: iload #7
    //   305: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   308: ifne -> 317
    //   311: aload_0
    //   312: iload #7
    //   314: invokespecial releasePlock : (I)V
    //   317: aload #13
    //   319: athrow
    //   320: aload_1
    //   321: aload_0
    //   322: getfield workerNamePrefix : Ljava/lang/String;
    //   325: aload #6
    //   327: getfield poolIndex : S
    //   330: iconst_1
    //   331: iushr
    //   332: invokestatic toString : (I)Ljava/lang/String;
    //   335: invokevirtual concat : (Ljava/lang/String;)Ljava/lang/String;
    //   338: invokevirtual setName : (Ljava/lang/String;)V
    //   341: aload #6
    //   343: areturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #1355	-> 0
    //   #1356	-> 5
    //   #1357	-> 14
    //   #1359	-> 19
    //   #1361	-> 53
    //   #1362	-> 70
    //   #1364	-> 102
    //   #1365	-> 108
    //   #1367	-> 123
    //   #1368	-> 132
    //   #1369	-> 142
    //   #1370	-> 150
    //   #1371	-> 163
    //   #1372	-> 166
    //   #1373	-> 187
    //   #1374	-> 203
    //   #1375	-> 213
    //   #1376	-> 233
    //   #1377	-> 239
    //   #1381	-> 245
    //   #1382	-> 253
    //   #1383	-> 260
    //   #1386	-> 266
    //   #1387	-> 283
    //   #1386	-> 292
    //   #1387	-> 311
    //   #1389	-> 320
    //   #1390	-> 341
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   166	79	11	probes	I
    //   187	58	12	step	I
    //   136	130	8	n	I
    //   142	124	9	m	I
    //   150	116	10	r	I
    //   0	344	0	this	Lio/netty/util/internal/chmv8/ForkJoinPool;
    //   0	344	1	wt	Lio/netty/util/internal/chmv8/ForkJoinWorkerThread;
    //   11	333	2	handler	Ljava/lang/Thread$UncaughtExceptionHandler;
    //   129	215	3	ws	[Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   33	311	4	s	I
    //   77	267	5	ps	I
    //   70	274	6	w	Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   123	221	7	nps	I
    // Exception table:
    //   from	to	target	type
    //   123	266	292	finally
    //   292	294	292	finally
  }
  
  final void deregisterWorker(ForkJoinWorkerThread wt, Throwable ex) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_1
    //   3: ifnull -> 200
    //   6: aload_1
    //   7: getfield workQueue : Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   10: dup
    //   11: astore_3
    //   12: ifnull -> 200
    //   15: aload_3
    //   16: iconst_m1
    //   17: putfield qlock : I
    //   20: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   23: aload_0
    //   24: getstatic io/netty/util/internal/chmv8/ForkJoinPool.STEALCOUNT : J
    //   27: aload_0
    //   28: getfield stealCount : J
    //   31: dup2
    //   32: lstore #5
    //   34: lload #5
    //   36: aload_3
    //   37: getfield nsteals : I
    //   40: i2l
    //   41: ladd
    //   42: invokevirtual compareAndSwapLong : (Ljava/lang/Object;JJJ)Z
    //   45: ifeq -> 20
    //   48: aload_0
    //   49: getfield plock : I
    //   52: dup
    //   53: istore #4
    //   55: iconst_2
    //   56: iand
    //   57: ifne -> 80
    //   60: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   63: aload_0
    //   64: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   67: iload #4
    //   69: iinc #4, 2
    //   72: iload #4
    //   74: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   77: ifne -> 86
    //   80: aload_0
    //   81: invokespecial acquirePlock : ()I
    //   84: istore #4
    //   86: iload #4
    //   88: ldc -2147483648
    //   90: iand
    //   91: iload #4
    //   93: iconst_2
    //   94: iadd
    //   95: ldc 2147483647
    //   97: iand
    //   98: ior
    //   99: istore #7
    //   101: aload_3
    //   102: getfield poolIndex : S
    //   105: istore #8
    //   107: aload_0
    //   108: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   111: astore #9
    //   113: aload #9
    //   115: ifnull -> 146
    //   118: iload #8
    //   120: iflt -> 146
    //   123: iload #8
    //   125: aload #9
    //   127: arraylength
    //   128: if_icmpge -> 146
    //   131: aload #9
    //   133: iload #8
    //   135: aaload
    //   136: aload_3
    //   137: if_acmpne -> 146
    //   140: aload #9
    //   142: iload #8
    //   144: aconst_null
    //   145: aastore
    //   146: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   149: aload_0
    //   150: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   153: iload #4
    //   155: iload #7
    //   157: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   160: ifne -> 200
    //   163: aload_0
    //   164: iload #7
    //   166: invokespecial releasePlock : (I)V
    //   169: goto -> 200
    //   172: astore #10
    //   174: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   177: aload_0
    //   178: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   181: iload #4
    //   183: iload #7
    //   185: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   188: ifne -> 197
    //   191: aload_0
    //   192: iload #7
    //   194: invokespecial releasePlock : (I)V
    //   197: aload #10
    //   199: athrow
    //   200: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   203: aload_0
    //   204: getstatic io/netty/util/internal/chmv8/ForkJoinPool.CTL : J
    //   207: aload_0
    //   208: getfield ctl : J
    //   211: dup2
    //   212: lstore #4
    //   214: lload #4
    //   216: ldc2_w 281474976710656
    //   219: lsub
    //   220: ldc2_w -281474976710656
    //   223: land
    //   224: lload #4
    //   226: ldc2_w 4294967296
    //   229: lsub
    //   230: ldc2_w 281470681743360
    //   233: land
    //   234: lor
    //   235: lload #4
    //   237: ldc2_w 4294967295
    //   240: land
    //   241: lor
    //   242: invokevirtual compareAndSwapLong : (Ljava/lang/Object;JJJ)Z
    //   245: ifeq -> 200
    //   248: aload_0
    //   249: iconst_0
    //   250: iconst_0
    //   251: invokespecial tryTerminate : (ZZ)Z
    //   254: ifne -> 446
    //   257: aload_3
    //   258: ifnull -> 446
    //   261: aload_3
    //   262: getfield array : [Lio/netty/util/internal/chmv8/ForkJoinTask;
    //   265: ifnull -> 446
    //   268: aload_3
    //   269: invokevirtual cancelAll : ()V
    //   272: aload_0
    //   273: getfield ctl : J
    //   276: dup2
    //   277: lstore #4
    //   279: bipush #32
    //   281: lushr
    //   282: l2i
    //   283: dup
    //   284: istore #9
    //   286: ifge -> 446
    //   289: lload #4
    //   291: l2i
    //   292: dup
    //   293: istore #11
    //   295: iflt -> 446
    //   298: iload #11
    //   300: ifle -> 433
    //   303: aload_0
    //   304: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   307: dup
    //   308: astore #6
    //   310: ifnull -> 446
    //   313: iload #11
    //   315: ldc 65535
    //   317: iand
    //   318: dup
    //   319: istore #10
    //   321: aload #6
    //   323: arraylength
    //   324: if_icmpge -> 446
    //   327: aload #6
    //   329: iload #10
    //   331: aaload
    //   332: dup
    //   333: astore #7
    //   335: ifnonnull -> 341
    //   338: goto -> 446
    //   341: aload #7
    //   343: getfield nextWait : I
    //   346: ldc 2147483647
    //   348: iand
    //   349: i2l
    //   350: iload #9
    //   352: ldc 65536
    //   354: iadd
    //   355: i2l
    //   356: bipush #32
    //   358: lshl
    //   359: lor
    //   360: lstore #12
    //   362: aload #7
    //   364: getfield eventCount : I
    //   367: iload #11
    //   369: ldc -2147483648
    //   371: ior
    //   372: if_icmpeq -> 378
    //   375: goto -> 446
    //   378: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   381: aload_0
    //   382: getstatic io/netty/util/internal/chmv8/ForkJoinPool.CTL : J
    //   385: lload #4
    //   387: lload #12
    //   389: invokevirtual compareAndSwapLong : (Ljava/lang/Object;JJJ)Z
    //   392: ifeq -> 430
    //   395: aload #7
    //   397: iload #11
    //   399: ldc 65536
    //   401: iadd
    //   402: ldc 2147483647
    //   404: iand
    //   405: putfield eventCount : I
    //   408: aload #7
    //   410: getfield parker : Ljava/lang/Thread;
    //   413: dup
    //   414: astore #8
    //   416: ifnull -> 446
    //   419: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   422: aload #8
    //   424: invokevirtual unpark : (Ljava/lang/Object;)V
    //   427: goto -> 446
    //   430: goto -> 272
    //   433: iload #9
    //   435: i2s
    //   436: ifge -> 446
    //   439: aload_0
    //   440: invokespecial tryAddWorker : ()V
    //   443: goto -> 446
    //   446: aload_2
    //   447: ifnonnull -> 456
    //   450: invokestatic helpExpungeStaleExceptions : ()V
    //   453: goto -> 460
    //   456: aload_2
    //   457: invokestatic rethrow : (Ljava/lang/Throwable;)V
    //   460: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #1403	-> 0
    //   #1404	-> 2
    //   #1406	-> 15
    //   #1407	-> 20
    //   #1410	-> 48
    //   #1412	-> 80
    //   #1413	-> 86
    //   #1415	-> 101
    //   #1416	-> 107
    //   #1417	-> 113
    //   #1418	-> 140
    //   #1420	-> 146
    //   #1421	-> 163
    //   #1420	-> 172
    //   #1421	-> 191
    //   #1426	-> 200
    //   #1431	-> 248
    //   #1432	-> 268
    //   #1434	-> 272
    //   #1435	-> 298
    //   #1436	-> 303
    //   #1439	-> 338
    //   #1440	-> 341
    //   #1442	-> 362
    //   #1443	-> 375
    //   #1444	-> 378
    //   #1445	-> 395
    //   #1446	-> 408
    //   #1447	-> 419
    //   #1450	-> 430
    //   #1452	-> 433
    //   #1453	-> 439
    //   #1458	-> 446
    //   #1459	-> 450
    //   #1461	-> 456
    //   #1462	-> 460
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   107	39	8	idx	I
    //   113	33	9	ws	[Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   55	145	4	ps	I
    //   34	166	5	sc	J
    //   101	99	7	nps	I
    //   416	14	8	p	Ljava/lang/Thread;
    //   362	68	12	nc	J
    //   310	123	6	ws	[Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   335	98	7	v	Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   321	112	10	i	I
    //   286	160	9	u	I
    //   295	151	11	e	I
    //   0	461	0	this	Lio/netty/util/internal/chmv8/ForkJoinPool;
    //   0	461	1	wt	Lio/netty/util/internal/chmv8/ForkJoinWorkerThread;
    //   0	461	2	ex	Ljava/lang/Throwable;
    //   2	459	3	w	Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   214	247	4	c	J
    // Exception table:
    //   from	to	target	type
    //   101	146	172	finally
    //   172	174	172	finally
  }
  
  static final class Submitter {
    int seed;
    
    Submitter(int s) {
      this.seed = s;
    }
  }
  
  final void externalPush(ForkJoinTask<?> task) {
    Submitter z = submitters.get();
    int ps = this.plock;
    WorkQueue[] ws = this.workQueues;
    WorkQueue q;
    int r, m;
    if (z != null && ps > 0 && ws != null && (m = ws.length - 1) >= 0 && (q = ws[m & (r = z.seed) & 0x7E]) != null && r != 0 && U.compareAndSwapInt(q, QLOCK, 0, 1)) {
      int s;
      int n;
      int am;
      ForkJoinTask<?>[] a;
      if ((a = q.array) != null && (am = a.length - 1) > (n = (s = q.top) - q.base)) {
        int j = ((am & s) << ASHIFT) + ABASE;
        U.putOrderedObject(a, j, task);
        q.top = s + 1;
        q.qlock = 0;
        if (n <= 1)
          signalWork(ws, q); 
        return;
      } 
      q.qlock = 0;
    } 
    fullExternalPush(task);
  }
  
  private void fullExternalPush(ForkJoinTask<?> task) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: getstatic io/netty/util/internal/chmv8/ForkJoinPool.submitters : Ljava/lang/ThreadLocal;
    //   5: invokevirtual get : ()Ljava/lang/Object;
    //   8: checkcast io/netty/util/internal/chmv8/ForkJoinPool$Submitter
    //   11: astore_3
    //   12: aload_3
    //   13: ifnonnull -> 64
    //   16: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   19: aload_0
    //   20: getstatic io/netty/util/internal/chmv8/ForkJoinPool.INDEXSEED : J
    //   23: aload_0
    //   24: getfield indexSeed : I
    //   27: dup
    //   28: istore_2
    //   29: iload_2
    //   30: ldc 1640531527
    //   32: iadd
    //   33: istore_2
    //   34: iload_2
    //   35: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   38: ifeq -> 98
    //   41: iload_2
    //   42: ifeq -> 98
    //   45: getstatic io/netty/util/internal/chmv8/ForkJoinPool.submitters : Ljava/lang/ThreadLocal;
    //   48: new io/netty/util/internal/chmv8/ForkJoinPool$Submitter
    //   51: dup
    //   52: iload_2
    //   53: invokespecial <init> : (I)V
    //   56: dup
    //   57: astore_3
    //   58: invokevirtual set : (Ljava/lang/Object;)V
    //   61: goto -> 98
    //   64: iload_2
    //   65: ifne -> 98
    //   68: aload_3
    //   69: getfield seed : I
    //   72: istore_2
    //   73: iload_2
    //   74: iload_2
    //   75: bipush #13
    //   77: ishl
    //   78: ixor
    //   79: istore_2
    //   80: iload_2
    //   81: iload_2
    //   82: bipush #17
    //   84: iushr
    //   85: ixor
    //   86: istore_2
    //   87: aload_3
    //   88: iload_2
    //   89: iload_2
    //   90: iconst_5
    //   91: ishl
    //   92: ixor
    //   93: dup
    //   94: istore_2
    //   95: putfield seed : I
    //   98: aload_0
    //   99: getfield plock : I
    //   102: dup
    //   103: istore #6
    //   105: ifge -> 116
    //   108: new java/util/concurrent/RejectedExecutionException
    //   111: dup
    //   112: invokespecial <init> : ()V
    //   115: athrow
    //   116: iload #6
    //   118: ifeq -> 142
    //   121: aload_0
    //   122: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   125: dup
    //   126: astore #4
    //   128: ifnull -> 142
    //   131: aload #4
    //   133: arraylength
    //   134: iconst_1
    //   135: isub
    //   136: dup
    //   137: istore #7
    //   139: ifge -> 352
    //   142: aload_0
    //   143: getfield parallelism : S
    //   146: istore #9
    //   148: iload #9
    //   150: iconst_1
    //   151: if_icmple -> 161
    //   154: iload #9
    //   156: iconst_1
    //   157: isub
    //   158: goto -> 162
    //   161: iconst_1
    //   162: istore #10
    //   164: iload #10
    //   166: iload #10
    //   168: iconst_1
    //   169: iushr
    //   170: ior
    //   171: istore #10
    //   173: iload #10
    //   175: iload #10
    //   177: iconst_2
    //   178: iushr
    //   179: ior
    //   180: istore #10
    //   182: iload #10
    //   184: iload #10
    //   186: iconst_4
    //   187: iushr
    //   188: ior
    //   189: istore #10
    //   191: iload #10
    //   193: iload #10
    //   195: bipush #8
    //   197: iushr
    //   198: ior
    //   199: istore #10
    //   201: iload #10
    //   203: iload #10
    //   205: bipush #16
    //   207: iushr
    //   208: ior
    //   209: istore #10
    //   211: iload #10
    //   213: iconst_1
    //   214: iadd
    //   215: iconst_1
    //   216: ishl
    //   217: istore #10
    //   219: aload_0
    //   220: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   223: dup
    //   224: astore #4
    //   226: ifnull -> 235
    //   229: aload #4
    //   231: arraylength
    //   232: ifne -> 243
    //   235: iload #10
    //   237: anewarray io/netty/util/internal/chmv8/ForkJoinPool$WorkQueue
    //   240: goto -> 244
    //   243: aconst_null
    //   244: astore #11
    //   246: aload_0
    //   247: getfield plock : I
    //   250: dup
    //   251: istore #6
    //   253: iconst_2
    //   254: iand
    //   255: ifne -> 278
    //   258: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   261: aload_0
    //   262: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   265: iload #6
    //   267: iinc #6, 2
    //   270: iload #6
    //   272: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   275: ifne -> 284
    //   278: aload_0
    //   279: invokespecial acquirePlock : ()I
    //   282: istore #6
    //   284: aload_0
    //   285: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   288: dup
    //   289: astore #4
    //   291: ifnull -> 300
    //   294: aload #4
    //   296: arraylength
    //   297: ifne -> 311
    //   300: aload #11
    //   302: ifnull -> 311
    //   305: aload_0
    //   306: aload #11
    //   308: putfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   311: iload #6
    //   313: ldc -2147483648
    //   315: iand
    //   316: iload #6
    //   318: iconst_2
    //   319: iadd
    //   320: ldc 2147483647
    //   322: iand
    //   323: ior
    //   324: istore #12
    //   326: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   329: aload_0
    //   330: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   333: iload #6
    //   335: iload #12
    //   337: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   340: ifne -> 349
    //   343: aload_0
    //   344: iload #12
    //   346: invokespecial releasePlock : (I)V
    //   349: goto -> 672
    //   352: aload #4
    //   354: iload_2
    //   355: iload #7
    //   357: iand
    //   358: bipush #126
    //   360: iand
    //   361: dup
    //   362: istore #8
    //   364: aaload
    //   365: dup
    //   366: astore #5
    //   368: ifnull -> 525
    //   371: aload #5
    //   373: getfield qlock : I
    //   376: ifne -> 520
    //   379: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   382: aload #5
    //   384: getstatic io/netty/util/internal/chmv8/ForkJoinPool.QLOCK : J
    //   387: iconst_0
    //   388: iconst_1
    //   389: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   392: ifeq -> 520
    //   395: aload #5
    //   397: getfield array : [Lio/netty/util/internal/chmv8/ForkJoinTask;
    //   400: astore #9
    //   402: aload #5
    //   404: getfield top : I
    //   407: istore #10
    //   409: iconst_0
    //   410: istore #11
    //   412: aload #9
    //   414: ifnull -> 433
    //   417: aload #9
    //   419: arraylength
    //   420: iload #10
    //   422: iconst_1
    //   423: iadd
    //   424: aload #5
    //   426: getfield base : I
    //   429: isub
    //   430: if_icmpgt -> 444
    //   433: aload #5
    //   435: invokevirtual growArray : ()[Lio/netty/util/internal/chmv8/ForkJoinTask;
    //   438: dup
    //   439: astore #9
    //   441: ifnull -> 486
    //   444: aload #9
    //   446: arraylength
    //   447: iconst_1
    //   448: isub
    //   449: iload #10
    //   451: iand
    //   452: getstatic io/netty/util/internal/chmv8/ForkJoinPool.ASHIFT : I
    //   455: ishl
    //   456: getstatic io/netty/util/internal/chmv8/ForkJoinPool.ABASE : I
    //   459: iadd
    //   460: istore #12
    //   462: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   465: aload #9
    //   467: iload #12
    //   469: i2l
    //   470: aload_1
    //   471: invokevirtual putOrderedObject : (Ljava/lang/Object;JLjava/lang/Object;)V
    //   474: aload #5
    //   476: iload #10
    //   478: iconst_1
    //   479: iadd
    //   480: putfield top : I
    //   483: iconst_1
    //   484: istore #11
    //   486: aload #5
    //   488: iconst_0
    //   489: putfield qlock : I
    //   492: goto -> 506
    //   495: astore #13
    //   497: aload #5
    //   499: iconst_0
    //   500: putfield qlock : I
    //   503: aload #13
    //   505: athrow
    //   506: iload #11
    //   508: ifeq -> 520
    //   511: aload_0
    //   512: aload #4
    //   514: aload #5
    //   516: invokevirtual signalWork : ([Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;)V
    //   519: return
    //   520: iconst_0
    //   521: istore_2
    //   522: goto -> 672
    //   525: aload_0
    //   526: getfield plock : I
    //   529: dup
    //   530: istore #6
    //   532: iconst_2
    //   533: iand
    //   534: ifne -> 670
    //   537: new io/netty/util/internal/chmv8/ForkJoinPool$WorkQueue
    //   540: dup
    //   541: aload_0
    //   542: aconst_null
    //   543: iconst_m1
    //   544: iload_2
    //   545: invokespecial <init> : (Lio/netty/util/internal/chmv8/ForkJoinPool;Lio/netty/util/internal/chmv8/ForkJoinWorkerThread;II)V
    //   548: astore #5
    //   550: aload #5
    //   552: iload #8
    //   554: i2s
    //   555: putfield poolIndex : S
    //   558: aload_0
    //   559: getfield plock : I
    //   562: dup
    //   563: istore #6
    //   565: iconst_2
    //   566: iand
    //   567: ifne -> 590
    //   570: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   573: aload_0
    //   574: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   577: iload #6
    //   579: iinc #6, 2
    //   582: iload #6
    //   584: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   587: ifne -> 596
    //   590: aload_0
    //   591: invokespecial acquirePlock : ()I
    //   594: istore #6
    //   596: aload_0
    //   597: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   600: dup
    //   601: astore #4
    //   603: ifnull -> 629
    //   606: iload #8
    //   608: aload #4
    //   610: arraylength
    //   611: if_icmpge -> 629
    //   614: aload #4
    //   616: iload #8
    //   618: aaload
    //   619: ifnonnull -> 629
    //   622: aload #4
    //   624: iload #8
    //   626: aload #5
    //   628: aastore
    //   629: iload #6
    //   631: ldc -2147483648
    //   633: iand
    //   634: iload #6
    //   636: iconst_2
    //   637: iadd
    //   638: ldc 2147483647
    //   640: iand
    //   641: ior
    //   642: istore #9
    //   644: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   647: aload_0
    //   648: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   651: iload #6
    //   653: iload #9
    //   655: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   658: ifne -> 667
    //   661: aload_0
    //   662: iload #9
    //   664: invokespecial releasePlock : (I)V
    //   667: goto -> 672
    //   670: iconst_0
    //   671: istore_2
    //   672: goto -> 12
    // Line number table:
    //   Java source line number -> byte code offset
    //   #1535	-> 0
    //   #1536	-> 2
    //   #1538	-> 12
    //   #1539	-> 16
    //   #1541	-> 45
    //   #1543	-> 64
    //   #1544	-> 68
    //   #1545	-> 73
    //   #1546	-> 80
    //   #1547	-> 87
    //   #1549	-> 98
    //   #1550	-> 108
    //   #1551	-> 116
    //   #1553	-> 142
    //   #1554	-> 148
    //   #1555	-> 164
    //   #1556	-> 191
    //   #1557	-> 219
    //   #1559	-> 246
    //   #1561	-> 278
    //   #1562	-> 284
    //   #1563	-> 305
    //   #1564	-> 311
    //   #1565	-> 326
    //   #1566	-> 343
    //   #1567	-> 349
    //   #1568	-> 352
    //   #1569	-> 371
    //   #1570	-> 395
    //   #1571	-> 402
    //   #1572	-> 409
    //   #1574	-> 412
    //   #1576	-> 444
    //   #1577	-> 462
    //   #1578	-> 474
    //   #1579	-> 483
    //   #1582	-> 486
    //   #1583	-> 492
    //   #1582	-> 495
    //   #1584	-> 506
    //   #1585	-> 511
    //   #1586	-> 519
    //   #1589	-> 520
    //   #1591	-> 525
    //   #1592	-> 537
    //   #1593	-> 550
    //   #1594	-> 558
    //   #1596	-> 590
    //   #1597	-> 596
    //   #1598	-> 622
    //   #1599	-> 629
    //   #1600	-> 644
    //   #1601	-> 661
    //   #1602	-> 667
    //   #1604	-> 670
    //   #1605	-> 672
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   128	14	4	ws	[Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   139	3	7	m	I
    //   148	201	9	p	I
    //   164	185	10	n	I
    //   246	103	11	nws	[Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   326	23	12	nps	I
    //   462	24	12	j	I
    //   402	118	9	a	[Lio/netty/util/internal/chmv8/ForkJoinTask;
    //   409	111	10	s	I
    //   412	108	11	submitted	Z
    //   644	23	9	nps	I
    //   226	446	4	ws	[Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   368	304	5	q	Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   105	567	6	ps	I
    //   352	320	7	m	I
    //   364	308	8	k	I
    //   12	663	3	z	Lio/netty/util/internal/chmv8/ForkJoinPool$Submitter;
    //   0	675	0	this	Lio/netty/util/internal/chmv8/ForkJoinPool;
    //   0	675	1	task	Lio/netty/util/internal/chmv8/ForkJoinTask;
    //   2	673	2	r	I
    // Local variable type table:
    //   start	length	slot	name	signature
    //   402	118	9	a	[Lio/netty/util/internal/chmv8/ForkJoinTask<*>;
    //   0	675	1	task	Lio/netty/util/internal/chmv8/ForkJoinTask<*>;
    // Exception table:
    //   from	to	target	type
    //   412	486	495	finally
    //   495	497	495	finally
  }
  
  final void incrementActiveCount() {
    long c;
    do {
    
    } while (!U.compareAndSwapLong(this, CTL, c = this.ctl, c & 0xFFFFFFFFFFFFL | (c & 0xFFFF000000000000L) + 281474976710656L));
  }
  
  final void signalWork(WorkQueue[] ws, WorkQueue q) {
    long c;
    int u;
    while ((u = (int)((c = this.ctl) >>> 32L)) < 0) {
      int e;
      if ((e = (int)c) <= 0) {
        if ((short)u < 0)
          tryAddWorker(); 
        break;
      } 
      int i;
      WorkQueue w;
      if (ws == null || ws.length <= (i = e & 0xFFFF) || (w = ws[i]) == null)
        break; 
      long nc = (w.nextWait & Integer.MAX_VALUE) | (u + 65536) << 32L;
      int ne = e + 65536 & Integer.MAX_VALUE;
      if (w.eventCount == (e | Integer.MIN_VALUE) && U.compareAndSwapLong(this, CTL, c, nc)) {
        w.eventCount = ne;
        Thread p;
        if ((p = w.parker) != null)
          U.unpark(p); 
        break;
      } 
      if (q != null && q.base >= q.top)
        break; 
    } 
  }
  
  final void runWorker(WorkQueue w) {
    w.growArray();
    for (int r = w.hint; scan(w, r) == 0; ) {
      r ^= r << 13;
      r ^= r >>> 17;
      r ^= r << 5;
    } 
  }
  
  private final int scan(WorkQueue w, int r) {
    long c = this.ctl;
    WorkQueue[] ws;
    int m;
    if ((ws = this.workQueues) != null && (m = ws.length - 1) >= 0 && w != null) {
      int j = m + m + 1, ec = w.eventCount;
      while (true) {
        WorkQueue q;
        int b;
        ForkJoinTask<?>[] a;
        if ((q = ws[r - j & m]) != null && (b = q.base) - q.top < 0 && (a = q.array) != null) {
          long i = (((a.length - 1 & b) << ASHIFT) + ABASE);
          ForkJoinTask<?> t;
          if ((t = (ForkJoinTask)U.getObjectVolatile(a, i)) != null) {
            if (ec < 0) {
              helpRelease(c, ws, w, q, b);
              break;
            } 
            if (q.base == b && U.compareAndSwapObject(a, i, t, null)) {
              U.putOrderedInt(q, QBASE, b + 1);
              if (b + 1 - q.top < 0)
                signalWork(ws, q); 
              w.runTask(t);
            } 
          } 
          break;
        } 
        if (--j < 0) {
          int e;
          if ((ec | (e = (int)c)) < 0)
            return awaitWork(w, c, ec); 
          if (this.ctl == c) {
            long nc = ec | c - 281474976710656L & 0xFFFFFFFF00000000L;
            w.nextWait = e;
            w.eventCount = ec | Integer.MIN_VALUE;
            if (!U.compareAndSwapLong(this, CTL, c, nc))
              w.eventCount = ec; 
          } 
          break;
        } 
      } 
    } 
    return 0;
  }
  
  private final int awaitWork(WorkQueue w, long c, int ec) {
    int stat;
    if ((stat = w.qlock) >= 0 && w.eventCount == ec && this.ctl == c && !Thread.interrupted()) {
      int e = (int)c;
      int u = (int)(c >>> 32L);
      int d = (u >> 16) + this.parallelism;
      if (e < 0 || (d <= 0 && tryTerminate(false, false))) {
        stat = w.qlock = -1;
      } else {
        int ns;
        if ((ns = w.nsteals) != 0) {
          w.nsteals = 0;
          long sc;
          do {
          
          } while (!U.compareAndSwapLong(this, STEALCOUNT, sc = this.stealCount, sc + ns));
        } else {
          long parkTime, deadline, pc = (d > 0 || ec != (e | Integer.MIN_VALUE)) ? 0L : ((w.nextWait & Integer.MAX_VALUE) | (u + 65536) << 32L);
          if (pc != 0L) {
            int dc = -((short)(int)(c >>> 32L));
            parkTime = (dc < 0) ? 200000000L : ((dc + 1) * 2000000000L);
            deadline = System.nanoTime() + parkTime - 2000000L;
          } else {
            parkTime = deadline = 0L;
          } 
          if (w.eventCount == ec && this.ctl == c) {
            Thread wt = Thread.currentThread();
            U.putObject(wt, PARKBLOCKER, this);
            w.parker = wt;
            if (w.eventCount == ec && this.ctl == c)
              U.park(false, parkTime); 
            w.parker = null;
            U.putObject(wt, PARKBLOCKER, null);
            if (parkTime != 0L && this.ctl == c && deadline - System.nanoTime() <= 0L && U.compareAndSwapLong(this, CTL, c, pc))
              stat = w.qlock = -1; 
          } 
        } 
      } 
    } 
    return stat;
  }
  
  private final void helpRelease(long c, WorkQueue[] ws, WorkQueue w, WorkQueue q, int b) {
    WorkQueue v;
    int e;
    int i;
    if (w != null && w.eventCount < 0 && (e = (int)c) > 0 && ws != null && ws.length > (i = e & 0xFFFF) && (v = ws[i]) != null && this.ctl == c) {
      long nc = (v.nextWait & Integer.MAX_VALUE) | ((int)(c >>> 32L) + 65536) << 32L;
      int ne = e + 65536 & Integer.MAX_VALUE;
      if (q != null && q.base == b && w.eventCount < 0 && v.eventCount == (e | Integer.MIN_VALUE) && U.compareAndSwapLong(this, CTL, c, nc)) {
        v.eventCount = ne;
        Thread p;
        if ((p = v.parker) != null)
          U.unpark(p); 
      } 
    } 
  }
  
  private int tryHelpStealer(WorkQueue joiner, ForkJoinTask<?> task) {
    int stat = 0, steps = 0;
    if (task != null && joiner != null && joiner.base - joiner.top >= 0)
      label82: while (true) {
        ForkJoinTask<?> subtask = task;
        WorkQueue j = joiner;
        while (true) {
          int s;
          if ((s = task.status) < 0) {
            stat = s;
            break;
          } 
          WorkQueue[] ws;
          int m;
          if ((ws = this.workQueues) == null || (m = ws.length - 1) <= 0)
            break; 
          WorkQueue v;
          int h;
          if ((v = ws[h = (j.hint | 0x1) & m]) == null || v.currentSteal != subtask) {
            int origin = h;
            do {
              if (((h = h + 2 & m) & 0xF) == 1)
                if (subtask.status >= 0) {
                  if (j.currentJoin != subtask)
                    continue label82; 
                } else {
                  continue label82;
                }  
              if ((v = ws[h]) != null && v.currentSteal == subtask) {
                j.hint = h;
                continue label74;
              } 
            } while (h != origin);
            break;
          } 
          label74: while (subtask.status >= 0) {
            ForkJoinTask<?>[] arrayOfForkJoinTask;
            int b;
            if ((b = v.base) - v.top < 0 && (arrayOfForkJoinTask = v.array) != null) {
              int i = ((arrayOfForkJoinTask.length - 1 & b) << ASHIFT) + ABASE;
              ForkJoinTask<?> t = (ForkJoinTask)U.getObjectVolatile(arrayOfForkJoinTask, i);
              if (subtask.status < 0 || j.currentJoin != subtask || v.currentSteal != subtask)
                break; 
              stat = 1;
              if (v.base == b) {
                if (t == null)
                  // Byte code: goto -> 477 
                if (U.compareAndSwapObject(arrayOfForkJoinTask, i, t, null)) {
                  U.putOrderedInt(v, QBASE, b + 1);
                  ForkJoinTask<?> ps = joiner.currentSteal;
                  int jt = joiner.top;
                  do {
                    joiner.currentSteal = t;
                    t.doExec();
                  } while (task.status >= 0 && joiner.top != jt && (t = joiner.pop()) != null);
                  joiner.currentSteal = ps;
                  // Byte code: goto -> 477
                } 
              } 
              continue;
            } 
            ForkJoinTask<?> next = v.currentJoin;
            if (subtask.status >= 0) {
              if (j.currentJoin == subtask) {
                if (v.currentSteal != subtask)
                  continue label82; 
                if (next == null || ++steps == 64)
                  break; 
                subtask = next;
                j = v;
                continue;
              } 
              continue label82;
            } 
            continue label82;
          } 
          continue label82;
        } 
        break;
      }  
    return stat;
  }
  
  private int helpComplete(WorkQueue joiner, CountedCompleter<?> task) {
    int s = 0;
    WorkQueue[] ws;
    int m;
    if ((ws = this.workQueues) != null && (m = ws.length - 1) >= 0 && joiner != null && task != null) {
      int j = joiner.poolIndex;
      int scans = m + m + 1;
      long c = 0L;
      int k = scans;
      for (; (s = task.status) >= 0; j += 2) {
        if (joiner.internalPopAndExecCC(task)) {
          k = scans;
        } else {
          if ((s = task.status) < 0)
            break; 
          WorkQueue q;
          if ((q = ws[j & m]) != null && q.pollAndExecCC(task)) {
            k = scans;
          } else if (--k < 0) {
            if (c == (c = this.ctl))
              break; 
            k = scans;
          } 
        } 
      } 
    } 
    return s;
  }
  
  final boolean tryCompensate(long c) {
    WorkQueue[] ws = this.workQueues;
    int pc = this.parallelism, e = (int)c;
    int m;
    if (ws != null && (m = ws.length - 1) >= 0 && e >= 0 && this.ctl == c) {
      WorkQueue w = ws[e & m];
      if (e != 0 && w != null) {
        long nc = (w.nextWait & Integer.MAX_VALUE) | c & 0xFFFFFFFF00000000L;
        int ne = e + 65536 & Integer.MAX_VALUE;
        if (w.eventCount == (e | Integer.MIN_VALUE) && U.compareAndSwapLong(this, CTL, c, nc)) {
          w.eventCount = ne;
          Thread p;
          if ((p = w.parker) != null)
            U.unpark(p); 
          return true;
        } 
      } else {
        int tc;
        if ((tc = (short)(int)(c >>> 32L)) >= 0 && (int)(c >> 48L) + pc > 1) {
          long nc = c - 281474976710656L & 0xFFFF000000000000L | c & 0xFFFFFFFFFFFFL;
          if (U.compareAndSwapLong(this, CTL, c, nc))
            return true; 
        } else if (tc + pc < 32767) {
          long nc = c + 4294967296L & 0xFFFF00000000L | c & 0xFFFF0000FFFFFFFFL;
          if (U.compareAndSwapLong(this, CTL, c, nc)) {
            Throwable ex = null;
            ForkJoinWorkerThread wt = null;
            try {
              ForkJoinWorkerThreadFactory fac;
              if ((fac = this.factory) != null && (wt = fac.newThread(this)) != null) {
                wt.start();
                return true;
              } 
            } catch (Throwable rex) {
              ex = rex;
            } 
            deregisterWorker(wt, ex);
          } 
        } 
      } 
    } 
    return false;
  }
  
  final int awaitJoin(WorkQueue joiner, ForkJoinTask<?> task) {
    int s = 0;
    if (task != null && (s = task.status) >= 0 && joiner != null) {
      ForkJoinTask<?> prevJoin = joiner.currentJoin;
      joiner.currentJoin = task;
      do {
      
      } while (joiner.tryRemoveAndExec(task) && (s = task.status) >= 0);
      if (s >= 0 && task instanceof CountedCompleter)
        s = helpComplete(joiner, (CountedCompleter)task); 
      long cc = 0L;
      while (s >= 0 && (s = task.status) >= 0) {
        if ((s = tryHelpStealer(joiner, task)) == 0 && (s = task.status) >= 0) {
          if (!tryCompensate(cc)) {
            cc = this.ctl;
            continue;
          } 
          if (task.trySetSignal() && (s = task.status) >= 0)
            synchronized (task) {
              if (task.status >= 0) {
                try {
                  task.wait();
                } catch (InterruptedException ie) {}
              } else {
                task.notifyAll();
              } 
            }  
          long c;
          do {
          
          } while (!U.compareAndSwapLong(this, CTL, c = this.ctl, c & 0xFFFFFFFFFFFFL | (c & 0xFFFF000000000000L) + 281474976710656L));
        } 
      } 
      joiner.currentJoin = prevJoin;
    } 
    return s;
  }
  
  final void helpJoinOnce(WorkQueue joiner, ForkJoinTask<?> task) {
    int s;
    if (joiner != null && task != null && (s = task.status) >= 0) {
      ForkJoinTask<?> prevJoin = joiner.currentJoin;
      joiner.currentJoin = task;
      do {
      
      } while (joiner.tryRemoveAndExec(task) && (s = task.status) >= 0);
      if (s >= 0) {
        if (task instanceof CountedCompleter)
          helpComplete(joiner, (CountedCompleter)task); 
        do {
        
        } while (task.status >= 0 && tryHelpStealer(joiner, task) > 0);
      } 
      joiner.currentJoin = prevJoin;
    } 
  }
  
  private WorkQueue findNonEmptyStealQueue() {
    int r = ThreadLocalRandom.current().nextInt();
    while (true) {
      int ps = this.plock;
      int m;
      WorkQueue[] ws;
      if ((ws = this.workQueues) != null && (m = ws.length - 1) >= 0)
        for (int j = m + 1 << 2; j >= 0; j--) {
          WorkQueue q;
          if ((q = ws[(r - j << 1 | 0x1) & m]) != null && q.base - q.top < 0)
            return q; 
        }  
      if (this.plock == ps)
        return null; 
    } 
  }
  
  final void helpQuiescePool(WorkQueue w) {
    ForkJoinTask<?> ps = w.currentSteal;
    boolean active = true;
    while (true) {
      ForkJoinTask<?> t;
      while ((t = w.nextLocalTask()) != null)
        t.doExec(); 
      WorkQueue q;
      if ((q = findNonEmptyStealQueue()) != null) {
        if (!active) {
          active = true;
          long l;
          do {
          
          } while (!U.compareAndSwapLong(this, CTL, l = this.ctl, l & 0xFFFFFFFFFFFFL | (l & 0xFFFF000000000000L) + 281474976710656L));
        } 
        int b;
        if ((b = q.base) - q.top < 0 && (t = q.pollAt(b)) != null) {
          (w.currentSteal = t).doExec();
          w.currentSteal = ps;
        } 
        continue;
      } 
      if (active) {
        long l1, nc = (l1 = this.ctl) & 0xFFFFFFFFFFFFL | (l1 & 0xFFFF000000000000L) - 281474976710656L;
        if ((int)(nc >> 48L) + this.parallelism == 0)
          break; 
        if (U.compareAndSwapLong(this, CTL, l1, nc))
          active = false; 
        continue;
      } 
      long c;
      if ((int)((c = this.ctl) >> 48L) + this.parallelism <= 0 && U.compareAndSwapLong(this, CTL, c, c & 0xFFFFFFFFFFFFL | (c & 0xFFFF000000000000L) + 281474976710656L))
        break; 
    } 
  }
  
  final ForkJoinTask<?> nextTaskFor(WorkQueue w) {
    while (true) {
      ForkJoinTask<?> t;
      if ((t = w.nextLocalTask()) != null)
        return t; 
      WorkQueue q;
      if ((q = findNonEmptyStealQueue()) == null)
        return null; 
      int b;
      if ((b = q.base) - q.top < 0 && (t = q.pollAt(b)) != null)
        return t; 
    } 
  }
  
  static int getSurplusQueuedTaskCount() {
    Thread t;
    if (t = Thread.currentThread() instanceof ForkJoinWorkerThread) {
      ForkJoinWorkerThread wt;
      ForkJoinPool pool;
      int p = (pool = (wt = (ForkJoinWorkerThread)t).pool).parallelism;
      WorkQueue q;
      int n = (q = wt.workQueue).top - q.base;
      int a = (int)(pool.ctl >> 48L) + p;
      return n - ((a > (p >>>= 1)) ? 0 : ((a > (p >>>= 1)) ? 1 : ((a > (p >>>= 1)) ? 2 : ((a > (p >>>= 1)) ? 4 : 8))));
    } 
    return 0;
  }
  
  private boolean tryTerminate(boolean now, boolean enable) {
    // Byte code:
    //   0: aload_0
    //   1: getstatic io/netty/util/internal/chmv8/ForkJoinPool.common : Lio/netty/util/internal/chmv8/ForkJoinPool;
    //   4: if_acmpne -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield plock : I
    //   13: dup
    //   14: istore_3
    //   15: iflt -> 86
    //   18: iload_2
    //   19: ifne -> 24
    //   22: iconst_0
    //   23: ireturn
    //   24: iload_3
    //   25: iconst_2
    //   26: iand
    //   27: ifne -> 48
    //   30: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   33: aload_0
    //   34: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   37: iload_3
    //   38: iinc #3, 2
    //   41: iload_3
    //   42: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   45: ifne -> 53
    //   48: aload_0
    //   49: invokespecial acquirePlock : ()I
    //   52: istore_3
    //   53: iload_3
    //   54: iconst_2
    //   55: iadd
    //   56: ldc 2147483647
    //   58: iand
    //   59: ldc -2147483648
    //   61: ior
    //   62: istore #4
    //   64: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   67: aload_0
    //   68: getstatic io/netty/util/internal/chmv8/ForkJoinPool.PLOCK : J
    //   71: iload_3
    //   72: iload #4
    //   74: invokevirtual compareAndSwapInt : (Ljava/lang/Object;JII)Z
    //   77: ifne -> 86
    //   80: aload_0
    //   81: iload #4
    //   83: invokespecial releasePlock : (I)V
    //   86: aload_0
    //   87: getfield ctl : J
    //   90: dup2
    //   91: lstore #4
    //   93: ldc2_w 2147483648
    //   96: land
    //   97: lconst_0
    //   98: lcmp
    //   99: ifeq -> 142
    //   102: lload #4
    //   104: bipush #32
    //   106: lushr
    //   107: l2i
    //   108: i2s
    //   109: aload_0
    //   110: getfield parallelism : S
    //   113: iadd
    //   114: ifgt -> 140
    //   117: aload_0
    //   118: dup
    //   119: astore #6
    //   121: monitorenter
    //   122: aload_0
    //   123: invokevirtual notifyAll : ()V
    //   126: aload #6
    //   128: monitorexit
    //   129: goto -> 140
    //   132: astore #7
    //   134: aload #6
    //   136: monitorexit
    //   137: aload #7
    //   139: athrow
    //   140: iconst_1
    //   141: ireturn
    //   142: iload_1
    //   143: ifne -> 233
    //   146: lload #4
    //   148: bipush #48
    //   150: lshr
    //   151: l2i
    //   152: aload_0
    //   153: getfield parallelism : S
    //   156: iadd
    //   157: ifle -> 162
    //   160: iconst_0
    //   161: ireturn
    //   162: aload_0
    //   163: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   166: dup
    //   167: astore #6
    //   169: ifnull -> 233
    //   172: iconst_0
    //   173: istore #8
    //   175: iload #8
    //   177: aload #6
    //   179: arraylength
    //   180: if_icmpge -> 233
    //   183: aload #6
    //   185: iload #8
    //   187: aaload
    //   188: dup
    //   189: astore #7
    //   191: ifnull -> 227
    //   194: aload #7
    //   196: invokevirtual isEmpty : ()Z
    //   199: ifeq -> 217
    //   202: iload #8
    //   204: iconst_1
    //   205: iand
    //   206: ifeq -> 227
    //   209: aload #7
    //   211: getfield eventCount : I
    //   214: iflt -> 227
    //   217: aload_0
    //   218: aload #6
    //   220: aload #7
    //   222: invokevirtual signalWork : ([Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;)V
    //   225: iconst_0
    //   226: ireturn
    //   227: iinc #8, 1
    //   230: goto -> 175
    //   233: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   236: aload_0
    //   237: getstatic io/netty/util/internal/chmv8/ForkJoinPool.CTL : J
    //   240: lload #4
    //   242: lload #4
    //   244: ldc2_w 2147483648
    //   247: lor
    //   248: invokevirtual compareAndSwapLong : (Ljava/lang/Object;JJJ)Z
    //   251: ifeq -> 86
    //   254: iconst_0
    //   255: istore #6
    //   257: iload #6
    //   259: iconst_3
    //   260: if_icmpge -> 516
    //   263: aload_0
    //   264: getfield workQueues : [Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   267: dup
    //   268: astore #7
    //   270: ifnull -> 510
    //   273: aload #7
    //   275: arraylength
    //   276: istore #10
    //   278: iconst_0
    //   279: istore #11
    //   281: iload #11
    //   283: iload #10
    //   285: if_icmpge -> 364
    //   288: aload #7
    //   290: iload #11
    //   292: aaload
    //   293: dup
    //   294: astore #8
    //   296: ifnull -> 358
    //   299: aload #8
    //   301: iconst_m1
    //   302: putfield qlock : I
    //   305: iload #6
    //   307: ifle -> 358
    //   310: aload #8
    //   312: invokevirtual cancelAll : ()V
    //   315: iload #6
    //   317: iconst_1
    //   318: if_icmple -> 358
    //   321: aload #8
    //   323: getfield owner : Lio/netty/util/internal/chmv8/ForkJoinWorkerThread;
    //   326: dup
    //   327: astore #9
    //   329: ifnull -> 358
    //   332: aload #9
    //   334: invokevirtual isInterrupted : ()Z
    //   337: ifne -> 350
    //   340: aload #9
    //   342: invokevirtual interrupt : ()V
    //   345: goto -> 350
    //   348: astore #12
    //   350: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   353: aload #9
    //   355: invokevirtual unpark : (Ljava/lang/Object;)V
    //   358: iinc #11, 1
    //   361: goto -> 281
    //   364: aload_0
    //   365: getfield ctl : J
    //   368: dup2
    //   369: lstore #13
    //   371: l2i
    //   372: ldc 2147483647
    //   374: iand
    //   375: dup
    //   376: istore #12
    //   378: ifeq -> 510
    //   381: iload #12
    //   383: ldc 65535
    //   385: iand
    //   386: dup
    //   387: istore #11
    //   389: iload #10
    //   391: if_icmpge -> 510
    //   394: iload #11
    //   396: iflt -> 510
    //   399: aload #7
    //   401: iload #11
    //   403: aaload
    //   404: dup
    //   405: astore #8
    //   407: ifnull -> 510
    //   410: aload #8
    //   412: getfield nextWait : I
    //   415: ldc 2147483647
    //   417: iand
    //   418: i2l
    //   419: lload #13
    //   421: ldc2_w 281474976710656
    //   424: ladd
    //   425: ldc2_w -281474976710656
    //   428: land
    //   429: lor
    //   430: lload #13
    //   432: ldc2_w 281472829227008
    //   435: land
    //   436: lor
    //   437: lstore #16
    //   439: aload #8
    //   441: getfield eventCount : I
    //   444: iload #12
    //   446: ldc -2147483648
    //   448: ior
    //   449: if_icmpne -> 507
    //   452: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   455: aload_0
    //   456: getstatic io/netty/util/internal/chmv8/ForkJoinPool.CTL : J
    //   459: lload #13
    //   461: lload #16
    //   463: invokevirtual compareAndSwapLong : (Ljava/lang/Object;JJJ)Z
    //   466: ifeq -> 507
    //   469: aload #8
    //   471: iload #12
    //   473: ldc 65536
    //   475: iadd
    //   476: ldc 2147483647
    //   478: iand
    //   479: putfield eventCount : I
    //   482: aload #8
    //   484: iconst_m1
    //   485: putfield qlock : I
    //   488: aload #8
    //   490: getfield parker : Ljava/lang/Thread;
    //   493: dup
    //   494: astore #15
    //   496: ifnull -> 507
    //   499: getstatic io/netty/util/internal/chmv8/ForkJoinPool.U : Lsun/misc/Unsafe;
    //   502: aload #15
    //   504: invokevirtual unpark : (Ljava/lang/Object;)V
    //   507: goto -> 364
    //   510: iinc #6, 1
    //   513: goto -> 257
    //   516: goto -> 86
    // Line number table:
    //   Java source line number -> byte code offset
    //   #2229	-> 0
    //   #2230	-> 7
    //   #2231	-> 9
    //   #2232	-> 18
    //   #2233	-> 22
    //   #2234	-> 24
    //   #2236	-> 48
    //   #2237	-> 53
    //   #2238	-> 64
    //   #2239	-> 80
    //   #2242	-> 86
    //   #2243	-> 102
    //   #2244	-> 117
    //   #2245	-> 122
    //   #2246	-> 126
    //   #2248	-> 140
    //   #2250	-> 142
    //   #2252	-> 146
    //   #2253	-> 160
    //   #2254	-> 162
    //   #2255	-> 172
    //   #2256	-> 183
    //   #2259	-> 217
    //   #2260	-> 225
    //   #2255	-> 227
    //   #2265	-> 233
    //   #2266	-> 254
    //   #2268	-> 263
    //   #2269	-> 273
    //   #2270	-> 278
    //   #2271	-> 288
    //   #2272	-> 299
    //   #2273	-> 305
    //   #2274	-> 310
    //   #2275	-> 315
    //   #2276	-> 332
    //   #2278	-> 340
    //   #2280	-> 345
    //   #2279	-> 348
    //   #2282	-> 350
    //   #2270	-> 358
    //   #2290	-> 364
    //   #2292	-> 410
    //   #2295	-> 439
    //   #2297	-> 469
    //   #2298	-> 482
    //   #2299	-> 488
    //   #2300	-> 499
    //   #2302	-> 507
    //   #2266	-> 510
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   64	22	4	nps	I
    //   191	42	7	w	Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   175	58	8	i	I
    //   169	64	6	ws	[Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   350	0	12	ignore	Ljava/lang/Throwable;
    //   329	29	9	wt	Ljava/lang/Thread;
    //   296	68	8	w	Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   281	83	11	i	I
    //   496	11	15	p	Ljava/lang/Thread;
    //   439	68	16	nc	J
    //   407	103	8	w	Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   278	232	10	n	I
    //   389	121	11	i	I
    //   378	132	12	e	I
    //   371	139	13	cc	J
    //   270	240	7	ws	[Lio/netty/util/internal/chmv8/ForkJoinPool$WorkQueue;
    //   257	259	6	pass	I
    //   93	426	4	c	J
    //   0	519	0	this	Lio/netty/util/internal/chmv8/ForkJoinPool;
    //   0	519	1	now	Z
    //   0	519	2	enable	Z
    //   15	504	3	ps	I
    // Exception table:
    //   from	to	target	type
    //   122	129	132	finally
    //   132	137	132	finally
    //   340	345	348	java/lang/Throwable
  }
  
  static WorkQueue commonSubmitterQueue() {
    Submitter z;
    ForkJoinPool p;
    WorkQueue[] ws;
    int m;
    return ((z = submitters.get()) != null && (p = common) != null && (ws = p.workQueues) != null && (m = ws.length - 1) >= 0) ? ws[m & z.seed & 0x7E] : null;
  }
  
  final boolean tryExternalUnpush(ForkJoinTask<?> task) {
    Submitter z = submitters.get();
    WorkQueue[] ws = this.workQueues;
    boolean popped = false;
    WorkQueue joiner;
    ForkJoinTask<?>[] a;
    int m, s;
    if (z != null && ws != null && (m = ws.length - 1) >= 0 && (joiner = ws[z.seed & m & 0x7E]) != null && joiner.base != (s = joiner.top) && (a = joiner.array) != null) {
      long j = (((a.length - 1 & s - 1) << ASHIFT) + ABASE);
      if (U.getObject(a, j) == task && U.compareAndSwapInt(joiner, QLOCK, 0, 1)) {
        if (joiner.top == s && joiner.array == a && U.compareAndSwapObject(a, j, task, null)) {
          joiner.top = s - 1;
          popped = true;
        } 
        joiner.qlock = 0;
      } 
    } 
    return popped;
  }
  
  final int externalHelpComplete(CountedCompleter<?> task) {
    Submitter z = submitters.get();
    WorkQueue[] ws = this.workQueues;
    int s = 0;
    WorkQueue joiner;
    int m, j;
    if (z != null && ws != null && (m = ws.length - 1) >= 0 && (joiner = ws[(j = z.seed) & m & 0x7E]) != null && task != null) {
      int scans = m + m + 1;
      long c = 0L;
      j |= 0x1;
      int k = scans;
      for (; (s = task.status) >= 0; j += 2) {
        if (joiner.externalPopAndExecCC(task)) {
          k = scans;
        } else {
          if ((s = task.status) < 0)
            break; 
          WorkQueue q;
          if ((q = ws[j & m]) != null && q.pollAndExecCC(task)) {
            k = scans;
          } else if (--k < 0) {
            if (c == (c = this.ctl))
              break; 
            k = scans;
          } 
        } 
      } 
    } 
    return s;
  }
  
  public ForkJoinPool() {
    this(Math.min(32767, Runtime.getRuntime().availableProcessors()), defaultForkJoinWorkerThreadFactory, null, false);
  }
  
  public ForkJoinPool(int parallelism) {
    this(parallelism, defaultForkJoinWorkerThreadFactory, null, false);
  }
  
  public ForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, boolean asyncMode) {
    this(checkParallelism(parallelism), checkFactory(factory), handler, asyncMode ? 1 : 0, "ForkJoinPool-" + nextPoolId() + "-worker-");
    checkPermission();
  }
  
  private static int checkParallelism(int parallelism) {
    if (parallelism <= 0 || parallelism > 32767)
      throw new IllegalArgumentException(); 
    return parallelism;
  }
  
  private static ForkJoinWorkerThreadFactory checkFactory(ForkJoinWorkerThreadFactory factory) {
    if (factory == null)
      throw new NullPointerException(); 
    return factory;
  }
  
  private ForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, int mode, String workerNamePrefix) {
    this.workerNamePrefix = workerNamePrefix;
    this.factory = factory;
    this.ueh = handler;
    this.mode = (short)mode;
    this.parallelism = (short)parallelism;
    long np = -parallelism;
    this.ctl = np << 48L & 0xFFFF000000000000L | np << 32L & 0xFFFF00000000L;
  }
  
  public static ForkJoinPool commonPool() {
    return common;
  }
  
  public <T> T invoke(ForkJoinTask<T> task) {
    if (task == null)
      throw new NullPointerException(); 
    externalPush(task);
    return task.join();
  }
  
  public void execute(ForkJoinTask<?> task) {
    if (task == null)
      throw new NullPointerException(); 
    externalPush(task);
  }
  
  public void execute(Runnable task) {
    ForkJoinTask<?> job;
    if (task == null)
      throw new NullPointerException(); 
    if (task instanceof ForkJoinTask) {
      job = (ForkJoinTask)task;
    } else {
      job = new ForkJoinTask.RunnableExecuteAction(task);
    } 
    externalPush(job);
  }
  
  public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
    if (task == null)
      throw new NullPointerException(); 
    externalPush(task);
    return task;
  }
  
  public <T> ForkJoinTask<T> submit(Callable<T> task) {
    ForkJoinTask<T> job = new ForkJoinTask.AdaptedCallable<T>(task);
    externalPush(job);
    return job;
  }
  
  public <T> ForkJoinTask<T> submit(Runnable task, T result) {
    ForkJoinTask<T> job = new ForkJoinTask.AdaptedRunnable<T>(task, result);
    externalPush(job);
    return job;
  }
  
  public ForkJoinTask<?> submit(Runnable task) {
    ForkJoinTask<?> job;
    if (task == null)
      throw new NullPointerException(); 
    if (task instanceof ForkJoinTask) {
      job = (ForkJoinTask)task;
    } else {
      job = new ForkJoinTask.AdaptedRunnableAction(task);
    } 
    externalPush(job);
    return job;
  }
  
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
    ArrayList<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
    boolean done = false;
    try {
      for (Callable<T> t : tasks) {
        ForkJoinTask<T> f = new ForkJoinTask.AdaptedCallable<T>(t);
        futures.add(f);
        externalPush(f);
      } 
      for (int i = 0, size = futures.size(); i < size; i++)
        ((ForkJoinTask)futures.get(i)).quietlyJoin(); 
      done = true;
      return futures;
    } finally {
      if (!done)
        for (int i = 0, size = futures.size(); i < size; i++)
          ((Future)futures.get(i)).cancel(false);  
    } 
  }
  
  public ForkJoinWorkerThreadFactory getFactory() {
    return this.factory;
  }
  
  public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
    return this.ueh;
  }
  
  public int getParallelism() {
    int par;
    return ((par = this.parallelism) > 0) ? par : 1;
  }
  
  public static int getCommonPoolParallelism() {
    return commonParallelism;
  }
  
  public int getPoolSize() {
    return this.parallelism + (short)(int)(this.ctl >>> 32L);
  }
  
  public boolean getAsyncMode() {
    return (this.mode == 1);
  }
  
  public int getRunningThreadCount() {
    int rc = 0;
    WorkQueue[] ws;
    if ((ws = this.workQueues) != null)
      for (int i = 1; i < ws.length; i += 2) {
        WorkQueue w;
        if ((w = ws[i]) != null && w.isApparentlyUnblocked())
          rc++; 
      }  
    return rc;
  }
  
  public int getActiveThreadCount() {
    int r = this.parallelism + (int)(this.ctl >> 48L);
    return (r <= 0) ? 0 : r;
  }
  
  public boolean isQuiescent() {
    return (this.parallelism + (int)(this.ctl >> 48L) <= 0);
  }
  
  public long getStealCount() {
    long count = this.stealCount;
    WorkQueue[] ws;
    if ((ws = this.workQueues) != null)
      for (int i = 1; i < ws.length; i += 2) {
        WorkQueue w;
        if ((w = ws[i]) != null)
          count += w.nsteals; 
      }  
    return count;
  }
  
  public long getQueuedTaskCount() {
    long count = 0L;
    WorkQueue[] ws;
    if ((ws = this.workQueues) != null)
      for (int i = 1; i < ws.length; i += 2) {
        WorkQueue w;
        if ((w = ws[i]) != null)
          count += w.queueSize(); 
      }  
    return count;
  }
  
  public int getQueuedSubmissionCount() {
    int count = 0;
    WorkQueue[] ws;
    if ((ws = this.workQueues) != null)
      for (int i = 0; i < ws.length; i += 2) {
        WorkQueue w;
        if ((w = ws[i]) != null)
          count += w.queueSize(); 
      }  
    return count;
  }
  
  public boolean hasQueuedSubmissions() {
    WorkQueue[] ws;
    if ((ws = this.workQueues) != null)
      for (int i = 0; i < ws.length; i += 2) {
        WorkQueue w;
        if ((w = ws[i]) != null && !w.isEmpty())
          return true; 
      }  
    return false;
  }
  
  protected ForkJoinTask<?> pollSubmission() {
    WorkQueue[] ws;
    if ((ws = this.workQueues) != null)
      for (int i = 0; i < ws.length; i += 2) {
        WorkQueue w;
        ForkJoinTask<?> t;
        if ((w = ws[i]) != null && (t = w.poll()) != null)
          return t; 
      }  
    return null;
  }
  
  protected int drainTasksTo(Collection<? super ForkJoinTask<?>> c) {
    int count = 0;
    WorkQueue[] ws;
    if ((ws = this.workQueues) != null)
      for (int i = 0; i < ws.length; i++) {
        WorkQueue w;
        if ((w = ws[i]) != null) {
          ForkJoinTask<?> t;
          while ((t = w.poll()) != null) {
            c.add(t);
            count++;
          } 
        } 
      }  
    return count;
  }
  
  public String toString() {
    String level;
    long qt = 0L, qs = 0L;
    int rc = 0;
    long st = this.stealCount;
    long c = this.ctl;
    WorkQueue[] ws;
    if ((ws = this.workQueues) != null)
      for (int i = 0; i < ws.length; i++) {
        WorkQueue w;
        if ((w = ws[i]) != null) {
          int size = w.queueSize();
          if ((i & 0x1) == 0) {
            qs += size;
          } else {
            qt += size;
            st += w.nsteals;
            if (w.isApparentlyUnblocked())
              rc++; 
          } 
        } 
      }  
    int pc = this.parallelism;
    int tc = pc + (short)(int)(c >>> 32L);
    int ac = pc + (int)(c >> 48L);
    if (ac < 0)
      ac = 0; 
    if ((c & 0x80000000L) != 0L) {
      level = (tc == 0) ? "Terminated" : "Terminating";
    } else {
      level = (this.plock < 0) ? "Shutting down" : "Running";
    } 
    return super.toString() + "[" + level + ", parallelism = " + pc + ", size = " + tc + ", active = " + ac + ", running = " + rc + ", steals = " + st + ", tasks = " + qt + ", submissions = " + qs + "]";
  }
  
  public void shutdown() {
    checkPermission();
    tryTerminate(false, true);
  }
  
  public List<Runnable> shutdownNow() {
    checkPermission();
    tryTerminate(true, true);
    return Collections.emptyList();
  }
  
  public boolean isTerminated() {
    long c = this.ctl;
    return ((c & 0x80000000L) != 0L && (short)(int)(c >>> 32L) + this.parallelism <= 0);
  }
  
  public boolean isTerminating() {
    long c = this.ctl;
    return ((c & 0x80000000L) != 0L && (short)(int)(c >>> 32L) + this.parallelism > 0);
  }
  
  public boolean isShutdown() {
    return (this.plock < 0);
  }
  
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    if (Thread.interrupted())
      throw new InterruptedException(); 
    if (this == common) {
      awaitQuiescence(timeout, unit);
      return false;
    } 
    long nanos = unit.toNanos(timeout);
    if (isTerminated())
      return true; 
    if (nanos <= 0L)
      return false; 
    long deadline = System.nanoTime() + nanos;
    synchronized (this) {
      while (true) {
        if (isTerminated())
          return true; 
        if (nanos <= 0L)
          return false; 
        long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
        wait((millis > 0L) ? millis : 1L);
        nanos = deadline - System.nanoTime();
      } 
    } 
  }
  
  public boolean awaitQuiescence(long timeout, TimeUnit unit) {
    long nanos = unit.toNanos(timeout);
    Thread thread = Thread.currentThread();
    ForkJoinWorkerThread wt;
    if (thread instanceof ForkJoinWorkerThread && (wt = (ForkJoinWorkerThread)thread).pool == this) {
      helpQuiescePool(wt.workQueue);
      return true;
    } 
    long startTime = System.nanoTime();
    int r = 0;
    boolean found = true;
    WorkQueue[] ws;
    int m;
    while (!isQuiescent() && (ws = this.workQueues) != null && (m = ws.length - 1) >= 0) {
      if (!found) {
        if (System.nanoTime() - startTime > nanos)
          return false; 
        Thread.yield();
      } 
      found = false;
      for (int j = m + 1 << 2; j >= 0; j--) {
        WorkQueue q;
        int b;
        if ((q = ws[r++ & m]) != null && (b = q.base) - q.top < 0) {
          found = true;
          ForkJoinTask<?> t;
          if ((t = q.pollAt(b)) != null)
            t.doExec(); 
          break;
        } 
      } 
    } 
    return true;
  }
  
  static void quiesceCommonPool() {
    common.awaitQuiescence(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
  }
  
  public static void managedBlock(ManagedBlocker blocker) throws InterruptedException {
    Thread t = Thread.currentThread();
    if (t instanceof ForkJoinWorkerThread) {
      ForkJoinPool p = ((ForkJoinWorkerThread)t).pool;
      while (!blocker.isReleasable()) {
        if (p.tryCompensate(p.ctl)) {
          try {
            do {
            
            } while (!blocker.isReleasable() && !blocker.block());
          } finally {
            p.incrementActiveCount();
          } 
          break;
        } 
      } 
    } else {
      do {
      
      } while (!blocker.isReleasable() && !blocker.block());
    } 
  }
  
  protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
    return new ForkJoinTask.AdaptedRunnable<T>(runnable, value);
  }
  
  protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    return new ForkJoinTask.AdaptedCallable<T>(callable);
  }
  
  static {
    try {
      U = getUnsafe();
      Class<?> k = ForkJoinPool.class;
      CTL = U.objectFieldOffset(k.getDeclaredField("ctl"));
      STEALCOUNT = U.objectFieldOffset(k.getDeclaredField("stealCount"));
      PLOCK = U.objectFieldOffset(k.getDeclaredField("plock"));
      INDEXSEED = U.objectFieldOffset(k.getDeclaredField("indexSeed"));
      Class<?> tk = Thread.class;
      PARKBLOCKER = U.objectFieldOffset(tk.getDeclaredField("parkBlocker"));
      Class<?> wk = WorkQueue.class;
      QBASE = U.objectFieldOffset(wk.getDeclaredField("base"));
      QLOCK = U.objectFieldOffset(wk.getDeclaredField("qlock"));
      Class<?> ak = ForkJoinTask[].class;
      ABASE = U.arrayBaseOffset(ak);
      int scale = U.arrayIndexScale(ak);
      if ((scale & scale - 1) != 0)
        throw new Error("data type scale not a power of two"); 
      ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
    } catch (Exception e) {
      throw new Error(e);
    } 
  }
  
  static final ThreadLocal<Submitter> submitters = new ThreadLocal<Submitter>();
  
  public static final ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory = new DefaultForkJoinWorkerThreadFactory();
  
  private static final RuntimePermission modifyThreadPermission = new RuntimePermission("modifyThread");
  
  static final ForkJoinPool common = AccessController.<ForkJoinPool>doPrivileged(new PrivilegedAction<ForkJoinPool>() {
        public ForkJoinPool run() {
          return ForkJoinPool.makeCommonPool();
        }
      });
  
  static final int commonParallelism;
  
  private static int poolNumberSequence;
  
  private static final long IDLE_TIMEOUT = 2000000000L;
  
  private static final long FAST_IDLE_TIMEOUT = 200000000L;
  
  private static final long TIMEOUT_SLOP = 2000000L;
  
  private static final int MAX_HELP = 64;
  
  private static final int SEED_INCREMENT = 1640531527;
  
  private static final int AC_SHIFT = 48;
  
  private static final int TC_SHIFT = 32;
  
  private static final int ST_SHIFT = 31;
  
  private static final int EC_SHIFT = 16;
  
  private static final int SMASK = 65535;
  
  private static final int MAX_CAP = 32767;
  
  private static final int EVENMASK = 65534;
  
  private static final int SQMASK = 126;
  
  private static final int SHORT_SIGN = 32768;
  
  private static final int INT_SIGN = -2147483648;
  
  private static final long STOP_BIT = 2147483648L;
  
  private static final long AC_MASK = -281474976710656L;
  
  private static final long TC_MASK = 281470681743360L;
  
  private static final long TC_UNIT = 4294967296L;
  
  private static final long AC_UNIT = 281474976710656L;
  
  private static final int UAC_SHIFT = 16;
  
  private static final int UTC_SHIFT = 0;
  
  private static final int UAC_MASK = -65536;
  
  private static final int UTC_MASK = 65535;
  
  private static final int UAC_UNIT = 65536;
  
  private static final int UTC_UNIT = 1;
  
  private static final int E_MASK = 2147483647;
  
  private static final int E_SEQ = 65536;
  
  private static final int SHUTDOWN = -2147483648;
  
  private static final int PL_LOCK = 2;
  
  private static final int PL_SIGNAL = 1;
  
  private static final int PL_SPINS = 256;
  
  static final int LIFO_QUEUE = 0;
  
  static final int FIFO_QUEUE = 1;
  
  static final int SHARED_QUEUE = -1;
  
  volatile long pad00;
  
  volatile long pad01;
  
  volatile long pad02;
  
  volatile long pad03;
  
  volatile long pad04;
  
  volatile long pad05;
  
  volatile long pad06;
  
  volatile long stealCount;
  
  volatile long ctl;
  
  volatile int plock;
  
  volatile int indexSeed;
  
  final short parallelism;
  
  final short mode;
  
  WorkQueue[] workQueues;
  
  final ForkJoinWorkerThreadFactory factory;
  
  final Thread.UncaughtExceptionHandler ueh;
  
  final String workerNamePrefix;
  
  volatile Object pad10;
  
  volatile Object pad11;
  
  volatile Object pad12;
  
  volatile Object pad13;
  
  volatile Object pad14;
  
  volatile Object pad15;
  
  volatile Object pad16;
  
  volatile Object pad17;
  
  volatile Object pad18;
  
  volatile Object pad19;
  
  volatile Object pad1a;
  
  volatile Object pad1b;
  
  private static final Unsafe U;
  
  private static final long CTL;
  
  private static final long PARKBLOCKER;
  
  private static final int ABASE;
  
  private static final int ASHIFT;
  
  private static final long STEALCOUNT;
  
  private static final long PLOCK;
  
  private static final long INDEXSEED;
  
  private static final long QBASE;
  
  private static final long QLOCK;
  
  static {
    int par = common.parallelism;
    commonParallelism = (par > 0) ? par : 1;
  }
  
  private static ForkJoinPool makeCommonPool() {
    int parallelism = -1;
    ForkJoinWorkerThreadFactory factory = defaultForkJoinWorkerThreadFactory;
    Thread.UncaughtExceptionHandler handler = null;
    try {
      String pp = System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism");
      String fp = System.getProperty("java.util.concurrent.ForkJoinPool.common.threadFactory");
      String hp = System.getProperty("java.util.concurrent.ForkJoinPool.common.exceptionHandler");
      if (pp != null)
        parallelism = Integer.parseInt(pp); 
      if (fp != null)
        factory = (ForkJoinWorkerThreadFactory)ClassLoader.getSystemClassLoader().loadClass(fp).newInstance(); 
      if (hp != null)
        handler = (Thread.UncaughtExceptionHandler)ClassLoader.getSystemClassLoader().loadClass(hp).newInstance(); 
    } catch (Exception ignore) {}
    if (parallelism < 0 && (parallelism = Runtime.getRuntime().availableProcessors() - 1) < 0)
      parallelism = 0; 
    if (parallelism > 32767)
      parallelism = 32767; 
    return new ForkJoinPool(parallelism, factory, handler, 0, "ForkJoinPool.commonPool-worker-");
  }
  
  private static Unsafe getUnsafe() {
    try {
      return Unsafe.getUnsafe();
    } catch (SecurityException tryReflectionInstead) {
      try {
        return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
              public Unsafe run() throws Exception {
                Class<Unsafe> k = Unsafe.class;
                for (Field f : k.getDeclaredFields()) {
                  f.setAccessible(true);
                  Object x = f.get(null);
                  if (k.isInstance(x))
                    return k.cast(x); 
                } 
                throw new NoSuchFieldError("the Unsafe");
              }
            });
      } catch (PrivilegedActionException e) {
        throw new RuntimeException("Could not initialize intrinsics", e.getCause());
      } 
    } 
  }
  
  public static interface ManagedBlocker {
    boolean block() throws InterruptedException;
    
    boolean isReleasable();
  }
  
  public static interface ForkJoinWorkerThreadFactory {
    ForkJoinWorkerThread newThread(ForkJoinPool param1ForkJoinPool);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\chmv8\ForkJoinPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */