package javafx.application;

import java.security.AccessController;

public abstract class Preloader extends Application {
   private static final String lineSeparator;

   public void handleProgressNotification(ProgressNotification var1) {
   }

   public void handleStateChangeNotification(StateChangeNotification var1) {
   }

   public void handleApplicationNotification(PreloaderNotification var1) {
   }

   public boolean handleErrorNotification(ErrorNotification var1) {
      return false;
   }

   static {
      String var0 = (String)AccessController.doPrivileged(() -> {
         return System.getProperty("line.separator");
      });
      lineSeparator = var0 != null ? var0 : "\n";
   }

   public static class StateChangeNotification implements PreloaderNotification {
      private final Type notificationType;
      private final Application application;

      public StateChangeNotification(Type var1) {
         this.notificationType = var1;
         this.application = null;
      }

      public StateChangeNotification(Type var1, Application var2) {
         this.notificationType = var1;
         this.application = var2;
      }

      public Type getType() {
         return this.notificationType;
      }

      public Application getApplication() {
         return this.application;
      }

      public static enum Type {
         BEFORE_LOAD,
         BEFORE_INIT,
         BEFORE_START;
      }
   }

   public static class ProgressNotification implements PreloaderNotification {
      private final double progress;
      private final String details;

      public ProgressNotification(double var1) {
         this(var1, "");
      }

      private ProgressNotification(double var1, String var3) {
         this.progress = var1;
         this.details = var3;
      }

      public double getProgress() {
         return this.progress;
      }

      private String getDetails() {
         return this.details;
      }
   }

   public static class ErrorNotification implements PreloaderNotification {
      private String location;
      private String details = "";
      private Throwable cause;

      public ErrorNotification(String var1, String var2, Throwable var3) {
         if (var2 == null) {
            throw new NullPointerException();
         } else {
            this.location = var1;
            this.details = var2;
            this.cause = var3;
         }
      }

      public String getLocation() {
         return this.location;
      }

      public String getDetails() {
         return this.details;
      }

      public Throwable getCause() {
         return this.cause;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("Preloader.ErrorNotification: ");
         var1.append(this.details);
         if (this.cause != null) {
            var1.append(Preloader.lineSeparator).append("Caused by: ").append(this.cause.toString());
         }

         if (this.location != null) {
            var1.append(Preloader.lineSeparator).append("Location: ").append(this.location);
         }

         return var1.toString();
      }
   }

   public interface PreloaderNotification {
   }
}
