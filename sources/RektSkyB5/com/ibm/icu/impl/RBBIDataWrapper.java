/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.Trie2;
import com.ibm.icu.text.RuleBasedBreakIterator;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class RBBIDataWrapper {
    public RBBIDataHeader fHeader;
    public RBBIStateTable fFTable;
    public RBBIStateTable fRTable;
    public Trie2 fTrie;
    public String fRuleSource;
    public int[] fStatusTable;
    public static final int DATA_FORMAT = 1114794784;
    public static final int FORMAT_VERSION = 0x5000000;
    private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable();
    public static final int DH_SIZE = 20;
    public static final int DH_MAGIC = 0;
    public static final int DH_FORMATVERSION = 1;
    public static final int DH_LENGTH = 2;
    public static final int DH_CATCOUNT = 3;
    public static final int DH_FTABLE = 4;
    public static final int DH_FTABLELEN = 5;
    public static final int DH_RTABLE = 6;
    public static final int DH_RTABLELEN = 7;
    public static final int DH_TRIE = 8;
    public static final int DH_TRIELEN = 9;
    public static final int DH_RULESOURCE = 10;
    public static final int DH_RULESOURCELEN = 11;
    public static final int DH_STATUSTABLE = 12;
    public static final int DH_STATUSTABLELEN = 13;
    public static final int ACCEPTING = 0;
    public static final int LOOKAHEAD = 1;
    public static final int TAGIDX = 2;
    public static final int RESERVED = 3;
    public static final int NEXTSTATES = 4;
    public static final int RBBI_LOOKAHEAD_HARD_BREAK = 1;
    public static final int RBBI_BOF_REQUIRED = 2;

    public static boolean equals(RBBIStateTable left, RBBIStateTable right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return left.equals(right);
    }

    public int getRowIndex(int state) {
        return state * (this.fHeader.fCatCount + 4);
    }

    RBBIDataWrapper() {
    }

    public static RBBIDataWrapper get(ByteBuffer bytes) throws IOException {
        RBBIDataWrapper This = new RBBIDataWrapper();
        ICUBinary.readHeader(bytes, 1114794784, IS_ACCEPTABLE);
        This.fHeader = new RBBIDataHeader();
        This.fHeader.fMagic = bytes.getInt();
        This.fHeader.fFormatVersion[0] = bytes.get();
        This.fHeader.fFormatVersion[1] = bytes.get();
        This.fHeader.fFormatVersion[2] = bytes.get();
        This.fHeader.fFormatVersion[3] = bytes.get();
        This.fHeader.fLength = bytes.getInt();
        This.fHeader.fCatCount = bytes.getInt();
        This.fHeader.fFTable = bytes.getInt();
        This.fHeader.fFTableLen = bytes.getInt();
        This.fHeader.fRTable = bytes.getInt();
        This.fHeader.fRTableLen = bytes.getInt();
        This.fHeader.fTrie = bytes.getInt();
        This.fHeader.fTrieLen = bytes.getInt();
        This.fHeader.fRuleSource = bytes.getInt();
        This.fHeader.fRuleSourceLen = bytes.getInt();
        This.fHeader.fStatusTable = bytes.getInt();
        This.fHeader.fStatusTableLen = bytes.getInt();
        ICUBinary.skipBytes(bytes, 24);
        if (This.fHeader.fMagic != 45472 || !IS_ACCEPTABLE.isDataVersionAcceptable(This.fHeader.fFormatVersion)) {
            throw new IOException("Break Iterator Rule Data Magic Number Incorrect, or unsupported data version.");
        }
        int pos = 80;
        if (This.fHeader.fFTable < pos || This.fHeader.fFTable > This.fHeader.fLength) {
            throw new IOException("Break iterator Rule data corrupt");
        }
        ICUBinary.skipBytes(bytes, This.fHeader.fFTable - pos);
        pos = This.fHeader.fFTable;
        This.fFTable = RBBIStateTable.get(bytes, This.fHeader.fFTableLen);
        ICUBinary.skipBytes(bytes, This.fHeader.fRTable - (pos += This.fHeader.fFTableLen));
        pos = This.fHeader.fRTable;
        This.fRTable = RBBIStateTable.get(bytes, This.fHeader.fRTableLen);
        ICUBinary.skipBytes(bytes, This.fHeader.fTrie - (pos += This.fHeader.fRTableLen));
        pos = This.fHeader.fTrie;
        bytes.mark();
        This.fTrie = Trie2.createFromSerialized(bytes);
        bytes.reset();
        if (pos > This.fHeader.fStatusTable) {
            throw new IOException("Break iterator Rule data corrupt");
        }
        ICUBinary.skipBytes(bytes, This.fHeader.fStatusTable - pos);
        pos = This.fHeader.fStatusTable;
        This.fStatusTable = ICUBinary.getInts(bytes, This.fHeader.fStatusTableLen / 4, This.fHeader.fStatusTableLen & 3);
        if ((pos += This.fHeader.fStatusTableLen) > This.fHeader.fRuleSource) {
            throw new IOException("Break iterator Rule data corrupt");
        }
        ICUBinary.skipBytes(bytes, This.fHeader.fRuleSource - pos);
        pos = This.fHeader.fRuleSource;
        This.fRuleSource = ICUBinary.getString(bytes, This.fHeader.fRuleSourceLen / 2, This.fHeader.fRuleSourceLen & 1);
        if (RuleBasedBreakIterator.fDebugEnv != null && RuleBasedBreakIterator.fDebugEnv.indexOf("data") >= 0) {
            This.dump(System.out);
        }
        return This;
    }

    public void dump(PrintStream out) {
        if (this.fFTable == null) {
            throw new NullPointerException();
        }
        out.println("RBBI Data Wrapper dump ...");
        out.println();
        out.println("Forward State Table");
        this.dumpTable(out, this.fFTable);
        out.println("Reverse State Table");
        this.dumpTable(out, this.fRTable);
        this.dumpCharCategories(out);
        out.println("Source Rules: " + this.fRuleSource);
    }

    public static String intToString(int n2, int width) {
        StringBuilder dest = new StringBuilder(width);
        dest.append(n2);
        while (dest.length() < width) {
            dest.insert(0, ' ');
        }
        return dest.toString();
    }

    public static String intToHexString(int n2, int width) {
        StringBuilder dest = new StringBuilder(width);
        dest.append(Integer.toHexString(n2));
        while (dest.length() < width) {
            dest.insert(0, ' ');
        }
        return dest.toString();
    }

    private void dumpTable(PrintStream out, RBBIStateTable table) {
        if (table == null || table.fTable.length == 0) {
            out.println("  -- null -- ");
        } else {
            int n2;
            StringBuilder header = new StringBuilder(" Row  Acc Look  Tag");
            for (n2 = 0; n2 < this.fHeader.fCatCount; ++n2) {
                header.append(RBBIDataWrapper.intToString(n2, 5));
            }
            out.println(header.toString());
            for (n2 = 0; n2 < header.length(); ++n2) {
                out.print("-");
            }
            out.println();
            for (int state = 0; state < table.fNumStates; ++state) {
                this.dumpRow(out, table, state);
            }
            out.println();
        }
    }

    private void dumpRow(PrintStream out, RBBIStateTable table, int state) {
        StringBuilder dest = new StringBuilder(this.fHeader.fCatCount * 5 + 20);
        dest.append(RBBIDataWrapper.intToString(state, 4));
        int row = this.getRowIndex(state);
        if (table.fTable[row + 0] != 0) {
            dest.append(RBBIDataWrapper.intToString(table.fTable[row + 0], 5));
        } else {
            dest.append("     ");
        }
        if (table.fTable[row + 1] != 0) {
            dest.append(RBBIDataWrapper.intToString(table.fTable[row + 1], 5));
        } else {
            dest.append("     ");
        }
        dest.append(RBBIDataWrapper.intToString(table.fTable[row + 2], 5));
        for (int col = 0; col < this.fHeader.fCatCount; ++col) {
            dest.append(RBBIDataWrapper.intToString(table.fTable[row + 4 + col], 5));
        }
        out.println(dest);
    }

    private void dumpCharCategories(PrintStream out) {
        int category;
        int n2 = this.fHeader.fCatCount;
        String[] catStrings = new String[n2 + 1];
        int rangeStart = 0;
        int rangeEnd = 0;
        int lastCat = -1;
        int[] lastNewline = new int[n2 + 1];
        for (category = 0; category <= this.fHeader.fCatCount; ++category) {
            catStrings[category] = "";
        }
        out.println("\nCharacter Categories");
        out.println("--------------------");
        for (int char32 = 0; char32 <= 0x10FFFF; ++char32) {
            category = this.fTrie.get(char32);
            if ((category &= 0xFFFFBFFF) < 0 || category > this.fHeader.fCatCount) {
                out.println("Error, bad category " + Integer.toHexString(category) + " for char " + Integer.toHexString(char32));
                break;
            }
            if (category == lastCat) {
                rangeEnd = char32;
                continue;
            }
            if (lastCat >= 0) {
                if (catStrings[lastCat].length() > lastNewline[lastCat] + 70) {
                    lastNewline[lastCat] = catStrings[lastCat].length() + 10;
                    int n3 = lastCat;
                    catStrings[n3] = catStrings[n3] + "\n       ";
                }
                int n4 = lastCat;
                catStrings[n4] = catStrings[n4] + " " + Integer.toHexString(rangeStart);
                if (rangeEnd != rangeStart) {
                    int n5 = lastCat;
                    catStrings[n5] = catStrings[n5] + "-" + Integer.toHexString(rangeEnd);
                }
            }
            lastCat = category;
            rangeStart = rangeEnd = char32;
        }
        int n6 = lastCat;
        catStrings[n6] = catStrings[n6] + " " + Integer.toHexString(rangeStart);
        if (rangeEnd != rangeStart) {
            int n7 = lastCat;
            catStrings[n7] = catStrings[n7] + "-" + Integer.toHexString(rangeEnd);
        }
        for (category = 0; category <= this.fHeader.fCatCount; ++category) {
            out.println(RBBIDataWrapper.intToString(category, 5) + "  " + catStrings[category]);
        }
        out.println();
    }

    public static final class RBBIDataHeader {
        int fMagic = 0;
        byte[] fFormatVersion = new byte[4];
        int fLength;
        public int fCatCount;
        int fFTable;
        int fFTableLen;
        int fRTable;
        int fRTableLen;
        int fTrie;
        int fTrieLen;
        int fRuleSource;
        int fRuleSourceLen;
        int fStatusTable;
        int fStatusTableLen;
    }

    private static final class IsAcceptable
    implements ICUBinary.Authenticate {
        private IsAcceptable() {
        }

        @Override
        public boolean isDataVersionAcceptable(byte[] version) {
            int intVersion = (version[0] << 24) + (version[1] << 16) + (version[2] << 8) + version[3];
            return intVersion == 0x5000000;
        }
    }

    public static class RBBIStateTable {
        public int fNumStates;
        public int fRowLen;
        public int fFlags;
        public int fReserved;
        public short[] fTable;

        static RBBIStateTable get(ByteBuffer bytes, int length) throws IOException {
            if (length == 0) {
                return null;
            }
            if (length < 16) {
                throw new IOException("Invalid RBBI state table length.");
            }
            RBBIStateTable This = new RBBIStateTable();
            This.fNumStates = bytes.getInt();
            This.fRowLen = bytes.getInt();
            This.fFlags = bytes.getInt();
            This.fReserved = bytes.getInt();
            int lengthOfShorts = length - 16;
            This.fTable = ICUBinary.getShorts(bytes, lengthOfShorts / 2, lengthOfShorts & 1);
            return This;
        }

        public int put(DataOutputStream bytes) throws IOException {
            bytes.writeInt(this.fNumStates);
            bytes.writeInt(this.fRowLen);
            bytes.writeInt(this.fFlags);
            bytes.writeInt(this.fReserved);
            int tableLen = this.fRowLen * this.fNumStates / 2;
            for (int i2 = 0; i2 < tableLen; ++i2) {
                bytes.writeShort(this.fTable[i2]);
            }
            int bytesWritten = 16 + this.fRowLen * this.fNumStates;
            while (bytesWritten % 8 != 0) {
                bytes.writeByte(0);
                ++bytesWritten;
            }
            return bytesWritten;
        }

        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof RBBIStateTable)) {
                return false;
            }
            RBBIStateTable otherST = (RBBIStateTable)other;
            if (this.fNumStates != otherST.fNumStates) {
                return false;
            }
            if (this.fRowLen != otherST.fRowLen) {
                return false;
            }
            if (this.fFlags != otherST.fFlags) {
                return false;
            }
            if (this.fReserved != otherST.fReserved) {
                return false;
            }
            return Arrays.equals(this.fTable, otherST.fTable);
        }
    }
}

