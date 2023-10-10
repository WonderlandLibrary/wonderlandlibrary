/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.tools.reflect;

import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.libs.javassist.CodeConverter;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.CtField;
import com.viaversion.viaversion.libs.javassist.CtMethod;
import com.viaversion.viaversion.libs.javassist.CtNewMethod;
import com.viaversion.viaversion.libs.javassist.Modifier;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.Translator;
import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;
import com.viaversion.viaversion.libs.javassist.bytecode.MethodInfo;
import com.viaversion.viaversion.libs.javassist.tools.reflect.CannotReflectException;

public class Reflection
implements Translator {
    static final String classobjectField = "_classobject";
    static final String classobjectAccessor = "_getClass";
    static final String metaobjectField = "_metaobject";
    static final String metaobjectGetter = "_getMetaobject";
    static final String metaobjectSetter = "_setMetaobject";
    static final String readPrefix = "_r_";
    static final String writePrefix = "_w_";
    static final String metaobjectClassName = "com.viaversion.viaversion.libs.javassist.tools.reflect.Metaobject";
    static final String classMetaobjectClassName = "com.viaversion.viaversion.libs.javassist.tools.reflect.ClassMetaobject";
    protected CtMethod trapMethod;
    protected CtMethod trapStaticMethod;
    protected CtMethod trapRead;
    protected CtMethod trapWrite;
    protected CtClass[] readParam;
    protected ClassPool classPool = null;
    protected CodeConverter converter = new CodeConverter();

    private boolean isExcluded(String name) {
        return name.startsWith("_m_") || name.equals(classobjectAccessor) || name.equals(metaobjectSetter) || name.equals(metaobjectGetter) || name.startsWith(readPrefix) || name.startsWith(writePrefix);
    }

    @Override
    public void start(ClassPool pool) throws NotFoundException {
        this.classPool = pool;
        String msg = "com.viaversion.viaversion.libs.javassist.tools.reflect.Sample is not found or broken.";
        try {
            CtClass c2 = this.classPool.get("com.viaversion.viaversion.libs.javassist.tools.reflect.Sample");
            this.rebuildClassFile(c2.getClassFile());
            this.trapMethod = c2.getDeclaredMethod("trap");
            this.trapStaticMethod = c2.getDeclaredMethod("trapStatic");
            this.trapRead = c2.getDeclaredMethod("trapRead");
            this.trapWrite = c2.getDeclaredMethod("trapWrite");
            this.readParam = new CtClass[]{this.classPool.get("java.lang.Object")};
        }
        catch (NotFoundException e2) {
            throw new RuntimeException("com.viaversion.viaversion.libs.javassist.tools.reflect.Sample is not found or broken.");
        }
        catch (BadBytecode e3) {
            throw new RuntimeException("com.viaversion.viaversion.libs.javassist.tools.reflect.Sample is not found or broken.");
        }
    }

    @Override
    public void onLoad(ClassPool pool, String classname) throws CannotCompileException, NotFoundException {
        CtClass clazz = pool.get(classname);
        clazz.instrument(this.converter);
    }

    public boolean makeReflective(String classname, String metaobject, String metaclass) throws CannotCompileException, NotFoundException {
        return this.makeReflective(this.classPool.get(classname), this.classPool.get(metaobject), this.classPool.get(metaclass));
    }

    public boolean makeReflective(Class<?> clazz, Class<?> metaobject, Class<?> metaclass) throws CannotCompileException, NotFoundException {
        return this.makeReflective(clazz.getName(), metaobject.getName(), metaclass.getName());
    }

    public boolean makeReflective(CtClass clazz, CtClass metaobject, CtClass metaclass) throws CannotCompileException, CannotReflectException, NotFoundException {
        if (clazz.isInterface()) {
            throw new CannotReflectException("Cannot reflect an interface: " + clazz.getName());
        }
        if (clazz.subclassOf(this.classPool.get(classMetaobjectClassName))) {
            throw new CannotReflectException("Cannot reflect a subclass of ClassMetaobject: " + clazz.getName());
        }
        if (clazz.subclassOf(this.classPool.get(metaobjectClassName))) {
            throw new CannotReflectException("Cannot reflect a subclass of Metaobject: " + clazz.getName());
        }
        this.registerReflectiveClass(clazz);
        return this.modifyClassfile(clazz, metaobject, metaclass);
    }

    private void registerReflectiveClass(CtClass clazz) {
        CtField[] fs = clazz.getDeclaredFields();
        for (int i2 = 0; i2 < fs.length; ++i2) {
            CtField f2 = fs[i2];
            int mod = f2.getModifiers();
            if ((mod & 1) == 0 || (mod & 0x10) != 0) continue;
            String name = f2.getName();
            this.converter.replaceFieldRead(f2, clazz, readPrefix + name);
            this.converter.replaceFieldWrite(f2, clazz, writePrefix + name);
        }
    }

    private boolean modifyClassfile(CtClass clazz, CtClass metaobject, CtClass metaclass) throws CannotCompileException, NotFoundException {
        CtField f2;
        boolean addMeta;
        if (clazz.getAttribute("Reflective") != null) {
            return false;
        }
        clazz.setAttribute("Reflective", new byte[0]);
        CtClass mlevel = this.classPool.get("com.viaversion.viaversion.libs.javassist.tools.reflect.Metalevel");
        boolean bl = addMeta = !clazz.subtypeOf(mlevel);
        if (addMeta) {
            clazz.addInterface(mlevel);
        }
        this.processMethods(clazz, addMeta);
        this.processFields(clazz);
        if (addMeta) {
            f2 = new CtField(this.classPool.get(metaobjectClassName), metaobjectField, clazz);
            f2.setModifiers(4);
            clazz.addField(f2, CtField.Initializer.byNewWithParams(metaobject));
            clazz.addMethod(CtNewMethod.getter(metaobjectGetter, f2));
            clazz.addMethod(CtNewMethod.setter(metaobjectSetter, f2));
        }
        f2 = new CtField(this.classPool.get(classMetaobjectClassName), classobjectField, clazz);
        f2.setModifiers(10);
        clazz.addField(f2, CtField.Initializer.byNew(metaclass, new String[]{clazz.getName()}));
        clazz.addMethod(CtNewMethod.getter(classobjectAccessor, f2));
        return true;
    }

    private void processMethods(CtClass clazz, boolean dontSearch) throws CannotCompileException, NotFoundException {
        CtMethod[] ms = clazz.getMethods();
        for (int i2 = 0; i2 < ms.length; ++i2) {
            CtMethod m2 = ms[i2];
            int mod = m2.getModifiers();
            if (!Modifier.isPublic(mod) || Modifier.isAbstract(mod)) continue;
            this.processMethods0(mod, clazz, m2, i2, dontSearch);
        }
    }

    private void processMethods0(int mod, CtClass clazz, CtMethod m2, int identifier, boolean dontSearch) throws CannotCompileException, NotFoundException {
        CtMethod m22;
        String name = m2.getName();
        if (this.isExcluded(name)) {
            return;
        }
        if (m2.getDeclaringClass() == clazz) {
            if (Modifier.isNative(mod)) {
                return;
            }
            m22 = m2;
            if (Modifier.isFinal(mod)) {
                m22.setModifiers(mod &= 0xFFFFFFEF);
            }
        } else {
            if (Modifier.isFinal(mod)) {
                return;
            }
            m22 = CtNewMethod.delegator(this.findOriginal(m2, dontSearch), clazz);
            m22.setModifiers(mod &= 0xFFFFFEFF);
            clazz.addMethod(m22);
        }
        m22.setName("_m_" + identifier + "_" + name);
        CtMethod body = Modifier.isStatic(mod) ? this.trapStaticMethod : this.trapMethod;
        CtMethod wmethod = CtNewMethod.wrapped(m2.getReturnType(), name, m2.getParameterTypes(), m2.getExceptionTypes(), body, CtMethod.ConstParameter.integer(identifier), clazz);
        wmethod.setModifiers(mod);
        clazz.addMethod(wmethod);
    }

    private CtMethod findOriginal(CtMethod m2, boolean dontSearch) throws NotFoundException {
        if (dontSearch) {
            return m2;
        }
        String name = m2.getName();
        CtMethod[] ms = m2.getDeclaringClass().getDeclaredMethods();
        for (int i2 = 0; i2 < ms.length; ++i2) {
            String orgName = ms[i2].getName();
            if (!orgName.endsWith(name) || !orgName.startsWith("_m_") || !ms[i2].getSignature().equals(m2.getSignature())) continue;
            return ms[i2];
        }
        return m2;
    }

    private void processFields(CtClass clazz) throws CannotCompileException, NotFoundException {
        CtField[] fs = clazz.getDeclaredFields();
        for (int i2 = 0; i2 < fs.length; ++i2) {
            CtField f2 = fs[i2];
            int mod = f2.getModifiers();
            if ((mod & 1) == 0 || (mod & 0x10) != 0) continue;
            String name = f2.getName();
            CtClass ftype = f2.getType();
            CtMethod wmethod = CtNewMethod.wrapped(ftype, readPrefix + name, this.readParam, null, this.trapRead, CtMethod.ConstParameter.string(name), clazz);
            wmethod.setModifiers(mod |= 8);
            clazz.addMethod(wmethod);
            CtClass[] writeParam = new CtClass[]{this.classPool.get("java.lang.Object"), ftype};
            wmethod = CtNewMethod.wrapped(CtClass.voidType, writePrefix + name, writeParam, null, this.trapWrite, CtMethod.ConstParameter.string(name), clazz);
            wmethod.setModifiers(mod);
            clazz.addMethod(wmethod);
        }
    }

    public void rebuildClassFile(ClassFile cf) throws BadBytecode {
        if (ClassFile.MAJOR_VERSION < 50) {
            return;
        }
        for (MethodInfo mi : cf.getMethods()) {
            mi.rebuildStackMap(this.classPool);
        }
    }
}

