package net.ccbluex.liquidbounce.event;

import java.util.function.Predicate;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventHook;
import net.ccbluex.liquidbounce.event.Listenable;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\u0000020H\n¢\b"}, d2={"<anonymous>", "", "it", "Lnet/ccbluex/liquidbounce/event/EventHook;", "test"})
final class EventManager$unregisterListener$1<T>
implements Predicate<EventHook> {
    final Listenable $listenable;

    @Override
    public final boolean test(@NotNull EventHook it) {
        Intrinsics.checkParameterIsNotNull(it, "it");
        return Intrinsics.areEqual(it.getEventClass(), this.$listenable);
    }

    EventManager$unregisterListener$1(Listenable listenable) {
        this.$listenable = listenable;
    }
}
