/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines.experimental;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.EmptyCoroutineContext;
import kotlin.coroutines.experimental.SequenceBuilder;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000e\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u00020\u00050\u0004B\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\t\u0010\u0018\u001a\u00020\u0019H\u0096\u0002J\u000e\u0010\u001a\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010\u001bJ\r\u0010\u001c\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u0010\u001bJ\u0015\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u0005H\u0016\u00a2\u0006\u0002\u0010\u001fJ\u0010\u0010 \u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u0017H\u0016J\u0019\u0010\"\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00028\u0000H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u001f\u0010$\u001a\u00020\u00052\f\u0010%\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010&R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0011\u001a\u0004\u0018\u00018\u0000X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0012R\u0012\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\t\u00a8\u0006'"}, d2={"Lkotlin/coroutines/experimental/SequenceBuilderIterator;", "T", "Lkotlin/coroutines/experimental/SequenceBuilder;", "", "Lkotlin/coroutines/experimental/Continuation;", "", "()V", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "nextIterator", "nextStep", "getNextStep", "()Lkotlin/coroutines/experimental/Continuation;", "setNextStep", "(Lkotlin/coroutines/experimental/Continuation;)V", "nextValue", "Ljava/lang/Object;", "state", "", "Lkotlin/coroutines/experimental/State;", "exceptionalState", "", "hasNext", "", "next", "()Ljava/lang/Object;", "nextNotReady", "resume", "value", "(Lkotlin/Unit;)V", "resumeWithException", "exception", "yield", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "yieldAll", "iterator", "(Ljava/util/Iterator;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlin-stdlib-coroutines"})
final class SequenceBuilderIterator<T>
extends SequenceBuilder<T>
implements Iterator<T>,
Continuation<Unit>,
KMappedMarker {
    private int state;
    private T nextValue;
    private Iterator<? extends T> nextIterator;
    @Nullable
    private Continuation<? super Unit> nextStep;

    @Nullable
    public final Continuation<Unit> getNextStep() {
        return this.nextStep;
    }

    public final void setNextStep(@Nullable Continuation<? super Unit> continuation2) {
        this.nextStep = continuation2;
    }

    @Override
    public boolean hasNext() {
        while (true) {
            Continuation<? super Unit> step;
            switch (this.state) {
                case 0: {
                    break;
                }
                case 1: {
                    Iterator<T> iterator2 = this.nextIterator;
                    if (iterator2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iterator2.hasNext()) {
                        this.state = 2;
                        return true;
                    }
                    this.nextIterator = null;
                    break;
                }
                case 4: {
                    return false;
                }
                case 2: 
                case 3: {
                    return true;
                }
                default: {
                    throw this.exceptionalState();
                }
            }
            this.state = 5;
            if (this.nextStep == null) {
                Intrinsics.throwNpe();
            }
            this.nextStep = null;
            step.resume(Unit.INSTANCE);
        }
    }

    @Override
    public T next() {
        switch (this.state) {
            case 0: 
            case 1: {
                return this.nextNotReady();
            }
            case 2: {
                this.state = 1;
                Iterator<T> iterator2 = this.nextIterator;
                if (iterator2 == null) {
                    Intrinsics.throwNpe();
                }
                return iterator2.next();
            }
            case 3: {
                this.state = 0;
                T result = this.nextValue;
                this.nextValue = null;
                return result;
            }
        }
        throw this.exceptionalState();
    }

    private final T nextNotReady() {
        if (!this.hasNext()) {
            throw (Throwable)new NoSuchElementException();
        }
        return this.next();
    }

    private final Throwable exceptionalState() {
        Throwable throwable;
        switch (this.state) {
            case 4: {
                throwable = new NoSuchElementException();
                break;
            }
            case 5: {
                throwable = new IllegalStateException("Iterator has failed.");
                break;
            }
            default: {
                throwable = new IllegalStateException("Unexpected state of the iterator: " + this.state);
            }
        }
        return throwable;
    }

    @Override
    @Nullable
    public Object yield(T value, @NotNull Continuation<? super Unit> $completion) {
        Continuation<? super Unit> continuation2;
        this.nextValue = value;
        this.state = 3;
        boolean bl = false;
        Continuation<? super Unit> continuation3 = $completion;
        boolean bl2 = false;
        Continuation<? super Unit> c = continuation2 = CoroutineIntrinsics.normalizeContinuation(continuation3);
        boolean bl3 = false;
        this.setNextStep(c);
        return IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }

    @Override
    @Nullable
    public Object yieldAll(@NotNull Iterator<? extends T> iterator2, @NotNull Continuation<? super Unit> $completion) {
        Continuation<? super Unit> continuation2;
        if (!iterator2.hasNext()) {
            return Unit.INSTANCE;
        }
        this.nextIterator = iterator2;
        this.state = 2;
        boolean bl = false;
        Continuation<? super Unit> continuation3 = $completion;
        boolean bl2 = false;
        Continuation<? super Unit> c = continuation2 = CoroutineIntrinsics.normalizeContinuation(continuation3);
        boolean bl3 = false;
        this.setNextStep(c);
        return IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }

    @Override
    public void resume(@NotNull Unit value) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        this.state = 4;
    }

    @Override
    public void resumeWithException(@NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        throw exception;
    }

    @Override
    @NotNull
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}

