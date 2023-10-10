// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.expr;

import com.viaversion.viaversion.libs.javassist.compiler.JvstTypeChecker;
import com.viaversion.viaversion.libs.javassist.compiler.ast.ASTList;
import com.viaversion.viaversion.libs.javassist.compiler.JvstCodeGen;
import com.viaversion.viaversion.libs.javassist.bytecode.Bytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeAttribute;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.compiler.CompileError;
import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.compiler.ProceedHandler;
import com.viaversion.viaversion.libs.javassist.compiler.Javac;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.CtBehavior;
import com.viaversion.viaversion.libs.javassist.bytecode.MethodInfo;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;

public class Instanceof extends Expr
{
    protected Instanceof(final int pos, final CodeIterator i, final CtClass declaring, final MethodInfo m) {
        super(pos, i, declaring, m);
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
    
    public CtClass getType() throws NotFoundException {
        final ConstPool cp = this.getConstPool();
        final int pos = this.currentPos;
        final int index = this.iterator.u16bitAt(pos + 1);
        final String name = cp.getClassInfo(index);
        return this.thisClass.getClassPool().getCtClass(name);
    }
    
    @Override
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }
    
    @Override
    public void replace(final String statement) throws CannotCompileException {
        this.thisClass.getClassFile();
        final ConstPool constPool = this.getConstPool();
        final int pos = this.currentPos;
        final int index = this.iterator.u16bitAt(pos + 1);
        final Javac jc = new Javac(this.thisClass);
        final ClassPool cp = this.thisClass.getClassPool();
        final CodeAttribute ca = this.iterator.get();
        try {
            final CtClass[] params = { cp.get("java.lang.Object") };
            final CtClass retType = CtClass.booleanType;
            final int paramVar = ca.getMaxLocals();
            jc.recordParams("java.lang.Object", params, true, paramVar, this.withinStatic());
            final int retVar = jc.recordReturnType(retType, true);
            jc.recordProceed(new ProceedForInstanceof(index));
            jc.recordType(this.getType());
            Expr.checkResultValue(retType, statement);
            final Bytecode bytecode = jc.getBytecode();
            Expr.storeStack(params, true, paramVar, bytecode);
            jc.recordLocalVariables(ca, pos);
            bytecode.addConstZero(retType);
            bytecode.addStore(retVar, retType);
            jc.compileStmnt(statement);
            bytecode.addLoad(retVar, retType);
            this.replace0(pos, bytecode, 3);
        }
        catch (CompileError e) {
            throw new CannotCompileException(e);
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
        catch (BadBytecode e3) {
            throw new CannotCompileException("broken method");
        }
    }
    
    static class ProceedForInstanceof implements ProceedHandler
    {
        int index;
        
        ProceedForInstanceof(final int i) {
            this.index = i;
        }
        
        @Override
        public void doit(final JvstCodeGen gen, final Bytecode bytecode, final ASTList args) throws CompileError {
            if (gen.getMethodArgsLength(args) != 1) {
                throw new CompileError("$proceed() cannot take more than one parameter for instanceof");
            }
            gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
            bytecode.addOpcode(193);
            bytecode.addIndex(this.index);
            gen.setType(CtClass.booleanType);
        }
        
        @Override
        public void setReturnType(final JvstTypeChecker c, final ASTList args) throws CompileError {
            c.atMethodArgs(args, new int[1], new int[1], new String[1]);
            c.setType(CtClass.booleanType);
        }
    }
}
