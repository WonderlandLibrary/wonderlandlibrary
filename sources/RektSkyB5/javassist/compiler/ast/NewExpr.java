/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.Visitor;

public class NewExpr
extends ASTList
implements TokenId {
    private static final long serialVersionUID = 1L;
    protected boolean newArray;
    protected int arrayType;

    public NewExpr(ASTList className, ASTList args) {
        super(className, new ASTList(args));
        this.newArray = false;
        this.arrayType = 307;
    }

    public NewExpr(int type, ASTList arraySize, ArrayInit init) {
        super(null, new ASTList(arraySize));
        this.newArray = true;
        this.arrayType = type;
        if (init != null) {
            NewExpr.append(this, init);
        }
    }

    public static NewExpr makeObjectArray(ASTList className, ASTList arraySize, ArrayInit init) {
        NewExpr e2 = new NewExpr(className, arraySize);
        e2.newArray = true;
        if (init != null) {
            NewExpr.append(e2, init);
        }
        return e2;
    }

    public boolean isArray() {
        return this.newArray;
    }

    public int getArrayType() {
        return this.arrayType;
    }

    public ASTList getClassName() {
        return (ASTList)this.getLeft();
    }

    public ASTList getArguments() {
        return (ASTList)this.getRight().getLeft();
    }

    public ASTList getArraySize() {
        return this.getArguments();
    }

    public ArrayInit getInitializer() {
        ASTree t2 = this.getRight().getRight();
        if (t2 == null) {
            return null;
        }
        return (ArrayInit)t2.getLeft();
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atNewExpr(this);
    }

    @Override
    protected String getTag() {
        return this.newArray ? "new[]" : "new";
    }
}

