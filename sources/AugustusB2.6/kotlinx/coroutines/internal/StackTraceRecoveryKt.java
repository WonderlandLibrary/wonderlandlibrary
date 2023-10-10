// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlin.Result;
import kotlin.Pair;
import java.util.ArrayDeque;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlinx.coroutines.DebugKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;

public final class StackTraceRecoveryKt
{
    private static final String baseContinuationImplClassName;
    
    public static final <E extends Throwable> E recoverStackTrace(E exception, final Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        if (!DebugKt.getRECOVER_STACK_TRACES() || !(continuation instanceof CoroutineStackFrame)) {
            return exception;
        }
        final CoroutineStackFrame coroutineStackFrame = (CoroutineStackFrame)continuation;
        exception = exception;
        Pair<E, StackTraceElement[]> to = null;
        Pair<E, StackTraceElement[]> pair = null;
        Label_0167: {
            final Throwable cause;
            if ((cause = exception.getCause()) != null && Intrinsics.areEqual(cause.getClass(), exception.getClass())) {
                final StackTraceElement[] stackTrace;
                final StackTraceElement[] value = stackTrace = exception.getStackTrace();
                Intrinsics.checkExpressionValueIsNotNull(value, "currentTrace");
                final StackTraceElement[] array = value;
                while (true) {
                    for (int length = value.length, i = 0; i < length; ++i) {
                        final StackTraceElement stackTraceElement = array[i];
                        Intrinsics.checkExpressionValueIsNotNull(stackTraceElement, "it");
                        if (isArtificial(stackTraceElement)) {
                            final boolean b = true;
                            pair = (to = (b ? Pixmap.to(cause, stackTrace) : Pixmap.to(exception, new StackTraceElement[0])));
                            break Label_0167;
                        }
                    }
                    final boolean b = false;
                    continue;
                }
            }
            pair = (to = Pixmap.to(exception, new StackTraceElement[0]));
        }
        final Pair<E, StackTraceElement[]> pair2 = to;
        final Throwable t = pair.component1();
        final StackTraceElement[] recoveredStacktrace = pair2.component2();
        final Throwable tryCopyException = ExceptionsConstuctorKt.tryCopyException(t);
        if (tryCopyException == null) {
            return exception;
        }
        final Throwable result = tryCopyException;
        final CoroutineStackFrame coroutineStackFrame2 = coroutineStackFrame;
        final ArrayDeque<StackTraceElement> arrayDeque = new ArrayDeque<StackTraceElement>();
        final StackTraceElement stackTraceElement2 = coroutineStackFrame2.getStackTraceElement();
        if (stackTraceElement2 != null) {
            arrayDeque.add(stackTraceElement2);
        }
        CoroutineStackFrame coroutineStackFrame3 = coroutineStackFrame2;
        while (true) {
            CoroutineStackFrame coroutineStackFrame5;
            CoroutineStackFrame coroutineStackFrame4;
            if (!((coroutineStackFrame4 = (coroutineStackFrame5 = coroutineStackFrame3)) instanceof CoroutineStackFrame)) {
                coroutineStackFrame5 = (coroutineStackFrame4 = null);
            }
            if (coroutineStackFrame4 == null) {
                break;
            }
            final CoroutineStackFrame callerFrame = coroutineStackFrame5.getCallerFrame();
            if (callerFrame == null) {
                break;
            }
            coroutineStackFrame3 = callerFrame;
            final StackTraceElement stackTraceElement3 = callerFrame.getStackTraceElement();
            if (stackTraceElement3 == null) {
                continue;
            }
            arrayDeque.add(stackTraceElement3);
        }
        final ArrayDeque<StackTraceElement> arrayDeque2;
        if ((arrayDeque2 = arrayDeque).isEmpty()) {
            return exception;
        }
        if (t != exception) {
            mergeRecoveredTraces(recoveredStacktrace, arrayDeque2);
        }
        return createFinalException(t, result, arrayDeque2);
    }
    
