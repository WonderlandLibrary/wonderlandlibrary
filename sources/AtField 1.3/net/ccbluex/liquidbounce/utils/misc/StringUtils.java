/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils.misc;

import java.util.Arrays;
import java.util.HashMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class StringUtils {
    private static HashMap stringCache = new HashMap();

    public static String breakString(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] stringArray = string.split("");
        int n = 0;
        for (String string2 : stringArray) {
            if (string2.equals("")) continue;
            if (string2.equals(string2.toUpperCase()) && Character.isLetter(string2.toCharArray()[0]) && n != 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(string2);
            ++n;
        }
        return stringBuilder.toString();
    }

    public static String fixString(String string) {
        if (stringCache.containsKey(string)) {
            return (String)stringCache.get(string);
        }
        string = string.replaceAll("\uf8ff", "");
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (c > '\uff01' && c < '\uff60') {
                stringBuilder.append(Character.toChars(c - 65248));
                continue;
            }
            stringBuilder.append(c);
        }
        String string2 = stringBuilder.toString();
        stringCache.put(string, string2);
        return string2;
    }

    public static String replace(String string, String string2, String string3) {
        if (string.isEmpty() || string2.isEmpty() || string2.equals(string3)) {
            return string;
        }
        if (string3 == null) {
            string3 = "";
        }
        int n = string.length();
        int n2 = string2.length();
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < n; ++i) {
            int n3 = stringBuilder.indexOf(string2, i);
            if (n3 == -1) {
                if (i == 0) {
                    return string;
                }
                return stringBuilder.toString();
            }
            stringBuilder.replace(n3, n3 + n2, string3);
        }
        return stringBuilder.toString();
    }

    public static String toCompleteString(String[] stringArray, int n) {
        if (stringArray.length <= n) {
            return "";
        }
        return String.join((CharSequence)" ", Arrays.copyOfRange(stringArray, n, stringArray.length));
    }
}

