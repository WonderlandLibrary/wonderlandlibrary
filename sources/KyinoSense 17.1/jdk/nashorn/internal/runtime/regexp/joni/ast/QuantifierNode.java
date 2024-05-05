/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StateNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

public final class QuantifierNode
extends StateNode {
    public Node target;
    public int lower;
    public int upper;
    public boolean greedy;
    public int targetEmptyInfo;
    public Node headExact;
    public Node nextHeadExact;
    public boolean isRefered;
    private static final ReduceType[][] REDUCE_TABLE = new ReduceType[][]{{ReduceType.DEL, ReduceType.A, ReduceType.A, ReduceType.QQ, ReduceType.AQ, ReduceType.ASIS}, {ReduceType.DEL, ReduceType.DEL, ReduceType.DEL, ReduceType.P_QQ, ReduceType.P_QQ, ReduceType.DEL}, {ReduceType.A, ReduceType.A, ReduceType.DEL, ReduceType.ASIS, ReduceType.P_QQ, ReduceType.DEL}, {ReduceType.DEL, ReduceType.AQ, ReduceType.AQ, ReduceType.DEL, ReduceType.AQ, ReduceType.AQ}, {ReduceType.DEL, ReduceType.DEL, ReduceType.DEL, ReduceType.DEL, ReduceType.DEL, ReduceType.DEL}, {ReduceType.ASIS, ReduceType.PQ_Q, ReduceType.DEL, ReduceType.AQ, ReduceType.AQ, ReduceType.DEL}};
    private static final String[] PopularQStr = new String[]{"?", "*", "+", "??", "*?", "+?"};
    private static final String[] ReduceQStr = new String[]{"", "", "*", "*?", "??", "+ and ??", "+? and ?"};
    public static final int REPEAT_INFINITE = -1;

    public QuantifierNode(int lower, int upper, boolean byNumber) {
        this.lower = lower;
        this.upper = upper;
        this.greedy = true;
        this.targetEmptyInfo = 0;
        if (byNumber) {
            this.setByNumber();
        }
    }

    @Override
    public int getType() {
        return 5;
    }

    @Override
    protected void setChild(Node newChild) {
        this.target = newChild;
    }

    @Override
    protected Node getChild() {
        return this.target;
    }

    public void setTarget(Node tgt) {
        this.target = tgt;
        tgt.parent = this;
    }

    public StringNode convertToString(int flag) {
        StringNode sn = new StringNode();
        sn.flag = flag;
        sn.swap(this);
        return sn;
    }

    @Override
    public String getName() {
        return "Quantifier";
    }

    @Override
    public String toString(int level) {
        StringBuilder value = new StringBuilder(super.toString(level));
        value.append("\n  target: " + QuantifierNode.pad(this.target, level + 1));
        value.append("\n  lower: " + this.lower);
        value.append("\n  upper: " + this.upper);
        value.append("\n  greedy: " + this.greedy);
        value.append("\n  targetEmptyInfo: " + this.targetEmptyInfo);
        value.append("\n  headExact: " + QuantifierNode.pad(this.headExact, level + 1));
        value.append("\n  nextHeadExact: " + QuantifierNode.pad(this.nextHeadExact, level + 1));
        value.append("\n  isRefered: " + this.isRefered);
        return value.toString();
    }

    public boolean isAnyCharStar() {
        return this.greedy && QuantifierNode.isRepeatInfinite(this.upper) && this.target.getType() == 3;
    }

    protected int popularNum() {
        if (this.greedy) {
            if (this.lower == 0) {
                if (this.upper == 1) {
                    return 0;
                }
                if (QuantifierNode.isRepeatInfinite(this.upper)) {
                    return 1;
                }
            } else if (this.lower == 1 && QuantifierNode.isRepeatInfinite(this.upper)) {
                return 2;
            }
        } else if (this.lower == 0) {
            if (this.upper == 1) {
                return 3;
            }
            if (QuantifierNode.isRepeatInfinite(this.upper)) {
                return 4;
            }
        } else if (this.lower == 1 && QuantifierNode.isRepeatInfinite(this.upper)) {
            return 5;
        }
        return -1;
    }

    protected void set(QuantifierNode other) {
        this.setTarget(other.target);
        other.target = null;
        this.lower = other.lower;
        this.upper = other.upper;
        this.greedy = other.greedy;
        this.targetEmptyInfo = other.targetEmptyInfo;
        this.headExact = other.headExact;
        this.nextHeadExact = other.nextHeadExact;
        this.isRefered = other.isRefered;
    }

    public void reduceNestedQuantifier(QuantifierNode other) {
        int pnum = this.popularNum();
        int cnum = other.popularNum();
        if (pnum < 0 || cnum < 0) {
            return;
        }
        switch (REDUCE_TABLE[cnum][pnum]) {
            case DEL: {
                this.set(other);
                break;
            }
            case A: {
                this.setTarget(other.target);
                this.lower = 0;
                this.upper = -1;
                this.greedy = true;
                break;
            }
            case AQ: {
                this.setTarget(other.target);
                this.lower = 0;
                this.upper = -1;
                this.greedy = false;
                break;
            }
            case QQ: {
                this.setTarget(other.target);
                this.lower = 0;
                this.upper = 1;
                this.greedy = false;
                break;
            }
            case P_QQ: {
                this.setTarget(other);
                this.lower = 0;
                this.upper = 1;
                this.greedy = false;
                other.lower = 1;
                other.upper = -1;
                other.greedy = true;
                return;
            }
            case PQ_Q: {
                this.setTarget(other);
                this.lower = 0;
                this.upper = 1;
                this.greedy = true;
                other.lower = 1;
                other.upper = -1;
                other.greedy = false;
                return;
            }
            case ASIS: {
                this.setTarget(other);
                return;
            }
        }
        other.target = null;
    }

    public int setQuantifier(Node tgt, boolean group, ScanEnvironment env, char[] chars, int p, int end) {
        if (this.lower == 1 && this.upper == 1) {
            return 1;
        }
        switch (tgt.getType()) {
            case 0: {
                StringNode n;
                StringNode sn;
                if (group || !(sn = (StringNode)tgt).canBeSplit() || (n = sn.splitLastChar()) == null) break;
                this.setTarget(n);
                return 2;
            }
            case 5: {
                QuantifierNode qnt = (QuantifierNode)tgt;
                int nestQNum = this.popularNum();
                int targetQNum = qnt.popularNum();
                if (targetQNum < 0) break;
                if (nestQNum >= 0) {
                    this.reduceNestedQuantifier(qnt);
                    return 0;
                }
                if (targetQNum != 1 && targetQNum != 2 || QuantifierNode.isRepeatInfinite(this.upper) || this.upper <= 1 || !this.greedy) break;
                this.upper = this.lower == 0 ? 1 : this.lower;
            }
        }
        this.setTarget(tgt);
        return 0;
    }

    public static boolean isRepeatInfinite(int n) {
        return n == -1;
    }

    static enum ReduceType {
        ASIS,
        DEL,
        A,
        AQ,
        QQ,
        P_QQ,
        PQ_Q;

    }
}

