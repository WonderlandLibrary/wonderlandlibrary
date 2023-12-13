/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CacheBase;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.util.ICUUncheckedIOException;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class Norm2AllModes {
    public final Normalizer2Impl impl;
    public final ComposeNormalizer2 comp;
    public final DecomposeNormalizer2 decomp;
    public final FCDNormalizer2 fcd;
    public final ComposeNormalizer2 fcc;
    private static CacheBase<String, Norm2AllModes, ByteBuffer> cache = new SoftCache<String, Norm2AllModes, ByteBuffer>(){

        @Override
        protected Norm2AllModes createInstance(String key, ByteBuffer bytes) {
            Normalizer2Impl impl = bytes == null ? new Normalizer2Impl().load(key + ".nrm") : new Normalizer2Impl().load(bytes);
            return new Norm2AllModes(impl);
        }
    };
    public static final NoopNormalizer2 NOOP_NORMALIZER2 = new NoopNormalizer2();

    private Norm2AllModes(Normalizer2Impl ni) {
        this.impl = ni;
        this.comp = new ComposeNormalizer2(ni, false);
        this.decomp = new DecomposeNormalizer2(ni);
        this.fcd = new FCDNormalizer2(ni);
        this.fcc = new ComposeNormalizer2(ni, true);
    }

    private static Norm2AllModes getInstanceFromSingleton(Norm2AllModesSingleton singleton) {
        if (singleton.exception != null) {
            throw singleton.exception;
        }
        return singleton.allModes;
    }

    public static Norm2AllModes getNFCInstance() {
        return Norm2AllModes.getInstanceFromSingleton(NFCSingleton.INSTANCE);
    }

    public static Norm2AllModes getNFKCInstance() {
        return Norm2AllModes.getInstanceFromSingleton(NFKCSingleton.INSTANCE);
    }

    public static Norm2AllModes getNFKC_CFInstance() {
        return Norm2AllModes.getInstanceFromSingleton(NFKC_CFSingleton.INSTANCE);
    }

    public static Normalizer2WithImpl getN2WithImpl(int index) {
        switch (index) {
            case 0: {
                return Norm2AllModes.getNFCInstance().decomp;
            }
            case 1: {
                return Norm2AllModes.getNFKCInstance().decomp;
            }
            case 2: {
                return Norm2AllModes.getNFCInstance().comp;
            }
            case 3: {
                return Norm2AllModes.getNFKCInstance().comp;
            }
        }
        return null;
    }

    public static Norm2AllModes getInstance(ByteBuffer bytes, String name) {
        Norm2AllModesSingleton singleton;
        if (bytes == null && (singleton = name.equals("nfc") ? NFCSingleton.INSTANCE : (name.equals("nfkc") ? NFKCSingleton.INSTANCE : (name.equals("nfkc_cf") ? NFKC_CFSingleton.INSTANCE : null))) != null) {
            if (singleton.exception != null) {
                throw singleton.exception;
            }
            return singleton.allModes;
        }
        return cache.getInstance(name, bytes);
    }

    public static Normalizer2 getFCDNormalizer2() {
        return Norm2AllModes.getNFCInstance().fcd;
    }

    private static final class NFKC_CFSingleton {
        private static final Norm2AllModesSingleton INSTANCE = new Norm2AllModesSingleton("nfkc_cf");

        private NFKC_CFSingleton() {
        }
    }

    private static final class NFKCSingleton {
        private static final Norm2AllModesSingleton INSTANCE = new Norm2AllModesSingleton("nfkc");

        private NFKCSingleton() {
        }
    }

    private static final class NFCSingleton {
        private static final Norm2AllModesSingleton INSTANCE = new Norm2AllModesSingleton("nfc");

        private NFCSingleton() {
        }
    }

    private static final class Norm2AllModesSingleton {
        private Norm2AllModes allModes;
        private RuntimeException exception;

        private Norm2AllModesSingleton(String name) {
            try {
                Normalizer2Impl impl = new Normalizer2Impl().load(name + ".nrm");
                this.allModes = new Norm2AllModes(impl);
            }
            catch (RuntimeException e2) {
                this.exception = e2;
            }
        }
    }

    public static final class FCDNormalizer2
    extends Normalizer2WithImpl {
        public FCDNormalizer2(Normalizer2Impl ni) {
            super(ni);
        }

        @Override
        protected void normalize(CharSequence src, Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.makeFCD(src, 0, src.length(), buffer);
        }

        @Override
        protected void normalizeAndAppend(CharSequence src, boolean doNormalize, Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.makeFCDAndAppend(src, doNormalize, buffer);
        }

        @Override
        public int spanQuickCheckYes(CharSequence s2) {
            return this.impl.makeFCD(s2, 0, s2.length(), null);
        }

        @Override
        public int getQuickCheck(int c2) {
            return this.impl.isDecompYes(this.impl.getNorm16(c2)) ? 1 : 0;
        }

        @Override
        public boolean hasBoundaryBefore(int c2) {
            return this.impl.hasFCDBoundaryBefore(c2);
        }

        @Override
        public boolean hasBoundaryAfter(int c2) {
            return this.impl.hasFCDBoundaryAfter(c2);
        }

        @Override
        public boolean isInert(int c2) {
            return this.impl.isFCDInert(c2);
        }
    }

    public static final class ComposeNormalizer2
    extends Normalizer2WithImpl {
        private final boolean onlyContiguous;

        public ComposeNormalizer2(Normalizer2Impl ni, boolean fcc) {
            super(ni);
            this.onlyContiguous = fcc;
        }

        @Override
        protected void normalize(CharSequence src, Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.compose(src, 0, src.length(), this.onlyContiguous, true, buffer);
        }

        @Override
        protected void normalizeAndAppend(CharSequence src, boolean doNormalize, Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.composeAndAppend(src, doNormalize, this.onlyContiguous, buffer);
        }

        @Override
        public boolean isNormalized(CharSequence s2) {
            return this.impl.compose(s2, 0, s2.length(), this.onlyContiguous, false, new Normalizer2Impl.ReorderingBuffer(this.impl, new StringBuilder(), 5));
        }

        @Override
        public Normalizer.QuickCheckResult quickCheck(CharSequence s2) {
            int spanLengthAndMaybe = this.impl.composeQuickCheck(s2, 0, s2.length(), this.onlyContiguous, false);
            if ((spanLengthAndMaybe & 1) != 0) {
                return Normalizer.MAYBE;
            }
            if (spanLengthAndMaybe >>> 1 == s2.length()) {
                return Normalizer.YES;
            }
            return Normalizer.NO;
        }

        @Override
        public int spanQuickCheckYes(CharSequence s2) {
            return this.impl.composeQuickCheck(s2, 0, s2.length(), this.onlyContiguous, true) >>> 1;
        }

        @Override
        public int getQuickCheck(int c2) {
            return this.impl.getCompQuickCheck(this.impl.getNorm16(c2));
        }

        @Override
        public boolean hasBoundaryBefore(int c2) {
            return this.impl.hasCompBoundaryBefore(c2);
        }

        @Override
        public boolean hasBoundaryAfter(int c2) {
            return this.impl.hasCompBoundaryAfter(c2, this.onlyContiguous);
        }

        @Override
        public boolean isInert(int c2) {
            return this.impl.isCompInert(c2, this.onlyContiguous);
        }
    }

    public static final class DecomposeNormalizer2
    extends Normalizer2WithImpl {
        public DecomposeNormalizer2(Normalizer2Impl ni) {
            super(ni);
        }

        @Override
        protected void normalize(CharSequence src, Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.decompose(src, 0, src.length(), buffer);
        }

        @Override
        protected void normalizeAndAppend(CharSequence src, boolean doNormalize, Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.decomposeAndAppend(src, doNormalize, buffer);
        }

        @Override
        public int spanQuickCheckYes(CharSequence s2) {
            return this.impl.decompose(s2, 0, s2.length(), null);
        }

        @Override
        public int getQuickCheck(int c2) {
            return this.impl.isDecompYes(this.impl.getNorm16(c2)) ? 1 : 0;
        }

        @Override
        public boolean hasBoundaryBefore(int c2) {
            return this.impl.hasDecompBoundaryBefore(c2);
        }

        @Override
        public boolean hasBoundaryAfter(int c2) {
            return this.impl.hasDecompBoundaryAfter(c2);
        }

        @Override
        public boolean isInert(int c2) {
            return this.impl.isDecompInert(c2);
        }
    }

    public static abstract class Normalizer2WithImpl
    extends Normalizer2 {
        public final Normalizer2Impl impl;

        public Normalizer2WithImpl(Normalizer2Impl ni) {
            this.impl = ni;
        }

        @Override
        public StringBuilder normalize(CharSequence src, StringBuilder dest) {
            if (dest == src) {
                throw new IllegalArgumentException();
            }
            dest.setLength(0);
            this.normalize(src, new Normalizer2Impl.ReorderingBuffer(this.impl, dest, src.length()));
            return dest;
        }

        @Override
        public Appendable normalize(CharSequence src, Appendable dest) {
            if (dest == src) {
                throw new IllegalArgumentException();
            }
            Normalizer2Impl.ReorderingBuffer buffer = new Normalizer2Impl.ReorderingBuffer(this.impl, dest, src.length());
            this.normalize(src, buffer);
            buffer.flush();
            return dest;
        }

        protected abstract void normalize(CharSequence var1, Normalizer2Impl.ReorderingBuffer var2);

        @Override
        public StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second) {
            return this.normalizeSecondAndAppend(first, second, true);
        }

        @Override
        public StringBuilder append(StringBuilder first, CharSequence second) {
            return this.normalizeSecondAndAppend(first, second, false);
        }

        public StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second, boolean doNormalize) {
            if (first == second) {
                throw new IllegalArgumentException();
            }
            this.normalizeAndAppend(second, doNormalize, new Normalizer2Impl.ReorderingBuffer(this.impl, first, first.length() + second.length()));
            return first;
        }

        protected abstract void normalizeAndAppend(CharSequence var1, boolean var2, Normalizer2Impl.ReorderingBuffer var3);

        @Override
        public String getDecomposition(int c2) {
            return this.impl.getDecomposition(c2);
        }

        @Override
        public String getRawDecomposition(int c2) {
            return this.impl.getRawDecomposition(c2);
        }

        @Override
        public int composePair(int a2, int b2) {
            return this.impl.composePair(a2, b2);
        }

        @Override
        public int getCombiningClass(int c2) {
            return this.impl.getCC(this.impl.getNorm16(c2));
        }

        @Override
        public boolean isNormalized(CharSequence s2) {
            return s2.length() == this.spanQuickCheckYes(s2);
        }

        @Override
        public Normalizer.QuickCheckResult quickCheck(CharSequence s2) {
            return this.isNormalized(s2) ? Normalizer.YES : Normalizer.NO;
        }

        public abstract int getQuickCheck(int var1);
    }

    public static final class NoopNormalizer2
    extends Normalizer2 {
        @Override
        public StringBuilder normalize(CharSequence src, StringBuilder dest) {
            if (dest != src) {
                dest.setLength(0);
                return dest.append(src);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public Appendable normalize(CharSequence src, Appendable dest) {
            if (dest != src) {
                try {
                    return dest.append(src);
                }
                catch (IOException e2) {
                    throw new ICUUncheckedIOException(e2);
                }
            }
            throw new IllegalArgumentException();
        }

        @Override
        public StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second) {
            if (first != second) {
                return first.append(second);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public StringBuilder append(StringBuilder first, CharSequence second) {
            if (first != second) {
                return first.append(second);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public String getDecomposition(int c2) {
            return null;
        }

        @Override
        public boolean isNormalized(CharSequence s2) {
            return true;
        }

        @Override
        public Normalizer.QuickCheckResult quickCheck(CharSequence s2) {
            return Normalizer.YES;
        }

        @Override
        public int spanQuickCheckYes(CharSequence s2) {
            return s2.length();
        }

        @Override
        public boolean hasBoundaryBefore(int c2) {
            return true;
        }

        @Override
        public boolean hasBoundaryAfter(int c2) {
            return true;
        }

        @Override
        public boolean isInert(int c2) {
            return true;
        }
    }
}

