/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.compiler.ast;

import com.viaversion.viaversion.libs.javassist.compiler.CompileError;
import com.viaversion.viaversion.libs.javassist.compiler.TokenId;
import com.viaversion.viaversion.libs.javassist.compiler.ast.ASTList;
import com.viaversion.viaversion.libs.javassist.compiler.ast.ASTree;
import com.viaversion.viaversion.libs.javassist.compiler.ast.Symbol;
import com.viaversion.viaversion.libs.javassist.compiler.ast.Visitor;

public class Declarator
extends ASTList
implements TokenId {
    private static final long serialVersionUID = 1L;
    protected int varType;
    protected int arrayDim;
    protected int localVar;
    protected String qualifiedClass;

    public Declarator(int type, int dim) {
        super(null);
        this.varType = type;
        this.arrayDim = dim;
        this.localVar = -1;
        this.qualifiedClass = null;
    }

    public Declarator(ASTList className, int dim) {
        super(null);
        this.varType = 307;
        this.arrayDim = dim;
        this.localVar = -1;
        this.qualifiedClass = Declarator.astToClassName(className, '/');
    }

    public Declarator(int type, String jvmClassName, int dim, int var2, Symbol sym) {
        super(null);
        this.varType = type;
        this.arrayDim = dim;
        this.localVar = var2;
        this.qualifiedClass = jvmClassName;
        this.setLeft(sym);
        Declarator.append(this, null);
    }

    public Declarator make(Symbol sym, int dim, ASTree init) {
        Declarator d2 = new Declarator(this.varType, this.arrayDim + dim);
        d2.qualifiedClass = this.qualifiedClass;
        d2.setLeft(sym);
        Declarator.append(d2, init);
        return d2;
    }

    public int getType() {
        return this.varType;
    }

    public int getArrayDim() {
        return this.arrayDim;
    }

    public void addArrayDim(int d2) {
        this.arrayDim += d2;
    }

    public String getClassName() {
        return this.qualifiedClass;
    }

    public void setClassName(String s2) {
        this.qualifiedClass = s2;
    }

    public Symbol getVariable() {
        return (Symbol)this.getLeft();
    }

    public void setVariable(Symbol sym) {
        this.setLeft(sym);
    }

    public ASTree getInitializer() {
        ASTList t2 = this.tail();
        if (t2 != null) {
            return t2.head();
        }
        return null;
    }

    public void setLocalVar(int n2) {
        this.localVar = n2;
    }

    public int getLocalVar() {
        return this.localVar;
    }

    @Override
    public String getTag() {
        return "decl";
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atDeclarator(this);
    }

    public static String astToClassName(ASTList name, char sep) {
        if (name == null) {
            return null;
        }
        StringBuffer sbuf = new StringBuffer();
        Declarator.astToClassName(sbuf, name, sep);
        return sbuf.toString();
    }

    private static void astToClassName(StringBuffer sbuf, ASTList name, char sep) {
        while (true) {
            ASTree h2;
            if ((h2 = name.head()) instanceof Symbol) {
                sbuf.append(((Symbol)h2).get());
            } else if (h2 instanceof ASTList) {
                Declarator.astToClassName(sbuf, (ASTList)h2, sep);
            }
            name = name.tail();
            if (name == null) break;
            sbuf.append(sep);
        }
    }
}

