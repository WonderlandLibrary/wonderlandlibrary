/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;

public class RandomAccessFileManager
extends OutputStreamManager {
    static final int DEFAULT_BUFFER_SIZE = 262144;
    private static final RandomAccessFileManagerFactory FACTORY = new RandomAccessFileManagerFactory();
    private final boolean isImmediateFlush;
    private final String advertiseURI;
    private final RandomAccessFile randomAccessFile;
    private final ByteBuffer buffer;
    private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal();

    protected RandomAccessFileManager(RandomAccessFile file, String fileName, OutputStream os, boolean immediateFlush, String advertiseURI, Layout<? extends Serializable> layout) {
        super(os, fileName, layout);
        this.isImmediateFlush = immediateFlush;
        this.randomAccessFile = file;
        this.advertiseURI = advertiseURI;
        this.isEndOfBatch.set(Boolean.FALSE);
        this.buffer = ByteBuffer.allocate(262144);
    }

    public static RandomAccessFileManager getFileManager(String fileName, boolean append, boolean isFlush, String advertiseURI, Layout<? extends Serializable> layout) {
        return (RandomAccessFileManager)RandomAccessFileManager.getManager(fileName, new FactoryData(append, isFlush, advertiseURI, layout), FACTORY);
    }

    public Boolean isEndOfBatch() {
        return this.isEndOfBatch.get();
    }

    public void setEndOfBatch(boolean isEndOfBatch) {
        this.isEndOfBatch.set(isEndOfBatch);
    }

    @Override
    protected synchronized void write(byte[] bytes, int offset, int length) {
        super.write(bytes, offset, length);
        int chunk = 0;
        do {
            if (length > this.buffer.remaining()) {
                this.flush();
            }
            chunk = Math.min(length, this.buffer.remaining());
            this.buffer.put(bytes, offset, chunk);
            offset += chunk;
        } while ((length -= chunk) > 0);
        if (this.isImmediateFlush || this.isEndOfBatch.get() == Boolean.TRUE) {
            this.flush();
        }
    }

    @Override
    public synchronized void flush() {
        this.buffer.flip();
        try {
            this.randomAccessFile.write(this.buffer.array(), 0, this.buffer.limit());
        }
        catch (IOException ex) {
            String msg = "Error writing to RandomAccessFile " + this.getName();
            throw new AppenderLoggingException(msg, ex);
        }
        this.buffer.clear();
    }

    @Override
    public synchronized void close() {
        this.flush();
        try {
            this.randomAccessFile.close();
        }
        catch (IOException ex) {
            LOGGER.error("Unable to close RandomAccessFile " + this.getName() + ". " + ex);
        }
    }

    public String getFileName() {
        return this.getName();
    }

    @Override
    public Map<String, String> getContentFormat() {
        HashMap<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("fileURI", this.advertiseURI);
        return result;
    }

    private static class RandomAccessFileManagerFactory
    implements ManagerFactory<RandomAccessFileManager, FactoryData> {
        private RandomAccessFileManagerFactory() {
        }

        @Override
        public RandomAccessFileManager createManager(String name, FactoryData data) {
            File file = new File(name);
            File parent = file.getParentFile();
            if (null != parent && !parent.exists()) {
                parent.mkdirs();
            }
            if (!data.append) {
                file.delete();
            }
            DummyOutputStream os = new DummyOutputStream();
            try {
                RandomAccessFile raf = new RandomAccessFile(name, "rw");
                if (data.append) {
                    raf.seek(raf.length());
                } else {
                    raf.setLength(0L);
                }
                return new RandomAccessFileManager(raf, name, os, data.immediateFlush, data.advertiseURI, data.layout);
            }
            catch (Exception ex) {
                AbstractManager.LOGGER.error("RandomAccessFileManager (" + name + ") " + ex);
                return null;
            }
        }
    }

    private static class FactoryData {
        private final boolean append;
        private final boolean immediateFlush;
        private final String advertiseURI;
        private final Layout<? extends Serializable> layout;

        public FactoryData(boolean append, boolean immediateFlush, String advertiseURI, Layout<? extends Serializable> layout) {
            this.append = append;
            this.immediateFlush = immediateFlush;
            this.advertiseURI = advertiseURI;
            this.layout = layout;
        }
    }

    static class DummyOutputStream
    extends OutputStream {
        DummyOutputStream() {
        }

        @Override
        public void write(int b) throws IOException {
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
        }
    }
}

