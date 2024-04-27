/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.io.FilesKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ResourceLocation
 *  org.apache.commons.io.FileUtils
 */
package net.ccbluex.liquidbounce.script.remapper;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.TypeCastException;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

public final class Remapper {
    private static final String srgName;
    public static final Remapper INSTANCE;
    private static final HashMap methods;
    private static final HashMap fields;
    private static final File srgFile;

    public final String remapField(Class clazz, String string) {
        if (!fields.containsKey(clazz.getName())) {
            return string;
        }
        Object v = fields.get(clazz.getName());
        if (v == null) {
            Intrinsics.throwNpe();
        }
        return ((HashMap)v).getOrDefault(string, string);
    }

    private final void parseSrg() {
        Iterable iterable = FilesKt.readLines$default((File)srgFile, null, (int)1, null);
        boolean bl = false;
        for (Object t : iterable) {
            HashMap hashMap;
            Map map;
            String string;
            boolean bl2;
            String string2;
            String string3;
            String string4;
            String string5;
            String string6;
            String string7 = (String)t;
            boolean bl3 = false;
            List list = StringsKt.split$default((CharSequence)string7, (String[])new String[]{" "}, (boolean)false, (int)0, (int)6, null);
            if (StringsKt.startsWith$default((String)string7, (String)"FD:", (boolean)false, (int)2, null)) {
                string6 = (String)list.get(1);
                string5 = (String)list.get(2);
                string4 = string6;
                int n = 0;
                int n2 = StringsKt.lastIndexOf$default((CharSequence)string6, (char)'/', (int)0, (boolean)false, (int)6, null);
                int n3 = 0;
                String string8 = string4;
                if (string8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string3 = StringsKt.replace$default((String)string8.substring(n, n2), (char)'/', (char)'.', (boolean)false, (int)4, null);
                string2 = string6;
                n2 = StringsKt.lastIndexOf$default((CharSequence)string6, (char)'/', (int)0, (boolean)false, (int)6, null) + 1;
                n3 = 0;
                String string9 = string2;
                if (string9 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string4 = string9.substring(n2);
                Object object = string5;
                n3 = StringsKt.lastIndexOf$default((CharSequence)string5, (char)'/', (int)0, (boolean)false, (int)6, null) + 1;
                boolean bl4 = false;
                String string10 = object;
                if (string10 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string2 = string10.substring(n3);
                object = fields;
                n3 = 0;
                Object object2 = object;
                bl2 = false;
                if (!object2.containsKey(string3)) {
                    string = string3;
                    map = fields;
                    boolean bl5 = false;
                    hashMap = new HashMap();
                    map.put(string, hashMap);
                }
                Object v = fields.get(string3);
                if (v == null) {
                    Intrinsics.throwNpe();
                }
                ((Map)v).put(string2, string4);
                continue;
            }
            if (!StringsKt.startsWith$default((String)string7, (String)"MD:", (boolean)false, (int)2, null)) continue;
            string6 = (String)list.get(1);
            string5 = (String)list.get(2);
            string3 = (String)list.get(3);
            string2 = string6;
            int n = 0;
            int n4 = StringsKt.lastIndexOf$default((CharSequence)string6, (char)'/', (int)0, (boolean)false, (int)6, null);
            int n5 = 0;
            String string11 = string2;
            if (string11 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string4 = StringsKt.replace$default((String)string11.substring(n, n4), (char)'/', (char)'.', (boolean)false, (int)4, null);
            String string12 = string6;
            n4 = StringsKt.lastIndexOf$default((CharSequence)string6, (char)'/', (int)0, (boolean)false, (int)6, null) + 1;
            n5 = 0;
            String string13 = string12;
            if (string13 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string2 = string13.substring(n4);
            Object object = string3;
            n5 = StringsKt.lastIndexOf$default((CharSequence)string3, (char)'/', (int)0, (boolean)false, (int)6, null) + 1;
            bl2 = false;
            String string14 = object;
            if (string14 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string12 = string14.substring(n5);
            object = methods;
            n5 = 0;
            Object object3 = object;
            boolean bl6 = false;
            if (!object3.containsKey(string4)) {
                string = string4;
                map = methods;
                boolean bl7 = false;
                hashMap = new HashMap();
                map.put(string, hashMap);
            }
            Object v = methods.get(string4);
            if (v == null) {
                Intrinsics.throwNpe();
            }
            ((Map)v).put(string12 + string5, string2);
        }
    }

    public final String remapMethod(Class clazz, String string, String string2) {
        if (!methods.containsKey(clazz.getName())) {
            return string;
        }
        Object v = methods.get(clazz.getName());
        if (v == null) {
            Intrinsics.throwNpe();
        }
        return ((HashMap)v).getOrDefault(string + string2, string);
    }

    private Remapper() {
    }

    public final void loadSrg() {
        if (!srgFile.exists()) {
            srgFile.createNewFile();
            ClientUtils.getLogger().info("[Remapper] Extracting stable_22 srg...");
            FileUtils.copyInputStreamToFile((InputStream)Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("More/mcp-stable_22.srg")).func_110527_b(), (File)srgFile);
        }
        ClientUtils.getLogger().info("[Remapper] Loading srg...");
        this.parseSrg();
        ClientUtils.getLogger().info("[Remapper] Loaded srg.");
    }

    static {
        Remapper remapper;
        srgName = "stable_22";
        INSTANCE = remapper = new Remapper();
        srgFile = new File(LiquidBounce.INSTANCE.getFileManager().dir, "mcp-stable_22.srg");
        boolean bl = false;
        fields = new HashMap();
        bl = false;
        methods = new HashMap();
    }
}

