/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Retention;
import kotlin.annotation.Target;

@Target(allowedTargets={AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER})
@Retention(value=AnnotationRetention.RUNTIME)
@java.lang.annotation.Retention(value=RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value={ElementType.METHOD})
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0087\u0002\u0018\u00002\u00020\u0001B\u0014\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005R\u000f\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0006R\u000f\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/event/EventTarget;", "", "ignoreCondition", "", "priority", "", "()Z", "()I", "KyinoClient"})
public @interface EventTarget {
    public boolean ignoreCondition() default false;

    public int priority() default 0;
}

