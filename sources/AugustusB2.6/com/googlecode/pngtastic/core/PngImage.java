// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class PngImage
{
    private final Logger log;
    private String fileName;
    private List<PngChunk> chunks;
    private long width;
    private long height;
    private short bitDepth;
    private short colorType;
    private short interlace;
    private PngImageType imageType;
    
    public final String getFileName() {
        return this.fileName;
    }
    
    public final void setFileName(final String fileName) {
        this.fileName = fileName;
    }
    
    public final List<PngChunk> getChunks() {
        return this.chunks;
    }
    
    public final long getWidth() {
        return this.width;
    }
    
    public final long getHeight() {
        return this.height;
    }
    
    public final short getInterlace() {
        return this.interlace;
    }
    
    public final void setInterlace(final short interlace) {
        this.interlace = 0;
    }
    
    public PngImage() {
        this.chunks = new ArrayList<PngChunk>();
        this.log = new Logger("NONE");
    }
    
    public PngImage(final Logger log) {
        this.chunks = new ArrayList<PngChunk>();
        this.log = log;
    }
    
    public PngImage(final InputStream ins) {
        this(ins, null);
    }
    
    private PngImage(final InputStream ins, final String logLevel) {
        this(new Logger(null));
        try {
            final DataInputStream dis;
            if ((dis = new DataInputStream(ins)).readLong() != -8552249625308161526L) {
                throw new PngException("Bad png signature");
            }
            int length;
            PngChunk chunk;
            do {
                length = dis.readInt();
                final byte[] type = getChunkData(dis, 4);
                final byte[] data = getChunkData(dis, length);
                final long crc = (long)dis.readInt() & 0xFFFFFFFFL;
                if (!(chunk = new PngChunk(type, data)).verifyCRC(crc)) {
                    throw new PngException("Corrupted file, crc check failed");
                }
                this.addChunk(chunk);
            } while (length > 0 && !"IEND".equals(chunk.getTypeString()));
        }
        catch (IOException e) {
            throw new PngException("Error: " + e.getMessage(), e);
        }
    }
    
    static FileOutputStream writeFileOutputStream(final File out, final byte[] bytes) throws IOException {
        FileOutputStream outs = null;
        try {
            (outs = new FileOutputStream(out)).write(bytes);
            outs.close();
        }
        finally {
            if (outs != null) {
                outs.close();
            }
        }
        return outs;
    }
    
    public final DataOutputStream writeDataOutputStream(final OutputStream output) throws IOException {
        final DataOutputStream outs;
        (outs = new DataOutputStream(output)).writeLong(-8552249625308161526L);
        for (final PngChunk chunk : this.chunks) {
            this.log.debug("export: %s", chunk.toString());
            outs.writeInt(chunk.getLength());
            outs.write(chunk.getType());
            outs.write(chunk.getData());
            final int i = (int)chunk.getCRC();
            outs.writeInt(i);
        }
        outs.close();
        return outs;
    }
    
    public final void addChunk(final PngChunk chunk) {
        final String typeString = chunk.getTypeString();
        switch (typeString) {
            case "IHDR": {
                this.width = chunk.getWidth();
                this.height = chunk.getHeight();
                this.bitDepth = chunk.getBitDepth();
                this.colorType = chunk.getColorType();
                this.interlace = chunk.getInterlace();
                break;
            }
        }
        this.chunks.add(chunk);
    }
    
    public final byte[] getImageData() {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final Iterator<PngChunk> iterator = this.chunks.iterator();
            while (iterator.hasNext()) {
                final PngChunk chunk;
                if ((chunk = iterator.next()).getTypeString().equals("IDAT")) {
                    out.write(chunk.getData());
                }
            }
            return out.toByteArray();
        }
        catch (IOException e) {
            System.out.println("Couldn't get image data: " + e);
            return null;
        }
    }
    
    public final int getSampleBitCount() {
        this.imageType = ((this.imageType == null) ? PngImageType.forColorType(this.colorType) : this.imageType);
        return this.imageType.channelCount() * this.bitDepth;
    }
    
    private static byte[] getChunkData(final InputStream ins, final int length) throws PngException {
        final byte[] data = new byte[length];
        try {
            final int actual;
            if ((actual = ins.read(data)) < length) {
                throw new PngException(String.format("Expected %d bytes but got %d", length, actual));
            }
        }
        catch (IOException e) {
            throw new PngException("Error reading chunk data", e);
        }
        return data;
    }
}
