// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import com.badlogic.gdx.graphics.Pixmap;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlin.Result;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;

public final class DispatchedKt
{
    private static final CoroutineContext.Element.DefaultImpls UNDEFINED$4fdbb1f;
    
    public static final <T> void resumeCancellable(final Continuation<? super T> $this$resumeCancellable, final T value) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "$this$resumeCancellable"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0        
        //     7: dup            
        //     8: astore_2       
        //     9: instanceof      Lkotlinx/coroutines/DispatchedContinuation;
        //    12: ifeq            320
        //    15: aload_0        
        //    16: checkcast       Lkotlinx/coroutines/DispatchedContinuation;
        //    19: dup            
        //    20: astore_0        /* this_$iv */
        //    21: getfield        kotlinx/coroutines/DispatchedContinuation.dispatcher:Lkotlinx/coroutines/CoroutineDispatcher;
        //    24: aload_0         /* this_$iv */
        //    25: invokevirtual   kotlinx/coroutines/DispatchedContinuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //    28: invokevirtual   kotlinx/coroutines/CoroutineDispatcher.isDispatchNeeded:(Lkotlin/coroutines/CoroutineContext;)Z
        //    31: ifeq            62
        //    34: aload_0         /* this_$iv */
        //    35: aload_1        
        //    36: putfield        kotlinx/coroutines/DispatchedContinuation._state:Ljava/lang/Object;
        //    39: aload_0         /* this_$iv */
        //    40: iconst_1       
        //    41: putfield        kotlinx/coroutines/DispatchedContinuation.resumeMode:I
        //    44: aload_0         /* this_$iv */
        //    45: getfield        kotlinx/coroutines/DispatchedContinuation.dispatcher:Lkotlinx/coroutines/CoroutineDispatcher;
        //    48: aload_0         /* this_$iv */
        //    49: invokevirtual   kotlinx/coroutines/DispatchedContinuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //    52: aload_0         /* this_$iv */
        //    53: checkcast       Ljava/lang/Runnable;
        //    56: invokevirtual   kotlinx/coroutines/CoroutineDispatcher.dispatch:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Runnable;)V
        //    59: goto            340
        //    62: aload_0         /* this_$iv */
        //    63: astore_2        /* $this$executeUnconfined$iv$iv */
        //    64: getstatic       kotlinx/coroutines/ThreadLocalEventLoop.INSTANCE:Lkotlinx/coroutines/ThreadLocalEventLoop;
        //    67: pop            
        //    68: invokestatic    kotlinx/coroutines/ThreadLocalEventLoop.getEventLoop$kotlinx_coroutines_core:()Lkotlinx/coroutines/EventLoop;
        //    71: dup            
        //    72: astore_3        /* eventLoop$iv$iv */
        //    73: invokevirtual   kotlinx/coroutines/EventLoop.isUnconfinedLoopActive:()Z
        //    76: ifeq            100
        //    79: aload_2         /* $this$executeUnconfined$iv$iv */
        //    80: aload_1        
        //    81: putfield        kotlinx/coroutines/DispatchedContinuation._state:Ljava/lang/Object;
        //    84: aload_2         /* $this$executeUnconfined$iv$iv */
        //    85: iconst_1       
        //    86: putfield        kotlinx/coroutines/DispatchedContinuation.resumeMode:I
        //    89: aload_3         /* eventLoop$iv$iv */
        //    90: aload_2         /* $this$executeUnconfined$iv$iv */
        //    91: checkcast       Lkotlinx/coroutines/DispatchedTask;
        //    94: invokevirtual   kotlinx/coroutines/EventLoop.dispatchUnconfined:(Lkotlinx/coroutines/DispatchedTask;)V
        //    97: goto            340
        //   100: aload_2         /* $this$executeUnconfined$iv$iv */
        //   101: checkcast       Lkotlinx/coroutines/DispatchedTask;
        //   104: astore_2        /* $this$runUnconfinedEventLoop$iv$iv$iv */
        //   105: aload_3         /* eventLoop$iv$iv */
        //   106: iconst_1       
        //   107: invokevirtual   kotlinx/coroutines/EventLoop.incrementUseCount:(Z)V
        //   110: aload_0         /* this_$iv */
        //   111: dup            
        //   112: astore          this_$iv$iv
        //   114: invokevirtual   kotlinx/coroutines/DispatchedContinuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   117: getstatic       kotlinx/coroutines/Job.Key:Lkotlinx/coroutines/Job$Key;
        //   120: checkcast       Lkotlin/coroutines/CoroutineContext$Key;
        //   123: invokeinterface kotlin/coroutines/CoroutineContext.get:(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;
        //   128: checkcast       Lkotlinx/coroutines/Job;
        //   131: dup            
        //   132: astore          job$iv$iv
        //   134: ifnull          194
        //   137: aload           job$iv$iv
        //   139: invokeinterface kotlinx/coroutines/Job.isActive:()Z
        //   144: ifne            194
        //   147: aload           this_$iv$iv
        //   149: astore          6
        //   151: aload           job$iv$iv
        //   153: invokeinterface kotlinx/coroutines/Job.getCancellationException:()Ljava/util/concurrent/CancellationException;
        //   158: checkcast       Ljava/lang/Throwable;
        //   161: astore          5
        //   163: aload           6
        //   165: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   168: pop            
        //   169: astore          4
        //   171: aload           5
        //   173: invokestatic    com/badlogic/gdx/graphics/Pixmap.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //   176: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   179: astore          5
        //   181: aload           4
        //   183: aload           5
        //   185: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   190: iconst_1       
        //   191: goto            195
        //   194: iconst_0       
        //   195: ifne            279
        //   198: aload_0         /* this_$iv */
        //   199: astore          4
        //   201: aload_1        
        //   202: astore_0        /* value$iv$iv */
        //   203: aload           this_$iv$iv
        //   205: invokevirtual   kotlinx/coroutines/DispatchedContinuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   208: astore          6
        //   210: aload           this_$iv$iv
        //   212: getfield        kotlinx/coroutines/DispatchedContinuation.countOrElement:Ljava/lang/Object;
        //   215: astore          countOrElement$iv$iv$iv
        //   217: aload           context$iv$iv$iv
        //   219: aload           countOrElement$iv$iv$iv
        //   221: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.updateThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)Ljava/lang/Object;
        //   224: astore_1        /* oldValue$iv$iv$iv */
        //   225: aload           this_$iv$iv
        //   227: getfield        kotlinx/coroutines/DispatchedContinuation.continuation:Lkotlin/coroutines/Continuation;
        //   230: astore          4
        //   232: aload_0         /* value$iv$iv */
        //   233: astore          5
        //   235: aload           4
        //   237: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   240: pop            
        //   241: astore_0       
        //   242: aload           5
        //   244: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   247: astore          4
        //   249: aload_0        
        //   250: aload           4
        //   252: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   257: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   260: pop            
        //   261: aload           context$iv$iv$iv
        //   263: aload_1         /* oldValue$iv$iv$iv */
        //   264: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.restoreThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V
        //   267: goto            279
        //   270: astore_0       
        //   271: aload           context$iv$iv$iv
        //   273: aload_1         /* oldValue$iv$iv$iv */
        //   274: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.restoreThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V
        //   277: aload_0        
        //   278: athrow         
        //   279: aload_3         /* eventLoop$iv$iv */
        //   280: invokevirtual   kotlinx/coroutines/EventLoop.processUnconfinedEvent:()Z
        //   283: ifeq            289
        //   286: goto            279
        //   289: aload_3         /* eventLoop$iv$iv */
        //   290: iconst_1       
        //   291: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   294: goto            340
        //   297: astore_0        /* e$iv$iv$iv */
        //   298: aload_2         /* $this$runUnconfinedEventLoop$iv$iv$iv */
        //   299: aload_0         /* e$iv$iv$iv */
        //   300: aconst_null    
        //   301: invokevirtual   kotlinx/coroutines/DispatchedTask.handleFatalException$kotlinx_coroutines_core:(Ljava/lang/Throwable;Ljava/lang/Throwable;)V
        //   304: aload_3         /* eventLoop$iv$iv */
        //   305: iconst_1       
        //   306: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   309: goto            340
        //   312: astore_0       
        //   313: aload_3         /* eventLoop$iv$iv */
        //   314: iconst_1       
        //   315: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   318: aload_0        
        //   319: athrow         
        //   320: aload_0        
        //   321: dup            
        //   322: astore_0       
        //   323: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   326: pop            
        //   327: astore_0       
        //   328: aload_1        
        //   329: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   332: astore_1       
        //   333: aload_0        
        //   334: aload_1        
        //   335: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   340: return         
        //    Signature:
        //  <T:Ljava/lang/Object;>(Lkotlin/coroutines/Continuation<-TT;>;TT;)V
        //    StackMapTable: 00 0B FF 00 3E 00 02 07 00 17 07 00 0B 00 00 FD 00 25 07 00 17 07 00 1A FF 00 5D 00 04 07 00 17 07 00 0B 07 00 19 07 00 1A 00 00 40 01 FF 00 4A 00 07 00 07 00 0B 07 00 19 07 00 1A 00 00 07 00 11 00 01 07 00 0D FF 00 08 00 04 00 00 07 00 19 07 00 1A 00 00 FF 00 09 00 04 00 00 00 07 00 1A 00 00 FF 00 07 00 04 00 00 07 00 19 07 00 1A 00 01 07 00 0D FF 00 0E 00 04 00 00 00 07 00 1A 00 01 07 00 0D FF 00 07 00 02 07 00 10 07 00 0B 00 00 F9 00 13
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  225    261    270    279    Any
        //  110    289    297    312    Ljava/lang/Throwable;
        //  110    289    312    320    Any
        //  297    304    312    320    Any
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
    
