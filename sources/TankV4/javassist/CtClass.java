package javassist;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Collection;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.compiler.AccessorMaker;
import javassist.expr.ExprEditor;

public abstract class CtClass {
   protected String qualifiedName;
   public static String debugDump = null;
   public static final String version = "3.20.0-GA";
   static final String javaLangObject = "java.lang.Object";
   public static CtClass booleanType = new CtPrimitiveType("boolean", 'Z', "java.lang.Boolean", "booleanValue", "()Z", 172, 4, 1);
   public static CtClass charType;
   public static CtClass byteType;
   public static CtClass shortType;
   public static CtClass intType;
   public static CtClass longType;
   public static CtClass floatType;
   public static CtClass doubleType;
   public static CtClass voidType;
   static CtClass[] primitiveTypes = new CtClass[9];

   public static void main(String[] var0) {
      System.out.println("Javassist version 3.20.0-GA");
      System.out.println("Copyright (C) 1999-2015 Shigeru Chiba. All Rights Reserved.");
   }

   protected CtClass(String var1) {
      this.qualifiedName = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(this.getClass().getName());
      var1.append("@");
      var1.append(Integer.toHexString(this.hashCode()));
      var1.append("[");
      this.extendToString(var1);
      var1.append("]");
      return var1.toString();
   }

   protected void extendToString(StringBuffer var1) {
      var1.append(this.getName());
   }

   public ClassPool getClassPool() {
      return null;
   }

   public ClassFile getClassFile() {
      this.checkModify();
      return this.getClassFile2();
   }

   public ClassFile getClassFile2() {
      return null;
   }

   public AccessorMaker getAccessorMaker() {
      return null;
   }

   public URL getURL() throws NotFoundException {
      throw new NotFoundException(this.getName());
   }

   public boolean isModified() {
      return false;
   }

   public boolean isFrozen() {
      return true;
   }

   public void freeze() {
   }

   void checkModify() throws RuntimeException {
      if (this.isFrozen()) {
         throw new RuntimeException(this.getName() + " class is frozen");
      }
   }

   public void defrost() {
      throw new RuntimeException("cannot defrost " + this.getName());
   }

   public boolean isPrimitive() {
      return false;
   }

   public boolean isArray() {
      return false;
   }

   public CtClass getComponentType() throws NotFoundException {
      return null;
   }

   public boolean subtypeOf(CtClass var1) throws NotFoundException {
      return this == var1 || this.getName().equals(var1.getName());
   }

   public String getName() {
      return this.qualifiedName;
   }

   public final String getSimpleName() {
      String var1 = this.qualifiedName;
      int var2 = var1.lastIndexOf(46);
      return var2 < 0 ? var1 : var1.substring(var2 + 1);
   }

   public final String getPackageName() {
      String var1 = this.qualifiedName;
      int var2 = var1.lastIndexOf(46);
      return var2 < 0 ? null : var1.substring(0, var2);
   }

   public void setName(String var1) {
      this.checkModify();
      if (var1 != null) {
         this.qualifiedName = var1;
      }

   }

   public String getGenericSignature() {
      return null;
   }

   public void setGenericSignature(String var1) {
      this.checkModify();
   }

   public void replaceClassName(String var1, String var2) {
      this.checkModify();
   }

   public void replaceClassName(ClassMap var1) {
      this.checkModify();
   }

   public synchronized Collection getRefClasses() {
      ClassFile var1 = this.getClassFile2();
      if (var1 != null) {
         ClassMap var2 = new ClassMap(this) {
            final CtClass this$0;

            {
               this.this$0 = var1;
            }

            public void put(String var1, String var2) {
               this.put0(var1, var2);
            }

            public Object get(Object var1) {
               String var2 = toJavaName((String)var1);
               this.put0(var2, var2);
               return null;
            }

            public void fix(String var1) {
            }
         };
         var1.getRefClasses(var2);
         return var2.values();
      } else {
         return null;
      }
   }

   public boolean isInterface() {
      return false;
   }

   public boolean isAnnotation() {
      return false;
   }

   public boolean isEnum() {
      return false;
   }

   public int getModifiers() {
      return 0;
   }

   public boolean hasAnnotation(Class var1) {
      return this.hasAnnotation(var1.getName());
   }

   public boolean hasAnnotation(String var1) {
      return false;
   }

   public Object getAnnotation(Class var1) throws ClassNotFoundException {
      return null;
   }

   public Object[] getAnnotations() throws ClassNotFoundException {
      return new Object[0];
   }

   public Object[] getAvailableAnnotations() {
      return new Object[0];
   }

   public CtClass[] getDeclaredClasses() throws NotFoundException {
      return this.getNestedClasses();
   }

