package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class OptionException extends RuntimeException {
   private static final long serialVersionUID = -1L;
   private final List options = new ArrayList();

   protected OptionException(Collection var1) {
      this.options.addAll(var1);
   }

   protected OptionException(Collection var1, Throwable var2) {
      super(var2);
      this.options.addAll(var1);
   }

   public Collection options() {
      return Collections.unmodifiableCollection(this.options);
   }

   protected final String singleOptionMessage() {
      return this.singleOptionMessage((String)this.options.get(0));
   }

   protected final String singleOptionMessage(String var1) {
      return "'" + var1 + "'";
   }

   protected final String multipleOptionMessage() {
      StringBuilder var1 = new StringBuilder("[");
      Iterator var2 = this.options.iterator();

      while(var2.hasNext()) {
         var1.append(this.singleOptionMessage((String)var2.next()));
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append(']');
      return var1.toString();
   }

   static OptionException unrecognizedOption(String var0) {
      return new UnrecognizedOptionException(var0);
   }
}
