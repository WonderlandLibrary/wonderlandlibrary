package org.apache.logging.log4j.message;

public class SimpleMessage implements Message {
   private static final long serialVersionUID = -8398002534962715992L;
   private final String message;

   public SimpleMessage() {
      this((String)null);
   }

   public SimpleMessage(String var1) {
      this.message = var1;
   }

   public String getFormattedMessage() {
      return this.message;
   }

   public String getFormat() {
      return this.message;
   }

   public Object[] getParameters() {
      return null;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         boolean var10000;
         label35: {
            SimpleMessage var2 = (SimpleMessage)var1;
            if (this.message != null) {
               if (this.message.equals(var2.message)) {
                  break label35;
               }
            } else if (var2.message == null) {
               break label35;
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.message != null ? this.message.hashCode() : 0;
   }

   public String toString() {
      return "SimpleMessage[message=" + this.message + "]";
   }

   public Throwable getThrowable() {
      return null;
   }
}
