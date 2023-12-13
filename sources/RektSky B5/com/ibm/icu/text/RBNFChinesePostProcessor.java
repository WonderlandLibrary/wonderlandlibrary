/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.NFRuleSet;
import com.ibm.icu.text.RBNFPostProcessor;
import com.ibm.icu.text.RuleBasedNumberFormat;

final class RBNFChinesePostProcessor
implements RBNFPostProcessor {
    private boolean longForm;
    private int format;
    private static final String[] rulesetNames = new String[]{"%traditional", "%simplified", "%accounting", "%time"};

    RBNFChinesePostProcessor() {
    }

    @Override
    public void init(RuleBasedNumberFormat formatter, String rules) {
    }

    @Override
    public void process(StringBuilder buf, NFRuleSet ruleSet) {
        int n2;
        int i2;
        String name = ruleSet.getName();
        for (i2 = 0; i2 < rulesetNames.length; ++i2) {
            if (!rulesetNames[i2].equals(name)) continue;
            this.format = i2;
            this.longForm = i2 == 1 || i2 == 3;
            break;
        }
        if (this.longForm) {
            i2 = buf.indexOf("*");
            while (i2 != -1) {
                buf.delete(i2, i2 + 1);
                i2 = buf.indexOf("*", i2);
            }
            return;
        }
        String DIAN = "\u9ede";
        String[][] markers = new String[][]{{"\u842c", "\u5104", "\u5146", "\u3007"}, {"\u4e07", "\u4ebf", "\u5146", "\u3007"}, {"\u842c", "\u5104", "\u5146", "\u96f6"}};
        String[] m2 = markers[this.format];
        for (int i3 = 0; i3 < m2.length - 1; ++i3) {
            n2 = buf.indexOf(m2[i3]);
            if (n2 == -1) continue;
            buf.insert(n2 + m2[i3].length(), '|');
        }
        int x2 = buf.indexOf("\u9ede");
        if (x2 == -1) {
            x2 = buf.length();
        }
        int s2 = 0;
        n2 = -1;
        String ling = markers[this.format][3];
        block14: while (x2 >= 0) {
            int m3 = buf.lastIndexOf("|", x2);
            int nn = buf.lastIndexOf(ling, x2);
            int ns = 0;
            if (nn > m3) {
                ns = nn > 0 && buf.charAt(nn - 1) != '*' ? 2 : 1;
            }
            x2 = m3 - 1;
            switch (s2 * 3 + ns) {
                case 0: {
                    s2 = ns;
                    n2 = -1;
                    continue block14;
                }
                case 1: {
                    s2 = ns;
                    n2 = nn;
                    continue block14;
                }
                case 2: {
                    s2 = ns;
                    n2 = -1;
                    continue block14;
                }
                case 3: {
                    s2 = ns;
                    n2 = -1;
                    continue block14;
                }
                case 4: {
                    buf.delete(nn - 1, nn + ling.length());
                    s2 = 0;
                    n2 = -1;
                    continue block14;
                }
                case 5: {
                    buf.delete(n2 - 1, n2 + ling.length());
                    s2 = ns;
                    n2 = -1;
                    continue block14;
                }
                case 6: {
                    s2 = ns;
                    n2 = -1;
                    continue block14;
                }
                case 7: {
                    buf.delete(nn - 1, nn + ling.length());
                    s2 = 0;
                    n2 = -1;
                    continue block14;
                }
                case 8: {
                    s2 = ns;
                    n2 = -1;
                    continue block14;
                }
            }
            throw new IllegalStateException();
        }
        int i4 = buf.length();
        while (--i4 >= 0) {
            char c2 = buf.charAt(i4);
            if (c2 != '*' && c2 != '|') continue;
            buf.delete(i4, i4 + 1);
        }
    }
}

