/*
 * Decompiled with CFR 0.152.
 */
package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD, ElementType.METHOD})
@Retention(value=RetentionPolicy.SOURCE)
public @interface Delegate {
    public Class<?>[] types() default {};

    public Class<?>[] excludes() default {};
}

