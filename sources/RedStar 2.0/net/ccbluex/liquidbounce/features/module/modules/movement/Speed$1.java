package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.value.ListValue;

class Speed$1
extends ListValue {
    Speed$1(String arg0, String[] arg1, String arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    protected void onChange(String oldValue, String newValue) {
        if (Speed.this.getState()) {
            Speed.this.onDisable();
        }
    }

    @Override
    protected void onChanged(String oldValue, String newValue) {
        if (Speed.this.getState()) {
            Speed.this.onEnable();
        }
    }
}
