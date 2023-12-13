package javassist.bytecode;

import javassist.CtClass;
import javassist.CtPrimitiveType;

public class Bytecode extends ByteVector implements Cloneable, Opcode {
   public static final CtClass THIS;
   ConstPool constPool;
   int maxStack;
   int maxLocals;
   ExceptionTable tryblocks;
   private int stackDepth;

   public Bytecode(ConstPool var1, int var2, int var3) {
      this.constPool = var1;
      this.maxStack = var2;
      this.maxLocals = var3;
      this.tryblocks = new ExceptionTable(var1);
      this.stackDepth = 0;
   }

   public Bytecode(ConstPool var1) {
      this(var1, 0, 0);
   }

   public Object clone() {
      try {
         Bytecode var1 = (Bytecode)super.clone();
         var1.tryblocks = (ExceptionTable)this.tryblocks.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public ConstPool getConstPool() {
      return this.constPool;
   }

   public ExceptionTable getExceptionTable() {
      return this.tryblocks;
   }

   public CodeAttribute toCodeAttribute() {
      return new CodeAttribute(this.constPool, this.maxStack, this.maxLocals, this.get(), this.tryblocks);
   }

   public int length() {
      return this.getSize();
   }

   public byte[] get() {
      return this.copy();
   }

   public int getMaxStack() {
      return this.maxStack;
   }

   public void setMaxStack(int var1) {
      this.maxStack = var1;
   }

   public int getMaxLocals() {
      return this.maxLocals;
   }

   public void setMaxLocals(int var1) {
      this.maxLocals = var1;
   }

   public void setMaxLocals(boolean var1, CtClass[] var2, int var3) {
      if (!var1) {
         ++var3;
      }

      if (var2 != null) {
         CtClass var4 = CtClass.doubleType;
         CtClass var5 = CtClass.longType;
         int var6 = var2.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            CtClass var8 = var2[var7];
            if (var8 != var4 && var8 != var5) {
               ++var3;
            } else {
               var3 += 2;
            }
         }
      }

      this.maxLocals = var3;
   }

   public void incMaxLocals(int var1) {
      this.maxLocals += var1;
   }

   public void addExceptionHandler(int var1, int var2, int var3, CtClass var4) {
      this.addExceptionHandler(var1, var2, var3, this.constPool.addClassInfo(var4));
   }

   public void addExceptionHandler(int var1, int var2, int var3, String var4) {
      this.addExceptionHandler(var1, var2, var3, this.constPool.addClassInfo(var4));
   }

   public void addExceptionHandler(int var1, int var2, int var3, int var4) {
      this.tryblocks.add(var1, var2, var3, var4);
   }

   public int currentPc() {
      return this.getSize();
   }

   public int read(int var1) {
      return super.read(var1);
   }

   public int read16bit(int var1) {
      int var2 = this.read(var1);
      int var3 = this.read(var1 + 1);
      return (var2 << 8) + (var3 & 255);
   }

   public int read32bit(int var1) {
      int var2 = this.read16bit(var1);
      int var3 = this.read16bit(var1 + 2);
      return (var2 << 16) + (var3 & '\uffff');
   }

   public void write(int var1, int var2) {
      super.write(var1, var2);
   }

   public void write16bit(int var1, int var2) {
      this.write(var1, var2 >> 8);
      this.write(var1 + 1, var2);
   }

   public void write32bit(int var1, int var2) {
      this.write16bit(var1, var2 >> 16);
      this.write16bit(var1 + 2, var2);
   }

   public void add(int var1) {
      super.add(var1);
   }

   public void add32bit(int var1) {
      this.add(var1 >> 24, var1 >> 16, var1 >> 8, var1);
   }

   public void addGap(int var1) {
      super.addGap(var1);
   }

   public void addOpcode(int var1) {
      this.add(var1);
      this.growStack(STACK_GROW[var1]);
   }

   public void growStack(int var1) {
      this.setStackDepth(this.stackDepth + var1);
   }

   public int getStackDepth() {
      return this.stackDepth;
   }

   public void setStackDepth(int var1) {
      this.stackDepth = var1;
      if (this.stackDepth > this.maxStack) {
         this.maxStack = this.stackDepth;
      }

   }

   public void addIndex(int var1) {
      this.add(var1 >> 8, var1);
   }

   public void addAload(int var1) {
      if (var1 < 4) {
         this.addOpcode(42 + var1);
      } else if (var1 < 256) {
         this.addOpcode(25);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(25);
         this.addIndex(var1);
      }

   }

   public void addAstore(int var1) {
      if (var1 < 4) {
         this.addOpcode(75 + var1);
      } else if (var1 < 256) {
         this.addOpcode(58);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(58);
         this.addIndex(var1);
      }

   }

   public void addIconst(int var1) {
      if (var1 < 6 && -2 < var1) {
         this.addOpcode(3 + var1);
      } else if (var1 <= 127 && -128 <= var1) {
         this.addOpcode(16);
         this.add(var1);
      } else if (var1 <= 32767 && -32768 <= var1) {
         this.addOpcode(17);
         this.add(var1 >> 8);
         this.add(var1);
      } else {
         this.addLdc(this.constPool.addIntegerInfo(var1));
      }

   }

   public void addConstZero(CtClass var1) {
      if (var1.isPrimitive()) {
         if (var1 == CtClass.longType) {
            this.addOpcode(9);
         } else if (var1 == CtClass.floatType) {
            this.addOpcode(11);
         } else if (var1 == CtClass.doubleType) {
            this.addOpcode(14);
         } else {
            if (var1 == CtClass.voidType) {
               throw new RuntimeException("void type?");
            }

            this.addOpcode(3);
         }
      } else {
         this.addOpcode(1);
      }

   }

   public void addIload(int var1) {
      if (var1 < 4) {
         this.addOpcode(26 + var1);
      } else if (var1 < 256) {
         this.addOpcode(21);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(21);
         this.addIndex(var1);
      }

   }

   public void addIstore(int var1) {
      if (var1 < 4) {
         this.addOpcode(59 + var1);
      } else if (var1 < 256) {
         this.addOpcode(54);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(54);
         this.addIndex(var1);
      }

   }

   public void addLconst(long var1) {
      if (var1 != 0L && var1 != 1L) {
         this.addLdc2w(var1);
      } else {
         this.addOpcode(9 + (int)var1);
      }

   }

   public void addLload(int var1) {
      if (var1 < 4) {
         this.addOpcode(30 + var1);
      } else if (var1 < 256) {
         this.addOpcode(22);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(22);
         this.addIndex(var1);
      }

   }

   public void addLstore(int var1) {
      if (var1 < 4) {
         this.addOpcode(63 + var1);
      } else if (var1 < 256) {
         this.addOpcode(55);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(55);
         this.addIndex(var1);
      }

   }

   public void addDconst(double var1) {
      if (var1 != 0.0D && var1 != 1.0D) {
         this.addLdc2w(var1);
      } else {
         this.addOpcode(14 + (int)var1);
      }

   }

   public void addDload(int var1) {
      if (var1 < 4) {
         this.addOpcode(38 + var1);
      } else if (var1 < 256) {
         this.addOpcode(24);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(24);
         this.addIndex(var1);
      }

   }

   public void addDstore(int var1) {
      if (var1 < 4) {
         this.addOpcode(71 + var1);
      } else if (var1 < 256) {
         this.addOpcode(57);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(57);
         this.addIndex(var1);
      }

   }

   public void addFconst(float var1) {
      if (var1 != 0.0F && var1 != 1.0F && var1 != 2.0F) {
         this.addLdc(this.constPool.addFloatInfo(var1));
      } else {
         this.addOpcode(11 + (int)var1);
      }

   }

   public void addFload(int var1) {
      if (var1 < 4) {
         this.addOpcode(34 + var1);
      } else if (var1 < 256) {
         this.addOpcode(23);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(23);
         this.addIndex(var1);
      }

   }

   public void addFstore(int var1) {
      if (var1 < 4) {
         this.addOpcode(67 + var1);
      } else if (var1 < 256) {
         this.addOpcode(56);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(56);
         this.addIndex(var1);
      }

   }

   public int addLoad(int var1, CtClass var2) {
      if (var2.isPrimitive()) {
         if (var2 != CtClass.booleanType && var2 != CtClass.charType && var2 != CtClass.byteType && var2 != CtClass.shortType && var2 != CtClass.intType) {
            if (var2 == CtClass.longType) {
               this.addLload(var1);
               return 2;
            }

            if (var2 != CtClass.floatType) {
               if (var2 == CtClass.doubleType) {
                  this.addDload(var1);
                  return 2;
               }

               throw new RuntimeException("void type?");
            }

            this.addFload(var1);
         } else {
            this.addIload(var1);
         }
      } else {
         this.addAload(var1);
      }

      return 1;
   }

   public int addStore(int var1, CtClass var2) {
      if (var2.isPrimitive()) {
         if (var2 != CtClass.booleanType && var2 != CtClass.charType && var2 != CtClass.byteType && var2 != CtClass.shortType && var2 != CtClass.intType) {
            if (var2 == CtClass.longType) {
               this.addLstore(var1);
               return 2;
            }

            if (var2 != CtClass.floatType) {
               if (var2 == CtClass.doubleType) {
                  this.addDstore(var1);
                  return 2;
               }

               throw new RuntimeException("void type?");
            }

            this.addFstore(var1);
         } else {
            this.addIstore(var1);
         }
      } else {
         this.addAstore(var1);
      }

      return 1;
   }

   public int addLoadParameters(CtClass[] var1, int var2) {
      int var3 = 0;
      if (var1 != null) {
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            var3 += this.addLoad(var3 + var2, var1[var5]);
         }
      }

      return var3;
   }

