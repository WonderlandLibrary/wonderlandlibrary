/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontConstants;
import com.sun.javafx.font.PrismFontFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.AccessController;
import java.security.PrivilegedActionException;

class FontFileReader
implements FontConstants {
    String filename;
    long filesize;
    RandomAccessFile raFile;
    private static final int READBUFFERSIZE = 1024;
    private byte[] readBuffer;
    private int readBufferLen;
    private int readBufferStart;

    public FontFileReader(String string) {
        this.filename = string;
    }

    public String getFilename() {
        return this.filename;
    }

    public synchronized boolean openFile() throws PrivilegedActionException {
        if (this.raFile != null) {
            return false;
        }
        this.raFile = AccessController.doPrivileged(() -> {
            try {
                return new RandomAccessFile(this.filename, "r");
            }
            catch (FileNotFoundException fileNotFoundException) {
                return null;
            }
        });
        if (this.raFile != null) {
            try {
                this.filesize = this.raFile.length();
                return true;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return false;
    }

    public synchronized void closeFile() throws IOException {
        if (this.raFile != null) {
            this.raFile.close();
            this.raFile = null;
            this.readBuffer = null;
        }
    }

    public synchronized long getLength() {
        return this.filesize;
    }

    public synchronized void reset() throws IOException {
        if (this.raFile != null) {
            this.raFile.seek(0L);
        }
    }

    private synchronized int readFromFile(byte[] arrby, long l, int n) {
        try {
            this.raFile.seek(l);
            int n2 = this.raFile.read(arrby, 0, n);
            return n2;
        }
        catch (IOException iOException) {
            if (PrismFontFactory.debugFonts) {
                iOException.printStackTrace();
            }
            return 0;
        }
    }

    public synchronized Buffer readBlock(int n, int n2) {
        if (this.readBuffer == null) {
            this.readBuffer = new byte[1024];
            this.readBufferLen = 0;
        }
        if (n2 <= 1024) {
            if (this.readBufferStart <= n && this.readBufferStart + this.readBufferLen >= n + n2) {
                return new Buffer(this.readBuffer, n - this.readBufferStart);
            }
            this.readBufferStart = n;
            this.readBufferLen = (long)(n + 1024) > this.filesize ? (int)this.filesize - n : 1024;
            this.readFromFile(this.readBuffer, this.readBufferStart, this.readBufferLen);
            return new Buffer(this.readBuffer, 0);
        }
        byte[] arrby = new byte[n2];
        this.readFromFile(arrby, n, n2);
        return new Buffer(arrby, 0);
    }

    static class Buffer {
        byte[] data;
        int pos;
        int orig;

        Buffer(byte[] arrby, int n) {
            this.orig = this.pos = n;
            this.data = arrby;
        }

        int getInt(int n) {
            n += this.orig;
            int n2 = this.data[n++] & 0xFF;
            n2 <<= 8;
            n2 |= this.data[n++] & 0xFF;
            n2 <<= 8;
            n2 |= this.data[n++] & 0xFF;
            n2 <<= 8;
            return n2 |= this.data[n++] & 0xFF;
        }

        int getInt() {
            int n = this.data[this.pos++] & 0xFF;
            n <<= 8;
            n |= this.data[this.pos++] & 0xFF;
            n <<= 8;
            n |= this.data[this.pos++] & 0xFF;
            n <<= 8;
            return n |= this.data[this.pos++] & 0xFF;
        }

        short getShort(int n) {
            n += this.orig;
            int n2 = this.data[n++] & 0xFF;
            n2 <<= 8;
            return (short)(n2 |= this.data[n++] & 0xFF);
        }

        short getShort() {
            int n = this.data[this.pos++] & 0xFF;
            n <<= 8;
            return (short)(n |= this.data[this.pos++] & 0xFF);
        }

        char getChar(int n) {
            n += this.orig;
            int n2 = this.data[n++] & 0xFF;
            n2 <<= 8;
            return (char)(n2 |= this.data[n++] & 0xFF);
        }

        char getChar() {
            int n = this.data[this.pos++] & 0xFF;
            n <<= 8;
            return (char)(n |= this.data[this.pos++] & 0xFF);
        }

        void position(int n) {
            this.pos = this.orig + n;
        }

        int capacity() {
            return this.data.length - this.orig;
        }

        byte get() {
            return this.data[this.pos++];
        }

        byte get(int n) {
            return this.data[n += this.orig];
        }

        void skip(int n) {
            this.pos += n;
        }

        void get(int n, byte[] arrby, int n2, int n3) {
            System.arraycopy(this.data, this.orig + n, arrby, n2, n3);
        }
    }
}

