/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.AbstractJavaLinker;
import jdk.internal.dynalink.beans.CheckRestrictedPackage;
import jdk.internal.dynalink.beans.DynamicMethod;
import jdk.internal.dynalink.beans.FacetIntrospector;
import jdk.internal.dynalink.beans.GuardedInvocationComponent;
import jdk.internal.dynalink.beans.SimpleDynamicMethod;
import jdk.internal.dynalink.beans.SingleDynamicMethod;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.beans.StaticClassIntrospector;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.Lookup;

class StaticClassLinker
implements TypeBasedGuardingDynamicLinker {
    private static final ClassValue<SingleClassStaticsLinker> linkers = new ClassValue<SingleClassStaticsLinker>(){

        @Override
        protected SingleClassStaticsLinker computeValue(Class<?> clazz) {
            return new SingleClassStaticsLinker(clazz);
        }
    };
    static final MethodHandle GET_CLASS;
    static final MethodHandle IS_CLASS;
    static final MethodHandle ARRAY_CTOR;

    StaticClassLinker() {
    }

    static Object getConstructorMethod(Class<?> clazz, String signature) {
        return linkers.get(clazz).getConstructorMethod(signature);
    }

    static Collection<String> getReadableStaticPropertyNames(Class<?> clazz) {
        return linkers.get(clazz).getReadablePropertyNames();
    }

    static Collection<String> getWritableStaticPropertyNames(Class<?> clazz) {
        return linkers.get(clazz).getWritablePropertyNames();
    }

    static Collection<String> getStaticMethodNames(Class<?> clazz) {
        return linkers.get(clazz).getMethodNames();
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices linkerServices) throws Exception {
        Object receiver = request.getReceiver();
        if (receiver instanceof StaticClass) {
            return linkers.get(((StaticClass)receiver).getRepresentedClass()).getGuardedInvocation(request, linkerServices);
        }
        return null;
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return type == StaticClass.class;
    }

    private static boolean isClass(Class<?> clazz, Object obj) {
        return obj instanceof StaticClass && ((StaticClass)obj).getRepresentedClass() == clazz;
    }

    static {
        ARRAY_CTOR = Lookup.PUBLIC.findStatic(Array.class, "newInstance", MethodType.methodType(Object.class, Class.class, Integer.TYPE));
        Lookup lookup = new Lookup(MethodHandles.lookup());
        GET_CLASS = lookup.findVirtual(StaticClass.class, "getRepresentedClass", MethodType.methodType(Class.class));
        IS_CLASS = lookup.findOwnStatic("isClass", Boolean.TYPE, Class.class, Object.class);
    }

    private static class SingleClassStaticsLinker
    extends AbstractJavaLinker {
        private final DynamicMethod constructor;

        SingleClassStaticsLinker(Class<?> clazz) {
            super(clazz, IS_CLASS.bindTo(clazz));
            this.setPropertyGetter("class", GET_CLASS, GuardedInvocationComponent.ValidationType.INSTANCE_OF);
            this.constructor = SingleClassStaticsLinker.createConstructorMethod(clazz);
        }

        private static DynamicMethod createConstructorMethod(Class<?> clazz) {
            if (clazz.isArray()) {
                MethodHandle boundArrayCtor = ARRAY_CTOR.bindTo(clazz.getComponentType());
                return new SimpleDynamicMethod(StaticClassIntrospector.editConstructorMethodHandle(boundArrayCtor.asType(boundArrayCtor.type().changeReturnType(clazz))), clazz, "<init>");
            }
            if (CheckRestrictedPackage.isRestrictedClass(clazz)) {
                return null;
            }
            return SingleClassStaticsLinker.createDynamicMethod(Arrays.asList(clazz.getConstructors()), clazz, "<init>");
        }

        @Override
        FacetIntrospector createFacetIntrospector() {
            return new StaticClassIntrospector(this.clazz);
        }

        @Override
        public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices linkerServices) throws Exception {
            MethodHandle ctorInvocation;
            GuardedInvocation gi = super.getGuardedInvocation(request, linkerServices);
            if (gi != null) {
                return gi;
            }
            CallSiteDescriptor desc = request.getCallSiteDescriptor();
            String op = desc.getNameToken(1);
            if ("new" == op && this.constructor != null && (ctorInvocation = this.constructor.getInvocation(desc, linkerServices)) != null) {
                return new GuardedInvocation(ctorInvocation, this.getClassGuard(desc.getMethodType()));
            }
            return null;
        }

        @Override
        SingleDynamicMethod getConstructorMethod(String signature) {
            return this.constructor != null ? this.constructor.getMethodForExactParamTypes(signature) : null;
        }
    }
}

