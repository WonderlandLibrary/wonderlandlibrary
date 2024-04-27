package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.value.IntegerValue;

class Scaffold$3
extends IntegerValue {
    Scaffold$3(String arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    @Override
    protected void onChanged(Integer oldValue, Integer newValue) {
        if (this.getMaximum() < newValue) {
            this.set(this.getMaximum());
        } else if (this.getMinimum() > newValue) {
            this.set(this.getMinimum());
        }
    }
}
