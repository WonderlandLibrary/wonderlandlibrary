/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.script.remapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\r\u001a\u00020\fH\u0002J\u001a\u0010\u000e\u001a\u00020\u00052\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u00102\u0006\u0010\u0011\u001a\u00020\u0005J\"\u0010\u0012\u001a\u00020\u00052\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u00102\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005RR\u0010\u0003\u001aF\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00040\u0004j*\u0012\u0004\u0012\u00020\u0005\u0012 \u0012\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005`\u0006`\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000RR\u0010\u0007\u001aF\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00040\u0004j*\u0012\u0004\u0012\u00020\u0005\u0012 \u0012\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005`\u0006`\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/script/remapper/Remapper;", "", "()V", "fields", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "methods", "srgFile", "Ljava/io/File;", "srgName", "loadSrg", "", "parseSrg", "remapField", "clazz", "Ljava/lang/Class;", "name", "remapMethod", "desc", "KyinoClient"})
public final class Remapper {
    private static final String srgName = "stable_22";
    private static final File srgFile;
    private static final HashMap<String, HashMap<String, String>> fields;
    private static final HashMap<String, HashMap<String, String>> methods;
    public static final Remapper INSTANCE;

    public final void loadSrg() {
        if (!srgFile.exists()) {
            srgFile.createNewFile();
            ClientUtils.getLogger().info("[Remapper] Downloading stable_22 srg...");
            HttpUtils.download("https://cloud.liquidbounce.net/LiquidBounce/srgs/mcp-stable_22.srg", srgFile);
            ClientUtils.getLogger().info("[Remapper] Downloaded stable_22.");
        }
        ClientUtils.getLogger().info("[Remapper] Loading srg...");
        this.parseSrg();
        ClientUtils.getLogger().info("[Remapper] Loaded srg.");
    }

    private final void parseSrg() {
        Iterable $this$forEach$iv = FilesKt.readLines$default(srgFile, null, 1, null);
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            String methodName;
            String methodSrg;
            String fieldSrg;
            HashMap hashMap;
            Map map;
            String string;
            boolean bl;
            String name;
            String it = (String)element$iv;
            boolean bl2 = false;
            List args2 = StringsKt.split$default((CharSequence)it, new String[]{" "}, false, 0, 6, null);
            if (StringsKt.startsWith$default(it, "FD:", false, 2, null)) {
                String fieldName;
                name = (String)args2.get(1);
                String srg = (String)args2.get(2);
                String string2 = name;
                int n = 0;
                int n2 = StringsKt.lastIndexOf$default((CharSequence)name, '/', 0, false, 6, null);
                int n3 = 0;
                String string3 = string2;
                if (string3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string4 = string3.substring(n, n2);
                Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                String className = StringsKt.replace$default(string4, '/', '.', false, 4, null);
                String string5 = name;
                n2 = StringsKt.lastIndexOf$default((CharSequence)name, '/', 0, false, 6, null) + 1;
                n3 = 0;
                String string6 = string5;
                if (string6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                Intrinsics.checkExpressionValueIsNotNull(string6.substring(n2), "(this as java.lang.String).substring(startIndex)");
                Object object = srg;
                n3 = StringsKt.lastIndexOf$default((CharSequence)srg, '/', 0, false, 6, null) + 1;
                boolean bl3 = false;
                String string7 = object;
                if (string7 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                Intrinsics.checkExpressionValueIsNotNull(string7.substring(n3), "(this as java.lang.String).substring(startIndex)");
                object = fields;
                n3 = 0;
                Object object2 = object;
                bl = false;
                if (!object2.containsKey(className)) {
                    string = className;
                    map = fields;
                    boolean bl4 = false;
                    hashMap = new HashMap();
                    map.put(string, hashMap);
                }
                HashMap<String, String> hashMap2 = fields.get(className);
                if (hashMap2 == null) {
                    Intrinsics.throwNpe();
                }
                Intrinsics.checkExpressionValueIsNotNull(hashMap2, "fields[className]!!");
                ((Map)hashMap2).put(fieldSrg, fieldName);
                continue;
            }
            if (!StringsKt.startsWith$default(it, "MD:", false, 2, null)) continue;
            name = (String)args2.get(1);
            String desc = (String)args2.get(2);
            String srg = (String)args2.get(3);
            fieldSrg = name;
            int n = 0;
            int n4 = StringsKt.lastIndexOf$default((CharSequence)name, '/', 0, false, 6, null);
            int n5 = 0;
            String string8 = fieldSrg;
            if (string8 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string9 = string8.substring(n, n4);
            Intrinsics.checkExpressionValueIsNotNull(string9, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            String className = StringsKt.replace$default(string9, '/', '.', false, 4, null);
            String string10 = name;
            n4 = StringsKt.lastIndexOf$default((CharSequence)name, '/', 0, false, 6, null) + 1;
            n5 = 0;
            String string11 = string10;
            if (string11 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            Intrinsics.checkExpressionValueIsNotNull(string11.substring(n4), "(this as java.lang.String).substring(startIndex)");
            Object object = srg;
            n5 = StringsKt.lastIndexOf$default((CharSequence)srg, '/', 0, false, 6, null) + 1;
            bl = false;
            String string12 = object;
            if (string12 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            Intrinsics.checkExpressionValueIsNotNull(string12.substring(n5), "(this as java.lang.String).substring(startIndex)");
            object = methods;
            n5 = 0;
            Object object3 = object;
            boolean bl5 = false;
            if (!object3.containsKey(className)) {
                string = className;
                map = methods;
                boolean bl6 = false;
                hashMap = new HashMap();
                map.put(string, hashMap);
            }
            HashMap<String, String> hashMap3 = methods.get(className);
            if (hashMap3 == null) {
                Intrinsics.throwNpe();
            }
            Intrinsics.checkExpressionValueIsNotNull(hashMap3, "methods[className]!!");
            ((Map)hashMap3).put(methodSrg + desc, methodName);
        }
    }

    @NotNull
    public final String remapField(@NotNull Class<?> clazz, @NotNull String name) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        Intrinsics.checkParameterIsNotNull(name, "name");
        if (!fields.containsKey(clazz.getName())) {
            return name;
        }
        HashMap<String, String> hashMap = fields.get(clazz.getName());
        if (hashMap == null) {
            Intrinsics.throwNpe();
        }
        String string = hashMap.getOrDefault(name, name);
        Intrinsics.checkExpressionValueIsNotNull(string, "fields[clazz.name]!!.getOrDefault(name, name)");
        return string;
    }

    @NotNull
    public final String remapMethod(@NotNull Class<?> clazz, @NotNull String name, @NotNull String desc) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(desc, "desc");
        if (!methods.containsKey(clazz.getName())) {
            return name;
        }
        HashMap<String, String> hashMap = methods.get(clazz.getName());
        if (hashMap == null) {
            Intrinsics.throwNpe();
        }
        String string = hashMap.getOrDefault(name + desc, name);
        Intrinsics.checkExpressionValueIsNotNull(string, "methods[clazz.name]!!.ge\u2026efault(name + desc, name)");
        return string;
    }

    private Remapper() {
    }

    static {
        Remapper remapper;
        INSTANCE = remapper = new Remapper();
        srgFile = new File(LiquidBounce.INSTANCE.getFileManager().dir, "mcp-stable_22.srg");
        boolean bl = false;
        fields = new HashMap();
        bl = false;
        methods = new HashMap();
    }
}

