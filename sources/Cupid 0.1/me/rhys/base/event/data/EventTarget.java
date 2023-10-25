package me.rhys.base.event.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventTarget {
  EventPriority value() default EventPriority.MEDIUM;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\data\EventTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */