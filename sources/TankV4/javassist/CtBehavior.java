package javassist;

import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.LocalVariableTypeAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.StackMap;
import javassist.bytecode.StackMapTable;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.expr.ExprEditor;

public abstract class CtBehavior extends CtMember {
   protected MethodInfo methodInfo;

   protected CtBehavior(CtClass var1, MethodInfo var2) {
      super(var1);
      this.methodInfo = var2;
   }

   void copy(CtBehavior var1, boolean var2, ClassMap var3) throws CannotCompileException {
      CtClass var4 = this.declaringClass;
      MethodInfo var5 = var1.methodInfo;
      CtClass var6 = var1.getDeclaringClass();
      ConstPool var7 = var4.getClassFile2().getConstPool();
      var3 = new ClassMap(var3);
      var3.put(var6.getName(), var4.getName());

      try {
         boolean var8 = false;
         CtClass var9 = var6.getSuperclass();
         CtClass var10 = var4.getSuperclass();
         String var11 = null;
         if (var9 != null && var10 != null) {
            String var12 = var9.getName();
            var11 = var10.getName();
            if (!var12.equals(var11)) {
               if (var12.equals("java.lang.Object")) {
                  var8 = true;
               } else {
                  var3.putIfNone(var12, var11);
               }
            }
         }

         this.methodInfo = new MethodInfo(var7, var5.getName(), var5, var3);
         if (var2 && var8) {
            this.methodInfo.setSuperclass(var11);
         }

      } catch (NotFoundException var13) {
         throw new CannotCompileException(var13);
      } catch (BadBytecode var14) {
         throw new CannotCompileException(var14);
      }
   }

   protected void extendToString(StringBuffer var1) {
      var1.append(' ');
      var1.append(this.getName());
      var1.append(' ');
      var1.append(this.methodInfo.getDescriptor());
   }

   public abstract String getLongName();

   public MethodInfo getMethodInfo() {
      this.declaringClass.checkModify();
      return this.methodInfo;
   }

   public MethodInfo getMethodInfo2() {
      return this.methodInfo;
   }

   public int getModifiers() {
      return AccessFlag.toModifier(this.methodInfo.getAccessFlags());
   }

   public void setModifiers(int var1) {
      this.declaringClass.checkModify();
      this.methodInfo.setAccessFlags(AccessFlag.of(var1));
   }

   public boolean hasAnnotation(String var1) {
      MethodInfo var2 = this.getMethodInfo2();
      AnnotationsAttribute var3 = (AnnotationsAttribute)var2.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute var4 = (AnnotationsAttribute)var2.getAttribute("RuntimeVisibleAnnotations");
      return CtClassType.hasAnnotationType(var1, this.getDeclaringClass().getClassPool(), var3, var4);
   }

   public Object getAnnotation(Class var1) throws ClassNotFoundException {
      MethodInfo var2 = this.getMethodInfo2();
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
      MethodInfo var2 = this.getMethodInfo2();
      AnnotationsAttribute var3 = (AnnotationsAttribute)var2.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute var4 = (AnnotationsAttribute)var2.getAttribute("RuntimeVisibleAnnotations");
      return CtClassType.toAnnotationType(var1, this.getDeclaringClass().getClassPool(), var3, var4);
   }

   public Object[][] getParameterAnnotations() throws ClassNotFoundException {
      return this.getParameterAnnotations(false);
   }

   public Object[][] getAvailableParameterAnnotations() {
      try {
         return this.getParameterAnnotations(true);
      } catch (ClassNotFoundException var2) {
         throw new RuntimeException("Unexpected exception", var2);
      }
   }

   Object[][] getParameterAnnotations(boolean var1) throws ClassNotFoundException {
      MethodInfo var2 = this.getMethodInfo2();
      ParameterAnnotationsAttribute var3 = (ParameterAnnotationsAttribute)var2.getAttribute("RuntimeInvisibleParameterAnnotations");
      ParameterAnnotationsAttribute var4 = (ParameterAnnotationsAttribute)var2.getAttribute("RuntimeVisibleParameterAnnotations");
      return CtClassType.toAnnotationType(var1, this.getDeclaringClass().getClassPool(), var3, var4, var2);
   }

