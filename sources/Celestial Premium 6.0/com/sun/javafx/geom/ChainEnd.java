/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.CurveLink;

final class ChainEnd {
    CurveLink head;
    CurveLink tail;
    ChainEnd partner;
    int etag;

    public ChainEnd(CurveLink curveLink, ChainEnd chainEnd) {
        this.head = curveLink;
        this.tail = curveLink;
        this.partner = chainEnd;
        this.etag = curveLink.getEdgeTag();
    }

    public CurveLink getChain() {
        return this.head;
    }

    public void setOtherEnd(ChainEnd chainEnd) {
        this.partner = chainEnd;
    }

    public ChainEnd getPartner() {
        return this.partner;
    }

    public CurveLink linkTo(ChainEnd chainEnd) {
        ChainEnd chainEnd2;
        ChainEnd chainEnd3;
        ChainEnd chainEnd4;
        if (this.etag == 0 || chainEnd.etag == 0) {
            throw new InternalError("ChainEnd linked more than once!");
        }
        if (this.etag == chainEnd.etag) {
            throw new InternalError("Linking chains of the same type!");
        }
        if (this.etag == 1) {
            chainEnd4 = this;
            chainEnd3 = chainEnd;
        } else {
            chainEnd4 = chainEnd;
            chainEnd3 = this;
        }
        this.etag = 0;
        chainEnd.etag = 0;
        chainEnd4.tail.setNext(chainEnd3.head);
        chainEnd4.tail = chainEnd3.tail;
        if (this.partner == chainEnd) {
            return chainEnd4.head;
        }
        ChainEnd chainEnd5 = chainEnd3.partner;
        chainEnd5.partner = chainEnd2 = chainEnd4.partner;
        chainEnd2.partner = chainEnd5;
        if (chainEnd4.head.getYTop() < chainEnd5.head.getYTop()) {
            chainEnd4.tail.setNext(chainEnd5.head);
            chainEnd5.head = chainEnd4.head;
        } else {
            chainEnd2.tail.setNext(chainEnd4.head);
            chainEnd2.tail = chainEnd4.tail;
        }
        return null;
    }

    public void addLink(CurveLink curveLink) {
        if (this.etag == 1) {
            this.tail.setNext(curveLink);
            this.tail = curveLink;
        } else {
            curveLink.setNext(this.head);
            this.head = curveLink;
        }
    }

    public double getX() {
        if (this.etag == 1) {
            return this.tail.getXBot();
        }
        return this.head.getXBot();
    }
}

