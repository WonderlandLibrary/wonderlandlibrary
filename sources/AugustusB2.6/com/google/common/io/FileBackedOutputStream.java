// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.google.common.annotations.VisibleForTesting;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import javax.annotation.CheckForNull;
import java.io.File;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.io.OutputStream;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class FileBackedOutputStream extends OutputStream
{
    private final int fileThreshold;
    private final boolean resetOnFinalize;
    private final ByteSource source;
    @CheckForNull
    private final File parentDirectory;
    @GuardedBy("this")
    private OutputStream out;
    @CheckForNull
    @GuardedBy("this")
    private MemoryOutput memory;
    @CheckForNull
    @GuardedBy("this")
    private File file;
    
    @CheckForNull
    @VisibleForTesting
    synchronized File getFile() {
        return this.file;
    }
    
    public FileBackedOutputStream(final int fileThreshold) {
        this(fileThreshold, false);
    }
    
    public FileBackedOutputStream(final int fileThreshold, final boolean resetOnFinalize) {
        this(fileThreshold, resetOnFinalize, null);
    }
    
    private FileBackedOutputStream(final int fileThreshold, final boolean resetOnFinalize, @CheckForNull final File parentDirectory) {
        this.fileThreshold = fileThreshold;
        this.resetOnFinalize = resetOnFinalize;
        this.parentDirectory = parentDirectory;
        this.memory = new MemoryOutput();
        this.out = this.memory;
        if (resetOnFinalize) {
            this.source = new ByteSource() {
                @Override
                public InputStream openStream() throws IOException {
                    return FileBackedOutputStream.this.openInputStream();
                }
                
                @Override
                protected void finalize() {
                    try {
                        FileBackedOutputStream.this.reset();
                    }
                    catch (Throwable t) {
                        t.printStackTrace(System.err);
                    }
                }
            };
        }
        else {
            this.source = new ByteSource() {
                @Override
                public InputStream openStream() throws IOException {
                    return FileBackedOutputStream.this.openInputStream();
                }
            };
        }
    }
    
    public ByteSource asByteSource() {
        return this.source;
    }
    
    private synchronized InputStream openInputStream() throws IOException {
        if (this.file != null) {
            return new FileInputStream(this.file);
        }
        Objects.requireNonNull(this.memory);
        return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
    }
    
    public synchronized void reset() throws IOException {
        try {
            this.close();
        }
        finally {
            if (this.memory == null) {
                this.memory = new MemoryOutput();
            }
            else {
                this.memory.reset();
            }
            this.out = this.memory;
            if (this.file != null) {
                final File deleteMe = this.file;
                this.file = null;
                if (!deleteMe.delete()) {
                    final String value = String.valueOf(deleteMe);
                    throw new IOException(new StringBuilder(18 + String.valueOf(value).length()).append("Could not delete: ").append(value).toString());
                }
            }
        }
    }
    
    @Override
    public synchronized void write(final int b) throws IOException {
        this.update(1);
        this.out.write(b);
    }
    
    @Override
    public synchronized void write(final byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    
    @Override
    public synchronized void write(final byte[] b, final int off, final int len) throws IOException {
        this.update(len);
        this.out.write(b, off, len);
    }
    
    @Override
    public synchronized void close() throws IOException {
        this.out.close();
    }
    
    @Override
    public synchronized void flush() throws IOException {
        this.out.flush();
    }
    
    @GuardedBy("this")
    private void update(final int len) throws IOException {
        if (this.memory != null && this.memory.getCount() + len > this.fileThreshold) {
            final File temp = File.createTempFile("FileBackedOutputStream", null, this.parentDirectory);
            if (this.resetOnFinalize) {
                temp.deleteOnExit();
            }
            try {
                final FileOutputStream transfer = new FileOutputStream(temp);
                transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
                transfer.flush();
                this.out = transfer;
            }
            catch (IOException e) {
                temp.delete();
                throw e;
            }
            this.file = temp;
            this.memory = null;
        }
    }
    
    private static class MemoryOutput extends ByteArrayOutputStream
    {
        byte[] getBuffer() {
            return this.buf;
        }
        
        int getCount() {
            return this.count;
        }
    }
}
