// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core;

import java.io.FilterInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import com.googlecode.pngtastic.core.processing.PngByteArrayOutputStream;
import java.util.Iterator;
import com.googlecode.pngtastic.core.processing.PngtasticCompressionHandler;
import com.googlecode.pngtastic.core.processing.PngtasticInterlaceHandler;
import com.googlecode.pngtastic.core.processing.PngtasticFilterHandler;
import com.googlecode.pngtastic.core.processing.PngCompressionHandler;
import com.googlecode.pngtastic.core.processing.PngInterlaceHandler;
import com.googlecode.pngtastic.core.processing.PngFilterHandler;

public abstract class PngProcessor
{
    protected final Logger log;
    protected final PngFilterHandler pngFilterHandler;
    protected final PngInterlaceHandler pngInterlaceHandler;
    protected PngCompressionHandler pngCompressionHandler;
    
    protected PngProcessor(final String logLevel) {
        this.log = new Logger(logLevel);
        this.pngFilterHandler = new PngtasticFilterHandler(this.log);
        this.pngInterlaceHandler = new PngtasticInterlaceHandler(this.log, this.pngFilterHandler);
        this.pngCompressionHandler = new PngtasticCompressionHandler(this.log);
    }
    
    protected final PngByteArrayOutputStream getInflatedImageData(PngChunk chunk, final Iterator<PngChunk> itChunks) throws IOException {
        final PngByteArrayOutputStream imageBytes = new PngByteArrayOutputStream((chunk == null) ? 0 : chunk.getLength());
        final DataOutputStream imageData = new DataOutputStream(imageBytes);
        Throwable t = null;
        try {
            while (chunk != null && "IDAT".equals(chunk.getTypeString())) {
                imageData.write(chunk.getData());
                chunk = (itChunks.hasNext() ? itChunks.next() : null);
            }
            final PngByteArrayOutputStream inflate = inflate(imageBytes);
            imageData.close();
            return inflate;
        }
        catch (Throwable t3) {
            final Throwable t2 = t3;
            t = t3;
            throw t2;
        }
        finally {
            if (t != null) {
                try {
                    imageData.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            else {
                imageData.close();
            }
        }
    }
    
    private static PngByteArrayOutputStream inflate(PngByteArrayOutputStream bytes) throws IOException {
        final PngByteArrayOutputStream inflatedOut = new PngByteArrayOutputStream();
        Throwable t = null;
        try {
            inflater2 = new InflaterInputStream(new ByteArrayInputStream(bytes.get(), 0, bytes.len()));
            Throwable t2 = null;
            try {
                final byte[] block = new byte[8192];
                int readLength;
                while ((readLength = ((FilterInputStream)inflater2).read(block)) != -1) {
                    inflatedOut.write(block, 0, readLength);
                }
                final PngByteArrayOutputStream pngByteArrayOutputStream = inflatedOut;
                ((InflaterInputStream)inflater2).close();
                inflatedOut.close();
                return pngByteArrayOutputStream;
            }
            catch (Throwable t4) {
                final Throwable t3 = t4;
                t2 = t4;
                throw t3;
            }
            finally {
                if (t2 != null) {
                    try {
                        ((InflaterInputStream)inflater2).close();
                    }
                    catch (Throwable inflater2) {
                        t2.addSuppressed((Throwable)inflater2);
                    }
                }
                else {
                    ((InflaterInputStream)inflater2).close();
                }
            }
        }
        catch (Throwable t6) {
            final Throwable t5 = t6;
            t = t6;
            throw t5;
        }
        finally {
            if (t != null) {
                try {
                    inflatedOut.close();
                }
                catch (Throwable inflatedOut) {
                    t.addSuppressed((Throwable)inflatedOut);
                }
            }
            else {
                inflatedOut.close();
            }
        }
    }
    
    protected final List<byte[]> getScanlines(final PngByteArrayOutputStream inflatedImageData, final int sampleBitCount, final int rowLength, final long height) {
        final List<byte[]> rows = new ArrayList<byte[]>(Math.max((int)height, 0));
        byte[] previousRow = new byte[rowLength];
        for (int i = 0; i < height; ++i) {
            final int offset = i * rowLength;
            final byte[] row = new byte[rowLength];
            System.arraycopy(inflatedImageData.get(), offset, row, 0, rowLength);
            try {
                this.pngFilterHandler.deFilter(row, previousRow, sampleBitCount);
                rows.add(row);
                previousRow = row.clone();
            }
            catch (PngException e) {
                this.log.error("Error: %s", e.getMessage());
            }
        }
        return rows;
    }
    
    protected static PngChunk processHeadChunks(final PngImage result, final boolean removeGamma, final Iterator<PngChunk> itChunks) throws IOException {
        PngChunk chunk = null;
        while (itChunks.hasNext()) {
            chunk = itChunks.next();
            if ("IDAT".equals(chunk.getTypeString())) {
                break;
            }
            final PngChunk pngChunk;
            if ((!(pngChunk = chunk).isCritical() && !"TRNS".equals(pngChunk.getTypeString().toUpperCase()) && !"GAMA".equals(pngChunk.getTypeString().toUpperCase()) && !"CHRM".equals(pngChunk.getTypeString().toUpperCase())) || (removeGamma && "GAMA".equalsIgnoreCase(chunk.getTypeString()))) {
                continue;
            }
            final PngChunk newChunk = new PngChunk(chunk.getType(), chunk.getData().clone());
            if ("IHDR".equals(chunk.getTypeString())) {
                newChunk.setInterlace((byte)0);
            }
            result.addChunk(newChunk);
        }
        return chunk;
    }
}
