package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "run"})
final class HytGetName$onPacket$4
implements Runnable {
    final String $name;

    @Override
    public final void run() {
        try {
            Thread.sleep(10000L);
            LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
            ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d删除无敌人：" + this.$name);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    HytGetName$onPacket$4(String string) {
        this.$name = string;
    }
}
