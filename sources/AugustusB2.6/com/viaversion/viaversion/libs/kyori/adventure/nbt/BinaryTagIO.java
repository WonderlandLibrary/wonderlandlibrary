// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.Map;
import java.io.DataOutput;
import java.io.OutputStream;
import java.io.DataInput;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

public final class BinaryTagIO
{
    private BinaryTagIO() {
    }
    
    @NotNull
    public static Reader unlimitedReader() {
        return BinaryTagReaderImpl.UNLIMITED;
    }
    
    @NotNull
    public static Reader reader() {
        return BinaryTagReaderImpl.DEFAULT_LIMIT;
    }
    
    @NotNull
    public static Reader reader(final long sizeLimitBytes) {
        if (sizeLimitBytes <= 0L) {
            throw new IllegalArgumentException("The size limit must be greater than zero");
        }
        return new BinaryTagReaderImpl(sizeLimitBytes);
    }
    
    @NotNull
    public static Writer writer() {
        return BinaryTagWriterImpl.INSTANCE;
    }
    
    @Deprecated
    @NotNull
    public static CompoundBinaryTag readPath(@NotNull final Path path) throws IOException {
        return reader().read(path);
    }
    
    @Deprecated
    @NotNull
    public static CompoundBinaryTag readInputStream(@NotNull final InputStream input) throws IOException {
        return reader().read(input);
    }
    
    @Deprecated
    @NotNull
    public static CompoundBinaryTag readCompressedPath(@NotNull final Path path) throws IOException {
        return reader().read(path, Compression.GZIP);
    }
    
    @Deprecated
    @NotNull
    public static CompoundBinaryTag readCompressedInputStream(@NotNull final InputStream input) throws IOException {
        return reader().read(input, Compression.GZIP);
    }
    
    @Deprecated
    @NotNull
    public static CompoundBinaryTag readDataInput(@NotNull final DataInput input) throws IOException {
        return reader().read(input);
    }
    
    @Deprecated
    public static void writePath(@NotNull final CompoundBinaryTag tag, @NotNull final Path path) throws IOException {
        writer().write(tag, path);
    }
    
    @Deprecated
    public static void writeOutputStream(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output) throws IOException {
        writer().write(tag, output);
    }
    
    @Deprecated
    public static void writeCompressedPath(@NotNull final CompoundBinaryTag tag, @NotNull final Path path) throws IOException {
        writer().write(tag, path, Compression.GZIP);
    }
    
    @Deprecated
    public static void writeCompressedOutputStream(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output) throws IOException {
        writer().write(tag, output, Compression.GZIP);
    }
    
    @Deprecated
    public static void writeDataOutput(@NotNull final CompoundBinaryTag tag, @NotNull final DataOutput output) throws IOException {
        writer().write(tag, output);
    }
    
    static {
        BinaryTagTypes.COMPOUND.id();
    }
    
    public interface Reader
    {
        @NotNull
        default CompoundBinaryTag read(@NotNull final Path path) throws IOException {
            return this.read(path, Compression.NONE);
        }
        
        @NotNull
        CompoundBinaryTag read(@NotNull final Path path, @NotNull final Compression compression) throws IOException;
        
        @NotNull
        default CompoundBinaryTag read(@NotNull final InputStream input) throws IOException {
            return this.read(input, Compression.NONE);
        }
        
        @NotNull
        CompoundBinaryTag read(@NotNull final InputStream input, @NotNull final Compression compression) throws IOException;
        
        @NotNull
        CompoundBinaryTag read(@NotNull final DataInput input) throws IOException;
        
        default Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final Path path) throws IOException {
            return this.readNamed(path, Compression.NONE);
        }
        
        Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final Path path, @NotNull final Compression compression) throws IOException;
        
        default Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final InputStream input) throws IOException {
            return this.readNamed(input, Compression.NONE);
        }
        
        Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final InputStream input, @NotNull final Compression compression) throws IOException;
        
        Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final DataInput input) throws IOException;
    }
    
    public interface Writer
    {
        default void write(@NotNull final CompoundBinaryTag tag, @NotNull final Path path) throws IOException {
            this.write(tag, path, Compression.NONE);
        }
        
        void write(@NotNull final CompoundBinaryTag tag, @NotNull final Path path, @NotNull final Compression compression) throws IOException;
        
        default void write(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output) throws IOException {
            this.write(tag, output, Compression.NONE);
        }
        
        void write(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output, @NotNull final Compression compression) throws IOException;
        
        void write(@NotNull final CompoundBinaryTag tag, @NotNull final DataOutput output) throws IOException;
        
        default void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, @NotNull final Path path) throws IOException {
            this.writeNamed(tag, path, Compression.NONE);
        }
        
        void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, @NotNull final Path path, @NotNull final Compression compression) throws IOException;
        
        default void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, @NotNull final OutputStream output) throws IOException {
            this.writeNamed(tag, output, Compression.NONE);
        }
        
        void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, @NotNull final OutputStream output, @NotNull final Compression compression) throws IOException;
        
        void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, @NotNull final DataOutput output) throws IOException;
    }
    
    public abstract static class Compression
    {
        public static final Compression NONE;
        public static final Compression GZIP;
        public static final Compression ZLIB;
        
        @NotNull
        abstract InputStream decompress(@NotNull final InputStream is) throws IOException;
        
        @NotNull
        abstract OutputStream compress(@NotNull final OutputStream os) throws IOException;
        
        static {
            NONE = new Compression() {
                @NotNull
                @Override
                InputStream decompress(@NotNull final InputStream is) {
                    return is;
                }
                
                @NotNull
                @Override
                OutputStream compress(@NotNull final OutputStream os) {
                    return os;
                }
                
                @Override
                public String toString() {
                    return "Compression.NONE";
                }
            };
            GZIP = new Compression() {
                @NotNull
                @Override
                InputStream decompress(@NotNull final InputStream is) throws IOException {
                    return new GZIPInputStream(is);
                }
                
                @NotNull
                @Override
                OutputStream compress(@NotNull final OutputStream os) throws IOException {
                    return new GZIPOutputStream(os);
                }
                
                @Override
                public String toString() {
                    return "Compression.GZIP";
                }
            };
            ZLIB = new Compression() {
                @NotNull
                @Override
                InputStream decompress(@NotNull final InputStream is) {
                    return new InflaterInputStream(is);
                }
                
                @NotNull
                @Override
                OutputStream compress(@NotNull final OutputStream os) {
                    return new DeflaterOutputStream(os);
                }
                
                @Override
                public String toString() {
                    return "Compression.ZLIB";
                }
            };
        }
    }
}
