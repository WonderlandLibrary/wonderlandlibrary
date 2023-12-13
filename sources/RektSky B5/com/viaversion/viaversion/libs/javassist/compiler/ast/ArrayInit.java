/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.compiler.ast;

import com.viaversion.viaversion.libs.javassist.compiler.CompileError;
import com.viaversion.viaversion.libs.javassist.compiler.ast.ASTList;
import com.viaversion.viaversion.libs.javassist.compiler.ast.ASTree;
import com.viaversion.viaversion.libs.javassist.compiler.ast.Visitor;

public class ArrayInit
extends ASTList {
    private static final long serialVersionUID = 1L;

    public ArrayInit(ASTree firstElement) {
        super(firstElement);
    }

    public int size() {
        int s2 = this.length();
        if (s2 == 1 && this.head() == null) {
            return 0;
        }
        return s2;
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atArrayInit(this);
    }

    @Override
    public String getTag() {
        return "array";
    }
}

