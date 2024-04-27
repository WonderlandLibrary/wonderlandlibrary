/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.script.Bindings;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardedTypeConversion;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.Guards;
import jdk.internal.dynalink.support.LinkerServicesImpl;
import jdk.internal.dynalink.support.Lookup;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ListAdapter;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;
import jdk.nashorn.internal.runtime.linker.JavaArgumentConverters;

final class NashornLinker
implements TypeBasedGuardingDynamicLinker,
GuardingTypeConverterFactory,
ConversionComparator {
    private static final ClassValue<MethodHandle> ARRAY_CONVERTERS = new ClassValue<MethodHandle>(){

        @Override
        protected MethodHandle computeValue(Class<?> type) {
            return NashornLinker.createArrayConverter(type);
        }
    };
    private static final MethodHandle IS_SCRIPT_OBJECT = Guards.isInstance(ScriptObject.class, jdk.nashorn.internal.lookup.Lookup.MH.type(Boolean.TYPE, Object.class));
    private static final MethodHandle IS_SCRIPT_FUNCTION = Guards.isInstance(ScriptFunction.class, jdk.nashorn.internal.lookup.Lookup.MH.type(Boolean.TYPE, Object.class));
    private static final MethodHandle IS_NATIVE_ARRAY = Guards.isOfClass(NativeArray.class, jdk.nashorn.internal.lookup.Lookup.MH.type(Boolean.TYPE, Object.class));
    private static final MethodHandle IS_NASHORN_OR_UNDEFINED_TYPE = NashornLinker.findOwnMH("isNashornTypeOrUndefined", Boolean.TYPE, Object.class);
    private static final MethodHandle CREATE_MIRROR = NashornLinker.findOwnMH("createMirror", Object.class, Object.class);
    private static final MethodHandle TO_COLLECTION;
    private static final MethodHandle TO_DEQUE;
    private static final MethodHandle TO_LIST;
    private static final MethodHandle TO_QUEUE;

    NashornLinker() {
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return NashornLinker.canLinkTypeStatic(type);
    }

    static boolean canLinkTypeStatic(Class<?> type) {
        return ScriptObject.class.isAssignableFrom(type) || Undefined.class == type;
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices linkerServices) throws Exception {
        LinkRequest requestWithoutContext = request.withoutRuntimeContext();
        Object self = requestWithoutContext.getReceiver();
        CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
        if (desc.getNameTokenCount() < 2 || !"dyn".equals(desc.getNameToken(0))) {
            return null;
        }
        return Bootstrap.asTypeSafeReturn(NashornLinker.getGuardedInvocation(self, request, desc), linkerServices, desc);
    }

    private static GuardedInvocation getGuardedInvocation(Object self, LinkRequest request, CallSiteDescriptor desc) {
        GuardedInvocation inv;
        if (self instanceof ScriptObject) {
            inv = ((ScriptObject)self).lookup(desc, request);
        } else if (self instanceof Undefined) {
            inv = Undefined.lookup(desc);
        } else {
            throw new AssertionError((Object)self.getClass().getName());
        }
        return inv;
    }

    @Override
    public GuardedTypeConversion convertToType(Class<?> sourceType, Class<?> targetType) throws Exception {
        GuardedInvocation gi = NashornLinker.convertToTypeNoCast(sourceType, targetType);
        if (gi != null) {
            return new GuardedTypeConversion(gi.asType(jdk.nashorn.internal.lookup.Lookup.MH.type(targetType, sourceType)), true);
        }
        gi = NashornLinker.getSamTypeConverter(sourceType, targetType);
        if (gi != null) {
            return new GuardedTypeConversion(gi.asType(jdk.nashorn.internal.lookup.Lookup.MH.type(targetType, sourceType)), false);
        }
        return null;
    }

    private static GuardedInvocation convertToTypeNoCast(Class<?> sourceType, Class<?> targetType) throws Exception {
        MethodHandle mh = JavaArgumentConverters.getConverter(targetType);
        if (mh != null) {
            return new GuardedInvocation(mh, NashornLinker.canLinkTypeStatic(sourceType) ? null : IS_NASHORN_OR_UNDEFINED_TYPE);
        }
        GuardedInvocation arrayConverter = NashornLinker.getArrayConverter(sourceType, targetType);
        if (arrayConverter != null) {
            return arrayConverter;
        }
        return NashornLinker.getMirrorConverter(sourceType, targetType);
    }

    private static GuardedInvocation getSamTypeConverter(Class<?> sourceType, Class<?> targetType) throws Exception {
        boolean isSourceTypeGeneric = sourceType.isAssignableFrom(ScriptFunction.class);
        if ((isSourceTypeGeneric || ScriptFunction.class.isAssignableFrom(sourceType)) && NashornLinker.isAutoConvertibleFromFunction(targetType)) {
            MethodHandle ctor = JavaAdapterFactory.getConstructor(ScriptFunction.class, targetType, NashornLinker.getCurrentLookup());
            assert (ctor != null);
            return new GuardedInvocation(ctor, isSourceTypeGeneric ? IS_SCRIPT_FUNCTION : null);
        }
        return null;
    }

    private static MethodHandles.Lookup getCurrentLookup() {
        LinkRequest currentRequest = AccessController.doPrivileged(new PrivilegedAction<LinkRequest>(){

            @Override
            public LinkRequest run() {
                return LinkerServicesImpl.getCurrentLinkRequest();
            }
        });
        return currentRequest == null ? MethodHandles.publicLookup() : currentRequest.getCallSiteDescriptor().getLookup();
    }

    private static GuardedInvocation getArrayConverter(Class<?> sourceType, Class<?> targetType) {
        boolean isSourceTypeGeneric;
        boolean isSourceTypeNativeArray = sourceType == NativeArray.class;
        boolean bl = isSourceTypeGeneric = !isSourceTypeNativeArray && sourceType.isAssignableFrom(NativeArray.class);
        if (isSourceTypeNativeArray || isSourceTypeGeneric) {
            MethodHandle guard;
            MethodHandle methodHandle = guard = isSourceTypeGeneric ? IS_NATIVE_ARRAY : null;
            if (targetType.isArray()) {
                return new GuardedInvocation(ARRAY_CONVERTERS.get(targetType), guard);
            }
            if (targetType == List.class) {
                return new GuardedInvocation(TO_LIST, guard);
            }
            if (targetType == Deque.class) {
                return new GuardedInvocation(TO_DEQUE, guard);
            }
            if (targetType == Queue.class) {
                return new GuardedInvocation(TO_QUEUE, guard);
            }
            if (targetType == Collection.class) {
                return new GuardedInvocation(TO_COLLECTION, guard);
            }
        }
        return null;
    }

    private static MethodHandle createArrayConverter(Class<?> type) {
        assert (type.isArray());
        MethodHandle converter = jdk.nashorn.internal.lookup.Lookup.MH.insertArguments(JSType.TO_JAVA_ARRAY.methodHandle(), 1, type.getComponentType());
        return jdk.nashorn.internal.lookup.Lookup.MH.asType(converter, converter.type().changeReturnType(type));
    }

    private static GuardedInvocation getMirrorConverter(Class<?> sourceType, Class<?> targetType) {
        if (targetType == Map.class || targetType == Bindings.class || targetType == JSObject.class || targetType == ScriptObjectMirror.class) {
            if (ScriptObject.class.isAssignableFrom(sourceType)) {
                return new GuardedInvocation(CREATE_MIRROR);
            }
            if (sourceType.isAssignableFrom(ScriptObject.class) || sourceType.isInterface()) {
                return new GuardedInvocation(CREATE_MIRROR, IS_SCRIPT_OBJECT);
            }
        }
        return null;
    }

    private static boolean isAutoConvertibleFromFunction(Class<?> clazz) {
        return NashornLinker.isAbstractClass(clazz) && !ScriptObject.class.isAssignableFrom(clazz) && JavaAdapterFactory.isAutoConvertibleFromFunction(clazz);
    }

    static boolean isAbstractClass(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers()) && !clazz.isArray();
    }

    @Override
    public ConversionComparator.Comparison compareConversion(Class<?> sourceType, Class<?> targetType1, Class<?> targetType2) {
        if (sourceType == NativeArray.class) {
            if (NashornLinker.isArrayPreferredTarget(targetType1)) {
                if (!NashornLinker.isArrayPreferredTarget(targetType2)) {
                    return ConversionComparator.Comparison.TYPE_1_BETTER;
                }
            } else if (NashornLinker.isArrayPreferredTarget(targetType2)) {
                return ConversionComparator.Comparison.TYPE_2_BETTER;
            }
            if (targetType1.isArray()) {
                if (!targetType2.isArray()) {
                    return ConversionComparator.Comparison.TYPE_1_BETTER;
                }
            } else if (targetType2.isArray()) {
                return ConversionComparator.Comparison.TYPE_2_BETTER;
            }
        }
        if (ScriptObject.class.isAssignableFrom(sourceType)) {
            if (targetType1.isInterface()) {
                if (!targetType2.isInterface()) {
                    return ConversionComparator.Comparison.TYPE_1_BETTER;
                }
            } else if (targetType2.isInterface()) {
                return ConversionComparator.Comparison.TYPE_2_BETTER;
            }
        }
        return ConversionComparator.Comparison.INDETERMINATE;
    }

    private static boolean isArrayPreferredTarget(Class<?> clazz) {
        return clazz == List.class || clazz == Collection.class || clazz == Queue.class || clazz == Deque.class;
    }

    private static MethodHandle asReturning(MethodHandle mh, Class<?> nrtype) {
        return mh.asType(mh.type().changeReturnType(nrtype));
    }

    private static boolean isNashornTypeOrUndefined(Object obj) {
        return obj instanceof ScriptObject || obj instanceof Undefined;
    }

    private static Object createMirror(Object obj) {
        return obj instanceof ScriptObject ? ScriptUtils.wrap((ScriptObject)obj) : obj;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return jdk.nashorn.internal.lookup.Lookup.MH.findStatic(MethodHandles.lookup(), NashornLinker.class, name, jdk.nashorn.internal.lookup.Lookup.MH.type(rtype, types));
    }

    static {
        MethodHandle listAdapterCreate = new Lookup(MethodHandles.lookup()).findStatic(ListAdapter.class, "create", MethodType.methodType(ListAdapter.class, Object.class));
        TO_COLLECTION = NashornLinker.asReturning(listAdapterCreate, Collection.class);
        TO_DEQUE = NashornLinker.asReturning(listAdapterCreate, Deque.class);
        TO_LIST = NashornLinker.asReturning(listAdapterCreate, List.class);
        TO_QUEUE = NashornLinker.asReturning(listAdapterCreate, Queue.class);
    }
}

