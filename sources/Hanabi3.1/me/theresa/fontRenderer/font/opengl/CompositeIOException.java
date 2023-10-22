package me.theresa.fontRenderer.font.opengl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CompositeIOException extends IOException {
   private final ArrayList exceptions = new ArrayList();

   public void addException(Exception e) {
      this.exceptions.add(e);
   }

   public String getMessage() {
      StringBuilder msg = new StringBuilder("Composite Exception: \n");
      Iterator var2 = this.exceptions.iterator();

      while(var2.hasNext()) {
         Object exception = var2.next();
         msg.append("\t").append(((IOException)exception).getMessage()).append("\n");
      }

      return msg.toString();
   }
}