    public static final <T> void resumeCancellableWithException(final Continuation<? super T> $this$resumeCancellableWithException, final Throwable exception) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "$this$resumeCancellableWithException"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1        
        //     7: ldc             "exception"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0        
        //    13: dup            
        //    14: astore_2       
        //    15: instanceof      Lkotlinx/coroutines/DispatchedContinuation;
        //    18: ifeq            354
        //    21: aload_0        
        //    22: checkcast       Lkotlinx/coroutines/DispatchedContinuation;
        //    25: dup            
        //    26: astore_0        /* this_$iv */
        //    27: getfield        kotlinx/coroutines/DispatchedContinuation.continuation:Lkotlin/coroutines/Continuation;
        //    30: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //    35: astore_2        /* context$iv */
        //    36: new             Lkotlinx/coroutines/CompletedExceptionally;
        //    39: dup            
        //    40: aload_1        
        //    41: iconst_0       
        //    42: iconst_2       
        //    43: invokespecial   kotlinx/coroutines/CompletedExceptionally.<init>:(Ljava/lang/Throwable;ZI)V
        //    46: astore_3        /* state$iv */
        //    47: aload_0         /* this_$iv */
        //    48: getfield        kotlinx/coroutines/DispatchedContinuation.dispatcher:Lkotlinx/coroutines/CoroutineDispatcher;
        //    51: aload_2         /* context$iv */
        //    52: invokevirtual   kotlinx/coroutines/CoroutineDispatcher.isDispatchNeeded:(Lkotlin/coroutines/CoroutineContext;)Z
        //    55: ifeq            92
        //    58: aload_0         /* this_$iv */
        //    59: new             Lkotlinx/coroutines/CompletedExceptionally;
        //    62: dup            
        //    63: aload_1        
        //    64: iconst_0       
        //    65: iconst_2       
        //    66: invokespecial   kotlinx/coroutines/CompletedExceptionally.<init>:(Ljava/lang/Throwable;ZI)V
        //    69: putfield        kotlinx/coroutines/DispatchedContinuation._state:Ljava/lang/Object;
        //    72: aload_0         /* this_$iv */
        //    73: iconst_1       
        //    74: putfield        kotlinx/coroutines/DispatchedContinuation.resumeMode:I
        //    77: aload_0         /* this_$iv */
        //    78: getfield        kotlinx/coroutines/DispatchedContinuation.dispatcher:Lkotlinx/coroutines/CoroutineDispatcher;
        //    81: aload_2         /* context$iv */
        //    82: aload_0         /* this_$iv */
        //    83: checkcast       Ljava/lang/Runnable;
        //    86: invokevirtual   kotlinx/coroutines/CoroutineDispatcher.dispatch:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Runnable;)V
        //    89: goto            383
        //    92: aload_0         /* this_$iv */
        //    93: astore_2        /* $this$executeUnconfined$iv$iv */
        //    94: getstatic       kotlinx/coroutines/ThreadLocalEventLoop.INSTANCE:Lkotlinx/coroutines/ThreadLocalEventLoop;
        //    97: pop            
        //    98: invokestatic    kotlinx/coroutines/ThreadLocalEventLoop.getEventLoop$kotlinx_coroutines_core:()Lkotlinx/coroutines/EventLoop;
        //   101: dup            
        //   102: astore          eventLoop$iv$iv
        //   104: invokevirtual   kotlinx/coroutines/EventLoop.isUnconfinedLoopActive:()Z
        //   107: ifeq            132
        //   110: aload_2         /* $this$executeUnconfined$iv$iv */
        //   111: aload_3         /* state$iv */
        //   112: putfield        kotlinx/coroutines/DispatchedContinuation._state:Ljava/lang/Object;
        //   115: aload_2         /* $this$executeUnconfined$iv$iv */
        //   116: iconst_1       
        //   117: putfield        kotlinx/coroutines/DispatchedContinuation.resumeMode:I
        //   120: aload           eventLoop$iv$iv
        //   122: aload_2         /* $this$executeUnconfined$iv$iv */
        //   123: checkcast       Lkotlinx/coroutines/DispatchedTask;
        //   126: invokevirtual   kotlinx/coroutines/EventLoop.dispatchUnconfined:(Lkotlinx/coroutines/DispatchedTask;)V
        //   129: goto            383
        //   132: aload_2         /* $this$executeUnconfined$iv$iv */
        //   133: checkcast       Lkotlinx/coroutines/DispatchedTask;
        //   136: astore_2        /* $this$runUnconfinedEventLoop$iv$iv$iv */
        //   137: aload           eventLoop$iv$iv
        //   139: iconst_1       
        //   140: invokevirtual   kotlinx/coroutines/EventLoop.incrementUseCount:(Z)V
        //   143: aload_0         /* this_$iv */
        //   144: dup            
        //   145: astore_3        /* this_$iv$iv */
        //   146: invokevirtual   kotlinx/coroutines/DispatchedContinuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   149: getstatic       kotlinx/coroutines/Job.Key:Lkotlinx/coroutines/Job$Key;
        //   152: checkcast       Lkotlin/coroutines/CoroutineContext$Key;
        //   155: invokeinterface kotlin/coroutines/CoroutineContext.get:(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;
        //   160: checkcast       Lkotlinx/coroutines/Job;
        //   163: dup            
        //   164: astore          job$iv$iv
        //   166: ifnull          223
        //   169: aload           job$iv$iv
        //   171: invokeinterface kotlinx/coroutines/Job.isActive:()Z
        //   176: ifne            223
        //   179: aload_3         /* this_$iv$iv */
        //   180: astore          6
        //   182: aload           job$iv$iv
        //   184: invokeinterface kotlinx/coroutines/Job.getCancellationException:()Ljava/util/concurrent/CancellationException;
        //   189: checkcast       Ljava/lang/Throwable;
        //   192: astore          5
        //   194: aload           6
        //   196: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   199: pop            
        //   200: astore_3       
        //   201: aload           5
        //   203: invokestatic    com/badlogic/gdx/graphics/Pixmap.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //   206: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   209: astore          5
        //   211: aload_3        
        //   212: aload           5
        //   214: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   219: iconst_1       
        //   220: goto            224
        //   223: iconst_0       
        //   224: ifne            309
        //   227: aload_0         /* this_$iv */
        //   228: astore_3       
        //   229: aload_1        
        //   230: astore_0        /* exception$iv$iv */
        //   231: aload_3         /* this_$iv$iv */
        //   232: invokevirtual   kotlinx/coroutines/DispatchedContinuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   235: astore          6
        //   237: aload_3         /* this_$iv$iv */
        //   238: getfield        kotlinx/coroutines/DispatchedContinuation.countOrElement:Ljava/lang/Object;
        //   241: astore          countOrElement$iv$iv$iv
        //   243: aload           context$iv$iv$iv
        //   245: aload           countOrElement$iv$iv$iv
        //   247: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.updateThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)Ljava/lang/Object;
        //   250: astore_1        /* oldValue$iv$iv$iv */
        //   251: aload_3         /* this_$iv$iv */
        //   252: getfield        kotlinx/coroutines/DispatchedContinuation.continuation:Lkotlin/coroutines/Continuation;
        //   255: astore_3       
        //   256: aload_0         /* exception$iv$iv */
        //   257: astore          exception$iv$iv$iv
        //   259: aload_3         /* $this$resumeWithStackTrace$iv$iv$iv */
        //   260: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   263: pop            
        //   264: aload           exception$iv$iv$iv
        //   266: aload_3         /* $this$resumeWithStackTrace$iv$iv$iv */
        //   267: invokestatic    kotlinx/coroutines/internal/StackTraceRecoveryKt.recoverStackTrace:(Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Throwable;
        //   270: astore_0       
        //   271: astore_3       
        //   272: aload_0        
        //   273: invokestatic    com/badlogic/gdx/graphics/Pixmap.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //   276: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   279: astore_0       
        //   280: aload_3        
        //   281: aload_0        
        //   282: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   287: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   290: pop            
        //   291: aload           context$iv$iv$iv
        //   293: aload_1         /* oldValue$iv$iv$iv */
        //   294: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.restoreThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V
        //   297: goto            309
        //   300: astore_0       
        //   301: aload           context$iv$iv$iv
        //   303: aload_1         /* oldValue$iv$iv$iv */
        //   304: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.restoreThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V
        //   307: aload_0        
        //   308: athrow         
        //   309: aload           eventLoop$iv$iv
        //   311: invokevirtual   kotlinx/coroutines/EventLoop.processUnconfinedEvent:()Z
        //   314: ifeq            320
        //   317: goto            309
        //   320: aload           eventLoop$iv$iv
        //   322: iconst_1       
        //   323: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   326: goto            383
        //   329: astore_0        /* e$iv$iv$iv */
        //   330: aload_2         /* $this$runUnconfinedEventLoop$iv$iv$iv */
        //   331: aload_0         /* e$iv$iv$iv */
        //   332: aconst_null    
        //   333: invokevirtual   kotlinx/coroutines/DispatchedTask.handleFatalException$kotlinx_coroutines_core:(Ljava/lang/Throwable;Ljava/lang/Throwable;)V
        //   336: aload           eventLoop$iv$iv
        //   338: iconst_1       
        //   339: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   342: goto            383
        //   345: astore_0       
        //   346: aload           eventLoop$iv$iv
        //   348: iconst_1       
        //   349: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   352: aload_0        
        //   353: athrow         
        //   354: aload_0        
        //   355: dup            
        //   356: astore_0        /* $this$resumeWithStackTrace$iv */
        //   357: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   360: pop            
        //   361: aload_1        
        //   362: aload_0         /* $this$resumeWithStackTrace$iv */
        //   363: invokestatic    kotlinx/coroutines/internal/StackTraceRecoveryKt.recoverStackTrace:(Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Throwable;
        //   366: astore_3       
        //   367: astore_2       
        //   368: aload_3        
        //   369: invokestatic    com/badlogic/gdx/graphics/Pixmap.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //   372: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   375: astore_0       
        //   376: aload_2        
        //   377: aload_0        
        //   378: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   383: return         
        //    Signature:
        //  <T:Ljava/lang/Object;>(Lkotlin/coroutines/Continuation<-TT;>;Ljava/lang/Throwable;)V
        //    StackMapTable: 00 0B FF 00 5C 00 04 07 00 17 07 00 0D 00 07 00 15 00 00 FF 00 27 00 05 07 00 17 07 00 0D 07 00 17 00 07 00 1A 00 00 FF 00 5A 00 05 07 00 17 07 00 0D 07 00 19 00 07 00 1A 00 00 40 01 FF 00 4B 00 07 00 07 00 0B 07 00 19 00 07 00 1A 00 07 00 11 00 01 07 00 0D FF 00 08 00 05 00 00 07 00 19 00 07 00 1A 00 00 FF 00 0A 00 05 00 00 00 00 07 00 1A 00 00 FF 00 08 00 05 00 00 07 00 19 00 07 00 1A 00 01 07 00 0D FF 00 0F 00 05 00 00 00 00 07 00 1A 00 01 07 00 0D FF 00 08 00 02 07 00 10 07 00 0D 00 00 F9 00 1C
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  251    291    300    309    Any
        //  143    320    329    345    Ljava/lang/Throwable;
        //  143    320    345    354    Any
        //  329    336    345    354    Any
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
    
