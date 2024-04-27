package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import net.ccbluex.liquidbounce.value.BoolValue;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\n\n\b*\u0000\b\n\u000020J02020H¨"}, d2={"net/ccbluex/liquidbounce/features/module/modules/misc/LiquidChat$jwtValue$1", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onChanged", "", "oldValue", "", "newValue", "Pride"})
public static final class LiquidChat$jwtValue$1
extends BoolValue {
    final LiquidChat this$0;

    @Override
    protected void onChanged(boolean oldValue, boolean newValue) {
        if (this.this$0.getState()) {
            this.this$0.setState(false);
            this.this$0.setState(true);
        }
    }

    LiquidChat$jwtValue$1(LiquidChat $outer, String $super_call_param$1, boolean $super_call_param$2) {
        this.this$0 = $outer;
        super($super_call_param$1, $super_call_param$2);
    }
}
