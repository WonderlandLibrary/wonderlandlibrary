package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javassist.ClassPool;
import javassist.bytecode.stackmap.MapMaker;

public class MethodInfo {
   ConstPool constPool;
   int accessFlags;
   int name;
   String cachedName;
   int descriptor;
   ArrayList attribute;
   public static boolean doPreverify = false;
   public static final String nameInit = "<init>";
   public static final String nameClinit = "<clinit>";

   private MethodInfo(ConstPool var1) {
      this.constPool = var1;
      this.attribute = null;
   }

   public MethodInfo(ConstPool var1, String var2, String var3) {
      this(var1);
      this.accessFlags = 0;
      this.name = var1.addUtf8Info(var2);
      this.cachedName = var2;
      this.descriptor = this.constPool.addUtf8Info(var3);
   }

   MethodInfo(ConstPool var1, DataInputStream var2) throws IOException {
      this(var1);
      this.read(var2);
   }

   public MethodInfo(ConstPool var1, String var2, MethodInfo var3, Map var4) throws BadBytecode {
      this(var1);
      this.read(var3, var2, var4);
   }

   public String toString() {
      return this.getName() + " " + this.getDescriptor();
   }

   void compact(ConstPool var1) {
      this.name = var1.addUtf8Info(this.getName());
      this.descriptor = var1.addUtf8Info(this.getDescriptor());
      this.attribute = AttributeInfo.copyAll(this.attribute, var1);
      this.constPool = var1;
   }

   void prune(ConstPool var1) {
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

      AttributeInfo var5 = this.getAttribute("RuntimeInvisibleParameterAnnotations");
      if (var5 != null) {
         var5 = var5.copy(var1, (Map)null);
         var2.add(var5);
      }

      AttributeInfo var6 = this.getAttribute("RuntimeVisibleParameterAnnotations");
      if (var6 != null) {
         var6 = var6.copy(var1, (Map)null);
         var2.add(var6);
      }

      AnnotationDefaultAttribute var7 = (AnnotationDefaultAttribute)this.getAttribute("AnnotationDefault");
      if (var7 != null) {
         var2.add(var7);
      }

      ExceptionsAttribute var8 = this.getExceptionsAttribute();
      if (var8 != null) {
         var2.add(var8);
      }

      AttributeInfo var9 = this.getAttribute("Signature");
      if (var9 != null) {
         var9 = var9.copy(var1, (Map)null);
         var2.add(var9);
      }

      this.attribute = var2;
      this.name = var1.addUtf8Info(this.getName());
      this.descriptor = var1.addUtf8Info(this.getDescriptor());
      this.constPool = var1;
   }

   public String getName() {
      if (this.cachedName == null) {
         this.cachedName = this.constPool.getUtf8Info(this.name);
      }

      return this.cachedName;
   }

   public void setName(String var1) {
      this.name = this.constPool.addUtf8Info(var1);
      this.cachedName = var1;
   }

   public boolean isMethod() {
      String var1 = this.getName();
      return !var1.equals("<init>") && !var1.equals("<clinit>");
   }

   public ConstPool getConstPool() {
      return this.constPool;
   }

   public boolean isConstructor() {
      return this.getName().equals("<init>");
   }

