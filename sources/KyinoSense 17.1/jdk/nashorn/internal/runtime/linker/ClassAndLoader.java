/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import jdk.nashorn.internal.runtime.ECMAErrors;

final class ClassAndLoader {
    private static final AccessControlContext GET_LOADER_ACC_CTXT = ClassAndLoader.createPermAccCtxt("getClassLoader");
    private final Class<?> representativeClass;
    private ClassLoader loader;
    private boolean loaderRetrieved;

    static AccessControlContext createPermAccCtxt(String ... permNames) {
        Permissions perms = new Permissions();
        for (String permName : permNames) {
            perms.add(new RuntimePermission(permName));
        }
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }

    ClassAndLoader(Class<?> representativeClass, boolean retrieveLoader) {
        this.representativeClass = representativeClass;
        if (retrieveLoader) {
            this.retrieveLoader();
        }
    }

    Class<?> getRepresentativeClass() {
        return this.representativeClass;
    }

    boolean canSee(ClassAndLoader other) {
        try {
            Class<?> otherClass = other.getRepresentativeClass();
            return Class.forName(otherClass.getName(), false, this.getLoader()) == otherClass;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    ClassLoader getLoader() {
        if (!this.loaderRetrieved) {
            this.retrieveLoader();
        }
        return this.getRetrievedLoader();
    }

    ClassLoader getRetrievedLoader() {
        assert (this.loaderRetrieved);
        return this.loader;
    }

    private void retrieveLoader() {
        this.loader = this.representativeClass.getClassLoader();
        this.loaderRetrieved = true;
    }

    public boolean equals(Object obj) {
        return obj instanceof ClassAndLoader && ((ClassAndLoader)obj).getRetrievedLoader() == this.getRetrievedLoader();
    }

    public int hashCode() {
        return System.identityHashCode(this.getRetrievedLoader());
    }

    static ClassAndLoader getDefiningClassAndLoader(final Class<?>[] types) {
        if (types.length == 1) {
            return new ClassAndLoader(types[0], false);
        }
        return AccessController.doPrivileged(new PrivilegedAction<ClassAndLoader>(){

            @Override
            public ClassAndLoader run() {
                return ClassAndLoader.getDefiningClassAndLoaderPrivileged(types);
            }
        }, GET_LOADER_ACC_CTXT);
    }

    static ClassAndLoader getDefiningClassAndLoaderPrivileged(Class<?>[] types) {
        Collection<ClassAndLoader> maximumVisibilityLoaders = ClassAndLoader.getMaximumVisibilityLoaders(types);
        Iterator<ClassAndLoader> it = maximumVisibilityLoaders.iterator();
        if (maximumVisibilityLoaders.size() == 1) {
            return it.next();
        }
        assert (maximumVisibilityLoaders.size() > 1);
        StringBuilder b = new StringBuilder();
        b.append(it.next().getRepresentativeClass().getCanonicalName());
        while (it.hasNext()) {
            b.append(", ").append(it.next().getRepresentativeClass().getCanonicalName());
        }
        throw ECMAErrors.typeError("extend.ambiguous.defining.class", b.toString());
    }

    private static Collection<ClassAndLoader> getMaximumVisibilityLoaders(Class<?>[] types) {
        LinkedList<ClassAndLoader> maximumVisibilityLoaders = new LinkedList<ClassAndLoader>();
        block0: for (ClassAndLoader maxCandidate : ClassAndLoader.getClassLoadersForTypes(types)) {
            Iterator it = maximumVisibilityLoaders.iterator();
            while (it.hasNext()) {
                ClassAndLoader existingMax = (ClassAndLoader)it.next();
                boolean candidateSeesExisting = maxCandidate.canSee(existingMax);
                boolean exitingSeesCandidate = existingMax.canSee(maxCandidate);
                if (candidateSeesExisting) {
                    if (exitingSeesCandidate) continue;
                    it.remove();
                    continue;
                }
                if (!exitingSeesCandidate) continue;
                continue block0;
            }
            maximumVisibilityLoaders.add(maxCandidate);
        }
        return maximumVisibilityLoaders;
    }

    private static Collection<ClassAndLoader> getClassLoadersForTypes(Class<?>[] types) {
        LinkedHashMap<ClassAndLoader, ClassAndLoader> classesAndLoaders = new LinkedHashMap<ClassAndLoader, ClassAndLoader>();
        for (Class<?> c : types) {
            ClassAndLoader cl = new ClassAndLoader(c, true);
            if (classesAndLoaders.containsKey(cl)) continue;
            classesAndLoaders.put(cl, cl);
        }
        return classesAndLoaders.keySet();
    }
}

