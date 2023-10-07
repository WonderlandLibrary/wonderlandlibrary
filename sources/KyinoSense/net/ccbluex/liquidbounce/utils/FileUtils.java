/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 */
package net.ccbluex.liquidbounce.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class FileUtils {
    public static void unpackFile(File file, String name) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.copy((InputStream)FileUtils.class.getClassLoader().getResourceAsStream(name), (OutputStream)fos);
        fos.close();
    }
}

