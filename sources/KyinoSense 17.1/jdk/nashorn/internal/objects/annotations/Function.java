/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jdk.nashorn.internal.objects.annotations.Where;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Function {
    public String name() default "";

    public int attributes() default 0;

    public int arity() default -2;

    public Where where() default Where.PROTOTYPE;
}

