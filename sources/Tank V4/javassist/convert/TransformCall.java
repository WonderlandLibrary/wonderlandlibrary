package javassist.convert;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public class TransformCall extends Transformer {
   protected String classname;
   protected String methodname;
   protected String methodDescriptor;
   protected String newClassname;
   protected String newMethodname;
   protected boolean newMethodIsPrivate;
   protected int newIndex;
   protected ConstPool constPool;

   public TransformCall(Transformer var1, CtMethod var2, CtMethod var3) {
      this(var1, var2.getName(), var3);
      this.classname = var2.getDeclaringClass().getName();
   }

   public TransformCall(Transformer var1, String var2, CtMethod var3) {
      super(var1);
      this.methodname = var2;
      this.methodDescriptor = var3.getMethodInfo2().getDescriptor();
      this.classname = this.newClassname = var3.getDeclaringClass().getName();
      this.newMethodname = var3.getName();
      this.constPool = null;
      this.newMethodIsPrivate = Modifier.isPrivate(var3.getModifiers());
   }

   public void initialize(ConstPool var1, CodeAttribute var2) {
      if (this.constPool != var1) {
         this.newIndex = 0;
      }

   }

   public int transform(CtClass var1, int var2, CodeIterator var3, ConstPool var4) throws BadBytecode {
      int var5 = var3.byteAt(var2);
      if (var5 == 185 || var5 == 183 || var5 == 184 || var5 == 182) {
         int var6 = var3.u16bitAt(var2 + 1);
         String var7 = var4.eqMember(this.methodname, this.methodDescriptor, var6);
         if (var7 != null && var1.getClassPool() != false) {
            int var8 = var4.getMemberNameAndType(var6);
            var2 = this.match(var5, var2, var3, var4.getNameAndTypeDescriptor(var8), var4);
         }
      }

      return var2;
   }

   protected int match(int var1, int var2, CodeIterator var3, int var4, ConstPool var5) throws BadBytecode {
      if (this.newIndex == 0) {
         int var6 = var5.addNameAndTypeInfo(var5.addUtf8Info(this.newMethodname), var4);
         int var7 = var5.addClassInfo(this.newClassname);
         if (var1 == 185) {
            this.newIndex = var5.addInterfaceMethodrefInfo(var7, var6);
         } else {
            if (this.newMethodIsPrivate && var1 == 182) {
               var3.writeByte(183, var2);
            }

            this.newIndex = var5.addMethodrefInfo(var7, var6);
         }

         this.constPool = var5;
      }

      var3.write16bit(this.newIndex, var2 + 1);
      return var2;
   }
}
