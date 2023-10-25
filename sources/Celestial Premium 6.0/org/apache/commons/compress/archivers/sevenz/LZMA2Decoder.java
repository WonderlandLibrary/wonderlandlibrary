/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.tukaani.xz.FinishableOutputStream
 *  org.tukaani.xz.FinishableWrapperOutputStream
 *  org.tukaani.xz.LZMA2InputStream
 *  org.tukaani.xz.LZMA2Options
 */
package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.CoderBase;
import org.tukaani.xz.FinishableOutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.LZMA2InputStream;
import org.tukaani.xz.LZMA2Options;

class LZMA2Decoder
extends CoderBase {
    LZMA2Decoder() {
        super(LZMA2Options.class, Number.class);
    }

    InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
        try {
            int dictionarySize = this.getDictionarySize(coder);
            return new LZMA2InputStream(in, dictionarySize);
        }
        catch (IllegalArgumentException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    OutputStream encode(OutputStream out, Object opts) throws IOException {
        LZMA2Options options = this.getOptions(opts);
        FinishableWrapperOutputStream wrapped = new FinishableWrapperOutputStream(out);
        return options.getOutputStream((FinishableOutputStream)wrapped);
    }

    byte[] getOptionsAsProperties(Object opts) {
        int dictSize = this.getDictSize(opts);
        int lead = Integer.numberOfLeadingZeros(dictSize);
        int secondBit = (dictSize >>> 30 - lead) - 2;
        return new byte[]{(byte)((19 - lead) * 2 + secondBit)};
    }

    Object getOptionsFromCoder(Coder coder, InputStream in) {
        return this.getDictionarySize(coder);
    }

    private int getDictSize(Object opts) {
        if (opts instanceof LZMA2Options) {
            return ((LZMA2Options)opts).getDictSize();
        }
        return this.numberOptionOrDefault(opts);
    }

    private int getDictionarySize(Coder coder) throws IllegalArgumentException {
        int dictionarySizeBits = 0xFF & coder.properties[0];
        if ((dictionarySizeBits & 0xFFFFFFC0) != 0) {
            throw new IllegalArgumentException("Unsupported LZMA2 property bits");
        }
        if (dictionarySizeBits > 40) {
            throw new IllegalArgumentException("Dictionary larger than 4GiB maximum size");
        }
        if (dictionarySizeBits == 40) {
            return -1;
        }
        return (2 | dictionarySizeBits & 1) << dictionarySizeBits / 2 + 11;
    }

    private LZMA2Options getOptions(Object opts) throws IOException {
        if (opts instanceof LZMA2Options) {
            return (LZMA2Options)opts;
        }
        LZMA2Options options = new LZMA2Options();
        options.setDictSize(this.numberOptionOrDefault(opts));
        return options;
    }

    private int numberOptionOrDefault(Object opts) {
        return LZMA2Decoder.numberOptionOrDefault(opts, 0x800000);
    }
}

