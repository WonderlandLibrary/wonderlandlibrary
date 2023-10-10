/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.coll.Collation;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationDataBuilder;
import com.ibm.icu.impl.coll.CollationFastLatin;
import com.ibm.icu.impl.coll.CollationLoader;
import com.ibm.icu.impl.coll.CollationRootElements;
import com.ibm.icu.impl.coll.CollationRuleParser;
import com.ibm.icu.impl.coll.CollationSettings;
import com.ibm.icu.impl.coll.CollationTailoring;
import com.ibm.icu.impl.coll.CollationWeights;
import com.ibm.icu.impl.coll.UTF16CollationIterator;
import com.ibm.icu.impl.coll.UVector32;
import com.ibm.icu.impl.coll.UVector64;
import com.ibm.icu.text.CanonicalIterator;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.text.UnicodeSetIterator;
import com.ibm.icu.util.ULocale;
import java.text.ParseException;

public final class CollationBuilder
extends CollationRuleParser.Sink {
    private static final boolean DEBUG = false;
    private static final UnicodeSet COMPOSITES = new UnicodeSet("[:NFD_QC=N:]");
    private static final int MAX_INDEX = 1048575;
    private static final int HAS_BEFORE2 = 64;
    private static final int HAS_BEFORE3 = 32;
    private static final int IS_TAILORED = 8;
    private Normalizer2 nfd;
    private Normalizer2 fcd;
    private Normalizer2Impl nfcImpl;
    private CollationTailoring base;
    private CollationData baseData;
    private CollationRootElements rootElements;
    private long variableTop;
    private CollationDataBuilder dataBuilder;
    private boolean fastLatinEnabled;
    private UnicodeSet optimizeSet = new UnicodeSet();
    private long[] ces = new long[31];
    private int cesLength;
    private UVector32 rootPrimaryIndexes;
    private UVector64 nodes;

    public CollationBuilder(CollationTailoring b2) {
        this.nfd = Normalizer2.getNFDInstance();
        this.fcd = Norm2AllModes.getFCDNormalizer2();
        this.nfcImpl = Norm2AllModes.getNFCInstance().impl;
        this.base = b2;
        this.baseData = b2.data;
        this.rootElements = new CollationRootElements(b2.data.rootElements);
        this.variableTop = 0L;
        this.dataBuilder = new CollationDataBuilder();
        this.fastLatinEnabled = true;
        this.cesLength = 0;
        this.rootPrimaryIndexes = new UVector32();
        this.nodes = new UVector64();
        this.nfcImpl.ensureCanonIterData();
        this.dataBuilder.initForTailoring(this.baseData);
    }

    public CollationTailoring parseAndBuild(String ruleString) throws ParseException {
        if (this.baseData.rootElements == null) {
            throw new UnsupportedOperationException("missing root elements data, tailoring not supported");
        }
        CollationTailoring tailoring = new CollationTailoring(this.base.settings);
        CollationRuleParser parser = new CollationRuleParser(this.baseData);
        this.variableTop = this.base.settings.readOnly().variableTop;
        parser.setSink(this);
        parser.setImporter(new BundleImporter());
        CollationSettings ownedSettings = tailoring.settings.copyOnWrite();
        parser.parse(ruleString, ownedSettings);
        if (this.dataBuilder.hasMappings()) {
            this.makeTailoredCEs();
            this.closeOverComposites();
            this.finalizeCEs();
            this.optimizeSet.add(0, 127);
            this.optimizeSet.add(192, 255);
            this.optimizeSet.remove(44032, 55203);
            this.dataBuilder.optimize(this.optimizeSet);
            tailoring.ensureOwnedData();
            if (this.fastLatinEnabled) {
                this.dataBuilder.enableFastLatin();
            }
            this.dataBuilder.build(tailoring.ownedData);
            this.dataBuilder = null;
        } else {
            tailoring.data = this.baseData;
        }
        ownedSettings.fastLatinOptions = CollationFastLatin.getOptions(tailoring.data, ownedSettings, ownedSettings.fastLatinPrimaries);
        tailoring.setRules(ruleString);
        tailoring.setVersion(this.base.version, 0);
        return tailoring;
    }

    @Override
    void addReset(int strength, CharSequence str) {
        assert (str.length() != 0);
        if (str.charAt(0) == '\ufffe') {
            this.ces[0] = this.getSpecialResetPosition(str);
            this.cesLength = 1;
            assert ((this.ces[0] & 0xC0C0L) == 0L);
        } else {
            String nfdString = this.nfd.normalize(str);
            this.cesLength = this.dataBuilder.getCEs(nfdString, this.ces, 0);
            if (this.cesLength > 31) {
                throw new IllegalArgumentException("reset position maps to too many collation elements (more than 31)");
            }
        }
        if (strength == 15) {
            return;
        }
        assert (0 <= strength && strength <= 2);
        int index = this.findOrInsertNodeForCEs(strength);
        long node = this.nodes.elementAti(index);
        while (CollationBuilder.strengthFromNode(node) > strength) {
            index = CollationBuilder.previousIndexFromNode(node);
            node = this.nodes.elementAti(index);
        }
        if (CollationBuilder.strengthFromNode(node) == strength && CollationBuilder.isTailoredNode(node)) {
            index = CollationBuilder.previousIndexFromNode(node);
        } else if (strength == 0) {
            int nextIndex;
            long p2 = CollationBuilder.weight32FromNode(node);
            if (p2 == 0L) {
                throw new UnsupportedOperationException("reset primary-before ignorable not possible");
            }
            if (p2 <= this.rootElements.getFirstPrimary()) {
                throw new UnsupportedOperationException("reset primary-before first non-ignorable not supported");
            }
            if (p2 == 0xFF020200L) {
                throw new UnsupportedOperationException("reset primary-before [first trailing] not supported");
            }
            p2 = this.rootElements.getPrimaryBefore(p2, this.baseData.isCompressiblePrimary(p2));
            index = this.findOrInsertNodeForPrimary(p2);
            while ((nextIndex = CollationBuilder.nextIndexFromNode(node = this.nodes.elementAti(index))) != 0) {
                index = nextIndex;
            }
        } else {
            index = this.findCommonNode(index, 1);
            if (strength >= 2) {
                index = this.findCommonNode(index, 2);
            }
            if (CollationBuilder.strengthFromNode(node = this.nodes.elementAti(index)) == strength) {
                int previousWeight16;
                int previousIndex;
                int weight16 = CollationBuilder.weight16FromNode(node);
                if (weight16 == 0) {
                    throw new UnsupportedOperationException(strength == 1 ? "reset secondary-before secondary ignorable not possible" : "reset tertiary-before completely ignorable not possible");
                }
                assert (weight16 > 256);
                weight16 = this.getWeight16Before(index, node, strength);
                int i2 = previousIndex = CollationBuilder.previousIndexFromNode(node);
                while (true) {
                    int previousStrength;
                    if ((previousStrength = CollationBuilder.strengthFromNode(node = this.nodes.elementAti(i2))) < strength) {
                        assert (weight16 >= 1280 || i2 == previousIndex);
                        previousWeight16 = 1280;
                        break;
                    }
                    if (previousStrength == strength && !CollationBuilder.isTailoredNode(node)) {
                        previousWeight16 = CollationBuilder.weight16FromNode(node);
                        break;
                    }
                    i2 = CollationBuilder.previousIndexFromNode(node);
                }
                if (previousWeight16 == weight16) {
                    index = previousIndex;
                } else {
                    node = CollationBuilder.nodeFromWeight16(weight16) | CollationBuilder.nodeFromStrength(strength);
                    index = this.insertNodeBetween(previousIndex, index, node);
                }
            } else {
                int weight16 = this.getWeight16Before(index, node, strength);
                index = this.findOrInsertWeakNode(index, weight16, strength);
            }
            strength = CollationBuilder.ceStrength(this.ces[this.cesLength - 1]);
        }
        this.ces[this.cesLength - 1] = CollationBuilder.tempCEFromIndexAndStrength(index, strength);
    }

    private int getWeight16Before(int index, long node, int level) {
        int weight16;
        assert (CollationBuilder.strengthFromNode(node) < level || !CollationBuilder.isTailoredNode(node));
        int t2 = CollationBuilder.strengthFromNode(node) == 2 ? CollationBuilder.weight16FromNode(node) : 1280;
        while (CollationBuilder.strengthFromNode(node) > 1) {
            index = CollationBuilder.previousIndexFromNode(node);
            node = this.nodes.elementAti(index);
        }
        if (CollationBuilder.isTailoredNode(node)) {
            return 256;
        }
        int s2 = CollationBuilder.strengthFromNode(node) == 1 ? CollationBuilder.weight16FromNode(node) : 1280;
        while (CollationBuilder.strengthFromNode(node) > 0) {
            index = CollationBuilder.previousIndexFromNode(node);
            node = this.nodes.elementAti(index);
        }
        if (CollationBuilder.isTailoredNode(node)) {
            return 256;
        }
        long p2 = CollationBuilder.weight32FromNode(node);
        if (level == 1) {
            weight16 = this.rootElements.getSecondaryBefore(p2, s2);
        } else {
            weight16 = this.rootElements.getTertiaryBefore(p2, s2, t2);
            assert ((weight16 & 0xFFFFC0C0) == 0);
        }
        return weight16;
    }

    private long getSpecialResetPosition(CharSequence str) {
        long node;
        int index;
        long ce;
        assert (str.length() == 2);
        int strength = 0;
        boolean isBoundary = false;
        CollationRuleParser.Position pos = CollationRuleParser.POSITION_VALUES[str.charAt(1) - 10240];
        switch (pos) {
            case FIRST_TERTIARY_IGNORABLE: {
                return 0L;
            }
            case LAST_TERTIARY_IGNORABLE: {
                return 0L;
            }
            case FIRST_SECONDARY_IGNORABLE: {
                int index2 = this.findOrInsertNodeForRootCE(0L, 2);
                long node2 = this.nodes.elementAti(index2);
                index2 = CollationBuilder.nextIndexFromNode(node2);
                if (index2 != 0) {
                    node2 = this.nodes.elementAti(index2);
                    assert (CollationBuilder.strengthFromNode(node2) <= 2);
                    if (CollationBuilder.isTailoredNode(node2) && CollationBuilder.strengthFromNode(node2) == 2) {
                        return CollationBuilder.tempCEFromIndexAndStrength(index2, 2);
                    }
                }
                return this.rootElements.getFirstTertiaryCE();
            }
            case LAST_SECONDARY_IGNORABLE: {
                ce = this.rootElements.getLastTertiaryCE();
                strength = 2;
                break;
            }
            case FIRST_PRIMARY_IGNORABLE: {
                index = this.findOrInsertNodeForRootCE(0L, 1);
                node = this.nodes.elementAti(index);
                while ((index = CollationBuilder.nextIndexFromNode(node)) != 0 && (strength = CollationBuilder.strengthFromNode(node = this.nodes.elementAti(index))) >= 1) {
                    if (strength != 1) continue;
                    if (!CollationBuilder.isTailoredNode(node)) break;
                    if (CollationBuilder.nodeHasBefore3(node)) {
                        index = CollationBuilder.nextIndexFromNode(this.nodes.elementAti(CollationBuilder.nextIndexFromNode(node)));
                        assert (CollationBuilder.isTailoredNode(this.nodes.elementAti(index)));
                    }
                    return CollationBuilder.tempCEFromIndexAndStrength(index, 1);
                }
                ce = this.rootElements.getFirstSecondaryCE();
                strength = 1;
                break;
            }
            case LAST_PRIMARY_IGNORABLE: {
                ce = this.rootElements.getLastSecondaryCE();
                strength = 1;
                break;
            }
            case FIRST_VARIABLE: {
                ce = this.rootElements.getFirstPrimaryCE();
                isBoundary = true;
                break;
            }
            case LAST_VARIABLE: {
                ce = this.rootElements.lastCEWithPrimaryBefore(this.variableTop + 1L);
                break;
            }
            case FIRST_REGULAR: {
                ce = this.rootElements.firstCEWithPrimaryAtLeast(this.variableTop + 1L);
                isBoundary = true;
                break;
            }
            case LAST_REGULAR: {
                ce = this.rootElements.firstCEWithPrimaryAtLeast(this.baseData.getFirstPrimaryForGroup(17));
                break;
            }
            case FIRST_IMPLICIT: {
                ce = this.baseData.getSingleCE(19968);
                break;
            }
            case LAST_IMPLICIT: {
                throw new UnsupportedOperationException("reset to [last implicit] not supported");
            }
            case FIRST_TRAILING: {
                ce = Collation.makeCE(0xFF020200L);
                isBoundary = true;
                break;
            }
            case LAST_TRAILING: {
                throw new IllegalArgumentException("LDML forbids tailoring to U+FFFF");
            }
            default: {
                assert (false);
                return 0L;
            }
        }
        index = this.findOrInsertNodeForRootCE(ce, strength);
        node = this.nodes.elementAti(index);
        if ((pos.ordinal() & 1) == 0) {
            if (!CollationBuilder.nodeHasAnyBefore(node) && isBoundary) {
                index = CollationBuilder.nextIndexFromNode(node);
                if (index != 0) {
                    node = this.nodes.elementAti(index);
                    assert (CollationBuilder.isTailoredNode(node));
                    ce = CollationBuilder.tempCEFromIndexAndStrength(index, strength);
                } else {
                    assert (strength == 0);
                    long p2 = ce >>> 32;
                    int pIndex = this.rootElements.findPrimary(p2);
                    boolean isCompressible = this.baseData.isCompressiblePrimary(p2);
                    p2 = this.rootElements.getPrimaryAfter(p2, pIndex, isCompressible);
                    ce = Collation.makeCE(p2);
                    index = this.findOrInsertNodeForRootCE(ce, 0);
                    node = this.nodes.elementAti(index);
                }
            }
            if (CollationBuilder.nodeHasAnyBefore(node)) {
                if (CollationBuilder.nodeHasBefore2(node)) {
                    index = CollationBuilder.nextIndexFromNode(this.nodes.elementAti(CollationBuilder.nextIndexFromNode(node)));
                    node = this.nodes.elementAti(index);
                }
                if (CollationBuilder.nodeHasBefore3(node)) {
                    index = CollationBuilder.nextIndexFromNode(this.nodes.elementAti(CollationBuilder.nextIndexFromNode(node)));
                }
                assert (CollationBuilder.isTailoredNode(this.nodes.elementAti(index)));
                ce = CollationBuilder.tempCEFromIndexAndStrength(index, strength);
            }
        } else {
            long nextNode;
            int nextIndex;
            while ((nextIndex = CollationBuilder.nextIndexFromNode(node)) != 0 && CollationBuilder.strengthFromNode(nextNode = this.nodes.elementAti(nextIndex)) >= strength) {
                index = nextIndex;
                node = nextNode;
            }
            if (CollationBuilder.isTailoredNode(node)) {
                ce = CollationBuilder.tempCEFromIndexAndStrength(index, strength);
            }
        }
        return ce;
    }

    @Override
    void addRelation(int strength, CharSequence prefix, CharSequence str, CharSequence extension) {
        String nfdPrefix = prefix.length() == 0 ? "" : this.nfd.normalize(prefix);
        String nfdString = this.nfd.normalize(str);
        int nfdLength = nfdString.length();
        if (nfdLength >= 2) {
            char c2 = nfdString.charAt(0);
            if (Normalizer2Impl.Hangul.isJamoL(c2) || Normalizer2Impl.Hangul.isJamoV(c2)) {
                throw new UnsupportedOperationException("contractions starting with conjoining Jamo L or V not supported");
            }
            c2 = nfdString.charAt(nfdLength - 1);
            if (Normalizer2Impl.Hangul.isJamoL(c2) || Normalizer2Impl.Hangul.isJamoV(c2) && Normalizer2Impl.Hangul.isJamoL(nfdString.charAt(nfdLength - 2))) {
                throw new UnsupportedOperationException("contractions ending with conjoining Jamo L or L+V not supported");
            }
        }
        if (strength != 15) {
            int index = this.findOrInsertNodeForCEs(strength);
            assert (this.cesLength > 0);
            long ce = this.ces[this.cesLength - 1];
            if (strength == 0 && !CollationBuilder.isTempCE(ce) && ce >>> 32 == 0L) {
                throw new UnsupportedOperationException("tailoring primary after ignorables not supported");
            }
            if (strength == 3 && ce == 0L) {
                throw new UnsupportedOperationException("tailoring quaternary after tertiary ignorables not supported");
            }
            index = this.insertTailoredNodeAfter(index, strength);
            int tempStrength = CollationBuilder.ceStrength(ce);
            if (strength < tempStrength) {
                tempStrength = strength;
            }
            this.ces[this.cesLength - 1] = CollationBuilder.tempCEFromIndexAndStrength(index, tempStrength);
        }
        this.setCaseBits(nfdString);
        int cesLengthBeforeExtension = this.cesLength;
        if (extension.length() != 0) {
            String nfdExtension = this.nfd.normalize(extension);
            this.cesLength = this.dataBuilder.getCEs(nfdExtension, this.ces, this.cesLength);
            if (this.cesLength > 31) {
                throw new IllegalArgumentException("extension string adds too many collation elements (more than 31 total)");
            }
        }
        int ce32 = -1;
        if (!(nfdPrefix.contentEquals(prefix) && nfdString.contentEquals(str) || this.ignorePrefix(prefix) || this.ignoreString(str))) {
            ce32 = this.addIfDifferent(prefix, str, this.ces, this.cesLength, ce32);
        }
        this.addWithClosure(nfdPrefix, nfdString, this.ces, this.cesLength, ce32);
        this.cesLength = cesLengthBeforeExtension;
    }

    private int findOrInsertNodeForCEs(int strength) {
        long ce;
        assert (0 <= strength && strength <= 3);
        while (true) {
            if (this.cesLength == 0) {
                this.ces[0] = 0L;
                ce = 0L;
                this.cesLength = 1;
                break;
            }
            ce = this.ces[this.cesLength - 1];
            if (CollationBuilder.ceStrength(ce) <= strength) break;
            --this.cesLength;
        }
        if (CollationBuilder.isTempCE(ce)) {
            return CollationBuilder.indexFromTempCE(ce);
        }
        if ((int)(ce >>> 56) == 254) {
            throw new UnsupportedOperationException("tailoring relative to an unassigned code point not supported");
        }
        return this.findOrInsertNodeForRootCE(ce, strength);
    }

    private int findOrInsertNodeForRootCE(long ce, int strength) {
        assert ((int)(ce >>> 56) != 254);
        assert ((ce & 0xC0L) == 0L);
        int index = this.findOrInsertNodeForPrimary(ce >>> 32);
        if (strength >= 1) {
            int lower32 = (int)ce;
            index = this.findOrInsertWeakNode(index, lower32 >>> 16, 1);
            if (strength >= 2) {
                index = this.findOrInsertWeakNode(index, lower32 & 0x3F3F, 2);
            }
        }
        return index;
    }

    private static final int binarySearchForRootPrimaryNode(int[] rootPrimaryIndexes, int length, long[] nodes, long p2) {
        if (length == 0) {
            return -1;
        }
        int start = 0;
        int limit = length;
        int i2;
        long node;
        long nodePrimary;
        while (p2 != (nodePrimary = (node = nodes[rootPrimaryIndexes[i2 = (int)(((long)start + (long)limit) / 2L)]]) >>> 32)) {
            if (p2 < nodePrimary) {
                if (i2 == start) {
                    return ~start;
                }
                limit = i2;
                continue;
            }
            if (i2 == start) {
                return ~(start + 1);
            }
            start = i2;
        }
        return i2;
    }

    private int findOrInsertNodeForPrimary(long p2) {
        int rootIndex = CollationBuilder.binarySearchForRootPrimaryNode(this.rootPrimaryIndexes.getBuffer(), this.rootPrimaryIndexes.size(), this.nodes.getBuffer(), p2);
        if (rootIndex >= 0) {
            return this.rootPrimaryIndexes.elementAti(rootIndex);
        }
        int index = this.nodes.size();
        this.nodes.addElement(CollationBuilder.nodeFromWeight32(p2));
        this.rootPrimaryIndexes.insertElementAt(index, ~rootIndex);
        return index;
    }

    private int findOrInsertWeakNode(int index, int weight16, int level) {
        int nextIndex;
        assert (0 <= index && index < this.nodes.size());
        assert (1 <= level && level <= 2);
        if (weight16 == 1280) {
            return this.findCommonNode(index, level);
        }
        long node = this.nodes.elementAti(index);
        assert (CollationBuilder.strengthFromNode(node) < level);
        if (weight16 != 0 && weight16 < 1280) {
            int hasThisLevelBefore;
            int n2 = hasThisLevelBefore = level == 1 ? 64 : 32;
            if ((node & (long)hasThisLevelBefore) == 0L) {
                long commonNode = CollationBuilder.nodeFromWeight16(1280) | CollationBuilder.nodeFromStrength(level);
                if (level == 1) {
                    commonNode |= node & 0x20L;
                    node &= 0xFFFFFFFFFFFFFFDFL;
                }
                this.nodes.setElementAt(node | (long)hasThisLevelBefore, index);
                int nextIndex2 = CollationBuilder.nextIndexFromNode(node);
                node = CollationBuilder.nodeFromWeight16(weight16) | CollationBuilder.nodeFromStrength(level);
                index = this.insertNodeBetween(index, nextIndex2, node);
                this.insertNodeBetween(index, nextIndex2, commonNode);
                return index;
            }
        }
        while ((nextIndex = CollationBuilder.nextIndexFromNode(node)) != 0) {
            node = this.nodes.elementAti(nextIndex);
            int nextStrength = CollationBuilder.strengthFromNode(node);
            if (nextStrength <= level) {
                if (nextStrength < level) break;
                if (!CollationBuilder.isTailoredNode(node)) {
                    int nextWeight16 = CollationBuilder.weight16FromNode(node);
                    if (nextWeight16 == weight16) {
                        return nextIndex;
                    }
                    if (nextWeight16 > weight16) break;
                }
            }
            index = nextIndex;
        }
        node = CollationBuilder.nodeFromWeight16(weight16) | CollationBuilder.nodeFromStrength(level);
        return this.insertNodeBetween(index, nextIndex, node);
    }

    private int insertTailoredNodeAfter(int index, int strength) {
        int nextIndex;
        assert (0 <= index && index < this.nodes.size());
        if (strength >= 1) {
            index = this.findCommonNode(index, 1);
            if (strength >= 2) {
                index = this.findCommonNode(index, 2);
            }
        }
        long node = this.nodes.elementAti(index);
        while ((nextIndex = CollationBuilder.nextIndexFromNode(node)) != 0 && CollationBuilder.strengthFromNode(node = this.nodes.elementAti(nextIndex)) > strength) {
            index = nextIndex;
        }
        node = 8L | CollationBuilder.nodeFromStrength(strength);
        return this.insertNodeBetween(index, nextIndex, node);
    }

    private int insertNodeBetween(int index, int nextIndex, long node) {
        assert (CollationBuilder.previousIndexFromNode(node) == 0);
        assert (CollationBuilder.nextIndexFromNode(node) == 0);
        assert (CollationBuilder.nextIndexFromNode(this.nodes.elementAti(index)) == nextIndex);
        int newIndex = this.nodes.size();
        this.nodes.addElement(node |= CollationBuilder.nodeFromPreviousIndex(index) | CollationBuilder.nodeFromNextIndex(nextIndex));
        node = this.nodes.elementAti(index);
        this.nodes.setElementAt(CollationBuilder.changeNodeNextIndex(node, newIndex), index);
        if (nextIndex != 0) {
            node = this.nodes.elementAti(nextIndex);
            this.nodes.setElementAt(CollationBuilder.changeNodePreviousIndex(node, newIndex), nextIndex);
        }
        return newIndex;
    }

    private int findCommonNode(int index, int strength) {
        assert (1 <= strength && strength <= 2);
        long node = this.nodes.elementAti(index);
        if (CollationBuilder.strengthFromNode(node) >= strength) {
            return index;
        }
        if (strength == 1 ? !CollationBuilder.nodeHasBefore2(node) : !CollationBuilder.nodeHasBefore3(node)) {
            return index;
        }
        index = CollationBuilder.nextIndexFromNode(node);
        node = this.nodes.elementAti(index);
        assert (!CollationBuilder.isTailoredNode(node) && CollationBuilder.strengthFromNode(node) == strength && CollationBuilder.weight16FromNode(node) < 1280);
        do {
            index = CollationBuilder.nextIndexFromNode(node);
            node = this.nodes.elementAti(index);
            assert (CollationBuilder.strengthFromNode(node) >= strength);
        } while (CollationBuilder.isTailoredNode(node) || CollationBuilder.strengthFromNode(node) > strength || CollationBuilder.weight16FromNode(node) < 1280);
        assert (CollationBuilder.weight16FromNode(node) == 1280);
        return index;
    }

    private void setCaseBits(CharSequence nfdString) {
        int numTailoredPrimaries = 0;
        for (int i2 = 0; i2 < this.cesLength; ++i2) {
            if (CollationBuilder.ceStrength(this.ces[i2]) != 0) continue;
            ++numTailoredPrimaries;
        }
        assert (numTailoredPrimaries <= 31);
        long cases = 0L;
        if (numTailoredPrimaries > 0) {
            CharSequence s2 = nfdString;
            UTF16CollationIterator baseCEs = new UTF16CollationIterator(this.baseData, false, s2, 0);
            int baseCEsLength = baseCEs.fetchCEs() - 1;
            assert (baseCEsLength >= 0 && baseCEs.getCE(baseCEsLength) == 0x101000100L);
            int lastCase = 0;
            int numBasePrimaries = 0;
            for (int i3 = 0; i3 < baseCEsLength; ++i3) {
                long ce = baseCEs.getCE(i3);
                if (ce >>> 32 == 0L) continue;
                ++numBasePrimaries;
                int c2 = (int)ce >> 14 & 3;
                assert (c2 == 0 || c2 == 2);
                if (numBasePrimaries < numTailoredPrimaries) {
                    cases |= (long)c2 << (numBasePrimaries - 1) * 2;
                    continue;
                }
                if (numBasePrimaries == numTailoredPrimaries) {
                    lastCase = c2;
                    continue;
                }
                if (c2 == lastCase) continue;
                lastCase = 1;
                break;
            }
            if (numBasePrimaries >= numTailoredPrimaries) {
                cases |= (long)lastCase << (numTailoredPrimaries - 1) * 2;
            }
        }
        for (int i4 = 0; i4 < this.cesLength; ++i4) {
            long ce = this.ces[i4] & 0xFFFFFFFFFFFF3FFFL;
            int strength = CollationBuilder.ceStrength(ce);
            if (strength == 0) {
                ce |= (cases & 3L) << 14;
                cases >>>= 2;
            } else if (strength == 2) {
                ce |= 0x8000L;
            }
            this.ces[i4] = ce;
        }
    }

    @Override
    void suppressContractions(UnicodeSet set) {
        this.dataBuilder.suppressContractions(set);
    }

    @Override
    void optimize(UnicodeSet set) {
        this.optimizeSet.addAll(set);
    }

    private int addWithClosure(CharSequence nfdPrefix, CharSequence nfdString, long[] newCEs, int newCEsLength, int ce32) {
        ce32 = this.addIfDifferent(nfdPrefix, nfdString, newCEs, newCEsLength, ce32);
        ce32 = this.addOnlyClosure(nfdPrefix, nfdString, newCEs, newCEsLength, ce32);
        this.addTailComposites(nfdPrefix, nfdString);
        return ce32;
    }

    private int addOnlyClosure(CharSequence nfdPrefix, CharSequence nfdString, long[] newCEs, int newCEsLength, int ce32) {
        if (nfdPrefix.length() == 0) {
            String str;
            CanonicalIterator stringIter = new CanonicalIterator(nfdString.toString());
            String prefix = "";
            while ((str = stringIter.next()) != null) {
                if (this.ignoreString(str) || str.contentEquals(nfdString)) continue;
                ce32 = this.addIfDifferent(prefix, str, newCEs, newCEsLength, ce32);
            }
        } else {
            String prefix;
            CanonicalIterator prefixIter = new CanonicalIterator(nfdPrefix.toString());
            CanonicalIterator stringIter = new CanonicalIterator(nfdString.toString());
            while ((prefix = prefixIter.next()) != null) {
                String str;
                if (this.ignorePrefix(prefix)) continue;
                boolean samePrefix = prefix.contentEquals(nfdPrefix);
                while ((str = stringIter.next()) != null) {
                    if (this.ignoreString(str) || samePrefix && str.contentEquals(nfdString)) continue;
                    ce32 = this.addIfDifferent(prefix, str, newCEs, newCEsLength, ce32);
                }
                stringIter.reset();
            }
        }
        return ce32;
    }

    private void addTailComposites(CharSequence nfdPrefix, CharSequence nfdString) {
        int lastStarter;
        int indexAfterLastStarter = nfdString.length();
        while (true) {
            if (indexAfterLastStarter == 0) {
                return;
            }
            lastStarter = Character.codePointBefore(nfdString, indexAfterLastStarter);
            if (this.nfd.getCombiningClass(lastStarter) == 0) break;
            indexAfterLastStarter -= Character.charCount(lastStarter);
        }
        if (Normalizer2Impl.Hangul.isJamoL(lastStarter)) {
            return;
        }
        UnicodeSet composites = new UnicodeSet();
        if (!this.nfcImpl.getCanonStartSet(lastStarter, composites)) {
            return;
        }
        StringBuilder newNFDString = new StringBuilder();
        StringBuilder newString = new StringBuilder();
        long[] newCEs = new long[31];
        UnicodeSetIterator iter = new UnicodeSetIterator(composites);
        while (iter.next()) {
            int ce32;
            int newCEsLength;
            assert (iter.codepoint != UnicodeSetIterator.IS_STRING);
            int composite = iter.codepoint;
            String decomp = this.nfd.getDecomposition(composite);
            if (!this.mergeCompositeIntoString(nfdString, indexAfterLastStarter, composite, decomp, newNFDString, newString) || (newCEsLength = this.dataBuilder.getCEs(nfdPrefix, newNFDString, newCEs, 0)) > 31 || (ce32 = this.addIfDifferent(nfdPrefix, newString, newCEs, newCEsLength, -1)) == -1) continue;
            this.addOnlyClosure(nfdPrefix, newNFDString, newCEs, newCEsLength, ce32);
        }
    }

    private boolean mergeCompositeIntoString(CharSequence nfdString, int indexAfterLastStarter, int composite, CharSequence decomp, StringBuilder newNFDString, StringBuilder newString) {
        assert (Character.codePointBefore(nfdString, indexAfterLastStarter) == Character.codePointAt(decomp, 0));
        int lastStarterLength = Character.offsetByCodePoints(decomp, 0, 1);
        if (lastStarterLength == decomp.length()) {
            return false;
        }
        if (this.equalSubSequences(nfdString, indexAfterLastStarter, decomp, lastStarterLength)) {
            return false;
        }
        newNFDString.setLength(0);
        newNFDString.append(nfdString, 0, indexAfterLastStarter);
        newString.setLength(0);
        newString.append(nfdString, 0, indexAfterLastStarter - lastStarterLength).appendCodePoint(composite);
        int sourceIndex = indexAfterLastStarter;
        int decompIndex = lastStarterLength;
        int sourceChar = -1;
        int sourceCC = 0;
        int decompCC = 0;
        while (true) {
            if (sourceChar < 0) {
                if (sourceIndex >= nfdString.length()) break;
                sourceChar = Character.codePointAt(nfdString, sourceIndex);
                sourceCC = this.nfd.getCombiningClass(sourceChar);
                assert (sourceCC != 0);
            }
            if (decompIndex >= decomp.length()) break;
            int decompChar = Character.codePointAt(decomp, decompIndex);
            decompCC = this.nfd.getCombiningClass(decompChar);
            if (decompCC == 0) {
                return false;
            }
            if (sourceCC < decompCC) {
                return false;
            }
            if (decompCC < sourceCC) {
                newNFDString.appendCodePoint(decompChar);
                decompIndex += Character.charCount(decompChar);
                continue;
            }
            if (decompChar != sourceChar) {
                return false;
            }
            newNFDString.appendCodePoint(decompChar);
            decompIndex += Character.charCount(decompChar);
            sourceIndex += Character.charCount(decompChar);
            sourceChar = -1;
        }
        if (sourceChar >= 0) {
            if (sourceCC < decompCC) {
                return false;
            }
            newNFDString.append(nfdString, sourceIndex, nfdString.length());
            newString.append(nfdString, sourceIndex, nfdString.length());
        } else if (decompIndex < decomp.length()) {
            newNFDString.append(decomp, decompIndex, decomp.length());
        }
        assert (this.nfd.isNormalized(newNFDString));
        assert (this.fcd.isNormalized(newString));
        assert (this.nfd.normalize(newString).equals(newNFDString.toString()));
        return true;
    }

    private boolean equalSubSequences(CharSequence left, int leftStart, CharSequence right, int rightStart) {
        int leftLength = left.length();
        if (leftLength - leftStart != right.length() - rightStart) {
            return false;
        }
        while (leftStart < leftLength) {
            if (left.charAt(leftStart++) == right.charAt(rightStart++)) continue;
            return false;
        }
        return true;
    }

    private boolean ignorePrefix(CharSequence s2) {
        return !this.isFCD(s2);
    }

    private boolean ignoreString(CharSequence s2) {
        return !this.isFCD(s2) || Normalizer2Impl.Hangul.isHangul(s2.charAt(0));
    }

    private boolean isFCD(CharSequence s2) {
        return this.fcd.isNormalized(s2);
    }

    private void closeOverComposites() {
        String prefix = "";
        UnicodeSetIterator iter = new UnicodeSetIterator(COMPOSITES);
        while (iter.next()) {
            assert (iter.codepoint != UnicodeSetIterator.IS_STRING);
            String nfdString = this.nfd.getDecomposition(iter.codepoint);
            this.cesLength = this.dataBuilder.getCEs(nfdString, this.ces, 0);
            if (this.cesLength > 31) continue;
            String composite = iter.getString();
            this.addIfDifferent(prefix, composite, this.ces, this.cesLength, -1);
        }
    }

    private int addIfDifferent(CharSequence prefix, CharSequence str, long[] newCEs, int newCEsLength, int ce32) {
        long[] oldCEs = new long[31];
        int oldCEsLength = this.dataBuilder.getCEs(prefix, str, oldCEs, 0);
        if (!CollationBuilder.sameCEs(newCEs, newCEsLength, oldCEs, oldCEsLength)) {
            if (ce32 == -1) {
                ce32 = this.dataBuilder.encodeCEs(newCEs, newCEsLength);
            }
            this.dataBuilder.addCE32(prefix, str, ce32);
        }
        return ce32;
    }

    private static boolean sameCEs(long[] ces1, int ces1Length, long[] ces2, int ces2Length) {
        if (ces1Length != ces2Length) {
            return false;
        }
        assert (ces1Length <= 31);
        for (int i2 = 0; i2 < ces1Length; ++i2) {
            if (ces1[i2] == ces2[i2]) continue;
            return false;
        }
        return true;
    }

    private static final int alignWeightRight(int w2) {
        if (w2 != 0) {
            while ((w2 & 0xFF) == 0) {
                w2 >>>= 8;
            }
        }
        return w2;
    }

    private void makeTailoredCEs() {
        CollationWeights primaries = new CollationWeights();
        CollationWeights secondaries = new CollationWeights();
        CollationWeights tertiaries = new CollationWeights();
        long[] nodesArray = this.nodes.getBuffer();
        for (int rpi = 0; rpi < this.rootPrimaryIndexes.size(); ++rpi) {
            int s2;
            int i2 = this.rootPrimaryIndexes.elementAti(rpi);
            long node = nodesArray[i2];
            long p2 = CollationBuilder.weight32FromNode(node);
            int t2 = s2 = p2 == 0L ? 0 : 1280;
            int q2 = 0;
            boolean pIsTailored = false;
            boolean sIsTailored = false;
            boolean tIsTailored = false;
            int pIndex = p2 == 0L ? 0 : this.rootElements.findPrimary(p2);
            int nextIndex = CollationBuilder.nextIndexFromNode(node);
            while (nextIndex != 0) {
                i2 = nextIndex;
                node = nodesArray[i2];
                nextIndex = CollationBuilder.nextIndexFromNode(node);
                int strength = CollationBuilder.strengthFromNode(node);
                if (strength == 3) {
                    assert (CollationBuilder.isTailoredNode(node));
                    if (q2 == 3) {
                        throw new UnsupportedOperationException("quaternary tailoring gap too small");
                    }
                    ++q2;
                } else {
                    if (strength == 2) {
                        if (CollationBuilder.isTailoredNode(node)) {
                            if (!tIsTailored) {
                                int tLimit;
                                int tCount = CollationBuilder.countTailoredNodes(nodesArray, nextIndex, 2) + 1;
                                if (t2 == 0) {
                                    t2 = this.rootElements.getTertiaryBoundary() - 256;
                                    tLimit = (int)this.rootElements.getFirstTertiaryCE() & 0x3F3F;
                                } else if (!pIsTailored && !sIsTailored) {
                                    tLimit = this.rootElements.getTertiaryAfter(pIndex, s2, t2);
                                } else if (t2 == 256) {
                                    tLimit = 1280;
                                } else {
                                    assert (t2 == 1280);
                                    tLimit = this.rootElements.getTertiaryBoundary();
                                }
                                assert (tLimit == 16384 || (tLimit & 0xFFFFC0C0) == 0);
                                tertiaries.initForTertiary();
                                if (!tertiaries.allocWeights(t2, tLimit, tCount)) {
                                    throw new UnsupportedOperationException("tertiary tailoring gap too small");
                                }
                                tIsTailored = true;
                            }
                            t2 = (int)tertiaries.nextWeight();
                            assert (t2 != -1);
                        } else {
                            t2 = CollationBuilder.weight16FromNode(node);
                            tIsTailored = false;
                        }
                    } else {
                        if (strength == 1) {
                            if (CollationBuilder.isTailoredNode(node)) {
                                if (!sIsTailored) {
                                    int sLimit;
                                    int sCount = CollationBuilder.countTailoredNodes(nodesArray, nextIndex, 1) + 1;
                                    if (s2 == 0) {
                                        s2 = this.rootElements.getSecondaryBoundary() - 256;
                                        sLimit = (int)(this.rootElements.getFirstSecondaryCE() >> 16);
                                    } else if (!pIsTailored) {
                                        sLimit = this.rootElements.getSecondaryAfter(pIndex, s2);
                                    } else if (s2 == 256) {
                                        sLimit = 1280;
                                    } else {
                                        assert (s2 == 1280);
                                        sLimit = this.rootElements.getSecondaryBoundary();
                                    }
                                    if (s2 == 1280) {
                                        s2 = this.rootElements.getLastCommonSecondary();
                                    }
                                    secondaries.initForSecondary();
                                    if (!secondaries.allocWeights(s2, sLimit, sCount)) {
                                        throw new UnsupportedOperationException("secondary tailoring gap too small");
                                    }
                                    sIsTailored = true;
                                }
                                s2 = (int)secondaries.nextWeight();
                                assert (s2 != -1);
                            } else {
                                s2 = CollationBuilder.weight16FromNode(node);
                                sIsTailored = false;
                            }
                        } else {
                            assert (CollationBuilder.isTailoredNode(node));
                            if (!pIsTailored) {
                                int pCount = CollationBuilder.countTailoredNodes(nodesArray, nextIndex, 0) + 1;
                                boolean isCompressible = this.baseData.isCompressiblePrimary(p2);
                                long pLimit = this.rootElements.getPrimaryAfter(p2, pIndex, isCompressible);
                                primaries.initForPrimary(isCompressible);
                                if (!primaries.allocWeights(p2, pLimit, pCount)) {
                                    throw new UnsupportedOperationException("primary tailoring gap too small");
                                }
                                pIsTailored = true;
                            }
                            p2 = primaries.nextWeight();
                            assert (p2 != 0xFFFFFFFFL);
                            s2 = 1280;
                            sIsTailored = false;
                        }
                        t2 = s2 == 0 ? 0 : 1280;
                        tIsTailored = false;
                    }
                    q2 = 0;
                }
                if (!CollationBuilder.isTailoredNode(node)) continue;
                nodesArray[i2] = Collation.makeCE(p2, s2, t2, q2);
            }
        }
    }

    private static int countTailoredNodes(long[] nodesArray, int i2, int strength) {
        long node;
        int count = 0;
        while (i2 != 0 && CollationBuilder.strengthFromNode(node = nodesArray[i2]) >= strength) {
            if (CollationBuilder.strengthFromNode(node) == strength) {
                if (!CollationBuilder.isTailoredNode(node)) break;
                ++count;
            }
            i2 = CollationBuilder.nextIndexFromNode(node);
        }
        return count;
    }

    private void finalizeCEs() {
        CollationDataBuilder newBuilder = new CollationDataBuilder();
        newBuilder.initForTailoring(this.baseData);
        CEFinalizer finalizer = new CEFinalizer(this.nodes.getBuffer());
        newBuilder.copyFrom(this.dataBuilder, finalizer);
        this.dataBuilder = newBuilder;
    }

    private static long tempCEFromIndexAndStrength(int index, int strength) {
        return 4629700417037541376L + ((long)(index & 0xFE000) << 43) + ((long)(index & 0x1FC0) << 42) + (long)((index & 0x3F) << 24) + (long)(strength << 8);
    }

    private static int indexFromTempCE(long tempCE) {
        return (int)((tempCE -= 4629700417037541376L) >> 43) & 0xFE000 | (int)(tempCE >> 42) & 0x1FC0 | (int)(tempCE >> 24) & 0x3F;
    }

    private static int strengthFromTempCE(long tempCE) {
        return (int)tempCE >> 8 & 3;
    }

    private static boolean isTempCE(long ce) {
        int sec = (int)ce >>> 24;
        return 6 <= sec && sec <= 69;
    }

    private static int indexFromTempCE32(int tempCE32) {
        return (tempCE32 -= 1077937696) >> 11 & 0xFE000 | tempCE32 >> 10 & 0x1FC0 | tempCE32 >> 8 & 0x3F;
    }

    private static boolean isTempCE32(int ce32) {
        return (ce32 & 0xFF) >= 2 && 6 <= (ce32 >> 8 & 0xFF) && (ce32 >> 8 & 0xFF) <= 69;
    }

    private static int ceStrength(long ce) {
        return CollationBuilder.isTempCE(ce) ? CollationBuilder.strengthFromTempCE(ce) : ((ce & 0xFF00000000000000L) != 0L ? 0 : (((int)ce & 0xFF000000) != 0 ? 1 : (ce != 0L ? 2 : 15)));
    }

    private static long nodeFromWeight32(long weight32) {
        return weight32 << 32;
    }

    private static long nodeFromWeight16(int weight16) {
        return (long)weight16 << 48;
    }

    private static long nodeFromPreviousIndex(int previous) {
        return (long)previous << 28;
    }

    private static long nodeFromNextIndex(int next) {
        return next << 8;
    }

    private static long nodeFromStrength(int strength) {
        return strength;
    }

    private static long weight32FromNode(long node) {
        return node >>> 32;
    }

    private static int weight16FromNode(long node) {
        return (int)(node >> 48) & 0xFFFF;
    }

    private static int previousIndexFromNode(long node) {
        return (int)(node >> 28) & 0xFFFFF;
    }

    private static int nextIndexFromNode(long node) {
        return (int)node >> 8 & 0xFFFFF;
    }

    private static int strengthFromNode(long node) {
        return (int)node & 3;
    }

    private static boolean nodeHasBefore2(long node) {
        return (node & 0x40L) != 0L;
    }

    private static boolean nodeHasBefore3(long node) {
        return (node & 0x20L) != 0L;
    }

    private static boolean nodeHasAnyBefore(long node) {
        return (node & 0x60L) != 0L;
    }

    private static boolean isTailoredNode(long node) {
        return (node & 8L) != 0L;
    }

    private static long changeNodePreviousIndex(long node, int previous) {
        return node & 0xFFFF00000FFFFFFFL | CollationBuilder.nodeFromPreviousIndex(previous);
    }

    private static long changeNodeNextIndex(long node, int next) {
        return node & 0xFFFFFFFFF00000FFL | CollationBuilder.nodeFromNextIndex(next);
    }

    static {
        COMPOSITES.remove(44032, 55203);
    }

    private static final class CEFinalizer
    implements CollationDataBuilder.CEModifier {
        private long[] finalCEs;

        CEFinalizer(long[] ces) {
            this.finalCEs = ces;
        }

        @Override
        public long modifyCE32(int ce32) {
            assert (!Collation.isSpecialCE32(ce32));
            if (CollationBuilder.isTempCE32(ce32)) {
                return this.finalCEs[CollationBuilder.indexFromTempCE32(ce32)] | (long)((ce32 & 0xC0) << 8);
            }
            return 0x101000100L;
        }

        @Override
        public long modifyCE(long ce) {
            if (CollationBuilder.isTempCE(ce)) {
                return this.finalCEs[CollationBuilder.indexFromTempCE(ce)] | ce & 0xC000L;
            }
            return 0x101000100L;
        }
    }

    private static final class BundleImporter
    implements CollationRuleParser.Importer {
        BundleImporter() {
        }

        @Override
        public String getRules(String localeID, String collationType) {
            return CollationLoader.loadRules(new ULocale(localeID), collationType);
        }
    }
}

