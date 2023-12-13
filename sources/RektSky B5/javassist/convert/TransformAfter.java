/*
 * Decompiled with CFR 0.152.
 */
package javassist.convert;

import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.convert.TransformBefore;
import javassist.convert.Transformer;

public class TransformAfter
extends TransformBefore {
    public TransformAfter(Transformer next, CtMethod origMethod, CtMethod afterMethod) throws NotFoundException {
        super(next, origMethod, afterMethod);
    }

    @Override
    protected int match2(int pos, CodeIterator iterator) throws BadBytecode {
        iterator.move(pos);
        iterator.insert(this.saveCode);
        iterator.insert(this.loadCode);
        int p2 = iterator.insertGap(3);
        iterator.setMark(p2);
        iterator.insert(this.loadCode);
        pos = iterator.next();
        p2 = iterator.getMark();
        iterator.writeByte(iterator.byteAt(pos), p2);
        iterator.write16bit(iterator.u16bitAt(pos + 1), p2 + 1);
        iterator.writeByte(184, pos);
        iterator.write16bit(this.newIndex, pos + 1);
        iterator.move(p2);
        return iterator.next();
    }
}

