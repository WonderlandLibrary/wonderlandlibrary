/*
 * Decompiled with CFR 0.152.
 */
package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE, ElementType.FIELD})
@Retention(value=RetentionPolicy.SOURCE)
public @interface Accessors {
    public boolean fluent() default false;

    public boolean chain() default false;

    public String[] prefix() default {};
}

