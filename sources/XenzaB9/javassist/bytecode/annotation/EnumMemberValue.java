// 
// Decompiled by Procyon v0.6.0
// 

package javassist.bytecode.annotation;

import java.io.IOException;
import java.util.Map;
import javassist.bytecode.Descriptor;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class EnumMemberValue extends MemberValue
{
    int typeIndex;
    int valueIndex;
    
    public EnumMemberValue(final int type, final int value, final ConstPool cp) {
        super('e', cp);
        this.typeIndex = type;
        this.valueIndex = value;
    }
    
    public EnumMemberValue(final ConstPool cp) {
        super('e', cp);
        final int n = 0;
        this.valueIndex = n;
        this.typeIndex = n;
    }
    
    @Override
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) throws ClassNotFoundException {
        try {
            return this.getType(cl).getField(this.getValue()).get(null);
        }
        catch (final NoSuchFieldException e) {
            throw new ClassNotFoundException(this.getType() + "." + this.getValue());
        }
        catch (final IllegalAccessException e2) {
            throw new ClassNotFoundException(this.getType() + "." + this.getValue());
        }
    }
    
    @Override
    Class<?> getType(final ClassLoader cl) throws ClassNotFoundException {
        return MemberValue.loadClass(cl, this.getType());
    }
    
    @Override
    public void renameClass(final String oldname, final String newname) {
        final String type = this.cp.getUtf8Info(this.typeIndex);
        final String newType = Descriptor.rename(type, oldname, newname);
        this.setType(Descriptor.toClassName(newType));
    }
    
    @Override
    public void renameClass(final Map<String, String> classnames) {
        final String type = this.cp.getUtf8Info(this.typeIndex);
        final String newType = Descriptor.rename(type, classnames);
        this.setType(Descriptor.toClassName(newType));
    }
    
    public String getType() {
        return Descriptor.toClassName(this.cp.getUtf8Info(this.typeIndex));
    }
    
    public void setType(final String typename) {
        this.typeIndex = this.cp.addUtf8Info(Descriptor.of(typename));
    }
    
    public String getValue() {
        return this.cp.getUtf8Info(this.valueIndex);
    }
    
    public void setValue(final String name) {
        this.valueIndex = this.cp.addUtf8Info(name);
    }
    
    @Override
    public String toString() {
        return this.getType() + "." + this.getValue();
    }
    
    @Override
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.enumConstValue(this.cp.getUtf8Info(this.typeIndex), this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitEnumMemberValue(this);
    }
}
