// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.expr;

import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.CtPrimitiveType;
import com.viaversion.viaversion.libs.javassist.bytecode.Bytecode;
import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;
import com.viaversion.viaversion.libs.javassist.bytecode.ExceptionsAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.ExceptionTable;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeAttribute;
import java.util.List;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import java.util.LinkedList;
import com.viaversion.viaversion.libs.javassist.CtConstructor;
import com.viaversion.viaversion.libs.javassist.CtBehavior;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.bytecode.MethodInfo;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;
import com.viaversion.viaversion.libs.javassist.bytecode.Opcode;

public abstract class Expr implements Opcode
{
    int currentPos;
    CodeIterator iterator;
    CtClass thisClass;
    MethodInfo thisMethod;
    boolean edited;
    int maxLocals;
    int maxStack;
    static final String javaLangObject = "java.lang.Object";
    
    protected Expr(final int pos, final CodeIterator i, final CtClass declaring, final MethodInfo m) {
        this.currentPos = pos;
        this.iterator = i;
        this.thisClass = declaring;
        this.thisMethod = m;
    }
    
    public CtClass getEnclosingClass() {
        return this.thisClass;
    }
    
    protected final ConstPool getConstPool() {
        return this.thisMethod.getConstPool();
    }
    
    protected final boolean edited() {
        return this.edited;
    }
    
    protected final int locals() {
        return this.maxLocals;
    }
    
    protected final int stack() {
        return this.maxStack;
    }
    
    protected final boolean withinStatic() {
        return (this.thisMethod.getAccessFlags() & 0x8) != 0x0;
    }
    
    public CtBehavior where() {
        final MethodInfo mi = this.thisMethod;
        final CtBehavior[] cb = this.thisClass.getDeclaredBehaviors();
        for (int i = cb.length - 1; i >= 0; --i) {
            if (cb[i].getMethodInfo2() == mi) {
                return cb[i];
            }
        }
        final CtConstructor init = this.thisClass.getClassInitializer();
        if (init != null && init.getMethodInfo2() == mi) {
            return init;
        }
        for (int j = cb.length - 1; j >= 0; --j) {
            if (this.thisMethod.getName().equals(cb[j].getMethodInfo2().getName()) && this.thisMethod.getDescriptor().equals(cb[j].getMethodInfo2().getDescriptor())) {
                return cb[j];
            }
        }
        throw new RuntimeException("fatal: not found");
    }
    
    public CtClass[] mayThrow() {
        final ClassPool pool = this.thisClass.getClassPool();
        final ConstPool cp = this.thisMethod.getConstPool();
        final List<CtClass> list = new LinkedList<CtClass>();
        try {
            final CodeAttribute ca = this.thisMethod.getCodeAttribute();
            final ExceptionTable et = ca.getExceptionTable();
            final int pos = this.currentPos;
            for (int n = et.size(), i = 0; i < n; ++i) {
                if (et.startPc(i) <= pos && pos < et.endPc(i)) {
                    final int t = et.catchType(i);
                    if (t > 0) {
                        try {
                            addClass(list, pool.get(cp.getClassInfo(t)));
                        }
                        catch (NotFoundException ex) {}
                    }
                }
            }
        }
        catch (NullPointerException ex2) {}
        final ExceptionsAttribute ea = this.thisMethod.getExceptionsAttribute();
        if (ea != null) {
            final String[] exceptions = ea.getExceptions();
            if (exceptions != null) {
                for (int n2 = exceptions.length, j = 0; j < n2; ++j) {
                    try {
                        addClass(list, pool.get(exceptions[j]));
                    }
                    catch (NotFoundException ex3) {}
                }
            }
        }
        return list.toArray(new CtClass[list.size()]);
    }
    
    private static void addClass(final List<CtClass> list, final CtClass c) {
        if (list.contains(c)) {
            return;
        }
        list.add(c);
    }
    
    public int indexOfBytecode() {
        return this.currentPos;
    }
    
    public int getLineNumber() {
        return this.thisMethod.getLineNumber(this.currentPos);
    }
    
    public String getFileName() {
        final ClassFile cf = this.thisClass.getClassFile2();
        if (cf == null) {
            return null;
        }
        return cf.getSourceFile();
    }
    
    static final boolean checkResultValue(final CtClass retType, final String prog) throws CannotCompileException {
        final boolean hasIt = prog.indexOf("$_") >= 0;
        if (!hasIt && retType != CtClass.voidType) {
            throw new CannotCompileException("the resulting value is not stored in $_");
        }
        return hasIt;
    }
    
    static final void storeStack(final CtClass[] params, final boolean isStaticCall, final int regno, final Bytecode bytecode) {
        storeStack0(0, params.length, params, regno + 1, bytecode);
        if (isStaticCall) {
            bytecode.addOpcode(1);
        }
        bytecode.addAstore(regno);
    }
    
    private static void storeStack0(final int i, final int n, final CtClass[] params, final int regno, final Bytecode bytecode) {
        if (i >= n) {
            return;
        }
        final CtClass c = params[i];
        int size;
        if (c instanceof CtPrimitiveType) {
            size = ((CtPrimitiveType)c).getDataSize();
        }
        else {
            size = 1;
        }
        storeStack0(i + 1, n, params, regno + size, bytecode);
        bytecode.addStore(regno, c);
    }
    
    public abstract void replace(final String p0) throws CannotCompileException;
    
    public void replace(final String statement, final ExprEditor recursive) throws CannotCompileException {
        this.replace(statement);
        if (recursive != null) {
            this.runEditor(recursive, this.iterator);
        }
    }
    
    protected void replace0(int pos, final Bytecode bytecode, final int size) throws BadBytecode {
        final byte[] code = bytecode.get();
        this.edited = true;
        final int gap = code.length - size;
        for (int i = 0; i < size; ++i) {
            this.iterator.writeByte(0, pos + i);
        }
        if (gap > 0) {
            pos = this.iterator.insertGapAt(pos, gap, false).position;
        }
        this.iterator.write(code, pos);
        this.iterator.insert(bytecode.getExceptionTable(), pos);
        this.maxLocals = bytecode.getMaxLocals();
        this.maxStack = bytecode.getMaxStack();
    }
    
    protected void runEditor(final ExprEditor ed, final CodeIterator oldIterator) throws CannotCompileException {
        final CodeAttribute codeAttr = oldIterator.get();
        final int orgLocals = codeAttr.getMaxLocals();
        final int orgStack = codeAttr.getMaxStack();
        final int newLocals = this.locals();
        codeAttr.setMaxStack(this.stack());
        codeAttr.setMaxLocals(newLocals);
        final ExprEditor.LoopContext context = new ExprEditor.LoopContext(newLocals);
        final int size = oldIterator.getCodeLength();
        final int endPos = oldIterator.lookAhead();
        oldIterator.move(this.currentPos);
        if (ed.doit(this.thisClass, this.thisMethod, context, oldIterator, endPos)) {
            this.edited = true;
        }
        oldIterator.move(endPos + oldIterator.getCodeLength() - size);
        codeAttr.setMaxLocals(orgLocals);
        codeAttr.setMaxStack(orgStack);
        this.maxLocals = context.maxLocals;
        this.maxStack += context.maxStack;
    }
}
