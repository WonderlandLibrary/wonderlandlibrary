package com.google.common.io;

import java.io.IOException;

@Deprecated
public interface InputSupplier<T> {
  T getInput() throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\InputSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */