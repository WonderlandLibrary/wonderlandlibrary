package javassist.bytecode.stackmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;

public class BasicBlock {
   protected int position;
   protected int length;
   protected int incoming;
   protected BasicBlock[] exit;
   protected boolean stop;
   protected BasicBlock.Catch toCatch;

   protected BasicBlock(int var1) {
      this.position = var1;
      this.length = 0;
      this.incoming = 0;
   }

   public static BasicBlock find(BasicBlock[] var0, int var1) throws BadBytecode {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         int var3 = var0[var2].position;
         if (var3 <= var1 && var1 < var3 + var0[var2].length) {
            return var0[var2];
         }
      }

      throw new BadBytecode("no basic block at " + var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.getClass().getName();
      int var3 = var2.lastIndexOf(46);
      var1.append(var3 < 0 ? var2 : var2.substring(var3 + 1));
      var1.append("[");
      this.toString2(var1);
      var1.append("]");
      return var1.toString();
   }

   protected void toString2(StringBuffer var1) {
      var1.append("pos=").append(this.position).append(", len=").append(this.length).append(", in=").append(this.incoming).append(", exit{");
      if (this.exit != null) {
         for(int var2 = 0; var2 < this.exit.length; ++var2) {
            var1.append(this.exit[var2].position).append(",");
         }
      }

      var1.append("}, {");

      for(BasicBlock.Catch var3 = this.toCatch; var3 != null; var3 = var3.next) {
         var1.append("(").append(var3.body.position).append(", ").append(var3.typeIndex).append("), ");
      }

      var1.append("}");
   }

   public static class Maker {
      protected BasicBlock makeBlock(int var1) {
         return new BasicBlock(var1);
      }

      protected BasicBlock[] makeArray(int var1) {
         return new BasicBlock[var1];
      }

      private BasicBlock[] makeArray(BasicBlock var1) {
         BasicBlock[] var2 = this.makeArray(1);
         var2[0] = var1;
         return var2;
      }

      private BasicBlock[] makeArray(BasicBlock var1, BasicBlock var2) {
         BasicBlock[] var3 = this.makeArray(2);
         var3[0] = var1;
         var3[1] = var2;
         return var3;
      }

      public BasicBlock[] make(MethodInfo var1) throws BadBytecode {
         CodeAttribute var2 = var1.getCodeAttribute();
         if (var2 == null) {
            return null;
         } else {
            CodeIterator var3 = var2.iterator();
            return this.make(var3, 0, var3.getCodeLength(), var2.getExceptionTable());
         }
      }

      public BasicBlock[] make(CodeIterator var1, int var2, int var3, ExceptionTable var4) throws BadBytecode {
         HashMap var5 = this.makeMarks(var1, var2, var3, var4);
         BasicBlock[] var6 = this.makeBlocks(var5);
         this.addCatchers(var6, var4);
         return var6;
      }

      private BasicBlock.Mark makeMark(HashMap var1, int var2) {
         return this.makeMark0(var1, var2, true, true);
      }

      private BasicBlock.Mark makeMark(HashMap var1, int var2, BasicBlock[] var3, int var4, boolean var5) {
         BasicBlock.Mark var6 = this.makeMark0(var1, var2, false, false);
         var6.setJump(var3, var4, var5);
         return var6;
      }

      private BasicBlock.Mark makeMark0(HashMap var1, int var2, boolean var3, boolean var4) {
         Integer var5 = new Integer(var2);
         BasicBlock.Mark var6 = (BasicBlock.Mark)var1.get(var5);
         if (var6 == null) {
            var6 = new BasicBlock.Mark(var2);
            var1.put(var5, var6);
         }

         if (var3) {
            if (var6.block == null) {
               var6.block = this.makeBlock(var2);
            }

            if (var4) {
               ++var6.block.incoming;
            }
         }

         return var6;
      }

      private HashMap makeMarks(CodeIterator var1, int var2, int var3, ExceptionTable var4) throws BadBytecode {
         var1.begin();
         var1.move(var2);
         HashMap var5 = new HashMap();

         int var6;
         while(var1.hasNext()) {
            var6 = var1.next();
            if (var6 >= var3) {
               break;
            }

            int var7 = var1.byteAt(var6);
            if ((153 > var7 || var7 > 166) && var7 != 198 && var7 != 199) {
               if (167 <= var7 && var7 <= 171) {
                  int var11;
                  int var13;
                  int var16;
                  int var17;
                  switch(var7) {
                  case 167:
                     this.makeGoto(var5, var6, var6 + var1.s16bitAt(var6 + 1), 3);
                     break;
                  case 168:
                     this.makeJsr(var5, var6, var6 + var1.s16bitAt(var6 + 1), 3);
                     break;
                  case 169:
                     this.makeMark(var5, var6, (BasicBlock[])null, 2, true);
                     break;
                  case 170:
                     var16 = (var6 & -4) + 4;
                     var17 = var1.s32bitAt(var16 + 4);
                     int var18 = var1.s32bitAt(var16 + 8);
                     var11 = var18 - var17 + 1;
                     BasicBlock[] var19 = this.makeArray(var11 + 1);
                     var19[0] = this.makeMark(var5, var6 + var1.s32bitAt(var16)).block;
                     var13 = var16 + 12;
                     int var14 = var13 + var11 * 4;

                     for(int var15 = 1; var13 < var14; var13 += 4) {
                        var19[var15++] = this.makeMark(var5, var6 + var1.s32bitAt(var13)).block;
                     }

                     this.makeMark(var5, var6, var19, var14 - var6, true);
                     break;
                  case 171:
                     var16 = (var6 & -4) + 4;
                     var17 = var1.s32bitAt(var16 + 4);
                     BasicBlock[] var10 = this.makeArray(var17 + 1);
                     var10[0] = this.makeMark(var5, var6 + var1.s32bitAt(var16)).block;
                     var11 = var16 + 8 + 4;
                     int var12 = var11 + var17 * 8 - 4;

                     for(var13 = 1; var11 < var12; var11 += 8) {
                        var10[var13++] = this.makeMark(var5, var6 + var1.s32bitAt(var11)).block;
                     }

                     this.makeMark(var5, var6, var10, var12 - var6, true);
                  }
               } else if ((172 > var7 || var7 > 177) && var7 != 191) {
                  if (var7 == 200) {
                     this.makeGoto(var5, var6, var6 + var1.s32bitAt(var6 + 1), 5);
                  } else if (var7 == 201) {
                     this.makeJsr(var5, var6, var6 + var1.s32bitAt(var6 + 1), 5);
                  } else if (var7 == 196 && var1.byteAt(var6 + 1) == 169) {
                     this.makeMark(var5, var6, (BasicBlock[])null, 4, true);
                  }
               } else {
                  this.makeMark(var5, var6, (BasicBlock[])null, 1, true);
               }
            } else {
               BasicBlock.Mark var8 = this.makeMark(var5, var6 + var1.s16bitAt(var6 + 1));
               BasicBlock.Mark var9 = this.makeMark(var5, var6 + 3);
               this.makeMark(var5, var6, this.makeArray(var8.block, var9.block), 3, false);
            }
         }

         if (var4 != null) {
            var6 = var4.size();

            while(true) {
               --var6;
               if (var6 < 0) {
                  break;
               }

               this.makeMark0(var5, var4.startPc(var6), true, false);
               this.makeMark(var5, var4.handlerPc(var6));
            }
         }

         return var5;
      }

      private void makeGoto(HashMap var1, int var2, int var3, int var4) {
         BasicBlock.Mark var5 = this.makeMark(var1, var3);
         BasicBlock[] var6 = this.makeArray(var5.block);
         this.makeMark(var1, var2, var6, var4, true);
      }

      protected void makeJsr(HashMap var1, int var2, int var3, int var4) throws BadBytecode {
         throw new BasicBlock.JsrBytecode();
      }

