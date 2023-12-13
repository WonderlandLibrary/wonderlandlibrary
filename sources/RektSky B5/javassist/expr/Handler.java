/*
 * Decompiled with CFR 0.152.
 */
package javassist.expr;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.expr.Expr;

public class Handler
extends Expr {
    private static String EXCEPTION_NAME = "$1";
    private ExceptionTable etable;
    private int index;

    protected Handler(ExceptionTable et, int nth, CodeIterator it, CtClass declaring, MethodInfo m2) {
        super(et.handlerPc(nth), it, declaring, m2);
        this.etable = et;
        this.index = nth;
    }

    @Override
    public CtBehavior where() {
        return super.where();
    }

    @Override
    public int getLineNumber() {
        return super.getLineNumber();
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    @Override
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    public CtClass getType() throws NotFoundException {
        int type = this.etable.catchType(this.index);
        if (type == 0) {
            return null;
        }
        ConstPool cp = this.getConstPool();
        String name = cp.getClassInfo(type);
        return this.thisClass.getClassPool().getCtClass(name);
    }

    public boolean isFinally() {
        return this.etable.catchType(this.index) == 0;
    }

    @Override
    public void replace(String statement) throws CannotCompileException {
        throw new RuntimeException("not implemented yet");
    }

    public void insertBefore(String src) throws CannotCompileException {
        this.edited = true;
        ConstPool cp = this.getConstPool();
        CodeAttribute ca = this.iterator.get();
        Javac jv = new Javac(this.thisClass);
        Bytecode b2 = jv.getBytecode();
        b2.setStackDepth(1);
        b2.setMaxLocals(ca.getMaxLocals());
        try {
            CtClass type = this.getType();
            int var2 = jv.recordVariable(type, EXCEPTION_NAME);
            jv.recordReturnType(type, false);
            b2.addAstore(var2);
            jv.compileStmnt(src);
            b2.addAload(var2);
            int oldHandler = this.etable.handlerPc(this.index);
            b2.addOpcode(167);
            b2.addIndex(oldHandler - this.iterator.getCodeLength() - b2.currentPc() + 1);
            this.maxStack = b2.getMaxStack();
            this.maxLocals = b2.getMaxLocals();
            int pos = this.iterator.append(b2.get());
            this.iterator.append(b2.getExceptionTable(), pos);
            this.etable.setHandlerPc(this.index, pos);
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
        catch (CompileError e3) {
            throw new CannotCompileException(e3);
        }
    }
}

