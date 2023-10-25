package org.lwjgl.util.mapped;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MappedType {
  int padding() default 0;
  
  boolean cacheLinePadding() default false;
  
  int align() default 4;
  
  boolean autoGenerateOffsets() default true;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\mapped\MappedType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */