// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.Path;

class FilePasswordProvider implements PasswordProvider
{
    private final Path passwordPath;
    
    public FilePasswordProvider(final String passwordFile) throws NoSuchFileException {
        this.passwordPath = Paths.get(passwordFile, new String[0]);
        if (!Files.exists(this.passwordPath, new LinkOption[0])) {
            throw new NoSuchFileException("PasswordFile '" + passwordFile + "' does not exist");
        }
    }
    
    @Override
    public char[] getPassword() {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(this.passwordPath);
            final ByteBuffer bb = ByteBuffer.wrap(bytes);
            final CharBuffer decoded = Charset.defaultCharset().decode(bb);
            final char[] result = new char[decoded.limit()];
            decoded.get(result, 0, result.length);
            decoded.rewind();
            decoded.put(new char[result.length]);
            return result;
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not read password from " + this.passwordPath + ": " + e, e);
        }
        finally {
            if (bytes != null) {
                Arrays.fill(bytes, (byte)0);
            }
        }
    }
}