   public CtClass[] getParameterTypes() throws NotFoundException {
      return Descriptor.getParameterTypes(this.methodInfo.getDescriptor(), this.declaringClass.getClassPool());
   }

   CtClass getReturnType0() throws NotFoundException {
      return Descriptor.getReturnType(this.methodInfo.getDescriptor(), this.declaringClass.getClassPool());
   }

   public String getSignature() {
      return this.methodInfo.getDescriptor();
   }

   public String getGenericSignature() {
      SignatureAttribute var1 = (SignatureAttribute)this.methodInfo.getAttribute("Signature");
      return var1 == null ? null : var1.getSignature();
   }

   public void setGenericSignature(String var1) {
      this.declaringClass.checkModify();
      this.methodInfo.addAttribute(new SignatureAttribute(this.methodInfo.getConstPool(), var1));
   }

   public CtClass[] getExceptionTypes() throws NotFoundException {
      ExceptionsAttribute var2 = this.methodInfo.getExceptionsAttribute();
      String[] var1;
      if (var2 == null) {
         var1 = null;
      } else {
         var1 = var2.getExceptions();
      }

      return this.declaringClass.getClassPool().get(var1);
   }

   public void setExceptionTypes(CtClass[] var1) throws NotFoundException {
      this.declaringClass.checkModify();
      if (var1 != null && var1.length != 0) {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].getName();
         }

         ExceptionsAttribute var4 = this.methodInfo.getExceptionsAttribute();
         if (var4 == null) {
            var4 = new ExceptionsAttribute(this.methodInfo.getConstPool());
            this.methodInfo.setExceptionsAttribute(var4);
         }

