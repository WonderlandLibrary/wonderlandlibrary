package Reality.Realii.utils.cheats.RenderUtills;

import java.io.Serializable;
import java.util.Objects;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


public abstract class Pair<A, B> implements Serializable {

    public static <A, B> ImmutablePair<A, B> of(A a, B b) { return ImmutablePair.of(a, b); }

    public static <A> ImmutablePair<A, A> of(A a) { return ImmutablePair.of(a, a); }

    public abstract A getFirst();

    public abstract B getSecond();

    public abstract <R> R apply(BiFunction<? super A, ? super B, ? extends R> func);

    public abstract void use(BiConsumer<? super A, ? super B> func);

    @Override
    public int hashCode() {
        return Objects.hash(getFirst(), getSecond());
    }

    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if (that instanceof ImmutablePair<?, ?>) {
            final ImmutablePair<?, ?> other = (ImmutablePair<?, ?>) that;
            return Objects.equals(getFirst(), other.getFirst()) && Objects.equals(getSecond(), other.getSecond());
        }
        return false;
    }
}
