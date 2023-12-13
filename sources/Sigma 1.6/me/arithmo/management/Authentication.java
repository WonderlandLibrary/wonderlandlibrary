/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Authentication {
    public static String convertToHex(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        int bytesCounter = 0;
        int value = 0;
        StringBuilder sbHex = new StringBuilder();
        StringBuilder sbText = new StringBuilder();
        StringBuilder sbResult = new StringBuilder();
        while ((value = is.read()) != -1) {
            sbHex.append(String.format("%02X", value));
            if (!Character.isISOControl(value)) {
                sbText.append((char)value);
            } else {
                sbText.append(".");
            }
            if (bytesCounter == 15) {
                sbResult.append(sbHex);
                sbHex.setLength(0);
                sbText.setLength(0);
                bytesCounter = 0;
                continue;
            }
            ++bytesCounter;
        }
        if (bytesCounter != 0) {
            while (bytesCounter < 16) {
                sbHex.append("   ");
                ++bytesCounter;
            }
            sbResult.append(sbHex);
        }
        is.close();
        return sbResult.toString();
    }
}

