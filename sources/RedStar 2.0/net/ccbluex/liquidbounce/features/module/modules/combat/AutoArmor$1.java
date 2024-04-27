package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.value.IntegerValue;

class AutoArmor$1
extends IntegerValue {
    AutoArmor$1(String arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    @Override
    protected void onChanged(Integer oldValue, Integer newValue) {
        int maxDelay = (Integer)AutoArmor.this.maxDelayValue.get();
        if (maxDelay < newValue) {
            this.set(maxDelay);
        }
    }
}
