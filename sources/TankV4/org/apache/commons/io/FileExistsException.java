package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

public class FileExistsException extends IOException {
   private static final long serialVersionUID = 1L;

   public FileExistsException() {
   }

   public FileExistsException(String var1) {
      super(var1);
   }

   public FileExistsException(File var1) {
      super("File " + var1 + " exists");
   }
}
