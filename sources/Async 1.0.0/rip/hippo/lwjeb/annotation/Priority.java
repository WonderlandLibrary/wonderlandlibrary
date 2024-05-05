package rip.hippo.lwjeb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hippo
 * @version 1.0.0, 7/30/21
 * @since 5.3.0
 * <p>
 * Marks the priority for a handler.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD})
public @interface Priority {

  /**
   * The priority of the handler.
   *
   * @return The priority.
   */
  int value();
}