    public static final <T> void resumeDirect(Continuation<? super T> $this$resumeDirect, T value) {
        Intrinsics.checkParameterIsNotNull($this$resumeDirect, "$this$resumeDirect");
        if ($this$resumeDirect instanceof DispatchedContinuation) {
            $this$resumeDirect = ((DispatchedContinuation)$this$resumeDirect).continuation;
            final Result.Companion companion = Result.Companion;
            $this$resumeDirect = $this$resumeDirect;
            value = Result.constructor-impl(value);
            $this$resumeDirect.resumeWith(value);
            return;
        }
        $this$resumeDirect = (DispatchedContinuation)$this$resumeDirect;
        final Result.Companion companion2 = Result.Companion;
        $this$resumeDirect = (DispatchedContinuation)$this$resumeDirect;
        $this$resumeDirect.resumeWith(Result.constructor-impl(value));
    }
    
    public static final <T> void resumeDirectWithException(Continuation<? super T> $this$resumeWithStackTrace$iv, Throwable exception) {
        Intrinsics.checkParameterIsNotNull($this$resumeDirectWithException, "$this$resumeDirectWithException");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if ($this$resumeDirectWithException instanceof DispatchedContinuation) {
            final Object o = $this$resumeWithStackTrace$iv = ((DispatchedContinuation<?>)$this$resumeDirectWithException).continuation;
            final Result.Companion companion = Result.Companion;
            final Throwable recoverStackTrace = StackTraceRecoveryKt.recoverStackTrace(exception, $this$resumeWithStackTrace$iv);
            exception = (Throwable)o;
            ((Continuation)exception).resumeWith(Result.constructor-impl(Pixmap.createFailure(recoverStackTrace)));
            return;
        }
        $this$resumeWithStackTrace$iv = $this$resumeDirectWithException;
        final Result.Companion companion2 = Result.Companion;
        $this$resumeDirectWithException.resumeWith(Result.constructor-impl(Pixmap.createFailure(StackTraceRecoveryKt.recoverStackTrace(exception, $this$resumeWithStackTrace$iv))));
    }
    