         var4.setExceptions(var2);
      } else {
         this.methodInfo.removeExceptionsAttribute();
      }
   }

   public abstract boolean isEmpty();

   public void setBody(String var1) throws CannotCompileException {
      this.setBody(var1, (String)null, (String)null);
   }

   public void setBody(String var1, String var2, String var3) throws CannotCompileException {
      CtClass var4 = this.declaringClass;
      var4.checkModify();

      try {
         Javac var5 = new Javac(var4);
         if (var3 != null) {
            var5.recordProceed(var2, var3);
         }

         Bytecode var6 = var5.compileBody(this, var1);
         this.methodInfo.setCodeAttribute(var6.toCodeAttribute());
         this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & -1025);
         this.methodInfo.rebuildStackMapIf6(var4.getClassPool(), var4.getClassFile2());
         this.declaringClass.rebuildClassFile();
      } catch (CompileError var7) {
         throw new CannotCompileException(var7);
      } catch (BadBytecode var8) {
         throw new CannotCompileException(var8);
      }
   }

   static void setBody0(CtClass var0, MethodInfo var1, CtClass var2, MethodInfo var3, ClassMap var4) throws CannotCompileException {
      var2.checkModify();
      var4 = new ClassMap(var4);
      var4.put(var0.getName(), var2.getName());

      try {
         CodeAttribute var5 = var1.getCodeAttribute();
         if (var5 != null) {
            ConstPool var6 = var3.getConstPool();
            CodeAttribute var7 = (CodeAttribute)var5.copy(var6, var4);
            var3.setCodeAttribute(var7);
         }
      } catch (CodeAttribute.RuntimeCopyException var8) {
         throw new CannotCompileException(var8);
      }

      var3.setAccessFlags(var3.getAccessFlags() & -1025);
      var2.rebuildClassFile();
   }

   public byte[] getAttribute(String var1) {
      AttributeInfo var2 = this.methodInfo.getAttribute(var1);
      return var2 == null ? null : var2.get();
   }

   public void setAttribute(String var1, byte[] var2) {
      this.declaringClass.checkModify();
      this.methodInfo.addAttribute(new AttributeInfo(this.methodInfo.getConstPool(), var1, var2));
   }

   public void useCflow(String var1) throws CannotCompileException {
      CtClass var2 = this.declaringClass;
      var2.checkModify();
      ClassPool var3 = var2.getClassPool();
      int var5 = 0;

      while(true) {
         String var4 = "_cflow$" + var5++;

         try {
            var2.getDeclaredField(var4);
         } catch (NotFoundException var10) {
            var3.recordCflow(var1, this.declaringClass.getName(), var4);

            try {
               CtClass var6 = var3.get("javassist.runtime.Cflow");
               CtField var7 = new CtField(var6, var4, var2);
               var7.setModifiers(9);
               var2.addField(var7, CtField.Initializer.byNew(var6));
               this.insertBefore(var4 + ".enter();", false);
               String var8 = var4 + ".exit();";
               this.insertAfter(var8, true);
               return;
            } catch (NotFoundException var9) {
               throw new CannotCompileException(var9);
            }
         }
      }
   }

   public void addLocalVariable(String var1, CtClass var2) throws CannotCompileException {
      this.declaringClass.checkModify();
      ConstPool var3 = this.methodInfo.getConstPool();
      CodeAttribute var4 = this.methodInfo.getCodeAttribute();
      if (var4 == null) {
         throw new CannotCompileException("no method body");
      } else {
         LocalVariableAttribute var5 = (LocalVariableAttribute)var4.getAttribute("LocalVariableTable");
         if (var5 == null) {
            var5 = new LocalVariableAttribute(var3);
            var4.getAttributes().add(var5);
         }

         int var6 = var4.getMaxLocals();
         String var7 = Descriptor.of(var2);
         var5.addEntry(0, var4.getCodeLength(), var3.addUtf8Info(var1), var3.addUtf8Info(var7), var6);
         var4.setMaxLocals(var6 + Descriptor.dataSize(var7));
      }
   }

   public void insertParameter(CtClass var1) throws CannotCompileException {
      this.declaringClass.checkModify();
      String var2 = this.methodInfo.getDescriptor();
      String var3 = Descriptor.insertParameter(var1, var2);

      try {
         this.addParameter2(Modifier.isStatic(this.getModifiers()) ? 0 : 1, var1, var2);
      } catch (BadBytecode var5) {
         throw new CannotCompileException(var5);
      }

      this.methodInfo.setDescriptor(var3);
   }

   public void addParameter(CtClass var1) throws CannotCompileException {
      this.declaringClass.checkModify();
      String var2 = this.methodInfo.getDescriptor();
      String var3 = Descriptor.appendParameter(var1, var2);
      int var4 = Modifier.isStatic(this.getModifiers()) ? 0 : 1;

      try {
         this.addParameter2(var4 + Descriptor.paramSize(var2), var1, var2);
      } catch (BadBytecode var6) {
         throw new CannotCompileException(var6);
      }

      this.methodInfo.setDescriptor(var3);
   }

   private void addParameter2(int var1, CtClass var2, String var3) throws BadBytecode {
      CodeAttribute var4 = this.methodInfo.getCodeAttribute();
      if (var4 != null) {
         int var5 = 1;
         char var6 = 'L';
         int var7 = 0;
         if (var2.isPrimitive()) {
            CtPrimitiveType var8 = (CtPrimitiveType)var2;
            var5 = var8.getDataSize();
            var6 = var8.getDescriptor();
         } else {
            var7 = this.methodInfo.getConstPool().addClassInfo(var2);
         }

         var4.insertLocalVar(var1, var5);
         LocalVariableAttribute var12 = (LocalVariableAttribute)var4.getAttribute("LocalVariableTable");
         if (var12 != null) {
            var12.shiftIndex(var1, var5);
         }

         LocalVariableTypeAttribute var9 = (LocalVariableTypeAttribute)var4.getAttribute("LocalVariableTypeTable");
         if (var9 != null) {
            var9.shiftIndex(var1, var5);
         }

         StackMapTable var10 = (StackMapTable)var4.getAttribute("StackMapTable");
         if (var10 != null) {
            var10.insertLocal(var1, StackMapTable.typeTagOf(var6), var7);
         }

         StackMap var11 = (StackMap)var4.getAttribute("StackMap");
         if (var11 != null) {
            var11.insertLocal(var1, StackMapTable.typeTagOf(var6), var7);
         }
      }

   }

   public void instrument(CodeConverter var1) throws CannotCompileException {
      this.declaringClass.checkModify();
      ConstPool var2 = this.methodInfo.getConstPool();
      var1.doit(this.getDeclaringClass(), this.methodInfo, var2);
   }

   public void instrument(ExprEditor var1) throws CannotCompileException {
      if (this.declaringClass.isFrozen()) {
         this.declaringClass.checkModify();
      }

      if (var1.doit(this.declaringClass, this.methodInfo)) {
         this.declaringClass.checkModify();
      }

   }

   public void insertBefore(String var1) throws CannotCompileException {
      this.insertBefore(var1, true);
   }

   private void insertBefore(String var1, boolean var2) throws CannotCompileException {
      CtClass var3 = this.declaringClass;
      var3.checkModify();
      CodeAttribute var4 = this.methodInfo.getCodeAttribute();
      if (var4 == null) {
         throw new CannotCompileException("no method body");
      } else {
         CodeIterator var5 = var4.iterator();
         Javac var6 = new Javac(var3);

         try {
            int var7 = var6.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            var6.recordParamNames(var4, var7);
            var6.recordLocalVariables(var4, 0);
            var6.recordType(this.getReturnType0());
            var6.compileStmnt(var1);
            Bytecode var8 = var6.getBytecode();
            int var9 = var8.getMaxStack();
            int var10 = var8.getMaxLocals();
            if (var9 > var4.getMaxStack()) {
               var4.setMaxStack(var9);
            }

            if (var10 > var4.getMaxLocals()) {
               var4.setMaxLocals(var10);
            }

            int var11 = var5.insertEx(var8.get());
            var5.insert(var8.getExceptionTable(), var11);
            if (var2) {
               this.methodInfo.rebuildStackMapIf6(var3.getClassPool(), var3.getClassFile2());
            }

         } catch (NotFoundException var12) {
            throw new CannotCompileException(var12);
         } catch (CompileError var13) {
            throw new CannotCompileException(var13);
         } catch (BadBytecode var14) {
            throw new CannotCompileException(var14);
         }
      }
   }

   public void insertAfter(String var1) throws CannotCompileException {
      this.insertAfter(var1, false);
   }

   public void insertAfter(String var1, boolean var2) throws CannotCompileException {
      CtClass var3 = this.declaringClass;
      var3.checkModify();
      ConstPool var4 = this.methodInfo.getConstPool();
      CodeAttribute var5 = this.methodInfo.getCodeAttribute();
      if (var5 == null) {
         throw new CannotCompileException("no method body");
      } else {
         CodeIterator var6 = var5.iterator();
         int var7 = var5.getMaxLocals();
         Bytecode var8 = new Bytecode(var4, 0, var7 + 1);
         var8.setStackDepth(var5.getMaxStack() + 1);
         Javac var9 = new Javac(var8, var3);

         try {
            int var10 = var9.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            var9.recordParamNames(var5, var10);
            CtClass var11 = this.getReturnType0();
            int var12 = var9.recordReturnType(var11, true);
            var9.recordLocalVariables(var5, 0);
            int var13 = this.insertAfterHandler(var2, var8, var11, var12, var9, var1);
            int var14 = var6.getCodeLength();
            if (var2) {
               var5.getExceptionTable().add(this.getStartPosOfBody(var5), var14, var14, 0);
            }

            int var15 = 0;
            int var16 = 0;
            boolean var17 = true;

            while(var6.hasNext()) {
               int var18 = var6.next();
               if (var18 >= var14) {
                  break;
               }

               int var19 = var6.byteAt(var18);
               if (var19 == 176 || var19 == 172 || var19 == 174 || var19 == 173 || var19 == 175 || var19 == 177) {
                  if (var17) {
                     var15 = this.insertAfterAdvice(var8, var9, var1, var4, var11, var12);
                     var14 = var6.append(var8.get());
                     var6.append(var8.getExceptionTable(), var14);
                     var16 = var6.getCodeLength() - var15;
                     var13 = var16 - var14;
                     var17 = false;
                  }

                  this.insertGoto(var6, var16, var18);
                  var16 = var6.getCodeLength() - var15;
                  var14 = var16 - var13;
               }
            }

            if (var17) {
               var14 = var6.append(var8.get());
               var6.append(var8.getExceptionTable(), var14);
            }

            var5.setMaxStack(var8.getMaxStack());
            var5.setMaxLocals(var8.getMaxLocals());
            this.methodInfo.rebuildStackMapIf6(var3.getClassPool(), var3.getClassFile2());
         } catch (NotFoundException var20) {
            throw new CannotCompileException(var20);
         } catch (CompileError var21) {
            throw new CannotCompileException(var21);
         } catch (BadBytecode var22) {
            throw new CannotCompileException(var22);
         }
      }
   }

   private int insertAfterAdvice(Bytecode var1, Javac var2, String var3, ConstPool var4, CtClass var5, int var6) throws CompileError {
      int var7 = var1.currentPc();
      if (var5 == CtClass.voidType) {
         var1.addOpcode(1);
         var1.addAstore(var6);
         var2.compileStmnt(var3);
         var1.addOpcode(177);
         if (var1.getMaxLocals() < 1) {
            var1.setMaxLocals(1);
         }
      } else {
         var1.addStore(var6, var5);
         var2.compileStmnt(var3);
         var1.addLoad(var6, var5);
         if (var5.isPrimitive()) {
            var1.addOpcode(((CtPrimitiveType)var5).getReturnOp());
         } else {
            var1.addOpcode(176);
         }
      }

      return var1.currentPc() - var7;
   }

   private void insertGoto(CodeIterator var1, int var2, int var3) throws BadBytecode {
      var1.setMark(var2);
      var1.writeByte(0, var3);
      boolean var4 = var2 + 2 - var3 > 32767;
      int var5 = var4 ? 4 : 2;
      CodeIterator.Gap var6 = var1.insertGapAt(var3, var5, false);
      var3 = var6.position + var6.length - var5;
      int var7 = var1.getMark() - var3;
      if (var4) {
         var1.writeByte(200, var3);
         var1.write32bit(var7, var3 + 1);
      } else if (var7 <= 32767) {
         var1.writeByte(167, var3);
         var1.write16bit(var7, var3 + 1);
      } else {
         if (var6.length < 4) {
            CodeIterator.Gap var8 = var1.insertGapAt(var6.position, 2, false);
            var3 = var8.position + var8.length + var6.length - 4;
         }

         var1.writeByte(200, var3);
         var1.write32bit(var1.getMark() - var3, var3 + 1);
      }

   }

   private int insertAfterHandler(boolean var1, Bytecode var2, CtClass var3, int var4, Javac var5, String var6) throws CompileError {
      if (!var1) {
         return 0;
      } else {
         int var7 = var2.getMaxLocals();
         var2.incMaxLocals(1);
         int var8 = var2.currentPc();
         var2.addAstore(var7);
         if (var3.isPrimitive()) {
            char var9 = ((CtPrimitiveType)var3).getDescriptor();
            if (var9 == 'D') {
               var2.addDconst(0.0D);
               var2.addDstore(var4);
            } else if (var9 == 'F') {
               var2.addFconst(0.0F);
               var2.addFstore(var4);
            } else if (var9 == 'J') {
               var2.addLconst(0L);
               var2.addLstore(var4);
            } else if (var9 == 'V') {
               var2.addOpcode(1);
               var2.addAstore(var4);
            } else {
               var2.addIconst(0);
               var2.addIstore(var4);
            }
         } else {
            var2.addOpcode(1);
            var2.addAstore(var4);
         }

         var5.compileStmnt(var6);
         var2.addAload(var7);
         var2.addOpcode(191);
         return var2.currentPc() - var8;
      }
   }

   public void addCatch(String var1, CtClass var2) throws CannotCompileException {
      this.addCatch(var1, var2, "$e");
   }

   public void addCatch(String var1, CtClass var2, String var3) throws CannotCompileException {
      CtClass var4 = this.declaringClass;
      var4.checkModify();
      ConstPool var5 = this.methodInfo.getConstPool();
      CodeAttribute var6 = this.methodInfo.getCodeAttribute();
      CodeIterator var7 = var6.iterator();
      Bytecode var8 = new Bytecode(var5, var6.getMaxStack(), var6.getMaxLocals());
      var8.setStackDepth(1);
      Javac var9 = new Javac(var8, var4);

      try {
         var9.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
         int var10 = var9.recordVariable(var2, var3);
         var8.addAstore(var10);
         var9.compileStmnt(var1);
         int var11 = var8.getMaxStack();
         int var12 = var8.getMaxLocals();
         if (var11 > var6.getMaxStack()) {
            var6.setMaxStack(var11);
         }

         if (var12 > var6.getMaxLocals()) {
            var6.setMaxLocals(var12);
         }

         int var13 = var7.getCodeLength();
         int var14 = var7.append(var8.get());
         var6.getExceptionTable().add(this.getStartPosOfBody(var6), var13, var13, var5.addClassInfo(var2));
         var7.append(var8.getExceptionTable(), var14);
         this.methodInfo.rebuildStackMapIf6(var4.getClassPool(), var4.getClassFile2());
      } catch (NotFoundException var15) {
         throw new CannotCompileException(var15);
      } catch (CompileError var16) {
         throw new CannotCompileException(var16);
      } catch (BadBytecode var17) {
         throw new CannotCompileException(var17);
      }
   }

   int getStartPosOfBody(CodeAttribute var1) throws CannotCompileException {
      return 0;
   }

   public int insertAt(int var1, String var2) throws CannotCompileException {
      return this.insertAt(var1, true, var2);
   }

   public int insertAt(int var1, boolean var2, String var3) throws CannotCompileException {
      CodeAttribute var4 = this.methodInfo.getCodeAttribute();
      if (var4 == null) {
         throw new CannotCompileException("no method body");
      } else {
         LineNumberAttribute var5 = (LineNumberAttribute)var4.getAttribute("LineNumberTable");
         if (var5 == null) {
            throw new CannotCompileException("no line number info");
         } else {
            LineNumberAttribute.Pc var6 = var5.toNearPc(var1);
            var1 = var6.line;
            int var7 = var6.index;
            if (!var2) {
               return var1;
            } else {
               CtClass var8 = this.declaringClass;
               var8.checkModify();
               CodeIterator var9 = var4.iterator();
               Javac var10 = new Javac(var8);

               try {
                  var10.recordLocalVariables(var4, var7);
                  var10.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
                  var10.setMaxLocals(var4.getMaxLocals());
                  var10.compileStmnt(var3);
                  Bytecode var11 = var10.getBytecode();
                  int var12 = var11.getMaxLocals();
                  int var13 = var11.getMaxStack();
                  var4.setMaxLocals(var12);
                  if (var13 > var4.getMaxStack()) {
                     var4.setMaxStack(var13);
                  }

                  var7 = var9.insertAt(var7, var11.get());
                  var9.insert(var11.getExceptionTable(), var7);
                  this.methodInfo.rebuildStackMapIf6(var8.getClassPool(), var8.getClassFile2());
                  return var1;
               } catch (NotFoundException var14) {
                  throw new CannotCompileException(var14);
               } catch (CompileError var15) {
                  throw new CannotCompileException(var15);
               } catch (BadBytecode var16) {
                  throw new CannotCompileException(var16);
               }
            }
         }
      }
   }
}
