package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.value.FloatValue;

class Scaffold$4
extends FloatValue {
    Scaffold$4(String arg0, float arg1, float arg2, float arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    @Override
    protected void onChanged(Float oldValue, Float newValue) {
        float i = ((Float)Scaffold.this.minTurnSpeed.get()).floatValue();
        if (i > newValue.floatValue()) {
            this.set(Float.valueOf(i));
        }
    }
}
