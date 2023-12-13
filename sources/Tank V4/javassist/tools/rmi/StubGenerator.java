package javassist.tools.rmi;

import java.lang.reflect.Method;
import java.util.Hashtable;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

public class StubGenerator implements Translator {
   private static final String fieldImporter = "importer";
   private static final String fieldObjectId = "objectId";
   private static final String accessorObjectId = "_getObjectId";
   private static final String sampleClass = "javassist.tools.rmi.Sample";
   private ClassPool classPool;
   private Hashtable proxyClasses = new Hashtable();
   private CtMethod forwardMethod;
   private CtMethod forwardStaticMethod;
   private CtClass[] proxyConstructorParamTypes;
   private CtClass[] interfacesForProxy;
   private CtClass[] exceptionForProxy;

   public void start(ClassPool var1) throws NotFoundException {
      this.classPool = var1;
      CtClass var2 = var1.get("javassist.tools.rmi.Sample");
      this.forwardMethod = var2.getDeclaredMethod("forward");
      this.forwardStaticMethod = var2.getDeclaredMethod("forwardStatic");
      this.proxyConstructorParamTypes = var1.get(new String[]{"javassist.tools.rmi.ObjectImporter", "int"});
      this.interfacesForProxy = var1.get(new String[]{"java.io.Serializable", "javassist.tools.rmi.Proxy"});
      this.exceptionForProxy = new CtClass[]{var1.get("javassist.tools.rmi.RemoteException")};
   }

   public void onLoad(ClassPool var1, String var2) {
   }

   public boolean isProxyClass(String var1) {
      return this.proxyClasses.get(var1) != null;
   }

   public synchronized boolean makeProxyClass(Class var1) throws CannotCompileException, NotFoundException {
      String var2 = var1.getName();
      if (this.proxyClasses.get(var2) != null) {
         return false;
      } else {
         CtClass var3 = this.produceProxyClass(this.classPool.get(var2), var1);
         this.proxyClasses.put(var2, var3);
         this.modifySuperclass(var3);
         return true;
      }
   }

   private CtClass produceProxyClass(CtClass var1, Class var2) throws CannotCompileException, NotFoundException {
      int var3 = var1.getModifiers();
      if (!Modifier.isAbstract(var3) && !Modifier.isNative(var3) && Modifier.isPublic(var3)) {
         CtClass var4 = this.classPool.makeClass(var1.getName(), var1.getSuperclass());
         var4.setInterfaces(this.interfacesForProxy);
         CtField var5 = new CtField(this.classPool.get("javassist.tools.rmi.ObjectImporter"), "importer", var4);
         var5.setModifiers(2);
         var4.addField(var5, CtField.Initializer.byParameter(0));
         var5 = new CtField(CtClass.intType, "objectId", var4);
         var5.setModifiers(2);
         var4.addField(var5, CtField.Initializer.byParameter(1));
         var4.addMethod(CtNewMethod.getter("_getObjectId", var5));
         var4.addConstructor(CtNewConstructor.defaultConstructor(var4));
         CtConstructor var6 = CtNewConstructor.skeleton(this.proxyConstructorParamTypes, (CtClass[])null, var4);
         var4.addConstructor(var6);

         try {
            this.addMethods(var4, var2.getMethods());
            return var4;
         } catch (SecurityException var8) {
            throw new CannotCompileException(var8);
         }
      } else {
         throw new CannotCompileException(var1.getName() + " must be public, non-native, and non-abstract.");
      }
   }

   private CtClass toCtClass(Class var1) throws NotFoundException {
      String var2;
      if (!var1.isArray()) {
         var2 = var1.getName();
      } else {
         StringBuffer var3 = new StringBuffer();

         do {
            var3.append("[]");
            var1 = var1.getComponentType();
         } while(var1.isArray());

         var3.insert(0, var1.getName());
         var2 = var3.toString();
      }

      return this.classPool.get(var2);
   }

   private CtClass[] toCtClass(Class[] var1) throws NotFoundException {
      int var2 = var1.length;
      CtClass[] var3 = new CtClass[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = this.toCtClass(var1[var4]);
      }

      return var3;
   }

   private void addMethods(CtClass var1, Method[] var2) throws CannotCompileException, NotFoundException {
      for(int var4 = 0; var4 < var2.length; ++var4) {
         Method var5 = var2[var4];
         int var6 = var5.getModifiers();
         if (var5.getDeclaringClass() != Object.class && !Modifier.isFinal(var6)) {
            if (Modifier.isPublic(var6)) {
               CtMethod var7;
               if (Modifier.isStatic(var6)) {
                  var7 = this.forwardStaticMethod;
               } else {
                  var7 = this.forwardMethod;
               }

               CtMethod var3 = CtNewMethod.wrapped(this.toCtClass(var5.getReturnType()), var5.getName(), this.toCtClass(var5.getParameterTypes()), this.exceptionForProxy, var7, CtMethod.ConstParameter.integer(var4), var1);
               var3.setModifiers(var6);
               var1.addMethod(var3);
            } else if (!Modifier.isProtected(var6) && !Modifier.isPrivate(var6)) {
               throw new CannotCompileException("the methods must be public, protected, or private.");
            }
         }
      }

   }

   private void modifySuperclass(CtClass var1) throws CannotCompileException, NotFoundException {
      while(true) {
         CtClass var2 = var1.getSuperclass();
         if (var2 != null) {
            try {
               var2.getDeclaredConstructor((CtClass[])null);
            } catch (NotFoundException var4) {
               var2.addConstructor(CtNewConstructor.defaultConstructor(var2));
               var1 = var2;
               continue;
            }
         }

         return;
      }
   }
}
