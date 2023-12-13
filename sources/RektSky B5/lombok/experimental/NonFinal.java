/*
 * Decompiled with CFR 0.152.
 */
package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@Retention(value=RetentionPolicy.SOURCE)
public @interface NonFinal {
}

