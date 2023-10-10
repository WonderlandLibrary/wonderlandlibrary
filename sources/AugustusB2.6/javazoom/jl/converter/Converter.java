// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.converter;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.File;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.Obuffer;
import javazoom.jl.decoder.Bitstream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Decoder;

public class Converter
{
    public synchronized void convert(final String sourceName, final String destName) throws JavaLayerException {
        this.convert(sourceName, destName, null, null);
    }
    
    public synchronized void convert(final String sourceName, final String destName, final ProgressListener progressListener) throws JavaLayerException {
        this.convert(sourceName, destName, progressListener, null);
    }
    
    public void convert(final String sourceName, String destName, final ProgressListener progressListener, final Decoder.Params decoderParams) throws JavaLayerException {
        if (destName.length() == 0) {
            destName = null;
        }
        try {
            final InputStream in = this.openInput(sourceName);
            this.convert(in, destName, progressListener, decoderParams);
            in.close();
        }
        catch (IOException ioe) {
            throw new JavaLayerException(ioe.getLocalizedMessage(), ioe);
        }
    }
    
    public synchronized void convert(InputStream sourceStream, final String destName, ProgressListener progressListener, final Decoder.Params decoderParams) throws JavaLayerException {
        if (progressListener == null) {
            progressListener = PrintWriterProgressListener.newStdOut(0);
        }
        try {
            if (!(sourceStream instanceof BufferedInputStream)) {
                sourceStream = new BufferedInputStream(sourceStream);
            }
            int frameCount = -1;
            if (sourceStream.markSupported()) {
                sourceStream.mark(-1);
                frameCount = this.countFrames(sourceStream);
                sourceStream.reset();
            }
            progressListener.converterUpdate(1, frameCount, 0);
            Obuffer output = null;
            final Decoder decoder = new Decoder(decoderParams);
            final Bitstream stream = new Bitstream(sourceStream);
            if (frameCount == -1) {
                frameCount = Integer.MAX_VALUE;
            }
            int frame = 0;
            final long startTime = System.currentTimeMillis();
            try {
                while (frame < frameCount) {
                    try {
                        final Header header = stream.readFrame();
                        if (header == null) {
                            break;
                        }
                        progressListener.readFrame(frame, header);
                        if (output == null) {
                            final int channels = (header.mode() == 3) ? 1 : 2;
                            final int freq = header.frequency();
                            output = new WaveFileObuffer(channels, freq, destName);
                            decoder.setOutputBuffer(output);
                        }
                        final Obuffer decoderOutput = decoder.decodeFrame(header, stream);
                        if (decoderOutput != output) {
                            throw new InternalError("Output buffers are different.");
                        }
                        progressListener.decodedFrame(frame, header, output);
                        stream.closeFrame();
                    }
                    catch (Exception ex) {
                        final boolean stop = !progressListener.converterException(ex);
                        if (stop) {
                            throw new JavaLayerException(ex.getLocalizedMessage(), ex);
                        }
                    }
                    ++frame;
                }
            }
            finally {
                if (output != null) {
                    output.close();
                }
            }
            final int time = (int)(System.currentTimeMillis() - startTime);
            progressListener.converterUpdate(2, time, frame);
        }
        catch (IOException ex2) {
            throw new JavaLayerException(ex2.getLocalizedMessage(), ex2);
        }
    }
    
    protected int countFrames(final InputStream in) {
        return -1;
    }
    
    protected InputStream openInput(final String fileName) throws IOException {
        final File file = new File(fileName);
        final InputStream fileIn = new FileInputStream(file);
        final BufferedInputStream bufIn = new BufferedInputStream(fileIn);
        return bufIn;
    }
    
    public static class PrintWriterProgressListener implements ProgressListener
    {
        public static final int NO_DETAIL = 0;
        public static final int EXPERT_DETAIL = 1;
        public static final int VERBOSE_DETAIL = 2;
        public static final int DEBUG_DETAIL = 7;
        public static final int MAX_DETAIL = 10;
        private PrintWriter pw;
        private int detailLevel;
        
        public static PrintWriterProgressListener newStdOut(final int detail) {
            return new PrintWriterProgressListener(new PrintWriter(System.out, true), detail);
        }
        
        public PrintWriterProgressListener(final PrintWriter writer, final int detailLevel) {
            this.pw = writer;
            this.detailLevel = detailLevel;
        }
        
        public boolean isDetail(final int detail) {
            return this.detailLevel >= detail;
        }
        
        @Override
        public void converterUpdate(final int updateID, final int param1, int param2) {
            if (this.isDetail(2)) {
                switch (updateID) {
                    case 2: {
                        if (param2 == 0) {
                            param2 = 1;
                        }
                        this.pw.println();
                        this.pw.println("Converted " + param2 + " frames in " + param1 + " ms (" + param1 / param2 + " ms per frame.)");
                        break;
                    }
                }
            }
        }
        
        @Override
        public void parsedFrame(final int frameNo, final Header header) {
            if (frameNo == 0 && this.isDetail(2)) {
                final String headerString = header.toString();
                this.pw.println("File is a " + headerString);
            }
            else if (this.isDetail(10)) {
                final String headerString = header.toString();
                this.pw.println("Prased frame " + frameNo + ": " + headerString);
            }
        }
        
        @Override
        public void readFrame(final int frameNo, final Header header) {
            if (frameNo == 0 && this.isDetail(2)) {
                final String headerString = header.toString();
                this.pw.println("File is a " + headerString);
            }
            else if (this.isDetail(10)) {
                final String headerString = header.toString();
                this.pw.println("Read frame " + frameNo + ": " + headerString);
            }
        }
        
        @Override
        public void decodedFrame(final int frameNo, final Header header, final Obuffer o) {
            if (this.isDetail(10)) {
                final String headerString = header.toString();
                this.pw.println("Decoded frame " + frameNo + ": " + headerString);
                this.pw.println("Output: " + o);
            }
            else if (this.isDetail(2)) {
                if (frameNo == 0) {
                    this.pw.print("Converting.");
                    this.pw.flush();
                }
                if (frameNo % 10 == 0) {
                    this.pw.print('.');
                    this.pw.flush();
                }
            }
        }
        
        @Override
        public boolean converterException(final Throwable t) {
            if (this.detailLevel > 0) {
                t.printStackTrace(this.pw);
                this.pw.flush();
            }
            return false;
        }
    }
    
    public interface ProgressListener
    {
        public static final int UPDATE_FRAME_COUNT = 1;
        public static final int UPDATE_CONVERT_COMPLETE = 2;
        
        void converterUpdate(final int p0, final int p1, final int p2);
        
        void parsedFrame(final int p0, final Header p1);
        
        void readFrame(final int p0, final Header p1);
        
        void decodedFrame(final int p0, final Header p1, final Obuffer p2);
        
        boolean converterException(final Throwable p0);
    }
}
