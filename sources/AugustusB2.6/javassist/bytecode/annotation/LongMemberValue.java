// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class LongMemberValue extends MemberValue
{
    int valueIndex;
    
    public LongMemberValue(final int index, final ConstPool cp) {
        super('J', cp);
        this.valueIndex = index;
    }
    
    public LongMemberValue(final long j, final ConstPool cp) {
        super('J', cp);
        this.setValue(j);
    }
    
    public LongMemberValue(final ConstPool cp) {
        super('J', cp);
        this.setValue(0L);
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) {
        return new Long(this.getValue());
    }
    
    Class getType(final ClassLoader cl) {
        return Long.TYPE;
    }
    
    public long getValue() {
        return this.cp.getLongInfo(this.valueIndex);
    }
    
    public void setValue(final long newValue) {
        this.valueIndex = this.cp.addLongInfo(newValue);
    }
    
    public String toString() {
        return Long.toString(this.getValue());
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(this.getValue());
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitLongMemberValue(this);
    }
}
