// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core.processing;

import java.util.Arrays;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.IOException;
import java.util.List;
import com.googlecode.pngtastic.core.Logger;

public final class PngtasticCompressionHandler implements PngCompressionHandler
{
    private final Logger log;
    private static final List<Integer> compressionStrategies;
    
    public PngtasticCompressionHandler(final Logger log) {
        this.log = log;
    }
    
    @Override
    public final byte[] deflate(final PngByteArrayOutputStream inflatedImageData, final Integer compressionLevel, final boolean concurrent) throws IOException {
        final List<byte[]> results = this.deflateImageDataConcurrently(inflatedImageData, compressionLevel);
        byte[] result = null;
        for (int i = 0; i < results.size(); ++i) {
            final byte[] data = results.get(i);
            if (result == null || data.length < result.length) {
                result = data;
            }
        }
        this.log.debug("Image bytes=%d", (result == null) ? -1 : result.length);
        return result;
    }
    
    private List<byte[]> deflateImageDataConcurrently(final PngByteArrayOutputStream inflatedImageData, final Integer compressionLevel) {
        final Collection<byte[]> results = new ConcurrentLinkedQueue<byte[]>();
        final Collection<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
        for (final int strategy : PngtasticCompressionHandler.compressionStrategies) {
            tasks.add(Executors.callable(new Runnable() {
                @Override
                public final void run() {
                    try {
                        results.add(PngtasticCompressionHandler.this.deflateImageData(inflatedImageData, strategy, compressionLevel));
                    }
                    catch (Throwable e) {
                        PngtasticCompressionHandler.this.log.error("Uncaught Exception: %s", e.getMessage());
                    }
                }
            }));
        }
        final ExecutorService compressionThreadPool = Executors.newFixedThreadPool(PngtasticCompressionHandler.compressionStrategies.size());
        try {
            compressionThreadPool.invokeAll((Collection<? extends Callable<Object>>)tasks);
        }
        catch (InterruptedException ex) {}
        finally {
            compressionThreadPool.shutdown();
        }
        return new ArrayList<byte[]>(results);
    }
    
    private byte[] deflateImageData(final PngByteArrayOutputStream inflatedImageData, final int strategy, Integer compression) throws IOException {
        byte[] result = null;
        int bestCompression = 9;
        if (compressionLevel == null || compressionLevel > 9 || compressionLevel < 0) {
            ByteArrayOutputStream deflatedOut;
            for (compression = 9; compression > 0; --compression) {
                deflatedOut = deflate(inflatedImageData, strategy, compression);
                if (result == null || result.length > deflatedOut.size()) {
                    result = deflatedOut.toByteArray();
                    bestCompression = compression;
                }
            }
        }
        else {
            result = deflate(inflatedImageData, strategy, compressionLevel).toByteArray();
            bestCompression = compressionLevel;
        }
        this.log.debug("Compression strategy: %s, compression level=%d, bytes=%d", strategy, bestCompression, (result == null) ? -1 : result.length);
        return result;
    }
    
    private static ByteArrayOutputStream deflate(final PngByteArrayOutputStream inflatedImageData, final int strategy, int compression) throws IOException {
        final ByteArrayOutputStream deflatedOut = new ByteArrayOutputStream();
        ((Deflater)(deflater = new Deflater(compression))).setStrategy(strategy);
        try {
            final DeflaterOutputStream stream;
            (stream = new DeflaterOutputStream(deflatedOut, (Deflater)deflater)).write(inflatedImageData.get(), 0, inflatedImageData.len());
            stream.close();
        }
        finally {
            ((Deflater)deflater).end();
        }
        return deflatedOut;
    }
    
    static {
        compressionStrategies = Arrays.asList(0, 1, 2);
    }
}
