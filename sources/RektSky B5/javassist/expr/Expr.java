/*
 * Decompiled with CFR 0.152.
 */
package javassist.expr;

import java.util.LinkedList;
import java.util.List;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import javassist.expr.ExprEditor;

public abstract class Expr
implements Opcode {
    int currentPos;
    CodeIterator iterator;
    CtClass thisClass;
    MethodInfo thisMethod;
    boolean edited;
    int maxLocals;
    int maxStack;
    static final String javaLangObject = "java.lang.Object";

    protected Expr(int pos, CodeIterator i2, CtClass declaring, MethodInfo m2) {
        this.currentPos = pos;
        this.iterator = i2;
        this.thisClass = declaring;
        this.thisMethod = m2;
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
        return (this.thisMethod.getAccessFlags() & 8) != 0;
    }

    public CtBehavior where() {
        MethodInfo mi = this.thisMethod;
        CtBehavior[] cb = this.thisClass.getDeclaredBehaviors();
        for (int i2 = cb.length - 1; i2 >= 0; --i2) {
            if (cb[i2].getMethodInfo2() != mi) continue;
            return cb[i2];
        }
        CtConstructor init = this.thisClass.getClassInitializer();
        if (init != null && init.getMethodInfo2() == mi) {
            return init;
        }
        for (int i3 = cb.length - 1; i3 >= 0; --i3) {
            if (!this.thisMethod.getName().equals(cb[i3].getMethodInfo2().getName()) || !this.thisMethod.getDescriptor().equals(cb[i3].getMethodInfo2().getDescriptor())) continue;
            return cb[i3];
        }
        throw new RuntimeException("fatal: not found");
    }

    public CtClass[] mayThrow() {
        String[] exceptions;
        ExceptionsAttribute ea;
        ClassPool pool = this.thisClass.getClassPool();
        ConstPool cp = this.thisMethod.getConstPool();
        LinkedList<CtClass> list = new LinkedList<CtClass>();
        try {
            CodeAttribute ca = this.thisMethod.getCodeAttribute();
            ExceptionTable et = ca.getExceptionTable();
            int pos = this.currentPos;
            int n2 = et.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                int t2;
                if (et.startPc(i2) > pos || pos >= et.endPc(i2) || (t2 = et.catchType(i2)) <= 0) continue;
                try {
                    Expr.addClass(list, pool.get(cp.getClassInfo(t2)));
                    continue;
                }
                catch (NotFoundException notFoundException) {
                    // empty catch block
                }
            }
        }
        catch (NullPointerException ca) {
            // empty catch block
        }
        if ((ea = this.thisMethod.getExceptionsAttribute()) != null && (exceptions = ea.getExceptions()) != null) {
            int n3 = exceptions.length;
            for (int i3 = 0; i3 < n3; ++i3) {
                try {
                    Expr.addClass(list, pool.get(exceptions[i3]));
                    continue;
                }
                catch (NotFoundException notFoundException) {
                    // empty catch block
                }
            }
        }
        return list.toArray(new CtClass[list.size()]);
    }

    private static void addClass(List<CtClass> list, CtClass c2) {
        if (list.contains(c2)) {
            return;
        }
        list.add(c2);
    }

    public int indexOfBytecode() {
        return this.currentPos;
    }

    public int getLineNumber() {
        return this.thisMethod.getLineNumber(this.currentPos);
    }

    public String getFileName() {
        ClassFile cf = this.thisClass.getClassFile2();
        if (cf == null) {
            return null;
        }
        return cf.getSourceFile();
    }

    static final boolean checkResultValue(CtClass retType, String prog) throws CannotCompileException {
        boolean hasIt;
        boolean bl = hasIt = prog.indexOf("$_") >= 0;
        if (!hasIt && retType != CtClass.voidType) {
            throw new CannotCompileException("the resulting value is not stored in $_");
        }
        return hasIt;
    }

    static final void storeStack(CtClass[] params, boolean isStaticCall, int regno, Bytecode bytecode) {
        Expr.storeStack0(0, params.length, params, regno + 1, bytecode);
        if (isStaticCall) {
            bytecode.addOpcode(1);
        }
        bytecode.addAstore(regno);
    }

    private static void storeStack0(int i2, int n2, CtClass[] params, int regno, Bytecode bytecode) {
        if (i2 >= n2) {
            return;
        }
        CtClass c2 = params[i2];
        int size = c2 instanceof CtPrimitiveType ? ((CtPrimitiveType)c2).getDataSize() : 1;
        Expr.storeStack0(i2 + 1, n2, params, regno + size, bytecode);
        bytecode.addStore(regno, c2);
    }

    public abstract void replace(String var1) throws CannotCompileException;

    public void replace(String statement, ExprEditor recursive) throws CannotCompileException {
        this.replace(statement);
        if (recursive != null) {
            this.runEditor(recursive, this.iterator);
        }
    }

    protected void replace0(int pos, Bytecode bytecode, int size) throws BadBytecode {
        byte[] code = bytecode.get();
        this.edited = true;
        int gap = code.length - size;
        for (int i2 = 0; i2 < size; ++i2) {
            this.iterator.writeByte(0, pos + i2);
        }
        if (gap > 0) {
            pos = this.iterator.insertGapAt((int)pos, (int)gap, (boolean)false).position;
        }
        this.iterator.write(code, pos);
        this.iterator.insert(bytecode.getExceptionTable(), pos);
        this.maxLocals = bytecode.getMaxLocals();
        this.maxStack = bytecode.getMaxStack();
    }

    protected void runEditor(ExprEditor ed, CodeIterator oldIterator) throws CannotCompileException {
        CodeAttribute codeAttr = oldIterator.get();
        int orgLocals = codeAttr.getMaxLocals();
        int orgStack = codeAttr.getMaxStack();
        int newLocals = this.locals();
        codeAttr.setMaxStack(this.stack());
        codeAttr.setMaxLocals(newLocals);
        ExprEditor.LoopContext context = new ExprEditor.LoopContext(newLocals);
        int size = oldIterator.getCodeLength();
        int endPos = oldIterator.lookAhead();
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

