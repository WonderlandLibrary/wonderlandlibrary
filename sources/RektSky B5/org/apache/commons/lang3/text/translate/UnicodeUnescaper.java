/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

@Deprecated
public class UnicodeUnescaper
extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u') {
            int i2 = 2;
            while (index + i2 < input.length() && input.charAt(index + i2) == 'u') {
                ++i2;
            }
            if (index + i2 < input.length() && input.charAt(index + i2) == '+') {
                ++i2;
            }
            if (index + i2 + 4 <= input.length()) {
                CharSequence unicode = input.subSequence(index + i2, index + i2 + 4);
                try {
                    int value = Integer.parseInt(unicode.toString(), 16);
                    out.write((char)value);
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, nfe);
                }
                return i2 + 4;
            }
            throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + input.subSequence(index, input.length()) + "' due to end of CharSequence");
        }
        return 0;
    }
}

