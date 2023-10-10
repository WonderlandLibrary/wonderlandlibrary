// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.intrinsics;

import com.badlogic.gdx.graphics.Pixmap;
import kotlin.Result;
import kotlinx.coroutines.DispatchedKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;

public class CancellableKt
{
    public static final <R, T> void startCoroutineCancellable(final Function2<? super R, ? super Continuation<? super T>, ?> $this$startCoroutineCancellable, final R receiver, final Continuation<? super T> completion) {
        Intrinsics.checkParameterIsNotNull($this$startCoroutineCancellable, "$this$startCoroutineCancellable");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        try {
            DispatchedKt.resumeCancellable(IntrinsicsKt__IntrinsicsJvmKt.intercepted((Continuation<? super Unit>)IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted((Function2<? super R, ? super Continuation<? super Object>, ?>)$this$startCoroutineCancellable, receiver, (Continuation<? super Object>)completion)), Unit.INSTANCE);
        }
        catch (Throwable e$iv) {
            final Result.Companion companion = Result.Companion;
            completion.resumeWith(Result.constructor-impl(Pixmap.createFailure(e$iv)));
        }
    }
    
    public static <R, T> void startCoroutineUndispatched(final Function2<? super R, ? super Continuation<? super T>, ?> $this$startCoroutineUndispatched, final R receiver, final Continuation<? super T> completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "$this$startCoroutineUndispatched"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_2         /* completion */
        //     7: ldc             "completion"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_2         /* completion */
        //    13: dup            
        //    14: astore_3       
        //    15: ldc             "completion"
        //    17: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    20: aload_3        
        //    21: astore_3        /* actualCompletion$iv */
        //    22: aload_3         /* actualCompletion$iv */
        //    23: astore          actualCompletion
        //    25: aload_2         /* completion */
        //    26: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //    31: dup            
        //    32: astore_2        /* context$iv */
        //    33: aconst_null    
        //    34: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.updateThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)Ljava/lang/Object;
        //    37: astore          oldValue$iv
        //    39: aload_0         /* $this$startCoroutineUndispatched */
        //    40: astore_0        /* $this$startCoroutineUndispatched */
        //    41: aload_1         /* receiver */
        //    42: astore_1        /* receiver */
        //    43: aload           actualCompletion
        //    45: astore          null
        //    47: aload_0        
        //    48: iconst_2       
        //    49: invokestatic    kotlin/jvm/internal/TypeIntrinsics.beforeCheckcastToFunctionOfArity:(Ljava/lang/Object;I)Ljava/lang/Object;
        //    52: checkcast       Lkotlin/jvm/functions/Function2;
        //    55: aload_1        
        //    56: aload           4
        //    58: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    63: astore_0       
        //    64: aload_2         /* context$iv */
        //    65: aload           oldValue$iv
        //    67: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.restoreThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V
        //    70: aload_0        
        //    71: goto            83
        //    74: astore_0       
        //    75: aload_2         /* context$iv */
        //    76: aload           oldValue$iv
        //    78: invokestatic    kotlinx/coroutines/internal/ThreadContextKt.restoreThreadContext:(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V
        //    81: aload_0        
        //    82: athrow         
        //    83: astore_1       
        //    84: goto            114
        //    87: astore          e$iv
        //    89: aload_3         /* actualCompletion$iv */
        //    90: dup            
        //    91: astore_0       
        //    92: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //    95: pop            
        //    96: astore_2       
        //    97: aload           e$iv
        //    99: invokestatic    com/badlogic/gdx/graphics/Pixmap.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //   102: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   105: astore_0       
        //   106: aload_2        
        //   107: aload_0        
        //   108: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   113: return         
        //   114: aload_1        
        //   115: dup            
        //   116: astore_0        /* value$iv */
        //   117: getstatic       kotlin/coroutines/intrinsics/CoroutineSingletons.COROUTINE_SUSPENDED:Lkotlin/coroutines/intrinsics/CoroutineSingletons;
        //   120: if_acmpeq       147
        //   123: aload_3         /* actualCompletion$iv */
        //   124: astore_1       
        //   125: aload_0         /* value$iv */
        //   126: astore          4
        //   128: aload_1        
        //   129: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   132: pop            
        //   133: astore_0       
        //   134: aload           4
        //   136: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   139: astore_1       
        //   140: aload_0        
        //   141: aload_1        
        //   142: invokeinterface kotlin/coroutines/Continuation.resumeWith:(Ljava/lang/Object;)V
        //   147: return         
        //    Signature:
        //  <R:Ljava/lang/Object;T:Ljava/lang/Object;>(Lkotlin/jvm/functions/Function2<-TR;-Lkotlin/coroutines/Continuation<-TT;>;*>;TR;Lkotlin/coroutines/Continuation<-TT;>;)V [from metadata: <R:Ljava/lang/Object;T:Ljava/lang/Object;>(Lkotlin/jvm/functions/Function2<-TR;-Lkotlin/coroutines/Continuation<-TT;>;+Ljava/lang/Object;>;TR;Lkotlin/coroutines/Continuation<-TT;>;)V]
        //  
        //    StackMapTable: 00 05 FF 00 4A 00 06 00 00 07 00 0A 07 00 09 00 07 00 05 00 01 07 00 06 FF 00 08 00 04 00 00 00 07 00 09 00 01 07 00 05 43 07 00 06 FF 00 1A 00 04 00 07 00 05 00 07 00 09 00 00 FF 00 20 00 00 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  39     64     74     83     Any
        //  22     84     87     114    Ljava/lang/Throwable;
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
}
