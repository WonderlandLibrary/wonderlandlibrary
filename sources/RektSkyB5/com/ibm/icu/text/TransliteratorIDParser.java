/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.CaseInsensitiveString;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TransliteratorIDParser {
    private static final char ID_DELIM = ';';
    private static final char TARGET_SEP = '-';
    private static final char VARIANT_SEP = '/';
    private static final char OPEN_REV = '(';
    private static final char CLOSE_REV = ')';
    private static final String ANY = "Any";
    private static final int FORWARD = 0;
    private static final int REVERSE = 1;
    private static final Map<CaseInsensitiveString, String> SPECIAL_INVERSES = Collections.synchronizedMap(new HashMap());

    TransliteratorIDParser() {
    }

    public static SingleID parseFilterID(String id, int[] pos) {
        int start = pos[0];
        Specs specs = TransliteratorIDParser.parseFilterID(id, pos, true);
        if (specs == null) {
            pos[0] = start;
            return null;
        }
        SingleID single = TransliteratorIDParser.specsToID(specs, 0);
        single.filter = specs.filter;
        return single;
    }

    public static SingleID parseSingleID(String id, int[] pos, int dir) {
        SingleID single;
        int start = pos[0];
        Specs specsA = null;
        Specs specsB = null;
        boolean sawParen = false;
        for (int pass = 1; pass <= 2; ++pass) {
            if (pass == 2 && (specsA = TransliteratorIDParser.parseFilterID(id, pos, true)) == null) {
                pos[0] = start;
                return null;
            }
            if (!Utility.parseChar(id, pos, '(')) continue;
            sawParen = true;
            if (Utility.parseChar(id, pos, ')') || (specsB = TransliteratorIDParser.parseFilterID(id, pos, true)) != null && Utility.parseChar(id, pos, ')')) break;
            pos[0] = start;
            return null;
        }
        if (sawParen) {
            if (dir == 0) {
                single = TransliteratorIDParser.specsToID(specsA, 0);
                single.canonID = single.canonID + '(' + TransliteratorIDParser.specsToID((Specs)specsB, (int)0).canonID + ')';
                if (specsA != null) {
                    single.filter = specsA.filter;
                }
            } else {
                single = TransliteratorIDParser.specsToID(specsB, 0);
                single.canonID = single.canonID + '(' + TransliteratorIDParser.specsToID((Specs)specsA, (int)0).canonID + ')';
                if (specsB != null) {
                    single.filter = specsB.filter;
                }
            }
        } else {
            if (dir == 0) {
                single = TransliteratorIDParser.specsToID(specsA, 0);
            } else {
                single = TransliteratorIDParser.specsToSpecialInverse(specsA);
                if (single == null) {
                    single = TransliteratorIDParser.specsToID(specsA, 1);
                }
            }
            single.filter = specsA.filter;
        }
        return single;
    }

    public static UnicodeSet parseGlobalFilter(String id, int[] pos, int dir, int[] withParens, StringBuffer canonID) {
        UnicodeSet filter = null;
        int start = pos[0];
        if (withParens[0] == -1) {
            withParens[0] = Utility.parseChar(id, pos, '(') ? 1 : 0;
        } else if (withParens[0] == 1 && !Utility.parseChar(id, pos, '(')) {
            pos[0] = start;
            return null;
        }
        pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
        if (UnicodeSet.resemblesPattern(id, pos[0])) {
            ParsePosition ppos = new ParsePosition(pos[0]);
            try {
                filter = new UnicodeSet(id, ppos, null);
            }
            catch (IllegalArgumentException e2) {
                pos[0] = start;
                return null;
            }
            String pattern = id.substring(pos[0], ppos.getIndex());
            pos[0] = ppos.getIndex();
            if (withParens[0] == 1 && !Utility.parseChar(id, pos, ')')) {
                pos[0] = start;
                return null;
            }
            if (canonID != null) {
                if (dir == 0) {
                    if (withParens[0] == 1) {
                        pattern = String.valueOf('(') + pattern + ')';
                    }
                    canonID.append(pattern + ';');
                } else {
                    if (withParens[0] == 0) {
                        pattern = String.valueOf('(') + pattern + ')';
                    }
                    canonID.insert(0, pattern + ';');
                }
            }
        }
        return filter;
    }

    public static boolean parseCompoundID(String id, int dir, StringBuffer canonID, List<SingleID> list, UnicodeSet[] globalFilter) {
        SingleID single;
        int[] pos = new int[]{0};
        int[] withParens = new int[1];
        list.clear();
        globalFilter[0] = null;
        canonID.setLength(0);
        withParens[0] = 0;
        UnicodeSet filter = TransliteratorIDParser.parseGlobalFilter(id, pos, dir, withParens, canonID);
        if (filter != null) {
            if (!Utility.parseChar(id, pos, ';')) {
                canonID.setLength(0);
                pos[0] = 0;
            }
            if (dir == 0) {
                globalFilter[0] = filter;
            }
        }
        boolean sawDelimiter = true;
        while ((single = TransliteratorIDParser.parseSingleID(id, pos, dir)) != null) {
            if (dir == 0) {
                list.add(single);
            } else {
                list.add(0, single);
            }
            if (Utility.parseChar(id, pos, ';')) continue;
            sawDelimiter = false;
            break;
        }
        if (list.size() == 0) {
            return false;
        }
        for (int i2 = 0; i2 < list.size(); ++i2) {
            SingleID single2 = list.get(i2);
            canonID.append(single2.canonID);
            if (i2 == list.size() - 1) continue;
            canonID.append(';');
        }
        if (sawDelimiter) {
            withParens[0] = 1;
            filter = TransliteratorIDParser.parseGlobalFilter(id, pos, dir, withParens, canonID);
            if (filter != null) {
                Utility.parseChar(id, pos, ';');
                if (dir == 1) {
                    globalFilter[0] = filter;
                }
            }
        }
        pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
        return pos[0] == id.length();
    }

    static List<Transliterator> instantiateList(List<SingleID> ids) {
        Transliterator t2;
        ArrayList<Transliterator> translits = new ArrayList<Transliterator>();
        for (SingleID single : ids) {
            if (single.basicID.length() == 0) continue;
            t2 = single.getInstance();
            if (t2 == null) {
                throw new IllegalArgumentException("Illegal ID " + single.canonID);
            }
            translits.add(t2);
        }
        if (translits.size() == 0) {
            t2 = Transliterator.getBasicInstance("Any-Null", null);
            if (t2 == null) {
                throw new IllegalArgumentException("Internal error; cannot instantiate Any-Null");
            }
            translits.add(t2);
        }
        return translits;
    }

    public static String[] IDtoSTV(String id) {
        String source = ANY;
        String target = null;
        String variant = "";
        int sep = id.indexOf(45);
        int var2 = id.indexOf(47);
        if (var2 < 0) {
            var2 = id.length();
        }
        boolean isSourcePresent = false;
        if (sep < 0) {
            target = id.substring(0, var2);
            variant = id.substring(var2);
        } else if (sep < var2) {
            if (sep > 0) {
                source = id.substring(0, sep);
                isSourcePresent = true;
            }
            target = id.substring(++sep, var2);
            variant = id.substring(var2);
        } else {
            if (var2 > 0) {
                source = id.substring(0, var2);
                isSourcePresent = true;
            }
            variant = id.substring(var2, sep++);
            target = id.substring(sep);
        }
        if (variant.length() > 0) {
            variant = variant.substring(1);
        }
        return new String[]{source, target, variant, isSourcePresent ? "" : null};
    }

    public static String STVtoID(String source, String target, String variant) {
        StringBuilder id = new StringBuilder(source);
        if (id.length() == 0) {
            id.append(ANY);
        }
        id.append('-').append(target);
        if (variant != null && variant.length() != 0) {
            id.append('/').append(variant);
        }
        return id.toString();
    }

    public static void registerSpecialInverse(String target, String inverseTarget, boolean bidirectional) {
        SPECIAL_INVERSES.put(new CaseInsensitiveString(target), inverseTarget);
        if (bidirectional && !target.equalsIgnoreCase(inverseTarget)) {
            SPECIAL_INVERSES.put(new CaseInsensitiveString(inverseTarget), target);
        }
    }

    private static Specs parseFilterID(String id, int[] pos, boolean allowFilter) {
        String first = null;
        String source = null;
        String target = null;
        String variant = null;
        String filter = null;
        int delimiter = 0;
        int specCount = 0;
        int start = pos[0];
        while (true) {
            String spec;
            char c2;
            pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
            if (pos[0] == id.length()) break;
            if (allowFilter && filter == null && UnicodeSet.resemblesPattern(id, pos[0])) {
                ParsePosition ppos = new ParsePosition(pos[0]);
                new UnicodeSet(id, ppos, null);
                filter = id.substring(pos[0], ppos.getIndex());
                pos[0] = ppos.getIndex();
                continue;
            }
            if (delimiter == 0 && ((c2 = id.charAt(pos[0])) == '-' && target == null || c2 == '/' && variant == null)) {
                delimiter = c2;
                pos[0] = pos[0] + 1;
                continue;
            }
            if (delimiter == 0 && specCount > 0 || (spec = Utility.parseUnicodeIdentifier(id, pos)) == null) break;
            switch (delimiter) {
                case 0: {
                    first = spec;
                    break;
                }
                case 45: {
                    target = spec;
                    break;
                }
                case 47: {
                    variant = spec;
                }
            }
            ++specCount;
            delimiter = 0;
        }
        if (first != null) {
            if (target == null) {
                target = first;
            } else {
                source = first;
            }
        }
        if (source == null && target == null) {
            pos[0] = start;
            return null;
        }
        boolean sawSource = true;
        if (source == null) {
            source = ANY;
            sawSource = false;
        }
        if (target == null) {
            target = ANY;
        }
        return new Specs(source, target, variant, sawSource, filter);
    }

    private static SingleID specsToID(Specs specs, int dir) {
        String canonID = "";
        String basicID = "";
        String basicPrefix = "";
        if (specs != null) {
            StringBuilder buf = new StringBuilder();
            if (dir == 0) {
                if (specs.sawSource) {
                    buf.append(specs.source).append('-');
                } else {
                    basicPrefix = specs.source + '-';
                }
                buf.append(specs.target);
            } else {
                buf.append(specs.target).append('-').append(specs.source);
            }
            if (specs.variant != null) {
                buf.append('/').append(specs.variant);
            }
            basicID = basicPrefix + buf.toString();
            if (specs.filter != null) {
                buf.insert(0, specs.filter);
            }
            canonID = buf.toString();
        }
        return new SingleID(canonID, basicID);
    }

    private static SingleID specsToSpecialInverse(Specs specs) {
        if (!specs.source.equalsIgnoreCase(ANY)) {
            return null;
        }
        String inverseTarget = SPECIAL_INVERSES.get(new CaseInsensitiveString(specs.target));
        if (inverseTarget != null) {
            StringBuilder buf = new StringBuilder();
            if (specs.filter != null) {
                buf.append(specs.filter);
            }
            if (specs.sawSource) {
                buf.append(ANY).append('-');
            }
            buf.append(inverseTarget);
            String basicID = "Any-" + inverseTarget;
            if (specs.variant != null) {
                buf.append('/').append(specs.variant);
                basicID = basicID + '/' + specs.variant;
            }
            return new SingleID(buf.toString(), basicID);
        }
        return null;
    }

    static class SingleID {
        public String canonID;
        public String basicID;
        public String filter;

        SingleID(String c2, String b2, String f2) {
            this.canonID = c2;
            this.basicID = b2;
            this.filter = f2;
        }

        SingleID(String c2, String b2) {
            this(c2, b2, null);
        }

        Transliterator getInstance() {
            Transliterator t2 = this.basicID == null || this.basicID.length() == 0 ? Transliterator.getBasicInstance("Any-Null", this.canonID) : Transliterator.getBasicInstance(this.basicID, this.canonID);
            if (t2 != null && this.filter != null) {
                t2.setFilter(new UnicodeSet(this.filter));
            }
            return t2;
        }
    }

    private static class Specs {
        public String source;
        public String target;
        public String variant;
        public String filter;
        public boolean sawSource;

        Specs(String s2, String t2, String v2, boolean sawS, String f2) {
            this.source = s2;
            this.target = t2;
            this.variant = v2;
            this.sawSource = sawS;
            this.filter = f2;
        }
    }
}

