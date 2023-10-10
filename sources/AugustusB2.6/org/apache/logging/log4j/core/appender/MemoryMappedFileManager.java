// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.util.NullOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.security.PrivilegedActionException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.io.IOException;
import java.util.Objects;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.util.Constants;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.io.RandomAccessFile;

public class MemoryMappedFileManager extends OutputStreamManager
{
    static final int DEFAULT_REGION_LENGTH = 33554432;
    private static final int MAX_REMAP_COUNT = 10;
    private static final MemoryMappedFileManagerFactory FACTORY;
    private static final double NANOS_PER_MILLISEC = 1000000.0;
    private final boolean immediateFlush;
    private final int regionLength;
    private final String advertiseURI;
    private final RandomAccessFile randomAccessFile;
    private MappedByteBuffer mappedBuffer;
    private long mappingOffset;
    
    protected MemoryMappedFileManager(final RandomAccessFile file, final String fileName, final OutputStream os, final boolean immediateFlush, final long position, final int regionLength, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader) throws IOException {
        super(os, fileName, layout, writeHeader, ByteBuffer.wrap(Constants.EMPTY_BYTE_ARRAY));
        this.immediateFlush = immediateFlush;
        this.randomAccessFile = Objects.requireNonNull(file, "RandomAccessFile");
        this.regionLength = regionLength;
        this.advertiseURI = advertiseURI;
        this.mappedBuffer = mmap(this.randomAccessFile.getChannel(), this.getFileName(), position, regionLength);
        this.byteBuffer = this.mappedBuffer;
        this.mappingOffset = position;
    }
    
    public static MemoryMappedFileManager getFileManager(final String fileName, final boolean append, final boolean immediateFlush, final int regionLength, final String advertiseURI, final Layout<? extends Serializable> layout) {
        return AbstractManager.narrow(MemoryMappedFileManager.class, OutputStreamManager.getManager(fileName, new FactoryData(append, immediateFlush, regionLength, advertiseURI, layout), MemoryMappedFileManager.FACTORY));
    }
    
    @Deprecated
    public Boolean isEndOfBatch() {
        return Boolean.FALSE;
    }
    
    @Deprecated
    public void setEndOfBatch(final boolean endOfBatch) {
    }
    
    @Override
    protected synchronized void write(final byte[] bytes, int offset, int length, final boolean immediateFlush) {
        while (length > this.mappedBuffer.remaining()) {
            final int chunk = this.mappedBuffer.remaining();
            this.mappedBuffer.put(bytes, offset, chunk);
            offset += chunk;
            length -= chunk;
            this.remap();
        }
        this.mappedBuffer.put(bytes, offset, length);
    }
    
    private synchronized void remap() {
        final long offset = this.mappingOffset + this.mappedBuffer.position();
        final int length = this.mappedBuffer.remaining() + this.regionLength;
        try {
            unsafeUnmap(this.mappedBuffer);
            final long fileLength = this.randomAccessFile.length() + this.regionLength;
            MemoryMappedFileManager.LOGGER.debug("{} {} extending {} by {} bytes to {}", this.getClass().getSimpleName(), this.getName(), this.getFileName(), this.regionLength, fileLength);
            final long startNanos = System.nanoTime();
            this.randomAccessFile.setLength(fileLength);
            final float millis = (float)((System.nanoTime() - startNanos) / 1000000.0);
            MemoryMappedFileManager.LOGGER.debug("{} {} extended {} OK in {} millis", this.getClass().getSimpleName(), this.getName(), this.getFileName(), millis);
            this.mappedBuffer = mmap(this.randomAccessFile.getChannel(), this.getFileName(), offset, length);
            this.byteBuffer = this.mappedBuffer;
            this.mappingOffset = offset;
        }
        catch (Exception ex) {
            this.logError("Unable to remap", ex);
        }
    }
    
    @Override
    public synchronized void flush() {
        this.mappedBuffer.force();
    }
    
    public synchronized boolean closeOutputStream() {
        final long position = this.mappedBuffer.position();
        final long length = this.mappingOffset + position;
        try {
            unsafeUnmap(this.mappedBuffer);
        }
        catch (Exception ex) {
            this.logError("Unable to unmap MappedBuffer", ex);
        }
        try {
            MemoryMappedFileManager.LOGGER.debug("MMapAppender closing. Setting {} length to {} (offset {} + position {})", this.getFileName(), length, this.mappingOffset, position);
            this.randomAccessFile.setLength(length);
            this.randomAccessFile.close();
            return true;
        }
        catch (IOException ex2) {
            this.logError("Unable to close MemoryMappedFile", ex2);
            return false;
        }
    }
    
    public static MappedByteBuffer mmap(final FileChannel fileChannel, final String fileName, final long start, final int size) throws IOException {
        int i = 1;
        while (true) {
            try {
                MemoryMappedFileManager.LOGGER.debug("MMapAppender remapping {} start={}, size={}", fileName, start, size);
                final long startNanos = System.nanoTime();
                final MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, start, size);
                map.order(ByteOrder.nativeOrder());
                final float millis = (float)((System.nanoTime() - startNanos) / 1000000.0);
                MemoryMappedFileManager.LOGGER.debug("MMapAppender remapped {} OK in {} millis", fileName, millis);
                return map;
            }
            catch (IOException e) {
                if (e.getMessage() == null || !e.getMessage().endsWith("user-mapped section open")) {
                    throw e;
                }
                MemoryMappedFileManager.LOGGER.debug("Remap attempt {}/{} failed. Retrying...", (Object)i, 10, e);
                if (i < 10) {
                    Thread.yield();
                }
                else {
                    try {
                        Thread.sleep(1L);
                    }
                    catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        throw e;
                    }
                }
                ++i;
                continue;
            }
            break;
        }
    }
    
    private static void unsafeUnmap(final MappedByteBuffer mbb) throws PrivilegedActionException {
        MemoryMappedFileManager.LOGGER.debug("MMapAppender unmapping old buffer...");
        final long startNanos = System.nanoTime();
        final Method getCleanerMethod;
        final Object cleaner;
        final Method cleanMethod;
        AccessController.doPrivileged(() -> {
            getCleanerMethod = mbb.getClass().getMethod("cleaner", (Class<?>[])new Class[0]);
            getCleanerMethod.setAccessible(true);
            cleaner = getCleanerMethod.invoke(mbb, new Object[0]);
            cleanMethod = cleaner.getClass().getMethod("clean", (Class<?>[])new Class[0]);
            cleanMethod.invoke(cleaner, new Object[0]);
            return null;
        });
        final float millis = (float)((System.nanoTime() - startNanos) / 1000000.0);
        MemoryMappedFileManager.LOGGER.debug("MMapAppender unmapped buffer OK in {} millis", (Object)millis);
    }
    
    public String getFileName() {
        return this.getName();
    }
    
    public int getRegionLength() {
        return this.regionLength;
    }
    
    public boolean isImmediateFlush() {
        return this.immediateFlush;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("fileURI", this.advertiseURI);
        return result;
    }
    
    @Override
    protected void flushBuffer(final ByteBuffer buffer) {
    }
    
    @Override
    public ByteBuffer getByteBuffer() {
        return this.mappedBuffer;
    }
    
    @Override
    public ByteBuffer drain(final ByteBuffer buf) {
        this.remap();
        return this.mappedBuffer;
    }
    
    static {
        FACTORY = new MemoryMappedFileManagerFactory();
    }
    
    private static class FactoryData
    {
        private final boolean append;
        private final boolean immediateFlush;
        private final int regionLength;
        private final String advertiseURI;
        private final Layout<? extends Serializable> layout;
        
        public FactoryData(final boolean append, final boolean immediateFlush, final int regionLength, final String advertiseURI, final Layout<? extends Serializable> layout) {
            this.append = append;
            this.immediateFlush = immediateFlush;
            this.regionLength = regionLength;
            this.advertiseURI = advertiseURI;
            this.layout = layout;
        }
    }
    
    private static class MemoryMappedFileManagerFactory implements ManagerFactory<MemoryMappedFileManager, FactoryData>
    {
        @Override
        public MemoryMappedFileManager createManager(final String name, final FactoryData data) {
            final File file = new File(name);
            if (!data.append) {
                file.delete();
            }
            final boolean writeHeader = !data.append || !file.exists();
            final OutputStream os = NullOutputStream.getInstance();
            RandomAccessFile raf = null;
            try {
                FileUtils.makeParentDirs(file);
                raf = new RandomAccessFile(name, "rw");
                final long position = data.append ? raf.length() : 0L;
                raf.setLength(position + data.regionLength);
                return new MemoryMappedFileManager(raf, name, os, data.immediateFlush, position, data.regionLength, data.advertiseURI, data.layout, writeHeader);
            }
            catch (Exception ex) {
                AbstractManager.LOGGER.error("MemoryMappedFileManager (" + name + ") " + ex, ex);
                Closer.closeSilently(raf);
                return null;
            }
        }
    }
}
