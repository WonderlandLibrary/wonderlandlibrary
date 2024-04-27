package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.value.IntegerValue;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\n\b\n\b*\u0000\b\n\u000020J02020H¨"}, d2={"net/ccbluex/liquidbounce/features/module/modules/player/InventoryCleaner$minDelayValue$1", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "onChanged", "", "oldValue", "", "newValue", "Pride"})
public static final class InventoryCleaner$minDelayValue$1
extends IntegerValue {
    final InventoryCleaner this$0;

    @Override
    protected void onChanged(int oldValue, int newValue) {
        int maxDelay = ((Number)this.this$0.maxDelayValue.get()).intValue();
        if (maxDelay < newValue) {
            this.set(maxDelay);
        }
    }

    InventoryCleaner$minDelayValue$1(InventoryCleaner $outer, String $super_call_param$1, int $super_call_param$2, int $super_call_param$3, int $super_call_param$4) {
        this.this$0 = $outer;
        super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
    }
}
