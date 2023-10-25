/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.prism.impl.ps.CachingEllipseRepState;
import com.sun.prism.impl.ps.CachingShapeRep;
import com.sun.prism.impl.ps.CachingShapeRepState;

public class CachingEllipseRep
extends CachingShapeRep {
    @Override
    CachingShapeRepState createState() {
        return new CachingEllipseRepState();
    }
}

