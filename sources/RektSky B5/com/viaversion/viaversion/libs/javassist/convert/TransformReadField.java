/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.convert;

import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.CtField;
import com.viaversion.viaversion.libs.javassist.Modifier;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.convert.Transformer;

public class TransformReadField
extends Transformer {
    protected String fieldname;
    protected CtClass fieldClass;
    protected boolean isPrivate;
    protected String methodClassname;
    protected String methodName;

    public TransformReadField(Transformer next, CtField field, String methodClassname, String methodName) {
        super(next);
        this.fieldClass = field.getDeclaringClass();
        this.fieldname = field.getName();
        this.methodClassname = methodClassname;
        this.methodName = methodName;
        this.isPrivate = Modifier.isPrivate(field.getModifiers());
    }

    static String isField(ClassPool pool, ConstPool cp, CtClass fclass, String fname, boolean is_private, int index) {
        if (!cp.getFieldrefName(index).equals(fname)) {
            return null;
        }
        try {
            CtClass c2 = pool.get(cp.getFieldrefClassName(index));
            if (c2 == fclass || !is_private && TransformReadField.isFieldInSuper(c2, fclass, fname)) {
                return cp.getFieldrefType(index);
            }
        }
        catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return null;
    }

    static boolean isFieldInSuper(CtClass clazz, CtClass fclass, String fname) {
        if (!clazz.subclassOf(fclass)) {
            return false;
        }
        try {
            CtField f2 = clazz.getField(fname);
            return f2.getDeclaringClass() == fclass;
        }
        catch (NotFoundException notFoundException) {
            return false;
        }
    }

    @Override
    public int transform(CtClass tclazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
        int c2 = iterator.byteAt(pos);
        if (c2 == 180 || c2 == 178) {
            int index = iterator.u16bitAt(pos + 1);
            String typedesc = TransformReadField.isField(tclazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
            if (typedesc != null) {
                if (c2 == 178) {
                    iterator.move(pos);
                    pos = iterator.insertGap(1);
                    iterator.writeByte(1, pos);
                    pos = iterator.next();
                }
                String type = "(Ljava/lang/Object;)" + typedesc;
                int mi = cp.addClassInfo(this.methodClassname);
                int methodref = cp.addMethodrefInfo(mi, this.methodName, type);
                iterator.writeByte(184, pos);
                iterator.write16bit(methodref, pos + 1);
                return pos;
            }
        }
        return pos;
    }
}

