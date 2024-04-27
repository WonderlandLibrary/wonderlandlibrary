/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.MethodHandleTransformer;
import jdk.internal.dynalink.support.TypeConverterFactory;

public class LinkerServicesImpl
implements LinkerServices {
    private static final RuntimePermission GET_CURRENT_LINK_REQUEST = new RuntimePermission("dynalink.getCurrentLinkRequest");
    private static final ThreadLocal<LinkRequest> threadLinkRequest = new ThreadLocal();
    private final TypeConverterFactory typeConverterFactory;
    private final GuardingDynamicLinker topLevelLinker;
    private final MethodHandleTransformer internalObjectsFilter;

    public LinkerServicesImpl(TypeConverterFactory typeConverterFactory, GuardingDynamicLinker topLevelLinker, MethodHandleTransformer internalObjectsFilter) {
        this.typeConverterFactory = typeConverterFactory;
        this.topLevelLinker = topLevelLinker;
        this.internalObjectsFilter = internalObjectsFilter;
    }

    @Override
    public boolean canConvert(Class<?> from, Class<?> to) {
        return this.typeConverterFactory.canConvert(from, to);
    }

    @Override
    public MethodHandle asType(MethodHandle handle, MethodType fromType) {
        return this.typeConverterFactory.asType(handle, fromType);
    }

    @Override
    public MethodHandle asTypeLosslessReturn(MethodHandle handle, MethodType fromType) {
        return LinkerServices.Implementation.asTypeLosslessReturn(this, handle, fromType);
    }

    @Override
    public MethodHandle getTypeConverter(Class<?> sourceType, Class<?> targetType) {
        return this.typeConverterFactory.getTypeConverter(sourceType, targetType);
    }

    @Override
    public ConversionComparator.Comparison compareConversion(Class<?> sourceType, Class<?> targetType1, Class<?> targetType2) {
        return this.typeConverterFactory.compareConversion(sourceType, targetType1, targetType2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest) throws Exception {
        LinkRequest prevLinkRequest = threadLinkRequest.get();
        threadLinkRequest.set(linkRequest);
        try {
            GuardedInvocation guardedInvocation = this.topLevelLinker.getGuardedInvocation(linkRequest, this);
            return guardedInvocation;
        }
        finally {
            threadLinkRequest.set(prevLinkRequest);
        }
    }

    @Override
    public MethodHandle filterInternalObjects(MethodHandle target) {
        return this.internalObjectsFilter != null ? this.internalObjectsFilter.transform(target) : target;
    }

    public static LinkRequest getCurrentLinkRequest() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(GET_CURRENT_LINK_REQUEST);
        }
        return threadLinkRequest.get();
    }
}

