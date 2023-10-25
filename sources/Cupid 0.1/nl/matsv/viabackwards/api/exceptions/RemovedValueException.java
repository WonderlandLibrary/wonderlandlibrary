package nl.matsv.viabackwards.api.exceptions;

import java.io.IOException;

public class RemovedValueException extends IOException {
  public static final RemovedValueException EX = new RemovedValueException() {
      public Throwable fillInStackTrace() {
        return this;
      }
    };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\exceptions\RemovedValueException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */