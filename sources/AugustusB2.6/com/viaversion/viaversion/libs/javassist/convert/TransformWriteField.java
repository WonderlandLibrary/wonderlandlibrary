// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.convert;

import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.CtField;

public final class TransformWriteField extends TransformReadField
{
    public TransformWriteField(final Transformer next, final CtField field, final String methodClassname, final String methodName) {
        super(next, field, methodClassname, methodName);
    }
    
    @Override
    public int transform(final CtClass tclazz, int pos, final CodeIterator iterator, final ConstPool cp) throws BadBytecode {
        final int c = iterator.byteAt(pos);
        if (c == 181 || c == 179) {
            final int index = iterator.u16bitAt(pos + 1);
            final String typedesc = TransformReadField.isField(tclazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
            if (typedesc != null) {
                if (c == 179) {
                    final CodeAttribute ca = iterator.get();
                    iterator.move(pos);
                    final char c2 = typedesc.charAt(0);
                    if (c2 == 'J' || c2 == 'D') {
                        pos = iterator.insertGap(3);
                        iterator.writeByte(1, pos);
                        iterator.writeByte(91, pos + 1);
                        iterator.writeByte(87, pos + 2);
                        ca.setMaxStack(ca.getMaxStack() + 2);
                    }
                    else {
                        pos = iterator.insertGap(2);
                        iterator.writeByte(1, pos);
                        iterator.writeByte(95, pos + 1);
                        ca.setMaxStack(ca.getMaxStack() + 1);
                    }
                    pos = iterator.next();
                }
                final int mi = cp.addClassInfo(this.methodClassname);
                final String type = "(Ljava/lang/Object;" + typedesc + ")V";
                final int methodref = cp.addMethodrefInfo(mi, this.methodName, type);
                iterator.writeByte(184, pos);
                iterator.write16bit(methodref, pos + 1);
            }
        }
        return pos;
    }
}
