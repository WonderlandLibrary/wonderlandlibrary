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
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\b\u000020B\n\b\b0R0¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/EventTarget;", "", "ignoreCondition", "", "()Z", "Pride"})
public @interface EventTarget {
    public boolean ignoreCondition() default false;
}
