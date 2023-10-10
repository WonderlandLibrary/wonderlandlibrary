package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javassist.CtClass;

public final class ConstPool {
   LongVector items;
   int numOfItems;
   int thisClassInfo;
   HashMap itemsCache;
   public static final int CONST_Class = 7;
   public static final int CONST_Fieldref = 9;
   public static final int CONST_Methodref = 10;
   public static final int CONST_InterfaceMethodref = 11;
   public static final int CONST_String = 8;
   public static final int CONST_Integer = 3;
   public static final int CONST_Float = 4;
   public static final int CONST_Long = 5;
   public static final int CONST_Double = 6;
   public static final int CONST_NameAndType = 12;
   public static final int CONST_Utf8 = 1;
   public static final int CONST_MethodHandle = 15;
   public static final int CONST_MethodType = 16;
   public static final int CONST_InvokeDynamic = 18;
   public static final CtClass THIS = null;
   public static final int REF_getField = 1;
   public static final int REF_getStatic = 2;
   public static final int REF_putField = 3;
   public static final int REF_putStatic = 4;
   public static final int REF_invokeVirtual = 5;
   public static final int REF_invokeStatic = 6;
   public static final int REF_invokeSpecial = 7;
   public static final int REF_newInvokeSpecial = 8;
   public static final int REF_invokeInterface = 9;

   public ConstPool(String var1) {
      this.items = new LongVector();
      this.itemsCache = null;
      this.numOfItems = 0;
      this.addItem0((ConstInfo)null);
      this.thisClassInfo = this.addClassInfo(var1);
   }

   public ConstPool(DataInputStream var1) throws IOException {
      this.itemsCache = null;
      this.thisClassInfo = 0;
      this.read(var1);
   }

   void prune() {
      this.itemsCache = null;
   }

   public int getSize() {
      return this.numOfItems;
   }

   public String getClassName() {
      return this.getClassInfo(this.thisClassInfo);
   }

   public int getThisClassInfo() {
      return this.thisClassInfo;
   }

   void setThisClassInfo(int var1) {
      this.thisClassInfo = var1;
   }

   ConstInfo getItem(int var1) {
      return this.items.elementAt(var1);
   }

   public int getTag(int var1) {
      return this.getItem(var1).getTag();
   }

   public String getClassInfo(int var1) {
      ClassInfo var2 = (ClassInfo)this.getItem(var1);
      return var2 == null ? null : Descriptor.toJavaName(this.getUtf8Info(var2.name));
   }

   public String getClassInfoByDescriptor(int var1) {
      ClassInfo var2 = (ClassInfo)this.getItem(var1);
      if (var2 == null) {
         return null;
      } else {
         String var3 = this.getUtf8Info(var2.name);
         return var3.charAt(0) == '[' ? var3 : Descriptor.of(var3);
      }
   }

   public int getNameAndTypeName(int var1) {
      NameAndTypeInfo var2 = (NameAndTypeInfo)this.getItem(var1);
      return var2.memberName;
   }

   public int getNameAndTypeDescriptor(int var1) {
      NameAndTypeInfo var2 = (NameAndTypeInfo)this.getItem(var1);
      return var2.typeDescriptor;
   }

   public int getMemberClass(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      return var2.classIndex;
   }

   public int getMemberNameAndType(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      return var2.nameAndTypeIndex;
   }

   public int getFieldrefClass(int var1) {
      FieldrefInfo var2 = (FieldrefInfo)this.getItem(var1);
      return var2.classIndex;
   }

   public String getFieldrefClassName(int var1) {
      FieldrefInfo var2 = (FieldrefInfo)this.getItem(var1);
      return var2 == null ? null : this.getClassInfo(var2.classIndex);
   }

   public int getFieldrefNameAndType(int var1) {
      FieldrefInfo var2 = (FieldrefInfo)this.getItem(var1);
      return var2.nameAndTypeIndex;
   }

   public String getFieldrefName(int var1) {
      FieldrefInfo var2 = (FieldrefInfo)this.getItem(var1);
      if (var2 == null) {
         return null;
      } else {
         NameAndTypeInfo var3 = (NameAndTypeInfo)this.getItem(var2.nameAndTypeIndex);
         return var3 == null ? null : this.getUtf8Info(var3.memberName);
      }
   }

   public String getFieldrefType(int var1) {
      FieldrefInfo var2 = (FieldrefInfo)this.getItem(var1);
      if (var2 == null) {
         return null;
      } else {
         NameAndTypeInfo var3 = (NameAndTypeInfo)this.getItem(var2.nameAndTypeIndex);
         return var3 == null ? null : this.getUtf8Info(var3.typeDescriptor);
      }
   }

