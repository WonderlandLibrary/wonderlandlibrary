/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.Trie2;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.coll.Collation;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.CharsTrie;

public final class TailoredSet {
    private CollationData data;
    private CollationData baseData;
    private UnicodeSet tailored;
    private StringBuilder unreversedPrefix = new StringBuilder();
    private String suffix;

    public TailoredSet(UnicodeSet t2) {
        this.tailored = t2;
    }

    public void forData(CollationData d2) {
        this.data = d2;
        this.baseData = d2.base;
        assert (this.baseData != null);
        for (Trie2.Range range : this.data.trie) {
            if (range.leadSurrogate) break;
            this.enumTailoredRange(range.startCodePoint, range.endCodePoint, range.value, this);
        }
    }

    private void enumTailoredRange(int start, int end, int ce32, TailoredSet ts) {
        if (ce32 == 192) {
            return;
        }
        ts.handleCE32(start, end, ce32);
    }

    private void handleCE32(int start, int end, int ce32) {
        assert (ce32 != 192);
        if (Collation.isSpecialCE32(ce32) && (ce32 = this.data.getIndirectCE32(ce32)) == 192) {
            return;
        }
        do {
            int baseCE32 = this.baseData.getFinalCE32(this.baseData.getCE32(start));
            if (Collation.isSelfContainedCE32(ce32) && Collation.isSelfContainedCE32(baseCE32)) {
                if (ce32 == baseCE32) continue;
                this.tailored.add(start);
                continue;
            }
            this.compare(start, ce32, baseCE32);
        } while (++start <= end);
    }

    private void compare(int c2, int ce32, int baseCE32) {
        int baseTag;
        int tag;
        int baseIndex;
        int baseIndex2;
        int dataIndex;
        if (Collation.isPrefixCE32(ce32)) {
            dataIndex = Collation.indexFromCE32(ce32);
            ce32 = this.data.getFinalCE32(this.data.getCE32FromContexts(dataIndex));
            if (Collation.isPrefixCE32(baseCE32)) {
                baseIndex2 = Collation.indexFromCE32(baseCE32);
                baseCE32 = this.baseData.getFinalCE32(this.baseData.getCE32FromContexts(baseIndex2));
                this.comparePrefixes(c2, this.data.contexts, dataIndex + 2, this.baseData.contexts, baseIndex2 + 2);
            } else {
                this.addPrefixes(this.data, c2, this.data.contexts, dataIndex + 2);
            }
        } else if (Collation.isPrefixCE32(baseCE32)) {
            baseIndex = Collation.indexFromCE32(baseCE32);
            baseCE32 = this.baseData.getFinalCE32(this.baseData.getCE32FromContexts(baseIndex));
            this.addPrefixes(this.baseData, c2, this.baseData.contexts, baseIndex + 2);
        }
        if (Collation.isContractionCE32(ce32)) {
            dataIndex = Collation.indexFromCE32(ce32);
            ce32 = (ce32 & 0x100) != 0 ? 1 : this.data.getFinalCE32(this.data.getCE32FromContexts(dataIndex));
            if (Collation.isContractionCE32(baseCE32)) {
                baseIndex2 = Collation.indexFromCE32(baseCE32);
                baseCE32 = (baseCE32 & 0x100) != 0 ? 1 : this.baseData.getFinalCE32(this.baseData.getCE32FromContexts(baseIndex2));
                this.compareContractions(c2, this.data.contexts, dataIndex + 2, this.baseData.contexts, baseIndex2 + 2);
            } else {
                this.addContractions(c2, this.data.contexts, dataIndex + 2);
            }
        } else if (Collation.isContractionCE32(baseCE32)) {
            baseIndex = Collation.indexFromCE32(baseCE32);
            baseCE32 = this.baseData.getFinalCE32(this.baseData.getCE32FromContexts(baseIndex));
            this.addContractions(c2, this.baseData.contexts, baseIndex + 2);
        }
        if (Collation.isSpecialCE32(ce32)) {
            tag = Collation.tagFromCE32(ce32);
            assert (tag != 8);
            assert (tag != 9);
            assert (tag != 14);
        } else {
            tag = -1;
        }
        if (Collation.isSpecialCE32(baseCE32)) {
            baseTag = Collation.tagFromCE32(baseCE32);
            assert (baseTag != 8);
            assert (baseTag != 9);
        } else {
            baseTag = -1;
        }
        if (baseTag == 14) {
            if (!Collation.isLongPrimaryCE32(ce32)) {
                this.add(c2);
                return;
            }
            long dataCE = this.baseData.ces[Collation.indexFromCE32(baseCE32)];
            long p2 = Collation.getThreeBytePrimaryForOffsetData(c2, dataCE);
            if (Collation.primaryFromLongPrimaryCE32(ce32) != p2) {
                this.add(c2);
                return;
            }
        }
        if (tag != baseTag) {
            this.add(c2);
            return;
        }
        if (tag == 5) {
            int baseLength;
            int length = Collation.lengthFromCE32(ce32);
            if (length != (baseLength = Collation.lengthFromCE32(baseCE32))) {
                this.add(c2);
                return;
            }
            int idx0 = Collation.indexFromCE32(ce32);
            int idx1 = Collation.indexFromCE32(baseCE32);
            for (int i2 = 0; i2 < length; ++i2) {
                if (this.data.ce32s[idx0 + i2] == this.baseData.ce32s[idx1 + i2]) continue;
                this.add(c2);
                break;
            }
        } else if (tag == 6) {
            int baseLength;
            int length = Collation.lengthFromCE32(ce32);
            if (length != (baseLength = Collation.lengthFromCE32(baseCE32))) {
                this.add(c2);
                return;
            }
            int idx0 = Collation.indexFromCE32(ce32);
            int idx1 = Collation.indexFromCE32(baseCE32);
            for (int i3 = 0; i3 < length; ++i3) {
                if (this.data.ces[idx0 + i3] == this.baseData.ces[idx1 + i3]) continue;
                this.add(c2);
                break;
            }
        } else if (tag == 12) {
            StringBuilder jamos = new StringBuilder();
            int length = Normalizer2Impl.Hangul.decompose(c2, jamos);
            if (this.tailored.contains(jamos.charAt(0)) || this.tailored.contains(jamos.charAt(1)) || length == 3 && this.tailored.contains(jamos.charAt(2))) {
                this.add(c2);
            }
        } else if (ce32 != baseCE32) {
            this.add(c2);
        }
    }

