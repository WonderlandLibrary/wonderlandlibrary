/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.RBBINode;
import com.ibm.icu.text.RBBIRuleBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class RBBITableBuilder {
    private RBBIRuleBuilder fRB;
    private int fRootIx;
    private List<RBBIStateDescriptor> fDStates;

    RBBITableBuilder(RBBIRuleBuilder rb, int rootNodeIx) {
        this.fRootIx = rootNodeIx;
        this.fRB = rb;
        this.fDStates = new ArrayList<RBBIStateDescriptor>();
    }

    void build() {
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return;
        }
        this.fRB.fTreeRoots[this.fRootIx] = this.fRB.fTreeRoots[this.fRootIx].flattenVariables();
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("ftree") >= 0) {
            System.out.println("Parse tree after flattening variable references.");
            this.fRB.fTreeRoots[this.fRootIx].printTree(true);
        }
        if (this.fRB.fSetBuilder.sawBOF()) {
            RBBINode bofLeaf;
            RBBINode bofTop = new RBBINode(8);
            bofTop.fLeftChild = bofLeaf = new RBBINode(3);
            bofTop.fRightChild = this.fRB.fTreeRoots[this.fRootIx];
            bofLeaf.fParent = bofTop;
            bofLeaf.fVal = 2;
            this.fRB.fTreeRoots[this.fRootIx] = bofTop;
        }
        RBBINode cn = new RBBINode(8);
        cn.fLeftChild = this.fRB.fTreeRoots[this.fRootIx];
        this.fRB.fTreeRoots[this.fRootIx].fParent = cn;
        cn.fRightChild = new RBBINode(6);
        cn.fRightChild.fParent = cn;
        this.fRB.fTreeRoots[this.fRootIx] = cn;
        this.fRB.fTreeRoots[this.fRootIx].flattenSets();
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("stree") >= 0) {
            System.out.println("Parse tree after flattening Unicode Set references.");
            this.fRB.fTreeRoots[this.fRootIx].printTree(true);
        }
        this.calcNullable(this.fRB.fTreeRoots[this.fRootIx]);
        this.calcFirstPos(this.fRB.fTreeRoots[this.fRootIx]);
        this.calcLastPos(this.fRB.fTreeRoots[this.fRootIx]);
        this.calcFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("pos") >= 0) {
            System.out.print("\n");
            this.printPosSets(this.fRB.fTreeRoots[this.fRootIx]);
        }
        if (this.fRB.fChainRules) {
            this.calcChainedFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
        }
        if (this.fRB.fSetBuilder.sawBOF()) {
            this.bofFixup();
        }
        this.buildStateTable();
        this.flagAcceptingStates();
        this.flagLookAheadStates();
        this.flagTaggedStates();
        this.mergeRuleStatusVals();
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("states") >= 0) {
            this.printStates();
        }
    }

    void calcNullable(RBBINode n) {
        if (n == null) {
            return;
        }
        if (n.fType == 0 || n.fType == 6) {
            n.fNullable = false;
            return;
        }
        if (n.fType == 4 || n.fType == 5) {
            n.fNullable = true;
            return;
        }
        this.calcNullable(n.fLeftChild);
        this.calcNullable(n.fRightChild);
        n.fNullable = n.fType == 9 ? n.fLeftChild.fNullable || n.fRightChild.fNullable : (n.fType == 8 ? n.fLeftChild.fNullable && n.fRightChild.fNullable : n.fType == 10 || n.fType == 12);
    }

    void calcFirstPos(RBBINode n) {
        if (n == null) {
            return;
        }
        if (n.fType == 3 || n.fType == 6 || n.fType == 4 || n.fType == 5) {
            n.fFirstPosSet.add(n);
            return;
        }
        this.calcFirstPos(n.fLeftChild);
        this.calcFirstPos(n.fRightChild);
        if (n.fType == 9) {
            n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
            n.fFirstPosSet.addAll(n.fRightChild.fFirstPosSet);
        } else if (n.fType == 8) {
            n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
            if (n.fLeftChild.fNullable) {
                n.fFirstPosSet.addAll(n.fRightChild.fFirstPosSet);
            }
        } else if (n.fType == 10 || n.fType == 12 || n.fType == 11) {
            n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
        }
    }

    void calcLastPos(RBBINode n) {
        if (n == null) {
            return;
        }
        if (n.fType == 3 || n.fType == 6 || n.fType == 4 || n.fType == 5) {
            n.fLastPosSet.add(n);
            return;
        }
        this.calcLastPos(n.fLeftChild);
        this.calcLastPos(n.fRightChild);
        if (n.fType == 9) {
            n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
            n.fLastPosSet.addAll(n.fRightChild.fLastPosSet);
        } else if (n.fType == 8) {
            n.fLastPosSet.addAll(n.fRightChild.fLastPosSet);
            if (n.fRightChild.fNullable) {
                n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
            }
        } else if (n.fType == 10 || n.fType == 12 || n.fType == 11) {
            n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
        }
    }

    void calcFollowPos(RBBINode n) {
        if (n == null || n.fType == 3 || n.fType == 6) {
            return;
        }
        this.calcFollowPos(n.fLeftChild);
        this.calcFollowPos(n.fRightChild);
        if (n.fType == 8) {
            for (RBBINode i : n.fLeftChild.fLastPosSet) {
                i.fFollowPos.addAll(n.fRightChild.fFirstPosSet);
            }
        }
        if (n.fType == 10 || n.fType == 11) {
            for (RBBINode i : n.fLastPosSet) {
                i.fFollowPos.addAll(n.fFirstPosSet);
            }
        }
    }

    void calcChainedFollowPos(RBBINode tree) {
        ArrayList<RBBINode> endMarkerNodes = new ArrayList<RBBINode>();
        ArrayList<RBBINode> leafNodes = new ArrayList<RBBINode>();
        tree.findNodes(endMarkerNodes, 6);
        tree.findNodes(leafNodes, 3);
        RBBINode userRuleRoot = tree;
        if (this.fRB.fSetBuilder.sawBOF()) {
            userRuleRoot = tree.fLeftChild.fRightChild;
        }
        Assert.assrt(userRuleRoot != null);
        Set<RBBINode> matchStartNodes = userRuleRoot.fFirstPosSet;
        for (RBBINode tNode : leafNodes) {
            int cLBProp;
            int c;
            RBBINode endNode = null;
            for (RBBINode endMarkerNode : endMarkerNodes) {
                if (!tNode.fFollowPos.contains(endMarkerNode)) continue;
                endNode = tNode;
                break;
            }
            if (endNode == null || this.fRB.fLBCMNoChain && (c = this.fRB.fSetBuilder.getFirstChar(endNode.fVal)) != -1 && (cLBProp = UCharacter.getIntPropertyValue(c, 4104)) == 9) continue;
            for (RBBINode startNode : matchStartNodes) {
                if (startNode.fType != 3 || endNode.fVal != startNode.fVal) continue;
                endNode.fFollowPos.addAll(startNode.fFollowPos);
            }
        }
    }

    void bofFixup() {
        RBBINode bofNode = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fLeftChild;
        Assert.assrt(bofNode.fType == 3);
        Assert.assrt(bofNode.fVal == 2);
        Set<RBBINode> matchStartNodes = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fRightChild.fFirstPosSet;
        for (RBBINode startNode : matchStartNodes) {
            if (startNode.fType != 3 || startNode.fVal != bofNode.fVal) continue;
            bofNode.fFollowPos.addAll(startNode.fFollowPos);
        }
    }

    void buildStateTable() {
        int lastInputSymbol = this.fRB.fSetBuilder.getNumCharCategories() - 1;
        RBBIStateDescriptor failState = new RBBIStateDescriptor(lastInputSymbol);
        this.fDStates.add(failState);
        RBBIStateDescriptor initialState = new RBBIStateDescriptor(lastInputSymbol);
        initialState.fPositions.addAll(this.fRB.fTreeRoots[this.fRootIx].fFirstPosSet);
        this.fDStates.add(initialState);
        block0: while (true) {
            RBBIStateDescriptor T = null;
            for (int tx = 1; tx < this.fDStates.size(); ++tx) {
                RBBIStateDescriptor temp = this.fDStates.get(tx);
                if (temp.fMarked) continue;
                T = temp;
                break;
            }
            if (T == null) break;
            T.fMarked = true;
            int a = 1;
            while (true) {
                if (a > lastInputSymbol) continue block0;
                Set<RBBINode> U = null;
                for (RBBINode p : T.fPositions) {
                    if (p.fType != 3 || p.fVal != a) continue;
                    if (U == null) {
                        U = new HashSet<RBBINode>();
                    }
                    U.addAll(p.fFollowPos);
                }
                int ux = 0;
                boolean UinDstates = false;
                if (U != null) {
                    Assert.assrt(U.size() > 0);
                    for (int ix = 0; ix < this.fDStates.size(); ++ix) {
                        RBBIStateDescriptor temp2 = this.fDStates.get(ix);
                        if (!((Object)U).equals(temp2.fPositions)) continue;
                        U = temp2.fPositions;
                        ux = ix;
                        UinDstates = true;
                        break;
                    }
                    if (!UinDstates) {
                        RBBIStateDescriptor newState = new RBBIStateDescriptor(lastInputSymbol);
                        newState.fPositions = U;
                        this.fDStates.add(newState);
                        ux = this.fDStates.size() - 1;
                    }
                    T.fDtran[a] = ux;
                }
                ++a;
            }
            break;
        }
    }

    void flagAcceptingStates() {
        ArrayList<RBBINode> endMarkerNodes = new ArrayList<RBBINode>();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(endMarkerNodes, 6);
        for (int i = 0; i < endMarkerNodes.size(); ++i) {
            RBBINode endMarker = (RBBINode)endMarkerNodes.get(i);
            for (int n = 0; n < this.fDStates.size(); ++n) {
                RBBIStateDescriptor sd = this.fDStates.get(n);
                if (!sd.fPositions.contains(endMarker)) continue;
                if (sd.fAccepting == 0) {
                    sd.fAccepting = endMarker.fVal;
                    if (sd.fAccepting == 0) {
                        sd.fAccepting = -1;
                    }
                }
                if (sd.fAccepting == -1 && endMarker.fVal != 0) {
                    sd.fAccepting = endMarker.fVal;
                }
                if (!endMarker.fLookAheadEnd) continue;
                sd.fLookAhead = sd.fAccepting;
            }
        }
    }

    void flagLookAheadStates() {
        ArrayList<RBBINode> lookAheadNodes = new ArrayList<RBBINode>();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(lookAheadNodes, 4);
        for (int i = 0; i < lookAheadNodes.size(); ++i) {
            RBBINode lookAheadNode = (RBBINode)lookAheadNodes.get(i);
            for (int n = 0; n < this.fDStates.size(); ++n) {
                RBBIStateDescriptor sd = this.fDStates.get(n);
                if (!sd.fPositions.contains(lookAheadNode)) continue;
                sd.fLookAhead = lookAheadNode.fVal;
            }
        }
    }

    void flagTaggedStates() {
        ArrayList<RBBINode> tagNodes = new ArrayList<RBBINode>();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(tagNodes, 5);
        for (int i = 0; i < tagNodes.size(); ++i) {
            RBBINode tagNode = (RBBINode)tagNodes.get(i);
            for (int n = 0; n < this.fDStates.size(); ++n) {
                RBBIStateDescriptor sd = this.fDStates.get(n);
                if (!sd.fPositions.contains(tagNode)) continue;
                sd.fTagVals.add(tagNode.fVal);
            }
        }
    }

    void mergeRuleStatusVals() {
        if (this.fRB.fRuleStatusVals.size() == 0) {
            this.fRB.fRuleStatusVals.add(1);
            this.fRB.fRuleStatusVals.add(0);
            TreeSet s0 = new TreeSet();
            Integer izero = 0;
            this.fRB.fStatusSets.put(s0, izero);
            TreeSet<Integer> s1 = new TreeSet<Integer>();
            s1.add(izero);
            this.fRB.fStatusSets.put(s0, izero);
        }
        for (int n = 0; n < this.fDStates.size(); ++n) {
            RBBIStateDescriptor sd = this.fDStates.get(n);
            SortedSet<Integer> statusVals = sd.fTagVals;
            Integer arrayIndexI = this.fRB.fStatusSets.get(statusVals);
            if (arrayIndexI == null) {
                arrayIndexI = this.fRB.fRuleStatusVals.size();
                this.fRB.fStatusSets.put(statusVals, arrayIndexI);
                this.fRB.fRuleStatusVals.add(statusVals.size());
                this.fRB.fRuleStatusVals.addAll(statusVals);
            }
            sd.fTagsIdx = arrayIndexI;
        }
    }

    void printPosSets(RBBINode n) {
        if (n == null) {
            return;
        }
        RBBINode.printNode(n);
        System.out.print("         Nullable:  " + n.fNullable);
        System.out.print("         firstpos:  ");
        this.printSet(n.fFirstPosSet);
        System.out.print("         lastpos:   ");
        this.printSet(n.fLastPosSet);
        System.out.print("         followpos: ");
        this.printSet(n.fFollowPos);
        this.printPosSets(n.fLeftChild);
        this.printPosSets(n.fRightChild);
    }

    int getTableSize() {
        int size = 0;
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return 0;
        }
        size = 16;
        int numRows = this.fDStates.size();
        int numCols = this.fRB.fSetBuilder.getNumCharCategories();
        int rowSize = 8 + 2 * numCols;
        size += numRows * rowSize;
        while (size % 8 > 0) {
            ++size;
        }
        return size;
    }

    short[] exportTable() {
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return new short[0];
        }
        Assert.assrt(this.fRB.fSetBuilder.getNumCharCategories() < 32767 && this.fDStates.size() < 32767);
        int numStates = this.fDStates.size();
        int rowLen = 4 + this.fRB.fSetBuilder.getNumCharCategories();
        int tableSize = this.getTableSize() / 2;
        short[] table = new short[tableSize];
        table[0] = (short)(numStates >>> 16);
        table[1] = (short)(numStates & 0xFFFF);
        table[2] = (short)(rowLen >>> 16);
        table[3] = (short)(rowLen & 0xFFFF);
        int flags = 0;
        if (this.fRB.fLookAheadHardBreak) {
            flags |= 1;
        }
        if (this.fRB.fSetBuilder.sawBOF()) {
            flags |= 2;
        }
        table[4] = (short)(flags >>> 16);
        table[5] = (short)(flags & 0xFFFF);
        int numCharCategories = this.fRB.fSetBuilder.getNumCharCategories();
        for (int state = 0; state < numStates; ++state) {
            RBBIStateDescriptor sd = this.fDStates.get(state);
            int row = 8 + state * rowLen;
            Assert.assrt(-32768 < sd.fAccepting && sd.fAccepting <= 32767);
            Assert.assrt(-32768 < sd.fLookAhead && sd.fLookAhead <= 32767);
            table[row + 0] = (short)sd.fAccepting;
            table[row + 1] = (short)sd.fLookAhead;
            table[row + 2] = (short)sd.fTagsIdx;
            for (int col = 0; col < numCharCategories; ++col) {
                table[row + 4 + col] = (short)sd.fDtran[col];
            }
        }
        return table;
    }

    void printSet(Collection<RBBINode> s) {
        for (RBBINode n : s) {
            RBBINode.printInt(n.fSerialNum, 8);
        }
        System.out.println();
    }

    void printStates() {
        int c;
        System.out.print("state |           i n p u t     s y m b o l s \n");
        System.out.print("      | Acc  LA    Tag");
        for (c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); ++c) {
            RBBINode.printInt(c, 3);
        }
        System.out.print("\n");
        System.out.print("      |---------------");
        for (c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); ++c) {
            System.out.print("---");
        }
        System.out.print("\n");
        for (int n = 0; n < this.fDStates.size(); ++n) {
            RBBIStateDescriptor sd = this.fDStates.get(n);
            RBBINode.printInt(n, 5);
            System.out.print(" | ");
            RBBINode.printInt(sd.fAccepting, 3);
            RBBINode.printInt(sd.fLookAhead, 4);
            RBBINode.printInt(sd.fTagsIdx, 6);
            System.out.print(" ");
            for (c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); ++c) {
                RBBINode.printInt(sd.fDtran[c], 3);
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    void printRuleStatusTable() {
        int thisRecord = 0;
        int nextRecord = 0;
        List<Integer> tbl = this.fRB.fRuleStatusVals;
        System.out.print("index |  tags \n");
        System.out.print("-------------------\n");
        while (nextRecord < tbl.size()) {
            thisRecord = nextRecord;
            nextRecord = thisRecord + tbl.get(thisRecord) + 1;
            RBBINode.printInt(thisRecord, 7);
            for (int i = thisRecord + 1; i < nextRecord; ++i) {
                int val = tbl.get(i);
                RBBINode.printInt(val, 7);
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    static class RBBIStateDescriptor {
        boolean fMarked;
        int fAccepting;
        int fLookAhead;
        SortedSet<Integer> fTagVals = new TreeSet<Integer>();
        int fTagsIdx;
        Set<RBBINode> fPositions = new HashSet<RBBINode>();
        int[] fDtran;

        RBBIStateDescriptor(int maxInputSymbol) {
            this.fDtran = new int[maxInputSymbol + 1];
        }
    }
}

