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
    private static HashMap<String, String> stringCache = new HashMap();

    public static String fixString(String str) {
        if (stringCache.containsKey(str)) {
            return stringCache.get(str);
        }
        str = str.replaceAll("\uf8ff", "");
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c > '\uff01' && c < '\uff60') {
                sb.append(Character.toChars(c - 65248));
                continue;
            }
            sb.append(c);
        }
        String result = sb.toString();
        stringCache.put(str, result);
        return result;
    }

    public static String toCompleteString(String[] args2, int start) {
        if (args2.length <= start) {
            return "";
        }
        return String.join((CharSequence)" ", Arrays.copyOfRange(args2, start, args2.length));
    }

    public static String replace(String string, String searchChars, String replaceChars) {
        if (string.isEmpty() || searchChars.isEmpty() || searchChars.equals(replaceChars)) {
            return string;
        }
        if (replaceChars == null) {
            replaceChars = "";
        }
        int stringLength = string.length();
        int searchCharsLength = searchChars.length();
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < stringLength; ++i) {
            int start = stringBuilder.indexOf(searchChars, i);
            if (start == -1) {
                if (i == 0) {
                    return string;
                }
                return stringBuilder.toString();
            }
            stringBuilder.replace(start, start + searchCharsLength, replaceChars);
        }
        return stringBuilder.toString();
    }
}

