/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.event;

import java.lang.reflect.Method;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/event/EventHook;", "", "eventClass", "Lnet/ccbluex/liquidbounce/event/Listenable;", "method", "Ljava/lang/reflect/Method;", "eventTarget", "Lnet/ccbluex/liquidbounce/event/EventTarget;", "(Lnet/ccbluex/liquidbounce/event/Listenable;Ljava/lang/reflect/Method;Lnet/ccbluex/liquidbounce/event/EventTarget;)V", "getEventClass", "()Lnet/ccbluex/liquidbounce/event/Listenable;", "isIgnoreCondition", "", "()Z", "getMethod", "()Ljava/lang/reflect/Method;", "LiKingSense"})
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
        Intrinsics.checkParameterIsNotNull((Object)eventClass, (String)"eventClass");
        Intrinsics.checkParameterIsNotNull((Object)method, (String)"method");
        Intrinsics.checkParameterIsNotNull((Object)eventTarget, (String)"eventTarget");
        this.eventClass = eventClass;
        this.method = method;
        this.isIgnoreCondition = eventTarget.ignoreCondition();
    }
}

