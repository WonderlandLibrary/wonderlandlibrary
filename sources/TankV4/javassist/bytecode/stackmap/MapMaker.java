package javassist.bytecode.stackmap;

import java.util.ArrayList;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ByteArray;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.StackMap;
import javassist.bytecode.StackMapTable;

public class MapMaker extends Tracer {
   public static StackMapTable make(ClassPool var0, MethodInfo var1) throws BadBytecode {
      CodeAttribute var2 = var1.getCodeAttribute();
      if (var2 == null) {
         return null;
      } else {
         TypedBlock[] var3;
         try {
            var3 = TypedBlock.makeBlocks(var1, var2, true);
         } catch (BasicBlock.JsrBytecode var7) {
            return null;
         }

         if (var3 == null) {
            return null;
         } else {
            MapMaker var4 = new MapMaker(var0, var1, var2);

            try {
               var4.make(var3, var2.getCode());
            } catch (BadBytecode var6) {
               throw new BadBytecode(var1, var6);
            }

            return var4.toStackMap(var3);
         }
      }
   }

   public static StackMap make2(ClassPool var0, MethodInfo var1) throws BadBytecode {
      CodeAttribute var2 = var1.getCodeAttribute();
      if (var2 == null) {
         return null;
      } else {
         TypedBlock[] var3;
         try {
            var3 = TypedBlock.makeBlocks(var1, var2, true);
         } catch (BasicBlock.JsrBytecode var7) {
            return null;
         }

         if (var3 == null) {
            return null;
         } else {
            MapMaker var4 = new MapMaker(var0, var1, var2);

            try {
               var4.make(var3, var2.getCode());
            } catch (BadBytecode var6) {
               throw new BadBytecode(var1, var6);
            }

            return var4.toStackMap2(var1.getConstPool(), var3);
         }
      }
   }

   public MapMaker(ClassPool var1, MethodInfo var2, CodeAttribute var3) {
      super(var1, var2.getConstPool(), var3.getMaxStack(), var3.getMaxLocals(), TypedBlock.getRetType(var2.getDescriptor()));
   }

   protected MapMaker(MapMaker var1) {
      super(var1);
   }

   void make(TypedBlock[] var1, byte[] var2) throws BadBytecode {
      this.make(var2, var1[0]);
      this.findDeadCatchers(var2, var1);

      try {
         this.fixTypes(var2, var1);
      } catch (NotFoundException var4) {
         throw new BadBytecode("failed to resolve types", var4);
      }
   }

   private void make(byte[] var1, TypedBlock var2) throws BadBytecode {
      copyTypeData(var2.stackTop, var2.stackTypes, this.stackTypes);
      this.stackTop = var2.stackTop;
      copyTypeData(var2.localsTypes.length, var2.localsTypes, this.localsTypes);
      this.traceException(var1, var2.toCatch);
      int var3 = var2.position;
      int var4 = var3 + var2.length;

      while(var3 < var4) {
         var3 += this.doOpcode(var3, var1);
         this.traceException(var1, var2.toCatch);
      }

      if (var2.exit != null) {
         for(int var5 = 0; var5 < var2.exit.length; ++var5) {
            TypedBlock var6 = (TypedBlock)var2.exit[var5];
            if (var6.alreadySet()) {
               this.mergeMap(var6, true);
            } else {
               this.recordStackMap(var6);
               MapMaker var7 = new MapMaker(this);
               var7.make(var1, var6);
            }
         }
      }

   }

   private void traceException(byte[] var1, BasicBlock.Catch var2) throws BadBytecode {
      for(; var2 != null; var2 = var2.next) {
         TypedBlock var3 = (TypedBlock)var2.body;
         if (var3.alreadySet()) {
            this.mergeMap(var3, false);
            if (var3.stackTop < 1) {
               throw new BadBytecode("bad catch clause: " + var2.typeIndex);
            }

            var3.stackTypes[0] = this.merge(this.toExceptionType(var2.typeIndex), var3.stackTypes[0]);
         } else {
            this.recordStackMap(var3, var2.typeIndex);
            MapMaker var4 = new MapMaker(this);
            var4.make(var1, var3);
         }
      }

   }

   private void mergeMap(TypedBlock var1, boolean var2) throws BadBytecode {
      int var3 = this.localsTypes.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         var1.localsTypes[var4] = this.merge(validateTypeData(this.localsTypes, var3, var4), var1.localsTypes[var4]);
      }