   public int getMethodrefClass(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      return var2.classIndex;
   }

   public String getMethodrefClassName(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      return var2 == null ? null : this.getClassInfo(var2.classIndex);
   }

   public int getMethodrefNameAndType(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      return var2.nameAndTypeIndex;
   }

   public String getMethodrefName(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      if (var2 == null) {
         return null;
      } else {
         NameAndTypeInfo var3 = (NameAndTypeInfo)this.getItem(var2.nameAndTypeIndex);
         return var3 == null ? null : this.getUtf8Info(var3.memberName);
      }
   }

   public String getMethodrefType(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      if (var2 == null) {
         return null;
      } else {
         NameAndTypeInfo var3 = (NameAndTypeInfo)this.getItem(var2.nameAndTypeIndex);
         return var3 == null ? null : this.getUtf8Info(var3.typeDescriptor);
      }
   }

   public int getInterfaceMethodrefClass(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      return var2.classIndex;
   }

   public String getInterfaceMethodrefClassName(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      return this.getClassInfo(var2.classIndex);
   }

   public int getInterfaceMethodrefNameAndType(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      return var2.nameAndTypeIndex;
   }

   public String getInterfaceMethodrefName(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      if (var2 == null) {
         return null;
      } else {
         NameAndTypeInfo var3 = (NameAndTypeInfo)this.getItem(var2.nameAndTypeIndex);
         return var3 == null ? null : this.getUtf8Info(var3.memberName);
      }
   }

   public String getInterfaceMethodrefType(int var1) {
      MemberrefInfo var2 = (MemberrefInfo)this.getItem(var1);
      if (var2 == null) {
         return null;
      } else {
         NameAndTypeInfo var3 = (NameAndTypeInfo)this.getItem(var2.nameAndTypeIndex);
         return var3 == null ? null : this.getUtf8Info(var3.typeDescriptor);
      }
   }

   public Object getLdcValue(int var1) {
      ConstInfo var2 = this.getItem(var1);
      Object var3 = null;
      if (var2 instanceof StringInfo) {
         var3 = this.getStringInfo(var1);
      } else if (var2 instanceof FloatInfo) {
         var3 = new Float(this.getFloatInfo(var1));
      } else if (var2 instanceof IntegerInfo) {
         var3 = new Integer(this.getIntegerInfo(var1));
      } else if (var2 instanceof LongInfo) {
         var3 = new Long(this.getLongInfo(var1));
      } else if (var2 instanceof DoubleInfo) {
         var3 = new Double(this.getDoubleInfo(var1));
      } else {
         var3 = null;
      }

      return var3;
   }

   public int getIntegerInfo(int var1) {
      IntegerInfo var2 = (IntegerInfo)this.getItem(var1);
      return var2.value;
   }

   public float getFloatInfo(int var1) {
      FloatInfo var2 = (FloatInfo)this.getItem(var1);
      return var2.value;
   }

   public long getLongInfo(int var1) {
      LongInfo var2 = (LongInfo)this.getItem(var1);
      return var2.value;
   }

   public double getDoubleInfo(int var1) {
      DoubleInfo var2 = (DoubleInfo)this.getItem(var1);
      return var2.value;
   }

   public String getStringInfo(int var1) {
      StringInfo var2 = (StringInfo)this.getItem(var1);
      return this.getUtf8Info(var2.string);
   }

   public String getUtf8Info(int var1) {
      Utf8Info var2 = (Utf8Info)this.getItem(var1);
      return var2.string;
   }

   public int getMethodHandleKind(int var1) {
      MethodHandleInfo var2 = (MethodHandleInfo)this.getItem(var1);
      return var2.refKind;
   }

   public int getMethodHandleIndex(int var1) {
      MethodHandleInfo var2 = (MethodHandleInfo)this.getItem(var1);
      return var2.refIndex;
   }

   public int getMethodTypeInfo(int var1) {
      MethodTypeInfo var2 = (MethodTypeInfo)this.getItem(var1);
      return var2.descriptor;
   }

   public int getInvokeDynamicBootstrap(int var1) {
      InvokeDynamicInfo var2 = (InvokeDynamicInfo)this.getItem(var1);
      return var2.bootstrap;
   }

   public int getInvokeDynamicNameAndType(int var1) {
      InvokeDynamicInfo var2 = (InvokeDynamicInfo)this.getItem(var1);
      return var2.nameAndType;
   }

   public String getInvokeDynamicType(int var1) {
      InvokeDynamicInfo var2 = (InvokeDynamicInfo)this.getItem(var1);
      if (var2 == null) {
         return null;
      } else {
         NameAndTypeInfo var3 = (NameAndTypeInfo)this.getItem(var2.nameAndType);
         return var3 == null ? null : this.getUtf8Info(var3.typeDescriptor);
      }
   }