   public void addCheckcast(CtClass var1) {
      this.addOpcode(192);
      this.addIndex(this.constPool.addClassInfo(var1));
   }

   public void addCheckcast(String var1) {
      this.addOpcode(192);
      this.addIndex(this.constPool.addClassInfo(var1));
   }

   public void addInstanceof(String var1) {
      this.addOpcode(193);
      this.addIndex(this.constPool.addClassInfo(var1));
   }

   public void addGetfield(CtClass var1, String var2, String var3) {
      this.add(180);
      int var4 = this.constPool.addClassInfo(var1);
      this.addIndex(this.constPool.addFieldrefInfo(var4, var2, var3));
      this.growStack(Descriptor.dataSize(var3) - 1);
   }

   public void addGetfield(String var1, String var2, String var3) {
      this.add(180);
      int var4 = this.constPool.addClassInfo(var1);
      this.addIndex(this.constPool.addFieldrefInfo(var4, var2, var3));
      this.growStack(Descriptor.dataSize(var3) - 1);
   }

   public void addGetstatic(CtClass var1, String var2, String var3) {
      this.add(178);
      int var4 = this.constPool.addClassInfo(var1);
      this.addIndex(this.constPool.addFieldrefInfo(var4, var2, var3));
      this.growStack(Descriptor.dataSize(var3));
   }

