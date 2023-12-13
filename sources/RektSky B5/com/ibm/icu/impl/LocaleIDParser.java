/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.LocaleIDs;
import com.ibm.icu.impl.locale.AsciiUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public final class LocaleIDParser {
    private char[] id;
    private int index;
    private StringBuilder buffer;
    private boolean canonicalize;
    private boolean hadCountry;
    Map<String, String> keywords;
    String baseName;
    private static final char KEYWORD_SEPARATOR = '@';
    private static final char HYPHEN = '-';
    private static final char KEYWORD_ASSIGN = '=';
    private static final char COMMA = ',';
    private static final char ITEM_SEPARATOR = ';';
    private static final char DOT = '.';
    private static final char UNDERSCORE = '_';
    private static final char DONE = '\uffff';

    public LocaleIDParser(String localeID) {
        this(localeID, false);
    }

    public LocaleIDParser(String localeID, boolean canonicalize) {
        this.id = localeID.toCharArray();
        this.index = 0;
        this.buffer = new StringBuilder(this.id.length + 5);
        this.canonicalize = canonicalize;
    }

    private void reset() {
        this.index = 0;
        this.buffer = new StringBuilder(this.id.length + 5);
    }

    private void append(char c2) {
        this.buffer.append(c2);
    }

    private void addSeparator() {
        this.append('_');
    }

    private String getString(int start) {
        return this.buffer.substring(start);
    }

    private void set(int pos, String s2) {
        this.buffer.delete(pos, this.buffer.length());
        this.buffer.insert(pos, s2);
    }

    private void append(String s2) {
        this.buffer.append(s2);
    }

    private char next() {
        if (this.index == this.id.length) {
            ++this.index;
            return '\uffff';
        }
        return this.id[this.index++];
    }

    private void skipUntilTerminatorOrIDSeparator() {
        while (!this.isTerminatorOrIDSeparator(this.next())) {
        }
        --this.index;
    }

    private boolean atTerminator() {
        return this.index >= this.id.length || this.isTerminator(this.id[this.index]);
    }

    private boolean isTerminator(char c2) {
        return c2 == '@' || c2 == '\uffff' || c2 == '.';
    }

    private boolean isTerminatorOrIDSeparator(char c2) {
        return c2 == '_' || c2 == '-' || this.isTerminator(c2);
    }

    private boolean haveExperimentalLanguagePrefix() {
        char c2;
        if (this.id.length > 2 && ((c2 = this.id[1]) == '-' || c2 == '_')) {
            c2 = this.id[0];
            return c2 == 'x' || c2 == 'X' || c2 == 'i' || c2 == 'I';
        }
        return false;
    }

    private boolean haveKeywordAssign() {
        for (int i2 = this.index; i2 < this.id.length; ++i2) {
            if (this.id[i2] != '=') continue;
            return true;
        }
        return false;
    }

    private int parseLanguage() {
        String lang;
        char c2;
        int startLength = this.buffer.length();
        if (this.haveExperimentalLanguagePrefix()) {
            this.append(AsciiUtil.toLower(this.id[0]));
            this.append('-');
            this.index = 2;
        }
        while (!this.isTerminatorOrIDSeparator(c2 = this.next())) {
            this.append(AsciiUtil.toLower(c2));
        }
        --this.index;
        if (this.buffer.length() - startLength == 3 && (lang = LocaleIDs.threeToTwoLetterLanguage(this.getString(0))) != null) {
            this.set(0, lang);
        }
        return 0;
    }

    private void skipLanguage() {
        if (this.haveExperimentalLanguagePrefix()) {
            this.index = 2;
        }
        this.skipUntilTerminatorOrIDSeparator();
    }

    private int parseScript() {
        if (!this.atTerminator()) {
            char c2;
            int oldIndex = this.index++;
            int oldBlen = this.buffer.length();
            boolean firstPass = true;
            while (!this.isTerminatorOrIDSeparator(c2 = this.next()) && AsciiUtil.isAlpha(c2)) {
                if (firstPass) {
                    this.addSeparator();
                    this.append(AsciiUtil.toUpper(c2));
                    firstPass = false;
                    continue;
                }
                this.append(AsciiUtil.toLower(c2));
            }
            --this.index;
            if (this.index - oldIndex != 5) {
                this.index = oldIndex;
                this.buffer.delete(oldBlen, this.buffer.length());
            } else {
                ++oldBlen;
            }
            return oldBlen;
        }
        return this.buffer.length();
    }

    private void skipScript() {
        if (!this.atTerminator()) {
            char c2;
            int oldIndex = this.index++;
            while (!this.isTerminatorOrIDSeparator(c2 = this.next()) && AsciiUtil.isAlpha(c2)) {
            }
            --this.index;
            if (this.index - oldIndex != 5) {
                this.index = oldIndex;
            }
        }
    }

    private int parseCountry() {
        if (!this.atTerminator()) {
            char c2;
            int oldIndex = this.index++;
            int oldBlen = this.buffer.length();
            boolean firstPass = true;
            while (!this.isTerminatorOrIDSeparator(c2 = this.next())) {
                if (firstPass) {
                    this.hadCountry = true;
                    this.addSeparator();
                    ++oldBlen;
                    firstPass = false;
                }
                this.append(AsciiUtil.toUpper(c2));
            }
            --this.index;
            int charsAppended = this.buffer.length() - oldBlen;
            if (charsAppended != 0) {
                String region;
                if (charsAppended < 2 || charsAppended > 3) {
                    this.index = oldIndex;
                    this.buffer.delete(--oldBlen, this.buffer.length());
                    this.hadCountry = false;
                } else if (charsAppended == 3 && (region = LocaleIDs.threeToTwoLetterRegion(this.getString(oldBlen))) != null) {
                    this.set(oldBlen, region);
                }
            }
            return oldBlen;
        }
        return this.buffer.length();
    }

    private void skipCountry() {
        if (!this.atTerminator()) {
            if (this.id[this.index] == '_' || this.id[this.index] == '-') {
                ++this.index;
            }
            int oldIndex = this.index;
            this.skipUntilTerminatorOrIDSeparator();
            int charsSkipped = this.index - oldIndex;
            if (charsSkipped < 2 || charsSkipped > 3) {
                this.index = oldIndex;
            }
        }
    }

    private int parseVariant() {
        char c2;
        int oldBlen = this.buffer.length();
        boolean start = true;
        boolean needSeparator = true;
        boolean skipping = false;
        boolean firstPass = true;
        while ((c2 = this.next()) != '\uffff') {
            if (c2 == '.') {
                start = false;
                skipping = true;
                continue;
            }
            if (c2 == '@') {
                if (this.haveKeywordAssign()) break;
                skipping = false;
                start = false;
                needSeparator = true;
                continue;
            }
            if (start) {
                start = false;
                if (c2 == '_' || c2 == '-') continue;
                --this.index;
                continue;
            }
            if (skipping) continue;
            if (needSeparator) {
                needSeparator = false;
                if (firstPass && !this.hadCountry) {
                    this.addSeparator();
                    ++oldBlen;
                }
                this.addSeparator();
                if (firstPass) {
                    ++oldBlen;
                    firstPass = false;
                }
            }
            if ((c2 = AsciiUtil.toUpper(c2)) == '-' || c2 == ',') {
                c2 = '_';
            }
            this.append(c2);
        }
        --this.index;
        return oldBlen;
    }

    public String getLanguage() {
        this.reset();
        return this.getString(this.parseLanguage());
    }

    public String getScript() {
        this.reset();
        this.skipLanguage();
        return this.getString(this.parseScript());
    }

    public String getCountry() {
        this.reset();
        this.skipLanguage();
        this.skipScript();
        return this.getString(this.parseCountry());
    }

    public String getVariant() {
        this.reset();
        this.skipLanguage();
        this.skipScript();
        this.skipCountry();
        return this.getString(this.parseVariant());
    }

    public String[] getLanguageScriptCountryVariant() {
        this.reset();
        return new String[]{this.getString(this.parseLanguage()), this.getString(this.parseScript()), this.getString(this.parseCountry()), this.getString(this.parseVariant())};
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public void parseBaseName() {
        if (this.baseName != null) {
            this.set(0, this.baseName);
        } else {
            this.reset();
            this.parseLanguage();
            this.parseScript();
            this.parseCountry();
            this.parseVariant();
            int len = this.buffer.length();
            if (len > 0 && this.buffer.charAt(len - 1) == '_') {
                this.buffer.deleteCharAt(len - 1);
            }
        }
    }

    public String getBaseName() {
        if (this.baseName != null) {
            return this.baseName;
        }
        this.parseBaseName();
        return this.getString(0);
    }

    public String getName() {
        this.parseBaseName();
        this.parseKeywords();
        return this.getString(0);
    }

    private boolean setToKeywordStart() {
        for (int i2 = this.index; i2 < this.id.length; ++i2) {
            if (this.id[i2] != '@') continue;
            if (this.canonicalize) {
                for (int j2 = ++i2; j2 < this.id.length; ++j2) {
                    if (this.id[j2] != '=') continue;
                    this.index = i2;
                    return true;
                }
                break;
            }
            if (++i2 >= this.id.length) break;
            this.index = i2;
            return true;
        }
        return false;
    }

    private static boolean isDoneOrKeywordAssign(char c2) {
        return c2 == '\uffff' || c2 == '=';
    }

    private static boolean isDoneOrItemSeparator(char c2) {
        return c2 == '\uffff' || c2 == ';';
    }

    private String getKeyword() {
        int start = this.index;
        while (!LocaleIDParser.isDoneOrKeywordAssign(this.next())) {
        }
        --this.index;
        return AsciiUtil.toLowerString(new String(this.id, start, this.index - start).trim());
    }

    private String getValue() {
        int start = this.index;
        while (!LocaleIDParser.isDoneOrItemSeparator(this.next())) {
        }
        --this.index;
        return new String(this.id, start, this.index - start).trim();
    }

    private Comparator<String> getKeyComparator() {
        Comparator<String> comp = new Comparator<String>(){

            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        };
        return comp;
    }

    public Map<String, String> getKeywordMap() {
        block6: {
            Map<String, String> m2;
            block7: {
                String key;
                if (this.keywords != null) break block6;
                m2 = null;
                if (!this.setToKeywordStart()) break block7;
                while ((key = this.getKeyword()).length() != 0) {
                    block9: {
                        String value;
                        block11: {
                            block10: {
                                block8: {
                                    char c2 = this.next();
                                    if (c2 == '=') break block8;
                                    if (c2 == '\uffff') {
                                        break;
                                    }
                                    break block9;
                                }
                                value = this.getValue();
                                if (value.length() == 0) break block9;
                                if (m2 != null) break block10;
                                m2 = new TreeMap(this.getKeyComparator());
                                break block11;
                            }
                            if (((TreeMap)m2).containsKey(key)) break block9;
                        }
                        ((TreeMap)m2).put(key, value);
                    }
                    if (this.next() == ';') continue;
                }
            }
            this.keywords = m2 != null ? m2 : Collections.emptyMap();
        }
        return this.keywords;
    }

    private int parseKeywords() {
        int oldBlen = this.buffer.length();
        Map<String, String> m2 = this.getKeywordMap();
        if (!m2.isEmpty()) {
            boolean first = true;
            for (Map.Entry<String, String> e2 : m2.entrySet()) {
                this.append(first ? (char)'@' : ';');
                first = false;
                this.append(e2.getKey());
                this.append('=');
                this.append(e2.getValue());
            }
            if (!first) {
                ++oldBlen;
            }
        }
        return oldBlen;
    }

    public Iterator<String> getKeywords() {
        Map<String, String> m2 = this.getKeywordMap();
        return m2.isEmpty() ? null : m2.keySet().iterator();
    }

    public String getKeywordValue(String keywordName) {
        Map<String, String> m2 = this.getKeywordMap();
        return m2.isEmpty() ? null : m2.get(AsciiUtil.toLowerString(keywordName.trim()));
    }

    public void defaultKeywordValue(String keywordName, String value) {
        this.setKeywordValue(keywordName, value, false);
    }

    public void setKeywordValue(String keywordName, String value) {
        this.setKeywordValue(keywordName, value, true);
    }

    private void setKeywordValue(String keywordName, String value, boolean reset) {
        if (keywordName == null) {
            if (reset) {
                this.keywords = Collections.emptyMap();
            }
        } else {
            if ((keywordName = AsciiUtil.toLowerString(keywordName.trim())).length() == 0) {
                throw new IllegalArgumentException("keyword must not be empty");
            }
            if (value != null && (value = value.trim()).length() == 0) {
                throw new IllegalArgumentException("value must not be empty");
            }
            Map<String, String> m2 = this.getKeywordMap();
            if (m2.isEmpty()) {
                if (value != null) {
                    this.keywords = new TreeMap<String, String>(this.getKeyComparator());
                    this.keywords.put(keywordName, value.trim());
                }
            } else if (reset || !m2.containsKey(keywordName)) {
                if (value != null) {
                    m2.put(keywordName, value);
                } else {
                    m2.remove(keywordName);
                    if (m2.isEmpty()) {
                        this.keywords = Collections.emptyMap();
                    }
                }
            }
        }
    }
}

