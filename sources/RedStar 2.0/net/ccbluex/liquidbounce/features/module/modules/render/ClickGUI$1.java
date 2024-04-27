package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.value.ListValue;

class ClickGUI$1
extends ListValue {
    ClickGUI$1(String arg0, String[] arg1, String arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    protected void onChanged(String oldValue, String newValue) {
        ClickGUI.this.updateStyle();
    }
}
