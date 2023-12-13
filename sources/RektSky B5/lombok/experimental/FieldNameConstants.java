/*
 * Decompiled with CFR 0.152.
 */
package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.SOURCE)
public @interface FieldNameConstants {
    public AccessLevel level() default AccessLevel.PUBLIC;

    public boolean asEnum() default false;

    public String innerTypeName() default "";

    public boolean onlyExplicitlyIncluded() default false;

    @Target(value={ElementType.FIELD})
    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Exclude {
    }

    @Target(value={ElementType.FIELD})
    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Include {
    }
}

