/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.RBBIDataWrapper;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.RBBINode;
import com.ibm.icu.text.RBBIRuleBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

class RBBITableBuilder {
    private RBBIRuleBuilder fRB;
    private int fRootIx;
    private List<RBBIStateDescriptor> fDStates;
    private List<short[]> fSafeTable;

    RBBITableBuilder(RBBIRuleBuilder rb, int rootNodeIx) {
        this.fRootIx = rootNodeIx;
        this.fRB = rb;
        this.fDStates = new ArrayList<RBBIStateDescriptor>();
    }

    void buildForwardTable() {
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
    }

    void calcNullable(RBBINode n2) {
        if (n2 == null) {
            return;
        }
        if (n2.fType == 0 || n2.fType == 6) {
            n2.fNullable = false;
            return;
        }
        if (n2.fType == 4 || n2.fType == 5) {
            n2.fNullable = true;
            return;
        }
        this.calcNullable(n2.fLeftChild);
        this.calcNullable(n2.fRightChild);
        n2.fNullable = n2.fType == 9 ? n2.fLeftChild.fNullable || n2.fRightChild.fNullable : (n2.fType == 8 ? n2.fLeftChild.fNullable && n2.fRightChild.fNullable : n2.fType == 10 || n2.fType == 12);
    }

    void calcFirstPos(RBBINode n2) {
        if (n2 == null) {
            return;
        }
        if (n2.fType == 3 || n2.fType == 6 || n2.fType == 4 || n2.fType == 5) {
            n2.fFirstPosSet.add(n2);
            return;
        }
        this.calcFirstPos(n2.fLeftChild);
        this.calcFirstPos(n2.fRightChild);
        if (n2.fType == 9) {
            n2.fFirstPosSet.addAll(n2.fLeftChild.fFirstPosSet);
            n2.fFirstPosSet.addAll(n2.fRightChild.fFirstPosSet);
        } else if (n2.fType == 8) {
            n2.fFirstPosSet.addAll(n2.fLeftChild.fFirstPosSet);
            if (n2.fLeftChild.fNullable) {
                n2.fFirstPosSet.addAll(n2.fRightChild.fFirstPosSet);
            }
        } else if (n2.fType == 10 || n2.fType == 12 || n2.fType == 11) {
            n2.fFirstPosSet.addAll(n2.fLeftChild.fFirstPosSet);
        }
    }

    void calcLastPos(RBBINode n2) {
        if (n2 == null) {
            return;
        }
        if (n2.fType == 3 || n2.fType == 6 || n2.fType == 4 || n2.fType == 5) {
            n2.fLastPosSet.add(n2);
            return;
        }
        this.calcLastPos(n2.fLeftChild);
        this.calcLastPos(n2.fRightChild);
        if (n2.fType == 9) {
            n2.fLastPosSet.addAll(n2.fLeftChild.fLastPosSet);
            n2.fLastPosSet.addAll(n2.fRightChild.fLastPosSet);
        } else if (n2.fType == 8) {
            n2.fLastPosSet.addAll(n2.fRightChild.fLastPosSet);
            if (n2.fRightChild.fNullable) {
                n2.fLastPosSet.addAll(n2.fLeftChild.fLastPosSet);
            }
        } else if (n2.fType == 10 || n2.fType == 12 || n2.fType == 11) {
            n2.fLastPosSet.addAll(n2.fLeftChild.fLastPosSet);
        }
    }

    void calcFollowPos(RBBINode n2) {
        if (n2 == null || n2.fType == 3 || n2.fType == 6) {
            return;
        }
        this.calcFollowPos(n2.fLeftChild);
        this.calcFollowPos(n2.fRightChild);
        if (n2.fType == 8) {
            for (RBBINode i2 : n2.fLeftChild.fLastPosSet) {
                i2.fFollowPos.addAll(n2.fRightChild.fFirstPosSet);
            }
        }
        if (n2.fType == 10 || n2.fType == 11) {
            for (RBBINode i2 : n2.fLastPosSet) {
                i2.fFollowPos.addAll(n2.fFirstPosSet);
            }
        }
    }

    void addRuleRootNodes(List<RBBINode> dest, RBBINode node) {
        if (node == null) {
            return;
        }
        if (node.fRuleRoot) {
            dest.add(node);
            return;
        }
        this.addRuleRootNodes(dest, node.fLeftChild);
        this.addRuleRootNodes(dest, node.fRightChild);
    }

