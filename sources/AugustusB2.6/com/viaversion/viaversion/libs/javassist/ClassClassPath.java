// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist;

import java.net.URL;
import java.io.InputStream;

public class ClassClassPath implements ClassPath
{
    private Class<?> thisClass;
    
    public ClassClassPath(final Class<?> c) {
        this.thisClass = c;
    }
    
    ClassClassPath() {
        this(Object.class);
    }
    
    @Override
    public InputStream openClassfile(final String classname) throws NotFoundException {
        final String filename = '/' + classname.replace('.', '/') + ".class";
        return this.thisClass.getResourceAsStream(filename);
    }
    
    @Override
    public URL find(final String classname) {
        final String filename = '/' + classname.replace('.', '/') + ".class";
        return this.thisClass.getResource(filename);
    }
    
    @Override
    public String toString() {
        return this.thisClass.getName() + ".class";
    }
}
