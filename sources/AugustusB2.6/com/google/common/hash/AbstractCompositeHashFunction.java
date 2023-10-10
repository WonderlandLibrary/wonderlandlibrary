// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.charset.Charset;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;

@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractCompositeHashFunction extends AbstractHashFunction
{
    final HashFunction[] functions;
    private static final long serialVersionUID = 0L;
    
    AbstractCompositeHashFunction(final HashFunction... functions) {
        for (final HashFunction function : functions) {
            Preconditions.checkNotNull(function);
        }
        this.functions = functions;
    }
    
    abstract HashCode makeHash(final Hasher[] p0);
    
    @Override
    public Hasher newHasher() {
        final Hasher[] hashers = new Hasher[this.functions.length];
        for (int i = 0; i < hashers.length; ++i) {
            hashers[i] = this.functions[i].newHasher();
        }
        return this.fromHashers(hashers);
    }
    
    @Override
    public Hasher newHasher(final int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        final Hasher[] hashers = new Hasher[this.functions.length];
        for (int i = 0; i < hashers.length; ++i) {
            hashers[i] = this.functions[i].newHasher(expectedInputSize);
        }
        return this.fromHashers(hashers);
    }
    
    private Hasher fromHashers(final Hasher[] hashers) {
        return new Hasher() {
            @Override
            public Hasher putByte(final byte b) {
                for (final Hasher hasher : hashers) {
                    hasher.putByte(b);
                }
                return this;
            }
            
            @Override
            public Hasher putBytes(final byte[] bytes) {
                for (final Hasher hasher : hashers) {
                    hasher.putBytes(bytes);
                }
                return this;
            }
            
            @Override
            public Hasher putBytes(final byte[] bytes, final int off, final int len) {
                for (final Hasher hasher : hashers) {
                    hasher.putBytes(bytes, off, len);
                }
                return this;
            }
            
            @Override
            public Hasher putBytes(final ByteBuffer bytes) {
                final int pos = bytes.position();
                for (final Hasher hasher : hashers) {
                    Java8Compatibility.position(bytes, pos);
                    hasher.putBytes(bytes);
                }
                return this;
            }
            
            @Override
            public Hasher putShort(final short s) {
                for (final Hasher hasher : hashers) {
                    hasher.putShort(s);
                }
                return this;
            }
            
            @Override
            public Hasher putInt(final int i) {
                for (final Hasher hasher : hashers) {
                    hasher.putInt(i);
                }
                return this;
            }
            
            @Override
            public Hasher putLong(final long l) {
                for (final Hasher hasher : hashers) {
                    hasher.putLong(l);
                }
                return this;
            }
            
            @Override
            public Hasher putFloat(final float f) {
                for (final Hasher hasher : hashers) {
                    hasher.putFloat(f);
                }
                return this;
            }
            
            @Override
            public Hasher putDouble(final double d) {
                for (final Hasher hasher : hashers) {
                    hasher.putDouble(d);
                }
                return this;
            }
            
            @Override
            public Hasher putBoolean(final boolean b) {
                for (final Hasher hasher : hashers) {
                    hasher.putBoolean(b);
                }
                return this;
            }
            
            @Override
            public Hasher putChar(final char c) {
                for (final Hasher hasher : hashers) {
                    hasher.putChar(c);
                }
                return this;
            }
            
            @Override
            public Hasher putUnencodedChars(final CharSequence chars) {
                for (final Hasher hasher : hashers) {
                    hasher.putUnencodedChars(chars);
                }
                return this;
            }
            
            @Override
            public Hasher putString(final CharSequence chars, final Charset charset) {
                for (final Hasher hasher : hashers) {
                    hasher.putString(chars, charset);
                }
                return this;
            }
            
            @Override
            public <T> Hasher putObject(@ParametricNullness final T instance, final Funnel<? super T> funnel) {
                for (final Hasher hasher : hashers) {
                    hasher.putObject(instance, funnel);
                }
                return this;
            }
            
            @Override
            public HashCode hash() {
                return AbstractCompositeHashFunction.this.makeHash(hashers);
            }
        };
    }
}
