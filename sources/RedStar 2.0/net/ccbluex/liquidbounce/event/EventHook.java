package net.ccbluex.liquidbounce.event;

import java.lang.reflect.Method;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\u0000\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\b\b\u0000\u000020B000¢\bR0¢\b\n\u0000\b\t\nR0\f¢\b\n\u0000\b\rR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/EventHook;", "", "eventClass", "Lnet/ccbluex/liquidbounce/event/Listenable;", "method", "Ljava/lang/reflect/Method;", "eventTarget", "Lnet/ccbluex/liquidbounce/event/EventTarget;", "(Lnet/ccbluex/liquidbounce/event/Listenable;Ljava/lang/reflect/Method;Lnet/ccbluex/liquidbounce/event/EventTarget;)V", "getEventClass", "()Lnet/ccbluex/liquidbounce/event/Listenable;", "isIgnoreCondition", "", "()Z", "getMethod", "()Ljava/lang/reflect/Method;", "Pride"})
public final class EventHook {
    private final boolean isIgnoreCondition;
    @NotNull
    private final Listenable eventClass;
    @NotNull
    private final Method method;

    public final boolean isIgnoreCondition() {
        return this.isIgnoreCondition;
    }

    @NotNull
    public final Listenable getEventClass() {
        return this.eventClass;
    }

    @NotNull
    public final Method getMethod() {
        return this.method;
    }

    public EventHook(@NotNull Listenable eventClass, @NotNull Method method, @NotNull EventTarget eventTarget) {
        Intrinsics.checkParameterIsNotNull(eventClass, "eventClass");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.checkParameterIsNotNull(eventTarget, "eventTarget");
        this.eventClass = eventClass;
        this.method = method;
        this.isIgnoreCondition = eventTarget.ignoreCondition();
    }
}
