/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ClassLoaderUtil;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.coll.BOCSU;
import com.ibm.icu.impl.coll.CollationCompare;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationFastLatin;
import com.ibm.icu.impl.coll.CollationKeys;
import com.ibm.icu.impl.coll.CollationLoader;
import com.ibm.icu.impl.coll.CollationRoot;
import com.ibm.icu.impl.coll.CollationSettings;
import com.ibm.icu.impl.coll.CollationTailoring;
import com.ibm.icu.impl.coll.ContractionsAndExpansions;
import com.ibm.icu.impl.coll.FCDUTF16CollationIterator;
import com.ibm.icu.impl.coll.SharedObject;
import com.ibm.icu.impl.coll.TailoredSet;
import com.ibm.icu.impl.coll.UTF16CollationIterator;
import com.ibm.icu.text.CollationElementIterator;
import com.ibm.icu.text.CollationKey;
import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RawCollationKey;
import com.ibm.icu.text.UCharacterIterator;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.text.UnicodeSetIterator;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.VersionInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.CharacterIterator;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class RuleBasedCollator
extends Collator {
    private Lock frozenLock;
    private CollationBuffer collationBuffer;
    CollationData data;
    SharedObject.Reference<CollationSettings> settings;
    CollationTailoring tailoring;
    private ULocale validLocale;
    private boolean actualLocaleIsSameAsValid;

    public RuleBasedCollator(String rules) throws Exception {
        if (rules == null) {
            throw new IllegalArgumentException("Collation rules can not be null");
        }
        this.validLocale = ULocale.ROOT;
        this.internalBuildTailoring(rules);
    }

    private final void internalBuildTailoring(String rules) throws Exception {
        CollationTailoring t2;
        CollationTailoring base = CollationRoot.getRoot();
        ClassLoader classLoader = ClassLoaderUtil.getClassLoader(this.getClass());
        try {
            Class<?> builderClass = classLoader.loadClass("com.ibm.icu.impl.coll.CollationBuilder");
            Object builder = builderClass.getConstructor(CollationTailoring.class).newInstance(base);
            Method parseAndBuild = builderClass.getMethod("parseAndBuild", String.class);
            t2 = (CollationTailoring)parseAndBuild.invoke(builder, rules);
        }
        catch (InvocationTargetException e2) {
            throw (Exception)e2.getTargetException();
        }
        t2.actualLocale = null;
        this.adoptTailoring(t2);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void initMaxExpansions() {
        CollationTailoring collationTailoring = this.tailoring;
        synchronized (collationTailoring) {
            if (this.tailoring.maxExpansions == null) {
                this.tailoring.maxExpansions = CollationElementIterator.computeMaxExpansions(this.tailoring.data);
            }
        }
    }

    public CollationElementIterator getCollationElementIterator(String source) {
        this.initMaxExpansions();
        return new CollationElementIterator(source, this);
    }

    public CollationElementIterator getCollationElementIterator(CharacterIterator source) {
        this.initMaxExpansions();
        CharacterIterator newsource = (CharacterIterator)source.clone();
        return new CollationElementIterator(newsource, this);
    }

    public CollationElementIterator getCollationElementIterator(UCharacterIterator source) {
        this.initMaxExpansions();
        return new CollationElementIterator(source, this);
    }

    @Override
    public boolean isFrozen() {
        return this.frozenLock != null;
    }

    @Override
    public Collator freeze() {
        if (!this.isFrozen()) {
            this.frozenLock = new ReentrantLock();
            if (this.collationBuffer == null) {
                this.collationBuffer = new CollationBuffer(this.data);
            }
        }
        return this;
    }

    @Override
    public RuleBasedCollator cloneAsThawed() {
        try {
            RuleBasedCollator result = (RuleBasedCollator)super.clone();
            result.settings = this.settings.clone();
            result.collationBuffer = null;
            result.frozenLock = null;
            return result;
        }
        catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    private void checkNotFrozen() {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen RuleBasedCollator");
        }
    }

    private final CollationSettings getOwnedSettings() {
        return this.settings.copyOnWrite();
    }

    private final CollationSettings getDefaultSettings() {
        return this.tailoring.settings.readOnly();
    }

    @Deprecated
    public void setHiraganaQuaternary(boolean flag) {
        this.checkNotFrozen();
    }

    @Deprecated
    public void setHiraganaQuaternaryDefault() {
        this.checkNotFrozen();
    }

    public void setUpperCaseFirst(boolean upperfirst) {
        this.checkNotFrozen();
        if (upperfirst == this.isUpperCaseFirst()) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setCaseFirst(upperfirst ? 768 : 0);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setLowerCaseFirst(boolean lowerfirst) {
        this.checkNotFrozen();
        if (lowerfirst == this.isLowerCaseFirst()) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setCaseFirst(lowerfirst ? 512 : 0);
        this.setFastLatinOptions(ownedSettings);
    }

    public final void setCaseFirstDefault() {
        this.checkNotFrozen();
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (this.settings.readOnly() == defaultSettings) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setCaseFirstDefault(defaultSettings.options);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setAlternateHandlingDefault() {
        this.checkNotFrozen();
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (this.settings.readOnly() == defaultSettings) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setAlternateHandlingDefault(defaultSettings.options);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setCaseLevelDefault() {
        this.checkNotFrozen();
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (this.settings.readOnly() == defaultSettings) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setFlagDefault(1024, defaultSettings.options);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setDecompositionDefault() {
        this.checkNotFrozen();
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (this.settings.readOnly() == defaultSettings) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setFlagDefault(1, defaultSettings.options);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setFrenchCollationDefault() {
        this.checkNotFrozen();
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (this.settings.readOnly() == defaultSettings) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setFlagDefault(2048, defaultSettings.options);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setStrengthDefault() {
        this.checkNotFrozen();
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (this.settings.readOnly() == defaultSettings) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setStrengthDefault(defaultSettings.options);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setNumericCollationDefault() {
        this.checkNotFrozen();
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (this.settings.readOnly() == defaultSettings) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setFlagDefault(2, defaultSettings.options);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setFrenchCollation(boolean flag) {
        this.checkNotFrozen();
        if (flag == this.isFrenchCollation()) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setFlag(2048, flag);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setAlternateHandlingShifted(boolean shifted) {
        this.checkNotFrozen();
        if (shifted == this.isAlternateHandlingShifted()) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setAlternateHandlingShifted(shifted);
        this.setFastLatinOptions(ownedSettings);
    }

    public void setCaseLevel(boolean flag) {
        this.checkNotFrozen();
        if (flag == this.isCaseLevel()) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setFlag(1024, flag);
        this.setFastLatinOptions(ownedSettings);
    }

    @Override
    public void setDecomposition(int decomposition) {
        boolean flag;
        this.checkNotFrozen();
        switch (decomposition) {
            case 16: {
                flag = false;
                break;
            }
            case 17: {
                flag = true;
                break;
            }
            default: {
                throw new IllegalArgumentException("Wrong decomposition mode.");
            }
        }
        if (flag == this.settings.readOnly().getFlag(1)) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setFlag(1, flag);
        this.setFastLatinOptions(ownedSettings);
    }

    @Override
    public void setStrength(int newStrength) {
        this.checkNotFrozen();
        if (newStrength == this.getStrength()) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setStrength(newStrength);
        this.setFastLatinOptions(ownedSettings);
    }

    @Override
    public RuleBasedCollator setMaxVariable(int group) {
        int value;
        if (group == -1) {
            value = -1;
        } else if (4096 <= group && group <= 4099) {
            value = group - 4096;
        } else {
            throw new IllegalArgumentException("illegal max variable group " + group);
        }
        int oldValue = this.settings.readOnly().getMaxVariable();
        if (value == oldValue) {
            return this;
        }
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (this.settings.readOnly() == defaultSettings && value < 0) {
            return this;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        if (group == -1) {
            group = 4096 + defaultSettings.getMaxVariable();
        }
        long varTop = this.data.getLastPrimaryForGroup(group);
        assert (varTop != 0L);
        ownedSettings.setMaxVariable(value, defaultSettings.options);
        ownedSettings.variableTop = varTop;
        this.setFastLatinOptions(ownedSettings);
        return this;
    }

    @Override
    public int getMaxVariable() {
        return 4096 + this.settings.readOnly().getMaxVariable();
    }

    @Override
    @Deprecated
    public int setVariableTop(String varTop) {
        long ce2;
        long ce1;
        this.checkNotFrozen();
        if (varTop == null || varTop.length() == 0) {
            throw new IllegalArgumentException("Variable top argument string can not be null or zero in length.");
        }
        boolean numeric = this.settings.readOnly().isNumeric();
        if (this.settings.readOnly().dontCheckFCD()) {
            UTF16CollationIterator ci = new UTF16CollationIterator(this.data, numeric, varTop, 0);
            ce1 = ci.nextCE();
            ce2 = ci.nextCE();
        } else {
            FCDUTF16CollationIterator ci = new FCDUTF16CollationIterator(this.data, numeric, varTop, 0);
            ce1 = ci.nextCE();
            ce2 = ci.nextCE();
        }
        if (ce1 == 0x101000100L || ce2 != 0x101000100L) {
            throw new IllegalArgumentException("Variable top argument string must map to exactly one collation element");
        }
        this.internalSetVariableTop(ce1 >>> 32);
        return (int)this.settings.readOnly().variableTop;
    }

    @Override
    @Deprecated
    public void setVariableTop(int varTop) {
        this.checkNotFrozen();
        this.internalSetVariableTop((long)varTop & 0xFFFFFFFFL);
    }

    private void internalSetVariableTop(long varTop) {
        if (varTop != this.settings.readOnly().variableTop) {
            int group = this.data.getGroupForPrimary(varTop);
            if (group < 4096 || 4099 < group) {
                throw new IllegalArgumentException("The variable top must be a primary weight in the space/punctuation/symbols/currency symbols range");
            }
            long v2 = this.data.getLastPrimaryForGroup(group);
            assert (v2 != 0L && v2 >= varTop);
            varTop = v2;
            if (varTop != this.settings.readOnly().variableTop) {
                CollationSettings ownedSettings = this.getOwnedSettings();
                ownedSettings.setMaxVariable(group - 4096, this.getDefaultSettings().options);
                ownedSettings.variableTop = varTop;
                this.setFastLatinOptions(ownedSettings);
            }
        }
    }

    public void setNumericCollation(boolean flag) {
        this.checkNotFrozen();
        if (flag == this.getNumericCollation()) {
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        ownedSettings.setFlag(2, flag);
        this.setFastLatinOptions(ownedSettings);
    }

    @Override
    public void setReorderCodes(int ... order) {
        int length;
        this.checkNotFrozen();
        int n2 = length = order != null ? order.length : 0;
        if (length == 1 && order[0] == 103) {
            length = 0;
        }
        if (length == 0 ? this.settings.readOnly().reorderCodes.length == 0 : Arrays.equals(order, this.settings.readOnly().reorderCodes)) {
            return;
        }
        CollationSettings defaultSettings = this.getDefaultSettings();
        if (length == 1 && order[0] == -1) {
            if (this.settings.readOnly() != defaultSettings) {
                CollationSettings ownedSettings = this.getOwnedSettings();
                ownedSettings.copyReorderingFrom(defaultSettings);
                this.setFastLatinOptions(ownedSettings);
            }
            return;
        }
        CollationSettings ownedSettings = this.getOwnedSettings();
        if (length == 0) {
            ownedSettings.resetReordering();
        } else {
            ownedSettings.setReordering(this.data, (int[])order.clone());
        }
        this.setFastLatinOptions(ownedSettings);
    }

    private void setFastLatinOptions(CollationSettings ownedSettings) {
        ownedSettings.fastLatinOptions = CollationFastLatin.getOptions(this.data, ownedSettings, ownedSettings.fastLatinPrimaries);
    }

    public String getRules() {
        return this.tailoring.getRules();
    }

    public String getRules(boolean fullrules) {
        if (!fullrules) {
            return this.tailoring.getRules();
        }
        return CollationLoader.getRootRules() + this.tailoring.getRules();
    }

    @Override
    public UnicodeSet getTailoredSet() {
        UnicodeSet tailored = new UnicodeSet();
        if (this.data.base != null) {
            new TailoredSet(tailored).forData(this.data);
        }
        return tailored;
    }

    public void getContractionsAndExpansions(UnicodeSet contractions, UnicodeSet expansions, boolean addPrefixes) throws Exception {
        if (contractions != null) {
            contractions.clear();
        }
        if (expansions != null) {
            expansions.clear();
        }
        new ContractionsAndExpansions(contractions, expansions, null, addPrefixes).forData(this.data);
    }

    void internalAddContractions(int c2, UnicodeSet set) {
        new ContractionsAndExpansions(set, null, null, false).forCodePoint(this.data, c2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public CollationKey getCollationKey(String source) {
        if (source == null) {
            return null;
        }
        CollationBuffer buffer = null;
        try {
            buffer = this.getCollationBuffer();
            CollationKey collationKey = this.getCollationKey(source, buffer);
            return collationKey;
        }
        finally {
            this.releaseCollationBuffer(buffer);
        }
    }

    private CollationKey getCollationKey(String source, CollationBuffer buffer) {
        buffer.rawCollationKey = this.getRawCollationKey(source, buffer.rawCollationKey, buffer);
        return new CollationKey(source, buffer.rawCollationKey);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public RawCollationKey getRawCollationKey(String source, RawCollationKey key) {
        if (source == null) {
            return null;
        }
        CollationBuffer buffer = null;
        try {
            buffer = this.getCollationBuffer();
            RawCollationKey rawCollationKey = this.getRawCollationKey(source, key, buffer);
            return rawCollationKey;
        }
        finally {
            this.releaseCollationBuffer(buffer);
        }
    }

    private RawCollationKey getRawCollationKey(CharSequence source, RawCollationKey key, CollationBuffer buffer) {
        if (key == null) {
            key = new RawCollationKey(this.simpleKeyLengthEstimate(source));
        } else if (key.bytes == null) {
            key.bytes = new byte[this.simpleKeyLengthEstimate(source)];
        }
        CollationKeyByteSink sink = new CollationKeyByteSink(key);
        this.writeSortKey(source, sink, buffer);
        key.size = sink.NumberOfBytesAppended();
        return key;
    }

    private int simpleKeyLengthEstimate(CharSequence source) {
        return 2 * source.length() + 10;
    }

    private void writeSortKey(CharSequence s2, CollationKeyByteSink sink, CollationBuffer buffer) {
        boolean numeric = this.settings.readOnly().isNumeric();
        if (this.settings.readOnly().dontCheckFCD()) {
            buffer.leftUTF16CollIter.setText(numeric, s2, 0);
            CollationKeys.writeSortKeyUpToQuaternary(buffer.leftUTF16CollIter, this.data.compressibleBytes, this.settings.readOnly(), sink, 1, CollationKeys.SIMPLE_LEVEL_FALLBACK, true);
        } else {
            buffer.leftFCDUTF16Iter.setText(numeric, s2, 0);
            CollationKeys.writeSortKeyUpToQuaternary(buffer.leftFCDUTF16Iter, this.data.compressibleBytes, this.settings.readOnly(), sink, 1, CollationKeys.SIMPLE_LEVEL_FALLBACK, true);
        }
        if (this.settings.readOnly().getStrength() == 15) {
            this.writeIdenticalLevel(s2, sink);
        }
        sink.Append(0);
    }

    private void writeIdenticalLevel(CharSequence s2, CollationKeyByteSink sink) {
        int nfdQCYesLimit = this.data.nfcImpl.decompose(s2, 0, s2.length(), null);
        sink.Append(1);
        ((CollationKeyByteSink)sink).key_.size = sink.NumberOfBytesAppended();
        int prev = 0;
        if (nfdQCYesLimit != 0) {
            prev = BOCSU.writeIdenticalLevelRun(prev, s2, 0, nfdQCYesLimit, sink.key_);
        }
        if (nfdQCYesLimit < s2.length()) {
            int destLengthEstimate = s2.length() - nfdQCYesLimit;
            StringBuilder nfd = new StringBuilder();
            this.data.nfcImpl.decompose(s2, nfdQCYesLimit, s2.length(), nfd, destLengthEstimate);
            BOCSU.writeIdenticalLevelRun(prev, nfd, 0, nfd.length(), sink.key_);
        }
        sink.setBufferAndAppended(((CollationKeyByteSink)sink).key_.bytes, ((CollationKeyByteSink)sink).key_.size);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public long[] internalGetCEs(CharSequence str) {
        CollationBuffer buffer = null;
        try {
            UTF16CollationIterator iter;
            buffer = this.getCollationBuffer();
            boolean numeric = this.settings.readOnly().isNumeric();
            if (this.settings.readOnly().dontCheckFCD()) {
                buffer.leftUTF16CollIter.setText(numeric, str, 0);
                iter = buffer.leftUTF16CollIter;
            } else {
                buffer.leftFCDUTF16Iter.setText(numeric, str, 0);
                iter = buffer.leftFCDUTF16Iter;
            }
            int length = iter.fetchCEs() - 1;
            assert (length >= 0 && iter.getCE(length) == 0x101000100L);
            long[] ces = new long[length];
            System.arraycopy(iter.getCEs(), 0, ces, 0, length);
            long[] lArray = ces;
            return lArray;
        }
        finally {
            this.releaseCollationBuffer(buffer);
        }
    }

    @Override
    public int getStrength() {
        return this.settings.readOnly().getStrength();
    }

    @Override
    public int getDecomposition() {
        return (this.settings.readOnly().options & 1) != 0 ? 17 : 16;
    }

    public boolean isUpperCaseFirst() {
        return this.settings.readOnly().getCaseFirst() == 768;
    }

    public boolean isLowerCaseFirst() {
        return this.settings.readOnly().getCaseFirst() == 512;
    }

    public boolean isAlternateHandlingShifted() {
        return this.settings.readOnly().getAlternateHandling();
    }

    public boolean isCaseLevel() {
        return (this.settings.readOnly().options & 0x400) != 0;
    }

    public boolean isFrenchCollation() {
        return (this.settings.readOnly().options & 0x800) != 0;
    }

    @Deprecated
    public boolean isHiraganaQuaternary() {
        return false;
    }

    @Override
    public int getVariableTop() {
        return (int)this.settings.readOnly().variableTop;
    }

    public boolean getNumericCollation() {
        return (this.settings.readOnly().options & 2) != 0;
    }

    @Override
    public int[] getReorderCodes() {
        return (int[])this.settings.readOnly().reorderCodes.clone();
    }

    @Override
    public boolean equals(Object obj) {
        UnicodeSet otherTailored;
        boolean otherIsRoot;
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        RuleBasedCollator o2 = (RuleBasedCollator)obj;
        if (!this.settings.readOnly().equals(o2.settings.readOnly())) {
            return false;
        }
        if (this.data == o2.data) {
            return true;
        }
        boolean thisIsRoot = this.data.base == null;
        boolean bl = otherIsRoot = o2.data.base == null;
        assert (!thisIsRoot || !otherIsRoot);
        if (thisIsRoot != otherIsRoot) {
            return false;
        }
        String theseRules = this.tailoring.getRules();
        String otherRules = o2.tailoring.getRules();
        if ((thisIsRoot || theseRules.length() != 0) && (otherIsRoot || otherRules.length() != 0) && theseRules.equals(otherRules)) {
            return true;
        }
        UnicodeSet thisTailored = this.getTailoredSet();
        return thisTailored.equals(otherTailored = o2.getTailoredSet());
    }

    @Override
    public int hashCode() {
        int h2 = this.settings.readOnly().hashCode();
        if (this.data.base == null) {
            return h2;
        }
        UnicodeSet set = this.getTailoredSet();
        UnicodeSetIterator iter = new UnicodeSetIterator(set);
        while (iter.next() && iter.codepoint != UnicodeSetIterator.IS_STRING) {
            h2 ^= this.data.getCE32(iter.codepoint);
        }
        return h2;
    }

    @Override
    public int compare(String source, String target) {
        return this.doCompare(source, target);
    }

    private static final int compareNFDIter(Normalizer2Impl nfcImpl, NFDIterator left, NFDIterator right) {
        block3: {
            while (true) {
                int rightCp;
                int leftCp;
                if ((leftCp = left.nextCodePoint()) == (rightCp = right.nextCodePoint())) {
                    if (leftCp >= 0) continue;
                    break block3;
                }
                if ((leftCp = leftCp < 0 ? -2 : (leftCp == 65534 ? -1 : left.nextDecomposedCodePoint(nfcImpl, leftCp))) < (rightCp = rightCp < 0 ? -2 : (rightCp == 65534 ? -1 : right.nextDecomposedCodePoint(nfcImpl, rightCp)))) {
                    return -1;
                }
                if (leftCp > rightCp) break;
            }
            return 1;
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @Deprecated
    protected int doCompare(CharSequence left, CharSequence right) {
        CollationBuffer buffer;
        int fastLatinOptions;
        int result;
        if (left == right) {
            return 0;
        }
        int equalPrefixLength = 0;
        while (true) {
            if (equalPrefixLength == left.length()) {
                if (equalPrefixLength != right.length()) break;
                return 0;
            }
            if (equalPrefixLength == right.length() || left.charAt(equalPrefixLength) != right.charAt(equalPrefixLength)) break;
            ++equalPrefixLength;
        }
        CollationSettings roSettings = this.settings.readOnly();
        boolean numeric = roSettings.isNumeric();
        if (equalPrefixLength > 0 && (equalPrefixLength != left.length() && this.data.isUnsafeBackward(left.charAt(equalPrefixLength), numeric) || equalPrefixLength != right.length() && this.data.isUnsafeBackward(right.charAt(equalPrefixLength), numeric))) {
            while (--equalPrefixLength > 0 && this.data.isUnsafeBackward(left.charAt(equalPrefixLength), numeric)) {
            }
        }
        if ((result = !((fastLatinOptions = roSettings.fastLatinOptions) < 0 || equalPrefixLength != left.length() && left.charAt(equalPrefixLength) > '\u017f' || equalPrefixLength != right.length() && right.charAt(equalPrefixLength) > '\u017f') ? CollationFastLatin.compareUTF16(this.data.fastLatinTable, roSettings.fastLatinPrimaries, fastLatinOptions, left, right, equalPrefixLength) : -2) == -2) {
            buffer = null;
            try {
                buffer = this.getCollationBuffer();
                if (roSettings.dontCheckFCD()) {
                    buffer.leftUTF16CollIter.setText(numeric, left, equalPrefixLength);
                    buffer.rightUTF16CollIter.setText(numeric, right, equalPrefixLength);
                    result = CollationCompare.compareUpToQuaternary(buffer.leftUTF16CollIter, buffer.rightUTF16CollIter, roSettings);
                } else {
                    buffer.leftFCDUTF16Iter.setText(numeric, left, equalPrefixLength);
                    buffer.rightFCDUTF16Iter.setText(numeric, right, equalPrefixLength);
                    result = CollationCompare.compareUpToQuaternary(buffer.leftFCDUTF16Iter, buffer.rightFCDUTF16Iter, roSettings);
                }
            }
            finally {
                this.releaseCollationBuffer(buffer);
            }
        }
        if (result != 0 || roSettings.getStrength() < 15) {
            return result;
        }
        buffer = null;
        try {
            buffer = this.getCollationBuffer();
            Normalizer2Impl nfcImpl = this.data.nfcImpl;
            if (roSettings.dontCheckFCD()) {
                buffer.leftUTF16NFDIter.setText(left, equalPrefixLength);
                buffer.rightUTF16NFDIter.setText(right, equalPrefixLength);
                int n2 = RuleBasedCollator.compareNFDIter(nfcImpl, buffer.leftUTF16NFDIter, buffer.rightUTF16NFDIter);
                return n2;
            }
            buffer.leftFCDUTF16NFDIter.setText(nfcImpl, left, equalPrefixLength);
            buffer.rightFCDUTF16NFDIter.setText(nfcImpl, right, equalPrefixLength);
            int n3 = RuleBasedCollator.compareNFDIter(nfcImpl, buffer.leftFCDUTF16NFDIter, buffer.rightFCDUTF16NFDIter);
            return n3;
        }
        finally {
            this.releaseCollationBuffer(buffer);
        }
    }

    RuleBasedCollator(CollationTailoring t2, ULocale vl) {
        this.data = t2.data;
        this.settings = t2.settings.clone();
        this.tailoring = t2;
        this.validLocale = vl;
        this.actualLocaleIsSameAsValid = false;
    }

    private void adoptTailoring(CollationTailoring t2) {
        assert (this.settings == null && this.data == null && this.tailoring == null);
        this.data = t2.data;
        this.settings = t2.settings.clone();
        this.tailoring = t2;
        this.validLocale = t2.actualLocale;
        this.actualLocaleIsSameAsValid = false;
    }

    final boolean isUnsafe(int c2) {
        return this.data.isUnsafeBackward(c2, this.settings.readOnly().isNumeric());
    }

    @Override
    public VersionInfo getVersion() {
        int version = this.tailoring.version;
        int rtVersion = VersionInfo.UCOL_RUNTIME_VERSION.getMajor();
        return VersionInfo.getInstance((version >>> 24) + (rtVersion << 4) + (rtVersion >> 4), version >> 16 & 0xFF, version >> 8 & 0xFF, version & 0xFF);
    }

    @Override
    public VersionInfo getUCAVersion() {
        VersionInfo v2 = this.getVersion();
        return VersionInfo.getInstance(v2.getMinor() >> 3, v2.getMinor() & 7, v2.getMilli() >> 6, 0);
    }

    private final CollationBuffer getCollationBuffer() {
        if (this.isFrozen()) {
            this.frozenLock.lock();
        } else if (this.collationBuffer == null) {
            this.collationBuffer = new CollationBuffer(this.data);
        }
        return this.collationBuffer;
    }

    private final void releaseCollationBuffer(CollationBuffer buffer) {
        if (this.isFrozen()) {
            this.frozenLock.unlock();
        }
    }

    @Override
    public ULocale getLocale(ULocale.Type type) {
        if (type == ULocale.ACTUAL_LOCALE) {
            return this.actualLocaleIsSameAsValid ? this.validLocale : this.tailoring.actualLocale;
        }
        if (type == ULocale.VALID_LOCALE) {
            return this.validLocale;
        }
        throw new IllegalArgumentException("unknown ULocale.Type " + type);
    }

    @Override
    void setLocale(ULocale valid, ULocale actual) {
        assert (valid == null == (actual == null));
        if (Utility.objectEquals(actual, this.tailoring.actualLocale)) {
            this.actualLocaleIsSameAsValid = false;
        } else {
            assert (Utility.objectEquals(actual, valid));
            this.actualLocaleIsSameAsValid = true;
        }
        this.validLocale = valid;
    }

    private static final class CollationBuffer {
        UTF16CollationIterator leftUTF16CollIter;
        UTF16CollationIterator rightUTF16CollIter;
        FCDUTF16CollationIterator leftFCDUTF16Iter;
        FCDUTF16CollationIterator rightFCDUTF16Iter;
        UTF16NFDIterator leftUTF16NFDIter;
        UTF16NFDIterator rightUTF16NFDIter;
        FCDUTF16NFDIterator leftFCDUTF16NFDIter;
        FCDUTF16NFDIterator rightFCDUTF16NFDIter;
        RawCollationKey rawCollationKey;

        private CollationBuffer(CollationData data) {
            this.leftUTF16CollIter = new UTF16CollationIterator(data);
            this.rightUTF16CollIter = new UTF16CollationIterator(data);
            this.leftFCDUTF16Iter = new FCDUTF16CollationIterator(data);
            this.rightFCDUTF16Iter = new FCDUTF16CollationIterator(data);
            this.leftUTF16NFDIter = new UTF16NFDIterator();
            this.rightUTF16NFDIter = new UTF16NFDIterator();
            this.leftFCDUTF16NFDIter = new FCDUTF16NFDIterator();
            this.rightFCDUTF16NFDIter = new FCDUTF16NFDIterator();
        }
    }

    private static final class FCDUTF16NFDIterator
    extends UTF16NFDIterator {
        private StringBuilder str;

        FCDUTF16NFDIterator() {
        }

        void setText(Normalizer2Impl nfcImpl, CharSequence seq, int start) {
            this.reset();
            int spanLimit = nfcImpl.makeFCD(seq, start, seq.length(), null);
            if (spanLimit == seq.length()) {
                this.s = seq;
                this.pos = start;
            } else {
                if (this.str == null) {
                    this.str = new StringBuilder();
                } else {
                    this.str.setLength(0);
                }
                this.str.append(seq, start, spanLimit);
                Normalizer2Impl.ReorderingBuffer buffer = new Normalizer2Impl.ReorderingBuffer(nfcImpl, this.str, seq.length() - start);
                nfcImpl.makeFCD(seq, spanLimit, seq.length(), buffer);
                this.s = this.str;
                this.pos = 0;
            }
        }
    }

    private static class UTF16NFDIterator
    extends NFDIterator {
        protected CharSequence s;
        protected int pos;

        UTF16NFDIterator() {
        }

        void setText(CharSequence seq, int start) {
            this.reset();
            this.s = seq;
            this.pos = start;
        }

        @Override
        protected int nextRawCodePoint() {
            if (this.pos == this.s.length()) {
                return -1;
            }
            int c2 = Character.codePointAt(this.s, this.pos);
            this.pos += Character.charCount(c2);
            return c2;
        }
    }

    private static abstract class NFDIterator {
        private String decomp;
        private int index;

        NFDIterator() {
        }

        final void reset() {
            this.index = -1;
        }

        final int nextCodePoint() {
            if (this.index >= 0) {
                if (this.index == this.decomp.length()) {
                    this.index = -1;
                } else {
                    int c2 = Character.codePointAt(this.decomp, this.index);
                    this.index += Character.charCount(c2);
                    return c2;
                }
            }
            return this.nextRawCodePoint();
        }

        final int nextDecomposedCodePoint(Normalizer2Impl nfcImpl, int c2) {
            if (this.index >= 0) {
                return c2;
            }
            this.decomp = nfcImpl.getDecomposition(c2);
            if (this.decomp == null) {
                return c2;
            }
            c2 = Character.codePointAt(this.decomp, 0);
            this.index = Character.charCount(c2);
            return c2;
        }

        protected abstract int nextRawCodePoint();
    }

    private static final class CollationKeyByteSink
    extends CollationKeys.SortKeyByteSink {
        private RawCollationKey key_;

        CollationKeyByteSink(RawCollationKey key) {
            super(key.bytes);
            this.key_ = key;
        }

        @Override
        protected void AppendBeyondCapacity(byte[] bytes, int start, int n2, int length) {
            if (this.Resize(n2, length)) {
                System.arraycopy(bytes, start, this.buffer_, length, n2);
            }
        }

        @Override
        protected boolean Resize(int appendCapacity, int length) {
            int newCapacity = 2 * this.buffer_.length;
            int altCapacity = length + 2 * appendCapacity;
            if (newCapacity < altCapacity) {
                newCapacity = altCapacity;
            }
            if (newCapacity < 200) {
                newCapacity = 200;
            }
            byte[] newBytes = new byte[newCapacity];
            System.arraycopy(this.buffer_, 0, newBytes, 0, length);
            this.key_.bytes = newBytes;
            this.buffer_ = newBytes;
            return true;
        }
    }
}

