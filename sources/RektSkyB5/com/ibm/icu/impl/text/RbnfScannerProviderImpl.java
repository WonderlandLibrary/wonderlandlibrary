/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.text.CollationElementIterator;
import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RbnfLenientScanner;
import com.ibm.icu.text.RbnfLenientScannerProvider;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.util.ULocale;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class RbnfScannerProviderImpl
implements RbnfLenientScannerProvider {
    private static final boolean DEBUG = ICUDebug.enabled("rbnf");
    private Map<String, RbnfLenientScanner> cache = new HashMap<String, RbnfLenientScanner>();

    @Deprecated
    public RbnfScannerProviderImpl() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @Deprecated
    public RbnfLenientScanner get(ULocale locale, String extras) {
        RbnfLenientScanner result = null;
        String key = locale.toString() + "/" + extras;
        Map<String, RbnfLenientScanner> map = this.cache;
        synchronized (map) {
            result = this.cache.get(key);
            if (result != null) {
                return result;
            }
        }
        result = this.createScanner(locale, extras);
        map = this.cache;
        synchronized (map) {
            this.cache.put(key, result);
        }
        return result;
    }

    @Deprecated
    protected RbnfLenientScanner createScanner(ULocale locale, String extras) {
        RuleBasedCollator collator = null;
        try {
            collator = (RuleBasedCollator)Collator.getInstance(locale.toLocale());
            if (extras != null) {
                String rules = collator.getRules() + extras;
                collator = new RuleBasedCollator(rules);
            }
            collator.setDecomposition(17);
        }
        catch (Exception e2) {
            if (DEBUG) {
                e2.printStackTrace();
                System.out.println("++++");
            }
            collator = null;
        }
        return new RbnfLenientScannerImpl(collator);
    }

    private static class RbnfLenientScannerImpl
    implements RbnfLenientScanner {
        private final RuleBasedCollator collator;

        private RbnfLenientScannerImpl(RuleBasedCollator rbc) {
            this.collator = rbc;
        }

        @Override
        public boolean allIgnorable(String s2) {
            CollationElementIterator iter = this.collator.getCollationElementIterator(s2);
            int o2 = iter.next();
            while (o2 != -1 && CollationElementIterator.primaryOrder(o2) == 0) {
                o2 = iter.next();
            }
            return o2 == -1;
        }

        @Override
        public int[] findText(String str, String key, int startingAt) {
            int keyLen = 0;
            for (int p2 = startingAt; p2 < str.length() && keyLen == 0; ++p2) {
                keyLen = this.prefixLength(str.substring(p2), key);
                if (keyLen == 0) continue;
                return new int[]{p2, keyLen};
            }
            return new int[]{-1, 0};
        }

        public int[] findText2(String str, String key, int startingAt) {
            CollationElementIterator strIter = this.collator.getCollationElementIterator(str);
            CollationElementIterator keyIter = this.collator.getCollationElementIterator(key);
            int keyStart = -1;
            strIter.setOffset(startingAt);
            int oStr = strIter.next();
            int oKey = keyIter.next();
            while (oKey != -1) {
                while (oStr != -1 && CollationElementIterator.primaryOrder(oStr) == 0) {
                    oStr = strIter.next();
                }
                while (oKey != -1 && CollationElementIterator.primaryOrder(oKey) == 0) {
                    oKey = keyIter.next();
                }
                if (oStr == -1) {
                    return new int[]{-1, 0};
                }
                if (oKey == -1) break;
                if (CollationElementIterator.primaryOrder(oStr) == CollationElementIterator.primaryOrder(oKey)) {
                    keyStart = strIter.getOffset();
                    oStr = strIter.next();
                    oKey = keyIter.next();
                    continue;
                }
                if (keyStart != -1) {
                    keyStart = -1;
                    keyIter.reset();
                    continue;
                }
                oStr = strIter.next();
            }
            return new int[]{keyStart, strIter.getOffset() - keyStart};
        }

        @Override
        public int prefixLength(String str, String prefix) {
            CollationElementIterator strIter = this.collator.getCollationElementIterator(str);
            CollationElementIterator prefixIter = this.collator.getCollationElementIterator(prefix);
            int oStr = strIter.next();
            int oPrefix = prefixIter.next();
            while (oPrefix != -1) {
                while (CollationElementIterator.primaryOrder(oStr) == 0 && oStr != -1) {
                    oStr = strIter.next();
                }
                while (CollationElementIterator.primaryOrder(oPrefix) == 0 && oPrefix != -1) {
                    oPrefix = prefixIter.next();
                }
                if (oPrefix == -1) break;
                if (oStr == -1) {
                    return 0;
                }
                if (CollationElementIterator.primaryOrder(oStr) != CollationElementIterator.primaryOrder(oPrefix)) {
                    return 0;
                }
                oStr = strIter.next();
                oPrefix = prefixIter.next();
            }
            int result = strIter.getOffset();
            if (oStr != -1) {
                --result;
            }
            return result;
        }
    }
}

