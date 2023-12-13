/*
 * Decompiled with CFR 0.152.
 */
package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.SOURCE)
public @interface NoArgsConstructor {
    public String staticName() default "";

    public AnyAnnotation[] onConstructor() default {};

    public AccessLevel access() default AccessLevel.PUBLIC;

    public boolean force() default false;

    @Deprecated
    @Retention(value=RetentionPolicy.SOURCE)
    @Target(value={})
    public static @interface AnyAnnotation {
    }
}

