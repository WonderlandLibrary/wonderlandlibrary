package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeAttribute extends AttributeInfo implements Opcode {
   public static final String tag = "Code";
   private int maxStack;
   private int maxLocals;
   private ExceptionTable exceptions;
   private ArrayList attributes;

   public CodeAttribute(ConstPool var1, int var2, int var3, byte[] var4, ExceptionTable var5) {
      super(var1, "Code");
      this.maxStack = var2;
      this.maxLocals = var3;
      this.info = var4;
      this.exceptions = var5;
      this.attributes = new ArrayList();
   }

   private CodeAttribute(ConstPool var1, CodeAttribute var2, Map var3) throws BadBytecode {
      super(var1, "Code");
      this.maxStack = var2.getMaxStack();
      this.maxLocals = var2.getMaxLocals();
      this.exceptions = var2.getExceptionTable().copy(var1, var3);
      this.attributes = new ArrayList();
      List var4 = var2.getAttributes();
      int var5 = var4.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         AttributeInfo var7 = (AttributeInfo)var4.get(var6);
         this.attributes.add(var7.copy(var1, var3));
      }

      this.info = var2.copyCode(var1, var3, this.exceptions, this);
   }

   CodeAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, (byte[])null);
      int var4 = var3.readInt();
      this.maxStack = var3.readUnsignedShort();
      this.maxLocals = var3.readUnsignedShort();
      int var5 = var3.readInt();
      this.info = new byte[var5];
      var3.readFully(this.info);
      this.exceptions = new ExceptionTable(var1, var3);
      this.attributes = new ArrayList();
      int var6 = var3.readUnsignedShort();

      for(int var7 = 0; var7 < var6; ++var7) {
         this.attributes.add(AttributeInfo.read(var1, var3));
      }

   }

   public AttributeInfo copy(ConstPool var1, Map var2) throws CodeAttribute.RuntimeCopyException {
      try {
         return new CodeAttribute(var1, this, var2);
      } catch (BadBytecode var4) {
         throw new CodeAttribute.RuntimeCopyException("bad bytecode. fatal?");
      }
   }

   public int length() {
      return 18 + this.info.length + this.exceptions.size() * 8 + AttributeInfo.getLength(this.attributes);
   }

   void write(DataOutputStream var1) throws IOException {
      var1.writeShort(this.name);
      var1.writeInt(this.length() - 6);
      var1.writeShort(this.maxStack);
      var1.writeShort(this.maxLocals);
      var1.writeInt(this.info.length);
      var1.write(this.info);
      this.exceptions.write(var1);
      var1.writeShort(this.attributes.size());
      AttributeInfo.writeAll(this.attributes, var1);
   }

   public byte[] get() {
      throw new UnsupportedOperationException("CodeAttribute.get()");
   }

   public void set(byte[] var1) {
      throw new UnsupportedOperationException("CodeAttribute.set()");
   }

   void renameClass(String var1, String var2) {
      AttributeInfo.renameClass(this.attributes, var1, var2);
   }

   void renameClass(Map var1) {
      AttributeInfo.renameClass((List)this.attributes, (Map)var1);
   }

   void getRefClasses(Map var1) {
      AttributeInfo.getRefClasses(this.attributes, var1);
   }

   public String getDeclaringClass() {
      ConstPool var1 = this.getConstPool();
      return var1.getClassName();
   }

   public int getMaxStack() {
      return this.maxStack;
   }

   public void setMaxStack(int var1) {
      this.maxStack = var1;
   }

   public int computeMaxStack() throws BadBytecode {
      this.maxStack = (new CodeAnalyzer(this)).computeMaxStack();
      return this.maxStack;
   }

   public int getMaxLocals() {
      return this.maxLocals;
   }

   public void setMaxLocals(int var1) {
      this.maxLocals = var1;
   }

   public int getCodeLength() {
      return this.info.length;
   }

   public byte[] getCode() {
      return this.info;
   }

   void setCode(byte[] var1) {
      super.set(var1);
   }

   public CodeIterator iterator() {
      return new CodeIterator(this);
   }

   public ExceptionTable getExceptionTable() {
      return this.exceptions;
   }

   public List getAttributes() {
      return this.attributes;
   }

   public AttributeInfo getAttribute(String var1) {
      return AttributeInfo.lookup(this.attributes, var1);
   }

   public void setAttribute(StackMapTable var1) {
      AttributeInfo.remove(this.attributes, "StackMapTable");
      if (var1 != null) {
         this.attributes.add(var1);
      }

   }

   public void setAttribute(StackMap var1) {
      AttributeInfo.remove(this.attributes, "StackMap");
      if (var1 != null) {
         this.attributes.add(var1);
      }

   }

   private byte[] copyCode(ConstPool var1, Map var2, ExceptionTable var3, CodeAttribute var4) throws BadBytecode {
      int var5 = this.getCodeLength();
      byte[] var6 = new byte[var5];
      var4.info = var6;
      CodeAttribute.LdcEntry var7 = copyCode(this.info, 0, var5, this.getConstPool(), var6, var1, var2);
      return CodeAttribute.LdcEntry.doit(var6, var7, var3, var4);
   }

   private static CodeAttribute.LdcEntry copyCode(byte[] var0, int var1, int var2, ConstPool var3, byte[] var4, ConstPool var5, Map var6) throws BadBytecode {
      CodeAttribute.LdcEntry var9 = null;

      int var7;
      for(int var10 = var1; var10 < var2; var10 = var7) {
         var7 = CodeIterator.nextOpcode(var0, var10);
         int var11 = var0[var10];
         var4[var10] = (byte)var11;
         switch(var11 & 255) {
         case 18:
            int var8 = var0[var10 + 1] & 255;
            var8 = var3.copy(var8, var5, var6);
            if (var8 < 256) {
               var4[var10 + 1] = (byte)var8;
            } else {
               var4[var10] = 0;
               var4[var10 + 1] = 0;
               CodeAttribute.LdcEntry var12 = new CodeAttribute.LdcEntry();
               var12.where = var10;
               var12.index = var8;
               var12.next = var9;
               var9 = var12;
            }
            break;
         case 19:
         case 20:
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 187:
         case 189:
         case 192:
         case 193:
            copyConstPoolInfo(var10 + 1, var0, var3, var4, var5, var6);
            break;
         case 185:
            copyConstPoolInfo(var10 + 1, var0, var3, var4, var5, var6);
            var4[var10 + 3] = var0[var10 + 3];
            var4[var10 + 4] = var0[var10 + 4];
            break;
         case 186:
            copyConstPoolInfo(var10 + 1, var0, var3, var4, var5, var6);
            var4[var10 + 3] = 0;
            var4[var10 + 4] = 0;
            break;
         case 197:
            copyConstPoolInfo(var10 + 1, var0, var3, var4, var5, var6);
            var4[var10 + 3] = var0[var10 + 3];
            break;
         default:
            while(true) {
               ++var10;
               if (var10 >= var7) {
                  break;
               }

               var4[var10] = var0[var10];
            }
         }
      }

      return var9;
   }

   private static void copyConstPoolInfo(int var0, byte[] var1, ConstPool var2, byte[] var3, ConstPool var4, Map var5) {
      int var6 = (var1[var0] & 255) << 8 | var1[var0 + 1] & 255;
      var6 = var2.copy(var6, var4, var5);
      var3[var0] = (byte)(var6 >> 8);
      var3[var0 + 1] = (byte)var6;
   }

   public void insertLocalVar(int var1, int var2) throws BadBytecode {
      CodeIterator var3 = this.iterator();

      while(var3.hasNext()) {
         shiftIndex(var3, var1, var2);
      }

      this.setMaxLocals(this.getMaxLocals() + var2);
   }

   private static void shiftIndex(CodeIterator var0, int var1, int var2) throws BadBytecode {
      int var3 = var0.next();
      int var4 = var0.byteAt(var3);
      if (var4 >= 21) {
         if (var4 < 79) {
            if (var4 < 26) {
               shiftIndex8(var0, var3, var4, var1, var2);
            } else if (var4 < 46) {
               shiftIndex0(var0, var3, var4, var1, var2, 26, 21);
            } else {
               if (var4 < 54) {
                  return;
               }

               if (var4 < 59) {
                  shiftIndex8(var0, var3, var4, var1, var2);
               } else {
                  shiftIndex0(var0, var3, var4, var1, var2, 59, 54);
               }
            }
         } else {
            int var5;
            if (var4 == 132) {
               var5 = var0.byteAt(var3 + 1);
               if (var5 < var1) {
                  return;
               }

               var5 += var2;
               if (var5 < 256) {
                  var0.writeByte(var5, var3 + 1);
               } else {
                  byte var6 = (byte)var0.byteAt(var3 + 2);
                  int var7 = var0.insertExGap(3);
                  var0.writeByte(196, var7 - 3);
                  var0.writeByte(132, var7 - 2);
                  var0.write16bit(var5, var7 - 1);
                  var0.write16bit(var6, var7 + 1);
               }
            } else if (var4 == 169) {
               shiftIndex8(var0, var3, var4, var1, var2);
            } else if (var4 == 196) {
               var5 = var0.u16bitAt(var3 + 2);
               if (var5 < var1) {
                  return;
               }

               var5 += var2;
               var0.write16bit(var5, var3 + 2);
            }
         }

      }
   }

   private static void shiftIndex8(CodeIterator var0, int var1, int var2, int var3, int var4) throws BadBytecode {
      int var5 = var0.byteAt(var1 + 1);
      if (var5 >= var3) {
         var5 += var4;
         if (var5 < 256) {
            var0.writeByte(var5, var1 + 1);
         } else {
            int var6 = var0.insertExGap(2);
            var0.writeByte(196, var6 - 2);
            var0.writeByte(var2, var6 - 1);
            var0.write16bit(var5, var6);
         }

      }
   }

   private static void shiftIndex0(CodeIterator var0, int var1, int var2, int var3, int var4, int var5, int var6) throws BadBytecode {
      int var7 = (var2 - var5) % 4;
      if (var7 >= var3) {
         var7 += var4;
         if (var7 < 4) {
            var0.writeByte(var2 + var4, var1);
         } else {
            var2 = (var2 - var5) / 4 + var6;
            int var8;
            if (var7 < 256) {
               var8 = var0.insertExGap(1);
               var0.writeByte(var2, var8 - 1);
               var0.writeByte(var7, var8);
            } else {
               var8 = var0.insertExGap(3);
               var0.writeByte(196, var8 - 1);
               var0.writeByte(var2, var8);
               var0.write16bit(var7, var8 + 1);
            }
         }

      }
   }

   static class LdcEntry {
      CodeAttribute.LdcEntry next;
      int where;
      int index;

      static byte[] doit(byte[] var0, CodeAttribute.LdcEntry var1, ExceptionTable var2, CodeAttribute var3) throws BadBytecode {
         if (var1 != null) {
            var0 = CodeIterator.changeLdcToLdcW(var0, var2, var3, var1);
         }

         return var0;
      }
   }

   public static class RuntimeCopyException extends RuntimeException {
      public RuntimeCopyException(String var1) {
         super(var1);
      }
   }
}
