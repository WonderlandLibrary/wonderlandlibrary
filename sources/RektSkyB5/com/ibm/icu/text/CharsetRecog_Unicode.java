/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.ibm.icu.text.CharsetRecognizer;

abstract class CharsetRecog_Unicode
extends CharsetRecognizer {
    CharsetRecog_Unicode() {
    }

    @Override
    abstract String getName();

    @Override
    abstract CharsetMatch match(CharsetDetector var1);

    static int codeUnit16FromBytes(byte hi, byte lo) {
        return (hi & 0xFF) << 8 | lo & 0xFF;
    }

    static int adjustConfidence(int codeUnit, int confidence) {
        if (codeUnit == 0) {
            confidence -= 10;
        } else if (codeUnit >= 32 && codeUnit <= 255 || codeUnit == 10) {
            confidence += 10;
        }
        if (confidence < 0) {
            confidence = 0;
        } else if (confidence > 100) {
            confidence = 100;
        }
        return confidence;
    }

    static class CharsetRecog_UTF_32_LE
    extends CharsetRecog_UTF_32 {
        CharsetRecog_UTF_32_LE() {
        }

        @Override
        int getChar(byte[] input, int index) {
            return (input[index + 3] & 0xFF) << 24 | (input[index + 2] & 0xFF) << 16 | (input[index + 1] & 0xFF) << 8 | input[index + 0] & 0xFF;
        }

        @Override
        String getName() {
            return "UTF-32LE";
        }
    }

    static class CharsetRecog_UTF_32_BE
    extends CharsetRecog_UTF_32 {
        CharsetRecog_UTF_32_BE() {
        }

        @Override
        int getChar(byte[] input, int index) {
            return (input[index + 0] & 0xFF) << 24 | (input[index + 1] & 0xFF) << 16 | (input[index + 2] & 0xFF) << 8 | input[index + 3] & 0xFF;
        }

        @Override
        String getName() {
            return "UTF-32BE";
        }
    }

    static abstract class CharsetRecog_UTF_32
    extends CharsetRecog_Unicode {
        CharsetRecog_UTF_32() {
        }

        abstract int getChar(byte[] var1, int var2);

        @Override
        abstract String getName();

        @Override
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
            for (int i2 = 0; i2 < limit; i2 += 4) {
                int ch = this.getChar(input, i2);
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

        @Override
        String getName() {
            return "UTF-16LE";
        }

        @Override
        CharsetMatch match(CharsetDetector det) {
            byte[] input = det.fRawInput;
            int confidence = 10;
            int bytesToCheck = Math.min(input.length, 30);
            for (int charIndex = 0; charIndex < bytesToCheck - 1; charIndex += 2) {
                int codeUnit = CharsetRecog_UTF_16_LE.codeUnit16FromBytes(input[charIndex + 1], input[charIndex]);
                if (charIndex == 0 && codeUnit == 65279) {
                    confidence = 100;
                    break;
                }
                if ((confidence = CharsetRecog_UTF_16_LE.adjustConfidence(codeUnit, confidence)) == 0 || confidence == 100) break;
            }
            if (bytesToCheck < 4 && confidence < 100) {
                confidence = 0;
            }
            if (confidence > 0) {
                return new CharsetMatch(det, this, confidence);
            }
            return null;
        }
    }

    static class CharsetRecog_UTF_16_BE
    extends CharsetRecog_Unicode {
        CharsetRecog_UTF_16_BE() {
        }

        @Override
        String getName() {
            return "UTF-16BE";
        }

        @Override
        CharsetMatch match(CharsetDetector det) {
            byte[] input = det.fRawInput;
            int confidence = 10;
            int bytesToCheck = Math.min(input.length, 30);
            for (int charIndex = 0; charIndex < bytesToCheck - 1; charIndex += 2) {
                int codeUnit = CharsetRecog_UTF_16_BE.codeUnit16FromBytes(input[charIndex], input[charIndex + 1]);
                if (charIndex == 0 && codeUnit == 65279) {
                    confidence = 100;
                    break;
                }
                if ((confidence = CharsetRecog_UTF_16_BE.adjustConfidence(codeUnit, confidence)) == 0 || confidence == 100) break;
            }
            if (bytesToCheck < 4 && confidence < 100) {
                confidence = 0;
            }
            if (confidence > 0) {
                return new CharsetMatch(det, this, confidence);
            }
            return null;
        }
    }
}

