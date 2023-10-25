/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.ibm.icu.text.CharsetRecognizer;

class CharsetRecog_UTF8
extends CharsetRecognizer {
    CharsetRecog_UTF8() {
    }

    String getName() {
        return "UTF-8";
    }

    CharsetMatch match(CharsetDetector det) {
        boolean hasBOM = false;
        int numValid = 0;
        int numInvalid = 0;
        byte[] input = det.fRawInput;
        int trailBytes = 0;
        if (det.fRawLength >= 3 && (input[0] & 0xFF) == 239 && (input[1] & 0xFF) == 187 && (input[2] & 0xFF) == 191) {
            hasBOM = true;
        }
        block0: for (int i = 0; i < det.fRawLength; ++i) {
            byte b = input[i];
            if ((b & 0x80) == 0) continue;
            if ((b & 0xE0) == 192) {
                trailBytes = 1;
            } else if ((b & 0xF0) == 224) {
                trailBytes = 2;
            } else if ((b & 0xF8) == 240) {
                trailBytes = 3;
            } else {
                if (++numInvalid > 5) break;
                trailBytes = 0;
            }
            while (++i < det.fRawLength) {
                b = input[i];
                if ((b & 0xC0) != 128) {
                    ++numInvalid;
                    continue block0;
                }
                if (--trailBytes != 0) continue;
                ++numValid;
                continue block0;
            }
        }
        int confidence = 0;
        if (hasBOM && numInvalid == 0) {
            confidence = 100;
        } else if (hasBOM && numValid > numInvalid * 10) {
            confidence = 80;
        } else if (numValid > 3 && numInvalid == 0) {
            confidence = 100;
        } else if (numValid > 0 && numInvalid == 0) {
            confidence = 80;
        } else if (numValid == 0 && numInvalid == 0) {
            confidence = 10;
        } else if (numValid > numInvalid * 10) {
            confidence = 25;
        }
        return confidence == 0 ? null : new CharsetMatch(det, this, confidence);
    }
}

