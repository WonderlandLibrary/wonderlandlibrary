/*
 * Decompiled with CFR 0.152.
 */
package javassist.expr;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.expr.Cast;
import javassist.expr.ConstructorCall;
import javassist.expr.Expr;
import javassist.expr.FieldAccess;
import javassist.expr.Handler;
import javassist.expr.Instanceof;
import javassist.expr.MethodCall;
import javassist.expr.NewArray;
import javassist.expr.NewExpr;

public class ExprEditor {
    public boolean doit(CtClass clazz, MethodInfo minfo) throws CannotCompileException {
        CodeAttribute codeAttr = minfo.getCodeAttribute();
        if (codeAttr == null) {
            return false;
        }
        CodeIterator iterator = codeAttr.iterator();
        boolean edited = false;
        LoopContext context = new LoopContext(codeAttr.getMaxLocals());
        while (iterator.hasNext()) {
            if (!this.loopBody(iterator, clazz, minfo, context)) continue;
            edited = true;
        }
        ExceptionTable et = codeAttr.getExceptionTable();
        int n2 = et.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Handler h2 = new Handler(et, i2, iterator, clazz, minfo);
            this.edit(h2);
            if (!h2.edited()) continue;
            edited = true;
            context.updateMax(h2.locals(), h2.stack());
        }
        if (codeAttr.getMaxLocals() < context.maxLocals) {
            codeAttr.setMaxLocals(context.maxLocals);
        }
        codeAttr.setMaxStack(codeAttr.getMaxStack() + context.maxStack);
        try {
            if (edited) {
                minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz.getClassFile2());
            }
        }
        catch (BadBytecode b2) {
            throw new CannotCompileException(b2.getMessage(), b2);
        }
        return edited;
    }

    boolean doit(CtClass clazz, MethodInfo minfo, LoopContext context, CodeIterator iterator, int endPos) throws CannotCompileException {
        boolean edited = false;
        while (iterator.hasNext() && iterator.lookAhead() < endPos) {
            int size = iterator.getCodeLength();
            if (!this.loopBody(iterator, clazz, minfo, context)) continue;
            edited = true;
            int size2 = iterator.getCodeLength();
            if (size == size2) continue;
            endPos += size2 - size;
        }
        return edited;
    }

    final boolean loopBody(CodeIterator iterator, CtClass clazz, MethodInfo minfo, LoopContext context) throws CannotCompileException {
        try {
            Expr expr = null;
            int pos = iterator.next();
            int c2 = iterator.byteAt(pos);
            if (c2 >= 178) {
                if (c2 < 188) {
                    if (c2 == 184 || c2 == 185 || c2 == 182) {
                        expr = new MethodCall(pos, iterator, clazz, minfo);
                        this.edit((MethodCall)expr);
                    } else if (c2 == 180 || c2 == 178 || c2 == 181 || c2 == 179) {
                        expr = new FieldAccess(pos, iterator, clazz, minfo, c2);
                        this.edit((FieldAccess)expr);
                    } else if (c2 == 187) {
                        int index = iterator.u16bitAt(pos + 1);
                        context.newList = new NewOp(context.newList, pos, minfo.getConstPool().getClassInfo(index));
                    } else if (c2 == 183) {
                        NewOp newList = context.newList;
                        if (newList != null && minfo.getConstPool().isConstructor(newList.type, iterator.u16bitAt(pos + 1)) > 0) {
                            expr = new NewExpr(pos, iterator, clazz, minfo, newList.type, newList.pos);
                            this.edit((NewExpr)expr);
                            context.newList = newList.next;
                        } else {
                            MethodCall mcall = new MethodCall(pos, iterator, clazz, minfo);
                            if (mcall.getMethodName().equals("<init>")) {
                                ConstructorCall ccall = new ConstructorCall(pos, iterator, clazz, minfo);
                                expr = ccall;
                                this.edit(ccall);
                            } else {
                                expr = mcall;
                                this.edit(mcall);
                            }
                        }
                    }
                } else if (c2 == 188 || c2 == 189 || c2 == 197) {
                    expr = new NewArray(pos, iterator, clazz, minfo, c2);
                    this.edit((NewArray)expr);
                } else if (c2 == 193) {
                    expr = new Instanceof(pos, iterator, clazz, minfo);
                    this.edit((Instanceof)expr);
                } else if (c2 == 192) {
                    expr = new Cast(pos, iterator, clazz, minfo);
                    this.edit((Cast)expr);
                }
            }
            if (expr != null && expr.edited()) {
                context.updateMax(expr.locals(), expr.stack());
                return true;
            }
            return false;
        }
        catch (BadBytecode e2) {
            throw new CannotCompileException(e2);
        }
    }

    public void edit(NewExpr e2) throws CannotCompileException {
    }

    public void edit(NewArray a2) throws CannotCompileException {
    }

    public void edit(MethodCall m2) throws CannotCompileException {
    }

    public void edit(ConstructorCall c2) throws CannotCompileException {
    }

    public void edit(FieldAccess f2) throws CannotCompileException {
    }

    public void edit(Instanceof i2) throws CannotCompileException {
    }

    public void edit(Cast c2) throws CannotCompileException {
    }

    public void edit(Handler h2) throws CannotCompileException {
    }

    static final class LoopContext {
        NewOp newList;
        int maxLocals;
        int maxStack;

        LoopContext(int locals) {
            this.maxLocals = locals;
            this.maxStack = 0;
            this.newList = null;
        }

        void updateMax(int locals, int stack) {
            if (this.maxLocals < locals) {
                this.maxLocals = locals;
            }
            if (this.maxStack < stack) {
                this.maxStack = stack;
            }
        }
    }

    static final class NewOp {
        NewOp next;
        int pos;
        String type;

        NewOp(NewOp n2, int p2, String t2) {
            this.next = n2;
            this.pos = p2;
            this.type = t2;
        }
    }
}

