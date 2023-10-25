/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ZipEncoding {
    public boolean canEncode(String var1);

    public ByteBuffer encode(String var1) throws IOException;

    public String decode(byte[] var1) throws IOException;
}

