// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core;

import java.util.zip.CRC32;
import java.io.UnsupportedEncodingException;

public final class PngChunk
{
    private final byte[] type;
    private final byte[] data;
    
    public PngChunk(final byte[] type, final byte[] data) {
        this.type = type;
        this.data = data;
    }
    
    public final String getTypeString() {
        try {
            return new String(this.type, "UTF8");
        }
        catch (UnsupportedEncodingException ex) {
            return "";
        }
    }
    
    public final byte[] getType() {
        return this.type;
    }
    
    public final byte[] getData() {
        return this.data;
    }
    
    public final int getLength() {
        return this.data.length;
    }
    
    public final long getWidth() {
        return this.getUnsignedInt(0);
    }
    
    public final long getHeight() {
        return this.getUnsignedInt(4);
    }
    
    public final short getBitDepth() {
        return this.getUnsignedByte(8);
    }
    
    public final short getColorType() {
        return this.getUnsignedByte(9);
    }
    
    public final short getInterlace() {
        return this.getUnsignedByte(12);
    }
    
    public final void setInterlace(final byte interlace) {
        this.data[12] = 0;
    }
    
    private long getUnsignedInt(final int offset) {
        long value = 0L;
        for (int i = 0; i < 4; ++i) {
            value += (this.data[offset + i] & 0xFF) << (3 - i << 3);
        }
        return value;
    }
    
    private short getUnsignedByte(final int offset) {
        return (short)(this.data[offset] & 0xFF);
    }
    
    public final boolean isCritical() {
        final String type;
        return (type = this.getTypeString().toUpperCase()).equals("IHDR") || type.equals("PLTE") || type.equals("IDAT") || type.equals("IEND");
    }
    
    public final boolean verifyCRC(final long crc) {
        return this.getCRC() == crc;
    }
    
    public final long getCRC() {
        final CRC32 crc32;
        (crc32 = new CRC32()).update(this.type);
        crc32.update(this.data);
        return crc32.getValue();
    }
    
    @Override
    public final String toString() {
        final StringBuilder result;
        (result = new StringBuilder()).append('[').append(this.getTypeString()).append(']').append('\n');
        if ("IHDR".equals(this.getTypeString().toUpperCase())) {
            result.append("Size:        ").append(this.getWidth()).append('x').append(this.getHeight()).append('\n');
            result.append("Bit depth:   ").append(this.getUnsignedByte(8)).append('\n');
            result.append("Image type:  ").append(this.getUnsignedByte(9)).append(" (").append(PngImageType.forColorType(this.getUnsignedByte(9))).append(")\n");
            result.append("Color type:  ").append(this.getUnsignedByte(9)).append('\n');
            result.append("Compression: ").append(this.getUnsignedByte(10)).append('\n');
            result.append("Filter:      ").append(this.getUnsignedByte(11)).append('\n');
            result.append("Interlace:   ").append(this.getUnsignedByte(12));
        }
        if ("TEXT".equals(this.getTypeString().toUpperCase())) {
            result.append("Text:        ").append(new String(this.data));
        }
        if ("IDAT".equals(this.getTypeString().toUpperCase())) {
            result.append("Image Data:  length=").append(this.data.length).append(", data=");
            result.append(", crc=").append(this.getCRC());
        }
        return result.toString();
    }
}
