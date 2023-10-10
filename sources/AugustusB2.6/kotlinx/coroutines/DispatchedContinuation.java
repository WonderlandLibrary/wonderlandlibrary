// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.internal.ThreadContextKt;
import kotlin.jvm.internal.Intrinsics;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.coroutines.Continuation;

public final class DispatchedContinuation<T> extends DispatchedTask<T> implements Continuation<T>, CoroutineStackFrame
{
    public Object _state;
    private final CoroutineStackFrame callerFrame;
    public final Object countOrElement;
    public final CoroutineDispatcher dispatcher;
    public final Continuation<T> continuation;
    
    @Override
    public final CoroutineStackFrame getCallerFrame() {
        return this.callerFrame;
    }
    
    @Override
    public final StackTraceElement getStackTraceElement() {
        return null;
    }
    
    @Override
    public final Object takeState$kotlinx_coroutines_core() {
        final Object state = this._state;
        if (DebugKt.getASSERTIONS_ENABLED() && state == DispatchedKt.access$getUNDEFINED$p$55cb9d62()) {
            throw new AssertionError();
        }
        this._state = DispatchedKt.access$getUNDEFINED$p$55cb9d62();
        return state;
    }
    
    @Override
    public final Continuation<T> getDelegate$kotlinx_coroutines_core() {
        return this;
    }
    