      if (var2) {
         var3 = this.stackTop;

         for(var4 = 0; var4 < var3; ++var4) {
            var1.stackTypes[var4] = this.merge(this.stackTypes[var4], var1.stackTypes[var4]);
         }
      }

   }

   private TypeData merge(TypeData var1, TypeData var2) throws BadBytecode {
      if (var1 == var2) {
         return var2;
      } else if (!(var2 instanceof TypeData.ClassName) && !(var2 instanceof TypeData.BasicType)) {
         if (var2 instanceof TypeData.AbsTypeVar) {
            ((TypeData.AbsTypeVar)var2).merge(var1);
            return var2;
         } else {
            throw new RuntimeException("fatal: this should never happen");
         }
      } else {
         return var2;
      }
   }

   private void recordStackMap(TypedBlock var1) throws BadBytecode {
      TypeData[] var2 = TypeData.make(this.stackTypes.length);
      int var3 = this.stackTop;
      recordTypeData(var3, this.stackTypes, var2);
      this.recordStackMap0(var1, var3, var2);
   }

   private void recordStackMap(TypedBlock var1, int var2) throws BadBytecode {
      TypeData[] var3 = TypeData.make(this.stackTypes.length);
      var3[0] = this.toExceptionType(var2).join();
      this.recordStackMap0(var1, 1, var3);
   }

   private TypeData.ClassName toExceptionType(int var1) {
      String var2;
      if (var1 == 0) {
         var2 = "java.lang.Throwable";
      } else {
         var2 = this.cpool.getClassInfo(var1);
      }

      return new TypeData.ClassName(var2);
   }

   private void recordStackMap0(TypedBlock var1, int var2, TypeData[] var3) throws BadBytecode {
      int var4 = this.localsTypes.length;
      TypeData[] var5 = TypeData.make(var4);
      int var6 = recordTypeData(var4, this.localsTypes, var5);
      var1.setStackMap(var2, var3, var6, var5);
   }

   protected static int recordTypeData(int var0, TypeData[] var1, TypeData[] var2) {
      int var3 = -1;

      for(int var4 = 0; var4 < var0; ++var4) {
         TypeData var5 = validateTypeData(var1, var0, var4);
         var2[var4] = var5.join();
         if (var5 != TOP) {
            var3 = var4 + 1;
         }
      }

      return var3 + 1;
   }

   protected static void copyTypeData(int var0, TypeData[] var1, TypeData[] var2) {
      for(int var3 = 0; var3 < var0; ++var3) {
         var2[var3] = var1[var3];
      }

   }

   private static TypeData validateTypeData(TypeData[] var0, int var1, int var2) {
      TypeData var3 = var0[var2];
      return var3.is2WordType() && var2 + 1 < var1 && var0[var2 + 1] != TOP ? TOP : var3;
   }

   private void findDeadCatchers(byte[] var1, TypedBlock[] var2) throws BadBytecode {
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TypedBlock var5 = var2[var4];
         if (!var5.alreadySet()) {
            this.fixDeadcode(var1, var5);
            BasicBlock.Catch var6 = var5.toCatch;
            if (var6 != null) {
               TypedBlock var7 = (TypedBlock)var6.body;
               if (!var7.alreadySet()) {
                  this.recordStackMap(var7, var6.typeIndex);
                  this.fixDeadcode(var1, var7);
                  var7.incoming = 1;
               }
            }
         }
      }

   }

   private void fixDeadcode(byte[] var1, TypedBlock var2) throws BadBytecode {
      int var3 = var2.position;
      int var4 = var2.length - 3;
      if (var4 < 0) {
         if (var4 == -1) {
            var1[var3] = 0;
         }

         var1[var3 + var2.length - 1] = -65;
         var2.incoming = 1;
         this.recordStackMap(var2, 0);
      } else {
         var2.incoming = 0;

         for(int var5 = 0; var5 < var4; ++var5) {
            var1[var3 + var5] = 0;
         }

         var1[var3 + var4] = -89;
         ByteArray.write16bit(-var4, var1, var3 + var4 + 1);
      }
   }

   private void fixTypes(byte[] var1, TypedBlock[] var2) throws NotFoundException, BadBytecode {
      ArrayList var3 = new ArrayList();
      int var4 = var2.length;
      int var5 = 0;

      for(int var6 = 0; var6 < var4; ++var6) {
         TypedBlock var7 = var2[var6];
         if (var7.alreadySet()) {
            int var8 = var7.localsTypes.length;

            int var9;
            for(var9 = 0; var9 < var8; ++var9) {
               var5 = var7.localsTypes[var9].dfs(var3, var5, this.classPool);
            }

            var8 = var7.stackTop;

            for(var9 = 0; var9 < var8; ++var9) {
               var5 = var7.stackTypes[var9].dfs(var3, var5, this.classPool);
            }
         }
      }

   }

   public StackMapTable toStackMap(TypedBlock[] var1) {
      StackMapTable.Writer var2 = new StackMapTable.Writer(32);
      int var3 = var1.length;
      TypedBlock var4 = var1[0];
      int var5 = var4.length;
      if (var4.incoming > 0) {
         var2.sameFrame(0);
         --var5;
      }

      for(int var6 = 1; var6 < var3; ++var6) {
         TypedBlock var7 = var1[var6];
         if (var7 > var1[var6 - 1]) {
            var7.resetNumLocals();
            int var8 = stackMapDiff(var4.numLocals, var4.localsTypes, var7.numLocals, var7.localsTypes);
            this.toStackMapBody(var2, var7, var8, var5, var4);
            var5 = var7.length - 1;
            var4 = var7;
         } else if (var7.incoming == 0) {
            var2.sameFrame(var5);
            var5 = var7.length - 1;
            var4 = var7;
         } else {
            var5 += var7.length;
         }
      }

      return var2.toStackMapTable(this.cpool);
   }

   private void toStackMapBody(StackMapTable.Writer var1, TypedBlock var2, int var3, int var4, TypedBlock var5) {
      int var6 = var2.stackTop;
      int[] var7;
      int[] var8;
      if (var6 == 0) {
         if (var3 == 0) {
            var1.sameFrame(var4);
            return;
         }

         if (0 > var3 && var3 >= -3) {
            var1.chopFrame(var4, -var3);
            return;
         }

         if (0 < var3 && var3 <= 3) {
            var7 = new int[var3];
            var8 = this.fillStackMap(var2.numLocals - var5.numLocals, var5.numLocals, var7, var2.localsTypes);
            var1.appendFrame(var4, var8, var7);
            return;
         }
      } else {
         TypeData var11;
         if (var6 == 1 && var3 == 0) {
            var11 = var2.stackTypes[0];
            var1.sameLocals(var4, var11.getTypeTag(), var11.getTypeData(this.cpool));
            return;
         }

         if (var6 == 2 && var3 == 0) {
            var11 = var2.stackTypes[0];
            if (var11.is2WordType()) {
               var1.sameLocals(var4, var11.getTypeTag(), var11.getTypeData(this.cpool));
               return;
            }
         }
      }

      var7 = new int[var6];
      var8 = this.fillStackMap(var6, 0, var7, var2.stackTypes);
      int[] var9 = new int[var2.numLocals];
      int[] var10 = this.fillStackMap(var2.numLocals, 0, var9, var2.localsTypes);
      var1.fullFrame(var4, var10, var9, var8, var7);
   }

   private int[] fillStackMap(int var1, int var2, int[] var3, TypeData[] var4) {
      int var5 = diffSize(var4, var2, var2 + var1);
      ConstPool var6 = this.cpool;
      int[] var7 = new int[var5];
      int var8 = 0;

      for(int var9 = 0; var9 < var1; ++var9) {
         TypeData var10 = var4[var2 + var9];
         var7[var8] = var10.getTypeTag();
         var3[var8] = var10.getTypeData(var6);
         if (var10.is2WordType()) {
            ++var9;
         }

         ++var8;
      }

      return var7;
   }

   private static int stackMapDiff(int var0, TypeData[] var1, int var2, TypeData[] var3) {
      int var4 = var2 - var0;
      int var5;
      if (var4 > 0) {
         var5 = var0;
      } else {
         var5 = var2;
      }

      if (var3 < var5) {
         return var4 > 0 ? diffSize(var3, var5, var2) : -diffSize(var1, var5, var0);
      } else {
         return -100;
      }
   }

   private static int diffSize(TypeData[] var0, int var1, int var2) {
      int var3 = 0;

      while(var1 < var2) {
         TypeData var4 = var0[var1++];
         ++var3;
         if (var4.is2WordType()) {
            ++var1;
         }
      }

      return var3;
   }

   public StackMap toStackMap2(ConstPool var1, TypedBlock[] var2) {
      StackMap.Writer var3 = new StackMap.Writer();
      int var4 = var2.length;
      boolean[] var5 = new boolean[var4];
      TypedBlock var6 = var2[0];
      var5[0] = var6.incoming > 0;
      int var7 = var5[0] ? 1 : 0;

      int var8;
      for(var8 = 1; var8 < var4; ++var8) {
         TypedBlock var9 = var2[var8];
         if (var5[var8] = this.isTarget(var9, var2[var8 - 1])) {
            var9.resetNumLocals();
            ++var7;
         }
      }

      if (var7 == 0) {
         return null;
      } else {
         var3.write16bit(var7);

         for(var8 = 0; var8 < var4; ++var8) {
            if (var5[var8]) {
               this.writeStackFrame(var3, var1, var2[var8].position, var2[var8]);
            }
         }

         return var3.toStackMap(var1);
      }
   }

   private void writeStackFrame(StackMap.Writer var1, ConstPool var2, int var3, TypedBlock var4) {
      var1.write16bit(var3);
      this.writeVerifyTypeInfo(var1, var2, var4.localsTypes, var4.numLocals);
      this.writeVerifyTypeInfo(var1, var2, var4.stackTypes, var4.stackTop);
   }

   private void writeVerifyTypeInfo(StackMap.Writer var1, ConstPool var2, TypeData[] var3, int var4) {
      int var5 = 0;

      int var6;
      TypeData var7;
      for(var6 = 0; var6 < var4; ++var6) {
         var7 = var3[var6];
         if (var7 != null && var7.is2WordType()) {
            ++var5;
            ++var6;
         }
      }

      var1.write16bit(var4 - var5);

      for(var6 = 0; var6 < var4; ++var6) {
         var7 = var3[var6];
         var1.writeVerifyTypeInfo(var7.getTypeTag(), var7.getTypeData(var2));
         if (var7.is2WordType()) {
            ++var6;
         }
      }

   }
}
