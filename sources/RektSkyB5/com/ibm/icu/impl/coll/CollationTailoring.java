/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.Trie2_32;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationSettings;
import com.ibm.icu.impl.coll.SharedObject;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.VersionInfo;
import java.util.Map;

public final class CollationTailoring {
    public CollationData data;
    public SharedObject.Reference<CollationSettings> settings;
    private String rules;
    private UResourceBundle rulesResource;
    public ULocale actualLocale = ULocale.ROOT;
    public int version = 0;
    CollationData ownedData;
    Trie2_32 trie;
    UnicodeSet unsafeBackwardSet;
    public Map<Integer, Integer> maxExpansions;

    CollationTailoring(SharedObject.Reference<CollationSettings> baseSettings) {
        if (baseSettings != null) {
            assert (baseSettings.readOnly().reorderCodes.length == 0);
            assert (baseSettings.readOnly().reorderTable == null);
            assert (baseSettings.readOnly().minHighNoReorder == 0L);
            this.settings = baseSettings.clone();
        } else {
            this.settings = new SharedObject.Reference<CollationSettings>(new CollationSettings());
        }
    }

    void ensureOwnedData() {
        if (this.ownedData == null) {
            Normalizer2Impl nfcImpl = Norm2AllModes.getNFCInstance().impl;
            this.ownedData = new CollationData(nfcImpl);
        }
        this.data = this.ownedData;
    }

    void setRules(String r2) {
        assert (this.rules == null && this.rulesResource == null);
        this.rules = r2;
    }

    void setRulesResource(UResourceBundle res) {
        assert (this.rules == null && this.rulesResource == null);
        this.rulesResource = res;
    }

    public String getRules() {
        if (this.rules != null) {
            return this.rules;
        }
        if (this.rulesResource != null) {
            return this.rulesResource.getString();
        }
        return "";
    }

    static VersionInfo makeBaseVersion(VersionInfo ucaVersion) {
        return VersionInfo.getInstance(VersionInfo.UCOL_BUILDER_VERSION.getMajor(), (ucaVersion.getMajor() << 3) + ucaVersion.getMinor(), ucaVersion.getMilli() << 6, 0);
    }

    void setVersion(int baseVersion, int rulesVersion) {
        int r2 = rulesVersion >> 16 & 0xFF00;
        int s2 = rulesVersion >> 16 & 0xFF;
        int t2 = rulesVersion >> 8 & 0xFF;
        int q2 = rulesVersion & 0xFF;
        this.version = VersionInfo.UCOL_BUILDER_VERSION.getMajor() << 24 | baseVersion & 0xFFC000 | r2 + (r2 >> 6) & 0x3F00 | (s2 << 3) + (s2 >> 5) + t2 + (q2 << 4) + (q2 >> 4) & 0xFF;
    }

    int getUCAVersion() {
        return this.version >> 12 & 0xFF0 | this.version >> 14 & 3;
    }
}