   public void addGetstatic(String var1, String var2, String var3) {
      this.add(178);
      int var4 = this.constPool.addClassInfo(var1);
      this.addIndex(this.constPool.addFieldrefInfo(var4, var2, var3));
      this.growStack(Descriptor.dataSize(var3));
   }

   public void addInvokespecial(CtClass var1, String var2, CtClass var3, CtClass[] var4) {
      String var5 = Descriptor.ofMethod(var3, var4);
      this.addInvokespecial(var1, var2, var5);
   }

   public void addInvokespecial(CtClass var1, String var2, String var3) {
      boolean var4 = var1 == null ? false : var1.isInterface();
      this.addInvokespecial(var4, this.constPool.addClassInfo(var1), var2, var3);
   }

   public void addInvokespecial(String var1, String var2, String var3) {
      this.addInvokespecial(false, this.constPool.addClassInfo(var1), var2, var3);
   }

   public void addInvokespecial(int var1, String var2, String var3) {
      this.addInvokespecial(false, var1, var2, var3);
   }

   public void addInvokespecial(boolean var1, int var2, String var3, String var4) {
      this.add(183);
      int var5;
      if (var1) {
         var5 = this.constPool.addInterfaceMethodrefInfo(var2, var3, var4);
      } else {
         var5 = this.constPool.addMethodrefInfo(var2, var3, var4);
      }

      this.addIndex(var5);
      this.growStack(Descriptor.dataSize(var4) - 1);
   }