    private static final <E extends Throwable> E createFinalException(final E cause, final E result, final ArrayDeque<StackTraceElement> resultStackTrace) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "Coroutine boundary"
        //     3: invokestatic    kotlinx/coroutines/internal/StackTraceRecoveryKt.artificialFrame:(Ljava/lang/String;)Ljava/lang/StackTraceElement;
        //     6: invokevirtual   java/util/ArrayDeque.addFirst:(Ljava/lang/Object;)V
        //     9: aload_0         /* cause */
        //    10: invokevirtual   java/lang/Throwable.getStackTrace:()[Ljava/lang/StackTraceElement;
        //    13: dup            
        //    14: astore_0        /* causeTrace */
        //    15: dup            
        //    16: ldc             "causeTrace"
        //    18: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    21: getstatic       kotlinx/coroutines/internal/StackTraceRecoveryKt.baseContinuationImplClassName:Ljava/lang/String;
        //    24: dup            
        //    25: ldc             "baseContinuationImplClassName"
        //    27: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    30: invokestatic    kotlinx/coroutines/internal/StackTraceRecoveryKt.frameIndex:([Ljava/lang/StackTraceElement;Ljava/lang/String;)I
        //    33: dup            
        //    34: istore_3        /* size */
        //    35: iconst_m1      
        //    36: if_icmpne       86
        //    39: aload_1         /* result */
        //    40: aload_2         /* resultStackTrace */
        //    41: checkcast       Ljava/util/Collection;
        //    44: astore          4
        //    46: astore_0        /* causeTrace */
        //    47: aload           $this$toTypedArray$iv
        //    49: dup            
        //    50: astore          6
        //    52: iconst_0       
        //    53: anewarray       Ljava/lang/StackTraceElement;
        //    56: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    61: dup            
        //    62: ifnonnull       75
        //    65: new             Lkotlin/TypeCastException;
        //    68: dup            
        //    69: ldc             "null cannot be cast to non-null type kotlin.Array<T>"
        //    71: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //    74: athrow         
        //    75: astore_2        /* resultStackTrace */
        //    76: aload_0         /* causeTrace */
        //    77: aload_2         /* resultStackTrace */
        //    78: checkcast       [Ljava/lang/StackTraceElement;
        //    81: invokevirtual   java/lang/Throwable.setStackTrace:([Ljava/lang/StackTraceElement;)V
        //    84: aload_1         /* result */
        //    85: areturn        
        //    86: aload_2         /* resultStackTrace */
        //    87: invokevirtual   java/util/ArrayDeque.size:()I
        //    90: iload_3         /* size */
        //    91: iadd           
        //    92: anewarray       Ljava/lang/StackTraceElement;
        //    95: astore          mergedStackTrace
        //    97: iconst_0       
        //    98: istore          5
        //   100: iload_3         /* size */
        //   101: istore          6
        //   103: iload           5
        //   105: iload           6
        //   107: if_icmpge       125
        //   110: aload           mergedStackTrace
        //   112: iload           i
        //   114: aload_0         /* causeTrace */
        //   115: iload           i
        //   117: aaload         
        //   118: aastore        
        //   119: iinc            i, 1
        //   122: goto            103
        //   125: iconst_0       
        //   126: istore          5
        //   128: aload_2         /* resultStackTrace */
        //   129: checkcast       Ljava/lang/Iterable;
        //   132: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   137: astore_0       
        //   138: aload_0        
        //   139: invokeinterface java/util/Iterator.hasNext:()Z
        //   144: ifeq            173
        //   147: aload_0        
        //   148: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   153: checkcast       Ljava/lang/StackTraceElement;
        //   156: astore          element
        //   158: aload           mergedStackTrace
        //   160: iload_3         /* size */
        //   161: iload           index
        //   163: iadd           
        //   164: aload           element
        //   166: aastore        
        //   167: iinc            index, 1
        //   170: goto            138
        //   173: aload_1         /* result */
        //   174: aload           mergedStackTrace
        //   176: invokevirtual   java/lang/Throwable.setStackTrace:([Ljava/lang/StackTraceElement;)V
        //   179: aload_1         /* result */
        //   180: areturn        
        //    Signature:
        //  <E:Ljava/lang/Throwable;>(TE;TE;Ljava/util/ArrayDeque<Ljava/lang/StackTraceElement;>;)TE;
        //    StackMapTable: 00 06 FF 00 4B 00 02 07 00 1E 07 00 1E 00 01 07 00 15 FF 00 0A 00 04 07 00 16 07 00 1E 07 00 1F 01 00 00 FE 00 10 07 00 16 01 01 FF 00 15 00 05 00 07 00 1E 07 00 1F 01 07 00 16 00 00 FF 00 0C 00 06 07 00 21 07 00 1E 00 01 07 00 16 01 00 00 FF 00 22 00 05 00 07 00 1E 00 00 07 00 16 00 00
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
    
