/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.AnnotationsWriter;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.MemberValueVisitor;

public class LongMemberValue
extends MemberValue {
    int valueIndex;

    public LongMemberValue(int index, ConstPool cp) {
        super('J', cp);
        this.valueIndex = index;
    }

    public LongMemberValue(long j2, ConstPool cp) {
        super('J', cp);
        this.setValue(j2);
    }

    public LongMemberValue(ConstPool cp) {
        super('J', cp);
        this.setValue(0L);
    }

    @Override
    Object getValue(ClassLoader cl, ClassPool cp, Method m2) {
        return this.getValue();
    }

    @Override
    Class<?> getType(ClassLoader cl) {
        return Long.TYPE;
    }

    public long getValue() {
        return this.cp.getLongInfo(this.valueIndex);
    }

    public void setValue(long newValue) {
        this.valueIndex = this.cp.addLongInfo(newValue);
    }

    public String toString() {
        return Long.toString(this.getValue());
    }

    @Override
    public void write(AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor visitor) {
        visitor.visitLongMemberValue(this);
    }
}

