package com.sun.javafx.iio;

import java.io.IOException;

public class ImageStorageException extends IOException {
   private static final long serialVersionUID = 1L;

   public ImageStorageException(String var1) {
      super(var1);
   }

   public ImageStorageException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }
}
