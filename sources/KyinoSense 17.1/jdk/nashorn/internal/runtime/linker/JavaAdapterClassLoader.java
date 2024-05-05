/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.DumpBytecode;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.linker.ClassAndLoader;
import jdk.nashorn.internal.runtime.linker.JavaAdapterServices;

final class JavaAdapterClassLoader {
    private static final AccessControlContext CREATE_LOADER_ACC_CTXT = ClassAndLoader.createPermAccCtxt("createClassLoader");
    private static final AccessControlContext GET_CONTEXT_ACC_CTXT = ClassAndLoader.createPermAccCtxt("nashorn.getContext");
    private static final Collection<String> VISIBLE_INTERNAL_CLASS_NAMES = Collections.unmodifiableCollection(new HashSet<String>(Arrays.asList(JavaAdapterServices.class.getName(), ScriptObject.class.getName(), ScriptFunction.class.getName(), JSType.class.getName())));
    private final String className;
    private final byte[] classBytes;

    JavaAdapterClassLoader(String className, byte[] classBytes) {
        this.className = className.replace('/', '.');
        this.classBytes = classBytes;
    }

    StaticClass generateClass(final ClassLoader parentLoader, final ProtectionDomain protectionDomain) {
        assert (protectionDomain != null);
        return AccessController.doPrivileged(new PrivilegedAction<StaticClass>(){

            @Override
            public StaticClass run() {
                try {
                    return StaticClass.forClass(Class.forName(JavaAdapterClassLoader.this.className, true, JavaAdapterClassLoader.this.createClassLoader(parentLoader, protectionDomain)));
                }
                catch (ClassNotFoundException e) {
                    throw new AssertionError((Object)e);
                }
            }
        }, CREATE_LOADER_ACC_CTXT);
    }

    private ClassLoader createClassLoader(ClassLoader parentLoader, final ProtectionDomain protectionDomain) {
        return new SecureClassLoader(parentLoader){
            private final ClassLoader myLoader;
            {
                super(x0);
                this.myLoader = this.getClass().getClassLoader();
            }

            @Override
            public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                try {
                    Context.checkPackageAccess(name);
                    return super.loadClass(name, resolve);
                }
                catch (SecurityException se) {
                    if (VISIBLE_INTERNAL_CLASS_NAMES.contains(name)) {
                        return this.myLoader.loadClass(name);
                    }
                    throw se;
                }
            }

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.equals(JavaAdapterClassLoader.this.className)) {
                    assert (JavaAdapterClassLoader.this.classBytes != null) : "what? already cleared .class bytes!!";
                    Context ctx = AccessController.doPrivileged(new PrivilegedAction<Context>(){

                        @Override
                        public Context run() {
                            return Context.getContext();
                        }
                    }, GET_CONTEXT_ACC_CTXT);
                    DumpBytecode.dumpBytecode(ctx.getEnv(), ctx.getLogger(Compiler.class), JavaAdapterClassLoader.this.classBytes, name);
                    return this.defineClass(name, JavaAdapterClassLoader.this.classBytes, 0, JavaAdapterClassLoader.this.classBytes.length, protectionDomain);
                }
                throw new ClassNotFoundException(name);
            }
        };
    }
}

