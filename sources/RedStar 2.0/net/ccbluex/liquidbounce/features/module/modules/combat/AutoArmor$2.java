package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.value.IntegerValue;

class AutoArmor$2
extends IntegerValue {
    AutoArmor$2(String arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    @Override
    protected void onChanged(Integer oldValue, Integer newValue) {
        int minDelay = (Integer)AutoArmor.this.minDelayValue.get();
        if (minDelay > newValue) {
            this.set(minDelay);
        }
    }
}
