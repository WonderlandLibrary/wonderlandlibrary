/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.tukaani.xz.ARMOptions
 *  org.tukaani.xz.ARMThumbOptions
 *  org.tukaani.xz.FilterOptions
 *  org.tukaani.xz.FinishableOutputStream
 *  org.tukaani.xz.FinishableWrapperOutputStream
 *  org.tukaani.xz.IA64Options
 *  org.tukaani.xz.LZMAInputStream
 *  org.tukaani.xz.PowerPCOptions
 *  org.tukaani.xz.SPARCOptions
 *  org.tukaani.xz.X86Options
 */
package org.apache.commons.compress.archivers.sevenz;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.commons.compress.archivers.sevenz.AES256SHA256Decoder;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.CoderBase;
import org.apache.commons.compress.archivers.sevenz.DeltaDecoder;
import org.apache.commons.compress.archivers.sevenz.LZMA2Decoder;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.tukaani.xz.ARMOptions;
import org.tukaani.xz.ARMThumbOptions;
import org.tukaani.xz.FilterOptions;
import org.tukaani.xz.FinishableOutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.IA64Options;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.PowerPCOptions;
import org.tukaani.xz.SPARCOptions;
import org.tukaani.xz.X86Options;

class Coders {
    private static final Map<SevenZMethod, CoderBase> CODER_MAP = new HashMap<SevenZMethod, CoderBase>(){
        private static final long serialVersionUID = 1664829131806520867L;
        {
            this.put(SevenZMethod.COPY, new CopyDecoder());
            this.put(SevenZMethod.LZMA, new LZMADecoder());
            this.put(SevenZMethod.LZMA2, new LZMA2Decoder());
            this.put(SevenZMethod.DEFLATE, new DeflateDecoder());
            this.put(SevenZMethod.BZIP2, new BZIP2Decoder());
            this.put(SevenZMethod.AES256SHA256, new AES256SHA256Decoder());
            this.put(SevenZMethod.BCJ_X86_FILTER, new BCJDecoder((FilterOptions)new X86Options()));
            this.put(SevenZMethod.BCJ_PPC_FILTER, new BCJDecoder((FilterOptions)new PowerPCOptions()));
            this.put(SevenZMethod.BCJ_IA64_FILTER, new BCJDecoder((FilterOptions)new IA64Options()));
            this.put(SevenZMethod.BCJ_ARM_FILTER, new BCJDecoder((FilterOptions)new ARMOptions()));
            this.put(SevenZMethod.BCJ_ARM_THUMB_FILTER, new BCJDecoder((FilterOptions)new ARMThumbOptions()));
            this.put(SevenZMethod.BCJ_SPARC_FILTER, new BCJDecoder((FilterOptions)new SPARCOptions()));
            this.put(SevenZMethod.DELTA_FILTER, new DeltaDecoder());
        }
    };

    Coders() {
    }

    static CoderBase findByMethod(SevenZMethod method) {
        return CODER_MAP.get((Object)method);
    }

    static InputStream addDecoder(InputStream is, Coder coder, byte[] password) throws IOException {
        CoderBase cb = Coders.findByMethod(SevenZMethod.byId(coder.decompressionMethodId));
        if (cb == null) {
            throw new IOException("Unsupported compression method " + Arrays.toString(coder.decompressionMethodId));
        }
        return cb.decode(is, coder, password);
    }

    static OutputStream addEncoder(OutputStream out, SevenZMethod method, Object options) throws IOException {
        CoderBase cb = Coders.findByMethod(method);
        if (cb == null) {
            throw new IOException("Unsupported compression method " + (Object)((Object)method));
        }
        return cb.encode(out, options);
    }

    private static class DummyByteAddingInputStream
    extends FilterInputStream {
        private boolean addDummyByte = true;

        private DummyByteAddingInputStream(InputStream in) {
            super(in);
        }

        public int read() throws IOException {
            int result = super.read();
            if (result == -1 && this.addDummyByte) {
                this.addDummyByte = false;
                result = 0;
            }
            return result;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            int result = super.read(b, off, len);
            if (result == -1 && this.addDummyByte) {
                this.addDummyByte = false;
                b[off] = 0;
                return 1;
            }
            return result;
        }
    }

    static class BZIP2Decoder
    extends CoderBase {
        BZIP2Decoder() {
            super(Number.class);
        }

        InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
            return new BZip2CompressorInputStream(in);
        }

        OutputStream encode(OutputStream out, Object options) throws IOException {
            int blockSize = BZIP2Decoder.numberOptionOrDefault(options, 9);
            return new BZip2CompressorOutputStream(out, blockSize);
        }
    }

    static class DeflateDecoder
    extends CoderBase {
        DeflateDecoder() {
            super(Number.class);
        }

        InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
            return new InflaterInputStream(new DummyByteAddingInputStream(in), new Inflater(true));
        }

        OutputStream encode(OutputStream out, Object options) {
            int level = DeflateDecoder.numberOptionOrDefault(options, 9);
            return new DeflaterOutputStream(out, new Deflater(level, true));
        }
    }

    static class BCJDecoder
    extends CoderBase {
        private final FilterOptions opts;

        BCJDecoder(FilterOptions opts) {
            super(new Class[0]);
            this.opts = opts;
        }

        InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
            try {
                return this.opts.getInputStream(in);
            }
            catch (AssertionError e) {
                IOException ex = new IOException("BCJ filter needs XZ for Java > 1.4 - see http://commons.apache.org/proper/commons-compress/limitations.html#7Z");
                ex.initCause((Throwable)((Object)e));
                throw ex;
            }
        }

        OutputStream encode(OutputStream out, Object options) {
            FinishableOutputStream fo = this.opts.getOutputStream((FinishableOutputStream)new FinishableWrapperOutputStream(out));
            return new FilterOutputStream((OutputStream)fo){

                public void flush() {
                }
            };
        }
    }

    static class LZMADecoder
    extends CoderBase {
        LZMADecoder() {
            super(new Class[0]);
        }

        InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
            byte propsByte = coder.properties[0];
            long dictSize = coder.properties[1];
            for (int i = 1; i < 4; ++i) {
                dictSize |= ((long)coder.properties[i + 1] & 0xFFL) << 8 * i;
            }
            if (dictSize > 0x7FFFFFF0L) {
                throw new IOException("Dictionary larger than 4GiB maximum size");
            }
            return new LZMAInputStream(in, -1L, propsByte, (int)dictSize);
        }
    }

    static class CopyDecoder
    extends CoderBase {
        CopyDecoder() {
            super(new Class[0]);
        }

        InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
            return in;
        }

        OutputStream encode(OutputStream out, Object options) {
            return out;
        }
    }
}

