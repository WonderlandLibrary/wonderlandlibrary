package javassist.bytecode.analysis;

import java.util.ArrayList;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.stackmap.BasicBlock;

public class ControlFlow {
   private CtClass clazz;
   private MethodInfo methodInfo;
   private ControlFlow.Block[] basicBlocks;
   private Frame[] frames;

   public ControlFlow(CtMethod var1) throws BadBytecode {
      this(var1.getDeclaringClass(), var1.getMethodInfo2());
   }

   public ControlFlow(CtClass var1, MethodInfo var2) throws BadBytecode {
      this.clazz = var1;
      this.methodInfo = var2;
      this.frames = null;
      this.basicBlocks = (ControlFlow.Block[])((ControlFlow.Block[])(new BasicBlock.Maker(this) {
         final ControlFlow this$0;

         {
            this.this$0 = var1;
         }

         protected BasicBlock makeBlock(int var1) {
            return new ControlFlow.Block(var1, ControlFlow.access$000(this.this$0));
         }

         protected BasicBlock[] makeArray(int var1) {
            return new ControlFlow.Block[var1];
         }
      }).make(var2));
      int var3 = this.basicBlocks.length;
      int[] var4 = new int[var3];

      int var5;
      ControlFlow.Block var6;
      for(var5 = 0; var5 < var3; ++var5) {
         var6 = this.basicBlocks[var5];
         var6.index = var5;
         var6.entrances = new ControlFlow.Block[var6.incomings()];
         var4[var5] = 0;
      }

      for(var5 = 0; var5 < var3; ++var5) {
         var6 = this.basicBlocks[var5];

         for(int var7 = 0; var7 < var6.exits(); ++var7) {
            ControlFlow.Block var8 = var6.exit(var7);
            var8.entrances[var4[var8.index]++] = var6;
         }

         ControlFlow.Catcher[] var10 = var6.catchers();

         for(int var11 = 0; var11 < var10.length; ++var11) {
            ControlFlow.Block var9 = ControlFlow.Catcher.access$100(var10[var11]);
            var9.entrances[var4[var9.index]++] = var6;
         }
      }

   }

   public ControlFlow.Block[] basicBlocks() {
      return this.basicBlocks;
   }

   public Frame frameAt(int var1) throws BadBytecode {
      if (this.frames == null) {
         this.frames = (new Analyzer()).analyze(this.clazz, this.methodInfo);
      }

      return this.frames[var1];
   }

   public ControlFlow.Node[] dominatorTree() {
      int var1 = this.basicBlocks.length;
      if (var1 == 0) {
         return null;
      } else {
         ControlFlow.Node[] var2 = new ControlFlow.Node[var1];
         boolean[] var3 = new boolean[var1];
         int[] var4 = new int[var1];

         for(int var5 = 0; var5 < var1; ++var5) {
            var2[var5] = new ControlFlow.Node(this.basicBlocks[var5]);
            var3[var5] = false;
         }

         ControlFlow.Access var7 = new ControlFlow.Access(this, var2) {
            final ControlFlow this$0;

            {
               this.this$0 = var1;
            }

            BasicBlock[] exits(ControlFlow.Node var1) {
               return ControlFlow.Node.access$200(var1).getExit();
            }

            BasicBlock[] entrances(ControlFlow.Node var1) {
               return ControlFlow.Node.access$200(var1).entrances;
            }
         };
         var2[0].makeDepth1stTree((ControlFlow.Node)null, var3, 0, var4, var7);

         do {
            for(int var6 = 0; var6 < var1; ++var6) {
               var3[var6] = false;
            }
         } while(var2[0].makeDominatorTree(var3, var4, var7));

         ControlFlow.Node.access$300(var2);
         return var2;
      }
   }

   public ControlFlow.Node[] postDominatorTree() {
      int var1 = this.basicBlocks.length;
      if (var1 == 0) {
         return null;
      } else {
         ControlFlow.Node[] var2 = new ControlFlow.Node[var1];
         boolean[] var3 = new boolean[var1];
         int[] var4 = new int[var1];

         for(int var5 = 0; var5 < var1; ++var5) {
            var2[var5] = new ControlFlow.Node(this.basicBlocks[var5]);
            var3[var5] = false;
         }

         ControlFlow.Access var9 = new ControlFlow.Access(this, var2) {
            final ControlFlow this$0;

            {
               this.this$0 = var1;
            }

            BasicBlock[] exits(ControlFlow.Node var1) {
               return ControlFlow.Node.access$200(var1).entrances;
            }

            BasicBlock[] entrances(ControlFlow.Node var1) {
               return ControlFlow.Node.access$200(var1).getExit();
            }
         };
         int var6 = 0;

         for(int var7 = 0; var7 < var1; ++var7) {
            if (ControlFlow.Node.access$200(var2[var7]).exits() == 0) {
               var6 = var2[var7].makeDepth1stTree((ControlFlow.Node)null, var3, var6, var4, var9);
            }
         }

         boolean var10;
         do {
            int var8;
            for(var8 = 0; var8 < var1; ++var8) {
               var3[var8] = false;
            }

            var10 = false;

            for(var8 = 0; var8 < var1; ++var8) {
               if (ControlFlow.Node.access$200(var2[var8]).exits() == 0 && var2[var8].makeDominatorTree(var3, var4, var9)) {
                  var10 = true;
               }
            }
         } while(var10);

         ControlFlow.Node.access$300(var2);
         return var2;
      }
   }

   static MethodInfo access$000(ControlFlow var0) {
      return var0.methodInfo;
   }

