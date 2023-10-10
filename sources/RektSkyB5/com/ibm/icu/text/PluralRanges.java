/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.Output;
import java.util.Arrays;
import java.util.EnumSet;

@Deprecated
public final class PluralRanges
implements Freezable<PluralRanges>,
Comparable<PluralRanges> {
    private volatile boolean isFrozen;
    private Matrix matrix = new Matrix();
    private boolean[] explicit = new boolean[StandardPlural.COUNT];

    @Deprecated
    public PluralRanges() {
    }

    @Deprecated
    public void add(StandardPlural rangeStart, StandardPlural rangeEnd, StandardPlural result) {
        if (this.isFrozen) {
            throw new UnsupportedOperationException();
        }
        this.explicit[result.ordinal()] = true;
        if (rangeStart == null) {
            for (StandardPlural rs : StandardPlural.values()) {
                if (rangeEnd == null) {
                    for (StandardPlural re : StandardPlural.values()) {
                        this.matrix.setIfNew(rs, re, result);
                    }
                    continue;
                }
                this.explicit[rangeEnd.ordinal()] = true;
                this.matrix.setIfNew(rs, rangeEnd, result);
            }
        } else if (rangeEnd == null) {
            this.explicit[rangeStart.ordinal()] = true;
            for (StandardPlural re : StandardPlural.values()) {
                this.matrix.setIfNew(rangeStart, re, result);
            }
        } else {
            this.explicit[rangeStart.ordinal()] = true;
            this.explicit[rangeEnd.ordinal()] = true;
            this.matrix.setIfNew(rangeStart, rangeEnd, result);
        }
    }

    @Deprecated
    public StandardPlural get(StandardPlural start, StandardPlural end) {
        StandardPlural result = this.matrix.get(start, end);
        return result == null ? end : result;
    }

    @Deprecated
    public boolean isExplicit(StandardPlural start, StandardPlural end) {
        return this.matrix.get(start, end) != null;
    }

    @Deprecated
    public boolean isExplicitlySet(StandardPlural count) {
        return this.explicit[count.ordinal()];
    }

    @Deprecated
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PluralRanges)) {
            return false;
        }
        PluralRanges otherPR = (PluralRanges)other;
        return this.matrix.equals(otherPR.matrix) && Arrays.equals(this.explicit, otherPR.explicit);
    }

    @Deprecated
    public int hashCode() {
        return this.matrix.hashCode();
    }

    @Override
    @Deprecated
    public int compareTo(PluralRanges that) {
        return this.matrix.compareTo(that.matrix);
    }

    @Override
    @Deprecated
    public boolean isFrozen() {
        return this.isFrozen;
    }

    @Override
    @Deprecated
    public PluralRanges freeze() {
        this.isFrozen = true;
        return this;
    }

    @Override
    @Deprecated
    public PluralRanges cloneAsThawed() {
        PluralRanges result = new PluralRanges();
        result.explicit = (boolean[])this.explicit.clone();
        result.matrix = this.matrix.clone();
        return result;
    }

    @Deprecated
    public String toString() {
        return this.matrix.toString();
    }

    private static final class Matrix
    implements Comparable<Matrix>,
    Cloneable {
        private byte[] data = new byte[StandardPlural.COUNT * StandardPlural.COUNT];

        Matrix() {
            for (int i2 = 0; i2 < this.data.length; ++i2) {
                this.data[i2] = -1;
            }
        }

        void set(StandardPlural start, StandardPlural end, StandardPlural result) {
            this.data[start.ordinal() * StandardPlural.COUNT + end.ordinal()] = (byte)(result == null ? -1 : (byte)result.ordinal());
        }

        void setIfNew(StandardPlural start, StandardPlural end, StandardPlural result) {
            byte old = this.data[start.ordinal() * StandardPlural.COUNT + end.ordinal()];
            if (old >= 0) {
                throw new IllegalArgumentException("Previously set value for <" + (Object)((Object)start) + ", " + (Object)((Object)end) + ", " + (Object)((Object)StandardPlural.VALUES.get(old)) + ">");
            }
            this.data[start.ordinal() * StandardPlural.COUNT + end.ordinal()] = (byte)(result == null ? -1 : (byte)result.ordinal());
        }

        StandardPlural get(StandardPlural start, StandardPlural end) {
            byte result = this.data[start.ordinal() * StandardPlural.COUNT + end.ordinal()];
            return result < 0 ? null : StandardPlural.VALUES.get(result);
        }

        StandardPlural endSame(StandardPlural end) {
            StandardPlural first = null;
            for (StandardPlural start : StandardPlural.VALUES) {
                StandardPlural item = this.get(start, end);
                if (item == null) continue;
                if (first == null) {
                    first = item;
                    continue;
                }
                if (first == item) continue;
                return null;
            }
            return first;
        }

        StandardPlural startSame(StandardPlural start, EnumSet<StandardPlural> endDone, Output<Boolean> emit) {
            emit.value = false;
            StandardPlural first = null;
            for (StandardPlural end : StandardPlural.VALUES) {
                StandardPlural item = this.get(start, end);
                if (item == null) continue;
                if (first == null) {
                    first = item;
                    continue;
                }
                if (first != item) {
                    return null;
                }
                if (endDone.contains((Object)end)) continue;
                emit.value = true;
            }
            return first;
        }

        public int hashCode() {
            int result = 0;
            for (int i2 = 0; i2 < this.data.length; ++i2) {
                result = result * 37 + this.data[i2];
            }
            return result;
        }

        public boolean equals(Object other) {
            if (!(other instanceof Matrix)) {
                return false;
            }
            return 0 == this.compareTo((Matrix)other);
        }

        @Override
        public int compareTo(Matrix o2) {
            for (int i2 = 0; i2 < this.data.length; ++i2) {
                int diff = this.data[i2] - o2.data[i2];
                if (diff == 0) continue;
                return diff;
            }
            return 0;
        }

        public Matrix clone() {
            Matrix result = new Matrix();
            result.data = (byte[])this.data.clone();
            return result;
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            for (StandardPlural i2 : StandardPlural.values()) {
                for (StandardPlural j2 : StandardPlural.values()) {
                    StandardPlural x2 = this.get(i2, j2);
                    if (x2 == null) continue;
                    result.append((Object)((Object)i2) + " & " + (Object)((Object)j2) + " \u2192 " + (Object)((Object)x2) + ";\n");
                }
            }
            return result.toString();
        }
    }
}

