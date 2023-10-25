package org.apache.logging.log4j.core.config.plugins.validation.validators;

import java.lang.annotation.Annotation;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
import org.apache.logging.log4j.status.StatusLogger;

public class ValidPortValidator implements ConstraintValidator<ValidPort> {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private ValidPort annotation;
  
  public void initialize(ValidPort annotation) {
    this.annotation = annotation;
  }
  
  public boolean isValid(String name, Object value) {
    if (value instanceof CharSequence)
      return isValid(name, TypeConverters.convert(value.toString(), Integer.class, Integer.valueOf(-1))); 
    if (!Integer.class.isInstance(value)) {
      LOGGER.error(this.annotation.message());
      return false;
    } 
    int port = ((Integer)value).intValue();
    if (port < 0 || port > 65535) {
      LOGGER.error(this.annotation.message());
      return false;
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\validation\validators\ValidPortValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */