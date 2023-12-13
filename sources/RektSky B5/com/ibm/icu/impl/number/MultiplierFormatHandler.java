/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.DecimalQuantity;
import com.ibm.icu.impl.number.MicroProps;
import com.ibm.icu.impl.number.MicroPropsGenerator;
import com.ibm.icu.number.Scale;

public class MultiplierFormatHandler
implements MicroPropsGenerator {
    final Scale multiplier;
    final MicroPropsGenerator parent;

    public MultiplierFormatHandler(Scale multiplier, MicroPropsGenerator parent) {
        this.multiplier = multiplier;
        this.parent = parent;
    }

    @Override
    public MicroProps processQuantity(DecimalQuantity quantity) {
        MicroProps micros = this.parent.processQuantity(quantity);
        this.multiplier.applyTo(quantity);
        return micros;
    }
}

