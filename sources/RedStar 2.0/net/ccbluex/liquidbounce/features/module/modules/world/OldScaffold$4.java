package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.value.FloatValue;

class OldScaffold$4
extends FloatValue {
    OldScaffold$4(String arg0, float arg1, float arg2, float arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    @Override
    protected void onChanged(Float oldValue, Float newValue) {
        float v = ((Float)OldScaffold.this.minTurnSpeedValue.get()).floatValue();
        if (v > newValue.floatValue()) {
            this.set(Float.valueOf(v));
        }
        if (this.getMaximum() < newValue.floatValue()) {
            this.set(Float.valueOf(this.getMaximum()));
        } else if (this.getMinimum() > newValue.floatValue()) {
            this.set(Float.valueOf(this.getMinimum()));
        }
    }
}
