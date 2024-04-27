package net.ccbluex.liquidbounce;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "invoke"})
final class LiquidBounce$startClient$1
extends Lambda
implements Function0<Unit> {
    public static final LiquidBounce$startClient$1 INSTANCE = new /* invalid duplicate definition of identical inner class */;

    @Override
    public final void invoke() {
        try {
            LiquidBounce.INSTANCE.getClientRichPresence().setup();
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to setup Discord RPC.", throwable);
        }
    }

    LiquidBounce$startClient$1() {
    }
}
