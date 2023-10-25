/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.IntTrieBuilder;
import com.ibm.icu.impl.TrieBuilder;
import com.ibm.icu.text.RBBINode;
import com.ibm.icu.text.RBBIRuleBuilder;
import com.ibm.icu.text.UnicodeSet;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class RBBISetBuilder {
    RBBIRuleBuilder fRB;
    RangeDescriptor fRangeList;
    IntTrieBuilder fTrie;
    int fTrieSize;
    int fGroupCount;
    boolean fSawBOF;
    RBBIDataManipulate dm = new RBBIDataManipulate();

    RBBISetBuilder(RBBIRuleBuilder rb) {
        this.fRB = rb;
    }

    void build() {
        RangeDescriptor rlRange;
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("usets") >= 0) {
            this.printSets();
        }
        this.fRangeList = new RangeDescriptor();
        this.fRangeList.fStartChar = 0;
        this.fRangeList.fEndChar = 0x10FFFF;
        for (RBBINode usetNode : this.fRB.fUSetNodes) {
            UnicodeSet inputSet = usetNode.fInputSet;
            int inputSetRangeCount = inputSet.getRangeCount();
            int inputSetRangeIndex = 0;
            rlRange = this.fRangeList;
            while (inputSetRangeIndex < inputSetRangeCount) {
                int inputSetRangeBegin = inputSet.getRangeStart(inputSetRangeIndex);
                int inputSetRangeEnd = inputSet.getRangeEnd(inputSetRangeIndex);
                while (rlRange.fEndChar < inputSetRangeBegin) {
                    rlRange = rlRange.fNext;
                }
                if (rlRange.fStartChar < inputSetRangeBegin) {
                    rlRange.split(inputSetRangeBegin);
                    continue;
                }
                if (rlRange.fEndChar > inputSetRangeEnd) {
                    rlRange.split(inputSetRangeEnd + 1);
                }
                if (rlRange.fIncludesSets.indexOf(usetNode) == -1) {
                    rlRange.fIncludesSets.add(usetNode);
                }
                if (inputSetRangeEnd == rlRange.fEndChar) {
                    ++inputSetRangeIndex;
                }
                rlRange = rlRange.fNext;
            }
        }
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("range") >= 0) {
            this.printRanges();
        }
        rlRange = this.fRangeList;
        while (rlRange != null) {
            RangeDescriptor rlSearchRange = this.fRangeList;
            while (rlSearchRange != rlRange) {
                if (((Object)rlRange.fIncludesSets).equals(rlSearchRange.fIncludesSets)) {
                    rlRange.fNum = rlSearchRange.fNum;
                    break;
                }
                rlSearchRange = rlSearchRange.fNext;
            }
            if (rlRange.fNum == 0) {
                ++this.fGroupCount;
                rlRange.fNum = this.fGroupCount + 2;
                rlRange.setDictionaryFlag();
                this.addValToSets(rlRange.fIncludesSets, this.fGroupCount + 2);
            }
            rlRange = rlRange.fNext;
        }
        String eofString = "eof";
        String bofString = "bof";
        for (RBBINode usetNode : this.fRB.fUSetNodes) {
            UnicodeSet inputSet = usetNode.fInputSet;
            if (inputSet.contains(eofString)) {
                this.addValToSet(usetNode, 1);
            }
            if (!inputSet.contains(bofString)) continue;
            this.addValToSet(usetNode, 2);
            this.fSawBOF = true;
        }
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("rgroup") >= 0) {
            this.printRangeGroups();
        }
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("esets") >= 0) {
            this.printSets();
        }
        this.fTrie = new IntTrieBuilder(null, 100000, 0, 0, true);
        rlRange = this.fRangeList;
        while (rlRange != null) {
            this.fTrie.setRange(rlRange.fStartChar, rlRange.fEndChar + 1, rlRange.fNum, true);
            rlRange = rlRange.fNext;
        }
    }

    int getTrieSize() {
        int size = 0;
        try {
            size = this.fTrie.serialize(null, true, this.dm);
        }
        catch (IOException e) {
            Assert.assrt(false);
        }
        return size;
    }

    void serializeTrie(OutputStream os) throws IOException {
        this.fTrie.serialize(os, true, this.dm);
    }

    void addValToSets(List<RBBINode> sets, int val) {
        for (RBBINode usetNode : sets) {
            this.addValToSet(usetNode, val);
        }
    }

    void addValToSet(RBBINode usetNode, int val) {
        RBBINode leafNode = new RBBINode(3);
        leafNode.fVal = val;
        if (usetNode.fLeftChild == null) {
            usetNode.fLeftChild = leafNode;
            leafNode.fParent = usetNode;
        } else {
            RBBINode orNode = new RBBINode(9);
            orNode.fLeftChild = usetNode.fLeftChild;
            orNode.fRightChild = leafNode;
            orNode.fLeftChild.fParent = orNode;
            orNode.fRightChild.fParent = orNode;
            usetNode.fLeftChild = orNode;
            orNode.fParent = usetNode;
        }
    }

    int getNumCharCategories() {
        return this.fGroupCount + 3;
    }

    boolean sawBOF() {
        return this.fSawBOF;
    }

    int getFirstChar(int category) {
        int retVal = -1;
        RangeDescriptor rlRange = this.fRangeList;
        while (rlRange != null) {
            if (rlRange.fNum == category) {
                retVal = rlRange.fStartChar;
                break;
            }
            rlRange = rlRange.fNext;
        }
        return retVal;
    }

    void printRanges() {
        System.out.print("\n\n Nonoverlapping Ranges ...\n");
        RangeDescriptor rlRange = this.fRangeList;
        while (rlRange != null) {
            System.out.print(" " + rlRange.fNum + "   " + rlRange.fStartChar + "-" + rlRange.fEndChar);
            for (int i = 0; i < rlRange.fIncludesSets.size(); ++i) {
                RBBINode varRef;
                RBBINode usetNode = rlRange.fIncludesSets.get(i);
                String setName = "anon";
                RBBINode setRef = usetNode.fParent;
                if (setRef != null && (varRef = setRef.fParent) != null && varRef.fType == 2) {
                    setName = varRef.fText;
                }
                System.out.print(setName);
                System.out.print("  ");
            }
            System.out.println("");
            rlRange = rlRange.fNext;
        }
    }

    void printRangeGroups() {
        int lastPrintedGroupNum = 0;
        System.out.print("\nRanges grouped by Unicode Set Membership...\n");
        RangeDescriptor rlRange = this.fRangeList;
        while (rlRange != null) {
            int groupNum = rlRange.fNum & 0xBFFF;
            if (groupNum > lastPrintedGroupNum) {
                int i;
                lastPrintedGroupNum = groupNum;
                if (groupNum < 10) {
                    System.out.print(" ");
                }
                System.out.print(groupNum + " ");
                if ((rlRange.fNum & 0x4000) != 0) {
                    System.out.print(" <DICT> ");
                }
                for (i = 0; i < rlRange.fIncludesSets.size(); ++i) {
                    RBBINode varRef;
                    RBBINode usetNode = rlRange.fIncludesSets.get(i);
                    String setName = "anon";
                    RBBINode setRef = usetNode.fParent;
                    if (setRef != null && (varRef = setRef.fParent) != null && varRef.fType == 2) {
                        setName = varRef.fText;
                    }
                    System.out.print(setName);
                    System.out.print(" ");
                }
                i = 0;
                RangeDescriptor tRange = rlRange;
                while (tRange != null) {
                    if (tRange.fNum == rlRange.fNum) {
                        if (i++ % 5 == 0) {
                            System.out.print("\n    ");
                        }
                        RBBINode.printHex(tRange.fStartChar, -1);
                        System.out.print("-");
                        RBBINode.printHex(tRange.fEndChar, 0);
                    }
                    tRange = tRange.fNext;
                }
                System.out.print("\n");
            }
            rlRange = rlRange.fNext;
        }
        System.out.print("\n");
    }

    void printSets() {
        System.out.print("\n\nUnicode Sets List\n------------------\n");
        for (int i = 0; i < this.fRB.fUSetNodes.size(); ++i) {
            RBBINode varRef;
            RBBINode usetNode = this.fRB.fUSetNodes.get(i);
            RBBINode.printInt(2, i);
            String setName = "anonymous";
            RBBINode setRef = usetNode.fParent;
            if (setRef != null && (varRef = setRef.fParent) != null && varRef.fType == 2) {
                setName = varRef.fText;
            }
            System.out.print("  " + setName);
            System.out.print("   ");
            System.out.print(usetNode.fText);
            System.out.print("\n");
            if (usetNode.fLeftChild == null) continue;
            usetNode.fLeftChild.printTree(true);
        }
        System.out.print("\n");
    }

    class RBBIDataManipulate
    implements TrieBuilder.DataManipulate {
        RBBIDataManipulate() {
        }

        public int getFoldedValue(int start, int offset) {
            boolean[] inBlockZero = new boolean[1];
            int limit = start + 1024;
            while (start < limit) {
                int value = RBBISetBuilder.this.fTrie.getValue(start, inBlockZero);
                if (inBlockZero[0]) {
                    start += 32;
                    continue;
                }
                if (value != 0) {
                    return offset | 0x8000;
                }
                ++start;
            }
            return 0;
        }
    }

    static class RangeDescriptor {
        int fStartChar;
        int fEndChar;
        int fNum;
        List<RBBINode> fIncludesSets;
        RangeDescriptor fNext;

        RangeDescriptor() {
            this.fIncludesSets = new ArrayList<RBBINode>();
        }

        RangeDescriptor(RangeDescriptor other) {
            this.fStartChar = other.fStartChar;
            this.fEndChar = other.fEndChar;
            this.fNum = other.fNum;
            this.fIncludesSets = new ArrayList<RBBINode>(other.fIncludesSets);
        }

        void split(int where) {
            Assert.assrt(where > this.fStartChar && where <= this.fEndChar);
            RangeDescriptor nr = new RangeDescriptor(this);
            nr.fStartChar = where;
            this.fEndChar = where - 1;
            nr.fNext = this.fNext;
            this.fNext = nr;
        }

        void setDictionaryFlag() {
            for (int i = 0; i < this.fIncludesSets.size(); ++i) {
                RBBINode varRef;
                RBBINode usetNode = this.fIncludesSets.get(i);
                String setName = "";
                RBBINode setRef = usetNode.fParent;
                if (setRef != null && (varRef = setRef.fParent) != null && varRef.fType == 2) {
                    setName = varRef.fText;
                }
                if (!setName.equals("dictionary")) continue;
                this.fNum |= 0x4000;
                break;
            }
        }
    }
}