    @Override
    public final void resumeWith(final Object result) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        kotlinx/coroutines/DispatchedContinuation.continuation:Lkotlin/coroutines/Continuation;
        //     4: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //     9: astore_2        /* context */
        //    10: aload_1         /* result */
        //    11: invokestatic    com/badlogic/gdx/graphics/Pixmap.toState:(Ljava/lang/Object;)Ljava/lang/Object;
        //    14: astore_3        /* state */
        //    15: aload_0         /* this */
        //    16: getfield        kotlinx/coroutines/DispatchedContinuation.dispatcher:Lkotlinx/coroutines/CoroutineDispatcher;
        //    19: aload_2         /* context */
        //    20: invokevirtual   kotlinx/coroutines/CoroutineDispatcher.isDispatchNeeded:(Lkotlin/coroutines/CoroutineContext;)Z
        //    23: ifeq            49
        //    26: aload_0         /* this */
        //    27: aload_3         /* state */
        //    28: putfield        kotlinx/coroutines/DispatchedContinuation._state:Ljava/lang/Object;
        //    31: aload_0         /* this */
        //    32: iconst_0       
        //    33: putfield        kotlinx/coroutines/DispatchedContinuation.resumeMode:I
        //    36: aload_0         /* this */
        //    37: getfield        kotlinx/coroutines/DispatchedContinuation.dispatcher:Lkotlinx/coroutines/CoroutineDispatcher;
        //    40: aload_2         /* context */
        //    41: aload_0         /* this */
        //    42: checkcast       Ljava/lang/Runnable;
        //    45: invokevirtual   kotlinx/coroutines/CoroutineDispatcher.dispatch:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Runnable;)V
        //    48: return         
        //    49: aload_0         /* this */
        //    50: astore_2        /* $this$executeUnconfined$iv */
        //    51: getstatic       kotlinx/coroutines/ThreadLocalEventLoop.INSTANCE:Lkotlinx/coroutines/ThreadLocalEventLoop;
        //    54: pop            
        //    55: invokestatic    kotlinx/coroutines/ThreadLocalEventLoop.getEventLoop$kotlinx_coroutines_core:()Lkotlinx/coroutines/EventLoop;
        //    58: dup            
        //    59: astore          eventLoop$iv
        //    61: invokevirtual   kotlinx/coroutines/EventLoop.isUnconfinedLoopActive:()Z
        //    64: ifeq            87
        //    67: aload_2         /* $this$executeUnconfined$iv */
        //    68: aload_3         /* state */
        //    69: putfield        kotlinx/coroutines/DispatchedContinuation._state:Ljava/lang/Object;
        //    72: aload_2         /* $this$executeUnconfined$iv */
        //    73: iconst_0       
        //    74: putfield        kotlinx/coroutines/DispatchedContinuation.resumeMode:I
        //    77: aload           eventLoop$iv
        //    79: aload_2         /* $this$executeUnconfined$iv */
        //    80: checkcast       Lkotlinx/coroutines/DispatchedTask;
        //    83: invokevirtual   kotlinx/coroutines/EventLoop.dispatchUnconfined:(Lkotlinx/coroutines/DispatchedTask;)V
        //    86: return         
        //    87: aload_2         /* $this$executeUnconfined$iv */
        //    88: checkcast       Lkotlinx/coroutines/DispatchedTask;
        //    91: astore_2        /* $this$runUnconfinedEventLoop$iv$iv */
        //    92: aload           eventLoop$iv
        //    94: iconst_1       
        //    95: invokevirtual   kotlinx/coroutines/EventLoop.incrementUseCount:(Z)V
        //    98: aload_0         /* this */
        //    99: invokevirtual   kotlinx/coroutines/DispatchedContinuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   102: astore_3       
        //   103: aload_0         /* this */
        //   104: getfield        kotlinx/coroutines/DispatchedContinuation.countOrElement:Ljava/lang/Object;
        //   107: astore          countOrElement$iv
        //   109: aload_3         /* context$iv */
        //   110: aload           countOrElement$iv
        //   112: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.updateThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)Ljava/lang/Object;
        //   115: astore          oldValue$iv
        //   117: aload_0         /* this */
        //   118: getfield        kotlinx/coroutines/DispatchedContinuation.continuation:Lkotlin/coroutines/Continuation;
        //   121: aload_1         /* result */
        //   122: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   127: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   130: pop            
        //   131: aload_3         /* context$iv */
        //   132: aload           oldValue$iv
        //   134: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.restoreThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V
        //   137: goto            149
        //   140: astore_1       
        //   141: aload_3         /* context$iv */
        //   142: aload           oldValue$iv
        //   144: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.restoreThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V
        //   147: aload_1        
        //   148: athrow         
        //   149: aload           eventLoop$iv
        //   151: invokevirtual   kotlinx/coroutines/EventLoop.processUnconfinedEvent:()Z
        //   154: ifeq            160
        //   157: goto            149
        //   160: aload           eventLoop$iv
        //   162: iconst_1       
        //   163: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   166: return         
        //   167: astore_1        /* e$iv$iv */
        //   168: aload_2         /* $this$runUnconfinedEventLoop$iv$iv */
        //   169: aload_1         /* e$iv$iv */
        //   170: aconst_null    
        //   171: invokevirtual   kotlinx/coroutines/DispatchedTask.handleFatalException$kotlinx_coroutines_core:(Ljava/lang/Throwable;Ljava/lang/Throwable;)V
        //   174: aload           eventLoop$iv
        //   176: iconst_1       
        //   177: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   180: return         
        //   181: astore_1       
        //   182: aload           eventLoop$iv
        //   184: iconst_1       
        //   185: invokevirtual   kotlinx/coroutines/EventLoop.decrementUseCount:(Z)V
        //   188: aload_1        
        //   189: athrow         
        //    StackMapTable: 00 07 FD 00 31 00 07 00 07 FF 00 25 00 05 07 00 12 07 00 07 07 00 12 00 07 00 15 00 00 FF 00 34 00 06 00 00 07 00 14 07 00 0D 07 00 15 07 00 07 00 01 07 00 0A FF 00 08 00 05 00 00 07 00 14 00 07 00 15 00 00 FF 00 0A 00 05 00 00 00 00 07 00 15 00 00 FF 00 06 00 05 00 00 07 00 14 00 07 00 15 00 01 07 00 0A FF 00 0D 00 05 00 00 00 00 07 00 15 00 01 07 00 0A
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  117    131    140    149    Any
        //  98     160    167    181    Ljava/lang/Throwable;
        //  98     160    181    190    Any
        //  167    174    181    190    Any
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
    
    @Override
    public final String toString() {
        return "DispatchedContinuation[" + this.dispatcher + ", " + Pixmap.toDebugString(this.continuation) + ']';
    }
    
    public DispatchedContinuation(final CoroutineDispatcher dispatcher, final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(dispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        super(0);
        this.dispatcher = dispatcher;
        this.continuation = (Continuation<T>)continuation;
        this._state = DispatchedKt.access$getUNDEFINED$p$55cb9d62();
        Continuation<T> continuation2;
        if (!((continuation2 = this.continuation) instanceof CoroutineStackFrame)) {
            continuation2 = null;
        }
        this.callerFrame = (CoroutineStackFrame)continuation2;
        this.countOrElement = ThreadContextKt.threadContextElements(this.getContext());
    }
    
    @Override
    public final CoroutineContext getContext() {
        return this.continuation.getContext();
    }
}
