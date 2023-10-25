package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public class ScannerException extends MarkedYAMLException {
  private static final long serialVersionUID = 4782293188600445954L;
  
  public ScannerException(String context, Mark contextMark, String problem, Mark problemMark, String note) {
    super(context, contextMark, problem, problemMark, note);
  }
  
  public ScannerException(String context, Mark contextMark, String problem, Mark problemMark) {
    this(context, contextMark, problem, problemMark, null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\scanner\ScannerException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */