/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.prism.impl.ps.CachingRoundRectRepState;
import com.sun.prism.impl.ps.CachingShapeRep;
import com.sun.prism.impl.ps.CachingShapeRepState;

public class CachingRoundRectRep
extends CachingShapeRep {
    @Override
    CachingShapeRepState createState() {
        return new CachingRoundRectRepState();
    }
}

