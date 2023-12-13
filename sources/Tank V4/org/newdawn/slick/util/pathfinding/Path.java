package org.newdawn.slick.util.pathfinding;

import java.io.Serializable;
import java.util.ArrayList;

public class Path implements Serializable {
   private static final long serialVersionUID = 1L;
   private ArrayList steps = new ArrayList();

   public int getLength() {
      return this.steps.size();
   }

   public Path.Step getStep(int var1) {
      return (Path.Step)this.steps.get(var1);
   }

   public int getX(int var1) {
      return Path.Step.access$000(this.getStep(var1));
   }

   public int getY(int var1) {
      return Path.Step.access$100(this.getStep(var1));
   }

   public void appendStep(int var1, int var2) {
      this.steps.add(new Path.Step(this, var1, var2));
   }

   public void prependStep(int var1, int var2) {
      this.steps.add(0, new Path.Step(this, var1, var2));
   }

   public boolean contains(int var1, int var2) {
      return this.steps.contains(new Path.Step(this, var1, var2));
   }

   public class Step implements Serializable {
      private int x;
      private int y;
      private final Path this$0;

      public Step(Path var1, int var2, int var3) {
         this.this$0 = var1;
         this.x = var2;
         this.y = var3;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public int hashCode() {
         return this.x * this.y;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof Path.Step)) {
            return false;
         } else {
            Path.Step var2 = (Path.Step)var1;
            return var2.x == this.x && var2.y == this.y;
         }
      }

      static int access$000(Path.Step var0) {
         return var0.x;
      }

      static int access$100(Path.Step var0) {
         return var0.y;
      }
   }
}
