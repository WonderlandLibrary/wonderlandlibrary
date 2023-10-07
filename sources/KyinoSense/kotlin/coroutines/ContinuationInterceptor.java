/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.TypeCastException;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bg\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fJ(\u0010\u0002\u001a\u0004\u0018\u0001H\u0003\"\b\b\u0000\u0010\u0003*\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0005H\u0096\u0002\u00a2\u0006\u0002\u0010\u0006J\"\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\bH&J\u0014\u0010\u000b\u001a\u00020\f2\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0014\u0010\r\u001a\u00020\u000e2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\bH\u0016\u00a8\u0006\u0010"}, d2={"Lkotlin/coroutines/ContinuationInterceptor;", "Lkotlin/coroutines/CoroutineContext$Element;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "interceptContinuation", "Lkotlin/coroutines/Continuation;", "T", "continuation", "minusKey", "Lkotlin/coroutines/CoroutineContext;", "releaseInterceptedContinuation", "", "Key", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
public interface ContinuationInterceptor
extends CoroutineContext.Element {
    public static final Key Key = kotlin.coroutines.ContinuationInterceptor$Key.$$INSTANCE;

    @NotNull
    public <T> Continuation<T> interceptContinuation(@NotNull Continuation<? super T> var1);

    public void releaseInterceptedContinuation(@NotNull Continuation<?> var1);

    @Override
    @Nullable
    public <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> var1);

    @Override
    @NotNull
    public CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> var1);

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=3)
    public static final class DefaultImpls {
        public static void releaseInterceptedContinuation(ContinuationInterceptor $this, @NotNull Continuation<?> continuation2) {
            Intrinsics.checkParameterIsNotNull(continuation2, "continuation");
        }

        @Nullable
        public static <E extends CoroutineContext.Element> E get(ContinuationInterceptor $this, @NotNull CoroutineContext.Key<E> key) {
            CoroutineContext.Element element;
            Intrinsics.checkParameterIsNotNull(key, "key");
            if (key == Key) {
                ContinuationInterceptor continuationInterceptor = $this;
                if (continuationInterceptor == null) {
                    throw new TypeCastException("null cannot be cast to non-null type E");
                }
                element = continuationInterceptor;
            } else {
                element = null;
            }
            return (E)element;
        }

        @NotNull
        public static CoroutineContext minusKey(ContinuationInterceptor $this, @NotNull CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return key == Key ? (CoroutineContext)EmptyCoroutineContext.INSTANCE : (CoroutineContext)$this;
        }

        public static <R> R fold(ContinuationInterceptor $this, R initial, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
            Intrinsics.checkParameterIsNotNull(operation, "operation");
            return CoroutineContext.Element.DefaultImpls.fold($this, initial, operation);
        }

        @NotNull
        public static CoroutineContext plus(ContinuationInterceptor $this, @NotNull CoroutineContext context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return CoroutineContext.Element.DefaultImpls.plus($this, context);
        }
    }

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2={"Lkotlin/coroutines/ContinuationInterceptor$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlin/coroutines/ContinuationInterceptor;", "()V", "kotlin-stdlib"})
    public static final class Key
    implements CoroutineContext.Key<ContinuationInterceptor> {
        static final /* synthetic */ Key $$INSTANCE;

        private Key() {
        }

        static {
            Key key;
            $$INSTANCE = key = new Key();
        }
    }
}

