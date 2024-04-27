package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;

class Spammer$1
extends IntegerValue {
    Spammer$1(String arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    @Override
    protected void onChanged(Integer oldValue, Integer newValue) {
        int minDelayValueObject = (Integer)Spammer.this.minDelayValue.get();
        if (minDelayValueObject > newValue) {
            this.set(minDelayValueObject);
        }
        Spammer.this.delay = TimeUtils.randomDelay((Integer)Spammer.this.minDelayValue.get(), (Integer)Spammer.this.maxDelayValue.get());
    }
}
