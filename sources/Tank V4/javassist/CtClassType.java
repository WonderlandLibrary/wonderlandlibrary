package javassist;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ConstantAttribute;
import javassist.bytecode.Descriptor;
import javassist.bytecode.EnclosingMethodAttribute;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.InnerClassesAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.compiler.AccessorMaker;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.expr.ExprEditor;

class CtClassType extends CtClass {
   ClassPool classPool;
   boolean wasChanged;
   private boolean wasFrozen;
   boolean wasPruned;
   boolean gcConstPool;
   ClassFile classfile;
   byte[] rawClassfile;
   private WeakReference memberCache;
   private AccessorMaker accessors;
   private FieldInitLink fieldInitializers;
   private Hashtable hiddenMethods;
   private int uniqueNumberSeed;
   private boolean doPruning;
   private int getCount;
   private static final int GET_THRESHOLD = 2;

   CtClassType(String var1, ClassPool var2) {
      super(var1);
      this.doPruning = ClassPool.doPruning;
      this.classPool = var2;
      this.wasChanged = this.wasFrozen = this.wasPruned = this.gcConstPool = false;
      this.classfile = null;
      this.rawClassfile = null;
      this.memberCache = null;
      this.accessors = null;
      this.fieldInitializers = null;
      this.hiddenMethods = null;
      this.uniqueNumberSeed = 0;
      this.getCount = 0;
   }

   CtClassType(InputStream var1, ClassPool var2) throws IOException {
      this((String)null, var2);
      this.classfile = new ClassFile(new DataInputStream(var1));
      this.qualifiedName = this.classfile.getName();
   }

   CtClassType(ClassFile var1, ClassPool var2) {
      this((String)null, var2);
      this.classfile = var1;
      this.qualifiedName = this.classfile.getName();
   }

   protected void extendToString(StringBuffer var1) {
      if (this.wasChanged) {
         var1.append("changed ");
      }

      if (this.wasFrozen) {
         var1.append("frozen ");
      }

      if (this.wasPruned) {
         var1.append("pruned ");
      }

      var1.append(Modifier.toString(this.getModifiers()));
      var1.append(" class ");
      var1.append(this.getName());

      try {
         CtClass var2 = this.getSuperclass();
         if (var2 != null) {
            String var3 = var2.getName();
            if (!var3.equals("java.lang.Object")) {
               var1.append(" extends " + var2.getName());
            }
         }
      } catch (NotFoundException var4) {
         var1.append(" extends ??");
      }

      try {
         CtClass[] var6 = this.getInterfaces();
         if (var6.length > 0) {
            var1.append(" implements ");
         }

         for(int var8 = 0; var8 < var6.length; ++var8) {
            var1.append(var6[var8].getName());
            var1.append(", ");
         }
      } catch (NotFoundException var5) {
         var1.append(" extends ??");
      }

      CtMember.Cache var7 = this.getMembers();
      this.exToString(var1, " fields=", var7.fieldHead(), var7.lastField());
      this.exToString(var1, " constructors=", var7.consHead(), var7.lastCons());
      this.exToString(var1, " methods=", var7.methodHead(), var7.lastMethod());
   }

   private void exToString(StringBuffer var1, String var2, CtMember var3, CtMember var4) {
      var1.append(var2);

      while(var3 != var4) {
         var3 = var3.next();
         var1.append(var3);
         var1.append(", ");
      }

   }

   public AccessorMaker getAccessorMaker() {
      if (this.accessors == null) {
         this.accessors = new AccessorMaker(this);
      }

      return this.accessors;
   }

   public ClassFile getClassFile2() {
      return this.getClassFile3(true);
   }

   public ClassFile getClassFile3(boolean var1) {
      ClassFile var2 = this.classfile;
      if (var2 != null) {
         return var2;
      } else {
         if (var1) {
            this.classPool.compress();
         }

         if (this.rawClassfile != null) {
            try {
               ClassFile var14 = new ClassFile(new DataInputStream(new ByteArrayInputStream(this.rawClassfile)));
               this.rawClassfile = null;
               this.getCount = 2;
               return this.setClassFile(var14);
            } catch (IOException var9) {
               throw new RuntimeException(var9.toString(), var9);
            }
         } else {
            InputStream var3 = null;

            ClassFile var5;
            BufferedInputStream var13;
            try {
               var3 = this.classPool.openClassfile(this.getName());
               if (var3 == null) {
                  throw new NotFoundException(this.getName());
               }

               var13 = new BufferedInputStream(var3);
               ClassFile var4 = new ClassFile(new DataInputStream(var13));
               if (!var4.getName().equals(this.qualifiedName)) {
                  throw new RuntimeException("cannot find " + this.qualifiedName + ": " + var4.getName() + " found in " + this.qualifiedName.replace('.', '/') + ".class");
               }

               var5 = this.setClassFile(var4);
            } catch (NotFoundException var11) {
               throw new RuntimeException(var11.toString(), var11);
            } catch (IOException var12) {
               throw new RuntimeException(var12.toString(), var12);
            }

            if (var13 != null) {
               try {
                  var13.close();
               } catch (IOException var10) {
               }
            }

            return var5;
         }
      }
   }

   final void incGetCounter() {
      ++this.getCount;
   }

   void compress() {
      if (this.getCount < 2) {
         if (!this.isModified() && ClassPool.releaseUnmodifiedClassFile) {
            this.removeClassFile();
         } else if (this.isFrozen() && !this.wasPruned) {
            this.saveClassFile();
         }
      }

      this.getCount = 0;
   }

   private synchronized void saveClassFile() {
      if (this.classfile != null && this.hasMemberCache() == null) {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         DataOutputStream var2 = new DataOutputStream(var1);

         try {
            this.classfile.write(var2);
            var1.close();
            this.rawClassfile = var1.toByteArray();
            this.classfile = null;
         } catch (IOException var4) {
         }

      }
   }

   private synchronized void removeClassFile() {
      if (this.classfile != null && !this.isModified() && this.hasMemberCache() == null) {
         this.classfile = null;
      }

   }

