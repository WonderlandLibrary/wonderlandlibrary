package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.error.Mark;

public class DuplicateKeyException extends ConstructorException {
  protected DuplicateKeyException(Mark contextMark, Object key, Mark problemMark) {
    super("while constructing a mapping", contextMark, "found duplicate key " + String.valueOf(key), problemMark);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\constructor\DuplicateKeyException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */