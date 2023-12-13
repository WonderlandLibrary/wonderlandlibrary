package javassist;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.convert.TransformAccessArrayField;
import javassist.convert.TransformAfter;
import javassist.convert.TransformBefore;
import javassist.convert.TransformCall;
import javassist.convert.TransformFieldAccess;
import javassist.convert.TransformNew;
import javassist.convert.TransformNewClass;
import javassist.convert.TransformReadField;
import javassist.convert.TransformWriteField;
import javassist.convert.Transformer;

public class CodeConverter {
   protected Transformer transformers = null;

   public void replaceNew(CtClass var1, CtClass var2, String var3) {
      this.transformers = new TransformNew(this.transformers, var1.getName(), var2.getName(), var3);
   }

   public void replaceNew(CtClass var1, CtClass var2) {
      this.transformers = new TransformNewClass(this.transformers, var1.getName(), var2.getName());
   }

   public void redirectFieldAccess(CtField var1, CtClass var2, String var3) {
      this.transformers = new TransformFieldAccess(this.transformers, var1, var2.getName(), var3);
   }

   public void replaceFieldRead(CtField var1, CtClass var2, String var3) {
      this.transformers = new TransformReadField(this.transformers, var1, var2.getName(), var3);
   }

   public void replaceFieldWrite(CtField var1, CtClass var2, String var3) {
      this.transformers = new TransformWriteField(this.transformers, var1, var2.getName(), var3);
   }

   public void replaceArrayAccess(CtClass var1, CodeConverter.ArrayAccessReplacementMethodNames var2) throws NotFoundException {
      this.transformers = new TransformAccessArrayField(this.transformers, var1.getName(), var2);
   }

   public void redirectMethodCall(CtMethod var1, CtMethod var2) throws CannotCompileException {
      String var3 = var1.getMethodInfo2().getDescriptor();
      String var4 = var2.getMethodInfo2().getDescriptor();
      if (!var3.equals(var4)) {
         throw new CannotCompileException("signature mismatch: " + var2.getLongName());
      } else {
         int var5 = var1.getModifiers();
         int var6 = var2.getModifiers();
         if (Modifier.isStatic(var5) == Modifier.isStatic(var6) && (!Modifier.isPrivate(var5) || Modifier.isPrivate(var6)) && var1.getDeclaringClass().isInterface() == var2.getDeclaringClass().isInterface()) {
            this.transformers = new TransformCall(this.transformers, var1, var2);
         } else {
            throw new CannotCompileException("invoke-type mismatch " + var2.getLongName());
         }
      }
   }

   public void redirectMethodCall(String var1, CtMethod var2) throws CannotCompileException {
      this.transformers = new TransformCall(this.transformers, var1, var2);
   }

   public void insertBeforeMethod(CtMethod var1, CtMethod var2) throws CannotCompileException {
      try {
         this.transformers = new TransformBefore(this.transformers, var1, var2);
      } catch (NotFoundException var4) {
         throw new CannotCompileException(var4);
      }
   }

   public void insertAfterMethod(CtMethod var1, CtMethod var2) throws CannotCompileException {
      try {
         this.transformers = new TransformAfter(this.transformers, var1, var2);
      } catch (NotFoundException var4) {
         throw new CannotCompileException(var4);
      }
   }

   protected void doit(CtClass var1, MethodInfo var2, ConstPool var3) throws CannotCompileException {
      CodeAttribute var5 = var2.getCodeAttribute();
      if (var5 != null && this.transformers != null) {
         Transformer var4;
         for(var4 = this.transformers; var4 != null; var4 = var4.getNext()) {
            var4.initialize(var3, var1, var2);
         }

         CodeIterator var6 = var5.iterator();

         int var7;
         while(var6.hasNext()) {
            try {
               var7 = var6.next();

               for(var4 = this.transformers; var4 != null; var4 = var4.getNext()) {
                  var7 = var4.transform(var1, var7, var6, var3);
               }
            } catch (BadBytecode var11) {
               throw new CannotCompileException(var11);
            }
         }

         var7 = 0;
         int var8 = 0;

         for(var4 = this.transformers; var4 != null; var4 = var4.getNext()) {
            int var9 = var4.extraLocals();
            if (var9 > var7) {
               var7 = var9;
            }

            var9 = var4.extraStack();
            if (var9 > var8) {
               var8 = var9;
            }
         }

         for(var4 = this.transformers; var4 != null; var4 = var4.getNext()) {
            var4.clean();
         }

         if (var7 > 0) {
            var5.setMaxLocals(var5.getMaxLocals() + var7);
         }

         if (var8 > 0) {
            var5.setMaxStack(var5.getMaxStack() + var8);
         }

         try {
            var2.rebuildStackMapIf6(var1.getClassPool(), var1.getClassFile2());
         } catch (BadBytecode var10) {
            throw new CannotCompileException(var10.getMessage(), var10);
         }
      }
   }

   public static class DefaultArrayAccessReplacementMethodNames implements CodeConverter.ArrayAccessReplacementMethodNames {
      public String byteOrBooleanRead() {
         return "arrayReadByteOrBoolean";
      }

      public String byteOrBooleanWrite() {
         return "arrayWriteByteOrBoolean";
      }

      public String charRead() {
         return "arrayReadChar";
      }

      public String charWrite() {
         return "arrayWriteChar";
      }

      public String doubleRead() {
         return "arrayReadDouble";
      }

      public String doubleWrite() {
         return "arrayWriteDouble";
      }

      public String floatRead() {
         return "arrayReadFloat";
      }

      public String floatWrite() {
         return "arrayWriteFloat";
      }

      public String intRead() {
         return "arrayReadInt";
      }

      public String intWrite() {
         return "arrayWriteInt";
      }

      public String longRead() {
         return "arrayReadLong";
      }

      public String longWrite() {
         return "arrayWriteLong";
      }

      public String objectRead() {
         return "arrayReadObject";
      }

      public String objectWrite() {
         return "arrayWriteObject";
      }

      public String shortRead() {
         return "arrayReadShort";
      }

      public String shortWrite() {
         return "arrayWriteShort";
      }
   }

   public interface ArrayAccessReplacementMethodNames {
      String byteOrBooleanRead();

      String byteOrBooleanWrite();

      String charRead();

      String charWrite();

      String doubleRead();

      String doubleWrite();

      String floatRead();

      String floatWrite();

      String intRead();

      String intWrite();

      String longRead();

      String longWrite();

      String objectRead();

      String objectWrite();

      String shortRead();

      String shortWrite();
   }
}
