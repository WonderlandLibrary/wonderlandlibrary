// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.utils;

import java.util.zip.InflaterOutputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.io.ByteArrayOutputStream;

public class Compression
{
    public static final byte[] MAGIC_HEADER;
    
    static {
        MAGIC_HEADER = "UDPPacketer".getBytes();
    }
    
    public static byte[] compress(final byte[] in) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final DeflaterOutputStream defl = new DeflaterOutputStream(out);
            defl.write(in);
            defl.flush();
            defl.close();
            final byte[] compressed = out.toByteArray();
            return ByteUtils.concatenate(Compression.MAGIC_HEADER, compressed);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(150);
            return null;
        }
    }
    
    public static byte[] decompress(final byte[] in) {
        try {
            final byte[] in2 = ByteUtils.remove(in, Compression.MAGIC_HEADER.length);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final InflaterOutputStream infl = new InflaterOutputStream(out);
            infl.write(in2);
            infl.flush();
            infl.close();
            return out.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(150);
            return null;
        }
    }
    
    public static boolean isCompressed(final byte[] in) {
        for (int i = 0; i < Compression.MAGIC_HEADER.length; ++i) {
            if (in[i] != Compression.MAGIC_HEADER[i]) {
                return false;
            }
        }
        return true;
    }
}