      private BasicBlock[] makeBlocks(HashMap var1) {
         BasicBlock.Mark[] var2 = (BasicBlock.Mark[])((BasicBlock.Mark[])var1.values().toArray(new BasicBlock.Mark[var1.size()]));
         Arrays.sort(var2);
         ArrayList var3 = new ArrayList();
         int var4 = 0;
         BasicBlock var5;
         if (var2.length > 0 && var2[0].position == 0 && var2[0].block != null) {
            var5 = getBBlock(var2[var4++]);
         } else {
            var5 = this.makeBlock(0);
         }

         var3.add(var5);

         while(var4 < var2.length) {
            BasicBlock.Mark var6 = var2[var4++];
            BasicBlock var7 = getBBlock(var6);
            if (var7 == null) {
               if (var5.length > 0) {
                  var5 = this.makeBlock(var5.position + var5.length);
                  var3.add(var5);
               }

               var5.length = var6.position + var6.size - var5.position;
               var5.exit = var6.jump;
               var5.stop = var6.alwaysJmp;
            } else {
               if (var5.length == 0) {
                  var5.length = var6.position - var5.position;
                  ++var7.incoming;
                  var5.exit = this.makeArray(var7);
               } else if (var5.position + var5.length < var6.position) {
                  var5 = this.makeBlock(var5.position + var5.length);
                  var3.add(var5);
                  var5.length = var6.position - var5.position;
                  var5.stop = true;
                  var5.exit = this.makeArray(var7);
               }

               var3.add(var7);
               var5 = var7;
            }
         }

         return (BasicBlock[])((BasicBlock[])var3.toArray(this.makeArray(var3.size())));
      }

      private static BasicBlock getBBlock(BasicBlock.Mark var0) {
         BasicBlock var1 = var0.block;
         if (var1 != null && var0.size > 0) {
            var1.exit = var0.jump;
            var1.length = var0.size;
            var1.stop = var0.alwaysJmp;
         }

         return var1;
      }

      private void addCatchers(BasicBlock[] var1, ExceptionTable var2) throws BadBytecode {
         if (var2 != null) {
            int var3 = var2.size();

            while(true) {
               --var3;
               if (var3 < 0) {
                  return;
               }

               BasicBlock var4 = BasicBlock.find(var1, var2.handlerPc(var3));
               int var5 = var2.startPc(var3);
               int var6 = var2.endPc(var3);
               int var7 = var2.catchType(var3);
               --var4.incoming;

               for(int var8 = 0; var8 < var1.length; ++var8) {
                  BasicBlock var9 = var1[var8];
                  int var10 = var9.position;
                  if (var5 <= var10 && var10 < var6) {
                     var9.toCatch = new BasicBlock.Catch(var4, var7, var9.toCatch);
                     ++var4.incoming;
                  }
               }
            }
         }
      }
   }

   static class Mark implements Comparable {
      int position;
      BasicBlock block;
      BasicBlock[] jump;
      boolean alwaysJmp;
      int size;
      BasicBlock.Catch catcher;

      Mark(int var1) {
         this.position = var1;
         this.block = null;
         this.jump = null;
         this.alwaysJmp = false;
         this.size = 0;
         this.catcher = null;
      }

      public int compareTo(Object var1) {
         if (var1 instanceof BasicBlock.Mark) {
            int var2 = ((BasicBlock.Mark)var1).position;
            return this.position - var2;
         } else {
            return -1;
         }
      }

      void setJump(BasicBlock[] var1, int var2, boolean var3) {
         this.jump = var1;
         this.size = var2;
         this.alwaysJmp = var3;
      }
   }

   public static class Catch {
      public BasicBlock.Catch next;
      public BasicBlock body;
      public int typeIndex;

      Catch(BasicBlock var1, int var2, BasicBlock.Catch var3) {
         this.body = var1;
         this.typeIndex = var2;
         this.next = var3;
      }
   }

   static class JsrBytecode extends BadBytecode {
      JsrBytecode() {
         super("JSR");
      }
   }
}
