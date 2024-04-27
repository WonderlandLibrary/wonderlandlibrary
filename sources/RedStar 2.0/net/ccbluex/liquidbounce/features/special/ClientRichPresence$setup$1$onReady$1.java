// ERROR: Unable to apply inner class name fixup
package net.ccbluex.liquidbounce.features.special;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.features.special.ClientRichPresence;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "invoke"})
static final class ClientRichPresence.setup.onReady.1
extends Lambda
implements Function0<Unit> {
    final ClientRichPresence.setup.1 this$0;

    @Override
    public final void invoke() {
        while (this.this$0.this$0.running) {
            this.this$0.this$0.update();
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    ClientRichPresence.setup.onReady.1(ClientRichPresence.setup.1 var1_1) {
        this.this$0 = var1_1;
        super(0);
    }
}
