/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class StreamUtils {
    public static byte[] readAllBytes(InputStream is) throws IOException {
        return StreamUtils.readNBytes(is, Integer.MAX_VALUE);
    }

    private static byte[] readNBytes(InputStream is, int len) throws IOException {
        int var6;
        if (len < 0) {
            throw new IllegalArgumentException("len < 0");
        }
        ArrayList<byte[]> var2 = null;
        byte[] var3 = null;
        int var4 = 0;
        int var5 = len;
        do {
            byte[] var7 = new byte[Math.min(var5, 8192)];
            int var8 = 0;
            while ((var6 = is.read(var7, var8, Math.min(var7.length - var8, var5))) > 0) {
                var8 += var6;
                var5 -= var6;
            }
            if (var8 <= 0) continue;
            if (0x7FFFFFF7 - var4 < var8) {
                throw new OutOfMemoryError("Required array size too large");
            }
            var4 += var8;
            if (var3 == null) {
                var3 = var7;
                continue;
            }
            if (var2 == null) {
                var2 = new ArrayList<byte[]>();
                var2.add(var3);
            }
            var2.add(var7);
        } while (var6 >= 0 && var5 > 0);
        if (var2 == null) {
            if (var3 == null) {
                return new byte[0];
            }
            return var3.length == var4 ? var3 : Arrays.copyOf(var3, var4);
        }
        var3 = new byte[var4];
        int var11 = 0;
        var5 = var4;
        for (byte[] var9 : var2) {
            int var10 = Math.min(var9.length, var5);
            System.arraycopy(var9, 0, var3, var11, var10);
            var11 += var10;
            var5 -= var10;
        }
        return var3;
    }
}

