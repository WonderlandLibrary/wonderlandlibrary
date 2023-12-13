package org.lwjgl.opengl;

class ReferencesStack {
   private References[] references_stack;
   private int stack_pos;

   public References getReferences() {
      return this.references_stack[this.stack_pos];
   }

   public void pushState() {
      int var1 = ++this.stack_pos;
      if (var1 == this.references_stack.length) {
         this.growStack();
      }

      this.references_stack[var1].copy(this.references_stack[var1 - 1], -1);
   }

   public References popState(int var1) {
      References var2 = this.references_stack[this.stack_pos--];
      this.references_stack[this.stack_pos].copy(var2, ~var1);
      var2.clear();
      return var2;
   }

   private void growStack() {
      References[] var1 = new References[this.references_stack.length + 1];
      System.arraycopy(this.references_stack, 0, var1, 0, this.references_stack.length);
      this.references_stack = var1;
      this.references_stack[this.references_stack.length - 1] = new References(GLContext.getCapabilities());
   }

   ReferencesStack() {
      ContextCapabilities var1 = GLContext.getCapabilities();
      this.references_stack = new References[1];
      this.stack_pos = 0;

      for(int var2 = 0; var2 < this.references_stack.length; ++var2) {
         this.references_stack[var2] = new References(var1);
      }

   }
}