   public CtClass[] getNestedClasses() throws NotFoundException {
      return new CtClass[0];
   }

   public void setModifiers(int var1) {
      this.checkModify();
   }

   public boolean subclassOf(CtClass var1) {
      return false;
   }

   public CtClass getSuperclass() throws NotFoundException {
      return null;
   }

   public void setSuperclass(CtClass var1) throws CannotCompileException {
      this.checkModify();
   }

   public CtClass[] getInterfaces() throws NotFoundException {
      return new CtClass[0];
   }

   public void setInterfaces(CtClass[] var1) {
      this.checkModify();
   }

   public void addInterface(CtClass var1) {
      this.checkModify();
   }

   public CtClass getDeclaringClass() throws NotFoundException {
      return null;
   }

   /** @deprecated */
   public final CtMethod getEnclosingMethod() throws NotFoundException {
      CtBehavior var1 = this.getEnclosingBehavior();
      if (var1 == null) {
         return null;
      } else if (var1 instanceof CtMethod) {
         return (CtMethod)var1;
      } else {
         throw new NotFoundException(var1.getLongName() + " is enclosing " + this.getName());
      }
   }

   public CtBehavior getEnclosingBehavior() throws NotFoundException {
      return null;
   }

   public CtClass makeNestedClass(String var1, boolean var2) {
      throw new RuntimeException(this.getName() + " is not a class");
   }

   public CtField[] getFields() {
      return new CtField[0];
   }

   public CtField getField(String var1) throws NotFoundException {
      return this.getField(var1, (String)null);
   }

   public CtField getField(String var1, String var2) throws NotFoundException {
      throw new NotFoundException(var1);
   }

   CtField getField2(String var1, String var2) {
      return null;
   }

   public CtField[] getDeclaredFields() {
      return new CtField[0];
   }

   public CtField getDeclaredField(String var1) throws NotFoundException {
      throw new NotFoundException(var1);
   }

   public CtField getDeclaredField(String var1, String var2) throws NotFoundException {
      throw new NotFoundException(var1);
   }

   public CtBehavior[] getDeclaredBehaviors() {
      return new CtBehavior[0];
   }

   public CtConstructor[] getConstructors() {
      return new CtConstructor[0];
   }

   public CtConstructor getConstructor(String var1) throws NotFoundException {
      throw new NotFoundException("no such constructor");
   }

   public CtConstructor[] getDeclaredConstructors() {
      return new CtConstructor[0];
   }

   public CtConstructor getDeclaredConstructor(CtClass[] var1) throws NotFoundException {
      String var2 = Descriptor.ofConstructor(var1);
      return this.getConstructor(var2);
   }

   public CtConstructor getClassInitializer() {
      return null;
   }

   public CtMethod[] getMethods() {
      return new CtMethod[0];
   }

   public CtMethod getMethod(String var1, String var2) throws NotFoundException {
      throw new NotFoundException(var1);
   }

   public CtMethod[] getDeclaredMethods() {
      return new CtMethod[0];
   }

   public CtMethod getDeclaredMethod(String var1, CtClass[] var2) throws NotFoundException {
      throw new NotFoundException(var1);
   }

   public CtMethod[] getDeclaredMethods(String var1) throws NotFoundException {
      throw new NotFoundException(var1);
   }

   public CtMethod getDeclaredMethod(String var1) throws NotFoundException {
      throw new NotFoundException(var1);
   }

   public CtConstructor makeClassInitializer() throws CannotCompileException {
      throw new CannotCompileException("not a class");
   }

   public void addConstructor(CtConstructor var1) throws CannotCompileException {
      this.checkModify();
   }

   public void removeConstructor(CtConstructor var1) throws NotFoundException {
      this.checkModify();
   }

   public void addMethod(CtMethod var1) throws CannotCompileException {
      this.checkModify();
   }

   public void removeMethod(CtMethod var1) throws NotFoundException {
      this.checkModify();
   }

   public void addField(CtField var1) throws CannotCompileException {
      this.addField(var1, (CtField.Initializer)null);
   }

   public void addField(CtField var1, String var2) throws CannotCompileException {
      this.checkModify();
   }

   public void addField(CtField var1, CtField.Initializer var2) throws CannotCompileException {
      this.checkModify();
   }

   public void removeField(CtField var1) throws NotFoundException {
      this.checkModify();
   }

   public byte[] getAttribute(String var1) {
      return null;
   }

   public void setAttribute(String var1, byte[] var2) {
      this.checkModify();
   }

   public void instrument(CodeConverter var1) throws CannotCompileException {
      this.checkModify();
   }

   public void instrument(ExprEditor var1) throws CannotCompileException {
      this.checkModify();
   }