    void calcChainedFollowPos(RBBINode tree) {
        ArrayList<RBBINode> endMarkerNodes = new ArrayList<RBBINode>();
        ArrayList<RBBINode> leafNodes = new ArrayList<RBBINode>();
        tree.findNodes(endMarkerNodes, 6);
        tree.findNodes(leafNodes, 3);
        ArrayList<RBBINode> ruleRootNodes = new ArrayList<RBBINode>();
        this.addRuleRootNodes(ruleRootNodes, tree);
        HashSet<RBBINode> matchStartNodes = new HashSet<RBBINode>();
        for (RBBINode node : ruleRootNodes) {
            if (!node.fChainIn) continue;
            matchStartNodes.addAll(node.fFirstPosSet);
        }
        for (RBBINode tNode : leafNodes) {
            int cLBProp;
            int c2;
            RBBINode endNode = null;
            for (RBBINode endMarkerNode : endMarkerNodes) {
                if (!tNode.fFollowPos.contains(endMarkerNode)) continue;
                endNode = tNode;
                break;
            }
            if (endNode == null || this.fRB.fLBCMNoChain && (c2 = this.fRB.fSetBuilder.getFirstChar(endNode.fVal)) != -1 && (cLBProp = UCharacter.getIntPropertyValue(c2, 4104)) == 9) continue;
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
            int a2 = 1;
            while (true) {
                if (a2 > lastInputSymbol) continue block0;
                Set<RBBINode> U = null;
                for (RBBINode p2 : T.fPositions) {
                    if (p2.fType != 3 || p2.fVal != a2) continue;
                    if (U == null) {
                        U = new HashSet<RBBINode>();
                    }
                    U.addAll(p2.fFollowPos);
                }
                int ux = 0;
                boolean UinDstates = false;
                if (U != null) {
                    Assert.assrt(U.size() > 0);
                    for (int ix = 0; ix < this.fDStates.size(); ++ix) {
                        RBBIStateDescriptor temp2 = this.fDStates.get(ix);
                        if (!U.equals(temp2.fPositions)) continue;
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
                    T.fDtran[a2] = ux;
                }
                ++a2;
            }
            break;
        }
    }

    void flagAcceptingStates() {
        ArrayList<RBBINode> endMarkerNodes = new ArrayList<RBBINode>();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(endMarkerNodes, 6);
        for (int i2 = 0; i2 < endMarkerNodes.size(); ++i2) {
            RBBINode endMarker = (RBBINode)endMarkerNodes.get(i2);
            for (int n2 = 0; n2 < this.fDStates.size(); ++n2) {
                RBBIStateDescriptor sd = this.fDStates.get(n2);
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
        for (int i2 = 0; i2 < lookAheadNodes.size(); ++i2) {
            RBBINode lookAheadNode = (RBBINode)lookAheadNodes.get(i2);
            for (int n2 = 0; n2 < this.fDStates.size(); ++n2) {
                RBBIStateDescriptor sd = this.fDStates.get(n2);
                if (!sd.fPositions.contains(lookAheadNode)) continue;
                sd.fLookAhead = lookAheadNode.fVal;
            }
        }
    }

    void flagTaggedStates() {
        ArrayList<RBBINode> tagNodes = new ArrayList<RBBINode>();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(tagNodes, 5);
        for (int i2 = 0; i2 < tagNodes.size(); ++i2) {
            RBBINode tagNode = (RBBINode)tagNodes.get(i2);
            for (int n2 = 0; n2 < this.fDStates.size(); ++n2) {
                RBBIStateDescriptor sd = this.fDStates.get(n2);
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
        for (int n2 = 0; n2 < this.fDStates.size(); ++n2) {
            RBBIStateDescriptor sd = this.fDStates.get(n2);
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

    void printPosSets(RBBINode n2) {
        if (n2 == null) {
            return;
        }
        RBBINode.printNode(n2);
        System.out.print("         Nullable:  " + n2.fNullable);
        System.out.print("         firstpos:  ");
        this.printSet(n2.fFirstPosSet);
        System.out.print("         lastpos:   ");
        this.printSet(n2.fLastPosSet);
        System.out.print("         followpos: ");
        this.printSet(n2.fFollowPos);
        this.printPosSets(n2.fLeftChild);
        this.printPosSets(n2.fRightChild);
    }

    boolean findDuplCharClassFrom(RBBIRuleBuilder.IntPair categories) {
        int numStates = this.fDStates.size();
        int numCols = this.fRB.fSetBuilder.getNumCharCategories();
        int table_base = 0;
        int table_dupl = 0;
        while (categories.first < numCols - 1) {
            categories.second = categories.first + 1;
            while (categories.second < numCols) {
                for (int state = 0; state < numStates; ++state) {
                    RBBIStateDescriptor sd = this.fDStates.get(state);
                    table_base = sd.fDtran[categories.first];
                    table_dupl = sd.fDtran[categories.second];
                    if (table_base != table_dupl) break;
                }
                if (table_base == table_dupl) {
                    return true;
                }
                ++categories.second;
            }
            ++categories.first;
        }
        return false;
    }

    void removeColumn(int column) {
        int numStates = this.fDStates.size();
        for (int state = 0; state < numStates; ++state) {
            RBBIStateDescriptor sd = this.fDStates.get(state);
            assert (column < sd.fDtran.length);
            int[] newArray = Arrays.copyOf(sd.fDtran, sd.fDtran.length - 1);
            System.arraycopy(sd.fDtran, column + 1, newArray, column, newArray.length - column);
            sd.fDtran = newArray;
        }
    }

    boolean findDuplicateState(RBBIRuleBuilder.IntPair states) {
        int numStates = this.fDStates.size();
        int numCols = this.fRB.fSetBuilder.getNumCharCategories();
        while (states.first < numStates - 1) {
            RBBIStateDescriptor firstSD = this.fDStates.get(states.first);
            states.second = states.first + 1;
            while (states.second < numStates) {
                RBBIStateDescriptor duplSD = this.fDStates.get(states.second);
                if (firstSD.fAccepting == duplSD.fAccepting && firstSD.fLookAhead == duplSD.fLookAhead && firstSD.fTagsIdx == duplSD.fTagsIdx) {
                    boolean rowsMatch = true;
                    for (int col = 0; col < numCols; ++col) {
                        int firstVal = firstSD.fDtran[col];
                        int duplVal = duplSD.fDtran[col];
                        if (firstVal == duplVal || (firstVal == states.first || firstVal == states.second) && (duplVal == states.first || duplVal == states.second)) continue;
                        rowsMatch = false;
                        break;
                    }
                    if (rowsMatch) {
                        return true;
                    }
                }
                ++states.second;
            }
            ++states.first;
        }
        return false;
    }

    boolean findDuplicateSafeState(RBBIRuleBuilder.IntPair states) {
        int numStates = this.fSafeTable.size();
        while (states.first < numStates - 1) {
            short[] firstRow = this.fSafeTable.get(states.first);
            states.second = states.first + 1;
            while (states.second < numStates) {
                short[] duplRow = this.fSafeTable.get(states.second);
                boolean rowsMatch = true;
                int numCols = firstRow.length;
                for (int col = 0; col < numCols; ++col) {
                    short firstVal = firstRow[col];
                    short duplVal = duplRow[col];
                    if (firstVal == duplVal || (firstVal == states.first || firstVal == states.second) && (duplVal == states.first || duplVal == states.second)) continue;
                    rowsMatch = false;
                    break;
                }
                if (rowsMatch) {
                    return true;
                }
                ++states.second;
            }
            ++states.first;
        }
        return false;
    }

    void removeState(RBBIRuleBuilder.IntPair duplStates) {
        int keepState = duplStates.first;
        int duplState = duplStates.second;
        assert (keepState < duplState);
        assert (duplState < this.fDStates.size());
        this.fDStates.remove(duplState);
        int numStates = this.fDStates.size();
        int numCols = this.fRB.fSetBuilder.getNumCharCategories();
        for (int state = 0; state < numStates; ++state) {
            RBBIStateDescriptor sd = this.fDStates.get(state);
            for (int col = 0; col < numCols; ++col) {
                int existingVal;
                int newVal = existingVal = sd.fDtran[col];
                if (existingVal == duplState) {
                    newVal = keepState;
                } else if (existingVal > duplState) {
                    newVal = existingVal - 1;
                }
                sd.fDtran[col] = newVal;
            }
            if (sd.fAccepting == duplState) {
                sd.fAccepting = keepState;
            } else if (sd.fAccepting > duplState) {
                --sd.fAccepting;
            }
            if (sd.fLookAhead == duplState) {
                sd.fLookAhead = keepState;
                continue;
            }
            if (sd.fLookAhead <= duplState) continue;
            --sd.fLookAhead;
        }
    }

    void removeSafeState(RBBIRuleBuilder.IntPair duplStates) {
        int keepState = duplStates.first;
        int duplState = duplStates.second;
        assert (keepState < duplState);
        assert (duplState < this.fSafeTable.size());
        this.fSafeTable.remove(duplState);
        int numStates = this.fSafeTable.size();
        for (int state = 0; state < numStates; ++state) {
            short[] row = this.fSafeTable.get(state);
            for (int col = 0; col < row.length; ++col) {
                int existingVal;
                int newVal = existingVal = row[col];
                if (existingVal == duplState) {
                    newVal = keepState;
                } else if (existingVal > duplState) {
                    newVal = existingVal - 1;
                }
                row[col] = (short)newVal;
            }
        }
    }

    void removeDuplicateStates() {
        RBBIRuleBuilder.IntPair dupls = new RBBIRuleBuilder.IntPair(3, 0);
        while (this.findDuplicateState(dupls)) {
            this.removeState(dupls);
        }
    }

    int getTableSize() {
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return 0;
        }
        int size = 16;
        int numRows = this.fDStates.size();
        int numCols = this.fRB.fSetBuilder.getNumCharCategories();
        int rowSize = 8 + 2 * numCols;
        size += numRows * rowSize;
        size = size + 7 & 0xFFFFFFF8;
        return size;
    }

    RBBIDataWrapper.RBBIStateTable exportTable() {
        RBBIDataWrapper.RBBIStateTable table = new RBBIDataWrapper.RBBIStateTable();
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return table;
        }
        Assert.assrt(this.fRB.fSetBuilder.getNumCharCategories() < Short.MAX_VALUE && this.fDStates.size() < Short.MAX_VALUE);
        table.fNumStates = this.fDStates.size();
        int rowLen = 4 + this.fRB.fSetBuilder.getNumCharCategories();
        int tableSize = (this.getTableSize() - 16) / 2;
        table.fTable = new short[tableSize];
        table.fRowLen = rowLen * 2;
        if (this.fRB.fLookAheadHardBreak) {
            table.fFlags |= 1;
        }
        if (this.fRB.fSetBuilder.sawBOF()) {
            table.fFlags |= 2;
        }
        int numCharCategories = this.fRB.fSetBuilder.getNumCharCategories();
        for (int state = 0; state < table.fNumStates; ++state) {
            RBBIStateDescriptor sd = this.fDStates.get(state);
            int row = state * rowLen;
            Assert.assrt(Short.MIN_VALUE < sd.fAccepting && sd.fAccepting <= Short.MAX_VALUE);
            Assert.assrt(Short.MIN_VALUE < sd.fLookAhead && sd.fLookAhead <= Short.MAX_VALUE);
            table.fTable[row + 0] = (short)sd.fAccepting;
            table.fTable[row + 1] = (short)sd.fLookAhead;
            table.fTable[row + 2] = (short)sd.fTagsIdx;
            for (int col = 0; col < numCharCategories; ++col) {
                table.fTable[row + 4 + col] = (short)sd.fDtran[col];
            }
        }
        return table;
    }

    void buildSafeReverseTable() {
        StringBuilder safePairs = new StringBuilder();
        int numCharClasses = this.fRB.fSetBuilder.getNumCharCategories();
        int numStates = this.fDStates.size();
        for (int c1 = 0; c1 < numCharClasses; ++c1) {
            for (int c2 = 0; c2 < numCharClasses; ++c2) {
                int wantedEndState = -1;
                int endState = 0;
                for (int startState = 1; startState < numStates; ++startState) {
                    RBBIStateDescriptor startStateD = this.fDStates.get(startState);
                    int s2 = startStateD.fDtran[c1];
                    RBBIStateDescriptor s2StateD = this.fDStates.get(s2);
                    endState = s2StateD.fDtran[c2];
                    if (wantedEndState < 0) {
                        wantedEndState = endState;
                        continue;
                    }
                    if (wantedEndState != endState) break;
                }
                if (wantedEndState != endState) continue;
                safePairs.append((char)c1);
                safePairs.append((char)c2);
            }
        }
        assert (this.fSafeTable == null);
        this.fSafeTable = new ArrayList<short[]>();
        for (int row = 0; row < numCharClasses + 2; ++row) {
            this.fSafeTable.add(new short[numCharClasses]);
        }
        short[] startState = this.fSafeTable.get(1);
        for (int charClass = 0; charClass < numCharClasses; ++charClass) {
            startState[charClass] = (short)(charClass + 2);
        }
        for (int row = 2; row < numCharClasses + 2; ++row) {
            System.arraycopy(startState, 0, this.fSafeTable.get(row), 0, startState.length);
        }
        for (int pairIdx = 0; pairIdx < safePairs.length(); pairIdx += 2) {
            char c1 = safePairs.charAt(pairIdx);
            char c2 = safePairs.charAt(pairIdx + 1);
            short[] rowState = this.fSafeTable.get(c2 + 2);
            rowState[c1] = 0;
        }
        RBBIRuleBuilder.IntPair states = new RBBIRuleBuilder.IntPair(1, 0);
        while (this.findDuplicateSafeState(states)) {
            this.removeSafeState(states);
        }
    }

    int getSafeTableSize() {
        if (this.fSafeTable == null) {
            return 0;
        }
        int size = 16;
        int numRows = this.fSafeTable.size();
        int numCols = this.fSafeTable.get(0).length;
        int rowSize = 8 + 2 * numCols;
        size += numRows * rowSize;
        size = size + 7 & 0xFFFFFFF8;
        return size;
    }

    RBBIDataWrapper.RBBIStateTable exportSafeTable() {
        RBBIDataWrapper.RBBIStateTable table = new RBBIDataWrapper.RBBIStateTable();
        table.fNumStates = this.fSafeTable.size();
        int numCharCategories = this.fSafeTable.get(0).length;
        int rowLen = 4 + numCharCategories;
        int tableSize = (this.getSafeTableSize() - 16) / 2;
        table.fTable = new short[tableSize];
        table.fRowLen = rowLen * 2;
        for (int state = 0; state < table.fNumStates; ++state) {
            short[] rowArray = this.fSafeTable.get(state);
            int row = state * rowLen;
            for (int col = 0; col < numCharCategories; ++col) {
                table.fTable[row + 4 + col] = rowArray[col];
            }
        }
        return table;
    }

    void printSet(Collection<RBBINode> s2) {
        for (RBBINode n2 : s2) {
            RBBINode.printInt(n2.fSerialNum, 8);
        }
        System.out.println();
    }

    void printStates() {
        int c2;
        System.out.print("state |           i n p u t     s y m b o l s \n");
        System.out.print("      | Acc  LA    Tag");
        for (c2 = 0; c2 < this.fRB.fSetBuilder.getNumCharCategories(); ++c2) {
            RBBINode.printInt(c2, 3);
        }
        System.out.print("\n");
        System.out.print("      |---------------");
        for (c2 = 0; c2 < this.fRB.fSetBuilder.getNumCharCategories(); ++c2) {
            System.out.print("---");
        }
        System.out.print("\n");
        for (int n2 = 0; n2 < this.fDStates.size(); ++n2) {
            RBBIStateDescriptor sd = this.fDStates.get(n2);
            RBBINode.printInt(n2, 5);
            System.out.print(" | ");
            RBBINode.printInt(sd.fAccepting, 3);
            RBBINode.printInt(sd.fLookAhead, 4);
            RBBINode.printInt(sd.fTagsIdx, 6);
            System.out.print(" ");
            for (c2 = 0; c2 < this.fRB.fSetBuilder.getNumCharCategories(); ++c2) {
                RBBINode.printInt(sd.fDtran[c2], 3);
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    void printReverseTable() {
        int c2;
        System.out.printf("    Safe Reverse Table \n", new Object[0]);
        if (this.fSafeTable == null) {
            System.out.printf("   --- nullptr ---\n", new Object[0]);
            return;
        }
        int numCharCategories = this.fSafeTable.get(0).length;
        System.out.printf("state |           i n p u t     s y m b o l s \n", new Object[0]);
        System.out.printf("      | Acc  LA    Tag", new Object[0]);
        for (c2 = 0; c2 < numCharCategories; ++c2) {
            System.out.printf(" %2d", c2);
        }
        System.out.printf("\n", new Object[0]);
        System.out.printf("      |---------------", new Object[0]);
        for (c2 = 0; c2 < numCharCategories; ++c2) {
            System.out.printf("---", new Object[0]);
        }
        System.out.printf("\n", new Object[0]);
        for (int n2 = 0; n2 < this.fSafeTable.size(); ++n2) {
            short[] rowArray = this.fSafeTable.get(n2);
            System.out.printf("  %3d | ", n2);
            System.out.printf("%3d %3d %5d ", 0, 0, 0);
            for (c2 = 0; c2 < numCharCategories; ++c2) {
                System.out.printf(" %2d", rowArray[c2]);
            }
            System.out.printf("\n", new Object[0]);
        }
        System.out.printf("\n\n", new Object[0]);
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
            for (int i2 = thisRecord + 1; i2 < nextRecord; ++i2) {
                int val2 = tbl.get(i2);
                RBBINode.printInt(val2, 7);
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

