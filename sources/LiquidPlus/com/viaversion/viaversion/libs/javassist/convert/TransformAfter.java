/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.convert;

import com.viaversion.viaversion.libs.javassist.CtMethod;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;
import com.viaversion.viaversion.libs.javassist.convert.TransformBefore;
import com.viaversion.viaversion.libs.javassist.convert.Transformer;

public class TransformAfter
extends TransformBefore {
    public TransformAfter(Transformer next, CtMethod origMethod, CtMethod afterMethod) throws NotFoundException {
        super(next, origMethod, afterMethod);
    }

    @Override
    protected int match2(int pos, CodeIterator iterator2) throws BadBytecode {
        iterator2.move(pos);
        iterator2.insert(this.saveCode);
        iterator2.insert(this.loadCode);
        int p = iterator2.insertGap(3);
        iterator2.setMark(p);
        iterator2.insert(this.loadCode);
        pos = iterator2.next();
        p = iterator2.getMark();
        iterator2.writeByte(iterator2.byteAt(pos), p);
        iterator2.write16bit(iterator2.u16bitAt(pos + 1), p + 1);
        iterator2.writeByte(184, pos);
        iterator2.write16bit(this.newIndex, pos + 1);
        iterator2.move(p);
        return iterator2.next();
    }
}

