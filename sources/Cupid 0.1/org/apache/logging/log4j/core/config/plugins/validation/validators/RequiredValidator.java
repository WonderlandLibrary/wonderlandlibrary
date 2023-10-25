package org.apache.logging.log4j.core.config.plugins.validation.validators;

import java.lang.annotation.Annotation;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.util.Assert;
import org.apache.logging.log4j.status.StatusLogger;

public class RequiredValidator implements ConstraintValidator<Required> {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private Required annotation;
  
  public void initialize(Required anAnnotation) {
    this.annotation = anAnnotation;
  }
  
  public boolean isValid(String name, Object value) {
    return (Assert.isNonEmpty(value) || err(name));
  }
  
  private boolean err(String name) {
    LOGGER.error(this.annotation.message() + ": " + name);
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\validation\validators\RequiredValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */