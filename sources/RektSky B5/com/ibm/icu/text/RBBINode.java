/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.text.UnicodeSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class RBBINode {
    static final int setRef = 0;
    static final int uset = 1;
    static final int varRef = 2;
    static final int leafChar = 3;
    static final int lookAhead = 4;
    static final int tag = 5;
    static final int endMark = 6;
    static final int opStart = 7;
    static final int opCat = 8;
    static final int opOr = 9;
    static final int opStar = 10;
    static final int opPlus = 11;
    static final int opQuestion = 12;
    static final int opBreak = 13;
    static final int opReverse = 14;
    static final int opLParen = 15;
    static final int nodeTypeLimit = 16;
    static final String[] nodeTypeNames = new String[]{"setRef", "uset", "varRef", "leafChar", "lookAhead", "tag", "endMark", "opStart", "opCat", "opOr", "opStar", "opPlus", "opQuestion", "opBreak", "opReverse", "opLParen"};
    static final int precZero = 0;
    static final int precStart = 1;
    static final int precLParen = 2;
    static final int precOpOr = 3;
    static final int precOpCat = 4;
    int fType;
    RBBINode fParent;
    RBBINode fLeftChild;
    RBBINode fRightChild;
    UnicodeSet fInputSet;
    int fPrecedence = 0;
    String fText;
    int fFirstPos;
    int fLastPos;
    boolean fNullable;
    int fVal;
    boolean fLookAheadEnd;
    boolean fRuleRoot;
    boolean fChainIn;
    Set<RBBINode> fFirstPosSet;
    Set<RBBINode> fLastPosSet;
    Set<RBBINode> fFollowPos;
    int fSerialNum;
    static int gLastSerial;

    RBBINode(int t2) {
        Assert.assrt(t2 < 16);
        this.fSerialNum = ++gLastSerial;
        this.fType = t2;
        this.fFirstPosSet = new HashSet<RBBINode>();
        this.fLastPosSet = new HashSet<RBBINode>();
        this.fFollowPos = new HashSet<RBBINode>();
        this.fPrecedence = t2 == 8 ? 4 : (t2 == 9 ? 3 : (t2 == 7 ? 1 : (t2 == 15 ? 2 : 0)));
    }

    RBBINode(RBBINode other) {
        this.fSerialNum = ++gLastSerial;
        this.fType = other.fType;
        this.fInputSet = other.fInputSet;
        this.fPrecedence = other.fPrecedence;
        this.fText = other.fText;
        this.fFirstPos = other.fFirstPos;
        this.fLastPos = other.fLastPos;
        this.fNullable = other.fNullable;
        this.fVal = other.fVal;
        this.fRuleRoot = false;
        this.fChainIn = other.fChainIn;
        this.fFirstPosSet = new HashSet<RBBINode>(other.fFirstPosSet);
        this.fLastPosSet = new HashSet<RBBINode>(other.fLastPosSet);
        this.fFollowPos = new HashSet<RBBINode>(other.fFollowPos);
    }

    RBBINode cloneTree() {
        RBBINode n2;
        if (this.fType == 2) {
            n2 = this.fLeftChild.cloneTree();
        } else if (this.fType == 1) {
            n2 = this;
        } else {
            n2 = new RBBINode(this);
            if (this.fLeftChild != null) {
                n2.fLeftChild = this.fLeftChild.cloneTree();
                n2.fLeftChild.fParent = n2;
            }
            if (this.fRightChild != null) {
                n2.fRightChild = this.fRightChild.cloneTree();
                n2.fRightChild.fParent = n2;
            }
        }
        return n2;
    }

    RBBINode flattenVariables() {
        if (this.fType == 2) {
            RBBINode retNode = this.fLeftChild.cloneTree();
            retNode.fRuleRoot = this.fRuleRoot;
            retNode.fChainIn = this.fChainIn;
            return retNode;
        }
        if (this.fLeftChild != null) {
            this.fLeftChild = this.fLeftChild.flattenVariables();
            this.fLeftChild.fParent = this;
        }
        if (this.fRightChild != null) {
            this.fRightChild = this.fRightChild.flattenVariables();
            this.fRightChild.fParent = this;
        }
        return this;
    }

    void flattenSets() {
        RBBINode replTree;
        RBBINode usetNode;
        RBBINode setRefNode;
        Assert.assrt(this.fType != 0);
        if (this.fLeftChild != null) {
            if (this.fLeftChild.fType == 0) {
                setRefNode = this.fLeftChild;
                usetNode = setRefNode.fLeftChild;
                replTree = usetNode.fLeftChild;
                this.fLeftChild = replTree.cloneTree();
                this.fLeftChild.fParent = this;
            } else {
                this.fLeftChild.flattenSets();
            }
        }
        if (this.fRightChild != null) {
            if (this.fRightChild.fType == 0) {
                setRefNode = this.fRightChild;
                usetNode = setRefNode.fLeftChild;
                replTree = usetNode.fLeftChild;
                this.fRightChild = replTree.cloneTree();
                this.fRightChild.fParent = this;
            } else {
                this.fRightChild.flattenSets();
            }
        }
    }

    void findNodes(List<RBBINode> dest, int kind) {
        if (this.fType == kind) {
            dest.add(this);
        }
        if (this.fLeftChild != null) {
            this.fLeftChild.findNodes(dest, kind);
        }
        if (this.fRightChild != null) {
            this.fRightChild.findNodes(dest, kind);
        }
    }

    static void printNode(RBBINode n2) {
        if (n2 == null) {
            System.out.print(" -- null --\n");
        } else {
            RBBINode.printInt(n2.fSerialNum, 10);
            RBBINode.printString(nodeTypeNames[n2.fType], 11);
            RBBINode.printInt(n2.fParent == null ? 0 : n2.fParent.fSerialNum, 11);
            RBBINode.printInt(n2.fLeftChild == null ? 0 : n2.fLeftChild.fSerialNum, 11);
            RBBINode.printInt(n2.fRightChild == null ? 0 : n2.fRightChild.fSerialNum, 12);
            RBBINode.printInt(n2.fFirstPos, 12);
            RBBINode.printInt(n2.fVal, 7);
            if (n2.fType == 2) {
                System.out.print(" " + n2.fText);
            }
        }
        System.out.println("");
    }

    static void printString(String s2, int minWidth) {
        int i2;
        for (i2 = minWidth; i2 < 0; ++i2) {
            System.out.print(' ');
        }
        for (i2 = s2.length(); i2 < minWidth; ++i2) {
            System.out.print(' ');
        }
        System.out.print(s2);
    }

    static void printInt(int i2, int minWidth) {
        String s2 = Integer.toString(i2);
        RBBINode.printString(s2, Math.max(minWidth, s2.length() + 1));
    }

    static void printHex(int i2, int minWidth) {
        String s2 = Integer.toString(i2, 16);
        String leadingZeroes = "00000".substring(0, Math.max(0, 5 - s2.length()));
        s2 = leadingZeroes + s2;
        RBBINode.printString(s2, minWidth);
    }

    void printTree(boolean printHeading) {
        if (printHeading) {
            System.out.println("-------------------------------------------------------------------");
            System.out.println("    Serial       type     Parent  LeftChild  RightChild    position  value");
        }
        RBBINode.printNode(this);
        if (this.fType != 2) {
            if (this.fLeftChild != null) {
                this.fLeftChild.printTree(false);
            }
            if (this.fRightChild != null) {
                this.fRightChild.printTree(false);
            }
        }
    }
}

