/*
 * Decompiled with CFR 0.152.
 */
package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;

@Target(value={ElementType.FIELD, ElementType.TYPE})
@Retention(value=RetentionPolicy.SOURCE)
public @interface Wither {
    public AccessLevel value() default AccessLevel.PUBLIC;

    public AnyAnnotation[] onMethod() default {};

    public AnyAnnotation[] onParam() default {};

    @Deprecated
    @Retention(value=RetentionPolicy.SOURCE)
    @Target(value={})
    public static @interface AnyAnnotation {
    }
}

