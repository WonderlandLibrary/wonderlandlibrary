package javassist;

import java.util.ListIterator;
import java.util.Map;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.SignatureAttribute;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.compiler.SymbolTable;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.StringL;

public class CtField extends CtMember {
   static final String javaLangString = "java.lang.String";
   protected FieldInfo fieldInfo;

   public CtField(CtClass var1, String var2, CtClass var3) throws CannotCompileException {
      this(Descriptor.of(var1), var2, var3);
   }

   public CtField(CtField var1, CtClass var2) throws CannotCompileException {
      this(var1.fieldInfo.getDescriptor(), var1.fieldInfo.getName(), var2);
      ListIterator var3 = var1.fieldInfo.getAttributes().listIterator();
      FieldInfo var4 = this.fieldInfo;
      var4.setAccessFlags(var1.fieldInfo.getAccessFlags());
      ConstPool var5 = var4.getConstPool();

      while(var3.hasNext()) {
         AttributeInfo var6 = (AttributeInfo)var3.next();
         var4.addAttribute(var6.copy(var5, (Map)null));
      }

   }

   private CtField(String var1, String var2, CtClass var3) throws CannotCompileException {
      super(var3);
      ClassFile var4 = var3.getClassFile2();
      if (var4 == null) {
         throw new CannotCompileException("bad declaring class: " + var3.getName());
      } else {
         this.fieldInfo = new FieldInfo(var4.getConstPool(), var2, var1);
      }
   }

   CtField(FieldInfo var1, CtClass var2) {
      super(var2);
      this.fieldInfo = var1;
   }

   public String toString() {
      return this.getDeclaringClass().getName() + "." + this.getName() + ":" + this.fieldInfo.getDescriptor();
   }

   protected void extendToString(StringBuffer var1) {
      var1.append(' ');
      var1.append(this.getName());
      var1.append(' ');
      var1.append(this.fieldInfo.getDescriptor());
   }

   protected ASTree getInitAST() {
      return null;
   }

   CtField.Initializer getInit() {
      ASTree var1 = this.getInitAST();
      return var1 == null ? null : CtField.Initializer.byExpr(var1);
   }

   public static CtField make(String var0, CtClass var1) throws CannotCompileException {
      Javac var2 = new Javac(var1);

      try {
         CtMember var3 = var2.compile(var0);
         if (var3 instanceof CtField) {
            return (CtField)var3;
         }
      } catch (CompileError var4) {
         throw new CannotCompileException(var4);
      }

      throw new CannotCompileException("not a field");
   }

   public FieldInfo getFieldInfo() {
      this.declaringClass.checkModify();
      return this.fieldInfo;
   }

   public FieldInfo getFieldInfo2() {
      return this.fieldInfo;
   }

   public CtClass getDeclaringClass() {
      return super.getDeclaringClass();
   }

   public String getName() {
      return this.fieldInfo.getName();
   }

   public void setName(String var1) {
      this.declaringClass.checkModify();
      this.fieldInfo.setName(var1);
   }

   public int getModifiers() {
      return AccessFlag.toModifier(this.fieldInfo.getAccessFlags());
   }

   public void setModifiers(int var1) {
      this.declaringClass.checkModify();
      this.fieldInfo.setAccessFlags(AccessFlag.of(var1));
   }

   public boolean hasAnnotation(String var1) {
      FieldInfo var2 = this.getFieldInfo2();
      AnnotationsAttribute var3 = (AnnotationsAttribute)var2.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute var4 = (AnnotationsAttribute)var2.getAttribute("RuntimeVisibleAnnotations");
      return CtClassType.hasAnnotationType(var1, this.getDeclaringClass().getClassPool(), var3, var4);
   }

   public Object getAnnotation(Class var1) throws ClassNotFoundException {
      FieldInfo var2 = this.getFieldInfo2();
      AnnotationsAttribute var3 = (AnnotationsAttribute)var2.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute var4 = (AnnotationsAttribute)var2.getAttribute("RuntimeVisibleAnnotations");
      return CtClassType.getAnnotationType(var1, this.getDeclaringClass().getClassPool(), var3, var4);
   }

   public Object[] getAnnotations() throws ClassNotFoundException {
      return this.getAnnotations(false);
   }

   public Object[] getAvailableAnnotations() {
      try {
         return this.getAnnotations(true);
      } catch (ClassNotFoundException var2) {
         throw new RuntimeException("Unexpected exception", var2);
      }
   }

