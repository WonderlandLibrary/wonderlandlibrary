// 
// Decompiled by Procyon v0.5.36
// 

package javassist;

import java.net.URL;
import java.io.InputStream;

public class ClassClassPath implements ClassPath
{
    private Class thisClass;
    
    public ClassClassPath(final Class c) {
        this.thisClass = c;
    }
    
    ClassClassPath() {
        this(Object.class);
    }
    
    public InputStream openClassfile(final String classname) {
        final String jarname = "/" + classname.replace('.', '/') + ".class";
        return this.thisClass.getResourceAsStream(jarname);
    }
    
    public URL find(final String classname) {
        final String jarname = "/" + classname.replace('.', '/') + ".class";
        return this.thisClass.getResource(jarname);
    }
    
    public void close() {
    }
    
    public String toString() {
        return this.thisClass.getName() + ".class";
    }
}
