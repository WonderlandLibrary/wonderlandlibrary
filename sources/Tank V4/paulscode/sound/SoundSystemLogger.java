package paulscode.sound;

public class SoundSystemLogger {
   public void message(String var1, int var2) {
      String var4 = "";

      for(int var5 = 0; var5 < var2; ++var5) {
         var4 = var4 + "    ";
      }

      String var3 = var4 + var1;
      System.out.println(var3);
   }

   public void importantMessage(String var1, int var2) {
      String var4 = "";

      for(int var5 = 0; var5 < var2; ++var5) {
         var4 = var4 + "    ";
      }

      String var3 = var4 + var1;
      System.out.println(var3);
   }

   public boolean errorCheck(boolean var1, String var2, String var3, int var4) {
      if (var1) {
         this.errorMessage(var2, var3, var4);
      }

      return var1;
   }

   public void errorMessage(String var1, String var2, int var3) {
      String var6 = "";

      for(int var7 = 0; var7 < var3; ++var7) {
         var6 = var6 + "    ";
      }

      String var4 = var6 + "Error in class '" + var1 + "'";
      String var5 = "    " + var6 + var2;
      System.out.println(var4);
      System.out.println(var5);
   }

   public void printStackTrace(Exception var1, int var2) {
      this.printExceptionMessage(var1, var2);
      this.importantMessage("STACK TRACE:", var2);
      if (var1 != null) {
         StackTraceElement[] var3 = var1.getStackTrace();
         if (var3 != null) {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               StackTraceElement var4 = var3[var5];
               if (var4 != null) {
                  this.message(var4.toString(), var2 + 1);
               }
            }

         }
      }
   }

   public void printExceptionMessage(Exception var1, int var2) {
      this.importantMessage("ERROR MESSAGE:", var2);
      if (var1.getMessage() == null) {
         this.message("(none)", var2 + 1);
      } else {
         this.message(var1.getMessage(), var2 + 1);
      }

   }
}
