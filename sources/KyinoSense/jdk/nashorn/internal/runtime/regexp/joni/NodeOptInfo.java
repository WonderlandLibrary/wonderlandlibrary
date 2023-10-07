/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.MinMaxLen;
import jdk.nashorn.internal.runtime.regexp.joni.OptAnchorInfo;
import jdk.nashorn.internal.runtime.regexp.joni.OptEnvironment;
import jdk.nashorn.internal.runtime.regexp.joni.OptExactInfo;
import jdk.nashorn.internal.runtime.regexp.joni.OptMapInfo;

public final class NodeOptInfo {
    final MinMaxLen length = new MinMaxLen();
    final OptAnchorInfo anchor = new OptAnchorInfo();
    final OptExactInfo exb = new OptExactInfo();
    final OptExactInfo exm = new OptExactInfo();
    final OptExactInfo expr = new OptExactInfo();
    final OptMapInfo map = new OptMapInfo();

    public void setBoundNode(MinMaxLen mmd) {
        this.exb.mmd.copy(mmd);
        this.expr.mmd.copy(mmd);
        this.map.mmd.copy(mmd);
    }

    public void clear() {
        this.length.clear();
        this.anchor.clear();
        this.exb.clear();
        this.exm.clear();
        this.expr.clear();
        this.map.clear();
    }

    public void copy(NodeOptInfo other) {
        this.length.copy(other.length);
        this.anchor.copy(other.anchor);
        this.exb.copy(other.exb);
        this.exm.copy(other.exm);
        this.expr.copy(other.expr);
        this.map.copy(other.map);
    }

    public void concatLeftNode(NodeOptInfo other) {
        OptAnchorInfo tanchor = new OptAnchorInfo();
        tanchor.concat(this.anchor, other.anchor, this.length.max, other.length.max);
        this.anchor.copy(tanchor);
        if (other.exb.length > 0 && this.length.max == 0) {
            tanchor.concat(this.anchor, other.exb.anchor, this.length.max, other.length.max);
            other.exb.anchor.copy(tanchor);
        }
        if (other.map.value > 0 && this.length.max == 0 && other.map.mmd.max == 0) {
            other.map.anchor.leftAnchor |= this.anchor.leftAnchor;
        }
        boolean exbReach = this.exb.reachEnd;
        boolean exmReach = this.exm.reachEnd;
        if (other.length.max != 0) {
            this.exm.reachEnd = false;
            this.exb.reachEnd = false;
        }
        if (other.exb.length > 0) {
            if (exbReach) {
                this.exb.concat(other.exb);
                other.exb.clear();
            } else if (exmReach) {
                this.exm.concat(other.exb);
                other.exb.clear();
            }
        }
        this.exm.select(other.exb);
        this.exm.select(other.exm);
        if (this.expr.length > 0) {
            if (other.length.max > 0) {
                int otherLengthMax = other.length.max;
                if (otherLengthMax == Integer.MAX_VALUE) {
                    otherLengthMax = -1;
                }
                if (this.expr.length > otherLengthMax) {
                    this.expr.length = otherLengthMax;
                }
                if (this.expr.mmd.max == 0) {
                    this.exb.select(this.expr);
                } else {
                    this.exm.select(this.expr);
                }
            }
        } else if (other.expr.length > 0) {
            this.expr.copy(other.expr);
        }
        this.map.select(other.map);
        this.length.add(other.length);
    }

    public void altMerge(NodeOptInfo other, OptEnvironment env) {
        this.anchor.altMerge(other.anchor);
        this.exb.altMerge(other.exb, env);
        this.exm.altMerge(other.exm, env);
        this.expr.altMerge(other.expr, env);
        this.map.altMerge(other.map);
        this.length.altMerge(other.length);
    }

    public void setBound(MinMaxLen mmd) {
        this.exb.mmd.copy(mmd);
        this.expr.mmd.copy(mmd);
        this.map.mmd.copy(mmd);
    }
}

