// ERROR: Unable to apply inner class name fixup
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoPlay;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000*\u0000\b\n\u000020J\b0H¨¸\u0000"}, d2={"kotlin/concurrent/TimersKt$timerTask$1", "Ljava/util/TimerTask;", "run", "", "kotlin-stdlib"})
public static final class AutoPlay$queueAutoPlay$.inlined.schedule.1
extends TimerTask {
    final AutoPlay this$0;
    final Function0 $runnable$inlined;

    public AutoPlay$queueAutoPlay$.inlined.schedule.1(AutoPlay autoPlay, Function0 function0) {
        this.this$0 = autoPlay;
        this.$runnable$inlined = function0;
    }

    @Override
    public void run() {
        TimerTask $this$schedule = this;
        boolean bl = false;
        if (this.this$0.getState()) {
            this.$runnable$inlined.invoke();
        }
    }
}