   public static class Catcher {
      private ControlFlow.Block node;
      private int typeIndex;

      Catcher(BasicBlock.Catch var1) {
         this.node = (ControlFlow.Block)var1.body;
         this.typeIndex = var1.typeIndex;
      }

      public ControlFlow.Block block() {
         return this.node;
      }

      public String type() {
         return this.typeIndex == 0 ? "java.lang.Throwable" : this.node.method.getConstPool().getClassInfo(this.typeIndex);
      }

      static ControlFlow.Block access$100(ControlFlow.Catcher var0) {
         return var0.node;
      }
   }

   public static class Node {
      private ControlFlow.Block block;
      private ControlFlow.Node parent;
      private ControlFlow.Node[] children;

      Node(ControlFlow.Block var1) {
         this.block = var1;
         this.parent = null;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Node[pos=").append(this.block().position());
         var1.append(", parent=");
         var1.append(this.parent == null ? "*" : Integer.toString(this.parent.block().position()));
         var1.append(", children{");

         for(int var2 = 0; var2 < this.children.length; ++var2) {
            var1.append(this.children[var2].block().position()).append(", ");
         }

         var1.append("}]");
         return var1.toString();
      }

      public ControlFlow.Block block() {
         return this.block;
      }

      public ControlFlow.Node parent() {
         return this.parent;
      }

      public int children() {
         return this.children.length;
      }

      public ControlFlow.Node child(int var1) {
         return this.children[var1];
      }

      int makeDepth1stTree(ControlFlow.Node var1, boolean[] var2, int var3, int[] var4, ControlFlow.Access var5) {
         int var6 = this.block.index;
         if (var2[var6]) {
            return var3;
         } else {
            var2[var6] = true;
            this.parent = var1;
            BasicBlock[] var7 = var5.exits(this);
            if (var7 != null) {
               for(int var8 = 0; var8 < var7.length; ++var8) {
                  ControlFlow.Node var9 = var5.node(var7[var8]);
                  var3 = var9.makeDepth1stTree(this, var2, var3, var4, var5);
               }
            }

            var4[var6] = var3++;
            return var3;
         }
      }

      private static ControlFlow.Node getAncestor(ControlFlow.Node var0, ControlFlow.Node var1, int[] var2) {
         while(true) {
            if (var0 != var1) {
               if (var2[var0.block.index] < var2[var1.block.index]) {
                  var0 = var0.parent;
               } else {
                  var1 = var1.parent;
               }

               if (var0 != null && var1 != null) {
                  continue;
               }

               return null;
            }

            return var0;
         }
      }

      private static void setChildren(ControlFlow.Node[] var0) {
         int var1 = var0.length;
         int[] var2 = new int[var1];

         int var3;
         for(var3 = 0; var3 < var1; ++var3) {
            var2[var3] = 0;
         }

         ControlFlow.Node var4;
         for(var3 = 0; var3 < var1; ++var3) {
            var4 = var0[var3].parent;
            if (var4 != null) {
               ++var2[var4.block.index];
            }
         }

         for(var3 = 0; var3 < var1; ++var3) {
            var0[var3].children = new ControlFlow.Node[var2[var3]];
         }

         for(var3 = 0; var3 < var1; ++var3) {
            var2[var3] = 0;
         }

         for(var3 = 0; var3 < var1; ++var3) {
            var4 = var0[var3];
            ControlFlow.Node var5 = var4.parent;
            if (var5 != null) {
               var5.children[var2[var5.block.index]++] = var4;
            }
         }

      }

      static ControlFlow.Block access$200(ControlFlow.Node var0) {
         return var0.block;
      }

      static void access$300(ControlFlow.Node[] var0) {
         setChildren(var0);
      }
   }

   abstract static class Access {
      ControlFlow.Node[] all;

      Access(ControlFlow.Node[] var1) {
         this.all = var1;
      }

      ControlFlow.Node node(BasicBlock var1) {
         return this.all[((ControlFlow.Block)var1).index];
      }

      abstract BasicBlock[] exits(ControlFlow.Node var1);

      abstract BasicBlock[] entrances(ControlFlow.Node var1);
   }

   public static class Block extends BasicBlock {
      public Object clientData = null;
      int index;
      MethodInfo method;
      ControlFlow.Block[] entrances;

      Block(int var1, MethodInfo var2) {
         super(var1);
         this.method = var2;
      }

      protected void toString2(StringBuffer var1) {
         super.toString2(var1);
         var1.append(", incoming{");

         for(int var2 = 0; var2 < this.entrances.length; ++var2) {
            var1.append(this.entrances[var2].position).append(", ");
         }

         var1.append("}");
      }

      BasicBlock[] getExit() {
         return this.exit;
      }

      public int index() {
         return this.index;
      }

      public int position() {
         return this.position;
      }

      public int length() {
         return this.length;
      }

      public int incomings() {
         return this.incoming;
      }

      public ControlFlow.Block incoming(int var1) {
         return this.entrances[var1];
      }

      public int exits() {
         return this.exit == null ? 0 : this.exit.length;
      }

      public ControlFlow.Block exit(int var1) {
         return (ControlFlow.Block)this.exit[var1];
      }

      public ControlFlow.Catcher[] catchers() {
         ArrayList var1 = new ArrayList();

         for(BasicBlock.Catch var2 = this.toCatch; var2 != null; var2 = var2.next) {
            var1.add(new ControlFlow.Catcher(var2));
         }

         return (ControlFlow.Catcher[])((ControlFlow.Catcher[])var1.toArray(new ControlFlow.Catcher[var1.size()]));
      }
   }
}
