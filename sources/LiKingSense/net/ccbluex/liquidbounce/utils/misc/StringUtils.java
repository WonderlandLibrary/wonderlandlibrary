/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils.misc;

import java.util.Arrays;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class StringUtils {
    public static String breakString(String s) {
        StringBuilder sb = new StringBuilder();
        String[] sArray = s.split("");
        int index = 0;
        for (String s1 : sArray) {
            if (s1.equals("")) continue;
            if (s1.equals(s1.toUpperCase()) && Character.isLetter(s1.toCharArray()[0]) && index != 0) {
                sb.append(" ");
            }
            sb.append(s1);
            ++index;
        }
        return sb.toString();
    }

    public static String toCompleteString(String[] args, int start) {
        if (args.length <= start) {
            return "";
        }
        return String.join((CharSequence)" ", Arrays.copyOfRange(args, start, args.length));
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

