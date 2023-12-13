package org.lwjgl.opengl;

class StateStack {
   private int[] state_stack = new int[1];
   private int stack_pos = 0;

   public int getState() {
      return this.state_stack[this.stack_pos];
   }

   public void pushState(int var1) {
      int var2 = ++this.stack_pos;
      if (var2 == this.state_stack.length) {
         this.growState();
      }

      this.state_stack[var2] = var1;
   }

   public int popState() {
      return this.state_stack[this.stack_pos--];
   }

   public void growState() {
      int[] var1 = new int[this.state_stack.length + 1];
      System.arraycopy(this.state_stack, 0, var1, 0, this.state_stack.length);
      this.state_stack = var1;
   }

   StateStack(int var1) {
      this.state_stack[this.stack_pos] = var1;
   }
}
