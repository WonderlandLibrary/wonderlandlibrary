package javassist.bytecode;

class ExceptionTableEntry {
   int startPc;
   int endPc;
   int handlerPc;
   int catchType;

   ExceptionTableEntry(int var1, int var2, int var3, int var4) {
      this.startPc = var1;
      this.endPc = var2;
      this.handlerPc = var3;
      this.catchType = var4;
   }
}