   public int isConstructor(String var1, int var2) {
      return this.isMember(var1, "<init>", var2);
   }

   public int isMember(String var1, String var2, int var3) {
      MemberrefInfo var4 = (MemberrefInfo)this.getItem(var3);
      if (this.getClassInfo(var4.classIndex).equals(var1)) {
         NameAndTypeInfo var5 = (NameAndTypeInfo)this.getItem(var4.nameAndTypeIndex);
         if (this.getUtf8Info(var5.memberName).equals(var2)) {
            return var5.typeDescriptor;
         }
      }

      return 0;
   }

   public String eqMember(String var1, String var2, int var3) {
      MemberrefInfo var4 = (MemberrefInfo)this.getItem(var3);
      NameAndTypeInfo var5 = (NameAndTypeInfo)this.getItem(var4.nameAndTypeIndex);
      return this.getUtf8Info(var5.memberName).equals(var1) && this.getUtf8Info(var5.typeDescriptor).equals(var2) ? this.getClassInfo(var4.classIndex) : null;
   }

   private int addItem0(ConstInfo var1) {
      this.items.addElement(var1);
      return this.numOfItems++;
   }

   private int addItem(ConstInfo var1) {
      if (this.itemsCache == null) {
         this.itemsCache = makeItemsCache(this.items);
      }

      ConstInfo var2 = (ConstInfo)this.itemsCache.get(var1);
      if (var2 != null) {
         return var2.index;
      } else {
         this.items.addElement(var1);
         this.itemsCache.put(var1, var1);
         return this.numOfItems++;
      }
   }

   public int copy(int var1, ConstPool var2, Map var3) {
      if (var1 == 0) {
         return 0;
      } else {
         ConstInfo var4 = this.getItem(var1);
         return var4.copy(this, var2, var3);
      }
   }

   int addConstInfoPadding() {
      return this.addItem0(new ConstInfoPadding(this.numOfItems));
   }

   public int addClassInfo(CtClass var1) {
      if (var1 == THIS) {
         return this.thisClassInfo;
      } else {
         return !var1.isArray() ? this.addClassInfo(var1.getName()) : this.addClassInfo(Descriptor.toJvmName(var1));
      }
   }

   public int addClassInfo(String var1) {
      int var2 = this.addUtf8Info(Descriptor.toJvmName(var1));
      return this.addItem(new ClassInfo(var2, this.numOfItems));
   }

   public int addNameAndTypeInfo(String var1, String var2) {
      return this.addNameAndTypeInfo(this.addUtf8Info(var1), this.addUtf8Info(var2));
   }

   public int addNameAndTypeInfo(int var1, int var2) {
      return this.addItem(new NameAndTypeInfo(var1, var2, this.numOfItems));
   }

   public int addFieldrefInfo(int var1, String var2, String var3) {
      int var4 = this.addNameAndTypeInfo(var2, var3);
      return this.addFieldrefInfo(var1, var4);
   }

   public int addFieldrefInfo(int var1, int var2) {
      return this.addItem(new FieldrefInfo(var1, var2, this.numOfItems));
   }

   public int addMethodrefInfo(int var1, String var2, String var3) {
      int var4 = this.addNameAndTypeInfo(var2, var3);
      return this.addMethodrefInfo(var1, var4);
   }

   public int addMethodrefInfo(int var1, int var2) {
      return this.addItem(new MethodrefInfo(var1, var2, this.numOfItems));
   }

   public int addInterfaceMethodrefInfo(int var1, String var2, String var3) {
      int var4 = this.addNameAndTypeInfo(var2, var3);
      return this.addInterfaceMethodrefInfo(var1, var4);
   }

   public int addInterfaceMethodrefInfo(int var1, int var2) {
      return this.addItem(new InterfaceMethodrefInfo(var1, var2, this.numOfItems));
   }

   public int addStringInfo(String var1) {
      int var2 = this.addUtf8Info(var1);
      return this.addItem(new StringInfo(var2, this.numOfItems));
   }

   public int addIntegerInfo(int var1) {
      return this.addItem(new IntegerInfo(var1, this.numOfItems));
   }

   public int addFloatInfo(float var1) {
      return this.addItem(new FloatInfo(var1, this.numOfItems));
   }

   public int addLongInfo(long var1) {
      int var3 = this.addItem(new LongInfo(var1, this.numOfItems));
      if (var3 == this.numOfItems - 1) {
         this.addConstInfoPadding();
      }

      return var3;
   }

   public int addDoubleInfo(double var1) {
      int var3 = this.addItem(new DoubleInfo(var1, this.numOfItems));
      if (var3 == this.numOfItems - 1) {
         this.addConstInfoPadding();
      }

      return var3;
   }

