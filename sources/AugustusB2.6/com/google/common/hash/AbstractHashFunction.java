// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.ByteBuffer;
import com.google.common.base.Preconditions;
import java.nio.charset.Charset;
import com.google.errorprone.annotations.Immutable;

@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractHashFunction implements HashFunction
{
    @Override
    public <T> HashCode hashObject(@ParametricNullness final T instance, final Funnel<? super T> funnel) {
        return this.newHasher().putObject(instance, funnel).hash();
    }
    
    @Override
    public HashCode hashUnencodedChars(final CharSequence input) {
        final int len = input.length();
        return this.newHasher(len * 2).putUnencodedChars(input).hash();
    }
    
    @Override
    public HashCode hashString(final CharSequence input, final Charset charset) {
        return this.newHasher().putString(input, charset).hash();
    }
    
    @Override
    public HashCode hashInt(final int input) {
        return this.newHasher(4).putInt(input).hash();
    }
    
    @Override
    public HashCode hashLong(final long input) {
        return this.newHasher(8).putLong(input).hash();
    }
    
    @Override
    public HashCode hashBytes(final byte[] input) {
        return this.hashBytes(input, 0, input.length);
    }
    
    @Override
    public HashCode hashBytes(final byte[] input, final int off, final int len) {
        Preconditions.checkPositionIndexes(off, off + len, input.length);
        return this.newHasher(len).putBytes(input, off, len).hash();
    }
    
    @Override
    public HashCode hashBytes(final ByteBuffer input) {
        return this.newHasher(input.remaining()).putBytes(input).hash();
    }
    
    @Override
    public Hasher newHasher(final int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0, "expectedInputSize must be >= 0 but was %s", expectedInputSize);
        return this.newHasher();
    }
}
