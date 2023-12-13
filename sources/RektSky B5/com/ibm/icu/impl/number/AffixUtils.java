/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.NumberStringBuilder;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.UnicodeSet;

public class AffixUtils {
    private static final int STATE_BASE = 0;
    private static final int STATE_FIRST_QUOTE = 1;
    private static final int STATE_INSIDE_QUOTE = 2;
    private static final int STATE_AFTER_QUOTE = 3;
    private static final int STATE_FIRST_CURR = 4;
    private static final int STATE_SECOND_CURR = 5;
    private static final int STATE_THIRD_CURR = 6;
    private static final int STATE_FOURTH_CURR = 7;
    private static final int STATE_FIFTH_CURR = 8;
    private static final int STATE_OVERFLOW_CURR = 9;
    private static final int TYPE_CODEPOINT = 0;
    public static final int TYPE_MINUS_SIGN = -1;
    public static final int TYPE_PLUS_SIGN = -2;
    public static final int TYPE_PERCENT = -3;
    public static final int TYPE_PERMILLE = -4;
    public static final int TYPE_CURRENCY_SINGLE = -5;
    public static final int TYPE_CURRENCY_DOUBLE = -6;
    public static final int TYPE_CURRENCY_TRIPLE = -7;
    public static final int TYPE_CURRENCY_QUAD = -8;
    public static final int TYPE_CURRENCY_QUINT = -9;
    public static final int TYPE_CURRENCY_OVERFLOW = -15;