   private Object[] getAnnotations(boolean var1) throws ClassNotFoundException {
      FieldInfo var2 = this.getFieldInfo2();
      AnnotationsAttribute var3 = (AnnotationsAttribute)var2.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute var4 = (AnnotationsAttribute)var2.getAttribute("RuntimeVisibleAnnotations");
      return CtClassType.toAnnotationType(var1, this.getDeclaringClass().getClassPool(), var3, var4);
   }

   public String getSignature() {
      return this.fieldInfo.getDescriptor();
   }

   public String getGenericSignature() {
      SignatureAttribute var1 = (SignatureAttribute)this.fieldInfo.getAttribute("Signature");
      return var1 == null ? null : var1.getSignature();
   }

   public void setGenericSignature(String var1) {
      this.declaringClass.checkModify();
      this.fieldInfo.addAttribute(new SignatureAttribute(this.fieldInfo.getConstPool(), var1));
   }

   public CtClass getType() throws NotFoundException {
      return Descriptor.toCtClass(this.fieldInfo.getDescriptor(), this.declaringClass.getClassPool());
   }

   public void setType(CtClass var1) {
      this.declaringClass.checkModify();
      this.fieldInfo.setDescriptor(Descriptor.of(var1));
   }

   public Object getConstantValue() {
      int var1 = this.fieldInfo.getConstantValue();
      if (var1 == 0) {
         return null;
      } else {
         ConstPool var2 = this.fieldInfo.getConstPool();
         switch(var2.getTag(var1)) {
         case 3:
            int var3 = var2.getIntegerInfo(var1);
            if ("Z".equals(this.fieldInfo.getDescriptor())) {
               return new Boolean(var3 != 0);
            }

            return new Integer(var3);
         case 4:
            return new Float(var2.getFloatInfo(var1));
         case 5:
            return new Long(var2.getLongInfo(var1));
         case 6:
            return new Double(var2.getDoubleInfo(var1));
         case 7:
         default:
            throw new RuntimeException("bad tag: " + var2.getTag(var1) + " at " + var1);
         case 8:
            return var2.getStringInfo(var1);
         }
      }
   }

   public byte[] getAttribute(String var1) {
      AttributeInfo var2 = this.fieldInfo.getAttribute(var1);
      return var2 == null ? null : var2.get();
   }

   public void setAttribute(String var1, byte[] var2) {
      this.declaringClass.checkModify();
      this.fieldInfo.addAttribute(new AttributeInfo(this.fieldInfo.getConstPool(), var1, var2));
   }

   static class MultiArrayInitializer extends CtField.Initializer {
      CtClass type;
      int[] dim;

      MultiArrayInitializer(CtClass var1, int[] var2) {
         this.type = var1;
         this.dim = var2;
      }

      void check(String var1) throws CannotCompileException {
         if (var1.charAt(0) != '[') {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         int var6 = var3.addMultiNewarray(var1, this.dim);
         var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
         return var6 + 1;
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         int var5 = var3.addMultiNewarray(var1, this.dim);
         var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
         return var5;
      }
   }

   static class ArrayInitializer extends CtField.Initializer {
      CtClass type;
      int size;

      ArrayInitializer(CtClass var1, int var2) {
         this.type = var1;
         this.size = var2;
      }

      private void addNewarray(Bytecode var1) {
         if (this.type.isPrimitive()) {
            var1.addNewarray(((CtPrimitiveType)this.type).getArrayType(), this.size);
         } else {
            var1.addAnewarray(this.type, this.size);
         }

      }

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         this.addNewarray(var3);
         var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
         return 2;
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         this.addNewarray(var3);
         var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
         return 1;
      }
   }

   static class StringInitializer extends CtField.Initializer {
      String value;

      StringInitializer(String var1) {
         this.value = var1;
      }

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         var3.addLdc(this.value);
         var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
         return 2;
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         var3.addLdc(this.value);
         var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
         return 1;
      }

