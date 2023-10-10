// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlinx.coroutines.internal.ThreadSafeHeap;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlin.TypeCastException;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class EventLoopImplBase extends EventLoopImplPlatform
{
    private volatile Object _queue;
    private static final AtomicReferenceFieldUpdater _queue$FU;
    private volatile Object _delayed;
    private static final AtomicReferenceFieldUpdater _delayed$FU;
    private volatile boolean isCompleted;
    
    @Override
    protected final boolean isEmpty() {
        if (!this.isUnconfinedQueueEmpty()) {
            return false;
        }
        final DelayedTaskQueue delayed;
        if ((delayed = (DelayedTaskQueue)this._delayed) != null && !delayed.isEmpty()) {
            return false;
        }
        final Object queue;
        final Object o;
        if ((o = (queue = this._queue)) == null) {
            return true;
        }
        if (o instanceof LockFreeTaskQueueCore) {
            return ((LockFreeTaskQueueCore)queue).isEmpty();
        }
        return queue == EventLoop_commonKt.access$getCLOSED_EMPTY$p$55cb9d62();
    }
    
    @Override
    protected final long getNextTime() {
        if (super.getNextTime() == 0L) {
            return 0L;
        }
        final Object queue;
        if ((queue = this._queue) != null) {
            if (queue instanceof LockFreeTaskQueueCore) {
                if (!((LockFreeTaskQueueCore)queue).isEmpty()) {
                    return 0L;
                }
            }
            else {
                if (queue == EventLoop_commonKt.access$getCLOSED_EMPTY$p$55cb9d62()) {
                    return Long.MAX_VALUE;
                }
                return 0L;
            }
        }
        final DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue)this._delayed;
        if (delayedTaskQueue != null) {
            final DelayedTask delayedTask = delayedTaskQueue.peek();
            if (delayedTask != null) {
                final long nanoTime = delayedTask.nanoTime;
                final TimeSource timeSource = null;
                return RangesKt___RangesKt.coerceAtLeast(nanoTime - ((timeSource != null) ? timeSource.nanoTime() : System.nanoTime()), 0L);
            }
        }
        return Long.MAX_VALUE;
    }
    
    @Override
    protected final void shutdown() {
        final ThreadLocalEventLoop instance = ThreadLocalEventLoop.INSTANCE;
        ThreadLocalEventLoop.resetEventLoop$kotlinx_coroutines_core();
        this.isCompleted = true;
        if (DebugKt.getASSERTIONS_ENABLED() && !this.isCompleted) {
            throw new AssertionError();
        }
        while (true) {
            final LockFreeTaskQueueCore queue;
            final Object o;
            if ((o = (queue = (LockFreeTaskQueueCore)this._queue)) == null) {
                if (EventLoopImplBase._queue$FU.compareAndSet(this, null, EventLoop_commonKt.access$getCLOSED_EMPTY$p$55cb9d62())) {
                    break;
                }
                continue;
            }
            else {
                if (o instanceof LockFreeTaskQueueCore) {
                    queue.close();
                    break;
                }
                if (queue == EventLoop_commonKt.access$getCLOSED_EMPTY$p$55cb9d62()) {
                    break;
                }
                final LockFreeTaskQueueCore lockFreeTaskQueueCore2;
                final LockFreeTaskQueueCore lockFreeTaskQueueCore = lockFreeTaskQueueCore2 = new LockFreeTaskQueueCore(8, (boolean)(1 != 0));
                final LockFreeTaskQueueCore lockFreeTaskQueueCore3 = queue;
                if (lockFreeTaskQueueCore3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.Runnable /* = java.lang.Runnable */");
                }
                lockFreeTaskQueueCore.addLast(lockFreeTaskQueueCore3);
                if (!EventLoopImplBase._queue$FU.compareAndSet(this, queue, lockFreeTaskQueueCore2)) {
                    continue;
                }
                break;
            }
        }
        while (this.processNextEvent() <= 0L) {}
        this.rescheduleAllDelayed();
    }
    
    @Override
    public final long processNextEvent() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   kotlinx/coroutines/EventLoopImplBase.processUnconfinedEvent:()Z
        //     4: ifeq            12
        //     7: aload_0         /* this */
        //     8: invokevirtual   kotlinx/coroutines/EventLoopImplBase.getNextTime:()J
        //    11: lreturn        
        //    12: aload_0         /* this */
        //    13: getfield        kotlinx/coroutines/EventLoopImplBase._delayed:Ljava/lang/Object;
        //    16: checkcast       Lkotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue;
        //    19: dup            
        //    20: astore_1        /* delayed */
        //    21: ifnull          165
        //    24: aload_1         /* delayed */
        //    25: invokevirtual   kotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue.isEmpty:()Z
        //    28: ifne            165
        //    31: aconst_null    
        //    32: dup            
        //    33: ifnull          44
        //    36: invokeinterface kotlinx/coroutines/TimeSource.nanoTime:()J
        //    41: goto            48
        //    44: pop            
        //    45: invokestatic    java/lang/System.nanoTime:()J
        //    48: lstore_2        /* now */
        //    49: aload_1         /* delayed */
        //    50: dup            
        //    51: astore          this_$iv
        //    53: dup            
        //    54: astore          lock$iv$iv
        //    56: monitorenter   
        //    57: aload           this_$iv
        //    59: invokevirtual   kotlinx/coroutines/internal/ThreadSafeHeap.firstImpl:()Lkotlinx/coroutines/internal/ThreadSafeHeapNode;
        //    62: dup            
        //    63: ifnonnull       74
        //    66: pop            
        //    67: aload           lock$iv$iv
        //    69: monitorexit    
        //    70: aconst_null    
        //    71: goto            153
        //    74: dup            
        //    75: astore          6
        //    77: checkcast       Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;
        //    80: dup            
        //    81: astore          it
        //    83: lload_2         /* now */
        //    84: lstore          10
        //    86: astore          7
        //    88: lload           10
        //    90: aload           7
        //    92: getfield        kotlinx/coroutines/EventLoopImplBase$DelayedTask.nanoTime:J
        //    95: lsub           
        //    96: lconst_0       
        //    97: lcmp           
        //    98: iflt            105
        //   101: iconst_1       
        //   102: goto            106
        //   105: iconst_0       
        //   106: ifeq            121
        //   109: aload_0         /* this */
        //   110: aload           it
        //   112: checkcast       Ljava/lang/Runnable;
        //   115: invokespecial   kotlinx/coroutines/EventLoopImplBase.enqueueImpl:(Ljava/lang/Runnable;)Z
        //   118: goto            122
        //   121: iconst_0       
        //   122: ifeq            134
        //   125: aload           this_$iv
        //   127: iconst_0       
        //   128: invokevirtual   kotlinx/coroutines/internal/ThreadSafeHeap.removeAtImpl:(I)Lkotlinx/coroutines/internal/ThreadSafeHeapNode;
        //   131: goto            135
        //   134: aconst_null    
        //   135: astore          4
        //   137: aload           lock$iv$iv
        //   139: monitorexit    
        //   140: aload           4
        //   142: goto            153
        //   145: astore          4
        //   147: aload           lock$iv$iv
        //   149: monitorexit    
        //   150: aload           4
        //   152: athrow         
        //   153: checkcast       Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;
        //   156: ifnonnull       162
        //   159: goto            165
        //   162: goto            49
        //   165: aload_0         /* this */
        //   166: dup            
        //   167: astore          7
        //   169: astore          10
        //   171: aload           10
        //   173: getfield        kotlinx/coroutines/EventLoopImplBase._queue:Ljava/lang/Object;
        //   176: dup            
        //   177: astore_1       
        //   178: dup            
        //   179: astore_2       
        //   180: ifnonnull       187
        //   183: aconst_null    
        //   184: goto            500
        //   187: aload_2        
        //   188: instanceof      Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;
        //   191: ifeq            451
        //   194: aload_1        
        //   195: dup            
        //   196: ifnonnull       209
        //   199: new             Lkotlin/TypeCastException;
        //   202: dup            
        //   203: ldc             "null cannot be cast to non-null type kotlinx.coroutines.Queue<kotlinx.coroutines.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.internal.LockFreeTaskQueueCore<kotlinx.coroutines.Runnable /* = java.lang.Runnable */> */"
        //   205: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   208: athrow         
        //   209: checkcast       Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;
        //   212: dup            
        //   213: astore_2       
        //   214: dup            
        //   215: astore_2       
        //   216: astore_3       
        //   217: aload_3        
        //   218: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore._state$internal:J
        //   221: dup2           
        //   222: lstore          18
        //   224: ldc2_w          1152921504606846976
        //   227: land           
        //   228: lconst_0       
        //   229: lcmp           
        //   230: ifeq            239
        //   233: getstatic       kotlinx/coroutines/internal/LockFreeTaskQueueCore.REMOVE_FROZEN$4fdbb1f:Lkotlin/coroutines/CoroutineContext$Element$DefaultImpls;
        //   236: goto            415
        //   239: lload           18
        //   241: ldc2_w          1073741823
        //   244: land           
        //   245: l2i            
        //   246: istore          4
        //   248: lload           18
        //   250: ldc2_w          1152921503533105152
        //   253: land           
        //   254: bipush          30
        //   256: lshr           
        //   257: l2i            
        //   258: dup            
        //   259: istore          5
        //   261: aload_2        
        //   262: invokestatic    kotlinx/coroutines/internal/LockFreeTaskQueueCore.access$getMask$p:(Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;)I
        //   265: iand           
        //   266: iload           4
        //   268: aload_2        
        //   269: invokestatic    kotlinx/coroutines/internal/LockFreeTaskQueueCore.access$getMask$p:(Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;)I
        //   272: iand           
        //   273: if_icmpne       280
        //   276: aconst_null    
        //   277: goto            415
        //   280: aload_2        
        //   281: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.array$internal:Ljava/util/concurrent/atomic/AtomicReferenceArray;
        //   284: iload           4
        //   286: aload_2        
        //   287: invokestatic    kotlinx/coroutines/internal/LockFreeTaskQueueCore.access$getMask$p:(Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;)I
        //   290: iand           
        //   291: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.get:(I)Ljava/lang/Object;
        //   294: dup            
        //   295: astore          5
        //   297: ifnonnull       314
        //   300: aload_2        
        //   301: invokestatic    kotlinx/coroutines/internal/LockFreeTaskQueueCore.access$getSingleConsumer$p:(Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;)Z
        //   304: ifeq            311
        //   307: aconst_null    
        //   308: goto            415
        //   311: goto            217
        //   314: aload           5
        //   316: instanceof      Lkotlinx/coroutines/internal/LockFreeTaskQueueCore$Placeholder;
        //   319: ifeq            326
        //   322: aconst_null    
        //   323: goto            415
        //   326: iload           4
        //   328: iconst_1       
        //   329: iadd           
        //   330: ldc             1073741823
        //   332: iand           
        //   333: istore          6
        //   335: aload_2        
        //   336: getstatic       kotlinx/coroutines/internal/LockFreeTaskQueueCore._state$FU$internal:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //   339: swap           
        //   340: lload           18
        //   342: getstatic       kotlinx/coroutines/internal/LockFreeTaskQueueCore.Companion:Lkotlinx/coroutines/internal/LockFreeTaskQueueCore$Companion;
        //   345: lload           18
        //   347: iload           6
        //   349: invokevirtual   kotlinx/coroutines/internal/LockFreeTaskQueueCore$Companion.updateHead:(JI)J
        //   352: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.compareAndSet:(Ljava/lang/Object;JJ)Z
        //   355: ifeq            378
        //   358: aload_2        
        //   359: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.array$internal:Ljava/util/concurrent/atomic/AtomicReferenceArray;
        //   362: iload           4
        //   364: aload_2        
        //   365: invokestatic    kotlinx/coroutines/internal/LockFreeTaskQueueCore.access$getMask$p:(Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;)I
        //   368: iand           
        //   369: aconst_null    
        //   370: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.set:(ILjava/lang/Object;)V
        //   373: aload           5
        //   375: goto            415
        //   378: aload_2        
        //   379: invokestatic    kotlinx/coroutines/internal/LockFreeTaskQueueCore.access$getSingleConsumer$p:(Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;)Z
        //   382: ifeq            412
        //   385: aload_2        
        //   386: checkcast       Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;
        //   389: astore_2       
        //   390: aload_2        
        //   391: iload           4
        //   393: iload           6
        //   395: invokestatic    kotlinx/coroutines/internal/LockFreeTaskQueueCore.access$removeSlowPath:(Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;II)Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;
        //   398: dup            
        //   399: ifnonnull       408
        //   402: pop            
        //   403: aload           5
        //   405: goto            415
        //   408: astore_2       
        //   409: goto            390
        //   412: goto            217
        //   415: dup            
        //   416: astore_2       
        //   417: getstatic       kotlinx/coroutines/internal/LockFreeTaskQueueCore.REMOVE_FROZEN$4fdbb1f:Lkotlin/coroutines/CoroutineContext$Element$DefaultImpls;
        //   420: if_acmpeq       430
        //   423: aload_2        
        //   424: checkcast       Ljava/lang/Runnable;
        //   427: goto            500
        //   430: aload           7
        //   432: getstatic       kotlinx/coroutines/EventLoopImplBase._queue$FU:Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;
        //   435: swap           
        //   436: aload_1        
        //   437: dup            
        //   438: checkcast       Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;
        //   441: invokevirtual   kotlinx/coroutines/internal/LockFreeTaskQueueCore.next:()Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;
        //   444: invokevirtual   java/util/concurrent/atomic/AtomicReferenceFieldUpdater.compareAndSet:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z
        //   447: pop            
        //   448: goto            171
        //   451: aload_1        
        //   452: invokestatic    kotlinx/coroutines/EventLoop_commonKt.access$getCLOSED_EMPTY$p$55cb9d62:()Lkotlin/coroutines/CoroutineContext$Element$DefaultImpls;
        //   455: if_acmpne       462
        //   458: aconst_null    
        //   459: goto            500
        //   462: aload           7
        //   464: getstatic       kotlinx/coroutines/EventLoopImplBase._queue$FU:Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;
        //   467: swap           
        //   468: aload_1        
        //   469: aconst_null    
        //   470: invokevirtual   java/util/concurrent/atomic/AtomicReferenceFieldUpdater.compareAndSet:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z
        //   473: ifeq            497
        //   476: aload_1        
        //   477: dup            
        //   478: ifnonnull       491
        //   481: new             Lkotlin/TypeCastException;
        //   484: dup            
        //   485: ldc             "null cannot be cast to non-null type kotlinx.coroutines.Runnable /* = java.lang.Runnable */"
        //   487: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   490: athrow         
        //   491: checkcast       Ljava/lang/Runnable;
        //   494: goto            500
        //   497: goto            171
        //   500: dup            
        //   501: ifnull          512
        //   504: invokeinterface java/lang/Runnable.run:()V
        //   509: goto            513
        //   512: pop            
        //   513: aload_0         /* this */
        //   514: invokevirtual   kotlinx/coroutines/EventLoopImplBase.getNextTime:()J
        //   517: lreturn        
        //    StackMapTable: 00 25 0C FF 00 1F 00 02 07 00 19 07 00 1B 00 01 05 43 04 FC 00 00 04 FF 00 18 00 05 07 00 19 07 00 1B 04 07 00 1B 07 00 1B 00 01 07 00 24 FC 00 1E 07 00 1A 40 01 FA 00 0E 40 01 FF 00 0B 00 05 07 00 19 07 00 1B 04 00 07 00 1B 00 00 40 07 00 24 FF 00 09 00 06 00 00 00 00 00 07 00 1B 00 01 07 00 10 FF 00 07 00 03 07 00 19 07 00 1B 04 00 01 07 00 24 08 F9 00 02 FF 00 05 00 0B 07 00 19 00 00 00 00 00 00 07 00 19 00 00 07 00 19 00 00 FF 00 0F 00 0B 07 00 19 07 00 0D 07 00 0D 00 00 00 00 07 00 19 00 00 07 00 19 00 00 FF 00 15 00 0B 07 00 19 07 00 0D 00 00 00 00 00 07 00 19 00 00 07 00 19 00 01 07 00 0D FF 00 07 00 0B 07 00 19 07 00 0D 07 00 20 07 00 20 00 00 00 07 00 19 00 00 07 00 19 00 00 FF 00 15 00 13 07 00 19 07 00 0D 07 00 20 07 00 20 00 00 00 07 00 19 00 00 07 00 19 00 00 00 00 00 00 00 04 00 00 FF 00 28 00 13 07 00 19 07 00 0D 07 00 20 07 00 20 01 00 00 07 00 19 00 00 07 00 19 00 00 00 00 00 00 00 04 00 00 FF 00 1E 00 0B 07 00 19 07 00 0D 07 00 20 07 00 20 00 00 00 07 00 19 00 00 07 00 19 00 00 FF 00 02 00 13 07 00 19 07 00 0D 07 00 20 07 00 20 01 07 00 0D 00 07 00 19 00 00 07 00 19 00 00 00 00 00 00 00 04 00 00 0B FF 00 33 00 0B 07 00 19 07 00 0D 07 00 20 07 00 20 01 07 00 0D 01 07 00 19 00 00 07 00 19 00 00 FF 00 0B 00 0B 07 00 19 07 00 0D 07 00 20 00 01 07 00 0D 01 07 00 19 00 00 07 00 19 00 00 FF 00 11 00 0B 07 00 19 07 00 0D 00 00 01 07 00 0D 01 07 00 19 00 00 07 00 19 00 01 07 00 20 FF 00 03 00 0B 07 00 19 07 00 0D 07 00 20 07 00 20 00 00 00 07 00 19 00 00 07 00 19 00 00 FF 00 02 00 0B 07 00 19 07 00 0D 00 00 00 00 00 07 00 19 00 00 07 00 19 00 01 07 00 0D 0E 14 0A FF 00 1C 00 01 07 00 19 00 01 07 00 0D FF 00 05 00 0B 07 00 19 00 00 00 00 00 00 07 00 19 00 00 07 00 19 00 00 FF 00 02 00 01 07 00 19 00 01 07 00 0E 4B 07 00 0E 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  57     67     145    153    Any
        //  74     137    145    153    Any
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
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:595)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
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
    
    @Override
    public final void dispatch(final CoroutineContext context, final Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        this.enqueue(block);
    }
    
    public final void enqueue(final Runnable task) {
        while (true) {
            Intrinsics.checkParameterIsNotNull(task, "task");
            if (this.enqueueImpl(task)) {
                break;
            }
            this = DefaultExecutor.INSTANCE;
        }
        this.unpark();
    }
    
    private final boolean enqueueImpl(final Runnable task) {
        final EventLoopImplBase $this$loop$iv = this;
        while (true) {
            final Object queue = $this$loop$iv._queue;
            if (this.isCompleted) {
                return false;
            }
            final Object o;
            if ((o = queue) == null) {
                if (EventLoopImplBase._queue$FU.compareAndSet(this, null, task)) {
                    return true;
                }
                continue;
            }
            else if (o instanceof LockFreeTaskQueueCore) {
                final Object o2 = queue;
                if (o2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.Queue<kotlinx.coroutines.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.internal.LockFreeTaskQueueCore<kotlinx.coroutines.Runnable /* = java.lang.Runnable */> */");
                }
                switch (((LockFreeTaskQueueCore<Runnable>)o2).addLast(task)) {
                    case 0: {
                        return true;
                    }
                    case 2: {
                        return false;
                    }
                    case 1: {
                        final AtomicReferenceFieldUpdater queue$FU = EventLoopImplBase._queue$FU;
                        final Object o3 = queue;
                        queue$FU.compareAndSet(this, o3, ((LockFreeTaskQueueCore)o3).next());
                        continue;
                    }
                }
            }
            else {
                if (queue == EventLoop_commonKt.access$getCLOSED_EMPTY$p$55cb9d62()) {
                    return false;
                }
                final LockFreeTaskQueueCore newQueue;
                final LockFreeTaskQueueCore lockFreeTaskQueueCore = newQueue = new LockFreeTaskQueueCore(8, (boolean)(1 != 0));
                final Object o4 = queue;
                if (o4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.Runnable /* = java.lang.Runnable */");
                }
                lockFreeTaskQueueCore.addLast(o4);
                newQueue.addLast(task);
                if (EventLoopImplBase._queue$FU.compareAndSet(this, queue, newQueue)) {
                    return true;
                }
                continue;
            }
        }
    }
    
    public final void schedule(final long now, final DelayedTask delayedTask) {
        Intrinsics.checkParameterIsNotNull(delayedTask, "delayedTask");
        int scheduleTask;
        if (this.isCompleted) {
            scheduleTask = 1;
        }
        else {
            DelayedTaskQueue delayed;
            if ((delayed = (DelayedTaskQueue)this._delayed) == null) {
                final EventLoopImplBase eventLoopImplBase;
                EventLoopImplBase._delayed$FU.compareAndSet(eventLoopImplBase = this, null, new DelayedTaskQueue(now));
                final Object delayed2 = eventLoopImplBase._delayed;
                if (delayed2 == null) {
                    Intrinsics.throwNpe();
                }
                delayed = (DelayedTaskQueue)delayed2;
            }
            scheduleTask = delayedTask.scheduleTask(now, delayed, this);
        }
        switch (scheduleTask) {
            case 0: {
                final DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue)this._delayed;
                if (((delayedTaskQueue != null) ? delayedTaskQueue.peek() : null) == delayedTask) {
                    this.unpark();
                }
            }
            case 1: {
                this.reschedule(now, delayedTask);
            }
            case 2: {}
            default: {
                throw new IllegalStateException("unexpected result".toString());
            }
        }
    }
    
    protected final void resetAll() {
        this._queue = null;
        this._delayed = null;
    }
    
    private final void rescheduleAllDelayed() {
        final TimeSource timeSource = null;
        final long now = (timeSource != null) ? timeSource.nanoTime() : System.nanoTime();
        while (true) {
            final DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue)this._delayed;
            if (delayedTaskQueue == null) {
                break;
            }
            final DelayedTask delayedTask2 = delayedTaskQueue.removeFirstOrNull();
            if (delayedTask2 == null) {
                break;
            }
            final DelayedTask delayedTask = delayedTask2;
            this.reschedule(now, delayedTask);
        }
    }
    
    public EventLoopImplBase() {
        this._queue = null;
        this._delayed = null;
    }
    
    static {
        _queue$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopImplBase.class, Object.class, "_queue");
        _delayed$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopImplBase.class, Object.class, "_delayed");
    }
    
    public abstract static class DelayedTask implements Comparable<DelayedTask>, Runnable, DisposableHandle, ThreadSafeHeapNode
    {
        private Object _heap;
        public long nanoTime;
        
        public final int scheduleTask(final long now, final DelayedTaskQueue delayed, final EventLoopImplBase eventLoop) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ldc             "delayed"
            //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
            //     6: aload           eventLoop
            //     8: ldc             "eventLoop"
            //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
            //    13: aload_0         /* this */
            //    14: getfield        kotlinx/coroutines/EventLoopImplBase$DelayedTask._heap:Ljava/lang/Object;
            //    17: invokestatic    kotlinx/coroutines/EventLoop_commonKt.access$getDISPOSED_TASK$p$55cb9d62:()Lkotlin/coroutines/CoroutineContext$Element$DefaultImpls;
            //    20: if_acmpne       25
            //    23: iconst_2       
            //    24: ireturn        
            //    25: aload_3         /* delayed */
            //    26: astore          5
            //    28: aload_0         /* this */
            //    29: checkcast       Lkotlinx/coroutines/internal/ThreadSafeHeapNode;
            //    32: astore          node$iv
            //    34: aload           this_$iv
            //    36: dup            
            //    37: astore          lock$iv$iv
            //    39: monitorenter   
            //    40: aload           this_$iv
            //    42: invokevirtual   kotlinx/coroutines/internal/ThreadSafeHeap.firstImpl:()Lkotlinx/coroutines/internal/ThreadSafeHeapNode;
            //    45: checkcast       Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;
            //    48: astore          firstTask
            //    50: aload           eventLoop
            //    52: invokestatic    kotlinx/coroutines/EventLoopImplBase.access$isCompleted$p:(Lkotlinx/coroutines/EventLoopImplBase;)Z
            //    55: ifeq            63
            //    58: aload           lock$iv$iv
            //    60: monitorexit    
            //    61: iconst_1       
            //    62: ireturn        
            //    63: aload           firstTask
            //    65: ifnonnull       76
            //    68: aload_3         /* delayed */
            //    69: lload_1         /* now */
            //    70: putfield        kotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue.timeNow:J
            //    73: goto            116
            //    76: aload           firstTask
            //    78: getfield        kotlinx/coroutines/EventLoopImplBase$DelayedTask.nanoTime:J
            //    81: dup2           
            //    82: lstore          firstTime
            //    84: lload_1         /* now */
            //    85: lsub           
            //    86: lconst_0       
            //    87: lcmp           
            //    88: iflt            95
            //    91: lload_1         /* now */
            //    92: goto            97
            //    95: lload           firstTime
            //    97: dup2           
            //    98: lstore          minTime
            //   100: aload_3         /* delayed */
            //   101: getfield        kotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue.timeNow:J
            //   104: lsub           
            //   105: lconst_0       
            //   106: lcmp           
            //   107: ifle            116
            //   110: aload_3         /* delayed */
            //   111: lload           minTime
            //   113: putfield        kotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue.timeNow:J
            //   116: aload_0         /* this */
            //   117: getfield        kotlinx/coroutines/EventLoopImplBase$DelayedTask.nanoTime:J
            //   120: aload_3         /* delayed */
            //   121: getfield        kotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue.timeNow:J
            //   124: lsub           
            //   125: lconst_0       
            //   126: lcmp           
            //   127: ifge            138
            //   130: aload_0         /* this */
            //   131: aload_3         /* delayed */
            //   132: getfield        kotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue.timeNow:J
            //   135: putfield        kotlinx/coroutines/EventLoopImplBase$DelayedTask.nanoTime:J
            //   138: aload           this_$iv
            //   140: aload           node$iv
            //   142: invokevirtual   kotlinx/coroutines/internal/ThreadSafeHeap.addImpl:(Lkotlinx/coroutines/internal/ThreadSafeHeapNode;)V
            //   145: aload           lock$iv$iv
            //   147: monitorexit    
            //   148: goto            157
            //   151: astore_1       
            //   152: aload           lock$iv$iv
            //   154: monitorexit    
            //   155: aload_1        
            //   156: athrow         
            //   157: iconst_0       
            //   158: ireturn        
            //    StackMapTable: 00 09 19 FF 00 25 00 08 07 00 0C 04 07 00 0D 00 07 00 0D 07 00 10 07 00 0D 07 00 0C 00 00 0C FF 00 12 00 0B 07 00 0C 00 00 07 00 0D 00 07 00 0D 07 00 10 07 00 0D 00 00 04 00 00 FF 00 01 00 08 07 00 0C 00 00 07 00 0D 00 07 00 0D 07 00 10 07 00 0D 00 01 04 12 FF 00 15 00 08 00 00 00 00 00 07 00 0D 07 00 10 07 00 0D 00 00 FF 00 0C 00 08 00 00 00 00 00 00 00 07 00 0D 00 01 07 00 08 FF 00 05 00 00 00 00
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  40     58     151    157    Any
            //  63     145    151    157    Any
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        @Override
        public String toString() {
            return "Delayed[nanos=" + this.nanoTime + ']';
        }
    }
    
    public static final class DelayedTaskQueue extends ThreadSafeHeap<DelayedTask>
    {
        public long timeNow;
        
        public DelayedTaskQueue(final long timeNow) {
            this.timeNow = timeNow;
        }
    }
}
