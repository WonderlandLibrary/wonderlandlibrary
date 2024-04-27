package net.ccbluex.liquidbounce.features.special;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.features.special.ClientRichPresence;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b*\u0000\b\n\u000020J02\b02\b0HJ\b02\b0H¨\t"}, d2={"net/ccbluex/liquidbounce/features/special/ClientRichPresence$setup$1", "Lcom/jagrosh/discordipc/IPCListener;", "onClose", "", "client", "Lcom/jagrosh/discordipc/IPCClient;", "json", "Lorg/json/JSONObject;", "onReady", "Pride"})
public static final class ClientRichPresence$setup$1
implements IPCListener {
    final ClientRichPresence this$0;

    @Override
    public void onReady(@Nullable IPCClient client) {
        ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this){
            final ClientRichPresence$setup$1 this$0;

            public final void invoke() {
                while (ClientRichPresence.access$getRunning$p(this.this$0.this$0)) {
                    this.this$0.this$0.update();
                    try {
                        Thread.sleep(1000L);
                    }
                    catch (InterruptedException interruptedException) {}
                }
            }
            {
                this.this$0 = var1_1;
                super(0);
            }
        }, 31, null);
    }

    @Override
    public void onClose(@Nullable IPCClient client, @Nullable JSONObject json) {
        this.this$0.running = false;
    }

    ClientRichPresence$setup$1(ClientRichPresence $outer) {
        this.this$0 = $outer;
    }
}
