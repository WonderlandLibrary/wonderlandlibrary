package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.IOException;
import java.util.List;

public class CompositeAction extends AbstractAction {
   private final Action[] actions;
   private final boolean stopOnError;

   public CompositeAction(List var1, boolean var2) {
      this.actions = new Action[var1.size()];
      var1.toArray(this.actions);
      this.stopOnError = var2;
   }

   public void run() {
      try {
         this.execute();
      } catch (IOException var2) {
         LOGGER.warn((String)"Exception during file rollover.", (Throwable)var2);
      }

   }

   public boolean execute() throws IOException {
      if (this.stopOnError) {
         Action[] var9 = this.actions;
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Action var12 = var9[var11];
            if (!var12.execute()) {
               return false;
            }
         }

         return true;
      } else {
         boolean var1 = true;
         IOException var2 = null;
         Action[] var3 = this.actions;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Action var6 = var3[var5];

            try {
               var1 &= var6.execute();
            } catch (IOException var8) {
               var1 = false;
               if (var2 == null) {
                  var2 = var8;
               }
            }
         }

         if (var2 != null) {
            throw var2;
         } else {
            return var1;
         }
      }
   }
}