   public boolean isStaticInitializer() {
      return this.getName().equals("<clinit>");
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   public void setAccessFlags(int var1) {
      this.accessFlags = var1;
   }

   public String getDescriptor() {
      return this.constPool.getUtf8Info(this.descriptor);
   }

   public void setDescriptor(String var1) {
      if (!var1.equals(this.getDescriptor())) {
         this.descriptor = this.constPool.addUtf8Info(var1);
      }

   }

   public List getAttributes() {
      if (this.attribute == null) {
         this.attribute = new ArrayList();
      }

      return this.attribute;
   }

   public AttributeInfo getAttribute(String var1) {
      return AttributeInfo.lookup(this.attribute, var1);
   }

   public void addAttribute(AttributeInfo var1) {
      if (this.attribute == null) {
         this.attribute = new ArrayList();
      }

      AttributeInfo.remove(this.attribute, var1.getName());
      this.attribute.add(var1);
   }

   public ExceptionsAttribute getExceptionsAttribute() {
      AttributeInfo var1 = AttributeInfo.lookup(this.attribute, "Exceptions");
      return (ExceptionsAttribute)var1;
   }

   public CodeAttribute getCodeAttribute() {
      AttributeInfo var1 = AttributeInfo.lookup(this.attribute, "Code");
      return (CodeAttribute)var1;
   }

   public void removeExceptionsAttribute() {
      AttributeInfo.remove(this.attribute, "Exceptions");
   }

   public void setExceptionsAttribute(ExceptionsAttribute var1) {
      this.removeExceptionsAttribute();
      if (this.attribute == null) {
         this.attribute = new ArrayList();
      }

      this.attribute.add(var1);
   }

   public void removeCodeAttribute() {
      AttributeInfo.remove(this.attribute, "Code");
   }

   public void setCodeAttribute(CodeAttribute var1) {
      this.removeCodeAttribute();
      if (this.attribute == null) {
         this.attribute = new ArrayList();
      }

      this.attribute.add(var1);
   }

   public void rebuildStackMapIf6(ClassPool var1, ClassFile var2) throws BadBytecode {
      if (var2.getMajorVersion() >= 50) {
         this.rebuildStackMap(var1);
      }

      if (doPreverify) {
         this.rebuildStackMapForME(var1);
      }

   }

   public void rebuildStackMap(ClassPool var1) throws BadBytecode {
      CodeAttribute var2 = this.getCodeAttribute();
      if (var2 != null) {
         StackMapTable var3 = MapMaker.make(var1, this);
         var2.setAttribute(var3);
      }

   }

   public void rebuildStackMapForME(ClassPool var1) throws BadBytecode {
      CodeAttribute var2 = this.getCodeAttribute();
      if (var2 != null) {
         StackMap var3 = MapMaker.make2(var1, this);
         var2.setAttribute(var3);
      }

   }

   public int getLineNumber(int var1) {
      CodeAttribute var2 = this.getCodeAttribute();
      if (var2 == null) {
         return -1;
      } else {
         LineNumberAttribute var3 = (LineNumberAttribute)var2.getAttribute("LineNumberTable");
         return var3 == null ? -1 : var3.toLineNumber(var1);
      }
   }

   public void setSuperclass(String var1) throws BadBytecode {
      if (this.isConstructor()) {
         CodeAttribute var2 = this.getCodeAttribute();
         byte[] var3 = var2.getCode();
         CodeIterator var4 = var2.iterator();
         int var5 = var4.skipSuperConstructor();
         if (var5 >= 0) {
            ConstPool var6 = this.constPool;
            int var7 = ByteArray.readU16bit(var3, var5 + 1);
            int var8 = var6.getMethodrefNameAndType(var7);
            int var9 = var6.addClassInfo(var1);
            int var10 = var6.addMethodrefInfo(var9, var8);
            ByteArray.write16bit(var10, var3, var5 + 1);
         }

      }
   }

   private void read(MethodInfo var1, String var2, Map var3) throws BadBytecode {
      ConstPool var4 = this.constPool;
      this.accessFlags = var1.accessFlags;
      this.name = var4.addUtf8Info(var2);
      this.cachedName = var2;
      ConstPool var5 = var1.constPool;
      String var6 = var5.getUtf8Info(var1.descriptor);
      String var7 = Descriptor.rename(var6, var3);
      this.descriptor = var4.addUtf8Info(var7);
      this.attribute = new ArrayList();
      ExceptionsAttribute var8 = var1.getExceptionsAttribute();
      if (var8 != null) {
         this.attribute.add(var8.copy(var4, var3));
      }

      CodeAttribute var9 = var1.getCodeAttribute();
      if (var9 != null) {
         this.attribute.add(var9.copy(var4, var3));
      }

   }

   private void read(DataInputStream var1) throws IOException {
      this.accessFlags = var1.readUnsignedShort();
      this.name = var1.readUnsignedShort();
      this.descriptor = var1.readUnsignedShort();
      int var2 = var1.readUnsignedShort();
      this.attribute = new ArrayList();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.attribute.add(AttributeInfo.read(this.constPool, var1));
      }

   }

   void write(DataOutputStream var1) throws IOException {
      var1.writeShort(this.accessFlags);
      var1.writeShort(this.name);
      var1.writeShort(this.descriptor);
      if (this.attribute == null) {
         var1.writeShort(0);
      } else {
         var1.writeShort(this.attribute.size());
         AttributeInfo.writeAll(this.attribute, var1);
      }

   }
}
