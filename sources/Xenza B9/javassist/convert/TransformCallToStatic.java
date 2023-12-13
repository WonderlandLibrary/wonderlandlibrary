// 
// Decompiled by Procyon v0.6.0
// 

package javassist.convert;

import javassist.bytecode.Descriptor;
import javassist.bytecode.ConstPool;
import javassist.bytecode.CodeIterator;
import javassist.CtMethod;

public class TransformCallToStatic extends TransformCall
{
    public TransformCallToStatic(final Transformer next, final CtMethod origMethod, final CtMethod substMethod) {
        super(next, origMethod, substMethod);
        this.methodDescriptor = origMethod.getMethodInfo2().getDescriptor();
    }
    
    @Override
    protected int match(final int c, final int pos, final CodeIterator iterator, final int typedesc, final ConstPool cp) {
        if (this.newIndex == 0) {
            final String desc = Descriptor.insertParameter(this.classname, this.methodDescriptor);
            final int nt = cp.addNameAndTypeInfo(this.newMethodname, desc);
            final int ci = cp.addClassInfo(this.newClassname);
            this.newIndex = cp.addMethodrefInfo(ci, nt);
            this.constPool = cp;
        }
        iterator.writeByte(184, pos);
        iterator.write16bit(this.newIndex, pos + 1);
        return pos;
    }
}