    private static final void mergeRecoveredTraces(final StackTraceElement[] recoveredStacktrace, final ArrayDeque<StackTraceElement> result) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3        /* $this$indexOfFirst$iv */
        //     2: iconst_0       
        //     3: istore_2       
        //     4: aload_3         /* $this$indexOfFirst$iv */
        //     5: arraylength    
        //     6: istore          4
        //     8: iload_2        
        //     9: iload           4
        //    11: if_icmpge       36
        //    14: aload_3         /* $this$indexOfFirst$iv */
        //    15: iload_2         /* index$iv */
        //    16: aaload         
        //    17: dup            
        //    18: astore          5
        //    20: invokestatic    kotlinx/coroutines/internal/StackTraceRecoveryKt.isArtificial:(Ljava/lang/StackTraceElement;)Z
        //    23: ifeq            30
        //    26: iload_2         /* index$iv */
        //    27: goto            37
        //    30: iinc            index$iv, 1
        //    33: goto            8
        //    36: iconst_m1      
        //    37: iconst_1       
        //    38: iadd           
        //    39: istore_2        /* startIndex */
        //    40: aload_0         /* recoveredStacktrace */
        //    41: arraylength    
        //    42: iconst_1       
        //    43: isub           
        //    44: dup            
        //    45: istore_3       
        //    46: istore_3       
        //    47: iload_2         /* startIndex */
        //    48: istore_2        /* startIndex */
        //    49: iload_3        
        //    50: iload_2        
        //    51: if_icmplt       102
        //    54: aload_0         /* recoveredStacktrace */
        //    55: iload_3         /* i */
        //    56: aaload         
        //    57: dup            
        //    58: astore          4
        //    60: aload_1         /* result */
        //    61: invokevirtual   java/util/ArrayDeque.getLast:()Ljava/lang/Object;
        //    64: dup            
        //    65: ldc             "result.last"
        //    67: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    70: checkcast       Ljava/lang/StackTraceElement;
        //    73: invokestatic    kotlinx/coroutines/internal/StackTraceRecoveryKt.elementWiseEquals:(Ljava/lang/StackTraceElement;Ljava/lang/StackTraceElement;)Z
        //    76: ifeq            84
        //    79: aload_1         /* result */
        //    80: invokevirtual   java/util/ArrayDeque.removeLast:()Ljava/lang/Object;
        //    83: pop            
        //    84: aload_1         /* result */
        //    85: aload_0         /* recoveredStacktrace */
        //    86: iload_3         /* i */
        //    87: aaload         
        //    88: invokevirtual   java/util/ArrayDeque.addFirst:(Ljava/lang/Object;)V
        //    91: iload_3         /* i */
        //    92: iload_2        
        //    93: if_icmpeq       102
        //    96: iinc            i, -1
        //    99: goto            54
        //   102: return         
        //    Signature:
        //  ([Ljava/lang/StackTraceElement;Ljava/util/ArrayDeque<Ljava/lang/StackTraceElement;>;)V
        //    StackMapTable: 00 07 FE 00 08 01 07 00 16 01 15 F8 00 05 40 01 FD 00 10 01 01 1D FF 00 11 00 00 00 00
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
    
