// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class AnnotationMemberValue extends MemberValue
{
    Annotation value;
    
    public AnnotationMemberValue(final ConstPool cp) {
        this(null, cp);
    }
    
    public AnnotationMemberValue(final Annotation a, final ConstPool cp) {
        super('@', cp);
        this.value = a;
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) throws ClassNotFoundException {
        return AnnotationImpl.make(cl, this.getType(cl), cp, this.value);
    }
    
    Class getType(final ClassLoader cl) throws ClassNotFoundException {
        if (this.value == null) {
            throw new ClassNotFoundException("no type specified");
        }
        return MemberValue.loadClass(cl, this.value.getTypeName());
    }
    
    public Annotation getValue() {
        return this.value;
    }
    
    public void setValue(final Annotation newValue) {
        this.value = newValue;
    }
    
    public String toString() {
        return this.value.toString();
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.annotationValue();
        this.value.write(writer);
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitAnnotationMemberValue(this);
    }
}
