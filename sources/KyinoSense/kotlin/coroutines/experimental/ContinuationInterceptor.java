/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines.experimental;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u0000 \u00062\u00020\u0001:\u0001\u0006J\"\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003H&\u00a8\u0006\u0007"}, d2={"Lkotlin/coroutines/experimental/ContinuationInterceptor;", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "interceptContinuation", "Lkotlin/coroutines/experimental/Continuation;", "T", "continuation", "Key", "kotlin-stdlib-coroutines"})
@SinceKotlin(version="1.1")
public interface ContinuationInterceptor
extends CoroutineContext.Element {
    public static final Key Key = kotlin.coroutines.experimental.ContinuationInterceptor$Key.$$INSTANCE;

    @NotNull
    public <T> Continuation<T> interceptContinuation(@NotNull Continuation<? super T> var1);

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=3)
    public static final class DefaultImpls {
        @Nullable
        public static <E extends CoroutineContext.Element> E get(ContinuationInterceptor $this, @NotNull CoroutineContext.Key<E> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return CoroutineContext.Element.DefaultImpls.get($this, key);
        }

        public static <R> R fold(ContinuationInterceptor $this, R initial, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
            Intrinsics.checkParameterIsNotNull(operation, "operation");
            return CoroutineContext.Element.DefaultImpls.fold($this, initial, operation);
        }

        @NotNull
        public static CoroutineContext minusKey(ContinuationInterceptor $this, @NotNull CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return CoroutineContext.Element.DefaultImpls.minusKey($this, key);
        }

        @NotNull
        public static CoroutineContext plus(ContinuationInterceptor $this, @NotNull CoroutineContext context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return CoroutineContext.Element.DefaultImpls.plus($this, context);
        }
    }

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2={"Lkotlin/coroutines/experimental/ContinuationInterceptor$Key;", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "()V", "kotlin-stdlib-coroutines"})
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