    public static int estimateLength(CharSequence patternString) {
        int cp;
        if (patternString == null) {
            return 0;
        }
        int state = 0;
        int length = 0;
        block9: for (int offset = 0; offset < patternString.length(); offset += Character.charCount(cp)) {
            cp = Character.codePointAt(patternString, offset);
            switch (state) {
                case 0: {
                    if (cp == 39) {
                        state = 1;
                        continue block9;
                    }
                    ++length;
                    continue block9;
                }
                case 1: {
                    if (cp == 39) {
                        ++length;
                        state = 0;
                        continue block9;
                    }
                    ++length;
                    state = 2;
                    continue block9;
                }
                case 2: {
                    if (cp == 39) {
                        state = 3;
                        continue block9;
                    }
                    ++length;
                    continue block9;
                }
                case 3: {
                    if (cp == 39) {
                        ++length;
                        state = 2;
                        continue block9;
                    }
                    ++length;
                    continue block9;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        switch (state) {
            case 1: 
            case 2: {
                throw new IllegalArgumentException("Unterminated quote: \"" + patternString + "\"");
            }
        }
        return length;
    }

    public static int escape(CharSequence input, StringBuilder output) {
        int cp;
        if (input == null) {
            return 0;
        }
        int state = 0;
        int startLength = output.length();
        block4: for (int offset = 0; offset < input.length(); offset += Character.charCount(cp)) {
            cp = Character.codePointAt(input, offset);
            switch (cp) {
                case 39: {
                    output.append("''");
                    continue block4;
                }
                case 37: 
                case 43: 
                case 45: 
                case 164: 
                case 8240: {
                    if (state == 0) {
                        output.append('\'');
                        output.appendCodePoint(cp);
                        state = 2;
                        continue block4;
                    }
                    output.appendCodePoint(cp);
                    continue block4;
                }
                default: {
                    if (state == 2) {
                        output.append('\'');
                        output.appendCodePoint(cp);
                        state = 0;
                        continue block4;
                    }
                    output.appendCodePoint(cp);
                }
            }
        }
        if (state == 2) {
            output.append('\'');
        }
        return output.length() - startLength;
    }

    public static String escape(CharSequence input) {
        if (input == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        AffixUtils.escape(input, sb);
        return sb.toString();
    }

    public static final NumberFormat.Field getFieldForType(int type) {
        switch (type) {
            case -1: {
                return NumberFormat.Field.SIGN;
            }
            case -2: {
                return NumberFormat.Field.SIGN;
            }
            case -3: {
                return NumberFormat.Field.PERCENT;
            }
            case -4: {
                return NumberFormat.Field.PERMILLE;
            }
            case -5: {
                return NumberFormat.Field.CURRENCY;
            }
            case -6: {
                return NumberFormat.Field.CURRENCY;
            }
            case -7: {
                return NumberFormat.Field.CURRENCY;
            }
            case -8: {
                return NumberFormat.Field.CURRENCY;
            }
            case -9: {
                return NumberFormat.Field.CURRENCY;
            }
            case -15: {
                return NumberFormat.Field.CURRENCY;
            }
        }
        throw new AssertionError();
    }

    public static int unescape(CharSequence affixPattern, NumberStringBuilder output, int position, SymbolProvider provider) {
        assert (affixPattern != null);
        int length = 0;
        long tag = 0L;
        while (AffixUtils.hasNext(tag, affixPattern)) {
            int typeOrCp = AffixUtils.getTypeOrCp(tag = AffixUtils.nextToken(tag, affixPattern));
            if (typeOrCp == -15) {
                length += output.insertCodePoint(position + length, 65533, NumberFormat.Field.CURRENCY);
                continue;
            }
            if (typeOrCp < 0) {
                length += output.insert(position + length, provider.getSymbol(typeOrCp), AffixUtils.getFieldForType(typeOrCp));
                continue;
            }
            length += output.insertCodePoint(position + length, typeOrCp, null);
        }
        return length;
    }

    public static int unescapedCount(CharSequence affixPattern, boolean lengthOrCount, SymbolProvider provider) {
        int length = 0;
        long tag = 0L;
        while (AffixUtils.hasNext(tag, affixPattern)) {
            int typeOrCp = AffixUtils.getTypeOrCp(tag = AffixUtils.nextToken(tag, affixPattern));
            if (typeOrCp == -15) {
                ++length;
                continue;
            }
            if (typeOrCp < 0) {
                CharSequence symbol = provider.getSymbol(typeOrCp);
                length += lengthOrCount ? symbol.length() : Character.codePointCount(symbol, 0, symbol.length());
                continue;
            }
            length += lengthOrCount ? Character.charCount(typeOrCp) : 1;
        }
        return length;
    }

    public static boolean containsType(CharSequence affixPattern, int type) {
        if (affixPattern == null || affixPattern.length() == 0) {
            return false;
        }
        long tag = 0L;
        while (AffixUtils.hasNext(tag, affixPattern)) {
            if (AffixUtils.getTypeOrCp(tag = AffixUtils.nextToken(tag, affixPattern)) != type) continue;
            return true;
        }
        return false;
    }

    public static boolean hasCurrencySymbols(CharSequence affixPattern) {
        if (affixPattern == null || affixPattern.length() == 0) {
            return false;
        }
        long tag = 0L;
        while (AffixUtils.hasNext(tag, affixPattern)) {
            int typeOrCp = AffixUtils.getTypeOrCp(tag = AffixUtils.nextToken(tag, affixPattern));
            if (typeOrCp >= 0 || AffixUtils.getFieldForType(typeOrCp) != NumberFormat.Field.CURRENCY) continue;
            return true;
        }
        return false;
    }

    public static String replaceType(CharSequence affixPattern, int type, char replacementChar) {
        if (affixPattern == null || affixPattern.length() == 0) {
            return "";
        }
        char[] chars = affixPattern.toString().toCharArray();
        long tag = 0L;
        while (AffixUtils.hasNext(tag, affixPattern)) {
            if (AffixUtils.getTypeOrCp(tag = AffixUtils.nextToken(tag, affixPattern)) != type) continue;
            int offset = AffixUtils.getOffset(tag);
            chars[offset - 1] = replacementChar;
        }
        return new String(chars);
    }

    public static boolean containsOnlySymbolsAndIgnorables(CharSequence affixPattern, UnicodeSet ignorables) {
        if (affixPattern == null) {
            return true;
        }
        long tag = 0L;
        while (AffixUtils.hasNext(tag, affixPattern)) {
            int typeOrCp = AffixUtils.getTypeOrCp(tag = AffixUtils.nextToken(tag, affixPattern));
            if (typeOrCp < 0 || ignorables.contains(typeOrCp)) continue;
            return false;
        }
        return true;
    }

    public static void iterateWithConsumer(CharSequence affixPattern, TokenConsumer consumer) {
        assert (affixPattern != null);
        long tag = 0L;
        while (AffixUtils.hasNext(tag, affixPattern)) {
            tag = AffixUtils.nextToken(tag, affixPattern);
            int typeOrCp = AffixUtils.getTypeOrCp(tag);
            consumer.consumeToken(typeOrCp);
        }
    }

    private static long nextToken(long tag, CharSequence patternString) {
        int offset = AffixUtils.getOffset(tag);
        int state = AffixUtils.getState(tag);
        block31: while (offset < patternString.length()) {
            int cp = Character.codePointAt(patternString, offset);
            int count = Character.charCount(cp);
            switch (state) {
                case 0: {
                    switch (cp) {
                        case 39: {
                            state = 1;
                            offset += count;
                            continue block31;
                        }
                        case 45: {
                            return AffixUtils.makeTag(offset + count, -1, 0, 0);
                        }
                        case 43: {
                            return AffixUtils.makeTag(offset + count, -2, 0, 0);
                        }
                        case 37: {
                            return AffixUtils.makeTag(offset + count, -3, 0, 0);
                        }
                        case 8240: {
                            return AffixUtils.makeTag(offset + count, -4, 0, 0);
                        }
                        case 164: {
                            state = 4;
                            offset += count;
                            continue block31;
                        }
                    }
                    return AffixUtils.makeTag(offset + count, 0, 0, cp);
                }
                case 1: {
                    if (cp == 39) {
                        return AffixUtils.makeTag(offset + count, 0, 0, cp);
                    }
                    return AffixUtils.makeTag(offset + count, 0, 2, cp);
                }
                case 2: {
                    if (cp == 39) {
                        state = 3;
                        offset += count;
                        continue block31;
                    }
                    return AffixUtils.makeTag(offset + count, 0, 2, cp);
                }
                case 3: {
                    if (cp == 39) {
                        return AffixUtils.makeTag(offset + count, 0, 2, cp);
                    }
                    state = 0;
                    continue block31;
                }
                case 4: {
                    if (cp == 164) {
                        state = 5;
                        offset += count;
                        continue block31;
                    }
                    return AffixUtils.makeTag(offset, -5, 0, 0);
                }
                case 5: {
                    if (cp == 164) {
                        state = 6;
                        offset += count;
                        continue block31;
                    }
                    return AffixUtils.makeTag(offset, -6, 0, 0);
                }
                case 6: {
                    if (cp == 164) {
                        state = 7;
                        offset += count;
                        continue block31;
                    }
                    return AffixUtils.makeTag(offset, -7, 0, 0);
                }
                case 7: {
                    if (cp == 164) {
                        state = 8;
                        offset += count;
                        continue block31;
                    }
                    return AffixUtils.makeTag(offset, -8, 0, 0);
                }
                case 8: {
                    if (cp == 164) {
                        state = 9;
                        offset += count;
                        continue block31;
                    }
                    return AffixUtils.makeTag(offset, -9, 0, 0);
                }
                case 9: {
                    if (cp == 164) {
                        offset += count;
                        continue block31;
                    }
                    return AffixUtils.makeTag(offset, -15, 0, 0);
                }
            }
            throw new AssertionError();
        }
        switch (state) {
            case 0: {
                return -1L;
            }
            case 1: 
            case 2: {
                throw new IllegalArgumentException("Unterminated quote in pattern affix: \"" + patternString + "\"");
            }
            case 3: {
                return -1L;
            }
            case 4: {
                return AffixUtils.makeTag(offset, -5, 0, 0);
            }
            case 5: {
                return AffixUtils.makeTag(offset, -6, 0, 0);
            }
            case 6: {
                return AffixUtils.makeTag(offset, -7, 0, 0);
            }
            case 7: {
                return AffixUtils.makeTag(offset, -8, 0, 0);
            }
            case 8: {
                return AffixUtils.makeTag(offset, -9, 0, 0);
            }
            case 9: {
                return AffixUtils.makeTag(offset, -15, 0, 0);
            }
        }
        throw new AssertionError();
    }

    private static boolean hasNext(long tag, CharSequence string) {
        assert (tag >= 0L);
        int state = AffixUtils.getState(tag);
        int offset = AffixUtils.getOffset(tag);
        if (state == 2 && offset == string.length() - 1 && string.charAt(offset) == '\'') {
            return false;
        }
        if (state != 0) {
            return true;
        }
        return offset < string.length();
    }

    private static int getTypeOrCp(long tag) {
        assert (tag >= 0L);
        int type = AffixUtils.getType(tag);
        return type == 0 ? AffixUtils.getCodePoint(tag) : -type;
    }

    private static long makeTag(int offset, int type, int state, int cp) {
        long tag = 0L;
        tag |= (long)offset;
        tag |= -((long)type) << 32;
        tag |= (long)state << 36;
        assert ((tag |= (long)cp << 40) >= 0L);
        return tag;
    }

    private static int getOffset(long tag) {
        return (int)(tag & 0xFFFFFFFFFFFFFFFFL);
    }

    private static int getType(long tag) {
        return (int)(tag >>> 32 & 0xFL);
    }

    private static int getState(long tag) {
        return (int)(tag >>> 36 & 0xFL);
    }

    private static int getCodePoint(long tag) {
        return (int)(tag >>> 40);
    }

    public static interface TokenConsumer {
        public void consumeToken(int var1);
    }

    public static interface SymbolProvider {
        public CharSequence getSymbol(int var1);
    }
}

