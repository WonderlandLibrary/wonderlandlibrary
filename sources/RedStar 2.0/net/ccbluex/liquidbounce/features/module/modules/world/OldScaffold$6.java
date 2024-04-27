package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.value.BoolValue;

class OldScaffold$6
extends BoolValue {
    OldScaffold$6(String arg0, boolean arg1) {
        super(arg0, arg1);
    }

    @Override
    protected void onChanged(Boolean oldValue, Boolean newValue) {
        if (newValue.booleanValue()) {
            OldScaffold.this.sprintValue.set(false);
        }
    }
}
