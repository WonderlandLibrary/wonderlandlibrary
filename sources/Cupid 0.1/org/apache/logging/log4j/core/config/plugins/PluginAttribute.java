package org.apache.logging.log4j.core.config.plugins;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginAttributeVisitor;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@PluginVisitorStrategy(PluginAttributeVisitor.class)
public @interface PluginAttribute {
  boolean defaultBoolean() default false;
  
  byte defaultByte() default 0;
  
  char defaultChar() default '\000';
  
  Class<?> defaultClass() default Object.class;
  
  double defaultDouble() default 0.0D;
  
  float defaultFloat() default 0.0F;
  
  int defaultInt() default 0;
  
  long defaultLong() default 0L;
  
  short defaultShort() default 0;
  
  String defaultString() default "";
  
  String value();
  
  boolean sensitive() default false;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\PluginAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */