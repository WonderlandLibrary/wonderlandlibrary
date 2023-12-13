package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectMessage implements Message {
   private static final long serialVersionUID = -5903272448334166185L;
   private transient Object obj;

   public ObjectMessage(Object var1) {
      if (var1 == null) {
         var1 = "null";
      }

      this.obj = var1;
   }

   public String getFormattedMessage() {
      return this.obj.toString();
   }

   public String getFormat() {
      return this.obj.toString();
   }

   public Object[] getParameters() {
      return new Object[]{this.obj};
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         boolean var10000;
         label35: {
            ObjectMessage var2 = (ObjectMessage)var1;
            if (this.obj != null) {
               if (this.obj.equals(var2.obj)) {
                  break label35;
               }
            } else if (var2.obj == null) {
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
      return this.obj != null ? this.obj.hashCode() : 0;
   }

   public String toString() {
      return "ObjectMessage[obj=" + this.obj.toString() + "]";
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      if (this.obj instanceof Serializable) {
         var1.writeObject(this.obj);
      } else {
         var1.writeObject(this.obj.toString());
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.obj = var1.readObject();
   }

   public Throwable getThrowable() {
      return this.obj instanceof Throwable ? (Throwable)this.obj : null;
   }
}
