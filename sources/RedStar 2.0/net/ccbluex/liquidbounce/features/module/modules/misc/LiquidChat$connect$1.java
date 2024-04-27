package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "invoke"})
final class LiquidChat$connect$1
extends Lambda
implements Function0<Unit> {
    final LiquidChat this$0;

    @Override
    public final void invoke() {
        try {
            this.this$0.getClient().connect();
            if (((Boolean)this.this$0.getJwtValue().get()).booleanValue()) {
                this.this$0.getClient().loginJWT(LiquidChat.Companion.getJwtToken());
            } else if (UserUtils.INSTANCE.isValidToken(MinecraftInstance.mc.getSession().getToken())) {
                this.this$0.getClient().loginMojang();
            }
        }
        catch (Exception cause) {
            ClientUtils.getLogger().error("LiquidChat error", (Throwable)cause);
            ClientUtils.displayChatMessage("§7[§a§lChat§7] §cError: §7" + cause.getClass().getName() + ": " + cause.getMessage());
        }
        this.this$0.loginThread = null;
    }

    LiquidChat$connect$1(LiquidChat liquidChat) {
        this.this$0 = liquidChat;
        super(0);
    }
}
