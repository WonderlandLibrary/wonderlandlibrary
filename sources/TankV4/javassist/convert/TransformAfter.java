package javassist.convert;

import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;

public class TransformAfter extends TransformBefore {
   public TransformAfter(Transformer var1, CtMethod var2, CtMethod var3) throws NotFoundException {
      super(var1, var2, var3);
   }

   protected int match2(int var1, CodeIterator var2) throws BadBytecode {
      var2.move(var1);
      var2.insert(this.saveCode);
      var2.insert(this.loadCode);
      int var3 = var2.insertGap(3);
      var2.setMark(var3);
      var2.insert(this.loadCode);
      var1 = var2.next();
      var3 = var2.getMark();
      var2.writeByte(var2.byteAt(var1), var3);
      var2.write16bit(var2.u16bitAt(var1 + 1), var3 + 1);
      var2.writeByte(184, var1);
      var2.write16bit(this.newIndex, var1 + 1);
      var2.move(var3);
      return var2.next();
   }
}
