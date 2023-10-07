/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.coroutines.experimental.migration;

import kotlin.Metadata;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0002\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\b"}, d2={"Lkotlin/coroutines/experimental/migration/ContextMigration;", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "(Lkotlin/coroutines/experimental/CoroutineContext;)V", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "Key", "kotlin-stdlib-coroutines"})
final class ContextMigration
extends AbstractCoroutineContextElement {
    @NotNull
    private final CoroutineContext context;
    public static final Key Key = new Key(null);

    @NotNull
    public final CoroutineContext getContext() {
        return this.context;
    }

    public ContextMigration(@NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(Key);
        this.context = context;
    }

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2={"Lkotlin/coroutines/experimental/migration/ContextMigration$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlin/coroutines/experimental/migration/ContextMigration;", "()V", "kotlin-stdlib-coroutines"})
    public static final class Key
    implements CoroutineContext.Key<ContextMigration> {
        private Key() {
        }

        public /* synthetic */ Key(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

