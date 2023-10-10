// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface ComObject {
    String clsId() default "";
    
    String progId() default "";
}
