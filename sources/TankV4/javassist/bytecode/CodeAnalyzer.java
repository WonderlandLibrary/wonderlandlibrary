package javassist.bytecode;

class CodeAnalyzer implements Opcode {
   private ConstPool constPool;
   private CodeAttribute codeAttr;

   public CodeAnalyzer(CodeAttribute var1) {
      this.codeAttr = var1;
      this.constPool = var1.getConstPool();
   }

   public int computeMaxStack() throws BadBytecode {
      CodeIterator var1 = this.codeAttr.iterator();
      int var2 = var1.getCodeLength();
      int[] var3 = new int[var2];
      this.constPool = this.codeAttr.getConstPool();
      this.initStack(var3, this.codeAttr);

      boolean var4;
      int var5;
      do {
         var4 = false;

         for(var5 = 0; var5 < var2; ++var5) {
            if (var3[var5] < 0) {
               var4 = true;
               this.visitBytecode(var1, var3, var5);
            }
         }
      } while(var4);

      var5 = 1;

      for(int var6 = 0; var6 < var2; ++var6) {
         if (var3[var6] > var5) {
            var5 = var3[var6];
         }
      }

      return var5 - 1;
   }

   private void initStack(int[] var1, CodeAttribute var2) {
      var1[0] = -1;
      ExceptionTable var3 = var2.getExceptionTable();
      if (var3 != null) {
         int var4 = var3.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            var1[var3.handlerPc(var5)] = -2;
         }
      }

   }

   private void visitBytecode(CodeIterator var1, int[] var2, int var3) throws BadBytecode {
      int var4 = var2.length;
      var1.move(var3);
      int var5 = -var2[var3];
      int[] var6 = new int[]{-1};

      while(var1.hasNext()) {
         var3 = var1.next();
         var2[var3] = var5;
         int var7 = var1.byteAt(var3);
         var5 = this.visitInst(var7, var1, var3, var5);
         if (var5 < 1) {
            throw new BadBytecode("stack underflow at " + var3);
         }

         if (var5 <= var6 || var2 <= var7) {
            break;
         }

         if (var7 == 168 || var7 == 201) {
            --var5;
         }
      }

   }

   private void checkTarget(int var1, int var2, int var3, int[] var4, int var5) throws BadBytecode {
      if (var2 >= 0 && var3 > var2) {
         int var6 = var4[var2];
         if (var6 == 0) {
            var4[var2] = -var5;
         } else if (var6 != var5 && var6 != -var5) {
            throw new BadBytecode("verification error (" + var5 + "," + var6 + ") at " + var1);
         }

      } else {
         throw new BadBytecode("bad branch offset at " + var1);
      }
   }

   private int visitInst(int var1, CodeIterator var2, int var3, int var4) throws BadBytecode {
      String var5;
      switch(var1) {
      case 178:
         var4 += this.getFieldSize(var2, var3);
         break;
      case 179:
         var4 -= this.getFieldSize(var2, var3);
         break;
      case 180:
         var4 += this.getFieldSize(var2, var3) - 1;
         break;
      case 181:
         var4 -= this.getFieldSize(var2, var3) + 1;
         break;
      case 182:
      case 183:
         var5 = this.constPool.getMethodrefType(var2.u16bitAt(var3 + 1));
         var4 += Descriptor.dataSize(var5) - 1;
         break;
      case 184:
         var5 = this.constPool.getMethodrefType(var2.u16bitAt(var3 + 1));
         var4 += Descriptor.dataSize(var5);
         break;
      case 185:
         var5 = this.constPool.getInterfaceMethodrefType(var2.u16bitAt(var3 + 1));
         var4 += Descriptor.dataSize(var5) - 1;
         break;
      case 186:
         var5 = this.constPool.getInvokeDynamicType(var2.u16bitAt(var3 + 1));
         var4 += Descriptor.dataSize(var5);
         break;
      case 191:
         var4 = 1;
         break;
      case 196:
         var1 = var2.byteAt(var3 + 1);
      case 187:
      case 188:
      case 189:
      case 190:
      case 192:
      case 193:
      case 194:
      case 195:
      default:
         var4 += STACK_GROW[var1];
         break;
      case 197:
         var4 += 1 - var2.byteAt(var3 + 3);
      }

      return var4;
   }

   private int getFieldSize(CodeIterator var1, int var2) {
      String var3 = this.constPool.getFieldrefType(var1.u16bitAt(var2 + 1));
      return Descriptor.dataSize(var3);
   }
}
