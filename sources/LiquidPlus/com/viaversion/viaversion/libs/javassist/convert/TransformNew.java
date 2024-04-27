/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.convert;

import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.bytecode.Descriptor;
import com.viaversion.viaversion.libs.javassist.bytecode.StackMap;
import com.viaversion.viaversion.libs.javassist.bytecode.StackMapTable;
import com.viaversion.viaversion.libs.javassist.convert.Transformer;

public final class TransformNew
extends Transformer {
    private int nested;
    private String classname;
    private String trapClass;
    private String trapMethod;

    public TransformNew(Transformer next, String classname, String trapClass, String trapMethod) {
        super(next);
        this.classname = classname;
        this.trapClass = trapClass;
        this.trapMethod = trapMethod;
    }

    @Override
    public void initialize(ConstPool cp, CodeAttribute attr) {
        this.nested = 0;
    }

    @Override
    public int transform(CtClass clazz, int pos, CodeIterator iterator2, ConstPool cp) throws CannotCompileException {
        int index;
        int typedesc;
        int c = iterator2.byteAt(pos);
        if (c == 187) {
            int index2 = iterator2.u16bitAt(pos + 1);
            if (cp.getClassInfo(index2).equals(this.classname)) {
                StackMap sm;
                if (iterator2.byteAt(pos + 3) != 89) {
                    throw new CannotCompileException("NEW followed by no DUP was found");
                }
                iterator2.writeByte(0, pos);
                iterator2.writeByte(0, pos + 1);
                iterator2.writeByte(0, pos + 2);
                iterator2.writeByte(0, pos + 3);
                ++this.nested;
                StackMapTable smt = (StackMapTable)iterator2.get().getAttribute("StackMapTable");
                if (smt != null) {
                    smt.removeNew(pos);
                }
                if ((sm = (StackMap)iterator2.get().getAttribute("StackMap")) != null) {
                    sm.removeNew(pos);
                }
            }
        } else if (c == 183 && (typedesc = cp.isConstructor(this.classname, index = iterator2.u16bitAt(pos + 1))) != 0 && this.nested > 0) {
            int methodref = this.computeMethodref(typedesc, cp);
            iterator2.writeByte(184, pos);
            iterator2.write16bit(methodref, pos + 1);
            --this.nested;
        }
        return pos;
    }

    private int computeMethodref(int typedesc, ConstPool cp) {
        int classIndex = cp.addClassInfo(this.trapClass);
        int mnameIndex = cp.addUtf8Info(this.trapMethod);
        typedesc = cp.addUtf8Info(Descriptor.changeReturnType(this.classname, cp.getUtf8Info(typedesc)));
        return cp.addMethodrefInfo(classIndex, cp.addNameAndTypeInfo(mnameIndex, typedesc));
    }
}

