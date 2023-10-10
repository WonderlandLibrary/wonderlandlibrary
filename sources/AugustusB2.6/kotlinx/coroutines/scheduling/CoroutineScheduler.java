// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import kotlin.Unit;
import kotlin.ranges.RangesKt___RangesKt;
import java.util.concurrent.TimeUnit;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import com.badlogic.gdx.graphics.Pixmap;
import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.RejectedExecutionException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.Semaphore;
import java.util.concurrent.Executor;
import java.io.Closeable;

public final class CoroutineScheduler implements Closeable, Executor
{
    private final GlobalQueue globalQueue;
    private final Semaphore cpuPermits;
    private volatile long parkedWorkersStack;
    private static final AtomicLongFieldUpdater parkedWorkersStack$FU;
    private final Worker[] workers;
    volatile long controlState;
    static final AtomicLongFieldUpdater controlState$FU;
    private final Random random;
    private volatile int _isTerminated;
    private static final AtomicIntegerFieldUpdater _isTerminated$FU;
    private final int corePoolSize;
    private final int maxPoolSize;
    private final long idleWorkerKeepAliveNs;
    private final String schedulerName;
    private static final int MAX_SPINS;
    private static final int MAX_YIELDS;
    private static final int MAX_PARK_TIME_NS;
    private static final int MIN_PARK_TIME_NS;
    private static final CoroutineContext.Element.DefaultImpls NOT_IN_STACK$4fdbb1f;
    
    private final Worker parkedWorkersStackPop() {
        final CoroutineScheduler $this$loop$iv = this;
        while (true) {
            final long top;
            final int index = (int)((top = $this$loop$iv.parkedWorkersStack) & 0x1FFFFFL);
            final Worker worker2 = this.workers[index];
            if (worker2 == null) {
                return null;
            }
            final Worker worker = worker2;
            final long updVersion = top + 2097152L & 0xFFFFFFFFFFE00000L;
            final int updIndex;
            if ((updIndex = parkedWorkersStackNextIndex(worker)) >= 0 && CoroutineScheduler.parkedWorkersStack$FU.compareAndSet(this, top, updVersion | (long)updIndex)) {
                worker.setNextParkedWorker(CoroutineScheduler.NOT_IN_STACK$4fdbb1f);
                return worker;
            }
        }
    }
    
    private static int parkedWorkersStackNextIndex(Worker worker) {
        for (Object o = worker.getNextParkedWorker(); o != CoroutineScheduler.NOT_IN_STACK$4fdbb1f; o = worker.getNextParkedWorker()) {
            if (o == null) {
                return 0;
            }
            final int updIndex;
            if ((updIndex = (worker = (Worker)o).getIndexInArray()) != 0) {
                return updIndex;
            }
        }
        return -1;
    }
    
    private final boolean isTerminated() {
        return this._isTerminated != 0;
    }
    
    @Override
    public final void execute(final Runnable command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        dispatch$default$30bf587f(this, command, null, false, 6);
    }
    
