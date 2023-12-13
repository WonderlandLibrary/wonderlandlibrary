/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Visitor;

public class ASTList
extends ASTree {
    private static final long serialVersionUID = 1L;
    private ASTree left;
    private ASTList right;

    public ASTList(ASTree _head, ASTList _tail) {
        this.left = _head;
        this.right = _tail;
    }

    public ASTList(ASTree _head) {
        this.left = _head;
        this.right = null;
    }

    public static ASTList make(ASTree e1, ASTree e2, ASTree e3) {
        return new ASTList(e1, new ASTList(e2, new ASTList(e3)));
    }

    @Override
    public ASTree getLeft() {
        return this.left;
    }

    @Override
    public ASTree getRight() {
        return this.right;
    }

    @Override
    public void setLeft(ASTree _left) {
        this.left = _left;
    }

    @Override
    public void setRight(ASTree _right) {
        this.right = (ASTList)_right;
    }

    public ASTree head() {
        return this.left;
    }

    public void setHead(ASTree _head) {
        this.left = _head;
    }

    public ASTList tail() {
        return this.right;
    }

    public void setTail(ASTList _tail) {
        this.right = _tail;
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atASTList(this);
    }

    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("(<");
        sbuf.append(this.getTag());
        sbuf.append('>');
        ASTList list = this;
        while (list != null) {
            sbuf.append(' ');
            ASTree a2 = list.left;
            sbuf.append(a2 == null ? "<null>" : a2.toString());
            list = list.right;
        }
        sbuf.append(')');
        return sbuf.toString();
    }

    public int length() {
        return ASTList.length(this);
    }

    public static int length(ASTList list) {
        if (list == null) {
            return 0;
        }
        int n2 = 0;
        while (list != null) {
            list = list.right;
            ++n2;
        }
        return n2;
    }

    public ASTList sublist(int nth) {
        ASTList list = this;
        while (nth-- > 0) {
            list = list.right;
        }
        return list;
    }

    public boolean subst(ASTree newObj, ASTree oldObj) {
        ASTList list = this;
        while (list != null) {
            if (list.left == oldObj) {
                list.left = newObj;
                return true;
            }
            list = list.right;
        }
        return false;
    }

    public static ASTList append(ASTList a2, ASTree b2) {
        return ASTList.concat(a2, new ASTList(b2));
    }

    public static ASTList concat(ASTList a2, ASTList b2) {
        if (a2 == null) {
            return b2;
        }
        ASTList list = a2;
        while (list.right != null) {
            list = list.right;
        }
        list.right = b2;
        return a2;
    }
}

