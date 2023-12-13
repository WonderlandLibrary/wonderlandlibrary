// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.AbstractMap;
import java.util.Map;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

final class BinaryTagReaderImpl implements BinaryTagIO.Reader
{
    private final long maxBytes;
    static final BinaryTagIO.Reader UNLIMITED;
    static final BinaryTagIO.Reader DEFAULT_LIMIT;
    
    BinaryTagReaderImpl(final long maxBytes) {
        this.maxBytes = maxBytes;
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag read(@NotNull final Path path, final BinaryTagIO.Compression compression) throws IOException {
        try (final InputStream is = Files.newInputStream(path, new OpenOption[0])) {
            return this.read(is, compression);
        }
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag read(@NotNull final InputStream input, final BinaryTagIO.Compression compression) throws IOException {
        try (final DataInputStream dis = new DataInputStream(new BufferedInputStream(compression.decompress(IOStreamUtil.closeShield(input))))) {
            return this.read((DataInput)dis);
        }
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag read(@NotNull DataInput input) throws IOException {
        if (!(input instanceof TrackingDataInput)) {
            input = new TrackingDataInput(input, this.maxBytes);
        }
        final BinaryTagType<? extends BinaryTag> type = BinaryTagType.of(input.readByte());
        requireCompound(type);
        input.skipBytes(input.readUnsignedShort());
        return BinaryTagTypes.COMPOUND.read(input);
    }
    
    @Override
    public Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final Path path, final BinaryTagIO.Compression compression) throws IOException {
        try (final InputStream is = Files.newInputStream(path, new OpenOption[0])) {
            return this.readNamed(is, compression);
        }
    }
    
    @Override
    public Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final InputStream input, final BinaryTagIO.Compression compression) throws IOException {
        try (final DataInputStream dis = new DataInputStream(new BufferedInputStream(compression.decompress(IOStreamUtil.closeShield(input))))) {
            return this.readNamed((DataInput)dis);
        }
    }
    
    @Override
    public Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final DataInput input) throws IOException {
        final BinaryTagType<? extends BinaryTag> type = BinaryTagType.of(input.readByte());
        requireCompound(type);
        final String name = input.readUTF();
        return new AbstractMap.SimpleImmutableEntry<String, CompoundBinaryTag>(name, BinaryTagTypes.COMPOUND.read(input));
    }
    
    private static void requireCompound(final BinaryTagType<? extends BinaryTag> type) throws IOException {
        if (type != BinaryTagTypes.COMPOUND) {
            throw new IOException(String.format("Expected root tag to be a %s, was %s", BinaryTagTypes.COMPOUND, type));
        }
    }
    
    static {
        UNLIMITED = new BinaryTagReaderImpl(-1L);
        DEFAULT_LIMIT = new BinaryTagReaderImpl(131082L);
    }
}