    public static final <T> void dispatch(DispatchedTask<? super T> $this$dispatch, int var_1_6E) {
        Intrinsics.checkParameterIsNotNull($this$dispatch, "$this$dispatch");
        final Continuation<? super Object> delegate$kotlinx_coroutines_core = ((DispatchedTask<? super Object>)$this$dispatch).getDelegate$kotlinx_coroutines_core();
        if ((mode == 0 || mode == 1) && delegate$kotlinx_coroutines_core instanceof DispatchedContinuation && Pixmap.isCancellableMode(mode) == Pixmap.isCancellableMode(((DispatchedTask)$this$dispatch).resumeMode)) {
            dispatcher = ((DispatchedContinuation<? super Object>)delegate$kotlinx_coroutines_core).dispatcher;
            final CoroutineContext context = delegate$kotlinx_coroutines_core.getContext();
            if (dispatcher.isDispatchNeeded(context)) {
                dispatcher.dispatch(context, (Runnable)$this$dispatch);
                return;
            }
            final Object task = $this$dispatch;
            final ThreadLocalEventLoop instance = ThreadLocalEventLoop.INSTANCE;
            if (($this$dispatch = ThreadLocalEventLoop.getEventLoop$kotlinx_coroutines_core()).isUnconfinedLoopActive()) {
                $this$dispatch.dispatchUnconfined((DispatchedTask<?>)task);
                return;
            }
            var_1_6E = (int)task;
            $this$dispatch.incrementUseCount(true);
            try {
                final DispatchedTask<?> $this$resume = (DispatchedTask<?>)task;
                resume((DispatchedTask<? super Object>)$this$resume, $this$resume.getDelegate$kotlinx_coroutines_core(), 3);
                while ($this$dispatch.processUnconfinedEvent()) {}
                return;
            }
            catch (Throwable exception) {
                ((DispatchedTask)var_1_6E).handleFatalException$kotlinx_coroutines_core(exception, null);
                return;
            }
            finally {
                $this$dispatch.decrementUseCount(true);
            }
        }
        resume((DispatchedTask<? super Object>)$this$dispatch, delegate$kotlinx_coroutines_core, mode);
    }
    
    private static <T> void resume(DispatchedTask<? super T> recovered, final Continuation<? super T> delegate, final int useMode) {
        Intrinsics.checkParameterIsNotNull($this$resume, "$this$resume");
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        final Object state;
        final Throwable exception;
        if ((exception = DispatchedTask.getExceptionalResult$kotlinx_coroutines_core(state = $this$resume.takeState$kotlinx_coroutines_core())) != null) {
            recovered = ((delegate instanceof DispatchedTask) ? exception : StackTraceRecoveryKt.recoverStackTrace(exception, delegate));
            Pixmap.resumeWithExceptionMode((Continuation<? super Object>)delegate, recovered, useMode);
            return;
        }
        Pixmap.resumeMode((Continuation<? super Object>)delegate, $this$resume.getSuccessfulResult$kotlinx_coroutines_core(state), useMode);
    }
    
    static {
        UNDEFINED$4fdbb1f = new CoroutineContext.Element.DefaultImpls("UNDEFINED");
    }
}
