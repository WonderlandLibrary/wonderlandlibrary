// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import kotlin.coroutines.Continuation;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

final class AwaitAll<T>
{
    volatile int notCompletedCount;
    static final AtomicIntegerFieldUpdater notCompletedCount$FU;
    private final Deferred<T>[] deferreds;
    
    public final Object await(final Continuation<? super List<? extends T>> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2        /* uCont$iv */
        //     2: new             Lkotlinx/coroutines/CancellableContinuationImpl;
        //     5: dup            
        //     6: aload_2         /* uCont$iv */
        //     7: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.intercepted:(Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;
        //    10: iconst_1       
        //    11: invokespecial   kotlinx/coroutines/CancellableContinuationImpl.<init>:(Lkotlin/coroutines/Continuation;I)V
        //    14: dup            
        //    15: astore_2        /* cancellable$iv */
        //    16: checkcast       Lkotlinx/coroutines/CancellableContinuation;
        //    19: astore_3        /* cont */
        //    20: aload_0         /* this */
        //    21: dup            
        //    22: astore          10
        //    24: getfield        kotlinx/coroutines/AwaitAll.deferreds:[Lkotlinx/coroutines/Deferred;
        //    27: arraylength    
        //    28: dup            
        //    29: istore          4
        //    31: anewarray       Lkotlinx/coroutines/AwaitAll$AwaitAllNode;
        //    34: astore          5
        //    36: iconst_0       
        //    37: istore          6
        //    39: iload           6
        //    41: iload           4
        //    43: if_icmpge       171
        //    46: aload           5
        //    48: iload           6
        //    50: dup            
        //    51: invokestatic    com/badlogic/gdx/graphics/Pixmap.boxInt:(I)Ljava/lang/Integer;
        //    54: checkcast       Ljava/lang/Number;
        //    57: invokevirtual   java/lang/Number.intValue:()I
        //    60: istore          7
        //    62: istore          8
        //    64: astore          9
        //    66: aload_0         /* this */
        //    67: dup            
        //    68: astore          10
        //    70: getfield        kotlinx/coroutines/AwaitAll.deferreds:[Lkotlinx/coroutines/Deferred;
        //    73: iload           i
        //    75: aaload         
        //    76: dup            
        //    77: astore          deferred
        //    79: invokeinterface kotlinx/coroutines/Deferred.start:()Z
        //    84: pop            
        //    85: new             Lkotlinx/coroutines/AwaitAll$AwaitAllNode;
        //    88: dup            
        //    89: aload_0         /* this */
        //    90: aload_3         /* cont */
        //    91: aload           deferred
        //    93: checkcast       Lkotlinx/coroutines/Job;
        //    96: invokespecial   kotlinx/coroutines/AwaitAll$AwaitAllNode.<init>:(Lkotlinx/coroutines/AwaitAll;Lkotlinx/coroutines/CancellableContinuation;Lkotlinx/coroutines/Job;)V
        //    99: dup            
        //   100: astore          11
        //   102: dup            
        //   103: astore          $this$apply
        //   105: aload           deferred
        //   107: aload           $this$apply
        //   109: checkcast       Lkotlinx/coroutines/CompletionHandlerBase;
        //   112: astore          7
        //   114: astore          10
        //   116: astore          12
        //   118: aload           $this$asHandler$iv
        //   120: checkcast       Lkotlin/jvm/functions/Function1;
        //   123: astore          7
        //   125: aload           12
        //   127: aload           10
        //   129: aload           7
        //   131: invokeinterface kotlinx/coroutines/Deferred.invokeOnCompletion:(Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/DisposableHandle;
        //   136: astore          10
        //   138: astore          7
        //   140: aload           10
        //   142: ldc             "<set-?>"
        //   144: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   147: aload           7
        //   149: aload           10
        //   151: putfield        kotlinx/coroutines/AwaitAll$AwaitAllNode.handle:Lkotlinx/coroutines/DisposableHandle;
        //   154: aload           11
        //   156: astore          7
        //   158: aload           9
        //   160: iload           8
        //   162: aload           7
        //   164: aastore        
        //   165: iinc            6, 1
        //   168: goto            39
        //   171: aload           5
        //   173: astore          nodes
        //   175: new             Lkotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel;
        //   178: dup            
        //   179: aload_0         /* this */
        //   180: aload           nodes
        //   182: invokespecial   kotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel.<init>:(Lkotlinx/coroutines/AwaitAll;[Lkotlinx/coroutines/AwaitAll$AwaitAllNode;)V
        //   185: astore          disposer
        //   187: aload           nodes
        //   189: dup            
        //   190: astore          5
        //   192: dup            
        //   193: astore          7
        //   195: arraylength    
        //   196: istore          5
        //   198: iconst_0       
        //   199: istore          10
        //   201: iload           10
        //   203: iload           5
        //   205: if_icmpge       230
        //   208: aload           7
        //   210: iload           10
        //   212: aaload         
        //   213: dup            
        //   214: astore          11
        //   216: dup            
        //   217: astore          6
        //   219: aload           disposer
        //   221: invokevirtual   kotlinx/coroutines/AwaitAll$AwaitAllNode.setDisposer:(Lkotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel;)V
        //   224: iinc            10, 1
        //   227: goto            201
        //   230: aload_3         /* cont */
        //   231: invokeinterface kotlinx/coroutines/CancellableContinuation.isCompleted:()Z
        //   236: ifeq            247
        //   239: aload           disposer
        //   241: invokevirtual   kotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel.disposeAll:()V
        //   244: goto            273
        //   247: aload_3         /* cont */
        //   248: aload           disposer
        //   250: checkcast       Lkotlinx/coroutines/CancelHandlerBase;
        //   253: astore          5
        //   255: astore          9
        //   257: aload           $this$asHandler$iv
        //   259: checkcast       Lkotlin/jvm/functions/Function1;
        //   262: astore          8
        //   264: aload           9
        //   266: aload           8
        //   268: invokeinterface kotlinx/coroutines/CancellableContinuation.invokeOnCancellation:(Lkotlin/jvm/functions/Function1;)V
        //   273: aload_2         /* cancellable$iv */
        //   274: invokevirtual   kotlinx/coroutines/CancellableContinuationImpl.getResult:()Ljava/lang/Object;
        //   277: dup            
        //   278: getstatic       kotlin/coroutines/intrinsics/CoroutineSingletons.COROUTINE_SUSPENDED:Lkotlin/coroutines/intrinsics/CoroutineSingletons;
        //   281: if_acmpne       293
        //   284: aload_1         /* $completion */
        //   285: dup            
        //   286: astore          7
        //   288: ldc             "frame"
        //   290: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   293: areturn        
        //    Signature:
        //  (Lkotlin/coroutines/Continuation<-Ljava/util/List<+TT;>;>;)Ljava/lang/Object;
        //    StackMapTable: 00 07 FF 00 27 00 07 07 00 0F 07 00 0A 07 00 14 07 00 13 01 07 00 05 01 00 00 FF 00 83 00 06 07 00 0F 07 00 0A 07 00 14 07 00 13 00 07 00 05 00 00 FF 00 1D 00 0B 00 07 00 0A 07 00 14 07 00 13 07 00 11 01 00 07 00 05 00 00 01 00 00 FF 00 1C 00 05 00 07 00 0A 07 00 14 07 00 13 07 00 11 00 00 10 F9 00 19 FF 00 13 00 00 00 01 07 00 08
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
    
    public AwaitAll(final Deferred<? extends T>[] deferreds) {
        Intrinsics.checkParameterIsNotNull(deferreds, "deferreds");
        this.deferreds = (Deferred<T>[])deferreds;
        this.notCompletedCount = this.deferreds.length;
    }
    
    static {
        notCompletedCount$FU = AtomicIntegerFieldUpdater.newUpdater(AwaitAll.class, "notCompletedCount");
    }
    
    final class DisposeHandlersOnCancel extends CancelHandler
    {
        private final AwaitAllNode[] nodes;
        
        public final void disposeAll() {
            AwaitAllNode[] nodes;
            for (int length = (nodes = this.nodes).length, i = 0; i < length; ++i) {
                final DisposableHandle handle = nodes[i].handle;
                if (handle == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("handle");
                }
                handle.dispose();
            }
        }
        
        @Override
        public final void invoke(final Throwable cause) {
            this.disposeAll();
        }
        
        @Override
        public final String toString() {
            return "DisposeHandlersOnCancel[" + this.nodes + ']';
        }
        
        public DisposeHandlersOnCancel(final AwaitAll $outer, final AwaitAllNode[] nodes) {
            Intrinsics.checkParameterIsNotNull(nodes, "nodes");
            this.nodes = nodes;
        }
    }
    
    private final class AwaitAllNode extends JobNode<Job>
    {
        public DisposableHandle handle;
        private volatile DisposeHandlersOnCancel disposer;
        private final CancellableContinuation<List<? extends T>> continuation;
        
        public final void setDisposer(final DisposeHandlersOnCancel <set-?>) {
            this.disposer = <set-?>;
        }
        
        @Override
        public final void invoke(final Throwable cause) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ifnull          43
            //     4: aload_0         /* this */
            //     5: getfield        kotlinx/coroutines/AwaitAll$AwaitAllNode.continuation:Lkotlinx/coroutines/CancellableContinuation;
            //     8: aload_1         /* cause */
            //     9: invokeinterface kotlinx/coroutines/CancellableContinuation.tryResumeWithException:(Ljava/lang/Throwable;)Ljava/lang/Object;
            //    14: dup            
            //    15: astore_1        /* token */
            //    16: ifnull          168
            //    19: aload_0         /* this */
            //    20: getfield        kotlinx/coroutines/AwaitAll$AwaitAllNode.continuation:Lkotlinx/coroutines/CancellableContinuation;
            //    23: aload_1         /* token */
            //    24: invokeinterface kotlinx/coroutines/CancellableContinuation.completeResume:(Ljava/lang/Object;)V
            //    29: aload_0         /* this */
            //    30: getfield        kotlinx/coroutines/AwaitAll$AwaitAllNode.disposer:Lkotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel;
            //    33: dup            
            //    34: astore_2        /* disposer */
            //    35: ifnull          168
            //    38: aload_2         /* disposer */
            //    39: invokevirtual   kotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel.disposeAll:()V
            //    42: return         
            //    43: aload_0         /* this */
            //    44: getfield        kotlinx/coroutines/AwaitAll$AwaitAllNode.this$0:Lkotlinx/coroutines/AwaitAll;
            //    47: getstatic       kotlinx/coroutines/AwaitAll.notCompletedCount$FU:Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
            //    50: swap           
            //    51: invokevirtual   java/util/concurrent/atomic/AtomicIntegerFieldUpdater.decrementAndGet:(Ljava/lang/Object;)I
            //    54: ifne            168
            //    57: aload_0         /* this */
            //    58: getfield        kotlinx/coroutines/AwaitAll$AwaitAllNode.continuation:Lkotlinx/coroutines/CancellableContinuation;
            //    61: checkcast       Lkotlin/coroutines/Continuation;
            //    64: astore_1       
            //    65: aload_0         /* this */
            //    66: getfield        kotlinx/coroutines/AwaitAll$AwaitAllNode.this$0:Lkotlinx/coroutines/AwaitAll;
            //    69: invokestatic    kotlinx/coroutines/AwaitAll.access$getDeferreds$p:(Lkotlinx/coroutines/AwaitAll;)[Lkotlinx/coroutines/Deferred;
            //    72: dup            
            //    73: astore_2        /* $this$map$iv */
            //    74: astore_3       
            //    75: new             Ljava/util/ArrayList;
            //    78: dup            
            //    79: aload_2         /* $this$map$iv */
            //    80: arraylength    
            //    81: invokespecial   java/util/ArrayList.<init>:(I)V
            //    84: checkcast       Ljava/util/Collection;
            //    87: astore          destination$iv$iv
            //    89: aload_3         /* $this$mapTo$iv$iv */
            //    90: dup            
            //    91: astore_2       
            //    92: arraylength    
            //    93: istore_3       
            //    94: iconst_0       
            //    95: istore          5
            //    97: iload           5
            //    99: iload_3        
            //   100: if_icmpge       142
            //   103: aload_2        
            //   104: iload           5
            //   106: aaload         
            //   107: astore          item$iv$iv
            //   109: aload           destination$iv$iv
            //   111: aload           item$iv$iv
            //   113: astore          null
            //   115: astore          7
            //   117: aload           it
            //   119: invokeinterface kotlinx/coroutines/Deferred.getCompleted:()Ljava/lang/Object;
            //   124: astore          6
            //   126: aload           7
            //   128: aload           6
            //   130: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
            //   135: pop            
            //   136: iinc            5, 1
            //   139: goto            97
            //   142: aload           destination$iv$iv
            //   144: checkcast       Ljava/util/List;
            //   147: astore_2       
            //   148: aload_1        
            //   149: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //   152: pop            
            //   153: astore          4
            //   155: aload_2        
            //   156: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   159: astore_2       
            //   160: aload           4
            //   162: aload_2        
            //   163: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
            //   168: return         
            //    StackMapTable: 00 04 FA 00 2B FF 00 35 00 06 00 07 00 0B 07 00 03 01 07 00 06 01 00 00 FF 00 2C 00 05 00 07 00 0B 00 00 07 00 06 00 00 FF 00 19 00 00 00 00
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
        
        public AwaitAllNode(final CancellableContinuation<? super List<? extends T>> continuation, final Job job) {
            Intrinsics.checkParameterIsNotNull(continuation, "continuation");
            Intrinsics.checkParameterIsNotNull(job, "job");
            super(job);
            this.continuation = (CancellableContinuation<List<? extends T>>)continuation;
        }
    }
}
