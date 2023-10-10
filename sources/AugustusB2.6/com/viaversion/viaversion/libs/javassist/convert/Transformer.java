// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.convert;

import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;
import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.bytecode.MethodInfo;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.bytecode.Opcode;

public abstract class Transformer implements Opcode
{
    private Transformer next;
    
    public Transformer(final Transformer t) {
        this.next = t;
    }
    
    public Transformer getNext() {
        return this.next;
    }
    
    public void initialize(final ConstPool cp, final CodeAttribute attr) {
    }
    
    public void initialize(final ConstPool cp, final CtClass clazz, final MethodInfo minfo) throws CannotCompileException {
        this.initialize(cp, minfo.getCodeAttribute());
    }
    
    public void clean() {
    }
    
    public abstract int transform(final CtClass p0, final int p1, final CodeIterator p2, final ConstPool p3) throws CannotCompileException, BadBytecode;
    
    public int extraLocals() {
        return 0;
    }
    
    public int extraStack() {
        return 0;
    }
}
