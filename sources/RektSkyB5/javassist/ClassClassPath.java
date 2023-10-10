/*
 * Decompiled with CFR 0.152.
 */
package javassist;

import java.io.InputStream;
import java.net.URL;
import javassist.ClassPath;
import javassist.NotFoundException;

public class ClassClassPath
implements ClassPath {
    private Class<?> thisClass;

    public ClassClassPath(Class<?> c2) {
        this.thisClass = c2;
    }

    ClassClassPath() {
        this(Object.class);
    }

    @Override
    public InputStream openClassfile(String classname) throws NotFoundException {
        String filename = '/' + classname.replace('.', '/') + ".class";
        return this.thisClass.getResourceAsStream(filename);
    }

    @Override
    public URL find(String classname) {
        String filename = '/' + classname.replace('.', '/') + ".class";
        return this.thisClass.getResource(filename);
    }

    public String toString() {
        return this.thisClass.getName() + ".class";
    }
}

