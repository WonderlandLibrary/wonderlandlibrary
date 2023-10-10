// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.io.IOException;
import com.googlecode.pngtastic.core.processing.PngByteArrayOutputStream;
import java.util.Iterator;
import java.io.File;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public final class PngOptimizer extends PngProcessor
{
    private boolean generateDataUriCss;
    private final List<OptimizerResult> results;
    
    public PngOptimizer() {
        this("NONE");
    }
    
    private PngOptimizer(final String logLevel) {
        super(logLevel);
        this.generateDataUriCss = false;
        this.results = new ArrayList<OptimizerResult>();
    }
    
    public final void optimize(final PngImage image, final String outputFileName, final boolean removeGamma, final Integer compressionLevel) throws IOException {
        this.log.debug("=== OPTIMIZING ===", new Object[0]);
        final long start = System.currentTimeMillis();
        final boolean removeGamma2 = false;
        PngImage pngImage;
        if (image.getInterlace() == 1 && image.getSampleBitCount() < 8) {
            pngImage = image;
        }
        else {
            final PngImage result;
            (result = new PngImage(this.log)).setInterlace((short)0);
            final Iterator<PngChunk> iterator = image.getChunks().iterator();
            PngChunk processHeadChunks = PngProcessor.processHeadChunks(result, removeGamma2, iterator);
            final PngByteArrayOutputStream inflatedImageData = this.getInflatedImageData(processHeadChunks, iterator);
            final int rowLength = (int)Math.ceil(image.getWidth() * image.getSampleBitCount() / 8.0f) + 1;
            final List<byte[]> list = (image.getInterlace() == 1) ? this.pngInterlaceHandler.deInterlace((int)image.getWidth(), (int)image.getHeight(), image.getSampleBitCount(), inflatedImageData) : this.getScanlines(inflatedImageData, image.getSampleBitCount(), rowLength, image.getHeight());
            final HashMap<PngFilterType, List<byte[]>> hashMap = new HashMap<PngFilterType, List<byte[]>>();
            final PngFilterType[] standardValues = PngFilterType.standardValues();
            for (int i = 0; i < 5; ++i) {
                final PngFilterType pngFilterType = standardValues[i];
                this.log.debug("Applying filter: %s", pngFilterType);
                final List<byte[]> copyScanlines = copyScanlines(list);
                this.pngFilterHandler.applyFiltering(pngFilterType, copyScanlines, image.getSampleBitCount());
                hashMap.put(pngFilterType, copyScanlines);
            }
            Object o = null;
            byte[] data = null;
            for (final Map.Entry<Object, Object> entry : hashMap.entrySet()) {
                final byte[] deflate = this.pngCompressionHandler.deflate(serialize(entry.getValue()), compressionLevel, true);
                if (data == null || deflate.length < data.length) {
                    data = deflate;
                    o = entry.getKey();
                }
            }
            this.pngFilterHandler.applyAdaptiveFiltering$6fb3b3b3(copyScanlines(list), hashMap, image.getSampleBitCount());
            final byte[] deflate2 = this.pngCompressionHandler.deflate(inflatedImageData, compressionLevel, true);
            this.log.debug("Original=%d, Adaptive=%d, %s=%d", image.getImageData().length, deflate2.length, o, (data == null) ? 0 : data.length);
            if (data == null || deflate2.length < data.length) {
                data = deflate2;
                final PngFilterType adaptive = PngFilterType.ADAPTIVE;
            }
            result.addChunk(new PngChunk("IDAT".getBytes(), data));
            while (processHeadChunks != null) {
                if (processHeadChunks.isCritical() && !"IDAT".equals(processHeadChunks.getTypeString())) {
                    final ByteArrayOutputStream out = new ByteArrayOutputStream(processHeadChunks.getLength());
                    final DataOutputStream dataOutputStream;
                    (dataOutputStream = new DataOutputStream(out)).write(processHeadChunks.getData());
                    dataOutputStream.close();
                    result.addChunk(new PngChunk(processHeadChunks.getType(), out.toByteArray()));
                }
                processHeadChunks = (iterator.hasNext() ? iterator.next() : null);
            }
            final List<PngChunk> chunks;
            if ((chunks = result.getChunks()) != null) {
                final String s = "IEND";
                final List<PngChunk> list2 = chunks;
                if (!s.equals(list2.get(list2.size() - 1).getTypeString())) {
                    result.addChunk(new PngChunk("IEND".getBytes(), new byte[0]));
                }
            }
            pngImage = result;
        }
        final PngImage optimized = pngImage;
        final ByteArrayOutputStream optimizedBytes = new ByteArrayOutputStream();
        final long optimizedSize = optimized.writeDataOutputStream(optimizedBytes).size();
        final File originalFile;
        final long originalFileSize = (originalFile = new File(image.getFileName())).length();
        final byte[] optimalBytes = (optimizedSize < originalFileSize) ? optimizedBytes.toByteArray() : getFileBytes(originalFile, originalFileSize);
        final File file;
        PngImage.writeFileOutputStream(file = new File(outputFileName), optimalBytes);
        final long optimizedFileSize = file.length();
        final long time = System.currentTimeMillis() - start;
        this.log.debug("Optimized in %d milliseconds, size %d", time, optimizedSize);
        this.log.debug("Original length in bytes: %d (%s)", originalFileSize, image.getFileName());
        this.log.debug("Final length in bytes: %d (%s)", optimizedFileSize, outputFileName);
        final long fileSizeDifference = (optimizedFileSize <= originalFileSize) ? (originalFileSize - optimizedFileSize) : (-(optimizedFileSize - originalFileSize));
        this.log.info("%5.2f%% :%6dB ->%6dB (%5dB saved) - %s", fileSizeDifference / (float)originalFileSize * 100.0f, originalFileSize, optimizedFileSize, fileSizeDifference, outputFileName);
        this.results.add(new OptimizerResult(image.getFileName(), originalFileSize, optimizedFileSize, image.getWidth(), image.getHeight(), null));
    }
    
    private static List<byte[]> copyScanlines(final List<byte[]> original) {
        final List<byte[]> copy = new ArrayList<byte[]>(original.size());
        for (final byte[] scanline : original) {
            copy.add(scanline.clone());
        }
        return copy;
    }
    
    private static PngByteArrayOutputStream serialize(final List<byte[]> scanlines) {
        final int scanlineLength;
        final byte[] imageData = new byte[(scanlineLength = scanlines.get(0).length) * scanlines.size()];
        for (int i = 0; i < scanlines.size(); ++i) {
            final int offset = i * scanlineLength;
            System.arraycopy(scanlines.get(i), 0, imageData, offset, scanlineLength);
        }
        return new PngByteArrayOutputStream(imageData);
    }
    
    private static byte[] getFileBytes(final File originalFile, long originalFileSize) throws IOException {
        buffer = ByteBuffer.allocate((int)originalFileSize);
        FileInputStream ins = null;
        try {
            (ins = new FileInputStream(originalFile)).getChannel().read((ByteBuffer)buffer);
            ins.close();
        }
        finally {
            if (ins != null) {
                ins.close();
            }
        }
        return ((ByteBuffer)buffer).array();
    }
    
    public static final class OptimizerResult
    {
        public OptimizerResult(final String fileName, final long originalFileSize, final long optimizedFileSize, final long width, final long height, final String dataUri) {
        }
    }
}