    public static final <E extends Throwable> E unwrap(final E exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (!DebugKt.getRECOVER_STACK_TRACES()) {
            return exception;
        }
        final Throwable cause;
        if ((cause = exception.getCause()) == null || (Intrinsics.areEqual(cause.getClass(), exception.getClass()) ^ true)) {
            return exception;
        }
        final StackTraceElement[] stackTrace = exception.getStackTrace();
        Intrinsics.checkExpressionValueIsNotNull(stackTrace, "exception.stackTrace");
        final StackTraceElement[] array = stackTrace;
        final int length = stackTrace.length;
        int i = 0;
        while (true) {
            while (i < length) {
                final StackTraceElement stackTraceElement = array[i];
                Intrinsics.checkExpressionValueIsNotNull(stackTraceElement, "it");
                if (isArtificial(stackTraceElement)) {
                    final boolean b = true;
                    if (b) {
                        return (E)cause;
                    }
                    return exception;
                }
                else {
                    ++i;
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    private static StackTraceElement artificialFrame(final String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        return new StackTraceElement("\b\b\b(" + message, "\b", "\b", -1);
    }
    
    private static boolean isArtificial(final StackTraceElement $this$isArtificial) {
        Intrinsics.checkParameterIsNotNull($this$isArtificial, "$this$isArtificial");
        final String className = $this$isArtificial.getClassName();
        Intrinsics.checkExpressionValueIsNotNull(className, "className");
        return StringsKt__StringNumberConversionsKt.startsWith$default$3705f858(className, "\b\b\b", false, 2);
    }
    
    private static final int frameIndex(final StackTraceElement[] $this$frameIndex, final String methodName) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_0        /* $this$frameIndex */
        //     2: iconst_0       
        //     3: istore_2       
        //     4: aload_0         /* $this$indexOfFirst$iv */
        //     5: arraylength    
        //     6: istore_3       
        //     7: iload_2        
        //     8: iload_3        
        //     9: if_icmpge       37
        //    12: aload_0         /* $this$indexOfFirst$iv */
        //    13: iload_2         /* index$iv */
        //    14: aaload         
        //    15: astore          it
        //    17: aload_1         /* methodName */
        //    18: aload           it
        //    20: invokevirtual   java/lang/StackTraceElement.getClassName:()Ljava/lang/String;
        //    23: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //    26: ifeq            31
        //    29: iload_2         /* index$iv */
        //    30: ireturn        
        //    31: iinc            index$iv, 1
        //    34: goto            7
        //    37: iconst_m1      
        //    38: ireturn        
        //    StackMapTable: 00 03 FD 00 07 01 01 17 FF 00 05 00 00 00 00
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
    
    private static final boolean elementWiseEquals(final StackTraceElement $this$elementWiseEquals, final StackTraceElement e) {
        return $this$elementWiseEquals.getLineNumber() == e.getLineNumber() && Intrinsics.areEqual($this$elementWiseEquals.getMethodName(), e.getMethodName()) && Intrinsics.areEqual($this$elementWiseEquals.getFileName(), e.getFileName()) && Intrinsics.areEqual($this$elementWiseEquals.getClassName(), e.getClassName());
    }
    
    static {
        Object o;
        try {
            final Result.Companion companion = Result.Companion;
            final Class<?> forName = Class.forName("kotlin.coroutines.jvm.internal.BaseContinuationImpl");
            Intrinsics.checkExpressionValueIsNotNull(forName, "Class.forName(baseContinuationImplClass)");
            o = Result.constructor-impl(forName.getCanonicalName());
        }
        catch (Throwable exception) {
            final Result.Companion companion2 = Result.Companion;
            o = Result.constructor-impl(Pixmap.createFailure(exception));
        }
        final String s;
        baseContinuationImplClassName = ((Result.exceptionOrNull-impl(s = (String)o) == null) ? s : "kotlin.coroutines.jvm.internal.BaseContinuationImpl");
        Object $this;
        try {
            final Result.Companion companion3 = Result.Companion;
            final Class<?> forName2 = Class.forName("kotlinx.coroutines.internal.StackTraceRecoveryKt");
            Intrinsics.checkExpressionValueIsNotNull(forName2, "Class.forName(stackTraceRecoveryClass)");
            $this = Result.constructor-impl(forName2.getCanonicalName());
        }
        catch (Throwable exception2) {
            final Result.Companion companion4 = Result.Companion;
            $this = Result.constructor-impl(Pixmap.createFailure(exception2));
        }
        Result.exceptionOrNull-impl($this);
    }
}
