package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javassist.CannotCompileException;

public final class ClassFile {
   int major;
   int minor;
   ConstPool constPool;
   int thisClass;
   int accessFlags;
   int superClass;
   int[] interfaces;
   ArrayList fields;
   ArrayList methods;
   ArrayList attributes;
   String thisclassname;
   String[] cachedInterfaces;
   String cachedSuperclass;
   public static final int JAVA_1 = 45;
   public static final int JAVA_2 = 46;
   public static final int JAVA_3 = 47;
   public static final int JAVA_4 = 48;
   public static final int JAVA_5 = 49;
   public static final int JAVA_6 = 50;
   public static final int JAVA_7 = 51;
   public static final int JAVA_8 = 52;
   public static final int MAJOR_VERSION;

   public ClassFile(DataInputStream var1) throws IOException {
      this.read(var1);
   }

   public ClassFile(boolean var1, String var2, String var3) {
      this.major = MAJOR_VERSION;
      this.minor = 0;
      this.constPool = new ConstPool(var2);
      this.thisClass = this.constPool.getThisClassInfo();
      if (var1) {
         this.accessFlags = 1536;
      } else {
         this.accessFlags = 32;
      }

      this.initSuperclass(var3);
      this.interfaces = null;
      this.fields = new ArrayList();
      this.methods = new ArrayList();
      this.thisclassname = var2;
      this.attributes = new ArrayList();
      this.attributes.add(new SourceFileAttribute(this.constPool, getSourcefileName(this.thisclassname)));
   }

   private void initSuperclass(String var1) {
      if (var1 != null) {
         this.superClass = this.constPool.addClassInfo(var1);
         this.cachedSuperclass = var1;
      } else {
         this.superClass = this.constPool.addClassInfo("java.lang.Object");
         this.cachedSuperclass = "java.lang.Object";
      }

   }

   private static String getSourcefileName(String var0) {
      int var1 = var0.lastIndexOf(46);
      if (var1 >= 0) {
         var0 = var0.substring(var1 + 1);
      }

      return var0 + ".java";
   }

   public void compact() {
      ConstPool var1 = this.compact0();
      ArrayList var2 = this.methods;
      int var3 = var2.size();

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         MethodInfo var5 = (MethodInfo)var2.get(var4);
         var5.compact(var1);
      }

      var2 = this.fields;
      var3 = var2.size();

      for(var4 = 0; var4 < var3; ++var4) {
         FieldInfo var6 = (FieldInfo)var2.get(var4);
         var6.compact(var1);
      }

