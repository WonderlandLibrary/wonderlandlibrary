package javassist.tools.reflect;

import java.util.Iterator;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;

public class Reflection implements Translator {
   static final String classobjectField = "_classobject";
   static final String classobjectAccessor = "_getClass";
   static final String metaobjectField = "_metaobject";
   static final String metaobjectGetter = "_getMetaobject";
   static final String metaobjectSetter = "_setMetaobject";
   static final String readPrefix = "_r_";
   static final String writePrefix = "_w_";
   static final String metaobjectClassName = "javassist.tools.reflect.Metaobject";
   static final String classMetaobjectClassName = "javassist.tools.reflect.ClassMetaobject";
   protected CtMethod trapMethod;
   protected CtMethod trapStaticMethod;
   protected CtMethod trapRead;
   protected CtMethod trapWrite;
   protected CtClass[] readParam;
   protected ClassPool classPool = null;
   protected CodeConverter converter = new CodeConverter();

   public void start(ClassPool var1) throws NotFoundException {
      this.classPool = var1;
      String var2 = "javassist.tools.reflect.Sample is not found or broken.";

      try {
         CtClass var3 = this.classPool.get("javassist.tools.reflect.Sample");
         this.rebuildClassFile(var3.getClassFile());
         this.trapMethod = var3.getDeclaredMethod("trap");
         this.trapStaticMethod = var3.getDeclaredMethod("trapStatic");
         this.trapRead = var3.getDeclaredMethod("trapRead");
         this.trapWrite = var3.getDeclaredMethod("trapWrite");
         this.readParam = new CtClass[]{this.classPool.get("java.lang.Object")};
      } catch (NotFoundException var4) {
         throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
      } catch (BadBytecode var5) {
         throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
      }
   }

   public void onLoad(ClassPool var1, String var2) throws CannotCompileException, NotFoundException {
      CtClass var3 = var1.get(var2);
      var3.instrument(this.converter);
   }

   public boolean makeReflective(String var1, String var2, String var3) throws CannotCompileException, NotFoundException {
      return this.makeReflective(this.classPool.get(var1), this.classPool.get(var2), this.classPool.get(var3));
   }

   public boolean makeReflective(Class var1, Class var2, Class var3) throws CannotCompileException, NotFoundException {
      return this.makeReflective(var1.getName(), var2.getName(), var3.getName());
   }

   public boolean makeReflective(CtClass var1, CtClass var2, CtClass var3) throws CannotCompileException, CannotReflectException, NotFoundException {
      if (var1.isInterface()) {
         throw new CannotReflectException("Cannot reflect an interface: " + var1.getName());
      } else if (var1.subclassOf(this.classPool.get("javassist.tools.reflect.ClassMetaobject"))) {
         throw new CannotReflectException("Cannot reflect a subclass of ClassMetaobject: " + var1.getName());
      } else if (var1.subclassOf(this.classPool.get("javassist.tools.reflect.Metaobject"))) {
         throw new CannotReflectException("Cannot reflect a subclass of Metaobject: " + var1.getName());
      } else {
         this.registerReflectiveClass(var1);
         return this.modifyClassfile(var1, var2, var3);
      }
   }

