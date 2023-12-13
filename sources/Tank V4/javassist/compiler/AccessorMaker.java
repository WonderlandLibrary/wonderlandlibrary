package javassist.compiler;

import java.util.HashMap;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.SyntheticAttribute;

public class AccessorMaker {
   private CtClass clazz;
   private int uniqueNumber;
   private HashMap accessors;
   static final String lastParamType = "javassist.runtime.Inner";

   public AccessorMaker(CtClass var1) {
      this.clazz = var1;
      this.uniqueNumber = 1;
      this.accessors = new HashMap();
   }

   public String getConstructor(CtClass var1, String var2, MethodInfo var3) throws CompileError {
      String var4 = "<init>:" + var2;
      String var5 = (String)this.accessors.get(var4);
      if (var5 != null) {
         return var5;
      } else {
         var5 = Descriptor.appendParameter("javassist.runtime.Inner", var2);
         ClassFile var6 = this.clazz.getClassFile();

         try {
            ConstPool var7 = var6.getConstPool();
            ClassPool var8 = this.clazz.getClassPool();
            MethodInfo var9 = new MethodInfo(var7, "<init>", var5);
            var9.setAccessFlags(0);
            var9.addAttribute(new SyntheticAttribute(var7));
            ExceptionsAttribute var10 = var3.getExceptionsAttribute();
            if (var10 != null) {
               var9.addAttribute(var10.copy(var7, (Map)null));
            }

            CtClass[] var11 = Descriptor.getParameterTypes(var2, var8);
            Bytecode var12 = new Bytecode(var7);
            var12.addAload(0);
            int var13 = 1;
            int var14 = 0;

            while(true) {
               if (var14 >= var11.length) {
                  var12.setMaxLocals(var13 + 1);
                  var12.addInvokespecial(this.clazz, "<init>", var2);
                  var12.addReturn((CtClass)null);
                  var9.setCodeAttribute(var12.toCodeAttribute());
                  var6.addMethod(var9);
                  break;
               }

               var13 += var12.addLoad(var13, var11[var14]);
               ++var14;
            }
         } catch (CannotCompileException var15) {
            throw new CompileError(var15);
         } catch (NotFoundException var16) {
            throw new CompileError(var16);
         }

         this.accessors.put(var4, var5);
         return var5;
      }
   }

   public String getMethodAccessor(String var1, String var2, String var3, MethodInfo var4) throws CompileError {
      String var5 = var1 + ":" + var2;
      String var6 = (String)this.accessors.get(var5);
      if (var6 != null) {
         return var6;
      } else {
         ClassFile var7 = this.clazz.getClassFile();
         var6 = this.findAccessorName(var7);

         try {
            ConstPool var8 = var7.getConstPool();
            ClassPool var9 = this.clazz.getClassPool();
            MethodInfo var10 = new MethodInfo(var8, var6, var3);
            var10.setAccessFlags(8);
            var10.addAttribute(new SyntheticAttribute(var8));
            ExceptionsAttribute var11 = var4.getExceptionsAttribute();
            if (var11 != null) {
               var10.addAttribute(var11.copy(var8, (Map)null));
            }

            CtClass[] var12 = Descriptor.getParameterTypes(var3, var9);
            int var13 = 0;
            Bytecode var14 = new Bytecode(var8);

            for(int var15 = 0; var15 < var12.length; ++var15) {
               var13 += var14.addLoad(var13, var12[var15]);
            }

            var14.setMaxLocals(var13);
            if (var2 == var3) {
               var14.addInvokestatic(this.clazz, var1, var2);
            } else {
               var14.addInvokevirtual(this.clazz, var1, var2);
            }

            var14.addReturn(Descriptor.getReturnType(var2, var9));
            var10.setCodeAttribute(var14.toCodeAttribute());
            var7.addMethod(var10);
         } catch (CannotCompileException var16) {
            throw new CompileError(var16);
         } catch (NotFoundException var17) {
            throw new CompileError(var17);
         }

         this.accessors.put(var5, var6);
         return var6;
      }
   }

   public MethodInfo getFieldGetter(FieldInfo var1, boolean var2) throws CompileError {
      String var3 = var1.getName();
      String var4 = var3 + ":getter";
      Object var5 = this.accessors.get(var4);
      if (var5 != null) {
         return (MethodInfo)var5;
      } else {
         ClassFile var6 = this.clazz.getClassFile();
         String var7 = this.findAccessorName(var6);

         try {
            ConstPool var8 = var6.getConstPool();
            ClassPool var9 = this.clazz.getClassPool();
            String var10 = var1.getDescriptor();
            String var11;
            if (var2) {
               var11 = "()" + var10;
            } else {
               var11 = "(" + Descriptor.of(this.clazz) + ")" + var10;
            }

            MethodInfo var12 = new MethodInfo(var8, var7, var11);
            var12.setAccessFlags(8);
            var12.addAttribute(new SyntheticAttribute(var8));
            Bytecode var13 = new Bytecode(var8);
            if (var2) {
               var13.addGetstatic(Bytecode.THIS, var3, var10);
            } else {
               var13.addAload(0);
               var13.addGetfield(Bytecode.THIS, var3, var10);
               var13.setMaxLocals(1);
            }

            var13.addReturn(Descriptor.toCtClass(var10, var9));
            var12.setCodeAttribute(var13.toCodeAttribute());
            var6.addMethod(var12);
            this.accessors.put(var4, var12);
            return var12;
         } catch (CannotCompileException var14) {
            throw new CompileError(var14);
         } catch (NotFoundException var15) {
            throw new CompileError(var15);
         }
      }
   }

   public MethodInfo getFieldSetter(FieldInfo var1, boolean var2) throws CompileError {
      String var3 = var1.getName();
      String var4 = var3 + ":setter";
      Object var5 = this.accessors.get(var4);
      if (var5 != null) {
         return (MethodInfo)var5;
      } else {
         ClassFile var6 = this.clazz.getClassFile();
         String var7 = this.findAccessorName(var6);

         try {
            ConstPool var8 = var6.getConstPool();
            ClassPool var9 = this.clazz.getClassPool();
            String var10 = var1.getDescriptor();
            String var11;
            if (var2) {
               var11 = "(" + var10 + ")V";
            } else {
               var11 = "(" + Descriptor.of(this.clazz) + var10 + ")V";
            }

            MethodInfo var12 = new MethodInfo(var8, var7, var11);
            var12.setAccessFlags(8);
            var12.addAttribute(new SyntheticAttribute(var8));
            Bytecode var13 = new Bytecode(var8);
            int var14;
            if (var2) {
               var14 = var13.addLoad(0, Descriptor.toCtClass(var10, var9));
               var13.addPutstatic(Bytecode.THIS, var3, var10);
            } else {
               var13.addAload(0);
               var14 = var13.addLoad(1, Descriptor.toCtClass(var10, var9)) + 1;
               var13.addPutfield(Bytecode.THIS, var3, var10);
            }

            var13.addReturn((CtClass)null);
            var13.setMaxLocals(var14);
            var12.setCodeAttribute(var13.toCodeAttribute());
            var6.addMethod(var12);
            this.accessors.put(var4, var12);
            return var12;
         } catch (CannotCompileException var15) {
            throw new CompileError(var15);
         } catch (NotFoundException var16) {
            throw new CompileError(var16);
         }
      }
   }

   private String findAccessorName(ClassFile var1) {
      String var2;
      do {
         var2 = "access$" + this.uniqueNumber++;
      } while(var1.getMethod(var2) != null);

      return var2;
   }
}
