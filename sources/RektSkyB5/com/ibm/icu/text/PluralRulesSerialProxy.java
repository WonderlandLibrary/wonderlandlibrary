/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.PluralRules;
import java.io.ObjectStreamException;
import java.io.Serializable;

class PluralRulesSerialProxy
implements Serializable {
    private static final long serialVersionUID = 42L;
    private final String data;

    PluralRulesSerialProxy(String rules) {
        this.data = rules;
    }

    private Object readResolve() throws ObjectStreamException {
        return PluralRules.createRules(this.data);
    }
}

