package org.yaml.snakeyaml.composer;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public class ComposerException extends MarkedYAMLException {
  private static final long serialVersionUID = 2146314636913113935L;
  
  protected ComposerException(String context, Mark contextMark, String problem, Mark problemMark) {
    super(context, contextMark, problem, problemMark);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\composer\ComposerException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */