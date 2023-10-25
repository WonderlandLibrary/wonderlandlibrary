/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.AbstractHttpData;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public abstract class AbstractDiskHttpData
extends AbstractHttpData {
    protected File file;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractDiskHttpData.class);
    private boolean isRenamed;
    private FileChannel fileChannel;

    @Override
    public void setContent(File file) throws IOException {
        if (this.file != null) {
            this.delete();
        }
        this.file = file;
        this.size = file.length();
        this.isRenamed = true;
        this.completed = true;
    }

    @Override
    public byte[] get() throws IOException {
        if (this.file == null) {
            return EmptyArrays.EMPTY_BYTES;
        }
        return AbstractDiskHttpData.readFrom(this.file);
    }

    protected abstract String getDiskFilename();

    @Override
    public ByteBuf getByteBuf() throws IOException {
        if (this.file == null) {
            return Unpooled.EMPTY_BUFFER;
        }
        byte[] byArray = AbstractDiskHttpData.readFrom(this.file);
        return Unpooled.wrappedBuffer(byArray);
    }

    @Override
    public File getFile() throws IOException {
        return this.file;
    }

    @Override
    public void setContent(ByteBuf byteBuf) throws IOException {
        if (byteBuf == null) {
            throw new NullPointerException("buffer");
        }
        this.size = byteBuf.readableBytes();
        if (this.definedSize > 0L && this.definedSize < this.size) {
            throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
        }
        if (this.file == null) {
            this.file = this.tempFile();
        }
        if (byteBuf.readableBytes() == 0) {
            this.file.createNewFile();
            byteBuf.release();
            return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = byteBuf.nioBuffer();
        int n = 0;
        while ((long)n < this.size) {
            n += fileChannel.write(byteBuffer);
        }
        byteBuf.readerIndex(byteBuf.readerIndex() + n);
        fileChannel.force(false);
        fileChannel.close();
        fileOutputStream.close();
        this.completed = true;
        byteBuf.release();
    }

    @Override
    public String getString(Charset charset) throws IOException {
        if (this.file == null) {
            return "";
        }
        if (charset == null) {
            byte[] byArray = AbstractDiskHttpData.readFrom(this.file);
            return new String(byArray, HttpConstants.DEFAULT_CHARSET.name());
        }
        byte[] byArray = AbstractDiskHttpData.readFrom(this.file);
        return new String(byArray, charset.name());
    }

    @Override
    public void addContent(ByteBuf byteBuf, boolean bl) throws IOException {
        if (byteBuf != null) {
            int n = byteBuf.readableBytes();
            if (this.definedSize > 0L && this.definedSize < this.size + (long)n) {
                throw new IOException("Out of size: " + (this.size + (long)n) + " > " + this.definedSize);
            }
            ByteBuffer byteBuffer = byteBuf.nioBufferCount() == 1 ? byteBuf.nioBuffer() : byteBuf.copy().nioBuffer();
            int n2 = 0;
            if (this.file == null) {
                this.file = this.tempFile();
            }
            if (this.fileChannel == null) {
                FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                this.fileChannel = fileOutputStream.getChannel();
            }
            while (n2 < n) {
                n2 += this.fileChannel.write(byteBuffer);
            }
            this.size += (long)n;
            byteBuf.readerIndex(byteBuf.readerIndex() + n2);
            byteBuf.release();
        }
        if (bl) {
            if (this.file == null) {
                this.file = this.tempFile();
            }
            if (this.fileChannel == null) {
                FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                this.fileChannel = fileOutputStream.getChannel();
            }
            this.fileChannel.force(false);
            this.fileChannel.close();
            this.fileChannel = null;
            this.completed = true;
        } else if (byteBuf == null) {
            throw new NullPointerException("buffer");
        }
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    private File tempFile() throws IOException {
        String string = this.getDiskFilename();
        String string2 = string != null ? '_' + string : this.getPostfix();
        File file = this.getBaseDirectory() == null ? File.createTempFile(this.getPrefix(), string2) : File.createTempFile(this.getPrefix(), string2, new File(this.getBaseDirectory()));
        if (this.deleteOnExit()) {
            file.deleteOnExit();
        }
        return file;
    }

    private static byte[] readFrom(File file) throws IOException {
        long l = file.length();
        if (l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("File too big to be loaded in memory");
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        byte[] byArray = new byte[(int)l];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byArray);
        int n = 0;
        while ((long)n < l) {
            n += fileChannel.read(byteBuffer);
        }
        fileChannel.close();
        return byArray;
    }

    protected abstract String getBaseDirectory();

    @Override
    public ByteBuf getChunk(int n) throws IOException {
        int n2;
        int n3;
        if (this.file == null || n == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (this.fileChannel == null) {
            FileInputStream fileInputStream = new FileInputStream(this.file);
            this.fileChannel = fileInputStream.getChannel();
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(n);
        for (n2 = 0; n2 < n; n2 += n3) {
            n3 = this.fileChannel.read(byteBuffer);
            if (n3 != -1) continue;
            this.fileChannel.close();
            this.fileChannel = null;
            break;
        }
        if (n2 == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        byteBuffer.flip();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(byteBuffer);
        byteBuf.readerIndex(0);
        byteBuf.writerIndex(n2);
        return byteBuf;
    }

    protected abstract String getPrefix();

    @Override
    public void delete() {
        if (this.fileChannel != null) {
            try {
                this.fileChannel.force(false);
                this.fileChannel.close();
            }
            catch (IOException iOException) {
                logger.warn("Failed to close a file.", iOException);
            }
            this.fileChannel = null;
        }
        if (!this.isRenamed) {
            if (this.file != null && this.file.exists()) {
                this.file.delete();
            }
            this.file = null;
        }
    }

    protected abstract boolean deleteOnExit();

    @Override
    public void setContent(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream");
        }
        if (this.file != null) {
            this.delete();
        }
        this.file = this.tempFile();
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        FileChannel fileChannel = fileOutputStream.getChannel();
        byte[] byArray = new byte[16384];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byArray);
        int n = inputStream.read(byArray);
        int n2 = 0;
        while (n > 0) {
            byteBuffer.position(n).flip();
            n2 += fileChannel.write(byteBuffer);
            n = inputStream.read(byArray);
        }
        fileChannel.force(false);
        fileChannel.close();
        this.size = n2;
        if (this.definedSize > 0L && this.definedSize < this.size) {
            this.file.delete();
            this.file = null;
            throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
        }
        this.isRenamed = true;
        this.completed = true;
    }

    protected AbstractDiskHttpData(String string, Charset charset, long l) {
        super(string, charset, l);
    }

    @Override
    public String getString() throws IOException {
        return this.getString(HttpConstants.DEFAULT_CHARSET);
    }

    protected abstract String getPostfix();

    @Override
    public boolean renameTo(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("dest");
        }
        if (this.file == null) {
            throw new IOException("No file defined so cannot be renamed");
        }
        if (!this.file.renameTo(file)) {
            long l;
            FileInputStream fileInputStream = new FileInputStream(this.file);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();
            FileChannel fileChannel2 = fileOutputStream.getChannel();
            int n = 8196;
            for (l = 0L; l < this.size; l += fileChannel.transferTo(l, n, fileChannel2)) {
                if ((long)n >= this.size - l) continue;
                n = (int)(this.size - l);
            }
            fileChannel.close();
            fileChannel2.close();
            if (l == this.size) {
                this.file.delete();
                this.file = file;
                this.isRenamed = true;
                return true;
            }
            file.delete();
            return false;
        }
        this.file = file;
        this.isRenamed = true;
        return true;
    }
}