   public void addInvokestatic(CtClass var1, String var2, CtClass var3, CtClass[] var4) {
      String var5 = Descriptor.ofMethod(var3, var4);
      this.addInvokestatic(var1, var2, var5);
   }

   public void addInvokestatic(CtClass var1, String var2, String var3) {
      this.addInvokestatic(this.constPool.addClassInfo(var1), var2, var3);
   }

   public void addInvokestatic(String var1, String var2, String var3) {
      this.addInvokestatic(this.constPool.addClassInfo(var1), var2, var3);
   }

   public void addInvokestatic(int var1, String var2, String var3) {
      this.add(184);
      this.addIndex(this.constPool.addMethodrefInfo(var1, var2, var3));
      this.growStack(Descriptor.dataSize(var3));
   }

   public void addInvokevirtual(CtClass var1, String var2, CtClass var3, CtClass[] var4) {
      String var5 = Descriptor.ofMethod(var3, var4);
      this.addInvokevirtual(var1, var2, var5);
   }

   public void addInvokevirtual(CtClass var1, String var2, String var3) {
      this.addInvokevirtual(this.constPool.addClassInfo(var1), var2, var3);
   }

   public void addInvokevirtual(String var1, String var2, String var3) {
      this.addInvokevirtual(this.constPool.addClassInfo(var1), var2, var3);
   }

   public void addInvokevirtual(int var1, String var2, String var3) {
      this.add(182);
      this.addIndex(this.constPool.addMethodrefInfo(var1, var2, var3));
      this.growStack(Descriptor.dataSize(var3) - 1);
   }

   public void addInvokeinterface(CtClass var1, String var2, CtClass var3, CtClass[] var4, int var5) {
      String var6 = Descriptor.ofMethod(var3, var4);
      this.addInvokeinterface(var1, var2, var6, var5);
   }

   public void addInvokeinterface(CtClass var1, String var2, String var3, int var4) {
      this.addInvokeinterface(this.constPool.addClassInfo(var1), var2, var3, var4);
   }

   public void addInvokeinterface(String var1, String var2, String var3, int var4) {
      this.addInvokeinterface(this.constPool.addClassInfo(var1), var2, var3, var4);
   }

   public void addInvokeinterface(int var1, String var2, String var3, int var4) {
      this.add(185);
      this.addIndex(this.constPool.addInterfaceMethodrefInfo(var1, var2, var3));
      this.add(var4);
      this.add(0);
      this.growStack(Descriptor.dataSize(var3) - 1);
   }

   public void addInvokedynamic(int var1, String var2, String var3) {
      int var4 = this.constPool.addNameAndTypeInfo(var2, var3);
      int var5 = this.constPool.addInvokeDynamicInfo(var1, var4);
      this.add(186);
      this.addIndex(var5);
      this.add(0, 0);
      this.growStack(Descriptor.dataSize(var3));
   }

   public void addLdc(String var1) {
      this.addLdc(this.constPool.addStringInfo(var1));
   }

   public void addLdc(int var1) {
      if (var1 > 255) {
         this.addOpcode(19);
         this.addIndex(var1);
      } else {
         this.addOpcode(18);
         this.add(var1);
      }

   }

