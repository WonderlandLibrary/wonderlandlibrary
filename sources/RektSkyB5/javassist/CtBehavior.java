/*
 * Decompiled with CFR 0.152.
 */
package javassist;

import javassist.CannotCompileException;
import javassist.ClassMap;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtClassType;
import javassist.CtField;
import javassist.CtMember;
import javassist.CtPrimitiveType;
import javassist.Modifier;
import javassist.NotFoundException;
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

public abstract class CtBehavior
extends CtMember {
    protected MethodInfo methodInfo;

    protected CtBehavior(CtClass clazz, MethodInfo minfo) {
        super(clazz);
        this.methodInfo = minfo;
    }

    void copy(CtBehavior src, boolean isCons, ClassMap map) throws CannotCompileException {
        CtClass declaring = this.declaringClass;
        MethodInfo srcInfo = src.methodInfo;
        CtClass srcClass = src.getDeclaringClass();
        ConstPool cp = declaring.getClassFile2().getConstPool();
        map = new ClassMap(map);
        map.put(srcClass.getName(), declaring.getName());
        try {
            String srcSuperName;
            boolean patch = false;
            CtClass srcSuper = srcClass.getSuperclass();
            CtClass destSuper = declaring.getSuperclass();
            String destSuperName = null;
            if (srcSuper != null && destSuper != null && !(srcSuperName = srcSuper.getName()).equals(destSuperName = destSuper.getName())) {
                if (srcSuperName.equals("java.lang.Object")) {
                    patch = true;
                } else {
                    map.putIfNone(srcSuperName, destSuperName);
                }
            }
            this.methodInfo = new MethodInfo(cp, srcInfo.getName(), srcInfo, map);
            if (isCons && patch) {
                this.methodInfo.setSuperclass(destSuperName);
            }
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
        catch (BadBytecode e3) {
            throw new CannotCompileException(e3);
        }
    }

    @Override
    protected void extendToString(StringBuffer buffer) {
        buffer.append(' ');
        buffer.append(this.getName());
        buffer.append(' ');
        buffer.append(this.methodInfo.getDescriptor());
    }

    public abstract String getLongName();

    public MethodInfo getMethodInfo() {
        this.declaringClass.checkModify();
        return this.methodInfo;
    }

    public MethodInfo getMethodInfo2() {
        return this.methodInfo;
    }

    @Override
    public int getModifiers() {
        return AccessFlag.toModifier(this.methodInfo.getAccessFlags());
    }

    @Override
    public void setModifiers(int mod) {
        this.declaringClass.checkModify();
        this.methodInfo.setAccessFlags(AccessFlag.of(mod));
    }

    @Override
    public boolean hasAnnotation(String typeName) {
        MethodInfo mi = this.getMethodInfo2();
        AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.hasAnnotationType(typeName, this.getDeclaringClass().getClassPool(), ainfo, ainfo2);
    }

    @Override
    public Object getAnnotation(Class<?> clz) throws ClassNotFoundException {
        MethodInfo mi = this.getMethodInfo2();
        AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.getAnnotationType(clz, this.getDeclaringClass().getClassPool(), ainfo, ainfo2);
    }

    @Override
    public Object[] getAnnotations() throws ClassNotFoundException {
        return this.getAnnotations(false);
    }

    @Override
    public Object[] getAvailableAnnotations() {
        try {
            return this.getAnnotations(true);
        }
        catch (ClassNotFoundException e2) {
            throw new RuntimeException("Unexpected exception", e2);
        }
    }

    private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
        MethodInfo mi = this.getMethodInfo2();
        AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.toAnnotationType(ignoreNotFound, this.getDeclaringClass().getClassPool(), ainfo, ainfo2);
    }

    public Object[][] getParameterAnnotations() throws ClassNotFoundException {
        return this.getParameterAnnotations(false);
    }

    public Object[][] getAvailableParameterAnnotations() {
        try {
            return this.getParameterAnnotations(true);
        }
        catch (ClassNotFoundException e2) {
            throw new RuntimeException("Unexpected exception", e2);
        }
    }

    Object[][] getParameterAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
        MethodInfo mi = this.getMethodInfo2();
        ParameterAnnotationsAttribute ainfo = (ParameterAnnotationsAttribute)mi.getAttribute("RuntimeInvisibleParameterAnnotations");
        ParameterAnnotationsAttribute ainfo2 = (ParameterAnnotationsAttribute)mi.getAttribute("RuntimeVisibleParameterAnnotations");
        return CtClassType.toAnnotationType(ignoreNotFound, this.getDeclaringClass().getClassPool(), ainfo, ainfo2, mi);
    }

    public CtClass[] getParameterTypes() throws NotFoundException {
        return Descriptor.getParameterTypes(this.methodInfo.getDescriptor(), this.declaringClass.getClassPool());
    }

    CtClass getReturnType0() throws NotFoundException {
        return Descriptor.getReturnType(this.methodInfo.getDescriptor(), this.declaringClass.getClassPool());
    }

    @Override
    public String getSignature() {
        return this.methodInfo.getDescriptor();
    }

    @Override
    public String getGenericSignature() {
        SignatureAttribute sa = (SignatureAttribute)this.methodInfo.getAttribute("Signature");
        return sa == null ? null : sa.getSignature();
    }

    @Override
    public void setGenericSignature(String sig) {
        this.declaringClass.checkModify();
        this.methodInfo.addAttribute(new SignatureAttribute(this.methodInfo.getConstPool(), sig));
    }

    public CtClass[] getExceptionTypes() throws NotFoundException {
        ExceptionsAttribute ea = this.methodInfo.getExceptionsAttribute();
        String[] exceptions = ea == null ? null : ea.getExceptions();
        return this.declaringClass.getClassPool().get(exceptions);
    }

    public void setExceptionTypes(CtClass[] types) throws NotFoundException {
        this.declaringClass.checkModify();
        if (types == null || types.length == 0) {
            this.methodInfo.removeExceptionsAttribute();
            return;
        }
        String[] names = new String[types.length];
        for (int i2 = 0; i2 < types.length; ++i2) {
            names[i2] = types[i2].getName();
        }
        ExceptionsAttribute ea = this.methodInfo.getExceptionsAttribute();
        if (ea == null) {
            ea = new ExceptionsAttribute(this.methodInfo.getConstPool());
            this.methodInfo.setExceptionsAttribute(ea);
        }
        ea.setExceptions(names);
    }

    public abstract boolean isEmpty();

    public void setBody(String src) throws CannotCompileException {
        this.setBody(src, null, null);
    }

    public void setBody(String src, String delegateObj, String delegateMethod) throws CannotCompileException {
        CtClass cc = this.declaringClass;
        cc.checkModify();
        try {
            Javac jv = new Javac(cc);
            if (delegateMethod != null) {
                jv.recordProceed(delegateObj, delegateMethod);
            }
            Bytecode b2 = jv.compileBody(this, src);
            this.methodInfo.setCodeAttribute(b2.toCodeAttribute());
            this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & 0xFFFFFBFF);
            this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
            this.declaringClass.rebuildClassFile();
        }
        catch (CompileError e2) {
            throw new CannotCompileException(e2);
        }
        catch (BadBytecode e3) {
            throw new CannotCompileException(e3);
        }
    }

    static void setBody0(CtClass srcClass, MethodInfo srcInfo, CtClass destClass, MethodInfo destInfo, ClassMap map) throws CannotCompileException {
        destClass.checkModify();
        map = new ClassMap(map);
        map.put(srcClass.getName(), destClass.getName());
        try {
            CodeAttribute cattr = srcInfo.getCodeAttribute();
            if (cattr != null) {
                ConstPool cp = destInfo.getConstPool();
                CodeAttribute ca = (CodeAttribute)cattr.copy(cp, map);
                destInfo.setCodeAttribute(ca);
            }
        }
        catch (CodeAttribute.RuntimeCopyException e2) {
            throw new CannotCompileException(e2);
        }
        destInfo.setAccessFlags(destInfo.getAccessFlags() & 0xFFFFFBFF);
        destClass.rebuildClassFile();
    }

    @Override
    public byte[] getAttribute(String name) {
        AttributeInfo ai = this.methodInfo.getAttribute(name);
        if (ai == null) {
            return null;
        }
        return ai.get();
    }

    @Override
    public void setAttribute(String name, byte[] data) {
        this.declaringClass.checkModify();
        this.methodInfo.addAttribute(new AttributeInfo(this.methodInfo.getConstPool(), name, data));
    }

    public void useCflow(String name) throws CannotCompileException {
        CtClass cc = this.declaringClass;
        cc.checkModify();
        ClassPool pool = cc.getClassPool();
        int i2 = 0;
        while (true) {
            String fname = "_cflow$" + i2++;
            try {
                cc.getDeclaredField(fname);
            }
            catch (NotFoundException e2) {
                pool.recordCflow(name, this.declaringClass.getName(), fname);
                try {
                    CtClass type = pool.get("javassist.runtime.Cflow");
                    CtField field = new CtField(type, fname, cc);
                    field.setModifiers(9);
                    cc.addField(field, CtField.Initializer.byNew(type));
                    this.insertBefore(fname + ".enter();", false);
                    String src = fname + ".exit();";
                    this.insertAfter(src, true);
                }
                catch (NotFoundException e3) {
                    throw new CannotCompileException(e3);
                }
                return;
            }
        }
    }

    public void addLocalVariable(String name, CtClass type) throws CannotCompileException {
        this.declaringClass.checkModify();
        ConstPool cp = this.methodInfo.getConstPool();
        CodeAttribute ca = this.methodInfo.getCodeAttribute();
        if (ca == null) {
            throw new CannotCompileException("no method body");
        }
        LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
        if (va == null) {
            va = new LocalVariableAttribute(cp);
            ca.getAttributes().add(va);
        }
        int maxLocals = ca.getMaxLocals();
        String desc = Descriptor.of(type);
        va.addEntry(0, ca.getCodeLength(), cp.addUtf8Info(name), cp.addUtf8Info(desc), maxLocals);
        ca.setMaxLocals(maxLocals + Descriptor.dataSize(desc));
    }

    public void insertParameter(CtClass type) throws CannotCompileException {
        this.declaringClass.checkModify();
        String desc = this.methodInfo.getDescriptor();
        String desc2 = Descriptor.insertParameter(type, desc);
        try {
            this.addParameter2(Modifier.isStatic(this.getModifiers()) ? 0 : 1, type, desc);
        }
        catch (BadBytecode e2) {
            throw new CannotCompileException(e2);
        }
        this.methodInfo.setDescriptor(desc2);
    }

    public void addParameter(CtClass type) throws CannotCompileException {
        this.declaringClass.checkModify();
        String desc = this.methodInfo.getDescriptor();
        String desc2 = Descriptor.appendParameter(type, desc);
        int offset = Modifier.isStatic(this.getModifiers()) ? 0 : 1;
        try {
            this.addParameter2(offset + Descriptor.paramSize(desc), type, desc);
        }
        catch (BadBytecode e2) {
            throw new CannotCompileException(e2);
        }
        this.methodInfo.setDescriptor(desc2);
    }

    private void addParameter2(int where, CtClass type, String desc) throws BadBytecode {
        CodeAttribute ca = this.methodInfo.getCodeAttribute();
        if (ca != null) {
            StackMap sm;
            StackMapTable smt;
            LocalVariableTypeAttribute lvta;
            int size = 1;
            char typeDesc = 'L';
            int classInfo = 0;
            if (type.isPrimitive()) {
                CtPrimitiveType cpt = (CtPrimitiveType)type;
                size = cpt.getDataSize();
                typeDesc = cpt.getDescriptor();
            } else {
                classInfo = this.methodInfo.getConstPool().addClassInfo(type);
            }
            ca.insertLocalVar(where, size);
            LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
            if (va != null) {
                va.shiftIndex(where, size);
            }
            if ((lvta = (LocalVariableTypeAttribute)ca.getAttribute("LocalVariableTypeTable")) != null) {
                lvta.shiftIndex(where, size);
            }
            if ((smt = (StackMapTable)ca.getAttribute("StackMapTable")) != null) {
                smt.insertLocal(where, StackMapTable.typeTagOf(typeDesc), classInfo);
            }
            if ((sm = (StackMap)ca.getAttribute("StackMap")) != null) {
                sm.insertLocal(where, StackMapTable.typeTagOf(typeDesc), classInfo);
            }
        }
    }

    public void instrument(CodeConverter converter) throws CannotCompileException {
        this.declaringClass.checkModify();
        ConstPool cp = this.methodInfo.getConstPool();
        converter.doit(this.getDeclaringClass(), this.methodInfo, cp);
    }

    public void instrument(ExprEditor editor) throws CannotCompileException {
        if (this.declaringClass.isFrozen()) {
            this.declaringClass.checkModify();
        }
        if (editor.doit(this.declaringClass, this.methodInfo)) {
            this.declaringClass.checkModify();
        }
    }

    public void insertBefore(String src) throws CannotCompileException {
        this.insertBefore(src, true);
    }

    private void insertBefore(String src, boolean rebuild) throws CannotCompileException {
        CtClass cc = this.declaringClass;
        cc.checkModify();
        CodeAttribute ca = this.methodInfo.getCodeAttribute();
        if (ca == null) {
            throw new CannotCompileException("no method body");
        }
        CodeIterator iterator = ca.iterator();
        Javac jv = new Javac(cc);
        try {
            int nvars = jv.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            jv.recordParamNames(ca, nvars);
            jv.recordLocalVariables(ca, 0);
            jv.recordReturnType(this.getReturnType0(), false);
            jv.compileStmnt(src);
            Bytecode b2 = jv.getBytecode();
            int stack = b2.getMaxStack();
            int locals = b2.getMaxLocals();
            if (stack > ca.getMaxStack()) {
                ca.setMaxStack(stack);
            }
            if (locals > ca.getMaxLocals()) {
                ca.setMaxLocals(locals);
            }
            int pos = iterator.insertEx(b2.get());
            iterator.insert(b2.getExceptionTable(), pos);
            if (rebuild) {
                this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
            }
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
        catch (CompileError e3) {
            throw new CannotCompileException(e3);
        }
        catch (BadBytecode e4) {
            throw new CannotCompileException(e4);
        }
    }

    public void insertAfter(String src) throws CannotCompileException {
        this.insertAfter(src, false, false);
    }

    public void insertAfter(String src, boolean asFinally) throws CannotCompileException {
        this.insertAfter(src, asFinally, false);
    }

    public void insertAfter(String src, boolean asFinally, boolean redundant) throws CannotCompileException {
        CtClass cc = this.declaringClass;
        cc.checkModify();
        ConstPool pool = this.methodInfo.getConstPool();
        CodeAttribute ca = this.methodInfo.getCodeAttribute();
        if (ca == null) {
            throw new CannotCompileException("no method body");
        }
        CodeIterator iterator = ca.iterator();
        int retAddr = ca.getMaxLocals();
        Bytecode b2 = new Bytecode(pool, 0, retAddr + 1);
        b2.setStackDepth(ca.getMaxStack() + 1);
        Javac jv = new Javac(b2, cc);
        try {
            int pos;
            int nvars = jv.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            jv.recordParamNames(ca, nvars);
            CtClass rtype = this.getReturnType0();
            int varNo = jv.recordReturnType(rtype, true);
            jv.recordLocalVariables(ca, 0);
            int handlerLen = this.insertAfterHandler(asFinally, b2, rtype, varNo, jv, src);
            int handlerPos = iterator.getCodeLength();
            if (asFinally) {
                ca.getExceptionTable().add(this.getStartPosOfBody(ca), handlerPos, handlerPos, 0);
            }
            int adviceLen = 0;
            int advicePos = 0;
            boolean noReturn = true;
            while (iterator.hasNext() && (pos = iterator.next()) < handlerPos) {
                int c2 = iterator.byteAt(pos);
                if (c2 != 176 && c2 != 172 && c2 != 174 && c2 != 173 && c2 != 175 && c2 != 177) continue;
                if (redundant) {
                    int retVarNo;
                    Javac jvc;
                    Bytecode bcode;
                    iterator.setMark2(handlerPos);
                    if (noReturn) {
                        noReturn = false;
                        bcode = b2;
                        jvc = jv;
                        retVarNo = varNo;
                    } else {
                        bcode = new Bytecode(pool, 0, retAddr + 1);
                        bcode.setStackDepth(ca.getMaxStack() + 1);
                        jvc = new Javac(bcode, cc);
                        int nvars2 = jvc.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
                        jvc.recordParamNames(ca, nvars2);
                        retVarNo = jvc.recordReturnType(rtype, true);
                        jvc.recordLocalVariables(ca, 0);
                    }
                    int adviceLen2 = this.insertAfterAdvice(bcode, jvc, src, pool, rtype, retVarNo);
                    int offset = iterator.append(bcode.get());
                    iterator.append(bcode.getExceptionTable(), offset);
                    int advicePos2 = iterator.getCodeLength() - adviceLen2;
                    this.insertGoto(iterator, advicePos2, pos);
                    handlerPos = iterator.getMark2();
                    continue;
                }
                if (noReturn) {
                    adviceLen = this.insertAfterAdvice(b2, jv, src, pool, rtype, varNo);
                    handlerPos = iterator.append(b2.get());
                    iterator.append(b2.getExceptionTable(), handlerPos);
                    advicePos = iterator.getCodeLength() - adviceLen;
                    handlerLen = advicePos - handlerPos;
                    noReturn = false;
                }
                this.insertGoto(iterator, advicePos, pos);
                advicePos = iterator.getCodeLength() - adviceLen;
                handlerPos = advicePos - handlerLen;
            }
            if (noReturn) {
                handlerPos = iterator.append(b2.get());
                iterator.append(b2.getExceptionTable(), handlerPos);
            }
            ca.setMaxStack(b2.getMaxStack());
            ca.setMaxLocals(b2.getMaxLocals());
            this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
        catch (CompileError e3) {
            throw new CannotCompileException(e3);
        }
        catch (BadBytecode e4) {
            throw new CannotCompileException(e4);
        }
    }

    private int insertAfterAdvice(Bytecode code, Javac jv, String src, ConstPool cp, CtClass rtype, int varNo) throws CompileError {
        int pc = code.currentPc();
        if (rtype == CtClass.voidType) {
            code.addOpcode(1);
            code.addAstore(varNo);
            jv.compileStmnt(src);
            code.addOpcode(177);
            if (code.getMaxLocals() < 1) {
                code.setMaxLocals(1);
            }
        } else {
            code.addStore(varNo, rtype);
            jv.compileStmnt(src);
            code.addLoad(varNo, rtype);
            if (rtype.isPrimitive()) {
                code.addOpcode(((CtPrimitiveType)rtype).getReturnOp());
            } else {
                code.addOpcode(176);
            }
        }
        return code.currentPc() - pc;
    }

    private void insertGoto(CodeIterator iterator, int subr, int pos) throws BadBytecode {
        iterator.setMark(subr);
        iterator.writeByte(0, pos);
        boolean wide = subr + 2 - pos > Short.MAX_VALUE;
        int len = wide ? 4 : 2;
        CodeIterator.Gap gap = iterator.insertGapAt(pos, len, false);
        pos = gap.position + gap.length - len;
        int offset = iterator.getMark() - pos;
        if (wide) {
            iterator.writeByte(200, pos);
            iterator.write32bit(offset, pos + 1);
        } else if (offset <= Short.MAX_VALUE) {
            iterator.writeByte(167, pos);
            iterator.write16bit(offset, pos + 1);
        } else {
            if (gap.length < 4) {
                CodeIterator.Gap gap2 = iterator.insertGapAt(gap.position, 2, false);
                pos = gap2.position + gap2.length + gap.length - 4;
            }
            iterator.writeByte(200, pos);
            iterator.write32bit(iterator.getMark() - pos, pos + 1);
        }
    }

    private int insertAfterHandler(boolean asFinally, Bytecode b2, CtClass rtype, int returnVarNo, Javac javac, String src) throws CompileError {
        if (!asFinally) {
            return 0;
        }
        int var2 = b2.getMaxLocals();
        b2.incMaxLocals(1);
        int pc = b2.currentPc();
        b2.addAstore(var2);
        if (rtype.isPrimitive()) {
            char c2 = ((CtPrimitiveType)rtype).getDescriptor();
            if (c2 == 'D') {
                b2.addDconst(0.0);
                b2.addDstore(returnVarNo);
            } else if (c2 == 'F') {
                b2.addFconst(0.0f);
                b2.addFstore(returnVarNo);
            } else if (c2 == 'J') {
                b2.addLconst(0L);
                b2.addLstore(returnVarNo);
            } else if (c2 == 'V') {
                b2.addOpcode(1);
                b2.addAstore(returnVarNo);
            } else {
                b2.addIconst(0);
                b2.addIstore(returnVarNo);
            }
        } else {
            b2.addOpcode(1);
            b2.addAstore(returnVarNo);
        }
        javac.compileStmnt(src);
        b2.addAload(var2);
        b2.addOpcode(191);
        return b2.currentPc() - pc;
    }

    public void addCatch(String src, CtClass exceptionType) throws CannotCompileException {
        this.addCatch(src, exceptionType, "$e");
    }

    public void addCatch(String src, CtClass exceptionType, String exceptionName) throws CannotCompileException {
        CtClass cc = this.declaringClass;
        cc.checkModify();
        ConstPool cp = this.methodInfo.getConstPool();
        CodeAttribute ca = this.methodInfo.getCodeAttribute();
        CodeIterator iterator = ca.iterator();
        Bytecode b2 = new Bytecode(cp, ca.getMaxStack(), ca.getMaxLocals());
        b2.setStackDepth(1);
        Javac jv = new Javac(b2, cc);
        try {
            jv.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            int var2 = jv.recordVariable(exceptionType, exceptionName);
            b2.addAstore(var2);
            jv.compileStmnt(src);
            int stack = b2.getMaxStack();
            int locals = b2.getMaxLocals();
            if (stack > ca.getMaxStack()) {
                ca.setMaxStack(stack);
            }
            if (locals > ca.getMaxLocals()) {
                ca.setMaxLocals(locals);
            }
            int len = iterator.getCodeLength();
            int pos = iterator.append(b2.get());
            ca.getExceptionTable().add(this.getStartPosOfBody(ca), len, len, cp.addClassInfo(exceptionType));
            iterator.append(b2.getExceptionTable(), pos);
            this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
        catch (CompileError e3) {
            throw new CannotCompileException(e3);
        }
        catch (BadBytecode e4) {
            throw new CannotCompileException(e4);
        }
    }

    int getStartPosOfBody(CodeAttribute ca) throws CannotCompileException {
        return 0;
    }

    public int insertAt(int lineNum, String src) throws CannotCompileException {
        return this.insertAt(lineNum, true, src);
    }

    public int insertAt(int lineNum, boolean modify, String src) throws CannotCompileException {
        CodeAttribute ca = this.methodInfo.getCodeAttribute();
        if (ca == null) {
            throw new CannotCompileException("no method body");
        }
        LineNumberAttribute ainfo = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
        if (ainfo == null) {
            throw new CannotCompileException("no line number info");
        }
        LineNumberAttribute.Pc pc = ainfo.toNearPc(lineNum);
        lineNum = pc.line;
        int index = pc.index;
        if (!modify) {
            return lineNum;
        }
        CtClass cc = this.declaringClass;
        cc.checkModify();
        CodeIterator iterator = ca.iterator();
        Javac jv = new Javac(cc);
        try {
            jv.recordLocalVariables(ca, index);
            jv.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            jv.setMaxLocals(ca.getMaxLocals());
            jv.compileStmnt(src);
            Bytecode b2 = jv.getBytecode();
            int locals = b2.getMaxLocals();
            int stack = b2.getMaxStack();
            ca.setMaxLocals(locals);
            if (stack > ca.getMaxStack()) {
                ca.setMaxStack(stack);
            }
            index = iterator.insertAt(index, b2.get());
            iterator.insert(b2.getExceptionTable(), index);
            this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
            return lineNum;
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
        catch (CompileError e3) {
            throw new CannotCompileException(e3);
        }
        catch (BadBytecode e4) {
            throw new CannotCompileException(e4);
        }
    }
}