    private void comparePrefixes(int c2, CharSequence p2, int pidx, CharSequence q2, int qidx) {
        CharsTrie.Iterator prefixes = new CharsTrie(p2, pidx).iterator();
        CharsTrie.Iterator basePrefixes = new CharsTrie(q2, qidx).iterator();
        String tp = null;
        String bp = null;
        String none = "\uffff";
        CharsTrie.Entry te = null;
        CharsTrie.Entry be = null;
        while (true) {
            if (tp == null) {
                if (prefixes.hasNext()) {
                    te = prefixes.next();
                    tp = te.chars.toString();
                } else {
                    te = null;
                    tp = none;
                }
            }
            if (bp == null) {
                if (basePrefixes.hasNext()) {
                    be = basePrefixes.next();
                    bp = be.chars.toString();
                } else {
                    be = null;
                    bp = none;
                }
            }
            if (Utility.sameObjects(tp, none) && Utility.sameObjects(bp, none)) break;
            int cmp = tp.compareTo(bp);
            if (cmp < 0) {
                assert (te != null);
                this.addPrefix(this.data, tp, c2, te.value);
                te = null;
                tp = null;
                continue;
            }
            if (cmp > 0) {
                assert (be != null);
                this.addPrefix(this.baseData, bp, c2, be.value);
                be = null;
                bp = null;
                continue;
            }
            this.setPrefix(tp);
            assert (te != null && be != null);
            this.compare(c2, te.value, be.value);
            this.resetPrefix();
            be = null;
            te = null;
            bp = null;
            tp = null;
        }
    }

    private void compareContractions(int c2, CharSequence p2, int pidx, CharSequence q2, int qidx) {
        CharsTrie.Iterator suffixes = new CharsTrie(p2, pidx).iterator();
        CharsTrie.Iterator baseSuffixes = new CharsTrie(q2, qidx).iterator();
        String ts = null;
        String bs = null;
        String none = "\uffff\uffff";
        CharsTrie.Entry te = null;
        CharsTrie.Entry be = null;
        while (true) {
            if (ts == null) {
                if (suffixes.hasNext()) {
                    te = suffixes.next();
                    ts = te.chars.toString();
                } else {
                    te = null;
                    ts = none;
                }
            }
            if (bs == null) {
                if (baseSuffixes.hasNext()) {
                    be = baseSuffixes.next();
                    bs = be.chars.toString();
                } else {
                    be = null;
                    bs = none;
                }
            }
            if (Utility.sameObjects(ts, none) && Utility.sameObjects(bs, none)) break;
            int cmp = ts.compareTo(bs);
            if (cmp < 0) {
                this.addSuffix(c2, ts);
                te = null;
                ts = null;
                continue;
            }
            if (cmp > 0) {
                this.addSuffix(c2, bs);
                be = null;
                bs = null;
                continue;
            }
            this.suffix = ts;
            this.compare(c2, te.value, be.value);
            this.suffix = null;
            be = null;
            te = null;
            bs = null;
            ts = null;
        }
    }

    private void addPrefixes(CollationData d2, int c2, CharSequence p2, int pidx) {
        for (CharsTrie.Entry e2 : new CharsTrie(p2, pidx)) {
            this.addPrefix(d2, e2.chars, c2, e2.value);
        }
    }

    private void addPrefix(CollationData d2, CharSequence pfx, int c2, int ce32) {
        this.setPrefix(pfx);
        ce32 = d2.getFinalCE32(ce32);
        if (Collation.isContractionCE32(ce32)) {
            int idx = Collation.indexFromCE32(ce32);
            this.addContractions(c2, d2.contexts, idx + 2);
        }
        this.tailored.add(new StringBuilder(this.unreversedPrefix.appendCodePoint(c2)));
        this.resetPrefix();
    }

    private void addContractions(int c2, CharSequence p2, int pidx) {
        for (CharsTrie.Entry e2 : new CharsTrie(p2, pidx)) {
            this.addSuffix(c2, e2.chars);
        }
    }

    private void addSuffix(int c2, CharSequence sfx) {
        this.tailored.add(new StringBuilder(this.unreversedPrefix).appendCodePoint(c2).append(sfx));
    }

    private void add(int c2) {
        if (this.unreversedPrefix.length() == 0 && this.suffix == null) {
            this.tailored.add(c2);
        } else {
            StringBuilder s2 = new StringBuilder(this.unreversedPrefix);
            s2.appendCodePoint(c2);
            if (this.suffix != null) {
                s2.append(this.suffix);
            }
            this.tailored.add(s2);
        }
    }

    private void setPrefix(CharSequence pfx) {
        this.unreversedPrefix.setLength(0);
        this.unreversedPrefix.append(pfx).reverse();
    }

    private void resetPrefix() {
        this.unreversedPrefix.setLength(0);
    }
}

