// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

import java.util.Set;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public final class AlphabetConverter
{
    private final Map<Integer, String> originalToEncoded;
    private final Map<String, String> encodedToOriginal;
    private final int encodedLetterLength;
    private static final String ARROW = " -> ";
    
    private AlphabetConverter(final Map<Integer, String> originalToEncoded, final Map<String, String> encodedToOriginal, final int encodedLetterLength) {
        this.originalToEncoded = originalToEncoded;
        this.encodedToOriginal = encodedToOriginal;
        this.encodedLetterLength = encodedLetterLength;
    }
    
    public String encode(final String original) throws UnsupportedEncodingException {
        if (original == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        int codepoint;
        for (int i = 0; i < original.length(); i += Character.charCount(codepoint)) {
            codepoint = original.codePointAt(i);
            final String nextLetter = this.originalToEncoded.get(codepoint);
            if (nextLetter == null) {
                throw new UnsupportedEncodingException("Couldn't find encoding for '" + codePointToString(codepoint) + "' in " + original);
            }
            sb.append(nextLetter);
        }
        return sb.toString();
    }
    
    public String decode(final String encoded) throws UnsupportedEncodingException {
        if (encoded == null) {
            return null;
        }
        final StringBuilder result = new StringBuilder();
        int j = 0;
        while (j < encoded.length()) {
            final int i = encoded.codePointAt(j);
            final String s = codePointToString(i);
            if (s.equals(this.originalToEncoded.get(i))) {
                result.append(s);
                ++j;
            }
            else {
                if (j + this.encodedLetterLength > encoded.length()) {
                    throw new UnsupportedEncodingException("Unexpected end of string while decoding " + encoded);
                }
                final String nextGroup = encoded.substring(j, j + this.encodedLetterLength);
                final String next = this.encodedToOriginal.get(nextGroup);
                if (next == null) {
                    throw new UnsupportedEncodingException("Unexpected string without decoding (" + nextGroup + ") in " + encoded);
                }
                result.append(next);
                j += this.encodedLetterLength;
            }
        }
        return result.toString();
    }
    
    public int getEncodedCharLength() {
        return this.encodedLetterLength;
    }
    
    public Map<Integer, String> getOriginalToEncoded() {
        return Collections.unmodifiableMap((Map<? extends Integer, ? extends String>)this.originalToEncoded);
    }
    
    private void addSingleEncoding(final int level, final String currentEncoding, final Collection<Integer> encoding, final Iterator<Integer> originals, final Map<Integer, String> doNotEncodeMap) {
        if (level > 0) {
            for (final int encodingLetter : encoding) {
                if (!originals.hasNext()) {
                    return;
                }
                if (level == this.encodedLetterLength && doNotEncodeMap.containsKey(encodingLetter)) {
                    continue;
                }
                this.addSingleEncoding(level - 1, currentEncoding + codePointToString(encodingLetter), encoding, originals, doNotEncodeMap);
            }
        }
        else {
            Integer next;
            for (next = originals.next(); doNotEncodeMap.containsKey(next); next = originals.next()) {
                final String originalLetterAsString = codePointToString(next);
                this.originalToEncoded.put(next, originalLetterAsString);
                this.encodedToOriginal.put(originalLetterAsString, originalLetterAsString);
                if (!originals.hasNext()) {
                    return;
                }
            }
            final String originalLetterAsString = codePointToString(next);
            this.originalToEncoded.put(next, currentEncoding);
            this.encodedToOriginal.put(currentEncoding, originalLetterAsString);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<Integer, String> entry : this.originalToEncoded.entrySet()) {
            sb.append(codePointToString(entry.getKey())).append(" -> ").append(entry.getValue()).append(System.lineSeparator());
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AlphabetConverter)) {
            return false;
        }
        final AlphabetConverter other = (AlphabetConverter)obj;
        return this.originalToEncoded.equals(other.originalToEncoded) && this.encodedToOriginal.equals(other.encodedToOriginal) && this.encodedLetterLength == other.encodedLetterLength;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.originalToEncoded, this.encodedToOriginal, this.encodedLetterLength);
    }
    
    public static AlphabetConverter createConverterFromMap(final Map<Integer, String> originalToEncoded) {
        final Map<Integer, String> unmodifiableOriginalToEncoded = Collections.unmodifiableMap((Map<? extends Integer, ? extends String>)originalToEncoded);
        final Map<String, String> encodedToOriginal = new LinkedHashMap<String, String>();
        int encodedLetterLength = 1;
        for (final Map.Entry<Integer, String> e : unmodifiableOriginalToEncoded.entrySet()) {
            final String originalAsString = codePointToString(e.getKey());
            encodedToOriginal.put(e.getValue(), originalAsString);
            if (e.getValue().length() > encodedLetterLength) {
                encodedLetterLength = e.getValue().length();
            }
        }
        return new AlphabetConverter(unmodifiableOriginalToEncoded, encodedToOriginal, encodedLetterLength);
    }
    
    public static AlphabetConverter createConverterFromChars(final Character[] original, final Character[] encoding, final Character[] doNotEncode) {
        return createConverter(convertCharsToIntegers(original), convertCharsToIntegers(encoding), convertCharsToIntegers(doNotEncode));
    }
    
    private static Integer[] convertCharsToIntegers(final Character[] chars) {
        if (ArrayUtils.isEmpty((Object[])chars)) {
            return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
        }
        final Integer[] integers = new Integer[chars.length];
        for (int i = 0; i < chars.length; ++i) {
            integers[i] = (int)chars[i];
        }
        return integers;
    }
    
    public static AlphabetConverter createConverter(final Integer[] original, final Integer[] encoding, final Integer[] doNotEncode) {
        final Set<Integer> originalCopy = new LinkedHashSet<Integer>(Arrays.asList(original));
        final Set<Integer> encodingCopy = new LinkedHashSet<Integer>(Arrays.asList(encoding));
        final Set<Integer> doNotEncodeCopy = new LinkedHashSet<Integer>(Arrays.asList(doNotEncode));
        final Map<Integer, String> originalToEncoded = new LinkedHashMap<Integer, String>();
        final Map<String, String> encodedToOriginal = new LinkedHashMap<String, String>();
        final Map<Integer, String> doNotEncodeMap = new HashMap<Integer, String>();
        for (final int i : doNotEncodeCopy) {
            if (!originalCopy.contains(i)) {
                throw new IllegalArgumentException("Can not use 'do not encode' list because original alphabet does not contain '" + codePointToString(i) + "'");
            }
            if (!encodingCopy.contains(i)) {
                throw new IllegalArgumentException("Can not use 'do not encode' list because encoding alphabet does not contain '" + codePointToString(i) + "'");
            }
            doNotEncodeMap.put(i, codePointToString(i));
        }
        if (encodingCopy.size() >= originalCopy.size()) {
            final int encodedLetterLength = 1;
            final Iterator<Integer> it = encodingCopy.iterator();
            for (final int originalLetter : originalCopy) {
                final String originalLetterAsString = codePointToString(originalLetter);
                if (doNotEncodeMap.containsKey(originalLetter)) {
                    originalToEncoded.put(originalLetter, originalLetterAsString);
                    encodedToOriginal.put(originalLetterAsString, originalLetterAsString);
                }
                else {
                    Integer next;
                    for (next = it.next(); doNotEncodeCopy.contains(next); next = it.next()) {}
                    final String encodedLetter = codePointToString(next);
                    originalToEncoded.put(originalLetter, encodedLetter);
                    encodedToOriginal.put(encodedLetter, originalLetterAsString);
                }
            }
            return new AlphabetConverter(originalToEncoded, encodedToOriginal, encodedLetterLength);
        }
        if (encodingCopy.size() - doNotEncodeCopy.size() < 2) {
            throw new IllegalArgumentException("Must have at least two encoding characters (excluding those in the 'do not encode' list), but has " + (encodingCopy.size() - doNotEncodeCopy.size()));
        }
        int lettersSoFar = 1;
        for (int lettersLeft = (originalCopy.size() - doNotEncodeCopy.size()) / (encodingCopy.size() - doNotEncodeCopy.size()); lettersLeft / encodingCopy.size() >= 1; lettersLeft /= encodingCopy.size(), ++lettersSoFar) {}
        final int encodedLetterLength = lettersSoFar + 1;
        final AlphabetConverter ac = new AlphabetConverter(originalToEncoded, encodedToOriginal, encodedLetterLength);
        ac.addSingleEncoding(encodedLetterLength, "", encodingCopy, originalCopy.iterator(), doNotEncodeMap);
        return ac;
    }
    
    private static String codePointToString(final int i) {
        if (Character.charCount(i) == 1) {
            return String.valueOf((char)i);
        }
        return new String(Character.toChars(i));
    }
}
