/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.support.LinkRequestImpl;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.linker.AdaptationException;
import jdk.nashorn.internal.runtime.linker.AdaptationResult;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.ClassAndLoader;
import jdk.nashorn.internal.runtime.linker.JavaAdapterBytecodeGenerator;
import jdk.nashorn.internal.runtime.linker.JavaAdapterClassLoader;
import jdk.nashorn.internal.runtime.linker.JavaAdapterServices;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.linker.ReflectionCheckLinker;

public final class JavaAdapterFactory {
    private static final ProtectionDomain MINIMAL_PERMISSION_DOMAIN = JavaAdapterFactory.createMinimalPermissionDomain();
    private static final AccessControlContext CREATE_ADAPTER_INFO_ACC_CTXT = ClassAndLoader.createPermAccCtxt("createClassLoader", "getClassLoader", "accessDeclaredMembers", "accessClassInPackage.jdk.nashorn.internal.runtime");
    private static final ClassValue<Map<List<Class<?>>, AdapterInfo>> ADAPTER_INFO_MAPS = new ClassValue<Map<List<Class<?>>, AdapterInfo>>(){

        @Override
        protected Map<List<Class<?>>, AdapterInfo> computeValue(Class<?> type) {
            return new HashMap();
        }
    };

    public static StaticClass getAdapterClassFor(Class<?>[] types, ScriptObject classOverrides, MethodHandles.Lookup lookup) {
        return JavaAdapterFactory.getAdapterClassFor(types, classOverrides, JavaAdapterFactory.getProtectionDomain(lookup));
    }

