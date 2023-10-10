// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.ContinuationInterceptor;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.coroutines.CoroutineContext;
import kotlin.TypeCastException;
import kotlin.collections.EmptyList;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import java.util.List;
import kotlin.coroutines.Continuation;
import java.util.Collection;

public class AwaitKt
{
    public static final <T> Object awaitAll(final Collection<? extends Deferred<? extends T>> $this$awaitAll, final Continuation<? super List<? extends T>> $completion) {
        final AwaitKt$awaitAll.AwaitKt$awaitAll$2 awaitKt$awaitAll$2;
        Continuation $continuation = null;
        if ($completion instanceof AwaitKt$awaitAll.AwaitKt$awaitAll$2 && ((awaitKt$awaitAll$2 = (AwaitKt$awaitAll.AwaitKt$awaitAll$2)$completion).label & Integer.MIN_VALUE) != 0x0) {
            final AwaitKt$awaitAll.AwaitKt$awaitAll$2 awaitKt$awaitAll$3 = awaitKt$awaitAll$2;
            awaitKt$awaitAll$3.label -= Integer.MIN_VALUE;
        }
        else {
            $continuation = (Continuation)new AwaitKt$awaitAll.AwaitKt$awaitAll$2((Continuation)$completion);
        }
        Object $result = ((AwaitKt$awaitAll.AwaitKt$awaitAll$2)$continuation).result;
        final CoroutineSingletons coroutine_SUSPENDED = CoroutineSingletons.COROUTINE_SUSPENDED;
        Object o = null;
        switch (((AwaitKt$awaitAll.AwaitKt$awaitAll$2)$continuation).label) {
            case 0: {
                Pixmap.throwOnFailure($result);
                if ($this$awaitAll.isEmpty()) {
                    o = EmptyList.INSTANCE;
                    break;
                }
                $result = $this$awaitAll;
                $result = $this$awaitAll;
                final Deferred[] array = $this$awaitAll.toArray(new Deferred[0]);
                if (array == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                $result = array;
                $result = array;
                final AwaitAll awaitAll = new AwaitAll((Deferred[])$result);
                final Continuation $completion2 = $continuation;
                ((AwaitKt$awaitAll.AwaitKt$awaitAll$2)$completion2).L$0 = $this$awaitAll;
                ((AwaitKt$awaitAll.AwaitKt$awaitAll$2)$continuation).label = 1;
                if ((o = awaitAll.await($completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                Pixmap.throwOnFailure($result);
                o = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        return o;
    }
    
    public static void handleCoroutineException(final CoroutineContext context, final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        try {
            if (context.get((CoroutineContext.Key<CoroutineContext.Element>)CoroutineExceptionHandler.Key) != null) {
                return;
            }
        }
        catch (Throwable t) {
            CoroutineExceptionHandlerImplKt.handleCoroutineExceptionImpl(context, handlerException(exception, t));
            return;
        }
        CoroutineExceptionHandlerImplKt.handleCoroutineExceptionImpl(context, exception);
    }
    
    public static Throwable handlerException(final Throwable originalException, final Throwable thrownException) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "originalException"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* thrownException */
        //     7: ldc             "thrownException"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* originalException */
        //    13: aload_1         /* thrownException */
        //    14: if_acmpne       19
        //    17: aload_0         /* originalException */
        //    18: areturn        
        //    19: new             Ljava/lang/RuntimeException;
        //    22: dup            
        //    23: ldc             "Exception while trying to handle coroutine exception"
        //    25: aload_1         /* thrownException */
        //    26: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    29: dup            
        //    30: astore_1       
        //    31: dup            
        //    32: astore_2       
        //    33: checkcast       Ljava/lang/Throwable;
        //    36: astore_2       
        //    37: aload_0         /* originalException */
        //    38: astore_0        /* originalException */
        //    39: aload_2         /* $this$addSuppressedThrowable$iv */
        //    40: aload_0         /* other$iv */
        //    41: invokestatic    kotlin/ExceptionsKt__ExceptionsKt.addSuppressed:(Ljava/lang/Throwable;Ljava/lang/Throwable;)V
        //    44: aload_1        
        //    45: checkcast       Ljava/lang/Throwable;
        //    48: areturn        
        //    StackMapTable: 00 01 13
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
}
