/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.internal;

import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementations;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b\u00a2\u0006\u0002\u0010\f\u001a\b\u0010\r\u001a\u00020\u0005H\u0002\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", "T", "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "getJavaVersion", "kotlin-stdlib"})
public final class PlatformImplementationsKt {
    @JvmField
    @NotNull
    public static final PlatformImplementations IMPLEMENTATIONS;

    @InlineOnly
    private static final /* synthetic */ <T> T castToBaseType(Object instance) {
        try {
            Intrinsics.reifiedOperationMarker(1, "T");
            return (T)instance;
        }
        catch (ClassCastException e) {
            ClassLoader instanceCL = instance.getClass().getClassLoader();
            Intrinsics.reifiedOperationMarker(4, "T");
            ClassLoader baseTypeCL = Object.class.getClassLoader();
            Throwable throwable = new ClassCastException("Instance classloader: " + instanceCL + ", base type classloader: " + baseTypeCL).initCause(e);
            Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
            throw throwable;
        }
    }

    private static final int getJavaVersion() {
        int n;
        int n2 = 65542;
        String string = System.getProperty("java.specification.version");
        if (string == null) {
            return n2;
        }
        String version = string;
        int firstDot = StringsKt.indexOf$default((CharSequence)version, '.', 0, false, 6, null);
        if (firstDot < 0) {
            int n3;
            try {
                n3 = Integer.parseInt(version) * 65536;
            }
            catch (NumberFormatException e) {
                n3 = n2;
            }
            return n3;
        }
        int secondDot = StringsKt.indexOf$default((CharSequence)version, '.', firstDot + 1, false, 4, null);
        if (secondDot < 0) {
            secondDot = version.length();
        }
        String string2 = version;
        int n4 = 0;
        String string3 = string2.substring(n4, firstDot);
        Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
        String firstPart = string3;
        String string4 = version;
        int n5 = firstDot + 1;
        String string5 = string4.substring(n5, secondDot);
        Intrinsics.checkNotNullExpressionValue(string5, "this as java.lang.String\u2026ing(startIndex, endIndex)");
        String secondPart = string5;
        try {
            n = Integer.parseInt(firstPart) * 65536 + Integer.parseInt(secondPart);
        }
        catch (NumberFormatException e) {
            n = n2;
        }
        return n;
    }

    @PublishedApi
    @SinceKotlin(version="1.2")
    public static final boolean apiVersionIsAtLeast(int major, int minor, int patch) {
        return KotlinVersion.CURRENT.isAtLeast(major, minor, patch);
    }

    static {
        PlatformImplementations platformImplementations;
        block18: {
            boolean bl = false;
            int version = PlatformImplementationsKt.getJavaVersion();
            if (version >= 65544) {
                try {
                    Object obj = Class.forName("kotlin.internal.jdk8.JDK8PlatformImplementations").newInstance();
                    Intrinsics.checkNotNullExpressionValue(obj, "forName(\"kotlin.internal\u2026entations\").newInstance()");
                    try {
                        platformImplementations = (PlatformImplementations)obj;
                        break block18;
                    }
                    catch (ClassCastException classCastException) {
                        ClassLoader classLoader = obj.getClass().getClassLoader();
                        ClassLoader classLoader2 = PlatformImplementations.class.getClassLoader();
                        Throwable throwable = new ClassCastException("Instance classloader: " + classLoader + ", base type classloader: " + classLoader2).initCause(classCastException);
                        Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                        throw throwable;
                    }
                }
                catch (ClassNotFoundException classNotFoundException) {
                    try {
                        Object obj = Class.forName("kotlin.internal.JRE8PlatformImplementations").newInstance();
                        Intrinsics.checkNotNullExpressionValue(obj, "forName(\"kotlin.internal\u2026entations\").newInstance()");
                        try {
                            platformImplementations = (PlatformImplementations)obj;
                            break block18;
                        }
                        catch (ClassCastException classCastException) {
                            ClassLoader classLoader = obj.getClass().getClassLoader();
                            ClassLoader classLoader3 = PlatformImplementations.class.getClassLoader();
                            Throwable throwable = new ClassCastException("Instance classloader: " + classLoader + ", base type classloader: " + classLoader3).initCause(classCastException);
                            Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                            throw throwable;
                        }
                    }
                    catch (ClassNotFoundException classNotFoundException2) {
                        // empty catch block
                    }
                }
            }
            if (version >= 65543) {
                try {
                    Object obj = Class.forName("kotlin.internal.jdk7.JDK7PlatformImplementations").newInstance();
                    Intrinsics.checkNotNullExpressionValue(obj, "forName(\"kotlin.internal\u2026entations\").newInstance()");
                    try {
                        platformImplementations = (PlatformImplementations)obj;
                        break block18;
                    }
                    catch (ClassCastException classCastException) {
                        ClassLoader classLoader = obj.getClass().getClassLoader();
                        ClassLoader classLoader4 = PlatformImplementations.class.getClassLoader();
                        Throwable throwable = new ClassCastException("Instance classloader: " + classLoader + ", base type classloader: " + classLoader4).initCause(classCastException);
                        Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                        throw throwable;
                    }
                }
                catch (ClassNotFoundException classNotFoundException) {
                    try {
                        Object obj = Class.forName("kotlin.internal.JRE7PlatformImplementations").newInstance();
                        Intrinsics.checkNotNullExpressionValue(obj, "forName(\"kotlin.internal\u2026entations\").newInstance()");
                        try {
                            platformImplementations = (PlatformImplementations)obj;
                            break block18;
                        }
                        catch (ClassCastException classCastException) {
                            ClassLoader classLoader = obj.getClass().getClassLoader();
                            ClassLoader classLoader5 = PlatformImplementations.class.getClassLoader();
                            Throwable throwable = new ClassCastException("Instance classloader: " + classLoader + ", base type classloader: " + classLoader5).initCause(classCastException);
                            Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                            throw throwable;
                        }
                    }
                    catch (ClassNotFoundException classNotFoundException3) {
                        // empty catch block
                    }
                }
            }
            platformImplementations = new PlatformImplementations();
        }
        IMPLEMENTATIONS = platformImplementations;
    }
}

