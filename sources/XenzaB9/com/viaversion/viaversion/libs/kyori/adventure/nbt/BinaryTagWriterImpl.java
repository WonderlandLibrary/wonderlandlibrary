// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.Map;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

final class BinaryTagWriterImpl implements BinaryTagIO.Writer
{
    static final BinaryTagIO.Writer INSTANCE;
    
    @Override
    public void write(@NotNull final CompoundBinaryTag tag, @NotNull final Path path, final BinaryTagIO.Compression compression) throws IOException {
        try (final OutputStream os = Files.newOutputStream(path, new OpenOption[0])) {
            this.write(tag, os, compression);
        }
    }
    
    @Override
    public void write(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output, final BinaryTagIO.Compression compression) throws IOException {
        try (final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(compression.compress(IOStreamUtil.closeShield(output))))) {
            this.write(tag, (DataOutput)dos);
        }
    }
    
    @Override
    public void write(@NotNull final CompoundBinaryTag tag, @NotNull final DataOutput output) throws IOException {
        output.writeByte(BinaryTagTypes.COMPOUND.id());
        output.writeUTF("");
        BinaryTagTypes.COMPOUND.write(tag, output);
    }
    
    @Override
    public void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, @NotNull final Path path, final BinaryTagIO.Compression compression) throws IOException {
        try (final OutputStream os = Files.newOutputStream(path, new OpenOption[0])) {
            this.writeNamed(tag, os, compression);
        }
    }
    
    @Override
    public void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, @NotNull final OutputStream output, final BinaryTagIO.Compression compression) throws IOException {
        try (final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(compression.compress(IOStreamUtil.closeShield(output))))) {
            this.writeNamed(tag, (DataOutput)dos);
        }
    }
    
    @Override
    public void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, @NotNull final DataOutput output) throws IOException {
        output.writeByte(BinaryTagTypes.COMPOUND.id());
        output.writeUTF(tag.getKey());
        BinaryTagTypes.COMPOUND.write(tag.getValue(), output);
    }
    
    static {
        INSTANCE = new BinaryTagWriterImpl();
    }
}