   public Class toClass() throws CannotCompileException {
      return this.getClassPool().toClass(this);
   }

   public Class toClass(ClassLoader var1, ProtectionDomain var2) throws CannotCompileException {
      ClassPool var3 = this.getClassPool();
      if (var1 == null) {
         var1 = var3.getClassLoader();
      }

      return var3.toClass(this, var1, var2);
   }

   /** @deprecated */
   public final Class toClass(ClassLoader var1) throws CannotCompileException {
      return this.getClassPool().toClass(this, var1);
   }

   public void detach() {
      ClassPool var1 = this.getClassPool();
      CtClass var2 = var1.removeCached(this.getName());
      if (var2 != this) {
         var1.cacheCtClass(this.getName(), var2, false);
      }

   }

   public boolean stopPruning(boolean var1) {
      return true;
   }

   public void prune() {
   }

   void incGetCounter() {
   }

   public void rebuildClassFile() {
   }

   public byte[] toBytecode() throws IOException, CannotCompileException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      DataOutputStream var2 = new DataOutputStream(var1);
      this.toBytecode(var2);
      var2.close();
      return var1.toByteArray();
   }

   public void writeFile() throws NotFoundException, IOException, CannotCompileException {
      this.writeFile(".");
   }

   public void writeFile(String var1) throws CannotCompileException, IOException {
      DataOutputStream var2 = this.makeFileOutput(var1);
      this.toBytecode(var2);
      var2.close();
   }

   protected DataOutputStream makeFileOutput(String var1) {
      String var2 = this.getName();
      String var3 = var1 + File.separatorChar + var2.replace('.', File.separatorChar) + ".class";
      int var4 = var3.lastIndexOf(File.separatorChar);
      if (var4 > 0) {
         String var5 = var3.substring(0, var4);
         if (!var5.equals(".")) {
            (new File(var5)).mkdirs();
         }
      }

      return new DataOutputStream(new BufferedOutputStream(new CtClass.DelayedFileOutputStream(var3)));
   }

   public void debugWriteFile() {
      this.debugWriteFile(".");
   }

   public void debugWriteFile(String var1) {
      try {
         boolean var2 = this.stopPruning(true);
         this.writeFile(var1);
         this.defrost();
         this.stopPruning(var2);
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public void toBytecode(DataOutputStream var1) throws CannotCompileException, IOException {
      throw new CannotCompileException("not a class");
   }

   public String makeUniqueName(String var1) {
      throw new RuntimeException("not available in " + this.getName());
   }

   void compress() {
   }

   static {
      primitiveTypes[0] = booleanType;
      charType = new CtPrimitiveType("char", 'C', "java.lang.Character", "charValue", "()C", 172, 5, 1);
      primitiveTypes[1] = charType;
      byteType = new CtPrimitiveType("byte", 'B', "java.lang.Byte", "byteValue", "()B", 172, 8, 1);
      primitiveTypes[2] = byteType;
      shortType = new CtPrimitiveType("short", 'S', "java.lang.Short", "shortValue", "()S", 172, 9, 1);
      primitiveTypes[3] = shortType;
      intType = new CtPrimitiveType("int", 'I', "java.lang.Integer", "intValue", "()I", 172, 10, 1);
      primitiveTypes[4] = intType;
      longType = new CtPrimitiveType("long", 'J', "java.lang.Long", "longValue", "()J", 173, 11, 2);
      primitiveTypes[5] = longType;
      floatType = new CtPrimitiveType("float", 'F', "java.lang.Float", "floatValue", "()F", 174, 6, 1);
      primitiveTypes[6] = floatType;
      doubleType = new CtPrimitiveType("double", 'D', "java.lang.Double", "doubleValue", "()D", 175, 7, 2);
      primitiveTypes[7] = doubleType;
      voidType = new CtPrimitiveType("void", 'V', "java.lang.Void", (String)null, (String)null, 177, 0, 0);
      primitiveTypes[8] = voidType;
   }

   static class DelayedFileOutputStream extends OutputStream {
      private FileOutputStream file = null;
      private String filename;

      DelayedFileOutputStream(String var1) {
         this.filename = var1;
      }

      private void init() throws IOException {
         if (this.file == null) {
            this.file = new FileOutputStream(this.filename);
         }

      }

      public void write(int var1) throws IOException {
         this.init();
         this.file.write(var1);
      }

      public void write(byte[] var1) throws IOException {
         this.init();
         this.file.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.init();
         this.file.write(var1, var2, var3);
      }

      public void flush() throws IOException {
         this.init();
         this.file.flush();
      }

      public void close() throws IOException {
         this.init();
         this.file.close();
      }
   }
}
