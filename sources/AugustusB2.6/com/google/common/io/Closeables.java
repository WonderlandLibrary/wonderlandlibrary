// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Level;
import javax.annotation.CheckForNull;
import java.io.Closeable;
import com.google.common.annotations.VisibleForTesting;
import java.util.logging.Logger;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class Closeables
{
    @VisibleForTesting
    static final Logger logger;
    
    private Closeables() {
    }
    
    public static void close(@CheckForNull final Closeable closeable, final boolean swallowIOException) throws IOException {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException e) {
            if (!swallowIOException) {
                throw e;
            }
            Closeables.logger.log(Level.WARNING, "IOException thrown while closing Closeable.", e);
        }
    }
    
    public static void closeQuietly(@CheckForNull final InputStream inputStream) {
        try {
            close(inputStream, true);
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)impossible);
        }
    }
    
    public static void closeQuietly(@CheckForNull final Reader reader) {
        try {
            close(reader, true);
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)impossible);
        }
    }
    
    static {
        logger = Logger.getLogger(Closeables.class.getName());
    }
}
