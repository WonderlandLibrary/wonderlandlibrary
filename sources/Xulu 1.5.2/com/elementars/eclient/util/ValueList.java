// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.Iterator;
import dev.xulu.settings.Bind;
import dev.xulu.settings.Value;
import java.util.ArrayList;

public class ValueList extends ArrayList<Value<?>>
{
    @Override
    public boolean isEmpty() {
        if (super.isEmpty()) {
            return true;
        }
        boolean test = true;
        for (final Value<?> value : this) {
            if (!(value.getValue() instanceof Bind)) {
                test = false;
                break;
            }
        }
        return test;
    }
}
