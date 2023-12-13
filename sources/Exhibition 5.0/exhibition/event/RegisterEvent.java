// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterEvent {
    Class<? extends Event>[] events();
}