      int getConstantValue(ConstPool var1, CtClass var2) {
         return var2.getName().equals("java.lang.String") ? var1.addStringInfo(this.value) : 0;
      }
   }

   static class DoubleInitializer extends CtField.Initializer {
      double value;

      DoubleInitializer(double var1) {
         this.value = var1;
      }

      void check(String var1) throws CannotCompileException {
         if (!var1.equals("D")) {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         var3.addLdc2w(this.value);
         var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
         return 3;
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         var3.addLdc2w(this.value);
         var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
         return 2;
      }

      int getConstantValue(ConstPool var1, CtClass var2) {
         return var2 == CtClass.doubleType ? var1.addDoubleInfo(this.value) : 0;
      }
   }

   static class FloatInitializer extends CtField.Initializer {
      float value;

      FloatInitializer(float var1) {
         this.value = var1;
      }

      void check(String var1) throws CannotCompileException {
         if (!var1.equals("F")) {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         var3.addFconst(this.value);
         var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
         return 3;
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         var3.addFconst(this.value);
         var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
         return 2;
      }

      int getConstantValue(ConstPool var1, CtClass var2) {
         return var2 == CtClass.floatType ? var1.addFloatInfo(this.value) : 0;
      }
   }

   static class LongInitializer extends CtField.Initializer {
      long value;

      LongInitializer(long var1) {
         this.value = var1;
      }

      void check(String var1) throws CannotCompileException {
         if (!var1.equals("J")) {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         var3.addLdc2w(this.value);
         var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
         return 3;
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         var3.addLdc2w(this.value);
         var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
         return 2;
      }

      int getConstantValue(ConstPool var1, CtClass var2) {
         return var2 == CtClass.longType ? var1.addLongInfo(this.value) : 0;
      }
   }

   static class IntInitializer extends CtField.Initializer {
      int value;

      IntInitializer(int var1) {
         this.value = var1;
      }

      void check(String var1) throws CannotCompileException {
         char var2 = var1.charAt(0);
         if (var2 != 'I' && var2 != 'S' && var2 != 'B' && var2 != 'C' && var2 != 'Z') {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         var3.addIconst(this.value);
         var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
         return 2;
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         var3.addIconst(this.value);
         var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
         return 1;
      }

      int getConstantValue(ConstPool var1, CtClass var2) {
         return var1.addIntegerInfo(this.value);
      }
   }

   static class MethodInitializer extends CtField.NewInitializer {
      String methodName;

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         var3.addAload(0);
         int var6;
         if (this.stringParams == null) {
            var6 = 2;
         } else {
            var6 = this.compileStringParameter(var3) + 2;
         }

         if (this.withConstructorParams) {
            var6 += CtNewWrappedMethod.compileParameterList(var3, var4, 1);
         }

         String var7 = Descriptor.of(var1);
         String var8 = this.getDescriptor() + var7;
         var3.addInvokestatic(this.objectType, this.methodName, var8);
         var3.addPutfield(Bytecode.THIS, var2, var7);
         return var6;
      }

      private String getDescriptor() {
         String var1 = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
         if (this.stringParams == null) {
            return this.withConstructorParams ? "(Ljava/lang/Object;[Ljava/lang/Object;)" : "(Ljava/lang/Object;)";
         } else {
            return this.withConstructorParams ? "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)" : "(Ljava/lang/Object;[Ljava/lang/String;)";
         }
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         int var6 = 1;
         String var5;
         if (this.stringParams == null) {
            var5 = "()";
         } else {
            var5 = "([Ljava/lang/String;)";
            var6 += this.compileStringParameter(var3);
         }

         String var7 = Descriptor.of(var1);
         var3.addInvokestatic(this.objectType, this.methodName, var5 + var7);
         var3.addPutstatic(Bytecode.THIS, var2, var7);
         return var6;
      }
   }

   static class NewInitializer extends CtField.Initializer {
      CtClass objectType;
      String[] stringParams;
      boolean withConstructorParams;

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         var3.addAload(0);
         var3.addNew(this.objectType);
         var3.add(89);
         var3.addAload(0);
         int var6;
         if (this.stringParams == null) {
            var6 = 4;
         } else {
            var6 = this.compileStringParameter(var3) + 4;
         }

         if (this.withConstructorParams) {
            var6 += CtNewWrappedMethod.compileParameterList(var3, var4, 1);
         }

         var3.addInvokespecial(this.objectType, "<init>", this.getDescriptor());
         var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
         return var6;
      }

      private String getDescriptor() {
         String var1 = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
         if (this.stringParams == null) {
            return this.withConstructorParams ? "(Ljava/lang/Object;[Ljava/lang/Object;)V" : "(Ljava/lang/Object;)V";
         } else {
            return this.withConstructorParams ? "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V" : "(Ljava/lang/Object;[Ljava/lang/String;)V";
         }
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         var3.addNew(this.objectType);
         var3.add(89);
         int var6 = 2;
         String var5;
         if (this.stringParams == null) {
            var5 = "()V";
         } else {
            var5 = "([Ljava/lang/String;)V";
            var6 += this.compileStringParameter(var3);
         }

         var3.addInvokespecial(this.objectType, "<init>", var5);
         var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
         return var6;
      }

      protected final int compileStringParameter(Bytecode var1) throws CannotCompileException {
         int var2 = this.stringParams.length;
         var1.addIconst(var2);
         var1.addAnewarray("java.lang.String");

         for(int var3 = 0; var3 < var2; ++var3) {
            var1.add(89);
            var1.addIconst(var3);
            var1.addLdc(this.stringParams[var3]);
            var1.add(83);
         }

         return 4;
      }
   }

   static class ParamInitializer extends CtField.Initializer {
      int nthParam;

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         if (var4 != null && this.nthParam < var4.length) {
            var3.addAload(0);
            int var6 = nthParamToLocal(this.nthParam, var4, false);
            int var7 = var3.addLoad(var6, var1) + 1;
            var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
            return var7;
         } else {
            return 0;
         }
      }

      static int nthParamToLocal(int var0, CtClass[] var1, boolean var2) {
         CtClass var3 = CtClass.longType;
         CtClass var4 = CtClass.doubleType;
         int var5;
         if (var2) {
            var5 = 0;
         } else {
            var5 = 1;
         }

         for(int var6 = 0; var6 < var0; ++var6) {
            CtClass var7 = var1[var6];
            if (var7 != var3 && var7 != var4) {
               ++var5;
            } else {
               var5 += 2;
            }
         }

         return var5;
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         return 0;
      }
   }

   static class PtreeInitializer extends CtField.CodeInitializer0 {
      private ASTree expression;

      PtreeInitializer(ASTree var1) {
         this.expression = var1;
      }

      void compileExpr(Javac var1) throws CompileError {
         var1.compileExpr(this.expression);
      }

      int getConstantValue(ConstPool var1, CtClass var2) {
         return this.getConstantValue2(var1, var2, this.expression);
      }
   }

   static class CodeInitializer extends CtField.CodeInitializer0 {
      private String expression;

      CodeInitializer(String var1) {
         this.expression = var1;
      }

      void compileExpr(Javac var1) throws CompileError {
         var1.compileExpr(this.expression);
      }

      int getConstantValue(ConstPool var1, CtClass var2) {
         try {
            ASTree var3 = Javac.parseExpr(this.expression, new SymbolTable());
            return this.getConstantValue2(var1, var2, var3);
         } catch (CompileError var4) {
            return 0;
         }
      }
   }

   abstract static class CodeInitializer0 extends CtField.Initializer {
      abstract void compileExpr(Javac var1) throws CompileError;

      int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException {
         try {
            var3.addAload(0);
            this.compileExpr(var5);
            var3.addPutfield(Bytecode.THIS, var2, Descriptor.of(var1));
            return var3.getMaxStack();
         } catch (CompileError var7) {
            throw new CannotCompileException(var7);
         }
      }

      int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException {
         try {
            this.compileExpr(var4);
            var3.addPutstatic(Bytecode.THIS, var2, Descriptor.of(var1));
            return var3.getMaxStack();
         } catch (CompileError var6) {
            throw new CannotCompileException(var6);
         }
      }

      int getConstantValue2(ConstPool var1, CtClass var2, ASTree var3) {
         if (var2.isPrimitive()) {
            if (var3 instanceof IntConst) {
               long var4 = ((IntConst)var3).get();
               if (var2 == CtClass.doubleType) {
                  return var1.addDoubleInfo((double)var4);
               }

               if (var2 == CtClass.floatType) {
                  return var1.addFloatInfo((float)var4);
               }

               if (var2 == CtClass.longType) {
                  return var1.addLongInfo(var4);
               }

               if (var2 != CtClass.voidType) {
                  return var1.addIntegerInfo((int)var4);
               }
            } else if (var3 instanceof DoubleConst) {
               double var6 = ((DoubleConst)var3).get();
               if (var2 == CtClass.floatType) {
                  return var1.addFloatInfo((float)var6);
               }

               if (var2 == CtClass.doubleType) {
                  return var1.addDoubleInfo(var6);
               }
            }
         } else if (var3 instanceof StringL && var2.getName().equals("java.lang.String")) {
            return var1.addStringInfo(((StringL)var3).get());
         }

         return 0;
      }
   }

   public abstract static class Initializer {
      public static CtField.Initializer constant(int var0) {
         return new CtField.IntInitializer(var0);
      }

      public static CtField.Initializer constant(boolean var0) {
         return new CtField.IntInitializer(var0 ? 1 : 0);
      }

      public static CtField.Initializer constant(long var0) {
         return new CtField.LongInitializer(var0);
      }

      public static CtField.Initializer constant(float var0) {
         return new CtField.FloatInitializer(var0);
      }

      public static CtField.Initializer constant(double var0) {
         return new CtField.DoubleInitializer(var0);
      }

      public static CtField.Initializer constant(String var0) {
         return new CtField.StringInitializer(var0);
      }

      public static CtField.Initializer byParameter(int var0) {
         CtField.ParamInitializer var1 = new CtField.ParamInitializer();
         var1.nthParam = var0;
         return var1;
      }

      public static CtField.Initializer byNew(CtClass var0) {
         CtField.NewInitializer var1 = new CtField.NewInitializer();
         var1.objectType = var0;
         var1.stringParams = null;
         var1.withConstructorParams = false;
         return var1;
      }

      public static CtField.Initializer byNew(CtClass var0, String[] var1) {
         CtField.NewInitializer var2 = new CtField.NewInitializer();
         var2.objectType = var0;
         var2.stringParams = var1;
         var2.withConstructorParams = false;
         return var2;
      }

      public static CtField.Initializer byNewWithParams(CtClass var0) {
         CtField.NewInitializer var1 = new CtField.NewInitializer();
         var1.objectType = var0;
         var1.stringParams = null;
         var1.withConstructorParams = true;
         return var1;
      }

      public static CtField.Initializer byNewWithParams(CtClass var0, String[] var1) {
         CtField.NewInitializer var2 = new CtField.NewInitializer();
         var2.objectType = var0;
         var2.stringParams = var1;
         var2.withConstructorParams = true;
         return var2;
      }

      public static CtField.Initializer byCall(CtClass var0, String var1) {
         CtField.MethodInitializer var2 = new CtField.MethodInitializer();
         var2.objectType = var0;
         var2.methodName = var1;
         var2.stringParams = null;
         var2.withConstructorParams = false;
         return var2;
      }

      public static CtField.Initializer byCall(CtClass var0, String var1, String[] var2) {
         CtField.MethodInitializer var3 = new CtField.MethodInitializer();
         var3.objectType = var0;
         var3.methodName = var1;
         var3.stringParams = var2;
         var3.withConstructorParams = false;
         return var3;
      }

      public static CtField.Initializer byCallWithParams(CtClass var0, String var1) {
         CtField.MethodInitializer var2 = new CtField.MethodInitializer();
         var2.objectType = var0;
         var2.methodName = var1;
         var2.stringParams = null;
         var2.withConstructorParams = true;
         return var2;
      }

      public static CtField.Initializer byCallWithParams(CtClass var0, String var1, String[] var2) {
         CtField.MethodInitializer var3 = new CtField.MethodInitializer();
         var3.objectType = var0;
         var3.methodName = var1;
         var3.stringParams = var2;
         var3.withConstructorParams = true;
         return var3;
      }

      public static CtField.Initializer byNewArray(CtClass var0, int var1) throws NotFoundException {
         return new CtField.ArrayInitializer(var0.getComponentType(), var1);
      }

      public static CtField.Initializer byNewArray(CtClass var0, int[] var1) {
         return new CtField.MultiArrayInitializer(var0, var1);
      }

      public static CtField.Initializer byExpr(String var0) {
         return new CtField.CodeInitializer(var0);
      }

      static CtField.Initializer byExpr(ASTree var0) {
         return new CtField.PtreeInitializer(var0);
      }

      void check(String var1) throws CannotCompileException {
      }

      abstract int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5) throws CannotCompileException;

      abstract int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4) throws CannotCompileException;

      int getConstantValue(ConstPool var1, CtClass var2) {
         return 0;
      }
   }
}
