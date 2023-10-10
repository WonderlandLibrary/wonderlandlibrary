// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.nbt;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.zip.GZIPOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.util.zip.GZIPInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.nio.file.Path;

public final class BinaryTagIO
{
    private BinaryTagIO() {
    }
    
    public static CompoundTag readPath(final Path path) throws IOException {
        return readInputStream(Files.newInputStream(path, new OpenOption[0]));
    }
    
    public static CompoundTag readInputStream(final InputStream input) throws IOException {
        final DataInputStream dis = new DataInputStream(input);
        try {
            final CompoundTag dataInput = readDataInput(dis);
            dis.close();
            return dataInput;
        }
        catch (Throwable t) {
            try {
                dis.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    public static CompoundTag readCompressedPath(final Path path) throws IOException {
        return readCompressedInputStream(Files.newInputStream(path, new OpenOption[0]));
    }
    
    public static CompoundTag readCompressedInputStream(final InputStream input) throws IOException {
        final DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(input)));
        try {
            final CompoundTag dataInput = readDataInput(dis);
            dis.close();
            return dataInput;
        }
        catch (Throwable t) {
            try {
                dis.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    public static CompoundTag readDataInput(final DataInput input) throws IOException {
        final byte type = input.readByte();
        if (type != 10) {
            throw new IOException(String.format("Expected root tag to be a CompoundTag, was %s", type));
        }
        input.skipBytes(input.readUnsignedShort());
        final CompoundTag compoundTag = new CompoundTag();
        compoundTag.read(input);
        return compoundTag;
    }
    
    public static void writePath(final CompoundTag tag, final Path path) throws IOException {
        writeOutputStream(tag, Files.newOutputStream(path, new OpenOption[0]));
    }
    
    public static void writeOutputStream(final CompoundTag tag, final OutputStream output) throws IOException {
        final DataOutputStream dos = new DataOutputStream(output);
        try {
            writeDataOutput(tag, dos);
            dos.close();
        }
        catch (Throwable t) {
            try {
                dos.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    public static void writeCompressedPath(final CompoundTag tag, final Path path) throws IOException {
        writeCompressedOutputStream(tag, Files.newOutputStream(path, new OpenOption[0]));
    }
    
    public static void writeCompressedOutputStream(final CompoundTag tag, final OutputStream output) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(output));
        try {
            writeDataOutput(tag, dos);
            dos.close();
        }
        catch (Throwable t) {
            try {
                dos.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    public static void writeDataOutput(final CompoundTag tag, final DataOutput output) throws IOException {
        output.writeByte(10);
        output.writeUTF("");
        tag.write(output);
    }
    
    public static CompoundTag readString(final String input) throws IOException {
        try {
            final CharBuffer buffer = new CharBuffer(input);
            final TagStringReader parser = new TagStringReader(buffer);
            final CompoundTag tag = parser.compound();
            if (buffer.skipWhitespace().hasMore()) {
                throw new IOException("Document had trailing content after first CompoundTag");
            }
            return tag;
        }
        catch (StringTagParseException ex) {
            throw new IOException(ex);
        }
    }
    
    public static String writeString(final CompoundTag tag) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final TagStringWriter emit = new TagStringWriter(sb);
        try {
            emit.writeTag(tag);
            emit.close();
        }
        catch (Throwable t) {
            try {
                emit.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
        return sb.toString();
    }
}