   private void registerReflectiveClass(CtClass var1) {
      CtField[] var2 = var1.getDeclaredFields();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         CtField var4 = var2[var3];
         int var5 = var4.getModifiers();
         if ((var5 & 1) != 0 && (var5 & 16) == 0) {
            String var6 = var4.getName();
            this.converter.replaceFieldRead(var4, var1, "_r_" + var6);
            this.converter.replaceFieldWrite(var4, var1, "_w_" + var6);
         }
      }

   }

   private boolean modifyClassfile(CtClass var1, CtClass var2, CtClass var3) throws CannotCompileException, NotFoundException {
      if (var1.getAttribute("Reflective") != null) {
         return false;
      } else {
         var1.setAttribute("Reflective", new byte[0]);
         CtClass var4 = this.classPool.get("javassist.tools.reflect.Metalevel");
         boolean var5 = !var1.subtypeOf(var4);
         if (var5) {
            var1.addInterface(var4);
         }

         this.processMethods(var1, var5);
         this.processFields(var1);
         CtField var6;
         if (var5) {
            var6 = new CtField(this.classPool.get("javassist.tools.reflect.Metaobject"), "_metaobject", var1);
            var6.setModifiers(4);
            var1.addField(var6, CtField.Initializer.byNewWithParams(var2));
            var1.addMethod(CtNewMethod.getter("_getMetaobject", var6));
            var1.addMethod(CtNewMethod.setter("_setMetaobject", var6));
         }

         var6 = new CtField(this.classPool.get("javassist.tools.reflect.ClassMetaobject"), "_classobject", var1);
         var6.setModifiers(10);
         var1.addField(var6, CtField.Initializer.byNew(var3, new String[]{var1.getName()}));
         var1.addMethod(CtNewMethod.getter("_getClass", var6));
         return true;
      }
   }

   private void processMethods(CtClass var1, boolean var2) throws CannotCompileException, NotFoundException {
      CtMethod[] var3 = var1.getMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         CtMethod var5 = var3[var4];
         int var6 = var5.getModifiers();
         if (Modifier.isPublic(var6) && !Modifier.isAbstract(var6)) {
            this.processMethods0(var6, var1, var5, var4, var2);
         }
      }

   }

   private void processMethods0(int var1, CtClass var2, CtMethod var3, int var4, boolean var5) throws CannotCompileException, NotFoundException {
      String var7 = var3.getName();
      if (var7 != false) {
         CtMethod var8;
         if (var3.getDeclaringClass() == var2) {
            if (Modifier.isNative(var1)) {
               return;
            }

            var8 = var3;
            if (Modifier.isFinal(var1)) {
               var1 &= -17;
               var3.setModifiers(var1);
            }
         } else {
            if (Modifier.isFinal(var1)) {
               return;
            }

            var1 &= -257;
            var8 = CtNewMethod.delegator(this.findOriginal(var3, var5), var2);
            var8.setModifiers(var1);
            var2.addMethod(var8);
         }

         var8.setName("_m_" + var4 + "_" + var7);
         CtMethod var6;
         if (Modifier.isStatic(var1)) {
            var6 = this.trapStaticMethod;
         } else {
            var6 = this.trapMethod;
         }

         CtMethod var9 = CtNewMethod.wrapped(var3.getReturnType(), var7, var3.getParameterTypes(), var3.getExceptionTypes(), var6, CtMethod.ConstParameter.integer(var4), var2);
         var9.setModifiers(var1);
         var2.addMethod(var9);
      }
   }

   private CtMethod findOriginal(CtMethod var1, boolean var2) throws NotFoundException {
      if (var2) {
         return var1;
      } else {
         String var3 = var1.getName();
         CtMethod[] var4 = var1.getDeclaringClass().getDeclaredMethods();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5].getName();
            if (var6.endsWith(var3) && var6.startsWith("_m_") && var4[var5].getSignature().equals(var1.getSignature())) {
               return var4[var5];
            }
         }

         return var1;
      }
   }

   private void processFields(CtClass var1) throws CannotCompileException, NotFoundException {
      CtField[] var2 = var1.getDeclaredFields();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         CtField var4 = var2[var3];
         int var5 = var4.getModifiers();
         if ((var5 & 1) != 0 && (var5 & 16) == 0) {
            var5 |= 8;
            String var6 = var4.getName();
            CtClass var7 = var4.getType();
            CtMethod var8 = CtNewMethod.wrapped(var7, "_r_" + var6, this.readParam, (CtClass[])null, this.trapRead, CtMethod.ConstParameter.string(var6), var1);
            var8.setModifiers(var5);
            var1.addMethod(var8);
            CtClass[] var9 = new CtClass[]{this.classPool.get("java.lang.Object"), var7};
            var8 = CtNewMethod.wrapped(CtClass.voidType, "_w_" + var6, var9, (CtClass[])null, this.trapWrite, CtMethod.ConstParameter.string(var6), var1);
            var8.setModifiers(var5);
            var1.addMethod(var8);
         }
      }

   }

   public void rebuildClassFile(ClassFile var1) throws BadBytecode {
      if (ClassFile.MAJOR_VERSION >= 50) {
         Iterator var2 = var1.getMethods().iterator();

         while(var2.hasNext()) {
            MethodInfo var3 = (MethodInfo)var2.next();
            var3.rebuildStackMap(this.classPool);
         }

      }
   }
}
