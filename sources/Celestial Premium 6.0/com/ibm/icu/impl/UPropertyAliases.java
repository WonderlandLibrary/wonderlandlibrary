/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.ibm.icu.util.BytesTrie;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;

public final class UPropertyAliases {
    private static final int IX_VALUE_MAPS_OFFSET = 0;
    private static final int IX_BYTE_TRIES_OFFSET = 1;
    private static final int IX_NAME_GROUPS_OFFSET = 2;
    private static final int IX_RESERVED3_OFFSET = 3;
    private int[] valueMaps;
    private byte[] bytesTries;
    private String nameGroups;
    private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable();
    private static final byte[] DATA_FORMAT = new byte[]{112, 110, 97, 109};
    public static final UPropertyAliases INSTANCE;

    private void load(InputStream data) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(data);
        ICUBinary.readHeader(bis, DATA_FORMAT, IS_ACCEPTABLE);
        DataInputStream ds = new DataInputStream(bis);
        int indexesLength = ds.readInt() / 4;
        if (indexesLength < 8) {
            throw new IOException("pnames.icu: not enough indexes");
        }
        int[] inIndexes = new int[indexesLength];
        inIndexes[0] = indexesLength * 4;
        for (int i = 1; i < indexesLength; ++i) {
            inIndexes[i] = ds.readInt();
        }
        int offset = inIndexes[0];
        int nextOffset = inIndexes[1];
        int numInts = (nextOffset - offset) / 4;
        this.valueMaps = new int[numInts];
        for (int i = 0; i < numInts; ++i) {
            this.valueMaps[i] = ds.readInt();
        }
        offset = nextOffset;
        nextOffset = inIndexes[2];
        int numBytes = nextOffset - offset;
        this.bytesTries = new byte[numBytes];
        ds.readFully(this.bytesTries);
        offset = nextOffset;
        nextOffset = inIndexes[3];
        numBytes = nextOffset - offset;
        StringBuilder sb = new StringBuilder(numBytes);
        for (int i = 0; i < numBytes; ++i) {
            sb.append((char)ds.readByte());
        }
        this.nameGroups = sb.toString();
        data.close();
    }

    private UPropertyAliases() throws IOException {
        this.load(ICUData.getRequiredStream("data/icudt51b/pnames.icu"));
    }

    private int findProperty(int property) {
        int i = 1;
        for (int numRanges = this.valueMaps[0]; numRanges > 0; --numRanges) {
            int start = this.valueMaps[i];
            int limit = this.valueMaps[i + 1];
            i += 2;
            if (property < start) break;
            if (property < limit) {
                return i + (property - start) * 2;
            }
            i += (limit - start) * 2;
        }
        return 0;
    }

    private int findPropertyValueNameGroup(int valueMapIndex, int value) {
        if (valueMapIndex == 0) {
            return 0;
        }
        int n = ++valueMapIndex;
        ++valueMapIndex;
        int numRanges = this.valueMaps[n];
        if (numRanges < 16) {
            while (numRanges > 0) {
                int start = this.valueMaps[valueMapIndex];
                int limit = this.valueMaps[valueMapIndex + 1];
                valueMapIndex += 2;
                if (value >= start) {
                    if (value < limit) {
                        return this.valueMaps[valueMapIndex + value - start];
                    }
                    valueMapIndex += limit - start;
                    --numRanges;
                    continue;
                }
                break;
            }
        } else {
            int v;
            int valuesStart = valueMapIndex;
            int nameGroupOffsetsStart = valueMapIndex + numRanges - 16;
            while (value >= (v = this.valueMaps[valueMapIndex])) {
                if (value == v) {
                    return this.valueMaps[nameGroupOffsetsStart + valueMapIndex - valuesStart];
                }
                if (++valueMapIndex < nameGroupOffsetsStart) continue;
            }
        }
        return 0;
    }

    private String getName(int nameGroupsIndex, int nameIndex) {
        char numNames = this.nameGroups.charAt(nameGroupsIndex++);
        if (nameIndex < 0 || numNames <= nameIndex) {
            throw new IllegalIcuArgumentException("Invalid property (value) name choice");
        }
        while (nameIndex > 0) {
            while ('\u0000' != this.nameGroups.charAt(nameGroupsIndex++)) {
            }
            --nameIndex;
        }
        int nameStart = nameGroupsIndex;
        while ('\u0000' != this.nameGroups.charAt(nameGroupsIndex)) {
            ++nameGroupsIndex;
        }
        if (nameStart == nameGroupsIndex) {
            return null;
        }
        return this.nameGroups.substring(nameStart, nameGroupsIndex);
    }

    private static int asciiToLowercase(int c) {
        return 65 <= c && c <= 90 ? c + 32 : c;
    }

    private boolean containsName(BytesTrie trie, CharSequence name) {
        BytesTrie.Result result = BytesTrie.Result.NO_VALUE;
        for (int i = 0; i < name.length(); ++i) {
            int c = name.charAt(i);
            if (c == 45 || c == 95 || c == 32 || 9 <= c && c <= 13) continue;
            if (!result.hasNext()) {
                return false;
            }
            c = UPropertyAliases.asciiToLowercase(c);
            result = trie.next(c);
        }
        return result.hasValue();
    }

    public String getPropertyName(int property, int nameChoice) {
        int valueMapIndex = this.findProperty(property);
        if (valueMapIndex == 0) {
            throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
        }
        return this.getName(this.valueMaps[valueMapIndex], nameChoice);
    }

    public String getPropertyValueName(int property, int value, int nameChoice) {
        int valueMapIndex = this.findProperty(property);
        if (valueMapIndex == 0) {
            throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
        }
        int nameGroupOffset = this.findPropertyValueNameGroup(this.valueMaps[valueMapIndex + 1], value);
        if (nameGroupOffset == 0) {
            throw new IllegalArgumentException("Property " + property + " (0x" + Integer.toHexString(property) + ") does not have named values");
        }
        return this.getName(nameGroupOffset, nameChoice);
    }

    private int getPropertyOrValueEnum(int bytesTrieOffset, CharSequence alias) {
        BytesTrie trie = new BytesTrie(this.bytesTries, bytesTrieOffset);
        if (this.containsName(trie, alias)) {
            return trie.getValue();
        }
        return -1;
    }

    public int getPropertyEnum(CharSequence alias) {
        return this.getPropertyOrValueEnum(0, alias);
    }

    public int getPropertyValueEnum(int property, CharSequence alias) {
        int valueMapIndex = this.findProperty(property);
        if (valueMapIndex == 0) {
            throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
        }
        if ((valueMapIndex = this.valueMaps[valueMapIndex + 1]) == 0) {
            throw new IllegalArgumentException("Property " + property + " (0x" + Integer.toHexString(property) + ") does not have named values");
        }
        return this.getPropertyOrValueEnum(this.valueMaps[valueMapIndex], alias);
    }

    public static int compare(String stra, String strb) {
        int istra = 0;
        int istrb = 0;
        char cstra = '\u0000';
        char cstrb = '\u0000';
        block6: while (true) {
            boolean endstrb;
            if (istra < stra.length()) {
                cstra = stra.charAt(istra);
                switch (cstra) {
                    case '\t': 
                    case '\n': 
                    case '\u000b': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                    case '-': 
                    case '_': {
                        ++istra;
                        continue block6;
                    }
                }
            }
            block7: while (istrb < strb.length()) {
                cstrb = strb.charAt(istrb);
                switch (cstrb) {
                    case '\t': 
                    case '\n': 
                    case '\u000b': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                    case '-': 
                    case '_': {
                        ++istrb;
                        continue block7;
                    }
                }
            }
            boolean endstra = istra == stra.length();
            boolean bl = endstrb = istrb == strb.length();
            if (endstra) {
                if (endstrb) {
                    return 0;
                }
                cstra = '\u0000';
            } else if (endstrb) {
                cstrb = '\u0000';
            }
            int rc = UPropertyAliases.asciiToLowercase(cstra) - UPropertyAliases.asciiToLowercase(cstrb);
            if (rc != 0) {
                return rc;
            }
            ++istra;
            ++istrb;
        }
    }

    static {
        try {
            INSTANCE = new UPropertyAliases();
        }
        catch (IOException e) {
            MissingResourceException mre = new MissingResourceException("Could not construct UPropertyAliases. Missing pnames.icu", "", "");
            mre.initCause(e);
            throw mre;
        }
    }

    private static final class IsAcceptable
    implements ICUBinary.Authenticate {
        private IsAcceptable() {
        }

        public boolean isDataVersionAcceptable(byte[] version) {
            return version[0] == 2;
        }
    }
}

