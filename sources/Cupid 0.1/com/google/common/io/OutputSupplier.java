package com.google.common.io;

import java.io.IOException;

@Deprecated
public interface OutputSupplier<T> {
  T getOutput() throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\OutputSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */