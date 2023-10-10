// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.Arrays;
import java.io.Writer;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public final class TagStringIO
{
    private static final TagStringIO INSTANCE;
    private final boolean acceptLegacy;
    private final boolean emitLegacy;
    private final String indent;
    
    @NotNull
    public static TagStringIO get() {
        return TagStringIO.INSTANCE;
    }
    
    @NotNull
    public static Builder builder() {
        return new Builder();
    }
    
    private TagStringIO(@NotNull final Builder builder) {
        this.acceptLegacy = builder.acceptLegacy;
        this.emitLegacy = builder.emitLegacy;
        this.indent = builder.indent;
    }
    
    public CompoundBinaryTag asCompound(final String input) throws IOException {
        try {
            final CharBuffer buffer = new CharBuffer(input);
            final TagStringReader parser = new TagStringReader(buffer);
            parser.legacy(this.acceptLegacy);
            final CompoundBinaryTag tag = parser.compound();
            if (buffer.skipWhitespace().hasMore()) {
                throw new IOException("Document had trailing content after first CompoundTag");
            }
            return tag;
        }
        catch (StringTagParseException ex) {
            throw new IOException(ex);
        }
    }
    
    public String asString(final CompoundBinaryTag input) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final TagStringWriter emit = new TagStringWriter(sb, this.indent);
        try {
            emit.legacy(this.emitLegacy);
            emit.writeTag(input);
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
    
    public void toWriter(final CompoundBinaryTag input, final Writer dest) throws IOException {
        final TagStringWriter emit = new TagStringWriter(dest, this.indent);
        try {
            emit.legacy(this.emitLegacy);
            emit.writeTag(input);
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
    }
    
    static {
        INSTANCE = new TagStringIO(new Builder());
    }
    
    public static class Builder
    {
        private boolean acceptLegacy;
        private boolean emitLegacy;
        private String indent;
        
        Builder() {
            this.acceptLegacy = true;
            this.emitLegacy = false;
            this.indent = "";
        }
        
        @NotNull
        public Builder indent(final int spaces) {
            if (spaces == 0) {
                this.indent = "";
            }
            else if ((this.indent.length() > 0 && this.indent.charAt(0) != ' ') || spaces != this.indent.length()) {
                final char[] indent = new char[spaces];
                Arrays.fill(indent, ' ');
                this.indent = String.copyValueOf(indent);
            }
            return this;
        }
        
        @NotNull
        public Builder indentTab(final int tabs) {
            if (tabs == 0) {
                this.indent = "";
            }
            else if ((this.indent.length() > 0 && this.indent.charAt(0) != '\t') || tabs != this.indent.length()) {
                final char[] indent = new char[tabs];
                Arrays.fill(indent, '\t');
                this.indent = String.copyValueOf(indent);
            }
            return this;
        }
        
        @NotNull
        public Builder acceptLegacy(final boolean legacy) {
            this.acceptLegacy = legacy;
            return this;
        }
        
        @NotNull
        public Builder emitLegacy(final boolean legacy) {
            this.emitLegacy = legacy;
            return this;
        }
        
        @NotNull
        public TagStringIO build() {
            return new TagStringIO(this, null);
        }
    }
}
