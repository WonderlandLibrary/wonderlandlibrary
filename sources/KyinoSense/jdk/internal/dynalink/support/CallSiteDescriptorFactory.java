/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.DefaultCallSiteDescriptor;
import jdk.internal.dynalink.support.LookupCallSiteDescriptor;
import jdk.internal.dynalink.support.NameCodec;
import jdk.internal.dynalink.support.NamedDynCallSiteDescriptor;
import jdk.internal.dynalink.support.UnnamedDynCallSiteDescriptor;

public class CallSiteDescriptorFactory {
    private static final WeakHashMap<CallSiteDescriptor, Reference<CallSiteDescriptor>> publicDescs = new WeakHashMap();

    private CallSiteDescriptorFactory() {
    }

    public static CallSiteDescriptor create(MethodHandles.Lookup lookup, String name, MethodType methodType) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(methodType);
        Objects.requireNonNull(lookup);
        String[] tokenizedName = CallSiteDescriptorFactory.tokenizeName(name);
        if (CallSiteDescriptorFactory.isPublicLookup(lookup)) {
            return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(CallSiteDescriptorFactory.createPublicCallSiteDescriptor(tokenizedName, methodType));
        }
        return new LookupCallSiteDescriptor(tokenizedName, methodType, lookup);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static CallSiteDescriptor getCanonicalPublicDescriptor(CallSiteDescriptor desc) {
        WeakHashMap<CallSiteDescriptor, Reference<CallSiteDescriptor>> weakHashMap = publicDescs;
        synchronized (weakHashMap) {
            CallSiteDescriptor canonical;
            Reference<CallSiteDescriptor> ref = publicDescs.get(desc);
            if (ref != null && (canonical = ref.get()) != null) {
                return canonical;
            }
            publicDescs.put(desc, CallSiteDescriptorFactory.createReference(desc));
        }
        return desc;
    }

    protected static Reference<CallSiteDescriptor> createReference(CallSiteDescriptor desc) {
        return new WeakReference<CallSiteDescriptor>(desc);
    }

    private static CallSiteDescriptor createPublicCallSiteDescriptor(String[] tokenizedName, MethodType methodType) {
        int l = tokenizedName.length;
        if (l > 0 && tokenizedName[0] == "dyn") {
            if (l == 2) {
                return new UnnamedDynCallSiteDescriptor(tokenizedName[1], methodType);
            }
            if (l == 3) {
                return new NamedDynCallSiteDescriptor(tokenizedName[1], tokenizedName[2], methodType);
            }
        }
        return new DefaultCallSiteDescriptor(tokenizedName, methodType);
    }

    private static boolean isPublicLookup(MethodHandles.Lookup lookup) {
        return lookup == MethodHandles.publicLookup();
    }

    public static String[] tokenizeName(String name) {
        StringTokenizer tok = new StringTokenizer(name, ":");
        String[] tokens = new String[tok.countTokens()];
        for (int i = 0; i < tokens.length; ++i) {
            String token = tok.nextToken();
            if (i > 1) {
                token = NameCodec.decode(token);
            }
            tokens[i] = token.intern();
        }
        return tokens;
    }

    public static List<String> tokenizeOperators(CallSiteDescriptor desc) {
        String ops = desc.getNameToken(1);
        StringTokenizer tok = new StringTokenizer(ops, "|");
        int count = tok.countTokens();
        if (count == 1) {
            return Collections.singletonList(ops);
        }
        String[] tokens = new String[count];
        for (int i = 0; i < count; ++i) {
            tokens[i] = tok.nextToken();
        }
        return Arrays.asList(tokens);
    }

    public static CallSiteDescriptor dropParameterTypes(CallSiteDescriptor desc, int start, int end) {
        return desc.changeMethodType(desc.getMethodType().dropParameterTypes(start, end));
    }

    public static CallSiteDescriptor changeParameterType(CallSiteDescriptor desc, int num, Class<?> nptype) {
        return desc.changeMethodType(desc.getMethodType().changeParameterType(num, nptype));
    }

    public static CallSiteDescriptor changeReturnType(CallSiteDescriptor desc, Class<?> nrtype) {
        return desc.changeMethodType(desc.getMethodType().changeReturnType(nrtype));
    }

    public static CallSiteDescriptor insertParameterTypes(CallSiteDescriptor desc, int num, Class<?> ... ptypesToInsert) {
        return desc.changeMethodType(desc.getMethodType().insertParameterTypes(num, ptypesToInsert));
    }

    public static CallSiteDescriptor insertParameterTypes(CallSiteDescriptor desc, int num, List<Class<?>> ptypesToInsert) {
        return desc.changeMethodType(desc.getMethodType().insertParameterTypes(num, ptypesToInsert));
    }
}

