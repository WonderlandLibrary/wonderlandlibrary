/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.ibm.icu.text.CharsetRecognizer;

abstract class CharsetRecog_Unicode
extends CharsetRecognizer {
    CharsetRecog_Unicode() {
    }

    abstract String getName();

    abstract CharsetMatch match(CharsetDetector var1);

    static class CharsetRecog_UTF_32_LE
    extends CharsetRecog_UTF_32 {
        CharsetRecog_UTF_32_LE() {
        }

        int getChar(byte[] input, int index) {
            return (input[index + 3] & 0xFF) << 24 | (input[index + 2] & 0xFF) << 16 | (input[index + 1] & 0xFF) << 8 | input[index + 0] & 0xFF;
        }

        String getName() {
            return "UTF-32LE";
        }
    }

    static class CharsetRecog_UTF_32_BE
    extends CharsetRecog_UTF_32 {
        CharsetRecog_UTF_32_BE() {
        }

        int getChar(byte[] input, int index) {
            return (input[index + 0] & 0xFF) << 24 | (input[index + 1] & 0xFF) << 16 | (input[index + 2] & 0xFF) << 8 | input[index + 3] & 0xFF;
        }

        String getName() {
            return "UTF-32BE";
        }
    }

    static abstract class CharsetRecog_UTF_32
    extends CharsetRecog_Unicode {
        CharsetRecog_UTF_32() {
        }

        abstract int getChar(byte[] var1, int var2);

        abstract String getName();

        CharsetMatch match(CharsetDetector det) {
            byte[] input = det.fRawInput;
            int limit = det.fRawLength / 4 * 4;
            int numValid = 0;
            int numInvalid = 0;
            boolean hasBOM = false;
            int confidence = 0;
            if (limit == 0) {
                return null;
            }
            if (this.getChar(input, 0) == 65279) {
                hasBOM = true;
            }
            for (int i = 0; i < limit; i += 4) {
                int ch = this.getChar(input, i);
                if (ch < 0 || ch >= 0x10FFFF || ch >= 55296 && ch <= 57343) {
                    ++numInvalid;
                    continue;
                }
                ++numValid;
            }
            if (hasBOM && numInvalid == 0) {
                confidence = 100;
            } else if (hasBOM && numValid > numInvalid * 10) {
                confidence = 80;
            } else if (numValid > 3 && numInvalid == 0) {
                confidence = 100;
            } else if (numValid > 0 && numInvalid == 0) {
                confidence = 80;
            } else if (numValid > numInvalid * 10) {
                confidence = 25;
            }
            return confidence == 0 ? null : new CharsetMatch(det, this, confidence);
        }
    }

    static class CharsetRecog_UTF_16_LE
    extends CharsetRecog_Unicode {
        CharsetRecog_UTF_16_LE() {
        }

        String getName() {
            return "UTF-16LE";
        }

        CharsetMatch match(CharsetDetector det) {
            byte[] input = det.fRawInput;
            if (input.length >= 2 && (input[0] & 0xFF) == 255 && (input[1] & 0xFF) == 254) {
                if (input.length >= 4 && input[2] == 0 && input[3] == 0) {
                    return null;
                }
                int confidence = 100;
                return new CharsetMatch(det, this, confidence);
            }
            return null;
        }
    }

    static class CharsetRecog_UTF_16_BE
    extends CharsetRecog_Unicode {
        CharsetRecog_UTF_16_BE() {
        }

        String getName() {
            return "UTF-16BE";
        }

        CharsetMatch match(CharsetDetector det) {
            byte[] input = det.fRawInput;
            if (input.length >= 2 && (input[0] & 0xFF) == 254 && (input[1] & 0xFF) == 255) {
                int confidence = 100;
                return new CharsetMatch(det, this, confidence);
            }
            return null;
        }
    }
}

