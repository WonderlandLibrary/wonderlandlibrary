package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.value.IntegerValue;

class OldScaffold$1
extends IntegerValue {
    OldScaffold$1(String arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    @Override
    protected void onChanged(Integer oldValue, Integer newValue) {
        int i = (Integer)OldScaffold.this.minDelayValue.get();
        if (i > newValue) {
            this.set(i);
        }
    }
}
