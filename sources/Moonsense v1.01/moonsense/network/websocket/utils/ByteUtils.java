// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.utils;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class ByteUtils
{
    public static byte[] concatenate(final byte[] a, final byte[] b) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(a);
        outputStream.write(b);
        return outputStream.toByteArray();
    }
    
    public static byte[] remove(final byte[] a, final int num) {
        final byte[] b = new byte[a.length - num];
        for (int i = num; i < a.length; ++i) {
            b[i - num] = a[i];
        }
        return b;
    }
}
