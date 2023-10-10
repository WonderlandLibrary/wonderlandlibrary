// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import java.io.Serializable;
import java.util.ArrayList;
import kotlin.jvm.functions.Function2;
import kotlin.TypeCastException;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.UninitializedPropertyAccessException;
import java.util.Set;
import kotlin.ExceptionsKt__ExceptionsKt;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.ConcurrentKt;
import java.util.Iterator;
import java.util.concurrent.CancellationException;
import java.util.List;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlin.coroutines.CoroutineContext;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class JobSupport implements ChildJob, Job, ParentJob
{
    private volatile Object _state;
    private static final AtomicReferenceFieldUpdater _state$FU;
    public volatile ChildHandle parentHandle;
    
    @Override
    public final CoroutineContext.Key<?> getKey() {
        return Job.Key;
    }
    
    public final Object getState$kotlinx_coroutines_core() {
        final JobSupport $this$loop$iv = this;
        Object state;
        while ((state = $this$loop$iv._state) instanceof OpDescriptor) {
            ((OpDescriptor)state).perform(this);
        }
        return state;
    }
    
    @Override
    public boolean isActive() {
        final Object state;
        return (state = this.getState$kotlinx_coroutines_core()) instanceof Incomplete && ((Incomplete)state).isActive();
    }
    
    public final boolean isCompleted() {
        return !(this.getState$kotlinx_coroutines_core() instanceof Incomplete);
    }
    
    private final boolean tryFinalizeFinishingState(final Finishing state, final Object proposedUpdate, final int mode) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   kotlinx/coroutines/JobSupport.getState$kotlinx_coroutines_core:()Ljava/lang/Object;
        //     4: aload_1         /* state */
        //     5: if_acmpne       12
        //     8: iconst_1       
        //     9: goto            13
        //    12: iconst_0       
        //    13: dup            
        //    14: istore          4
        //    16: ifne            37
        //    19: ldc             "Failed requirement."
        //    21: astore_2        /* proposedUpdate */
        //    22: new             Ljava/lang/IllegalArgumentException;
        //    25: dup            
        //    26: aload_2         /* proposedUpdate */
        //    27: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    30: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    33: checkcast       Ljava/lang/Throwable;
        //    36: athrow         
        //    37: aload_1         /* state */
        //    38: invokevirtual   kotlinx/coroutines/JobSupport$Finishing.isSealed:()Z
        //    41: ifne            48
        //    44: iconst_1       
        //    45: goto            49
        //    48: iconst_0       
        //    49: dup            
        //    50: istore          4
        //    52: ifne            73
        //    55: ldc             "Failed requirement."
        //    57: astore_2        /* proposedUpdate */
        //    58: new             Ljava/lang/IllegalArgumentException;
        //    61: dup            
        //    62: aload_2         /* proposedUpdate */
        //    63: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    66: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    69: checkcast       Ljava/lang/Throwable;
        //    72: athrow         
        //    73: aload_1         /* state */
        //    74: getfield        kotlinx/coroutines/JobSupport$Finishing.isCompleting:Z
        //    77: dup            
        //    78: istore          4
        //    80: ifne            101
        //    83: ldc             "Failed requirement."
        //    85: astore_2        /* proposedUpdate */
        //    86: new             Ljava/lang/IllegalArgumentException;
        //    89: dup            
        //    90: aload_2         /* proposedUpdate */
        //    91: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    94: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    97: checkcast       Ljava/lang/Throwable;
        //   100: athrow         
        //   101: aload_2         /* proposedUpdate */
        //   102: dup            
        //   103: instanceof      Lkotlinx/coroutines/CompletedExceptionally;
        //   106: ifne            111
        //   109: pop            
        //   110: aconst_null    
        //   111: checkcast       Lkotlinx/coroutines/CompletedExceptionally;
        //   114: dup            
        //   115: ifnull          124
        //   118: getfield        kotlinx/coroutines/CompletedExceptionally.cause:Ljava/lang/Throwable;
        //   121: goto            126
        //   124: pop            
        //   125: aconst_null    
        //   126: astore          proposedException
        //   128: aload_1         /* state */
        //   129: monitorenter   
        //   130: aload_1         /* state */
        //   131: invokevirtual   kotlinx/coroutines/JobSupport$Finishing.isCancelling:()Z
        //   134: pop            
        //   135: aload_1         /* state */
        //   136: aload           proposedException
        //   138: invokevirtual   kotlinx/coroutines/JobSupport$Finishing.sealLocked:(Ljava/lang/Throwable;)Ljava/util/List;
        //   141: astore          exceptions
        //   143: aload_0         /* this */
        //   144: aload_1         /* state */
        //   145: aload           exceptions
        //   147: invokespecial   kotlinx/coroutines/JobSupport.getFinalRootCause:(Lkotlinx/coroutines/JobSupport$Finishing;Ljava/util/List;)Ljava/lang/Throwable;
        //   150: dup            
        //   151: astore          finalCause
        //   153: ifnull          163
        //   156: aload           finalCause
        //   158: aload           exceptions
        //   160: invokestatic    kotlinx/coroutines/JobSupport.addSuppressedExceptions:(Ljava/lang/Throwable;Ljava/util/List;)V
        //   163: aload           finalCause
        //   165: astore          5
        //   167: aload_1         /* state */
        //   168: monitorexit    
        //   169: aload           5
        //   171: goto            181
        //   174: astore          5
        //   176: aload_1         /* state */
        //   177: monitorexit    
        //   178: aload           5
        //   180: athrow         
        //   181: dup            
        //   182: astore          finalException
        //   184: ifnonnull       191
        //   187: aload_2         /* proposedUpdate */
        //   188: goto            213
        //   191: aload           finalException
        //   193: aload           proposedException
        //   195: if_acmpne       202
        //   198: aload_2         /* proposedUpdate */
        //   199: goto            213
        //   202: new             Lkotlinx/coroutines/CompletedExceptionally;
        //   205: dup            
        //   206: aload           finalException
        //   208: iconst_0       
        //   209: iconst_2       
        //   210: invokespecial   kotlinx/coroutines/CompletedExceptionally.<init>:(Ljava/lang/Throwable;ZI)V
        //   213: astore_2        /* finalState */
        //   214: aload           finalException
        //   216: ifnull          276
        //   219: aload_0         /* this */
        //   220: aload           finalException
        //   222: invokespecial   kotlinx/coroutines/JobSupport.cancelParent:(Ljava/lang/Throwable;)Z
        //   225: ifne            243
        //   228: aload           finalException
        //   230: dup            
        //   231: astore          4
        //   233: ldc             "exception"
        //   235: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   238: iconst_0       
        //   239: pop            
        //   240: goto            247
        //   243: iconst_1       
        //   244: goto            248
        //   247: iconst_0       
        //   248: dup            
        //   249: istore          4
        //   251: ifeq            276
        //   254: aload_2         /* finalState */
        //   255: dup            
        //   256: ifnonnull       269
        //   259: new             Lkotlin/TypeCastException;
        //   262: dup            
        //   263: ldc             "null cannot be cast to non-null type kotlinx.coroutines.CompletedExceptionally"
        //   265: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   268: athrow         
        //   269: checkcast       Lkotlinx/coroutines/CompletedExceptionally;
        //   272: invokevirtual   kotlinx/coroutines/CompletedExceptionally.makeHandled:()Z
        //   275: pop            
        //   276: aload_0         /* this */
        //   277: aload_2         /* finalState */
        //   278: invokevirtual   kotlinx/coroutines/JobSupport.onCompletionInternal:(Ljava/lang/Object;)V
        //   281: aload_0         /* this */
        //   282: getstatic       kotlinx/coroutines/JobSupport._state$FU:Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;
        //   285: swap           
        //   286: aload_1         /* state */
        //   287: aload_2         /* finalState */
        //   288: invokestatic    kotlinx/coroutines/JobSupportKt.boxIncomplete:(Ljava/lang/Object;)Ljava/lang/Object;
        //   291: invokevirtual   java/util/concurrent/atomic/AtomicReferenceFieldUpdater.compareAndSet:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z
        //   294: dup            
        //   295: istore          4
        //   297: ifne            353
        //   300: new             Ljava/lang/StringBuilder;
        //   303: dup            
        //   304: ldc             "Unexpected state: "
        //   306: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   309: aload_0         /* this */
        //   310: getfield        kotlinx/coroutines/JobSupport._state:Ljava/lang/Object;
        //   313: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   316: ldc             ", expected: "
        //   318: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   321: aload_1         /* state */
        //   322: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   325: ldc             ", update: "
        //   327: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   330: aload_2         /* finalState */
        //   331: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   334: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   337: astore_1        /* state */
        //   338: new             Ljava/lang/IllegalArgumentException;
        //   341: dup            
        //   342: aload_1         /* state */
        //   343: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   346: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   349: checkcast       Ljava/lang/Throwable;
        //   352: athrow         
        //   353: aload_0         /* this */
        //   354: aload_1         /* state */
        //   355: checkcast       Lkotlinx/coroutines/Incomplete;
        //   358: aload_2         /* finalState */
        //   359: iload_3         /* mode */
        //   360: invokespecial   kotlinx/coroutines/JobSupport.completeStateFinalization:(Lkotlinx/coroutines/Incomplete;Ljava/lang/Object;I)V
        //   363: iconst_1       
        //   364: ireturn        
        //    StackMapTable: 00 16 0C 40 01 17 0A 40 01 17 1B 49 07 00 2D 4C 07 00 42 41 07 00 30 FE 00 24 07 00 30 00 07 00 30 FF 00 0A 00 02 00 07 00 52 00 01 07 00 30 FF 00 06 00 05 07 00 50 07 00 52 07 00 2D 01 07 00 30 00 01 07 00 30 FC 00 09 07 00 30 FF 00 0A 00 06 07 00 50 07 00 52 00 01 00 07 00 30 00 00 4A 07 00 2D FF 00 1D 00 04 07 00 50 07 00 52 07 00 2D 01 00 00 03 40 01 54 07 00 2D 06 FB 00 4C
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  130    167    174    181    Any
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
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:543)
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
    
    private final Throwable getFinalRootCause(final Finishing state, final List<? extends Throwable> exceptions) {
        if (!exceptions.isEmpty()) {
            final Iterator<Object> iterator = exceptions.iterator();
            while (true) {
                while (iterator.hasNext()) {
                    final Object element$iv;
                    if (!(((Throwable)(element$iv = iterator.next())) instanceof CancellationException)) {
                        final Object o = element$iv;
                        Throwable t;
                        if ((t = (Throwable)o) == null) {
                            t = (Throwable)exceptions.get(0);
                        }
                        return t;
                    }
                }
                final Object o = null;
                continue;
            }
        }
        if (state.isCancelling()) {
            return this.createJobCancellationException();
        }
        return null;
    }
    
    private static void addSuppressedExceptions(final Throwable rootCause, final List<? extends Throwable> exceptions) {
        if (exceptions.size() <= 1) {
            return;
        }
        final Set seenExceptions = ConcurrentKt.identitySet(exceptions.size());
        final Throwable unwrappedCause = StackTraceRecoveryKt.unwrap(rootCause);
        final Iterator<? extends Throwable> iterator = exceptions.iterator();
        while (iterator.hasNext()) {
            final Throwable unwrapped;
            if ((unwrapped = StackTraceRecoveryKt.unwrap((Throwable)iterator.next())) != rootCause && unwrapped != unwrappedCause && !(unwrapped instanceof CancellationException) && seenExceptions.add(unwrapped)) {
                ExceptionsKt__ExceptionsKt.addSuppressed(rootCause, unwrapped);
            }
        }
    }
    
    private final boolean tryFinalizeSimpleState(final Incomplete state, final Object update, final int mode) {
        if (DebugKt.getASSERTIONS_ENABLED() && !(state instanceof Empty) && !(state instanceof JobNode)) {
            throw new AssertionError();
        }
        if (DebugKt.getASSERTIONS_ENABLED() && update instanceof CompletedExceptionally) {
            throw new AssertionError();
        }
        if (!JobSupport._state$FU.compareAndSet(this, state, JobSupportKt.boxIncomplete(update))) {
            return false;
        }
        this.onCompletionInternal(update);
        this.completeStateFinalization(state, update, mode);
        return true;
    }
    
    private final void completeStateFinalization(final Incomplete state, final Object update, final int mode) {
        final ChildHandle parentHandle = this.parentHandle;
        if (parentHandle != null) {
            parentHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
        Object o = update;
        if (!(update instanceof CompletedExceptionally)) {
            o = null;
        }
        final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
        final Throwable cause = (completedExceptionally != null) ? completedExceptionally.cause : null;
        if (state instanceof JobNode) {
            try {
                ((JobNode)state).invoke(cause);
            }
            catch (Throwable ex) {
                this.handleOnCompletionException$kotlinx_coroutines_core(new UninitializedPropertyAccessException("Exception in completion handler " + state + " for " + this, ex));
            }
        }
        else {
            final NodeList list = state.getList();
            if (list != null) {
                this.notifyCompletion(list, cause);
            }
        }
        this.afterCompletionInternal$4cfcfd12();
    }
    
    private final void notifyCancelling(final NodeList list, final Throwable cause) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3        /* this_$iv */
        //     2: aconst_null    
        //     3: astore          exception$iv
        //     5: aload_1         /* list */
        //     6: dup            
        //     7: astore_1        /* this_$iv$iv */
        //     8: invokevirtual   kotlinx/coroutines/internal/LockFreeLinkedListHead.getNext:()Ljava/lang/Object;
        //    11: dup            
        //    12: ifnonnull       25
        //    15: new             Lkotlin/TypeCastException;
        //    18: dup            
        //    19: ldc             "null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */"
        //    21: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //    24: athrow         
        //    25: checkcast       Lkotlinx/coroutines/internal/LockFreeLinkedListNode;
        //    28: astore          cur$iv$iv
        //    30: aload           cur$iv$iv
        //    32: aload_1         /* this_$iv$iv */
        //    33: checkcast       Lkotlinx/coroutines/internal/LockFreeLinkedListHead;
        //    36: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //    39: iconst_1       
        //    40: ixor           
        //    41: ifeq            166
        //    44: aload           cur$iv$iv
        //    46: instanceof      Lkotlinx/coroutines/JobCancellingNode;
        //    49: ifeq            156
        //    52: aload           cur$iv$iv
        //    54: checkcast       Lkotlinx/coroutines/JobNode;
        //    57: astore          node$iv
        //    59: aload           node$iv
        //    61: aload_2         /* cause */
        //    62: invokevirtual   kotlinx/coroutines/JobNode.invoke:(Ljava/lang/Throwable;)V
        //    65: goto            156
        //    68: astore          ex$iv
        //    70: aload           exception$iv
        //    72: dup            
        //    73: ifnull          101
        //    76: dup            
        //    77: astore          8
        //    79: dup            
        //    80: astore          9
        //    82: astore          9
        //    84: aload           ex$iv
        //    86: astore          other$iv$iv
        //    88: aload           $this$addSuppressedThrowable$iv$iv
        //    90: aload           other$iv$iv
        //    92: invokestatic    kotlin/ExceptionsKt__ExceptionsKt.addSuppressed:(Ljava/lang/Throwable;Ljava/lang/Throwable;)V
        //    95: aload           8
        //    97: dup            
        //    98: ifnonnull       155
        //   101: pop            
        //   102: aload_3         /* this_$iv */
        //   103: dup            
        //   104: astore          8
        //   106: checkcast       Lkotlinx/coroutines/JobSupport;
        //   109: astore          $this$run$iv
        //   111: new             Lkotlin/UninitializedPropertyAccessException;
        //   114: dup            
        //   115: new             Ljava/lang/StringBuilder;
        //   118: dup            
        //   119: ldc             "Exception in completion handler "
        //   121: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   124: aload           node$iv
        //   126: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   129: ldc             " for "
        //   131: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   134: aload           $this$run$iv
        //   136: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   139: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   142: aload           ex$iv
        //   144: invokespecial   kotlin/UninitializedPropertyAccessException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   147: checkcast       Ljava/lang/Throwable;
        //   150: astore          exception$iv
        //   152: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   155: pop            
        //   156: aload           cur$iv$iv
        //   158: invokevirtual   kotlinx/coroutines/internal/LockFreeLinkedListNode.getNextNode:()Lkotlinx/coroutines/internal/LockFreeLinkedListNode;
        //   161: astore          cur$iv$iv
        //   163: goto            30
        //   166: aload           exception$iv
        //   168: dup            
        //   169: ifnull          185
        //   172: dup            
        //   173: astore_1       
        //   174: astore          it$iv
        //   176: aload_3         /* this_$iv */
        //   177: aload           it$iv
        //   179: invokevirtual   kotlinx/coroutines/JobSupport.handleOnCompletionException$kotlinx_coroutines_core:(Ljava/lang/Throwable;)V
        //   182: goto            186
        //   185: pop            
        //   186: aload_0         /* this */
        //   187: aload_2         /* cause */
        //   188: invokespecial   kotlinx/coroutines/JobSupport.cancelParent:(Ljava/lang/Throwable;)Z
        //   191: pop            
        //   192: return         
        //    StackMapTable: 00 09 FF 00 19 00 05 07 00 50 07 00 55 07 00 30 07 00 50 05 00 01 07 00 2D FF 00 04 00 06 07 00 50 07 00 55 07 00 30 07 00 50 07 00 30 07 00 5A 00 00 FF 00 25 00 07 07 00 50 07 00 55 07 00 30 07 00 50 07 00 30 07 00 5A 07 00 4F 00 01 07 00 30 FF 00 20 00 08 07 00 50 07 00 55 07 00 30 07 00 50 00 07 00 5A 07 00 4F 07 00 30 00 01 07 00 30 FF 00 35 00 06 07 00 50 07 00 55 07 00 30 07 00 50 07 00 30 07 00 5A 00 01 07 00 2D 00 FF 00 09 00 05 07 00 50 00 07 00 30 07 00 50 07 00 30 00 00 FF 00 12 00 03 07 00 50 00 07 00 30 00 01 07 00 30 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  59     65     68     156    Ljava/lang/Throwable;
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
    
    private final boolean cancelParent(final Throwable cause) {
        if (this.isScopedCoroutine()) {
            return true;
        }
        final boolean isCancellation = cause instanceof CancellationException;
        final ChildHandle parent;
        if ((parent = this.parentHandle) == null || parent == NonDisposableHandle.INSTANCE) {
            return isCancellation;
        }
        return parent.childCancelled(cause) || isCancellation;
    }
    
    private final void notifyCompletion(final NodeList $this$notifyCompletion, final Throwable cause) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3        /* this_$iv */
        //     2: aconst_null    
        //     3: astore          exception$iv
        //     5: aload_1         /* $this$notifyCompletion */
        //     6: dup            
        //     7: astore_1        /* this_$iv$iv */
        //     8: invokevirtual   kotlinx/coroutines/internal/LockFreeLinkedListHead.getNext:()Ljava/lang/Object;
        //    11: dup            
        //    12: ifnonnull       25
        //    15: new             Lkotlin/TypeCastException;
        //    18: dup            
        //    19: ldc             "null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */"
        //    21: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //    24: athrow         
        //    25: checkcast       Lkotlinx/coroutines/internal/LockFreeLinkedListNode;
        //    28: astore          cur$iv$iv
        //    30: aload           cur$iv$iv
        //    32: aload_1         /* this_$iv$iv */
        //    33: checkcast       Lkotlinx/coroutines/internal/LockFreeLinkedListHead;
        //    36: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //    39: iconst_1       
        //    40: ixor           
        //    41: ifeq            166
        //    44: aload           cur$iv$iv
        //    46: instanceof      Lkotlinx/coroutines/JobNode;
        //    49: ifeq            156
        //    52: aload           cur$iv$iv
        //    54: checkcast       Lkotlinx/coroutines/JobNode;
        //    57: astore          node$iv
        //    59: aload           node$iv
        //    61: aload_2         /* cause */
        //    62: invokevirtual   kotlinx/coroutines/JobNode.invoke:(Ljava/lang/Throwable;)V
        //    65: goto            156
        //    68: astore          ex$iv
        //    70: aload           exception$iv
        //    72: dup            
        //    73: ifnull          101
        //    76: dup            
        //    77: astore          8
        //    79: dup            
        //    80: astore          9
        //    82: astore          9
        //    84: aload           ex$iv
        //    86: astore          other$iv$iv
        //    88: aload           $this$addSuppressedThrowable$iv$iv
        //    90: aload           other$iv$iv
        //    92: invokestatic    kotlin/ExceptionsKt__ExceptionsKt.addSuppressed:(Ljava/lang/Throwable;Ljava/lang/Throwable;)V
        //    95: aload           8
        //    97: dup            
        //    98: ifnonnull       155
        //   101: pop            
        //   102: aload_3         /* this_$iv */
        //   103: dup            
        //   104: astore          8
        //   106: checkcast       Lkotlinx/coroutines/JobSupport;
        //   109: astore          $this$run$iv
        //   111: new             Lkotlin/UninitializedPropertyAccessException;
        //   114: dup            
        //   115: new             Ljava/lang/StringBuilder;
        //   118: dup            
        //   119: ldc             "Exception in completion handler "
        //   121: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   124: aload           node$iv
        //   126: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   129: ldc             " for "
        //   131: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   134: aload           $this$run$iv
        //   136: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   139: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   142: aload           ex$iv
        //   144: invokespecial   kotlin/UninitializedPropertyAccessException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   147: checkcast       Ljava/lang/Throwable;
        //   150: astore          exception$iv
        //   152: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   155: pop            
        //   156: aload           cur$iv$iv
        //   158: invokevirtual   kotlinx/coroutines/internal/LockFreeLinkedListNode.getNextNode:()Lkotlinx/coroutines/internal/LockFreeLinkedListNode;
        //   161: astore          cur$iv$iv
        //   163: goto            30
        //   166: aload           exception$iv
        //   168: dup            
        //   169: ifnull          183
        //   172: dup            
        //   173: astore_1       
        //   174: astore          it$iv
        //   176: aload_3         /* this_$iv */
        //   177: aload           it$iv
        //   179: invokevirtual   kotlinx/coroutines/JobSupport.handleOnCompletionException$kotlinx_coroutines_core:(Ljava/lang/Throwable;)V
        //   182: return         
        //   183: pop            
        //   184: return         
        //    StackMapTable: 00 08 FF 00 19 00 05 00 07 00 55 07 00 30 07 00 50 05 00 01 07 00 2D FF 00 04 00 06 00 07 00 55 07 00 30 07 00 50 07 00 30 07 00 5A 00 00 FF 00 25 00 07 00 07 00 55 07 00 30 07 00 50 07 00 30 07 00 5A 07 00 4F 00 01 07 00 30 FF 00 20 00 08 00 07 00 55 07 00 30 07 00 50 00 07 00 5A 07 00 4F 07 00 30 00 01 07 00 30 FF 00 35 00 06 00 07 00 55 07 00 30 07 00 50 07 00 30 07 00 5A 00 01 07 00 2D 00 FF 00 09 00 05 00 00 00 07 00 50 07 00 30 00 00 FF 00 10 00 00 00 01 07 00 30
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  59     65     68     156    Ljava/lang/Throwable;
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
    public final boolean start() {
        final JobSupport this_$iv = this;
        while (true) {
            final Object state$kotlinx_coroutines_core;
            final Object state = state$kotlinx_coroutines_core = this_$iv.getState$kotlinx_coroutines_core();
            int n = 0;
            Label_0098: {
                final Object o;
                if ((o = state$kotlinx_coroutines_core) instanceof Empty) {
                    if (!((Empty)state$kotlinx_coroutines_core).isActive()) {
                        if (!JobSupport._state$FU.compareAndSet(this, state$kotlinx_coroutines_core, JobSupportKt.access$getEMPTY_ACTIVE$p())) {
                            n = -1;
                            break Label_0098;
                        }
                        this.onStartInternal$kotlinx_coroutines_core();
                        n = 1;
                        break Label_0098;
                    }
                }
                else if (o instanceof InactiveNodeList) {
                    final AtomicReferenceFieldUpdater state$FU = JobSupport._state$FU;
                    final Empty empty = (Empty)state$kotlinx_coroutines_core;
                    if (!state$FU.compareAndSet(this, empty, ((InactiveNodeList)empty).getList())) {
                        n = -1;
                        break Label_0098;
                    }
                    this.onStartInternal$kotlinx_coroutines_core();
                    n = 1;
                    break Label_0098;
                }
                n = 0;
            }
            switch (n) {
                case 0: {
                    return false;
                }
                case 1: {
                    return true;
                }
                default: {
                    continue;
                }
            }
        }
    }
    
    public void onStartInternal$kotlinx_coroutines_core() {
    }
    
    @Override
    public final CancellationException getCancellationException() {
        Object state;
        CancellationException cancellationException;
        if ((state = this.getState$kotlinx_coroutines_core()) instanceof Finishing) {
            final Throwable rootCause = ((Finishing)state).rootCause;
            if (rootCause == null || (cancellationException = this.toCancellationException(rootCause, Pixmap.getClassSimpleName(this) + " is cancelling")) == null) {
                state = "Job is still new or active: " + this;
                throw new IllegalStateException(state.toString());
            }
        }
        else {
            if (state instanceof Incomplete) {
                state = "Job is still new or active: " + this;
                throw new IllegalStateException(state.toString());
            }
            if (state instanceof CompletedExceptionally) {
                return this.toCancellationException(((CompletedExceptionally)state).cause, null);
            }
            cancellationException = new JobCancellationException(Pixmap.getClassSimpleName(this) + " has completed normally", null, this);
        }
        return cancellationException;
    }
    
    private CancellationException toCancellationException(final Throwable $this$toCancellationException, final String message) {
        Intrinsics.checkParameterIsNotNull($this$toCancellationException, "$this$toCancellationException");
        Throwable t = $this$toCancellationException;
        if (!($this$toCancellationException instanceof CancellationException)) {
            t = null;
        }
        CancellationException ex;
        if ((ex = (CancellationException)t) == null) {
            String string;
            if ((string = message) == null) {
                string = Pixmap.getClassSimpleName($this$toCancellationException) + " was cancelled";
            }
            ex = new JobCancellationException(string, $this$toCancellationException, this);
        }
        return ex;
    }
    
    @Override
    public final DisposableHandle invokeOnCompletion(final Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        return this.invokeOnCompletion(false, true, handler);
    }
    
    @Override
    public final DisposableHandle invokeOnCompletion(final boolean onCancelling, final boolean invokeImmediately, final Function1<? super Throwable, Unit> handler) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "handler"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aconst_null    
        //     7: astore          nodeCache
        //     9: aload_0         /* this */
        //    10: astore          this_$iv
        //    12: aload           this_$iv
        //    14: invokevirtual   kotlinx/coroutines/JobSupport.getState$kotlinx_coroutines_core:()Ljava/lang/Object;
        //    17: dup            
        //    18: astore          state
        //    20: dup            
        //    21: astore          7
        //    23: instanceof      Lkotlinx/coroutines/Empty;
        //    26: ifeq            152
        //    29: aload           state
        //    31: checkcast       Lkotlinx/coroutines/Empty;
        //    34: invokevirtual   kotlinx/coroutines/Empty.isActive:()Z
        //    37: ifeq            86
        //    40: aload           nodeCache
        //    42: dup            
        //    43: ifnonnull       63
        //    46: pop            
        //    47: aload_0         /* this */
        //    48: aload_3         /* handler */
        //    49: iload_1         /* onCancelling */
        //    50: invokespecial   kotlinx/coroutines/JobSupport.makeNode:(Lkotlin/jvm/functions/Function1;Z)Lkotlinx/coroutines/JobNode;
        //    53: dup            
        //    54: astore          7
        //    56: dup            
        //    57: astore          10
        //    59: astore          nodeCache
        //    61: aload           7
        //    63: astore          node
        //    65: aload_0         /* this */
        //    66: getstatic       kotlinx/coroutines/JobSupport._state$FU:Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;
        //    69: swap           
        //    70: aload           state
        //    72: aload           node
        //    74: invokevirtual   java/util/concurrent/atomic/AtomicReferenceFieldUpdater.compareAndSet:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z
        //    77: ifeq            474
        //    80: aload           node
        //    82: checkcast       Lkotlinx/coroutines/DisposableHandle;
        //    85: areturn        
        //    86: aload_0         /* this */
        //    87: aload           state
        //    89: checkcast       Lkotlinx/coroutines/Empty;
        //    92: astore          7
        //    94: astore          state
        //    96: new             Lkotlinx/coroutines/NodeList;
        //    99: dup            
        //   100: invokespecial   kotlinx/coroutines/NodeList.<init>:()V
        //   103: astore          8
        //   105: aload           7
        //   107: invokevirtual   kotlinx/coroutines/Empty.isActive:()Z
        //   110: ifeq            121
        //   113: aload           8
        //   115: checkcast       Lkotlinx/coroutines/Incomplete;
        //   118: goto            133
        //   121: new             Lkotlinx/coroutines/InactiveNodeList;
        //   124: dup            
        //   125: aload           8
        //   127: invokespecial   kotlinx/coroutines/InactiveNodeList.<init>:(Lkotlinx/coroutines/NodeList;)V
        //   130: checkcast       Lkotlinx/coroutines/Incomplete;
        //   133: astore          8
        //   135: aload           state
        //   137: getstatic       kotlinx/coroutines/JobSupport._state$FU:Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;
        //   140: swap           
        //   141: aload           7
        //   143: aload           8
        //   145: invokevirtual   java/util/concurrent/atomic/AtomicReferenceFieldUpdater.compareAndSet:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z
        //   148: pop            
        //   149: goto            12
        //   152: aload           7
        //   154: instanceof      Lkotlinx/coroutines/Incomplete;
        //   157: ifeq            422
        //   160: aload           state
        //   162: checkcast       Lkotlinx/coroutines/Incomplete;
        //   165: invokeinterface kotlinx/coroutines/Incomplete.getList:()Lkotlinx/coroutines/NodeList;
        //   170: dup            
        //   171: astore          list
        //   173: ifnonnull       202
        //   176: aload_0         /* this */
        //   177: aload           state
        //   179: dup            
        //   180: ifnonnull       193
        //   183: new             Lkotlin/TypeCastException;
        //   186: dup            
        //   187: ldc             "null cannot be cast to non-null type kotlinx.coroutines.JobNode<*>"
        //   189: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   192: athrow         
        //   193: checkcast       Lkotlinx/coroutines/JobNode;
        //   196: invokespecial   kotlinx/coroutines/JobSupport.promoteSingleToNodeList:(Lkotlinx/coroutines/JobNode;)V
        //   199: goto            12
        //   202: aconst_null    
        //   203: astore          rootCause
        //   205: getstatic       kotlinx/coroutines/NonDisposableHandle.INSTANCE:Lkotlinx/coroutines/NonDisposableHandle;
        //   208: checkcast       Lkotlinx/coroutines/DisposableHandle;
        //   211: astore          handle
        //   213: iload_1         /* onCancelling */
        //   214: ifeq            346
        //   217: aload           state
        //   219: instanceof      Lkotlinx/coroutines/JobSupport$Finishing;
        //   222: ifeq            346
        //   225: aload           state
        //   227: monitorenter   
        //   228: aload           state
        //   230: checkcast       Lkotlinx/coroutines/JobSupport$Finishing;
        //   233: getfield        kotlinx/coroutines/JobSupport$Finishing.rootCause:Ljava/lang/Throwable;
        //   236: dup            
        //   237: astore          rootCause
        //   239: ifnull          263
        //   242: aload_3         /* handler */
        //   243: dup            
        //   244: astore          9
        //   246: instanceof      Lkotlinx/coroutines/ChildHandleNode;
        //   249: ifeq            330
        //   252: aload           state
        //   254: checkcast       Lkotlinx/coroutines/JobSupport$Finishing;
        //   257: getfield        kotlinx/coroutines/JobSupport$Finishing.isCompleting:Z
        //   260: ifne            330
        //   263: aload           nodeCache
        //   265: dup            
        //   266: ifnonnull       286
        //   269: pop            
        //   270: aload_0         /* this */
        //   271: aload_3         /* handler */
        //   272: iload_1         /* onCancelling */
        //   273: invokespecial   kotlinx/coroutines/JobSupport.makeNode:(Lkotlin/jvm/functions/Function1;Z)Lkotlinx/coroutines/JobNode;
        //   276: dup            
        //   277: astore          handle
        //   279: dup            
        //   280: astore          nodeCache
        //   282: astore          nodeCache
        //   284: aload           handle
        //   286: astore          node
        //   288: aload_0         /* this */
        //   289: aload           state
        //   291: aload           list
        //   293: aload           node
        //   295: invokespecial   kotlinx/coroutines/JobSupport.addLastAtomic:(Ljava/lang/Object;Lkotlinx/coroutines/NodeList;Lkotlinx/coroutines/JobNode;)Z
        //   298: ifne            307
        //   301: aload           state
        //   303: monitorexit    
        //   304: goto            12
        //   307: aload           rootCause
        //   309: ifnonnull       323
        //   312: aload           node
        //   314: checkcast       Lkotlinx/coroutines/DisposableHandle;
        //   317: astore_1        /* onCancelling */
        //   318: aload           state
        //   320: monitorexit    
        //   321: aload_1         /* onCancelling */
        //   322: areturn        
        //   323: aload           node
        //   325: checkcast       Lkotlinx/coroutines/DisposableHandle;
        //   328: astore          handle
        //   330: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   333: pop            
        //   334: aload           state
        //   336: monitorexit    
        //   337: goto            346
        //   340: astore_1        /* onCancelling */
        //   341: aload           state
        //   343: monitorexit    
        //   344: aload_1         /* onCancelling */
        //   345: athrow         
        //   346: aload           rootCause
        //   348: ifnull          375
        //   351: iload_2         /* invokeImmediately */
        //   352: ifeq            372
        //   355: aload_3         /* handler */
        //   356: astore          9
        //   358: aload           rootCause
        //   360: astore          cause$iv
        //   362: aload           $this$invokeIt$iv
        //   364: aload           cause$iv
        //   366: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   371: pop            
        //   372: aload           handle
        //   374: areturn        
        //   375: aload           nodeCache
        //   377: dup            
        //   378: ifnonnull       398
        //   381: pop            
        //   382: aload_0         /* this */
        //   383: aload_3         /* handler */
        //   384: iload_1         /* onCancelling */
        //   385: invokespecial   kotlinx/coroutines/JobSupport.makeNode:(Lkotlin/jvm/functions/Function1;Z)Lkotlinx/coroutines/JobNode;
        //   388: dup            
        //   389: astore          10
        //   391: dup            
        //   392: astore          9
        //   394: astore          nodeCache
        //   396: aload           10
        //   398: astore          node
        //   400: aload_0         /* this */
        //   401: aload           state
        //   403: aload           list
        //   405: aload           node
        //   407: invokespecial   kotlinx/coroutines/JobSupport.addLastAtomic:(Ljava/lang/Object;Lkotlinx/coroutines/NodeList;Lkotlinx/coroutines/JobNode;)Z
        //   410: ifeq            419
        //   413: aload           node
        //   415: checkcast       Lkotlinx/coroutines/DisposableHandle;
        //   418: areturn        
        //   419: goto            12
        //   422: iload_2         /* invokeImmediately */
        //   423: ifeq            467
        //   426: aload_3         /* handler */
        //   427: astore          11
        //   429: aload           state
        //   431: dup            
        //   432: instanceof      Lkotlinx/coroutines/CompletedExceptionally;
        //   435: ifne            440
        //   438: pop            
        //   439: aconst_null    
        //   440: checkcast       Lkotlinx/coroutines/CompletedExceptionally;
        //   443: dup            
        //   444: ifnull          453
        //   447: getfield        kotlinx/coroutines/CompletedExceptionally.cause:Ljava/lang/Throwable;
        //   450: goto            455
        //   453: pop            
        //   454: aconst_null    
        //   455: astore          cause$iv
        //   457: aload           $this$invokeIt$iv
        //   459: aload           cause$iv
        //   461: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   466: pop            
        //   467: getstatic       kotlinx/coroutines/NonDisposableHandle.INSTANCE:Lkotlinx/coroutines/NonDisposableHandle;
        //   470: checkcast       Lkotlinx/coroutines/DisposableHandle;
        //   473: areturn        
        //   474: goto            12
        //    Signature:
        //  (ZZLkotlin/jvm/functions/Function1<-Ljava/lang/Throwable;Lkotlin/Unit;>;)Lkotlinx/coroutines/DisposableHandle;
        //    StackMapTable: 00 19 FD 00 0C 07 00 4F 07 00 50 FF 00 32 00 07 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 2D 00 01 07 00 4F 16 FF 00 22 00 09 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 50 07 00 46 07 00 55 00 00 FF 00 0B 00 08 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 50 07 00 46 00 01 07 00 48 FF 00 12 00 08 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 2D 07 00 2D 00 00 FF 00 28 00 06 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 00 02 07 00 50 07 00 2D FF 00 08 00 0C 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 2D 00 00 00 00 07 00 55 00 00 FF 00 3C 00 0C 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 2D 07 00 30 00 00 00 07 00 55 00 00 56 07 00 4F FF 00 14 00 0C 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 2D 07 00 30 00 07 00 4F 00 07 00 55 00 00 0F FF 00 06 00 0C 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 2D 07 00 30 07 00 45 00 00 07 00 55 00 00 FF 00 09 00 07 00 00 00 00 00 00 07 00 2D 00 01 07 00 30 FF 00 05 00 0C 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 2D 07 00 30 07 00 45 00 00 07 00 55 00 00 FF 00 19 00 09 00 00 00 00 00 00 00 00 07 00 45 00 00 FF 00 02 00 0C 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 07 00 2D 00 00 00 00 07 00 55 00 00 56 07 00 4F FF 00 14 00 06 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 00 00 FF 00 02 00 07 00 00 01 07 00 3D 00 00 07 00 2D 00 00 FF 00 11 00 0C 00 00 00 00 00 00 00 00 00 00 00 07 00 3D 00 01 07 00 2D 4C 07 00 42 41 07 00 30 FF 00 0B 00 00 00 00 FF 00 06 00 06 07 00 50 01 01 07 00 3D 07 00 4F 07 00 50 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  228    301    340    346    Any
        //  307    318    340    346    Any
        //  323    334    340    346    Any
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
    
    private final JobNode<?> makeNode(Function1<? super Throwable, Unit> handler, final boolean onCancelling) {
        if (onCancelling) {
            Object o = handler;
            if (!(handler instanceof JobCancellingNode)) {
                o = null;
            }
            final JobCancellingNode<?> jobCancellingNode = (JobCancellingNode<?>)o;
            if (jobCancellingNode != null) {
                final JobCancellingNode<?> jobCancellingNode2 = jobCancellingNode;
                final boolean b;
                if (!(b = (jobCancellingNode.job == this))) {
                    handler = "Failed requirement.";
                    throw new IllegalArgumentException(handler.toString());
                }
                final JobCancellingNode<?> jobCancellingNode3 = jobCancellingNode2;
                if (jobCancellingNode3 != null) {
                    return jobCancellingNode3;
                }
            }
            return new InvokeOnCancelling(this, (Function1<? super Throwable, Unit>)handler);
        }
        Object o2 = handler;
        if (!(handler instanceof JobNode)) {
            o2 = null;
        }
        final JobNode<?> jobNode = (JobNode<?>)o2;
        if (jobNode != null) {
            final JobNode<?> jobNode2 = jobNode;
            final JobNode it = jobNode;
            final boolean b2;
            if (!(b2 = (jobNode.job == this && !(it instanceof JobCancellingNode)))) {
                handler = "Failed requirement.";
                throw new IllegalArgumentException(handler.toString());
            }
            final JobNode<?> jobNode3;
            if ((jobNode3 = jobNode2) != null) {
                return jobNode3;
            }
        }
        return new InvokeOnCompletion(this, (Function1<? super Throwable, Unit>)handler);
    }
    
    private final boolean addLastAtomic(final Object expect, final NodeList list, final JobNode<?> node) {
        final LockFreeLinkedListNode this_$iv = list;
        final LockFreeLinkedListNode.CondAddOp condAdd$iv = new LockFreeLinkedListNode.CondAddOp(node, node, this, expect) {};
        while (true) {
            final Object prev = this_$iv.getPrev();
            if (prev == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
            switch (((LockFreeLinkedListNode)prev).tryCondAddNext(node, this_$iv, condAdd$iv)) {
                case 1: {
                    return true;
                }
                case 2: {
                    return false;
                }
                default: {
                    continue;
                }
            }
        }
    }
    
    private final void promoteSingleToNodeList(final JobNode<?> state) {
        state.addOneIfEmpty(new NodeList());
        final LockFreeLinkedListNode list = state.getNextNode();
        JobSupport._state$FU.compareAndSet(this, state, list);
    }
    
    public final void removeNode$kotlinx_coroutines_core(final JobNode<?> node) {
        Intrinsics.checkParameterIsNotNull(node, "node");
        final JobSupport this_$iv = this;
        Object state;
        Object o;
        while ((o = (state = this_$iv.getState$kotlinx_coroutines_core())) instanceof JobNode) {
            if (state != node) {
                return;
            }
            if (JobSupport._state$FU.compareAndSet(this, state, JobSupportKt.access$getEMPTY_ACTIVE$p())) {
                return;
            }
        }
        if (o instanceof Incomplete) {
            if (((Incomplete)state).getList() != null) {
                node.remove();
            }
        }
    }
    
    @Override
    public final void parentCancelled(final ParentJob parentJob) {
        Intrinsics.checkParameterIsNotNull(parentJob, "parentJob");
        this.makeCancelling(parentJob);
    }
    
    public final boolean cancelImpl$kotlinx_coroutines_core(final Object cause) {
        return this.makeCancelling(cause);
    }
    
    private final JobCancellationException createJobCancellationException() {
        return new JobCancellationException("Job was cancelled", null, this);
    }
    
    @Override
    public final CancellationException getChildJobCancellationCause() {
        Object state;
        final Object o;
        Throwable t3;
        Throwable t2;
        Throwable t;
        if ((o = (state = this.getState$kotlinx_coroutines_core())) instanceof Finishing) {
            t = (t2 = (t3 = ((Finishing)state).rootCause));
        }
        else if (o instanceof CompletedExceptionally) {
            t = (t2 = (t3 = ((CompletedExceptionally)state).cause));
        }
        else {
            if (o instanceof Incomplete) {
                state = "Cannot be cancelling child in this state: " + state;
                throw new IllegalStateException(state.toString());
            }
            t = (t2 = (t3 = null));
        }
        final Throwable rootCause = t2;
        if (!(t instanceof CancellationException)) {
            t3 = null;
        }
        CancellationException ex;
        if ((ex = (CancellationException)t3) == null) {
            ex = new JobCancellationException("Parent job is " + stateString(state), rootCause, this);
        }
        return ex;
    }
    
    private final Throwable createCauseException(final Object cause) {
        if (cause == null || cause instanceof Throwable) {
            if (cause != null) {
                return (Throwable)cause;
            }
            return this.createJobCancellationException();
        }
        else {
            if (cause == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
            }
            return ((ParentJob)cause).getChildJobCancellationCause();
        }
    }
    
    private final boolean makeCancelling(final Object cause) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aload_0         /* this */
        //     3: astore_3       
        //     4: aload_3        
        //     5: invokevirtual   kotlinx/coroutines/JobSupport.getState$kotlinx_coroutines_core:()Ljava/lang/Object;
        //     8: dup            
        //     9: astore          state
        //    11: dup            
        //    12: astore          5
        //    14: instanceof      Lkotlinx/coroutines/JobSupport$Finishing;
        //    17: ifeq            149
        //    20: aload           state
        //    22: monitorenter   
        //    23: aload           state
        //    25: checkcast       Lkotlinx/coroutines/JobSupport$Finishing;
        //    28: invokevirtual   kotlinx/coroutines/JobSupport$Finishing.isSealed:()Z
        //    31: ifeq            39
        //    34: aload           state
        //    36: monitorexit    
        //    37: iconst_0       
        //    38: ireturn        
        //    39: aload           state
        //    41: checkcast       Lkotlinx/coroutines/JobSupport$Finishing;
        //    44: invokevirtual   kotlinx/coroutines/JobSupport$Finishing.isCancelling:()Z
        //    47: istore_3        /* wasCancelling */
        //    48: aload_1        
        //    49: ifnonnull       56
        //    52: iload_3         /* wasCancelling */
        //    53: ifne            79
        //    56: aload_2        
        //    57: dup            
        //    58: ifnonnull       69
        //    61: pop            
        //    62: aload_0         /* this */
        //    63: aload_1        
        //    64: invokespecial   kotlinx/coroutines/JobSupport.createCauseException:(Ljava/lang/Object;)Ljava/lang/Throwable;
        //    67: dup            
        //    68: astore_1       
        //    69: astore_1        /* causeException */
        //    70: aload           state
        //    72: checkcast       Lkotlinx/coroutines/JobSupport$Finishing;
        //    75: aload_1         /* causeException */
        //    76: invokevirtual   kotlinx/coroutines/JobSupport$Finishing.addExceptionLocked:(Ljava/lang/Throwable;)V
        //    79: aload           state
        //    81: checkcast       Lkotlinx/coroutines/JobSupport$Finishing;
        //    84: getfield        kotlinx/coroutines/JobSupport$Finishing.rootCause:Ljava/lang/Throwable;
        //    87: astore_1       
        //    88: iload_3         /* wasCancelling */
        //    89: ifne            96
        //    92: iconst_1       
        //    93: goto            97
        //    96: iconst_0       
        //    97: ifeq            104
        //   100: aload_1        
        //   101: goto            105
        //   104: aconst_null    
        //   105: astore_1       
        //   106: aload           state
        //   108: monitorexit    
        //   109: aload_1        
        //   110: goto            119
        //   113: astore_1       
        //   114: aload           state
        //   116: monitorexit    
        //   117: aload_1        
        //   118: athrow         
        //   119: dup            
        //   120: astore          5
        //   122: dup            
        //   123: ifnull          146
        //   126: dup            
        //   127: astore          5
        //   129: astore_2        /* it */
        //   130: aload_0         /* this */
        //   131: aload           state
        //   133: checkcast       Lkotlinx/coroutines/JobSupport$Finishing;
        //   136: invokevirtual   kotlinx/coroutines/JobSupport$Finishing.getList:()Lkotlinx/coroutines/NodeList;
        //   139: aload_2         /* it */
        //   140: invokespecial   kotlinx/coroutines/JobSupport.notifyCancelling:(Lkotlinx/coroutines/NodeList;Ljava/lang/Throwable;)V
        //   143: goto            147
        //   146: pop            
        //   147: iconst_1       
        //   148: ireturn        
        //   149: aload           5
        //   151: instanceof      Lkotlinx/coroutines/Incomplete;
        //   154: ifeq            440
        //   157: aload_2        
        //   158: dup            
        //   159: ifnonnull       176
        //   162: pop            
        //   163: aload_0         /* this */
        //   164: aload_1        
        //   165: invokespecial   kotlinx/coroutines/JobSupport.createCauseException:(Ljava/lang/Object;)Ljava/lang/Throwable;
        //   168: dup            
        //   169: astore          5
        //   171: dup            
        //   172: astore_2       
        //   173: astore_2       
        //   174: aload           5
        //   176: astore          causeException
        //   178: aload           state
        //   180: checkcast       Lkotlinx/coroutines/Incomplete;
        //   183: invokeinterface kotlinx/coroutines/Incomplete.isActive:()Z
        //   188: ifeq            332
        //   191: aload_0         /* this */
        //   192: aload           state
        //   194: checkcast       Lkotlinx/coroutines/Incomplete;
        //   197: aload           causeException
        //   199: astore          6
        //   201: astore          causeException
        //   203: astore          state
        //   205: invokestatic    kotlinx/coroutines/DebugKt.getASSERTIONS_ENABLED:()Z
        //   208: ifeq            238
        //   211: aload           causeException
        //   213: instanceof      Lkotlinx/coroutines/JobSupport$Finishing;
        //   216: ifne            223
        //   219: iconst_1       
        //   220: goto            224
        //   223: iconst_0       
        //   224: ifne            238
        //   227: new             Ljava/lang/AssertionError;
        //   230: dup            
        //   231: invokespecial   java/lang/AssertionError.<init>:()V
        //   234: checkcast       Ljava/lang/Throwable;
        //   237: athrow         
        //   238: invokestatic    kotlinx/coroutines/DebugKt.getASSERTIONS_ENABLED:()Z
        //   241: ifeq            265
        //   244: aload           causeException
        //   246: invokeinterface kotlinx/coroutines/Incomplete.isActive:()Z
        //   251: ifne            265
        //   254: new             Ljava/lang/AssertionError;
        //   257: dup            
        //   258: invokespecial   java/lang/AssertionError.<init>:()V
        //   261: checkcast       Ljava/lang/Throwable;
        //   264: athrow         
        //   265: aload           state
        //   267: aload           causeException
        //   269: invokespecial   kotlinx/coroutines/JobSupport.getOrPromoteCancellingList:(Lkotlinx/coroutines/Incomplete;)Lkotlinx/coroutines/NodeList;
        //   272: dup            
        //   273: ifnonnull       281
        //   276: pop            
        //   277: iconst_0       
        //   278: goto            327
        //   281: astore          7
        //   283: new             Lkotlinx/coroutines/JobSupport$Finishing;
        //   286: dup            
        //   287: aload           7
        //   289: iconst_0       
        //   290: aload           6
        //   292: invokespecial   kotlinx/coroutines/JobSupport$Finishing.<init>:(Lkotlinx/coroutines/NodeList;ZLjava/lang/Throwable;)V
        //   295: astore          8
        //   297: aload           state
        //   299: getstatic       kotlinx/coroutines/JobSupport._state$FU:Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;
        //   302: swap           
        //   303: aload           causeException
        //   305: aload           8
        //   307: invokevirtual   java/util/concurrent/atomic/AtomicReferenceFieldUpdater.compareAndSet:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z
        //   310: ifne            317
        //   313: iconst_0       
        //   314: goto            327
        //   317: aload           state
        //   319: aload           7
        //   321: aload           6
        //   323: invokespecial   kotlinx/coroutines/JobSupport.notifyCancelling:(Lkotlinx/coroutines/NodeList;Ljava/lang/Throwable;)V
        //   326: iconst_1       
        //   327: ifeq            442
        //   330: iconst_1       
        //   331: ireturn        
        //   332: aload_0         /* this */
        //   333: aload           state
        //   335: new             Lkotlinx/coroutines/CompletedExceptionally;
        //   338: dup            
        //   339: aload           causeException
        //   341: iconst_0       
        //   342: iconst_2       
        //   343: invokespecial   kotlinx/coroutines/CompletedExceptionally.<init>:(Ljava/lang/Throwable;ZI)V
        //   346: iconst_0       
        //   347: invokespecial   kotlinx/coroutines/JobSupport.tryMakeCompleting:(Ljava/lang/Object;Ljava/lang/Object;I)I
        //   350: tableswitch {
        //                0: 380
        //                1: 415
        //                2: 415
        //                3: 417
        //          default: 420
        //        }
        //   380: new             Ljava/lang/StringBuilder;
        //   383: dup            
        //   384: ldc             "Cannot happen in "
        //   386: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   389: aload           state
        //   391: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   394: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   397: astore          5
        //   399: new             Ljava/lang/IllegalStateException;
        //   402: dup            
        //   403: aload           5
        //   405: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   408: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   411: checkcast       Ljava/lang/Throwable;
        //   414: athrow         
        //   415: iconst_1       
        //   416: ireturn        
        //   417: goto            4
        //   420: ldc             "unexpected result"
        //   422: astore          5
        //   424: new             Ljava/lang/IllegalStateException;
        //   427: dup            
        //   428: aload           5
        //   430: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   433: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   436: checkcast       Ljava/lang/Throwable;
        //   439: athrow         
        //   440: iconst_0       
        //   441: ireturn        
        //   442: goto            4
        //    StackMapTable: 00 1D FD 00 04 07 00 30 07 00 50 FF 00 22 00 05 07 00 50 07 00 2D 07 00 30 00 07 00 2D 00 00 FF 00 10 00 05 07 00 50 07 00 2D 07 00 30 01 07 00 2D 00 00 FF 00 0C 00 05 07 00 50 00 00 01 07 00 2D 00 01 07 00 30 09 FF 00 10 00 05 07 00 50 07 00 30 00 00 07 00 2D 00 00 40 01 FF 00 06 00 05 07 00 50 00 00 00 07 00 2D 00 00 40 07 00 30 FF 00 07 00 05 00 00 00 00 07 00 2D 00 01 07 00 30 FF 00 05 00 05 07 00 50 00 00 00 07 00 2D 00 01 07 00 30 FF 00 1A 00 00 00 01 07 00 30 00 FF 00 01 00 06 07 00 50 07 00 2D 07 00 30 07 00 50 07 00 2D 07 00 2D 00 00 FF 00 1A 00 05 07 00 50 07 00 2D 07 00 30 07 00 50 07 00 2D 00 01 07 00 30 FF 00 2E 00 07 07 00 50 07 00 2D 07 00 30 07 00 50 07 00 50 07 00 48 07 00 30 00 00 40 01 0D 1A 4F 07 00 55 FF 00 23 00 08 07 00 50 07 00 2D 07 00 30 07 00 50 07 00 50 00 07 00 30 07 00 55 00 00 FF 00 09 00 04 07 00 50 07 00 2D 07 00 30 07 00 50 00 01 01 FD 00 04 07 00 2D 07 00 30 FF 00 2F 00 05 00 00 00 00 07 00 2D 00 00 FF 00 22 00 00 00 00 FF 00 01 00 04 07 00 50 07 00 2D 07 00 30 07 00 50 00 00 FF 00 02 00 00 00 00 13 FF 00 01 00 04 07 00 50 07 00 2D 07 00 30 07 00 50 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  23     34     113    119    Any
        //  39     106    113    119    Any
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
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:543)
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
    
    private final NodeList getOrPromoteCancellingList(final Incomplete state) {
        final NodeList list = state.getList();
        if (list != null) {
            return list;
        }
        if (state instanceof Empty) {
            return new NodeList();
        }
        if (state instanceof JobNode) {
            this.promoteSingleToNodeList((JobNode<?>)state);
            return null;
        }
        throw new IllegalStateException(("State should have list: " + state).toString());
    }
    
    public final boolean makeCompletingOnce$kotlinx_coroutines_core(Object proposedUpdate, final int mode) {
        final JobSupport this_$iv = this;
        while (true) {
            final Object state = this_$iv.getState$kotlinx_coroutines_core();
            switch (this.tryMakeCompleting(state, proposedUpdate, mode)) {
                case 0: {
                    final String string = "Job " + this + " is already complete or completing, but is being completed with " + proposedUpdate;
                    Object o;
                    if (!((proposedUpdate = (o = proposedUpdate)) instanceof CompletedExceptionally)) {
                        o = null;
                    }
                    final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
                    throw new IllegalStateException(string, (completedExceptionally != null) ? completedExceptionally.cause : null);
                }
                case 1: {
                    return true;
                }
                case 2: {
                    return false;
                }
                case 3: {
                    continue;
                }
                default: {
                    throw new IllegalStateException("unexpected result".toString());
                }
            }
        }
    }
    
    private final int tryMakeCompleting(final Object state, final Object proposedUpdate, final int mode) {
        if (!(state instanceof Incomplete)) {
            return 0;
        }
        if ((!(state instanceof Empty) && !(state instanceof JobNode)) || state instanceof ChildHandleNode || proposedUpdate instanceof CompletedExceptionally) {
            return this.tryMakeCompletingSlowPath((Incomplete)state, proposedUpdate, mode);
        }
        if (!this.tryFinalizeSimpleState((Incomplete)state, proposedUpdate, mode)) {
            return 3;
        }
        return 1;
    }
    
    private final int tryMakeCompletingSlowPath(Incomplete state, final Object proposedUpdate, final int mode) {
        final NodeList orPromoteCancellingList = this.getOrPromoteCancellingList(state);
        if (orPromoteCancellingList == null) {
            return 3;
        }
        final NodeList list = orPromoteCancellingList;
        Incomplete incomplete;
        if (!((incomplete = state) instanceof Finishing)) {
            incomplete = null;
        }
        Finishing finishing3;
        Finishing finishing2;
        if ((finishing2 = (finishing3 = (Finishing)incomplete)) == null) {
            finishing3 = (finishing2 = new Finishing(list, (boolean)(0 != 0), null));
        }
        final Finishing finishing = finishing2;
        final Object notifyRootCause;
        synchronized (finishing3) {
            if (finishing.isCompleting) {
                return 0;
            }
            finishing.isCompleting = true;
            if (finishing != state && !JobSupport._state$FU.compareAndSet(this, state, finishing)) {
                return 3;
            }
            final boolean b;
            if (!(b = !finishing.isSealed())) {
                state = (Incomplete)"Failed requirement.";
                throw new IllegalArgumentException(state.toString());
            }
            final boolean wasCancelling = finishing.isCancelling();
            Object o = proposedUpdate;
            if (!(proposedUpdate instanceof CompletedExceptionally)) {
                o = null;
            }
            final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
            if (completedExceptionally != null) {
                final CompletedExceptionally it = completedExceptionally;
                finishing.addExceptionLocked(it.cause);
            }
            final Throwable rootCause = finishing.rootCause;
            final Throwable t;
            notifyRootCause = (t = (wasCancelling ? null : rootCause));
            final Unit instance = Unit.INSTANCE;
        }
        final Object o2 = notifyRootCause;
        if (o2 != null) {
            final Throwable it2 = (Throwable)o2;
            this.notifyCancelling(list, it2);
        }
        final Incomplete incomplete2 = state;
        Incomplete incomplete3;
        if (!((incomplete3 = incomplete2) instanceof ChildHandleNode)) {
            incomplete3 = null;
        }
        ChildHandleNode childHandleNode2;
        ChildHandleNode childHandleNode;
        if ((childHandleNode = (childHandleNode2 = (ChildHandleNode)incomplete3)) == null) {
            final NodeList list2 = incomplete2.getList();
            childHandleNode2 = (childHandleNode = ((list2 != null) ? nextChild(list2) : null));
        }
        final ChildHandleNode child = childHandleNode;
        if (childHandleNode2 != null && this.tryWaitForChild(finishing, child, proposedUpdate)) {
            return 2;
        }
        this.tryFinalizeFinishingState(finishing, proposedUpdate, mode);
        return 1;
    }
    
    private final boolean tryWaitForChild(final Finishing state, final ChildHandleNode child, final Object proposedUpdate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        kotlinx/coroutines/ChildHandleNode.childJob:Lkotlinx/coroutines/ChildJob;
        //     4: new             Lkotlinx/coroutines/JobSupport$ChildCompletion;
        //     7: dup            
        //     8: aload_0         /* this */
        //     9: aload_1         /* state */
        //    10: aload_2         /* child */
        //    11: aload_3         /* proposedUpdate */
        //    12: invokespecial   kotlinx/coroutines/JobSupport$ChildCompletion.<init>:(Lkotlinx/coroutines/JobSupport;Lkotlinx/coroutines/JobSupport$Finishing;Lkotlinx/coroutines/ChildHandleNode;Ljava/lang/Object;)V
        //    15: checkcast       Lkotlinx/coroutines/CompletionHandlerBase;
        //    18: astore          4
        //    20: astore          5
        //    22: aload           $this$asHandler$iv
        //    24: checkcast       Lkotlin/jvm/functions/Function1;
        //    27: astore          4
        //    29: aload           5
        //    31: iconst_0       
        //    32: iconst_0       
        //    33: aload           4
        //    35: iconst_1       
        //    36: aconst_null    
        //    37: invokestatic    kotlinx/coroutines/Job$DefaultImpls.invokeOnCompletion$default:(Lkotlinx/coroutines/Job;ZZLkotlin/jvm/functions/Function1;ILjava/lang/Object;)Lkotlinx/coroutines/DisposableHandle;
        //    40: dup            
        //    41: astore          4
        //    43: getstatic       kotlinx/coroutines/NonDisposableHandle.INSTANCE:Lkotlinx/coroutines/NonDisposableHandle;
        //    46: if_acmpeq       51
        //    49: iconst_1       
        //    50: ireturn        
        //    51: aload_2         /* child */
        //    52: checkcast       Lkotlinx/coroutines/internal/LockFreeLinkedListNode;
        //    55: invokestatic    kotlinx/coroutines/JobSupport.nextChild:(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Lkotlinx/coroutines/ChildHandleNode;
        //    58: dup            
        //    59: ifnonnull       65
        //    62: pop            
        //    63: iconst_0       
        //    64: ireturn        
        //    65: dup            
        //    66: astore          4
        //    68: astore_2        /* child */
        //    69: goto            0
        //    StackMapTable: 00 03 00 32 FF 00 0D 00 04 07 00 50 07 00 52 00 07 00 2D 00 01 07 00 40
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
    
    private static ChildHandleNode nextChild(LockFreeLinkedListNode $this$nextChild) {
        for ($this$nextChild = $this$nextChild; $this$nextChild.isRemoved(); $this$nextChild = $this$nextChild.getPrevNode()) {}
        while (true) {
            if (!($this$nextChild = $this$nextChild.getNextNode()).isRemoved()) {
                if ($this$nextChild instanceof ChildHandleNode) {
                    return (ChildHandleNode)$this$nextChild;
                }
                if ($this$nextChild instanceof NodeList) {
                    return null;
                }
                continue;
            }
        }
    }
    
    @Override
    public final ChildHandle attachChild(final ChildJob child) {
        Intrinsics.checkParameterIsNotNull(child, "child");
        final ChildHandleNode childHandleNode = new ChildHandleNode(this, child);
        final DisposableHandle invokeOnCompletion$default = DefaultImpls.invokeOnCompletion$default(this, true, false, (Function1)child, 2, null);
        if (invokeOnCompletion$default == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ChildHandle");
        }
        return (ChildHandle)invokeOnCompletion$default;
    }
    
    public void handleOnCompletionException$kotlinx_coroutines_core(final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        throw exception;
    }
    
    protected boolean isScopedCoroutine() {
        return false;
    }
    
    protected void onCompletionInternal(final Object state) {
    }
    
    protected void afterCompletionInternal$4cfcfd12() {
    }
    
    @Override
    public String toString() {
        return this.nameString$kotlinx_coroutines_core() + '{' + stateString(this.getState$kotlinx_coroutines_core()) + '}' + '@' + Pixmap.getHexAddress(this);
    }
    
    public String nameString$kotlinx_coroutines_core() {
        return Pixmap.getClassSimpleName(this);
    }
    
    private static String stateString(final Object state) {
        if (state instanceof Finishing) {
            if (((Finishing)state).isCancelling()) {
                return "Cancelling";
            }
            if (((Finishing)state).isCompleting) {
                return "Completing";
            }
            return "Active";
        }
        else if (state instanceof Incomplete) {
            if (((Incomplete)state).isActive()) {
                return "Active";
            }
            return "New";
        }
        else {
            if (state instanceof CompletedExceptionally) {
                return "Cancelled";
            }
            return "Completed";
        }
    }
    
    public JobSupport(final boolean active) {
        this._state = (active ? JobSupportKt.access$getEMPTY_ACTIVE$p() : JobSupportKt.access$getEMPTY_NEW$p());
    }
    
    @Override
    public final CoroutineContext plus(final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(context, "context");
        return CoroutineContext.Element.DefaultImpls.plus((CoroutineContext.Element)this, context);
    }
    
    @Override
    public final <R> R fold(final R initial, final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return CoroutineContext.Element.DefaultImpls.fold((CoroutineContext.Element)this, initial, operation);
    }
    
    @Override
    public final <E extends Element> E get(final CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        Intrinsics.checkParameterIsNotNull(key, "key");
        return CoroutineContext.Element.DefaultImpls.get((CoroutineContext.Element)this, key);
    }
    
    @Override
    public final CoroutineContext minusKey(final CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        Intrinsics.checkParameterIsNotNull(key, "key");
        return CoroutineContext.Element.DefaultImpls.minusKey((CoroutineContext.Element)this, key);
    }
    
    public static final /* synthetic */ void access$continueCompleting(JobSupport $this, Finishing state, ChildHandleNode lastChild, Object proposedUpdate) {
        proposedUpdate = proposedUpdate;
        lastChild = lastChild;
        state = state;
        $this = $this;
        final boolean b;
        if (!(b = ($this.getState$kotlinx_coroutines_core() == state))) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        final ChildHandleNode nextChild;
        if ((nextChild = nextChild(lastChild)) == null || !$this.tryWaitForChild(state, nextChild, proposedUpdate)) {
            $this.tryFinalizeFinishingState(state, proposedUpdate, 0);
        }
    }
    
    static {
        _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
    }
    
    static final class Finishing implements Incomplete
    {
        private volatile Object _exceptionsHolder;
        private final NodeList list;
        public volatile boolean isCompleting;
        public volatile Throwable rootCause;
        
        public final boolean isSealed() {
            return this._exceptionsHolder == JobSupportKt.access$getSEALED$p$55cb9d62();
        }
        
        public final boolean isCancelling() {
            return this.rootCause != null;
        }
        
        @Override
        public final boolean isActive() {
            return this.rootCause == null;
        }
        
        public final List<Throwable> sealLocked(final Throwable proposedException) {
            final Object eh;
            ArrayList<Throwable> allocateList;
            if ((eh = this._exceptionsHolder) == null) {
                allocateList = allocateList();
            }
            else if (eh instanceof Throwable) {
                final ArrayList<Throwable> allocateList2;
                (allocateList2 = allocateList()).add((Throwable)eh);
                allocateList = allocateList2;
            }
            else {
                if (!(eh instanceof ArrayList)) {
                    throw new IllegalStateException(("State is " + eh).toString());
                }
                allocateList = (ArrayList<Throwable>)eh;
            }
            final ArrayList list = allocateList;
            final Throwable rootCause;
            final Throwable t = rootCause = this.rootCause;
            if (t != null) {
                final Throwable it = t;
                list.add(0, it);
            }
            if (proposedException != null && (Intrinsics.areEqual(proposedException, rootCause) ^ true)) {
                list.add(proposedException);
            }
            this._exceptionsHolder = JobSupportKt.access$getSEALED$p$55cb9d62();
            return (List<Throwable>)list;
        }
        
        public final void addExceptionLocked(Throwable exception) {
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            final Throwable rootCause;
            if ((rootCause = this.rootCause) == null) {
                this.rootCause = exception;
                return;
            }
            if (exception == rootCause) {
                return;
            }
            final Object eh;
            if ((eh = this._exceptionsHolder) == null) {
                this._exceptionsHolder = exception;
                return;
            }
            if (eh instanceof Throwable) {
                if (exception == eh) {
                    return;
                }
                final Serializable allocateList = allocateList();
                final ArrayList $this$apply;
                ($this$apply = (ArrayList)allocateList).add(eh);
                $this$apply.add(exception);
                exception = (Throwable)allocateList;
                this._exceptionsHolder = exception;
            }
            else {
                if (eh instanceof ArrayList) {
                    ((ArrayList)eh).add(exception);
                    return;
                }
                throw new IllegalStateException(("State is " + eh).toString());
            }
        }
        
        private static ArrayList<Throwable> allocateList() {
            return new ArrayList<Throwable>(4);
        }
        
        @Override
        public final String toString() {
            return "Finishing[cancelling=" + this.isCancelling() + ", completing=" + this.isCompleting + ", rootCause=" + this.rootCause + ", exceptions=" + this._exceptionsHolder + ", list=" + this.list + ']';
        }
        
        @Override
        public final NodeList getList() {
            return this.list;
        }
        
        public Finishing(final NodeList list, final boolean isCompleting, final Throwable rootCause) {
            Intrinsics.checkParameterIsNotNull(list, "list");
            this.list = list;
            this.isCompleting = false;
            this.rootCause = rootCause;
        }
    }
    
    static final class ChildCompletion extends JobNode<Job>
    {
        private final JobSupport parent;
        private final Finishing state;
        private final ChildHandleNode child;
        private final Object proposedUpdate;
        
        @Override
        public final void invoke(final Throwable cause) {
            JobSupport.access$continueCompleting(this.parent, this.state, this.child, this.proposedUpdate);
        }
        
        @Override
        public final String toString() {
            return "ChildCompletion[" + this.child + ", " + this.proposedUpdate + ']';
        }
        
        public ChildCompletion(final JobSupport parent, final Finishing state, final ChildHandleNode child, final Object proposedUpdate) {
            Intrinsics.checkParameterIsNotNull(parent, "parent");
            Intrinsics.checkParameterIsNotNull(state, "state");
            Intrinsics.checkParameterIsNotNull(child, "child");
            super(child.childJob);
            this.parent = parent;
            this.state = state;
            this.child = child;
            this.proposedUpdate = proposedUpdate;
        }
    }
}
