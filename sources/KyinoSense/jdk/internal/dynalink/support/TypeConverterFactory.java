/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import java.lang.invoke.WrongMethodTypeException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardedTypeConversion;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import jdk.internal.dynalink.linker.MethodTypeConversionStrategy;
import jdk.internal.dynalink.support.ClassLoaderGetterContextProvider;
import jdk.internal.dynalink.support.ClassMap;
import jdk.internal.dynalink.support.TypeUtilities;

public class TypeConverterFactory {
    private final GuardingTypeConverterFactory[] factories;
    private final ConversionComparator[] comparators;
    private final MethodTypeConversionStrategy autoConversionStrategy;
    private final ClassValue<ClassMap<MethodHandle>> converterMap = new ClassValue<ClassMap<MethodHandle>>(){

        @Override
        protected ClassMap<MethodHandle> computeValue(final Class<?> sourceType) {
            return new ClassMap<MethodHandle>(TypeConverterFactory.getClassLoader(sourceType)){

                @Override
                protected MethodHandle computeValue(Class<?> targetType) {
                    try {
                        return TypeConverterFactory.this.createConverter(sourceType, targetType);
                    }
                    catch (RuntimeException e) {
                        throw e;
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    };
    private final ClassValue<ClassMap<MethodHandle>> converterIdentityMap = new ClassValue<ClassMap<MethodHandle>>(){

        @Override
        protected ClassMap<MethodHandle> computeValue(final Class<?> sourceType) {
            return new ClassMap<MethodHandle>(TypeConverterFactory.getClassLoader(sourceType)){

                @Override
                protected MethodHandle computeValue(Class<?> targetType) {
                    MethodHandle converter;
                    if (!TypeConverterFactory.canAutoConvert(sourceType, targetType) && (converter = TypeConverterFactory.this.getCacheableTypeConverter(sourceType, targetType)) != IDENTITY_CONVERSION) {
                        return converter;
                    }
                    return IDENTITY_CONVERSION.asType(MethodType.methodType(targetType, sourceType));
                }
            };
        }
    };
    private final ClassValue<ClassMap<Boolean>> canConvert = new ClassValue<ClassMap<Boolean>>(){

        @Override
        protected ClassMap<Boolean> computeValue(final Class<?> sourceType) {
            return new ClassMap<Boolean>(TypeConverterFactory.getClassLoader(sourceType)){

                @Override
                protected Boolean computeValue(Class<?> targetType) {
                    try {
                        return TypeConverterFactory.this.getTypeConverterNull(sourceType, targetType) != null;
                    }
                    catch (RuntimeException e) {
                        throw e;
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    };
    static final MethodHandle IDENTITY_CONVERSION = MethodHandles.identity(Object.class);

    private static final ClassLoader getClassLoader(final Class<?> clazz) {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

            @Override
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        }, ClassLoaderGetterContextProvider.GET_CLASS_LOADER_CONTEXT);
    }

    public TypeConverterFactory(Iterable<? extends GuardingTypeConverterFactory> factories, MethodTypeConversionStrategy autoConversionStrategy) {
        LinkedList<GuardingTypeConverterFactory> l = new LinkedList<GuardingTypeConverterFactory>();
        LinkedList<ConversionComparator> c = new LinkedList<ConversionComparator>();
        for (GuardingTypeConverterFactory guardingTypeConverterFactory : factories) {
            l.add(guardingTypeConverterFactory);
            if (!(guardingTypeConverterFactory instanceof ConversionComparator)) continue;
            c.add((ConversionComparator)((Object)guardingTypeConverterFactory));
        }
        this.factories = l.toArray(new GuardingTypeConverterFactory[l.size()]);
        this.comparators = c.toArray(new ConversionComparator[c.size()]);
        this.autoConversionStrategy = autoConversionStrategy;
    }

    public MethodHandle asType(MethodHandle handle, MethodType fromType) {
        MethodHandle converter;
        MethodHandle newHandle = handle;
        MethodType toType = newHandle.type();
        int l = toType.parameterCount();
        if (l != fromType.parameterCount()) {
            throw new WrongMethodTypeException("Parameter counts differ: " + handle.type() + " vs. " + fromType);
        }
        int pos = 0;
        LinkedList<MethodHandle> converters = new LinkedList<MethodHandle>();
        for (int i = 0; i < l; ++i) {
            TypeDescriptor.OfField toParamType;
            TypeDescriptor.OfField fromParamType = fromType.parameterType(i);
            if (TypeConverterFactory.canAutoConvert(fromParamType, toParamType = toType.parameterType(i))) {
                newHandle = TypeConverterFactory.applyConverters(newHandle, pos, converters);
                continue;
            }
            MethodHandle converter2 = this.getTypeConverterNull((Class<?>)fromParamType, (Class<?>)toParamType);
            if (converter2 != null) {
                if (converters.isEmpty()) {
                    pos = i;
                }
                converters.add(converter2);
                continue;
            }
            newHandle = TypeConverterFactory.applyConverters(newHandle, pos, converters);
        }
        newHandle = TypeConverterFactory.applyConverters(newHandle, pos, converters);
        TypeDescriptor.OfField fromRetType = fromType.returnType();
        TypeDescriptor.OfField toRetType = toType.returnType();
        if (fromRetType != Void.TYPE && toRetType != Void.TYPE && !TypeConverterFactory.canAutoConvert(toRetType, fromRetType) && (converter = this.getTypeConverterNull((Class<?>)toRetType, (Class<?>)fromRetType)) != null) {
            newHandle = MethodHandles.filterReturnValue(newHandle, converter);
        }
        MethodHandle autoConvertedHandle = this.autoConversionStrategy != null ? this.autoConversionStrategy.asType(newHandle, fromType) : newHandle;
        return autoConvertedHandle.asType(fromType);
    }

    private static MethodHandle applyConverters(MethodHandle handle, int pos, List<MethodHandle> converters) {
        if (converters.isEmpty()) {
            return handle;
        }
        MethodHandle newHandle = MethodHandles.filterArguments(handle, pos, converters.toArray(new MethodHandle[converters.size()]));
        converters.clear();
        return newHandle;
    }

    public boolean canConvert(Class<?> from, Class<?> to) {
        return TypeConverterFactory.canAutoConvert(from, to) || this.canConvert.get(from).get(to) != false;
    }

    public ConversionComparator.Comparison compareConversion(Class<?> sourceType, Class<?> targetType1, Class<?> targetType2) {
        for (ConversionComparator comparator : this.comparators) {
            ConversionComparator.Comparison result = comparator.compareConversion(sourceType, targetType1, targetType2);
            if (result == ConversionComparator.Comparison.INDETERMINATE) continue;
            return result;
        }
        if (TypeUtilities.isMethodInvocationConvertible(sourceType, targetType1)) {
            if (!TypeUtilities.isMethodInvocationConvertible(sourceType, targetType2)) {
                return ConversionComparator.Comparison.TYPE_1_BETTER;
            }
        } else if (TypeUtilities.isMethodInvocationConvertible(sourceType, targetType2)) {
            return ConversionComparator.Comparison.TYPE_2_BETTER;
        }
        return ConversionComparator.Comparison.INDETERMINATE;
    }

    static boolean canAutoConvert(Class<?> fromType, Class<?> toType) {
        return TypeUtilities.isMethodInvocationConvertible(fromType, toType);
    }

    MethodHandle getCacheableTypeConverterNull(Class<?> sourceType, Class<?> targetType) {
        MethodHandle converter = this.getCacheableTypeConverter(sourceType, targetType);
        return converter == IDENTITY_CONVERSION ? null : converter;
    }

    MethodHandle getTypeConverterNull(Class<?> sourceType, Class<?> targetType) {
        try {
            return this.getCacheableTypeConverterNull(sourceType, targetType);
        }
        catch (NotCacheableConverter e) {
            return e.converter;
        }
    }

    MethodHandle getCacheableTypeConverter(Class<?> sourceType, Class<?> targetType) {
        return this.converterMap.get(sourceType).get(targetType);
    }

    public MethodHandle getTypeConverter(Class<?> sourceType, Class<?> targetType) {
        try {
            return this.converterIdentityMap.get(sourceType).get(targetType);
        }
        catch (NotCacheableConverter e) {
            return e.converter;
        }
    }

    MethodHandle createConverter(Class<?> sourceType, Class<?> targetType) throws Exception {
        MethodHandle identity;
        MethodType type = MethodType.methodType(targetType, sourceType);
        MethodHandle last = identity = IDENTITY_CONVERSION.asType(type);
        boolean cacheable = true;
        int i = this.factories.length;
        while (i-- > 0) {
            GuardedTypeConversion next = this.factories[i].convertToType(sourceType, targetType);
            if (next == null) continue;
            cacheable = cacheable && next.isCacheable();
            GuardedInvocation conversionInvocation = next.getConversionInvocation();
            conversionInvocation.assertType(type);
            last = conversionInvocation.compose(last);
        }
        if (last == identity) {
            return IDENTITY_CONVERSION;
        }
        if (cacheable) {
            return last;
        }
        throw new NotCacheableConverter(last);
    }

    private static class NotCacheableConverter
    extends RuntimeException {
        final MethodHandle converter;

        NotCacheableConverter(MethodHandle converter) {
            super("", null, false, false);
            this.converter = converter;
        }
    }
}

