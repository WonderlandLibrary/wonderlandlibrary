package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;

class Spammer$2
extends IntegerValue {
    Spammer$2(String arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    @Override
    protected void onChanged(Integer oldValue, Integer newValue) {
        int maxDelayValueObject = (Integer)Spammer.this.maxDelayValue.get();
        if (maxDelayValueObject < newValue) {
            this.set(maxDelayValueObject);
        }
        Spammer.this.delay = TimeUtils.randomDelay((Integer)Spammer.this.minDelayValue.get(), (Integer)Spammer.this.maxDelayValue.get());
    }
}
