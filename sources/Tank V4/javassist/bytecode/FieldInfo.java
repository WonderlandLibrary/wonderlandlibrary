package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FieldInfo {
   ConstPool constPool;
   int accessFlags;
   int name;
   String cachedName;
   String cachedType;
   int descriptor;
   ArrayList attribute;

   private FieldInfo(ConstPool var1) {
      this.constPool = var1;
      this.accessFlags = 0;
      this.attribute = null;
   }

   public FieldInfo(ConstPool var1, String var2, String var3) {
      this(var1);
      this.name = var1.addUtf8Info(var2);
      this.cachedName = var2;
      this.descriptor = var1.addUtf8Info(var3);
   }

   FieldInfo(ConstPool var1, DataInputStream var2) throws IOException {
      this(var1);
      this.read(var2);
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

      AttributeInfo var5 = this.getAttribute("Signature");
      if (var5 != null) {
         var5 = var5.copy(var1, (Map)null);
         var2.add(var5);
      }

      int var6 = this.getConstantValue();
      if (var6 != 0) {
         var6 = this.constPool.copy(var6, var1, (Map)null);
         var2.add(new ConstantAttribute(var1, var6));
      }

      this.attribute = var2;
      this.name = var1.addUtf8Info(this.getName());
      this.descriptor = var1.addUtf8Info(this.getDescriptor());
      this.constPool = var1;
   }

   public ConstPool getConstPool() {
      return this.constPool;
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

   public int getConstantValue() {
      if ((this.accessFlags & 8) == 0) {
         return 0;
      } else {
         ConstantAttribute var1 = (ConstantAttribute)this.getAttribute("ConstantValue");
         return var1 == null ? 0 : var1.getConstantValue();
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
