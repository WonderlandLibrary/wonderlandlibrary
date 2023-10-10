// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist;

import java.io.IOException;
import java.util.Iterator;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;
import java.io.InputStream;
import java.net.URL;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.security.ProtectionDomain;
import java.util.Vector;
import java.util.HashMap;

public class Loader extends ClassLoader
{
    private HashMap<String, ClassLoader> notDefinedHere;
    private Vector<String> notDefinedPackages;
    private ClassPool source;
    private Translator translator;
    private ProtectionDomain domain;
    public boolean doDelegation;
    
    public Loader() {
        this((ClassPool)null);
    }
    
    public Loader(final ClassPool cp) {
        this.doDelegation = true;
        this.init(cp);
    }
    
    public Loader(final ClassLoader parent, final ClassPool cp) {
        super(parent);
        this.doDelegation = true;
        this.init(cp);
    }
    
    private void init(final ClassPool cp) {
        this.notDefinedHere = new HashMap<String, ClassLoader>();
        this.notDefinedPackages = new Vector<String>();
        this.source = cp;
        this.translator = null;
        this.domain = null;
        this.delegateLoadingOf("com.viaversion.viaversion.libs.javassist.Loader");
    }
    
    public void delegateLoadingOf(final String classname) {
        if (classname.endsWith(".")) {
            this.notDefinedPackages.addElement(classname);
        }
        else {
            this.notDefinedHere.put(classname, this);
        }
    }
    
    public void setDomain(final ProtectionDomain d) {
        this.domain = d;
    }
    
    public void setClassPool(final ClassPool cp) {
        this.source = cp;
    }
    
    public void addTranslator(final ClassPool cp, final Translator t) throws NotFoundException, CannotCompileException {
        this.source = cp;
        (this.translator = t).start(cp);
    }
    
    public static void main(final String[] args) throws Throwable {
        final Loader cl = new Loader();
        cl.run(args);
    }
    
    public void run(final String[] args) throws Throwable {
        if (args.length >= 1) {
            this.run(args[0], Arrays.copyOfRange(args, 1, args.length));
        }
    }
    
    public void run(final String classname, final String[] args) throws Throwable {
        final Class<?> c = this.loadClass(classname);
        try {
            c.getDeclaredMethod("main", String[].class).invoke(null, args);
        }
        catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
    
    @Override
    protected Class<?> loadClass(String name, final boolean resolve) throws ClassFormatError, ClassNotFoundException {
        name = name.intern();
        synchronized (name) {
            Class<?> c = this.findLoadedClass(name);
            if (c == null) {
                c = this.loadClassByDelegation(name);
            }
            if (c == null) {
                c = this.findClass(name);
            }
            if (c == null) {
                c = this.delegateToParent(name);
            }
            if (resolve) {
                this.resolveClass(c);
            }
            return c;
        }
    }
    
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        byte[] classfile = null;
        try {
            Label_0101: {
                if (this.source != null) {
                    if (this.translator != null) {
                        this.translator.onLoad(this.source, name);
                    }
                    try {
                        classfile = this.source.get(name).toBytecode();
                        break Label_0101;
                    }
                    catch (NotFoundException e2) {
                        return null;
                    }
                }
                final String jarname = "/" + name.replace('.', '/') + ".class";
                final InputStream in = this.getClass().getResourceAsStream(jarname);
                if (in == null) {
                    return null;
                }
                classfile = ClassPoolTail.readStream(in);
            }
        }
        catch (Exception e) {
            throw new ClassNotFoundException("caught an exception while obtaining a class file for " + name, e);
        }
        final int i = name.lastIndexOf(46);
        if (i != -1) {
            final String pname = name.substring(0, i);
            if (this.isDefinedPackage(pname)) {
                try {
                    this.definePackage(pname, null, null, null, null, null, null, null);
                }
                catch (IllegalArgumentException ex) {}
            }
        }
        if (this.domain == null) {
            return this.defineClass(name, classfile, 0, classfile.length);
        }
        return this.defineClass(name, classfile, 0, classfile.length, this.domain);
    }
    
    private boolean isDefinedPackage(final String name) {
        if (ClassFile.MAJOR_VERSION >= 53) {
            return this.getDefinedPackage(name) == null;
        }
        return this.getPackage(name) == null;
    }
    
    protected Class<?> loadClassByDelegation(final String name) throws ClassNotFoundException {
        Class<?> c = null;
        if (this.doDelegation && (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("sun.") || name.startsWith("com.sun.") || name.startsWith("org.w3c.") || name.startsWith("org.xml.") || this.notDelegated(name))) {
            c = this.delegateToParent(name);
        }
        return c;
    }
    
    private boolean notDelegated(final String name) {
        if (this.notDefinedHere.containsKey(name)) {
            return true;
        }
        for (final String pack : this.notDefinedPackages) {
            if (name.startsWith(pack)) {
                return true;
            }
        }
        return false;
    }
    
    protected Class<?> delegateToParent(final String classname) throws ClassNotFoundException {
        final ClassLoader cl = this.getParent();
        if (cl != null) {
            return cl.loadClass(classname);
        }
        return this.findSystemClass(classname);
    }
    
    public static class Simple extends ClassLoader
    {
        public Simple() {
        }
        
        public Simple(final ClassLoader parent) {
            super(parent);
        }
        
        public Class<?> invokeDefineClass(final CtClass cc) throws IOException, CannotCompileException {
            final byte[] code = cc.toBytecode();
            return this.defineClass(cc.getName(), code, 0, code.length);
        }
    }
}
