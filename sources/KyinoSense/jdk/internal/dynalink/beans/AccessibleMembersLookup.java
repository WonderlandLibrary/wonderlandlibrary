/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import jdk.internal.dynalink.beans.CheckRestrictedPackage;

class AccessibleMembersLookup {
    private final Map<MethodSignature, Method> methods = new HashMap<MethodSignature, Method>();
    private final Set<Class<?>> innerClasses = new LinkedHashSet();
    private final boolean instance;

    AccessibleMembersLookup(Class<?> clazz, boolean instance) {
        this.instance = instance;
        this.lookupAccessibleMembers(clazz);
    }

    Method getAccessibleMethod(Method m) {
        return m == null ? null : this.methods.get(new MethodSignature(m));
    }

    Collection<Method> getMethods() {
        return this.methods.values();
    }

    Class<?>[] getInnerClasses() {
        return this.innerClasses.toArray(new Class[this.innerClasses.size()]);
    }

    private void lookupAccessibleMembers(Class<?> clazz) {
        boolean searchSuperTypes;
        if (!CheckRestrictedPackage.isRestrictedClass(clazz)) {
            searchSuperTypes = false;
            for (Method method : clazz.getMethods()) {
                MethodSignature sig;
                boolean isStatic = Modifier.isStatic(method.getModifiers());
                if (this.instance == isStatic || this.methods.containsKey(sig = new MethodSignature(method))) continue;
                Class<?> declaringClass = method.getDeclaringClass();
                if (declaringClass != clazz && CheckRestrictedPackage.isRestrictedClass(declaringClass)) {
                    searchSuperTypes = true;
                    continue;
                }
                if (isStatic && clazz != declaringClass) continue;
                this.methods.put(sig, method);
            }
            for (GenericDeclaration genericDeclaration : clazz.getClasses()) {
                this.innerClasses.add((Class<?>)genericDeclaration);
            }
        } else {
            searchSuperTypes = true;
        }
        if (this.instance && searchSuperTypes) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                this.lookupAccessibleMembers(interfaces[i]);
            }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                this.lookupAccessibleMembers(superclass);
            }
        }
    }

    static final class MethodSignature {
        private final String name;
        private final Class<?>[] args;

        MethodSignature(String name, Class<?>[] args2) {
            this.name = name;
            this.args = args2;
        }

        MethodSignature(Method method) {
            this(method.getName(), method.getParameterTypes());
        }

        public boolean equals(Object o) {
            if (o instanceof MethodSignature) {
                MethodSignature ms = (MethodSignature)o;
                return ms.name.equals(this.name) && Arrays.equals(this.args, ms.args);
            }
            return false;
        }

        public int hashCode() {
            return this.name.hashCode() ^ Arrays.hashCode(this.args);
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("[MethodSignature ").append(this.name).append('(');
            if (this.args.length > 0) {
                b.append(this.args[0].getCanonicalName());
                for (int i = 1; i < this.args.length; ++i) {
                    b.append(", ").append(this.args[i].getCanonicalName());
                }
            }
            return b.append(")]").toString();
        }
    }
}

