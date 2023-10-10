// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;

public final class Bitstream implements BitstreamErrors
{
    static byte INITIAL_SYNC;
    static byte STRICT_SYNC;
    private static final int BUFFER_INT_SIZE = 433;
    private final int[] framebuffer;
    private int framesize;
    private byte[] frame_bytes;
    private int wordpointer;
    private int bitindex;
    private int syncword;
    private int header_pos;
    private boolean single_ch_mode;
    private final int[] bitmask;
    private final PushbackInputStream source;
    private final Header header;
    private final byte[] syncbuf;
    private Crc16[] crc;
    private byte[] rawid3v2;
    private boolean firstframe;
    
    public Bitstream(InputStream in) {
        this.framebuffer = new int[433];
        this.frame_bytes = new byte[1732];
        this.header_pos = 0;
        this.bitmask = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071 };
        this.header = new Header();
        this.syncbuf = new byte[4];
        this.crc = new Crc16[1];
        this.rawid3v2 = null;
        this.firstframe = true;
        if (in == null) {
            throw new NullPointerException("in");
        }
        in = new BufferedInputStream(in);
        this.loadID3v2(in);
        this.firstframe = true;
        this.source = new PushbackInputStream(in, 1732);
        this.closeFrame();
    }
    
    public int header_pos() {
        return this.header_pos;
    }
    
    private void loadID3v2(final InputStream in) {
        int size = -1;
        try {
            in.mark(10);
            size = this.readID3v2Header(in);
            this.header_pos = size;
        }
        catch (IOException ex) {}
        finally {
            try {
                in.reset();
            }
            catch (IOException ex2) {}
        }
        try {
            if (size > 0) {
                in.read(this.rawid3v2 = new byte[size], 0, this.rawid3v2.length);
            }
        }
        catch (IOException ex3) {}
    }
    
    private int readID3v2Header(final InputStream in) throws IOException {
        final byte[] id3header = new byte[4];
        int size = -10;
        in.read(id3header, 0, 3);
        if (id3header[0] == 73 && id3header[1] == 68 && id3header[2] == 51) {
            in.read(id3header, 0, 3);
            final int majorVersion = id3header[0];
            final int revision = id3header[1];
            in.read(id3header, 0, 4);
            size = (id3header[0] << 21) + (id3header[1] << 14) + (id3header[2] << 7) + id3header[3];
        }
        return size + 10;
    }
    
    public InputStream getRawID3v2() {
        if (this.rawid3v2 == null) {
            return null;
        }
        final ByteArrayInputStream bain = new ByteArrayInputStream(this.rawid3v2);
        return bain;
    }
    
    public void close() throws BitstreamException {
        try {
            this.source.close();
        }
        catch (IOException ex) {
            throw this.newBitstreamException(258, ex);
        }
    }
    
    public Header readFrame() throws BitstreamException {
        Header result = null;
        try {
            result = this.readNextFrame();
            if (this.firstframe) {
                result.parseVBR(this.frame_bytes);
                this.firstframe = false;
            }
        }
        catch (BitstreamException ex) {
            if (ex.getErrorCode() == 261) {
                try {
                    this.closeFrame();
                    result = this.readNextFrame();
                }
                catch (BitstreamException e) {
                    if (e.getErrorCode() != 260) {
                        throw this.newBitstreamException(e.getErrorCode(), e);
                    }
                }
            }
            else if (ex.getErrorCode() != 260) {
                throw this.newBitstreamException(ex.getErrorCode(), ex);
            }
        }
        return result;
    }
    
    private Header readNextFrame() throws BitstreamException {
        if (this.framesize == -1) {
            this.nextFrame();
        }
        return this.header;
    }
    
    private void nextFrame() throws BitstreamException {
        this.header.read_header(this, this.crc);
    }
    
    public void unreadFrame() throws BitstreamException {
        if (this.wordpointer == -1 && this.bitindex == -1 && this.framesize > 0) {
            try {
                this.source.unread(this.frame_bytes, 0, this.framesize);
            }
            catch (IOException ex) {
                throw this.newBitstreamException(258);
            }
        }
    }
    
    public void closeFrame() {
        this.framesize = -1;
        this.wordpointer = -1;
        this.bitindex = -1;
    }
    
    public boolean isSyncCurrentPosition(final int syncmode) throws BitstreamException {
        final int read = this.readBytes(this.syncbuf, 0, 4);
        final int headerstring = (this.syncbuf[0] << 24 & 0xFF000000) | (this.syncbuf[1] << 16 & 0xFF0000) | (this.syncbuf[2] << 8 & 0xFF00) | (this.syncbuf[3] << 0 & 0xFF);
        try {
            this.source.unread(this.syncbuf, 0, read);
        }
        catch (IOException ex) {}
        boolean sync = false;
        switch (read) {
            case 0: {
                sync = true;
                break;
            }
            case 4: {
                sync = this.isSyncMark(headerstring, syncmode, this.syncword);
                break;
            }
        }
        return sync;
    }
    
    public int readBits(final int n) {
        return this.get_bits(n);
    }
    
    public int readCheckedBits(final int n) {
        return this.get_bits(n);
    }
    
    protected BitstreamException newBitstreamException(final int errorcode) {
        return new BitstreamException(errorcode, null);
    }
    
    protected BitstreamException newBitstreamException(final int errorcode, final Throwable throwable) {
        return new BitstreamException(errorcode, throwable);
    }
    
    int syncHeader(final byte syncmode) throws BitstreamException {
        final int bytesRead = this.readBytes(this.syncbuf, 0, 3);
        if (bytesRead != 3) {
            throw this.newBitstreamException(260, null);
        }
        int headerstring = (this.syncbuf[0] << 16 & 0xFF0000) | (this.syncbuf[1] << 8 & 0xFF00) | (this.syncbuf[2] << 0 & 0xFF);
        boolean sync;
        do {
            headerstring <<= 8;
            if (this.readBytes(this.syncbuf, 3, 1) != 1) {
                throw this.newBitstreamException(260, null);
            }
            headerstring |= (this.syncbuf[3] & 0xFF);
            sync = this.isSyncMark(headerstring, syncmode, this.syncword);
        } while (!sync);
        return headerstring;
    }
    
    public boolean isSyncMark(final int headerstring, final int syncmode, final int word) {
        boolean sync = false;
        if (syncmode == Bitstream.INITIAL_SYNC) {
            sync = ((headerstring & 0xFFE00000) == 0xFFE00000);
        }
        else {
            sync = ((headerstring & 0xFFF80C00) == word && (headerstring & 0xC0) == 0xC0 == this.single_ch_mode);
        }
        if (sync) {
            sync = ((headerstring >>> 10 & 0x3) != 0x3);
        }
        if (sync) {
            sync = ((headerstring >>> 17 & 0x3) != 0x0);
        }
        if (sync) {
            sync = ((headerstring >>> 19 & 0x3) != 0x1);
        }
        return sync;
    }
    
    int read_frame_data(final int bytesize) throws BitstreamException {
        int numread = 0;
        numread = this.readFully(this.frame_bytes, 0, bytesize);
        this.framesize = bytesize;
        this.wordpointer = -1;
        this.bitindex = -1;
        return numread;
    }
    
    void parse_frame() throws BitstreamException {
        int b = 0;
        final byte[] byteread = this.frame_bytes;
        for (int bytesize = this.framesize, k = 0; k < bytesize; k += 4) {
            final int convert = 0;
            byte b2 = 0;
            byte b3 = 0;
            byte b4 = 0;
            byte b5 = 0;
            b2 = byteread[k];
            if (k + 1 < bytesize) {
                b3 = byteread[k + 1];
            }
            if (k + 2 < bytesize) {
                b4 = byteread[k + 2];
            }
            if (k + 3 < bytesize) {
                b5 = byteread[k + 3];
            }
            this.framebuffer[b++] = ((b2 << 24 & 0xFF000000) | (b3 << 16 & 0xFF0000) | (b4 << 8 & 0xFF00) | (b5 & 0xFF));
        }
        this.wordpointer = 0;
        this.bitindex = 0;
    }
    
    public int get_bits(final int number_of_bits) {
        int returnvalue = 0;
        final int sum = this.bitindex + number_of_bits;
        if (this.wordpointer < 0) {
            this.wordpointer = 0;
        }
        if (sum <= 32) {
            returnvalue = (this.framebuffer[this.wordpointer] >>> 32 - sum & this.bitmask[number_of_bits]);
            if ((this.bitindex += number_of_bits) == 32) {
                this.bitindex = 0;
                ++this.wordpointer;
            }
            return returnvalue;
        }
        final int Right = this.framebuffer[this.wordpointer] & 0xFFFF;
        ++this.wordpointer;
        final int Left = this.framebuffer[this.wordpointer] & 0xFFFF0000;
        returnvalue = ((Right << 16 & 0xFFFF0000) | (Left >>> 16 & 0xFFFF));
        returnvalue >>>= 48 - sum;
        returnvalue &= this.bitmask[number_of_bits];
        this.bitindex = sum - 32;
        return returnvalue;
    }
    
    void set_syncword(final int syncword0) {
        this.syncword = (syncword0 & 0xFFFFFF3F);
        this.single_ch_mode = ((syncword0 & 0xC0) == 0xC0);
    }
    
    private int readFully(final byte[] b, int offs, int len) throws BitstreamException {
        int nRead = 0;
        try {
            while (len > 0) {
                final int bytesread = this.source.read(b, offs, len);
                if (bytesread == -1) {
                    while (len-- > 0) {
                        b[offs++] = 0;
                    }
                    break;
                }
                nRead += bytesread;
                offs += bytesread;
                len -= bytesread;
            }
        }
        catch (IOException ex) {
            throw this.newBitstreamException(258, ex);
        }
        return nRead;
    }
    
    private int readBytes(final byte[] b, int offs, int len) throws BitstreamException {
        int totalBytesRead = 0;
        try {
            while (len > 0) {
                final int bytesread = this.source.read(b, offs, len);
                if (bytesread == -1) {
                    break;
                }
                totalBytesRead += bytesread;
                offs += bytesread;
                len -= bytesread;
            }
        }
        catch (IOException ex) {
            throw this.newBitstreamException(258, ex);
        }
        return totalBytesRead;
    }
    
    static {
        Bitstream.INITIAL_SYNC = 0;
        Bitstream.STRICT_SYNC = 1;
    }
}
