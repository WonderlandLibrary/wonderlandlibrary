package javafx.fxml;

import java.io.IOException;

public class LoadException extends IOException {
   private static final long serialVersionUID = 0L;

   public LoadException() {
   }

   public LoadException(String var1) {
      super(var1);
   }

   public LoadException(Throwable var1) {
      super(var1);
   }

   public LoadException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
