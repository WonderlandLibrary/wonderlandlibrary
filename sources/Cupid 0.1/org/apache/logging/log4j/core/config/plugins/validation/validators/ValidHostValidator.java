package org.apache.logging.log4j.core.config.plugins.validation.validators;

import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
import org.apache.logging.log4j.status.StatusLogger;

public class ValidHostValidator implements ConstraintValidator<ValidHost> {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private ValidHost annotation;
  
  public void initialize(ValidHost annotation) {
    this.annotation = annotation;
  }
  
  public boolean isValid(String name, Object value) {
    if (value == null) {
      LOGGER.error(this.annotation.message());
      return false;
    } 
    if (value instanceof InetAddress)
      return true; 
    try {
      InetAddress.getByName(value.toString());
      return true;
    } catch (UnknownHostException e) {
      LOGGER.error(this.annotation.message(), e);
      return false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\validation\validators\ValidHostValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */