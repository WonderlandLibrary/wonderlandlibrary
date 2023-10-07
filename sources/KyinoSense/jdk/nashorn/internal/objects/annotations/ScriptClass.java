/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface ScriptClass {
    public String value() default "";
}

