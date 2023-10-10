/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist;

import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.ClassMap;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.libs.javassist.CodeConverter;
import com.viaversion.viaversion.libs.javassist.CtBehavior;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.CtConstructor;
import com.viaversion.viaversion.libs.javassist.CtField;
import com.viaversion.viaversion.libs.javassist.CtMember;
import com.viaversion.viaversion.libs.javassist.CtMethod;
import com.viaversion.viaversion.libs.javassist.FieldInitLink;
import com.viaversion.viaversion.libs.javassist.Modifier;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.bytecode.AccessFlag;
import com.viaversion.viaversion.libs.javassist.bytecode.AnnotationsAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.AttributeInfo;
import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.Bytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstantAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.Descriptor;
import com.viaversion.viaversion.libs.javassist.bytecode.EnclosingMethodAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.FieldInfo;
import com.viaversion.viaversion.libs.javassist.bytecode.InnerClassesAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.MethodInfo;
import com.viaversion.viaversion.libs.javassist.bytecode.ParameterAnnotationsAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.SignatureAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.annotation.Annotation;
import com.viaversion.viaversion.libs.javassist.bytecode.annotation.AnnotationImpl;
import com.viaversion.viaversion.libs.javassist.compiler.AccessorMaker;
import com.viaversion.viaversion.libs.javassist.compiler.CompileError;
import com.viaversion.viaversion.libs.javassist.compiler.Javac;
import com.viaversion.viaversion.libs.javassist.expr.ExprEditor;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CtClassType
extends CtClass {
    ClassPool classPool;
    boolean wasChanged;
    private boolean wasFrozen;
    boolean wasPruned;
    boolean gcConstPool;
    ClassFile classfile;
    byte[] rawClassfile;
    private Reference<CtMember.Cache> memberCache;
    private AccessorMaker accessors;
    private FieldInitLink fieldInitializers;
    private Map<CtMethod, String> hiddenMethods;
    private int uniqueNumberSeed;
    private boolean doPruning = ClassPool.doPruning;
    private int getCount;
    private static final int GET_THRESHOLD = 2;

    CtClassType(String name, ClassPool cp) {
        super(name);
        this.classPool = cp;
        this.gcConstPool = false;
        this.wasPruned = false;
        this.wasFrozen = false;
        this.wasChanged = false;
        this.classfile = null;
        this.rawClassfile = null;
        this.memberCache = null;
        this.accessors = null;
        this.fieldInitializers = null;
        this.hiddenMethods = null;
        this.uniqueNumberSeed = 0;
        this.getCount = 0;
    }

    CtClassType(InputStream ins, ClassPool cp) throws IOException {
        this((String)null, cp);
        this.classfile = new ClassFile(new DataInputStream(ins));
        this.qualifiedName = this.classfile.getName();
    }

    CtClassType(ClassFile cf, ClassPool cp) {
        this((String)null, cp);
        this.classfile = cf;
        this.qualifiedName = this.classfile.getName();
    }

    @Override
    protected void extendToString(StringBuffer buffer) {
        if (this.wasChanged) {
            buffer.append("changed ");
        }
        if (this.wasFrozen) {
            buffer.append("frozen ");
        }
        if (this.wasPruned) {
            buffer.append("pruned ");
        }
        buffer.append(Modifier.toString(this.getModifiers()));
        buffer.append(" class ");
        buffer.append(this.getName());
        try {
            String name;
            CtClass ext = this.getSuperclass();
            if (ext != null && !(name = ext.getName()).equals("java.lang.Object")) {
                buffer.append(" extends " + ext.getName());
            }
        }
        catch (NotFoundException e2) {
            buffer.append(" extends ??");
        }
        try {
            CtClass[] intf = this.getInterfaces();
            if (intf.length > 0) {
                buffer.append(" implements ");
            }
            for (int i2 = 0; i2 < intf.length; ++i2) {
                buffer.append(intf[i2].getName());
                buffer.append(", ");
            }
        }
        catch (NotFoundException e3) {
            buffer.append(" extends ??");
        }
        CtMember.Cache memCache = this.getMembers();
        this.exToString(buffer, " fields=", memCache.fieldHead(), memCache.lastField());
        this.exToString(buffer, " constructors=", memCache.consHead(), memCache.lastCons());
        this.exToString(buffer, " methods=", memCache.methodHead(), memCache.lastMethod());
    }

    private void exToString(StringBuffer buffer, String msg, CtMember head, CtMember tail) {
        buffer.append(msg);
        while (head != tail) {
            head = head.next();
            buffer.append(head);
            buffer.append(", ");
        }
    }

    @Override
    public AccessorMaker getAccessorMaker() {
        if (this.accessors == null) {
            this.accessors = new AccessorMaker(this);
        }
        return this.accessors;
    }

    @Override
    public ClassFile getClassFile2() {
        return this.getClassFile3(true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ClassFile getClassFile3(boolean doCompress) {
        byte[] rcfile;
        ClassFile cfile = this.classfile;
        if (cfile != null) {
            return cfile;
        }
        if (doCompress) {
            this.classPool.compress();
        }
        CtClassType ctClassType = this;
        synchronized (ctClassType) {
            cfile = this.classfile;
            if (cfile != null) {
                return cfile;
            }
            rcfile = this.rawClassfile;
        }
        if (rcfile != null) {
            ClassFile cf;
            try {
                cf = new ClassFile(new DataInputStream(new ByteArrayInputStream(rcfile)));
            }
            catch (IOException e2) {
                throw new RuntimeException(e2.toString(), e2);
            }
            this.getCount = 2;
            CtClassType e2 = this;
            synchronized (e2) {
                this.rawClassfile = null;
                return this.setClassFile(cf);
            }
        }
        InputStream fin = null;
        try {
            fin = this.classPool.openClassfile(this.getName());
            if (fin == null) {
                throw new NotFoundException(this.getName());
            }
            ClassFile cf = new ClassFile(new DataInputStream(fin = new BufferedInputStream(fin)));
            if (!cf.getName().equals(this.qualifiedName)) {
                throw new RuntimeException("cannot find " + this.qualifiedName + ": " + cf.getName() + " found in " + this.qualifiedName.replace('.', '/') + ".class");
            }
            ClassFile classFile = this.setClassFile(cf);
            return classFile;
        }
        catch (NotFoundException e3) {
            throw new RuntimeException(e3.toString(), e3);
        }
        catch (IOException e4) {
            throw new RuntimeException(e4.toString(), e4);
        }
        finally {
            if (fin != null) {
                try {
                    fin.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    @Override
    final void incGetCounter() {
        ++this.getCount;
    }

    @Override
    void compress() {
        if (this.getCount < 2) {
            if (!this.isModified() && ClassPool.releaseUnmodifiedClassFile) {
                this.removeClassFile();
            } else if (this.isFrozen() && !this.wasPruned) {
                this.saveClassFile();
            }
        }
        this.getCount = 0;
    }

    private synchronized void saveClassFile() {
        if (this.classfile == null || this.hasMemberCache() != null) {
            return;
        }
        ByteArrayOutputStream barray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(barray);
        try {
            this.classfile.write(out);
            barray.close();
            this.rawClassfile = barray.toByteArray();
            this.classfile = null;
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private synchronized void removeClassFile() {
        if (this.classfile != null && !this.isModified() && this.hasMemberCache() == null) {
            this.classfile = null;
        }
    }

    private synchronized ClassFile setClassFile(ClassFile cf) {
        if (this.classfile == null) {
            this.classfile = cf;
        }
        return this.classfile;
    }

    @Override
    public ClassPool getClassPool() {
        return this.classPool;
    }

    void setClassPool(ClassPool cp) {
        this.classPool = cp;
    }

    @Override
    public URL getURL() throws NotFoundException {
        URL url = this.classPool.find(this.getName());
        if (url == null) {
            throw new NotFoundException(this.getName());
        }
        return url;
    }

    @Override
    public boolean isModified() {
        return this.wasChanged;
    }

    @Override
    public boolean isFrozen() {
        return this.wasFrozen;
    }

    @Override
    public void freeze() {
        this.wasFrozen = true;
    }

    @Override
    void checkModify() throws RuntimeException {
        if (this.isFrozen()) {
            String msg = this.getName() + " class is frozen";
            if (this.wasPruned) {
                msg = msg + " and pruned";
            }
            throw new RuntimeException(msg);
        }
        this.wasChanged = true;
    }

    @Override
    public void defrost() {
        this.checkPruned("defrost");
        this.wasFrozen = false;
    }

    @Override
    public boolean subtypeOf(CtClass clazz) throws NotFoundException {
        int i2;
        String cname = clazz.getName();
        if (this == clazz || this.getName().equals(cname)) {
            return true;
        }
        ClassFile file = this.getClassFile2();
        String supername = file.getSuperclass();
        if (supername != null && supername.equals(cname)) {
            return true;
        }
        String[] ifs = file.getInterfaces();
        int num = ifs.length;
        for (i2 = 0; i2 < num; ++i2) {
            if (!ifs[i2].equals(cname)) continue;
            return true;
        }
        if (supername != null && this.classPool.get(supername).subtypeOf(clazz)) {
            return true;
        }
        for (i2 = 0; i2 < num; ++i2) {
            if (!this.classPool.get(ifs[i2]).subtypeOf(clazz)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void setName(String name) throws RuntimeException {
        String oldname = this.getName();
        if (name.equals(oldname)) {
            return;
        }
        this.classPool.checkNotFrozen(name);
        ClassFile cf = this.getClassFile2();
        super.setName(name);
        cf.setName(name);
        this.nameReplaced();
        this.classPool.classNameChanged(oldname, this);
    }

    @Override
    public String getGenericSignature() {
        SignatureAttribute sa = (SignatureAttribute)this.getClassFile2().getAttribute("Signature");
        return sa == null ? null : sa.getSignature();
    }

    @Override
    public void setGenericSignature(String sig) {
        ClassFile cf = this.getClassFile();
        SignatureAttribute sa = new SignatureAttribute(cf.getConstPool(), sig);
        cf.addAttribute(sa);
    }

    @Override
    public void replaceClassName(ClassMap classnames) throws RuntimeException {
        String oldClassName = this.getName();
        String newClassName = classnames.get(Descriptor.toJvmName(oldClassName));
        if (newClassName != null) {
            newClassName = Descriptor.toJavaName(newClassName);
            this.classPool.checkNotFrozen(newClassName);
        }
        super.replaceClassName(classnames);
        ClassFile cf = this.getClassFile2();
        cf.renameClass(classnames);
        this.nameReplaced();
        if (newClassName != null) {
            super.setName(newClassName);
            this.classPool.classNameChanged(oldClassName, this);
        }
    }

    @Override
    public void replaceClassName(String oldname, String newname) throws RuntimeException {
        String thisname = this.getName();
        if (thisname.equals(oldname)) {
            this.setName(newname);
        } else {
            super.replaceClassName(oldname, newname);
            this.getClassFile2().renameClass(oldname, newname);
            this.nameReplaced();
        }
    }

    @Override
    public boolean isInterface() {
        return Modifier.isInterface(this.getModifiers());
    }

    @Override
    public boolean isAnnotation() {
        return Modifier.isAnnotation(this.getModifiers());
    }

    @Override
    public boolean isEnum() {
        return Modifier.isEnum(this.getModifiers());
    }

    @Override
    public int getModifiers() {
        ClassFile cf = this.getClassFile2();
        int acc = cf.getAccessFlags();
        acc = AccessFlag.clear(acc, 32);
        int inner = cf.getInnerAccessFlags();
        if (inner != -1) {
            if ((inner & 8) != 0) {
                acc |= 8;
            }
            if ((inner & 1) != 0) {
                acc |= 1;
            } else {
                acc &= 0xFFFFFFFE;
                if ((inner & 4) != 0) {
                    acc |= 4;
                } else if ((inner & 2) != 0) {
                    acc |= 2;
                }
            }
        }
        return AccessFlag.toModifier(acc);
    }

    @Override
    public CtClass[] getNestedClasses() throws NotFoundException {
        ClassFile cf = this.getClassFile2();
        InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
        if (ica == null) {
            return new CtClass[0];
        }
        String thisName = cf.getName() + "$";
        int n2 = ica.tableLength();
        ArrayList<CtClass> list = new ArrayList<CtClass>(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            String name = ica.innerClass(i2);
            if (name == null || !name.startsWith(thisName) || name.lastIndexOf(36) >= thisName.length()) continue;
            list.add(this.classPool.get(name));
        }
        return list.toArray(new CtClass[list.size()]);
    }

    @Override
    public void setModifiers(int mod) {
        this.checkModify();
        CtClassType.updateInnerEntry(mod, this.getName(), this, true);
        ClassFile cf = this.getClassFile2();
        cf.setAccessFlags(AccessFlag.of(mod & 0xFFFFFFF7));
    }

    private static void updateInnerEntry(int newMod, String name, CtClass clazz, boolean outer) {
        ClassFile cf = clazz.getClassFile2();
        InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
        if (ica != null) {
            int isStatic;
            int mod = newMod & 0xFFFFFFF7;
            int i2 = ica.find(name);
            if (!(i2 < 0 || (isStatic = ica.accessFlags(i2) & 8) == 0 && Modifier.isStatic(newMod))) {
                clazz.checkModify();
                ica.setAccessFlags(i2, AccessFlag.of(mod) | isStatic);
                String outName = ica.outerClass(i2);
                if (outName != null && outer) {
                    try {
                        CtClass parent = clazz.getClassPool().get(outName);
                        CtClassType.updateInnerEntry(mod, name, parent, false);
                    }
                    catch (NotFoundException e2) {
                        throw new RuntimeException("cannot find the declaring class: " + outName);
                    }
                }
                return;
            }
        }
        if (Modifier.isStatic(newMod)) {
            throw new RuntimeException("cannot change " + Descriptor.toJavaName(name) + " into a static class");
        }
    }

    @Override
    public boolean hasAnnotation(String annotationName) {
        ClassFile cf = this.getClassFile2();
        AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.hasAnnotationType(annotationName, this.getClassPool(), ainfo, ainfo2);
    }

    @Deprecated
    static boolean hasAnnotationType(Class<?> clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
        return CtClassType.hasAnnotationType(clz.getName(), cp, a1, a2);
    }

    static boolean hasAnnotationType(String annotationTypeName, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
        int i2;
        Annotation[] anno1 = a1 == null ? null : a1.getAnnotations();
        Annotation[] anno2 = a2 == null ? null : a2.getAnnotations();
        if (anno1 != null) {
            for (i2 = 0; i2 < anno1.length; ++i2) {
                if (!anno1[i2].getTypeName().equals(annotationTypeName)) continue;
                return true;
            }
        }
        if (anno2 != null) {
            for (i2 = 0; i2 < anno2.length; ++i2) {
                if (!anno2[i2].getTypeName().equals(annotationTypeName)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getAnnotation(Class<?> clz) throws ClassNotFoundException {
        ClassFile cf = this.getClassFile2();
        AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.getAnnotationType(clz, this.getClassPool(), ainfo, ainfo2);
    }

    static Object getAnnotationType(Class<?> clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
        int i2;
        Annotation[] anno1 = a1 == null ? null : a1.getAnnotations();
        Annotation[] anno2 = a2 == null ? null : a2.getAnnotations();
        String typeName = clz.getName();
        if (anno1 != null) {
            for (i2 = 0; i2 < anno1.length; ++i2) {
                if (!anno1[i2].getTypeName().equals(typeName)) continue;
                return CtClassType.toAnnoType(anno1[i2], cp);
            }
        }
        if (anno2 != null) {
            for (i2 = 0; i2 < anno2.length; ++i2) {
                if (!anno2[i2].getTypeName().equals(typeName)) continue;
                return CtClassType.toAnnoType(anno2[i2], cp);
            }
        }
        return null;
    }

    @Override
    public Object[] getAnnotations() throws ClassNotFoundException {
        return this.getAnnotations(false);
    }

    @Override
    public Object[] getAvailableAnnotations() {
        try {
            return this.getAnnotations(true);
        }
        catch (ClassNotFoundException e2) {
            throw new RuntimeException("Unexpected exception ", e2);
        }
    }

    private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
        ClassFile cf = this.getClassFile2();
        AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.toAnnotationType(ignoreNotFound, this.getClassPool(), ainfo, ainfo2);
    }

    static Object[] toAnnotationType(boolean ignoreNotFound, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
        int size2;
        Annotation[] anno2;
        int size1;
        Annotation[] anno1;
        if (a1 == null) {
            anno1 = null;
            size1 = 0;
        } else {
            anno1 = a1.getAnnotations();
            size1 = anno1.length;
        }
        if (a2 == null) {
            anno2 = null;
            size2 = 0;
        } else {
            anno2 = a2.getAnnotations();
            size2 = anno2.length;
        }
        if (!ignoreNotFound) {
            Object[] result = new Object[size1 + size2];
            for (int i2 = 0; i2 < size1; ++i2) {
                result[i2] = CtClassType.toAnnoType(anno1[i2], cp);
            }
            for (int j2 = 0; j2 < size2; ++j2) {
                result[j2 + size1] = CtClassType.toAnnoType(anno2[j2], cp);
            }
            return result;
        }
        ArrayList<Object> annotations = new ArrayList<Object>();
        for (int i3 = 0; i3 < size1; ++i3) {
            try {
                annotations.add(CtClassType.toAnnoType(anno1[i3], cp));
                continue;
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        for (int j3 = 0; j3 < size2; ++j3) {
            try {
                annotations.add(CtClassType.toAnnoType(anno2[j3], cp));
                continue;
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return annotations.toArray();
    }

    static Object[][] toAnnotationType(boolean ignoreNotFound, ClassPool cp, ParameterAnnotationsAttribute a1, ParameterAnnotationsAttribute a2, MethodInfo minfo) throws ClassNotFoundException {
        int numParameters = 0;
        numParameters = a1 != null ? a1.numParameters() : (a2 != null ? a2.numParameters() : Descriptor.numOfParameters(minfo.getDescriptor()));
        Object[][] result = new Object[numParameters][];
        for (int i2 = 0; i2 < numParameters; ++i2) {
            int j2;
            int size2;
            Annotation[] anno2;
            int size1;
            Annotation[] anno1;
            if (a1 == null) {
                anno1 = null;
                size1 = 0;
            } else {
                anno1 = a1.getAnnotations()[i2];
                size1 = anno1.length;
            }
            if (a2 == null) {
                anno2 = null;
                size2 = 0;
            } else {
                anno2 = a2.getAnnotations()[i2];
                size2 = anno2.length;
            }
            if (!ignoreNotFound) {
                int j3;
                result[i2] = new Object[size1 + size2];
                for (j3 = 0; j3 < size1; ++j3) {
                    result[i2][j3] = CtClassType.toAnnoType(anno1[j3], cp);
                }
                for (j3 = 0; j3 < size2; ++j3) {
                    result[i2][j3 + size1] = CtClassType.toAnnoType(anno2[j3], cp);
                }
                continue;
            }
            ArrayList<Object> annotations = new ArrayList<Object>();
            for (j2 = 0; j2 < size1; ++j2) {
                try {
                    annotations.add(CtClassType.toAnnoType(anno1[j2], cp));
                    continue;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
            }
            for (j2 = 0; j2 < size2; ++j2) {
                try {
                    annotations.add(CtClassType.toAnnoType(anno2[j2], cp));
                    continue;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
            }
            result[i2] = annotations.toArray();
        }
        return result;
    }

    private static Object toAnnoType(Annotation anno, ClassPool cp) throws ClassNotFoundException {
        try {
            ClassLoader cl = cp.getClassLoader();
            return anno.toAnnotationType(cl, cp);
        }
        catch (ClassNotFoundException e2) {
            ClassLoader cl2 = cp.getClass().getClassLoader();
            try {
                return anno.toAnnotationType(cl2, cp);
            }
            catch (ClassNotFoundException e22) {
                try {
                    Class<?> clazz = cp.get(anno.getTypeName()).toClass();
                    return AnnotationImpl.make(clazz.getClassLoader(), clazz, cp, anno);
                }
                catch (Throwable e3) {
                    throw new ClassNotFoundException(anno.getTypeName());
                }
            }
        }
    }

    @Override
    public boolean subclassOf(CtClass superclass) {
        if (superclass == null) {
            return false;
        }
        String superName = superclass.getName();
        try {
            for (CtClass curr = this; curr != null; curr = ((CtClass)curr).getSuperclass()) {
                if (!curr.getName().equals(superName)) continue;
                return true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    @Override
    public CtClass getSuperclass() throws NotFoundException {
        String supername = this.getClassFile2().getSuperclass();
        if (supername == null) {
            return null;
        }
        return this.classPool.get(supername);
    }

    @Override
    public void setSuperclass(CtClass clazz) throws CannotCompileException {
        this.checkModify();
        if (this.isInterface()) {
            this.addInterface(clazz);
        } else {
            this.getClassFile2().setSuperclass(clazz.getName());
        }
    }

    @Override
    public CtClass[] getInterfaces() throws NotFoundException {
        String[] ifs = this.getClassFile2().getInterfaces();
        int num = ifs.length;
        CtClass[] ifc = new CtClass[num];
        for (int i2 = 0; i2 < num; ++i2) {
            ifc[i2] = this.classPool.get(ifs[i2]);
        }
        return ifc;
    }

    @Override
    public void setInterfaces(CtClass[] list) {
        String[] ifs;
        this.checkModify();
        if (list == null) {
            ifs = new String[]{};
        } else {
            int num = list.length;
            ifs = new String[num];
            for (int i2 = 0; i2 < num; ++i2) {
                ifs[i2] = list[i2].getName();
            }
        }
        this.getClassFile2().setInterfaces(ifs);
    }

    @Override
    public void addInterface(CtClass anInterface) {
        this.checkModify();
        if (anInterface != null) {
            this.getClassFile2().addInterface(anInterface.getName());
        }
    }

    @Override
    public CtClass getDeclaringClass() throws NotFoundException {
        ClassFile cf = this.getClassFile2();
        InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
        if (ica == null) {
            return null;
        }
        String name = this.getName();
        int n2 = ica.tableLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!name.equals(ica.innerClass(i2))) continue;
            String outName = ica.outerClass(i2);
            if (outName != null) {
                return this.classPool.get(outName);
            }
            EnclosingMethodAttribute ema = (EnclosingMethodAttribute)cf.getAttribute("EnclosingMethod");
            if (ema == null) continue;
            return this.classPool.get(ema.className());
        }
        return null;
    }

    @Override
    public CtBehavior getEnclosingBehavior() throws NotFoundException {
        ClassFile cf = this.getClassFile2();
        EnclosingMethodAttribute ema = (EnclosingMethodAttribute)cf.getAttribute("EnclosingMethod");
        if (ema == null) {
            return null;
        }
        CtClass enc = this.classPool.get(ema.className());
        String name = ema.methodName();
        if ("<init>".equals(name)) {
            return enc.getConstructor(ema.methodDescriptor());
        }
        if ("<clinit>".equals(name)) {
            return enc.getClassInitializer();
        }
        return enc.getMethod(name, ema.methodDescriptor());
    }

    @Override
    public CtClass makeNestedClass(String name, boolean isStatic) {
        if (!isStatic) {
            throw new RuntimeException("sorry, only nested static class is supported");
        }
        this.checkModify();
        CtClass c2 = this.classPool.makeNestedClass(this.getName() + "$" + name);
        ClassFile cf = this.getClassFile2();
        ClassFile cf2 = c2.getClassFile2();
        InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
        if (ica == null) {
            ica = new InnerClassesAttribute(cf.getConstPool());
            cf.addAttribute(ica);
        }
        ica.append(c2.getName(), this.getName(), name, cf2.getAccessFlags() & 0xFFFFFFDF | 8);
        cf2.addAttribute(ica.copy(cf2.getConstPool(), null));
        return c2;
    }

    private void nameReplaced() {
        CtMember.Cache cache = this.hasMemberCache();
        if (cache != null) {
            CtMember tail = cache.lastMethod();
            for (CtMember mth = cache.methodHead(); mth != tail; mth = mth.next()) {
                mth.nameReplaced();
            }
        }
    }

    protected CtMember.Cache hasMemberCache() {
        if (this.memberCache != null) {
            return this.memberCache.get();
        }
        return null;
    }

    protected synchronized CtMember.Cache getMembers() {
        CtMember.Cache cache = null;
        if (this.memberCache == null || (cache = this.memberCache.get()) == null) {
            cache = new CtMember.Cache(this);
            this.makeFieldCache(cache);
            this.makeBehaviorCache(cache);
            this.memberCache = new WeakReference<CtMember.Cache>(cache);
        }
        return cache;
    }

    private void makeFieldCache(CtMember.Cache cache) {
        List<FieldInfo> fields = this.getClassFile3(false).getFields();
        for (FieldInfo finfo : fields) {
            cache.addField(new CtField(finfo, (CtClass)this));
        }
    }

    private void makeBehaviorCache(CtMember.Cache cache) {
        List<MethodInfo> methods = this.getClassFile3(false).getMethods();
        for (MethodInfo minfo : methods) {
            if (minfo.isMethod()) {
                cache.addMethod(new CtMethod(minfo, this));
                continue;
            }
            cache.addConstructor(new CtConstructor(minfo, (CtClass)this));
        }
    }

    @Override
    public CtField[] getFields() {
        ArrayList<CtMember> alist = new ArrayList<CtMember>();
        CtClassType.getFields(alist, this);
        return alist.toArray(new CtField[alist.size()]);
    }

    private static void getFields(List<CtMember> alist, CtClass cc) {
        if (cc == null) {
            return;
        }
        try {
            CtClassType.getFields(alist, cc.getSuperclass());
        }
        catch (NotFoundException notFoundException) {
            // empty catch block
        }
        try {
            CtClass[] ifs;
            for (CtClass ctc : ifs = cc.getInterfaces()) {
                CtClassType.getFields(alist, ctc);
            }
        }
        catch (NotFoundException ifs) {
            // empty catch block
        }
        CtMember.Cache memCache = ((CtClassType)cc).getMembers();
        CtMember field = memCache.fieldHead();
        CtMember tail = memCache.lastField();
        while (field != tail) {
            if (Modifier.isPrivate((field = field.next()).getModifiers())) continue;
            alist.add(field);
        }
    }

    @Override
    public CtField getField(String name, String desc) throws NotFoundException {
        CtField f2 = this.getField2(name, desc);
        return this.checkGetField(f2, name, desc);
    }

    private CtField checkGetField(CtField f2, String name, String desc) throws NotFoundException {
        if (f2 == null) {
            String msg = "field: " + name;
            if (desc != null) {
                msg = msg + " type " + desc;
            }
            throw new NotFoundException(msg + " in " + this.getName());
        }
        return f2;
    }

    @Override
    CtField getField2(String name, String desc) {
        CtField df = this.getDeclaredField2(name, desc);
        if (df != null) {
            return df;
        }
        try {
            CtClass[] ifs;
            for (CtClass ctc : ifs = this.getInterfaces()) {
                CtField f2 = ctc.getField2(name, desc);
                if (f2 == null) continue;
                return f2;
            }
            CtClass s2 = this.getSuperclass();
            if (s2 != null) {
                return s2.getField2(name, desc);
            }
        }
        catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return null;
    }

    @Override
    public CtField[] getDeclaredFields() {
        CtMember field;
        CtMember.Cache memCache = this.getMembers();
        CtMember tail = memCache.lastField();
        int num = CtMember.Cache.count(field, tail);
        CtField[] cfs = new CtField[num];
        int i2 = 0;
        for (field = memCache.fieldHead(); field != tail; field = field.next()) {
            cfs[i2++] = (CtField)field;
        }
        return cfs;
    }

    @Override
    public CtField getDeclaredField(String name) throws NotFoundException {
        return this.getDeclaredField(name, null);
    }

    @Override
    public CtField getDeclaredField(String name, String desc) throws NotFoundException {
        CtField f2 = this.getDeclaredField2(name, desc);
        return this.checkGetField(f2, name, desc);
    }

    private CtField getDeclaredField2(String name, String desc) {
        CtMember.Cache memCache = this.getMembers();
        CtMember field = memCache.fieldHead();
        CtMember tail = memCache.lastField();
        while (field != tail) {
            if (!(field = field.next()).getName().equals(name) || desc != null && !desc.equals(field.getSignature())) continue;
            return (CtField)field;
        }
        return null;
    }

    @Override
    public CtBehavior[] getDeclaredBehaviors() {
        CtMember cons;
        CtMember.Cache memCache = this.getMembers();
        CtMember consTail = memCache.lastCons();
        int cnum = CtMember.Cache.count(cons, consTail);
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        int mnum = CtMember.Cache.count(mth, mthTail);
        CtBehavior[] cb = new CtBehavior[cnum + mnum];
        int i2 = 0;
        for (cons = memCache.consHead(); cons != consTail; cons = cons.next()) {
            cb[i2++] = (CtBehavior)cons;
        }
        while (mth != mthTail) {
            mth = mth.next();
            cb[i2++] = (CtBehavior)mth;
        }
        return cb;
    }

    @Override
    public CtConstructor[] getConstructors() {
        CtMember.Cache memCache = this.getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        int n2 = 0;
        CtMember mem = cons;
        while (mem != consTail) {
            if (!CtClassType.isPubCons((CtConstructor)(mem = mem.next()))) continue;
            ++n2;
        }
        CtConstructor[] result = new CtConstructor[n2];
        int i2 = 0;
        mem = cons;
        while (mem != consTail) {
            CtConstructor cc = (CtConstructor)(mem = mem.next());
            if (!CtClassType.isPubCons(cc)) continue;
            result[i2++] = cc;
        }
        return result;
    }

    private static boolean isPubCons(CtConstructor cons) {
        return !Modifier.isPrivate(cons.getModifiers()) && cons.isConstructor();
    }

    @Override
    public CtConstructor getConstructor(String desc) throws NotFoundException {
        CtMember.Cache memCache = this.getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        while (cons != consTail) {
            CtConstructor cc = (CtConstructor)(cons = cons.next());
            if (!cc.getMethodInfo2().getDescriptor().equals(desc) || !cc.isConstructor()) continue;
            return cc;
        }
        return super.getConstructor(desc);
    }

    @Override
    public CtConstructor[] getDeclaredConstructors() {
        CtMember.Cache memCache = this.getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        int n2 = 0;
        CtMember mem = cons;
        while (mem != consTail) {
            CtConstructor cc = (CtConstructor)(mem = mem.next());
            if (!cc.isConstructor()) continue;
            ++n2;
        }
        CtConstructor[] result = new CtConstructor[n2];
        int i2 = 0;
        mem = cons;
        while (mem != consTail) {
            CtConstructor cc = (CtConstructor)(mem = mem.next());
            if (!cc.isConstructor()) continue;
            result[i2++] = cc;
        }
        return result;
    }

    @Override
    public CtConstructor getClassInitializer() {
        CtMember.Cache memCache = this.getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        while (cons != consTail) {
            CtConstructor cc = (CtConstructor)(cons = cons.next());
            if (!cc.isClassInitializer()) continue;
            return cc;
        }
        return null;
    }

    @Override
    public CtMethod[] getMethods() {
        HashMap<String, CtMember> h2 = new HashMap<String, CtMember>();
        CtClassType.getMethods0(h2, this);
        return h2.values().toArray(new CtMethod[h2.size()]);
    }

    private static void getMethods0(Map<String, CtMember> h2, CtClass cc) {
        try {
            CtClass[] ifs;
            for (CtClass ctc : ifs = cc.getInterfaces()) {
                CtClassType.getMethods0(h2, ctc);
            }
        }
        catch (NotFoundException ifs) {
            // empty catch block
        }
        try {
            CtClass s2 = cc.getSuperclass();
            if (s2 != null) {
                CtClassType.getMethods0(h2, s2);
            }
        }
        catch (NotFoundException s2) {
            // empty catch block
        }
        if (cc instanceof CtClassType) {
            CtMember.Cache memCache = ((CtClassType)cc).getMembers();
            CtMember mth = memCache.methodHead();
            CtMember mthTail = memCache.lastMethod();
            while (mth != mthTail) {
                if (Modifier.isPrivate((mth = mth.next()).getModifiers())) continue;
                h2.put(((CtMethod)mth).getStringRep(), mth);
            }
        }
    }

    @Override
    public CtMethod getMethod(String name, String desc) throws NotFoundException {
        CtMethod m2 = CtClassType.getMethod0(this, name, desc);
        if (m2 != null) {
            return m2;
        }
        throw new NotFoundException(name + "(..) is not found in " + this.getName());
    }

    private static CtMethod getMethod0(CtClass cc, String name, String desc) {
        if (cc instanceof CtClassType) {
            CtMember.Cache memCache = ((CtClassType)cc).getMembers();
            CtMember mth = memCache.methodHead();
            CtMember mthTail = memCache.lastMethod();
            while (mth != mthTail) {
                if (!(mth = mth.next()).getName().equals(name) || !((CtMethod)mth).getMethodInfo2().getDescriptor().equals(desc)) continue;
                return (CtMethod)mth;
            }
        }
        try {
            CtMethod m2;
            CtClass s2 = cc.getSuperclass();
            if (s2 != null && (m2 = CtClassType.getMethod0(s2, name, desc)) != null) {
                return m2;
            }
        }
        catch (NotFoundException s2) {
            // empty catch block
        }
        try {
            CtClass[] ifs;
            for (CtClass ctc : ifs = cc.getInterfaces()) {
                CtMethod m3 = CtClassType.getMethod0(ctc, name, desc);
                if (m3 == null) continue;
                return m3;
            }
        }
        catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return null;
    }

    @Override
    public CtMethod[] getDeclaredMethods() {
        CtMember.Cache memCache = this.getMembers();
        CtMember mthTail = memCache.lastMethod();
        ArrayList<CtMember> methods = new ArrayList<CtMember>();
        for (CtMember mth = memCache.methodHead(); mth != mthTail; mth = mth.next()) {
            methods.add(mth);
        }
        return methods.toArray(new CtMethod[methods.size()]);
    }

    @Override
    public CtMethod[] getDeclaredMethods(String name) throws NotFoundException {
        CtMember.Cache memCache = this.getMembers();
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        ArrayList<CtMember> methods = new ArrayList<CtMember>();
        while (mth != mthTail) {
            if (!(mth = mth.next()).getName().equals(name)) continue;
            methods.add(mth);
        }
        return methods.toArray(new CtMethod[methods.size()]);
    }

    @Override
    public CtMethod getDeclaredMethod(String name) throws NotFoundException {
        CtMember.Cache memCache = this.getMembers();
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        while (mth != mthTail) {
            if (!(mth = mth.next()).getName().equals(name)) continue;
            return (CtMethod)mth;
        }
        throw new NotFoundException(name + "(..) is not found in " + this.getName());
    }

    @Override
    public CtMethod getDeclaredMethod(String name, CtClass[] params) throws NotFoundException {
        String desc = Descriptor.ofParameters(params);
        CtMember.Cache memCache = this.getMembers();
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        while (mth != mthTail) {
            if (!(mth = mth.next()).getName().equals(name) || !((CtMethod)mth).getMethodInfo2().getDescriptor().startsWith(desc)) continue;
            return (CtMethod)mth;
        }
        throw new NotFoundException(name + "(..) is not found in " + this.getName());
    }

    @Override
    public void addField(CtField f2, String init) throws CannotCompileException {
        this.addField(f2, CtField.Initializer.byExpr(init));
    }

    @Override
    public void addField(CtField f2, CtField.Initializer init) throws CannotCompileException {
        this.checkModify();
        if (f2.getDeclaringClass() != this) {
            throw new CannotCompileException("cannot add");
        }
        if (init == null) {
            init = f2.getInit();
        }
        if (init != null) {
            init.check(f2.getSignature());
            int mod = f2.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                try {
                    ConstPool cp = this.getClassFile2().getConstPool();
                    int index = init.getConstantValue(cp, f2.getType());
                    if (index != 0) {
                        f2.getFieldInfo2().addAttribute(new ConstantAttribute(cp, index));
                        init = null;
                    }
                }
                catch (NotFoundException cp) {
                    // empty catch block
                }
            }
        }
        this.getMembers().addField(f2);
        this.getClassFile2().addField(f2.getFieldInfo2());
        if (init != null) {
            FieldInitLink fil = new FieldInitLink(f2, init);
            FieldInitLink link = this.fieldInitializers;
            if (link == null) {
                this.fieldInitializers = fil;
            } else {
                while (link.next != null) {
                    link = link.next;
                }
                link.next = fil;
            }
        }
    }

    @Override
    public void removeField(CtField f2) throws NotFoundException {
        this.checkModify();
        FieldInfo fi = f2.getFieldInfo2();
        ClassFile cf = this.getClassFile2();
        if (!cf.getFields().remove(fi)) {
            throw new NotFoundException(f2.toString());
        }
        this.getMembers().remove(f2);
        this.gcConstPool = true;
    }

    @Override
    public CtConstructor makeClassInitializer() throws CannotCompileException {
        CtConstructor clinit = this.getClassInitializer();
        if (clinit != null) {
            return clinit;
        }
        this.checkModify();
        ClassFile cf = this.getClassFile2();
        Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
        this.modifyClassConstructor(cf, code, 0, 0);
        return this.getClassInitializer();
    }

    @Override
    public void addConstructor(CtConstructor c2) throws CannotCompileException {
        this.checkModify();
        if (c2.getDeclaringClass() != this) {
            throw new CannotCompileException("cannot add");
        }
        this.getMembers().addConstructor(c2);
        this.getClassFile2().addMethod(c2.getMethodInfo2());
    }

    @Override
    public void removeConstructor(CtConstructor m2) throws NotFoundException {
        this.checkModify();
        MethodInfo mi = m2.getMethodInfo2();
        ClassFile cf = this.getClassFile2();
        if (!cf.getMethods().remove(mi)) {
            throw new NotFoundException(m2.toString());
        }
        this.getMembers().remove(m2);
        this.gcConstPool = true;
    }

    @Override
    public void addMethod(CtMethod m2) throws CannotCompileException {
        this.checkModify();
        if (m2.getDeclaringClass() != this) {
            throw new CannotCompileException("bad declaring class");
        }
        int mod = m2.getModifiers();
        if ((this.getModifiers() & 0x200) != 0) {
            if (Modifier.isProtected(mod) || Modifier.isPrivate(mod)) {
                throw new CannotCompileException("an interface method must be public: " + m2.toString());
            }
            m2.setModifiers(mod | 1);
        }
        this.getMembers().addMethod(m2);
        this.getClassFile2().addMethod(m2.getMethodInfo2());
        if ((mod & 0x400) != 0) {
            this.setModifiers(this.getModifiers() | 0x400);
        }
    }

    @Override
    public void removeMethod(CtMethod m2) throws NotFoundException {
        this.checkModify();
        MethodInfo mi = m2.getMethodInfo2();
        ClassFile cf = this.getClassFile2();
        if (!cf.getMethods().remove(mi)) {
            throw new NotFoundException(m2.toString());
        }
        this.getMembers().remove(m2);
        this.gcConstPool = true;
    }

    @Override
    public byte[] getAttribute(String name) {
        AttributeInfo ai = this.getClassFile2().getAttribute(name);
        if (ai == null) {
            return null;
        }
        return ai.get();
    }

    @Override
    public void setAttribute(String name, byte[] data) {
        this.checkModify();
        ClassFile cf = this.getClassFile2();
        cf.addAttribute(new AttributeInfo(cf.getConstPool(), name, data));
    }

    @Override
    public void instrument(CodeConverter converter) throws CannotCompileException {
        this.checkModify();
        ClassFile cf = this.getClassFile2();
        ConstPool cp = cf.getConstPool();
        List<MethodInfo> methods = cf.getMethods();
        for (MethodInfo minfo : methods.toArray(new MethodInfo[methods.size()])) {
            converter.doit(this, minfo, cp);
        }
    }

    @Override
    public void instrument(ExprEditor editor) throws CannotCompileException {
        this.checkModify();
        ClassFile cf = this.getClassFile2();
        List<MethodInfo> methods = cf.getMethods();
        for (MethodInfo minfo : methods.toArray(new MethodInfo[methods.size()])) {
            editor.doit(this, minfo);
        }
    }

    @Override
    public void prune() {
        if (this.wasPruned) {
            return;
        }
        this.wasFrozen = true;
        this.wasPruned = true;
        this.getClassFile2().prune();
    }

    @Override
    public void rebuildClassFile() {
        this.gcConstPool = true;
    }

    @Override
    public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
        try {
            if (this.isModified()) {
                this.checkPruned("toBytecode");
                ClassFile cf = this.getClassFile2();
                if (this.gcConstPool) {
                    cf.compact();
                    this.gcConstPool = false;
                }
                this.modifyClassConstructor(cf);
                this.modifyConstructors(cf);
                if (debugDump != null) {
                    this.dumpClassFile(cf);
                }
                cf.write(out);
                out.flush();
                this.fieldInitializers = null;
                if (this.doPruning) {
                    cf.prune();
                    this.wasPruned = true;
                }
            } else {
                this.classPool.writeClassfile(this.getName(), out);
            }
            this.getCount = 0;
            this.wasFrozen = true;
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
        catch (IOException e3) {
            throw new CannotCompileException(e3);
        }
    }

    private void dumpClassFile(ClassFile cf) throws IOException {
        try (DataOutputStream dump = this.makeFileOutput(debugDump);){
            cf.write(dump);
        }
    }

    private void checkPruned(String method) {
        if (this.wasPruned) {
            throw new RuntimeException(method + "(): " + this.getName() + " was pruned.");
        }
    }

    @Override
    public boolean stopPruning(boolean stop) {
        boolean prev = !this.doPruning;
        this.doPruning = !stop;
        return prev;
    }

    private void modifyClassConstructor(ClassFile cf) throws CannotCompileException, NotFoundException {
        if (this.fieldInitializers == null) {
            return;
        }
        Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
        Javac jv = new Javac(code, this);
        int stacksize = 0;
        boolean doInit = false;
        FieldInitLink fi = this.fieldInitializers;
        while (fi != null) {
            CtField f2 = fi.field;
            if (Modifier.isStatic(f2.getModifiers())) {
                doInit = true;
                int s2 = fi.init.compileIfStatic(f2.getType(), f2.getName(), code, jv);
                if (stacksize < s2) {
                    stacksize = s2;
                }
            }
            fi = fi.next;
        }
        if (doInit) {
            this.modifyClassConstructor(cf, code, stacksize, 0);
        }
    }

    private void modifyClassConstructor(ClassFile cf, Bytecode code, int stacksize, int localsize) throws CannotCompileException {
        MethodInfo m2 = cf.getStaticInitializer();
        if (m2 == null) {
            code.add(177);
            code.setMaxStack(stacksize);
            code.setMaxLocals(localsize);
            m2 = new MethodInfo(cf.getConstPool(), "<clinit>", "()V");
            m2.setAccessFlags(8);
            m2.setCodeAttribute(code.toCodeAttribute());
            cf.addMethod(m2);
            CtMember.Cache cache = this.hasMemberCache();
            if (cache != null) {
                cache.addConstructor(new CtConstructor(m2, (CtClass)this));
            }
        } else {
            CodeAttribute codeAttr = m2.getCodeAttribute();
            if (codeAttr == null) {
                throw new CannotCompileException("empty <clinit>");
            }
            try {
                int maxlocals;
                CodeIterator it = codeAttr.iterator();
                int pos = it.insertEx(code.get());
                it.insert(code.getExceptionTable(), pos);
                int maxstack = codeAttr.getMaxStack();
                if (maxstack < stacksize) {
                    codeAttr.setMaxStack(stacksize);
                }
                if ((maxlocals = codeAttr.getMaxLocals()) < localsize) {
                    codeAttr.setMaxLocals(localsize);
                }
            }
            catch (BadBytecode e2) {
                throw new CannotCompileException(e2);
            }
        }
        try {
            m2.rebuildStackMapIf6(this.classPool, cf);
        }
        catch (BadBytecode e3) {
            throw new CannotCompileException(e3);
        }
    }

    private void modifyConstructors(ClassFile cf) throws CannotCompileException, NotFoundException {
        if (this.fieldInitializers == null) {
            return;
        }
        ConstPool cp = cf.getConstPool();
        List<MethodInfo> methods = cf.getMethods();
        for (MethodInfo minfo : methods) {
            CodeAttribute codeAttr;
            if (!minfo.isConstructor() || (codeAttr = minfo.getCodeAttribute()) == null) continue;
            try {
                Bytecode init = new Bytecode(cp, 0, codeAttr.getMaxLocals());
                CtClass[] params = Descriptor.getParameterTypes(minfo.getDescriptor(), this.classPool);
                int stacksize = this.makeFieldInitializer(init, params);
                CtClassType.insertAuxInitializer(codeAttr, init, stacksize);
                minfo.rebuildStackMapIf6(this.classPool, cf);
            }
            catch (BadBytecode e2) {
                throw new CannotCompileException(e2);
            }
        }
    }

    private static void insertAuxInitializer(CodeAttribute codeAttr, Bytecode initializer, int stacksize) throws BadBytecode {
        CodeIterator it = codeAttr.iterator();
        int index = it.skipSuperConstructor();
        if (index < 0 && (index = it.skipThisConstructor()) >= 0) {
            return;
        }
        int pos = it.insertEx(initializer.get());
        it.insert(initializer.getExceptionTable(), pos);
        int maxstack = codeAttr.getMaxStack();
        if (maxstack < stacksize) {
            codeAttr.setMaxStack(stacksize);
        }
    }

    private int makeFieldInitializer(Bytecode code, CtClass[] parameters) throws CannotCompileException, NotFoundException {
        int stacksize = 0;
        Javac jv = new Javac(code, this);
        try {
            jv.recordParams(parameters, false);
        }
        catch (CompileError e2) {
            throw new CannotCompileException(e2);
        }
        FieldInitLink fi = this.fieldInitializers;
        while (fi != null) {
            int s2;
            CtField f2 = fi.field;
            if (!Modifier.isStatic(f2.getModifiers()) && stacksize < (s2 = fi.init.compile(f2.getType(), f2.getName(), code, parameters, jv))) {
                stacksize = s2;
            }
            fi = fi.next;
        }
        return stacksize;
    }

    Map<CtMethod, String> getHiddenMethods() {
        if (this.hiddenMethods == null) {
            this.hiddenMethods = new Hashtable<CtMethod, String>();
        }
        return this.hiddenMethods;
    }

    int getUniqueNumber() {
        return this.uniqueNumberSeed++;
    }

    @Override
    public String makeUniqueName(String prefix) {
        String name;
        HashMap<Object, CtClassType> table = new HashMap<Object, CtClassType>();
        this.makeMemberList(table);
        Set keys = table.keySet();
        String[] methods = new String[keys.size()];
        keys.toArray(methods);
        if (CtClassType.notFindInArray(prefix, methods)) {
            return prefix;
        }
        int i2 = 100;
        do {
            if (i2 <= 999) continue;
            throw new RuntimeException("too many unique name");
        } while (!CtClassType.notFindInArray(name = prefix + i2++, methods));
        return name;
    }

    private static boolean notFindInArray(String prefix, String[] values) {
        int len = values.length;
        for (int i2 = 0; i2 < len; ++i2) {
            if (!values[i2].startsWith(prefix)) continue;
            return false;
        }
        return true;
    }

    private void makeMemberList(Map<Object, CtClassType> table) {
        int mod = this.getModifiers();
        if (Modifier.isAbstract(mod) || Modifier.isInterface(mod)) {
            try {
                CtClass[] ifs = this.getInterfaces();
                for (CtClass ic : ifs) {
                    if (ic == null || !(ic instanceof CtClassType)) continue;
                    ((CtClassType)ic).makeMemberList(table);
                }
            }
            catch (NotFoundException ifs) {
                // empty catch block
            }
        }
        try {
            CtClass s2 = this.getSuperclass();
            if (s2 != null && s2 instanceof CtClassType) {
                ((CtClassType)s2).makeMemberList(table);
            }
        }
        catch (NotFoundException s2) {
            // empty catch block
        }
        List<MethodInfo> methods = this.getClassFile2().getMethods();
        for (MethodInfo minfo : methods) {
            table.put(minfo.getName(), this);
        }
        List<FieldInfo> fields = this.getClassFile2().getFields();
        for (FieldInfo finfo : fields) {
            table.put(finfo.getName(), this);
        }
    }
}