    private static StaticClass getAdapterClassFor(Class<?>[] types, ScriptObject classOverrides, ProtectionDomain protectionDomain) {
        assert (types != null && types.length > 0);
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            for (Class<?> type : types) {
                Context.checkPackageAccess(type);
                ReflectionCheckLinker.checkReflectionAccess(type, true);
            }
        }
        return JavaAdapterFactory.getAdapterInfo(types).getAdapterClass(classOverrides, protectionDomain);
    }

    private static ProtectionDomain getProtectionDomain(MethodHandles.Lookup lookup) {
        if ((lookup.lookupModes() & 2) == 0) {
            return MINIMAL_PERMISSION_DOMAIN;
        }
        return JavaAdapterFactory.getProtectionDomain(lookup.lookupClass());
    }

    private static ProtectionDomain getProtectionDomain(final Class<?> clazz) {
        return AccessController.doPrivileged(new PrivilegedAction<ProtectionDomain>(){

            @Override
            public ProtectionDomain run() {
                return clazz.getProtectionDomain();
            }
        });
    }

    public static MethodHandle getConstructor(Class<?> sourceType, Class<?> targetType, MethodHandles.Lookup lookup) throws Exception {
        StaticClass adapterClass = JavaAdapterFactory.getAdapterClassFor(new Class[]{targetType}, null, lookup);
        return Lookup.MH.bindTo(Bootstrap.getLinkerServices().getGuardedInvocation(new LinkRequestImpl(NashornCallSiteDescriptor.get(lookup, "dyn:new", MethodType.methodType(targetType, StaticClass.class, sourceType), 0), null, 0, false, adapterClass, null)).getInvocation(), adapterClass);
    }

    static boolean isAutoConvertibleFromFunction(Class<?> clazz) {
        return JavaAdapterFactory.getAdapterInfo(new Class[]{clazz}).autoConvertibleFromFunction;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static AdapterInfo getAdapterInfo(Class<?>[] types) {
        AdapterInfo adapterInfo;
        ClassAndLoader definingClassAndLoader = ClassAndLoader.getDefiningClassAndLoader(types);
        Map<List<Class<?>>, AdapterInfo> adapterInfoMap = ADAPTER_INFO_MAPS.get(definingClassAndLoader.getRepresentativeClass());
        List<Object> typeList = types.length == 1 ? Collections.singletonList(types[0]) : Arrays.asList((Object[])types.clone());
        Map<List<Class<?>>, AdapterInfo> map = adapterInfoMap;
        synchronized (map) {
            adapterInfo = adapterInfoMap.get(typeList);
            if (adapterInfo == null) {
                adapterInfo = JavaAdapterFactory.createAdapterInfo(types, definingClassAndLoader);
                adapterInfoMap.put(typeList, adapterInfo);
            }
        }
        return adapterInfo;
    }

    private static AdapterInfo createAdapterInfo(final Class<?>[] types, final ClassAndLoader definingClassAndLoader) {
        Class<?> superClass = null;
        final ArrayList interfaces = new ArrayList(types.length);
        for (Class<?> t : types) {
            int mod = t.getModifiers();
            if (!t.isInterface()) {
                if (superClass != null) {
                    return new AdapterInfo(AdaptationResult.Outcome.ERROR_MULTIPLE_SUPERCLASSES, t.getCanonicalName() + " and " + superClass.getCanonicalName());
                }
                if (Modifier.isFinal(mod)) {
                    return new AdapterInfo(AdaptationResult.Outcome.ERROR_FINAL_CLASS, t.getCanonicalName());
                }
                superClass = t;
            } else {
                if (interfaces.size() > 65535) {
                    throw new IllegalArgumentException("interface limit exceeded");
                }
                interfaces.add(t);
            }
            if (Modifier.isPublic(mod)) continue;
            return new AdapterInfo(AdaptationResult.Outcome.ERROR_NON_PUBLIC_CLASS, t.getCanonicalName());
        }
        final Class effectiveSuperClass = superClass == null ? Object.class : superClass;
        return AccessController.doPrivileged(new PrivilegedAction<AdapterInfo>(){

            @Override
            public AdapterInfo run() {
                try {
                    return new AdapterInfo(effectiveSuperClass, interfaces, definingClassAndLoader);
                }
                catch (AdaptationException e) {
                    return new AdapterInfo(e.getAdaptationResult());
                }
                catch (RuntimeException e) {
                    return new AdapterInfo(new AdaptationResult(AdaptationResult.Outcome.ERROR_OTHER, Arrays.toString(types), e.toString()));
                }
            }
        }, CREATE_ADAPTER_INFO_ACC_CTXT);
    }

    private static ProtectionDomain createMinimalPermissionDomain() {
        Permissions permissions = new Permissions();
        permissions.add(new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.objects"));
        permissions.add(new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime"));
        permissions.add(new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime.linker"));
        return new ProtectionDomain(new CodeSource(null, (CodeSigner[])null), permissions);
    }

    private static class AdapterInfo {
        private static final ClassAndLoader SCRIPT_OBJECT_LOADER = new ClassAndLoader(ScriptFunction.class, true);
        private final ClassLoader commonLoader;
        private final JavaAdapterClassLoader classAdapterGenerator;
        private final JavaAdapterClassLoader instanceAdapterGenerator;
        private final Map<CodeSource, StaticClass> instanceAdapters = new ConcurrentHashMap<CodeSource, StaticClass>();
        final boolean autoConvertibleFromFunction;
        final AdaptationResult adaptationResult;

        AdapterInfo(Class<?> superClass, List<Class<?>> interfaces, ClassAndLoader definingLoader) throws AdaptationException {
            this.commonLoader = AdapterInfo.findCommonLoader(definingLoader);
            JavaAdapterBytecodeGenerator gen = new JavaAdapterBytecodeGenerator(superClass, interfaces, this.commonLoader, false);
            this.autoConvertibleFromFunction = gen.isAutoConvertibleFromFunction();
            this.instanceAdapterGenerator = gen.createAdapterClassLoader();
            this.classAdapterGenerator = new JavaAdapterBytecodeGenerator(superClass, interfaces, this.commonLoader, true).createAdapterClassLoader();
            this.adaptationResult = AdaptationResult.SUCCESSFUL_RESULT;
        }

        AdapterInfo(AdaptationResult.Outcome outcome, String classList) {
            this(new AdaptationResult(outcome, classList));
        }

        AdapterInfo(AdaptationResult adaptationResult) {
            this.commonLoader = null;
            this.classAdapterGenerator = null;
            this.instanceAdapterGenerator = null;
            this.autoConvertibleFromFunction = false;
            this.adaptationResult = adaptationResult;
        }

        StaticClass getAdapterClass(ScriptObject classOverrides, ProtectionDomain protectionDomain) {
            if (this.adaptationResult.getOutcome() != AdaptationResult.Outcome.SUCCESS) {
                throw this.adaptationResult.typeError();
            }
            return classOverrides == null ? this.getInstanceAdapterClass(protectionDomain) : this.getClassAdapterClass(classOverrides, protectionDomain);
        }

        private StaticClass getInstanceAdapterClass(ProtectionDomain protectionDomain) {
            StaticClass instanceAdapterClass;
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource == null) {
                codeSource = MINIMAL_PERMISSION_DOMAIN.getCodeSource();
            }
            if ((instanceAdapterClass = this.instanceAdapters.get(codeSource)) != null) {
                return instanceAdapterClass;
            }
            ProtectionDomain effectiveDomain = codeSource.equals(MINIMAL_PERMISSION_DOMAIN.getCodeSource()) ? MINIMAL_PERMISSION_DOMAIN : protectionDomain;
            instanceAdapterClass = this.instanceAdapterGenerator.generateClass(this.commonLoader, effectiveDomain);
            StaticClass existing = this.instanceAdapters.putIfAbsent(codeSource, instanceAdapterClass);
            return existing == null ? instanceAdapterClass : existing;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private StaticClass getClassAdapterClass(ScriptObject classOverrides, ProtectionDomain protectionDomain) {
            JavaAdapterServices.setClassOverrides(classOverrides);
            try {
                StaticClass staticClass = this.classAdapterGenerator.generateClass(this.commonLoader, protectionDomain);
                return staticClass;
            }
            finally {
                JavaAdapterServices.setClassOverrides(null);
            }
        }

        private static ClassLoader findCommonLoader(ClassAndLoader classAndLoader) throws AdaptationException {
            if (classAndLoader.canSee(SCRIPT_OBJECT_LOADER)) {
                return classAndLoader.getLoader();
            }
            if (SCRIPT_OBJECT_LOADER.canSee(classAndLoader)) {
                return SCRIPT_OBJECT_LOADER.getLoader();
            }
            throw new AdaptationException(AdaptationResult.Outcome.ERROR_NO_COMMON_LOADER, classAndLoader.getRepresentativeClass().getCanonicalName());
        }
    }
}

