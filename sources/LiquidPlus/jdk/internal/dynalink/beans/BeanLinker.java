/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.AbstractJavaLinker;
import jdk.internal.dynalink.beans.BeanIntrospector;
import jdk.internal.dynalink.beans.FacetIntrospector;
import jdk.internal.dynalink.beans.GuardedInvocationComponent;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.Guards;
import jdk.internal.dynalink.support.Lookup;
import jdk.internal.dynalink.support.TypeUtilities;

class BeanLinker
extends AbstractJavaLinker
implements TypeBasedGuardingDynamicLinker {
    private static MethodHandle GET_LIST_ELEMENT = Lookup.PUBLIC.findVirtual(List.class, "get", MethodType.methodType(Object.class, Integer.TYPE));
    private static MethodHandle GET_MAP_ELEMENT = Lookup.PUBLIC.findVirtual(Map.class, "get", MethodType.methodType(Object.class, Object.class));
    private static MethodHandle LIST_GUARD = Guards.getInstanceOfGuard(List.class);
    private static MethodHandle MAP_GUARD = Guards.getInstanceOfGuard(Map.class);
    private static MethodHandle RANGE_CHECK_ARRAY = BeanLinker.findRangeCheck(Object.class);
    private static MethodHandle RANGE_CHECK_LIST = BeanLinker.findRangeCheck(List.class);
    private static MethodHandle CONTAINS_MAP = Lookup.PUBLIC.findVirtual(Map.class, "containsKey", MethodType.methodType(Boolean.TYPE, Object.class));
    private static MethodHandle SET_LIST_ELEMENT = Lookup.PUBLIC.findVirtual(List.class, "set", MethodType.methodType(Object.class, Integer.TYPE, Object.class));
    private static MethodHandle PUT_MAP_ELEMENT = Lookup.PUBLIC.findVirtual(Map.class, "put", MethodType.methodType(Object.class, Object.class, Object.class));
    private static MethodHandle GET_ARRAY_LENGTH = Lookup.PUBLIC.findStatic(Array.class, "getLength", MethodType.methodType(Integer.TYPE, Object.class));
    private static MethodHandle GET_COLLECTION_LENGTH = Lookup.PUBLIC.findVirtual(Collection.class, "size", MethodType.methodType(Integer.TYPE));
    private static MethodHandle GET_MAP_LENGTH = Lookup.PUBLIC.findVirtual(Map.class, "size", MethodType.methodType(Integer.TYPE));
    private static MethodHandle COLLECTION_GUARD = Guards.getInstanceOfGuard(Collection.class);

    BeanLinker(Class<?> clazz) {
        super(clazz, Guards.getClassGuard(clazz), Guards.getInstanceOfGuard(clazz));
        if (clazz.isArray()) {
            this.setPropertyGetter("length", GET_ARRAY_LENGTH, GuardedInvocationComponent.ValidationType.IS_ARRAY);
        } else if (List.class.isAssignableFrom(clazz)) {
            this.setPropertyGetter("length", GET_COLLECTION_LENGTH, GuardedInvocationComponent.ValidationType.INSTANCE_OF);
        }
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return type == this.clazz;
    }

    @Override
    FacetIntrospector createFacetIntrospector() {
        return new BeanIntrospector(this.clazz);
    }

    @Override
    protected GuardedInvocationComponent getGuardedInvocationComponent(CallSiteDescriptor callSiteDescriptor, LinkerServices linkerServices, List<String> operations) throws Exception {
        GuardedInvocationComponent superGic = super.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
        if (superGic != null) {
            return superGic;
        }
        if (operations.isEmpty()) {
            return null;
        }
        String op = operations.get(0);
        if ("getElem".equals(op)) {
            return this.getElementGetter(callSiteDescriptor, linkerServices, BeanLinker.pop(operations));
        }
        if ("setElem".equals(op)) {
            return this.getElementSetter(callSiteDescriptor, linkerServices, BeanLinker.pop(operations));
        }
        if ("getLength".equals(op)) {
            return this.getLengthGetter(callSiteDescriptor);
        }
        return null;
    }

    private GuardedInvocationComponent getElementGetter(CallSiteDescriptor callSiteDescriptor, LinkerServices linkerServices, List<String> operations) throws Exception {
        MethodHandle checkGuard;
        Object typedFixedKey;
        CollectionType collectionType;
        GuardedInvocationComponent gic;
        MethodType callSiteType = callSiteDescriptor.getMethodType();
        Class<?> declaredType = callSiteType.parameterType(0);
        GuardedInvocationComponent nextComponent = this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
        if (declaredType.isArray()) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(MethodHandles.arrayElementGetter(declaredType), linkerServices);
            collectionType = CollectionType.ARRAY;
        } else if (List.class.isAssignableFrom(declaredType)) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(GET_LIST_ELEMENT, linkerServices);
            collectionType = CollectionType.LIST;
        } else if (Map.class.isAssignableFrom(declaredType)) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(GET_MAP_ELEMENT, linkerServices);
            collectionType = CollectionType.MAP;
        } else if (this.clazz.isArray()) {
            gic = this.getClassGuardedInvocationComponent(linkerServices.filterInternalObjects(MethodHandles.arrayElementGetter(this.clazz)), callSiteType);
            collectionType = CollectionType.ARRAY;
        } else if (List.class.isAssignableFrom(this.clazz)) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(GET_LIST_ELEMENT, Guards.asType(LIST_GUARD, callSiteType), List.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF, linkerServices);
            collectionType = CollectionType.LIST;
        } else if (Map.class.isAssignableFrom(this.clazz)) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(GET_MAP_ELEMENT, Guards.asType(MAP_GUARD, callSiteType), Map.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF, linkerServices);
            collectionType = CollectionType.MAP;
        } else {
            return nextComponent;
        }
        String fixedKey = BeanLinker.getFixedKey(callSiteDescriptor);
        if (collectionType != CollectionType.MAP && fixedKey != null) {
            typedFixedKey = BeanLinker.convertKeyToInteger(fixedKey, linkerServices);
            if (typedFixedKey == null) {
                return nextComponent;
            }
        } else {
            typedFixedKey = fixedKey;
        }
        GuardedInvocation gi = gic.getGuardedInvocation();
        Binder binder = new Binder(linkerServices, callSiteType, typedFixedKey);
        MethodHandle invocation = gi.getInvocation();
        if (nextComponent == null) {
            return gic.replaceInvocation(binder.bind(invocation));
        }
        switch (collectionType) {
            case LIST: {
                checkGuard = BeanLinker.convertArgToInt(RANGE_CHECK_LIST, linkerServices, callSiteDescriptor);
                break;
            }
            case MAP: {
                checkGuard = linkerServices.filterInternalObjects(CONTAINS_MAP);
                break;
            }
            case ARRAY: {
                checkGuard = BeanLinker.convertArgToInt(RANGE_CHECK_ARRAY, linkerServices, callSiteDescriptor);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        AbstractJavaLinker.MethodPair matchedInvocations = BeanLinker.matchReturnTypes(binder.bind(invocation), nextComponent.getGuardedInvocation().getInvocation());
        return nextComponent.compose(matchedInvocations.guardWithTest(binder.bindTest(checkGuard)), gi.getGuard(), gic.getValidatorClass(), gic.getValidationType());
    }

    private static GuardedInvocationComponent createInternalFilteredGuardedInvocationComponent(MethodHandle invocation, LinkerServices linkerServices) {
        return new GuardedInvocationComponent(linkerServices.filterInternalObjects(invocation));
    }

    private static GuardedInvocationComponent createInternalFilteredGuardedInvocationComponent(MethodHandle invocation, MethodHandle guard, Class<?> validatorClass, GuardedInvocationComponent.ValidationType validationType, LinkerServices linkerServices) {
        return new GuardedInvocationComponent(linkerServices.filterInternalObjects(invocation), guard, validatorClass, validationType);
    }

    private static String getFixedKey(CallSiteDescriptor callSiteDescriptor) {
        return callSiteDescriptor.getNameTokenCount() == 2 ? null : callSiteDescriptor.getNameToken(2);
    }

    private static Object convertKeyToInteger(String fixedKey, LinkerServices linkerServices) throws Exception {
        try {
            if (linkerServices.canConvert(String.class, Number.class)) {
                try {
                    double doubleValue;
                    Object val = linkerServices.getTypeConverter(String.class, Number.class).invoke(fixedKey);
                    if (!(val instanceof Number)) {
                        return null;
                    }
                    Number n = (Number)val;
                    if (n instanceof Integer) {
                        return n;
                    }
                    int intIndex = n.intValue();
                    if ((double)intIndex != (doubleValue = n.doubleValue()) && !Double.isInfinite(doubleValue)) {
                        return null;
                    }
                    return intIndex;
                }
                catch (Error | Exception e) {
                    throw e;
                }
                catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
            return Integer.valueOf(fixedKey);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static MethodHandle convertArgToInt(MethodHandle mh, LinkerServices ls, CallSiteDescriptor desc) {
        Class<?> sourceType = desc.getMethodType().parameterType(1);
        if (TypeUtilities.isMethodInvocationConvertible(sourceType, Number.class)) {
            return mh;
        }
        if (ls.canConvert(sourceType, Number.class)) {
            MethodHandle converter = ls.getTypeConverter(sourceType, Number.class);
            return MethodHandles.filterArguments(mh, 1, converter.asType(converter.type().changeReturnType(mh.type().parameterType(1))));
        }
        return mh;
    }

    private static MethodHandle findRangeCheck(Class<?> collectionType) {
        return Lookup.findOwnStatic(MethodHandles.lookup(), "rangeCheck", Boolean.TYPE, collectionType, Object.class);
    }

    private static final boolean rangeCheck(Object array, Object index) {
        double doubleValue;
        if (!(index instanceof Number)) {
            return false;
        }
        Number n = (Number)index;
        int intIndex = n.intValue();
        if ((double)intIndex != (doubleValue = n.doubleValue()) && !Double.isInfinite(doubleValue)) {
            return false;
        }
        if (0 <= intIndex && intIndex < Array.getLength(array)) {
            return true;
        }
        throw new ArrayIndexOutOfBoundsException("Array index out of range: " + n);
    }

    private static final boolean rangeCheck(List<?> list, Object index) {
        double doubleValue;
        if (!(index instanceof Number)) {
            return false;
        }
        Number n = (Number)index;
        int intIndex = n.intValue();
        if ((double)intIndex != (doubleValue = n.doubleValue()) && !Double.isInfinite(doubleValue)) {
            return false;
        }
        if (0 <= intIndex && intIndex < list.size()) {
            return true;
        }
        throw new IndexOutOfBoundsException("Index: " + n + ", Size: " + list.size());
    }

    private GuardedInvocationComponent getElementSetter(CallSiteDescriptor callSiteDescriptor, LinkerServices linkerServices, List<String> operations) throws Exception {
        Object typedFixedKey;
        GuardedInvocationComponent nextComponent;
        CollectionType collectionType;
        GuardedInvocationComponent gic;
        MethodType callSiteType = callSiteDescriptor.getMethodType();
        Class<?> declaredType = callSiteType.parameterType(0);
        if (declaredType.isArray()) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(MethodHandles.arrayElementSetter(declaredType), linkerServices);
            collectionType = CollectionType.ARRAY;
        } else if (List.class.isAssignableFrom(declaredType)) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(SET_LIST_ELEMENT, linkerServices);
            collectionType = CollectionType.LIST;
        } else if (Map.class.isAssignableFrom(declaredType)) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(PUT_MAP_ELEMENT, linkerServices);
            collectionType = CollectionType.MAP;
        } else if (this.clazz.isArray()) {
            gic = this.getClassGuardedInvocationComponent(linkerServices.filterInternalObjects(MethodHandles.arrayElementSetter(this.clazz)), callSiteType);
            collectionType = CollectionType.ARRAY;
        } else if (List.class.isAssignableFrom(this.clazz)) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(SET_LIST_ELEMENT, Guards.asType(LIST_GUARD, callSiteType), List.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF, linkerServices);
            collectionType = CollectionType.LIST;
        } else if (Map.class.isAssignableFrom(this.clazz)) {
            gic = BeanLinker.createInternalFilteredGuardedInvocationComponent(PUT_MAP_ELEMENT, Guards.asType(MAP_GUARD, callSiteType), Map.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF, linkerServices);
            collectionType = CollectionType.MAP;
        } else {
            gic = null;
            collectionType = null;
        }
        GuardedInvocationComponent guardedInvocationComponent = nextComponent = collectionType == CollectionType.MAP ? null : this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
        if (gic == null) {
            return nextComponent;
        }
        String fixedKey = BeanLinker.getFixedKey(callSiteDescriptor);
        if (collectionType != CollectionType.MAP && fixedKey != null) {
            typedFixedKey = BeanLinker.convertKeyToInteger(fixedKey, linkerServices);
            if (typedFixedKey == null) {
                return nextComponent;
            }
        } else {
            typedFixedKey = fixedKey;
        }
        GuardedInvocation gi = gic.getGuardedInvocation();
        Binder binder = new Binder(linkerServices, callSiteType, typedFixedKey);
        MethodHandle invocation = gi.getInvocation();
        if (nextComponent == null) {
            return gic.replaceInvocation(binder.bind(invocation));
        }
        assert (collectionType == CollectionType.LIST || collectionType == CollectionType.ARRAY);
        MethodHandle checkGuard = BeanLinker.convertArgToInt(collectionType == CollectionType.LIST ? RANGE_CHECK_LIST : RANGE_CHECK_ARRAY, linkerServices, callSiteDescriptor);
        AbstractJavaLinker.MethodPair matchedInvocations = BeanLinker.matchReturnTypes(binder.bind(invocation), nextComponent.getGuardedInvocation().getInvocation());
        return nextComponent.compose(matchedInvocations.guardWithTest(binder.bindTest(checkGuard)), gi.getGuard(), gic.getValidatorClass(), gic.getValidationType());
    }

    private GuardedInvocationComponent getLengthGetter(CallSiteDescriptor callSiteDescriptor) {
        BeanLinker.assertParameterCount(callSiteDescriptor, 1);
        MethodType callSiteType = callSiteDescriptor.getMethodType();
        Class<?> declaredType = callSiteType.parameterType(0);
        if (declaredType.isArray()) {
            return new GuardedInvocationComponent(GET_ARRAY_LENGTH.asType(callSiteType));
        }
        if (Collection.class.isAssignableFrom(declaredType)) {
            return new GuardedInvocationComponent(GET_COLLECTION_LENGTH.asType(callSiteType));
        }
        if (Map.class.isAssignableFrom(declaredType)) {
            return new GuardedInvocationComponent(GET_MAP_LENGTH.asType(callSiteType));
        }
        if (this.clazz.isArray()) {
            return new GuardedInvocationComponent(GET_ARRAY_LENGTH.asType(callSiteType), Guards.isArray(0, callSiteType), GuardedInvocationComponent.ValidationType.IS_ARRAY);
        }
        if (Collection.class.isAssignableFrom(this.clazz)) {
            return new GuardedInvocationComponent(GET_COLLECTION_LENGTH.asType(callSiteType), Guards.asType(COLLECTION_GUARD, callSiteType), Collection.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF);
        }
        if (Map.class.isAssignableFrom(this.clazz)) {
            return new GuardedInvocationComponent(GET_MAP_LENGTH.asType(callSiteType), Guards.asType(MAP_GUARD, callSiteType), Map.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF);
        }
        return null;
    }

    private static void assertParameterCount(CallSiteDescriptor descriptor, int paramCount) {
        if (descriptor.getMethodType().parameterCount() != paramCount) {
            throw new BootstrapMethodError(descriptor.getName() + " must have exactly " + paramCount + " parameters.");
        }
    }

    private static class Binder {
        private final LinkerServices linkerServices;
        private final MethodType methodType;
        private final Object fixedKey;

        Binder(LinkerServices linkerServices, MethodType methodType, Object fixedKey) {
            this.linkerServices = linkerServices;
            this.methodType = fixedKey == null ? methodType : methodType.insertParameterTypes(1, fixedKey.getClass());
            this.fixedKey = fixedKey;
        }

        MethodHandle bind(MethodHandle handle) {
            return this.bindToFixedKey(this.linkerServices.asTypeLosslessReturn(handle, this.methodType));
        }

        MethodHandle bindTest(MethodHandle handle) {
            return this.bindToFixedKey(Guards.asType(handle, this.methodType));
        }

        private MethodHandle bindToFixedKey(MethodHandle handle) {
            return this.fixedKey == null ? handle : MethodHandles.insertArguments(handle, 1, this.fixedKey);
        }
    }

    private static enum CollectionType {
        ARRAY,
        LIST,
        MAP;

    }
}

