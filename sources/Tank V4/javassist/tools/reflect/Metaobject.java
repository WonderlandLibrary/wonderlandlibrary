package javassist.tools.reflect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Metaobject implements Serializable {
   protected ClassMetaobject classmetaobject;
   protected Metalevel baseobject;
   protected Method[] methods;

   public Metaobject(Object var1, Object[] var2) {
      this.baseobject = (Metalevel)var1;
      this.classmetaobject = this.baseobject._getClass();
      this.methods = this.classmetaobject.getReflectiveMethods();
   }

   protected Metaobject() {
      this.baseobject = null;
      this.classmetaobject = null;
      this.methods = null;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this.baseobject);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.baseobject = (Metalevel)var1.readObject();
      this.classmetaobject = this.baseobject._getClass();
      this.methods = this.classmetaobject.getReflectiveMethods();
   }

   public final ClassMetaobject getClassMetaobject() {
      return this.classmetaobject;
   }

   public final Object getObject() {
      return this.baseobject;
   }

   public final void setObject(Object var1) {
      this.baseobject = (Metalevel)var1;
      this.classmetaobject = this.baseobject._getClass();
      this.methods = this.classmetaobject.getReflectiveMethods();
      this.baseobject._setMetaobject(this);
   }

   public final String getMethodName(int var1) {
      String var2 = this.methods[var1].getName();
      int var3 = 3;

      char var4;
      do {
         var4 = var2.charAt(var3++);
      } while(var4 >= '0' && '9' >= var4);

      return var2.substring(var3);
   }

   public final Class[] getParameterTypes(int var1) {
      return this.methods[var1].getParameterTypes();
   }

   public final Class getReturnType(int var1) {
      return this.methods[var1].getReturnType();
   }

   public Object trapFieldRead(String var1) {
      Class var2 = this.getClassMetaobject().getJavaClass();

      try {
         return var2.getField(var1).get(this.getObject());
      } catch (NoSuchFieldException var4) {
         throw new RuntimeException(var4.toString());
      } catch (IllegalAccessException var5) {
         throw new RuntimeException(var5.toString());
      }
   }

   public void trapFieldWrite(String var1, Object var2) {
      Class var3 = this.getClassMetaobject().getJavaClass();

      try {
         var3.getField(var1).set(this.getObject(), var2);
      } catch (NoSuchFieldException var5) {
         throw new RuntimeException(var5.toString());
      } catch (IllegalAccessException var6) {
         throw new RuntimeException(var6.toString());
      }
   }

   public Object trapMethodcall(int var1, Object[] var2) throws Throwable {
      try {
         return this.methods[var1].invoke(this.getObject(), var2);
      } catch (InvocationTargetException var4) {
         throw var4.getTargetException();
      } catch (IllegalAccessException var5) {
         throw new CannotInvokeException(var5);
      }
   }
}
