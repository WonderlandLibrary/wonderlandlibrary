/*
 * Decompiled with CFR 0.152.
 */
package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;

@Target(value={ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(value=RetentionPolicy.SOURCE)
public @interface Builder {
    public String builderMethodName() default "builder";

    public String buildMethodName() default "build";

    public String builderClassName() default "";

    public boolean toBuilder() default false;

    public AccessLevel access() default AccessLevel.PUBLIC;

    public String setterPrefix() default "";

    @Target(value={ElementType.FIELD})
    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Default {
    }

    @Target(value={ElementType.FIELD, ElementType.PARAMETER})
    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ObtainVia {
        public String field() default "";

        public String method() default "";

        public boolean isStatic() default false;
    }
}