    @Override
    public final void close() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc2_w          10000
        //     4: lstore_2       
        //     5: dup            
        //     6: astore_1       
        //     7: getstatic       kotlinx/coroutines/scheduling/CoroutineScheduler._isTerminated$FU:Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
        //    10: swap           
        //    11: iconst_0       
        //    12: iconst_1       
        //    13: invokevirtual   java/util/concurrent/atomic/AtomicIntegerFieldUpdater.compareAndSet:(Ljava/lang/Object;II)Z
        //    16: ifeq            293
        //    19: aload_1        
        //    20: invokespecial   kotlinx/coroutines/scheduling/CoroutineScheduler.currentWorker:()Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;
        //    23: astore_2       
        //    24: aload_1        
        //    25: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.workers:[Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;
        //    28: dup            
        //    29: astore          4
        //    31: monitorenter   
        //    32: aload_1        
        //    33: dup            
        //    34: astore_3       
        //    35: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.controlState:J
        //    38: ldc2_w          2097151
        //    41: land           
        //    42: l2i            
        //    43: istore          6
        //    45: aload           4
        //    47: monitorexit    
        //    48: iload           6
        //    50: goto            61
        //    53: astore          6
        //    55: aload           4
        //    57: monitorexit    
        //    58: aload           6
        //    60: athrow         
        //    61: istore_3       
        //    62: iconst_1       
        //    63: istore          4
        //    65: iload_3        
        //    66: ifle            181
        //    69: aload_1        
        //    70: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.workers:[Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;
        //    73: iload           4
        //    75: aaload         
        //    76: dup            
        //    77: ifnonnull       83
        //    80: invokestatic    kotlin/jvm/internal/Intrinsics.throwNpe:()V
        //    83: dup            
        //    84: astore          5
        //    86: aload_2        
        //    87: if_acmpeq       169
        //    90: aload           5
        //    92: invokevirtual   kotlinx/coroutines/scheduling/CoroutineScheduler$Worker.isAlive:()Z
        //    95: ifeq            117
        //    98: aload           5
        //   100: checkcast       Ljava/lang/Thread;
        //   103: invokestatic    java/util/concurrent/locks/LockSupport.unpark:(Ljava/lang/Thread;)V
        //   106: aload           5
        //   108: ldc2_w          10000
        //   111: invokevirtual   kotlinx/coroutines/scheduling/CoroutineScheduler$Worker.join:(J)V
        //   114: goto            90
        //   117: aload           5
        //   119: invokevirtual   kotlinx/coroutines/scheduling/CoroutineScheduler$Worker.getState:()Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;
        //   122: astore          6
        //   124: invokestatic    kotlinx/coroutines/DebugKt.getASSERTIONS_ENABLED:()Z
        //   127: ifeq            157
        //   130: aload           6
        //   132: getstatic       kotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState.TERMINATED:Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;
        //   135: if_acmpne       142
        //   138: iconst_1       
        //   139: goto            143
        //   142: iconst_0       
        //   143: ifne            157
        //   146: new             Ljava/lang/AssertionError;
        //   149: dup            
        //   150: invokespecial   java/lang/AssertionError.<init>:()V
        //   153: checkcast       Ljava/lang/Throwable;
        //   156: athrow         
        //   157: aload           5
        //   159: invokevirtual   kotlinx/coroutines/scheduling/CoroutineScheduler$Worker.getLocalQueue:()Lkotlinx/coroutines/scheduling/WorkQueue;
        //   162: aload_1        
        //   163: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.globalQueue:Lkotlinx/coroutines/scheduling/GlobalQueue;
        //   166: invokevirtual   kotlinx/coroutines/scheduling/WorkQueue.offloadAllWork$kotlinx_coroutines_core:(Lkotlinx/coroutines/scheduling/GlobalQueue;)V
        //   169: iload           4
        //   171: iload_3        
        //   172: if_icmpeq       181
        //   175: iinc            4, 1
        //   178: goto            69
        //   181: aload_1        
        //   182: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.globalQueue:Lkotlinx/coroutines/scheduling/GlobalQueue;
        //   185: invokevirtual   kotlinx/coroutines/scheduling/GlobalQueue.close:()V
        //   188: aload_2        
        //   189: dup            
        //   190: ifnull          200
        //   193: invokevirtual   kotlinx/coroutines/scheduling/CoroutineScheduler$Worker.findTask$kotlinx_coroutines_core:()Lkotlinx/coroutines/scheduling/Task;
        //   196: dup            
        //   197: ifnonnull       211
        //   200: pop            
        //   201: aload_1        
        //   202: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.globalQueue:Lkotlinx/coroutines/scheduling/GlobalQueue;
        //   205: invokevirtual   kotlinx/coroutines/scheduling/GlobalQueue.removeFirstOrNull:()Ljava/lang/Object;
        //   208: checkcast       Lkotlinx/coroutines/scheduling/Task;
        //   211: dup            
        //   212: ifnonnull       219
        //   215: pop            
        //   216: goto            228
        //   219: dup            
        //   220: astore          4
        //   222: invokestatic    kotlinx/coroutines/scheduling/CoroutineScheduler.runSafely:(Lkotlinx/coroutines/scheduling/Task;)V
        //   225: goto            188
        //   228: aload_2        
        //   229: dup            
        //   230: ifnull          243
        //   233: getstatic       kotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState.TERMINATED:Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;
        //   236: invokevirtual   kotlinx/coroutines/scheduling/CoroutineScheduler$Worker.tryReleaseCpu$kotlinx_coroutines_core:(Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;)Z
        //   239: pop            
        //   240: goto            244
        //   243: pop            
        //   244: invokestatic    kotlinx/coroutines/DebugKt.getASSERTIONS_ENABLED:()Z
        //   247: ifeq            283
        //   250: aload_1        
        //   251: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.cpuPermits:Ljava/util/concurrent/Semaphore;
        //   254: invokevirtual   java/util/concurrent/Semaphore.availablePermits:()I
        //   257: aload_1        
        //   258: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.corePoolSize:I
        //   261: if_icmpne       268
        //   264: iconst_1       
        //   265: goto            269
        //   268: iconst_0       
        //   269: ifne            283
        //   272: new             Ljava/lang/AssertionError;
        //   275: dup            
        //   276: invokespecial   java/lang/AssertionError.<init>:()V
        //   279: checkcast       Ljava/lang/Throwable;
        //   282: athrow         
        //   283: aload_1        
        //   284: lconst_0       
        //   285: putfield        kotlinx/coroutines/scheduling/CoroutineScheduler.parkedWorkersStack:J
        //   288: aload_1        
        //   289: lconst_0       
        //   290: putfield        kotlinx/coroutines/scheduling/CoroutineScheduler.controlState:J
        //   293: return         
        //    StackMapTable: 00 16 FF 00 35 00 05 00 00 00 00 07 00 27 00 01 07 00 32 FF 00 07 00 03 00 07 00 44 07 00 47 00 01 01 FD 00 07 01 01 4D 07 00 47 FC 00 06 07 00 47 1A 18 40 01 0D FA 00 0B F9 00 0B 06 4B 07 00 2D 4A 07 00 4B 47 07 00 4B 08 FF 00 0E 00 02 00 07 00 44 00 01 07 00 47 00 17 40 01 0D F9 00 09
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  32     45     53     61     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:535)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:414)
        //     at com.strobel.assembler.ir.Instruction.accept(Instruction.java:490)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:403)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final void dispatch(final Runnable block, final TaskContext taskContext, final boolean fair) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        final Task task$kotlinx_coroutines_core;
        final Task task = task$kotlinx_coroutines_core = createTask$kotlinx_coroutines_core(block, taskContext);
        final Worker currentWorker = this.currentWorker();
        boolean b = false;
        Label_0156: {
            if (currentWorker == null) {
                b = true;
            }
            else {
                final Worker worker = currentWorker;
                if (currentWorker.getState() == WorkerState.TERMINATED) {
                    b = true;
                }
                else {
                    int n = -1;
                    if (task$kotlinx_coroutines_core.getMode() == TaskMode.NON_BLOCKING) {
                        if (worker.isBlocking()) {
                            n = 0;
                        }
                        else if (!worker.tryAcquireCpuPermit()) {
                            b = true;
                            break Label_0156;
                        }
                    }
                    final boolean b2;
                    b = ((((b2 = (fair ? worker.getLocalQueue().addLast(task$kotlinx_coroutines_core, this.globalQueue) : worker.getLocalQueue().add(task$kotlinx_coroutines_core, this.globalQueue))) && worker.getLocalQueue().getBufferSize$kotlinx_coroutines_core() <= TasksKt.QUEUE_SIZE_OFFLOAD_THRESHOLD) ? n : false) != 0);
                }
            }
        }
        switch (b) {
            case -1: {}
            case 1: {
                if (!this.globalQueue.addLast(task)) {
                    throw new RejectedExecutionException(this.schedulerName + " was terminated");
                }
                this.requestCpuWorker();
            }
            default: {
                this.requestCpuWorker();
            }
        }
    }
    
    public static /* synthetic */ void dispatch$default$30bf587f(final CoroutineScheduler coroutineScheduler, final Runnable block, TaskContext taskContext, final boolean b, final int n) {
        taskContext = NonBlockingContext.INSTANCE;
        coroutineScheduler.dispatch(block, taskContext, false);
    }
    
    public static Task createTask$kotlinx_coroutines_core(final Runnable block, final TaskContext taskContext) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        final long nanoTime = TasksKt.schedulerTimeSource.nanoTime();
        if (block instanceof Task) {
            ((Task)block).submissionTime = nanoTime;
            ((Task)block).taskContext = taskContext;
            return (Task)block;
        }
        return new TaskImpl(block, nanoTime, taskContext);
    }
    
    private final void requestCpuWorker() {
        if (this.cpuPermits.availablePermits() == 0) {
            this.tryUnpark();
            return;
        }
        if (this.tryUnpark()) {
            return;
        }
        final long state;
        final int created = (int)((state = this.controlState) & 0x1FFFFFL);
        final int blocking = (int)((state & 0x3FFFFE00000L) >> 21);
        if (created - blocking < this.corePoolSize) {
            final int newCpuWorkers;
            if ((newCpuWorkers = this.createNewWorker()) == 1 && this.corePoolSize > 1) {
                this.createNewWorker();
            }
            if (newCpuWorkers > 0) {
                return;
            }
        }
        this.tryUnpark();
    }
    
    private final boolean tryUnpark() {
        boolean wasParking;
        Worker worker;
        do {
            final Worker parkedWorkersStackPop = this.parkedWorkersStackPop();
            if (parkedWorkersStackPop == null) {
                return false;
            }
            worker = parkedWorkersStackPop;
            parkedWorkersStackPop.idleResetBeforeUnpark();
            wasParking = worker.isParking();
            LockSupport.unpark(worker);
        } while (!wasParking || !worker.tryForbidTermination());
        return true;
    }
    
    private final int createNewWorker() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.workers:[Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;
        //     4: dup            
        //     5: astore_1        /* lock$iv */
        //     6: monitorenter   
        //     7: aload_0         /* this */
        //     8: invokespecial   kotlinx/coroutines/scheduling/CoroutineScheduler.isTerminated:()Z
        //    11: ifeq            18
        //    14: aload_1         /* lock$iv */
        //    15: monitorexit    
        //    16: iconst_m1      
        //    17: ireturn        
        //    18: aload_0         /* this */
        //    19: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.controlState:J
        //    22: dup2           
        //    23: lstore_3        /* state */
        //    24: ldc2_w          2097151
        //    27: land           
        //    28: l2i            
        //    29: istore          created
        //    31: lload_3         /* state */
        //    32: ldc2_w          4398044413952
        //    35: land           
        //    36: bipush          21
        //    38: lshr           
        //    39: l2i            
        //    40: istore_2        /* blocking */
        //    41: iload           created
        //    43: iload_2         /* blocking */
        //    44: isub           
        //    45: dup            
        //    46: istore_2        /* cpuWorkers */
        //    47: aload_0         /* this */
        //    48: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.corePoolSize:I
        //    51: if_icmplt       58
        //    54: aload_1         /* lock$iv */
        //    55: monitorexit    
        //    56: iconst_0       
        //    57: ireturn        
        //    58: iload           created
        //    60: aload_0         /* this */
        //    61: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.maxPoolSize:I
        //    64: if_icmpge       77
        //    67: aload_0         /* this */
        //    68: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.cpuPermits:Ljava/util/concurrent/Semaphore;
        //    71: invokevirtual   java/util/concurrent/Semaphore.availablePermits:()I
        //    74: ifne            81
        //    77: aload_1         /* lock$iv */
        //    78: monitorexit    
        //    79: iconst_0       
        //    80: ireturn        
        //    81: aload_0         /* this */
        //    82: dup            
        //    83: astore          4
        //    85: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.controlState:J
        //    88: ldc2_w          2097151
        //    91: land           
        //    92: l2i            
        //    93: iconst_1       
        //    94: iadd           
        //    95: dup            
        //    96: istore_3        /* newIndex */
        //    97: ifle            113
        //   100: aload_0         /* this */
        //   101: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.workers:[Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;
        //   104: iload_3         /* newIndex */
        //   105: aaload         
        //   106: ifnonnull       113
        //   109: iconst_1       
        //   110: goto            114
        //   113: iconst_0       
        //   114: dup            
        //   115: istore          4
        //   117: ifne            138
        //   120: ldc             "Failed requirement."
        //   122: astore_2        /* cpuWorkers */
        //   123: new             Ljava/lang/IllegalArgumentException;
        //   126: dup            
        //   127: aload_2         /* cpuWorkers */
        //   128: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   131: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   134: checkcast       Ljava/lang/Throwable;
        //   137: athrow         
        //   138: new             Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;
        //   141: dup            
        //   142: aload_0         /* this */
        //   143: iload_3         /* newIndex */
        //   144: invokespecial   kotlinx/coroutines/scheduling/CoroutineScheduler$Worker.<init>:(Lkotlinx/coroutines/scheduling/CoroutineScheduler;I)V
        //   147: dup            
        //   148: astore          5
        //   150: dup            
        //   151: astore          4
        //   153: invokevirtual   kotlinx/coroutines/scheduling/CoroutineScheduler$Worker.start:()V
        //   156: aload           5
        //   158: astore          worker
        //   160: aload_0         /* this */
        //   161: dup            
        //   162: astore          5
        //   164: getstatic       kotlinx/coroutines/scheduling/CoroutineScheduler.controlState$FU:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //   167: swap           
        //   168: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.incrementAndGet:(Ljava/lang/Object;)J
        //   171: dup2           
        //   172: lstore          13
        //   174: ldc2_w          2097151
        //   177: land           
        //   178: l2i            
        //   179: istore          5
        //   181: iload_3         /* newIndex */
        //   182: iload           5
        //   184: if_icmpne       191
        //   187: iconst_1       
        //   188: goto            192
        //   191: iconst_0       
        //   192: dup            
        //   193: istore          5
        //   195: ifne            218
        //   198: ldc             "Failed requirement."
        //   200: astore          worker
        //   202: new             Ljava/lang/IllegalArgumentException;
        //   205: dup            
        //   206: aload           worker
        //   208: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   211: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   214: checkcast       Ljava/lang/Throwable;
        //   217: athrow         
        //   218: aload_0         /* this */
        //   219: getfield        kotlinx/coroutines/scheduling/CoroutineScheduler.workers:[Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;
        //   222: iload_3         /* newIndex */
        //   223: aload           worker
        //   225: aastore        
        //   226: iload_2         /* cpuWorkers */
        //   227: iconst_1       
        //   228: iadd           
        //   229: istore_2       
        //   230: aload_1        
        //   231: monitorexit    
        //   232: iload_2        
        //   233: ireturn        
        //   234: astore_2       
        //   235: aload_1         /* lock$iv */
        //   236: monitorexit    
        //   237: aload_2        
        //   238: athrow         
        //    StackMapTable: 00 0B FC 00 12 07 00 27 FF 00 27 00 06 07 00 44 07 00 27 01 00 00 01 00 00 FF 00 12 00 02 00 07 00 27 00 00 FF 00 03 00 03 07 00 44 07 00 27 01 00 00 FC 00 1F 01 40 01 17 FC 00 34 07 00 47 40 01 19 FF 00 0F 00 02 00 07 00 27 00 01 07 00 32
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  7      14     234    239    Any
        //  18     54     234    239    Any
        //  58     77     234    239    Any
        //  81     230    234    239    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final Worker currentWorker() {
        Thread currentThread;
        if (!((currentThread = Thread.currentThread()) instanceof Worker)) {
            currentThread = null;
        }
        final Worker worker = (Worker)currentThread;
        Worker worker3;
        if (worker != null) {
            final Worker worker2 = worker;
            if (!Intrinsics.areEqual(worker.this$0, this)) {
                return null;
            }
            worker3 = worker2;
        }
        else {
            worker3 = null;
        }
        return worker3;
    }
    
    @Override
    public final String toString() {
        int parkedWorkers = 0;
        int blockingWorkers = 0;
        int cpuWorkers = 0;
        int retired = 0;
        int terminated = 0;
        final ArrayList queueSizes = new ArrayList();
        Worker[] workers;
        for (int length = (workers = this.workers).length, i = 0; i < length; ++i) {
            final Worker worker;
            if ((worker = workers[i]) != null) {
                int queueSize = worker.getLocalQueue().size$kotlinx_coroutines_core();
                switch (CoroutineScheduler$WhenMappings.$EnumSwitchMapping$0[worker.getState().ordinal()]) {
                    case 1: {
                        ++parkedWorkers;
                        break;
                    }
                    case 2: {
                        ++blockingWorkers;
                        final ArrayList list = queueSizes;
                        queueSize = (int)(String.valueOf(queueSize) + "b");
                        list.add(queueSize);
                        break;
                    }
                    case 3: {
                        ++cpuWorkers;
                        final ArrayList list2 = queueSizes;
                        queueSize = (int)(String.valueOf(queueSize) + "c");
                        list2.add(queueSize);
                        break;
                    }
                    case 4: {
                        ++retired;
                        if (queueSize > 0) {
                            queueSizes.add(String.valueOf(queueSize) + "r");
                            break;
                        }
                        break;
                    }
                    case 5: {
                        ++terminated;
                        break;
                    }
                }
            }
        }
        final long state = this.controlState;
        final long state$internal;
        return this.schedulerName + '@' + Pixmap.getHexAddress(this) + '[' + "Pool Size {core = " + this.corePoolSize + ", max = " + this.maxPoolSize + "}, Worker States {" + "CPU = " + cpuWorkers + ", blocking = " + blockingWorkers + ", parked = " + parkedWorkers + ", retired = " + retired + ", terminated = " + terminated + "}, running workers queues = " + queueSizes + ", global queue size = " + ((int)((state$internal & 0xFFFFFFFC0000000L) >> 30) - (int)((state$internal = ((LockFreeTaskQueueCore)this.globalQueue._cur$internal)._state$internal) & 0x3FFFFFFFL) & 0x3FFFFFFF) + ", Control State Workers {" + "created = " + (int)(state & 0x1FFFFFL) + ", blocking = " + (int)((state & 0x3FFFFE00000L) >> 21) + '}' + "]";
    }
    
    private static void runSafely(final Task task) {
        try {
            task.run();
        }
        catch (Throwable e) {
            final Thread thread;
            final Thread value = thread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(value, "thread");
            value.getUncaughtExceptionHandler().uncaughtException(thread, e);
        }
    }
    
    public CoroutineScheduler(int corePoolSize, final int maxPoolSize, final long idleWorkerKeepAliveNs, final String schedulerName) {
        Intrinsics.checkParameterIsNotNull(schedulerName, "schedulerName");
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.idleWorkerKeepAliveNs = idleWorkerKeepAliveNs;
        this.schedulerName = schedulerName;
        if ((corePoolSize = ((this.corePoolSize > 0) ? 1 : 0)) == 0) {
            throw new IllegalArgumentException(("Core pool size " + this.corePoolSize + " should be at least 1").toString());
        }
        if ((corePoolSize = ((this.maxPoolSize >= this.corePoolSize) ? 1 : 0)) == 0) {
            throw new IllegalArgumentException(("Max pool size " + this.maxPoolSize + " should be greater than or equals to core pool size " + this.corePoolSize).toString());
        }
        if ((corePoolSize = ((this.maxPoolSize <= 2097150) ? 1 : 0)) == 0) {
            throw new IllegalArgumentException(("Max pool size " + this.maxPoolSize + " should not exceed maximal supported number of threads 2097150").toString());
        }
        if ((corePoolSize = ((this.idleWorkerKeepAliveNs > 0L) ? 1 : 0)) == 0) {
            throw new IllegalArgumentException(("Idle worker keep alive time " + this.idleWorkerKeepAliveNs + " must be positive").toString());
        }
        this.globalQueue = new GlobalQueue();
        this.cpuPermits = new Semaphore(this.corePoolSize, false);
        this.parkedWorkersStack = 0L;
        this.workers = new Worker[this.maxPoolSize + 1];
        this.controlState = 0L;
        this.random = new Random();
        this._isTerminated = 0;
    }
    
    public static final /* synthetic */ String access$getSchedulerName$p(final CoroutineScheduler $this) {
        return $this.schedulerName;
    }
    
    public static final /* synthetic */ Semaphore access$getCpuPermits$p(final CoroutineScheduler $this) {
        return $this.cpuPermits;
    }
    
    public static final /* synthetic */ void access$runSafely(final CoroutineScheduler $this, final Task task) {
        runSafely(task);
    }
    
    public static final /* synthetic */ int access$getMAX_YIELDS$cp() {
        return CoroutineScheduler.MAX_YIELDS;
    }
    
    public static final /* synthetic */ int access$getMAX_SPINS$cp() {
        return CoroutineScheduler.MAX_SPINS;
    }
    
    public static final /* synthetic */ int access$getMAX_PARK_TIME_NS$cp() {
        return CoroutineScheduler.MAX_PARK_TIME_NS;
    }
    
    public static final /* synthetic */ long access$getIdleWorkerKeepAliveNs$p(final CoroutineScheduler $this) {
        return $this.idleWorkerKeepAliveNs;
    }
    
    public static final /* synthetic */ void access$parkedWorkersStackPush(CoroutineScheduler $this, Worker worker) {
        worker = worker;
        $this = $this;
        if (worker.getNextParkedWorker() == CoroutineScheduler.NOT_IN_STACK$4fdbb1f) {
            final CoroutineScheduler coroutineScheduler = $this;
            long parkedWorkersStack;
            long n2;
            int indexInArray;
            do {
                final int n = (int)((parkedWorkersStack = coroutineScheduler.parkedWorkersStack) & 0x1FFFFFL);
                n2 = (parkedWorkersStack + 2097152L & 0xFFFFFFFFFFE00000L);
                indexInArray = worker.getIndexInArray();
                if (DebugKt.getASSERTIONS_ENABLED() && indexInArray == 0) {
                    throw new AssertionError();
                }
                worker.setNextParkedWorker($this.workers[n]);
            } while (!CoroutineScheduler.parkedWorkersStack$FU.compareAndSet($this, parkedWorkersStack, n2 | (long)indexInArray));
        }
    }
    
    public static final /* synthetic */ Worker[] access$getWorkers$p(final CoroutineScheduler $this) {
        return $this.workers;
    }
    
    public static final /* synthetic */ int access$getCreatedWorkers$p(CoroutineScheduler $this) {
        $this = $this;
        return (int)($this.controlState & 0x1FFFFFL);
    }
    
    public static final /* synthetic */ int access$getCorePoolSize$p(final CoroutineScheduler $this) {
        return $this.corePoolSize;
    }
    
    public static final /* synthetic */ void access$parkedWorkersStackTopUpdate(CoroutineScheduler $this, Worker worker, int oldIndex, int newIndex) {
        newIndex = newIndex;
        oldIndex = oldIndex;
        worker = worker;
        $this = $this;
        long parkedWorkersStack;
        long n2;
        int n3;
        int n4;
        do {
            final int n = (int)((parkedWorkersStack = $this.parkedWorkersStack) & 0x1FFFFFL);
            n2 = (parkedWorkersStack + 2097152L & 0xFFFFFFFFFFE00000L);
            int parkedWorkersStackNextIndex;
            n3 = ((n == oldIndex) ? ((newIndex == 0) ? (parkedWorkersStackNextIndex = parkedWorkersStackNextIndex(worker)) : (parkedWorkersStackNextIndex = newIndex)) : (parkedWorkersStackNextIndex = n));
            n4 = parkedWorkersStackNextIndex;
        } while (n3 < 0 || !CoroutineScheduler.parkedWorkersStack$FU.compareAndSet($this, parkedWorkersStack, n2 | (long)n4));
    }
    
    public static final /* synthetic */ GlobalQueue access$getGlobalQueue$p(final CoroutineScheduler $this) {
        return $this.globalQueue;
    }
    
    public static final /* synthetic */ int access$getMIN_PARK_TIME_NS$cp() {
        return CoroutineScheduler.MIN_PARK_TIME_NS;
    }
    
    public static final /* synthetic */ CoroutineContext.Element.DefaultImpls access$getNOT_IN_STACK$cp$55cb9d62() {
        return CoroutineScheduler.NOT_IN_STACK$4fdbb1f;
    }
    
    public static final /* synthetic */ Random access$getRandom$p(final CoroutineScheduler $this) {
        return $this.random;
    }
    
    static {
        new Companion((byte)0);
        MAX_YIELDS = (MAX_SPINS = LockFreeTaskQueueCore.Companion.systemProp$default("kotlinx.coroutines.scheduler.spins", 1000, 1, 0, 8, null)) + LockFreeTaskQueueCore.Companion.systemProp$default("kotlinx.coroutines.scheduler.yields", 0, 0, 0, 8, null);
        MAX_PARK_TIME_NS = (int)TimeUnit.SECONDS.toNanos(1L);
        MIN_PARK_TIME_NS = (int)RangesKt___RangesKt.coerceAtMost(RangesKt___RangesKt.coerceAtLeast(TasksKt.WORK_STEALING_TIME_RESOLUTION_NS / 4L, 10L), CoroutineScheduler.MAX_PARK_TIME_NS);
        NOT_IN_STACK$4fdbb1f = new CoroutineContext.Element.DefaultImpls("NOT_IN_STACK");
        parkedWorkersStack$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "parkedWorkersStack");
        controlState$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "controlState");
        _isTerminated$FU = AtomicIntegerFieldUpdater.newUpdater(CoroutineScheduler.class, "_isTerminated");
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
    
    public final class Worker extends Thread
    {
        private volatile int indexInArray;
        private final WorkQueue localQueue;
        private volatile WorkerState state;
        private volatile int terminationState;
        private static final AtomicIntegerFieldUpdater terminationState$FU;
        private long terminationDeadline;
        private volatile Object nextParkedWorker;
        private long lastExhaustionTime;
        private volatile int spins;
        private int parkTimeNs;
        private int rngState;
        private int lastStealIndex;
        final /* synthetic */ CoroutineScheduler this$0;
        
        public final int getIndexInArray() {
            return this.indexInArray;
        }
        
        private void setIndexInArray(final int index) {
            this.setName(CoroutineScheduler.access$getSchedulerName$p(CoroutineScheduler.this) + "-worker-" + ((index == 0) ? "TERMINATED" : String.valueOf(index)));
            this.indexInArray = index;
        }
        
        public final WorkQueue getLocalQueue() {
            return this.localQueue;
        }
        
        public final WorkerState getState() {
            return this.state;
        }
        
        public final boolean isParking() {
            return this.state == WorkerState.PARKING;
        }
        
        public final boolean isBlocking() {
            return this.state == WorkerState.BLOCKING;
        }
        
        public final Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }
        
        public final void setNextParkedWorker(final Object <set-?>) {
            this.nextParkedWorker = <set-?>;
        }
        
        public final boolean tryForbidTermination() {
            final int state;
            if ((state = this.terminationState) == 1) {
                return false;
            }
            if (state == -1) {
                return false;
            }
            if (state == 0) {
                return Worker.terminationState$FU.compareAndSet(this, 0, -1);
            }
            throw new IllegalStateException(("Invalid terminationState = " + state).toString());
        }
        
        public final boolean tryAcquireCpuPermit() {
            if (this.state == WorkerState.CPU_ACQUIRED) {
                return true;
            }
            if (CoroutineScheduler.access$getCpuPermits$p(CoroutineScheduler.this).tryAcquire()) {
                this.state = WorkerState.CPU_ACQUIRED;
                return true;
            }
            return false;
        }
        
        public final boolean tryReleaseCpu$kotlinx_coroutines_core(final WorkerState newState) {
            Intrinsics.checkParameterIsNotNull(newState, "newState");
            final WorkerState previousState;
            final boolean hadCpu;
            if (hadCpu = ((previousState = this.state) == WorkerState.CPU_ACQUIRED)) {
                CoroutineScheduler.access$getCpuPermits$p(CoroutineScheduler.this).release();
            }
            if (previousState != newState) {
                this.state = newState;
            }
            return hadCpu;
        }
        
        @Override
        public final void run() {
            boolean wasIdle = false;
            while (!CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                final Task task;
                if ((task = this.findTask$kotlinx_coroutines_core()) == null) {
                    if (this.state == WorkerState.CPU_ACQUIRED) {
                        final int spins;
                        if ((spins = this.spins) <= CoroutineScheduler.access$getMAX_YIELDS$cp()) {
                            this.spins = spins + 1;
                            if (spins >= CoroutineScheduler.access$getMAX_SPINS$cp()) {
                                Thread.yield();
                            }
                        }
                        else {
                            if (this.parkTimeNs < CoroutineScheduler.access$getMAX_PARK_TIME_NS$cp()) {
                                this.parkTimeNs = RangesKt___RangesKt.coerceAtMost(this.parkTimeNs * 3 >>> 1, CoroutineScheduler.access$getMAX_PARK_TIME_NS$cp());
                            }
                            this.tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
                            this.doPark(this.parkTimeNs);
                        }
                    }
                    else {
                        this.tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
                        if (this.blockingQuiescence()) {
                            this.terminationState = 0;
                            if (this.terminationDeadline == 0L) {
                                this.terminationDeadline = System.nanoTime() + CoroutineScheduler.access$getIdleWorkerKeepAliveNs$p(CoroutineScheduler.this);
                            }
                            if (this.doPark(CoroutineScheduler.access$getIdleWorkerKeepAliveNs$p(CoroutineScheduler.this)) && System.nanoTime() - this.terminationDeadline >= 0L) {
                                this.terminationDeadline = 0L;
                                this.tryTerminateWorker();
                            }
                        }
                    }
                    wasIdle = true;
                }
                else {
                    final TaskMode taskMode = task.getMode();
                    if (wasIdle) {
                        final TaskMode taskMode2 = taskMode;
                        this.terminationDeadline = 0L;
                        this.lastStealIndex = 0;
                        if (this.state == WorkerState.PARKING) {
                            if (DebugKt.getASSERTIONS_ENABLED() && taskMode2 != TaskMode.PROBABLY_BLOCKING) {
                                throw new AssertionError();
                            }
                            this.state = WorkerState.BLOCKING;
                            this.parkTimeNs = CoroutineScheduler.access$getMIN_PARK_TIME_NS$cp();
                        }
                        this.spins = 0;
                        wasIdle = false;
                    }
                    final TaskMode taskMode3 = taskMode;
                    final long submissionTime = task.submissionTime;
                    if (taskMode3 != TaskMode.NON_BLOCKING) {
                        CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, 2097152L);
                        if (this.tryReleaseCpu$kotlinx_coroutines_core(WorkerState.BLOCKING)) {
                            CoroutineScheduler.this.requestCpuWorker();
                        }
                    }
                    else {
                        final long nanoTime;
                        if (CoroutineScheduler.access$getCpuPermits$p(CoroutineScheduler.this).availablePermits() != 0 && (nanoTime = TasksKt.schedulerTimeSource.nanoTime()) - submissionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS && nanoTime - this.lastExhaustionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS * 5L) {
                            this.lastExhaustionTime = nanoTime;
                            CoroutineScheduler.this.requestCpuWorker();
                        }
                    }
                    CoroutineScheduler.access$runSafely(CoroutineScheduler.this, task);
                    if (taskMode == TaskMode.NON_BLOCKING) {
                        continue;
                    }
                    CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, -2097152L);
                    final WorkerState state;
                    if ((state = this.state) == WorkerState.TERMINATED) {
                        continue;
                    }
                    if (DebugKt.getASSERTIONS_ENABLED() && state != WorkerState.BLOCKING) {
                        throw new AssertionError();
                    }
                    this.state = WorkerState.RETIRING;
                }
            }
            this.tryReleaseCpu$kotlinx_coroutines_core(WorkerState.TERMINATED);
        }
        
        private int nextInt$kotlinx_coroutines_core(final int upperBound) {
            this.rngState ^= this.rngState << 13;
            this.rngState ^= this.rngState >> 17;
            this.rngState ^= this.rngState << 5;
            final int mask;
            if (((mask = upperBound - 1) & upperBound) == 0x0) {
                return this.rngState & mask;
            }
            return (this.rngState & Integer.MAX_VALUE) % upperBound;
        }
        
        private final boolean doPark(final long nanos) {
            CoroutineScheduler.access$parkedWorkersStackPush(CoroutineScheduler.this, this);
            if (!this.blockingQuiescence()) {
                return false;
            }
            LockSupport.parkNanos(nanos);
            return true;
        }
        
        private final void tryTerminateWorker() {
            synchronized (CoroutineScheduler.access$getWorkers$p(CoroutineScheduler.this)) {
                if (CoroutineScheduler.this.isTerminated()) {
                    return;
                }
                if (CoroutineScheduler.access$getCreatedWorkers$p(CoroutineScheduler.this) <= CoroutineScheduler.access$getCorePoolSize$p(CoroutineScheduler.this)) {
                    return;
                }
                if (!this.blockingQuiescence()) {
                    return;
                }
                if (!Worker.terminationState$FU.compareAndSet(this, 0, 1)) {
                    return;
                }
                final int oldIndex = this.indexInArray;
                this.setIndexInArray(0);
                CoroutineScheduler.access$parkedWorkersStackTopUpdate(CoroutineScheduler.this, this, oldIndex, 0);
                final int lastIndex;
                if ((lastIndex = (int)(CoroutineScheduler.controlState$FU.getAndDecrement(CoroutineScheduler.this) & 0x1FFFFFL)) != oldIndex) {
                    final Worker worker = CoroutineScheduler.access$getWorkers$p(CoroutineScheduler.this)[lastIndex];
                    if (worker == null) {
                        Intrinsics.throwNpe();
                    }
                    final Worker lastWorker = worker;
                    (CoroutineScheduler.access$getWorkers$p(CoroutineScheduler.this)[oldIndex] = lastWorker).setIndexInArray(oldIndex);
                    CoroutineScheduler.access$parkedWorkersStackTopUpdate(CoroutineScheduler.this, lastWorker, lastIndex, oldIndex);
                }
                CoroutineScheduler.access$getWorkers$p(CoroutineScheduler.this)[lastIndex] = null;
                final Unit instance = Unit.INSTANCE;
            }
            this.state = WorkerState.TERMINATED;
        }
        
        private final boolean blockingQuiescence() {
            final Task removeFirstWithModeOrNull = CoroutineScheduler.access$getGlobalQueue$p(CoroutineScheduler.this).removeFirstWithModeOrNull(TaskMode.PROBABLY_BLOCKING);
            if (removeFirstWithModeOrNull != null) {
                final Task it = removeFirstWithModeOrNull;
                this.localQueue.add(it, CoroutineScheduler.access$getGlobalQueue$p(CoroutineScheduler.this));
                return false;
            }
            return true;
        }
        
        public final void idleResetBeforeUnpark() {
            this.parkTimeNs = CoroutineScheduler.access$getMIN_PARK_TIME_NS$cp();
            this.spins = 0;
        }
        
        public final Task findTask$kotlinx_coroutines_core() {
            if (this.tryAcquireCpuPermit()) {
                return this.findTaskWithCpuPermit();
            }
            Task task;
            if ((task = this.localQueue.poll()) == null) {
                task = CoroutineScheduler.access$getGlobalQueue$p(CoroutineScheduler.this).removeFirstWithModeOrNull(TaskMode.PROBABLY_BLOCKING);
            }
            return task;
        }
        
        private final Task findTaskWithCpuPermit() {
            boolean globalFirst;
            if (globalFirst = (this.nextInt$kotlinx_coroutines_core(2 * CoroutineScheduler.access$getCorePoolSize$p(CoroutineScheduler.this)) == 0)) {
                final Task removeFirstWithModeOrNull = CoroutineScheduler.access$getGlobalQueue$p(CoroutineScheduler.this).removeFirstWithModeOrNull(TaskMode.NON_BLOCKING);
                if (removeFirstWithModeOrNull != null) {
                    globalFirst = (boolean)removeFirstWithModeOrNull;
                    globalFirst = (boolean)removeFirstWithModeOrNull;
                    return removeFirstWithModeOrNull;
                }
            }
            final Task poll = this.localQueue.poll();
            if (poll != null) {
                globalFirst = (boolean)poll;
                globalFirst = (boolean)poll;
                return poll;
            }
            if (!globalFirst) {
                final Task task = CoroutineScheduler.access$getGlobalQueue$p(CoroutineScheduler.this).removeFirstOrNull();
                if (task != null) {
                    return task;
                }
            }
            return this.trySteal();
        }
        
        private final Task trySteal() {
            final int created;
            if ((created = CoroutineScheduler.access$getCreatedWorkers$p(CoroutineScheduler.this)) < 2) {
                return null;
            }
            int stealIndex;
            if ((stealIndex = this.lastStealIndex) == 0) {
                stealIndex = this.nextInt$kotlinx_coroutines_core(created);
            }
            if (++stealIndex > created) {
                stealIndex = 1;
            }
            this.lastStealIndex = stealIndex;
            final Worker worker;
            if ((worker = CoroutineScheduler.access$getWorkers$p(CoroutineScheduler.this)[stealIndex]) != null && worker != this && this.localQueue.trySteal(worker.localQueue, CoroutineScheduler.access$getGlobalQueue$p(CoroutineScheduler.this))) {
                return this.localQueue.poll();
            }
            return null;
        }
        
        private Worker() {
            this.setDaemon(true);
            this.localQueue = new WorkQueue();
            this.state = WorkerState.RETIRING;
            this.terminationState = 0;
            this.nextParkedWorker = CoroutineScheduler.access$getNOT_IN_STACK$cp$55cb9d62();
            this.parkTimeNs = CoroutineScheduler.access$getMIN_PARK_TIME_NS$cp();
            this.rngState = CoroutineScheduler.access$getRandom$p(CoroutineScheduler.this).nextInt();
        }
        
        public Worker(final CoroutineScheduler $outer, final int index) {
            this();
            this.setIndexInArray(index);
        }
        
        static {
            terminationState$FU = AtomicIntegerFieldUpdater.newUpdater(Worker.class, "terminationState");
        }
    }
    
    public enum WorkerState
    {
        CPU_ACQUIRED, 
        BLOCKING, 
        PARKING, 
        RETIRING, 
        TERMINATED;
    }
}
