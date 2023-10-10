// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import kotlin.ranges.RangesKt___RangesKt;
import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class WorkQueue
{
    private final AtomicReferenceArray<Task> buffer;
    private volatile Object lastScheduledTask;
    private static final AtomicReferenceFieldUpdater lastScheduledTask$FU;
    volatile int producerIndex;
    private static AtomicIntegerFieldUpdater producerIndex$FU;
    volatile int consumerIndex;
    private static AtomicIntegerFieldUpdater consumerIndex$FU;
    
    public final int getBufferSize$kotlinx_coroutines_core() {
        return this.producerIndex - this.consumerIndex;
    }
    
    public final Task poll() {
        final Task task = WorkQueue.lastScheduledTask$FU.getAndSet(this, null);
        if (task == null) {
            final WorkQueue this_$iv = this;
            int tailLocal$iv;
            while ((tailLocal$iv = this_$iv.consumerIndex) - this_$iv.producerIndex != 0) {
                final int index$iv = tailLocal$iv & 0x7F;
                if (this_$iv.buffer.get(index$iv) == null) {
                    continue;
                }
                final WorkQueue workQueue = this_$iv;
                final AtomicIntegerFieldUpdater consumerIndex$FU = WorkQueue.consumerIndex$FU;
                final int n = tailLocal$iv;
                if (consumerIndex$FU.compareAndSet(workQueue, n, n + 1)) {
                    return this_$iv.buffer.getAndSet(index$iv, null);
                }
            }
            return null;
        }
        return task;
    }
    
    public final boolean add(Task task, final GlobalQueue globalQueue) {
        Intrinsics.checkParameterIsNotNull(task, "task");
        Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
        final Task task2 = WorkQueue.lastScheduledTask$FU.getAndSet(this, task);
        if (task2 == null) {
            return true;
        }
        task = task2;
        return this.addLast(task, globalQueue);
    }
    
    public final boolean addLast(final Task task, final GlobalQueue globalQueue) {
        Intrinsics.checkParameterIsNotNull(task, "task");
        Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
        boolean noOffloadingHappened = true;
        while (!this.tryAddLast(task)) {
            this.offloadWork(globalQueue);
            noOffloadingHappened = false;
        }
        return noOffloadingHappened;
    }
    
    public final boolean trySteal(WorkQueue victim, GlobalQueue globalQueue) {
        Intrinsics.checkParameterIsNotNull(victim, "victim");
        Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
        long time = TasksKt.schedulerTimeSource.nanoTime();
        final int bufferSize;
        if ((bufferSize = victim.getBufferSize$kotlinx_coroutines_core()) == 0) {
            final long n = time;
            final Object o = victim;
            time = (long)globalQueue;
            globalQueue = (GlobalQueue)o;
            final long n2 = n;
            victim = this;
            final Task task3 = (Task)((WorkQueue)globalQueue).lastScheduledTask;
            if (task3 != null) {
                final Task task4 = task3;
                if (n2 - task4.submissionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS && WorkQueue.lastScheduledTask$FU.compareAndSet(globalQueue, task4, null)) {
                    victim.add(task4, (GlobalQueue)time);
                    return true;
                }
            }
            return false;
        }
        boolean wasStolen = false;
        final int coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(bufferSize / 2, 1);
        int i = 0;
        final int n3 = coerceAtLeast;
    Label_0259_Outer:
        while (i < n3) {
            final WorkQueue this_$iv = victim;
            while (true) {
                int tailLocal$iv;
                while ((tailLocal$iv = this_$iv.consumerIndex) - this_$iv.producerIndex != 0) {
                    final int index$iv = tailLocal$iv & 0x7F;
                    final Task task5 = this_$iv.buffer.get(index$iv);
                    if (task5 == null) {
                        continue Label_0259_Outer;
                    }
                    final Task task = task5;
                    Task task7;
                    Task task6;
                    if (time - task.submissionTime < TasksKt.WORK_STEALING_TIME_RESOLUTION_NS && victim.getBufferSize$kotlinx_coroutines_core() <= TasksKt.QUEUE_SIZE_OFFLOAD_THRESHOLD) {
                        task6 = (task7 = null);
                    }
                    else {
                        final WorkQueue workQueue = this_$iv;
                        final AtomicIntegerFieldUpdater consumerIndex$FU = WorkQueue.consumerIndex$FU;
                        final int n4 = tailLocal$iv;
                        if (!consumerIndex$FU.compareAndSet(workQueue, n4, n4 + 1)) {
                            continue Label_0259_Outer;
                        }
                        task6 = (task7 = this_$iv.buffer.getAndSet(index$iv, null));
                    }
                    if (task7 == null) {
                        return wasStolen;
                    }
                    final Task task2 = task6;
                    wasStolen = true;
                    this.add(task2, globalQueue);
                    ++i;
                    continue Label_0259_Outer;
                }
                Task task7;
                Task task6 = task7 = null;
                continue;
            }
        }
        return wasStolen;
    }
    
    public final int size$kotlinx_coroutines_core() {
        if (this.lastScheduledTask != null) {
            return this.getBufferSize$kotlinx_coroutines_core() + 1;
        }
        return this.getBufferSize$kotlinx_coroutines_core();
    }
    
    private final void offloadWork(final GlobalQueue globalQueue) {
        final int coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(this.getBufferSize$kotlinx_coroutines_core() / 2, 1);
        int i = 0;
        final int n = coerceAtLeast;
    Label_0113_Outer:
        while (i < n) {
            final WorkQueue this_$iv = this;
            while (true) {
                int tailLocal$iv;
                while ((tailLocal$iv = this_$iv.consumerIndex) - this_$iv.producerIndex != 0) {
                    final int index$iv = tailLocal$iv & 0x7F;
                    if (this_$iv.buffer.get(index$iv) == null) {
                        continue Label_0113_Outer;
                    }
                    final WorkQueue workQueue = this_$iv;
                    final AtomicIntegerFieldUpdater consumerIndex$FU = WorkQueue.consumerIndex$FU;
                    final int n2 = tailLocal$iv;
                    if (!consumerIndex$FU.compareAndSet(workQueue, n2, n2 + 1)) {
                        continue Label_0113_Outer;
                    }
                    final Task task3;
                    final Task task2 = task3 = this_$iv.buffer.getAndSet(index$iv, null);
                    if (task3 == null) {
                        return;
                    }
                    final Task task = task2;
                    addToGlobalQueue(globalQueue, task);
                    ++i;
                    continue Label_0113_Outer;
                }
                Task task3;
                final Task task2 = task3 = null;
                continue;
            }
        }
    }
    
    private static void addToGlobalQueue(final GlobalQueue globalQueue, final Task task) {
        if (!globalQueue.addLast(task)) {
            throw new IllegalStateException("GlobalQueue could not be closed yet".toString());
        }
    }
    
    public final void offloadAllWork$kotlinx_coroutines_core(final GlobalQueue globalQueue) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "globalQueue"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* this */
        //     7: getstatic       kotlinx/coroutines/scheduling/WorkQueue.lastScheduledTask$FU:Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;
        //    10: swap           
        //    11: aconst_null    
        //    12: invokevirtual   java/util/concurrent/atomic/AtomicReferenceFieldUpdater.getAndSet:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    15: checkcast       Lkotlinx/coroutines/scheduling/Task;
        //    18: dup            
        //    19: ifnull          33
        //    22: dup            
        //    23: astore_2       
        //    24: astore_3        /* it */
        //    25: aload_1         /* globalQueue */
        //    26: aload_3         /* it */
        //    27: invokestatic    kotlinx/coroutines/scheduling/WorkQueue.addToGlobalQueue:(Lkotlinx/coroutines/scheduling/GlobalQueue;Lkotlinx/coroutines/scheduling/Task;)V
        //    30: goto            34
        //    33: pop            
        //    34: aload_1         /* globalQueue */
        //    35: aload_0         /* this */
        //    36: astore_2       
        //    37: astore          5
        //    39: aload_2         /* this_$iv */
        //    40: getfield        kotlinx/coroutines/scheduling/WorkQueue.consumerIndex:I
        //    43: dup            
        //    44: istore_3        /* tailLocal$iv */
        //    45: aload_2         /* this_$iv */
        //    46: getfield        kotlinx/coroutines/scheduling/WorkQueue.producerIndex:I
        //    49: isub           
        //    50: ifne            57
        //    53: aconst_null    
        //    54: goto            121
        //    57: iload_3         /* tailLocal$iv */
        //    58: bipush          127
        //    60: iand           
        //    61: istore          index$iv
        //    63: aload_2         /* this_$iv */
        //    64: dup            
        //    65: astore          6
        //    67: getfield        kotlinx/coroutines/scheduling/WorkQueue.buffer:Ljava/util/concurrent/atomic/AtomicReferenceArray;
        //    70: iload           index$iv
        //    72: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.get:(I)Ljava/lang/Object;
        //    75: checkcast       Lkotlinx/coroutines/scheduling/Task;
        //    78: ifnonnull       84
        //    81: goto            39
        //    84: aload_2         /* this_$iv */
        //    85: getstatic       kotlinx/coroutines/scheduling/WorkQueue.consumerIndex$FU:Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
        //    88: swap           
        //    89: iload_3         /* tailLocal$iv */
        //    90: dup            
        //    91: iconst_1       
        //    92: iadd           
        //    93: invokevirtual   java/util/concurrent/atomic/AtomicIntegerFieldUpdater.compareAndSet:(Ljava/lang/Object;II)Z
        //    96: ifeq            118
        //    99: aload_2         /* this_$iv */
        //   100: dup            
        //   101: astore          6
        //   103: getfield        kotlinx/coroutines/scheduling/WorkQueue.buffer:Ljava/util/concurrent/atomic/AtomicReferenceArray;
        //   106: iload           index$iv
        //   108: aconst_null    
        //   109: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.getAndSet:(ILjava/lang/Object;)Ljava/lang/Object;
        //   112: checkcast       Lkotlinx/coroutines/scheduling/Task;
        //   115: goto            121
        //   118: goto            39
        //   121: astore_2        /* this_$iv */
        //   122: aload           5
        //   124: aload_2        
        //   125: dup            
        //   126: ifnonnull       131
        //   129: pop            
        //   130: return         
        //   131: invokestatic    kotlinx/coroutines/scheduling/WorkQueue.addToGlobalQueue:(Lkotlinx/coroutines/scheduling/GlobalQueue;Lkotlinx/coroutines/scheduling/Task;)V
        //   134: goto            34
        //    StackMapTable: 00 08 61 07 00 11 00 FF 00 04 00 06 07 00 14 07 00 10 07 00 14 00 00 07 00 10 00 00 FF 00 11 00 06 07 00 14 07 00 10 07 00 14 01 00 07 00 10 00 00 FF 00 1A 00 06 07 00 14 07 00 10 07 00 14 01 01 07 00 10 00 00 FF 00 21 00 06 07 00 14 07 00 10 07 00 14 00 00 07 00 10 00 00 FF 00 02 00 06 07 00 14 07 00 10 00 00 00 07 00 10 00 01 07 00 11 FF 00 09 00 02 07 00 14 07 00 10 00 02 07 00 10 07 00 11
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
    
    private final boolean tryAddLast(final Task task) {
        if (this.getBufferSize$kotlinx_coroutines_core() == 127) {
            return false;
        }
        final int nextIndex = this.producerIndex & 0x7F;
        if (this.buffer.get(nextIndex) != null) {
            return false;
        }
        this.buffer.lazySet(nextIndex, task);
        WorkQueue.producerIndex$FU.incrementAndGet(this);
        return true;
    }
    
    public WorkQueue() {
        this.buffer = new AtomicReferenceArray<Task>(128);
        this.lastScheduledTask = null;
        this.producerIndex = 0;
        this.consumerIndex = 0;
    }
    
    static {
        lastScheduledTask$FU = AtomicReferenceFieldUpdater.newUpdater(WorkQueue.class, Object.class, "lastScheduledTask");
        WorkQueue.producerIndex$FU = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "producerIndex");
        WorkQueue.consumerIndex$FU = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "consumerIndex");
    }
}