      this.attributes = AttributeInfo.copyAll(this.attributes, var1);
      this.constPool = var1;
   }

   private ConstPool compact0() {
      ConstPool var1 = new ConstPool(this.thisclassname);
      this.thisClass = var1.getThisClassInfo();
      String var2 = this.getSuperclass();
      if (var2 != null) {
         this.superClass = var1.addClassInfo(this.getSuperclass());
      }

      if (this.interfaces != null) {
         int var3 = this.interfaces.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            this.interfaces[var4] = var1.addClassInfo(this.constPool.getClassInfo(this.interfaces[var4]));
         }
      }

      return var1;
   }

   public void prune() {
      ConstPool var1 = this.compact0();
      ArrayList var2 = new ArrayList();
      AttributeInfo var3 = this.getAttribute("RuntimeInvisibleAnnotations");
      if (var3 != null) {
         var3 = var3.copy(var1, (Map)null);
         var2.add(var3);
      }

      AttributeInfo var4 = this.getAttribute("RuntimeVisibleAnnotations");
      if (var4 != null) {
         var4 = var4.copy(var1, (Map)null);
         var2.add(var4);
      }

      AttributeInfo var5 = this.getAttribute("Signature");
      if (var5 != null) {
         var5 = var5.copy(var1, (Map)null);
         var2.add(var5);
      }

      ArrayList var6 = this.methods;
      int var7 = var6.size();

      int var8;
      for(var8 = 0; var8 < var7; ++var8) {
         MethodInfo var9 = (MethodInfo)var6.get(var8);
         var9.prune(var1);
      }

      var6 = this.fields;
      var7 = var6.size();

      for(var8 = 0; var8 < var7; ++var8) {
         FieldInfo var10 = (FieldInfo)var6.get(var8);
         var10.prune(var1);
      }

      this.attributes = var2;
      this.constPool = var1;
   }

   public ConstPool getConstPool() {
      return this.constPool;
   }

   public boolean isInterface() {
      return (this.accessFlags & 512) != 0;
   }

   public boolean isFinal() {
      return (this.accessFlags & 16) != 0;
   }

   public boolean isAbstract() {
      return (this.accessFlags & 1024) != 0;
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   public void setAccessFlags(int var1) {
      if ((var1 & 512) == 0) {
         var1 |= 32;
      }

      this.accessFlags = var1;
   }

   public int getInnerAccessFlags() {
      InnerClassesAttribute var1 = (InnerClassesAttribute)this.getAttribute("InnerClasses");
      if (var1 == null) {
         return -1;
      } else {
         String var2 = this.getName();
         int var3 = var1.tableLength();

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var2.equals(var1.innerClass(var4))) {
               return var1.accessFlags(var4);
            }
         }

         return -1;
      }
   }

   public String getName() {
      return this.thisclassname;
   }

   public void setName(String var1) {
      this.renameClass(this.thisclassname, var1);
   }

   public String getSuperclass() {
      if (this.cachedSuperclass == null) {
         this.cachedSuperclass = this.constPool.getClassInfo(this.superClass);
      }

      return this.cachedSuperclass;
   }

   public int getSuperclassId() {
      return this.superClass;
   }

   public void setSuperclass(String var1) throws CannotCompileException {
      if (var1 == null) {
         var1 = "java.lang.Object";
      }

      try {
         this.superClass = this.constPool.addClassInfo(var1);
         ArrayList var2 = this.methods;
         int var3 = var2.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            MethodInfo var5 = (MethodInfo)var2.get(var4);
            var5.setSuperclass(var1);
         }
      } catch (BadBytecode var6) {
         throw new CannotCompileException(var6);
      }

      this.cachedSuperclass = var1;
   }

   public final void renameClass(String var1, String var2) {
      if (!var1.equals(var2)) {
         if (var1.equals(this.thisclassname)) {
            this.thisclassname = var2;
         }

         var1 = Descriptor.toJvmName(var1);
         var2 = Descriptor.toJvmName(var2);
         this.constPool.renameClass(var1, var2);
         AttributeInfo.renameClass(this.attributes, var1, var2);
         ArrayList var3 = this.methods;
         int var4 = var3.size();

         int var5;
         String var7;
         for(var5 = 0; var5 < var4; ++var5) {
            MethodInfo var6 = (MethodInfo)var3.get(var5);
            var7 = var6.getDescriptor();
            var6.setDescriptor(Descriptor.rename(var7, var1, var2));
            AttributeInfo.renameClass(var6.getAttributes(), var1, var2);
         }

         var3 = this.fields;
         var4 = var3.size();

         for(var5 = 0; var5 < var4; ++var5) {
            FieldInfo var8 = (FieldInfo)var3.get(var5);
            var7 = var8.getDescriptor();
            var8.setDescriptor(Descriptor.rename(var7, var1, var2));
            AttributeInfo.renameClass(var8.getAttributes(), var1, var2);
         }

      }
   }

   public final void renameClass(Map var1) {
      String var2 = (String)var1.get(Descriptor.toJvmName(this.thisclassname));
      if (var2 != null) {
         this.thisclassname = Descriptor.toJavaName(var2);
      }

      this.constPool.renameClass(var1);
      AttributeInfo.renameClass((List)this.attributes, (Map)var1);
      ArrayList var3 = this.methods;
      int var4 = var3.size();

      int var5;
      String var7;
      for(var5 = 0; var5 < var4; ++var5) {
         MethodInfo var6 = (MethodInfo)var3.get(var5);
         var7 = var6.getDescriptor();
         var6.setDescriptor(Descriptor.rename(var7, var1));
         AttributeInfo.renameClass(var6.getAttributes(), var1);
      }

      var3 = this.fields;
      var4 = var3.size();

      for(var5 = 0; var5 < var4; ++var5) {
         FieldInfo var8 = (FieldInfo)var3.get(var5);
         var7 = var8.getDescriptor();
         var8.setDescriptor(Descriptor.rename(var7, var1));
         AttributeInfo.renameClass(var8.getAttributes(), var1);
      }

   }

   public final void getRefClasses(Map var1) {
      this.constPool.renameClass(var1);
      AttributeInfo.getRefClasses(this.attributes, var1);
      ArrayList var2 = this.methods;
      int var3 = var2.size();

      int var4;
      String var6;
      for(var4 = 0; var4 < var3; ++var4) {
         MethodInfo var5 = (MethodInfo)var2.get(var4);
         var6 = var5.getDescriptor();
         Descriptor.rename(var6, var1);
         AttributeInfo.getRefClasses(var5.getAttributes(), var1);
      }

      var2 = this.fields;
      var3 = var2.size();

      for(var4 = 0; var4 < var3; ++var4) {
         FieldInfo var7 = (FieldInfo)var2.get(var4);
         var6 = var7.getDescriptor();
         Descriptor.rename(var6, var1);
         AttributeInfo.getRefClasses(var7.getAttributes(), var1);
      }

   }

   public String[] getInterfaces() {
      if (this.cachedInterfaces != null) {
         return this.cachedInterfaces;
      } else {
         String[] var1 = null;
         if (this.interfaces == null) {
            var1 = new String[0];
         } else {
            int var2 = this.interfaces.length;
            String[] var3 = new String[var2];

            for(int var4 = 0; var4 < var2; ++var4) {
               var3[var4] = this.constPool.getClassInfo(this.interfaces[var4]);
            }

            var1 = var3;
         }

         this.cachedInterfaces = var1;
         return var1;
      }
   }

   public void setInterfaces(String[] var1) {
      this.cachedInterfaces = null;
      if (var1 != null) {
         int var2 = var1.length;
         this.interfaces = new int[var2];

         for(int var3 = 0; var3 < var2; ++var3) {
            this.interfaces[var3] = this.constPool.addClassInfo(var1[var3]);
         }
      }

   }

   public void addInterface(String var1) {
      this.cachedInterfaces = null;
      int var2 = this.constPool.addClassInfo(var1);
      if (this.interfaces == null) {
         this.interfaces = new int[1];
         this.interfaces[0] = var2;
      } else {
         int var3 = this.interfaces.length;
         int[] var4 = new int[var3 + 1];
         System.arraycopy(this.interfaces, 0, var4, 0, var3);
         var4[var3] = var2;
         this.interfaces = var4;
      }

   }

   public List getFields() {
      return this.fields;
   }

   public void addField(FieldInfo var1) throws DuplicateMemberException {
      this.testExistingField(var1.getName(), var1.getDescriptor());
      this.fields.add(var1);
   }

   public final void addField2(FieldInfo var1) {
      this.fields.add(var1);
   }

   private void testExistingField(String var1, String var2) throws DuplicateMemberException {
      ListIterator var3 = this.fields.listIterator(0);

      FieldInfo var4;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var4 = (FieldInfo)var3.next();
      } while(!var4.getName().equals(var1));

      throw new DuplicateMemberException("duplicate field: " + var1);
   }

   public List getMethods() {
      return this.methods;
   }

   public MethodInfo getMethod(String var1) {
      ArrayList var2 = this.methods;
      int var3 = var2.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         MethodInfo var5 = (MethodInfo)var2.get(var4);
         if (var5.getName().equals(var1)) {
            return var5;
         }
      }

      return null;
   }

   public MethodInfo getStaticInitializer() {
      return this.getMethod("<clinit>");
   }

   public void addMethod(MethodInfo var1) throws DuplicateMemberException {
      this.testExistingMethod(var1);
      this.methods.add(var1);
   }

   public final void addMethod2(MethodInfo var1) {
      this.methods.add(var1);
   }

   private void testExistingMethod(MethodInfo var1) throws DuplicateMemberException {
      String var2 = var1.getName();
      String var3 = var1.getDescriptor();
      ListIterator var4 = this.methods.listIterator(0);

      do {
         if (!var4.hasNext()) {
            return;
         }

         MethodInfo var10003 = (MethodInfo)var4.next();
      } while(var4 != false);

      throw new DuplicateMemberException("duplicate method: " + var2 + " in " + this.getName());
   }

   public List getAttributes() {
      return this.attributes;
   }

   public AttributeInfo getAttribute(String var1) {
      ArrayList var2 = this.attributes;
      int var3 = var2.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         AttributeInfo var5 = (AttributeInfo)var2.get(var4);
         if (var5.getName().equals(var1)) {
            return var5;
         }
      }

      return null;
   }

   public void addAttribute(AttributeInfo var1) {
      AttributeInfo.remove(this.attributes, var1.getName());
      this.attributes.add(var1);
   }

   public String getSourceFile() {
      SourceFileAttribute var1 = (SourceFileAttribute)this.getAttribute("SourceFile");
      return var1 == null ? null : var1.getFileName();
   }

   private void read(DataInputStream var1) throws IOException {
      int var4 = var1.readInt();
      if (var4 != -889275714) {
         throw new IOException("bad magic number: " + Integer.toHexString(var4));
      } else {
         this.minor = var1.readUnsignedShort();
         this.major = var1.readUnsignedShort();
         this.constPool = new ConstPool(var1);
         this.accessFlags = var1.readUnsignedShort();
         this.thisClass = var1.readUnsignedShort();
         this.constPool.setThisClassInfo(this.thisClass);
         this.superClass = var1.readUnsignedShort();
         int var3 = var1.readUnsignedShort();
         int var2;
         if (var3 == 0) {
            this.interfaces = null;
         } else {
            this.interfaces = new int[var3];

            for(var2 = 0; var2 < var3; ++var2) {
               this.interfaces[var2] = var1.readUnsignedShort();
            }
         }

         ConstPool var5 = this.constPool;
         var3 = var1.readUnsignedShort();
         this.fields = new ArrayList();

         for(var2 = 0; var2 < var3; ++var2) {
            this.addField2(new FieldInfo(var5, var1));
         }

         var3 = var1.readUnsignedShort();
         this.methods = new ArrayList();

         for(var2 = 0; var2 < var3; ++var2) {
            this.addMethod2(new MethodInfo(var5, var1));
         }

         this.attributes = new ArrayList();
         var3 = var1.readUnsignedShort();

         for(var2 = 0; var2 < var3; ++var2) {
            this.addAttribute(AttributeInfo.read(var5, var1));
         }

         this.thisclassname = this.constPool.getClassInfo(this.thisClass);
      }
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeInt(-889275714);
      var1.writeShort(this.minor);
      var1.writeShort(this.major);
      this.constPool.write(var1);
      var1.writeShort(this.accessFlags);
      var1.writeShort(this.thisClass);
      var1.writeShort(this.superClass);
      int var3;
      if (this.interfaces == null) {
         var3 = 0;
      } else {
         var3 = this.interfaces.length;
      }

      var1.writeShort(var3);

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         var1.writeShort(this.interfaces[var2]);
      }

      ArrayList var4 = this.fields;
      var3 = var4.size();
      var1.writeShort(var3);

      for(var2 = 0; var2 < var3; ++var2) {
         FieldInfo var5 = (FieldInfo)var4.get(var2);
         var5.write(var1);
      }

      var4 = this.methods;
      var3 = var4.size();
      var1.writeShort(var3);

      for(var2 = 0; var2 < var3; ++var2) {
         MethodInfo var6 = (MethodInfo)var4.get(var2);
         var6.write(var1);
      }

      var1.writeShort(this.attributes.size());
      AttributeInfo.writeAll(this.attributes, var1);
   }

   public int getMajorVersion() {
      return this.major;
   }

   public void setMajorVersion(int var1) {
      this.major = var1;
   }

   public int getMinorVersion() {
      return this.minor;
   }

   public void setMinorVersion(int var1) {
      this.minor = var1;
   }

   public void setVersionToJava5() {
      this.major = 49;
      this.minor = 0;
   }

   static {
      byte var0 = 47;

      try {
         Class.forName("java.lang.StringBuilder");
         boolean var3 = true;
         Class.forName("java.util.zip.DeflaterInputStream");
         var3 = true;
         Class.forName("java.lang.invoke.CallSite");
         var3 = true;
         Class.forName("java.util.function.Function");
         var0 = 52;
      } catch (Throwable var2) {
      }

      MAJOR_VERSION = var0;
   }
}