   private synchronized ClassFile setClassFile(ClassFile var1) {
      if (this.classfile == null) {
         this.classfile = var1;
      }

      return this.classfile;
   }

   public ClassPool getClassPool() {
      return this.classPool;
   }

   void setClassPool(ClassPool var1) {
      this.classPool = var1;
   }

   public URL getURL() throws NotFoundException {
      URL var1 = this.classPool.find(this.getName());
      if (var1 == null) {
         throw new NotFoundException(this.getName());
      } else {
         return var1;
      }
   }

   public boolean isModified() {
      return this.wasChanged;
   }

   public boolean isFrozen() {
      return this.wasFrozen;
   }

   public void freeze() {
      this.wasFrozen = true;
   }

   void checkModify() throws RuntimeException {
      if (this.isFrozen()) {
         String var1 = this.getName() + " class is frozen";
         if (this.wasPruned) {
            var1 = var1 + " and pruned";
         }

         throw new RuntimeException(var1);
      } else {
         this.wasChanged = true;
      }
   }

   public void defrost() {
      this.checkPruned("defrost");
      this.wasFrozen = false;
   }

   public boolean subtypeOf(CtClass var1) throws NotFoundException {
      String var3 = var1.getName();
      if (this != var1 && !this.getName().equals(var3)) {
         ClassFile var4 = this.getClassFile2();
         String var5 = var4.getSuperclass();
         if (var5 != null && var5.equals(var3)) {
            return true;
         } else {
            String[] var6 = var4.getInterfaces();
            int var7 = var6.length;

            int var2;
            for(var2 = 0; var2 < var7; ++var2) {
               if (var6[var2].equals(var3)) {
                  return true;
               }
            }

            if (var5 != null && this.classPool.get(var5).subtypeOf(var1)) {
               return true;
            } else {
               for(var2 = 0; var2 < var7; ++var2) {
                  if (this.classPool.get(var6[var2]).subtypeOf(var1)) {
                     return true;
                  }
               }

               return false;
            }
         }
      } else {
         return true;
      }
   }

   public void setName(String var1) throws RuntimeException {
      String var2 = this.getName();
      if (!var1.equals(var2)) {
         this.classPool.checkNotFrozen(var1);
         ClassFile var3 = this.getClassFile2();
         super.setName(var1);
         var3.setName(var1);
         this.nameReplaced();
         this.classPool.classNameChanged(var2, this);
      }
   }

   public String getGenericSignature() {
      SignatureAttribute var1 = (SignatureAttribute)this.getClassFile2().getAttribute("Signature");
      return var1 == null ? null : var1.getSignature();
   }

   public void setGenericSignature(String var1) {
      ClassFile var2 = this.getClassFile();
      SignatureAttribute var3 = new SignatureAttribute(var2.getConstPool(), var1);
      var2.addAttribute(var3);
   }

   public void replaceClassName(ClassMap var1) throws RuntimeException {
      String var2 = this.getName();
      String var3 = (String)var1.get(Descriptor.toJvmName(var2));
      if (var3 != null) {
         var3 = Descriptor.toJavaName(var3);
         this.classPool.checkNotFrozen(var3);
      }

      super.replaceClassName(var1);
      ClassFile var4 = this.getClassFile2();
      var4.renameClass(var1);
      this.nameReplaced();
      if (var3 != null) {
         super.setName(var3);
         this.classPool.classNameChanged(var2, this);
      }

   }

   public void replaceClassName(String var1, String var2) throws RuntimeException {
      String var3 = this.getName();
      if (var3.equals(var1)) {
         this.setName(var2);
      } else {
         super.replaceClassName(var1, var2);
         this.getClassFile2().renameClass(var1, var2);
         this.nameReplaced();
      }

   }

   public boolean isInterface() {
      return Modifier.isInterface(this.getModifiers());
   }

   public boolean isAnnotation() {
      return Modifier.isAnnotation(this.getModifiers());
   }

   public boolean isEnum() {
      return Modifier.isEnum(this.getModifiers());
   }

   public int getModifiers() {
      ClassFile var1 = this.getClassFile2();
      int var2 = var1.getAccessFlags();
      var2 = AccessFlag.clear(var2, 32);
      int var3 = var1.getInnerAccessFlags();
      if (var3 != -1 && (var3 & 8) != 0) {
         var2 |= 8;
      }

      return AccessFlag.toModifier(var2);
   }

   public CtClass[] getNestedClasses() throws NotFoundException {
      ClassFile var1 = this.getClassFile2();
      InnerClassesAttribute var2 = (InnerClassesAttribute)var1.getAttribute("InnerClasses");
      if (var2 == null) {
         return new CtClass[0];
      } else {
         String var3 = var1.getName() + "$";
         int var4 = var2.tableLength();
         ArrayList var5 = new ArrayList(var4);

         for(int var6 = 0; var6 < var4; ++var6) {
            String var7 = var2.innerClass(var6);
            if (var7 != null && var7.startsWith(var3) && var7.lastIndexOf(36) < var3.length()) {
               var5.add(this.classPool.get(var7));
            }
         }

         return (CtClass[])((CtClass[])var5.toArray(new CtClass[var5.size()]));
      }
   }

   public void setModifiers(int var1) {
      ClassFile var2 = this.getClassFile2();
      if (Modifier.isStatic(var1)) {
         int var3 = var2.getInnerAccessFlags();
         if (var3 == -1 || (var3 & 8) == 0) {
            throw new RuntimeException("cannot change " + this.getName() + " into a static class");
         }

         var1 &= -9;
      }

      this.checkModify();
      var2.setAccessFlags(AccessFlag.of(var1));
   }

   public boolean hasAnnotation(String var1) {
      ClassFile var2 = this.getClassFile2();
      AnnotationsAttribute var3 = (AnnotationsAttribute)var2.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute var4 = (AnnotationsAttribute)var2.getAttribute("RuntimeVisibleAnnotations");
      return hasAnnotationType(var1, this.getClassPool(), var3, var4);
   }

   /** @deprecated */
   static boolean hasAnnotationType(Class var0, ClassPool var1, AnnotationsAttribute var2, AnnotationsAttribute var3) {
      return hasAnnotationType(var0.getName(), var1, var2, var3);
   }

