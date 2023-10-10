package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class AttributeInfo {
   protected ConstPool constPool;
   int name;
   byte[] info;

   protected AttributeInfo(ConstPool var1, int var2, byte[] var3) {
      this.constPool = var1;
      this.name = var2;
      this.info = var3;
   }

   protected AttributeInfo(ConstPool var1, String var2) {
      this(var1, var2, (byte[])null);
   }

   public AttributeInfo(ConstPool var1, String var2, byte[] var3) {
      this(var1, var1.addUtf8Info(var2), var3);
   }

   protected AttributeInfo(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      this.constPool = var1;
      this.name = var2;
      int var4 = var3.readInt();
      this.info = new byte[var4];
      if (var4 > 0) {
         var3.readFully(this.info);
      }

   }

   static AttributeInfo read(ConstPool var0, DataInputStream var1) throws IOException {
      int var2 = var1.readUnsignedShort();
      String var3 = var0.getUtf8Info(var2);
      char var4 = var3.charAt(0);
      if (var4 < 'M') {
         if (var4 < 'E') {
            if (var3.equals("AnnotationDefault")) {
               return new AnnotationDefaultAttribute(var0, var2, var1);
            }

            if (var3.equals("BootstrapMethods")) {
               return new BootstrapMethodsAttribute(var0, var2, var1);
            }

            if (var3.equals("Code")) {
               return new CodeAttribute(var0, var2, var1);
            }

            if (var3.equals("ConstantValue")) {
               return new ConstantAttribute(var0, var2, var1);
            }

            if (var3.equals("Deprecated")) {
               return new DeprecatedAttribute(var0, var2, var1);
            }
         } else {
            if (var3.equals("EnclosingMethod")) {
               return new EnclosingMethodAttribute(var0, var2, var1);
            }

            if (var3.equals("Exceptions")) {
               return new ExceptionsAttribute(var0, var2, var1);
            }

            if (var3.equals("InnerClasses")) {
               return new InnerClassesAttribute(var0, var2, var1);
            }

            if (var3.equals("LineNumberTable")) {
               return new LineNumberAttribute(var0, var2, var1);
            }

            if (var3.equals("LocalVariableTable")) {
               return new LocalVariableAttribute(var0, var2, var1);
            }

            if (var3.equals("LocalVariableTypeTable")) {
               return new LocalVariableTypeAttribute(var0, var2, var1);
            }
         }
      } else if (var4 < 'S') {
         if (var3.equals("MethodParameters")) {
            return new MethodParametersAttribute(var0, var2, var1);
         }

         if (var3.equals("RuntimeVisibleAnnotations") || var3.equals("RuntimeInvisibleAnnotations")) {
            return new AnnotationsAttribute(var0, var2, var1);
         }

         if (var3.equals("RuntimeVisibleParameterAnnotations") || var3.equals("RuntimeInvisibleParameterAnnotations")) {
            return new ParameterAnnotationsAttribute(var0, var2, var1);
         }

         if (var3.equals("RuntimeVisibleTypeAnnotations") || var3.equals("RuntimeInvisibleTypeAnnotations")) {
            return new TypeAnnotationsAttribute(var0, var2, var1);
         }
      } else {
         if (var3.equals("Signature")) {
            return new SignatureAttribute(var0, var2, var1);
         }

         if (var3.equals("SourceFile")) {
            return new SourceFileAttribute(var0, var2, var1);
         }

         if (var3.equals("Synthetic")) {
            return new SyntheticAttribute(var0, var2, var1);
         }

         if (var3.equals("StackMap")) {
            return new StackMap(var0, var2, var1);
         }

         if (var3.equals("StackMapTable")) {
            return new StackMapTable(var0, var2, var1);
         }
      }

      return new AttributeInfo(var0, var2, var1);
   }

   public String getName() {
      return this.constPool.getUtf8Info(this.name);
   }

   public ConstPool getConstPool() {
      return this.constPool;
   }

   public int length() {
      return this.info.length + 6;
   }

   public byte[] get() {
      return this.info;
   }

   public void set(byte[] var1) {
      this.info = var1;
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      int var3 = this.info.length;
      byte[] var4 = this.info;
      byte[] var5 = new byte[var3];

      for(int var6 = 0; var6 < var3; ++var6) {
         var5[var6] = var4[var6];
      }

      return new AttributeInfo(var1, this.getName(), var5);
   }

   void write(DataOutputStream var1) throws IOException {
      var1.writeShort(this.name);
      var1.writeInt(this.info.length);
      if (this.info.length > 0) {
         var1.write(this.info);
      }

   }

   static int getLength(ArrayList var0) {
      int var1 = 0;
      int var2 = var0.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         AttributeInfo var4 = (AttributeInfo)var0.get(var3);
         var1 += var4.length();
      }

      return var1;
   }

   static AttributeInfo lookup(ArrayList var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         ListIterator var2 = var0.listIterator();

         AttributeInfo var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (AttributeInfo)var2.next();
         } while(!var3.getName().equals(var1));

         return var3;
      }
   }

   static synchronized void remove(ArrayList var0, String var1) {
      if (var0 != null) {
         ListIterator var2 = var0.listIterator();

         while(var2.hasNext()) {
            AttributeInfo var3 = (AttributeInfo)var2.next();
            if (var3.getName().equals(var1)) {
               var2.remove();
            }
         }

      }
   }

   static void writeAll(ArrayList var0, DataOutputStream var1) throws IOException {
      if (var0 != null) {
         int var2 = var0.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            AttributeInfo var4 = (AttributeInfo)var0.get(var3);
            var4.write(var1);
         }

      }
   }

   static ArrayList copyAll(ArrayList var0, ConstPool var1) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         int var3 = var0.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            AttributeInfo var5 = (AttributeInfo)var0.get(var4);
            var2.add(var5.copy(var1, (Map)null));
         }

         return var2;
      }
   }

   void renameClass(String var1, String var2) {
   }

   void renameClass(Map var1) {
   }

   static void renameClass(List var0, String var1, String var2) {
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         AttributeInfo var4 = (AttributeInfo)var3.next();
         var4.renameClass(var1, var2);
      }

   }

   static void renameClass(List var0, Map var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         AttributeInfo var3 = (AttributeInfo)var2.next();
         var3.renameClass(var1);
      }

   }

   void getRefClasses(Map var1) {
   }

   static void getRefClasses(List var0, Map var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         AttributeInfo var3 = (AttributeInfo)var2.next();
         var3.getRefClasses(var1);
      }

   }
}
