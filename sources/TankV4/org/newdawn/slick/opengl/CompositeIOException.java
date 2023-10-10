package org.newdawn.slick.opengl;

import java.io.IOException;
import java.util.ArrayList;

public class CompositeIOException extends IOException {
   private ArrayList exceptions = new ArrayList();

   public void addException(Exception var1) {
      this.exceptions.add(var1);
   }

   public String getMessage() {
      String var1 = "Composite Exception: \n";

      for(int var2 = 0; var2 < this.exceptions.size(); ++var2) {
         var1 = var1 + "\t" + ((IOException)this.exceptions.get(var2)).getMessage() + "\n";
      }

      return var1;
   }
}
