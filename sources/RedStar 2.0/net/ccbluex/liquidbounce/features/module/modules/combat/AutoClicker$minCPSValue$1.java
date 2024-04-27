package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import net.ccbluex.liquidbounce.value.IntegerValue;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\n\b\n\b*\u0000\b\n\u000020J02020H¨"}, d2={"net/ccbluex/liquidbounce/features/module/modules/combat/AutoClicker$minCPSValue$1", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "onChanged", "", "oldValue", "", "newValue", "Pride"})
public static final class AutoClicker$minCPSValue$1
extends IntegerValue {
    final AutoClicker this$0;

    @Override
    protected void onChanged(int oldValue, int newValue) {
        int maxCPS = ((Number)this.this$0.maxCPSValue.get()).intValue();
        if (maxCPS < newValue) {
            this.set(maxCPS);
        }
    }

    AutoClicker$minCPSValue$1(AutoClicker $outer, String $super_call_param$1, int $super_call_param$2, int $super_call_param$3, int $super_call_param$4) {
        this.this$0 = $outer;
        super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
    }
}