   static boolean hasAnnotationType(String var0, ClassPool var1, AnnotationsAttribute var2, AnnotationsAttribute var3) {
      Annotation[] var4;
      if (var2 == null) {
         var4 = null;
      } else {
         var4 = var2.getAnnotations();
      }

      Annotation[] var5;
      if (var3 == null) {
         var5 = null;
      } else {
         var5 = var3.getAnnotations();
      }

      int var6;
      if (var4 != null) {
         for(var6 = 0; var6 < var4.length; ++var6) {
            if (var4[var6].getTypeName().equals(var0)) {
               return true;
            }
         }
      }

      if (var5 != null) {
         for(var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getTypeName().equals(var0)) {
               return true;
            }
         }
      }

      return false;
   }

   public Object getAnnotation(Class var1) throws ClassNotFoundException {
      ClassFile var2 = this.getClassFile2();
      AnnotationsAttribute var3 = (AnnotationsAttribute)var2.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute var4 = (AnnotationsAttribute)var2.getAttribute("RuntimeVisibleAnnotations");
      return getAnnotationType(var1, this.getClassPool(), var3, var4);
   }

   static Object getAnnotationType(Class var0, ClassPool var1, AnnotationsAttribute var2, AnnotationsAttribute var3) throws ClassNotFoundException {
      Annotation[] var4;
      if (var2 == null) {
         var4 = null;
      } else {
         var4 = var2.getAnnotations();
      }

      Annotation[] var5;
      if (var3 == null) {
         var5 = null;
      } else {
         var5 = var3.getAnnotations();
      }

      String var6 = var0.getName();
      int var7;
      if (var4 != null) {
         for(var7 = 0; var7 < var4.length; ++var7) {
            if (var4[var7].getTypeName().equals(var6)) {
               return toAnnoType(var4[var7], var1);
            }
         }
      }

      if (var5 != null) {
         for(var7 = 0; var7 < var5.length; ++var7) {
            if (var5[var7].getTypeName().equals(var6)) {
               return toAnnoType(var5[var7], var1);
            }
         }
      }

      return null;
   }

   public Object[] getAnnotations() throws ClassNotFoundException {
      return this.getAnnotations(false);
   }

   public Object[] getAvailableAnnotations() {
      try {
         return this.getAnnotations(true);
      } catch (ClassNotFoundException var2) {
         throw new RuntimeException("Unexpected exception ", var2);
      }
   }

   private Object[] getAnnotations(boolean var1) throws ClassNotFoundException {
      ClassFile var2 = this.getClassFile2();
      AnnotationsAttribute var3 = (AnnotationsAttribute)var2.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute var4 = (AnnotationsAttribute)var2.getAttribute("RuntimeVisibleAnnotations");
      return toAnnotationType(var1, this.getClassPool(), var3, var4);
   }

   static Object[] toAnnotationType(boolean var0, ClassPool var1, AnnotationsAttribute var2, AnnotationsAttribute var3) throws ClassNotFoundException {
      Annotation[] var4;
      int var6;
      if (var2 == null) {
         var4 = null;
         var6 = 0;
      } else {
         var4 = var2.getAnnotations();
         var6 = var4.length;
      }

      Annotation[] var5;
      int var7;
      if (var3 == null) {
         var5 = null;
         var7 = 0;
      } else {
         var5 = var3.getAnnotations();
         var7 = var5.length;
      }

      int var9;
      if (!var0) {
         Object[] var13 = new Object[var6 + var7];

         for(var9 = 0; var9 < var6; ++var9) {
            var13[var9] = toAnnoType(var4[var9], var1);
         }

         for(var9 = 0; var9 < var7; ++var9) {
            var13[var9 + var6] = toAnnoType(var5[var9], var1);
         }

         return var13;
      } else {
         ArrayList var8 = new ArrayList();

         for(var9 = 0; var9 < var6; ++var9) {
            try {
               var8.add(toAnnoType(var4[var9], var1));
            } catch (ClassNotFoundException var12) {
            }
         }

         for(var9 = 0; var9 < var7; ++var9) {
            try {
               var8.add(toAnnoType(var5[var9], var1));
            } catch (ClassNotFoundException var11) {
            }
         }

         return var8.toArray();
      }
   }

   static Object[][] toAnnotationType(boolean var0, ClassPool var1, ParameterAnnotationsAttribute var2, ParameterAnnotationsAttribute var3, MethodInfo var4) throws ClassNotFoundException {
      boolean var5 = false;
      int var17;
      if (var2 != null) {
         var17 = var2.numParameters();
      } else if (var3 != null) {
         var17 = var3.numParameters();
      } else {
         var17 = Descriptor.numOfParameters(var4.getDescriptor());
      }

      Object[][] var6 = new Object[var17][];

      for(int var7 = 0; var7 < var17; ++var7) {
         Annotation[] var8;
         int var10;
         if (var2 == null) {
            var8 = null;
            var10 = 0;
         } else {
            var8 = var2.getAnnotations()[var7];
            var10 = var8.length;
         }

         Annotation[] var9;
         int var11;
         if (var3 == null) {
            var9 = null;
            var11 = 0;
         } else {
            var9 = var3.getAnnotations()[var7];
            var11 = var9.length;
         }

         if (!var0) {
            var6[var7] = new Object[var10 + var11];

            int var18;
            for(var18 = 0; var18 < var10; ++var18) {
               var6[var7][var18] = toAnnoType(var8[var18], var1);
            }

            for(var18 = 0; var18 < var11; ++var18) {
               var6[var7][var18 + var10] = toAnnoType(var9[var18], var1);
            }
         } else {
            ArrayList var12 = new ArrayList();

            int var13;
            for(var13 = 0; var13 < var10; ++var13) {
               try {
                  var12.add(toAnnoType(var8[var13], var1));
               } catch (ClassNotFoundException var16) {
               }
            }

            for(var13 = 0; var13 < var11; ++var13) {
               try {
                  var12.add(toAnnoType(var9[var13], var1));
               } catch (ClassNotFoundException var15) {
               }
            }

            var6[var7] = var12.toArray();
         }
      }

      return var6;
   }

   private static Object toAnnoType(Annotation var0, ClassPool var1) throws ClassNotFoundException {
      try {
         ClassLoader var2 = var1.getClassLoader();
         return var0.toAnnotationType(var2, var1);
      } catch (ClassNotFoundException var8) {
         ClassLoader var3 = var1.getClass().getClassLoader();

         try {
            return var0.toAnnotationType(var3, var1);
         } catch (ClassNotFoundException var7) {
            try {
               Class var5 = var1.get(var0.getTypeName()).toClass();
               return AnnotationImpl.make(var5.getClassLoader(), var5, var1, var0);
            } catch (Throwable var6) {
               throw new ClassNotFoundException(var0.getTypeName());
            }
         }
      }
   }

   public boolean subclassOf(CtClass var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.getName();
         Object var3 = this;

         try {
            while(var3 != null) {
               if (((CtClass)var3).getName().equals(var2)) {
                  return true;
               }

               var3 = ((CtClass)var3).getSuperclass();
            }
         } catch (Exception var5) {
         }

         return false;
      }
   }

   public CtClass getSuperclass() throws NotFoundException {
      String var1 = this.getClassFile2().getSuperclass();
      return var1 == null ? null : this.classPool.get(var1);
   }

   public void setSuperclass(CtClass var1) throws CannotCompileException {
      this.checkModify();
      if (this.isInterface()) {
         this.addInterface(var1);
      } else {
         this.getClassFile2().setSuperclass(var1.getName());
      }

   }

   public CtClass[] getInterfaces() throws NotFoundException {
      String[] var1 = this.getClassFile2().getInterfaces();
      int var2 = var1.length;
      CtClass[] var3 = new CtClass[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = this.classPool.get(var1[var4]);
      }

      return var3;
   }

   public void setInterfaces(CtClass[] var1) {
      this.checkModify();
      String[] var2;
      if (var1 == null) {
         var2 = new String[0];
      } else {
         int var3 = var1.length;
         var2 = new String[var3];

         for(int var4 = 0; var4 < var3; ++var4) {
            var2[var4] = var1[var4].getName();
         }
      }

      this.getClassFile2().setInterfaces(var2);
   }

   public void addInterface(CtClass var1) {
      this.checkModify();
      if (var1 != null) {
         this.getClassFile2().addInterface(var1.getName());
      }

   }

   public CtClass getDeclaringClass() throws NotFoundException {
      ClassFile var1 = this.getClassFile2();
      InnerClassesAttribute var2 = (InnerClassesAttribute)var1.getAttribute("InnerClasses");
      if (var2 == null) {
         return null;
      } else {
         String var3 = this.getName();
         int var4 = var2.tableLength();

         for(int var5 = 0; var5 < var4; ++var5) {
            if (var3.equals(var2.innerClass(var5))) {
               String var6 = var2.outerClass(var5);
               if (var6 != null) {
                  return this.classPool.get(var6);
               }

               EnclosingMethodAttribute var7 = (EnclosingMethodAttribute)var1.getAttribute("EnclosingMethod");
               if (var7 != null) {
                  return this.classPool.get(var7.className());
               }
            }
         }

         return null;
      }
   }

   public CtBehavior getEnclosingBehavior() throws NotFoundException {
      ClassFile var1 = this.getClassFile2();
      EnclosingMethodAttribute var2 = (EnclosingMethodAttribute)var1.getAttribute("EnclosingMethod");
      if (var2 == null) {
         return null;
      } else {
         CtClass var3 = this.classPool.get(var2.className());
         String var4 = var2.methodName();
         if ("<init>".equals(var4)) {
            return var3.getConstructor(var2.methodDescriptor());
         } else {
            return (CtBehavior)("<clinit>".equals(var4) ? var3.getClassInitializer() : var3.getMethod(var4, var2.methodDescriptor()));
         }
      }
   }

   public CtClass makeNestedClass(String var1, boolean var2) {
      if (!var2) {
         throw new RuntimeException("sorry, only nested static class is supported");
      } else {
         this.checkModify();
         CtClass var3 = this.classPool.makeNestedClass(this.getName() + "$" + var1);
         ClassFile var4 = this.getClassFile2();
         ClassFile var5 = var3.getClassFile2();
         InnerClassesAttribute var6 = (InnerClassesAttribute)var4.getAttribute("InnerClasses");
         if (var6 == null) {
            var6 = new InnerClassesAttribute(var4.getConstPool());
            var4.addAttribute(var6);
         }

         var6.append(var3.getName(), this.getName(), var1, var5.getAccessFlags() & -33 | 8);
         var5.addAttribute(var6.copy(var5.getConstPool(), (Map)null));
         return var3;
      }
   }

   private void nameReplaced() {
      CtMember.Cache var1 = this.hasMemberCache();
      if (var1 != null) {
         CtMember var2 = var1.methodHead();
         CtMember var3 = var1.lastMethod();

         while(var2 != var3) {
            var2 = var2.next();
            var2.nameReplaced();
         }
      }

   }

   protected CtMember.Cache hasMemberCache() {
      WeakReference var1 = this.memberCache;
      return var1 != null ? (CtMember.Cache)var1.get() : null;
   }

   protected synchronized CtMember.Cache getMembers() {
      CtMember.Cache var1 = null;
      if (this.memberCache == null || (var1 = (CtMember.Cache)this.memberCache.get()) == null) {
         var1 = new CtMember.Cache(this);
         this.makeFieldCache(var1);
         this.makeBehaviorCache(var1);
         this.memberCache = new WeakReference(var1);
      }

      return var1;
   }

   private void makeFieldCache(CtMember.Cache var1) {
      List var2 = this.getClassFile3(false).getFields();
      int var3 = var2.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         FieldInfo var5 = (FieldInfo)var2.get(var4);
         CtField var6 = new CtField(var5, this);
         var1.addField(var6);
      }

   }

   private void makeBehaviorCache(CtMember.Cache var1) {
      List var2 = this.getClassFile3(false).getMethods();
      int var3 = var2.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         MethodInfo var5 = (MethodInfo)var2.get(var4);
         if (var5.isMethod()) {
            CtMethod var6 = new CtMethod(var5, this);
            var1.addMethod(var6);
         } else {
            CtConstructor var7 = new CtConstructor(var5, this);
            var1.addConstructor(var7);
         }
      }

   }

   public CtField[] getFields() {
      ArrayList var1 = new ArrayList();
      getFields(var1, this);
      return (CtField[])((CtField[])var1.toArray(new CtField[var1.size()]));
   }

   private static void getFields(ArrayList var0, CtClass var1) {
      if (var1 != null) {
         try {
            getFields(var0, var1.getSuperclass());
         } catch (NotFoundException var7) {
         }

         try {
            CtClass[] var4 = var1.getInterfaces();
            int var3 = var4.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               getFields(var0, var4[var2]);
            }
         } catch (NotFoundException var8) {
         }

         CtMember.Cache var9 = ((CtClassType)var1).getMembers();
         CtMember var5 = var9.fieldHead();
         CtMember var6 = var9.lastField();

         while(var5 != var6) {
            var5 = var5.next();
            if (!Modifier.isPrivate(var5.getModifiers())) {
               var0.add(var5);
            }
         }

      }
   }

   public CtField getField(String var1, String var2) throws NotFoundException {
      CtField var3 = this.getField2(var1, var2);
      return this.checkGetField(var3, var1, var2);
   }

   private CtField checkGetField(CtField var1, String var2, String var3) throws NotFoundException {
      if (var1 == null) {
         String var4 = "field: " + var2;
         if (var3 != null) {
            var4 = var4 + " type " + var3;
         }

         throw new NotFoundException(var4 + " in " + this.getName());
      } else {
         return var1;
      }
   }

   CtField getField2(String var1, String var2) {
      CtField var3 = this.getDeclaredField2(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         try {
            CtClass[] var4 = this.getInterfaces();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               CtField var7 = var4[var6].getField2(var1, var2);
               if (var7 != null) {
                  return var7;
               }
            }

            CtClass var9 = this.getSuperclass();
            if (var9 != null) {
               return var9.getField2(var1, var2);
            }
         } catch (NotFoundException var8) {
         }

         return null;
      }
   }

   public CtField[] getDeclaredFields() {
      CtMember.Cache var1 = this.getMembers();
      CtMember var2 = var1.fieldHead();
      CtMember var3 = var1.lastField();
      int var4 = CtMember.Cache.count(var2, var3);
      CtField[] var5 = new CtField[var4];

      for(int var6 = 0; var2 != var3; var5[var6++] = (CtField)var2) {
         var2 = var2.next();
      }

      return var5;
   }

   public CtField getDeclaredField(String var1) throws NotFoundException {
      return this.getDeclaredField(var1, (String)null);
   }

   public CtField getDeclaredField(String var1, String var2) throws NotFoundException {
      CtField var3 = this.getDeclaredField2(var1, var2);
      return this.checkGetField(var3, var1, var2);
   }

   private CtField getDeclaredField2(String var1, String var2) {
      CtMember.Cache var3 = this.getMembers();
      CtMember var4 = var3.fieldHead();
      CtMember var5 = var3.lastField();

      do {
         do {
            if (var4 == var5) {
               return null;
            }

            var4 = var4.next();
         } while(!var4.getName().equals(var1));
      } while(var2 != null && !var2.equals(var4.getSignature()));

      return (CtField)var4;
   }

   public CtBehavior[] getDeclaredBehaviors() {
      CtMember.Cache var1 = this.getMembers();
      CtMember var2 = var1.consHead();
      CtMember var3 = var1.lastCons();
      int var4 = CtMember.Cache.count(var2, var3);
      CtMember var5 = var1.methodHead();
      CtMember var6 = var1.lastMethod();
      int var7 = CtMember.Cache.count(var5, var6);
      CtBehavior[] var8 = new CtBehavior[var4 + var7];

      int var9;
      for(var9 = 0; var2 != var3; var8[var9++] = (CtBehavior)var2) {
         var2 = var2.next();
      }

      while(var5 != var6) {
         var5 = var5.next();
         var8[var9++] = (CtBehavior)var5;
      }

      return var8;
   }

   public CtConstructor[] getConstructors() {
      CtMember.Cache var1 = this.getMembers();
      CtMember var2 = var1.consHead();
      CtMember var3 = var1.lastCons();
      int var4 = 0;
      CtMember var5 = var2;

      while(var5 != var3) {
         var5 = var5.next();
         if ((CtConstructor)var5 == false) {
            ++var4;
         }
      }

      CtConstructor[] var6 = new CtConstructor[var4];
      int var7 = 0;
      var5 = var2;

      while(var5 != var3) {
         var5 = var5.next();
         CtConstructor var8 = (CtConstructor)var5;
         if (var8 == false) {
            var6[var7++] = var8;
         }
      }

      return var6;
   }

   public CtConstructor getConstructor(String var1) throws NotFoundException {
      CtMember.Cache var2 = this.getMembers();
      CtMember var3 = var2.consHead();
      CtMember var4 = var2.lastCons();

      CtConstructor var5;
      do {
         if (var3 == var4) {
            return super.getConstructor(var1);
         }

         var3 = var3.next();
         var5 = (CtConstructor)var3;
      } while(!var5.getMethodInfo2().getDescriptor().equals(var1) || !var5.isConstructor());

      return var5;
   }

   public CtConstructor[] getDeclaredConstructors() {
      CtMember.Cache var1 = this.getMembers();
      CtMember var2 = var1.consHead();
      CtMember var3 = var1.lastCons();
      int var4 = 0;
      CtMember var5 = var2;

      while(var5 != var3) {
         var5 = var5.next();
         CtConstructor var6 = (CtConstructor)var5;
         if (var6.isConstructor()) {
            ++var4;
         }
      }

      CtConstructor[] var9 = new CtConstructor[var4];
      int var7 = 0;
      var5 = var2;

      while(var5 != var3) {
         var5 = var5.next();
         CtConstructor var8 = (CtConstructor)var5;
         if (var8.isConstructor()) {
            var9[var7++] = var8;
         }
      }

      return var9;
   }

   public CtConstructor getClassInitializer() {
      CtMember.Cache var1 = this.getMembers();
      CtMember var2 = var1.consHead();
      CtMember var3 = var1.lastCons();

      CtConstructor var4;
      do {
         if (var2 == var3) {
            return null;
         }

         var2 = var2.next();
         var4 = (CtConstructor)var2;
      } while(!var4.isClassInitializer());

      return var4;
   }

   public CtMethod[] getMethods() {
      HashMap var1 = new HashMap();
      getMethods0(var1, this);
      return (CtMethod[])((CtMethod[])var1.values().toArray(new CtMethod[var1.size()]));
   }

   private static void getMethods0(HashMap var0, CtClass var1) {
      try {
         CtClass[] var2 = var1.getInterfaces();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            getMethods0(var0, var2[var4]);
         }
      } catch (NotFoundException var6) {
      }

      try {
         CtClass var7 = var1.getSuperclass();
         if (var7 != null) {
            getMethods0(var0, var7);
         }
      } catch (NotFoundException var5) {
      }

      if (var1 instanceof CtClassType) {
         CtMember.Cache var8 = ((CtClassType)var1).getMembers();
         CtMember var9 = var8.methodHead();
         CtMember var10 = var8.lastMethod();

         while(var9 != var10) {
            var9 = var9.next();
            if (!Modifier.isPrivate(var9.getModifiers())) {
               var0.put(((CtMethod)var9).getStringRep(), var9);
            }
         }
      }

   }

   public CtMethod getMethod(String var1, String var2) throws NotFoundException {
      CtMethod var3 = getMethod0(this, var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         throw new NotFoundException(var1 + "(..) is not found in " + this.getName());
      }
   }

   private static CtMethod getMethod0(CtClass var0, String var1, String var2) {
      if (var0 instanceof CtClassType) {
         CtMember.Cache var3 = ((CtClassType)var0).getMembers();
         CtMember var4 = var3.methodHead();
         CtMember var5 = var3.lastMethod();

         while(var4 != var5) {
            var4 = var4.next();
            if (var4.getName().equals(var1) && ((CtMethod)var4).getMethodInfo2().getDescriptor().equals(var2)) {
               return (CtMethod)var4;
            }
         }
      }

      try {
         CtClass var9 = var0.getSuperclass();
         if (var9 != null) {
            CtMethod var11 = getMethod0(var9, var1, var2);
            if (var11 != null) {
               return var11;
            }
         }
      } catch (NotFoundException var7) {
      }

      try {
         CtClass[] var10 = var0.getInterfaces();
         int var12 = var10.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            CtMethod var6 = getMethod0(var10[var13], var1, var2);
            if (var6 != null) {
               return var6;
            }
         }
      } catch (NotFoundException var8) {
      }

      return null;
   }

   public CtMethod[] getDeclaredMethods() {
      CtMember.Cache var1 = this.getMembers();
      CtMember var2 = var1.methodHead();
      CtMember var3 = var1.lastMethod();
      int var4 = CtMember.Cache.count(var2, var3);
      CtMethod[] var5 = new CtMethod[var4];

      for(int var6 = 0; var2 != var3; var5[var6++] = (CtMethod)var2) {
         var2 = var2.next();
      }

      return var5;
   }

   public CtMethod[] getDeclaredMethods(String var1) throws NotFoundException {
      CtMember.Cache var2 = this.getMembers();
      CtMember var3 = var2.methodHead();
      CtMember var4 = var2.lastMethod();
      ArrayList var5 = new ArrayList();

      while(var3 != var4) {
         var3 = var3.next();
         if (var3.getName().equals(var1)) {
            var5.add((CtMethod)var3);
         }
      }

      return (CtMethod[])((CtMethod[])var5.toArray(new CtMethod[var5.size()]));
   }

   public CtMethod getDeclaredMethod(String var1) throws NotFoundException {
      CtMember.Cache var2 = this.getMembers();
      CtMember var3 = var2.methodHead();
      CtMember var4 = var2.lastMethod();

      do {
         if (var3 == var4) {
            throw new NotFoundException(var1 + "(..) is not found in " + this.getName());
         }

         var3 = var3.next();
      } while(!var3.getName().equals(var1));

      return (CtMethod)var3;
   }

   public CtMethod getDeclaredMethod(String var1, CtClass[] var2) throws NotFoundException {
      String var3 = Descriptor.ofParameters(var2);
      CtMember.Cache var4 = this.getMembers();
      CtMember var5 = var4.methodHead();
      CtMember var6 = var4.lastMethod();

      do {
         if (var5 == var6) {
            throw new NotFoundException(var1 + "(..) is not found in " + this.getName());
         }

         var5 = var5.next();
      } while(!var5.getName().equals(var1) || !((CtMethod)var5).getMethodInfo2().getDescriptor().startsWith(var3));

      return (CtMethod)var5;
   }

   public void addField(CtField var1, String var2) throws CannotCompileException {
      this.addField(var1, CtField.Initializer.byExpr(var2));
   }

   public void addField(CtField var1, CtField.Initializer var2) throws CannotCompileException {
      this.checkModify();
      if (var1.getDeclaringClass() != this) {
         throw new CannotCompileException("cannot add");
      } else {
         if (var2 == null) {
            var2 = var1.getInit();
         }

         if (var2 != null) {
            var2.check(var1.getSignature());
            int var3 = var1.getModifiers();
            if (Modifier.isStatic(var3) && Modifier.isFinal(var3)) {
               try {
                  ConstPool var4 = this.getClassFile2().getConstPool();
                  int var5 = var2.getConstantValue(var4, var1.getType());
                  if (var5 != 0) {
                     var1.getFieldInfo2().addAttribute(new ConstantAttribute(var4, var5));
                     var2 = null;
                  }
               } catch (NotFoundException var6) {
               }
            }
         }

         this.getMembers().addField(var1);
         this.getClassFile2().addField(var1.getFieldInfo2());
         if (var2 != null) {
            FieldInitLink var7 = new FieldInitLink(var1, var2);
            FieldInitLink var8 = this.fieldInitializers;
            if (var8 == null) {
               this.fieldInitializers = var7;
            } else {
               while(var8.next != null) {
                  var8 = var8.next;
               }

               var8.next = var7;
            }
         }

      }
   }

   public void removeField(CtField var1) throws NotFoundException {
      this.checkModify();
      FieldInfo var2 = var1.getFieldInfo2();
      ClassFile var3 = this.getClassFile2();
      if (var3.getFields().remove(var2)) {
         this.getMembers().remove(var1);
         this.gcConstPool = true;
      } else {
         throw new NotFoundException(var1.toString());
      }
   }

   public CtConstructor makeClassInitializer() throws CannotCompileException {
      CtConstructor var1 = this.getClassInitializer();
      if (var1 != null) {
         return var1;
      } else {
         this.checkModify();
         ClassFile var2 = this.getClassFile2();
         Bytecode var3 = new Bytecode(var2.getConstPool(), 0, 0);
         this.modifyClassConstructor(var2, var3, 0, 0);
         return this.getClassInitializer();
      }
   }

   public void addConstructor(CtConstructor var1) throws CannotCompileException {
      this.checkModify();
      if (var1.getDeclaringClass() != this) {
         throw new CannotCompileException("cannot add");
      } else {
         this.getMembers().addConstructor(var1);
         this.getClassFile2().addMethod(var1.getMethodInfo2());
      }
   }

   public void removeConstructor(CtConstructor var1) throws NotFoundException {
      this.checkModify();
      MethodInfo var2 = var1.getMethodInfo2();
      ClassFile var3 = this.getClassFile2();
      if (var3.getMethods().remove(var2)) {
         this.getMembers().remove(var1);
         this.gcConstPool = true;
      } else {
         throw new NotFoundException(var1.toString());
      }
   }

   public void addMethod(CtMethod var1) throws CannotCompileException {
      this.checkModify();
      if (var1.getDeclaringClass() != this) {
         throw new CannotCompileException("bad declaring class");
      } else {
         int var2 = var1.getModifiers();
         if ((this.getModifiers() & 512) != 0) {
            var1.setModifiers(var2 | 1);
            if ((var2 & 1024) == 0) {
               throw new CannotCompileException("an interface method must be abstract: " + var1.toString());
            }
         }

         this.getMembers().addMethod(var1);
         this.getClassFile2().addMethod(var1.getMethodInfo2());
         if ((var2 & 1024) != 0) {
            this.setModifiers(this.getModifiers() | 1024);
         }

      }
   }

   public void removeMethod(CtMethod var1) throws NotFoundException {
      this.checkModify();
      MethodInfo var2 = var1.getMethodInfo2();
      ClassFile var3 = this.getClassFile2();
      if (var3.getMethods().remove(var2)) {
         this.getMembers().remove(var1);
         this.gcConstPool = true;
      } else {
         throw new NotFoundException(var1.toString());
      }
   }

   public byte[] getAttribute(String var1) {
      AttributeInfo var2 = this.getClassFile2().getAttribute(var1);
      return var2 == null ? null : var2.get();
   }

   public void setAttribute(String var1, byte[] var2) {
      this.checkModify();
      ClassFile var3 = this.getClassFile2();
      var3.addAttribute(new AttributeInfo(var3.getConstPool(), var1, var2));
   }

   public void instrument(CodeConverter var1) throws CannotCompileException {
      this.checkModify();
      ClassFile var2 = this.getClassFile2();
      ConstPool var3 = var2.getConstPool();
      List var4 = var2.getMethods();
      int var5 = var4.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         MethodInfo var7 = (MethodInfo)var4.get(var6);
         var1.doit(this, var7, var3);
      }

   }

   public void instrument(ExprEditor var1) throws CannotCompileException {
      this.checkModify();
      ClassFile var2 = this.getClassFile2();
      List var3 = var2.getMethods();
      int var4 = var3.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         MethodInfo var6 = (MethodInfo)var3.get(var5);
         var1.doit(this, var6);
      }

   }

   public void prune() {
      if (!this.wasPruned) {
         this.wasPruned = this.wasFrozen = true;
         this.getClassFile2().prune();
      }
   }

   public void rebuildClassFile() {
      this.gcConstPool = true;
   }

   public void toBytecode(DataOutputStream var1) throws CannotCompileException, IOException {
      try {
         if (this.isModified()) {
            this.checkPruned("toBytecode");
            ClassFile var2 = this.getClassFile2();
            if (this.gcConstPool) {
               var2.compact();
               this.gcConstPool = false;
            }

            this.modifyClassConstructor(var2);
            this.modifyConstructors(var2);
            if (debugDump != null) {
               this.dumpClassFile(var2);
            }

            var2.write(var1);
            var1.flush();
            this.fieldInitializers = null;
            if (this.doPruning) {
               var2.prune();
               this.wasPruned = true;
            }
         } else {
            this.classPool.writeClassfile(this.getName(), var1);
         }

         this.getCount = 0;
         this.wasFrozen = true;
      } catch (NotFoundException var3) {
         throw new CannotCompileException(var3);
      } catch (IOException var4) {
         throw new CannotCompileException(var4);
      }
   }

   private void dumpClassFile(ClassFile var1) throws IOException {
      DataOutputStream var2 = this.makeFileOutput(debugDump);
      var1.write(var2);
      var2.close();
   }

   private void checkPruned(String var1) {
      if (this.wasPruned) {
         throw new RuntimeException(var1 + "(): " + this.getName() + " was pruned.");
      }
   }

   public boolean stopPruning(boolean var1) {
      boolean var2 = !this.doPruning;
      this.doPruning = !var1;
      return var2;
   }

   private void modifyClassConstructor(ClassFile var1) throws CannotCompileException, NotFoundException {
      if (this.fieldInitializers != null) {
         Bytecode var2 = new Bytecode(var1.getConstPool(), 0, 0);
         Javac var3 = new Javac(var2, this);
         int var4 = 0;
         boolean var5 = false;

         for(FieldInitLink var6 = this.fieldInitializers; var6 != null; var6 = var6.next) {
            CtField var7 = var6.field;
            if (Modifier.isStatic(var7.getModifiers())) {
               var5 = true;
               int var8 = var6.init.compileIfStatic(var7.getType(), var7.getName(), var2, var3);
               if (var4 < var8) {
                  var4 = var8;
               }
            }
         }

         if (var5) {
            this.modifyClassConstructor(var1, var2, var4, 0);
         }

      }
   }

   private void modifyClassConstructor(ClassFile var1, Bytecode var2, int var3, int var4) throws CannotCompileException {
      MethodInfo var5 = var1.getStaticInitializer();
      if (var5 == null) {
         var2.add(177);
         var2.setMaxStack(var3);
         var2.setMaxLocals(var4);
         var5 = new MethodInfo(var1.getConstPool(), "<clinit>", "()V");
         var5.setAccessFlags(8);
         var5.setCodeAttribute(var2.toCodeAttribute());
         var1.addMethod(var5);
         CtMember.Cache var6 = this.hasMemberCache();
         if (var6 != null) {
            var6.addConstructor(new CtConstructor(var5, this));
         }
      } else {
         CodeAttribute var13 = var5.getCodeAttribute();
         if (var13 == null) {
            throw new CannotCompileException("empty <clinit>");
         }

         try {
            CodeIterator var7 = var13.iterator();
            int var8 = var7.insertEx(var2.get());
            var7.insert(var2.getExceptionTable(), var8);
            int var9 = var13.getMaxStack();
            if (var9 < var3) {
               var13.setMaxStack(var3);
            }

            int var10 = var13.getMaxLocals();
            if (var10 < var4) {
               var13.setMaxLocals(var4);
            }
         } catch (BadBytecode var12) {
            throw new CannotCompileException(var12);
         }
      }

      try {
         var5.rebuildStackMapIf6(this.classPool, var1);
      } catch (BadBytecode var11) {
         throw new CannotCompileException(var11);
      }
   }

   private void modifyConstructors(ClassFile var1) throws CannotCompileException, NotFoundException {
      if (this.fieldInitializers != null) {
         ConstPool var2 = var1.getConstPool();
         List var3 = var1.getMethods();
         int var4 = var3.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            MethodInfo var6 = (MethodInfo)var3.get(var5);
            if (var6.isConstructor()) {
               CodeAttribute var7 = var6.getCodeAttribute();
               if (var7 != null) {
                  try {
                     Bytecode var8 = new Bytecode(var2, 0, var7.getMaxLocals());
                     CtClass[] var9 = Descriptor.getParameterTypes(var6.getDescriptor(), this.classPool);
                     int var10 = this.makeFieldInitializer(var8, var9);
                     insertAuxInitializer(var7, var8, var10);
                     var6.rebuildStackMapIf6(this.classPool, var1);
                  } catch (BadBytecode var11) {
                     throw new CannotCompileException(var11);
                  }
               }
            }
         }

      }
   }

   private static void insertAuxInitializer(CodeAttribute var0, Bytecode var1, int var2) throws BadBytecode {
      CodeIterator var3 = var0.iterator();
      int var4 = var3.skipSuperConstructor();
      if (var4 < 0) {
         var4 = var3.skipThisConstructor();
         if (var4 >= 0) {
            return;
         }
      }

      int var5 = var3.insertEx(var1.get());
      var3.insert(var1.getExceptionTable(), var5);
      int var6 = var0.getMaxStack();
      if (var6 < var2) {
         var0.setMaxStack(var2);
      }

   }

   private int makeFieldInitializer(Bytecode var1, CtClass[] var2) throws CannotCompileException, NotFoundException {
      int var3 = 0;
      Javac var4 = new Javac(var1, this);

      try {
         var4.recordParams(var2, false);
      } catch (CompileError var8) {
         throw new CannotCompileException(var8);
      }

      for(FieldInitLink var5 = this.fieldInitializers; var5 != null; var5 = var5.next) {
         CtField var6 = var5.field;
         if (!Modifier.isStatic(var6.getModifiers())) {
            int var7 = var5.init.compile(var6.getType(), var6.getName(), var1, var2, var4);
            if (var3 < var7) {
               var3 = var7;
            }
         }
      }

      return var3;
   }

   Hashtable getHiddenMethods() {
      if (this.hiddenMethods == null) {
         this.hiddenMethods = new Hashtable();
      }

      return this.hiddenMethods;
   }

   int getUniqueNumber() {
      return this.uniqueNumberSeed++;
   }

   public String makeUniqueName(String var1) {
      HashMap var2 = new HashMap();
      this.makeMemberList(var2);
      Set var3 = var2.keySet();
      String[] var4 = new String[var3.size()];
      var3.toArray(var4);
      if (var1 < var4) {
         return var1;
      } else {
         int var5 = 100;

         while(var5 <= 999) {
            String var6 = var1 + var5++;
            if (var6 < var4) {
               return var6;
            }
         }

         throw new RuntimeException("too many unique name");
      }
   }

   private void makeMemberList(HashMap var1) {
      int var2 = this.getModifiers();
      int var4;
      int var5;
      if (Modifier.isAbstract(var2) || Modifier.isInterface(var2)) {
         try {
            CtClass[] var3 = this.getInterfaces();
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
               CtClass var6 = var3[var5];
               if (var6 != null && var6 instanceof CtClassType) {
                  ((CtClassType)var6).makeMemberList(var1);
               }
            }
         } catch (NotFoundException var8) {
         }
      }

      try {
         CtClass var9 = this.getSuperclass();
         if (var9 != null && var9 instanceof CtClassType) {
            ((CtClassType)var9).makeMemberList(var1);
         }
      } catch (NotFoundException var7) {
      }

      List var10 = this.getClassFile2().getMethods();
      var4 = var10.size();

      for(var5 = 0; var5 < var4; ++var5) {
         MethodInfo var11 = (MethodInfo)var10.get(var5);
         var1.put(var11.getName(), this);
      }

      var10 = this.getClassFile2().getFields();
      var4 = var10.size();

      for(var5 = 0; var5 < var4; ++var5) {
         FieldInfo var12 = (FieldInfo)var10.get(var5);
         var1.put(var12.getName(), this);
      }

   }
}
