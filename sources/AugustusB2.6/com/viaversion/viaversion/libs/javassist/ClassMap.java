// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist;

import com.viaversion.viaversion.libs.javassist.bytecode.Descriptor;
import java.util.HashMap;

public class ClassMap extends HashMap<String, String>
{
    private static final long serialVersionUID = 1L;
    private ClassMap parent;
    
    public ClassMap() {
        this.parent = null;
    }
    
    ClassMap(final ClassMap map) {
        this.parent = map;
    }
    
    public void put(final CtClass oldname, final CtClass newname) {
        this.put(oldname.getName(), newname.getName());
    }
    
    @Override
    public String put(final String oldname, final String newname) {
        if (oldname == newname) {
            return oldname;
        }
        final String oldname2 = toJvmName(oldname);
        final String s = this.get(oldname2);
        if (s == null || !s.equals(oldname2)) {
            return super.put(oldname2, toJvmName(newname));
        }
        return s;
    }
    
    public void putIfNone(final String oldname, final String newname) {
        if (oldname == newname) {
            return;
        }
        final String oldname2 = toJvmName(oldname);
        final String s = this.get(oldname2);
        if (s == null) {
            super.put(oldname2, toJvmName(newname));
        }
    }
    
    protected final String put0(final String oldname, final String newname) {
        return super.put(oldname, newname);
    }
    
    @Override
    public String get(final Object jvmClassName) {
        final String found = super.get(jvmClassName);
        if (found == null && this.parent != null) {
            return this.parent.get(jvmClassName);
        }
        return found;
    }
    
    public void fix(final CtClass clazz) {
        this.fix(clazz.getName());
    }
    
    public void fix(final String name) {
        final String name2 = toJvmName(name);
        super.put(name2, name2);
    }
    
    public static String toJvmName(final String classname) {
        return Descriptor.toJvmName(classname);
    }
    
    public static String toJavaName(final String classname) {
        return Descriptor.toJavaName(classname);
    }
}