   public void addLdc2w(long var1) {
      this.addOpcode(20);
      this.addIndex(this.constPool.addLongInfo(var1));
   }

   public void addLdc2w(double var1) {
      this.addOpcode(20);
      this.addIndex(this.constPool.addDoubleInfo(var1));
   }

   public void addNew(CtClass var1) {
      this.addOpcode(187);
      this.addIndex(this.constPool.addClassInfo(var1));
   }

   public void addNew(String var1) {
      this.addOpcode(187);
      this.addIndex(this.constPool.addClassInfo(var1));
   }

   public void addAnewarray(String var1) {
      this.addOpcode(189);
      this.addIndex(this.constPool.addClassInfo(var1));
   }

   public void addAnewarray(CtClass var1, int var2) {
      this.addIconst(var2);
      this.addOpcode(189);
      this.addIndex(this.constPool.addClassInfo(var1));
   }

   public void addNewarray(int var1, int var2) {
      this.addIconst(var2);
      this.addOpcode(188);
      this.add(var1);
   }

   public int addMultiNewarray(CtClass var1, int[] var2) {
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         this.addIconst(var2[var4]);
      }

      this.growStack(var3);
      return this.addMultiNewarray(var1, var3);
   }

   public int addMultiNewarray(CtClass var1, int var2) {
      this.add(197);
      this.addIndex(this.constPool.addClassInfo(var1));
      this.add(var2);
      this.growStack(1 - var2);
      return var2;
   }

   public int addMultiNewarray(String var1, int var2) {
      this.add(197);
      this.addIndex(this.constPool.addClassInfo(var1));
      this.add(var2);
      this.growStack(1 - var2);
      return var2;
   }

   public void addPutfield(CtClass var1, String var2, String var3) {
      this.addPutfield0(var1, (String)null, var2, var3);
   }

   public void addPutfield(String var1, String var2, String var3) {
      this.addPutfield0((CtClass)null, var1, var2, var3);
   }

   private void addPutfield0(CtClass var1, String var2, String var3, String var4) {
      this.add(181);
      int var5 = var2 == null ? this.constPool.addClassInfo(var1) : this.constPool.addClassInfo(var2);
      this.addIndex(this.constPool.addFieldrefInfo(var5, var3, var4));
      this.growStack(-1 - Descriptor.dataSize(var4));
   }

   public void addPutstatic(CtClass var1, String var2, String var3) {
      this.addPutstatic0(var1, (String)null, var2, var3);
   }

   public void addPutstatic(String var1, String var2, String var3) {
      this.addPutstatic0((CtClass)null, var1, var2, var3);
   }

   private void addPutstatic0(CtClass var1, String var2, String var3, String var4) {
      this.add(179);
      int var5 = var2 == null ? this.constPool.addClassInfo(var1) : this.constPool.addClassInfo(var2);
      this.addIndex(this.constPool.addFieldrefInfo(var5, var3, var4));
      this.growStack(-Descriptor.dataSize(var4));
   }

   public void addReturn(CtClass var1) {
      if (var1 == null) {
         this.addOpcode(177);
      } else if (var1.isPrimitive()) {
         CtPrimitiveType var2 = (CtPrimitiveType)var1;
         this.addOpcode(var2.getReturnOp());
      } else {
         this.addOpcode(176);
      }

   }

   public void addRet(int var1) {
      if (var1 < 256) {
         this.addOpcode(169);
         this.add(var1);
      } else {
         this.addOpcode(196);
         this.addOpcode(169);
         this.addIndex(var1);
      }

   }

   public void addPrintln(String var1) {
      this.addGetstatic("java.lang.System", "err", "Ljava/io/PrintStream;");
      this.addLdc(var1);
      this.addInvokevirtual("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
   }

   public void add(int var1, int var2, int var3, int var4) {
      super.add(var1, var2, var3, var4);
   }

   public void add(int var1, int var2) {
      super.add(var1, var2);
   }

   static {
      THIS = ConstPool.THIS;
   }
}