   public int addUtf8Info(String var1) {
      return this.addItem(new Utf8Info(var1, this.numOfItems));
   }

   public int addMethodHandleInfo(int var1, int var2) {
      return this.addItem(new MethodHandleInfo(var1, var2, this.numOfItems));
   }

   public int addMethodTypeInfo(int var1) {
      return this.addItem(new MethodTypeInfo(var1, this.numOfItems));
   }

   public int addInvokeDynamicInfo(int var1, int var2) {
      return this.addItem(new InvokeDynamicInfo(var1, var2, this.numOfItems));
   }

   public Set getClassNames() {
      HashSet var1 = new HashSet();
      LongVector var2 = this.items;
      int var3 = this.numOfItems;

      for(int var4 = 1; var4 < var3; ++var4) {
         String var5 = var2.elementAt(var4).getClassName(this);
         if (var5 != null) {
            var1.add(var5);
         }
      }

      return var1;
   }

   public void renameClass(String var1, String var2) {
      LongVector var3 = this.items;
      int var4 = this.numOfItems;

      for(int var5 = 1; var5 < var4; ++var5) {
         ConstInfo var6 = var3.elementAt(var5);
         var6.renameClass(this, var1, var2, this.itemsCache);
      }

   }

   public void renameClass(Map var1) {
      LongVector var2 = this.items;
      int var3 = this.numOfItems;

      for(int var4 = 1; var4 < var3; ++var4) {
         ConstInfo var5 = var2.elementAt(var4);
         var5.renameClass(this, var1, this.itemsCache);
      }

   }

   private void read(DataInputStream var1) throws IOException {
      int var2 = var1.readUnsignedShort();
      this.items = new LongVector(var2);
      this.numOfItems = 0;
      this.addItem0((ConstInfo)null);

      while(true) {
         int var3;
         do {
            --var2;
            if (var2 <= 0) {
               return;
            }

            var3 = this.readOne(var1);
         } while(var3 != 5 && var3 != 6);

         this.addConstInfoPadding();
         --var2;
      }
   }

   private static HashMap makeItemsCache(LongVector var0) {
      HashMap var1 = new HashMap();
      int var2 = 1;

      while(true) {
         ConstInfo var3 = var0.elementAt(var2++);
         if (var3 == null) {
            return var1;
         }

         var1.put(var3, var3);
      }
   }

   private int readOne(DataInputStream var1) throws IOException {
      int var3 = var1.readUnsignedByte();
      Object var2;
      switch(var3) {
      case 1:
         var2 = new Utf8Info(var1, this.numOfItems);
         break;
      case 2:
      case 13:
      case 14:
      case 17:
      default:
         throw new IOException("invalid constant type: " + var3 + " at " + this.numOfItems);
      case 3:
         var2 = new IntegerInfo(var1, this.numOfItems);
         break;
      case 4:
         var2 = new FloatInfo(var1, this.numOfItems);
         break;
      case 5:
         var2 = new LongInfo(var1, this.numOfItems);
         break;
      case 6:
         var2 = new DoubleInfo(var1, this.numOfItems);
         break;
      case 7:
         var2 = new ClassInfo(var1, this.numOfItems);
         break;
      case 8:
         var2 = new StringInfo(var1, this.numOfItems);
         break;
      case 9:
         var2 = new FieldrefInfo(var1, this.numOfItems);
         break;
      case 10:
         var2 = new MethodrefInfo(var1, this.numOfItems);
         break;
      case 11:
         var2 = new InterfaceMethodrefInfo(var1, this.numOfItems);
         break;
      case 12:
         var2 = new NameAndTypeInfo(var1, this.numOfItems);
         break;
      case 15:
         var2 = new MethodHandleInfo(var1, this.numOfItems);
         break;
      case 16:
         var2 = new MethodTypeInfo(var1, this.numOfItems);
         break;
      case 18:
         var2 = new InvokeDynamicInfo(var1, this.numOfItems);
      }

      this.addItem0((ConstInfo)var2);
      return var3;
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeShort(this.numOfItems);
      LongVector var2 = this.items;
      int var3 = this.numOfItems;

      for(int var4 = 1; var4 < var3; ++var4) {
         var2.elementAt(var4).write(var1);
      }

   }

   public void print() {
      this.print(new PrintWriter(System.out, true));
   }

   public void print(PrintWriter var1) {
      int var2 = this.numOfItems;

      for(int var3 = 1; var3 < var2; ++var3) {
         var1.print(var3);
         var1.print(" ");
         this.items.elementAt(var3).print(var1);
      }

   }
}
