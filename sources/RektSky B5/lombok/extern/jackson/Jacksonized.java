/*
 * Decompiled with CFR 0.152.
 */
package lombok.extern.jackson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(value=RetentionPolicy.SOURCE)
public @interface Jacksonized {
}

