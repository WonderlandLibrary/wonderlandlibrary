/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.hash.AbstractStreamingHashFunction;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import java.nio.charset.Charset;

abstract class AbstractCompositeHashFunction
extends AbstractStreamingHashFunction {
    final HashFunction[] functions;
    private static final long serialVersionUID = 0L;

    AbstractCompositeHashFunction(HashFunction ... functions) {
        for (HashFunction function : functions) {
            Preconditions.checkNotNull(function);
        }
        this.functions = functions;
    }

    abstract HashCode makeHash(Hasher[] var1);

    @Override
    public Hasher newHasher() {
        final Hasher[] hashers = new Hasher[this.functions.length];
        for (int i2 = 0; i2 < hashers.length; ++i2) {
            hashers[i2] = this.functions[i2].newHasher();
        }
        return new Hasher(){

            @Override
            public Hasher putByte(byte b2) {
                for (Hasher hasher : hashers) {
                    hasher.putByte(b2);
                }
                return this;
            }

            @Override
            public Hasher putBytes(byte[] bytes) {
                for (Hasher hasher : hashers) {
                    hasher.putBytes(bytes);
                }
                return this;
            }

            @Override
            public Hasher putBytes(byte[] bytes, int off, int len) {
                for (Hasher hasher : hashers) {
                    hasher.putBytes(bytes, off, len);
                }
                return this;
            }

            @Override
            public Hasher putShort(short s2) {
                for (Hasher hasher : hashers) {
                    hasher.putShort(s2);
                }
                return this;
            }

            @Override
            public Hasher putInt(int i2) {
                for (Hasher hasher : hashers) {
                    hasher.putInt(i2);
                }
                return this;
            }

            @Override
            public Hasher putLong(long l2) {
                for (Hasher hasher : hashers) {
                    hasher.putLong(l2);
                }
                return this;
            }

            @Override
            public Hasher putFloat(float f2) {
                for (Hasher hasher : hashers) {
                    hasher.putFloat(f2);
                }
                return this;
            }

            @Override
            public Hasher putDouble(double d2) {
                for (Hasher hasher : hashers) {
                    hasher.putDouble(d2);
                }
                return this;
            }

            @Override
            public Hasher putBoolean(boolean b2) {
                for (Hasher hasher : hashers) {
                    hasher.putBoolean(b2);
                }
                return this;
            }

            @Override
            public Hasher putChar(char c2) {
                for (Hasher hasher : hashers) {
                    hasher.putChar(c2);
                }
                return this;
            }

            @Override
            public Hasher putUnencodedChars(CharSequence chars) {
                for (Hasher hasher : hashers) {
                    hasher.putUnencodedChars(chars);
                }
                return this;
            }

            @Override
            public Hasher putString(CharSequence chars, Charset charset) {
                for (Hasher hasher : hashers) {
                    hasher.putString(chars, charset);
                }
                return this;
            }

            @Override
            public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
                for (Hasher hasher : hashers) {
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

