/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.MethodInfo;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.scanners.AbstractScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.Utils;

public class MemberUsageScanner
extends AbstractScanner {
    private ClassPool classPool;

    @Override
    public void scan(Object cls, Store store) {
        try {
            CtClass ctClass = this.getClassPool().get(this.getMetadataAdapter().getClassName(cls));
            for (CtConstructor ctConstructor : ctClass.getDeclaredConstructors()) {
                this.scanMember(ctConstructor, store);
            }
            for (CtBehavior ctBehavior : ctClass.getDeclaredMethods()) {
                this.scanMember(ctBehavior, store);
            }
            ctClass.detach();
        }
        catch (Exception e2) {
            throw new ReflectionsException("Could not scan method usage for " + this.getMetadataAdapter().getClassName(cls), e2);
        }
    }

    void scanMember(CtBehavior member, final Store store) throws CannotCompileException {
        final String key = member.getDeclaringClass().getName() + "." + member.getMethodInfo().getName() + "(" + this.parameterNames(member.getMethodInfo()) + ")";
        member.instrument(new ExprEditor(){

            @Override
            public void edit(NewExpr e2) throws CannotCompileException {
                try {
                    MemberUsageScanner.this.put(store, e2.getConstructor().getDeclaringClass().getName() + ".<init>(" + MemberUsageScanner.this.parameterNames(e2.getConstructor().getMethodInfo()) + ")", e2.getLineNumber(), key);
                }
                catch (NotFoundException e1) {
                    throw new ReflectionsException("Could not find new instance usage in " + key, e1);
                }
            }

            @Override
            public void edit(MethodCall m2) throws CannotCompileException {
                try {
                    MemberUsageScanner.this.put(store, m2.getMethod().getDeclaringClass().getName() + "." + m2.getMethodName() + "(" + MemberUsageScanner.this.parameterNames(m2.getMethod().getMethodInfo()) + ")", m2.getLineNumber(), key);
                }
                catch (NotFoundException e2) {
                    throw new ReflectionsException("Could not find member " + m2.getClassName() + " in " + key, e2);
                }
            }

            @Override
            public void edit(ConstructorCall c2) throws CannotCompileException {
                try {
                    MemberUsageScanner.this.put(store, c2.getConstructor().getDeclaringClass().getName() + ".<init>(" + MemberUsageScanner.this.parameterNames(c2.getConstructor().getMethodInfo()) + ")", c2.getLineNumber(), key);
                }
                catch (NotFoundException e2) {
                    throw new ReflectionsException("Could not find member " + c2.getClassName() + " in " + key, e2);
                }
            }

            @Override
            public void edit(FieldAccess f2) throws CannotCompileException {
                try {
                    MemberUsageScanner.this.put(store, f2.getField().getDeclaringClass().getName() + "." + f2.getFieldName(), f2.getLineNumber(), key);
                }
                catch (NotFoundException e2) {
                    throw new ReflectionsException("Could not find member " + f2.getFieldName() + " in " + key, e2);
                }
            }
        });
    }

    private void put(Store store, String key, int lineNumber, String value) {
        if (this.acceptResult(key)) {
            this.put(store, key, value + " #" + lineNumber);
        }
    }

    String parameterNames(MethodInfo info) {
        return Utils.join(this.getMetadataAdapter().getParameterNames(info), ", ");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ClassPool getClassPool() {
        if (this.classPool == null) {
            MemberUsageScanner memberUsageScanner = this;
            synchronized (memberUsageScanner) {
                this.classPool = new ClassPool();
                ClassLoader[] classLoaders = this.getConfiguration().getClassLoaders();
                if (classLoaders == null) {
                    classLoaders = ClasspathHelper.classLoaders(new ClassLoader[0]);
                }
                for (ClassLoader classLoader : classLoaders) {
                    this.classPool.appendClassPath(new LoaderClassPath(classLoader));
                }
            }
        }
        return this.classPool;
    }
}

