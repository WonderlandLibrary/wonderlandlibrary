/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.io.FilesKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.script.remapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.TypeCastException;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;

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
        Iterable $this$forEach$iv = FilesKt.readLines$default((File)srgFile, null, (int)1, null);
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            HashMap hashMap;
            Map map;
            String string;
            boolean bl;
            String fieldSrg;
            String name;
            String it = (String)element$iv;
            boolean bl2 = false;
            List args = StringsKt.split$default((CharSequence)it, (String[])new String[]{" "}, (boolean)false, (int)0, (int)6, null);
            if (StringsKt.startsWith$default((String)it, (String)"FD:", (boolean)false, (int)2, null)) {
                name = (String)args.get(1);
                String srg = (String)args.get(2);
                String string2 = name;
                int n = 0;
                int n2 = StringsKt.lastIndexOf$default((CharSequence)name, (char)'/', (int)0, (boolean)false, (int)6, null);
                int n3 = 0;
                String string3 = string2;
                if (string3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String className = StringsKt.replace$default((String)string3.substring(n, n2), (char)'/', (char)'.', (boolean)false, (int)4, null);
                String string4 = name;
                n2 = StringsKt.lastIndexOf$default((CharSequence)name, (char)'/', (int)0, (boolean)false, (int)6, null) + 1;
                n3 = 0;
                String string5 = string4;
                if (string5 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String fieldName = string5.substring(n2);
                Object object = srg;
                n3 = StringsKt.lastIndexOf$default((CharSequence)srg, (char)'/', (int)0, (boolean)false, (int)6, null) + 1;
                boolean bl3 = false;
                String string6 = object;
                if (string6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                fieldSrg = string6.substring(n3);
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
                ((Map)hashMap2).put(fieldSrg, fieldName);
                continue;
            }
            if (!StringsKt.startsWith$default((String)it, (String)"MD:", (boolean)false, (int)2, null)) continue;
            name = (String)args.get(1);
            String desc = (String)args.get(2);
            String srg = (String)args.get(3);
            fieldSrg = name;
            int n = 0;
            int n4 = StringsKt.lastIndexOf$default((CharSequence)name, (char)'/', (int)0, (boolean)false, (int)6, null);
            int n5 = 0;
            String string7 = fieldSrg;
            if (string7 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String className = StringsKt.replace$default((String)string7.substring(n, n4), (char)'/', (char)'.', (boolean)false, (int)4, null);
            String string8 = name;
            n4 = StringsKt.lastIndexOf$default((CharSequence)name, (char)'/', (int)0, (boolean)false, (int)6, null) + 1;
            n5 = 0;
            String string9 = string8;
            if (string9 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String methodName = string9.substring(n4);
            Object object = srg;
            n5 = StringsKt.lastIndexOf$default((CharSequence)srg, (char)'/', (int)0, (boolean)false, (int)6, null) + 1;
            bl = false;
            String string10 = object;
            if (string10 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String methodSrg = string10.substring(n5);
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
            ((Map)hashMap3).put(methodSrg + desc, methodName);
        }
    }

    public final String remapField(Class<?> clazz, String name) {
        if (!fields.containsKey(clazz.getName())) {
            return name;
        }
        HashMap<String, String> hashMap = fields.get(clazz.getName());
        if (hashMap == null) {
            Intrinsics.throwNpe();
        }
        return hashMap.getOrDefault(name, name);
    }

    public final String remapMethod(Class<?> clazz, String name, String desc) {
        if (!methods.containsKey(clazz.getName())) {
            return name;
        }
        HashMap<String, String> hashMap = methods.get(clazz.getName());
        if (hashMap == null) {
            Intrinsics.throwNpe();
        }
        return hashMap.getOrDefault(name + desc, name);
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

