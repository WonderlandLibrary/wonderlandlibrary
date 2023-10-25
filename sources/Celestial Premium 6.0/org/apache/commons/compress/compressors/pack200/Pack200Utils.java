/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.compressors.pack200;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.ZipFile;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Pack200Utils {
    private Pack200Utils() {
    }

    public static void normalize(File jar) throws IOException {
        Pack200Utils.normalize(jar, jar, null);
    }

    public static void normalize(File jar, Map<String, String> props) throws IOException {
        Pack200Utils.normalize(jar, jar, props);
    }

    public static void normalize(File from, File to) throws IOException {
        Pack200Utils.normalize(from, to, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void normalize(File from, File to, Map<String, String> props) throws IOException {
        if (props == null) {
            props = new HashMap<String, String>();
        }
        props.put("pack.segment.limit", "-1");
        File f = File.createTempFile("commons-compress", "pack200normalize");
        f.deleteOnExit();
        try {
            OutputStream os = new FileOutputStream(f);
            ZipFile j = null;
            try {
                Pack200.Packer p = Pack200.newPacker();
                p.properties().putAll(props);
                j = new JarFile(from);
                p.pack((JarFile)j, os);
                j = null;
                os.close();
                os = null;
                Pack200.Unpacker u = Pack200.newUnpacker();
                os = new JarOutputStream(new FileOutputStream(to));
                u.unpack(f, (JarOutputStream)os);
            }
            finally {
                if (j != null) {
                    j.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        }
        finally {
            f.delete();
        }
    }
}

