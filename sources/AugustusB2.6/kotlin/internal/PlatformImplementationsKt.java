// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.internal;

import kotlin.jvm.internal.Intrinsics;
import kotlin.TypeCastException;

public final class PlatformImplementationsKt
{
    public static final PlatformImplementations IMPLEMENTATIONS;
    
    private static final int getJavaVersion() {
        final String property = System.getProperty("java.specification.version");
        if (property == null) {
            return 65542;
        }
        final String version = property;
        final int firstDot;
        if ((firstDot = StringsKt__StringsKt.indexOf$default$b46a3c3(property, '.', 0, false, 6)) < 0) {
            int n;
            try {
                n = Integer.parseInt(version) << 16;
            }
            catch (NumberFormatException ex) {
                n = 65542;
            }
            return n;
        }
        int secondDot;
        if ((secondDot = StringsKt__StringsKt.indexOf$default$b46a3c3(version, '.', firstDot + 1, false, 4)) < 0) {
            secondDot = version.length();
        }
        final String s = version;
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = s.substring(0, firstDot);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        final String firstPart = substring;
        final String s2 = version;
        final int beginIndex = firstDot + 1;
        final String s3 = s2;
        if (s3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring2 = s3.substring(beginIndex, secondDot);
        Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        final String secondPart = substring2;
        int n2;
        try {
            n2 = (Integer.parseInt(firstPart) << 16) + Integer.parseInt(secondPart);
        }
        catch (NumberFormatException ex2) {
            n2 = 65542;
        }
        return n2;
    }
    
    static {
        PlatformImplementations implementations = null;
        Label_0415: {
            final int version;
            if ((version = getJavaVersion()) >= 65544) {
                try {
                    final Object instance = Class.forName("kotlin.internal.jdk8.JDK8PlatformImplementations").newInstance();
                    Intrinsics.checkExpressionValueIsNotNull(instance, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                    final Object o = instance;
                    try {
                        final Object o2 = o;
                        if (o2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        }
                        implementations = (PlatformImplementations)o2;
                        break Label_0415;
                    }
                    catch (ClassCastException ex) {
                        final Throwable initCause = new ClassCastException("Instance classloader: " + ((PlatformImplementations)o).getClass().getClassLoader() + ", base type classloader: " + PlatformImplementations.class.getClassLoader()).initCause(ex);
                        Intrinsics.checkExpressionValueIsNotNull(initCause, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                        throw initCause;
                    }
                }
                catch (ClassNotFoundException ex5) {
                    try {
                        final Object instance2 = Class.forName("kotlin.internal.JRE8PlatformImplementations").newInstance();
                        Intrinsics.checkExpressionValueIsNotNull(instance2, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                        final Object o3 = instance2;
                        try {
                            final Object o4 = o3;
                            if (o4 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                            }
                            implementations = (PlatformImplementations)o4;
                            break Label_0415;
                        }
                        catch (ClassCastException ex2) {
                            final Throwable initCause2 = new ClassCastException("Instance classloader: " + ((PlatformImplementations)o3).getClass().getClassLoader() + ", base type classloader: " + PlatformImplementations.class.getClassLoader()).initCause(ex2);
                            Intrinsics.checkExpressionValueIsNotNull(initCause2, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                            throw initCause2;
                        }
                    }
                    catch (ClassNotFoundException ex6) {}
                }
            }
            if (version >= 65543) {
                try {
                    final Object instance3 = Class.forName("kotlin.internal.jdk7.JDK7PlatformImplementations").newInstance();
                    Intrinsics.checkExpressionValueIsNotNull(instance3, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                    final Object o5 = instance3;
                    try {
                        final Object o6 = o5;
                        if (o6 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        }
                        implementations = (PlatformImplementations)o6;
                        break Label_0415;
                    }
                    catch (ClassCastException ex3) {
                        final Throwable initCause3 = new ClassCastException("Instance classloader: " + ((PlatformImplementations)o5).getClass().getClassLoader() + ", base type classloader: " + PlatformImplementations.class.getClassLoader()).initCause(ex3);
                        Intrinsics.checkExpressionValueIsNotNull(initCause3, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                        throw initCause3;
                    }
                }
                catch (ClassNotFoundException ex7) {
                    try {
                        final Object instance4 = Class.forName("kotlin.internal.JRE7PlatformImplementations").newInstance();
                        Intrinsics.checkExpressionValueIsNotNull(instance4, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                        final Object o7 = instance4;
                        try {
                            final Object o8 = o7;
                            if (o8 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                            }
                            implementations = (PlatformImplementations)o8;
                            break Label_0415;
                        }
                        catch (ClassCastException ex4) {
                            final Throwable initCause4 = new ClassCastException("Instance classloader: " + ((PlatformImplementations)o7).getClass().getClassLoader() + ", base type classloader: " + PlatformImplementations.class.getClassLoader()).initCause(ex4);
                            Intrinsics.checkExpressionValueIsNotNull(initCause4, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                            throw initCause4;
                        }
                    }
                    catch (ClassNotFoundException ex8) {}
                }
            }
            implementations = new PlatformImplementations();
        }
        IMPLEMENTATIONS = implementations;
    }
}
